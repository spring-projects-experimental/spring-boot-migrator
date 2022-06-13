package org.springframework.sbm.openrewrite.java;

import org.junit.jupiter.api.Test;
import org.openrewrite.java.JavaParser;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.JavaType;

import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class JavaParserTest {

    @Test
    void withClasspathSet() {
        JavaParser javaParser = JavaParser.fromJavaVersion().build();
        String javaSource =
                "import javax.validation.constraints.Email;\n" +
                        "public class Cat {\n" +
                        "    @Email\n" +
                        "    private String email;\n" +
                        "}";

        javaParser.setClasspath(List.of(Path.of("/Users/fkrueger/.m2/repository/javax/validation/validation-api/2.0.1.Final/validation-api-2.0.1.Final.jar")));

        List<J.CompilationUnit> cus = javaParser.parse(javaSource);
        Object type = ((J.VariableDeclarations) cus.get(0).getClasses().get(0).getBody().getStatements().get(0)).getLeadingAnnotations().get(0).getType();
        assertThat(type).isInstanceOf(JavaType.Class.class);
        assertThat(((JavaType.Class) type).getFullyQualifiedName()).isEqualTo("javax.validation.constraints.Email");
    }

    @Test
    void withUnresolvedType() {
        JavaParser javaParser = Java11Parser.builder().build();
                // JavaParser.fromJavaVersion().build();
        String javaSource =
                "import javax.validation.constraints.Email;\n" +
                        "public class Cat {\n" +
                        "    @Email\n" +
                        "    private String email;\n" +
                        "}";

        List<J.CompilationUnit> cus = javaParser.parse(javaSource);
        Object type = ((J.VariableDeclarations) cus.get(0).getClasses().get(0).getBody().getStatements().get(0)).getLeadingAnnotations().get(0).getType();
        assertThat(type).isInstanceOf(JavaType.Unknown.class);
    }

    @Test
    //@Disabled("TODO fix me with #7")
    void unresolvedAndThenAddedDependency() {
        List<J.CompilationUnit> parse;
        JavaType type;

        String javaSource =
                "import javax.validation.constraints.Email;\n" +
                        "public class Cat {\n" +
                        "    @Email\n" +
                        "    private String email;\n" +
                        "}";
        Java11Parser javaParser = Java11Parser.builder().build();
        List<Path> classpath = List.of(Path.of("/Users/fkrueger/.m2/repository/javax/validation/validation-api/2.0.1.Final/validation-api-2.0.1.Final.jar"));
        javaParser.setSourceSet("main");
        parse = javaParser.parse(javaSource);
        type = ((J.VariableDeclarations) parse.get(0).getClasses().get(0).getBody().getStatements().get(0)).getLeadingAnnotations().get(0).getType();
        assertThat(type).isInstanceOf(JavaType.Unknown.class);

//        javaParser = JavaParser.fromJavaVersion().build();
        javaParser = javaParser.reset();
        javaParser.setSourceSet("main");
        javaParser.setClasspath(classpath);

        parse = javaParser.parse(javaSource);
        type = ((J.VariableDeclarations) parse.get(0).getClasses().get(0).getBody().getStatements().get(0)).getLeadingAnnotations().get(0).getType();
        assertThat(type).isNotInstanceOf(JavaType.Unknown.class);
        System.out.println(type);
    }
}
