
package org.mulesoft.schema.mule.core;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for invokeType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="invokeType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}abstractMessageProcessorType"&gt;
 *       &lt;attribute name="object-ref" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="method" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="methodArguments" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="methodArgumentTypes" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="name" type="{http://www.mulesoft.org/schema/mule/core}substitutableName" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "invokeType")
public class InvokeType
    extends AbstractMessageProcessorType
{

    @XmlAttribute(name = "object-ref")
    protected String objectRef;
    @XmlAttribute(name = "method")
    protected String method;
    @XmlAttribute(name = "methodArguments")
    protected String methodArguments;
    @XmlAttribute(name = "methodArgumentTypes")
    protected String methodArgumentTypes;
    @XmlAttribute(name = "name")
    protected String name;

    /**
     * Gets the value of the objectRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getObjectRef() {
        return objectRef;
    }

    /**
     * Sets the value of the objectRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setObjectRef(String value) {
        this.objectRef = value;
    }

    /**
     * Gets the value of the method property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMethod() {
        return method;
    }

    /**
     * Sets the value of the method property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMethod(String value) {
        this.method = value;
    }

    /**
     * Gets the value of the methodArguments property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMethodArguments() {
        return methodArguments;
    }

    /**
     * Sets the value of the methodArguments property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMethodArguments(String value) {
        this.methodArguments = value;
    }

    /**
     * Gets the value of the methodArgumentTypes property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMethodArgumentTypes() {
        return methodArgumentTypes;
    }

    /**
     * Sets the value of the methodArgumentTypes property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMethodArgumentTypes(String value) {
        this.methodArgumentTypes = value;
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
