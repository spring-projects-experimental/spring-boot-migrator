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
package org.springframework.sbm.jee.jpa.api;

import org.junit.jupiter.api.Test;
import org.openrewrite.xml.tree.Xml;

import javax.xml.bind.JAXBException;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PersistenceXmlTest {

    @Test
    void persistence_1_0_minimal() {
        String xml =
                "<persistence xmlns=\"http://java.sun.com/xml/ns/persistence/\"\n" +
                        "        xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "        xsi:schemaLocation=\"http://java.sun.com/xml/ns/persistence\n" +
                        "          http://java.sun.com/xml/ns/persistence/persistence_1_0.xsd\"\n" +
                        "        version=\"1.0\">\n" +
                        // In 1.0 the <persistence-unit> was allowed to be null
                        "</persistence>";
        Persistence persistence = getPersistence(xml);

        assertThat(persistence).isNotNull();
        assertThat(persistence.getVersion()).isEqualTo("1.0");
        assertThat(persistence.getPersistenceUnit()).isEmpty();
    }

    @Test
    void persistence_1_0_withNoExcludeListedClassesDeclared() {
        String xml =
                "<persistence xmlns=\"http://java.sun.com/xml/ns/persistence/\"\n" +
                        "        xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "        xsi:schemaLocation=\"http://java.sun.com/xml/ns/persistence\n" +
                        "          http://java.sun.com/xml/ns/persistence/persistence_1_0.xsd\"\n" +
                        "        version=\"1.0\">\n" +
                        "   <persistence-unit name=\"testPersistenceUnit\" transaction-type=\"RESOURCE_LOCAL\">\n" +
                        "   </persistence-unit>" +
                        "</persistence>";

        Persistence persistence = getPersistence(xml);

        assertThat(persistence).isNotNull();
        assertThat(persistence.getVersion()).isEqualTo("1.0");
        assertThat(persistence.getPersistenceUnit()).isNotEmpty();
        assertThat(persistence.getPersistenceUnit().get(0).getName()).isEqualTo("testPersistenceUnit");
        // The enum was of type TransactionType in 1.0 but this is irrelevant for Spring Boot migration
        assertThat(persistence.getPersistenceUnit().get(0).getTransactionType()).isSameAs(PersistenceUnitTransactionType.RESOURCE_LOCAL);
        // In 1.0 <exclude-unlisted-classes> is defined as 'false' by default but the 2.2 schema is used
        // For Spring Boot migration it's irrelevant
        assertThat(persistence.getPersistenceUnit().get(0).isExcludeUnlistedClasses()).isNull();
    }

    @Test
    void persistence_1_0() {
        String xml =
                "<persistence xmlns=\"http://java.sun.com/xml/ns/persistence/\"\n" +
                        "        xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "        xsi:schemaLocation=\"http://java.sun.com/xml/ns/persistence\n" +
                        "          http://java.sun.com/xml/ns/persistence/persistence_1_0.xsd\"\n" +
                        "        version=\"1.0\">\n" +
                        "   <persistence-unit name=\"testPersistenceUnit\" transaction-type=\"RESOURCE_LOCAL\">\n" +
                        "        <description>A description</description>\n" +
                        "        <provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>\n" +
                        "        <jta-data-source>java:Ds</jta-data-source>\n" +
                        "        <non-jta-data-source>NonJtaDataSource</non-jta-data-source>\n" +
                        "        <mapping-file>my-orm.xml</mapping-file>\n" +
                        "        <mapping-file>your-orm.xml</mapping-file>\n" +
                        "        <jar-file>some.jar</jar-file>\n" +
                        "        <jar-file>another.jar</jar-file>\n" +
                        "        <class>com.example.Entity1</class>\n" +
                        "        <class>com.example.Entity2</class>\n" +
                        "        <exclude-unlisted-classes>false</exclude-unlisted-classes>\n" +
                        "        <properties>\n" +
                        "            <property name=\"javax.persistence.jdbc.driver\" value=\"com.mysql.cj.jdbc.Driver\" />\n" +
                        "            <property name=\"javax.persistence.jdbc.url\" value=\"jdbc:mysql://localhost:3306/db\" />\n" +
                        "            <property name=\"javax.persistence.jdbc.user\" value=\"username\" />\n" +
                        "        </properties>" +
                        "   </persistence-unit>" +
                        "</persistence>";

        Persistence persistence = getPersistence(xml);

        assertThat(persistence).isNotNull();
        assertThat(persistence.getVersion()).isEqualTo("1.0");
        assertThat(persistence.getPersistenceUnit()).isNotEmpty();
        Persistence.PersistenceUnit persistenceUnit = persistence.getPersistenceUnit().get(0);
        assertThat(persistenceUnit.getName()).isEqualTo("testPersistenceUnit");
        assertThat(persistenceUnit.getDescription()).isEqualTo("A description");
        assertThat(persistenceUnit.getProvider()).isEqualTo("org.hibernate.jpa.HibernatePersistenceProvider");
        assertThat(persistenceUnit.getJtaDataSource()).isEqualTo("java:Ds");
        assertThat(persistenceUnit.getNonJtaDataSource()).isEqualTo("NonJtaDataSource");
        assertThat(persistenceUnit.getMappingFile().get(0)).isEqualTo("my-orm.xml");
        assertThat(persistenceUnit.getMappingFile().get(1)).isEqualTo("your-orm.xml");
        assertThat(persistenceUnit.getJarFile().get(0)).isEqualTo("some.jar");
        assertThat(persistenceUnit.getJarFile().get(1)).isEqualTo("another.jar");
        assertThat(persistenceUnit.getClazz().get(0)).isEqualTo("com.example.Entity1");
        assertThat(persistenceUnit.getClazz().get(1)).isEqualTo("com.example.Entity2");
        assertThat(persistenceUnit.isExcludeUnlistedClasses()).isFalse();
        List<Persistence.PersistenceUnit.Properties.Property> properties = persistenceUnit.getProperties().getProperty();
        assertThat(properties.get(0).getName()).isEqualTo("javax.persistence.jdbc.driver");
        assertThat(properties.get(0).getValue()).isEqualTo("com.mysql.cj.jdbc.Driver");
        assertThat(properties.get(1).getName()).isEqualTo("javax.persistence.jdbc.url");
        assertThat(properties.get(1).getValue()).isEqualTo("jdbc:mysql://localhost:3306/db");
        assertThat(properties.get(2).getName()).isEqualTo("javax.persistence.jdbc.user");
        assertThat(properties.get(2).getValue()).isEqualTo("username");
        // The enum was of type TransactionType in 1.0 but this is irrelevant for Spring Boot migration
        assertThat(persistenceUnit.getTransactionType()).isSameAs(PersistenceUnitTransactionType.RESOURCE_LOCAL);
    }

    @Test
    void persistence_2_0() {

        String xml =
                "<persistence xmlns=\"http://java.sun.com/xml/ns/persistence/\"\n" +
                        "        xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "        xsi:schemaLocation=\"http://java.sun.com/xml/ns/persistence\n" +
                        "          http://java.sun.com/xml/ns/persistence/persistence_2_0.xsd\"\n" +
                        "        version=\"2.0\">\n" +
                        "   <persistence-unit name=\"testPersistenceUnit\" transaction-type=\"RESOURCE_LOCAL\">\n" +
                        "        <description>A description</description>\n" +
                        "        <provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>\n" +
                        "        <jta-data-source>java:Ds</jta-data-source>\n" +
                        "        <non-jta-data-source>NonJtaDataSource</non-jta-data-source>\n" +
                        "        <mapping-file>my-orm.xml</mapping-file>\n" +
                        "        <mapping-file>your-orm.xml</mapping-file>\n" +
                        "        <jar-file>some.jar</jar-file>\n" +
                        "        <jar-file>another.jar</jar-file>\n" +
                        "        <class>com.example.Entity1</class>\n" +
                        "        <class>com.example.Entity2</class>\n" +
                        "        <exclude-unlisted-classes>false</exclude-unlisted-classes>\n" +
                        // New in 2.0 <xsd:simpleType name="persistence-unit-caching-type">
                        "        <shared-cache-mode>DISABLE_SELECTIVE</shared-cache-mode>" +
                        // New in 2.0 <xsd:element name="validation-mode"...
                        "        <validation-mode>CALLBACK</validation-mode>\n" +
                        "        <properties>\n" +
                        "            <property name=\"javax.persistence.jdbc.driver\" value=\"com.mysql.cj.jdbc.Driver\" />\n" +
                        "            <property name=\"javax.persistence.jdbc.url\" value=\"jdbc:mysql://localhost:3306/db\" />\n" +
                        "            <property name=\"javax.persistence.jdbc.user\" value=\"username\" />\n" +
                        "        </properties>" +
                        "   </persistence-unit>" +
                        "</persistence>";

        Persistence persistence = getPersistence(xml);

        assertThat(persistence).isNotNull();
        assertThat(persistence.getVersion()).isEqualTo("2.0");
        assertThat(persistence.getPersistenceUnit()).isNotEmpty();
        Persistence.PersistenceUnit persistenceUnit = persistence.getPersistenceUnit().get(0);
        assertThat(persistenceUnit.getName()).isEqualTo("testPersistenceUnit");

        // new in 2.0
        assertThat(persistenceUnit.getSharedCacheMode()).isEqualTo(PersistenceUnitCachingType.DISABLE_SELECTIVE);
        // new in 2.0
        assertThat(persistenceUnit.getValidationMode()).isEqualTo(PersistenceUnitValidationModeType.CALLBACK);

        assertThat(persistenceUnit.getDescription()).isEqualTo("A description");
        assertThat(persistenceUnit.getProvider()).isEqualTo("org.hibernate.jpa.HibernatePersistenceProvider");
        assertThat(persistenceUnit.getJtaDataSource()).isEqualTo("java:Ds");
        assertThat(persistenceUnit.getNonJtaDataSource()).isEqualTo("NonJtaDataSource");
        assertThat(persistenceUnit.getMappingFile().get(0)).isEqualTo("my-orm.xml");
        assertThat(persistenceUnit.getMappingFile().get(1)).isEqualTo("your-orm.xml");
        assertThat(persistenceUnit.getJarFile().get(0)).isEqualTo("some.jar");
        assertThat(persistenceUnit.getJarFile().get(1)).isEqualTo("another.jar");
        assertThat(persistenceUnit.getClazz().get(0)).isEqualTo("com.example.Entity1");
        assertThat(persistenceUnit.getClazz().get(1)).isEqualTo("com.example.Entity2");
        assertThat(persistenceUnit.isExcludeUnlistedClasses()).isFalse();
        List<Persistence.PersistenceUnit.Properties.Property> properties = persistenceUnit.getProperties().getProperty();
        assertThat(properties.get(0).getName()).isEqualTo("javax.persistence.jdbc.driver");
        assertThat(properties.get(0).getValue()).isEqualTo("com.mysql.cj.jdbc.Driver");
        assertThat(properties.get(1).getName()).isEqualTo("javax.persistence.jdbc.url");
        assertThat(properties.get(1).getValue()).isEqualTo("jdbc:mysql://localhost:3306/db");
        assertThat(properties.get(2).getName()).isEqualTo("javax.persistence.jdbc.user");
        assertThat(properties.get(2).getValue()).isEqualTo("username");
        assertThat(persistenceUnit.getTransactionType()).isSameAs(PersistenceUnitTransactionType.RESOURCE_LOCAL);
    }

    @Test
    void persistence_2_1() {
        // Starting with the 2.1 version, the Java Persistence API Schemas share the namespace, http://xmlns.jcp.org/xml/ns/persistence/.
        // Previous versions used the namespace http://java.sun.com/xml/ns/persistence/.
        String xml =
                "<persistence xmlns=\"http://xmlns.jcp.org/xml/ns/persistence\"\n" +
                        "        xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "        xsi:schemaLocation=\"http://xmlns.jcp.org/xml/ns/persistence\n" +
                        "          http://xmlns.jcp.org/xml/ns/persistence/persistence_2_1.xsd\"\n" +
                        "        version=\"2.1\">\n" +
                        "   <persistence-unit name=\"testPersistenceUnit\" transaction-type=\"RESOURCE_LOCAL\">\n" +
                        "        <description>A description</description>\n" +
                        "        <provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>\n" +
                        "        <jta-data-source>java:Ds</jta-data-source>\n" +
                        "        <non-jta-data-source>NonJtaDataSource</non-jta-data-source>\n" +
                        "        <mapping-file>my-orm.xml</mapping-file>\n" +
                        "        <mapping-file>your-orm.xml</mapping-file>\n" +
                        "        <jar-file>some.jar</jar-file>\n" +
                        "        <jar-file>another.jar</jar-file>\n" +
                        "        <class>com.example.Entity1</class>\n" +
                        "        <class>com.example.Entity2</class>\n" +
                        "        <exclude-unlisted-classes>false</exclude-unlisted-classes>\n" +
                        // New in 2.0 <xsd:simpleType
                        // name="persistence-unit-caching-type">
                        "        <shared-cache-mode>DISABLE_SELECTIVE</shared-cache-mode>" +
                        // New in 2.0 <xsd:element name="validation-mode"...
                        "        <validation-mode>CALLBACK</validation-mode>\n" +
                        "        <properties>\n" +
                        "            <property name=\"javax.persistence.jdbc.driver\" value=\"com.mysql.cj.jdbc.Driver\" />\n" +
                        "            <property name=\"javax.persistence.jdbc.url\" value=\"jdbc:mysql://localhost:3306/db\" />\n" +
                        "            <property name=\"javax.persistence.jdbc.user\" value=\"username\" />\n" +
                        "        </properties>" +
                        "   </persistence-unit>" +
                        "</persistence>";

        Persistence persistence = getPersistence(xml);

        assertThat(persistence).isNotNull();
        assertThat(persistence.getVersion()).isEqualTo("2.1");
        assertThat(persistence.getPersistenceUnit()).isNotEmpty();
        Persistence.PersistenceUnit persistenceUnit = persistence.getPersistenceUnit().get(0);
        assertThat(persistenceUnit.getName()).isEqualTo("testPersistenceUnit");

        // new in 2.0
        assertThat(persistenceUnit.getSharedCacheMode()).isEqualTo(PersistenceUnitCachingType.DISABLE_SELECTIVE);
        // new in 2.0
        assertThat(persistenceUnit.getValidationMode()).isEqualTo(PersistenceUnitValidationModeType.CALLBACK);

        assertThat(persistenceUnit.getDescription()).isEqualTo("A description");
        assertThat(persistenceUnit.getProvider()).isEqualTo("org.hibernate.jpa.HibernatePersistenceProvider");
        assertThat(persistenceUnit.getJtaDataSource()).isEqualTo("java:Ds");
        assertThat(persistenceUnit.getNonJtaDataSource()).isEqualTo("NonJtaDataSource");
        assertThat(persistenceUnit.getMappingFile().get(0)).isEqualTo("my-orm.xml");
        assertThat(persistenceUnit.getMappingFile().get(1)).isEqualTo("your-orm.xml");
        assertThat(persistenceUnit.getJarFile().get(0)).isEqualTo("some.jar");
        assertThat(persistenceUnit.getJarFile().get(1)).isEqualTo("another.jar");
        assertThat(persistenceUnit.getClazz().get(0)).isEqualTo("com.example.Entity1");
        assertThat(persistenceUnit.getClazz().get(1)).isEqualTo("com.example.Entity2");
        assertThat(persistenceUnit.isExcludeUnlistedClasses()).isFalse();
        List<Persistence.PersistenceUnit.Properties.Property> properties = persistenceUnit.getProperties().getProperty();
        assertThat(properties.get(0).getName()).isEqualTo("javax.persistence.jdbc.driver");
        assertThat(properties.get(0).getValue()).isEqualTo("com.mysql.cj.jdbc.Driver");
        assertThat(properties.get(1).getName()).isEqualTo("javax.persistence.jdbc.url");
        assertThat(properties.get(1).getValue()).isEqualTo("jdbc:mysql://localhost:3306/db");
        assertThat(properties.get(2).getName()).isEqualTo("javax.persistence.jdbc.user");
        assertThat(properties.get(2).getValue()).isEqualTo("username");
        assertThat(persistenceUnit.getTransactionType()).isSameAs(PersistenceUnitTransactionType.RESOURCE_LOCAL);
    }

    @Test
    void persistenceXml_22() throws JAXBException {
        String xml =
                "<persistence xmlns=\"http://xmlns.jcp.org/xml/ns/persistence\"\n" +
                        "        xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "        xsi:schemaLocation=\"http://xmlns.jcp.org/xml/ns/persistence\n" +
                        "          http://xmlns.jcp.org/xml/ns/persistence/persistence_2_2.xsd\"\n" +
                        "        version=\"2.2\">\n" +
                        "   <persistence-unit name=\"testPersistenceUnit\" transaction-type=\"RESOURCE_LOCAL\">\n" +
                        "        <description>A description</description>\n" +
                        "        <provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>\n" +
                        "        <jta-data-source>java:Ds</jta-data-source>\n" +
                        "        <non-jta-data-source>NonJtaDataSource</non-jta-data-source>\n" +
                        "        <mapping-file>my-orm.xml</mapping-file>\n" +
                        "        <mapping-file>your-orm.xml</mapping-file>\n" +
                        "        <jar-file>some.jar</jar-file>\n" +
                        "        <jar-file>another.jar</jar-file>\n" +
                        "        <class>com.example.Entity1</class>\n" +
                        "        <class>com.example.Entity2</class>\n" +
                        "        <exclude-unlisted-classes>false</exclude-unlisted-classes>\n" +
                        // New in 2.0 <xsd:simpleType
                        // name="persistence-unit-caching-type">
                        "        <shared-cache-mode>DISABLE_SELECTIVE</shared-cache-mode>" +
                        // New in 2.0 <xsd:element name="validation-mode"...
                        "        <validation-mode>CALLBACK</validation-mode>\n" +
                        "        <properties>\n" +
                        "            <property name=\"javax.persistence.jdbc.driver\" value=\"com.mysql.cj.jdbc.Driver\" />\n" +
                        "            <property name=\"javax.persistence.jdbc.url\" value=\"jdbc:mysql://localhost:3306/db\" />\n" +
                        "            <property name=\"javax.persistence.jdbc.user\" value=\"username\" />\n" +
                        "        </properties>" +
                        "   </persistence-unit>" +
                        "</persistence>";

        Persistence persistence = getPersistence(xml);

        assertThat(persistence).isNotNull();
        assertThat(persistence.getVersion()).isEqualTo("2.2");
        assertThat(persistence.getPersistenceUnit()).isNotEmpty();
        Persistence.PersistenceUnit persistenceUnit = persistence.getPersistenceUnit().get(0);
        assertThat(persistenceUnit.getName()).isEqualTo("testPersistenceUnit");

        // new since 2.0
        assertThat(persistenceUnit.getSharedCacheMode()).isEqualTo(PersistenceUnitCachingType.DISABLE_SELECTIVE);
        // new since 2.0
        assertThat(persistenceUnit.getValidationMode()).isEqualTo(PersistenceUnitValidationModeType.CALLBACK);

        assertThat(persistenceUnit.getDescription()).isEqualTo("A description");
        assertThat(persistenceUnit.getProvider()).isEqualTo("org.hibernate.jpa.HibernatePersistenceProvider");
        assertThat(persistenceUnit.getJtaDataSource()).isEqualTo("java:Ds");
        assertThat(persistenceUnit.getNonJtaDataSource()).isEqualTo("NonJtaDataSource");
        assertThat(persistenceUnit.getMappingFile().get(0)).isEqualTo("my-orm.xml");
        assertThat(persistenceUnit.getMappingFile().get(1)).isEqualTo("your-orm.xml");
        assertThat(persistenceUnit.getJarFile().get(0)).isEqualTo("some.jar");
        assertThat(persistenceUnit.getJarFile().get(1)).isEqualTo("another.jar");
        assertThat(persistenceUnit.getClazz().get(0)).isEqualTo("com.example.Entity1");
        assertThat(persistenceUnit.getClazz().get(1)).isEqualTo("com.example.Entity2");
        assertThat(persistenceUnit.isExcludeUnlistedClasses()).isFalse();
        List<Persistence.PersistenceUnit.Properties.Property> properties = persistenceUnit.getProperties().getProperty();
        assertThat(properties.get(0).getName()).isEqualTo("javax.persistence.jdbc.driver");
        assertThat(properties.get(0).getValue()).isEqualTo("com.mysql.cj.jdbc.Driver");
        assertThat(properties.get(1).getName()).isEqualTo("javax.persistence.jdbc.url");
        assertThat(properties.get(1).getValue()).isEqualTo("jdbc:mysql://localhost:3306/db");
        assertThat(properties.get(2).getName()).isEqualTo("javax.persistence.jdbc.user");
        assertThat(properties.get(2).getValue()).isEqualTo("username");
        assertThat(persistenceUnit.getTransactionType()).isSameAs(PersistenceUnitTransactionType.RESOURCE_LOCAL);
    }

    private Persistence getPersistence(String xml) {
        Xml.Document xmlDocument = mock(Xml.Document.class);
        when(xmlDocument.printAll()).thenReturn(xml);
        PersistenceXml sut = new PersistenceXml(Path.of("./target/testdummy/src/main/resources/META-INF/persistence.xml").normalize().toAbsolutePath(), xmlDocument);
        Persistence persistence = sut.getPersistence();
        return persistence;
    }

}