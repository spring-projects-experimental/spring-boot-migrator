
package org.mulesoft.schema.mule.core;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for notificationTypes.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="notificationTypes"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN"&gt;
 *     &lt;enumeration value="CONTEXT"/&gt;
 *     &lt;enumeration value="MODEL"/&gt;
 *     &lt;enumeration value="SERVICE"/&gt;
 *     &lt;enumeration value="SECURITY"/&gt;
 *     &lt;enumeration value="ASYNC-MESSAGE"/&gt;
 *     &lt;enumeration value="ENDPOINT-MESSAGE"/&gt;
 *     &lt;enumeration value="CONNECTOR-MESSAGE"/&gt;
 *     &lt;enumeration value="COMPONENT-MESSAGE"/&gt;
 *     &lt;enumeration value="PIPELINE-MESSAGE"/&gt;
 *     &lt;enumeration value="MANAGEMENT"/&gt;
 *     &lt;enumeration value="MESSAGE-PROCESSOR"/&gt;
 *     &lt;enumeration value="EXCEPTION-STRATEGY"/&gt;
 *     &lt;enumeration value="CONNECTION"/&gt;
 *     &lt;enumeration value="REGISTRY"/&gt;
 *     &lt;enumeration value="CUSTOM"/&gt;
 *     &lt;enumeration value="EXCEPTION"/&gt;
 *     &lt;enumeration value="TRANSACTION"/&gt;
 *     &lt;enumeration value="ROUTING"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "notificationTypes")
@XmlEnum
public enum NotificationTypes {

    CONTEXT("CONTEXT"),

    /**
     * 
     *                         DEPRECATED: The use of 'model' and 'service' is deprecated in Mule 3.4 and will be removed in Mule 4.0. As such this
     *                         notification is also deprecated and will be removed in Mule 4.0.
     *                     
     * 
     */
    MODEL("MODEL"),

    /**
     * 
     *                         DEPRECATED: The use of 'model' and 'service' is deprecated in Mule 3.4 and will be removed in Mule 4.0. As such this
     *                         notification is also deprecated and will be removed in Mule 4.0.
     *                     
     * 
     */
    SERVICE("SERVICE"),
    SECURITY("SECURITY"),
    @XmlEnumValue("ASYNC-MESSAGE")
    ASYNC_MESSAGE("ASYNC-MESSAGE"),
    @XmlEnumValue("ENDPOINT-MESSAGE")
    ENDPOINT_MESSAGE("ENDPOINT-MESSAGE"),
    @XmlEnumValue("CONNECTOR-MESSAGE")
    CONNECTOR_MESSAGE("CONNECTOR-MESSAGE"),
    @XmlEnumValue("COMPONENT-MESSAGE")
    COMPONENT_MESSAGE("COMPONENT-MESSAGE"),
    @XmlEnumValue("PIPELINE-MESSAGE")
    PIPELINE_MESSAGE("PIPELINE-MESSAGE"),
    MANAGEMENT("MANAGEMENT"),
    @XmlEnumValue("MESSAGE-PROCESSOR")
    MESSAGE_PROCESSOR("MESSAGE-PROCESSOR"),
    @XmlEnumValue("EXCEPTION-STRATEGY")
    EXCEPTION_STRATEGY("EXCEPTION-STRATEGY"),
    CONNECTION("CONNECTION"),
    REGISTRY("REGISTRY"),
    CUSTOM("CUSTOM"),
    EXCEPTION("EXCEPTION"),
    TRANSACTION("TRANSACTION"),
    ROUTING("ROUTING");
    private final String value;

    NotificationTypes(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static NotificationTypes fromValue(String v) {
        for (NotificationTypes c: NotificationTypes.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
