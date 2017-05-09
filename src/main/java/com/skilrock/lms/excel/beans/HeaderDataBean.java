package com.skilrock.lms.excel.beans;

public class HeaderDataBean {

	private String data;
	private String isDataSet = "YES";
	private String isMergedCell = "NO";
	private String mergeRange = "0,0,0,0";

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getIsDataSet() {
		return isDataSet;
	}

	public void setIsDataSet(String isDataSet) {
		this.isDataSet = isDataSet;
	}

	public String getIsMergedCell() {
		return isMergedCell;
	}

	public void setIsMergedCell(String isMergedCell) {
		this.isMergedCell = isMergedCell;
	}

	public String getMergeRange() {
		return mergeRange;
	}

	public void setMergeRange(String mergeRange) {
		this.mergeRange = mergeRange;
	}

}
