
package org.mulesoft.schema.mule.jms;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.mulesoft.schema.mule.core.AbstractGlobalInterceptingMessageProcessorType;
import org.mulesoft.schema.mule.core.AbstractInboundEndpointType;
import org.mulesoft.schema.mule.core.AbstractInterceptingMessageProcessorType;
import org.mulesoft.schema.mule.core.AbstractMixedContentMessageProcessorType;
import org.mulesoft.schema.mule.core.AbstractObserverMessageProcessorType;
import org.mulesoft.schema.mule.core.AbstractRedeliveryPolicyType;
import org.mulesoft.schema.mule.core.AbstractSecurityFilterType;
import org.mulesoft.schema.mule.core.AbstractTransactionType;
import org.mulesoft.schema.mule.core.AbstractTransformerType;
import org.mulesoft.schema.mule.core.AppendStringTransformerType;
import org.mulesoft.schema.mule.core.BaseAggregatorType;
import org.mulesoft.schema.mule.core.BaseTransactionType;
import org.mulesoft.schema.mule.core.BeanBuilderTransformer;
import org.mulesoft.schema.mule.core.CollectionFilterType;
import org.mulesoft.schema.mule.core.CollectionSplitter;
import org.mulesoft.schema.mule.core.CombineCollectionsTransformer;
import org.mulesoft.schema.mule.core.CommonFilterType;
import org.mulesoft.schema.mule.core.CommonTransformerType;
import org.mulesoft.schema.mule.core.CopyAttachmentType;
import org.mulesoft.schema.mule.core.CopyPropertiesType;
import org.mulesoft.schema.mule.core.CustomAggregator;
import org.mulesoft.schema.mule.core.CustomFilterType;
import org.mulesoft.schema.mule.core.CustomMessageProcessorType;
import org.mulesoft.schema.mule.core.CustomSecurityFilterType;
import org.mulesoft.schema.mule.core.CustomSplitter;
import org.mulesoft.schema.mule.core.CustomTransactionType;
import org.mulesoft.schema.mule.core.CustomTransformerType;
import org.mulesoft.schema.mule.core.EncryptionSecurityFilterType;
import org.mulesoft.schema.mule.core.EncryptionTransformerType;
import org.mulesoft.schema.mule.core.ExpressionComponent;
import org.mulesoft.schema.mule.core.ExpressionFilterType;
import org.mulesoft.schema.mule.core.ExpressionTransformerType;
import org.mulesoft.schema.mule.core.ForeachProcessorType;
import org.mulesoft.schema.mule.core.IdempotentMessageFilterType;
import org.mulesoft.schema.mule.core.IdempotentRedeliveryPolicyType;
import org.mulesoft.schema.mule.core.IdempotentSecureHashMessageFilter;
import org.mulesoft.schema.mule.core.KeyValueType;
import org.mulesoft.schema.mule.core.LoggerType;
import org.mulesoft.schema.mule.core.MapSplitter;
import org.mulesoft.schema.mule.core.MapType;
import org.mulesoft.schema.mule.core.MessageChunkSplitter;
import org.mulesoft.schema.mule.core.MessageFilterType;
import org.mulesoft.schema.mule.core.MessagePropertiesTransformerType;
import org.mulesoft.schema.mule.core.ParseTemplateTransformerType;
import org.mulesoft.schema.mule.core.RefFilterType;
import org.mulesoft.schema.mule.core.RefMessageProcessorType;
import org.mulesoft.schema.mule.core.RefTransformerType;
import org.mulesoft.schema.mule.core.RegexFilterType;
import org.mulesoft.schema.mule.core.RemoveAttachmentType;
import org.mulesoft.schema.mule.core.RemovePropertyType;
import org.mulesoft.schema.mule.core.RemoveVariableType;
import org.mulesoft.schema.mule.core.Response;
import org.mulesoft.schema.mule.core.ScopedPropertyFilterType;
import org.mulesoft.schema.mule.core.SetAttachmentType;
import org.mulesoft.schema.mule.core.SetPropertyType;
import org.mulesoft.schema.mule.core.SetVariableType;
import org.mulesoft.schema.mule.core.Splitter;
import org.mulesoft.schema.mule.core.TypeFilterType;
import org.mulesoft.schema.mule.core.UnitaryFilterType;
import org.mulesoft.schema.mule.core.UsernamePasswordFilterType;
import org.mulesoft.schema.mule.core.ValueExtractorTransformerType;
import org.mulesoft.schema.mule.core.WildcardFilterType;
import org.mulesoft.schema.mule.core.WireTap;
import org.mulesoft.schema.mule.core.XaTransactionType;
import org.mulesoft.schema.mule.http.BasicSecurityFilterType;


