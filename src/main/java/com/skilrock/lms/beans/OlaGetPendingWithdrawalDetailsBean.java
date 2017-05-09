package com.skilrock.lms.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OlaGetPendingWithdrawalDetailsBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String messageId;
	private String errorCode;
	private String errorText;
	private String returnType;
	 public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	List<OlaPendingWithdrawalDataBean> pendingWithdrawalList = null;
	

	public List<OlaPendingWithdrawalDataBean> getPendingWithdrawalList() {
		return pendingWithdrawalList;
	}

	public void setPendingWithdrawalList(OlaPendingWithdrawalDataBean bean1) {
		if (pendingWithdrawalList == null) {
			pendingWithdrawalList = new ArrayList<OlaPendingWithdrawalDataBean>();
			// pendingWithdrawalList.add(bean1);
		}
		pendingWithdrawalList.add(bean1);

	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorText() {
		return errorText;
	}

	public void setErrorText(String errorText) {
		this.errorText = errorText;
	}

}
