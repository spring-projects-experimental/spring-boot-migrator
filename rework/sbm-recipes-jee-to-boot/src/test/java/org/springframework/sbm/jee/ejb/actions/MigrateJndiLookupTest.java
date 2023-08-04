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
package org.springframework.sbm.jee.ejb.actions;

import org.springframework.sbm.GitHubIssue;
import org.springframework.sbm.test.JavaMigrationActionTestSupport;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

class MigrateJndiLookupTest {

    @Test
    @Disabled("Migrate JNDI lookup needs refactoring. Test is affected by concurrency settings in Rewrite visitors through Recipe.run(...)")
    void memberLookupWithCast() {
        List<String> given = List.of(
                "import org.springframework.beans.factory.annotation.Autowired;\n" +
                        "\n" +
                        "import javax.ejb.Stateless;\n" +
                        "import javax.naming.InitialContext;\n" +
                        "import javax.naming.NamingException;\n" +
                        "\n" +
                        "@Stateless\n" +
                        "public class BeanWithJndiMemberLookup {\n" +
                        "\n" +
                        "    private BeanWithJndiName example = (BeanWithJndiName) InitialContext.doLookup(\"java:module/beanWithJndiName\");\n" +
                        "\n" +
                        "    public BeanWithJndiMemberLookup() throws NamingException {\n" +
                        "    }\n" +
                        "}",
                // -----------
                "import javax.ejb.Stateless;\n" +
                        "\n" +
                        "@Stateless\n" +
                        "public class BeanWithJndiName {\n" +
                        "}\n"
        );

        List<String> expected = List.of(
                "import org.springframework.beans.factory.annotation.Autowired;\n" +
                        "\n" +
                        "import javax.ejb.Stateless;\n" +
                        "import javax.naming.NamingException;\n" +
                        "\n" +
                        "@Stateless\n" +
                        "public class BeanWithJndiMemberLookup {\n" +
                        "    @Autowired\n" +
                        "    private BeanWithJndiName example;\n" +
                        "\n" +
                        // FIXME: find and remove obsolete throws clause
                        "    public BeanWithJndiMemberLookup() throws NamingException {\n" +
                        "    }\n" +
                        "}",
                // -----------
                "import javax.ejb.Stateless;\n" +
                        "\n" +
                        "@Stateless\n" +
                        "public class BeanWithJndiName {\n" +
                        "}\n"
        );
        MigrateJndiLookup sut = new MigrateJndiLookup();
        JavaMigrationActionTestSupport.verify(given, expected, sut, "javax.ejb:javax.ejb-api:3.2", "org.springframework:spring-context:5.3.5");
    }

    @Test
    @Disabled("Migrate JNDI lookup needs refactoring. Test is affected by concurrency settings in Rewrite visitors through Recipe.run(...)")
    void memberLookupWithoutCast() {
        List<String> given = List.of(
                "import org.springframework.beans.factory.annotation.Autowired;\n" +
                        "\n" +
                        "import javax.ejb.Stateless;\n" +
                        "import javax.naming.InitialContext;\n" +
                        "import javax.naming.NamingException;\n" +
                        "\n" +
                        "@Stateless\n" +
                        "public class BeanWithJndiMemberLookup {\n" +
                        "\n" +
                        "    private BeanWithJndiName example = InitialContext.doLookup(\"java:module/beanWithJndiName\");\n" +
                        "\n" +
                        "    public BeanWithJndiMemberLookup() throws NamingException {\n" +
                        "    }\n" +
                        "}",
                // -----------
                "import javax.ejb.Stateless;\n" +
                        "\n" +
                        "@Stateless\n" +
                        "public class BeanWithJndiName {\n" +
                        "}\n"
        );

        List<String> expected = List.of(
                "import org.springframework.beans.factory.annotation.Autowired;\n" +
                        "\n" +
                        "import javax.ejb.Stateless;\n" +
                        "import javax.naming.NamingException;\n" +
                        "\n" +
                        "@Stateless\n" +
                        "public class BeanWithJndiMemberLookup {\n" +
                        "    @Autowired\n" +
                        "    private BeanWithJndiName example;\n" +
                        "\n" +
                        "    public BeanWithJndiMemberLookup() throws NamingException {\n" +
                        "    }\n" +
                        "}",
                // -----------
                "import javax.ejb.Stateless;\n" +
                        "\n" +
                        "@Stateless\n" +
                        "public class BeanWithJndiName {\n" +
                        "}\n"
        );
        MigrateJndiLookup sut = new MigrateJndiLookup();
        JavaMigrationActionTestSupport.verify(given, expected, sut, "javax.ejb:javax.ejb-api:3.2", "org.springframework:spring-context:5.3.5");
    }

