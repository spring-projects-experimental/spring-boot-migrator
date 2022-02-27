
package org.mulesoft.schema.mule.ee.wmq;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import org.mulesoft.schema.mule.jms.VendorJmsConnectorType;


/**
 * <p>Java class for wmqConnectorType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="wmqConnectorType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/jms}vendorJmsConnectorType"&gt;
 *       &lt;attribute name="queueManager" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="hostName" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="port" type="{http://www.mulesoft.org/schema/mule/core}substitutablePortNumber" /&gt;
 *       &lt;attribute name="temporaryModel" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="ccsId" type="{http://www.mulesoft.org/schema/mule/core}substitutableInt" /&gt;
 *       &lt;attribute name="transportType"&gt;
 *         &lt;simpleType&gt;
 *           &lt;union&gt;
 *             &lt;simpleType&gt;
 *               &lt;restriction base="{http://www.mulesoft.org/schema/mule/core}propertyPlaceholderType"&gt;
 *               &lt;/restriction&gt;
 *             &lt;/simpleType&gt;
 *             &lt;simpleType&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN"&gt;
 *                 &lt;enumeration value="BINDINGS_MQ"/&gt;
 *                 &lt;enumeration value="CLIENT_MQ_TCPIP"/&gt;
 *                 &lt;enumeration value="DIRECT_HTTP"/&gt;
 *                 &lt;enumeration value="DIRECT_TCPIP"/&gt;
 *                 &lt;enumeration value="MQJD"/&gt;
 *               &lt;/restriction&gt;
 *             &lt;/simpleType&gt;
 *           &lt;/union&gt;
 *         &lt;/simpleType&gt;
 *       &lt;/attribute&gt;
 *       &lt;attribute name="channel" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="propagateMQEvents" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" /&gt;
 *       &lt;attribute name="useRemoteQueueDefinitions" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" /&gt;
 *       &lt;attribute name="receiveExitHandler" type="{http://www.mulesoft.org/schema/mule/core}substitutableClass" /&gt;
 *       &lt;attribute name="receiveExitHandlerInit" type="{http://www.mulesoft.org/schema/mule/core}substitutableClass" /&gt;
 *       &lt;attribute name="sendExitHandler" type="{http://www.mulesoft.org/schema/mule/core}substitutableClass" /&gt;
 *       &lt;attribute name="sendExitHandlerInit" type="{http://www.mulesoft.org/schema/mule/core}substitutableClass" /&gt;
 *       &lt;attribute name="securityExitHandler" type="{http://www.mulesoft.org/schema/mule/core}substitutableClass" /&gt;
 *       &lt;attribute name="securityExitHandlerInit" type="{http://www.mulesoft.org/schema/mule/core}substitutableClass" /&gt;
 *       &lt;attribute name="targetClient"&gt;
 *         &lt;simpleType&gt;
 *           &lt;union&gt;
 *             &lt;simpleType&gt;
 *               &lt;restriction base="{http://www.mulesoft.org/schema/mule/core}propertyPlaceholderType"&gt;
 *               &lt;/restriction&gt;
 *             &lt;/simpleType&gt;
 *             &lt;simpleType&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN"&gt;
 *                 &lt;enumeration value="NONJMS_MQ"/&gt;
 *                 &lt;enumeration value="JMS_COMPLIANT"/&gt;
 *               &lt;/restriction&gt;
 *             &lt;/simpleType&gt;
 *           &lt;/union&gt;
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
@XmlType(name = "wmqConnectorType")
public class WmqConnectorType
    extends VendorJmsConnectorType
{

    @XmlAttribute(name = "queueManager")
    protected String queueManager;
    @XmlAttribute(name = "hostName")
    protected String hostName;
    @XmlAttribute(name = "port")
    protected String port;
    @XmlAttribute(name = "temporaryModel")
    protected String temporaryModel;
    @XmlAttribute(name = "ccsId")
    protected String ccsId;
    @XmlAttribute(name = "transportType")
    protected String transportType;
    @XmlAttribute(name = "channel")
    protected String channel;
    @XmlAttribute(name = "propagateMQEvents")
    protected String propagateMQEvents;
    @XmlAttribute(name = "useRemoteQueueDefinitions")
    protected String useRemoteQueueDefinitions;
    @XmlAttribute(name = "receiveExitHandler")
    protected String receiveExitHandler;
    @XmlAttribute(name = "receiveExitHandlerInit")
    protected String receiveExitHandlerInit;
    @XmlAttribute(name = "sendExitHandler")
    protected String sendExitHandler;
    @XmlAttribute(name = "sendExitHandlerInit")
    protected String sendExitHandlerInit;
    @XmlAttribute(name = "securityExitHandler")
    protected String securityExitHandler;
    @XmlAttribute(name = "securityExitHandlerInit")
    protected String securityExitHandlerInit;
    @XmlAttribute(name = "targetClient")
    protected String targetClient;

    /**
     * Gets the value of the queueManager property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQueueManager() {
        return queueManager;
    }

    /**
     * Sets the value of the queueManager property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQueueManager(String value) {
        this.queueManager = value;
    }

    /**
     * Gets the value of the hostName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHostName() {
        return hostName;
    }

    /**
     * Sets the value of the hostName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHostName(String value) {
        this.hostName = value;
    }

    /**
     * Gets the value of the port property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPort() {
        return port;
    }

    /**
     * Sets the value of the port property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPort(String value) {
        this.port = value;
    }

    /**
     * Gets the value of the temporaryModel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTemporaryModel() {
        return temporaryModel;
    }

    /**
     * Sets the value of the temporaryModel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTemporaryModel(String value) {
        this.temporaryModel = value;
    }

    /**
     * Gets the value of the ccsId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCcsId() {
        return ccsId;
    }

    /**
     * Sets the value of the ccsId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCcsId(String value) {
        this.ccsId = value;
    }

    /**
     * Gets the value of the transportType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransportType() {
        return transportType;
    }

    /**
     * Sets the value of the transportType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransportType(String value) {
        this.transportType = value;
    }

    /**
     * Gets the value of the channel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChannel() {
        return channel;
    }

    /**
     * Sets the value of the channel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChannel(String value) {
        this.channel = value;
    }

    /**
     * Gets the value of the propagateMQEvents property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPropagateMQEvents() {
        return propagateMQEvents;
    }

    /**
     * Sets the value of the propagateMQEvents property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPropagateMQEvents(String value) {
        this.propagateMQEvents = value;
    }

    /**
     * Gets the value of the useRemoteQueueDefinitions property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUseRemoteQueueDefinitions() {
        return useRemoteQueueDefinitions;
    }

    /**
     * Sets the value of the useRemoteQueueDefinitions property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUseRemoteQueueDefinitions(String value) {
        this.useRemoteQueueDefinitions = value;
    }

    /**
     * Gets the value of the receiveExitHandler property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReceiveExitHandler() {
        return receiveExitHandler;
    }

    /**
     * Sets the value of the receiveExitHandler property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReceiveExitHandler(String value) {
        this.receiveExitHandler = value;
    }

    /**
     * Gets the value of the receiveExitHandlerInit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReceiveExitHandlerInit() {
        return receiveExitHandlerInit;
    }

    /**
     * Sets the value of the receiveExitHandlerInit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReceiveExitHandlerInit(String value) {
        this.receiveExitHandlerInit = value;
    }

    /**
     * Gets the value of the sendExitHandler property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSendExitHandler() {
        return sendExitHandler;
    }

    /**
     * Sets the value of the sendExitHandler property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSendExitHandler(String value) {
        this.sendExitHandler = value;
    }

    /**
     * Gets the value of the sendExitHandlerInit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSendExitHandlerInit() {
        return sendExitHandlerInit;
    }

    /**
     * Sets the value of the sendExitHandlerInit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSendExitHandlerInit(String value) {
        this.sendExitHandlerInit = value;
    }

    /**
     * Gets the value of the securityExitHandler property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSecurityExitHandler() {
        return securityExitHandler;
    }

    /**
     * Sets the value of the securityExitHandler property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSecurityExitHandler(String value) {
        this.securityExitHandler = value;
    }

    /**
     * Gets the value of the securityExitHandlerInit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSecurityExitHandlerInit() {
        return securityExitHandlerInit;
    }

    /**
     * Sets the value of the securityExitHandlerInit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSecurityExitHandlerInit(String value) {
        this.securityExitHandlerInit = value;
    }

    /**
     * Gets the value of the targetClient property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTargetClient() {
        return targetClient;
    }

    /**
     * Sets the value of the targetClient property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTargetClient(String value) {
        this.targetClient = value;
    }

}
