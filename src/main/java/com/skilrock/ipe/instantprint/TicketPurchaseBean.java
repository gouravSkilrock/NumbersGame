package com.skilrock.ipe.instantprint;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for ticketPurchaseBean complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="ticketPurchaseBean">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="gameId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="gameNo" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="gameName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ticketNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="partyId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="partyType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="userId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="refMerId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="purchaseTime" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="refTransId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="purChannel" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="totalAmt" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="virnNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="imgList" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="saleStatus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="prizeCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="advMsg">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="entry" maxOccurs="unbounded" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="key" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                             &lt;element name="value" type="{http://instantPrint.ipe.skilrock.com/}arrayList" minOccurs="0"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="isSale" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ticketPurchaseBean", propOrder = { "gameId", "gameNo",
		"gameName", "ticketNo", "partyId", "partyType", "userId", "refMerId",
		"purchaseTime", "refTransId", "purChannel", "totalAmt", "virnNo",
		"imgList", "saleStatus", "prizeCode", "advMsg", "isSale" })
public class TicketPurchaseBean {

	protected int gameId;
	protected int gameNo;
	protected String gameName;
	protected String ticketNo;
	protected int partyId;
	protected String partyType;
	protected int userId;
	protected String refMerId;
	protected Long purchaseTime;
	protected int refTransId;
	protected String purChannel;
	protected double totalAmt;
	protected String virnNo;
	protected String imgList;
	protected String saleStatus;
	protected String prizeCode;
	@XmlElement(required = true)
	protected TicketPurchaseBean.AdvMsg advMsg;
	protected boolean isSale;

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
	 * Gets the value of the partyId property.
	 * 
	 */
	public int getPartyId() {
		return partyId;
	}

	/**
	 * Sets the value of the partyId property.
	 * 
	 */
	public void setPartyId(int value) {
		this.partyId = value;
	}

	/**
	 * Gets the value of the partyType property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getPartyType() {
		return partyType;
	}

	/**
	 * Sets the value of the partyType property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setPartyType(String value) {
		this.partyType = value;
	}

	/**
	 * Gets the value of the userId property.
	 * 
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * Sets the value of the userId property.
	 * 
	 */
	public void setUserId(int value) {
		this.userId = value;
	}

	/**
	 * Gets the value of the refMerId property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getRefMerId() {
		return refMerId;
	}

	/**
	 * Sets the value of the refMerId property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setRefMerId(String value) {
		this.refMerId = value;
	}

	/**
	 * Gets the value of the purchaseTime property.
	 * 
	 * @return possible object is {@link Long }
	 * 
	 */
	public Long getPurchaseTime() {
		return purchaseTime;
	}

	/**
	 * Sets the value of the purchaseTime property.
	 * 
	 * @param value
	 *            allowed object is {@link Long }
	 * 
	 */
	public void setPurchaseTime(Long value) {
		this.purchaseTime = value;
	}

	/**
	 * Gets the value of the refTransId property.
	 * 
	 */
	public int getRefTransId() {
		return refTransId;
	}

	/**
	 * Sets the value of the refTransId property.
	 * 
	 */
	public void setRefTransId(int value) {
		this.refTransId = value;
	}

	/**
	 * Gets the value of the purChannel property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getPurChannel() {
		return purChannel;
	}

	/**
	 * Sets the value of the purChannel property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setPurChannel(String value) {
		this.purChannel = value;
	}

	/**
	 * Gets the value of the totalAmt property.
	 * 
	 */
	public double getTotalAmt() {
		return totalAmt;
	}

