package com.skilrock.lms.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OlaPendingWithdrawalDataBean implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	 List<OlaPendingWithdrawalBean> pendingWithdrawalCodeList = null;
	 List<OlaPendingWithdrawalBean> amountList = null;
	 List<OlaPendingWithdrawalBean> dateList = null;

	public List<OlaPendingWithdrawalBean> getPendingWithdrawalCodeList() {
		return pendingWithdrawalCodeList;
	}

	public void setPendingWithdrawalCodeList(OlaPendingWithdrawalBean bean1) {
		if (pendingWithdrawalCodeList == null) {
			pendingWithdrawalCodeList = new ArrayList<OlaPendingWithdrawalBean>();
			// pendingWithdrawalList.add(bean1);
		}
		pendingWithdrawalCodeList.add(bean1);

	}

	public List<OlaPendingWithdrawalBean> getAmountList() {
		return amountList;
	}

	public void setAmountList(OlaPendingWithdrawalBean bean2) {
		if (amountList == null) {
			amountList = new ArrayList<OlaPendingWithdrawalBean>();
		}
		amountList.add(bean2);

	}

	public List<OlaPendingWithdrawalBean> getDateList() {
		return dateList;
	}

	public void setDateList(OlaPendingWithdrawalBean bean3) {
		if (dateList == null) {
			dateList = new ArrayList<OlaPendingWithdrawalBean>();
		}
		dateList.add(bean3);

	}
	
}
