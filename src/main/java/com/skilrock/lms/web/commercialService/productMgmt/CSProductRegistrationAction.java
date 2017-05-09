package com.skilrock.lms.web.commercialService.productMgmt;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.CSProductBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.commercialService.productMgmt.CSProductRegistrationHelper;

public class CSProductRegistrationAction extends ActionSupport implements ServletRequestAware, ServletResponseAware {
	/**
	 * Default Serial Version Id
	 */
	private static final long serialVersionUID = 1L;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private HttpSession session;
	private int catId;
	private String productCode;
	private String productDesc;
	private String operatorCode;
	private String circleCode;
	private double denomination;
	private double talktime;
	private String validity;
	private double adminFee;
	private double serviceTax;
	private String supplierName;
	private double unitPrice;
	private double retailerComm;
	private double agentComm;
	private double jvComm;
	private double goodCauseAmt;
	private double vatAmt;
	private String rechargeInst;
	
	public void fetchProductCategories()throws IOException, LMSException{
		PrintWriter out = getResponse().getWriter();
		CSProductRegistrationHelper helper = new CSProductRegistrationHelper();
		getResponse().setContentType("text/html");
		out.print(helper.getActiveProductCategories());
	}
	
	public void fetchOperators()throws IOException, LMSException{
		PrintWriter out = getResponse().getWriter();
		CSProductRegistrationHelper helper = new CSProductRegistrationHelper();
		getResponse().setContentType("text/html");
		out.print(helper.getActiveOperators());
	}
	
	public void fetchCircles()throws IOException, LMSException{
		PrintWriter out = getResponse().getWriter();
		CSProductRegistrationHelper helper = new CSProductRegistrationHelper();
		getResponse().setContentType("text/html");
		out.print(helper.getActiveCircles());
	}
	
	public String submitProduct(){
		session = getRequest().getSession();
		CSProductRegistrationHelper helper = new CSProductRegistrationHelper();
		CSProductBean prodBean = new CSProductBean();
		prodBean.setProductCode(productCode);
		prodBean.setCategoryId(catId);
		prodBean.setDesc(productDesc);
		prodBean.setOperatorCode(operatorCode);
		prodBean.setCircleCode(circleCode);
		prodBean.setDenomination(denomination);
		prodBean.setTalktime(talktime);
		prodBean.setValidity(validity);
		prodBean.setAdminFee(adminFee);
		prodBean.setServiceTax(serviceTax);
		prodBean.setSupplierName(supplierName);
		prodBean.setUnitPrice(unitPrice);
		prodBean.setRetailerComm(retailerComm);
		prodBean.setAgentComm(agentComm);
		prodBean.setJvComm(jvComm);
		prodBean.setGoodCause(goodCauseAmt);
		prodBean.setVat(vatAmt);
		prodBean.setRechargeInstruction(rechargeInst);
		
		prodBean = helper.registerProductInDb(prodBean);
		if("DUP_PROD_NUM".equalsIgnoreCase(prodBean.getStatus())){
			session.setAttribute("PROD_REG_STAT",prodBean.getStatus());
			return SUCCESS;
		}else if("SUCCESS".equalsIgnoreCase(prodBean.getStatus())){
			return SUCCESS;
		}else{
			return ERROR;
		}
	}
	
	
	public String getRechargeInst() {
		return rechargeInst;
	}

	public void setRechargeInst(String rechargeInst) {
		this.rechargeInst = rechargeInst;
	}

	public String getOperatorCode() {
		return operatorCode;
	}

	public void setOperatorCode(String operatorCode) {
		this.operatorCode = operatorCode;
	}

	public String getCircleCode() {
		return circleCode;
	}

	public void setCircleCode(String circleCode) {
		this.circleCode = circleCode;
	}

	public double getDenomination() {
		return denomination;
	}
	
	public double getTalktime() {
		return talktime;
	}

	public void setTalktime(double talktime) {
		this.talktime = talktime;
	}
	
	public String getValidity() {
		return validity;
	}

	public void setValidity(String validity) {
		this.validity = validity;
	}
	
	public double getAdminFee() {
		return adminFee;
	}

	public void setAdminFee(double adminFee) {
		this.adminFee = adminFee;
	}

	public double getServiceTax() {
		return serviceTax;
	}

	public void setServiceTax(double serviceTax) {
		this.serviceTax = serviceTax;
	}

	public void setDenomination(double denomination) {
		this.denomination = denomination;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}
	public HttpServletRequest getRequest() {
		return request;
	}
	
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}
	public HttpServletResponse getResponse() {
		return response;
	}

	public int getCatId() {
		return catId;
	}

	public void setCatId(int catId) {
		this.catId = catId;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductDesc() {
		return productDesc;
	}

	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public double getRetailerComm() {
		return retailerComm;
	}

	public void setRetailerComm(double retailerComm) {
		this.retailerComm = retailerComm;
	}

	public double getAgentComm() {
		return agentComm;
	}

	public void setAgentComm(double agentComm) {
		this.agentComm = agentComm;
	}

	public double getJvComm() {
		return jvComm;
	}

	public void setJvComm(double jvComm) {
		this.jvComm = jvComm;
	}

	public double getGoodCauseAmt() {
		return goodCauseAmt;
	}

	public void setGoodCauseAmt(double goodCauseAmt) {
		this.goodCauseAmt = goodCauseAmt;
	}

	public void setVatAmt(double vatAmt) {
		this.vatAmt = vatAmt;
	}

	public double getVatAmt() {
		return vatAmt;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public double getUnitPrice() {
		return unitPrice;
	}
	
	
}
