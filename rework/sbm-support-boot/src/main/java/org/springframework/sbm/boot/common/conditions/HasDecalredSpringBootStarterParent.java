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
package org.springframework.sbm.boot.common.conditions;

import org.springframework.sbm.build.api.Module;
import org.springframework.sbm.build.api.BuildFile;
import org.springframework.sbm.build.api.ParentDeclaration;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.Condition;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HasDecalredSpringBootStarterParent implements Condition {
    private Pattern versionPattern = Pattern.compile(".*");

    @Override
    public String getDescription() {
        return String.format("Check if any Build file has a spring-boot-starter-parent as parent with a version matching pattern '%s'.", versionPattern);
    }

    public void setVersionPattern(String versionPattern) {
        this.versionPattern = Pattern.compile(versionPattern);
    }

    @Override
    public boolean evaluate(ProjectContext context) {
        return context.getApplicationModules().stream()
                .map(Module::getBuildFile)
                .filter(BuildFile::hasParent)
                .map(BuildFile::getParentPomDeclaration)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(ParentDeclaration::getVersion)
                .map(versionPattern::matcher)
                .anyMatch(Matcher::matches);
    }
}
