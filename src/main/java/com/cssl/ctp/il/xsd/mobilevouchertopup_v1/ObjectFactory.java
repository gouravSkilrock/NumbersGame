package com.cssl.ctp.il.xsd.mobilevouchertopup_v1;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the com.cssl.ctp.il.xsd.mobilevouchertopup_v1
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

	private final static QName _MobileVoucherTopUpResponse_QNAME = new QName(
			"http://ctp.cssl.com/il/xsd/MobileVoucherTopUp-v1.0",
			"MobileVoucherTopUpResponse");
	private final static QName _MobileVoucherTopUpRequest_QNAME = new QName(
			"http://ctp.cssl.com/il/xsd/MobileVoucherTopUp-v1.0",
			"MobileVoucherTopUpRequest");

	/**
	 * Create a new ObjectFactory that can be used to create new instances of
	 * schema derived classes for package:
	 * com.cssl.ctp.il.xsd.mobilevouchertopup_v1
	 * 
	 */
	public ObjectFactory() {
	}

	/**
	 * Create an instance of {@link MobileVoucherTopUpRequestBodyType }
	 * 
	 */
	public MobileVoucherTopUpRequestBodyType createMobileVoucherTopUpRequestBodyType() {
		return new MobileVoucherTopUpRequestBodyType();
	}

	/**
	 * Create an instance of {@link MobileVoucherTopUpResponseBodyType }
	 * 
	 */
	public MobileVoucherTopUpResponseBodyType createMobileVoucherTopUpResponseBodyType() {
		return new MobileVoucherTopUpResponseBodyType();
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link MobileVoucherTopUpResponseBodyType }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://ctp.cssl.com/il/xsd/MobileVoucherTopUp-v1.0", name = "MobileVoucherTopUpResponse")
	public JAXBElement<MobileVoucherTopUpResponseBodyType> createMobileVoucherTopUpResponse(
			MobileVoucherTopUpResponseBodyType value) {
		return new JAXBElement<MobileVoucherTopUpResponseBodyType>(
				_MobileVoucherTopUpResponse_QNAME,
				MobileVoucherTopUpResponseBodyType.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link MobileVoucherTopUpRequestBodyType }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://ctp.cssl.com/il/xsd/MobileVoucherTopUp-v1.0", name = "MobileVoucherTopUpRequest")
	public JAXBElement<MobileVoucherTopUpRequestBodyType> createMobileVoucherTopUpRequest(
			MobileVoucherTopUpRequestBodyType value) {
		return new JAXBElement<MobileVoucherTopUpRequestBodyType>(
				_MobileVoucherTopUpRequest_QNAME,
				MobileVoucherTopUpRequestBodyType.class, null, value);
	}

}
