package com.skilrock.lms.coreEngine.reportsMgmt.controllerImpl;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Map;

import com.skilrock.lms.beans.NetSaleWinningReportDataBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.reportsMgmt.daoImpl.NetSaleWinningRepDaoImpl;

public class NetSaleWinningRepControllerImpl {

	private static NetSaleWinningRepControllerImpl instance = new NetSaleWinningRepControllerImpl();

	public NetSaleWinningRepControllerImpl() {
	}

	public static NetSaleWinningRepControllerImpl getInstance() {
		if (instance == null)
			instance = new NetSaleWinningRepControllerImpl();
		return instance;
	}

	public Map<String, NetSaleWinningReportDataBean> fetchNetSaleWinData(
			int agtId, String repType, Timestamp stDate, Timestamp endDate,
			String header) throws LMSException {
		Map<String, NetSaleWinningReportDataBean> dataMap = null;
		Connection con = null;
		try {
			con = DBConnect.getConnection();
			if ("AGENT".equalsIgnoreCase(repType) && agtId == -1) {
				dataMap = NetSaleWinningRepDaoImpl.getInstance()
						.fetchNetSaleWinDataAllAgent(stDate, endDate, con);
			} else if ("RETAILER".equalsIgnoreCase(repType) && agtId == -1) {
				dataMap = NetSaleWinningRepDaoImpl.getInstance()
						.fetchNetSaleWinDataAllRetOfAllAgent(stDate, endDate,
								con);
			} else if ("RETAILER".equalsIgnoreCase(repType) && agtId != -1) {
				dataMap = NetSaleWinningRepDaoImpl.getInstance()
						.fetchNetSaleWinDataAllRetOfSingleAgent(agtId, stDate,
								endDate, con);
			}

		} catch (LMSException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,
					LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeCon(con);
		}
		return dataMap;
	}

}
