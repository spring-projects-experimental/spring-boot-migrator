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
package org.springframework.sbm.java.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.sbm.build.api.BuildFile;
import org.springframework.sbm.build.api.Dependency;
import org.springframework.sbm.build.impl.OpenRewriteMavenBuildFile;
import org.springframework.sbm.build.util.PomBuilder;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.context.ProjectContextHolder;
import org.springframework.sbm.parsers.RewriteExecutionContext;
import org.springframework.sbm.parsers.JavaParserBuilder;
import org.springframework.sbm.project.resource.TestProjectContext;

/**
 * @author Fabian Krüger
 */
class DependencyChangeHandlerTest {
    @Test
    @DisplayName("Adding a dependency Should recompile affected modules")
    void shouldRecompileAffectedModules() {
        String parentPom = PomBuilder.buildPom("com.example:parent:1.0")
                .withModules("a", "b", "c")
                .build();
        // a -> b
        String aPom = PomBuilder.buildPom("com.example:parent:1.0", "a")
                .compileScopeDependencies("com.example:b:1.0")
                .build();

        String bPom = PomBuilder.buildPom("com.example:parent:1.0", "b")
                .build();

        String cPom = PomBuilder.buildPom("com.example:parent:1.0", "c")
                .build();

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withProjectResource("pom.xml", parentPom)
                .withProjectResource("a/pom.xml", aPom)
                .withJavaSource("a/src/main/java",
                        """
                                package com.example.a;
                                import com.example.b.BModuleClass;
                                                        
                                public class AModuleClass {
                                    private BModuleClass b;
                                } 
                                """)
                .withJavaSource("a/src/test/java",
                        """
                                package com.example.a;
                                                        
                                public class AModuleClassTest {
                                    private AModuleClass a;
                                } 
                                """)
                .withProjectResource("b/pom.xml", bPom)
                .withJavaSource("b/src/main/java",
                        """
                                package com.example.b;
                                                        
                                public class BModuleClass {
                                } 
                                """)
                .withProjectResource("c/pom.xml", cPom)
                .build();

        ProjectContextHolder projectContextHolder = new ProjectContextHolder();
        projectContextHolder.setProjectContext(projectContext);
        BuildFile buildFile = projectContext.getApplicationModules().getModule("c").getBuildFile();
        buildFile.addDependency(Dependency.builder()
                        .groupId("javax.validation")
                        .artifactId("validation-api")
                        .version("2.0.1.Final")
                        .scope("Test")
                        .build()
        );
        DependencyChangeHandler sut = new DependencyChangeHandler(projectContextHolder, new JavaParserBuilder(), new RewriteExecutionContext());
        sut.handleDependencyChanges((OpenRewriteMavenBuildFile) buildFile);
    }
}