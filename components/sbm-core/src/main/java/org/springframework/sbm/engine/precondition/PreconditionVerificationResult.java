package org.springframework.sbm.engine.precondition;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class PreconditionVerificationResult {
    @Getter
    private List<PreconditionCheckResult> results = new ArrayList<>();
    private final Path projectRoot;

    public void addResult(PreconditionCheckResult preconditionCheckResult) {
        this.results.add(preconditionCheckResult);
    }

    public boolean hasError() {
        return results.stream().anyMatch(r -> r.getState().equals(PreconditionCheck.ResultState.FAILED));
    }

    public Path getProjectRoot() {
        return projectRoot;
    }
}
