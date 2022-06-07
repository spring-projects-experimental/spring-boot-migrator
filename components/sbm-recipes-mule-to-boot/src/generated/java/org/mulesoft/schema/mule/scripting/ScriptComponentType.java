
package org.mulesoft.schema.mule.scripting;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.mulesoft.schema.mule.core.AbstractComponentType;
import org.mulesoft.schema.mule.core.PojoBindingType;


/**
 * <p>Java class for scriptComponentType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="scriptComponentType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}abstractComponentType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="script" type="{http://www.mulesoft.org/schema/mule/scripting}scriptType" minOccurs="0"/&gt;
 *         &lt;element name="java-interface-binding" type="{http://www.mulesoft.org/schema/mule/core}pojoBindingType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="script-ref" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "scriptComponentType", propOrder = {
    "script",
    "javaInterfaceBinding"
})
public class ScriptComponentType
    extends AbstractComponentType
{

    protected ScriptType script;
    @XmlElement(name = "java-interface-binding")
    protected List<PojoBindingType> javaInterfaceBinding;
    @XmlAttribute(name = "script-ref")
    protected String scriptRef;

    /**
     * Gets the value of the script property.
     * 
     * @return
     *     possible object is
     *     {@link ScriptType }
     *     
     */
    public ScriptType getScript() {
        return script;
    }

    /**
     * Sets the value of the script property.
     * 
     * @param value
     *     allowed object is
     *     {@link ScriptType }
     *     
     */
    public void setScript(ScriptType value) {
        this.script = value;
    }

    /**
     * Gets the value of the javaInterfaceBinding property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the javaInterfaceBinding property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getJavaInterfaceBinding().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PojoBindingType }
     * 
     * 
     */
    public List<PojoBindingType> getJavaInterfaceBinding() {
        if (javaInterfaceBinding == null) {
            javaInterfaceBinding = new ArrayList<PojoBindingType>();
        }
        return this.javaInterfaceBinding;
    }

    /**
     * Gets the value of the scriptRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getScriptRef() {
        return scriptRef;
    }

    /**
     * Sets the value of the scriptRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setScriptRef(String value) {
        this.scriptRef = value;
    }

}
