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
package org.springframework.sbm.java.api;

public class SubTypeHierarchy {

    private final TypeHierarchyNode root;

    public SubTypeHierarchy(Type root, ProjectJavaSources javaSourceSet) {
        this.root = new TypeHierarchyNode(root, null, javaSourceSet);
//        initTree(root, null, javaSourceSet, openRewriteJavaSource);
    }

    public TypeHierarchyNode getRoot() {
        return root;
    }

    private boolean isSubTypeOf(Type t, Type root) {
        boolean isSubTypeOf = false;
        if (t.getExtends().isPresent()) {
            isSubTypeOf = t.getExtends().get().getFullyQualifiedName().equals(root.getFullyQualifiedName());
        }
        if (!t.getImplements().isEmpty()) {
            isSubTypeOf = isSubTypeOf || t.getImplements().stream().anyMatch(i -> i.getFullyQualifiedName().equals(root.getFullyQualifiedName()));
        }
        return isSubTypeOf;
    }
}
