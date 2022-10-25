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

package org.springframework.sbm.boot.upgrade_27_30.report.helper;

import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.RecipeRun;
import org.openrewrite.SourceFile;
import org.openrewrite.java.spring.boot3.RemoveConstructorBindingAnnotation;
import org.springframework.sbm.boot.upgrade_27_30.report.SpringBootUpgradeReportSection;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.OpenRewriteSourceFilesFinder;

import java.util.List;
import java.util.Map;

public class ConstructorBindingHelper implements SpringBootUpgradeReportSection.Helper<List<String>> {
    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public boolean evaluate(ProjectContext context) {

        List<? extends SourceFile> rewriteSourceFiles =
                context.search(new OpenRewriteSourceFilesFinder());
        RemoveConstructorBindingAnnotation recipe = new RemoveConstructorBindingAnnotation();

        RecipeRun results = recipe.run(rewriteSourceFiles, new InMemoryExecutionContext(
                (t) -> {
                    throw new RuntimeException(t);
                }
        ));


        return !results.getResults().isEmpty();
    }

    @Override
    public Map<String, List<String>> getData(ProjectContext context) {
        return null;
    }
}
