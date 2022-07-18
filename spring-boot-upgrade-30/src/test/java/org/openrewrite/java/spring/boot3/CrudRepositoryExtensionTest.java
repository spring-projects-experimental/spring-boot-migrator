package org.openrewrite.java.spring.boot3;

import org.junit.jupiter.api.Test;
import org.openrewrite.InMemoryExecutionContext;
import org.openrewrite.Result;
import org.openrewrite.java.JavaParser;
import org.openrewrite.java.tree.J;
import org.openrewrite.test.RewriteTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CrudRepositoryExtensionTest {

    @Test
    public void shouldAddCrudRepository() {

        InMemoryExecutionContext ctx = new InMemoryExecutionContext(Throwable::printStackTrace);
        JavaParser parser = JavaParser
                .fromJavaVersion()
                .dependsOn("""
                        package org.springframework.data.repository;
                        public interface PagingAndSortingRepository<T, ID> {
                        }
                        """,
                        """
                        package org.springframework.data.repository;
                        public interface CrudRepository<T, ID> {
                        }
                        """)
                .build();

        List<J.CompilationUnit> cu = parser.parse("""
                package test;
                import org.springframework.data.repository.PagingAndSortingRepository;
                public interface A extends PagingAndSortingRepository<String, Long> {
                }
                """);


        String recipeName = "org.boot3.Crud";

        List<Result> result = RewriteTest
                .fromRuntimeClasspath(recipeName)
                .run(cu, ctx);


        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAfter().printAll()).isEqualTo("""
                package test;
                import org.springframework.data.repository.PagingAndSortingRepository;
                import org.springframework.data.repository.CrudRepository;
                public interface A extends PagingAndSortingRepository<String, Long>, CrudRepository<String, Long> {
                }
                                """);
    }
}
