package com.cssl.ctp.il.xsd.reversal_v1;

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
 * Java class for ReversalResponseBodyType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="ReversalResponseBodyType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ctp.cssl.com/il/xsd/Infra-v1.0}ProviderResponseBodyType">
 *       &lt;sequence>
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
@XmlType(name = "ReversalResponseBodyType", propOrder = { "reversalValue" })
public class ReversalResponseBodyType extends ProviderResponseBodyType {

	@XmlElement(name = "ReversalValue", required = true)
	protected MoneyType reversalValue;

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
