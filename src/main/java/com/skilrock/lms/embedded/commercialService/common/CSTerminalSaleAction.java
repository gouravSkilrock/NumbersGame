 package com.skilrock.lms.embedded.commercialService.common;

import java.io.PrintWriter;
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
import com.skilrock.camlot.coreEngine.common.CommonCamlotHelper;
import com.skilrock.cs.beans.CamlotSaleBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.commercialService.common.CSPWSaleHelper;
import com.skilrock.lms.embedded.drawGames.common.EmbeddedErrors;

public class CSTerminalSaleAction extends ActionSupport implements ServletRequestAware, ServletResponseAware{
	static Log logger = LogFactory.getLog(CSTerminalSaleAction.class);
	private static final long serialVersionUID = 1L;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private HttpSession session;
	private String agentid;
	private String retailerid;
	private String userName;
	private String operatorcode;
	private String circode;
	private String product;
	private double denomination;
	private double rechargeAmt; //incase of pinless
	private int bulkqty;
	private String narration;
	private String mobileNo;
	private String RMSTransId;
	private String CSRefTransId;
	private long lastTransId;
	
	
	public void saleTransaction(){ 
		ServletContext sc = ServletActionContext.getServletContext();
		try{
			PrintWriter pw = response.getWriter() ;
			HttpSession session = ((HttpSession)((Map<String, UserInfoBean>)sc.getAttribute("LOGGED_IN_USERS")).get(userName));
			if(session == null){
				pw.write("ERROR:"+EmbeddedErrors.SESSION_EXPIRED);
				return;
			}
			String apiProvider = (String)sc.getAttribute("CS_PROVIDER");
			if(apiProvider.equalsIgnoreCase("CAMLOT")){
				//Camlot
				CommonCamlotHelper camlotHelper = new CommonCamlotHelper();
				UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("USER_INFO");
				retailerid = userInfoBean.getUserOrgId()+"";
				String serverResp = camlotHelper.camlotSale(product, userInfoBean.getUserOrgId()+"", userInfoBean.getUserName(),operatorcode, denomination, rechargeAmt, bulkqty,mobileNo,lastTransId);
				pw.write(serverResp);
			}else if(apiProvider.equalsIgnoreCase("PAYWORLD")){
				//Payworld
				String agtid = (String)sc.getAttribute("PW_MERCHANT_ID");
				String agtpwd = (String)sc.getAttribute("PW_MERCHANT_PWD");
				String loginstatus = (String)sc.getAttribute("PW_MERCHANT_LOGIN_STATUS");
				String appver = (String)sc.getAttribute("PW_PAYWORLD_API_VERSION");
			
				CSPWSaleHelper csHelper = new CSPWSaleHelper();
				UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("USER_INFO");
				retailerid = userInfoBean.getUserOrgId()+"";
				String csResp = csHelper.pwSaleTransaction(product, userInfoBean.getUserOrgId()+"", userInfoBean.getUserName(), operatorcode, circode, denomination, rechargeAmt, bulkqty, narration, agtid, agtpwd, loginstatus, appver,(String)ServletActionContext.getServletContext().getAttribute("cs_isVoucherPrintON"),mobileNo,lastTransId);
				logger.debug("PWTerminalSaleResponse"+csResp);
				pw.write(csResp);
			}else{
				pw.write("ERROR:No Service Provider|");
			}
		}catch(Exception e){
			logger.debug("Some Error occured in PW "+new LMSException(e).getMessage());
			e.printStackTrace();
		}
	}
	
	public void serviceData(){
		try{
			PrintWriter pw = response.getWriter();
			CSPWSaleHelper helper = new CSPWSaleHelper();
			String resp = helper.fetchPWSaleResponse((String)ServletActionContext.getServletContext().getAttribute("cs_isVoucherPrintON"));
			
			logger.debug("PWTerminalServiceDataResp"+resp);
			pw.write(resp);
		}catch(Exception e){
			logger.debug("Some Error occured in PW "+new LMSException(e).getMessage());
		}
	}
	
	public void reprintLastTrans(){
		ServletContext sc = ServletActionContext.getServletContext();
		try{
			PrintWriter pw = response.getWriter() ;
			HttpSession session = ((HttpSession)((Map<String, UserInfoBean>)sc.getAttribute("LOGGED_IN_USERS")).get(userName));
			UserInfoBean userBean = (UserInfoBean)session.getAttribute("USER_INFO");
			if(session == null){
				pw.write("ERROR:"+EmbeddedErrors.SESSION_EXPIRED);
				return;
			}
			String apiProvider = (String)sc.getAttribute("CS_PROVIDER");
			if(apiProvider.equalsIgnoreCase("CAMLOT")){
				//Camlot
				CommonCamlotHelper camlotHelper = new CommonCamlotHelper();
				String resp = camlotHelper.camlotRefund(userBean);
				logger.debug("PWTerminalReprintResp"+resp);
				pw.write(resp);
			}else if(apiProvider.equalsIgnoreCase("PAYWORLD")){
				//payworld
				CSPWSaleHelper helper = new CSPWSaleHelper();
				String agtid = (String)sc.getAttribute("PW_MERCHANT_ID");
				String agtpwd = (String)sc.getAttribute("PW_MERCHANT_PWD");
				String loginstatus = (String)sc.getAttribute("PW_MERCHANT_LOGIN_STATUS");
				String appver = (String)sc.getAttribute("PW_PAYWORLD_CLIENT_VERSION");
				String resp = helper.fetchReprintLastTrans(userBean.getUserOrgId(), agtid,agtpwd,loginstatus,appver, userBean);
				logger.debug("PWTerminalReprintResp"+resp);
				pw.write(resp);
			}
		}catch(Exception e){
			logger.debug("Some Error occured in PW "+new LMSException(e).getMessage());
		}
	}
	
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
		
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}
	public String getAgentid() {
		return agentid;
	}
	public void setAgentid(String agentid) {
		this.agentid = agentid;
	}
	public String getOperatorcode() {
		return operatorcode;
	}
	public void setOperatorcode(String operatorcode) {
		this.operatorcode = operatorcode;
	}
	public String getCircode() {
		return circode;
	}
	public void setCircode(String circode) {
		this.circode = circode;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public double getDenomination() {
		return denomination;
	}
	public void setDenomination(double denomination) {
		this.denomination = denomination;
	}
	public double getRechargeAmt() {
		return rechargeAmt;
	}
	public void setRechargeAmt(double rechargeAmt) {
		this.rechargeAmt = rechargeAmt;
	}
	public int getBulkqty() {
		return bulkqty;
	}
	public void setBulkqty(int bulkqty) {
		this.bulkqty = bulkqty;
	}
	public String getNarration() {
		return narration;
	}
	public void setNarration(String narration) {
		this.narration = narration;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getCSRefTransId() {
		return CSRefTransId;
	}
	public void setCSRefTransId(String cSRefTransId) {
		CSRefTransId = cSRefTransId;
	}
	
	public long getLastTransId() {
		return lastTransId;
	}

	public void setLastTransId(long lastTransId) {
		this.lastTransId = lastTransId;
	}

	public static void main(String args[]) throws Exception{
 
		CSPWSaleHelper helper = new CSPWSaleHelper();
		//System.out.println(helper.fetchReprintLastTrans(5, "1", "", "LIVE", "3.33"));
	}
}
