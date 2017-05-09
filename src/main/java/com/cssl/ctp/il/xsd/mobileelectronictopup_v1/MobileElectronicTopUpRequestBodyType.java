package com.cssl.ctp.il.xsd.mobileelectronictopup_v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.cssl.ctp.il.xsd.infra_v1.TopUpRequestBodyType;

/**
 * The body of the request.
 * 
 * <p>
 * Java class for MobileElectronicTopUpRequestBodyType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="MobileElectronicTopUpRequestBodyType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ctp.cssl.com/il/xsd/Infra-v1.0}TopUpRequestBodyType">
 *       &lt;sequence>
 *         &lt;choice>
 *           &lt;element name="PANNumber" type="{http://ctp.cssl.com/il/xsd/Infra-v1.0}PANNumberType"/>
 *           &lt;sequence>
 *             &lt;element name="MobileNumber" type="{http://ctp.cssl.com/il/xsd/Infra-v1.0}MobileNumberType"/>
 *             &lt;element name="NotificationNumber" type="{http://ctp.cssl.com/il/xsd/Infra-v1.0}NotificationNumberType" minOccurs="0"/>
 *           &lt;/sequence>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MobileElectronicTopUpRequestBodyType", propOrder = {
		"panNumber", "mobileNumber", "notificationNumber" })
public class MobileElectronicTopUpRequestBodyType extends TopUpRequestBodyType {

	@XmlElement(name = "PANNumber")
	protected String panNumber;
	@XmlElement(name = "MobileNumber")
	protected String mobileNumber;
	@XmlElement(name = "NotificationNumber")
	protected String notificationNumber;

	/**
	 * Gets the value of the panNumber property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getPANNumber() {
		return panNumber;
	}

	/**
	 * Sets the value of the panNumber property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setPANNumber(String value) {
		this.panNumber = value;
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
	 * Gets the value of the notificationNumber property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getNotificationNumber() {
		return notificationNumber;
	}

	/**
	 * Sets the value of the notificationNumber property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setNotificationNumber(String value) {
		this.notificationNumber = value;
	}

}
