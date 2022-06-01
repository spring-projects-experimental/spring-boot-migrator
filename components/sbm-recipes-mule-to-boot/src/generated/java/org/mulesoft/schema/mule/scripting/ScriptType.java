
package org.mulesoft.schema.mule.scripting;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import org.mulesoft.schema.mule.core.AbstractMixedContentExtensionType;


/**
 * The script to execute as a service. When run inside Mule, scripts have a number of objects available to them in the script context. These are:
 *                 {html}
 *                 
 * <pre>
 * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;table xmlns="http://www.mulesoft.org/schema/mule/scripting" xmlns:mule="http://www.mulesoft.org/schema/mule/core" xmlns:schemadoc="http://www.mulesoft.org/schema/mule/schemadoc" xmlns:xsd="http://www.w3.org/2001/XMLSchema"&gt;&lt;tr&gt;&lt;td&gt;log&lt;/td&gt;&lt;td&gt;a logger that can be used to write to Mule's log file.&lt;/td&gt;&lt;/tr&gt;&lt;tr&gt;&lt;td&gt;muleContext&lt;/td&gt;&lt;td&gt;a reference to the MuleContext object.&lt;/td&gt;&lt;/tr&gt;&lt;tr&gt;&lt;td&gt;message&lt;/td&gt;&lt;td&gt;the current message.&lt;/td&gt;&lt;/tr&gt;&lt;tr&gt;&lt;td&gt;originalPayload&lt;/td&gt;&lt;td&gt;the payload of the current message before any transforms.&lt;/td&gt;&lt;/tr&gt;&lt;tr&gt;&lt;td&gt;payload&lt;/td&gt;&lt;td&gt;the transformed payload of the current message if a transformer is
 *                             configured on the service. Otherwise this is the same value as
 *                             _originalPayload_.
 *                         &lt;/td&gt;&lt;/tr&gt;&lt;tr&gt;&lt;td&gt;service&lt;/td&gt;&lt;td&gt;a reference to the current service object.&lt;/td&gt;&lt;/tr&gt;&lt;tr&gt;&lt;td&gt;id&lt;/td&gt;&lt;td&gt;the current event id.&lt;/td&gt;&lt;/tr&gt;&lt;tr&gt;&lt;td&gt;result&lt;/td&gt;&lt;td&gt;a placeholder object where the result of the script can be written.
 *                             Usually it's better to just return a value from the script unless the script
 *                             method doesn't have a return value.
 *                         &lt;/td&gt;&lt;/tr&gt;&lt;/table&gt;
 * </pre>
 * 
 *                 {html}
 *             
 * 
 * <p>Java class for scriptType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="scriptType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}abstractMixedContentExtensionType"&gt;
 *       &lt;sequence&gt;
 *         &lt;group ref="{http://www.mulesoft.org/schema/mule/core}propertiesGroup" minOccurs="0"/&gt;
 *         &lt;element name="text" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="engine" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="file" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "scriptType")
public class ScriptType
    extends AbstractMixedContentExtensionType
{

    @XmlAttribute(name = "name")
    protected String name;
    @XmlAttribute(name = "engine")
    protected String engine;
    @XmlAttribute(name = "file")
    protected String file;

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
     * Gets the value of the engine property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEngine() {
        return engine;
    }

    /**
     * Sets the value of the engine property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEngine(String value) {
        this.engine = value;
    }

    /**
     * Gets the value of the file property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFile() {
        return file;
    }

    /**
     * Sets the value of the file property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFile(String value) {
        this.file = value;
    }

}
