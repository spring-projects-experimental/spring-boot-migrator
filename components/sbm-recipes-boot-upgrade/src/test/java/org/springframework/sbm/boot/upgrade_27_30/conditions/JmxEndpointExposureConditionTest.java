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
package org.springframework.sbm.boot.upgrade_27_30.conditions;

import org.junit.jupiter.api.Test;
import org.springframework.sbm.boot.properties.SpringApplicationPropertiesPathMatcher;
import org.springframework.sbm.boot.properties.SpringBootApplicationPropertiesRegistrar;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.openrewrite.RewriteExecutionContext;
import org.springframework.sbm.project.resource.TestProjectContext;

import java.nio.file.Path;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class JmxEndpointExposureConditionTest {

    private static final String APPLICATION_PROPERTIES_WITH_JMX_ENDPOINT_EXPOSED = "foo=bar\n" +
            "migrate=true\n" +
            "management.endpoints.jmx.exposure.include=*\n";

    private static final String APPLICATION_PROPERTIES_WITHOUT_JMX_ENDPOINT_EXPOSED = "foo=bar\n" +
            "migrate=true\n";

    @Test
    public void givenProjectWithJmxEndpointExposureCustomization_evaluateCondition_expectFalse() {
        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .addRegistrar(new SpringBootApplicationPropertiesRegistrar(new SpringApplicationPropertiesPathMatcher(), new RewriteExecutionContext()))
                .withProjectResource(Path.of("src", "main", "resources", "application.properties"), APPLICATION_PROPERTIES_WITH_JMX_ENDPOINT_EXPOSED)
                .build();

        JmxEndpointExposureCondition jmxEndpointExposureCondition = new JmxEndpointExposureCondition();
        assertThat(jmxEndpointExposureCondition.evaluate(projectContext)).isFalse();
    }

    @Test
    public void givenProjectWithJmxEndpointExposureCustomization_evaluateCondition_expectTrue() {
        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .addRegistrar(new SpringBootApplicationPropertiesRegistrar(new SpringApplicationPropertiesPathMatcher(), new RewriteExecutionContext()))
                .withProjectResource(Path.of("src", "main", "resources", "application.properties"), APPLICATION_PROPERTIES_WITHOUT_JMX_ENDPOINT_EXPOSED)
                .build();

        JmxEndpointExposureCondition jmxEndpointExposureCondition = new JmxEndpointExposureCondition();
        assertThat(jmxEndpointExposureCondition.evaluate(projectContext)).isTrue();
    }

}
