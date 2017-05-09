package com.skilrock.lms.web.accMgmt.common;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.TrainingExpenseBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.accMgmt.common.UpdateTrainningExpHelper;
import com.skilrock.lms.web.reportsMgmt.common.ReportUtility;

public class UpdateTrainningExpAction extends ActionSupport implements ServletRequestAware, ServletResponseAware {

	private static final long serialVersionUID = 1L;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String trainingType;
	private int agtId[];
	private double trVar[];
	private double extraTrVar[];
	private Map<Integer, String> gameMap;
	private int gameNo;
	private String serviceBeanData;
	private int serviceId;
	private double incentiveVar[];

	public int[] getAgtId() {
		return agtId;
	}

	public void setAgtId(int[] agtId) {
		this.agtId = agtId;
	}

	public double[] getTrVar() {
		return trVar;
	}

	public void setTrVar(double[] trVar) {
		this.trVar = trVar;
	}

	public String getTrainingType() {
		return trainingType;
	}

	public void setTrainingType(String trainingType) {
		this.trainingType = trainingType;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setServletRequest(HttpServletRequest req) {
		this.request = req;

	}

	public void setServletResponse(HttpServletResponse res) {
		response = res;

	}

	public int getServiceId() {
		return serviceId;
	}

	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}

	public void setGameNo(int gameNo) {
		this.gameNo = gameNo;
	}

	public int getGameNo() {
		return gameNo;
	}

	public Map<Integer, String> getGameMap() {
		return gameMap;
	}

	public void setGameMap(Map<Integer, String> gameMap) {
		this.gameMap = gameMap;
	}

	public double[] getExtraTrVar() {
		return extraTrVar;
	}

	public void setExtraTrVar(double extraTrVar[]) {
		this.extraTrVar = extraTrVar;
	}
	
	public String getServiceBeanData() {
		return serviceBeanData;
	}

	public void setServiceBeanData(String serviceBeanData) {
		this.serviceBeanData = serviceBeanData;
	}
	
	public double[] getIncentiveVar() {
		return incentiveVar;
	}

	public void setIncentiveVar(double[] incentiveVar) {
		this.incentiveVar = incentiveVar;
	}

	public String updateTrainMenu() {
		try {
			// setGameMap(ReportUtility.fetchDrawDataMenu());
			serviceBeanData = new Gson().toJson(ReportUtility.fetchGameServiceWise());
		} catch (LMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public String updateTrainView() throws Exception {
		UpdateTrainningExpHelper helper = new UpdateTrainningExpHelper();
		Map<Integer, TrainingExpenseBean> tranOrgMap = null;
		if ("daily".equalsIgnoreCase(trainingType)) {
			tranOrgMap = helper.fetchDailyAgentTrainingExp(trainingType, gameNo, serviceId);
		} else {
			tranOrgMap = helper.fetchAgentTrainingExp(trainingType, gameNo, serviceId);
		}
		HttpSession session = request.getSession();
		session.setAttribute("tranOrgMap", tranOrgMap);
		return SUCCESS;
	}

	public String updateTrVar() {
		UpdateTrainningExpHelper helper = new UpdateTrainningExpHelper();
		HttpSession session = request.getSession();
		UserInfoBean userBean = (UserInfoBean) session.getAttribute("USER_INFO");
		int userId = userBean.getUserId();
		Map<Integer, TrainingExpenseBean> tranOrgMap = (Map<Integer, TrainingExpenseBean>) session.getAttribute("tranOrgMap");

		if ("daily".equalsIgnoreCase(trainingType)) {
			helper.updateDailyAgentTrExp(agtId, trVar, extraTrVar, incentiveVar, userId, trainingType, tranOrgMap, gameNo, serviceId);
			tranOrgMap = helper.fetchDailyAgentTrainingExp(trainingType, gameNo, serviceId);
		} else {
			helper.updateAgentTrExp(agtId, trVar, extraTrVar, incentiveVar, userId, trainingType, tranOrgMap, gameNo, serviceId);
			tranOrgMap = helper.fetchAgentTrainingExp(trainingType, gameNo, serviceId);
		}

		/*
		 * helper.updateAgentTrExp(agtId, trVar, userId, trainingType,
		 * tranOrgMap,gameNo); tranOrgMap =
		 * helper.fetchAgentTrainingExp(trainingType,gameNo);
		 */
		session.setAttribute("tranOrgMap", tranOrgMap);
		return SUCCESS;
	}
}
