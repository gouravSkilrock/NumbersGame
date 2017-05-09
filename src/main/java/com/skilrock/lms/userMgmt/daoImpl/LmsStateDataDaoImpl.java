package com.skilrock.lms.userMgmt.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import com.skilrock.lms.userMgmt.javaBeans.LmsStateDataBean;

public class LmsStateDataDaoImpl {
	final static Log logger = LogFactory.getLog(LmsUserDataDaoImpl.class);

	private static LmsStateDataDaoImpl singleInstance;
	private LmsStateDataDaoImpl(){}
	 public static LmsStateDataDaoImpl getSingleInstance() {
		    if (singleInstance == null) {
		      synchronized (LmsStateDataDaoImpl.class) {
		        if (singleInstance == null) {
		          singleInstance = new LmsStateDataDaoImpl();
		        }
		      }
		    }
		    return singleInstance;
		  }
	
	
	public List<LmsStateDataBean> fetchLmsStateData(Connection con)
			throws  SQLException {
		logger
				.info("***** Inside fetchLmsStateData method of LmsStateDataDaoImpl class");

		List<LmsStateDataBean> lmsStateList = null;
		LmsStateDataBean lmsStateDataBean = null;

		PreparedStatement pStatement = null;
		ResultSet rs = null;
		String query = "select SQL_CACHE state_code, name from st_lms_state_master order by name";

		
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
		
		return lmsStateList;
	}
}
