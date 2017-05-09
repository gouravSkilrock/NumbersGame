package com.skilrock.lms.embedded.sportsLottery.pwtMgmt.action;

import java.io.IOException;
import java.text.NumberFormat;

import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.coreEngine.sportsLottery.beans.PwtVerifyTicketBean;
import com.skilrock.lms.coreEngine.sportsLottery.beans.SportsLotteryPayPwtBean;
import com.skilrock.lms.coreEngine.sportsLottery.common.SLEErrors;
import com.skilrock.lms.coreEngine.sportsLottery.common.SLEException;
import com.skilrock.lms.coreEngine.sportsLottery.pwtMgmt.controllerImpl.PayPwtTicketControllerImpl;
import com.skilrock.lms.embedded.sportsLottery.common.SportsLotteryResponseData;



public class SportsLotteryTerminalPayPwtAction  extends BaseAction {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public SportsLotteryTerminalPayPwtAction() {
		super(SportsLotteryTerminalPayPwtAction.class);
	}
	
	private long ticketNo;
	private String userName;
	
	
	public long getTicketNo() {
		return ticketNo;
	}


	public void setTicketNo(long ticketNo) {
		this.ticketNo = ticketNo;
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public void payPwtTicket() throws Exception {
		UserInfoBean userBean=getUserBean(userName);
		PayPwtTicketControllerImpl payPwtTicketController=null;
		PwtVerifyTicketBean pwtVerifyTicketBean=null;
		SportsLotteryPayPwtBean payPwtTicketBean=null;
		try{
			payPwtTicketController=new PayPwtTicketControllerImpl();
			payPwtTicketBean=new SportsLotteryPayPwtBean();
			payPwtTicketBean.setInterfaceType("TERMINAL");
			payPwtTicketBean.setMerchantName("WGRL");
			payPwtTicketBean.setTicketNumber(ticketNo);
			payPwtTicketBean.setServiceCode("SL");
			payPwtTicketBean.setDirectPlrPwt("NO");
			pwtVerifyTicketBean=payPwtTicketController.payPwtTicket(payPwtTicketBean,userBean);
			if(pwtVerifyTicketBean == null){
				throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			}
			double bal = userBean.getAvailableCreditLimit()
					- userBean.getClaimableBal();
			
			NumberFormat nf = NumberFormat.getInstance();
			nf.setMinimumFractionDigits(2);
			
			String balance = nf.format(bal).replaceAll(",", "");
			pwtVerifyTicketBean.setTicketNumber(ticketNo);
			String responseString=SportsLotteryResponseData.generateSportsLotteryPwtResponseData(pwtVerifyTicketBean,balance);
			
			System.out.println(responseString);
			response.getOutputStream().write(responseString.getBytes());
		}catch (SLEException e) {
			try {
				response.getOutputStream().write(("ErrorMsg:"+e.getErrorMessage()).getBytes());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return;
		}catch (IOException e) {
			e.printStackTrace();
			try {
				response.getOutputStream().write("ErrorMsg:Error!Try Again".getBytes());
			} catch (IOException e1) {
				
				e1.printStackTrace();
			}
			return;
		}catch (Exception e) {
			e.printStackTrace();
			try {
				response.getOutputStream().write("ErrorMsg:Error!Try Again".getBytes());
			} catch (IOException e1) {
				
				e1.printStackTrace();
			}
			return;
		}
		
	}
		
	
}
