package com.skilrock.lms.api.reports;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.skilrock.lms.api.common.TpUtilityHelper;
import org.codehaus.xfire.transport.http.XFireServletController;

import com.skilrock.lms.api.reports.beans.LmsApiReportConsolidateGameDataBean;
import com.skilrock.lms.api.reports.beans.LmsApiReportRequestDataBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.web.drawGames.common.Util;

public class LmsReportsServicesImpl implements  ILmsReportsServices {
	static Log logger=LogFactory.getLog(LmsReportsServicesImpl.class); 
	final static long oneDay = 1 * 24 * 60 * 60 * 1000;
	
	public LmsApiReportConsolidateGameDataBean getDrawGameReports(LmsApiReportRequestDataBean lmsApiReportRequestDataBean) {
		
	HttpServletRequest request = XFireServletController.getRequest();
	String ip = request.getRemoteHost();
	logger.info("System User Name Sent IS :"+lmsApiReportRequestDataBean.getSystemUserName()+"System User Password Sent IS:"+lmsApiReportRequestDataBean.getSystemUserPassword()+" AND IP Sent IS :"+ip);
	LmsApiReportConsolidateGameDataBean lmsApiReportConsolidateGameDataBean=new LmsApiReportConsolidateGameDataBean();

	try{
		
	validateLmsApiReportRequestDataBean(lmsApiReportRequestDataBean,ip);
	lmsApiReportRequestDataBean=decideDatesForReport(lmsApiReportRequestDataBean);
	
	String raffleTktType=(String) request.getSession().getServletContext().getAttribute("raffle_ticket_type");
	String refMerchantId = (String)  request.getSession().getServletContext().getAttribute("REF_MERCHANT_ID");
	LmsReportsServicesApiHelper lmsReportsServicesApiHelper=new LmsReportsServicesApiHelper();
	lmsApiReportConsolidateGameDataBean= lmsReportsServicesApiHelper.getDrawGameReport(lmsApiReportConsolidateGameDataBean,lmsApiReportRequestDataBean,raffleTktType,refMerchantId);
	
	}catch (ParseException e) {
		lmsApiReportConsolidateGameDataBean.setErrorCode("500");
		lmsApiReportConsolidateGameDataBean.setIsSuccess(false);
		logger.info("Date Parsing Error");
	}catch (LMSException e) {
		lmsApiReportConsolidateGameDataBean.setErrorCode(e.getMessage());
		lmsApiReportConsolidateGameDataBean.setIsSuccess(false);
		logger.info("Date Parsing Error");
		return lmsApiReportConsolidateGameDataBean;
	}catch (Exception e) {
		lmsApiReportConsolidateGameDataBean.setErrorCode("500");
		lmsApiReportConsolidateGameDataBean.setIsSuccess(false);
		logger.info("Unknown Exception");
	}
	return lmsApiReportConsolidateGameDataBean;
	
	}
	
	public void validateLmsApiReportRequestDataBean(LmsApiReportRequestDataBean lmsApiReportRequestDataBean , String ip) throws LMSException{
		if(!TpUtilityHelper.validateTpSystemUser(ip,lmsApiReportRequestDataBean.getSystemUserName(),lmsApiReportRequestDataBean.getSystemUserPassword())){
			logger.info("Authentication Error");
			throw new LMSException("102"); // INVALID SYSTEM USER NAME OR PASSWORD
		}
		if(Util.getGameId(lmsApiReportRequestDataBean.getGameCode())==0){
			logger.info("Wrong Game Code Error");
			throw new LMSException("500"); // DATA ERROR
		}
	}
	
	
	public LmsApiReportRequestDataBean decideDatesForReport(LmsApiReportRequestDataBean lmsApiReportRequestDataBean) throws LMSException, ParseException{
		
		long diffInDays=0;
		boolean isDateRangeCrossed=false;
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		
		int noOfDays=Integer.parseInt(lmsApiReportRequestDataBean.getNoOfDaysForCurrentDate());
		if(lmsApiReportRequestDataBean.getIsDateWise()){
			if(!isDateValid(lmsApiReportRequestDataBean.getStartDate(),sdf)  || !isDateValid(lmsApiReportRequestDataBean.getEndDate(),sdf)){
				logger.info("Date Parsing Error");
				throw new LMSException("500"); // DATA ERROR
			}else {
				diffInDays=(sdf.parse(lmsApiReportRequestDataBean.getEndDate()).getTime()-sdf.parse(lmsApiReportRequestDataBean.getStartDate()).getTime())/oneDay;
				if(diffInDays>15)
				isDateRangeCrossed=true;
				else if(diffInDays<0)
					throw new LMSException("500");  // DATA ERROR
			}
		}else if(noOfDays<0){
			logger.info("Date Parsing Error");
			throw new LMSException("500");  // DATA ERROR
		}else if(noOfDays>15){
			lmsApiReportRequestDataBean.setEndDate(new Timestamp(Calendar.getInstance().getTime().getTime()).toString().substring(0,10));
			isDateRangeCrossed=true;
		}else {
			lmsApiReportRequestDataBean.setEndDate(new Timestamp(Calendar.getInstance().getTime().getTime()).toString().substring(0,10));
			Calendar cal=Calendar.getInstance();
			Date d=sdf.parse(lmsApiReportRequestDataBean.getEndDate());
			cal.setTime(d);
			cal.add(Calendar.DATE, -noOfDays);
			lmsApiReportRequestDataBean.setStartDate(new Timestamp(cal.getTime().getTime()).toString().substring(0,10));
		}
		
		if(isDateRangeCrossed){
			Calendar cal=Calendar.getInstance();
			Date d=sdf.parse(lmsApiReportRequestDataBean.getEndDate());
			cal.setTime(d);
			cal.add(Calendar.DATE, -15);
			lmsApiReportRequestDataBean.setStartDate(new Timestamp(cal.getTime().getTime()).toString().substring(0,10));
		}

		lmsApiReportRequestDataBean.setStartDate(lmsApiReportRequestDataBean.getStartDate()+ " 00:00:00");
		lmsApiReportRequestDataBean.setEndDate(lmsApiReportRequestDataBean.getEndDate()+ " 23:59:59");
		
		
		return lmsApiReportRequestDataBean;
	}
	
	
	public boolean isDateValid(String date ,SimpleDateFormat sdf) {
		Date testDate = null;
		String errorMessage = null;
		try {
			// CHECK NOT NULL
			testDate = sdf.parse(date);
		} catch (ParseException e) {
			errorMessage = "***INVALID DATE***";
			logger.info(errorMessage);
			return false;
		}
		if (!sdf.format(testDate).equals(date)) {
			errorMessage = "***INCORRECT DATE FORMAT***";
			logger.info(errorMessage);
			return false;
		}

		return true;
	}
}
