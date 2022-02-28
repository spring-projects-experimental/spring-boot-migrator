package org.springframework.sbm.mule.api;

import java.util.Set;

public interface DefinitionSnippet {
    Set<String> getRequiredImports();
    Set<String> getRequiredDependencies();
    String renderDslSnippet();
}
