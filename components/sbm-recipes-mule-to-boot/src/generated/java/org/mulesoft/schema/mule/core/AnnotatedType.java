
package org.mulesoft.schema.mule.core;

import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;
import org.mulesoft.schema.mule.http.AbstractHttpRequestAuthenticationProvider;
import org.mulesoft.schema.mule.http.CacheControlType;
import org.mulesoft.schema.mule.http.CookieType;
import org.mulesoft.schema.mule.http.HeaderType;


/**
 * <p>Java class for annotatedType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="annotatedType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="annotations" type="{http://www.mulesoft.org/schema/mule/core}annotationsType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "annotatedType", propOrder = {
    "annotations"
})
@XmlSeeAlso({
    MuleType.class,
    GlobalPropertyType.class,
    ConfigurationType.class,
    NotificationManagerType.class,
    AbstractAgentType.class,
    AbstractQueueStoreType.class,
    SubFlowType.class,
    AbstractPollOverrideType.class,
    AbstractModelType.class,
    FlowType.class,
    ProcessingStrategyType.class,
    AbstractTransactionManagerType.class,
    AbstractSchedulerType.class,
    AbstractInterceptorStackType.class,
    AbstractSecurityManagerType.class,
    org.mulesoft.schema.mule.core.RollbackExceptionStrategyType.OnRedeliveryAttemptsExceeded.class,
    AbstractRedeliveryPolicyType.class,
    CustomRouterResolverType.class,
    AbstractInboundRouterType.class,
    AbstractServiceType.class,
    ExceptionStrategyType.class,
    AbstractFlowConstructType.class,
    AggregationStrategyType.class,
    AbstractOutboundRouterType.class,
    AbstractMessageSourceType.class,
    AbstractHttpRequestAuthenticationProvider.class,
    AbstractConfigurationExtensionType.class,
    HeaderType.class,
    CookieType.class,
    CacheControlType.class,
    AbstractConnectorType.class,
    AbstractInboundEndpointType.class,
    AbstractOutboundEndpointType.class,
    AbstractGlobalEndpointType.class,
    AbstractMessageProcessorType.class,
    AbstractExtensionType.class
})
public abstract class AnnotatedType {

    protected AnnotationsType annotations;
    @XmlAnyAttribute
    private Map<QName, String> otherAttributes = new HashMap<QName, String>();

    /**
     * Gets the value of the annotations property.
     * 
     * @return
     *     possible object is
     *     {@link AnnotationsType }
     *     
     */
    public AnnotationsType getAnnotations() {
        return annotations;
    }

    /**
     * Sets the value of the annotations property.
     * 
     * @param value
     *     allowed object is
     *     {@link AnnotationsType }
     *     
     */
    public void setAnnotations(AnnotationsType value) {
        this.annotations = value;
    }

    /**
     * Gets a map that contains attributes that aren't bound to any typed property on this class.
     * 
     * <p>
     * the map is keyed by the name of the attribute and 
     * the value is the string value of the attribute.
     * 
     * the map returned by this method is live, and you can add new attribute
     * by updating the map directly. Because of this design, there's no setter.
     * 
     * 
     * @return
     *     always non-null
     */
    public Map<QName, String> getOtherAttributes() {
        return otherAttributes;
    }

}
