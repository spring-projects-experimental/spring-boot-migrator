package org.openrewrite.java.spring.boot3;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.params.provider.Arguments;
import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.Result;
import org.openrewrite.properties.PropertiesParser;
import org.openrewrite.properties.tree.Properties;
import org.openrewrite.test.RewriteTest;
import org.openrewrite.yaml.YamlParser;
import org.openrewrite.yaml.tree.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

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

        InputStream data = new FileInputStream(inputFilePath);

        String fileContent = new String(data.readAllBytes());
        String[] k = fileContent.split("expected:.*\n");

        return new ImmutablePair<>(k[0].replaceAll("input:.*\n", ""), k[1]);
    }

    protected Pair<String, String> provideIOX(String inputFilePath) throws IOException {

        InputStream data = ConfigRecipeTest.class.getResourceAsStream(inputFilePath);

        String fileContent = new String(data.readAllBytes());
        String[] k = fileContent.split("expected:.*\n");

        return new ImmutablePair<>(k[0].replaceAll("input:.*\n", ""), k[1]);
    }

    protected static Stream<Arguments> provideFiles(String folder, String fileType) throws URISyntaxException {

        URL url = RemovedPropertyTest.class.getResource(folder);

        File f = Paths.get(url.toURI()).toFile();

        return Arrays.stream(f.listFiles())
                .filter(k -> k.toString().contains(fileType))
                .map(k -> Arguments.of(k.toString()));

    }
}
