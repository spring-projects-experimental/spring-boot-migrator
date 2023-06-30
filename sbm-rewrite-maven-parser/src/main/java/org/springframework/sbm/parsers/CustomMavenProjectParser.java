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
package org.springframework.sbm.parsers;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.apache.maven.rtinfo.RuntimeInformation;
import org.apache.maven.settings.crypto.SettingsDecrypter;
import org.openrewrite.ExecutionContext;
import org.openrewrite.SourceFile;
import org.openrewrite.internal.lang.Nullable;
import org.openrewrite.maven.AbstractRewriteMojo;
import org.openrewrite.maven.AbstractRewriteRunMojo;
import org.openrewrite.maven.MavenMojoProjectParser;
import org.openrewrite.style.NamedStyles;
import org.springframework.core.io.Resource;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

/**
 * @author Fabian Krüger
 */
public class CustomMavenProjectParser extends MavenMojoProjectParser {

    private static boolean runPerSubmodule = false;

    public CustomMavenProjectParser(Log logger, Path baseDir, boolean pomCacheEnabled, @Nullable String pomCacheDirectory, RuntimeInformation runtime, boolean skipMavenParsing, Collection<String> exclusions, Collection<String> plainTextMasks, int sizeThresholdMb, MavenSession session, SettingsDecrypter settingsDecrypter, boolean runPerSubmodule) {
        super(logger, baseDir, pomCacheEnabled, pomCacheDirectory, runtime, skipMavenParsing, exclusions, plainTextMasks, sizeThresholdMb, session, settingsDecrypter, runPerSubmodule);
    }

    public RewriteProjectParsingResult parse(Path baseDir, List<Resource> resources, ExecutionContext executionContext) {
        try {
            List<NamedStyles> styles = List.of();
            Stream<SourceFile> sourceFilesStream = null;
            sourceFilesStream = parseToAst(baseDir, resources, styles, executionContext);
            DummyRewriteMojo dummyRewriteMojo = new DummyRewriteMojo();
            Method sourcesWithAutoDetectedStylesMethod = ReflectionUtils.findMethod(DummyRewriteMojo.class, "sourcesWithAutoDetectedStyles");
            ReflectionUtils.makeAccessible(sourcesWithAutoDetectedStylesMethod);
            Object o = ReflectionUtils.invokeMethod(sourcesWithAutoDetectedStylesMethod, dummyRewriteMojo, sourceFilesStream);
            List<SourceFile> sourceFiles = (List<SourceFile>) o;
            // TODO: Make exception handler configurable
            // TODO: Retrieve ExecutionContext implementation from properties
            return new RewriteProjectParsingResult(sourceFiles, executionContext);
        } catch (DependencyResolutionRequiredException e) {
            throw new RuntimeException(e);
        } catch (MojoExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private Stream<SourceFile> parseToAst(Path baseDir, List<Resource> resources, List<NamedStyles> styles, ExecutionContext executionContext) throws DependencyResolutionRequiredException, MojoExecutionException {
        MavenProject mavenProject = createFakeMavenProjectForProvenance(baseDir, resources, executionContext);
        return listSourceFiles(mavenProject, styles, executionContext);
    }

    private MavenProject createFakeMavenProjectForProvenance(Path baseDir, List<Resource> resources, ExecutionContext executionContext) {
        MavenProject mavenProject = new MavenProject();
        // Plugin compilerPlugin = mavenProject.getPlugin("org.apache.maven.plugins:maven-compiler-plugin");
        mavenProject.setPluginArtifacts(Set.of());
        return mavenProject;
    }

    class DummyRewriteMojo extends AbstractRewriteMojo {

        @Override
        public void execute() throws MojoExecutionException, MojoFailureException {

        }
    }

}