	/**
	 * Sets the value of the totalAmt property.
	 * 
	 */
	public void setTotalAmt(double value) {
		this.totalAmt = value;
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
	 * Gets the value of the imgList property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getImgList() {
		return imgList;
	}

	/**
	 * Sets the value of the imgList property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setImgList(String value) {
		this.imgList = value;
	}

	/**
	 * Gets the value of the saleStatus property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getSaleStatus() {
		return saleStatus;
	}

	/**
	 * Sets the value of the saleStatus property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setSaleStatus(String value) {
		this.saleStatus = value;
	}

	/**
	 * Gets the value of the prizeCode property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getPrizeCode() {
		return prizeCode;
	}

	/**
	 * Sets the value of the prizeCode property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setPrizeCode(String value) {
		this.prizeCode = value;
	}

	/**
	 * Gets the value of the advMsg property.
	 * 
	 * @return possible object is {@link TicketPurchaseBean.AdvMsg }
	 * 
	 */
	public TicketPurchaseBean.AdvMsg getAdvMsg() {
		return advMsg;
	}

	/**
	 * Sets the value of the advMsg property.
	 * 
	 * @param value
	 *            allowed object is {@link TicketPurchaseBean.AdvMsg }
	 * 
	 */
	public void setAdvMsg(TicketPurchaseBean.AdvMsg value) {
		this.advMsg = value;
	}

	/**
	 * Gets the value of the isSale property.
	 * 
	 */
	public boolean isIsSale() {
		return isSale;
	}

	/**
	 * Sets the value of the isSale property.
	 * 
	 */
	public void setIsSale(boolean value) {
		this.isSale = value;
	}

	/**
	 * <p>
	 * Java class for anonymous complex type.
	 * 
	 * <p>
	 * The following schema fragment specifies the expected content contained
	 * within this class.
	 * 
	 * <pre>
	 * &lt;complexType>
	 *   &lt;complexContent>
	 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
	 *       &lt;sequence>
	 *         &lt;element name="entry" maxOccurs="unbounded" minOccurs="0">
	 *           &lt;complexType>
	 *             &lt;complexContent>
	 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
	 *                 &lt;sequence>
	 *                   &lt;element name="key" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
	 *                   &lt;element name="value" type="{http://instantPrint.ipe.skilrock.com/}arrayList" minOccurs="0"/>
	 *                 &lt;/sequence>
	 *               &lt;/restriction>
	 *             &lt;/complexContent>
	 *           &lt;/complexType>
	 *         &lt;/element>
	 *       &lt;/sequence>
	 *     &lt;/restriction>
	 *   &lt;/complexContent>
	 * &lt;/complexType>
	 * </pre>
	 * 
	 * 
	 */
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = { "entry" })
	public static class AdvMsg {

		protected List<TicketPurchaseBean.AdvMsg.Entry> entry;

		/**
		 * Gets the value of the entry property.
		 * 
		 * <p>
		 * This accessor method returns a reference to the live list, not a
		 * snapshot. Therefore any modification you make to the returned list
		 * will be present inside the JAXB object. This is why there is not a
		 * <CODE>set</CODE> method for the entry property.
		 * 
		 * <p>
		 * For example, to add a new item, do as follows:
		 * 
		 * <pre>
		 * getEntry().add(newItem);
		 * </pre>
		 * 
		 * 
		 * <p>
		 * Objects of the following type(s) are allowed in the list
		 * {@link TicketPurchaseBean.AdvMsg.Entry }
		 * 
		 * 
		 */
		public List<TicketPurchaseBean.AdvMsg.Entry> getEntry() {
			if (entry == null) {
				entry = new java.util.ArrayList<TicketPurchaseBean.AdvMsg.Entry>();
			}
			return this.entry;
		}

		/**
		 * <p>
		 * Java class for anonymous complex type.
		 * 
		 * <p>
		 * The following schema fragment specifies the expected content
		 * contained within this class.
		 * 
		 * <pre>
		 * &lt;complexType>
		 *   &lt;complexContent>
		 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
		 *       &lt;sequence>
		 *         &lt;element name="key" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
		 *         &lt;element name="value" type="{http://instantPrint.ipe.skilrock.com/}arrayList" minOccurs="0"/>
		 *       &lt;/sequence>
		 *     &lt;/restriction>
		 *   &lt;/complexContent>
		 * &lt;/complexType>
		 * </pre>
		 * 
		 * 
		 */
		@XmlAccessorType(XmlAccessType.FIELD)
		@XmlType(name = "", propOrder = { "key", "value" })
		public static class Entry {

			protected String key;
			protected com.skilrock.ipe.instantprint.ArrayList value;

			/**
			 * Gets the value of the key property.
			 * 
			 * @return possible object is {@link String }
			 * 
			 */
			public String getKey() {
				return key;
			}

			/**
			 * Sets the value of the key property.
			 * 
			 * @param value
			 *            allowed object is {@link String }
			 * 
			 */
			public void setKey(String value) {
				this.key = value;
			}

			/**
			 * Gets the value of the value property.
			 * 
			 * @return possible object is
			 *         {@link com.skilrock.ipe.instantprint.ArrayList }
			 * 
			 */
			public com.skilrock.ipe.instantprint.ArrayList getValue() {
				return value;
			}

			/**
			 * Sets the value of the value property.
			 * 
			 * @param value
			 *            allowed object is
			 *            {@link com.skilrock.ipe.instantprint.ArrayList }
			 * 
			 */
			public void setValue(com.skilrock.ipe.instantprint.ArrayList value) {
				this.value = value;
			}

		}

	}

}
