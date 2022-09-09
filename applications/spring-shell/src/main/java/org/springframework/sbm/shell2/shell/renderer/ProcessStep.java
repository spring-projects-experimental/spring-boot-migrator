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
package org.springframework.sbm.shell2.shell.renderer;

import lombok.RequiredArgsConstructor;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.jline.utils.Colors;

/**
 * @author Fabian Krüger
 */
@RequiredArgsConstructor
public class ProcessStep implements ProgressStep {
    private final String description;
    private final Indent indent;
    private final Loader loader;
    private final String successBox;
    private final String failBox;
    private boolean isPaused;

    public ProcessStep(String description, Indent indent) {
        this.description = description;
        this.indent = indent;
        this.loader = new Loader();

        AttributedStringBuilder builder = new AttributedStringBuilder();
        builder.style(AttributedStyle.DEFAULT.bold().foreground(Colors.rgbColor("green")));
        builder.append("[ok]");
        this.successBox = builder.toAnsi();

        builder = new AttributedStringBuilder();
        builder.style(AttributedStyle.DEFAULT.bold().foreground(Colors.rgbColor("red")));
        builder.append("[X]");
        this.failBox = builder.toAnsi();
    }

    public String start() {
        String loaderStr = this.loader.renderLoader();
        return " ".repeat(indent.get())  + loaderStr + " " + description;
    }

    public String pause() {
        this.isPaused = true;
        return " ".repeat(indent.get()) + "[..] " + description;
    }

    @Override
    public String finish() {
        return " ".repeat(indent.get()) + this.successBox + " " + description;
    }


    public String fail() {
        return " ".repeat(indent.get()) + " " + failBox + " " + description;
    }

    public String render() {
        return start();
    }

    public void resume() {
        this.isPaused = false;
    }

    public boolean isPaused() {
        return isPaused;
    }

    private class Loader {
        private static final int LENGTH = 4;
        private int cnt = 0;
        private String renderLoader() {
            cnt++;
            if (cnt > LENGTH) cnt = 0;
            return ".".repeat(cnt) + " ".repeat(LENGTH - cnt);
        }
    }
}
