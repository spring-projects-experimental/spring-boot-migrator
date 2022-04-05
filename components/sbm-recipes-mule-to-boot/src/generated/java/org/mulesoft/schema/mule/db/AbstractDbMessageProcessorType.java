
package org.mulesoft.schema.mule.db;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import org.mulesoft.schema.mule.core.AbstractMessageProcessorType;


/**
 * <p>Java class for abstractDbMessageProcessorType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="abstractDbMessageProcessorType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}abstractMessageProcessorType"&gt;
 *       &lt;attGroup ref="{http://www.mulesoft.org/schema/mule/db}dbMessageProcessorAttributes"/&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "abstractDbMessageProcessorType")
@XmlSeeAlso({
    ExecuteDdlMessageProcessorType.class,
    ExecuteStoredProcedureMessageProcessorType.class,
    AdvancedDbMessageProcessorType.class
})
public class AbstractDbMessageProcessorType
    extends AbstractMessageProcessorType
{

    @XmlAttribute(name = "config-ref")
    protected String configRef;
    @XmlAttribute(name = "source")
    protected String source;
    @XmlAttribute(name = "target")
    protected String target;
    @XmlAttribute(name = "transactionalAction")
    protected TransactionalActionType transactionalAction;
    @XmlAttribute(name = "queryTimeout")
    protected BigInteger queryTimeout;

    /**
     * Gets the value of the configRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConfigRef() {
        return configRef;
    }

    /**
     * Sets the value of the configRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConfigRef(String value) {
        this.configRef = value;
    }

    /**
     * Gets the value of the source property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSource() {
        return source;
    }

    /**
     * Sets the value of the source property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSource(String value) {
        this.source = value;
    }

    /**
     * Gets the value of the target property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTarget() {
        if (target == null) {
            return "#[payload]";
        } else {
            return target;
        }
    }

    /**
     * Sets the value of the target property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTarget(String value) {
        this.target = value;
    }

    /**
     * Gets the value of the transactionalAction property.
     * 
     * @return
     *     possible object is
     *     {@link TransactionalActionType }
     *     
     */
    public TransactionalActionType getTransactionalAction() {
        if (transactionalAction == null) {
            return TransactionalActionType.JOIN_IF_POSSIBLE;
        } else {
            return transactionalAction;
        }
    }

    /**
     * Sets the value of the transactionalAction property.
     * 
     * @param value
     *     allowed object is
     *     {@link TransactionalActionType }
     *     
     */
    public void setTransactionalAction(TransactionalActionType value) {
        this.transactionalAction = value;
    }

    /**
     * Gets the value of the queryTimeout property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getQueryTimeout() {
        if (queryTimeout == null) {
            return new BigInteger("0");
        } else {
            return queryTimeout;
        }
    }

    /**
     * Sets the value of the queryTimeout property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setQueryTimeout(BigInteger value) {
        this.queryTimeout = value;
    }

}
