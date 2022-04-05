
package org.mulesoft.schema.mule.db;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlType;
import org.mulesoft.schema.mule.core.AbstractExtensionType;


/**
 * <p>Java class for TemplateSqlDefinitionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TemplateSqlDefinitionType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}abstractExtensionType"&gt;
 *       &lt;sequence&gt;
 *         &lt;choice&gt;
 *           &lt;sequence&gt;
 *             &lt;element name="parameterized-query" type="{http://www.mulesoft.org/schema/mule/db}ParameterizedQueryDefinitionType"/&gt;
 *             &lt;element name="in-param" type="{http://www.mulesoft.org/schema/mule/db}TemplateInputParamType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *           &lt;/sequence&gt;
 *           &lt;element name="dynamic-query" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *           &lt;sequence&gt;
 *             &lt;element name="template-query-ref" type="{http://www.mulesoft.org/schema/mule/db}TemplateRefType"/&gt;
 *             &lt;element name="in-param" type="{http://www.mulesoft.org/schema/mule/db}OverriddenTemplateInputParamType" maxOccurs="unbounded"/&gt;
 *           &lt;/sequence&gt;
 *         &lt;/choice&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="name" use="required" type="{http://www.mulesoft.org/schema/mule/core}substitutableName" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TemplateSqlDefinitionType", propOrder = {
    "rest"
})
public class TemplateSqlDefinitionType
    extends AbstractExtensionType
{

    @XmlElementRefs({
        @XmlElementRef(name = "parameterized-query", namespace = "http://www.mulesoft.org/schema/mule/db", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "in-param", namespace = "http://www.mulesoft.org/schema/mule/db", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "dynamic-query", namespace = "http://www.mulesoft.org/schema/mule/db", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "template-query-ref", namespace = "http://www.mulesoft.org/schema/mule/db", type = JAXBElement.class, required = false)
    })
    protected List<JAXBElement<?>> rest;
    @XmlAttribute(name = "name", required = true)
    protected String name;

    /**
     * Gets the rest of the content model. 
     * 
     * <p>
     * You are getting this "catch-all" property because of the following reason: 
     * The field name "InParam" is used by two different parts of a schema. See: 
     * line 337 of file:/Users/sanagaraj/workspace/opensource/spring-boot-migrator/components/sbm-recipes-mule-to-boot/src/main/xsd/mule/mule-db.xsd
     * line 330 of file:/Users/sanagaraj/workspace/opensource/spring-boot-migrator/components/sbm-recipes-mule-to-boot/src/main/xsd/mule/mule-db.xsd
     * <p>
     * To get rid of this property, apply a property customization to one 
     * of both of the following declarations to change their names: 
     * Gets the value of the rest property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the rest property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRest().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link ParameterizedQueryDefinitionType }{@code >}
     * {@link JAXBElement }{@code <}{@link TemplateInputParamType }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link TemplateRefType }{@code >}
     * 
     * 
     */
    public List<JAXBElement<?>> getRest() {
        if (rest == null) {
            rest = new ArrayList<JAXBElement<?>>();
        }
        return this.rest;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

}
