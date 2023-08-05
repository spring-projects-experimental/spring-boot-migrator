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
package org.springframework.sbm.boot.upgrade_24_25.report;

import org.springframework.core.annotation.Order;
import org.springframework.sbm.boot.UpgradeSectionBuilder;
import org.springframework.sbm.boot.asciidoctor.*;
import org.springframework.sbm.boot.common.finder.MethodPatternMatchingMethod;
import org.springframework.sbm.boot.upgrade_24_25.conditions.Boot_24_25_SpringDataJpaActionCondition;
import org.springframework.sbm.boot.upgrade_24_25.filter.SpringDataJpaAnalyzer;
import org.springframework.sbm.java.api.MethodCall;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.List;

@Component
@Order(25_005)
public class Boot_24_25_SpringDataJpa implements UpgradeSectionBuilder {


    @Override
    public boolean isApplicable(ProjectContext projectContext) {
        return new Boot_24_25_SpringDataJpaActionCondition().evaluate(projectContext);
    }

    @Override
    public Section build(ProjectContext projectContext) {

        // FIXME: accepts only single module projects
        if( ! projectContext.getApplicationModules().isSingleModuleApplication()) {
            throw new IllegalArgumentException("Only single module applications supported.");
        }

        SpringDataJpaAnalyzer springDataJpaAnalyzer = new SpringDataJpaAnalyzer();
        List<MethodCall> callsToGetOneMethod = springDataJpaAnalyzer.findCallsToGetOneMethod(projectContext);
        List<MethodPatternMatchingMethod> jpaRepositoriesWithGetByIdMethod = springDataJpaAnalyzer.getJpaRepositoriesWithGetByIdMethod(projectContext);


        Table.Builder builder = Table.builder()
                .headerCols("File", "Description", "Proposed change");


        jpaRepositoriesWithGetByIdMethod.forEach(m -> {
            Path relativePath = Path.of(".").toAbsolutePath().relativize(m.getJavaSource().getResource().getAbsolutePath());
            String description = String.format("defines `%s` returning `%s`", m.getMethodPattern(), m.getMethod().getReturnValue());
            String fix = String.format("Rename method to `get%sById()`", m.getMethod().getReturnValue());
            builder.row(relativePath.toString(), description, fix);
        });

        callsToGetOneMethod.forEach(mc -> {
            Path relativePath = Path.of(".").toAbsolutePath().relativize(mc.getJavaSource().getResource().getAbsolutePath());
            String description = String.format("calls `%s.%s`", mc.getMethodMatcher().getTargetTypePattern(), mc.getMethodMatcher().getMethodNamePattern());
            String fix = String.format("replace with call to `%s.getById()`", mc.getMethodMatcher().getTargetTypePattern());
            builder.row(relativePath.toString(), description, fix);
        });

        Table table = builder.build();

        return ChangeSection.RelevantChangeSection.builder()
                .title("Spring Data JPA")
                .paragraph(
                        "Spring Data JPA introduces a new getById method which replaces getOne. +\n" +
                        "If you find your application is now throwing a LazyLoadingException please rename any existing getById method to getXyzById (where xyz is an arbitrary string). +\n" +
                        "For more details, please read the https://docs.spring.io/spring-data/jpa/docs/2.6.0-RC1/reference/html/=new-features.2-5-0[updated Spring Data JPA reference documentation]."
                )
                .relevanceSection()
                .paragraph(
                        "The scan found calls to `JpaRepository.getOne(id)`. This method was deprecated and `JpaRepository.getById(id)` should be used instead. +\n" +
                        "The scan also found `getById(id)` methods implemented by query derivation in `JpaRepository` implementations. +\n" +
                        "Please see the table below for more information of what needs to be changed."
                )
                .table(table)
                .todoSection()
                .todoList(
                        TodoList.builder()
                                // FIXME: Create TODOs for all findings
                                .todo(
                                        TodoList.Todo.builder()
                                                .text("rename `" + jpaRepositoriesWithGetByIdMethod.get(0).getJavaSource().getTypes().get(0).getFullyQualifiedName() + "." + jpaRepositoriesWithGetByIdMethod.get(0).getMethodPattern() + "` to " + String.format("`get%sById()`", jpaRepositoriesWithGetByIdMethod.get(0).getMethod().getReturnValue()))
                                                .build()
                                )
                                .recipeName("boot-2.4-2.5-spring-data-jpa")
                                .build()
                )
                .build();

    }

}