    @Test
    @GitHubIssue("https://github.com/pivotal/spring-boot-migrator/issues/202")
    @Disabled("Migrate JNDI lookup needs refactoring. Test is affected by concurrency settings in Rewrite visitors through Recipe.run(...)")
    void test() {
        List<String> given = List.of(
                "public interface ExampleLocal {public void foo();}",
                // -----------
                "import javax.naming.InitialContext;\n" +
                        "import javax.ejb.Stateless;\n" +
                        "@Stateless\n" +
                        "public class SomeEjb {\n" +
                        "   private ExampleLocal example = (ExampleLocal) InitialContext.doLookup(\"java:module/ExampleLocal\");\n" +
                        "}"
        );

        List<String> expected = List.of(
                "public interface ExampleLocal {public void foo();}",
                // -----------
                "import org.springframework.beans.factory.annotation.Autowired;\n" +
                        "\n" +
                        "import javax.ejb.Stateless;\n" +
                        "\n" +
                        "@Stateless\n" +
                        "public class SomeEjb {\n" +
                        "    @Autowired\n" +
                        "    private ExampleLocal example;\n" +
                        "}"
        );

        MigrateJndiLookup sut = new MigrateJndiLookup();
        JavaMigrationActionTestSupport.verify(given, expected, sut, "javax.ejb:javax.ejb-api:3.2", "org.springframework:spring-context:5.3.5");
    }

    @Test
    @GitHubIssue("https://github.com/pivotal/spring-boot-migrator/issues/202")
    @Disabled("Migrate JNDI lookup needs refactoring. Test is affected by concurrency settings in Rewrite visitors through Recipe.run(...)")
    void test2() {
        List<String> given = List.of(
                "package com.example.foo; \n" +
                        "public interface ExampleLocal {public void foo();}",
                // -----------
                "package com.example.bar;\n" +
                        "import javax.naming.InitialContext;\n" +
                        "import javax.ejb.Stateless;\n" +
                        "import com.example.foo.ExampleLocal;\n" +
                        "@Stateless\n" +
                        "public class SomeEjb {\n" +
                        "   int foo = 3;\n" +
                        "   public void someMethod1() {\n" +
                        "       ExampleLocal example = (ExampleLocal) InitialContext.doLookup(\"java:module/ExampleLocal\");\n" +
                        "       ExampleLocal example2 = (ExampleLocal) InitialContext.doLookup(\"java:module/ExampleLocal\");\n" +
                        "       example.foo();\n" +
                        "       example2.foo();\n" +
                        "       int a = 10;\n" +
                        "   }\n" +
                        "}"
        );

        List<String> expected = List.of(
                "package com.example.foo; \n" +
                        "public interface ExampleLocal {public void foo();}",
                // -----------
                "package com.example.bar;\n" +
                        "\n" +
                        "import javax.ejb.Stateless;\n" +
                        "import com.example.foo.ExampleLocal;\n" +
                        "import org.springframework.beans.factory.annotation.Autowired;\n" +
                        "\n" +
                        "@Stateless\n" +
                        "public class SomeEjb {\n" +
//                "    // JNDI name was 'java:module/ExampleLocal'" +
                        "    @Autowired\n" +
                        "    private ExampleLocal example2;\n" +
                        "    @Autowired\n" +
                        "    private ExampleLocal example;\n" +
                        "    int foo = 3;\n" +
                        "\n" +
                        "    public void someMethod1() {\n" +
                        "        example.foo();\n" +
                        "        example2.foo();\n" +
//                "        int a = 10;\n" +
                        "    }\n" +
                        "}"
        );

        MigrateJndiLookup sut = new MigrateJndiLookup();
        JavaMigrationActionTestSupport.verify(given, expected, sut, "javax.ejb:javax.ejb-api:3.2", "org.springframework:spring-beans:5.3.5");
    }

    @Test
    @GitHubIssue("https://github.com/pivotal/spring-boot-migrator/issues/202")
    @Disabled("Migrate JNDI lookup needs refactoring. Test is affected by concurrency settings in Rewrite visitors through Recipe.run(...)")
    void test3() {
        List<String> given = List.of(
                "public interface ExampleLocal {public void foo();}",
                // -----------
                "import javax.naming.InitialContext;\n" +
                        "import javax.ejb.Stateless;\n" +
                        "@Stateless\n" +
                        "public class SomeEjb {\n" +
                        "   int foo = 3;\n" +
                        "   public void someMethod2() {\n" +
                        "       InitialContext ic = new InitialContext();\n" +
                        "       ExampleLocal example = (ExampleLocal) ic.lookup(\"java:module/ExampleLocal\");\n" +
                        "       example.foo();\n" +
                        "       int a = 10;\n" +
                        "   }\n" +
                        "}"
        );

        List<String> expected = List.of(
                "public interface ExampleLocal {public void foo();}",
                // -----------
                "import org.springframework.beans.factory.annotation.Autowired;\n" +
                        "\n" +
                        "import javax.ejb.Stateless;\n" +
                        "\n" +
                        "@Stateless\n" +
                        "public class SomeEjb {\n" +
//                "    // JNDI name was 'java:module/ExampleLocal'" +
                        "    @Autowired\n" +
                        "    private ExampleLocal example;\n" +
                        "    int foo = 3;\n" +
                        "\n" +
                        "    public void someMethod2() {\n" +
                        "        example.foo();\n" +
                        "    }\n" +
                        "}"
        );

        MigrateJndiLookup sut = new MigrateJndiLookup();
        JavaMigrationActionTestSupport.verify(given, expected, sut, "javax.ejb:javax.ejb-api:3.2", "org.springframework:spring-context:5.3.5");
    }

}