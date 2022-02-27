
package org.mulesoft.schema.mule.core;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for threadingProfileType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="threadingProfileType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}asynchronousThreadingProfileType"&gt;
 *       &lt;attribute name="doThreading" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" default="true" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "threadingProfileType")
public class ThreadingProfileType
    extends AsynchronousThreadingProfileType
{

    @XmlAttribute(name = "doThreading")
    protected String doThreading;

    /**
     * Gets the value of the doThreading property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDoThreading() {
        if (doThreading == null) {
            return "true";
        } else {
            return doThreading;
        }
    }

    /**
     * Sets the value of the doThreading property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDoThreading(String value) {
        this.doThreading = value;
    }

}
