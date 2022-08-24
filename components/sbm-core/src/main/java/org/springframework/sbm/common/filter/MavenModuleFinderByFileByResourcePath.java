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

package org.springframework.sbm.common.filter;

import lombok.AllArgsConstructor;
import org.springframework.sbm.project.resource.ProjectResourceSet;
import org.springframework.sbm.project.resource.RewriteSourceFileHolder;
import org.springframework.sbm.project.resource.filter.ProjectResourceFinder;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@AllArgsConstructor
public class MavenModuleFinderByFileByResourcePath implements ProjectResourceFinder<Path> {

    private final Path resourcePath;

    @Override
    public Path apply(ProjectResourceSet projectResourceSet) {
        List<String> listOfPom = projectResourceSet
                .stream()
                .map(RewriteSourceFileHolder::getAbsolutePath)
                .map(Path::toString)
                .filter(k -> k.contains("pom.xml"))
                .toList();


        Set<String> binarySearchTree = new HashSet<>();

        listOfPom.forEach(k -> {
            String path = k.substring(0, k.length() - "pom.xml".length());
            binarySearchTree.add(path);
        });

        String interestedFile = resourcePath.toAbsolutePath().toString();

        for (int i = interestedFile.length() - 1; i >= 0; i--) {

            if (interestedFile.charAt(i) != '/') continue;

            String subPath = interestedFile.substring(0, i + 1);

            if (binarySearchTree.contains(subPath)) {
                return Path.of(subPath);
            }
        }

        throw new IllegalStateException("Cannot find enclosing maven project for file: "
                + resourcePath + " could be caused by erroneously calling this function or running a recipe on an " +
                "empty project");
    }
}
