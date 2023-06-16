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

package org.springframework.sbm.boot.upgrade_27_30;

import org.junit.jupiter.api.Test;
import org.openrewrite.java.ChangePackage;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.java.api.JavaSource;
import org.springframework.sbm.project.resource.TestProjectContext;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class ChangeJavaxPackagesToJakartaTest {

    @Test
    void collectingJavaxPackages() {

        String javaClass1 =
                "package com.example;\n" +
                "import javax.money.MonetaryAmount;\n" +
                "public class SomeClass {\n" +
                "  public MonetaryAmount convertToEntityAttribute() {\n" +
                "      return null;\n" +
                "  }\n" +
                "}";

        String javaClass2 =
                "package com.example;\n" +
                "import javax.persistence.Converter;\n" +
                "public class SomeClass2 {\n" +
                "  public Converter getConverter() {\n" +
                "      return null;\n" +
                "  }\n" +
                "}";

        String javaClass3 =
                "package com.example;\n" +
                "public class NoImports {}";

        String javaClass4 =
                "package com.example;\n" +
                "import java.math.BigDecimal;\n" +
                "public class OtherImports {\n" +
                "  private BigDecimal number;\n" +
                "}";
        ProjectContext context = TestProjectContext.buildProjectContext()
                .withBuildFileHavingDependencies("javax.money:money-api:1.1")
                .withJavaSource("src/main/java", javaClass1)
                .withJavaSource("src/main/java", javaClass2)
                .withJavaSource("src/main/java", javaClass3)
                .withJavaSource("src/main/java", javaClass4)
                .build();

        List<JavaSource> matches = context.getProjectJavaSources().asStream()
                .filter(js -> js.hasImportStartingWith("javax."))
                .collect(Collectors.toList());

        assertThat(matches).hasSize(2);
        assertThat(matches.get(0).getSourcePath().toString()).isEqualTo("src/main/java/com/example/SomeClass.java");
        assertThat(matches.get(1).getSourcePath().toString()).isEqualTo("src/main/java/com/example/SomeClass2.java");
        matches.forEach(m -> System.out.println(m.getSourcePath()));

    }

    @Test
    void testReplacingPackages() {
        String javaClass1 =
                "package com.example;\n" +
                "\n" +
                "import javax.money.MonetaryAmount;\n" +
                "public class SomeClass {\n" +
                "  public MonetaryAmount convertToEntityAttribute() {\n" +
                "      return null;\n" +
                "  }\n" +
                "}";

        String javaClass2 =
                "package com.example;\n" +
                        "\n" +
                        "import javax.persistence.Converter;\n" +
                        "public class SomeClass2 {\n" +
                        "  public Converter getConverter() {\n" +
                        "      return null;\n" +
                        "  }\n" +
                        "}";

        ProjectContext context = TestProjectContext.buildProjectContext()
                .withBuildFileHavingDependencies(/*"javax.money:money-api:1.1", */"jakarta.persistence:jakarta.persistence-api:2.2.3")
                .withJavaSource("src/main/java/com/example/SomeClass.java", javaClass1)
                .withJavaSource("src/main/java/com/example/SomeClass2.java", javaClass2)
                .build();

//        context.getProjectJavaSources().apply(new ChangePackage("javax", "jakarta", true));

        // TODO: Not all javax. packages can be renamed. The ones coming from JDK itself must stay -> create a whitelist of
        // TODO: Since 2.7 the format of spring.factories changed to META-INF/spring/key-name.imports with the value as one line
        context.getProjectJavaSources().apply(new ChangePackage("javax.persistence", "jakarta.persistence", true));


//
//        assertThat(context.getProjectJavaSources().list().get(0).print()).isEqualTo(
//                "package com.example;\n" +
//                "\n" +
//                "import jakarta.money.MonetaryAmount;\n" +
//                "public class SomeClass {\n" +
//                "  public MonetaryAmount convertToEntityAttribute() {\n" +
//                "      return null;\n" +
//                "  }\n" +
//                "}");

        System.out.println(context.getProjectJavaSources().list().get(0).print());
        System.out.println(context.getProjectJavaSources().list().get(1).print());
    }



}
