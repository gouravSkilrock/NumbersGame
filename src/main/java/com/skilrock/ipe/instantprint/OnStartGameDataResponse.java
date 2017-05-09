package com.skilrock.ipe.instantprint;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for onStartGameDataResponse complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="onStartGameDataResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="return" type="{http://instantPrint.ipe.skilrock.com/}startGameBean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "onStartGameDataResponse", propOrder = { "_return" })
public class OnStartGameDataResponse {

	@XmlElement(name = "return")
	protected StartGameBean _return;

	/**
	 * Gets the value of the return property.
	 * 
	 * @return possible object is {@link StartGameBean }
	 * 
	 */
	public StartGameBean getReturn() {
		return _return;
	}

	/**
	 * Sets the value of the return property.
	 * 
	 * @param value
	 *            allowed object is {@link StartGameBean }
	 * 
	 */
	public void setReturn(StartGameBean value) {
		this._return = value;
	}

}
