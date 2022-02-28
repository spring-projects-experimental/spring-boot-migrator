package org.springframework.sbm.mule.api.toplevel;

import java.util.Set;

public interface TopLevelDefinition {
    Set<String> getRequiredImports();
    Set<String> getRequiredDependencies();
    String renderDslSnippet();
}
