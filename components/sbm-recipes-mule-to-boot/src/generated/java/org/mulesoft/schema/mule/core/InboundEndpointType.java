
package org.mulesoft.schema.mule.core;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for inboundEndpointType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="inboundEndpointType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}inboundEndpointTypeWithoutExchangePattern"&gt;
 *       &lt;attGroup ref="{http://www.mulesoft.org/schema/mule/core}allExchangePatterns"/&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "inboundEndpointType")
@XmlSeeAlso({
    org.mulesoft.schema.mule.amqp.InboundEndpointType.class,
    org.mulesoft.schema.mule.http.InboundEndpointType.class,
    org.mulesoft.schema.mule.tcp.InboundEndpointType.class,
    org.mulesoft.schema.mule.tls.InboundEndpointType.class
})
public class InboundEndpointType
    extends InboundEndpointTypeWithoutExchangePattern
{

    @XmlAttribute(name = "exchange-pattern")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String exchangePattern;

    /**
     * Gets the value of the exchangePattern property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExchangePattern() {
        return exchangePattern;
    }

    /**
     * Sets the value of the exchangePattern property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExchangePattern(String value) {
        this.exchangePattern = value;
    }

}
