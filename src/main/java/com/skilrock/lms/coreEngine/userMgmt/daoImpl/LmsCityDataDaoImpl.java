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
import com.skilrock.lms.coreEngine.userMgmt.javaBeans.LmsCityDataBean;

public class LmsCityDataDaoImpl {
	final static Log logger = LogFactory.getLog(LmsCityDataDaoImpl.class);

	public List<LmsCityDataBean> fetchLmsCityData(String stateCode,
			Connection con) throws LMSException {
		logger
				.info("***** Inside fetchLmsCityData method of LmsCityDataDaoImpl class");

		List<LmsCityDataBean> lmsCityList = null;
		LmsCityDataBean lmsCityDataBean = null;

		PreparedStatement pStatement = null;
		ResultSet rs = null;
		String query = "select city_name, city_code from st_lms_city_master where state_code = '"
				+ stateCode + "' and status='ACTIVE'";

		try {
			pStatement = con.prepareStatement(query);
			rs = pStatement.executeQuery();

			lmsCityList = new ArrayList<LmsCityDataBean>();

			while (rs.next()) {
				lmsCityDataBean = new LmsCityDataBean();

				lmsCityDataBean.setCityCode(rs.getString("city_code"));
				lmsCityDataBean.setCityName(rs.getString("city_name"));

				lmsCityList.add(lmsCityDataBean);
			}
			logger
					.debug("***** Fetched City List is "
							+ lmsCityList.toString());
		} catch (SQLException e) {
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,
					LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,
					LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return lmsCityList;
	}
}
