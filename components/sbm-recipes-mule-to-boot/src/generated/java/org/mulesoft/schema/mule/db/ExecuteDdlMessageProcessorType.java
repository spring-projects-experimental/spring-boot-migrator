
package org.mulesoft.schema.mule.db;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for executeDdlMessageProcessorType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="executeDdlMessageProcessorType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/db}abstractDbMessageProcessorType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="dynamic-query" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
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
@XmlType(name = "executeDdlMessageProcessorType", propOrder = {
    "dynamicQuery"
})
public class ExecuteDdlMessageProcessorType
    extends AbstractDbMessageProcessorType
{

    @XmlElement(name = "dynamic-query", required = true)
    protected String dynamicQuery;

    /**
     * Gets the value of the dynamicQuery property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDynamicQuery() {
        return dynamicQuery;
    }

    /**
     * Sets the value of the dynamicQuery property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDynamicQuery(String value) {
        this.dynamicQuery = value;
    }

}