/**
 * <p>Java class for inboundEndpointType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="inboundEndpointType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}abstractInboundEndpointType"&gt;
 *       &lt;group ref="{http://www.mulesoft.org/schema/mule/jms}defaultEndpointElements"/&gt;
 *       &lt;attGroup ref="{http://www.mulesoft.org/schema/mule/core}defaultEndpointAttributes"/&gt;
 *       &lt;attGroup ref="{http://www.mulesoft.org/schema/mule/jms}addressAttributes"/&gt;
 *       &lt;attGroup ref="{http://www.mulesoft.org/schema/mule/core}allExchangePatterns"/&gt;
 *       &lt;attribute name="durableName" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="xaPollingTimeout" type="{http://www.mulesoft.org/schema/mule/core}substitutableInt" /&gt;
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
    "endpointMessageProcessorElements",
    "response",
    "abstractRedeliveryPolicy",
    "abstractTransaction",
    "abstractXaTransaction",
    "selector",
    "property",
    "properties"
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
        @XmlElementRef(name = "abstract-mixed-content-message-processor", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    })
    protected List<JAXBElement<?>> endpointMessageProcessorElements;
    @XmlElement(namespace = "http://www.mulesoft.org/schema/mule/core")
    protected Response response;
    @XmlElementRef(name = "abstract-redelivery-policy", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    protected JAXBElement<? extends AbstractRedeliveryPolicyType> abstractRedeliveryPolicy;
    @XmlElementRef(name = "abstract-transaction", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    protected JAXBElement<? extends AbstractTransactionType> abstractTransaction;
    @XmlElement(name = "abstract-xa-transaction", namespace = "http://www.mulesoft.org/schema/mule/core")
    protected AbstractTransactionType abstractXaTransaction;
    protected JmsSelectorFilter selector;
    @XmlElement(namespace = "http://www.mulesoft.org/schema/mule/core")
    protected List<KeyValueType> property;
    @XmlElement(namespace = "http://www.mulesoft.org/schema/mule/core")
    protected MapType properties;
    @XmlAttribute(name = "durableName")
    protected String durableName;
    @XmlAttribute(name = "xaPollingTimeout")
    protected String xaPollingTimeout;
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
    @XmlAttribute(name = "topic")
    protected String topic;
    @XmlAttribute(name = "disableTemporaryReplyToDestinations")
    protected Boolean disableTemporaryReplyToDestinations;
    @XmlAttribute(name = "exchange-pattern")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String exchangePattern;

    /**
     * Gets the value of the endpointMessageProcessorElements property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the endpointMessageProcessorElements property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEndpointMessageProcessorElements().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AppendStringTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link SetVariableType }{@code >}
     * {@link JAXBElement }{@code <}{@link RemoveVariableType }{@code >}
     * {@link JAXBElement }{@code <}{@link BeanBuilderTransformer }{@code >}
     * {@link JAXBElement }{@code <}{@link SetAttachmentType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link RemovePropertyType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link ValueExtractorTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link MessagePropertiesTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link ParseTemplateTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link RemoveAttachmentType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link RefTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link CopyPropertiesType }{@code >}
     * {@link JAXBElement }{@code <}{@link SetVariableType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link EncryptionTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link RemoveVariableType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link ExpressionTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link SetPropertyType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link CustomTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link EncryptionTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link CopyAttachmentType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link CommonTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link RefFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link RegexFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link CollectionFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link TypeFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link TypeFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link CollectionFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link ExpressionFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link WildcardFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link PropertyFilter }{@code >}
     * {@link JAXBElement }{@code <}{@link ScopedPropertyFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link CustomFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link WildcardFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link UnitaryFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link CommonFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link EncryptionSecurityFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link CustomSecurityFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link UsernamePasswordFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link BasicSecurityFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractSecurityFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link BaseAggregatorType }{@code >}
     * {@link JAXBElement }{@code <}{@link ForeachProcessorType }{@code >}
     * {@link JAXBElement }{@code <}{@link MessageChunkSplitter }{@code >}
     * {@link JAXBElement }{@code <}{@link CollectionSplitter }{@code >}
     * {@link JAXBElement }{@code <}{@link WireTap }{@code >}
     * {@link JAXBElement }{@code <}{@link CustomAggregator }{@code >}
     * {@link JAXBElement }{@code <}{@link CustomSplitter }{@code >}
     * {@link JAXBElement }{@code <}{@link BaseAggregatorType }{@code >}
     * {@link JAXBElement }{@code <}{@link BaseAggregatorType }{@code >}
     * {@link JAXBElement }{@code <}{@link MapSplitter }{@code >}
     * {@link JAXBElement }{@code <}{@link CombineCollectionsTransformer }{@code >}
     * {@link JAXBElement }{@code <}{@link IdempotentSecureHashMessageFilter }{@code >}
     * {@link JAXBElement }{@code <}{@link IdempotentMessageFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link MessageFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractGlobalInterceptingMessageProcessorType }{@code >}
     * {@link JAXBElement }{@code <}{@link Splitter }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractInterceptingMessageProcessorType }{@code >}
     * {@link JAXBElement }{@code <}{@link LoggerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractObserverMessageProcessorType }{@code >}
     * {@link JAXBElement }{@code <}{@link RefMessageProcessorType }{@code >}
     * {@link JAXBElement }{@code <}{@link CustomMessageProcessorType }{@code >}
     * {@link JAXBElement }{@code <}{@link ExpressionComponent }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractMixedContentMessageProcessorType }{@code >}
     * 
     * 
     */
    public List<JAXBElement<?>> getEndpointMessageProcessorElements() {
        if (endpointMessageProcessorElements == null) {
            endpointMessageProcessorElements = new ArrayList<JAXBElement<?>>();
        }
        return this.endpointMessageProcessorElements;
    }

    /**
     * Gets the value of the response property.
     * 
     * @return
     *     possible object is
     *     {@link Response }
     *     
     */
    public Response getResponse() {
        return response;
    }

    /**
     * Sets the value of the response property.
     * 
     * @param value
     *     allowed object is
     *     {@link Response }
     *     
     */
    public void setResponse(Response value) {
        this.response = value;
    }

    /**
     * Gets the value of the abstractRedeliveryPolicy property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link IdempotentRedeliveryPolicyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractRedeliveryPolicyType }{@code >}
     *     
     */
    public JAXBElement<? extends AbstractRedeliveryPolicyType> getAbstractRedeliveryPolicy() {
        return abstractRedeliveryPolicy;
    }

    /**
     * Sets the value of the abstractRedeliveryPolicy property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link IdempotentRedeliveryPolicyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractRedeliveryPolicyType }{@code >}
     *     
     */
    public void setAbstractRedeliveryPolicy(JAXBElement<? extends AbstractRedeliveryPolicyType> value) {
        this.abstractRedeliveryPolicy = value;
    }

    /**
     * Gets the value of the abstractTransaction property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link BaseTransactionType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomTransactionType }{@code >}
     *     {@link JAXBElement }{@code <}{@link BaseTransactionType }{@code >}
     *     {@link JAXBElement }{@code <}{@link BaseTransactionType }{@code >}
     *     {@link JAXBElement }{@code <}{@link XaTransactionType }{@code >}
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
     *     {@link JAXBElement }{@code <}{@link BaseTransactionType }{@code >}
     *     {@link JAXBElement }{@code <}{@link XaTransactionType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransactionType }{@code >}
     *     
     */
    public void setAbstractTransaction(JAXBElement<? extends AbstractTransactionType> value) {
        this.abstractTransaction = value;
    }

    /**
     * Gets the value of the abstractXaTransaction property.
     * 
     * @return
     *     possible object is
     *     {@link AbstractTransactionType }
     *     
     */
    public AbstractTransactionType getAbstractXaTransaction() {
        return abstractXaTransaction;
    }

    /**
     * Sets the value of the abstractXaTransaction property.
     * 
     * @param value
     *     allowed object is
     *     {@link AbstractTransactionType }
     *     
     */
    public void setAbstractXaTransaction(AbstractTransactionType value) {
        this.abstractXaTransaction = value;
    }

    /**
     * Gets the value of the selector property.
     * 
     * @return
     *     possible object is
     *     {@link JmsSelectorFilter }
     *     
     */
    public JmsSelectorFilter getSelector() {
        return selector;
    }

    /**
     * Sets the value of the selector property.
     * 
     * @param value
     *     allowed object is
     *     {@link JmsSelectorFilter }
     *     
     */
    public void setSelector(JmsSelectorFilter value) {
        this.selector = value;
    }

    /**
     * Gets the value of the property property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the property property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProperty().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link KeyValueType }
     * 
     * 
     */
    public List<KeyValueType> getProperty() {
        if (property == null) {
            property = new ArrayList<KeyValueType>();
        }
        return this.property;
    }

    /**
     * Gets the value of the properties property.
     * 
     * @return
     *     possible object is
     *     {@link MapType }
     *     
     */
    public MapType getProperties() {
        return properties;
    }

    /**
     * Sets the value of the properties property.
     * 
     * @param value
     *     allowed object is
     *     {@link MapType }
     *     
     */
    public void setProperties(MapType value) {
        this.properties = value;
    }

    /**
     * Gets the value of the durableName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDurableName() {
        return durableName;
    }

    /**
     * Sets the value of the durableName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDurableName(String value) {
        this.durableName = value;
    }

    /**
     * Gets the value of the xaPollingTimeout property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXaPollingTimeout() {
        return xaPollingTimeout;
    }

    /**
     * Sets the value of the xaPollingTimeout property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXaPollingTimeout(String value) {
        this.xaPollingTimeout = value;
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

    /**
     * Gets the value of the topic property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTopic() {
        return topic;
    }

    /**
     * Sets the value of the topic property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTopic(String value) {
        this.topic = value;
    }

    /**
     * Gets the value of the disableTemporaryReplyToDestinations property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDisableTemporaryReplyToDestinations() {
        return disableTemporaryReplyToDestinations;
    }

    /**
     * Sets the value of the disableTemporaryReplyToDestinations property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDisableTemporaryReplyToDestinations(Boolean value) {
        this.disableTemporaryReplyToDestinations = value;
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
