package com.skilrock.ola.embedded.reportsMgmt.action;


import java.io.IOException;
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
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.embedded.drawGames.common.EmbeddedErrors;
import com.skilrock.ola.common.OLAUtility;
import com.skilrock.ola.reportsMgmt.controllerImpl.DepositWthdrwReportControllerImpl;
import com.skilrock.ola.reportsMgmt.javaBeans.OlaReportBean;


public class RetailerReportAction extends ActionSupport implements ServletRequestAware,
		ServletResponseAware {
	
	private static final long serialVersionUID = 1L;
	static Log logger = LogFactory.getLog(RetailerReportAction.class);
	
	HttpServletRequest request;
	HttpServletResponse response;
	private HttpSession session = null;	
	private String userName;
	private String fromDate;
	private int walletId;

	public void depositWithdrawRepData (){
		DepositWthdrwReportControllerImpl retRepController = new DepositWthdrwReportControllerImpl();
		ServletContext sc = ServletActionContext.getServletContext();
		Map currentUserSessionMap = (Map) sc.getAttribute("LOGGED_IN_USERS");
		HttpSession session =  (HttpSession)currentUserSessionMap.get(userName);
		UserInfoBean userBean = (UserInfoBean) session.getAttribute("USER_INFO");
		try{				
			 if(walletId>0){
				 String repData ="repData:";
				String	walletName = OLAUtility.getWalletName(walletId);
				if(walletName!=null&&fromDate!=null&&(!(fromDate.trim()).equalsIgnoreCase(""))){
					if(walletName.equalsIgnoreCase("PLAYER_LOTTERY")) {
						String responseData=retRepController.fetchOlaRetailerReportDataConsolidate(fromDate, walletId,userBean.getUserOrgId());
						if("".equals(responseData.trim())){
							response.getOutputStream().write(("ErrorMsg: No Records Found.").getBytes());

						}else{
							repData=repData+responseData;
							response.getOutputStream().write(repData.getBytes());
						}
						
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();			
			try {
				response.getOutputStream().write(("ErrorMsg: "+EmbeddedErrors.PURCHSE_INVALID_DATA).getBytes());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}		
	}
	
	
	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getFromDate() {
		return fromDate;
	}
	
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	
	public HttpSession getSession() {
		return session;
	}
	
	public void setSession(HttpSession session) {
		this.session = session;
	}
	
	public int getWalletId() {
		return walletId;
	}
	
	public void setWalletId(int walletId) {
		this.walletId = walletId;
	}
	
}
