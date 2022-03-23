
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
import org.mulesoft.schema.mule.http.RestServiceWrapperType;


/**
 * 
 *                 A simple request-response service, with no outbound router.
 *             
 * 
 * <p>Java class for simpleServiceType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="simpleServiceType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}baseFlowConstructType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.mulesoft.org/schema/mule/core}abstract-inbound-endpoint" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.mulesoft.org/schema/mule/core}abstract-component" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.mulesoft.org/schema/mule/core}legacy-abstract-exception-strategy" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attGroup ref="{http://www.mulesoft.org/schema/mule/core}simpleServiceTypes"/&gt;
 *       &lt;attribute name="address" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="endpoint-ref" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="transformer-refs" type="{http://www.w3.org/2001/XMLSchema}NMTOKENS" /&gt;
 *       &lt;attribute name="responseTransformer-refs" type="{http://www.w3.org/2001/XMLSchema}NMTOKENS" /&gt;
 *       &lt;attribute name="component-ref" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="component-class" type="{http://www.mulesoft.org/schema/mule/core}substitutableClass" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "simpleServiceType", propOrder = {
    "abstractInboundEndpoint",
    "abstractComponent",
    "legacyAbstractExceptionStrategy"
})
public class SimpleServiceType
    extends BaseFlowConstructType
{

    @XmlElementRef(name = "abstract-inbound-endpoint", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    protected JAXBElement<? extends AbstractInboundEndpointType> abstractInboundEndpoint;
    @XmlElementRef(name = "abstract-component", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    protected JAXBElement<? extends AbstractComponentType> abstractComponent;
    @XmlElementRef(name = "legacy-abstract-exception-strategy", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    protected JAXBElement<? extends ExceptionStrategyType> legacyAbstractExceptionStrategy;
    @XmlAttribute(name = "address")
    protected String address;
    @XmlAttribute(name = "endpoint-ref")
    protected String endpointRef;
    @XmlAttribute(name = "transformer-refs")
    @XmlSchemaType(name = "NMTOKENS")
    protected List<String> transformerRefs;
    @XmlAttribute(name = "responseTransformer-refs")
    @XmlSchemaType(name = "NMTOKENS")
    protected List<String> responseTransformerRefs;
    @XmlAttribute(name = "component-ref")
    protected String componentRef;
    @XmlAttribute(name = "component-class")
    protected String componentClass;
    @XmlAttribute(name = "type")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String type;

    /**
     * 
     *                                 The endpoint on which messages are received.
     *                             
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.core.InboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.tls.InboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.tcp.InboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.ee.wmq.InboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.jms.InboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.amqp.InboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.http.InboundEndpointType }{@code >}
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
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.core.InboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.tls.InboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.tcp.InboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.ee.wmq.InboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.jms.InboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.amqp.InboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.http.InboundEndpointType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractInboundEndpointType }{@code >}
     *     
     */
    public void setAbstractInboundEndpoint(JAXBElement<? extends AbstractInboundEndpointType> value) {
        this.abstractInboundEndpoint = value;
    }

    /**
     * 
     *                                 The component that is invoked when incoming messages are received.
     *                             
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link DefaultJavaComponentType }{@code >}
     *     {@link JAXBElement }{@code <}{@link DefaultComponentType }{@code >}
     *     {@link JAXBElement }{@code <}{@link PooledJavaComponentType }{@code >}
     *     {@link JAXBElement }{@code <}{@link DefaultComponentType }{@code >}
     *     {@link JAXBElement }{@code <}{@link RestServiceWrapperType }{@code >}
     *     {@link JAXBElement }{@code <}{@link StaticComponentType }{@code >}
     *     {@link JAXBElement }{@code <}{@link DefaultComponentType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractComponentType }{@code >}
     *     
     */
    public JAXBElement<? extends AbstractComponentType> getAbstractComponent() {
        return abstractComponent;
    }

    /**
     * Sets the value of the abstractComponent property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link DefaultJavaComponentType }{@code >}
     *     {@link JAXBElement }{@code <}{@link DefaultComponentType }{@code >}
     *     {@link JAXBElement }{@code <}{@link PooledJavaComponentType }{@code >}
     *     {@link JAXBElement }{@code <}{@link DefaultComponentType }{@code >}
     *     {@link JAXBElement }{@code <}{@link RestServiceWrapperType }{@code >}
     *     {@link JAXBElement }{@code <}{@link StaticComponentType }{@code >}
     *     {@link JAXBElement }{@code <}{@link DefaultComponentType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractComponentType }{@code >}
     *     
     */
    public void setAbstractComponent(JAXBElement<? extends AbstractComponentType> value) {
        this.abstractComponent = value;
    }

    /**
     * Gets the value of the legacyAbstractExceptionStrategy property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link CustomExceptionStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ChoiceExceptionStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link RollbackExceptionStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CatchExceptionStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ReferenceExceptionStrategyType }{@code >}
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
     *     {@link JAXBElement }{@code <}{@link CustomExceptionStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ChoiceExceptionStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link RollbackExceptionStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CatchExceptionStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ReferenceExceptionStrategyType }{@code >}
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
     * Gets the value of the endpointRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEndpointRef() {
        return endpointRef;
    }

    /**
     * Sets the value of the endpointRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEndpointRef(String value) {
        this.endpointRef = value;
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
     * Gets the value of the componentRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComponentRef() {
        return componentRef;
    }

    /**
     * Sets the value of the componentRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComponentRef(String value) {
        this.componentRef = value;
    }

    /**
     * Gets the value of the componentClass property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComponentClass() {
        return componentClass;
    }

    /**
     * Sets the value of the componentClass property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComponentClass(String value) {
        this.componentClass = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

}
