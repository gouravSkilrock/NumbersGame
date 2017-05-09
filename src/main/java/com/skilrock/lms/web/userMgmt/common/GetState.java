package com.skilrock.lms.web.userMgmt.common;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;

public class GetState extends ActionSupport implements ServletResponseAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String country = "";

	Log logger = LogFactory.getLog(GetState.class);
	private HttpServletResponse response;

	@Override
	public String execute() throws Exception {
		PrintWriter out = getResponse().getWriter();
		String tvShow = getCountry();
		logger.debug("Country:  " + tvShow);
		logger.debug("" + tvShow);
		if (tvShow == null) {
			tvShow = "";
		}
		ArrayList characters = getCharacters(tvShow);

		// And yes, I know creating HTML in an Action is generally very bad
		// form,
		// but I wanted to keep this exampel simple.
		String html = "";

		/*
		 * if (ledgerType.equals("Accountwise")) { html = "Select Account:
		 * <select name=\"accountType\">"; } else if
		 * (ledgerType.equals("Agentwise")) { html = "Select Agent: <select
		 * name=\"agentName\">"; }
		 */
		html = " <select class=\"option\" name=\"state\"><option value=-1>--PleaseSelect--</option>";

		int i = 0;
		for (Iterator it = characters.iterator(); it.hasNext();) {
			String name = (String) it.next();
			i++;
			html += "<option class=\"option\" value=\"" + name + "\">" + name
					+ "</option>";
		}
		html += "</select>";
		response.setContentType("text/html");
		out.print(html);
		return null;
	}

	private ArrayList getCharacters(String tvShow) throws LMSException {
		 
		ArrayList arrlist = new ArrayList();
		Connection con;
		Statement stmt = null;
		Statement stmt2 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;

		con = DBConnect.getConnection();
		try {
			stmt = con.createStatement();
			stmt2 = con.createStatement();
			/*
			 * if (ledgerType.equals("Accountwise")) { rs = stmt
			 * .executeQuery("select account_type from st_lms_bo_current_balance
			 * "); while (rs.next()) { al.add(rs.getString("account_type")); } }
			 * else if (ledgerType.equals("Agentwise")) { rs = stmt
			 * .executeQuery("select user_name from st_lms_user_master where
			 * organization_type='AGENT' "); while (rs.next()) {
			 * al.add(rs.getString("user_name")); } }
			 */

			rs = stmt
					.executeQuery("select country_code from st_lms_country_master where name='"
							+ country + "'");
			while (rs.next()) {
				String code = rs.getString("country_code");
				logger.debug("heeeeeeellllllllll");
				rs1 = stmt2
						.executeQuery("select name from st_lms_state_master where country_code='"
								+ code + "' and status='active' ");
				while (rs1.next()) {

					arrlist.add(rs1.getString("name"));
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

		return arrlist;

	} // End getCharacters()

	public String getCountry() {
		return country;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;

	}

}
