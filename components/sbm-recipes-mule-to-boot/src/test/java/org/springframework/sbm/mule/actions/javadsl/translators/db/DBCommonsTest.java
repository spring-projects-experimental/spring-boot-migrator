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

import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

class DBCommonsTest {

    @Test
    public void extractsQueryPart() {
        QueryWithParameters output =
                DBCommons.parseQueryParameter(
                        "select * from users where username='#[payload.username]' and password='#[payload.password]'"
                );
        assertThat(output.getQuery()).isEqualTo("select * from users where username=? and password=?");
    }

    @Test
    public void extractsExpressions() {
        QueryWithParameters output =
                DBCommons.parseQueryParameter(
                        "select * from users where username='#[payload.username]' and password='#[payload.password]'"
                );

        assertThat(output.getMuleExpressions()).hasSize(2);

        assertThat(output.getMuleExpressions().get(0)).isEqualTo("payload.username");
        assertThat(output.getMuleExpressions().get(1)).isEqualTo("payload.password");
    }

    @Test
    public void whenNullInputToParseQueryParameter() {

        QueryWithParameters output = DBCommons.parseQueryParameter(null);
        assertThat(output.getQuery()).isEmpty();
        assertThat(output.getMuleExpressions()).isEmpty();
    }

    @Test
    public void whenInputToParseQueryParameterIsEmpty() {
        QueryWithParameters output = DBCommons.parseQueryParameter("");
        assertThat(output.getQuery()).isEmpty();
        assertThat(output.getMuleExpressions()).isEmpty();
    }

    @Test
    public void parseInputQueryParameterPrimitiveDataType() {
        QueryWithParameters output =
                DBCommons.parseQueryParameter(
                        "select * from users where age=#[payload.age]"
                );

        assertThat(output.getMuleExpressions()).hasSize(1);
        assertThat(output.getMuleExpressions().get(0)).isEqualTo("payload.age");
        assertThat(output.getQuery()).isEqualTo("select * from users where age=?");
    }

    @Test
    public void parseInputWhenQueryParameterDoesNotHaveAnyMuleExpressions() {

        QueryWithParameters output =
                DBCommons.parseQueryParameter(
                        "select * from users"
                );

        assertThat(output.getQuery()).isEqualTo("select * from users");
        assertThat(output.getMuleExpressions()).isEmpty();
    }
}
