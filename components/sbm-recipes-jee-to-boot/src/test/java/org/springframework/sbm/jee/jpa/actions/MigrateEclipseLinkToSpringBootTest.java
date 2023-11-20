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
package org.springframework.sbm.jee.jpa.actions;

import org.springframework.sbm.SbmConstants;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.java.util.BasePackageCalculator;
import org.springframework.sbm.jee.jpa.resource.PersistenceXmlProjectResourceRegistrar;
import org.springframework.sbm.project.resource.SbmApplicationProperties;
import org.springframework.sbm.project.resource.TestProjectContext;
import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Version;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class MigrateEclipseLinkToSpringBootTest {

    private MigrateEclipseLinkToSpringBoot sut;
    private Configuration configuration;

    @BeforeEach
    void setUp() throws IOException {
        Version version = new Version("2.3.0");
        configuration = new Configuration(version);
        configuration.setTemplateLoader(new FileTemplateLoader(new File("./src/main/resources/templates")));

        SbmApplicationProperties sbmApplicationProperties = new SbmApplicationProperties();
        sbmApplicationProperties.setDefaultBasePackage("com.vmware.example");

        BasePackageCalculator basePackageCalculator = new BasePackageCalculator(sbmApplicationProperties);

        sut = new MigrateEclipseLinkToSpringBoot();
        sut.setConfiguration(configuration);
        sut.setBasePackageCalculator(basePackageCalculator);
    }

    @Test
    void shouldAddDependencyToSpringBootStarterDataJpaIfNoneExists() {
        String pomXml =
                """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0"
                         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <groupId>org.example</groupId>
                    <artifactId>sbm-testproject-jee-eclipselink</artifactId>
                    <version>1.0-SNAPSHOT</version>
                    <properties>
                        <maven.compiler.source>11</maven.compiler.source>
                        <maven.compiler.target>11</maven.compiler.target>
                    </properties>
                    <dependencies>
                        <dependency>
                            <groupId>org.eclipse.persistence</groupId>
                            <artifactId>org.eclipse.persistence.jpa</artifactId>
                            <version>2.7.10</version>
                        </dependency>
                    </dependencies>
                </project>
                """;

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withMavenRootBuildFileSource(pomXml)
                .build();

        sut.apply(projectContext);

        assertThat(projectContext.getApplicationModules().getRootModule().getBuildFile().print()).isEqualTo(
                """
                        <?xml version="1.0" encoding="UTF-8"?>
                        <project xmlns="http://maven.apache.org/POM/4.0.0"
                                 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                                 xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                            <modelVersion>4.0.0</modelVersion>
                            <groupId>org.example</groupId>
                            <artifactId>sbm-testproject-jee-eclipselink</artifactId>
                            <version>1.0-SNAPSHOT</version>
                            <properties>
                                <maven.compiler.source>11</maven.compiler.source>
                                <maven.compiler.target>11</maven.compiler.target>
                            </properties>
                            <dependencies>
                                <dependency>
                                    <groupId>org.eclipse.persistence</groupId>
                                    <artifactId>org.eclipse.persistence.jpa</artifactId>
                                    <version>2.7.10</version>
                                </dependency>
                                <dependency>
                                    <groupId>org.springframework.boot</groupId>
                                    <artifactId>spring-boot-starter-data-jpa</artifactId>
                                    <version>2.5.9</version>
                                    <exclusions>
                                        <exclusion>
                                            <groupId>org.hibernate</groupId>
                                            <artifactId>hibernate-core</artifactId>
                                        </exclusion>
                                    </exclusions>
                                </dependency>
                            </dependencies>
                        </project>
                        """
        );
    }

    @Test
    void shouldAddExcludesToSpringBootStarterDataJpaIfExists() {
        String pomXml =
                """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0"
                         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <groupId>org.example</groupId>
                    <artifactId>sbm-testproject-jee-eclipselink</artifactId>
                    <version>1.0-SNAPSHOT</version>
                    <properties>
                        <maven.compiler.source>11</maven.compiler.source>
                        <maven.compiler.target>11</maven.compiler.target>
                    </properties>
                    <dependencies>
                        <dependency>
                            <groupId>org.eclipse.persistence</groupId>
                            <artifactId>org.eclipse.persistence.jpa</artifactId>
                            <version>2.7.10</version>
                        </dependency>
                        <dependency>
                            <groupId>org.springframework.boot</groupId>
                            <artifactId>spring-boot-starter-data-jpa</artifactId>
                            <version>2.5.9</version>
                        </dependency>
                    </dependencies>
                </project>
                """;

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withMavenRootBuildFileSource(pomXml)
                .build();

        sut.apply(projectContext);

        assertThat(projectContext.getApplicationModules().getRootModule().getBuildFile().print()).isEqualTo(
                """
                        <?xml version="1.0" encoding="UTF-8"?>
                        <project xmlns="http://maven.apache.org/POM/4.0.0"
                                 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                                 xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                            <modelVersion>4.0.0</modelVersion>
                            <groupId>org.example</groupId>
                            <artifactId>sbm-testproject-jee-eclipselink</artifactId>
                            <version>1.0-SNAPSHOT</version>
                            <properties>
                                <maven.compiler.source>11</maven.compiler.source>
                                <maven.compiler.target>11</maven.compiler.target>
                            </properties>
                            <dependencies>
                                <dependency>
                                    <groupId>org.eclipse.persistence</groupId>
                                    <artifactId>org.eclipse.persistence.jpa</artifactId>
                                    <version>2.7.10</version>
                                </dependency>
                                <dependency>
                                    <groupId>org.springframework.boot</groupId>
                                    <artifactId>spring-boot-starter-data-jpa</artifactId>
                                    <version>2.5.9</version>
                                    <exclusions>
                                        <exclusion>
                                            <groupId>org.hibernate</groupId>
                                            <artifactId>hibernate-core</artifactId>
                                        </exclusion>
                                    </exclusions>
                                </dependency>
                            </dependencies>
                        </project>
                        """
        );
    }

    @Test
    void shouldAddEclipseLinkConfigurationClass() {
        String pomXml =
                """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0"
                         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <groupId>org.example</groupId>
                    <artifactId>sbm-testproject-jee-eclipselink</artifactId>
                    <version>1.0-SNAPSHOT</version>
                    <properties>
                        <maven.compiler.source>11</maven.compiler.source>
                        <maven.compiler.target>11</maven.compiler.target>
                    </properties>
                    <dependencies>
                        <dependency>
                            <groupId>org.eclipse.persistence</groupId>
                            <artifactId>org.eclipse.persistence.jpa</artifactId>
                            <version>2.7.10</version>
                        </dependency>
                    </dependencies>
                </project>
                """;

        String persistenceXml =
                """
                <persistence xmlns="http://java.sun.com/xml/ns/persistence" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://java.sun.com/xml/ns/persistence persistence_1_0.xsd" version="1.0">
                    <persistence-unit name="my-app" transaction-type="RESOURCE_LOCAL">
                        <provider>org.eclipse.persistence.jpa.PersistenceProvider</provider>
                        <exclude-unlisted-classes>false</exclude-unlisted-classes>
                        <properties>
                            <property name="javax.persistence.jdbc.driver" value="oracle.jdbc.OracleDriver"/>
                            <property name="javax.persistence.jdbc.url" value="jdbc:oracle:thin:@localhost:1521:orcl"/>
                            <property name="javax.persistence.jdbc.user" value="scott"/>
                            <property name="javax.persistence.jdbc.password" value="tiger"/>
                            <property name="eclipselink.application-location" value="PersistenceUnitProperties.APP_LOCATION"/>
                            <property name="eclipselink.cache.coordination.channel" value="PersistenceUnitProperties.COORDINATION_CHANNEL"/>
                            <property name="eclipselink.cache.coordination.jms.factory" value="PersistenceUnitProperties.COORDINATION_JMS_FACTORY"/>
                            <property name="eclipselink.cache.coordination.jms.host" value="PersistenceUnitProperties.COORDINATION_JMS_HOST"/>
                            <property name="eclipselink.cache.coordination.jms.reuse-topic-publisher" value="PersistenceUnitProperties.COORDINATION_JMS_REUSE_PUBLISHER"/>
                            <property name="eclipselink.cache.coordination.jms.topic" value="PersistenceUnitProperties.COORDINATION_JMS_TOPIC"/>
                            <property name="eclipselink.cache.coordination.jndi.initial-context-factory" value="PersistenceUnitProperties.COORDINATION_JNDI_CONTEXT"/>
                            <property name="eclipselink.cache.coordination.jndi.password" value="PersistenceUnitProperties.COORDINATION_JNDI_PASSWORD"/>
                            <property name="eclipselink.cache.coordination.jndi.user" value="PersistenceUnitProperties.COORDINATION_JNDI_USER"/>
                            <property name="eclipselink.cache.coordination.naming-service" value="PersistenceUnitProperties.COORDINATION_NAMING_SERVICE"/>
                            <property name="eclipselink.cache.coordination.propagate-asynchronously" value="PersistenceUnitProperties.COORDINATION_ASYNCH"/>
                            <property name="eclipselink.cache.coordination.protocol" value="PersistenceUnitProperties.COORDINATION_PROTOCOL"/>
                            <property name="eclipselink.cache.coordination.remove-connection-on-error" value="PersistenceUnitProperties.COORDINATION_REMOVE_CONNECTION"/>
                            <property name="eclipselink.cache.coordination.rmi.announcement-delay" value="PersistenceUnitProperties.COORDINATION_RMI_ANNOUNCEMENT_DELAY"/>
                            <property name="eclipselink.cache.coordination.rmi.multicast-group" value="PersistenceUnitProperties.COORDINATION_RMI_MULTICAST_GROUP"/>
                            <property name="eclipselink.cache.coordination.rmi.multicast-group.port" value="PersistenceUnitProperties.COORDINATION_RMI_MULTICAST_GROUP_PORT"/>
                            <property name="eclipselink.cache.coordination.rmi.packet-time-to-live" value="PersistenceUnitProperties.COORDINATION_RMI_PACKET_TIME_TO_LIVE"/>
                            <property name="eclipselink.cache.coordination.rmi.url" value="PersistenceUnitProperties.COORDINATION_RMI_URL"/>
                            <property name="eclipselink.cache.coordination.thread.pool.size" value="PersistenceUnitProperties.COORDINATION_THREAD_POOL_SIZE"/>
                            <property name="eclipselink.cache.database-event-listener" value="PersistenceUnitProperties.DATABASE_EVENT_LISTENER"/>
                            <property name="eclipselink.cache.shared.default" value="PersistenceUnitProperties.CACHE_SHARED_DEFAULT"/>
                            <property name="eclipselink.cache.size" value="cache.size"/>
                            <property name="eclipselink.cache.type" value="cache.type"/>
                            <property name="eclipselink.classloader" value="PersistenceUnitProperties.CLASSLOADER"/>
                            <property name="eclipselink.composite-unit" value="PersistenceUnitProperties.COMPOSITE_UNIT"/>
                            <property name="eclipselink.composite-unit.member" value="PersistenceUnitProperties.COMPOSITE_UNIT_MEMBER"/>
                            <property name="eclipselink.composite-unit.properties" value="PersistenceUnitProperties.COMPOSITE_UNIT_PROPERTIES"/>
                            <property name="eclipselink.concurrency.manager.waittime" value="concurrency.manager.waittime"/>
                            <property name="eclipselink.concurrency.manager.maxsleeptime" value="concurrency.manager.maxsleeptime"/>
                            <property name="eclipselink.concurrency.manager.maxfrequencytodumptinymessage" value="concurrency.manager.maxfrequencytodumptinymessage"/>
                            <property name="eclipselink.concurrency.manager.maxfrequencytodumpmassivemessage" value="concurrency.manager.maxfrequencytodumpmassivemessage"/>
                            <property name="eclipselink.concurrency.manager.allow.interruptedexception" value="concurrency.manager.allow.interruptedexception"/>
                            <property name="eclipselink.concurrency.manager.allow.concurrencyexception" value="concurrency.manager.allow.concurrencyexception"/>
                            <property name="eclipselink.concurrency.manager.allow.readlockstacktrace" value="concurrency.manager.allow.readlockstacktrace"/>
                            <property name="eclipselink.connection-pool" value="connection-pool"/>
                            <property name="eclipselink.connection-pool.read" value="connection-pool.read"/>
                            <property name="eclipselink.connection-pool.sequence" value="connection-pool.sequence"/>
                            <property name="eclipselink.create-ddl-jdbc-file-name" value="PersistenceUnitProperties.CREATE_JDBC_DDL_FILE"/>
                            <property name="eclipselink.ddl-generation" value="PersistenceUnitProperties.DDL_GENERATION"/>
                            <property name="eclipselink.ddl-generation.output-mode" value="PersistenceUnitProperties.DDL_GENERATION_MODE"/>
                            <property name="eclipselink.ddl.table-creation-suffix" value="ddl.table-creation-suffix"/>
                            <property name="eclipselink.deploy-on-startup" value="PersistenceUnitProperties.DEPLOY_ON_STARTUP"/>
                            <property name="eclipselink.descriptor.customizer" value="descriptor.customizer"/>
                            <property name="eclipselink.drop-ddl-jdbc-file-name" value="PersistenceUnitProperties.DROP_JDBC_DDL_FILE"/>
                            <property name="eclipselink.exception-handler" value="PersistenceUnitProperties.EXCEPTION_HANDLER_CLASS"/>
                            <property name="eclipselink.exclude-eclipselink-orm" value="PersistenceUnitProperties.EXCLUDE_ECLIPSELINK_ORM_FILE"/>
                            <property name="eclipselink.flush-clear.cache" value="PersistenceUnitProperties.FLUSH_CLEAR_CACHE"/>
                            <property name="eclipselink.id-validation" value="PersistenceUnitProperties.ID_VALIDATION"/>
                            <property name="eclipselink.jdbc.allow-native-sql-queries" value="PersistenceUnitProperties.ALLOW_NATIVE_SQL_QUERIES"/>
                            <property name="eclipselink.jdbc.batch-writing" value="PersistenceUnitProperties.BATCH_WRITING"/>
                            <property name="eclipselink.jdbc.batch-writing.size" value="PersistenceUnitProperties.BATCH_WRITING_SIZE"/>
                            <property name="eclipselink.jdbc.cache-statements" value="PersistenceUnitProperties.CACHE_STATEMENTS"/>
                            <property name="eclipselink.jdbc.cache-statements.size" value="PersistenceUnitProperties.CACHE_STATEMENTS_SIZE"/>
                            <property name="eclipselink.jdbc.connector" value="PersistenceUnitProperties.JDBC_CONNECTOR"/>
                            <property name="eclipselink.jdbc.exclusive-connection.is-lazy" value="PersistenceUnitProperties.EXCLUSIVE_CONNECTION_IS_LAZY"/>
                            <property name="eclipselink.jdbc.exclusive-connection.mode" value="PersistenceUnitProperties.EXCLUSIVE_CONNECTION_MODE"/>
                            <property name="eclipselink.jdbc.native-sql" value="PersistenceUnitProperties.NATIVE_SQL"/>
                            <property name="eclipselink.jdbc.property" value="jdbc.property"/>
                            <property name="eclipselink.jdbc.sql-cast" value="PersistenceUnitProperties.SQL_CAST"/>
                            <property name="eclipselink.jdbc.uppercase-columns" value="PersistenceUnitProperties.NATIVE_QUERY_UPPERCASE_COLUMNS"/>
                            <property name="eclipselink.jpa.uppercase-column-names" value="PersistenceUnitProperties.UPPERCASE_COLUMN_NAMES"/>
                            <property name="eclipselink.jpql.parser" value="PersistenceUnitProperties.JPQL_PARSER"/>
                            <property name="eclipselink.jpql.validation" value="PersistenceUnitProperties.JPQL_VALIDATION"/>
                            <property name="eclipselink.logging.connection" value="PersistenceUnitProperties.LOGGING_CONNECTION"/>
                            <property name="eclipselink.logging.exceptions" value="PersistenceUnitProperties.LOGGING_EXCEPTIONS"/>
                            <property name="eclipselink.logging.file" value="PersistenceUnitProperties.LOGGING_FILE"/>
                            <property name="eclipselink.logging.level" value="PersistenceUnitProperties.LOGGING_LEVEL"/>
                            <property name="eclipselink.logging.session" value="PersistenceUnitProperties.LOGGING_SESSION"/>
                            <property name="eclipselink.logging.thread" value="PersistenceUnitProperties.LOGGING_THREAD"/>
                            <property name="eclipselink.logging.timestamp" value="PersistenceUnitProperties.LOGGING_TIMESTAMP"/>
                            <property name="eclipselink.metadata-source" value="PersistenceUnitProperties.METADATA_SOURCE"/>
                            <property name="eclipselink.metadata-source.properties.file" value="PersistenceUnitProperties.METADATA_SOURCE_PROPERTIES_FILE"/>
                            <property name="eclipselink.metadata-source.send-refresh-command" value="PersistenceUnitProperties.METADATA_SOURCE_RCM_COMMAND"/>
                            <property name="eclipselink.metadata-source.xml.url" value="PersistenceUnitProperties.METADATA_SOURCE_XML_URL"/>
                            <property name="eclipselink.nosql.connection-factory" value="PersistenceUnitProperties.NOSQL_CONNECTION_FACTORY"/>
                            <property name="eclipselink.nosql.connection-spec" value="PersistenceUnitProperties.NOSQL_CONNECTION_SPEC"/>
                            <property name="eclipselink.nosql.property" value="nosql.property"/>
                            <property name="eclipselink.oracle.proxy-type" value="PersistenceUnitProperties.ORACLE_PROXY_TYPE"/>
                            <property name="eclipselink.orm.throw.exceptions" value="PersistenceUnitProperties.THROW_EXCEPTIONS"/>
                            <property name="eclipselink.orm.validate.schema" value="PersistenceUnitProperties.ORM_SCHEMA_VALIDATION"/>
                            <property name="eclipselink.partitioning" value="PersistenceUnitProperties.PARTITIONING"/>
                            <property name="eclipselink.partitioning.callback" value="PersistenceUnitProperties.PARTITIONING_CALLBACK"/>
                            <property name="eclipselink.persistence-context.close-on-commit" value="PersistenceUnitProperties.PERSISTENCE_CONTEXT_CLOSE_ON_COMMIT"/>
                            <property name="eclipselink.persistence-context.commit-without-persist-rules" value="PersistenceUnitProperties.PERSISTENCE_CONTEXT_COMMIT_WITHOUT_PERSIST_RULES"/>
                            <property name="eclipselink.persistence-context.flush-mode" value="PersistenceUnitProperties.PERSISTENCE_CONTEXT_FLUSH_MODE"/>
                            <property name="eclipselink.persistence-context.persist-on-commit" value="PersistenceUnitProperties.PERSISTENCE_CONTEXT_PERSIST_ON_COMMIT"/>
                            <property name="eclipselink.persistence-context.reference-mode" value="PersistenceUnitProperties.PERSISTENCE_CONTEXT_REFERENCE_MODE"/>
                            <property name="eclipselink.persistenceunits" value="PersistenceUnitProperties.ECLIPSELINK_PERSISTENCE_UNITS"/>
                            <property name="eclipselink.persistencexml" value="PersistenceUnitProperties.ECLIPSELINK_PERSISTENCE_XML"/>
                            <property name="eclipselink.persisencexml.default" value="persisencexml.default"/>
                            <property name="eclipselink.profiler" value="PersistenceUnitProperties.PROFILER"/>
                            <property name="eclipselink.session.customizer" value="PersistenceUnitProperties.SESSION_CUSTOMIZER"/>
                            <property name="eclipselink.session.include.descriptor.queries" value="PersistenceUnitProperties.INCLUDE_DESCRIPTOR_QUERIES"/>
                            <property name="eclipselink.session-event-listener" value="PersistenceUnitProperties.SESSION_EVENT_LISTENER_CLASS"/>
                            <property name="eclipselink.session-name" value="PersistenceUnitProperties.SESSION_NAME"/>
                            <property name="eclipselink.sessions-xml" value="PersistenceUnitProperties.SESSIONS_XML"/>
                            <property name="eclipselink.target-database" value="PersistenceUnitProperties.TARGET_DATABASE"/>
                            <property name="eclipselink.target-server" value="PersistenceUnitProperties.TARGET_SERVER"/>
                            <property name="eclipselink.temporal.mutable" value="PersistenceUnitProperties.TEMPORAL_MUTABLE"/>
                            <property name="eclipselink.tenant-id" value="PersistenceUnitProperties.MULTITENANT_PROPERTY_DEFAULT"/>
                            <property name="eclipselink.transaction.join-existing" value="PersistenceUnitProperties.JOIN_EXISTING_TRANSACTION"/>
                            <property name="eclipselink.tuning" value="PersistenceUnitProperties.TUNING"/>
                            <property name="eclipselink.validate-existence" value="PersistenceUnitProperties.VALIDATE_EXISTENCE"/>
                            <property name="eclipselink.validation-only" value="PersistenceUnitProperties.VALIDATION_ONLY_PROPERTY"/>
                            <property name="eclipselink.weaving" value="PersistenceUnitProperties.WEAVING"/>
                            <property name="eclipselink.weaving.changetracking" value="PersistenceUnitProperties.WEAVING_CHANGE_TRACKING"/>
                            <property name="eclipselink.weaving.eager" value="PersistenceUnitProperties.WEAVING_EAGER"/>
                            <property name="eclipselink.weaving.fetchgroups" value="PersistenceUnitProperties.WEAVING_FETCHGROUPS"/>
                            <property name="eclipselink.weaving.internal" value="PersistenceUnitProperties.WEAVING_INTERNAL"/>
                            <property name="eclipselink.weaving.lazy" value="PersistenceUnitProperties.WEAVING_LAZY"/>
                        </properties>
                    </persistence-unit>
                </persistence>
                """;

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withMavenRootBuildFileSource(pomXml)
                .withProjectResource("src/main/resources/META-INF/persistence.xml", persistenceXml)
                .addRegistrar(new PersistenceXmlProjectResourceRegistrar())
                .build();

        sut.apply(projectContext);

        assertThat(projectContext.getProjectJavaSources().findJavaSourceDeclaringType("com.vmware.example.EclipseLinkJpaConfiguration")).isNotEmpty();
        String cleanedSource = projectContext.getProjectJavaSources().findJavaSourceDeclaringType("com.vmware.example.EclipseLinkJpaConfiguration").get().print().replace("\r\n", SbmConstants.LS).replace("\r", "\n");
        assertThat(cleanedSource).isEqualTo(
                """
                        package com.vmware.example;
                                                
                        import org.eclipse.persistence.config.PersistenceUnitProperties;
                        import org.eclipse.persistence.logging.SessionLog;
                        import org.springframework.beans.factory.ObjectProvider;
                        import org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration;
                        import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
                        import org.springframework.context.annotation.Bean;
                        import org.springframework.context.annotation.Configuration;
                        import org.springframework.context.annotation.EnableLoadTimeWeaving;
                        import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
                        import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
                        import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
                        import org.springframework.transaction.jta.JtaTransactionManager;
                                                
                        import javax.sql.DataSource;
                        import java.util.HashMap;
                        import java.util.Map;
                                                
                        @Configuration
                        @EnableLoadTimeWeaving(aspectjWeaving = EnableLoadTimeWeaving.AspectJWeaving.ENABLED)
                        public class EclipseLinkJpaConfiguration extends JpaBaseConfiguration {
                                                
                            protected EclipseLinkJpaConfiguration(DataSource dataSource, JpaProperties properties, ObjectProvider<JtaTransactionManager> jtaTransactionManager) {
                                super(dataSource, properties, jtaTransactionManager);
                            }
                                                
                            @Override
                            protected AbstractJpaVendorAdapter createJpaVendorAdapter() {
                                return new EclipseLinkJpaVendorAdapter();
                            }
                                                
                            @Override
                            protected Map<String, Object> getVendorProperties() {
                                Map<String, Object> map = new HashMap<>();
                                                
                                map.put(PersistenceUnitProperties.PERSISTENCE_CONTEXT_REFERENCE_MODE, "PersistenceUnitProperties.PERSISTENCE_CONTEXT_REFERENCE_MODE");
                                map.put(PersistenceUnitProperties.VALIDATE_EXISTENCE, "PersistenceUnitProperties.VALIDATE_EXISTENCE");
                                map.put(PersistenceUnitProperties.SESSION_NAME, "PersistenceUnitProperties.SESSION_NAME");
                                map.put("eclipselink.concurrency.manager.waittime", "concurrency.manager.waittime");
                                map.put(PersistenceUnitProperties.COORDINATION_JNDI_CONTEXT, "PersistenceUnitProperties.COORDINATION_JNDI_CONTEXT");
                                map.put(PersistenceUnitProperties.COORDINATION_JNDI_PASSWORD, "PersistenceUnitProperties.COORDINATION_JNDI_PASSWORD");
                                map.put(PersistenceUnitProperties.COORDINATION_JNDI_USER, "PersistenceUnitProperties.COORDINATION_JNDI_USER");
                                map.put(PersistenceUnitProperties.WEAVING_LAZY, "PersistenceUnitProperties.WEAVING_LAZY");
                                map.put(PersistenceUnitProperties.JPQL_PARSER, "PersistenceUnitProperties.JPQL_PARSER");
                                map.put("eclipselink.persisencexml.default", "persisencexml.default");
                                map.put(PersistenceUnitProperties.METADATA_SOURCE_RCM_COMMAND, "PersistenceUnitProperties.METADATA_SOURCE_RCM_COMMAND");
                                map.put(PersistenceUnitProperties.BATCH_WRITING, "PersistenceUnitProperties.BATCH_WRITING");
                                map.put(PersistenceUnitProperties.CACHE_STATEMENTS, "PersistenceUnitProperties.CACHE_STATEMENTS");
                                map.put(PersistenceUnitProperties.COMPOSITE_UNIT, "PersistenceUnitProperties.COMPOSITE_UNIT");
                                map.put(PersistenceUnitProperties.JPQL_VALIDATION, "PersistenceUnitProperties.JPQL_VALIDATION");
                                map.put(PersistenceUnitProperties.TARGET_DATABASE, "PersistenceUnitProperties.TARGET_DATABASE");
                                map.put(PersistenceUnitProperties.COMPOSITE_UNIT_MEMBER, "PersistenceUnitProperties.COMPOSITE_UNIT_MEMBER");
                                map.put(PersistenceUnitProperties.THROW_EXCEPTIONS, "PersistenceUnitProperties.THROW_EXCEPTIONS");
                                map.put(PersistenceUnitProperties.LOGGING_CONNECTION, "PersistenceUnitProperties.LOGGING_CONNECTION");
                                map.put(PersistenceUnitProperties.COORDINATION_NAMING_SERVICE, "PersistenceUnitProperties.COORDINATION_NAMING_SERVICE");
                                map.put(PersistenceUnitProperties.COORDINATION_THREAD_POOL_SIZE, "PersistenceUnitProperties.COORDINATION_THREAD_POOL_SIZE");
                                map.put("eclipselink.concurrency.manager.allow.concurrencyexception", "concurrency.manager.allow.concurrencyexception");
                                map.put(PersistenceUnitProperties.LOGGING_EXCEPTIONS, "PersistenceUnitProperties.LOGGING_EXCEPTIONS");
                                map.put(PersistenceUnitProperties.DATABASE_EVENT_LISTENER, "PersistenceUnitProperties.DATABASE_EVENT_LISTENER");
                                map.put(PersistenceUnitProperties.WEAVING_EAGER, "PersistenceUnitProperties.WEAVING_EAGER");
                                map.put(PersistenceUnitProperties.CREATE_JDBC_DDL_FILE, "PersistenceUnitProperties.CREATE_JDBC_DDL_FILE");
                                map.put(PersistenceUnitProperties.ECLIPSELINK_PERSISTENCE_XML, "PersistenceUnitProperties.ECLIPSELINK_PERSISTENCE_XML");
                                map.put(PersistenceUnitProperties.MULTITENANT_PROPERTY_DEFAULT, "PersistenceUnitProperties.MULTITENANT_PROPERTY_DEFAULT");
                                map.put(PersistenceUnitProperties.LOGGING_FILE, "PersistenceUnitProperties.LOGGING_FILE");
                                map.put(PersistenceUnitProperties.LOGGING_TIMESTAMP, "PersistenceUnitProperties.LOGGING_TIMESTAMP");
                                map.put(PersistenceUnitProperties.COORDINATION_PROTOCOL, "PersistenceUnitProperties.COORDINATION_PROTOCOL");
                                map.put(PersistenceUnitProperties.WEAVING_CHANGE_TRACKING, "PersistenceUnitProperties.WEAVING_CHANGE_TRACKING");
                                map.put(PersistenceUnitProperties.SQL_CAST, "PersistenceUnitProperties.SQL_CAST");
                                map.put(PersistenceUnitProperties.SESSION_CUSTOMIZER, "PersistenceUnitProperties.SESSION_CUSTOMIZER");
                                map.put(PersistenceUnitProperties.INCLUDE_DESCRIPTOR_QUERIES, "PersistenceUnitProperties.INCLUDE_DESCRIPTOR_QUERIES");
                                map.put("eclipselink.concurrency.manager.maxfrequencytodumptinymessage", "concurrency.manager.maxfrequencytodumptinymessage");
                                map.put(PersistenceUnitProperties.WEAVING_FETCHGROUPS, "PersistenceUnitProperties.WEAVING_FETCHGROUPS");
                                map.put(PersistenceUnitProperties.PERSISTENCE_CONTEXT_CLOSE_ON_COMMIT, "PersistenceUnitProperties.PERSISTENCE_CONTEXT_CLOSE_ON_COMMIT");
                                map.put(PersistenceUnitProperties.COORDINATION_RMI_MULTICAST_GROUP, "PersistenceUnitProperties.COORDINATION_RMI_MULTICAST_GROUP");
                                map.put("eclipselink.concurrency.manager.maxsleeptime", "concurrency.manager.maxsleeptime");
                                map.put(PersistenceUnitProperties.DDL_GENERATION_MODE, "PersistenceUnitProperties.DDL_GENERATION_MODE");
                                map.put("eclipselink.nosql.property", "nosql.property");
                                map.put("eclipselink.concurrency.manager.allow.interruptedexception", "concurrency.manager.allow.interruptedexception");
                                map.put("eclipselink.connection-pool.read", "connection-pool.read");
                                map.put(PersistenceUnitProperties.METADATA_SOURCE, "PersistenceUnitProperties.METADATA_SOURCE");
                                map.put(PersistenceUnitProperties.EXCLUSIVE_CONNECTION_IS_LAZY, "PersistenceUnitProperties.EXCLUSIVE_CONNECTION_IS_LAZY");
                                map.put(PersistenceUnitProperties.DROP_JDBC_DDL_FILE, "PersistenceUnitProperties.DROP_JDBC_DDL_FILE");
                                map.put(PersistenceUnitProperties.CACHE_SHARED_DEFAULT, "PersistenceUnitProperties.CACHE_SHARED_DEFAULT");
                                map.put(PersistenceUnitProperties.ID_VALIDATION, "PersistenceUnitProperties.ID_VALIDATION");
                                map.put(PersistenceUnitProperties.COORDINATION_RMI_PACKET_TIME_TO_LIVE, "PersistenceUnitProperties.COORDINATION_RMI_PACKET_TIME_TO_LIVE");
                                map.put(PersistenceUnitProperties.WEAVING, "PersistenceUnitProperties.WEAVING");
                                map.put(PersistenceUnitProperties.COORDINATION_ASYNCH, "PersistenceUnitProperties.COORDINATION_ASYNCH");
                                map.put(PersistenceUnitProperties.ORM_SCHEMA_VALIDATION, "PersistenceUnitProperties.ORM_SCHEMA_VALIDATION");
                                map.put(PersistenceUnitProperties.TARGET_SERVER, "PersistenceUnitProperties.TARGET_SERVER");
                                map.put(PersistenceUnitProperties.COORDINATION_RMI_URL, "PersistenceUnitProperties.COORDINATION_RMI_URL");
                                map.put(PersistenceUnitProperties.COORDINATION_JMS_REUSE_PUBLISHER, "PersistenceUnitProperties.COORDINATION_JMS_REUSE_PUBLISHER");
                                map.put(PersistenceUnitProperties.PROFILER, "PersistenceUnitProperties.PROFILER");
                                map.put("eclipselink.concurrency.manager.maxfrequencytodumpmassivemessage", "concurrency.manager.maxfrequencytodumpmassivemessage");
                                map.put(PersistenceUnitProperties.DDL_GENERATION, "PersistenceUnitProperties.DDL_GENERATION");
                                map.put(PersistenceUnitProperties.UPPERCASE_COLUMN_NAMES, "PersistenceUnitProperties.UPPERCASE_COLUMN_NAMES");
                                map.put("eclipselink.concurrency.manager.allow.readlockstacktrace", "concurrency.manager.allow.readlockstacktrace");
                                map.put("eclipselink.cache.type", "cache.type");
                                map.put(PersistenceUnitProperties.PARTITIONING_CALLBACK, "PersistenceUnitProperties.PARTITIONING_CALLBACK");
                                map.put("eclipselink.connection-pool", "connection-pool");
                                map.put(PersistenceUnitProperties.EXCLUSIVE_CONNECTION_MODE, "PersistenceUnitProperties.EXCLUSIVE_CONNECTION_MODE");
                                map.put(PersistenceUnitProperties.NATIVE_QUERY_UPPERCASE_COLUMNS, "PersistenceUnitProperties.NATIVE_QUERY_UPPERCASE_COLUMNS");
                                map.put(PersistenceUnitProperties.CLASSLOADER, "PersistenceUnitProperties.CLASSLOADER");
                                map.put(PersistenceUnitProperties.TEMPORAL_MUTABLE, "PersistenceUnitProperties.TEMPORAL_MUTABLE");
                                map.put(PersistenceUnitProperties.ECLIPSELINK_PERSISTENCE_UNITS, "PersistenceUnitProperties.ECLIPSELINK_PERSISTENCE_UNITS");
                                map.put(PersistenceUnitProperties.PERSISTENCE_CONTEXT_COMMIT_WITHOUT_PERSIST_RULES, "PersistenceUnitProperties.PERSISTENCE_CONTEXT_COMMIT_WITHOUT_PERSIST_RULES");
                                map.put(PersistenceUnitProperties.LOGGING_SESSION, "PersistenceUnitProperties.LOGGING_SESSION");
                                map.put(PersistenceUnitProperties.SESSION_EVENT_LISTENER_CLASS, "PersistenceUnitProperties.SESSION_EVENT_LISTENER_CLASS");
                                map.put("eclipselink.connection-pool.sequence", "connection-pool.sequence");
                                map.put(PersistenceUnitProperties.ALLOW_NATIVE_SQL_QUERIES, "PersistenceUnitProperties.ALLOW_NATIVE_SQL_QUERIES");
                                map.put(PersistenceUnitProperties.PARTITIONING, "PersistenceUnitProperties.PARTITIONING");
                                map.put(PersistenceUnitProperties.TUNING, "PersistenceUnitProperties.TUNING");
                                map.put(PersistenceUnitProperties.EXCLUDE_ECLIPSELINK_ORM_FILE, "PersistenceUnitProperties.EXCLUDE_ECLIPSELINK_ORM_FILE");
                                map.put(PersistenceUnitProperties.APP_LOCATION, "PersistenceUnitProperties.APP_LOCATION");
                                map.put(PersistenceUnitProperties.COORDINATION_JMS_TOPIC, "PersistenceUnitProperties.COORDINATION_JMS_TOPIC");
                                map.put(PersistenceUnitProperties.NATIVE_SQL, "PersistenceUnitProperties.NATIVE_SQL");
                                map.put(PersistenceUnitProperties.COORDINATION_RMI_MULTICAST_GROUP_PORT, "PersistenceUnitProperties.COORDINATION_RMI_MULTICAST_GROUP_PORT");
                                map.put(PersistenceUnitProperties.DEPLOY_ON_STARTUP, "PersistenceUnitProperties.DEPLOY_ON_STARTUP");
                                map.put(PersistenceUnitProperties.LOGGING_LEVEL, "PersistenceUnitProperties.LOGGING_LEVEL");
                                map.put(PersistenceUnitProperties.NOSQL_CONNECTION_FACTORY, "PersistenceUnitProperties.NOSQL_CONNECTION_FACTORY");
                                map.put(PersistenceUnitProperties.BATCH_WRITING_SIZE, "PersistenceUnitProperties.BATCH_WRITING_SIZE");
                                map.put(PersistenceUnitProperties.LOGGING_THREAD, "PersistenceUnitProperties.LOGGING_THREAD");
                                map.put(PersistenceUnitProperties.SESSIONS_XML, "PersistenceUnitProperties.SESSIONS_XML");
                                map.put(PersistenceUnitProperties.COMPOSITE_UNIT_PROPERTIES, "PersistenceUnitProperties.COMPOSITE_UNIT_PROPERTIES");
                                map.put(PersistenceUnitProperties.METADATA_SOURCE_PROPERTIES_FILE, "PersistenceUnitProperties.METADATA_SOURCE_PROPERTIES_FILE");
                                map.put(PersistenceUnitProperties.PERSISTENCE_CONTEXT_PERSIST_ON_COMMIT, "PersistenceUnitProperties.PERSISTENCE_CONTEXT_PERSIST_ON_COMMIT");
                                map.put(PersistenceUnitProperties.COORDINATION_RMI_ANNOUNCEMENT_DELAY, "PersistenceUnitProperties.COORDINATION_RMI_ANNOUNCEMENT_DELAY");
                                map.put(PersistenceUnitProperties.ORACLE_PROXY_TYPE, "PersistenceUnitProperties.ORACLE_PROXY_TYPE");
                                map.put(PersistenceUnitProperties.COORDINATION_JMS_FACTORY, "PersistenceUnitProperties.COORDINATION_JMS_FACTORY");
                                map.put(PersistenceUnitProperties.NOSQL_CONNECTION_SPEC, "PersistenceUnitProperties.NOSQL_CONNECTION_SPEC");
                                map.put(PersistenceUnitProperties.PERSISTENCE_CONTEXT_FLUSH_MODE, "PersistenceUnitProperties.PERSISTENCE_CONTEXT_FLUSH_MODE");
                                map.put("eclipselink.cache.size", "cache.size");
                                map.put(PersistenceUnitProperties.EXCEPTION_HANDLER_CLASS, "PersistenceUnitProperties.EXCEPTION_HANDLER_CLASS");
                                map.put("eclipselink.descriptor.customizer", "descriptor.customizer");
                                map.put(PersistenceUnitProperties.FLUSH_CLEAR_CACHE, "PersistenceUnitProperties.FLUSH_CLEAR_CACHE");
                                map.put("eclipselink.jdbc.property", "jdbc.property");
                                map.put(PersistenceUnitProperties.JOIN_EXISTING_TRANSACTION, "PersistenceUnitProperties.JOIN_EXISTING_TRANSACTION");
                                map.put(PersistenceUnitProperties.COORDINATION_JMS_HOST, "PersistenceUnitProperties.COORDINATION_JMS_HOST");
                                map.put(PersistenceUnitProperties.WEAVING_INTERNAL, "PersistenceUnitProperties.WEAVING_INTERNAL");
                                map.put(PersistenceUnitProperties.VALIDATION_ONLY_PROPERTY, "PersistenceUnitProperties.VALIDATION_ONLY_PROPERTY");
                                map.put("eclipselink.ddl.table-creation-suffix", "ddl.table-creation-suffix");
                                map.put(PersistenceUnitProperties.COORDINATION_REMOVE_CONNECTION, "PersistenceUnitProperties.COORDINATION_REMOVE_CONNECTION");
                                map.put(PersistenceUnitProperties.JDBC_CONNECTOR, "PersistenceUnitProperties.JDBC_CONNECTOR");
                                map.put(PersistenceUnitProperties.METADATA_SOURCE_XML_URL, "PersistenceUnitProperties.METADATA_SOURCE_XML_URL");
                                map.put(PersistenceUnitProperties.COORDINATION_CHANNEL, "PersistenceUnitProperties.COORDINATION_CHANNEL");
                                map.put(PersistenceUnitProperties.CACHE_STATEMENTS_SIZE, "PersistenceUnitProperties.CACHE_STATEMENTS_SIZE");
                                                
                                return map;
                            }
                                                
                            @Bean
                            public InstrumentationLoadTimeWeaver loadTimeWeaver() throws Throwable {
                                InstrumentationLoadTimeWeaver loadTimeWeaver = new InstrumentationLoadTimeWeaver();
                                return loadTimeWeaver;
                            }
                        }"""
        );
    }

}