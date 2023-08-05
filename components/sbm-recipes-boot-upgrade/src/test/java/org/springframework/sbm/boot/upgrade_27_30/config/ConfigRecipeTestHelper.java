/*
 * Copyright 2021 - 2023 the original author or authors.
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
package org.springframework.sbm.boot.upgrade_27_30.config;

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

public class ConfigRecipeTestHelper {

    public static List<Result> runRecipeOnYaml(@Language("yml") String source, String recipeName) {
        InMemoryExecutionContext ctx = new InMemoryExecutionContext(Throwable::printStackTrace);
        List<Yaml.Documents> document = new YamlParser().parse(source);
        return RewriteTest
                .fromRuntimeClasspath(recipeName)
                .run(document, ctx).getResults();
    }

    public static List<Result> runRecipeOnProperties(@Language("properties") String source, String recipeName) {
        InMemoryExecutionContext ctx = new InMemoryExecutionContext(Throwable::printStackTrace);
        List<Properties.File> document = new PropertiesParser().parse(source);
        return RewriteTest
                .fromRuntimeClasspath(recipeName)
                .run(document, ctx).getResults();
    }

    public static Pair<String, String> provideIO(String inputFilePath) throws IOException {

        InputStream data = new FileInputStream(inputFilePath);

        String fileContent = new String(data.readAllBytes());
        String[] k = fileContent.split("expected:.*\n");

        return new ImmutablePair<>(k[0].replaceAll("input:.*\n", ""), k[1]);
    }

    public static Stream<Arguments> provideFiles(String folder, String fileType) throws URISyntaxException {

        URL url = RemovedPropertyTest.class.getResource(folder);

        File f = Paths.get(url.toURI()).toFile();

        return Arrays.stream(f.listFiles())
                .filter(k -> k.toString().contains(fileType))
                .map(k -> Arguments.of(k.toString()));

    }
}
