package com.skilrock.lms.coreEngine.userMgmt.serviceImpl;

import java.sql.Connection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.userMgmt.daoImpl.LmsStateDataDaoImpl;
import com.skilrock.lms.coreEngine.userMgmt.javaBeans.LmsStateDataBean;

public class LmsStateDateServiceImpl {
	final static Log logger = LogFactory.getLog(LmsUserDataServiceImpl.class);

	public List<LmsStateDataBean> getLmsStateData() throws LMSException {
		logger
				.info("***** Inside getUserInfoData method of LmsStateDateServiceImpl class");

		List<LmsStateDataBean> stateList;
		LmsStateDataDaoImpl lmsUserDataDaoImpl = null;
		Connection con = null;

		try {
			con = DBConnect.getConnection();
			lmsUserDataDaoImpl = new LmsStateDataDaoImpl();

			stateList = lmsUserDataDaoImpl.fetchLmsStateData(con);
		} catch (LMSException e) {
			throw e;
		} catch (Exception e) {
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,
					LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeCon(con);
		}
		return stateList;
	}
}
