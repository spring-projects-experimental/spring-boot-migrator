
package org.mulesoft.schema.mule.http;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.mulesoft.schema.mule.core.AbstractMessageProcessorType;


/**
 * <p>Java class for requestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="requestType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}abstractMessageProcessorType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="request-builder" type="{http://www.mulesoft.org/schema/mule/http}requestBuilderType" minOccurs="0"/&gt;
 *         &lt;choice&gt;
 *           &lt;element name="success-status-code-validator" minOccurs="0"&gt;
 *             &lt;complexType&gt;
 *               &lt;complexContent&gt;
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                   &lt;attribute name="values" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *                 &lt;/restriction&gt;
 *               &lt;/complexContent&gt;
 *             &lt;/complexType&gt;
 *           &lt;/element&gt;
 *           &lt;element name="failure-status-code-validator" minOccurs="0"&gt;
 *             &lt;complexType&gt;
 *               &lt;complexContent&gt;
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                   &lt;attribute name="values" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *                 &lt;/restriction&gt;
 *               &lt;/complexContent&gt;
 *             &lt;/complexType&gt;
 *           &lt;/element&gt;
 *         &lt;/choice&gt;
 *       &lt;/sequence&gt;
 *       &lt;attGroup ref="{http://www.mulesoft.org/schema/mule/http}commonRequestAttributes"/&gt;
 *       &lt;attribute name="path" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="method" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="config-ref" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="source" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="target" type="{http://www.w3.org/2001/XMLSchema}string" default="#[payload]" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "requestType", propOrder = {
    "requestBuilder",
    "successStatusCodeValidator",
    "failureStatusCodeValidator"
})
public class RequestType
    extends AbstractMessageProcessorType
{

    @XmlElement(name = "request-builder")
    protected RequestBuilderType requestBuilder;
    @XmlElement(name = "success-status-code-validator")
    protected RequestType.SuccessStatusCodeValidator successStatusCodeValidator;
    @XmlElement(name = "failure-status-code-validator")
    protected RequestType.FailureStatusCodeValidator failureStatusCodeValidator;
    @XmlAttribute(name = "path", required = true)
    protected String path;
    @XmlAttribute(name = "method")
    protected String method;
    @XmlAttribute(name = "config-ref", required = true)
    protected String configRef;
    @XmlAttribute(name = "source")
    protected String source;
    @XmlAttribute(name = "target")
    protected String target;
    @XmlAttribute(name = "followRedirects")
    protected String followRedirects;
    @XmlAttribute(name = "host")
    protected String host;
    @XmlAttribute(name = "port")
    protected String port;
    @XmlAttribute(name = "parseResponse")
    protected Boolean parseResponse;
    @XmlAttribute(name = "requestStreamingMode")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String requestStreamingMode;
    @XmlAttribute(name = "sendBodyMode")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String sendBodyMode;
    @XmlAttribute(name = "responseTimeout")
    protected String responseTimeout;

    /**
     * Gets the value of the requestBuilder property.
     * 
     * @return
     *     possible object is
     *     {@link RequestBuilderType }
     *     
     */
    public RequestBuilderType getRequestBuilder() {
        return requestBuilder;
    }

    /**
     * Sets the value of the requestBuilder property.
     * 
     * @param value
     *     allowed object is
     *     {@link RequestBuilderType }
     *     
     */
    public void setRequestBuilder(RequestBuilderType value) {
        this.requestBuilder = value;
    }

    /**
     * Gets the value of the successStatusCodeValidator property.
     * 
     * @return
     *     possible object is
     *     {@link RequestType.SuccessStatusCodeValidator }
     *     
     */
    public RequestType.SuccessStatusCodeValidator getSuccessStatusCodeValidator() {
        return successStatusCodeValidator;
    }

    /**
     * Sets the value of the successStatusCodeValidator property.
     * 
     * @param value
     *     allowed object is
     *     {@link RequestType.SuccessStatusCodeValidator }
     *     
     */
    public void setSuccessStatusCodeValidator(RequestType.SuccessStatusCodeValidator value) {
        this.successStatusCodeValidator = value;
    }

    /**
     * Gets the value of the failureStatusCodeValidator property.
     * 
     * @return
     *     possible object is
     *     {@link RequestType.FailureStatusCodeValidator }
     *     
     */
    public RequestType.FailureStatusCodeValidator getFailureStatusCodeValidator() {
        return failureStatusCodeValidator;
    }

    /**
     * Sets the value of the failureStatusCodeValidator property.
     * 
     * @param value
     *     allowed object is
     *     {@link RequestType.FailureStatusCodeValidator }
     *     
     */
    public void setFailureStatusCodeValidator(RequestType.FailureStatusCodeValidator value) {
        this.failureStatusCodeValidator = value;
    }

    /**
     * Gets the value of the path property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPath() {
        return path;
    }

    /**
     * Sets the value of the path property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPath(String value) {
        this.path = value;
    }

    /**
     * Gets the value of the method property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMethod() {
        return method;
    }

    /**
     * Sets the value of the method property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMethod(String value) {
        this.method = value;
    }

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
     * Gets the value of the followRedirects property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFollowRedirects() {
        return followRedirects;
    }

    /**
     * Sets the value of the followRedirects property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFollowRedirects(String value) {
        this.followRedirects = value;
    }

    /**
     * Gets the value of the host property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHost() {
        return host;
    }

    /**
     * Sets the value of the host property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHost(String value) {
        this.host = value;
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
     * Gets the value of the parseResponse property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isParseResponse() {
        if (parseResponse == null) {
            return true;
        } else {
            return parseResponse;
        }
    }

    /**
     * Sets the value of the parseResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setParseResponse(Boolean value) {
        this.parseResponse = value;
    }

    /**
     * Gets the value of the requestStreamingMode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequestStreamingMode() {
        return requestStreamingMode;
    }

    /**
     * Sets the value of the requestStreamingMode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequestStreamingMode(String value) {
        this.requestStreamingMode = value;
    }

    /**
     * Gets the value of the sendBodyMode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSendBodyMode() {
        return sendBodyMode;
    }

    /**
     * Sets the value of the sendBodyMode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSendBodyMode(String value) {
        this.sendBodyMode = value;
    }

    /**
     * Gets the value of the responseTimeout property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResponseTimeout() {
        return responseTimeout;
    }

    /**
     * Sets the value of the responseTimeout property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResponseTimeout(String value) {
        this.responseTimeout = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;attribute name="values" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class FailureStatusCodeValidator {

        @XmlAttribute(name = "values", required = true)
        protected String values;

        /**
         * Gets the value of the values property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getValues() {
            return values;
        }

        /**
         * Sets the value of the values property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setValues(String value) {
            this.values = value;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;attribute name="values" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class SuccessStatusCodeValidator {

        @XmlAttribute(name = "values", required = true)
        protected String values;

        /**
         * Gets the value of the values property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getValues() {
            return values;
        }

        /**
         * Sets the value of the values property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setValues(String value) {
            this.values = value;
        }

    }

}
