package com.skilrock.lms.web.instantWin.pwtMgmt.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.skilrock.lms.beans.PlayerBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.instantWin.common.IWException;
import com.skilrock.lms.coreEngine.scratchService.pwtMgmt.common.PlayerVerifyHelperForApp;
import com.skilrock.lms.instantWin.javaBeans.VerifyTicketResponseBean;
import com.skilrock.lms.web.instantWin.pwtMgmt.daoImpl.InstantWinPwtService;

public class InstantWinPwtAction extends BaseAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private VerifyTicketResponseBean verifyTicketResponseBean;
	private String ticketNbr;
	
	private String firstName;
	private String lastName;
	private String idType;
	private String idNumber;
	private String emailId;
	private String phone;
	private String plrAddr1;
	private String plrAddr2;
	private String state;
	private String city;
	private String plrCountry;
	private String plrPin;
	private String bankBranch;
	private String bankName;
	private String locationCity;
	private String bankAccNbr;

	private String plrAlreadyReg;
	private String playerType;
	private int playerId;
	
	private String tktNbr;
	private String tktStatus;
	private String paymentTime;
	private double winningAmt;
	private String purchaseTime;
	private String tktData;
	private String claimTime;
	private String purchasedFrom;
	private boolean paymentAllowed;
	private String iwTransactionId;
	private int errorCode;
	private String errorMsg;
	private boolean isPlayerReg;
	
	public InstantWinPwtAction() {
		super(InstantWinPwtAction.class);
	}

	public VerifyTicketResponseBean getVerifyTicketResponseBean() {
		return verifyTicketResponseBean;
	}

	public void setVerifyTicketResponseBean(
			VerifyTicketResponseBean verifyTicketResponseBean) {
		this.verifyTicketResponseBean = verifyTicketResponseBean;
	}

	public String getTicketNbr() {
		return ticketNbr;
	}

	public void setTicketNbr(String ticketNbr) {
		this.ticketNbr = ticketNbr;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPlrAddr1() {
		return plrAddr1;
	}

	public void setPlrAddr1(String plrAddr1) {
		this.plrAddr1 = plrAddr1;
	}

	public String getPlrAddr2() {
		return plrAddr2;
	}

	public void setPlrAddr2(String plrAddr2) {
		this.plrAddr2 = plrAddr2;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPlrCountry() {
		return plrCountry;
	}

	public void setPlrCountry(String plrCountry) {
		this.plrCountry = plrCountry;
	}

	public String getPlrPin() {
		return plrPin;
	}

	public void setPlrPin(String plrPin) {
		this.plrPin = plrPin;
	}

	public String getBankBranch() {
		return bankBranch;
	}

	public void setBankBranch(String bankBranch) {
		this.bankBranch = bankBranch;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getLocationCity() {
		return locationCity;
	}

	public void setLocationCity(String locationCity) {
		this.locationCity = locationCity;
	}

	public String getBankAccNbr() {
		return bankAccNbr;
	}

	public void setBankAccNbr(String bankAccNbr) {
		this.bankAccNbr = bankAccNbr;
	}

	public String getPlrAlreadyReg() {
		return plrAlreadyReg;
	}

	public void setPlrAlreadyReg(String plrAlreadyReg) {
		this.plrAlreadyReg = plrAlreadyReg;
	}

	public String getPlayerType() {
		return playerType;
	}

	public void setPlayerType(String playerType) {
		this.playerType = playerType;
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}
	
	public String getTktNbr() {
		return tktNbr;
	}

	public void setTktNbr(String tktNbr) {
		this.tktNbr = tktNbr;
	}

	public String getTktStatus() {
		return tktStatus;
	}

	public void setTktStatus(String tktStatus) {
		this.tktStatus = tktStatus;
	}

	public String getPaymentTime() {
		return paymentTime;
	}

	public void setPaymentTime(String paymentTime) {
		this.paymentTime = paymentTime;
	}

	public double getWinningAmt() {
		return winningAmt;
	}

	public void setWinningAmt(double winningAmt) {
		this.winningAmt = winningAmt;
	}

	public String getPurchaseTime() {
		return purchaseTime;
	}

	public void setPurchaseTime(String purchaseTime) {
		this.purchaseTime = purchaseTime;
	}

	public String getTktData() {
		return tktData;
	}

	public void setTktData(String tktData) {
		this.tktData = tktData;
	}

	public String getClaimTime() {
		return claimTime;
	}

	public void setClaimTime(String claimTime) {
		this.claimTime = claimTime;
	}

	public String getPurchasedFrom() {
		return purchasedFrom;
	}

	public void setPurchasedFrom(String purchasedFrom) {
		this.purchasedFrom = purchasedFrom;
	}

	public boolean isPaymentAllowed() {
		return paymentAllowed;
	}

	public void setPaymentAllowed(boolean paymentAllowed) {
		this.paymentAllowed = paymentAllowed;
	}

	public String getIwTransactionId() {
		return iwTransactionId;
	}

	public void setIwTransactionId(String iwTransactionId) {
		this.iwTransactionId = iwTransactionId;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public boolean isPlayerReg() {
		return isPlayerReg;
	}

	public void setPlayerReg(boolean isPlayerReg) {
		this.isPlayerReg = isPlayerReg;
	}

	@Override
	public String execute() throws Exception {
		return SUCCESS;
	}

	public String fetchVerifyTicketData() {
		UserInfoBean userInfoBean = (UserInfoBean) getSession().getAttribute("USER_INFO");
		try {
			verifyTicketResponseBean = new InstantWinPwtService().fetchVerifyTicketData(ticketNbr, userInfoBean);
		} catch (IWException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(verifyTicketResponseBean.isPlayerReg()) {
			return "register";
		} else
			return SUCCESS;
	}
	
	public String payPwtTicket() {
		UserInfoBean userInfoBean = (UserInfoBean) getSession().getAttribute("USER_INFO");
		try {
			verifyTicketResponseBean = new InstantWinPwtService().payPwtTicket(ticketNbr, playerId, userInfoBean);
		} catch (IWException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String fetchPlayerDetails() throws LMSException {
		PlayerVerifyHelperForApp searchPlayerHelper = new PlayerVerifyHelperForApp();
		Map<String, Object> playerBeanMap = searchPlayerHelper.searchPlayer(firstName, lastName, idNumber, idType);
		PlayerBean plrBean = (PlayerBean) playerBeanMap.get("plrBean");
		HttpSession session = request.getSession();
		session.setAttribute("playerBean", plrBean);
		String plrAlreadyReg = "NO";
		if (plrBean != null) {
			plrAlreadyReg = "YES";
		}
		List<String> countryList = (ArrayList<String>) playerBeanMap.get("countryList");
		if (countryList == null) {
			countryList = new ArrayList<String>();
		}
		session.setAttribute("countryList", countryList);
		session.setAttribute("plrAlreadyReg", plrAlreadyReg);
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
		UserInfoBean userInfoBean = (UserInfoBean) getSession().getAttribute("USER_INFO");
		System.out.println("plrAlreadyReg = " + plrAlreadyReg + "  , playerType = " + playerType + "  , playerId = " + playerId);
		if (!"YES".equalsIgnoreCase(plrAlreadyReg.trim()) && "player".equalsIgnoreCase(playerType.trim())) {
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
			System.out.println("Inside player registration 11111 & plrBean is " + plrBean);
		}

		try {
			if(plrBean != null)
				playerId = new InstantWinPwtService().registerPlayer(plrBean);
			if(playerId == 0)
				playerId = -1;
			verifyTicketResponseBean = new InstantWinPwtService().payPwtTicket(tktNbr, playerId, userInfoBean);
			/*verifyTicketResponseBean = new VerifyTicketResponseBean();
			verifyTicketResponseBean.setTktNbr(tktNbr);
			verifyTicketResponseBean.setPurchaseTime(purchaseTime);
			verifyTicketResponseBean.setPurchasedFrom(purchasedFrom);
			verifyTicketResponseBean.setTktData(tktData);
			verifyTicketResponseBean.setTktStatus(tktStatus);
			verifyTicketResponseBean.setWinningAmt(winningAmt);
			verifyTicketResponseBean.setErrorCode(errorCode);
			verifyTicketResponseBean.setErrorMsg(errorMsg);
			verifyTicketResponseBean.setPaymentAllowed(true);*/
			System.out.println(verifyTicketResponseBean);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return SUCCESS;
	}

}

