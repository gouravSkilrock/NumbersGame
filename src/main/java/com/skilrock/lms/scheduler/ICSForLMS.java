package com.skilrock.lms.scheduler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.PropertyLoader;
import com.skilrock.lms.rolemgmt.beans.ICSBean;

public class ICSForLMS {
	private static	Log logger = LogFactory.getLog(ICSForLMS.class);
	public static void main(String[] args) {
	// 	new ICSForLMS().executeICSQueries();
	}

	private boolean checkErrorDirectQuery(ResultSet rs, ICSBean bean)
			throws SQLException {
		boolean isError = false;
		boolean isNext = rs.next();
		String queryResult = "";
		if (!isNext) {
			queryResult = "null";
		} else {
			queryResult = "" + rs.getInt("result");
		}

		if (bean.getDirectResult().equalsIgnoreCase(queryResult)) {
			bean.setRunDirectResult("OK");
		} else {
			isError = true;
			bean
					.setRunDirectResult("Result Return By Query is Not Same as Expected. Return Result is == "
							+ queryResult);
		}
		System.out.println("isError = " + isError
				+ "  bean.getRunDirectResult() = " + bean.getRunDirectResult());
		return isError;
	}

	public List<ICSBean> executeICSQueries() throws LMSException {

		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		System.out.println("sjdghshg");
		List<ICSBean> icsBeanList = readProprtyFile();
		System.out.println("sjdghshg");
		List<ICSBean> icsBeanListForMail = new ArrayList<ICSBean>();
		try {
			boolean isError = false;
			for (ICSBean bean : icsBeanList) {
				try {
					pstmt = con.prepareStatement(bean.getDirectQuery());
					rs = pstmt.executeQuery();
					isError = checkErrorDirectQuery(rs, bean);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			/*
			 * for (ICSBean bean : icsBeanList) { pstmt =
			 * con.prepareStatement(bean.getReverseQuery()); rs =
			 * pstmt.executeQuery(); isError = checkErrorForReverseQuery(rs,
			 * bean); }
			 */
			return icsBeanList;
		}catch (Exception e) {
			logger.error("Exception ",e);
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}


	}

	public List<ICSBean> readProprtyFile() {
		ArrayList ICSBeanList = new ArrayList();
		System.out.println("1111111111");
		Properties prop = PropertyLoader
				.loadProperties("d:/config/ICS.properties");
		System.out.println("222222222");
		Set keys = prop.keySet();
		System.out.println("keys" + keys);
		ICSBean bean = null;

		for (Object propKey : keys) {
			String propKeyStr = (String) propKey;
			String keyValue = null;
			if (propKeyStr.indexOf("key") != -1) {
				bean = new ICSBean();
				keyValue = prop.getProperty(propKeyStr);
				bean.setTitle(prop.getProperty(keyValue + "_title"));
				bean.setDirectQuery(prop.getProperty(keyValue + "_query"));
				bean.setDirectResult(prop.getProperty(keyValue + "_result"));
				bean
						.setDirectErrorMes(prop.getProperty(keyValue
								+ "_errorMes"));
				// bean.setReverseQuery(prop.getProperty(keyValue+"_reverseQuery"));
				// bean.setReverseResult(prop.getProperty(keyValue+"_reverseResult"));
				// bean.setReverseErrorMes(prop.getProperty(keyValue+"_reverseErrorMes"));
				System.out.println(prop.getProperty(keyValue + "_title") + "\n"
						+ prop.getProperty(keyValue + "_query") + "\n"
						+ prop.getProperty(keyValue + "_result") + "\n"
						+ prop.getProperty(keyValue + "_errorMes") + "\n"
						+ prop.getProperty(keyValue + "_reverseQuery") + "\n"
						+ prop.getProperty(keyValue + "_reverseResult") + "\n"
						+ prop.getProperty(keyValue + "_reverseErrorMes"));
				ICSBeanList.add(bean);
			}
		}
		return ICSBeanList;
	}

}
