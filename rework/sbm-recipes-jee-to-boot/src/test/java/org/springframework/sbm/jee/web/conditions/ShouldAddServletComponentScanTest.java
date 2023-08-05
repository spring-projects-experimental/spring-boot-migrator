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
package org.springframework.sbm.jee.web.conditions;

import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.Condition;
import org.springframework.sbm.java.migration.conditions.HasTypeAnnotation;
import org.springframework.sbm.project.resource.TestProjectContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ShouldAddServletComponentScanTest {

    @ParameterizedTest
    @CsvSource({
            "true,true,true,false",
            "true,true,false,false",
            "true,false,true,true",
            "false,true,true,false"
    })
    void shouldReturnTrueIfAllConditionsAreMet(boolean isSpringBootApplication, boolean isNotAnnotatedWithServletScanComponent, boolean hasWebComponentAnnotation, boolean expected) {
        ProjectContext context = TestProjectContext.buildProjectContext().build();
        ShouldAddServletComponentScan sut = spy(new ShouldAddServletComponentScan());

        Condition isSpringBootApplicationCondition = mock(HasTypeAnnotation.class);
        Condition isNotAnnotatedWithServletScanComponentCondition = mock(HasTypeAnnotation.class);
        Condition hasWebComponentAnnotationCondition = mock(HasTypeAnnotation.class);

        doReturn(isSpringBootApplicationCondition).when(sut).createIsSpringBootApplicationCondition();
        doReturn(isNotAnnotatedWithServletScanComponentCondition).when(sut).createAnnotatedWithServletScanComponentCondition();
        doReturn(List.of(hasWebComponentAnnotationCondition)).when(sut).createHasWebComponentAnnotation();

        when(isSpringBootApplicationCondition.evaluate(context)).thenReturn(isSpringBootApplication);
        when(isNotAnnotatedWithServletScanComponentCondition.evaluate(context)).thenReturn(isNotAnnotatedWithServletScanComponent);
        when(hasWebComponentAnnotationCondition.evaluate(context)).thenReturn(hasWebComponentAnnotation);

        assertThat(sut.evaluate(context)).isEqualTo(expected);
    }

    @Test
    void testCreateIsSpringBootApplicationCondition() {
        ShouldAddServletComponentScan sut = new ShouldAddServletComponentScan();

        HasTypeAnnotation condition = sut.createIsSpringBootApplicationCondition();

        assertThat(condition.getAnnotation())
                .isEqualTo("org.springframework.boot.autoconfigure.SpringBootApplication");
    }

    @Test
    void testCreateAnnotatedWithServletScanComponentCondition() {
        ShouldAddServletComponentScan sut = new ShouldAddServletComponentScan();

        HasTypeAnnotation condition = sut.createAnnotatedWithServletScanComponentCondition();

        assertThat(condition.getAnnotation())
                .isEqualTo("org.springframework.boot.web.servlet.ServletComponentScan");
    }

    @Test
    void testCreateHasWebComponentAnnotationTest() {
        ShouldAddServletComponentScan sut = new ShouldAddServletComponentScan();

        List<HasTypeAnnotation> conditions = sut.createHasWebComponentAnnotation();

        assertThat(conditions)
                .extracting(HasTypeAnnotation::getAnnotation)
                .contains("javax.servlet.annotation.WebListener",
                        "javax.servlet.annotation.WebServlet",
                        "javax.servlet.annotation.WebFilter");
    }

}