package com.skilrock.lms.controller.userMgmtController;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skilrock.lms.api.common.TpUtilityHelper;
import com.skilrock.lms.api.lmsWrapper.common.WrapperUtility;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.common.utility.ResponsibleGaming;
import com.skilrock.lms.coreEngine.sportsLottery.common.SLEException;
import com.skilrock.lms.coreEngine.userMgmt.common.CommonFunctionsHelper;
import com.skilrock.lms.dao.UserMgmtDao;
import com.skilrock.lms.dao.common.DaoFactory;
import com.skilrock.lms.rest.services.bean.TpCommonStatusBean;
import com.skilrock.lms.sportsLottery.userMgmt.javaBeans.UserDataBean;
import com.skilrock.lms.web.drawGames.common.Util;

public class UserMgmtController {
	private static Logger loggger = LoggerFactory.getLogger(UserMgmtController.class);
	private volatile static UserMgmtController userMgmtController = null;

	private UserMgmtController(){}
	public static UserMgmtController getInstance() {
		if (userMgmtController == null) {
			synchronized (UserMgmtController.class) {
				if (userMgmtController == null) {
					loggger.info("getInstance(): First time getInstance was invoked!");
					userMgmtController = new UserMgmtController();
				}
			}
		}
		return userMgmtController;
	}

	public UserDataBean getUserInfo(String userName) throws Exception {
		loggger.info("-- Inside getUserInfo in Controller --");
		long startTime = System.currentTimeMillis();
		UserMgmtDao userDAO = null;
		UserDataBean userBean = null;
		try {
			userDAO = DaoFactory.getUserMgmtDao();
			userBean = userDAO.getUserInfo(userName);
		}catch (SLEException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
		long endTime = System.currentTimeMillis();
		loggger.info("Time Taken in Controller is  {} seconds",((endTime-startTime)/1000));
		return userBean;
	}
	
	public void logOutAllRetailers() throws LMSException, SQLException{
		loggger.info("I am in Logout All Retailers");
		HttpSession session = null;
		Map<String, HttpSession> currentUserSessionMap=null;
		ServletContext sc = LMSUtility.sc;
		Statement st=null;
		ResultSet rs=null;
		Connection con = DBConnect.getConnection();	
		try{
			if(sc.getAttribute("LOGGED_IN_USERS")==null)
			{	
				return;
			}
			currentUserSessionMap = (Map<String, HttpSession>) sc.getAttribute("LOGGED_IN_USERS");
			loggger.info(" LOGGED_IN_USERS maps is " + currentUserSessionMap);
			if(!currentUserSessionMap.isEmpty())
			{
			st = con.createStatement();
			rs = st.executeQuery("select user_name from st_lms_user_master where organization_type = 'RETAILER'");
			while(rs.next()){
				String username = rs.getString("user_name").toLowerCase();
				if(currentUserSessionMap.containsKey(username)){
					session = currentUserSessionMap.get(username);
					if (CommonFunctionsHelper.isSessionValid(session)) {
						session.removeAttribute("USER_INFO");
						session.removeAttribute("ACTION_LIST");
						session.removeAttribute("PRIV_MAP");
						session.invalidate();
						session = null;					
						}
				currentUserSessionMap.remove(username);
				}
			}
			sc.setAttribute("LOGGED_IN_USERS",currentUserSessionMap);
			loggger.info(""+sc.getAttribute("LOGGED_IN_USERS"));
			}else{
				loggger.info("NO_LOGGED_IN_USERS");
			}
		}catch (SQLException e){
			throw e;
		}finally{
			DBConnect.closeConnection(st, rs);
			DBConnect.closeCon(con);
		}
	}
	
	public void updateUserLogout(String sessionId){
		try {
				DaoFactory.getUserMgmtDao().updateUserLogout(sessionId);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public TpCommonStatusBean validateRGLimit(String userName, String criteria, String limit) throws Exception {
		Connection connection = null;
		boolean isFraud = false ;
		try {
			
			connection = DBConnect.getConnection();
			int userId = WrapperUtility.getUserIdFromUserName(userName, connection);
			UserInfoBean userBean = TpUtilityHelper.getUserDataFromUserId(userId, connection);
			

			boolean canClaim = true;
			if("RETAILER".equals(userBean.getUserType()) && "IW_PWT".equalsIgnoreCase(criteria)) {
				canClaim = Util.canClaimRetailer(userBean.getUserId(), userBean.getUserOrgId(), userBean.getParentOrgId(), connection);
				
				if(!canClaim)
					return new TpCommonStatusBean(false, "This ticket can't be claimed at Retailer.") ;
			}
			
			isFraud = ResponsibleGaming.respGaming(userBean, criteria, limit, connection);
			
		} catch (Exception e) {
			new TpCommonStatusBean(false, "LIMIT REACHED") ;
			throw e;
		} finally {
			DBConnect.closeCon(connection);
		}

		return new TpCommonStatusBean(!isFraud, isFraud ? "LIMIT REACHED" : "SUCCESS") ; 
	}
}