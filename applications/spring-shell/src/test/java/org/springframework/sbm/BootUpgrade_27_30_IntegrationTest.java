package org.springframework.sbm;

import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.openrewrite.maven.MavenParser;
import org.openrewrite.xml.tree.Xml;

import java.nio.file.Path;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class BootUpgrade_27_30_IntegrationTest extends IntegrationTestBaseClass {

    @Override
    protected String getTestSubDir() {
        return "boot-migration-27-30";
    }

    @Test
    void migrateSimpleApplication() {
        intializeTestProject();

        scanProject();

        applyRecipe("boot-2.7-3.0-dependency-version-update");

        verifyParentPomVersion();
        verifyMicrometerPackageUpdate();
        verifyYamlConfigurationUpdate();
        verifyPropertyConfigurationUpdate();
        verifyConstructorBindingRemoval();
        verifyCrudRepoAddition();
    }

    private void verifyCrudRepoAddition() {

        String studentRepo = loadJavaFile("org.springboot.example.upgrade", "StudentRepo");

        assertThat(studentRepo).isEqualTo("""
                package org.springboot.example.upgrade;
                                
                import org.springframework.data.repository.CrudRepository;
                import org.springframework.data.repository.PagingAndSortingRepository;
                                
                public interface StudentRepo extends PagingAndSortingRepository<Student<?>, Long>, CrudRepository<Student<?>, Long> {
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

    private void verifyMicrometerPackageUpdate() {
        String micrometerClass = loadFile(Path.of("src/main/java/org/springboot/example/upgrade/MicrometerConfig.java"));
        assertThat(micrometerClass).isEqualTo(
                "package org.springboot.example.upgrade;\n" +
                "\n" +
                "import io.micrometer.binder.MeterBinder;\n" +
                "\n" +
                "public class MicroMeterConfig {\n" +
                "\n" +
                "    private MeterBinder k;\n" +
                "}\n");
    }

    private void verifyYamlConfigurationUpdate() {
        String micrometerClass = loadFile(Path.of("src/main/resources/application.yaml"));
        assertThat(micrometerClass).isEqualTo(
                        "spring:\n" +
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
                        "  sql.init.username: username-data\n" +
                        "  sql.init.mode: mode1\n" +
                        "  sql.init.platform: pls\n" +
                        "  sql.init.schema-locations: table1\n" +
                        "  sql.init.password: password2\n" +
                        "  sql.init.username: username-schema\n" +
                        "  sql.init.separator: k\n" +
                        "  sql.init.encoding: UTF-8\n" +
                        "server.reactive.session.cookie.same-site: 'true'" +
                        "\n");
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
                        "server.reactive.session.cookie.same-site=true\n");
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
