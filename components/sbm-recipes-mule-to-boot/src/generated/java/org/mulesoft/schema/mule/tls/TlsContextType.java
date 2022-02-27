
package org.mulesoft.schema.mule.tls;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlType;
import org.mulesoft.schema.mule.core.AbstractExtensionType;


/**
 * <p>Java class for tlsContextType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tlsContextType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}abstractExtensionType"&gt;
 *       &lt;sequence&gt;
 *         &lt;choice&gt;
 *           &lt;sequence&gt;
 *             &lt;element ref="{http://www.mulesoft.org/schema/mule/tls}trust-store"/&gt;
 *             &lt;element ref="{http://www.mulesoft.org/schema/mule/tls}key-store" minOccurs="0"/&gt;
 *           &lt;/sequence&gt;
 *           &lt;sequence&gt;
 *             &lt;element ref="{http://www.mulesoft.org/schema/mule/tls}trust-store" minOccurs="0"/&gt;
 *             &lt;element ref="{http://www.mulesoft.org/schema/mule/tls}key-store"/&gt;
 *           &lt;/sequence&gt;
 *           &lt;sequence&gt;
 *             &lt;element ref="{http://www.mulesoft.org/schema/mule/tls}trust-store"/&gt;
 *             &lt;element ref="{http://www.mulesoft.org/schema/mule/tls}key-store"/&gt;
 *           &lt;/sequence&gt;
 *         &lt;/choice&gt;
 *         &lt;element name="revocation-check" type="{http://www.mulesoft.org/schema/mule/tls}rcWrapperElement" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="enabledProtocols" type="{http://www.mulesoft.org/schema/mule/core}substitutableString" /&gt;
 *       &lt;attribute name="enabledCipherSuites" type="{http://www.mulesoft.org/schema/mule/core}substitutableString" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tlsContextType", propOrder = {
    "rest"
})
public class TlsContextType
    extends AbstractExtensionType
{

    @XmlElementRefs({
        @XmlElementRef(name = "trust-store", namespace = "http://www.mulesoft.org/schema/mule/tls", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "key-store", namespace = "http://www.mulesoft.org/schema/mule/tls", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "revocation-check", namespace = "http://www.mulesoft.org/schema/mule/tls", type = JAXBElement.class, required = false)
    })
    protected List<JAXBElement<?>> rest;
    @XmlAttribute(name = "name")
    protected String name;
    @XmlAttribute(name = "enabledProtocols")
    protected String enabledProtocols;
    @XmlAttribute(name = "enabledCipherSuites")
    protected String enabledCipherSuites;

    /**
     * Gets the rest of the content model. 
     * 
     * <p>
     * You are getting this "catch-all" property because of the following reason: 
     * The field name "TrustStore" is used by two different parts of a schema. See: 
     * line 166 of file:/Users/sanagaraj/workspace/spring-boot-migrator/components/sbm-recipes-mule-to-boot/src/main/xsd/mule/mule-tls.xsd
     * line 162 of file:/Users/sanagaraj/workspace/spring-boot-migrator/components/sbm-recipes-mule-to-boot/src/main/xsd/mule/mule-tls.xsd
     * <p>
     * To get rid of this property, apply a property customization to one 
     * of both of the following declarations to change their names: 
     * Gets the value of the rest property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the rest property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRest().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link TlsContextTrustStoreType }{@code >}
     * {@link JAXBElement }{@code <}{@link TlsContextKeyStoreType }{@code >}
     * {@link JAXBElement }{@code <}{@link RcWrapperElement }{@code >}
     * 
     * 
     */
    public List<JAXBElement<?>> getRest() {
        if (rest == null) {
            rest = new ArrayList<JAXBElement<?>>();
        }
        return this.rest;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the enabledProtocols property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnabledProtocols() {
        return enabledProtocols;
    }

    /**
     * Sets the value of the enabledProtocols property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnabledProtocols(String value) {
        this.enabledProtocols = value;
    }

    /**
     * Gets the value of the enabledCipherSuites property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnabledCipherSuites() {
        return enabledCipherSuites;
    }

    /**
     * Sets the value of the enabledCipherSuites property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnabledCipherSuites(String value) {
        this.enabledCipherSuites = value;
    }

}
