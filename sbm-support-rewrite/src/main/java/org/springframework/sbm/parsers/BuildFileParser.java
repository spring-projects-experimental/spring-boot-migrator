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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.execution.MavenSession;
import org.openrewrite.ExecutionContext;
import org.openrewrite.Parser;
import org.openrewrite.SourceFile;
import org.openrewrite.marker.Marker;
import org.openrewrite.maven.MavenExecutionContextView;
import org.openrewrite.maven.MavenMojoProjectParser;
import org.openrewrite.maven.MavenParser;
import org.openrewrite.maven.cache.InMemoryMavenPomCache;
import org.openrewrite.maven.cache.MavenPomCache;
import org.openrewrite.xml.tree.Xml;
import org.springframework.core.io.Resource;
import org.springframework.sbm.utils.ResourceUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

/**
 * Copies behaviour from rewrite-maven-plugin:5.2.2
 *
 * @author Fabian Krüger
 */
@Slf4j
@Component
@RequiredArgsConstructor
class BuildFileParser {

    private final ParserSettings parserSettings;

    /**
     * Parse a list of Maven Pom files to a Map of {@code Path} and their parsed {@link Xml.Document}s.
     * The {@link Xml.Document}s are marked with {@link org.openrewrite.maven.tree.MavenResolutionResult} and the provided provenance markers.
     * Reimplements {@link org.openrewrite.maven.MavenMojoProjectParser#parseMaven(List, Map, ExecutionContext)}.
     *
     * @param baseDir the {@link Path} to the root of the scanned project
     * @param buildFiles the list of resources for relevant pom files.
     * @param activeProfiles teh active Maven profiles
     * @param executionContext the ExecutionContext to use
*    * @param skipMavenParsing skip parsing Maven files
     * @param provenanceMarkers the map of markers to be added
     */
    public Map<Path, Xml.Document>  parseBuildFiles(
            Path baseDir,
            List<Resource> buildFiles,
            List<String> activeProfiles,
            ExecutionContext executionContext,
            boolean skipMavenParsing,
            Map<Path, List<Marker>> provenanceMarkers
    ) {
        Assert.notNull(baseDir, "Base directory must be provided but was null.");
        Assert.notEmpty(buildFiles, "No build files provided.");
        List<Resource> nonPomFiles = retrieveNonPomFiles(buildFiles);
        Assert.isTrue(nonPomFiles.isEmpty(), "Provided resources which are not Maven build files: '%s'".formatted(nonPomFiles.stream().map(r -> ResourceUtil.getPath(r).toAbsolutePath()).toList()));
        List<Resource> resourcesWithoutProvenanceMarker = findResourcesWithoutProvenanceMarker(baseDir, buildFiles, provenanceMarkers);
        Assert.isTrue(resourcesWithoutProvenanceMarker.isEmpty(), "No provenance marker provided for these pom files %s".formatted(resourcesWithoutProvenanceMarker.stream().map(r -> ResourceUtil.getPath(r).toAbsolutePath()).toList()));

        if(skipMavenParsing) {
            return Map.of();
        }

        // 380 : 382
        // already
//        List<Resource> upstreamPoms = collectUpstreamPomFiles(pomFiles);
//        pomFiles.addAll(upstreamPoms);

        // 383
        MavenParser.Builder mavenParserBuilder = MavenParser.builder().mavenConfig(baseDir.resolve(".mvn/maven.config"));

        // 385 : 387
        initializeMavenSettings(executionContext);

        // 389 : 393
        if (parserSettings.isPomCacheEnabled()) {
            //The default pom cache is enabled as a two-layer cache L1 == in-memory and L2 == RocksDb
            //If the flag is set to false, only the default, in-memory cache is used.
            MavenPomCache pomCache = getPomCache();
            MavenExecutionContextView.view(executionContext).setPomCache(pomCache);
        }

        // 395 : 398

        mavenParserBuilder.activeProfiles(activeProfiles.toArray(new String[]{}));

        // 400 : 402
        List<SourceFile> parsedPoms = parsePoms(baseDir, buildFiles, mavenParserBuilder, executionContext);

        parsedPoms = parsedPoms.stream()
                .map(pp -> this.markPomFile(pp, provenanceMarkers.getOrDefault(baseDir.resolve(pp.getSourcePath()), emptyList())))
                .toList();

        // 422 : 436
        Map<Path, Xml.Document> result = createResult(baseDir, buildFiles, parsedPoms);

        // 438 : 444: add marker
//        for (Resource mavenProject : pomFiles) {
//            List<Marker> markers = provenanceMarkers.getOrDefault(mavenProject, emptyList());
//            Xml.Document document = result.get(mavenProject);
//            for (Marker marker : markers) {
//                result.put(mavenProject, document.withMarkers(document.getMarkers().addIfAbsent(marker)));
//            }
//        }

        return result;
    }

