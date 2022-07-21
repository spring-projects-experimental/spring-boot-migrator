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
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class CreateAutoconfigurationAction extends AbstractAction {

    private static final String SPRING_FACTORIES_PATH = "/**/src/main/resources/META-INF/spring.factories";
    private static final String AUTO_CONFIGURATION_IMPORTS = "src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports";

    @Override
    public void apply(ProjectContext context) {

        Optional<String> springAutoConfigProperties = getEnableAutoConfigFromSpringFactories(context);

        if (springAutoConfigProperties.isPresent()) {

            StringProjectResource springAutoconfigurationFile =
                    new StringProjectResource(
                            context.getProjectRootDirectory(),
                            context.getProjectRootDirectory().resolve(AUTO_CONFIGURATION_IMPORTS),
                            springAutoConfigProperties.get()
                    );
            context.getProjectResources().add(springAutoconfigurationFile);
        }
    }

    private Optional<String> getEnableAutoConfigFromSpringFactories(ProjectContext context) {

        List<ProjectResource> search = context.search(
                new PathPatternMatchingProjectResourceFinder(
                        SPRING_FACTORIES_PATH
                ));

        if (search.size() > 0) {
            String oldConfigFile = search.get(0).print();
            Properties prop = new Properties();

            try {
                prop.load(new ByteArrayInputStream(oldConfigFile.getBytes()));
                String content = prop.getProperty("org.springframework.boot.autoconfigure.EnableAutoConfiguration");
                return Optional.ofNullable(content);
            } catch (IOException e) {

                // TODO: Raise event or report
                e.printStackTrace();
            }
        }
        return Optional.empty();
    }
}
