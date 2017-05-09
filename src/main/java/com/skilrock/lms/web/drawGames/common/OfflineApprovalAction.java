package com.skilrock.lms.web.drawGames.common;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.OfflineApprovalBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.coreEngine.drawGames.common.OfflineApprovalHelper;

public class OfflineApprovalAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	private static final long serialVersionUID = 1L;
	private HttpServletRequest request;
	private HttpServletResponse response;

	private int[] fileId;
	private int[] retUserId;
	private String[] retName;
	private int[] gameNo;
	private int[] approvalStatus;

	public int[] getFileId() {
		return fileId;
	}

	public void setFileId(int[] fileId) {
		this.fileId = fileId;
	}

	public int[] getRetUserId() {
		return retUserId;
	}

	public void setRetUserId(int[] retUserId) {
		this.retUserId = retUserId;
	}

	public String[] getRetName() {
		return retName;
	}

	public void setRetName(String[] retName) {
		this.retName = retName;
	}

	public int[] getGameNo() {
		return gameNo;
	}

	public void setGameNo(int[] gameNo) {
		this.gameNo = gameNo;
	}

	public int[] getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(int[] approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	@Override
	public String execute() throws Exception {
		HttpSession session = request.getSession();
		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		OfflineApprovalHelper helper = new OfflineApprovalHelper();
		List<OfflineApprovalBean> beanList = helper.fetchFileDetails(
				userInfoBean.getUserOrgId(), null, true);
		session.setAttribute("Offline_File_List", beanList);
		return SUCCESS;
	}

	@SuppressWarnings("unchecked")
	public String approvalFiles() {
		HttpSession session = request.getSession();
		ServletContext sc = ServletActionContext.getServletContext();
		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");

		Map<Integer, Map<Integer, String>> drawIdTableMap = (Map<Integer, Map<Integer, String>>) sc
				.getAttribute("drawIdTableMap");
		String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
		OfflineApprovalHelper helper = new OfflineApprovalHelper();
		StringBuilder result = new StringBuilder("");
		for (int i = 0; i < approvalStatus.length; i++) {
			int index = approvalStatus[i];
			String temp = helper.mergeFile(userInfoBean, drawIdTableMap,
					userInfoBean.getUserOrgId(), gameNo[index], fileId[index],
					retUserId[index], refMerchantId, "LMS_Terminal");
			result.append(temp);
		}
		System.out.println(result);
		List<OfflineApprovalBean> beanList = helper.fetchFileDetails(
				userInfoBean.getUserOrgId(), fileId, false);
		session.setAttribute("Offline_File_List", beanList);
		return SUCCESS;
	}
}
