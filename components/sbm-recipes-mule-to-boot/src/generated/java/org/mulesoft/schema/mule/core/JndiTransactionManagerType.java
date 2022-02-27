
package org.mulesoft.schema.mule.core;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for jndiTransactionManagerType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="jndiTransactionManagerType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}abstractTransactionManagerType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="environment" type="{http://www.mulesoft.org/schema/mule/core}mapType" minOccurs="0"/&gt;
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
@XmlType(name = "jndiTransactionManagerType", propOrder = {
    "environment"
})
@XmlSeeAlso({
    JndiTransactionManager.class,
    CustomTransactionManagerType.class
})
public class JndiTransactionManagerType
    extends AbstractTransactionManagerType
{

    protected MapType environment;

    /**
     * Gets the value of the environment property.
     * 
     * @return
     *     possible object is
     *     {@link MapType }
     *     
     */
    public MapType getEnvironment() {
        return environment;
    }

    /**
     * Sets the value of the environment property.
     * 
     * @param value
     *     allowed object is
     *     {@link MapType }
     *     
     */
    public void setEnvironment(MapType value) {
        this.environment = value;
    }

}
