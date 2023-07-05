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

import lombok.RequiredArgsConstructor;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.handler.ArtifactHandler;
import org.apache.maven.artifact.metadata.ArtifactMetadata;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.OverConstrainedVersionException;
import org.apache.maven.artifact.versioning.VersionRange;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.artifact.PluginArtifact;
import org.apache.maven.rtinfo.RuntimeInformation;
import org.apache.maven.rtinfo.internal.DefaultRuntimeInformation;
import org.apache.maven.settings.crypto.SettingsDecrypter;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.jetbrains.annotations.NotNull;
import org.openrewrite.marker.Marker;
import org.openrewrite.maven.MavenMojoProjectParser;
import org.springframework.core.io.Resource;
import org.springframework.sbm.utils.ResourceUtil;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

/**
 * @author Fabian Krüger
 */
@RequiredArgsConstructor
public class ProvenanceMarkerFactory {

    private final ParserSettings parserSettings;

    /**
     * See {@link MavenMojoProjectParser#generateProvenance(MavenProject)}.
     */
    public Map<Resource, List<? extends Marker>> generateProvenanceMarkers(Path baseDir, Stream<Resource> pomFileResources) {

        RuntimeInformation runtimeInformation = new DefaultRuntimeInformation();
        MavenSession mavenSession = null;
        SettingsDecrypter settingsDecrypter = null;

        MavenMojoProjectParser helper = new MavenMojoProjectParser(
                getLogger(parserSettings),
                baseDir,
                parserSettings.isPomCacheEnabled(),
                parserSettings.getPomCacheDirectory(),
                runtimeInformation,
                parserSettings.isSkipMavenParsing(),
                parserSettings.getExclusions(),
                parserSettings.getPlainTextMasks(),
                parserSettings.getSizeThresholdMb(),
                mavenSession,
                settingsDecrypter,
                parserSettings.isRunPerSubmodule()
        );
        Map<Resource, List<? extends Marker>> result = new HashMap<>();
        pomFileResources.forEach(pom -> {
            MavenProject mavenProject = createMavenProject(pom);
            List<? extends Marker> markers = helper.generateProvenance(mavenProject);
            result.put(pom, markers);
        });
        return result;
    }

    private MavenProject createMavenProject(Resource pom) {
        try {
            MavenXpp3Reader reader = new MavenXpp3Reader();
            Model model = reader.read(ResourceUtil.getInputStream(pom));
            MavenProject mavenProject = new MavenProject(model);
            mavenProject.setName(model.getName());
            mavenProject.setGroupId(model.getGroupId());
            mavenProject.setArtifactId(model.getArtifactId());
            mavenProject.setVersion(model.getVersion());
            if(model.getBuild() != null){
                Plugin plugin = model.getBuild().getPlugins().get(0);
                PluginArtifact pluginArtifact = new PluginArtifact(plugin, new Artifact() {
                    @Override
                    public String getGroupId() {
                        return null;
                    }

                    @Override
                    public String getArtifactId() {
                        return null;
                    }

                    @Override
                    public String getVersion() {
                        return null;
                    }

                    @Override
                    public void setVersion(String s) {

                    }

                    @Override
                    public String getScope() {
                        return null;
                    }

                    @Override
                    public String getType() {
                        return null;
                    }

                    @Override
                    public String getClassifier() {
                        return null;
                    }

                    @Override
                    public boolean hasClassifier() {
                        return false;
                    }

                    @Override
                    public File getFile() {
                        return new File("pom.xml");
                    }

                    @Override
                    public void setFile(File file) {

                    }

                    @Override
                    public String getBaseVersion() {
                        return null;
                    }

                    @Override
                    public void setBaseVersion(String s) {

                    }

                    @Override
                    public String getId() {
                        return null;
                    }

                    @Override
                    public String getDependencyConflictId() {
                        return null;
                    }

                    @Override
                    public void addMetadata(ArtifactMetadata artifactMetadata) {

                    }

                    @Override
                    public Collection<ArtifactMetadata> getMetadataList() {
                        return null;
                    }

                    @Override
                    public void setRepository(ArtifactRepository artifactRepository) {

                    }

                    @Override
                    public ArtifactRepository getRepository() {
                        return null;
                    }

                    @Override
                    public void updateVersion(String s, ArtifactRepository artifactRepository) {

                    }

                    @Override
                    public String getDownloadUrl() {
                        return null;
                    }

                    @Override
                    public void setDownloadUrl(String s) {

                    }

                    @Override
                    public ArtifactFilter getDependencyFilter() {
                        return null;
                    }

                    @Override
                    public void setDependencyFilter(ArtifactFilter artifactFilter) {

                    }

                    @Override
                    public ArtifactHandler getArtifactHandler() {
                        return null;
                    }

                    @Override
                    public List<String> getDependencyTrail() {
                        return null;
                    }

                    @Override
                    public void setDependencyTrail(List<String> list) {

                    }

                    @Override
                    public void setScope(String s) {

                    }

                    @Override
                    public VersionRange getVersionRange() {
                        return null;
                    }

                    @Override
                    public void setVersionRange(VersionRange versionRange) {

                    }

                    @Override
                    public void selectVersion(String s) {

                    }

                    @Override
                    public void setGroupId(String s) {

                    }

                    @Override
                    public void setArtifactId(String s) {

                    }

                    @Override
                    public boolean isSnapshot() {
                        return false;
                    }

                    @Override
                    public void setResolved(boolean b) {

                    }

                    @Override
                    public boolean isResolved() {
                        return false;
                    }

                    @Override
                    public void setResolvedVersion(String s) {

                    }

                    @Override
                    public void setArtifactHandler(ArtifactHandler artifactHandler) {

                    }

                    @Override
                    public boolean isRelease() {
                        return false;
                    }

                    @Override
                    public void setRelease(boolean b) {

                    }

                    @Override
                    public List<ArtifactVersion> getAvailableVersions() {
                        return null;
                    }

                    @Override
                    public void setAvailableVersions(List<ArtifactVersion> list) {

                    }

                    @Override
                    public boolean isOptional() {
                        return false;
                    }

                    @Override
                    public void setOptional(boolean b) {

                    }

                    @Override
                    public ArtifactVersion getSelectedVersion() throws OverConstrainedVersionException {
                        return null;
                    }

                    @Override
                    public boolean isSelectedVersionKnown() throws OverConstrainedVersionException {
                        return false;
                    }

                    @Override
                    public int compareTo(@NotNull Artifact o) {
                        return 0;
                    }
                });
                mavenProject.setPluginArtifacts(Set.of(pluginArtifact));
            }
            return mavenProject;
        } catch (IOException | XmlPullParserException e) {
            throw new RuntimeException(e);
        }
    }

    private Log getLogger(ParserSettings parserSettings) {
        String loggerClassName = parserSettings.getLoggerClass();
        Log log = new SystemStreamLog();
        if(loggerClassName != null) {
            try {
                Class<?> loggerClass = Class.forName(loggerClassName);
                Object loggerObj = loggerClass.getConstructor().newInstance();
                if(loggerObj instanceof Log logger) {
                    log = logger;
                } else {
                    throw new ClassCastException("Class name provided as 'parser.loggerClass' is not of type %s".formatted(Log.class.getName()));
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Could not find type '%s' which is provided as 'parser.loggerClass'".formatted(loggerClassName), e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("Could not find default constructor on logger class type: '%s' which is provided as 'parser.loggerClass'".formatted(loggerClassName), e);
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException("Could not invoke default constructor in logger class '%s' which is provided as 'parser.loggerClass'".formatted(loggerClassName), e);
            }
        }
        return log;
    }
}
