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

package org.springframework.sbm.boot.common.conditions;

import org.junit.jupiter.api.Test;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;

import static org.assertj.core.api.Assertions.assertThat;

class HasSpringBootDependencyManuallyManagedTest {

    @Test
    public void conditionToBeTrueIfSpringProjectIsManuallyManaged() {
        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withMavenRootBuildFileSource("""
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.example</groupId>
    <artifactId>explicit-deps-app</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>explicit-deps-app</name>
    <description>explicit-deps-app</description>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
            <version>2.7.3</version>
        </dependency>
        <dependency>
            <groupId>io.dropwizard.metrics</groupId>
            <artifactId>metrics-annotation</artifactId>
            <version>4.2.8</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <version>2.7.3</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
</project>
                """)
                .build();

        HasSpringBootDependencyManuallyManaged condition = new HasSpringBootDependencyManuallyManaged();
        condition.setVersionPattern("2\\.7\\..*");

        boolean result = condition.evaluate(projectContext);

        assertThat(result).isTrue();
    }

    @Test
    public void conditionToBeFalseIfNoSpringBootDependency() {
        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withMavenRootBuildFileSource("""
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.example</groupId>
    <artifactId>explicit-deps-app</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>explicit-deps-app</name>
    <description>explicit-deps-app</description>

    <dependencies>
        <dependency>
            <groupId>io.dropwizard.metrics</groupId>
            <artifactId>metrics-annotation</artifactId>
            <version>4.2.8</version>
        </dependency>
    </dependencies>
</project>
                """)
                .build();

        HasSpringBootDependencyManuallyManaged condition = new HasSpringBootDependencyManuallyManaged();
        condition.setVersionPattern("2\\.7\\..*");

        boolean result = condition.evaluate(projectContext);

        assertThat(result).isFalse();
    }

    @Test
    public void conditionToBeFalseByOldVersion() {
        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withMavenRootBuildFileSource("""
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.example</groupId>
    <artifactId>explicit-deps-app</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>explicit-deps-app</name>
    <description>explicit-deps-app</description>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
            <version>2.6.0</version>
        </dependency>
        <dependency>
            <groupId>io.dropwizard.metrics</groupId>
            <artifactId>metrics-annotation</artifactId>
            <version>4.2.8</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <version>2.6.0</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
</project>
                """)
                .build();

        HasSpringBootDependencyManuallyManaged condition = new HasSpringBootDependencyManuallyManaged();
        condition.setVersionPattern("2\\.7\\..*");

        boolean result = condition.evaluate(projectContext);

        assertThat(result).isFalse();
    }

    @Test
    public void conditionToBeTrueIfManagedDependencies() {
        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withMavenRootBuildFileSource("""
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.example</groupId>
    <artifactId>explicit-deps-app</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>explicit-deps-app</name>
    <description>explicit-deps-app</description>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-web</artifactId>
                <version>2.7.3</version>
            </dependency>
            <dependency>
                <groupId>io.dropwizard.metrics</groupId>
                <artifactId>metrics-annotation</artifactId>
                <version>4.2.8</version>
            </dependency>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-test</artifactId>
                <version>2.7.3</version>
                <scope>test</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
</project>
                """)
                .build();

        HasSpringBootDependencyManuallyManaged condition = new HasSpringBootDependencyManuallyManaged();
        condition.setVersionPattern("2\\.7\\..*");

        boolean result = condition.evaluate(projectContext);

        assertThat(result).isTrue();
    }
}
