package com.skilrock.lms.web.drawGames.drawMgmt;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.coreEngine.drawGames.drawMgmt.OfflineBOFileProcessHelper;
import com.skilrock.lms.dge.beans.FileUploadBean;

public class OfflineBOFileProcessAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	private static final long serialVersionUID = 1L;
	private String fileRefId;
	private int gameNo;
	Log logger = LogFactory.getLog(OfflineBOFileProcessAction.class);
	private HttpServletResponse response;
	private int retUserId;
	private HttpServletRequest servletRequest;
	HttpSession session = null;
	private String status;

	public void declineFile() {
		try {
			PrintWriter out = getResponse().getWriter();
			String result = null;
			session = getRequest().getSession();
			UserInfoBean userInfoBean = (UserInfoBean) session
					.getAttribute("USER_INFO");
			logger.debug("--BO END File Upload--for Retailer Id::" + retUserId);
			OfflineBOFileProcessHelper helper = new OfflineBOFileProcessHelper();
			result = helper.declineFile(fileRefId, gameNo, userInfoBean
					.getUserId());
			logger.debug("********result" + result);
			out.print(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String featchRetFileUploadStatus() {
		session = getRequest().getSession();
		logger.debug("--BO END File Upload--for Retailer Id::" + retUserId);
		Map<String, List<FileUploadBean>> retFileMap = new HashMap<String, List<FileUploadBean>>();
		OfflineBOFileProcessHelper helper = new OfflineBOFileProcessHelper();
		retFileMap = helper.featchRetFileUploadStatus(retUserId);
		logger.debug("****retFileMap" + retFileMap);
		session.setAttribute("retFileMap", retFileMap);
		return SUCCESS;
	}

	public String getFileRefId() {
		return fileRefId;
	}

	public int getGameNo() {
		return gameNo;
	}

	public HttpServletRequest getRequest() {
		return servletRequest;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public int getRetUserId() {
		return retUserId;
	}

	public HttpSession getSession() {
		return session;
	}

	public String getStatus() {
		return status;
	}

	public void mergeFile() {
		try {
			PrintWriter out = getResponse().getWriter();
			String result = null;
			session = getRequest().getSession();
			UserInfoBean userInfoBean = (UserInfoBean) session
					.getAttribute("USER_INFO");
			logger.debug("--BO END File Upload--for Retailer Id::" + retUserId);
			String refMerchantId = (String) ServletActionContext.getServletContext().getAttribute("REF_MERCHANT_ID");
			OfflineBOFileProcessHelper helper = new OfflineBOFileProcessHelper();
			result = helper.mergeFile(fileRefId, status, retUserId, gameNo,
					userInfoBean.getUserId(),refMerchantId,"LMS_Terminal");
			logger.debug("********result" + result);
			out.print(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setFileRefId(String fileRefId) {
		this.fileRefId = fileRefId;
	}

	public void setGameNo(int gameNo) {
		this.gameNo = gameNo;
	}

	public void setRetUserId(int retUserId) {
		this.retUserId = retUserId;
	}

	public void setServletRequest(HttpServletRequest servletRequest) {
		this.servletRequest = servletRequest;

	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;

	}

	public void setSession(HttpSession session) {
		this.session = session;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
