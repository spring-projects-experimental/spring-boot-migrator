
package org.mulesoft.schema.mule.tcp;

import org.mulesoft.schema.mule.tls.Connector;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.*;


/**
 * <p>Java class for tcpConnectorType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tcpConnectorType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/tcp}noProtocolTcpConnectorType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.mulesoft.org/schema/mule/tcp}abstract-protocol" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="dispatcherFactory-ref" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tcpConnectorType", propOrder = {
    "abstractProtocol"
})
@XmlSeeAlso({
    PollingTcpConnectorType.class,
    Connector.class
})
public class TcpConnectorType
    extends NoProtocolTcpConnectorType
{

    @XmlElementRef(name = "abstract-protocol", namespace = "http://www.mulesoft.org/schema/mule/tcp", type = JAXBElement.class, required = false)
    protected JAXBElement<? extends AbstractProtocolType> abstractProtocol;
    @XmlAttribute(name = "dispatcherFactory-ref")
    protected String dispatcherFactoryRef;

    /**
     * 
     *                                 The class name for the protocol handler. This controls how the raw data stream is converted into messages. By default, messages are constructed as dara is received, with no correction for multiple packets or fragmentation. Typically, change this value, or use a transport that includes a protocol like HTTP.
     *                             
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ByteOrMessageProtocolType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractProtocolType }{@code >}
     *     {@link JAXBElement }{@code <}{@link LengthProtocolType }{@code >}
     *     {@link JAXBElement }{@code <}{@link LengthProtocolType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomProtocolType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractProtocolType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ByteOrMessageProtocolType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractProtocolType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomClassLoadingProtocolType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractProtocolType }{@code >}
     *     
     */
    public JAXBElement<? extends AbstractProtocolType> getAbstractProtocol() {
        return abstractProtocol;
    }

    /**
     * Sets the value of the abstractProtocol property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ByteOrMessageProtocolType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractProtocolType }{@code >}
     *     {@link JAXBElement }{@code <}{@link LengthProtocolType }{@code >}
     *     {@link JAXBElement }{@code <}{@link LengthProtocolType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomProtocolType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractProtocolType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ByteOrMessageProtocolType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractProtocolType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomClassLoadingProtocolType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractProtocolType }{@code >}
     *     
     */
    public void setAbstractProtocol(JAXBElement<? extends AbstractProtocolType> value) {
        this.abstractProtocol = value;
    }

    /**
     * Gets the value of the dispatcherFactoryRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDispatcherFactoryRef() {
        return dispatcherFactoryRef;
    }

    /**
     * Sets the value of the dispatcherFactoryRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDispatcherFactoryRef(String value) {
        this.dispatcherFactoryRef = value;
    }

}
