
package org.mulesoft.schema.mule.core;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for abstractInterceptorStackType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="abstractInterceptorStackType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}annotatedType"&gt;
 *       &lt;sequence maxOccurs="unbounded"&gt;
 *         &lt;element ref="{http://www.mulesoft.org/schema/mule/core}abstract-interceptor"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="name" use="required" type="{http://www.mulesoft.org/schema/mule/core}nonBlankString" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "abstractInterceptorStackType", propOrder = {
    "abstractInterceptor"
})
public class AbstractInterceptorStackType
    extends AnnotatedType
{

    @XmlElementRef(name = "abstract-interceptor", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class)
    protected List<JAXBElement<? extends AbstractInterceptorType>> abstractInterceptor;
    @XmlAttribute(name = "name", required = true)
    protected String name;

    /**
     * Gets the value of the abstractInterceptor property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the abstractInterceptor property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAbstractInterceptor().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link AbstractInterceptorType }{@code >}
     * {@link JAXBElement }{@code <}{@link CustomInterceptorType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractInterceptorType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractInterceptorType }{@code >}
     * 
     * 
     */
    public List<JAXBElement<? extends AbstractInterceptorType>> getAbstractInterceptor() {
        if (abstractInterceptor == null) {
            abstractInterceptor = new ArrayList<JAXBElement<? extends AbstractInterceptorType>>();
        }
        return this.abstractInterceptor;
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

}
