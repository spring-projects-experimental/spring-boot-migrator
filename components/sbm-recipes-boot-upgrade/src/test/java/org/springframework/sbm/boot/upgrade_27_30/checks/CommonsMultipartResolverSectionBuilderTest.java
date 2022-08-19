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

package org.springframework.sbm.boot.upgrade_27_30.checks;

import org.junit.jupiter.api.Test;
import org.springframework.sbm.boot.asciidoctor.Section;
import org.springframework.sbm.boot.common.finder.MatchingMethod;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.java.api.Type;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.matches;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CommonsMultipartResolverSectionBuilderTest {

    @Test
    void test_renameMe() {
        CommonsMultipartResolverBeanFinder finder = mock(CommonsMultipartResolverBeanFinder.class);

        Type type = mock(Type.class);
        Type type2 = mock(Type.class);

        ProjectContext context = mock(ProjectContext.class);
        CommonsMultipartResolverSectionBuilder sut = new CommonsMultipartResolverSectionBuilder(finder);
        List<MatchingMethod> matches = List.of(
                new MatchingMethod(null, type, null),
                new MatchingMethod(null, type2, null)
        );

        when(finder.findMatches(context)).thenReturn(matches);
        when(type.getFullyQualifiedName()).thenReturn("com.foo.bar.SomeClass");
        when(type2.getFullyQualifiedName()).thenReturn("com.foo.baz.AnotherClass");

        Section section = sut.build(context);

        String rendered = SectionRendererTestUtil.render(section);
        assertThat(rendered).isEqualTo(
                """
                        === `CommonsMultipartResolver` support has been removed
                        Support for Spring Framework’s `CommonsMultipartResolver` has been removed following its removal in Spring Framework 6
                        
                        ==== Relevance
                        
                        The scan found bean declarations of type `CommonsMultipartResolver`.  
                      
                        ==== Todo
                        
                        Remove beans of type `CommonsMultipartResolver` and rely on Spring Boot auto-configuration
                        
                        
                        * [ ] Remove from class `com.foo.bar.SomeClass` 
                        * [ ] Remove from class `com.foo.baz.AnotherClass`
                        """
        );
    }

}