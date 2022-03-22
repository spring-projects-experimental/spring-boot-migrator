package org.springframework.sbm.shell;

import org.junit.jupiter.api.Test;
import org.springframework.sbm.engine.precondition.PreconditionCheck;
import org.springframework.sbm.engine.precondition.PreconditionCheckResult;
import org.springframework.sbm.engine.precondition.PreconditionVerificationResult;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class PreconditionVerificationRendererTest {
    @Test
    void renderPreconditionCheckResult() {
        PreconditionVerificationRenderer sut = new PreconditionVerificationRenderer();
        Path projectRoot = Path.of("./foo").toAbsolutePath().normalize();

        PreconditionVerificationResult checkResult = new PreconditionVerificationResult(projectRoot);

        PreconditionCheckResult passedResult = new PreconditionCheckResult(PreconditionCheck.ResultState.PASSED, "passed");
        checkResult.addResult(passedResult);
        PreconditionCheckResult warnResult = new PreconditionCheckResult(PreconditionCheck.ResultState.WARN, "warn");
        checkResult.addResult(warnResult);
        PreconditionCheckResult failedResult = new PreconditionCheckResult(PreconditionCheck.ResultState.FAILED, "failed");
        checkResult.addResult(failedResult);

        String s = sut.renderPreconditionCheckResults(checkResult);
        assertThat(s).isEqualTo("\u001B[30;1m\n" +
                "\n" +
                "Checked preconditions for '/Users/fkrueger/git/spring-boot-migrator-oss/experimental/applications/spring-shell/foo'\n" +
                "\u001B[32;1m[ok]\u001B[0m passed\n" +
                "\u001B[93;1m [!]\u001B[0m warn\n" +
                "\u001B[91;1m [X]\u001B[0m failed\n" +
                "\n" +
                "\u001B[0m");
    }
}