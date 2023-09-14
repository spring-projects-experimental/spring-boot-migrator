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
package org.springframework.sbm.architecture;

import com.tngtech.archunit.core.domain.AccessTarget;
import com.tngtech.archunit.core.domain.JavaCall;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.openrewrite.ExecutionContext;
import org.springframework.sbm.SbmCoreConfig;
import org.springframework.sbm.boot.autoconfigure.ScopeConfiguration;
import org.springframework.sbm.parsers.RewriteExecutionContext;

import static com.tngtech.archunit.lang.conditions.ArchConditions.notBe;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packages = {"org.springframework.sbm", "org.openrewrite"}, importOptions = {ImportOption.DoNotIncludeTests.class, ImportOption.DoNotIncludeJars.class})
public class ControlledInstantiationOfExecutionContextTest {

    private static final Class<?> classWithPermissionToCreateExecutionContext = ScopeConfiguration.class;

    @ArchTest
    public static final ArchRule noClassInstantiatesExecutionContextWillyNilly =
            noClasses()
                    .should()
                    .callCodeUnitWhere(
                            JavaCall.Predicates.target(
                                    AccessTarget.Predicates.constructor()
                                            .and(AccessTarget.Predicates.declaredIn(
                                                    JavaClass.Predicates.assignableTo(ExecutionContext.class)
                                            ))
                            )
                    )
                    .andShould(notBe(classWithPermissionToCreateExecutionContext).and(notBe(SbmCoreConfig.class)))
                    .andShould()
                        .notBe(SbmCoreConfig.class)
                    .andShould()
                        .notBe(RewriteExecutionContext.class)
            ;
}

