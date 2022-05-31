package org.springframework.sbm.mule.actions.javadsl.translators.db;

public class DBCommons {
    public static String escapeDoubleQuotes(String str) {
        return str.replace("\"", "\\\"");
    }
}
