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
package org.springframework.sbm.boot.upgrade_27_30;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openrewrite.Recipe;
import org.openrewrite.Result;
import org.openrewrite.test.RewriteTest;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;


public class CrudRepositoryExtensionTest implements RewriteTest {

    private final JavaTestHelper javaTestHelper = new JavaTestHelper();
    private static final Recipe crudRepoExtensionRecipe = new CrudRepositoryExtensionWithReferences(
            "org.springframework.data.repository.PagingAndSortingRepository",
            "org.springframework.data.repository.CrudRepository"
    );

    private static final Recipe reactiveCrudExtensionRecipe = new CrudRepositoryExtensionWithReferences(
            "org.springframework.data.repository.reactive.ReactiveSortingRepository",
            "org.springframework.data.repository.reactive.ReactiveCrudRepository"
    );

    private static final Recipe rxJavaCrudExtensionRecipe = new CrudRepositoryExtensionWithReferences(
            "org.springframework.data.repository.reactive.RxJava3SortingRepository",
            "org.springframework.data.repository.reactive.RxJava3CrudRepository"
    );


    @ParameterizedTest
    @MethodSource("repositoryTestArguments")
    public void shouldAddCrudRepository(Recipe recipe, String pagingAndSortingRepository, String crudRepository, String repositoryPackage) {

        @NotNull List<Result> result = javaTestHelper.runRecipe(
                recipe,
                List.of(replacePagingRepoAndCrudRepo("""
                                package -repositoryPackage-;
                                public interface -pagingRepository-<T, ID> {
                                }
                                """, pagingAndSortingRepository, crudRepository, repositoryPackage),
                        replacePagingRepoAndCrudRepo("""
                                package -repositoryPackage-;
                                public interface -crudRepository-<T, ID> {
                                    void save(T entity);
                                }
                                """, pagingAndSortingRepository, crudRepository, repositoryPackage)
                ),
                replacePagingRepoAndCrudRepo("""
                        package test;
                        import -repositoryPackage-.-pagingRepository-;
                        public interface A extends -pagingRepository-<String, Long> {
                        }
                        """, pagingAndSortingRepository, crudRepository, repositoryPackage),
                replacePagingRepoAndCrudRepo("""
                        package test;
                        public class Hello  {
                            public void test(A a) {
                                a.save("Hello");
                            }
                        }
                        """, pagingAndSortingRepository, crudRepository, repositoryPackage)
        );

        javaTestHelper.assertResult(result, replacePagingRepoAndCrudRepo("""
                package test;
                import -repositoryPackage-.-crudRepository-;
                import -repositoryPackage-.-pagingRepository-;
                                                
                public interface A extends -pagingRepository-<String, Long>, -crudRepository-<String, Long> {
                }
                """, pagingAndSortingRepository, crudRepository, repositoryPackage
        ));
    }

    @ParameterizedTest
    @MethodSource("repositoryTestArguments")
    public void canDoQuestionMark(Recipe recipe, String pagingAndSortingRepository, String crudRepository, String repositoryPackage) {

        @NotNull List<Result> result = javaTestHelper.runRecipe(recipe,
                List.of(replacePagingRepoAndCrudRepo("""
                                package -repositoryPackage-;
                                public interface -pagingRepository-<T, ID> {
                                }
                                """, pagingAndSortingRepository, crudRepository, repositoryPackage),
                        replacePagingRepoAndCrudRepo("""
                                package -repositoryPackage-;
                                public interface -crudRepository-<T, ID> {
                                    void save(T);
                                }
                                """, pagingAndSortingRepository, crudRepository, repositoryPackage),
                        replacePagingRepoAndCrudRepo("""
                                package test;
                                public interface Payment<T> {
                                    T hello();
                                }
                                """, pagingAndSortingRepository, crudRepository, repositoryPackage)),
                replacePagingRepoAndCrudRepo("""
                        package test;
                        import -repositoryPackage-.-pagingRepository-;
                        public interface A extends -pagingRepository-<Payment<?>, Long> {
                        }
                        """, pagingAndSortingRepository, crudRepository, repositoryPackage),
                replacePagingRepoAndCrudRepo("""
                        package test;
                        public class Hello  {
                            public void test(A a) {
                                a.save("Hello");
                            }
                        }
                        """, pagingAndSortingRepository, crudRepository, repositoryPackage)
        );

        javaTestHelper.assertResult(result, replacePagingRepoAndCrudRepo("""
                        package test;
                        import -repositoryPackage-.-crudRepository-;
                        import -repositoryPackage-.-pagingRepository-;
                                        
                        public interface A extends -pagingRepository-<Payment<?>, Long>, -crudRepository-<Payment<?>, Long> {
                        }
                        """, pagingAndSortingRepository, crudRepository, repositoryPackage));
    }

