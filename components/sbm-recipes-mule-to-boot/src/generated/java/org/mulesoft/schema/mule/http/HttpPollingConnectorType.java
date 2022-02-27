
package org.mulesoft.schema.mule.http;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for httpPollingConnectorType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="httpPollingConnectorType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/http}httpConnectorType"&gt;
 *       &lt;attribute name="pollingFrequency" type="{http://www.mulesoft.org/schema/mule/core}substitutableLong" /&gt;
 *       &lt;attribute name="checkEtag" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" /&gt;
 *       &lt;attribute name="discardEmptyContent" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "httpPollingConnectorType")
public class HttpPollingConnectorType
    extends HttpConnectorType
{

    @XmlAttribute(name = "pollingFrequency")
    protected String pollingFrequency;
    @XmlAttribute(name = "checkEtag")
    protected String checkEtag;
    @XmlAttribute(name = "discardEmptyContent")
    protected String discardEmptyContent;

    /**
     * Gets the value of the pollingFrequency property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPollingFrequency() {
        return pollingFrequency;
    }

    /**
     * Sets the value of the pollingFrequency property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPollingFrequency(String value) {
        this.pollingFrequency = value;
    }

    /**
     * Gets the value of the checkEtag property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCheckEtag() {
        return checkEtag;
    }

    /**
     * Sets the value of the checkEtag property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCheckEtag(String value) {
        this.checkEtag = value;
    }

    /**
     * Gets the value of the discardEmptyContent property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDiscardEmptyContent() {
        return discardEmptyContent;
    }

    /**
     * Sets the value of the discardEmptyContent property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDiscardEmptyContent(String value) {
        this.discardEmptyContent = value;
    }

}
