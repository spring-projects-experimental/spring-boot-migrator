
package org.mulesoft.schema.mule.core;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for serviceOverridesType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="serviceOverridesType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;attribute name="messageReceiver" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="transactedMessageReceiver" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="xaTransactedMessageReceiver" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="dispatcherFactory" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="inboundTransformer" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="outboundTransformer" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="responseTransformer" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="endpointBuilder" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="messageFactory" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="serviceFinder" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="sessionHandler" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="inboundExchangePatterns" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="outboundExchangePatterns" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="defaultExchangePattern" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "serviceOverridesType")
public class ServiceOverridesType {

    @XmlAttribute(name = "messageReceiver")
    protected String messageReceiver;
    @XmlAttribute(name = "transactedMessageReceiver")
    protected String transactedMessageReceiver;
    @XmlAttribute(name = "xaTransactedMessageReceiver")
    protected String xaTransactedMessageReceiver;
    @XmlAttribute(name = "dispatcherFactory")
    protected String dispatcherFactory;
    @XmlAttribute(name = "inboundTransformer")
    protected String inboundTransformer;
    @XmlAttribute(name = "outboundTransformer")
    protected String outboundTransformer;
    @XmlAttribute(name = "responseTransformer")
    protected String responseTransformer;
    @XmlAttribute(name = "endpointBuilder")
    protected String endpointBuilder;
    @XmlAttribute(name = "messageFactory")
    protected String messageFactory;
    @XmlAttribute(name = "serviceFinder")
    protected String serviceFinder;
    @XmlAttribute(name = "sessionHandler")
    protected String sessionHandler;
    @XmlAttribute(name = "inboundExchangePatterns")
    protected String inboundExchangePatterns;
    @XmlAttribute(name = "outboundExchangePatterns")
    protected String outboundExchangePatterns;
    @XmlAttribute(name = "defaultExchangePattern")
    protected String defaultExchangePattern;

    /**
     * Gets the value of the messageReceiver property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessageReceiver() {
        return messageReceiver;
    }

    /**
     * Sets the value of the messageReceiver property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessageReceiver(String value) {
        this.messageReceiver = value;
    }

    /**
     * Gets the value of the transactedMessageReceiver property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransactedMessageReceiver() {
        return transactedMessageReceiver;
    }

    /**
     * Sets the value of the transactedMessageReceiver property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransactedMessageReceiver(String value) {
        this.transactedMessageReceiver = value;
    }

    /**
     * Gets the value of the xaTransactedMessageReceiver property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXaTransactedMessageReceiver() {
        return xaTransactedMessageReceiver;
    }

    /**
     * Sets the value of the xaTransactedMessageReceiver property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXaTransactedMessageReceiver(String value) {
        this.xaTransactedMessageReceiver = value;
    }

    /**
     * Gets the value of the dispatcherFactory property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDispatcherFactory() {
        return dispatcherFactory;
    }

    /**
     * Sets the value of the dispatcherFactory property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDispatcherFactory(String value) {
        this.dispatcherFactory = value;
    }

    /**
     * Gets the value of the inboundTransformer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInboundTransformer() {
        return inboundTransformer;
    }

    /**
     * Sets the value of the inboundTransformer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInboundTransformer(String value) {
        this.inboundTransformer = value;
    }

    /**
     * Gets the value of the outboundTransformer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOutboundTransformer() {
        return outboundTransformer;
    }

    /**
     * Sets the value of the outboundTransformer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOutboundTransformer(String value) {
        this.outboundTransformer = value;
    }

    /**
     * Gets the value of the responseTransformer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResponseTransformer() {
        return responseTransformer;
    }

    /**
     * Sets the value of the responseTransformer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResponseTransformer(String value) {
        this.responseTransformer = value;
    }

    /**
     * Gets the value of the endpointBuilder property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEndpointBuilder() {
        return endpointBuilder;
    }

    /**
     * Sets the value of the endpointBuilder property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEndpointBuilder(String value) {
        this.endpointBuilder = value;
    }

    /**
     * Gets the value of the messageFactory property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessageFactory() {
        return messageFactory;
    }

    /**
     * Sets the value of the messageFactory property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessageFactory(String value) {
        this.messageFactory = value;
    }

    /**
     * Gets the value of the serviceFinder property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceFinder() {
        return serviceFinder;
    }

    /**
     * Sets the value of the serviceFinder property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceFinder(String value) {
        this.serviceFinder = value;
    }

    /**
     * Gets the value of the sessionHandler property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSessionHandler() {
        return sessionHandler;
    }

    /**
     * Sets the value of the sessionHandler property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSessionHandler(String value) {
        this.sessionHandler = value;
    }

    /**
     * Gets the value of the inboundExchangePatterns property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInboundExchangePatterns() {
        return inboundExchangePatterns;
    }

    /**
     * Sets the value of the inboundExchangePatterns property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInboundExchangePatterns(String value) {
        this.inboundExchangePatterns = value;
    }

    /**
     * Gets the value of the outboundExchangePatterns property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOutboundExchangePatterns() {
        return outboundExchangePatterns;
    }

    /**
     * Sets the value of the outboundExchangePatterns property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOutboundExchangePatterns(String value) {
        this.outboundExchangePatterns = value;
    }

    /**
     * Gets the value of the defaultExchangePattern property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDefaultExchangePattern() {
        return defaultExchangePattern;
    }

    /**
     * Sets the value of the defaultExchangePattern property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDefaultExchangePattern(String value) {
        this.defaultExchangePattern = value;
    }

}
