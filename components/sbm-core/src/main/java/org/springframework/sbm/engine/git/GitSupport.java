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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.ApplicationProperties;
import org.springframework.sbm.project.resource.RepositoryNotFoundException;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Provides basic Git support.
 *
 * @author fkrueger
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GitSupport {

    private final ApplicationProperties applicationProperties;

    /**
     * Adds files to git index.
     *
     * @param repo         the location of the repo
     * @param filePatterns the filePatterns to add
     */
    public void add(File repo, String... filePatterns) {
        try {
            Git git = initGit(repo);
            AddCommand add = git.add();
            for (String filePattern : filePatterns) {
                add.addFilepattern(filePattern);
            }
            DirCache call = add.call();
        } catch (GitAPIException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds removed files to index.
     *
     * @param repo    the location of the repo
     * @param deleted the filePatterns to remove
     */
    public void delete(File repo, String... deleted) {
        try {
            Git git = initGit(repo);
            RmCommand rm = git.rm();
            for (String filePattern : deleted) {
                rm.addFilepattern(filePattern);
            }
            rm.call();
        } catch (GitAPIException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Commit to Git.
     *
     * @param repo    the location of the repo
     * @param message for the commit
     */
    public Commit commit(File repo, String message) {
        try {
            Git git = initGit(repo);
            CommitCommand commit = git.commit();
            commit.setMessage(message);
            RevCommit call = commit.call();
            return new Commit(call.getName(), call.getFullMessage());
        } catch (GitAPIException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get the latest commit
     *
     * @param repo the location of the repo
     */
    public Optional<Commit> getLatestCommit(File repo) {
        try {
            Git git = initGit(repo);
            Iterable<RevCommit> revCommits = git.log()
                    .setMaxCount(1)
                    .call();
            RevCommit lastCommit = revCommits.iterator().next();
            return Optional.of(new Commit(lastCommit.getName(), lastCommit.getFullMessage()));
        } catch (GitAPIException e) {
            log.warn("Git repository has no commits yet.");
            return Optional.empty();
        }
    }


    /**
     * Find and return a git repository at given location.
     *
     * @param repo the location of the repo to search for. {@code .git} is added to file location if not contained
     */
    public Repository getRepository(File repo) {
        try {
            FileRepositoryBuilder builder = new FileRepositoryBuilder();
            if (!repo.toString().endsWith(".git")) {
                repo = repo.toPath().resolve(".git").toFile();
            }
            return builder
                    .setGitDir(repo)
                    .setMustExist(true)
                    .build();
        } catch (IOException e) {
            throw new RepositoryNotFoundException(e);
        }
    }

    /**
     * Init a new git repo or return existing
     *
     * @param repo the location of the repo to initialize
     */
    public static Git initGit(File repo) {
        try {
            return Git.init().setDirectory(repo).call();
        } catch (GitAPIException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Does a {@code git reset --soft} to given {@code ref}.
     *
     * @param repo the location of the repo
     * @param ref  the ref to reset to
     */
    public void softReset(File repo, String ref) {
        try {
            Git git = initGit(repo);
            git.reset()
                    .setMode(ResetCommand.ResetType.SOFT)
                    .setRef(ref)
                    .call();
        } catch (GitAPIException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Checks if there are uncommitted changes or if the revision differs.
     *
     * @param repo             the location of the repo
     * @param expectedRevision the revision to check
     */
    // TODO: test this method
    public boolean hasUncommittedChangesOrDifferentRevision(File repo, String expectedRevision) {
        try {
            Git git = initGit(repo);
            Status status = git.status().call();
            Optional<Commit> latestCommit = getLatestCommit(repo);
            if (latestCommit.isEmpty()) {
                return true;
            }
            boolean isDifferentRev = !expectedRevision.equals(latestCommit.get().getHash());
            return status.hasUncommittedChanges() || isDifferentRev;
        } catch (GitAPIException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Add and commit modified and deleted files.
     *
     * @param repo             the location of the repo
     * @param commitMessage
     * @param modifedResources to add as modified
     * @param deletedResources to add as deleted
     */
    // TODO: test this method
    public Commit addAllAndCommit(File repo, String commitMessage, List<String> modifedResources, List<String> deletedResources) {
        if (!modifedResources.isEmpty()) {
            String[] modified = modifedResources.toArray(new String[]{});
            add(repo, modified);
        }

        if (!deletedResources.isEmpty()) {
            String[] deleted = deletedResources.toArray(new String[]{});
            delete(repo, deleted);
        }

        return commit(repo, commitMessage);
    }

    public boolean repoExists(File repoDir) {
        if (repoDir == null) return false;
        try {
            getRepository(repoDir);
            return true;
        } catch (RepositoryNotFoundException e) {
            return false;
        }
    }

    // TODO: test this method
    public void commitWhenGitAvailable(ProjectContext context, String appliedRecipeName, List<String> modifiedResources, List<String> deletedResources) {
        if (applicationProperties.isGitSupportEnabled()) {
            File repoDir = context.getProjectRootDirectory().toFile();
            modifiedResources = makeRelativeToRoot(modifiedResources, repoDir);
            deletedResources = makeRelativeToRoot(deletedResources, repoDir);
            if (repoExists(repoDir)) {
                String commitMessage = "SBM: applied recipe '" + appliedRecipeName + "'";
                Commit latestCommit = addAllAndCommit(repoDir, commitMessage, modifiedResources, deletedResources);
                context.setRevision(latestCommit.getHash());
            }
        }
    }

    public static Optional<String> getBranchName(File repo) {
        Git git = initGit(repo);
        try {
            return Optional.of(git.getRepository().getBranch());
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    private List<String> makeRelativeToRoot(List<String> paths, File projectRootDir) {
        return paths.stream()
                .map(p -> projectRootDir.toPath().relativize(Path.of(p).toAbsolutePath().normalize()))
                .map(Path::toString)
                .collect(Collectors.toList());
    }

    public GitStatus getStatus(File repo) {
        try {
            Git git = initGit(repo);
            Status status = null;
            status = git.status().call();
            GitStatus gitStatus = new GitStatus(status);
            return gitStatus;
        } catch (GitAPIException e) {
            throw new RuntimeException("Could not get git status.", e);
        }
    }

}
