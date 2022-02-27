
package org.mulesoft.schema.mule.core;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for timeUnitType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="timeUnitType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN"&gt;
 *     &lt;enumeration value="MILLISECONDS"/&gt;
 *     &lt;enumeration value="SECONDS"/&gt;
 *     &lt;enumeration value="MINUTES"/&gt;
 *     &lt;enumeration value="DAYS"/&gt;
 *     &lt;enumeration value="HOURS"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "timeUnitType")
@XmlEnum
public enum TimeUnitType {

    MILLISECONDS,
    SECONDS,
    MINUTES,
    DAYS,
    HOURS;

    public String value() {
        return name();
    }

    public static TimeUnitType fromValue(String v) {
        return valueOf(v);
    }

}