    @MethodSource("repositoryTestArguments")
    @ParameterizedTest
    public void whenThereAreNoParametersWhilstExtending(Recipe recipe, String pagingAndSortingRepository, String crudRepository, String repositoryPackage) {

        @NotNull List<Result> results = javaTestHelper.runRecipe(
                recipe,
                List.of(replacePagingRepoAndCrudRepo("""
                                package -repositoryPackage-;
                                public interface -pagingRepository-<T, ID> {
                                }
                                """, pagingAndSortingRepository, crudRepository, repositoryPackage),
                        replacePagingRepoAndCrudRepo("""
                                package -repositoryPackage-;
                                public interface -crudRepository-<T, ID> {
                                }
                                """, pagingAndSortingRepository, crudRepository, repositoryPackage)),
                replacePagingRepoAndCrudRepo("""
                        package test;
                        import -repositoryPackage-.-pagingRepository-;
                        public interface A extends -pagingRepository- {
                        }
                        """, pagingAndSortingRepository, crudRepository, repositoryPackage),
                replacePagingRepoAndCrudRepo("""
                        package test;
                        public class Hello  {
                            public void test(A a) {
                                a.save("Hello");
                            }
                        }
                        """, pagingAndSortingRepository, crudRepository, repositoryPackage)
        );

        javaTestHelper.assertResult(results, replacePagingRepoAndCrudRepo("""
                        package test;
                        import -repositoryPackage-.-crudRepository-;
                        import -repositoryPackage-.-pagingRepository-;
                                        
                        public interface A extends -pagingRepository-, -crudRepository- {
                        }
                        """, pagingAndSortingRepository, crudRepository, repositoryPackage));
    }

    @MethodSource("repositoryTestArguments")
    @ParameterizedTest
    public void multipleExtends(Recipe recipe, String pagingAndSortingRepository, String crudRepository, String repositoryPackage) {
        @NotNull List<Result> results = javaTestHelper.runRecipe(
                recipe,
                List.of(replacePagingRepoAndCrudRepo("""
                                package -repositoryPackage-;
                                public interface -pagingRepository-<T, ID> {
                                }
                                """, pagingAndSortingRepository, crudRepository, repositoryPackage),
                        replacePagingRepoAndCrudRepo("""
                                package temp;
                                public interface Hello<T, ID> {
                                }
                                """, pagingAndSortingRepository, crudRepository, repositoryPackage),
                        replacePagingRepoAndCrudRepo("""
                                package -repositoryPackage-;
                                public interface -crudRepository-<T, ID> {
                                }
                                """, pagingAndSortingRepository, crudRepository, repositoryPackage)),
                replacePagingRepoAndCrudRepo("""
                        package test;
                        import -repositoryPackage-.-pagingRepository-;
                        import temp.Hello;
                        public interface A extends Hello<String, Long>, -pagingRepository- {
                        }
                        """, pagingAndSortingRepository, crudRepository, repositoryPackage),
                replacePagingRepoAndCrudRepo("""
                        package test;
                        public class Hello  {
                            public void test(A a) {
                                a.save("Hello");
                            }
                        }
                        """, pagingAndSortingRepository, crudRepository, repositoryPackage)
        );

        javaTestHelper.assertResult(results, replacePagingRepoAndCrudRepo("""
                        package test;
                        import -repositoryPackage-.-crudRepository-;
                        import -repositoryPackage-.-pagingRepository-;
                        import temp.Hello;
                                        
                        public interface A extends Hello<String, Long>, -pagingRepository-, -crudRepository- {
                        }
                        """, pagingAndSortingRepository, crudRepository, repositoryPackage));
    }

    @MethodSource("repositoryTestArguments")
    @ParameterizedTest
    public void classImplementsPagingRepository(Recipe recipe, String pagingAndSortingRepository, String crudRepository, String repositoryPackage) {
        @NotNull List<Result> result = javaTestHelper.runRecipe(
                recipe,
                List.of(replacePagingRepoAndCrudRepo("""
                                package -repositoryPackage-;
                                public interface -pagingRepository-<T, ID> {
                                }
                                """, pagingAndSortingRepository, crudRepository, repositoryPackage),
                        """
                                package temp;
                                public interface Hello<T, ID> {
                                }
                                """,
                        replacePagingRepoAndCrudRepo("""
                                package -repositoryPackage-;
                                public interface -crudRepository-<T, ID> {
                                }
                                """, pagingAndSortingRepository, crudRepository, repositoryPackage)),
                replacePagingRepoAndCrudRepo("""
                        package test;
                        import -repositoryPackage-.-pagingRepository-;
                        import temp.Hello;
                        public class A implements Hello<String, Long>, -pagingRepository-<String, Long> {
                        }
                        """, pagingAndSortingRepository, crudRepository, repositoryPackage),
                replacePagingRepoAndCrudRepo("""
                        package test;
                        public class Hello  {
                            public void test(A a) {
                                a.save("Hello");
                            }
                        }
                        """, pagingAndSortingRepository, crudRepository, repositoryPackage)
        );

        javaTestHelper.assertResult(result, replacePagingRepoAndCrudRepo("""
                        package test;
                        import -repositoryPackage-.-crudRepository-;
                        import -repositoryPackage-.-pagingRepository-;
                        import temp.Hello;
                                        
                        public class A implements Hello<String, Long>, -pagingRepository-<String, Long>, -crudRepository-<String, Long> {
                        }
                        """, pagingAndSortingRepository, crudRepository, repositoryPackage));
    }

