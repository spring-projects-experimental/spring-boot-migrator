
package org.mulesoft.schema.mule.http;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import org.mulesoft.schema.mule.core.AbstractInterceptingMessageProcessorType;


/**
 * <p>Java class for staticResourceHandlerType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="staticResourceHandlerType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}abstractInterceptingMessageProcessorType"&gt;
 *       &lt;attribute name="resourceBase" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="defaultFile" type="{http://www.w3.org/2001/XMLSchema}string" default="index.html" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "staticResourceHandlerType")
public class StaticResourceHandlerType
    extends AbstractInterceptingMessageProcessorType
{

    @XmlAttribute(name = "resourceBase", required = true)
    protected String resourceBase;
    @XmlAttribute(name = "defaultFile")
    protected String defaultFile;

    /**
     * Gets the value of the resourceBase property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResourceBase() {
        return resourceBase;
    }

    /**
     * Sets the value of the resourceBase property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResourceBase(String value) {
        this.resourceBase = value;
    }

    /**
     * Gets the value of the defaultFile property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDefaultFile() {
        if (defaultFile == null) {
            return "index.html";
        } else {
            return defaultFile;
        }
    }

    /**
     * Sets the value of the defaultFile property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDefaultFile(String value) {
        this.defaultFile = value;
    }

}
