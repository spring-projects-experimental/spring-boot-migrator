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

package org.springframework.sbm.boot.common.finder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.sbm.project.resource.TestProjectContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SpringBeanMethodDeclarationFinderTest {

    public static final String SOME_BEAN = "a.b.c.SomeBean";
    private TestProjectContext.Builder builder;
    private SpringBeanMethodDeclarationFinder sut;

    @BeforeEach
    void beforeEach() {
        builder = TestProjectContext.buildProjectContext();
        sut = new SpringBeanMethodDeclarationFinder(SOME_BEAN);
    }

    @Nested
    class EmptyProjectContext {
        @Test
        void shouldReturnEmptyResult() {
            List<MatchingMethod> matches = builder.build().search(sut);
            assertThat(matches).isEmpty();
        }
    }

    @Nested
    class WithMatchingBean {

        @BeforeEach
        void beforeEach() {
            builder.withJavaSource("src/main/java",
                                          """
                                          import org.springframework.context.annotation.Configuration;
                                          import org.springframework.context.annotation.Bean;
                                          import a.b.c.SomeBean;
                                          
                                          @Configuration
                                          public class MyConfiguration {
                                            @Bean
                                            SomeBean someBean() {
                                                return null;
                                            }       
                                          }
                                          """
                    )
                    .withJavaSource("src/main/java",
                                           """
                                           package a.b.c;
                                           public class SomeBean {}
                                           """)
                    .withBuildFileHavingDependencies("org.springframework:spring-context:5.3.22");
        }


        @Test
        void shouldReturnTheMatchingBeanDeclaration() {
            List<MatchingMethod> matches = builder.build().search(sut);
            assertThat(matches).hasSize(1);
            assertThat(matches.get(0).getJavaSource().getSourcePath().toString()).isEqualTo("src/main/java/MyConfiguration.java");
            assertThat(matches.get(0).getType().getFullyQualifiedName()).isEqualTo("MyConfiguration");
            assertThat(matches.get(0).getMethod().getReturnValue().get()).isEqualTo("a.b.c.SomeBean");
            assertThat(matches.get(0).getMethod().getName()).isEqualTo("someBean");
        }
    }

    @Nested
    class WithMultipleMatchingBeans {

        @BeforeEach
        void beforeEach() {
            builder.withJavaSource("src/main/java",
                                  """
                                  import org.springframework.context.annotation.Configuration;
                                  import org.springframework.context.annotation.Bean;
                                  import a.b.c.SomeBean;
                                  import a.b.c.AnotherBean;
                                  
                                  @Configuration
                                  public class MyConfiguration {
                                    @Bean
                                    SomeBean someBean() {
                                        return null;
                                    }       
                                    @Bean
                                    AnotherBean anotherBean() {
                                        return null;
                                    }
                                  }
                                  """
                    )
                    .withJavaSource("src/main/java",
                                   """
                                   import org.springframework.context.annotation.Configuration;
                                   import org.springframework.context.annotation.Bean;
                                   import a.b.c.SomeBean;
                                   import a.b.c.AnotherBean;
                                   
                                   @Configuration
                                   public class MyConfiguration2 {
                                     @Bean
                                     SomeBean someBean2() {
                                         return null;
                                     }       
                                     @Bean
                                     AnotherBean anotherBean2() {
                                         return null;
                                     }
                                   }
                                   """
                    )
                    .withJavaSource("src/main/java",
                                   """
                                   package a.b.c;
                                   public class SomeBean {}
                                   """)
                    .withJavaSource("src/main/java",
                                   """
                                   package a.b.c;
                                   public class AnotherBean {}
                                   """)
                    .withBuildFileHavingDependencies("org.springframework:spring-context:5.3.22");
        }


        @Test
        void shouldReturnTheMatchingBeanDeclarations() {
            List<MatchingMethod> matches = builder.build().search(sut);
            assertThat(matches).hasSize(2);
            assertThat(matches.get(0).getJavaSource().getSourcePath().toString()).isEqualTo("src/main/java/MyConfiguration.java");
            assertThat(matches.get(0).getType().getFullyQualifiedName()).isEqualTo("MyConfiguration");
            assertThat(matches.get(0).getMethod().getReturnValue()).isPresent();
            assertThat(matches.get(0).getMethod().getReturnValue().get()).isEqualTo("a.b.c.SomeBean");
            assertThat(matches.get(0).getMethod().getName()).isEqualTo("someBean");
            assertThat(matches.get(1).getJavaSource().getSourcePath().toString()).isEqualTo("src/main/java/MyConfiguration2.java");
            assertThat(matches.get(1).getType().getFullyQualifiedName()).isEqualTo("MyConfiguration2");
            assertThat(matches.get(1).getMethod().getName()).isEqualTo("someBean2");
            assertThat(matches.get(1).getMethod().getReturnValue()).isPresent();
            assertThat(matches.get(1).getMethod().getReturnValue().get()).isEqualTo("a.b.c.SomeBean");
        }
    }


    @Nested
    class WithVoidBeans {

        @Test
        void shouldReturnEmptyListWithVoidReturnTypeOnBean() {

            builder.withJavaSource("src/main/java",
                            """
                            import org.springframework.context.annotation.Configuration;
                            import org.springframework.context.annotation.Bean;
                            
                            @Configuration
                            public class MyConfiguration {
                              @Bean
                              void someBean() {
                                  return null;
                              }
                            }
                            """
                    )
                    .withBuildFileHavingDependencies("org.springframework:spring-context:5.3.22");

            List<MatchingMethod> output = builder.build().search(sut);
            assertThat(output).isEmpty();
        }
    }

    @Nested
    class WithUnresolvedSymbols {
        @Test
        void shouldIgnoreClassesWhenSymbolsCantBeResolved() {

            SpringBeanMethodDeclarationFinder sut
                    = new SpringBeanMethodDeclarationFinder("a.b.c.HelloWorld");
            builder.withJavaSource("src/main/java",
                            """
                            import org.springframework.context.annotation.Configuration;
                            import org.springframework.context.annotation.Bean;
                            import a.b.c.HelloWorld;
                            @Configuration
                            public class MyConfiguration {
                              @Bean
                              HelloWorld someBean() {
                                  return null;
                              }
                            }
                            """
                    )
                    .withBuildFileHavingDependencies("org.springframework:spring-context:5.3.22");


            List<MatchingMethod> output = builder.build().search(sut);
            assertThat(output).hasSize(0);
        }
    }
}
