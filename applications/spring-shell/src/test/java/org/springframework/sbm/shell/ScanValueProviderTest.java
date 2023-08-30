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

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;

import static org.assertj.core.api.Assertions.assertThat;

class ScanValueProviderTest {

    @Test
    void completeWithPartialInputShouldOnlyProposeMatchingPrefix() {

        // Build up arguments to complete method call
        CompletionContext completionContext = new CompletionContext(Arrays.asList("scan", "sr"), 1, 2, null, null);

        // Create value provider instance
        ScanValueProvider valueProvider = new ScanValueProvider();

        // Invoke to get proposals
        List<CompletionProposal> proposals = valueProvider.complete(completionContext);

        // Validate returned proposals
        assertThat(proposals).extracting("value", String.class).containsOnly("src");

    }

}