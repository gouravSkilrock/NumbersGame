package com.skilrock.lms.web.accMgmt.common;

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

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.PwtBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.accMgmt.common.PWTPlayerHelper;

/**
 * This class is used to verify the VIRN code for direct player PWT.
 * 
 * @author Skilrock Technologies.
 * 
 */
public class PWTPlayerAction extends ActionSupport implements
		ServletRequestAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// private String[] pwtAmount;
	private String gameNbr_Name;

	private HttpServletRequest request;
	// private String agtOrgName;
	private String varForGameVarification = null;
	private String virnCode = null;

	/***************************************************************************
	 * This method is used to continue process of virn code verification.
	 * 
	 * @return SUCCESS.
	 */
	public String chequeProcess() {
		HttpSession session = getRequest().getSession();
		List list = (List) session.getAttribute("SAVED_TICKET_LIST");
		if (list != null) {
			if (list.size() > 0) {
				setVarForGameVarification("Yes");

			}
			return SUCCESS;

		} else {
			setVarForGameVarification("No");
			addActionError("There is  NO Verified Ticket for PWT ");
			return ERROR;
		}
	}

	/**
	 * This method is used to display VIRN code verify page.
	 * 
	 * @return SUCCESS
	 */
	public String displayPwtPlayerEntryPage() {
		HttpSession session = getRequest().getSession();
		System.out.println("Exittttted---------------");
		return SUCCESS;

	}

	public String getGameNbr_Name() {
		return gameNbr_Name;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public String getVarForGameVarification() {
		return varForGameVarification;
	}

	public String getVirnCode() {
		return virnCode;
	}

	public void setGameNbr_Name(String gameNbr_Name) {
		this.gameNbr_Name = gameNbr_Name;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setVarForGameVarification(String varForGameVarification) {
		this.varForGameVarification = varForGameVarification;
	}

	public void setVirnCode(String virnCode) {
		this.virnCode = virnCode;
	}

	/**
	 * This method is used to verify the virn code for the ticket.
	 * 
	 * @return SUCCESS.
	 * @throws LMSException
	 */

	public String verifyPwtTickets() throws LMSException {

		int gameId = 0;
		int gameNbr = 0;
		HttpSession session = getRequest().getSession();
		session.setAttribute("PWTLIST", null);

		PWTPlayerHelper pwtPlayerHelper = new PWTPlayerHelper();

		if (session.getAttribute("GAME_ID") != null) {
			gameId = ((Integer) session.getAttribute("GAME_ID")).intValue();
			gameNbr = ((Integer) session.getAttribute("GAME_NBR")).intValue();
		}

		boolean verify = pwtPlayerHelper.getverifyPwtTickets(virnCode, gameId,
				gameNbr);
		if (verify) {
			List<PwtBean> pwtList = pwtPlayerHelper.getPwtList();
			session.setAttribute("PWTLIST", pwtList);

			System.out.println("Verified---------------");
			return SUCCESS;
		} else {
			List<PwtBean> pwtList = pwtPlayerHelper.getPwtList();
			String message = pwtList.get(0).getMessage();
			addActionError("Message::  " + message);
			return ERROR;
		}

	}

}
