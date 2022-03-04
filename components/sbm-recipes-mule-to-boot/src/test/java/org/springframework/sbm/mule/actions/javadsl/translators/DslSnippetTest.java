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

        String out = DslSnippet.getMethodParameters(input);

        assertThat(out).isEqualTo("bb b");
    }
}
