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
import org.openrewrite.Tree;
import org.openrewrite.internal.lang.Nullable;
import org.openrewrite.java.JavaParser;
import org.openrewrite.java.internal.JavaTypeCache;
import org.openrewrite.java.marker.JavaSourceSet;
import org.openrewrite.marker.Generated;
import org.openrewrite.marker.Marker;
import org.openrewrite.marker.Markers;
import org.openrewrite.xml.tree.Xml;
import org.springframework.core.io.Resource;
import org.springframework.sbm.utils.ResourceUtil;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Fabian Krüger
 */
@Slf4j
public class HelperWithoutAGoodName {
    /**
     * {@link MavenMojoProjectParser#addProvenance(Path, List, Collection)}
     */
    public <T extends
            SourceFile> UnaryOperator<T> addProvenance(Path baseDir, List<Marker> provenance, @Nullable Collection<Path> generatedSources) {
//        MavenMojoProjectParser mavenMojoProjectParser = createMavenMojoProjectParser(baseDir);
//        Method method = ReflectionUtils.findMethod(MavenMojoProjectParser.class, "addProvenance", Path.class, List.class, Collection.class);
//        ReflectionUtils.makeAccessible(method);
//        if(method == null) {
//            throw new IllegalStateException("Could not find method '%s' on %s while trying to call it.".formatted("addProvenance", MavenMojoProjectParser.class.getName()));
//        }
//        Object result = ReflectionUtils.invokeMethod(method, mavenMojoProjectParser, baseDir, provenance, generatedSources);
//        return (UnaryOperator<T>) result;
        return (s) -> {
            Markers markers = s.getMarkers();

            Marker marker;
            for (Iterator var5 = provenance.iterator(); var5.hasNext(); markers = markers.addIfAbsent(marker)) {
                marker = (Marker) var5.next();
            }

            if (generatedSources != null && generatedSources.contains(baseDir.resolve(s.getSourcePath()))) {
                markers = markers.addIfAbsent(new Generated(Tree.randomId()));
            }

            return (T) s.withMarkers(markers);
        };
    }

    /**
     * process sources in src/main/java of current module.
     */
    public List<SourceFile> processMainSources(
            Path baseDir,
            List<Resource> resources,
            Xml.Document moduleBuildFile,
            JavaParser.Builder<? extends JavaParser, ?> javaParserBuilder,
            RewriteResourceParser rp,
            List<Marker> provenanceMarkers,
            Set<Path> alreadyParsed,
            ExecutionContext executionContext,
            SbmMavenProject currentProject
    ) {
        log.info("Processing main sources in module '%s'".formatted(currentProject.getProjectId()));
        // FIXME: 945
        // Some annotation processors output generated sources to the /target directory. These are added for parsing but
        // should be filtered out of the final SourceFile list.

        List<Resource> mainJavaSources = new ArrayList<>();
        List<Resource> javaSourcesInTarget = currentProject.getJavaSourcesInTarget(); // listJavaSources(resources, currentProject.getBasedir().resolve(currentProject.getBuildDirectory()));
        List<Resource> javaSourcesInMain = currentProject.getMainJavaSources(); // listJavaSources(resources, currentProject.getBasedir().resolve(currentProject.getSourceDirectory()));
        mainJavaSources.addAll(javaSourcesInTarget);
        mainJavaSources.addAll(javaSourcesInMain);

        log.info("[%s] Parsing source files".formatted(currentProject));

        // FIXME 945 classpath
        // - Resolve dependencies to non-reactor projects from Maven repository
        // - Resolve dependencies to reactor projects by providing the sources
        // javaParserBuilder.classpath(byte[])

        // we're processing a module here. The classpath of the module consists of all declared dependencies and their transitive dependencies too.
        // For dependencies to projects that belong to the current rector...
        // They'd either need to be built with Maven before to guarantee that the jars are installed to local Maven repo.
        // Or, the classpath must be created from the sources of the project.


        List<Path> dependencies = currentProject.getCompileClasspathElements();

        javaParserBuilder.classpath(dependencies);

        JavaTypeCache typeCache = new JavaTypeCache();
        javaParserBuilder.typeCache(typeCache);

        Iterable<Parser.Input> inputs = mainJavaSources.stream()
                .map(r -> new Parser.Input(ResourceUtil.getPath(r), () -> ResourceUtil.getInputStream(r)))
                .toList();

        Stream<? extends SourceFile> cus = Stream.of(javaParserBuilder)
                .map(JavaParser.Builder::build)
                .flatMap(parser -> parser.parseInputs(inputs, baseDir, executionContext))
                .peek(s -> alreadyParsed.add(baseDir.resolve(s.getSourcePath())));

        List<Marker> mainProjectProvenance = new ArrayList<>(provenanceMarkers);
        mainProjectProvenance.add(sourceSet("main", dependencies, typeCache));

        // FIXME: 945 Why target and not all main?
        List<Path> parsedJavaPaths = mainJavaSources.stream().map(ResourceUtil::getPath).toList();
        Stream<SourceFile> parsedJava = cus.map(addProvenance(baseDir, mainProjectProvenance, parsedJavaPaths));
        log.debug("[%s] Scanned %d java source files in main scope.".formatted(currentProject, mainJavaSources.size()));

        //Filter out any generated source files from the returned list, as we do not want to apply the recipe to the
        //generated files.
        Path buildDirectory = Paths.get(currentProject.getBuildDirectory());
        List<SourceFile> sourceFiles = parsedJava
                .filter(s -> !s.getSourcePath().startsWith(buildDirectory))
                .collect(Collectors.toCollection(ArrayList::new));

        int sourcesParsedBefore = alreadyParsed.size();
        alreadyParsed.addAll(parsedJavaPaths);
        List<SourceFile> parsedResourceFiles = rp.parse(currentProject.getModulePath().resolve("src/main/resources"), resources, alreadyParsed)
                .map(addProvenance(baseDir, mainProjectProvenance, null))
                .toList();

        log.debug("[%s] Scanned %d resource files in main scope.".formatted(currentProject, (alreadyParsed.size() - sourcesParsedBefore)));
        // Any resources parsed from "main/resources" should also have the main source set added to them.
        sourceFiles.addAll(parsedResourceFiles);
        return sourceFiles;
    }

