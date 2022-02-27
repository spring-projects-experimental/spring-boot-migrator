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
import org.springframework.sbm.project.resource.TestProjectContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class OpenRewriteExpressionTest {
    @Test
    void setAssignmentRightSide() {
        String code = "@Deprecated(forRemoval = false) public class Foo {}";
        // FIXME: #214 currently the SourceSet is also Refactoring and thus calls to Refactoring in AST nodes require a JavaSourceSet.
        JavaSource openRewriteJavaSource = TestProjectContext.buildProjectContext().withJavaSources(code).build().getProjectJavaSources().list().get(0);
        openRewriteJavaSource.getTypes().get(0).getAnnotation("java.lang.Deprecated").setAttribute("forRemoval", true, Boolean.class);
        Assertions.assertThat(openRewriteJavaSource.print()).isEqualTo("@Deprecated(forRemoval = true) public class Foo {}");
    }
}