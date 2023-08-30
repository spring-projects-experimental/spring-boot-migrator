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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Fabian Krüger
 */
public class SetOrderTest {

    @Test
    @DisplayName("SetShouldKeppOrder")
    void setShouldKeepOrder() {
        Set<String> orderedSet = new LinkedHashSet<>();
        orderedSet.add("A");
        orderedSet.add("B");
        orderedSet.add("C");

        addElement(orderedSet);
        orderedSet.stream().forEach(System.out::println);
    }

    private Set addElement(Set<String> orderedSet) {
        orderedSet.add("D");
        return orderedSet;
    }

}
