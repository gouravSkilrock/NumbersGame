package com.skilrock.lms.embedded.scratchService.reportsMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.coreEngine.userMgmt.common.CommonFunctionsHelper;
import com.skilrock.lms.embedded.drawGames.common.EmbeddedErrors;

public class LimitsAction extends ActionSupport implements ServletRequestAware,
		ServletResponseAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HttpServletRequest servletRequest;
	private HttpServletResponse servletResponse;
	private String userName;

	public void getLimits() throws Exception {

		ServletContext sc = ServletActionContext.getServletContext();
		String isDraw = (String) sc.getAttribute("IS_SCRATCH");
		if (isDraw.equalsIgnoreCase("NO")) {
			servletResponse.getOutputStream().write(
					"Scratch Game Not Avaialbe".getBytes());
			return;
		}
		Map currentUserSessionMap = (Map) sc.getAttribute("LOGGED_IN_USERS");
		if (currentUserSessionMap == null) {
			servletResponse
					.getOutputStream()
					.write(
							("ErrorMsg:" + EmbeddedErrors.SESSION_EXPIRED + "ErrorCode:01|")
									.getBytes());
			return;
		}
		// System.out.println(" LOGGED_IN_USERS maps is " +
		// currentUserSessionMap);

		System.out.println(" user name is " + userName);

		HttpSession session = (HttpSession) currentUserSessionMap.get(userName);
		// session = (HttpSession) sc.getAttribute(userName);
		if (!CommonFunctionsHelper.isSessionValid(session)) {
			servletResponse
					.getOutputStream()
					.write(
							("ErrorMsg:" + EmbeddedErrors.SESSION_EXPIRED + "ErrorCode:01|")
									.getBytes());
			return;
		}
		System.out.println(" session is in PWT RETAILER " + session);
		UserInfoBean infoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		int organizationId = infoBean.getUserOrgId();

		String query = "select aa.organization_id, verification_limit, approval_limit, pay_limit, scrap_limit, bb.pwt_scrap from st_oranization_limits aa, st_organization_master bb where  aa.organization_id = bb.organization_id and  aa.organization_id = ?";
		Connection connection = null;
		PreparedStatement pstmt = connection.prepareStatement(query);
		pstmt.setInt(1, organizationId);
		ResultSet result = pstmt.executeQuery();
		System.out.println("query that fetch limit details = " + pstmt);
		String limits = null;
		if (result.next()) {
			limits = "verification_limit\n"
					+ result.getDouble("verification_limit")
					+ "\napproval_limit\n" + result.getDouble("approval_limit")
					+ "\npay_limit\n" + result.getDouble("pay_limit")
					+ "\nscrap_limit\n" + result.getDouble("scrap_limit")
					+ "\npwt_scrap\n" + result.getString("pwt_scrap");

		}
		servletResponse.getOutputStream().write(limits.getBytes());
		System.out.println(" limits are " + limits);

	}

	public HttpServletRequest getServletRequest() {
		return servletRequest;
	}

	public HttpServletResponse getServletResponse() {
		return servletResponse;
	}

	public String getUserName() {
		return userName;
	}

	public void setServletRequest(HttpServletRequest servletRequest) {
		this.servletRequest = servletRequest;
	}

	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
