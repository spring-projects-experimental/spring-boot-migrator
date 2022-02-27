
package org.mulesoft.schema.mule.core;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for reflectionEntryPointResolverType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="reflectionEntryPointResolverType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}baseEntryPointResolverType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="exclude-object-methods" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/&gt;
 *         &lt;element name="exclude-entry-point" type="{http://www.mulesoft.org/schema/mule/core}methodType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "reflectionEntryPointResolverType", propOrder = {
    "excludeObjectMethods",
    "excludeEntryPoint"
})
@XmlSeeAlso({
    ComplexEntryPointResolverType.class
})
public class ReflectionEntryPointResolverType
    extends BaseEntryPointResolverType
{

    @XmlElement(name = "exclude-object-methods")
    protected Object excludeObjectMethods;
    @XmlElement(name = "exclude-entry-point")
    protected List<MethodType> excludeEntryPoint;

    /**
     * Gets the value of the excludeObjectMethods property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getExcludeObjectMethods() {
        return excludeObjectMethods;
    }

    /**
     * Sets the value of the excludeObjectMethods property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setExcludeObjectMethods(Object value) {
        this.excludeObjectMethods = value;
    }

    /**
     * Gets the value of the excludeEntryPoint property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the excludeEntryPoint property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getExcludeEntryPoint().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MethodType }
     * 
     * 
     */
    public List<MethodType> getExcludeEntryPoint() {
        if (excludeEntryPoint == null) {
            excludeEntryPoint = new ArrayList<MethodType>();
        }
        return this.excludeEntryPoint;
    }

}
