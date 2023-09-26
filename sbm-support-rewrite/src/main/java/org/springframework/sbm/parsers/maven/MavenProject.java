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

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.maven.model.Model;
import org.springframework.core.io.Resource;
import org.springframework.sbm.utils.ResourceUtil;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Getter
@Setter
/**
 * @author Fabian Krüger
 */
public class MavenProject {

    private final Path projectRoot;
    private final Resource pomFile;
    // FIXME: 945 temporary method, model should nopt come from Maven
    private Model pomModel;

    public MavenProject(Path projectRoot, Resource pomFile) {
        this.projectRoot = projectRoot;
        this.pomFile = pomFile;
    }

    @Deprecated(forRemoval = true)
    public File getFile() {
        return null;
    }

    public void setPluginArtifacts(Set<Object> of) {
    }

    public Path getBasedir() {
        return pomFile == null ? null : ResourceUtil.getPath(pomFile).getParent();
    }

    public List<MavenProject> getCollectedProjects() {
        return null;
    }

    public Resource getResource() {
        return pomFile;
    }

    public Path getModuleDir() {
        if(getBasedir() == null) {
            return null;
        } else if(projectRoot.relativize(ResourceUtil.getPath(pomFile)).toString().equals("pom.xml")){
            return Path.of("");
        } else {
            return projectRoot.relativize(ResourceUtil.getPath(pomFile)).getParent();
        }
    }


    public String getGroupIdAndArtifactId() {
        return this.pomModel.getGroupId() + ":" + pomModel.getArtifactId();
    }

}
