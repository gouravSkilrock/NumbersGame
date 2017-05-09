package com.skilrock.lms.web.accMgmt.common;

import com.skilrock.lms.common.db.QueryManager;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;

public class GetPlayerState extends ActionSupport implements
		ServletResponseAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String plrCountry = "";
	private HttpServletResponse response;

	@Override
	public String execute() throws Exception {
		PrintWriter out = getResponse().getWriter();
		String plrCntry = getPlrCountry();
		System.out.println("" + plrCntry);
		if (plrCntry == null) {
			plrCntry = "";
		}
		ArrayList characters = getCharacters(plrCntry);
		String html = "";
		html = "<select class=\"option\" name=\"plrState\" id=\"plrState\"><option class=\"option\" value=\"-1\">--Please Select--</option>";

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

	private ArrayList getCharacters(String plrCntry) throws Exception {
		 
		ArrayList arrlist = new ArrayList();
		Connection con = null;
		Statement stmt = null;
		Statement stmt2 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		try {
			con = DBConnect.getConnection();
			stmt = con.createStatement();
			stmt2 = con.createStatement();
			// getST5CountryCodeQuery

			String query1 = QueryManager.getST5CountryCodeQuery();
			String query2 = QueryManager.getST5State();
			rs = stmt.executeQuery(query1 + " where name='" + plrCountry + "'");
			while (rs.next()) {
				String code = rs.getString("country_code");
				System.out.println("heeeeeeellllllllll");
				rs1 = stmt2.executeQuery(query2 + " where country_code='"
						+ code + "'");
				while (rs1.next()) {
					// System.out.println("hhhhhhhhhhhhhh");
					arrlist.add(rs1.getString("name"));
				}

			}
		} catch (SQLException e) {

			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (rs1 != null) {
					rs1.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				if (stmt2 != null) {
					stmt2.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception ee) {
				System.out.println("Error in closing the Connection");
				ee.printStackTrace();
				throw new LMSException(ee);

			}

		}

		return arrlist;

	} // End getCharacters()

	public String getPlrCountry() {
		return plrCountry;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setPlrCountry(String plrCountry) {
		this.plrCountry = plrCountry;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;

	}

}
