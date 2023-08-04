
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
 *         The ejb-classType contains the fully-qualified name of the
 *         enterprise bean's class. It is used by ejb-class elements. 
 *         
 *         Example:
 *         
 *         <ejb-class>com.wombat.empl.EmployeeServiceBean</ejb-class>
 *         
 *         
 *             
 * 
 * <p>Java class for ejb-classType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ejb-classType"&gt;
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
@XmlType(name = "ejb-classType")
public class EjbClassType
    extends FullyQualifiedClassType
{


}
