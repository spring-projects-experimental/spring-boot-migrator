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
package org.springframework.sbm.jee.ejb.actions;

import org.springframework.sbm.java.api.*;

import java.util.ArrayList;
import java.util.List;

public class MigrateLocalStatelessSessionBeansHelper {

    public static final String EJB_STATELESS_ANNOTATION = "javax.ejb.Stateless";
    public static final String EJB_LOCAL_ANNOTATION = "javax.ejb.Local";
    private static final String SPRING_SERVICE_ANNOTATION = "org.springframework.stereotype.Service";
    private static final String EJB_REMOTE_ANNOTATION = "javax.ejb.Remote";
    private static final String EJB_SINGLETON_ANNOTATION = "javax.ejb.Singleton";
    private static final String EJB_LOCALBEAN_ANNOTATION = "javax.ejb.LocalBean";

    public boolean implementsNoRemoteInterface(SuperTypeHierarchy hierarchy) {
        boolean noRemoteInterface = new AnnotatedTypeFinder(hierarchy, EJB_REMOTE_ANNOTATION).findAnnotatedTypes().isEmpty();
        return noRemoteInterface || hierarchy.getRoot().getSuperTypes().isEmpty();
    }

    public void removeLocalAnnotations(SuperTypeHierarchy hierarchy) {
        List<Type> annotatedTypes = new AnnotatedTypeFinder(hierarchy, EJB_LOCAL_ANNOTATION).findAnnotatedTypes();
        annotatedTypes.forEach(type -> type.removeAnnotation(EJB_LOCAL_ANNOTATION));
    }

    public List<TypeAndSourceFile> findTypesAnnotatedWithStateless(ProjectJavaSources javaSourceSet) {
        return getTypeAndSourceFiles(javaSourceSet, EJB_STATELESS_ANNOTATION);
    }

    private List<TypeAndSourceFile> getTypeAndSourceFiles(ProjectJavaSources javaSourceSet, String annotation) {
        List<TypeAndSourceFile> typeAndSourceFiles = new ArrayList<>();
        javaSourceSet.list().stream()
                .forEach(js -> {
                    js.getTypes().stream()
                            .filter(t -> {
                                return t.hasAnnotation(annotation);
                            })
                            .forEach(t -> typeAndSourceFiles.add(new TypeAndSourceFile(js, t)));
                });
        return typeAndSourceFiles;
    }

    public void migrateStatelessAnnotation(Type ssb) {
        StatelessAnnotationTemplateMapper statelessAnnotationTemplateMapper = new StatelessAnnotationTemplateMapper();
        Annotation annotation = ssb.getAnnotations().stream().filter(a -> EJB_STATELESS_ANNOTATION.equals(a.getFullyQualifiedName())).findFirst().get();
        String annotationSnippet = statelessAnnotationTemplateMapper.mapToServiceAnnotation(annotation);
        ssb.removeAnnotation(EJB_STATELESS_ANNOTATION);
        ssb.addAnnotation(annotationSnippet, SPRING_SERVICE_ANNOTATION);
    }

    public List<TypeAndSourceFile> findTypesAnnotatedWithSingleton(ProjectJavaSources javaSourceSet) {
        return getTypeAndSourceFiles(javaSourceSet, EJB_SINGLETON_ANNOTATION);
    }

    public void migrateSingletonAnnotation(Type ssb) {
        SingletonAnnotationTemplateMapper statelessAnnotationTemplateMapper = new SingletonAnnotationTemplateMapper();
        Annotation annotation = ssb.getAnnotations().stream().filter(a -> EJB_SINGLETON_ANNOTATION.equals(a.getFullyQualifiedName())).findFirst().get();
        String annotationSnippet = statelessAnnotationTemplateMapper.mapToServiceAnnotation(annotation);
        ssb.removeAnnotation(EJB_SINGLETON_ANNOTATION);
        ssb.addAnnotation(annotationSnippet, SPRING_SERVICE_ANNOTATION);
    }

    public List<TypeAndSourceFile> findTypesAnnotatedWithLocalBean(ProjectJavaSources javaSourceSet) {
        return getTypeAndSourceFiles(javaSourceSet, EJB_LOCALBEAN_ANNOTATION);
    }

    public void removeLocalBeanAnnotations(ProjectJavaSources javaSourceSet) {
        findTypesAnnotatedWithLocalBean(javaSourceSet)
                .forEach(ts -> ts.getType().removeAnnotation(EJB_LOCALBEAN_ANNOTATION));
    }

    private class AnnotatedTypeFinder {

        private final SuperTypeHierarchy superTypeHierarchy;
        private final String annotationToFind;
        private final List<Type> annotatedTypes = new ArrayList<>();

        public AnnotatedTypeFinder(SuperTypeHierarchy superTypeHierarchy, String annotationToFind) {
            this.superTypeHierarchy = superTypeHierarchy;
            this.annotationToFind = annotationToFind;
        }

        public List<Type> findAnnotatedTypes() {
            traverseHierarchyUpAndSearchForAnnotation(superTypeHierarchy.getRoot());
            return annotatedTypes;
        }

        private void traverseHierarchyUpAndSearchForAnnotation(SuperTypeHierarchyNode currentNode) {
            if (currentNode.getNode().hasAnnotation(annotationToFind)) {
                this.annotatedTypes.add(currentNode.getNode());
            }
            if (!currentNode.getSuperTypes().isEmpty()) {
                currentNode.getSuperTypes().forEach(sthn -> traverseHierarchyUpAndSearchForAnnotation(sthn));
            }
        }

    }
}
