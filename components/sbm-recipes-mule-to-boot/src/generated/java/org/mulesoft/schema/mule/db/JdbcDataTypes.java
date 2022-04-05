
package org.mulesoft.schema.mule.db;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for JdbcDataTypes.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="JdbcDataTypes"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN"&gt;
 *     &lt;enumeration value="BIT"/&gt;
 *     &lt;enumeration value="TINYINT"/&gt;
 *     &lt;enumeration value="SMALLINT"/&gt;
 *     &lt;enumeration value="INTEGER"/&gt;
 *     &lt;enumeration value="BIGINT"/&gt;
 *     &lt;enumeration value="FLOAT"/&gt;
 *     &lt;enumeration value="REAL"/&gt;
 *     &lt;enumeration value="DOUBLE"/&gt;
 *     &lt;enumeration value="NUMERIC"/&gt;
 *     &lt;enumeration value="DECIMAL"/&gt;
 *     &lt;enumeration value="CHAR"/&gt;
 *     &lt;enumeration value="VARCHAR"/&gt;
 *     &lt;enumeration value="LONGVARCHAR"/&gt;
 *     &lt;enumeration value="DATE"/&gt;
 *     &lt;enumeration value="TIME"/&gt;
 *     &lt;enumeration value="TIMESTAMP"/&gt;
 *     &lt;enumeration value="BINARY"/&gt;
 *     &lt;enumeration value="VARBINARY"/&gt;
 *     &lt;enumeration value="LONGVARBINARY"/&gt;
 *     &lt;enumeration value="NULL"/&gt;
 *     &lt;enumeration value="OTHER"/&gt;
 *     &lt;enumeration value="JAVA_OBJECT"/&gt;
 *     &lt;enumeration value="DISTINCT"/&gt;
 *     &lt;enumeration value="STRUCT"/&gt;
 *     &lt;enumeration value="ARRAY"/&gt;
 *     &lt;enumeration value="BLOB"/&gt;
 *     &lt;enumeration value="CLOB"/&gt;
 *     &lt;enumeration value="REF"/&gt;
 *     &lt;enumeration value="DATALINK"/&gt;
 *     &lt;enumeration value="BOOLEAN"/&gt;
 *     &lt;enumeration value="ROWID"/&gt;
 *     &lt;enumeration value="NCHAR"/&gt;
 *     &lt;enumeration value="NVARCHAR"/&gt;
 *     &lt;enumeration value="LONGNVARCHAR"/&gt;
 *     &lt;enumeration value="NCLOB"/&gt;
 *     &lt;enumeration value="SQLXML"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "JdbcDataTypes")
@XmlEnum
public enum JdbcDataTypes {

    BIT,
    TINYINT,
    SMALLINT,
    INTEGER,
    BIGINT,
    FLOAT,
    REAL,
    DOUBLE,
    NUMERIC,
    DECIMAL,
    CHAR,
    VARCHAR,
    LONGVARCHAR,
    DATE,
    TIME,
    TIMESTAMP,
    BINARY,
    VARBINARY,
    LONGVARBINARY,
    NULL,
    OTHER,
    JAVA_OBJECT,
    DISTINCT,
    STRUCT,
    ARRAY,
    BLOB,
    CLOB,
    REF,
    DATALINK,
    BOOLEAN,
    ROWID,
    NCHAR,
    NVARCHAR,
    LONGNVARCHAR,
    NCLOB,
    SQLXML;

    public String value() {
        return name();
    }

    public static JdbcDataTypes fromValue(String v) {
        return valueOf(v);
    }

}
