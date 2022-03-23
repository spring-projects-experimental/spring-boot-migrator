
package org.mulesoft.schema.mule.ee.wmq;

import org.mulesoft.schema.mule.core.*;
import org.mulesoft.schema.mule.http.BasicSecurityFilterType;
import org.mulesoft.schema.mule.jms.PropertyFilter;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for inboundEndpointType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="inboundEndpointType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}abstractInboundEndpointType"&gt;
 *       &lt;group ref="{http://www.mulesoft.org/schema/mule/ee/wmq}defaultEndpointElements"/&gt;
 *       &lt;attGroup ref="{http://www.mulesoft.org/schema/mule/core}allExchangePatterns"/&gt;
 *       &lt;attGroup ref="{http://www.mulesoft.org/schema/mule/core}defaultEndpointAttributes"/&gt;
 *       &lt;attGroup ref="{http://www.mulesoft.org/schema/mule/ee/wmq}commonAddressAttributes"/&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "inboundEndpointType", propOrder = {
    "abstractTransformerOrAbstractFilterOrAbstractSecurityFilter"
})
public class InboundEndpointType
    extends AbstractInboundEndpointType
{

    @XmlElementRefs({
        @XmlElementRef(name = "abstract-transformer", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "abstract-filter", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "abstract-security-filter", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "abstract-intercepting-message-processor", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "abstract-observer-message-processor", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "processor", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "custom-processor", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "abstract-mixed-content-message-processor", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "response", namespace = "http://www.mulesoft.org/schema/mule/core", type = Response.class, required = false),
        @XmlElementRef(name = "abstract-redelivery-policy", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "abstract-transaction", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "abstract-xa-transaction", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "selector", namespace = "http://www.mulesoft.org/schema/mule/ee/wmq", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "property", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "properties", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    })
    protected List<Object> abstractTransformerOrAbstractFilterOrAbstractSecurityFilter;
    @XmlAttribute(name = "exchange-pattern")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String exchangePattern;
    @XmlAttribute(name = "ref")
    protected String ref;
    @XmlAttribute(name = "address")
    protected String address;
    @XmlAttribute(name = "responseTimeout")
    protected String responseTimeout;
    @XmlAttribute(name = "encoding")
    protected String encoding;
    @XmlAttribute(name = "connector-ref")
    protected String connectorRef;
    @XmlAttribute(name = "transformer-refs")
    @XmlSchemaType(name = "NMTOKENS")
    protected List<String> transformerRefs;
    @XmlAttribute(name = "responseTransformer-refs")
    @XmlSchemaType(name = "NMTOKENS")
    protected List<String> responseTransformerRefs;
    @XmlAttribute(name = "disableTransportTransformer")
    protected String disableTransportTransformer;
    @XmlAttribute(name = "mimeType")
    protected String mimeType;
    @XmlAttribute(name = "queue")
    protected String queue;

    /**
     * Gets the value of the abstractTransformerOrAbstractFilterOrAbstractSecurityFilter property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the abstractTransformerOrAbstractFilterOrAbstractSecurityFilter property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAbstractTransformerOrAbstractFilterOrAbstractSecurityFilter().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link RefTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link SetPropertyType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link CopyPropertiesType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link ValueExtractorTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link ParseTemplateTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link EncryptionTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link CopyAttachmentType }{@code >}
     * {@link JAXBElement }{@code <}{@link EncryptionTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link SetAttachmentType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link CustomTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link MessagePropertiesTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link SetVariableType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link RemoveAttachmentType }{@code >}
     * {@link JAXBElement }{@code <}{@link AppendStringTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link ExpressionTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link RemoveVariableType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link RemoveVariableType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link BeanBuilderTransformer }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link RemovePropertyType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link SetVariableType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link CommonTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link CollectionFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link PropertyFilter }{@code >}
     * {@link JAXBElement }{@code <}{@link ExpressionFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link CollectionFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link UnitaryFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link RefFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link WildcardFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link RegexFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link ScopedPropertyFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link TypeFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link WildcardFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link CustomFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link TypeFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link CommonFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link CustomSecurityFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link BasicSecurityFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link EncryptionSecurityFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link UsernamePasswordFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractSecurityFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link CustomSplitter }{@code >}
     * {@link JAXBElement }{@code <}{@link MessageChunkSplitter }{@code >}
     * {@link JAXBElement }{@code <}{@link BaseAggregatorType }{@code >}
     * {@link JAXBElement }{@code <}{@link BaseAggregatorType }{@code >}
     * {@link JAXBElement }{@code <}{@link MapSplitter }{@code >}
     * {@link JAXBElement }{@code <}{@link WireTap }{@code >}
     * {@link JAXBElement }{@code <}{@link Splitter }{@code >}
     * {@link JAXBElement }{@code <}{@link CustomAggregator }{@code >}
     * {@link JAXBElement }{@code <}{@link BaseAggregatorType }{@code >}
     * {@link JAXBElement }{@code <}{@link ForeachProcessorType }{@code >}
     * {@link JAXBElement }{@code <}{@link CollectionSplitter }{@code >}
     * {@link JAXBElement }{@code <}{@link IdempotentMessageFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link MessageFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link CombineCollectionsTransformer }{@code >}
     * {@link JAXBElement }{@code <}{@link IdempotentSecureHashMessageFilter }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractGlobalInterceptingMessageProcessorType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractInterceptingMessageProcessorType }{@code >}
     * {@link JAXBElement }{@code <}{@link LoggerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractObserverMessageProcessorType }{@code >}
     * {@link JAXBElement }{@code <}{@link RefMessageProcessorType }{@code >}
     * {@link JAXBElement }{@code <}{@link CustomMessageProcessorType }{@code >}
     * {@link JAXBElement }{@code <}{@link ExpressionComponent }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractMixedContentMessageProcessorType }{@code >}
     * {@link Response }
     * {@link JAXBElement }{@code <}{@link IdempotentRedeliveryPolicyType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractRedeliveryPolicyType }{@code >}
     * {@link JAXBElement }{@code <}{@link CustomTransactionType }{@code >}
     * {@link JAXBElement }{@code <}{@link BaseTransactionType }{@code >}
     * {@link JAXBElement }{@code <}{@link XaTransactionType }{@code >}
     * {@link JAXBElement }{@code <}{@link BaseTransactionType }{@code >}
     * {@link JAXBElement }{@code <}{@link BaseTransactionType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransactionType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransactionType }{@code >}
     * {@link JAXBElement }{@code <}{@link SelectorFilter }{@code >}
     * {@link JAXBElement }{@code <}{@link KeyValueType }{@code >}
     * {@link JAXBElement }{@code <}{@link MapType }{@code >}
     * 
     * 
     */
    public List<Object> getAbstractTransformerOrAbstractFilterOrAbstractSecurityFilter() {
        if (abstractTransformerOrAbstractFilterOrAbstractSecurityFilter == null) {
            abstractTransformerOrAbstractFilterOrAbstractSecurityFilter = new ArrayList<Object>();
        }
        return this.abstractTransformerOrAbstractFilterOrAbstractSecurityFilter;
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

    /**
     * Gets the value of the ref property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRef() {
        return ref;
    }

    /**
     * Sets the value of the ref property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRef(String value) {
        this.ref = value;
    }

    /**
     * Gets the value of the address property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the value of the address property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddress(String value) {
        this.address = value;
    }

    /**
     * Gets the value of the responseTimeout property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResponseTimeout() {
        return responseTimeout;
    }

    /**
     * Sets the value of the responseTimeout property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResponseTimeout(String value) {
        this.responseTimeout = value;
    }

    /**
     * Gets the value of the encoding property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * Sets the value of the encoding property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEncoding(String value) {
        this.encoding = value;
    }

    /**
     * Gets the value of the connectorRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConnectorRef() {
        return connectorRef;
    }

    /**
     * Sets the value of the connectorRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConnectorRef(String value) {
        this.connectorRef = value;
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
     * Gets the value of the disableTransportTransformer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDisableTransportTransformer() {
        return disableTransportTransformer;
    }

    /**
     * Sets the value of the disableTransportTransformer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDisableTransportTransformer(String value) {
        this.disableTransportTransformer = value;
    }

    /**
     * Gets the value of the mimeType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMimeType() {
        return mimeType;
    }

    /**
     * Sets the value of the mimeType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMimeType(String value) {
        this.mimeType = value;
    }

    /**
     * Gets the value of the queue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQueue() {
        return queue;
    }

    /**
     * Sets the value of the queue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQueue(String value) {
        this.queue = value;
    }

}
