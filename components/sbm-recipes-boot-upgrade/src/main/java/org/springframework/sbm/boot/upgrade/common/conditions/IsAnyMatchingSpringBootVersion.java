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
package org.springframework.sbm.boot.upgrade.common.conditions;

import org.jetbrains.annotations.NotNull;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.Condition;

import javax.validation.constraints.NotEmpty;
import java.util.Arrays;
import java.util.List;

public class IsAnyMatchingSpringBootVersion implements Condition {

    /**
     * VersionPattern will be used for {@code startsWith} check against the version number found.
     */
    @NotNull
    @NotEmpty
    private List<String> versionPatterns;

    @Override
    public String getDescription() {
        return "Check if scanned application is Spring Boot application of given version";
    }

    @Override
    public boolean evaluate(ProjectContext context) {
        return versionPatterns.stream()
                .anyMatch(p ->  new IsMatchingSpringBootVersion(p).evaluate(context));
    }

    public void setVersionPatterns(String versionPatterns) {
        this.versionPatterns = Arrays.asList(versionPatterns.split(","));
    }
}
