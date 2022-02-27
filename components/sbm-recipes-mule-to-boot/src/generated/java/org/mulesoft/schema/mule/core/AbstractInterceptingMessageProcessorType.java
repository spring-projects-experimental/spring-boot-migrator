
package org.mulesoft.schema.mule.core;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import org.mulesoft.schema.mule.http.StaticResourceHandlerType;


/**
 * <p>Java class for abstractInterceptingMessageProcessorType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="abstractInterceptingMessageProcessorType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}abstractMessageProcessorType"&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "abstractInterceptingMessageProcessorType")
@XmlSeeAlso({
    WireTap.class,
    BaseAggregatorType.class,
    ForeachProcessorType.class,
    AbstractGlobalInterceptingMessageProcessorType.class,
    BaseSplitterType.class,
    StaticResourceHandlerType.class
})
public class AbstractInterceptingMessageProcessorType
    extends AbstractMessageProcessorType
{


}
