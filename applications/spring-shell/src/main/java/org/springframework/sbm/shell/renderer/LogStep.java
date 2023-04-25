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

import lombok.RequiredArgsConstructor;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.jline.utils.Colors;

/**
 * @author Fabian Krüger
 */
@RequiredArgsConstructor
public class LogStep implements ProgressStep {

    private final String logMessage;
    private final Indent indent;

    @Override
    public String finish() {
        return "";
    }

    @Override
    public String render() {
        AttributedStringBuilder builder = new AttributedStringBuilder();
        builder.style(AttributedStyle.DEFAULT.bold().foreground(Colors.rgbColor("green")));
        builder.append("[ok]");

        return " ".repeat(indent.get()) + "    " + builder.toAttributedString().toAnsi() + " " + logMessage;
    }

    public String renderError() {
        AttributedStringBuilder builder = new AttributedStringBuilder();
        builder.append(" ".repeat(indent.get()))
                .append("     ")
                .style(AttributedStyle.DEFAULT.bold().foreground(Colors.rgbColor("red")))
                .append("[X]")
                .style(AttributedStyle.DEFAULT)
                .append(" ")
                .append(logMessage);

        return builder.toAttributedString().toAnsi();
    }

    public String renderWarning() {
        AttributedStringBuilder builder = new AttributedStringBuilder();
        builder.append(" ".repeat(indent.get()))
                .append("     ")
                .style(AttributedStyle.DEFAULT.bold().foreground(Colors.rgbColor("yellow")))
                .append("[!]")
                .style(AttributedStyle.DEFAULT)
                .append(" ")
                .append(logMessage);

        return builder.toAttributedString().toAnsi();
    }
}
