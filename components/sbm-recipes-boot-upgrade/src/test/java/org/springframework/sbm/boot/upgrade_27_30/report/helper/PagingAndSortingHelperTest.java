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

package org.springframework.sbm.boot.upgrade_27_30.report.helper;

import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;
import org.springframework.sbm.boot.upgrade_27_30.report.SpringBootUpgradeReportTestSupport;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;

public class PagingAndSortingHelperTest {
    @Test
    public void reportMigrationGuidanceWhenPagingAndSortingRepositoryIsFound() {
        @Language("java")
        String javaClassWithPagingAndSortingRepository = """
                package example;
                 
                 import org.springframework.data.repository.PagingAndSortingRepository;
                 
                 import java.util.List;
                 
                 public interface SongStatRepository extends PagingAndSortingRepository<SongStat, String> {
                     List<String> findTop10SongsByRegionOrderByTimesPlayedDesc(String region);
                 }
                """;

        @Language("java")
        String javaClassWithoutPagingAndSortingRepo = """
        
        package example;
        public class A {}
        """;

        ProjectContext context = TestProjectContext.buildProjectContext()
                .addJavaSource("src/main/java", javaClassWithPagingAndSortingRepository)
                .addJavaSource("src/main/java",javaClassWithoutPagingAndSortingRepo)
                .withBuildFileHavingDependencies("org.springframework.data:spring-data-commons:2.7.1")
                .build();

        SpringBootUpgradeReportTestSupport
                .generatedSection("Paging and sorting repository")
                .fromProjectContext(context)
                .shouldRenderAs(
                        """
                                === Paging and sorting repository
                                Issue: https://github.com/spring-projects-experimental/spring-boot-migrator/issues/518[#518], Contributors: https://github.com/sanagaraj-pivotal[@sanagaraj-pivotal^, role="ext-link"]

                                ==== What Changed
                                Sorting repositories no longer extend their respective CRUD repository.

                                The affected interfaces are:
                                
                                * `PagingAndSortingRepository` no longer extends `CrudRepository`
                                * `ReactiveSortingRepository` no longer extends `ReactiveCrudRepository`
                                * `RxJavaSortingRepository` no longer extends `RxJavaCrudRepository`
                                          
                                ==== Why is the application affected
                                We found classes which uses `PagingAndSortingRepository` in following files:
                                
                                * <PATH>/src/main/java/example/SongStatRepository.java
                                                  
                                                                
                                ==== Remediation
                                If one requires the old behavior one must extend not only the sorting repository, but also the respective CRUD repository explicitly. This was done so the sorting support could easily be combined with the List repositories introduced above.
                                
                                
                                    """
                );
    }

    @Test
    public void reportMigrationGuidanceWhenReactiveSortingRepositoryIsFound() {
        @Language("java")
        String javaClassWithReactiveSortingRepo = """
                package example;
                 
                 import org.springframework.data.repository.reactive.ReactiveSortingRepository;
                 
                 import java.util.List;
                 
                 public interface SongStatRepository extends ReactiveSortingRepository<SongStat, String> {
                     List<String> findTop10SongsByRegionOrderByTimesPlayedDesc(String region);
                 }
                """;

        @Language("java")
        String javaClassWithoutReactiveSortingRepo = """
        
        package example;
        public class A {}
        """;

        ProjectContext context = TestProjectContext.buildProjectContext()
                .addJavaSource("src/main/java", javaClassWithReactiveSortingRepo)
                .addJavaSource("src/main/java",javaClassWithoutReactiveSortingRepo)
                .withBuildFileHavingDependencies("org.springframework.data:spring-data-commons:2.7.1")
                .withBuildFileHavingDependencies("io.projectreactor:reactor-core:3.4.19")
                .build();

        SpringBootUpgradeReportTestSupport
                .generatedSection("Paging and sorting repository")
                .fromProjectContext(context)
                .shouldRenderAs(
                        """
                                === Paging and sorting repository
                                Issue: https://github.com/spring-projects-experimental/spring-boot-migrator/issues/518[#518], Contributors: https://github.com/sanagaraj-pivotal[@sanagaraj-pivotal^, role="ext-link"]
                                                                
                                ==== What Changed
                                Sorting repositories no longer extend their respective CRUD repository.
                                                                
                                The affected interfaces are:
                                                                
                                * `PagingAndSortingRepository` no longer extends `CrudRepository`
                                * `ReactiveSortingRepository` no longer extends `ReactiveCrudRepository`
                                * `RxJavaSortingRepository` no longer extends `RxJavaCrudRepository`
                                                                
                                ==== Why is the application affected
                                We found classes which uses `ReactiveSortingRepository` in following files:
                                                                
                                * <PATH>/src/main/java/example/SongStatRepository.java
                                                                
                                                                
                                ==== Remediation
                                If one requires the old behavior one must extend not only the sorting repository, but also the respective CRUD repository explicitly. This was done so the sorting support could easily be combined with the List repositories introduced above.
                                          
                                                                
                                """
                );
    }

