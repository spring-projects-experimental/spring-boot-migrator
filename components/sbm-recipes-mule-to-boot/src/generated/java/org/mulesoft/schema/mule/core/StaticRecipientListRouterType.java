
package org.mulesoft.schema.mule.core;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.mulesoft.schema.mule.jms.PropertyFilter;


/**
 * <p>Java class for staticRecipientListRouterType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="staticRecipientListRouterType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}abstractOutboundRouterType"&gt;
 *       &lt;sequence&gt;
 *         &lt;group ref="{http://www.mulesoft.org/schema/mule/core}defaultFilteredOutboundRouterElements"/&gt;
 *         &lt;element name="recipients" type="{http://www.mulesoft.org/schema/mule/core}listOrSetType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attGroup ref="{http://www.mulesoft.org/schema/mule/core}defaultFilteredOutboundRouterAttributes"/&gt;
 *       &lt;attribute name="recipientsProperty" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="recipientsDelimiter" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="synchronous" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "staticRecipientListRouterType", propOrder = {
    "replyTo",
    "abstractTransaction",
    "abstractFilter",
    "abstractTransformer",
    "recipients"
})
public class StaticRecipientListRouterType
    extends AbstractOutboundRouterType
{

    @XmlElement(name = "reply-to")
    protected EndpointRefType replyTo;
    @XmlElementRef(name = "abstract-transaction", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    protected JAXBElement<? extends AbstractTransactionType> abstractTransaction;
    @XmlElementRef(name = "abstract-filter", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    protected JAXBElement<? extends CommonFilterType> abstractFilter;
    @XmlElementRef(name = "abstract-transformer", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    protected List<JAXBElement<? extends CommonTransformerType>> abstractTransformer;
    protected ListOrSetType recipients;
    @XmlAttribute(name = "recipientsProperty")
    protected String recipientsProperty;
    @XmlAttribute(name = "recipientsDelimiter")
    protected String recipientsDelimiter;
    @XmlAttribute(name = "synchronous")
    protected Boolean synchronous;
    @XmlAttribute(name = "useTemplates")
    protected String useTemplates;
    @XmlAttribute(name = "transformer-refs")
    @XmlSchemaType(name = "NMTOKENS")
    protected List<String> transformerRefs;
    @XmlAttribute(name = "enableCorrelation")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String enableCorrelation;

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
     *     {@link JAXBElement }{@code <}{@link XaTransactionType }{@code >}
     *     {@link JAXBElement }{@code <}{@link BaseTransactionType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomTransactionType }{@code >}
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
     *     {@link JAXBElement }{@code <}{@link XaTransactionType }{@code >}
     *     {@link JAXBElement }{@code <}{@link BaseTransactionType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomTransactionType }{@code >}
     *     {@link JAXBElement }{@code <}{@link BaseTransactionType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractTransactionType }{@code >}
     *     
     */
    public void setAbstractTransaction(JAXBElement<? extends AbstractTransactionType> value) {
        this.abstractTransaction = value;
    }

    /**
     * 
     *                         Filters the messages to be processed by this router.
     *                         @Deprecated since 2.2.  Configure the filter on the endpoint instead of the router.
     *                     
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link RefFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ExpressionFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link PropertyFilter }{@code >}
     *     {@link JAXBElement }{@code <}{@link WildcardFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link UnitaryFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link TypeFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ScopedPropertyFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CollectionFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CollectionFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link TypeFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link WildcardFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link RegexFilterType }{@code >}
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
     *     {@link JAXBElement }{@code <}{@link RefFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ExpressionFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link PropertyFilter }{@code >}
     *     {@link JAXBElement }{@code <}{@link WildcardFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link UnitaryFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link TypeFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ScopedPropertyFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CollectionFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CollectionFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link TypeFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link WildcardFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link RegexFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CommonFilterType }{@code >}
     *     
     */
    public void setAbstractFilter(JAXBElement<? extends CommonFilterType> value) {
        this.abstractFilter = value;
    }

    /**
     * 
     *                         Filters are applied before message transformations. A transformer can be configured here to transform messages before they are filtered.
     *                     Gets the value of the abstractTransformer property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the abstractTransformer property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAbstractTransformer().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link EncryptionTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link SetVariableType }{@code >}
     * {@link JAXBElement }{@code <}{@link CopyAttachmentType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link SetPropertyType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link CustomTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link RemoveVariableType }{@code >}
     * {@link JAXBElement }{@code <}{@link RemoveVariableType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link BeanBuilderTransformer }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link RefTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link RemovePropertyType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link MessagePropertiesTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link ExpressionTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link RemoveAttachmentType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link ValueExtractorTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link CopyPropertiesType }{@code >}
     * {@link JAXBElement }{@code <}{@link ParseTemplateTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link SetAttachmentType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link SetVariableType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link EncryptionTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AppendStringTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link CommonTransformerType }{@code >}
     * 
     * 
     */
    public List<JAXBElement<? extends CommonTransformerType>> getAbstractTransformer() {
        if (abstractTransformer == null) {
            abstractTransformer = new ArrayList<JAXBElement<? extends CommonTransformerType>>();
        }
        return this.abstractTransformer;
    }

    /**
     * Gets the value of the recipients property.
     * 
     * @return
     *     possible object is
     *     {@link ListOrSetType }
     *     
     */
    public ListOrSetType getRecipients() {
        return recipients;
    }

    /**
     * Sets the value of the recipients property.
     * 
     * @param value
     *     allowed object is
     *     {@link ListOrSetType }
     *     
     */
    public void setRecipients(ListOrSetType value) {
        this.recipients = value;
    }

    /**
     * Gets the value of the recipientsProperty property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRecipientsProperty() {
        return recipientsProperty;
    }

    /**
     * Sets the value of the recipientsProperty property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRecipientsProperty(String value) {
        this.recipientsProperty = value;
    }

    /**
     * Gets the value of the recipientsDelimiter property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRecipientsDelimiter() {
        return recipientsDelimiter;
    }

    /**
     * Sets the value of the recipientsDelimiter property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRecipientsDelimiter(String value) {
        this.recipientsDelimiter = value;
    }

    /**
     * Gets the value of the synchronous property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSynchronous() {
        return synchronous;
    }

    /**
     * Sets the value of the synchronous property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSynchronous(Boolean value) {
        this.synchronous = value;
    }

    /**
     * Gets the value of the useTemplates property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUseTemplates() {
        if (useTemplates == null) {
            return "true";
        } else {
            return useTemplates;
        }
    }

    /**
     * Sets the value of the useTemplates property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUseTemplates(String value) {
        this.useTemplates = value;
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
