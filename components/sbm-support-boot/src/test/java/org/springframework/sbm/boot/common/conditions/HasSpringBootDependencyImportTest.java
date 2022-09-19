package org.springframework.sbm.boot.common.conditions;

import org.junit.jupiter.api.Test;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;

import static org.assertj.core.api.Assertions.assertThat;

class HasSpringBootDependencyImportTest {

    @Test
    public void conditionShouldBeTrueForImport() {
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
                                <artifactId>spring-boot-dependencies</artifactId>
                                <version>2.7.0</version>
                                <type>pom</type>
                                <scope>import</scope>
                            </dependency>
                        </dependencies>
                    </dependencyManagement>
                </project>
                        """)
                .build();

        HasSpringBootDependencyImport condition = new HasSpringBootDependencyImport();
        condition.setVersionPattern("2\\.7\\..*");

        boolean result = condition.evaluate(projectContext);

        assertThat(result).isTrue();
    }

    @Test
    public void conditionShouldBeFalseForMissingImport() {
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
                </project>
                        """)
                .build();

        HasSpringBootDependencyImport condition = new HasSpringBootDependencyImport();
        condition.setVersionPattern("2\\.7\\..*");

        boolean result = condition.evaluate(projectContext);

        assertThat(result).isFalse();
    }

    @Test
    public void conditionShouldBeFalseForOldVersion() {
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
                                <artifactId>spring-boot-dependencies</artifactId>
                                <version>2.6.0</version>
                                <type>pom</type>
                                <scope>import</scope>
                            </dependency>
                        </dependencies>
                    </dependencyManagement>
                </project>
                        """)
                .build();

        HasSpringBootDependencyImport condition = new HasSpringBootDependencyImport();
        condition.setVersionPattern("2\\.7\\..*");

        boolean result = condition.evaluate(projectContext);

        assertThat(result).isFalse();
    }

    @Test
    public void conditionShouldBeTrueForVersionInVariable() {
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
                            <properties>
                                <spring-boot.version>2.7.1</spring-boot.version>
                            </properties>                                  
                            <dependencyManagement>
                                 <dependencies>
                                    <dependency>
                                        <groupId>org.springframework.boot</groupId>
                                        <artifactId>spring-boot-dependencies</artifactId>
                                        <version>${spring-boot.version}</version>
                                        <type>pom</type>
                                        <scope>import</scope>
                                    </dependency>
                                </dependencies>
                            </dependencyManagement>
                        </project>
                                """)
                .build();

        HasSpringBootDependencyImport condition = new HasSpringBootDependencyImport();
        condition.setVersionPattern("2\\.7\\..*");

        boolean result = condition.evaluate(projectContext);

        assertThat(result).isTrue();
    }
}
