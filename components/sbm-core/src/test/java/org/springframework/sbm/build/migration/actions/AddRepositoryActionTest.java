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
package org.springframework.sbm.build.migration.actions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.openrewrite.internal.lang.Nullable;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Test adding a Repository to a pom.xml")
class AddRepositoryActionTest {

    @Nested
    @DisplayName("Without Repositories section")
    public static class ToPomWithoutRepositories {
        String pomWithoutRepositories =
                "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>some.group.id</groupId>\n" +
                        "    <artifactId>with-artifact</artifactId>\n" +
                        "    <packaging>jar</packaging>\n" +
                        "    <version>100.23.01-SNAPSHOT</version>\n" +
                        "</project>";
        private ProjectContext context;

        @BeforeEach
        void beforeEach() {
            context = TestProjectContext.buildProjectContext()
                    .withMavenRootBuildFileSource(pomWithoutRepositories)
                    .build();
        }

        @Test
        void shouldAddANewRepositoriesSection() {

            AddRepositoryAction sut = new AddRepositoryAction();
            sut.setId("repo-id");
            sut.setName("thename");
            sut.setUrl("https://some.url");
            sut.apply(context);

            assertThat(context.getBuildFile().print()).isEqualTo(
                    "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                            "        xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">\n" +
                            "    <modelVersion>4.0.0</modelVersion>\n" +
                            "    <groupId>some.group.id</groupId>\n" +
                            "    <artifactId>with-artifact</artifactId>\n" +
                            "    <packaging>jar</packaging>\n" +
                            "    <version>100.23.01-SNAPSHOT</version>\n" +
                            "    <repositories>\n" +
                            "        <repository>\n" +
                            "            <id>repo-id</id>\n" +
                            "            <name>thename</name>\n" +
                            "            <url>https://some.url</url>\n" +
                            "        </repository>\n" +
                            "    </repositories>\n" +
                            "</project>"
            );
        }

        @Test
        void shouldAddANewRepositoriesSectionWithReleasesInformation() {

            AddRepositoryAction sut = new AddRepositoryAction();
            sut.setId("repo-id");
            sut.setUrl("https://some.url");
            sut.setLayout("some-layout");
            sut.setReleasesEnabled(true);
            sut.setReleasesChecksumPolicy("some-policy");
            sut.setReleasesUpdatePolicy("some-update-policy");
            sut.apply(context);

            assertThat(context.getBuildFile().print()).isEqualTo(
                    "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                            "        xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">\n" +
                            "    <modelVersion>4.0.0</modelVersion>\n" +
                            "    <groupId>some.group.id</groupId>\n" +
                            "    <artifactId>with-artifact</artifactId>\n" +
                            "    <packaging>jar</packaging>\n" +
                            "    <version>100.23.01-SNAPSHOT</version>\n" +
                            "    <repositories>\n" +
                            "        <repository>\n" +
                            "            <id>repo-id</id>\n" +
                            "            <url>https://some.url</url>\n" +
                            "            <releases>\n" +
                            "                <enabled>true</enabled>\n" +
                            "                <checksumPolicy>some-policy</checksumPolicy>\n" +
                            "                <updatePolicy>some-update-policy</updatePolicy>\n" +
                            "            </releases>\n" +
                            "        </repository>\n" +
                            "    </repositories>\n" +
                            "</project>"
            );
        }

        @Test
        void shouldAddANewRepositoriesSectionWithSnapshotsInformation() {

            AddRepositoryAction sut = new AddRepositoryAction();
            sut.setId("repo-id");
            sut.setUrl("https://some.url");
            sut.setName("repo-name");
            sut.setLayout("some-layout");
            sut.setSnapshotsEnabled(true);
            sut.setSnapshotsChecksumPolicy("some-policy");
            sut.setSnapShotsUpdatePolicy("some-update-policy");
            sut.apply(context);

            assertThat(context.getBuildFile().print()).isEqualTo(
                    "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                            "        xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">\n" +
                            "    <modelVersion>4.0.0</modelVersion>\n" +
                            "    <groupId>some.group.id</groupId>\n" +
                            "    <artifactId>with-artifact</artifactId>\n" +
                            "    <packaging>jar</packaging>\n" +
                            "    <version>100.23.01-SNAPSHOT</version>\n" +
                            "    <repositories>\n" +
                            "        <repository>\n" +
                            "            <id>repo-id</id>\n" +
                            "            <name>repo-name</name>\n" +
                            "            <url>https://some.url</url>\n" +
                            "            <snapshots>\n" +
                            "                <enabled>true</enabled>\n" +
                            "                <checksumPolicy>some-policy</checksumPolicy>\n" +
                            "                <updatePolicy>some-update-policy</updatePolicy>\n" +
                            "            </snapshots>\n" +
                            "        </repository>\n" +
                            "    </repositories>\n" +
                            "</project>"
            );
        }

    }

