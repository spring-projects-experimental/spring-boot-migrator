/*
 * Copyright 2021 - 2022 the original author or authors.
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

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
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
import org.springframework.sbm.mule.api.*;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBElement;
import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
@NoArgsConstructor
@AllArgsConstructor
public class JavaDSLAction2 extends AbstractAction {

    private static final String SPRING_CONFIGURATION_ANNOTATION = "org.springframework.context.annotation.Configuration";
    @Autowired
    @Setter
    private MuleMigrationContextFactory muleMigrationContextFactory;

    @Autowired
    @Setter
    private FlowHandler flowHandler;

    @Override
    public void apply(ProjectContext context) {

        BuildFile buildFile = context.getApplicationModules().getRootModule().getBuildFile();
        MuleMigrationContext muleMigrationContext = muleMigrationContextFactory.createMuleMigrationContext(context);

        JavaSourceAndType flowConfigurationSource = findOrCreateFlowConfigurationClass(context);

        List<JAXBElement> topLevelElements = getTopLevelElements(muleMigrationContext);

        createJavaResource(context, muleMigrationContext.getMuleConfigurations().getConfigurations());

        startProcess("Handle sub-flow elements");
        handleSubflows(muleMigrationContext, flowConfigurationSource);
        endProcess();

        startProcess("Handle flow elements");
        handleFlows(buildFile, muleMigrationContext, flowConfigurationSource, topLevelElements);
        endProcess();

        // TODO: Spring Beans need to be retrieved as well
//           <spring:beans>
//              <spring:import resource="classpath:com/acme/ecma/bil/mulestack/_security.xml"/>
//              <spring:import resource="classpath:lib-ecma-headers.xml"/>
//              <spring:import resource="classpath:lib-ecma-exceptions.xml"/>
//              <spring:import resource="classpath:nginx.xml"/>
//          </spring:beans>
    }

    private void handleFlows(BuildFile buildFile, MuleMigrationContext muleMigrationContext, JavaSourceAndType flowConfigurationSource, List<JAXBElement> topLevelElements) {
        topLevelElements.forEach(o -> {
            // either create a method per flow and add the method and imports to the class
            DefinitionSnippet snippet = flowHandler.transformFlow(muleMigrationContext, o);

            List<Dependency> dependencies = snippet.getRequiredDependencies().stream()
                    .map(r -> r.split(":"))
                    .filter(p -> p.length == 3)
                    .map(p -> Dependency.builder().groupId(p[0]).artifactId(p[1]).version(p[2]).build())
                    .collect(Collectors.toList());

            buildFile.addDependencies(dependencies);
            flowConfigurationSource.getType().addMethod(snippet.renderDslSnippet(), snippet.getRequiredImports());
        });
    }

    private void handleSubflows( MuleMigrationContext muleMigrationContext, JavaSourceAndType flowConfigurationSource) {
        if (muleMigrationContext.getAvailableMuleSubFlows().size() > 0) {

            muleMigrationContext.getAvailableMuleSubFlows()
                    .forEach(subFlowType -> {
                                BaseDefinitionSnippet snippet = flowHandler.transformSubflow(muleMigrationContext, subFlowType);
                                flowConfigurationSource.getType().addMethod(snippet.renderDslSnippet(), snippet.getRequiredImports());
                            }
                    );
        }
    }


    private List<JAXBElement> getTopLevelElements(MuleMigrationContext muleMigrationContext) {
        Stream<JAXBElement> a = muleMigrationContext.getAvailableFlows().stream().filter(Objects::nonNull).map(JAXBElement.class::cast);
        Stream<JAXBElement> b = muleMigrationContext.getNonSupportedTypes().stream().filter(Objects::nonNull).map(JAXBElement.class::cast);
        return Stream.concat(a, b)
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
                        "public class "+className+" {}";
        JavaSource javaSource = mainJavaSourceSet.addJavaSource(projectContext.getProjectRootDirectory(), source, packageName);
        return new JavaSourceAndType(javaSource, javaSource.getTypes().get(0));
    }

    // TODO: fina a cohesive name
    private void createJavaResource(ProjectContext projectContext,
                                    Map<String, ? extends ConfigurationTypeAdapter> configurations) {

        SpringBootApplicationProperties defaultProperties = findOrCreateDefaultApplicationProperties(projectContext);

        configurations.values().stream()
                .map(c -> (List<SimpleEntry<String, String>>) c.configProperties())
                .flatMap(List::stream)
                .forEach(e -> defaultProperties.setProperty(e.getKey(), e.getValue()));
    }

    @NotNull
    private SpringBootApplicationProperties findOrCreateDefaultApplicationProperties(ProjectContext projectContext) {
        List<SpringBootApplicationProperties> bootApplicationProperties = projectContext.search(new SpringBootApplicationPropertiesResourceListFilter());
        if(bootApplicationProperties.isEmpty()) {
            new AddSpringBootApplicationPropertiesAction().apply(projectContext);
        }
        SpringBootApplicationProperties defaultProperties = projectContext.search(new SpringBootApplicationPropertiesResourceListFilter()).stream().filter(SpringBootApplicationProperties::isDefaultProperties).findFirst().get();
        return defaultProperties;
    }
}
