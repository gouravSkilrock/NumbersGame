package com.skilrock.lms.web.drawGames.pwtMgmt;

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
import com.skilrock.lms.beans.PlayerBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.CommonValidation;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.drawGames.pwtMgmt.AgtPWTProcessHelper;
import com.skilrock.lms.dge.beans.DrawIdBean;
import com.skilrock.lms.dge.beans.MainPWTDrawBean;
import com.skilrock.lms.dge.beans.PWTDrawBean;
import com.skilrock.lms.web.drawGames.common.Util;

public class AgentPwtProcessAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String bankAccNbr;
	private String bankBranch;
	private String bankName;

	private String emailId;
	private String firstName;
	private String idNumber;
	private String idType;
	private String lastName;
	private String locationCity;
	Log logger = LogFactory.getLog(AgentPwtProcessAction.class);
	private String phone;
	private int playerId;
	private String playerType;
	private String plrAddr1;
	private String plrAddr2;
	private String plrAlreadyReg;
	private String plrCity;
	private String plrCountry;
	private String plrPin;
	private Map pwtAppMap;
	HttpServletRequest request;
	HttpServletResponse response;
	private int retOrgId;
	private String state;
	private String ticketNbr;
	private String[] ticketNum;

	public String getBankAccNbr() {
		return bankAccNbr;
	}

	public String getBankBranch() {
		return bankBranch;
	}

	public String getBankName() {
		return bankName;
	}

	public String getEmailId() {
		return emailId;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public String getIdType() {
		return idType;
	}

	public String getLastName() {
		return lastName;
	}

	public String getLocationCity() {
		return locationCity;
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

	public Map getPwtAppMap() {
		return pwtAppMap;
	}

	public HttpServletRequest getRequest() {
		return request;
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

	public String getTicketNbr() {
		return ticketNbr;
	}

	public String[] getTicketNum() {
		return ticketNum;
	}

	public String plrRegistrationAndApprovalReq() throws LMSException {
		PlayerBean plrBean = null;
		logger.debug("plrAlreadyReg = " + plrAlreadyReg + "  , playerType = "
				+ playerType + "  , playerId = " + playerId);
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
		String highPrizeScheme = (String) ServletActionContext
				.getServletContext()
				.getAttribute("DRAW_GAME_HIGH_PRIZE_SCHEME");
		String rootPath = (String) session.getAttribute("ROOT_PATH");
		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		// if HIGH HIGH_PRIZE_CRITERIA or HIGH_PRIZE_AMT is NULL
		if (userInfoBean == null) {
			throw new LMSException("userInfoBean = " + userInfoBean);
		}
		logger.debug("highPrizeScheme  is " + highPrizeScheme);
		// player registration and approval process
		AgtPWTProcessHelper helper = new AgtPWTProcessHelper();
		logger.debug("root path is " + rootPath);

		this.pwtAppMap = helper.plrRegistrationAndApproval(userInfoBean,
				pwtDrawBean, playerType, playerId, plrBean, rootPath,
				highPrizeScheme, false);

		if(pwtAppMap==null)
			return "exceed";
		
		if (plrBean == null) {
			plrBean = (PlayerBean) session.getAttribute("playerBean");
			pwtAppMap.put("plrBean", plrBean);
		} else {
			pwtAppMap.put("plrBean", plrBean);
		}
		session.setAttribute("plrPwtAppDetMap", pwtAppMap);
		session.setAttribute("PWT_RES", pwtAppMap.get("PWT_RES_BEAN"));
		return SUCCESS;
	}

	// for Anonymous player
	public String registerAnyPlayer() throws LMSException {
		PlayerBean plrBean = null;
		boolean isAnonymous = true;
		HttpSession session = request.getSession();
		MainPWTDrawBean pwtDrawBean = (MainPWTDrawBean) session.getAttribute("PWT_RES");
		String highPrizeScheme = (String) ServletActionContext
				.getServletContext()
				.getAttribute("DRAW_GAME_HIGH_PRIZE_SCHEME");
		String rootPath = (String) session.getAttribute("ROOT_PATH");
		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		// if HIGH HIGH_PRIZE_CRITERIA or HIGH_PRIZE_AMT is NULL
		if (userInfoBean == null) {
			throw new LMSException("userInfoBean = " + userInfoBean);
		}
		logger.debug("highPrizeScheme  is " + highPrizeScheme);
		// player registration and approval process
		AgtPWTProcessHelper helper = new AgtPWTProcessHelper();
		logger.debug("root path is " + rootPath);
		this.pwtAppMap = helper.plrRegistrationAndApproval(userInfoBean,
				pwtDrawBean, playerType, playerId, plrBean, rootPath,
				highPrizeScheme, isAnonymous);
		if(pwtAppMap==null)
			return "exceed";
		
		session.setAttribute("plrPwtAppDetMap", pwtAppMap);
		session.setAttribute("PWT_RES", pwtAppMap.get("PWT_RES_BEAN"));

		return SUCCESS;
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

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setLocationCity(String locationCity) {
		this.locationCity = locationCity;
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

	public void setPwtAppMap(Map pwtAppMap) {
		this.pwtAppMap = pwtAppMap;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
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

	public void setTicketNbr(String ticketNbr) {
		this.ticketNbr = ticketNbr;
	}

	public void setTicketNum(String[] ticketNum) {
		this.ticketNum = ticketNum;
	}

	@SuppressWarnings("unchecked")
	public String submitRetPwts() throws LMSException {
		HttpSession session = getRequest().getSession();

		UserInfoBean userInfo = (UserInfoBean) session
				.getAttribute("USER_INFO");
		userInfo.setChannel("RETAIL");
		userInfo.setInterfaceType((String) request
				.getAttribute("interfaceType"));
		Map<String, PWTDrawBean> ticketMap = (Map<String, PWTDrawBean>) session
				.getAttribute("VERIFIED_TICKET_MAP");
		logger.debug(ticketMap + "**" + userInfo + "**" + retOrgId);
		AgtPWTProcessHelper pwtTicketHelper = new AgtPWTProcessHelper();
		Map<String, PWTDrawBean> ticketmap = pwtTicketHelper.retPwtSubmit(
				ticketMap, userInfo, retOrgId);

		session.setAttribute("VERIFIED_TICKET_MAP", ticketmap);
		session.setAttribute("SUBMITTED", "YES");
		return "success";
	}

	/*// methods for direct player pwt at agent end
	public String verifyDirPlrPwt() throws LMSException {

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
		AgtPWTProcessHelper helper = new AgtPWTProcessHelper();
		PWTDrawBean pwtWinningBean = helper.verifyAndSaveTicketNoDirPlr(
				drawScheduleBean, userInfoBean, pwtAmtForMasterApproval,
				highPrizeScheme);

		pwtWinningBean.setGameDispName(Util.getGameDisplayName(pwtWinningBean
				.getGameNo()));
		if ("UN_AUTH".equalsIgnoreCase(pwtWinningBean.getStatus())) {
			return "error";
		}
		session.setAttribute("PWT_RES", pwtWinningBean);
		if (pwtWinningBean.isValid()
				&& (pwtWinningBean.isHighPrize() || "OUT_LIMITS"
						.equalsIgnoreCase(pwtWinningBean.getPwtStatus()))) {
			session.setAttribute("PWT_AMT", pwtWinningBean.getTotalAmount());
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
	public String verifyDirPlrPwtNew() throws LMSException {

		HttpSession session = request.getSession();
		/*if (ticketNbr == null)
			return "error";*/

		ServletContext sc = ServletActionContext.getServletContext();
		String pwtAmtForMasterApproval = (String) sc.getAttribute("PWT_APPROVAL_LIMIT");
		String highPrizeScheme = (String) sc.getAttribute("DRAW_GAME_HIGH_PRIZE_SCHEME");
		String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
		String highPrizeAmt = (String) ServletActionContext.getServletContext().getAttribute("HIGH_PRIZE_AMT");
		String highPrizeCriteria = (String) ServletActionContext.getServletContext().getAttribute("HIGH_PRIZE_CRITERIA");
		UserInfoBean userInfoBean = (UserInfoBean) session.getAttribute("USER_INFO");
		MainPWTDrawBean mainPwtBean = new MainPWTDrawBean();
		if (ticketNbr != null && CommonValidation.isNumericWithoutDot(ticketNbr, false)){
			mainPwtBean.setTicketNo(ticketNbr.trim());
		}
		else{
			addActionError("Please Enter valid ticket number.");
			return "notvalidTkt";
		}
		logger.debug("*****ticketNbr***" + ticketNbr);
		AgtPWTProcessHelper helper = new AgtPWTProcessHelper();
		MainPWTDrawBean pwtWinningBean = helper.newMethod(mainPwtBean,
				userInfoBean, pwtAmtForMasterApproval, highPrizeScheme,
				refMerchantId, highPrizeAmt, highPrizeCriteria);
		
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
		
		session.setAttribute("PWT_RES", pwtWinningBean);

		if ("UN_AUTH".equalsIgnoreCase(pwtWinningBean.getStatus())) {
			return "error";
		}
		
		if (pwtWinningBean.getPwtStatus() != null && pwtWinningBean.isValid() && (!pwtWinningBean.isHighPrize() || "OUT_LIMITS".equalsIgnoreCase(pwtWinningBean.getPwtStatus()))) {
			session.setAttribute("PWT_AMT", pwtWinningBean.getTotlticketAmount());
			return "registration";
		} else if (pwtWinningBean.isValid() && pwtWinningBean.isWinTkt()) {
			String status = registerAnyPlayer();// register the player as
			// anonymous
			if (status.equals(SUCCESS)) {
				return "paySuccess";
			} else if("exceed".equals(status)) {
				return "exceed";
			}else{
				return "error";
			}
		} else {
			return SUCCESS;
		}

	}

	public String verifyNSaveTickets() throws LMSException {

		HttpSession session = getRequest().getSession();

		UserInfoBean userInfo = (UserInfoBean) session
				.getAttribute("USER_INFO");
		userInfo.setChannel("RETAIL");
		userInfo.setInterfaceType((String) request
				.getAttribute("interfaceType"));

		AgtPWTProcessHelper pwtTicketHelper = new AgtPWTProcessHelper();
		Map<String, PWTDrawBean> ticketmap = pwtTicketHelper
				.retTicketVerifyNSave(ticketNum, userInfo, retOrgId);

		session.setAttribute("VERIFIED_TICKET_MAP", ticketmap);
		session.setAttribute("SUBMITTED", "NO");

		return "success";
	}

}
