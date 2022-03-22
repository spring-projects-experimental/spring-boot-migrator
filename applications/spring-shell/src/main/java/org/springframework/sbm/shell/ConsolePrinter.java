package org.springframework.sbm.shell;

import org.springframework.stereotype.Component;

@Component
public class ConsolePrinter {
    public void println(String text) {
        System.out.println(text);
    }
}
