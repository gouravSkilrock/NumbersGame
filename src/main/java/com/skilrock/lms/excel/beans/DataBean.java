package com.skilrock.lms.excel.beans;

import java.util.List;

public class DataBean {

	private String header;
	private int noOfColumns;
	private List<HeaderBean> headerData;
	private List<RowBean> rowData;

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public int getNoOfColumns() {
		return noOfColumns;
	}

	public void setNoOfColumns(int noOfColumns) {
		this.noOfColumns = noOfColumns;
	}

	public List<HeaderBean> getHeaderData() {
		return headerData;
	}

	public void setHeaderData(List<HeaderBean> headerData) {
		this.headerData = headerData;
	}

	public List<RowBean> getRowData() {
		return rowData;
	}

	public void setRowData(List<RowBean> rowData) {
		this.rowData = rowData;
	}

}
