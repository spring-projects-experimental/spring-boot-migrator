
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
import javax.xml.bind.annotation.XmlType;


/**
 * 
 *                 [
 *         This type contains the fully-qualified Java type of the
 *         environment entry value that is expected by the
 *         application's code.
 *         
 *         The following are the legal values of env-entry-type-valuesType:
 *         
 *         java.lang.Boolean
 *         java.lang.Byte
 *         java.lang.Character
 *         java.lang.String
 *         java.lang.Short
 *         java.lang.Integer
 *         java.lang.Long
 *         java.lang.Float
 *         java.lang.Double
 *         		  java.lang.Class
 *         		  any enumeration type (i.e. a subclass of java.lang.Enum)
 *         
 *         Examples:
 *         
 *         <env-entry-type>java.lang.Boolean</env-entry-type>
 *         <env-entry-type>java.lang.Class</env-entry-type>
 *         <env-entry-type>com.example.Color</env-entry-type>
 *         
 *         
 *             
 * 
 * <p>Java class for env-entry-type-valuesType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="env-entry-type-valuesType"&gt;
 *   &lt;simpleContent&gt;
 *     &lt;restriction base="&lt;http://xmlns.jcp.org/xml/ns/javaee&gt;fully-qualified-classType"&gt;
 *     &lt;/restriction&gt;
 *   &lt;/simpleContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "env-entry-type-valuesType")
public class EnvEntryTypeValuesType
    extends FullyQualifiedClassType
{


}
