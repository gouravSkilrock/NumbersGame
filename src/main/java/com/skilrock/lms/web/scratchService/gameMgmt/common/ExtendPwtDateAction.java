package com.skilrock.lms.web.scratchService.gameMgmt.common;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.OpenGameBean;
import com.skilrock.lms.coreEngine.scratchService.gameMgmt.common.ExtendPwtDateHelper;

/**
 * This class provides methods to extend the PWT ENd Date and to extend Sale End
 * Date
 * 
 * @author Skilrock Technologies
 * 
 */
public class ExtendPwtDateAction extends ActionSupport implements
		ServletRequestAware {

	static Log logger = LogFactory.getLog(ExtendPwtDateAction.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String pwtEndDate = "";
	private HttpServletRequest request;
	private String saleEndDate = "";

	/**
	 * This method is used for extend PWT date for game
	 * 
	 * @return String
	 * @throws Exception
	 */
	public String extendDate() throws Exception {

		HttpSession s = request.getSession();
		List<OpenGameBean> gameData = new ArrayList((List) s
				.getAttribute("GAME_SEARCH_RESULTS"));

		int gameId = gameData.get(0).getGameId();
		logger.debug("date is" + getPwtEndDate());

		ExtendPwtDateHelper extendDate = new ExtendPwtDateHelper();
		extendDate.extendDate(gameId, getPwtEndDate());

		// String query1 = QueryManager.getMrpForGovtComm() + " '"+pwtEndDate+"'
		// where game_id = "+y+" ";
		// stmt1.executeUpdate("update st_se_game_master set game_status='OPEN',
		// pwt_end_date ='"+getPwtEndDate()+"'where game_id = "+gameId+"");
		// addActionError("New Pwt End Date IS:: " + getPwtEndDate());
		s.setAttribute("PWT_END_DATE", getPwtEndDate());
		return SUCCESS;

	}

	/**
	 * This method is used for extend Sale End Date and Pwt End Date for game
	 * 
	 * @return String
	 * @throws Exception
	 */
	public String extendSaleDate() throws Exception {

		HttpSession s = request.getSession();
		String presentDate = (String) s.getAttribute("presentDate");
		logger.debug("present date at action is " + presentDate);
		List<OpenGameBean> gameData = new ArrayList((List) s
				.getAttribute("SALE_ClOSE_SEARCH_RESULTS"));

		int gameId = gameData.get(0).getGameId();
		String pwtDateDb = gameData.get(0).getPwt_end_date();
		String saleDateDb = gameData.get(0).getSaleEndDate();
		logger.debug("pwt from database is  " + pwtDateDb);
		logger.debug("pwt date is" + getPwtEndDate());
		logger.debug("sale date is" + getSaleEndDate());

		ExtendPwtDateHelper extendSaleDate = new ExtendPwtDateHelper();
		String returnType = extendSaleDate.extendSaleDate(gameId,
				getSaleEndDate(), getPwtEndDate(), pwtDateDb, saleDateDb,
				presentDate);

		// String query1 = QueryManager.getMrpForGovtComm() + " '"+pwtEndDate+"'
		// where game_id = "+y+" ";
		// stmt1.executeUpdate("update st_se_game_master set game_status='OPEN',
		// pwt_end_date ='"+getPwtEndDate()+"'where game_id = "+gameId+"");

		if (returnType.equals("ERROR1")) {
			addActionError(getText("msg.entr.valid.sale.end.date"));
			return ERROR;
		}

		if (returnType.equals("ERROR")) {
			addActionError(getText("msg.entr.dates"));
			return ERROR;
		}
		if (returnType.equals("NOCHANGE")) {
			addActionError("you did not changed the dates ");
			return SUCCESS;
		}

		// addActionError("New Pwt End Date is:: " + getPwtEndDate());
		// addActionError("New Sale End Date is::" + getPwtEndDate());
		s.setAttribute("SALE_END_DATE", getSaleEndDate());
		s.setAttribute("PWT_END_DATE", getPwtEndDate());
		return SUCCESS;

	}

	public String getPwtEndDate() {
		return pwtEndDate;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public String getSaleEndDate() {
		return saleEndDate;
	}

	public void setPwtEndDate(String pwtEndDate) {
		this.pwtEndDate = pwtEndDate;
	}

	public void setSaleEndDate(String saleEndDate) {
		this.saleEndDate = saleEndDate;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

}