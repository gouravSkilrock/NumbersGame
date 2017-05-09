package com.skilrock.ipe.instantprint;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for gameInventoryDetailsBean complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="gameInventoryDetailsBean">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="gameNo" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="gameName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="virnFile" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="packFrom" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="packTo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="startDate" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="saleEndDate" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="pwtEndDate" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "gameInventoryDetailsBean", propOrder = { "gameNo", "gameName",
		"virnFile", "packFrom", "packTo", "startDate", "saleEndDate",
		"pwtEndDate" })
public class GameInventoryDetailsBean {

	protected int gameNo;
	protected String gameName;
	protected String virnFile;
	protected String packFrom;
	protected String packTo;
	protected Long startDate;
	protected Long saleEndDate;
	protected Long pwtEndDate;

	/**
	 * Gets the value of the gameNo property.
	 * 
	 */
	public int getGameNo() {
		return gameNo;
	}

	/**
	 * Sets the value of the gameNo property.
	 * 
	 */
	public void setGameNo(int value) {
		this.gameNo = value;
	}

	/**
	 * Gets the value of the gameName property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getGameName() {
		return gameName;
	}

	/**
	 * Sets the value of the gameName property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setGameName(String value) {
		this.gameName = value;
	}

	/**
	 * Gets the value of the virnFile property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getVirnFile() {
		return virnFile;
	}

	/**
	 * Sets the value of the virnFile property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setVirnFile(String value) {
		this.virnFile = value;
	}

	/**
	 * Gets the value of the packFrom property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getPackFrom() {
		return packFrom;
	}

	/**
	 * Sets the value of the packFrom property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setPackFrom(String value) {
		this.packFrom = value;
	}

	/**
	 * Gets the value of the packTo property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getPackTo() {
		return packTo;
	}

	/**
	 * Sets the value of the packTo property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setPackTo(String value) {
		this.packTo = value;
	}

	/**
	 * Gets the value of the startDate property.
	 * 
	 * @return possible object is {@link Long }
	 * 
	 */
	public Long getStartDate() {
		return startDate;
	}

	/**
	 * Sets the value of the startDate property.
	 * 
	 * @param value
	 *            allowed object is {@link Long }
	 * 
	 */
	public void setStartDate(Long value) {
		this.startDate = value;
	}

	/**
	 * Gets the value of the saleEndDate property.
	 * 
	 * @return possible object is {@link Long }
	 * 
	 */
	public Long getSaleEndDate() {
		return saleEndDate;
	}

	/**
	 * Sets the value of the saleEndDate property.
	 * 
	 * @param value
	 *            allowed object is {@link Long }
	 * 
	 */
	public void setSaleEndDate(Long value) {
		this.saleEndDate = value;
	}

	/**
	 * Gets the value of the pwtEndDate property.
	 * 
	 * @return possible object is {@link Long }
	 * 
	 */
	public Long getPwtEndDate() {
		return pwtEndDate;
	}

	/**
	 * Sets the value of the pwtEndDate property.
	 * 
	 * @param value
	 *            allowed object is {@link Long }
	 * 
	 */
	public void setPwtEndDate(Long value) {
		this.pwtEndDate = value;
	}

}
