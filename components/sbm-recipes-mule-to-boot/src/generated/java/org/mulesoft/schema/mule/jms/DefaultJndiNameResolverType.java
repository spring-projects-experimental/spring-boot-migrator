
package org.mulesoft.schema.mule.jms;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 *                 A deafult strategy to resolve JNDI names.
 *             
 * 
 * <p>Java class for defaultJndiNameResolverType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="defaultJndiNameResolverType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/jms}abstractJndiNameResolverType"&gt;
 *       &lt;attribute name="jndiInitialFactory" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="jndiProviderUrl" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="jndiProviderProperties-ref" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="initialContextFactory-ref" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "defaultJndiNameResolverType")
public class DefaultJndiNameResolverType
    extends AbstractJndiNameResolverType
{

    @XmlAttribute(name = "jndiInitialFactory", required = true)
    protected String jndiInitialFactory;
    @XmlAttribute(name = "jndiProviderUrl", required = true)
    protected String jndiProviderUrl;
    @XmlAttribute(name = "jndiProviderProperties-ref")
    protected String jndiProviderPropertiesRef;
    @XmlAttribute(name = "initialContextFactory-ref")
    protected String initialContextFactoryRef;

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
     * Gets the value of the jndiProviderPropertiesRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJndiProviderPropertiesRef() {
        return jndiProviderPropertiesRef;
    }

    /**
     * Sets the value of the jndiProviderPropertiesRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJndiProviderPropertiesRef(String value) {
        this.jndiProviderPropertiesRef = value;
    }

    /**
     * Gets the value of the initialContextFactoryRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInitialContextFactoryRef() {
        return initialContextFactoryRef;
    }

    /**
     * Sets the value of the initialContextFactoryRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInitialContextFactoryRef(String value) {
        this.initialContextFactoryRef = value;
    }

}
