
package org.mulesoft.schema.mule.core;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for extensibleEntryPointResolverSet complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="extensibleEntryPointResolverSet"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}abstractEntryPointResolverSetType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.mulesoft.org/schema/mule/core}abstract-entry-point-resolver" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "extensibleEntryPointResolverSet", propOrder = {
    "abstractEntryPointResolver"
})
public class ExtensibleEntryPointResolverSet
    extends AbstractEntryPointResolverSetType
{

    @XmlElementRef(name = "abstract-entry-point-resolver", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    protected List<JAXBElement<? extends AbstractEntryPointResolverType>> abstractEntryPointResolver;

    /**
     * Gets the value of the abstractEntryPointResolver property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the abstractEntryPointResolver property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAbstractEntryPointResolver().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link ReflectionEntryPointResolverType }{@code >}
     * {@link JAXBElement }{@code <}{@link ComplexEntryPointResolverType }{@code >}
     * {@link JAXBElement }{@code <}{@link MethodEntryPointResolverType }{@code >}
     * {@link JAXBElement }{@code <}{@link ComplexEntryPointResolverType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractEntryPointResolverType }{@code >}
     * {@link JAXBElement }{@code <}{@link CustomEntryPointResolverType }{@code >}
     * {@link JAXBElement }{@code <}{@link PropertyEntryPointResolverType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractEntryPointResolverType }{@code >}
     * 
     * 
     */
    public List<JAXBElement<? extends AbstractEntryPointResolverType>> getAbstractEntryPointResolver() {
        if (abstractEntryPointResolver == null) {
            abstractEntryPointResolver = new ArrayList<JAXBElement<? extends AbstractEntryPointResolverType>>();
        }
        return this.abstractEntryPointResolver;
    }

}
