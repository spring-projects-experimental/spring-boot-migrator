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
package org.springframework.sbm.shell.renderer;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author Fabian Krüger
 */
//@Slf4j
public class RecipeProgressRendererTest {

    Printer printer = mock(Printer.class);
    RecipeProgressRenderer sut;

    @Test
    void loaderTest() {
        sut = new RecipeProgressRenderer(printer);
        sut.startProcess("test");
        verify(printer).print(".    test");
        sut.render();
        verify(printer).print("..   test");
        sut.render();
        verify(printer).print("...  test");
        sut.render();
        verify(printer).print(".... test");
        sut.render();
        verify(printer).print(".    test");
        sut.render();
        sut.finishProcess();
        verify(printer).printAndNewLine("\u001B[32;1m[ok]\u001B[0m test");
    }

    @Test
    void subProcess() {
        sut = new RecipeProgressRenderer(printer);
        sut.startProcess("A");
        verify(printer).print(".    A");

        sut.startProcess("B");
        verify(printer).printAndNewLine("[..] A");
        verify(printer).print("    .    B");

        sut.render();
        verify(printer).print("    ..   B");

        sut.startProcess("C");
        verify(printer).printAndNewLine("    [..] B");
        verify(printer).print("        .    C");

        sut.render();
        verify(printer).print("        ..   C");

        sut.finishProcess();
        verify(printer).printAndNewLine("        \u001B[32;1m[ok]\u001B[0m C");

        sut.finishProcess();
        verify(printer).printAndNewLine("    \u001B[32;1m[ok]\u001B[0m B");

        sut.finishProcess();
        verify(printer).printAndNewLine("\u001B[32;1m[ok]\u001B[0m A");

    }


    @Test
    void subProcessWithLogging() {
        sut = new RecipeProgressRenderer(printer);
        System.out.flush();
        PrintStream initialOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outContent);
        System.setOut(out);

        sut = new RecipeProgressRenderer(new Printer());

        sut.startProcess("A");
        sut.logMessage("A log1");
        sut.logMessage("A log2");
        sut.startProcess("B");
        sut.render();
        sut.logMessage("B log1");
        sut.logMessage("B log2");
        sut.startProcess("C");
        sut.render();
        sut.finishProcess();
        sut.finishProcess();
        sut.finishProcess();

        String actual = outContent.toString();

        System.setOut(initialOut);
        System.out.println(actual);

        assertThat(actual).isEqualTo(
                parse(".    A|b|b|b|b|b|b" +
                        "[..] A\n" +
                        "    [ok] A log1\n" +
                        "    [ok] A log2\n" +
                        "    .    B|b|b|b|b|b|b|b|b|b|b" +
                        "    ..   B|b|b|b|b|b|b|b|b|b|b" +
                        "    [..] B\n" +
                        "        [ok] B log1\n" +
                        "        [ok] B log2\n" +
                        "        .    C|b|b|b|b|b|b|b|b|b|b|b|b|b|b" +
                        "        ..   C|b|b|b|b|b|b|b|b|b|b|b|b|b|b" +
                        "        [ok] C\n" +
                        "    [ok] B\n" +
                        "[ok] A\n")
        );
        System.setOut(initialOut);
    }

    @Test
    @Disabled("Logging Adapter fails in combination with other tests, related to lombok @Slf4j?")
    void successRecipeTest() {
        Logger log = LoggerFactory.getLogger(getClass());

        sut = new RecipeProgressRenderer(new Printer());
        System.out.flush();
        PrintStream initialOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outContent);
        System.setOut(out);

        sut.startProcess("A");
        sut.render();

        log.info("some info log");
        log.warn("some warn log");
        log.error("some error log");

        sut.render();
        sut.render();
        sut.startProcess("B");
        sut.render();
        sut.finishProcess();
        sut.finishProcess();
        sut.startProcess("C");
        sut.logMessage("C log 1");
        sut.logMessage("C log 2");
        sut.startProcess("D");
        sut.render();
        sut.finishAction();
        sut.startProcess("E");
        sut.failProcess();

        String actual = outContent.toString();

          System.setOut(initialOut);
          System.out.println(actual);

        assertThat(actual).isEqualTo(
                parse(
                        ".    A|b|b|b|b|b|b" +
                        "..   A|b|b|b|b|b|b" +
                        "[..] A\n" +
                        "    [ok] some info log\n" +
                        "     [!] some warn log\n" +
                        "     [X] some error log\n" +
                        "    .    B|b|b|b|b|b|b|b|b|b|b" +
                        "    ..   B|b|b|b|b|b|b|b|b|b|b" +
                        "    [ok] B\n" +
                        "[ok] A\n" +
                        ".    C|b|b|b|b|b|b" +
                        "[..] C\n" +
                        "    [ok] C log 1\n" +
                        "    [ok] C log 2\n" +
                        "    .    D|b|b|b|b|b|b|b|b|b|b" +
                        "    ..   D|b|b|b|b|b|b|b|b|b|b" +
                        "    [ok] D\n" +
                        "[ok] C\n" +
                        ".    E|b|b|b|b|b|b" +
                        " [X] E\n"
                )
        );
        System.setOut(initialOut);
    }

    private String parse(String s) {
        return s.replace("|b", new String(new byte[]{8}, StandardCharsets.UTF_8))
                .replace("[ok]", "\u001B[32;1m[ok]\u001B[0m")
                .replace("[X]", "\u001B[91;1m[X]\u001B[0m")
                .replace("[!]", "\u001B[93;1m[!]\u001B[0m");
    }
}
