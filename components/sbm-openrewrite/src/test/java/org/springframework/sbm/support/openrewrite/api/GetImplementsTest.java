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
package org.springframework.sbm.support.openrewrite.api;

import org.springframework.sbm.java.OpenRewriteTestSupport;
import org.junit.jupiter.api.Test;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.TypeTree;

import java.util.List;

public class GetImplementsTest {

    @Test
    void test() {
        String businessInterface =
                """
                package com.example.jee.app.ejb.local;
                                        
                import javax.ejb.Local;
                                        
                @Local
                public interface ABusinessInterface {
                    String businessMethod();
                }
                """;

        String ejb =
                """
                package com.example.jee.app.ejb.local;
                                        
                import javax.ejb.Stateless;
                                        
                @Stateless
                public class ABean implements ABusinessInterface {
                     @Override
                     public String businessMethod() {
                          return "A";
                     }
                }
                """;

        List<J.CompilationUnit> compilationUnitsFromStrings = OpenRewriteTestSupport.createCompilationUnitsFromStrings(List.of("javax.ejb:javax.ejb-api:3.2"), businessInterface, ejb);

        List<TypeTree> anImplements = compilationUnitsFromStrings.get(1).getClasses().get(0).getImplements();
    }

}
