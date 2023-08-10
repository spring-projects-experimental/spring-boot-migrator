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
package org.springframework.sbm.project.buildfile;

import org.jetbrains.annotations.NotNull;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.openrewrite.ExecutionContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.sbm.GitHubIssue;
import org.springframework.sbm.build.api.BuildFile;
import org.springframework.sbm.build.api.DependenciesChangedEvent;
import org.springframework.sbm.build.api.Dependency;
import org.springframework.sbm.build.api.Plugin;
import org.springframework.sbm.build.util.PomBuilder;
import org.springframework.sbm.build.impl.OpenRewriteMavenPlugin;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.context.ProjectContextHolder;
import org.springframework.sbm.java.api.Member;
import org.springframework.sbm.java.impl.DependenciesChangedEventHandler;
import org.springframework.sbm.java.impl.RewriteJavaParser;
import org.springframework.sbm.openrewrite.RewriteExecutionContext;
import org.springframework.sbm.parsers.JavaParserBuilder;
import org.springframework.sbm.project.resource.SbmApplicationProperties;
import org.springframework.sbm.project.resource.TestProjectContext;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class OpenRewriteMavenBuildFileTest {

    /*
     * Test that the coordinate for a declared dependency with version set as property contains the resolved value of the
     * version property.
     */
    @Test
    void coordinatesForRequestedDependencies_withVersionProperty_shouldHaveResolvedVersionNumber() {

        @Language("xml")
        String applicationPom = """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0"
                         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <groupId>org.example</groupId>
                    <artifactId>some-module</artifactId>
                    <version>1.0-SNAPSHOT</version>
                    <properties>
                        <maven.compiler.source>17</maven.compiler.source>
                        <maven.compiler.target>17</maven.compiler.target>
                        <validation-api.version>2.0.1.Final</validation-api.version>
                    </properties>
                    <dependencies>
                        <dependency>
                            <groupId>javax.validation</groupId>
                            <artifactId>validation-api</artifactId>
                            <version>${validation-api.version}</version>
                        </dependency>
                    </dependencies>
                </project>
                """;

        BuildFile buildFile = TestProjectContext
                .buildProjectContext()
                .withMavenRootBuildFileSource(applicationPom)
                .build()
                .getApplicationModules()
                .list()
                .get(0)
                .getBuildFile();

        assertThat(buildFile.getRequestedDependencies().get(0).getCoordinates()).isEqualTo("javax.validation:validation-api:2.0.1.Final");
    }

    @Nested
    class HandlingDuplicatedDependencyTest {

        @Test
        void shouldNotAddDependencyWhenAlreadyExists() {
            @Language("xml")
            String applicationPom = """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0"
                         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <groupId>org.example</groupId>
                    <artifactId>some-module</artifactId>
                    <version>1.0-SNAPSHOT</version>
                    <properties>
                        <maven.compiler.source>17</maven.compiler.source>
                        <maven.compiler.target>17</maven.compiler.target>
                    </properties>
                    <dependencies>
                        <dependency>
                            <groupId>javax.validation</groupId>
                            <artifactId>validation-api</artifactId>
                            <version>2.0.1.Final</version>
                        </dependency>
                    </dependencies>
                </project>
                """;

            BuildFile buildFile = TestProjectContext
                    .buildProjectContext()
                    .withMavenRootBuildFileSource(applicationPom)
                    .build()
                    .getApplicationModules()
                    .list()
                    .get(0)
                    .getBuildFile();

            buildFile.addDependency(Dependency.fromCoordinates("javax.validation:validation-api:2.0.1.Final"));
            buildFile.addDependency(Dependency.fromCoordinates("javax.validation:validation-api:2.0.1.Final"));
            assertThat(buildFile.getDeclaredDependencies()).hasSize(1);
        }

        @Test
        void shouldDuplicateDependencyWithDifferentScope() {
            @Language("xml")
            String applicationPom = """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0"
                         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <groupId>org.example</groupId>
                    <artifactId>some-module</artifactId>
                    <version>1.0-SNAPSHOT</version>
                    <properties>
                        <maven.compiler.source>17</maven.compiler.source>
                        <maven.compiler.target>17</maven.compiler.target>
                    </properties>
                    <dependencies>
                        <dependency>
                            <groupId>javax.validation</groupId>
                            <artifactId>validation-api</artifactId>
                            <version>2.0.1.Final</version>
                            <scope>test</scope>
                        </dependency>
                    </dependencies>
                </project>
                """;

            BuildFile buildFile = TestProjectContext
                    .buildProjectContext()
                    .withMavenRootBuildFileSource(applicationPom)
                    .build()
                    .getApplicationModules()
                    .list()
                    .get(0)
                    .getBuildFile();

            buildFile.addDependency(Dependency.fromCoordinates("javax.validation:validation-api:2.0.1.Final"));
            assertThat(buildFile.getDeclaredDependencies()).hasSize(2);
        }

        @Test
        void shouldNotDuplicateDependencyInManagedDependencySetting() {

            @Language("xml")
            String applicationPom = """
<?xml version="1.0" encoding="UTF-8"?>
    <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>2.7.4</version>
		<relativePath/> <!-- lookup parent from repository -->
	</parent>
	<groupId>com.example</groupId>
	<artifactId>demo</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<name>demo</name>
	<description>Demo project for Spring Boot</description>
	<properties>
		<java.version>17</java.version>
	</properties>
	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter</artifactId>
		</dependency>
		<dependency>
  			<groupId>org.springframework.boot</groupId>
  			<artifactId>spring-boot-starter-test</artifactId>
  			<scope>test</scope>
  		</dependency>
	</dependencies>

</project>
                """;

            BuildFile buildFile = TestProjectContext
                    .buildProjectContext()
                    .withMavenRootBuildFileSource(applicationPom)
                    .build()
                    .getApplicationModules()
                    .list()
                    .get(0)
                    .getBuildFile();

            buildFile.addDependency(Dependency.fromCoordinates("org.springframework.boot:spring-boot-starter:2.7.4"));
            Dependency springBootStarterTest = Dependency
                    .fromCoordinates("org.springframework.boot:spring-boot-starter-test:2.7.4");
            springBootStarterTest.setScope("test");
            buildFile.addDependency(springBootStarterTest);

            assertThat(buildFile.getDeclaredDependencies()).hasSize(2);
        }
    }

    @Nested
    class ResolvingMavenVariableTest {

        @Test
        void itResolvesVariables() {
            @Language("xml")
            String pom = """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <groupId>javax.validation</groupId>
                    <artifactId>demo</artifactId>
                    <version>1.0.0</version>
                    <properties>
                        <javaValidationApiVersion>2.0.1.Final</javaValidationApiVersion>
                    </properties>
                    <name>demo</name>
                    <dependencies>
                        <dependency>
                            <groupId>${project.groupId}</groupId>
                            <artifactId>validation-api</artifactId>
                            <version>${javaValidationApiVersion}</version>
                        </dependency>
                    </dependencies>
                </project>
                """;

            BuildFile buildFile = TestProjectContext
                    .buildProjectContext()
                    .withMavenRootBuildFileSource(pom)
                    .build()
                    .getApplicationModules()
                    .list()
                    .get(0)
                    .getBuildFile();

            assertThat(buildFile.getRequestedDependencies()).hasSize(1);
            Dependency javaValidationApiDependency = buildFile.getRequestedDependencies().get(0);
            assertThat(javaValidationApiDependency.getGroupId()).isEqualTo("javax.validation");
            assertThat(javaValidationApiDependency.getVersion()).isEqualTo("2.0.1.Final");
        }

        @Test
        void itResolvesVariableFromMavenConfig() {
            @Language("xml")
            String pom = """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <groupId>javax.validation</groupId>
                    <artifactId>demo</artifactId>
                    <version>${revision}</version>
                    <properties>
                        <javaValidationApiVersion>2.0.1.Final</javaValidationApiVersion>
                    </properties>
                    <name>demo</name>
                    <dependencies>
                        <dependency>
                            <groupId>${project.groupId}</groupId>
                            <artifactId>validation-api</artifactId>
                            <version>${javaValidationApiVersion}</version>
                        </dependency>
                    </dependencies>
                </project>
                """;

            BuildFile buildFile = TestProjectContext
                    .buildProjectContext()
                    .withProjectResource(".mvn/maven.config", """
                            -Drevision=1.0.0
                            """)
                    .withMavenRootBuildFileSource(pom)
                    .build()
                    .getApplicationModules()
                    .list()
                    .get(0)
                    .getBuildFile();

            assertThat(buildFile.getVersion()).isEqualTo("1.0.0");
        }
    }

    @Test
    @Tag("integration")
    void testGetResolvedDependenciesPaths() {

        String pomXml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\"\n" +
                        "    xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                        "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                        "  <modelVersion>4.0.0</modelVersion>\n" +
                        "  <groupId>org.springframework.sbm</groupId>\n" +
                        "  <artifactId>dummy-test-artifact</artifactId>\n" +
                        "  <version>1.0.0</version>\n" +
                        "  <dependencies> \n" +
                        "    <dependency>\n" +
                        "      <groupId>org.springframework</groupId>\n" +
                        "        <artifactId>spring-context</artifactId>\n" +
                        "        <type>jar</type>\n" +
                        "        <version>4.1.7.RELEASE</version>\n" +
                        "      </dependency>\n" +
                        "    <dependency>\n" +
                        "      <groupId>javax.transaction</groupId>\n" +
                        "      <artifactId>javax.transaction-api</artifactId>\n" +
                        "      <scope>runtime</scope>\n" +
                        "    </dependency>\n" +
                        "  </dependencies> \n" +
                        "  <dependencyManagement>\n" +
                        "    <dependencies>\n" +
                        "      <dependency>\n" +
                        "      <groupId>javax.transaction</groupId>\n" +
                        "      <artifactId>javax.transaction-api</artifactId>\n" +
                        "      <version>1.2</version>\n" +
                        "      </dependency>\n" +
                        "    </dependencies>\n" +
                        "  </dependencyManagement>\n" +
                        "</project>\n";

        BuildFile sut = TestProjectContext.buildProjectContext()
                .withMavenRootBuildFileSource(pomXml)
                .build()
                .getBuildFile();

        List<Path> dependenciesPaths = sut.getResolvedDependenciesPaths();

        assertThat(dependenciesPaths).isNotNull();
        assertThat(dependenciesPaths).hasSize(8);
        assertThat(dependenciesPaths.get(0).toFile().exists()).isTrue();
    }

    /*
    * Resolves pom type dependency and verifies the paths of all retrieved dependencies.
    * Currently, the behaviour is related to caching configuration in DependencyHelper.
    * All dependencies that do not exist in ~/.m2/repository get downloaded to ~/.rewrite/cache/artifacts.
    */
    @Test
    @Tag("integration")
    @Disabled("Disabled after upgrade to 7.25.0 because org/jboss/logging/jboss-logging/3.3.2.Final/jboss-logging-3.3.2.Final.jar is sometimes retreived and sometimes iot isn't")
    void testResolvedDependenciesWithPomTypeDependency() {
        String pomXml =
                "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                        "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "    xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>foo</groupId>\n" +
                        "    <artifactId>bar</artifactId>\n" +
                        "    <version>0.0.1-SNAPSHOT</version>\n" +
                        "    <name>foobat</name>\n" +
                        "    <repositories>\n" +
                        "        <repository>\n" +
                        "            <id>jcenter</id>\n" +
                        "            <name>jcenter</name>\n" +
                        "            <url>https://jcenter.bintray.com</url>\n" +
                        "        </repository>\n" +
                        "        <repository>\n" +
                        "            <id>mavencentral</id>\n" +
                        "            <name>mavencentral</name>\n" +
                        "            <url>https://repo.maven.apache.org/maven2</url>\n" +
                        "        </repository>\n" +
                        "    </repositories>" +
                        "    <dependencies>\n" +
                        "        <dependency>\n" +
                        "            <groupId>org.apache.tomee</groupId>\n" +
                        "            <artifactId>openejb-core-hibernate</artifactId>\n" +
                        "            <version>8.0.5</version>\n" +
                        "            <type>pom</type>\n" +
                        "        </dependency>\n" +
                        "    </dependencies>\n" +
                        "</project>";

        BuildFile sut = TestProjectContext.buildProjectContext()
                .withMavenRootBuildFileSource(pomXml)
                .build()
                .getBuildFile();

        List<String> unifiedPaths = sut.getResolvedDependenciesPaths().stream()
                .filter(Objects::nonNull)
                .map(dp -> {
                    String dep = dp.toString();
                    return dep.substring(dep.lastIndexOf("repository/") + "repository/".length());
                })
                .collect(Collectors.toList());


        assertThat(unifiedPaths.stream().sorted()).containsExactlyInAnyOrder(
                "org/apache/tomee/mbean-annotation-api/8.0.5/mbean-annotation-api-8.0.5.jar",
				"org/apache/tomee/openejb-jpa-integration/8.0.5/openejb-jpa-integration-8.0.5.jar",
				"org/apache/tomee/javaee-api/8.0-5/javaee-api-8.0-5.jar",
				"org/apache/commons/commons-lang3/3.11/commons-lang3-3.11.jar",
				"org/apache/tomee/openejb-api/8.0.5/openejb-api-8.0.5.jar",
				"org/apache/tomee/openejb-loader/8.0.5/openejb-loader-8.0.5.jar",
				"org/apache/tomee/openejb-javaagent/8.0.5/openejb-javaagent-8.0.5.jar",
				"org/apache/tomee/openejb-jee/8.0.5/openejb-jee-8.0.5.jar",
                "org/apache/tomee/openejb-core/8.0.5/openejb-core-8.0.5.jar",
				"jakarta/xml/bind/jakarta.xml.bind-api/2.3.2/jakarta.xml.bind-api-2.3.2.jar",
				"jakarta/activation/jakarta.activation-api/1.2.1/jakarta.activation-api-1.2.1.jar",
				"org/apache/tomee/openejb-jee-accessors/8.0.5/openejb-jee-accessors-8.0.5.jar",
				"org/metatype/sxc/sxc-jaxb-core/0.8/sxc-jaxb-core-0.8.jar",
				"org/metatype/sxc/sxc-runtime/0.8/sxc-runtime-0.8.jar",
				"commons-cli/commons-cli/1.4/commons-cli-1.4.jar",
				"commons-collections/commons-collections/3.2.2/commons-collections-3.2.2.jar",
				"com/sun/activation/jakarta.activation/1.2.1/jakarta.activation-1.2.1.jar",
				"org/apache/activemq/activemq-ra/5.16.0/activemq-ra-5.16.0.jar",
				"org/apache/activemq/activemq-kahadb-store/5.16.0/activemq-kahadb-store-5.16.0.jar",
				"org/apache/activemq/protobuf/activemq-protobuf/1.1/activemq-protobuf-1.1.jar",
				"org/apache/activemq/activemq-broker/5.16.0/activemq-broker-5.16.0.jar",
				"org/apache/activemq/activemq-client/5.16.0/activemq-client-5.16.0.jar",
				"org/fusesource/hawtbuf/hawtbuf/1.11/hawtbuf-1.11.jar",
				"org/apache/activemq/activemq-openwire-legacy/5.16.0/activemq-openwire-legacy-5.16.0.jar",
				"org/apache/activemq/activemq-jdbc-store/5.16.0/activemq-jdbc-store-5.16.0.jar",
				"org/apache/geronimo/components/geronimo-connector/3.1.4/geronimo-connector-3.1.4.jar",
				"org/apache/geronimo/specs/geronimo-j2ee-connector_1.6_spec/1.0/geronimo-j2ee-connector_1.6_spec-1.0.jar",
				"org/apache/geronimo/components/geronimo-transaction/3.1.4/geronimo-transaction-3.1.4.jar",
				"org/objectweb/howl/howl/1.0.1-1/howl-1.0.1-1.jar",
				"com/fasterxml/jackson/core/jackson-databind/2.12.0-rc1/jackson-databind-2.12.0-rc1.jar",
				"com/fasterxml/jackson/core/jackson-annotations/2.12.0-rc1/jackson-annotations-2.12.0-rc1.jar",
				"com/fasterxml/jackson/core/jackson-core/2.12.0-rc1/jackson-core-2.12.0-rc1.jar",
				"org/apache/geronimo/javamail/geronimo-javamail_1.6_mail/1.0.0/geronimo-javamail_1.6_mail-1.0.0.jar",
				"org/apache/xbean/xbean-asm7-shaded/4.14/xbean-asm7-shaded-4.14.jar",
				"org/apache/xbean/xbean-finder-shaded/4.14/xbean-finder-shaded-4.14.jar",

                "commons-net/commons-net/3.6/commons-net-3.6.jar",
                 "org/apache/geronimo/specs/geronimo-jms_1.1_spec/1.1.1/geronimo-jms_1.1_spec-1.1.1.jar",
                 "org/apache/geronimo/specs/geronimo-j2ee-management_1.1_spec/1.0.1/geronimo-j2ee-management_1.1_spec-1.0.1.jar",
                 "stax/stax-api/1.0.1/stax-api-1.0.1.jar",

				"org/apache/xbean/xbean-reflect/4.14/xbean-reflect-4.14.jar",
				"org/apache/xbean/xbean-naming/4.14/xbean-naming-4.14.jar",
				"org/apache/xbean/xbean-bundleutils/4.14/xbean-bundleutils-4.14.jar",
				"org/hsqldb/hsqldb/2.3.2/hsqldb-2.3.2.jar",
				"org/apache/commons/commons-dbcp2/2.1/commons-dbcp2-2.1.jar",
				"org/apache/commons/commons-pool2/2.3/commons-pool2-2.3.jar",
				"org/codehaus/swizzle/swizzle-stream/1.6.2/swizzle-stream-1.6.2.jar",
				"commons-logging/commons-logging/1.2/commons-logging-1.2.jar",
				"org/apache/openejb/shade/quartz-openejb-shade/2.2.1/quartz-openejb-shade-2.2.1.jar",
				"org/slf4j/slf4j-api/1.7.21/slf4j-api-1.7.21.jar",
				"org/apache/openwebbeans/openwebbeans-impl/2.0.12/openwebbeans-impl-2.0.12.jar",
				"org/apache/openwebbeans/openwebbeans-spi/2.0.12/openwebbeans-spi-2.0.12.jar",
				"org/apache/openwebbeans/openwebbeans-ejb/2.0.12/openwebbeans-ejb-2.0.12.jar",
				"org/apache/openwebbeans/openwebbeans-ee/2.0.12/openwebbeans-ee-2.0.12.jar",
				"org/apache/openwebbeans/openwebbeans-ee-common/2.0.12/openwebbeans-ee-common-2.0.12.jar",
				"org/apache/openwebbeans/openwebbeans-web/2.0.12/openwebbeans-web-2.0.12.jar",
				"org/apache/openwebbeans/openwebbeans-el22/2.0.12/openwebbeans-el22-2.0.12.jar",
				"org/hibernate/hibernate-entitymanager/5.4.10.Final/hibernate-entitymanager-5.4.10.Final.jar",
				"org/hibernate/hibernate-core/5.4.10.Final/hibernate-core-5.4.10.Final.jar",
				"org/javassist/javassist/3.24.0-GA/javassist-3.24.0-GA.jar",
                "antlr/antlr/2.7.7/antlr-2.7.7.jar",
				"org/jboss/jandex/2.1.1.Final/jandex-2.1.1.Final.jar",
				"javax/activation/javax.activation-api/1.2.0/javax.activation-api-1.2.0.jar",
				"javax/xml/bind/jaxb-api/2.3.1/jaxb-api-2.3.1.jar",
				"org/glassfish/jaxb/jaxb-runtime/2.3.1/jaxb-runtime-2.3.1.jar",
				"org/glassfish/jaxb/txw2/2.3.1/txw2-2.3.1.jar",
				"com/sun/istack/istack-commons-runtime/3.0.7/istack-commons-runtime-3.0.7.jar",
				"org/jvnet/staxex/stax-ex/1.8/stax-ex-1.8.jar",
				"com/sun/xml/fastinfoset/FastInfoset/1.2.15/FastInfoset-1.2.15.jar",
				"org/dom4j/dom4j/2.1.1/dom4j-2.1.1.jar",
				"org/hibernate/common/hibernate-commons-annotations/5.1.0.Final/hibernate-commons-annotations-5.1.0.Final.jar",
				"javax/persistence/javax.persistence-api/2.2/javax.persistence-api-2.2.jar",
				"net/bytebuddy/byte-buddy/1.10.2/byte-buddy-1.10.2.jar",
				"org/jboss/spec/javax/transaction/jboss-transaction-api_1.2_spec/1.1.1.Final/jboss-transaction-api_1.2_spec-1.1.1.Final.jar",
				"org/hibernate/hibernate-validator/5.1.3.Final/hibernate-validator-5.1.3.Final.jar",
				"com/fasterxml/classmate/1.0.0/classmate-1.0.0.jar",
				"org/hibernate/hibernate-ehcache/5.4.10.Final/hibernate-ehcache-5.4.10.Final.jar",
				"net/sf/ehcache/ehcache/2.10.3/ehcache-2.10.3.jar",
				"org/slf4j/slf4j-jdk14/1.7.21/slf4j-jdk14-1.7.21.jar"
        );
    }

    @Test
    @Disabled("FIXME: 786 Event listener")
    void addDependencyShouldPublishEvent() {
        ApplicationEventPublisher eventPublisher = mock(ApplicationEventPublisher.class);

        ExecutionContext executionContext = new RewriteExecutionContext();
        ProjectContext context = TestProjectContext.buildProjectContext(eventPublisher)
                .withExecutionContext(executionContext)
                .withMavenRootBuildFileSource(
                        """
                        <?xml version="1.0" encoding="UTF-8"?>
                        <project xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd"
                            xmlns="http://maven.apache.org/POM/4.0.0"
                            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
                          <modelVersion>4.0.0</modelVersion>
                          <groupId>org.springframework.sbm</groupId>
                          <artifactId>dummy-test-artifact</artifactId>
                          <version>1.0.0</version>
                        </project>
                        """
                )
                .withJavaSource("src/main/java",
                       """
                        import jakarta.validation.constraints.Email;
                        public class Cat {
                            @Email
                            private String email;
                        }
                        """
                )
                .build();

        BuildFile buildFile = context.getBuildFile();

        Member member = context.getProjectJavaSources().list().get(0).getTypes().get(0).getMembers().get(0);

        // The Email annotation cannot be resolved
        boolean b = member.hasAnnotation("javax.validation.constraints.Email");
        assertThat(b).isFalse();

        // adding the validation-api brings the Email annotation
        buildFile.addDependency(Dependency.builder()
                                .groupId("javax.validation")
                                .artifactId("validation-api")
                                .version("2.0.1.Final")
                                .build());


        Class<DependenciesChangedEvent> event = DependenciesChangedEvent.class;
        ArgumentCaptor<DependenciesChangedEvent> argumentCaptor = ArgumentCaptor.forClass(event);
        assertEventPublished(eventPublisher, argumentCaptor, event, 1);

        DependenciesChangedEvent fireEvent = argumentCaptor.getValue();
        assertThat(fireEvent.getResolvedDependencies().get(0).toString()).endsWith("javax/validation/validation-api/2.0.1.Final/validation-api-2.0.1.Final.jar");

        // call DependenciesChangedEventHandler to trigger recompile
        JavaParserBuilder rewriteJavaParser = new JavaParserBuilder();
        ProjectContextHolder projectContextHolder = new ProjectContextHolder();
        projectContextHolder.setProjectContext(context);
        DependenciesChangedEventHandler handler = new DependenciesChangedEventHandler(projectContextHolder, rewriteJavaParser, executionContext);
        handler.onDependenciesChanged(fireEvent);

        Member member2 = context.getProjectJavaSources().list().get(0).getTypes().get(0).getMembers().get(0);
        boolean b1 = member2.hasAnnotation("javax.validation.constraints.Email");
        assertThat(b1).isTrue();

    }

    @Test
    void testHasDependency() {
        String pomXml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\"\n" +
                        "    xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                        "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                        "  <modelVersion>4.0.0</modelVersion>\n" +
                        "  <groupId>org.springframework.sbm</groupId>\n" +
                        "  <artifactId>dummy-test-artifact</artifactId>\n" +
                        "  <version>1.0.0</version>\n" +
                        "  <dependencies> \n" +
                        "    <dependency>\n" +
                        "      <groupId>javax.transaction</groupId>\n" +
                        "      <artifactId>javax.transaction-api</artifactId>\n" +
                        "      <version>1.2</version>\n" +
                        "      <scope>runtime</scope>\n" +
                        "    </dependency>\n" +
                        "  </dependencies> \n" +
                        "</project>\n";

        BuildFile sut = TestProjectContext.buildProjectContext()
                .withMavenRootBuildFileSource(pomXml)
                .build()
                .getBuildFile();

        assertThat(sut.hasDeclaredDependencyMatchingRegex("javax\\.transaction.*")).isTrue();
        assertThat(sut.hasDeclaredDependencyMatchingRegex("javax\\..*")).isTrue();
        assertThat(sut.hasDeclaredDependencyMatchingRegex("javax\\..*", "javax\\.transaction.*")).isTrue();
        assertThat(sut.hasDeclaredDependencyMatchingRegex("javax\\.transaction\\.api.*")).isFalse();
        assertThat(sut.hasDeclaredDependencyMatchingRegex("javax\\.inject")).isFalse();
        assertThat(sut.hasDeclaredDependencyMatchingRegex(
                "javax\\.*",
                "javax\\.transaction.*",
                "javax\\.inject")
        ).isTrue();
    }

    @Test
    void testAddDependency() {

        String pomXml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\"\n" +
                        "    xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                        "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                        "  <modelVersion>4.0.0</modelVersion>\n" +
                        "  <groupId>org.springframework.sbm</groupId>\n" +
                        "  <artifactId>dummy-test-artifact</artifactId>\n" +
                        "  <version>1.0.0</version>\n" +
                        "</project>\n";

        BuildFile sut = TestProjectContext.buildProjectContext()
                .withMavenRootBuildFileSource(pomXml)
                .build()
                .getBuildFile();

        sut.addDependency(Dependency.builder()
                .groupId("javax.transaction")
                .artifactId("javax.transaction-api")
                .version("1.2") // FIXME: using 1.2.1 results in 1.2-b03 ?! Is this the dependency in the pm.xml or the resolved from bom
                .build());

        assertThat(sut.getEffectiveDependencyManagement()).hasSize(0);
        assertThat(sut.getDeclaredDependencies()).hasSize(1);

        Dependency addedDependency = sut.getDeclaredDependencies().get(0);
        assertThat(addedDependency.getGroupId()).isEqualTo("javax.transaction");
        assertThat(addedDependency.getArtifactId()).isEqualTo("javax.transaction-api");
        assertThat(addedDependency.getVersion()).isEqualTo("1.2");
    }


    // TODO: add test with transitive dependencies
    // TODO: add integration test verifying type resolution and classpath of JavaParser
    // TODO: merge with AddDependencyTest
    // TODO: add dependency with version managed in dependencyManagement
    @Test
    @Disabled
    void addDependency() {
        String pomXml =
                "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>org.springframework.sbm.examples</groupId>\n" +
                        "    <artifactId>example-app</artifactId>\n" +
                        "    <packaging>jar</packaging>\n" +
                        "    <version>1.0.0-SNAPSHOT</version>\n" +
                        "</project>";

        ApplicationEventPublisher eventPublisher = mock(ApplicationEventPublisher.class);
        BuildFile sut = TestProjectContext.buildProjectContext(eventPublisher)
                .withMavenRootBuildFileSource(pomXml)
                .build()
                .getBuildFile();

        Dependency dependency = Dependency.builder()
                .groupId("org.apiguardian")
                .artifactId("apiguardian-api")
                .version("1.1.0")
                .build();

        assertThat(sut.getDeclaredDependencies()).doesNotContain(dependency);

        sut.addDependency(dependency);

        assertThat(sut.getEffectiveDependencyManagement()).hasSize(0);
        assertThat(sut.getDeclaredDependencies()).hasSize(1);
        assertThat(sut.getDeclaredDependencies()).contains(dependency);
        ArgumentCaptor<DependenciesChangedEvent> argumentCaptor = ArgumentCaptor.forClass(DependenciesChangedEvent.class);

        // verify that DependenciesChangedEvent was published exactly once. ArgumentCaptor not type aware.
        // Don't know about a better way, see https://github.com/mockito/mockito/issues/565
        assertEventPublished(eventPublisher, argumentCaptor, DependenciesChangedEvent.class, 1);

        assertThat(argumentCaptor.getValue().getResolvedDependencies()).hasSize(1);
        assertThat(argumentCaptor.getValue().getResolvedDependencies().get(0).toString()).endsWith("org/apiguardian/apiguardian-api/1.1.0/apiguardian-api-1.1.0.jar");
    }

    private void assertEventPublished(ApplicationEventPublisher eventPublisher, ArgumentCaptor<DependenciesChangedEvent> argumentCaptor, Class<?> eventClass, int times) {
        verify(eventPublisher, Mockito.atLeastOnce()).publishEvent(argumentCaptor.capture());
        List<?> allEvents = argumentCaptor.getAllValues();
        long timesDependenciesChangedEventPublished = allEvents
                .stream()
                .filter(e -> eventClass.isInstance(e))
                .count();
        assertThat(timesDependenciesChangedEventPublished).isEqualTo(times);
    }

    @Test
    @Tag("integration")
    void testAddDependencies() {

        String pomXml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\"\n" +
                        "    xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                        "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                        "  <modelVersion>4.0.0</modelVersion>\n" +
                        "  <groupId>org.springframework.sbm</groupId>\n" +
                        "  <artifactId>dummy-test-artifact</artifactId>\n" +
                        "  <version>1.0.0</version>\n" +
                        "</project>\n";

        ApplicationEventPublisher eventPublisher = mock(ApplicationEventPublisher.class);
        BuildFile sut = TestProjectContext.buildProjectContext(eventPublisher)
                .withMavenRootBuildFileSource(pomXml)
                .build()
                .getBuildFile();

        sut.addDependencies(List.of(
                Dependency.builder()
                        .groupId("javax.mail")
                        .artifactId("javax.mail-api")
                        .version("1.6.2")
                        .build(),
                Dependency.builder()
                        .groupId("org.junit.jupiter")
                        .artifactId("junit-jupiter-engine")
                        .version("5.7.0")
                        .scope("test")
                        .exclusions(List.of(Dependency.builder()
                                .groupId("org.junit.jupiter")
                                .artifactId("junit-jupiter-api")
                                .build())
                        )
                        .build())
        );

        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<project xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\"\n"
                        + "    xmlns=\"http://maven.apache.org/POM/4.0.0\"\n"
                        + "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n"
                        + "  <modelVersion>4.0.0</modelVersion>\n"
                        + "  <groupId>org.springframework.sbm</groupId>\n"
                        + "  <artifactId>dummy-test-artifact</artifactId>\n"
                        + "  <version>1.0.0</version>\n"
                        + "  <dependencies>\n"
                        + "    <dependency>\n"
                        + "      <groupId>javax.mail</groupId>\n"
                        + "      <artifactId>javax.mail-api</artifactId>\n"
                        + "      <version>1.6.2</version>\n"
                        + "    </dependency>\n"
                        + "    <dependency>\n"
                        + "      <groupId>org.junit.jupiter</groupId>\n"
                        + "      <artifactId>junit-jupiter-engine</artifactId>\n"
                        + "      <version>5.7.0</version>\n"
                        + "      <scope>test</scope>\n"
                        + "      <exclusions>\n"
                        + "        <exclusion>\n"
                        + "          <groupId>org.junit.jupiter</groupId>\n"
                        + "          <artifactId>junit-jupiter-api</artifactId>\n"
                        + "        </exclusion>\n"
                        + "      </exclusions>\n"
                        + "    </dependency>\n"
                        + "  </dependencies>\n"
                        + "</project>\n",
                sut.print());

        assertThat(sut.getEffectiveDependencyManagement()).hasSize(0);
        assertThat(sut.getDeclaredDependencies()).hasSize(2);

        Dependency addedDependency = sut.getDeclaredDependencies().get(0);
        assertThat(addedDependency.getGroupId()).isEqualTo("javax.mail");
        assertThat(addedDependency.getArtifactId()).isEqualTo("javax.mail-api");
        assertThat(addedDependency.getVersion()).isEqualTo("1.6.2");

        addedDependency = sut.getDeclaredDependencies().get(1);
        assertThat(addedDependency.getGroupId()).isEqualTo("org.junit.jupiter");
        assertThat(addedDependency.getArtifactId()).isEqualTo("junit-jupiter-engine");
        assertThat(addedDependency.getVersion()).isEqualTo("5.7.0");
    }

    @Test
    void testDeleteDependencies() {

        String pomXml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\"\n" +
                        "    xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                        "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                        "  <modelVersion>4.0.0</modelVersion>\n" +
                        "  <groupId>org.springframework.sbm</groupId>\n" +
                        "  <artifactId>dummy-test-artifact</artifactId>\n" +
                        "  <version>1.0.0</version>\n" +
                        "  <dependencies> \n" +
                        "    <dependency>\n" +
                        "      <groupId>org.junit.jupiter</groupId>\n" +
                        "      <artifactId>junit-jupiter-api</artifactId>\n" +
                        "      <version>5.7.0</version>\n" +
                        "      <scope>test</scope>\n" +
                        "    </dependency>\n" +
                        "    <dependency>\n" +
                        "      <groupId>org.junit.jupiter</groupId>\n" +
                        "      <artifactId>junit-jupiter-engine</artifactId>\n" +
                        "      <version>5.7.0</version>\n" +
                        "      <scope>test</scope>\n" +
                        "    </dependency>\n" +
                        "  </dependencies> \n" +
                        "</project>\n";

        BuildFile sut = TestProjectContext.buildProjectContext().withMavenRootBuildFileSource(pomXml).build().getBuildFile();

        sut.removeDependencies(List.of(
                Dependency.builder()
                        .groupId("org.junit.jupiter")
                        .artifactId("junit-jupiter-api")
                        .scope("test")
                        .build(),
                Dependency.builder()
                        .groupId("org.junit.jupiter")
                        .artifactId("junit-jupiter-engine")
                        .scope("test")
                        .build())
        );

        assertThat(sut.getEffectiveDependencyManagement()).hasSize(0);
        assertThat(sut.getDeclaredDependencies()).hasSize(0);
    }

    @Nested
    class GetDependenciesEarMultiModuleTest {
        @Test
        void getRequestedDependencies() {
            ProjectContext context = TestProjectContext.buildFromDir(Path.of("./testcode/jee-ear-project/given"));


            // ear module
            BuildFile ear = getBuildFileByPackagingType(context, "ear");

            List<Dependency> requestedDependenciesInEar = ear.getRequestedDependencies();

            Dependency businessLogic = requestedDependenciesInEar.get(0);
            assertThat(businessLogic.getGroupId()).isEqualTo("com.example");
            assertThat(businessLogic.getArtifactId()).isEqualTo("business-logic");
            assertThat(businessLogic.getScope()).isEqualTo("compile");
            assertThat(businessLogic.getVersion()).isEqualTo("1.0");
            assertThat(businessLogic.getType()).isEqualTo("ejb");

            Dependency webapp = requestedDependenciesInEar.get(1);
            assertThat(webapp.getGroupId()).isEqualTo("com.example");
            assertThat(webapp.getArtifactId()).isEqualTo("webapp");
            assertThat(webapp.getScope()).isEqualTo("compile");
            assertThat(webapp.getVersion()).isEqualTo("1.0");
            assertThat(webapp.getType()).isEqualTo("war");

            // business-logic
            BuildFile ejb = getBuildFileByPackagingType(context, "ejb");

            List<Dependency> requestedDependenciesInEjb = ejb.getRequestedDependencies();

            Dependency business = requestedDependenciesInEjb.get(0);
            assertThat(business.getGroupId()).isEqualTo("org.apache.tomee");
            assertThat(business.getArtifactId()).isEqualTo("javaee-api");
            assertThat(business.getScope()).isEqualTo("provided");
            assertThat(business.getVersion()).isEqualTo("8.0-6");
            assertThat(business.getType()).isEqualTo("jar");

            // webapp
            BuildFile web = getBuildFileByPackagingType(context, "war");

            List<Dependency> requestedDependenciesInWeb = web.getRequestedDependencies();

            Dependency jeeApiInWebapp = requestedDependenciesInWeb.get(0);
            assertThat(jeeApiInWebapp.getGroupId()).isEqualTo("org.apache.tomee");
            assertThat(jeeApiInWebapp.getArtifactId()).isEqualTo("javaee-api");
            assertThat(jeeApiInWebapp.getScope()).isEqualTo("provided");
            assertThat(jeeApiInWebapp.getVersion()).isEqualTo("7.0");
            assertThat(jeeApiInWebapp.getType()).isEqualTo("jar");

            Dependency jstlInWebapp = requestedDependenciesInWeb.get(1);
            assertThat(jstlInWebapp.getGroupId()).isEqualTo("javax.servlet");
            assertThat(jstlInWebapp.getArtifactId()).isEqualTo("jstl");
            assertThat(jstlInWebapp.getScope()).isEqualTo("provided");
            assertThat(jstlInWebapp.getVersion()).isEqualTo("1.2");
            assertThat(jstlInWebapp.getType()).isEqualTo("jar");

            Dependency taglibsInWebapp = requestedDependenciesInWeb.get(2);
            assertThat(taglibsInWebapp.getGroupId()).isEqualTo("taglibs");
            assertThat(taglibsInWebapp.getArtifactId()).isEqualTo("standard");
            assertThat(taglibsInWebapp.getScope()).isEqualTo("provided");
            assertThat(taglibsInWebapp.getVersion()).isEqualTo("1.1.2");
            assertThat(taglibsInWebapp.getType()).isEqualTo("jar");
        }

        // TODO: Add test for getEffectiveDependencies()
        // TODO: Add test for getDeclaredDecpendencies()

        @NotNull
        private BuildFile getBuildFileByPackagingType(ProjectContext context, String ear1) {
            return context.getApplicationModules().stream().map(m -> m.getBuildFile()).filter(b -> {
                return b.getPackaging().equals(ear1);
            }).findFirst().get();
        }
    }

    /**
     * Test get[declared|requested|effective]Dependencies for pom files in multi-module project
     */
    @Nested
    class GetDependenciesMultiModuleTest {
        String parentPom = PomBuilder
                .buildPom("com.example:parent:1.0")
                .withProperties(Map.of(
                    "jakarta.version", "3.0.2",
                    "validation.groupId", "jakarta.validation",
                    "annotationApi.artifactId", "javax.annotation-api"
                    )
                )
                .withModules("module1", "module2")
                .build();

        String module1Pom = PomBuilder
                .buildPom("com.example:parent:1.0", "module1")
                .unscopedDependencies("com.example:module2:${project.version}")
                .testScopeDependencies("javax.annotation:${annotationApi.artifactId}:1.3.2")
                .build();

        String module2Pom = PomBuilder
                .buildPom("com.example:parent:1.0", "module2")
                .unscopedDependencies("${validation.groupId}:jakarta.validation-api:${jakarta.version}")
                .build();

        ProjectContext context = TestProjectContext
                .buildProjectContext()
                .withMavenRootBuildFileSource(parentPom)
                .withMavenBuildFileSource("module1", module1Pom)
                .withMavenBuildFileSource("module2", module2Pom)
                .build();

        BuildFile module1 = context.getApplicationModules().getModule("module1").getBuildFile();
        BuildFile module2 = context.getApplicationModules().getModule("module2").getBuildFile();

        @Test
        @DisplayName("getDeclaredDependencies should return the dependencies as declared in build file")
        void getDeclaredDependencies() {
            // Module 1
            List<Dependency> dependenciesDeclaredInModule1 = module1.getDeclaredDependencies();

            assertThat(dependenciesDeclaredInModule1).hasSize(2);

            Dependency dependency1DeclaredInModule1 = dependenciesDeclaredInModule1.get(0);
            assertThat(dependency1DeclaredInModule1.getGroupId()).isEqualTo("com.example");
            assertThat(dependency1DeclaredInModule1.getArtifactId()).isEqualTo("module2");
            assertThat(dependency1DeclaredInModule1.getVersion()).isEqualTo("${project.version}");
            assertThat(dependency1DeclaredInModule1.getScope()).isNull();
            assertThat(dependency1DeclaredInModule1.getClassifier()).isNull();
            assertThat(dependency1DeclaredInModule1.getExclusions()).isEmpty();

            Dependency dependency2DeclaredInModule1 = dependenciesDeclaredInModule1.get(1);
            assertThat(dependency2DeclaredInModule1.getGroupId()).isEqualTo("javax.annotation");
            assertThat(dependency2DeclaredInModule1.getArtifactId()).isEqualTo("${annotationApi.artifactId}");
            assertThat(dependency2DeclaredInModule1.getVersion()).isEqualTo("1.3.2");
            assertThat(dependency2DeclaredInModule1.getScope()).isEqualTo("test");
            assertThat(dependency2DeclaredInModule1.getClassifier()).isNull();
            assertThat(dependency2DeclaredInModule1.getExclusions()).isEmpty();

            // Module 2
            List<Dependency> dependenciesDeclaredInModule2 = module2.getDeclaredDependencies();

            assertThat(dependenciesDeclaredInModule2).hasSize(1);
            Dependency dependencyDeclaredInModule2 = dependenciesDeclaredInModule2.get(0);

            assertThat(dependencyDeclaredInModule2.getGroupId()).isEqualTo("${validation.groupId}");
            assertThat(dependencyDeclaredInModule2.getArtifactId()).isEqualTo("jakarta.validation-api");
            assertThat(dependencyDeclaredInModule2.getVersion()).isEqualTo("${jakarta.version}");
            assertThat(dependencyDeclaredInModule2.getScope()).isNull();
            assertThat(dependencyDeclaredInModule2.getClassifier()).isNull();
            assertThat(dependencyDeclaredInModule2.getExclusions()).isEmpty();
        }

        @Test
        @DisplayName("getRequestedDependencies should return the declared dependencies with resolved attributes")
        void getRequestedDependencies() {
            // Module 1
            List<Dependency> dependenciesRequestedInModule1 = module1.getRequestedDependencies();

            assertThat(dependenciesRequestedInModule1).hasSize(2);
            Dependency dependency1DeclaredInModule1 = dependenciesRequestedInModule1.get(0);

            assertThat(dependency1DeclaredInModule1.getGroupId()).isEqualTo("com.example");
            assertThat(dependency1DeclaredInModule1.getArtifactId()).isEqualTo("module2");
            assertThat(dependency1DeclaredInModule1.getVersion()).isEqualTo("1.0");
            assertThat(dependency1DeclaredInModule1.getScope()).isEqualTo("compile");
            assertThat(dependency1DeclaredInModule1.getClassifier()).isNull();
            assertThat(dependency1DeclaredInModule1.getExclusions()).isEmpty();

            Dependency dependency2DeclaredInModule1 = dependenciesRequestedInModule1.get(1);
            assertThat(dependency2DeclaredInModule1.getGroupId()).isEqualTo("javax.annotation");
            assertThat(dependency2DeclaredInModule1.getArtifactId()).isEqualTo("javax.annotation-api");
            assertThat(dependency2DeclaredInModule1.getVersion()).isEqualTo("1.3.2");
            assertThat(dependency2DeclaredInModule1.getScope()).isEqualTo("test");
            assertThat(dependency2DeclaredInModule1.getClassifier()).isNull();
            assertThat(dependency2DeclaredInModule1.getExclusions()).isEmpty();

            // Module 2
            List<Dependency> dependenciesRequestedInModule2 = module2.getRequestedDependencies();

            assertThat(dependenciesRequestedInModule2).hasSize(1);
            Dependency dependencyDeclaredInModule2 = dependenciesRequestedInModule2.get(0);

            assertThat(dependencyDeclaredInModule2.getGroupId()).isEqualTo("jakarta.validation");
            assertThat(dependencyDeclaredInModule2.getArtifactId()).isEqualTo("jakarta.validation-api");
            assertThat(dependencyDeclaredInModule2.getVersion()).isEqualTo("3.0.2");
            assertThat(dependencyDeclaredInModule2.getScope()).isEqualTo("compile");
            assertThat(dependencyDeclaredInModule2.getClassifier()).isNull();
            assertThat(dependencyDeclaredInModule2.getExclusions()).isEmpty();
        }

        @Test
        @DisplayName("getRequestedDependencies should return any available dependency (declared or transitive) with given scope")
        void getEffectiveDependencies() {
            // Module 1
            List<Dependency> dependenciesEffectiveInModule1 = new ArrayList(module1.getEffectiveDependencies());


            assertThat(dependenciesEffectiveInModule1).hasSize(3);
            Dependency depToModule2 = findDependencyByCoordinate(dependenciesEffectiveInModule1,"com.example:module2:1.0");

            assertThat(depToModule2.getGroupId()).isEqualTo("com.example");
            assertThat(depToModule2.getArtifactId()).isEqualTo("module2");
            assertThat(depToModule2.getVersion()).isEqualTo("1.0");
            assertThat(depToModule2.getScope()).isEqualTo("compile");
            assertThat(depToModule2.getClassifier()).isNull();
            assertThat(depToModule2.getExclusions()).isEmpty();

            Dependency depToAnnotationApi = findDependencyByCoordinate(dependenciesEffectiveInModule1, "javax.annotation:javax.annotation-api:1.3.2");

            assertThat(depToAnnotationApi.getGroupId()).isEqualTo("javax.annotation");
            assertThat(depToAnnotationApi.getArtifactId()).isEqualTo("javax.annotation-api");
            assertThat(depToAnnotationApi.getVersion()).isEqualTo("1.3.2");
            assertThat(depToAnnotationApi.getScope()).isEqualTo("test");
            assertThat(depToAnnotationApi.getClassifier()).isNull();
            assertThat(depToAnnotationApi.getExclusions()).isEmpty();

            Dependency transDepToValidationApi = findDependencyByCoordinate(dependenciesEffectiveInModule1, "jakarta.validation:jakarta.validation-api:3.0.2");

            assertThat(transDepToValidationApi.getGroupId()).isEqualTo("jakarta.validation");
            assertThat(transDepToValidationApi.getArtifactId()).isEqualTo("jakarta.validation-api");
            assertThat(transDepToValidationApi.getVersion()).isEqualTo("3.0.2");
            assertThat(transDepToValidationApi.getScope()).isEqualTo("compile");
            assertThat(transDepToValidationApi.getClassifier()).isNull();
            assertThat(transDepToValidationApi.getExclusions()).isEmpty();

            // Module 2
            List<Dependency> dependenciesEffectiveInModule2 = new ArrayList<>(module2.getEffectiveDependencies());

            assertThat(dependenciesEffectiveInModule2).hasSize(1);
            Dependency dependencyDeclaredInModule2 = findDependencyByCoordinate(dependenciesEffectiveInModule2, "jakarta.validation:jakarta.validation-api:3.0.2");

            assertThat(dependencyDeclaredInModule2.getGroupId()).isEqualTo("jakarta.validation");
            assertThat(dependencyDeclaredInModule2.getArtifactId()).isEqualTo("jakarta.validation-api");
            assertThat(dependencyDeclaredInModule2.getVersion()).isEqualTo("3.0.2");
            assertThat(dependencyDeclaredInModule2.getScope()).isEqualTo("compile");
            assertThat(dependencyDeclaredInModule2.getClassifier()).isNull();
            assertThat(dependencyDeclaredInModule2.getExclusions()).isEmpty();
        }

        @NotNull
        private Dependency findDependencyByCoordinate(List<Dependency> dependenciesEffectiveInModule1, String anObject) {
            return dependenciesEffectiveInModule1
                    .stream()
                    .filter(d -> d.getCoordinates().equals(anObject))
                    .findFirst()
                    .get();
        }
    }


    @Test
    void getRequestedDependencies() {
        @Language("xml")
        String pomSource = """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <groupId>com.example</groupId>
                    <artifactId>some-example</artifactId>
                    <packaging>jar</packaging>
                    <version>8.0.5-SNAPSHOT</version>
                    <dependencyManagement>
                        <dependencies>
                           <dependency>
                                <groupId>org.junit.jupiter</groupId>
                                <artifactId>junit-jupiter</artifactId>
                                <version>5.7.1</version>
                                <scope>test</scope>
                            </dependency>
                        </dependencies>
                    </dependencyManagement>
                    <dependencies>
                        <dependency>
                            <groupId>org.junit.jupiter</groupId>
                            <artifactId>junit-jupiter</artifactId>
                        </dependency>
                        <dependency>
                            <groupId>org.junit.jupiter</groupId>
                            <artifactId>junit-jupiter-api</artifactId>
                            <version>5.6.3</version>
                            <scope>test</scope>
                        </dependency>
                        <dependency>
                            <groupId>org.mockito</groupId>
                            <artifactId>mockito-core</artifactId>
                            <version>3.7.7</version>
                            <scope>test</scope>
                        </dependency>
                        <dependency>
                            <groupId>org.apache.tomee</groupId>
                            <artifactId>openejb-core-hibernate</artifactId>
                            <version>8.0.5</version>
                            <type>pom</type>
                        </dependency>
                    </dependencies>
                </project>
                """;
        ProjectContext build = TestProjectContext.buildProjectContext()
                .withMavenRootBuildFileSource(pomSource)
                .build();

        List<Dependency> dependencies = build.getBuildFile().getRequestedDependencies();

        assertThat(dependencies).hasSize(4);

        assertThat(dependencies)
                .anyMatch(d -> d.getGroupId().equals("org.junit.jupiter") &&
                        d.getArtifactId().equals("junit-jupiter") &&
                        d.getVersion().equals("5.7.1") &&
                        d.getScope().equals("test"))
                .anyMatch(d -> d.getGroupId().equals("org.junit.jupiter") &&
                        d.getArtifactId().equals("junit-jupiter-api") &&
                        d.getVersion().equals("5.6.3") &&
                        d.getScope().equals("test"))
                .anyMatch(d -> d.getGroupId().equals("org.mockito") &&
                        d.getArtifactId().equals("mockito-core") &&
                        d.getVersion().equals("3.7.7") &&
                        d.getScope().equals("test"))
                .anyMatch(d -> d.getGroupId().equals("org.mockito") &&
                        d.getArtifactId().equals("mockito-core") &&
                        d.getVersion().equals("3.7.7") &&
                        d.getScope().equals("test"))
                .anyMatch(d -> d.getGroupId().equals("org.apache.tomee") &&
                        d.getArtifactId().equals("openejb-core-hibernate") &&
                        d.getVersion().equals("8.0.5") &&
                        d.getType().equals("pom"));
    }

    @Test
    void testGetDeclaredDependencies() {
        @Language("xml")
        String pomSource =
                """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <groupId>com.example</groupId>
                    <artifactId>some-example</artifactId>
                    <packaging>jar</packaging>
                    <version>8.0.5-SNAPSHOT</version>
                    <dependencyManagement>
                        <dependencies>
                           <dependency>
                                <groupId>org.junit.jupiter</groupId>
                                <artifactId>junit-jupiter</artifactId>
                                <version>5.7.1</version>
                                <scope>test</scope>
                            </dependency>
                        </dependencies>
                    </dependencyManagement>
                    <dependencies>
                        <dependency>
                            <groupId>org.junit.jupiter</groupId>
                            <artifactId>junit-jupiter</artifactId>
                        </dependency>
                        <dependency>
                            <groupId>org.junit.jupiter</groupId>
                            <artifactId>junit-jupiter-api</artifactId>
                            <version>5.6.3</version>
                            <scope>test</scope>
                        </dependency>
                        <dependency>
                            <groupId>org.mockito</groupId>
                            <artifactId>mockito-core</artifactId>
                            <version>3.7.7</version>
                            <scope>test</scope>
                        </dependency>
                        <dependency>
                            <groupId>org.apache.tomee</groupId>
                            <artifactId>openejb-core-hibernate</artifactId>
                            <version>8.0.5</version>
                            <type>pom</type>
                        </dependency>
                    </dependencies>
                </project>
                """;
        ProjectContext build = TestProjectContext.buildProjectContext()
                .withMavenRootBuildFileSource(pomSource)
                .build();

        List<Dependency> dependencies = build.getBuildFile().getDeclaredDependencies();

        assertThat(dependencies).hasSize(4);

        assertThat(dependencies)
                .anyMatch(d -> d.getGroupId().equals("org.junit.jupiter") &&
                        d.getArtifactId().equals("junit-jupiter") &&
                        d.getVersion() == null &&
                        d.getScope() == null)
                .anyMatch(d -> d.getGroupId().equals("org.junit.jupiter") &&
                        d.getArtifactId().equals("junit-jupiter-api") &&
                        d.getVersion().equals("5.6.3") &&
                        d.getScope().equals("test"))
                .anyMatch(d -> d.getGroupId().equals("org.mockito") &&
                        d.getArtifactId().equals("mockito-core") &&
                        d.getVersion().equals("3.7.7") &&
                        d.getScope().equals("test"))
                .anyMatch(d -> d.getGroupId().equals("org.mockito") &&
                        d.getArtifactId().equals("mockito-core") &&
                        d.getVersion().equals("3.7.7") &&
                        d.getScope().equals("test"))
                .anyMatch(d -> d.getGroupId().equals("org.apache.tomee") &&
                        d.getArtifactId().equals("openejb-core-hibernate") &&
                        d.getVersion().equals("8.0.5") &&
                        d.getType().equals("pom"));
    }

    @Test
    @Tag("integration")
    void testDeleteTypePomDependencies() {
        String pomXml =
                "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                        "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "    xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>foo</groupId>\n" +
                        "    <artifactId>bar</artifactId>\n" +
                        "    <version>0.0.1-SNAPSHOT</version>\n" +
                        "    <name>foobar</name>\n" +
                        "\n" +
                        "    <dependencies>\n" +
                        "        <dependency>\n" +
                        "           <groupId>org.junit.jupiter</groupId>\n" +
                        "           <artifactId>junit-jupiter-api</artifactId>\n" +
                        "           <version>5.7.0</version>\n" +
                        "        </dependency>\n" +
                        "        <dependency>\n" +
                        "            <groupId>org.apache.tomee</groupId>\n" +
                        "            <artifactId>openejb-core-hibernate</artifactId>\n" +
                        "            <version>8.0.5</version>\n" +
                        "            <type>pom</type>\n" +
                        "        </dependency>\n" +
                        "    </dependencies>\n" +
                        "</project>";

        BuildFile sut = TestProjectContext.buildProjectContext()
                .withMavenRootBuildFileSource(pomXml)
                .build()
                .getBuildFile();
        assertThat(sut.getEffectiveDependencyManagement()).hasSize(0);
        assertThat(sut.getDeclaredDependencies()).hasSize(2);

        sut.removeDependencies(List.of(
                Dependency.builder()
                        .groupId("org.apache.tomee")
                        .artifactId("openejb-core-hibernate")
                        .type("pom")
                        .build())
        );

        assertThat(sut.getEffectiveDependencyManagement()).hasSize(0);
        assertThat(sut.getDeclaredDependencies()).hasSize(1);
        assertThat(sut.print()).isEqualTo("<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "    xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                "    <modelVersion>4.0.0</modelVersion>\n" +
                "    <groupId>foo</groupId>\n" +
                "    <artifactId>bar</artifactId>\n" +
                "    <version>0.0.1-SNAPSHOT</version>\n" +
                "    <name>foobar</name>\n" +
                "\n" +
                "    <dependencies>\n" +
                "        <dependency>\n" +
                "           <groupId>org.junit.jupiter</groupId>\n" +
                "           <artifactId>junit-jupiter-api</artifactId>\n" +
                "           <version>5.7.0</version>\n" +
                "        </dependency>\n" +
                "    </dependencies>\n" +
                "</project>");
    }

    @Test
    void testDeleteTypePomDependenciesAll() {

        String pomXml =
                "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                        "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "    xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>foo</groupId>\n" +
                        "    <artifactId>bar</artifactId>\n" +
                        "    <version>0.0.1-SNAPSHOT</version>\n" +
                        "    <dependencies>\n" +
                        "        <dependency>\n" +
                        "            <groupId>org.apache.tomee</groupId>\n" +
                        "            <artifactId>openejb-core-hibernate</artifactId>\n" +
                        "            <version>8.0.5</version>\n" +
                        "            <type>pom</type>\n" +
                        "        </dependency>\n" +
                        "    </dependencies>\n" +
                        "</project>";

        long beforeCreatingContext = System.currentTimeMillis();

        BuildFile sut = TestProjectContext.buildProjectContext().withMavenRootBuildFileSource(pomXml).build().getBuildFile();

        sut.removeDependencies(List.of(
                Dependency.builder()
                        .groupId("org.apache.tomee")
                        .artifactId("openejb-core-hibernate")
//                        .type("pom") //TODO: #7 OR remove dependency doesn't care about this attribute (ignores it, but it is meaningful here).
                        .build())
        );

        assertThat(sut.getEffectiveDependencyManagement()).hasSize(0);
        assertThat(sut.getDeclaredDependencies()).hasSize(0);

        assertThat(sut.print()).isEqualTo(
                "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                        "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "    xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>foo</groupId>\n" +
                        "    <artifactId>bar</artifactId>\n" +
                        "    <version>0.0.1-SNAPSHOT</version>\n" +
                        "</project>");
    }

    @Test
    @Disabled("FIXME: 786 Event listener")
    void testAddToDependencyManagement() {
        String givenPomXml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\"\n" +
                        "    xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                        "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                        "  <modelVersion>4.0.0</modelVersion>\n" +
                        "  <groupId>org.springframework.sbm</groupId>\n" +
                        "  <artifactId>dummy-test-artifact</artifactId>\n" +
                        "  <version>1.0.0</version>\n" +
                        "</project>";

        String expectedPomXmlSource =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\"\n" +
                        "    xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                        "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                        "  <modelVersion>4.0.0</modelVersion>\n" +
                        "  <groupId>org.springframework.sbm</groupId>\n" +
                        "  <artifactId>dummy-test-artifact</artifactId>\n" +
                        "  <version>1.0.0</version>\n" +
                        "  <dependencies>\n" +
                        "    <dependency>\n" +
                        "      <groupId>org.slf4j</groupId>\n" +
                        "      <artifactId>slf4j-api</artifactId>\n" +
                        "      <version>1.7.32</version>\n" +
                        "    </dependency>\n" +
                        "  </dependencies>\n" +
                        "</project>";

        ApplicationEventPublisher eventPublisher = mock(ApplicationEventPublisher.class);


        Path fakedProjectPath = Path.of("fakedPath").toAbsolutePath();
        ProjectContext projectContext = TestProjectContext.buildProjectContext(eventPublisher)
                .withProjectRoot(fakedProjectPath)
                .withMavenRootBuildFileSource(givenPomXml)
                // FIXME: onlyIfUsed temporary deactivated by using AlwaysAddDependency
                .withJavaSources(
                        "import java.lang.String;\n" +
                                "class Foo {\n" +
                                "   String s;\n" +
                                "}") // satisfy 'onlyIfUsing' in AddDependency recipe
                .build();

        Dependency addedDependency = Dependency.builder()
                .groupId("org.slf4j")
                .artifactId("slf4j-api")
                .version("1.7.32")
                .build();

        // call sut
        projectContext.getBuildFile().addDependency(addedDependency);

        // assert that DependenciesChangedEvent has been published with the list of dependencies
        ArgumentCaptor<DependenciesChangedEvent> argumentCaptor = ArgumentCaptor.forClass(DependenciesChangedEvent.class);

        assertEventPublished(eventPublisher, argumentCaptor, DependenciesChangedEvent.class, 1);

        List<Path> resolvedDependencies = argumentCaptor.getValue().getResolvedDependencies();
        assertThat(resolvedDependencies).hasSize(1);
        Path pathInMavenRepo = Path.of("org/slf4j/slf4j-api/1.7.32/slf4j-api-1.7.32.jar");
        assertThat(resolvedDependencies.get(0)).endsWith(pathInMavenRepo);

        // assert that the dependency has been added to <dependencies> in pom.xml
        assertThat(projectContext.getBuildFile().print()).isEqualTo(expectedPomXmlSource);

        // assert that resolved dependencies path contains added dependency
        List<Path> resolvedDependenciesPaths = projectContext.getBuildFile().getResolvedDependenciesPaths();
        assertThat(resolvedDependenciesPaths.get(0)).endsWith(pathInMavenRepo);


        // assert that dependency management contains dependency
        Dependency retrievedDependency = projectContext.getBuildFile().getDeclaredDependencies().get(0);
        assertThat(retrievedDependency).isEqualTo(addedDependency);
        assertThat(addedDependency.getGroupId()).isEqualTo("org.slf4j");
        assertThat(addedDependency.getArtifactId()).isEqualTo("slf4j-api");
        assertThat(addedDependency.getVersion()).isEqualTo("1.7.32");
    }

    @Test
    void shouldAddMavenPluginWhenNoPluginSectionExists() {
        String pomXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<project xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\"\n" +
                "    xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                "    <modelVersion>4.0.0</modelVersion>\n" +
                "    <groupId>com.vmware.example</groupId>\n" +
                "    <artifactId>jboss-sample</artifactId>\n" +
                "    <version>1.0.0</version>\n" +
                "</project>";

        String refactoredPomXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<project xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\"\n" +
                "    xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                "    <modelVersion>4.0.0</modelVersion>\n" +
                "    <groupId>com.vmware.example</groupId>\n" +
                "    <artifactId>jboss-sample</artifactId>\n" +
                "    <version>1.0.0</version>\n" +
                "    <build>\n" +
                "        <plugins>\n" +
                "            <plugin>\n" +
                "                <groupId>group.id</groupId>\n" +
                "                <artifactId>some.artifact</artifactId>\n" +
                "            </plugin>\n" +
                "        </plugins>\n" +
                "    </build>\n" +
                "</project>";

        OpenRewriteMavenPlugin plugin = new OpenRewriteMavenPlugin();
        plugin.setGroupId("group.id");
        plugin.setArtifactId("some.artifact");

        BuildFile buildFile = TestProjectContext.buildProjectContext().withMavenRootBuildFileSource(pomXml).build().getBuildFile();
        buildFile.addPlugin(plugin);
        assertEquals(refactoredPomXml, buildFile.print());
    }

    @Test
    void shouldAddMavenPluginWhenEmptyPluginSectionExists() {
        String pomXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<project xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\"\n" +
                "    xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                "    <modelVersion>4.0.0</modelVersion>\n" +
                "    <groupId>com.vmware.example</groupId>\n" +
                "    <artifactId>jboss-sample</artifactId>\n" +
                "    <version>1.0.0</version>\n" +
                "    <build>\n" +
                "        <plugins>\n" +
                "        </plugins>\n" +
                "    </build>\n" +
                "</project>";

        String refactoredPomXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<project xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\"\n" +
                "    xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                "    <modelVersion>4.0.0</modelVersion>\n" +
                "    <groupId>com.vmware.example</groupId>\n" +
                "    <artifactId>jboss-sample</artifactId>\n" +
                "    <version>1.0.0</version>\n" +
                "    <build>\n" +
                "        <plugins>\n" +
                "            <plugin>\n" +
                "                <groupId>group.id</groupId>\n" +
                "                <artifactId>some.artifact</artifactId>\n" +
                "            </plugin>\n" +
                "        </plugins>\n" +
                "    </build>\n" +
                "</project>";

        OpenRewriteMavenPlugin plugin = new OpenRewriteMavenPlugin();
        plugin.setGroupId("group.id");
        plugin.setArtifactId("some.artifact");

        BuildFile buildFile = TestProjectContext.buildProjectContext().withMavenRootBuildFileSource(pomXml).build().getBuildFile();
        buildFile.addPlugin(plugin);
        assertEquals(refactoredPomXml, buildFile.print());
    }

    @Test
    void shouldNotAddMavenPluginWhenSamePluginExists() {
        String pomXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<project xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\"\n" +
                "    xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                "    <modelVersion>4.0.0</modelVersion>\n" +
                "    <groupId>com.vmware.example</groupId>\n" +
                "    <artifactId>jboss-sample</artifactId>\n" +
                "    <version>1.0.0</version>\n" +
                "    <build>\n" +
                "        <plugins>\n" +
                "            <plugin>\n" +
                "                <artifactId>some.artifact</artifactId>\n" +
                "                <groupId>group.id</groupId>\n" +
                "            </plugin>\n" +
                "        </plugins>\n" +
                "    </build>\n" +
                "</project>";

        String refactoredPomXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<project xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\"\n" +
                "    xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                "    <modelVersion>4.0.0</modelVersion>\n" +
                "    <groupId>com.vmware.example</groupId>\n" +
                "    <artifactId>jboss-sample</artifactId>\n" +
                "    <version>1.0.0</version>\n" +
                "    <build>\n" +
                "        <plugins>\n" +
                "            <plugin>\n" +
                "                <artifactId>some.artifact</artifactId>\n" +
                "                <groupId>group.id</groupId>\n" +
                "            </plugin>\n" +
                "        </plugins>\n" +
                "    </build>\n" +
                "</project>";

        OpenRewriteMavenPlugin plugin = new OpenRewriteMavenPlugin();
        plugin.setGroupId("group.id");
        plugin.setArtifactId("some.artifact");

        BuildFile buildFile = TestProjectContext.buildProjectContext().withMavenRootBuildFileSource(pomXml).build().getBuildFile();
        buildFile.addPlugin(plugin);
        assertEquals(refactoredPomXml, buildFile.print());
    }

    @Test
    void shouldAddMavenPluginWhenAnotherPluginExists() {
        String pomXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<project xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\"\n" +
                "    xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                "    <modelVersion>4.0.0</modelVersion>\n" +
                "    <groupId>com.vmware.example</groupId>\n" +
                "    <artifactId>jboss-sample</artifactId>\n" +
                "    <version>1.0.0</version>\n" +
                "    <build>\n" +
                "        <plugins>\n" +
                "            <plugin>\n" +
                "                <groupId>other.group.id</groupId>\n" +
                "                <artifactId>some.other.artifact</artifactId>\n" +
                "            </plugin>\n" +
                "        </plugins>\n" +
                "    </build>\n" +
                "</project>";

        String refactoredPomXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<project xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\"\n" +
                "    xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                "    <modelVersion>4.0.0</modelVersion>\n" +
                "    <groupId>com.vmware.example</groupId>\n" +
                "    <artifactId>jboss-sample</artifactId>\n" +
                "    <version>1.0.0</version>\n" +
                "    <build>\n" +
                "        <plugins>\n" +
                "            <plugin>\n" +
                "                <groupId>other.group.id</groupId>\n" +
                "                <artifactId>some.other.artifact</artifactId>\n" +
                "            </plugin>\n" +
                "            <plugin>\n" +
                "                <groupId>group.id</groupId>\n" +
                "                <artifactId>some.artifact</artifactId>\n" +
                "            </plugin>\n" +
                "        </plugins>\n" +
                "    </build>\n" +
                "</project>";

        OpenRewriteMavenPlugin plugin = new OpenRewriteMavenPlugin();
        plugin.setGroupId("group.id");
        plugin.setArtifactId("some.artifact");

        BuildFile buildFile = TestProjectContext.buildProjectContext().withMavenRootBuildFileSource(pomXml).build().getBuildFile();
        buildFile.addPlugin(plugin);
        assertEquals(refactoredPomXml, buildFile.print());
    }

    @Test
    public void shouldCreateDependencyManagementWithDependencyWhenNoneExists() {

        String before =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>org.openrewrite.maven</groupId>\n" +
                        "    <artifactId>dependency-management-example</artifactId>\n" +
                        "    <version>0.1-SNAPSHOT</version>\n" +
                        "    <name>dependency-management-example</name>\n" +
                        "\n" +
                        "</project>";

        String expected =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>org.openrewrite.maven</groupId>\n" +
                        "    <artifactId>dependency-management-example</artifactId>\n" +
                        "    <version>0.1-SNAPSHOT</version>\n" +
                        "    <name>dependency-management-example</name>\n" +
                        "    <dependencyManagement>\n" +
                        "        <dependencies>\n" +
                        "            <dependency>\n" +
                        "                <groupId>org.junit.jupiter</groupId>\n" +
                        "                <artifactId>junit-jupiter-api</artifactId>\n" +
                        "                <version>5.6.2</version>\n" +
                        "                <scope>test</scope>\n" +
                        "            </dependency>\n" +
                        "        </dependencies>\n" +
                        "    </dependencyManagement>\n" +
                        "\n" +
                        "</project>";

        Dependency dependency = Dependency.builder()
                .scope("test")
                .groupId("org.junit.jupiter")
                .artifactId("junit-jupiter-api")
                .version("5.6.2")
                .build();

        BuildFile buildFile = TestProjectContext.buildProjectContext().withMavenRootBuildFileSource(before).build().getBuildFile();
        buildFile.addToDependencyManagement(dependency);
        assertEquals(expected, buildFile.print());
    }

    @Test
    public void shouldAddDependencyWhenDependencyManagementAlreadyExists() {

        String before =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>org.openrewrite.maven</groupId>\n" +
                        "    <artifactId>dependency-management-example</artifactId>\n" +
                        "    <version>0.1-SNAPSHOT</version>\n" +
                        "    <name>dependency-management-example</name>\n" +
                        "    <dependencyManagement>\n" +
                        "        <dependencies>\n" +
                        "            <dependency>\n" +
                        "                <groupId>org.junit.jupiter</groupId>\n" +
                        "                <artifactId>junit-jupiter-api</artifactId>\n" +
                        "                <version>5.6.2</version>\n" +
                        "                <scope>test</scope>\n" +
                        "            </dependency>\n" +
                        "        </dependencies>\n" +
                        "    </dependencyManagement>\n" +
                        "</project>";

        String expected =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>org.openrewrite.maven</groupId>\n" +
                        "    <artifactId>dependency-management-example</artifactId>\n" +
                        "    <version>0.1-SNAPSHOT</version>\n" +
                        "    <name>dependency-management-example</name>\n" +
                        "    <dependencyManagement>\n" +
                        "        <dependencies>\n" +
                        "            <dependency>\n" +
                        "                <groupId>org.junit.jupiter</groupId>\n" +
                        "                <artifactId>junit-jupiter-api</artifactId>\n" +
                        "                <version>5.6.2</version>\n" +
                        "                <scope>test</scope>\n" +
                        "            </dependency>\n" +
                        "            <dependency>\n" +
                        "                <groupId>org.projectlombok</groupId>\n" +
                        "                <artifactId>lombok</artifactId>\n" +
                        "                <version>1.18.12</version>\n" +
                        "            </dependency>\n" +
                        "        </dependencies>\n" +
                        "    </dependencyManagement>\n" +
                        "</project>";

        Dependency dependency = Dependency.builder().groupId("org.projectlombok").artifactId("lombok").version("1.18.12").build();

        BuildFile buildFile = TestProjectContext.buildProjectContext().withMavenRootBuildFileSource(before).build().getBuildFile();
        buildFile.addToDependencyManagement(dependency);
        assertEquals(expected, buildFile.print());
    }

    @Test
    public void shouldUpdateVersionIfDifferent() {

        String before =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>org.openrewrite.maven</groupId>\n" +
                        "    <artifactId>dependency-management-example</artifactId>\n" +
                        "    <version>0.1-SNAPSHOT</version>\n" +
                        "    <name>dependency-management-example</name>\n" +
                        "    <dependencyManagement>\n" +
                        "        <dependencies>\n" +
                        "            <dependency>\n" +
                        "                <groupId>org.junit.jupiter</groupId>\n" +
                        "                <artifactId>junit-jupiter-api</artifactId>\n" +
                        "                <version>5.6.2</version>\n" +
                        "                <scope>test</scope>\n" +
                        "            </dependency>\n" +
                        "        </dependencies>\n" +
                        "    </dependencyManagement>\n" +
                        "\n" +
                        "</project>";

        String expected =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>org.openrewrite.maven</groupId>\n" +
                        "    <artifactId>dependency-management-example</artifactId>\n" +
                        "    <version>0.1-SNAPSHOT</version>\n" +
                        "    <name>dependency-management-example</name>\n" +
                        "    <dependencyManagement>\n" +
                        "        <dependencies>\n" +
                        "            <dependency>\n" +
                        "                <groupId>org.junit.jupiter</groupId>\n" +
                        "                <artifactId>junit-jupiter-api</artifactId>\n" +
                        "                <version>10.100</version>\n" +
                        "                <scope>test</scope>\n" +
                        "            </dependency>\n" +
                        "        </dependencies>\n" +
                        "    </dependencyManagement>\n" +
                        "\n" +
                        "</project>";

        Dependency dependency = Dependency.builder()
                .groupId("org.junit.jupiter")
                .artifactId("junit-jupiter-api")
                .version("10.100")
                .scope("test")
                .build();

        ApplicationEventPublisher eventPublisher = mock(ApplicationEventPublisher.class);
        BuildFile buildFile = TestProjectContext.buildProjectContext().withMavenRootBuildFileSource(before).build().getBuildFile();
        buildFile.addToDependencyManagement(dependency);
        assertEquals(expected, buildFile.print());
    }

    @Test
    public void shouldUpdateScopeIfDifferent() {

        String before =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>org.openrewrite.maven</groupId>\n" +
                        "    <artifactId>dependency-management-example</artifactId>\n" +
                        "    <version>0.1-SNAPSHOT</version>\n" +
                        "    <name>dependency-management-example</name>\n" +
                        "    <dependencyManagement>\n" +
                        "        <dependencies>\n" +
                        "            <dependency>\n" +
                        "                <groupId>org.junit.jupiter</groupId>\n" +
                        "                <artifactId>junit-jupiter-api</artifactId>\n" +
                        "                <version>5.6.2</version>\n" +
                        "                <scope>compile</scope>\n" +
                        "            </dependency>\n" +
                        "        </dependencies>\n" +
                        "    </dependencyManagement>\n" +
                        "\n" +
                        "</project>";

        String expected =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>org.openrewrite.maven</groupId>\n" +
                        "    <artifactId>dependency-management-example</artifactId>\n" +
                        "    <version>0.1-SNAPSHOT</version>\n" +
                        "    <name>dependency-management-example</name>\n" +
                        "    <dependencyManagement>\n" +
                        "        <dependencies>\n" +
                        "            <dependency>\n" +
                        "                <groupId>org.junit.jupiter</groupId>\n" +
                        "                <artifactId>junit-jupiter-api</artifactId>\n" +
                        "                <version>10.100</version>\n" +
                        "                <scope>test</scope>\n" +
                        "            </dependency>\n" +
                        "        </dependencies>\n" +
                        "    </dependencyManagement>\n" +
                        "\n" +
                        "</project>";

        Dependency dependency = Dependency.builder()
                .groupId("org.junit.jupiter")
                .artifactId("junit-jupiter-api")
                .version("10.100")
                .scope("test")
                .build();

        BuildFile buildFile = TestProjectContext.buildProjectContext().withMavenRootBuildFileSource(before).build().getBuildFile();
        buildFile.addToDependencyManagement(dependency);
        assertEquals(expected, buildFile.print());
    }


    @Test
    public void shouldRemoveScopeIfRemoved() {

        String before =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>org.openrewrite.maven</groupId>\n" +
                        "    <artifactId>dependency-management-example</artifactId>\n" +
                        "    <version>0.1-SNAPSHOT</version>\n" +
                        "    <name>dependency-management-example</name>\n" +
                        "    <dependencyManagement>\n" +
                        "        <dependencies>\n" +
                        "            <dependency>\n" +
                        "                <groupId>org.junit.jupiter</groupId>\n" +
                        "                <artifactId>junit-jupiter-api</artifactId>\n" +
                        "                <version>5.6.2</version>\n" +
                        "                <scope>compile</scope>\n" +
                        "            </dependency>\n" +
                        "        </dependencies>\n" +
                        "    </dependencyManagement>\n" +
                        "\n" +
                        "</project>";

        String expected =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>org.openrewrite.maven</groupId>\n" +
                        "    <artifactId>dependency-management-example</artifactId>\n" +
                        "    <version>0.1-SNAPSHOT</version>\n" +
                        "    <name>dependency-management-example</name>\n" +
                        "    <dependencyManagement>\n" +
                        "        <dependencies>\n" +
                        "            <dependency>\n" +
                        "                <groupId>org.junit.jupiter</groupId>\n" +
                        "                <artifactId>junit-jupiter-api</artifactId>\n" +
                        "                <version>10.100</version>\n" +
                        "            </dependency>\n" +
                        "        </dependencies>\n" +
                        "    </dependencyManagement>\n" +
                        "\n" +
                        "</project>";

        Dependency dependency = Dependency.builder()
                .groupId("org.junit.jupiter")
                .artifactId("junit-jupiter-api")
                .version("10.100")
                .scope(null)
                .build();

        BuildFile buildFile = TestProjectContext.buildProjectContext()
                .withMavenRootBuildFileSource(before)
                .build()
                .getBuildFile();

        buildFile.addToDependencyManagement(dependency);
        assertEquals(expected, buildFile.print());
    }

    @Test
    public void packagingType() {

        String before =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>org.openrewrite.maven</groupId>\n" +
                        "    <artifactId>dependency-management-example</artifactId>\n" +
                        "    <version>0.1-SNAPSHOT</version>\n" +
                        "    <packaging>war</packaging>\n" +
                        "\n" +
                        "</project>";

        String expected =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>org.openrewrite.maven</groupId>\n" +
                        "    <artifactId>dependency-management-example</artifactId>\n" +
                        "    <version>0.1-SNAPSHOT</version>\n" +
                        "    <packaging>jar</packaging>\n" +
                        "\n" +
                        "</project>";

        BuildFile buildFile = TestProjectContext.buildProjectContext()
                .withMavenRootBuildFileSource(before)
                .build()
                .getBuildFile();

        assertThat(buildFile.getPackaging()).isEqualTo("war");
        buildFile.setPackaging("jar");
        assertThat(buildFile.getPackaging()).isEqualTo("jar");
    }

    @Test
    void testHasPlugin() {
        String pomXml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" \n" +
                        "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" \n" +
                        "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>com.vmwre.sbm.examples</groupId>\n" +
                        "    <artifactId>migrate-jsf-2.x-to-spring-boot</artifactId>\n" +
                        "    <version>1.0</version>\n" +
                        "    <packaging>jar</packaging>\n" +
                        "    <build>\n" +
                        "        <plugins>\n" +
                        "            <plugin>\n" +
                        "                <groupId>com.example</groupId>\n" +
                        "                <artifactId>the-example</artifactId>\n" +
                        "            </plugin>\n" +
                        "        </plugins>\n" +
                        "    </build>\n" +
                        "</project>\n";

        BuildFile buildFile = TestProjectContext.buildProjectContext()
                .withMavenRootBuildFileSource(pomXml)
                .build()
                .getBuildFile();

        OpenRewriteMavenPlugin plugin = OpenRewriteMavenPlugin.builder()
                .groupId("com.example")
                .artifactId("the-example")
                .build();

        boolean hasPlugin = buildFile.hasPlugin(plugin);

        assertThat(hasPlugin).isTrue();
    }

    @Test
    void testGetNameWithNameShouldReturnName() {
        String pomXml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" \n" +
                        "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" \n" +
                        "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>com.vmwre.sbm.examples</groupId>\n" +
                        "    <artifactId>migrate-jsf-2.x-to-spring-boot</artifactId>\n" +
                        "    <version>1.0</version>\n" +
                        "    <name>the-name</name>\n" +
                        "</project>\n";

        Optional<String> name = TestProjectContext.buildProjectContext()
                .withMavenRootBuildFileSource(pomXml)
                .build()
                .getBuildFile()
                .getName();

        assertThat(name.get()).isEqualTo("the-name");
    }

    @Test
    void testGetNameWithNoNameShouldReturnEmptyOptional() {
        String pomXml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" \n" +
                        "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" \n" +
                        "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>com.vmwre.sbm.examples</groupId>\n" +
                        "    <artifactId>migrate-jsf-2.x-to-spring-boot</artifactId>\n" +
                        "    <version>1.0</version>\n" +
                        "</project>\n";

        Optional<String> name = TestProjectContext.buildProjectContext()
                .withMavenRootBuildFileSource(pomXml)
                .build()
                .getBuildFile()
                .getName();

        assertThat(name).isEmpty();
    }

    @Test
    void getPlugins() {
        String pomXml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">\n" +
                        "\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>org.mule.examples</groupId>\n" +
                        "    <artifactId>hello-world</artifactId>\n" +
                        "    <version>2.1.5-SNAPSHOT</version>\n" +
                        "    <packaging>jar</packaging>\n" +
                        "    <name>hello-world</name>" +
                        " <build>\n" +
                        "        <plugins>\n" +
                        "            <plugin>\n" +
                        "                <groupId>org.mule.tools.maven</groupId>\n" +
                        "                <artifactId>mule-maven-plugin</artifactId>\n" +
                        "                <version>${mule.maven.plugin.version}</version>\n" +
                        "                <extensions>true</extensions>\n" +
                        "                <configuration>\n" +
                        "                    <classifier>mule-application-example</classifier>\n" +
                        "                </configuration>\n" +
                        "            </plugin>\n" +
                        "            <plugin>\n" +
                        "                <groupId>com.mulesoft.munit.tools</groupId>\n" +
                        "                <artifactId>munit-maven-plugin</artifactId>\n" +
                        "                <version>${munit.version}</version>\n" +
                        "                <executions>\n" +
                        "                    <execution>\n" +
                        "                        <id>test</id>\n" +
                        "                        <phase>test</phase>\n" +
                        "                        <goals>\n" +
                        "                            <goal>test</goal>\n" +
                        "                            <goal>coverage-report</goal>\n" +
                        "                        </goals>\n" +
                        "                    </execution>\n" +
                        "                </executions>\n" +
                        "                <configuration>\n" +
                        "                    <coverage>\n" +
                        "                        <runCoverage>true</runCoverage>\n" +
                        "                        <formats>\n" +
                        "                            <format>html</format>\n" +
                        "                        </formats>\n" +
                        "                    </coverage>\n" +
                        "                    <runtimeVersion>${app.runtime}</runtimeVersion>\n" +
                        "                    <dynamicPorts>\n" +
                        "                        <dynamicPort>http.port</dynamicPort>\n" +
                        "                    </dynamicPorts>\n" +
                        "                </configuration>\n" +
                        "            </plugin>\n" +
                        "        </plugins>\n" +
                        "    </build>\n" +
                        "</project>";

        BuildFile openRewriteMavenBuildFile = TestProjectContext.buildProjectContext().withMavenRootBuildFileSource(pomXml).build().getBuildFile();

        List<Plugin> plugins = openRewriteMavenBuildFile.getPlugins();

        assertThat(plugins).hasSize(2);
        assertThat(plugins.get(0).getGroupId()).isEqualTo("org.mule.tools.maven");
        assertThat(plugins.get(0).getArtifactId()).isEqualTo("mule-maven-plugin");
        assertThat(plugins.get(0).getVersion()).isEqualTo("${mule.maven.plugin.version}");
        assertThat(plugins.get(0).getConfiguration().getPropertyKeys()).isNotEmpty();

		assertThat(plugins.get(1).getGroupId()).isEqualTo("com.mulesoft.munit.tools");
        assertThat(plugins.get(1).getArtifactId()).isEqualTo("munit-maven-plugin");
        assertThat(plugins.get(1).getVersion()).isEqualTo("${munit.version}");
        assertThat(plugins.get(1).getConfiguration().getPropertyKeys()).isNotEmpty();
        assertThat(plugins.get(1).getExecutions()).isNotEmpty();
        assertThat(plugins.get(1).getExecutions().get(0).getId()).isEqualTo("test");
        assertThat(plugins.get(1).getExecutions().get(0).getPhase()).isEqualTo("test");
        assertThat(plugins.get(1).getExecutions().get(0).getGoals()).hasSize(2);
        assertThat(plugins.get(1).getExecutions().get(0).getGoals().get(0)).isEqualTo("test");
        assertThat(plugins.get(1).getExecutions().get(0).getGoals().get(1)).isEqualTo("coverage-report");
    }

	@Test
	void deserializePluginConfiguration() {

		String pomXml =
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
						"<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">\n" +
						"\n" +
						"    <modelVersion>4.0.0</modelVersion>\n" +
						"    <groupId>org.springframework.boot</groupId>\n" +
						"    <artifactId>spring-boot-starter-parent</artifactId>\n" +
						"    <version>2.7.3</version>\n" +
						"    <packaging>jar</packaging>\n" +
						"    <name>hello-world</name>" +
						" <properties>\n" +
						"       <source>17</source>\n" +
						"       <target>17</target>\n" +
						" </properties>\n" +
						" <build>\n" +
						"        <plugins>\n" +
						"            <plugin>\n" +
						"                <groupId>org.apache.maven.plugins</groupId>\n" +
						"                <artifactId>maven-compiler-plugin</artifactId>\n" +
						"                <configuration>\n" +
						"                    <source>${source}</source>\n" +
						"                    <release>${source}</release>\n" +
						"                    <target>17</target>\n" +
						"                    <fork>false</fork>\n" +
                        "                    <showWarnings>true</showWarnings>\n" +
                        "                    <showDeprecation>true</showDeprecation>\n"+
						"                    <compilerArgs>\n" +
						"                        <compilerArg>-J-Duser.language=en_us</compilerArg>\n" +
						"                    </compilerArgs>\n" +
						"                </configuration>\n" +
						"            </plugin>\n" +
						"        </plugins>\n" +
						"    </build>\n" +
						"</project>";

		BuildFile openRewriteMavenBuildFile = TestProjectContext.buildProjectContext().withMavenRootBuildFileSource(pomXml).build().getBuildFile();

		Plugin compilerPlugin = openRewriteMavenBuildFile.getPlugins()
				.stream()
				.filter(plugin -> plugin.getGroupId().equals("org.apache.maven.plugins") &&
						plugin.getArtifactId().equals("maven-compiler-plugin"))
				.findAny().orElseThrow();

		assertThat(compilerPlugin.getConfiguration().getDeclaredStringValue("source").get()).isEqualTo("${source}");
		assertThat(compilerPlugin.getConfiguration().getDeclaredStringValue("target").get()).isEqualTo("17");
		assertThat(compilerPlugin.getConfiguration().getDeclaredStringValue("fork").get()).isEqualTo("false");
	}

	@Test
	void serializePluginConfiguration() {

		String pomXml =
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
						"<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">\n" +
						"\n" +
						"    <modelVersion>4.0.0</modelVersion>\n" +
						"    <groupId>org.springframework.boot</groupId>\n" +
						"    <artifactId>spring-boot-starter-parent</artifactId>\n" +
						"    <version>2.7.3</version>\n" +
						"    <packaging>jar</packaging>\n" +
						"    <name>hello-world</name>" +
						" <build>\n" +
						"        <plugins>\n" +
						"            <plugin>\n" +
						"                <groupId>org.apache.maven.plugins</groupId>\n" +
						"                <artifactId>maven-compiler-plugin</artifactId>\n" +
						"            </plugin>\n" +
						"        </plugins>\n" +
						"    </build>\n" +
						"</project>";

		String expected =
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
						"<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">\n" +
						"\n" +
						"    <modelVersion>4.0.0</modelVersion>\n" +
						"    <groupId>org.springframework.boot</groupId>\n" +
						"    <artifactId>spring-boot-starter-parent</artifactId>\n" +
						"    <version>2.7.3</version>\n" +
						"    <packaging>jar</packaging>\n" +
						"    <name>hello-world</name>" +
						" <build>\n" +
						"        <plugins>\n" +
						"            <plugin>\n" +
						"                <groupId>org.apache.maven.plugins</groupId>\n" +
						"                <artifactId>maven-compiler-plugin</artifactId>\n" +
						"                <configuration>\n" +
						"                    <source>17</source>\n" +
						"                    <target>17</target>\n" +
						"                </configuration>\n" +
						"            </plugin>\n" +
						"        </plugins>\n" +
						"    </build>\n" +
						"</project>";

		BuildFile openRewriteMavenBuildFile = TestProjectContext.buildProjectContext().withMavenRootBuildFileSource(pomXml).build().getBuildFile();

		Plugin compilerPlugin = openRewriteMavenBuildFile.getPlugins()
				.stream()
				.filter(plugin -> plugin.getGroupId().equals("org.apache.maven.plugins") &&
						plugin.getArtifactId().equals("maven-compiler-plugin"))
				.findAny().orElseThrow();

		compilerPlugin.getConfiguration().setDeclaredStringValue("source", "17");
		compilerPlugin.getConfiguration().setDeclaredStringValue("target", "17");

		assertThat(openRewriteMavenBuildFile.print()).isEqualTo(expected);

	}

	@Test
	void deleteProperty_withSingleModule() {

		String pomXml =
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
						"<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">\n" +
						"\n" +
						"    <modelVersion>4.0.0</modelVersion>\n" +
						"    <groupId>org.springframework.boot</groupId>\n" +
						"    <artifactId>spring-boot-starter-parent</artifactId>\n" +
						"    <version>2.7.3</version>\n" +
						"    <packaging>jar</packaging>\n" +
						"    <name>hello-world</name>" +
						" <properties>\n" +
						"       <java.version>17</java.version>\n" +
						"       <maven.compiler.source>17</maven.compiler.source>\n" +
						" </properties>\n" +
						" <build>\n" +
						"        <plugins>\n" +
						"            <plugin>\n" +
						"                <groupId>org.apache.maven.plugins</groupId>\n" +
						"                <artifactId>maven-compiler-plugin</artifactId>\n" +
						"            </plugin>\n" +
						"        </plugins>\n" +
						"    </build>\n" +
						"</project>";

		String expected =
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
						"<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">\n" +
						"\n" +
						"    <modelVersion>4.0.0</modelVersion>\n" +
						"    <groupId>org.springframework.boot</groupId>\n" +
						"    <artifactId>spring-boot-starter-parent</artifactId>\n" +
						"    <version>2.7.3</version>\n" +
						"    <packaging>jar</packaging>\n" +
						"    <name>hello-world</name>" +
						" <properties>\n" +
						"       <maven.compiler.source>17</maven.compiler.source>\n" +
						" </properties>\n" +
						" <build>\n" +
						"        <plugins>\n" +
						"            <plugin>\n" +
						"                <groupId>org.apache.maven.plugins</groupId>\n" +
						"                <artifactId>maven-compiler-plugin</artifactId>\n" +
						"            </plugin>\n" +
						"        </plugins>\n" +
						"    </build>\n" +
						"</project>";

		BuildFile openRewriteMavenBuildFile = TestProjectContext.buildProjectContext().withMavenRootBuildFileSource(pomXml).build().getBuildFile();

		openRewriteMavenBuildFile.deleteProperty("java.version");

		assertThat(openRewriteMavenBuildFile.print()).isEqualTo(expected);

	}

    @Test
    void deletePropertyCalledOnRootModule_withMultiModules() {
        String rootPom = PomBuilder.buildPom("com.example:parent:1.0")
                .packaging("pom")
                .property("maven.compiler.source", "17")
                .property("maven.compiler.target", "17")
                .withModules("module1")
                .build();

        String module1Pom = PomBuilder.buildPom("com.example:parent:1.0", "module1")
                .packaging("jar")
                .property("maven.compiler.source", "17")
                .property("maven.compiler.target", "17")
                .build();

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withMavenBuildFileSource("pom.xml", rootPom)
                .withMavenBuildFileSource("module1/pom.xml", module1Pom)
                .build();

        BuildFile rootModule = projectContext.getApplicationModules().getRootModule().getBuildFile();

        rootModule.deleteProperty("maven.compiler.source");

        assertThat(rootModule.getProperty("maven.compiler.source")).isNull();
        assertThat(rootModule.getProperty("maven.compiler.target")).isEqualTo("17");
        assertThat(rootModule.print()).isEqualTo(  """
                                                                                          <?xml version="1.0" encoding="UTF-8"?>
                                                                                          <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                                                                                              <modelVersion>4.0.0</modelVersion>
                                                                                              <groupId>com.example</groupId>
                                                                                              <artifactId>parent</artifactId>
                                                                                              <version>1.0</version>
                                                                                              <packaging>pom</packaging>
                                                                                              <properties>
                                                                                                  <maven.compiler.target>17</maven.compiler.target>
                                                                                              </properties>
                                                                                              <modules>
                                                                                                  <module>module1</module>
                                                                                              </modules>
                                                                                          </project>
                                                                                          """);

        BuildFile module1 = projectContext.getApplicationModules().getModule("module1").getBuildFile();
        assertThat(module1.getProperty("maven.compiler.source")).isEqualTo("17");
        assertThat(module1.getProperty("maven.compiler.target")).isEqualTo("17");
        assertThat(module1.print()).isEqualTo("""
                                                                                          <?xml version="1.0" encoding="UTF-8"?>
                                                                                          <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                                                                                              <modelVersion>4.0.0</modelVersion>
                                                                                              <parent>
                                                                                                  <groupId>com.example</groupId>
                                                                                                  <artifactId>parent</artifactId>
                                                                                                  <version>1.0</version>
                                                                                              </parent>
                                                                                              <artifactId>module1</artifactId>
                                                                                              <packaging>jar</packaging>
                                                                                              <properties>
                                                                                                  <maven.compiler.target>17</maven.compiler.target>
                                                                                                  <maven.compiler.source>17</maven.compiler.source>
                                                                                              </properties>
                                                                                          </project>
                                                                                          """);
    }

    @Test
    void deletePropertyCalledOnChildModule_withMultiModules() {
        String rootPom = PomBuilder.buildPom("com.example:parent:1.0")
                .packaging("pom")
                .property("maven.compiler.source", "17")
                .property("maven.compiler.target", "17")
                .withModules("module1")
                .build();

        String module1Pom = PomBuilder.buildPom("com.example:parent:1.0", "module1")
                .packaging("jar")
                .property("maven.compiler.source", "17")
                .property("maven.compiler.target", "17")
                .build();

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withMavenBuildFileSource("pom.xml", rootPom)
                .withMavenBuildFileSource("module1/pom.xml", module1Pom)
                .build();

        BuildFile rootModule = projectContext.getApplicationModules().getRootModule().getBuildFile();
        BuildFile module1 = projectContext.getApplicationModules().getModule("module1").getBuildFile();

        module1.deleteProperty("maven.compiler.source");

        assertThat(rootModule.getProperty("maven.compiler.source")).isEqualTo("17");
        assertThat(rootModule.getProperty("maven.compiler.target")).isEqualTo("17");
        assertThat(rootModule.print()).isEqualTo("""
                                                  <?xml version="1.0" encoding="UTF-8"?>
                                                  <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                                                      <modelVersion>4.0.0</modelVersion>
                                                      <groupId>com.example</groupId>
                                                      <artifactId>parent</artifactId>
                                                      <version>1.0</version>
                                                      <packaging>pom</packaging>
                                                      <properties>
                                                          <maven.compiler.target>17</maven.compiler.target>
                                                          <maven.compiler.source>17</maven.compiler.source>
                                                      </properties>
                                                      <modules>
                                                          <module>module1</module>
                                                      </modules>
                                                  </project>
                                                  """);


        assertThat(module1.getProperty("maven.compiler.source")).isNull();
        assertThat(module1.getProperty("maven.compiler.target")).isEqualTo("17");
        assertThat(module1.print()).isEqualTo("""
                                              <?xml version="1.0" encoding="UTF-8"?>
                                              <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                                                  <modelVersion>4.0.0</modelVersion>
                                                  <parent>
                                                      <groupId>com.example</groupId>
                                                      <artifactId>parent</artifactId>
                                                      <version>1.0</version>
                                                  </parent>
                                                  <artifactId>module1</artifactId>
                                                  <packaging>jar</packaging>
                                                  <properties>
                                                      <maven.compiler.target>17</maven.compiler.target>
                                                  </properties>
                                              </project>
                                              """);
    }

	@Test
    void removePluginsMatchingRegex() {
        String pomXml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">\n" +
                        "\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>org.mule.examples</groupId>\n" +
                        "    <artifactId>hello-world</artifactId>\n" +
                        "    <version>2.1.5-SNAPSHOT</version>\n" +
                        "    <packaging>jar</packaging>\n" +
                        "    <name>hello-world</name>" +
                        " <build>\n" +
                        "        <plugins>\n" +
                        "            <plugin>\n" +
                        "                <groupId>org.mule.tools.maven</groupId>\n" +
                        "                <artifactId>mule-maven-plugin</artifactId>\n" +
                        "                <version>${mule.maven.plugin.version}</version>\n" +
                        "                <extensions>true</extensions>\n" +
                        "                <configuration>\n" +
                        "                    <classifier>mule-application-example</classifier>\n" +
                        "                </configuration>\n" +
                        "            </plugin>\n" +
                        "            <plugin>\n" +
                        "                <groupId>com.mulesoft.munit.tools</groupId>\n" +
                        "                <artifactId>munit-maven-plugin</artifactId>\n" +
                        "                <version>${munit.version}</version>\n" +
                        "                <executions>\n" +
                        "                    <execution>\n" +
                        "                        <id>test</id>\n" +
                        "                        <phase>test</phase>\n" +
                        "                        <goals>\n" +
                        "                            <goal>test</goal>\n" +
                        "                            <goal>coverage-report</goal>\n" +
                        "                        </goals>\n" +
                        "                    </execution>\n" +
                        "                </executions>\n" +
                        "                <configuration>\n" +
                        "                    <coverage>\n" +
                        "                        <runCoverage>true</runCoverage>\n" +
                        "                        <formats>\n" +
                        "                            <format>html</format>\n" +
                        "                        </formats>\n" +
                        "                    </coverage>\n" +
                        "                    <runtimeVersion>${app.runtime}</runtimeVersion>\n" +
                        "                    <dynamicPorts>\n" +
                        "                        <dynamicPort>http.port</dynamicPort>\n" +
                        "                    </dynamicPorts>\n" +
                        "                </configuration>\n" +
                        "            </plugin>\n" +
                        "        </plugins>\n" +
                        "    </build>\n" +
                        "</project>";

        String expected =
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">\n" +
                        "\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>org.mule.examples</groupId>\n" +
                        "    <artifactId>hello-world</artifactId>\n" +
                        "    <version>2.1.5-SNAPSHOT</version>\n" +
                        "    <packaging>jar</packaging>\n" +
                        "    <name>hello-world</name> <build>\n" +
                        "        <plugins>\n" +
                        "            <plugin>\n" +
                        "                <groupId>org.mule.tools.maven</groupId>\n" +
                        "                <artifactId>mule-maven-plugin</artifactId>\n" +
                        "                <version>${mule.maven.plugin.version}</version>\n" +
                        "                <extensions>true</extensions>\n" +
                        "                <configuration>\n" +
                        "                    <classifier>mule-application-example</classifier>\n" +
                        "                </configuration>\n" +
                        "            </plugin>\n" +
                        "        </plugins>\n" +
                        "    </build>\n" +
                        "</project>";

        BuildFile openRewriteMavenBuildFile = TestProjectContext.buildProjectContext().withMavenRootBuildFileSource(pomXml).build().getBuildFile();

        openRewriteMavenBuildFile.removePluginsMatchingRegex("com\\.mulesoft\\..*");


        assertThat(openRewriteMavenBuildFile.print()).isEqualTo(expected);
    }

    @Test
    void removePlugins() {
        String pomXml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">\n" +
                        "\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>org.mule.examples</groupId>\n" +
                        "    <artifactId>hello-world</artifactId>\n" +
                        "    <version>2.1.5-SNAPSHOT</version>\n" +
                        "    <packaging>jar</packaging>\n" +
                        "    <name>hello-world</name>" +
                        " <build>\n" +
                        "        <plugins>\n" +
                        "            <plugin>\n" +
                        "                <groupId>org.mule.tools.maven</groupId>\n" +
                        "                <artifactId>mule-maven-plugin</artifactId>\n" +
                        "                <version>${mule.maven.plugin.version}</version>\n" +
                        "                <extensions>true</extensions>\n" +
                        "                <configuration>\n" +
                        "                    <classifier>mule-application-example</classifier>\n" +
                        "                </configuration>\n" +
                        "            </plugin>\n" +
                        "            <plugin>\n" +
                        "                <groupId>com.mulesoft.munit.tools</groupId>\n" +
                        "                <artifactId>munit-maven-plugin</artifactId>\n" +
                        "                <version>${munit.version}</version>\n" +
                        "                <executions>\n" +
                        "                    <execution>\n" +
                        "                        <id>test</id>\n" +
                        "                        <phase>test</phase>\n" +
                        "                        <goals>\n" +
                        "                            <goal>test</goal>\n" +
                        "                            <goal>coverage-report</goal>\n" +
                        "                        </goals>\n" +
                        "                    </execution>\n" +
                        "                </executions>\n" +
                        "                <configuration>\n" +
                        "                    <coverage>\n" +
                        "                        <runCoverage>true</runCoverage>\n" +
                        "                        <formats>\n" +
                        "                            <format>html</format>\n" +
                        "                        </formats>\n" +
                        "                    </coverage>\n" +
                        "                    <runtimeVersion>${app.runtime}</runtimeVersion>\n" +
                        "                    <dynamicPorts>\n" +
                        "                        <dynamicPort>http.port</dynamicPort>\n" +
                        "                    </dynamicPorts>\n" +
                        "                </configuration>\n" +
                        "            </plugin>\n" +
                        "        </plugins>\n" +
                        "    </build>\n" +
                        "</project>";

        String expected =
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">\n" +
                        "\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>org.mule.examples</groupId>\n" +
                        "    <artifactId>hello-world</artifactId>\n" +
                        "    <version>2.1.5-SNAPSHOT</version>\n" +
                        "    <packaging>jar</packaging>\n" +
                        "    <name>hello-world</name> <build>\n" +
                        "        <plugins>\n" +
                        "            <plugin>\n" +
                        "                <groupId>org.mule.tools.maven</groupId>\n" +
                        "                <artifactId>mule-maven-plugin</artifactId>\n" +
                        "                <version>${mule.maven.plugin.version}</version>\n" +
                        "                <extensions>true</extensions>\n" +
                        "                <configuration>\n" +
                        "                    <classifier>mule-application-example</classifier>\n" +
                        "                </configuration>\n" +
                        "            </plugin>\n" +
                        "        </plugins>\n" +
                        "    </build>\n" +
                        "</project>";

        BuildFile openRewriteMavenBuildFile = TestProjectContext.buildProjectContext().withMavenRootBuildFileSource(pomXml).build().getBuildFile();

        openRewriteMavenBuildFile.removePlugins("com.mulesoft.munit.tools:munit-maven-plugin");


        assertThat(openRewriteMavenBuildFile.print()).isEqualTo(expected);
    }

    @Test
    // FIXME: To make this setProperty work with multi-module projects the MavenBuildFileRefactoring as well as the ResourceWrapper logic must be refactored.
    void setProperty() {
        String parentPom = PomBuilder.buildPom("com.example:parent:0.1")
                .packaging("pom")
                .withModules("moduleA")
                .withProperties(Map.of("some-property", "value1"))
                .build();
        String moduleA = PomBuilder.buildPom("com.example:parent:0.1", "moduleA").build();
        ProjectContext context = TestProjectContext
                .buildProjectContext()
                .withMavenRootBuildFileSource(parentPom)
                .withMavenBuildFileSource("moduleA", moduleA)
                .build();
        BuildFile moduleABuildFile = context.getApplicationModules().getTopmostApplicationModules().get(0).getBuildFile();
         moduleABuildFile.setProperty("some-property", "different-value");

        assertThat(moduleABuildFile.getProperty("some-property")).isEqualTo("different-value");
        assertThat(context.getApplicationModules().getRootModule().getBuildFile().getProperty("some-property")).isEqualTo("value1");
    }

    @Test
    void hasParentWithParentShouldReturnTrue() {
        String pomXml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <parent>\n" +
                        "        <groupId>org.springframework.boot</groupId>\n" +
                        "        <artifactId>spring-boot-starter-parent</artifactId>\n" +
                        "        <version>2.4.12</version>\n" +
                        "        <relativePath/> <!-- lookup parent from repository -->\n" +
                        "    </parent>\n" +
                        "    <groupId>com.example</groupId>\n" +
                        "    <artifactId>spring-boot-24-to-25-example</artifactId>\n" +
                        "    <version>0.0.1-SNAPSHOT</version>\n" +
                        "    <name>spring-boot-2.4-to-2.5-example</name>\n" +
                        "    <description>spring-boot-2.4-to-2.5-example</description>\n" +
                        "    <properties>\n" +
                        "        <java.version>11</java.version>\n" +
                        "    </properties>\n" +
                        "</project>\n";

        BuildFile openRewriteMavenBuildFile = TestProjectContext.buildProjectContext().withMavenRootBuildFileSource(pomXml).build().getBuildFile();
        assertThat(openRewriteMavenBuildFile.hasParent()).isTrue();
    }

    @Test
    void hasParentWithoutParentShouldReturnFalse() {
        String pomXml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>com.example</groupId>\n" +
                        "    <artifactId>spring-boot-24-to-25-example</artifactId>\n" +
                        "    <version>0.0.1-SNAPSHOT</version>\n" +
                        "    <name>spring-boot-2.4-to-2.5-example</name>\n" +
                        "    <description>spring-boot-2.4-to-2.5-example</description>\n" +
                        "    <properties>\n" +
                        "        <java.version>11</java.version>\n" +
                        "    </properties>\n" +
                        "</project>\n";

        BuildFile openRewriteMavenBuildFile = TestProjectContext.buildProjectContext().withMavenRootBuildFileSource(pomXml).build().getBuildFile();
        assertThat(openRewriteMavenBuildFile.hasParent()).isFalse();
    }

    @Test
    void hasParentWithParentShouldReturnParent() {
        String pomXml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <parent>\n" +
                        "        <groupId>org.springframework.boot</groupId>\n" +
                        "        <artifactId>spring-boot-starter-parent</artifactId>\n" +
                        "        <version>2.4.12</version>\n" +
                        "        <relativePath/> <!-- lookup parent from repository -->\n" +
                        "    </parent>\n" +
                        "    <groupId>com.example</groupId>\n" +
                        "    <artifactId>spring-boot-24-to-25-example</artifactId>\n" +
                        "    <version>0.0.1-SNAPSHOT</version>\n" +
                        "    <name>spring-boot-2.4-to-2.5-example</name>\n" +
                        "    <description>spring-boot-2.4-to-2.5-example</description>\n" +
                        "    <properties>\n" +
                        "        <java.version>11</java.version>\n" +
                        "    </properties>\n" +
                        "</project>\n";

        BuildFile openRewriteMavenBuildFile = TestProjectContext.buildProjectContext().withMavenRootBuildFileSource(pomXml).build().getBuildFile();

        assertThat(openRewriteMavenBuildFile.getParentPomDeclaration()).isNotEmpty();
        assertThat(openRewriteMavenBuildFile.getParentPomDeclaration().get().getGroupId()).isEqualTo("org.springframework.boot");
        assertThat(openRewriteMavenBuildFile.getParentPomDeclaration().get().getArtifactId()).isEqualTo("spring-boot-starter-parent");
        assertThat(openRewriteMavenBuildFile.getParentPomDeclaration().get().getVersion()).isEqualTo("2.4.12");
    }

    @Test
    void upgradeParentVersion() {
        String pomXml = """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <parent>
                        <groupId>org.springframework.boot</groupId>
                        <artifactId>spring-boot-starter-parent</artifactId>
                        <version>2.4.12</version>
                        <relativePath/> <!-- lookup parent from repository -->
                    </parent>
                    <groupId>com.example</groupId>
                    <artifactId>spring-boot-24-to-25-example</artifactId>
                    <version>0.0.1-SNAPSHOT</version>
                    <name>spring-boot-2.4-to-2.5-example</name>
                    <description>spring-boot-2.4-to-2.5-example</description>
                    <properties>
                        <java.version>11</java.version>
                    </properties>
                </project>
                """;

        BuildFile openRewriteMavenBuildFile = TestProjectContext.buildProjectContext().withMavenRootBuildFileSource(pomXml).build().getBuildFile();

        openRewriteMavenBuildFile.upgradeParentVersion("2.5.6");

        assertThat(openRewriteMavenBuildFile.getParentPomDeclaration()).isNotEmpty();
        assertThat(openRewriteMavenBuildFile.getParentPomDeclaration().get().getVersion()).isEqualTo("2.5.6");
    }

    @GitHubIssue("https://github.com/spring-projects-experimental/spring-boot-migrator/issues/55")
    @Test
    void parsingPomWithEmptyPropertiesSectionShouldSucceed() {
        String pomXml = """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-parent</artifactId>
                    <version>2.4.12</version>
                    <properties>
                    
                    </properties>
                </project>
                """;

        ProjectContext projectContext = TestProjectContext.buildProjectContext().withMavenRootBuildFileSource(pomXml).build();
        assertThat(projectContext.getApplicationModules().getRootModule().getBuildFile()).isNotNull();
    }
}
