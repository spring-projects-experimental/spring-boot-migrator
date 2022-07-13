package org.springframework.sbm;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.openrewrite.maven.MavenParser;
import org.openrewrite.xml.tree.Xml;
import org.springframework.sbm.IntegrationTestBaseClass;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

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

    @NotNull
    private Xml.Document parsePom(String pomContent) {
        MavenParser mavenParser = new MavenParser.Builder().build();
        return mavenParser.parse(pomContent).get(0);
    }
}
