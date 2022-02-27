
package org.mulesoft.schema.mule.core;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for baseMessageFilterType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="baseMessageFilterType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}abstractGlobalInterceptingMessageProcessorType"&gt;
 *       &lt;attribute name="onUnaccepted" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="throwOnUnaccepted" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "baseMessageFilterType")
@XmlSeeAlso({
    MessageFilterType.class,
    IdempotentMessageFilterType.class
})
public class BaseMessageFilterType
    extends AbstractGlobalInterceptingMessageProcessorType
{

    @XmlAttribute(name = "onUnaccepted")
    protected String onUnaccepted;
    @XmlAttribute(name = "throwOnUnaccepted")
    protected String throwOnUnaccepted;

    /**
     * Gets the value of the onUnaccepted property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOnUnaccepted() {
        return onUnaccepted;
    }

    /**
     * Sets the value of the onUnaccepted property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOnUnaccepted(String value) {
        this.onUnaccepted = value;
    }

    /**
     * Gets the value of the throwOnUnaccepted property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getThrowOnUnaccepted() {
        return throwOnUnaccepted;
    }

    /**
     * Sets the value of the throwOnUnaccepted property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setThrowOnUnaccepted(String value) {
        this.throwOnUnaccepted = value;
    }

}
