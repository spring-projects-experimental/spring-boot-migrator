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

class MigrateLocalStatelessSessionBeansTest {

    MigrateLocalStatelessSessionBeansHelper helper = new MigrateLocalStatelessSessionBeansHelper();
    MigrateLocalStatelessSessionBeans sut = new MigrateLocalStatelessSessionBeans(helper);

    @GitHubIssue("https://github.com/pivotal/spring-boot-migrator/issues/236")
    @Test
    void migrateLocalBean() {
        String givenBean =
                "import javax.ejb.*;\n" +
                        "@Singleton(name = \"DaSingleton\")\n" +
                        "@LocalBean\n" +
                        "public class LocalBeanAndLocalAnnotatedBean {}";

        String expectedBean =
                "import org.springframework.stereotype.Service;\n" +
                        "\n" +
                        "\n" +
                        "@Service(\"DaSingleton\")\n" +
                        "public class LocalBeanAndLocalAnnotatedBean {}";

        JavaMigrationActionTestSupport.verify(
                List.of(givenBean),
                List.of(expectedBean),
                sut,
                "javax.ejb:javax.ejb-api:3.2",
                "org.springframework:spring-context:5.3.5");
    }

    @GitHubIssue("https://github.com/pivotal/spring-boot-migrator/issues/201")
    @Test
    void migrateSimpleStatelessAnnotation_201() {
        String givenBean =
                "import javax.ejb.Stateless;\n" +
                        "@Stateless(name=\"theBeanName\", description=\"Hello, hello\", beanName=\"aFancyBeanName\")\n" +
                        "public class ImplementingBean2 {}";

        String expectedBean =
                "import org.springframework.stereotype.Service;\n" +
                        "\n" +
                        "/**\n" +
                        "* Hello, hello\n" +
                        "*/\n" +
                        "@Service(\"theBeanName\")\n" +
                        "public class ImplementingBean2 {}";

        JavaMigrationActionTestSupport.verify(
                List.of(givenBean),
                List.of(expectedBean),
                sut,
                "javax.ejb:javax.ejb-api:3.2",
                "org.springframework:spring-context:5.3.5");
    }

    @GitHubIssue("https://github.com/pivotal/spring-boot-migrator/issues/26")
    @Test
    void migrateBeanWithLocalStatelessAndInterface_26() {
        String givenInterface =
                "public interface BusinessInterface {}";

        String givenBean =
                "import javax.ejb.Local;\n" +
                        "import javax.ejb.Stateless;\n" +
                        "@Stateless(name=\"theBeanName\", description=\"Hello, hello\")\n" +
                        "@Local\n" +
                        "public class ImplementingBean implements BusinessInterface {}";

        String expectedBean =
                "import org.springframework.stereotype.Service;\n" +
                        "\n" +
                        "/**\n" +
                        "* Hello, hello\n" +
                        "*/\n" +
                        "@Service(\"theBeanName\")\n" +
                        "public class ImplementingBean implements BusinessInterface {}";

        JavaMigrationActionTestSupport.verify(
                List.of(givenInterface, givenBean),
                List.of(givenInterface, expectedBean),
                sut,
                "javax.ejb:javax.ejb-api:3.2",
                "org.springframework:spring-context:5.3.5"
        );
    }

    @GitHubIssue("https://github.com/pivotal/spring-boot-migrator/issues/157")
    @Test
    void migrateEjbWithLocalBusinessInterface_157() {

        String givenBusinessInterface =
                "import javax.ejb.Local;\n" +
                        "@Local\n" +
                        "public interface BusinessInterface {}";

        String expectedBusinessInterface =
                "public interface BusinessInterface {}";

        String givenSessionBean =
                "import javax.ejb.Stateless;\n" +
                        "@Stateless\n" +
                        "public class SessionBean implements BusinessInterface {}";

        String expectedSessionBean =
                "import org.springframework.stereotype.Service;\n" +
                        "\n" +
                        "@Service\n" +
                        "public class SessionBean implements BusinessInterface {}";

        JavaMigrationActionTestSupport.verify(
                List.of(givenBusinessInterface, givenSessionBean),
                List.of(expectedBusinessInterface, expectedSessionBean),
                sut,
                "javax.ejb:javax.ejb-api:3.2",
                "org.springframework:spring-context:5.3.5"
        );
    }

    @GitHubIssue("https://github.com/pivotal/spring-boot-migrator/issues/158")
    @Test
    void test_158() {
        String givenBusinessInterface =
                "public interface BusinessInterface {}";

        String expectedBusinessInterface =
                "public interface BusinessInterface {}";

        String givenSessionBean =
                "import javax.ejb.Stateless;\n" +
                        "import javax.ejb.Local;\n" +
                        "@Stateless(name = \"beanyBean\")\n" +
                        "@Local(BusinessInterface.class)\n" +
                        "public class TheBean implements BusinessInterface {}";

        String expectedSessionBean =
                "import org.springframework.stereotype.Service;\n" +
                        "\n" +
                        "@Service(\"beanyBean\")\n" +
                        "public class TheBean implements BusinessInterface {}";

        JavaMigrationActionTestSupport.verify(
                List.of(givenBusinessInterface, givenSessionBean),
                List.of(expectedBusinessInterface, expectedSessionBean),
                sut,
                "javax.ejb:javax.ejb-api:3.2",
                "org.springframework:spring-context:5.3.5"
        );
    }
}