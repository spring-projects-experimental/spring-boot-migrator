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

package org.springframework.sbm.build.util;import org.springframework.sbm.project.parser.DependencyHelper;

import java.util.Arrays;
import java.util.List;

public class PomBuilder {
        private String coordinate;
        private List<String> modules;
        private String type;
        private String parent;
        private String artifactId;
        private List<String> dependencies;

        public static PomBuilder buiildPom(String coordinate) {
            PomBuilder pomBuilder = new PomBuilder();
            pomBuilder.coordinate = coordinate;
            return pomBuilder;
        }

        public static PomBuilder buiildPom(String parent, String artifactId) {
            PomBuilder pomBuilder = new PomBuilder();
            pomBuilder.parent = parent;
            pomBuilder.artifactId = artifactId;
            return pomBuilder;
        }

        public PomBuilder withModules(String... modules) {
            this.modules = Arrays.asList(modules);
            return this;
        }

        public String build() {
            StringBuilder sb = new StringBuilder();
            sb.append(
                    """
                    <?xml version="1.0" encoding="UTF-8"?>
                    <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                        <modelVersion>4.0.0</modelVersion>
                    """
            );

            if(parent != null) {
                String[] coord = parent.split(":");
                sb.append("    <parent>").append("\n");
                sb.append("        <groupId>").append(coord[0]).append("</groupId>").append("\n");
                sb.append("        <artifactId>").append(coord[1]).append("</artifactId>").append("\n");
                sb.append("        <version>").append(coord[2]).append("</version>").append("\n");
                sb.append("    </parent>").append("\n");
                sb.append("    <artifactId>").append(artifactId).append("</artifactId>").append("\n");
            } else {
                String[] coord = coordinate.split(":");
                sb.append("    <groupId>").append(coord[0]).append("</groupId>").append("\n");
                sb.append("    <artifactId>").append(coord[1]).append("</artifactId>").append("\n");
                sb.append("    <version>").append(coord[2]).append("</version>").append("\n");
            }


            if(type != null ){
                sb.append("    <type>").append(type).append("</type>").append("\n");
            }

            if(modules != null && !modules.isEmpty()) {
                sb.append("    <modules>").append("\n");
                modules.forEach(m -> sb.append("        <module>").append(m).append("</module>\n"));
                sb.append("    </modules>").append("\n");
            }

            if(dependencies != null) {
                String dependenciesRendered = buildDependencies(dependencies);
                sb.append(dependenciesRendered);
            }

            sb.append("</project>");
            return sb.toString();
        }

        String buildDependencies(List<String> dependencyCoordinates) {
            DependencyHelper dependencyHelper = new DependencyHelper();
            StringBuilder dependenciesSection = new StringBuilder();
            dependenciesSection.append("    ").append("<dependencies>").append("\n");
            dependencyHelper.mapCoordinatesToDependencies(dependencyCoordinates).stream().forEach(dependency -> {
                dependenciesSection.append("    ").append("    ").append("<dependency>").append("\n");
                dependenciesSection.append("    ").append("    ").append("    ").append("<groupId>").append(dependency.getGroupId()).append("</groupId>").append("\n");
                dependenciesSection.append("    ").append("    ").append("    ").append("<artifactId>").append(dependency.getArtifactId()).append("</artifactId>").append("\n");
                dependenciesSection.append("    ").append("    ").append("    ").append("<version>").append(dependency.getVersion()).append("</version>").append("\n");
                dependenciesSection.append("    ").append("    ").append("</dependency>").append("\n");
            });
            dependenciesSection.append("    ").append("</dependencies>").append("\n");
            String dependenciesText = dependenciesSection.toString();
            return dependenciesText;
        }

        public PomBuilder type(String type) {
            this.type = type;
            return this;
        }

        public PomBuilder dependencies(String... s) {
            this.dependencies = Arrays.asList(s);
            return this;
        }
}
