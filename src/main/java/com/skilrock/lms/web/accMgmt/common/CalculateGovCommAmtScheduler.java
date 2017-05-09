package com.skilrock.lms.web.accMgmt.common;

import java.sql.Connection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import com.skilrock.lms.beans.GovCommBean;
import com.skilrock.lms.beans.ServicesBean;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.coreEngine.accMgmt.common.CalculateGovCommAmtHelper;

public class CalculateGovCommAmtScheduler {
	private static final long serialVersionUID = 1L;

	public void calculateGovernmentCommission() {
		Connection connection = null;
		Map<Integer, GovCommBean> commissionMap = null;
		try {
			String deployDate = Utility.getPropertyValue("DEPLOYMENT_DATE");
			Timestamp startTime = new Timestamp(new SimpleDateFormat(Utility.getPropertyValue("date_format")).parse(deployDate).getTime());

			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			String endTimeString = new SimpleDateFormat(Utility.getPropertyValue("date_format")).format(calendar.getTime());
			SimpleDateFormat dateFormat = new SimpleDateFormat(Utility.getPropertyValue("date_format")+" HH:mm:ss");
			Timestamp endTime = new Timestamp(dateFormat.parse(endTimeString+" 23:59:59").getTime());
			//Timestamp endTime = new Timestamp((Util.getCurrentTimeStamp().getTime()) - 24 * 60 * 60 * 1000-100);

			connection = DBConnect.getConnection();
			connection.setAutoCommit(false);

			CalculateGovCommAmtHelper helper = new CalculateGovCommAmtHelper();
			if(ServicesBean.isDG()) {
				commissionMap = helper.getGovCommAmtDetailToApproveForDG(startTime, endTime, "DG", "ALL", connection);
				if (commissionMap != null && commissionMap.size() > 0) {
					helper.insertGovCommDetails(commissionMap, "DG", connection);
				}
			}
			if(ServicesBean.isSLE()) {
				commissionMap = helper.getGovCommAmtDetailToApproveForSLE(startTime, endTime, "SLE", "ALL", connection);
				if (commissionMap != null && commissionMap.size() > 0) {
					helper.insertGovCommDetails(commissionMap, "SLE", connection);
				}
			}

			connection.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(connection);
		}
	}
}