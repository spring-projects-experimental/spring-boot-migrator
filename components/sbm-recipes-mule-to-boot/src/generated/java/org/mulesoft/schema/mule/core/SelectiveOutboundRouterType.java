
package org.mulesoft.schema.mule.core;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for selectiveOutboundRouterType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="selectiveOutboundRouterType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}abstractRoutingMessageProcessorType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="when" type="{http://www.mulesoft.org/schema/mule/core}whenMessageProcessorFilterPairType" maxOccurs="unbounded"/&gt;
 *         &lt;element name="otherwise" type="{http://www.mulesoft.org/schema/mule/core}otherwiseMessageProcessorFilterPairType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "selectiveOutboundRouterType", propOrder = {
    "when",
    "otherwise"
})
public class SelectiveOutboundRouterType
    extends AbstractRoutingMessageProcessorType
{

    @XmlElement(required = true)
    protected List<WhenMessageProcessorFilterPairType> when;
    protected OtherwiseMessageProcessorFilterPairType otherwise;

    /**
     * Gets the value of the when property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the when property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getWhen().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link WhenMessageProcessorFilterPairType }
     * 
     * 
     */
    public List<WhenMessageProcessorFilterPairType> getWhen() {
        if (when == null) {
            when = new ArrayList<WhenMessageProcessorFilterPairType>();
        }
        return this.when;
    }

    /**
     * Gets the value of the otherwise property.
     * 
     * @return
     *     possible object is
     *     {@link OtherwiseMessageProcessorFilterPairType }
     *     
     */
    public OtherwiseMessageProcessorFilterPairType getOtherwise() {
        return otherwise;
    }

    /**
     * Sets the value of the otherwise property.
     * 
     * @param value
     *     allowed object is
     *     {@link OtherwiseMessageProcessorFilterPairType }
     *     
     */
    public void setOtherwise(OtherwiseMessageProcessorFilterPairType value) {
        this.otherwise = value;
    }

}
