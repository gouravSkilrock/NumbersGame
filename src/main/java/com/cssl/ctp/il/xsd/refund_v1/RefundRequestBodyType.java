package com.cssl.ctp.il.xsd.refund_v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.cssl.ctp.il.xsd.infra_v1.MoneyType;
import com.cssl.ctp.il.xsd.infra_v1.ProductRequestBodyType;

/**
 * The body of the request.
 * 
 * <p>
 * Java class for RefundRequestBodyType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="RefundRequestBodyType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ctp.cssl.com/il/xsd/Infra-v1.0}ProductRequestBodyType">
 *       &lt;sequence>
 *         &lt;element name="OriginalClientRequestID" type="{http://ctp.cssl.com/il/xsd/Infra-v1.0}ClientRequestIDType"/>
 *         &lt;element name="OriginalTransactionID" type="{http://ctp.cssl.com/il/xsd/Infra-v1.0}TransactionIDType"/>
 *         &lt;element name="RefundValue" type="{http://ctp.cssl.com/il/xsd/Infra-v1.0}MoneyType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RefundRequestBodyType", propOrder = {
		"originalClientRequestID", "originalTransactionID", "refundValue" })
public class RefundRequestBodyType extends ProductRequestBodyType {

	@XmlElement(name = "OriginalClientRequestID", required = true)
	protected String originalClientRequestID;
	@XmlElement(name = "OriginalTransactionID", required = true)
	protected String originalTransactionID;
	@XmlElement(name = "RefundValue", required = true)
	protected MoneyType refundValue;

	/**
	 * Gets the value of the originalClientRequestID property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getOriginalClientRequestID() {
		return originalClientRequestID;
	}

	/**
	 * Sets the value of the originalClientRequestID property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setOriginalClientRequestID(String value) {
		this.originalClientRequestID = value;
	}

	/**
	 * Gets the value of the originalTransactionID property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getOriginalTransactionID() {
		return originalTransactionID;
	}

	/**
	 * Sets the value of the originalTransactionID property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setOriginalTransactionID(String value) {
		this.originalTransactionID = value;
	}

	/**
	 * Gets the value of the refundValue property.
	 * 
	 * @return possible object is {@link MoneyType }
	 * 
	 */
	public MoneyType getRefundValue() {
		return refundValue;
	}

	/**
	 * Sets the value of the refundValue property.
	 * 
	 * @param value
	 *            allowed object is {@link MoneyType }
	 * 
	 */
	public void setRefundValue(MoneyType value) {
		this.refundValue = value;
	}

}
