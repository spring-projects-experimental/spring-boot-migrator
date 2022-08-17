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

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class BootUpgrade_27_30_IntegrationTest extends IntegrationTestBaseClass {

    @Override
    protected String getTestSubDir() {
        return "boot-migration-27-30";
    }

    @Test
    @Tag("integration")
    void migrateSimpleApplication() {
        intializeTestProject();

        scanProject();

        applyRecipe("boot-2.7-3.0-dependency-version-update");

        buildProject();
        verifyParentPomVersion();
        verifyYamlConfigurationUpdate();
        verifyPropertyConfigurationUpdate();
        verifyConstructorBindingRemoval();
        verifyCrudRepoAddition();
        verifyAutoConfigurationIsRefactored();
        verifyEhCacheVersionIsUpgraded();
    }

    private void buildProject() {
        executeMavenGoals(getTestDir(), "clean", "verify");
    }

    private void verifyEhCacheVersionIsUpgraded() {
        String pomContent = loadFile(Path.of("pom.xml"));

        Xml.Document mavenAsXMLDocument = parsePom(pomContent);

        List<Dependency> dependencies = mavenAsXMLDocument
                .getMarkers()
                .findFirst(MavenResolutionResult.class)
                .get()
                .getPom()
                .getRequestedDependencies();

        Optional<Dependency> ehcacheResult = dependencies
                .stream()
                .filter(dependency -> dependency.getArtifactId().equals("ehcache"))
                .findFirst();

        assertThat(ehcacheResult).isPresent();

        Dependency ehcacheDependency = ehcacheResult.get();

        assertThat(ehcacheDependency.getArtifactId()).isEqualTo("ehcache");
        assertThat(ehcacheDependency.getGav().getGroupId()).isEqualTo("org.ehcache");
        assertThat(ehcacheDependency.getGav().getVersion()).isNull();
        assertThat(ehcacheDependency.getClassifier()).isEqualTo("jakarta");
    }

    private void verifyAutoConfigurationIsRefactored() {
        String autoConfig = loadFile(Path.of("src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports"));
        assertThat(autoConfig).isEqualTo("com.hello.GreetingConfig");
    }

    private void verifyCrudRepoAddition() {

        String studentRepo = loadJavaFile("org.springboot.example.upgrade", "StudentRepoPagingAndSorting");

        assertThat(studentRepo).isEqualTo("""
                package org.springboot.example.upgrade;
                                
                import org.springframework.data.repository.CrudRepository;
                import org.springframework.data.repository.PagingAndSortingRepository;
                                
                public interface StudentRepoPagingAndSorting extends PagingAndSortingRepository<Student, Long>, CrudRepository<Student, Long> {
                }
                """);

        String studentRepoReactive = loadJavaFile("org.springboot.example.upgrade", "StudentRepoReactiveSorting");

        assertThat(studentRepoReactive).isEqualTo("""
                package org.springboot.example.upgrade;
                                
                import org.springframework.data.repository.reactive.ReactiveCrudRepository;
                import org.springframework.data.repository.reactive.ReactiveSortingRepository;
                                
                public interface StudentRepoReactiveSorting extends ReactiveSortingRepository<Student, Long>, ReactiveCrudRepository<Student, Long> {
                }
                """);

        String studentRepoRx= loadJavaFile("org.springboot.example.upgrade", "StudentRepoRxJava3Sorting");

        assertThat(studentRepoRx).isEqualTo("""
                package org.springboot.example.upgrade;
                                
                import org.springframework.data.repository.reactive.RxJava3CrudRepository;
                import org.springframework.data.repository.reactive.RxJava3SortingRepository;
                                
                public interface StudentRepoRxJava3Sorting extends RxJava3SortingRepository<Student, Long>, RxJava3CrudRepository<Student, Long> {
                }
                """);
    }

    private void verifyConstructorBindingRemoval() {
        String constructorBindingConfigClass = loadJavaFile("org.springboot.example.upgrade", "ConstructorBindingConfig");
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
        String micrometerClass = loadFile(Path.of("src/main/resources/application.yaml"));
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
                                "  security:\n" +
                                "    saml2:\n" +
                                "      relyingparty:\n" +
                                "        registration:\n" +
                                "          idpone:\n" +
                                "            assertingparty:\n" +
                                "              verification:\n" +
                                "                credentials:\n" +
                                "                  certificate-location: classpath:saml/idpone.crt\n" +
                                "              entity-id: https://idpone.com\n" +
                                "              sso-url: https://idpone.com\n" +
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
                                "server.reactive.session.cookie.same-site: 'true'\n");
    }


    private void verifyPropertyConfigurationUpdate() {

        String applicationProperties = loadFile(Path.of("src/main/resources/application.properties"));
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
                                "spring.security.saml2.relyingparty.registration.idpone.assertingparty.entity-id=https://idpone.com\n" +
                                "spring.security.saml2.relyingparty.registration.idpone.assertingparty.sso-url=https://idpone.com\n" +
                                "spring.security.saml2.relyingparty.registration.idpone.assertingparty.verification.credentials.certificate-location=classpath:saml/idpone.crt\n" +
                                "\n" +
                                "server.reactive.session.cookie.same-site=true\n" +
                                "\n" +
                                "spring.datasource.url=jdbc:h2:mem:testdb\n" +
                                "spring.datasource.driverClassName=org.h2.Driver\n" +
                                "spring.datasource.username=sa\n" +
                                "spring.datasource.password=password\n" +
                                "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect\n");
    }

    private void verifyParentPomVersion() {
        String pomContent = loadFile(Path.of("pom.xml"));

        Xml.Document mavenAsXMLDocument = parsePom(pomContent);

        Xml.Tag parentTag =mavenAsXMLDocument
                .getRoot()
                .getChildren("parent").get(0);

        String version = parentTag.getChildValue("version").get();

        String groupId = parentTag.getChildValue("groupId").get();
        String artifactId = parentTag.getChildValue("artifactId").get();

        assertThat(version).isEqualTo("3.0.0-M3");
        assertThat(groupId).isEqualTo("org.springframework.boot");
        assertThat(artifactId).isEqualTo("spring-boot-starter-parent");
    }

    @NotNull
    private Xml.Document parsePom(String pomContent) {
        MavenParser mavenParser = new MavenParser.Builder().build();
        return mavenParser.parse(pomContent).get(0);
    }
}
