package com.skilrock.lms.beans;

import java.io.Serializable;
import java.util.List;

public class InvTransitionWarehouseWiseBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<String> headers;
	private List<InvTransitionWarehouseWiseDataBean> warehouseWiseDataBeans;
	private int bookLocation;
	private ScratchBookDataBean scratchBookDataBean;

	public List<String> getHeaders() {
		return headers;
	}

	public void setHeaders(List<String> headers) {
		this.headers = headers;
	}

	public List<InvTransitionWarehouseWiseDataBean> getWarehouseWiseDataBeans() {
		return warehouseWiseDataBeans;
	}

	public void setWarehouseWiseDataBeans(
			List<InvTransitionWarehouseWiseDataBean> warehouseWiseDataBeans) {
		this.warehouseWiseDataBeans = warehouseWiseDataBeans;
	}

	public int getBookLocation() {
		return bookLocation;
	}

	public void setBookLocation(int bookLocation) {
		this.bookLocation = bookLocation;
	}

	public ScratchBookDataBean getScratchBookDataBean() {
		return scratchBookDataBean;
	}

	public void setScratchBookDataBean(ScratchBookDataBean scratchBookDataBean) {
		this.scratchBookDataBean = scratchBookDataBean;
	}

	@Override
	public String toString() {
		return "InvTransitionWarehouseWiseBean [headers=" + headers
				+ ", warehouseWiseDataBeans=" + warehouseWiseDataBeans
				+ ", bookLocation=" + bookLocation + ", scratchBookDataBean="
				+ scratchBookDataBean + "]";
	}

}
