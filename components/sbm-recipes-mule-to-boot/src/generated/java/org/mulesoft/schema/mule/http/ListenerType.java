
package org.mulesoft.schema.mule.http;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.mulesoft.schema.mule.core.AbstractMessageSourceType;


/**
 * <p>Java class for listenerType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="listenerType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}abstractMessageSourceType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="response-builder" type="{http://www.mulesoft.org/schema/mule/http}responseBuilderType" minOccurs="0"/&gt;
 *         &lt;element name="error-response-builder" type="{http://www.mulesoft.org/schema/mule/http}responseBuilderType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="path" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="allowedMethods" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="config-ref" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="responseStreamingMode" default="AUTO"&gt;
 *         &lt;simpleType&gt;
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN"&gt;
 *             &lt;enumeration value="AUTO"/&gt;
 *             &lt;enumeration value="ALWAYS"/&gt;
 *             &lt;enumeration value="NEVER"/&gt;
 *           &lt;/restriction&gt;
 *         &lt;/simpleType&gt;
 *       &lt;/attribute&gt;
 *       &lt;attribute name="parseRequest" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "listenerType", propOrder = {
    "responseBuilder",
    "errorResponseBuilder"
})
public class ListenerType
    extends AbstractMessageSourceType
{

    @XmlElement(name = "response-builder")
    protected ResponseBuilderType responseBuilder;
    @XmlElement(name = "error-response-builder")
    protected ResponseBuilderType errorResponseBuilder;
    @XmlAttribute(name = "path", required = true)
    protected String path;
    @XmlAttribute(name = "allowedMethods")
    protected String allowedMethods;
    @XmlAttribute(name = "config-ref", required = true)
    protected String configRef;
    @XmlAttribute(name = "responseStreamingMode")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String responseStreamingMode;
    @XmlAttribute(name = "parseRequest")
    protected String parseRequest;

    /**
     * Gets the value of the responseBuilder property.
     * 
     * @return
     *     possible object is
     *     {@link ResponseBuilderType }
     *     
     */
    public ResponseBuilderType getResponseBuilder() {
        return responseBuilder;
    }

    /**
     * Sets the value of the responseBuilder property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResponseBuilderType }
     *     
     */
    public void setResponseBuilder(ResponseBuilderType value) {
        this.responseBuilder = value;
    }

    /**
     * Gets the value of the errorResponseBuilder property.
     * 
     * @return
     *     possible object is
     *     {@link ResponseBuilderType }
     *     
     */
    public ResponseBuilderType getErrorResponseBuilder() {
        return errorResponseBuilder;
    }

    /**
     * Sets the value of the errorResponseBuilder property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResponseBuilderType }
     *     
     */
    public void setErrorResponseBuilder(ResponseBuilderType value) {
        this.errorResponseBuilder = value;
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
     * Gets the value of the allowedMethods property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAllowedMethods() {
        return allowedMethods;
    }

    /**
     * Sets the value of the allowedMethods property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAllowedMethods(String value) {
        this.allowedMethods = value;
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
     * Gets the value of the responseStreamingMode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResponseStreamingMode() {
        if (responseStreamingMode == null) {
            return "AUTO";
        } else {
            return responseStreamingMode;
        }
    }

    /**
     * Sets the value of the responseStreamingMode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResponseStreamingMode(String value) {
        this.responseStreamingMode = value;
    }

    /**
     * Gets the value of the parseRequest property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParseRequest() {
        return parseRequest;
    }

    /**
     * Sets the value of the parseRequest property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParseRequest(String value) {
        this.parseRequest = value;
    }

}
