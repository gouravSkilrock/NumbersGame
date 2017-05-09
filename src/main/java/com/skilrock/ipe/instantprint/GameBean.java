package com.skilrock.ipe.instantprint;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for gameBean complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="gameBean">
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
 *         &lt;element name="gameStatus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="activeBookMap">
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
 *                             &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
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
 *         &lt;element name="noOfTktPerBook" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="gameKey" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="packNoDigit" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="bookNoDigit" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="tktNoDigit" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="gameNoDigit" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="rankDigit" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="virnDigit" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="gamePrintScheme" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="isSample" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="textOrImage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="imageSizeMap">
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
 *                             &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
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
 *         &lt;element name="imageDataMap">
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
 *                             &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
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
 *         &lt;element name="imageTypeMap">
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
 *                             &lt;element name="value" type="{http://jaxb.dev.java.net/array}stringArray" minOccurs="0"/>
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
 *         &lt;element name="gameLogoType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="gameLogoCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="prizeLogoType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="list" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "gameBean", propOrder = { "gameId", "gameNo", "gameName",
		"ticketPrice", "startDate", "saleEndDate", "pwtEndDate", "gameStatus",
		"activeBookMap", "noOfTktPerBook", "gameKey", "packNoDigit",
		"bookNoDigit", "tktNoDigit", "gameNoDigit", "rankDigit", "virnDigit",
		"gamePrintScheme", "isSample", "textOrImage", "imageSizeMap",
		"imageDataMap", "imageTypeMap", "gameLogoType", "gameLogoCode",
		"prizeLogoType", "list" })
public class GameBean {

	protected int gameId;
	protected int gameNo;
	protected String gameName;
	protected double ticketPrice;
	protected Long startDate;
	protected Long saleEndDate;
	protected Long pwtEndDate;
	protected String gameStatus;
	@XmlElement(required = true)
	protected GameBean.ActiveBookMap activeBookMap;
	protected int noOfTktPerBook;
	protected byte[] gameKey;
	protected int packNoDigit;
	protected int bookNoDigit;
	protected int tktNoDigit;
	protected int gameNoDigit;
	protected int rankDigit;
	protected int virnDigit;
	protected String gamePrintScheme;
	protected String isSample;
	protected String textOrImage;
	@XmlElement(required = true)
	protected GameBean.ImageSizeMap imageSizeMap;
	@XmlElement(required = true)
	protected GameBean.ImageDataMap imageDataMap;
	@XmlElement(required = true)
	protected GameBean.ImageTypeMap imageTypeMap;
	protected String gameLogoType;
	protected String gameLogoCode;
	protected String prizeLogoType;
	@XmlElement(nillable = true)
	protected List<String> list;

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
	 * Gets the value of the activeBookMap property.
	 * 
	 * @return possible object is {@link GameBean.ActiveBookMap }
	 * 
	 */
	public GameBean.ActiveBookMap getActiveBookMap() {
		return activeBookMap;
	}

	/**
	 * Sets the value of the activeBookMap property.
	 * 
	 * @param value
	 *            allowed object is {@link GameBean.ActiveBookMap }
	 * 
	 */
	public void setActiveBookMap(GameBean.ActiveBookMap value) {
		this.activeBookMap = value;
	}

	/**
	 * Gets the value of the noOfTktPerBook property.
	 * 
	 */
	public int getNoOfTktPerBook() {
		return noOfTktPerBook;
	}

	/**
	 * Sets the value of the noOfTktPerBook property.
	 * 
	 */
	public void setNoOfTktPerBook(int value) {
		this.noOfTktPerBook = value;
	}

	/**
	 * Gets the value of the gameKey property.
	 * 
	 * @return possible object is byte[]
	 */
	public byte[] getGameKey() {
		return gameKey;
	}

	/**
	 * Sets the value of the gameKey property.
	 * 
	 * @param value
	 *            allowed object is byte[]
	 */
	public void setGameKey(byte[] value) {
		this.gameKey = ((byte[]) value);
	}

	/**
	 * Gets the value of the packNoDigit property.
	 * 
	 */
	public int getPackNoDigit() {
		return packNoDigit;
	}

