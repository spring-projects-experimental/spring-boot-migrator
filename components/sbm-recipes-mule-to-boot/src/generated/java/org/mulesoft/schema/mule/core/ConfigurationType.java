
package org.mulesoft.schema.mule.core;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;
import org.mulesoft.schema.mule.http.HttpConfigType;


/**
 * <p>Java class for configurationType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="configurationType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}annotatedType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="default-threading-profile" type="{http://www.mulesoft.org/schema/mule/core}threadingProfileType" minOccurs="0"/&gt;
 *         &lt;element name="default-dispatcher-threading-profile" type="{http://www.mulesoft.org/schema/mule/core}threadingProfileType" minOccurs="0"/&gt;
 *         &lt;element name="default-receiver-threading-profile" type="{http://www.mulesoft.org/schema/mule/core}threadingProfileType" minOccurs="0"/&gt;
 *         &lt;element name="default-service-threading-profile" type="{http://www.mulesoft.org/schema/mule/core}threadingProfileType" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.mulesoft.org/schema/mule/core}abstract-reconnection-strategy" minOccurs="0"/&gt;
 *         &lt;element name="expression-language" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;extension base="{http://www.mulesoft.org/schema/mule/core}abstractMixedContentExtensionType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="import" maxOccurs="unbounded" minOccurs="0"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;attribute name="name" type="{http://www.mulesoft.org/schema/mule/core}nonBlankString" /&gt;
 *                           &lt;attribute name="class" use="required" type="{http://www.mulesoft.org/schema/mule/core}substitutableClass" /&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="alias" maxOccurs="unbounded" minOccurs="0"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;attribute name="name" use="required" type="{http://www.mulesoft.org/schema/mule/core}nonBlankString" /&gt;
 *                           &lt;attribute name="expression" use="required" type="{http://www.mulesoft.org/schema/mule/core}nonBlankString" /&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="global-functions" minOccurs="0"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;attribute name="file" type="{http://www.mulesoft.org/schema/mule/core}nonBlankString" /&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                 &lt;/sequence&gt;
 *                 &lt;attribute name="autoResolveVariables" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" default="true" /&gt;
 *                 &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *               &lt;/extension&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element ref="{http://www.mulesoft.org/schema/mule/core}abstract-configuration-extension" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="defaultResponseTimeout" type="{http://www.w3.org/2001/XMLSchema}string" default="10000" /&gt;
 *       &lt;attribute name="defaultTransactionTimeout" type="{http://www.w3.org/2001/XMLSchema}string" default="30000" /&gt;
 *       &lt;attribute name="defaultExceptionStrategy-ref" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="shutdownTimeout" type="{http://www.mulesoft.org/schema/mule/core}substitutableInt" default="5000" /&gt;
 *       &lt;attribute name="maxQueueTransactionFilesSize" type="{http://www.mulesoft.org/schema/mule/core}substitutableInt" default="500" /&gt;
 *       &lt;attribute name="useExtendedTransformations" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" default="true" /&gt;
 *       &lt;attribute name="flowEndingWithOneWayEndpointReturnsNull" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" default="false" /&gt;
 *       &lt;attribute name="enricherPropagatesSessionVariableChanges" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" default="false" /&gt;
 *       &lt;attribute name="defaultObjectSerializer-ref" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="defaultProcessingStrategy" type="{http://www.mulesoft.org/schema/mule/core}flowProcessingStrategyType" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "configurationType", propOrder = {
    "defaultThreadingProfile",
    "defaultDispatcherThreadingProfile",
    "defaultReceiverThreadingProfile",
    "defaultServiceThreadingProfile",
    "abstractReconnectionStrategy",
    "expressionLanguage",
    "abstractConfigurationExtension"
})
public class ConfigurationType
    extends AnnotatedType
{

    @XmlElement(name = "default-threading-profile")
    protected ThreadingProfileType defaultThreadingProfile;
    @XmlElement(name = "default-dispatcher-threading-profile")
    protected ThreadingProfileType defaultDispatcherThreadingProfile;
    @XmlElement(name = "default-receiver-threading-profile")
    protected ThreadingProfileType defaultReceiverThreadingProfile;
    @XmlElement(name = "default-service-threading-profile")
    protected ThreadingProfileType defaultServiceThreadingProfile;
    @XmlElementRef(name = "abstract-reconnection-strategy", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    protected JAXBElement<? extends AbstractReconnectionStrategyType> abstractReconnectionStrategy;
    @XmlElement(name = "expression-language")
    protected ConfigurationType.ExpressionLanguage expressionLanguage;
    @XmlElementRef(name = "abstract-configuration-extension", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    protected List<JAXBElement<? extends AbstractConfigurationExtensionType>> abstractConfigurationExtension;
    @XmlAttribute(name = "defaultResponseTimeout")
    protected String defaultResponseTimeout;
    @XmlAttribute(name = "defaultTransactionTimeout")
    protected String defaultTransactionTimeout;
    @XmlAttribute(name = "defaultExceptionStrategy-ref")
    protected String defaultExceptionStrategyRef;
    @XmlAttribute(name = "shutdownTimeout")
    protected String shutdownTimeout;
    @XmlAttribute(name = "maxQueueTransactionFilesSize")
    protected String maxQueueTransactionFilesSize;
    @XmlAttribute(name = "useExtendedTransformations")
    protected String useExtendedTransformations;
    @XmlAttribute(name = "flowEndingWithOneWayEndpointReturnsNull")
    protected String flowEndingWithOneWayEndpointReturnsNull;
    @XmlAttribute(name = "enricherPropagatesSessionVariableChanges")
    protected String enricherPropagatesSessionVariableChanges;
    @XmlAttribute(name = "defaultObjectSerializer-ref")
    protected String defaultObjectSerializerRef;
    @XmlAttribute(name = "defaultProcessingStrategy")
    protected String defaultProcessingStrategy;

    /**
     * Gets the value of the defaultThreadingProfile property.
     * 
     * @return
     *     possible object is
     *     {@link ThreadingProfileType }
     *     
     */
    public ThreadingProfileType getDefaultThreadingProfile() {
        return defaultThreadingProfile;
    }

    /**
     * Sets the value of the defaultThreadingProfile property.
     * 
     * @param value
     *     allowed object is
     *     {@link ThreadingProfileType }
     *     
     */
    public void setDefaultThreadingProfile(ThreadingProfileType value) {
        this.defaultThreadingProfile = value;
    }

    /**
     * Gets the value of the defaultDispatcherThreadingProfile property.
     * 
     * @return
     *     possible object is
     *     {@link ThreadingProfileType }
     *     
     */
    public ThreadingProfileType getDefaultDispatcherThreadingProfile() {
        return defaultDispatcherThreadingProfile;
    }

    /**
     * Sets the value of the defaultDispatcherThreadingProfile property.
     * 
     * @param value
     *     allowed object is
     *     {@link ThreadingProfileType }
     *     
     */
    public void setDefaultDispatcherThreadingProfile(ThreadingProfileType value) {
        this.defaultDispatcherThreadingProfile = value;
    }

    /**
     * Gets the value of the defaultReceiverThreadingProfile property.
     * 
     * @return
     *     possible object is
     *     {@link ThreadingProfileType }
     *     
     */
    public ThreadingProfileType getDefaultReceiverThreadingProfile() {
        return defaultReceiverThreadingProfile;
    }

    /**
     * Sets the value of the defaultReceiverThreadingProfile property.
     * 
     * @param value
     *     allowed object is
     *     {@link ThreadingProfileType }
     *     
     */
    public void setDefaultReceiverThreadingProfile(ThreadingProfileType value) {
        this.defaultReceiverThreadingProfile = value;
    }

    /**
     * Gets the value of the defaultServiceThreadingProfile property.
     * 
     * @return
     *     possible object is
     *     {@link ThreadingProfileType }
     *     
     */
    public ThreadingProfileType getDefaultServiceThreadingProfile() {
        return defaultServiceThreadingProfile;
    }

    /**
     * Sets the value of the defaultServiceThreadingProfile property.
     * 
     * @param value
     *     allowed object is
     *     {@link ThreadingProfileType }
     *     
     */
    public void setDefaultServiceThreadingProfile(ThreadingProfileType value) {
        this.defaultServiceThreadingProfile = value;
    }

    /**
     * 
     *                                 The default reconnection strategy, used by connectors and endpoints. This can also be configured on connectors, in which case the connector configuration is used instead of this default.
     *                             
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ReconnectForeverStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ReconnectCustomStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ReconnectSimpleStrategyType }{@code >}
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
     *     {@link JAXBElement }{@code <}{@link ReconnectCustomStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ReconnectSimpleStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractReconnectionStrategyType }{@code >}
     *     
     */
    public void setAbstractReconnectionStrategy(JAXBElement<? extends AbstractReconnectionStrategyType> value) {
        this.abstractReconnectionStrategy = value;
    }

    /**
     * Gets the value of the expressionLanguage property.
     * 
     * @return
     *     possible object is
     *     {@link ConfigurationType.ExpressionLanguage }
     *     
     */
    public ConfigurationType.ExpressionLanguage getExpressionLanguage() {
        return expressionLanguage;
    }

    /**
     * Sets the value of the expressionLanguage property.
     * 
     * @param value
     *     allowed object is
     *     {@link ConfigurationType.ExpressionLanguage }
     *     
     */
    public void setExpressionLanguage(ConfigurationType.ExpressionLanguage value) {
        this.expressionLanguage = value;
    }

    /**
     * 
     *                                 Mule application configuration extensions. Extensions can be defined as child of the configuration elements and then accessed by each module that defines it.
     *                             Gets the value of the abstractConfigurationExtension property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the abstractConfigurationExtension property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAbstractConfigurationExtension().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link HttpConfigType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractConfigurationExtensionType }{@code >}
     * 
     * 
     */
    public List<JAXBElement<? extends AbstractConfigurationExtensionType>> getAbstractConfigurationExtension() {
        if (abstractConfigurationExtension == null) {
            abstractConfigurationExtension = new ArrayList<JAXBElement<? extends AbstractConfigurationExtensionType>>();
        }
        return this.abstractConfigurationExtension;
    }

    /**
     * Gets the value of the defaultResponseTimeout property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDefaultResponseTimeout() {
        if (defaultResponseTimeout == null) {
            return "10000";
        } else {
            return defaultResponseTimeout;
        }
    }

    /**
     * Sets the value of the defaultResponseTimeout property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDefaultResponseTimeout(String value) {
        this.defaultResponseTimeout = value;
    }

    /**
     * Gets the value of the defaultTransactionTimeout property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDefaultTransactionTimeout() {
        if (defaultTransactionTimeout == null) {
            return "30000";
        } else {
            return defaultTransactionTimeout;
        }
    }

    /**
     * Sets the value of the defaultTransactionTimeout property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDefaultTransactionTimeout(String value) {
        this.defaultTransactionTimeout = value;
    }

    /**
     * Gets the value of the defaultExceptionStrategyRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDefaultExceptionStrategyRef() {
        return defaultExceptionStrategyRef;
    }

    /**
     * Sets the value of the defaultExceptionStrategyRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDefaultExceptionStrategyRef(String value) {
        this.defaultExceptionStrategyRef = value;
    }

    /**
     * Gets the value of the shutdownTimeout property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShutdownTimeout() {
        if (shutdownTimeout == null) {
            return "5000";
        } else {
            return shutdownTimeout;
        }
    }

    /**
     * Sets the value of the shutdownTimeout property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShutdownTimeout(String value) {
        this.shutdownTimeout = value;
    }

    /**
     * Gets the value of the maxQueueTransactionFilesSize property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaxQueueTransactionFilesSize() {
        if (maxQueueTransactionFilesSize == null) {
            return "500";
        } else {
            return maxQueueTransactionFilesSize;
        }
    }

    /**
     * Sets the value of the maxQueueTransactionFilesSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaxQueueTransactionFilesSize(String value) {
        this.maxQueueTransactionFilesSize = value;
    }

    /**
     * Gets the value of the useExtendedTransformations property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUseExtendedTransformations() {
        if (useExtendedTransformations == null) {
            return "true";
        } else {
            return useExtendedTransformations;
        }
    }

    /**
     * Sets the value of the useExtendedTransformations property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUseExtendedTransformations(String value) {
        this.useExtendedTransformations = value;
    }

    /**
     * Gets the value of the flowEndingWithOneWayEndpointReturnsNull property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFlowEndingWithOneWayEndpointReturnsNull() {
        if (flowEndingWithOneWayEndpointReturnsNull == null) {
            return "false";
        } else {
            return flowEndingWithOneWayEndpointReturnsNull;
        }
    }

    /**
     * Sets the value of the flowEndingWithOneWayEndpointReturnsNull property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFlowEndingWithOneWayEndpointReturnsNull(String value) {
        this.flowEndingWithOneWayEndpointReturnsNull = value;
    }

    /**
     * Gets the value of the enricherPropagatesSessionVariableChanges property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnricherPropagatesSessionVariableChanges() {
        if (enricherPropagatesSessionVariableChanges == null) {
            return "false";
        } else {
            return enricherPropagatesSessionVariableChanges;
        }
    }

    /**
     * Sets the value of the enricherPropagatesSessionVariableChanges property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnricherPropagatesSessionVariableChanges(String value) {
        this.enricherPropagatesSessionVariableChanges = value;
    }

    /**
     * Gets the value of the defaultObjectSerializerRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDefaultObjectSerializerRef() {
        return defaultObjectSerializerRef;
    }

    /**
     * Sets the value of the defaultObjectSerializerRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDefaultObjectSerializerRef(String value) {
        this.defaultObjectSerializerRef = value;
    }

    /**
     * Gets the value of the defaultProcessingStrategy property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDefaultProcessingStrategy() {
        return defaultProcessingStrategy;
    }

    /**
     * Sets the value of the defaultProcessingStrategy property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDefaultProcessingStrategy(String value) {
        this.defaultProcessingStrategy = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}abstractMixedContentExtensionType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="import" maxOccurs="unbounded" minOccurs="0"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;attribute name="name" type="{http://www.mulesoft.org/schema/mule/core}nonBlankString" /&gt;
     *                 &lt;attribute name="class" use="required" type="{http://www.mulesoft.org/schema/mule/core}substitutableClass" /&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="alias" maxOccurs="unbounded" minOccurs="0"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;attribute name="name" use="required" type="{http://www.mulesoft.org/schema/mule/core}nonBlankString" /&gt;
     *                 &lt;attribute name="expression" use="required" type="{http://www.mulesoft.org/schema/mule/core}nonBlankString" /&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="global-functions" minOccurs="0"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;attribute name="file" type="{http://www.mulesoft.org/schema/mule/core}nonBlankString" /&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *       &lt;/sequence&gt;
     *       &lt;attribute name="autoResolveVariables" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" default="true" /&gt;
     *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
     *     &lt;/extension&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class ExpressionLanguage
        extends AbstractMixedContentExtensionType
    {

        @XmlAttribute(name = "autoResolveVariables")
        protected String autoResolveVariables;

        /**
         * Gets the value of the autoResolveVariables property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAutoResolveVariables() {
            if (autoResolveVariables == null) {
                return "true";
            } else {
                return autoResolveVariables;
            }
        }

        /**
         * Sets the value of the autoResolveVariables property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAutoResolveVariables(String value) {
            this.autoResolveVariables = value;
        }

    }

}
