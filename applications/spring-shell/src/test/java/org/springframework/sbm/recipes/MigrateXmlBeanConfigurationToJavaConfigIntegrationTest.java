/*
 * Copyright 2021 - 2023 the original author or authors.
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
package org.springframework.sbm.recipes;

import org.springframework.sbm.IntegrationTestBaseClass;
import org.springframework.sbm.testhelper.common.utils.TestDiff;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MigrateXmlBeanConfigurationToJavaConfigIntegrationTest extends IntegrationTestBaseClass {

    @Override
    protected String getTestSubDir() {
        return "xml-to-java-config";
    }

    @Test
    @Tag("integration")
    void testRecipe() {

        String expected =
                "package org.springframework.sbm.spring.xml.example;\n" +
                "\n" +
                "import java.lang.String;\n" +
                "import java.util.List;\n" +
                "import org.springframework.context.annotation.Bean;\n" +
                "import org.springframework.context.annotation.Configuration;\n" +
                "\n" +
                "@Configuration\n" +
                "public class MyApplicationContext {\n" +
                "  @Bean\n" +
                "  public TestBean country() {\n" +
                "    TestBean country = new TestBean(\"India\", \"20000\");\n" +
                "    return country;\n" +
                "  }\n" +
                "\n" +
                "  @Bean\n" +
                "  public AnotherServiceImpl anotherService() {\n" +
                "    AnotherServiceImpl anotherService = new AnotherServiceImpl(appleService());\n" +
                "    anotherService.setTheList(myList());\n" +
                "    return anotherService;\n" +
                "  }\n" +
                "\n" +
                "  @Bean\n" +
                "  public List<String> myList() {\n" +
                "    List<String> myList = new ArrayList<>();\n" +
                "    myList.add(\"foo\");\n" +
                "    myList.add(\"bar\");\n" +
                "    return myList;\n" +
                "  }\n" +
                "\n" +
                "  @Bean\n" +
                "  public AppleService appleService() {\n" +
                "    AppleService appleService = new AppleService();\n" +
                "    appleService.setCountry(country());\n" +
                "    return appleService;\n" +
                "  }\n" +
                "}\n";

        intializeTestProject();
        scanProject();
        applyRecipe(
                "initialize-spring-boot-migration",
                "migrate-spring-xml-to-java-config"
        );
        String myApplicationContext = super.loadJavaFile("org.springframework.sbm.spring.xml.example", "MyApplicationContext");

        assertThat(myApplicationContext)
                .as(TestDiff.of(myApplicationContext, expected))
                .isEqualTo(expected);

    }
}
