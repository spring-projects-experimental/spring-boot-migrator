
package org.mulesoft.schema.mule.core;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import org.mulesoft.schema.mule.db.DatabaseConfigType;
import org.mulesoft.schema.mule.db.TemplateSqlDefinitionType;
import org.mulesoft.schema.mule.http.ListenerConfigType;
import org.mulesoft.schema.mule.http.ProxyType;
import org.mulesoft.schema.mule.http.RequestBuilderType;
import org.mulesoft.schema.mule.http.RequestConfigType;
import org.mulesoft.schema.mule.http.ResponseBuilderType;
import org.mulesoft.schema.mule.jms.AbstractJndiNameResolverType;
import org.mulesoft.schema.mule.jms.ConnectionFactoryPoolType;
import org.mulesoft.schema.mule.scripting.GroovyRefreshableType;
import org.mulesoft.schema.mule.tcp.TcpAbstractSocketPropertiesType;
import org.mulesoft.schema.mule.tls.TlsContextType;


/**
 * <p>Java class for abstractExtensionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="abstractExtensionType"&gt;
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
@XmlType(name = "abstractExtensionType")
@XmlSeeAlso({
    AbstractCachingStrategyType.class,
    TemplateSqlDefinitionType.class,
    DatabaseConfigType.class,
    ListenerConfigType.class,
    RequestConfigType.class,
    ResponseBuilderType.class,
    RequestBuilderType.class,
    ProxyType.class,
    TcpAbstractSocketPropertiesType.class,
    TlsContextType.class,
    GroovyRefreshableType.class,
    AbstractJndiNameResolverType.class,
    ConnectionFactoryPoolType.class
})
public class AbstractExtensionType
    extends AnnotatedType
{


}
