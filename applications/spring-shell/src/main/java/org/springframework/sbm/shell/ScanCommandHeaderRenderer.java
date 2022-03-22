package org.springframework.sbm.shell;

import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.jline.utils.Colors;
import org.springframework.stereotype.Component;

@Component
public class ScanCommandHeaderRenderer {
    public String renderHeader(String projectRoot) {
        AttributedStringBuilder builder = new AttributedStringBuilder();
        builder.append("\n");
        builder.style(AttributedStyle.DEFAULT.italicDefault().boldDefault().foreground(Colors.rgbColor("green")));
        builder.append("scanning '" + projectRoot + "'");
        return builder.toAnsi();
    }
}
