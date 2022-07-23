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
package org.springframework.sbm.shell;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.Utils;
import org.springframework.util.ReflectionUtils;

import static org.assertj.core.api.Assertions.assertThat;

class ScanValueProviderTest {

    @Test
    void completeWithPartialInputShouldOnlyProposeMatchingPrefix() {

        // Build up arguments to complete method call
        Method scan = ReflectionUtils.findMethod(ScanShellCommand.class, "scan", String.class);
        MethodParameter methodParameter = Utils.createMethodParameter(scan, 0);
        CompletionContext completionContext = new CompletionContext(Arrays.asList("scan", "sr"), 1, 2);

        // Create value provider instance
        ScanValueProvider valueProvider = new ScanValueProvider();
        assertThat(valueProvider.supports(methodParameter, completionContext)).isTrue();

        // Invoke to get proposals
        List<CompletionProposal> proposals = valueProvider.complete(methodParameter, completionContext, new String[0]);

        // Validate returned proposals
        assertThat(proposals).extracting("value", String.class).containsOnly("src");

    }

}