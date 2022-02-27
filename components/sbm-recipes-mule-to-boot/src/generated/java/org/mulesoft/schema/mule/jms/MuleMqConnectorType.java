
package org.mulesoft.schema.mule.jms;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for muleMqConnectorType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="muleMqConnectorType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/jms}vendorJmsConnectorType"&gt;
 *       &lt;attribute name="brokerURL" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="bufferOutput" type="{http://www.w3.org/2001/XMLSchema}string" default="queued" /&gt;
 *       &lt;attribute name="syncWrites" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" default="false" /&gt;
 *       &lt;attribute name="syncBatchSize" type="{http://www.mulesoft.org/schema/mule/core}substitutableInt" default="50" /&gt;
 *       &lt;attribute name="syncTime" type="{http://www.mulesoft.org/schema/mule/core}substitutableInt" default="20" /&gt;
 *       &lt;attribute name="globalStoreCapacity" type="{http://www.mulesoft.org/schema/mule/core}substitutableInt" default="5000" /&gt;
 *       &lt;attribute name="maxUnackedSize" type="{http://www.mulesoft.org/schema/mule/core}substitutableInt" default="100" /&gt;
 *       &lt;attribute name="useJMSEngine" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" default="true" /&gt;
 *       &lt;attribute name="queueWindowSize" type="{http://www.mulesoft.org/schema/mule/core}substitutableInt" default="100" /&gt;
 *       &lt;attribute name="autoAckCount" type="{http://www.mulesoft.org/schema/mule/core}substitutableInt" default="50" /&gt;
 *       &lt;attribute name="enableSharedDurable" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" default="false" /&gt;
 *       &lt;attribute name="randomiseRNames" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" default="true" /&gt;
 *       &lt;attribute name="messageThreadPoolSize" type="{http://www.mulesoft.org/schema/mule/core}substitutableInt" default="30" /&gt;
 *       &lt;attribute name="discOnClusterFailure" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" default="true" /&gt;
 *       &lt;attribute name="initialRetryCount" type="{http://www.mulesoft.org/schema/mule/core}substitutableInt" default="2" /&gt;
 *       &lt;attribute name="muleMqMaxRedelivery" type="{http://www.mulesoft.org/schema/mule/core}substitutableInt" default="100" /&gt;
 *       &lt;attribute name="retryCommit" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" default="false" /&gt;
 *       &lt;attribute name="enableMultiplexedConnections" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" default="false" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "muleMqConnectorType")
