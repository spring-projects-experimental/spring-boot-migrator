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
package org.springframework.sbm.boot.upgrade_27_30.checks;

import org.junit.jupiter.api.Test;
import org.springframework.sbm.boot.asciidoctor.Section;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.java.api.JavaSource;
import org.springframework.sbm.java.api.JavaSourceAndType;
import org.springframework.sbm.java.api.Type;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ApacheSolrRepositorySectionBuilderTest {

    @Test
    void givenAContextWithSolrRepository_applySectionBuilder_validateReport() {
        ApacheSolrRepositoryBeanFinder finder = mock(ApacheSolrRepositoryBeanFinder.class);

        Type type = mock(Type.class);
        Type type1 = mock(Type.class);
        JavaSource javaSource = mock(JavaSource.class);
        JavaSource javaSource1 = mock(JavaSource.class);
        JavaSourceAndType javaSourceAndType = new JavaSourceAndType(javaSource,type);
        JavaSourceAndType javaSourceAndType1 = new JavaSourceAndType(javaSource1,type1);

        ProjectContext context = mock(ProjectContext.class);
        ApacheSolrRepositorySectionBuilder sut = new ApacheSolrRepositorySectionBuilder(finder);
        List<JavaSourceAndType> matches = List.of(
                javaSourceAndType,
                javaSourceAndType1
        );

        when(context.search(finder)).thenReturn(matches);
        when(type.getFullyQualifiedName()).thenReturn("com.foo.bar.SomeClass");
        when(type1.getFullyQualifiedName()).thenReturn("com.foo.baz.AnotherClass");

        Section section = sut.build(context);

        String rendered = SectionRendererTestUtil.render(section);
        assertThat(rendered).isEqualTo(
                """
                        === `Spring Data ApacheSolr` support has been removed
                        Support for `Spring Data ApacheSolr` has been removed in Spring Framework 6
                        
                        ==== Relevance
                        
                        The scan found bean declarations of type `SolrCrudRepository`.  
                      
                        ==== Todo
                        
                        Remove repositories of type `SolrCrudRepository`
                        
                        
                        * [ ] Remove from class `com.foo.bar.SomeClass` 
                        * [ ] Remove from class `com.foo.baz.AnotherClass`
                        """
        );
    }

}