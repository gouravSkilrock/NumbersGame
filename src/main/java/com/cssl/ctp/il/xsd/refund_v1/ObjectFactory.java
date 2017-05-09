package com.cssl.ctp.il.xsd.refund_v1;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the com.cssl.ctp.il.xsd.refund_v1 package.
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

	private final static QName _RefundResponse_QNAME = new QName(
			"http://ctp.cssl.com/il/xsd/Refund-v1.0", "RefundResponse");
	private final static QName _RefundRequest_QNAME = new QName(
			"http://ctp.cssl.com/il/xsd/Refund-v1.0", "RefundRequest");

	/**
	 * Create a new ObjectFactory that can be used to create new instances of
	 * schema derived classes for package: com.cssl.ctp.il.xsd.refund_v1
	 * 
	 */
	public ObjectFactory() {
	}

	/**
	 * Create an instance of {@link RefundRequestBodyType }
	 * 
	 */
	public RefundRequestBodyType createRefundRequestBodyType() {
		return new RefundRequestBodyType();
	}

	/**
	 * Create an instance of {@link RefundResponseBodyType }
	 * 
	 */
	public RefundResponseBodyType createRefundResponseBodyType() {
		return new RefundResponseBodyType();
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link RefundResponseBodyType }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://ctp.cssl.com/il/xsd/Refund-v1.0", name = "RefundResponse")
	public JAXBElement<RefundResponseBodyType> createRefundResponse(
			RefundResponseBodyType value) {
		return new JAXBElement<RefundResponseBodyType>(_RefundResponse_QNAME,
				RefundResponseBodyType.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link RefundRequestBodyType }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://ctp.cssl.com/il/xsd/Refund-v1.0", name = "RefundRequest")
	public JAXBElement<RefundRequestBodyType> createRefundRequest(
			RefundRequestBodyType value) {
		return new JAXBElement<RefundRequestBodyType>(_RefundRequest_QNAME,
				RefundRequestBodyType.class, null, value);
	}

}
