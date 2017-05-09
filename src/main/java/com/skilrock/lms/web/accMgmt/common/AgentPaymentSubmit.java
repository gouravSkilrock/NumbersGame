/***
 *  * � copyright 2007, SkilRock Technologies, A division of Sugal & Damani Lottery Agency Pvt. Ltd.
 * All Rights Reserved
 * The contents of this file are the property of Sugal & Damani Lottery Agency Pvt. Ltd.
 * and are subject to a License agreement with Sugal & Damani Lottery Agency Pvt. Ltd.; you may
 * not use this file except in compliance with that License.  You may obtain a
 * copy of that license from:
 * Legal Department
 * Sugal & Damani Lottery Agency Pvt. Ltd.
 * 6/35,WEA, Karol Bagh,
 * New Delhi
 * India - 110005
 * This software is distributed under the License and is provided on an �AS IS�
 * basis, without warranty of any kind, either express or implied, unless
 * otherwise provided in the License.  See the License for governing rights and
 * limitations under the License.
 * 
 */
package com.skilrock.lms.web.accMgmt.common;


import java.sql.Connection;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryHelper;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.coreEngine.accMgmt.common.AgentPaymentSubmitHelper;
import com.skilrock.lms.coreEngine.accMgmt.common.CashRegisterHelper;
import com.skilrock.lms.coreEngine.accMgmt.common.RetailerPaymentSubmitHelper;
import com.skilrock.lms.coreEngine.reportsMgmt.common.GraphReportHelper;


/**
 * This class is used to submit the total payment from Agent. Agent
 * 
 * @author Skilrock Technologies
 * 
 */
