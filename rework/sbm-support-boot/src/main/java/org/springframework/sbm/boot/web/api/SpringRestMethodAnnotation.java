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
package org.springframework.sbm.boot.web.api;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

enum SpringRestMethodAnnotation {
    DELETE_MAPPING("org.springframework.web.bind.annotation.DeleteMapping"),
    GET_MAPPING("org.springframework.web.bind.annotation.GetMapping"),
    POST_MAPPING("org.springframework.web.bind.annotation.PostMapping"),
    PUT_MAPPING("org.springframework.web.bind.annotation.PutMapping"),
    PATCH_MAPPING("org.springframework.web.bind.annotation.PatchMapping"),
    REQUEST_MAPPING("org.springframework.web.bind.annotation.RequestMapping");
    @Getter
    private final String fullyQualifiedName;

    SpringRestMethodAnnotation(String fqn) {
        this.fullyQualifiedName = fqn;
    }

    public static List<SpringRestMethodAnnotation> getAll() {
        return Arrays.asList(values());
    }
}
