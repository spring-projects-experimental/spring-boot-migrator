
package org.mulesoft.schema.mule.db;

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
 *     &lt;enumeration value="ALWAYS_JOIN"/&gt;
 *     &lt;enumeration value="JOIN_IF_POSSIBLE"/&gt;
 *     &lt;enumeration value="NOT_SUPPORTED"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "transactionalActionType")
@XmlEnum
public enum TransactionalActionType {

    ALWAYS_JOIN,
    JOIN_IF_POSSIBLE,
    NOT_SUPPORTED;

    public String value() {
        return name();
    }

    public static TransactionalActionType fromValue(String v) {
        return valueOf(v);
    }

}
