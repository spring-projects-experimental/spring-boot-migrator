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

    @Test
    void shouldNotAddCrudRepositoryWithoutCall() {
        javaTestHelper.runAndVerifyNoChanges(
                crudRepoExtensionRecipe,
                List.of("""
                                package org.springframework.data.repository;
                                public interface CrudRepository<T, ID> {
                                }
                                """,
                        """
                                package org.springframework.data.repository;
                                public interface PagingAndSortingRepository<T, ID> {
                                }
                                """,
                        """
                                package test;
                                
                                import org.springframework.beans.factory.annotation.Autowired;
                                import org.springframework.stereotype.Component;
                                
                                @Component
                                public class HelloComponent {
                                    @Autowired
                                    private A<String, Long> myRepo;
                                
                                    public void saveReactiveRepo() {
                                        //myRepo.findAll("");
                                    }
                                }
                                """
                ),
                """
                        package test;
                        import org.springframework.data.repository.PagingAndSortingRepository;
                        public interface A extends PagingAndSortingRepository<String, Long> {
                        }
                        """
        );
    }

}
