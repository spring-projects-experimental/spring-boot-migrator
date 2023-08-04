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

package org.springframework.sbm.boot.web.api;

import lombok.Builder;
import org.springframework.sbm.java.api.Method;
import org.springframework.sbm.java.api.MethodParam;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Optional;

/**
 * Record of request mapping annotation data (e.g. {@code RequestMapping} or {@code GetMapping}).
 *
 * @param path the list of mapped paths
 * @param name
 * @param method
 * @param params
 * @param headers the list of headers
 * @param consumes the effective media types
 * @param produces the effective media types
 * @param methodReference the method
 * @param returnType the fully qualified of the return type
 * @param requestBodyParameterType the fully qualified name of parameter annotated with {@RequestBody}
 *
 * @author Fabian Krüger
 */
@Builder
public record RestMethod(
        List<String> path,
        String name,
        List<RequestMethod> method,
        List<String> params,
        List<String> headers,
        List<String> consumes,
        List<String> produces,
        Method methodReference,
        String returnType,
        Optional<MethodParam> requestBodyParameterType) {

    public Optional<MethodParam> requestBodyParameterType() {
        if(requestBodyParameterType == null) return Optional.empty();
        return requestBodyParameterType;
    }
}
