package com.skilrock.lms.web.drawGames.pwtMgmt;

import java.io.IOException;

import javax.servlet.http.HttpSession;

import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.controller.pwtMgmtController.MerchantPwtControllerImpl;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameRPOSHelper;
import com.skilrock.lms.web.drawGames.common.Util;
import com.skilrock.lms.web.drawGames.pwtMgmt.javaBeans.PwtVerifyTicketBean;

/**
 * @author Nikhil K. Bansal
 * @category Direct Player Pwt MerchantWise
 */

@SuppressWarnings("serial")
public class MerchantPwtProcessAction extends BaseAction {

	public MerchantPwtProcessAction() {
		super(MerchantPwtProcessAction.class);
	}

	private String ticketNumber;
	private String verificationCode;
	private PwtVerifyTicketBean pwtVerifyBean;
	private String recieptNo;
	private double winningAmt;
	private String userName;

	public double getWinningAmt() {
		return winningAmt;
	}

	public void setWinningAmt(double winningAmt) {
		this.winningAmt = winningAmt;
	}

	public String getTicketNumber() {
		return ticketNumber;
	}

	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public PwtVerifyTicketBean getPwtVerifyBean() {
		return pwtVerifyBean;
	}

	public void setPwtVerifyBean(PwtVerifyTicketBean pwtVerifyBean) {
		this.pwtVerifyBean = pwtVerifyBean;
	}

	public String getRecieptNo() {
		return recieptNo;
	}

