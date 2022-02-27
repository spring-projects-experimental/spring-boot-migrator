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

import org.springframework.sbm.java.OpenRewriteTestSupport;
import org.junit.jupiter.api.Test;
import org.openrewrite.ExecutionContext;
import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.tree.J;

import static org.assertj.core.api.Assertions.assertThat;

public class AddOrReplaceAnnotationAttributeTest {


    @Test
    void addBooleanAttributeToAnnotationWithoutAttributes() {
        String code = "@Deprecated public class Foo {}";
        J.CompilationUnit compilationUnit = OpenRewriteTestSupport.createCompilationUnit(code);

        J.Annotation annotation = compilationUnit.getClasses().get(0).getLeadingAnnotations().get(0);
        JavaIsoVisitor<ExecutionContext> javaIsoVisitor = new AddOrReplaceAnnotationAttribute(annotation, "forRemoval", true, Boolean.class);

        String refactoredCu = javaIsoVisitor.visit(compilationUnit, new InMemoryExecutionContext()).print();

        assertThat(refactoredCu).isEqualTo("@Deprecated(forRemoval = true) public class Foo {}");
    }

    @Test
    void addStringAttributeToAnnotationWithoutAttributes() {
        String code = "@Deprecated public class Foo {}";
        J.CompilationUnit compilationUnit = OpenRewriteTestSupport.createCompilationUnit(code);

        J.Annotation annotation = compilationUnit.getClasses().get(0).getLeadingAnnotations().get(0);
        JavaIsoVisitor<ExecutionContext> javaIsoVisitor = new AddOrReplaceAnnotationAttribute(annotation, "since", "2020", String.class);
        String refactoredCu = javaIsoVisitor.visit(compilationUnit, new InMemoryExecutionContext()).print();

        assertThat(refactoredCu).isEqualTo("@Deprecated(since = \"2020\") public class Foo {}");
    }

    @Test
    void changeAnnotationAttributeValue() {
        String code = "@Deprecated(forRemoval = false) public class Foo {}";
        J.CompilationUnit compilationUnit = OpenRewriteTestSupport.createCompilationUnit(code);
        J.Annotation annotation = compilationUnit.getClasses().get(0).getLeadingAnnotations().get(0);
        JavaIsoVisitor<ExecutionContext> javaIsoVisitor = new AddOrReplaceAnnotationAttribute(annotation, "forRemoval", true, Boolean.class);
        String refactoredCu = javaIsoVisitor.visit(compilationUnit, new InMemoryExecutionContext()).print();
        assertThat(refactoredCu).isEqualTo("@Deprecated(forRemoval = true) public class Foo {}");
    }

    @Test
    void changeAnnotationAttributeValueOfAnnotationWithAttributes() {
        String code = "@Deprecated(forRemoval = false, since = \"2020\") public class Foo {}";
        J.CompilationUnit compilationUnit = OpenRewriteTestSupport.createCompilationUnit(code);
        J.Annotation annotation = compilationUnit.getClasses().get(0).getLeadingAnnotations().get(0);
        JavaIsoVisitor<ExecutionContext> javaIsoVisitor = new AddOrReplaceAnnotationAttribute(annotation, "forRemoval", true, Boolean.class);
        String refactoredCu = javaIsoVisitor.visit(compilationUnit, new InMemoryExecutionContext()).print();
        assertThat(refactoredCu).isEqualTo("@Deprecated(forRemoval = true, since = \"2020\") public class Foo {}");
    }

    @Test
    void changeAnnotationAttributeValueOfAnnotationWithAttributes2() {
        String code = "@Deprecated(since = \"2020\", forRemoval = false) public class Foo {}";
        J.CompilationUnit compilationUnit = OpenRewriteTestSupport.createCompilationUnit(code);
        J.Annotation annotation = compilationUnit.getClasses().get(0).getLeadingAnnotations().get(0);
        JavaIsoVisitor<ExecutionContext> javaIsoVisitor = new AddOrReplaceAnnotationAttribute(annotation, "forRemoval", true, Boolean.class);
        String refactoredCu = javaIsoVisitor.visit(compilationUnit, new InMemoryExecutionContext()).print();
        assertThat(refactoredCu).isEqualTo("@Deprecated(since = \"2020\", forRemoval = true) public class Foo {}");
    }

    @Test
    void addAttributeToAnnotationWithAttributes() {
        String code = "@Deprecated(forRemoval = true) public class Foo {}";
        J.CompilationUnit compilationUnit = OpenRewriteTestSupport.createCompilationUnit(code);
        J.Annotation annotation = compilationUnit.getClasses().get(0).getLeadingAnnotations().get(0);
        JavaIsoVisitor<ExecutionContext> javaIsoVisitor = new AddOrReplaceAnnotationAttribute(annotation, "since", "2020", String.class);
        String refactoredCu = javaIsoVisitor.visit(compilationUnit, new InMemoryExecutionContext()).print();
        assertThat(refactoredCu).isEqualTo("@Deprecated(forRemoval = true, since = \"2020\") public class Foo {}");
    }


}




