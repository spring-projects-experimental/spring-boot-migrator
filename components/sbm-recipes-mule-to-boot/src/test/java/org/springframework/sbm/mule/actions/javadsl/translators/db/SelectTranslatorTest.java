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
        DslSnippet output = target.translate(1, input, null, null, null, null);

        assertThat(output.getRenderedSnippet()).isEqualTo(
                        "// TODO: substitute expression language with appropriate java code \n" +
                                "// TODO: use appropriate translation for pagination for more information visit: https://bit.ly/3xlqByv \n" +
                                "// TODO: The datatype might not be LinkedMultiValueMap please substitute the right type for payload\n" +
                                ".<LinkedMultiValueMap<String, String>>handle((p, h) ->\n" +
                                "                        jdbcTemplate.queryForList(\n" +
                                "                                \"Select * from Students where name like \\\"Sandeep\\\"\"))");
    }
}
