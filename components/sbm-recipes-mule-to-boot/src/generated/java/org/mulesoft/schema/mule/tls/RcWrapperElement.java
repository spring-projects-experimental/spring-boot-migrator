
package org.mulesoft.schema.mule.tls;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for rcWrapperElement complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="rcWrapperElement"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;choice&gt;
 *         &lt;element name="standard-revocation-check" type="{http://www.mulesoft.org/schema/mule/tls}rcStandardType"/&gt;
 *         &lt;element name="crl-file" type="{http://www.mulesoft.org/schema/mule/tls}rcCrlFileType"/&gt;
 *         &lt;element name="custom-ocsp-responder" type="{http://www.mulesoft.org/schema/mule/tls}rcCustomOcspType"/&gt;
 *       &lt;/choice&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "rcWrapperElement", propOrder = {
    "standardRevocationCheck",
    "crlFile",
    "customOcspResponder"
})
public class RcWrapperElement {

    @XmlElement(name = "standard-revocation-check")
    protected RcStandardType standardRevocationCheck;
    @XmlElement(name = "crl-file")
    protected RcCrlFileType crlFile;
    @XmlElement(name = "custom-ocsp-responder")
    protected RcCustomOcspType customOcspResponder;

    /**
     * Gets the value of the standardRevocationCheck property.
     * 
     * @return
     *     possible object is
     *     {@link RcStandardType }
     *     
     */
    public RcStandardType getStandardRevocationCheck() {
        return standardRevocationCheck;
    }

    /**
     * Sets the value of the standardRevocationCheck property.
     * 
     * @param value
     *     allowed object is
     *     {@link RcStandardType }
     *     
     */
    public void setStandardRevocationCheck(RcStandardType value) {
        this.standardRevocationCheck = value;
    }

    /**
     * Gets the value of the crlFile property.
     * 
     * @return
     *     possible object is
     *     {@link RcCrlFileType }
     *     
     */
    public RcCrlFileType getCrlFile() {
        return crlFile;
    }

    /**
     * Sets the value of the crlFile property.
     * 
     * @param value
     *     allowed object is
     *     {@link RcCrlFileType }
     *     
     */
    public void setCrlFile(RcCrlFileType value) {
        this.crlFile = value;
    }

    /**
     * Gets the value of the customOcspResponder property.
     * 
     * @return
     *     possible object is
     *     {@link RcCustomOcspType }
     *     
     */
    public RcCustomOcspType getCustomOcspResponder() {
        return customOcspResponder;
    }

    /**
     * Sets the value of the customOcspResponder property.
     * 
     * @param value
     *     allowed object is
     *     {@link RcCustomOcspType }
     *     
     */
    public void setCustomOcspResponder(RcCustomOcspType value) {
        this.customOcspResponder = value;
    }

}
