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
package org.openrewrite.java;

import org.junit.jupiter.api.Test;
import org.openrewrite.Cursor;
import org.openrewrite.Tree;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.Space;
import org.openrewrite.marker.Markers;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

public class OpenRewriteSanbox {

    class JavaMethodCount extends JavaIsoVisitor<AtomicInteger> {
        @Override
        public J.MethodDeclaration visitMethodDeclaration(J.MethodDeclaration method, AtomicInteger atomicInteger) {
            if (method.isConstructor()) {
                atomicInteger.incrementAndGet();
            }
            return super.visitMethodDeclaration(method, atomicInteger);
        }
    }

    class MethodRefactorVisitor extends JavaIsoVisitor<Void> {
        @Override
        public J.MethodDeclaration visitMethodDeclaration(J.MethodDeclaration method, Void p) {
            J.Identifier identifier = new J.Identifier(UUID.randomUUID(), Space.format(" "), Markers.EMPTY, "testMethod", null, null);
            return method.withName(identifier);
        }
    }

    @Test
    void counter() {
        JavaParser jp = JavaParser.fromJavaVersion().build();

        J.CompilationUnit cu = jp.parse("    import org.slf4j.Logger;\n" +
                "    public class Sample {\n" +
                "        Logger logger;\n" +
                "\n" +
                "public Sample() {} \n" +
                "public void mylogger() " +
                "        {\n" +
                "            logger.info(\"1\");\n" +
                "            logger.warn(\"2\");\n" +
                "            logger.error(\"3\");\n" +
                "        }\n" +
                "     }\n" +
                "    }").get(0);

        AtomicInteger counter = new AtomicInteger();
        new JavaMethodCount().visit(cu, counter);
        assertThat(counter.get()).isEqualTo(1);

        System.out.println(cu.printAll());
    }

    @Test
    void refactor() {
        JavaParser jp = JavaParser.fromJavaVersion().build();

        J.CompilationUnit cu = jp.parse("    import org.slf4j.Logger;\n" +
                "    public class Sample {\n" +
                "        Logger logger;\n" +
                "public Sample() {} \n" +
                "\n" +
                "public void mylogger() " +
                "        {\n" +
                "            logger.info(\"1\");\n" +
                "            logger.warn(\"2\");\n" +
                "            logger.error(\"3\");\n" +
                "            return true;\n" +
                "        }\n" +
                "     }\n" +
                "    }").get(0);

        Tree tree = new MethodRefactorVisitor().visit(cu, null);

        System.out.println(tree.print(new Cursor(null, null)));
    }
}
