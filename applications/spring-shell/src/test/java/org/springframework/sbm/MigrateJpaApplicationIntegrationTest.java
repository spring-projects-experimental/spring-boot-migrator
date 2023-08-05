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
package org.springframework.sbm;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MigrateJpaApplicationIntegrationTest extends IntegrationTestBaseClass {

    @Override
    protected String getTestSubDir() {
        return "jpa-hibernate";
    }

    @Test
    @Tag("integration")
    void migrateJpaApplication() {
        intializeTestProject();
        executeMavenGoals(getTestDir(), "clean", "package");
        scanProject();
        assertApplicableRecipesContain(
                "initialize-spring-boot-migration",
                "migrate-stateless-ejb",
                "migrate-jpa-to-spring-boot"
        );
        applyRecipe(
                "initialize-spring-boot-migration",
                "migrate-stateless-ejb",
                "migrate-jpa-to-spring-boot"
        );

        assertThat(super.loadJavaFile("org.superbiz.injection.h3jpa", "SpringBootApp")).isNotEmpty();
        assertThat(super.loadTestJavaFile("org.superbiz.injection.h3jpa", "SpringBootAppTest")).isNotEmpty();
        String movies = loadJavaFile("org.superbiz.injection.h3jpa", "Movies");
        assertThat(movies).contains(
                """
                package org.superbiz.injection.h3jpa;
                 
                import org.springframework.stereotype.Service;
                import org.springframework.transaction.annotation.Transactional;
                
                import javax.persistence.EntityManager;
                import javax.persistence.PersistenceContext;
                import javax.persistence.Query;
                import java.util.List;
                
                @Service
                @Transactional
                public class Movies {
                
                    @PersistenceContext(unitName = "default")
                    private EntityManager entityManager;
                
                    public void addMovie(Movie movie) throws Exception {
                        entityManager.persist(movie);
                    }
                
                    public void deleteMovie(Movie movie) throws Exception {
                        movie = entityManager.find(Movie.class, movie.getId());
                        entityManager.remove(movie);
                    }
                
                    public List<Movie> getMovies() throws Exception {
                        Query query = entityManager.createQuery("SELECT m from Movie as m");
                        return query.getResultList();
                    }
                 
                }
                """
        );

    }

}
