package com.cssl.ctp.il.xsd.availability_v1;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the com.cssl.ctp.il.xsd.availability_v1
 * package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the
 * Java representation for XML content. The Java representation of XML content
 * can consist of schema derived interfaces and classes representing the binding
 * of schema type definitions, element declarations and model groups. Factory
 * methods for each of these are provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

	private final static QName _AvailabilityRequest_QNAME = new QName(
			"http://ctp.cssl.com/il/xsd/Availability-v1.0",
			"AvailabilityRequest");
	private final static QName _AvailabilityResponse_QNAME = new QName(
			"http://ctp.cssl.com/il/xsd/Availability-v1.0",
			"AvailabilityResponse");

	/**
	 * Create a new ObjectFactory that can be used to create new instances of
	 * schema derived classes for package: com.cssl.ctp.il.xsd.availability_v1
	 * 
	 */
	public ObjectFactory() {
	}

	/**
	 * Create an instance of {@link AvailabilityResponseBodyType }
	 * 
	 */
	public AvailabilityResponseBodyType createAvailabilityResponseBodyType() {
		return new AvailabilityResponseBodyType();
	}

	/**
	 * Create an instance of {@link AvailabilityRequestBodyType }
	 * 
	 */
	public AvailabilityRequestBodyType createAvailabilityRequestBodyType() {
		return new AvailabilityRequestBodyType();
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link AvailabilityRequestBodyType }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://ctp.cssl.com/il/xsd/Availability-v1.0", name = "AvailabilityRequest")
	public JAXBElement<AvailabilityRequestBodyType> createAvailabilityRequest(
			AvailabilityRequestBodyType value) {
		return new JAXBElement<AvailabilityRequestBodyType>(
				_AvailabilityRequest_QNAME, AvailabilityRequestBodyType.class,
				null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link AvailabilityResponseBodyType }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://ctp.cssl.com/il/xsd/Availability-v1.0", name = "AvailabilityResponse")
	public JAXBElement<AvailabilityResponseBodyType> createAvailabilityResponse(
			AvailabilityResponseBodyType value) {
		return new JAXBElement<AvailabilityResponseBodyType>(
				_AvailabilityResponse_QNAME,
				AvailabilityResponseBodyType.class, null, value);
	}

}
