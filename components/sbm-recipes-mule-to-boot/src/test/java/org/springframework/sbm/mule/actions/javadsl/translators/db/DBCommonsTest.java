package org.springframework.sbm.mule.actions.javadsl.translators.db;

import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

class DBCommonsTest {

    @Test
    public void extractsQueryPart() {
        QueryParameter output =
                DBCommons.parseQueryParameter(
                        "select * from users where username='#[payload.username]' and password='#[payload.password]'"
                );
        assertThat(output.getQuery()).isEqualTo("select * from users where username=? and password=?");
    }

    @Test
    public void extractsExpressions() {
        QueryParameter output =
                DBCommons.parseQueryParameter(
                        "select * from users where username='#[payload.username]' and password='#[payload.password]'"
                );

        assertThat(output.getMuleExpressions()).hasSize(2);

        assertThat(output.getMuleExpressions().get(0)).isEqualTo("payload.username");
        assertThat(output.getMuleExpressions().get(1)).isEqualTo("payload.password");
    }

    @Test
    public void whenNullInputToParseQueryParameter() {

        QueryParameter output = DBCommons.parseQueryParameter(null);
        assertThat(output.getQuery()).isEmpty();
        assertThat(output.getMuleExpressions()).isEmpty();
    }

    @Test
    public void whenInputToParseQueryParameterIsEmpty() {
        QueryParameter output = DBCommons.parseQueryParameter("");
        assertThat(output.getQuery()).isEmpty();
        assertThat(output.getMuleExpressions()).isEmpty();
    }

    @Test
    public void parseInputQueryParameterPrimitiveDataType() {
        QueryParameter output =
                DBCommons.parseQueryParameter(
                        "select * from users where age=#[payload.age]"
                );

        assertThat(output.getMuleExpressions()).hasSize(1);
        assertThat(output.getMuleExpressions().get(0)).isEqualTo("payload.age");
        assertThat(output.getQuery()).isEqualTo("select * from users where age=?");
    }

    @Test
    public void parseInputWhenQueryParameterDoesNotHaveAnyMuleExpressions() {

        QueryParameter output =
                DBCommons.parseQueryParameter(
                        "select * from users"
                );

        assertThat(output.getQuery()).isEqualTo("select * from users");
        assertThat(output.getMuleExpressions()).isEmpty();
    }
}
