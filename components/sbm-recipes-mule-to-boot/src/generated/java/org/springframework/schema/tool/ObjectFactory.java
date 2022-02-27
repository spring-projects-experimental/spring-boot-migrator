
package org.springframework.schema.tool;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.springframework.schema.tool package. 
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


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.springframework.schema.tool
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Annotation }
     * 
     */
    public Annotation createAnnotation() {
        return new Annotation();
    }

    /**
     * Create an instance of {@link TypedParameterType }
     * 
     */
    public TypedParameterType createTypedParameterType() {
        return new TypedParameterType();
    }

    /**
     * Create an instance of {@link AssignableToType }
     * 
     */
    public AssignableToType createAssignableToType() {
        return new AssignableToType();
    }

    /**
     * Create an instance of {@link ExportsType }
     * 
     */
    public ExportsType createExportsType() {
        return new ExportsType();
    }

    /**
     * Create an instance of {@link RegistersScopeType }
     * 
     */
    public RegistersScopeType createRegistersScopeType() {
        return new RegistersScopeType();
    }

    /**
     * Create an instance of {@link ExpectedMethodType }
     * 
     */
    public ExpectedMethodType createExpectedMethodType() {
        return new ExpectedMethodType();
    }

}
