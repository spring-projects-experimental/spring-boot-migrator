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
package org.springframework.sbm.utils;

import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Utility class for {@link Resource}s.
 *
 * @author fkrueger
 */
public class ResourceUtil {
    public ResourceUtil() {
    }

    public static Path getPath(Resource resource) {
        try {
            return resource.getFile().toPath();
        } catch (IOException var2) {
            throw new RuntimeException(var2);
        }
    }

    public static InputStream getInputStream(Resource resource) {
        try {
            return resource.getInputStream();
        } catch (IOException var2) {
            throw new RuntimeException(var2);
        }
    }

    public static void write(Path basePath, List<Resource> resources) {
        resources.stream()
                .forEach(r -> ResourceUtil.persistResource(basePath, r));
    }

    private static void persistResource(Path basePath, Resource r) {
        Path resourcePath = ResourceUtil.getPath(r);
        if(resourcePath.isAbsolute()) {
            Path relativize = resourcePath.relativize(basePath);
        } else {
            resourcePath = basePath.resolve(resourcePath).toAbsolutePath().normalize();
        }
        if(resourcePath.toFile().exists()) {
            return;
        }
        try {
            if(!resourcePath.getParent().toFile().exists()) {
                Files.createDirectories(resourcePath.getParent());
            }
            Files.writeString(resourcePath, ResourceUtil.getContent(r));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getContent(Resource r) {
        try {
            return new String(getInputStream(r).readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static long contentLength(Resource resource) {
        try {
            return resource.contentLength();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
