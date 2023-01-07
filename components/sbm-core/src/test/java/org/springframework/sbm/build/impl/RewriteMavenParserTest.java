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
package org.springframework.sbm.build.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openrewrite.ExecutionContext;
import org.springframework.sbm.build.util.PomBuilder;
import org.springframework.sbm.openrewrite.RewriteExecutionContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author Fabian Krüger
 */
@ExtendWith(MockitoExtension.class)
class RewriteMavenParserTest {

    @Mock
    MavenSettingsInitializer mavenSettingsInitializer;
    @InjectMocks
    RewriteMavenParser sut;

    @Test
    void noExecutionContextGiven() {
        verify(mavenSettingsInitializer).initializeMavenSettings(any(ExecutionContext.class));
    }

    @Test
    void customExecutionContextGiven() {
        String pom = PomBuilder.buildPom("com.example:project:1.0").build();
        ExecutionContext ctx = new RewriteExecutionContext();
        sut.parse(ctx, pom);
        verify(mavenSettingsInitializer).initializeMavenSettings(ctx);
    }

}