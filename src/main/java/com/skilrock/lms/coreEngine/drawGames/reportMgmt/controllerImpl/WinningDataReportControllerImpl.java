package com.skilrock.lms.coreEngine.drawGames.reportMgmt.controllerImpl;

import java.sql.Connection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.skilrock.lms.beans.ReportStatusBean;
import com.skilrock.lms.beans.ServiceRequest;
import com.skilrock.lms.beans.ServiceResponse;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.DBConnectReplica;
import com.skilrock.lms.coreEngine.drawGames.drawMgmt.javaBeans.WinningDataReportBean;
import com.skilrock.lms.coreEngine.service.IServiceDelegate;
import com.skilrock.lms.coreEngine.service.ServiceDelegate;
import com.skilrock.lms.coreEngine.service.dge.ServiceMethodName;
import com.skilrock.lms.coreEngine.service.dge.ServiceName;
import com.skilrock.lms.web.reportsMgmt.common.ReportUtility;

public class WinningDataReportControllerImpl {
	private static final Logger logger = LoggerFactory.getLogger("WinningDataReportControllerImpl");

	private static WinningDataReportControllerImpl singleInstance;

	private WinningDataReportControllerImpl(){}

	public static WinningDataReportControllerImpl getSingleInstance() {
		if (singleInstance == null) {
			synchronized (WinningDataReportControllerImpl.class) {
				if (singleInstance == null) {
					singleInstance = new WinningDataReportControllerImpl();
				}
			}
		}

		return singleInstance;
	}

	public String getArchiveDate(ReportStatusBean reportStatusBean) {
		Connection connection = null;
		String archDate = null;
		try {
			if("NO".equals(Utility.getPropertyValue("IS_DATA_FROM_REPLICA")) || "MAIN_DB".equals(reportStatusBean.getReportingFrom()))
				connection = DBConnect.getConnection();
			else
				connection = DBConnectReplica.getConnection();

			archDate = ReportUtility.getLastArchDate(connection);
			logger.info("Archive Date - "+archDate);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBConnect.closeCon(connection);
		}

		return archDate;
	}

	@SuppressWarnings("unchecked")
	public List<WinningDataReportBean> getWinningData(int gameId, String drawDate) {
		WinningDataReportBean reportBean = null;
		ServiceRequest sReq = null;
		ServiceResponse sRes = null;
		IServiceDelegate delegate = null;
		List<WinningDataReportBean> winningDataList = null;
		try {
			reportBean = new WinningDataReportBean();
			reportBean.setGameId(gameId);
			reportBean.setDrawDateTime(drawDate);
			sRes = new ServiceResponse();
			sReq = new ServiceRequest();
			sReq.setServiceName(ServiceName.REPORTS_MGMT);
			sReq.setServiceMethod(ServiceMethodName.WINNING_DATA_REPORT);
			sReq.setServiceData(reportBean);
			delegate = ServiceDelegate.getInstance();
			sRes = delegate.getResponse(sReq);
			if(sRes.getIsSuccess()) {
				winningDataList = (List<WinningDataReportBean>) new Gson().fromJson((JsonElement)sRes.getResponseData(), new TypeToken<List<WinningDataReportBean>>(){}.getType());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return winningDataList;
	}
}