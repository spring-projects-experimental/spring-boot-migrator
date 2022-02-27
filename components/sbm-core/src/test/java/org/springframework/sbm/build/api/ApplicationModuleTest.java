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

import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class ApplicationModuleTest {

    @Test
    @Disabled("TODO: Maven Reactor")
    void testGetModuleResources() {
        String parentPom =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>com.acme</groupId>\n" +
                        "    <artifactId>dummy</artifactId>\n" +
                        "    <version>0.0.1-SNAPSHOT</version>\n" +
                        "    <packaging>pom</packaging>" +
                        "</project>";
        String pom1 =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <parent>\n" +
                        "        <groupId>com.acme</groupId>\n" +
                        "        <artifactId>dummy</artifactId>\n" +
                        "        <version>0.0.1-SNAPSHOT</version>\n" +
                        "        <relativePath>../</relativePath>\n" +
                        "    </parent>\n" +
                        "    <artifactId>pom1</artifactId>\n" +
                        "</project>";
        String pom2 =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <parent>\n" +
                        "        <groupId>com.acme</groupId>\n" +
                        "        <artifactId>dummy</artifactId>\n" +
                        "        <version>0.0.1-SNAPSHOT</version>\n" +
                        "        <relativePath>../</relativePath>\n" +
                        "    </parent>\n" +
                        "    <artifactId>pom2</artifactId>\n" +
                        "    <dependencies>\n" +
                        "        <dependency>\n" +
                        "            <groupId>com.acme</groupId>\n" +
                        "            <artifactId>pom1</artifactId>\n" +
                        "            <version>0.0.1-SNAPSHOT</version>\n" +
                        "        </dependency>\n" +
                        "    </dependencies>\n" +
                        "</project>";

        String javaSource2 = "public class Module1 {}";
        String javaSource3 = "public class Module2 {}";

        ProjectContext context = TestProjectContext.buildProjectContext()
                .addProjectResource("pom.xml", parentPom)
                .addProjectResource("module1/pom.xml", pom1)
                .addProjectResource("module2/pom.xml", pom2)
                .addJavaSource("module1/src/main/java", javaSource2)
                .addJavaSource("module2/src/main/java", javaSource3)
                .build();

        ApplicationModule parentModule = context.getApplicationModules().getModule(Path.of(""));
        ApplicationModule module1 = context.getApplicationModules().getModule(Path.of("module1"));
        ApplicationModule module2 = context.getApplicationModules().getModule(Path.of("module2"));

        assertThat(parentModule.getMainJavaSourceSet().list()).isEmpty();

        assertThat(module1.getMainJavaSourceSet().list()).hasSize(1);
        assertThat(module1.getMainJavaSourceSet().list().get(0).print()).isEqualTo(javaSource2);

        assertThat(module2.getMainJavaSourceSet().list()).hasSize(1);
        assertThat(module2.getMainJavaSourceSet().list().get(0).print()).isEqualTo(javaSource3);
    }

}