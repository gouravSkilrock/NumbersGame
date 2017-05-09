package com.cssl.ctp.il.xsd.voucher_v1;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import com.cssl.ctp.il.xsd.infra_v1.ProviderResponseBodyType;

/**
 * <p>
 * Java class for VoucherTopUpResponseBodyType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="VoucherTopUpResponseBodyType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ctp.cssl.com/il/xsd/Infra-v1.0}ProviderResponseBodyType">
 *       &lt;sequence>
 *         &lt;element name="PINNumber" type="{http://ctp.cssl.com/il/xsd/Infra-v1.0}PINNumberType"/>
 *         &lt;element name="ExpiryDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VoucherTopUpResponseBodyType", propOrder = { "pinNumber",
		"expiryDate" })
public class VoucherTopUpResponseBodyType extends ProviderResponseBodyType {

	@XmlElement(name = "PINNumber", required = true)
	protected BigInteger pinNumber;
	@XmlElement(name = "ExpiryDate")
	@XmlSchemaType(name = "date")
	protected XMLGregorianCalendar expiryDate;

	/**
	 * Gets the value of the pinNumber property.
	 * 
	 * @return possible object is {@link BigInteger }
	 * 
	 */
	public BigInteger getPINNumber() {
		return pinNumber;
	}

	/**
	 * Sets the value of the pinNumber property.
	 * 
	 * @param value
	 *            allowed object is {@link BigInteger }
	 * 
	 */
	public void setPINNumber(BigInteger value) {
		this.pinNumber = value;
	}

	/**
	 * Gets the value of the expiryDate property.
	 * 
	 * @return possible object is {@link XMLGregorianCalendar }
	 * 
	 */
	public XMLGregorianCalendar getExpiryDate() {
		return expiryDate;
	}

	/**
	 * Sets the value of the expiryDate property.
	 * 
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	public void setExpiryDate(XMLGregorianCalendar value) {
		this.expiryDate = value;
	}

}
