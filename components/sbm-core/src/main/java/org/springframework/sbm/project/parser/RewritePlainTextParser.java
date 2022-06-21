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
