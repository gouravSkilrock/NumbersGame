package com.skilrock.lms.web.reportsMgmt.common;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.skilrock.lms.api.lmsWrapper.LmsWrapperServiceApiHelper;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.coreEngine.userMgmt.common.VirtualSportsControllerImpl;
import com.skilrock.lms.coreEngine.virtualSport.beans.TPSaleRequestBean;
import com.skilrock.lms.coreEngine.virtualSport.beans.VSRequestBean;
import com.skilrock.lms.coreEngine.virtualSport.beans.VSResponseBean;
import com.skilrock.lms.coreEngine.virtualSport.playMgmt.daoImpl.VirtualSportGamePlayDaoImpl;
import com.skilrock.lms.userMgmt.javaBeans.UpdateClaimCriteriaBean;

public class VSSaleReconciliationAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String startDate;
	private String endDate;
	private String status;
	Map<String, VSRequestBean> reconcDataMap = null ;
	private VSRequestBean transIds[] ;
	private String jsonParamData;
	private List<VSRequestBean> requestBeanList;
	//private VSRequestBean data;

	public VSSaleReconciliationAction() {
		super(VSSaleReconciliationAction.class);
	}
	
	
	public String getDataToReconcile() {
		VSSaleReconciliationController controller = new VSSaleReconciliationController();

		try{
			reconcDataMap = controller.getDataToReconcile(startDate, endDate, status);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return SUCCESS ;
	}
	
	public String settleTransactions(){
		requestBeanList = new ArrayList<VSRequestBean>();
		JsonArray dataArray = new JsonParser().parse(jsonParamData).getAsJsonArray();
		UserInfoBean userInfoBean = null;
/*		VSRequestBean requestBean = null ;
		VSResponseBean responseBean = null ;*/
		JsonObject updateData = null;
		Connection conn = null ;
		LmsWrapperServiceApiHelper helper = new LmsWrapperServiceApiHelper() ;
		TPSaleRequestBean tpTransactionBean = new TPSaleRequestBean();
		try{
			conn = DBConnect.getConnection();
			for(int i=0; i<dataArray.size(); i++) {
				updateData = dataArray.get(i).getAsJsonObject();
				userInfoBean = helper.getUserDataFromUserId(updateData.get("userId").getAsInt());
				tpTransactionBean.setGameId(updateData.get("gameId").getAsInt());
				tpTransactionBean.setTicketNumber(updateData.get("ticketNumber").getAsString());
				tpTransactionBean.setTxnId(updateData.get("txnId").getAsString());
				VirtualSportGamePlayDaoImpl.virtualBettingRefundTicket(tpTransactionBean,userInfoBean, conn);
	    	}
			
		/*	for (VSRequestBean map : requestBeanList) {
				requestBean = new VSRequestBean();
				requestBean.setTxnId(map.getTxnId());
				responseBean = VirtualSportsControllerImpl.Single.INSTANCE.getInstance().getTxnStatus(requestBean);
				if ("error".equalsIgnoreCase(responseBean.getVsCommonResponseBean().getResult())) {
					userInfoBean = helper.getUserDataFromUserId(map.getUserId());
					tpTransactionBean.setGameId(map.getGameId());
					tpTransactionBean.setTicketNumber(map.getTicketNumber());
					tpTransactionBean.setTxnId(requestBean.getTxnId());
					VirtualSportGamePlayDaoImpl.virtualBettingRefundTicket(tpTransactionBean,userInfoBean, conn);
				}
			}*/
		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			DBConnect.closeResource(conn);}
		return SUCCESS ;
	}


	
	
	public List<VSRequestBean> getRequestBeanList() {
		return requestBeanList;
	}


	public void setRequestBeanList(List<VSRequestBean> requestBeanList) {
		this.requestBeanList = requestBeanList;
	}


	public String getStartDate() {
		return startDate;
	}


	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}


	public String getEndDate() {
		return endDate;
	}


	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public Map<String, VSRequestBean> getReconcDataMap() {
		return reconcDataMap;
	}


	public void setReconcDataMap(Map<String, VSRequestBean> reconcDataMap) {
		this.reconcDataMap = reconcDataMap;
	}


	public VSRequestBean[] getTransIds() {
		return transIds;
	}


	public void setTransIds(VSRequestBean[] transIds) {
		this.transIds = transIds;
	}


	public String getJsonParamData() {
		return jsonParamData;
	}


	public void setJsonParamData(String jsonParamData) {
		this.jsonParamData = jsonParamData;
	}
	
	
}
