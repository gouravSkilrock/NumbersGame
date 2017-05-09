package com.skilrock.lms.api.reports.beans;

import java.util.List;



public class LmsApiReportConsolidateGameDataBean {

	private static final long serialVersionUID = 1L;
	private String gameCode;
	private boolean isSuccess;
	private String errorCode;
	private String startData;
	private String endData;
	private List<LmsApiReportDGConsolidateDrawBean> drawDataBeanList;

	public List<LmsApiReportDGConsolidateDrawBean> getDrawDataBeanList() {
		return drawDataBeanList;
	}
	public void setDrawDataBeanList(List<LmsApiReportDGConsolidateDrawBean> drawDataBeanList) {
		this.drawDataBeanList = drawDataBeanList;
	}
	public String getStartData() {
		return startData;
	}
	public void setStartData(String startData) {
		this.startData = startData;
	}
	public String getEndData() {
		return endData;
	}
	public void setEndData(String endData) {
		this.endData = endData;
	}
	public String getGameCode() {
		return gameCode;
	}
	public void setGameCode(String gameCode) {
		this.gameCode = gameCode;
	}
	public boolean getIsSuccess() {
		return isSuccess;
	}
	public void setIsSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
}
