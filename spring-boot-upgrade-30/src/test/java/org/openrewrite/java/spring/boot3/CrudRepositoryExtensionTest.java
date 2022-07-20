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
package org.openrewrite.java.spring.boot3;

import org.junit.jupiter.api.Test;

import java.util.List;


public class CrudRepositoryExtensionTest {

    private JavaTestHelper javaTestHelper = new JavaTestHelper();
    private String recipeName = "org.boot3.Crud";

    @Test
    public void shouldAddCrudRepository() {

        javaTestHelper.runAndVerify(
                recipeName,
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
                public interface A extends PagingAndSortingRepository<String, Long> {
                }
                """,
                """
                package test;
                import org.springframework.data.repository.CrudRepository;
                import org.springframework.data.repository.PagingAndSortingRepository;
                
                public interface A extends PagingAndSortingRepository<String, Long>, CrudRepository<String, Long> {
                }
                """
        );
    }

    @Test
    public void canDoQuestionMark() {

        javaTestHelper.runAndVerify(recipeName,
                List.of("""
                        package org.springframework.data.repository;
                        public interface PagingAndSortingRepository<T, ID> {
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
                import org.springframework.data.repository.PagingAndSortingRepository;
                public interface A extends PagingAndSortingRepository<Payment<?>, Long> {
                }
                """,
                """
                package test;
                import org.springframework.data.repository.CrudRepository;
                import org.springframework.data.repository.PagingAndSortingRepository;
                
                public interface A extends PagingAndSortingRepository<Payment<?>, Long>, CrudRepository<Payment<?>, Long> {
                }
                """
                );
    }

    @Test
    public void onlyExtendCrudRepoIfInterfaceHasPagingAndSortingRepository() {
        javaTestHelper.runAndVerify(recipeName,
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
                recipeName,
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
                recipeName,
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
                recipeName,
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
}
