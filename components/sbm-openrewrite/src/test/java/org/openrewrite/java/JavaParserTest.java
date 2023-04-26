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
package org.openrewrite.java;

import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.ExpectedToFail;
import org.openrewrite.marker.SearchResult;
import org.springframework.sbm.GitHubIssue;

import java.util.UUID;

/**
 * @author Fabian Krüger
 */
public class JavaParserTest {

    @Test
    @ExpectedToFail
    @GitHubIssue("https://github.com/openrewrite/rewrite/issues/3168")
    void parsedCompilationUnitsShouldHaveModifiableListOfMarkers() {
        JavaParser.fromJavaVersion().build().parse("class Pizza {}").get(0).getMarkers().getMarkers().add(new SearchResult(UUID.randomUUID(), "test"));
    }

}
