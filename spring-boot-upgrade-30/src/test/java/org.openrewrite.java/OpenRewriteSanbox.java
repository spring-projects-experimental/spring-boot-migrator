package org.openrewrite.java;

import org.junit.jupiter.api.Test;
import org.openrewrite.Cursor;
import org.openrewrite.Tree;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.JavaType;
import org.openrewrite.java.tree.Space;
import org.openrewrite.marker.Markers;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

public class OpenRewriteSanbox {

    class JavaMethodCount extends JavaVisitor<AtomicInteger> {

        @Override
        public J visitMethodInvocation(J.MethodInvocation method, AtomicInteger p) {
            //increment the shared counter when visiting a method invocation.
            p.incrementAndGet();

            return super.visitMethodInvocation(method, p);
        }
    }

    class MethodRefactorVisitor extends JavaVisitor<Void> {

        @Override
        public J visitMethodDeclaration(J.MethodDeclaration method, Void p) {
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
        assertThat(counter.get()).isEqualTo(3);

        System.out.println(cu.printAll());
    }

    @Test
    void refactor() {
        JavaParser jp = JavaParser.fromJavaVersion().build();

        J.CompilationUnit cu = jp.parse("    import org.slf4j.Logger;\n" +
                "    public class Sample {\n" +
                "        Logger logger;\n" +
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
