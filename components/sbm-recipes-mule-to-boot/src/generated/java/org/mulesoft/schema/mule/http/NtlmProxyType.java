
package org.mulesoft.schema.mule.http;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for NtlmProxyType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="NtlmProxyType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/http}proxyType"&gt;
 *       &lt;attribute name="ntlmDomain" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NtlmProxyType")
@XmlSeeAlso({
    GlobalNtlmProxyType.class
})
public class NtlmProxyType
    extends ProxyType
{

    @XmlAttribute(name = "ntlmDomain", required = true)
    protected String ntlmDomain;

    /**
     * Gets the value of the ntlmDomain property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNtlmDomain() {
        return ntlmDomain;
    }

    /**
     * Sets the value of the ntlmDomain property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNtlmDomain(String value) {
        this.ntlmDomain = value;
    }

}
