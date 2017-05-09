package com.skilrock.lms.web.scratchService.inventoryMgmt.common;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;

public class UpdateHeader extends ActionSupport implements ServletRequestAware,
		ServletResponseAware {
	static Log logger = LogFactory.getLog(UpdateHeader.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HttpServletRequest request;
	private HttpServletResponse response;

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public String updateHeader() {
		logger.debug("hellllllllllllllll");
		HttpSession session = getRequest().getSession();
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		int userOrgId = userBean.getUserOrgId();
		double availableCredit = userBean.getAvailableCreditLimit();
		 
		Connection con = DBConnect.getConnection();
		try {
			Statement actDetStmt = con.createStatement();

			String getACtDetails = "select available_credit from st_lms_organization_master where organization_id="
					+ userOrgId;
			ResultSet actRs = actDetStmt.executeQuery(getACtDetails);
			while (actRs.next()) {
				availableCredit = actRs.getDouble("available_credit");
			}

			userBean.setAvailableCreditLimit(availableCredit);
			session.setAttribute("USER_INFO", userBean);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();

			} catch (SQLException se) {
				se.printStackTrace();
			}

		}

		return SUCCESS;

	}

}
