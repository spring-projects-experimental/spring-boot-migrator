
package org.mulesoft.schema.mule.db;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;
import org.mulesoft.schema.mule.core.AnnotationsType;


/**
 * <p>Java class for abstractDbMixedContentMessageProcessorType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="abstractDbMixedContentMessageProcessorType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}abstractMixedContentMessageProcessorType"&gt;
 *       &lt;attGroup ref="{http://www.mulesoft.org/schema/mule/db}dbMessageProcessorAttributes"/&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "abstractDbMixedContentMessageProcessorType", propOrder = {
    "content"
})
@XmlSeeAlso({
    BulkUpdateMessageProcessorType.class
})
public class AbstractDbMixedContentMessageProcessorType {

    @XmlElementRef(name = "annotations", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    @XmlMixed
    protected List<Serializable> content;
    @XmlAttribute(name = "config-ref")
    protected String configRef;
    @XmlAttribute(name = "source")
    protected String source;
    @XmlAttribute(name = "target")
    protected String target;
    @XmlAttribute(name = "transactionalAction")
    protected TransactionalActionType transactionalAction;
    @XmlAttribute(name = "queryTimeout")
    protected BigInteger queryTimeout;
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
     * Gets the value of the configRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConfigRef() {
        return configRef;
    }

    /**
     * Sets the value of the configRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConfigRef(String value) {
        this.configRef = value;
    }

    /**
     * Gets the value of the source property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSource() {
        return source;
    }

    /**
     * Sets the value of the source property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSource(String value) {
        this.source = value;
    }

    /**
     * Gets the value of the target property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTarget() {
        if (target == null) {
            return "#[payload]";
        } else {
            return target;
        }
    }

    /**
     * Sets the value of the target property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTarget(String value) {
        this.target = value;
    }

    /**
     * Gets the value of the transactionalAction property.
     * 
     * @return
     *     possible object is
     *     {@link TransactionalActionType }
     *     
     */
    public TransactionalActionType getTransactionalAction() {
        if (transactionalAction == null) {
            return TransactionalActionType.JOIN_IF_POSSIBLE;
        } else {
            return transactionalAction;
        }
    }

    /**
     * Sets the value of the transactionalAction property.
     * 
     * @param value
     *     allowed object is
     *     {@link TransactionalActionType }
     *     
     */
    public void setTransactionalAction(TransactionalActionType value) {
        this.transactionalAction = value;
    }

    /**
     * Gets the value of the queryTimeout property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getQueryTimeout() {
        if (queryTimeout == null) {
            return new BigInteger("0");
        } else {
            return queryTimeout;
        }
    }

    /**
     * Sets the value of the queryTimeout property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setQueryTimeout(BigInteger value) {
        this.queryTimeout = value;
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
