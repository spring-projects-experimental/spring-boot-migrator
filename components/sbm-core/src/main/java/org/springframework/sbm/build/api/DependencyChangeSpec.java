package org.springframework.sbm.build.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.openrewrite.maven.tree.Scope;

@Getter
@AllArgsConstructor
public class DependencyChangeSpec {

    public enum Operation{
        ADD,
        REMOVE
    }

    @Getter
    private Dependency dependency;
    private Operation operation;

    public DependencyChangeSpec changeScope(Scope scope){
        DependencyChangeSpec newSpec = new DependencyChangeSpec(this.dependency, this.operation);
        newSpec.dependency.setScope(scope.name());
        return newSpec;
    }
}
