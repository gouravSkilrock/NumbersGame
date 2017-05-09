package com.skilrock.lms.coreEngine.reportsMgmt.controllerImpl;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.reportsMgmt.daoImpl.IntakeReportDaoImpl;
import com.skilrock.lms.coreEngine.reportsMgmt.javaBeans.IntakeReportDataBean;

public class IntakeReportControllerImpl {
	final static Log logger = LogFactory.getLog(IntakeReportControllerImpl.class);

	private static IntakeReportControllerImpl instance;

	private IntakeReportControllerImpl() {
	}

	public static IntakeReportControllerImpl getInstance() {
		if (instance == null) {
			synchronized (IntakeReportControllerImpl.class) {
				if (instance == null) {
					instance = new IntakeReportControllerImpl();
				}
			}
		}
		return instance;
	}

	public List<IntakeReportDataBean> fetchReportData(String service,Timestamp startTime, Timestamp endTime) throws LMSException {
		Connection connection = null;
		List<IntakeReportDataBean> reportList = null;
		try {
			connection = DBConnect.getConnection();
			reportList = IntakeReportDaoImpl.getInstance().fetchReportData(service,startTime, endTime, connection);
		} catch (LMSException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeCon(connection);
		}

		return reportList;
	}
}