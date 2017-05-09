package com.cssl.ctp.il.xsd.infra_v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for PaymentMethodType.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * <p>
 * 
 * <pre>
 * &lt;simpleType name="PaymentMethodType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Cash"/>
 *     &lt;enumeration value="NoPayment"/>
 *     &lt;enumeration value="Card"/>
 *     &lt;enumeration value="EFT"/>
 *     &lt;enumeration value="Cheque"/>
 *     &lt;enumeration value="Stamp"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "PaymentMethodType")
@XmlEnum
public enum PaymentMethodType {

	@XmlEnumValue("Cash")
	CASH("Cash"), @XmlEnumValue("NoPayment")
	NO_PAYMENT("NoPayment"), @XmlEnumValue("Card")
	CARD("Card"), EFT("EFT"), @XmlEnumValue("Cheque")
	CHEQUE("Cheque"), @XmlEnumValue("Stamp")
	STAMP("Stamp");
	private final String value;

	PaymentMethodType(String v) {
		value = v;
	}

	public String value() {
		return value;
	}

	public static PaymentMethodType fromValue(String v) {
		for (PaymentMethodType c : PaymentMethodType.values()) {
			if (c.value.equals(v)) {
				return c;
			}
		}
		throw new IllegalArgumentException(v);
	}

}
