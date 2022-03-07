package org.springframework.sbm.engine.precondition;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class PreconditionVerificationResult {
    @Deprecated
    private List<String> messages = new ArrayList<>();
    @Getter
    private List<PreconditionCheckResult> results = new ArrayList<>();

    @Deprecated
    public List<String> getMessages() {
        return messages;
    }

    public void addMessage(String message) {
        this.messages.add(message);
    }

    public void addResult(PreconditionCheckResult preconditionCheckResult) {
        this.results.add(preconditionCheckResult);
    }
}
