package com.skilrock.lms.common.utility;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import rng.RNGUtilities;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skilrock.lms.admin.SetResetUserPasswordAction;
import com.skilrock.lms.api.lms.AnyType2AnyTypeMap;
import com.skilrock.lms.beans.HistoryBean;
import com.skilrock.lms.beans.ServiceRequest;
import com.skilrock.lms.beans.ServiceResponse;
import com.skilrock.lms.beans.UserIdMappingBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.MyTextProvider;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.coreEngine.ola.SendSMS;
import com.skilrock.lms.coreEngine.service.IServiceDelegate;
import com.skilrock.lms.coreEngine.service.ServiceDelegate;
import com.skilrock.lms.coreEngine.service.dge.ServiceMethodName;
import com.skilrock.lms.coreEngine.service.dge.ServiceName;
import com.skilrock.lms.dge.beans.DrawIdBean;
import com.skilrock.lms.dge.beans.PWTDrawBean;
import com.skilrock.lms.dge.beans.PanelIdBean;
import com.skilrock.lms.web.drawGames.common.Util;

public class CommonMethods{
	
	static MyTextProvider textProvider = new MyTextProvider();  

	public static CommonMethods instance;

	public static CommonMethods getSingleInstance() {
	    if (instance == null) {
	      synchronized (CommonMethods.class) {
	        if (instance == null) {
	        	instance = new CommonMethods();
	        }
	      }
	    }
	    return instance;
	  }

	final static long oneDay = 1 * 24 * 60 * 60 * 1000;
	static Log logger = LogFactory.getLog(CommonMethods.class);

	public static double calculateDrawGameVatAgt(double mrpAmt,
			double agtSaleCommRate, double prizePayOutRatio, double govtComm,
			double vat) throws LMSException {
		String countryDeployed =Utility.getPropertyValue("COUNTRY_DEPLOYED");
		double vatAmt = 0.0;
		if ("ZIMBABWE".equalsIgnoreCase(countryDeployed)) {
			vatAmt = (mrpAmt - mrpAmt * (prizePayOutRatio + govtComm) / 100)
					* vat * 0.01 / (1 + vat * 0.01);

		} else if ("KENYA".equalsIgnoreCase(countryDeployed)) {
			// NO VAT
		} else if ("NIGERIA".equalsIgnoreCase(countryDeployed)) {
			vatAmt = mrpAmt * vat * 0.01;
		} else if ("TANZANIA".equalsIgnoreCase(countryDeployed)) {
			vatAmt = mrpAmt * vat * 0.01;
		}else if("GHANA".equalsIgnoreCase(countryDeployed)){
			vatAmt = mrpAmt * vat * 0.01;
		} else if("SAFARIBET".equalsIgnoreCase(countryDeployed)){
			vatAmt = mrpAmt * vat * 0.01;
		} else if ("BENIN".equalsIgnoreCase(countryDeployed)) {
			vatAmt = mrpAmt * vat * 0.01;
		} else if ("SIKKIM".equalsIgnoreCase(countryDeployed)) {
			vatAmt = mrpAmt * vat * 0.01;
		}else if ("PHILIP".equalsIgnoreCase(countryDeployed)) {
			vatAmt = mrpAmt * vat * 0.01;
		} else {
			throw new LMSException();
		}
		return vatAmt;
	}

	public static double roundDrawTktAmt(double mrp) {
		if(mrp<=10.0){
		return (double)(long)(mrp * 2 + 0.5) / 2;
		}else{
		return (double)(long)mrp;	
		}
	}

	public static double calculateDrawGameVatPlr(double mrpAmt,
			double plrSaleCommRate, double prizePayOutRatio, double govtComm,
			double vat) throws LMSException {
		double vatAmt = 0.0;
		String countryDeployed =Utility.getPropertyValue("COUNTRY_DEPLOYED");
		if ("ZIMBABWE".equalsIgnoreCase(countryDeployed)) {
			vatAmt = (mrpAmt - mrpAmt * (prizePayOutRatio + govtComm) / 100)
					* vat * 0.01 / (1 + vat * 0.01);

		} else if ("KENYA".equalsIgnoreCase(countryDeployed)) {
			// NO VAT
		} else if ("NIGERIA".equalsIgnoreCase(countryDeployed)) {
			vatAmt = mrpAmt * vat * 0.01;
		}
		else if ("TANZANIA".equalsIgnoreCase(countryDeployed)) {
			vatAmt = mrpAmt * vat * 0.01;
		}else if("GHANA".equalsIgnoreCase(countryDeployed)){
			vatAmt = mrpAmt * vat * 0.01;
		}else if("SAFARIBET".equalsIgnoreCase(countryDeployed)){
			vatAmt = mrpAmt * vat * 0.01;
		}else if ("BENIN".equalsIgnoreCase(countryDeployed)) {
			vatAmt = mrpAmt * vat * 0.01;
		}else if ("SIKKIM".equalsIgnoreCase(countryDeployed)) {
			vatAmt = mrpAmt * vat * 0.01;
		}else if ("PHILIP".equalsIgnoreCase(countryDeployed)) {
			vatAmt = mrpAmt * vat * 0.01;
		}else {		
			throw new LMSException();
		}
		return vatAmt;
	}

	public static double calculateDrawGameVatRet(double mrpAmt,
			double retailerSaleCommRate, double prizePayOutRatio,
			double govtComm, double vat) throws LMSException {
		double vatAmt = 0.0;
		String countryDeployed =Utility.getPropertyValue("COUNTRY_DEPLOYED");
		if ("ZIMBABWE".equalsIgnoreCase(countryDeployed)) {
			vatAmt = (mrpAmt - mrpAmt * (prizePayOutRatio + govtComm) / 100)
					* vat * 0.01 / (1 + vat * 0.01);

		} else if ("KENYA".equalsIgnoreCase(countryDeployed)) {
			// NO VAT
		} else if ("NIGERIA".equalsIgnoreCase(countryDeployed)) {
			vatAmt = mrpAmt * vat * 0.01;
		} else if ("TANZANIA".equalsIgnoreCase(countryDeployed)) {
			vatAmt = mrpAmt * vat * 0.01;
		}else if("GHANA".equalsIgnoreCase(countryDeployed)){
			vatAmt = mrpAmt * vat * 0.01;
		}else if("SAFARIBET".equalsIgnoreCase(countryDeployed)){
			vatAmt = mrpAmt * vat * 0.01;
		}else if ("BENIN".equalsIgnoreCase(countryDeployed)) {
			vatAmt = mrpAmt * vat * 0.01;
		}else if ("SIKKIM".equalsIgnoreCase(countryDeployed)) {
			vatAmt = mrpAmt * vat * 0.01;
		}else if ("PHILIP".equalsIgnoreCase(countryDeployed)) {
			vatAmt = mrpAmt * vat * 0.01;
		} else {
			throw new LMSException();
		}
		return vatAmt;
	}

	public static double calculateVat(double mrpAmt, double agentSaleCommRate,
			double prizePayOutRatio, double govtComm, double vat,
			String govtCommRule, double fixedAmt, Long ticketsInScheme)
			throws LMSException {
		double vatAmt = 0.0;
		String countryDeployed =Utility.getPropertyValue("COUNTRY_DEPLOYED");
		if ("ZIMBABWE".equalsIgnoreCase(countryDeployed)) {
			if (govtCommRule.equals("FIXED_PER")) {
				vatAmt = (mrpAmt - mrpAmt * (prizePayOutRatio + govtComm) / 100)
						* vat * 0.01 / (1 + vat * 0.01);
			} else if (govtCommRule.equals("MIN_PROFIT")) {
				vatAmt = fixedAmt / mrpAmt * ticketsInScheme;
			}

		} else if ("KENYA".equalsIgnoreCase(countryDeployed)) {
			// No VAT
		} else if ("NIGERIA".equalsIgnoreCase(countryDeployed)) {
			vatAmt = mrpAmt * vat * 0.01;

		} else if ("TANZANIA".equalsIgnoreCase(countryDeployed)) {
			vatAmt = mrpAmt * vat * 0.01;

		}else if("GHANA".equalsIgnoreCase(countryDeployed)){
			vatAmt = mrpAmt * vat * 0.01;
		}else if("SAFARIBET".equalsIgnoreCase(countryDeployed)){
			vatAmt = mrpAmt * vat * 0.01;
		}else if ("BENIN".equalsIgnoreCase(countryDeployed)) {
			vatAmt = mrpAmt * vat * 0.01;
		}else if ("SIKKIM".equalsIgnoreCase(countryDeployed)) {
			vatAmt = mrpAmt * vat * 0.01;
		}else if ("PHILIP".equalsIgnoreCase(countryDeployed)) {
			vatAmt = mrpAmt * vat * 0.01;
		}else {
			throw new LMSException();
		}
		return vatAmt;
	}

	public static double calculateVatAgent(double mrpAmt,
			double retailerSaleCommRate, double prizePayOutRatio,
			double govtComm, double vat, String govtCommRule, double fixedAmt,
			Long ticketsInScheme) throws LMSException {
		double vatAmt = 0.0;
		String countryDeployed =Utility.getPropertyValue("COUNTRY_DEPLOYED");
		if ("ZIMBABWE".equalsIgnoreCase(countryDeployed)) {
			if (govtCommRule.equals("FIXED_PER")) {
				vatAmt = (mrpAmt - mrpAmt * (prizePayOutRatio + govtComm) / 100)
						* vat * 0.01 / (1 + vat * 0.01);
			} else if (govtCommRule.equals("MIN_PROFIT")) {
				vatAmt = fixedAmt / mrpAmt * ticketsInScheme;
			}

		} else if ("KENYA".equalsIgnoreCase(countryDeployed)) {
			// No VAT
		} else if ("NIGERIA".equalsIgnoreCase(countryDeployed)) {
			vatAmt = mrpAmt * vat * 0.01;

		}else if ("TANZANIA".equalsIgnoreCase(countryDeployed)) {
			vatAmt = mrpAmt * vat * 0.01;

		}else if("GHANA".equalsIgnoreCase(countryDeployed)){
			vatAmt = mrpAmt * vat * 0.01;
		}else if("SAFARIBET".equalsIgnoreCase(countryDeployed)){
			vatAmt = mrpAmt * vat * 0.01;
		}else if ("BENIN".equalsIgnoreCase(countryDeployed)) {
			vatAmt = mrpAmt * vat * 0.01;
		}else if ("SIKKIM".equalsIgnoreCase(countryDeployed)) {
			vatAmt = mrpAmt * vat * 0.01;
		}else if ("PHILIP".equalsIgnoreCase(countryDeployed)) {
			vatAmt = mrpAmt * vat * 0.01;
		} else {
			throw new LMSException();
		}

		return vatAmt;
	}

	public static double calDrawGameTaxableSale(double ticketMrp,
			double saleCommRate, double prizePayOutRatio, double govtComm,
			double vat) throws LMSException {
		double taxableSale = 0.0;
		String countryDeployed =Utility.getPropertyValue("COUNTRY_DEPLOYED");
		if ("ZIMBABWE".equalsIgnoreCase(countryDeployed)) {
			taxableSale = ticketMrp * (100 - (prizePayOutRatio + govtComm))
					/ 100 * 100 / (100 + vat);
		} else if ("KENYA".equalsIgnoreCase(countryDeployed)) {
			taxableSale = ticketMrp;
		} else if ("NIGERIA".equalsIgnoreCase(countryDeployed)) {

			taxableSale = ticketMrp;
		}else if ("TANZANIA".equalsIgnoreCase(countryDeployed)) {

			taxableSale = ticketMrp;
		}else if("GHANA".equalsIgnoreCase(countryDeployed)){
			taxableSale = ticketMrp;
		}else if("SAFARIBET".equalsIgnoreCase(countryDeployed)){
			taxableSale = ticketMrp;
		}else if ("BENIN".equalsIgnoreCase(countryDeployed)) {
			taxableSale = ticketMrp;
		}else if ("SIKKIM".equalsIgnoreCase(countryDeployed)) {
			taxableSale = ticketMrp;
		}else if ("PHILIP".equalsIgnoreCase(countryDeployed)) {
			taxableSale = ticketMrp;
		}  else {
			throw new LMSException();
		}
		return taxableSale;
	}

