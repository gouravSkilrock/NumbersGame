package com.skilrock.lms.web.scratchService.inventoryMgmt.common;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.BookSeriesBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.scratchService.inventoryMgmt.common.UpdateBookNVirnStatusHelper;

public class UpdateBookNVirnStatusAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	static Log logger = LogFactory.getLog(UpdateBookNVirnStatusAction.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String[] bookNbr;
	private String[] bookSeriesFrom;
	private String[] bookSeriesTo;

	private String fileName;
	private int gameId;
	private String[] packNbr;
	private String[] packSeriesFrom;
	private String[] packSeriesTo;
	private String remarks;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private HttpSession session;
	private String statusType;

	// common function to be called
	public void fetchGameDetailsAjax() {
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
			UpdateBookNVirnStatusHelper helper = new UpdateBookNVirnStatusHelper();
			String gameDetails = helper.fetchPwtGameDetails();
			System.out.println("game details String on retailer PWT == "
					+ gameDetails);
			pw.print(gameDetails);
		} catch (LMSException e) {
			e.printStackTrace();
			if (pw != null) {
				pw.print("");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String[] getBookNbr() {
		return bookNbr;
	}

	public String[] getBookSeriesFrom() {
		return bookSeriesFrom;
	}

	public String[] getBookSeriesTo() {
		return bookSeriesTo;
	}

	public String getFileName() {
		return fileName;
	}

	public int getGameId() {
		return gameId;
	}

	public String[] getPackNbr() {
		return packNbr;
	}

	public String[] getPackSeriesFrom() {
		return packSeriesFrom;
	}

	public String[] getPackSeriesTo() {
		return packSeriesTo;
	}

	public String getRemarks() {
		return remarks;
	}

	public String getStatusType() {
		return statusType;
	}

	public void setBookNbr(String[] bookNbr) {
		this.bookNbr = bookNbr;
	}

	public void setBookSeriesFrom(String[] bookSeriesFrom) {
		this.bookSeriesFrom = bookSeriesFrom;
	}

	public void setBookSeriesTo(String[] bookSeriesTo) {
		this.bookSeriesTo = bookSeriesTo;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public void setPackNbr(String[] packNbr) {
		this.packNbr = packNbr;
	}

	public void setPackSeriesFrom(String[] packSeriesFrom) {
		this.packSeriesFrom = packSeriesFrom;
	}

	public void setPackSeriesTo(String[] packSeriesTo) {
		this.packSeriesTo = packSeriesTo;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public void setServletRequest(HttpServletRequest req) {
		this.request = req;
	}

	public void setServletResponse(HttpServletResponse res) {
		this.response = res;

	}

	public void setStatusType(String statusType) {
		this.statusType = statusType;
		System.out.println("statusType ============================ "
				+ statusType);
	}

	public String updBookNVirnStatus() throws LMSException {

		session = request.getSession();
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");

		UpdateBookNVirnStatusHelper helper = new UpdateBookNVirnStatusHelper();

		String errMes = "";
		errMes = helper.validateBookNVirnEntries(fileName, gameId, bookNbr,
				bookSeriesFrom, bookSeriesTo, packNbr, packSeriesFrom,
				packSeriesTo, remarks);
		if (errMes != null && !"".equals(errMes.trim())) {
			addActionError(errMes);
			return INPUT;
		}
		ServletContext sc = ServletActionContext.getServletContext();
		String activateAt = (String) sc.getAttribute("BOOK_ACTIVATION_AT");
		Map bookMap = helper.updateBookNVirnStatus(fileName, gameId, bookNbr,
				bookSeriesFrom, bookSeriesTo, packNbr, packSeriesFrom,
				packSeriesTo, userBean.getUserId(), userBean.getUserOrgId(),
				userBean.getUserType(), statusType, remarks, activateAt);

		// Set<String> validBooks = (Set<String>)bookMap.get("validBooks");;
		Set<String> inValidBooks = (Set<String>) bookMap.get("inValidBooks");
		Set<String> invalidPacks = (Set<String>) bookMap.get("invalidPacks");
		List<BookSeriesBean> invalidBkSerList = (List<BookSeriesBean>) bookMap
				.get("invalidBkSerList");
		List<BookSeriesBean> invalidPkSerList = (List<BookSeriesBean>) bookMap
				.get("invalidPkSerList");

		Set<String> inValidVirnCodeSet = (Set<String>) bookMap
				.get("inValidVirnCodeSet");
		// Set<String> virnCodeSetToUpdate =
		// (Set<String>)bookMap.get("virnCodeSetToUpdate");

		session = request.getSession();
		session.setAttribute("miss_inv_bk", inValidBooks);
		session.setAttribute("miss_inv_pk", invalidPacks);
		session.setAttribute("miss_inv_bkser", invalidBkSerList);
		session.setAttribute("miss_inv_pkser", invalidPkSerList);
		session.setAttribute("miss_inv_virn", inValidVirnCodeSet);

		return SUCCESS;
	}

}
