package com.skilrock.lms.embedded.scratchService.inventoryMgmt.common;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.scratchService.inventoryMgmt.common.SoldTicketEntryForRetHelper;
import com.skilrock.lms.coreEngine.userMgmt.common.CommonFunctionsHelper;
import com.skilrock.lms.embedded.drawGames.common.EmbeddedErrors;

public class SoldTicketEntryForRet extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	static Log logger = LogFactory.getLog(SoldTicketEntryForRet.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String[] bookNbr;

	private String[] currRem;

	private String[] gameName;

	private HttpServletResponse response;
	private String[] tktInBook;
	private String[] updCurrRem;
	private String userName;

	public void fetchBooksDetail() throws LMSException {
		System.out.println("fetchBooksDetail");
		try {
			ServletContext sc = ServletActionContext.getServletContext();
			String isDraw = (String) sc.getAttribute("IS_DRAW");
			Map currentUserSessionMap = (Map) sc
					.getAttribute("LOGGED_IN_USERS");
			ServletOutputStream out = response.getOutputStream();
			if (currentUserSessionMap == null) {
				out
						.write(("ErrorMsg:" + EmbeddedErrors.SESSION_EXPIRED + "ErrorCode:01|")
								.getBytes());
				return;
			}
			// logger.debug(" LOGGED_IN_USERS maps is " +
			// currentUserSessionMap);
			HttpSession session = (HttpSession) currentUserSessionMap
					.get(userName);

			if (!CommonFunctionsHelper.isSessionValid(session)) {
				out
						.write(("ErrorMsg:" + EmbeddedErrors.SESSION_EXPIRED + "ErrorCode:01|")
								.getBytes());
				return;
			}

			UserInfoBean userInfo = (UserInfoBean) session
					.getAttribute("USER_INFO");

			SoldTicketEntryForRetHelper helper = new SoldTicketEntryForRetHelper();
			String resString = helper
					.fetchBooksDetails(userInfo.getUserOrgId());
			if ("".equals(resString.trim())) {
				out.write("ErrorMsg:No Books Available".getBytes());
			} else {
				out.write(resString.toString().getBytes());
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new LMSException(e);
		}

	}

	public String[] getBookNbr() {
		return bookNbr;
	}

	public String[] getCurrRem() {
		return currRem;
	}

	public String[] getGameName() {
		return gameName;
	}

	public String[] getTktInBook() {
		return tktInBook;
	}

	public String[] getUpdCurrRem() {
		return updCurrRem;
	}

	public String getUserName() {
		return userName;
	}

	public void saveSoldTicketEntry() {
		ServletOutputStream out = null;
		try {
			System.out.println("saveSoldTicketEntry");

			ServletContext sc = ServletActionContext.getServletContext();
			// String isDraw = (String) sc.getAttribute("IS_DRAW");
			out = response.getOutputStream();
			Map currentUserSessionMap = (Map) sc
					.getAttribute("LOGGED_IN_USERS");
			if (currentUserSessionMap == null) {
				out
						.write(("ErrorMsg:" + EmbeddedErrors.SESSION_EXPIRED + "ErrorCode:01|")
								.getBytes());
				return;
			}
			// logger.debug(" LOGGED_IN_USERS maps is " +
			// currentUserSessionMap);
			HttpSession session = (HttpSession) currentUserSessionMap
					.get(userName);
			if (!CommonFunctionsHelper.isSessionValid(session)) {
				out
						.write(("ErrorMsg:" + EmbeddedErrors.SESSION_EXPIRED + "ErrorCode:01|")
								.getBytes());
				return;
			}

			UserInfoBean userInfo = (UserInfoBean) session
					.getAttribute("USER_INFO");
			//SoldTicketEntryForRetHelper helper = new SoldTicketEntryForRetHelper();
			boolean flag = SoldTicketEntryForRetHelper.saveSoldTicketEntry(bookNbr, currRem,
					userInfo, updCurrRem, tktInBook);
			if (flag) {
				out.write("true".getBytes());
			} else {
				out.write("false".getBytes());
			}

		} catch (LMSException e) {
			try {
				out.write("false".getBytes());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setBookNbr(String[] bookNbr) {
		this.bookNbr = bookNbr;
	}

	public void setCurrRem(String[] currRem) {
		this.currRem = currRem;
	}

	public void setGameName(String[] gameName) {
		this.gameName = gameName;
	}

	public void setServletRequest(HttpServletRequest req) {

	}

	public void setServletResponse(HttpServletResponse res) {
		this.response = res;

	}

	public void setTktInBook(String[] tktInBook) {
		this.tktInBook = tktInBook;
	}

	public void setUpdCurrRem(String[] updCurrRem) {
		this.updCurrRem = updCurrRem;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
