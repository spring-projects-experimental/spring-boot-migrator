
package org.mulesoft.schema.mule.http;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.mulesoft.schema.mule.core.CollectionFilterType;
import org.mulesoft.schema.mule.core.CommonFilterType;
import org.mulesoft.schema.mule.core.CustomFilterType;
import org.mulesoft.schema.mule.core.DefaultComponentType;
import org.mulesoft.schema.mule.core.ExpressionFilterType;
import org.mulesoft.schema.mule.core.KeyValueType;
import org.mulesoft.schema.mule.core.RefFilterType;
import org.mulesoft.schema.mule.core.RegexFilterType;
import org.mulesoft.schema.mule.core.ScopedPropertyFilterType;
import org.mulesoft.schema.mule.core.TypeFilterType;
import org.mulesoft.schema.mule.core.UnitaryFilterType;
import org.mulesoft.schema.mule.core.ValueType;
import org.mulesoft.schema.mule.core.WildcardFilterType;
import org.mulesoft.schema.mule.jms.PropertyFilter;


/**
 * <p>Java class for restServiceWrapperType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="restServiceWrapperType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}defaultComponentType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="error-filter" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element ref="{http://www.mulesoft.org/schema/mule/core}abstract-filter" minOccurs="0"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="payloadParameterName" type="{http://www.mulesoft.org/schema/mule/core}valueType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="requiredParameter" type="{http://www.mulesoft.org/schema/mule/core}keyValueType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="optionalParameter" type="{http://www.mulesoft.org/schema/mule/core}keyValueType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="httpMethod" default="GET"&gt;
 *         &lt;simpleType&gt;
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN"&gt;
 *             &lt;enumeration value="DELETE"/&gt;
 *             &lt;enumeration value="GET"/&gt;
 *             &lt;enumeration value="POST"/&gt;
 *           &lt;/restriction&gt;
 *         &lt;/simpleType&gt;
 *       &lt;/attribute&gt;
 *       &lt;attribute name="serviceUrl" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "restServiceWrapperType", propOrder = {
    "errorFilter",
    "payloadParameterName",
    "requiredParameter",
    "optionalParameter"
})
public class RestServiceWrapperType
    extends DefaultComponentType
{

    @XmlElement(name = "error-filter")
    protected RestServiceWrapperType.ErrorFilter errorFilter;
    protected List<ValueType> payloadParameterName;
    protected List<KeyValueType> requiredParameter;
    protected List<KeyValueType> optionalParameter;
    @XmlAttribute(name = "httpMethod")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String httpMethod;
    @XmlAttribute(name = "serviceUrl", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String serviceUrl;

    /**
     * Gets the value of the errorFilter property.
     * 
     * @return
     *     possible object is
     *     {@link RestServiceWrapperType.ErrorFilter }
     *     
     */
    public RestServiceWrapperType.ErrorFilter getErrorFilter() {
        return errorFilter;
    }

    /**
     * Sets the value of the errorFilter property.
     * 
     * @param value
     *     allowed object is
     *     {@link RestServiceWrapperType.ErrorFilter }
     *     
     */
    public void setErrorFilter(RestServiceWrapperType.ErrorFilter value) {
        this.errorFilter = value;
    }

    /**
     * Gets the value of the payloadParameterName property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the payloadParameterName property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPayloadParameterName().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ValueType }
     * 
     * 
     */
    public List<ValueType> getPayloadParameterName() {
        if (payloadParameterName == null) {
            payloadParameterName = new ArrayList<ValueType>();
        }
        return this.payloadParameterName;
    }

    /**
     * Gets the value of the requiredParameter property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the requiredParameter property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRequiredParameter().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link KeyValueType }
     * 
     * 
     */
    public List<KeyValueType> getRequiredParameter() {
        if (requiredParameter == null) {
            requiredParameter = new ArrayList<KeyValueType>();
        }
        return this.requiredParameter;
    }

    /**
     * Gets the value of the optionalParameter property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the optionalParameter property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOptionalParameter().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link KeyValueType }
     * 
     * 
     */
    public List<KeyValueType> getOptionalParameter() {
        if (optionalParameter == null) {
            optionalParameter = new ArrayList<KeyValueType>();
        }
        return this.optionalParameter;
    }

    /**
     * Gets the value of the httpMethod property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHttpMethod() {
        if (httpMethod == null) {
            return "GET";
        } else {
            return httpMethod;
        }
    }

    /**
     * Sets the value of the httpMethod property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHttpMethod(String value) {
        this.httpMethod = value;
    }

    /**
     * Gets the value of the serviceUrl property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceUrl() {
        return serviceUrl;
    }

    /**
     * Sets the value of the serviceUrl property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceUrl(String value) {
        this.serviceUrl = value;
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
     *       &lt;sequence&gt;
     *         &lt;element ref="{http://www.mulesoft.org/schema/mule/core}abstract-filter" minOccurs="0"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "abstractFilter"
    })
    public static class ErrorFilter {

        @XmlElementRef(name = "abstract-filter", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
        protected JAXBElement<? extends CommonFilterType> abstractFilter;

        /**
         * Gets the value of the abstractFilter property.
         * 
         * @return
         *     possible object is
         *     {@link JAXBElement }{@code <}{@link RefFilterType }{@code >}
         *     {@link JAXBElement }{@code <}{@link RegexFilterType }{@code >}
         *     {@link JAXBElement }{@code <}{@link CollectionFilterType }{@code >}
         *     {@link JAXBElement }{@code <}{@link TypeFilterType }{@code >}
         *     {@link JAXBElement }{@code <}{@link TypeFilterType }{@code >}
         *     {@link JAXBElement }{@code <}{@link CollectionFilterType }{@code >}
         *     {@link JAXBElement }{@code <}{@link ExpressionFilterType }{@code >}
         *     {@link JAXBElement }{@code <}{@link WildcardFilterType }{@code >}
         *     {@link JAXBElement }{@code <}{@link PropertyFilter }{@code >}
         *     {@link JAXBElement }{@code <}{@link ScopedPropertyFilterType }{@code >}
         *     {@link JAXBElement }{@code <}{@link CustomFilterType }{@code >}
         *     {@link JAXBElement }{@code <}{@link WildcardFilterType }{@code >}
         *     {@link JAXBElement }{@code <}{@link UnitaryFilterType }{@code >}
         *     {@link JAXBElement }{@code <}{@link CommonFilterType }{@code >}
         *     
         */
        public JAXBElement<? extends CommonFilterType> getAbstractFilter() {
            return abstractFilter;
        }

        /**
         * Sets the value of the abstractFilter property.
         * 
         * @param value
         *     allowed object is
         *     {@link JAXBElement }{@code <}{@link RefFilterType }{@code >}
         *     {@link JAXBElement }{@code <}{@link RegexFilterType }{@code >}
         *     {@link JAXBElement }{@code <}{@link CollectionFilterType }{@code >}
         *     {@link JAXBElement }{@code <}{@link TypeFilterType }{@code >}
         *     {@link JAXBElement }{@code <}{@link TypeFilterType }{@code >}
         *     {@link JAXBElement }{@code <}{@link CollectionFilterType }{@code >}
         *     {@link JAXBElement }{@code <}{@link ExpressionFilterType }{@code >}
         *     {@link JAXBElement }{@code <}{@link WildcardFilterType }{@code >}
         *     {@link JAXBElement }{@code <}{@link PropertyFilter }{@code >}
         *     {@link JAXBElement }{@code <}{@link ScopedPropertyFilterType }{@code >}
         *     {@link JAXBElement }{@code <}{@link CustomFilterType }{@code >}
         *     {@link JAXBElement }{@code <}{@link WildcardFilterType }{@code >}
         *     {@link JAXBElement }{@code <}{@link UnitaryFilterType }{@code >}
         *     {@link JAXBElement }{@code <}{@link CommonFilterType }{@code >}
         *     
         */
        public void setAbstractFilter(JAXBElement<? extends CommonFilterType> value) {
            this.abstractFilter = value;
        }

    }

}
