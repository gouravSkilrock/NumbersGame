package com.skilrock.lms.api.lmsWrapper.common;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.api.lmsWrapper.beans.LmsWrapperDrawScheduleDataBean;
import com.skilrock.lms.api.lmsWrapper.beans.LmsWrapperDrawscheduleBeanResult;
import com.skilrock.lms.api.lmsWrapper.beans.LmsWrapperPerformDrawDataBean;
import com.skilrock.lms.api.lmsWrapper.beans.LmsWrapperRandomIdRequestBean;
import com.skilrock.lms.api.lmsWrapper.beans.LmsWrapperRandomIdResponseBean;
import com.skilrock.lms.api.lmsWrapper.beans.LmsWrapperResultSubmissionDrawDataBean;
import com.skilrock.lms.api.lmsWrapper.beans.LmsWrapperUserIdMappingBean;
import com.skilrock.lms.beans.UserIdMappingBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.common.utility.MD5Encoder;
import com.skilrock.lms.coreEngine.drawGames.drawMgmt.DrawGameMgmtHelper;
import com.skilrock.lms.dge.beans.DrawScheduleBean;
import com.skilrock.lms.dge.beans.DrawScheduleBeanResult;
import com.skilrock.lms.dge.beans.ManualWinningBean;
import com.skilrock.lms.web.drawGames.common.DrawGameRPOS;
import com.skilrock.lms.web.drawGames.common.Util;

public class LmsWrapperDrawGameMgmtHelper {

	static Log logger = LogFactory.getLog(LmsWrapperDrawGameMgmtHelper.class);
	
