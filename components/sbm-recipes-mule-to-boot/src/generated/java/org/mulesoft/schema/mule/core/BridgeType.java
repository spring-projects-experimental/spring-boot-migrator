
package org.mulesoft.schema.mule.core;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * 
 *                 A bridge with a single inbound endpoint and a single outbound endpoint.
 *             
 * 
 * <p>Java class for bridgeType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="bridgeType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}baseFlowConstructType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.mulesoft.org/schema/mule/core}abstract-inbound-endpoint" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.mulesoft.org/schema/mule/core}abstract-outbound-endpoint" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.mulesoft.org/schema/mule/core}legacy-abstract-exception-strategy" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attGroup ref="{http://www.mulesoft.org/schema/mule/core}allExchangePatterns"/&gt;
 *       &lt;attribute name="inboundAddress" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="inboundEndpoint-ref" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="outboundAddress" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="outboundEndpoint-ref" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="transformer-refs" type="{http://www.w3.org/2001/XMLSchema}NMTOKENS" /&gt;
 *       &lt;attribute name="responseTransformer-refs" type="{http://www.w3.org/2001/XMLSchema}NMTOKENS" /&gt;
 *       &lt;attribute name="transacted" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "bridgeType", propOrder = {
    "abstractInboundEndpoint",
    "abstractOutboundEndpoint",
    "legacyAbstractExceptionStrategy"
})
public class BridgeType
    extends BaseFlowConstructType
{

    @XmlElementRef(name = "abstract-inbound-endpoint", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    protected JAXBElement<? extends AbstractInboundEndpointType> abstractInboundEndpoint;
    @XmlElementRef(name = "abstract-outbound-endpoint", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    protected JAXBElement<? extends AbstractOutboundEndpointType> abstractOutboundEndpoint;
    @XmlElementRef(name = "legacy-abstract-exception-strategy", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    protected JAXBElement<? extends ExceptionStrategyType> legacyAbstractExceptionStrategy;
    @XmlAttribute(name = "inboundAddress")
    protected String inboundAddress;
    @XmlAttribute(name = "inboundEndpoint-ref")
    protected String inboundEndpointRef;
    @XmlAttribute(name = "outboundAddress")
    protected String outboundAddress;
    @XmlAttribute(name = "outboundEndpoint-ref")
    protected String outboundEndpointRef;
    @XmlAttribute(name = "transformer-refs")
    @XmlSchemaType(name = "NMTOKENS")
    protected List<String> transformerRefs;
    @XmlAttribute(name = "responseTransformer-refs")
    @XmlSchemaType(name = "NMTOKENS")
    protected List<String> responseTransformerRefs;
    @XmlAttribute(name = "transacted")
    protected Boolean transacted;
    @XmlAttribute(name = "exchange-pattern")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String exchangePattern;

    /**
     * 
     *                                 The endpoint on which messages are received.
     *                             
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.ee.wmq.InboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.jms.InboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.tls.InboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.amqp.InboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.core.InboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.http.InboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.tcp.InboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractInboundEndpointType }{@code >}
     *     
     */
    public JAXBElement<? extends AbstractInboundEndpointType> getAbstractInboundEndpoint() {
        return abstractInboundEndpoint;
    }

    /**
     * Sets the value of the abstractInboundEndpoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.ee.wmq.InboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.jms.InboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.tls.InboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.amqp.InboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.core.InboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.http.InboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.tcp.InboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractInboundEndpointType }{@code >}
     *     
     */
    public void setAbstractInboundEndpoint(JAXBElement<? extends AbstractInboundEndpointType> value) {
        this.abstractInboundEndpoint = value;
    }

    /**
     * 
     *                                 The endpoint to which messages are sent.
     *                             
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.amqp.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.tls.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.core.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.ee.wmq.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.http.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.tcp.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.jms.OutboundEndpointType }{@code >}
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
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.tls.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.core.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.ee.wmq.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.http.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.tcp.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.jms.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractOutboundEndpointType }{@code >}
     *     
     */
    public void setAbstractOutboundEndpoint(JAXBElement<? extends AbstractOutboundEndpointType> value) {
        this.abstractOutboundEndpoint = value;
    }

    /**
     * Gets the value of the legacyAbstractExceptionStrategy property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ReferenceExceptionStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ChoiceExceptionStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link RollbackExceptionStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CatchExceptionStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomExceptionStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ExceptionStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ServiceExceptionStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ServiceExceptionStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ExceptionStrategyType }{@code >}
     *     
     */
    public JAXBElement<? extends ExceptionStrategyType> getLegacyAbstractExceptionStrategy() {
        return legacyAbstractExceptionStrategy;
    }

    /**
     * Sets the value of the legacyAbstractExceptionStrategy property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ReferenceExceptionStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ChoiceExceptionStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link RollbackExceptionStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CatchExceptionStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomExceptionStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ExceptionStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ServiceExceptionStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ServiceExceptionStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ExceptionStrategyType }{@code >}
     *     
     */
    public void setLegacyAbstractExceptionStrategy(JAXBElement<? extends ExceptionStrategyType> value) {
        this.legacyAbstractExceptionStrategy = value;
    }

    /**
     * Gets the value of the inboundAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInboundAddress() {
        return inboundAddress;
    }

    /**
     * Sets the value of the inboundAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInboundAddress(String value) {
        this.inboundAddress = value;
    }

    /**
     * Gets the value of the inboundEndpointRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInboundEndpointRef() {
        return inboundEndpointRef;
    }

    /**
     * Sets the value of the inboundEndpointRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInboundEndpointRef(String value) {
        this.inboundEndpointRef = value;
    }

    /**
     * Gets the value of the outboundAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOutboundAddress() {
        return outboundAddress;
    }

    /**
     * Sets the value of the outboundAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOutboundAddress(String value) {
        this.outboundAddress = value;
    }

    /**
     * Gets the value of the outboundEndpointRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOutboundEndpointRef() {
        return outboundEndpointRef;
    }

    /**
     * Sets the value of the outboundEndpointRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOutboundEndpointRef(String value) {
        this.outboundEndpointRef = value;
    }

    /**
     * Gets the value of the transformerRefs property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the transformerRefs property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTransformerRefs().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getTransformerRefs() {
        if (transformerRefs == null) {
            transformerRefs = new ArrayList<String>();
        }
        return this.transformerRefs;
    }

    /**
     * Gets the value of the responseTransformerRefs property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the responseTransformerRefs property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getResponseTransformerRefs().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getResponseTransformerRefs() {
        if (responseTransformerRefs == null) {
            responseTransformerRefs = new ArrayList<String>();
        }
        return this.responseTransformerRefs;
    }

    /**
     * Gets the value of the transacted property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTransacted() {
        return transacted;
    }

    /**
     * Sets the value of the transacted property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTransacted(Boolean value) {
        this.transacted = value;
    }

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
