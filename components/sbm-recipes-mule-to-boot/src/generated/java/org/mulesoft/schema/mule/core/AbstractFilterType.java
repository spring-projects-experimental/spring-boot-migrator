
package org.mulesoft.schema.mule.core;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import org.mulesoft.schema.mule.jms.PropertyFilter;
import org.mulesoft.schema.mule.scripting.ScriptFilterType;


/**
 * <p>Java class for abstractFilterType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="abstractFilterType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}commonFilterType"&gt;
 *       &lt;attribute name="name" type="{http://www.mulesoft.org/schema/mule/core}substitutableName" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "abstractFilterType")
@XmlSeeAlso({
    UnitaryFilterType.class,
    CollectionFilterType.class,
    ExpressionFilterType.class,
    TypeFilterType.class,
    CustomFilterType.class,
    PatternFilterType.class,
    ScriptFilterType.class,
    PropertyFilter.class
})
public class AbstractFilterType
    extends CommonFilterType
{

    @XmlAttribute(name = "name")
    protected String name;

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
