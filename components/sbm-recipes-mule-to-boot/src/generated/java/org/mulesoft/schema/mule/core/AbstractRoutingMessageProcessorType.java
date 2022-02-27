
package org.mulesoft.schema.mule.core;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for abstractRoutingMessageProcessorType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="abstractRoutingMessageProcessorType"&gt;
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
@XmlType(name = "abstractRoutingMessageProcessorType")
@XmlSeeAlso({
    SelectiveOutboundRouterType.class,
    UntilSuccessful.class,
    ScatterGather.class,
    ProcessorWithAtLeastOneTargetType.class,
    RecipientList.class,
    SingleTarget.class,
    BaseSingleRouteRoutingMessageProcessorType.class,
    ProcessorWithExactlyOneTargetType.class,
    AbstractDynamicRoutingMessageProcessor.class
})
public class AbstractRoutingMessageProcessorType
    extends AbstractMessageProcessorType
{


}
