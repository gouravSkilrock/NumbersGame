package com.cssl.ctp.il.xsd.mobileelectronictopup_v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import com.cssl.ctp.il.xsd.infra_v1.MoneyType;
import com.cssl.ctp.il.xsd.infra_v1.ProviderResponseBodyType;

/**
 * The body of the response.
 * 
 * <p>
 * Java class for MobileElectronicTopUpResponseBodyType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="MobileElectronicTopUpResponseBodyType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ctp.cssl.com/il/xsd/Infra-v1.0}ProviderResponseBodyType">
 *       &lt;sequence>
 *         &lt;element name="MobileNumber" type="{http://ctp.cssl.com/il/xsd/Mobile-v1.0}MobileNumberType" minOccurs="0"/>
 *         &lt;element name="TopUpValue" type="{http://ctp.cssl.com/il/xsd/Infra-v1.0}MoneyType" minOccurs="0"/>
 *         &lt;element name="Balance" type="{http://ctp.cssl.com/il/xsd/Infra-v1.0}MoneyType" minOccurs="0"/>
 *         &lt;element name="ExpiryDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MobileElectronicTopUpResponseBodyType", propOrder = {
		"mobileNumber", "topUpValue", "balance", "expiryDate" })
public class MobileElectronicTopUpResponseBodyType extends
		ProviderResponseBodyType {

	@XmlElement(name = "MobileNumber")
	protected String mobileNumber;
	@XmlElement(name = "TopUpValue")
	protected MoneyType topUpValue;
	@XmlElement(name = "Balance")
	protected MoneyType balance;
	@XmlElement(name = "ExpiryDate")
	@XmlSchemaType(name = "date")
	protected XMLGregorianCalendar expiryDate;

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

	/**
	 * Gets the value of the balance property.
	 * 
	 * @return possible object is {@link MoneyType }
	 * 
	 */
	public MoneyType getBalance() {
		return balance;
	}

	/**
	 * Sets the value of the balance property.
	 * 
	 * @param value
	 *            allowed object is {@link MoneyType }
	 * 
	 */
	public void setBalance(MoneyType value) {
		this.balance = value;
	}

	/**
	 * Gets the value of the expiryDate property.
	 * 
	 * @return possible object is {@link XMLGregorianCalendar }
	 * 
	 */
	public XMLGregorianCalendar getExpiryDate() {
		return expiryDate;
	}

	/**
	 * Sets the value of the expiryDate property.
	 * 
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	public void setExpiryDate(XMLGregorianCalendar value) {
		this.expiryDate = value;
	}

}