    @Test
    public void reportMigrationGuidanceWhenRxJavaSortingRepositoryIsFound() {
        @Language("java")
        String javaClassWithReactiveSortingRepo = """
                package example;
                 
                 import org.springframework.data.repository.reactive.RxJava3SortingRepository;
                 
                 import java.util.List;
                 
                 public interface SongStatRepository extends RxJava3SortingRepository<SongStat, String> {
                     List<String> findTop10SongsByRegionOrderByTimesPlayedDesc(String region);
                 }
                """;

        @Language("java")
        String javaClassWithoutReactiveSortingRepo = """
        
        package example;
        public class A {}
        """;

        ProjectContext context = TestProjectContext.buildProjectContext()
                .addJavaSource("src/main/java", javaClassWithReactiveSortingRepo)
                .addJavaSource("src/main/java",javaClassWithoutReactiveSortingRepo)
                .withBuildFileHavingDependencies("org.springframework.data:spring-data-commons:2.7.1")
                .withBuildFileHavingDependencies("io.reactivex.rxjava3:rxjava:3.1.5")
                .build();

        SpringBootUpgradeReportTestSupport
                .generatedSection("Paging and sorting repository")
                .fromProjectContext(context)
                .shouldRenderAs(
                        """
                                === Paging and sorting repository
                                Issue: https://github.com/spring-projects-experimental/spring-boot-migrator/issues/518[#518], Contributors: https://github.com/sanagaraj-pivotal[@sanagaraj-pivotal^, role="ext-link"]

                                ==== What Changed
                                Sorting repositories no longer extend their respective CRUD repository.

                                The affected interfaces are:
                                
                                * `PagingAndSortingRepository` no longer extends `CrudRepository`
                                * `ReactiveSortingRepository` no longer extends `ReactiveCrudRepository`
                                * `RxJavaSortingRepository` no longer extends `RxJavaCrudRepository`
                                          
                                ==== Why is the application affected
                                We found classes which uses `RxJavaSortingRepository` in following files:
                                
                                * <PATH>/src/main/java/example/SongStatRepository.java
                                                    
                                                                
                                ==== Remediation
                                If one requires the old behavior one must extend not only the sorting repository, but also the respective CRUD repository explicitly. This was done so the sorting support could easily be combined with the List repositories introduced above.
                                
                                
                                    """
                );
    }

