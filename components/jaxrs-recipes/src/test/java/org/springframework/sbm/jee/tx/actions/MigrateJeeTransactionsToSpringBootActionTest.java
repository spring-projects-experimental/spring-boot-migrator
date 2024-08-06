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
package org.springframework.sbm.jee.tx.actions;

import org.springframework.sbm.test.JavaMigrationActionTestSupport;
import org.springframework.sbm.java.api.JavaSource;
import org.springframework.sbm.java.api.Method;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.project.resource.TestProjectContext;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MigrateJeeTransactionsToSpringBootActionTest {

    @Test
    void migrateTransactionAnnotations() {
        String given = """
                import javax.ejb.*;
                @TransactionManagement(TransactionManagementType.CONTAINER)
                @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
                public class TransactionalService {
                   public void requiresNewFromType() {}
                   @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
                   public void notSupported() {}
                   @TransactionAttribute(TransactionAttributeType.MANDATORY)
                   public void mandatory() {}
                   @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
                   public void requiresNew() {}
                   @TransactionAttribute(TransactionAttributeType.REQUIRED)
                   public void required() {}
                   @TransactionAttribute(TransactionAttributeType.NEVER)
                   public void never() {}
                   @TransactionAttribute(TransactionAttributeType.SUPPORTS)
                   public void supports() {}
                }
                """;

        String expected = """
                import org.springframework.transaction.annotation.Propagation;
                import org.springframework.transaction.annotation.Transactional;
                                
                @Transactional(propagation = Propagation.REQUIRES_NEW)
                public class TransactionalService {
                   public void requiresNewFromType() {}
                                
                   @Transactional(propagation = Propagation.NOT_SUPPORTED)
                   public void notSupported() {}
                                
                   @Transactional(propagation = Propagation.MANDATORY)
                   public void mandatory() {}
                                
                   @Transactional(propagation = Propagation.REQUIRES_NEW)
                   public void requiresNew() {}
                                
                   @Transactional(propagation = Propagation.REQUIRED)
                   public void required() {}
                                
                   @Transactional(propagation = Propagation.NEVER)
                   public void never() {}
                                
                   @Transactional(propagation = Propagation.SUPPORTS)
                   public void supports() {}
                }
                """;

        MigrateJeeTransactionsToSpringBootAction sut = new MigrateJeeTransactionsToSpringBootAction();

        JavaMigrationActionTestSupport.verify(given, expected, sut, "javax.ejb:javax.ejb-api:3.2", "org.springframework.boot:spring-boot-starter-data-jpa:2.4.2");
    }

    @Test
    void transformMethodAnnotations() {
        String given =
                "import javax.ejb.*;\n" +
                        "public class TransactionalService {\n" +
                        "    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)\n" +
                        "    public void notSupported() {}\n" +
                        "}";

        String expected =
                "import org.springframework.transaction.annotation.Propagation;\n" +
                        "import org.springframework.transaction.annotation.Transactional;\n" +
                        "\n" +
                        "public class TransactionalService {\n" +
                        "    @Transactional(propagation = Propagation.NOT_SUPPORTED)\n" +
                        "    public void notSupported() {}\n" +
                        "}";

        MigrateJeeTransactionsToSpringBootAction sut = new MigrateJeeTransactionsToSpringBootAction();

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withBuildFileHavingDependencies("javax.ejb:javax.ejb-api:3.2", "org.springframework.boot:spring-boot-starter-data-jpa:2.4.2")
                .withJavaSources(given)
                .build();

        JavaSource javaSource = projectContext.getProjectJavaSources().list().get(0);
        Method m = javaSource.getTypes().get(0).getMethods().get(0);
        sut.transformMethodAnnotations(m);

        assertThat(javaSource.print()).isEqualTo(expected);
    }
}