package com.skilrock.camlot.coreEngine.common;

import java.math.BigInteger;
import java.util.Calendar;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.Holder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cssl.ctp.il.wsdl.mobileelectronictopup_v1.Fault;
import com.cssl.ctp.il.wsdl.mobileelectronictopup_v1.MobileElectronicTopUpBindingV10;
import com.cssl.ctp.il.wsdl.mobileelectronictopup_v1.MobileElectronicTopUpPortTypeV10;
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
import com.skilrock.cs.beans.CamlotSaleBean;
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;

public class CamlotMobileETUSaleHelper implements MobileElectronicTopUpPortTypeV10{
	private static Log logger = LogFactory.getLog(CamlotMobileETUSaleHelper.class);
	private CamlotSaleBean cBean = new CamlotSaleBean();
	
	public CamlotMobileETUSaleHelper(CamlotSaleBean bean){
		cBean = bean;
	}
	
	public CamlotSaleBean saleMobileETU(){
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
		cal.setMonth(cal1.get(Calendar.MONTH));
		cal.setDay(cal1.get(Calendar.DAY_OF_WEEK));
		reqHeader.setRequestTimeStamp(cal);
		
		MobileElectronicTopUpRequestBodyType reqBody = new MobileElectronicTopUpRequestBodyType();
		reqBody.setProductID(cBean.getProductId());
		MoneyType topupValue = new MoneyType();
		topupValue.setAmount(new Double(cBean.getAmount()+"").longValue());
		topupValue.setCurrencyCode(cBean.getCurrCode());
		reqBody.setTopUpValue(topupValue);
		reqBody.setMobileNumber(cBean.getMobileNum());
		//reqBody.setPANNumber(cBean.getPANNumber());
		//reqBody.setNotificationNumber(cBean.getNotificationNumber());
		Holder<ResponseHeaderType> respHeader = new Holder<ResponseHeaderType>();
		respHeader.value = new ResponseHeaderType();
		Holder<MobileElectronicTopUpResponseBodyType> respBody = new Holder<MobileElectronicTopUpResponseBodyType>();
		respBody.value = new MobileElectronicTopUpResponseBodyType();
		try{
			new CamlotMobileETUSaleHelper(cBean).mobileElectronicTopUp(reqHeader, reqBody, respHeader, respBody);
			cBean.getHeader().setMessageSequenceID(respHeader.value.getMessageSequenceID());
			cBean.getHeader().setTimeStamp(respHeader.value.getTransactionTimestamp().toString());
			//cBean.setBalance(new BigInteger(respBody.value.getBalance().getAmount()+"",10));
			//cBean.setExpiryDate(respBody.value.getExpiryDate().toString());
			//cBean.setMobileNum((respBody.value.getMobileNumber()));
			cBean.getHeader().setOriginalTransactionID(respBody.value.getTransactionID());
			//cBean.set(respBody.value.getProviderMessage());
			cBean.setProviderTransactionRef(respBody.value.getProviderTransactionReference());
			//cBean.setAmount(respBody.value.getTopUpValue().getAmount());
			cBean.getFault().setCode(respBody.value.getResultCode());
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

	public void mobileElectronicTopUp(RequestHeaderType requestHeader,
			MobileElectronicTopUpRequestBodyType requestBody,
			Holder<ResponseHeaderType> responseHeader,
			Holder<MobileElectronicTopUpResponseBodyType> responseBody)
			throws Fault {
		MobileElectronicTopUpBindingV10 service = new MobileElectronicTopUpBindingV10();
		MobileElectronicTopUpPortTypeV10 port = service.getPARTNER();
		/*((BindingProvider)port).getRequestContext().put(BindingProvider.USERNAME_PROPERTY, "67b8c439add16df4280faf529963d3927f94a51d");
		((BindingProvider)port).getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, "ee8d9bb3923d3562a288761b6b81f160b6cf75d4");*/
		/*the request logger*/
		logger.debug("MobileETU Request:---ClientRequestId:"+requestHeader.getClientRequestID()+"|CTPOutletId:"+requestHeader.getCTPOutletID()
					+"|Locale:"+requestHeader.getLocale()+"|MessageSequenceId:"+requestHeader.getMessageSequenceID()
					+"|MessageTypeId:"+requestHeader.getMessageTypeID()+"|RetailerStoreId:"+requestHeader.getRetailerStoreID()
					+"|UniqueId:"+requestHeader.getUniqueID()+"|EntryMethod:"+requestHeader.getEntryMethod()
					+"|PaymentMethod:"+requestHeader.getPaymentMethod()+"|RequestTimestamp:"+requestHeader.getRequestTimeStamp()
					+"|ProductId:"+requestBody.getProductID()+"|TopUpValue:"+requestBody.getTopUpValue()+"|Amount:"+requestBody.getTopUpValue().getAmount()
					+"|CurrenyCode:"+requestBody.getTopUpValue().getCurrencyCode()+"|mobileNo:"+requestBody.getMobileNumber()
					+"|PANNumber:"+requestBody.getPANNumber()+"|NotificationNum:"+requestBody.getNotificationNumber());
		try{
			port.mobileElectronicTopUp(requestHeader, requestBody, responseHeader, responseBody);
			/*the response logger*/
			logger.debug("MobileETU Response:---MessageSequenceId:"+responseHeader.value.getMessageSequenceID()
					+"|TransactionTimestamp:"+responseHeader.value.getTransactionTimestamp()
					+"|ResultCode:"+responseBody.value.getResultCode()+"|MessageText:"+responseBody.value.getMessageText()
					+"|TransactionId:"+responseBody.value.getTransactionID()
					+"|MobileNumber"+responseBody.value.getMobileNumber()
					+"|ProviderTransactionRef:"+responseBody.value.getProviderTransactionReference()
					+"|ProviderMessage:"+responseBody.value.getProviderMessage());
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public static void main(){
		CamlotSaleBean csbean = new CamlotSaleBean();
		csbean.getHeader().setClientRequestID("31274865");
		csbean.getHeader().setCTPOutletID("101001");
		csbean.getHeader().setEntryMethod(EntryMethodType.E_VOUCHER);
		csbean.setMobileNum("919654775013");
		csbean.getHeader().setMessageTypeID("MobileElectronicTopUp");
		csbean.getHeader().setOriginalTransactionID("12345");
		csbean.setProductId("1001-000050");
		csbean.getHeader().setPaymentMethod(PaymentMethodType.CASH);
		csbean.setAmount(50);
		csbean.getHeader().setTransactionType(TransactionTypeType.SALE);
		//csbean.setPANNumber("238765633");
		csbean = new CamlotMobileETUSaleHelper(csbean).saleMobileETU();
		
	}

}
