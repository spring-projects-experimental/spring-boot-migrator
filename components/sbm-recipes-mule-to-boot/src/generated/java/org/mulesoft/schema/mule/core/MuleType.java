
package org.mulesoft.schema.mule.core;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlType;
import org.mulesoft.schema.mule.amqp.AmqpConnectorType;
import org.mulesoft.schema.mule.db.AbstractUserAndPasswordDatabaseConfigType;
import org.mulesoft.schema.mule.db.MySqlDatabaseConfigType;
import org.mulesoft.schema.mule.db.OracleDatabaseConfigType;
import org.mulesoft.schema.mule.db.TemplateSqlDefinitionType;
import org.mulesoft.schema.mule.ee.wmq.WmqConnectorType;
import org.mulesoft.schema.mule.http.GlobalNtlmProxyType;
import org.mulesoft.schema.mule.http.GlobalProxyType;
import org.mulesoft.schema.mule.http.GlobalRequestBuilderType;
import org.mulesoft.schema.mule.http.GlobalResponseBuilderType;
import org.mulesoft.schema.mule.http.HttpConnectorType;
import org.mulesoft.schema.mule.http.HttpPollingConnectorType;
import org.mulesoft.schema.mule.http.ListenerConfigType;
import org.mulesoft.schema.mule.http.RequestConfigType;
import org.mulesoft.schema.mule.jms.ActiveMqConnectorType;
import org.mulesoft.schema.mule.jms.ConnectionFactoryPoolType;
import org.mulesoft.schema.mule.jms.CustomConnector;
import org.mulesoft.schema.mule.jms.GenericConnectorType;
import org.mulesoft.schema.mule.jms.MuleMqConnectorType;
import org.mulesoft.schema.mule.jms.PropertyFilter;
import org.mulesoft.schema.mule.jms.VendorJmsConnectorType;
import org.mulesoft.schema.mule.tcp.PollingTcpConnectorType;
import org.mulesoft.schema.mule.tcp.TcpClientSocketPropertiesType;
import org.mulesoft.schema.mule.tcp.TcpConnectorType;
import org.mulesoft.schema.mule.tcp.TcpServerSocketPropertiesType;
import org.mulesoft.schema.mule.tls.Connector;
import org.mulesoft.schema.mule.tls.TlsContextType;
import org.springframework.schema.beans.Bean;
import org.springframework.schema.beans.Beans;
import org.springframework.schema.beans.Ref;
import org.springframework.schema.context.PropertyPlaceholder;


