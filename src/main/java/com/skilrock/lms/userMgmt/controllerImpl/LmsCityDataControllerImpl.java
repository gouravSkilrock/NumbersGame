package com.skilrock.lms.userMgmt.controllerImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.GenericException;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.userMgmt.daoImpl.LmsCityDataDaoImpl;
import com.skilrock.lms.userMgmt.javaBeans.LmsCityDataBean;

public class LmsCityDataControllerImpl {
	final static Log logger = LogFactory.getLog(LmsUserDataControllerImpl.class);
	
	private static LmsCityDataControllerImpl singleInstance;
	private LmsCityDataControllerImpl(){}
	 public static LmsCityDataControllerImpl getSingleInstance() {
		    if (singleInstance == null) {
		      synchronized (LmsCityDataControllerImpl.class) {
		        if (singleInstance == null) {
		          singleInstance = new LmsCityDataControllerImpl();
		        }
		      }
		    }
		    return singleInstance;
		  }
	public List<LmsCityDataBean> getLmsCityData(String stateCode)
			throws GenericException {
		logger
				.info("***** Inside getLmsCityData method of LmsCityDataServiceImpl class");

		List<LmsCityDataBean> cityList;
		Connection con = null;

		try {
			con = DBConnect.getConnection();
			

			cityList = LmsCityDataDaoImpl.getSingleInstance().fetchLmsCityData(stateCode, con);
		} catch (SQLException e) {
			throw new GenericException(LMSErrors.SQL_EXCEPTION_CODE, e);
		}catch (Exception e) {
			throw new GenericException(LMSErrors.GENERAL_EXCEPTION_CODE, e);
		} finally {
			DBConnect.closeCon(con);
		}
		return cityList;
	}
}
