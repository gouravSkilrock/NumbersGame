package com.skilrock.lms.embedded.loginMgmt;

import java.sql.*;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionContext;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameRPOSHelper;
import com.skilrock.lms.coreEngine.userMgmt.common.CommonFunctionsHelper;
import com.skilrock.lms.embedded.drawGames.common.EmbeddedErrors;
import com.skilrock.lms.web.drawGames.common.Util;

/**
 * @author gauravk This Class is used to check the heart beat of Terminals
 * 
 */
public class TerminalsActivityAction implements ServletRequestAware, ServletResponseAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Log logger = LogFactory.getLog(TerminalsActivityAction.class);

	private HttpServletResponse response;
	private HttpServletRequest request;
	private String terminalId;
	private String username;
	private String deviceType;
	private String version;
	private String profile;
	private String simType;
	private long LSTktNo;

	public void checkTerminalHeartBeat() throws Exception{
		ServletContext sc = ServletActionContext.getServletContext();
		Map currentUserSessionMap = (Map) sc.getAttribute("LOGGED_IN_USERS");
		if (currentUserSessionMap == null) {
			response.getOutputStream().write(("ErrorMsg:" + EmbeddedErrors.SESSION_EXPIRED + "ErrorCode:01|").getBytes());
			return;
		}

		/*HttpSession session = (HttpSession) currentUserSessionMap.get(username);
		if (!CommonFunctionsHelper.isSessionValid(session)) {
			response.getOutputStream().write(("ErrorMsg:" + EmbeddedErrors.SESSION_EXPIRED + "ErrorCode:01|").getBytes());
			return;
		}*/

		/*
		UserInfoBean userBean = (UserInfoBean) session.getAttribute("USER_INFO");
		String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
		int autoCancelHoldDays=Integer.parseInt((String) sc.getAttribute("AUTO_CANCEL_CLOSER_DAYS"));
		long lastPrintedTicket=0;
		int gameId = 0;

		if(LSTktNo !=0){
			lastPrintedTicket = LSTktNo/100;
			gameId = Util.getGameIdFromGameNumber(Util.getGamenoFromTktnumber(String.valueOf(LSTktNo)));
		}

		String actionName=ActionContext.getContext().getName();
		DrawGameRPOSHelper drawGameRPOSHelper = new DrawGameRPOSHelper();
		drawGameRPOSHelper.checkLastPrintedTicketStatusAndUpdate(userBean,lastPrintedTicket,"TERMINAL",refMerchantId,autoCancelHoldDays,actionName, gameId);
		*/

		Connection con = null;
		PreparedStatement pstmt = null;
		StringBuilder sb = new StringBuilder();
		try {
			con = DBConnect.getConnection();
			pstmt = con
					.prepareStatement("update st_lms_ret_offline_master om inner join st_lms_user_master um on om.organization_id=um.organization_id set last_HBT_time=?,last_connected_through=?,device_type=? where serial_number like ? and user_name=?");
			if (terminalId == null || deviceType == null) {
				throw new LMSException(
						"Terminal id or device type should not be null .");
			}

			if (deviceType.equalsIgnoreCase("TPS")) {
				if (terminalId.length() > 15) {
					terminalId = terminalId.substring(terminalId.length() - 15,
							terminalId.length());
				}
			} else {
				if (terminalId.length() > 8) {
					terminalId = terminalId.substring(terminalId.length() - 8,
							terminalId.length());
				}

				pstmt.setTimestamp(1, new Timestamp(new Date().getTime()));
				pstmt.setString(2, simType);
				pstmt.setString(3, deviceType);
				pstmt.setString(4, "%"+terminalId);
				pstmt.setString(5, username);
				pstmt.executeUpdate();

				String currentTime = new java.sql.Timestamp((new Date())
						.getTime()).toString().split("\\.")[0];

				sb.append("success,").append("CurrentT:").append(currentTime);
				response.getOutputStream().write(sb.toString().getBytes());
			}
			logger.info("Heart Beat Request Data - > Terminal Id - "
					+ terminalId + "User Name - " + username);
		} catch (Exception e) {
			logger.error("Exception in terminal heart beat checking  ....");
			e.printStackTrace();

		} finally {
			try {
				con.close();
				pstmt.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse resp) {
		response = resp;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String teminalId) {
		this.terminalId = teminalId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public String getSimType() {
		return simType;
	}

	public void setSimType(String simType) {
		this.simType = simType;
	}

	public long getLSTktNo() {
		return LSTktNo;
	}

	public void setLSTktNo(long lSTktNo) {
		LSTktNo = lSTktNo;
	}
}