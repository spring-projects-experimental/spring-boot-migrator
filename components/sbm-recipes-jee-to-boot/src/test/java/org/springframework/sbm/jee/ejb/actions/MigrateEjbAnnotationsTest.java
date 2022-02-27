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
import org.junit.jupiter.api.Test;

import java.util.List;

class MigrateEjbAnnotationsTest {

    @Test
    @GitHubIssue("https://github.com/pivotal/spring-boot-migrator/issues/204")
    void replaceSimpleEjbOnMember() {
        List<String> given = List.of(
                "import javax.ejb.Stateless;\n" +
                        "@Stateless\n" +
                        "public class SomeEjb {}",

                "import javax.ejb.Stateless;\n" +
                        "import javax.ejb.EJB;\n" +
                        "@Stateless\n" +
                        "public class ClientEjb {\n" +
                        "    @EJB\n" +
                        "    private SomeEjb someEjb;\n" +
                        "}"
        );
        List<String> expected = List.of(
                "import javax.ejb.Stateless;\n" +
                        "@Stateless\n" +
                        "public class SomeEjb {}",

                "import org.springframework.beans.factory.annotation.Autowired;\n" +
                        "\n" +
                        "import javax.ejb.Stateless;\n" +
                        "\n" +
                        "@Stateless\n" +
                        "public class ClientEjb {\n" +
                        "    @Autowired\n" +
                        "    private SomeEjb someEjb;\n" +
                        "}"
        );

        MigrateEjbAnnotations sut = new MigrateEjbAnnotations();

        JavaMigrationActionTestSupport.verify(given, expected, sut, "javax.ejb:javax.ejb-api:3.2", "org.springframework:spring-context:5.3.5");
    }

    @Test
    @GitHubIssue("https://github.com/pivotal/spring-boot-migrator/issues/204")
    void replaceEjbWithBeanNameOnMember() {
        List<String> given = List.of(
                "import javax.ejb.Stateless;\n" +
                        "@Stateless\n" +
                        "public class SomeEjb {}",

                "import javax.ejb.Stateless;\n" +
                        "import javax.ejb.EJB;\n" +
                        "@Stateless\n" +
                        "public class ClientEjb {\n" +
                        "    @EJB(beanName = \"someEjbBeanName\")\n" +
                        "    private SomeEjb someEjb;\n" +
                        "}"
        );
        List<String> expected = List.of(
                "import javax.ejb.Stateless;\n" +
                        "@Stateless\n" +
                        "public class SomeEjb {}",

                "import org.springframework.beans.factory.annotation.Autowired;\n" +
                        "import org.springframework.beans.factory.annotation.Qualifier;\n" +
                        "\n" +
                        "import javax.ejb.Stateless;\n" +
                        "\n" +
                        "@Stateless\n" +
                        "public class ClientEjb {\n" +
                        "    @Autowired\n" +
                        "    @Qualifier(\"someEjbBeanName\")\n" +
                        "    private SomeEjb someEjb;\n" +
                        "}"
        );

        MigrateEjbAnnotations sut = new MigrateEjbAnnotations();

        JavaMigrationActionTestSupport.verify(given, expected, sut, "javax.ejb:javax.ejb-api:3.2", "org.springframework:spring-context:5.3.5");
    }

    @Test
    @GitHubIssue("https://github.com/pivotal/spring-boot-migrator/issues/204")
    void replaceSimpleEjbOnMemberAndField() {
        List<String> given = List.of(
                "import javax.ejb.Stateless;\n" +
                        "@Stateless\n" +
                        "public class SomeEjb {}",

                "import javax.ejb.Stateless;\n" +
                        "@Stateless\n" +
                        "public class AnotherEjb {}",

                "import javax.ejb.Stateless;\n" +
                        "import javax.ejb.EJB;\n" +
                        "@Stateless\n" +
                        "public class ClientEjb {\n" +
                        "    @EJB\n" +
                        "    private SomeEjb someEjb;\n" +
                        "    private AnotherEjb anotherEjb;\n" +
                        "    @EJB\n" +
                        "    public void setAnotherEjb(AnotherEjb anotherEjb) {\n" +
                        "        this.anotherEjb = anotherEjb;\n" +
                        "    }\n" +
                        "}"
        );
        List<String> expected = List.of(
                "import javax.ejb.Stateless;\n" +
                        "@Stateless\n" +
                        "public class SomeEjb {}",

                "import javax.ejb.Stateless;\n" +
                        "@Stateless\n" +
                        "public class AnotherEjb {}",

                "import javax.ejb.Stateless;\n" +
                        "\n" +
                        "import org.springframework.beans.factory.annotation.Autowired;\n" +
                        "\n" +
                        "@Stateless\n" +
                        "public class ClientEjb {\n" +
                        "    @Autowired\n" +
                        "    private SomeEjb someEjb;\n" +
                        "    private AnotherEjb anotherEjb;\n" +
                        "\n" +
                        "    @Autowired\n" +
                        "    public void setAnotherEjb(AnotherEjb anotherEjb) {\n" +
                        "        this.anotherEjb = anotherEjb;\n" +
                        "    }\n" +
                        "}"
        );

        MigrateEjbAnnotations sut = new MigrateEjbAnnotations();

        JavaMigrationActionTestSupport.verify(given, expected, sut, "javax.ejb:javax.ejb-api:3.2", "org.springframework:spring-context:5.3.5");
    }

