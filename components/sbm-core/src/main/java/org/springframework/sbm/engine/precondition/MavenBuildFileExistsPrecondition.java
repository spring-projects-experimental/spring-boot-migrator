package org.springframework.sbm.engine.precondition;

import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.List;

@Order(1)
@Component
class MavenBuildFileExistsPrecondition extends PreconditionCheck {

    @Override
    public PreconditionCheckResult verify(Path projectRoot, List<Resource> projectResources) {
        if(projectResources.stream().noneMatch(r -> "pom.xml".equals(getPath(r).getFileName().toString()))) {
            return new PreconditionCheckResult(ResultState.FAILED, "SBM requires a Maven build file. Please provide a minimal pom.xml.");
        } else {
            return new PreconditionCheckResult(ResultState.PASSED, "Found pom.xml.");
        }
    }

}
