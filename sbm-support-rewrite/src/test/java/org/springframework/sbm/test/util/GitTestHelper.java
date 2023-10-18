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
package org.springframework.sbm.test.util;

import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.openrewrite.shaded.jgit.api.Git;
import org.openrewrite.shaded.jgit.api.errors.GitAPIException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * @author Fabian Krüger
 */
public class GitTestHelper {

    public static Path cloneProjectCommit(String url, String target, String commitHash) {
        return checkout(url, target, commitHash);
    }

    public static Path cloneProjectTag(String url, String target, String tag) {
        String ref = "refs/tags/" + tag;
        return checkout(url, target, ref);
    }

    @NotNull
    private static Path checkout(String url, String target, String ref) {
        try {
            File directory = Path.of(target).toFile();
            if (!directory.exists()) {
                FileUtils.forceMkdir(directory.toPath().toAbsolutePath().normalize().toFile());
            }
            Git git = Git.cloneRepository()
                    .setDirectory(directory)
                    .setURI(url)
                    .call();

            git.checkout()
                     .setName(ref)
                    .call();

            return git.getRepository().getDirectory().toPath().getParent();
        } catch (GitAPIException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
