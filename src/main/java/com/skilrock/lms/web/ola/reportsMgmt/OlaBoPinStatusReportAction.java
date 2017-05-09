package com.skilrock.lms.web.ola.reportsMgmt;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.ActionMessage;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.OlaPinStatusBean;
import com.skilrock.lms.coreEngine.ola.reportMgmt.OlaBoPinStatusReportHelper;
/**
 * This class provide method to fetch pin status record and set these records in ArrayList
 * @author Neeraj Jain
 *
 */
public class OlaBoPinStatusReportAction extends ActionSupport implements ServletRequestAware,ServletResponseAware {
	private static final long serialVersionUID = 1L;
	private String walletName ;
	private String distributorType;
	private int denoType;
	private ArrayList<OlaPinStatusBean> pinStatusBeanList = new ArrayList();
	
	public String fetchPinStatus(){
		int walletId=0;
		OlaBoPinStatusReportHelper helper = new OlaBoPinStatusReportHelper();
		if (walletName.equalsIgnoreCase("null")) {
			addActionMessage("Wallet Id Cannot Be NULL");
			return ERROR;
		}else if(walletName.equalsIgnoreCase("-1")){
			walletId=-1;// For All Wallet
		}else {
				String[] walletArr = walletName.split(":");
				walletId = Integer.parseInt(walletArr[0]);
				
		}
		setPinStatusBeanList(helper.pinStatusData(walletId,distributorType,pinStatusBeanList,denoType));// set pinStatusBeanList value
		return SUCCESS;
	}

	@Override
	public void setServletRequest(HttpServletRequest arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setServletResponse(HttpServletResponse arg0) {
		// TODO Auto-generated method stub
		
	}

	public String getWalletName() {
		return walletName;
	}

	public void setWalletName(String walletName) {
		this.walletName = walletName;
	}

	public String getDistributorType() {
		return distributorType;
	}

	public void setDistributorType(String distributorType) {
		this.distributorType = distributorType;
	}

	public int getDenoType() {
		return denoType;
	}

	public void setDenoType(int denoType) {
		this.denoType = denoType;
	}

	public ArrayList<OlaPinStatusBean> getPinStatusBeanList() {
		return pinStatusBeanList;
	}

	public void setPinStatusBeanList(ArrayList<OlaPinStatusBean> pinStatusBeanList) {
		this.pinStatusBeanList = pinStatusBeanList;
	}

}