/**
 * <p>Java class for muleType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="muleType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}annotatedType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="description" type="{http://www.mulesoft.org/schema/mule/core}descriptionType" minOccurs="0"/&gt;
 *         &lt;choice maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;group ref="{http://www.mulesoft.org/schema/mule/core}springRootElements"/&gt;
 *           &lt;group ref="{http://www.mulesoft.org/schema/mule/core}muleRootElements"/&gt;
 *         &lt;/choice&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="version" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "muleType", propOrder = {
    "description",
    "beansOrBeanOrPropertyPlaceholder"
})
public class MuleType
    extends AnnotatedType
{

    protected DescriptionType description;
    @XmlElementRefs({
        @XmlElementRef(name = "beans", namespace = "http://www.springframework.org/schema/beans", type = Beans.class, required = false),
        @XmlElementRef(name = "bean", namespace = "http://www.springframework.org/schema/beans", type = Bean.class, required = false),
        @XmlElementRef(name = "property-placeholder", namespace = "http://www.springframework.org/schema/context", type = PropertyPlaceholder.class, required = false),
        @XmlElementRef(name = "ref", namespace = "http://www.springframework.org/schema/beans", type = Ref.class, required = false),
        @XmlElementRef(name = "global-property", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "configuration", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "notifications", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "abstract-extension", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "abstract-shared-extension", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "abstract-mixed-content-extension", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "abstract-agent", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "abstract-security-manager", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "abstract-transaction-manager", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "abstract-shared-transaction-manager", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "abstract-connector", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "abstract-shared-connector", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "abstract-global-endpoint", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "abstract-exception-strategy", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "abstract-flow-construct", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "flow", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "sub-flow", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "abstract-model", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "abstract-interceptor-stack", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "abstract-filter", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "abstract-transformer", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "processor-chain", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "custom-processor", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "abstract-empty-processor", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "invoke", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "set-payload", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "abstract-global-intercepting-message-processor", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "custom-queue-store", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "abstract-processing-strategy", namespace = "http://www.mulesoft.org/schema/mule/core", type = JAXBElement.class, required = false)
    })
    protected List<Object> beansOrBeanOrPropertyPlaceholder;
    @XmlAttribute(name = "version")
    protected String version;

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link DescriptionType }
     *     
     */
    public DescriptionType getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link DescriptionType }
     *     
     */
    public void setDescription(DescriptionType value) {
        this.description = value;
    }

    /**
     * Gets the value of the beansOrBeanOrPropertyPlaceholder property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the beansOrBeanOrPropertyPlaceholder property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBeansOrBeanOrPropertyPlaceholder().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Beans }
     * {@link Bean }
     * {@link PropertyPlaceholder }
     * {@link Ref }
     * {@link JAXBElement }{@code <}{@link GlobalPropertyType }{@code >}
     * {@link JAXBElement }{@code <}{@link ConfigurationType }{@code >}
     * {@link JAXBElement }{@code <}{@link NotificationManagerType }{@code >}
     * {@link JAXBElement }{@code <}{@link GlobalNtlmProxyType }{@code >}
     * {@link JAXBElement }{@code <}{@link TcpClientSocketPropertiesType }{@code >}
     * {@link JAXBElement }{@code <}{@link GlobalProxyType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractCachingStrategyType }{@code >}
     * {@link JAXBElement }{@code <}{@link TemplateSqlDefinitionType }{@code >}
     * {@link JAXBElement }{@code <}{@link TcpServerSocketPropertiesType }{@code >}
     * {@link JAXBElement }{@code <}{@link GlobalRequestBuilderType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractExtensionType }{@code >}
     * {@link JAXBElement }{@code <}{@link ListenerConfigType }{@code >}
     * {@link JAXBElement }{@code <}{@link ConnectionFactoryPoolType }{@code >}
     * {@link JAXBElement }{@code <}{@link TlsContextType }{@code >}
     * {@link JAXBElement }{@code <}{@link MySqlDatabaseConfigType }{@code >}
     * {@link JAXBElement }{@code <}{@link OracleDatabaseConfigType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractUserAndPasswordDatabaseConfigType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractUserAndPasswordDatabaseConfigType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractExtensionType }{@code >}
     * {@link JAXBElement }{@code <}{@link RequestConfigType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractExtensionType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractMixedContentExtensionType }{@code >}
     * {@link JAXBElement }{@code <}{@link CustomAgentType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractAgentType }{@code >}
     * {@link JAXBElement }{@code <}{@link SecurityManagerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractSecurityManagerType }{@code >}
     * {@link JAXBElement }{@code <}{@link CustomTransactionManagerType }{@code >}
     * {@link JAXBElement }{@code <}{@link TransactionManagerType }{@code >}
     * {@link JAXBElement }{@code <}{@link JndiTransactionManager }{@code >}
     * {@link JAXBElement }{@code <}{@link TransactionManagerType }{@code >}
     * {@link JAXBElement }{@code <}{@link JndiTransactionManagerType }{@code >}
     * {@link JAXBElement }{@code <}{@link TransactionManagerType }{@code >}
     * {@link JAXBElement }{@code <}{@link TransactionManagerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransactionManagerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransactionManagerType }{@code >}
     * {@link JAXBElement }{@code <}{@link VendorJmsConnectorType }{@code >}
     * {@link JAXBElement }{@code <}{@link Connector }{@code >}
     * {@link JAXBElement }{@code <}{@link VendorJmsConnectorType }{@code >}
     * {@link JAXBElement }{@code <}{@link TcpConnectorType }{@code >}
     * {@link JAXBElement }{@code <}{@link AmqpConnectorType }{@code >}
     * {@link JAXBElement }{@code <}{@link MuleMqConnectorType }{@code >}
     * {@link JAXBElement }{@code <}{@link MuleMqConnectorType }{@code >}
     * {@link JAXBElement }{@code <}{@link CustomConnectorType }{@code >}
     * {@link JAXBElement }{@code <}{@link PollingTcpConnectorType }{@code >}
     * {@link JAXBElement }{@code <}{@link HttpPollingConnectorType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractConnectorType }{@code >}
     * {@link JAXBElement }{@code <}{@link HttpConnectorType }{@code >}
     * {@link JAXBElement }{@code <}{@link WmqConnectorType }{@code >}
     * {@link JAXBElement }{@code <}{@link WmqConnectorType }{@code >}
     * {@link JAXBElement }{@code <}{@link GenericConnectorType }{@code >}
     * {@link JAXBElement }{@code <}{@link ActiveMqConnectorType }{@code >}
     * {@link JAXBElement }{@code <}{@link CustomConnector }{@code >}
     * {@link JAXBElement }{@code <}{@link ActiveMqConnectorType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractConnectorType }{@code >}
     * {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.amqp.GlobalEndpointType }{@code >}
     * {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.http.GlobalEndpointType }{@code >}
     * {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.tcp.GlobalEndpointType }{@code >}
     * {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.core.GlobalEndpointType }{@code >}
     * {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.jms.GlobalEndpointType }{@code >}
     * {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.ee.wmq.GlobalEndpointType }{@code >}
     * {@link JAXBElement }{@code <}{@link org.mulesoft.schema.mule.tls.GlobalEndpointType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractGlobalEndpointType }{@code >}
     * {@link JAXBElement }{@code <}{@link ReferenceExceptionStrategyType }{@code >}
     * {@link JAXBElement }{@code <}{@link ChoiceExceptionStrategyType }{@code >}
     * {@link JAXBElement }{@code <}{@link RollbackExceptionStrategyType }{@code >}
     * {@link JAXBElement }{@code <}{@link CatchExceptionStrategyType }{@code >}
     * {@link JAXBElement }{@code <}{@link CustomExceptionStrategyType }{@code >}
     * {@link JAXBElement }{@code <}{@link ExceptionStrategyType }{@code >}
     * {@link JAXBElement }{@code <}{@link BridgeType }{@code >}
     * {@link JAXBElement }{@code <}{@link SimpleServiceType }{@code >}
     * {@link JAXBElement }{@code <}{@link ValidatorType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractFlowConstructType }{@code >}
     * {@link JAXBElement }{@code <}{@link FlowType }{@code >}
     * {@link JAXBElement }{@code <}{@link SubFlowType }{@code >}
     * {@link JAXBElement }{@code <}{@link DefaultModelType }{@code >}
     * {@link JAXBElement }{@code <}{@link SedaModelType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractModelType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractInterceptorStackType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractInterceptorStackType }{@code >}
     * {@link JAXBElement }{@code <}{@link TypeFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link RegexFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link CustomFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link CollectionFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link RefFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link CollectionFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link ScopedPropertyFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link ExpressionFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link PropertyFilter }{@code >}
     * {@link JAXBElement }{@code <}{@link TypeFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link WildcardFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link UnitaryFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link WildcardFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link CommonFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link RemoveAttachmentType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link BeanBuilderTransformer }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link SetPropertyType }{@code >}
     * {@link JAXBElement }{@code <}{@link RefTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link ValueExtractorTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link EncryptionTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link CopyAttachmentType }{@code >}
     * {@link JAXBElement }{@code <}{@link ParseTemplateTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link RemoveVariableType }{@code >}
     * {@link JAXBElement }{@code <}{@link SetVariableType }{@code >}
     * {@link JAXBElement }{@code <}{@link CopyPropertiesType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link MessagePropertiesTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link SetVariableType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link ExpressionTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link RemoveVariableType }{@code >}
     * {@link JAXBElement }{@code <}{@link SetAttachmentType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link CustomTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AppendStringTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link EncryptionTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link RemovePropertyType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link CommonTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link MessageProcessorChainType }{@code >}
     * {@link JAXBElement }{@code <}{@link CustomMessageProcessorType }{@code >}
     * {@link JAXBElement }{@code <}{@link GlobalResponseBuilderType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractEmptyMessageProcessorType }{@code >}
     * {@link JAXBElement }{@code <}{@link InvokeType }{@code >}
     * {@link JAXBElement }{@code <}{@link SetPayloadTransformerType }{@code >}
     * {@link JAXBElement }{@code <}{@link CombineCollectionsTransformer }{@code >}
     * {@link JAXBElement }{@code <}{@link IdempotentSecureHashMessageFilter }{@code >}
     * {@link JAXBElement }{@code <}{@link MessageFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link IdempotentMessageFilterType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractGlobalInterceptingMessageProcessorType }{@code >}
     * {@link JAXBElement }{@code <}{@link CustomQueueStoreType }{@code >}
     * {@link JAXBElement }{@code <}{@link NonBlockingProcessingStrategy }{@code >}
     * {@link JAXBElement }{@code <}{@link QueuedAsynchronousProcessingStrategy }{@code >}
     * {@link JAXBElement }{@code <}{@link AsynchronousProcessingStrategy }{@code >}
     * {@link JAXBElement }{@code <}{@link QueuedAsynchronousProcessingStrategy }{@code >}
     * {@link JAXBElement }{@code <}{@link AsynchronousProcessingStrategy }{@code >}
     * {@link JAXBElement }{@code <}{@link CustomProcessingStrategy }{@code >}
     * {@link JAXBElement }{@code <}{@link ProcessingStrategyType }{@code >}
     * 
     * 
     */
    public List<Object> getBeansOrBeanOrPropertyPlaceholder() {
        if (beansOrBeanOrPropertyPlaceholder == null) {
            beansOrBeanOrPropertyPlaceholder = new ArrayList<Object>();
        }
        return this.beansOrBeanOrPropertyPlaceholder;
    }

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersion(String value) {
        this.version = value;
    }

}
