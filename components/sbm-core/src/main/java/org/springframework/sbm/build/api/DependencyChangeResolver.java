package org.springframework.sbm.build.api;

import lombok.NonNull;
import org.apache.commons.lang3.tuple.Pair;
import org.openrewrite.maven.tree.Scope;

import java.util.*;
import java.util.stream.Collectors;

import static org.openrewrite.maven.tree.Scope.*;

/**
 * Resolve the dependency change spec. Their is a ascending
 * order of the scopes where a higher order covers its predecessor
 * and more. Below is the ascending order of the scopes.
 * <p>
 * Test (lowest), Runtime, Provided, Compile (highest)
 * <p>
 * Based on the above scope, the following rule decides the fate
 * of a proposed dependency change spec.
 * <p>
 * Rule 1 :- If the proposed dependency already exists
 * transitively but its scope is lesser than the proposed
 * scope, the proposed dependency will be added to  the
 * build file.
 * <p>
 * Rule 2 :- If the proposed dependency already declared
 * directly but its scope is lesser than the the proposed
 * scope, the existing dependency will be replaced.
 * <p>
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
        UPGRADE_GRAPH.put(Compile, List.of(Test, Provided, Runtime));
        UPGRADE_GRAPH.put(Provided, List.of(Test, Runtime));
        UPGRADE_GRAPH.put(Runtime, List.of(Test));
        UPGRADE_GRAPH.put(Test, Collections.emptyList());
    }

    public DependencyChangeResolver(BuildFile buildFile, @NonNull Dependency proposedChangeSpec) {
        this.buildFile = buildFile;
        this.proposedChangeSpec = proposedChangeSpec;
        this.potentialMatches = buildFile.getEffectiveDependencies()
                .stream()
                .filter(d -> d.equals(this.proposedChangeSpec))
                .collect(Collectors.toList());
    }

    /**
     * Return a pair of dependencies to be removed ( left) and added ( right)
     * @return
     */
    public Pair<List<Dependency>, Optional<Dependency>> apply() {
        if (potentialMatches.isEmpty())
            return Pair.of(Collections.emptyList(), Optional.of(proposedChangeSpec));

        Scope proposedDependencyScope = Scope.fromName(proposedChangeSpec.getScope());
        List<Scope> supersededScopes = UPGRADE_GRAPH.get(proposedDependencyScope);

        Optional<Dependency> right = potentialMatches
                .stream()
                .filter(d -> supersededScopes.contains(fromName(d.getScope())))
                .findAny()
                .map(any -> proposedChangeSpec);

        List<Dependency> left = buildFile
                .getDeclaredDependencies(ALL_SCOPES.toArray(new Scope[0]))
                .stream()
                .filter(proposedChangeSpec::equals)
                .collect(Collectors.toList());

        return Pair.of(left, right);

    }

}
