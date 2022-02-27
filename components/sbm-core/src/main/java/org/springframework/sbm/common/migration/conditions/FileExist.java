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
package org.springframework.sbm.common.migration.conditions;

import org.springframework.sbm.engine.recipe.Condition;
import org.springframework.sbm.engine.context.ProjectContext;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileExist implements Condition {

    private String fileName;

    @Override
    public String getDescription() {
        return "an app contains a file '" + fileName + "'";
    }

    @Override
    public boolean evaluate(ProjectContext context) {
        return context.getProjectResources().stream()
                .anyMatch(f -> !f.isDeleted() && fileName.equals(f.getAbsolutePath().getFileName().toString()));
    }
}
