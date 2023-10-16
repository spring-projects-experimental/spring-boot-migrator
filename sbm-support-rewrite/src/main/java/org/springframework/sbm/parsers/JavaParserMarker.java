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
package org.springframework.sbm.parsers;

import lombok.Value;
import lombok.With;
import org.openrewrite.java.JavaParser;
import org.openrewrite.marker.Marker;

import java.util.UUID;


/**
 * Used to keep the stateful {@link JavaParser} for later parsing.
 *
 * This is required when (re-)parsing a submodule somewhere (non-leaf) in a reactor build tree.
 * Then this module requires the JavaParser with types from the last parse where types from lower modules are cached.
 *
 * @author Fabian Krüger
 */
@Value
@With
public class JavaParserMarker implements Marker {
    private final UUID id;
    private final JavaParser javaParser;
}
