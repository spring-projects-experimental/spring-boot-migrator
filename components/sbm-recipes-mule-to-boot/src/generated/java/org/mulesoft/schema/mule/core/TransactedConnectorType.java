
package org.mulesoft.schema.mule.core;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import org.mulesoft.schema.mule.jms.GenericConnectorType;
import org.mulesoft.schema.mule.jms.VendorJmsConnectorType;


/**
 * <p>Java class for transactedConnectorType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="transactedConnectorType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}connectorType"&gt;
 *       &lt;attribute name="createMultipleTransactedReceivers" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" /&gt;
 *       &lt;attribute name="numberOfConcurrentTransactedReceivers" type="{http://www.mulesoft.org/schema/mule/core}substitutableInt" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "transactedConnectorType")
@XmlSeeAlso({
    GenericConnectorType.class,
    VendorJmsConnectorType.class
})
public class TransactedConnectorType
    extends ConnectorType
{

    @XmlAttribute(name = "createMultipleTransactedReceivers")
    protected String createMultipleTransactedReceivers;
    @XmlAttribute(name = "numberOfConcurrentTransactedReceivers")
    protected String numberOfConcurrentTransactedReceivers;

    /**
     * Gets the value of the createMultipleTransactedReceivers property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCreateMultipleTransactedReceivers() {
        return createMultipleTransactedReceivers;
    }

    /**
     * Sets the value of the createMultipleTransactedReceivers property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCreateMultipleTransactedReceivers(String value) {
        this.createMultipleTransactedReceivers = value;
    }

    /**
     * Gets the value of the numberOfConcurrentTransactedReceivers property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumberOfConcurrentTransactedReceivers() {
        return numberOfConcurrentTransactedReceivers;
    }

    /**
     * Sets the value of the numberOfConcurrentTransactedReceivers property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumberOfConcurrentTransactedReceivers(String value) {
        this.numberOfConcurrentTransactedReceivers = value;
    }

}
