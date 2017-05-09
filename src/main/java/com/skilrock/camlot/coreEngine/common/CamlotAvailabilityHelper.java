package com.skilrock.camlot.coreEngine.common;

import java.math.BigInteger;
import java.util.Calendar;


import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.Holder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cssl.ctp.il.wsdl.il_availability_v1.AvailabilityBindingV10;
import com.cssl.ctp.il.wsdl.il_availability_v1.AvailabilityPortTypeV10;
import com.cssl.ctp.il.wsdl.il_availability_v1.Fault;
import com.cssl.ctp.il.xsd.availability_v1.AvailabilityRequestBodyType;
import com.cssl.ctp.il.xsd.availability_v1.AvailabilityResponseBodyType;
import com.cssl.ctp.il.xsd.csheaders_v1.RequestHeaderType;
import com.cssl.ctp.il.xsd.csheaders_v1.ResponseHeaderType;
import com.cssl.ctp.il.xsd.infra_v1.EntryMethodType;
import com.cssl.ctp.il.xsd.infra_v1.FaultInfoType;
import com.cssl.ctp.il.xsd.infra_v1.PaymentMethodType;
import com.cssl.ctp.il.xsd.infra_v1.TransactionTypeType;
import com.skilrock.cs.beans.CamlotAvailBean;
import com.skilrock.cs.beans.CamlotFaultBean;
import com.skilrock.cs.beans.CamlotSOAPHeaderBean;
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;

public class CamlotAvailabilityHelper implements AvailabilityPortTypeV10{
	private static Log logger = LogFactory.getLog(CamlotAvailabilityHelper.class);
	private CamlotAvailBean cBean = new CamlotAvailBean();

	public CamlotAvailabilityHelper(CamlotAvailBean bean){
		cBean = bean;
	}
	public CamlotAvailabilityHelper(){
		super();
	}
	
