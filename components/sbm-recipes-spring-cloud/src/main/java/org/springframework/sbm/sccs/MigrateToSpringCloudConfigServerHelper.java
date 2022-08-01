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
package org.springframework.sbm.sccs;

import org.springframework.sbm.boot.properties.api.SpringBootApplicationProperties;
import org.springframework.sbm.boot.properties.api.SpringProfile;
import org.springframework.sbm.boot.properties.search.SpringBootApplicationPropertiesResourceListFilter;
import org.springframework.sbm.engine.git.GitSupport;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class MigrateToSpringCloudConfigServerHelper {

    private String regex = ".*/application[-\\w]{0,1}.properties";
    private Pattern profilePattern = Pattern.compile(regex);
    private GitSupport gitSupport;

    public MigrateToSpringCloudConfigServerHelper(GitSupport gitSupport) {
        this.gitSupport = gitSupport;
    }

    List<SpringProfile> findAllSpringProfiles(ProjectContext projectContext) {
        List<SpringBootApplicationProperties> applicationProperties = projectContext.search(new SpringBootApplicationPropertiesResourceListFilter());
        List<SpringProfile> profilesFound = new ArrayList<>();
        applicationProperties.forEach(ap -> {
            profilesFound.add(ap.getSpringProfile());
        });

        projectContext.getProjectJavaSources().list().stream()
                .map(js -> js.getTypes())
                .flatMap(List::stream)
                .filter(t -> t.hasAnnotation("org.springframework.context.annotation.Profile"))
                .map(t -> t.getAnnotation("org.springframework.context.annotation.Profile"))
                .map(a -> a.getAttribute("value"))
                .map(a -> a.printAssignmentValue())
                .forEach(a -> {
                    if (!profilesFound.contains(a)) profilesFound.add(new SpringProfile(a));
                });

        return profilesFound;
    }

    List<SpringBootApplicationProperties> findAllSpringApplicationProperties(ProjectContext context) {
        return context.search(new SpringBootApplicationPropertiesResourceListFilter());
    }

    Path initializeSccsProjectDir(Path projectRootDirectory) {
        Path parent = projectRootDirectory.getParent();
        if (parent == null) {
            throw new RuntimeException(String.format("Cold not get parent dir of project root '' as base path to create directory.", projectRootDirectory));
        }
        String projectName = projectRootDirectory.toFile().getName();
        Path sccsBaseDir = parent.resolve(projectName + "-config");
        if (sccsBaseDir.toFile().exists()) {
            throw new RuntimeException(String.format("The directory '%s' attempted to use for the Spring Cloud Config Server project already exists.", sccsBaseDir));
        }
        try {
            sccsBaseDir = Files.createDirectory(sccsBaseDir);
        } catch (IOException e) {
            throw new RuntimeException(String.format("Failed to create the directory '%s'for the Spring Cloud Config Server project.", sccsBaseDir));
        }
        gitSupport.initGit(sccsBaseDir.toFile());
        return sccsBaseDir;
    }

    // FIXME: if the recipe fails this action can't be reverted
    void copyFiles(List<SpringBootApplicationProperties> bootApplicationProperties, Path sccsProjectDir) {
        bootApplicationProperties.forEach(p ->
                {
                    try {
                        Path fileName = p.getAbsolutePath().getFileName();
                        Files.copy(p.getAbsolutePath(), sccsProjectDir.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }

    void commitProperties(Path sccsProjectDir, List<SpringBootApplicationProperties> bootApplicationProperties) {
        File repo = sccsProjectDir.toFile();

        List<String> modifiedResources = bootApplicationProperties.stream().map(p -> p.getAbsolutePath().toString()).collect(Collectors.toList());
        List<String> listOfFiles = bootApplicationProperties.stream().map(p -> p.getAbsolutePath().getFileName().toString()).collect(Collectors.toList());
        String commitMessage = "Added properties files: " + listOfFiles.stream().collect(Collectors.joining(", "));
        gitSupport.addAllAndCommit(repo, commitMessage, modifiedResources, List.of());
    }

    void configureSccsConnection(List<SpringBootApplicationProperties> bootApplicationProperties) {
        Optional<SpringBootApplicationProperties> optDefaultProperties = bootApplicationProperties.stream()
                .filter(p -> p.getSpringProfile().getProfileName().equals("default"))
                .findFirst();

        if (optDefaultProperties.isEmpty()) {
            throw new RuntimeException("Could not find application.properties file to add connection to Spring Cloud Config Server.");
        }

        optDefaultProperties.get().setProperty("spring.config.import", "optional:configserver:http://localhost:8888");
    }

    public void deleteAllButDefaultProperties(List<SpringBootApplicationProperties> bootApplicationProperties) {
        bootApplicationProperties.forEach(p -> {
            if (!p.isDefaultProperties()) {
                p.delete();
            }
        });
    }

    public void initGit(File dir) {
        GitSupport.initGit(dir);
    }
}
