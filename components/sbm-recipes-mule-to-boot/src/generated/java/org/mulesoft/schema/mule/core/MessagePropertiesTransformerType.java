
package org.mulesoft.schema.mule.core;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for messagePropertiesTransformerType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="messagePropertiesTransformerType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}abstractTransformerType"&gt;
 *       &lt;choice maxOccurs="unbounded"&gt;
 *         &lt;element name="delete-message-property" type="{http://www.mulesoft.org/schema/mule/core}keyType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="add-message-property" type="{http://www.mulesoft.org/schema/mule/core}propertyWithDataType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="rename-message-property" type="{http://www.mulesoft.org/schema/mule/core}keyValueType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="add-message-properties" type="{http://www.mulesoft.org/schema/mule/core}mapType" minOccurs="0"/&gt;
 *       &lt;/choice&gt;
 *       &lt;attribute name="overwrite" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" default="true" /&gt;
 *       &lt;attribute name="scope" default="outbound"&gt;
 *         &lt;simpleType&gt;
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN"&gt;
 *             &lt;enumeration value="invocation"/&gt;
 *             &lt;enumeration value="outbound"/&gt;
 *             &lt;enumeration value="session"/&gt;
 *             &lt;enumeration value="application"/&gt;
 *           &lt;/restriction&gt;
 *         &lt;/simpleType&gt;
 *       &lt;/attribute&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "messagePropertiesTransformerType", propOrder = {
    "deleteMessagePropertyOrAddMessagePropertyOrRenameMessageProperty"
})
public class MessagePropertiesTransformerType
    extends AbstractTransformerType
{

    @XmlElements({
        @XmlElement(name = "delete-message-property", type = KeyType.class),
        @XmlElement(name = "add-message-property", type = PropertyWithDataType.class),
        @XmlElement(name = "rename-message-property", type = KeyValueType.class),
        @XmlElement(name = "add-message-properties", type = MapType.class)
    })
    protected List<Object> deleteMessagePropertyOrAddMessagePropertyOrRenameMessageProperty;
    @XmlAttribute(name = "overwrite")
    protected String overwrite;
    @XmlAttribute(name = "scope")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String scope;

    /**
     * Gets the value of the deleteMessagePropertyOrAddMessagePropertyOrRenameMessageProperty property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the deleteMessagePropertyOrAddMessagePropertyOrRenameMessageProperty property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDeleteMessagePropertyOrAddMessagePropertyOrRenameMessageProperty().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link KeyType }
     * {@link PropertyWithDataType }
     * {@link KeyValueType }
     * {@link MapType }
     * 
     * 
     */
    public List<Object> getDeleteMessagePropertyOrAddMessagePropertyOrRenameMessageProperty() {
        if (deleteMessagePropertyOrAddMessagePropertyOrRenameMessageProperty == null) {
            deleteMessagePropertyOrAddMessagePropertyOrRenameMessageProperty = new ArrayList<Object>();
        }
        return this.deleteMessagePropertyOrAddMessagePropertyOrRenameMessageProperty;
    }

    /**
     * Gets the value of the overwrite property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOverwrite() {
        if (overwrite == null) {
            return "true";
        } else {
            return overwrite;
        }
    }

    /**
     * Sets the value of the overwrite property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOverwrite(String value) {
        this.overwrite = value;
    }

    /**
     * Gets the value of the scope property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getScope() {
        if (scope == null) {
            return "outbound";
        } else {
            return scope;
        }
    }

    /**
     * Sets the value of the scope property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setScope(String value) {
        this.scope = value;
    }

}