	public LmsWrapperDrawscheduleBeanResult fetchDrawListForResSubmission(LmsWrapperResultSubmissionDrawDataBean resultSubmissionDrawDataBean){
		LmsWrapperDrawscheduleBeanResult lmsWrapperDrawscheduleBeanResult=new LmsWrapperDrawscheduleBeanResult();
		int fetchedUserId=WrapperUtility.getUserIdFromUserName(resultSubmissionDrawDataBean.getUserName());
		
		 String fromDate=resultSubmissionDrawDataBean.getFromDate();
		 String fromHour=resultSubmissionDrawDataBean.getFromHour();
		 String fromMin=resultSubmissionDrawDataBean.getFromMin();
		 String fromSec=resultSubmissionDrawDataBean.getFromSec();
		 String toDate=resultSubmissionDrawDataBean.getToDate();
		 String toHour=resultSubmissionDrawDataBean.getToHour();
		 String toMin=resultSubmissionDrawDataBean.getToMin();
		 String toSec=resultSubmissionDrawDataBean.getToSec();
		 String performStatus[]=resultSubmissionDrawDataBean.getPerformStatus();
		 String gameNo=resultSubmissionDrawDataBean.getGameNo();
		int drawId=resultSubmissionDrawDataBean.getDrawId();
		
		try{
			
		Calendar fromDrawCal = Calendar.getInstance();
		Calendar toDrawCal = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date frdate = null;
		Date todateObj = null;
		Timestamp fromTimeStamp = null;
		Timestamp toTimeStamp = null;
		if ( fromDate != null) {
			if (fromDate.length() == 0) {
				fromDate = null;
			} else {
				logger.debug("len-----" + fromDate.length());

			}
		}
		if (toDate != null) {
			if (toDate.length() == 0) {
				toDate = null;
			}
		}
		if (fromHour != null && fromMin != null && fromSec != null
				&& toHour != null && toMin != null && toSec != null) {

			if (fromHour.length() == 0) {
				fromHour = "00";
			}
			if (fromMin.length() == 0) {
				fromMin = "00";
			}
			if (fromSec.length() == 0) {
				fromSec = "00";
			}
			if (toHour.length() == 0) {
				toHour = "23";
			}
			if (toMin.length() == 0) {
				toMin = "59";
			}
			if (toSec.length() == 0) {
				toSec = "59";
			}
		}
		gameNo = gameNo.substring(0, gameNo.indexOf("-"));
		String gameName = Util.getGameName(Integer.parseInt(gameNo));
		
			if (fromDate != null) {
				frdate = format.parse(fromDate);
				fromDrawCal.setTime(frdate);
				fromDrawCal.set(Calendar.HOUR, Integer.parseInt(fromHour));
				fromDrawCal.set(Calendar.MINUTE, Integer.parseInt(fromMin));
				fromDrawCal.set(Calendar.SECOND, Integer.parseInt(fromSec));
				fromTimeStamp = new Timestamp(fromDrawCal.getTime().getTime());
			}
			if (toDate != null) {
				todateObj = format.parse(toDate);
				toDrawCal.setTime(todateObj);
				toDrawCal.set(Calendar.HOUR, Integer.parseInt(toHour));
				toDrawCal.set(Calendar.MINUTE, Integer.parseInt(toMin));
				toDrawCal.set(Calendar.SECOND, Integer.parseInt(toSec));
				toTimeStamp = new Timestamp(toDrawCal.getTime().getTime());
			}
		

		DrawScheduleBean drawScheduleBean = new DrawScheduleBean();
		drawScheduleBean.setDraw_id(drawId);
		drawScheduleBean.setGameNo(Integer.parseInt(gameNo));
		drawScheduleBean.setGameId(Util.getGameIdFromGameNumber(Integer.parseInt(gameNo)));
		drawScheduleBean.setStartDate(fromTimeStamp);
		drawScheduleBean.setEndDate(toTimeStamp);
		System.out.println("performStatus[0] is ..." +performStatus[0]);
		drawScheduleBean.setStatus(performStatus[0].equals("ALL") ? "FREEZE"
				: null);
		System.out.println("heej" + performStatus[0]);
		drawScheduleBean.setUserId(fetchedUserId);
		System.out.println("The fetchedUserId is ..."+fetchedUserId);
		drawScheduleBean.setPerformStatus(performStatus);
		DrawGameMgmtHelper helper = new DrawGameMgmtHelper();
			
		
		ArrayList<DrawScheduleBeanResult> drawResultList=helper.getManualEntryData(drawScheduleBean);
				
		DrawScheduleBeanResult drawScheduleBeanResult=new DrawScheduleBeanResult();
		ArrayList<LmsWrapperDrawScheduleDataBean> drawSchedulerDataBeanList=new ArrayList<LmsWrapperDrawScheduleDataBean>();
		LmsWrapperDrawScheduleDataBean drawSchedulerDataBean=null;
		Iterator<DrawScheduleBeanResult> it2=drawResultList.iterator();
		while(it2.hasNext()){
			drawScheduleBeanResult=it2.next();
			drawSchedulerDataBean=new LmsWrapperDrawScheduleDataBean();
			drawSchedulerDataBean.setCheckPPR(drawScheduleBeanResult.getCheckPPR());
			drawSchedulerDataBean.setDrawDateTime(drawScheduleBeanResult.getDrawDateTime());
			drawSchedulerDataBean.setDrawDay(drawScheduleBeanResult.getDrawDay());
			drawSchedulerDataBean.setDrawId(drawScheduleBeanResult.getDrawId());
			drawSchedulerDataBean.setDrawStatus(drawScheduleBeanResult.getDrawStatus());
			drawSchedulerDataBean.setFreezeDateTime(drawScheduleBeanResult.getFreezeDateTime());
			drawSchedulerDataBean.setPerformStatus(drawScheduleBeanResult.getPerformStatus());
			drawSchedulerDataBean.setWinningResult(drawScheduleBeanResult.getWinningResult());
			drawSchedulerDataBeanList.add(drawSchedulerDataBean);
			
		}
		lmsWrapperDrawscheduleBeanResult.setDrawScheduleDataBeanList(drawSchedulerDataBeanList);
		}
		catch (Exception e) {
			// TODO: handle exception
		}
	
		return lmsWrapperDrawscheduleBeanResult;
	
	}
	
