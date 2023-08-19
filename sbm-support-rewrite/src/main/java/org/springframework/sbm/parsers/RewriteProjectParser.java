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
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.openrewrite.ExecutionContext;
import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.SourceFile;
import org.openrewrite.marker.Marker;
import org.openrewrite.maven.AbstractRewriteMojo;
import org.openrewrite.maven.MavenExecutionContextView;
import org.openrewrite.maven.MavenMojoProjectParser;
import org.openrewrite.maven.tree.MavenRepository;
import org.openrewrite.style.NamedStyles;
import org.openrewrite.tree.ParsingEventListener;
import org.openrewrite.tree.ParsingExecutionContextView;
import org.openrewrite.xml.tree.Xml;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.Resource;
import org.springframework.sbm.parsers.events.FinishedParsingProjectEvent;
import org.springframework.sbm.parsers.events.StartedParsingProjectEvent;
import org.springframework.sbm.utils.ResourceUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * @author Fabian Krüger
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RewriteProjectParser {

    private static boolean runPerSubmodule = false;
    private final MavenExecutor mavenExecutor;
    private final ProvenanceMarkerFactory provenanceMarkerFactory;
    private final BuildFileParser buildFileParser;
    private final SourceFileParser sourceFileParser;
    private final StyleDetector styleDetector;
    private final ParserSettings parserSettings;
    private final ParsingEventListener parsingEventListener;
    private final ApplicationEventPublisher eventPublisher;
    private final ProjectScanner scanner;


    public RewriteProjectParsingResult parse(Path baseDir) {
        // FIXME: Take ignorePatterns from application properties
        Set<String> ignorePatterns = Set.of("**/.git/**");
        List<Resource> resources = scanner.scan(baseDir, ignorePatterns);
        // FIXME: Take ExecutionContext type from application properties
        return this.parse(baseDir, resources, new InMemoryExecutionContext(t -> {throw new RuntimeException(t);}));
    }

    /**
     * Parse given {@link Resource}s in {@code baseDir} to OpenRewrite AST representation.
     * <p>
     * extract all poms from list of resources
     * sort the list of poms
     * parse all poms
     * - create marker - generateProvenance()
     * Read java version from pom.xml, also checks maven-compiler-plugin settings.
     * Adds markers: BuildEnvironment, GitProvenance, BuildTool, OperatingSystemProvenance, JavaVersion, JavaProject
     * - Parse Maven files - parseMaven()
     * * parse source files
     * - listSourceFiles()
     * Extract source-encoding from poms
     * Create JavaParser with logging and styles
     * Parse resources
     * processMainSources()
     * processTestSources()
     *
     * @see {@link MavenMojoProjectParser#listSourceFiles(MavenProject, List, ExecutionContext)}
     */
    public RewriteProjectParsingResult parse(Path givenBaseDir, List<Resource> resources, ExecutionContext executionContext) {
        if (!givenBaseDir.isAbsolute()) {
            givenBaseDir = givenBaseDir.toAbsolutePath().normalize();
        }
        final Path baseDir = givenBaseDir;
        // FIXME: ... WARN 30694 --- [           main] .m.p.i.DeprecatedCoreExpressionValidator : Parameter 'local' is deprecated core expression; Avoid use of ArtifactRepository type. If you need access to local repository, switch to '${repositorySystemSession}' expression and get LRM from it instead.
        MavenExecutionContextView.view(executionContext).setLocalRepository(new MavenRepository("local", "file://" + Path.of(System.getProperty("user.home")).resolve(".m2/repository"), null, null, false, null, null, null));
        eventPublisher.publishEvent(new StartedParsingProjectEvent(resources));

        ParsingExecutionContextView.view(executionContext).setParsingListener(parsingEventListener);

        // TODO: "runPerSubmodule"
        // TODO: See ConfigurableRewriteMojo#getPlainTextMasks()
        // TODO: where to retrieve styles from? --> see AbstractRewriteMojo#getActiveStyles() & AbstractRewriteMojo#loadStyles()
        List<NamedStyles> styles = List.of();

        // retrieve all pom files from all modules in the active reactor build
        // TODO: Move this to a build file sort and filter component, for now it could use Maven's DefaultGraphBuilder
        //       this requires File to be used and thus binds the component to file access.

        AtomicReference<RewriteProjectParsingResult> atomicReference = new AtomicReference<>();

        withMavenSession(baseDir, mavenSession -> {
            List<MavenProject> sortedProjectsList = mavenSession.getProjectDependencyGraph().getSortedProjects();
            SortedProjects mavenInfos = new SortedProjects(resources, sortedProjectsList, List.of("default"));

//            List<Resource> sortedBuildFileResources = buildFileParser.filterAndSortBuildFiles(resources);

            // generate provenance
            Map<Path, List<Marker>> provenanceMarkers = provenanceMarkerFactory.generateProvenanceMarkers(baseDir, mavenInfos);

            // 127: parse build files
            Map<Path, Xml.Document> resourceToDocumentMap = buildFileParser.parseBuildFiles(baseDir, mavenInfos.getResources(), mavenInfos.getActiveProfiles(), executionContext, parserSettings.isSkipMavenParsing(), provenanceMarkers);

            List<SourceFile> parsedAndSortedBuildFileDocuments = mavenInfos.getResources().stream()
                    .map(r -> resourceToDocumentMap.get(ResourceUtil.getPath(r)))
                    .map(SourceFile.class::cast)
                    .toList();
            // 128 : 131
            log.trace("Start to parse %d source files in %d modules".formatted(resources.size() + resourceToDocumentMap.size(), resourceToDocumentMap.size()));
            Stream<SourceFile> sourceFilesStream = sourceFileParser.parseOtherSourceFiles(baseDir, mavenInfos, resourceToDocumentMap, mavenInfos.getResources(), provenanceMarkers, styles, executionContext);
            List<SourceFile> list = sourceFilesStream.toList();
//        List<SourceFile> sourceFilesWithoutPoms = sourceFilesStream.filter(sf -> resourceToDocumentMap.keySet().contains(baseDir.resolve(sf.getSourcePath()).toAbsolutePath().normalize())).toList();
            List<SourceFile> resultingList = new ArrayList<>(); // sourceFilesStream2.toList();
            resultingList.addAll(parsedAndSortedBuildFileDocuments);
            resultingList.addAll(list);
            List<SourceFile> sourceFiles = styleDetector.sourcesWithAutoDetectedStyles(resultingList.stream());

            eventPublisher.publishEvent(new FinishedParsingProjectEvent(sourceFiles));

            atomicReference.set(new RewriteProjectParsingResult(sourceFiles, executionContext));
        });

        return atomicReference.get();
    }

    private void withMavenSession(Path baseDir, Consumer<MavenSession> consumer) {
        mavenExecutor.onProjectSucceededEvent(baseDir, List.of("clean", "package"), event -> consumer.accept(event.getSession()));
    }

    @org.jetbrains.annotations.Nullable
    private static List<SourceFile> autoDetectStyles(Stream<SourceFile> sourceFilesStream) {
        RewriteMojo dummyRewriteMojo = new RewriteMojo();
        Method sourcesWithAutoDetectedStylesMethod = ReflectionUtils.findMethod(RewriteMojo.class, "sourcesWithAutoDetectedStyles");
        ReflectionUtils.makeAccessible(sourcesWithAutoDetectedStylesMethod);
        Object o = ReflectionUtils.invokeMethod(sourcesWithAutoDetectedStylesMethod, dummyRewriteMojo, sourceFilesStream);
        List<SourceFile> sourceFiles = (List<SourceFile>) o;
        return sourceFiles;
    }

//    private Stream<SourceFile> parseToAst(Path baseDir, List<Resource> resources, List<NamedStyles> styles, ExecutionContext executionContext) throws DependencyResolutionRequiredException, MojoExecutionException {
//        MavenProject mavenProject = createFakeMavenProjectForProvenance(baseDir, resources, executionContext);
//        return super.listSourceFiles(mavenProject, styles, executionContext);
//    }

    private MavenProject createFakeMavenProjectForProvenance(Path baseDir, List<Resource> resources, ExecutionContext executionContext) {
        MavenProject mavenProject = new MavenProject();
        // Plugin compilerPlugin = mavenProject.getPlugin("org.apache.maven.plugins:maven-compiler-plugin");
        mavenProject.setPluginArtifacts(Set.of());
        return mavenProject;
    }

    /**
     * Extending {@code AbstractRewriteMojo} to open up protected method for reuse
     */
    static class RewriteMojo extends AbstractRewriteMojo {

        @Override
        public void execute() throws MojoExecutionException, MojoFailureException {

        }
    }

}
