
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
package org.springframework.sbm.jee.ejb.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 
 *                 An injection target specifies a class and a name within
 *                 that class into which a resource should be injected.
 * 
 *                 The injection target class specifies the fully qualified
 *                 class name that is the target of the injection. The
 *                 Java EE specifications describe which classes can be an
 *                 injection target.
 * 
 *                 The injection target name specifies the target within
 *                 the specified class. The target is first looked for as a
 *                 JavaBeans property name. If not found, the target is
 *                 looked for as a field name.
 * 
 *                 The specified resource will be injected into the target
 *                 during initialization of the class by either calling the
 *                 set method for the target property or by setting a value
 *                 into the named field.
 * 
 *             
 * 
 * <p>Java class for injection-targetType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="injection-targetType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="injection-target-class" type="{http://xmlns.jcp.org/xml/ns/javaee}fully-qualified-classType"/&gt;
 *         &lt;element name="injection-target-name" type="{http://xmlns.jcp.org/xml/ns/javaee}java-identifierType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "injection-targetType", propOrder = {
    "injectionTargetClass",
    "injectionTargetName"
})
public class InjectionTargetType {

    @XmlElement(name = "injection-target-class", required = true)
    protected FullyQualifiedClassType injectionTargetClass;
    @XmlElement(name = "injection-target-name", required = true)
    protected JavaIdentifierType injectionTargetName;

    /**
     * Gets the value of the injectionTargetClass property.
     * 
     * @return
     *     possible object is
     *     {@link FullyQualifiedClassType }
     *     
     */
    public FullyQualifiedClassType getInjectionTargetClass() {
        return injectionTargetClass;
    }

    /**
     * Sets the value of the injectionTargetClass property.
     * 
     * @param value
     *     allowed object is
     *     {@link FullyQualifiedClassType }
     *     
     */
    public void setInjectionTargetClass(FullyQualifiedClassType value) {
        this.injectionTargetClass = value;
    }

    /**
     * Gets the value of the injectionTargetName property.
     * 
     * @return
     *     possible object is
     *     {@link JavaIdentifierType }
     *     
     */
    public JavaIdentifierType getInjectionTargetName() {
        return injectionTargetName;
    }

    /**
     * Sets the value of the injectionTargetName property.
     * 
     * @param value
     *     allowed object is
     *     {@link JavaIdentifierType }
     *     
     */
    public void setInjectionTargetName(JavaIdentifierType value) {
        this.injectionTargetName = value;
    }

}
