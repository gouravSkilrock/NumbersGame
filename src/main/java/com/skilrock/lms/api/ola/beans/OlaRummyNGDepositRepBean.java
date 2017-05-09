package com.skilrock.lms.api.ola.beans;

import java.util.ArrayList;
import java.util.List;





public class OlaRummyNGDepositRepBean {
	

	String requestType;
	String fromDate;	
	String toDate;
	List<OlaRummyNGTxnRepBean> rummyngTxnList;
	String errorCode;
	String errorMsg;
	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public List<OlaRummyNGTxnRepBean> getRummyngTxnList() {
		return rummyngTxnList;
	}
	public void setRummyngTxnList(OlaRummyNGTxnRepBean rummyngTxn ) {
		if(rummyngTxnList == null){
			rummyngTxnList = new ArrayList<OlaRummyNGTxnRepBean>();
		}
		rummyngTxnList.add(rummyngTxn);
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
	
	
	

	

}
