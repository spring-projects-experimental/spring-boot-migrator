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
package org.springframework.sbm.java.util;

import lombok.RequiredArgsConstructor;
import org.springframework.sbm.java.api.JavaSource;
import org.springframework.sbm.project.resource.SbmApplicationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Calculates and returns the base package when given a list of JavaSource
 *
 * @author Soumya Prakash Behera
 */
@Component
@RequiredArgsConstructor
public class BasePackageCalculator {

    private final SbmApplicationProperties sbmApplicationProperties;

    public String calculateBasePackage(List<JavaSource> javaSources) {
        if(javaSources.isEmpty()) {
            return sbmApplicationProperties.getDefaultBasePackage();
        }

        if(javaSources.size() == 1) {
           return javaSources.get(0).getPackageName();
        }

        List<String[]> javaSourcesAsStringArray = new ArrayList<>();

        for(JavaSource javaSource : javaSources) {
            javaSourcesAsStringArray.add(javaSource.getPackageName().split("\\."));
        }

        javaSourcesAsStringArray.sort(Comparator.comparingInt(str -> str.length));

        String[] shortestPackage = javaSourcesAsStringArray.get(0);

        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < shortestPackage.length; i++) {
            for (String[] strArray : javaSourcesAsStringArray) {
                if(!strArray[i].equals(shortestPackage[i])) {
                    return sb.isEmpty() ? "" : sb.substring(1);
                }
            }
            sb.append(".").append(shortestPackage[i]);
        }

        return String.join(".", shortestPackage);
    }

}
