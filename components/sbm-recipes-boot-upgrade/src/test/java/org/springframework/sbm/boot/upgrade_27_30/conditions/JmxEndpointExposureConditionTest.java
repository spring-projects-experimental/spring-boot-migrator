package org.springframework.sbm.boot.upgrade_27_30.conditions;

import org.junit.jupiter.api.Test;
import org.springframework.sbm.boot.properties.SpringApplicationPropertiesPathMatcher;
import org.springframework.sbm.boot.properties.SpringBootApplicationPropertiesRegistrar;
import org.springframework.sbm.engine.context.ProjectContext;
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
                .addRegistrar(new SpringBootApplicationPropertiesRegistrar(new SpringApplicationPropertiesPathMatcher()))
                .addProjectResource(Path.of("src", "main", "resources", "application.properties"), APPLICATION_PROPERTIES_WITH_JMX_ENDPOINT_EXPOSED)
                .build();

        JmxEndpointExposureCondition jmxEndpointExposureCondition = new JmxEndpointExposureCondition();
        assertThat(jmxEndpointExposureCondition.evaluate(projectContext)).isFalse();
    }

    @Test
    public void givenProjectWithJmxEndpointExposureCustomization_evaluateCondition_expectTrue() {
        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .addRegistrar(new SpringBootApplicationPropertiesRegistrar(new SpringApplicationPropertiesPathMatcher()))
                .addProjectResource(Path.of("src", "main", "resources", "application.properties"), APPLICATION_PROPERTIES_WITHOUT_JMX_ENDPOINT_EXPOSED)
                .build();

        JmxEndpointExposureCondition jmxEndpointExposureCondition = new JmxEndpointExposureCondition();
        assertThat(jmxEndpointExposureCondition.evaluate(projectContext)).isTrue();
    }

}