	public String performManualWinningEntry(LmsWrapperPerformDrawDataBean performDrawDataBean) {
	
	/*	
		if(!validateResultSubmitUser(performDrawDataBean.getUserName())){
			return "FAILED";
		}*/
		
		String result="";
		int fetchedUserId=WrapperUtility.getUserIdFromUserName(performDrawDataBean.getUserName());
		if(fetchedUserId==0)
			return "FAILED";
		ManualWinningBean mwBean = new ManualWinningBean();
		mwBean.setGameNumber(performDrawDataBean.getGameNo());
		mwBean.setGameId(Util.getGameIdFromGameNumber(Integer.parseInt(performDrawDataBean.getGameNo())));
		mwBean.setDrawIds(performDrawDataBean.getDrawIds());
		mwBean.setDrawType(performDrawDataBean.getDrawType());
		
		System.out.println("draw type" + performDrawDataBean.getDrawType());
		mwBean.setCardType(performDrawDataBean.getCardType());
		System.out.println("card Type" + performDrawDataBean.getCardType());
		mwBean.setWinningNumbers(performDrawDataBean.getWinningNumbers());
		System.out.println("winning numbers" + performDrawDataBean.getWinningNumbers());
		mwBean.setWinNumSize(performDrawDataBean.getWinNumSize());
		System.out.println("winNumSize is "+performDrawDataBean.getWinNumSize());	
		mwBean.setUserId(fetchedUserId);
		mwBean.setMachineNumbers(performDrawDataBean.getMachineNumbers());
		DrawGameMgmtHelper helper = new DrawGameMgmtHelper();
		
		try {
			result = helper.performManualWinningEntry(mwBean);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DrawGameRPOS.onStartNewData();
		return result;
	}
	
	public LmsWrapperDrawscheduleBeanResult fetchDrawListForMachineResSubmission(LmsWrapperResultSubmissionDrawDataBean resultSubmissionDrawDataBean){
		LmsWrapperDrawscheduleBeanResult lmsWrapperDrawscheduleBeanResult=new LmsWrapperDrawscheduleBeanResult();
		int fetchedUserId=WrapperUtility.getUserIdFromUserName(resultSubmissionDrawDataBean.getUserName());
		
		 String fromDate=resultSubmissionDrawDataBean.getFromDate();
		 String fromHour=resultSubmissionDrawDataBean.getFromHour();
		 String fromMin=resultSubmissionDrawDataBean.getFromMin();
		 String fromSec=resultSubmissionDrawDataBean.getFromSec();
		 String toDate=resultSubmissionDrawDataBean.getToDate();
		 String toHour=resultSubmissionDrawDataBean.getToHour();
		 String toMin=resultSubmissionDrawDataBean.getToMin();
		 String toSec=resultSubmissionDrawDataBean.getToSec();
		 String performStatus[]=resultSubmissionDrawDataBean.getPerformStatus();
		 String gameNo=resultSubmissionDrawDataBean.getGameNo();
		int drawId=resultSubmissionDrawDataBean.getDrawId();
		
		try{
			
		Calendar fromDrawCal = Calendar.getInstance();
		Calendar toDrawCal = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date frdate = null;
		Date todateObj = null;
		Timestamp fromTimeStamp = null;
		Timestamp toTimeStamp = null;
		if ( fromDate != null) {
			if (fromDate.length() == 0) {
				fromDate = null;
			} else {
				logger.debug("len-----" + fromDate.length());

			}
		}
		if (toDate != null) {
			if (toDate.length() == 0) {
				toDate = null;
			}
		}
		
		gameNo = gameNo.substring(0, gameNo.indexOf("-"));
	//	String gameName = Util.getGameName(Integer.parseInt(gameNo));
		
			if (fromDate != null) {
				frdate = format.parse(fromDate);
				fromDrawCal.setTime(frdate);
				fromDrawCal.set(Calendar.HOUR, Integer.parseInt(fromHour));
				fromDrawCal.set(Calendar.MINUTE, Integer.parseInt(fromMin));
				fromDrawCal.set(Calendar.SECOND, Integer.parseInt(fromSec));
				fromTimeStamp = new Timestamp(fromDrawCal.getTime().getTime());
			}
			if (toDate != null) {
				todateObj = format.parse(toDate);
				toDrawCal.setTime(todateObj);
				toDrawCal.set(Calendar.HOUR, Integer.parseInt(toHour));
				toDrawCal.set(Calendar.MINUTE, Integer.parseInt(toMin));
				toDrawCal.set(Calendar.SECOND, Integer.parseInt(toSec));
				toTimeStamp = new Timestamp(toDrawCal.getTime().getTime());
			}
		

		DrawScheduleBean drawScheduleBean = new DrawScheduleBean();
		drawScheduleBean.setDraw_id(drawId);
		drawScheduleBean.setGameNo(Integer.parseInt(gameNo));
		drawScheduleBean.setStartDate(fromTimeStamp);
		drawScheduleBean.setEndDate(toTimeStamp);
		System.out.println("performStatus[0] is ..." +performStatus[0]);
		drawScheduleBean.setStatus(performStatus[0].equals("ALL") ? "FREEZE"
				: null);
		System.out.println("heej" + performStatus[0]);
		drawScheduleBean.setUserId(fetchedUserId);
		System.out.println("The fetchedUserId is ..."+fetchedUserId);
		drawScheduleBean.setPerformStatus(performStatus);
		DrawGameMgmtHelper helper = new DrawGameMgmtHelper();
			
		
		ArrayList<DrawScheduleBeanResult> drawResultList=helper.getManualMachineNumberEntryData(drawScheduleBean);
				
		DrawScheduleBeanResult drawScheduleBeanResult=new DrawScheduleBeanResult();
		ArrayList<LmsWrapperDrawScheduleDataBean> drawSchedulerDataBeanList=new ArrayList<LmsWrapperDrawScheduleDataBean>();
		LmsWrapperDrawScheduleDataBean drawSchedulerDataBean=null;
		Iterator<DrawScheduleBeanResult> it2=drawResultList.iterator();
		while(it2.hasNext()){
			drawScheduleBeanResult=it2.next();
			drawSchedulerDataBean=new LmsWrapperDrawScheduleDataBean();
			//drawSchedulerDataBean.setCheckPPR(drawScheduleBeanResult.getCheckPPR());
			drawSchedulerDataBean.setDrawDateTime(drawScheduleBeanResult.getDrawDateTime());
			//drawSchedulerDataBean.setDrawDay(drawScheduleBeanResult.getDrawDay());
			drawSchedulerDataBean.setDrawId(drawScheduleBeanResult.getDrawId());
			drawSchedulerDataBean.setDrawStatus(drawScheduleBeanResult.getDrawStatus());
			drawSchedulerDataBean.setFreezeDateTime(drawScheduleBeanResult.getFreezeDateTime());
			drawSchedulerDataBean.setPerformStatus(drawScheduleBeanResult.getPerformStatus());
			drawSchedulerDataBean.setWinningResult(drawScheduleBeanResult.getWinningResult());
			drawSchedulerDataBeanList.add(drawSchedulerDataBean);
			
		}
		lmsWrapperDrawscheduleBeanResult.setDrawScheduleDataBeanList(drawSchedulerDataBeanList);
		}
		catch (Exception e) {
			// TODO: handle exception
		}
	
		return lmsWrapperDrawscheduleBeanResult;
	
	}
	
	
	
	
	public String performManualWinningMachineNumberEntry(LmsWrapperPerformDrawDataBean performDrawDataBean) {
		
			String result="";
			int fetchedUserId=WrapperUtility.getUserIdFromUserName(performDrawDataBean.getUserName());
			if(fetchedUserId==0)
				return "FAILED";
			ManualWinningBean mwBean = new ManualWinningBean();
			mwBean.setGameNumber(performDrawDataBean.getGameNo());
			mwBean.setDrawIds(performDrawDataBean.getDrawIds());
			mwBean.setDrawType(performDrawDataBean.getDrawType());
			
			System.out.println("draw type" + performDrawDataBean.getDrawType());
			mwBean.setCardType(performDrawDataBean.getCardType());
			System.out.println("card Type" + performDrawDataBean.getCardType());
			mwBean.setWinningNumbers(performDrawDataBean.getWinningNumbers());
			System.out.println("winning numbers" + performDrawDataBean.getWinningNumbers());
			mwBean.setWinNumSize(performDrawDataBean.getWinNumSize());
			System.out.println("winNumSize is "+performDrawDataBean.getWinNumSize());	
			mwBean.setUserId(fetchedUserId);
			DrawGameMgmtHelper helper = new DrawGameMgmtHelper();
			
			try {
				result = helper.performManualWinningMachineNumberEntry(mwBean);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			DrawGameRPOS.onStartNewData();
			return result;
		}
	
	
	
	public boolean validateResultSubmitUser(String userName){
		String authenticatePassword="";
		String password="";
		
		Connection con =DBConnect.getConnection();
 		String fetchuserData="select user_name,password from st_lms_user_master where user_name='"+userName+"'";
          try {
			Statement stmt=con.createStatement();
			ResultSet rs=stmt.executeQuery(fetchuserData);
			if(rs.next()){
				userName=rs.getString("user_name");
				password=rs.getString("password");
					
					authenticatePassword=(String)LMSUtility.sc.getAttribute("WRAPPER_USER_PASSWORD");
			}else{
				System.out.println("user Name does not exist");
				return false;
			}
			
			if(MD5Encoder.encode(authenticatePassword).equalsIgnoreCase(password)){
				
				return true;
			}
			System.out.println("password does not match");
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}finally{
			try{
				con.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return false;	
		}
	
	// TEMP 
	public ArrayList<Integer> getAllUserIds(){

		ArrayList<Integer> allRetUsers =  null;
		ResultSet rs = null;
		Connection con = null;
		Statement stmt = null; 
		try {
        	allRetUsers = new ArrayList<Integer>();
        	con =DBConnect.getConnection();
			stmt=con.createStatement();
			rs=stmt.executeQuery("select user_id from st_lms_user_master where organization_type = 'RETAILER'"); // CHECK IN FUTURE FOr RANDOM ID AS WELL
			while(rs.next()){
				allRetUsers.add(rs.getInt("user_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBConnect.closeConnection(con, stmt , rs);
		}
		return allRetUsers;
		}
	
	public void updateInsertRandomIdInLMS(LmsWrapperRandomIdRequestBean lmsWrapperRandomIdRequestBean , LmsWrapperRandomIdResponseBean lmsWrapperRandomIdResponseBean){
		
		Connection con = null;
		UserIdMappingBean userIdMappingBean = null;
		LmsWrapperUserIdMappingBean randomBean =  null;

		int errorCode = 0;
		boolean isSuccess =false;
		String errorMessage  = null;
		try {
				randomBean = lmsWrapperRandomIdRequestBean.getLmsWrapperUserIdMappingBean();
				userIdMappingBean = new UserIdMappingBean();
				userIdMappingBean.setAll(randomBean.isAll());
				userIdMappingBean.setUserId(randomBean.getUserId());
				userIdMappingBean.setSpecific(!randomBean.isAll());
				userIdMappingBean.setExpiryDays(randomBean.getExpiryDays());
				userIdMappingBean.setThirdPartyGeneration(true);
				userIdMappingBean.setActivity(randomBean.getActivity());
				userIdMappingBean.setDoneByUserId(randomBean.getDoneByUserId());
				userIdMappingBean.setRequesInitiateTime(randomBean.getRequesInitiateTime());
				userIdMappingBean.setAdvUserMappingIdMap(randomBean.getAdvUserMappingIdMap());
				userIdMappingBean.setUserMappingIdMap(randomBean.getUserMappingIdMap());
				userIdMappingBean.setNewCodeExpiryDateTime(randomBean.getNewCodeExpiryDateTime());
				userIdMappingBean.setNewGenerationDateTime(randomBean.getNewGenerationDateTime());
				userIdMappingBean.setAdvGenerationDateTime(randomBean.getAdvGenerationDateTime());
				userIdMappingBean.setAdvCodeExpiryDateTime(randomBean.getAdvCodeExpiryDateTime());
				con = DBConnect.getConnection();
				// CODE TO GENERATE THE ID
				CommonMethods.getUserIdToGenMappindId(userIdMappingBean ,con);
				errorMessage = "SUCCESS";
				isSuccess = true;
				errorCode = 100;
			}catch (LMSException e) {
				isSuccess =false;
				errorCode = e.getErrorCode();
				errorMessage  = e.getErrorMessage();
				logger.error(e.getErrorMessage());
			} catch (Exception e) {
				errorCode = LMSErrors.GENERAL_EXCEPTION_ERROR_CODE;
				isSuccess =false;
				errorMessage  = LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE;
				logger.error("EXCEPTION : - " , e);
			}finally{
				lmsWrapperRandomIdResponseBean.setSuccess(isSuccess);
				lmsWrapperRandomIdResponseBean.setErrorCode(errorCode);
				lmsWrapperRandomIdResponseBean.setErrorMessage(errorMessage);
				DBConnect.closeCon(con);
			}
		}
}
