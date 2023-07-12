package org.springframework.sbm.parsers.events;

import org.openrewrite.Parser;
import org.openrewrite.SourceFile;
import org.springframework.context.ApplicationEvent;

/**
 * @author Fabian Krüger
 */
public record ParsedResourceEvent(Parser.Input input, SourceFile sourceFile){
}
