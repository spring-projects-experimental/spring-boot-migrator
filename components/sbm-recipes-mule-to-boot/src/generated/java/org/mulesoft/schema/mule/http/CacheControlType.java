
package org.mulesoft.schema.mule.http;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import org.mulesoft.schema.mule.core.AnnotatedType;


/**
 * <p>Java class for cacheControlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="cacheControlType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}annotatedType"&gt;
 *       &lt;attribute name="directive"&gt;
 *         &lt;simpleType&gt;
 *           &lt;union&gt;
 *             &lt;simpleType&gt;
 *               &lt;restriction base="{http://www.mulesoft.org/schema/mule/core}substitutableInt"&gt;
 *               &lt;/restriction&gt;
 *             &lt;/simpleType&gt;
 *             &lt;simpleType&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN"&gt;
 *                 &lt;enumeration value="public"/&gt;
 *                 &lt;enumeration value="private"/&gt;
 *               &lt;/restriction&gt;
 *             &lt;/simpleType&gt;
 *           &lt;/union&gt;
 *         &lt;/simpleType&gt;
 *       &lt;/attribute&gt;
 *       &lt;attribute name="noCache" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" default="false" /&gt;
 *       &lt;attribute name="noStore" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" default="false" /&gt;
 *       &lt;attribute name="mustRevalidate" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" default="false" /&gt;
 *       &lt;attribute name="maxAge" type="{http://www.mulesoft.org/schema/mule/core}substitutableInt" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cacheControlType")
public class CacheControlType
    extends AnnotatedType
{

    @XmlAttribute(name = "directive")
    protected List<String> directive;
    @XmlAttribute(name = "noCache")
    protected String noCache;
    @XmlAttribute(name = "noStore")
    protected String noStore;
    @XmlAttribute(name = "mustRevalidate")
    protected String mustRevalidate;
    @XmlAttribute(name = "maxAge")
    protected String maxAge;

    /**
     * Gets the value of the directive property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the directive property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDirective().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getDirective() {
        if (directive == null) {
            directive = new ArrayList<String>();
        }
        return this.directive;
    }

    /**
     * Gets the value of the noCache property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNoCache() {
        if (noCache == null) {
            return "false";
        } else {
            return noCache;
        }
    }

    /**
     * Sets the value of the noCache property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNoCache(String value) {
        this.noCache = value;
    }

    /**
     * Gets the value of the noStore property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNoStore() {
        if (noStore == null) {
            return "false";
        } else {
            return noStore;
        }
    }

    /**
     * Sets the value of the noStore property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNoStore(String value) {
        this.noStore = value;
    }

    /**
     * Gets the value of the mustRevalidate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMustRevalidate() {
        if (mustRevalidate == null) {
            return "false";
        } else {
            return mustRevalidate;
        }
    }

    /**
     * Sets the value of the mustRevalidate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMustRevalidate(String value) {
        this.mustRevalidate = value;
    }

    /**
     * Gets the value of the maxAge property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaxAge() {
        return maxAge;
    }

    /**
     * Sets the value of the maxAge property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaxAge(String value) {
        this.maxAge = value;
    }

}
