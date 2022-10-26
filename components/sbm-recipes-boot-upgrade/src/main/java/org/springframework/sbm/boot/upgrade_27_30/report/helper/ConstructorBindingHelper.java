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

import org.openrewrite.ExecutionContext;
import org.openrewrite.TreeVisitor;
import org.openrewrite.internal.lang.Nullable;
import org.openrewrite.java.spring.boot3.RemoveConstructorBindingAnnotation;
import org.openrewrite.java.tree.J;
import org.springframework.sbm.boot.upgrade_27_30.report.SpringBootUpgradeReportSection;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.OpenRewriteSourceFilesFinder;
import org.springframework.sbm.project.resource.RewriteSourceFileHolder;
import org.springframework.sbm.support.openrewrite.GenericOpenRewriteRecipe;

import java.util.List;
import java.util.Map;


public class ConstructorBindingHelper implements SpringBootUpgradeReportSection.Helper<List<String>> {

    private static class ConstructorBindingAnnotationDetector extends RemoveConstructorBindingAnnotation {
        public @Nullable TreeVisitor<?, ExecutionContext> getSingleSourceApplicableTest() {
            return super.getSingleSourceApplicableTest();
        }
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public boolean evaluate(ProjectContext context) {

        ConstructorBindingAnnotationDetector constructorBindingAnnotationDetector =
                new ConstructorBindingAnnotationDetector();

        TreeVisitor<?, ExecutionContext> testVisitor =
                constructorBindingAnnotationDetector.getSingleSourceApplicableTest();
        GenericOpenRewriteRecipe<TreeVisitor<?, ExecutionContext>> recipe =
                new GenericOpenRewriteRecipe<>(() -> testVisitor);

        List<RewriteSourceFileHolder<J.CompilationUnit>> rewriteSourceFileHolders = context.getProjectJavaSources().find(recipe);

        return !rewriteSourceFileHolders.isEmpty();
    }

    @Override
    public Map<String, List<String>> getData(ProjectContext context) {
        return null;
    }
}
