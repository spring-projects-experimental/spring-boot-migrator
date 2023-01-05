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
package org.springframework.sbm.build.impl;

import lombok.RequiredArgsConstructor;
import org.openrewrite.Recipe;
import org.openrewrite.Result;
import org.openrewrite.maven.MavenParser;
import org.openrewrite.maven.MavenVisitor;
import org.openrewrite.xml.tree.Xml;
import org.springframework.sbm.openrewrite.RewriteExecutionContext;
import org.springframework.sbm.project.resource.RewriteSourceFileHolder;
import org.springframework.sbm.support.openrewrite.GenericOpenRewriteRecipe;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
class MavenBuildFileRefactoring<P> {

    private final RewriteSourceFileHolder<Xml.Document> pom;

    public void execute(MavenVisitor... visitors) {
        List<Result> results = Arrays.stream(visitors)
                .map(v -> new GenericOpenRewriteRecipe(() -> v))
                .map(this::executeRecipe)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        processResults(results);
    }

    public void execute(Recipe... visitors) {
        for (Recipe recipe : visitors) {
            List<Result> results = executeRecipe(recipe);
            processResults(results);
        }
    }

    private List<Result> executeRecipe(Recipe recipe) {
        List<Result> results = recipe.run(List.of(pom.getSourceFile()), new RewriteExecutionContext()).getResults();
        return results;
    }

    private void processResults(List<Result> results) {
        if (!results.isEmpty()) {
			// FIXME: Works only on a single POM and does not apply to all other resources
			pom.replaceWith((Xml.Document) results.get(0).getAfter());
           // results.forEach(c -> processResult(c));
        }
    }

    private void processResult(Result result) {
        MavenParser parser = MavenParser
                .builder()
                .build();
        Xml.Document wrappedMavenFile = parser.parse(result.getAfter().printAll()).get(0);
        wrappedMavenFile = (Xml.Document) wrappedMavenFile.withSourcePath(pom.getSourceFile().getSourcePath());
        pom.replaceWith(wrappedMavenFile);
    }
}
