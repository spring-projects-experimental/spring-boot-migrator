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

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ApiRouterKitFlowDefinitionSnippetTest {

    @Test
    public void shouldDetectAPIRoutingKitNamingPattern() {

        assertTrue(ApiRouterKitFlowDefinitionSnippet.isApiRouterKitName("get:/helloworld:helloword-config"));
    }

    @Test
    public void shouldBeAbleToDetectNonAPIRoutingKitNames() {
        assertFalse(ApiRouterKitFlowDefinitionSnippet.isApiRouterKitName("helloword-config"));
    }

    @Test
    public void shouldParseConfigRefFromName() {

        String name = "get:/helloworld:helloword-config";
        ApiRouterKitFlowDefinitionSnippet apiRouterSnippet = new ApiRouterKitFlowDefinitionSnippet(name, Collections.emptyList());
        assertThat(apiRouterSnippet.getConfigRef()).isEqualTo("helloword-config");
    }

    @Test
    public void shouldParseConfigRefFromNameWithContentTypeNamePattern() {
        String name = "get:/helloworld:application/json:helloword-config";
        ApiRouterKitFlowDefinitionSnippet apiRouterSnippet = new ApiRouterKitFlowDefinitionSnippet(name, Collections.emptyList());
        assertThat(apiRouterSnippet.getConfigRef()).isEqualTo("helloword-config");
    }

    @Test
    public void shouldParsePathAndMethod() {
        String name = "post:/clients/{client_identifier}/risk/rating:application/json:hbfr-bil-risk-client-rating-mb05-hub-sys-config";
        ApiRouterKitFlowDefinitionSnippet apiRouterSnippet = new ApiRouterKitFlowDefinitionSnippet(name, Collections.emptyList());

        assertTrue(ApiRouterKitFlowDefinitionSnippet.isApiRouterKitName(name));
        assertThat(apiRouterSnippet.getMethod()).isEqualTo("post");
        assertThat(apiRouterSnippet.getRoute()).isEqualTo("/clients/{client_identifier}/risk/rating");
    }
}
