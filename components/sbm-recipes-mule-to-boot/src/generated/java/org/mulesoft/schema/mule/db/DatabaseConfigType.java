
package org.mulesoft.schema.mule.db;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.mulesoft.schema.mule.core.AbstractExtensionType;
import org.mulesoft.schema.mule.core.AbstractReconnectionStrategyType;
import org.mulesoft.schema.mule.core.ReconnectCustomStrategyType;
import org.mulesoft.schema.mule.core.ReconnectForeverStrategyType;
import org.mulesoft.schema.mule.core.ReconnectSimpleStrategyType;


/**
 * <p>Java class for DatabaseConfigType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DatabaseConfigType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}abstractExtensionType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.mulesoft.org/schema/mule/db}pooling-profile" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.mulesoft.org/schema/mule/db}connection-properties" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.mulesoft.org/schema/mule/db}data-types" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.mulesoft.org/schema/mule/core}abstract-reconnection-strategy" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="name" use="required" type="{http://www.mulesoft.org/schema/mule/core}substitutableName" /&gt;
 *       &lt;attribute name="dataSource-ref" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="url" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="useXaTransactions" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *       &lt;attribute name="driverClassName" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="connectionTimeout" type="{http://www.w3.org/2001/XMLSchema}int" /&gt;
 *       &lt;attribute name="transactionIsolation"&gt;
 *         &lt;simpleType&gt;
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN"&gt;
 *             &lt;enumeration value="NONE"/&gt;
 *             &lt;enumeration value="READ_COMMITTED"/&gt;
 *             &lt;enumeration value="READ_UNCOMMITTED"/&gt;
 *             &lt;enumeration value="REPEATABLE_READ"/&gt;
 *             &lt;enumeration value="SERIALIZABLE"/&gt;
 *           &lt;/restriction&gt;
 *         &lt;/simpleType&gt;
 *       &lt;/attribute&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DatabaseConfigType", propOrder = {
    "poolingProfile",
    "connectionProperties",
    "dataTypes",
    "abstractReconnectionStrategy"
})
@XmlSeeAlso({
    AbstractUserAndPasswordDatabaseConfigType.class
})
public class DatabaseConfigType
    extends AbstractExtensionType
{

    @XmlElement(name = "pooling-profile")
    protected DatabasePoolingProfileType poolingProfile;
    @XmlElement(name = "connection-properties")
    protected ConnectionPropertiesType connectionProperties;
    @XmlElement(name = "data-types")
    protected CustomDataTypes dataTypes;
    @XmlElementRef(name = "abstract-reconnection-strategy", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    protected JAXBElement<? extends AbstractReconnectionStrategyType> abstractReconnectionStrategy;
    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "dataSource-ref")
    protected String dataSourceRef;
    @XmlAttribute(name = "url")
    protected String url;
    @XmlAttribute(name = "useXaTransactions")
    protected Boolean useXaTransactions;
    @XmlAttribute(name = "driverClassName")
    protected String driverClassName;
    @XmlAttribute(name = "connectionTimeout")
    protected Integer connectionTimeout;
    @XmlAttribute(name = "transactionIsolation")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String transactionIsolation;

    /**
     * Gets the value of the poolingProfile property.
     * 
     * @return
     *     possible object is
     *     {@link DatabasePoolingProfileType }
     *     
     */
    public DatabasePoolingProfileType getPoolingProfile() {
        return poolingProfile;
    }

    /**
     * Sets the value of the poolingProfile property.
     * 
     * @param value
     *     allowed object is
     *     {@link DatabasePoolingProfileType }
     *     
     */
    public void setPoolingProfile(DatabasePoolingProfileType value) {
        this.poolingProfile = value;
    }

    /**
     * Gets the value of the connectionProperties property.
     * 
     * @return
     *     possible object is
     *     {@link ConnectionPropertiesType }
     *     
     */
    public ConnectionPropertiesType getConnectionProperties() {
        return connectionProperties;
    }

    /**
     * Sets the value of the connectionProperties property.
     * 
     * @param value
     *     allowed object is
     *     {@link ConnectionPropertiesType }
     *     
     */
    public void setConnectionProperties(ConnectionPropertiesType value) {
        this.connectionProperties = value;
    }

    /**
     * Gets the value of the dataTypes property.
     * 
     * @return
     *     possible object is
     *     {@link CustomDataTypes }
     *     
     */
    public CustomDataTypes getDataTypes() {
        return dataTypes;
    }

    /**
     * Sets the value of the dataTypes property.
     * 
     * @param value
     *     allowed object is
     *     {@link CustomDataTypes }
     *     
     */
    public void setDataTypes(CustomDataTypes value) {
        this.dataTypes = value;
    }

    /**
     * Gets the value of the abstractReconnectionStrategy property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ReconnectForeverStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ReconnectSimpleStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ReconnectCustomStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractReconnectionStrategyType }{@code >}
     *     
     */
    public JAXBElement<? extends AbstractReconnectionStrategyType> getAbstractReconnectionStrategy() {
        return abstractReconnectionStrategy;
    }

    /**
     * Sets the value of the abstractReconnectionStrategy property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ReconnectForeverStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ReconnectSimpleStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ReconnectCustomStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractReconnectionStrategyType }{@code >}
     *     
     */
    public void setAbstractReconnectionStrategy(JAXBElement<? extends AbstractReconnectionStrategyType> value) {
        this.abstractReconnectionStrategy = value;
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
     * Gets the value of the dataSourceRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataSourceRef() {
        return dataSourceRef;
    }

    /**
     * Sets the value of the dataSourceRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataSourceRef(String value) {
        this.dataSourceRef = value;
    }

    /**
     * Gets the value of the url property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the value of the url property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUrl(String value) {
        this.url = value;
    }

    /**
     * Gets the value of the useXaTransactions property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isUseXaTransactions() {
        return useXaTransactions;
    }

    /**
     * Sets the value of the useXaTransactions property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setUseXaTransactions(Boolean value) {
        this.useXaTransactions = value;
    }

    /**
     * Gets the value of the driverClassName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDriverClassName() {
        return driverClassName;
    }

    /**
     * Sets the value of the driverClassName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDriverClassName(String value) {
        this.driverClassName = value;
    }

    /**
     * Gets the value of the connectionTimeout property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getConnectionTimeout() {
        return connectionTimeout;
    }

    /**
     * Sets the value of the connectionTimeout property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setConnectionTimeout(Integer value) {
        this.connectionTimeout = value;
    }

    /**
     * Gets the value of the transactionIsolation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransactionIsolation() {
        return transactionIsolation;
    }

    /**
     * Sets the value of the transactionIsolation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransactionIsolation(String value) {
        this.transactionIsolation = value;
    }

}
