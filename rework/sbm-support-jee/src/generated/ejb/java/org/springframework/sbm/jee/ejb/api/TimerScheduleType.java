
/*
 * Copyright 2021 - 2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.sbm.jee.ejb.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for timer-scheduleType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="timer-scheduleType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="second" type="{http://xmlns.jcp.org/xml/ns/javaee}string" minOccurs="0"/&gt;
 *         &lt;element name="minute" type="{http://xmlns.jcp.org/xml/ns/javaee}string" minOccurs="0"/&gt;
 *         &lt;element name="hour" type="{http://xmlns.jcp.org/xml/ns/javaee}string" minOccurs="0"/&gt;
 *         &lt;element name="day-of-month" type="{http://xmlns.jcp.org/xml/ns/javaee}string" minOccurs="0"/&gt;
 *         &lt;element name="month" type="{http://xmlns.jcp.org/xml/ns/javaee}string" minOccurs="0"/&gt;
 *         &lt;element name="day-of-week" type="{http://xmlns.jcp.org/xml/ns/javaee}string" minOccurs="0"/&gt;
 *         &lt;element name="year" type="{http://xmlns.jcp.org/xml/ns/javaee}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}ID" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "timer-scheduleType", propOrder = {
    "second",
    "minute",
    "hour",
    "dayOfMonth",
    "month",
    "dayOfWeek",
    "year"
})
public class TimerScheduleType {

    protected org.springframework.sbm.jee.ejb.api.String second;
    protected org.springframework.sbm.jee.ejb.api.String minute;
    protected org.springframework.sbm.jee.ejb.api.String hour;
    @XmlElement(name = "day-of-month")
    protected org.springframework.sbm.jee.ejb.api.String dayOfMonth;
    protected org.springframework.sbm.jee.ejb.api.String month;
    @XmlElement(name = "day-of-week")
    protected org.springframework.sbm.jee.ejb.api.String dayOfWeek;
    protected org.springframework.sbm.jee.ejb.api.String year;
    @XmlAttribute(name = "id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected java.lang.String id;

    /**
     * Gets the value of the second property.
     * 
     * @return
     *     possible object is
     *     {@link org.springframework.sbm.jee.ejb.api.String }
     *     
     */
    public org.springframework.sbm.jee.ejb.api.String getSecond() {
        return second;
    }

    /**
     * Sets the value of the second property.
     * 
     * @param value
     *     allowed object is
     *     {@link org.springframework.sbm.jee.ejb.api.String }
     *     
     */
    public void setSecond(org.springframework.sbm.jee.ejb.api.String value) {
        this.second = value;
    }

    /**
     * Gets the value of the minute property.
     * 
     * @return
     *     possible object is
     *     {@link org.springframework.sbm.jee.ejb.api.String }
     *     
     */
    public org.springframework.sbm.jee.ejb.api.String getMinute() {
        return minute;
    }

    /**
     * Sets the value of the minute property.
     * 
     * @param value
     *     allowed object is
     *     {@link org.springframework.sbm.jee.ejb.api.String }
     *     
     */
    public void setMinute(org.springframework.sbm.jee.ejb.api.String value) {
        this.minute = value;
    }

    /**
     * Gets the value of the hour property.
     * 
     * @return
     *     possible object is
     *     {@link org.springframework.sbm.jee.ejb.api.String }
     *     
     */
    public org.springframework.sbm.jee.ejb.api.String getHour() {
        return hour;
    }

    /**
     * Sets the value of the hour property.
     * 
     * @param value
     *     allowed object is
     *     {@link org.springframework.sbm.jee.ejb.api.String }
     *     
     */
    public void setHour(org.springframework.sbm.jee.ejb.api.String value) {
        this.hour = value;
    }

    /**
     * Gets the value of the dayOfMonth property.
     * 
     * @return
     *     possible object is
     *     {@link org.springframework.sbm.jee.ejb.api.String }
     *     
     */
    public org.springframework.sbm.jee.ejb.api.String getDayOfMonth() {
        return dayOfMonth;
    }

    /**
     * Sets the value of the dayOfMonth property.
     * 
     * @param value
     *     allowed object is
     *     {@link org.springframework.sbm.jee.ejb.api.String }
     *     
     */
    public void setDayOfMonth(org.springframework.sbm.jee.ejb.api.String value) {
        this.dayOfMonth = value;
    }

    /**
     * Gets the value of the month property.
     * 
     * @return
     *     possible object is
     *     {@link org.springframework.sbm.jee.ejb.api.String }
     *     
     */
    public org.springframework.sbm.jee.ejb.api.String getMonth() {
        return month;
    }

    /**
     * Sets the value of the month property.
     * 
     * @param value
     *     allowed object is
     *     {@link org.springframework.sbm.jee.ejb.api.String }
     *     
     */
    public void setMonth(org.springframework.sbm.jee.ejb.api.String value) {
        this.month = value;
    }

    /**
     * Gets the value of the dayOfWeek property.
     * 
     * @return
     *     possible object is
     *     {@link org.springframework.sbm.jee.ejb.api.String }
     *     
     */
    public org.springframework.sbm.jee.ejb.api.String getDayOfWeek() {
        return dayOfWeek;
    }

    /**
     * Sets the value of the dayOfWeek property.
     * 
     * @param value
     *     allowed object is
     *     {@link org.springframework.sbm.jee.ejb.api.String }
     *     
     */
    public void setDayOfWeek(org.springframework.sbm.jee.ejb.api.String value) {
        this.dayOfWeek = value;
    }

    /**
     * Gets the value of the year property.
     * 
     * @return
     *     possible object is
     *     {@link org.springframework.sbm.jee.ejb.api.String }
     *     
     */
    public org.springframework.sbm.jee.ejb.api.String getYear() {
        return year;
    }

    /**
     * Sets the value of the year property.
     * 
     * @param value
     *     allowed object is
     *     {@link org.springframework.sbm.jee.ejb.api.String }
     *     
     */
    public void setYear(org.springframework.sbm.jee.ejb.api.String value) {
        this.year = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String }
     *     
     */
    public java.lang.String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String }
     *     
     */
    public void setId(java.lang.String value) {
        this.id = value;
    }

}
