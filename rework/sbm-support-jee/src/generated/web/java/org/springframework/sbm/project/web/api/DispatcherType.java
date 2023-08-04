
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
package org.springframework.sbm.project.web.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 
 *         The dispatcher has five legal values: FORWARD, REQUEST,
 *         INCLUDE, ASYNC, and ERROR.
 *         
 *         A value of FORWARD means the Filter will be applied under
 *         RequestDispatcher.forward() calls.
 *         A value of REQUEST means the Filter will be applied under
 *         ordinary client calls to the path or servlet.
 *         A value of INCLUDE means the Filter will be applied under
 *         RequestDispatcher.include() calls.
 *         A value of ASYNC means the Filter will be applied under
 *         calls dispatched from an AsyncContext.
 *         A value of ERROR means the Filter will be applied under the
 *         error page mechanism.
 *         
 *         The absence of any dispatcher elements in a filter-mapping
 *         indicates a default of applying filters only under ordinary
 *         client calls to the path or servlet.
 *         
 *       
 * 
 * <p>Java class for dispatcherType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="dispatcherType"&gt;
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
@XmlType(name = "dispatcherType")
public class DispatcherType
    extends String
{


}
