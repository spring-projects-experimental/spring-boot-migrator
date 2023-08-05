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
package org.springframework.sbm.engine.precondition;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.List;

@Component
class JavaVersionPreconditionCheck extends PreconditionCheck {
    @Override
    public PreconditionCheckResult verify(Path projectRoot, List<Resource> projectResources) {
        String javaVersion = System.getProperty("java.specification.version");
        if(! ("11".equals(javaVersion) || "17".equals(javaVersion))) {
            return new PreconditionCheckResult(ResultState.WARN, String.format("Java 11 or 17 is required. Check found Java %s.", javaVersion));
        }
        return new PreconditionCheckResult(ResultState.PASSED, String.format("Required Java version (%s) was found.", javaVersion));
    }
}
