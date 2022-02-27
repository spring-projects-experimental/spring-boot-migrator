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
package org.springframework.sbm.actions.spring.xml.migration;

import com.squareup.javapoet.TypeSpec;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

// FIXME: repair test
class XmlToJavaConfigurationMigrationIntegrationTest {

//    MigrateXmlToJavaConfigurationAction sut = new MigrateXmlToJavaConfigurationAction(new Helper(), new BeanMethodFactory());

    @Test
    void readBeanDefinition() {

        String xml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<beans xmlns=\"http://www.springframework.org/schema/beans\"\n" +
                        "       xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:context=\"http://www.springframework.org/schema/context\"\n" +
                        "       xmlns:aop=\"http://www.springframework.org/schema/aop\"\n" +
                        "       xmlns:util=\"http://www.springframework.org/schema/util\"\n" +
                        "       xsi:schemaLocation=\"http://www.springframework.org/schema/beans\n" +
                        "    http://www.springframework.org/schema/beans/spring-beans-3.0.xsd\n" +
                        "    http://www.springframework.org/schema/context\n" +
                        "    http://www.springframework.org/schema/context/spring-context-3.0.xsd\n" +
                        "    http://www.springframework.org/schema/util\n" +
                        "    http://www.springframework.org/schema/util/spring-util-3.0.xsd\n" +
                        "    http://www.springframework.org/schema/aop\n" +
                        "    http://www.springframework.org/schema/aop/spring-aop-3.0.xsd\">\n" +
                        "\n" +
                        "    <bean name=\"country\" class=\"org.springframework.sbm.actions.spring.xml.example.TestBean\">\n" +
                        "        <constructor-arg index=\"0\" value=\"India\"></constructor-arg>\n" +
                        "        <constructor-arg index=\"1\" value=\"20000\"></constructor-arg>\n" +
                        "    </bean>\n" +
                        "    <bean id=\"appleService\" class=\"org.springframework.sbm.actions.spring.xml.example.AppleService\">\n" +
                        "        <property name=\"country\" ref=\"country\"/>\n" +
                        "    </bean>\n" +
                        "    <bean id=\"anotherService\" class=\"org.springframework.sbm.actions.spring.xml.example.AnotherServiceImpl\">\n" +
                        "        <constructor-arg ref=\"appleService\"/>\n" +
                        "        <property name=\"theList\" ref=\"myList\"/>\n" +
                        "        <property name=\"favoriteInteger\" value=\"35\"/>" +
                        "        <property name=\"favoriteString\" value=\"Hello, I am a string\"/>" +
                        "    </bean>\n" +
                        "\n" +
                        "    <util:list id=\"myList\" value-type=\"java.lang.String\">\n" +
                        "        <value>foo</value>\n" +
                        "        <value>bar</value>\n" +
                        "    </util:list>\n" +
                        "\n" +
                        "    <context:annotation-config />\n" +
                        "</beans>";

        String expected = "package com.example;\n" +
                "\n" +
                "import org.springframework.sbm.actions.spring.xml.example.AnotherServiceImpl;\n" +
                "import org.springframework.sbm.actions.spring.xml.example.AppleService;\n" +
                "import org.springframework.sbm.actions.spring.xml.example.TestBean;\n" +
                "import java.lang.String;\n" +
                "import java.util.List;\n" +
                "import org.springframework.context.annotation.Bean;\n" +
                "import org.springframework.context.annotation.Configuration;\n" +
                "\n" +
                "@Configuration\n" +
                "public class ApplicationContext {\n" +
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
                "    anotherService.setFavoriteInteger(35);\n" +
                "    anotherService.setFavoriteString(\"Hello, I am a string\");\n" +
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



    }


    @Test
    void migratePropertyList() {
        String xml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<beans xmlns=\"http://www.springframework.org/schema/beans\"\n" +
                        "       xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:context=\"http://www.springframework.org/schema/context\"\n" +
                        "       xmlns:aop=\"http://www.springframework.org/schema/aop\"\n" +
                        "       xmlns:util=\"http://www.springframework.org/schema/util\"\n" +
                        "       xsi:schemaLocation=\"http://www.springframework.org/schema/beans\n" +
                        "    http://www.springframework.org/schema/beans/spring-beans-3.0.xsd\n" +
                        "    http://www.springframework.org/schema/context\n" +
                        "    http://www.springframework.org/schema/context/spring-context-3.0.xsd\n" +
                        "    http://www.springframework.org/schema/util\n" +
                        "    http://www.springframework.org/schema/util/spring-util-3.0.xsd\n" +
                        "    http://www.springframework.org/schema/aop\n" +
                        "    http://www.springframework.org/schema/aop/spring-aop-3.0.xsd\">\n" +
                        "    <util:list id=\"myList\" value-type=\"java.lang.String\">\n" +
                        "        <value>foo</value>\n" +
                        "        <value>bar</value>\n" +
                        "    </util:list>\n" +
                        "\n" +
                        "    <context:annotation-config />\n" +
                        "</beans>";

        String expected = "package com.example;\n\n" +
                "import java.lang.String;\n" +
                "import java.util.List;\n" +
                "import org.springframework.context.annotation.Bean;\n" +
                "import org.springframework.context.annotation.Configuration;\n\n" +
                "@Configuration\n" +
                "public class ApplicationContext {\n" +
                "  @Bean\n" +
                "  public List<String> myList() {\n" +
                "    List<String> myList = new ArrayList<>();\n" +
                "    myList.add(\"foo\");\n" +
                "    myList.add(\"bar\");\n" +
                "    return myList;\n" +
                "  }\n" +
                "}\n";

//        Resource testResource = new TestResource(Path.of("some/path/applicationContext.xml"), xml);
//        MigrationContext migrationContext = new MigrationContext(new URLClassLoader(new URL[0]));
//        String config  = sut.migrateToJavaBeanConfiguration(migrationContext, "com.example", testResource, Path.of("some/path/applicationContext.xml"));
//        assertThat(expected).as(TestDiff.of(config, expected))
//                .isEqualTo(config);
//        System.out.println(config);
    }

    @Test
    void testCreateConfigurationClassFromFilename() {
        Helper sut = new Helper();
        TypeSpec.Builder builder = Helper.createConfigurationClassFromFilename(Path.of("some/path/applicationContext.xml").getFileName().toString());
        String result = builder.build().toString();
        String expected = "@org.springframework.context.annotation.Configuration\n" +
                "public class ApplicationContext {\n" +
                "}\n";
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void calculateClassName() {
        Helper sut = new Helper();
        String filename = "applicationContext.xml";
        String classname = Helper.calculateClassname(filename);
        assertThat("ApplicationContext").isEqualTo(classname);

        filename = "application-context-file.xml";
        classname = Helper.calculateClassname(filename);
        assertThat("ApplicationContextFile").isEqualTo(classname);
    }

}