package com.skilrock.camlot.coreEngine.common;

import java.math.BigInteger;
import java.util.Calendar;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.Holder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cssl.ctp.il.wsdl.il_availability_v1.AvailabilityBindingV10;
import com.cssl.ctp.il.wsdl.il_availability_v1.AvailabilityPortTypeV10;
import com.cssl.ctp.il.wsdl.mobileelectronictopup_v1.MobileElectronicTopUpBindingV10;
import com.cssl.ctp.il.wsdl.mobileelectronictopup_v1.MobileElectronicTopUpPortTypeV10;
import com.cssl.ctp.il.wsdl.mobilevouchertopup_v1.Fault;
import com.cssl.ctp.il.wsdl.mobilevouchertopup_v1.MobileVoucherTopUpBindingV10;
import com.cssl.ctp.il.wsdl.mobilevouchertopup_v1.MobileVoucherTopUpPortTypeV10;
import com.cssl.ctp.il.xsd.csheaders_v1.RequestHeaderType;
import com.cssl.ctp.il.xsd.csheaders_v1.ResponseHeaderType;
import com.cssl.ctp.il.xsd.infra_v1.EntryMethodType;
import com.cssl.ctp.il.xsd.infra_v1.FaultInfoType;
import com.cssl.ctp.il.xsd.infra_v1.MoneyType;
import com.cssl.ctp.il.xsd.infra_v1.PaymentMethodType;
import com.cssl.ctp.il.xsd.infra_v1.TransactionTypeType;
import com.cssl.ctp.il.xsd.mobileelectronictopup_v1.MobileElectronicTopUpRequestBodyType;
import com.cssl.ctp.il.xsd.mobileelectronictopup_v1.MobileElectronicTopUpResponseBodyType;
import com.cssl.ctp.il.xsd.mobilevouchertopup_v1.MobileVoucherTopUpRequestBodyType;
import com.cssl.ctp.il.xsd.mobilevouchertopup_v1.MobileVoucherTopUpResponseBodyType;
import com.skilrock.cs.beans.CamlotFaultBean;
import com.skilrock.cs.beans.CamlotSOAPHeaderBean;
import com.skilrock.cs.beans.CamlotSaleBean;
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;

public class CamlotMobileVTUSaleHelper implements MobileVoucherTopUpPortTypeV10 {

	private static Log logger = LogFactory.getLog(CamlotMobileVTUSaleHelper.class);
	private CamlotSaleBean cBean = new CamlotSaleBean();
	
	public CamlotMobileVTUSaleHelper(CamlotSaleBean bean){
		cBean = bean;
	}
	
