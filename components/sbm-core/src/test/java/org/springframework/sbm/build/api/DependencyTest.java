package org.springframework.sbm.build.api;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class DependencyTest {

    @Test
    public void handlesErroneousCoordinateInput() {

        Dependency out = Dependency.fromCoordinates("helloWorld");

        assertThat(out).isNull();
    }
}
