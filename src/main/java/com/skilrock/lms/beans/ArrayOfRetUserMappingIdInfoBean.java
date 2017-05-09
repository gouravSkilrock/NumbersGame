
package com.skilrock.lms.beans;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfRetUserMappingIdInfoBean complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfRetUserMappingIdInfoBean">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="RetUserMappingIdInfoBean" type="{http://beans.lms.skilrock.com}RetUserMappingIdInfoBean" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfRetUserMappingIdInfoBean", propOrder = {
    "retUserMappingIdInfoBeen"
})
public class ArrayOfRetUserMappingIdInfoBean {

    @XmlElement(name = "RetUserMappingIdInfoBean", nillable = true)
    protected List<RetUserMappingIdInfoBean> retUserMappingIdInfoBeen;

    /**
     * Gets the value of the retUserMappingIdInfoBeen property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the retUserMappingIdInfoBeen property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRetUserMappingIdInfoBeen().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RetUserMappingIdInfoBean }
     * 
     * 
     */
    public List<RetUserMappingIdInfoBean> getRetUserMappingIdInfoBeen() {
        if (retUserMappingIdInfoBeen == null) {
            retUserMappingIdInfoBeen = new ArrayList<RetUserMappingIdInfoBean>();
        }
        return this.retUserMappingIdInfoBeen;
    }

}
