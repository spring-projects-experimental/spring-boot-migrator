/*
 * Copyright 2021 - 2022 the original author or authors.
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
package org.springframework.sbm.build.impl;

import org.openrewrite.maven.tree.MavenResolutionResult;
import org.openrewrite.xml.tree.Xml;

import java.util.Optional;

/**
 * @author Fabian Krüger
 */
public class MavenBuildFileUtil {
    public static Optional<MavenResolutionResult> findMavenResolution(Xml.Document pom) {
        return pom.getMarkers().findFirst(MavenResolutionResult.class);
    }

    public static MavenResolutionResult getMavenResolution(Xml.Document pom) {
        return pom.getMarkers().findFirst(MavenResolutionResult.class).orElseThrow(() -> new IllegalStateException("Maven AST without a MavenResolutionResult marker"));
    }
}
