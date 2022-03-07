package org.springframework.sbm.engine.precondition;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
class PreconditionCheckResult {
    private final PreconditionCheck.ResultState state;
    private final String message;
}
