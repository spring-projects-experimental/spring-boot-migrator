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
package org.springframework.sbm.jee.jpa.actions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.sbm.build.api.ApplicationModule;
import org.springframework.sbm.build.api.Dependency;
import org.springframework.sbm.engine.context.ProjectContext;
import org.springframework.sbm.engine.recipe.AbstractAction;
import org.springframework.sbm.java.api.ProjectJavaSources;
import org.springframework.sbm.java.util.BasePackageCalculator;
import org.springframework.sbm.jee.jpa.api.Persistence;
import org.springframework.sbm.jee.jpa.api.PersistenceXml;
import org.springframework.sbm.jee.jpa.filter.PersistenceXmlResourceFilter;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.StringWriter;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class MigrateEclipseLinkToSpringBoot extends AbstractAction {

    @Autowired
    @JsonIgnore
    @Setter
    private Configuration configuration;

    @Autowired
    @JsonIgnore
    @Setter
    private BasePackageCalculator basePackageCalculator;

    @Override
    public void apply(ProjectContext context) {
        List<ApplicationModule> affectedModules = context.getApplicationModules().stream()
                .filter(b -> b.getBuildFile().hasDeclaredDependencyMatchingRegex("org\\.eclipse\\.persistence\\:.*"))
                .collect(Collectors.toList());

        affectedModules.forEach(m -> {
            upgradeEclipseLink(m);
            declareDependencyToSpringBootStarterDataJpaWithHibernateExcluded(m);
            List<SpringBootJpaProperty> eclipseLinkProperties = extractEclipseLinkProperties(m);
            Map<String, Object> eclipseLinkPropertiesTemplateParams = mapEclipseLinkProperties(eclipseLinkProperties);
            String eclipseLinkConfigPackage = findPackageForEclipseLinkConfigurationClass(context.getProjectJavaSources());
            String eclipseLinkConfigClassName = findNameForEclipseLinkConfigurationClass(eclipseLinkConfigPackage, context.getProjectJavaSources());
            String configurationSource = renderEclipseLinkConfigurationSource(eclipseLinkConfigPackage, eclipseLinkConfigClassName, eclipseLinkPropertiesTemplateParams);
            addClassToModule(m, context.getProjectRootDirectory(), m.getMainJavaSourceSet().getJavaSourceLocation().getSourceFolder(), eclipseLinkConfigPackage, configurationSource);
        });
    }

    private Map<String, Object> mapEclipseLinkProperties(List<SpringBootJpaProperty> bootJpaProperties) {
        return bootJpaProperties.stream()
                .filter(p -> p.getPropertyName().startsWith("eclipselink."))
                .collect(Collectors.toMap(p -> getPropertyNameForEclipseLinkConfiguration(p), SpringBootJpaProperty::getPropertyValue));
    }

    private String getPropertyNameForEclipseLinkConfiguration(SpringBootJpaProperty p) {
        String eclipseLinkPropertyName = this.mapEclipseLinkPropertyName(p.getPropertyName());
        if(eclipseLinkPropertyName == null) {
            eclipseLinkPropertyName = "\"" + p.getPropertyName() + "\"";
        }
        return eclipseLinkPropertyName;
    }

    private String mapEclipseLinkPropertyName(String propertyName) {
        Map<String, String> mapping = new HashMap<>();
        mapping.put("eclipselink.allow-zero-id", "PersistenceUnitProperties.ALLOW_ZERO_ID");
        mapping.put("eclipselink.application-location", "PersistenceUnitProperties.APP_LOCATION");
        mapping.put("eclipselink.beanvalidation.no-optimisation", "PersistenceUnitProperties.BEAN_VALIDATION_NO_OPTIMISATION");
        mapping.put("eclipselink.cache.coordination.channel", "PersistenceUnitProperties.COORDINATION_CHANNEL");
        mapping.put("eclipselink.cache.coordination.jgroups.config", "PersistenceUnitProperties.COORDINATION_JGROUPS_CONFIG");
        mapping.put("eclipselink.cache.coordination.jms.factory", "PersistenceUnitProperties.COORDINATION_JMS_FACTORY");
        mapping.put("eclipselink.cache.coordination.jms.host", "PersistenceUnitProperties.COORDINATION_JMS_HOST");
        mapping.put("eclipselink.cache.coordination.jms.reuse-topic-publisher", "PersistenceUnitProperties.COORDINATION_JMS_REUSE_PUBLISHER");
        mapping.put("eclipselink.cache.coordination.jms.topic", "PersistenceUnitProperties.COORDINATION_JMS_TOPIC");
        mapping.put("eclipselink.cache.coordination.jndi.initial-context-factory", "PersistenceUnitProperties.COORDINATION_JNDI_CONTEXT");
        mapping.put("eclipselink.cache.coordination.jndi.password", "PersistenceUnitProperties.COORDINATION_JNDI_PASSWORD");
        mapping.put("eclipselink.cache.coordination.jndi.user", "PersistenceUnitProperties.COORDINATION_JNDI_USER");
        mapping.put("eclipselink.cache.coordination.naming-service", "PersistenceUnitProperties.COORDINATION_NAMING_SERVICE");
        mapping.put("eclipselink.cache.coordination.propagate-asynchronously", "PersistenceUnitProperties.COORDINATION_ASYNCH");
        mapping.put("eclipselink.cache.coordination.protocol", "PersistenceUnitProperties.COORDINATION_PROTOCOL");
        mapping.put("eclipselink.cache.coordination.remove-connection-on-error", "PersistenceUnitProperties.COORDINATION_REMOVE_CONNECTION");
        mapping.put("eclipselink.cache.coordination.rmi.announcement-delay", "PersistenceUnitProperties.COORDINATION_RMI_ANNOUNCEMENT_DELAY");
        mapping.put("eclipselink.cache.coordination.rmi.multicast-group", "PersistenceUnitProperties.COORDINATION_RMI_MULTICAST_GROUP");
        mapping.put("eclipselink.cache.coordination.rmi.multicast-group.port", "PersistenceUnitProperties.COORDINATION_RMI_MULTICAST_GROUP_PORT");
        mapping.put("eclipselink.cache.coordination.rmi.packet-time-to-live", "PersistenceUnitProperties.COORDINATION_RMI_PACKET_TIME_TO_LIVE");
        mapping.put("eclipselink.cache.coordination.rmi.url", "PersistenceUnitProperties.COORDINATION_RMI_URL");
        mapping.put("eclipselink.cache.coordination.serializer", "PersistenceUnitProperties.COORDINATION_SERIALIZER");
        mapping.put("eclipselink.cache.coordination.thread.pool.size", "PersistenceUnitProperties.COORDINATION_THREAD_POOL_SIZE");
        mapping.put("eclipselink.cache.database-event-listener", "PersistenceUnitProperties.DATABASE_EVENT_LISTENER");
        mapping.put("eclipselink.cache.query-results", "PersistenceUnitProperties.QUERY_CACHE");
        mapping.put("eclipselink.cache.shared.default", "PersistenceUnitProperties.CACHE_SHARED_DEFAULT");
        mapping.put("eclipselink.cache.size.default", "PersistenceUnitProperties.CACHE_SIZE_DEFAULT");
        mapping.put("eclipselink.cache.type.default", "PersistenceUnitProperties.CACHE_TYPE_DEFAULT");
        mapping.put("eclipselink.canonicalmodel.generate_timestamp", "PersistenceUnitProperties.CANONICAL_MODEL_GENERATE_TIMESTAMP");
        mapping.put("eclipselink.canonicalmodel.load_xml", "PersistenceUnitProperties.CANONICAL_MODEL_LOAD_XML");
        mapping.put("eclipselink.canonicalmodel.prefix", "PersistenceUnitProperties.CANONICAL_MODEL_PREFIX");
        mapping.put("eclipselink.canonicalmodel.subpackage", "PersistenceUnitProperties.CANONICAL_MODEL_SUB_PACKAGE");
        mapping.put("eclipselink.canonicalmodel.suffix", "PersistenceUnitProperties.CANONICAL_MODEL_SUFFIX");
        mapping.put("eclipselink.canonicalmodel.use_static_factory", "PersistenceUnitProperties.CANONICAL_MODEL_USE_STATIC_FACTORY");
        mapping.put("eclipselink.classloader", "PersistenceUnitProperties.CLASSLOADER");
        mapping.put("eclipselink.composite-unit", "PersistenceUnitProperties.COMPOSITE_UNIT");
        mapping.put("eclipselink.composite-unit.member", "PersistenceUnitProperties.COMPOSITE_UNIT_MEMBER");
        mapping.put("eclipselink.composite-unit.properties", "PersistenceUnitProperties.COMPOSITE_UNIT_PROPERTIES");
        mapping.put("eclipselink.connection-pool.force-internal-pool", "PersistenceUnitProperties.CONNECTION_POOL_INTERNALLY_POOL_DATASOURCE");
        mapping.put("eclipselink.create-ddl-jdbc-file-name", "PersistenceUnitProperties.CREATE_JDBC_DDL_FILE");
        mapping.put("eclipselink.ddl-generation", "PersistenceUnitProperties.DDL_GENERATION");
        mapping.put("eclipselink.ddl-generation.index-foreign-keys", "PersistenceUnitProperties.DDL_GENERATION_INDEX_FOREIGN_KEYS");
        mapping.put("eclipselink.ddl-generation.output-mode", "PersistenceUnitProperties.DDL_GENERATION_MODE");
        mapping.put("eclipselink.ddl-generation.table-creation-suffix", "PersistenceUnitProperties.TABLE_CREATION_SUFFIX");
        mapping.put("eclipselink.ddlgen-terminate-statements", "PersistenceUnitProperties.SCHEMA_GENERATION_SCRIPT_TERMINATE_STATEMENTS");
        mapping.put("eclipselink.deploy-on-startup", "PersistenceUnitProperties.DEPLOY_ON_STARTUP");
        mapping.put("eclipselink.drop-ddl-jdbc-file-name", "PersistenceUnitProperties.DROP_JDBC_DDL_FILE");
        mapping.put("eclipselink.exception-handler", "PersistenceUnitProperties.EXCEPTION_HANDLER_CLASS");
        mapping.put("eclipselink.exclude-eclipselink-orm", "PersistenceUnitProperties.EXCLUDE_ECLIPSELINK_ORM_FILE");
        mapping.put("eclipselink.flush-clear.cache", "PersistenceUnitProperties.FLUSH_CLEAR_CACHE");
        mapping.put("eclipselink.id-validation", "PersistenceUnitProperties.ID_VALIDATION");
        mapping.put("eclipselink.jdbc.allow-native-sql-queries", "PersistenceUnitProperties.ALLOW_NATIVE_SQL_QUERIES");
        mapping.put("eclipselink.jdbc.batch-writing", "PersistenceUnitProperties.BATCH_WRITING");
        mapping.put("eclipselink.jdbc.batch-writing.size", "PersistenceUnitProperties.BATCH_WRITING_SIZE");
        mapping.put("eclipselink.jdbc.bind-parameters", "PersistenceUnitProperties.JDBC_BIND_PARAMETERS");
        mapping.put("eclipselink.jdbc.cache-statements", "PersistenceUnitProperties.CACHE_STATEMENTS");
        mapping.put("eclipselink.jdbc.cache-statements.size", "PersistenceUnitProperties.CACHE_STATEMENTS_SIZE");
        mapping.put("eclipselink.jdbc.connections.initial", "PersistenceUnitProperties.JDBC_CONNECTIONS_INITIAL");
        mapping.put("eclipselink.jdbc.connections.max", "PersistenceUnitProperties.JDBC_CONNECTIONS_MAX");
        mapping.put("eclipselink.jdbc.connections.min", "PersistenceUnitProperties.JDBC_CONNECTIONS_MIN");
        mapping.put("eclipselink.jdbc.connections.wait-timeout", "PersistenceUnitProperties.JDBC_CONNECTIONS_WAIT");
        mapping.put("eclipselink.jdbc.connector", "PersistenceUnitProperties.JDBC_CONNECTOR");
        mapping.put("eclipselink.jdbc.exclusive-connection.is-lazy", "PersistenceUnitProperties.EXCLUSIVE_CONNECTION_IS_LAZY");
        mapping.put("eclipselink.jdbc.exclusive-connection.mode", "PersistenceUnitProperties.EXCLUSIVE_CONNECTION_MODE");
        mapping.put("eclipselink.jdbc.native-sql", "PersistenceUnitProperties.NATIVE_SQL");
        mapping.put("eclipselink.jdbc.read-connections.initial", "PersistenceUnitProperties.JDBC_READ_CONNECTIONS_INITIAL");
        mapping.put("eclipselink.jdbc.read-connections.max", "PersistenceUnitProperties.JDBC_READ_CONNECTIONS_MAX");
        mapping.put("eclipselink.jdbc.read-connections.min", "PersistenceUnitProperties.JDBC_READ_CONNECTIONS_MIN");
        mapping.put("eclipselink.jdbc.read-connections.shared", "PersistenceUnitProperties.JDBC_READ_CONNECTIONS_SHARED");
        mapping.put("eclipselink.jdbc.result-set-access-optimization", "PersistenceUnitProperties.JDBC_RESULT_SET_ACCESS_OPTIMIZATION");
        mapping.put("eclipselink.jdbc.sequence-connection-pool", "PersistenceUnitProperties.JDBC_SEQUENCE_CONNECTION_POOL");
        mapping.put("eclipselink.jdbc.sequence-connection-pool.initial", "PersistenceUnitProperties.JDBC_SEQUENCE_CONNECTION_POOL_INITIAL");
        mapping.put("eclipselink.jdbc.sequence-connection-pool.max", "PersistenceUnitProperties.JDBC_SEQUENCE_CONNECTION_POOL_MAX");
        mapping.put("eclipselink.jdbc.sequence-connection-pool.min", "PersistenceUnitProperties.JDBC_SEQUENCE_CONNECTION_POOL_MIN");
        mapping.put("eclipselink.jdbc.sequence-connection-pool.non-jta-data-source", "PersistenceUnitProperties.JDBC_SEQUENCE_CONNECTION_POOL_DATASOURCE");
        mapping.put("eclipselink.jdbc.sql-cast", "PersistenceUnitProperties.SQL_CAST");
        mapping.put("eclipselink.jdbc.uppercase-columns", "PersistenceUnitProperties.NATIVE_QUERY_UPPERCASE_COLUMNS");
        mapping.put("eclipselink.jdbc.write-connections.initial", "PersistenceUnitProperties.JDBC_WRITE_CONNECTIONS_INITIAL");
        mapping.put("eclipselink.jdbc.write-connections.max", "PersistenceUnitProperties.JDBC_WRITE_CONNECTIONS_MAX");
        mapping.put("eclipselink.jdbc.write-connections.min", "PersistenceUnitProperties.JDBC_WRITE_CONNECTIONS_MIN");
        mapping.put("eclipselink.jpa.sql-call-deferral", "PersistenceUnitProperties.SQL_CALL_DEFERRAL");
        mapping.put("eclipselink.jpa.uppercase-column-names", "PersistenceUnitProperties.UPPERCASE_COLUMN_NAMES");
        mapping.put("eclipselink.jpql.parser", "PersistenceUnitProperties.JPQL_PARSER");
        mapping.put("eclipselink.jpql.validation", "PersistenceUnitProperties.JPQL_VALIDATION");
        mapping.put("eclipselink.locking.timestamp.local.default", "PersistenceUnitProperties.USE_LOCAL_TIMESTAMP");
        mapping.put("eclipselink.logging.connection", "PersistenceUnitProperties.LOGGING_CONNECTION");
        mapping.put("eclipselink.logging.exceptions", "PersistenceUnitProperties.LOGGING_EXCEPTIONS");
        mapping.put("eclipselink.logging.file", "PersistenceUnitProperties.LOGGING_FILE");
        mapping.put("eclipselink.logging.level", "PersistenceUnitProperties.LOGGING_LEVEL");
        mapping.put("eclipselink.logging.logger", "PersistenceUnitProperties.LOGGING_LOGGER");
        mapping.put("eclipselink.logging.parameters", "PersistenceUnitProperties.LOGGING_PARAMETERS");
        mapping.put("eclipselink.logging.session", "PersistenceUnitProperties.LOGGING_SESSION");
        mapping.put("eclipselink.logging.thread", "PersistenceUnitProperties.LOGGING_THREAD");
        mapping.put("eclipselink.logging.timestamp", "PersistenceUnitProperties.LOGGING_TIMESTAMP");
        mapping.put("eclipselink.memory.free-metadata", "PersistenceUnitProperties.FREE_METADATA");
        mapping.put("eclipselink.metadata-source", "PersistenceUnitProperties.METADATA_SOURCE");
        mapping.put("eclipselink.metadata-source.properties.file", "PersistenceUnitProperties.METADATA_SOURCE_PROPERTIES_FILE");
        mapping.put("eclipselink.metadata-source.send-refresh-command", "PersistenceUnitProperties.METADATA_SOURCE_RCM_COMMAND");
        mapping.put("eclipselink.metadata-source.xml.file", "PersistenceUnitProperties.METADATA_SOURCE_XML_FILE");
        mapping.put("eclipselink.metadata-source.xml.url", "PersistenceUnitProperties.METADATA_SOURCE_XML_URL");
        mapping.put("eclipselink.multitenant.strategy", "PersistenceUnitProperties.MULTITENANT_STRATEGY");
        mapping.put("eclipselink.multitenant.tenants-share-cache", "PersistenceUnitProperties.MULTITENANT_SHARED_CACHE");
        mapping.put("eclipselink.multitenant.tenants-share-emf", "PersistenceUnitProperties.MULTITENANT_SHARED_EMF");
        mapping.put("eclipselink.nosql.connection-factory", "PersistenceUnitProperties.NOSQL_CONNECTION_FACTORY");
        mapping.put("eclipselink.nosql.connection-spec", "PersistenceUnitProperties.NOSQL_CONNECTION_SPEC");
        mapping.put("eclipselink.nosql.property.password", "PersistenceUnitProperties.NOSQL_PASSWORD");
        mapping.put("eclipselink.nosql.property.user", "PersistenceUnitProperties.NOSQL_USER");
        mapping.put("eclipselink.oracle.proxy-type", "PersistenceUnitProperties.ORACLE_PROXY_TYPE");
        mapping.put("eclipselink.order-updates", "PersistenceUnitProperties.ORDER_UPDATES");
        mapping.put("eclipselink.orm.throw.exceptions", "PersistenceUnitProperties.THROW_EXCEPTIONS");
        mapping.put("eclipselink.orm.validate.schema", "PersistenceUnitProperties.ORM_SCHEMA_VALIDATION");
        mapping.put("eclipselink.partitioning", "PersistenceUnitProperties.PARTITIONING");
        mapping.put("eclipselink.partitioning.callback", "PersistenceUnitProperties.PARTITIONING_CALLBACK");
        mapping.put("eclipselink.persistence-context.close-on-commit", "PersistenceUnitProperties.PERSISTENCE_CONTEXT_CLOSE_ON_COMMIT");
        mapping.put("eclipselink.persistence-context.commit-order", "PersistenceUnitProperties.PERSISTENCE_CONTEXT_COMMIT_ORDER");
        mapping.put("eclipselink.persistence-context.commit-without-persist-rules", "PersistenceUnitProperties.PERSISTENCE_CONTEXT_COMMIT_WITHOUT_PERSIST_RULES");
        mapping.put("eclipselink.persistence-context.flush-mode", "PersistenceUnitProperties.PERSISTENCE_CONTEXT_FLUSH_MODE");
        mapping.put("eclipselink.persistence-context.persist-on-commit", "PersistenceUnitProperties.PERSISTENCE_CONTEXT_PERSIST_ON_COMMIT");
        mapping.put("eclipselink.persistence-context.reference-mode", "PersistenceUnitProperties.PERSISTENCE_CONTEXT_REFERENCE_MODE");
        mapping.put("eclipselink.persistenceunits", "PersistenceUnitProperties.ECLIPSELINK_PERSISTENCE_UNITS");
        mapping.put("eclipselink.persistencexml", "PersistenceUnitProperties.ECLIPSELINK_PERSISTENCE_XML");
        mapping.put("eclipselink.profiler", "PersistenceUnitProperties.PROFILER");
        mapping.put("eclipselink.project-cache", "PersistenceUnitProperties.PROJECT_CACHE");
        mapping.put("eclipselink.project-cache.java-serialization.file-location", "PersistenceUnitProperties.PROJECT_CACHE_FILE");
        mapping.put("eclipselink.query.timeout.unit", "PersistenceUnitProperties.QUERY_TIMEOUT_UNIT");
        mapping.put("eclipselink.remote.client.url", "PersistenceUnitProperties.REMOTE_URL");
        mapping.put("eclipselink.remote.protocol", "PersistenceUnitProperties.REMOTE_PROTOCOL");
        mapping.put("eclipselink.remote.server.name", "PersistenceUnitProperties.REMOTE_SERVER_NAME");
        mapping.put("eclipselink.se-puinfo", "PersistenceUnitProperties.ECLIPSELINK_SE_PUINFO");
        mapping.put("eclipselink.sequencing.default-sequence-to-table", "PersistenceUnitProperties.SEQUENCING_SEQUENCE_DEFAULT");
        mapping.put("eclipselink.sequencing.start-sequence-at-nextval", "PersistenceUnitProperties.SEQUENCING_START_AT_NEXTVAL");
        mapping.put("eclipselink.serializer", "PersistenceUnitProperties.SERIALIZER");
        mapping.put("eclipselink.session-event-listener", "PersistenceUnitProperties.SESSION_EVENT_LISTENER_CLASS");
        mapping.put("eclipselink.session-name", "PersistenceUnitProperties.SESSION_NAME");
        mapping.put("eclipselink.session.customizer", "PersistenceUnitProperties.SESSION_CUSTOMIZER");
        mapping.put("eclipselink.session.include.descriptor.queries", "PersistenceUnitProperties.INCLUDE_DESCRIPTOR_QUERIES");
        mapping.put("eclipselink.sessions-xml", "PersistenceUnitProperties.SESSIONS_XML");
        mapping.put("eclipselink.target-database", "PersistenceUnitProperties.TARGET_DATABASE");
        mapping.put("eclipselink.target-database-properties", "PersistenceUnitProperties.TARGET_DATABASE_PROPERTIES");
        mapping.put("eclipselink.target-server", "PersistenceUnitProperties.TARGET_SERVER");
        mapping.put("eclipselink.temporal.mutable", "PersistenceUnitProperties.TEMPORAL_MUTABLE");
        mapping.put("eclipselink.tenant-id", "PersistenceUnitProperties.MULTITENANT_PROPERTY_DEFAULT");
        mapping.put("eclipselink.tenant-schema-id", "PersistenceUnitProperties.MULTITENANT_SCHEMA_PROPERTY_DEFAULT");
        mapping.put("eclipselink.tolerate-invalid-jpql", "PersistenceUnitProperties.JPQL_TOLERATE");
        mapping.put("eclipselink.transaction.join-existing", "PersistenceUnitProperties.JOIN_EXISTING_TRANSACTION");
        mapping.put("eclipselink.tuning", "PersistenceUnitProperties.TUNING");
        mapping.put("eclipselink.validate-existence", "PersistenceUnitProperties.VALIDATE_EXISTENCE");
        mapping.put("eclipselink.validation-only", "PersistenceUnitProperties.VALIDATION_ONLY_PROPERTY");
        mapping.put("eclipselink.weaving", "PersistenceUnitProperties.WEAVING");
        mapping.put("eclipselink.weaving.changetracking", "PersistenceUnitProperties.WEAVING_CHANGE_TRACKING");
        mapping.put("eclipselink.weaving.eager", "PersistenceUnitProperties.WEAVING_EAGER");
        mapping.put("eclipselink.weaving.fetchgroups", "PersistenceUnitProperties.WEAVING_FETCHGROUPS");
        mapping.put("eclipselink.weaving.internal", "PersistenceUnitProperties.WEAVING_INTERNAL");
        mapping.put("eclipselink.weaving.lazy", "PersistenceUnitProperties.WEAVING_LAZY");
        mapping.put("eclipselink.weaving.mappedsuperclass", "PersistenceUnitProperties.WEAVING_MAPPEDSUPERCLASS");
        mapping.put("eclipselink.weaving.rest", "PersistenceUnitProperties.WEAVING_REST");

        return mapping.get(propertyName);
    }

    private String findPackageForEclipseLinkConfigurationClass(ProjectJavaSources projectJavaSources) {
        String packageName = basePackageCalculator.calculateBasePackage(projectJavaSources.list());
        return packageName;
    }

    private void declareDependencyToSpringBootStarterDataJpaWithHibernateExcluded(ApplicationModule m) {
        if(m.getBuildFile().hasDeclaredDependencyMatchingRegex("org\\.springframework\\.boot\\:spring-boot-starter-data-jpa\\:.*")) {
            m.getBuildFile().excludeDependencies(createExcludedDependencies());
        } else {
            Dependency springBootStarterDataJpaDependency = createSpringBootStarterDataJpaDependency();
            m.getBuildFile().addDependency(springBootStarterDataJpaDependency);
        }
    }

    private Dependency createSpringBootStarterDataJpaDependency() {
        return Dependency.builder()
                .groupId("org.springframework.boot")
                .artifactId("spring-boot-starter-data-jpa")
                .version("2.5.9") // TODO: use latest.release or make configurable
                .exclusions(createExcludedDependencies())
                .build();
    }

    @NotNull
    private List<Dependency> createExcludedDependencies() {
        return List.of(

            Dependency.builder()
                .groupId("org.hibernate")
                .artifactId("hibernate-entitymanager")
                .build(),

            Dependency.builder()
                    .groupId("org.hibernate")
                    .artifactId("hibernate-core")
                    .build()

        );
    }

    private List<SpringBootJpaProperty> extractEclipseLinkProperties(ApplicationModule module) {
        List<SpringBootJpaProperty> springBootJpaProperties = new ArrayList<>();

        Optional<PersistenceXml> optPersistenceXml = module.search(new PersistenceXmlResourceFilter());
        if(optPersistenceXml.isPresent()) {
            PersistenceXml persistenceXml = optPersistenceXml.get();
            Persistence.PersistenceUnit.Properties properties = persistenceXml.getPersistence().getPersistenceUnit().get(0) // FIXME: should multiple persistence-units be handled or fail?
                    .getProperties();

            if(false == properties.getProperty().isEmpty()) {
                return properties.getProperty().stream()
                        .filter(p -> p.getName().startsWith("eclipselink."))
                        .map(p -> new SpringBootJpaProperty(p.getName(), p.getValue()))
                        .collect(Collectors.toList());
            }

        }

        return springBootJpaProperties;
    }

    private void addClassToModule(ApplicationModule m, Path projectRootDir, Path sourceFolder, String packageName, String configurationCode) {
        m.getMainJavaSourceSet().addJavaSource(projectRootDir, configurationCode, packageName);
    }

    private String findNameForEclipseLinkConfigurationClass(String eclipseLinkConfigPackage, ProjectJavaSources projectJavaSources) {
        return "EclipseLinkJpaConfiguration";
    }

    private String renderEclipseLinkConfigurationSource(String eclipseLinkConfigPackage, String eclipseLinkConfigClassName, Map<String, Object> eclipseLinkProperties) {
        try {
            StringWriter writer = new StringWriter();
            Map<String, Object> params = new HashMap<>();
            params.put("packageName", eclipseLinkConfigPackage);
            params.put("className", eclipseLinkConfigClassName);
            params.put("eclipseLinkProperties", eclipseLinkProperties);
            Template template = configuration.getTemplate("eclipselink-configuration-class.ftl");
            template.process(params, writer);
            String src = writer.toString();
            return src;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private void declareDependencyToSpringBootStarterDataJpaWithHibernateExcluded() {
    }

    private void upgradeEclipseLink(ApplicationModule m) {

    }

    @Override
    public boolean isApplicable(ProjectContext context) {
        return context.getApplicationModules().stream()
                .anyMatch(b -> b.getBuildFile().hasDeclaredDependencyMatchingRegex("org\\.eclipse\\.persistence\\:.*"));
    }
}
