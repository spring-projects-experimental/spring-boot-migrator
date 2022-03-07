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
package org.springframework.sbm.mule.actions.javadsl.translators;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;

class DslSnippetTest {

    @Test
    public void shouldNotShowDuplicates() {

        List<DslSnippet> input = List.of(
                new DslSnippet("", emptySet(), emptySet(),
                        Set.of(new Bean("b", "bb"))
                ),
                new DslSnippet("", emptySet(), emptySet(),
                        Set.of(new Bean("b", "bb"))
                ));

        String out = DslSnippet.renderMethodParameters(input);

        assertThat(out).isEqualTo("bb b");
    }
}
