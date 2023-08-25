/*
 * Copyright 2021 - 2023 the original author or authors.
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
        assertThat(s).isEqualTo(
                "\u001B[1m\n" +
                "\n" +
                "Checked preconditions for '" +projectRoot+ "'\n" +
                "\u001B[32;1m[ok]\u001B[0m passed\n" +
                "\u001B[93;1m [!]\u001B[0m warn\n" +
                "\u001B[91;1m [X]\u001B[0m failed\n" +
                "\n" +
                "\u001B[0m"
        );
    }
}