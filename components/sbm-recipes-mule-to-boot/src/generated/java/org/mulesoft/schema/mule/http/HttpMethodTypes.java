
package org.mulesoft.schema.mule.http;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for httpMethodTypes.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="httpMethodTypes"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN"&gt;
 *     &lt;enumeration value="OPTIONS"/&gt;
 *     &lt;enumeration value="GET"/&gt;
 *     &lt;enumeration value="HEAD"/&gt;
 *     &lt;enumeration value="PATCH"/&gt;
 *     &lt;enumeration value="POST"/&gt;
 *     &lt;enumeration value="PUT"/&gt;
 *     &lt;enumeration value="TRACE"/&gt;
 *     &lt;enumeration value="CONNECT"/&gt;
 *     &lt;enumeration value="DELETE"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "httpMethodTypes")
@XmlEnum
public enum HttpMethodTypes {

    OPTIONS,
    GET,
    HEAD,
    PATCH,
    POST,
    PUT,
    TRACE,
    CONNECT,
    DELETE;

    public String value() {
        return name();
    }

    public static HttpMethodTypes fromValue(String v) {
        return valueOf(v);
    }

}
