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

package org.openrewrite.java;

import org.junit.jupiter.api.Test;
import org.openrewrite.*;
import org.openrewrite.java.tree.*;
import org.springframework.sbm.java.OpenRewriteTestSupport;

import java.lang.reflect.ParameterizedType;
import java.nio.file.Path;
import java.util.List;

public class InheritanceLearningTest {

    @Test
    void test() {
        String entity =
                "public class Payment<T> {}";

        String repository =
                "import org.springframework.data.repository.PagingAndSortingRepository;\n" +
                "interface Payments extends PagingAndSortingRepository<Payment<?>, Long> {\n" +
                "}";

        List<Path> classpathFiles = OpenRewriteTestSupport.getClasspathFiles("org.springframework.boot:spring-boot-starter-data-jpa:2.7.1"); // 3.0.0-M4

        JavaParser javaParser = JavaParser.fromJavaVersion()
                .classpath(classpathFiles)
                .build();
        List<J.CompilationUnit> compilationUnits = javaParser.parse(entity, repository);

        J.CompilationUnit cu = compilationUnits.get(1);

//        // find interface extending PagingAndSortingRepository
//        compilationUnits.stream()
//        .flatMap(compilationUnit -> {
//            return compilationUnit.getClasses()
//                .stream()
//                .filter(c -> {
//                    if(c.getImplements() == null) return false;
//                    return c.getImplements().stream()
//                        .map(i -> i.getType())
//                        .filter(JavaType.class::isInstance)
//                        .map(JavaType.class::cast)
//                        .filter(JavaType.FullyQualified.class::isInstance)
//                        .map(JavaType.FullyQualified.class::cast)
//                        .anyMatch(jt -> "org.springframework.data.repository.PagingAndSortingRepository".equals(jt.getFullyQualifiedName()));
//                })
//                .map(c -> {
//                    return new CompilationUnitAndClass(compilationUnit, c);
//                });
//        })
//        .forEach(c -> this.extendsInterface(c));


        Recipe recipe = new Recipe() {

            @Override
            public String getDisplayName() {
                return "";
            }

            @Override
            protected TreeVisitor<?, ExecutionContext> getVisitor() {
                return new JavaIsoVisitor<ExecutionContext>() {
                    @Override
                    public J.ClassDeclaration visitClassDeclaration(J.ClassDeclaration classDecl, ExecutionContext executionContext) {
                        J.ClassDeclaration classDeclaration = super.visitClassDeclaration(classDecl, executionContext);

                        if (isMatch(classDeclaration)) {
                            return addCrudRepositoryToInheritanceHierarchy(classDeclaration);
                        }

                        return classDeclaration;
                    }

                    private J.ClassDeclaration addCrudRepositoryToInheritanceHierarchy(J.ClassDeclaration classDeclaration) {
                        List<Expression> typeParameters = ((J.ParameterizedType) cu.getClasses().get(0).getImplements().get(0)).getTypeParameters();


                        String typeName = String.format("org.springframework.data.repository.CrudRepository", typeParameters.get(0).getType().toString(), typeParameters.get(1).getType().toString());
                        JavaType javaType = JavaType.buildType(typeName);
                        boolean ofClassType = TypeUtils.isOfClassType(javaType, "org.springframework.data.repository.CrudRepository");
                        List<JavaType> parameters = List.of(typeParameters.get(0).getType(), typeParameters.get(1).getType());
                        JavaType.Parameterized parameterized = TypeUtils.asParameterized(javaType);
                        JavaType.Parameterized interfaceType = parameterized.withTypeParameters(parameters);
                        ImplementInterface<ExecutionContext> implementInterface = new ImplementInterface<ExecutionContext>(classDeclaration, interfaceType);
                        J.CompilationUnit value = getCursor().dropParentUntil((o) -> o.getClass().isAssignableFrom(J.CompilationUnit.class)).getValue();
                        return (J.ClassDeclaration) implementInterface.visitClassDeclaration(classDeclaration, new InMemoryExecutionContext());
                    }

                    private boolean isMatch(J.ClassDeclaration classDeclaration) {
                        return classDeclaration.getImplements() != null && classDeclaration.getImplements().stream()
                                .map(i -> i.getType())
                                .filter(JavaType.class::isInstance)
                                .map(JavaType.class::cast)
                                .filter(JavaType.FullyQualified.class::isInstance)
                                .map(JavaType.FullyQualified.class::cast)
                                .anyMatch(jt -> "org.springframework.data.repository.PagingAndSortingRepository".equals(jt.getFullyQualifiedName()));
                    }
                };
            }
        };

        List<Result> run = recipe.run(compilationUnits);
        run.stream().map(Result::getAfter).map(J.CompilationUnit.class::cast).forEach(J.CompilationUnit::printAll);


        //List<J.TypeParameter> typeParameters = cu.getClasses().get(0).getImplements().get(0).getTypeParameters();
/*
        List<Expression> typeParameters = ((J.ParameterizedType) cu.getClasses().get(0).getImplements().get(0)).getTypeParameters();
        J.ClassDeclaration classDeclaration = cu.getClasses().get(0);
        ArrayList<TypeTree> list = new ArrayList<>(classDeclaration.getImplements());
        String typeName = String.format("org.springframework.data.repository.CrudRepository", typeParameters.get(0).getType().toString(), typeParameters.get(1).getType().toString());
        JavaType javaType = JavaType.buildType(typeName);
        boolean ofClassType = TypeUtils.isOfClassType(javaType, "org.springframework.data.repository.CrudRepository");
        List<JavaType> parameters = List.of(typeParameters.get(0).getType(), typeParameters.get(1).getType());
        JavaType.Parameterized parameterized = TypeUtils.asParameterized(javaType);
        JavaType.Parameterized interfaceType = parameterized.withTypeParameters(parameters);
        ImplementInterface<ExecutionContext> implementInterface = new ImplementInterface<ExecutionContext>(classDeclaration, interfaceType);
        cu = (J.CompilationUnit) implementInterface.visit(cu, new InMemoryExecutionContext());
*/
        System.out.println(cu.printAll());
    }

    private J.CompilationUnit extendsInterface(CompilationUnitAndClass cd) {
        List<Expression> typeParameters = ((J.ParameterizedType) cd.getClassDeclaration().getImplements().get(0)).getTypeParameters();

        String typeName = String.format("org.springframework.data.repository.CrudRepository", typeParameters.get(0).getType().toString(), typeParameters.get(1).getType().toString());
        JavaType javaType = JavaType.buildType(typeName);
        List<JavaType> parameters = List.of(typeParameters.get(0).getType(), typeParameters.get(1).getType());
        JavaType.Parameterized parameterized = TypeUtils.asParameterized(javaType);
        JavaType.Parameterized interfaceType = parameterized.withTypeParameters(parameters);

        ImplementInterface<ExecutionContext> implementInterface = new ImplementInterface<ExecutionContext>(cd.getClassDeclaration(), interfaceType);
        J.CompilationUnit cu = (J.CompilationUnit) implementInterface.visit(cd.getCompilationUnit(), new InMemoryExecutionContext());
        return cu;
    }
}
