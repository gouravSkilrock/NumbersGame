package com.skilrock.lms.rest.services.common;

import java.lang.reflect.Type;
import java.sql.Connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.skilrock.lms.ajax.AjaxRequestHelper;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.coreEngine.sportsLottery.common.controllerImpl.CommonMethodsControllerImpl;
import com.skilrock.lms.coreEngine.virtualSport.beans.TPSaleRequestBean;
import com.skilrock.lms.rest.services.bean.TPResponseBean;
import com.skilrock.lms.rest.services.bean.TPTxRequestBean;
import com.skilrock.lms.rest.services.bean.TPTxResponseBean;

public class ReqResParser {
	private static Logger loggger = LoggerFactory.getLogger(ReqResParser.class);
	private volatile static ReqResParser reqResParser = null;

	private ReqResParser(){}
	public static ReqResParser getInstance() {
		if (reqResParser == null) {
			synchronized (ReqResParser.class) {
				if (reqResParser == null) {
					loggger.info("getInstance(): First time getInstance was invoked!");
					reqResParser = new ReqResParser();
				}
			}
		}
		return reqResParser;

	}
	
	public void prepareResponseForSale(UserInfoBean userInfoBean, TPTxRequestBean tpTransactionBean, TPTxResponseBean tpTxResponseBean, TPResponseBean tpResponseBean, Connection con) {
		String balString = new AjaxRequestHelper().getAvlblCreditAmt(userInfoBean, con);
		// double bal = Double.parseDouble(balString.split("\\=")[3]);
		// NumberFormat nf = NumberFormat.getInstance();
		// nf.setMinimumFractionDigits(2);
		// tpTxResponseBean.setAvailBal(Double.parseDouble(nf.format(bal).replaceAll(",", "")));
		tpTxResponseBean.setAvailBal(Double.parseDouble(balString.split("\\=")[3]));
		tpTxResponseBean.setGameId(tpTransactionBean.getGameId());
		tpTxResponseBean.setTxType(tpTransactionBean.getTxType());
		tpTxResponseBean.setTxAmount(tpTransactionBean.getTxAmount());
		tpTxResponseBean.setGameTypeId(tpTransactionBean.getGameTypeId());
		tpTxResponseBean.setTicketNumber(tpTransactionBean.getTicketNumber());
		tpTxResponseBean.setUserMappingId(userInfoBean.getCurrentUserMappingId());
		if ("IW".equals(tpTransactionBean.getServiceCode()))
			tpTxResponseBean.setAdvMessageMap(com.skilrock.lms.coreEngine.instantWin.common.controllerImpl.CommonMethodsControllerImpl.getInstance().getIWAdvMessages(userInfoBean.getUserOrgId(), tpTransactionBean.getGameId(), "SALE"));
		else if ("SLE".equals(tpTransactionBean.getServiceCode()))
			tpTxResponseBean.setAdvMessageMap(CommonMethodsControllerImpl.getInstance().getSLEAdvMessages(userInfoBean.getUserOrgId(), tpTransactionBean.getGameTypeId()));
		tpResponseBean.setResponseData(tpTxResponseBean);
	}
	
	public TPTxRequestBean fetchReqForTx(String tpRequestBean){
		JsonObject reqJsonObject = (JsonObject) new JsonParser().parse(tpRequestBean);
		Type elementType = new TypeToken<TPTxRequestBean>(){}.getType();
		TPTxRequestBean tpTxRequestBean=new Gson().fromJson(reqJsonObject, elementType);
		return tpTxRequestBean;
		
	}
	
	public TPSaleRequestBean fetchReqForVBTx(String tpRequestBean){
		TPSaleRequestBean tpTxRequestBean=null;
		JsonArray js=(JsonArray)new JsonParser().parse(tpRequestBean);
		try{
			tpTxRequestBean=new Gson().fromJson(js.get(0), TPSaleRequestBean.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tpTxRequestBean;
	}
		
}
