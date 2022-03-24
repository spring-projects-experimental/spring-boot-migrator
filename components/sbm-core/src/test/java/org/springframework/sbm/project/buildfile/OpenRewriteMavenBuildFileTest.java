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
package org.springframework.sbm.project.buildfile;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.sbm.build.api.BuildFile;
import org.springframework.sbm.build.api.DependenciesChangedEvent;
import org.springframework.sbm.build.api.Dependency;
import org.springframework.sbm.build.api.Plugin;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class OpenRewriteMavenBuildFileTest {

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

    @Test
    @Tag("integration")
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
//                        "            <type>pom</type>\n" + // FIXME: #7
                        "        </dependency>\n" +
                        "    </dependencies>\n" +
                        "</project>";

        BuildFile sut = TestProjectContext.buildProjectContext()
                .withMavenRootBuildFileSource(pomXml)
                .build()
                .getBuildFile();

        List<String> actualPaths = sut.getResolvedDependenciesPaths().stream()
                .map(dp -> dp.toString().substring(dp.toString().lastIndexOf("repository/") + "repository/".length())) // strip of path to Maven repository
                .collect(Collectors.toList());

        assertThat(actualPaths).containsExactlyInAnyOrder(
                "org/apache/tomee/mbean-annotation-api/8.0.5/mbean-annotation-api-8.0.5.jar",
				"org/apache/tomee/openejb-jpa-integration/8.0.5/openejb-jpa-integration-8.0.5.jar",
				"org/apache/tomee/javaee-api/8.0-5/javaee-api-8.0-5.jar",
				"org/apache/commons/commons-lang3/3.11/commons-lang3-3.11.jar",
				"org/apache/tomee/openejb-api/8.0.5/openejb-api-8.0.5.jar",
				"org/apache/tomee/openejb-loader/8.0.5/openejb-loader-8.0.5.jar",
				"org/apache/tomee/openejb-javaagent/8.0.5/openejb-javaagent-8.0.5.jar",
				"org/apache/tomee/openejb-jee/8.0.5/openejb-jee-8.0.5.jar",
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
				"org/jboss/logging/jboss-logging/3.3.2.Final/jboss-logging-3.3.2.Final.jar",
				"net/sf/ehcache/ehcache/2.10.3/ehcache-2.10.3.jar",
				"org/slf4j/slf4j-jdk14/1.7.21/slf4j-jdk14-1.7.21.jar"
        );

        assertThat(actualPaths).hasSize(75);
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

        assertThat(sut.getDependencyManagement()).hasSize(0);
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

        assertThat(sut.getDependencyManagement()).hasSize(0);
        assertThat(sut.getDeclaredDependencies()).hasSize(1);
        assertThat(sut.getDeclaredDependencies()).contains(dependency);
        ArgumentCaptor<DependenciesChangedEvent> argumentCaptor = ArgumentCaptor.forClass(DependenciesChangedEvent.class);
        verify(eventPublisher, times(46)).publishEvent(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().getResolvedDependencies()).hasSize(1);
        assertThat(argumentCaptor.getValue().getResolvedDependencies().get(0).toString()).endsWith("org/apiguardian/apiguardian-api/1.1.0/apiguardian-api-1.1.0.jar");
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
                        + "  xmlns=\"http://maven.apache.org/POM/4.0.0\"\n"
                        + "  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n"
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

        assertThat(sut.getDependencyManagement()).hasSize(0);
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

        assertThat(sut.getDependencyManagement()).hasSize(0);
        assertThat(sut.getDeclaredDependencies()).hasSize(0);
    }

    @Test
    @Disabled("#7")
    void testGetDependencies() {
        String pomSource =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>com.example</groupId>\n" +
                        "    <artifactId>some-example</artifactId>\n" +
                        "    <packaging>jar</packaging>\n" +
                        "    <version>8.0.5-SNAPSHOT</version>\n" +
                        "    <dependencyManagement>\n" +
                        "        <dependencies>\n" +
                        "           <dependency>\n" +
                        "                <groupId>org.junit.jupiter</groupId>\n" +
                        "                <artifactId>junit-jupiter</artifactId>\n" +
                        "                <version>5.7.1</version>\n" +
                        "                <scope>test</scope>\n" +
                        "            </dependency>\n" +
                        "        </dependencies>\n" +
                        "    </dependencyManagement>\n" +
                        "    <dependencies>\n" +
                        "        <dependency>\n" +
                        "            <groupId>org.junit.jupiter</groupId>\n" +
                        "            <artifactId>junit-jupiter</artifactId>\n" +
                        "        </dependency>\n" +
                        "        <dependency>\n" +
                        "            <groupId>org.junit.jupiter</groupId>\n" +
                        "            <artifactId>junit-jupiter-api</artifactId>\n" +
                        "            <version>5.6.3</version>\n" +
                        "            <scope>test</scope>\n" +
                        "        </dependency>\n" +
                        "        <dependency>\n" +
                        "            <groupId>org.mockito</groupId>\n" +
                        "            <artifactId>mockito-core</artifactId>\n" +
                        "            <version>3.7.7</version>\n" +
                        "            <scope>test</scope>\n" +
                        "        </dependency>\n" +
                        "        <dependency>\n" +
                        "            <groupId>org.apache.tomee</groupId>\n" +
                        "            <artifactId>openejb-core-hibernate</artifactId>\n" +
                        "            <version>8.0.5</version>\n" +
//                        "            <type>pom</type>\n" + // FIXME: #7
                        "        </dependency>\n" +
                        "    </dependencies>\n" +
                        "</project>\n";
        ProjectContext build = TestProjectContext.buildProjectContext()
                .withMavenRootBuildFileSource(pomSource)
                .build();

        List<Dependency> dependencies = build.getBuildFile().getDeclaredDependencies();

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
//                        "            <type>pom</type>\n" + // FIXME: #7
                        "        </dependency>\n" +
                        "    </dependencies>\n" +
                        "</project>";

        BuildFile sut = TestProjectContext.buildProjectContext()
                .withMavenRootBuildFileSource(pomXml)
                .build()
                .getBuildFile();
        assertThat(sut.getDependencyManagement()).hasSize(0);
        assertThat(sut.getDeclaredDependencies()).hasSize(2);

        sut.removeDependencies(List.of(
                Dependency.builder()
                        .groupId("org.apache.tomee")
                        .artifactId("openejb-core-hibernate")
                        .type("pom")
                        .build())
        );

        assertThat(sut.getDependencyManagement()).hasSize(0);
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
                        "    <name>foobat</name>\n" +
                        "\n" +
                        "    <dependencies>\n" +
                        "        <dependency>\n" +
                        "            <groupId>org.apache.tomee</groupId>\n" +
                        "            <artifactId>openejb-core-hibernate</artifactId>\n" +
                        "            <version>8.0.5</version>\n" +
//                        "            <type>pom</type>\n" + // FIXME: #7
                        "        </dependency>\n" +
                        "    </dependencies>\n" +
                        "</project>";

        BuildFile sut = TestProjectContext.buildProjectContext().withMavenRootBuildFileSource(pomXml).build().getBuildFile();

        sut.removeDependencies(List.of(
                Dependency.builder()
                        .groupId("org.apache.tomee")
                        .artifactId("openejb-core-hibernate")
                        .type("pom") //TODO: OR remove dependency doesn't care about this attribute (ignores it, but it is meaningful here).
                        .build())
        );

        assertThat(sut.getDependencyManagement()).hasSize(0);
        assertThat(sut.getDeclaredDependencies()).hasSize(0);

        assertThat(sut.print()).isEqualTo(
                "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                        "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "    xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>foo</groupId>\n" +
                        "    <artifactId>bar</artifactId>\n" +
                        "    <version>0.0.1-SNAPSHOT</version>\n" +
                        "    <name>foobat</name>\n" +
                        "</project>");
    }

    @Test
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
                        "  xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                        "  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
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
                // FIXME: onlyIfUsed temporary deactivated by sing AlwaysAddDependency
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
        verify(eventPublisher, times(48)).publishEvent(argumentCaptor.capture()); //  // publish event called for parsed JavaSource too
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

        Plugin plugin = new Plugin();
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

        Plugin plugin = new Plugin();
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

        Plugin plugin = new Plugin();
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

        Plugin plugin = new Plugin();
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
                "<?xml  version=\"1.0\" encoding=\"UTF-8\"?>\n" +
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

        Plugin plugin = Plugin.builder()
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
        assertThat(plugins.get(0).getConfiguration()).isEmpty();

        assertThat(plugins.get(1).getGroupId()).isEqualTo("com.mulesoft.munit.tools");
        assertThat(plugins.get(1).getArtifactId()).isEqualTo("munit-maven-plugin");
        assertThat(plugins.get(1).getVersion()).isEqualTo("${munit.version}");
        assertThat(plugins.get(1).getConfiguration()).isEmpty();
        assertThat(plugins.get(1).getExecutions()).isEmpty();
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

        openRewriteMavenBuildFile.upgradeParentVersion("2.5.6");

        assertThat(openRewriteMavenBuildFile.getParentPomDeclaration()).isNotEmpty();
        assertThat(openRewriteMavenBuildFile.getParentPomDeclaration().get().getVersion()).isEqualTo("2.5.6");
    }
}
