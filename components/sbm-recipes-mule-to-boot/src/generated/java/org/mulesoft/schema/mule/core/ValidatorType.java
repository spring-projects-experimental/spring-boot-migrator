
package org.mulesoft.schema.mule.core;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;
import org.mulesoft.schema.mule.jms.PropertyFilter;


/**
 * 
 *                 A validator with a single request-response inbound endpoint and a single outbound endpoint.
 *             
 * 
 * <p>Java class for validatorType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="validatorType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}baseFlowConstructType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.mulesoft.org/schema/mule/core}abstract-inbound-endpoint" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.mulesoft.org/schema/mule/core}abstract-filter" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.mulesoft.org/schema/mule/core}abstract-outbound-endpoint" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.mulesoft.org/schema/mule/core}legacy-abstract-exception-strategy" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="inboundAddress" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="inboundEndpoint-ref" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="validationFilter-ref" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="outboundAddress" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="outboundEndpoint-ref" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="ackExpression" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="nackExpression" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="errorExpression" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "validatorType", propOrder = {
    "abstractInboundEndpoint",
    "abstractFilter",
    "abstractOutboundEndpoint",
    "legacyAbstractExceptionStrategy"
})
public class ValidatorType
    extends BaseFlowConstructType
{

    @XmlElementRef(name = "abstract-inbound-endpoint", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    protected JAXBElement<? extends AbstractInboundEndpointType> abstractInboundEndpoint;
    @XmlElementRef(name = "abstract-filter", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    protected JAXBElement<? extends CommonFilterType> abstractFilter;
    @XmlElementRef(name = "abstract-outbound-endpoint", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    protected JAXBElement<? extends AbstractOutboundEndpointType> abstractOutboundEndpoint;
    @XmlElementRef(name = "legacy-abstract-exception-strategy", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    protected JAXBElement<? extends ExceptionStrategyType> legacyAbstractExceptionStrategy;
    @XmlAttribute(name = "inboundAddress")
    protected String inboundAddress;
    @XmlAttribute(name = "inboundEndpoint-ref")
    protected String inboundEndpointRef;
    @XmlAttribute(name = "validationFilter-ref")
    protected String validationFilterRef;
    @XmlAttribute(name = "outboundAddress")
    protected String outboundAddress;
    @XmlAttribute(name = "outboundEndpoint-ref")
    protected String outboundEndpointRef;
    @XmlAttribute(name = "ackExpression")
    protected String ackExpression;
    @XmlAttribute(name = "nackExpression")
    protected String nackExpression;
    @XmlAttribute(name = "errorExpression")
    protected String errorExpression;

    /**
     * 
     *                                 The endpoint on which messages are received.
     *                             
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.jms.InboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.tls.InboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.amqp.InboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.http.InboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.core.InboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.tcp.InboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.ee.wmq.InboundEndpointType }{@code >}
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
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.jms.InboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.tls.InboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.amqp.InboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.http.InboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.core.InboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.tcp.InboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.ee.wmq.InboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractInboundEndpointType }{@code >}
     *     
     */
    public void setAbstractInboundEndpoint(JAXBElement<? extends AbstractInboundEndpointType> value) {
        this.abstractInboundEndpoint = value;
    }

    /**
     * 
     *                                 The filter to use to validate incoming message: accepted messages are considered valid.
     *                             
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link TypeFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link WildcardFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ExpressionFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CollectionFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link RefFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link RegexFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link WildcardFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link TypeFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link PropertyFilter }{@code >}
     *     {@link JAXBElement }{@code <}{@link UnitaryFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CollectionFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ScopedPropertyFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CommonFilterType }{@code >}
     *     
     */
    public JAXBElement<? extends CommonFilterType> getAbstractFilter() {
        return abstractFilter;
    }

    /**
     * Sets the value of the abstractFilter property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link TypeFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link WildcardFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ExpressionFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CollectionFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link RefFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link RegexFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link WildcardFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link TypeFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link PropertyFilter }{@code >}
     *     {@link JAXBElement }{@code <}{@link UnitaryFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CollectionFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ScopedPropertyFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CommonFilterType }{@code >}
     *     
     */
    public void setAbstractFilter(JAXBElement<? extends CommonFilterType> value) {
        this.abstractFilter = value;
    }

    /**
     * 
     *                                 The endpoint to which messages are sent.
     *                             
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.tcp.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.core.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.jms.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.tls.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.http.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.amqp.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.ee.wmq.OutboundEndpointType }{@code >}
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
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.tcp.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.core.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.jms.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.tls.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.http.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.amqp.OutboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.ee.wmq.OutboundEndpointType }{@code >}
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
     *     {@link JAXBElement }{@code <}{@link RollbackExceptionStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ReferenceExceptionStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ChoiceExceptionStrategyType }{@code >}
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
     *     {@link JAXBElement }{@code <}{@link RollbackExceptionStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ReferenceExceptionStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ChoiceExceptionStrategyType }{@code >}
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
     * Gets the value of the validationFilterRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValidationFilterRef() {
        return validationFilterRef;
    }

    /**
     * Sets the value of the validationFilterRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValidationFilterRef(String value) {
        this.validationFilterRef = value;
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
     * Gets the value of the ackExpression property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAckExpression() {
        return ackExpression;
    }

    /**
     * Sets the value of the ackExpression property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAckExpression(String value) {
        this.ackExpression = value;
    }

    /**
     * Gets the value of the nackExpression property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNackExpression() {
        return nackExpression;
    }

    /**
     * Sets the value of the nackExpression property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNackExpression(String value) {
        this.nackExpression = value;
    }

    /**
     * Gets the value of the errorExpression property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErrorExpression() {
        return errorExpression;
    }

    /**
     * Sets the value of the errorExpression property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErrorExpression(String value) {
        this.errorExpression = value;
    }

}
