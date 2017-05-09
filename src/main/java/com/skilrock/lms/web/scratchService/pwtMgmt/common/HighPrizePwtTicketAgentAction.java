package com.skilrock.lms.web.scratchService.pwtMgmt.common;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.ActiveGameBean;
import com.skilrock.lms.beans.GameTicketFormatBean;
import com.skilrock.lms.beans.TicketBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.coreEngine.scratchService.pwtMgmt.common.HighPrizePwtTicketAgentHelper;

public class HighPrizePwtTicketAgentAction extends ActionSupport implements
		ServletRequestAware {

	static Log logger = LogFactory.getLog(HighPrizePwtTicketAgentAction.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int game_id;

	private String gameNbr_Name;
	private String HighPrize;
	private HttpServletRequest request;
	private List<TicketBean> savedTicketList;
	private List<TicketBean> ticketList;
	private List<TicketBean> ticketList2;
	private String[] ticketNumber;

	public String addTicketRow() {

		// System.out.println("Game Name & Nbr::" + getGameNbr_Name());
		HttpSession session = getRequest().getSession();
		List<TicketBean> ticketList = (List) session
				.getAttribute("TICKET_LIST");

		copyValuesToBean();
		// System.out.println("Size:" + getVirnCode().length);

		if (ticketList != null) {

			ticketList.add(new TicketBean());
		}

		setTicketList(ticketList);
		return SUCCESS;

	}

	public String comeBack() {
		HttpSession session = getRequest().getSession();
		session.removeAttribute("VERIFIED_TICKET_LIST");
		session.removeAttribute("SELECTED_GAMENBR_NAME");
		session.removeAttribute("ACTIVE_GAME_LIST");
		session.removeAttribute("SAVED_TICKET_LIST");
		session.removeAttribute("TICKET_LIST");
		return SUCCESS;
	}

	private void copyValuesToBean() {

		HttpSession session = getRequest().getSession();
		List<TicketBean> ticketList = (List) session
				.getAttribute("TICKET_LIST");
		TicketBean ticketBean = null;

		List<ActiveGameBean> activeGameList = null;
		activeGameList = (List) session.getAttribute("ACTIVE_GAME_LIST");
		HighPrizePwtTicketAgentHelper pwtTicketHelper = new HighPrizePwtTicketAgentHelper();
		int game_id;
		game_id = pwtTicketHelper.getGameId(activeGameList, getGameNbr_Name());
		GameTicketFormatBean gameTicketFmtBean = pwtTicketHelper
				.getGameTicketFormat(game_id);
		int gameNbrDigits = gameTicketFmtBean.getGameNbrDigits();
		int packNbrDigits = gameTicketFmtBean.getPackDigits();
		int bookNbrDigits = gameTicketFmtBean.getBookDigits();

		String ticketNbr = null;
		if (ticketList != null) {
			for (int i = 0; i < ticketList.size(); i++) {
				ticketBean = ticketList.get(i);
				ticketNbr = getTicketNumber()[i];

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
				int size = ticketNbr.length();

				// System.out.println("ticket No:"+i+" :
				// "+getTicketNumber()[i]+" and Size is :"+size);

			}
		}

	}

	public String displayPwtTicketEntryPage() {

		HttpSession session = getRequest().getSession();
		setTicketList(new ArrayList<TicketBean>());

		HighPrizePwtTicketAgentHelper pwtTicketHelper = new HighPrizePwtTicketAgentHelper();
		List<ActiveGameBean> activeGameList = pwtTicketHelper.getActiveGames();

		session.setAttribute("ACTIVE_GAME_LIST", activeGameList);

		List<TicketBean> ticketList = getTicketList();

		ticketList.add(new TicketBean());
		ticketList.add(new TicketBean());
		ticketList.add(new TicketBean());
		ticketList.add(new TicketBean());
		ticketList.add(new TicketBean());
		ticketList.add(new TicketBean());
		ticketList.add(new TicketBean());
		ticketList.add(new TicketBean());
		ticketList.add(new TicketBean());
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

	public String getHighPrize() {
		return HighPrize;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public List<TicketBean> getSavedTicketList() {
		return savedTicketList;
	}

	public List<TicketBean> getTicketList() {
		return ticketList;
	}

	public List<TicketBean> getTicketList2() {
		return ticketList2;
	}

	public String[] getTicketNumber() {
		return ticketNumber;
	}

	public String saveTicketsData() {

		HttpSession session = getRequest().getSession();
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");

		List<TicketBean> verifiedTicketList = (List) session
				.getAttribute("VERIFIED_TICKET_LIST");
		String gameNbrName = (String) session
				.getAttribute("SELECTED_GAMENBR_NAME");
		System.out.println("Ticket List Size::" + verifiedTicketList.size());
		System.out.println("........................." + gameNbrName);
		HighPrizePwtTicketAgentHelper pwtTicketHelper = new HighPrizePwtTicketAgentHelper();
		int game_id = pwtTicketHelper.getGameIdFromDataBase(gameNbrName);
		System.out.println("Get the Game ID is : " + game_id);

		// get game nbr from gamr nbr name
		String[] gameNameNbrArr = gameNbrName.split("-");
		int gameNbr = Integer.parseInt(gameNameNbrArr[0]);
		setSavedTicketList(pwtTicketHelper.saveTicketsData(game_id,
				verifiedTicketList, userBean.getUserId(), gameNbr, userBean
						.getUserOrgId()));
		System.out.println("Afterrrrrrrrr callingggg save dataa");
		session.setAttribute("SAVED_TICKET_LIST", savedTicketList);
		System.out.println("Saved List is " + savedTicketList);

		return SUCCESS;

	}

	public void setGame_id(int game_id) {
		this.game_id = game_id;
	}

	public void setGameNbr_Name(String gameNbr_Name) {
		this.gameNbr_Name = gameNbr_Name;
	}

	public void setHighPrize(String highPrize) {
		HighPrize = highPrize;
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

	public void setTicketNumber(String[] ticketNumber) {
		this.ticketNumber = ticketNumber;
	}

	public String verifyTickets() {
		copyValuesToBean();
		HttpSession session = getRequest().getSession();

		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");

		List<TicketBean> ticketList = (List) session
				.getAttribute("TICKET_LIST");

		// System.out.println("Ticket List Size::" + ticketList.size());
		// System.out.println(getGameNbr_Name());

		List<ActiveGameBean> activeGameList = null;
		session.setAttribute("SELECTED_GAMENBR_NAME", getGameNbr_Name());
		activeGameList = (List) session.getAttribute("ACTIVE_GAME_LIST");
		HighPrizePwtTicketAgentHelper pwtTicketHelper = new HighPrizePwtTicketAgentHelper();
		int game_id = 0;
		game_id = pwtTicketHelper.getGameId(activeGameList, getGameNbr_Name());

		// get game nbr from game name nbr
		String[] gameNameNbrArr = getGameNbr_Name().split("-");
		int gameNbr = Integer.parseInt(gameNameNbrArr[0]);

		System.out.println("game id at the time of verification: " + game_id);

		ticketList2 = pwtTicketHelper.getVerifiedTickets(ticketList, game_id,
				gameNbr, userBean.getUserOrgId());
		if (ticketList2 == null) {
			addActionError("Entered Ticket Numbers are not in correct format(gameNo-packNoBookNo-TicketNo)");
			return ERROR;
		}
		// setTicketList2(pwtTicketHelper.getVerifiedTickets(ticketList,game_id));
		session.setAttribute("VERIFIED_TICKET_LIST", ticketList2);
		session.setAttribute("HIGH_PRIZE", pwtTicketHelper.getHighPrizeAgent());

		return SUCCESS;
	}

}
