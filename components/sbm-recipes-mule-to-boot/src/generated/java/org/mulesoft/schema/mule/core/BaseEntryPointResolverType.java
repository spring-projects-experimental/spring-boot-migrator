
package org.mulesoft.schema.mule.core;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for baseEntryPointResolverType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="baseEntryPointResolverType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}abstractEntryPointResolverType"&gt;
 *       &lt;attribute name="acceptVoidMethods" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "baseEntryPointResolverType")
@XmlSeeAlso({
    PropertyEntryPointResolverType.class,
    MethodEntryPointResolverType.class,
    ReflectionEntryPointResolverType.class
})
public class BaseEntryPointResolverType
    extends AbstractEntryPointResolverType
{

    @XmlAttribute(name = "acceptVoidMethods")
    protected String acceptVoidMethods;

    /**
     * Gets the value of the acceptVoidMethods property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAcceptVoidMethods() {
        return acceptVoidMethods;
    }

    /**
     * Sets the value of the acceptVoidMethods property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAcceptVoidMethods(String value) {
        this.acceptVoidMethods = value;
    }

}
