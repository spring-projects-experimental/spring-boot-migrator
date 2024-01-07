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
package org.springframework.sbm;

import org.openrewrite.ExecutionContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.rewrite.parsers.RewriteExecutionContext;
import org.springframework.rewrite.scopes.annotations.ScanScope;

import java.util.function.Supplier;

/**
 * @author Fabian Krüger
 */
@Configuration
public class SbmCoreConfig {
    @Bean
    @ScanScope
    @Primary
    Supplier<ExecutionContext> executionContextSupplier() {
        return () -> new RewriteExecutionContext();
    }
}
