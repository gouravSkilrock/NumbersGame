package com.skilrock.ipe.instantprint;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for gameBasicDetailBean complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="gameBasicDetailBean">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="gameId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="gameNo" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="gameName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ticketPrice" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="startDate" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="saleEndDate" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="pwtEndDate" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="noOfBooks" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="noOfTicketsPerBook" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="noOfBooksPerPack" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="gameStatus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="rankFile" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="digitsOfPack" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="digitsOfBook" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="digitsOfTicket" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="digitsOfVirn" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="imgFile" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="gameType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="printType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="xmlScheme" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="success" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "gameBasicDetailBean", propOrder = { "gameId", "gameNo",
		"gameName", "ticketPrice", "startDate", "saleEndDate", "pwtEndDate",
		"noOfBooks", "noOfTicketsPerBook", "noOfBooksPerPack", "gameStatus",
		"rankFile", "digitsOfPack", "digitsOfBook", "digitsOfTicket",
		"digitsOfVirn", "imgFile", "gameType", "printType", "xmlScheme",
		"success" })
public class GameBasicDetailBean {

	protected int gameId;
	protected int gameNo;
	protected String gameName;
	protected double ticketPrice;
	protected Long startDate;
	protected Long saleEndDate;
	protected Long pwtEndDate;
	protected int noOfBooks;
	protected int noOfTicketsPerBook;
	protected int noOfBooksPerPack;
	protected String gameStatus;
	protected String rankFile;
	protected int digitsOfPack;
	protected int digitsOfBook;
	protected int digitsOfTicket;
	protected int digitsOfVirn;
	protected String imgFile;
	protected String gameType;
	protected String printType;
	protected String xmlScheme;
	protected boolean success;

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
	 * Gets the value of the ticketPrice property.
	 * 
	 */
	public double getTicketPrice() {
		return ticketPrice;
	}

	/**
	 * Sets the value of the ticketPrice property.
	 * 
	 */
	public void setTicketPrice(double value) {
		this.ticketPrice = value;
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

	/**
	 * Gets the value of the noOfBooks property.
	 * 
	 */
	public int getNoOfBooks() {
		return noOfBooks;
	}

	/**
	 * Sets the value of the noOfBooks property.
	 * 
	 */
	public void setNoOfBooks(int value) {
		this.noOfBooks = value;
	}

	/**
	 * Gets the value of the noOfTicketsPerBook property.
	 * 
	 */
	public int getNoOfTicketsPerBook() {
		return noOfTicketsPerBook;
	}

	/**
	 * Sets the value of the noOfTicketsPerBook property.
	 * 
	 */
	public void setNoOfTicketsPerBook(int value) {
		this.noOfTicketsPerBook = value;
	}

	/**
	 * Gets the value of the noOfBooksPerPack property.
	 * 
	 */
	public int getNoOfBooksPerPack() {
		return noOfBooksPerPack;
	}

	/**
	 * Sets the value of the noOfBooksPerPack property.
	 * 
	 */
	public void setNoOfBooksPerPack(int value) {
		this.noOfBooksPerPack = value;
	}

	/**
	 * Gets the value of the gameStatus property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getGameStatus() {
		return gameStatus;
	}

	/**
	 * Sets the value of the gameStatus property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setGameStatus(String value) {
		this.gameStatus = value;
	}

	/**
	 * Gets the value of the rankFile property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getRankFile() {
		return rankFile;
	}

	/**
	 * Sets the value of the rankFile property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setRankFile(String value) {
		this.rankFile = value;
	}

	/**
	 * Gets the value of the digitsOfPack property.
	 * 
	 */
	public int getDigitsOfPack() {
		return digitsOfPack;
	}

	/**
	 * Sets the value of the digitsOfPack property.
	 * 
	 */
	public void setDigitsOfPack(int value) {
		this.digitsOfPack = value;
	}

	/**
	 * Gets the value of the digitsOfBook property.
	 * 
	 */
	public int getDigitsOfBook() {
		return digitsOfBook;
	}

	/**
	 * Sets the value of the digitsOfBook property.
	 * 
	 */
	public void setDigitsOfBook(int value) {
		this.digitsOfBook = value;
	}

	/**
	 * Gets the value of the digitsOfTicket property.
	 * 
	 */
	public int getDigitsOfTicket() {
		return digitsOfTicket;
	}

	/**
	 * Sets the value of the digitsOfTicket property.
	 * 
	 */
	public void setDigitsOfTicket(int value) {
		this.digitsOfTicket = value;
	}

	/**
	 * Gets the value of the digitsOfVirn property.
	 * 
	 */
	public int getDigitsOfVirn() {
		return digitsOfVirn;
	}

	/**
	 * Sets the value of the digitsOfVirn property.
	 * 
	 */
	public void setDigitsOfVirn(int value) {
		this.digitsOfVirn = value;
	}

	/**
	 * Gets the value of the imgFile property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getImgFile() {
		return imgFile;
	}

	/**
	 * Sets the value of the imgFile property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setImgFile(String value) {
		this.imgFile = value;
	}

	/**
	 * Gets the value of the gameType property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getGameType() {
		return gameType;
	}

	/**
	 * Sets the value of the gameType property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setGameType(String value) {
		this.gameType = value;
	}

	/**
	 * Gets the value of the printType property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getPrintType() {
		return printType;
	}

	/**
	 * Sets the value of the printType property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setPrintType(String value) {
		this.printType = value;
	}

	/**
	 * Gets the value of the xmlScheme property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getXmlScheme() {
		return xmlScheme;
	}

	/**
	 * Sets the value of the xmlScheme property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setXmlScheme(String value) {
		this.xmlScheme = value;
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

}
