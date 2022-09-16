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
package org.springframework.sbm.jee.jpa.actions;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;

/**
 * @author Fabian Krüger
 */
class RenameUnitNameOfPersistenceContextAnnotationsToDefaultTest {
    @Test
    void shouldRemoveUnitNameAttribute() {

        String javaCode = """
                import javax.persistence.PersistenceContext;
                import javax.persistence.EntityManager;
                public class Foo {
                    @PersistenceContext(unitName = "foo")
                    private EntityManager entityManager;
                }
                """;

        ProjectContext context = TestProjectContext
                .buildProjectContext()
                .addJavaSource("src/main/java", javaCode)
                .withBuildFileHavingDependencies("javax.persistence:javax.persistence-api:2.2", "javax.ejb:javax.ejb-api:3.2")
                .build();

        RenameUnitNameOfPersistenceContextAnnotationsToDefault sut = new RenameUnitNameOfPersistenceContextAnnotationsToDefault();

        sut.apply(context);

        String expected = """
                import javax.persistence.PersistenceContext;
                import javax.persistence.EntityManager;
                public class Foo {
                    @PersistenceContext(unitName = "default")
                    private EntityManager entityManager;
                }
                """;


        Assertions.assertThat(context.getProjectJavaSources().list().get(0).print()).isEqualTo(expected);
    }
}