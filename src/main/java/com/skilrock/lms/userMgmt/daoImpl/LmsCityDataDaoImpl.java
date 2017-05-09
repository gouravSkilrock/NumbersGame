package com.skilrock.lms.userMgmt.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.userMgmt.controllerImpl.LmsCityDataControllerImpl;
import com.skilrock.lms.userMgmt.javaBeans.LmsCityDataBean;

public class LmsCityDataDaoImpl {
	final static Log logger = LogFactory.getLog(LmsCityDataDaoImpl.class);

	private static LmsCityDataDaoImpl singleInstance;
	private LmsCityDataDaoImpl(){}
	 public static LmsCityDataDaoImpl getSingleInstance() {
		    if (singleInstance == null) {
		      synchronized (LmsCityDataDaoImpl.class) {
		        if (singleInstance == null) {
		          singleInstance = new LmsCityDataDaoImpl();
		        }
		      }
		    }
		    return singleInstance;
		  }
	 
	public List<LmsCityDataBean> fetchLmsCityData(String stateCode,
			Connection con) throws SQLException {
		logger
				.info("***** Inside fetchLmsCityData method of LmsCityDataDaoImpl class");

		List<LmsCityDataBean> lmsCityList = null;
		LmsCityDataBean lmsCityDataBean = null;

		PreparedStatement pStatement = null;
		ResultSet rs = null;
		String query = "select city_name, city_code from st_lms_city_master where state_code = '"
				+ stateCode + "' and status='ACTIVE'";

		
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
		
		return lmsCityList;
	}
}
