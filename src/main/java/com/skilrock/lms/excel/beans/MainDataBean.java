package com.skilrock.lms.excel.beans;

import java.util.List;

public class MainDataBean {

	private String mainHeader;
	private String startDate;
	private String endDate;
	private String otherData;
	private int noOfColumns;
	private List<DataBean> mainData;

	public String getMainHeader() {
		return mainHeader;
	}

	public void setMainHeader(String mainHeader) {
		this.mainHeader = mainHeader;
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

	public String getOtherData() {
		return otherData;
	}

	public void setOtherData(String otherData) {
		this.otherData = otherData;
	}

	public int getNoOfColumns() {
		return noOfColumns;
	}

	public void setNoOfColumns(int noOfColumns) {
		this.noOfColumns = noOfColumns;
	}

	public List<DataBean> getMainData() {
		return mainData;
	}

	public void setMainData(List<DataBean> mainData) {
		this.mainData = mainData;
	}

}
