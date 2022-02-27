
package org.mulesoft.schema.mule.core;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for transactionalActionType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="transactionalActionType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN"&gt;
 *     &lt;enumeration value="ALWAYS_BEGIN"/&gt;
 *     &lt;enumeration value="BEGIN_OR_JOIN"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "transactionalActionType")
@XmlEnum
public enum TransactionalActionType {

    ALWAYS_BEGIN,
    BEGIN_OR_JOIN;

    public String value() {
        return name();
    }

    public static TransactionalActionType fromValue(String v) {
        return valueOf(v);
    }

}
