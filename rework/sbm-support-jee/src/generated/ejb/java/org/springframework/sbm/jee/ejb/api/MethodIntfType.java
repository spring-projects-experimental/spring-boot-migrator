
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
 * 
 *                 The method-intf element allows a method element to
 *                 differentiate between the methods with the same name and
 *                 signature that are multiply defined across the home and
 *                 component interfaces (e.g, in both an enterprise bean's
 *                 remote and local interfaces or in both an enterprise bean's
 *                 home and remote interfaces, etc.); the component and web
 *                 service endpoint interfaces, and so on.
 * 
 *                 Local applies to the local component interface, local business
 *                 interfaces, and the no-interface view.
 * 
 *                 Remote applies to both remote component interface and the remote
 *                 business interfaces.
 * 
 *                 ServiceEndpoint refers to methods exposed through a web service
 *                 endpoint.
 * 
 *                 Timer refers to the bean's timeout callback methods.
 * 
 *                 MessageEndpoint refers to the methods of a message-driven bean's
 *                 message-listener interface.
 * 
 *                 LifecycleCallback refers to the PostConstruct and PreDestroy
 *                 lifecycle callback methods of a singleton session bean and
 *                 to the PostConstruct, PreDestroy, PrePassivate, and PostActivate
 *                 lifecycle callback methods of a stateful session bean.
 * 
 *                 The method-intf element must be one of the following:
 * 
 *                 Home
 *                 Remote
 *                 LocalHome
 *                 Local
 *                 ServiceEndpoint
 *                 Timer
 *                 MessageEndpoint
 *                 LifecycleCallback
 * 
 *             
 * 
 * <p>Java class for method-intfType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="method-intfType"&gt;
 *   &lt;simpleContent&gt;
 *     &lt;restriction base="&lt;http://xmlns.jcp.org/xml/ns/javaee&gt;string"&gt;
 *     &lt;/restriction&gt;
 *   &lt;/simpleContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "method-intfType")
public class MethodIntfType
    extends String
{


}
