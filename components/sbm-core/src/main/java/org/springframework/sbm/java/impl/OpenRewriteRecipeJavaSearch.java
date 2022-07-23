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
package org.springframework.sbm.java.impl;

import org.springframework.sbm.java.api.JavaSource;
import org.jetbrains.annotations.NotNull;
import org.openrewrite.ExecutionContext;
import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.PrintOutputCapture;
import org.openrewrite.Result;
import org.openrewrite.java.JavaParser;
import org.openrewrite.java.JavaPrinter;
import org.openrewrite.java.tree.J;
import org.openrewrite.marker.Marker;
import org.openrewrite.marker.SearchResult;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class OpenRewriteRecipeJavaSearch {

    private final Function<List<J.CompilationUnit>, List<Result>> searchRecipe;
    private final JavaParser javaParser;

    public OpenRewriteRecipeJavaSearch(Function<List<J.CompilationUnit>, List<Result>> searchRecipe, JavaParser javaParser) {
        this.searchRecipe = searchRecipe;
        this.javaParser = javaParser;
    }

    public void commentFindings(List<? extends JavaSource> javaSources, String commentText) {
        List<J.CompilationUnit> cus = getCompilationUnits(javaSources);
        List<Result> results = this.searchRecipe.apply(cus);
        String comment = "\n/*\n" + commentText + "\n*/\n";
        results.stream()
                .forEach(result -> {
                    OpenRewriteJavaSource affectedJavaSource = javaSources.stream()
                            .filter(js -> js.getClass().isAssignableFrom(OpenRewriteJavaSource.class))
                            .map(OpenRewriteJavaSource.class::cast)
                            .filter(js -> result.getBefore().getId().equals(js.getResource().getId()))
                            .findFirst()
                            .get();

                    JavaPrinter<ExecutionContext> javaPrinter = new JavaPrinter<>() {
                        @Override
                        public <M extends Marker> M visitMarker(Marker marker, PrintOutputCapture<ExecutionContext> p) {
                            if (marker instanceof SearchResult) {
                                p.out.append(comment);
                            }
                            return (M) marker;
                        }
                    };

                    PrintOutputCapture<Integer> outputCapture = new PrintOutputCapture(new InMemoryExecutionContext());
                    ((JavaPrinter) javaPrinter).visit((J.CompilationUnit) result.getAfter(), outputCapture);
                    J.CompilationUnit compilationUnit = javaParser.parse(outputCapture.out.toString()).get(0).withSourcePath(result.getBefore().getSourcePath());
                    affectedJavaSource.getResource().replaceWith(compilationUnit);
                });
    }

    @NotNull
    private List<J.CompilationUnit> getCompilationUnits(List<? extends JavaSource> allCompilationUnits) {
        return allCompilationUnits.stream()
                .filter(cls -> cls.getClass().isAssignableFrom(OpenRewriteJavaSource.class))
                .map(cls -> (OpenRewriteJavaSource) cls)
                .map(jcu -> jcu.getCompilationUnit())
                .collect(Collectors.toList());
    }
}
