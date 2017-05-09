package com.skilrock.lms.beans;

import java.util.List;
import java.util.Map;

public class DispatchOrderResponse {
	private int responseCode;
	private Map<String, Map<String, List<String>>> orderData;

	public DispatchOrderResponse(int responseCode, Map<String, Map<String, List<String>>> orderData) {
		super();
		this.responseCode = responseCode;
		this.orderData = orderData;
	}

	public DispatchOrderResponse(int responseCode) {
		super();
		this.responseCode = responseCode;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public Map<String, Map<String, List<String>>> getOrderData() {
		return orderData;
	}

	public void setOrderData(Map<String, Map<String, List<String>>> orderData) {
		this.orderData = orderData;
	}

}
