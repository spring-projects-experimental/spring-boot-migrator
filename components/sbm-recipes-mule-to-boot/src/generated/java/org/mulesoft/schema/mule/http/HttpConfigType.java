
package org.mulesoft.schema.mule.http;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import org.mulesoft.schema.mule.core.AbstractConfigurationExtensionType;


/**
 * <p>Java class for httpConfigType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="httpConfigType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}abstractConfigurationExtensionType"&gt;
 *       &lt;attribute name="useTransportForUris" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" default="false" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "httpConfigType")
public class HttpConfigType
    extends AbstractConfigurationExtensionType
{

    @XmlAttribute(name = "useTransportForUris")
    protected String useTransportForUris;

    /**
     * Gets the value of the useTransportForUris property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUseTransportForUris() {
        if (useTransportForUris == null) {
            return "false";
        } else {
            return useTransportForUris;
        }
    }

    /**
     * Sets the value of the useTransportForUris property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUseTransportForUris(String value) {
        this.useTransportForUris = value;
    }

}