    private List<Resource> findResourcesWithoutProvenanceMarker(Path baseDir, List<Resource> buildFileResources, Map<Path, List<Marker>> provenanceMarkers) {
        return buildFileResources.stream()
                .filter(r -> !provenanceMarkers.containsKey(baseDir.resolve(ResourceUtil.getPath(r)).normalize()))
                .toList();
    }

    private static List<Resource> retrieveNonPomFiles(List<Resource> buildFileResources) {
        return buildFileResources.stream().filter(r -> !"pom.xml".equals(ResourceUtil.getPath(r).getFileName().toString())).toList();
    }

    private SourceFile markPomFile(SourceFile pp, List<Marker> markers) {
        for (Marker marker : markers) {
            pp = pp.withMarkers(pp.getMarkers().addIfAbsent(marker));
        }
        return pp;
    }

    private Map<Path, Xml.Document> createResult(Path basePath, List<Resource> pomFiles, List<SourceFile> parsedPoms) {
        return parsedPoms.stream()
                .map(pom -> mapResourceToDocument(basePath, pom, pomFiles))
                .collect(Collectors.toMap(e-> ResourceUtil.getPath(e.getKey()), e -> e.getValue()));
    }

    private Map.Entry<Resource, Xml.Document> mapResourceToDocument(Path basePath, SourceFile pom, List<Resource> parsedPoms) {
        Xml.Document doc = (Xml.Document) pom;
        Resource resource = parsedPoms.stream()
                .filter(p -> ResourceUtil.getPath(p).toString().equals(basePath.resolve(pom.getSourcePath()).toAbsolutePath().normalize().toString()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Could not find matching path for Xml.Document '%s'".formatted(pom.getSourcePath().toAbsolutePath().normalize().toString())));
        return Map.entry(resource, doc);
    }

    private List<SourceFile> parsePoms(Path baseDir, List<Resource> pomFiles, MavenParser.Builder mavenParserBuilder, ExecutionContext executionContext) {
        Iterable<Parser.Input> pomFileInputs = pomFiles.stream()
                .map(p -> new Parser.Input(ResourceUtil.getPath(p), () -> ResourceUtil.getInputStream(p)))
                .toList();
        return mavenParserBuilder.build().parseInputs(pomFileInputs, baseDir, executionContext).toList();
    }

    /**
     * {@link MavenMojoProjectParser##getPomCache()}
     */
    private static MavenPomCache getPomCache() {
//        if (pomCache == null) {
//            if (isJvm64Bit()) {
//                try {
//                    if (pomCacheDirectory == null) {
//                        //Default directory in the RocksdbMavenPomCache is ".rewrite-cache"
//                        pomCache = new CompositeMavenPomCache(
//                                new InMemoryMavenPomCache(),
//                                new RocksdbMavenPomCache(Paths.get(System.getProperty("user.home")))
//                        );
//                    } else {
//                        pomCache = new CompositeMavenPomCache(
//                                new InMemoryMavenPomCache(),
//                                new RocksdbMavenPomCache(Paths.get(pomCacheDirectory))
//                        );
//                    }
//                } catch (Exception e) {
//                    logger.warn("Unable to initialize RocksdbMavenPomCache, falling back to InMemoryMavenPomCache");
//                    logger.debug(e);
//                }
//            } else {
//                logger.warn("RocksdbMavenPomCache is not supported on 32-bit JVM. falling back to InMemoryMavenPomCache");
//            }
//        }
//        if (pomCache == null) {
            MavenPomCache pomCache = new InMemoryMavenPomCache();
//        }
        return pomCache;
    }

    private void initializeMavenSettings(ExecutionContext executionContext) {

    }

    public List<Resource> filterAndSortBuildFiles(List<Resource> resources) {
        return resources.stream()
                .filter(r -> "pom.xml".equals(ResourceUtil.getPath(r).toFile().getName()))
                .filter(r -> filterTestResources(r))
                .sorted((r1, r2) -> {

                    Path r1Path = ResourceUtil.getPath(r1);
                    ArrayList<String> r1PathParts = new ArrayList<>();
                    r1Path.iterator().forEachRemaining(it -> r1PathParts.add(it.toString()));

                    Path r2Path = ResourceUtil.getPath(r2);
                    ArrayList<String> r2PathParts = new ArrayList<>();
                    r2Path.iterator().forEachRemaining(it -> r2PathParts.add(it.toString()));
                    return Integer.compare(r1PathParts.size(), r2PathParts.size());
                })
                .toList();
    }

    private static boolean filterTestResources(Resource r) {
        String path = ResourceUtil.getPath(r).toString();
        boolean underTest = path.contains("src/test");
        if(underTest) {
            log.info("Ignore build file '%s' having 'src/test' in its path indicating it's a build file for tests.".formatted(path));
        }
        return !underTest;
    }

}
