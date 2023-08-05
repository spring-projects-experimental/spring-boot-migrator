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
package org.springframework.sbm.build.impl;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.openrewrite.*;
import org.openrewrite.internal.InMemoryLargeSourceSet;
import org.openrewrite.internal.lang.Nullable;
import org.openrewrite.marker.Markers;
import org.openrewrite.maven.*;
import org.openrewrite.maven.tree.*;
import org.openrewrite.xml.tree.Xml;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.sbm.build.api.BuildFile;
import org.springframework.sbm.build.api.DependenciesChangedEvent;
import org.springframework.sbm.build.api.Dependency;
import org.springframework.sbm.build.api.ParentDeclaration;
import org.springframework.sbm.build.api.Plugin;
import org.springframework.sbm.build.api.RepositoryDefinition;
import org.springframework.sbm.build.api.RewriteMavenParentDeclaration;
import org.springframework.sbm.build.impl.inner.PluginRepositoryHandler;
import org.springframework.sbm.build.migration.recipe.AddMavenPlugin;
import org.springframework.sbm.build.migration.recipe.RemoveMavenPlugin;
import org.springframework.sbm.build.migration.visitor.AddOrUpdateDependencyManagement;
import org.springframework.sbm.java.impl.ClasspathRegistry;
import org.springframework.sbm.openrewrite.RewriteExecutionContext;
import org.springframework.sbm.project.resource.RewriteSourceFileHolder;
import org.springframework.sbm.parsers.RewriteMavenArtifactDownloader;
import org.springframework.sbm.support.openrewrite.GenericOpenRewriteRecipe;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

@Slf4j
public class OpenRewriteMavenBuildFile extends RewriteSourceFileHolder<Xml.Document> implements BuildFile {

    private final ApplicationEventPublisher eventPublisher;
    private final PluginRepositoryHandler pluginRepositoryHandler = new PluginRepositoryHandler();
	private final MavenBuildFileRefactoring<Xml.Document> refactoring;

    public OpenRewriteMavenBuildFile(Path projectRootDirectory, SourceFile maven, ApplicationEventPublisher eventPublisher, ExecutionContext executionContext, MavenBuildFileRefactoring refactoring) {
        this(projectRootDirectory, cast(maven), eventPublisher, executionContext, refactoring);
    }

    private static Xml.Document cast(SourceFile maven) {
        if(Xml.Document.class.isInstance(maven)) {
            return Xml.Document.class.cast(maven);
        } else {
            throw new IllegalArgumentException("Provided maven was not of expected type Xml.Document but was '%s'".formatted(maven.getClass()));
        }
    }

    // Execute separately since RefreshPomModel caches the refreshed maven files after the first visit
    public static class RefreshPomModel extends Recipe {

        private List<SourceFile> sourceFiles;

        @Deprecated(forRemoval = true)
        protected List<SourceFile> visit(List<SourceFile> before, ExecutionContext ctx) {
            if (sourceFiles == null) {
                List<SourceFile> nonMavenFiles = new ArrayList<>(before.size());
                List<Xml.Document> mavenFiles = new ArrayList<>();
                for (SourceFile f : before) {
                    if (f instanceof Xml.Document) {
                        mavenFiles.add((Xml.Document) f);
                    } else {
                        nonMavenFiles.add(f);
                    }
                }
                MavenParser mavenParser = MavenParser.builder().build();
                List<Parser.Input> parserInput = mavenFiles.stream()
                        .map(m -> new Parser.Input(
                                        m.getSourcePath(),
                                        null,
                                        () -> new ByteArrayInputStream(m.printAll().getBytes(StandardCharsets.UTF_8)),
                                        !Files.exists(m.getSourcePath())
                                )
                        )
                        .collect(Collectors.toList());
                List<Xml.Document> newMavenFiles = mavenParser.parseInputs(parserInput, null, ctx)
                        .filter(Xml.Document.class::isInstance)
                        .map(Xml.Document.class::cast)
                        .toList();

                for (int i = 0; i < newMavenFiles.size(); i++) {
                    Optional<MavenResolutionResult> mavenModels = MavenBuildFileUtil.findMavenResolution(mavenFiles.get(i));
                    Optional<MavenResolutionResult> newMavenModels = MavenBuildFileUtil.findMavenResolution(newMavenFiles.get(i));
                    mavenFiles.get(i).withMarkers(Markers.build(List.of(newMavenModels.get())));
                    // FIXME: 497 verify correctness
                    mavenFiles.set(i, newMavenFiles.get(i));
                }

                sourceFiles = nonMavenFiles;
                sourceFiles.addAll(mavenFiles);
            }
            return sourceFiles;
        }

