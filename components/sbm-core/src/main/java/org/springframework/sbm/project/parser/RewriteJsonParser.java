package org.springframework.sbm.project.parser;

import org.openrewrite.ExecutionContext;
import org.openrewrite.Parser;
import org.openrewrite.internal.lang.Nullable;
import org.openrewrite.json.JsonParser;
import org.openrewrite.json.tree.Json;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.List;

@Component
public class RewriteJsonParser extends JsonParser {

    private JsonParser delegatingParser = new JsonParser();

    @Override
    public List<Json.Document> parseInputs(Iterable<Input> sourceFiles, @Nullable Path relativeTo, ExecutionContext ctx) {
        return delegatingParser.parseInputs(sourceFiles, relativeTo, ctx);
    }

    @Override
    public List<Json.Document> parse(String... sources) {
        return delegatingParser.parse(sources);
    }

    @Override
    public boolean accept(Path path) {
        return delegatingParser.accept(path);
    }

    @Override
    public Path sourcePathFromSourceText(Path prefix, String sourceCode) {
        return delegatingParser.sourcePathFromSourceText(prefix, sourceCode);
    }

    @Override
    public List<Json.Document> parse(Iterable<Path> sourceFiles, @Nullable Path relativeTo, ExecutionContext ctx) {
        return delegatingParser.parse(sourceFiles, relativeTo, ctx);
    }

    @Override
    public List<Json.Document> parse(ExecutionContext ctx, String... sources) {
        return delegatingParser.parse(ctx, sources);
    }

    @Override
    public boolean accept(Input input) {
        return delegatingParser.accept(input);
    }

    @Override
    public List<Input> acceptedInputs(Iterable<Input> input) {
        return delegatingParser.acceptedInputs(input);
    }

    @Override
    public Parser<Json.Document> reset() {
        return delegatingParser.reset();
    }
}
