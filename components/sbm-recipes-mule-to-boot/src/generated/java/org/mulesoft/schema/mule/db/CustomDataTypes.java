
package org.mulesoft.schema.mule.db;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.mulesoft.schema.mule.core.AnnotatedType;


/**
 * <p>Java class for CustomDataTypes complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CustomDataTypes"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}annotatedType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="data-type" type="{http://www.mulesoft.org/schema/mule/db}CustomDataType" maxOccurs="unbounded"/&gt;
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
@XmlType(name = "CustomDataTypes", propOrder = {
    "dataType"
})
public class CustomDataTypes
    extends AnnotatedType
{

    @XmlElement(name = "data-type", required = true)
    protected List<CustomDataType> dataType;

    /**
     * Gets the value of the dataType property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dataType property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDataType().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CustomDataType }
     * 
     * 
     */
    public List<CustomDataType> getDataType() {
        if (dataType == null) {
            dataType = new ArrayList<CustomDataType>();
        }
        return this.dataType;
    }

}