public class AgentPaymentSubmit  extends ActionSupport implements
		ServletRequestAware {
	static Log logger= LogFactory.getLog(AgentPaymentSubmit.class);
	
	private static final long serialVersionUID = 1271130427666936592L;
	
	private HttpServletRequest request = null;
	private int orgId;
	private String orgType = null;
	private double cashAmnt;
	private double totalAmount;
	private double totalPay;
	int transaction_id;
	private int id;
	private String[] multiples;
	private String[] retDenoType;
	private String[] retMultiples;
	private String agentNameValue ;// it can be organization name/code/code_name etc 
	private Map<Integer, String> agentInfoMap;
	private String isCashRegister;
	private String retOrgName;
	/**
	 * This method is used to submit the payment made by the Agent
	 * 
	 * @return SUCCESS
	 * @throws LMSException
	 */
	public String start() {
		HttpSession session = null;
		String isCREnable = "INACTIVE";
		try{
			session = getRequest().getSession();
			ServletContext sc = ServletActionContext.getServletContext();
			UserInfoBean userBean = (UserInfoBean) session.getAttribute("USER_INFO");
			logger.info("REQUEST_CASH_PAYMENT_MENU-"+request.getAttribute("AUDIT_ID")+":"+userBean.getUserId());
			isCREnable = (String)sc.getAttribute("IS_CASH_REGISTER");
			if("ACTIVE".equalsIgnoreCase(isCREnable))
			{
				QueryHelper qp = new QueryHelper();
				isCREnable = qp.checkDrawerAvailablity(userBean.getUserId());
				if("INACTIVE".equals(isCREnable)){
					throw new LMSException(LMSErrors.DRAWER_NOT_ASSIGN_ERROR_CODE,LMSErrors.DRAWER_NOT_ASSIGN_ERROR_MESSAGE);
				}
			}
			isCashRegister=isCREnable;
			//session.setAttribute("isCashRegister",isCREnable);
			agentInfoMap=CommonMethods.getOrgInfoMap(userBean.getUserOrgId(),"AGENT",userBean.getRoleId());
			logger.info("RESPONSE_CASH_PAYMENT_MENU-:  cash register"+isCREnable);
		} catch (LMSException le) {
				logger.error("Exception",le);
				logger.info("RESPONSE_CASH_PAYMENT_MENU-: ErrorCode:"+le.getErrorCode()+" ErrorMessage:"+le.getErrorMessage());
	        	request.setAttribute("LMS_EXCEPTION",le.getErrorMessage());
	    		return "applicationException";
		} catch (Exception e) {
				e.printStackTrace();
				logger.info("RESPONSE_CASH_PAYMENT_MENU-: ErrorCode:"+LMSErrors.GENERAL_EXCEPTION_ERROR_CODE+" ErrorMessage:"+LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
				request.setAttribute("LMS_EXCEPTION",LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		    	return "applicationException";
		}
		return SUCCESS;
	}


    public String agentPayment() throws Exception {
		logger.info("REQUEST_CASH_PAYMENT_SUBMIT-"+this);
		HttpSession session = null;
		UserInfoBean userBean = null,agentInfoBean=null;
		String[] denoType=null;
		Connection con=null;
		try
		{
			session = getRequest().getSession();
			userBean = (UserInfoBean) session.getAttribute("USER_INFO");
			agentInfoBean=CommonMethods.fetchUserData(orgId);
			logger.info("REQUEST_CASH_PAYMENT_SUBMIT-"+request.getAttribute("AUDIT_ID")+":"+userBean.getUserId());
			String currencySymbol = (String) ServletActionContext.getServletContext().getAttribute("CURRENCY_SYMBOL");
			logger.info("user_id is-"+userBean.getUserId());
			ServletContext sc = ServletActionContext.getServletContext();
			String isCREnable = (String)sc.getAttribute("IS_CASH_REGISTER");
			if(totalAmount!=cashAmnt){
				throw new LMSException(LMSErrors.CASH_PAYMENT_INVALIDATE_DATA_ERROR_CODE,LMSErrors.CASH_PAYMENT_INVALIDATE_DATA_ERROR_MESSAGE);
			}
			if("ACTIVE".equalsIgnoreCase(isCREnable)){
				CashRegisterHelper drawerHelper = new CashRegisterHelper();
				List<String> denoList = drawerHelper.getReceivedDenoList();
				denoType = (String[]) denoList.toArray(new String[denoList.size()]);
			}
			con = DBConnect.getConnection();
			con.setAutoCommit(false);
			int retOrgId=Integer.parseInt(retOrgName);
			int agentId=agentInfoBean.getUserId();
			AgentPaymentSubmitHelper helper=new AgentPaymentSubmitHelper();
			String autoGeneRecieptNoAndId=helper.submitCashAgentAmt(orgId, "AGENT", totalAmount, userBean.getUserId(),userBean.getUserOrgId(),userBean.getUserType(),denoType,multiples,retDenoType,retMultiples,con);
			if(orgType.equalsIgnoreCase("RETAILER")){
				RetailerPaymentSubmitHelper retailerPaymentSubmit=new RetailerPaymentSubmitHelper();
			    autoGeneRecieptNoAndId=retailerPaymentSubmit.retailerCashPaySubmit(retOrgId, "RETAILER",retOrgId, totalAmount, agentId, orgId, "AGENT", con);
			}
			con.commit();
			String[] autoGeneReceipt=autoGeneRecieptNoAndId.split("#");
			String autoGeneRecieptNo=autoGeneReceipt[0];
			int id=Integer.parseInt(autoGeneReceipt[1]);
			java.util.Date d=new java.util.Date();
			
			SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			String generationTime=sdf.format(d.getTime());
			logger.info("Generation time:"+generationTime);
			boolean isThermalRcptRequired = "true".equals((String) ServletActionContext.getServletContext().getAttribute("IS_CASH_RCPT_ON_THERMAL_PRINTER"));
			
			if(isThermalRcptRequired){
				NumberFormat nf = NumberFormat.getInstance();
				nf.setMinimumFractionDigits(2);
				
				String amountCash = nf.format(totalAmount);
				String data="data=0|txType=RECEIPT|address="+CommonMethods.getOrgAddress(String.valueOf(userBean.getUserOrgId()))+"|genTime="+generationTime+"|mode=Voucher|voucherNo="+autoGeneRecieptNo+"|txDate="+sdf.format(d.getTime())+"|amount="+amountCash+"|orgName="+agentNameValue+"|ctr=200|parentOrgName="+userBean.getOrgName()+"|curSymbol="+currencySymbol;
				session.setAttribute("APP_DATA",data);
			}
			    session.setAttribute("totalPay", totalAmount);
			    session.setAttribute("orgName",agentNameValue);
				session.setAttribute("Receipt_Id", autoGeneRecieptNo);
				GraphReportHelper graphReportHelper = new GraphReportHelper();
				if("AGENT".equalsIgnoreCase(orgType)){
					String parentOrgName = null;
					int userOrgID = 0;
					parentOrgName = userBean.getOrgName();
					userOrgID = userBean.getUserOrgId();
					graphReportHelper.createTextReportBO(id, parentOrgName, userOrgID,
							(String) session.getAttribute("ROOT_PATH"));
				}else{
					graphReportHelper.createTextReportAgent(id, (String) session.getAttribute("ROOT_PATH"), orgId, agentInfoBean.getOrgName());
				}
		}catch (LMSException le) {
				logger.info("RESPONSE_CASH_PAYMENT_SUBMIT-: ErrorCode:"+le.getErrorCode()+" ErrorMessage:"+le.getErrorMessage());
	        	request.setAttribute("LMS_EXCEPTION",le.getErrorMessage());
	    		return "applicationException";
			}catch (Exception e) {
				logger.error("Exception",e);
				logger.info("RESPONSE_CASH_PAYMENT_SUBMIT-: ErrorCode:"+LMSErrors.GENERAL_EXCEPTION_ERROR_CODE+" ErrorMessage:"+LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
				request.setAttribute("LMS_EXCEPTION",LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		    	return "applicationException";
			}finally{
				try{
					if(con!=null){
						con.close();
					}
				}catch (SQLException e) {
					e.printStackTrace();
				}
			}

		return SUCCESS;
	}
	public int getOrgId() {
		return orgId;
	}


	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}
	
	
	public int getId() {
		return id;
	}

	

	public String getOrgType() {
		return orgType;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public double getTotalAmount() {
		return totalAmount;
	}
	public double getCashAmnt() {
		return cashAmnt;
	}

	public double getTotalPay() {
		return totalPay;
	}

	public int getTransaction_id() {
		return transaction_id;
	}

	public void setId(int id) {
		this.id = id;
	}
	public void setCashAmnt(double cashAmnt) {
		this.cashAmnt = cashAmnt;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public void setTotalPay(double totalPay) {
		this.totalPay = totalPay;
	}

	public void setTransaction_id(int transaction_id) {
		this.transaction_id = transaction_id;
	}

	public String[] getMultiples() {
		return multiples;
	}

	public void setMultiples(String[] multiples) {
		this.multiples = multiples;
	}

	public String[] getRetDenoType() {
		return retDenoType;
	}

	public void setRetDenoType(String[] retDenoType) {
		this.retDenoType = retDenoType;
	}

	public String[] getRetMultiples() {
		return retMultiples;
	}

	public void setRetMultiples(String[] retMultiples) {
		this.retMultiples = retMultiples;
	}
	public String getAgentNameValue() {
		return agentNameValue;
	}

	public void setAgentNameValue(String agentNameValue) {
		this.agentNameValue = agentNameValue;
	}


	public Map<Integer, String> getAgentInfoMap() {
		return agentInfoMap;
	}


	public void setAgentInfoMap(Map<Integer, String> agentInfoMap) {
		this.agentInfoMap = agentInfoMap;
	}


	@Override
	public String toString() {
		return "AgentPaymentSubmit [agentNameValue=" + agentNameValue
				+ ", agentOrgMap=" + agentInfoMap + ", cashAmnt=" + cashAmnt
				+ ", multiples=" + Arrays.toString(multiples) + ", orgId="
				+ orgId + ",  orgType=" + orgType
				+ ", retDenoType=" + Arrays.toString(retDenoType)
				+ ", retMultiples=" + Arrays.toString(retMultiples)
				+ ", totalAmount=" + totalAmount + ", totalPay=" + totalPay
				+ ", transaction_id=" + transaction_id + "]";
	}


	public void setIsCashRegister(String isCashRegister) {
		this.isCashRegister = isCashRegister;
	}
	public String getIsCashRegister() {
		return isCashRegister;
	}
	public void setRetOrgName(String retOrgName) {
		this.retOrgName = retOrgName;
	}
	public String getRetOrgName() {
		return retOrgName;
	}
}