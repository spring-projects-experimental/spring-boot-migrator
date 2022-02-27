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
package org.springframework.sbm.shell;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.sbm.build.migration.actions.AddMavenPlugin;
import org.springframework.sbm.engine.events.ActionFinishedEvent;
import org.springframework.sbm.engine.events.ActionLogEvent;
import org.springframework.sbm.engine.events.ActionProcessFinishedEvent;
import org.springframework.sbm.engine.events.ActionStartedEvent;
import org.springframework.sbm.engine.recipe.AbstractAction;
import org.springframework.sbm.engine.recipe.DisplayDescription;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Iterator;

import static org.assertj.core.api.Assertions.assertThat;

class ApplyCommandRendererTest {

    @Test
    @Disabled("Activate when in doubt")
    void poorMansConcurrencyTest() throws InterruptedException, IOException {
        Logger log = LoggerFactory.getLogger(getClass());

        ApplyCommandRenderer sut = new ApplyCommandRenderer();

        for (int i = 0; i < 400_000; i++) {
            sut.setDelay(1); // higher chance of conflict
            sut.setInitialDelay(0);

            System.out.flush();
            PrintStream initialOut = System.out;
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            PrintStream out = new PrintStream(outContent);
            System.setOut(out);

            AbstractAction action1 = new DisplayDescription();
            action1.setDescription("action 1");
            AbstractAction action2 = new AddMavenPlugin();
            action2.setDescription("action 2");

            sut.onActionStarted(new ActionStartedEvent("action 1"));
            Thread.sleep(2);
            sut.onActionLog(new ActionLogEvent("log 1"));
            log.error("error");
            log.info("info");
            sut.onActionStarted(new ActionStartedEvent("action 2"));
            Thread.sleep(3);
            log.warn("warn");
            sut.onActionLog(new ActionLogEvent("log 2"));
            sut.onActionLog(new ActionLogEvent("log 3"));
            sut.onActionProcessFinished(new ActionProcessFinishedEvent());
            sut.onActionFinished(new ActionFinishedEvent("action 1"));

//            System.setOut(initialOut);
//            System.out.println(actual);

            String s = outContent.toString(StandardCharsets.UTF_8);
            Iterator<String> lines = Arrays.asList(s.split("\n")).iterator();
            // Match:  '.    action 1|b|b|b|b|b|b|b|b|b|b|b|b|b..   action 1|b|b|b|b|b|b|b|b|b|b|b|b|b[..] action 1'
            String one = lines.next();
            assertThat(one).matches(parse("^.*([\\. ]{0,4} action 1(\\|b){13}){1,}\\[\\.\\.\\] action 1"));
            assertThat(one).endsWith(parse("[..] action 1"));
            assertThat(lines.next()).isEqualTo(parse("    [ok] log 1"));
            assertThat(lines.next()).isEqualTo(parse("     [X] error"));
            assertThat(lines.next()).isEqualTo(parse("    [ok] info"));
            // Match:  '    .    action 2|b|b|b|b|b|b|b|b|b|b|b|b|b|b|b|b|b    ..   action 2|b|b|b|b|b|b|b|b|b|b|b|b|b|b|b|b|b    [..] action 2'
            assertThat(lines.next()).matches(parse("^.*(    [\\. ]{0,4} action 2(\\|b){17}){1,}    \\[\\.\\.\\] action 2"));
            assertThat(lines.next()).isEqualTo(parse("         [!] warn"));
            assertThat(lines.next()).isEqualTo(parse("        [ok] log 2"));
            assertThat(lines.next()).isEqualTo(parse("        [ok] log 3"));
            assertThat(lines.next()).isEqualTo(parse("    [ok] action 2"));
            assertThat(lines.next()).isEqualTo(parse("[ok] action 1"));

            outContent.flush();
            log.info("This should log to console again");
            String s2 = outContent.toString(StandardCharsets.UTF_8);
            System.setOut(initialOut);
            assertThat(s2).endsWith(" [main] INFO org.springframework.sbm.shell.ApplyCommandRendererTest - This should log to console again\n");
        }
    }

    private String parse(String s) {
        return s.replace("|b", new String(new byte[]{8}, StandardCharsets.UTF_8))
                .replace("[ok]", "\u001B[32;1m[ok]\u001B[0m")
                .replace("[X]", "\u001B[91;1m[X]\u001B[0m")
                .replace("[!]", "\u001B[93;1m[!]\u001B[0m");
    }
}