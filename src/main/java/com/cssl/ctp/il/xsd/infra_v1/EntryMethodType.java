package com.cssl.ctp.il.xsd.infra_v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for EntryMethodType.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * <p>
 * 
 * <pre>
 * &lt;simpleType name="EntryMethodType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="MagneticSwipe"/>
 *     &lt;enumeration value="KeyEntryCustomerPresent"/>
 *     &lt;enumeration value="KeyEntry3FailedSwipes"/>
 *     &lt;enumeration value="EVoucher"/>
 *     &lt;enumeration value="TaleXus"/>
 *     &lt;enumeration value="BarCoded"/>
 *     &lt;enumeration value="KeyEntry3FailedScans"/>
 *     &lt;enumeration value="DlrtsKeyedTransaction"/>
 *     &lt;enumeration value="SmartCard"/>
 *     &lt;enumeration value="Contactless"/>
 *     &lt;enumeration value="Ivr"/>
 *     &lt;enumeration value="HhScan"/>
 *     &lt;enumeration value="HhManualEntry"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "EntryMethodType")
@XmlEnum
public enum EntryMethodType {

	@XmlEnumValue("MagneticSwipe")
	MAGNETIC_SWIPE("MagneticSwipe"), @XmlEnumValue("KeyEntryCustomerPresent")
	KEY_ENTRY_CUSTOMER_PRESENT("KeyEntryCustomerPresent"), @XmlEnumValue("KeyEntry3FailedSwipes")
	KEY_ENTRY_3_FAILED_SWIPES("KeyEntry3FailedSwipes"), @XmlEnumValue("EVoucher")
	E_VOUCHER("EVoucher"), @XmlEnumValue("TaleXus")
	TALE_XUS("TaleXus"), @XmlEnumValue("BarCoded")
	BAR_CODED("BarCoded"), @XmlEnumValue("KeyEntry3FailedScans")
	KEY_ENTRY_3_FAILED_SCANS("KeyEntry3FailedScans"), @XmlEnumValue("DlrtsKeyedTransaction")
	DLRTS_KEYED_TRANSACTION("DlrtsKeyedTransaction"), @XmlEnumValue("SmartCard")
	SMART_CARD("SmartCard"), @XmlEnumValue("Contactless")
	CONTACTLESS("Contactless"), @XmlEnumValue("Ivr")
	IVR("Ivr"), @XmlEnumValue("HhScan")
	HH_SCAN("HhScan"), @XmlEnumValue("HhManualEntry")
	HH_MANUAL_ENTRY("HhManualEntry");
	private final String value;

	EntryMethodType(String v) {
		value = v;
	}

	public String value() {
		return value;
	}

	public static EntryMethodType fromValue(String v) {
		for (EntryMethodType c : EntryMethodType.values()) {
			if (c.value.equals(v)) {
				return c;
			}
		}
		throw new IllegalArgumentException(v);
	}

}
