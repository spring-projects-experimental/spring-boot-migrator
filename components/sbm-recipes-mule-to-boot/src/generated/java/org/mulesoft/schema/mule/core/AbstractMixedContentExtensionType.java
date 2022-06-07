
package org.mulesoft.schema.mule.core;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import org.mulesoft.schema.mule.scripting.LangType;
import org.mulesoft.schema.mule.scripting.ScriptType;


/**
 * <p>Java class for abstractMixedContentExtensionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="abstractMixedContentExtensionType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}annotatedMixedContentType"&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "abstractMixedContentExtensionType")
@XmlSeeAlso({
    org.mulesoft.schema.mule.core.ConfigurationType.ExpressionLanguage.class,
    ScriptType.class,
    LangType.class
})
public class AbstractMixedContentExtensionType
    extends AnnotatedMixedContentType
{


}
