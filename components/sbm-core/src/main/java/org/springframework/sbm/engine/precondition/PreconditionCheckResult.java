package org.springframework.sbm.engine.precondition;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PreconditionCheckResult {
    private final PreconditionCheck.ResultState state;
    private final String message;
}
