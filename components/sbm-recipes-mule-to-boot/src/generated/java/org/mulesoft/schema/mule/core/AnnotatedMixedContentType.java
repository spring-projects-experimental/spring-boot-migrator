
package org.mulesoft.schema.mule.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;


/**
 * <p>Java class for annotatedMixedContentType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="annotatedMixedContentType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="annotations" type="{http://www.mulesoft.org/schema/mule/core}annotationsType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "annotatedMixedContentType", propOrder = {
    "content"
})
@XmlSeeAlso({
    org.mulesoft.schema.mule.ee.dw.TransformMessageType.InputPayload.class,
    org.mulesoft.schema.mule.ee.dw.TransformMessageType.InputVariable.class,
    org.mulesoft.schema.mule.ee.dw.TransformMessageType.InputSessionVariable.class,
    org.mulesoft.schema.mule.ee.dw.TransformMessageType.InputInboundProperty.class,
    org.mulesoft.schema.mule.ee.dw.TransformMessageType.InputOutboundProperty.class,
    org.mulesoft.schema.mule.ee.dw.TransformMessageType.SetPayload.class,
    org.mulesoft.schema.mule.ee.dw.TransformMessageType.SetVariable.class,
    org.mulesoft.schema.mule.ee.dw.TransformMessageType.SetSessionVariable.class,
    org.mulesoft.schema.mule.ee.dw.TransformMessageType.SetProperty.class,
    AbstractMixedContentExtensionType.class,
    AbstractMixedContentMessageProcessorType.class
})
public abstract class AnnotatedMixedContentType {

    @XmlElementRef(name = "annotations", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    @XmlMixed
    protected List<Serializable> content;
    @XmlAnyAttribute
    private Map<QName, String> otherAttributes = new HashMap<QName, String>();

    /**
     * Gets the value of the content property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the content property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContent().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link AnnotationsType }{@code >}
     * {@link String }
     * 
     * 
     */
    public List<Serializable> getContent() {
        if (content == null) {
            content = new ArrayList<Serializable>();
        }
        return this.content;
    }

    /**
     * Gets a map that contains attributes that aren't bound to any typed property on this class.
     * 
     * <p>
     * the map is keyed by the name of the attribute and 
     * the value is the string value of the attribute.
     * 
     * the map returned by this method is live, and you can add new attribute
     * by updating the map directly. Because of this design, there's no setter.
     * 
     * 
     * @return
     *     always non-null
     */
    public Map<QName, String> getOtherAttributes() {
        return otherAttributes;
    }

}
