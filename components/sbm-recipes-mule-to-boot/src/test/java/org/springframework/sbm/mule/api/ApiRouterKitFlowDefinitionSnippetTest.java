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
package org.springframework.sbm.mule.api;

import org.junit.jupiter.api.Test;
import org.springframework.sbm.mule.api.toplevel.ApiRouterKitFlowTopLevelElement;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ApiRouterKitFlowDefinitionSnippetTest {

    @Test
    public void shouldDetectAPIRoutingKitNamingPattern() {

        assertTrue(ApiRouterKitFlowTopLevelElement.isApiRouterKitName("get:/helloworld:helloword-config"));
    }

    @Test
    public void shouldBeAbleToDetectNonAPIRoutingKitNames() {
        assertFalse(ApiRouterKitFlowTopLevelElement.isApiRouterKitName("helloword-config"));
    }

    @Test
    public void shouldParseConfigRefFromName() {
        ApiRouterKitFlowTopLevelElement apiRouterSnippet = new ApiRouterKitFlowTopLevelElement(
                "get:/helloworld:helloword-config",
                List.of(),
                null,
                Map.of());

        assertThat(apiRouterSnippet.getConfigRef()).isEqualTo("helloword-config");
    }

    @Test
    public void shouldParseConfigRefFromNameWithContentTypeNamePattern() {
        ApiRouterKitFlowTopLevelElement apiRouterSnippet = new ApiRouterKitFlowTopLevelElement(
                "get:/helloworld:application/json:helloword-config",
                List.of(),
                null,
                Map.of());
        assertThat(apiRouterSnippet.getConfigRef()).isEqualTo("helloword-config");
    }

    @Test
    public void shouldParsePathAndMethod() {
        String name = "post:/clients/{client_identifier}/risk/rating:application/json:hbfr-bil-risk-client-rating-mb05-hub-sys-config";
        ApiRouterKitFlowTopLevelElement apiRouterSnippet = new ApiRouterKitFlowTopLevelElement(
                name,
                List.of(),
                null,
                Map.of());

        assertTrue(ApiRouterKitFlowTopLevelElement.isApiRouterKitName(name));
        assertThat(apiRouterSnippet.getMethod()).isEqualTo("post");
        assertThat(apiRouterSnippet.getRoute()).isEqualTo("/clients/{client_identifier}/risk/rating");
    }
}
