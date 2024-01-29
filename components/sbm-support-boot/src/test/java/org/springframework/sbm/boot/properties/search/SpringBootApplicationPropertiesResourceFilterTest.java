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
package org.springframework.sbm.boot.properties.search;

import org.springframework.rewrite.parser.RewriteExecutionContext;
import org.springframework.sbm.boot.properties.SpringApplicationPropertiesPathMatcher;
import org.springframework.sbm.boot.properties.SpringBootApplicationPropertiesRegistrar;
import org.springframework.sbm.boot.properties.api.SpringBootApplicationProperties;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SpringBootApplicationPropertiesResourceFilterTest {

    @Test
    void test() {
        ProjectContext context = TestProjectContext.buildProjectContext()
                .withProjectResource("src/main/resources/application.properties", "foo=bar\na=b")
                .addRegistrar(new SpringBootApplicationPropertiesRegistrar(new SpringApplicationPropertiesPathMatcher(), new RewriteExecutionContext()))
                .build();

        List<SpringBootApplicationProperties> properties = context.search(new SpringBootApplicationPropertiesResourceListFinder());
        assertThat(properties).hasSize(1);
        assertThat(properties.get(0).getProperty("foo").get()).isEqualTo("bar");
    }

}