    @Nested
    @DisplayName("With existing Repositories section")
    public static class ToPomWithExistingRepositoriesSection {
        String pomXml =
                "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>some.group.id</groupId>\n" +
                        "    <artifactId>with-artifact</artifactId>\n" +
                        "    <packaging>jar</packaging>\n" +
                        "    <version>100.23.01-SNAPSHOT</version>\n" +
                        "    <repositories>\n" +
                        "        <repository>\n" +
                        "            <id>rewrite-snapshots</id>\n" +
                        "            <name>rewrite-snapshots</name>\n" +
                        "            <url>https://oss.sonatype.org/content/repositories/snapshots/</url>\n" +
                        "        </repository>\n" +
                        "    </repositories>\n" +
                        "</project>";
        private ProjectContext context;

        @BeforeEach
        void beforeEach() {
            context = TestProjectContext.buildProjectContext()
                    .withMavenRootBuildFileSource(pomXml)
                    .build();
        }

        @Test
        void shouldAppendRepositotyIfNotExist() {

            AddRepositoryAction sut = new AddRepositoryAction();
            sut.setId("repo-id");
            sut.setUrl("https://some.url");
            sut.setName("repo-name");
            sut.setLayout("some-layout");
            sut.setSnapshotsEnabled(true);
            sut.setSnapshotsChecksumPolicy("some-policy");
            sut.setSnapShotsUpdatePolicy("some-update-policy");
            sut.apply(context);
            System.out.println(context.getBuildFile().print());
            assertThat(context.getBuildFile().print()).isEqualTo("<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                    "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">\n" +
                    "    <modelVersion>4.0.0</modelVersion>\n" +
                    "    <groupId>some.group.id</groupId>\n" +
                    "    <artifactId>with-artifact</artifactId>\n" +
                    "    <packaging>jar</packaging>\n" +
                    "    <version>100.23.01-SNAPSHOT</version>\n" +
                    "    <repositories>\n" +
                    "        <repository>\n" +
                    "            <id>rewrite-snapshots</id>\n" +
                    "            <name>rewrite-snapshots</name>\n" +
                    "            <url>https://oss.sonatype.org/content/repositories/snapshots/</url>\n" +
                    "        </repository>\n" +
                    "        <repository>\n" +
                    "            <id>repo-id</id>\n" +
                    "            <name>repo-name</name>\n" +
                    "            <url>https://some.url</url>\n" +
                    "            <snapshots>\n" +
                    "                <enabled>true</enabled>\n" +
                    "                <checksumPolicy>some-policy</checksumPolicy>\n" +
                    "                <updatePolicy>some-update-policy</updatePolicy>\n" +
                    "            </snapshots>\n" +
                    "        </repository>\n" +
                    "    </repositories>\n" +
                    "</project>");
        }

        @Test
        void shouldIgnoreRepositotyIfSameIdExists() {

            AddRepositoryAction sut = new AddRepositoryAction();
            sut.setId("rewrite-snapshots");
            sut.setUrl("https://some.url");
            sut.apply(context);
            assertThat(context.getBuildFile().print()).isEqualTo("<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                    "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">\n" +
                    "    <modelVersion>4.0.0</modelVersion>\n" +
                    "    <groupId>some.group.id</groupId>\n" +
                    "    <artifactId>with-artifact</artifactId>\n" +
                    "    <packaging>jar</packaging>\n" +
                    "    <version>100.23.01-SNAPSHOT</version>\n" +
                    "    <repositories>\n" +
                    "        <repository>\n" +
                    "            <id>rewrite-snapshots</id>\n" +
                    "            <name>rewrite-snapshots</name>\n" +
                    "            <url>https://oss.sonatype.org/content/repositories/snapshots/</url>\n" +
                    "        </repository>\n" +
                    "    </repositories>\n" +
                    "</project>");
        }

        @Test
        void shouldThrowExceptionForMissingURLMandatoryAttributes() {
            AddRepositoryAction sut = new AddRepositoryAction();
            sut.setId("myId");

            NullPointerException thrown = assertThrows(NullPointerException.class, () -> sut.apply(context));

            assertThat(thrown.getMessage()).isEqualTo("url is marked non-null but is null");
        }

        @Test
        void shouldThrowExceptionForMissingIdMandatoryAttributes() {
            AddRepositoryAction sut = new AddRepositoryAction();
            sut.setUrl("www.google.com");

            NullPointerException thrown = assertThrows(NullPointerException.class, () -> sut.apply(context));

            assertThat(thrown.getMessage()).isEqualTo("id is marked non-null but is null");
        }

    }
}
