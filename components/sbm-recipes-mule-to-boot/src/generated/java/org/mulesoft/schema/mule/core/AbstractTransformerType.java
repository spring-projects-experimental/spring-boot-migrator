
package org.mulesoft.schema.mule.core;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for abstractTransformerType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="abstractTransformerType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}commonTransformerType"&gt;
 *       &lt;attGroup ref="{http://www.mulesoft.org/schema/mule/core}mimeTypeAttributes"/&gt;
 *       &lt;attribute name="name" type="{http://www.mulesoft.org/schema/mule/core}substitutableName" /&gt;
 *       &lt;attribute name="returnClass" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="ignoreBadInput" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" /&gt;
 *       &lt;attribute name="encoding" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "abstractTransformerType")
@XmlSeeAlso({
    CustomTransformerType.class,
    MessagePropertiesTransformerType.class,
    AppendStringTransformerType.class,
    ParseTemplateTransformerType.class,
    EncryptionTransformerType.class,
    BeanBuilderTransformer.class,
    ExpressionTransformerType.class,
    ValueExtractorTransformerType.class
})
public class AbstractTransformerType
    extends CommonTransformerType
{

    @XmlAttribute(name = "name")
    protected String name;
    @XmlAttribute(name = "returnClass")
    protected String returnClass;
    @XmlAttribute(name = "ignoreBadInput")
    protected String ignoreBadInput;
    @XmlAttribute(name = "encoding")
    protected String encoding;
    @XmlAttribute(name = "mimeType")
    protected String mimeType;

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the returnClass property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReturnClass() {
        return returnClass;
    }

    /**
     * Sets the value of the returnClass property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReturnClass(String value) {
        this.returnClass = value;
    }

    /**
     * Gets the value of the ignoreBadInput property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIgnoreBadInput() {
        return ignoreBadInput;
    }

    /**
     * Sets the value of the ignoreBadInput property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIgnoreBadInput(String value) {
        this.ignoreBadInput = value;
    }

    /**
     * Gets the value of the encoding property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * Sets the value of the encoding property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEncoding(String value) {
        this.encoding = value;
    }

    /**
     * Gets the value of the mimeType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMimeType() {
        return mimeType;
    }

    /**
     * Sets the value of the mimeType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMimeType(String value) {
        this.mimeType = value;
    }

}
