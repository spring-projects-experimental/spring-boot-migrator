package org.springframework.sbm.engine.precondition;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PreconditionVerifier {

    private final List<PreconditionCheck> preconditions;

    public PreconditionVerificationResult verifyPreconditions(Path projectRoot, List<Resource> projectResources) {
        PreconditionVerificationResult result = new PreconditionVerificationResult(projectRoot);
        preconditions.stream().forEach(pc -> result.addResult(pc.verify(projectRoot, projectResources)));
        return result;
    }
}
