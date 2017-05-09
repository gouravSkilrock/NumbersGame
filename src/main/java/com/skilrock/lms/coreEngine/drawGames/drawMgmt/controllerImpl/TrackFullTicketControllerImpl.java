package com.skilrock.lms.coreEngine.drawGames.drawMgmt.controllerImpl;

import java.lang.reflect.Type;
import java.sql.Connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.skilrock.lms.beans.ServiceRequest;
import com.skilrock.lms.beans.ServiceResponse;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.utility.SendReportMailerMain;
import com.skilrock.lms.coreEngine.drawGames.drawMgmt.daoImpl.TrackFullTicketDaoImpl;
import com.skilrock.lms.coreEngine.drawGames.drawMgmt.javaBeans.TrackFullTicketBean;
import com.skilrock.lms.coreEngine.service.IServiceDelegate;
import com.skilrock.lms.coreEngine.service.ServiceDelegate;
import com.skilrock.lms.coreEngine.service.dge.ServiceMethodName;
import com.skilrock.lms.coreEngine.service.dge.ServiceName;
import com.skilrock.lms.web.drawGames.common.Util;

public class TrackFullTicketControllerImpl {
	private static final Logger logger = LoggerFactory.getLogger("TrackFullTicketControllerImpl");

	public TrackFullTicketBean fetchTicketDetails(TrackFullTicketBean ticketBean, UserInfoBean userBean, String requestIp) {
		TrackFullTicketBean responseBean = null;
		TrackFullTicketDaoImpl daoImpl = null;
		Connection connection = null;

		ServiceRequest sReq = null;
		ServiceResponse sRes = null;
		IServiceDelegate delegate = null;
		try {
			responseBean = new TrackFullTicketBean();
			responseBean.setTicketNumber(ticketBean.getTicketNumber());
			responseBean.setTicketFormat(ticketBean.getTicketFormat());
			responseBean.setRemarks(ticketBean.getRemarks());

			daoImpl = new TrackFullTicketDaoImpl();
			connection = DBConnect.getConnection();

			int maxAuthAttempt = Integer.parseInt(Utility.getPropertyValue("MAX_AUTH_ATTEMP_TRACK_TICKET"));
			int maxUnauthAttempt = Integer.parseInt(Utility.getPropertyValue("MAX_UNAUTH_ATTEMP_TRACK_TICKET"));
			boolean isValid = daoImpl.checkAuthUnauthAttempts(userBean.getUserId(), maxAuthAttempt, maxUnauthAttempt, connection);
			if(!isValid) {
				SendReportMailerMain mailerMain = new SendReportMailerMain();
				mailerMain.sendMailAfterTrackTicketLimitReached(userBean.getOrgName());

				responseBean.setStatus("MAX_ATTEMP_REACHED");
				return responseBean;
			}

			int gameId = 0;
			if("OLD".equals(ticketBean.getTicketFormat())) {
				gameId = Util.getGameIdFromLmsGameNumber(Integer.parseInt(ticketBean.getTicketNumber().substring(5, 6)));
			} else {
				gameId = Util.getGameIdFromLmsGameNumber(Integer.parseInt(ticketBean.getTicketNumber().substring(1, 2)));
				ticketBean.setReprintCount(Short.parseShort(ticketBean.getTicketNumber().substring(ticketBean.getTicketNumber().length()-1, ticketBean.getTicketNumber().length())));
				ticketBean.setTicketNumber(ticketBean.getTicketNumber().substring(0, ticketBean.getTicketNumber().length()-1));
			}
			if(gameId > 0) {
				ticketBean.setGameId(gameId);
				ticketBean.setIdGenDate(Utility.getPropertyValue("USER_MAPPING_ID_DEPLOYMENT_DATE"));
				boolean found = daoImpl.trackTicketInLMS(ticketBean, connection);
				if(found) {
					logger.info("Ticket Found in LMS.");
					sRes = new ServiceResponse();
					sReq = new ServiceRequest();
					sReq.setServiceName(ServiceName.REPORTS_MGMT);
					sReq.setServiceMethod(ServiceMethodName.TRACK_FULL_TICKET_DETAILS);
					sReq.setServiceData(ticketBean);
					delegate = ServiceDelegate.getInstance();
					sRes = delegate.getResponse(sReq);
					Type type = new TypeToken<TrackFullTicketBean>(){}.getType();
					responseBean = (TrackFullTicketBean) new Gson().fromJson((JsonElement)sRes.getResponseData(), type);
					if("SUCCESS".equals(responseBean.getStatus()) && "NEW".equals(ticketBean.getTicketFormat())) {
						if(responseBean.getReprintCount() != ticketBean.getReprintCount()) {
							responseBean.setStatus("TICKET_NOT_EXIST");
						}
					}
				} else {
					responseBean.setStatus("TICKET_NOT_EXIST");
				}
			} else {
				responseBean.setStatus("INVALID_TICKET_NUMBER");
			}

			connection.setAutoCommit(false);
			daoImpl.insertTrackTicketHistoryData(userBean.getUserId(), responseBean, requestIp, connection);
			connection.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBConnect.closeCon(connection);
		}

		return responseBean;
	}

	/*	Scheduler Code Start	*/
	public static void resetUsersAttemptLimits() {
		TrackFullTicketDaoImpl daoImpl = null;
		Connection connection = null;
		try {
			daoImpl = new TrackFullTicketDaoImpl();
			connection = DBConnect.getConnection();
			connection.setAutoCommit(false);
			daoImpl.resetUsersAttemptLimits(connection);
			connection.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBConnect.closeCon(connection);
		}
	}
	/*	Scheduler Code End	*/
}