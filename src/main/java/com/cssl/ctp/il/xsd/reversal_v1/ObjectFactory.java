package com.cssl.ctp.il.xsd.reversal_v1;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the com.cssl.ctp.il.xsd.reversal_v1 package.
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

	private final static QName _ReversalRequest_QNAME = new QName(
			"http://ctp.cssl.com/il/xsd/Reversal-v1.0", "ReversalRequest");
	private final static QName _ReversalResponse_QNAME = new QName(
			"http://ctp.cssl.com/il/xsd/Reversal-v1.0", "ReversalResponse");

	/**
	 * Create a new ObjectFactory that can be used to create new instances of
	 * schema derived classes for package: com.cssl.ctp.il.xsd.reversal_v1
	 * 
	 */
	public ObjectFactory() {
	}

	/**
	 * Create an instance of {@link ReversalRequestBodyType }
	 * 
	 */
	public ReversalRequestBodyType createReversalRequestBodyType() {
		return new ReversalRequestBodyType();
	}

	/**
	 * Create an instance of {@link ReversalResponseBodyType }
	 * 
	 */
	public ReversalResponseBodyType createReversalResponseBodyType() {
		return new ReversalResponseBodyType();
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link ReversalRequestBodyType }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://ctp.cssl.com/il/xsd/Reversal-v1.0", name = "ReversalRequest")
	public JAXBElement<ReversalRequestBodyType> createReversalRequest(
			ReversalRequestBodyType value) {
		return new JAXBElement<ReversalRequestBodyType>(_ReversalRequest_QNAME,
				ReversalRequestBodyType.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link ReversalResponseBodyType }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://ctp.cssl.com/il/xsd/Reversal-v1.0", name = "ReversalResponse")
	public JAXBElement<ReversalResponseBodyType> createReversalResponse(
			ReversalResponseBodyType value) {
		return new JAXBElement<ReversalResponseBodyType>(
				_ReversalResponse_QNAME, ReversalResponseBodyType.class, null,
				value);
	}

}