    @Test
    public void reportMigrationGuidanceWhenThereIsAMixOfDifferentRepoIsFound() {
        @Language("java")
        String javaClassWithPagingAndSortingRepository = """
                package example;
                 
                 import org.springframework.data.repository.PagingAndSortingRepository;
                 
                 import java.util.List;
                 
                 public interface SongStatRepositoryPagingAndSorting extends PagingAndSortingRepository<SongStat, String> {
                     List<String> findTop10SongsByRegionOrderByTimesPlayedDesc(String region);
                 }
                """;
        @Language("java")
        String javaClassWithReactiveSortingRepo = """
                package example;
                 
                 import org.springframework.data.repository.reactive.ReactiveSortingRepository;
                 
                 import java.util.List;
                 
                 public interface SongStatRepositoryReactive extends ReactiveSortingRepository<SongStat, String> {
                     List<String> findTop10SongsByRegionOrderByTimesPlayedDesc(String region);
                 }
                """;
        @Language("java")
        String javaClassWithRXReactiveSortingRepo = """
                package example;
                 
                 import org.springframework.data.repository.reactive.RxJava3SortingRepository;
                 
                 import java.util.List;
                 
                 public interface SongStatRepositoryReactiveRx extends RxJava3SortingRepository<SongStat, String> {
                     List<String> findTop10SongsByRegionOrderByTimesPlayedDesc(String region);
                 }
                """;

        @Language("java")
        String javaClassWithoutPagingAndSortingRepo = """
        
        package example;
        public class A {}
        """;

        ProjectContext context = TestProjectContext.buildProjectContext()
                .addJavaSource("src/main/java", javaClassWithPagingAndSortingRepository)
                .addJavaSource("src/main/java",javaClassWithoutPagingAndSortingRepo)
                .addJavaSource("src/main/java",javaClassWithReactiveSortingRepo)
                .addJavaSource("src/main/java",javaClassWithRXReactiveSortingRepo)
                .withBuildFileHavingDependencies("org.springframework.data:spring-data-commons:2.7.1")
                .withBuildFileHavingDependencies("io.reactivex.rxjava3:rxjava:3.1.5")
                .withBuildFileHavingDependencies("io.projectreactor:reactor-core:3.4.19")
                .build();

        SpringBootUpgradeReportTestSupport
                .generatedSection("Paging and sorting repository")
                .fromProjectContext(context)
                .shouldRenderAs(
                        """
                                === Paging and sorting repository
                                Issue: https://github.com/spring-projects-experimental/spring-boot-migrator/issues/518[#518], Contributors: https://github.com/sanagaraj-pivotal[@sanagaraj-pivotal^, role="ext-link"]
                                                                
                                ==== What Changed
                                Sorting repositories no longer extend their respective CRUD repository.
                                                                
                                The affected interfaces are:
                                                                
                                * `PagingAndSortingRepository` no longer extends `CrudRepository`
                                * `ReactiveSortingRepository` no longer extends `ReactiveCrudRepository`
                                * `RxJavaSortingRepository` no longer extends `RxJavaCrudRepository`
                                                                
                                ==== Why is the application affected
                                We found classes which uses `PagingAndSortingRepository` in following files:
                                                                
                                * <PATH>/src/main/java/example/SongStatRepositoryPagingAndSorting.java
                                                                
                                We found classes which uses `ReactiveSortingRepository` in following files:
                                                                
                                * <PATH>/src/main/java/example/SongStatRepositoryReactive.java
                                                                
                                We found classes which uses `RxJavaSortingRepository` in following files:
                                                                
                                * <PATH>/src/main/java/example/SongStatRepositoryReactiveRx.java
                                                                
                                                                
                                ==== Remediation
                                If one requires the old behavior one must extend not only the sorting repository, but also the respective CRUD repository explicitly. This was done so the sorting support could easily be combined with the List repositories introduced above.
                                           
                                                                
                                """
                );
    }

    @Test
    public void doNotReportWhenNoMatchesAreFound() {

        @Language("java")
        String javaClassWithoutPagingAndSortingRepo = """
        
        package example;
        public class A {}
        """;

        ProjectContext context = TestProjectContext.buildProjectContext()
                .addJavaSource("src/main/java",javaClassWithoutPagingAndSortingRepo)
                .withBuildFileHavingDependencies("org.springframework.data:spring-data-commons:2.7.1")
                .build();

        SpringBootUpgradeReportTestSupport
                .generatedSection("Paging and sorting repository")
                .fromProjectContext(context)
                .shouldNotRender();
    }
}
