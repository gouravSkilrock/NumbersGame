package com.skilrock.ipe.instantprint;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for newGameUpload complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="newGameUpload">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="gameBean" type="{http://instantPrint.ipe.skilrock.com/}gameBasicDetailBean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "newGameUpload", propOrder = { "gameBean" })
public class NewGameUpload {

	protected GameBasicDetailBean gameBean;

	/**
	 * Gets the value of the gameBean property.
	 * 
	 * @return possible object is {@link GameBasicDetailBean }
	 * 
	 */
	public GameBasicDetailBean getGameBean() {
		return gameBean;
	}

	/**
	 * Sets the value of the gameBean property.
	 * 
	 * @param value
	 *            allowed object is {@link GameBasicDetailBean }
	 * 
	 */
	public void setGameBean(GameBasicDetailBean value) {
		this.gameBean = value;
	}

}
