package com.skilrock.lms.embedded.instantPrint.playMgmt;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.ipe.Bean.PwtLMSBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.instantPrint.common.IPEUtility;
import com.skilrock.lms.coreEngine.instantPrint.playMgmt.MPesaPaymentProcessHelper;
import com.skilrock.lms.coreEngine.instantPrint.playMgmt.RetPwtProcessHelper;
import com.skilrock.lms.embedded.drawGames.common.EmbeddedErrors;


public class RetPwtProcessAction extends ActionSupport implements ServletRequestAware, ServletResponseAware {
	
	private static final long serialVersionUID = 1L;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private int gameId;
	private String userName;
	private String ticketNo;
	private String virnNo;
	private String mPesa;
	private String mobileNo;
	private boolean isAutoCancel;
	

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	
	public String getmPesa() {
		return mPesa;
	}

	public void setmPesa(String mPesa) {
		this.mPesa = mPesa;
	}

	public String getVirnNo() {
		return virnNo;
	}

	public void setVirnNo(String virnNo) {
		this.virnNo = virnNo;
	}

	public boolean isAutoCancel() {
		return isAutoCancel;
	}

	public void setAutoCancel(boolean isAutoCancel) {
		this.isAutoCancel = isAutoCancel;
	}

	public String getTicketNo() {
		return ticketNo;
	}

	public void setTicketNo(String ticketNo) {
		this.ticketNo = ticketNo;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}
	
