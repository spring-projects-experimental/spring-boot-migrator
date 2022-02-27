
package org.mulesoft.schema.mule.core;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 *                 Allows a lifecycle adaptor factory to be implemented, which allows an alternative custom lifecycle adaptor to be used if required instead of the default implementation that propagates the Mule lifecycle to component implementations.
 *             
 * 
 * <p>Java class for abstractLifecycleAdapterFactory complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="abstractLifecycleAdapterFactory"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "abstractLifecycleAdapterFactory")
@XmlSeeAlso({
    CustomLifecycleAdapterFactory.class
})
public class AbstractLifecycleAdapterFactory {


}
