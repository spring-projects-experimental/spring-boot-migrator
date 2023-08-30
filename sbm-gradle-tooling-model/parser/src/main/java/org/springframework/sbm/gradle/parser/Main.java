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
package org.springframework.sbm.gradle.parser;

import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.SourceFile;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
//        List<SourceFile> sources = GradleProjectParser.parse(new File("sample"), new File("sample/build.gradle.kts"), new InMemoryExecutionContext(), new DefaultParserConfig()).collect(Collectors.toList());
//        System.out.println(sources.size());
//
//        sources = GradleProjectParser.parse(new File("demo"), new File("demo/build.gradle.kts"), new InMemoryExecutionContext(), new DefaultParserConfig()).collect(Collectors.toList());
//        System.out.println(sources.size());

        List<SourceFile> sources = GradleProjectParser.parse(new File("/Users/aboyko/Documents/STS4-arm/spring-petclinic"), new File("/Users/aboyko/Documents/STS4-arm/spring-petclinic/build.gradle"), new InMemoryExecutionContext(), new DefaultParserConfig()).collect(Collectors.toList());
        System.out.println(sources.size());
    }
}