	public void setRecieptNo(String recieptNo) {
		this.recieptNo = recieptNo;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String merchantTicketVerification() {
		HttpSession session = null;
		UserInfoBean userBean = null;
		try {
			session = getSession();
			userBean = (UserInfoBean) session.getAttribute("USER_INFO");
			pwtVerifyBean = MerchantPwtControllerImpl.getInstance().merchantWiseTicketPwtInformation(ticketNumber, userBean);
			session.setAttribute("PWT_BEAN", pwtVerifyBean);
			return SUCCESS;
		} catch (LMSException el) {
			el.printStackTrace();
			request.setAttribute("LMS_EXCEPTION", el.getErrorMessage());
			return ERROR;
		} catch (Exception e) {
			e.printStackTrace();
			return "applicationError";
		}
	}

	public String payDirectPwtProcess() {
		HttpSession session = null;
		UserInfoBean userBean = null;
		try {
			session = getSession();
			pwtVerifyBean = (PwtVerifyTicketBean) session.getAttribute("PWT_BEAN");
			userBean = (UserInfoBean) session.getAttribute("USER_INFO");
			PwtVerifyTicketBean pwtBean = MerchantPwtControllerImpl.getInstance().payDirectPwtProcesscontrol(verificationCode, pwtVerifyBean, userBean);
			session.removeAttribute("PWT_BEAN");
			if (pwtBean.getRecieptNumber() != null) {
				session.setAttribute("generatedReceiptNumber", recieptNo);
				session.setAttribute("winningAmt", pwtBean.getTotalWinAmt());
				winningAmt = pwtBean.getTotalWinAmt();

				return SUCCESS;
			} else {
				return ERROR;
			}
		} catch (LMSException el) {
			el.printStackTrace();
			request.setAttribute("LMS_EXCEPTION", el.getErrorMessage());
			return ERROR;
		} catch (Exception e) {
			e.printStackTrace();
			return "applicationError";
		}

	}

	public String trackTpTicketRet() throws Exception {
		HttpSession session = null;
		UserInfoBean userBean = null;
		session = getSession();
		try {
			userBean = (UserInfoBean) session.getAttribute("USER_INFO");
			pwtVerifyBean = MerchantPwtControllerImpl.getInstance().merchantWiseTicketPwtInformation(ticketNumber, userBean);
			String highPrizeAmt = (String) LMSUtility.sc.getAttribute("HIGH_PRIZE_AMT");
			if (pwtVerifyBean == null) {
				pwtVerifyBean.setTicketStatus("INTERNAL_ERROR");
				return ERROR;
			}

			pwtVerifyBean.setAdvMsg(Util.getAdvMessage(userBean.getUserOrgId(), pwtVerifyBean.getGameId(), "RETAILER", "PWT", "DG"));
			session.setAttribute("PWT_BEAN", pwtVerifyBean);
			System.out.println("Ticket Status : " + pwtVerifyBean.getTicketStatus() + "/" + pwtVerifyBean.getResponseMsg());
			if (pwtVerifyBean.getTotalWinAmt() == 0.0 && "UNCLAIMED".equalsIgnoreCase(pwtVerifyBean.getTicketStatus()) && "CLAIM ALLOW".equalsIgnoreCase(pwtVerifyBean.getVerifyTicketDrawDataBeanList().get(0).getDrawStatus())) {
				pwtVerifyBean.setResponseMsg("NON WINNING TICKET");
				return "error";
			}
			if (!"UNCLAIMED".equalsIgnoreCase(pwtVerifyBean.getTicketStatus())) {
				pwtVerifyBean.setResponseMsg("TICKET ALREADY CLAIMED");
				return "error";
			}
			if (Double.parseDouble(highPrizeAmt) <= pwtVerifyBean.getTotalWinAmt()) {
				pwtVerifyBean.setResponseMsg("This ticket can only be claimed at BO.");
				return "error";
			}
			if ("CLAIM HOLD".equalsIgnoreCase(pwtVerifyBean.getVerifyTicketDrawDataBeanList().get(0).getDrawStatus())) {
				pwtVerifyBean.setResponseMsg("RESULT AWAITED");
				return "error";
			} else if ("CLAIM ALLOW".equalsIgnoreCase(pwtVerifyBean.getVerifyTicketDrawDataBeanList().get(0).getDrawStatus()))
				return "PWT";
			else {
				pwtVerifyBean.setResponseMsg(pwtVerifyBean.getVerifyTicketDrawDataBeanList().get(0).getDrawStatus());
				return "error";
			}
		} catch (LMSException e) {
			pwtVerifyBean = new PwtVerifyTicketBean();
			pwtVerifyBean.setResponseMsg(e.getErrorMessage());
			session.setAttribute("PWT_BEAN", pwtVerifyBean);
			return ERROR;
		}
	}

	public String payTpTicketPwtProcessRet() throws IOException {
		HttpSession session = null;
		UserInfoBean userBean = null;
		PwtVerifyTicketBean pwtBean = null;
		try {
			session = getSession();
			pwtVerifyBean = (PwtVerifyTicketBean) session.getAttribute("PWT_BEAN");
			userBean = (UserInfoBean) session.getAttribute("USER_INFO");
			userBean.setChannel("WEB");
			pwtBean = DrawGameRPOSHelper.payTpPwt(verificationCode, pwtVerifyBean, userBean);
			session.removeAttribute("PWT_BEAN");
			if (pwtBean.getRecieptNumber() != null) {
				session.setAttribute("generatedReceiptNumber", recieptNo);
				session.setAttribute("winningAmt", pwtBean.getTotalWinAmt());
				winningAmt = pwtBean.getTotalWinAmt();
				session.setAttribute("PWT_BEAN", pwtBean);
			} else {
				return ERROR;
			}
		} catch (LMSException el) {
			pwtVerifyBean.setResponseMsg(el.getErrorMessage());
			pwtVerifyBean.setResponseCode(el.getErrorCode());
			session.setAttribute("PWT_BEAN", pwtVerifyBean);
			return ERROR;
		} catch (Exception e) {
			pwtVerifyBean.setResponseMsg(e.getMessage());
			session.setAttribute("PWT_BEAN", pwtVerifyBean);
			return ERROR;
		}

		return "PWT";
	}
}