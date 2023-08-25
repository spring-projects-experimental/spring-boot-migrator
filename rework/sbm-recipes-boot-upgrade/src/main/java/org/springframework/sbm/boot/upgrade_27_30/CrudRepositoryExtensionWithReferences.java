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
package org.springframework.sbm.boot.upgrade_27_30;


import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.openrewrite.ExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.SourceFile;
import org.openrewrite.internal.ListUtils;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.JavaType;
import org.openrewrite.java.tree.MethodCall;
import org.openrewrite.java.tree.TypeUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Setter
public class CrudRepositoryExtensionWithReferences extends Recipe {

    @Override
    @NotNull
    public String getDisplayName() {
        return "Extends CrudRepository for Interfaces that extends PagingAndSortingRepository";
    }

    @Override
    public String getDescription() {
        return getDisplayName();
    }

    public CrudRepositoryExtensionWithReferences() {

    }

    public CrudRepositoryExtensionWithReferences(String pagingAndSortingRepository, String targetCrudRepository) {
        this.pagingAndSortingRepository = pagingAndSortingRepository;
        this.targetCrudRepository = targetCrudRepository;
    }

    private String pagingAndSortingRepository;
    private String targetCrudRepository;

    // FIXME: OR8.1 visit is not called anymore
    protected List<SourceFile> visit(List<SourceFile> allSourceFiles, ExecutionContext ctx) {

        Set<String> classesToAddCrudRepository = new HashSet<>();
        for (SourceFile source : allSourceFiles) {

            if (source instanceof J) {
                J cu = (J) source;

                new JavaIsoVisitor<Integer>() {

                    @Override
                    public J.MemberReference visitMemberReference(J.MemberReference memberRef, Integer integer) {

                        JavaType callingClassType = memberRef.getContaining().getType();
                        JavaType.FullyQualified fullyQualified = TypeUtils.asFullyQualified(callingClassType);

                        if ((fullyQualified != null)
                                && shouldApplyCrudExtension(callingClassType, memberRef)) {
                            classesToAddCrudRepository.add(fullyQualified.getFullyQualifiedName());
                        }

                        return super.visitMemberReference(memberRef, integer);
                    }

                    @Override
                    public J.MethodInvocation visitMethodInvocation(J.MethodInvocation method, Integer integer) {
                        if (method.getSelect() == null) {
                            return super.visitMethodInvocation(method, integer);
                        }

                        JavaType callingClassType = method.getSelect().getType();

                        if (shouldApplyCrudExtension(callingClassType, method)) {
                            JavaType.FullyQualified fullyQualified = TypeUtils.asFullyQualified(callingClassType);
                            if (fullyQualified != null) {
                                classesToAddCrudRepository.add(fullyQualified.getFullyQualifiedName());
                            }
                        }

                        return super.visitMethodInvocation(method, integer);
                    }

                    private boolean shouldApplyCrudExtension(JavaType callingClassType, MethodCall method) {
                        return TypeUtils.isAssignableTo(pagingAndSortingRepository, callingClassType)
                                && (method.getMethodType() == null ||
                                TypeUtils.isAssignableTo(targetCrudRepository, method.getMethodType().getDeclaringType()))
                                ;
                    }
                }.visit(cu, 0);
            }
        }

        return ListUtils.map(allSourceFiles, sourceFile -> (SourceFile) new JavaIsoVisitor<Integer>() {

            @Override
            public J.ClassDeclaration visitClassDeclaration(J.ClassDeclaration classDecl, Integer p) {
                JavaType.FullyQualified fullyQualified = TypeUtils.asFullyQualified(classDecl.getType());
                if (
                        TypeUtils.isAssignableTo(pagingAndSortingRepository, classDecl.getType())
                                && fullyQualified != null
                                && classesToAddCrudRepository.contains(fullyQualified.getFullyQualifiedName())
                ) {
                    Optional<JavaType.FullyQualified> pagingInterface = getExtendPagingAndSorting(classDecl);
                    if (pagingInterface.isEmpty()) {
                        return classDecl;
                    }
                    List<JavaType> typeParameters = pagingInterface.get().getTypeParameters();
                    doAfterVisit(new ImplementTypedInterface<>(classDecl, targetCrudRepository, typeParameters));

                    return classDecl;
                }

                return super.visitClassDeclaration(classDecl, p);
            }
        }.visit(sourceFile, 0));
    }

    private Optional<JavaType.FullyQualified> getExtendPagingAndSorting(J.ClassDeclaration classDecl) {
        if (classDecl.getType() == null) {
            return Optional.empty();
        }
        return classDecl.getType().getInterfaces().stream()
                .filter(impl -> impl.getFullyQualifiedName().equals(pagingAndSortingRepository))
                .findAny();
    }

    //    @Override
//    protected @Nullable TreeVisitor<?, ExecutionContext> getApplicableTest() {
//        return new JavaIsoVisitor<>() {
//            @Override
//            @NotNull
//            public J.ClassDeclaration visitClassDeclaration(@NotNull J.ClassDeclaration classDecl, @NotNull ExecutionContext executionContext) {
//                return doesItExtendPagingAndSorting(classDecl) ? applyThisRecipe(classDecl) : ceaseVisit(classDecl);
//            }
//
//            private boolean doesItExtendPagingAndSorting(J.ClassDeclaration classDecl) {
//                if (classDecl.getImplements() == null) {
//                    return false;
//                }
//                return classDecl.getType().getInterfaces().stream()
//                        .anyMatch(impl -> impl.getFullyQualifiedName().equals(pagingAndSortingRepository));
//            }
//
//            private J.ClassDeclaration ceaseVisit(J.ClassDeclaration classDecl) {
//                return classDecl;
//            }
//
//            @NotNull
//            private J.ClassDeclaration applyThisRecipe(J.ClassDeclaration classDecl) {
//                return classDecl.withMarkers(classDecl.getMarkers().searchResult());
//            }
//        };
//    }

//    @Override
//    @NotNull
//    protected JavaIsoVisitor<ExecutionContext> getVisitor() {
//        return new JavaIsoVisitor<>() {
//            @Override
//            @NotNull
//            public J.ClassDeclaration visitClassDeclaration(@NotNull J.ClassDeclaration classDecl, @NotNull ExecutionContext executionContext) {
//
//                Optional<JavaType.FullyQualified> pagingInterface = getExtendPagingAndSorting(classDecl);
//                if (pagingInterface.isEmpty()) {
//                    return classDecl;
//                }
//                List<JavaType> typeParameters = pagingInterface.get().getTypeParameters();
//                doAfterVisit(new ImplementTypedInterface<>(classDecl, targetCrudRepository, typeParameters));
//                return classDecl;
//            }
//
//            private Optional<JavaType.FullyQualified> getExtendPagingAndSorting(J.ClassDeclaration classDecl) {
//                if (classDecl.getType() == null) {
//                    return Optional.empty();
//                }
//                return classDecl.getType().getInterfaces().stream()
//                        .filter(impl -> impl.getFullyQualifiedName().equals(pagingAndSortingRepository))
//                        .findAny();
//            }
//        };
//
//    }
}
