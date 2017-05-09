package com.skilrock.lms.userMgmt.controllerImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.GenericException;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.userMgmt.daoImpl.LmsUserDataDaoImpl;
import com.skilrock.lms.userMgmt.javaBeans.LmsUserDataBean;

public class LmsUserDataControllerImpl {
	final static Log logger = LogFactory.getLog(LmsUserDataControllerImpl.class);
	private static LmsUserDataControllerImpl singleInstance;
	private LmsUserDataControllerImpl(){}
	 public static LmsUserDataControllerImpl getSingleInstance() {
		    if (singleInstance == null) {
		      synchronized (LmsUserDataControllerImpl.class) {
		        if (singleInstance == null) {
		          singleInstance = new LmsUserDataControllerImpl();
		        }
		      }
		    }
		    return singleInstance;
		  }
	public List<LmsUserDataBean> getUserInfoData(String cityCode,
			String userType) throws GenericException {
		logger
				.info("***** Inside getUserInfoData method of LmsUserDataServiceImpl class");

		List<LmsUserDataBean> userList = null;
		LmsUserDataDaoImpl lmsUserDataDaoImpl = null;

		Connection con = null;

		try {
			con = DBConnect.getConnection();
			lmsUserDataDaoImpl =LmsUserDataDaoImpl.getSingleInstance();

			userList = lmsUserDataDaoImpl.fetchLmsUserDetails(userType,
					cityCode, con);
			logger.info("User List From LMS is " + userList);
		} catch (SQLException e) {
			throw new GenericException(LMSErrors.SQL_EXCEPTION_CODE, e);
		}catch (Exception e) {
			throw new GenericException(LMSErrors.GENERAL_EXCEPTION_CODE, e);
		} finally {
			DBConnect.closeCon(con);
		}
		return userList;
	}
	public List<LmsUserDataBean> getNearByUserInfoData(String lat,String lng) throws GenericException {
		logger
				.info("***** Inside getUserInfoData method of LmsUserDataServiceImpl class");

		List<LmsUserDataBean> userList = null;
		LmsUserDataDaoImpl lmsUserDataDaoImpl = null;

		Connection con = null;

		try {
			con = DBConnect.getConnection();
			lmsUserDataDaoImpl =LmsUserDataDaoImpl.getSingleInstance();

			userList = lmsUserDataDaoImpl.fetchNearByLmsUserDetails(lat,
					lng,con);
			logger.info("User List From LMS is " + userList);
		} catch (SQLException e) {
			throw new GenericException(LMSErrors.SQL_EXCEPTION_CODE, e);
		}catch (Exception e) {
			throw new GenericException(LMSErrors.GENERAL_EXCEPTION_CODE, e);
		} finally {
			DBConnect.closeCon(con);
		}
		return userList;
	}
	/**
	 * This Method Fetch Org details on the basis of CityCode and StateCode
	 * @param stateCode
	 * @param cityCode
	 * @return
	 * @throws GenericException
	 */
	public List<LmsUserDataBean> getUserData(String stateCode,String cityCode) throws GenericException {
		List<LmsUserDataBean> userList = null;
    	LmsUserDataDaoImpl lmsUserDataDaoImpl = null;
    	Connection con = null;
     try {
			con = DBConnect.getConnection();
			lmsUserDataDaoImpl =LmsUserDataDaoImpl.getSingleInstance();
			userList = lmsUserDataDaoImpl.fetchUserDetails(stateCode,cityCode, con);
			logger.debug("User List From LMS is " + userList);
		} catch (SQLException e) {
			logger.error("SQLException",e);
			throw new GenericException(LMSErrors.SQL_EXCEPTION_CODE, e);
		}catch (Exception e) {
			logger.error("Exception",e);
			throw new GenericException(LMSErrors.GENERAL_EXCEPTION_CODE, e);
		} finally {
			DBConnect.closeCon(con);
		}
		return userList;
	}
	
}
