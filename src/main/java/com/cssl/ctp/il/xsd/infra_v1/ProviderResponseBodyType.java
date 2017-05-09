package com.cssl.ctp.il.xsd.infra_v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for ProviderResponseBodyType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="ProviderResponseBodyType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ctp.cssl.com/il/xsd/Infra-v1.0}ResponseBodyType">
 *       &lt;sequence>
 *         &lt;element name="ProviderTransactionReference" type="{http://ctp.cssl.com/il/xsd/Infra-v1.0}ProviderTransactionReferenceType" minOccurs="0"/>
 *         &lt;element name="ProviderMessage" type="{http://ctp.cssl.com/il/xsd/Infra-v1.0}ProviderMessageType" minOccurs="0"/>
 *         &lt;element name="TransactionID" type="{http://ctp.cssl.com/il/xsd/Infra-v1.0}TransactionIDType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProviderResponseBodyType", propOrder = {
		"providerTransactionReference", "providerMessage", "transactionID" })
public class ProviderResponseBodyType extends ResponseBodyType {

	@XmlElement(name = "ProviderTransactionReference")
	protected String providerTransactionReference;
	@XmlElement(name = "ProviderMessage")
	protected String providerMessage;
	@XmlElement(name = "TransactionID", required = true)
	protected String transactionID;

	/**
	 * Gets the value of the providerTransactionReference property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getProviderTransactionReference() {
		return providerTransactionReference;
	}

	/**
	 * Sets the value of the providerTransactionReference property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setProviderTransactionReference(String value) {
		this.providerTransactionReference = value;
	}

	/**
	 * Gets the value of the providerMessage property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getProviderMessage() {
		return providerMessage;
	}

	/**
	 * Sets the value of the providerMessage property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setProviderMessage(String value) {
		this.providerMessage = value;
	}

	/**
	 * Gets the value of the transactionID property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getTransactionID() {
		return transactionID;
	}

	/**
	 * Sets the value of the transactionID property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setTransactionID(String value) {
		this.transactionID = value;
	}

}
