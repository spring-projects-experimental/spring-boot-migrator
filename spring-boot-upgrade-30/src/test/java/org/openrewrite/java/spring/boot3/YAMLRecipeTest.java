package org.openrewrite.java.spring.boot3;

import org.intellij.lang.annotations.Language;
import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.Result;
import org.openrewrite.test.RewriteTest;
import org.openrewrite.yaml.YamlParser;
import org.openrewrite.yaml.tree.Yaml;

import java.util.List;

public abstract class YAMLRecipeTest {

    private final InMemoryExecutionContext ctx = new InMemoryExecutionContext(Throwable::printStackTrace);

    abstract String getRecipeName();
    protected List<Result> runRecipe(@Language("yml") String source) {
        List<Yaml.Documents> document = new YamlParser().parse(source);
        return RewriteTest
                .fromRuntimeClasspath(getRecipeName())
                .run(document, ctx);
    }
}
