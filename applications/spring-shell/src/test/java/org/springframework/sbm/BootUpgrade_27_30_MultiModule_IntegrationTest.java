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
package org.springframework.sbm;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openrewrite.maven.MavenParser;
import org.openrewrite.maven.tree.Dependency;
import org.openrewrite.maven.tree.MavenResolutionResult;
import org.openrewrite.xml.tree.Xml;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class BootUpgrade_27_30_MultiModule_IntegrationTest  extends IntegrationTestBaseClass {
    @Override
    protected String getTestSubDir() {
        return "boot-migration-27-30-multi-module";
    }

    @Test
    @Tag("integration")
    void migrateMultiModuleApplication() {
        intializeTestProject();

        scanProject();

        applyRecipe("sbu30-set-java-version");
        applyRecipe("sbu30-upgrade-dependencies");
        applyRecipe("sbu30-johnzon-dependency-update");
        applyRecipe("sbu30-upgrade-spring-cloud-dependency");
        applyRecipe("sbu30-upgrade-boot-version");
        applyRecipe("sbu30-migrate-to-jakarta-packages");
        applyRecipe("sbu30-remove-construtor-binding");
        applyRecipe("sbu30-auto-configuration");
        applyRecipe("sbu30-225-logging-date-format");
        applyRecipe("sbu30-migrate-spring-data-properties");
        applyRecipe("sbu30-paging-and-sorting-repository");

        buildProject();

        verifyParentPomVersion();
        verifyYamlConfigurationUpdate();
        verifyPropertyConfigurationUpdate();
        verifyConstructorBindingRemoval();
        verifyEhCacheVersionIsUpgraded();
    }

    private void verifyConstructorBindingRemoval() {
        String constructorBindingConfigClass = loadJavaFileFromSubmodule("spring-app/", "org.springboot.example.upgrade", "ConstructorBindingConfig");
        assertThat(constructorBindingConfigClass).isEqualTo("package org.springboot.example.upgrade;\n" +
                "\n" +
                "import org.springframework.boot.context.properties.ConfigurationProperties;\n" +
                "\n" +
                "@ConfigurationProperties(prefix = \"mail\")\n" +
                "public class ConstructorBindingConfig {\n" +
                "    private String hostName;\n" +
                "\n" +
                "    public ConstructorBindingConfig(String hostName) {\n" +
                "        this.hostName = hostName;\n" +
                "    }\n" +
                "}" +
                "\n");
    }

    private void verifyYamlConfigurationUpdate() {
        String micrometerClass = loadFile(Path.of("spring-app/src/main/resources/application.yaml"));
        assertThat(micrometerClass).isEqualTo(
                "spring:\n" +
                        "  datasource:\n" +
                        "    url: jdbc:h2:mem:testdb\n" +
                        "    driverClassName: org.h2.Driver\n" +
                        "  jpa:\n" +
                        "    database-platform: org.hibernate.dialect.H2Dialect\n" +
                        "  elasticsearch:\n" +
                        "    connection-timeout: '1'\n" +
                        "    password: testpassword\n" +
                        "    socket-timeout: '2'\n" +
                        "    restclient.sniffer.delay-after-failure: '3'\n" +
                        "    restclient.sniffer.interval: '4'\n" +
                        "    username: username\n" +
                        "  elasticsearch.connection-timeout: '1000'\n" +
                        "  elasticsearch.webclient.max-in-memory-size: '122'\n" +
                        "  elasticsearch.password: abc\n" +
                        "  elasticsearch.socket-timeout: '100'\n" +
                        "  elasticsearch.username: testUser\n" +
                        "  sql.init.data-locations: testdata\n" +
                        "  sql.init.password: password\n" +
                        "  sql.init.username: sa\n" +
                        "  sql.init.mode: mode1\n" +
                        "  sql.init.platform: pls\n" +
                        "  sql.init.schema-locations: table1\n" +
                        "  sql.init.password: password\n" +
                        "  sql.init.username: sa\n" +
                        "  sql.init.separator: k\n" +
                        "  sql.init.encoding: UTF-8\n" +
                        "  cassandra:\n" +
                        "    keyspaceName: testKeySpace\n" +
                        "    contactPoints: localhost\n" +
                        "    port: 9042\n" +
                        "    username: testusername\n" +
                        "    schemaAction: NONE\n" +
                        "    request:\n" +
                        "      timeout: 10s\n" +
                        "    connection:\n" +
                        "      connectTimeout: 10s\n" +
                        "      initQueryTimeout: 10s\n" +
                        "server.reactive.session.cookie.same-site: 'true'\n");
    }

    private void verifyPropertyConfigurationUpdate() {
        String applicationProperties = loadFile(Path.of("spring-app/src/main/resources/application.properties"));
        assertThat(applicationProperties).isEqualTo(
                "spring.elasticsearch.connection-timeout=1000\n" +
                        "spring.elasticsearch.webclient.max-in-memory-size=122\n" +
                        "spring.elasticsearch.password=abc\n" +
                        "spring.elasticsearch.socket-timeout=100\n" +
                        "spring.elasticsearch.username=testUser\n" +
                        "\n" +
                        "spring.sql.init.data-locations=testdata\n" +
                        "spring.sql.init.password=password\n" +
                        "spring.sql.init.username=username\n" +
                        "spring.sql.init.mode=mode1\n" +
                        "spring.sql.init.platform=pls\n" +
                        "spring.sql.init.schema-locations=table1\n" +
                        "spring.sql.init.password=password2\n" +
                        "spring.sql.init.username=username2\n" +
                        "spring.sql.init.separator=k\n" +
                        "spring.sql.init.encoding=UTF-8\n" +
                        "\n" +
                        "spring.elasticsearch.connection-timeout=1\n" +
                        "spring.elasticsearch.password=testpassword\n" +
                        "spring.elasticsearch.socket-timeout=2\n" +
                        "spring.elasticsearch.restclient.sniffer.delay-after-failure=3\n" +
                        "spring.elasticsearch.restclient.sniffer.interval=4\n" +
                        "spring.elasticsearch.username=username\n" +
                        "\n" +
                        "server.reactive.session.cookie.same-site=true\n" +
                        "\n" +
                        "spring.datasource.url=jdbc:h2:mem:testdb\n" +
                        "spring.datasource.driverClassName=org.h2.Driver\n" +
                        "spring.datasource.username=sa\n" +
                        "spring.datasource.password=password\n" +
                        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect\n" +
                        "\n" +
                        "spring.data.cassandra.keyspace-name=testKeySpace\n" +
                        "spring.data.cassandra.port=9042\n" +
                        "spring.data.cassandra.contact-points=localhost\n" +
                        "spring.data.cassandra.username=testusername\n" +
                        "spring.data.cassandra.schema-action=NONE\n" +
                        "spring.data.cassandra.request.timeout=10s\n" +
                        "spring.data.cassandra.connection.connect-timeout=10s\n" +
                        "spring.data.cassandra.connection.init-query-timeout=10s\n" +
                        "logging.pattern.dateformat=yyyy-MM-dd HH:mm:ss.SSS\n");
    }

    private void verifyEhCacheVersionIsUpgraded() {
        Optional<Dependency> ehcacheResult = getDependencyByArtifactId("ehcache", "spring-app/");

        assertThat(ehcacheResult).isPresent();

        Dependency ehcacheDependency = ehcacheResult.get();

        assertThat(ehcacheDependency.getArtifactId()).isEqualTo("ehcache");
        assertThat(ehcacheDependency.getGav().getGroupId()).isEqualTo("org.ehcache");
        assertThat(ehcacheDependency.getClassifier()).isEqualTo("jakarta");
    }

    private Optional<Dependency> getDependencyByArtifactId(String artifactId, String module) {
        Xml.Document mavenAsXMLDocument = getBuildFileByModule(module);
        List<Dependency> dependencies = getDependencies(mavenAsXMLDocument);
        return dependencies
                .stream()
                .filter(dependency -> dependency.getArtifactId().equals(artifactId))
                .findFirst();
    }

    private List<Dependency> getDependencies(Xml.Document mavenAsXMLDocument) {
        return mavenAsXMLDocument
                .getMarkers()
                .findFirst(MavenResolutionResult.class)
                .get()
                .getPom()
                .getRequestedDependencies();
    }

    private void verifyParentPomVersion() {
        Xml.Document mavenAsXMLDocument = getRootBuildFile();

        Xml.Tag parentTag = mavenAsXMLDocument
                .getRoot()
                .getChildren("parent").get(0);

        String version = parentTag.getChildValue("version").get();

        String groupId = parentTag.getChildValue("groupId").get();
        String artifactId = parentTag.getChildValue("artifactId").get();

        assertThat(version).isEqualTo("3.0.0");
        assertThat(groupId).isEqualTo("org.springframework.boot");
        assertThat(artifactId).isEqualTo("spring-boot-starter-parent");
    }

    @NotNull
    private Xml.Document getRootBuildFile() {
        return parsePom(loadFile(Path.of("pom.xml")));
    }

    @NotNull
    private Xml.Document getBuildFileByModule(String app) {

        return parseSubmodulePom(loadFile(Path.of("pom.xml")), loadFile(Path.of(app + "pom.xml")));
    }


    @NotNull
    private Xml.Document parsePom(String pomContent) {
        MavenParser mavenParser = new MavenParser.Builder().build();
        return mavenParser.parse(pomContent).get(0);
    }

    @NotNull
    private Xml.Document parseSubmodulePom(String parentPom, String pomContent) {
        MavenParser mavenParser = new MavenParser.Builder().build();
        return mavenParser.parse(parentPom, pomContent).get(1);
    }

    private void buildProject() {
        executeMavenGoals(getTestDir(), "clean", "verify");
    }
}
