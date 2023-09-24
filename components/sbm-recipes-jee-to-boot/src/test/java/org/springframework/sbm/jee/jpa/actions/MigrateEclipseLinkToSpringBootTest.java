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
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                "         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                "    <modelVersion>4.0.0</modelVersion>\n" +
                "    <groupId>org.example</groupId>\n" +
                "    <artifactId>sbm-testproject-jee-eclipselink</artifactId>\n" +
                "    <version>1.0-SNAPSHOT</version>\n" +
                "    <properties>\n" +
                "        <maven.compiler.source>11</maven.compiler.source>\n" +
                "        <maven.compiler.target>11</maven.compiler.target>\n" +
                "    </properties>\n" +
                "    <dependencies>\n" +
                "        <dependency>\n" +
                "            <groupId>org.eclipse.persistence</groupId>\n" +
                "            <artifactId>org.eclipse.persistence.jpa</artifactId>\n" +
                "            <version>2.7.10</version>\n" +
                "        </dependency>\n" +
                "    </dependencies>\n" +
                "</project>";

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withMavenRootBuildFileSource(pomXml)
                .build();

        sut.apply(projectContext);

        assertThat(projectContext.getApplicationModules().getRootModule().getBuildFile().print()).isEqualTo(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                "         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                "    <modelVersion>4.0.0</modelVersion>\n" +
                "    <groupId>org.example</groupId>\n" +
                "    <artifactId>sbm-testproject-jee-eclipselink</artifactId>\n" +
                "    <version>1.0-SNAPSHOT</version>\n" +
                "    <properties>\n" +
                "        <maven.compiler.source>11</maven.compiler.source>\n" +
                "        <maven.compiler.target>11</maven.compiler.target>\n" +
                "    </properties>\n" +
                "    <dependencies>\n" +
                "        <dependency>\n" +
                "            <groupId>org.eclipse.persistence</groupId>\n" +
                "            <artifactId>org.eclipse.persistence.jpa</artifactId>\n" +
                "            <version>2.7.10</version>\n" +
                "        </dependency>\n" +
                "        <dependency>\n" +
                "            <groupId>org.springframework.boot</groupId>\n" +
                "            <artifactId>spring-boot-starter-data-jpa</artifactId>\n" +
                "            <version>2.5.9</version>\n" +
                "            <exclusions>\n" +
                "                <exclusion>\n" +
                "                    <groupId>org.hibernate</groupId>\n" +
                "                    <artifactId>hibernate-core</artifactId>\n" +
                "                </exclusion>\n" +
                "            </exclusions>\n" +
                "        </dependency>\n" +
                "    </dependencies>\n" +
                "</project>"
        );
    }

    @Test
    void shouldAddExcludesToSpringBootStarterDataJpaIfExists() {
        String pomXml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                        "         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                        "    <modelVersion>4.0.0</modelVersion>\n" +
                        "    <groupId>org.example</groupId>\n" +
                        "    <artifactId>sbm-testproject-jee-eclipselink</artifactId>\n" +
                        "    <version>1.0-SNAPSHOT</version>\n" +
                        "    <properties>\n" +
                        "        <maven.compiler.source>11</maven.compiler.source>\n" +
                        "        <maven.compiler.target>11</maven.compiler.target>\n" +
                        "    </properties>\n" +
                        "    <dependencies>\n" +
                        "        <dependency>\n" +
                        "            <groupId>org.eclipse.persistence</groupId>\n" +
                        "            <artifactId>org.eclipse.persistence.jpa</artifactId>\n" +
                        "            <version>2.7.10</version>\n" +
                        "        </dependency>\n" +
                        "        <dependency>\n" +
                        "            <groupId>org.springframework.boot</groupId>\n" +
                        "            <artifactId>spring-boot-starter-data-jpa</artifactId>\n" +
                        "            <version>2.5.9</version>\n" +
                        "        </dependency>\n" +
                        "    </dependencies>\n" +
                        "</project>";

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withMavenRootBuildFileSource(pomXml)
                .build();

        sut.apply(projectContext);

        assertThat(projectContext.getApplicationModules().getRootModule().getBuildFile().print()).isEqualTo(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                "         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                "    <modelVersion>4.0.0</modelVersion>\n" +
                "    <groupId>org.example</groupId>\n" +
                "    <artifactId>sbm-testproject-jee-eclipselink</artifactId>\n" +
                "    <version>1.0-SNAPSHOT</version>\n" +
                "    <properties>\n" +
                "        <maven.compiler.source>11</maven.compiler.source>\n" +
                "        <maven.compiler.target>11</maven.compiler.target>\n" +
                "    </properties>\n" +
                "    <dependencies>\n" +
                "        <dependency>\n" +
                "            <groupId>org.eclipse.persistence</groupId>\n" +
                "            <artifactId>org.eclipse.persistence.jpa</artifactId>\n" +
                "            <version>2.7.10</version>\n" +
                "        </dependency>\n" +
                "        <dependency>\n" +
                "            <groupId>org.springframework.boot</groupId>\n" +
                "            <artifactId>spring-boot-starter-data-jpa</artifactId>\n" +
                "            <version>2.5.9</version>\n" +
                "            <exclusions>\n" +
                "                <exclusion>\n" +
                "                    <groupId>org.hibernate</groupId>\n" +
                "                    <artifactId>hibernate-core</artifactId>\n" +
                "                </exclusion>\n" +
                "            </exclusions>\n" +
                "        </dependency>\n" +
                "    </dependencies>\n" +
                "</project>"
        );
    }

    @Test
    void shouldAddEclipseLinkConfigurationClass() {
        String pomXml =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                "         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                "    <modelVersion>4.0.0</modelVersion>\n" +
                "    <groupId>org.example</groupId>\n" +
                "    <artifactId>sbm-testproject-jee-eclipselink</artifactId>\n" +
                "    <version>1.0-SNAPSHOT</version>\n" +
                "    <properties>\n" +
                "        <maven.compiler.source>11</maven.compiler.source>\n" +
                "        <maven.compiler.target>11</maven.compiler.target>\n" +
                "    </properties>\n" +
                "    <dependencies>\n" +
                "        <dependency>\n" +
                "            <groupId>org.eclipse.persistence</groupId>\n" +
                "            <artifactId>org.eclipse.persistence.jpa</artifactId>\n" +
                "            <version>2.7.10</version>\n" +
                "        </dependency>\n" +
                "    </dependencies>\n" +
                "</project>";

        String persistenceXml =
                "<persistence xmlns=\"http://java.sun.com/xml/ns/persistence\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://java.sun.com/xml/ns/persistence persistence_1_0.xsd\" version=\"1.0\">\n" +
                "    <persistence-unit name=\"my-app\" transaction-type=\"RESOURCE_LOCAL\">\n" +
                "        <provider>org.eclipse.persistence.jpa.PersistenceProvider</provider>\n" +
                "        <exclude-unlisted-classes>false</exclude-unlisted-classes>\n" +
                "        <properties>\n" +
                "            <property name=\"javax.persistence.jdbc.driver\" value=\"oracle.jdbc.OracleDriver\"/>\n" +
                "            <property name=\"javax.persistence.jdbc.url\" value=\"jdbc:oracle:thin:@localhost:1521:orcl\"/>\n" +
                "            <property name=\"javax.persistence.jdbc.user\" value=\"scott\"/>\n" +
                "            <property name=\"javax.persistence.jdbc.password\" value=\"tiger\"/>\n" +
                "            <property name=\"eclipselink.application-location\" value=\"PersistenceUnitProperties.APP_LOCATION\"/>\n" +
                "            <property name=\"eclipselink.cache.coordination.channel\" value=\"PersistenceUnitProperties.COORDINATION_CHANNEL\"/>\n" +
                "            <property name=\"eclipselink.cache.coordination.jms.factory\" value=\"PersistenceUnitProperties.COORDINATION_JMS_FACTORY\"/>\n" +
                "            <property name=\"eclipselink.cache.coordination.jms.host\" value=\"PersistenceUnitProperties.COORDINATION_JMS_HOST\"/>\n" +
                "            <property name=\"eclipselink.cache.coordination.jms.reuse-topic-publisher\" value=\"PersistenceUnitProperties.COORDINATION_JMS_REUSE_PUBLISHER\"/>\n" +
                "            <property name=\"eclipselink.cache.coordination.jms.topic\" value=\"PersistenceUnitProperties.COORDINATION_JMS_TOPIC\"/>\n" +
                "            <property name=\"eclipselink.cache.coordination.jndi.initial-context-factory\" value=\"PersistenceUnitProperties.COORDINATION_JNDI_CONTEXT\"/>\n" +
                "            <property name=\"eclipselink.cache.coordination.jndi.password\" value=\"PersistenceUnitProperties.COORDINATION_JNDI_PASSWORD\"/>\n" +
                "            <property name=\"eclipselink.cache.coordination.jndi.user\" value=\"PersistenceUnitProperties.COORDINATION_JNDI_USER\"/>\n" +
                "            <property name=\"eclipselink.cache.coordination.naming-service\" value=\"PersistenceUnitProperties.COORDINATION_NAMING_SERVICE\"/>\n" +
                "            <property name=\"eclipselink.cache.coordination.propagate-asynchronously\" value=\"PersistenceUnitProperties.COORDINATION_ASYNCH\"/>\n" +
                "            <property name=\"eclipselink.cache.coordination.protocol\" value=\"PersistenceUnitProperties.COORDINATION_PROTOCOL\"/>\n" +
                "            <property name=\"eclipselink.cache.coordination.remove-connection-on-error\" value=\"PersistenceUnitProperties.COORDINATION_REMOVE_CONNECTION\"/>\n" +
                "            <property name=\"eclipselink.cache.coordination.rmi.announcement-delay\" value=\"PersistenceUnitProperties.COORDINATION_RMI_ANNOUNCEMENT_DELAY\"/>\n" +
                "            <property name=\"eclipselink.cache.coordination.rmi.multicast-group\" value=\"PersistenceUnitProperties.COORDINATION_RMI_MULTICAST_GROUP\"/>\n" +
                "            <property name=\"eclipselink.cache.coordination.rmi.multicast-group.port\" value=\"PersistenceUnitProperties.COORDINATION_RMI_MULTICAST_GROUP_PORT\"/>\n" +
                "            <property name=\"eclipselink.cache.coordination.rmi.packet-time-to-live\" value=\"PersistenceUnitProperties.COORDINATION_RMI_PACKET_TIME_TO_LIVE\"/>\n" +
                "            <property name=\"eclipselink.cache.coordination.rmi.url\" value=\"PersistenceUnitProperties.COORDINATION_RMI_URL\"/>\n" +
                "            <property name=\"eclipselink.cache.coordination.thread.pool.size\" value=\"PersistenceUnitProperties.COORDINATION_THREAD_POOL_SIZE\"/>\n" +
                "            <property name=\"eclipselink.cache.database-event-listener\" value=\"PersistenceUnitProperties.DATABASE_EVENT_LISTENER\"/>\n" +
                "            <property name=\"eclipselink.cache.shared.default\" value=\"PersistenceUnitProperties.CACHE_SHARED_DEFAULT\"/>\n" +
                "            <property name=\"eclipselink.cache.size\" value=\"cache.size\"/>\n" +
                "            <property name=\"eclipselink.cache.type\" value=\"cache.type\"/>\n" +
                "            <property name=\"eclipselink.classloader\" value=\"PersistenceUnitProperties.CLASSLOADER\"/>\n" +
                "            <property name=\"eclipselink.composite-unit\" value=\"PersistenceUnitProperties.COMPOSITE_UNIT\"/>\n" +
                "            <property name=\"eclipselink.composite-unit.member\" value=\"PersistenceUnitProperties.COMPOSITE_UNIT_MEMBER\"/>\n" +
                "            <property name=\"eclipselink.composite-unit.properties\" value=\"PersistenceUnitProperties.COMPOSITE_UNIT_PROPERTIES\"/>\n" +
                "            <property name=\"eclipselink.concurrency.manager.waittime\" value=\"concurrency.manager.waittime\"/>\n" +
                "            <property name=\"eclipselink.concurrency.manager.maxsleeptime\" value=\"concurrency.manager.maxsleeptime\"/>\n" +
                "            <property name=\"eclipselink.concurrency.manager.maxfrequencytodumptinymessage\" value=\"concurrency.manager.maxfrequencytodumptinymessage\"/>\n" +
                "            <property name=\"eclipselink.concurrency.manager.maxfrequencytodumpmassivemessage\" value=\"concurrency.manager.maxfrequencytodumpmassivemessage\"/>\n" +
                "            <property name=\"eclipselink.concurrency.manager.allow.interruptedexception\" value=\"concurrency.manager.allow.interruptedexception\"/>\n" +
                "            <property name=\"eclipselink.concurrency.manager.allow.concurrencyexception\" value=\"concurrency.manager.allow.concurrencyexception\"/>\n" +
                "            <property name=\"eclipselink.concurrency.manager.allow.readlockstacktrace\" value=\"concurrency.manager.allow.readlockstacktrace\"/>\n" +
                "            <property name=\"eclipselink.connection-pool\" value=\"connection-pool\"/>\n" +
                "            <property name=\"eclipselink.connection-pool.read\" value=\"connection-pool.read\"/>\n" +
                "            <property name=\"eclipselink.connection-pool.sequence\" value=\"connection-pool.sequence\"/>\n" +
                "            <property name=\"eclipselink.create-ddl-jdbc-file-name\" value=\"PersistenceUnitProperties.CREATE_JDBC_DDL_FILE\"/>\n" +
                "            <property name=\"eclipselink.ddl-generation\" value=\"PersistenceUnitProperties.DDL_GENERATION\"/>\n" +
                "            <property name=\"eclipselink.ddl-generation.output-mode\" value=\"PersistenceUnitProperties.DDL_GENERATION_MODE\"/>\n" +
                "            <property name=\"eclipselink.ddl.table-creation-suffix\" value=\"ddl.table-creation-suffix\"/>\n" +
                "            <property name=\"eclipselink.deploy-on-startup\" value=\"PersistenceUnitProperties.DEPLOY_ON_STARTUP\"/>\n" +
                "            <property name=\"eclipselink.descriptor.customizer\" value=\"descriptor.customizer\"/>\n" +
                "            <property name=\"eclipselink.drop-ddl-jdbc-file-name\" value=\"PersistenceUnitProperties.DROP_JDBC_DDL_FILE\"/>\n" +
                "            <property name=\"eclipselink.exception-handler\" value=\"PersistenceUnitProperties.EXCEPTION_HANDLER_CLASS\"/>\n" +
                "            <property name=\"eclipselink.exclude-eclipselink-orm\" value=\"PersistenceUnitProperties.EXCLUDE_ECLIPSELINK_ORM_FILE\"/>\n" +
                "            <property name=\"eclipselink.flush-clear.cache\" value=\"PersistenceUnitProperties.FLUSH_CLEAR_CACHE\"/>\n" +
                "            <property name=\"eclipselink.id-validation\" value=\"PersistenceUnitProperties.ID_VALIDATION\"/>\n" +
                "            <property name=\"eclipselink.jdbc.allow-native-sql-queries\" value=\"PersistenceUnitProperties.ALLOW_NATIVE_SQL_QUERIES\"/>\n" +
                "            <property name=\"eclipselink.jdbc.batch-writing\" value=\"PersistenceUnitProperties.BATCH_WRITING\"/>\n" +
                "            <property name=\"eclipselink.jdbc.batch-writing.size\" value=\"PersistenceUnitProperties.BATCH_WRITING_SIZE\"/>\n" +
                "            <property name=\"eclipselink.jdbc.cache-statements\" value=\"PersistenceUnitProperties.CACHE_STATEMENTS\"/>\n" +
                "            <property name=\"eclipselink.jdbc.cache-statements.size\" value=\"PersistenceUnitProperties.CACHE_STATEMENTS_SIZE\"/>\n" +
                "            <property name=\"eclipselink.jdbc.connector\" value=\"PersistenceUnitProperties.JDBC_CONNECTOR\"/>\n" +
                "            <property name=\"eclipselink.jdbc.exclusive-connection.is-lazy\" value=\"PersistenceUnitProperties.EXCLUSIVE_CONNECTION_IS_LAZY\"/>\n" +
                "            <property name=\"eclipselink.jdbc.exclusive-connection.mode\" value=\"PersistenceUnitProperties.EXCLUSIVE_CONNECTION_MODE\"/>\n" +
                "            <property name=\"eclipselink.jdbc.native-sql\" value=\"PersistenceUnitProperties.NATIVE_SQL\"/>\n" +
                "            <property name=\"eclipselink.jdbc.property\" value=\"jdbc.property\"/>\n" +
                "            <property name=\"eclipselink.jdbc.sql-cast\" value=\"PersistenceUnitProperties.SQL_CAST\"/>\n" +
                "            <property name=\"eclipselink.jdbc.uppercase-columns\" value=\"PersistenceUnitProperties.NATIVE_QUERY_UPPERCASE_COLUMNS\"/>\n" +
                "            <property name=\"eclipselink.jpa.uppercase-column-names\" value=\"PersistenceUnitProperties.UPPERCASE_COLUMN_NAMES\"/>\n" +
                "            <property name=\"eclipselink.jpql.parser\" value=\"PersistenceUnitProperties.JPQL_PARSER\"/>\n" +
                "            <property name=\"eclipselink.jpql.validation\" value=\"PersistenceUnitProperties.JPQL_VALIDATION\"/>\n" +
                "            <property name=\"eclipselink.logging.connection\" value=\"PersistenceUnitProperties.LOGGING_CONNECTION\"/>\n" +
                "            <property name=\"eclipselink.logging.exceptions\" value=\"PersistenceUnitProperties.LOGGING_EXCEPTIONS\"/>\n" +
                "            <property name=\"eclipselink.logging.file\" value=\"PersistenceUnitProperties.LOGGING_FILE\"/>\n" +
                "            <property name=\"eclipselink.logging.level\" value=\"PersistenceUnitProperties.LOGGING_LEVEL\"/>\n" +
                "            <property name=\"eclipselink.logging.session\" value=\"PersistenceUnitProperties.LOGGING_SESSION\"/>\n" +
                "            <property name=\"eclipselink.logging.thread\" value=\"PersistenceUnitProperties.LOGGING_THREAD\"/>\n" +
                "            <property name=\"eclipselink.logging.timestamp\" value=\"PersistenceUnitProperties.LOGGING_TIMESTAMP\"/>\n" +
                "            <property name=\"eclipselink.metadata-source\" value=\"PersistenceUnitProperties.METADATA_SOURCE\"/>\n" +
                "            <property name=\"eclipselink.metadata-source.properties.file\" value=\"PersistenceUnitProperties.METADATA_SOURCE_PROPERTIES_FILE\"/>\n" +
                "            <property name=\"eclipselink.metadata-source.send-refresh-command\" value=\"PersistenceUnitProperties.METADATA_SOURCE_RCM_COMMAND\"/>\n" +
                "            <property name=\"eclipselink.metadata-source.xml.url\" value=\"PersistenceUnitProperties.METADATA_SOURCE_XML_URL\"/>\n" +
                "            <property name=\"eclipselink.nosql.connection-factory\" value=\"PersistenceUnitProperties.NOSQL_CONNECTION_FACTORY\"/>\n" +
                "            <property name=\"eclipselink.nosql.connection-spec\" value=\"PersistenceUnitProperties.NOSQL_CONNECTION_SPEC\"/>\n" +
                "            <property name=\"eclipselink.nosql.property\" value=\"nosql.property\"/>\n" +
                "            <property name=\"eclipselink.oracle.proxy-type\" value=\"PersistenceUnitProperties.ORACLE_PROXY_TYPE\"/>\n" +
                "            <property name=\"eclipselink.orm.throw.exceptions\" value=\"PersistenceUnitProperties.THROW_EXCEPTIONS\"/>\n" +
                "            <property name=\"eclipselink.orm.validate.schema\" value=\"PersistenceUnitProperties.ORM_SCHEMA_VALIDATION\"/>\n" +
                "            <property name=\"eclipselink.partitioning\" value=\"PersistenceUnitProperties.PARTITIONING\"/>\n" +
                "            <property name=\"eclipselink.partitioning.callback\" value=\"PersistenceUnitProperties.PARTITIONING_CALLBACK\"/>\n" +
                "            <property name=\"eclipselink.persistence-context.close-on-commit\" value=\"PersistenceUnitProperties.PERSISTENCE_CONTEXT_CLOSE_ON_COMMIT\"/>\n" +
                "            <property name=\"eclipselink.persistence-context.commit-without-persist-rules\" value=\"PersistenceUnitProperties.PERSISTENCE_CONTEXT_COMMIT_WITHOUT_PERSIST_RULES\"/>\n" +
                "            <property name=\"eclipselink.persistence-context.flush-mode\" value=\"PersistenceUnitProperties.PERSISTENCE_CONTEXT_FLUSH_MODE\"/>\n" +
                "            <property name=\"eclipselink.persistence-context.persist-on-commit\" value=\"PersistenceUnitProperties.PERSISTENCE_CONTEXT_PERSIST_ON_COMMIT\"/>\n" +
                "            <property name=\"eclipselink.persistence-context.reference-mode\" value=\"PersistenceUnitProperties.PERSISTENCE_CONTEXT_REFERENCE_MODE\"/>\n" +
                "            <property name=\"eclipselink.persistenceunits\" value=\"PersistenceUnitProperties.ECLIPSELINK_PERSISTENCE_UNITS\"/>\n" +
                "            <property name=\"eclipselink.persistencexml\" value=\"PersistenceUnitProperties.ECLIPSELINK_PERSISTENCE_XML\"/>\n" +
                "            <property name=\"eclipselink.persisencexml.default\" value=\"persisencexml.default\"/>\n" +
                "            <property name=\"eclipselink.profiler\" value=\"PersistenceUnitProperties.PROFILER\"/>\n" +
                "            <property name=\"eclipselink.session.customizer\" value=\"PersistenceUnitProperties.SESSION_CUSTOMIZER\"/>\n" +
                "            <property name=\"eclipselink.session.include.descriptor.queries\" value=\"PersistenceUnitProperties.INCLUDE_DESCRIPTOR_QUERIES\"/>\n" +
                "            <property name=\"eclipselink.session-event-listener\" value=\"PersistenceUnitProperties.SESSION_EVENT_LISTENER_CLASS\"/>\n" +
                "            <property name=\"eclipselink.session-name\" value=\"PersistenceUnitProperties.SESSION_NAME\"/>\n" +
                "            <property name=\"eclipselink.sessions-xml\" value=\"PersistenceUnitProperties.SESSIONS_XML\"/>\n" +
                "            <property name=\"eclipselink.target-database\" value=\"PersistenceUnitProperties.TARGET_DATABASE\"/>\n" +
                "            <property name=\"eclipselink.target-server\" value=\"PersistenceUnitProperties.TARGET_SERVER\"/>\n" +
                "            <property name=\"eclipselink.temporal.mutable\" value=\"PersistenceUnitProperties.TEMPORAL_MUTABLE\"/>\n" +
                "            <property name=\"eclipselink.tenant-id\" value=\"PersistenceUnitProperties.MULTITENANT_PROPERTY_DEFAULT\"/>\n" +
                "            <property name=\"eclipselink.transaction.join-existing\" value=\"PersistenceUnitProperties.JOIN_EXISTING_TRANSACTION\"/>\n" +
                "            <property name=\"eclipselink.tuning\" value=\"PersistenceUnitProperties.TUNING\"/>\n" +
                "            <property name=\"eclipselink.validate-existence\" value=\"PersistenceUnitProperties.VALIDATE_EXISTENCE\"/>\n" +
                "            <property name=\"eclipselink.validation-only\" value=\"PersistenceUnitProperties.VALIDATION_ONLY_PROPERTY\"/>\n" +
                "            <property name=\"eclipselink.weaving\" value=\"PersistenceUnitProperties.WEAVING\"/>\n" +
                "            <property name=\"eclipselink.weaving.changetracking\" value=\"PersistenceUnitProperties.WEAVING_CHANGE_TRACKING\"/>\n" +
                "            <property name=\"eclipselink.weaving.eager\" value=\"PersistenceUnitProperties.WEAVING_EAGER\"/>\n" +
                "            <property name=\"eclipselink.weaving.fetchgroups\" value=\"PersistenceUnitProperties.WEAVING_FETCHGROUPS\"/>\n" +
                "            <property name=\"eclipselink.weaving.internal\" value=\"PersistenceUnitProperties.WEAVING_INTERNAL\"/>\n" +
                "            <property name=\"eclipselink.weaving.lazy\" value=\"PersistenceUnitProperties.WEAVING_LAZY\"/>\n" +
                "        </properties>\n" +
                "    </persistence-unit>\n" +
                "</persistence>";

        ProjectContext projectContext = TestProjectContext.buildProjectContext()
                .withMavenRootBuildFileSource(pomXml)
                .withProjectResource("src/main/resources/META-INF/persistence.xml", persistenceXml)
                .build();

        sut.apply(projectContext);

        assertThat(projectContext.getProjectJavaSources().findJavaSourceDeclaringType("com.vmware.example.EclipseLinkJpaConfiguration")).isNotEmpty();
        assertThat(projectContext.getProjectJavaSources().findJavaSourceDeclaringType("com.vmware.example.EclipseLinkJpaConfiguration").get().print()).isEqualTo(
                "package com.vmware.example;\n" +
                        "\n" +
                        "import org.eclipse.persistence.config.PersistenceUnitProperties;\n" +
                        "import org.eclipse.persistence.logging.SessionLog;\n" +
                        "import org.springframework.beans.factory.ObjectProvider;\n" +
                        "import org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration;\n" +
                        "import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;\n" +
                        "import org.springframework.context.annotation.Bean;\n" +
                        "import org.springframework.context.annotation.Configuration;\n" +
                        "import org.springframework.context.annotation.EnableLoadTimeWeaving;\n" +
                        "import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;\n" +
                        "import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;\n" +
                        "import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;\n" +
                        "import org.springframework.transaction.jta.JtaTransactionManager;\n" +
                        "\n" +
                        "import javax.sql.DataSource;\n" +
                        "import java.util.HashMap;\n" +
                        "import java.util.Map;\n" +
                        "\n" +
                        "@Configuration\n" +
                        "@EnableLoadTimeWeaving(aspectjWeaving = EnableLoadTimeWeaving.AspectJWeaving.ENABLED)\n" +
                        "public class EclipseLinkJpaConfiguration extends JpaBaseConfiguration {\n" +
                        "\n" +
                        "    protected EclipseLinkJpaConfiguration(DataSource dataSource, JpaProperties properties, ObjectProvider<JtaTransactionManager> jtaTransactionManager) {\n" +
                        "        super(dataSource, properties, jtaTransactionManager);\n" +
                        "    }\n" +
                        "\n" +
                        "    @Override\n" +
                        "    protected AbstractJpaVendorAdapter createJpaVendorAdapter() {\n" +
                        "        return new EclipseLinkJpaVendorAdapter();\n" +
                        "    }\n" +
                        "\n" +
                        "    @Override\n" +
                        "    protected Map<String, Object> getVendorProperties() {\n" +
                        "        Map<String, Object> map = new HashMap<>();\n" +
                        "\n" +
                        "        map.put(PersistenceUnitProperties.PERSISTENCE_CONTEXT_REFERENCE_MODE, \"PersistenceUnitProperties.PERSISTENCE_CONTEXT_REFERENCE_MODE\");\n" +
                        "        map.put(PersistenceUnitProperties.VALIDATE_EXISTENCE, \"PersistenceUnitProperties.VALIDATE_EXISTENCE\");\n" +
                        "        map.put(PersistenceUnitProperties.SESSION_NAME, \"PersistenceUnitProperties.SESSION_NAME\");\n" +
                        "        map.put(\"eclipselink.concurrency.manager.waittime\", \"concurrency.manager.waittime\");\n" +
                        "        map.put(PersistenceUnitProperties.COORDINATION_JNDI_CONTEXT, \"PersistenceUnitProperties.COORDINATION_JNDI_CONTEXT\");\n" +
                        "        map.put(PersistenceUnitProperties.COORDINATION_JNDI_PASSWORD, \"PersistenceUnitProperties.COORDINATION_JNDI_PASSWORD\");\n" +
                        "        map.put(PersistenceUnitProperties.COORDINATION_JNDI_USER, \"PersistenceUnitProperties.COORDINATION_JNDI_USER\");\n" +
                        "        map.put(PersistenceUnitProperties.WEAVING_LAZY, \"PersistenceUnitProperties.WEAVING_LAZY\");\n" +
                        "        map.put(PersistenceUnitProperties.JPQL_PARSER, \"PersistenceUnitProperties.JPQL_PARSER\");\n" +
                        "        map.put(\"eclipselink.persisencexml.default\", \"persisencexml.default\");\n" +
                        "        map.put(PersistenceUnitProperties.METADATA_SOURCE_RCM_COMMAND, \"PersistenceUnitProperties.METADATA_SOURCE_RCM_COMMAND\");\n" +
                        "        map.put(PersistenceUnitProperties.BATCH_WRITING, \"PersistenceUnitProperties.BATCH_WRITING\");\n" +
                        "        map.put(PersistenceUnitProperties.CACHE_STATEMENTS, \"PersistenceUnitProperties.CACHE_STATEMENTS\");\n" +
                        "        map.put(PersistenceUnitProperties.COMPOSITE_UNIT, \"PersistenceUnitProperties.COMPOSITE_UNIT\");\n" +
                        "        map.put(PersistenceUnitProperties.JPQL_VALIDATION, \"PersistenceUnitProperties.JPQL_VALIDATION\");\n" +
                        "        map.put(PersistenceUnitProperties.TARGET_DATABASE, \"PersistenceUnitProperties.TARGET_DATABASE\");\n" +
                        "        map.put(PersistenceUnitProperties.COMPOSITE_UNIT_MEMBER, \"PersistenceUnitProperties.COMPOSITE_UNIT_MEMBER\");\n" +
                        "        map.put(PersistenceUnitProperties.THROW_EXCEPTIONS, \"PersistenceUnitProperties.THROW_EXCEPTIONS\");\n" +
                        "        map.put(PersistenceUnitProperties.LOGGING_CONNECTION, \"PersistenceUnitProperties.LOGGING_CONNECTION\");\n" +
                        "        map.put(PersistenceUnitProperties.COORDINATION_NAMING_SERVICE, \"PersistenceUnitProperties.COORDINATION_NAMING_SERVICE\");\n" +
                        "        map.put(PersistenceUnitProperties.COORDINATION_THREAD_POOL_SIZE, \"PersistenceUnitProperties.COORDINATION_THREAD_POOL_SIZE\");\n" +
                        "        map.put(\"eclipselink.concurrency.manager.allow.concurrencyexception\", \"concurrency.manager.allow.concurrencyexception\");\n" +
                        "        map.put(PersistenceUnitProperties.LOGGING_EXCEPTIONS, \"PersistenceUnitProperties.LOGGING_EXCEPTIONS\");\n" +
                        "        map.put(PersistenceUnitProperties.DATABASE_EVENT_LISTENER, \"PersistenceUnitProperties.DATABASE_EVENT_LISTENER\");\n" +
                        "        map.put(PersistenceUnitProperties.WEAVING_EAGER, \"PersistenceUnitProperties.WEAVING_EAGER\");\n" +
                        "        map.put(PersistenceUnitProperties.CREATE_JDBC_DDL_FILE, \"PersistenceUnitProperties.CREATE_JDBC_DDL_FILE\");\n" +
                        "        map.put(PersistenceUnitProperties.ECLIPSELINK_PERSISTENCE_XML, \"PersistenceUnitProperties.ECLIPSELINK_PERSISTENCE_XML\");\n" +
                        "        map.put(PersistenceUnitProperties.MULTITENANT_PROPERTY_DEFAULT, \"PersistenceUnitProperties.MULTITENANT_PROPERTY_DEFAULT\");\n" +
                        "        map.put(PersistenceUnitProperties.LOGGING_FILE, \"PersistenceUnitProperties.LOGGING_FILE\");\n" +
                        "        map.put(PersistenceUnitProperties.LOGGING_TIMESTAMP, \"PersistenceUnitProperties.LOGGING_TIMESTAMP\");\n" +
                        "        map.put(PersistenceUnitProperties.COORDINATION_PROTOCOL, \"PersistenceUnitProperties.COORDINATION_PROTOCOL\");\n" +
                        "        map.put(PersistenceUnitProperties.WEAVING_CHANGE_TRACKING, \"PersistenceUnitProperties.WEAVING_CHANGE_TRACKING\");\n" +
                        "        map.put(PersistenceUnitProperties.SQL_CAST, \"PersistenceUnitProperties.SQL_CAST\");\n" +
                        "        map.put(PersistenceUnitProperties.SESSION_CUSTOMIZER, \"PersistenceUnitProperties.SESSION_CUSTOMIZER\");\n" +
                        "        map.put(PersistenceUnitProperties.INCLUDE_DESCRIPTOR_QUERIES, \"PersistenceUnitProperties.INCLUDE_DESCRIPTOR_QUERIES\");\n" +
                        "        map.put(\"eclipselink.concurrency.manager.maxfrequencytodumptinymessage\", \"concurrency.manager.maxfrequencytodumptinymessage\");\n" +
                        "        map.put(PersistenceUnitProperties.WEAVING_FETCHGROUPS, \"PersistenceUnitProperties.WEAVING_FETCHGROUPS\");\n" +
                        "        map.put(PersistenceUnitProperties.PERSISTENCE_CONTEXT_CLOSE_ON_COMMIT, \"PersistenceUnitProperties.PERSISTENCE_CONTEXT_CLOSE_ON_COMMIT\");\n" +
                        "        map.put(PersistenceUnitProperties.COORDINATION_RMI_MULTICAST_GROUP, \"PersistenceUnitProperties.COORDINATION_RMI_MULTICAST_GROUP\");\n" +
                        "        map.put(\"eclipselink.concurrency.manager.maxsleeptime\", \"concurrency.manager.maxsleeptime\");\n" +
                        "        map.put(PersistenceUnitProperties.DDL_GENERATION_MODE, \"PersistenceUnitProperties.DDL_GENERATION_MODE\");\n" +
                        "        map.put(\"eclipselink.nosql.property\", \"nosql.property\");\n" +
                        "        map.put(\"eclipselink.concurrency.manager.allow.interruptedexception\", \"concurrency.manager.allow.interruptedexception\");\n" +
                        "        map.put(\"eclipselink.connection-pool.read\", \"connection-pool.read\");\n" +
                        "        map.put(PersistenceUnitProperties.METADATA_SOURCE, \"PersistenceUnitProperties.METADATA_SOURCE\");\n" +
                        "        map.put(PersistenceUnitProperties.EXCLUSIVE_CONNECTION_IS_LAZY, \"PersistenceUnitProperties.EXCLUSIVE_CONNECTION_IS_LAZY\");\n" +
                        "        map.put(PersistenceUnitProperties.DROP_JDBC_DDL_FILE, \"PersistenceUnitProperties.DROP_JDBC_DDL_FILE\");\n" +
                        "        map.put(PersistenceUnitProperties.CACHE_SHARED_DEFAULT, \"PersistenceUnitProperties.CACHE_SHARED_DEFAULT\");\n" +
                        "        map.put(PersistenceUnitProperties.ID_VALIDATION, \"PersistenceUnitProperties.ID_VALIDATION\");\n" +
                        "        map.put(PersistenceUnitProperties.COORDINATION_RMI_PACKET_TIME_TO_LIVE, \"PersistenceUnitProperties.COORDINATION_RMI_PACKET_TIME_TO_LIVE\");\n" +
                        "        map.put(PersistenceUnitProperties.WEAVING, \"PersistenceUnitProperties.WEAVING\");\n" +
                        "        map.put(PersistenceUnitProperties.COORDINATION_ASYNCH, \"PersistenceUnitProperties.COORDINATION_ASYNCH\");\n" +
                        "        map.put(PersistenceUnitProperties.ORM_SCHEMA_VALIDATION, \"PersistenceUnitProperties.ORM_SCHEMA_VALIDATION\");\n" +
                        "        map.put(PersistenceUnitProperties.TARGET_SERVER, \"PersistenceUnitProperties.TARGET_SERVER\");\n" +
                        "        map.put(PersistenceUnitProperties.COORDINATION_RMI_URL, \"PersistenceUnitProperties.COORDINATION_RMI_URL\");\n" +
                        "        map.put(PersistenceUnitProperties.COORDINATION_JMS_REUSE_PUBLISHER, \"PersistenceUnitProperties.COORDINATION_JMS_REUSE_PUBLISHER\");\n" +
                        "        map.put(PersistenceUnitProperties.PROFILER, \"PersistenceUnitProperties.PROFILER\");\n" +
                        "        map.put(\"eclipselink.concurrency.manager.maxfrequencytodumpmassivemessage\", \"concurrency.manager.maxfrequencytodumpmassivemessage\");\n" +
                        "        map.put(PersistenceUnitProperties.DDL_GENERATION, \"PersistenceUnitProperties.DDL_GENERATION\");\n" +
                        "        map.put(PersistenceUnitProperties.UPPERCASE_COLUMN_NAMES, \"PersistenceUnitProperties.UPPERCASE_COLUMN_NAMES\");\n" +
                        "        map.put(\"eclipselink.concurrency.manager.allow.readlockstacktrace\", \"concurrency.manager.allow.readlockstacktrace\");\n" +
                        "        map.put(\"eclipselink.cache.type\", \"cache.type\");\n" +
                        "        map.put(PersistenceUnitProperties.PARTITIONING_CALLBACK, \"PersistenceUnitProperties.PARTITIONING_CALLBACK\");\n" +
                        "        map.put(\"eclipselink.connection-pool\", \"connection-pool\");\n" +
                        "        map.put(PersistenceUnitProperties.EXCLUSIVE_CONNECTION_MODE, \"PersistenceUnitProperties.EXCLUSIVE_CONNECTION_MODE\");\n" +
                        "        map.put(PersistenceUnitProperties.NATIVE_QUERY_UPPERCASE_COLUMNS, \"PersistenceUnitProperties.NATIVE_QUERY_UPPERCASE_COLUMNS\");\n" +
                        "        map.put(PersistenceUnitProperties.CLASSLOADER, \"PersistenceUnitProperties.CLASSLOADER\");\n" +
                        "        map.put(PersistenceUnitProperties.TEMPORAL_MUTABLE, \"PersistenceUnitProperties.TEMPORAL_MUTABLE\");\n" +
                        "        map.put(PersistenceUnitProperties.ECLIPSELINK_PERSISTENCE_UNITS, \"PersistenceUnitProperties.ECLIPSELINK_PERSISTENCE_UNITS\");\n" +
                        "        map.put(PersistenceUnitProperties.PERSISTENCE_CONTEXT_COMMIT_WITHOUT_PERSIST_RULES, \"PersistenceUnitProperties.PERSISTENCE_CONTEXT_COMMIT_WITHOUT_PERSIST_RULES\");\n" +
                        "        map.put(PersistenceUnitProperties.LOGGING_SESSION, \"PersistenceUnitProperties.LOGGING_SESSION\");\n" +
                        "        map.put(PersistenceUnitProperties.SESSION_EVENT_LISTENER_CLASS, \"PersistenceUnitProperties.SESSION_EVENT_LISTENER_CLASS\");\n" +
                        "        map.put(\"eclipselink.connection-pool.sequence\", \"connection-pool.sequence\");\n" +
                        "        map.put(PersistenceUnitProperties.ALLOW_NATIVE_SQL_QUERIES, \"PersistenceUnitProperties.ALLOW_NATIVE_SQL_QUERIES\");\n" +
                        "        map.put(PersistenceUnitProperties.PARTITIONING, \"PersistenceUnitProperties.PARTITIONING\");\n" +
                        "        map.put(PersistenceUnitProperties.TUNING, \"PersistenceUnitProperties.TUNING\");\n" +
                        "        map.put(PersistenceUnitProperties.EXCLUDE_ECLIPSELINK_ORM_FILE, \"PersistenceUnitProperties.EXCLUDE_ECLIPSELINK_ORM_FILE\");\n" +
                        "        map.put(PersistenceUnitProperties.APP_LOCATION, \"PersistenceUnitProperties.APP_LOCATION\");\n" +
                        "        map.put(PersistenceUnitProperties.COORDINATION_JMS_TOPIC, \"PersistenceUnitProperties.COORDINATION_JMS_TOPIC\");\n" +
                        "        map.put(PersistenceUnitProperties.NATIVE_SQL, \"PersistenceUnitProperties.NATIVE_SQL\");\n" +
                        "        map.put(PersistenceUnitProperties.COORDINATION_RMI_MULTICAST_GROUP_PORT, \"PersistenceUnitProperties.COORDINATION_RMI_MULTICAST_GROUP_PORT\");\n" +
                        "        map.put(PersistenceUnitProperties.DEPLOY_ON_STARTUP, \"PersistenceUnitProperties.DEPLOY_ON_STARTUP\");\n" +
                        "        map.put(PersistenceUnitProperties.LOGGING_LEVEL, \"PersistenceUnitProperties.LOGGING_LEVEL\");\n" +
                        "        map.put(PersistenceUnitProperties.NOSQL_CONNECTION_FACTORY, \"PersistenceUnitProperties.NOSQL_CONNECTION_FACTORY\");\n" +
                        "        map.put(PersistenceUnitProperties.BATCH_WRITING_SIZE, \"PersistenceUnitProperties.BATCH_WRITING_SIZE\");\n" +
                        "        map.put(PersistenceUnitProperties.LOGGING_THREAD, \"PersistenceUnitProperties.LOGGING_THREAD\");\n" +
                        "        map.put(PersistenceUnitProperties.SESSIONS_XML, \"PersistenceUnitProperties.SESSIONS_XML\");\n" +
                        "        map.put(PersistenceUnitProperties.COMPOSITE_UNIT_PROPERTIES, \"PersistenceUnitProperties.COMPOSITE_UNIT_PROPERTIES\");\n" +
                        "        map.put(PersistenceUnitProperties.METADATA_SOURCE_PROPERTIES_FILE, \"PersistenceUnitProperties.METADATA_SOURCE_PROPERTIES_FILE\");\n" +
                        "        map.put(PersistenceUnitProperties.PERSISTENCE_CONTEXT_PERSIST_ON_COMMIT, \"PersistenceUnitProperties.PERSISTENCE_CONTEXT_PERSIST_ON_COMMIT\");\n" +
                        "        map.put(PersistenceUnitProperties.COORDINATION_RMI_ANNOUNCEMENT_DELAY, \"PersistenceUnitProperties.COORDINATION_RMI_ANNOUNCEMENT_DELAY\");\n" +
                        "        map.put(PersistenceUnitProperties.ORACLE_PROXY_TYPE, \"PersistenceUnitProperties.ORACLE_PROXY_TYPE\");\n" +
                        "        map.put(PersistenceUnitProperties.COORDINATION_JMS_FACTORY, \"PersistenceUnitProperties.COORDINATION_JMS_FACTORY\");\n" +
                        "        map.put(PersistenceUnitProperties.NOSQL_CONNECTION_SPEC, \"PersistenceUnitProperties.NOSQL_CONNECTION_SPEC\");\n" +
                        "        map.put(PersistenceUnitProperties.PERSISTENCE_CONTEXT_FLUSH_MODE, \"PersistenceUnitProperties.PERSISTENCE_CONTEXT_FLUSH_MODE\");\n" +
                        "        map.put(\"eclipselink.cache.size\", \"cache.size\");\n" +
                        "        map.put(PersistenceUnitProperties.EXCEPTION_HANDLER_CLASS, \"PersistenceUnitProperties.EXCEPTION_HANDLER_CLASS\");\n" +
                        "        map.put(\"eclipselink.descriptor.customizer\", \"descriptor.customizer\");\n" +
                        "        map.put(PersistenceUnitProperties.FLUSH_CLEAR_CACHE, \"PersistenceUnitProperties.FLUSH_CLEAR_CACHE\");\n" +
                        "        map.put(\"eclipselink.jdbc.property\", \"jdbc.property\");\n" +
                        "        map.put(PersistenceUnitProperties.JOIN_EXISTING_TRANSACTION, \"PersistenceUnitProperties.JOIN_EXISTING_TRANSACTION\");\n" +
                        "        map.put(PersistenceUnitProperties.COORDINATION_JMS_HOST, \"PersistenceUnitProperties.COORDINATION_JMS_HOST\");\n" +
                        "        map.put(PersistenceUnitProperties.WEAVING_INTERNAL, \"PersistenceUnitProperties.WEAVING_INTERNAL\");\n" +
                        "        map.put(PersistenceUnitProperties.VALIDATION_ONLY_PROPERTY, \"PersistenceUnitProperties.VALIDATION_ONLY_PROPERTY\");\n" +
                        "        map.put(\"eclipselink.ddl.table-creation-suffix\", \"ddl.table-creation-suffix\");\n" +
                        "        map.put(PersistenceUnitProperties.COORDINATION_REMOVE_CONNECTION, \"PersistenceUnitProperties.COORDINATION_REMOVE_CONNECTION\");\n" +
                        "        map.put(PersistenceUnitProperties.JDBC_CONNECTOR, \"PersistenceUnitProperties.JDBC_CONNECTOR\");\n" +
                        "        map.put(PersistenceUnitProperties.METADATA_SOURCE_XML_URL, \"PersistenceUnitProperties.METADATA_SOURCE_XML_URL\");\n" +
                        "        map.put(PersistenceUnitProperties.COORDINATION_CHANNEL, \"PersistenceUnitProperties.COORDINATION_CHANNEL\");\n" +
                        "        map.put(PersistenceUnitProperties.CACHE_STATEMENTS_SIZE, \"PersistenceUnitProperties.CACHE_STATEMENTS_SIZE\");\n" +
                        "\n" +
                        "        return map;\n" +
                        "    }\n" +
                        "\n" +
                        "    @Bean\n" +
                        "    public InstrumentationLoadTimeWeaver loadTimeWeaver()  throws Throwable {\n" +
                        "        InstrumentationLoadTimeWeaver loadTimeWeaver = new InstrumentationLoadTimeWeaver();\n" +
                        "        return loadTimeWeaver;\n" +
                        "    }\n" +
                        "}"
        );
    }

}