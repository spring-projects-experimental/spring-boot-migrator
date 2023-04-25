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
package org.openrewrite.maven;

import org.jetbrains.annotations.Nullable;
import org.openrewrite.maven.tree.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * PomSerializer allows to serialize a {@link Pom} model to its String representation.
 *
 * WARNING: `repository.username` and `repository.password` will be lost during serialization.
 *
 *
 * @author Fabian Krüger
 */
public class PomSerializer {

    private static final String LE = System.lineSeparator();
    private static final String IN = "    ";

    public static String serialize(Pom pom) {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append(LE);
        sb
                .append("<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">")
                .append(LE);
        sb.append(IN).append("<modelVersion>4.0.0</modelVersion>").append(LE);

        Parent parent = pom.getParent();
        if (parent != null) {
            sb.append(
                e(1, "parent",
                  e(2, "groupId", parent.getGroupId()),
                  e(2, "artifactId", parent.getArtifactId()),
                  e(2, "version", parent.getVersion())
                )
            );
        }

        sb.append(e(1, "groupId", pom.getGroupId()));
        sb.append(e(1, "artifactId", pom.getArtifactId()));
        sb.append(e(1, "version", pom.getVersion()));
        sb.append(e(1, "packaging", "jar"));
        sb.append(e(1, "name", pom.getName()));

        Map<String, String> properties = pom.getProperties();
        if (!properties.isEmpty()) {
            sb.append(IN).append("<properties>").append(LE);
            properties.forEach((k, v) -> {
                sb.append(IN).append(IN).append("<" + k + ">" + v + "</" + k + ">").append(LE);
            });
            sb.append(IN).append("</properties>").append(LE);
        }

        List<MavenRepository> repositories = pom.getRepositories();
        if (repositories != null && !repositories.isEmpty()) {
            sb.append(IN).append("<repositories>").append(LE);
            repositories.stream().forEach(r -> renderRepository(sb, r));
            sb.append(IN).append("</repositories>").append(LE);
        }

        if(pom.getDependencies() != null && !pom.getDependencies().isEmpty()) {
            sb.append(renderDependencies(pom.getDependencies()));
        }

        if(pom.getPlugins() != null && !pom.getPlugins().isEmpty()) {
            sb.append(renderPlugins(pom));
        }

        if(!pom.getDependencyManagement().isEmpty()) {
            // TODO
        }
        if(!pom.getLicenses().isEmpty()) {
            // TODO
        }
        if(!pom.getPluginManagement().isEmpty()) {
            // TODO
        }
        if(!pom.getProfiles().isEmpty()) {
            // TODO
        }
//        if(pom.getRepository())


        sb.append("</project>").append(LE);
        String pomXml = sb.toString();
        System.out.println(pomXml);
        return pomXml;
    }

    private static String renderPlugins(Pom pom) {
        String plugins = pom.getPlugins().stream().map(PomSerializer::map).collect(Collectors.joining());
        return e2(1, "build", e2(2, "plugins", plugins));
    }

    private static String map(Plugin plugin) {
        return e2(3, "plugin",
                  e(4, "groupId", plugin.getGroupId()),
                  e(4, "artifactId", plugin.getArtifactId()),
                  e(4, "version", plugin.getVersion()),
                  e2(4, "configuration", renderPluginConfiguration(plugin))
           ) + LE;
    }

    @Nullable
    private static String renderPluginConfiguration(Plugin plugin) {
        Map<String, Object> configuration = plugin.getConfiguration("", Map.class);
        return configuration.entrySet().stream()
                .map(es -> PomSerializer.mapConfigProperty(5, es)).collect(Collectors.joining());
    }

    private static String mapConfigProperty(int level, Map.Entry<String, Object> e) {
        String tag = (String) e.getKey();
        String value;
        if(Map.class.isInstance(e.getValue())){
            Map<String, Object> map = (Map<String, Object>) e.getValue();
            int finalLevel = level +1;
//            String[] values = map.entrySet().stream().map(es -> PomSerializer.mapConfigProperty(finalLevel, es)).collect(Collectors.joining())
            value = map.entrySet().stream().map(es -> PomSerializer.mapConfigProperty(finalLevel, es)).collect(Collectors.joining());
            return e2(level, tag, value);
        }
        value = (String) e.getValue();
        return e(level, tag, value);
    }

    private static String renderDependencies(List<Dependency> dependencies) {
        String deps = dependencies.stream().map(PomSerializer::mapDependency).collect(Collectors.joining());
        return e2(1, "dependencies", deps);
    }


    private static String mapDependency(Dependency dependency) {
        return e2(2, "dependency",
                   e(3, "groupId", dependency.getGroupId()),
                   e(3, "artifactId", dependency.getArtifactId()),
                   e(3, "version", dependency.getVersion()),
                   e(3, "optional", dependency.getOptional()),
                   e(3, "type", dependency.getType()),
                   e(3, "scope", dependency.getScope())
                ) + LE;
    }

    private static String e(int level, String tag, String... value) {
        String values = Arrays
                .asList(value)
                .stream()
                .collect(Collectors.joining());
        return IN.repeat(level) + "<%s>%s%s%s</%s>%s".formatted(tag, LE, values, IN, tag, LE);
    }

    private static String e(int level, String tag, String value) {
        if(value == null) {
            return "";
        }
        return IN.repeat(level) + "<%s>%s</%s>%s".formatted(tag, value, tag, LE);
    }

    private static String e2(int i, String tag, String value) {
        return IN.repeat(i) + "<" + tag + ">" + LE +  value  + IN.repeat(i) + "</" + tag + ">" + LE;
    }

    private static String e2(int i, String tag, String... value) {
        String values = Arrays
                .asList(value)
                .stream()
                .collect(Collectors.joining());
        return IN.repeat(i) + "<" + tag + ">" + LE +  values + IN.repeat(i) + "</" + tag + ">";
    }


    private static void renderRepository(StringBuilder sb, MavenRepository r) {
        sb.append(IN).append(IN).append("<repository>").append(LE);
        sb.append(IN).append(IN).append(IN).append("<id>").append(r.getId()).append("</id>").append(LE);
        sb.append(IN).append(IN).append(IN).append("<uri>").append(r.getUri()).append("</uri>").append(LE);
        if(r.getUsername() != null) {
            e(3, "username", r.getUsername());
        }
        if (r.getPassword() != null) {
            sb
                    .append(IN)
                    .append(IN)
                    .append(IN)
                    .append("<password>")
                    .append(r.getPassword())
                    .append("</password>")
                    .append(LE);
        }
        if (r.getReleases() != null) {
            sb
                    .append(IN)
                    .append(IN)
                    .append(IN)
                    .append("<releases><enabled>")
                    .append(r.getReleases())
                    .append("</enabled></releases>")
                    .append(LE);
        }
        if (r.getSnapshots() != null) {
            sb
                    .append(IN)
                    .append(IN)
                    .append(IN)
                    .append("<snapshots><enabled>")
                    .append(r.getSnapshots())
                    .append("</enabled></snapshots>")
                    .append(LE);
        }
        if(r.isKnownToExist()) {

        }
        sb.append(IN).append(IN).append("</repository>").append(LE);
    }
}
