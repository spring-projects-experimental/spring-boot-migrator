
package org.mulesoft.schema.mule.db;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for advancedDbMessageProcessorType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="advancedDbMessageProcessorType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/db}abstractDbMessageProcessorType"&gt;
 *       &lt;sequence&gt;
 *         &lt;group ref="{http://www.mulesoft.org/schema/mule/db}parameterizedQuery"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "advancedDbMessageProcessorType", propOrder = {
    "templateQueryRef",
    "inParam",
    "parameterizedQuery",
    "queryParameter",
    "dynamicQuery"
})
@XmlSeeAlso({
    SelectMessageProcessorType.class,
    UpdateMessageProcessorType.class,
    InsertMessageProcessorType.class
})
public class AdvancedDbMessageProcessorType
    extends AbstractDbMessageProcessorType
{

    @XmlElement(name = "template-query-ref")
    protected TemplateRefType templateQueryRef;
    @XmlElement(name = "in-param")
    protected List<InputParamType> inParam;
    @XmlElement(name = "parameterized-query")
    protected String parameterizedQuery;
    @XmlElement(name = "in-param")
    protected List<InputParamType> queryParameter;
    @XmlElement(name = "dynamic-query")
    protected String dynamicQuery;

    /**
     * Gets the value of the templateQueryRef property.
     * 
     * @return
     *     possible object is
     *     {@link TemplateRefType }
     *     
     */
    public TemplateRefType getTemplateQueryRef() {
        return templateQueryRef;
    }

    /**
     * Sets the value of the templateQueryRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link TemplateRefType }
     *     
     */
    public void setTemplateQueryRef(TemplateRefType value) {
        this.templateQueryRef = value;
    }

    /**
     * Gets the value of the inParam property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the inParam property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInParam().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link InputParamType }
     * 
     * 
     */
    public List<InputParamType> getInParam() {
        if (inParam == null) {
            inParam = new ArrayList<InputParamType>();
        }
        return this.inParam;
    }

    /**
     * Gets the value of the parameterizedQuery property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParameterizedQuery() {
        return parameterizedQuery;
    }

    /**
     * Sets the value of the parameterizedQuery property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParameterizedQuery(String value) {
        this.parameterizedQuery = value;
    }

    /**
     * Gets the value of the queryParameter property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the queryParameter property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getQueryParameter().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link InputParamType }
     * 
     * 
     */
    public List<InputParamType> getQueryParameter() {
        if (queryParameter == null) {
            queryParameter = new ArrayList<InputParamType>();
        }
        return this.queryParameter;
    }

    /**
     * Gets the value of the dynamicQuery property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDynamicQuery() {
        return dynamicQuery;
    }

    /**
     * Sets the value of the dynamicQuery property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDynamicQuery(String value) {
        this.dynamicQuery = value;
    }

}
