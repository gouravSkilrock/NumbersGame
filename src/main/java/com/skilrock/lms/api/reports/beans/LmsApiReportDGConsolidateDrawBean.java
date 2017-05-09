package com.skilrock.lms.api.reports.beans;


public class LmsApiReportDGConsolidateDrawBean {
	private static final long serialVersionUID = 1L;
	// Draw Specific Information
	private String drawDateTime;
	private String drawDay;
	private String drawFreezeTime;
	private String drawEventId;
	private String drawName;
	private String drawStatus;
	private String winningResult;	
	//Client Specific Beans 
	private LmsApiReportDGPMSSaleBean pmsSaleBean;
	private LmsApiReportDGLMSSaleBean  lmsSaleBean;
	public String getDrawDateTime() {
		return drawDateTime;
	}
	public String getDrawDay() {
		return drawDay;
	}
	public String getDrawFreezeTime() {
		return drawFreezeTime;
	}
	public String getDrawName() {
		return drawName;
	}
	public String getDrawStatus() {
		return drawStatus;
	}
	public String getWinningResult() {
		return winningResult;
	}
	public void setDrawDateTime(String drawDateTime) {
		this.drawDateTime = drawDateTime;
	}
	public void setDrawDay(String drawDay) {
		this.drawDay = drawDay;
	}
	public void setDrawFreezeTime(String drawFreezeTime) {
		this.drawFreezeTime = drawFreezeTime;
	}
	public void setDrawName(String drawName) {
		this.drawName = drawName;
	}
	public void setDrawStatus(String drawStatus) {
		this.drawStatus = drawStatus;
	}
	public void setWinningResult(String winningResult) {
		this.winningResult = winningResult;
	}
	public LmsApiReportDGPMSSaleBean getPmsSaleBean() {
		return pmsSaleBean;
	}
	public void setPmsSaleBean(LmsApiReportDGPMSSaleBean pmsSaleBean) {
		this.pmsSaleBean = pmsSaleBean;
	}
	public LmsApiReportDGLMSSaleBean getLmsSaleBean() {
		return lmsSaleBean;
	}
	public void setLmsSaleBean(LmsApiReportDGLMSSaleBean lmsSaleBean) {
		this.lmsSaleBean = lmsSaleBean;
	}
	public String getDrawEventId() {
		return drawEventId;
	}
	public void setDrawEventId(String drawEventId) {
		this.drawEventId = drawEventId;
	}
	

}
