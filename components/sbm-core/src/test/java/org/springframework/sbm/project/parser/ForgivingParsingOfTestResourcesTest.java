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
package org.springframework.sbm.project.parser;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openrewrite.Parser;
import org.openrewrite.SourceFile;
import org.openrewrite.tree.ParsingEventListener;
import org.openrewrite.tree.ParsingExecutionContextView;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.rewrite.project.resource.RewriteSourceFileHolder;
import org.springframework.sbm.project.resource.TestProjectContext;
import org.springframework.sbm.test.TestProjectContextInfo;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Fabian Krüger
 */
public class ForgivingParsingOfTestResourcesTest {

    @Test
    @DisplayName("Proof that resource with syntax error is excluded from AST but other resources aren't")
    @Disabled("TODO: Move into launcher")
    void test_renameMe() {
        TestProjectContextInfo projectContextInfo = TestProjectContext
                .buildProjectContext()
                .withProjectResource("src/test/resources/one.yaml", "valid: true")
                .withProjectResource("src/test/resources/error.yaml", """
                        min-risk-score:
                          100 # illegal line break
                        attenuation-duration: !include attenuation-duration_ok.yaml
                          risk-score-classes: !include risk-score-class_ok.yaml # illegal indentation
                        exposure-config: !include exposure-config_ok.yaml
                        """)
                .withProjectResource("src/test/resources/three.yaml", "is.valid: true")
                .buildProjectContextInfo();
        ProjectContext context = projectContextInfo.projectContext();

        List<RewriteSourceFileHolder<? extends SourceFile>> parsedResources = context.getProjectResources().list();
        assertThat(parsedResources).hasSize(3);
        assertThat(parsedResources.get(0).getSourcePath().toString()).isEqualTo("pom.xml");
        assertThat(parsedResources.get(1).getSourcePath().toString()).isEqualTo("src/test/resources/one.yaml");
        // src/test/resources/error.yaml is ignored
        assertThat(parsedResources.get(2).getSourcePath().toString()).isEqualTo("src/test/resources/three.yaml");
        ParsingExecutionContextView contextView = ParsingExecutionContextView.view(projectContextInfo.executionContext());
//        assertThat(contextView.getParseFailures()).hasSize(1);
//        assertThat(contextView.getParseFailures().get(0).getText()).isEqualTo("""
//                                                                                min-risk-score:
//                                                                                  100 # illegal line break
//                                                                                attenuation-duration: !include attenuation-duration_ok.yaml
//                                                                                  risk-score-classes: !include risk-score-class_ok.yaml # illegal indentation
//                                                                                exposure-config: !include exposure-config_ok.yaml
//                                                                                """);
    }
}
