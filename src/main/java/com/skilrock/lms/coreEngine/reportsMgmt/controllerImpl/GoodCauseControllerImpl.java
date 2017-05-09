package com.skilrock.lms.coreEngine.reportsMgmt.controllerImpl;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.reportsMgmt.daoImpl.GoodCauseDaoImpl;
import com.skilrock.lms.coreEngine.reportsMgmt.javaBeans.GoodCauseDataBean;

public class GoodCauseControllerImpl {
	final static Log logger = LogFactory.getLog(GoodCauseControllerImpl.class);

	private static GoodCauseControllerImpl instance;

	private GoodCauseControllerImpl() {
	}

	public static GoodCauseControllerImpl getInstance() {
		if (instance == null) {
			synchronized (GoodCauseControllerImpl.class) {
				if (instance == null) {
					instance = new GoodCauseControllerImpl();
				}
			}
		}
		return instance;
	}

	public List<GoodCauseDataBean> fetchGoodCauseData(Timestamp startTime, Timestamp endTime) throws LMSException {
		Connection connection = null;
		List<GoodCauseDataBean> reportList = null;
		try {
			connection = DBConnect.getConnection();
			reportList = GoodCauseDaoImpl.getInstance().fetchGoodCauseData(startTime, endTime, connection);
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