package org.springframework.sbm.project.parser;

import org.openrewrite.ExecutionContext;
import org.openrewrite.Parser;
import org.openrewrite.Tree;
import org.openrewrite.internal.lang.Nullable;
import org.openrewrite.marker.Markers;
import org.openrewrite.text.PlainText;
import org.openrewrite.text.PlainTextParser;
import org.openrewrite.tree.ParsingEventListener;
import org.openrewrite.tree.ParsingExecutionContextView;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class RewritePlainTextParser extends PlainTextParser {
    public List<PlainText> parseInputs(Iterable<Parser.Input> sources, @Nullable Path relativeTo, ExecutionContext ctx) {
        List<PlainText> plainTexts = new ArrayList();
        Iterator var5 = sources.iterator();
        ParsingEventListener parsingListener = ParsingExecutionContextView.view(ctx).getParsingListener();

        while(var5.hasNext()) {
            Parser.Input source = (Parser.Input)var5.next();
            PlainText plainText = new PlainText(Tree.randomId(), relativeTo == null ? source.getPath() : relativeTo.relativize(source.getPath()).normalize(), source.getSource().getCharset().name(), source.getSource().isCharsetBomMarked(), Markers.EMPTY, source.getSource().readFully());
            plainTexts.add(plainText);
            parsingListener.parsed(source, plainText);
        }

        return plainTexts;
    }
}