        @Override
        public String toString() {
            return "REFRESH_POM_MODEL_RECIPE";
        }

        @Override
        public String getDisplayName() {
            return "Refresh POM model";
        }

        @Override
        public String getDescription() {
            return getDisplayName();
        }

    }

    public static final Path JAVA_SOURCE_FOLDER = Path.of("src/main/java");
    public static final Path JAVA_TEST_SOURCE_FOLDER = Path.of("src/test/java");
    private static final Path RESOURCE_FOLDER = Path.of("src/main/resources");
    private static final Path RESOURCE_TEST_FOLDER = Path.of("src/test/resources");

    private final ExecutionContext executionContext;


    public OpenRewriteMavenBuildFile(Path absoluteProjectPath,
                                     Xml.Document sourceFile,
                                     ApplicationEventPublisher eventPublisher,
                                     ExecutionContext executionContext,
                                     MavenBuildFileRefactoring refactoring) {
        super(absoluteProjectPath, sourceFile);
        this.eventPublisher = eventPublisher;
        this.executionContext = executionContext;
        this.refactoring = refactoring;
    }

    public void apply(Recipe recipe) {
        // FIXME: #7 Make ExecutionContext a Spring Bean and caching configurable, also if the project root is used as workdir it must be added to .gitignore
        //executionContext.putMessage("org.openrewrite.maven.pomCache", new RocksdbMavenPomCache(this.getAbsoluteProjectDir()));
		refactoring.execute(recipe);
    }

    public MavenResolutionResult getPom() {
        return MavenBuildFileUtil.findMavenResolution(getSourceFile()).get();
    }

	public RewriteSourceFileHolder<Xml.Document> getResource() {
		return this;
	}

    @Override
    public void addDependency(Dependency dependency) {
        if (!containsDependency(dependency)) {
            addDependencyInner(dependency);
            eventPublisher.publishEvent(new DependenciesChangedEvent(getResolvedDependenciesPaths()));
        }
    }

    private boolean containsDependency(Dependency dependency) {
        List<ResolvedDependency> listToSearch;

        Map<Scope, List<ResolvedDependency>> projectDependencies = getPom().getDependencies();

        listToSearch = dependency.getScope() == null ? projectDependencies.get(Scope.Compile) :
                projectDependencies.get(Scope.fromName(dependency.getScope()));

        return listToSearch
                .stream()
                .anyMatch(
                        d -> d.getArtifactId().equals(dependency.getArtifactId())
                                && d.getGroupId().equals(dependency.getGroupId())
                                && d.getVersion().equals(dependency.getVersion())
                );
    }

