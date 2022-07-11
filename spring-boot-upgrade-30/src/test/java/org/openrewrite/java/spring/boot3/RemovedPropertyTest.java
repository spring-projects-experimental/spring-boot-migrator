package org.openrewrite.java.spring.boot3;

import org.junit.jupiter.api.Test;
import org.openrewrite.Result;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class RemovedPropertyTest extends YAMLRecipeTest {

    @Override
    String getRecipeName() {
        return "org.openrewrite.java.spring.boot3.SpringBootPropertiesManual_2_7_Removed";
    }

    @Test
    public void remove() {

        List<Result> result = runRecipe("""
                some:
                  other: "prop"
                management:
                  endpoint:
                    jolokia:
                      config: "abc"
                """.stripIndent());

        assertThat(result).hasSize(1);

        assertThat(result.get(0).getAfter().printAll()).isEqualTo("""
                some:
                  other: "prop"
                """.stripIndent());
    }
}
