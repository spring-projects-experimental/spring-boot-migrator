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
package org.springframework.sbm.java.util;

import org.springframework.sbm.java.api.JavaSource;
import org.springframework.sbm.project.resource.ApplicationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BasePackageCalculator {

    private final ApplicationProperties applicationProperties;

    public String calculateBasePackage(List<JavaSource> javaSources) {
       if(javaSources.isEmpty()) return applicationProperties.getDefaultBasePackage();

        List<JavaSource> sortableJavaSources = new ArrayList<>();
        sortableJavaSources.addAll(javaSources);
        javaSources = sortableJavaSources;
        javaSources.sort(Comparator.comparing(js -> js.getPackageName().split("\\.").length));
        JavaSource javaSourceInBasePackage = javaSources.get(0);
        if(javaSources.size() > 1) {
            JavaSource shortestPackage = javaSourceInBasePackage;
            String shortestPackageName = shortestPackage.getPackageName();

            Optional<JavaSource> javaSourceInDifferentBasePackage = javaSources.stream()
                    .filter(js -> !js.getPackageName().startsWith(shortestPackageName))
                    .findFirst();

            if(javaSourceInDifferentBasePackage.isPresent()) {
                return applicationProperties.getDefaultBasePackage();
                /*
                throw new RuntimeException(String.format("Could not calculate base package. Found at least two conflicting candidates: [%s] and [%s] found in these resources [%s] and [%s]",
                        javaSources.get(0).getPackageName(),
                        javaSourceInDifferentBasePackage.get().getPackageName(),
                        javaSources.get(0).getAbsolutePath(),
                        javaSourceInDifferentBasePackage.get().getAbsolutePath()));
                 */
            }
        }

        return javaSourceInBasePackage.getPackageName();
    }

}
