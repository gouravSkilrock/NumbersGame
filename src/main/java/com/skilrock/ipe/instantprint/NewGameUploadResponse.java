package com.skilrock.ipe.instantprint;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for newGameUploadResponse complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="newGameUploadResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="return" type="{http://instantPrint.ipe.skilrock.com/}gameBasicDetailBean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "newGameUploadResponse", propOrder = { "_return" })
public class NewGameUploadResponse {

	@XmlElement(name = "return")
	protected GameBasicDetailBean _return;

	/**
	 * Gets the value of the return property.
	 * 
	 * @return possible object is {@link GameBasicDetailBean }
	 * 
	 */
	public GameBasicDetailBean getReturn() {
		return _return;
	}

	/**
	 * Sets the value of the return property.
	 * 
	 * @param value
	 *            allowed object is {@link GameBasicDetailBean }
	 * 
	 */
	public void setReturn(GameBasicDetailBean value) {
		this._return = value;
	}

}
