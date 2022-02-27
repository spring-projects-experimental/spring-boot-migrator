
package org.mulesoft.schema.mule.tcp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for customClassLoadingProtocolType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="customClassLoadingProtocolType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/tcp}lengthProtocolType"&gt;
 *       &lt;attribute name="classLoader-ref" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "customClassLoadingProtocolType")
public class CustomClassLoadingProtocolType
    extends LengthProtocolType
{

    @XmlAttribute(name = "classLoader-ref")
    protected String classLoaderRef;

    /**
     * Gets the value of the classLoaderRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClassLoaderRef() {
        return classLoaderRef;
    }

    /**
     * Sets the value of the classLoaderRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClassLoaderRef(String value) {
        this.classLoaderRef = value;
    }

}
