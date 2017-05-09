package com.cssl.ctp.il.xsd.csheaders_v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Response header
 * 
 * <p>
 * Java class for ResponseHeaderType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="ResponseHeaderType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="MessageSequenceID" type="{http://ctp.cssl.com/il/xsd/Infra-v1.0}MessageSequenceIDType"/>
 *         &lt;element name="TransactionTimestamp" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResponseHeaderType", propOrder = { "messageSequenceID",
		"transactionTimestamp" })
public class ResponseHeaderType {

	@XmlElement(name = "MessageSequenceID", required = true)
	protected String messageSequenceID;
	@XmlElement(name = "TransactionTimestamp", required = true)
	@XmlSchemaType(name = "dateTime")
	protected XMLGregorianCalendar transactionTimestamp;

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
	 * Gets the value of the transactionTimestamp property.
	 * 
	 * @return possible object is {@link XMLGregorianCalendar }
	 * 
	 */
	public XMLGregorianCalendar getTransactionTimestamp() {
		return transactionTimestamp;
	}

	/**
	 * Sets the value of the transactionTimestamp property.
	 * 
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	public void setTransactionTimestamp(XMLGregorianCalendar value) {
		this.transactionTimestamp = value;
	}

}
