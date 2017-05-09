package com.skilrock.lms.web.scratchService.inventoryMgmt.common;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.InventoryGameReportLinkBean;
import com.skilrock.lms.coreEngine.scratchService.inventoryMgmt.common.InventoryGameReportLinkHelper;

public class InventoryGameReportLink extends ActionSupport implements
		ServletRequestAware {

	/**
	 * 
	 */
	static Log logger = LogFactory.getLog(InventoryGameReportLink.class);

	private static final long serialVersionUID = 1L;
	private int gameid;
	private String gamename;
	private int gamenumber;
	private String saleenddate;
	private HttpServletRequest servletRequest;

	public int getGameid() {
		return gameid;
	}

	public String getGamename() {
		return gamename;
	}

	public int getGamenumber() {
		return gamenumber;
	}

	public String getSaleenddate() {
		return saleenddate;
	}

	public HttpServletRequest getServletRequest() {
		return servletRequest;
	}

	public String inventoryGameReportLink() {
		List<InventoryGameReportLinkBean> gamebeanlist = null;
		logger.debug(" game id " + gameid + "Game Name : " + gamename
				+ "\tGame Number : " + gamenumber + "\tSale End Date : "
				+ saleenddate);
		InventoryGameReportLinkHelper gameReportLinkhelper = new InventoryGameReportLinkHelper();
		gamebeanlist = gameReportLinkhelper.getDetails(gameid, gamename,
				gamenumber, saleenddate);
		getServletRequest().getSession().setAttribute("searchResultBOLink",
				gamebeanlist);
		return "success";
	}

	public void setGameid(int gameid) {
		this.gameid = gameid;
	}

	public void setGamename(String gamename) {
		this.gamename = gamename;
	}

	public void setGamenumber(int gamenumber) {
		this.gamenumber = gamenumber;
	}

	public void setSaleenddate(String saleenddate) {
		this.saleenddate = saleenddate;
	}

	public void setServletRequest(HttpServletRequest servletRequest) {
		this.servletRequest = servletRequest;
	}

}
