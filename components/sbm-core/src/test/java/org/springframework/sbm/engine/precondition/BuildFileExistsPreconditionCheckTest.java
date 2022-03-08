package org.springframework.sbm.engine.precondition;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BuildFileExistsPreconditionCheckTest {

    @Test
    void shouldReturnErrorMessageIfNoMavenBuildFileExists() {
        MavenBuildFileExistsPreconditionCheck sut = new MavenBuildFileExistsPreconditionCheck();
        Path projectRoot = Path.of(".");
        PreconditionCheckResult checkResult = sut.verify(projectRoot, List.of());
        assertThat(checkResult.getState()).isEqualTo(PreconditionCheck.ResultState.FAILED);
        assertThat(checkResult.getMessage()).isEqualTo("SBM requires a Maven build file. Please provide a minimal pom.xml.");
    }

    @Test
    void shouldReturnSuccessIfMavenBuildFileExists() throws IOException {
        MavenBuildFileExistsPreconditionCheck sut = new MavenBuildFileExistsPreconditionCheck();
        Resource buildGradle = mock(Resource.class);
        File buildGradleFile = mock(File.class);
        when(buildGradle.getFile()).thenReturn(buildGradleFile);
        when(buildGradleFile.toPath()).thenReturn(Path.of("./foo/pom.xml").toAbsolutePath().normalize());
        PreconditionCheckResult checkResult = sut.verify(Path.of("./foo").toAbsolutePath().normalize(), List.of(buildGradle));
        assertThat(checkResult.getState()).isEqualTo(PreconditionCheck.ResultState.PASSED);
        assertThat(checkResult.getMessage()).isEqualTo("Found pom.xml.");
    }

}