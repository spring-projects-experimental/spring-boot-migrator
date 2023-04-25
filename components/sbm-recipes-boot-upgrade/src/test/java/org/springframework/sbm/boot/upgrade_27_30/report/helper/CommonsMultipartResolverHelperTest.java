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

public class CommonsMultipartResolverHelperTest {

    @Test
    public void detectsCommonsMultiPartResolverClass() {
        @Language("java")
        String commonsMultiPartClass = """
                package example;
                
                 import org.springframework.context.annotation.Bean;
                 import org.springframework.web.multipart.commons.CommonsMultipartResolver;
                 
                 public class SongUploadConfig {
                 
                     @Bean
                     public CommonsMultipartResolver commonsMultipartResolver() {
                         final CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
                         commonsMultipartResolver.setMaxUploadSize(-1);
                         return commonsMultipartResolver;
                     }
                 }
                """;

        ProjectContext context = TestProjectContext.buildProjectContext()
                .addJavaSource("src/main/java", commonsMultiPartClass)
                .withBuildFileHavingDependencies("org.springframework.boot:spring-boot-starter-web:2.7.1")
                .build();

        SpringBootUpgradeReportTestSupport
                .generatedSection("Commons Multipart Upload")
                .fromProjectContext(context)
                .shouldRenderAs(
                        """
                         === Commons Multipart Upload
                                                         
                         ==== What Changed
                         Support for Spring Framework's `CommonsMultipartResolver` has been removed following its removal in Spring Framework 6
                                                         
                         ==== Why is the application affected
                         We found usage of `CommonsMultipartResolver` in following files:
                         
                         * `example.SongUploadConfig`
                         
                         ==== Remediation
                         Remove beans of type `CommonsMultipartResolver` and rely on Spring Boot auto-configuration
                                  
                                            
                         """
                );
    }

    @Test
    public void detectsCommonMultiPartResolverInMultipleFiles() {
        @Language("java")
        String commonsMultiPartClass1 = """
                package example;
                
                 import org.springframework.context.annotation.Bean;
                 import org.springframework.web.multipart.commons.CommonsMultipartResolver;
                 
                 public class SongUploadConfig {
                 
                     @Bean
                     public CommonsMultipartResolver songMultiPartUploader() {
                         final CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
                         commonsMultipartResolver.setMaxUploadSize(10);
                         return commonsMultipartResolver;
                     }
                 }
                """;

        String commonsMultiPartClass2 = """
                package example;
                
                 import org.springframework.context.annotation.Bean;
                 import org.springframework.web.multipart.commons.CommonsMultipartResolver;
                 
                 public class VideoUploadConfig {
                 
                     @Bean
                     public CommonsMultipartResolver songMultiPartUploader() {
                         final CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
                         commonsMultipartResolver.setMaxUploadSize(10);
                         return commonsMultipartResolver;
                     }
                 }
                """;
        ProjectContext context = TestProjectContext.buildProjectContext()
                .addJavaSource("src/main/java", commonsMultiPartClass1)
                .addJavaSource("src/main/java", commonsMultiPartClass2)
                .withBuildFileHavingDependencies("org.springframework.boot:spring-boot-starter-web:2.7.1")
                .build();

        SpringBootUpgradeReportTestSupport
                .generatedSection("Commons Multipart Upload")
                .fromProjectContext(context)
                .shouldRenderAs(
                        """
                         === Commons Multipart Upload
                                                         
                         ==== What Changed
                         Support for Spring Framework's `CommonsMultipartResolver` has been removed following its removal in Spring Framework 6
                                                         
                         ==== Why is the application affected
                         We found usage of `CommonsMultipartResolver` in following files:
                         
                         * `example.SongUploadConfig`
                         * `example.VideoUploadConfig`
                         
                         ==== Remediation
                         Remove beans of type `CommonsMultipartResolver` and rely on Spring Boot auto-configuration
                                  
                                            
                         """
                );
    }

    @Test
    public void doesNotReportMigrationGuidanceWhenNoCommonsMultiPartResolverIsFound() {
        @Language("java")
        String aClass = """
                package example;
                
                public class A {}
                """;

        ProjectContext context = TestProjectContext.buildProjectContext()
                .addJavaSource("src/main/java", aClass)
                .withBuildFileHavingDependencies("org.springframework.boot:spring-boot-starter-web:2.7.1")
                .build();

        SpringBootUpgradeReportTestSupport
                .generatedSection("Commons Multipart Upload")
                .fromProjectContext(context)
                .shouldNotRender();
    }
}