    @NotNull
    private static JavaSourceSet sourceSet(String name, List<Path> dependencies, JavaTypeCache typeCache) {
        return JavaSourceSet.build(name, dependencies, typeCache, false);
    }


    /**
     * Calls {@link MavenMojoProjectParser#processTestSources(SbmMavenProject, JavaParser.Builder, ResourceParser, List, Set, ExecutionContext)}
     */
    public List<SourceFile> processTestSources(
            Path baseDir,
            Xml.Document moduleBuildFile,
            JavaParser.Builder<? extends JavaParser, ?> javaParserBuilder,
            RewriteResourceParser rp,
            List<Marker> provenanceMarkers,
            Set<Path> alreadyParsed,
            ExecutionContext executionContext,
            SbmMavenProject currentProject,
            List<Resource> resources
    ) {
        log.info("Processing test sources in module '%s'".formatted(currentProject.getProjectId()));

        List<Path> testDependencies = currentProject.getTestClasspathElements();

        javaParserBuilder.classpath(testDependencies);
        JavaTypeCache typeCache = new JavaTypeCache();
        javaParserBuilder.typeCache(typeCache);

        List<Resource> testJavaSources = listJavaSources(resources, currentProject.getBasedir().resolve(currentProject.getTestSourceDirectory()));
        alreadyParsed.addAll(testJavaSources.stream().map(ResourceUtil::getPath).toList());

        Iterable<Parser.Input> inputs = testJavaSources.stream()
                .map(r -> new Parser.Input(ResourceUtil.getPath(r), () -> ResourceUtil.getInputStream(r)))
                .toList();

        Stream<? extends SourceFile> cus = Stream.of(javaParserBuilder)
                .map(JavaParser.Builder::build)
                .flatMap(parser -> parser.parseInputs(inputs, baseDir, executionContext));

        List<Marker> markers = new ArrayList<>(provenanceMarkers);
        markers.add(sourceSet("test", testDependencies, typeCache));

        Stream<SourceFile> parsedJava = cus.map(addProvenance(baseDir, markers, null));

        log.debug("[%s] Scanned %d java source files in test scope.".formatted(currentProject, testJavaSources.size()));
        Stream<SourceFile> sourceFiles = parsedJava;

        // Any resources parsed from "test/resources" should also have the test source set added to them.
        int sourcesParsedBefore = alreadyParsed.size();
        Stream<SourceFile> parsedResourceFiles = rp.parse(currentProject.getBasedir().resolve("src/test/resources"), resources, alreadyParsed)
                .map(addProvenance(baseDir, markers, null));
        log.debug("[%s] Scanned %d resource files in test scope.".formatted(currentProject, (alreadyParsed.size() - sourcesParsedBefore)));
        sourceFiles = Stream.concat(sourceFiles, parsedResourceFiles);
        List<SourceFile> result = sourceFiles.toList();
        return result;
    }


    // FIXME: 945 take Java sources from resources
    private static List<Resource> listJavaSources(List<Resource> resources, Path sourceDirectory) {
        return resources.stream()
                .filter(whenIn(sourceDirectory))
                .filter(whenFileNameEndsWithJava())
                .toList();
    }

    @NotNull
    private static Predicate<Resource> whenFileNameEndsWithJava() {
        return p -> ResourceUtil.getPath(p).getFileName().toString().endsWith(".java");
    }

    @NotNull
    private static Predicate<Resource> whenIn(Path sourceDirectory) {
        return r -> ResourceUtil.getPath(r).toString().startsWith(sourceDirectory.toString());
    }
}
