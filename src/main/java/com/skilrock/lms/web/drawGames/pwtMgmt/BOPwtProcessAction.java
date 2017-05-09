package com.skilrock.lms.web.drawGames.pwtMgmt;

import java.util.List;
import java.util.Map;

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
import com.skilrock.lms.beans.DrawPwtApproveRequestNPlrBean;
import com.skilrock.lms.beans.PlayerBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.CommonValidation;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.coreEngine.drawGames.pwtMgmt.BOPwtProcessHelper;
import com.skilrock.lms.dge.beans.DrawIdBean;
import com.skilrock.lms.dge.beans.MainPWTDrawBean;
import com.skilrock.lms.dge.beans.PWTDrawBean;
import com.skilrock.lms.web.drawGames.common.Util;

public class BOPwtProcessAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int agentOrgId;
	private String bankAccNbr;
	private String bankBranch;

	private String bankName;
	private String chequeDate;
	private String chqNbr;
	private String country;
	private String denyPwtStatus;

	private String draweeBank;

	// private String virnNbr;
	private int drawId;

	private String emailId;
	// private int approvedById;
	private String firstName;

	private int gameId;
	private String gameIdNbrName;
	private int gameNbr;

	private String gameType;
	private String generatedReceiptNumber;

	private String idNumber;
	private String idType;
	private String issuiningParty;
	private String lastName;
	private String locationCity;
	Log logger = LogFactory.getLog(BOPwtProcessAction.class);
	private double netAmt;
	private String panelId;
	private int partyId;
	private String partyType;
	private String paymentPendingAt;
	private String paymentType;
	private String phone;
	private int playerId;

	private String playerType;
	private String plrAddr1;
	private String plrAddr2;
	private String plrAlreadyReg;
	private String plrCity;
	private String plrCountry;
	private String plrPin;
	DrawPwtApproveRequestNPlrBean plrPwtBean;
	private double pwtAmount;
	private Map pwtAppMap;
	List<DrawPwtApproveRequestNPlrBean> pwtPayDetailsList;
	List<DrawPwtApproveRequestNPlrBean> pwtReqDetailsList;
	HttpServletRequest request;
	private int requestedById;
	private String requesterType;
	private String requestId;
	HttpServletResponse response;
	private int retOrgId;
	private String state;
	private String status;

	private int taskId;
	private double taxAmt;
	private String ticketNbr;
	
	private String ticketNbrDraw;
	private String city;
	MainPWTDrawBean mainPwtBean;


	public MainPWTDrawBean getMainPwtBean() {
		return mainPwtBean;
	}

	public void setMainPwtBean(MainPWTDrawBean mainPwtBean) {
		this.mainPwtBean = mainPwtBean;
	}

	public String approvePendingPwtsByMas() throws LMSException {
		String pwtAmtForMasterApproval = (String) ServletActionContext
				.getServletContext().getAttribute("PWT_APPROVAL_LIMIT");
		BOPwtProcessHelper processHelper = new BOPwtProcessHelper();
		HttpSession session = getRequest().getSession();
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		int approvedByOrgId = userBean.getUserOrgId();
		int approvedByUserId = userBean.getUserId();
		String remarks = processHelper.approvePendingPwtsByMas(taskId,
				pwtAmount, requestedById, requesterType, approvedByOrgId,
				approvedByUserId, gameId, gameNbr, drawId, panelId, ticketNbr,
				pwtAmtForMasterApproval);
		session.setAttribute("REMARKS", remarks);
		return SUCCESS;
	}

	public String denyRequestedPwts() throws LMSException {
		HttpSession session = getRequest().getSession();
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		BOPwtProcessHelper processHelper = new BOPwtProcessHelper();
		boolean isDenied = processHelper.denyPWTProcess(taskId, drawId, gameId,
				ticketNbr, denyPwtStatus, gameNbr, userBean.getUserId(),
				userBean.getUserOrgId(), panelId);
		if (isDenied) {
			return SUCCESS;
		} else {
			return ERROR;
		}
	}

	public int getAgentOrgId() {
		return agentOrgId;
	}

	// added by amit on 04/08/10 for anonymous player

	public String getBankAccNbr() {
		return bankAccNbr;
	}

	public String getBankBranch() {
		return bankBranch;
	}

	public String getBankName() {
		return bankName;
	}

	public String getChequeDate() {
		return chequeDate;
	}

	public String getChqNbr() {
		return chqNbr;
	}

	public String getCountry() {
		return country;
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

	public String getEmailId() {
		return emailId;
	}

	public String getFirstName() {
		return firstName;
	}

	public int getGameId() {
		return gameId;
	}

	public String getGameIdNbrName() {
		return gameIdNbrName;
	}

	public int getGameNbr() {
		return gameNbr;
	}

	public String getGameType() {
		return gameType;
	}

	public String getGeneratedReceiptNumber() {
		return generatedReceiptNumber;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public String getIdType() {
		return idType;
	}

	public String getIssuiningParty() {
		return issuiningParty;
	}

	public String getLastName() {
		return lastName;
	}

	public String getLocationCity() {
		return locationCity;
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

	public String getPaymentPendingAt() {
		return paymentPendingAt;
	}
	
	public String getPaymentType() {
		return paymentType;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPendingPwtToPay() throws LMSException {
		HttpSession session = getRequest().getSession();
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		int payByOrgId = 0;
		BOPwtProcessHelper processHelper = new BOPwtProcessHelper();

		// pwtPayDetailsList=processHelper.getRequestsPwtsToPay(requestId,agentOrgId,firstName,lastName,status,payByOrgId,partyType);

		if ("BO".equals(paymentPendingAt)) {
			payByOrgId = userBean.getUserOrgId();
		} else if ("AGENT".equals(paymentPendingAt)) {
			payByOrgId = agentOrgId;
		}
		pwtPayDetailsList = processHelper.getRequestsPwtsToPay(requestId,
				agentOrgId, firstName, lastName, status, payByOrgId,
				paymentPendingAt, partyType);

		return SUCCESS;
	}

	public String getPendingPwtToPayDetails() throws LMSException {
		ServletContext sc = ServletActionContext.getServletContext();
		String raffleTktType = (String)sc.getAttribute("raffle_ticket_type");
		BOPwtProcessHelper processHelper = new BOPwtProcessHelper();
		plrPwtBean = processHelper.getRequestDetails(taskId, partyType,raffleTktType);
		// plrBean=(PlayerBean)plrPetDetails.get(0);
		// plrPwtBean=(PwtApproveRequestNPlrBean)plrPetDetails.get(1);
		return SUCCESS;
	}

	public String getPhone() {
		return phone;
	}

	public int getPlayerId() {
		return playerId;
	}

	public String getPlayerType() {
		return playerType;
	}

	public String getPlrAddr1() {
		return plrAddr1;
	}

	public String getPlrAddr2() {
		return plrAddr2;
	}

	public String getPlrAlreadyReg() {
		return plrAlreadyReg;
	}

	public String getPlrCity() {
		return plrCity;
	}

	public String getPlrCountry() {
		return plrCountry;
	}

	public String getPlrPin() {
		return plrPin;
	}

	public DrawPwtApproveRequestNPlrBean getPlrPwtBean() {
		return plrPwtBean;
	}

	public double getPwtAmount() {
		return pwtAmount;
	}

	public Map getPwtAppMap() {
		return pwtAppMap;
	}

	public List<DrawPwtApproveRequestNPlrBean> getPwtPayDetailsList() {
		return pwtPayDetailsList;
	}

	public List<DrawPwtApproveRequestNPlrBean> getPwtReqDetailsList() {
		return pwtReqDetailsList;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public int getRequestedById() {
		return requestedById;
	}

	public String getRequesterType() {
		return requesterType;
	}

	public String getRequestId() {
		return requestId;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public int getRetOrgId() {
		return retOrgId;
	}

	public String getState() {
		return state;
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

	public String getTicketNbrDraw() {
		return ticketNbrDraw;
	}

	public String getUnapprovedPwtForMas() throws LMSException {

		HttpSession session = getRequest().getSession();
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		int approvalByOrgId = userBean.getUserOrgId();

		if (retOrgId > 0) {
			requestedById = retOrgId;
		} else {
			requestedById = agentOrgId;
		}

		logger.debug("requested by id is in draw game  " + requestedById
				+ ":retorg id :" + retOrgId);

		BOPwtProcessHelper processHelper = new BOPwtProcessHelper();
		pwtReqDetailsList = processHelper.getRequestedPwts(requestId,
				requestedById, requesterType, firstName, lastName, status,
				approvalByOrgId, partyType);
		logger.debug("before success");

		return SUCCESS;
	}

	public String getUnapprovedPwtForMasDetails() throws LMSException {
		ServletContext sc = ServletActionContext.getServletContext();
		String raffleTktType = (String)sc.getAttribute("raffle_ticket_type");
		BOPwtProcessHelper processHelper = new BOPwtProcessHelper();
		plrPwtBean = processHelper.getRequestDetails(taskId, partyType,raffleTktType);
		// plrBean=(PlayerBean)plrPetDetails.get(0);
		// plrPwtBean=(PwtApproveRequestNPlrBean)plrPetDetails.get(1);
		return SUCCESS;
	}

	public String getUnapprovePwts() throws LMSException {
		HttpSession session = getRequest().getSession();
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		int approvalByOrgId = userBean.getUserOrgId();

		if (retOrgId > 0) {
			requestedById = retOrgId;
		} else {
			requestedById = agentOrgId;
		}

		logger.debug("requested by id is " + requestedById + ":retorg id :"
				+ retOrgId);

		BOPwtProcessHelper processHelper = new BOPwtProcessHelper();
		pwtReqDetailsList = processHelper.getRequestedPwts(requestId,
				requestedById, requesterType, firstName, lastName, status,
				approvalByOrgId, partyType);
		logger.debug("before success");
		return SUCCESS;

	}

	public String payPendingPwts() throws LMSException {
		BOPwtProcessHelper processHelper = new BOPwtProcessHelper();
		HttpSession session = getRequest().getSession();
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		int payByOrgId = userBean.getUserOrgId();
		int payByUserId = userBean.getUserId();
		String payByOrgName = userBean.getOrgName();
		String rootPath = (String) session.getAttribute("ROOT_PATH");
		generatedReceiptNumber = processHelper.payPendingPwt(taskId, pwtAmount,
				taxAmt, netAmt, partyId, partyType, ticketNbr, drawId, panelId,
				gameId, payByOrgId, payByUserId, payByOrgName, paymentType,
				chqNbr, draweeBank, chequeDate, issuiningParty, gameNbr,
				rootPath);
		session.setAttribute("generatedReceiptNumber", generatedReceiptNumber);
		return SUCCESS;
	}

	/**
	 * Player registration process
	 * 
	 * @return
	 * @throws LMSException
	 */

	@SuppressWarnings("unchecked")
	public String plrRegistrationAndApprovalReq() throws LMSException {
		PlayerBean plrBean = null;
		logger.debug("plrAlreadyReg = " + plrAlreadyReg + "  , playerType = "
				+ playerType + "  , playerId = " + playerId);
		if(plrCity==null && city !=null)
			plrCity=city;
		if (!"YES".equalsIgnoreCase(plrAlreadyReg.trim())
				&& "player".equalsIgnoreCase(playerType.trim())) {
			plrBean = new PlayerBean();
			plrBean.setFirstName(firstName);
			plrBean.setLastName(lastName);
			plrBean.setIdType(idType);
			plrBean.setIdNumber(idNumber);
			plrBean.setEmailId(emailId);
			plrBean.setPhone(phone);
			plrBean.setPlrAddr1(plrAddr1);
			plrBean.setPlrAddr2(plrAddr2);
			plrBean.setPlrState(state);
			plrBean.setPlrCity(plrCity);
			plrBean.setPlrCountry(plrCountry);
			plrBean.setPlrPin(Long.parseLong(plrPin));
			plrBean.setBankName(bankName);
			plrBean.setBankBranch(bankBranch);
			plrBean.setLocationCity(locationCity);
			plrBean.setBankAccNbr(bankAccNbr);
			logger.debug("Inside player registration 11111 & plrBean is "
					+ plrBean);
		}

		HttpSession session = request.getSession();
		//PWTDrawBean pwtDrawBean = (PWTDrawBean) session.getAttribute("PWT_RES");
	    MainPWTDrawBean pwtDrawBean = (MainPWTDrawBean) session.getAttribute("PWT_RES");
		String rootPath = (String) session.getAttribute("ROOT_PATH");
		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		if (userInfoBean == null) {
			throw new LMSException("userInfoBean = " + userInfoBean);
		}
		// player registration and approval process
		BOPwtProcessHelper helper = new BOPwtProcessHelper();
		logger.debug("root path is " + rootPath);

		this.pwtAppMap = helper.plrRegistrationAndApproval(userInfoBean,
				pwtDrawBean, playerType, playerId, plrBean, rootPath, false);

		if (plrBean == null) {
			plrBean = (PlayerBean) session.getAttribute("playerBean");
		}
		pwtAppMap.put("plrBean", plrBean);
		session.setAttribute("plrPwtAppDetMap", pwtAppMap);
		session.setAttribute("PWT_RES", pwtAppMap.get("PWT_RES_BEAN"));
		mainPwtBean = pwtDrawBean;
		return SUCCESS;
	}

	public String registerAnyPlayer() throws LMSException {
		PlayerBean plrBean = null;
		boolean isAnonymous = true;
		HttpSession session = request.getSession();
		//PWTDrawBean pwtDrawBean = (PWTDrawBean) session.getAttribute("PWT_RES");
		MainPWTDrawBean pwtDrawBean = (MainPWTDrawBean) session.getAttribute("PWT_RES");
		String rootPath = (String) session.getAttribute("ROOT_PATH");
		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		if (userInfoBean == null) {
			throw new LMSException("userInfoBean = " + userInfoBean);
		}
		// player registration and approval process
		BOPwtProcessHelper helper = new BOPwtProcessHelper();
		logger.debug("root path is " + rootPath);
		this.pwtAppMap = helper.plrRegistrationAndApproval(userInfoBean,
				pwtDrawBean, playerType, playerId, plrBean, rootPath,
				isAnonymous);
		session.setAttribute("plrPwtAppDetMap", pwtAppMap);
		session.setAttribute("PWT_RES", pwtAppMap.get("PWT_RES_BEAN"));
		mainPwtBean = pwtDrawBean;
		return SUCCESS;
	}

	public void setAgentOrgId(int agentOrgId) {
		this.agentOrgId = agentOrgId;
	}

	public void setBankAccNbr(String bankAccNbr) {
		this.bankAccNbr = bankAccNbr;
	}

	public void setBankBranch(String bankBranch) {
		this.bankBranch = bankBranch;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public void setChequeDate(String chequeDate) {
		this.chequeDate = chequeDate;
	}

	public void setChqNbr(String chqNbr) {
		this.chqNbr = chqNbr;
	}

	public void setCountry(String country) {
		this.country = country;
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

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public void setGameIdNbrName(String gameIdNbrName) {
		this.gameIdNbrName = gameIdNbrName;
	}

	public void setGameNbr(int gameNbr) {
		this.gameNbr = gameNbr;
	}

	public void setGameType(String gameType) {
		this.gameType = gameType;
	}

	public void setGeneratedReceiptNumber(String generatedReceiptNumber) {
		this.generatedReceiptNumber = generatedReceiptNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	public void setIssuiningParty(String issuiningParty) {
		this.issuiningParty = issuiningParty;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setLocationCity(String locationCity) {
		this.locationCity = locationCity;
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

	public void setPaymentPendingAt(String paymentPendingAt) {
		this.paymentPendingAt = paymentPendingAt;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public void setPlayerType(String playerType) {
		this.playerType = playerType;
	}

	public void setPlrAddr1(String plrAddr1) {
		this.plrAddr1 = plrAddr1;
	}

	public void setPlrAddr2(String plrAddr2) {
		this.plrAddr2 = plrAddr2;
	}

	public void setPlrAlreadyReg(String plrAlreadyReg) {
		this.plrAlreadyReg = plrAlreadyReg;
	}

	public void setPlrCity(String plrCity) {
		this.plrCity = plrCity;
	}

	public void setPlrCountry(String plrCountry) {
		this.plrCountry = plrCountry;
	}

	public void setPlrPin(String plrPin) {
		this.plrPin = plrPin;
	}

	public void setPlrPwtBean(DrawPwtApproveRequestNPlrBean plrPwtBean) {
		this.plrPwtBean = plrPwtBean;
	}

	public void setPwtAmount(double pwtAmount) {
		this.pwtAmount = pwtAmount;
	}

	public void setPwtAppMap(Map pwtAppMap) {
		this.pwtAppMap = pwtAppMap;
	}

	public void setPwtPayDetailsList(
			List<DrawPwtApproveRequestNPlrBean> pwtPayDetailsList) {
		this.pwtPayDetailsList = pwtPayDetailsList;
	}

	public void setPwtReqDetailsList(
			List<DrawPwtApproveRequestNPlrBean> pwtReqDetailsList) {
		this.pwtReqDetailsList = pwtReqDetailsList;
	}

	public void setRequestedById(int requestedById) {
		this.requestedById = requestedById;
	}

	public void setRequesterType(String requesterType) {
		this.requesterType = requesterType;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public void setRetOrgId(int retOrgId) {
		this.retOrgId = retOrgId;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public void setState(String state) {
		this.state = state;
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

	public void setTicketNbrDraw(String ticketNbrDraw) {
		this.ticketNbrDraw = ticketNbrDraw;
	}

	public String verifyDirectPlrTicket() throws LMSException {

		try {
			logger.debug("verifyDirectPlrTicket():88888");
			String pwtAmtForMasterApproval = (String) ServletActionContext
					.getServletContext().getAttribute("PWT_APPROVAL_LIMIT");
			String highPrizeScheme = (String) ServletActionContext
					.getServletContext().getAttribute(
							"DRAW_GAME_HIGH_PRIZE_SCHEME");
			PWTDrawBean pwtDarwBean = new PWTDrawBean();
			pwtDarwBean.setTicketNo(ticketNbrDraw);

			String returnType = "input";
			HttpSession session = getRequest().getSession();
			UserInfoBean userInfoBean = (UserInfoBean) session
					.getAttribute("USER_INFO");
			// if HIGH HIGH_PRIZE_CRITERIA or HIGH_PRIZE_AMT is NULL
			if (pwtAmtForMasterApproval == null || userInfoBean == null) {
				pwtDarwBean.setStatus("ERROR");
				return returnType;
			}
			pwtDarwBean.setPartyId(userInfoBean.getUserOrgId());
			pwtDarwBean.setPartyType(userInfoBean.getUserType());
			BOPwtProcessHelper helper = new BOPwtProcessHelper();
			returnType = helper.verifyAndSaveTicketDirPlr1(userInfoBean,
					pwtAmtForMasterApproval, pwtDarwBean, highPrizeScheme);
			session.setAttribute("pwtDrawBean", pwtDarwBean);// set for
			// player
			// registration

			logger.debug(" return type ********************* " + returnType);
			return returnType;

		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(e);
		}
	}
/*
	// this method is added by amit on 20/07/10
	public String verifyDirectPlrTicketNo() throws LMSException {
		HttpSession session = request.getSession();
		ServletContext sc = ServletActionContext.getServletContext();
		String pwtAmtForMasterApproval = (String) sc
				.getAttribute("PWT_APPROVAL_LIMIT");
		String highPrizeScheme = (String) sc
				.getAttribute("DRAW_GAME_HIGH_PRIZE_SCHEME");
		String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		PWTDrawBean drawScheduleBean = new PWTDrawBean();

		drawScheduleBean.setTicketNo(ticketNbr.trim());
		drawScheduleBean.setPartyId(userInfoBean.getUserOrgId());
		drawScheduleBean.setUserId(userInfoBean.getUserId());
		drawScheduleBean.setPartyType(userInfoBean.getUserType());
		drawScheduleBean.setRefMerchantId(refMerchantId);
        
		logger.debug("*****ticketNbr***" + ticketNbr);
		BOPwtProcessHelper helper = new BOPwtProcessHelper();
		PWTDrawBean pwtWinningBean = helper.verifyAndSaveTicketNoDirPlr(
				drawScheduleBean, userInfoBean, pwtAmtForMasterApproval,
				highPrizeScheme);
		boolean isMasAppReq = true;
		pwtWinningBean.setGameDispName(Util.getGameDisplayName(pwtWinningBean
				.getGameNo()));
		session.setAttribute("PWT_RES", pwtWinningBean);
		if (pwtWinningBean.isValid() && pwtWinningBean.isHighPrize()) {
			session.setAttribute("PWT_AMT", pwtWinningBean.getTotalAmount());
			if ("MAS_APP_REQ".equalsIgnoreCase(pwtWinningBean.getPwtStatus())) {
				isMasAppReq = false;
			}
			session.setAttribute("isMasAppReq", isMasAppReq);
			return "registration";
		} else if (pwtWinningBean.isValid() && pwtWinningBean.isWinTkt()) {
			String status = registerAnyPlayer();// register the player as
			// anonymous
			if (status.equals(SUCCESS)) {
				return "paySuccess";
			} else {
				return "error";
			}
		} else {
			return SUCCESS;
		}

	}
*/	
	public String verifyDirectPlrTicketNo() throws LMSException {

		
		HttpSession session = request.getSession();
		mainPwtBean = new MainPWTDrawBean();
		//boolean isAuth=false;
		if (ticketNbr != null && CommonValidation.isNumericWithoutDot(ticketNbr, false)){
			mainPwtBean.setTicketNo(ticketNbr.trim());
		}
		else{
			addActionError("Please Enter valid ticket number.");
			return "error";
		}
		

		/*isUnauth=Util.isBoAuthorizedToClaimTicket(ticketNbr);
		session.setAttribute("isUnAuthorized", isUnauth);
		if(isUnauth){
			return SUCCESS;
		}*/

		ServletContext sc = ServletActionContext.getServletContext();
		String pwtAmtForMasterApproval = (String) sc.getAttribute("PWT_APPROVAL_LIMIT");
		String highPrizeScheme = (String) sc
				.getAttribute("DRAW_GAME_HIGH_PRIZE_SCHEME");
		String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
		String highPrizeAmt = (String) LMSUtility.sc.getAttribute("HIGH_PRIZE_AMT");
		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		logger.debug("*****ticketNbr***" + ticketNbr);
		BOPwtProcessHelper helper = new BOPwtProcessHelper();
		MainPWTDrawBean pwtWinningBean = helper.newMethod(mainPwtBean,
				userInfoBean, pwtAmtForMasterApproval, highPrizeScheme,
				refMerchantId,highPrizeAmt);

		boolean isMasAppReq = true;
		session.setAttribute("PWT_RES", pwtWinningBean);
		if(pwtWinningBean.getWinningBeanList() != null) {
			for(PWTDrawBean pwtDrawBean : pwtWinningBean.getWinningBeanList()) {
				if(pwtDrawBean.getDrawWinList() != null) {
					for(DrawIdBean drawIdBean : pwtDrawBean.getDrawWinList()) {
						if(drawIdBean.getRankId() == 4 && "TwelveByTwentyFour".equals(Util.getGameName(pwtWinningBean.getGameId()))) {
							return "rank4";
						}
					}
				}
			}
		}
		
		if (pwtWinningBean.isValid() && pwtWinningBean.isHighPrize()) {
			session.setAttribute("PWT_AMT", pwtWinningBean.getTotlticketAmount());
			if ("MAS_APP_REQ".equalsIgnoreCase(pwtWinningBean.getPwtStatus())) {
				isMasAppReq = false;
			}
			session.setAttribute("isMasAppReq", isMasAppReq);
			return "registration";
		} else if (pwtWinningBean.isValid() && pwtWinningBean.isWinTkt()) {
			String status = registerAnyPlayer();// register the player as
			// anonymous
			if (status.equals(SUCCESS)) {
				return "paySuccess";
			} else {
				return "error";
			}
		} else {
			return SUCCESS;
		}

	}

}