	public CamlotSaleBean saleMobileVTU(){
		RequestHeaderType reqHeader = new RequestHeaderType();
		reqHeader.setClientRequestID(cBean.getHeader().getClientRequestID());
		reqHeader.setCTPOutletID(cBean.getHeader().getCTPOutletID());
		reqHeader.setLocale(cBean.getHeader().getLocale());
		reqHeader.setUniqueID(cBean.getHeader().getUniqueID());
		reqHeader.setEntryMethod(cBean.getHeader().getEntryMethod());
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
		cal.setMonth(cal1.get(Calendar.MONTH)+1);
		cal.setDay(cal1.get(Calendar.DAY_OF_WEEK));
		reqHeader.setRequestTimeStamp(cal);
		
		MobileVoucherTopUpRequestBodyType reqBody = new MobileVoucherTopUpRequestBodyType();
		reqBody.setProductID(cBean.getProductId());
		MoneyType topupValue = new MoneyType();
		topupValue.setAmount(new Double(cBean.getAmount()).longValue()*100); //we have to append two zeros for decimal i.e. 23.45 is same as 2345 and 23.00 is same as 2300
		topupValue.setCurrencyCode(cBean.getCurrCode());
		reqBody.setTopUpValue(topupValue);
		Holder<ResponseHeaderType> respHeader = new Holder<ResponseHeaderType>();
		respHeader.value = new ResponseHeaderType();
		Holder<MobileVoucherTopUpResponseBodyType> respBody = new Holder<MobileVoucherTopUpResponseBodyType>();
		respBody.value = new MobileVoucherTopUpResponseBodyType();
		try{
			new CamlotMobileVTUSaleHelper(cBean).mobileVoucherTopUp(reqHeader, reqBody, respHeader, respBody);
			cBean.getHeader().setMessageSequenceID(respHeader.value.getMessageSequenceID());
			cBean.getHeader().setTimeStamp(respHeader.value.getTransactionTimestamp().toString());
			cBean.getHeader().setOriginalTransactionID(respBody.value.getTransactionID());
			//cBean.setBalance(respBody.value.getBalance().getAmount());
			cBean.setPINNumber(respBody.value.getPINNumber());
			cBean.setExpiryDate(respBody.value.getExpiryDate().toString());
			//cBean.setMobileNum((respBody.value.getMobileNumber()));
			cBean.setProviderMessage(respBody.value.getProviderMessage());
			cBean.setProviderTransactionRef(respBody.value.getProviderTransactionReference());
			//cBean.setAmount(respBody.value.getTopUpValue().getAmount());
			cBean.getFault().setCode(respBody.value.getResultCode());
			cBean.getFault().setMessage(respBody.value.getMessageText());
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

	public void mobileVoucherTopUp(RequestHeaderType requestHeader,
			MobileVoucherTopUpRequestBodyType requestBody,
			Holder<ResponseHeaderType> responseHeader,
			Holder<MobileVoucherTopUpResponseBodyType> responseBody)
			throws Fault {
		MobileVoucherTopUpBindingV10 service = new MobileVoucherTopUpBindingV10();
		MobileVoucherTopUpPortTypeV10 port = service.getPARTNER();
		/*((BindingProvider)port).getRequestContext().put(BindingProvider.USERNAME_PROPERTY, "67b8c439add16df4280faf529963d3927f94a51d");
		((BindingProvider)port).getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, "ee8d9bb3923d3562a288761b6b81f160b6cf75d4");*/
		/*the request logger*/
		logger.debug("MobileVTU Request:---ClientRequestId:"+requestHeader.getClientRequestID()+"|CTPOutletId:"+requestHeader.getCTPOutletID()
					+"|Locale:"+requestHeader.getLocale()+"|MessageSequenceId:"+requestHeader.getMessageSequenceID()
					+"|MessageTypeId:"+requestHeader.getMessageTypeID()+"|RetailerStoreId:"+requestHeader.getRetailerStoreID()
					+"|UniqueId:"+requestHeader.getUniqueID()+"|EntryMethod:"+requestHeader.getEntryMethod()
					+"|PaymentMethod:"+requestHeader.getPaymentMethod()+"|RequestTimestamp:"+requestHeader.getRequestTimeStamp()
					+"|ProductId:"+requestBody.getProductID()+"|TopUpValue:"+requestBody.getTopUpValue());
		try{
			port.mobileVoucherTopUp(requestHeader, requestBody, responseHeader, responseBody);
			/*the response logger*/
			logger.debug("MobileVTU Response:---MessageSequenceId:"+responseHeader.value.getMessageSequenceID()
					+"|TransactionTimestamp:"+responseHeader.value.getTransactionTimestamp()
					+"|ResultCode:"+responseBody.value.getResultCode()+"|MessageText:"+responseBody.value.getMessageText()
					+"|TransactionId:"+responseBody.value.getTransactionID()
					+"|PINNumber"+responseBody.value.getPINNumber()
					+"|PINExpiry:"+responseBody.value.getExpiryDate()
					+"|ProviderTransactionRef:"+responseBody.value.getProviderTransactionReference()
					+"|ProviderMessage:"+responseBody.value.getProviderMessage());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]){
		CamlotSaleBean csbean = new CamlotSaleBean();
		CamlotSOAPHeaderBean header = new CamlotSOAPHeaderBean();
		CamlotFaultBean fault = new CamlotFaultBean();
		header.setClientRequestID("12455338");
		header.setTimeStamp("");
		header.setCTPOutletID("101001");
		header.setUniqueID("67b8c439add16df4280faf529963d3927f94a51d");
		header.setEntryMethod(EntryMethodType.MAGNETIC_SWIPE);
		header.setLocale("en_GB");
		header.setMessageSequenceID("12455338");
		header.setMessageTypeID("MobileVoucherTopUp");
		header.setOriginalTransactionID("35139005");
		header.setPaymentMethod(PaymentMethodType.CASH);
		header.setRetailerStoreID("12345");
		header.setTransactionType(TransactionTypeType.SALE);
		csbean.setHeader(header);
		csbean.setProductId("2002");
		csbean.setAmount(50000);
		csbean.setCurrCode("TZS");
		csbean.setFault(fault);
		csbean = new CamlotMobileVTUSaleHelper(csbean).saleMobileVTU();
		
	}
	
	
	

}
