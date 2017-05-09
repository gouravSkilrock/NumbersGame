package com.skilrock.lms.embedded.drawGames.common;


import java.io.IOException;
import java.text.NumberFormat;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.skilrock.lms.ajax.AjaxRequestHelper;
import com.skilrock.lms.beans.OrgPwtLimitBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.controller.pwtMgmtController.MerchantPwtControllerImpl;
import com.skilrock.lms.coreEngine.drawGames.common.CommonFunctionsHelper;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameRPOSHelper;
import com.skilrock.lms.coreEngine.drawGames.pwtMgmt.RetPWTProcessHelper;
import com.skilrock.lms.web.drawGames.common.Util;
import com.skilrock.lms.web.drawGames.pwtMgmt.javaBeans.BoardTicketDataBean;
import com.skilrock.lms.web.drawGames.pwtMgmt.javaBeans.PwtVerifyTicketBean;
import com.skilrock.lms.web.drawGames.pwtMgmt.javaBeans.PwtVerifyTicketDrawDataBean;

public class TrackTpTicketAction extends BaseAction{

	/**
	 * @author - Anuj Sharma
	 * @category - Third Party Tickets To Be Claimed from Terminal
	 */
	private static final long serialVersionUID = 1L;
	
	private PwtVerifyTicketBean pwtVerifyBean;
	private String ticketNumber;
	private String verificationCode;
	private String recieptNo;
	private double winningAmt;
	private String userName ;
	
	public TrackTpTicketAction() {
		
		super(TrackTpTicketAction.class);
		
	}
	
	public void trackTpTicket() throws Exception
	{
		HttpSession session = null ;
		String responseData = "";
		session = getSession(userName);
		try{
			UserInfoBean userInfoBean = (UserInfoBean) session
			.getAttribute("USER_INFO");
		pwtVerifyBean=MerchantPwtControllerImpl.getInstance().merchantWiseTicketPwtInformation(ticketNumber, userInfoBean);
		if(pwtVerifyBean == null){
			responseData = "ErrorMsg: Some Error Occurred...!!! Try Again...!!!!";
			response.getOutputStream().write((responseData).getBytes());	
			return;
		}
		String highPrizeAmt = (String) LMSUtility.sc.getAttribute("HIGH_PRIZE_AMT");
		session.setAttribute("PWT_BEAN", pwtVerifyBean);
		
		if("CLAIM HOLD".equalsIgnoreCase(pwtVerifyBean.getVerifyTicketDrawDataBeanList().get(0).getDrawStatus())){
			responseData = "RaffleData:#|Ticket No:"+pwtVerifyBean.getTicketNumber()+"|Message:Awaited|Ticket Status:"+pwtVerifyBean.getTicketStatus()+"|Retailer Name:"+pwtVerifyBean.getUserName()+"|Total Pay:"+pwtVerifyBean.getTotalWinAmt()+"|Draw Status:"+pwtVerifyBean.getVerifyTicketDrawDataBeanList().get(0).getDrawStatus();
			response.getOutputStream().write((responseData).getBytes());
			return;
		}
		else if ("VERIFICATION PENDING".equalsIgnoreCase(pwtVerifyBean.getVerifyTicketDrawDataBeanList().get(0).getDrawStatus())){
			responseData = "RaffleData:#|Ticket No:"+pwtVerifyBean.getTicketNumber()+"|Message:VER PND|Ticket Status:"+pwtVerifyBean.getTicketStatus()+"|Retailer Name:"+pwtVerifyBean.getUserName()+"|Total Pay:"+pwtVerifyBean.getTotalWinAmt()+"|Draw Status:"+pwtVerifyBean.getVerifyTicketDrawDataBeanList().get(0).getDrawStatus();
			response.getOutputStream().write((responseData).getBytes());
			return;
		}
		else if("CLAIMED".equalsIgnoreCase(pwtVerifyBean.getTicketStatus()))
		{
			responseData = "RaffleData:#|Ticket No:"+pwtVerifyBean.getTicketNumber()+"|Message:CLAIMED|Ticket Status:"+pwtVerifyBean.getTicketStatus()+"|Retailer Name:"+pwtVerifyBean.getUserName()+"|Total Pay:"+pwtVerifyBean.getTotalWinAmt()+"|Draw Status:"+pwtVerifyBean.getVerifyTicketDrawDataBeanList().get(0).getDrawStatus();
			response.getOutputStream().write((responseData).getBytes());
			return;
		}
		else if(pwtVerifyBean.getTotalWinAmt() == 0.0 && "UNCLAIMED".equalsIgnoreCase(pwtVerifyBean.getTicketStatus()) && "CLAIM ALLOW".equalsIgnoreCase(pwtVerifyBean.getVerifyTicketDrawDataBeanList().get(0).getDrawStatus())){
			responseData = "RaffleData:#|Ticket No:"+pwtVerifyBean.getTicketNumber()+"|Message:TRY AGAIN|Ticket Status:"+pwtVerifyBean.getTicketStatus()+"|Retailer Name:"+pwtVerifyBean.getUserName()+"|Total Pay:"+pwtVerifyBean.getTotalWinAmt()+"|Draw Status:"+pwtVerifyBean.getVerifyTicketDrawDataBeanList().get(0).getDrawStatus();
			response.getOutputStream().write((responseData).getBytes());
			return;
		}
		else if(Double.parseDouble(highPrizeAmt) <= pwtVerifyBean.getTotalWinAmt())
		{
			responseData = "ErrorMsg:"+"This ticket can only be claimed at BO." ;
			response.getOutputStream().write((responseData).getBytes());
			return;
		}
		
		else if ("CLAIM ALLOW".equalsIgnoreCase(pwtVerifyBean.getVerifyTicketDrawDataBeanList().get(0).getDrawStatus()) && pwtVerifyBean.getTotalWinAmt() > 0.0 && "UNCLAIMED".equalsIgnoreCase(pwtVerifyBean.getTicketStatus())){
			responseData = "RaffleData:#|Ticket No:"+pwtVerifyBean.getTicketNumber()+"|isVerCode:"+("OKPOS".equalsIgnoreCase(pwtVerifyBean.getMerchantName())?"N":"Y")+"|Ticket Status:"+pwtVerifyBean.getTicketStatus()+"|Retailer Name:"+pwtVerifyBean.getUserName()+"|Total Pay:"+pwtVerifyBean.getTotalWinAmt()+"|Draw Status:"+pwtVerifyBean.getVerifyTicketDrawDataBeanList().get(0).getDrawStatus();
			response.getOutputStream().write((responseData).getBytes());
			return;
		}
		else{
			responseData = "ErrorMsg:"+pwtVerifyBean.getVerifyTicketDrawDataBeanList().get(0).getDrawStatus() ;
			response.getOutputStream().write((responseData).getBytes());
			return;
		}
		}catch(LMSException el)
		{
			responseData = "ErrorMsg:"+el.getErrorMessage()+"|ErrorCode:"+el.getErrorCode();
			response.getOutputStream().write((responseData).getBytes());
			return;
		}
	}
	