	public static double calScratchTaxableSale(double ticketMrp,
			double saleCommRate, double prizePayOutRatio, double govtComm,
			double vat) throws LMSException {
		double taxableSale = 0.0;
		String countryDeployed =Utility.getPropertyValue("COUNTRY_DEPLOYED");
		if ("ZIMBABWE".equalsIgnoreCase(countryDeployed)) {
			taxableSale = ticketMrp * (100 - (prizePayOutRatio + govtComm))
					/ 100 * 100 / (100 + vat);
		} else if ("KENYA".equalsIgnoreCase(countryDeployed)) {
			taxableSale = ticketMrp;
		} else if ("NIGERIA".equalsIgnoreCase(countryDeployed)) {

			taxableSale = ticketMrp;
		}else if ("TANZANIA".equalsIgnoreCase(countryDeployed)) {

			taxableSale = ticketMrp;
		}else if("GHANA".equalsIgnoreCase(countryDeployed)){
			taxableSale = ticketMrp;
		}else if("SAFARIBET".equalsIgnoreCase(countryDeployed)){
			taxableSale = ticketMrp;
		}else if ("BENIN".equalsIgnoreCase(countryDeployed)) {
			taxableSale = ticketMrp;
		}else if ("SIKKIM".equalsIgnoreCase(countryDeployed)) {
			taxableSale = ticketMrp;
		}else if ("PHILIP".equalsIgnoreCase(countryDeployed)) {
			taxableSale = ticketMrp;
		} else {
			throw new LMSException();
		}
		return taxableSale;
	}

	public static double calTaxableSale(double ticketMrp, double saleCommRate,
			double prizePayOutRatio, double govtComm, double vat)
			throws LMSException {
		double taxableSale = 0.0;
		String countryDeployed =Utility.getPropertyValue("COUNTRY_DEPLOYED");
		if ("ZIMBABWE".equalsIgnoreCase(countryDeployed)) {
			taxableSale = ticketMrp * (100 - (prizePayOutRatio + govtComm))
					/ 100 * 100 / (100 + vat);
		} else if ("KENYA".equalsIgnoreCase(countryDeployed)) {
			taxableSale = ticketMrp;
		} else if ("NIGERIA".equalsIgnoreCase(countryDeployed)) {
			taxableSale = ticketMrp;
		}else if ("TANZANIA".equalsIgnoreCase(countryDeployed)) {

			taxableSale = ticketMrp;
		}else if ("GHANA".equalsIgnoreCase(countryDeployed)) {
			taxableSale = ticketMrp;
		}else if ("SAFARIBET".equalsIgnoreCase(countryDeployed)) {
			taxableSale = ticketMrp;
		}else if ("BENIN".equalsIgnoreCase(countryDeployed)) {
			taxableSale = ticketMrp;
		}else if ("SIKKIM".equalsIgnoreCase(countryDeployed)) {
			taxableSale = ticketMrp;
		}else if ("PHILIP".equalsIgnoreCase(countryDeployed)) {
			taxableSale = ticketMrp;
		} else {
			throw new LMSException();
		}
		return taxableSale;
	}
//@ amit : added for credit note
	
	public static String changeCrNoteBalRet(double creAmt,int agtOrgId) {
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String selectQuery = null;
		double retMaxBalLimit = 0.0;
		String errMsg = "";
		try {
				selectQuery = "select ifnull(slom.available_credit-slom.claimable_bal,0)-ifnull(retbal.bal,0) as totalbal from st_lms_organization_master slom,(select ifnull(sum(if(available_credit-claimable_bal>0,available_credit-claimable_bal,0)),0) as bal from st_lms_organization_master where parent_id="
						+ agtOrgId
						+ ") as retbal where slom.organization_id="
						+ agtOrgId;
				pstmt = con.prepareStatement(selectQuery);

				System.out.println("Bal Query::" + pstmt);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					retMaxBalLimit = rs.getDouble("totalbal");
				}

				if (creAmt <= retMaxBalLimit) {
					errMsg = "TRUE";
				} else if (retMaxBalLimit <= 0) {
					errMsg = "Agent of this Retailer have insufficient Credit Amount";
				} else {
					errMsg = "You Can Increase Only "
							+ FormatNumber.formatNumberForJSP(retMaxBalLimit)
							+ " Amount ";
				}
		

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return errMsg;
	}
	public static String changeCreditLimitRet(int retOrgId, double crLimit,
			String type) {
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String selectQuery = null;
		double retMaxBalLimit = 0.0;
		String errMsg = "";
		double retCrLimit = 0;
		int agtOrgId = 0;
		boolean checkType = "XCL".equals(type);
		try {
			selectQuery = "select credit_limit,parent_id,extended_credit_limit from st_lms_organization_master where organization_id="
					+ retOrgId;
			pstmt = con.prepareStatement(selectQuery);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				retCrLimit = checkType ? rs.getDouble("extended_credit_limit")
						: rs.getDouble("credit_limit");
				agtOrgId = rs.getInt("parent_id");
			}
			System.out.println("--------retCrLimit" + retCrLimit
					+ "--------crLimit" + crLimit);
			if(crLimit<0.0){
				errMsg = "Can't  Decrease More than Retailer's limit";
				return errMsg;
			}
			if (crLimit > retCrLimit) {
				selectQuery = "select ifnull(slom.available_credit-slom.claimable_bal,0)-ifnull(retbal.bal,0) as totalbal from st_lms_organization_master slom,(select ifnull(sum(if(available_credit-claimable_bal>0,available_credit-claimable_bal,0)),0) as bal from st_lms_organization_master where parent_id="
						+ agtOrgId
						+ ") as retbal where slom.organization_id="
						+ agtOrgId;
				pstmt = con.prepareStatement(selectQuery);

				System.out.println("Bal Query::" + pstmt);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					retMaxBalLimit = rs.getDouble("totalbal");
				}

				if (crLimit - retCrLimit <= retMaxBalLimit) {
					errMsg = "TRUE";
				} else if (retMaxBalLimit <= 0) {
					errMsg = "Agent of this Retailer have insufficient Credit Amount";
				} else {
					errMsg = "You Can Increase Only "
							+ FormatNumber.formatNumberForJSP(retMaxBalLimit)
							+ " Amount ";
				}
			} else {
				errMsg = "TRUE";
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return errMsg;
	}

	public static String chkCreditLimitAgt(int agtOrgId, double bal,Connection con)throws LMSException {
		//Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String selectQuery = null;
		double retMaxBalLimit = 0.0;
		String errMsg = "";
		try {
			selectQuery = "select ifnull(slom.available_credit-slom.claimable_bal,0)-ifnull(retbal.bal,0) as totalbal from st_lms_organization_master slom,(select sum(retBal.retBal) as bal from (select if(available_credit-claimable_bal>0,available_credit-claimable_bal,0) as retBal from st_lms_organization_master where parent_id="
					+ agtOrgId
					+ ") as retBal) as retbal where slom.organization_id="
					+ agtOrgId;
			pstmt = con.prepareStatement(selectQuery);

			logger.info("Bal Query::" + pstmt);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				retMaxBalLimit = rs.getDouble("totalbal");
			}
			logger.info("bal" + bal + "   retMaxBalLimit" + retMaxBalLimit);
			if (bal <= retMaxBalLimit) {
				errMsg = "TRUE";
			} else if (retMaxBalLimit <= 0) {
				errMsg = textProvider.getText("msg.u.cannot.acpt.anyAmt");
			} else {
				errMsg = textProvider.getText("msg.u.can.acpt")+" "
						+ FormatNumber.formatPDFNumbers(retMaxBalLimit)
						+ textProvider.getText("msg.amt.only");
			}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return errMsg;
	}
	
