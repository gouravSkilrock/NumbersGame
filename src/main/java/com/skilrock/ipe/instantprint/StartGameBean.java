package com.skilrock.ipe.instantprint;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for startGameBean complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="startGameBean">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="gameBean" type="{http://instantPrint.ipe.skilrock.com/}gameBean" minOccurs="0"/>
 *         &lt;element name="gameMap">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="entry" maxOccurs="unbounded" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="key" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *                             &lt;element name="value" type="{http://instantPrint.ipe.skilrock.com/}gameBean" minOccurs="0"/>
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
@XmlType(name = "startGameBean", propOrder = { "gameBean", "gameMap", "success" })
public class StartGameBean {

	protected GameBean gameBean;
	@XmlElement(required = true)
	protected StartGameBean.GameMap gameMap;
	protected boolean success;

	/**
	 * Gets the value of the gameBean property.
	 * 
	 * @return possible object is {@link GameBean }
	 * 
	 */
	public GameBean getGameBean() {
		return gameBean;
	}

	/**
	 * Sets the value of the gameBean property.
	 * 
	 * @param value
	 *            allowed object is {@link GameBean }
	 * 
	 */
	public void setGameBean(GameBean value) {
		this.gameBean = value;
	}

	/**
	 * Gets the value of the gameMap property.
	 * 
	 * @return possible object is {@link StartGameBean.GameMap }
	 * 
	 */
	public StartGameBean.GameMap getGameMap() {
		return gameMap;
	}

	/**
	 * Sets the value of the gameMap property.
	 * 
	 * @param value
	 *            allowed object is {@link StartGameBean.GameMap }
	 * 
	 */
	public void setGameMap(StartGameBean.GameMap value) {
		this.gameMap = value;
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
	 *                   &lt;element name="key" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
	 *                   &lt;element name="value" type="{http://instantPrint.ipe.skilrock.com/}gameBean" minOccurs="0"/>
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
	public static class GameMap {

		protected List<StartGameBean.GameMap.Entry> entry;

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
		 * {@link StartGameBean.GameMap.Entry }
		 * 
		 * 
		 */
		public List<StartGameBean.GameMap.Entry> getEntry() {
			if (entry == null) {
				entry = new ArrayList<StartGameBean.GameMap.Entry>();
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
		 *         &lt;element name="key" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
		 *         &lt;element name="value" type="{http://instantPrint.ipe.skilrock.com/}gameBean" minOccurs="0"/>
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

			protected Integer key;
			protected GameBean value;

			/**
			 * Gets the value of the key property.
			 * 
			 * @return possible object is {@link Integer }
			 * 
			 */
			public Integer getKey() {
				return key;
			}

			/**
			 * Sets the value of the key property.
			 * 
			 * @param value
			 *            allowed object is {@link Integer }
			 * 
			 */
			public void setKey(Integer value) {
				this.key = value;
			}

			/**
			 * Gets the value of the value property.
			 * 
			 * @return possible object is {@link GameBean }
			 * 
			 */
			public GameBean getValue() {
				return value;
			}

			/**
			 * Sets the value of the value property.
			 * 
			 * @param value
			 *            allowed object is {@link GameBean }
			 * 
			 */
			public void setValue(GameBean value) {
				this.value = value;
			}

		}

	}

}
