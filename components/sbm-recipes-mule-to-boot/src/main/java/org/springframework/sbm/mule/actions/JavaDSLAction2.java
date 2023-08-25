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
package org.springframework.sbm.mule.actions;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.openrewrite.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.sbm.boot.properties.actions.AddSpringBootApplicationPropertiesAction;
import org.springframework.sbm.boot.properties.api.SpringBootApplicationProperties;
import org.springframework.sbm.boot.properties.search.SpringBootApplicationPropertiesResourceListFilter;
import org.springframework.sbm.build.api.BuildFile;
import org.springframework.sbm.build.api.Dependency;
import org.springframework.sbm.build.api.JavaSourceSet;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.AbstractAction;
import org.springframework.sbm.java.api.JavaSource;
import org.springframework.sbm.java.api.JavaSourceAndType;
import org.springframework.sbm.java.api.Type;
import org.springframework.sbm.mule.api.MuleMigrationContext;
import org.springframework.sbm.mule.api.MuleMigrationContextFactory;
import org.springframework.sbm.mule.api.toplevel.TopLevelElement;
import org.springframework.sbm.mule.api.toplevel.TopLevelElementFactory;
import org.springframework.sbm.mule.api.toplevel.UnknownTopLevelElement;
import org.springframework.sbm.mule.api.toplevel.configuration.ConfigurationTypeAdapter;
import org.springframework.sbm.mule.api.toplevel.configuration.MuleConfigurationsExtractor;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBElement;
import java.util.AbstractMap.SimpleEntry;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JavaDSLAction2 extends AbstractAction {

    private static final String SPRING_CONFIGURATION_ANNOTATION = "org.springframework.context.annotation.Configuration";
    private final MuleMigrationContextFactory muleMigrationContextFactory;
    private final Map<Class<?>, TopLevelElementFactory> topLevelTypeMap;
    private final ExecutionContext executionContext;
    @Setter
    private boolean muleTriggerMeshTransformEnabled;

    @Autowired
    public JavaDSLAction2(MuleMigrationContextFactory muleMigrationContextFactory, List<TopLevelElementFactory> topLevelTypeFactories, ExecutionContext executionContext) {
        topLevelTypeMap = topLevelTypeFactories.stream()
                .collect(Collectors.toMap(TopLevelElementFactory::getSupportedTopLevelType, Function.identity()));
        this.muleMigrationContextFactory = muleMigrationContextFactory;
        this.executionContext = executionContext;
    }

    @Override
    public String getDescription() {
        return "Migrating Mulesoft to Spring Boot";
    }

    @Override
    public void apply(ProjectContext context) {
        BuildFile buildFile = context.getApplicationModules().getRootModule().getBuildFile();
        MuleMigrationContext muleMigrationContext = muleMigrationContextFactory.createMuleMigrationContext(context);
        JavaSourceAndType flowConfigurationSource = findOrCreateFlowConfigurationClass(context);
        handleApplicationConfiguration(context, muleMigrationContext.getMuleConfigurations().getConfigurations(), buildFile);

        startProcess("Converting Mulesoft files");
        handleTopLevelElements(buildFile, muleMigrationContext, flowConfigurationSource, context);
        endProcess();

        // TODO: Spring Beans need to be retrieved as well
//           <spring:beans>
//              <spring:import resource="classpath:com/acme/ecma/bil/mulestack/_security.xml"/>
//              <spring:import resource="classpath:lib-ecma-headers.xml"/>
//              <spring:import resource="classpath:lib-ecma-exceptions.xml"/>
//              <spring:import resource="classpath:nginx.xml"/>
//          </spring:beans>
    }

    private void handleTopLevelElements(BuildFile buildFile, MuleMigrationContext muleMigrationContext, JavaSourceAndType flowConfigurationSource, ProjectContext context) {
        List<TopLevelElement> topLevelElements = new ArrayList<>();
        for (JAXBElement tle : muleMigrationContext.getTopLevelElements()) {
            if (MuleConfigurationsExtractor.isConfigType(tle)) {
                continue;
            }
            if (topLevelTypeMap.containsKey(tle.getValue().getClass())) {
                TopLevelElementFactory tltf = topLevelTypeMap.get(tle.getValue().getClass());
                topLevelElements.add(tltf.buildDefinition(tle, muleMigrationContext.getMuleConfigurations()));
            } else {
                topLevelElements.add(new UnknownTopLevelElement(tle));
            }
        }
        Set<Dependency> dependencies = topLevelElements.stream()
                .map(this::buildDependencies)
                .flatMap(List::stream)
                .collect(Collectors.toSet());
        startProcess("Adding " + dependencies.size() + " dependencies");

        addGenericRequiredDependencies(dependencies);

        buildFile.addDependencies(new ArrayList<>(dependencies));
        endProcess();

        if (muleTriggerMeshTransformEnabled) {
            logEvent("Adding TriggerMesh Dataweave payload class");
            createClass(context, createTmDwPayloadClass(context));
        }

        logEvent("Adding " + topLevelElements.size() + " methods");
        topLevelElements.forEach(topLevelElement -> {
            flowConfigurationSource.getType().addMethod(
                    topLevelElement.renderDslSnippet(),
                    topLevelElement.getRequiredImports()
            );

            if (topLevelElement.hasGeneratedDependentFlows()) {
                topLevelElement
                        .generatedDependentFlows()
                        .forEach(methodContents -> flowConfigurationSource
                                .getType()
                                .addMethod(
                                        methodContents,
                                        topLevelElement.getRequiredImports()
                                )
                        );

            }

            createExternalClasses(context, topLevelElement);
        });
    }

    private void addGenericRequiredDependencies(Set<Dependency> dependencies) {
        dependencies.add(
                Dependency.builder()
                .groupId("org.projectlombok")
                .artifactId("lombok")
                .version("1.18.24")
                .scope("provided")
                .build()
        );
    }

    private void createExternalClasses(ProjectContext context, TopLevelElement topLevelElement) {
        topLevelElement.getExternalClassContents().stream()
                .filter(Predicate.not(StringUtils::isEmpty))
                .forEach(ecc -> {
                    createClass(context, ecc);
                });
    }

    private List<Dependency> buildDependencies(TopLevelElement snippet) {
        return snippet.getRequiredDependencies().stream()
                .map(Dependency::fromCoordinates)
                .collect(Collectors.toList());
    }

    private JavaSourceAndType findOrCreateFlowConfigurationClass(ProjectContext projectContext) {
        Optional<JavaSourceAndType> matchingConfigClass = projectContext.getProjectJavaSources().list().stream()
                .filter(js -> js.getTypes().stream().anyMatch(t -> t.hasAnnotation(SPRING_CONFIGURATION_ANNOTATION)))
                .map(js -> {
                    Type match = js.getTypes().stream().filter(t -> t.hasAnnotation(SPRING_CONFIGURATION_ANNOTATION)).findFirst().get();
                    return new JavaSourceAndType(js, match);
                })
                .findFirst();

        if (matchingConfigClass.isEmpty()) {
            return createConfigurationClass(projectContext, "FlowConfigurations");
        } else {
            return matchingConfigClass.get();
        }
    }

    private JavaSourceAndType createConfigurationClass(ProjectContext projectContext, String className) {
        JavaSourceSet mainJavaSourceSet = projectContext.getApplicationModules().getTopmostApplicationModules().get(0).getMainJavaSourceSet();
        String packageName = mainJavaSourceSet.getJavaSourceLocation().getPackageName();
        String source =
                "package " + packageName + ";\n" +
                        "import " + SPRING_CONFIGURATION_ANNOTATION + ";\n" +
                        "@Configuration\n" +
                        "public class " + className + " {}";
        JavaSource javaSource = mainJavaSourceSet.addJavaSource(projectContext.getProjectRootDirectory(), source, packageName);
        return new JavaSourceAndType(javaSource, javaSource.getTypes().get(0));
    }

    private void createClass(ProjectContext projectContext, String content) {
        JavaSourceSet mainJavaSourceSet = projectContext.getApplicationModules().getTopmostApplicationModules().get(0).getMainJavaSourceSet();
        String packageName = mainJavaSourceSet.getJavaSourceLocation().getPackageName();
        mainJavaSourceSet.addJavaSource(projectContext.getProjectRootDirectory(), content, packageName);
    }

    private void handleApplicationConfiguration(ProjectContext projectContext,
                                                Map<String, ? extends ConfigurationTypeAdapter> configurations,
                                                BuildFile buildFile) {

        SpringBootApplicationProperties defaultProperties = findOrCreateDefaultApplicationProperties(projectContext);

        configurations.values().stream()
                .map(c -> (List<SimpleEntry<String, String>>) c.configProperties())
                .flatMap(List::stream)
                .forEach(e -> defaultProperties.setProperty(e.getKey(), e.getValue()));

        List<Dependency> dependencies = configurations.values().stream()
                .filter(configurationTypeAdapter -> !configurationTypeAdapter.getDependencies().isEmpty())
                .map(k -> (List<Dependency>) k.getDependencies())
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        if (!dependencies.isEmpty()) {
            buildFile.addDependencies(dependencies);
        }
    }

    @NotNull
    private SpringBootApplicationProperties findOrCreateDefaultApplicationProperties(ProjectContext projectContext) {
        List<SpringBootApplicationProperties> bootApplicationProperties = projectContext.search(new SpringBootApplicationPropertiesResourceListFilter());
        if (bootApplicationProperties.isEmpty()) {
            new AddSpringBootApplicationPropertiesAction(executionContext).apply(projectContext);
        }

        return projectContext
                .search(new SpringBootApplicationPropertiesResourceListFilter()).stream()
                .filter(SpringBootApplicationProperties::isDefaultProperties)
                .findFirst()
                .get();
    }

    private String createTmDwPayloadClass(ProjectContext projectContext) {
        JavaSourceSet mainJavaSourceSet = projectContext.getApplicationModules().getTopmostApplicationModules().get(0).getMainJavaSourceSet();
        String packageName = mainJavaSourceSet.getJavaSourceLocation().getPackageName();
        return "package " + packageName + ";\n" +
                        "import " + SPRING_CONFIGURATION_ANNOTATION + ";\n\n" +
                        "import lombok.Data;\n\n" +
                        "/* Included with the baseline to support bridging between the Flow configuration and the translation implementation. */\n\n" +
                        "@Data\n" +
                        "public class TmDwPayload {\n" +
                        "    private String id;\n" +
                        "    private String source;\n" +
                        "    private String sourceType;\n" +
                        "    private String payload;\n" +
                        "}\n";
    }

}
