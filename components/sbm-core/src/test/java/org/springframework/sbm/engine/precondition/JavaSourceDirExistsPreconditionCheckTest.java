/*
 * Copyright 2021 - 2022 the original author or authors.
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

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class JavaSourceDirExistsPreconditionCheckTest {
    @Test
    void shouldReturnFailedMessageWhenJavaSourceDirNotExists() throws IOException {
        JavaSourceDirExistsPreconditionCheck sut = new JavaSourceDirExistsPreconditionCheck();
        Resource r1 = mock(Resource.class);
        File f1 = mock(File.class);
        when(f1.toPath()).thenReturn(Path.of("src/main/resources"));
        when(r1.getFile()).thenReturn(f1);
        List<Resource> resources = List.of(r1);
        PreconditionCheckResult checkResult = sut.verify(Path.of("."), resources);
        assertThat(checkResult.getState()).isEqualTo(PreconditionCheck.ResultState.FAILED);
        assertThat(checkResult.getMessage()).isEqualTo("PreconditionCheck check could not find a 'src/main/java' dir. This dir is required.");
        verify(r1).getFile();
        verify(f1).toPath();
    }

    @Test
    void shouldReturnSuccessMessageWhenJavaSourceDirExists() throws IOException {
        JavaSourceDirExistsPreconditionCheck sut = new JavaSourceDirExistsPreconditionCheck();
        Resource r1 = mock(Resource.class);
        File f1 = mock(File.class);
        when(f1.toPath()).thenReturn(Path.of("src/main/java"));
        when(r1.getFile()).thenReturn(f1);
        List<Resource> resources = List.of(r1);
        PreconditionCheckResult checkResult = sut.verify(Path.of("."), resources);
        assertThat(checkResult.getState()).isEqualTo(PreconditionCheck.ResultState.PASSED);
        assertThat(checkResult.getMessage()).isEqualTo("Found required source dir 'src/main/java'.");
        verify(r1).getFile();
        verify(f1).toPath();
    }

    @Test
    void shouldReturnSuccessMessageWhenJavaSourceDirExistsInChildModule() throws IOException {
        JavaSourceDirExistsPreconditionCheck sut = new JavaSourceDirExistsPreconditionCheck();
        Resource r1 = mock(Resource.class);
        File f1 = mock(File.class);
        when(f1.toPath()).thenReturn(Path.of("module1/src/main/java").toAbsolutePath());
        when(r1.getFile()).thenReturn(f1);
        List<Resource> resources = List.of(r1);
        PreconditionCheckResult checkResult = sut.verify(Path.of(".").toAbsolutePath(), resources);
        assertThat(checkResult.getState()).isEqualTo(PreconditionCheck.ResultState.PASSED);
        assertThat(checkResult.getMessage()).isEqualTo("Found required source dir 'src/main/java'.");
        verify(r1).getFile();
        verify(f1).toPath();
    }

}