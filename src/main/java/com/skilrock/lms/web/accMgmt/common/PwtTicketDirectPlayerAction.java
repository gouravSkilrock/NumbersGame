/***
 *  * © copyright 2007, SkilRock Technologies, A division of Sugal & Damani Lottery Agency Pvt. Ltd.
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
 * 
 */
package com.skilrock.lms.web.accMgmt.common;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.ActiveGameBean;
import com.skilrock.lms.beans.GameTicketFormatBean;
import com.skilrock.lms.beans.TicketBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.accMgmt.common.PwtTicketDirectPlayerHelper;

/**
 * This class is used to Direct Player PWT ticket verification.
 * 
 * @author Skilrock Technologies
 * 
 */

public class PwtTicketDirectPlayerAction extends ActionSupport implements
		ServletRequestAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Connection conn = null;
	private int game_id;

	private String gameNbr_Name;
	private HttpServletRequest request;
	private List<TicketBean> savedTicketList;
	private List<TicketBean> ticketList;
	private List<TicketBean> ticketList2;
	private String ticketNumber;

	private void copyValuesToBean() {

		HttpSession session = getRequest().getSession();
		List<TicketBean> ticketList = (List) session
				.getAttribute("TICKET_LIST");

		GameTicketFormatBean gameFormatBean = null;
		List<ActiveGameBean> activeGameList = (List) session
				.getAttribute("ACTIVE_GAME_LIST");
		List<GameTicketFormatBean> gameFormatList = (List) session
				.getAttribute("GAME_FORMAT_LIST");
		PwtTicketDirectPlayerHelper pwtTicketHelper = new PwtTicketDirectPlayerHelper();
		int gameId = pwtTicketHelper.getGameId(activeGameList,
				getGameNbr_Name());

		for (int i = 0; i < gameFormatList.size(); i++) {
			gameFormatBean = gameFormatList.get(i);

			if (gameId == gameFormatBean.getGameId()) {
				break;
			}

		}

		int gameNbrDigits = gameFormatBean.getGameNbrDigits();
		int packNbrDigits = gameFormatBean.getPackDigits();
		int bookNbrDigits = gameFormatBean.getBookDigits();

		TicketBean ticketBean = null;
		String ticketNbr = null;
		if (ticketList != null) {
			for (int i = 0; i < ticketList.size(); i++) {
				ticketBean = ticketList.get(i);

				ticketNbr = getTicketNumber();

				if (ticketNbr.indexOf("-") == -1
						&& ticketNbr.length() > gameNbrDigits) {
					ticketNbr = ticketNbr.substring(0, gameNbrDigits) + "-"
							+ ticketNbr.substring(gameNbrDigits);
					ticketNbr = ticketNbr.substring(0, gameNbrDigits
							+ packNbrDigits + bookNbrDigits + 1)
							+ "-"
							+ ticketNbr.substring(gameNbrDigits + packNbrDigits
									+ bookNbrDigits + 1);

				}

				ticketBean.setTicketNumber(ticketNbr);
				System.out.println("######ticket number is " + ticketNbr);
				// int size=getTicketNumber().length();

				// System.out.println("ticket No:"+i+" :
				// "+getTicketNumber()[i]+" and Size is :"+size);

			}
		}

	}

	/**
	 * This method is used to display Game-Ticket validation page
	 * 
	 * @return SUCCESS
	 * @throws LMSException
	 */
	public String displayPwtTicketEntryPage() throws LMSException {

		int count = 1;
		HttpSession session = getRequest().getSession();
		String varTRANC_START = (String) session.getAttribute("TRANC_START");
		// varTRANC_START variable is used to trace it is the staring of the
		// transaction .because the same connection object would be used for
		// other classes to make the transaction by the same connection.
		if (varTRANC_START == null || varTRANC_START != null
				&& varTRANC_START.equals("END")) {
			session.setAttribute("TRANC_START", "START");
		}
		if (varTRANC_START != null && varTRANC_START.equals("START")) {

			Connection conn = (Connection) session.getAttribute("ConnObject");
			System.out.println("conn????????" + conn);
			if (conn != null) {
				try {

					conn.rollback();
					conn.close();
				} catch (SQLException e) {
					System.out.println("SQL Exception in rollaback the conn");
					throw new LMSException();

				}
			}

		}

		setTicketList(new ArrayList<TicketBean>());

		PwtTicketDirectPlayerHelper pwtTicketHelper = new PwtTicketDirectPlayerHelper();
		List<ActiveGameBean> activeGameList = pwtTicketHelper.getActiveGames();

		List<GameTicketFormatBean> gameFormatList = null;

		if (activeGameList != null && activeGameList.size() > 0) {
			gameFormatList = pwtTicketHelper
					.getGameTicketFormatList(activeGameList);

			session.setAttribute("GAME_FORMAT_LIST", gameFormatList);
		}

		session.setAttribute("ACTIVE_GAME_LIST", activeGameList);

		// to get the list of active games i.e 'OPEN','SALE_HOLD','SALE_CLOSE'
		session.setAttribute("ACTIVE_GAME_LIST", activeGameList);
		session.setAttribute("COUNTER", new Integer(count));
		List<TicketBean> ticketList = getTicketList();

		ticketList.add(new TicketBean());

		session.setAttribute("TICKET_LIST", ticketList);

		return SUCCESS;

	}

	public int getGame_id() {
		return game_id;
	}

	public String getGameNbr_Name() {
		return gameNbr_Name;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	// commented by yogesh it is not in use anywhere in LMS

	/*
	 * private String saveTicketsData() throws LMSException {
	 * 
	 * HttpSession session = getRequest().getSession(); List<TicketBean>
	 * verifiedTicketList = (List) session
	 * .getAttribute("VERIFIED_TICKET_LIST"); String gameNbrName = (String)
	 * session .getAttribute("SELECTED_GAMENBR_NAME");
	 * 
	 * System.out.println("Ticket List Size::" + verifiedTicketList.size());
	 * 
	 * System.out.println("........................." + gameNbrName);
	 * 
	 * PwtTicketDirectPlayerHelper pwtTicketHelper = new
	 * PwtTicketDirectPlayerHelper(); int game_id =
	 * pwtTicketHelper.getGameIdFromDataBase(gameNbrName);
	 * 
	 * session.setAttribute("GAME_ID", game_id); System.out.println("Get the
	 * Game ID is : " + game_id);
	 * 
	 * setSavedTicketList(pwtTicketHelper.saveTicketsData(game_id,
	 * verifiedTicketList));
	 * 
	 * conn = pwtTicketHelper.getConnectrion(); System.out.println("Connection
	 * Object in Session :" + conn);
	 * 
	 * session.setAttribute("ConnObject", conn);
	 * System.out.println("Afterrrrrrrrr callingggg save dataa");
	 * 
	 * session.setAttribute("SAVED_TICKET_LIST", savedTicketList);
	 * 
	 * System.out.println("Saved List is " + savedTicketList);
	 * 
	 * return SUCCESS; }
	 */

	public List<TicketBean> getSavedTicketList() {
		return savedTicketList;
	}

	public List<TicketBean> getTicketList() {
		return ticketList;
	}

	public List<TicketBean> getTicketList2() {
		return ticketList2;
	}

	public String getTicketNumber() {
		return ticketNumber;
	}

	public void setGame_id(int game_id) {
		this.game_id = game_id;
	}

	public void setGameNbr_Name(String gameNbr_Name) {
		this.gameNbr_Name = gameNbr_Name;
	}

	public void setSavedTicketList(List<TicketBean> savedTicketList) {
		this.savedTicketList = savedTicketList;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setTicketList(List<TicketBean> ticketList) {
		this.ticketList = ticketList;
	}

	public void setTicketList2(List<TicketBean> ticketList2) {
		this.ticketList2 = ticketList2;
	}

	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}

	/**
	 * This method is used to verify ticket wheather this ticket is valid or
	 * not..
	 * 
	 * @return String
	 * @throws LMSException
	 */
	public String verifyTickets() throws LMSException {
		copyValuesToBean();
		HttpSession session = getRequest().getSession();
		List<TicketBean> ticketList = (List<TicketBean>) session
				.getAttribute("TICKET_LIST");
		System.out.println("ticket list is *********** "
				+ ticketList.get(0).getTicketNumber());

		if (ticketList != null) {
			// this flag is used to enable or disable the button for further
			// process, if ticket is invalid button for further process would
			// not be shown on the jsp page dated 7 march-2008.
			session.setAttribute("statusFlag", "No");
			System.out
					.println(ticketList.size() + ">>>>>>>size of ticket list");
			for (int i = 0; i < ticketList.size(); i++) {
				boolean varFlag = false;
				varFlag = ticketList.get(i).getIsValid();
				System.out.println(varFlag
						+ ">>>var for falg set to ticket varification");
				if (varFlag) {
					session.setAttribute("statusFlag", "Yes");
					break;
				} else {
					continue;
				}
			}
		}

		List<ActiveGameBean> activeGameList = null;
		session.setAttribute("SELECTED_GAMENBR_NAME", getGameNbr_Name());
		activeGameList = (List) session.getAttribute("ACTIVE_GAME_LIST");
		// counter is used because if user refresh the same page and the same
		// action occurs so counter increases,so to block that increment by
		// refreshing. This counter is used to show the steps followed by user
		// to complete direct pwt receive process dated 7-march-2008.
		int count = (Integer) session.getAttribute("COUNTER");
		count = count + 1;
		if (count > 2) {
			count = 1;
		}
		session.setAttribute("COUNTER", new Integer(count));
		PwtTicketDirectPlayerHelper pwtTicketHelper = new PwtTicketDirectPlayerHelper();
		int game_id = -1;
		game_id = pwtTicketHelper.getGameId(activeGameList, getGameNbr_Name());

		/*
		 * if (game_id > 0) { game_id =
		 * pwtTicketHelper.getGameId(activeGameList, getGameNbr_Name());
		 * 
		 * System.out.println("game id at the time of verification: " +
		 * game_id); }else{
		 * 
		 * addActionError("Probably Game is not currect or some may Internal
		 * Error"); return ERROR; }
		 */
		if (game_id <= 0) {
			addActionError("Probably Game is not currect or some may Internal Error");
			return ERROR;
		}

		// get game nbr from game nbr name
		String[] gameNameNbrArr = getGameNbr_Name().split("-");
		int gameNbr = Integer.parseInt(gameNameNbrArr[0]);

		List<TicketBean> ticketList3 = pwtTicketHelper.getVerifiedTickets(
				ticketList, game_id);

		if (ticketList3 == null) {

			addActionError("Ticket Number is not in Correct Format");
			return ERROR;

		}
		setTicketList2(ticketList3);

		session.setAttribute("TICKET_NUMBER", ticketNumber);
		session.setAttribute("VERIFIED_TICKET_LIST", ticketList2);
		session.setAttribute("GAME_ID", game_id);
		session.setAttribute("GAME_NBR", gameNbr);
		if (ticketList2 != null) {
			session.setAttribute("statusFlag", "No");
			System.out.println(ticketList2.size()
					+ ">>>>>>>size of ticket list");
			for (int i = 0; i < ticketList.size(); i++) {
				boolean varFlag = false;
				varFlag = ticketList2.get(i).getIsValid();
				System.out.println(varFlag
						+ ">>>var for falg set to ticket varification");
				if (varFlag) {
					session.setAttribute("statusFlag", "Yes");
					break;
				} else {
					continue;
				}
			}

		}
		return SUCCESS;
	}

}
