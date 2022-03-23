package org.springframework.sbm.engine.precondition;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.sbm.engine.git.GitStatus;
import org.springframework.sbm.engine.git.GitSupport;
import org.springframework.sbm.project.resource.ApplicationProperties;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.springframework.sbm.engine.precondition.PreconditionCheck.ResultState.FAILED;
import static org.springframework.sbm.engine.precondition.PreconditionCheck.ResultState.PASSED;

/**
 * Checks Git preconditions when {@code sbm.gitSupportEnabled=true}.
 *
 * Preconditions are
 *
 *  - A .git dir must exist under project root.
 *  - No uncommitted changes must exist.
 *  - No untracked resources must exist.
 */
@Order(99)
@Component
@RequiredArgsConstructor
class DoesGitDirExistWhenGitSupportEnabledPreconditionCheck extends PreconditionCheck {

    private final ApplicationProperties applicationProperties;

    private static final String NO_GIT_DIR_EXISTS = "'sbm.gitSupportEnabled' is 'true' but no '.git' dir exists in project dir. Either disable git support or initialize git.";
    private static final String HAS_UNCOMMITTED_CHANGES = "'sbm.gitSupportEnabled' is 'true' but Git status is not clean. Commit all changes and add or ignore all resources before scan.";
    private final String CHECK_PASSED = "'sbm.gitSupportEnabled' is 'true', changes will be committed to branch [%s] after each recipe.";
    private final String CHECK_IGNORED = "'sbm.gitSupportEnabled' is 'false', Nothing will be committed.";
	private final GitSupport gitSupport;

	@Override
	public PreconditionCheckResult verify(Path projectRoot, List<Resource> projectResources) {
		if (applicationProperties.isGitSupportEnabled()) {
			if (noGitDirExists(projectRoot)) {
                return new PreconditionCheckResult(FAILED, NO_GIT_DIR_EXISTS);
			}
			else if (! isGitStatusClean(projectRoot)) {
                return new PreconditionCheckResult(FAILED, HAS_UNCOMMITTED_CHANGES);
			} else {
				return new PreconditionCheckResult(PASSED, String.format(CHECK_PASSED, getBranch(projectRoot)));
			}
		}
		return new PreconditionCheckResult(PASSED, CHECK_IGNORED);
	}

    private String getBranch(Path projectRoot) {
        File repo = projectRoot.normalize().toAbsolutePath().toFile();
        Optional<String> branchName = gitSupport.getBranchName(repo);
        if (branchName.isPresent()) {
            return branchName.get();
        } else {
            return "not found!";
        }
    }

    private boolean isGitStatusClean(Path projectRoot) {
        File repo = projectRoot.normalize().toAbsolutePath().toFile();
        GitStatus status = gitSupport.getStatus(repo);
        return status.isClean();
	}

	private boolean noGitDirExists(Path projectRoot) {
		Path path = projectRoot.resolve(".git").normalize().toAbsolutePath();
		if (!path.toFile().exists() || !path.toFile().isDirectory()) {
			return true;
		}
		else {
			return false;
		}
	}

}
