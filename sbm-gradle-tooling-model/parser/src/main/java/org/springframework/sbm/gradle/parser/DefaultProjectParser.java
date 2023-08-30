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
package org.springframework.sbm.gradle.parser;

import org.gradle.util.GradleVersion;
import org.openrewrite.ExecutionContext;
import org.openrewrite.OmniParser;
import org.openrewrite.SourceFile;
import org.openrewrite.config.Environment;
import org.openrewrite.gradle.GradleParser;
import org.openrewrite.gradle.marker.GradleProject;
import org.openrewrite.gradle.marker.GradleSettings;
import org.openrewrite.groovy.GroovyParser;
import org.openrewrite.internal.lang.Nullable;
import org.openrewrite.java.JavaParser;
import org.openrewrite.java.internal.JavaTypeCache;
import org.openrewrite.java.marker.JavaProject;
import org.openrewrite.java.marker.JavaSourceSet;
import org.openrewrite.java.marker.JavaVersion;
import org.openrewrite.json.JsonParser;
import org.openrewrite.kotlin.KotlinParser;
import org.openrewrite.marker.*;
import org.openrewrite.marker.ci.BuildEnvironment;
import org.openrewrite.properties.PropertiesParser;
import org.openrewrite.protobuf.ProtoParser;
import org.openrewrite.style.NamedStyles;
import org.openrewrite.text.PlainTextParser;
import org.openrewrite.tree.ParseError;
import org.openrewrite.xml.XmlParser;
import org.openrewrite.yaml.YamlParser;
import org.springframework.sbm.gradle.tooling.GradleProjectData;
import org.springframework.sbm.gradle.tooling.JavaSourceSetData;
import org.springframework.sbm.gradle.tooling.KotlinSourceSetData;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.openrewrite.PathUtils.separatorsToUnix;
import static org.openrewrite.Tree.randomId;

class DefaultProjectParser {

    private final AtomicBoolean firstWarningLogged = new AtomicBoolean(false);
    protected final Path baseDir;

    protected final ParseConfig parserConfig;
    protected final GradleProjectData project;
    private final List<Marker> sharedProvenance;

    private List<NamedStyles> styles;
    private Environment environment;

    DefaultProjectParser(GradleProjectData project, ParseConfig parserConfig) {
        this.baseDir = repositoryRoot(project);
        this.parserConfig = parserConfig;
        this.project = project;

        BuildEnvironment buildEnvironment = BuildEnvironment.build(System::getenv);
        sharedProvenance = Stream.of(
                        buildEnvironment,
                        gitProvenance(baseDir, buildEnvironment),
                        OperatingSystemProvenance.current(),
                        new BuildTool(randomId(), BuildTool.Type.Gradle, project.getGradleVersion()))
                .filter(Objects::nonNull)
                .collect(toList());
    }

    /**
     * Attempt to determine the root of the git repository for the given project.
     * Many Gradle builds co-locate the build root with the git repository root, but that is not required.
     * If no git repository can be located in any folder containing the build, the build root will be returned.
     */
    static Path repositoryRoot(GradleProjectData project) {
        Path buildRoot = project.getRootProjectDir().toPath();
        Path maybeBaseDir = buildRoot;
        while (maybeBaseDir != null && !Files.exists(maybeBaseDir.resolve(".git"))) {
            maybeBaseDir = maybeBaseDir.getParent();
        }
        if (maybeBaseDir == null) {
            return buildRoot;
        }
        return maybeBaseDir;
    }

    @Nullable
    private GitProvenance gitProvenance(Path baseDir, @Nullable BuildEnvironment buildEnvironment) {
        try {
            return GitProvenance.fromProjectDirectory(baseDir, buildEnvironment);
        } catch (Exception e) {
            // Logging at a low level as this is unlikely to happen except in non-git projects, where it is expected
//            logger.debug("Unable to determine git provenance", e);
      }
        return null;
    }

    public Stream<SourceFile> parse(ExecutionContext ctx) {
        Stream<SourceFile> builder = Stream.of();
        Set<Path> alreadyParsed = new HashSet<>();
        if (project.isRootProject()) {
            for (GradleProjectData subProject : project.getSubprojects()) {
                builder = Stream.concat(builder, parse(subProject, alreadyParsed, ctx));
            }
        }
        builder = Stream.concat(builder, parse(project, alreadyParsed, ctx));

        // log parse errors here at the end, so that we don't log parse errors for files that were excluded
        return builder.map(this::logParseErrors);
    }

