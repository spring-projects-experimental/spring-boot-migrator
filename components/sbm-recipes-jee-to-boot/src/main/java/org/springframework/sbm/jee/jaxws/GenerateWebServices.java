/*
 * Copyright 2021 - 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.sbm.jee.jaxws;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.openrewrite.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.sbm.build.api.Module;
import org.springframework.sbm.build.impl.OpenRewriteMavenPlugin;
import org.springframework.sbm.build.impl.OpenRewriteMavenPlugin.OpenRewriteMavenPluginExecution;
import org.springframework.sbm.engine.recipe.AbstractAction;
import org.springframework.sbm.engine.recipe.UserInteractions;
import org.springframework.sbm.java.api.*;
import org.springframework.sbm.java.migration.conditions.HasAnnotation;
import org.springframework.sbm.engine.context.ProjectContext;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.FilenameUtils;
import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.Parser;
import org.openrewrite.xml.XmlParser;
import org.openrewrite.xml.tree.Xml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author Alex Boyko
 */
public class GenerateWebServices extends AbstractAction {

    private static final String PROPERTY_KEY_JAVA_GEN_FOLDER = "generated-sources.dir";
    private static final String PROPERTY_VALUE_JAVA_GEN_FOLDER = "src/generated";
    public static final String QUESTION = "Provide the path to WSDL file for Web Service '%s'";

    @JsonIgnore
    private final UserInteractions ui;

    @JsonIgnore
    private final Configuration configuration;

    @Autowired
    @JsonIgnore
    private ExecutionContext executionContext;

    public GenerateWebServices(UserInteractions ui, Configuration configuration) {
        this.ui = ui;
        this.configuration = configuration;
        setCondition(HasAnnotation.builder().annotation(WebServiceDescriptor.WEB_SERVICE_ANNOTATION).build());
        setDescription("Generate XML message schema file from Jax-WS Web-Service annotated types");
    }

