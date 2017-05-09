package com.skilrock.lms.scratch.orderBeans;

import java.util.List;

import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;

public class OrderCartBean {
	private int requestId;
	private String tpUserId;
	private List<OrderListBean> orderList;

	public int getRequestId() {
		return requestId;
	}

	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}

	public String getTpUserId() {
		return tpUserId;
	}

	public void setTpUserId(String tpUserId) {
		this.tpUserId = tpUserId;
	}

	public List<OrderListBean> getOrderList() {
		return orderList;
	}

	public void setOrderList(List<OrderListBean> orderList) {
		this.orderList = orderList;
	}
	
	public void validateRequestId() throws LMSException{
		if(this.getRequestId() == 0){
		  throw new LMSException(LMSErrors.INVALID_DATA_ERROR_CODE, "RequestId Is Not Present");	
		}
	}
	
	public void validateTpUserId() throws LMSException{
		if(this.getTpUserId() == null || this.getTpUserId().length() ==0){
		  throw new LMSException(LMSErrors.INVALID_DATA_ERROR_CODE, "TpUser Id Is Not Present");	
		}
	}
	
	public void validateOrderList() throws LMSException{
		if(this.getOrderList().isEmpty() || this.getOrderList().size() == 0){
		  throw new LMSException(LMSErrors.INVALID_DATA_ERROR_CODE, "Order List Is Not Present");	
		}
		// For validate OrderListBean Data
		for (OrderListBean orderListBean : this.getOrderList()) {
			orderListBean.validateGameId();
			orderListBean.validateNoOfItems();
		}
	}
}
