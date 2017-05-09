package com.cssl.ctp.il.xsd.refund_v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.cssl.ctp.il.xsd.infra_v1.MoneyType;
import com.cssl.ctp.il.xsd.infra_v1.ProviderResponseBodyType;

/**
 * The body of the response.
 * 
 * <p>
 * Java class for RefundResponseBodyType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="RefundResponseBodyType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ctp.cssl.com/il/xsd/Infra-v1.0}ProviderResponseBodyType">
 *       &lt;sequence>
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
@XmlType(name = "RefundResponseBodyType", propOrder = { "refundValue" })
public class RefundResponseBodyType extends ProviderResponseBodyType {

	@XmlElement(name = "RefundValue", required = true)
	protected MoneyType refundValue;

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
