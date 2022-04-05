
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


/**
 * <p>Java class for pojoBindingType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="pojoBindingType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.mulesoft.org/schema/mule/core}abstract-outbound-endpoint" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="interface" use="required" type="{http://www.mulesoft.org/schema/mule/core}substitutableClass" /&gt;
 *       &lt;attribute name="method" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "pojoBindingType", propOrder = {
    "abstractOutboundEndpoint"
})
public class PojoBindingType {

    @XmlElementRef(name = "abstract-outbound-endpoint", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class)
    protected List<JAXBElement<? extends AbstractOutboundEndpointType>> abstractOutboundEndpoint;
    @XmlAttribute(name = "interface", required = true)
    protected String _interface;
    @XmlAttribute(name = "method")
    @XmlSchemaType(name = "anySimpleType")
    protected String method;

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
     * Gets the value of the interface property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInterface() {
        return _interface;
    }

    /**
     * Sets the value of the interface property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInterface(String value) {
        this._interface = value;
    }

    /**
     * Gets the value of the method property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMethod() {
        return method;
    }

    /**
     * Sets the value of the method property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMethod(String value) {
        this.method = value;
    }

}
