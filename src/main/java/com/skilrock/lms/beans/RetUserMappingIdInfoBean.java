
package com.skilrock.lms.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RetUserMappingIdInfoBean complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RetUserMappingIdInfoBean">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="advMappingId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="codeExpiryTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="newGenerationTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="userId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="userMappingId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RetUserMappingIdInfoBean", propOrder = {
    "advMappingId",
    "codeExpiryTime",
    "newGenerationTime",
    "userId",
    "userMappingId"
})
public class RetUserMappingIdInfoBean {

    @XmlElement(nillable = true)
    protected Integer advMappingId;
    @XmlElement(nillable = true)
    protected String codeExpiryTime;
    @XmlElement(nillable = true)
    protected String newGenerationTime;
    @XmlElement(nillable = true)
    protected Integer userId;
    @XmlElement(nillable = true)
    protected Integer userMappingId;

    /**
     * Gets the value of the advMappingId property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getAdvMappingId() {
        return advMappingId;
    }

    /**
     * Sets the value of the advMappingId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setAdvMappingId(Integer value) {
        this.advMappingId = value;
    }

    /**
     * Gets the value of the codeExpiryTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeExpiryTime() {
        return codeExpiryTime;
    }

    /**
     * Sets the value of the codeExpiryTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeExpiryTime(String value) {
        this.codeExpiryTime = value;
    }

    /**
     * Gets the value of the newGenerationTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNewGenerationTime() {
        return newGenerationTime;
    }

    /**
     * Sets the value of the newGenerationTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNewGenerationTime(String value) {
        this.newGenerationTime = value;
    }

    /**
     * Gets the value of the userId property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * Sets the value of the userId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setUserId(Integer value) {
        this.userId = value;
    }

    /**
     * Gets the value of the userMappingId property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getUserMappingId() {
        return userMappingId;
    }

    /**
     * Sets the value of the userMappingId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setUserMappingId(Integer value) {
        this.userMappingId = value;
    }

}
