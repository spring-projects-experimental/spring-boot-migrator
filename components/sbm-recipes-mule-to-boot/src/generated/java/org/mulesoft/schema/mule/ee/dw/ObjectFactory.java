
package org.mulesoft.schema.mule.ee.dw;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.mulesoft.schema.mule.ee.dw package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _TransformMessage_QNAME = new QName("http://www.mulesoft.org/schema/mule/ee/dw", "transform-message");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.mulesoft.schema.mule.ee.dw
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link TransformMessageType }
     * 
     */
    public TransformMessageType createTransformMessageType() {
        return new TransformMessageType();
    }

    /**
     * Create an instance of {@link TransformMessageType.InputPayload }
     * 
     */
    public TransformMessageType.InputPayload createTransformMessageTypeInputPayload() {
        return new TransformMessageType.InputPayload();
    }

    /**
     * Create an instance of {@link TransformMessageType.InputVariable }
     * 
     */
    public TransformMessageType.InputVariable createTransformMessageTypeInputVariable() {
        return new TransformMessageType.InputVariable();
    }

    /**
     * Create an instance of {@link TransformMessageType.InputSessionVariable }
     * 
     */
    public TransformMessageType.InputSessionVariable createTransformMessageTypeInputSessionVariable() {
        return new TransformMessageType.InputSessionVariable();
    }

    /**
     * Create an instance of {@link TransformMessageType.InputInboundProperty }
     * 
     */
    public TransformMessageType.InputInboundProperty createTransformMessageTypeInputInboundProperty() {
        return new TransformMessageType.InputInboundProperty();
    }

    /**
     * Create an instance of {@link TransformMessageType.InputOutboundProperty }
     * 
     */
    public TransformMessageType.InputOutboundProperty createTransformMessageTypeInputOutboundProperty() {
        return new TransformMessageType.InputOutboundProperty();
    }

    /**
     * Create an instance of {@link TransformMessageType.SetPayload }
     * 
     */
    public TransformMessageType.SetPayload createTransformMessageTypeSetPayload() {
        return new TransformMessageType.SetPayload();
    }

    /**
     * Create an instance of {@link TransformMessageType.SetVariable }
     * 
     */
    public TransformMessageType.SetVariable createTransformMessageTypeSetVariable() {
        return new TransformMessageType.SetVariable();
    }

    /**
     * Create an instance of {@link TransformMessageType.SetSessionVariable }
     * 
     */
    public TransformMessageType.SetSessionVariable createTransformMessageTypeSetSessionVariable() {
        return new TransformMessageType.SetSessionVariable();
    }

    /**
     * Create an instance of {@link TransformMessageType.SetProperty }
     * 
     */
    public TransformMessageType.SetProperty createTransformMessageTypeSetProperty() {
        return new TransformMessageType.SetProperty();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TransformMessageType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link TransformMessageType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.mulesoft.org/schema/mule/ee/dw", name = "transform-message", substitutionHeadNamespace = "http://www.mulesoft.org/schema/mule/core", substitutionHeadName = "abstract-message-processor")
    public JAXBElement<TransformMessageType> createTransformMessage(TransformMessageType value) {
        return new JAXBElement<TransformMessageType>(_TransformMessage_QNAME, TransformMessageType.class, null, value);
    }

}
