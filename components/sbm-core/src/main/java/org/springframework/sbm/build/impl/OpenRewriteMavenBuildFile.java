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

import org.springframework.sbm.build.api.*;
import org.springframework.sbm.build.migration.recipe.AddMavenPlugin;
import org.springframework.sbm.build.migration.recipe.RemoveMavenPlugin;
import org.springframework.sbm.build.migration.visitor.AddOrUpdateDependencyManagement;
import org.springframework.sbm.build.migration.visitor.AddProperty;
import org.springframework.sbm.build.migration.visitor.SetPackaging;
import org.springframework.sbm.java.impl.ClasspathRegistry;
import org.springframework.sbm.openrewrite.RewriteExecutionContext;
import org.springframework.sbm.project.resource.RewriteSourceFileHolder;
import org.springframework.sbm.support.openrewrite.GenericOpenRewriteRecipe;
import org.jboss.shrinkwrap.resolver.api.maven.MavenResolvedArtifact;
import org.jboss.shrinkwrap.resolver.api.maven.MavenWorkingSession;
import org.jboss.shrinkwrap.resolver.api.maven.PackagingType;
import org.jboss.shrinkwrap.resolver.api.maven.ScopeType;
import org.jboss.shrinkwrap.resolver.api.maven.coordinate.MavenCoordinates;
import org.jboss.shrinkwrap.resolver.api.maven.coordinate.MavenDependencies;
import org.jboss.shrinkwrap.resolver.api.maven.coordinate.MavenDependency;
import org.jboss.shrinkwrap.resolver.impl.maven.MavenWorkingSessionImpl;
import org.openrewrite.*;
import org.openrewrite.maven.*;
import org.openrewrite.maven.tree.Maven;
import org.openrewrite.maven.tree.Pom;
import org.openrewrite.maven.tree.Scope;
import org.openrewrite.xml.tree.Xml;
import org.springframework.context.ApplicationEventPublisher;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OpenRewriteMavenBuildFile extends RewriteSourceFileHolder<Maven> implements BuildFile {

    private final ApplicationEventPublisher eventPublisher;

    // Execute separately since RefreshPomModel caches the refreshed maven files after the first visit
    public static class RefreshPomModel extends Recipe {

        private List<SourceFile> sourceFiles;

        @Override
        protected List<SourceFile> visit(List<SourceFile> before, ExecutionContext ctx) {
            if (sourceFiles == null) {
                List<SourceFile> nonMavenFiles = new ArrayList<>(before.size());
                List<Maven> mavenFiles = new ArrayList<>();
                for (SourceFile f : before) {
                    if (f instanceof Maven) {
                        mavenFiles.add((Maven) f);
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
                List<Maven> newMavenFiles = mavenParser.parseInputs(parserInput, null, ctx);

                for (int i = 0; i < newMavenFiles.size(); i++) {
                    Maven m = mavenFiles.get(i).withModel(newMavenFiles.get(i).getModel());
                    mavenFiles.set(i, m);
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

    public OpenRewriteMavenBuildFile(Path absoluteProjectPath, Maven sourceFile, ApplicationEventPublisher eventPublisher, RewriteExecutionContext executionContext) {
        super(absoluteProjectPath, sourceFile);
        this.eventPublisher = eventPublisher;
        this.executionContext = executionContext;
    }

    public void apply(Recipe recipe) {
        List<Result> result = recipe.run(List.of(getSourceFile()), executionContext);
        if (!result.isEmpty()) {
            replaceWith((Maven) result.get(0).getAfter());
        }
    }

    public Maven getPom() {
        return getSourceFile();
    }

    /**
     * Removes all dependencies matching given regex.
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
    public void addDependency(Dependency dependency) {
        addDependencyInner(dependency);
        eventPublisher.publishEvent(new DependenciesChangedEvent(getResolvedDependenciesPaths()));
    }

    @Override
    public void addToDependencyManagement(Dependency dependency) {
        addToDependencyManagementInner(dependency);
        eventPublisher.publishEvent(new DependenciesChangedEvent(getResolvedDependenciesPaths()));
    }

    @Override
    public void addDependencies(List<Dependency> dependencies) {
        addDependenciesInner(dependencies);
        eventPublisher.publishEvent(new DependenciesChangedEvent(getResolvedDependenciesPaths()));
    }

    @Override
    public void removeDependencies(List<Dependency> dependencies) {
        removeDependenciesInner(dependencies);
        eventPublisher.publishEvent(new DependenciesChangedEvent(getResolvedDependenciesPaths()));
    }

    public List<Dependency> getDeclaredDependencies() {
        Collection<org.openrewrite.maven.tree.Pom.Dependency> dependencies = getPom().getModel().getDependencies();
        return dependencies.stream()
                .map(d -> Dependency.builder()
                        .groupId(d.getGroupId())
                        .artifactId(d.getArtifactId())
                        .version(d.getVersion())
                        .scope(scopeString(d.getScope()))
                        .type(d.getType())
                        .build())
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public Set<Pom.Dependency> getEffectiveDependencies(Scope scope) {
        return getPom().getModel().getDependencies(scope);
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

    public void addDependencyInner(Dependency dependency) {
        addDependenciesInner(List.of(dependency));
    }

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
        return getPom().getMavenModel().getPom().getDependencies().stream()
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
        Set<Pom.Dependency> compileDependencies = getPom().getModel().getDependencies(Scope.Compile);
        compileDependencies.addAll(getPom().getModel().getDependencies(Scope.Test));
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
        Maven pom = getPom();
        if (pom.getModel().getDependencyManagement() == null) {
            return Collections.emptyList();
        }
        return pom.getModel().getDependencyManagement().getDependencies().stream()
                .flatMap(d -> d.getDependencies().stream())
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

        try {
            MavenResolvedArtifact[] artifacts = new MavenResolvedArtifact[0];
            File file = getAbsolutePath().toFile();
            if (file.exists()) {
                MavenWorkingSession mavenWorkingSession = new MavenWorkingSessionImpl().loadPomFromFile(file);
                if (!mavenWorkingSession.getDeclaredDependencies().isEmpty()) {
                    artifacts = org.jboss.shrinkwrap.resolver.api.maven.Maven.resolver().loadPomFromFile(file)
                            .importDependencies(ScopeType.values())
                            .resolve().withTransitivity().asResolvedArtifact();
                }
            } else {
                List<MavenDependency> mavenDependencies = getDeclaredDependencies().stream()
                        .map(d -> MavenDependencies.createDependency(
                                MavenCoordinates.createCoordinate(
                                        d.getGroupId(), d.getArtifactId(), d.getVersion(),
                                        d.getType() == null ? PackagingType.JAR : PackagingType.of(d.getType()),
                                        d.getClassifier()),
                                ScopeType.fromScopeType(d.getScope()),
                                false))
                        .collect(Collectors.toList());

                if (mavenDependencies.isEmpty()) {
                    return Collections.emptyList();
                }

                artifacts = org.jboss.shrinkwrap.resolver.api.maven.Maven.resolver()
                        .addDependencies(mavenDependencies)
                        .resolve().withTransitivity().asResolvedArtifact();
            }

            return Arrays.stream(artifacts)
                    .map(a -> a.asFile().toPath())
                    .collect(Collectors.toList());
        } catch (
                Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public boolean hasPlugin(Plugin plugin) {
        // TODO: [FK] discuss how to handle conditions. This code is exactly the same as in #AddMavenPluginVisitor.pluginDefinitionExists(Maven.Pom pom) which is private and the test would repeat the test for AddMavenPluginVisitor
        Maven pom = getPom();
        Optional<Xml.Tag> pluginDefinition = pom.getRoot().getChildren("build").stream()
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
        classpath.add(getPom().getSourcePath().toAbsolutePath().getParent().resolve("target/classes"));
        classpath.addAll(getResolvedDependenciesPaths());
        return classpath;
    }

    @Override
    public List<Path> getTestSourceFolders() {
        return Arrays.asList(getAbsolutePath().getParent().resolve(JAVA_TEST_SOURCE_FOLDER));
    }

    final public String getProperty(String key) {
        return getPom().getModel().getProperties().get(key);
    }

    final public void setProperty(String key, String value) {
        if (value == null) {
            apply(new RemoveProperty(key));
        } else {
            String current = getProperty(key);
            apply(current == null ? new AddProperty(key, value) : new ChangePropertyValue(key, value));
        }
        apply(new RefreshPomModel());
    }

    @Override
    public String getPackaging() {
        String packaging = getPom().getModel().getPackaging();
        return packaging;
    }

    @Override
    public void setPackaging(String packaging) {
        apply(new SetPackaging(packaging).doNext(new RefreshPomModel()));
    }

    @Override
    public boolean isRootBuildFile() {
        return getSourcePath().getParent() == null;
    }


    @Override
    public String getGroupId() {
        return getPom().getMavenModel().getPom().getGroupId();
    }

    @Override
    public String getArtifactId() {
        return getPom().getMavenModel().getPom().getArtifactId();
    }

    @Override
    public String getVersion() {
        return getPom().getMavenModel().getPom().getVersion();
    }

    @Override
    public String getCoordinates() {
        return getGroupId() + ":" + getArtifactId() + ":" + getVersion();
    }

    @Override
    public boolean hasParent() {
        Pom parent = getPom().getModel().getParent();
        return parent != null;
    }

    @Override
    public void upgradeParentVersion(String version) {
        if (hasParent()) {
            Pom parent = getPom().getModel().getParent();
            apply(
                    new UpgradeParentVersion(parent.getGroupId(), parent.getArtifactId(), version, null)
                            .doNext(new RefreshPomModel())
            );
        }
    }

    @Override
    public ParentDeclaration getParentPomDeclaration() {
        Pom parent = getPom().getModel().getParent();
        // FIXME: no relativePath for parent declaration
        if (parent == null) {
            // TODO: return Optional instead of null
            return null;
        }
        return new RewriteMavenParentDeclaration(parent.getGroupId(), parent.getArtifactId(), parent.getVersion(), "Not supported (yet)");
    }

    @Override
    public Optional<String> getName() {
        return Optional.ofNullable(getPom().getModel().getName());
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
        Maven maven = getPom();

        List<Plugin> plugins = new ArrayList<>();

        MavenVisitor mavenVisitor = new MavenVisitor() {

            @Override
            public Maven visitMaven(Maven maven, ExecutionContext ctx) {
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

        mavenVisitor.visitMaven(maven, executionContext);

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

        List<Result> run = recipe.run(List.of(getPom()), executionContext);
        if (!run.isEmpty()) {
            replaceWith((Maven) run.get(0).getAfter());
        }
    }

}
