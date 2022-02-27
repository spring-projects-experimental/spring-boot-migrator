
package org.mulesoft.schema.mule.http;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;
import org.mulesoft.schema.mule.core.AbstractExtensionType;


/**
 * <p>Java class for responseBuilderType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="responseBuilderType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}abstractExtensionType"&gt;
 *       &lt;sequence&gt;
 *         &lt;choice maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;element name="builder"&gt;
 *             &lt;complexType&gt;
 *               &lt;complexContent&gt;
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                   &lt;attribute name="ref" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *                 &lt;/restriction&gt;
 *               &lt;/complexContent&gt;
 *             &lt;/complexType&gt;
 *           &lt;/element&gt;
 *           &lt;element name="header" type="{http://www.mulesoft.org/schema/mule/http}httpHeaderType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *           &lt;element name="headers" type="{http://www.mulesoft.org/schema/mule/http}headersType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;/choice&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="statusCode" type="{http://www.mulesoft.org/schema/mule/core}substitutableInt" /&gt;
 *       &lt;attribute name="reasonPhrase" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="disablePropertiesAsHeaders" type="{http://www.mulesoft.org/schema/mule/core}substitutableBoolean" default="false" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "responseBuilderType", propOrder = {
    "builderOrHeaderOrHeaders"
})
public class ResponseBuilderType
    extends AbstractExtensionType
{

    @XmlElements({
        @XmlElement(name = "builder", type = ResponseBuilderType.Builder.class),
        @XmlElement(name = "header", type = HttpHeaderType.class),
        @XmlElement(name = "headers", type = HeadersType.class)
    })
    protected List<Object> builderOrHeaderOrHeaders;
    @XmlAttribute(name = "statusCode")
    protected String statusCode;
    @XmlAttribute(name = "reasonPhrase")
    protected String reasonPhrase;
    @XmlAttribute(name = "disablePropertiesAsHeaders")
    protected String disablePropertiesAsHeaders;

    /**
     * Gets the value of the builderOrHeaderOrHeaders property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the builderOrHeaderOrHeaders property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBuilderOrHeaderOrHeaders().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ResponseBuilderType.Builder }
     * {@link HttpHeaderType }
     * {@link HeadersType }
     * 
     * 
     */
    public List<Object> getBuilderOrHeaderOrHeaders() {
        if (builderOrHeaderOrHeaders == null) {
            builderOrHeaderOrHeaders = new ArrayList<Object>();
        }
        return this.builderOrHeaderOrHeaders;
    }

    /**
     * Gets the value of the statusCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatusCode() {
        return statusCode;
    }

    /**
     * Sets the value of the statusCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatusCode(String value) {
        this.statusCode = value;
    }

    /**
     * Gets the value of the reasonPhrase property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReasonPhrase() {
        return reasonPhrase;
    }

    /**
     * Sets the value of the reasonPhrase property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReasonPhrase(String value) {
        this.reasonPhrase = value;
    }

    /**
     * Gets the value of the disablePropertiesAsHeaders property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDisablePropertiesAsHeaders() {
        if (disablePropertiesAsHeaders == null) {
            return "false";
        } else {
            return disablePropertiesAsHeaders;
        }
    }

    /**
     * Sets the value of the disablePropertiesAsHeaders property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDisablePropertiesAsHeaders(String value) {
        this.disablePropertiesAsHeaders = value;
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
     *       &lt;attribute name="ref" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Builder {

        @XmlAttribute(name = "ref", required = true)
        protected String ref;

        /**
         * Gets the value of the ref property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRef() {
            return ref;
        }

        /**
         * Sets the value of the ref property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRef(String value) {
            this.ref = value;
        }

    }

}
