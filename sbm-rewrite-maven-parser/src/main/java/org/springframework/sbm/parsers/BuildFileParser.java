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
package org.springframework.sbm.parsers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.model.Model;
import org.apache.maven.plugin.logging.Log;
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
import java.util.*;
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

    private final MavenModelReader mavenModelReader;
    private final ParserSettings parserSettings;

    /**
     * See {@link org.openrewrite.maven.MavenMojoProjectParser#parseMaven(List, Map, ExecutionContext)}
     */
    public Map<Path, Xml.Document>  parseBuildFiles(Path baseDir, List<Resource> buildFileResources, ExecutionContext executionContext, boolean skipMavenParsing, Map<Path, List<Marker>> provenanceMarkers) {
        Assert.notNull(baseDir, "Base directory must be provided but was null.");
        Assert.notEmpty(buildFileResources, "No build files provided.");
        if(skipMavenParsing) {
            return Map.of();
        }

        List<Resource> pomFiles = new ArrayList<>();
        pomFiles.addAll(buildFileResources);

        Resource topLevelPom = pomFiles.get(0);
        Model topLevelModel = new MavenModelReader().readModel(topLevelPom);

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
        List<String> activeProfiles = readActiveProfiles(topLevelModel);
        mavenParserBuilder.activeProfiles(activeProfiles.toArray(new String[]{}));

        // 400 : 402
        List<SourceFile> parsedPoms = parsePoms(baseDir, pomFiles, mavenParserBuilder, executionContext);

        parsedPoms = parsedPoms.stream()
                .map(pp -> this.markPomFile(pp, provenanceMarkers.getOrDefault(baseDir.resolve(pp.getSourcePath()), emptyList())))
                .toList();

        // 422 : 436
        Map<Path, Xml.Document> result = createResult(baseDir, pomFiles, parsedPoms);

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



//        return pomFiles.stream()
//                .map(pom -> mapResourceToDocument(basePath, pom, parsedPoms))
//                .sorted(this::sortMap)
//                .collect(Collectors.toMap(l -> l.getKey(), l -> l.getValue()));
    }

    private int sortMap(Map.Entry<Resource, Xml.Document> e1, Map.Entry<Resource, Xml.Document> e2) {
        Path path1 = ResourceUtil.getPath(e1.getKey());
        Path path2 = ResourceUtil.getPath(e2.getKey());
        return path1.compareTo(path2);
    }


    private Map.Entry<Resource, Xml.Document> mapResourceToDocument(Path basePath, SourceFile pom, List<Resource> parsedPoms) {
        Xml.Document doc = (Xml.Document) pom;
        Resource resource = parsedPoms.stream()
                .filter(p -> ResourceUtil.getPath(p).toString().equals(basePath.resolve(pom.getSourcePath()).toAbsolutePath().normalize().toString()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Could not find matching path for Xml.Document '%s'".formatted(pom.getSourcePath().toAbsolutePath().normalize().toString())));
        return Map.entry(resource, doc);
    }

    private static Map.Entry<Resource, Xml.Document> mapResourceToDocument(Path basePath, Resource pom, List<SourceFile> parsedPoms) {
        Xml.Document sourceFile = parsedPoms
                .stream()
                .filter(p -> basePath.resolve(p.getSourcePath()).normalize().toString().equals(ResourceUtil.getPath(pom).toString()))
                .filter(Xml.Document.class::isInstance)
                .map(Xml.Document.class::cast)
                .findFirst()
                .get();
        return Map.entry(pom, sourceFile);
    }

    private List<SourceFile> parsePoms(Path baseDir, List<Resource> pomFiles, MavenParser.Builder mavenParserBuilder, ExecutionContext executionContext) {
        Iterable<Parser.Input> pomFileInputs = pomFiles.stream()
                .map(p -> new Parser.Input(ResourceUtil.getPath(p), () -> ResourceUtil.getInputStream(p)))
                .toList();
        return mavenParserBuilder.build().parseInputs(pomFileInputs, baseDir, executionContext).toList();
    }

    private List<String> readActiveProfiles(Model topLevelModel) {
        return parserSettings.getActiveProfiles() != null ? parserSettings.getActiveProfiles() : List.of("default");
    }

    /**
     * {@link MavenMojoProjectParser#getPomCache(String, Log)}
     */
    private static MavenPomCache getPomCache() {
        // FIXME: Provide a way to initialize the MavenTypeCache from properties
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

    private List<Resource> collectUpstreamPomFiles(List<Resource> pomFiles) {
//        pomFiles.stream()
//                .map(mavenModelReader::readModel)
//                .map(Model::)
//        return pomFiles;
        // FIXME: implement
        return null;
    }

    public List<Resource> filterAndSortBuildFiles(List<Resource> resources) {
        return resources.stream()
                .filter(r -> "pom.xml".equals(ResourceUtil.getPath(r).toFile().getName()))
                .filter(r -> filterTestResources(r))
                .sorted((r1, r2) -> {

                    Path r1Path = ResourceUtil.getPath(r1);
                    ArrayList<String> r1PathParts = new ArrayList<String>();
                    r1Path.iterator().forEachRemaining(it -> r1PathParts.add(it.toString()));

                    Path r2Path = ResourceUtil.getPath(r2);
                    ArrayList<String> r2PathParts = new ArrayList<String>();
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
