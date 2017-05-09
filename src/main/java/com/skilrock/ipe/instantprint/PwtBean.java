package com.skilrock.ipe.instantprint;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for pwtBean complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="pwtBean">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="claimStatus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="gameId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="gameName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="gameNo" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="highPrize" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="isSold" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="message" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="messageCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="mobileNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="prizeAmt" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="refNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="regRequired" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="returnType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="success" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="ticketMessage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ticketNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ticketVerificationStatus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tktvalidity" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="updateTicketType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="valid" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="validity" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="verificationStatus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="virnNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="virnvalidity" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "pwtBean", propOrder = { "claimStatus", "gameId", "gameName",
		"gameNo", "highPrize", "isSold", "message", "messageCode",
		"mobileNumber", "prizeAmt", "refNumber", "regRequired", "returnType",
		"status", "success", "ticketMessage", "ticketNo",
		"ticketVerificationStatus", "tktvalidity", "updateTicketType", "valid",
		"validity", "verificationStatus", "virnNo", "virnvalidity" })
public class PwtBean {

	protected String claimStatus;
	protected int gameId;
	protected String gameName;
	protected int gameNo;
	protected boolean highPrize;
	protected String isSold;
	protected String message;
	protected String messageCode;
	protected String mobileNumber;
	protected double prizeAmt;
	protected String refNumber;
	protected boolean regRequired;
	protected String returnType;
	protected String status;
	protected boolean success;
	protected String ticketMessage;
	protected String ticketNo;
	protected String ticketVerificationStatus;
	protected String tktvalidity;
	protected String updateTicketType;
	protected boolean valid;
	protected String validity;
	protected String verificationStatus;
	protected String virnNo;
	protected String virnvalidity;

	/**
	 * Gets the value of the claimStatus property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getClaimStatus() {
		return claimStatus;
	}

	/**
	 * Sets the value of the claimStatus property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setClaimStatus(String value) {
		this.claimStatus = value;
	}

	/**
	 * Gets the value of the gameId property.
	 * 
	 */
	public int getGameId() {
		return gameId;
	}

	/**
	 * Sets the value of the gameId property.
	 * 
	 */
	public void setGameId(int value) {
		this.gameId = value;
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
	 * Gets the value of the highPrize property.
	 * 
	 */
	public boolean isHighPrize() {
		return highPrize;
	}

	/**
	 * Sets the value of the highPrize property.
	 * 
	 */
	public void setHighPrize(boolean value) {
		this.highPrize = value;
	}

	/**
	 * Gets the value of the isSold property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getIsSold() {
		return isSold;
	}

	/**
	 * Sets the value of the isSold property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setIsSold(String value) {
		this.isSold = value;
	}

	/**
	 * Gets the value of the message property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the value of the message property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setMessage(String value) {
		this.message = value;
	}

	/**
	 * Gets the value of the messageCode property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getMessageCode() {
		return messageCode;
	}

	/**
	 * Sets the value of the messageCode property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setMessageCode(String value) {
		this.messageCode = value;
	}

	/**
	 * Gets the value of the mobileNumber property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getMobileNumber() {
		return mobileNumber;
	}

	/**
	 * Sets the value of the mobileNumber property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setMobileNumber(String value) {
		this.mobileNumber = value;
	}

	/**
	 * Gets the value of the prizeAmt property.
	 * 
	 */
	public double getPrizeAmt() {
		return prizeAmt;
	}

	/**
	 * Sets the value of the prizeAmt property.
	 * 
	 */
	public void setPrizeAmt(double value) {
		this.prizeAmt = value;
	}

	/**
	 * Gets the value of the refNumber property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getRefNumber() {
		return refNumber;
	}

	/**
	 * Sets the value of the refNumber property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setRefNumber(String value) {
		this.refNumber = value;
	}

	/**
	 * Gets the value of the regRequired property.
	 * 
	 */
	public boolean isRegRequired() {
		return regRequired;
	}

	/**
	 * Sets the value of the regRequired property.
	 * 
	 */
	public void setRegRequired(boolean value) {
		this.regRequired = value;
	}

	/**
	 * Gets the value of the returnType property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getReturnType() {
		return returnType;
	}

	/**
	 * Sets the value of the returnType property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setReturnType(String value) {
		this.returnType = value;
	}

	/**
	 * Gets the value of the status property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Sets the value of the status property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setStatus(String value) {
		this.status = value;
	}

	/**
	 * Gets the value of the success property.
	 * 
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * Sets the value of the success property.
	 * 
	 */
	public void setSuccess(boolean value) {
		this.success = value;
	}

	/**
	 * Gets the value of the ticketMessage property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getTicketMessage() {
		return ticketMessage;
	}

	/**
	 * Sets the value of the ticketMessage property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setTicketMessage(String value) {
		this.ticketMessage = value;
	}

	/**
	 * Gets the value of the ticketNo property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getTicketNo() {
		return ticketNo;
	}

	/**
	 * Sets the value of the ticketNo property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setTicketNo(String value) {
		this.ticketNo = value;
	}

	/**
	 * Gets the value of the ticketVerificationStatus property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getTicketVerificationStatus() {
		return ticketVerificationStatus;
	}

	/**
	 * Sets the value of the ticketVerificationStatus property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setTicketVerificationStatus(String value) {
		this.ticketVerificationStatus = value;
	}

	/**
	 * Gets the value of the tktvalidity property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getTktvalidity() {
		return tktvalidity;
	}

	/**
	 * Sets the value of the tktvalidity property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setTktvalidity(String value) {
		this.tktvalidity = value;
	}

	/**
	 * Gets the value of the updateTicketType property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getUpdateTicketType() {
		return updateTicketType;
	}

	/**
	 * Sets the value of the updateTicketType property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setUpdateTicketType(String value) {
		this.updateTicketType = value;
	}

	/**
	 * Gets the value of the valid property.
	 * 
	 */
	public boolean isValid() {
		return valid;
	}

	/**
	 * Sets the value of the valid property.
	 * 
	 */
	public void setValid(boolean value) {
		this.valid = value;
	}

	/**
	 * Gets the value of the validity property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getValidity() {
		return validity;
	}

	/**
	 * Sets the value of the validity property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setValidity(String value) {
		this.validity = value;
	}

	/**
	 * Gets the value of the verificationStatus property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getVerificationStatus() {
		return verificationStatus;
	}

	/**
	 * Sets the value of the verificationStatus property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setVerificationStatus(String value) {
		this.verificationStatus = value;
	}

	/**
	 * Gets the value of the virnNo property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getVirnNo() {
		return virnNo;
	}

	/**
	 * Sets the value of the virnNo property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setVirnNo(String value) {
		this.virnNo = value;
	}

	/**
	 * Gets the value of the virnvalidity property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getVirnvalidity() {
		return virnvalidity;
	}

	/**
	 * Sets the value of the virnvalidity property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setVirnvalidity(String value) {
		this.virnvalidity = value;
	}

}
