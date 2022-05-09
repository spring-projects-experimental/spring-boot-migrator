package org.springframework.sbm.openrewrite.java;

import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.jupiter.api.Test;
import org.openrewrite.java.JavaParser;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.JavaType;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class RetrieveAnnotationTypeTest {
    @Test
    void retrieveAnnotation() {
        String javaSource =
                "import javax.ejb.Stateless;\n" +
                "@Stateless\n" +
                "public class MyClass {" +
                "}";

//        String mavenRepo = System.getProperty("user.home") + "/.m2/repository";
//        List<Path> paths = JavaParser.dependenciesFromClasspath("ejb-api");
//        List<Path> paths = JavaParser.dependenciesFromClasspath("javax/ejb/javax.ejb-api/3.2/javax.ejb-api-3.2.jar");

        List<Path> classpathFiles = getClasspathFiles("javax.ejb:javax.ejb-api:3.2");

        JavaParser javaParser = JavaParser
                .fromJavaVersion()
                .classpath(classpathFiles)
                .build();

        List<J.Annotation> leadingAnnotations = javaParser.parse(javaSource).get(0).getClasses().get(0).getLeadingAnnotations();
        JavaType.Class type = JavaType.Class.class.cast(leadingAnnotations.get(0).getType());
        assertThat(type.getFullyQualifiedName()).isEqualTo("javax.ejb.Stateless");
    }

    public static List<Path> getClasspathFiles(String... classpath) {
        if (classpath.length == 0) return List.of();
        File[] as = Maven.resolver().resolve(classpath).withTransitivity().as(File.class);
        return Arrays.stream(as)
                .map(File::toPath)
                .collect(Collectors.toList());
    }
}
