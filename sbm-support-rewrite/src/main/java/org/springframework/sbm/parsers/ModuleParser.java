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
import org.openrewrite.FileAttributes;
import org.openrewrite.Parser;
import org.openrewrite.SourceFile;
import org.openrewrite.internal.lang.Nullable;
import org.openrewrite.java.JavaParser;
import org.openrewrite.java.internal.JavaTypeCache;
import org.openrewrite.java.marker.JavaSourceSet;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.JavaType;
import org.openrewrite.marker.Generated;
import org.openrewrite.marker.Marker;
import org.openrewrite.marker.Markers;
import org.openrewrite.xml.tree.Xml;
import org.springframework.core.io.Resource;
import org.springframework.sbm.utils.ResourceUtil;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.openrewrite.Tree.randomId;

/**
 * @author Fabian Krüger
 */
@Slf4j
public class ModuleParser {

    /**
     * Add {@link Marker}s to {@link SourceFile}.
     */
    public <T extends SourceFile> UnaryOperator<T> addProvenance(
            Path baseDir,
            List<Marker> provenance,
            @Nullable Collection<Path> generatedSources
    ) {
        return s -> {
            Markers markers = s.getMarkers();
            for (Marker marker : provenance) {
                markers = markers.addIfAbsent(marker);
            }
            if (generatedSources != null && generatedSources.contains(baseDir.resolve(s.getSourcePath()))) {
                markers = markers.addIfAbsent(new Generated(randomId()));
            }
            return s.withMarkers(markers);
        };
    }

    /**
     * Parse Java sources and resources under {@code src/main} of current module.
     */
    public SourceSetParsingResult processMainSources(
            Path baseDir,
            List<Resource> resources,
            Xml.Document moduleBuildFile,
            JavaParser.Builder<? extends JavaParser, ?> javaParserBuilder,
            RewriteResourceParser rp,
            List<Marker> provenanceMarkers,
            Set<Path> alreadyParsed,
            ExecutionContext executionContext,
            MavenProject currentProject
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
                .map(r -> {
                    FileAttributes fileAttributes = null;
                    Path path = ResourceUtil.getPath(r);
                    boolean isSynthetic = Files.exists(path);
                    Supplier<InputStream> inputStreamSupplier = () -> ResourceUtil.getInputStream(r);
                    Parser.Input input = new Parser.Input(path, fileAttributes, inputStreamSupplier, isSynthetic);
                    return input;
                })
                .toList();

        List<JavaType.FullyQualified> localClassesCp = new ArrayList<>();
        JavaSourceSet javaSourceSet = sourceSet("main", dependencies, typeCache);
        List<? extends SourceFile> cus = javaParserBuilder.build()
                .parseInputs(inputs, baseDir, executionContext)
                .peek(s -> {
                    ((J.CompilationUnit)s).getClasses()
                            .stream()
                            .map(J.ClassDeclaration::getType)
                            .forEach(localClassesCp::add);

                    alreadyParsed.add(baseDir.resolve(s.getSourcePath()));
                })
                .toList();

        // TODO: This is a hack:
        // Parsed java sources are not themselves on the classpath (here).
        // The actual parsing happens when the stream is terminated (toList),
        // therefore the toList() must be called before the parsed compilation units can be added to the classpath
        List<Marker> mainProjectProvenance = new ArrayList<>(provenanceMarkers);
        javaSourceSet = createJavaSourceSetMarker(localClassesCp, javaSourceSet);
        mainProjectProvenance.add(javaSourceSet);

        List<Path> parsedJavaPaths = javaSourcesInTarget.stream().map(ResourceUtil::getPath).toList();
        Stream<SourceFile> parsedJava = cus.stream().map(addProvenance(baseDir, mainProjectProvenance, parsedJavaPaths));
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
        return new SourceSetParsingResult(sourceFiles, javaSourceSet.getClasspath());
    }

    @NotNull
    private static JavaSourceSet createJavaSourceSetMarker(List<JavaType.FullyQualified> localClassesCp, JavaSourceSet javaSourceSet) {
        List<JavaType.FullyQualified> mainCp = javaSourceSet.getClasspath();
        mainCp.addAll(localClassesCp);
        javaSourceSet = javaSourceSet.withClasspath(mainCp);
        return javaSourceSet;
    }

    @NotNull
    private static JavaSourceSet sourceSet(String name, List<Path> dependencies, JavaTypeCache typeCache) {
        return JavaSourceSet.build(name, dependencies, typeCache, false);
    }


    /**
     * Parse Java sources and resource files under {@code src/test}.
     */
    public SourceSetParsingResult processTestSources(
            Path baseDir,
            Xml.Document moduleBuildFile,
            JavaParser.Builder<? extends JavaParser, ?> javaParserBuilder,
            RewriteResourceParser rp,
            List<Marker> provenanceMarkers,
            Set<Path> alreadyParsed,
            ExecutionContext executionContext,
            MavenProject currentProject,
            List<Resource> resources,
            List<JavaType.FullyQualified> classpath) {
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

        final List<JavaType.FullyQualified> localClassesCp = new ArrayList<>();
        List<? extends SourceFile> cus = javaParserBuilder.build()
                .parseInputs(inputs, baseDir, executionContext)
                .peek(s -> {
                    ((J.CompilationUnit) s).getClasses()
                            .stream()
                            .map(J.ClassDeclaration::getType)
                            .forEach(localClassesCp::add);
                    alreadyParsed.add(baseDir.resolve(s.getSourcePath()));
                })
                .toList();

        List<Marker> markers = new ArrayList<>(provenanceMarkers);

        JavaSourceSet javaSourceSet = sourceSet("test", testDependencies, typeCache);
        List<JavaType.FullyQualified> curClasspath = Stream.concat(classpath.stream(), localClassesCp.stream()).toList();
        javaSourceSet = createJavaSourceSetMarker(curClasspath, javaSourceSet);
        markers.add(javaSourceSet);
        Stream<SourceFile> parsedJava = cus.stream().map(addProvenance(baseDir, markers, null));

        log.debug("[%s] Scanned %d java source files in test scope.".formatted(currentProject, testJavaSources.size()));
        Stream<SourceFile> sourceFiles = parsedJava;

        // Any resources parsed from "test/resources" should also have the test source set added to them.
        int sourcesParsedBefore = alreadyParsed.size();
        Stream<SourceFile> parsedResourceFiles = rp.parse(currentProject.getBasedir().resolve("src/test/resources"), resources, alreadyParsed)
                .map(addProvenance(baseDir, markers, null));
        log.debug("[%s] Scanned %d resource files in test scope.".formatted(currentProject, (alreadyParsed.size() - sourcesParsedBefore)));
        sourceFiles = Stream.concat(sourceFiles, parsedResourceFiles);
        List<SourceFile> result = sourceFiles.toList();
        return new SourceSetParsingResult(result, javaSourceSet.getClasspath());
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
