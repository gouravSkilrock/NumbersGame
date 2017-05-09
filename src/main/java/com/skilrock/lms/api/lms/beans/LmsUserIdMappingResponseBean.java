
package com.skilrock.lms.api.lms.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for LmsUserIdMappingResponseBean complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="LmsUserIdMappingResponseBean">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="errorCode" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="errorMessage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="lmsUserIdMappingRequestBean" type="{http://beans.lms.api.lms.skilrock.com}LmsUserIdMappingRequestBean" minOccurs="0"/>
 *         &lt;element name="success" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LmsUserIdMappingResponseBean", propOrder = {
    "errorCode",
    "errorMessage",
    "lmsUserIdMappingRequestBean",
    "success"
})
public class LmsUserIdMappingResponseBean {

    protected Integer errorCode;
    @XmlElement(nillable = true)
    protected String errorMessage;
    @XmlElement(nillable = true)
    protected LmsUserIdMappingRequestBean lmsUserIdMappingRequestBean;
    protected Boolean success;

    /**
     * Gets the value of the errorCode property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getErrorCode() {
        return errorCode;
    }

    /**
     * Sets the value of the errorCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setErrorCode(Integer value) {
        this.errorCode = value;
    }

    /**
     * Gets the value of the errorMessage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Sets the value of the errorMessage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErrorMessage(String value) {
        this.errorMessage = value;
    }

    /**
     * Gets the value of the lmsUserIdMappingRequestBean property.
     * 
     * @return
     *     possible object is
     *     {@link LmsUserIdMappingRequestBean }
     *     
     */
    public LmsUserIdMappingRequestBean getLmsUserIdMappingRequestBean() {
        return lmsUserIdMappingRequestBean;
    }

    /**
     * Sets the value of the lmsUserIdMappingRequestBean property.
     * 
     * @param value
     *     allowed object is
     *     {@link LmsUserIdMappingRequestBean }
     *     
     */
    public void setLmsUserIdMappingRequestBean(LmsUserIdMappingRequestBean value) {
        this.lmsUserIdMappingRequestBean = value;
    }

    /**
     * Gets the value of the success property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSuccess() {
        return success;
    }

    /**
     * Sets the value of the success property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSuccess(Boolean value) {
        this.success = value;
    }

}
