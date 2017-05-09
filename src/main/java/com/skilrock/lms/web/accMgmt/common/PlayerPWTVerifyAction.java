/*
 * � copyright 2007, SkilRock Technologies, A division of Sugal & Damani Lottery Agency Pvt. Ltd.
 * All Rights Reserved
 * The contents of this file are the property of Sugal & Damani Lottery Agency Pvt. Ltd.
 * and are subject to a License agreement with Sugal & Damani Lottery Agency Pvt. Ltd.; you may
 * not use this file except in compliance with that License.  You may obtain a
 * copy of that license from:
 * Legal Department
 * Sugal & Damani Lottery Agency Pvt. Ltd.
 * 6/35,WEA, Karol Bagh,
 * New Delhi
 * India - 110005
 * This software is distributed under the License and is provided on an �AS IS�
 * basis, without warranty of any kind, either express or implied, unless
 * otherwise provided in the License.  See the License for governing rights and
 * limitations under the License.
 */

package com.skilrock.lms.web.accMgmt.common;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.PlayerBean;
import com.skilrock.lms.beans.PwtBean;
import com.skilrock.lms.beans.TicketBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.db.TableConstants;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.accMgmt.common.PlayerPWTVerifyHelper;
import com.skilrock.lms.coreEngine.accMgmt.common.PwtTicketDirectPlayerHelper;
import com.skilrock.lms.coreEngine.reportsMgmt.common.GraphReportHelper;

/**
 * This class is used to verify the player,resister the player if not exist,and
 * complete the Direct player PWT receive.This class finally commit the whole
 * process.
 * 
 * @author Skilrock Technologies.,
 * 
 */
