package org.springframework.sbm.shell;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ScanCommandHeaderRendererTest {

    @Test
    void renderHeader() {
		ScanCommandHeaderRenderer sut = new ScanCommandHeaderRenderer();
		String s = sut.renderHeader("some/path");
		assertThat(s).isEqualTo("\n\u001B[32mscanning 'some/path'\u001B[0m");
	}
}