
package org.mulesoft.schema.mule.core;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for asynchronousThreadingProfileType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="asynchronousThreadingProfileType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.mulesoft.org/schema/mule/core}abstractServiceThreadingProfileType"&gt;
 *       &lt;attGroup ref="{http://www.mulesoft.org/schema/mule/core}commonThreadPoolAttributes"/&gt;
 *       &lt;attribute name="maxThreadsActive" type="{http://www.mulesoft.org/schema/mule/core}substitutableInt" /&gt;
 *       &lt;attribute name="maxThreadsIdle" type="{http://www.mulesoft.org/schema/mule/core}substitutableInt" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "asynchronousThreadingProfileType")
@XmlSeeAlso({
    ThreadingProfileType.class
})
public class AsynchronousThreadingProfileType
    extends AbstractServiceThreadingProfileType
{

    @XmlAttribute(name = "maxThreadsActive")
    protected String maxThreadsActive;
    @XmlAttribute(name = "maxThreadsIdle")
    protected String maxThreadsIdle;
    @XmlAttribute(name = "threadTTL")
    protected String threadTTL;
    @XmlAttribute(name = "poolExhaustedAction")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String poolExhaustedAction;
    @XmlAttribute(name = "threadWaitTimeout")
    protected String threadWaitTimeout;
    @XmlAttribute(name = "maxBufferSize")
    protected String maxBufferSize;

    /**
     * Gets the value of the maxThreadsActive property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaxThreadsActive() {
        return maxThreadsActive;
    }

    /**
     * Sets the value of the maxThreadsActive property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaxThreadsActive(String value) {
        this.maxThreadsActive = value;
    }

    /**
     * Gets the value of the maxThreadsIdle property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaxThreadsIdle() {
        return maxThreadsIdle;
    }

    /**
     * Sets the value of the maxThreadsIdle property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaxThreadsIdle(String value) {
        this.maxThreadsIdle = value;
    }

    /**
     * Gets the value of the threadTTL property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getThreadTTL() {
        return threadTTL;
    }

    /**
     * Sets the value of the threadTTL property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setThreadTTL(String value) {
        this.threadTTL = value;
    }

    /**
     * Gets the value of the poolExhaustedAction property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPoolExhaustedAction() {
        return poolExhaustedAction;
    }

    /**
     * Sets the value of the poolExhaustedAction property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPoolExhaustedAction(String value) {
        this.poolExhaustedAction = value;
    }

    /**
     * Gets the value of the threadWaitTimeout property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getThreadWaitTimeout() {
        return threadWaitTimeout;
    }

    /**
     * Sets the value of the threadWaitTimeout property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setThreadWaitTimeout(String value) {
        this.threadWaitTimeout = value;
    }

    /**
     * Gets the value of the maxBufferSize property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaxBufferSize() {
        return maxBufferSize;
    }

    /**
     * Sets the value of the maxBufferSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaxBufferSize(String value) {
        this.maxBufferSize = value;
    }

}
