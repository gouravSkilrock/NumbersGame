
package com.skilrock.lms.api.lms.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import com.skilrock.lms.api.lms.AnyType2AnyTypeMap;
import com.skilrock.lms.api.lms.ArrayOfInt;
import com.skilrock.lms.beans.ArrayOfRetUserMappingIdInfoBean;


/**
 * <p>Java class for LmsUserIdMappingRequestBean complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="LmsUserIdMappingRequestBean">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="activity" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="advCodeExpiryDateTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="advGenerationDateTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="advMappingIdList" type="{http://lms.api.lms.skilrock.com}ArrayOfInt" minOccurs="0"/>
 *         &lt;element name="advUserMappingIdList" type="{http://beans.lms.skilrock.com}ArrayOfRetUserMappingIdInfoBean" minOccurs="0"/>
 *         &lt;element name="advUserMappingIdMap" type="{http://lms.api.lms.skilrock.com}anyType2anyTypeMap" minOccurs="0"/>
 *         &lt;element name="all" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="doneByUserId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="exists" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="expiryDays" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="firstGeneration" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="generationTime" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="lastCodeExpiryDays" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="lastExpDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="lastSuccDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="mappingIdList" type="{http://lms.api.lms.skilrock.com}ArrayOfInt" minOccurs="0"/>
 *         &lt;element name="newCodeExpiryDateTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="newGenerationDateTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="requesInitiateTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="specific" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="systemUserName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="systemUserPassword" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="thirdPartyGeneration" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="userId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="userIdList" type="{http://lms.api.lms.skilrock.com}ArrayOfInt" minOccurs="0"/>
 *         &lt;element name="userMappingId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="userMappingIdMap" type="{http://lms.api.lms.skilrock.com}anyType2anyTypeMap" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LmsUserIdMappingRequestBean", propOrder = {
    "activity",
    "advCodeExpiryDateTime",
    "advGenerationDateTime",
    "advMappingIdList",
    "advUserMappingIdList",
    "advUserMappingIdMap",
    "all",
    "doneByUserId",
    "exists",
    "expiryDays",
    "firstGeneration",
    "generationTime",
    "lastCodeExpiryDays",
    "lastExpDate",
    "lastSuccDate",
    "mappingIdList",
    "newCodeExpiryDateTime",
    "newGenerationDateTime",
    "requesInitiateTime",
    "specific",
    "systemUserName",
    "systemUserPassword",
    "thirdPartyGeneration",
    "userId",
    "userIdList",
    "userMappingId",
    "userMappingIdMap"
})
public class LmsUserIdMappingRequestBean {

    @XmlElement(nillable = true)
    protected String activity;
    @XmlElement(nillable = true)
    protected String advCodeExpiryDateTime;
    @XmlElement(nillable = true)
    protected String advGenerationDateTime;
    @XmlElement(nillable = true)
    protected ArrayOfInt advMappingIdList;
    @XmlElement(nillable = true)
    protected ArrayOfRetUserMappingIdInfoBean advUserMappingIdList;
    @XmlElement(nillable = true)
    protected AnyType2AnyTypeMap advUserMappingIdMap;
    protected Boolean all;
    protected Integer doneByUserId;
    protected Boolean exists;
    protected Integer expiryDays;
    protected Boolean firstGeneration;
    protected Long generationTime;
    protected Integer lastCodeExpiryDays;
    @XmlElement(nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar lastExpDate;
    @XmlElement(nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar lastSuccDate;
    @XmlElement(nillable = true)
    protected ArrayOfInt mappingIdList;
    @XmlElement(nillable = true)
    protected String newCodeExpiryDateTime;
    @XmlElement(nillable = true)
    protected String newGenerationDateTime;
    @XmlElement(nillable = true)
    protected String requesInitiateTime;
    protected Boolean specific;
    @XmlElement(nillable = true)
    protected String systemUserName;
    @XmlElement(nillable = true)
    protected String systemUserPassword;
    protected Boolean thirdPartyGeneration;
    protected Integer userId;
    @XmlElement(nillable = true)
    protected ArrayOfInt userIdList;
    protected Integer userMappingId;
    @XmlElement(nillable = true)
    protected AnyType2AnyTypeMap userMappingIdMap;

    /**
     * Gets the value of the activity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getActivity() {
        return activity;
    }

    /**
     * Sets the value of the activity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setActivity(String value) {
        this.activity = value;
    }

    /**
     * Gets the value of the advCodeExpiryDateTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdvCodeExpiryDateTime() {
        return advCodeExpiryDateTime;
    }

    /**
     * Sets the value of the advCodeExpiryDateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdvCodeExpiryDateTime(String value) {
        this.advCodeExpiryDateTime = value;
    }

    /**
     * Gets the value of the advGenerationDateTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdvGenerationDateTime() {
        return advGenerationDateTime;
    }

    /**
     * Sets the value of the advGenerationDateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdvGenerationDateTime(String value) {
        this.advGenerationDateTime = value;
    }

    /**
     * Gets the value of the advMappingIdList property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfInt }
     *     
     */
    public ArrayOfInt getAdvMappingIdList() {
        return advMappingIdList;
    }

    /**
     * Sets the value of the advMappingIdList property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfInt }
     *     
     */
    public void setAdvMappingIdList(ArrayOfInt value) {
        this.advMappingIdList = value;
    }

    /**
     * Gets the value of the advUserMappingIdList property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfRetUserMappingIdInfoBean }
     *     
     */
    public ArrayOfRetUserMappingIdInfoBean getAdvUserMappingIdList() {
        return advUserMappingIdList;
    }

    /**
     * Sets the value of the advUserMappingIdList property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfRetUserMappingIdInfoBean }
     *     
     */
    public void setAdvUserMappingIdList(ArrayOfRetUserMappingIdInfoBean value) {
        this.advUserMappingIdList = value;
    }

    /**
     * Gets the value of the advUserMappingIdMap property.
     * 
     * @return
     *     possible object is
     *     {@link AnyType2AnyTypeMap }
     *     
     */
    public AnyType2AnyTypeMap getAdvUserMappingIdMap() {
        return advUserMappingIdMap;
    }

    /**
     * Sets the value of the advUserMappingIdMap property.
     * 
     * @param value
     *     allowed object is
     *     {@link AnyType2AnyTypeMap }
     *     
     */
    public void setAdvUserMappingIdMap(AnyType2AnyTypeMap value) {
        this.advUserMappingIdMap = value;
    }

    /**
     * Gets the value of the all property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isAll() {
        return all;
    }

    /**
     * Sets the value of the all property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setAll(Boolean value) {
        this.all = value;
    }

    /**
     * Gets the value of the doneByUserId property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getDoneByUserId() {
        return doneByUserId;
    }

    /**
     * Sets the value of the doneByUserId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setDoneByUserId(Integer value) {
        this.doneByUserId = value;
    }

    /**
     * Gets the value of the exists property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isExists() {
        return exists;
    }

    /**
     * Sets the value of the exists property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setExists(Boolean value) {
        this.exists = value;
    }

    /**
     * Gets the value of the expiryDays property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getExpiryDays() {
        return expiryDays;
    }

    /**
     * Sets the value of the expiryDays property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setExpiryDays(Integer value) {
        this.expiryDays = value;
    }

    /**
     * Gets the value of the firstGeneration property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isFirstGeneration() {
        return firstGeneration;
    }

    /**
     * Sets the value of the firstGeneration property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setFirstGeneration(Boolean value) {
        this.firstGeneration = value;
    }

    /**
     * Gets the value of the generationTime property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getGenerationTime() {
        return generationTime;
    }

    /**
     * Sets the value of the generationTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setGenerationTime(Long value) {
        this.generationTime = value;
    }

    /**
     * Gets the value of the lastCodeExpiryDays property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getLastCodeExpiryDays() {
        return lastCodeExpiryDays;
    }

    /**
     * Sets the value of the lastCodeExpiryDays property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setLastCodeExpiryDays(Integer value) {
        this.lastCodeExpiryDays = value;
    }

    /**
     * Gets the value of the lastExpDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getLastExpDate() {
        return lastExpDate;
    }

    /**
     * Sets the value of the lastExpDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setLastExpDate(XMLGregorianCalendar value) {
        this.lastExpDate = value;
    }

    /**
     * Gets the value of the lastSuccDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getLastSuccDate() {
        return lastSuccDate;
    }

    /**
     * Sets the value of the lastSuccDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setLastSuccDate(XMLGregorianCalendar value) {
        this.lastSuccDate = value;
    }

    /**
     * Gets the value of the mappingIdList property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfInt }
     *     
     */
    public ArrayOfInt getMappingIdList() {
        return mappingIdList;
    }

    /**
     * Sets the value of the mappingIdList property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfInt }
     *     
     */
    public void setMappingIdList(ArrayOfInt value) {
        this.mappingIdList = value;
    }

    /**
     * Gets the value of the newCodeExpiryDateTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNewCodeExpiryDateTime() {
        return newCodeExpiryDateTime;
    }

    /**
     * Sets the value of the newCodeExpiryDateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNewCodeExpiryDateTime(String value) {
        this.newCodeExpiryDateTime = value;
    }

    /**
     * Gets the value of the newGenerationDateTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNewGenerationDateTime() {
        return newGenerationDateTime;
    }

    /**
     * Sets the value of the newGenerationDateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNewGenerationDateTime(String value) {
        this.newGenerationDateTime = value;
    }

    /**
     * Gets the value of the requesInitiateTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequesInitiateTime() {
        return requesInitiateTime;
    }

    /**
     * Sets the value of the requesInitiateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequesInitiateTime(String value) {
        this.requesInitiateTime = value;
    }

    /**
     * Gets the value of the specific property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSpecific() {
        return specific;
    }

    /**
     * Sets the value of the specific property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSpecific(Boolean value) {
        this.specific = value;
    }

    /**
     * Gets the value of the systemUserName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSystemUserName() {
        return systemUserName;
    }

    /**
     * Sets the value of the systemUserName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSystemUserName(String value) {
        this.systemUserName = value;
    }

    /**
     * Gets the value of the systemUserPassword property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSystemUserPassword() {
        return systemUserPassword;
    }

    /**
     * Sets the value of the systemUserPassword property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSystemUserPassword(String value) {
        this.systemUserPassword = value;
    }

    /**
     * Gets the value of the thirdPartyGeneration property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isThirdPartyGeneration() {
        return thirdPartyGeneration;
    }

    /**
     * Sets the value of the thirdPartyGeneration property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setThirdPartyGeneration(Boolean value) {
        this.thirdPartyGeneration = value;
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
     * Gets the value of the userIdList property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfInt }
     *     
     */
    public ArrayOfInt getUserIdList() {
        return userIdList;
    }

    /**
     * Sets the value of the userIdList property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfInt }
     *     
     */
    public void setUserIdList(ArrayOfInt value) {
        this.userIdList = value;
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

    /**
     * Gets the value of the userMappingIdMap property.
     * 
     * @return
     *     possible object is
     *     {@link AnyType2AnyTypeMap }
     *     
     */
    public AnyType2AnyTypeMap getUserMappingIdMap() {
        return userMappingIdMap;
    }

    /**
     * Sets the value of the userMappingIdMap property.
     * 
     * @param value
     *     allowed object is
     *     {@link AnyType2AnyTypeMap }
     *     
     */
    public void setUserMappingIdMap(AnyType2AnyTypeMap value) {
        this.userMappingIdMap = value;
    }

}
