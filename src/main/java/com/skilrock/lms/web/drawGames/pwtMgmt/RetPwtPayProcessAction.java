package com.skilrock.lms.web.drawGames.pwtMgmt;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.DrawPwtApproveRequestNPlrBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.drawGames.pwtMgmt.RetPwtPayProcessHelper;

public class RetPwtPayProcessAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String chequeDate;
	private String chqNbr;
	private String denyPwtStatus;
	private String draweeBank;
	private int drawId;
	private String firstName;
	private int gameId;

	private int gameNbr;
	private String issuiningParty;

	private String lastName;
	Log logger = LogFactory.getLog(RetPwtPayProcessAction.class);
	private double netAmt;
	private String panelId;
	private int partyId;
	private String partyType;

	private String paymentType;
	DrawPwtApproveRequestNPlrBean plrPwtBean;
	private double pwtAmount;
	List<DrawPwtApproveRequestNPlrBean> pwtPayDetailsList;
	HttpServletRequest request;
	private String requestId;
	HttpServletResponse response;
	private String status;
	private int taskId;

	private double taxAmt;

	private String ticketNbr;

	public String denyRequestedPwts() throws LMSException {
		HttpSession session = getRequest().getSession();
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		RetPwtPayProcessHelper processHelper = new RetPwtPayProcessHelper();
		if (processHelper.denyPWTProcess(taskId, drawId, gameId, ticketNbr,
				denyPwtStatus, gameNbr, userBean, panelId)) {
			return SUCCESS;
		} else {
			throw new LMSException("Request for payment not denied");
		}
	}

	public String getChequeDate() {
		return chequeDate;
	}

	public String getChqNbr() {
		return chqNbr;
	}

	public String getDenyPwtStatus() {
		return denyPwtStatus;
	}

	public String getDraweeBank() {
		return draweeBank;
	}

	public int getDrawId() {
		return drawId;
	}

	public String getFirstName() {
		return firstName;
	}

	public int getGameId() {
		return gameId;
	}

	public int getGameNbr() {
		return gameNbr;
	}

	public String getIssuiningParty() {
		return issuiningParty;
	}

	public String getLastName() {
		return lastName;
	}

	public double getNetAmt() {
		return netAmt;
	}

	public String getPanelId() {
		return panelId;
	}

	public int getPartyId() {
		return partyId;
	}

	public String getPartyType() {
		return partyType;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public String getPendingPwtToPay() throws LMSException {
		HttpSession session = getRequest().getSession();
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		int payByOrgId = userBean.getUserOrgId();
		RetPwtPayProcessHelper processHelper = new RetPwtPayProcessHelper();
		pwtPayDetailsList = processHelper.getRequestsPwtsToPay(requestId,
				firstName, lastName, status, payByOrgId);

		return SUCCESS;
	}

	public String getPendingPwtToPayDetails() throws LMSException {
		RetPwtPayProcessHelper processHelper = new RetPwtPayProcessHelper();
		plrPwtBean = processHelper.getPendingPwtDetails(taskId, partyType);
		// plrBean=(PlayerBean)plrPetDetails.get(0);
		// plrPwtBean=(PwtApproveRequestNPlrBean)plrPetDetails.get(1);

		return SUCCESS;
	}

	public DrawPwtApproveRequestNPlrBean getPlrPwtBean() {
		return plrPwtBean;
	}

	public double getPwtAmount() {
		return pwtAmount;
	}

	public List<DrawPwtApproveRequestNPlrBean> getPwtPayDetailsList() {
		return pwtPayDetailsList;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public String getRequestId() {
		return requestId;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public String getStatus() {
		return status;
	}

	public int getTaskId() {
		return taskId;
	}

	public double getTaxAmt() {
		return taxAmt;
	}

	public String getTicketNbr() {
		return ticketNbr;
	}

	public String payPendingPwts() throws LMSException {

		RetPwtPayProcessHelper processHelper = new RetPwtPayProcessHelper();
		HttpSession session = getRequest().getSession();
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		// int payByOrgId=userBean.getUserOrgId();
		// int payByUserId=userBean.getUserId();
		// String payByOrgName=userBean.getOrgName();
		String rootPath = (String) session.getAttribute("ROOT_PATH");
		String autoGeneratedReceiptNo = processHelper.payPendingPwt(taskId,
				pwtAmount, taxAmt, netAmt, partyId, partyType, ticketNbr,
				drawId, panelId, gameId, userBean, paymentType, chqNbr,
				draweeBank, chequeDate, issuiningParty, gameNbr, rootPath);
		session.setAttribute("autoGeneratedReceiptNo", autoGeneratedReceiptNo);
		return SUCCESS;
	}

	public void setChequeDate(String chequeDate) {
		this.chequeDate = chequeDate;
	}

	public void setChqNbr(String chqNbr) {
		this.chqNbr = chqNbr;
	}

	public void setDenyPwtStatus(String denyPwtStatus) {
		this.denyPwtStatus = denyPwtStatus;
	}

	public void setDraweeBank(String draweeBank) {
		this.draweeBank = draweeBank;
	}

	public void setDrawId(int drawId) {
		this.drawId = drawId;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public void setGameNbr(int gameNbr) {
		this.gameNbr = gameNbr;
	}

	public void setIssuiningParty(String issuiningParty) {
		this.issuiningParty = issuiningParty;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setNetAmt(double netAmt) {
		this.netAmt = netAmt;
	}

	public void setPanelId(String panelId) {
		this.panelId = panelId;
	}

	public void setPartyId(int partyId) {
		this.partyId = partyId;
	}

	public void setPartyType(String partyType) {
		this.partyType = partyType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public void setPlrPwtBean(DrawPwtApproveRequestNPlrBean plrPwtBean) {
		this.plrPwtBean = plrPwtBean;
	}

	public void setPwtAmount(double pwtAmount) {
		this.pwtAmount = pwtAmount;
	}

	public void setPwtPayDetailsList(
			List<DrawPwtApproveRequestNPlrBean> pwtPayDetailsList) {
		this.pwtPayDetailsList = pwtPayDetailsList;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public void setTaxAmt(double taxAmt) {
		this.taxAmt = taxAmt;
	}

	public void setTicketNbr(String ticketNbr) {
		this.ticketNbr = ticketNbr;
	}

}