    @Test
    @GitHubIssue("https://github.com/pivotal/spring-boot-migrator/issues/204")
    void replaceEjbWithBeanNameOnField() {
        List<String> given = List.of(
                "import javax.ejb.Stateless;\n" +
                        "@Stateless(name=\"fancyEjb\")\n" +
                        "public class AnotherEjb {}",

                "import javax.ejb.Stateless;\n" +
                        "import javax.ejb.EJB;\n" +
                        "@Stateless\n" +
                        "public class ClientEjb {\n" +
                        "    private AnotherEjb anotherEjb;\n" +
                        "    @EJB(beanName=\"fancyEjb\")\n" +
                        "    public void setAnotherEjb(AnotherEjb anotherEjb) {\n" +
                        "        this.anotherEjb = anotherEjb;\n" +
                        "    }\n" +
                        "}"
        );
        List<String> expected = List.of(
                "import javax.ejb.Stateless;\n" +
                        "@Stateless(name=\"fancyEjb\")\n" +
                        "public class AnotherEjb {}",

                "import org.springframework.beans.factory.annotation.Autowired;\n" +
                        "import org.springframework.beans.factory.annotation.Qualifier;\n" +
                        "\n" +
                        "import javax.ejb.Stateless;\n" +
                        "\n" +
                        "@Stateless\n" +
                        "public class ClientEjb {\n" +
                        "    private AnotherEjb anotherEjb;\n" +
                        "\n" +
                        "    @Autowired\n" +
                        "    @Qualifier(\"fancyEjb\")\n" +
                        "    public void setAnotherEjb(AnotherEjb anotherEjb) {\n" +
                        "        this.anotherEjb = anotherEjb;\n" +
                        "    }\n" +
                        "}"
        );

        MigrateEjbAnnotations sut = new MigrateEjbAnnotations();

        JavaMigrationActionTestSupport.verify(given, expected, sut, "javax.ejb:javax.ejb-api:3.2", "org.springframework:spring-context:5.3.5");
    }


