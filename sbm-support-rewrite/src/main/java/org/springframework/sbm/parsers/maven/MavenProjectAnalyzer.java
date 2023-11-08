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
package org.springframework.sbm.parsers.maven;

import org.apache.maven.model.*;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.openrewrite.maven.utilities.MavenArtifactDownloader;
import org.springframework.core.io.Resource;
import org.springframework.sbm.parsers.MavenProject;
import org.springframework.sbm.parsers.ParserContext;
import org.springframework.sbm.utils.ResourceUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Implements the ordering of Maven (reactor) build projects.
 * See <a href="https://maven.apache.org/guides/mini/guide-multiple-modules.html#reactor-sorting">Reactor Sorting</a>
 *
 * @author Fabian Krüger
 */
public class MavenProjectAnalyzer {

    private static final String POM_XML = "pom.xml";
    private static final MavenXpp3Reader XPP_3_READER = new MavenXpp3Reader();
    private final MavenArtifactDownloader rewriteMavenArtifactDownloader;

    public MavenProjectAnalyzer(MavenArtifactDownloader rewriteMavenArtifactDownloader) {
        this.rewriteMavenArtifactDownloader = rewriteMavenArtifactDownloader;
    }

    public List<MavenProject> getSortedProjects(Path baseDir, List<Resource> resources) {

        List<Resource> allPomFiles = resources.stream().filter(r -> ResourceUtil.getPath(r).getFileName().toString().equals(POM_XML)).toList();

        if (allPomFiles.isEmpty()) {
            throw new IllegalArgumentException("The provided resources did not contain any 'pom.xml' file.");
        }

        Resource rootPom = allPomFiles.stream().filter(r -> ResourceUtil.getPath(r).toString().equals(baseDir.resolve(POM_XML).normalize().toString())).findFirst().orElseThrow(() -> new IllegalArgumentException("The provided resources do not contain a root 'pom.xml' file."));
        Model rootPomModel = new Model(rootPom);

        if (isSingleModuleProject(rootPomModel)) {
            return List.of(new MavenProject(baseDir, rootPom, rootPomModel, rewriteMavenArtifactDownloader, resources));
        }
        List<Model> reactorModels = new ArrayList<>();
        recursivelyFindReactorModules(baseDir, null, reactorModels, allPomFiles, rootPomModel);
        List<Model> sortedModels = sortModels(reactorModels);
        return map(baseDir, resources, sortedModels);
    }

    private List<MavenProject> map(Path baseDir, List<Resource> resources, List<Model> sortedModels) {
        List<MavenProject> mavenProjects = new ArrayList<>();
        sortedModels
                .stream()
                .filter(Objects::nonNull)
                .forEach(m -> {
                    String projectDir = baseDir.resolve(m.getProjectDirectory().toString()).normalize().toString();
                    List<Resource> filteredResources = resources.stream().filter(r -> ResourceUtil.getPath(r).toString().startsWith(projectDir)).toList();
                    mavenProjects.add(new MavenProject(baseDir, m.getResource(), m, rewriteMavenArtifactDownloader, filteredResources));
        });
        // set all non parent poms as collected projects for root parent pom
        List<MavenProject> collected = new ArrayList<>(mavenProjects);
        collected.remove(0);
        mavenProjects.get(0).setCollectedProjects(collected);
        return mavenProjects;
    }

    private List<Model> recursivelyFindReactorModules(Path baseDir, String path, List<Model> reactorModels, List<Resource> allPomFiles, Model pomModel) {
        // TODO: verify given module is root pom
        if(pomModel != null) {
            reactorModels.add(pomModel);
        } else {
            throw new IllegalStateException("PomModel was null.");
        }

        List<String> moduleNames = pomModel.getModules();

        moduleNames.stream()
                .forEach(moduleName -> {

                    String modulePathSegment = path == null ? moduleName : path + "/" + moduleName;

                    allPomFiles.stream()
                            .filter(resource -> {
                                String modulePath = baseDir.resolve(modulePathSegment).resolve(POM_XML).toAbsolutePath().normalize().toString();
                                String resourcePath = ResourceUtil.getPath(resource).toAbsolutePath().normalize().toString();
                                return resourcePath.equals(modulePath);
                            })
                            .map(Model::new)
                            .forEach(m -> recursivelyFindReactorModules(baseDir, modulePathSegment,  reactorModels, allPomFiles, m).stream());
                });
        return reactorModels;
    }

    public List<Model> sortModels(List<Model> reactorModels) {
        List<Model> sortedModels = new ArrayList<>();
        Map<String, Model> gaToModelMap = reactorModels.stream()
                .collect(Collectors.toMap(
                        m ->{
                            return (m.getGroupId() == null ? (m.getParent() == null ? null : m.getParent().getGroupId()) : m.getGroupId()) + ":" + m.getArtifactId();
                        } ,
                        m -> m
                ));

        Map<Model, List<Model>> dependsOn = new HashMap<>();

        for (Model m : reactorModels) {
            addToDependants(m, dependsOn, null);
            if (hasParent(m)) {
                String parentGa = m.getParent().getGroupId() + ":" + m.getParent().getArtifactId();
                Model parentModel = gaToModelMap.get(parentGa);
                addToDependants(m, dependsOn, parentModel);
            }
            for (Dependency d : m.getDependencies()) {
                String dependencyGa = getGav(d);
                if (gaToModelMap.containsKey(dependencyGa)) {
                    Model dependencyModel = gaToModelMap.get(dependencyGa);
                    addToDependants(m, dependsOn, dependencyModel);
                }
            }
            if (m.getBuild() != null && m.getBuild().getPluginsAsMap() != null && !m.getBuild().getPluginsAsMap().isEmpty()) {
                for (String pluginName : m.getBuild().getPluginsAsMap().keySet()) {
                    // TODO: find plugin dependencies
                }
            }
        }

        ArrayList<Map.Entry<Model, List<Model>>> entries = new ArrayList<>(dependsOn.entrySet());

        // sort entries by number of values
        entries.stream()
                .sorted((e1, e2) -> {
                    int compare = Integer.compare(e1.getValue().size(), e2.getValue().size());
                    if(compare != 0) {
                        return compare;
                    } else {
                        // with same number of dependencies order by dependant
                        if(e1.getValue().contains(e2.getKey())) {
                            return 1;
                        } else if(e2.getValue().contains(e1.getKey())) {
                            return -1;
                        } else {
                            // default is order as given by reactorModels
                            return Integer.compare(reactorModels.indexOf(e1.getKey()), reactorModels.indexOf(e2.getKey()));
                        }
                    }
                })
                .forEach(e -> sortedModels.add(e.getKey()));
        return sortedModels;
    }

