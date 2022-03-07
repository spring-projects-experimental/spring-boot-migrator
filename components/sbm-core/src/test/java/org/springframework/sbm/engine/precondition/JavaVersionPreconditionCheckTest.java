package org.springframework.sbm.engine.precondition;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;

import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JavaVersionPreconditionCheckTest {

    @Test
    void unsupportedJavaVersionShouldTriggerWarning() {
        JavaVersionPreconditionCheck sut = new JavaVersionPreconditionCheck();
        Path projectRoot = Path.of("./test-dummy").toAbsolutePath().normalize();
        List<Resource> resources = List.of();

        System.setProperty("java.specification.version", "10");

        PreconditionCheckResult checkResult = sut.verify(projectRoot, resources);
        assertThat(checkResult.getState()).isEqualTo(PreconditionCheck.ResultState.WARN);
        assertThat(checkResult.getMessage()).isEqualTo("Java 11 is required. Check found Java 10.");
    }

    @Test
    void supportedJavaVersionShouldPass() {
        JavaVersionPreconditionCheck sut = new JavaVersionPreconditionCheck();
        Path projectRoot = Path.of("./test-dummy").toAbsolutePath().normalize();
        List<Resource> resources = List.of();

        System.setProperty("java.specification.version", "11");

        PreconditionCheckResult checkResult = sut.verify(projectRoot, resources);
        assertThat(checkResult.getState()).isEqualTo(PreconditionCheck.ResultState.PASSED);
        assertThat(checkResult.getMessage()).isEqualTo("Required Java version (11) was found.");
    }

}