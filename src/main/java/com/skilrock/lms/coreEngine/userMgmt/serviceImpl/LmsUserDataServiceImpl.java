package com.skilrock.lms.coreEngine.userMgmt.serviceImpl;

import java.sql.Connection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.userMgmt.daoImpl.LmsUserDataDaoImpl;
import com.skilrock.lms.coreEngine.userMgmt.javaBeans.LmsUserDataBean;

public class LmsUserDataServiceImpl {
	final static Log logger = LogFactory.getLog(LmsUserDataServiceImpl.class);

	public List<LmsUserDataBean> getUserInfoData(String cityCode,
			String userType) throws LMSException {
		logger
				.info("***** Inside getUserInfoData method of LmsUserDataServiceImpl class");

		List<LmsUserDataBean> userList = null;
		LmsUserDataDaoImpl lmsUserDataDaoImpl = null;

		Connection con = null;

		try {
			con = DBConnect.getConnection();
			lmsUserDataDaoImpl = new LmsUserDataDaoImpl();

			userList = lmsUserDataDaoImpl.fetchLmsUserDetails(userType,
					cityCode, con);
			logger.info("User List From LMS is " + userList);
		} catch (LMSException e) {
			throw e;
		} catch (Exception e) {
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,
					LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeCon(con);
		}
		return userList;
	}
}