public class PlayerPWTVerifyAction extends ActionSupport implements
		ServletRequestAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Connection conn = null;
	private String country = null;
	private String emailId = null;
	private String firstName = null;
	private String idNumber;
	private String idType = null;
	private String lastName = null;
	private String phone = null;
	private String plrAddr1 = null;
	private String plrAddr2 = null;
	private String plrCity = null;
	private String plrCountry = null;
	private String plrPin;
	private String plrState = null;
	private HttpServletRequest request;

	private String varForPlayerPWTVerify;

	/**
	 * This method is used to commit data in the database ,of whole process for
	 * direct player PWT receive.It completes whole process and send response to
	 * display PWT amount for the selected game-Ticket.
	 * 
	 * @return SUCCESS;
	 * @throws LMSException
	 */
	public String donePWTProcess() throws LMSException {
		int gameId = 0;
		int gameNbr = 0;
		int playerId = 0;
		HttpSession session = getRequest().getSession();
		session.setAttribute("Receipt_Id", null);
		session.setAttribute("PlayerId", null);

		UserInfoBean userInfo;
		userInfo = (UserInfoBean) session.getAttribute("USER_INFO");
		session.setAttribute("Receipt_Id", null);

		String boAccountName = userInfo.getOrgName();

		saveTicketsData(session);
		if (session.getAttribute("ConnObject") == null) {
			addActionError("Connection object is not available");
			System.out.println("Connection is not available for transaction");
			return ERROR;

		}

		conn = (Connection) session.getAttribute("ConnObject");
		System.out.println("Connection object from PlayerPWTAction by session"
				+ conn);
		if (session.getAttribute("PLAYER_DETAILS") == null) {
			playerId = registerPlayer(conn);

			System.out.println("Player Id from register" + playerId);

		} else {
			List<PlayerBean> playerBean = (List) session
					.getAttribute("PLAYER_DETAILS");

			playerId = playerBean.get(0).getPlrId();
			plrAddr1 = playerBean.get(0).getPlrAddr1();
			plrAddr2 = playerBean.get(0).getPlrAddr2();
			plrCity = playerBean.get(0).getPlrCity();
			idType = playerBean.get(0).getIdType();
			idNumber = playerBean.get(0).getIdNumber();

			System.out.println("Player Id from database" + playerId);
		}
		if (session.getAttribute("GAME_ID") != null) {
			gameId = ((Integer) session.getAttribute("GAME_ID")).intValue();
			gameNbr = ((Integer) session.getAttribute("GAME_NBR")).intValue();
		}
		/*
		 * if(session.getAttribute("GAME_ID")!=null) {
		 * gameId=((Integer)session.getAttribute("GAME_ID")).intValue(); }
		 */
		List<TicketBean> verifiedTicketList = (List) session
				.getAttribute("VERIFIED_TICKET_LIST");
		String ticket_nbr = verifiedTicketList.get(0).getTicketNumber();
		String pwtApprovalLimitStr = (String) ServletActionContext
				.getServletContext().getAttribute("PWT_APPROVAL_LIMIT");
		double pwtApprovalLimit = Double.parseDouble(pwtApprovalLimitStr);

		List<PwtBean> pwtList = (List) session.getAttribute("PWTLIST");
		System.out.println("pwt list" + pwtList);

		session.setAttribute("PWT_AMT", pwtList.get(0).getPwtAmount());
		PlayerPWTVerifyHelper plrHelper = new PlayerPWTVerifyHelper();

		int pwtReceiptId = plrHelper.partiallyPWTProcess(gameId, playerId,
				pwtList, conn, ticket_nbr, pwtApprovalLimit, gameNbr);
		session.setAttribute("ConnObject", null);

		if (pwtReceiptId > 0) {
			session.setAttribute("Receipt_Id", pwtReceiptId);
			session.setAttribute("PlayerId", playerId);
			GraphReportHelper graphReportHelper = new GraphReportHelper();
			// graphReportHelper.createTextReportTempPlayerReceipt(pwtReceiptId,firstName,lastName,plrAddr1,plrAddr2,plrCity,gameId,boAccountName,(String)session.getAttribute("ROOT_PATH"),idType,idNumber);
			graphReportHelper.createTextReportTempPlayerReceipt(pwtReceiptId
					+ "", userInfo.getUserType(), (String) session
					.getAttribute("ROOT_PATH"), "SCRATCH_GAME");
			System.out
					.println("^^^^^^^^^^^^^^^^^^^^^^^^^inside success loop::  "
							+ pwtReceiptId);
			return SUCCESS;
		} else {
			System.out
					.println("############################3inside error loop:::  "
							+ pwtReceiptId);
			return ERROR;
		}
	}

	public Connection getConn() {
		return conn;
	}

	public String getCountry() {
		return country;
	}

	private List<String> getCountryList() throws LMSException {
		List<String> list = new ArrayList<String>();
		String query = null;
		ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
		 
		try {
			conn = DBConnect.getConnection();
			st = conn.createStatement();

			query = QueryManager.getST5Country();
			System.out.println("query>>>>>" + query);
			rs = st.executeQuery(query);
			while (rs.next()) {
				list.add(rs.getString("name"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("error in the sql");
			throw new LMSException(e);
		} finally {

			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException se) {
				throw new LMSException(se);
			}
		}
		return list;
	}

	public String getEmailId() {
		return emailId;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public String getIdType() {
		return idType;
	}

	public String getLastName() {
		return lastName;
	}

	public String getPhone() {
		return phone;
	}

	public String getPlrAddr1() {
		return plrAddr1;
	}

	public String getPlrAddr2() {
		return plrAddr2;
	}

	public String getPlrCity() {
		return plrCity;
	}

	public String getPlrCountry() {
		return plrCountry;
	}

	public String getPlrPin() {
		return plrPin;
	}

	public String getPlrState() {
		return plrState;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public String getVarForPlayerPWTVerify() {
		return varForPlayerPWTVerify;
	}

	/**
	 * This method is used continue player verification or registration process.
	 * 
	 * @return SUCCESS.
	 */
	public String process() {
		HttpSession session = getRequest().getSession();
		// /To show the steps on the jsp page
		int count = (Integer) session.getAttribute("COUNTER");
		count = count + 1;
		if (count > 3) {
			count = 3;
		}

		session.setAttribute("COUNTER", new Integer(count));
		setVarForPlayerPWTVerify("No");
		return SUCCESS;

	}

	private int registerPlayer(Connection conn) throws LMSException {
		HttpSession session = getRequest().getSession();
		// session.setAttribute("PLAYER_DETAILS", null);
		System.out.println("hello i AM in verify player   action");
		System.out.println("First Name:" + firstName);
		System.out.println("Last Status:" + lastName);
		System.out.println("ID Number:" + idNumber);
		System.out.println("ID Type:" + idType);
		// conn=(Connection)session.getAttribute("ConnObject");
		Map<String, String> searchMap = new HashMap<String, String>();
		searchMap.put(TableConstants.PLAYER_FIRSTNAME, firstName);
		searchMap.put(TableConstants.PLAYER_LASTNAME, lastName);
		searchMap.put(TableConstants.PLAYER_IDNUMBER, idNumber);
		searchMap.put(TableConstants.PLAYER_IDTYPE, idType);
		searchMap.put(TableConstants.PLAYER_EMAIL, emailId);
		searchMap.put(TableConstants.PLAYER_PHONE, phone);
		searchMap.put(TableConstants.PLAYER_ADDR1, plrAddr1);
		searchMap.put(TableConstants.PLAYER_ADDR2, plrAddr2);
		searchMap.put(TableConstants.PLAYER_CITY, plrCity);
		searchMap.put(TableConstants.PLAYER_STATE, plrState);
		searchMap.put(TableConstants.PLAYER_COUNTRY, plrCountry);
		searchMap.put(TableConstants.PLAYER_PIN, plrPin);
		PlayerPWTVerifyHelper searchPlayerHelper = new PlayerPWTVerifyHelper();
		int playerId = searchPlayerHelper.registerPlayer(searchMap, conn);

		return playerId;

	}

	private String saveTicketsData(HttpSession session1) throws LMSException {
		System.out.println("inside playerpwtverifyaction");
		HttpSession session = session1;

		UserInfoBean userBean = (UserInfoBean) session1
				.getAttribute("USER_INFO");

		List<TicketBean> verifiedTicketList = (List) session
				.getAttribute("VERIFIED_TICKET_LIST");
		// List<ActiveGameBean> activeGameBeanList=(List)
		// session.getAttribute("ACTIVE_GAME_LIST");
		String gameNbrName = (String) session
				.getAttribute("SELECTED_GAMENBR_NAME");
		// conn=(Connection)session.getAttribute("connObj");
		System.out.println("Ticket List Size::" + verifiedTicketList.size());
		// System.out.println(activeGameBeanList);
		System.out.println("........................." + gameNbrName);

		PwtTicketDirectPlayerHelper pwtTicketHelper = new PwtTicketDirectPlayerHelper();
		int game_id = pwtTicketHelper.getGameIdFromDataBase(gameNbrName);

		session.setAttribute("GAME_ID", game_id);
		System.out.println("Get the Game ID is : " + game_id);

		pwtTicketHelper.saveTicketsData(game_id, verifiedTicketList, userBean
				.getUserId(), userBean.getUserOrgId());
		// ////to get the connection object for transaction
		conn = pwtTicketHelper.getConnectrion();
		System.out.println("Connection Object in Session :" + conn);

		session.setAttribute("ConnObject", conn);
		System.out.println("Afterrrrrrrrr callingggg save dataa");

		// session.setAttribute("SAVED_TICKET_LIST", savedTicketList);

		// System.out.println("Saved List is "+savedTicketList);

		return SUCCESS;

	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setPlrAddr1(String plrAddr1) {
		this.plrAddr1 = plrAddr1;
	}

	public void setPlrAddr2(String plrAddr2) {
		this.plrAddr2 = plrAddr2;
	}

	public void setPlrCity(String plrCity) {
		this.plrCity = plrCity;
	}

	public void setPlrCountry(String plrCountry) {
		this.plrCountry = plrCountry;
	}

	public void setPlrPin(String plrPin) {
		this.plrPin = plrPin;
	}

	public void setPlrState(String plrState) {
		this.plrState = plrState;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setVarForPlayerPWTVerify(String varForPlayerPWTVerify) {
		this.varForPlayerPWTVerify = varForPlayerPWTVerify;
	}

	/**
	 * This method is used for player verification and registration if not
	 * exists.
	 * 
	 * @return SUCCESS.
	 * @throws LMSException
	 */

	public String verify() throws LMSException {
		List<String> countryList = null;
		HttpSession session = getRequest().getSession();
		session.setAttribute("PLAYER_DETAILS", null);

		System.out.println("hello i AM in verify player   action");
		System.out.println("First Name:" + firstName);
		System.out.println("Last Status:" + lastName);
		System.out.println("ID Number:" + idNumber);
		System.out.println("ID Type:" + idType);

		Map<String, String> searchMap = new HashMap<String, String>();
		searchMap.put(TableConstants.PLAYER_FIRSTNAME, firstName);
		searchMap.put(TableConstants.PLAYER_LASTNAME, lastName);
		searchMap.put(TableConstants.PLAYER_IDNUMBER, idNumber);
		searchMap.put(TableConstants.PLAYER_IDTYPE, idType);

		PlayerPWTVerifyHelper searchPlayerHelper = new PlayerPWTVerifyHelper();
		List<PlayerBean> searchResults = searchPlayerHelper
				.searchPlayer(searchMap);

		if (searchResults != null && searchResults.size() > 0) {
			System.out.println("Yes:---Search result Processed");
			session.setAttribute("PLAYER_DETAILS", searchResults);
			System.out.println("Search Result's Email Id"
					+ searchResults.get(0).getEmailId());
			setVarForPlayerPWTVerify("Yes");
			return SUCCESS;
		} else {
			setVarForPlayerPWTVerify("No");
			countryList = getCountryList();
			session.setAttribute("countryList", countryList);
			return SUCCESS;
		}
	}

}