    @Override
    public void addDependencies(List<Dependency> dependencies) {
        addDependenciesInner(dependencies);
        eventPublisher.publishEvent(new DependenciesChangedEvent(getResolvedDependenciesPaths()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasDeclaredDependencyMatchingRegex(String... dependencyPatterns) {
        return getDeclaredDependencies().stream()
                .map(d -> d.getCoordinates())
                .anyMatch(dc -> Arrays.stream(dependencyPatterns).anyMatch(r -> dc.matches(r)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasEffectiveDependencyMatchingRegex(String... dependencyPatterns) {
        return getEffectiveDependencies().stream()
                .map(d -> d.getCoordinates())
                .anyMatch(dc -> Arrays.stream(dependencyPatterns).anyMatch(r -> dc.matches(r)));
    }

    @Override
    public boolean hasExactDeclaredDependency(Dependency dependency) {
        return getDeclaredDependencies().stream()
                .anyMatch(d -> d.equals(dependency));
    }

    /**
     * Retrieve dependencies declared in buildfile with version and scope from dependency management if not explicitly declared.
     *
     * Given this pom.xml and a call without any given `scope` parameter
     *
     * [source,xml]
     * ----
     * <dependencyManagement>
     *         <dependencies>
     *            <dependency>
     *                 <groupId>org.junit.jupiter</groupId>
     *                 <artifactId>junit-jupiter</artifactId>
     *                 <version>5.7.1</version>
     *                 <scope>test</scope>
     *             </dependency>
     *         </dependencies>
     *     </dependencyManagement>
     *     <dependencies>
     *         <dependency>
     *             <groupId>org.junit.jupiter</groupId>
     *             <artifactId>junit-jupiter</artifactId>
     *         </dependency>
     *     </dependencies>
     * ----
     *
     * a dependency `org.junit.jupiter:junit-jupiter:5.7.1` with scope `test` will be returned.
     *
     * TODO: tests...
     * - with all scopes
     * - Managed versions with type and classifier given
     * - exclusions
     * - type
     */
    @Override
    public List<Dependency> getDeclaredDependencies(Scope... scopes) {
        // returns dependencies as declared in xml
        List<org.openrewrite.maven.tree.Dependency> requestedDependencies = getPom().getPom().getRequestedDependencies();
        // FIXME: #7 use getPom().getDependencies() instead ?
        return requestedDependencies.stream()
                .filter(d -> {
                    if(scopes.length == 0) {
                        return true;
                    } else {
                        // FIXME: scope test should also return compile!
                        return Arrays.stream(scopes).anyMatch(scope -> {
                            String effectiveScope = d.getScope() == null ? "compile" : d.getScope();
                            return scope.toString().equalsIgnoreCase(effectiveScope);
                        });
                    }
                })
                .map(this::mapDependency)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Dependency> getRequestedDependencies() {
        List<org.openrewrite.maven.tree.Dependency> requestedDependencies = getPom().getPom().getRequestedDependencies();
        // FIXME: #7 use getPom().getDependencies() instead ?
        List<Dependency> declaredDependenciesWithEffectiveVersions = requestedDependencies.stream()
                .map(d -> mapDependency(d))
                .map(d -> {
                    if(d.getType() == null || d.getClassifier() == null || d.getVersion() == null) {

                        // resolve values for properties like ${my.artifactId} or ${dep.version}
                        String resolvedGroupId = resolve(d.getGroupId());
                        String resolvedArtifactId = resolve(d.getArtifactId());
                        String resolvedVersion = resolve(d.getVersion());

                        List<ResolvedDependency> dependencies = getPom().findDependencies(
                                resolvedGroupId,
                                resolvedArtifactId,
                                d.getScope() != null ? Scope.fromName(d.getScope()) : null
                        );
                        if (dependencies.isEmpty()) {
                            // requested dependency from another module in this multi-module project won't be resolvable
                            d.setGroupId(resolvedGroupId);
                            d.setArtifactId(resolvedArtifactId);
                            d.setVersion(resolvedVersion);
                        }
                        else {
                            ResolvedDependency resolvedDependency = dependencies.get(0);
                            d.setGroupId(resolvedGroupId);
                            d.setArtifactId(resolvedArtifactId);
                            d.setVersion(resolvedDependency.getVersion());
                            d.setClassifier(resolvedDependency.getClassifier());
                            d.setType(resolvedDependency.getType());
                        }


                        if(d.getScope() == null ) {
                            String s = resolveScope(resolvedGroupId, resolvedArtifactId, d.getType(), d.getClassifier());
                            if(s == null) {
                                s = "compile";
                            }
                            d.setScope(s.toLowerCase());
                        }
                    }
                    return d;
                })
                .collect(Collectors.toList());
        return declaredDependenciesWithEffectiveVersions;
    }

    /**
     * {@inheritDoc}
     *
     * TODO: #497 Test with declared and transitive dependencies
     */
    @Override
    public Set<Dependency> getEffectiveDependencies(Scope scope) {
        return getPom().getDependencies().get(scope)
                .stream()
                .map(d -> mapDependency(scope, d))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Dependency> getEffectiveDependencies() {
        Set<Dependency> collect = getPom()
                .getDependencies()
                .get(Scope.Compile)
                .stream()
                .map(d -> mapDependency(Scope.Compile, d))
                .collect(Collectors.toSet());

        getPom()
                .getDependencies()
                .get(Scope.Provided)
                .stream()
                .map(d -> mapDependency(Scope.Provided, d))
                .forEach(d -> collect.add(d));

        getPom()
                .getDependencies()
                .get(Scope.Test)
                .stream()
                .map(d -> mapDependency(Scope.Test, d))
                .forEach(d -> collect.add(d));

        return collect;
    }

    @Override
    public void removeDependencies(List<Dependency> dependencies) {
        removeDependenciesInner(dependencies);

        eventPublisher.publishEvent(new DependenciesChangedEvent(getResolvedDependenciesPaths()));
    }

    /**
     * Removes all dependencies matching given regex.
     *
     * Example: {@code "com\\.acme\\:artifact\\.id\\:.*"} matches all versions of {@code com.acme:artifact.id}
     *
     * @param regex varargs matching dependency coordinates `groupId:artifactId:version`
     */
    @Override
    public void removeDependenciesMatchingRegex(String... regex) {
        List<Dependency> dependenciesMatching = getDeclaredDependencies().stream()
                .filter(c -> Arrays.stream(regex).anyMatch(r -> c.getCoordinates().matches(r)))
                .collect(Collectors.toList());
        removeDependenciesInner(dependenciesMatching);
        eventPublisher.publishEvent(new DependenciesChangedEvent(getResolvedDependenciesPaths()));
    }

    @Override
    public void addToDependencyManagement(Dependency dependency) {
        addToDependencyManagementInner(dependency);
        eventPublisher.publishEvent(new DependenciesChangedEvent(getResolvedDependenciesPaths()));
    }

    private org.springframework.sbm.build.api.Dependency mapDependency(org.openrewrite.maven.tree.Dependency d) {
        Dependency.DependencyBuilder dependencyBuilder = Dependency.builder()
                .groupId(d.getGroupId())
                .artifactId(d.getArtifactId())
                .version(d.getVersion())
                .scope(d.getScope())
                .type(d.getType());
        if (d.getExclusions() != null && !d.getExclusions().isEmpty()) {
            dependencyBuilder.exclusions(d.getExclusions().stream()
                    .map(e -> Dependency.builder().groupId(e.getGroupId()).artifactId(e.getArtifactId()).build())
                    .collect(Collectors.toList()));
        }
        return dependencyBuilder.build();
    }

    private String resolveScope(String groupId, String artifactId, @Nullable String type, @Nullable String classifier) {
        Scope managedScope = getPom().getPom().getManagedScope(groupId, artifactId, type, classifier);
        return managedScope != null ? managedScope.name().toLowerCase() : "compile";
    }

    private org.springframework.sbm.build.api.Dependency mapDependency(Scope scope, ResolvedDependency d) {
        return new Dependency(
                d.getGroupId(),
                d.getArtifactId(),
                d.getVersion(),
                d.getType(),
                scope.name().toLowerCase(),
                d.getClassifier(),
                d.getRequested().getExclusions() != null ?
                    d.getRequested().getExclusions()
                            .stream()
                            .map(e -> Dependency.builder().groupId(e.getGroupId()).artifactId(e.getArtifactId()).build())
                            .collect(Collectors.toList())
                        :
                        List.of()
        );
    }

    public void addDependencyInner(Dependency dependency) {
        addDependenciesInner(List.of(dependency));
    }

    private String scopeString(Scope scope) {
        //TODO: kdv is this really a good way to represent the 'scope' ?
        return scope == null ? null : scope.toString().toLowerCase();
    }

    protected void addDependenciesInner(List<Dependency> dependencies) {
        if (!dependencies.isEmpty()) {
            Recipe r = getAddDependencyRecipe(dependencies.get(0));
            dependencies.stream().skip(1).forEach(d -> r.getRecipeList().add(getAddDependencyRecipe(d)));
            apply(r, getResource());
            refreshPomModel();
            List<Dependency> exclusions = dependencies.stream()
                    .filter(not(d -> d.getExclusions().isEmpty()))
                    .flatMap(d -> d.getExclusions().stream())
                    .collect(Collectors.toList());

            excludeDependenciesInner(exclusions);

            updateClasspathRegistry();
        }
    }

    private void refreshPomModel() {
//        apply(new GenericOpenRewriteRecipe<>(() -> new UpdateMavenModel<>()));
        refactoring.refreshPomModels();
    }

    /**
     * Does not updateClasspathRegistry
     */
    private void excludeDependenciesInner(List<Dependency> exclusions) {
        if (!exclusions.isEmpty()) {
            Dependency excludedDependency = exclusions.get(0);
            ExcludeDependency excludeDependency = new ExcludeDependency(excludedDependency.getGroupId(), excludedDependency.getArtifactId(), excludedDependency.getScope());
            exclusions.stream().skip(1).forEach(d -> excludeDependency.getRecipeList().add(new ExcludeDependency(d.getGroupId(), d.getArtifactId(), d.getScope())));
            apply(excludeDependency);
            refreshPomModel();
        }
    }

    private void updateClasspathRegistry() {
        ClasspathRegistry instance = ClasspathRegistry.getInstance();
        // FIXME: removed dependencies must be removed from ProjectDependenciesRegistry too
        Set<ResolvedDependency> compileDependencies = new HashSet<>(getPom().getDependencies().get(Scope.Compile));
        Set<ResolvedDependency> testDependencies = getPom().getDependencies().get(Scope.Test)
                .stream()
                .flatMap(d -> d.getDependencies().stream())
                .collect(Collectors.toSet());
        compileDependencies.addAll(testDependencies);
        compileDependencies.forEach(instance::addDependency);
    }

    private Recipe getAddDependencyRecipe(Dependency dependency) {

        AddDependencyVisitor addDependencyVisitor = new AddDependencyVisitor(
                dependency.getGroupId(),
                dependency.getArtifactId(),
                dependency.getVersion(),
                null,
                dependency.getScope() == null ? "compile" : dependency.getScope(),
                false,
                dependency.getType(),
                dependency.getClassifier(),
                false,
                null);

        return new GenericOpenRewriteRecipe<AddDependencyVisitor>(() -> addDependencyVisitor);
    }

    public void removeDependenciesInner(List<Dependency> dependencies) {
        if (!dependencies.isEmpty()) {
            Recipe r = getDeleteDependencyVisitor(dependencies.get(0));
            dependencies.stream().skip(1).forEach(d -> {
                r.getRecipeList().add(getDeleteDependencyVisitor(d));
            });
            apply(r);
            refreshPomModel();
        }
    }

    private Recipe getDeleteDependencyVisitor(Dependency dependency) {
        // FIXME: Test that RemoveDependency considers scope
        RemoveDependency v = new RemoveDependency(dependency.getGroupId(), dependency.getArtifactId(), dependency.getScope());
        return v;
    }

    @Override
    public List<Dependency> getEffectiveDependencyManagement() {
        MavenResolutionResult pom = getPom();
        if (pom.getPom().getDependencyManagement() == null) {
            return Collections.emptyList();
        }
        return pom.getPom().getDependencyManagement().stream()
                .map(this::getDependency)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<Dependency> getRequestedDependencyManagement() {
        MavenResolutionResult pom = getPom();
        if (pom.getPom().getRequested().getDependencyManagement() == null) {
            return Collections.emptyList();
        }
        return pom.getPom().getRequested().getDependencyManagement().stream()
                .map(this::getDependency)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<Dependency> getRequestedManagedDependencies() {
        return this.getPom().getPom().getRequested()
                .getDependencyManagement()
                .stream()
                .map(md -> Dependency.builder()
                        .artifactId(md.getArtifactId())
                        .version(md.getVersion())
                        .groupId(md.getGroupId())
                        .build())
                .collect(Collectors.toList());
    }

    private Dependency getDependency(ResolvedManagedDependency d) {
        return Dependency.builder()
                .groupId(d.getGroupId())
                .artifactId(d.getArtifactId())
                .version(d.getVersion())
                .scope(scopeString(d.getScope()))
                .build();
    }

    private Dependency getDependency(ManagedDependency d) {
        return Dependency.builder()
                .groupId(d.getGroupId())
                .artifactId(d.getArtifactId())
                .version(d.getVersion())
                .build();
    }

    @Override
    public void addToDependencyManagementInner(Dependency dependency) {
        AddOrUpdateDependencyManagement addOrUpdateDependencyManagement = new AddOrUpdateDependencyManagement(dependency);
        apply(new GenericOpenRewriteRecipe<>(() -> addOrUpdateDependencyManagement), getResource());
        refreshPomModel();
    }

    private <V extends TreeVisitor<?, ExecutionContext>> void apply(Recipe recipe, RewriteSourceFileHolder<Xml.Document> resource) {
        refactoring.execute(resource, recipe);
    }
    // FIXME: #7 rework dependencies/classpath registry
    // collect declared dependencies (jar/pom)
    // resolve classpath according to list of jar/pom

    @Override
    public List<Path> getResolvedDependenciesPaths() {
        RewriteMavenArtifactDownloader rewriteMavenArtifactDownloader = new RewriteMavenArtifactDownloader();
        return getPom().getDependencies().get(Scope.Provided).stream()
                .filter(this::filterProjectDependencies)
                .map(rd -> rewriteMavenArtifactDownloader.downloadArtifact(rd))
                .collect(Collectors.toList());
    }

    @NotNull
    private boolean filterProjectDependencies(ResolvedDependency rd) {
        return rd.getRepository() != null;
    }

    @Override
    public boolean hasPlugin(Plugin plugin) {
        // TODO: [FK] discuss how to handle conditions. This code is exactly the same as in #AddMavenPluginVisitor.pluginDefinitionExists(Maven.Pom pom) which is private and the test would repeat the test for AddMavenPluginVisitor
        Xml.Document sourceFile = getSourceFile();
        Optional<Xml.Tag> pluginDefinition = sourceFile.getRoot().getChildren("build").stream()
                .flatMap(b -> b.getChildren("plugins").stream())
                .flatMap(b -> b.getChildren("plugin").stream())
                .filter(p -> p.getChildren("groupId") != null && !p.getChildren("groupId").isEmpty())
                .filter(p -> {
                    String groupId = ((Xml.CharData) p.getChildren("groupId").get(0).getContent().get(0)).getText();
                    return plugin.getGroupId().equals(groupId);
                })
                .filter(p -> {
                    String artifactId = ((Xml.CharData) p.getChildren("artifactId").get(0).getContent().get(0)).getText();
                    return plugin.getArtifactId().equals(artifactId);
                })
                .findFirst();
        return pluginDefinition.isPresent();
    }

    @Override
    public void addPlugin(Plugin plugin) {
        apply(new AddMavenPlugin((OpenRewriteMavenPlugin) plugin), getResource());
    }

    @Override
    public List<Path> getSourceFolders() {
        return List.of(getAbsolutePath().getParent().resolve(JAVA_SOURCE_FOLDER));
    }

    @Override
    public List<Path> getResourceFolders() {
        return List.of(getAbsolutePath().getParent().resolve(RESOURCE_FOLDER));
    }

    @Override
    public List<Path> getTestResourceFolders() {
        return List.of(getAbsolutePath().getParent().resolve(RESOURCE_TEST_FOLDER));
    }

    @Override
    public List<Path> getClasspath() {
        List<Path> classpath = new ArrayList<>();
        classpath.add(getSourceFile().getSourcePath().toAbsolutePath().getParent().resolve("target/classes"));
        classpath.addAll(getResolvedDependenciesPaths());
        return classpath;
    }

    @Override
    public List<Path> getTestSourceFolders() {
        return List.of(getAbsolutePath().getParent().resolve(JAVA_TEST_SOURCE_FOLDER));
    }

    final public String getProperty(String key) {
        return getPom().getPom().getRequested().getProperties().get(key);
    }

	@Override
	final public void deleteProperty(String key){
		apply(new RemoveProperty(key), getResource());
        refreshPomModel();
    }

    final public void setProperty(String key, String value) {
		String current = getProperty(key);
        Recipe recipe = current == null ? new AddProperty(key, value, false, false) : new ChangePropertyValue(key, value, false, false);
        apply(recipe, getResource());
        refreshPomModel();
    }

    @Override
    public String getPackaging() {
        String packaging = getPom().getPom().getPackaging();
        if (packaging == null) {
            packaging = "jar";
        }
        return packaging;
    }

    @Override
    public void setPackaging(String packaging) {
        ChangePackaging changePackaging = new ChangePackaging(getGroupId(), getArtifactId(), packaging);
        apply(changePackaging);
    }

    @Override
    public boolean isRootBuildFile() {
        return getSourcePath().getParent() == null;
    }


    @Override
    public String getGroupId() {
        return getPom().getPom().getGroupId();
    }

    @Override
    public String getArtifactId() {
        return getPom().getPom().getArtifactId();
    }

    @Override
    public String getVersion() {
        return resolve(getPom().getPom().getVersion());
    }

    @Override
    public String getCoordinates() {
        return getGroupId() + ":" + getArtifactId() + ":" + getVersion();
    }

    @Override
    public boolean hasParent() {
        @Nullable Parent parent = getPom().getPom().getRequested().getParent();
        return parent != null;
    }

    @Override
    public void upgradeParentVersion(String version) {
        if (hasParent()) {
            @Nullable Parent parent = getPom().getPom().getRequested().getParent();
            apply(
                    new UpgradeParentVersion(parent.getGroupId(), parent.getArtifactId(), version, null, List.of())
            );
        }
    }

    @Override
    public Optional<ParentDeclaration> getParentPomDeclaration() {
        @Nullable Parent parent = getPom().getPom().getRequested().getParent();
        // FIXME: no relativePath for parent declaration
        if (parent == null) {
            // TODO: return Optional instead of null
            return Optional.empty();
        }
        return Optional.of(new RewriteMavenParentDeclaration(parent.getGroupId(), parent.getArtifactId(), parent.getVersion(), "Not supported (yet)"));
    }

    @Override
    public Optional<String> getName() {
        return Optional.ofNullable(getPom().getPom().getRequested().getName());
    }

    @Override
    public Path getMainResourceFolder() {
        return getResourceFolders().get(0);
    }

    @Override
    public Path getTestResourceFolder() {
        return getTestResourceFolders().get(0);
    }

    @Override
    public void excludeDependencies(List<Dependency> excludedDependencies) {
        excludeDependenciesInner(excludedDependencies);
        updateClasspathRegistry();
    }

    @Override
    public void addRepository(RepositoryDefinition repository) {
        Recipe recipe = new AddMavenRepository(repository);
        apply(recipe);
        refreshPomModel();
    }

    @Override
    public void addPluginRepository(RepositoryDefinition repository) {
        AddMavenPluginRepository addMavenPluginRepository = new AddMavenPluginRepository(repository);
        apply(addMavenPluginRepository);
    }

    @Override
    public List<RepositoryDefinition> getRepositories() {
        return getPom().getPom().getRepositories().stream()
                .map(r -> RepositoryDefinition.builder()
                        .id(r.getId())
                        .url(r.getUri())
                        .releasesEnabled("true".equalsIgnoreCase(r.getReleases()))
                        .snapshotsEnabled("true".equalsIgnoreCase(r.getSnapshots()))
                        .build()
                ).collect(Collectors.toList());
    }

    @Override
    public List<String> getDeclaredModules() {
        List<MavenResolutionResult> modulesMarker = getPom().getModules();
        return modulesMarker.stream()
                .map(m -> m.getPom().getGav().toString())
                .collect(Collectors.toList());
    }

    @Override
    public List<RepositoryDefinition> getPluginRepositories() {
        return pluginRepositoryHandler.getRepositoryDefinitions(getSourceFile());
    }

    private boolean anyRegexMatchesCoordinate(Plugin p, String... regex) {
        String coordinate = p.getGroupId() + ":" + p.getArtifactId();
        return Stream.of(regex).anyMatch(r -> coordinate.matches(r));
    }


    @Override
    public List<Plugin> getPlugins() {
		return getPom().getPom().getRequested().getPlugins().stream()
				.map(p -> new OpenRewriteMavenPlugin(p, getResource(), refactoring))
				.collect(Collectors.toList());
    }

    /**
     * Remove all plugins with coordinates {@code <groupId:artifactId>} matching any given regex.
     */
    @Override
    public void removePluginsMatchingRegex(String... regex) {
        List<String> coordinates = getPlugins().stream()
                .filter(p -> this.anyRegexMatchesCoordinate(p, regex))
                .map(p -> p.getGroupId() + ":" + p.getArtifactId())
                .collect(Collectors.toList());
        if (!coordinates.isEmpty()) {
            removePlugins(coordinates.toArray(new String[]{}));
        }
    }

    @Override
    public void removePlugins(String... coordinates) {
        List<RemoveMavenPlugin> removeMavenPlugins = new ArrayList<>();
        List<String> c = Arrays.asList(coordinates);
        Iterator<String> iterator = c.iterator();
        Recipe recipe;
        String coordinate = iterator.next();
        String[] split = coordinate.split(":");
        recipe = new RemoveMavenPlugin(split[0], split[1]);
        while (iterator.hasNext()) {
            coordinate = iterator.next();
            split = coordinate.split(":");
            recipe.getRecipeList().add(new RemoveMavenPlugin(split[0], split[1]));
        }

        List<Result> run = recipe.run(new InMemoryLargeSourceSet(List.of(getSourceFile())), executionContext).getChangeset().getAllResults();
        if (!run.isEmpty()) {
            replaceWith((Xml.Document) run.get(0).getAfter());
        }
    }

	@Override
	public Optional<Plugin> findPlugin(String groupId, String artifactId){
		return getPlugins()
				.stream()
				.filter(plugin -> plugin.getGroupId().equals(groupId) &&
						plugin.getArtifactId().equals(artifactId))
				.findAny();
	}

    private String resolve(String expression) {
        return getPom().getPom().getValue(expression);
    }
}
