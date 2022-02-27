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
import org.springframework.sbm.support.openrewrite.GenericOpenRewriteRecipe;
import org.springframework.sbm.testhelper.common.utils.TestDiff;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openrewrite.Result;
import org.openrewrite.SourceFile;
import org.openrewrite.java.JavaParser;
import org.openrewrite.java.RemoveAnnotation;
import org.openrewrite.java.search.FindAnnotations;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.JavaType.FullyQualified;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class FindInterfacesAnnotatedWithTest {

    @Test
    @Disabled("Adding imports fails when sing AddAnnotation because JavaParser used misses dependencies")
    void test() {
        String source =
                "@javax.ejb.Local\n" +
                        "public interface Foo {}";

        String source2 =
                "@javax.ejb.Stateless\n" +
                        "public interface Bar {}";
        String source3 =
                "@javax.ejb.Stateless\n" +
                        "@javax.ejb.Local\n" +
                        "interface Baz {}";

        String source4 =
                "@javax.ejb.Stateless\n" +
                        "@javax.ejb.Local\n" +
                        "class SomeBean {}";

        String source5 =
                "public class AnotherBean implements Baz {}";

        String source6 =
                "@javax.ejb.Stateless\n" +
                        "@javax.ejb.Local\n" +
                        "public interface ImplementedTwice {}";

        String source7 =
                "@javax.ejb.Stateless(name=\"b1\")\n" +
                        "public class ImplementedTwiceBean1 implements ImplementedTwice {}";

        String source8 =
                "public class ImplementedTwiceBean2 implements ImplementedTwice {}";

        List<J.CompilationUnit> compilationUnits = OpenRewriteTestSupport.createCompilationUnitsFromStrings(List.of("javax.ejb:javax.ejb-api:3.2", "org.springframework:spring-context:5.3.5"),
                source,
                source2,
                source3,
                source4,
                source5,
                source6,
                source7,
                source8);

        String statelessPattern = "@javax.ejb.Stateless";
        String localPattern = "@javax.ejb.Local";

        List<SourceFile> sourcesAnnotatedWithStateless = new FindAnnotations(statelessPattern)
                .run(compilationUnits)
                .stream()
                .map(Result::getBefore)
                .collect(Collectors.toList());

        List<SourceFile> sourcesAnnotatedWithStatelessAndLocal = new FindAnnotations(localPattern)
                .run(sourcesAnnotatedWithStateless)
                .stream()
                .map(Result::getAfter)
                .collect(Collectors.toList());

        List<SourceFile> interfaces = new FilterInterfaceVisitor()
                .run(sourcesAnnotatedWithStatelessAndLocal)
                .stream()
                .map(Result::getAfter)
                .collect(Collectors.toList());

        assertThat(interfaces).hasSize(2);
        assertThat(interfaces.get(0).printAll()).isEqualTo(
                "/*~~>*/@javax.ejb.Stateless\n" +
                        "/*~~>*/@javax.ejb.Local\n" +
                        "interface Baz {}"
        );

        List<Result> resultList = new RemoveAnnotation(localPattern).doNext(new RemoveAnnotation(statelessPattern)).run(interfaces);

        assertThat(resultList.get(0).getAfter().printAll()).isEqualTo("/*~~>*/interface Baz {}");

        JavaParser javaParser = JavaParser.fromJavaVersion().build();

        List<FullyQualified> cleanedInterfaces = resultList.stream()
                .map(Result::getAfter)
                .map(SourceFile::printAll)
                .map(javaParser::parse)
                .flatMap(List::stream)
                .map(J.CompilationUnit::getClasses)
                .flatMap(List::stream)
                .filter(cd -> cd.getKind() == J.ClassDeclaration.Kind.Type.Interface)
                .map(cd -> cd.getType())
                .collect(Collectors.toList());

        List<Result> typesImplementingInterfaces = new FindTypesImplementing(cleanedInterfaces).run(compilationUnits);

        assertThat(typesImplementingInterfaces.get(0).getAfter().printAll()).isEqualTo("/*~~>*/public class AnotherBean implements Baz {}");

        List<String> results = new ArrayList<>();

        String springServiceAnnotation = "org.springframework.stereotype.Service";
        typesImplementingInterfaces.stream()
                .map(Result::getAfter)
                .map(SourceFile::printAll)
                .map(javaParser::parse)
                .flatMap(List::stream)
                .forEach(cu -> {
                    cu.getClasses().stream()
                            .forEach(cd -> {
                                List<Result> list = new GenericOpenRewriteRecipe<>(() -> new AddAnnotationVisitor(() -> JavaParser.fromJavaVersion().build(), cd, "@Service", "org.springframework.stereotype.Service")).run(List.of(cu));
                                J.CompilationUnit sf = (J.CompilationUnit) list.get(0).getAfter();
                                results.add(sf.printAll());
                            });

                });

        assertThat(results).hasSize(3);

        String expected = "/*~~>*/import " + springServiceAnnotation + ";\n\n" +
                "@Service\n" +
                "public class AnotherBean implements Baz {}";

        assertThat(results.get(0)).as(TestDiff.of(results.get(0), expected)).isEqualTo(expected);
    }


    @Test
    void testClassDeclWithLeadingAnnotations() {

        String source3 =
                "@javax.ejb.Stateless\n" +
                        "@javax.ejb.Local\n" +
                        "public interface Baz {}";

        String source5 =
                "public class AnotherBean implements Baz {}";

        List<J.CompilationUnit> compilationUnits = OpenRewriteTestSupport.createCompilationUnitsFromStrings(List.of("javax.ejb:javax.ejb-api:3.2", "org.springframework:spring-context:5.3.5"), source3, source5);

        J.ClassDeclaration classDeclaration = compilationUnits.get(1).getClasses().get(0);
        JavaParser javaParser = JavaParser.fromJavaVersion().build();
        List<Result> list = new GenericOpenRewriteRecipe<>(() -> new AddAnnotationVisitor(javaParser, classDeclaration, "@Service", "org.springframework.stereotype.Service")).run(List.of(compilationUnits.get(1)));
        J.CompilationUnit j = (J.CompilationUnit) list.get(0).getAfter();

        System.out.println(j.printAll());
    }

}
