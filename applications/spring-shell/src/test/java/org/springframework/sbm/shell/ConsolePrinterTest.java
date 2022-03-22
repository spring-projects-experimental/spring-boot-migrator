package org.springframework.sbm.shell;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;

class ConsolePrinterTest {

    @Test
    void shouldPrintToConsole() {
        ByteArrayOutputStream sysOutBuffer = new ByteArrayOutputStream();
        System.setOut(new PrintStream(sysOutBuffer));

        ConsolePrinter sut = new ConsolePrinter();
        sut.println("some text");
        assertThat(sysOutBuffer.toString()).isEqualTo("some text\n");
    }
}