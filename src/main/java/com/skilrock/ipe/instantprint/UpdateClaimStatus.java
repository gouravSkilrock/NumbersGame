package com.skilrock.ipe.instantprint;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for updateClaimStatus complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="updateClaimStatus">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="pwtBean" type="{http://instantPrint.ipe.skilrock.com/}pwtBean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "updateClaimStatus", propOrder = { "pwtBean" })
public class UpdateClaimStatus {

	protected PwtBean pwtBean;

	/**
	 * Gets the value of the pwtBean property.
	 * 
	 * @return possible object is {@link PwtBean }
	 * 
	 */
	public PwtBean getPwtBean() {
		return pwtBean;
	}

	/**
	 * Sets the value of the pwtBean property.
	 * 
	 * @param value
	 *            allowed object is {@link PwtBean }
	 * 
	 */
	public void setPwtBean(PwtBean value) {
		this.pwtBean = value;
	}

}
