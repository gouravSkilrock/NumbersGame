package com.cssl.ctp.il.xsd.csheaders_v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import com.cssl.ctp.il.xsd.infra_v1.EntryMethodType;
import com.cssl.ctp.il.xsd.infra_v1.PaymentMethodType;

/**
 * Header for transactions.
 * 
 * <p>
 * Java class for RequestHeaderType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="RequestHeaderType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="UniqueID" type="{http://ctp.cssl.com/il/xsd/Infra-v1.0}UniqueIDType"/>
 *         &lt;element name="MessageTypeID" type="{http://ctp.cssl.com/il/xsd/Infra-v1.0}MessageTypeIDType"/>
 *         &lt;element name="CTPOutletID" type="{http://ctp.cssl.com/il/xsd/Infra-v1.0}CTPOutletIDType"/>
 *         &lt;element name="RetailerStoreID" type="{http://ctp.cssl.com/il/xsd/Infra-v1.0}RetailerStoreIDType"/>
 *         &lt;element name="RequestTimeStamp" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="ClientRequestID" type="{http://ctp.cssl.com/il/xsd/Infra-v1.0}ClientRequestIDType"/>
 *         &lt;element name="MessageSequenceID" type="{http://ctp.cssl.com/il/xsd/Infra-v1.0}MessageSequenceIDType"/>
 *         &lt;element name="PaymentMethod" type="{http://ctp.cssl.com/il/xsd/Infra-v1.0}PaymentMethodType" minOccurs="0"/>
 *         &lt;element name="EntryMethod" type="{http://ctp.cssl.com/il/xsd/Infra-v1.0}EntryMethodType" minOccurs="0"/>
 *         &lt;element name="Locale" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="5"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
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
@XmlType(name = "RequestHeaderType", propOrder = { "uniqueID", "messageTypeID",
		"ctpOutletID", "retailerStoreID", "requestTimeStamp",
		"clientRequestID", "messageSequenceID", "paymentMethod", "entryMethod",
		"locale" })
public class RequestHeaderType {

	@XmlElement(name = "UniqueID", required = true)
	protected String uniqueID;
	@XmlElement(name = "MessageTypeID", required = true)
	protected String messageTypeID;
	@XmlElement(name = "CTPOutletID", required = true)
	protected String ctpOutletID;
	@XmlElement(name = "RetailerStoreID", required = true)
	protected String retailerStoreID;
	@XmlElement(name = "RequestTimeStamp", required = true)
	@XmlSchemaType(name = "dateTime")
	protected XMLGregorianCalendar requestTimeStamp;
	@XmlElement(name = "ClientRequestID", required = true)
	protected String clientRequestID;
	@XmlElement(name = "MessageSequenceID", required = true)
	protected String messageSequenceID;
	@XmlElement(name = "PaymentMethod")
	protected PaymentMethodType paymentMethod;
	@XmlElement(name = "EntryMethod")
	protected EntryMethodType entryMethod;
	@XmlElement(name = "Locale")
	protected String locale;

	/**
	 * Gets the value of the uniqueID property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getUniqueID() {
		return uniqueID;
	}

	/**
	 * Sets the value of the uniqueID property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setUniqueID(String value) {
		this.uniqueID = value;
	}

	/**
	 * Gets the value of the messageTypeID property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getMessageTypeID() {
		return messageTypeID;
	}

	/**
	 * Sets the value of the messageTypeID property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setMessageTypeID(String value) {
		this.messageTypeID = value;
	}

	/**
	 * Gets the value of the ctpOutletID property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCTPOutletID() {
		return ctpOutletID;
	}

	/**
	 * Sets the value of the ctpOutletID property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setCTPOutletID(String value) {
		this.ctpOutletID = value;
	}

	/**
	 * Gets the value of the retailerStoreID property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getRetailerStoreID() {
		return retailerStoreID;
	}

	/**
	 * Sets the value of the retailerStoreID property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setRetailerStoreID(String value) {
		this.retailerStoreID = value;
	}

	/**
	 * Gets the value of the requestTimeStamp property.
	 * 
	 * @return possible object is {@link XMLGregorianCalendar }
	 * 
	 */
	public XMLGregorianCalendar getRequestTimeStamp() {
		return requestTimeStamp;
	}

	/**
	 * Sets the value of the requestTimeStamp property.
	 * 
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	public void setRequestTimeStamp(XMLGregorianCalendar value) {
		this.requestTimeStamp = value;
	}

	/**
	 * Gets the value of the clientRequestID property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getClientRequestID() {
		return clientRequestID;
	}

	/**
	 * Sets the value of the clientRequestID property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setClientRequestID(String value) {
		this.clientRequestID = value;
	}

	/**
	 * Gets the value of the messageSequenceID property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getMessageSequenceID() {
		return messageSequenceID;
	}

	/**
	 * Sets the value of the messageSequenceID property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setMessageSequenceID(String value) {
		this.messageSequenceID = value;
	}

	/**
	 * Gets the value of the paymentMethod property.
	 * 
	 * @return possible object is {@link PaymentMethodType }
	 * 
	 */
	public PaymentMethodType getPaymentMethod() {
		return paymentMethod;
	}

	/**
	 * Sets the value of the paymentMethod property.
	 * 
	 * @param value
	 *            allowed object is {@link PaymentMethodType }
	 * 
	 */
	public void setPaymentMethod(PaymentMethodType value) {
		this.paymentMethod = value;
	}

	/**
	 * Gets the value of the entryMethod property.
	 * 
	 * @return possible object is {@link EntryMethodType }
	 * 
	 */
	public EntryMethodType getEntryMethod() {
		return entryMethod;
	}

	/**
	 * Sets the value of the entryMethod property.
	 * 
	 * @param value
	 *            allowed object is {@link EntryMethodType }
	 * 
	 */
	public void setEntryMethod(EntryMethodType value) {
		this.entryMethod = value;
	}

	/**
	 * Gets the value of the locale property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getLocale() {
		return locale;
	}

	/**
	 * Sets the value of the locale property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setLocale(String value) {
		this.locale = value;
	}

}
