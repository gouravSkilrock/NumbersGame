package com.cssl.ctp.il.xsd.availability_v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * The body of the request.
 * 
 * <p>
 * Java class for AvailabilityRequestBodyType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="AvailabilityRequestBodyType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Verbose" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AvailabilityRequestBodyType", propOrder = { "verbose" })
public class AvailabilityRequestBodyType {

	@XmlElement(name = "Verbose")
	protected boolean verbose;

	/**
	 * Gets the value of the verbose property.
	 * 
	 */
	public boolean isVerbose() {
		return verbose;
	}

	/**
	 * Sets the value of the verbose property.
	 * 
	 */
	public void setVerbose(boolean value) {
		this.verbose = value;
	}

}
