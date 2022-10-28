package org.springframework.sbm.build.api;

import lombok.NonNull;
import org.openrewrite.maven.tree.Scope;

import java.util.*;
import java.util.stream.Collectors;
import static org.openrewrite.maven.tree.Scope.*;

/**
 * Resolve the dependency change spec. Their is a ascending
 * order of the scopes where a higher order covers its predecessor
 * and more. Below is the ascending order of the scopes.
 *
 * Test (lowest), Runtime, Provided, Compile (highest)
 *
 * Based on the above scope, the following rule decides the fate
 * of a proposed dependency change spec.
 *
 * Rule 1 :- If the proposed dependency already exists
 * transitively but its scope is lesser than the proposed
 * scope, the proposed dependency will be added to  the
 * build file.
 *
 * Rule 2 :- If the proposed dependency already declared
 * directly but its scope is lesser than the the proposed
 * scope, the existing dependency will be replaced.
 *
 * Rule 3 :- If there is no matching dependency already exists
 * the proposed dependency will beadded.
 */
public class DependencyChangeResolver {

    public static final EnumSet<Scope> ALL_SCOPES = EnumSet.range(None, System);
    @NonNull
    private BuildFile buildFile;
    private @NonNull Dependency proposedChangeSpec;
    private List<Dependency> potentialMatches;

    private static Map<Scope, List<Scope>> UPGRADE_GRAPH = new HashMap<>();
    static {
        // For a given scope (key), SBM will upgrade ( upsert) if any of the listed scope
        // exists in the directly included dependencies
        UPGRADE_GRAPH.put(Compile, List.of(Test,Provided,Runtime));
        UPGRADE_GRAPH.put(Provided,List.of(Test,Runtime));
        UPGRADE_GRAPH.put(Runtime,List.of(Test));
        UPGRADE_GRAPH.put(Test,Collections.emptyList());
    }

    public DependencyChangeResolver(BuildFile buildFile, @NonNull Dependency proposedChangeSpec){
        this.buildFile = buildFile;
        this.proposedChangeSpec = proposedChangeSpec;
        this.potentialMatches = buildFile.getEffectiveDependencies()
                .stream()
                .filter(d -> d.equals(this.proposedChangeSpec))
                .collect(Collectors.toList());
    }

    public void apply(){
        if(isUpsertRequired())
            upsertDependencies(proposedChangeSpec);
    }

    private boolean isUpsertRequired() {
        if(potentialMatches.isEmpty()) // Rule 3
            return true;

        Scope proposedDependencyScope = Scope.fromName(proposedChangeSpec.getScope());

        return potentialMatches
                .stream()
                .map(Dependency::getScope)
                .map(Scope::fromName)
                .anyMatch(UPGRADE_GRAPH.get(proposedDependencyScope)::contains); // Rule 1
    }

    private void upsertDependencies(@NonNull Dependency effectiveSpec){
        List<Dependency> declaredDependencies = buildFile.getDeclaredDependencies(ALL_SCOPES.toArray(new Scope[0]));

        List<Dependency> existingDependenciesList = declaredDependencies
                .stream()
                .filter(proposedChangeSpec::equals)
                .collect(Collectors.toList());

        if(!existingDependenciesList.isEmpty()) // Rule 2
            buildFile.removeDependencies(existingDependenciesList);

        buildFile.addDependency(proposedChangeSpec);
    }

}
