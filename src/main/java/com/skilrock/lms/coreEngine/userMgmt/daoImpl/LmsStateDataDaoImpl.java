package com.skilrock.lms.coreEngine.userMgmt.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.userMgmt.javaBeans.LmsStateDataBean;

public class LmsStateDataDaoImpl {
	final static Log logger = LogFactory.getLog(LmsUserDataDaoImpl.class);

	public List<LmsStateDataBean> fetchLmsStateData(Connection con)
			throws LMSException {
		logger
				.info("***** Inside fetchLmsStateData method of LmsStateDataDaoImpl class");

		List<LmsStateDataBean> lmsStateList = null;
		LmsStateDataBean lmsStateDataBean = null;

		PreparedStatement pStatement = null;
		ResultSet rs = null;
		String query = "select state_code, name from st_lms_state_master";

		try {
			pStatement = con.prepareStatement(query);
			logger.info("fetchLmsStateData Query is " + query);
			rs = pStatement.executeQuery();

			lmsStateList = new ArrayList<LmsStateDataBean>();

			while (rs.next()) {
				lmsStateDataBean = new LmsStateDataBean();

				lmsStateDataBean.setStateCode(rs.getString("state_code"));
				lmsStateDataBean.setStateName(rs.getString("name"));

				lmsStateList.add(lmsStateDataBean);
			}
			logger.debug("***** Fetched State List is "
					+ lmsStateList.toString());
		} catch (SQLException e) {
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,
					LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,
					LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return lmsStateList;
	}
}
