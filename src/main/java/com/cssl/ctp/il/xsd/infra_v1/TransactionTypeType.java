package com.cssl.ctp.il.xsd.infra_v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for TransactionTypeType.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * <p>
 * 
 * <pre>
 * &lt;simpleType name="TransactionTypeType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Sale"/>
 *     &lt;enumeration value="Payment"/>
 *     &lt;enumeration value="Refund"/>
 *     &lt;enumeration value="Reversal"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "TransactionTypeType")
@XmlEnum
public enum TransactionTypeType {

	@XmlEnumValue("Sale")
	SALE("Sale"), @XmlEnumValue("Payment")
	PAYMENT("Payment"), @XmlEnumValue("Refund")
	REFUND("Refund"), @XmlEnumValue("Reversal")
	REVERSAL("Reversal");
	private final String value;

	TransactionTypeType(String v) {
		value = v;
	}

	public String value() {
		return value;
	}

	public static TransactionTypeType fromValue(String v) {
		for (TransactionTypeType c : TransactionTypeType.values()) {
			if (c.value.equals(v)) {
				return c;
			}
		}
		throw new IllegalArgumentException(v);
	}

}
