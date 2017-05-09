package com.skilrock.lms.dge.beans;

import java.util.List;

public class DrawWinningReportBean {

	List<Long> timeList = null;
	List<String> winningDataList = null;
	List<String> statusList = null;
	List<DrawDetailsBean> drawDetailsList;

	public List<Long> getTimeList() {
		return timeList;
	}

	public void setTimeList(List<Long> timeList) {
		this.timeList = timeList;
	}

	public List<String> getWinningDataList() {
		return winningDataList;
	}

	public void setWinningDataList(List<String> winningDataList) {
		this.winningDataList = winningDataList;
	}

	public List<String> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<String> statusList) {
		this.statusList = statusList;
	}

	public List<DrawDetailsBean> getDrawDetailsList() {
		return drawDetailsList;
	}

	public void setDrawDetailsList(List<DrawDetailsBean> drawDetailsList) {
		this.drawDetailsList = drawDetailsList;
	}

	@Override
	public String toString() {
		return "DrawWinningReportBean [timeList=" + timeList
				+ ", winningDataList=" + winningDataList + ", statusList="
				+ statusList + ", drawDetailsList=" + drawDetailsList + "]";
	}

}
