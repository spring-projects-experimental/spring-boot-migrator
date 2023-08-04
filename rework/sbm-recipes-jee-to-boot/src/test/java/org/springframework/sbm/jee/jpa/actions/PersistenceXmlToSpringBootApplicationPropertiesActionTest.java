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
package org.springframework.sbm.jee.jpa.actions;

import org.springframework.sbm.boot.properties.api.SpringBootApplicationProperties;
import org.springframework.sbm.boot.properties.search.SpringBootApplicationPropertiesResourceListFilter;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.jee.jpa.filter.PersistenceXmlResourceFilter;
import org.springframework.sbm.jee.jpa.resource.PersistenceXmlProjectResourceRegistrar;
import org.springframework.sbm.project.resource.TestProjectContext;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.sbm.test.ActionTest;

import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PersistenceXmlToSpringBootApplicationPropertiesActionTest {


    @Nested
    class GivenMultiModuleProject {
        @Test
        void whenNoProjectContextExistThenExceptionThrown() {
            String parentPom =
                    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                            "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                            "    <modelVersion>4.0.0</modelVersion>\n" +
                            "    <groupId>com.acme</groupId>\n" +
                            "    <artifactId>dummy</artifactId>\n" +
                            "    <version>0.0.1-SNAPSHOT</version>\n" +
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
                            "</project>";

            ProjectContext projectContext = TestProjectContext.buildProjectContext()
                    .withMavenRootBuildFileSource(parentPom)
                    .withProjectResource("pom1/pom.xml", pom1)
                    .withProjectResource("pom2/pom.xml", pom2)
                    .addRegistrar(new PersistenceXmlProjectResourceRegistrar())
                    .build();

            MigratePersistenceXmlToApplicationPropertiesAction sut = new MigratePersistenceXmlToApplicationPropertiesAction();
            assertThatThrownBy(() -> sut.apply(projectContext)).hasMessage("No file 'META-INF/persistence.xml' could be found.");
        }

        @Test
        @Disabled("Maven reactor bot working yet, see #504")
        void whenNoApplicationPropertiesExistThenPropertiesShouldBeCreated() {
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

            String persistenceXml =
                    "<persistence xmlns=\"http://java.sun.com/xml/ns/persistence/\"\n" +
                            "        xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                            "        xsi:schemaLocation=\"http://java.sun.com/xml/ns/persistence\n" +
                            "          http://java.sun.com/xml/ns/persistence/persistence_1_0.xsd\"\n" +
                            "        version=\"1.0\">\n" +
                            "   <persistence-unit name=\"testPersistenceUnit\" transaction-type=\"RESOURCE_LOCAL\">\n" +
                            "   </persistence-unit>" +
                            "</persistence>";

            ProjectContext projectContext = TestProjectContext.buildProjectContext()
                    .withMavenRootBuildFileSource(parentPom)
                    .withProjectResource("pom1/pom.xml", pom1)
                    .withProjectResource("pom2/pom.xml", pom2)
                    .withProjectResource("pom2/src/main/resources/META-INF/persistence.xml", persistenceXml)
                    .addRegistrar(new PersistenceXmlProjectResourceRegistrar())
                    .build();

            assertThat(projectContext.search(new SpringBootApplicationPropertiesResourceListFilter())).isEmpty();

            MigratePersistenceXmlToApplicationPropertiesAction sut = new MigratePersistenceXmlToApplicationPropertiesAction();
            sut.apply(projectContext);

            List<SpringBootApplicationProperties> applicationProperties = projectContext.search(new SpringBootApplicationPropertiesResourceListFilter());
            assertThat(applicationProperties).hasSize(1);
            assertThat(applicationProperties.get(0).getSourcePath().toString()).isEqualTo("pom1/src/main/resources/application.properties");
        }
    }

    @Test
    void migrateJpaToSpringBoot() {
        ActionTest.withProjectContext(
                TestProjectContext.buildProjectContext()
                        .withProjectResource(Path.of("src/main/resources/META-INF/persistence.xml"), """
                                <persistence version="1.0"
                                             xmlns="http://xmlns.jcp.org/xml/ns/persistence"
                                             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                                             xsi:schemaLocation="http://java.sun.com/xml/ns/persistence http://java.sun.com/xml/ns/persistence/persistence_1_0.xsd">
                                    <persistence-unit name="movie-unit">
                                        <provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>
                                        <jta-data-source>movieDatabase</jta-data-source>
                                        <non-jta-data-source>movieDatabaseUnmanaged</non-jta-data-source>
                                        <properties>
                                            <property name="hibernate.hbm2ddl.auto" value="create-drop"/>
                                            <property name="hibernate.dialect" value="org.hibernate.dialect.HSQLDialect"/>
                                        </properties>
                                    </persistence-unit>
                                </persistence>
                                """)
                        .addRegistrar(new PersistenceXmlProjectResourceRegistrar()))
                .actionUnderTest(new MigratePersistenceXmlToApplicationPropertiesAction())
                .verify(context -> {
                    List<SpringBootApplicationProperties> applicationProperties = context.search(new SpringBootApplicationPropertiesResourceListFilter());
                    SpringBootApplicationProperties springBootApplicationProperties = applicationProperties.get(0);
                    assertThat(springBootApplicationProperties.getProperty("spring.jpa.hibernate.ddl-auto").get()).isEqualTo("create-drop");
                    assertThat(springBootApplicationProperties.getProperty("spring.jpa.database-platform").get()).isEqualTo("org.hibernate.dialect.HSQLDialect");
                    assertThat(context.search(new PersistenceXmlResourceFilter())).isNotEmpty();
                });



    }
}
