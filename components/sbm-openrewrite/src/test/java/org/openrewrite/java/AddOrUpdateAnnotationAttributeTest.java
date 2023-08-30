/*
 * Copyright 2021 - 2023 the original author or authors.
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
package org.openrewrite.java;

import org.openrewrite.java.AddOrUpdateAnnotationAttribute;
import org.openrewrite.java.tree.JavaType;
import org.springframework.sbm.java.OpenRewriteTestSupport;
import org.junit.jupiter.api.Test;
import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.java.tree.J;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AddOrUpdateAnnotationAttributeTest {


    @Test
    void addBooleanAttributeToAnnotationWithoutAttributes() {
        String code = "@Deprecated public class Foo {}";
        J.CompilationUnit compilationUnit = OpenRewriteTestSupport.createCompilationUnit(code);

        J.Annotation annotation = compilationUnit.getClasses().get(0).getLeadingAnnotations().get(0);
        AddOrUpdateAnnotationAttribute javaIsoVisitor = new AddOrUpdateAnnotationAttribute(((JavaType.Class)annotation.getType()).getFullyQualifiedName(), "forRemoval", "true", true);

        String refactoredCu = javaIsoVisitor.run(List.of(compilationUnit), new InMemoryExecutionContext()).getResults().get(0).getAfter().printAll();

        assertThat(refactoredCu).isEqualTo("@Deprecated(forRemoval = true) public class Foo {}");
    }

    @Test
    void addStringAttributeToAnnotationWithoutAttributes() {
        String code = "@Deprecated public class Foo {}";
        J.CompilationUnit compilationUnit = OpenRewriteTestSupport.createCompilationUnit(code);

        J.Annotation annotation = compilationUnit.getClasses().get(0).getLeadingAnnotations().get(0);
        AddOrUpdateAnnotationAttribute javaIsoVisitor = new AddOrUpdateAnnotationAttribute(((JavaType.Class)annotation.getType()).getFullyQualifiedName(), "since", "2020", true);
        String refactoredCu = javaIsoVisitor.run(List.of(compilationUnit), new InMemoryExecutionContext()).getResults().get(0).getAfter().printAll();

        assertThat(refactoredCu).isEqualTo("@Deprecated(since = \"2020\") public class Foo {}");
    }

    @Test
    void changeAnnotationAttributeValue() {
        String code = "@Deprecated(forRemoval = false) public class Foo {}";
        J.CompilationUnit compilationUnit = OpenRewriteTestSupport.createCompilationUnit(code);
        J.Annotation annotation = compilationUnit.getClasses().get(0).getLeadingAnnotations().get(0);
        AddOrUpdateAnnotationAttribute javaIsoVisitor = new AddOrUpdateAnnotationAttribute(((JavaType.Class)annotation.getType()).getFullyQualifiedName(), "forRemoval", "true", false);
        String refactoredCu = javaIsoVisitor.run(List.of(compilationUnit), new InMemoryExecutionContext()).getResults().get(0).getAfter().printAll();
        assertThat(refactoredCu).isEqualTo("@Deprecated(forRemoval = true) public class Foo {}");
    }

    @Test
    void changeAnnotationAttributeValueOfAnnotationWithAttributes() {
        String code = "@Deprecated(forRemoval = false, since = \"2020\") public class Foo {}";
        J.CompilationUnit compilationUnit = OpenRewriteTestSupport.createCompilationUnit(code);
        J.Annotation annotation = compilationUnit.getClasses().get(0).getLeadingAnnotations().get(0);
        AddOrUpdateAnnotationAttribute javaIsoVisitor = new AddOrUpdateAnnotationAttribute(((JavaType.Class)annotation.getType()).getFullyQualifiedName(), "forRemoval", "true", false);
        String refactoredCu = javaIsoVisitor.run(List.of(compilationUnit), new InMemoryExecutionContext()).getResults().get(0).getAfter().printAll();
        assertThat(refactoredCu).isEqualTo("@Deprecated(forRemoval = true, since = \"2020\") public class Foo {}");
    }

    @Test
    void changeAnnotationAttributeValueOfAnnotationWithAttributes2() {
        String code = "@Deprecated(since = \"2020\", forRemoval = false) public class Foo {}";
        J.CompilationUnit compilationUnit = OpenRewriteTestSupport.createCompilationUnit(code);
        J.Annotation annotation = compilationUnit.getClasses().get(0).getLeadingAnnotations().get(0);
        AddOrUpdateAnnotationAttribute javaIsoVisitor = new AddOrUpdateAnnotationAttribute(((JavaType.Class)annotation.getType()).getFullyQualifiedName(), "forRemoval", "true", false);
        String refactoredCu = javaIsoVisitor.run(List.of(compilationUnit), new InMemoryExecutionContext()).getResults().get(0).getAfter().printAll();
        assertThat(refactoredCu).isEqualTo("@Deprecated(since = \"2020\", forRemoval = true) public class Foo {}");
    }

    @Test
    void addAttributeToAnnotationWithAttributes() {
        String code = "@Deprecated(forRemoval = true) public class Foo {}";
        J.CompilationUnit compilationUnit = OpenRewriteTestSupport.createCompilationUnit(code);
        J.Annotation annotation = compilationUnit.getClasses().get(0).getLeadingAnnotations().get(0);
        AddOrUpdateAnnotationAttribute javaIsoVisitor = new AddOrUpdateAnnotationAttribute(((JavaType.Class)annotation.getType()).getFullyQualifiedName(), "since", "2020", false);
        String refactoredCu = javaIsoVisitor.run(List.of(compilationUnit), new InMemoryExecutionContext()).getResults().get(0).getAfter().printAll();
        assertThat(refactoredCu).isEqualTo("@Deprecated(since = \"2020\", forRemoval = true) public class Foo {}");
    }


}




