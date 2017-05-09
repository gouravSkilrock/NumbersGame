package com.skilrock.lms.dge.beans;

import java.io.Serializable;
import java.util.List;

public class ReprintTicketDAO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<Integer> cancelDrawList;

	private PurchaseTicketDAO purchaseTicketDAO;

	public List<Integer> getCancelDrawList() {
		return cancelDrawList;
	}

	public PurchaseTicketDAO getPurchaseTicketDAO() {
		return purchaseTicketDAO;
	}

	public void setCancelDrawList(List<Integer> cancelDrawList) {
		this.cancelDrawList = cancelDrawList;
	}

	public void setPurchaseTicketDAO(PurchaseTicketDAO purchaseTicketDAO) {
		this.purchaseTicketDAO = purchaseTicketDAO;
	}
}