	public CamlotAvailBean checkServiceAvailabilty(){
		RequestHeaderType reqHeader = new RequestHeaderType();
		reqHeader.setClientRequestID(cBean.getHeader().getClientRequestID());
		reqHeader.setCTPOutletID(cBean.getHeader().getCTPOutletID());
		reqHeader.setUniqueID(cBean.getHeader().getUniqueID());
		reqHeader.setEntryMethod(cBean.getHeader().getEntryMethod());
		reqHeader.setLocale(cBean.getHeader().getLocale());
		reqHeader.setMessageSequenceID(cBean.getHeader().getMessageSequenceID());
		reqHeader.setMessageTypeID(cBean.getHeader().getMessageTypeID());
		reqHeader.setClientRequestID(cBean.getHeader().getClientRequestID());
		reqHeader.setPaymentMethod(cBean.getHeader().getPaymentMethod());
		reqHeader.setRetailerStoreID(cBean.getHeader().getRetailerStoreID());
		
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(new java.util.Date());
		XMLGregorianCalendar cal = new XMLGregorianCalendarImpl();
		cal.setTime(cal1.get(Calendar.HOUR), cal1.get(Calendar.MINUTE), cal1.get(Calendar.SECOND), cal1.get(Calendar.MILLISECOND));
		cal.setYear(cal1.get(Calendar.YEAR));
		cal.setMonth(cal1.get(Calendar.MONTH));
		cal.setDay(cal1.get(Calendar.DAY_OF_WEEK));
		reqHeader.setRequestTimeStamp(cal);
		
		AvailabilityRequestBodyType reqBody = new AvailabilityRequestBodyType();
		reqBody.setVerbose(cBean.isVerbose());
		Holder<ResponseHeaderType> respHeader = new Holder<ResponseHeaderType>();
		respHeader.value = new ResponseHeaderType();
		Holder<AvailabilityResponseBodyType> respBody = new Holder<AvailabilityResponseBodyType>();
		respBody.value = new AvailabilityResponseBodyType();
		try{
			new CamlotAvailabilityHelper(cBean).availability(reqHeader, reqBody, respHeader, respBody);
			cBean.getHeader().setMessageSequenceID(respHeader.value.getMessageSequenceID());
			cBean.getHeader().setTimeStamp(respHeader.value.getTransactionTimestamp().toString());
			cBean.getFault().setMessage(respBody.value.getMessageText());
			cBean.getFault().setCode(respBody.value.getResultCode());
			cBean.setAvailable(true);
			logger.debug("response message Sequence Id: "+cBean.getHeader().getMessageSequenceID());
			logger.debug("response timestamp: "+cBean.getHeader().getTimeStamp());
			logger.debug("response message text: "+cBean.getFault().getMessage());
			logger.debug("response result code: "+cBean.getFault().getCode());
		}catch(Fault f){
			f.printStackTrace();
			FaultInfoType faultType = f.getFaultInfo();
			cBean.getFault().setCode(faultType.getCode());
			cBean.getFault().setMessage(faultType.getMessage());
			cBean.getFault().setReferenceCode(faultType.getReferenceCode());
			cBean.setAvailable(false);
			logger.debug("ERROR: "+faultType.getCode()+"|Message:"+faultType.getMessage()+"|RefCode:"+faultType.getReferenceCode()+"|");
			return this.cBean;
		}
		return this.cBean;
	}
	public void availability(RequestHeaderType requestHeader,
			AvailabilityRequestBodyType requestBody,
			Holder<ResponseHeaderType> responseHeader,
			Holder<AvailabilityResponseBodyType> responseBody) throws Fault {
			AvailabilityBindingV10 service = new AvailabilityBindingV10();
			AvailabilityPortTypeV10 port = service.getPARTNER();
			/*((BindingProvider)port).getRequestContext().put(BindingProvider.USERNAME_PROPERTY, "67b8c439add16df4280faf529963d3927f94a51d");
			((BindingProvider)port).getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, "ee8d9bb3923d3562a288761b6b81f160b6cf75d4");*/
			/*the request logger*/
			logger.debug("Availability Request:---ClientRequestId:"+requestHeader.getClientRequestID()+"|CTPOutletId:"+requestHeader.getCTPOutletID()
						+"|Locale:"+requestHeader.getLocale()+"|MessageSequenceId:"+requestHeader.getMessageSequenceID()
						+"|MessageTypeId:"+requestHeader.getMessageTypeID()+"|RetailerStoreId:"+requestHeader.getRetailerStoreID()
						+"|UniqueId:"+requestHeader.getUniqueID()+"|EntryMethod:"+requestHeader.getEntryMethod()
						+"|PaymentMethod:"+requestHeader.getPaymentMethod()+"|RequestTimestamp:"+requestHeader.getRequestTimeStamp()
						+"|Verbose:"+requestBody.isVerbose());
			try{
				port.availability(requestHeader, requestBody, responseHeader, responseBody);
				/*the response logger*/
				logger.debug("Availability Response:---MessageSequenceId:"+responseHeader.value.getMessageSequenceID()
						+"|TransactionTimestamp:"+responseHeader.value.getTransactionTimestamp()
						+"|ResultCode:"+responseBody.value.getResultCode()+"|MessageText:"+responseBody.value.getMessageText());
			}catch(Exception e){
				e.printStackTrace();
			}
	}
	
	public static void main(String args[]){
		
		CamlotAvailBean testBean = new CamlotAvailBean();
		CamlotSOAPHeaderBean header = new CamlotSOAPHeaderBean();
		CamlotFaultBean fault = new CamlotFaultBean();
		header.setClientRequestID("12455337");
		header.setTimeStamp("");
		header.setCTPOutletID("101001");
		header.setUniqueID("67b8c439add16df4280faf529963d3927f94a51d");
		header.setEntryMethod(EntryMethodType.MAGNETIC_SWIPE);
		header.setLocale("en_GB");
		header.setMessageSequenceID("12455337");
		header.setMessageTypeID("Availablity");
		header.setClientRequestID("764764");
		header.setOriginalTransactionID("35139005");
		header.setPaymentMethod(PaymentMethodType.CASH);
		header.setRetailerStoreID("12345");
		header.setTransactionType(TransactionTypeType.SALE);
		testBean.setHeader(header);
		testBean.setFault(fault);
		testBean.setVerbose(true);
		testBean = (new CamlotAvailabilityHelper(testBean)).checkServiceAvailabilty();
	}
}
