package com.cssl.ctp.il.xsd.mobileelectronictopup_v1;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the
 * com.cssl.ctp.il.xsd.mobileelectronictopup_v1 package.
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

	private final static QName _MobileElectronicTopUpResponse_QNAME = new QName(
			"http://ctp.cssl.com/il/xsd/MobileElectronicTopUp-v1.0",
			"MobileElectronicTopUpResponse");
	private final static QName _MobileElectronicTopUpRequest_QNAME = new QName(
			"http://ctp.cssl.com/il/xsd/MobileElectronicTopUp-v1.0",
			"MobileElectronicTopUpRequest");

	/**
	 * Create a new ObjectFactory that can be used to create new instances of
	 * schema derived classes for package:
	 * com.cssl.ctp.il.xsd.mobileelectronictopup_v1
	 * 
	 */
	public ObjectFactory() {
	}

	/**
	 * Create an instance of {@link MobileElectronicTopUpRequestBodyType }
	 * 
	 */
	public MobileElectronicTopUpRequestBodyType createMobileElectronicTopUpRequestBodyType() {
		return new MobileElectronicTopUpRequestBodyType();
	}

	/**
	 * Create an instance of {@link MobileElectronicTopUpResponseBodyType }
	 * 
	 */
	public MobileElectronicTopUpResponseBodyType createMobileElectronicTopUpResponseBodyType() {
		return new MobileElectronicTopUpResponseBodyType();
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link MobileElectronicTopUpResponseBodyType }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://ctp.cssl.com/il/xsd/MobileElectronicTopUp-v1.0", name = "MobileElectronicTopUpResponse")
	public JAXBElement<MobileElectronicTopUpResponseBodyType> createMobileElectronicTopUpResponse(
			MobileElectronicTopUpResponseBodyType value) {
		return new JAXBElement<MobileElectronicTopUpResponseBodyType>(
				_MobileElectronicTopUpResponse_QNAME,
				MobileElectronicTopUpResponseBodyType.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link MobileElectronicTopUpRequestBodyType }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://ctp.cssl.com/il/xsd/MobileElectronicTopUp-v1.0", name = "MobileElectronicTopUpRequest")
	public JAXBElement<MobileElectronicTopUpRequestBodyType> createMobileElectronicTopUpRequest(
			MobileElectronicTopUpRequestBodyType value) {
		return new JAXBElement<MobileElectronicTopUpRequestBodyType>(
				_MobileElectronicTopUpRequest_QNAME,
				MobileElectronicTopUpRequestBodyType.class, null, value);
	}

}
