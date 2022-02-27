
package org.mulesoft.schema.mule.core;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for abstractModelType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="abstractModelType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}annotatedType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.mulesoft.org/schema/mule/core}legacy-abstract-exception-strategy" minOccurs="0"/&gt;
 *         &lt;group ref="{http://www.mulesoft.org/schema/mule/core}entryPointResolvers"/&gt;
 *         &lt;element ref="{http://www.mulesoft.org/schema/mule/core}abstract-service" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="name" type="{http://www.mulesoft.org/schema/mule/core}nonBlankString" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "abstractModelType", propOrder = {
    "legacyAbstractExceptionStrategy",
    "abstractEntryPointResolverSet",
    "abstractEntryPointResolver",
    "abstractService"
})
@XmlSeeAlso({
    SedaModelType.class
})
public class AbstractModelType
    extends AnnotatedType
{

    @XmlElementRef(name = "legacy-abstract-exception-strategy", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    protected JAXBElement<? extends ExceptionStrategyType> legacyAbstractExceptionStrategy;
    @XmlElementRef(name = "abstract-entry-point-resolver-set", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    protected JAXBElement<? extends AbstractEntryPointResolverSetType> abstractEntryPointResolverSet;
    @XmlElementRef(name = "abstract-entry-point-resolver", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    protected JAXBElement<? extends AbstractEntryPointResolverType> abstractEntryPointResolver;
    @XmlElementRef(name = "abstract-service", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    protected List<JAXBElement<? extends AbstractServiceType>> abstractService;
    @XmlAttribute(name = "name")
    protected String name;

    /**
     * Gets the value of the legacyAbstractExceptionStrategy property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link RollbackExceptionStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ReferenceExceptionStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ChoiceExceptionStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CatchExceptionStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomExceptionStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ExceptionStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ServiceExceptionStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ServiceExceptionStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ExceptionStrategyType }{@code >}
     *     
     */
    public JAXBElement<? extends ExceptionStrategyType> getLegacyAbstractExceptionStrategy() {
        return legacyAbstractExceptionStrategy;
    }

    /**
     * Sets the value of the legacyAbstractExceptionStrategy property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link RollbackExceptionStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ReferenceExceptionStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ChoiceExceptionStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CatchExceptionStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomExceptionStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ExceptionStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ServiceExceptionStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ServiceExceptionStrategyType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ExceptionStrategyType }{@code >}
     *     
     */
    public void setLegacyAbstractExceptionStrategy(JAXBElement<? extends ExceptionStrategyType> value) {
        this.legacyAbstractExceptionStrategy = value;
    }

    /**
     * Gets the value of the abstractEntryPointResolverSet property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ExtensibleEntryPointResolverSet }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomEntryPointResolverSetType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ExtensibleEntryPointResolverSet }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractEntryPointResolverSetType }{@code >}
     *     
     */
    public JAXBElement<? extends AbstractEntryPointResolverSetType> getAbstractEntryPointResolverSet() {
        return abstractEntryPointResolverSet;
    }

    /**
     * Sets the value of the abstractEntryPointResolverSet property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ExtensibleEntryPointResolverSet }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomEntryPointResolverSetType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ExtensibleEntryPointResolverSet }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractEntryPointResolverSetType }{@code >}
     *     
     */
    public void setAbstractEntryPointResolverSet(JAXBElement<? extends AbstractEntryPointResolverSetType> value) {
        this.abstractEntryPointResolverSet = value;
    }

    /**
     * Gets the value of the abstractEntryPointResolver property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ReflectionEntryPointResolverType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ComplexEntryPointResolverType }{@code >}
     *     {@link JAXBElement }{@code <}{@link MethodEntryPointResolverType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ComplexEntryPointResolverType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractEntryPointResolverType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomEntryPointResolverType }{@code >}
     *     {@link JAXBElement }{@code <}{@link PropertyEntryPointResolverType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractEntryPointResolverType }{@code >}
     *     
     */
    public JAXBElement<? extends AbstractEntryPointResolverType> getAbstractEntryPointResolver() {
        return abstractEntryPointResolver;
    }

    /**
     * Sets the value of the abstractEntryPointResolver property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ReflectionEntryPointResolverType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ComplexEntryPointResolverType }{@code >}
     *     {@link JAXBElement }{@code <}{@link MethodEntryPointResolverType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ComplexEntryPointResolverType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractEntryPointResolverType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CustomEntryPointResolverType }{@code >}
     *     {@link JAXBElement }{@code <}{@link PropertyEntryPointResolverType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractEntryPointResolverType }{@code >}
     *     
     */
    public void setAbstractEntryPointResolver(JAXBElement<? extends AbstractEntryPointResolverType> value) {
        this.abstractEntryPointResolver = value;
    }

    /**
     * Gets the value of the abstractService property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the abstractService property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAbstractService().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link CustomServiceType }{@code >}
     * {@link JAXBElement }{@code <}{@link SedaServiceType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractServiceType }{@code >}
     * 
     * 
     */
    public List<JAXBElement<? extends AbstractServiceType>> getAbstractService() {
        if (abstractService == null) {
            abstractService = new ArrayList<JAXBElement<? extends AbstractServiceType>>();
        }
        return this.abstractService;
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
