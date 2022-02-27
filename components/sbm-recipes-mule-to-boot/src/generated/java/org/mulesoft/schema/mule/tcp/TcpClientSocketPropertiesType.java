
package org.mulesoft.schema.mule.tcp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tcpClientSocketPropertiesType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tcpClientSocketPropertiesType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/tcp}tcpAbstractSocketPropertiesType"&gt;
 *       &lt;attribute name="connectionTimeout" type="{http://www.mulesoft.org/schema/mule/core}substitutableInt" default="30000" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tcpClientSocketPropertiesType")
public class TcpClientSocketPropertiesType
    extends TcpAbstractSocketPropertiesType
{

    @XmlAttribute(name = "connectionTimeout")
    protected String connectionTimeout;

    /**
     * Gets the value of the connectionTimeout property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConnectionTimeout() {
        if (connectionTimeout == null) {
            return "30000";
        } else {
            return connectionTimeout;
        }
    }

    /**
     * Sets the value of the connectionTimeout property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConnectionTimeout(String value) {
        this.connectionTimeout = value;
    }

}
