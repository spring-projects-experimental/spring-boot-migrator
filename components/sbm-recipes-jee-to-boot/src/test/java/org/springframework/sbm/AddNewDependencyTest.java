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
package org.springframework.sbm;

import org.springframework.sbm.build.api.BuildFile;
import org.springframework.sbm.build.api.Dependency;
import org.springframework.sbm.java.api.JavaSource;
import org.springframework.sbm.java.api.Type;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class AddNewDependencyTest {


    @Test
    @Disabled
    void t1_typesNotResolvedWhenDependencyIsMissing() {
        String javaSourceCode = "public class AnnotateMePlease {}";

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withJavaSources(javaSourceCode)
                .build();

        JavaSource javaSource = projectContext.getProjectJavaSources().list().get(0);
        Type type = javaSource.getTypes().get(0);
//        buildFile.addDependency(dependencyToAdd);
        type.addAnnotation("javax.ejb.Stateless");
        assertThat(javaSource.print()).isEqualTo(
                "@Stateless\n" +
                        "public class AnnotateMePlease {}"
        );
        assertThat(type.hasAnnotation("javax.ejb.Stateless")).isFalse();
    }

    // FIXME: flaky tests, succeed one by one, fail depending on the order when executed together
    // in FindTypes ident.getType() is not null when t1_typesNotResolvedWhenDependencyIsMissing runs last
    @Test
    @Disabled
    void t1_typesShouldBeResolvedWhenDependencyIsAdded() {
        String javaSourceCode = "public class AnnotateMePlease {}";
        Dependency dependencyToAdd = Dependency.builder()
                .groupId("javax.ejb")
                .artifactId("javax.ejb-api")
                .version("3.2")
                .build();

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withJavaSources(javaSourceCode)
                .build();

        JavaSource javaSource = projectContext.getProjectJavaSources().list().get(0);
        Type type = javaSource.getTypes().get(0);

        BuildFile buildFile = projectContext.getBuildFile();

        buildFile.addDependency(dependencyToAdd);

        type.addAnnotation("javax.ejb.Stateless");
        assertThat(javaSource.print()).isEqualTo(
                "import javax.ejb.Stateless;\n" +
                        "\n" +
                        "@Stateless\n" +
                        "public class AnnotateMePlease {}"
        );
        assertThat(type.hasAnnotation("javax.ejb.Stateless")).isTrue();
    }


}
