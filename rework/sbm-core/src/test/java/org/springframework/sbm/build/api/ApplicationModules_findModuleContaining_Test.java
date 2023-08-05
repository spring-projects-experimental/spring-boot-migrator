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
package org.springframework.sbm.build.api;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ApplicationModules_findModuleContaining_Test {

    @Test
    void test_renameMe() {
        Module module1 = mock(Module.class);
        Module module2 = mock(Module.class);
        Module module3 = mock(Module.class);
        List<Module> modules = List.of(module1, module2, module3);
        ApplicationModules sut = new ApplicationModules(modules);
        Path resourcePath = Path.of("xyz");

        when(module1.contains(resourcePath)).thenReturn(false);
        when(module2.contains(resourcePath)).thenReturn(false);
        when(module3.contains(resourcePath)).thenReturn(true);

        Optional<Module> module = sut.findModuleContaining(resourcePath);

        Assertions.assertThat(module.get()).isSameAs(module3);
    }

}