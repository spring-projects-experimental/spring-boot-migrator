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
package org.springframework.sbm.gradle.tooling;

import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.ProjectConnection;
import org.gradle.tooling.internal.consumer.DefaultGradleConnector;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class ModelBuilder {
    public static <T> T forProjectDirectory(File projectDir, File buildFile, Class<T> modelClass) {
        DefaultGradleConnector connector = (DefaultGradleConnector) GradleConnector.newConnector();
        if (Files.exists(projectDir.toPath().resolve("gradle/wrapper/gradle-wrapper.properties"))) {
            connector.useBuildDistribution();
        } else {
            connector.useGradleVersion("7.6");
        }
        connector.forProjectDirectory(projectDir);
        List<String> arguments = new ArrayList<>();
        if (buildFile != null && buildFile.exists()) {
            arguments.add("-b");
            arguments.add(buildFile.getAbsolutePath());
        }
        arguments.add("--init-script");
        Path init = projectDir.toPath().resolve("openrewrite-tooling.gradle").toAbsolutePath();
        arguments.add(init.toString());
        try (ProjectConnection connection = connector.connect()) {
            org.gradle.tooling.ModelBuilder<T> customModelBuilder = connection.model(modelClass);
            try (InputStream is = ModelBuilder.class.getResourceAsStream("/init.gradle")) {
                if (is == null) {
                    throw new IllegalStateException("Expected to find init.gradle on the classpath");
                }
                Files.copy(is, init, StandardCopyOption.REPLACE_EXISTING);
                customModelBuilder.withArguments(arguments);
                return customModelBuilder.get();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            } finally {
                try {
                    Files.delete(init);
                } catch (IOException e) {
                    //noinspection ThrowFromFinallyBlock
                    throw new UncheckedIOException(e);
                }
            }
        }
    }
}