	/**
	 * Sets the value of the packNoDigit property.
	 * 
	 */
	public void setPackNoDigit(int value) {
		this.packNoDigit = value;
	}

	/**
	 * Gets the value of the bookNoDigit property.
	 * 
	 */
	public int getBookNoDigit() {
		return bookNoDigit;
	}

	/**
	 * Sets the value of the bookNoDigit property.
	 * 
	 */
	public void setBookNoDigit(int value) {
		this.bookNoDigit = value;
	}

	/**
	 * Gets the value of the tktNoDigit property.
	 * 
	 */
	public int getTktNoDigit() {
		return tktNoDigit;
	}

	/**
	 * Sets the value of the tktNoDigit property.
	 * 
	 */
	public void setTktNoDigit(int value) {
		this.tktNoDigit = value;
	}

	/**
	 * Gets the value of the gameNoDigit property.
	 * 
	 */
	public int getGameNoDigit() {
		return gameNoDigit;
	}

	/**
	 * Sets the value of the gameNoDigit property.
	 * 
	 */
	public void setGameNoDigit(int value) {
		this.gameNoDigit = value;
	}

	/**
	 * Gets the value of the rankDigit property.
	 * 
	 */
	public int getRankDigit() {
		return rankDigit;
	}

	/**
	 * Sets the value of the rankDigit property.
	 * 
	 */
	public void setRankDigit(int value) {
		this.rankDigit = value;
	}

	/**
	 * Gets the value of the virnDigit property.
	 * 
	 */
	public int getVirnDigit() {
		return virnDigit;
	}

	/**
	 * Sets the value of the virnDigit property.
	 * 
	 */
	public void setVirnDigit(int value) {
		this.virnDigit = value;
	}

	/**
	 * Gets the value of the gamePrintScheme property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getGamePrintScheme() {
		return gamePrintScheme;
	}

	/**
	 * Sets the value of the gamePrintScheme property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setGamePrintScheme(String value) {
		this.gamePrintScheme = value;
	}

	/**
	 * Gets the value of the isSample property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getIsSample() {
		return isSample;
	}

	/**
	 * Sets the value of the isSample property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setIsSample(String value) {
		this.isSample = value;
	}

	/**
	 * Gets the value of the textOrImage property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getTextOrImage() {
		return textOrImage;
	}

	/**
	 * Sets the value of the textOrImage property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setTextOrImage(String value) {
		this.textOrImage = value;
	}

	/**
	 * Gets the value of the imageSizeMap property.
	 * 
	 * @return possible object is {@link GameBean.ImageSizeMap }
	 * 
	 */
	public GameBean.ImageSizeMap getImageSizeMap() {
		return imageSizeMap;
	}

	/**
	 * Sets the value of the imageSizeMap property.
	 * 
	 * @param value
	 *            allowed object is {@link GameBean.ImageSizeMap }
	 * 
	 */
	public void setImageSizeMap(GameBean.ImageSizeMap value) {
		this.imageSizeMap = value;
	}

	/**
	 * Gets the value of the imageDataMap property.
	 * 
	 * @return possible object is {@link GameBean.ImageDataMap }
	 * 
	 */
	public GameBean.ImageDataMap getImageDataMap() {
		return imageDataMap;
	}

	/**
	 * Sets the value of the imageDataMap property.
	 * 
	 * @param value
	 *            allowed object is {@link GameBean.ImageDataMap }
	 * 
	 */
	public void setImageDataMap(GameBean.ImageDataMap value) {
		this.imageDataMap = value;
	}

	/**
	 * Gets the value of the imageTypeMap property.
	 * 
	 * @return possible object is {@link GameBean.ImageTypeMap }
	 * 
	 */
	public GameBean.ImageTypeMap getImageTypeMap() {
		return imageTypeMap;
	}

	/**
	 * Sets the value of the imageTypeMap property.
	 * 
	 * @param value
	 *            allowed object is {@link GameBean.ImageTypeMap }
	 * 
	 */
	public void setImageTypeMap(GameBean.ImageTypeMap value) {
		this.imageTypeMap = value;
	}

