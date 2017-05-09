package com.cssl.ctp.il.wsdl.refund_v1;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import com.cssl.ctp.il.xsd.csheaders_v1.RequestHeaderType;
import com.cssl.ctp.il.xsd.csheaders_v1.ResponseHeaderType;
import com.cssl.ctp.il.xsd.infra_v1.FaultInfoType;
import com.cssl.ctp.il.xsd.refund_v1.RefundRequestBodyType;
import com.cssl.ctp.il.xsd.refund_v1.RefundResponseBodyType;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the com.cssl.ctp.il.wsdl.refund_v1 package.
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
			"http://ctp.cssl.com/il/wsdl/Refund-v1.0", "RefundResponse");
	private final static QName _RefundRequest_QNAME = new QName(
			"http://ctp.cssl.com/il/wsdl/Refund-v1.0", "RefundRequest");
	private final static QName _FaultInfo_QNAME = new QName(
			"http://ctp.cssl.com/il/wsdl/Refund-v1.0", "FaultInfo");
	private final static QName _RequestHeader_QNAME = new QName(
			"http://ctp.cssl.com/il/wsdl/Refund-v1.0", "RequestHeader");
	private final static QName _ResponseHeader_QNAME = new QName(
			"http://ctp.cssl.com/il/wsdl/Refund-v1.0", "ResponseHeader");

	/**
	 * Create a new ObjectFactory that can be used to create new instances of
	 * schema derived classes for package: com.cssl.ctp.il.wsdl.refund_v1
	 * 
	 */
	public ObjectFactory() {
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link RefundResponseBodyType }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://ctp.cssl.com/il/wsdl/Refund-v1.0", name = "RefundResponse")
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
	@XmlElementDecl(namespace = "http://ctp.cssl.com/il/wsdl/Refund-v1.0", name = "RefundRequest")
	public JAXBElement<RefundRequestBodyType> createRefundRequest(
			RefundRequestBodyType value) {
		return new JAXBElement<RefundRequestBodyType>(_RefundRequest_QNAME,
				RefundRequestBodyType.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link FaultInfoType }
	 * {@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://ctp.cssl.com/il/wsdl/Refund-v1.0", name = "FaultInfo")
	public JAXBElement<FaultInfoType> createFaultInfo(FaultInfoType value) {
		return new JAXBElement<FaultInfoType>(_FaultInfo_QNAME,
				FaultInfoType.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link RequestHeaderType }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://ctp.cssl.com/il/wsdl/Refund-v1.0", name = "RequestHeader")
	public JAXBElement<RequestHeaderType> createRequestHeader(
			RequestHeaderType value) {
		return new JAXBElement<RequestHeaderType>(_RequestHeader_QNAME,
				RequestHeaderType.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link ResponseHeaderType }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://ctp.cssl.com/il/wsdl/Refund-v1.0", name = "ResponseHeader")
	public JAXBElement<ResponseHeaderType> createResponseHeader(
			ResponseHeaderType value) {
		return new JAXBElement<ResponseHeaderType>(_ResponseHeader_QNAME,
				ResponseHeaderType.class, null, value);
	}

}
