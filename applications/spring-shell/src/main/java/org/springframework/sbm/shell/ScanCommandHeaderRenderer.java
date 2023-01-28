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

import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.jline.utils.Colors;
import org.springframework.stereotype.Component;

/**
 * @author Modified by Mahendra Rao(bsmahi)
 */
@Component
public class ScanCommandHeaderRenderer {
    public String renderHeader(String projectRoot) {
        AttributedStringBuilder builder = new AttributedStringBuilder();
        builder.append("\n");
        builder.style(AttributedStyle.DEFAULT.italicDefault().boldDefault().foreground(Colors.rgbColor("green")));
        builder.append("scanning '").append(projectRoot).append("'");
        return builder.toAnsi();
    }
}
