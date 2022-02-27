
package org.mulesoft.schema.mule.core;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for abstractNotificationExceptionStrategyType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="abstractNotificationExceptionStrategyType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}exceptionStrategyType"&gt;
 *       &lt;attribute name="enableNotifications" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" /&gt;
 *       &lt;attribute name="name" type="{http://www.mulesoft.org/schema/mule/core}substitutableName" /&gt;
 *       &lt;attribute name="when" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="logException" type="{http://www.mulesoft.org/schema/mule/core}expressionBoolean" default="true" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "abstractNotificationExceptionStrategyType")
@XmlSeeAlso({
    CatchExceptionStrategyType.class,
    RollbackExceptionStrategyType.class,
    AbstractExceptionStrategyType.class
})
public class AbstractNotificationExceptionStrategyType
    extends ExceptionStrategyType
{

    @XmlAttribute(name = "enableNotifications")
    protected Boolean enableNotifications;
    @XmlAttribute(name = "name")
    protected String name;
    @XmlAttribute(name = "when")
    protected String when;
    @XmlAttribute(name = "logException")
    protected List<String> logException;

    /**
     * Gets the value of the enableNotifications property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isEnableNotifications() {
        if (enableNotifications == null) {
            return true;
        } else {
            return enableNotifications;
        }
    }

    /**
     * Sets the value of the enableNotifications property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setEnableNotifications(Boolean value) {
        this.enableNotifications = value;
    }

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
     * Gets the value of the when property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWhen() {
        return when;
    }

    /**
     * Sets the value of the when property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWhen(String value) {
        this.when = value;
    }

    /**
     * Gets the value of the logException property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the logException property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLogException().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getLogException() {
        if (logException == null) {
            logException = new ArrayList<String>();
        }
        return this.logException;
    }

}
