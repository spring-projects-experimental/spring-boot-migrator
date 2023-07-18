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
package org.springframework.sbm.build.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.openrewrite.*;
import org.openrewrite.internal.InMemoryLargeSourceSet;
import org.openrewrite.marker.Markers;
import org.openrewrite.maven.MavenVisitor;
import org.openrewrite.maven.tree.MavenResolutionResult;
import org.openrewrite.xml.tree.Xml;
import org.springframework.sbm.project.resource.ProjectResourceSet;
import org.springframework.sbm.project.resource.RewriteSourceFileHolder;
import org.springframework.sbm.support.openrewrite.GenericOpenRewriteRecipe;

import javax.print.Doc;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class provides a facade to apply OpenRewrite {@code Recipe}s and {@code Visitor}s to the project Maven build files.
 *
 * @author Fabian Krüger
 */
@RequiredArgsConstructor
public class MavenBuildFileRefactoring<T extends SourceFile> {
    private final ProjectResourceSet projectResourceSet;
    private final RewriteMavenParser mavenParser;
    private final ExecutionContext executionContext;

    /**
     * Applies the provided {@code Visitor}s to all Maven build files in the {@code ProjectContext}.
     *
     * The changes are immediately reflected in the wrapping {@code BuildFile}s.
     */
    public void execute(MavenVisitor... visitors) {
        List<Result> results = Arrays.stream(visitors)
                .map(v -> new GenericOpenRewriteRecipe(() -> v))
                .map(this::executeRecipe)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        processResults(results);
    }

    /**
     * Applies the provided {@code Recipe}s to all Maven build files in the {@code ProjectContext}.
     *
     * The changes are immediately reflected in the wrapping {@code BuildFile}s.
     */
    public void execute(Recipe... recipes) {
        for (Recipe recipe : recipes) {
            List<Result> results = executeRecipe(recipe);
            processResults(results);
        }
    }

    /**
     * Applies the provided {@code Recipe}s to the provided Maven build file.
     *
     * The changes are immediately reflected in the wrapping {@code BuildFile}.
     * A caller must decide if refreshing the Pom files in {@code ProjectContext} is required after this method.
     */
    public <V extends TreeVisitor<?, ExecutionContext>> void execute(RewriteSourceFileHolder<Xml.Document> resource, Recipe... recipes) {
        for (Recipe recipe : recipes) {
            List<Result> results = executeRecipe(recipe, resource);
            processResults(results);
        }
    }

    public void refreshPomModels() {
        // store buildfiles and their index in project resource list
        List<BuildFileWithIndex> buildFilesWithIndex = new ArrayList<>();
        List<RewriteSourceFileHolder<? extends SourceFile>> projectResources = projectResourceSet.list();
        for(RewriteSourceFileHolder<? extends SourceFile> sf : projectResources) {
            if(isMavenBuildFile(sf)) {
                int index = projectResources.indexOf(sf);
                Xml.Document xmlDoc = (Xml.Document) sf.getSourceFile();
                buildFilesWithIndex.add(new BuildFileWithIndex(index, (RewriteSourceFileHolder<Xml.Document>) sf));
            }
        }

        // create parser inputs from buildfiles content
        List<Parser.Input> parserInputs = buildFilesWithIndex
                .stream()
                .map(BuildFileWithIndex::getXmlDoc)
                .map(m -> new Parser.Input(m.getSourcePath(), null, () -> new ByteArrayInputStream(
                        m.print().getBytes(StandardCharsets.UTF_8)), !Files.exists(m.getSourcePath())))
                .collect(Collectors.toList());

        // parse buildfiles
        Stream<Xml.Document> newMavenFiles = mavenParser.parseInputs(parserInputs, null, executionContext)
                .filter(Xml.Document.class::isInstance)
                .map(Xml.Document.class::cast);

        // replace new model in build files
        newMavenFiles.forEach(mf -> {
                    replaceModelInBuildFile(projectResources, buildFilesWithIndex, newMavenFiles.toList(), mf);
                });
    }

