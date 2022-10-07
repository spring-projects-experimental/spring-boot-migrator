package org.springframework.sbm.boot.upgrade_27_30.checks;


import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.java.api.JavaSourceAndType;
import org.springframework.sbm.java.api.Type;
import org.springframework.sbm.project.resource.TestProjectContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ApacheSolrRepositoryBeanFinderTest {

    @Language("java")
    private static String SOLR_REPO =
            """
            package foo.bar;
            import org.springframework.data.solr.repository.SolrCrudRepository;
            public class ProductRepository implements SolrCrudRepository<Product, String> {}
            """;

    @Language("java")
    private static String NO_SOLR_REPO =
            """
            package foo.bar;
            public class ProductRepository {}
            """;

    @Test
    public void givenModuleWithSolrRepository_find_expectNonEmptyList(){
        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                                                            .withJavaSources(SOLR_REPO)
                                                            .withBuildFileHavingDependencies("org.springframework.data:spring-data-solr:4.3.15")
                                                            .build();

        ApacheSolrRepositoryBeanFinder solrRepositoryBeanFinder = new ApacheSolrRepositoryBeanFinder();
        List<JavaSourceAndType> solrRepositories = projectContext.search(solrRepositoryBeanFinder);
        assertThat(solrRepositories).isNotEmpty();
        Type type = solrRepositories.get(0).getType();
        assertThat(type.getFullyQualifiedName()).isEqualTo("foo.bar.ProductRepository");
    }

    @Test
    public void givenModuleWithoutSolrRepository_find_expectNonEmptyList(){
        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                                                            .withJavaSources(NO_SOLR_REPO)
                                                            .withBuildFileHavingDependencies("org.springframework.data:spring-data-solr:4.3.15")
                                                            .build();

        ApacheSolrRepositoryBeanFinder solrRepositoryBeanFinder = new ApacheSolrRepositoryBeanFinder();
        List<JavaSourceAndType> solrRepositories = projectContext.search(solrRepositoryBeanFinder);
        assertThat(solrRepositories).isEmpty();
    }
}
