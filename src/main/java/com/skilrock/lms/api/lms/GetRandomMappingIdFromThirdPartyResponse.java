
package com.skilrock.lms.api.lms;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.skilrock.lms.api.lms.beans.LmsUserIdMappingResponseBean;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="out" type="{http://beans.lms.api.lms.skilrock.com}LmsUserIdMappingResponseBean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "out"
})
@XmlRootElement(name = "getRandomMappingIdFromThirdPartyResponse")
public class GetRandomMappingIdFromThirdPartyResponse {

    @XmlElement(required = true, nillable = true)
    protected LmsUserIdMappingResponseBean out;

    /**
     * Gets the value of the out property.
     * 
     * @return
     *     possible object is
     *     {@link LmsUserIdMappingResponseBean }
     *     
     */
    public LmsUserIdMappingResponseBean getOut() {
        return out;
    }

    /**
     * Sets the value of the out property.
     * 
     * @param value
     *     allowed object is
     *     {@link LmsUserIdMappingResponseBean }
     *     
     */
    public void setOut(LmsUserIdMappingResponseBean value) {
        this.out = value;
    }

}