    @Test
    @GitHubIssue("https://github.com/pivotal/spring-boot-migrator/issues/204")
        // TODO: beanInterface gets not migrated, see #219
    void allAtOnce() {
        List<String> given = List.of(
                "public interface Another {}",

                "import javax.ejb.Local;\n" +
                        "@Local\n" +
                        "public interface LocalAnother extends Another {}",

                "import javax.ejb.Remote;\n" +
                        "@Remote\n" +
                        "public interface RemoteAnother extends Another {}",

                "import javax.ejb.Stateless;\n" +
                        "@Stateless\n" +
                        "public class TheEjb {}",

                "import javax.ejb.Stateless;\n" +
                        "@Stateless(name=\"fancyEjb\")\n" +
                        "public class AnotherEjb implements LocalAnother, RemoteAnother {}",

                "import javax.ejb.Stateless;\n" +
                        "import javax.ejb.EJB;\n" +
                        "@Stateless\n" +
                        "public class ClientEjb {\n" +
                        "    @EJB(description = \"the description for theEJB\", lookup = \"ejb:earname/modulename/TheEJB!TheEJB\")\n" +
                        "    private TheEjb theEjb;\n" +
                        "    private Another anotherEjb;\n" +
                        "    @EJB(" +
                        "       beanName=\"fancyEjb\", " +
                        "       description = \"the description\", " +
                        "       beanInterface = LocalAnother.class, " +
                        "       mappedName = \"fancyMappedName\"," +
                        "       name = \"theName\")\n" +
                        "    public void setAnotherEjb(Another anotherEjb) {\n" +
                        "        this.anotherEjb = anotherEjb;\n" +
                        "    }\n" +
                        "}"
        );

        List<String> expected = List.of(
                "public interface Another {}",

                "import javax.ejb.Local;\n" +
                        "@Local\n" +
                        "public interface LocalAnother extends Another {}",

                "import javax.ejb.Remote;\n" +
                        "@Remote\n" +
                        "public interface RemoteAnother extends Another {}",

                "import javax.ejb.Stateless;\n" +
                        "@Stateless\n" +
                        "public class TheEjb {}",

                "import javax.ejb.Stateless;\n" +
                        "@Stateless(name=\"fancyEjb\")\n" +
                        "public class AnotherEjb implements LocalAnother, RemoteAnother {}",

                "import javax.ejb.Stateless;\n" +
                        "\n" +
                        "import org.springframework.beans.factory.annotation.Autowired;\n" +
                        "import org.springframework.beans.factory.annotation.Qualifier;\n" +
                        "\n" +

                        "@Stateless\n" +
                        "public class ClientEjb {\n" +
                        "    /*\n" +
                        "     * the description for theEJB\n" +
                        "     * SBM-TODO: lookup was 'ejb:earname/modulename/TheEJB!TheEJB'\n" +
                        "     */\n" +
                        "    @Autowired\n" +
                        "    private TheEjb theEjb;\n" +
                        "    private Another anotherEjb;\n" +
                        "\n" +
                        "    /*\n" +
                        "     * the description\n" +
                        "     * SBM-TODO: beanInterface was 'LocalAnother.class'\n" +
                        "     */\n" +
                        "    @Autowired\n" +
                        "    @Qualifier(\"fancyEjb\")\n" +
                        "    public void setAnotherEjb(Another anotherEjb) {\n" +
                        "        this.anotherEjb = anotherEjb;\n" +
                        "    }\n" +
                        "}");

        MigrateEjbAnnotations sut = new MigrateEjbAnnotations();

        JavaMigrationActionTestSupport.verify(given, expected, sut, "javax.ejb:javax.ejb-api:3.2", "org.springframework:spring-context:5.3.5");
    }

    @Test
    @GitHubIssue("https://github.com/pivotal/spring-boot-migrator/issues/159")
    void test() {
        List<String> given = List.of(
                "public interface BusinessInterface {}",

                "import javax.ejb.Stateless;\n" +
                        "@Stateless(name=\"TheBeanA\")\n" +
                        "public class BeanA implements BusinessInterface {}",

                "import javax.ejb.Stateless;\n" +
                        "@Stateless(name=\"TheBeanB\")\n" +
                        "public class BeanB implements BusinessInterface {}",

                "import javax.ejb.Stateless;\n" +
                        "import javax.ejb.EJB;\n" +
                        "@Stateless\n" +
                        "public class Client {\n" +
                        "    @EJB(beanName=\"TheBeanA\")\n" +
                        "    private BusinessInterface a;\n" +
                        "    @EJB(beanName=\"TheBeanB\")\n" +
                        "    private BusinessInterface b;\n" +
                        "}"
        );

        List<String> expected = List.of(
                "public interface BusinessInterface {}",

                "import javax.ejb.Stateless;\n" +
                        "@Stateless(name=\"TheBeanA\")\n" +
                        "public class BeanA implements BusinessInterface {}",

                "import javax.ejb.Stateless;\n" +
                        "@Stateless(name=\"TheBeanB\")\n" +
                        "public class BeanB implements BusinessInterface {}",

                "import javax.ejb.Stateless;\n" +
                        "\n" +
                        "import org.springframework.beans.factory.annotation.Autowired;\n" +
                        "import org.springframework.beans.factory.annotation.Qualifier;\n" +
                        "\n" +
                        "@Stateless\n" +
                        "public class Client {\n" +
                        "    @Autowired\n" +
                        "    @Qualifier(\"TheBeanA\")\n" +
                        "    private BusinessInterface a;\n" +
                        "    @Autowired\n" +
                        "    @Qualifier(\"TheBeanB\")\n" +
                        "    private BusinessInterface b;\n" +
                        "}"
        );

        MigrateEjbAnnotations sut = new MigrateEjbAnnotations();

        JavaMigrationActionTestSupport.verify(given, expected, sut, "javax.ejb:javax.ejb-api:3.2", "org.springframework:spring-context:5.3.5");
    }
}