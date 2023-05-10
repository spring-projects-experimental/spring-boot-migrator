package org.springframework.sbm.architecture;

import com.tngtech.archunit.core.domain.AccessTarget;
import com.tngtech.archunit.core.domain.JavaCall;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.junit.ArchUnitRunner;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.runner.RunWith;
import org.openrewrite.ExecutionContext;
import org.springframework.sbm.openrewrite.RewriteExecutionContext;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@RunWith(ArchUnitRunner.class)
@AnalyzeClasses(packages = "org.springframework.sbm", importOptions = ImportOption.DoNotIncludeTests.class)
public class ControlledInstantiationOfExecutionContextTest {

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
            ;
}
