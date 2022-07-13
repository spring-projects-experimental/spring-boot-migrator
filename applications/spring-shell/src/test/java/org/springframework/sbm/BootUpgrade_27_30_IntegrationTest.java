package org.springframework.sbm;

import org.junit.jupiter.api.Test;
import org.springframework.sbm.IntegrationTestBaseClass;

import java.nio.file.Path;

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
        System.out.println(pomContent);
    }
}
