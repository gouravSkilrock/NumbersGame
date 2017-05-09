package com.skilrock.lms.userMgmt.controllerImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.GenericException;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.userMgmt.daoImpl.LmsStateDataDaoImpl;
import com.skilrock.lms.userMgmt.javaBeans.LmsStateDataBean;

public class LmsStateDateControllerImpl {
	final static Log logger = LogFactory.getLog(LmsUserDataControllerImpl.class);
	private static LmsStateDateControllerImpl singleInstance;
	private LmsStateDateControllerImpl(){}
	 public static LmsStateDateControllerImpl getSingleInstance() {
		    if (singleInstance == null) {
		      synchronized (LmsStateDateControllerImpl.class) {
		        if (singleInstance == null) {
		          singleInstance = new LmsStateDateControllerImpl();
		        }
		      }
		    }
		    return singleInstance;
		  }
	
	
	
	public List<LmsStateDataBean> getLmsStateData() throws GenericException {
		logger
				.info("***** Inside getUserInfoData method of LmsStateDateServiceImpl class");

		List<LmsStateDataBean> stateList=null;
		
		Connection con = null;

		try {
			con = DBConnect.getConnection();
			stateList = LmsStateDataDaoImpl.getSingleInstance().fetchLmsStateData(con);
		}catch (SQLException e) {
			throw new GenericException(LMSErrors.SQL_EXCEPTION_CODE, e);
		}catch (Exception e) {
			throw new GenericException(LMSErrors.GENERAL_EXCEPTION_CODE, e);
		}  finally {
			DBConnect.closeCon(con);
		}
		return stateList;
	}
}
