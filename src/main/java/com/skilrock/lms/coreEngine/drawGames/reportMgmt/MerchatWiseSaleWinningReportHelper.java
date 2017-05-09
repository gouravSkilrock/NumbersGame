package com.skilrock.lms.coreEngine.drawGames.reportMgmt;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.skilrock.lms.beans.ServiceRequest;
import com.skilrock.lms.beans.ServiceResponse;
import com.skilrock.lms.coreEngine.service.IServiceDelegate;
import com.skilrock.lms.coreEngine.service.ServiceDelegate;
import com.skilrock.lms.coreEngine.service.dge.ServiceMethodName;
import com.skilrock.lms.coreEngine.service.dge.ServiceName;
import com.skilrock.lms.dge.beans.MerchantWiseSaleWinningBean;
public class MerchatWiseSaleWinningReportHelper {
	public MerchantWiseSaleWinningBean fetchSaleWinningData(String startDate,String endDate){
		ServiceRequest sReq=null;
		ServiceResponse sResp=null;
		JsonObject reqData=null;
		IServiceDelegate delegate=null;
		MerchantWiseSaleWinningBean merchantwiseSaleWinningBean=null;
		try{
			sReq=new ServiceRequest();
			sResp=new ServiceResponse();
			reqData=new JsonObject();
			reqData.addProperty("startDate", startDate);
			reqData.addProperty("endDate", endDate);
			sReq.setServiceData(reqData);
			sReq.setServiceName(ServiceName.REPORTS_MGMT);
			sReq.setServiceMethod(ServiceMethodName.FETCH_MERCHANT_SALE_WINNING_DATA);
			delegate=ServiceDelegate.getInstance();
			sResp=delegate.getResponse(sReq);
			if(sResp.getIsSuccess()){
				merchantwiseSaleWinningBean = new Gson().fromJson(sResp.getResponseData().toString(), MerchantWiseSaleWinningBean.class);
                return merchantwiseSaleWinningBean;
			}
			
		}catch (Exception e) {
 			e.printStackTrace();
		}
		return null;
	}
	public MerchantWiseSaleWinningBean fetchMtnSaleWinningData(String startDate,String endDate){
		ServiceRequest sReq=null;
		ServiceResponse sResp=null;
		JsonObject reqData=null;
		IServiceDelegate delegate=null;
		MerchantWiseSaleWinningBean merchantwiseSaleWinningBean=null;
		try{
			merchantwiseSaleWinningBean=new MerchantWiseSaleWinningBean();
			sReq=new ServiceRequest();
			sResp=new ServiceResponse();
			reqData=new JsonObject();
			reqData.addProperty("startDate", startDate);
			reqData.addProperty("endDate", endDate);
			sReq.setServiceData(reqData);
			sReq.setServiceName(ServiceName.REPORTS_MGMT);
			sReq.setServiceMethod(ServiceMethodName.FETCH_MTN_SALE_WINNING_DATA);
			delegate=ServiceDelegate.getInstance();
			sResp=delegate.getResponse(sReq);
			if(sResp.getIsSuccess()){
				
				merchantwiseSaleWinningBean = new Gson().fromJson(sResp.getResponseData().toString(), MerchantWiseSaleWinningBean.class);
				merchantwiseSaleWinningBean.setResponse(true);
				return merchantwiseSaleWinningBean;
			}else{
				 merchantwiseSaleWinningBean.setResponse(false);
				 merchantwiseSaleWinningBean.setArchivingDate(sResp.getResponseData().toString().replaceAll("\"",""));
				 return merchantwiseSaleWinningBean;
			}
		}catch (Exception e) {
 			e.printStackTrace();
		}
		return null;
	}
}
