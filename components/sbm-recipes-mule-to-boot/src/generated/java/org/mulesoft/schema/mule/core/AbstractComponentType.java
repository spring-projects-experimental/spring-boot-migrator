
package org.mulesoft.schema.mule.core;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import org.mulesoft.schema.mule.scripting.ScriptComponentType;


/**
 * <p>Java class for abstractComponentType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="abstractComponentType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}abstractMessageProcessorType"&gt;
 *       &lt;group ref="{http://www.mulesoft.org/schema/mule/core}interceptorGroup" minOccurs="0"/&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "abstractComponentType", propOrder = {
    "abstractInterceptorOrInterceptorStack"
})
@XmlSeeAlso({
    DefaultJavaComponentType.class,
    StaticComponentType.class,
    DefaultComponentType.class,
    ScriptComponentType.class
})
public class AbstractComponentType
    extends AbstractMessageProcessorType
{

    @XmlElementRefs({
        @XmlElementRef(name = "abstract-interceptor", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "interceptor-stack", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    })
    protected List<JAXBElement<?>> abstractInterceptorOrInterceptorStack;

    /**
     * Gets the value of the abstractInterceptorOrInterceptorStack property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the abstractInterceptorOrInterceptorStack property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAbstractInterceptorOrInterceptorStack().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link AbstractInterceptorType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractInterceptorType }{@code >}
     * {@link JAXBElement }{@code <}{@link CustomInterceptorType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractInterceptorType }{@code >}
     * {@link JAXBElement }{@code <}{@link RefInterceptorStackType }{@code >}
     * 
     * 
     */
    public List<JAXBElement<?>> getAbstractInterceptorOrInterceptorStack() {
        if (abstractInterceptorOrInterceptorStack == null) {
            abstractInterceptorOrInterceptorStack = new ArrayList<JAXBElement<?>>();
        }
        return this.abstractInterceptorOrInterceptorStack;
    }

}
