package com.skilrock.lms.web.scratchService.inventoryMgmt.common;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork2.ModelDriven;
import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.scratchService.inventoryMgmt.beans.WarehouseRegBean;
import com.skilrock.lms.coreEngine.scratchService.inventoryMgmt.common.WarehouseRegHelper;
import com.skilrock.lms.coreEngine.userMgmt.common.CountryOrgHelper;
import com.skilrock.lms.web.drawGames.common.Util;
import com.skilrock.lms.web.userMgmt.common.CountryOrgAction;

public class WarehouseRegAction extends BaseAction implements ModelDriven<WarehouseRegBean>{
	Log logger = LogFactory.getLog(WarehouseRegAction.class);
	
	WarehouseRegBean regBean = new WarehouseRegBean() ;
	public static final String APPLICATION_ERROR = "applicationError";
	
	
	public WarehouseRegAction() {
		super(WarehouseRegAction.class);
	}

	public String execute() throws Exception
	{
		logger.info("Inside Warehouse Action");
		CountryOrgHelper country = new CountryOrgHelper();
		List countryList = country.getCountry();
		
		HttpSession session = request.getSession();
		session.setAttribute("list", countryList);
		session.setAttribute("boUsers", Util.fetchAllBOUsers());
		
		return SUCCESS ;
	}
	
	public String registerWarehouse() throws LMSException
	{
		logger.info("Warehouse Name : " + regBean.getWhName()) ;
		logger.info("Owner Id : " + regBean.getOwnerId()) ;
		logger.info("Address 1 : " + regBean.getAddrLine1()) ;
		logger.info("Address 2 : " + regBean.getAddrLine2()) ;
		logger.info("City : " + regBean.getCity()) ;
		logger.info("State : " + regBean.getState()) ;
		logger.info("Country : " + regBean.getCountry()) ;
		logger.info("Warehouse Type : " + regBean.getWarehouseType()) ;
		String status = "";
		try{
		status = WarehouseRegHelper.registerWarehouse(regBean);
		}catch(Exception e){
			return APPLICATION_ERROR ;
		}
		
		if("success".equalsIgnoreCase(status)){
			HttpSession session = request.getSession();
			session.setAttribute("NEW_WH_REG", regBean.getWhName()) ;
			return SUCCESS ;
		}
		
		if("failure".equalsIgnoreCase(status))
			addActionError("Warehouse "+regBean.getWhName()+" Already Exists");
		
		
		
		return ERROR ;
	}

	

	@Override
	public WarehouseRegBean getModel() {
		return regBean;
	}
	
	
	
	
}