    private SourceFile logParseErrors(SourceFile source) {
        if (source instanceof ParseError) {
            if (firstWarningLogged.compareAndSet(false, true)) {
//                logger.warn("There were problems parsing some source files, run with --info to see full stack traces");
            }
//            logger.warn("There were problems parsing " + source.getSourcePath());
        }
        return source;
    }

    protected Environment environment() {
        if (environment == null) {
            Map<Object, Object> gradleProps = project.getProperties().entrySet().stream()
                    .filter(entry -> entry.getKey() != null && entry.getValue() != null)
                    .collect(toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue));

            Properties properties = new Properties();
            properties.putAll(gradleProps);

            Environment.Builder env = Environment.builder();
//            env.scanClassLoader(getClass().getClassLoader());

//            File rewriteConfig = extension.getConfigFile();
//            if (rewriteConfig.exists()) {
//                try (FileInputStream is = new FileInputStream(rewriteConfig)) {
//                    YamlResourceLoader resourceLoader = new YamlResourceLoader(is, rewriteConfig.toURI(), properties, getClass().getClassLoader());
//                    env.load(resourceLoader);
//                } catch (IOException e) {
//                    throw new RuntimeException("Unable to load rewrite configuration", e);
//                }
//            } else if (extension.getConfigFileSetDeliberately()) {
//                logger.warn("Rewrite configuration file {} does not exist.", rewriteConfig);
//            }

            environment = env.build();
        }
        return environment;
    }

    public List<String> getActiveStyles() {
        return new ArrayList<>(parserConfig.getActiveStyles());
    }

    private List<NamedStyles> getStyles() {
        if (styles == null) {
            styles = environment().activateStyles(getActiveStyles());
//            File checkstyleConfig = extension.getCheckstyleConfigFile();
//            if (checkstyleConfig != null && checkstyleConfig.exists()) {
//                try {
//                    styles.add(CheckstyleConfigLoader.loadCheckstyleConfig(checkstyleConfig.toPath(), extension.getCheckstyleProperties()));
//                } catch (Exception e) {
//                    logger.warn("Unable to parse Checkstyle configuration", e);
//                }
//            }
        }
        return styles;
    }

    private boolean isExcluded(Collection<PathMatcher> exclusions, Path path) {
        for (PathMatcher excluded : exclusions) {
            if (excluded.matches(path)) {
                return true;
            }
        }
        return false;
    }

    private Collection<PathMatcher> pathMatchers(Path basePath, Collection<String> pathExpressions) {
        return pathExpressions.stream()
                .map(o -> basePath.getFileSystem().getPathMatcher("glob:" + o))
                .collect(Collectors.toList());
    }

    private static Collection<String> mergeExclusions(GradleProjectData project, Path baseDir, ParseConfig parserConfig) {
        return Stream.concat(
                project.getSubprojects().stream()
                        .map(subproject -> separatorsToUnix(baseDir.relativize(subproject.getProjectDir().toPath()).toString())),
                parserConfig.getExclusions().stream()
        ).collect(toList());
    }

    private OmniParser omniParser(Set<Path> alreadyParsed) {
        List<Path> buildScriptClasspath = project.getBuildscriptClasspath().stream().map(f -> f.toPath()).collect(toList());
        List<Path> settingsClasspath = project.getSettingsClasspath().stream().map(f -> f.toPath()).collect(toList());
        return OmniParser.builder()
                .plainTextMasks(pathMatchers(baseDir, parserConfig.getPlainTextMasks()))
                .exclusionMatchers(pathMatchers(baseDir, mergeExclusions(project, baseDir, parserConfig)))
                .exclusions(alreadyParsed)
                .sizeThresholdMb(parserConfig.getSizeThresholdMb())
                .parsers(
                        GradleParser.builder()
                                .groovyParser(GroovyParser.builder()
                                        .styles(styles)
                                        .logCompilationWarningsAndErrors(false))
                                .buildscriptClasspath(buildScriptClasspath)
                                .settingsClasspath(settingsClasspath)
                                .build(),
                        new JsonParser(),
                        new XmlParser(),
                        new YamlParser(),
                        new PropertiesParser(),
                        new ProtoParser(),
                        new PlainTextParser()
                )
                .build();
    }

    private <T extends SourceFile> UnaryOperator<T> addProvenance(List<Marker> projectProvenance, @Nullable Marker sourceSet) {
        return s -> {
            Markers m = s.getMarkers();
            for (Marker marker : projectProvenance) {
                m = m.add(marker);
            }
            if (sourceSet != null) {
                m = m.add(sourceSet);
            }
            return s.withMarkers(m);
        };
    }

    protected Stream<SourceFile> parseNonProjectResources(GradleProjectData subproject, Set<Path> alreadyParsed, ExecutionContext ctx, List<Marker> projectProvenance, Stream<SourceFile> sourceFiles) {
        //Collect any additional yaml/properties/xml files that are NOT already in a source set.
        return omniParser(alreadyParsed).parseAll(subproject.getProjectDir().toPath());
    }

    public Stream<SourceFile> parse(GradleProjectData subproject, Set<Path> alreadyParsed, ExecutionContext ctx) {
        GradleProject gradleProjectMarker = GradleProject.fromToolingModel(subproject);
        Collection<PathMatcher> exclusions = parserConfig.getExclusions().stream()
                .map(pattern -> subproject.getProjectDir().toPath().getFileSystem().getPathMatcher("glob:" + pattern))
                .collect(toList());
        if (isExcluded(exclusions, subproject.getProjectDir().toPath())) {
//            logger.lifecycle("Skipping project {} because it is excluded", subproject.getName());
            return Stream.empty();
        }

        try {
//            logger.lifecycle("Scanning sources in project {}", subproject.getName());
            List<NamedStyles> styles = getStyles();
            Collection<JavaSourceSetData> sourceSets = subproject.getJavaSourceSets();
            List<Marker> projectProvenance;
            if (sourceSets.isEmpty()) {
                projectProvenance = sharedProvenance;
            } else {
                projectProvenance = new ArrayList<>(sharedProvenance);
                projectProvenance.add(new JavaProject(randomId(), subproject.getName(),
                        new JavaProject.Publication(subproject.getGroup(),
                                subproject.getName(),
                                subproject.getVersion())));
            }

            Stream<SourceFile> sourceFiles = Stream.of();
            //noinspection DataFlowIssue
            if (subproject.isMultiPlatformKotlinProject()) {
                sourceFiles = parseMultiplatformKotlinProject(subproject, exclusions, alreadyParsed, projectProvenance, ctx);
            }

            for (JavaSourceSetData sourceSet : sourceSets) {
                Stream<SourceFile> sourceSetSourceFiles = Stream.of();
                JavaTypeCache javaTypeCache = new JavaTypeCache();
                JavaVersion javaVersion = new JavaVersion(randomId(), sourceSet.getJavaVersionData().getCreatedBy(),
                        sourceSet.getJavaVersionData().getVmVendor(),
                        sourceSet.getJavaVersionData().getSourceCompatibility(),
                        sourceSet.getJavaVersionData().getTargetCompatibility());

                List<Path> javaPaths = sourceSet.getJava().stream()
                        .filter(it -> it.isFile() && it.getName().endsWith(".java"))
                        .map(File::toPath)
                        .map(Path::toAbsolutePath)
                        .map(Path::normalize)
                        .collect(toList());

                Collection<File> implementationClasspath = sourceSet.getImplementationClasspath();
                // The implementation configuration doesn't include build/source directories from project dependencies
                // So mash it and our rewriteImplementation together to get everything
                List<Path> dependencyPaths = Stream.concat(implementationClasspath.stream(), sourceSet.getCompileClasspath().stream())
                        .map(File::toPath)
                        .map(Path::toAbsolutePath)
                        .map(Path::normalize)
                        .distinct()
                        .collect(toList());

                if (!javaPaths.isEmpty()) {
                    alreadyParsed.addAll(javaPaths);
                    Stream<SourceFile> cus = Stream
                            .of((Supplier<JavaParser>) () -> JavaParser.fromJavaVersion()
                                    .classpath(dependencyPaths)
                                    .styles(styles)
                                    .typeCache(javaTypeCache)
//                                    .logCompilationWarningsAndErrors(extension.getLogCompilationWarningsAndErrors())
                                    .build())
                            .map(Supplier::get)
                            .flatMap(jp -> jp.parse(javaPaths, baseDir, ctx))
                            .map(cu -> {
                                if (isExcluded(exclusions, cu.getSourcePath()) ||
                                        cu.getSourcePath().startsWith(baseDir.relativize(subproject.getBuildDir().toPath()))) {
                                    return null;
                                }
                                return cu;
                            })
                            .filter(Objects::nonNull)
                            .map(it -> it.withMarkers(it.getMarkers().add(javaVersion)));
                    sourceSetSourceFiles = Stream.concat(sourceSetSourceFiles, cus);
//                    logger.info("Scanned {} Java sources in {}/{}", javaPaths.size(), subproject.getName(), sourceSet.getName());
                }

                if (subproject.getPlugins().stream().anyMatch(p -> "org.jetbrains.kotlin.jvm".equals(p.getId()))) {
                    List<Path> kotlinPaths = sourceSet.getSources().stream()
                            .filter(it -> it.isFile() && it.getName().endsWith(".kt"))
                            .map(File::toPath)
                            .map(Path::toAbsolutePath)
                            .map(Path::normalize)
                            .collect(toList());

                    if (!kotlinPaths.isEmpty()) {
                        alreadyParsed.addAll(kotlinPaths);
                        Stream<SourceFile> cus = Stream
                                .of((Supplier<KotlinParser>) () -> KotlinParser.builder()
                                        .classpath(dependencyPaths)
                                        .styles(styles)
                                        .typeCache(javaTypeCache)
//                                        .logCompilationWarningsAndErrors(extension.getLogCompilationWarningsAndErrors())
                                        .build())
                                .map(Supplier::get)
                                .flatMap(kp -> kp.parse(kotlinPaths, baseDir, ctx))
                                .map(cu -> {
                                    if (isExcluded(exclusions, cu.getSourcePath())) {
                                        return null;
                                    }
                                    return cu;
                                })
                                .filter(Objects::nonNull)
                                .map(it -> it.withMarkers(it.getMarkers().add(javaVersion)));
                        sourceSetSourceFiles = Stream.concat(sourceSetSourceFiles, cus);
//                        logger.info("Scanned {} Kotlin sources in {}/{}", kotlinPaths.size(), subproject.getName(), sourceSet.getName());
                    }
                }

                if (subproject.getPlugins().stream().anyMatch(p -> "org.gradle.api.plugins.GroovyPlugin".equals(p.getId()))) {
                    List<Path> groovyPaths = sourceSet.getSources().stream()
                            .filter(it -> it.isFile() && it.getName().endsWith(".groovy"))
                            .map(File::toPath)
                            .map(Path::toAbsolutePath)
                            .map(Path::normalize)
                            .collect(toList());

                    if (!groovyPaths.isEmpty()) {
                        // Groovy sources are aware of java types that are intermixed in the same directory/sourceSet
                        // Include the build directory containing class files so these definitions are available
                        List<Path> dependenciesWithBuildDirs = Stream.concat(
                                dependencyPaths.stream(),
                                sourceSet.getClassesDirs().stream().map(File::toPath)
                        ).collect(toList());

                        alreadyParsed.addAll(groovyPaths);

                        Stream<SourceFile> cus = Stream
                                .of((Supplier<GroovyParser>) () -> GroovyParser.builder()
                                        .classpath(dependenciesWithBuildDirs)
                                        .styles(styles)
                                        .typeCache(javaTypeCache)
                                        .logCompilationWarningsAndErrors(false)
                                        .build())
                                .map(Supplier::get)
                                .flatMap(gp -> gp.parse(groovyPaths, baseDir, ctx))
                                .map(cu -> {
                                    if (isExcluded(exclusions, cu.getSourcePath())) {
                                        return null;
                                    }
                                    return cu;
                                })
                                .filter(Objects::nonNull)
                                .map(it -> it.withMarkers(it.getMarkers().add(javaVersion)));
                        sourceSetSourceFiles = Stream.concat(sourceSetSourceFiles, cus);
//                        logger.info("Scanned {} Groovy sources in {}/{}", groovyPaths.size(), subproject.getName(), sourceSet.getName());
                    }
                }

                for (File resourcesDir : sourceSet.getSourceDirectories()) {
                    if (resourcesDir.exists()) {
                        sourceSetSourceFiles = Stream.concat(sourceSetSourceFiles, omniParser(alreadyParsed)
                                .parseAll(resourcesDir.toPath()));
                    }
                }

                JavaSourceSet sourceSetProvenance = JavaSourceSet.build(sourceSet.getName(), dependencyPaths, javaTypeCache, false);
                sourceFiles = Stream.concat(sourceFiles, sourceSetSourceFiles.map(addProvenance(projectProvenance, sourceSetProvenance)));
            }

            sourceFiles = Stream.concat(sourceFiles, parseNonProjectResources(subproject, alreadyParsed, ctx, projectProvenance, sourceFiles)
                    .map(addProvenance(projectProvenance, null)));

            // Attach GradleProject marker to the build script
            if (this.project.getBuildscriptFile() != null) {
                Path buildScriptPath = baseDir.relativize(this.project.getBuildscriptFile().toPath());
                if (!isExcluded(exclusions, buildScriptPath)) {
                    sourceFiles = sourceFiles.map(sourceFile -> {
                        if (!sourceFile.getSourcePath().equals(buildScriptPath)) {
                            return sourceFile;
                        }
                        try {
                            GradleProject gp = GradleProject.fromToolingModel(subproject);
                            return sourceFile.withMarkers(sourceFile.getMarkers().add(gp));
                        } catch (Exception e) {
                            // Gradle dependency resolution exceptions may be cyclic, which can be a problem for serialization
                            RuntimeException sanitizedException = new RuntimeException(e.getMessage());
                            sanitizedException.setStackTrace(e.getStackTrace());
                            return Markup.warn(sourceFile, sanitizedException);
                        }
                    });
                }
            }

            if (GradleVersion.current().compareTo(GradleVersion.version("4.4")) >= 0) {
                if (project.getSettingsBuildscriptFile() != null) {
                    Path settingsScriptPath = baseDir.relativize(project.getSettingsBuildscriptFile().toPath());
                    if (!isExcluded(exclusions, settingsScriptPath)) {
                        sourceFiles = sourceFiles.map(sourceFile -> {
                            if (!sourceFile.getSourcePath().equals(settingsScriptPath)) {
                                return sourceFile;
                            }
                            try {
                                GradleSettings gs = GradleSettings.fromToolingModel(project.getGradleSettings());
                                return sourceFile.withMarkers(sourceFile.getMarkers().add(gs));
                            } catch (Exception e) {
                                RuntimeException sanitizedException = new RuntimeException(e.getMessage());
                                sanitizedException.setStackTrace(e.getStackTrace());
                                return Markup.warn(sourceFile, sanitizedException);
                            }
                        });
                    }
                }
            }

            return sourceFiles;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Stream<SourceFile> parseMultiplatformKotlinProject(GradleProjectData subproject, Collection<PathMatcher> exclusions, Set<Path> alreadyParsed, List<Marker> projectProvenance, ExecutionContext ctx) {
        Stream<SourceFile> sourceFiles = Stream.of();
        for (KotlinSourceSetData sourceSet : project.getKotlinSourceSets()) {
            try {
                List<Path> kotlinPaths = sourceSet.getKotlin().stream()
                        .filter(it -> it.isFile() && it.getName().endsWith(".kt"))
                        .map(File::toPath)
                        .map(Path::toAbsolutePath)
                        .map(Path::normalize)
                        .collect(toList());

                // The implementation configuration doesn't include build/source directories from project dependencies
                // So mash it and our rewriteImplementation together to get everything
                List<Path> dependencyPaths = Stream.concat(sourceSet.getImplementationClasspath().stream(), sourceSet.getCompileClasspath().stream())
                        .map(File::toPath)
                        .map(Path::toAbsolutePath)
                        .map(Path::normalize)
                        .distinct()
                        .collect(toList());

                if (!kotlinPaths.isEmpty()) {
                    JavaTypeCache javaTypeCache = new JavaTypeCache();
                    KotlinParser kp = KotlinParser.builder()
                            .classpath(dependencyPaths)
                            .styles(getStyles())
                            .typeCache(javaTypeCache)
//                            .logCompilationWarningsAndErrors(extension.getLogCompilationWarningsAndErrors())
                            .build();

                    Stream<SourceFile> cus = kp.parse(kotlinPaths, baseDir, ctx);
                    alreadyParsed.addAll(kotlinPaths);
                    cus = cus.map(cu -> {
                        if (isExcluded(exclusions, cu.getSourcePath())) {
                            return null;
                        }
                        return cu;
                    }).filter(Objects::nonNull);
                    JavaSourceSet sourceSetProvenance = JavaSourceSet.build(sourceSet.getName(), dependencyPaths, javaTypeCache, false);

                    sourceFiles = Stream.concat(sourceFiles, cus.map(addProvenance(projectProvenance, sourceSetProvenance)));
//                    logger.info("Scanned {} Kotlin sources in {}/{}", kotlinPaths.size(), subproject.getName(), sourceSet.getName());
                }
                return sourceFiles;
            } catch (Exception e) {
//                logger.warn("Failed to resolve sourceSet from {}:{}. Some type information may be incomplete",
//                        subproject.getPath(), sourceSet.getName());
            }
        }

        return Stream.empty();
    }

}

