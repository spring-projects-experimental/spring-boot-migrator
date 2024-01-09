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

import lombok.Getter;
import lombok.Setter;
import org.openrewrite.java.JavaParser;
import org.springframework.rewrite.scopes.annotations.ScanScope;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

/**
 * @author Fabian Krüger
 */
@Component
@ScanScope
public class JavaParserBuilder extends JavaParser.Builder{

    @Getter
    @Setter
    private JavaParser.Builder builder = JavaParser.fromJavaVersion();

    public Supplier<JavaParser.Builder> getSupplier() {
        return () -> builder;
    }

    @Override
    public JavaParser build() {
        return builder.build();
    }
}
