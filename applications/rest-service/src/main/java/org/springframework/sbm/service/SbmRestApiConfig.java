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
package org.springframework.sbm.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.sbm.engine.recipe.UserInteractions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration
public class SbmRestApiConfig {
    @Bean
    UserInteractions userInteractionsPlaceholder() {
        return new UserInteractions() {

            @Override
            public boolean askUserYesOrNo(String question) {
                return false;
            }

            @Override
            public String askForInput(String question) {
                return null;
            }
        };
    }

    @Bean
    MappingJackson2HttpMessageConverter messageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        return new MappingJackson2HttpMessageConverter(objectMapper);
    }
}
