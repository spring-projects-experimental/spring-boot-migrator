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
package org.springframework.sbm.parsers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.sbm.common.util.LinuxWindowsPathUnifier;
import org.springframework.sbm.utils.ResourceUtil;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

/**
 * @author Fabian Krüger
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ProjectScanner {
    private final ResourceLoader resourceLoader;

    public List<Resource> scan(Path baseDir, Set<String> ignoredPatters) {
        if(!baseDir.isAbsolute()) {
            baseDir = baseDir.toAbsolutePath().normalize();
        }
        if(!baseDir.toFile().exists()) {
            throw new IllegalArgumentException("Provided path does not exist: " + baseDir);
        }
        Path absoluteRootPath = baseDir.toAbsolutePath();
        String unifiedPath = new LinuxWindowsPathUnifier().unifyPath(absoluteRootPath.toString() + "/**");
        String pattern = "file:" + unifiedPath;
        try {
            Resource[] resources = ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResources(pattern);

            List<PathMatcher> pathMatchers = ignoredPatters.stream()
                    .map(baseDir.getFileSystem()::getPathMatcher)
                    .toList();

            return Stream.of(resources)
                    .filter(r -> isAccepted(r, pathMatchers))
                    .toList();

        } catch (IOException e) {
            throw new RuntimeException("Can't get resources for pattern '" + pattern + "'", e);
        }
    }

    private boolean isAccepted(Resource r, List<PathMatcher> pathMatchers) {
        if(ResourceUtil.getPath(r).toFile().isDirectory()) {
            return false;
        }
        Optional<PathMatcher> isIgnored = pathMatchers
                .stream()
                .filter(matcher -> {
                    Path resourcePath = ResourceUtil.getPath(r);
                    boolean matches = matcher.matches(resourcePath);
                    if(matches && log.isInfoEnabled()) {
                       log.info("Resource '%s' matches ignore pattern '%s'".formatted(resourcePath, matcher));
                    }
                    return matches;
                })
                .findFirst();
        if(isIgnored.isPresent() && log.isInfoEnabled()) {
            log.info("Ignoring scanned resource '%s'.".formatted(ResourceUtil.getPath(r)));
        }
        return isIgnored.isEmpty();
    }
}
