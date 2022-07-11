package org.openrewrite.java.spring.boot3;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.intellij.lang.annotations.Language;
import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.Result;
import org.openrewrite.properties.PropertiesParser;
import org.openrewrite.properties.tree.Properties;
import org.openrewrite.test.RewriteTest;
import org.openrewrite.yaml.YamlParser;
import org.openrewrite.yaml.tree.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public abstract class ConfigRecipeTest {

    private final InMemoryExecutionContext ctx = new InMemoryExecutionContext(Throwable::printStackTrace);

    abstract String getRecipeName();

    protected List<Result> runRecipeOnYaml(@Language("yml") String source) {
        List<Yaml.Documents> document = new YamlParser().parse(source);
        return RewriteTest
                .fromRuntimeClasspath(getRecipeName())
                .run(document, ctx);
    }

    protected List<Result> runRecipeOnProperties(@Language("properties") String source) {
        List<Properties.File> document = new PropertiesParser().parse(source);
        return RewriteTest
                .fromRuntimeClasspath(getRecipeName())
                .run(document, ctx);
    }

    protected Pair<String, String> provideIO(String inputFilePath) throws IOException {
        InputStream data =
                ConfigRecipeTest.class.getResourceAsStream(inputFilePath);

        if (data == null) {
            throw new RuntimeException("unable to read: "+ inputFilePath);
        }

        String fileContent = new String(data.readAllBytes());
        String[] k = fileContent.split("expected:.*\n");

        return new ImmutablePair<>(k[0].replaceAll("input:.*\n", ""), k[1]);
    }

}
