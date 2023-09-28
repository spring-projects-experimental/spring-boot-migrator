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
package org.springframework.sbm.parsers;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.openrewrite.ExecutionContext;
import org.openrewrite.Parser;
import org.openrewrite.SourceFile;
import org.openrewrite.hcl.HclParser;
import org.openrewrite.java.JavaParser;
import org.openrewrite.json.JsonParser;
import org.openrewrite.properties.PropertiesParser;
import org.openrewrite.protobuf.ProtoParser;
import org.openrewrite.quark.QuarkParser;
import org.openrewrite.text.PlainTextParser;
import org.openrewrite.xml.XmlParser;
import org.openrewrite.yaml.YamlParser;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.sbm.utils.ResourceUtil;

import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Code from https://github.com/fabapp2/rewrite-maven-plugin/blob/83d184ea9ffe3046429f16c91aa56a9610bae832/src/main/java/org/openrewrite/maven/ResourceParser.java
 * The motivation was to decouple the parser from file access.
 */
@Slf4j
public class RewriteResourceParser {
    private static final Set<String> DEFAULT_IGNORED_DIRECTORIES = new HashSet<>(Arrays.asList("build", "target", "out", ".sonar", ".gradle", ".idea", ".project", "node_modules", ".git", ".metadata", ".DS_Store"));

    private final Path baseDir;
    private final Collection<PathMatcher> exclusions;
    private final int sizeThresholdMb;
    private final Collection<Path> excludedDirectories;
    private final Collection<PathMatcher> plainTextMasks;

    /**
     * Sometimes java files will exist in the src/main/resources directory. For example, Drools:
     */
    private final JavaParser.Builder<? extends JavaParser, ?> javaParserBuilder;
    private final ExecutionContext executionContext;

    public RewriteResourceParser(
            Path baseDir,
            Collection<String> exclusions,
            Collection<String> plainTextMasks,
            int sizeThresholdMb,
            Collection<Path> excludedDirectories,
             JavaParser.Builder<? extends JavaParser, ?> javaParserBuilder,
            ExecutionContext executionContext
    ) {
        this.baseDir = baseDir;
        this.javaParserBuilder = javaParserBuilder;
        this.executionContext = executionContext;
        this.exclusions = pathMatchers(baseDir, exclusions);
        this.sizeThresholdMb = sizeThresholdMb;
        this.excludedDirectories = excludedDirectories;
        this.plainTextMasks = pathMatchers(baseDir, plainTextMasks);
    }

    private Collection<PathMatcher> pathMatchers(Path basePath, Collection<String> pathExpressions) {
        return pathExpressions.stream()
                .map(o -> basePath.getFileSystem().getPathMatcher("glob:" + o))
                .collect(Collectors.toList());
    }

    public Stream<SourceFile> parse(Path searchDir, List<Resource> resources, Set<Path> alreadyParsed) {
        // TODO: 945 remove/clean this up
        List<Resource> resourcesLeft = resources.stream()
                .filter(r -> alreadyParsed.stream().noneMatch(path -> ResourceUtil.getPath(r).toString().startsWith(path.toString())))
                .toList();
        return this.parseSourceFiles(searchDir, resourcesLeft, alreadyParsed, executionContext);


//
//        Stream<SourceFile> sourceFiles = Stream.empty();
//        if (!searchDir.toFile().exists()) {
//            return sourceFiles;
//        } else {
//            Consumer<Throwable> errorConsumer = (t) -> {
//                this.logger.debug("Error parsing", t);
//            };
//            InMemoryExecutionContext ctx = new InMemoryExecutionContext(errorConsumer);
//
//            try {
//                sourceFiles = Stream.concat(sourceFiles, this.parseSourceFiles(searchDir, alreadyParsed, ctx));
//                return sourceFiles;
//            } catch (IOException var7) {
//                this.logger.error(var7.getMessage(), var7);
//                throw new UncheckedIOException(var7);
//            }
//        }
    }

