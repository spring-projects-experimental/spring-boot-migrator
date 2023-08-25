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
package org.springframework.sbm.gradle.tooling;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.io.File;
import java.io.Serializable;
import java.util.Collection;

@AllArgsConstructor
@Value
public class KotlinSourceSetDataImpl implements KotlinSourceSetData, Serializable {

    private final String name;
    private final Collection<File> kotlin;
    private final Collection<File> compileClasspath;
    private final Collection<File> implementationClasspath;

}
