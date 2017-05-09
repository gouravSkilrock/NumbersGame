package com.skilrock.lms.web.userMgmt.common;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.skilrock.lms.beans.OrgBean;
import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.coreEngine.userMgmt.common.CommonFunctionsHelper;
import com.skilrock.lms.userMgmt.controllerImpl.AgentRetailerClaimCriteriaControllerImpl;
import com.skilrock.lms.userMgmt.javaBeans.UpdateClaimCriteriaBean;
import com.skilrock.lms.userMgmt.javaBeans.UpdatePayoutCenterBean;

public class AgentRetailerClaimCriteriaAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	private List<OrgBean> agentList; 
	private List<UpdateClaimCriteriaBean> retCriteriaList;
	private List<UpdatePayoutCenterBean> payoutCenterList;
	private int agentOrgId;
	private String orgType;
	private String jsonParamData;

	public AgentRetailerClaimCriteriaAction() {
		super(AgentRetailerClaimCriteriaAction.class.getName());
	}

	public List<OrgBean> getAgentList() {
		return agentList;
	}

	public void setAgentList(List<OrgBean> agentList) {
		this.agentList = agentList;
	}

	public List<UpdateClaimCriteriaBean> getRetCriteriaList() {
		return retCriteriaList;
	}

	public void setRetCriteriaList(List<UpdateClaimCriteriaBean> retCriteriaList) {
		this.retCriteriaList = retCriteriaList;
	}

	public List<UpdatePayoutCenterBean> getPayoutCenterList() {
		return payoutCenterList;
	}

	public void setPayoutCenterList(List<UpdatePayoutCenterBean> payoutCenterList) {
		this.payoutCenterList = payoutCenterList;
	}

	public int getAgentOrgId() {
		return agentOrgId;
	}

	public void setAgentOrgId(int agentOrgId) {
		this.agentOrgId = agentOrgId;
	}

	public String getOrgType() {
		return orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public String getJsonParamData() {
		return jsonParamData;
	}

	public void setJsonParamData(String jsonParamData) {
		this.jsonParamData = jsonParamData;
	}

	/*	Update Sold Ticket Claim Criteria Start	*/

	public String updateClaimCriteriaMenu() {
		agentList = new CommonFunctionsHelper().getActiveAgentList();
		return SUCCESS;
	}

	public String updateClaimCriteriaDetails() {
		try {
			retCriteriaList = AgentRetailerClaimCriteriaControllerImpl.getInstance().fetchRetailerCriteriaList(agentOrgId);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return SUCCESS;
	}

	public void retailerClaimCriteriaSetToDefault() {
		retCriteriaList = new ArrayList<UpdateClaimCriteriaBean>();
		try {
			JsonArray dataArray = new JsonParser().parse(jsonParamData).getAsJsonArray();

			boolean selfRetailer = "YES".equals(Utility.getPropertyValue("CLAIM_AT_SELF_RET")) ? true : false;
			boolean selfAgent = "YES".equals(Utility.getPropertyValue("CLAIM_AT_SELF_AGT")) ? true : false;
			boolean otherRetailerSameAgent = "YES".equals(Utility.getPropertyValue("CLAIM_AT_OTHER_RET_SAME_AGT")) ? true : false;
			boolean otherRetailer = "YES".equals(Utility.getPropertyValue("CLAIM_AT_OTHER_RET")) ? true : false;
			boolean otherAgent = "YES".equals(Utility.getPropertyValue("CLAIM_AT_OTHER_AGT")) ? true : false;
			boolean atBO = "YES".equals(Utility.getPropertyValue("CLAIM_AT_BO")) ? true : false;

			UpdateClaimCriteriaBean criteriaBean = null;
			JsonObject updateData = null;
			for(int i=0; i<dataArray.size(); i++) {
        		updateData = dataArray.get(i).getAsJsonObject();
        		criteriaBean = new UpdateClaimCriteriaBean();
				criteriaBean.setRetOrgId(updateData.get("retOrgId").getAsInt());
				criteriaBean.setSelfRetailer(selfRetailer);
				criteriaBean.setSelfAgent(selfAgent);
				criteriaBean.setOtherRetailerSameAgent(otherRetailerSameAgent);
				criteriaBean.setOtherRetailer(otherRetailer);
				criteriaBean.setOtherAgent(otherAgent);
				criteriaBean.setAtBO(atBO);
				retCriteriaList.add(criteriaBean);
        	}

			AgentRetailerClaimCriteriaControllerImpl.getInstance().retailerClaimCriteriaUpdate(retCriteriaList, getUserBean().getUserId(), request.getRemoteAddr());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void retailerClaimCriteriaUpdate() {
		retCriteriaList = new ArrayList<UpdateClaimCriteriaBean>();
		try {
			JsonArray dataArray = new JsonParser().parse(jsonParamData).getAsJsonArray();
			UpdateClaimCriteriaBean criteriaBean = null;
			JsonObject updateData = null;
			for(int i=0; i<dataArray.size(); i++) {
        		updateData = dataArray.get(i).getAsJsonObject();
        		criteriaBean = new UpdateClaimCriteriaBean();
				criteriaBean.setRetOrgId(updateData.get("retOrgId").getAsInt());
				criteriaBean.setSelfRetailer(updateData.get("selfRetailer").getAsBoolean());
				criteriaBean.setSelfAgent(updateData.get("selfAgent").getAsBoolean());
				criteriaBean.setOtherRetailerSameAgent(updateData.get("otherRetailerSameAgent").getAsBoolean());
				criteriaBean.setOtherRetailer(updateData.get("otherRetailer").getAsBoolean());
				criteriaBean.setOtherAgent(updateData.get("otherAgent").getAsBoolean());
				criteriaBean.setAtBO(updateData.get("atBO").getAsBoolean());
				retCriteriaList.add(criteriaBean);
        	}

			AgentRetailerClaimCriteriaControllerImpl.getInstance().retailerClaimCriteriaUpdate(retCriteriaList, getUserBean().getUserId(), request.getRemoteAddr());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/*	Update Sold Ticket Claim Criteria End	*/

	/*	Update Payout Center Start	*/

	public String updatePayoutCenterMenu() {
		agentList = new CommonFunctionsHelper().getActiveAgentList();
		return SUCCESS;
	}

	public String updatePayoutCenterDetails() {
		try {
			payoutCenterList = AgentRetailerClaimCriteriaControllerImpl.getInstance().fetchOrgPayoutList(agentOrgId);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return SUCCESS;
	}

	public void payoutCenterSetToDefault() {
		payoutCenterList = new ArrayList<UpdatePayoutCenterBean>();
		try {
			JsonArray dataArray = new JsonParser().parse(jsonParamData).getAsJsonArray();

			boolean claimAtSelf = false;
			boolean claimAtOther = false;
			double verificationLimit = 0.00;
			double minClaimAmount = 0.00;
			double maxClaimAmount = 0.00;
			double claimLimit = 0.00;

			if("AGENT".equals(orgType)) {
				claimAtSelf = "YES".equals(Utility.getPropertyValue("SELF_CLAIM_AGT")) ? true : false;
				claimAtOther = "YES".equals(Utility.getPropertyValue("OTHER_CLAIM_AGT")) ? true : false;
				verificationLimit = Double.parseDouble(Utility.getPropertyValue("agtVerLimit"));
				minClaimAmount = Double.parseDouble(Utility.getPropertyValue("MIN_CLAIM_PER_TICKET_AGT"));
				maxClaimAmount = Double.parseDouble(Utility.getPropertyValue("MAX_CLAIM_PER_TICKET_AGT"));
				claimLimit = Double.parseDouble(Utility.getPropertyValue("MAX_PER_DAY_PAY_LIMIT_FOR_AGENT"));
			} else if("RETAILER".equals(orgType)) {
				claimAtSelf = "YES".equals(Utility.getPropertyValue("SELF_CLAIM_RET")) ? true : false;
				claimAtOther = "YES".equals(Utility.getPropertyValue("OTHER_CLAIM_RET")) ? true : false;
				verificationLimit = Double.parseDouble(Utility.getPropertyValue("retVerLimit"));
				minClaimAmount = Double.parseDouble(Utility.getPropertyValue("MIN_CLAIM_PER_TICKET_RET"));
				maxClaimAmount = Double.parseDouble(Utility.getPropertyValue("MAX_CLAIM_PER_TICKET_RET"));
				claimLimit = 0.00;
			}

			UpdatePayoutCenterBean payoutCenterBean = null;
			JsonObject updateData = null;
			for(int i=0; i<dataArray.size(); i++) {
        		updateData = dataArray.get(i).getAsJsonObject();
        		payoutCenterBean = new UpdatePayoutCenterBean();
        		payoutCenterBean.setOrgId(updateData.get("orgId").getAsInt());
        		payoutCenterBean.setClaimAtSelf(claimAtSelf);
        		payoutCenterBean.setClaimAtOther(claimAtOther);
        		payoutCenterBean.setVerificationLimit(String.valueOf(verificationLimit));
        		payoutCenterBean.setClaimLimit(String.valueOf(claimLimit));
        		payoutCenterBean.setMinClaimAmount(String.valueOf(minClaimAmount));
        		payoutCenterBean.setMaxClaimAmount(String.valueOf(maxClaimAmount));
				payoutCenterList.add(payoutCenterBean);
        	}

			AgentRetailerClaimCriteriaControllerImpl.getInstance().payoutCenterUpdate(payoutCenterList, getUserBean().getUserId());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void payoutCenterUpdate() {
		payoutCenterList = new ArrayList<UpdatePayoutCenterBean>();
		try {
			JsonArray dataArray = new JsonParser().parse(jsonParamData).getAsJsonArray();
			UpdatePayoutCenterBean payoutCenterBean = null;
			JsonObject updateData = null;
			for(int i=0; i<dataArray.size(); i++) {
        		updateData = dataArray.get(i).getAsJsonObject();
        		payoutCenterBean = new UpdatePayoutCenterBean();
        		payoutCenterBean.setOrgId(updateData.get("orgId").getAsInt());
        		payoutCenterBean.setClaimAtSelf(updateData.get("claimAtSelf").getAsBoolean());
        		payoutCenterBean.setClaimAtOther(updateData.get("claimAtOther").getAsBoolean());
        		payoutCenterBean.setVerificationLimit(updateData.get("verificationLimit").getAsString());
        		if("AGENT".equals(orgType)) {
        			payoutCenterBean.setClaimLimit(updateData.get("claimLimit").getAsString());
        		} else if("RETAILER".equals(orgType)) {
        			payoutCenterBean.setClaimLimit(String.valueOf(0.00));
        		}
        		payoutCenterBean.setMinClaimAmount(updateData.get("minClaimAmount").getAsString());
        		payoutCenterBean.setMaxClaimAmount(updateData.get("maxClaimAmount").getAsString());
        		payoutCenterList.add(payoutCenterBean);
        	}

			AgentRetailerClaimCriteriaControllerImpl.getInstance().payoutCenterUpdate(payoutCenterList, getUserBean().getUserId());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/*	Update Payout Center End	*/
}