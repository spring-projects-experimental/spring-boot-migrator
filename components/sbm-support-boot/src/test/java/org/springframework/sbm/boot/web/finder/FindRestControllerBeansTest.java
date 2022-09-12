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

package org.springframework.sbm.boot.web.finder;

import org.junit.jupiter.api.Test;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;
import org.springframework.sbm.boot.web.api.RestControllerBean;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class FindRestControllerBeansTest {
    @Test
    void test_renameMe() {
        String restController =
                """
                        package com.example.rest;
                        import org.springframework.web.bind.annotation.RestController;
                        import org.springframework.web.bind.annotation.GetMapping;
                                        
                        @RestController
                        public class MyRestEndpoint {
                            @GetMapping({"/one","/other"})
                            String getRoot() {
                                return "";   
                            }
                        }
                        """;
        ProjectContext context = TestProjectContext
                .buildProjectContext()
                .withBuildFileHavingDependencies("org.springframework:spring-web:5.3.22")
                .addJavaSource("src/main/java", restController)
                .build();

        List<RestControllerBean> restControllerBeans = context.search(new FindRestControllerBeans());

        assertThat(restControllerBeans).hasSize(1);
        assertThat(restControllerBeans.get(0).getRestMethods().get(0).path()).containsExactlyInAnyOrder("/one", "/other");
        assertThat(restControllerBeans.get(0).getRestMethods().get(0).method()).containsExactly(RequestMethod.GET);
    }
}