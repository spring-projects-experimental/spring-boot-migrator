/*
 * Copyright 2023 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.sbm.gradle.tooling;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.file.SourceDirectorySet;
import org.gradle.api.initialization.Settings;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.compile.JavaCompile;
import org.gradle.invocation.DefaultGradle;
import org.gradle.util.GradleVersion;
import org.openrewrite.gradle.marker.*;
import org.openrewrite.maven.tree.MavenRepository;

import java.io.File;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Value
@AllArgsConstructor
public class GradleProjectDataImpl implements Serializable {

    private static Class<?>[] SUPPORTED_GRADLE_PROPERTY_VALUE_TYPES =new Class<?>[] {
            Number.class,
            Boolean.class,
            String.class,
            Character.class
    };

    private final String name;
    private final String path;
    private final String group;
    private final String version;
    private final List<GradlePluginDescriptor> plugins;
    private final List<MavenRepository> mavenRepositories;
    private final List<MavenRepository> mavenPluginRepositories;
    private final Map<String, GradleDependencyConfiguration> nameToConfiguration;

    private final GradleSettings gradleSettings;
    private final String gradleVersion;
    private final boolean rootProject;
    private final File rootProjectDir;
    private final Collection<GradleProjectDataImpl> subprojects;
    private final File projectDir;
    private final File buildDir;
    private final File buildscriptFile;
    private final File settingsBuildscriptFile;
    private final Map<String, ?> properties;
    private final Collection<JavaSourceSetDataImpl> javaSourceSets;
    private final boolean multiPlatformKotlinProject;
    private final Collection<KotlinSourceSetDataImpl> kotlinSourceSets;
    private final Collection<File> buildscriptClasspath;
    private final Collection<File> settingsClasspath;

    public static GradleProjectDataImpl from(Project project) {
        GradleProject gardleProjectMarker = GradleProjectBuilder.gradleProject(project);
        return new GradleProjectDataImpl(
                gardleProjectMarker.getName(),
                gardleProjectMarker.getPath(),
                gardleProjectMarker.getGroup(),
                gardleProjectMarker.getVersion(),
                gardleProjectMarker.getPlugins(),
                gardleProjectMarker.getMavenRepositories(),
                gardleProjectMarker.getMavenPluginRepositories(),
                gardleProjectMarker.getNameToConfiguration(),

                GradleSettingsBuilder.gradleSettings(((DefaultGradle)project.getGradle()).getSettings()),
                project.getGradle().getGradleVersion(),
                project == project.getRootProject(),
                project.getRootProject().getProjectDir(),
                subprojects(project.getSubprojects()),
                project.getProjectDir(),
                project.getBuildDir(),
                project.getBuildscript().getSourceFile(),
                settingsBuildscriptFile(project),
                properties(project.getProperties()),
                javaSourceSets(project),
                isMultiPlatformKotlinProject(project),
                kotlinSourceSets(project),
                project.getBuildscript().getConfigurations().getByName("classpath").resolve(),
                settingsClasspath(project)
        );
    }

    private static Collection<GradleProjectDataImpl> subprojects(Collection<Project> subprojects) {
        List<GradleProjectDataImpl> sub = new ArrayList(subprojects.size());
        Iterator var2 = subprojects.iterator();
        while(var2.hasNext()) {
            Project s = (Project)var2.next();
            sub.add(from(s));
        }
        return sub;
    }

    private static File settingsBuildscriptFile(Project project) {
        Settings settings = ((DefaultGradle)project.getGradle()).getSettings();
        return settings.getBuildscript().getSourceFile() == null ? null : settings.getBuildscript().getSourceFile();
    }

    private static Map<String, ?> properties(Map<String, ?> props) {
        return props.entrySet()
                .stream()
                .filter(e -> Arrays.stream(SUPPORTED_GRADLE_PROPERTY_VALUE_TYPES).anyMatch(c -> c.isInstance(e.getValue())))
                .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
    }

    private static List<JavaSourceSetDataImpl> javaSourceSets(Project project) {
        JavaPluginConvention javaConvention = (JavaPluginConvention)project.getConvention().findPlugin(JavaPluginConvention.class);
        if (javaConvention == null) {
            return Collections.emptyList();
        } else {
            List<JavaSourceSetDataImpl> sourceSetData = new ArrayList(javaConvention.getSourceSets().size());
            Iterator var3 = javaConvention.getSourceSets().iterator();

            while(var3.hasNext()) {
                final SourceSet sourceSet = (SourceSet)var3.next();
                sourceSetData.add(new JavaSourceSetDataImpl(
                        sourceSet.getName(),
                        sourceSet.getAllSource().getFiles(),
                        sourceSet.getResources().getSourceDirectories().getFiles(),
                        sourceSet.getAllJava().getFiles(),
                        sourceSet.getOutput().getClassesDirs().getFiles(),
                        sourceSet.getCompileClasspath().getFiles(),
                        javaSourceSetImplementationClasspath(project, sourceSet),
                        sourceSetJavaVersion(project, sourceSet)
                ));
            }
            return sourceSetData;
        }
    }

    private static Collection<File> javaSourceSetImplementationClasspath(Project project, SourceSet sourceSet) {
        Configuration implementation = project.getConfigurations().getByName(sourceSet.getImplementationConfigurationName());
        Configuration rewriteImplementation = project.getConfigurations().maybeCreate("rewrite" + sourceSet.getImplementationConfigurationName());
        rewriteImplementation.extendsFrom(new Configuration[]{implementation});

        try {
            return rewriteImplementation.resolve();
        } catch (Exception var5) {
            return Collections.emptySet();
        }
    }

    private static JavaVersionDataImpl sourceSetJavaVersion(Project project, SourceSet sourceSet) {
        final JavaCompile javaCompileTask = (JavaCompile)project.getTasks().getByName(sourceSet.getCompileJavaTaskName());
        return new JavaVersionDataImpl(
                System.getProperty("java.runtime.version"),
                System.getProperty("java.vm.vendor"),
                javaCompileTask.getSourceCompatibility(),
                javaCompileTask.getTargetCompatibility()
        );
    }

    private static boolean isMultiPlatformKotlinProject(Project project) {
        try {
            return project.getPlugins().hasPlugin("org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension") || project.getExtensions().findByName("kotlin") != null && project.getExtensions().findByName("kotlin").getClass().getCanonicalName().startsWith("org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension");
        } catch (Throwable t) {
            return false;
        }
    }

    private static Collection<KotlinSourceSetDataImpl> kotlinSourceSets(Project project) {
        NamedDomainObjectContainer sourceSets;
        try {
            Object kotlinExtension = project.getExtensions().getByName("kotlin");
            Class<?> clazz = kotlinExtension.getClass().getClassLoader().loadClass("org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension");
            sourceSets = (NamedDomainObjectContainer)clazz.getMethod("getSourceSets").invoke(kotlinExtension);
        } catch (Exception e) {
            return Collections.emptyList();
        }

        SortedSet sourceSetNames;
        try {
            sourceSetNames = (SortedSet)sourceSets.getClass().getMethod("getNames").invoke(sourceSets);
        } catch (Exception var19) {
            return Collections.emptyList();
        }

        List<KotlinSourceSetDataImpl> kotlinSourceSetData = new ArrayList(sourceSetNames.size());
        Iterator it = sourceSetNames.iterator();

        while(it.hasNext()) {
            final String sourceSetName = (String)it.next();

            try {
                Object sourceSet = sourceSets.getClass().getMethod("getByName", String.class).invoke(sourceSets, sourceSetName);
                final SourceDirectorySet kotlinDirectorySet = (SourceDirectorySet)sourceSet.getClass().getMethod("getKotlin").invoke(sourceSet);
                String implementationName = (String)sourceSet.getClass().getMethod("getImplementationConfigurationName").invoke(sourceSet);
                Configuration implementation = project.getConfigurations().getByName(implementationName);
                Configuration rewriteImplementation = (Configuration)project.getConfigurations().maybeCreate("rewrite" + implementationName);
                rewriteImplementation.extendsFrom(new Configuration[]{implementation});

                Set implementationClasspath;
                try {
                    implementationClasspath = rewriteImplementation.resolve();
                } catch (Exception e) {
                    implementationClasspath = Collections.emptySet();
                }

                String compileName = (String)sourceSet.getClass().getMethod("getCompileOnlyConfigurationName").invoke(sourceSet);
                Configuration compileOnly = project.getConfigurations().getByName(compileName);
                Configuration rewriteCompileOnly = (Configuration)project.getConfigurations().maybeCreate("rewrite" + compileName);
                rewriteCompileOnly.setCanBeResolved(true);
                rewriteCompileOnly.extendsFrom(new Configuration[]{compileOnly});
                final Set<File> compClasspath = rewriteCompileOnly.getFiles();
                kotlinSourceSetData.add(new KotlinSourceSetDataImpl(
                        sourceSetName,
                        kotlinDirectorySet.getFiles(),
                        compClasspath,
                        implementationClasspath
                ));
            } catch (Exception e) {
            }
        }

        return kotlinSourceSetData;
    }

    private static Collection<File> settingsClasspath(Project project) {
        if (GradleVersion.current().compareTo(GradleVersion.version("4.4")) >= 0) {
            Settings settings = ((DefaultGradle)project.getGradle()).getSettings();
            return settings.getBuildscript().getConfigurations().getByName("classpath").resolve();
        } else {
            return Collections.emptyList();
        }
    }


}
