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
package org.springframework.sbm.boot.upgrade_27_30.report.helper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.sbm.boot.upgrade_27_30.report.SpringBootUpgradeReportTestSupport;
import org.springframework.sbm.build.util.PomBuilder;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;

/**
 * @author Fabian Krüger
 */
public class ActuatorEndpointsSanitizationReportSectionTest {

    @Test
    @DisplayName("Actuator Endpoints SanitizationReport should render")
    void shouldRender() {
        String parentPom = PomBuilder
                .buildParentPom("org.springframework.boot:spring-boot-starter-parent:3.0.0", "com.example:parent:1.0")
                .withModules("moduleA", "moduleB", "moduleC")
                .build();
        String moduleA = PomBuilder
                .buildPom("com.example:parent:1.0", "moduleA")
                .compileScopeDependencies("com.example:moduleC:1.0")
                .build();
        String moduleB = PomBuilder
                .buildPom("com.example:parent:1.0", "moduleB")
                .compileScopeDependencies("com.example:moduleC:1.0")
                .build();
        String moduleC = PomBuilder
                .buildPom("com.example:parent:1.0", "moduleC")
                .compileScopeDependencies("org.springframework.boot:spring-boot-starter-actuator")
                .build();

        ProjectContext context = TestProjectContext
                .buildProjectContext()
                .withMavenRootBuildFileSource(parentPom)
                .withMavenBuildFileSource("moduleA", moduleA)
                .withMavenBuildFileSource("moduleB", moduleB)
                .withMavenBuildFileSource("moduleC", moduleC)
                .build();

        SpringBootUpgradeReportTestSupport.generatedSection("Actuator Endpoints Sanitization")
                .fromProjectContext(context)
                .shouldRenderAs("""
                                        === Actuator Endpoints Sanitization
                                        Issue: https://github.com/spring-projects-experimental/spring-boot-migrator/issues/445[#445^, role="ext-link"], Contributors: https://github.com/fabapp2[@fabapp2^, role="ext-link"]
                                                                      
                                        ==== What Changed
                                        Since, the `/env` and `/configprops` endpoints can contains sensitive values, all values are always masked by default.
                                        This used to be case only for keys considered to be sensitive.
                                                                      
                                        Instead, this release opts for a more secure default.
                                        The keys-based approach has been removed in favor of a role based approach, similar to the health endpoint details.
                                        Whether unsanitized values are shown or not can be configured using a property which can have the following values:
                                                                     
                                        - `NEVER` - All values are sanitized.
                                        - `ALWAYS` - All values are present in the output (sanitizing functions will apply).
                                        - `WHEN_AUTHORIZED` - Values are present in the output only if a user is authorized (sanitizing functions will apply).
                                                                      
                                        For JMX, users are always considered to be authorized. For HTTP, users are considered to be authorized if they are authenticated and have the specified roles.
                                                                      
                                        Sanitization for the QuartzEndpoint is also configurable in the same way.
                                                                     
                                        ==== Why is the application affected
                                        The scan found a dependency to actuator on the classpath.
                                                                                
                                        * file://<PATH>/moduleA/pom.xml[`moduleA/pom.xml`]
                                        * file://<PATH>/moduleB/pom.xml[`moduleB/pom.xml`]
                                        * file://<PATH>/moduleC/pom.xml[`moduleC/pom.xml`]
                                                                    
                                        ==== Remediation
                                        
                                        ===== Verify the new sanitization fulfills your requirements
                                        Please verify that none of the sanitized values must be plain text to meet your requirements.
                                        If some sanitized values are required in plain text a custom `SanitizingFunction` must be provided.
                                        Please see the documentation about https://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/html/howto.html#howto.actuator.sanitize-sensitive-values.customizing-sanitization[Customizing Sanitization^, role="ext-link"]\s
                                        for further information on how to do this.
                                                                                
                                        * https://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/html/howto.html#howto.actuator.sanitize-sensitive-values[Sanitize Sensitive Values^, role="ext-link"]
                                        * https://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/html/howto.html#howto.actuator.sanitize-sensitive-values.customizing-sanitization[Customizing Sanitization^, role="ext-link"]
                                                                                
                                                                                
                                        """);
    }

}
