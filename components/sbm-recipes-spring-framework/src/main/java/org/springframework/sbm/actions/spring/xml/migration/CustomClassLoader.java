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
package org.springframework.sbm.actions.spring.xml.migration;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CustomClassLoader extends URLClassLoader {

    private Optional<ClassLoader> additionalClassLoader = Optional.empty();

    public CustomClassLoader(ClassLoader parentClassLoader) {
        super(new URL[0], parentClassLoader);
    }

    public void setAdditionalClassLoader(Optional<ClassLoader> additionalClassLoader) {
        this.additionalClassLoader = additionalClassLoader;
    }



    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        if(additionalClassLoader.isPresent()) {
            try {
                return additionalClassLoader.get().loadClass(name);
            } catch (ClassNotFoundException cnfe) {
                return super.loadClass(name);
            }
        } else {
            return super.loadClass(name);
        }
    }

    private ClassLoader createClassLoader(List<Path> classpath) {
        URL[] classpathUrls = createUrlsFromClasspath(classpath);
        URLClassLoader classLoader = new URLClassLoader("AdditionalClassLoader", classpathUrls, null);
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
