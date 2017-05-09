
package com.skilrock.lms.api.lms;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.skilrock.lms.api.lms.beans.LmsUserIdMappingRequestBean;


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
 *         &lt;element name="in0" type="{http://beans.lms.api.lms.skilrock.com}LmsUserIdMappingRequestBean"/>
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
    "in0"
})
@XmlRootElement(name = "getRandomMappingIdFromThirdParty")
public class GetRandomMappingIdFromThirdParty {

    @XmlElement(required = true, nillable = true)
    protected LmsUserIdMappingRequestBean in0;

    /**
     * Gets the value of the in0 property.
     * 
     * @return
     *     possible object is
     *     {@link LmsUserIdMappingRequestBean }
     *     
     */
    public LmsUserIdMappingRequestBean getIn0() {
        return in0;
    }

    /**
     * Sets the value of the in0 property.
     * 
     * @param value
     *     allowed object is
     *     {@link LmsUserIdMappingRequestBean }
     *     
     */
    public void setIn0(LmsUserIdMappingRequestBean value) {
        this.in0 = value;
    }

}
