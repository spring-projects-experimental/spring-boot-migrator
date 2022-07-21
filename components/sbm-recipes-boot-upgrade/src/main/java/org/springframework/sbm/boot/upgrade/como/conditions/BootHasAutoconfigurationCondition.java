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
package org.springframework.sbm.boot.upgrade.como.conditions;

import org.springframework.sbm.common.filter.PathPatternMatchingProjectResourceFinder;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.Condition;

public class BootHasAutoconfigurationCondition implements Condition {

    public static final String AUTO_CONFIGURATION_PROPERTY = "org.springframework.boot.autoconfigure.EnableAutoConfiguration";

    @Override
    public String getDescription() {
        return "Check if there is autoconfiguration";
    }

    @Override
    public boolean evaluate(ProjectContext context) {

        return context
                .search(new PathPatternMatchingProjectResourceFinder("/**/src/main/resources/META-INF/spring.factories")).stream()
                .anyMatch(r -> r.print().contains(AUTO_CONFIGURATION_PROPERTY));
        //        try (Stream<Path> walkStream = Files.walk(context.getProjectRootDirectory())) {
//            return walkStream.filter(p -> p.toFile().isFile())
//                .map(Path::toAbsolutePath)
//                .filter(CreateAutoconfigurationAction::isSpringFactory)
//                .anyMatch(not(path -> Files.exists(getAutoConfigurationPath(path))));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }
}
