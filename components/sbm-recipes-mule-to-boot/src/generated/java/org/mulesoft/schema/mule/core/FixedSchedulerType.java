
package org.mulesoft.schema.mule.core;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for fixedSchedulerType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="fixedSchedulerType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}abstractSchedulerType"&gt;
 *       &lt;attribute name="frequency" type="{http://www.mulesoft.org/schema/mule/core}substitutableLong" default="1000" /&gt;
 *       &lt;attribute name="timeUnit" type="{http://www.mulesoft.org/schema/mule/core}timeUnitType" default="MILLISECONDS" /&gt;
 *       &lt;attribute name="startDelay" type="{http://www.mulesoft.org/schema/mule/core}substitutableLong" default="0" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "fixedSchedulerType")
public class FixedSchedulerType
    extends AbstractSchedulerType
{

    @XmlAttribute(name = "frequency")
    protected String frequency;
    @XmlAttribute(name = "timeUnit")
    protected TimeUnitType timeUnit;
    @XmlAttribute(name = "startDelay")
    protected String startDelay;

    /**
     * Gets the value of the frequency property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFrequency() {
        if (frequency == null) {
            return "1000";
        } else {
            return frequency;
        }
    }

    /**
     * Sets the value of the frequency property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFrequency(String value) {
        this.frequency = value;
    }

    /**
     * Gets the value of the timeUnit property.
     * 
     * @return
     *     possible object is
     *     {@link TimeUnitType }
     *     
     */
    public TimeUnitType getTimeUnit() {
        if (timeUnit == null) {
            return TimeUnitType.MILLISECONDS;
        } else {
            return timeUnit;
        }
    }

    /**
     * Sets the value of the timeUnit property.
     * 
     * @param value
     *     allowed object is
     *     {@link TimeUnitType }
     *     
     */
    public void setTimeUnit(TimeUnitType value) {
        this.timeUnit = value;
    }

    /**
     * Gets the value of the startDelay property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStartDelay() {
        if (startDelay == null) {
            return "0";
        } else {
            return startDelay;
        }
    }

    /**
     * Sets the value of the startDelay property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStartDelay(String value) {
        this.startDelay = value;
    }

}