	public void payTpTicketPwtProcess() throws IOException{
		
		UserInfoBean userBean=null;
		
		PwtVerifyTicketBean pwtBean = null ;
		
		try{
			
			//session = getSession();
			ServletContext sc = ServletActionContext.getServletContext();
			Map currentUserSessionMap = (Map) sc.getAttribute("LOGGED_IN_USERS");
			HttpSession sess = (HttpSession) currentUserSessionMap.get(userName);
			
			pwtVerifyBean=(PwtVerifyTicketBean)sess.getAttribute("PWT_BEAN");
			
			userBean=(UserInfoBean)sess.getAttribute("USER_INFO");
			userBean.setChannel("TERMINAL");

			AjaxRequestHelper ajxHelper1 = new AjaxRequestHelper();
			ajxHelper1.getAvlblCreditAmt(userBean);
			double bal1 = userBean.getAvailableCreditLimit()
			- userBean.getClaimableBal();
			
			pwtBean = DrawGameRPOSHelper.payTpPwt(verificationCode,pwtVerifyBean,userBean);
			
			sess.removeAttribute("PWT_BEAN");
			
			if (pwtBean.getRecieptNumber()!=null) {
				
				sess.setAttribute("generatedReceiptNumber", recieptNo);
				
				sess.setAttribute("winningAmt", pwtBean.getTotalWinAmt());
				int win = 0;
				winningAmt=pwtBean.getTotalWinAmt();
				if (pwtVerifyBean.getVerifyTicketDrawDataBeanList() != null && pwtVerifyBean.getVerifyTicketDrawDataBeanList().size() > 0) {
					for (PwtVerifyTicketDrawDataBean drawIdBean : pwtVerifyBean.getVerifyTicketDrawDataBeanList()){
						if( drawIdBean.getBoardTicketBeanList()!=null && drawIdBean.getBoardTicketBeanList().size()>0) {
						    for (BoardTicketDataBean panelIdBean : drawIdBean.getBoardTicketBeanList()){
						    	if(panelIdBean.getWinningAmt()>0.0){
						    	win++;	
						    	}
							}
						}
					}
				}
				AjaxRequestHelper ajxHelper2 = new AjaxRequestHelper();
				ajxHelper2.getAvlblCreditAmt(userBean);
				double bal2 = userBean.getAvailableCreditLimit()
				- userBean.getClaimableBal();
				double bal = bal2 - bal1;
				NumberFormat nFormat = NumberFormat.getInstance();
				nFormat.setMinimumFractionDigits(2);
				String balance =  nFormat.format(bal).replace(",", "");
				response.getOutputStream().write(("RaffleData:#|DrawTime:"+pwtBean.getVerifyTicketDrawDataBeanList().get(0).getDrawDateTime()+"*"+pwtBean.getVerifyTicketDrawDataBeanList().get(0).getDrawName()+",No:"+win+",Message:WIN "+pwtBean.getTotalWinAmt()+"|Total Pay:"+pwtBean.getTotalWinAmt()+"|Amt:"+balance+"|gameName:"+Util.getGameDisplayName(pwtBean.getGameId())+"|").getBytes());
				
			} else {
				
				response.getOutputStream().write(("ErrorMsg:" + pwtBean.getResponseMsg()+ "|ErrorCode:"+pwtBean.getResponseCode()).getBytes());
				
			}
		}
		catch (LMSException el) {
			
			el.printStackTrace();
			
			response.getOutputStream().write(("ErrorMsg:" + el.getErrorMessage()+ "|ErrorCode:"+el.getErrorCode()).getBytes());

		}catch (Exception e) {
			
			e.printStackTrace();
			
			response.getOutputStream().write(("ErrorMsg:" + e.getMessage()).getBytes());

		}
		
	}

	public PwtVerifyTicketBean getPwtVerifyBean() {
		return pwtVerifyBean;
	}

	public void setPwtVerifyBean(PwtVerifyTicketBean pwtVerifyBean) {
		this.pwtVerifyBean = pwtVerifyBean;
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

	public String getRecieptNo() {
		return recieptNo;
	}

	public void setRecieptNo(String recieptNo) {
		this.recieptNo = recieptNo;
	}

	public double getWinningAmt() {
		return winningAmt;
	}

	public void setWinningAmt(double winningAmt) {
		this.winningAmt = winningAmt;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
	
}