	@SuppressWarnings("unchecked")
	public void verifyPWTProcess() throws IOException, LMSException {
		ServletContext sc = ServletActionContext.getServletContext();
		String isIPE = (String) sc.getAttribute("IS_IPE");
		if ("NO".equalsIgnoreCase(isIPE)) {
			response.getOutputStream().write(
					"ErrorMsg:Instant Game Not Available|".getBytes());
			return;
		}
		
		int gameNo = IPEUtility.fetchGameNoFrmTicket(ticketNo, 3);
	    int gameId = IPEUtility.getGameId(gameNo);
	  
		Map<String, HttpSession> currentUserSessionMap = (Map<String, HttpSession>) sc
		.getAttribute("LOGGED_IN_USERS");
		HttpSession session = currentUserSessionMap.get(userName);
		if (session == null) {
			response
			.getOutputStream()
			.write(
					("ErrorMsg:" + EmbeddedErrors.SESSION_EXPIRED + "ErrorCode:01|")
					.getBytes());
			return;
		}
	    // String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID_IPE");
		UserInfoBean userBean = (UserInfoBean) session.getAttribute("USER_INFO");
		PwtLMSBean winLMSBean = new PwtLMSBean();
		if (gameNo == 0) {
			winLMSBean.setStatus("INVALID_TICKET");
			winLMSBean.setReturnType("success");
		}
		winLMSBean.setGameId(gameId);
		winLMSBean.setGameNo(gameNo);
		winLMSBean.setTicketNo(ticketNo);
		winLMSBean.setVirnNo(virnNo);
		
		String highPrizeCriteria = (String) ServletActionContext.getServletContext().getAttribute("HIGH_PRIZE_CRITERIA");
		String highPrizeAmt = (String) ServletActionContext.getServletContext().getAttribute("HIGH_PRIZE_AMT");
		System.out.println("high prize amt = " + highPrizeAmt
				+ " and  highPrizeCriteria = " + highPrizeCriteria);
		RetPwtProcessHelper helper = new RetPwtProcessHelper();
		// helper.payPwtTicket(userBean, refMerchantId, highPrizeCriteria, highPrizeAmt, winBean);
		
		if("Y".equalsIgnoreCase(mPesa) && mobileNo!=null){
			winLMSBean.setIsmPesaEnable(true);
			winLMSBean.setMobileNumber(mobileNo);
			UserInfoBean mPesaUserBean = new MPesaPaymentProcessHelper().payByMPesaAcc(winLMSBean, userBean);
			winLMSBean= helper.verifypwt(winLMSBean, mPesaUserBean, highPrizeCriteria, highPrizeAmt);
		}else{
			winLMSBean.setIsmPesaEnable(false);
			winLMSBean= helper.verifypwt(winLMSBean, userBean, highPrizeCriteria, highPrizeAmt);
		}		
		
		//output for embedded terminal
					
		String returnType = winLMSBean.getReturnType();
		

		System.out.println("pwt type return = " + returnType);

		if ("registration".equalsIgnoreCase(winLMSBean.getReturnType())) {
			response.getOutputStream()
					.write("Register User".getBytes());
			System.out.println("Register User");
			return;
		} else if (winLMSBean.getStatus() != null
				&& winLMSBean.getStatus().equalsIgnoreCase(
				"OUT_VERIFY_LIMIT")) {
			returnType = "ErrorMsg:" + EmbeddedErrors.PWT_OUT_VERIFY_LIMIT;
	System.out.println("FINAL PWT DATA:" + returnType);
	response.getOutputStream().write(returnType.getBytes());
	return;
	}
	else if ("OUT_PAY_LIMIT"
			.equalsIgnoreCase(winLMSBean.getStatus())) {
		returnType = "ErrorMsg:" + EmbeddedErrors.PWT_OUT_VERIFY_LIMIT;
		System.out.println("FINAL PWT DATA:" + returnType);
		response.getOutputStream().write(returnType.getBytes());
		return;
	
	}else if (winLMSBean.getStatus() != null
			&& winLMSBean.getStatus().equalsIgnoreCase(
					"OUT_VERIFY_LIMIT")) {
		returnType = "ErrorMsg:"
				+ EmbeddedErrors.PWT_OUT_VERIFY_LIMIT;
		System.out.println("FINAL PWT DATA:" + returnType);
		response.getOutputStream().write(returnType.getBytes());
		return;
	}		else if (winLMSBean.getStatus() != null
				&& winLMSBean.getStatus().equalsIgnoreCase(
				"NORMAL_PAY")) {

			returnType = "Ticket Validity:" + winLMSBean.getTktvalidity()
					+ "\n" + "Ticket Message:" + winLMSBean.getStatus()
					+ "\n" + "Virn Validity:"
					+ winLMSBean.getVirnvalidity() + "\n"
					+ "Virn Message:" + winLMSBean.getMessage() + "\n"
					+ "Winning Amount:" + winLMSBean.getPrizeAmt() + "\n"
					+ "Amt:" + winLMSBean.getPrizeAmt() + "\n"
					+ "mPesa:" + winLMSBean.ismPesaEnable() + "\n";

			// response.getOutputStream()
			// .write("VIRN is Valid".getBytes());

			response.getOutputStream().write(returnType.getBytes());
			return;
		} else if (winLMSBean.getStatus() != null
				&& winLMSBean.ismPesaEnable() == true){
			returnType = "Ticket Validity:" + winLMSBean.getTktvalidity()
			+ "\n" + "Ticket Message:" + winLMSBean.getStatus()
			+ "\n" + "Virn Validity:"
			+ winLMSBean.getVirnvalidity() + "\n"
			+ "Virn Message:" + winLMSBean.getMessage() + "\n"
			+ "Mobile validity:" + "Valid Number" + "\n"
			+ "Mobile message:" + "Mobile number valid" + "\n"
			+ "Winning Amount:" + winLMSBean.getPrizeAmt() + "\n"
			+ "Amt:" + winLMSBean.getPrizeAmt() + "\n"
			+ "mPesa:" + winLMSBean.ismPesaEnable() + "\n"
			+ "Mobile Number:" +winLMSBean.getMobileNumber()+ "\n"
			+ "Ref No:" +winLMSBean.getRefNumber()+ "|";

	// response.getOutputStream()
	// .write("VIRN is Valid".getBytes());

	response.getOutputStream().write(returnType.getBytes());
	return;
		}
	
	else if ("No Prize!! Try Again."
				.equalsIgnoreCase(winLMSBean.getStatus())) {
			returnType = "Ticket Validity:" + winLMSBean.getTktvalidity()
			+ "\n" + "Ticket Message:"
			+ winLMSBean.getStatus();
	response.getOutputStream().write(returnType.getBytes());
	return;		
		}else if ("error".equalsIgnoreCase(winLMSBean.getReturnType())) {
		returnType = "Ticket Validity:" + winLMSBean.getTktvalidity()
					+ "\n" + "Ticket Message:" + winLMSBean.getStatus();
					
					

			// response.getOutputStream()
			// .write("VIRN is Valid".getBytes());

			response.getOutputStream().write(returnType.getBytes());
			return;
		} 
		else if ("InValid Virn".equalsIgnoreCase(winLMSBean.getStatus())) {
					returnType = "Ticket Validity:" + winLMSBean.getTktvalidity()
						+ "\n" + "Ticket Message:"
						+ winLMSBean.getStatus() + "\n" + "Virn Validity:"
						+ winLMSBean.getVirnvalidity() + "\n"
						+ "Virn Message:" + winLMSBean.getMessage();
				response.getOutputStream().write(returnType.getBytes());
				return;
			} else {
				returnType = "Ticket Validity:" + winLMSBean.getTktvalidity()
						+ "\n" + "Ticket Message:"
						+ winLMSBean.getStatus();
				response.getOutputStream().write(returnType.getBytes());
				return;
			}

		
	
		
	}
}
