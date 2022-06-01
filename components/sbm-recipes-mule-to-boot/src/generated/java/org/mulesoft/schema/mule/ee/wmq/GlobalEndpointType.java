
package org.mulesoft.schema.mule.ee.wmq;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.mulesoft.schema.mule.core.AbstractGlobalEndpointType;
import org.mulesoft.schema.mule.core.AbstractGlobalInterceptingMessageProcessorType;
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
import org.mulesoft.schema.mule.db.BulkUpdateMessageProcessorType;
import org.mulesoft.schema.mule.http.BasicSecurityFilterType;
import org.mulesoft.schema.mule.jms.PropertyFilter;
import org.mulesoft.schema.mule.scripting.ScriptFilterType;
import org.mulesoft.schema.mule.scripting.ScriptTransformerType;


/**
 * <p>Java class for globalEndpointType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="globalEndpointType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}abstractGlobalEndpointType"&gt;
 *       &lt;group ref="{http://www.mulesoft.org/schema/mule/ee/wmq}defaultEndpointElements"/&gt;
 *       &lt;attGroup ref="{http://www.mulesoft.org/schema/mule/ee/wmq}commonAddressAttributes"/&gt;
 *       &lt;attGroup ref="{http://www.mulesoft.org/schema/mule/ee/wmq}outboundAddressAttributes"/&gt;
 *       &lt;attGroup ref="{http://www.mulesoft.org/schema/mule/core}defaultEndpointAttributes"/&gt;
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
@XmlType(name = "globalEndpointType", propOrder = {
    "abstractTransformerOrAbstractFilterOrAbstractSecurityFilter"
})
public class GlobalEndpointType
    extends AbstractGlobalEndpointType
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
    @XmlAttribute(name = "queue")
    protected String queue;
    @XmlAttribute(name = "disableTemporaryReplyToDestinations")
    protected Boolean disableTemporaryReplyToDestinations;
    @XmlAttribute(name = "correlationId")
    protected String correlationId;
    @XmlAttribute(name = "messageType")
    protected String messageType;
    @XmlAttribute(name = "characterSet")
    protected String characterSet;
    @XmlAttribute(name = "persistentDelivery")
    protected String persistentDelivery;
    @XmlAttribute(name = "timeToLive")
    protected String timeToLive;
    @XmlAttribute(name = "priority")
    protected String priority;
    @XmlAttribute(name = "targetClient")
    protected String targetClient;
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
    @XmlAttribute(name = "exchange-pattern")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String exchangePattern;

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
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link ParseTemplateTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link EncryptionTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link RemoveVariableType }{@code >}
     * {@link JAXBElement }{@code <}{@link SetPropertyType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link RefTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link BeanBuilderTransformer }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AppendStringTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link RemoveVariableType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link ExpressionTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link RemoveAttachmentType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link CopyPropertiesType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link SetVariableType }{@code >}
     * {@link JAXBElement }{@code <}{@link CustomTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link MessagePropertiesTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link ValueExtractorTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link ScriptTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link SetVariableType }{@code >}
     * {@link JAXBElement }{@code <}{@link EncryptionTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link CopyAttachmentType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link RemovePropertyType }{@code >}
     * {@link JAXBElement }{@code <}{@link SetAttachmentType }{@code >}
     * {@link JAXBElement }{@code <}{@link CommonTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link TypeFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link CollectionFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link ExpressionFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link ScopedPropertyFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link CustomFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link UnitaryFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link CollectionFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link WildcardFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link RefFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link PropertyFilter }{@code >}
     * {@link JAXBElement }{@code <}{@link TypeFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link RegexFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link WildcardFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link ScriptFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link CommonFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link BasicSecurityFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link CustomSecurityFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link UsernamePasswordFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link EncryptionSecurityFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractSecurityFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link IdempotentSecureHashMessageFilter }{@code >}
     * {@link JAXBElement }{@code <}{@link CombineCollectionsTransformer }{@code >}
     * {@link JAXBElement }{@code <}{@link MessageFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link IdempotentMessageFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractGlobalInterceptingMessageProcessorType }{@code >}
     * {@link JAXBElement }{@code <}{@link MapSplitter }{@code >}
     * {@link JAXBElement }{@code <}{@link ForeachProcessorType }{@code >}
     * {@link JAXBElement }{@code <}{@link CustomSplitter }{@code >}
     * {@link JAXBElement }{@code <}{@link BaseAggregatorType }{@code >}
     * {@link JAXBElement }{@code <}{@link BaseAggregatorType }{@code >}
     * {@link JAXBElement }{@code <}{@link CustomAggregator }{@code >}
     * {@link JAXBElement }{@code <}{@link MessageChunkSplitter }{@code >}
     * {@link JAXBElement }{@code <}{@link WireTap }{@code >}
     * {@link JAXBElement }{@code <}{@link CollectionSplitter }{@code >}
     * {@link JAXBElement }{@code <}{@link BaseAggregatorType }{@code >}
     * {@link JAXBElement }{@code <}{@link Splitter }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractInterceptingMessageProcessorType }{@code >}
     * {@link JAXBElement }{@code <}{@link LoggerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractObserverMessageProcessorType }{@code >}
     * {@link JAXBElement }{@code <}{@link RefMessageProcessorType }{@code >}
     * {@link JAXBElement }{@code <}{@link CustomMessageProcessorType }{@code >}
     * {@link JAXBElement }{@code <}{@link BulkUpdateMessageProcessorType }{@code >}
     * {@link JAXBElement }{@code <}{@link ExpressionComponent }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractMixedContentMessageProcessorType }{@code >}
     * {@link Response }
     * {@link JAXBElement }{@code <}{@link IdempotentRedeliveryPolicyType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractRedeliveryPolicyType }{@code >}
     * {@link JAXBElement }{@code <}{@link BaseTransactionType }{@code >}
     * {@link JAXBElement }{@code <}{@link CustomTransactionType }{@code >}
     * {@link JAXBElement }{@code <}{@link BaseTransactionType }{@code >}
     * {@link JAXBElement }{@code <}{@link XaTransactionType }{@code >}
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
     * Gets the value of the correlationId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCorrelationId() {
        return correlationId;
    }

    /**
     * Sets the value of the correlationId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCorrelationId(String value) {
        this.correlationId = value;
    }

    /**
     * Gets the value of the messageType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessageType() {
        return messageType;
    }

    /**
     * Sets the value of the messageType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessageType(String value) {
        this.messageType = value;
    }

    /**
     * Gets the value of the characterSet property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCharacterSet() {
        return characterSet;
    }

    /**
     * Sets the value of the characterSet property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCharacterSet(String value) {
        this.characterSet = value;
    }

    /**
     * Gets the value of the persistentDelivery property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPersistentDelivery() {
        return persistentDelivery;
    }

    /**
     * Sets the value of the persistentDelivery property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPersistentDelivery(String value) {
        this.persistentDelivery = value;
    }

    /**
     * Gets the value of the timeToLive property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTimeToLive() {
        return timeToLive;
    }

    /**
     * Sets the value of the timeToLive property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTimeToLive(String value) {
        this.timeToLive = value;
    }

    /**
     * Gets the value of the priority property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPriority() {
        return priority;
    }

    /**
     * Sets the value of the priority property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPriority(String value) {
        this.priority = value;
    }

    /**
     * Gets the value of the targetClient property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTargetClient() {
        return targetClient;
    }

    /**
     * Sets the value of the targetClient property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTargetClient(String value) {
        this.targetClient = value;
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
