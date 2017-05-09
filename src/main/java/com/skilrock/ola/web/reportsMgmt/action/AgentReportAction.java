package com.skilrock.ola.web.reportsMgmt.action;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.GenericException;
import com.skilrock.lms.common.exception.LMSErrorProperty;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.ola.common.OLAUtility;
import com.skilrock.ola.commonMethods.controllerImpl.OlaCommonMethodControllerImpl;
import com.skilrock.ola.javaBeans.OlaWalletBean;
import com.skilrock.ola.reportsMgmt.controllerImpl.DepositWthdrwReportControllerImpl;
import com.skilrock.ola.reportsMgmt.controllerImpl.SearchPlayerControllerImpl;
import com.skilrock.ola.reportsMgmt.javaBeans.OlaReportBean;
import com.skilrock.ola.userMgmt.javaBeans.OlaPlayerRegistrationRequestBean;


public class AgentReportAction extends ActionSupport implements ServletRequestAware,
		ServletResponseAware {
	/**
	 * 
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	static Log logger = LogFactory.getLog(AgentReportAction.class);
	HttpServletRequest request;
	HttpServletResponse response;
	private Map<Integer, OlaWalletBean> walletDetailsMap=null;
	private String walletName;
	private String regToDate;
	private String regFromDate;
	private String alias;
	private String regType;
	private String start_date;
	private String end_date;
	private String playerType;
	private int retOrgId;
	private ArrayList<OlaPlayerRegistrationRequestBean> plrDetailsList;
	

	public String fetchReportMenu(){				
		setWalletDetailsMap(new OlaCommonMethodControllerImpl().getWalletDetails());
		return SUCCESS;
	}
	
	public String searchPlayer(){
		HttpSession session =  getRequest().getSession();
		UserInfoBean userBean = (UserInfoBean) session.getAttribute("USER_INFO");
		if(userBean.getUserType().equalsIgnoreCase("Retailer")){
			retOrgId=userBean.getUserOrgId();
		}
		SearchPlayerControllerImpl searchPlrController = new SearchPlayerControllerImpl();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calFrom = Calendar.getInstance();
		Calendar calTo= Calendar.getInstance();
		try{
			calFrom.setTime(sf.parse(regFromDate));
			calTo.setTime(sf.parse(regToDate));
			if(calFrom.compareTo(calTo)>0){
				addActionMessage("Invalid Date Selection : To Date Should be after From Date ");
				return SUCCESS;
			}	
			regToDate=regToDate+" 23:59:59";
			regFromDate=regFromDate+" 00:00:00";
			if("DIRECT".equalsIgnoreCase(regType)){
				regToDate = null;
				regFromDate = null;
			}
				plrDetailsList = searchPlrController.searchPlayer(retOrgId, walletName, regToDate, regFromDate, alias, regType);
			
		}
		catch (LMSException e) {
			System.out.println(e.getErrorCode());
			String errorMessage=LMSErrorProperty.getPropertyValue(e.getErrorCode());
			logger.info(e.getErrorCode()+errorMessage);
			addActionMessage(errorMessage);				
		}catch (GenericException e){
			System.out.println(e.getErrorCode());
			String errorMessage=LMSErrorProperty.getPropertyValue(e.getErrorCode());
			logger.info(e.getErrorCode()+errorMessage);
			addActionMessage(errorMessage);
		}catch (Exception e){
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String fetchOlaAgentDepWithReportData() {
		HttpSession session = getRequest().getSession();
		UserInfoBean userInfoBean = (UserInfoBean) session.getAttribute("USER_INFO");
		int walletId = OLAUtility.getWalletId(walletName);
		OlaReportBean olaReportBean = new OlaReportBean();
		olaReportBean.setFromDate(start_date + " 00:00:00");
		olaReportBean.setToDate(end_date + " 23:59:59");
		DepositWthdrwReportControllerImpl controller = new DepositWthdrwReportControllerImpl();
		try {
				List<OlaReportBean> olaReportList = controller.fetchOlaAgentDepWithReportData(olaReportBean, walletId, userInfoBean.getUserOrgId());
				session.setAttribute("OLA_DEP_WITH_DATA_LIST", olaReportList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String getWalletName() {
		return walletName;
	}

	public void setWalletName(String walletName) {
		this.walletName = walletName;
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

	public void setWalletDetailsMap(Map<Integer, OlaWalletBean> walletDetailsMap) {
		this.walletDetailsMap = walletDetailsMap;
	}

	public Map<Integer, OlaWalletBean> getWalletDetailsMap() {
		return walletDetailsMap;
	}
	
	
	public String getRegToDate() {
		return regToDate;
	}

	public void setRegToDate(String regToDate) {
		this.regToDate = regToDate;
	}

	public String getRegFromDate() {
		return regFromDate;
	}

	public void setRegFromDate(String regFromDate) {
		this.regFromDate = regFromDate;
	}

	public String getRegType() {
		return regType;
	}

	public void setRegType(String regType) {
		this.regType = regType;
	}

	public void setPlrDetailsList(ArrayList<OlaPlayerRegistrationRequestBean> plrDetailsList) {
		this.plrDetailsList = plrDetailsList;
	}

	public ArrayList<OlaPlayerRegistrationRequestBean> getPlrDetailsList() {
		return plrDetailsList;
	}
	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	public String getStart_date() {
		return start_date;
	}

	public void setStart_date(String startDate) {
		start_date = startDate;
	}

	public String getPlayerType() {
		return playerType;
	}

	public void setPlayerType(String playerType) {
		this.playerType = playerType;
	}

	public void setRetOrgId(int retOrgId) {
		this.retOrgId = retOrgId;
	}

	public int getRetOrgId() {
		return retOrgId;
	}
	
	public String getEnd_date() {
		return end_date;
	}

	public void setEnd_date(String endDate) {
		end_date = endDate;
	}
}
