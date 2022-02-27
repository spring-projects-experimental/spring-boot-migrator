
package org.springframework.schema.context;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;attribute name="weaver-class" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="aspectj-weaving" default="autodetect"&gt;
 *         &lt;simpleType&gt;
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *             &lt;enumeration value="on"/&gt;
 *             &lt;enumeration value="off"/&gt;
 *             &lt;enumeration value="autodetect"/&gt;
 *           &lt;/restriction&gt;
 *         &lt;/simpleType&gt;
 *       &lt;/attribute&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "load-time-weaver")
public class LoadTimeWeaver {

    @XmlAttribute(name = "weaver-class")
    protected String weaverClass;
    @XmlAttribute(name = "aspectj-weaving")
    protected String aspectjWeaving;

    /**
     * Gets the value of the weaverClass property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWeaverClass() {
        return weaverClass;
    }

    /**
     * Sets the value of the weaverClass property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWeaverClass(String value) {
        this.weaverClass = value;
    }

    /**
     * Gets the value of the aspectjWeaving property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAspectjWeaving() {
        if (aspectjWeaving == null) {
            return "autodetect";
        } else {
            return aspectjWeaving;
        }
    }

    /**
     * Sets the value of the aspectjWeaving property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAspectjWeaving(String value) {
        this.aspectjWeaving = value;
    }

}
