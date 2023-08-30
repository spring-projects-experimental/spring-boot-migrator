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
package org.springframework.sbm.project.resource;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ResourceHelper {

    private final ResourceLoader resourceLoader;

    public Resource[] loadResources(String pattern) {
        try {
            return ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResources(pattern);
        } catch (IOException e) {
            throw new RuntimeException("Can't get resources for pattern '" + pattern + "'", e);
        }
    }

    public static String getResourceAsString(Resource resource) {
        try (InputStream inputStream = resource.getInputStream()) {
            // TODO: discuss if we should handle non UTF-8 encodings here, e.g. with http://tika.apache.org/
            return new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new IllegalArgumentException("Can't get resource content as String for '" + resource.getFilename(), e);
        }
    }
}
