package com.skilrock.ipe.instantprint;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for inventoryUpload complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="inventoryUpload">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="invGameBean" type="{http://instantPrint.ipe.skilrock.com/}gameInventoryDetailsBean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "inventoryUpload", propOrder = { "invGameBean" })
public class InventoryUpload {

	protected GameInventoryDetailsBean invGameBean;

	/**
	 * Gets the value of the invGameBean property.
	 * 
	 * @return possible object is {@link GameInventoryDetailsBean }
	 * 
	 */
	public GameInventoryDetailsBean getInvGameBean() {
		return invGameBean;
	}

	/**
	 * Sets the value of the invGameBean property.
	 * 
	 * @param value
	 *            allowed object is {@link GameInventoryDetailsBean }
	 * 
	 */
	public void setInvGameBean(GameInventoryDetailsBean value) {
		this.invGameBean = value;
	}

}
