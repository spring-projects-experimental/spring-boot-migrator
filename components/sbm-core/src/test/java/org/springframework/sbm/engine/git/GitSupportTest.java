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
package org.springframework.sbm.engine.git;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Files;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileUrlResource;
import org.springframework.sbm.engine.commands.ScanCommand;
import org.springframework.sbm.project.resource.SbmApplicationProperties;
import org.springframework.sbm.project.resource.TestProjectContext;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Fabian Krüger
 */
public class GitSupportTest {
    @Test
    void findRepository_withoutGit_shouldLogErrorAndReturnEmpty(@TempDir Path tmpDir) {
        final ch.qos.logback.classic.Logger logger = (Logger) LoggerFactory.getLogger(GitSupport.class);
        logger.setLevel(Level.ALL);
        PrintStream realSysOut = System.out;
        ByteArrayOutputStream sysOutBuffer = new ByteArrayOutputStream();
        System.setOut(new PrintStream(sysOutBuffer));

        Optional<Repository> repository = GitSupport.findRepository(tmpDir.toFile());
        assertThat(repository).isEmpty();
        assertThat(sysOutBuffer.toString()).matches("[\\d\\: \\. ]*\\[main\\] ERROR o\\.s\\.sbm\\.engine\\.git\\.GitSupport - Could not find \\.git in the given directory '"+tmpDir.toString().replace("/", "\\/")+"' or any of it's parents(?s).*");
        System.setOut(realSysOut);
    }

    @Test
    void findRepository_witGitInDir_shouldReturnRepository(@TempDir Path tmpDir) {
        GitSupport.initGit(tmpDir.toFile());
        Optional<Repository> repository = GitSupport.findRepository(tmpDir.toFile());
        assertThat(repository).isNotEmpty();
        assertThat(repository.get().getDirectory().toPath()).isEqualTo(tmpDir.resolve(".git"));
    }

    @Test
    void findRepository_witGitInParentDir_shouldReturnRepository(@TempDir Path tmpDir) {
        // init git in parent dir
        GitSupport.initGit(tmpDir.toFile());
        // project dir is below parent dir
        Path projectDir = tmpDir.resolve("project");
        Files.newFolder(projectDir.toString());
        // when passing project dir without .git
        Optional<Repository> repository = GitSupport.findRepository(projectDir.toFile());
        // it returns repository
        assertThat(repository).isNotEmpty();
        // in parent dir
        assertThat(repository.get().getDirectory().toPath()).isEqualTo(tmpDir.resolve(".git"));
    }

    @Test
    void addFiles_withGitInSameDirAndFileRelativeFilename_shouldAddTheFileToIndex(@TempDir Path tmpDir) {
        // init git in dir
        GitSupport.initGit(tmpDir.toFile());
        // create a file
        String filename = "file.txt";
        Files.newFile(tmpDir.resolve(filename).toString());
        // add file
        GitSupport gitSupport = new GitSupport(new SbmApplicationProperties());
        gitSupport.add(tmpDir.toFile(), filename);
        GitStatus status = gitSupport.getStatus(tmpDir.toFile());
        assertThat(status.getUncommittedChanges()).contains(filename);
    }

    @Test
    void addFiles_withGitInParentDirAndFilenameAsPattern_shouldAddTheFileToIndex(@TempDir Path tmpDir) {
        // init git in dir
        Git git = GitSupport.initGit(tmpDir.toFile());
        // project dir is below parent dir
        Path projectDir = tmpDir.resolve("project");
        Files.newFolder(projectDir.toString());
        // create a file in project dir
        String filename = "file.txt";
        Files.newFile(projectDir.resolve(filename).toString());
        // add file
        GitSupport gitSupport = new GitSupport(new SbmApplicationProperties());
        gitSupport.add(projectDir.toFile(), filename);
        GitStatus status = gitSupport.getStatus(tmpDir.toFile());
        assertThat(status.getUncommittedChanges()).contains(tmpDir.relativize(projectDir.resolve(filename)).toString());
    }

    @Test
    void addFiles_withGitInGivenDirAndFilenameAsPattern_shouldAddTheFileToIndex(@TempDir Path tmpDir) {
        // init git in dir
        Git git = GitSupport.initGit(tmpDir.toFile());
        // project dir is below parent dir
        Path projectDir = tmpDir.resolve("project");
        Files.newFolder(projectDir.toString());
        // create a file in project dir
        String filename = "file.txt";
        Files.newFile(projectDir.resolve(filename).toString());
        // add file
        GitSupport gitSupport = new GitSupport(new SbmApplicationProperties());
        gitSupport.add(projectDir.toFile(), filename);
        GitStatus status = gitSupport.getStatus(tmpDir.toFile());
        assertThat(status.getUncommittedChanges()).contains(tmpDir.relativize(projectDir.resolve(filename)).toString());
    }

    @Test
    void addFiles_withGitInGivenDirAndDotAsPattern_shouldAddTheFileToIndex(@TempDir Path tmpDir) {
        // init git in dir
        Git git = GitSupport.initGit(tmpDir.toFile());
        // project dir is below parent dir
        Path projectDir = tmpDir.resolve("project");
        Files.newFolder(projectDir.toString());
        // create a file in project dir
        String filename = "file.txt";
        Files.newFile(projectDir.resolve(filename).toString());
        // add file
        GitSupport gitSupport = new GitSupport(new SbmApplicationProperties());
        gitSupport.add(projectDir.toFile(), ".");
        GitStatus status = gitSupport.getStatus(tmpDir.toFile());
        assertThat(status.getUncommittedChanges()).contains(tmpDir.relativize(projectDir.resolve(filename)).toString());
    }

    @Test
    void addAllAndCommit(@TempDir Path tmpDir) {
        GitSupport.initGit(tmpDir.toFile());
        Path projectDir = tmpDir.resolve("project");
        Files.newFolder(projectDir.toString());
        GitSupport gitSupport = new GitSupport(new SbmApplicationProperties());
        String initialFile = projectDir.resolve("some.file").toString();
        Files.newFile(initialFile);
        assertThat(gitSupport.getStatus(projectDir.toFile()).isClean()).isFalse();
        gitSupport.addAllAndCommit(projectDir.toFile(), "initial commit", List.of("."), List.of());
        assertThat(gitSupport.getStatus(projectDir.toFile()).isClean()).isTrue();
        String newFile = projectDir.resolve("another.file").toString();
        Files.newFile(newFile);
        gitSupport.addAllAndCommit(projectDir.toFile(), "add file", List.of(newFile), List.of());
        assertThat(gitSupport.getLatestCommit(projectDir.toFile()).get().getMessage()).isEqualTo("add file");
    }
}
