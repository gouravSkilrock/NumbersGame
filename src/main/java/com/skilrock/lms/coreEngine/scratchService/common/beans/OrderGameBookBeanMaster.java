package com.skilrock.lms.coreEngine.scratchService.common.beans;

import java.util.List;

public class OrderGameBookBeanMaster {
	private int orderId;
	private List<String> bookList;

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public List<String> getBookList() {
		return bookList;
	}

	public void setBookList(List<String> bookList) {
		this.bookList = bookList;
	}

	@Override
	public String toString() {
		return "OrderGameBookBeanMaster [orderId=" + orderId + ", bookList="
				+ bookList + "]";
	}

}
