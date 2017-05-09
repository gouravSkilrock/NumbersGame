package com.skilrock.lms.web.drawGames.drawMgmt;

import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.controller.drawMgmtController.DrawMgmtController;
import com.skilrock.lms.web.drawGames.pwtMgmt.javaBeans.PwtVerifyTicketBean;

/**
 * @author Nikhil K. Bansal
 * @category Track Thirt Party Ticket Action
 */

public class TrackTPTicketAction extends BaseAction{
	
	public TrackTPTicketAction() {
		super(TrackTPTicketAction.class);
	}
	
	private String ticketNumber;
	private PwtVerifyTicketBean pwtVerifyBean;
	public String getTicketNumber() {
		return ticketNumber;
	}
	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}
	public PwtVerifyTicketBean getPwtVerifyBean() {
		return pwtVerifyBean;
	}
	public void setPwtVerifyBean(PwtVerifyTicketBean pwtVerifyBean) {
		this.pwtVerifyBean = pwtVerifyBean;
	}
	
	
	public String fetchTPTktDetails(){
		try {
			pwtVerifyBean=DrawMgmtController.getInstance().fetchTPTktDetailsFromDGE(ticketNumber);
			if(pwtVerifyBean!=null){
				return SUCCESS;
			}
			else{
				return "applicationError"; 
			}
		}catch (LMSException el) {
			el.printStackTrace();
			request.setAttribute("LMS_EXCEPTION", el.getErrorMessage());
			return ERROR;
		}catch (Exception e) {
			e.printStackTrace();
			return "applicationError";
		}
	}
	
}