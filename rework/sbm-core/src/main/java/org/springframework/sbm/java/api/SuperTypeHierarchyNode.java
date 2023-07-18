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

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
public class SuperTypeHierarchyNode {

    private final Type node;
    private final List<SuperTypeHierarchyNode> superTypes = new ArrayList<>();

    public SuperTypeHierarchyNode(Type type) {
        this.node = type;

        // find all classes extending or implementing type
        List<Type> superTypes = new ArrayList<>();
        Optional<? extends Type> superClass = type.getExtends();
        if (superClass.isPresent()) {
            superTypes.add(superClass.get());
        }
        List<? extends Type> superInterfaces = type.getImplements();
        if (!superInterfaces.isEmpty()) {
            superTypes.addAll(superInterfaces);
        }

        // create SuperTypeHierarchyNode for each of them
        superTypes.forEach(st ->
                this.superTypes.add(new SuperTypeHierarchyNode(st)));
    }

}
