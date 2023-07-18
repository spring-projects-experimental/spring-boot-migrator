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
import org.springframework.sbm.java.api.SuperTypeHierarchy;
import org.springframework.sbm.java.api.SuperTypeHierarchyNode;
import org.springframework.sbm.project.resource.TestProjectContext;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SuperTypeHierarchyTest {

    @Test
    void superTypeTest() {
        String javaSource1 =
                "public interface Foo3 {}";
        String javaSource2 =
                "interface AnotherInterface3 {}\n" +
                        "class FooImpl3 implements Foo3, AnotherInterface3 {}";
        String javaSource3 =
                "public class Bar3 extends FooImpl3 {}\n" +
                        "class Baz3 {}";
        String javaSource4 =
                "public class Banana3 extends Bar3 {}";
        String javaSource5 =
                "public abstract class Apple3 extends Bar3 {}";
        String javaSource6 =
                "class Papaya3 extends Apple3 {}";

        ProjectJavaSources javaSourceSet = TestProjectContext.buildProjectContext()
                .withJavaSources(
                        javaSource1,
                        javaSource2,
                        javaSource3,
                        javaSource4,
                        javaSource5,
                        javaSource6
                )
                .build()
                .getProjectJavaSources();

        JavaSource papaya = javaSourceSet.list().get(5);
        SuperTypeHierarchy sut = new SuperTypeHierarchy(papaya.getTypes().get(0));

        assertThat(sut.getRoot().getNode().getFullyQualifiedName()).isEqualTo("Papaya3");

        assertThat(sut.getRoot().getSuperTypes()).hasSize(1);
        assertThat(sut.getRoot().getSuperTypes().get(0).getNode().getFullyQualifiedName()).isEqualTo("Apple3");
        SuperTypeHierarchyNode appleNode = sut.getRoot().getSuperTypes().get(0);

        assertThat(appleNode.getSuperTypes()).hasSize(1);
        assertThat(appleNode.getSuperTypes().get(0).getNode().getFullyQualifiedName()).isEqualTo("Bar3");
        SuperTypeHierarchyNode bar = appleNode.getSuperTypes().get(0);

        assertThat(bar.getSuperTypes()).hasSize(1);
        assertThat(bar.getSuperTypes().get(0).getNode().getFullyQualifiedName()).isEqualTo("FooImpl3");
        SuperTypeHierarchyNode fooImpl = bar.getSuperTypes().get(0);

        assertThat(fooImpl.getSuperTypes()).hasSize(2);
        assertThat(fooImpl.getSuperTypes().get(0).getNode().getFullyQualifiedName()).isEqualTo("Foo3");
        assertThat(fooImpl.getSuperTypes().get(1).getNode().getFullyQualifiedName()).isEqualTo("AnotherInterface3");
        SuperTypeHierarchyNode foo = fooImpl.getSuperTypes().get(0);

        assertThat(foo.getSuperTypes()).isEmpty();

    }
}
