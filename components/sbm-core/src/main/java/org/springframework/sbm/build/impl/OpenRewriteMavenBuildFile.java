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

import org.jetbrains.annotations.NotNull;
import org.openrewrite.*;
import org.openrewrite.internal.lang.Nullable;
import org.openrewrite.marker.Markers;
import org.openrewrite.maven.*;
import org.openrewrite.maven.tree.MavenResolutionResult;
import org.openrewrite.maven.tree.Parent;
import org.openrewrite.maven.tree.ResolvedDependency;
import org.openrewrite.maven.tree.Scope;
import org.openrewrite.xml.tree.Xml;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.sbm.build.api.*;
import org.springframework.sbm.build.migration.recipe.AddMavenPlugin;
import org.springframework.sbm.build.migration.recipe.RemoveMavenPlugin;
import org.springframework.sbm.build.migration.visitor.AddOrUpdateDependencyManagement;
import org.springframework.sbm.build.migration.visitor.AddProperty;
import org.springframework.sbm.java.impl.ClasspathRegistry;
import org.springframework.sbm.openrewrite.RewriteExecutionContext;
import org.springframework.sbm.project.resource.RewriteSourceFileHolder;
import org.springframework.sbm.support.openrewrite.GenericOpenRewriteRecipe;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OpenRewriteMavenBuildFile extends RewriteSourceFileHolder<Xml.Document> implements BuildFile {

    private final ApplicationEventPublisher eventPublisher;


    // TODO: #7 clarify if RefreshPomModel is still required?
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
                                        () -> new ByteArrayInputStream(m.printAll().getBytes(StandardCharsets.UTF_8)),
                                        !Files.exists(m.getSourcePath())
                                )
                        )
                        .collect(Collectors.toList());
                List<Xml.Document> newMavenFiles = mavenParser.parseInputs(parserInput, null, ctx);

                for (int i = 0; i < newMavenFiles.size(); i++) {
                    Optional<MavenResolutionResult> mavenModels = MavenBuildFileUtil.getMavenResolution(mavenFiles.get(i));
                    Optional<MavenResolutionResult> newMavenModels = MavenBuildFileUtil.getMavenResolution(newMavenFiles.get(i));
                    mavenFiles.get(i).withMarkers(Markers.build(Arrays.asList(newMavenModels.get())));
//                    Xml.Document m = mavenFiles.get(i).getMarkers().withModel(newMavenFiles.get(i).getModel());
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

    }

    public static final Path JAVA_SOURCE_FOLDER = Path.of("src/main/java");
    public static final Path JAVA_TEST_SOURCE_FOLDER = Path.of("src/test/java");
    private static final Path RESOURCE_FOLDER = Path.of("src/main/resources");
    private static final Path RESOURCE_TEST_FOLDER = Path.of("src/test/resources");

    private final RewriteExecutionContext executionContext;

    public OpenRewriteMavenBuildFile(Path absoluteProjectPath, Xml.Document sourceFile, ApplicationEventPublisher eventPublisher, RewriteExecutionContext executionContext) {
        super(absoluteProjectPath, sourceFile);
        this.eventPublisher = eventPublisher;
        this.executionContext = executionContext;
    }

    public void apply(Recipe recipe) {
        List<Result> result = recipe.run(List.of(getSourceFile()), executionContext);
        if (!result.isEmpty()) {
            replaceWith((Xml.Document) result.get(0).getAfter());
        }
    }

    public MavenResolutionResult getPom() {
        return MavenBuildFileUtil.getMavenResolution(getSourceFile()).get();
    }

    @Override
    public void addDependency(Dependency dependency) {
        addDependencyInner(dependency);
        eventPublisher.publishEvent(new DependenciesChangedEvent(getResolvedDependenciesPaths()));
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
    public boolean hasDeclaredDependencyMatchingRegex(String... regex) {
        return getDeclaredDependencies().stream()
                .map(d -> d.getCoordinates())
                .anyMatch(dc -> Arrays.stream(regex).anyMatch(r -> dc.matches(r)));
    }

    @Override
    public boolean hasExactDeclaredDependency(Dependency dependency) {
        return getDeclaredDependencies().stream()
                .anyMatch(d -> d.equals(dependency));
    }

    /*
    * TODO: write tests:
    * - with all scopes
    * - Managed versions with type and classifier given
    * - exclusions
    * - type
    * */
    @Override
    public List<Dependency> getDeclaredDependencies(Scope... scopes) {
        // returns dependencies as declared in xml
        List<org.openrewrite.maven.tree.Dependency> requestedDependencies = getPom().getPom().getRequestedDependencies();
        List<Dependency> declaredDependenciesWithEffectiveVersions = requestedDependencies.stream()
                .filter(d -> {
                    if(scopes.length == 0) {
                        return true;
                    } else {
                        // FIXME: scope test should also return compile!
                        return Arrays.asList(scopes).stream().anyMatch(scope -> scope.toString().equals(d.getScope()));
                    }
                })
                .map(d -> mapDependency(d))
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
        return getPom().getDependencies().entrySet()
                .stream()
                .flatMap(e -> e.getValue().stream().map(v -> mapDependency(e.getKey(), v)))
                .collect(Collectors.toSet());
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
                .version(calculateVersion(d))
                .scope(calculateScope(d))
                .type(d.getType());
        if(d.getExclusions() != null && ! d.getExclusions().isEmpty()) {
            dependencyBuilder.exclusions(d.getExclusions().stream()
                    .map(e -> Dependency.builder().groupId(e.getGroupId()).artifactId(e.getArtifactId()).build())
                    .collect(Collectors.toList()));
        }
        return dependencyBuilder.build();
    }

    private String calculateScope(org.openrewrite.maven.tree.Dependency d) {
        String scope = null;
        if(d.getScope() != null) {
            scope = d.getScope();
        } else {
            Scope managedScope = getPom().getPom().getManagedScope(d.getGroupId(), d.getArtifactId(), null, null);
            if(managedScope != null) {
                scope = managedScope.name().toLowerCase();
            }
        }
        return scope;
    }

    private String calculateVersion(org.openrewrite.maven.tree.Dependency d) {
        String version = null;
        if(d.getVersion() != null) {
            version = d.getVersion();
        } else {
            String managedVersion = getPom().getPom().getManagedVersion(d.getGroupId(), d.getArtifactId(), null, null);
            if(managedVersion != null) {
                version = managedVersion;
            }
        }
        return version;
    }

    private org.springframework.sbm.build.api.Dependency mapDependency(Scope scope, ResolvedDependency d) {
       return new Dependency(
                d.getGroupId(),
                d.getArtifactId(),
                d.getVersion(),
                d.getType(),
                scope.name(),
                d.getClassifier(),
                d.getRequested().getExclusions()
                        .stream()
                        .map(e -> Dependency.builder().groupId(e.getGroupId()).artifactId(e.getArtifactId()).build())
                        .collect(Collectors.toList())
                );
    }

    public void addDependencyInner(Dependency dependency) {
        addDependenciesInner(List.of(dependency));
    }

    private String scopeString(Scope scope) {
        //TODO: kdv is this really a good way to represent the 'scope' ?
        return scope == null ? null : scope.toString().toLowerCase();
    }

/* // TODO: https://github.com/pivotal/spring-boot-migrator/issues/14
    public void setPackaging(MavenPackaging packaging) {
        ChangeMavenPackagingVisitor changeMavenPackagingVisitor = new ChangeMavenPackagingVisitor(packaging.getPackagingValue());
        // TODO: #14
        new Refactoring(modifiablePom).execute(changeMavenPackagingVisitor);

    }
*/

    /* // TODO: https://github.com/pivotal/spring-boot-migrator/issues/14
        public MavenPackaging getPackaging() {
            List<Xml.Tag> packaging = modifiablePom.getSourceFile().getRoot().getChildren("packaging");
            if(packaging.isEmpty()) {
                return MavenPackaging.JAR;
            }
            return MavenPackaging.valueOf(packaging.get(0).getValue().get().toUpperCase());
        }
    */

    protected void addDependenciesInner(List<Dependency> dependencies) {
//        dependencies = dependencies.stream().filter(d -> hasExactDeclaredDependency(d)).collect(Collectors.toList());
        if (!dependencies.isEmpty()) {
            Recipe r = getAddDependencyRecipe(dependencies.get(0));
            dependencies.stream().skip(1).forEach(d -> r.doNext(getAddDependencyRecipe(d)));
            apply(r);
            apply(new RefreshPomModel());

            List<Dependency> exclusions = dependencies.stream()
                    .filter(d -> false == d.getExclusions().isEmpty())
                    .flatMap(d -> d.getExclusions().stream())
                    .collect(Collectors.toList());

            excludeDependenciesInner(exclusions);

            updateClasspathRegistry();
        }
    }

    private boolean hasEffectiveDependency(Dependency d) {
        return getEffectiveDependencies().stream()
                .anyMatch(dep -> d.getCoordinates().equals(dep.getCoordinates()));
    }

    /**
     * Does not updateClasspathRegistry
     */
    private void excludeDependenciesInner(List<Dependency> exclusions) {
        if (false == exclusions.isEmpty()) {
            Dependency excludedDependency = exclusions.get(0);
            ExcludeDependency excludeDependency = new ExcludeDependency(excludedDependency.getGroupId(), excludedDependency.getArtifactId(), excludedDependency.getScope());
            exclusions.stream().skip(1).forEach(d -> excludeDependency.doNext(new ExcludeDependency(d.getGroupId(), d.getArtifactId(), d.getScope())));
            apply(excludeDependency);
            apply(new RefreshPomModel()); // TODO: 482: check if required
        }
    }

    private void updateClasspathRegistry() {
        ClasspathRegistry instance = ClasspathRegistry.getInstance();
        // FIXME: removed dependencies must be removed from ProjectDependenciesRegistry too
        Set<ResolvedDependency> compileDependencies = getPom().getDependencies().get(Scope.Compile).stream().collect(Collectors.toSet());
        Set<ResolvedDependency> collect = getPom().getDependencies().get(Scope.Test)
                .stream()
                .flatMap(d -> d.getDependencies().stream())
                .collect(Collectors.toSet());
        compileDependencies.addAll(collect);
        compileDependencies.stream()
                .forEach(instance::addDependency);
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
            dependencies.stream().skip(1).forEach(d -> r.doNext(getDeleteDependencyVisitor(d)));
            apply(r);
        }
    }

    private Recipe getDeleteDependencyVisitor(Dependency dependency) {
        // FIXME: Test that RemoveDependency considers scope
        RemoveDependency v = new RemoveDependency(dependency.getGroupId(), dependency.getArtifactId(), null);
        return v;
    }

    @Override
    public List<Dependency> getDependencyManagement() {
        MavenResolutionResult pom = getPom();
        if (pom.getPom().getDependencyManagement() == null) {
            return Collections.emptyList();
        }
        return pom.getPom().getDependencyManagement().stream()
                .map(d -> Dependency.builder()
                        .groupId(d.getGroupId())
                        .artifactId(d.getArtifactId())
                        .version(d.getVersion())
                        .scope(scopeString(d.getScope()))
                        .build())
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public void addToDependencyManagementInner(Dependency dependency) {
        AddOrUpdateDependencyManagement addOrUpdateDependencyManagement = new AddOrUpdateDependencyManagement(dependency);
        apply(new GenericOpenRewriteRecipe<>(() -> addOrUpdateDependencyManagement));
        // Execute separately since RefreshPomModel caches the refreshed maven files after the first visit
        apply(new RefreshPomModel());
    }

    @Override
    // TODO:
    public List<Path> getResolvedDependenciesPaths() {

        return new ArrayList<>(ClasspathRegistry.getInstance().getCurrentDependencies());
//
//        try {
//            MavenResolvedArtifact[] artifacts = new MavenResolvedArtifact[0];
//            File file = getAbsolutePath().toFile();
//            if (file.exists()) {
//                MavenWorkingSession mavenWorkingSession = new MavenWorkingSessionImpl().loadPomFromFile(file);
//                if (!mavenWorkingSession.getDeclaredDependencies().isEmpty()) {
//                    artifacts = org.jboss.shrinkwrap.resolver.api.maven.Maven.resolver().loadPomFromFile(file)
//                            .importDependencies(ScopeType.values())
//                            .resolve().withTransitivity().asResolvedArtifact();
//                }
//            } else {
//                List<MavenDependency> mavenDependencies = getDeclaredDependencies().stream()
//                        .map(d -> MavenDependencies.createDependency(
//                                MavenCoordinates.createCoordinate(
//                                        d.getGroupId(), d.getArtifactId(), d.getVersion(),
//                                        d.getType() == null ? PackagingType.JAR : PackagingType.of(d.getType()),
//                                        d.getClassifier()),
//                                ScopeType.fromScopeType(d.getScope()),
//                                false))
//                        .collect(Collectors.toList());
//
//                if (mavenDependencies.isEmpty()) {
//                    return Collections.emptyList();
//                }
//
//                artifacts = org.jboss.shrinkwrap.resolver.api.maven.Maven.resolver()
//                        .addDependencies(mavenDependencies)
//                        .resolve().withTransitivity().asResolvedArtifact();
//            }
//
//            return Arrays.stream(artifacts)
//                    .map(a -> a.asFile().toPath())
//                    .collect(Collectors.toList());
//        } catch (
//                Exception e) {
//            throw new RuntimeException(e);
//        }

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
        apply(new AddMavenPlugin(plugin));
    }

    @Override
    public List<Path> getSourceFolders() {
        return Arrays.asList(getAbsolutePath().getParent().resolve(JAVA_SOURCE_FOLDER));
    }

    @Override
    public List<Path> getResourceFolders() {
        return Arrays.asList(getAbsolutePath().getParent().resolve(RESOURCE_FOLDER));
    }

    @Override
    public List<Path> getTestResourceFolders() {
        return Arrays.asList(getAbsolutePath().getParent().resolve(RESOURCE_TEST_FOLDER));
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
        return Arrays.asList(getAbsolutePath().getParent().resolve(JAVA_TEST_SOURCE_FOLDER));
    }

    final public String getProperty(String key) {
        return getPom().getPom().getProperties().get(key);
    }

    final public void setProperty(String key, String value) {
        if (value == null) {
            apply(new RemoveProperty(key));
        } else {
            String current = getProperty(key);
            apply(current == null ? new AddProperty(key, value) : new ChangePropertyValue(key, value, false));
        }
        apply(new RefreshPomModel());
    }

    @Override
    public String getPackaging() {
        String packaging = getPom().getPom().getPackaging();
        if(packaging == null) {
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
        return getPom().getPom().getVersion();
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
                    new UpgradeParentVersion(parent.getGroupId(), parent.getArtifactId(), version, null)
                            .doNext(new RefreshPomModel())
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
    public void excludeDependencies(List<Dependency> excludedDependencies) {
        excludeDependenciesInner(excludedDependencies);
        updateClasspathRegistry();
    }

    private boolean anyRegexMatchesCoordinate(Plugin p, String... regex) {
        String coordinate = p.getGroupId() + ":" + p.getArtifactId();
        return Stream.of(regex).anyMatch(r -> coordinate.matches(r));
    }


    @Override
    public List<Plugin> getPlugins() {

        List<Plugin> plugins = new ArrayList<>();

        MavenVisitor mavenVisitor = new MavenVisitor<ExecutionContext>() {

            @Override
            public Xml.Document visitDocument(Xml.Document maven, ExecutionContext ctx) {
                Xml.Tag mavenRoot = maven.getRoot();
                Optional<Xml.Tag> build = mavenRoot.getChild("build");
                if (build.isPresent()) {
                    Xml.Tag buildTag = build.get();
                    Optional<Xml.Tag> pluginTags = buildTag.getChild("plugins");
                    if (pluginTags.isPresent()) {
                        List<Xml.Tag> plugin = pluginTags.get().getChildren("plugin");
                        List<Plugin> pluginList = plugin.stream()
                                .map(this::mapToPlugin)
                                .collect(Collectors.toList());
                        plugins.addAll(pluginList);
                    }
                }
                return null;
            }

            private Plugin mapToPlugin(Xml.Tag tag) {
                String groupId = tag.getChild("groupId").get().getValue().get();
                String artifactId = tag.getChild("artifactId").get().getValue().get();
                Optional<Xml.Tag> versionTag = tag.getChild("version");
                String version = null;
                if (versionTag.isPresent()) {
                    version = versionTag.get().getValue().get();
                }
                Plugin plugin = new Plugin(groupId, artifactId, version, List.of(), "", "");
                return plugin;
            }
        };

        mavenVisitor.visitDocument(getSourceFile(), executionContext);

        return plugins;
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
            recipe.doNext(new RemoveMavenPlugin(split[0], split[1]));
        }

        List<Result> run = recipe.run(List.of(getSourceFile()), executionContext);
        if (!run.isEmpty()) {
            replaceWith((Xml.Document) run.get(0).getAfter());
        }
    }

}
