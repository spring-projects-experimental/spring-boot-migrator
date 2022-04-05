
package org.mulesoft.schema.mule.db;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for selectMessageProcessorType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="selectMessageProcessorType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/db}advancedDbMessageProcessorType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.mulesoft.org/schema/mule/db}abstractQueryResultSetHandler" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="streaming" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *       &lt;attribute name="fetchSize" type="{http://www.mulesoft.org/schema/mule/core}substitutableInt" /&gt;
 *       &lt;attribute name="maxRows" type="{http://www.mulesoft.org/schema/mule/core}substitutableInt" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "selectMessageProcessorType", propOrder = {
    "abstractQueryResultSetHandler"
})
public class SelectMessageProcessorType
    extends AdvancedDbMessageProcessorType
{

    protected AbstractQueryResultSetHandlerType abstractQueryResultSetHandler;
    @XmlAttribute(name = "streaming")
    protected Boolean streaming;
    @XmlAttribute(name = "fetchSize")
    protected String fetchSize;
    @XmlAttribute(name = "maxRows")
    protected String maxRows;

    /**
     * Gets the value of the abstractQueryResultSetHandler property.
     * 
     * @return
     *     possible object is
     *     {@link AbstractQueryResultSetHandlerType }
     *     
     */
    public AbstractQueryResultSetHandlerType getAbstractQueryResultSetHandler() {
        return abstractQueryResultSetHandler;
    }

    /**
     * Sets the value of the abstractQueryResultSetHandler property.
     * 
     * @param value
     *     allowed object is
     *     {@link AbstractQueryResultSetHandlerType }
     *     
     */
    public void setAbstractQueryResultSetHandler(AbstractQueryResultSetHandlerType value) {
        this.abstractQueryResultSetHandler = value;
    }

    /**
     * Gets the value of the streaming property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isStreaming() {
        if (streaming == null) {
            return false;
        } else {
            return streaming;
        }
    }

    /**
     * Sets the value of the streaming property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setStreaming(Boolean value) {
        this.streaming = value;
    }

    /**
     * Gets the value of the fetchSize property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFetchSize() {
        return fetchSize;
    }

    /**
     * Sets the value of the fetchSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFetchSize(String value) {
        this.fetchSize = value;
    }

    /**
     * Gets the value of the maxRows property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaxRows() {
        return maxRows;
    }

    /**
     * Sets the value of the maxRows property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaxRows(String value) {
        this.maxRows = value;
    }

}
