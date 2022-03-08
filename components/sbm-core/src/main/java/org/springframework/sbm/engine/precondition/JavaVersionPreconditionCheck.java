package org.springframework.sbm.engine.precondition;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.List;

@Component
class JavaVersionPreconditionCheck extends PreconditionCheck {
    @Override
    public PreconditionCheckResult verify(Path projectRoot, List<Resource> projectResources) {
        String javaVersion = System.getProperty("java.specification.version");
        if(! "11".equals(javaVersion)) {
            return new PreconditionCheckResult(ResultState.WARN, String.format("Java 11 is required. Check found Java %s.", javaVersion));
        }
        return new PreconditionCheckResult(ResultState.PASSED, String.format("Required Java version (%s) was found.", javaVersion));
    }
}
