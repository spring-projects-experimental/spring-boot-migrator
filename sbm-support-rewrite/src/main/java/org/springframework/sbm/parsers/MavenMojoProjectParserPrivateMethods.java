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
import org.apache.maven.rtinfo.internal.DefaultRuntimeInformation;
import org.apache.maven.settings.crypto.DefaultSettingsDecrypter;
import org.jetbrains.annotations.NotNull;
import org.openrewrite.ExecutionContext;
import org.openrewrite.SourceFile;
import org.openrewrite.Tree;
import org.openrewrite.internal.lang.Nullable;
import org.openrewrite.java.JavaParser;
import org.openrewrite.marker.Generated;
import org.openrewrite.marker.Marker;
import org.openrewrite.marker.Markers;
import org.openrewrite.maven.MavenMojoProjectParser;
import org.openrewrite.maven.ResourceParser;
import org.openrewrite.maven.tree.ResolvedDependency;
import org.openrewrite.maven.utilities.MavenArtifactDownloader;
import org.openrewrite.xml.tree.Xml;
import org.sonatype.plexus.components.cipher.DefaultPlexusCipher;
import org.sonatype.plexus.components.cipher.PlexusCipherException;
import org.sonatype.plexus.components.sec.dispatcher.DefaultSecDispatcher;

import java.nio.file.Path;
import java.util.*;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

/**
 * @author Fabian Krüger
 */
@Slf4j
@RequiredArgsConstructor
class MavenMojoProjectParserPrivateMethods {

    private final MavenMojoProjectParserFactory mavenMojoProjectParserFactory;
    private final MavenArtifactDownloader artifactDownloader;

    /**
     */
    public List<SourceFile> processMainSources(Path baseDir, Xml.Document moduleBuildFile, JavaParser.Builder<? extends JavaParser, ?> javaParserBuilder, ResourceParser rp, List<Marker> provenanceMarkers, Set<Path> alreadyParsed, ExecutionContext executionContext, SbmMavenProject sbmMavenProject) {
        // FIXME: 945
        return null;
//        return invokeProcessMethod(baseDir, sbmMavenProject, moduleBuildFile, javaParserBuilder, rp, provenanceMarkers, alreadyParsed, executionContext, "processMainSources");
    }

    /**
     * Calls {@link MavenMojoProjectParser#processTestSources(SbmMavenProject, JavaParser.Builder, ResourceParser, List, Set, ExecutionContext)}
     */
    public List<SourceFile> processTestSources(Path baseDir, Xml.Document moduleBuildFile, JavaParser.Builder<? extends JavaParser,?> javaParserBuilder, ResourceParser rp, List<Marker> provenanceMarkers, Set<Path> alreadyParsed, ExecutionContext executionContext, SbmMavenProject sbmMavenProject) {
        return invokeProcessMethod(baseDir, sbmMavenProject, moduleBuildFile, javaParserBuilder, rp, provenanceMarkers, alreadyParsed, executionContext, "processTestSources");
    }

    /**
     * See {@link MavenMojoProjectParser#processMainSources(SbmMavenProject, JavaParser.Builder, ResourceParser, List, Set, ExecutionContext)}
     */
    @NotNull
    private List<SourceFile> invokeProcessMethod(
            Path baseDir,
            SbmMavenProject sbmMavenProject,
            Xml.Document moduleBuildFile,
            JavaParser.Builder<? extends JavaParser, ?> javaParserBuilder,
            ResourceParser rp,
            List<Marker> provenanceMarkers,
            Set<Path> alreadyParsed,
            ExecutionContext executionContext,
            String methodName
    ) {
        // FIXME: #945
        return null;
//        MavenMojoProjectParser mavenMojoProjectParser = createMavenMojoProjectParser(baseDir);
//        Method method = ReflectionUtils.findMethod(
//                MavenMojoProjectParser.class,
//                methodName,
//                SbmMavenProject.class,
//                JavaParser.Builder.class,
//                ResourceParser.class,
//                List.class,
//                Set.class,
//                ExecutionContext.class);
//        ReflectionUtils.makeAccessible(method);
//        if (method == null) {
//            throw new IllegalStateException("Could not find method '%s' on %s while trying to call it.".formatted(methodName, MavenMojoProjectParser.class.getName()));
//        }
//        log.debug("Starting reflective call to %s.%s()".formatted(mavenMojoProjectParser.getClass().getName(), method.getName()));
//        Object result = ReflectionUtils.invokeMethod(method, mavenMojoProjectParser,
//                sbmMavenProject,
//                javaParserBuilder,
//                rp,
//                provenanceMarkers,
//                alreadyParsed,
//                executionContext
//        );
//        if (result instanceof Stream) {
//            List<SourceFile> sourceFiles = ((Stream<SourceFile>) result).toList();
//            return sourceFiles;
//        } else {
//            throw new RuntimeException("Could not cast result returned from MavenMojoParser#methodName to Stream<SourceFile>.");
//        }
    }


    // TODO: 945 keep but move to a better class
    /**
     * {@link MavenMojoProjectParser#addProvenance(Path, List, Collection)}
     */
    public <T extends SourceFile> UnaryOperator<T> addProvenance(Path baseDir, List<Marker> provenance, @Nullable Collection<Path> generatedSources) {
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
            for(Iterator var5 = provenance.iterator(); var5.hasNext(); markers = markers.addIfAbsent(marker)) {
                marker = (Marker)var5.next();
            }

            if (generatedSources != null && generatedSources.contains(baseDir.resolve(s.getSourcePath()))) {
                markers = markers.addIfAbsent(new Generated(Tree.randomId()));
            }

            return (T) s.withMarkers(markers);
        };
    }

    private MavenMojoProjectParser createMavenMojoProjectParser(Path baseDir) {
        try {
            return mavenMojoProjectParserFactory.create(baseDir, new DefaultRuntimeInformation(), new DefaultSettingsDecrypter(new DefaultSecDispatcher(new DefaultPlexusCipher())));
        } catch (PlexusCipherException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Path> downloadArtifacts(List<ResolvedDependency> dependencies) {

//        eventPublisher.publishEvent(new StartDownloadingDependenciesEvent(dependencies.size()));


        List<Path> paths = dependencies
                .stream()
                .filter(d -> d.getRepository() != null)
//                .peek(d -> eventPublisher.publishEvent(new StartDownloadingDependencyEvent(d.getRequested())))
//                .parallel()
                .map(artifactDownloader::downloadArtifact)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

//        eventPublisher.publishEvent(new FinishedDownloadingDependencies());

        return paths;
    }
}