	/**
	 * Gets the value of the gameLogoType property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getGameLogoType() {
		return gameLogoType;
	}

	/**
	 * Sets the value of the gameLogoType property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setGameLogoType(String value) {
		this.gameLogoType = value;
	}

	/**
	 * Gets the value of the gameLogoCode property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getGameLogoCode() {
		return gameLogoCode;
	}

	/**
	 * Sets the value of the gameLogoCode property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setGameLogoCode(String value) {
		this.gameLogoCode = value;
	}

	/**
	 * Gets the value of the prizeLogoType property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getPrizeLogoType() {
		return prizeLogoType;
	}

	/**
	 * Sets the value of the prizeLogoType property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setPrizeLogoType(String value) {
		this.prizeLogoType = value;
	}

	/**
	 * Gets the value of the list property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the list property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getList().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link String }
	 * 
	 * 
	 */
	public List<String> getList() {
		if (list == null) {
			list = new ArrayList<String>();
		}
		return this.list;
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
	 *                   &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
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
	public static class ActiveBookMap {

		protected List<GameBean.ActiveBookMap.Entry> entry;

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
		 * {@link GameBean.ActiveBookMap.Entry }
		 * 
		 * 
		 */
		public List<GameBean.ActiveBookMap.Entry> getEntry() {
			if (entry == null) {
				entry = new ArrayList<GameBean.ActiveBookMap.Entry>();
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
		 *         &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
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
			protected Integer value;

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
			 * @return possible object is {@link Integer }
			 * 
			 */
			public Integer getValue() {
				return value;
			}

			/**
			 * Sets the value of the value property.
			 * 
			 * @param value
			 *            allowed object is {@link Integer }
			 * 
			 */
			public void setValue(Integer value) {
				this.value = value;
			}

		}

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
	 *                   &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
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
	public static class ImageDataMap {

		protected List<GameBean.ImageDataMap.Entry> entry;

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
		 * {@link GameBean.ImageDataMap.Entry }
		 * 
		 * 
		 */
		public List<GameBean.ImageDataMap.Entry> getEntry() {
			if (entry == null) {
				entry = new ArrayList<GameBean.ImageDataMap.Entry>();
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
		 *         &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
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
			protected byte[] value;

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
			 * @return possible object is byte[]
			 */
			public byte[] getValue() {
				return value;
			}

			/**
			 * Sets the value of the value property.
			 * 
			 * @param value
			 *            allowed object is byte[]
			 */
			public void setValue(byte[] value) {
				this.value = ((byte[]) value);
			}

		}

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
	 *                   &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
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
	public static class ImageSizeMap {

		protected List<GameBean.ImageSizeMap.Entry> entry;

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
		 * {@link GameBean.ImageSizeMap.Entry }
		 * 
		 * 
		 */
		public List<GameBean.ImageSizeMap.Entry> getEntry() {
			if (entry == null) {
				entry = new ArrayList<GameBean.ImageSizeMap.Entry>();
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
		 *         &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
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
			protected Integer value;

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
			 * @return possible object is {@link Integer }
			 * 
			 */
			public Integer getValue() {
				return value;
			}

			/**
			 * Sets the value of the value property.
			 * 
			 * @param value
			 *            allowed object is {@link Integer }
			 * 
			 */
			public void setValue(Integer value) {
				this.value = value;
			}

		}

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
	 *                   &lt;element name="value" type="{http://jaxb.dev.java.net/array}stringArray" minOccurs="0"/>
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
	public static class ImageTypeMap {

		protected List<GameBean.ImageTypeMap.Entry> entry;

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
		 * {@link GameBean.ImageTypeMap.Entry }
		 * 
		 * 
		 */
		public List<GameBean.ImageTypeMap.Entry> getEntry() {
			if (entry == null) {
				entry = new ArrayList<GameBean.ImageTypeMap.Entry>();
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
		 *         &lt;element name="value" type="{http://jaxb.dev.java.net/array}stringArray" minOccurs="0"/>
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
			protected StringArray value;

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
			 * @return possible object is {@link StringArray }
			 * 
			 */
			public StringArray getValue() {
				return value;
			}

			/**
			 * Sets the value of the value property.
			 * 
			 * @param value
			 *            allowed object is {@link StringArray }
			 * 
			 */
			public void setValue(StringArray value) {
				this.value = value;
			}

		}

	}

}
