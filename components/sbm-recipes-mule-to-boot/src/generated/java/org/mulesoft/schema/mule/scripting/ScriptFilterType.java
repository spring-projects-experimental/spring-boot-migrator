
package org.mulesoft.schema.mule.scripting;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.mulesoft.schema.mule.core.AbstractFilterType;


/**
 * <p>Java class for scriptFilterType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="scriptFilterType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}abstractFilterType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="script" type="{http://www.mulesoft.org/schema/mule/scripting}scriptType"/&gt;
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
@XmlType(name = "scriptFilterType", propOrder = {
    "script"
})
public class ScriptFilterType
    extends AbstractFilterType
{

    @XmlElement(required = true)
    protected ScriptType script;

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

}
