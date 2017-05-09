package com.skilrock.lms.web.drawGames.drawMgmt;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.opensymphony.xwork2.ModelDriven;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.coreEngine.drawGames.drawMgmt.controllerImpl.TrackFullTicketControllerImpl;
import com.skilrock.lms.coreEngine.drawGames.drawMgmt.javaBeans.TrackFullTicketBean;

public class TrackFullTicketAction extends BaseAction implements ModelDriven<TrackFullTicketBean> {
	private static final long serialVersionUID = 1L;

	public TrackFullTicketAction() {
		super(TrackFullTicketAction.class);
	}

	private TrackFullTicketBean ticketBean;

	public TrackFullTicketBean getTicketBean() {
		return ticketBean;
	}

	public void setTicketBean(TrackFullTicketBean ticketBean) {
		this.ticketBean = ticketBean;
	}

	@Override
	public TrackFullTicketBean getModel() {
		ticketBean = new TrackFullTicketBean();
		return ticketBean;
	}

	public String trackTicketDetails() {
		Matcher matcher = Pattern.compile("\\d+").matcher(ticketBean.getTicketNumber());
		if(!matcher.matches()) {
			ticketBean.setStatus("INVALID_TICKET_NUMBER");
			return INPUT;
		} else if(ticketBean.getTicketNumber().length()<13 || ticketBean.getTicketNumber().length()>14) {
			ticketBean.setStatus("INVALID_TICKET_LENGTH");
			return INPUT;
		} else if("NEW".equals(ticketBean.getTicketFormat()) && ticketBean.getTicketNumber().length() != 14) {
			ticketBean.setStatus("INVALID_TICKET_LENGTH_NEW");
			return INPUT;
		}

		UserInfoBean userBean = null;
		String requestIp = null;
		TrackFullTicketControllerImpl controllerImpl = null;
		try {
			userBean = getUserBean();
			requestIp = request.getRemoteAddr();
			controllerImpl = new TrackFullTicketControllerImpl();
			ticketBean = controllerImpl.fetchTicketDetails(ticketBean, userBean, requestIp);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return SUCCESS;
	}
}