
package org.mulesoft.schema.mule.core;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlType;
import org.mulesoft.schema.mule.http.ListenerType;


/**
 * <p>Java class for compositeMessageSourceType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="compositeMessageSourceType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}abstractMessageSourceType"&gt;
 *       &lt;sequence maxOccurs="unbounded"&gt;
 *         &lt;group ref="{http://www.mulesoft.org/schema/mule/core}messageSourceOrInboundEndpoint"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "compositeMessageSourceType", propOrder = {
    "abstractMessageSourceOrAbstractInboundEndpoint"
})
public class CompositeMessageSourceType
    extends AbstractMessageSourceType
{

    @XmlElementRefs({
        @XmlElementRef(name = "abstract-message-source", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "abstract-inbound-endpoint", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    })
    protected List<JAXBElement<? extends AnnotatedType>> abstractMessageSourceOrAbstractInboundEndpoint;

    /**
     * Gets the value of the abstractMessageSourceOrAbstractInboundEndpoint property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the abstractMessageSourceOrAbstractInboundEndpoint property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAbstractMessageSourceOrAbstractInboundEndpoint().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link ListenerType }{@code >}
     * {@link JAXBElement }{@code <}{@link CustomMessageSourceType }{@code >}
     * {@link JAXBElement }{@code <}{@link CompositeMessageSourceType }{@code >}
     * {@link JAXBElement }{@code <}{@link PollInboundEndpointType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractMessageSourceType }{@code >}
     * {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.ee.wmq.InboundEndpointType }{@code >}
     * {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.core.InboundEndpointType }{@code >}
     * {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.tcp.InboundEndpointType }{@code >}
     * {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.jms.InboundEndpointType }{@code >}
     * {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.amqp.InboundEndpointType }{@code >}
     * {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.http.InboundEndpointType }{@code >}
     * {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.tls.InboundEndpointType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractInboundEndpointType }{@code >}
     * 
     * 
     */
    public List<JAXBElement<? extends AnnotatedType>> getAbstractMessageSourceOrAbstractInboundEndpoint() {
        if (abstractMessageSourceOrAbstractInboundEndpoint == null) {
            abstractMessageSourceOrAbstractInboundEndpoint = new ArrayList<JAXBElement<? extends AnnotatedType>>();
        }
        return this.abstractMessageSourceOrAbstractInboundEndpoint;
    }

}
