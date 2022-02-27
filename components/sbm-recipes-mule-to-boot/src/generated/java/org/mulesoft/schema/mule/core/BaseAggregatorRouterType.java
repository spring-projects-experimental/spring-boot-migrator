
package org.mulesoft.schema.mule.core;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for baseAggregatorRouterType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="baseAggregatorRouterType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}filteredInboundRouterType"&gt;
 *       &lt;attribute name="timeout" type="{http://www.mulesoft.org/schema/mule/core}substitutableInt" /&gt;
 *       &lt;attribute name="failOnTimeout" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "baseAggregatorRouterType")
@XmlSeeAlso({
    MessageChunkingAggregatorRouterType.class
})
public class BaseAggregatorRouterType
    extends FilteredInboundRouterType
{

    @XmlAttribute(name = "timeout")
    protected String timeout;
    @XmlAttribute(name = "failOnTimeout")
    protected String failOnTimeout;

    /**
     * Gets the value of the timeout property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTimeout() {
        return timeout;
    }

    /**
     * Sets the value of the timeout property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTimeout(String value) {
        this.timeout = value;
    }

    /**
     * Gets the value of the failOnTimeout property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFailOnTimeout() {
        return failOnTimeout;
    }

    /**
     * Sets the value of the failOnTimeout property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFailOnTimeout(String value) {
        this.failOnTimeout = value;
    }

}
