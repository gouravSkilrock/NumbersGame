package com.skilrock.lms.embedded.loginMgmt;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.loginMgmt.common.ManualVersionDownloadForRetailerHelper;
import com.skilrock.lms.embedded.drawGames.common.EmbeddedErrors;

public class ManualVersionDownloadForRetailerAction extends ActionSupport
		implements ServletRequestAware, ServletResponseAware {
	
	Log logger = LogFactory.getLog(ManualVersionDownloadForRetailerAction.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long LSTktNo;
	private double version;
	private String profile;
	private String username;
	private String deviceType;
	private String terminalId;
	private HttpServletRequest request;
	private HttpServletResponse response;

	public void getAvailableTerminalBuildVersions() throws IOException {
		String versionInfo = null;
		try {
			logger.info("Manual Downlod request: UserName: "+username+ " deviceType: "+deviceType+ " Current Version: "+version+ " Last Sold Ticket Number: "+LSTktNo+" Profile Sent is: "+ profile +" TerminalId Sent Is : " +terminalId);

			ManualVersionDownloadForRetailerHelper helper = new ManualVersionDownloadForRetailerHelper();
			versionInfo = helper.fetchAvailableTerminalBuildVersions(deviceType,profile,version);
		} catch (LMSException e) {
			logger.error("EXCEPTION :- " + e);
			if(e.getErrorCode()==LMSErrors.SQL_EXCEPTION_ERROR_CODE)
				versionInfo = "ErrorMsg:"+EmbeddedErrors.TRY_AGAIN_ERROR_MSG+"|ErrorCode:"+EmbeddedErrors.TRY_AGAIN_ERROR_CODE+"|";
			else
				versionInfo = "ErrorMsg:"+EmbeddedErrors.NO_ACTIVE_APPLICATION_AVAILABLE+"ErrorCode:"+EmbeddedErrors.NO_ACTIVE_APPLICATION_AVAILABLE_ERROR_CODE+"|";
		} catch (Exception e) {
			logger.error("EXCEPTION :- " + e);
			versionInfo = "ErrorMsg:"+EmbeddedErrors.NO_ACTIVE_APPLICATION_AVAILABLE+"ErrorCode:"+EmbeddedErrors.NO_ACTIVE_APPLICATION_AVAILABLE_ERROR_CODE+"|";
		}
		response.getOutputStream().write(versionInfo.getBytes());
		return;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public double getVersion() {
		return version;
	}

	public void setVersion(double version) {
		this.version = version;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public long getLSTktNo() {
		return LSTktNo;
	}

	public void setLSTktNo(long lSTktNo) {
		LSTktNo = lSTktNo;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}
	
}
