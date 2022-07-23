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
package org.springframework.sbm.boot.upgrade.common.actions;

import org.jetbrains.annotations.NotNull;
import org.openrewrite.Tree;
import org.openrewrite.marker.Markers;
import org.openrewrite.text.PlainText;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.AbstractAction;
import org.springframework.sbm.project.resource.RewriteSourceFileHolder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static java.nio.file.StandardOpenOption.WRITE;
import static java.util.function.Predicate.not;

public class CreateAutoconfigurationAction extends AbstractAction {

    private static final String SPRING_FACTORIES_PATH = "src/main/resources/META-INF/spring.factories";
    private static final String AUTO_CONFIGURATION_IMPORTS = "/org.springframework.boot.autoconfigure.AutoConfiguration.imports";

    @NotNull
    public static Path getAutoConfigurationPath(Path springFactoriesPath) {
        return Path.of(springFactoriesPath.getParent() + AUTO_CONFIGURATION_IMPORTS);
    }

    public static boolean isSpringFactory(Path path) {
        return path.endsWith(SPRING_FACTORIES_PATH);
    }

    @Override
    public void apply(ProjectContext context) {
        try (Stream<Path> walkStream = Files.walk(context.getProjectRootDirectory())) {
            walkStream.filter(p -> p.toFile().isFile())
                .map(Path::toAbsolutePath)
                .filter(CreateAutoconfigurationAction::isSpringFactory)
                .filter(not(path -> Files.exists(getAutoConfigurationPath(path))))
                .forEach(springFactoriesPath -> createAutoConfiguration(springFactoriesPath, context));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void createAutoConfiguration(Path springFactoriesPath, ProjectContext context) {
        Path autoConfigurationPath = getAutoConfigurationPath(springFactoriesPath);
        createFile(autoConfigurationPath);
        String content = getContent(springFactoriesPath);
        writeToFile(autoConfigurationPath, content);
        RewriteSourceFileHolder<PlainText> newResource = new RewriteSourceFileHolder<>(autoConfigurationPath, getPlainText(autoConfigurationPath, content));
        newResource.markAsChanged();
        context.getProjectResources().add(newResource);
    }

    @NotNull
    private PlainText getPlainText(Path autoConfigurationPath, String content) {
        return new PlainText(Tree.randomId(), autoConfigurationPath, Markers.EMPTY, null, false, null, null, content);
    }

    private void writeToFile(Path autoConfigurationPath, @NotNull String content) {
        try {
            Files.writeString(autoConfigurationPath, content, WRITE);
        } catch (IOException e) {
            throw new RuntimeException("Could not write to file " + autoConfigurationPath, e);
        }
    }

    private void createFile(Path autoConfigurationPath) {
        try {
            Files.createFile(autoConfigurationPath);
        } catch (IOException e) {
            throw new RuntimeException("Could not create file " + autoConfigurationPath, e);
        }
    }

    @NotNull
    private String getContent(Path springFactoriesPath) {
        try {
            return Files.readString(springFactoriesPath)
                .replace("org.springframework.boot.autoconfigure.EnableAutoConfiguration=", "")
                .replace("\\", "")
                .replace("\n", "")
                .replace(",", "\n");
        } catch (IOException e) {
            throw new RuntimeException("Could not read file " + springFactoriesPath, e);
        }
    }

}
