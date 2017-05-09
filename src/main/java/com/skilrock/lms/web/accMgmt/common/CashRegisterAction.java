package com.skilrock.lms.web.accMgmt.common;

import java.io.PrintWriter;
import java.util.*;

import javax.servlet.http.*;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.coreEngine.accMgmt.common.CashRegisterHelper;

public class CashRegisterAction extends ActionSupport implements ServletRequestAware,ServletResponseAware{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private int drawerName;
	private int drawerId;
	private int cashierName;
	private double retAmout;
	//private String[] denoType;
	private String[] multiples;
	private String createDrawerName;
	private String remarks;
	private String type;

	
	public void getCashierList()
	{
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
			CashRegisterHelper drawerHelper = new CashRegisterHelper();
			Map<Integer,String> cashierDetails = drawerHelper.getCashierDetails();
			pw.print(cashierDetails.toString().replace("{", "").replace("}", ""));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void getDrawerList()
	{
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
			CashRegisterHelper drawerHelper = new CashRegisterHelper();
			Map<Integer,String> drawerDetails = drawerHelper.getDrawerDetails();
			pw.print(drawerDetails.toString().replace("{", "").replace("}", ""));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String assignDrawerToCashier()
	{
		HttpSession session = getRequest().getSession();
		UserInfoBean userBean = (UserInfoBean) session.getAttribute("USER_INFO");
		CashRegisterHelper drawerHelper = new CashRegisterHelper();
		List<String> denoList = drawerHelper.getReceivedDenoList();
		String[] denoType = (String[]) denoList.toArray(new String[denoList.size()]);
		drawerHelper.assignDrawer(cashierName,drawerName,userBean.getUserId(),multiples,denoType);
		return SUCCESS;
	}
	public void checkDrawerAvailability()
	{
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
			CashRegisterHelper drawerHelper = new CashRegisterHelper();
			String isDrawerAssigned = drawerHelper.checkDrawerAvailability(drawerName);
			pw.print(isDrawerAssigned);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void checkDrawerExistance()
	{
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
			CashRegisterHelper drawerHelper = new CashRegisterHelper();
			String isDrawerExist = drawerHelper.checkDrawerExistance(createDrawerName);
				pw.print(isDrawerExist);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String clearDrawer()
	{
		try {
			HttpSession session = getRequest().getSession();
			UserInfoBean userBean = (UserInfoBean) session
					.getAttribute("USER_INFO");
			CashRegisterHelper drawerHelper = new CashRegisterHelper();
			drawerHelper.clearDrawer(drawerId,userBean.getUserId());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String createDrawer()
	{
		try {
			CashRegisterHelper drawerHelper = new CashRegisterHelper();
			List<String> denoList = drawerHelper.getReceivedDenoList();
			String[] denoArray = (String[]) denoList.toArray(new String[denoList.size()]);
				drawerHelper.createDrawer(createDrawerName, remarks, denoArray);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return SUCCESS;
	}
	
	public void addRetAmountDenomination()
	{
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
			HttpSession session = getRequest().getSession();
			UserInfoBean userBean = (UserInfoBean) session
					.getAttribute("USER_INFO");
			CashRegisterHelper drawerHelper = new CashRegisterHelper();
			String denoList = drawerHelper.getReturnDenoList(
					Math.abs(retAmout), userBean.getUserId());
			pw.print(denoList.toString().replace("{", "").replace("}", ""));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addRecAmountDenomination()
	{
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
			HttpSession session = getRequest().getSession();
			UserInfoBean userBean = (UserInfoBean) session
					.getAttribute("USER_INFO");
			CashRegisterHelper drawerHelper = new CashRegisterHelper();
			if (type==null || !type.equalsIgnoreCase("EXCHANGE")) {
				List<String> denoList = drawerHelper.getReceivedDenoList();
				pw.print(denoList.toString().replace("[", "").replace("]", ""));
			} else {
				String denoResult = drawerHelper.getExchangeDenoList(userBean);
				pw.print(denoResult);
				//pw.print(denoList.toString().replace("[", "").replace("]", ""));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public void setServletRequest(HttpServletRequest req) {
		request = req;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse res) {
		response = res;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	/*public String[] getDenoType() {
		return denoType;
	}

	public void setDenoType(String[] denoType) {
		this.denoType = denoType;
	}*/

	public String[] getMultiples() {
		return multiples;
	}
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getCreateDrawerName() {
		return createDrawerName;
	}

	public void setCreateDrawerName(String createDrawerName) {
		this.createDrawerName = createDrawerName;
	}

	public String execute() {
		return SUCCESS;
	}

	public int getDrawerId() {
		return drawerId;
	}

	public void setDrawerId(int drawerId) {
		this.drawerId = drawerId;
	}

	public void setMultiples(String[] multiples) {
		this.multiples = multiples;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public int getDrawerName() {
		return drawerName;
	}

	public void setDrawerName(int drawerName) {
		this.drawerName = drawerName;
	}

	public int getCashierName() {
		return cashierName;
	}

	public void setCashierName(int cashierName) {
		this.cashierName = cashierName;
	}

	public double getRetAmout() {
		return retAmout;
	}

	public void setRetAmout(double retAmout) {
		this.retAmout = retAmout;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
