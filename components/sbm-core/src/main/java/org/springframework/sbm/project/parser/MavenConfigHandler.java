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

package org.springframework.sbm.project.parser;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class MavenConfigHandler {

    private final MavenConfigParser configParser;

    public void injectMavenConfigIntoSystemProperties(List<Resource> resources) {
        List<String> mavenConfigs = getMavenConfigs(resources);

        Map<String, String> config = configParser.parse(mavenConfigs);
        config.keySet().forEach(k -> System.setProperty(k, config.get(k)));
    }

    private List<String> getMavenConfigs(List<Resource> resources) {
        return resources.stream()
                .filter(p -> getPath(p).getFileName().toString().equals("maven.config"))
                .flatMap(this::readLines)
                .collect(Collectors.toList());
    }

    private Stream<String> readLines(Resource r){
        try {
            return Files.readAllLines(r.getFile().toPath()).stream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private static Path getPath(Resource r) {
        try {
            return r.getFile().toPath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
