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

import org.openrewrite.shaded.jgit.api.Git;
import org.openrewrite.shaded.jgit.api.errors.GitAPIException;
import org.springframework.core.io.Resource;
import org.springframework.sbm.utils.ResourceUtil;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Fabian Krüger
 */
public class TestProjectHelper {
    private final Path targetDir;
    private List<Resource> resources = new ArrayList<>();
    private boolean initializeGitRepo;
    private String gitUrl;
    private String gitTag;

    public TestProjectHelper(Path targetDir) {
        this.targetDir = targetDir;
    }

    public static TestProjectHelper createTestProject(Path targetDir) {
        return new TestProjectHelper(targetDir);
    }

    public static TestProjectHelper createTestProject(String targetDir) {
        return new TestProjectHelper(Path.of(targetDir).toAbsolutePath().normalize());
    }

    public TestProjectHelper withResources(Resource... resources) {
        this.resources.addAll(Arrays.asList(resources));
        return this;
    }

    public TestProjectHelper initializeGitRepo() {
        this.initializeGitRepo = true;
        return this;
    }

    public TestProjectHelper cloneGitProject(String url) {
        this.gitUrl = url;
        return this;
    }

    public TestProjectHelper checkoutTag(String tag) {
        this.gitTag = tag;
        return this;
    }

    public void writeToFilesystem() {
        if (initializeGitRepo) {
            try {
                Git.init().setDirectory(targetDir.toFile()).call();
            } catch (GitAPIException e) {
                throw new RuntimeException(e);
            }
        } else if (gitUrl != null) {
            try {
                File directory = targetDir.toFile();
                Git git = Git.cloneRepository()
                        .setDirectory(directory)
                        .setURI(this.gitUrl)
                        .call();

                if (gitTag != null) {
                    git.checkout()
                            .setName("refs/tags/" + gitTag)
                            .call();
                }
            } catch (GitAPIException e) {
                throw new RuntimeException(e);
            }
        }
        ResourceUtil.write(targetDir, resources);
    }
}
