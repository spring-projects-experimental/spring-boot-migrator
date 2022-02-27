
package org.mulesoft.schema.mule.core;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for jndiConnectorType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="jndiConnectorType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}connectorType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="jndi-provider-property" type="{http://www.mulesoft.org/schema/mule/core}keyValueType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="jndi-provider-properties" type="{http://www.mulesoft.org/schema/mule/core}mapType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="jndiContext-ref" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="jndiInitialFactory" type="{http://www.mulesoft.org/schema/mule/core}substitutableClass" /&gt;
 *       &lt;attribute name="jndiProviderUrl" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="jndiUrlPkgPrefixes" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "jndiConnectorType", propOrder = {
    "jndiProviderProperty",
    "jndiProviderProperties"
})
public class JndiConnectorType
    extends ConnectorType
{

    @XmlElement(name = "jndi-provider-property")
    protected List<KeyValueType> jndiProviderProperty;
    @XmlElement(name = "jndi-provider-properties")
    protected MapType jndiProviderProperties;
    @XmlAttribute(name = "jndiContext-ref")
    protected String jndiContextRef;
    @XmlAttribute(name = "jndiInitialFactory")
    protected String jndiInitialFactory;
    @XmlAttribute(name = "jndiProviderUrl")
    protected String jndiProviderUrl;
    @XmlAttribute(name = "jndiUrlPkgPrefixes")
    protected String jndiUrlPkgPrefixes;

    /**
     * Gets the value of the jndiProviderProperty property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the jndiProviderProperty property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getJndiProviderProperty().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link KeyValueType }
     * 
     * 
     */
    public List<KeyValueType> getJndiProviderProperty() {
        if (jndiProviderProperty == null) {
            jndiProviderProperty = new ArrayList<KeyValueType>();
        }
        return this.jndiProviderProperty;
    }

    /**
     * Gets the value of the jndiProviderProperties property.
     * 
     * @return
     *     possible object is
     *     {@link MapType }
     *     
     */
    public MapType getJndiProviderProperties() {
        return jndiProviderProperties;
    }

    /**
     * Sets the value of the jndiProviderProperties property.
     * 
     * @param value
     *     allowed object is
     *     {@link MapType }
     *     
     */
    public void setJndiProviderProperties(MapType value) {
        this.jndiProviderProperties = value;
    }

    /**
     * Gets the value of the jndiContextRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJndiContextRef() {
        return jndiContextRef;
    }

    /**
     * Sets the value of the jndiContextRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJndiContextRef(String value) {
        this.jndiContextRef = value;
    }

    /**
     * Gets the value of the jndiInitialFactory property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJndiInitialFactory() {
        return jndiInitialFactory;
    }

    /**
     * Sets the value of the jndiInitialFactory property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJndiInitialFactory(String value) {
        this.jndiInitialFactory = value;
    }

    /**
     * Gets the value of the jndiProviderUrl property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJndiProviderUrl() {
        return jndiProviderUrl;
    }

    /**
     * Sets the value of the jndiProviderUrl property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJndiProviderUrl(String value) {
        this.jndiProviderUrl = value;
    }

    /**
     * Gets the value of the jndiUrlPkgPrefixes property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJndiUrlPkgPrefixes() {
        return jndiUrlPkgPrefixes;
    }

    /**
     * Sets the value of the jndiUrlPkgPrefixes property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJndiUrlPkgPrefixes(String value) {
        this.jndiUrlPkgPrefixes = value;
    }

}
