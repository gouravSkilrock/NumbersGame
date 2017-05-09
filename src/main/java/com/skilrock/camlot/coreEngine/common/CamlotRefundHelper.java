package com.skilrock.camlot.coreEngine.common;

import java.util.Calendar;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Holder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cssl.ctp.il.wsdl.refund_v1.Fault;
import com.cssl.ctp.il.wsdl.refund_v1.RefundBindingV10;
import com.cssl.ctp.il.wsdl.refund_v1.RefundPortTypeV10;
import com.cssl.ctp.il.xsd.csheaders_v1.RequestHeaderType;
import com.cssl.ctp.il.xsd.csheaders_v1.ResponseHeaderType;
import com.cssl.ctp.il.xsd.infra_v1.FaultInfoType;
import com.cssl.ctp.il.xsd.refund_v1.RefundRequestBodyType;
import com.cssl.ctp.il.xsd.refund_v1.RefundResponseBodyType;
import com.skilrock.cs.beans.CamlotRefundBean;
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;

public class CamlotRefundHelper implements RefundPortTypeV10{
	private static Log logger = LogFactory.getLog(CamlotRefundHelper.class);
	private CamlotRefundBean cBean = new CamlotRefundBean();
	
	public CamlotRefundHelper(CamlotRefundBean bean){
		this.cBean = bean;
	}
	
	public CamlotRefundBean refundVoucher(){
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
		
		RefundRequestBodyType reqBody = new RefundRequestBodyType();
		reqBody.setOriginalClientRequestID(cBean.getOriginalClientRequestId());
		reqBody.setOriginalTransactionID(cBean.getOriginalTransactionID());
		
		Holder<ResponseHeaderType> respHeader = new Holder<ResponseHeaderType>();
		respHeader.value = new ResponseHeaderType();
		Holder<RefundResponseBodyType> respBody = new Holder<RefundResponseBodyType>();
		respBody.value = new RefundResponseBodyType();
		try{
			new CamlotRefundHelper(this.cBean).refund(reqHeader, reqBody, respHeader, respBody);
			cBean.getHeader().setMessageSequenceID(respHeader.value.getMessageSequenceID());
			cBean.getHeader().setTimeStamp(respHeader.value.getTransactionTimestamp().toString());
			cBean.getFault().setMessage(respBody.value.getMessageText());
			cBean.getFault().setCode(respBody.value.getResultCode());
			cBean.setAmount(respBody.value.getRefundValue().getAmount());
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
			logger.debug("ERROR: "+faultType.getCode()+"|Message:"+faultType.getMessage()+"|RefCode:"+faultType.getReferenceCode()+"|");
			return this.cBean;
		}
		return this.cBean;
	}
	public void refund(RequestHeaderType requestHeader,
			RefundRequestBodyType requestBody,
			Holder<ResponseHeaderType> responseHeader,
			Holder<RefundResponseBodyType> responseBody) throws Fault {
		RefundBindingV10 service = new RefundBindingV10();
		RefundPortTypeV10 port = service.getPARTNER();
		((BindingProvider)port).getRequestContext().put(BindingProvider.USERNAME_PROPERTY, "67b8c439add16df4280faf529963d3927f94a51d");
		((BindingProvider)port).getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, "ee8d9bb3923d3562a288761b6b81f160b6cf75d4");
		port.refund(requestHeader, requestBody, responseHeader, responseBody);
	}
	
}
