package com.skilrock.lms.rest.scratchService.inventoryMgmt.service;

import com.skilrock.lms.rest.services.bean.ScracthMgmtBean;

public interface BookActivationService {

	public boolean isBookNumberValid(ScracthMgmtBean bean);
	public String updateBookNumberStatus(ScracthMgmtBean bean);
	public String ticketByTicketSale(ScracthMgmtBean bean);
	public String ticketByTicketUnSold(ScracthMgmtBean bean);
}
