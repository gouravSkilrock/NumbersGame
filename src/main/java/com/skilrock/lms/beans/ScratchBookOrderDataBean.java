package com.skilrock.lms.beans;

import java.util.Map;

public class ScratchBookOrderDataBean {
	private int orderId;
	private String orderDate;
	private String orderStatus;
	private String dlNbr;
	private Map<String, Map<String, Integer>> gameDataMap;

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getDlNbr() {
		return dlNbr;
	}

	public void setDlNbr(String dlNbr) {
		this.dlNbr = dlNbr;
	}

	public Map<String, Map<String, Integer>> getGameDataMap() {
		return gameDataMap;
	}

	public void setGameDataMap(Map<String, Map<String, Integer>> gameDataMap) {
		this.gameDataMap = gameDataMap;
	}

	@Override
	public String toString() {
		return "ScratchBookOrderDataBean [orderId=" + orderId + ", orderDate="
				+ orderDate + ", orderStatus=" + orderStatus + ", dlNbr="
				+ dlNbr + ", gameDataMap=" + gameDataMap + "]";
	}

}
