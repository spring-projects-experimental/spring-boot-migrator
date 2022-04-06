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
import org.springframework.sbm.project.resource.ApplicationProperties;
import org.springframework.sbm.project.resource.ResourceHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PathScanner {

    private final ApplicationProperties applicationProperties;
    private final ResourceHelper resourceHelper;
    private AntPathMatcher antPathMatcher = new AntPathMatcher(File.separator);

    public List<Resource> scan(Path projectRoot) {
        String pattern = "**";
        Path absoluteRootPath = projectRoot.toAbsolutePath();
        Resource[] resources = resourceHelper.loadResources("file:" + absoluteRootPath + "/" + pattern);

        return Arrays.asList(resources).stream()
                .filter(p -> this.isRelevant(p, absoluteRootPath))
                .collect(Collectors.toList());
    }

    private boolean isRelevant(Resource givenResource, Path rootPath) {
        if (getPath(givenResource).toFile().isDirectory()) {
            return false;
        }
        return applicationProperties.getIgnoredPathsPatterns().stream()
                .noneMatch(ir -> antPathMatcher.match(rootPath.resolve(ir).toString(), getPath(givenResource).toAbsolutePath().normalize().toString()));
    }

    private Path getPath(Resource r) {
        try {
            return r.getFile().toPath().toAbsolutePath().normalize();
        } catch (IOException e) {
            throw new ProjectParserException(String.format("Error retrieving path for Resource '%s'", r), e);
        }
    }


}
