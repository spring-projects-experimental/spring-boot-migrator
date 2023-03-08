package org.springframework.sbm;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.DefaultApplicationArguments;

import static org.assertj.core.api.Assertions.assertThat;

public class ReportTestSingleModule extends IntegrationTestBaseClass {

    @Autowired
    private SpringBootMigratorRunner springBootMigratorRunner;

    @Autowired
    private ReportHolder reportHolder;

    @Override
    protected String getTestSubDir() {
        return "boot-migration-27-30";
    }


    @Test
    public void generatesAllRelevantSection() {
        intializeTestProject();

        springBootMigratorRunner.run(new DefaultApplicationArguments(getTestDir().toString()));

        assertThat(reportHolder.getReport()).contains("Constructor Binding");
        assertThat(reportHolder.getReport()).contains("Johnzon Dependency Upgrade");
    }
}
