package com.cssl.ctp.il.xsd.infra_v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for ProductRequestBodyType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="ProductRequestBodyType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ProductID" type="{http://ctp.cssl.com/il/xsd/Infra-v1.0}ProductIDType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProductRequestBodyType", propOrder = { "productID" })
public class ProductRequestBodyType {

	@XmlElement(name = "ProductID", required = true)
	protected String productID;

	/**
	 * Gets the value of the productID property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getProductID() {
		return productID;
	}

	/**
	 * Sets the value of the productID property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setProductID(String value) {
		this.productID = value;
	}

}
