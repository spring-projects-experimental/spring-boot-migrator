package org.springframework.sbm.mule.actions.javadsl.translators.db;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mulesoft.schema.mule.db.SelectMessageProcessorType;
import org.springframework.sbm.mule.actions.javadsl.translators.DslSnippet;

import static org.assertj.core.api.Assertions.assertThat;

class SelectTranslatorTest {

    private final SelectTranslator target = new SelectTranslator();
    private SelectMessageProcessorType input;

    @BeforeEach
    public void setup() {
        input = new SelectMessageProcessorType();
    }

    @Test
    public void itShouldEscapeDoubleQuotes() {

        input.setMaxRows("500");
        input.setDynamicQuery("Select * from Students where name like \"Sandeep\"");
        DslSnippet output = target.translate(input, null, null, null, null);

        assertThat(output.getRenderedSnippet()).isEqualTo("// TODO: substitute expression language with appropriate java code \n " + "               .handle((p, h) -> jdbcTemplate.queryForList(\"Select * from Students where name like \\\"Sandeep\\\" LIMIT 500\"))");
    }

    @Test
    public void itShouldOmitLimitWhenMaxRowsIsNotAvailable() {
        input.setDynamicQuery("Select * from Students where name like \"Sandeep\"");
        DslSnippet output = target.translate(input, null, null, null, null);

        assertThat(output.getRenderedSnippet()).isEqualTo("// TODO: substitute expression language with appropriate java code \n " + "               .handle((p, h) -> jdbcTemplate.queryForList(\"Select * from Students where name like \\\"Sandeep\\\"\"))");
    }
}
