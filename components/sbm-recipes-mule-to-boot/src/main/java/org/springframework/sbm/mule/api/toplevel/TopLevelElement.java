package org.springframework.sbm.mule.api.toplevel;

import java.util.Set;

public interface TopLevelElement {
    Set<String> getRequiredImports();
    Set<String> getRequiredDependencies();
    String renderDslSnippet();
}
