package org.springframework.sbm.engine.precondition;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;
import org.springframework.sbm.engine.git.GitSupport;
import org.springframework.sbm.project.resource.ApplicationProperties;

import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DoesGitDirExistWhenGitSupportEnabledPreconditionCheckTest {

    @Test
    void gitSupportDisabled() {
        ApplicationProperties applicationProperties = new ApplicationProperties();
        applicationProperties.setGitSupportEnabled(false);

        DoesGitDirExistWhenGitSupportEnabledPreconditionCheck sut = new DoesGitDirExistWhenGitSupportEnabledPreconditionCheck(applicationProperties, new GitSupport(applicationProperties));

        Path projectRoot = Path.of("./test-dummy").toAbsolutePath().normalize();
        List<Resource> projectResources = List.of();
        PreconditionCheckResult checkResult = sut.verify(projectRoot, projectResources);

        assertThat(checkResult.getState()).isSameAs(PreconditionCheck.ResultState.PASSED);
    }

}