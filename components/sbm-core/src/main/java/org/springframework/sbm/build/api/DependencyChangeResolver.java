package org.springframework.sbm.build.api;

import lombok.NonNull;
import org.openrewrite.maven.tree.Scope;

import java.util.*;
import java.util.stream.Collectors;
import static org.openrewrite.maven.tree.Scope.*;

public class DependencyChangeResolver {

    public static final EnumSet<Scope> ALL_SCOPES = EnumSet.range(None, System);
    @NonNull
    private BuildFile buildFile;
    @NonNull
    private DependencyChangeSpec proposedChangeSpec;
    private List<Dependency> potentialMatches;

    private static Map<Scope, List<Scope>> dependencyGraph = new HashMap<>();
    static {
        dependencyGraph.put(Provided, List.of(Provided));
        dependencyGraph.put(Compile, List.of(Provided,Compile));
        dependencyGraph.put(System, List.of(Provided,Compile,System));
        dependencyGraph.put(Test, List.of(Provided,Compile,System,Test));
    }

    public DependencyChangeResolver(BuildFile buildFile, DependencyChangeSpec proposedChangeSpec){
        this.buildFile = buildFile;
        this.proposedChangeSpec = proposedChangeSpec;
        this.potentialMatches = buildFile.getEffectiveDependencies()
                .stream()
                .filter(d -> d.equals(this.proposedChangeSpec.getDependency()))
                .collect(Collectors.toList());
    }

    public void apply(){
        this.upsertDependencies(upgradeDependencySpec().orElse(proposedChangeSpec));
    }

    private Optional<DependencyChangeSpec> upgradeDependencySpec() {
        if(potentialMatches.isEmpty())
            return Optional.empty();

        Scope proposedDependencyScope = Scope.fromName(proposedChangeSpec.getDependency().getScope());
        if(proposedDependencyScope == None)
            proposedDependencyScope = Compile;

        //What if the transitive declares the scope as test but the project dont have direct dependency.
        // Do we really need to care about the transitives?
        Optional<DependencyChangeSpec> upgradedDependencySpec
                = potentialMatches
                .stream()
                .map(Dependency::getScope)
                .map(Scope::fromName)
                .filter(dependencyGraph.get(proposedDependencyScope)::contains)
                .map(proposedChangeSpec::changeScope)
                .findFirst();

        upgradedDependencySpec
                .orElseThrow(() -> new IllegalArgumentException(getInvalidScopeMessage()));

        return upgradedDependencySpec;
    }

    private String getInvalidScopeMessage() {
        Scope proposedDependencyScope = Scope.fromName(proposedChangeSpec.getDependency().getScope());
        List<Scope> allowedScopes = dependencyGraph.get(proposedDependencyScope);
        List<String> disallowedScopes
                = ALL_SCOPES
                .stream()
                .filter(s -> !allowedScopes.contains(s))
                .map(Scope::name)
                .collect(Collectors.toList());

        return "Dependency "
                + this.proposedChangeSpec.getDependency().getCoordinates()
                + " already present in scope "
                + disallowedScopes;
    }

    private void upsertDependencies(DependencyChangeSpec effectiveSpec){
        List<Dependency> declaredDependencies = buildFile.getDeclaredDependencies(ALL_SCOPES.toArray(new Scope[0]));

        List<Dependency> existingDependenciesList = declaredDependencies
                .stream()
                .filter(proposedChangeSpec::equals)
                .collect(Collectors.toList());

        buildFile.removeDependencies(existingDependenciesList);
        buildFile.addDependency(effectiveSpec.getDependency());
    }

}
