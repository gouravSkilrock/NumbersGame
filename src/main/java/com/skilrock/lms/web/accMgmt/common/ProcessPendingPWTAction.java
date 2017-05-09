/*
 * © copyright 2007, SkilRock Technologies, A division of Sugal & Damani Lottery Agency Pvt. Ltd.
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
 * This software is distributed under the License and is provided on an “AS IS”
 * basis, without warranty of any kind, either express or implied, unless
 * otherwise provided in the License.  See the License for governing rights and
 * limitations under the License.
 */

package com.skilrock.lms.web.accMgmt.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.GameContants;
import com.skilrock.lms.beans.PlayerPWTBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.TableConstants;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.accMgmt.common.PlayerPWTVerifyHelper;
import com.skilrock.lms.coreEngine.accMgmt.common.ProcessPendingPWTHelper;
import com.skilrock.lms.coreEngine.reportsMgmt.common.GraphReportHelper;

/**
 * This class Processes the Pending Direct Player PWT. It pays net PWT to player
 * after all the deduction.
 * 
 * @author Skilrock Technologies
 * 
 */
public class ProcessPendingPWTAction extends ActionSupport implements
		ServletRequestAware {

	public static final String APPLICATION_ERROR = "applicationError";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String chequeDate;
	private String chequeNbr;
	private String denyPwtStatus;
	private String detail = "No";
	private String draweeBank;
	private String end = null;
	private int gameId;
	private String gameName;
	private String gameNbr;
	private String issuingParty;
	private double netAmt;
	private String playerFirstName;
	private int playerId;
	private double pwtAmt;
	private HttpServletRequest request;
	private String searchResultsAvailable;
	private int start = 0;

	private String status;
	private double tax;
	private String ticketNbr;
	private String trancDate;

	private int transactionId;

	private String virnCode;

	/**
	 * This method is used to Cancel the process data
	 * 
	 * @return SUCCESS;
	 * @throws LMSException
	 */
	public String CancelPWTReceive() throws LMSException { // HttpSession
		// session =
		// getRequest().getSession();
		return SUCCESS;

	}

	/**
	 * This method is used to commit data in the database ,of whole process for
	 * direct player PWT receive.It completes whole process and send response
	 * having PWT amount for the selected game-Ticket.
	 * 
	 * @return SUCCESS;
	 * @throws LMSException
	 */
	public String CommitDirectPlayerPWTReceive() throws LMSException {
		HttpSession session = getRequest().getSession();
		UserInfoBean userInfo;
		userInfo = (UserInfoBean) session.getAttribute("USER_INFO");
		String boOrgName = userInfo.getOrgName();
		int orgId = userInfo.getUserOrgId();
		List<PlayerPWTBean> searchResults = (List) session
				.getAttribute("SEARCH_RESULTS1");
		List<PlayerPWTBean> playerPWTBeanList;
		PlayerPWTBean playerPWTBean;
		System.out.println("transactionId:" + transactionId);
		// try {
		PlayerPWTVerifyHelper pendingPWTHelper = new PlayerPWTVerifyHelper();
		if (searchResults != null && searchResults.size() > 0) {

			for (int i = 0; i < searchResults.size(); i++) {
				System.out.println(searchResults.get(i).getPlayerFirstName());
				if (transactionId == searchResults.get(i).getTransactionId()) {
					playerPWTBeanList = new ArrayList<PlayerPWTBean>();
					playerPWTBean = searchResults.get(i);
					gameId = playerPWTBean.getGameId();
					pwtAmt = playerPWTBean.getPwtAmt();
					virnCode = playerPWTBean.getVirnCode();
					playerId = playerPWTBean.getPlayerId();
					gameNbr = new Integer(playerPWTBean.getGameNbr())
							.toString();
					gameName = playerPWTBean.getGameName();
					transactionId = playerPWTBean.getTransactionId();
					break;

				}
			}
			session.setAttribute("GAME_NBR", gameNbr);
			session.setAttribute("GAME_NAME", gameName);
			session.setAttribute("PWT_AMT", pwtAmt);
			// System.out.println(tax+","+pwtAmt);
			// System.out.println("Game Name"+gameName);
			// System.out.println("Transaction details GameId,Player id,PWT
			// amount,Tax,VIRN code"+gameId+playerId+pwtAmt+tax+virnCode);

			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			Calendar cal = Calendar.getInstance();
			dateFormat.setCalendar(cal);
			java.sql.Date chqDate = null;
			try {
				chqDate = new java.sql.Date(dateFormat.parse(getChequeDate())
						.getTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				System.out.println("could not parse date ");
				// e.printStackTrace();
			}
			System.out.println("--------%%%%%%%%% " + "cheque nbr "
					+ getChequeNbr() + "getDraweeBank() " + getDraweeBank()
					+ "chqDate " + chqDate);

			if ("".equals(chequeNbr.trim())) {
				chequeNbr = null;
			}

			if ("".equals(draweeBank.trim())) {
				draweeBank = null;
			}

			System.out.println("555555555  " + chequeNbr);
			int id = pendingPWTHelper.CommitPWTProcess(gameId, playerId,
					pwtAmt, tax, virnCode, transactionId, chequeNbr,
					draweeBank, issuingParty, chqDate, orgId, userInfo
							.getUserId(), Integer.parseInt(gameNbr));
			// int id=pendingPWTHelper.CommitPWTProcess(gameId, playerId,
			// pwtAmt, tax, virnCode);
			if (id > -1) {

				session.setAttribute("Receipt_Id", id);
				GraphReportHelper graphReportHelper = new GraphReportHelper();
				// graphReportHelper.createTextReportPlayer(id,(String)session.getAttribute("ROOT_PATH"),boOrgName,pwtAmt,tax,playerId,orgId);
				return SUCCESS;

			}

		} else {
			setSearchResultsAvailable("No");
			session.setAttribute("SearchResultsAvailable", "No");
			return ERROR;
		}

		return SUCCESS;

	}

	public String denyDirectPlayerPWTReceive() throws LMSException {
		System.out.println(":: inside   deny tttttttttttttttttttttttttt"
				+ ticketNbr + "game nbr " + gameNbr);
		PlayerPWTVerifyHelper pendingPWTHelper = new PlayerPWTVerifyHelper();
		System.out.println("=======================  " + transactionId + " ,"
				+ virnCode + " ," + gameId + " ," + ticketNbr + " ,"
				+ denyPwtStatus);
		if (pendingPWTHelper.denyPWTProcess(transactionId, virnCode, gameId,
				ticketNbr, denyPwtStatus, Integer.parseInt(gameNbr))) {
			return SUCCESS;
		} else {
			addActionError("Some error is occured Please Try Again");
			return ERROR;
		}
	}

	@Override
	public String execute() {

		HttpSession session = request.getSession();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -6);
		System.out.println("PWT_START_DATE"
				+ new java.sql.Date(cal.getTime().getTime()).toString());
		session.setAttribute("PWT_START_DATE", new java.sql.Date(cal.getTime()
				.getTime()).toString());

		return SUCCESS;
	}

	public String getChequeDate() {
		return chequeDate;
	}

	public String getChequeNbr() {
		return chequeNbr;
	}

	public String getDenyPwtStatus() {
		return denyPwtStatus;
	}

	public String getDetail() {
		return detail;
	}

	public String getDraweeBank() {
		return draweeBank;
	}

	public String getEnd() {
		return end;
	}

	public int getGameId() {
		return gameId;
	}

	public String getGameName() {
		return gameName;
	}

	public String getGameNbr() {
		return gameNbr;
	}

	public String getIssuingParty() {
		return issuingParty;
	}

	public double getNetAmt() {
		return netAmt;
	}

	public String getPlayerFirstName() {
		return playerFirstName;
	}

	public int getPlayerId() {
		return playerId;
	}

	public double getPwtAmt() {
		return pwtAmt;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public String getSearchResultsAvailable() {
		return searchResultsAvailable;
	}

	public int getStart() {
		return start;
	}

	public String getStatus() {
		return status;
	}

	private String getStatusCondition() {
		String actualStatus = null;
		if (status != null && !status.trim().equals("")) {
			if (status.equals("Pending PWT")) {
				actualStatus = new String("PND_PWT");
			} else if (status.equals("Claimed PWT")) {
				actualStatus = new String("CLAIM_PLR");
			} else if (status.equals("Cancelled PWT")) {
				actualStatus = new String("CANCEL");
			}
		}

		return actualStatus;
	}

	public double getTax() {
		return tax;
	}

	public String getTicketNbr() {
		return ticketNbr;
	}

	public String getTrancDate() {
		return trancDate;
	}

	public int getTransactionId() {
		return transactionId;
	}

	public String getVirnCode() {
		return virnCode;
	}

	/**
	 * Selected PWT details.
	 * 
	 * @return String
	 * @throws Exception
	 */
	public String PlayerPWTDetails() throws Exception {

		HttpSession session = getRequest().getSession();
		// /Searched Pending PWTs
		List<PlayerPWTBean> searchResults = (List) session
				.getAttribute("SEARCH_RESULTS1");
		List<PlayerPWTBean> playerPWTBeanList;
		PlayerPWTBean playerPWTBean;
		// / TransactionId parameter coming from the user end and for this
		// transaction id Pending player PWT details will be fetched from the
		// list.
		// try {

		if (searchResults != null && searchResults.size() > 0) {

			for (int i = 0; i < searchResults.size(); i++) {
				System.out.println(searchResults.get(i).getPlayerFirstName());
				// if that selected transaction id is same , as transaction id
				// selected from list
				if (transactionId == searchResults.get(i).getTransactionId()) {
					playerPWTBeanList = new ArrayList<PlayerPWTBean>();
					playerPWTBean = searchResults.get(i);
					playerPWTBeanList.add(playerPWTBean);
					System.out.println("Last Name of processed Player"
							+ searchResults.get(i).getPlayerLastName());
					session.setAttribute("SEARCH_RESULTS_PWT",
							playerPWTBeanList);
					break;

				}
			}
			if (detail.equals("Yes")) {
				return "details";

			} else {
				return SUCCESS;
			}

		} else {
			setSearchResultsAvailable("No");
			session.setAttribute("SearchResultsAvailable", "No");
			return ERROR;
		}
		/*
		 * } catch (LMSException le) { return APPLICATION_ERROR; }
		 */

	}

	/**
	 * This method handles the first,next,previous and last click on the order
	 * request page
	 * 
	 * @return String
	 */
	public String searchAjax() {
		int endValue = 0;
		int startValue = 0;
		// PrintWriter out = getResponse().getWriter();
		HttpSession session = getRequest().getSession();
		List ajaxList = (List) session.getAttribute("SEARCH_RESULTS1");
		List ajaxSearchList = new ArrayList();
		// System.out.println("end "+getEnd());
		if (ajaxList != null) {
			if (getEnd() != null) {
				end = getEnd();
			} else {
				end = "first";
			}
			System.out.println("List Size " + ajaxList.size());
			startValue = (Integer) session
					.getAttribute("startValueOrderSearch");
			if (end.equals("first")) {
				System.out.println("i m in first");
				startValue = 0;
				endValue = startValue + 5;
				if (endValue > ajaxList.size()) {
					endValue = ajaxList.size();
				}
			} else if (end.equals("Previous")) {
				System.out.println("i m in Previous");
				startValue = startValue - 5;
				if (startValue < 5) {
					startValue = 0;
				}
				endValue = startValue + 5;
				if (endValue > ajaxList.size()) {
					endValue = ajaxList.size();
				}
			} else if (end.equals("Next")) {
				System.out.println("i m in Next");
				startValue = startValue + 5;
				endValue = startValue + 5;
				if (endValue > ajaxList.size()) {
					endValue = ajaxList.size();
				}
				if (startValue > ajaxList.size()) {
					startValue = ajaxList.size() - ajaxList.size() % 5;
				}
			} else if (end.equals("last")) {
				endValue = ajaxList.size();
				startValue = endValue - endValue % 5;

			}
			if (startValue == endValue) {
				startValue = endValue - 5;
			}
			System.out.println("End value" + endValue);
			System.out.println("Start Value" + startValue);
			for (int i = startValue; i < endValue; i++) {
				ajaxSearchList.add(ajaxList.get(i));
			}
			session.setAttribute("SEARCH_RESULTS", ajaxSearchList);
			session.setAttribute("startValueOrderSearch", startValue);
			setSearchResultsAvailable("Yes");

		}
		return SUCCESS;
	}

	/**
	 * This method provides the search PEnding or Claimed direct player pwt .
	 * 
	 * @return String
	 * @throws Exception
	 */
	public String SearchPendingPWT() throws Exception {

		HttpSession session = getRequest().getSession();
		session.setAttribute("SEARCH_RESULTS1", null);
		session.setAttribute("SEARCH_RESULTS", null);
		session.setAttribute("SearchResultsAvailable", null);

		System.out.println("Game Name:" + gameName);
		System.out.println("Game Nbr:" + gameNbr);

		Map<String, String> searchMap = new HashMap<String, String>();
		searchMap.put(GameContants.GAME_NAME, gameName);
		searchMap.put(GameContants.GAME_NBR, gameNbr);
		searchMap.put(TableConstants.TRANC_DATE, trancDate);
		searchMap.put(GameContants.PLAYER_FIRST_NAME, playerFirstName);

		String statusValue = getStatusCondition();

		searchMap.put(TableConstants.PWT_STATUS, statusValue);

		// try {
		ProcessPendingPWTHelper pendingPWTHelper = new ProcessPendingPWTHelper();
		List<PlayerPWTBean> searchResults = pendingPWTHelper
				.SearchPendingPWT(searchMap);

		if (searchResults != null && searchResults.size() > 0) {

			session.setAttribute("SEARCH_RESULTS1", searchResults);
			session.setAttribute("startValueOrderSearch", new Integer(0));
			session.setAttribute("SearchResultsAvailable", "Yes");
			setSearchResultsAvailable("Yes");
		} else {
			setSearchResultsAvailable("No");
			session.setAttribute("SearchResultsAvailable", "No");
		}

		searchAjax();

		return SUCCESS;
	}

	public void setChequeDate(String chequeDate) {
		this.chequeDate = chequeDate;
	}

	public void setChequeNbr(String chequeNbr) {
		this.chequeNbr = chequeNbr;
	}

	public void setDenyPwtStatus(String denyPwtStatus) {
		this.denyPwtStatus = denyPwtStatus;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public void setDraweeBank(String draweeBank) {
		this.draweeBank = draweeBank;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public void setGameNbr(String gameNbr) {
		this.gameNbr = gameNbr;
	}

	public void setIssuingParty(String issuingParty) {
		this.issuingParty = issuingParty;
	}

	public void setNetAmt(double netAmt) {
		this.netAmt = netAmt;
	}

	public void setPlayerFirstName(String playerFirstName) {
		this.playerFirstName = playerFirstName;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public void setPwtAmt(double pwtAmt) {
		this.pwtAmt = pwtAmt;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setSearchResultsAvailable(String searchResultsAvailable) {
		this.searchResultsAvailable = searchResultsAvailable;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setTax(double tax) {
		this.tax = tax;
	}

	public void setTicketNbr(String ticketNbr) {
		this.ticketNbr = ticketNbr;
	}

	public void setTrancDate(String trancDate) {
		this.trancDate = trancDate;
	}

	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}

	public void setVirnCode(String virnCode) {
		this.virnCode = virnCode;
	}

}
