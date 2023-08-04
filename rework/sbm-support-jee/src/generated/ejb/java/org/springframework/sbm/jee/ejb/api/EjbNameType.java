
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
 *         The ejb-nameType specifies an enterprise bean's name. It is
 *         used by ejb-name elements. This name is assigned by the
 *         file producer to name the enterprise bean in the
 *         ejb-jar file or .war file's deployment descriptor. The name must be
 *         unique among the names of the enterprise beans in the same
 *         ejb-jar file or .war file.
 *         
 *         There is no architected relationship between the used
 *         ejb-name in the deployment descriptor and the JNDI name that
 *         the Deployer will assign to the enterprise bean's home.
 *         
 *         The name for an entity bean must conform to the lexical
 *         rules for an NMTOKEN.
 *         
 *         Example:
 *         
 *         <ejb-name>EmployeeService</ejb-name>
 *         
 *         
 *             
 * 
 * <p>Java class for ejb-nameType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ejb-nameType"&gt;
 *   &lt;simpleContent&gt;
 *     &lt;restriction base="&lt;http://xmlns.jcp.org/xml/ns/javaee&gt;xsdNMTOKENType"&gt;
 *     &lt;/restriction&gt;
 *   &lt;/simpleContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ejb-nameType")
public class EjbNameType
    extends XsdNMTOKENType
{


}
