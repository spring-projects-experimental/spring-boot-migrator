
package org.mulesoft.schema.mule.core;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for outboundRouterType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="outboundRouterType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}abstractOutboundRouterType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.mulesoft.org/schema/mule/core}abstract-outbound-endpoint"/&gt;
 *         &lt;group ref="{http://www.mulesoft.org/schema/mule/core}defaultOutboundRouterElements"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attGroup ref="{http://www.mulesoft.org/schema/mule/core}defaultCorrelationAttributes"/&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "outboundRouterType", propOrder = {
    "abstractOutboundEndpoint",
    "replyTo",
    "abstractTransaction"
})
public class OutboundRouterType
    extends AbstractOutboundRouterType
{

    @XmlElementRef(name = "abstract-outbound-endpoint", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class)
    protected JAXBElement<? extends AbstractOutboundEndpointType> abstractOutboundEndpoint;
    @XmlElement(name = "reply-to")
    protected EndpointRefType replyTo;
    @XmlElementRef(name = "abstract-transaction", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    protected JAXBElement<? extends AbstractTransactionType> abstractTransaction;
    @XmlAttribute(name = "enableCorrelation")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String enableCorrelation;

    /**
     * Gets the value of the abstractOutboundEndpoint property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.amqp.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.tcp.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.ee.wmq.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.jms.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.tls.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.core.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.http.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractOutboundEndpointType }{@code >}
     *     
     */
    public JAXBElement<? extends AbstractOutboundEndpointType> getAbstractOutboundEndpoint() {
        return abstractOutboundEndpoint;
    }

    /**
     * Sets the value of the abstractOutboundEndpoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.amqp.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.tcp.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.ee.wmq.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.jms.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.tls.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.core.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.http.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractOutboundEndpointType }{@code >}
     *     
     */
    public void setAbstractOutboundEndpoint(JAXBElement<? extends AbstractOutboundEndpointType> value) {
        this.abstractOutboundEndpoint = value;
    }

    /**
     * Gets the value of the replyTo property.
     * 
     * @return
     *     possible object is
     *     {@link EndpointRefType }
     *     
     */
    public EndpointRefType getReplyTo() {
        return replyTo;
    }

    /**
     * Sets the value of the replyTo property.
     * 
     * @param value
     *     allowed object is
     *     {@link EndpointRefType }
     *     
     */
    public void setReplyTo(EndpointRefType value) {
        this.replyTo = value;
    }

    /**
     * 
     *                         Defines an overall transaction that will be used for all endpoints on this router.  This is only useful when you want to define an outbound only transaction that will commit all of the transactions defined on the outbound endpoints for this router.  Note that you must still define a transaction on each of the endpoints that should take part in the transaction.  These transactions should always be configured to JOIN the existing transaction.
     *                     
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link BaseTransactionType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomTransactionType }{@code >}
     *     {@link JAXBElement }{@code <}{@link BaseTransactionType }{@code >}
     *     {@link JAXBElement }{@code <}{@link XaTransactionType }{@code >}
     *     {@link JAXBElement }{@code <}{@link BaseTransactionType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransactionType }{@code >}
     *     
     */
    public JAXBElement<? extends AbstractTransactionType> getAbstractTransaction() {
        return abstractTransaction;
    }

    /**
     * Sets the value of the abstractTransaction property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link BaseTransactionType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomTransactionType }{@code >}
     *     {@link JAXBElement }{@code <}{@link BaseTransactionType }{@code >}
     *     {@link JAXBElement }{@code <}{@link XaTransactionType }{@code >}
     *     {@link JAXBElement }{@code <}{@link BaseTransactionType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransactionType }{@code >}
     *     
     */
    public void setAbstractTransaction(JAXBElement<? extends AbstractTransactionType> value) {
        this.abstractTransaction = value;
    }

    /**
     * Gets the value of the enableCorrelation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnableCorrelation() {
        if (enableCorrelation == null) {
            return "IF_NOT_SET";
        } else {
            return enableCorrelation;
        }
    }

    /**
     * Sets the value of the enableCorrelation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnableCorrelation(String value) {
        this.enableCorrelation = value;
    }

}
