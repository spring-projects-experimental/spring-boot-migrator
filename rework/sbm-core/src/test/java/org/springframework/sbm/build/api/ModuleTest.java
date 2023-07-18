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
package org.springframework.sbm.build.api;

import org.intellij.lang.annotations.Language;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;


class ModuleTest {

    /**
     * Searches for files in {@code /src/main/resources} of this module.
     */
    @Test
    void searchMainResources() {

    }

    /**
     * tests {@link ApplicationModules#getModule(Path)}
     */
    @Test
    void testGetModule() {
        @Language("xml")
        String parentPom =
                """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <groupId>com.acme</groupId>
                    <artifactId>dummy</artifactId>
                    <version>0.0.1-SNAPSHOT</version>
                    <packaging>pom</packaging>
                    <modules>
                        <module>pom1</module>
                        <module>pom2</module>
                    </modules>
                </project>
                """;

        @Language("xml")
        String pom1 =
                """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <parent>
                        <groupId>com.acme</groupId>
                        <artifactId>dummy</artifactId>
                        <version>0.0.1-SNAPSHOT</version>
                        <relativePath>../</relativePath>
                    </parent>
                    <artifactId>pom1</artifactId>
                </project>
                """;

        @Language("xml")
        String pom2 =
                """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <parent>
                        <groupId>com.acme</groupId>
                        <artifactId>dummy</artifactId>
                        <version>0.0.1-SNAPSHOT</version>
                        <relativePath>../</relativePath>
                    </parent>
                    <artifactId>pom2</artifactId>
                    <dependencies>
                        <dependency>
                            <groupId>com.acme</groupId>
                            <artifactId>pom1</artifactId>
                            <version>0.0.1-SNAPSHOT</version>
                        </dependency>
                        <dependency>
                            <groupId>javax.validation</groupId>
                            <artifactId>validation-api</artifactId>
                            <version>2.0.1.Final</version>
                        </dependency>
                    </dependencies>
                </project>
                """;

        String javaSource2 = "public class Module1 {}";
        String javaSource3 = "public class Module2 {}";
        String javaSource4 = "public class SomeTest {}";

        ProjectContext context = TestProjectContext.buildProjectContext()
                .withProjectResource("pom.xml", parentPom)
                .withProjectResource("module1/pom.xml", pom1)
                .withProjectResource("module2/pom.xml", pom2)
                .withJavaSource("module1/src/main/java", javaSource2)
                .withJavaSource("module2/src/main/java", javaSource3)
                .withJavaSource("module2/src/test/java", javaSource4)
                .build();

        Module parentModule = context.getApplicationModules().getModule(Path.of(""));
        Module module1 = context.getApplicationModules().getModule(Path.of("module1"));
        Module module2 = context.getApplicationModules().getModule(Path.of("module2"));

        assertThat(parentModule.getMainJavaSourceSet().list()).isEmpty();

        assertThat(module1.getMainJavaSourceSet().list()).hasSize(1);
        assertThat(module1.getMainJavaSourceSet().list().get(0).print()).isEqualTo(javaSource2);

        assertThat(module2.getMainJavaSourceSet().list()).hasSize(1);
        assertThat(module2.getMainJavaSourceSet().list().get(0).print()).isEqualTo(javaSource3);

        assertThat(module2.getTestJavaSourceSet().list()).hasSize(1);
        assertThat(module2.getTestJavaSourceSet().list().get(0).print()).isEqualTo(javaSource4);
    }

}