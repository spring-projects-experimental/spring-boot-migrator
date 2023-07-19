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
package org.springframework.sbm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.parsers.ProjectScanner;
import org.springframework.sbm.project.parser.ProjectContextInitializer;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

/**
 * @author Fabian Krüger
 */
@SpringBootApplication
public class InvertedSbm implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(InvertedSbm.class, args);
    }

    @Autowired
    ProjectContextInitializer projectContextInitializer;
    @Autowired
    ProjectScanner projectScanner;

    @Override
    public void run(String... args) throws Exception {
        Path baseDir = Path.of("/Users/fkrueger/projects/sbm-projects/demo-spring-song-app");
        List<Resource> resources = projectScanner.scan(baseDir, Set.of("**/.DS_*","**/.idea/**", "**/.git/**"));
        ProjectContext context = projectContextInitializer.initProjectContext(baseDir, resources);
        context.getProjectJavaSources()
                .stream()
                .forEach(js -> System.out.println(js.getAbsolutePath().toString()));
    }
}
