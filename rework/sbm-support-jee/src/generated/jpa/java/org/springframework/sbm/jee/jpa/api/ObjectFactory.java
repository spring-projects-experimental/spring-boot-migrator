
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
package org.springframework.sbm.jee.jpa.api;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.springframework.sbm.jee.jpa.api package.
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.springframework.sbm.jee.jpa.api
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Persistence }
     * 
     */
    public Persistence createPersistence() {
        return new Persistence();
    }

    /**
     * Create an instance of {@link Persistence.PersistenceUnit }
     * 
     */
    public Persistence.PersistenceUnit createPersistencePersistenceUnit() {
        return new Persistence.PersistenceUnit();
    }

    /**
     * Create an instance of {@link Persistence.PersistenceUnit.Properties }
     * 
     */
    public Persistence.PersistenceUnit.Properties createPersistencePersistenceUnitProperties() {
        return new Persistence.PersistenceUnit.Properties();
    }

    /**
     * Create an instance of {@link Persistence.PersistenceUnit.Properties.Property }
     * 
     */
    public Persistence.PersistenceUnit.Properties.Property createPersistencePersistenceUnitPropertiesProperty() {
        return new Persistence.PersistenceUnit.Properties.Property();
    }

}