    @ParameterizedTest
    @MethodSource("repositoryTestArguments")
    void shouldExtendCrudRepositoryInInnerInterface(Recipe recipe, String pagingAndSortingRepository,
                                                    String crudRepository,
                                                    String repositoryPackage) {
        @NotNull List<Result> result = javaTestHelper.runRecipe(
                recipe,
                List.of(replacePagingRepoAndCrudRepo("""
                                package -repositoryPackage-;
                                public interface -crudRepository-<T, ID> {
                                    void save(String entity);
                                }
                                """, pagingAndSortingRepository, crudRepository, repositoryPackage),
                        replacePagingRepoAndCrudRepo("""
                                package -repositoryPackage-;
                                public interface -pagingRepository-<T, ID> {
                                }
                                """, pagingAndSortingRepository, crudRepository, repositoryPackage)
                ),
                replacePagingRepoAndCrudRepo("""
                        package test;
                        import -repositoryPackage-.-pagingRepository-;
                        class Hello {
                            public interface A extends -pagingRepository-<String, Long> {
                            }

                            public void myCall(A a) {
                                a.save("");
                            }
                        }
                        """, pagingAndSortingRepository, crudRepository, repositoryPackage)
        );

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAfter().printAll())
                .isEqualTo(
                        replacePagingRepoAndCrudRepo("""
                        package test;
                        import -repositoryPackage-.-crudRepository-;
                        import -repositoryPackage-.-pagingRepository-;
                        
                        class Hello {
                            public interface A extends -pagingRepository-<String, Long>, -crudRepository-<String, Long> {
                            }

                            public void myCall(A a) {
                                a.save("");
                            }
                        }
                        """, pagingAndSortingRepository, crudRepository, repositoryPackage)
                );
    }

    @ParameterizedTest
    @MethodSource("repositoryTestArguments")
    void shouldExtendCrudRepositoryForCrudMethodReference(Recipe recipe, String pagingAndSortingRepository,
                                                          String crudRepository,
                                                          String repositoryPackage) {
        @NotNull List<Result> result = javaTestHelper.runRecipe(
                recipe,
                List.of(replacePagingRepoAndCrudRepo("""
                                package -repositoryPackage-;
                                public interface -crudRepository-<T, ID> {
                                    void save(String entity);
                                }
                                """, pagingAndSortingRepository, crudRepository, repositoryPackage),
                        replacePagingRepoAndCrudRepo("""
                                package -repositoryPackage-;
                                public interface -pagingRepository-<T, ID> {
                                }
                                """, pagingAndSortingRepository, crudRepository, repositoryPackage)
                ),
                replacePagingRepoAndCrudRepo("""
                        package test;
                        import java.util.List;
                        
                        import -repositoryPackage-.-pagingRepository-;
                        
                        class Hello {
                            public interface A extends -pagingRepository-<String, Long> {
                            }

                            public void myCall(A a) {
                                List.of("1", "2", "3").stream()
                                        .forEach(a::save);
                            }
                        }
                        """, pagingAndSortingRepository, crudRepository, repositoryPackage)
        );

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAfter().printAll())
                .isEqualTo(
                        replacePagingRepoAndCrudRepo("""
                        package test;
                        import java.util.List;
                        
                        import -repositoryPackage-.-crudRepository-;
                        import -repositoryPackage-.-pagingRepository-;
                        
                        class Hello {
                            public interface A extends -pagingRepository-<String, Long>, -crudRepository-<String, Long> {
                            }

                            public void myCall(A a) {
                                List.of("1", "2", "3").stream()
                                        .forEach(a::save);
                            }
                        }
                        """, pagingAndSortingRepository, crudRepository, repositoryPackage)
                );
    }

    private String replacePagingRepoAndCrudRepo(String template, String pagingRepo, String crudRepo, String repositoryPackage) {

        return template
                .replaceAll("-pagingRepository-", pagingRepo)
                .replaceAll("-crudRepository-", crudRepo)
                .replaceAll("-repositoryPackage-", repositoryPackage);
    }

    private static Stream<Arguments> repositoryTestArguments() {
        return Stream.of(
                Arguments.of(crudRepoExtensionRecipe, "PagingAndSortingRepository", "CrudRepository", "org.springframework.data.repository"),
                Arguments.of(reactiveCrudExtensionRecipe, "ReactiveSortingRepository", "ReactiveCrudRepository", "org.springframework.data.repository.reactive"),
                Arguments.of(rxJavaCrudExtensionRecipe, "RxJava3SortingRepository", "RxJava3CrudRepository", "org.springframework.data.repository.reactive")
        );
    }
}
