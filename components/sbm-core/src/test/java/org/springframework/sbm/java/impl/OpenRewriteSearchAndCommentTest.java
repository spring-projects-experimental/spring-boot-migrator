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

import org.openrewrite.java.JavaParser;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.SbmApplicationProperties;
import org.springframework.sbm.project.resource.TestProjectContext;
import org.junit.jupiter.api.Test;
import org.openrewrite.java.search.FindAnnotations;

import static org.assertj.core.api.Assertions.assertThat;

class OpenRewriteSearchAndCommentTest {

    @Test
    void markMatches() {

        String javaSource1 =
                "public class SomeTest {" +
                        "   @Deprecated public void test() {}" +
                        "}";
        String javaSource2 =
                "public class SomeTest2 {" +
                        "   public void test2() {}" +
                        "}";

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withJavaSources(javaSource1, javaSource2)
                .build();

        String markerText = "marker text";

        JavaParser javaParser = new RewriteJavaParser(new SbmApplicationProperties());
        OpenRewriteRecipeJavaSearch sut = new OpenRewriteRecipeJavaSearch(compilationUnits -> new FindAnnotations("@java.lang.Deprecated").run(compilationUnits).getResults(), javaParser);

        sut.commentFindings(projectContext.getProjectJavaSources().list(), markerText);

        assertThat(projectContext.getProjectJavaSources().list().size()).isEqualTo(2);
        assertThat(projectContext.getProjectJavaSources().list().get(1).print()).isEqualTo(javaSource2);
        assertThat(projectContext.getProjectJavaSources().list().get(0).print()).isEqualTo(
                "public class SomeTest {   \n" +
                        "/*\n" +
                        markerText +
                        "\n*/\n@Deprecated public void test() {}}");
    }
}