package com.skilrock.lms.coreEngine.reportsMgmt.common;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.IncentiveReportDataBean;
import com.skilrock.lms.beans.IncentiveReportDataBean.IncentiveReportDataBeanBuilder;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;

public class IncentiveReportHelper {

	private static Log logger = LogFactory.getLog(IncentiveReportHelper.class);

	private static IncentiveReportHelper incentiveReportHelper = null;

	private IncentiveReportHelper() {
	}

	public static IncentiveReportHelper getInstance() {
		if (incentiveReportHelper == null)
			incentiveReportHelper = new IncentiveReportHelper();

		return incentiveReportHelper;
	}

	public IncentiveReportDataBean fetchIncentiveData(String startDate,
			String endDate, String userName, String reportType)
			throws LMSException {
		IncentiveReportDataBean bean = null;
		Connection conn = null;
		Statement statement = null;
		ResultSet rs = null;
		try {
			conn = DBConnect.getConnection();
			statement = conn.createStatement();
			String query = "select user_name, sale_amt, winning_amt, incentive_amt from st_iw_retailer_"
					+ reportType.toLowerCase()
					+ "_incentive_data where start_date = '"
					+ startDate
					+ "' and end_date='"
					+ endDate
					+ "' and user_name = '"
					+ userName + "';";
			logger.info("Query : " + query) ;
			rs = statement.executeQuery(query);
			if (rs.next()) {
				double saleAmt = rs.getDouble("sale_amt");
				double winAmt = rs.getDouble("winning_amt");
				double incAmt = rs.getDouble("incentive_amt");
				bean = new IncentiveReportDataBeanBuilder()
						.setSaleAmt(rs.getDouble("sale_amt"))
						.setWinningAmt(rs.getDouble("winning_amt"))
						.setNonWinAmt(saleAmt - winAmt).setIncAmt(incAmt)
						.build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,
					LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeResource(conn, statement, rs);
		}
		return bean;
	}

}
