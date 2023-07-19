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
import org.springframework.sbm.java.api.ProjectJavaSources;
import org.springframework.sbm.java.api.SubTypeHierarchy;
import org.springframework.sbm.java.api.TypeHierarchyNode;
import org.springframework.sbm.project.resource.RewriteSourceFileHolder;
import org.springframework.sbm.project.resource.TestProjectContext;
import org.springframework.sbm.support.openrewrite.java.FindCompilationUnitContainingType;
import org.junit.jupiter.api.Test;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.JavaType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SubTypeHierarchyTest {
    @Test
    void testTypeHierarchyNode() {

        String javaSource1 =
                """
                package a;
                public interface Foo8 {}
                """;
        String javaSource2 =
                """
                import a.Foo8;
                public class FooImpl8 implements Foo8 {}
                """;
        String javaSource3 =
                """
                public class Bar8 extends FooImpl8 {}
                class Baz8 {}
                """;
        String javaSource4 =
                """
                public class Banana8 extends Bar8 {}
                """;
        String javaSource5 =
                """
                public abstract class Apple8 extends Bar8 {}
                """;
        String javaSource6 =
                """
                class Papaya8 extends Apple8 {}
                """;

        ProjectJavaSources javaSourceSet = TestProjectContext.buildProjectContext()
                .withJavaSources(
                        javaSource1,
                        javaSource2,
                        javaSource3,
                        javaSource4,
                        javaSource5,
                        javaSource6
                )
                .withDummyRootBuildFile()
                .build()
                .getProjectJavaSources();

        JavaSource javaSource = javaSourceSet.list().get(0);

        SubTypeHierarchy subTypeHierarchy = new SubTypeHierarchy(javaSource.getTypes().get(0), javaSourceSet);

        TypeHierarchyNode root = subTypeHierarchy.getRoot();
        assertThat(root.getFullyQualifiedName()).isEqualTo("a.Foo8");

        assertThat(root.getChildren()).hasSize(1); // FooImpl
        TypeHierarchyNode fooImpl = root.getChildren().get(0);
        assertThat(fooImpl.getFullyQualifiedName()).isEqualTo("FooImpl8");

        assertThat(fooImpl.getChildren()).hasSize(1); // Bar
        TypeHierarchyNode bar = fooImpl.getChildren().get(0);
        assertThat(bar.getFullyQualifiedName()).isEqualTo("Bar8");

        assertThat(bar.getChildren()).hasSize(2); // Banana, Apple
        TypeHierarchyNode banana = bar.getChildren().get(0);
        assertThat(banana.getFullyQualifiedName()).isEqualTo("Banana8");
        assertThat(banana.getChildren()).isEmpty();

        TypeHierarchyNode apple = bar.getChildren().get(1);
        assertThat(apple.getFullyQualifiedName()).isEqualTo("Apple8");

        assertThat(apple.getChildren()).hasSize(1);
        TypeHierarchyNode papaya = apple.getChildren().get(0);
        assertThat(papaya.getFullyQualifiedName()).isEqualTo("Papaya8");
        assertThat(papaya.getChildren()).isEmpty();
    }

    @Test
    void testTypeHierarchyNode2() {

        String javaSource1 =
                "public interface Foo2 {}";
        String javaSource2 =
                "public class FooImpl2 implements Foo2 {}";
        String javaSource3 =
                "public class Bar2 extends FooImpl2 {}\n" +
                        "class Baz2 {}";

        ProjectJavaSources projectJavaSources = TestProjectContext.buildProjectContext()
                .withJavaSources(javaSource1, javaSource2, javaSource3)
                .build()
                .getProjectJavaSources();

        FindCompilationUnitContainingType findCompilationUnitContainingType = new FindCompilationUnitContainingType((JavaType.FullyQualified) JavaType.buildType("Bar2"));

        List<RewriteSourceFileHolder<J.CompilationUnit>> result = (List<RewriteSourceFileHolder<J.CompilationUnit>>) projectJavaSources.find(findCompilationUnitContainingType);

        assertThat(result.get(0).print()).isEqualTo(javaSource3);
    }
}