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
package org.springframework.sbm.boot.upgrade_27_30.report;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.sbm.boot.properties.SpringApplicationPropertiesPathMatcher;
import org.springframework.sbm.boot.properties.SpringBootApplicationPropertiesRegistrar;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;


/**
 * @author Fabian Krüger
 */
public class ChangesToDataPropertiesReportTest {

    @Test
    @DisplayName("Changes to Data Properties should render")
    void changesToDataPropertiesSection_renders() {
        ProjectContext context = TestProjectContext.buildProjectContext()
                .addRegistrar(new SpringBootApplicationPropertiesRegistrar(new SpringApplicationPropertiesPathMatcher()))
                .addProjectResource("src/main/resources/application.properties", "spring.data.foo=bar")
                .addProjectResource("src/main/resources/application-another.properties", "spring.data.here=there")
                .build();

        SpringBootUpgradeReportTestSupport.generatedSection("Changes to Data Properties")
                .fromProjectContext(context)
                .shouldRenderAs(
                        """
                        === Changes to Data Properties
                        Issue: https://github.com/spring-projects-experimental/spring-boot-migrator/issues/441[#441], Contributors: https://github.com/fabapp2[@fabapp2^, role="ext-link"]
                                                              
                        ==== What Changed
                        The data prefix has been reserved for Spring Data and any properties under the `data` prefix imply that Spring
                        Data is required on the classpath.
                                                              
                        ==== Why is the application affected
                        The scan found properties with `spring.data` prefix but no dependency matching `org.springframework.data:.*`.
                        
                        * file://<PATH>/src/main/resources/application.properties[`src/main/resources/application.properties`]
                        ** `spring.data.foo`
                        * file://<PATH>/src/main/resources/application-another.properties[`src/main/resources/application-another.properties`]
                        ** `spring.data.here`
                                           
                        ==== Remediation
                        Either add `spring-data` dependency, rename the property or remove it in case it's not required anymore.
                                                              
                        """);
    }

    @Test
    @DisplayName("Changes to Data Properties shouldn't render")
    void changesToDataPropertiesSection_notRendered() {
        ProjectContext context = TestProjectContext.buildProjectContext()
                .addRegistrar(new SpringBootApplicationPropertiesRegistrar(new SpringApplicationPropertiesPathMatcher()))
                .addProjectResource("src/main/resources/application.properties", "data.foo=bar")
                .addProjectResource("src/main/resources/application-another.properties", "data.here=there")
                .build();

        SpringBootUpgradeReportTestSupport.generatedSection("Changes to Data Properties")
                .fromProjectContext(context)
                .shouldNotRender();
    }

}
