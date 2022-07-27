package org.springframework.sbm.boot.upgrade_27_30;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openrewrite.Recipe;

import java.util.List;

public class CrudRepositoryWithCallsCheckTest {
    private final JavaTestHelper javaTestHelper = new JavaTestHelper();
    private static final Recipe crudRepoExtensionRecipe = new CrudRepositoryExtension(
            "org.springframework.data.repository.PagingAndSortingRepository",
            "org.springframework.data.repository.CrudRepository"
    );

    private static final Recipe testRecipe = new ConditionTestRecipe();

    @Test
    void shouldNotAddCrudRepositoryWithoutCall() {
        javaTestHelper.runRecipe(
                testRecipe,
                List.of("""
                                package org.springframework.data.repository;
                                public interface CrudRepository<T, ID> {
                                }
                                """,
                        """
                                package org.springframework.data.repository;
                                public interface PagingAndSortingRepository<T, ID> {
                                    void save(String entity);
                                }
                                """
                ),
                """
                        package test;
                        import org.springframework.data.repository.PagingAndSortingRepository;                        
                        class Hello {
                            public interface A extends PagingAndSortingRepository<String, Long> {
                                void test(String p);
                            }

                            public void myCall(A a) {
                                a.save("");
                        
                                String myString = "MyString";
                                int k = myString.length();
                                
                                Integer myInt = Integer.parseInt("0");
                            }
                            
                        }
                        """,
//                """
//                        package test;
//                        import org.springframework.data.repository.PagingAndSortingRepository;
//                        public interface A extends PagingAndSortingRepository<String, Long> {
//                        }
//
//                        class Hello {
//                            public void myCall() {
//                                A a = null;
//                                a.save("");
//                            }
//                        }
//                        """,
                """
                        package test;
                        import org.springframework.data.repository.PagingAndSortingRepository;
                        public interface A extends PagingAndSortingRepository<String, Long>, CrudRepository<String, Long> {
                        }
                        
                        class Hello {
                            public void myCall() {
                                A a = null;
                                a.save("");        
                            }
                        }
                        """
        );
    }

}
