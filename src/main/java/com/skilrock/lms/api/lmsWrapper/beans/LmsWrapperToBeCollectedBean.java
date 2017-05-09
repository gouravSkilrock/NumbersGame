package com.skilrock.lms.api.lmsWrapper.beans;

import java.util.Map;

public class LmsWrapperToBeCollectedBean {

	
	Map<String, String> gameNameMap;
	//Map<Integer , LmsWrapperCollectionReportOverAllBean> agentMap;
	Map<String , LmsWrapperCollectionReportOverAllBean> agentMap;
	LmsWrapperCompleteCollectionBean lmsWrapperCompleteCollectionBean;
	LmsWrapperCollectionReportOverAllBean lmsWrapperCollectionReportOverAllBean;
	public Map<String, String> getGameNameMap() {
		return gameNameMap;
	}
	public void setGameNameMap(Map<String, String> gameNameMap) {
		this.gameNameMap = gameNameMap;
	}
	public Map<String, LmsWrapperCollectionReportOverAllBean> getAgentMap() {
		return agentMap;
	}
	public void setAgentMap(
			Map<String, LmsWrapperCollectionReportOverAllBean> agentMap) {
		this.agentMap = agentMap;
	}
	public LmsWrapperCompleteCollectionBean getLmsWrapperCompleteCollectionBean() {
		return lmsWrapperCompleteCollectionBean;
	}
	public void setLmsWrapperCompleteCollectionBean(
			LmsWrapperCompleteCollectionBean lmsWrapperCompleteCollectionBean) {
		this.lmsWrapperCompleteCollectionBean = lmsWrapperCompleteCollectionBean;
	}
	public LmsWrapperCollectionReportOverAllBean getLmsWrapperCollectionReportOverAllBean() {
		return lmsWrapperCollectionReportOverAllBean;
	}
	public void setLmsWrapperCollectionReportOverAllBean(
			LmsWrapperCollectionReportOverAllBean lmsWrapperCollectionReportOverAllBean) {
		this.lmsWrapperCollectionReportOverAllBean = lmsWrapperCollectionReportOverAllBean;
	}
	
	
}
