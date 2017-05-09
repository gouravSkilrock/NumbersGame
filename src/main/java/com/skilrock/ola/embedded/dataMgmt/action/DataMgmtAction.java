package com.skilrock.ola.embedded.dataMgmt.action;


import java.io.IOException;
import java.text.NumberFormat;
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
import com.skilrock.lms.ajax.AjaxRequestHelper;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.GenericException;
import com.skilrock.lms.common.exception.LMSErrorProperty;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.coreEngine.ola.reportMgmt.OlaRetailerReportHelper;
import com.skilrock.lms.coreEngine.userMgmt.common.CommonFunctionsHelper;
import com.skilrock.lms.embedded.drawGames.common.EmbeddedErrors;
import com.skilrock.ola.accMgmt.controllerImpl.OlaRetDepositControllerImpl;
import com.skilrock.ola.accMgmt.controllerImpl.OlaRetWithdrawlControllerImpl;
import com.skilrock.ola.accMgmt.javaBeans.OLADepositRequestBean;
import com.skilrock.ola.accMgmt.javaBeans.OLADepositResponseBean;
import com.skilrock.ola.accMgmt.javaBeans.OLAWithdrawalRequestBean;
import com.skilrock.ola.accMgmt.javaBeans.OLAWithdrawalResponseBean;
import com.skilrock.ola.common.OLAUtility;
import com.skilrock.ola.commonMethods.controllerImpl.OlaCommonMethodControllerImpl;
import com.skilrock.ola.javaBeans.OlaWalletBean;


public class DataMgmtAction extends ActionSupport implements ServletRequestAware,
		ServletResponseAware {
	/**
	 * 
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	static Log logger = LogFactory.getLog(DataMgmtAction.class);
	HttpServletRequest request;
	HttpServletResponse response;
	private HttpSession session = null;	
	private String userName;
	private double version;

	
	
	public void fetchWalletData(){
		logger.info("Fetch Wallet Data for userName:"+userName+"version:"+version);
		try{
			ServletContext sc = LMSUtility.sc;
			Map currentUserSessionMap = (Map) sc.getAttribute("LOGGED_IN_USERS");
			if(currentUserSessionMap==null){
				try{
						response.getOutputStream().write(("ErrorMsg:" + EmbeddedErrors.SESSION_EXPIRED + "ErrorCode:01|").getBytes());
						return ;
					}catch(Exception e){
						e.printStackTrace();
					}
			}
			session = (HttpSession)currentUserSessionMap.get(userName);
			if(!CommonFunctionsHelper.isSessionValid(session)){
				response.getOutputStream().write(("ErrorMsg:" + EmbeddedErrors.SESSION_EXPIRED + "ErrorCode:01|").getBytes());
				return ;
			}
			if(version>=8.5){
				OlaCommonMethodControllerImpl helper = new OlaCommonMethodControllerImpl();
				StringBuilder sb = new StringBuilder();
				boolean isFirst =true;
				Map<Integer, OlaWalletBean> walletData = helper.getWalletDetails();
				for(Map.Entry<Integer,OlaWalletBean> entry : walletData.entrySet()){
					if(!"PLAYER_LOTTERY".equalsIgnoreCase(entry.getValue().getWalletDevName())){
						continue;
					}
					if(isFirst){
						sb.append(entry.getKey()+","+entry.getValue().getWalletDisplayName());
						isFirst=false;
					}else{
						sb.append("|"+entry.getKey()+","+entry.getValue().getWalletDisplayName());
					}
				}
				logger.info("reponse:"+sb);
				String walletInfo = "WalletData:"+sb.toString();
				response.getOutputStream().write(walletInfo.getBytes());
			}else{
				response.getOutputStream().write(("ErrorMsg: "+EmbeddedErrors.BAD_VERSION_ERROR_MESSAGE).getBytes());			
			}
		}catch(Exception e){
			e.printStackTrace();
			try {
				response.getOutputStream().write(("ErrorMsg: "+EmbeddedErrors.PURCHSE_INVALID_DATA).getBytes());
			} catch (IOException e1) {
				logger.info("Error In Setting Response");
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
	
	public double getVersion() {
		return version;
	}

	public void setVersion(double version) {
		this.version = version;
	}
	
}
