package com.cssl.ctp.il.xsd.infra_v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for ResponseBodyType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="ResponseBodyType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ResultCode" type="{http://ctp.cssl.com/il/xsd/Infra-v1.0}ResultCodeType"/>
 *         &lt;element name="MessageText" type="{http://ctp.cssl.com/il/xsd/Infra-v1.0}MessageTextType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResponseBodyType", propOrder = { "resultCode", "messageText" })
public class ResponseBodyType {

	@XmlElement(name = "ResultCode", required = true)
	protected String resultCode;
	@XmlElement(name = "MessageText", required = true)
	protected String messageText;

	/**
	 * Gets the value of the resultCode property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getResultCode() {
		return resultCode;
	}

	/**
	 * Sets the value of the resultCode property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setResultCode(String value) {
		this.resultCode = value;
	}

	/**
	 * Gets the value of the messageText property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getMessageText() {
		return messageText;
	}

	/**
	 * Sets the value of the messageText property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setMessageText(String value) {
		this.messageText = value;
	}

}