public class MuleMqConnectorType
    extends VendorJmsConnectorType
{

    @XmlAttribute(name = "brokerURL")
    protected String brokerURL;
    @XmlAttribute(name = "bufferOutput")
    protected String bufferOutput;
    @XmlAttribute(name = "syncWrites")
    protected String syncWrites;
    @XmlAttribute(name = "syncBatchSize")
    protected String syncBatchSize;
    @XmlAttribute(name = "syncTime")
    protected String syncTime;
    @XmlAttribute(name = "globalStoreCapacity")
    protected String globalStoreCapacity;
    @XmlAttribute(name = "maxUnackedSize")
    protected String maxUnackedSize;
    @XmlAttribute(name = "useJMSEngine")
    protected String useJMSEngine;
    @XmlAttribute(name = "queueWindowSize")
    protected String queueWindowSize;
    @XmlAttribute(name = "autoAckCount")
    protected String autoAckCount;
    @XmlAttribute(name = "enableSharedDurable")
    protected String enableSharedDurable;
    @XmlAttribute(name = "randomiseRNames")
    protected String randomiseRNames;
    @XmlAttribute(name = "messageThreadPoolSize")
    protected String messageThreadPoolSize;
    @XmlAttribute(name = "discOnClusterFailure")
    protected String discOnClusterFailure;
    @XmlAttribute(name = "initialRetryCount")
    protected String initialRetryCount;
    @XmlAttribute(name = "muleMqMaxRedelivery")
    protected String muleMqMaxRedelivery;
    @XmlAttribute(name = "retryCommit")
    protected String retryCommit;
    @XmlAttribute(name = "enableMultiplexedConnections")
    protected String enableMultiplexedConnections;

    /**
     * Gets the value of the brokerURL property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBrokerURL() {
        return brokerURL;
    }

    /**
     * Sets the value of the brokerURL property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBrokerURL(String value) {
        this.brokerURL = value;
    }

    /**
     * Gets the value of the bufferOutput property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBufferOutput() {
        if (bufferOutput == null) {
            return "queued";
        } else {
            return bufferOutput;
        }
    }

    /**
     * Sets the value of the bufferOutput property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBufferOutput(String value) {
        this.bufferOutput = value;
    }

    /**
     * Gets the value of the syncWrites property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSyncWrites() {
        if (syncWrites == null) {
            return "false";
        } else {
            return syncWrites;
        }
    }

    /**
     * Sets the value of the syncWrites property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSyncWrites(String value) {
        this.syncWrites = value;
    }

    /**
     * Gets the value of the syncBatchSize property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSyncBatchSize() {
        if (syncBatchSize == null) {
            return "50";
        } else {
            return syncBatchSize;
        }
    }

    /**
     * Sets the value of the syncBatchSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSyncBatchSize(String value) {
        this.syncBatchSize = value;
    }

    /**
     * Gets the value of the syncTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSyncTime() {
        if (syncTime == null) {
            return "20";
        } else {
            return syncTime;
        }
    }

    /**
     * Sets the value of the syncTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSyncTime(String value) {
        this.syncTime = value;
    }

    /**
     * Gets the value of the globalStoreCapacity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGlobalStoreCapacity() {
        if (globalStoreCapacity == null) {
            return "5000";
        } else {
            return globalStoreCapacity;
        }
    }

    /**
     * Sets the value of the globalStoreCapacity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGlobalStoreCapacity(String value) {
        this.globalStoreCapacity = value;
    }

    /**
     * Gets the value of the maxUnackedSize property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaxUnackedSize() {
        if (maxUnackedSize == null) {
            return "100";
        } else {
            return maxUnackedSize;
        }
    }

    /**
     * Sets the value of the maxUnackedSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaxUnackedSize(String value) {
        this.maxUnackedSize = value;
    }

    /**
     * Gets the value of the useJMSEngine property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUseJMSEngine() {
        if (useJMSEngine == null) {
            return "true";
        } else {
            return useJMSEngine;
        }
    }

    /**
     * Sets the value of the useJMSEngine property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUseJMSEngine(String value) {
        this.useJMSEngine = value;
    }

    /**
     * Gets the value of the queueWindowSize property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQueueWindowSize() {
        if (queueWindowSize == null) {
            return "100";
        } else {
            return queueWindowSize;
        }
    }

    /**
     * Sets the value of the queueWindowSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQueueWindowSize(String value) {
        this.queueWindowSize = value;
    }

    /**
     * Gets the value of the autoAckCount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAutoAckCount() {
        if (autoAckCount == null) {
            return "50";
        } else {
            return autoAckCount;
        }
    }

    /**
     * Sets the value of the autoAckCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAutoAckCount(String value) {
        this.autoAckCount = value;
    }

    /**
     * Gets the value of the enableSharedDurable property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnableSharedDurable() {
        if (enableSharedDurable == null) {
            return "false";
        } else {
            return enableSharedDurable;
        }
    }

    /**
     * Sets the value of the enableSharedDurable property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnableSharedDurable(String value) {
        this.enableSharedDurable = value;
    }

    /**
     * Gets the value of the randomiseRNames property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRandomiseRNames() {
        if (randomiseRNames == null) {
            return "true";
        } else {
            return randomiseRNames;
        }
    }

    /**
     * Sets the value of the randomiseRNames property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRandomiseRNames(String value) {
        this.randomiseRNames = value;
    }

    /**
     * Gets the value of the messageThreadPoolSize property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessageThreadPoolSize() {
        if (messageThreadPoolSize == null) {
            return "30";
        } else {
            return messageThreadPoolSize;
        }
    }

    /**
     * Sets the value of the messageThreadPoolSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessageThreadPoolSize(String value) {
        this.messageThreadPoolSize = value;
    }

    /**
     * Gets the value of the discOnClusterFailure property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDiscOnClusterFailure() {
        if (discOnClusterFailure == null) {
            return "true";
        } else {
            return discOnClusterFailure;
        }
    }

    /**
     * Sets the value of the discOnClusterFailure property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDiscOnClusterFailure(String value) {
        this.discOnClusterFailure = value;
    }

    /**
     * Gets the value of the initialRetryCount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInitialRetryCount() {
        if (initialRetryCount == null) {
            return "2";
        } else {
            return initialRetryCount;
        }
    }

    /**
     * Sets the value of the initialRetryCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInitialRetryCount(String value) {
        this.initialRetryCount = value;
    }

    /**
     * Gets the value of the muleMqMaxRedelivery property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMuleMqMaxRedelivery() {
        if (muleMqMaxRedelivery == null) {
            return "100";
        } else {
            return muleMqMaxRedelivery;
        }
    }

    /**
     * Sets the value of the muleMqMaxRedelivery property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMuleMqMaxRedelivery(String value) {
        this.muleMqMaxRedelivery = value;
    }

    /**
     * Gets the value of the retryCommit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRetryCommit() {
        if (retryCommit == null) {
            return "false";
        } else {
            return retryCommit;
        }
    }

    /**
     * Sets the value of the retryCommit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRetryCommit(String value) {
        this.retryCommit = value;
    }

    /**
     * Gets the value of the enableMultiplexedConnections property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnableMultiplexedConnections() {
        if (enableMultiplexedConnections == null) {
            return "false";
        } else {
            return enableMultiplexedConnections;
        }
    }

    /**
     * Sets the value of the enableMultiplexedConnections property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnableMultiplexedConnections(String value) {
        this.enableMultiplexedConnections = value;
    }

}
