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
package org.springframework.sbm.support.openrewrite.java;

import org.springframework.sbm.java.OpenRewriteTestSupport;
import org.junit.jupiter.api.Test;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.JavaType;
import org.openrewrite.java.tree.TypeTree;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class GetImplementsTest {

    @Test
    void getImplements_withOneImplementedInterface() {
        String theInterface = "public interface TheInterface {}";
        String theClass = "public class TheClass implements TheInterface {}";

        List<J.CompilationUnit> compilationUnits = OpenRewriteTestSupport.createCompilationUnitsFromStrings(theInterface, theClass);

        List<TypeTree> implementedInterfaces = compilationUnits.get(1).getClasses().get(0).getImplements();
        assertThat(implementedInterfaces).hasSize(1);
        List<JavaType.FullyQualified> fqImplementedInterfaces = compilationUnits.get(1).getClasses().get(0).getType().getInterfaces();
        assertThat(fqImplementedInterfaces).hasSize(1);
    }

}
