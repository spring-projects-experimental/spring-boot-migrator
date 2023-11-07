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
package org.springframework.sbm.boot.common.actions;

import org.intellij.lang.annotations.Language;
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

    private AddSpringBootContextTestClassAction sut;

    // TODO: add missing tests: test adding test with no classes in src/test/java but in src/main/java -> root package must be taken from src/main/java then


    @BeforeEach
    void setUp() throws IOException {
        Version version = new Version("2.3.0");
        Configuration configuration = new Configuration(version);
        configuration.setTemplateLoader(new FileTemplateLoader(new File("./src/main/resources/templates")));
        sut = new AddSpringBootContextTestClassAction();
        sut.setConfiguration(configuration);
    }

    @Nested
    public class GivenSingleModuleProject {
        @Test
        void testApplyShouldAddNewSource() {

            @Language("java")
            String expectedTestClassSource = """
                    package org.springframework.sbm.root.test;
                    
                    import org.junit.jupiter.api.Test;
                    import org.springframework.boot.test.context.SpringBootTest;
                    
                    @SpringBootTest
                    class SpringBootAppTest {
                    
                        @Test
                        void contextLoads() {
                        }
                    
                    }
                    """;

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
            assertThat(context.getProjectJavaSources().list().get(2).print()).isEqualToNormalizingNewlines(expectedTestClassSource);
        }
    }

    @Nested
    public class GivenMultiModuleProject {
        @Language("xml")
        private static final String parentPom = """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0"
                         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <groupId>com.example.sbm</groupId>
                    <artifactId>parent</artifactId>
                    <version>0.1.0-SNAPSHOT</version>
                    <modelVersion>4.0.0</modelVersion>
                    <packaging>pom</packaging>
                    <modules>
                        <module>module1</module>
                        <module>module2</module>
                    </modules>
                </project>
                """;

        @Language("xml")
        private static final String childPom1 = """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0"
                         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <parent>
                        <groupId>com.example.sbm</groupId>
                        <artifactId>parent</artifactId>
                        <version>0.1.0-SNAPSHOT</version>
                        <relativePath>../pom.xml</relativePath>
                    </parent>
                    <artifactId>module1</artifactId>
                    <modelVersion>4.0.0</modelVersion>
                    <dependencies>
                        <dependency>
                            <groupId>com.example.sbm</groupId>
                            <artifactId>module2</artifactId>
                            <version>0.1.0-SNAPSHOT</version>
                        </dependency>
                    </dependencies>
                </project>
                """;

        @Language("xml")
        private static final String childPom2 = """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0"
                         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <parent>
                        <groupId>com.example.sbm</groupId>
                        <artifactId>parent</artifactId>
                        <version>0.1.0-SNAPSHOT</version>
                        <relativePath>../pom.xml</relativePath>
                    </parent>
                    <artifactId>module2</artifactId>
                    <modelVersion>4.0.0</modelVersion>
                </project>
                """;

        @Language("java")
        private static final String javaClass1 = """
                package com.example.sbm;
                public class SomeClass {}
                """;

        @Test
        void test() {
            SbmApplicationProperties sbmApplicationProperties = new SbmApplicationProperties();
            sbmApplicationProperties.setDefaultBasePackage("com.example.sbm");

            ProjectContext projectContext = TestProjectContext.buildProjectContext()
                    .withProjectResource("pom.xml", parentPom)
                    .withApplicationProperties(sbmApplicationProperties)
                    .withProjectResource("module1/pom.xml", childPom1)
                    .withProjectResource("module2/pom.xml", childPom2)
                    .withJavaSourceInModule("module1/src/main/java", javaClass1)
                    .build();

            sut.setSbmApplicationProperties(sbmApplicationProperties);

            sut.apply(projectContext);

            assertThat(projectContext.getApplicationModules().findModule("com.example.sbm:parent:0.1.0-SNAPSHOT").get().getMainJavaSourceSet().list()).isEmpty();
            assertThat(projectContext.getApplicationModules().findModule("com.example.sbm:parent:0.1.0-SNAPSHOT").get().getTestJavaSourceSet().list()).isEmpty();
            assertThat(projectContext.getApplicationModules().findModule("com.example.sbm:module1:0.1.0-SNAPSHOT").get().getTestJavaSourceSet().list().get(0).getTypes().get(0).getFullyQualifiedName()).isEqualTo("com.example.sbm.SpringBootAppTest");
            assertThat(projectContext.getApplicationModules().findModule("com.example.sbm:module2:0.1.0-SNAPSHOT").get().getTestJavaSourceSet().list()).isEmpty();
        }
    }
}