    @Override
    public void apply(ProjectContext context) {
        // TODO: find applicable module here
        context.getApplicationModules().stream()
                .forEach(module -> {
                    List<WebServiceDescriptor> wsDescriptors = module.getMainJavaSourceSet().list().stream()
                            .flatMap(js -> js.getTypes().stream())
                            .filter(c -> c.hasAnnotation(WebServiceDescriptor.WEB_SERVICE_ANNOTATION))
                            .filter(c -> c.getKind() == KindOfType.CLASS)
                            .map(typeAnnotatedAsWebService -> this.processType(module, typeAnnotatedAsWebService))
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());

                    if (!wsDescriptors.isEmpty()) {

                        generateWebConfig(module, wsDescriptors);

                        List<WebServiceDescriptor> noPojoPresentWsDescriptors = wsDescriptors.stream()
                                .filter(d -> !d.isPojoCodePresent(module))
                                .collect(Collectors.toList());

                        if (!noPojoPresentWsDescriptors.isEmpty()) {
                            addMavenPluginForJavaSourceGeneration(module, noPojoPresentWsDescriptors);
                        }

                        wsDescriptors.forEach(wsd -> {
                            wsd.generateEndpoint(module);
                            wsd.removeJeeAnnotations();
                        });
                    }
                });
    }

    private void addMavenPluginForJavaSourceGeneration(Module module, List<WebServiceDescriptor> descriptors) {
        module.getBuildFile().setProperty(PROPERTY_KEY_JAVA_GEN_FOLDER, PROPERTY_VALUE_JAVA_GEN_FOLDER);

        List<OpenRewriteMavenPluginExecution> generateExecs = descriptors.stream().map(d -> OpenRewriteMavenPluginExecution.builder()
                .goal("generate")
                .configuration("\n<configuration>\n" +
                        "<schemaDirectory>${project.basedir}/src/main/resources</schemaDirectory>\n" +
                        "<schemaIncludes>*.wsdl</schemaIncludes>\n" +
                        "<generateDirectory>${generated-sources.dir}</generateDirectory>\n" +
                        "<generatePackage>" + d.getPackageName() + "</generatePackage>" +
                        "</configuration>\n")
                .build()
        ).collect(Collectors.toList());

        module.getBuildFile().addPlugin(
                OpenRewriteMavenPlugin.builder()
                        .groupId("org.jvnet.jaxb2.maven2")
                        .artifactId("maven-jaxb2-plugin")
                        .version("0.14.0")
                        .executions(generateExecs)
                        .build()
        );

        module.getBuildFile().addPlugin(
                OpenRewriteMavenPlugin.builder()
                        .groupId("org.codehaus.mojo")
                        .artifactId("build-helper-maven-plugin")
                        .execution(OpenRewriteMavenPluginExecution.builder()
                                .goal("add-source")
                                .phase("generate-sources")
                                .configuration("\n<configuration>\n" +
                                        "<sources>\n" +
                                        "<source>${generated-sources.dir}</source>\n" +
                                        "</sources>\n" +
                                        "</configuration>\n")
                                .build()
                        )
                        .build()
        );
    }

    private void generateWebConfig(Module module, List<WebServiceDescriptor> wsDescriptors) {
        JavaSourceLocation location = module.getMainJavaSourceSet().getJavaSourceLocation();

        Map<String, Object> params = new HashMap<>();
        params.put("packageName", location.getPackageName());
        params.put("className", "WebServiceConfig");
        params.put("wsdls", wsDescriptors.stream()
                .map(d -> {
                    Map<String, String> props = new HashMap<>();
                    String fileName = d.getWsdl().getSourcePath().getFileName().toString();
                    props.put("file", fileName);
                    props.put("methodName", FilenameUtils.removeExtension(fileName));
                    props.put("beanName", d.getWsdlDefBeanName());
                    props.put("contextPath", d.getPathContext());
                    return props;
                })
                .collect(Collectors.toList())
        );

        StringWriter writer = new StringWriter();
        try {
            Template template = configuration.getTemplate("jaxws-web-config.ftl");
            template.process(params, writer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String src = writer.toString();
        module.getMainJavaSourceSet().addJavaSource(module.getProjectRootDirectory(), location.getSourceFolder(), src, location.getPackageName());
    }

    private WebServiceDescriptor processType(Module module, Type typeAnnotatedAsWebService) {
        Type effectiveType = getEndpointInterfaceTypeType(module, typeAnnotatedAsWebService);
        Xml.Document wsdl = createWsdlFile(module, typeAnnotatedAsWebService);
        if (wsdl != null) {
            return new WebServiceDescriptor(typeAnnotatedAsWebService, effectiveType, wsdl, configuration);
        } else {
            System.out.println("Skipping Web Service '" + effectiveType.getSimpleName() + "'...");
            return null;
        }
    }

    private Xml.Document createWsdlFile(Module module, Type type) {
        Path p = null;
        String input = null;
        while (p == null) {
            input = ui.askForInput(String.format(QUESTION, type.getSimpleName()));
            if (input.isEmpty()) {
                return null;
            }
            p = Path.of(input);
            try {
                Xml.Document doc = parseWsdl(p, executionContext);
                if (doc.getRoot() == null) {
                    throw new Exception("Invalid WSDL file. It has either invalid or no XML content.");
                }
                String filename = type.getSimpleName().toLowerCase() + ".wsdl";
                module.getMainResourceSet().addStringResource(filename, doc.printAll());
                return doc;
            } catch (Exception e) {
                // TODO: Replace System.out.println, see #175
                System.out.println("Error processing WSDL file '" + p + "'. " + e.getMessage() + "\nTry entering file path again or press 'Enter' to cancel migrating this Web Service");
                p = null;
            }
        }
        throw new RuntimeException(String.format("Could not create WSDL file from given input '%s'", input));
    }

    public static Xml.Document parseWsdl(Path p, ExecutionContext executionContext) {
        List<Xml.Document> docs = new XmlParser() {
            public boolean accept(Path path) {
                return path.toString().endsWith(".wsdl");
            }
        }.parseInputs(Collections.singleton(new Parser.Input(p, () -> {
            try {
                return Files.newInputStream(p);
            } catch (IOException e) {
                return new ByteArrayInputStream(new byte[0]);
            }
        })), null, executionContext)
        .map(Xml.Document.class::cast)
        .toList();

        if (docs.isEmpty()) {
            throw new RuntimeException("Failed to parse XML file '" + p + "'");
        } else {
            return docs.get(0);
        }
    }

    private Type getEndpointInterfaceTypeType(Module module, Type type) {
        Annotation annotation = type.getAnnotations().stream().filter(a -> WebServiceDescriptor.WEB_SERVICE_ANNOTATION.equals(a.getFullyQualifiedName())).findFirst().get();
        Expression endPointInterfaceExpression = annotation.getAttribute("endpointInterface");
        if (endPointInterfaceExpression != null) {
            String endpointInterfaceFqName = endPointInterfaceExpression.getAssignmentRightSide().print().replace("\"", "");
            return new SuperTypeHierarchy(type).getRoot().getSuperTypes().stream()
                    .map(shn -> shn.getNode())
                    .filter(t -> t.getKind() == KindOfType.INTERFACE)
                    .filter(t -> endpointInterfaceFqName.equals(t.getFullyQualifiedName()))
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

}
