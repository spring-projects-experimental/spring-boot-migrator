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
package org.springframework.sbm.boot.upgrade.common.actions;

import org.springframework.sbm.common.filter.PathPatternMatchingProjectResourceFinder;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.AbstractAction;
import org.springframework.sbm.project.resource.ProjectResource;
import org.springframework.sbm.project.resource.StringProjectResource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.regex.Pattern;

public class CreateAutoconfigurationAction extends AbstractAction {

    private static final String SPRING_FACTORIES_PATH = "/**/src/main/resources/META-INF/spring.factories";
    private static final String SPRING_FACTORIES_FILE = "src/main/resources/META-INF/spring.factories";
    private static final String AUTO_CONFIGURATION_IMPORTS = "src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports";
    public static final String ENABLE_AUTO_CONFIGURATION_KEY = "org.springframework.boot.autoconfigure.EnableAutoConfiguration";
    public static final Pattern COMMENT_REGEX = Pattern.compile("^#.*(\r|\n)+");

    @Override
    public void apply(ProjectContext context) {
        Optional<Properties> props = getSpringFactoriesProperties(context);
        if (props.isEmpty()) {
            return;
        }

        Optional<String> springAutoConfigProperties = getEnableAutoConfigFromSpringFactories(props.get());

        if (springAutoConfigProperties.isPresent()) {

            String autoConfigString = springAutoConfigProperties
                    .get()
                    .replaceAll(",", "\\\n");

            StringProjectResource springAutoconfigurationFile =
                    new StringProjectResource(
                            context.getProjectRootDirectory(),
                            context.getProjectRootDirectory().resolve(AUTO_CONFIGURATION_IMPORTS),
                            autoConfigString
                    );
            context.getProjectResources().add(springAutoconfigurationFile);

            removeAutoConfigKeyFromSpringFactories(props.get(), context);
        }
    }

    private void removeAutoConfigKeyFromSpringFactories(Properties props, ProjectContext context) {
        try {
            props.remove(ENABLE_AUTO_CONFIGURATION_KEY);
            StringWriter stringWriter = new StringWriter();
            props.store(stringWriter, null);
            String propertiesWithoutComment = COMMENT_REGEX.matcher(stringWriter.toString()).replaceAll("");

            StringProjectResource springUpdatedSpringFactories =
                    new StringProjectResource(
                            context.getProjectRootDirectory(),
                            context.getProjectRootDirectory().resolve(SPRING_FACTORIES_FILE),
                            propertiesWithoutComment
                    );
            context.getProjectResources().replace(
                    context.getProjectRootDirectory().resolve(SPRING_FACTORIES_FILE),
                    springUpdatedSpringFactories);
        } catch (IOException e) {
            // TODO: Raise event or report
            e.printStackTrace();
        }
    }

    private Optional<String> getEnableAutoConfigFromSpringFactories(Properties props) {
        String content = props.getProperty(ENABLE_AUTO_CONFIGURATION_KEY);
        return Optional.ofNullable(content);
    }

    private Optional<Properties> getSpringFactoriesProperties(ProjectContext context) {
        List<ProjectResource> search = context.search(
                new PathPatternMatchingProjectResourceFinder(
                        SPRING_FACTORIES_PATH
                ));

        if (search.size() > 0) {
            String oldConfigFile = search.get(0).print();
            Properties prop = new Properties();

            try {
                prop.load(new ByteArrayInputStream(oldConfigFile.getBytes()));
                return Optional.of(prop);
            } catch (IOException e) {
                // TODO: Raise event or report
                e.printStackTrace();
            }
        }
        return Optional.empty();
    }
}
