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
package org.springframework.sbm.java.api;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class TypeHierarchyNode {
    private final Type node;
    private final List<TypeHierarchyNode> children = new ArrayList<>();
    private final Type parent;

    public TypeHierarchyNode(Type node, Type parent, ProjectJavaSources javaSourceSet) {
        this.node = node;
        this.parent = parent;

        List<? extends Type> childNodes = javaSourceSet.list().stream()
                .map(JavaSource::getTypes)
                .flatMap(List::stream)
                .filter(t -> isSubTypeOf(t, node))
                .collect(Collectors.toList());

        childNodes.forEach(cn -> {
            this.children.add(new TypeHierarchyNode(cn, node, javaSourceSet));
        });
    }

    public String getFullyQualifiedName() {
        return node.getFullyQualifiedName();
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