    @SuppressWarnings({"DuplicatedCode", "unchecked"})
    public <S extends SourceFile> Stream<S> parseSourceFiles(
            Path searchDir,
            List<Resource> resources,
            Set<Path> alreadyParsed,
            ExecutionContext ctx) {

        List<Path> resourcesLeft = new ArrayList<>();
        List<Path> quarkPaths = new ArrayList<>();
        List<Path> plainTextPaths = new ArrayList<>();

        List<Resource> filteredResources = resources
                .stream()
                .filter(r -> ResourceUtil.getPath(r).toString().startsWith(searchDir.toString()))
                .toList();

        filteredResources.forEach(resource -> {
            Path file = ResourceUtil.getPath(resource);
            Path dir = file.getParent();
            if (isExcluded(dir) || isIgnoredDirectory(searchDir, dir) || excludedDirectories.contains(dir) || alreadyParsed.contains(new FileSystemResource(dir)) || alreadyParsed.contains(resource)) {
                return;
            } else {
                // FIXME: 945 only check threshold if value > 0 is given
                long fileSize = ResourceUtil.contentLength(resource);
                if (isOverSizeThreshold(fileSize)) {
                        log.info("Parsing as quark " + file + " as its size " + fileSize / (1024L * 1024L) +
                                "Mb exceeds size threshold " + sizeThresholdMb + "Mb");
                        quarkPaths.add(file);
                    } else if (isParsedAsPlainText(file)) {
                        plainTextPaths.add(file);
                    } else {
                        resourcesLeft.add(file);
                    }
            }
        });

        Stream<S> sourceFiles = Stream.empty();

        JavaParser javaParser = javaParserBuilder.build();
        List<Path> javaPaths = new ArrayList<>();

        JsonParser jsonParser = new JsonParser();
        List<Path> jsonPaths = new ArrayList<>();

        XmlParser xmlParser = new XmlParser();
        List<Path> xmlPaths = new ArrayList<>();

        YamlParser yamlParser = new YamlParser();
        List<Path> yamlPaths = new ArrayList<>();

        PropertiesParser propertiesParser = new PropertiesParser();
        List<Path> propertiesPaths = new ArrayList<>();

        ProtoParser protoParser = new ProtoParser();
        List<Path> protoPaths = new ArrayList<>();

        // Python currently not supported
//        PythonParser pythonParser = PythonParser.builder().build();
//        List<Path> pythonPaths = new ArrayList<>();

        HclParser hclParser = HclParser.builder().build();
        List<Path> hclPaths = new ArrayList<>();

        PlainTextParser plainTextParser = new PlainTextParser();

        QuarkParser quarkParser = new QuarkParser();

        filteredResources
                .forEach(resource -> {
            // See https://github.com/quarkusio/quarkus/blob/main/devtools/project-core-extension-codestarts/src/main/resources/codestarts/quarkus/extension-codestarts/resteasy-reactive-codestart/java/src/main/java/org/acme/%7Bresource.class-name%7D.tpl.qute.java
            // for an example of why we don't want qute files be parsed as java
            Path path = ResourceUtil.getPath(resource);
//            if (javaParser.accept(path) && !path.toString().endsWith(".qute.java")) {
//                javaPaths.add(path);
//            }
            if (jsonParser.accept(path)) {
                jsonPaths.add(path);
            } else if (xmlParser.accept(path)) {
                xmlPaths.add(path);
            } else if (yamlParser.accept(path)) {
                yamlPaths.add(path);
            } else if (propertiesParser.accept(path)) {
                propertiesPaths.add(path);
            } else if (protoParser.accept(path)) {
                protoPaths.add(path);
            } /*else if(pythonParser.accept(path)) {
                pythonPaths.add(path);
            }*/ else if (hclParser.accept(path)) {
                hclPaths.add(path);
            } else if (quarkParser.accept(path)) {
                quarkPaths.add(path);
            }
        });

        Map<Path, Resource> pathToResource = filteredResources.stream().collect(Collectors.toMap(r -> ResourceUtil.getPath(r), r -> r));

        if (!javaPaths.isEmpty()) {
            List<Parser.Input> inputs = getInputs(pathToResource, javaPaths);
            sourceFiles = Stream.concat(sourceFiles, (Stream<S>) javaParser.parseInputs(inputs, baseDir, ctx));
            alreadyParsed.addAll(javaPaths);
        }

        if (!jsonPaths.isEmpty()) {
            List<Parser.Input> inputs = getInputs(pathToResource, jsonPaths);
            sourceFiles = Stream.concat(sourceFiles, (Stream<S>) jsonParser.parseInputs(inputs, baseDir, ctx));
            alreadyParsed.addAll(jsonPaths);
        }

        if (!xmlPaths.isEmpty()) {
            List<Parser.Input> inputs = getInputs(pathToResource, xmlPaths);
            sourceFiles = Stream.concat(sourceFiles, (Stream<S>) xmlParser.parseInputs(inputs, baseDir, ctx));
            alreadyParsed.addAll(xmlPaths);
        }

        if (!yamlPaths.isEmpty()) {
            List<Parser.Input> inputs = getInputs(pathToResource, yamlPaths);
            sourceFiles = Stream.concat(sourceFiles, (Stream<S>) yamlParser.parseInputs(inputs, baseDir, ctx));
            alreadyParsed.addAll(yamlPaths);
        }

        if (!propertiesPaths.isEmpty()) {
            List<Parser.Input> inputs = getInputs(pathToResource, propertiesPaths);
            sourceFiles = Stream.concat(sourceFiles, (Stream<S>) propertiesParser.parseInputs(inputs, baseDir, ctx));
            alreadyParsed.addAll(propertiesPaths);
        }

        if (!protoPaths.isEmpty()) {
            List<Parser.Input> inputs = getInputs(pathToResource, protoPaths);
            sourceFiles = Stream.concat(sourceFiles, (Stream<S>) protoParser.parseInputs(inputs, baseDir, ctx));
            alreadyParsed.addAll(protoPaths);
        }

//        if (!pythonPaths.isEmpty()) {
//            List<Parser.Input> inputs = getInputs(pathToResource, pythonPaths);
//            sourceFiles = Stream.concat(sourceFiles, (Stream<S>) pythonParser.parseInputs(inputs, baseDir, ctx));
//            alreadyParsed.addAll(pythonPaths);
//        }

        if (!hclPaths.isEmpty()) {
            List<Parser.Input> inputs = getInputs(pathToResource, hclPaths);
            sourceFiles = Stream.concat(sourceFiles, (Stream<S>) hclParser.parseInputs(inputs, baseDir, ctx));
            alreadyParsed.addAll(hclPaths);
        }

        if (!plainTextPaths.isEmpty()) {
            List<Parser.Input> inputs = getInputs(pathToResource, plainTextPaths);
            sourceFiles = Stream.concat(sourceFiles, (Stream<S>) plainTextParser.parseInputs(inputs, baseDir, ctx));
            alreadyParsed.addAll(plainTextPaths);
        }

        if (!quarkPaths.isEmpty()) {
            List<Parser.Input> inputs = getInputs(pathToResource, quarkPaths);
            sourceFiles = Stream.concat(sourceFiles, (Stream<S>) quarkParser.parseInputs(inputs, baseDir, ctx));
            alreadyParsed.addAll(quarkPaths);
        }

        return sourceFiles;
    }

    @NotNull
    private static List<Parser.Input> getInputs(Map<Path, Resource> pathResourceMap, List<Path> paths) {
        return paths.stream()
                .map(path -> new Parser.Input(path, () -> ResourceUtil.getInputStream(pathResourceMap.get(path)))).toList();
    }

    private boolean isOverSizeThreshold(long fileSize) {
        return sizeThresholdMb > 0 && fileSize > sizeThresholdMb * 1024L * 1024L;
    }

    private boolean isExcluded(Path path) {
        if (!exclusions.isEmpty()) {
            for (PathMatcher excluded : exclusions) {
                if (excluded.matches(baseDir.relativize(path))) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isParsedAsPlainText(Path path) {
        if (!plainTextMasks.isEmpty()) {
            Path computed = baseDir.relativize(path);
            if (!computed.startsWith("/")) {
                computed = Paths.get("/").resolve(computed);
            }
            for (PathMatcher matcher : plainTextMasks) {
                if (matcher.matches(computed)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isIgnoredDirectory(Path searchDir, Path path) {
        for (Path pathSegment : searchDir.relativize(path)) {
            if (DEFAULT_IGNORED_DIRECTORIES.contains(pathSegment.toString())) {
                return true;
            }
        }
        return false;
    }
}
