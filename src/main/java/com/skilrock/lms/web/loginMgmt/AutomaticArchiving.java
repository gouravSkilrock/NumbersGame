package com.skilrock.lms.web.loginMgmt;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.skilrock.lms.beans.SchedulerDetailsBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameOfflineHelper;
import com.skilrock.lms.coreEngine.userMgmt.common.CommonFunctionsHelper;
import com.skilrock.lms.scheduler.SchedulerCommonFuntionsHelper;

/**
 * 
 * * @author gauravk
 *  This Class does the following activities - 1 - Disable
 * login of all users. 2 - Logout all the users. 3 - Run LMS Archiving. 4 -
 * Enable login of all users. 5 - Send email if ICS does not run successfully.
 */

public class AutomaticArchiving implements Job{

	private static String mailType = "multiple";
	private static String toMultipleEmail = "vishal.verma@skilrock.com,yogesh@skilrock.com";
	private static String toMail = "@skilrock.com";

	Log logger = LogFactory.getLog(AutomaticArchiving.class);
	
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		
		try{
			String jobName = context.getJobDetail().getFullName();
			logger.info("SimpleJob says: " + jobName + " executing at "
					+ new java.util.Date());
			
			Map<String,SchedulerDetailsBean> scheBeanMap =SchedulerCommonFuntionsHelper.getSchedulerBeanMap(context.getJobDetail().getGroup());
			
			SchedulerCommonFuntionsHelper.insertSchedulerGroupHistory(context.getJobDetail().getGroup());
			if(scheBeanMap.size()>0){
				
				if(scheBeanMap.get("Auto_Archiving_SCHEDULER").isActive()){
					ServletContext servletContext = LMSUtility.sc;
					String  isAutoArch =(String)servletContext.getAttribute("autoArchiving"); 
					logger.info("Auto Archiving"+isAutoArch);
					System.out.println("Auto Archiving");
					if(isAutoArch.equalsIgnoreCase("true")){
					
					String errorMsg = null;
					try{
						SchedulerCommonFuntionsHelper.updateSchedulerStart( scheBeanMap.get("Auto_Archiving_SCHEDULER").getJobId());
						logger.info("Inside Auto Archiving");
				
							logger.info("Inside Auto Archiving");
						startArchiving();	
						
						SchedulerCommonFuntionsHelper.updateSchedulerEnd(scheBeanMap.get("Auto_Archiving_SCHEDULER").getJobId());
					}catch (Exception e) {
						logger.error("Exception in Auto_Archiving_SCHEDULER ", e);
						if(e.getMessage()!=null){
							errorMsg =e.getMessage();
						}else{
							
							errorMsg="Error Occurred Msg Is Null ";
						}
					}
					if(errorMsg!=null){
						SchedulerCommonFuntionsHelper.updateSchedulerError( scheBeanMap.get("Auto_Archiving_SCHEDULER").getJobId(), errorMsg);
					}
					}else{
						logger.info("System is not configured for the automatic archiving");
					}
					
					
				}
			}
			/*ServletContext servletContext = LMSUtility.sc;
			String  isAutoArch =(String)servletContext.getAttribute("autoArchiving"); 
			logger.info("Auto Archiving"+isAutoArch);
			System.out.println("Auto Archiving");
			if(isAutoArch.equalsIgnoreCase("true")){
				logger.info("Inside Auto Archiving");
					startArchiving();	
			}else{
				logger.info("System is not configured for the automatic archiving");
			}*/
			
		}catch (LMSException e) {
			logger.error("LMSException in Weekly Job Scheduler  ", e);
		}catch (Exception e) {
			logger.error("Exception in Weekly Job Scheduler  ", e);
		}
	}

	public void startArchiving() throws LMSException{
		Statement stmt = null;
		ResultSet rs = null;
		//int queryId;
		boolean result = false;
		Connection con = null;
		//boolean flag = false;
		//String errMessage = null;
		//String subject = "FOR TESTING  - LMS ERROR REPORTING SYSTEM (LMS ERRORS) ";
		//String databaseName = DBConnect.getDatabaseName();
/*		String body = "*********************************************************************************************************\n\n"
				+ "***ICS FOR LMS***"
				+ "\n\n"
				+ "Error occured in Database: "
				+ databaseName
				+ "\n\n You can check these errors by going through the below details : \n\n\n";*/
		try {
			con = DBConnect.getConnection();
			stmt = con.createStatement();
			logger.info("Stop login of all users");
			stopLoginAllUsers();
			logger.info("Logout all users before archiving !");
			logOutAll(); // Logout all users
			CallableStatement pstmt = con
					.prepareCall("{call runLMSArchiving()}");
			pstmt.executeUpdate();

			rs = stmt
					.executeQuery("select * from tempdate_history where date(processing_time)='"
							+ new java.sql.Date(new Date().getTime()) + "'");
			if (rs.next()) {
				result = true;
			}
			if (result) {
				logger.info("LMS Archiving run successfully :");
			} else {
				logger.info("LMS Archiving does not run successfully :");
				if (mailType.equalsIgnoreCase("multiple")) {
					toMultipleEmail = toMultipleEmail.trim();
					String[] multipleEmailArray = toMultipleEmail.split(",");
					new MailSend().sendEmailMany(multipleEmailArray, "LMS ARCHIVING DOES NOT RUN",
							"LMS archiving does not run successfully on "
									+ new Timestamp(new Date().getTime())
									+ ". Please look over the issue.");
				}
				
				/*MailSend.sendMail(toMail,
						"LMS archiving does not run successfully on "
								+ new Timestamp(new Date().getTime())
								+ ". Please look over the issue.",
						"LMS ARCHIVING DOES NOT RUN");*/
			}

			stopLoginAllUsers();
			logger.info("Login allowed all users after archiving !");
		/*	logger.info("ICS Process start.");
			rs = stmt
					.executeQuery("select  qm.id,error_msg from st_ics_query_master qm inner join st_ics_daily_query_status qs on qm.id=qs.query_id where qm.query_status='ACTIVE' and qm.is_critical='YES' and date(ics_run_date)='"
							+ new java.sql.Date(new Date().getTime())
							+ "' and is_success=1");
			while (rs.next()) {
				flag = true;
				queryId = rs.getInt("id");
				errMessage = rs.getString("error_msg");
				body = body + "Query Id :" + queryId + "\n   Error Message : "
						+ errMessage + "\n";
			}
			if (!flag) {
				logger.info("ICS run successfully.");
			} else {
				logger.info("ICS does not run successfully.");

				if (mailType.equalsIgnoreCase("multiple")) {
					toMultipleEmail = toMultipleEmail.trim();
					String[] multipleEmailArray = toMultipleEmail.split(",");
					new MailSend().sendEmailMany(multipleEmailArray, subject,
							body);
				} else {
					MailSend.sendMail(toMail, body, subject);
				}

			}*/
		}  catch (Exception e) {
			if (LMSFilterDispatcher.stopLogInUsers) {
				LMSFilterDispatcher.stopLogInUsers = false;
			}
			logger.error("Error occured in running LMS Archiving",e);
			if (mailType.equalsIgnoreCase("multiple")) {
				toMultipleEmail = toMultipleEmail.trim();
				String[] multipleEmailArray = toMultipleEmail.split(",");
				new MailSend().sendEmailMany(multipleEmailArray, "LMS ARCHIVING DOES NOT RUN",
						"LMS archiving does not run successfully on "
								+ new Timestamp(new Date().getTime())
								+ ". Please look over the issue.");
			}
			logger.info("Exception ",e);
			throw new LMSException("Exception"+e.getMessage());
			/*MailSend.sendMail(toMail,
					"LMS archiving does not run successfully on "
>>>>>>> 1.1.2.2.2.1
							+ new Timestamp(new Date().getTime())
							+ ". Please look over the issue.",
<<<<<<< AutomaticArchiving.java
					"LMS ARCHIVING DOES NOT RUN");
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE) ;
=======
					"LMS ARCHIVING DOES NOT RUN");*/
		

		}finally {
			DBConnect.closeCon(con);
			DBConnect.closeRs(rs);
			DBConnect.closeStmt(stmt);
			
		} 
	}

	public void stopLoginAllUsers() {
		LMSFilterDispatcher.stopLogInUsers = (!LMSFilterDispatcher.stopLogInUsers);
		if (!LMSFilterDispatcher.stopLogInUsers) {
			System.out.println("Archiving Complete : All users login allowed");
		} else {
			System.out.println("Archving Start : All users login blocked.");
		}
	}

	@SuppressWarnings("unchecked")
	public void logOutAll() throws IOException {
		HttpSession session = null;
		String userName = null;
		int userId = 0;
		ServletContext sc = LMSUtility.sc;
		Map<String, HttpSession> currentUserSessionMap = (Map<String, HttpSession>) sc
				.getAttribute("LOGGED_IN_USERS");
		System.out.println(" LOGGED_IN_USERS maps is " + currentUserSessionMap);
		int forcedOut = 0;
		if (currentUserSessionMap != null) {
			Iterator<Map.Entry<String, HttpSession>> iter = currentUserSessionMap
					.entrySet().iterator();
			int alreadyOut = 0;

			while (iter.hasNext()) {
				Map.Entry<String, HttpSession> pair = (Map.Entry<String, HttpSession>) iter
						.next();
				userName = pair.getKey();
				session = pair.getValue();
				if ("admin".equalsIgnoreCase(userName)) {
					continue;
				}
				if (session == null
						|| !CommonFunctionsHelper.isSessionValid(session)) {
					alreadyOut++;
					continue;
				}

				session.removeAttribute("USER_INFO");
				session.removeAttribute("ACTION_LIST");
				session.removeAttribute("PRIV_MAP");
				session.invalidate();
				session = null;
				System.out.println("Log out Successfully and Session is "
						+ session);
				if (DrawGameOfflineHelper.checkOfflineUser(userId)) {
					if (DrawGameOfflineHelper.updateLogoutSuccess(userName)) {
						forcedOut++;
					} else {
						alreadyOut++;
					}
					return;
				} else {
					forcedOut++;
				}
			}

			session = currentUserSessionMap.get("admin");
			currentUserSessionMap = new HashMap<String, HttpSession>();
			currentUserSessionMap.put("admin", session);
			sc.setAttribute("LOGGED_IN_USERS", currentUserSessionMap);
			System.out.println("Forced Out:"+forcedOut+"Already Out :"+alreadyOut);
		}
		System.out.println(sc.getAttribute("LOGGED_IN_USERS"));
	}
	public static void main(String[] args) {
		// new AutomaticArchiving().startArchiving() ;
		
	}

}