    private void addToDependants(Model model, Map<Model, List<Model>> dependsOn, Model dependantModel) {
        if (!dependsOn.containsKey(model)) {
            dependsOn.put(model, new ArrayList<>());
        }
        if (dependantModel != null) {
            dependsOn.get(model).add(dependantModel);
        }
    }

    private boolean hasParent(Model m) {
        return m.getParent() != null;
    }

    private String getGav(Dependency d) {
        return d.getGroupId() + ":" + d.getArtifactId();
    }

    private static boolean isSingleModuleProject(Model rootPomModel) {
        return !rootPomModel.getPackaging().equals("pom");
    }

    public ParserContext createParserContext(Path baseDir, List<Resource> resources) {
        List<MavenProject> sortedProjectsList = getSortedProjects(baseDir, resources);
        ParserContext parserContext = new ParserContext(baseDir, resources, sortedProjectsList);
        return parserContext;
    }

    static class Model extends org.apache.maven.model.Model {
        private final Resource resource;
        private final org.apache.maven.model.Model delegate;

        public Model(Resource resource) {
            this.resource = resource;
            try {
                this.delegate = XPP_3_READER.read(ResourceUtil.getInputStream(resource));
                this.delegate.setPomFile(resource.getFile());
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (XmlPullParserException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public String toString() {
            return (delegate.getGroupId() == null ? delegate.getParent().getGroupId() : delegate.getGroupId()) + ":" + delegate.getArtifactId();
        }

        public Resource getResource() {
            return resource;
        }

        @Override
        public String getArtifactId() {
            return delegate.getArtifactId();
        }

        @Override
        public Build getBuild() {
            return delegate.getBuild();
        }

        @Override
        public String getChildProjectUrlInheritAppendPath() {
            return delegate.getChildProjectUrlInheritAppendPath();
        }

        @Override
        public CiManagement getCiManagement() {
            return delegate.getCiManagement();
        }

        @Override
        public List<Contributor> getContributors() {
            return delegate.getContributors();
        }

        @Override
        public String getDescription() {
            return delegate.getDescription();
        }

        @Override
        public List<Developer> getDevelopers() {
            return delegate.getDevelopers();
        }

        @Override
        public String getGroupId() {
            return delegate.getGroupId();
        }

        @Override
        public String getInceptionYear() {
            return delegate.getInceptionYear();
        }

        @Override
        public IssueManagement getIssueManagement() {
            return delegate.getIssueManagement();
        }

        @Override
        public List<License> getLicenses() {
            return delegate.getLicenses();
        }

        @Override
        public List<MailingList> getMailingLists() {
            return delegate.getMailingLists();
        }

        @Override
        public String getModelEncoding() {
            return delegate.getModelEncoding();
        }

        @Override
        public String getModelVersion() {
            return delegate.getModelVersion();
        }

        @Override
        public String getName() {
            String name = delegate.getName();
            if(name == null) {
                name = delegate.getArtifactId();
            }
            return name;
        }

        @Override
        public Organization getOrganization() {
            return delegate.getOrganization();
        }

        @Override
        public String getPackaging() {
            return delegate.getPackaging();
        }

        @Override
        public Parent getParent() {
            return delegate.getParent();
        }

        @Override
        public Prerequisites getPrerequisites() {
            return delegate.getPrerequisites();
        }

        @Override
        public List<Profile> getProfiles() {
            return delegate.getProfiles();
        }

        @Override
        public Scm getScm() {
            return delegate.getScm();
        }

        @Override
        public String getUrl() {
            return delegate.getUrl();
        }

        @Override
        public String getVersion() {
            return delegate.getVersion();
        }

        @Override
        public File getPomFile() {
            return delegate.getPomFile();
        }

        @Override
        public File getProjectDirectory() {
            return delegate.getPomFile().toPath().getParent().toFile();
        }

        @Override
        public String getId() {
            return delegate.getId();
        }

        @Override
        public List<Dependency> getDependencies() {
            return delegate.getDependencies();
        }

        @Override
        public DependencyManagement getDependencyManagement() {
            return delegate.getDependencyManagement();
        }

        @Override
        public DistributionManagement getDistributionManagement() {
            return delegate.getDistributionManagement();
        }

        @Override
        public InputLocation getLocation(Object key) {
            return delegate.getLocation(key);
        }

        @Override
        public List<String> getModules() {
            return delegate.getModules();
        }

        @Override
        public List<Repository> getPluginRepositories() {
            return delegate.getPluginRepositories();
        }

        @Override
        public Properties getProperties() {
            return delegate.getProperties();
        }

        @Override
        public Reporting getReporting() {
            return delegate.getReporting();
        }

        @Override
        public Object getReports() {
            return delegate.getReports();
        }

        @Override
        public List<Repository> getRepositories() {
            return delegate.getRepositories();
        }


    }
}
