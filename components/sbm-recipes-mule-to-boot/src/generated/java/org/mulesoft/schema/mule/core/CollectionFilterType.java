
package org.mulesoft.schema.mule.core;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;
import org.mulesoft.schema.mule.jms.PropertyFilter;


/**
 * <p>Java class for collectionFilterType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="collectionFilterType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}abstractFilterType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.mulesoft.org/schema/mule/core}abstract-filter" maxOccurs="unbounded" minOccurs="2"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "collectionFilterType", propOrder = {
    "abstractFilter"
})
public class CollectionFilterType
    extends AbstractFilterType
{

    @XmlElementRef(name = "abstract-filter", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class)
    protected List<JAXBElement<? extends CommonFilterType>> abstractFilter;

    /**
     * Gets the value of the abstractFilter property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the abstractFilter property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAbstractFilter().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link CollectionFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link PropertyFilter }{@code >}
     * {@link JAXBElement }{@code <}{@link ExpressionFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link CollectionFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link UnitaryFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link RefFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link WildcardFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link RegexFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link ScopedPropertyFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link TypeFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link WildcardFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link CustomFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link TypeFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link CommonFilterType }{@code >}
     * 
     * 
     */
    public List<JAXBElement<? extends CommonFilterType>> getAbstractFilter() {
        if (abstractFilter == null) {
            abstractFilter = new ArrayList<JAXBElement<? extends CommonFilterType>>();
        }
        return this.abstractFilter;
    }

}
