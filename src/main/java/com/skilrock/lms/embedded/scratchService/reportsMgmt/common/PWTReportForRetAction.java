package com.skilrock.lms.embedded.scratchService.reportsMgmt.common;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.GetDate;
import com.skilrock.lms.coreEngine.userMgmt.common.CommonFunctionsHelper;
import com.skilrock.lms.embedded.drawGames.common.EmbeddedErrors;

public class PWTReportForRetAction extends ActionSupport implements
		ServletResponseAware, ServletRequestAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String edit = null;
	private String end = null;
	private String end_Date;

	private HttpServletResponse response;
	private String searchResultsAvailable;
	private int start = 0;

	private String start_date;

	private String totaltime;

	String[] type = { "Daily", "Weekly", "Monthly" };

	private String userName;

	public String getEdit() {
		return edit;
	}

	public String getEnd() {
		return end;
	}

	public String getEnd_Date() {
		return end_Date;
	}

	public String getSearchResultsAvailable() {
		return searchResultsAvailable;
	}

	public int getStart() {
		return start;
	}

	public String getStart_date() {
		return start_date;
	}

	public String getTotaltime() {
		return totaltime;
	}

	public String getUserName() {
		return userName;
	}

	public void pwtReportForRet() throws LMSException, Exception {
		System.out.println(" inside report retailer ");
		ServletContext sc = ServletActionContext.getServletContext();
		// ServletContext sc = ServletActionContext.getServletContext();
		String isDraw = (String) sc.getAttribute("IS_SCRATCH");
		if (isDraw.equalsIgnoreCase("NO")) {
			try {
				response.getOutputStream().write(
						"Scrach Game Not Avaialbe".getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}
		Map currentUserSessionMap = (Map) sc.getAttribute("LOGGED_IN_USERS");
		if (currentUserSessionMap == null) {
			response
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
			response
					.getOutputStream()
					.write(
							("ErrorMsg:" + EmbeddedErrors.SESSION_EXPIRED + "ErrorCode:01|")
									.getBytes());
			return;
		}
		System.out.println(" session is in PWT RETAILER " + session);
		UserInfoBean infoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");

		Connection con = DBConnect.getConnection();
		// query to get all draw games from draw master for game
		String query = "select game_id,game_nbr,game_name from st_draw_game_master";
		PreparedStatement pStatement = con.prepareStatement(query);
		ResultSet rs = pStatement.executeQuery();
		ResultSet rs1 = null;
		int userOrgId = infoBean.getUserOrgId();
		Map<Integer, Double> dgPWTMap = new HashMap<Integer, Double>();
		while (rs.next()) {
			query = "select sum(pwt_amt) from st_dg_ret_pwt_"
					+ rs.getInt("game_nbr") + " where retailer_org_id="
					+ userOrgId;
			pStatement = con.prepareStatement(query);
			rs1 = pStatement.executeQuery();
			while (rs1.next()) {
				dgPWTMap.put(rs.getInt("game_nbr"), rs1.getDouble(1));
			}
			query = "select sum(pwt_amt) from st_dg_ret_direct_plr_pwt where retailer_org_id="
					+ userOrgId + " and game_id=" + rs.getInt("game_id");
			pStatement = con.prepareStatement(query);
			rs1 = pStatement.executeQuery();
			while (rs1.next()) {
				dgPWTMap.put(rs.getInt("game_nbr"), dgPWTMap.get(rs
						.getInt("game_nbr"))
						+ rs1.getDouble(1));
			}
		}

		// query to get all instant games from instant master for game
		query = "select game_id,game_nbr,game_name from st_game_master";
		pStatement = con.prepareStatement(query);
		rs = pStatement.executeQuery();
		rs1 = null;
		Map<Integer, Double> igPWTMap = new HashMap<Integer, Double>();
		while (rs.next()) {
			query = "select sum(pwt_amt) from st_retailer_pwt where retailer_org_id="
					+ userOrgId + " and game_id=" + rs.getInt("game_id");
			pStatement = con.prepareStatement(query);
			rs1 = pStatement.executeQuery();
			while (rs1.next()) {
				igPWTMap.put(rs.getInt("game_nbr"), rs1.getDouble(1));
			}
			query = "select sum(pwt_amt) from st_retailer_direct_player_pwt where retailer_org_id="
					+ userOrgId + " and game_id=" + rs.getInt("game_id");
			pStatement = con.prepareStatement(query);
			rs1 = pStatement.executeQuery();
			while (rs1.next()) {
				igPWTMap.put(rs.getInt("game_nbr"), igPWTMap.get(rs
						.getInt("game_nbr"))
						+ rs1.getDouble(1));
			}
		}
		System.out.println(" draw game map " + dgPWTMap);
		System.out.println(" instant game map " + igPWTMap);

		Set<Integer> keySet = dgPWTMap.keySet();
		Iterator<Integer> iter = keySet.iterator();
		// for draw game
		StringBuilder sBuilder = new StringBuilder("<games>");
		int gameNo = 0;
		while (iter.hasNext()) {
			gameNo = iter.next();
			sBuilder.append("<game>");
			sBuilder.append("<gameType>DRAW</gameType>");
			sBuilder.append("<gameNo>" + gameNo + "</gameNo>");
			sBuilder.append("<winAmt>" + dgPWTMap.get(gameNo) + "</winAmt>");
			sBuilder.append("</game>");

		}
		keySet = igPWTMap.keySet();
		iter = keySet.iterator();
		// response.getOutputStream().write(b)
		while (iter.hasNext()) {
			gameNo = iter.next();
			sBuilder.append("<game>");
			sBuilder.append("<gameType>INSTANT</gameType>");
			sBuilder.append("<gameNo>" + gameNo + "</gameNo>");
			sBuilder.append("<winAmt>" + igPWTMap.get(gameNo) + "</winAmt>");
			sBuilder.append("</game>");

		}
		sBuilder.append("</games>");
		System.out.println(" final pwt report string " + sBuilder.toString());
		response.getOutputStream().write(sBuilder.toString().getBytes());

	}

	public void setEdit(String edit) {
		this.edit = edit;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public void setEnd_Date(String end_Date) {
		System.out.println("end date called" + end_Date);
		if (end_Date != null) {
			this.end_Date = GetDate.getSqlToUtilFormatStr(end_Date);
		} else {
			this.end_Date = new java.sql.Date(new java.util.Date().getTime())
					.toString();
		}
	}

	public void setSearchResultsAvailable(String searchResultsAvailable) {
		this.searchResultsAvailable = searchResultsAvailable;
	}

	public void setServletRequest(HttpServletRequest req) {

	}

	public void setServletResponse(HttpServletResponse res) {
		response = res;

	}

	public void setStart(int start) {
		this.start = start;
	}

	public void setStart_date(String start_date) {
		System.out.println("first date called" + start_date);
		if (start_date != null) {
			this.start_date = GetDate.getSqlToUtilFormatStr(start_date);
		} else {
			this.start_date = new java.sql.Date(new java.util.Date().getTime())
					.toString();
		}
	}

	public void setTotaltime(String totaltime) {
		this.totaltime = totaltime;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
