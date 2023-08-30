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
package org.springframework.sbm.boot.upgrade_27_30.actions;

import org.junit.jupiter.api.Test;
import org.springframework.sbm.boot.properties.SpringApplicationPropertiesPathMatcher;
import org.springframework.sbm.boot.properties.SpringBootApplicationPropertiesRegistrar;
import org.springframework.sbm.boot.properties.api.SpringBootApplicationProperties;
import org.springframework.sbm.boot.properties.search.SpringBootApplicationPropertiesResourceListFilter;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.openrewrite.RewriteExecutionContext;
import org.springframework.sbm.project.resource.TestProjectContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class Boot_27_30_JmxEndpointExposureActionTest {
    private final String DUMMY_PROPERTY_FILE = "foo=bar\n" +
            "defaultBasePackage=org.springframework.sbm";

    @Test
    public void givenAProjectWithoutJmxEndpointExposureOverride_andSpringBootProperties_applyAction_expectPropertyAdded() {
        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .addRegistrar(new SpringBootApplicationPropertiesRegistrar(new SpringApplicationPropertiesPathMatcher(), new RewriteExecutionContext()))
                .withProjectResource("src/main/resources/application.properties", DUMMY_PROPERTY_FILE)
                .build();

        Boot_27_30_JmxEndpointExposureAction boot_27_30_jmxEndpointExposureAction = new Boot_27_30_JmxEndpointExposureAction();
        boot_27_30_jmxEndpointExposureAction.apply(projectContext);

        List<SpringBootApplicationProperties> bootApplicationProperties = new SpringBootApplicationPropertiesResourceListFilter().apply(projectContext.getProjectResources());
        assertThat(bootApplicationProperties.size()).isEqualTo(1);
        assertThat(bootApplicationProperties.get(0).getProperty("management.endpoints.jmx.exposure.include").isPresent()).isTrue();

    }
}
