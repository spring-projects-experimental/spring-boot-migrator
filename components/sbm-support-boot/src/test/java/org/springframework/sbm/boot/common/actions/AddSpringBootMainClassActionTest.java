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
package org.springframework.sbm.boot.common.actions;

import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;
import freemarker.template.Configuration;
import freemarker.template.Version;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class AddSpringBootMainClassActionTest {

    private final AddSpringBootMainClassAction sut = new AddSpringBootMainClassAction();

    @BeforeEach
    void setUp() throws IOException {
        Version version = new Version("2.3.0");
        Configuration configuration = new Configuration(version);
        configuration.setDirectoryForTemplateLoading(new File("src/main/resources/templates"));
        sut.setConfiguration(configuration);
    }

    @Test
    void testApplyShouldAddNewSource() {

        ProjectContext context = TestProjectContext.buildProjectContext()
                .withDummyRootBuildFile()
                .withJavaSources(
                        "package org.springframework.sbm.root;\n" + // package defines packagename for new class
                                "public class Foo {}")
                .build();

        sut.apply(context);

        assertThat(context.getProjectJavaSources().list()).hasSize(2);
        assertThat(context.getProjectJavaSources().list().get(1).print())
                .isEqualToNormalizingNewlines(
                        """
                                package org.springframework.sbm.root;
                                                                
                                import org.springframework.boot.SpringApplication;
                                import org.springframework.boot.autoconfigure.SpringBootApplication;
                                                                
                                @SpringBootApplication
                                public class SpringBootApp {
                                
                                    public static void main(String[] args) {
                                        SpringApplication.run(SpringBootApp.class, args);
                                    }
                                }
                                """
                );
    }
}
