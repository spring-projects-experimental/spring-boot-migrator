package org.springframework.sbm.engine.git;

import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.lib.IndexDiff;

import java.util.Map;
import java.util.Set;

public class GitStatus {
    private Status wrapped;

    public GitStatus(Status wrapped) {
        this.wrapped = wrapped;
    }

    public boolean isClean() {
        return wrapped.isClean();
    }

    public boolean hasUncommittedChanges() {
        return wrapped.hasUncommittedChanges();
    }

    public Set<String> getAdded() {
        return wrapped.getAdded();
    }

    public Set<String> getChanged() {
        return wrapped.getChanged();
    }

    public Set<String> getRemoved() {
        return wrapped.getRemoved();
    }

    public Set<String> getMissing() {
        return wrapped.getMissing();
    }

    public Set<String> getModified() {
        return wrapped.getModified();
    }

    public Set<String> getUntracked() {
        return wrapped.getUntracked();
    }

    public Set<String> getUntrackedFolders() {
        return wrapped.getUntrackedFolders();
    }

    public Set<String> getConflicting() {
        return wrapped.getConflicting();
    }

    public Map<String, IndexDiff.StageState> getConflictingStageState() {
        return wrapped.getConflictingStageState();
    }

    public Set<String> getIgnoredNotInIndex() {
        return wrapped.getIgnoredNotInIndex();
    }

    public Set<String> getUncommittedChanges() {
        return wrapped.getUncommittedChanges();
    }
}