	public static String chkCreditLimitAgt(int agtOrgId, double bal) {
		Connection con = DBConnect.getConnection();
		/*PreparedStatement pstmt = null;
		ResultSet rs = null;
		String selectQuery = null;
		double retMaxBalLimit = 0.0;*/
		String errMsg = "";
		try {/*
			selectQuery = "select ifnull(slom.available_credit-slom.claimable_bal,0)-ifnull(retbal.bal,0) as totalbal from st_lms_organization_master slom,(select sum(retBal.retBal) as bal from (select if(available_credit-claimable_bal>0,available_credit-claimable_bal,0) as retBal from st_lms_organization_master where parent_id="
					+ agtOrgId
					+ ") as retBal) as retbal where slom.organization_id="
					+ agtOrgId;
			pstmt = con.prepareStatement(selectQuery);

			logger.info("Bal Query::" + pstmt);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				retMaxBalLimit = rs.getDouble("totalbal");
			}
			logger.info("bal" + bal + "   retMaxBalLimit" + retMaxBalLimit);
			if (bal <= retMaxBalLimit) {
				errMsg = "TRUE";
			} else if (retMaxBalLimit <= 0) {
				errMsg = "You Cannot Accept Any Amount";
			} else {
				errMsg = "You Can Accept "
						+ FormatNumber.formatPDFNumbers(retMaxBalLimit)
						+ " Amount Only";
			}
		*/
			errMsg=chkCreditLimitAgt(agtOrgId, bal, con);
			
		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return errMsg;
	}

	public synchronized static String convertDateInGlobalFormat(String date,
			String format, String returnFormat) {
		String formatParser = findParserString(format);
		String[] formatArr = format.split(formatParser);
		logger.debug(formatArr[0] + formatParser + formatArr[1] + formatParser
				+ formatArr[2]);
		String[] dateArr = date.split(formatParser);
		logger.debug(dateArr[0] + formatParser + dateArr[1] + formatParser
				+ dateArr[2]);
		String returnFormatParser = findParserString(returnFormat);
		String[] returnFormatArr = returnFormat.split(returnFormatParser);
		logger.debug(returnFormatArr[0] + returnFormatParser
				+ returnFormatArr[1] + returnFormatParser + returnFormatArr[2]);
		String finalDate[] = new String[3];

		String arr[] = new String[] { "dd", "mm", "yy" };
		if (formatArr.length == dateArr.length) {
			for (String element : arr) {
				int formatArrIndex = -1;
				int returnFormatArrIndex = -1;

				for (int i = 0; formatArr.length > i; i++) {
					if (formatArr[i].toLowerCase().indexOf(element) != -1) {
						formatArrIndex = i;
						break;
					}
				}
				for (int i = 0; returnFormatArr.length > i; i++) {
					if (returnFormatArr[i].toLowerCase().indexOf(element) != -1) {
						returnFormatArrIndex = i;
						break;
					}
				}

				finalDate[returnFormatArrIndex] = dateArr[formatArrIndex];
				// logger.debug(returnFormatArrIndex+"
				// "+finalDate[returnFormatArrIndex]);
			}
		}
		logger.debug("date converted in  === "
				+ (finalDate[0] + returnFormatParser + finalDate[1]
						+ returnFormatParser + finalDate[2]));
		return finalDate[0] + returnFormatParser + finalDate[1]
				+ returnFormatParser + finalDate[2];
	}

	public static String fetchReprintCount(int gameNo, String ticketNo) {
		System.out.println("inside the fetchReprintCount() method...");
		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.DRAWGAME);
		sReq.setServiceMethod(ServiceMethodName.DRAWGAME_FETCH_REPRINT_COUNT);
		sReq.setServiceData(gameNo + "," + ticketNo);
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		sRes = delegate.getResponse(sReq);
		String reprintCount = (String) sRes.getResponseData();
		System.out.println("gameNo:" + gameNo + "ticketNo:" + ticketNo
				+ "reprintCount:" + reprintCount);
		return reprintCount;
	}
	
	
	public static LinkedHashMap<String, ArrayList<Object>> fetchTicketToCancel(String ticketList) {
		System.out.println("inside the fetchTicketsToCancel() method...");
		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.PLAYGAME);
		sReq.setServiceMethod(ServiceMethodName.FETCH_TICKETS_TO_CANCEL);
		sReq.setServiceData(ticketList);
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		sRes = delegate.getResponse(sReq);		
		String responseString = sRes.getResponseData().toString();
		Type elementType = new TypeToken<LinkedHashMap<String, ArrayList<Object>>>() {}.getType();
		LinkedHashMap ticketInfoMap = new Gson().fromJson(responseString, elementType);
		return ticketInfoMap;
	}
	
	
	
	
	public static String fetchUpdatedReprintCount(int gameId, String ticketNo, int count) {
		//count is -ve or +ve depending on decrease or increase.
		
		System.out.println("inside the fetchReprintCount() method...");
		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.DRAWGAME);
		sReq.setServiceMethod(ServiceMethodName.DRAWGAME_FETCH_REPRINT_COUNT);
		sReq.setServiceData(gameId + "," + ticketNo + "," + count);
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		sRes = delegate.getResponse(sReq);
		String reprintCount = (String) sRes.getResponseData();
		System.out.println("gameId:" + gameId + "ticketNo:" + ticketNo
				+ "reprintCount:" + reprintCount);
		return reprintCount;
	}

	public static String findParserString(String type) {
		String parser = "";
		String parserArr[] = new String[] { "/", "-", "." };
		for (String element : parserArr) {
			if (type.indexOf(element) >= 0) {
				parser = element;
				break;
			}
		}

		return parser;
	}

	public static double fmtToTwoDecimal(double number) {
		return Math.round((number * 100)) / 100.0;

	}

	public static List<Integer> getAgentOrgIdList() {
		ArrayList<Integer> orgIdList = new ArrayList<Integer>();
		Connection con = DBConnect.getConnection();
		try {
			PreparedStatement pstmt = con
					.prepareStatement("select organization_id, organization_type, name from st_lms_organization_master where organization_type = 'AGENT'");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				orgIdList.add(rs.getInt("organization_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return orgIdList;
	}

	public synchronized static int getGameNbr(int gameId) {
		int gameNbr = -1;
		Connection con = DBConnect.getConnection();
		try {
			PreparedStatement pstmt = con
					.prepareStatement("select game_nbr from st_se_game_master where game_id = ?");
			pstmt.setInt(1, gameId);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				gameNbr = rs.getInt("game_nbr");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
		return gameNbr;
	}

	public static void main(String[] args) {
		System.out
				.println(convertDateInGlobalFormat("09/05/02", "", "mm-dd-yy"));

	}

	public static void sendMail(String errMsg) {
		HttpServletRequest request = ServletActionContext.getRequest();
		String errTime = "" + new Date().getTime();
		String local = request.getLocalAddr() + "/" + request.getLocalName();

		HttpSession session = request.getSession();
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");

		String remote = request.getRemoteAddr() + " UserName "
				+ userBean.getUserName() + " OrgName " + userBean.getOrgName();

		ServletContext sc = ServletActionContext.getServletContext();

		logger.debug(local + "-------*****************-----------" + remote);
		sc.setAttribute("ERROR_TIME", errTime);
		List to = new ArrayList();
		to.add("lms.error@skilrock.com");
		MailSender ms = new MailSender("lms.error@skilrock.com", "skilrock",
				to, "ServerError at " + local + " " + errTime, remote + " "
						+ errMsg, "");
		ms.setDaemon(true);
		ms.start();
	}

	//added by gaurav
	//retrieving the last date from temp history table for report expand
	public static String getLastArchDate() {
		 Connection con = null;
		 PreparedStatement pstmt = null;
		 ResultSet rs = null;
		String lastDate="";
		String oldLastDate = "";
		con = DBConnect.getConnection();
		try {
			pstmt = con.prepareStatement("select last_date from tempdate_history order by id desc limit 1");
			rs=pstmt.executeQuery();
			if(rs.next()){
				lastDate=rs.getDate("last_date").toString();	
			}else{
				pstmt = con.prepareStatement("select value last_date from st_lms_property_master where property_code='deployment_date'");
				rs=pstmt.executeQuery();
				if(rs.next()){
					oldLastDate=rs.getString("last_date");
					SimpleDateFormat formatOld = new SimpleDateFormat("dd-MM-yyyy");
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					Date oldDate = formatOld.parse(oldLastDate);
					Calendar cal1 = Calendar.getInstance();
					cal1.setTime(oldDate);
					cal1.add(Calendar.DAY_OF_MONTH, -1);
					lastDate = format.format(cal1.getTime());
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			DBConnect.closeCon(con);
		}
		return lastDate;
	}
	public static String getArchDate() {
		 Connection con = null;
		 PreparedStatement pstmt = null;
		 ResultSet rs = null;
		String lastDate="";
		con = DBConnect.getConnection();
		try {
			pstmt = con.prepareStatement("select last_date from tempdate_history order by id desc limit 1");
			rs=pstmt.executeQuery();
			if(rs.next()){
				lastDate=rs.getDate("last_date").toString();	
			}
			else
			{
				return null;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			DBConnect.closeCon(con);
		}
		return lastDate;
	}
	
	public static String getXclOrClDetails(int orgId,String type){
		Connection con =null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String selectQuery = null;
		String result="";
		double retCrLimit =0.0;
		try{
			if("BENIN".equalsIgnoreCase(Utility.getPropertyValue("COUNTRY_DEPLOYED"))){
				selectQuery = "select credit_limit,extended_credit_limit,extends_credit_limit_upto,collected_security_deposit security_deposit from st_lms_organization_master om INNER JOIN st_lms_organization_security_levy_master slm ON om.organization_id=slm.organization_id where om.organization_id=?";
			}else{
				selectQuery = "select credit_limit,extended_credit_limit,extends_credit_limit_upto,security_deposit from st_lms_organization_master where organization_id=?";
			}
			
						
			con=DBConnect.getConnection();
			pstmt = con.prepareStatement(selectQuery);
			pstmt.setInt(1, orgId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				retCrLimit = (type.equalsIgnoreCase("XCL"))? rs.getDouble("extended_credit_limit")
						: rs.getDouble("credit_limit");
				int days= calculateExtendsCreditLimitUpto(rs
						.getDate("extends_credit_limit_upto"));
				double secDep=rs.getDouble("security_deposit");
				result=result.concat(String.valueOf(retCrLimit)).concat(":").concat(String.valueOf(days).concat(":").concat(String.valueOf(secDep)));
			}
		}catch (Exception e) {
		}
		return result;
	}

	
	private static int calculateExtendsCreditLimitUpto(java.sql.Date date) {
		if (date == null) {
			return 0;
		}
		long days = 0;

		Calendar today = Calendar.getInstance();
		today.set(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today
				.get(Calendar.DAY_OF_MONTH), 0, 0, 0);

		Calendar extendedDate = Calendar.getInstance();
		extendedDate.setTimeInMillis(date.getTime());
		extendedDate.set(extendedDate.get(Calendar.YEAR), extendedDate
				.get(Calendar.MONTH), extendedDate.get(Calendar.DAY_OF_MONTH),
				0, 0, 1);

		long timeDiff = extendedDate.getTimeInMillis()
				- today.getTimeInMillis();
		if (timeDiff > 0) {
			days = timeDiff / (1000 * 60 * 60 * 24);
		}
		// System.out.println(" dd days : "+days +" hours = "+hours);
		// System.out.println(date +", extendedDate = "+extendedDate.getTime()
		// +" ,today : "+today.getTime());
		return (int) days;
	}
	
	public static String getOrgAddress(String orgId){
		String address="";
		String addSeprator=", ";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		if(orgId==null || orgId.equalsIgnoreCase("")){
			return address;
		}
			try {
				con = DBConnect.getConnection();
				pstmt = con.prepareStatement(QueryManager.getST6AddressQuery());
				pstmt.setString(1, orgId);
				rs=pstmt.executeQuery();
				while (rs.next()) {
					address = address.concat(rs.getString(1)).concat(addSeprator).concat(rs.getString(2)).concat(addSeprator).concat(rs.getString(3)).concat(addSeprator).concat(rs.getString(4)).concat(addSeprator).concat(rs.getString(5));
				}
			}
			catch (Exception e) {
				address="";
				e.printStackTrace();
			}
			finally{
				try {
					pstmt.close();
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				if(con!=null){
					DBConnect.closeCon(con);
				}
			}
		
		return address;
	}
	
/*	public static Map<Integer, String> getAgentInfoMap()	throws LMSException {
			
			Connection con = DBConnect.getConnection();
			PreparedStatement pstmt =null;
			ResultSet rs = null;
			String query=null;
			Map<Integer,String> agentInfoMap=null;
			
			try {
					String appendOrder =QueryManager.getAppendOrgOrder();
						if((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE") ){
							query ="select org_code orgCode,organization_id from st_lms_organization_master where organization_type=? order by  "+appendOrder;
	
					}else if((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE_NAME")){
		
		
						query ="select concat(org_code,'_',name) orgCode,organization_id from st_lms_organization_master where organization_type=? order by "+appendOrder;
	
			
	
					}else if((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("NAME_CODE")){
		
	
						query ="select concat(name,'_',org_code) orgCode,organization_id from st_lms_organization_master where organization_type=?  order by  "+appendOrder;
	
		
		
					}else {
					
						query ="select name orgCode,organization_id from st_lms_organization_master where organization_type=?   order by  "+appendOrder;
			
		
					}
			
				pstmt = con.prepareStatement(query);
				pstmt.setString(1,"AGENT");
				
				rs = pstmt.executeQuery();
				agentInfoMap=new LinkedHashMap<Integer, String>();
			while (rs.next()) {
				agentInfoMap.put(rs.getInt("organization_id"), rs.getString("orgCode"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return agentInfoMap;
		}*/
	
	public static Map<Integer, String> getOrgInfoMap(int userOrgId, String orgType)	throws LMSException {
		
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt =null;
		ResultSet rs = null;
		String query=null;
		Map<Integer,String> retInfoMap=null;
		
		try {
			String appendOrder =QueryManager.getAppendOrgOrder();
			
			if((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE") ){
				
				
				query ="select org_code orgCode,organization_id from st_lms_organization_master where organization_type=? and parent_id=?  and organization_status <> 'TERMINATE' order by  "+appendOrder;
			
			}else if((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE_NAME")){
				
				
			query ="select concat(org_code,'_',name) orgCode,organization_id from st_lms_organization_master where organization_type=? and parent_id=? and organization_status <> 'TERMINATE' order by "+appendOrder;
			
					
			
			}else if((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("NAME_CODE")){
				
			
				query ="select concat(name,'_',org_code) orgCode,organization_id from st_lms_organization_master where organization_type=? and parent_id=? and organization_status <> 'TERMINATE' order by  "+appendOrder;
			
				
				
			}else {
							
				query ="select name orgCode,organization_id from st_lms_organization_master where organization_type=? and parent_id=?  and organization_status <> 'TERMINATE' order by  "+appendOrder;
					
				
			}
					
			pstmt = con.prepareStatement(query);
			pstmt.setString(1,orgType);
			pstmt.setInt(2,userOrgId);
			rs = pstmt.executeQuery();
			retInfoMap=new LinkedHashMap<Integer, String>();
		while (rs.next()) {
			retInfoMap.put(rs.getInt("organization_id"), rs.getString("orgCode"));
		}
		
			} catch (SQLException se) {
				logger.error("Exception",se);
				throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
			}
			catch (Exception e) {
				logger.error("Exception",e);
				throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			}
			finally {
				try {
					DBConnect.closeCon(con);
					}
				 catch (Exception ee) {
					logger.error("Exception",ee);
					throw new LMSException(LMSErrors.CONNECTION_CLOSE_ERROR_CODE,LMSErrors.CONNECTION_CLOSE_ERROR_MESSAGE);
					

				}
			}
			return retInfoMap;
			}

	public static final boolean checkPriviledgeActiveForUser(int userId, String priviledgeTitle, String interfaceType, String serviceName, Connection connection) throws LMSException {
		String serviceTableName = null;
		if("HOME".equals(serviceName)) {
			serviceTableName = "st_lms_priviledge_rep";
		} else if("DG".equals(serviceName)) {
			serviceTableName = "st_dg_priviledge_rep";
		} else if("CS".equals(serviceName)) {
			serviceTableName = "st_cs_priviledge_rep";
		} else if("SE".equals(serviceName)) {
			serviceTableName = "st_se_priviledge_rep";
		} else if("SLE".equals(serviceName)) {
			serviceTableName = "st_sle_priviledge_rep";
		} else if("OLA".equals(serviceName)) {
			serviceTableName = "st_ola_priviledge_rep";
		} else if("IW".equals(serviceName)) {
			serviceTableName = "st_iw_priviledge_rep";
		} else {
			throw new LMSException("Invalid Service Name");
		}

		boolean flag = false;
		Statement stmt = null;
		ResultSet rs = null;
		String query = "SELECT upm.priv_id FROM st_lms_user_priv_mapping upm INNER JOIN "+serviceTableName+" lpr ON lpr.priv_id=upm.priv_id " +
						"AND upm.user_id="+userId+" AND lpr.priv_title='"+priviledgeTitle+"' AND lpr.channel='"+interfaceType+"' AND lpr.status='ACTIVE' AND upm.status='ACTIVE';";
		try {
			stmt = connection.createStatement();
			logger.info("checkPriviledgeActiveForUser Query - "+query);
			rs = stmt.executeQuery(query);
			if(rs.next()) {
				flag = true;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return flag;
	}

	public static final List<String> getTierWiseInterfaceList(String tierType) {
		List<String> interfaceList = new ArrayList<String>();
		Connection connection = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			String query = "SELECT DISTINCT(interface) FROM st_lms_service_delivery_master sdm INNER JOIN st_lms_tier_master ltm ON sdm.tier_id=ltm.tier_id " +
					"AND ltm.tier_code='"+tierType+"' AND ltm.tier_status='ACTIVE' AND sdm.channel='RETAIL';";
			connection = DBConnect.getConnection();
			stmt = connection.createStatement();
			logger.info("getTypeWiseInterfaceList Query - "+query);
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				interfaceList.add(rs.getString("interface"));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return interfaceList;
	}
	public static void doRoundingForPwtAmt(PWTDrawBean winningBean){
		// Rounding Done Based On the Requirement. Initially we used to do it in DGE.
		List<DrawIdBean> drawWinList = null;
		drawWinList=winningBean.getDrawWinList();
		Iterator<DrawIdBean> drawWiseiterator = drawWinList.iterator();
		while(drawWiseiterator.hasNext()){
			DrawIdBean drawIdBean = drawWiseiterator.next();
			//Draw Wise Rounding
			if(drawIdBean.getPanelWinList()!=null){
			drawIdBean.setWinningAmt(Math.round(Double.parseDouble(drawIdBean.getWinningAmt()))+"");
			Iterator<PanelIdBean> panelWiseiterator = drawIdBean.getPanelWinList().iterator();
				while(panelWiseiterator.hasNext()){
					PanelIdBean panelIdBean = panelWiseiterator.next();
					//Panel Wise Rounding
					panelIdBean.setWinningAmt(Math.round(panelIdBean.getWinningAmt()));
				}
			}		
		}
	}
	
	
	private static boolean isNewRangeReq(Timestamp lastGenDate , int curStrtRange , int curEndRange) throws LMSException{
		try {
			Calendar cal =  Calendar.getInstance();
			int curYear = cal.get(Calendar.YEAR);
			cal.setTimeInMillis(lastGenDate.getTime());
			int lastYear = cal.get(Calendar.YEAR);
		
			if(lastYear == curYear){
				return false;
			}else if(lastYear<curYear){
				return true;
			}else{
				throw new LMSException();
			}
		} catch (LMSException e) {
			throw new LMSException();
		}catch (Exception e) {
			throw new LMSException();
		}
	} 
	
	/** @author  Mandeep 

     * @param  userIdMappingBean
     *         Bean that Contains the information of generation process 
     *
     * @param  con
     *         Data Base connection.
     *
     * @throws  LMSException 
     */
	public static synchronized void getUserIdToGenMappindId(UserIdMappingBean userIdMappingBean, Connection con) throws LMSException{

		String updateQuery = null;
		int lastCodeExpiryDays = 0;
		int curStrtRange = 0;
		int curEndRange = 0;
		
		ResultSet rs=null;
		Statement stmt = null;
		PreparedStatement pstmt=null;
		
		Timestamp lastExpDate = null;
		Timestamp lastSuccDate = null;
		
		boolean isSameDay = false;
		boolean isFirstTimeGen = false;
		boolean retailersBlocked =false;
		boolean newRangeReq = false;
		SetResetUserPasswordAction setResetUserPasswordAction = null;
		try {
			checkMappingIdDup(con);
			pstmt = con.prepareStatement("select gen_date ,code_expiry_days ,last_exp_date , if(date(gen_date) = date(now()) , true,false) same_day , start_range, end_range  from st_lms_user_random_id_generation_status order by gen_date DESC limit 1");
			rs = pstmt.executeQuery();
				if(rs.next()){
					isSameDay = rs.getBoolean("same_day");
					
					// NEWLY ADDEDD
					curEndRange = rs.getInt("end_range");
					curStrtRange = rs.getInt("start_range");
					
					lastSuccDate = rs.getTimestamp("gen_date");
					lastExpDate = rs.getTimestamp("last_exp_date");
					lastCodeExpiryDays = rs.getInt("code_expiry_days");
					newRangeReq = isNewRangeReq(lastSuccDate , curStrtRange , curEndRange);
					updateQuery = "update st_lms_user_random_id_mapping set user_mapping_id = ? , mapping_id_gen_date = ? , code_expiry = ? , adv_user_mapping_id = ? , adv_mapping_id_gen_date = ? , adv_code_expiry =? where user_id =? ";
				}else {
					newRangeReq = true;
					isFirstTimeGen = true;
					updateQuery = "insert into st_lms_user_random_id_mapping values (?,?,?,?,?,?,?)";
				}
				
				userIdMappingBean.setLastExpDate(lastExpDate);
				userIdMappingBean.setLastSuccDate(lastSuccDate);
				userIdMappingBean.setFirstGeneration(isFirstTimeGen);
				userIdMappingBean.setLastCodeExpiryDays(lastCodeExpiryDays);
				
				// NEWLY ADDEDD
				userIdMappingBean.setCurEndRange(curEndRange);
				userIdMappingBean.setCurStrtRange(curStrtRange);
				userIdMappingBean.setNewRangeReq(newRangeReq);
				
				setResetUserPasswordAction = new SetResetUserPasswordAction();
				if(userIdMappingBean.isAll()){
					//check if the retailer has done any transaction today if No , then make sure to logout that retailer
					if(isSameDay){
						throw new LMSException(LMSErrors.RAMDOM_ID_GENERATION_ERROR_CODE , LMSErrors.RAMDOM_ID_GENERATION_ERROR_MESSAGE);
					}
					/*check user Transaction for today*/
					checkUserTransaction(userIdMappingBean.isAll() ,userIdMappingBean.getUserId() ,userIdMappingBean.getRequesInitiateTime(), con);
					/*logout all retailers*/
					setResetUserPasswordAction.logOutAllRets();
					/*stop all retailers from login */
					setResetUserPasswordAction.stopLoginAllUsers();
					retailersBlocked = true;
					
					if(!userIdMappingBean.isThirdPartyGeneration()){
						/*Generate Only if Not Third */
						generateNewMappingForAll(userIdMappingBean ,con);
					}
					/*update/insert random ids into the data base*/
					updateUserMappingId(userIdMappingBean ,updateQuery ,con);
				}else if(userIdMappingBean.isSpecific()){
					//check if the retailer has done any transaction today if No , then make sure to logout that retailer 
						checkUserTransaction(userIdMappingBean.isAll() ,userIdMappingBean.getUserId() ,userIdMappingBean.getRequesInitiateTime(), con);
					if(!userIdMappingBean.isThirdPartyGeneration()){
						/*Generate Only if Not Third */
						generateNewMappingForIndividual(userIdMappingBean ,con);
					}
					/*update/insert random ids into the data base*/
					updateUserMappingId(userIdMappingBean ,updateQuery ,con);
				}else{
					throw new LMSException(LMSErrors.SECURITY_BREACH_ERROR_CODE , LMSErrors.SECURITY_BREACH_ERROR_MESSAGE);
				}
		}catch (LMSException e) {
			e.printStackTrace();
			throw e;
		} catch (SQLException e) {
			logger.error("SQLException : - ", e);
			e.printStackTrace();
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE , LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			logger.error("Exception : - ", e);
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE , LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			if(retailersBlocked){
				try {
					/*resume all retailers only if they are blocked*/
					setResetUserPasswordAction.stopLoginAllUsers();
					retailersBlocked = false;
				} catch (IOException e) {
					logger.error("PROBLEM WITH ENABLING RETAILERS : - ", e);
					e.printStackTrace();
					throw new LMSException(LMSErrors.IO_EXCEPTION_ERROR_CODE , LMSErrors.IO_EXCEPTION_ERROR_MESSAGE);
				}
			}
			DBConnect.closeStmt(stmt);
			DBConnect.closePstmt(pstmt);
			
		}
	}
	
	
	private static void generateNewMappingForIndividual(UserIdMappingBean userIdMappingBean , Connection con) throws LMSException{

		boolean isExists;
		ResultSet rs=null;
		long currentTimeInMilis = 0;
		PreparedStatement pstmt=null;
		LinkedHashMap<Integer, Integer> userMappingIdMap = null;
		LinkedHashMap<Integer, Integer> advUserMappingIdMap = null;
		try {
			if(userIdMappingBean.getUserId() == 0){
				throw new LMSException(LMSErrors.INVALIDATE_RETAILER_ERROR_CODE , LMSErrors.INVALIDATE_RETAILER_ERROR_MESSAGE);
			}

			pstmt = con.prepareStatement("select * from st_lms_user_random_id_mapping where user_id = ?");
			pstmt.setInt(1, userIdMappingBean.getUserId());
			rs = pstmt.executeQuery();
			if(rs.next()){
				isExists = true; 
			}else{
				isExists = false;
			}

			userIdMappingBean.setExists(isExists);
			pstmt = con.prepareStatement("select user_id, user_mapping_id, adv_user_mapping_id  from st_lms_user_random_id_mapping");
			rs = pstmt.executeQuery();
			userMappingIdMap =  new LinkedHashMap<Integer, Integer>();
			advUserMappingIdMap =  new LinkedHashMap<Integer, Integer>();
			while(rs.next()){
				int userId = rs.getInt("user_id");
				userMappingIdMap.put(userId, rs.getInt("user_mapping_id"));
				advUserMappingIdMap.put(userId, rs.getInt("adv_user_mapping_id"));
			}
			
			currentTimeInMilis = System.currentTimeMillis();		
			if(userIdMappingBean.isFirstGeneration()){
				userIdMappingBean.setNewGenerationDateTime(getCodeGenAndExpDates(0 ,currentTimeInMilis ,true));
				userIdMappingBean.setNewCodeExpiryDateTime(getCodeGenAndExpDates(userIdMappingBean.getExpiryDays() ,currentTimeInMilis ,false));
				
				userIdMappingBean.setAdvGenerationDateTime(getCodeGenAndExpDates(userIdMappingBean.getExpiryDays() ,currentTimeInMilis+oneDay ,true));
				userIdMappingBean.setAdvCodeExpiryDateTime(getCodeGenAndExpDates(userIdMappingBean.getExpiryDays()*2 ,currentTimeInMilis+oneDay ,false));
				generateRandomIds(userIdMappingBean.getUserId() , 1 ,new HashSet<Integer>() , userMappingIdMap ,userIdMappingBean.isAll() ,userIdMappingBean.isNewRangeReq() , userIdMappingBean.getCurStrtRange() , userIdMappingBean.getCurEndRange());
			}else if(!isExists){
				userIdMappingBean.setNewGenerationDateTime(getCodeGenAndExpDates(0 ,currentTimeInMilis ,true));
				userIdMappingBean.setNewCodeExpiryDateTime(getCodeGenAndExpDates(userIdMappingBean.getExpiryDays() ,currentTimeInMilis ,false));
				
				userIdMappingBean.setAdvGenerationDateTime(getCodeGenAndExpDates((userIdMappingBean.getLastCodeExpiryDays()*-1) ,userIdMappingBean.getLastExpDate().getTime() ,true));
				userIdMappingBean.setAdvCodeExpiryDateTime(getCodeGenAndExpDates(0 ,userIdMappingBean.getLastExpDate().getTime() ,false));
				generateRandomIds(userIdMappingBean.getUserId() , 1 ,new HashSet<Integer>(userMappingIdMap.values()) , userMappingIdMap ,userIdMappingBean.isAll() ,userIdMappingBean.isNewRangeReq() , userIdMappingBean.getCurStrtRange() , userIdMappingBean.getCurEndRange());
			}else{/*// NOT DECIDED YET
				userMappingIdMap =  null;
				userIdMappingBean.setNewGenerationDateTime(getCodeGenAndExpDates(0 ,currentTimeInMilis ,true));
				userIdMappingBean.setNewCodeExpiryDateTime(getCodeGenAndExpDates(userIdMappingBean.getExpiryDays() ,userIdMappingBean.getLastExpDate().getTime() ,false));
				
				userIdMappingBean.setAdvGenerationDateTime(getCodeGenAndExpDates(0 ,userIdMappingBean.getLastExpDate().getTime()+oneDay ,true));
				userIdMappingBean.setAdvCodeExpiryDateTime(getCodeGenAndExpDates(userIdMappingBean.getExpiryDays() ,userIdMappingBean.getLastExpDate().getTime()+oneDay ,false));
				userMappingIdMap = new LinkedHashMap<Integer, Integer>();
				userMappingIdMap.put(userIdMappingBean.getUserId(), advUserMappingIdMap.get(userIdMappingBean.getUserId()));
			*/}
			
			// ADVANCE GEN IN BOTH THE CASES;
			generateRandomIds(userIdMappingBean.getUserId() , 1, new HashSet<Integer>(advUserMappingIdMap.values())  , advUserMappingIdMap ,userIdMappingBean.isAll() ,userIdMappingBean.isNewRangeReq() , userIdMappingBean.getCurStrtRange() , userIdMappingBean.getCurEndRange());
			userIdMappingBean.setUserMappingIdMap(userMappingIdMap);
			userIdMappingBean.setAdvUserMappingIdMap(advUserMappingIdMap);
			//updateUserMappingId(userIdMappingBean ,isExists ,con);
	}catch (LMSException e) {
		throw e;
	} catch (SQLException e) {
		logger.error("SQLException : - ", e);
		e.printStackTrace();
		throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE , LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
	} catch (Exception e) {
		logger.error("Exception : - ", e);
		e.printStackTrace();
		throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE , LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
	}
	}
	
	private static void generateNewMappingForAll(UserIdMappingBean userIdMappingBean , Connection con) throws LMSException{

		ResultSet rs=null;
		Statement stmt = null;
		String selectQuery = null;
		long currentTimeInMilis = 0;
		LinkedHashMap<Integer, Integer> userMappingIdMap = null;
		LinkedHashMap<Integer, Integer> advUserMappingIdMap = null;
		
		try {
			if(!userIdMappingBean.isFirstGeneration() && !userIdMappingBean.isNewRangeReq()){
				selectQuery = "select user_id, user_mapping_id, adv_user_mapping_id  from st_lms_user_random_id_mapping";
			}else{
				selectQuery = "select user_id, 0 user_mapping_id, 0 adv_user_mapping_id   from st_lms_user_master where organization_type = 'RETAILER'";
			}
			stmt = con.createStatement();
			rs = stmt.executeQuery(selectQuery);
			userMappingIdMap =  new LinkedHashMap<Integer, Integer>();
			advUserMappingIdMap =  new LinkedHashMap<Integer, Integer>();
			while(rs.next()){
				int userId = rs.getInt("user_id");
				userMappingIdMap.put(userId, rs.getInt("user_mapping_id"));
				advUserMappingIdMap.put(userId, rs.getInt("adv_user_mapping_id"));
			}
			currentTimeInMilis = System.currentTimeMillis();
			if(userIdMappingBean.isFirstGeneration() || userIdMappingBean.isNewRangeReq()){
				userIdMappingBean.setNewGenerationDateTime(getCodeGenAndExpDates(0 ,currentTimeInMilis ,true));
				userIdMappingBean.setNewCodeExpiryDateTime(getCodeGenAndExpDates(userIdMappingBean.getExpiryDays() ,currentTimeInMilis ,false));
				
				userIdMappingBean.setAdvGenerationDateTime(getCodeGenAndExpDates(userIdMappingBean.getExpiryDays() ,currentTimeInMilis+oneDay ,true));
				userIdMappingBean.setAdvCodeExpiryDateTime(getCodeGenAndExpDates(userIdMappingBean.getExpiryDays()*2 ,currentTimeInMilis+oneDay ,false));
				generateRandomIds(userIdMappingBean.getUserId() , userMappingIdMap.size() ,new HashSet<Integer>() , userMappingIdMap ,userIdMappingBean.isAll() ,userIdMappingBean.isNewRangeReq() , userIdMappingBean.getCurStrtRange() , userIdMappingBean.getCurEndRange());
			}else{
				userMappingIdMap =  null;
				userIdMappingBean.setNewGenerationDateTime(getCodeGenAndExpDatesForAll(userIdMappingBean.getRequesInitiateTime() , userIdMappingBean.getLastSuccDate().toString() , userIdMappingBean.getLastCodeExpiryDays() , true , false));
				userIdMappingBean.setNewCodeExpiryDateTime(getCodeGenAndExpDatesForAll("" , userIdMappingBean.getNewGenerationDateTime() , userIdMappingBean.getLastCodeExpiryDays() , false ,true));
				
				userIdMappingBean.setAdvGenerationDateTime(getCodeGenAndExpDatesForAll("" ,userIdMappingBean.getNewCodeExpiryDateTime() , 1 , true ,true));
				userIdMappingBean.setAdvCodeExpiryDateTime(getCodeGenAndExpDatesForAll("" ,userIdMappingBean.getAdvGenerationDateTime() ,userIdMappingBean.getExpiryDays() , false ,true));
				userMappingIdMap = new LinkedHashMap<Integer, Integer>(advUserMappingIdMap);
				/*userMappingIdMap =  null;
				userIdMappingBean.setNewGenerationDateTime(getCodeGenAndExpDates((userIdMappingBean.getLastCodeExpiryDays()*-1) ,userIdMappingBean.getLastExpDate().getTime() ,true));
				userIdMappingBean.setNewCodeExpiryDateTime(getCodeGenAndExpDates(0 ,userIdMappingBean.getLastExpDate().getTime() ,false));
				
				userIdMappingBean.setAdvGenerationDateTime(getCodeGenAndExpDates(0 ,userIdMappingBean.getLastExpDate().getTime()+oneDay ,true));
				userIdMappingBean.setAdvCodeExpiryDateTime(getCodeGenAndExpDates(userIdMappingBean.getExpiryDays() ,userIdMappingBean.getLastExpDate().getTime()+oneDay ,false));
				userMappingIdMap = new LinkedHashMap<Integer, Integer>(advUserMappingIdMap);*/
			}
			// ADVANCE GEN IN BOTH THE CASES;
			generateRandomIds(userIdMappingBean.getUserId() , advUserMappingIdMap.size() ,new HashSet<Integer>() , advUserMappingIdMap ,userIdMappingBean.isAll() ,userIdMappingBean.isNewRangeReq() , userIdMappingBean.getCurStrtRange() , userIdMappingBean.getCurEndRange());
			
			if(userMappingIdMap.size() != advUserMappingIdMap.size()){
				throw new LMSException(LMSErrors.RAMDOM_ID_GENERATION_PROCESS_ERROR_CODE , LMSErrors.RAMDOM_ID_GENERATION_PROCESS_ERROR_MESSAGE);
			}
			userIdMappingBean.setUserMappingIdMap(userMappingIdMap);
			userIdMappingBean.setAdvUserMappingIdMap(advUserMappingIdMap);
			//updateUserMappingId(userIdMappingBean ,false ,con);
	}catch (LMSException e) {
		throw e;
	} catch (SQLException e) {
		logger.error("SQLException : - ", e);
		e.printStackTrace();
		throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE , LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
	} catch (Exception e) {
		logger.error("Exception : - ", e);
		e.printStackTrace();
		throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE , LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
	}
	}

	private static void updateUserMappingId(UserIdMappingBean userIdMappingBean ,String updateQuery ,Connection con) throws LMSException, IOException{
		
		int size = 0;
		int check = 0;
		int count = 0;
		boolean batchExecuted = false;
		PreparedStatement pstmt = null;
		try {
			con.setAutoCommit(false);
		if (userIdMappingBean.isSpecific()) {
			int userId = userIdMappingBean.getUserId();
			if(!userIdMappingBean.isExists()){
				pstmt = con.prepareStatement("insert into st_lms_user_random_id_mapping values (?,?,?,?,?,?,?)");
				pstmt.setInt(1, userIdMappingBean.getUserId());
				pstmt.setInt(2, userIdMappingBean.getUserMappingIdMap().get(userId));
				pstmt.setString(3, userIdMappingBean.getNewGenerationDateTime());
				pstmt.setString(4, userIdMappingBean.getNewCodeExpiryDateTime());
				pstmt.setInt(5, userIdMappingBean.getAdvUserMappingIdMap().get(userId));
				pstmt.setString(6, userIdMappingBean.getAdvGenerationDateTime());
				pstmt.setString(7, userIdMappingBean.getAdvCodeExpiryDateTime());
				pstmt.executeUpdate();
				pstmt.clearParameters();
			}else{/* NO DECIDED
				new SetResetUserPasswordAction().logOutRetFromUserId(userIdMappingBean.getUserId(), con);
				pstmt = con.prepareStatement("update st_lms_user_random_id_mapping set user_mapping_id = ? , mapping_id_gen_date = ? , code_expiry = ? , adv_user_mapping_id = ? , adv_mapping_id_gen_date = ? , adv_code_expiry =? where user_id =? ");
				pstmt.setInt(1, userIdMappingBean.getUserMappingIdMap().get(userId));
				pstmt.setString(2, userIdMappingBean.getNewGenerationDateTime());
				pstmt.setString(3, userIdMappingBean.getNewCodeExpiryDateTime());
				pstmt.setInt(4, userIdMappingBean.getAdvUserMappingIdMap().get(userId));
				pstmt.setString(5, userIdMappingBean.getAdvGenerationDateTime());
				pstmt.setString(6, userIdMappingBean.getAdvCodeExpiryDateTime());
				pstmt.setInt(7, userIdMappingBean.getUserId());
				pstmt.executeUpdate();
				pstmt.clearParameters();
			*/}
			pstmt = con.prepareStatement("insert into st_lms_user_random_id_mapping_history select user_id ,user_mapping_id ,mapping_id_gen_date, code_expiry , ? , ? from st_lms_user_random_id_mapping where user_id = ?"); // Mapping history
			pstmt.setString(1, userIdMappingBean.getActivity());
			pstmt.setInt(2, userIdMappingBean.getDoneByUserId());
			pstmt.setInt(3, userIdMappingBean.getUserId());
			pstmt.executeUpdate();
			pstmt.clearParameters();
			checkUserTransaction(userIdMappingBean.isAll() ,userIdMappingBean.getUserId() ,userIdMappingBean.getRequesInitiateTime(), con);
		} else if(userIdMappingBean.isAll()){
			LinkedHashMap<Integer, Integer> newMap = userIdMappingBean.getUserMappingIdMap(); 
			LinkedHashMap<Integer, Integer> advMap = userIdMappingBean.getAdvUserMappingIdMap();
			size = newMap.size();
			pstmt = con.prepareStatement(updateQuery);

			if(!userIdMappingBean.isFirstGeneration()){
			for(Map.Entry<Integer, Integer> entry : newMap.entrySet()){
				int userId = entry.getKey();
				count++;
			if (batchExecuted) {
				pstmt = con.prepareStatement(updateQuery);
				batchExecuted = false;
			}
			pstmt.setInt(1, entry.getValue());
			pstmt.setString(2, userIdMappingBean.getNewGenerationDateTime());
			pstmt.setString(3, userIdMappingBean.getNewCodeExpiryDateTime());
			pstmt.setInt(4,    advMap.get(userId));
			pstmt.setString(5, userIdMappingBean.getAdvGenerationDateTime());
			pstmt.setString(6, userIdMappingBean.getAdvCodeExpiryDateTime());
			pstmt.setInt(7, userId);

			pstmt.addBatch();
			pstmt.clearParameters();
			if (count != 0 && count % 100 == 0) {
				check += pstmt.executeBatch().length;
				batchExecuted = true;
					}
				}
			}else{
				for(Map.Entry<Integer, Integer> entry : newMap.entrySet()){
					int userId = entry.getKey();
					count++;
					if (batchExecuted) {
						pstmt = con.prepareStatement(updateQuery);
						batchExecuted = false;
					}
			pstmt.setInt(1, userId);
			pstmt.setInt(2, entry.getValue());
			pstmt.setString(3, userIdMappingBean.getNewGenerationDateTime());
			pstmt.setString(4, userIdMappingBean.getNewCodeExpiryDateTime());
			pstmt.setInt(5,    advMap.get(userId));
			pstmt.setString(6, userIdMappingBean.getAdvGenerationDateTime());
			pstmt.setString(7, userIdMappingBean.getAdvCodeExpiryDateTime());
			logger.info("pstmt "+pstmt);
			pstmt.addBatch();
			pstmt.clearParameters();
			if (count != 0 && count % 100 == 0) {
				check += pstmt.executeBatch().length;
				batchExecuted = true;
					}
				}
			}
		if (check <= size - 1) {
			pstmt.executeBatch();
		}
			pstmt.clearParameters();
			pstmt = con.prepareStatement("insert into st_lms_user_random_id_mapping_history select user_id ,user_mapping_id ,mapping_id_gen_date, code_expiry , ? , ? from st_lms_user_random_id_mapping ");
			pstmt.setString(1, userIdMappingBean.getActivity());
			pstmt.setInt(2, userIdMappingBean.getDoneByUserId());
			int result = pstmt.executeUpdate();
			logger.info(result +" HISTORY UPDATED");
			
			pstmt = con.prepareStatement("insert into st_lms_user_random_id_generation_status (gen_date , code_expiry_days , last_exp_date, status , start_range , end_range)  values (?,?,?,?,?,?)");
			pstmt.setString(1, userIdMappingBean.getRequesInitiateTime());
			pstmt.setInt(2, userIdMappingBean.getExpiryDays());
			pstmt.setString(3, userIdMappingBean.getAdvCodeExpiryDateTime());
			pstmt.setString(4, "SUCCESS");
			pstmt.setInt(5, (userIdMappingBean.isNewRangeReq())?Integer.parseInt(Utility.getPropertyValue("RANDOM_RET_ID_START_RANGE")):userIdMappingBean.getCurStrtRange());
			pstmt.setInt(6, (userIdMappingBean.isNewRangeReq())?Integer.parseInt(Utility.getPropertyValue("RANDOM_RET_ID_END_RANGE")):userIdMappingBean.getCurEndRange());
			result = pstmt.executeUpdate();
			logger.info(result +" STATUS UPDATED");
			checkUserTransaction(userIdMappingBean.isAll() ,userIdMappingBean.getUserId() ,userIdMappingBean.getRequesInitiateTime(), con);
		}else{
			throw new LMSException(LMSErrors.SECURITY_BREACH_ERROR_CODE , LMSErrors.SECURITY_BREACH_ERROR_MESSAGE);
		}
		
		// DOUBLE VERIFY 
		checkMappingIdDup(con);
		con.commit();

	}catch (LMSException e) {
		e.printStackTrace();
		throw e;
	} catch (SQLException e) {
		if(e.getMessage().contains("Duplicate entry")){
			throw new LMSException(LMSErrors.RAMDOM_ID_GENERATION_ERROR_CODE , LMSErrors.RAMDOM_ID_GENERATION_ERROR_MESSAGE);
		}else{
			e.printStackTrace();
			logger.error("SQLException : - ", e);
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE , LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		}
	} catch (Exception e) {
		e.printStackTrace();
		logger.error("Exception : - ", e);
		throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE , LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
	}finally{
		DBConnect.closePstmt(pstmt);
		}
	}
	
	
	private static String getCodeGenAndExpDates(int noOfDays , long currentTimeInMilis , boolean isFirstDate) throws LMSException{
		Calendar cal = null;
		SimpleDateFormat sdf = null;
		String newGenExpDateTime = null;
		try{
			cal = Calendar.getInstance();
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			cal.setTimeInMillis(currentTimeInMilis);
			if(isFirstDate){
				cal.set(Calendar.HOUR_OF_DAY, 00);
				cal.set(Calendar.MINUTE, 00);
				cal.set(Calendar.SECOND, 00);
			}else{
				cal.set(Calendar.HOUR_OF_DAY, 23);
				cal.set(Calendar.MINUTE, 59);
				cal.set(Calendar.SECOND, 59);
			}
			cal.add(Calendar.DATE,noOfDays);
			newGenExpDateTime = sdf.format(new Timestamp(cal.getTime().getTime()));
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception" , e);
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return newGenExpDateTime;
	}
	
	private static String getCodeGenAndExpDatesForAll(String reqInitTime , String startDate , int days , boolean isFirstDate , boolean codeExpiry) throws LMSException {
		Calendar cal = null;
		SimpleDateFormat sdf = null;
		Timestamp startDateTime = null;
		Timestamp decidedTime = null;
		Timestamp initiatedTime = null;
		Timestamp generatedTimestamp = null;
		try {
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			cal = Calendar.getInstance();
			startDateTime = new Timestamp(sdf.parse(startDate).getTime());
			if (!codeExpiry) {
				initiatedTime = new Timestamp(sdf.parse(reqInitTime).getTime());
				generatedTimestamp = new Timestamp(startDateTime.getTime());
				cal.setTimeInMillis(generatedTimestamp.getTime());
				cal.add(Calendar.DATE, days + 1);
				cal.setTimeInMillis(initiatedTime.getTime());
				/*if (initiatedTime.after(new Timestamp(cal.getTimeInMillis()))) {
					// does nothing
				} else {
					cal.setTimeInMillis(initiatedTime.getTime());
				}*/
			}else{
				generatedTimestamp = new Timestamp(startDateTime.getTime());
				cal.setTimeInMillis(generatedTimestamp.getTime());
				cal.add(Calendar.DATE, days);
			}
			if(isFirstDate){
				cal.set(Calendar.HOUR_OF_DAY, 00);
				cal.set(Calendar.MINUTE, 00);
				cal.set(Calendar.SECOND, 00);
			}else{
				cal.set(Calendar.HOUR_OF_DAY, 23);
				cal.set(Calendar.MINUTE, 59);
				cal.set(Calendar.SECOND, 59);
			}
			decidedTime = new Timestamp(cal.getTimeInMillis());
		} catch (Exception e) {
			logger.error("Exception", e);
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return decidedTime.toString();
	}
	
	private static void generateRandomIds(int userId , int genWeight , Set<Integer> checkSet , LinkedHashMap<Integer, Integer> newGenUserMappingIdMap ,boolean isAll , boolean isNewGenReq , int stRng , int endRng) throws LMSException{
		
		int count =0;
		int randEndRange = 0;
		int randStartRange = 0;
		List<Integer> userIdList =  null;
		HashSet<Integer> newMappingSet = null;
		try {
			newMappingSet = new HashSet<Integer>();
			//randStartRange = ConfigurationVariables.RANDOM_RET_ID_B_START_RANGE;
			//randEndRange = ConfigurationVariables.RANDOM_RET_ID_B_END_RANGE;
			if(!isNewGenReq){
				randStartRange = stRng;
				randEndRange = endRng;
			}else{
				randStartRange = Integer.parseInt(Utility.getPropertyValue("RANDOM_RET_ID_START_RANGE"));
				randEndRange = Integer.parseInt(Utility.getPropertyValue("RANDOM_RET_ID_END_RANGE"));	
			}
			if(!isAll){
				userIdList = new ArrayList<Integer>();
				userIdList.add(userId);
				newGenUserMappingIdMap.clear();
			}else{
				userIdList = new ArrayList<Integer>(newGenUserMappingIdMap.keySet());
			}
			while (newMappingSet.size() != genWeight) {
				int randStr = RNGUtilities.generateRandomNumber(randStartRange, randEndRange);
				// CHECK DUP ONLY IN CASE OF NEW REG/UPDATION 
				if(checkSet.add(randStr)){
					newMappingSet.add(randStr);
					newGenUserMappingIdMap.put(userIdList.get(count), randStr);
					count++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception" , e);
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}
	
	private static void checkMappingIdDup(Connection con) throws LMSException{
		
		ResultSet rs=null;
		Statement stmt = null;
		try{
				stmt =  con.createStatement();
				rs = stmt.executeQuery("select user_mapping_id from st_lms_user_random_id_mapping group by user_mapping_id having count(user_mapping_id) >1 union all select adv_user_mapping_id  from st_lms_user_random_id_mapping group by  adv_user_mapping_id having count(adv_user_mapping_id) >1");
				if(rs.next()){
					throw new LMSException(LMSErrors.SECURITY_BREACH_ERROR_CODE , LMSErrors.SECURITY_BREACH_ERROR_MESSAGE);
				}
		}catch (LMSException e) {
			logger.error("Exception" , e);
			throw e;
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("SQLException : - ", e);
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE , LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception : - ", e);
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE , LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			DBConnect.closeRs(rs);
			DBConnect.closeStmt(stmt);
			
		}
	}
	
	public static void checkUserTransaction(boolean isAll ,int userId ,String transactionDateTime ,Connection con) throws LMSException{
		
		ResultSet rs=null;
		PreparedStatement pstmt = null;
		try{
				if(isAll){
					pstmt =  con.prepareStatement("select transaction_id from st_lms_retailer_transaction_master where date(transaction_date)  = date(?) and transaction_type in ('DG_SALE','DG_REFUND_CANCEL','DG_REFUND_FAILED','SALE','SALE_RET','DG_SALE_OFFLINE','CS_SALE','CS_CANCEL_SERVER')");
				}else{
					pstmt =  con.prepareStatement("select * from st_lms_retailer_transaction_master where date(transaction_date) = date(?) and retailer_user_id  = ? and transaction_type in ('DG_SALE','DG_REFUND_CANCEL','DG_REFUND_FAILED','SALE','SALE_RET','DG_SALE_OFFLINE','CS_SALE','CS_CANCEL_SERVER')");
					pstmt.setInt(2, userId);
				}
				pstmt.setString(1, transactionDateTime);
				rs = pstmt.executeQuery();
				if(rs.next()){
					throw new LMSException(LMSErrors.RAMDOM_ID_GENERATION_ERROR_CODE , LMSErrors.RAMDOM_ID_GENERATION_ERROR_MESSAGE);
				}
		}catch (LMSException e) {
			e.printStackTrace();
			throw e;
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("SQLException : - ", e);
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE , LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception : - ", e);
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE , LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			DBConnect.closeRs(rs);
			DBConnect.closeStmt(pstmt);
		}
	}
	
	public static void insertUpdateOrganizationHistory(HistoryBean historyBean, Connection connection) throws LMSException {
		PreparedStatement pstmt = null;
		String fieldName = null;
		try {
			pstmt = connection.prepareStatement("INSERT INTO st_lms_organization_master_history (organization_id, change_type, change_value, change_time, done_by_user_id, comments, request_ip) VALUES (?,?,?,?,?,?,?);");
			pstmt.setInt(1, historyBean.getOrganizationId());
			pstmt.setString(2, historyBean.getChangeType());
			pstmt.setString(3, historyBean.getChangeValue());
			pstmt.setTimestamp(4, Util.getCurrentTimeStamp());
			pstmt.setInt(5, historyBean.getDoneByUserId());
			pstmt.setString(6, historyBean.getComments());
			pstmt.setString(7, historyBean.getRequestIp());
			logger.info("Insert st_lms_organization_master_history History : "+pstmt);
			pstmt.executeUpdate();

			if("ADDRESS_1".equals(historyBean.getChangeType())) {
				fieldName = "addr_line1";
			} else if("ADDRESS_2".equals(historyBean.getChangeType())) {
				fieldName = "addr_line2";
			} else if("CITY".equals(historyBean.getChangeType())) {
				fieldName = "city";
			} else if("ORGANIZATION_STATUS".equals(historyBean.getChangeType())) {
				fieldName = "organization_status";
			} else if("DIVISION_CODE".equals(historyBean.getChangeType())) {
				fieldName = "division_code";
			} else if("AREA_CODE".equals(historyBean.getChangeType())) {
				fieldName = "area_code";
			} else if("PIN_CODE".equals(historyBean.getChangeType())) {
				fieldName = "pin_code";
			} else if("PWT_SCRAP".equals(historyBean.getChangeType())) {
				fieldName = "pwt_scrap";
			} else if("SECURITY_DEPOSIT".equals(historyBean.getChangeType())) {
				fieldName = "security_deposit";
			}

			// CHANGE ORG CODE , ORG NAME 
			pstmt = connection.prepareStatement("UPDATE st_lms_organization_master SET "+fieldName+"=? WHERE organization_id=?;");
			pstmt.setString(1, historyBean.getUpdatedValue());
			pstmt.setInt(2, historyBean.getOrganizationId());
			logger.info("Update Organization Information : "+pstmt);
			pstmt.executeUpdate();
		} catch (SQLException se) {
			se.printStackTrace();
			throw new LMSException("SQL Exception", se);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("Exception", e);
		}
	}

	public static void insertUpdateOrganizationHistoryForAGTShift(HistoryBean historyBean, Connection connection) throws LMSException {
		PreparedStatement pstmt = null;
		String fieldName = null;
		try {
			pstmt = connection.prepareStatement("INSERT INTO st_lms_organization_master_history (organization_id, change_type, change_value, change_time, done_by_user_id, comments, request_ip) VALUES (?,?,?,?,?,?,?);");
			pstmt.setInt(1, historyBean.getOrganizationId());
			pstmt.setString(2, historyBean.getChangeType());
			pstmt.setString(3, historyBean.getChangeValue());
			pstmt.setTimestamp(4, Util.getCurrentTimeStamp());
			pstmt.setInt(5, historyBean.getDoneByUserId());
			pstmt.setString(6, historyBean.getComments());
			pstmt.setString(7, historyBean.getRequestIp());
			logger.info("Insert st_lms_organization_master_history History : "+pstmt);
			pstmt.executeUpdate();

			if("ADDRESS_1".equals(historyBean.getChangeType())) {
				fieldName = "addr_line1";
			} else if("ADDRESS_2".equals(historyBean.getChangeType())) {
				fieldName = "addr_line2";
			} else if("CITY".equals(historyBean.getChangeType())) {
				fieldName = "city";
			} else if("ORGANIZATION_STATUS".equals(historyBean.getChangeType())) {
				fieldName = "organization_status";
			} else if("DIVISION_CODE".equals(historyBean.getChangeType())) {
				fieldName = "division_code";
			} else if("AREA_CODE".equals(historyBean.getChangeType())) {
				fieldName = "area_code";
			} else if("PIN_CODE".equals(historyBean.getChangeType())) {
				fieldName = "pin_code";
			} else if("PWT_SCRAP".equals(historyBean.getChangeType())) {
				fieldName = "pwt_scrap";
			} else if("SECURITY_DEPOSIT".equals(historyBean.getChangeType())) {
				fieldName = "security_deposit";
			}

			// CHANGED ORG CODE , ORG NAME 
			pstmt = connection.prepareStatement("UPDATE st_lms_organization_master SET "
							+ fieldName + "=? , name = concat(name,'"
							+ historyBean.getDateAppender()
							+ "') , org_code = concat(org_code,'"
							+ historyBean.getDateAppender()
							+ "') WHERE organization_id=?;");
			
			pstmt.setString(1, historyBean.getUpdatedValue());
			pstmt.setInt(2, historyBean.getOrganizationId());
			logger.info("Update Organization Information : "+pstmt);
			pstmt.executeUpdate();
		} catch (SQLException se) {
			se.printStackTrace();
			throw new LMSException("SQL Exception", se);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("Exception", e);
		}
	}
	
	public static void insertUpdateUserHistory(HistoryBean historyBean, Connection connection) throws LMSException {
		PreparedStatement pstmt = null;
		String tableName = null;
		String fieldName = null;
		boolean isStatusUpdate = false;
		try {
			pstmt = connection.prepareStatement("INSERT INTO st_lms_user_master_history (user_id, change_type, change_value, change_time, done_by_user_id, comments, request_ip) VALUES (?,?,?,?,?,?,?);");
			pstmt.setInt(1, historyBean.getOrganizationId());
			pstmt.setString(2, historyBean.getChangeType());
			pstmt.setString(3, historyBean.getChangeValue());
			pstmt.setTimestamp(4, Util.getCurrentTimeStamp());
			pstmt.setInt(5, historyBean.getDoneByUserId());
			pstmt.setString(6, historyBean.getComments());
			pstmt.setString(7, historyBean.getRequestIp());
			logger.info("Insert st_lms_user_master_history History : "+pstmt);
			pstmt.executeUpdate();

			if("EMAIL_ID".equals(historyBean.getChangeType())) {
				tableName = "st_lms_user_contact_details";
				fieldName = "email_id";
			} else if("PHONE_NUMBER".equals(historyBean.getChangeType())) {
				tableName = "st_lms_user_contact_details";
				fieldName = "phone_nbr";
			} else if("MOBILE_NUMBER".equals(historyBean.getChangeType())) {
				tableName = "st_lms_user_contact_details";
				fieldName = "mobile_nbr";
			} else if("PASSWORD".equals(historyBean.getChangeType())) {
				tableName = "st_lms_user_master";
				fieldName = "password";
			} else if("USER_STATUS".equals(historyBean.getChangeType())) {
				tableName = "st_lms_user_master";
				fieldName = "status";
				if("TERMINATE".equals(historyBean.getUpdatedValue())) {
					isStatusUpdate = true;
				}
			}

			pstmt = connection.prepareStatement("UPDATE "+tableName+" SET "+fieldName+"=? WHERE user_id=?;");
			pstmt.setString(1, historyBean.getUpdatedValue());
			pstmt.setInt(2, historyBean.getOrganizationId());
			logger.info("Update User Information : "+pstmt);
			pstmt.executeUpdate();

			if(isStatusUpdate) {
				pstmt = connection.prepareStatement("UPDATE st_lms_user_master SET termination_date=? WHERE user_id=?;");
				pstmt.setString(1, Util.getCurrentTimeString());
				pstmt.setInt(2, historyBean.getOrganizationId());
				logger.info("Update User Termination Date : "+pstmt);
				pstmt.executeUpdate();
			}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new LMSException("SQL Exception", se);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("Exception", e);
		}
	}

	public static void insertUpdateUserHistoryForAGTShift(HistoryBean historyBean, Connection connection) throws LMSException {
		PreparedStatement pstmt = null;
		String tableName = null;
		String fieldName = null;
		boolean isStatusUpdate = false;
		try {
			pstmt = connection.prepareStatement("INSERT INTO st_lms_user_master_history (user_id, change_type, change_value, change_time, done_by_user_id, comments, request_ip) VALUES (?,?,?,?,?,?,?);");
			pstmt.setInt(1, historyBean.getOrganizationId());
			pstmt.setString(2, historyBean.getChangeType());
			pstmt.setString(3, historyBean.getChangeValue());
			pstmt.setTimestamp(4, Util.getCurrentTimeStamp());
			pstmt.setInt(5, historyBean.getDoneByUserId());
			pstmt.setString(6, historyBean.getComments());
			pstmt.setString(7, historyBean.getRequestIp());
			logger.info("Insert st_lms_user_master_history History : "+pstmt);
			pstmt.executeUpdate();

			if("EMAIL_ID".equals(historyBean.getChangeType())) {
				tableName = "st_lms_user_contact_details";
				fieldName = "email_id";
			} else if("PHONE_NUMBER".equals(historyBean.getChangeType())) {
				tableName = "st_lms_user_contact_details";
				fieldName = "phone_nbr";
			} else if("MOBILE_NUMBER".equals(historyBean.getChangeType())) {
				tableName = "st_lms_user_contact_details";
				fieldName = "mobile_nbr";
			} else if("PASSWORD".equals(historyBean.getChangeType())) {
				tableName = "st_lms_user_master";
				fieldName = "password";
			} else if("USER_STATUS".equals(historyBean.getChangeType())) {
				tableName = "st_lms_user_master";
				fieldName = "status";
				if("TERMINATE".equals(historyBean.getUpdatedValue())) {
					isStatusUpdate = true;
				}
			}

			// Changed user name
			pstmt = connection.prepareStatement("UPDATE "+tableName+" SET "+fieldName+"=? , user_name = concat(user_name,'"+historyBean.getDateAppender()+"') WHERE user_id=?;");
			pstmt.setString(1, historyBean.getUpdatedValue());
			pstmt.setInt(2, historyBean.getOrganizationId());
			logger.info("Update User Information : "+pstmt);
			pstmt.executeUpdate();

			if(isStatusUpdate) {
				pstmt = connection.prepareStatement("UPDATE st_lms_user_master SET termination_date=? WHERE user_id=?;");
				pstmt.setString(1, Util.getCurrentTimeString());
				pstmt.setInt(2, historyBean.getOrganizationId());
				logger.info("Update User Termination Date : "+pstmt);
				pstmt.executeUpdate();
			}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new LMSException("SQL Exception", se);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("Exception", e);
		}
	}
	
	public static LinkedHashMap<Integer, Integer> prepareHashMapFromAnyTypeMap(AnyType2AnyTypeMap anyMap ){
		LinkedHashMap<Integer, Integer> hashMap = new LinkedHashMap<Integer, Integer>();
		for(AnyType2AnyTypeMap.Entry entry : anyMap.getEntries()){
			hashMap.put((Integer)entry.getKey(), (Integer)entry.getValue());
		}
		return hashMap;
	}
	
	public static UserInfoBean fetchUserData(int orgId)
			throws LMSException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		UserInfoBean userBean = null;
		try {
			connection = DBConnect.getConnection();
			userBean = new UserInfoBean();
			pstmt = connection
					.prepareStatement("select um.user_id, om.name from st_lms_user_master um inner join st_lms_organization_master om on um.organization_id = om.organization_id where om.organization_id = "
							+ orgId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				userBean.setOrgName(rs.getString("name"));
				userBean.setUserId(rs.getInt("user_id"));
			}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		finally	{
			DBConnect.closeCon(connection);
		}
		return userBean;
	}
	
	public static Map<String, String> fetchStateList() {
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		Map<String, String> stateMap = new TreeMap<String, String>();
		try {
			con = DBConnect.getConnection();
			stmt = con.createStatement();
			rs = stmt
					.executeQuery("select state_code, name from st_lms_state_master where status = 'ACTIVE'");
			while (rs.next()) {
				stateMap.put(rs.getString("state_code"), rs.getString("name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}
		return stateMap;
	}
	
	public static Map<String, String> fetchCityListStateWise(String stateCode) {
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		Map<String, String> cityMap = new TreeMap<String, String>();
		String qyery = "SELECT city_name FROM st_lms_city_master ";
		try {
			con = DBConnect.getConnection();
			stmt = con.createStatement();
			if(!"ALL".equals(stateCode)) {
				qyery += "WHERE state_code = '"+stateCode+"' AND status = 'ACTIVE'";
			} else {
				qyery += "WHERE status = 'ACTIVE'";
			}
			
			rs = stmt
					.executeQuery(qyery);
			while (rs.next()) {
				cityMap.put(rs.getString("city_name"), rs.getString("city_name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}
		return cityMap;
	}
	public static String getSelectedLocale(){
		Cookie userLocale = null;
		HttpServletRequest request = ServletActionContext.getRequest();
		String requestedLocale = "";
		if(request != null){
			Cookie[] cookies = request.getCookies();
			if(cookies != null){
				for (Cookie element : cookies) {
					userLocale = element;
					if (userLocale.getName().equals("LMSLocale")) {
						requestedLocale = userLocale.getValue();
						break;
					}
		
				}
			}
		}
		if(requestedLocale == ""){
			requestedLocale = Locale.getDefault().getLanguage();
		}
		return requestedLocale;
	}

	public static double calTaxablePwt(Double fmtPwtAmt, double govtCommPwt) throws LMSException {
		double taxableSale = 0.0;
		String countryDeployed =Utility.getPropertyValue("COUNTRY_DEPLOYED");
		if ("ZIMBABWE".equalsIgnoreCase(countryDeployed)) {
			taxableSale = fmtPwtAmt;
		} else if ("KENYA".equalsIgnoreCase(countryDeployed)) {
			taxableSale = fmtPwtAmt;
		} else if ("NIGERIA".equalsIgnoreCase(countryDeployed)) {
			taxableSale = fmtPwtAmt;
		}else if ("TANZANIA".equalsIgnoreCase(countryDeployed)) {
			taxableSale = fmtPwtAmt;
		}else if ("GHANA".equalsIgnoreCase(countryDeployed)) {
			taxableSale = fmtPwtAmt-(govtCommPwt * fmtPwtAmt *0.01);
		}else if ("SAFARIBET".equalsIgnoreCase(countryDeployed)) {
			taxableSale = fmtPwtAmt-(govtCommPwt * fmtPwtAmt *0.01);
		}else if ("BENIN".equalsIgnoreCase(countryDeployed)) {
			taxableSale = fmtPwtAmt;
		}else if ("SIKKIM".equalsIgnoreCase(countryDeployed)) {
			taxableSale = fmtPwtAmt;
		}else if ("PHILIP".equalsIgnoreCase(countryDeployed)) {
			taxableSale = fmtPwtAmt;
		} else {
			throw new LMSException();
		}
		return taxableSale;
	}
	
	public static String prepareSMSData(String[] pickedData, String[] playType, StringBuilder cost, StringBuilder ticket, List<String> drawDateTime){
		StringBuilder finalData = new StringBuilder();
		try{
			Map<String, String> betNameMap = prepareBetShortNamesMap();
			
			finalData.append("Ticket No : ").append(ticket).append("\n");
			finalData.append("Ticket Amt : ").append(cost).append("\n");
			
			for(String str : drawDateTime)
				finalData.append("Draw Time:").append(str.split("\\&")[0]).append("\n");
			
			for(int i = 0; i < pickedData.length; i++)
				finalData.append(betNameMap.get(playType[i]) == null ? playType[i].toUpperCase() : betNameMap.get(playType[i]).toUpperCase()).append(":").append(pickedData[i]).append("\n");
		} catch(Exception e) {
			logger.info("Some error occurred in Sending SMS : " + e.getMessage());
		}
		return finalData.toString();
	}
	
	private static Map<String, String> prepareBetShortNamesMap() {
		Map<String, String> betNameMap = new HashMap<String, String>();
		betNameMap.put("Direct1", "D1");
		betNameMap.put("Direct2", "D2");
		betNameMap.put("Direct3", "D3");
		betNameMap.put("Direct4", "D4");
		betNameMap.put("Direct5", "D5");
		betNameMap.put("Direct6", "D6");
		betNameMap.put("Direct7", "D7");
		betNameMap.put("Direct8", "D8");
		betNameMap.put("Direct9", "D9");
		betNameMap.put("Direct10", "D10");
		betNameMap.put("Direct12", "D12");
		betNameMap.put("Perm1", "P1");
		betNameMap.put("Perm2", "P2");
		betNameMap.put("Perm3", "P3");
		betNameMap.put("Perm4", "P4");
		betNameMap.put("Perm5", "P5");
		betNameMap.put("Perm6", "P6");
		betNameMap.put("Perm7", "P7");
		betNameMap.put("Perm8", "P8");
		betNameMap.put("Perm9", "P9");
		betNameMap.put("Perm10", "P10");
		betNameMap.put("Perm11", "P11");
		betNameMap.put("Perm12", "P12");
		betNameMap.put("First12", "F12");
		betNameMap.put("Last12", "L12");
		
		return betNameMap ;
	}

	public static String sendSMS(String smsData){
		logger.info("SMS Data : " + smsData);
		logger.info("SMS Data Length : " + smsData.length());
		String [] numbers = Utility.getPropertyValue("MOBILE_NO_WLS").split(",");
		for(String num : numbers){
			SendSMS smsSend = new SendSMS(smsData,num);
			smsSend.setDaemon(true);
			smsSend.start();
		}
		return null;
	}
	
	public static String appendRoleAgentMappingQuery(String query,String columnName,int roleId){
		logger.info("appendRoleAgentMappingQuery method ");
		StringBuilder builder = new StringBuilder(query);
		builder.append(" and "+columnName+" in (select agent_id from st_lms_role_agent_mapping  where role_id = "+roleId+" ) ");
		return builder.toString();
	}
	
	public static String appendRoleAgentMappingQueryForJoin(int roleId){
		logger.info("appendRoleAgentMappingQuery for join method ");
		String builder="(select agent_id from st_lms_role_agent_mapping  where role_id = "+roleId+" ) ";
		return builder;
	}
	
	
	public static Map<Integer, String> getOrgInfoMap(int userOrgId, String orgType,int roleId)	throws LMSException {
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt =null;
		ResultSet rs = null;
		String query=null;
		Map<Integer,String> retInfoMap=null;
		String appendOrder = "";
		try {
			appendOrder =QueryManager.getAppendOrgOrder();
			
			if((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE") ){
				query ="select org_code orgCode,organization_id from st_lms_organization_master where organization_type=? and parent_id=?  and organization_status <> 'TERMINATE'";
			}else if((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE_NAME")){
				query ="select concat(org_code,'_',name) orgCode,organization_id from st_lms_organization_master where organization_type=? and parent_id=? and organization_status <> 'TERMINATE'";
			}else if((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("NAME_CODE")){
				query ="select concat(name,'_',org_code) orgCode,organization_id from st_lms_organization_master where organization_type=? and parent_id=? and organization_status <> 'TERMINATE' ";
			}else {
				query ="select name orgCode,organization_id from st_lms_organization_master where organization_type=? and parent_id=?  and organization_status <> 'TERMINATE'";
			}
			query = appendRoleAgentMappingQuery(query, "organization_id", roleId)+ " order by  "+appendOrder;
			pstmt = con.prepareStatement(query);
			pstmt.setString(1,orgType);
			pstmt.setInt(2,userOrgId);
			logger.info("getOrgInfoMap method "+ pstmt);
			rs = pstmt.executeQuery();
			retInfoMap=new LinkedHashMap<Integer, String>();
			while (rs.next()) {
				retInfoMap.put(rs.getInt("organization_id"), rs.getString("orgCode"));
			}
		} catch (SQLException se) {
			logger.error("Exception",se);
		}
		catch (Exception e) {
			logger.error("Exception",e);
		}
		finally {
			DBConnect.closeResource(con,pstmt,rs);
		}
		return retInfoMap;
		}

}