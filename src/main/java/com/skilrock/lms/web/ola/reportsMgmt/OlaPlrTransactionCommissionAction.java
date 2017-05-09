package com.skilrock.lms.web.ola.reportsMgmt;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.OlaCommissionBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.ola.reportMgmt.OlaPlrTransactionCommissionHelper;

public class OlaPlrTransactionCommissionAction extends ActionSupport implements ServletRequestAware, ServletResponseAware{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HttpSession session = null;
	private HttpServletRequest request;
	private HttpServletResponse response;
    private Date startDate;
    private Date endDate;
    private String walletName;
    private String date;
    private String netGamingExpType;
    private List<OlaCommissionBean> olaCommissionBeanList;
    private String message;
    private String playerId;
    private String netGamingDistributionModel;
    public static void main(String[] args){
		OlaPlrTransactionCommissionHelper helper=new OlaPlrTransactionCommissionHelper();
     
    }
    
	public String olaPlrTransactionCommissionSUCCESS() throws LMSException{
		int walletId=2;
		session = request.getSession();
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		int retOrgId=userBean.getUserOrgId();
		ServletContext sc=ServletActionContext.getServletContext();
		netGamingDistributionModel=(String) sc.getAttribute("OLA_NETGAMING_DISTRIBUTION_MODEL");
		System.out.println("Net Gaming Model:"+netGamingDistributionModel);
		System.out.println("hello india ");
		fetchDate(date, netGamingExpType);
		OlaPlrTransactionCommissionHelper helper=new OlaPlrTransactionCommissionHelper();
		String status=helper.checkRetailerTransactionStatus(startDate, endDate, retOrgId, walletId);
		if("DONE".equalsIgnoreCase(status)){
			
			 setOlaCommissionBeanList(helper.getPlayerWiseCommissionData( startDate, endDate, walletId,retOrgId,netGamingDistributionModel ));
			 
		}else{
			setMessage("Net Gaming Commission is not Calculated");
		}
		
		return SUCCESS;
	}
	public String olaPlrTransactionCommissionSEARCH() throws LMSException{
		int walletId=2;
		session = request.getSession();
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		int retOrgId=userBean.getUserOrgId();
		System.out.println("hello india ");
		fetchDate(date, netGamingExpType);
		ServletContext sc=ServletActionContext.getServletContext();
		netGamingDistributionModel=(String) sc.getAttribute("OLA_NETGAMING_DISTRIBUTION_MODEL");
		System.out.println("Net Gaming Model:"+netGamingDistributionModel);
		
		OlaPlrTransactionCommissionHelper helper = new OlaPlrTransactionCommissionHelper();
		if ("CUMMULATIVE".equalsIgnoreCase(netGamingDistributionModel)) {
			setOlaCommissionBeanList(helper
					.getPlayerDepositCummulativeTypeCommissionData(startDate,
							endDate, walletId, playerId));
		} else {
			setOlaCommissionBeanList(helper.getPlayerDepositCommissionData(
					startDate, endDate, walletId, playerId));
		}
			 
		
		return SUCCESS;
	}
	
	
	public HttpServletResponse getResponse() {
		return response;
	}

	public void setServletRequest(HttpServletRequest req) {
		this.request = req;

	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;

	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getWalletName() {
		return walletName;
	}

	public void setWalletName(String walletName) {
		this.walletName = walletName;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	
	public String getNetGamingExpType() {
		return netGamingExpType;
	}

	public void setNetGamingExpType(String netGamingExpType) {
		this.netGamingExpType = netGamingExpType;
	}

	
	public List<OlaCommissionBean> getOlaCommissionBeanList() {
		return olaCommissionBeanList;
	}

	public void setOlaCommissionBeanList(
			List<OlaCommissionBean> olaCommissionBeanList) {
		this.olaCommissionBeanList = olaCommissionBeanList;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	private void fetchDate(String tDate, String type) {
		
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// code to get week for given date... goes here
		try {
			if ("WEEKLY".equalsIgnoreCase(type)) {
				cal.setTimeInMillis(sdf.parse(tDate).getTime());
				if(cal.get(Calendar.DAY_OF_WEEK) != 1)
					cal.add(Calendar.DAY_OF_WEEK,
							-(cal.get(Calendar.DAY_OF_WEEK) - 2));
				else
					cal.add(Calendar.DAY_OF_WEEK,-6);
				setStartDate(new Date(cal.getTime().getTime()));
				
				cal.add(Calendar.DAY_OF_MONTH, +6);
				setEndDate(new Date(cal.getTime().getTime()));
				
				
			} else if ("MONTHLY".equalsIgnoreCase(type)){
				cal.setTimeInMillis(sdf.parse(date).getTime());
				cal.add(Calendar.DAY_OF_MONTH,-cal.get(Calendar.DAY_OF_MONTH));
				
				cal.setTimeInMillis(startDate.getTime());
				cal.add(Calendar.MONTH,1);
				
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public String getNetGamingDistributionModel() {
		return netGamingDistributionModel;
	}

	public void setNetGamingDistributionModel(String netGamingDistributionModel) {
		this.netGamingDistributionModel = netGamingDistributionModel;
	}

	
}
