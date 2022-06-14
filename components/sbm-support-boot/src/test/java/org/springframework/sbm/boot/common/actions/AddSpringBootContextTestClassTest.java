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
package org.springframework.sbm.boot.common.actions;

import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.SbmApplicationProperties;
import org.springframework.sbm.project.resource.TestProjectContext;
import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Version;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class AddSpringBootContextTestClassTest {

    private Configuration configuration;

    private AddSpringBootContextTestClassAction sut;

    // TODO: add missing tests: test adding test with no classes in src/test/java but in src/main/java -> root package must be taken from src/main/java then


    @BeforeEach
    void setUp() throws IOException {
        Version version = new Version("2.3.0");
        configuration = new Configuration(version);
        configuration.setTemplateLoader(new FileTemplateLoader(new File("./src/main/resources/templates")));
        sut = new AddSpringBootContextTestClassAction();
        sut.setConfiguration(configuration);
    }

    @Nested
    public class GivenSingleModuleProject {
        @Test
        void testApplyShouldAddNewSource() {

            String expectedTestClassSource =
                    "package org.springframework.sbm.root.test;\n" +
                    "\n" +
                    "import org.junit.jupiter.api.Test;\n" +
                    "import org.springframework.boot.test.context.SpringBootTest;\n" +
                    "\n" +
                    "@SpringBootTest\n" +
                    "class SpringBootAppTest {\n" +
                    "\n" +
                    "    @Test\n" +
                    "    void contextLoads() {\n" +
                    "    }\n" +
                    "\n" +
                    "}\n";

            SbmApplicationProperties sbmApplicationProperties = new SbmApplicationProperties();
            sbmApplicationProperties.setDefaultBasePackage("foo.bar");

            ProjectContext context = TestProjectContext.buildProjectContext()
                    .withDummyRootBuildFile()
                    .withApplicationProperties(sbmApplicationProperties)
                    .withJavaTestSources(
                            // This package is the root package
                            "package org.springframework.sbm.root.test;\n" +
                                    "public class ExistingTest {}",

                            "package org.springframework.sbm.root.test.sub;\n" +
                                    "public class AnotherExistingTest {}"
                    )
                    .build();

            assertThat(context.getProjectJavaSources().list()).hasSize(2);

            sut.setSbmApplicationProperties(sbmApplicationProperties);
            sut.apply(context);

            assertThat(context.getProjectJavaSources().list()).hasSize(3);
            assertThat(context.getProjectJavaSources().list().get(2).getPackageName()).isEqualTo("org.springframework.sbm.root.test");
            assertThat(context.getProjectJavaSources().list().get(2).print()).isEqualTo(expectedTestClassSource);
        }
    }

    @Nested
    public class GivenMultiModuleProject {
        private static final String parentPom =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                        "         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <groupId>com.example.sbm</groupId>\n" +
                        "    <artifactId>parent</artifactId>\n" +
                        "    <version>0.1.0-SNAPSHOT</version>\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <packaging>pom</packaging>\n" +
                        "    <modules>\n" +
                        "        <module>module1</module>\n" +
                        "        <module>module2</module>\n" +
                        "    </modules>" +
                        "</project>\n";

        private static final String childPom1 =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                        "         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <parent>\n" +
                        "        <groupId>com.example.sbm</groupId>\n" +
                        "        <artifactId>parent</artifactId>\n" +
                        "        <version>0.1.0-SNAPSHOT</version>\n" +
                        "        <relativePath>../pom.xml</relativePath>\n" +
                        "    </parent>\n" +
                        "    <artifactId>module1</artifactId>\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <dependencies>\n" +
                        "        <dependency>\n" +
                        "            <groupId>com.example.sbm</groupId>\n" +
                        "            <artifactId>module2</artifactId>\n" +
                        "            <version>0.1.0-SNAPSHOT</version>\n" +
                        "        </dependency>\n" +
                        "    </dependencies>\n" +
                        "</project>\n";

        private static final String childPom2 =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                        "         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <parent>\n" +
                        "        <groupId>com.example.sbm</groupId>\n" +
                        "        <artifactId>parent</artifactId>\n" +
                        "        <version>0.1.0-SNAPSHOT</version>\n" +
                        "        <relativePath>../pom.xml</relativePath>\n" +
                        "    </parent>\n" +
                        "    <artifactId>module2</artifactId>\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "</project>\n";


        private static final String javaClass1 =
                "package com.example.sbm;\n" +
                        "public class SomeClass {}";

        @Test
        void test() {
            SbmApplicationProperties sbmApplicationProperties = new SbmApplicationProperties();
            sbmApplicationProperties.setDefaultBasePackage("com.example.sbm");

            ProjectContext projectContext = TestProjectContext.buildProjectContext()
                    .addProjectResource("pom.xml", parentPom)
                    .withApplicationProperties(sbmApplicationProperties)
                    .addProjectResource("module1/pom.xml", childPom1)
                    .addProjectResource("module2/pom.xml", childPom2)
                    .addJavaSourceToModule("module1/src/main/java", javaClass1)
                    .build();

            sut.setSbmApplicationProperties(sbmApplicationProperties);

            sut.apply(projectContext);

            assertThat(projectContext.getApplicationModules().getModule("").getMainJavaSourceSet().list()).isEmpty();
            assertThat(projectContext.getApplicationModules().getModule("").getTestJavaSourceSet().list()).isEmpty();
            assertThat(projectContext.getApplicationModules().getModule("module1").getTestJavaSourceSet().list().get(0).getTypes().get(0).getFullyQualifiedName()).isEqualTo("com.example.sbm.SpringBootAppTest");
            assertThat(projectContext.getApplicationModules().getModule("module2").getTestJavaSourceSet().list()).isEmpty();
        }
    }


}
