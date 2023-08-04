
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

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 
 *         Please see section 8.2.2 of the specification for details.
 *         
 *       
 * 
 * <p>Java class for absoluteOrderingType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="absoluteOrderingType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;choice maxOccurs="unbounded" minOccurs="0"&gt;
 *         &lt;element name="name" type="{http://xmlns.jcp.org/xml/ns/javaee}java-identifierType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="others" type="{http://xmlns.jcp.org/xml/ns/javaee}ordering-othersType" minOccurs="0"/&gt;
 *       &lt;/choice&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "absoluteOrderingType", propOrder = {
    "nameOrOthers"
})
public class AbsoluteOrderingType {

    @XmlElements({
        @XmlElement(name = "name", type = JavaIdentifierType.class),
        @XmlElement(name = "others", type = OrderingOthersType.class)
    })
    protected List<Object> nameOrOthers;

    /**
     * Gets the value of the nameOrOthers property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the nameOrOthers property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNameOrOthers().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JavaIdentifierType }
     * {@link OrderingOthersType }
     * 
     * 
     */
    public List<Object> getNameOrOthers() {
        if (nameOrOthers == null) {
            nameOrOthers = new ArrayList<Object>();
        }
        return this.nameOrOthers;
    }

}
