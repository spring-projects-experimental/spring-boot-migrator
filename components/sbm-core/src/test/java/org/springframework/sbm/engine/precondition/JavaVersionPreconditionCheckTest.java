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
        assertThat(checkResult.getMessage()).isEqualTo("Java 11 or 17 is required. Check found Java 10.");
    }

    @Test
    void supportedJavaVersionShouldPassWithJava11() {
        JavaVersionPreconditionCheck sut = new JavaVersionPreconditionCheck();
        Path projectRoot = Path.of("./test-dummy").toAbsolutePath().normalize();
        List<Resource> resources = List.of();

        System.setProperty("java.specification.version", "11");

        PreconditionCheckResult checkResult = sut.verify(projectRoot, resources);
        assertThat(checkResult.getState()).isEqualTo(PreconditionCheck.ResultState.PASSED);
        assertThat(checkResult.getMessage()).isEqualTo("Required Java version (11) was found.");
    }

    @Test
    void supportedJavaVersionShouldPassWithJava17() {
        JavaVersionPreconditionCheck sut = new JavaVersionPreconditionCheck();
        Path projectRoot = Path.of("./test-dummy").toAbsolutePath().normalize();
        List<Resource> resources = List.of();

        System.setProperty("java.specification.version", "17");

        PreconditionCheckResult checkResult = sut.verify(projectRoot, resources);
        assertThat(checkResult.getState()).isEqualTo(PreconditionCheck.ResultState.PASSED);
        assertThat(checkResult.getMessage()).isEqualTo("Required Java version (17) was found.");
    }

}