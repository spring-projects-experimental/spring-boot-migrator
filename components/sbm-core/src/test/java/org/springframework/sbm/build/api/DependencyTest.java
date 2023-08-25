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

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


class DependencyTest {

    @Test
    public void handlesErroneousCoordinateInput() {
        assertThrows(IllegalArgumentException.class, () -> Dependency.fromCoordinates("one"));
    }

    @Test
    public void handlesErroneousCoordinateFiveParts() {
        assertThrows(IllegalArgumentException.class, () -> Dependency.fromCoordinates("one:two:three:four:five"));;
    }

    @Test
    public void handlesErroneousCoordinateFourParts() {
        Dependency dependency = Dependency.fromCoordinates("one:two:three:four");

        assertThat(dependency.getGroupId()).isEqualTo("one");
        assertThat(dependency.getArtifactId()).isEqualTo("two");
        assertThat(dependency.getVersion()).isEqualTo("three");
        assertThat(dependency.getClassifier()).isEqualTo("four");
    }

    @Test
    public void handlesDependencyWithTwoParts() {
        Dependency dependency = Dependency.fromCoordinates("one:two");

        assertThat(dependency.getGroupId()).isEqualTo("one");
        assertThat(dependency.getArtifactId()).isEqualTo("two");
    }

    @Test
    public void handlesDependencyWithThreeParts() {
        Dependency dependency = Dependency.fromCoordinates("one:two:three");

        assertThat(dependency.getGroupId()).isEqualTo("one");
        assertThat(dependency.getArtifactId()).isEqualTo("two");
        assertThat(dependency.getVersion()).isEqualTo("three");
    }
}
