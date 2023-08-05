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
package org.springframework.sbm.actions.spring.xml.migration;

import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.build.api.BuildFile;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MigrationContextFactory {

    MigrationContext createMigrationContext(ProjectContext context) {
        BuildFile buildFile = context.getBuildFile();
        List<Path> classpath = buildFile.getClasspath();
        ClassLoader classLoader = createClassLoader(classpath);
        MigrationContext migrationContext = new MigrationContext(context, classLoader);
        return migrationContext;
    }

    /**
     * Create a classloader with access required for migration.
     *
     * URLClassLoader provides access to classes and jars of the application to migrate
     * and its parent {@code ClassLoader.getPlatformClassLoader()} provides access to JDK classes.
     *
     * @param classpath to provide
     */
    private ClassLoader createClassLoader(List<Path> classpath) {
        URL[] classpathUrls = createUrlsFromClasspath(classpath);
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        URLClassLoader classLoader = new URLClassLoader("SBMCustomClassLoader", classpathUrls, contextClassLoader/*ClassLoader.getPlatformClassLoader()*/);
        return classLoader;
    }

    private URL[] createUrlsFromClasspath(List<Path> classpath) {
        return classpath
                .stream()
                .map(path -> {
                    try {
                        return path.toFile().toURI().toURL();
                    } catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList()).toArray(new URL[0]);
    }
}