    private void replaceModelInBuildFile(
            List<RewriteSourceFileHolder<? extends SourceFile>> projectResources,
            List<BuildFileWithIndex> buildFilesWithIndex,
            List<Xml.Document> newMavenFiles,
            Xml.Document mf
    ) {
        // get index in list of build files
        int indexInNewMavenFiles = newMavenFiles.indexOf(mf);
        RewriteSourceFileHolder<Xml.Document> originalPom = buildFilesWithIndex.get(indexInNewMavenFiles).getXmlDoc();
        int indexInProjectResources = projectResources.indexOf(originalPom);
        // replace marker
        Markers markers = originalPom.getSourceFile().getMarkers().removeByType(MavenResolutionResult.class);
        MavenResolutionResult updatedModel = mf.getMarkers().findFirst(MavenResolutionResult.class).get();
        markers = markers.addIfAbsent(updatedModel);
        Xml.Document refreshedPom = originalPom.getSourceFile().withMarkers(markers);
        RewriteSourceFileHolder<Xml.Document> rewriteSourceFileHolder = (RewriteSourceFileHolder<Xml.Document>) projectResources.get(indexInProjectResources);
        rewriteSourceFileHolder.replaceWith(refreshedPom);
    }

    private boolean isMavenBuildFile(RewriteSourceFileHolder<? extends SourceFile> sf) {
        return Xml.Document.class.isInstance(sf.getSourceFile()) && Xml.Document.class.cast(sf.getSourceFile()).getMarkers().findFirst(MavenResolutionResult.class).isPresent();
    }

    @Getter
    class BuildFileWithIndex {
        private final int index;
        private final RewriteSourceFileHolder<Xml.Document> xmlDoc;

        public BuildFileWithIndex(int index, RewriteSourceFileHolder<Xml.Document> xmlDoc) {

            this.index = index;
            this.xmlDoc = xmlDoc;
        }
    }

    private List<Result> executeRecipe(Recipe recipe) {
        List<SourceFile> documentsWrappedInOpenRewriteMavenBuildFile = getDocumentsWrappedInOpenRewriteMavenBuildFile()
                .stream()
                .filter(SourceFile.class::isInstance)
                .map(SourceFile.class::cast)
                .toList();
        List<Result> results = recipe.run(new InMemoryLargeSourceSet(documentsWrappedInOpenRewriteMavenBuildFile), executionContext).getChangeset().getAllResults();
        return results;
    }

    private List<Result> executeRecipe(Recipe recipe, RewriteSourceFileHolder<Xml.Document> resource) {
        List<Result> results = recipe.run(new InMemoryLargeSourceSet(List.of(resource.getSourceFile())), executionContext).getChangeset().getAllResults();
        return results;
    }

    private List<Xml.Document> getDocumentsWrappedInOpenRewriteMavenBuildFile() {
        return getOpenRewriteMavenBuildFiles()
                .stream()
                .map(bf -> bf.getSourceFile())
                .collect(Collectors.toList());
    }

    @NotNull
    private List<OpenRewriteMavenBuildFile> getOpenRewriteMavenBuildFiles() {
        return this.projectResourceSet
                .stream()
                .filter(r -> OpenRewriteMavenBuildFile.class.isInstance(r))
                .map(OpenRewriteMavenBuildFile.class::cast)
                .collect(Collectors.toList());
    }

    private void processResults(List<Result> results) {
        if (!results.isEmpty()) {
            results.forEach(r -> {
                if(!(r.getAfter() instanceof Xml.Document)) {
                    throw new RuntimeException("Return type of refactoring result is not Xml.Document but " + r.getAfter().getClass() + " with content: \n" + r.getAfter().printAll());
                }
                OpenRewriteMavenBuildFile openRewriteMavenBuildFile = getOpenRewriteMavenBuildFiles()
                        .stream()
                        .filter(bf -> bf.getSourceFile().getId().equals(r.getAfter().getId()))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Could not find a BuildFile that wraps Xml.Document with id '%s' in the Result.".formatted(r.getAfter().getId())));
                openRewriteMavenBuildFile.replaceWith((Xml.Document) r.getAfter());
            });
        }
    }

}
