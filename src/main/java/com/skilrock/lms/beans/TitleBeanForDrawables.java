package com.skilrock.lms.beans;

import java.util.List;

public class TitleBeanForDrawables {

	private String chartTitle;
	private String chartSubTitle;
	private String chartType;
	private String currencySymbol;
	private String xAxisTitle;
	private String yAxisTitle;
	private List<Object> data; // Category List
	public String getChartTitle() {
		return chartTitle;
	}
	public void setChartTitle(String chartTitle) {
		this.chartTitle = chartTitle;
	}
	public String getChartSubTitle() {
		return chartSubTitle;
	}
	public void setChartSubTitle(String chartSubTitle) {
		this.chartSubTitle = chartSubTitle;
	}
	public String getChartType() {
		return chartType;
	}
	public void setChartType(String chartType) {
		this.chartType = chartType;
	}
	public String getCurrencySymbol() {
		return currencySymbol;
	}
	public void setCurrencySymbol(String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}
	public String getxAxisTitle() {
		return xAxisTitle;
	}
	public void setxAxisTitle(String xAxisTitle) {
		this.xAxisTitle = xAxisTitle;
	}
	public String getyAxisTitle() {
		return yAxisTitle;
	}
	public void setyAxisTitle(String yAxisTitle) {
		this.yAxisTitle = yAxisTitle;
	}
	public List<Object> getData() {
		return data;
	}
	public void setData(List<Object> data) {
		this.data = data;
	}
}
