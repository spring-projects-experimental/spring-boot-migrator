
package org.mulesoft.schema.mule.core;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import org.mulesoft.schema.mule.amqp.BasicAckType;
import org.mulesoft.schema.mule.amqp.BasicRejectType;
import org.mulesoft.schema.mule.amqp.ReturnHandlerType;
import org.mulesoft.schema.mule.db.AbstractDbMessageProcessorType;
import org.mulesoft.schema.mule.ee.dw.TransformMessageType;
import org.mulesoft.schema.mule.http.RequestType;


/**
 * <p>Java class for abstractMessageProcessorType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="abstractMessageProcessorType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}annotatedType"&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "abstractMessageProcessorType")
@XmlSeeAlso({
    TransformMessageType.class,
    RefMessageProcessorType.class,
    CustomMessageProcessorType.class,
    MessageProcessorChainType.class,
    InvokeType.class,
    MessageEnricherType.class,
    AsyncType.class,
    RequestReplyType.class,
    AbstractObserverMessageProcessorType.class,
    AbstractTransactional.class,
    FlowRef.class,
    AbstractInterceptorType.class,
    SetPayloadTransformerType.class,
    CommonTransformerType.class,
    AbstractRoutingMessageProcessorType.class,
    BasicAckType.class,
    BasicRejectType.class,
    ReturnHandlerType.class,
    AbstractDbMessageProcessorType.class,
    AbstractEmptyMessageProcessorType.class,
    RequestType.class,
    AbstractComponentType.class,
    AbstractSecurityFilterType.class,
    AbstractInterceptingMessageProcessorType.class,
    CommonFilterType.class
})
public class AbstractMessageProcessorType
    extends AnnotatedType
{


}
