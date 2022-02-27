
package org.mulesoft.schema.mule.schemadoc;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for transportFeaturesType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="transportFeaturesType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="MEPs" type="{http://www.mulesoft.org/schema/mule/schemadoc}MEPsType"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="receiveEvents" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *       &lt;attribute name="dispatchEvents" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *       &lt;attribute name="requestEvents" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *       &lt;attribute name="transactions" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *       &lt;attribute name="transactionTypes" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="streaming" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *       &lt;attribute name="retries" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "transportFeaturesType", propOrder = {
    "mePs"
})
public class TransportFeaturesType {

    @XmlElement(name = "MEPs", required = true)
    protected MEPsType mePs;
    @XmlAttribute(name = "receiveEvents", required = true)
    protected boolean receiveEvents;
    @XmlAttribute(name = "dispatchEvents", required = true)
    protected boolean dispatchEvents;
    @XmlAttribute(name = "requestEvents", required = true)
    protected boolean requestEvents;
    @XmlAttribute(name = "transactions", required = true)
    protected boolean transactions;
    @XmlAttribute(name = "transactionTypes")
    protected String transactionTypes;
    @XmlAttribute(name = "streaming", required = true)
    protected boolean streaming;
    @XmlAttribute(name = "retries")
    protected Boolean retries;

    /**
     * Gets the value of the mePs property.
     * 
     * @return
     *     possible object is
     *     {@link MEPsType }
     *     
     */
    public MEPsType getMEPs() {
        return mePs;
    }

    /**
     * Sets the value of the mePs property.
     * 
     * @param value
     *     allowed object is
     *     {@link MEPsType }
     *     
     */
    public void setMEPs(MEPsType value) {
        this.mePs = value;
    }

    /**
     * Gets the value of the receiveEvents property.
     * 
     */
    public boolean isReceiveEvents() {
        return receiveEvents;
    }

    /**
     * Sets the value of the receiveEvents property.
     * 
     */
    public void setReceiveEvents(boolean value) {
        this.receiveEvents = value;
    }

    /**
     * Gets the value of the dispatchEvents property.
     * 
     */
    public boolean isDispatchEvents() {
        return dispatchEvents;
    }

    /**
     * Sets the value of the dispatchEvents property.
     * 
     */
    public void setDispatchEvents(boolean value) {
        this.dispatchEvents = value;
    }

    /**
     * Gets the value of the requestEvents property.
     * 
     */
    public boolean isRequestEvents() {
        return requestEvents;
    }

    /**
     * Sets the value of the requestEvents property.
     * 
     */
    public void setRequestEvents(boolean value) {
        this.requestEvents = value;
    }

    /**
     * Gets the value of the transactions property.
     * 
     */
    public boolean isTransactions() {
        return transactions;
    }

    /**
     * Sets the value of the transactions property.
     * 
     */
    public void setTransactions(boolean value) {
        this.transactions = value;
    }

    /**
     * Gets the value of the transactionTypes property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransactionTypes() {
        return transactionTypes;
    }

    /**
     * Sets the value of the transactionTypes property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransactionTypes(String value) {
        this.transactionTypes = value;
    }

    /**
     * Gets the value of the streaming property.
     * 
     */
    public boolean isStreaming() {
        return streaming;
    }

    /**
     * Sets the value of the streaming property.
     * 
     */
    public void setStreaming(boolean value) {
        this.streaming = value;
    }

    /**
     * Gets the value of the retries property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isRetries() {
        return retries;
    }

    /**
     * Sets the value of the retries property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRetries(Boolean value) {
        this.retries = value;
    }

}
