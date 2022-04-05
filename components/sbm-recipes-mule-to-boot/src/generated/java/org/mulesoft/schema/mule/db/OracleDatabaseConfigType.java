
package org.mulesoft.schema.mule.db;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for oracleDatabaseConfigType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="oracleDatabaseConfigType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/db}abstractUserAndPasswordDatabaseConfigType"&gt;
 *       &lt;attribute name="host" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="port" type="{http://www.mulesoft.org/schema/mule/core}substitutableInt" /&gt;
 *       &lt;attribute name="instance" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "oracleDatabaseConfigType")
public class OracleDatabaseConfigType
    extends AbstractUserAndPasswordDatabaseConfigType
{

    @XmlAttribute(name = "host")
    protected String host;
    @XmlAttribute(name = "port")
    protected String port;
    @XmlAttribute(name = "instance")
    protected String instance;

    /**
     * Gets the value of the host property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHost() {
        return host;
    }

    /**
     * Sets the value of the host property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHost(String value) {
        this.host = value;
    }

    /**
     * Gets the value of the port property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPort() {
        return port;
    }

    /**
     * Sets the value of the port property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPort(String value) {
        this.port = value;
    }

    /**
     * Gets the value of the instance property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInstance() {
        return instance;
    }

    /**
     * Sets the value of the instance property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInstance(String value) {
        this.instance = value;
    }

}
