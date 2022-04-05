
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
import org.springframework.schema.beans.PropertyType;


/**
 * <p>Java class for customOutboundRouterType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="customOutboundRouterType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}abstractOutboundRouterType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.mulesoft.org/schema/mule/core}abstract-outbound-endpoint" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;group ref="{http://www.mulesoft.org/schema/mule/core}defaultFilteredOutboundRouterElements"/&gt;
 *         &lt;element ref="{http://www.springframework.org/schema/beans}property" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attGroup ref="{http://www.mulesoft.org/schema/mule/core}defaultOutboundRouterAttributes"/&gt;
 *       &lt;attribute name="class" use="required" type="{http://www.mulesoft.org/schema/mule/core}substitutableClass" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "customOutboundRouterType", propOrder = {
    "abstractOutboundEndpoint",
    "replyTo",
    "abstractTransaction",
    "abstractFilter",
    "abstractTransformer",
    "property"
})
public class CustomOutboundRouterType
    extends AbstractOutboundRouterType
{

    @XmlElementRef(name = "abstract-outbound-endpoint", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    protected List<JAXBElement<? extends AbstractOutboundEndpointType>> abstractOutboundEndpoint;
    @XmlElement(name = "reply-to")
    protected EndpointRefType replyTo;
    @XmlElementRef(name = "abstract-transaction", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    protected JAXBElement<? extends AbstractTransactionType> abstractTransaction;
    @XmlElementRef(name = "abstract-filter", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    protected JAXBElement<? extends CommonFilterType> abstractFilter;
    @XmlElementRef(name = "abstract-transformer", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    protected List<JAXBElement<? extends CommonTransformerType>> abstractTransformer;
    @XmlElement(namespace = "http://www.springframework.org/schema/beans")
    protected List<PropertyType> property;
    @XmlAttribute(name = "class", required = true)
    protected String clazz;
    @XmlAttribute(name = "transformer-refs")
    @XmlSchemaType(name = "NMTOKENS")
    protected List<String> transformerRefs;
    @XmlAttribute(name = "enableCorrelation")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String enableCorrelation;

    /**
     * Gets the value of the abstractOutboundEndpoint property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the abstractOutboundEndpoint property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAbstractOutboundEndpoint().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.amqp.OutboundEndpointType }{@code >}
     * {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.tls.OutboundEndpointType }{@code >}
     * {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.core.OutboundEndpointType }{@code >}
     * {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.ee.wmq.OutboundEndpointType }{@code >}
     * {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.http.OutboundEndpointType }{@code >}
     * {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.tcp.OutboundEndpointType }{@code >}
     * {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.jms.OutboundEndpointType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractOutboundEndpointType }{@code >}
     * 
     * 
     */
    public List<JAXBElement<? extends AbstractOutboundEndpointType>> getAbstractOutboundEndpoint() {
        if (abstractOutboundEndpoint == null) {
            abstractOutboundEndpoint = new ArrayList<JAXBElement<? extends AbstractOutboundEndpointType>>();
        }
        return this.abstractOutboundEndpoint;
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
     *     {@link JAXBElement }{@code <}{@link CustomTransactionType }{@code >}
     *     {@link JAXBElement }{@code <}{@link XaTransactionType }{@code >}
     *     {@link JAXBElement }{@code <}{@link BaseTransactionType }{@code >}
     *     {@link JAXBElement }{@code <}{@link BaseTransactionType }{@code >}
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
     *     {@link JAXBElement }{@code <}{@link CustomTransactionType }{@code >}
     *     {@link JAXBElement }{@code <}{@link XaTransactionType }{@code >}
     *     {@link JAXBElement }{@code <}{@link BaseTransactionType }{@code >}
     *     {@link JAXBElement }{@code <}{@link BaseTransactionType }{@code >}
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
     *     {@link JAXBElement }{@code <}{@link TypeFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link RegexFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CollectionFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link RefFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CollectionFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ScopedPropertyFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ExpressionFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link PropertyFilter }{@code >}
     *     {@link JAXBElement }{@code <}{@link TypeFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link WildcardFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link UnitaryFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link WildcardFilterType }{@code >}
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
     *     {@link JAXBElement }{@code <}{@link RegexFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CollectionFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link RefFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CollectionFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ScopedPropertyFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ExpressionFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link PropertyFilter }{@code >}
     *     {@link JAXBElement }{@code <}{@link TypeFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link WildcardFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link UnitaryFilterType }{@code >}
     *     {@link JAXBElement }{@code <}{@link WildcardFilterType }{@code >}
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
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link RemoveAttachmentType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link BeanBuilderTransformer }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link SetPropertyType }{@code >}
     * {@link JAXBElement }{@code <}{@link RefTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link ValueExtractorTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link EncryptionTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link CopyAttachmentType }{@code >}
     * {@link JAXBElement }{@code <}{@link ParseTemplateTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link RemoveVariableType }{@code >}
     * {@link JAXBElement }{@code <}{@link SetVariableType }{@code >}
     * {@link JAXBElement }{@code <}{@link CopyPropertiesType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link MessagePropertiesTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link SetVariableType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link ExpressionTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link RemoveVariableType }{@code >}
     * {@link JAXBElement }{@code <}{@link SetAttachmentType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link CustomTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AppendStringTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link EncryptionTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link RemovePropertyType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
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
     * 
     *                                 Spring-style property elements so that custom configuration can be configured on the
     *                                 custom router.
     *                             Gets the value of the property property.
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
     * {@link PropertyType }
     * 
     * 
     */
    public List<PropertyType> getProperty() {
        if (property == null) {
            property = new ArrayList<PropertyType>();
        }
        return this.property;
    }

    /**
     * Gets the value of the clazz property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClazz() {
        return clazz;
    }

    /**
     * Sets the value of the clazz property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClazz(String value) {
        this.clazz = value;
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
