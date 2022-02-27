
package org.mulesoft.schema.mule.core;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for roundRobinSplitterType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="roundRobinSplitterType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}messageSplitterOutboundRouterType"&gt;
 *       &lt;attribute name="deterministic" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" /&gt;
 *       &lt;attribute name="disableRoundRobin" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" /&gt;
 *       &lt;attribute name="failIfNoMatch" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "roundRobinSplitterType")
@XmlSeeAlso({
    ExpressionSplitterOutboundRouterType.class
})
public class RoundRobinSplitterType
    extends MessageSplitterOutboundRouterType
{

    @XmlAttribute(name = "deterministic")
    protected String deterministic;
    @XmlAttribute(name = "disableRoundRobin")
    protected String disableRoundRobin;
    @XmlAttribute(name = "failIfNoMatch")
    protected String failIfNoMatch;

    /**
     * Gets the value of the deterministic property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeterministic() {
        return deterministic;
    }

    /**
     * Sets the value of the deterministic property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeterministic(String value) {
        this.deterministic = value;
    }

    /**
     * Gets the value of the disableRoundRobin property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDisableRoundRobin() {
        return disableRoundRobin;
    }

    /**
     * Sets the value of the disableRoundRobin property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDisableRoundRobin(String value) {
        this.disableRoundRobin = value;
    }

    /**
     * Gets the value of the failIfNoMatch property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFailIfNoMatch() {
        return failIfNoMatch;
    }

    /**
     * Sets the value of the failIfNoMatch property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFailIfNoMatch(String value) {
        this.failIfNoMatch = value;
    }

}
