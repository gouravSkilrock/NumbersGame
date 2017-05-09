package com.cssl.ctp.il.xsd.infra_v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * The body of a Top Up request.
 * 
 * <p>
 * Java class for TopUpRequestBodyType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="TopUpRequestBodyType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ctp.cssl.com/il/xsd/Infra-v1.0}ProductRequestBodyType">
 *       &lt;sequence>
 *         &lt;element name="TopUpValue" type="{http://ctp.cssl.com/il/xsd/Infra-v1.0}MoneyType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TopUpRequestBodyType", propOrder = { "topUpValue" })
public class TopUpRequestBodyType extends ProductRequestBodyType {

	@XmlElement(name = "TopUpValue", required = true)
	protected MoneyType topUpValue;

	/**
	 * Gets the value of the topUpValue property.
	 * 
	 * @return possible object is {@link MoneyType }
	 * 
	 */
	public MoneyType getTopUpValue() {
		return topUpValue;
	}

	/**
	 * Sets the value of the topUpValue property.
	 * 
	 * @param value
	 *            allowed object is {@link MoneyType }
	 * 
	 */
	public void setTopUpValue(MoneyType value) {
		this.topUpValue = value;
	}

}
