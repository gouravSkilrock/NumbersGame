package com.skilrock.lms.web.userMgmt.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.userMgmt.common.OrganizationHelper;

public class CreateUserOrg extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Log logger = LogFactory.getLog(CreateUserOrg.class);

	private String privType;
	private HttpServletRequest request;

	private HttpServletResponse response;

	@Override
	public String execute() throws LMSException {

		List<String> orgList = new ArrayList<String>();
		Connection con = null;
		ResultSet rs = null;

		try {
			con = DBConnect.getConnection();
			Statement stmt = con.createStatement();
			HttpSession session = request.getSession();
			UserInfoBean bean = (UserInfoBean) session
					.getAttribute("USER_INFO");
			String userType = bean.getUserType();
			String roleName = bean.getRoleName();

			if (userType.equals("BO")) {
				if (roleName.equals("BO_MAS")) {
					logger.debug("role name is BO MAS");
					logger.debug("role name is BO MAS");
					rs = stmt
							.executeQuery("select name from st_lms_organization_master where organization_type='BO'");
				}
				// else
				// rs = stmt.executeQuery("select name from
				// st_lms_organization_master where organization_type='AGENT'");
			} else if (userType.equals("AGENT")) {
				rs = stmt
						.executeQuery("select name from st_lms_organization_master where organization_type='RETAILER'");
			}

			while (rs.next()) {
				orgList.add(rs.getString("name"));
			}

			session.setAttribute("list", orgList);

			// get the email mailing privilege list from database
			// OrganizationHelper orgReg = new OrganizationHelper();
			// Map<Integer, String> reportTypeTitleMap =
			// orgReg.getMailingReportTitle("BO");
			// logger.debug("email privilege list in orgnization creation
			// == "+reportTypeTitleMap);
			// session.setAttribute("reportTypeTitleMap", reportTypeTitleMap);

			return SUCCESS;

		} catch (SQLException se) {
			se.printStackTrace();
			throw new LMSException("SQL Exception", se);

		} finally {
			try {
				if (con != null) {
					con.close();
				}

			} catch (SQLException e) {
				e.printStackTrace();
				throw new LMSException("SQL Exception", e);
			}
		}
	}

	public void getEmailPrivListId() throws IOException {
		// get the email mailing privilege list from database
		HttpSession session = request.getSession();
		OrganizationHelper orgReg = new OrganizationHelper();
		Map<Integer, String> reportTypeTitleMap = orgReg
				.getMailingReportTitle(privType);
		logger.debug("Inside getEmailPrivListId");
		logger.debug("email privilege list in orgnization creation == "
				+ reportTypeTitleMap);
		logger.debug("email privilege list in orgnization creation == "
				+ reportTypeTitleMap);
		session.setAttribute("reportTypeTitleMap", reportTypeTitleMap);
		PrintWriter out = response.getWriter();
		out.print(reportTypeTitleMap.toString().replace("{", "").replace("}",
				""));

	}

	public String getPrivType() {
		return privType;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setPrivType(String privType) {
		this.privType = privType;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse res) {
		this.response = res;

	}

}