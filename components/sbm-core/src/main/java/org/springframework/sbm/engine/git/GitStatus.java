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