
package org.mulesoft.schema.mule.core;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for complexEntryPointResolverType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="complexEntryPointResolverType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}reflectionEntryPointResolverType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.mulesoft.org/schema/mule/core}include-entry-point" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="enableDiscovery" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" default="true" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "complexEntryPointResolverType", propOrder = {
    "includeEntryPoint"
})
public class ComplexEntryPointResolverType
    extends ReflectionEntryPointResolverType
{

    @XmlElement(name = "include-entry-point")
    protected List<MethodType> includeEntryPoint;
    @XmlAttribute(name = "enableDiscovery")
    protected String enableDiscovery;

    /**
     * Gets the value of the includeEntryPoint property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the includeEntryPoint property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIncludeEntryPoint().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MethodType }
     * 
     * 
     */
    public List<MethodType> getIncludeEntryPoint() {
        if (includeEntryPoint == null) {
            includeEntryPoint = new ArrayList<MethodType>();
        }
        return this.includeEntryPoint;
    }

    /**
     * Gets the value of the enableDiscovery property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnableDiscovery() {
        if (enableDiscovery == null) {
            return "true";
        } else {
            return enableDiscovery;
        }
    }

    /**
     * Sets the value of the enableDiscovery property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnableDiscovery(String value) {
        this.enableDiscovery = value;
    }

}
