package com.skilrock.ipe.instantprint;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for mapBeanElements complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="mapBeanElements">
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
@XmlType(name = "mapBeanElements", propOrder = { "key", "value" })
public class MapBeanElements {

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
