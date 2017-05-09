package com.cssl.ctp.il.xsd.reversal_v1;

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
 * Java class for ReversalRequestBodyType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="ReversalRequestBodyType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ctp.cssl.com/il/xsd/Infra-v1.0}ProductRequestBodyType">
 *       &lt;sequence>
 *         &lt;element name="OriginalClientRequestID" type="{http://ctp.cssl.com/il/xsd/Infra-v1.0}ClientRequestIDType"/>
 *         &lt;element name="ReversalValue" type="{http://ctp.cssl.com/il/xsd/Infra-v1.0}MoneyType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ReversalRequestBodyType", propOrder = {
		"originalClientRequestID", "reversalValue" })
public class ReversalRequestBodyType extends ProductRequestBodyType {

	@XmlElement(name = "OriginalClientRequestID", required = true)
	protected String originalClientRequestID;
	@XmlElement(name = "ReversalValue", required = true)
	protected MoneyType reversalValue;

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
	 * Gets the value of the reversalValue property.
	 * 
	 * @return possible object is {@link MoneyType }
	 * 
	 */
	public MoneyType getReversalValue() {
		return reversalValue;
	}

	/**
	 * Sets the value of the reversalValue property.
	 * 
	 * @param value
	 *            allowed object is {@link MoneyType }
	 * 
	 */
	public void setReversalValue(MoneyType value) {
		this.reversalValue = value;
	}

}
