
package org.mulesoft.schema.mule.core;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for asyncReplyCollectionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="asyncReplyCollectionType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.mulesoft.org/schema/mule/core}abstract-inbound-endpoint" maxOccurs="unbounded"/&gt;
 *         &lt;choice maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;element ref="{http://www.mulesoft.org/schema/mule/core}abstract-async-reply-router"/&gt;
 *           &lt;element ref="{http://www.mulesoft.org/schema/mule/core}abstract-inbound-router"/&gt;
 *           &lt;element ref="{http://www.mulesoft.org/schema/mule/core}processor"/&gt;
 *           &lt;element ref="{http://www.mulesoft.org/schema/mule/core}custom-processor"/&gt;
 *         &lt;/choice&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="timeout" type="{http://www.mulesoft.org/schema/mule/core}substitutableInt" /&gt;
 *       &lt;attribute name="failOnTimeout" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "asyncReplyCollectionType", propOrder = {
    "abstractInboundEndpoint",
    "abstractAsyncReplyRouterOrAbstractInboundRouterOrProcessor"
})
public class AsyncReplyCollectionType {

    @XmlElementRef(name = "abstract-inbound-endpoint", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class)
    protected List<JAXBElement<? extends AbstractInboundEndpointType>> abstractInboundEndpoint;
    @XmlElementRefs({
        @XmlElementRef(name = "abstract-async-reply-router", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "abstract-inbound-router", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "processor", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "custom-processor", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    })
    protected List<JAXBElement<?>> abstractAsyncReplyRouterOrAbstractInboundRouterOrProcessor;
    @XmlAttribute(name = "timeout")
    protected String timeout;
    @XmlAttribute(name = "failOnTimeout")
    protected String failOnTimeout;

    /**
     * Gets the value of the abstractInboundEndpoint property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the abstractInboundEndpoint property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAbstractInboundEndpoint().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.jms.InboundEndpointType }{@code >}
     * {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.tls.InboundEndpointType }{@code >}
     * {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.amqp.InboundEndpointType }{@code >}
     * {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.http.InboundEndpointType }{@code >}
     * {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.core.InboundEndpointType }{@code >}
     * {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.tcp.InboundEndpointType }{@code >}
     * {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.ee.wmq.InboundEndpointType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractInboundEndpointType }{@code >}
     * 
     * 
     */
    public List<JAXBElement<? extends AbstractInboundEndpointType>> getAbstractInboundEndpoint() {
        if (abstractInboundEndpoint == null) {
            abstractInboundEndpoint = new ArrayList<JAXBElement<? extends AbstractInboundEndpointType>>();
        }
        return this.abstractInboundEndpoint;
    }

    /**
     * Gets the value of the abstractAsyncReplyRouterOrAbstractInboundRouterOrProcessor property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the abstractAsyncReplyRouterOrAbstractInboundRouterOrProcessor property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAbstractAsyncReplyRouterOrAbstractInboundRouterOrProcessor().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link AsyncReplyRouterType }{@code >}
     * {@link JAXBElement }{@code <}{@link CustomAsyncReplyRouterType }{@code >}
     * {@link JAXBElement }{@code <}{@link AsyncReplyRouterType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractAsyncReplyRouterType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractInboundRouterType }{@code >}
     * {@link JAXBElement }{@code <}{@link FilteredInboundRouterType }{@code >}
     * {@link JAXBElement }{@code <}{@link WireTapRouterType }{@code >}
     * {@link JAXBElement }{@code <}{@link IdempotentReceiverType }{@code >}
     * {@link JAXBElement }{@code <}{@link BaseAggregatorRouterType }{@code >}
     * {@link JAXBElement }{@code <}{@link CustomInboundRouterType }{@code >}
     * {@link JAXBElement }{@code <}{@link CustomCorrelationAggregatorRouterType }{@code >}
     * {@link JAXBElement }{@code <}{@link IdempotentSecureHashReceiverType }{@code >}
     * {@link JAXBElement }{@code <}{@link BaseAggregatorRouterType }{@code >}
     * {@link JAXBElement }{@code <}{@link MessageChunkingAggregatorRouterType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractInboundRouterType }{@code >}
     * {@link JAXBElement }{@code <}{@link RefMessageProcessorType }{@code >}
     * {@link JAXBElement }{@code <}{@link CustomMessageProcessorType }{@code >}
     * 
     * 
     */
    public List<JAXBElement<?>> getAbstractAsyncReplyRouterOrAbstractInboundRouterOrProcessor() {
        if (abstractAsyncReplyRouterOrAbstractInboundRouterOrProcessor == null) {
            abstractAsyncReplyRouterOrAbstractInboundRouterOrProcessor = new ArrayList<JAXBElement<?>>();
        }
        return this.abstractAsyncReplyRouterOrAbstractInboundRouterOrProcessor;
    }

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
