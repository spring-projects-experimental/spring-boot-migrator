/*
 * Copyright 2021 - 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.sbm.build.api;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * Shared logic for selecting the reactor root build file.
 *
 * <p>This heuristic is used by both {@link RootBuildFileFilter}
 * and {@link ApplicationModules} to ensure consistent root detection.
 *
 * <p><b>Ranking priority (ascending = stronger candidate):</b>
 * <ol>
 *   <li>Explicitly flagged as root ({@code isRootBuildFile() == true})</li>
 *   <li>Aggregator POM ({@code packaging=pom} AND non-empty {@code <modules>})</li>
 *   <li>Plain POM ({@code packaging=pom})</li>
 *   <li>Everything else (jar, war, etc.)</li>
 * </ol>
 *
 * <p>Within the same category:
 * <ul>
 *   <li>Shallower paths win (closer to project root)</li>
 *   <li>Lexical path comparison ensures deterministic selection</li>
 * </ul>
 *
 * <p><b>Caveat:</b>
 * If parsing fails to resolve {@code <modules>} (e.g. partial scan failure),
 * the aggregator heuristic cannot trigger. In that case the selection
 * degrades gracefully to:
 *
 * <pre>
 * pom packaging → shallowest path → lexical tie-break
 * </pre>
 */
final class RootBuildFileSelector {

    private RootBuildFileSelector() {}

    /*
     * ===============================
     *  Public selection entry points
     * ===============================
     */

    static BuildFile selectRootBuildFile(List<BuildFile> buildFiles) {
        return buildFiles.stream()
                .min(ROOT_BUILD_FILE_COMPARATOR)
                .orElseThrow(() ->
                        new RootBuildFileNotFoundException("Could not find BuildFile for root module."));
    }

    static Comparator<BuildFile> rootComparator() {
        return ROOT_BUILD_FILE_COMPARATOR;
    }

    static Comparator<Module> rootModuleComparator() {
        return ROOT_MODULE_COMPARATOR;
    }

    /*
     * ===============================
     *  Comparator definitions
     * ===============================
     */

    private static final Comparator<BuildFile> ROOT_BUILD_FILE_COMPARATOR =
            Comparator.comparingInt(RootBuildFileSelector::score)
                    .thenComparingInt(bf -> pathDepth(bf.getSourcePath()))
                    .thenComparing(bf -> bf.getSourcePath() == null ? "" : bf.getSourcePath().toString());

    /**
     * Derives module ordering directly from the BuildFile comparator,
     * preventing drift if ranking rules evolve.
     */
    private static final Comparator<Module> ROOT_MODULE_COMPARATOR =
            Comparator.comparing(Module::getBuildFile, ROOT_BUILD_FILE_COMPARATOR);

    /*
     * ===============================
     *  Ranking heuristic
     * ===============================
     */

    /**
     * Lower score = stronger root candidate.
     */
    static int score(BuildFile bf) {
        if (bf.isRootBuildFile()) {
            return 0;
        }

        boolean isPom = "pom".equals(safeLower(bf.getPackaging()));
        if (!isPom) {
            return 3;
        }

        boolean isAggregator = false;
        try {
            List<String> modules = bf.getDeclaredModules();
            isAggregator = modules != null && !modules.isEmpty();
        } catch (UnsupportedOperationException ignored) {
            // Non-Maven BuildFile implementations may not support this.
            // Treat as non-aggregator POM.
        }

        return isAggregator ? 1 : 2;
    }

    /*
     * ===============================
     *  Helpers
     * ===============================
     */

    static int pathDepth(Path path) {
        return path == null ? Integer.MAX_VALUE : path.getNameCount();
    }

    private static String safeLower(String s) {
        return s == null ? "" : s.toLowerCase(Locale.ROOT);
    }
}
