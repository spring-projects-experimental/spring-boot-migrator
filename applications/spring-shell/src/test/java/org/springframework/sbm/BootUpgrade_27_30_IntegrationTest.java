package org.springframework.sbm;

import org.junit.jupiter.api.Test;
import org.openrewrite.maven.MavenParser;
import org.openrewrite.xml.tree.Xml;
import org.springframework.sbm.IntegrationTestBaseClass;

import java.nio.file.Path;
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

        MavenParser mavenParser = new MavenParser.Builder().build();
        List<Xml.Document> mavenAsXMLDocuments = mavenParser.parse(pomContent);
        String version = mavenAsXMLDocuments.get(0)
                .getRoot()
                .getChildren("parent")
                .get(0)
                .getChildValue("version")
                .get();

        assertThat(version).isEqualTo("3.0.0-M3");
    }
}
