/*
 * Copyright 2021 - 2023 the original author or authors.
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

import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.jline.utils.Colors;
import org.springframework.sbm.engine.precondition.PreconditionCheck;
import org.springframework.sbm.engine.precondition.PreconditionCheckResult;
import org.springframework.sbm.engine.precondition.PreconditionVerificationResult;
import org.springframework.stereotype.Component;

@Component
public class PreconditionVerificationRenderer {

    public String renderPreconditionCheckResults(PreconditionVerificationResult result) {
        AttributedStringBuilder stringBuilder = new AttributedStringBuilder();
        stringBuilder.style(stringBuilder.style().DEFAULT.bold());
        stringBuilder.append("\n\n").append(String.format("Checked preconditions for '%s'", result.getProjectRoot())).append("\n");

        result.getResults().forEach(r -> stringBuilder.append(renderCheckResult(r)));
        stringBuilder.append("\n");

        return stringBuilder.toAnsi();
    }

    private String renderCheckResult(PreconditionCheckResult r) {
        AttributedStringBuilder builder = new AttributedStringBuilder();

        // TODO: move rendering of status into central place
        if (r.getState().equals(PreconditionCheck.ResultState.FAILED)) {
            builder.style(builder.style().DEFAULT.bold().foreground(Colors.rgbColor("red")));
            builder.append(" [X]");
            builder.style(builder.style().DEFAULT);
        }

        if (r.getState().equals(PreconditionCheck.ResultState.PASSED)) {
            builder.style(AttributedStyle.DEFAULT.bold().foreground(Colors.rgbColor("green")));
            builder.append("[ok]");
            builder.style(AttributedStyle.DEFAULT);
        }

        if (r.getState().equals(PreconditionCheck.ResultState.WARN)) {
            builder.style(AttributedStyle.DEFAULT.bold().foreground(Colors.rgbColor("yellow")));
            builder.append(" [!]");
            builder.style(AttributedStyle.DEFAULT);
        }

        builder.append(" ").append(r.getMessage());
        builder.append("\n");

        return builder.toAnsi();
    }

}
