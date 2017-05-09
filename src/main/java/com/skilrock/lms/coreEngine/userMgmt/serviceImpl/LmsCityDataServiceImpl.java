package com.skilrock.lms.coreEngine.userMgmt.serviceImpl;

import java.sql.Connection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.userMgmt.daoImpl.LmsCityDataDaoImpl;
import com.skilrock.lms.coreEngine.userMgmt.javaBeans.LmsCityDataBean;

public class LmsCityDataServiceImpl {
	final static Log logger = LogFactory.getLog(LmsUserDataServiceImpl.class);

	public List<LmsCityDataBean> getLmsCityData(String stateCode)
			throws LMSException {
		logger
				.info("***** Inside getLmsCityData method of LmsCityDataServiceImpl class");

		List<LmsCityDataBean> cityList;
		LmsCityDataDaoImpl lmsCityDataDaoImpl = null;
		Connection con = null;

		try {
			con = DBConnect.getConnection();
			lmsCityDataDaoImpl = new LmsCityDataDaoImpl();

			cityList = lmsCityDataDaoImpl.fetchLmsCityData(stateCode, con);
		} catch (LMSException e) {
			throw e;
		} catch (Exception e) {
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,
					LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeCon(con);
		}
		return cityList;
	}
}
