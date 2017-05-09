package com.skilrock.lms.web.scratchService.pwtMgmt.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.PlayerBean;
import com.skilrock.lms.beans.PwtApproveRequestNPlrBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.scratchService.pwtMgmt.common.AgentPwtProcessHelper;
import com.skilrock.lms.coreEngine.scratchService.pwtMgmt.common.PlayerVerifyHelperForApp;

public class AgentPwtProcessAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {

	static Log logger = LogFactory.getLog(AgentPwtProcessAction.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int approvedById;
	private String bankAccNbr;
	private String bankBranch;
	private String bankName;
	private String chequeDate;
	private String chqNbr;
	private String country;
	private String denyPwtStatus;
	private String draweeBank;
	private String emailId;
	private String firstName;
	private int gameId;
	private String gameIdNbrName;
	private int gameNbr;
	private String idNumber;
	private String idType;
	private String issuiningParty;
	private String lastName;
	private String locationCity;
	private double netAmt;
	private int partyId;
	private String partyType;
	private String paymentType;
	private String phone;
	private int playerId;
	private String playerType;
	private String plrAddr1;

	private String plrAddr2;

	private String plrAlreadyReg;
	PlayerBean plrBean;
	private String city;
	private String plrCountry;

	private String plrPin;

	PwtApproveRequestNPlrBean plrPwtBean;

	private double pwtAmount;

	private Map pwtAppMap;

	/**
	 * This Method is used to verify PWT Ticket and VIRN Entries
	 * 
	 * @throws LMSException
	 * @throws
	 */
	Map<String, Object> pwtErrorMap;

	List<PwtApproveRequestNPlrBean> pwtPayDetailsList;

	List<PwtApproveRequestNPlrBean> pwtReqDetailsList;

	private int ratailerId;

	HttpServletRequest request;

	private int requestedById;
	private String requestId;
	HttpServletResponse response;
	private int retOrgId;
	private String state;
	// private String requestDate;
	private String status;
	private int taskId;
	private double taxAmt;
	private String ticketNbr;
	private String virnNbr;

	public void approvePendingPwts() throws LMSException {
		PrintWriter out;
		try {
			out = getResponse().getWriter();
		} catch (IOException e) {
			e.printStackTrace();
			throw new LMSException("Io exception", e);
		}
		response.setContentType("text/html");
		AgentPwtProcessHelper processHelper = new AgentPwtProcessHelper();
		HttpSession session = getRequest().getSession();
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		int agentOrgId = userBean.getUserOrgId();
		int agtUserId = userBean.getUserId();
		int boOrgId = userBean.getParentOrgId();
		String channel = "RETAIL";
		String interFaceType = (String) request.getAttribute("interfaceType");
		String remarks = processHelper.approvePendingPwts(taskId, pwtAmount,
				requestedById, agentOrgId, agtUserId, gameNbr, virnNbr,
				ticketNbr, gameId, boOrgId, channel, interFaceType);
		// boolean
		// isApproved=processHelper.approvePendingPwts(1,100,10,agentOrgId,agtUserId);
		out.print(remarks);
		// return SUCCESS;
	}

	public String denyRequestedPwts() throws LMSException {
		HttpSession session = getRequest().getSession();
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		userBean.setChannel("RETAIL");
		userBean.setInterfaceType((String) request
				.getAttribute("interfaceType"));
		AgentPwtProcessHelper processHelper = new AgentPwtProcessHelper();
		if (processHelper.denyPWTProcess(taskId, virnNbr, gameId, ticketNbr,
				denyPwtStatus, gameNbr, userBean)) {
			return SUCCESS;
		} else {
			throw new LMSException("Request for payment not denied");
		}
	}

	public void denyRequestedPwtsAjax() throws LMSException {

		PrintWriter out = null;
		try {
			out = getResponse().getWriter();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HttpSession session = getRequest().getSession();
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		userBean.setChannel("RETAIL");
		userBean.setInterfaceType((String) request
				.getAttribute("interfaceType"));
		AgentPwtProcessHelper processHelper = new AgentPwtProcessHelper();
		out.print(processHelper.denyPWTProcess(taskId, virnNbr, gameId,
				ticketNbr, denyPwtStatus, gameNbr, userBean));

	}

	public int getApprovedById() {
		return approvedById;
	}

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
		AgentPwtProcessHelper processHelper = new AgentPwtProcessHelper();
		pwtPayDetailsList = processHelper.getRequestsPwtsToPay(requestId,
				retOrgId, firstName, lastName, status, payByOrgId, partyType);

		return SUCCESS;
	}

	public String getPendingPwtToPayDetails() throws LMSException {
		AgentPwtProcessHelper processHelper = new AgentPwtProcessHelper();
		plrPwtBean = processHelper.getPendingPwtDetails(taskId, partyType);
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

	public PlayerBean getPlrBean() {
		return plrBean;
	}

	
	public String getPlrCountry() {
		return plrCountry;
	}

	public String getPlrPin() {
		return plrPin;
	}

	public PwtApproveRequestNPlrBean getPlrPwtBean() {
		return plrPwtBean;
	}

	public double getPwtAmount() {
		return pwtAmount;
	}

	public Map getPwtAppMap() {
		return pwtAppMap;
	}

	public Map<String, Object> getPwtErrorMap() {
		return pwtErrorMap;
	}

	public List<PwtApproveRequestNPlrBean> getPwtPayDetailsList() {
		return pwtPayDetailsList;
	}

	public List<PwtApproveRequestNPlrBean> getPwtReqDetailsList() {
		return pwtReqDetailsList;
	}

	public int getRatailerId() {
		return ratailerId;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public int getRequestedById() {
		return requestedById;
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

	/**
	 * This method is used to get the upapproved pwts for the login agent on the
	 * basis of search criteria
	 * 
	 * @return String
	 * @throws LMSException
	 */
	public String getUnapprovePwts() throws LMSException {
		HttpSession session = getRequest().getSession();
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		int approvalByOrgId = userBean.getUserOrgId();
		AgentPwtProcessHelper processHelper = new AgentPwtProcessHelper();
		pwtReqDetailsList = processHelper.getUnapprovePwts(requestId, retOrgId,
				approvedById, firstName, lastName, status, approvalByOrgId);

		return SUCCESS;

	}

	public String getVirnNbr() {
		return virnNbr;
	}

	public String payPendingPwts() throws LMSException {

		AgentPwtProcessHelper processHelper = new AgentPwtProcessHelper();
		HttpServletRequest request =  ServletActionContext.getRequest();
		request.setAttribute("code", "SE");
		request.setAttribute("interfaceType", "WEB");
		HttpSession session = getRequest().getSession();
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		int payByOrgId = userBean.getUserOrgId();
		int payByUserId = userBean.getUserId();
		String payByOrgName = userBean.getOrgName();
		String rootPath = (String) session.getAttribute("ROOT_PATH");
		String autoGeneratedReceiptNo = processHelper.payPendingPwt(taskId,
				pwtAmount, taxAmt, netAmt, partyId, partyType, ticketNbr,
				virnNbr, gameId, payByOrgId, payByUserId, paymentType, chqNbr,
				draweeBank, chequeDate, issuiningParty, gameNbr, payByOrgName,
				rootPath);
		session.setAttribute("autoGeneratedReceiptNo", autoGeneratedReceiptNo);
		return SUCCESS;
	}

	/**
	 * Player registration process
	 * 
	 * @return
	 * @throws LMSException
	 */
	public String plrRegistrationAndApprovalReq() throws LMSException {
		PlayerBean plrBean = null;
		/*logger.debug("plrAlreadyReg = " + plrAlreadyReg + "  , playerType = "
				+ playerType + "  , playerId = " + playerId);*/
		Map<String, Object> playerBeanMap = new PlayerVerifyHelperForApp().searchPlayer(firstName, lastName, idNumber, idType);
		plrBean = (PlayerBean) playerBeanMap.get("plrBean");
		plrAlreadyReg = "NO";
		if (plrBean != null) {
			plrAlreadyReg = "YES";
		}
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
			plrBean.setPlrCity(city);
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
		Map pwtDetails = (Map) session.getAttribute("pwtDetailMap");
		String rootPath = (String) session.getAttribute("ROOT_PATH");
		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");

		userInfoBean.setChannel("RETAIL");
		userInfoBean.setInterfaceType((String) request
				.getAttribute("interfaceType"));

		// if HIGH HIGH_PRIZE_CRITERIA or HIGH_PRIZE_AMT is NULL
		if (userInfoBean == null) {
			throw new LMSException("userInfoBean = " + userInfoBean);
		}
		if(playerType.contains("anonymous")){
			playerId = 1;
		}
		playerType = "player";
		
		// player registration and approval process
		AgentPwtProcessHelper helper = new AgentPwtProcessHelper();
		this.pwtAppMap = helper.plrRegistrationAndApproval(userInfoBean,
				pwtDetails, playerType, playerId, plrBean, rootPath);

		if (plrBean == null) {
			plrBean = (PlayerBean) session.getAttribute("playerBean");
			pwtAppMap.put("plrBean", plrBean);
		}
		session.setAttribute("plrPwtAppDetMap", pwtAppMap);

		return SUCCESS;
	}

	public void setApprovedById(int approvedById) {
		this.approvedById = approvedById;
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

	public void setPartyId(int partyId) {
		this.partyId = partyId;
	}

	public void setPartyType(String partyType) {
		this.partyType = partyType;
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

	public void setPlrBean(PlayerBean plrBean) {
		this.plrBean = plrBean;
	}
	
	public void setPlrCountry(String plrCountry) {
		this.plrCountry = plrCountry;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setPlrPin(String plrPin) {
		this.plrPin = plrPin;
	}

	public void setPlrPwtBean(PwtApproveRequestNPlrBean plrPwtBean) {
		this.plrPwtBean = plrPwtBean;
	}

	public void setPwtAmount(double pwtAmount) {
		this.pwtAmount = pwtAmount;
	}

	public void setPwtAppMap(Map pwtAppMap) {
		this.pwtAppMap = pwtAppMap;
	}

	public void setPwtErrorMap(Map<String, Object> pwtErrorMap) {
		this.pwtErrorMap = pwtErrorMap;
	}

	public void setPwtPayDetailsList(
			List<PwtApproveRequestNPlrBean> pwtPayDetailsList) {
		this.pwtPayDetailsList = pwtPayDetailsList;
	}

	public void setPwtReqDetailsList(
			List<PwtApproveRequestNPlrBean> pwtReqDetailsList) {
		this.pwtReqDetailsList = pwtReqDetailsList;
	}

	public void setRatailerId(int ratailerId) {
		this.ratailerId = ratailerId;
	}

	public void setRequestedById(int requestedById) {
		this.requestedById = requestedById;
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

	public void setVirnNbr(String virnNbr) {
		this.virnNbr = virnNbr;
	}

	public String verifyDirectPlrTicketNVirn() throws LMSException {
		try {

			// String highPrizeCriteria =
			// (String)ServletActionContext.getServletContext().getAttribute("HIGH_PRIZE_CRITERIA");
			// String highPrizeAmt =
			// (String)ServletActionContext.getServletContext().getAttribute("HIGH_PRIZE_AMT");
			logger.debug("g= " + gameIdNbrName + " t = " + ticketNbr
					+ " virn = " + virnNbr);
			HttpSession session = getRequest().getSession();
			UserInfoBean userInfoBean = (UserInfoBean) session
					.getAttribute("USER_INFO");
			// if HIGH HIGH_PRIZE_CRITERIA or HIGH_PRIZE_AMT is NULL
			if (userInfoBean == null) {
				throw new LMSException(" userInfoBean = " + userInfoBean);
			}

			String gameArr[] = gameIdNbrName.split("-"); //
			AgentPwtProcessHelper helper = new AgentPwtProcessHelper();
			Map<String, Object> pwtDetailMap = helper.verifyAndSaveTicketNVirn(
					ticketNbr, virnNbr, Integer.parseInt(gameArr[0]),
					gameArr[1], gameArr[2], userInfoBean);
			session.setAttribute("pwtDetailMap", pwtDetailMap);
			pwtErrorMap = pwtDetailMap;
			if (pwtDetailMap != null && pwtDetailMap.containsKey("returnType")) {
				String returnType = (String) pwtDetailMap.get("returnType");
				logger.debug("pwt type return = " + returnType);
				return returnType;
			}
			return INPUT;
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(e);
		}
	}

}