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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openrewrite.Recipe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CrudRepositoryExtensionTest {

    private JavaTestHelper javaTestHelper = new JavaTestHelper();
    private Recipe crudRepoExtensionRecipe = new CrudRepositoryExtension(
            "org.springframework.data.repository.PagingAndSortingRepository",
            "org.springframework.data.repository.CrudRepository"
    );

    private Recipe reactiveCrudExtensionRecipe = new CrudRepositoryExtension(
            "org.springframework.data.repository.ReactiveSortingRepository",
            "org.springframework.data.repository.ReactiveCrudRepository"
    );

    private Map<String, Recipe> recipeMap = new HashMap<>();

    @BeforeEach
    public void setup() {

        recipeMap.put("crudRepo", crudRepoExtensionRecipe);
        recipeMap.put("reactiveRepo", reactiveCrudExtensionRecipe);
    }

    @ParameterizedTest
    @CsvSource({
            "crudRepo,PagingAndSortingRepository,CrudRepository",
            "reactiveRepo,ReactiveSortingRepository,ReactiveCrudRepository",
    })
    public void shouldAddCrudRepository(String recipe, String pagingAndSortingRepository, String crudRepository) {

        javaTestHelper.runAndVerify(
                recipeMap.get(recipe),
                List.of(replacePagingRepoAndCrudRepo("""
                                package org.springframework.data.repository;
                                public interface -pagingRepository-<T, ID> {
                                }
                                """, pagingAndSortingRepository, crudRepository),
                        replacePagingRepoAndCrudRepo("""
                                package org.springframework.data.repository;
                                public interface -crudRepository-<T, ID> {
                                }
                                """, pagingAndSortingRepository, crudRepository)
                ),
                replacePagingRepoAndCrudRepo("""
                        package test;
                        import org.springframework.data.repository.-pagingRepository-;
                        public interface A extends -pagingRepository-<String, Long> {
                        }
                        """, pagingAndSortingRepository, crudRepository),
                replacePagingRepoAndCrudRepo("""
                        package test;
                        import org.springframework.data.repository.-crudRepository-;
                        import org.springframework.data.repository.-pagingRepository-;
                                                        
                        public interface A extends -pagingRepository-<String, Long>, -crudRepository-<String, Long> {
                        }
                        """, pagingAndSortingRepository, crudRepository
                )
        );
    }

    @ParameterizedTest
    @CsvSource({
            "crudRepo,PagingAndSortingRepository,CrudRepository",
            "reactiveRepo,ReactiveSortingRepository,ReactiveCrudRepository",
    })
    public void canDoQuestionMark(String recipe, String pagingAndSortingRepository, String crudRepository) {

        javaTestHelper.runAndVerify(recipeMap.get(recipe),
                List.of(replacePagingRepoAndCrudRepo("""
                                package org.springframework.data.repository;
                                public interface -pagingRepository-<T, ID> {
                                }
                                """, pagingAndSortingRepository, crudRepository),
                        replacePagingRepoAndCrudRepo("""
                                package org.springframework.data.repository;
                                public interface -crudRepository-<T, ID> {
                                }
                                """, pagingAndSortingRepository, crudRepository),
                        replacePagingRepoAndCrudRepo("""
                                package test;
                                public interface Payment<T> {
                                    T hello();
                                }
                                """, pagingAndSortingRepository, crudRepository)),
                replacePagingRepoAndCrudRepo("""
                        package test;
                        import org.springframework.data.repository.-pagingRepository-;
                        public interface A extends -pagingRepository-<Payment<?>, Long> {
                        }
                        """, pagingAndSortingRepository, crudRepository),
                replacePagingRepoAndCrudRepo("""
                        package test;
                        import org.springframework.data.repository.-crudRepository-;
                        import org.springframework.data.repository.-pagingRepository-;
                                        
                        public interface A extends -pagingRepository-<Payment<?>, Long>, -crudRepository-<Payment<?>, Long> {
                        }
                        """, pagingAndSortingRepository, crudRepository)
        );
    }

    @Test
    public void onlyExtendCrudRepoIfInterfaceHasPagingAndSortingRepository() {
        javaTestHelper.runAndVerifyNoChanges(crudRepoExtensionRecipe,
                List.of("""
                                package org.springframework.data.repository;
                                public interface HelloWorld<T, ID> {
                                }
                                """,
                        """
                                package org.springframework.data.repository;
                                public interface CrudRepository<T, ID> {
                                }
                                """,
                        """
                                package test;
                                public interface Payment<T> {
                                    T hello();
                                }
                                """),
                """
                        package test;
                        import org.springframework.data.repository.HelloWorld;
                        public interface A extends HelloWorld<Payment<?>, Long> {
                        }
                        """
        );
    }

    @Test
    public void whenThereAreNoParametersWhilstExtending() {

        javaTestHelper.runAndVerify(
                crudRepoExtensionRecipe,
                List.of("""
                                package org.springframework.data.repository;
                                public interface PagingAndSortingRepository<T, ID> {
                                }
                                """,
                        """
                                package org.springframework.data.repository;
                                public interface CrudRepository<T, ID> {
                                }
                                """),
                """
                        package test;
                        import org.springframework.data.repository.PagingAndSortingRepository;
                        public interface A extends PagingAndSortingRepository {
                        }
                        """,
                """
                        package test;
                        import org.springframework.data.repository.CrudRepository;
                        import org.springframework.data.repository.PagingAndSortingRepository;
                                        
                        public interface A extends PagingAndSortingRepository, CrudRepository {
                        }
                        """
        );
    }

    @Test
    public void multipleExtends() {
        javaTestHelper.runAndVerify(
                crudRepoExtensionRecipe,
                List.of("""
                                package org.springframework.data.repository;
                                public interface PagingAndSortingRepository<T, ID> {
                                }
                                """,
                        """
                                package org.springframework.data.repository;
                                public interface Hello<T, ID> {
                                }
                                """,
                        """
                                package org.springframework.data.repository;
                                public interface CrudRepository<T, ID> {
                                }
                                """),
                """
                        package test;
                        import org.springframework.data.repository.PagingAndSortingRepository;
                        import org.springframework.data.repository.Hello;
                        public interface A extends Hello<String, Long>, PagingAndSortingRepository {
                        }
                        """,
                """
                        package test;
                        import org.springframework.data.repository.PagingAndSortingRepository;
                        import org.springframework.data.repository.CrudRepository;
                        import org.springframework.data.repository.Hello;
                                        
                        public interface A extends Hello<String, Long>, PagingAndSortingRepository, CrudRepository {
                        }
                        """
        );
    }

    @Test
    public void classImplementsPagingRepository() {
        javaTestHelper.runAndVerify(
                crudRepoExtensionRecipe,
                List.of("""
                                package org.springframework.data.repository;
                                public interface PagingAndSortingRepository<T, ID> {
                                }
                                """,
                        """
                                package org.springframework.data.repository;
                                public interface Hello<T, ID> {
                                }
                                """,
                        """
                                package org.springframework.data.repository;
                                public interface CrudRepository<T, ID> {
                                }
                                """),
                """
                        package test;
                        import org.springframework.data.repository.PagingAndSortingRepository;
                        import org.springframework.data.repository.Hello;
                        public class A implements Hello<String, Long>, PagingAndSortingRepository<String, Long> {
                        }
                        """,
                """
                        package test;
                        import org.springframework.data.repository.PagingAndSortingRepository;
                        import org.springframework.data.repository.CrudRepository;
                        import org.springframework.data.repository.Hello;
                                        
                        public class A implements Hello<String, Long>, PagingAndSortingRepository<String, Long>, CrudRepository<String, Long> {
                        }
                        """
        );
    }

    @Test
    public void shouldAddReactiveCrudRepository() {

        javaTestHelper.runAndVerify(
                reactiveCrudExtensionRecipe,
                List.of("""
                                package org.springframework.data.repository;
                                public interface ReactiveSortingRepository<T, ID> {
                                }
                                """,
                        """
                                package org.springframework.data.repository;
                                public interface ReactiveCrudRepository<T, ID> {
                                }
                                """),
                """
                        package test;
                        import org.springframework.data.repository.ReactiveSortingRepository;
                        public interface A extends ReactiveSortingRepository<String, Long> {
                        }
                        """,
                """
                        package test;
                        import org.springframework.data.repository.ReactiveCrudRepository;
                        import org.springframework.data.repository.ReactiveSortingRepository;
                                        
                        public interface A extends ReactiveSortingRepository<String, Long>, ReactiveCrudRepository<String, Long> {
                        }
                        """
        );
    }

    private String replacePagingRepoAndCrudRepo(String template, String pagingRepo, String crudRepo) {

        return template
                .replaceAll("-pagingRepository-", pagingRepo)
                .replaceAll("-crudRepository-", crudRepo);
    }

}
