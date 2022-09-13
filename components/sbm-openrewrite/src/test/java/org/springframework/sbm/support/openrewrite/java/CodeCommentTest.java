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
package org.springframework.sbm.support.openrewrite.java;

import org.openrewrite.*;
import org.springframework.sbm.java.OpenRewriteTestSupport;
import org.junit.jupiter.api.Test;
import org.openrewrite.java.JavaPrinter;
import org.openrewrite.java.search.FindAnnotations;
import org.openrewrite.java.tree.J;
import org.openrewrite.marker.Marker;
import org.openrewrite.marker.SearchResult;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CodeCommentTest {

    private final SelectorParser selectorParser = new SelectorParser();

    @Test
    void markerAsComment() {
        String javaCode =
                "public class SomeTest {" +
                        "   @Deprecated public void test() {}" +
                        "}";
        J.CompilationUnit compilationUnit = OpenRewriteTestSupport.createCompilationUnitFromString(javaCode);
        FindAnnotations findAnnotations = new FindAnnotations("@java.lang.Deprecated");

        List<J.CompilationUnit> cus = List.of(compilationUnit);
        RecipeRun recipeRun = findAnnotations.run(cus);

        String markerText = "\n/*\n Found @Deprecated without attributes: \n - please set markedForRemoval \n - please set since \nhere --> */ ";

        J.CompilationUnit cu = (J.CompilationUnit) recipeRun.getResults().get(0).getAfter();

        JavaPrinter<ExecutionContext> javaPrinter = new JavaPrinter<>() {
            @Override
            public <M extends Marker> M visitMarker(Marker marker, PrintOutputCapture<ExecutionContext> p) {
                if (marker instanceof SearchResult) {
                    p.out.append(markerText);
                }
                return (M) marker;
            }
        };

        PrintOutputCapture<Integer> outputCapture = new PrintOutputCapture(new InMemoryExecutionContext());
        ((JavaPrinter) javaPrinter).visit(cu, outputCapture);
        String s = outputCapture.out.toString();

        assertThat(s)
                .isEqualTo(
                        "public class SomeTest {   " + markerText + "@Deprecated public void test() {}}");
    }
}
