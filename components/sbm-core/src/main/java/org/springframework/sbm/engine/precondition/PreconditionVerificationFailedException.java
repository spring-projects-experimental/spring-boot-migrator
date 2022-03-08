package org.springframework.sbm.engine.precondition;

import lombok.Getter;

public class PreconditionVerificationFailedException extends RuntimeException {

    @Getter
    private PreconditionVerificationResult preconditionVerificationResult;

    public PreconditionVerificationFailedException(PreconditionVerificationResult preconditionVerificationResult) {
        this.preconditionVerificationResult = preconditionVerificationResult;
    }

}
