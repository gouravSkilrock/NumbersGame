package com.skilrock.lms.dge.beans;


import java.io.Serializable;
import java.util.List;

public class TicketWiseDataBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<AnalysisReportDrawBean> anaBeanList;
	private boolean isArchieved;
	private String archData;
	private String errorMessage;
	private int errorCode;

	public List<AnalysisReportDrawBean> getAnaBeanList() {
		return anaBeanList;
	}

	public void setAnaBeanList(List<AnalysisReportDrawBean> anaBeanList) {
		this.anaBeanList = anaBeanList;
	}

	public boolean isArchieved() {
		return isArchieved;
	}

	public void setArchieved(boolean isArchieved) {
		this.isArchieved = isArchieved;
	}

	public String getArchData() {
		return archData;
	}

	public void setArchData(String archData) {
		this.archData = archData;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	
	

}
