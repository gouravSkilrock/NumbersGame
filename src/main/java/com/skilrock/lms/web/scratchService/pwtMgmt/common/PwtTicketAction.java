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

package com.skilrock.lms.web.scratchService.pwtMgmt.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.ajax.AjaxRequestHelper;
import com.skilrock.lms.beans.ActiveGameBean;
import com.skilrock.lms.beans.GameTicketFormatBean;
import com.skilrock.lms.beans.TicketBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.scratchService.pwtMgmt.common.PwtTicketHelper;

public class PwtTicketAction extends ActionSupport implements
		ServletRequestAware {
	static Log logger = LogFactory.getLog(PwtTicketAction.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<TicketBean> duplicateticketList;

	private String gameNbr_Name;
	private int partyOrgId;
	private String PWTFlag;
	private HttpServletRequest request;
	private List<TicketBean> savedTicketList;
	private String ticketDetails;
	// private int game_id;
	private List<TicketBean> ticketList;
	private List<TicketBean> ticketList2;

	List<String> ticketListString = new ArrayList<String>();
	private String[] ticketNumber;

	public String displayPwtTicketEntryPage() throws LMSException {
		HttpSession session = getRequest().getSession();
		UserInfoBean userInfo = (UserInfoBean) session
				.getAttribute("USER_INFO");
		setTicketList(new ArrayList<TicketBean>());
		PwtTicketHelper pwtTicketHelper = new PwtTicketHelper();
		List<ActiveGameBean> activeGameList = pwtTicketHelper.getActiveGames();

		AjaxRequestHelper helper = new AjaxRequestHelper();
		Map<Integer, String> map = helper.fetchOrganizationListMap("agentpwt",
				userInfo);

		List<GameTicketFormatBean> gameFormatList = null;
		if (activeGameList != null && activeGameList.size() > 0) {
			// gameFormatList =
			// pwtTicketHelper.getGameTicketFormatList(activeGameList);
			session.setAttribute("GAME_FORMAT_LIST", gameFormatList);
		}

		session.setAttribute("AGENT_LIST_MAP", map);
		session.setAttribute("ACTIVE_GAME_LIST", activeGameList);
		return SUCCESS;

	}

	public String getGameNbr_Name() {
		return gameNbr_Name;
	}

	/*
	 * public String saveTicketsData() {
	 * 
	 * HttpSession session = getRequest().getSession(); List<TicketBean>
	 * verifiedTicketList = (List) session.getAttribute("VERIFIED_TICKET_LIST");
	 * //List<ActiveGameBean> activeGameBeanList=(List)
	 * session.getAttribute("ACTIVE_GAME_LIST"); String
	 * gameNbrName=(String)session.getAttribute("SELECTED_GAMENBR_NAME");
	 * UserInfoBean userBean=(UserInfoBean)session.getAttribute("USER_INFO");
	 * System.out.println("Ticket List Size::" + verifiedTicketList.size());
	 * //System.out.println(activeGameBeanList);
	 * System.out.println("........................."+gameNbrName);
	 * 
	 * PwtTicketHelper pwtTicketHelper = new PwtTicketHelper(); int game_id =
	 * pwtTicketHelper.getGameIdFromDataBase(gameNbrName);
	 * 
	 * System.out.println("Get the Game ID is : "+game_id);
	 * 
	 * setSavedTicketList(pwtTicketHelper.saveTicketsData(game_id,verifiedTicketList,userBean.getUserId(),userBean.getUserOrgId()));
	 * 
	 * System.out.println("Afterrrrrrrrr callingggg save dataa");
	 * 
	 * session.setAttribute("SAVED_TICKET_LIST", savedTicketList);
	 * 
	 * System.out.println("Saved List is "+savedTicketList);
	 * 
	 * return SUCCESS; }
	 */
	/*
	 * public String comeBack(){ HttpSession session =
	 * getRequest().getSession();
	 * session.removeAttribute("VERIFIED_TICKET_LIST");
	 * session.removeAttribute("SELECTED_GAMENBR_NAME");
	 * session.removeAttribute("ACTIVE_GAME_LIST");
	 * session.removeAttribute("SAVED_TICKET_LIST");
	 * session.removeAttribute("TICKET_LIST"); return SUCCESS; }
	 */

	public int getPartyOrgId() {
		return partyOrgId;
	}

	public String getPWTFlag() {
		return PWTFlag;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public List<TicketBean> getSavedTicketList() {
		return savedTicketList;
	}

	public String getTicketDetails() {
		return ticketDetails;
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

	public void setGameNbr_Name(String gameNbr_Name) {
		this.gameNbr_Name = gameNbr_Name;
	}

	public void setPartyOrgId(int partyOrgId) {
		this.partyOrgId = partyOrgId;
	}

	public void setPWTFlag(String flag) {
		PWTFlag = flag;
	}

	public void setSavedTicketList(List<TicketBean> savedTicketList) {
		this.savedTicketList = savedTicketList;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setTicketDetails(String ticketDetails) {
		this.ticketDetails = ticketDetails;
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

	public String verifyTickets() throws LMSException {

		HttpSession session = getRequest().getSession();
		PwtTicketHelper pwtTicketHelper = new PwtTicketHelper();

		// read ticket from text files and text boxes
		Map map = pwtTicketHelper.getTicketList(ticketNumber, ticketDetails);
		if (map != null && map.containsKey("error")) {
			addActionError("Data In File Exceeds limit ");
			return ERROR;
		} else if (map != null && map.isEmpty()) {
			addActionError("Ticket List is Empty");
			return ERROR;
		} else {
			ticketListString = (List<String>) map.get("ticketListString");
			duplicateticketList = (List<TicketBean>) map
					.get("duplicateticketList");
			System.out.println("tktList = " + ticketListString
					+ "\n duplicateticketList = " + duplicateticketList);
		}

		// get the verified TicketBean List
		int gameNbr = Integer.parseInt(getGameNbr_Name().split("-")[0]);
		List<TicketBean> verifiedTicketList = pwtTicketHelper
				.getGameWiseVerifiedTickets(ticketListString, gameNbr);

		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		userBean.setChannel("RETAIL");
		userBean.setInterfaceType((String) request
				.getAttribute("interfaceType"));
		if (verifiedTicketList != null) {
			pwtTicketHelper.saveTicketsData(verifiedTicketList, userBean
					.getUserId(), userBean.getUserOrgId(), partyOrgId, userBean
					.getChannel(), userBean.getInterfaceType());
			verifiedTicketList.addAll(duplicateticketList);
			session.setAttribute("VERIFIED_TICKET_LIST", verifiedTicketList);
			return SUCCESS;
		} else {
			addActionError("Ticket Number is not in correct format");
			// session.setAttribute("TICKET_LIST",ticketList);
			return ERROR;
		}

	}

}
