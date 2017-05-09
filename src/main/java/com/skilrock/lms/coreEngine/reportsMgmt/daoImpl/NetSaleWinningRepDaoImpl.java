package com.skilrock.lms.coreEngine.reportsMgmt.daoImpl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.Map;

import com.skilrock.lms.beans.NetSaleWinningReportDataBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.web.reportsMgmt.common.ReportUtility;

public class NetSaleWinningRepDaoImpl {

	private static NetSaleWinningRepDaoImpl instance = new NetSaleWinningRepDaoImpl();

	private NetSaleWinningRepDaoImpl() {
	}

	public static NetSaleWinningRepDaoImpl getInstance() {
		if (instance == null)
			instance = new NetSaleWinningRepDaoImpl();
		return instance;
	}

	public Map<String, NetSaleWinningReportDataBean> fetchNetSaleWinDataAllAgent(
			Timestamp stDate, Timestamp endDate, Connection con)
			throws LMSException {
		NetSaleWinningReportDataBean dataBean = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		PreparedStatement gamePstmt = null;
		Map<String, NetSaleWinningReportDataBean> agtMap = new LinkedHashMap<String, NetSaleWinningReportDataBean>();
		try {
			String gameQry = ReportUtility.getDrawGameMapQuery(stDate);
			gamePstmt = con.prepareStatement(gameQry);
			ResultSet rsGame = gamePstmt.executeQuery();
			while (rsGame.next()) {
				int gameNo = rsGame.getInt("game_id");
				cstmt = con
						.prepareCall("call getNetSaleWinningAgentWise(?,?,?)");
				cstmt.setTimestamp(1, stDate);
				cstmt.setTimestamp(2, endDate);
				cstmt.setInt(3, gameNo);
				rs = cstmt.executeQuery();
				String agtOrgId = null;
				while (rs.next()) {
					agtOrgId = rs.getString("orgCode_Name");
					if (!agtMap.containsKey(agtOrgId)) {
						dataBean = new NetSaleWinningReportDataBean();
						agtMap.put(agtOrgId, dataBean);
					}

					agtMap.get(agtOrgId).setDgMrpSale(
							agtMap.get(agtOrgId).getDgMrpSale()
									+ rs.getDouble("mrpSale"));
					agtMap.get(agtOrgId).setDgNetSale(
							agtMap.get(agtOrgId).getDgNetSale()
									+ rs.getDouble("netSale") - rs.getDouble("pwt"));
					agtMap.get(agtOrgId).setDgCommAmt(
							agtMap.get(agtOrgId).getDgCommAmt()
									+ rs.getDouble("retSaleComm"));
					agtMap.get(agtOrgId).setDgPwt(
							agtMap.get(agtOrgId).getDgPwt()
									+ rs.getDouble("pwt"));
					agtMap.get(agtOrgId).setDgSDAmt(
							agtMap.get(agtOrgId).getDgSDAmt()
									+ rs.getDouble("saleSdAmt"));
					agtMap.get(agtOrgId).setDgLeviAmt(
							agtMap.get(agtOrgId).getDgLeviAmt()
									+ rs.getDouble("saleVatAmt"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,
					LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,
					LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closePstmt(gamePstmt);
			DBConnect.closeCstmt(cstmt);
		}
		return agtMap;
	}

	public Map<String, NetSaleWinningReportDataBean> fetchNetSaleWinDataAllRetOfSingleAgent(
			int agtId, Timestamp stDate, Timestamp endDate, Connection con)
			throws LMSException {
		NetSaleWinningReportDataBean dataBean = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		PreparedStatement gamePstmt = null;
		Map<String, NetSaleWinningReportDataBean> agtMap = new LinkedHashMap<String, NetSaleWinningReportDataBean>();
		try {
			String gameQry = ReportUtility.getDrawGameMapQuery(stDate);
			gamePstmt = con.prepareStatement(gameQry);
			ResultSet rsGame = gamePstmt.executeQuery();
			while (rsGame.next()) {
				int gameNo = rsGame.getInt("game_id");
				cstmt = con
						.prepareCall("call getNetSaleWinDataAllRetAgentWise(?,?,?,?)");
				cstmt.setTimestamp(1, stDate);
				cstmt.setTimestamp(2, endDate);
				cstmt.setInt(3, gameNo);
				cstmt.setInt(4, agtId);
				rs = cstmt.executeQuery();
				String agtOrgId = null;
				while (rs.next()) {
					agtOrgId = rs.getString("retCode_Name");
					if (!agtMap.containsKey(agtOrgId)) {
						dataBean = new NetSaleWinningReportDataBean();
						agtMap.put(agtOrgId, dataBean);
					}

					agtMap.get(agtOrgId).setDgMrpSale(
							agtMap.get(agtOrgId).getDgMrpSale()
									+ rs.getDouble("mrpSale"));
					agtMap.get(agtOrgId).setDgNetSale(
							agtMap.get(agtOrgId).getDgNetSale()
									+ rs.getDouble("netSale") - rs.getDouble("pwt"));
					agtMap.get(agtOrgId).setDgCommAmt(
							agtMap.get(agtOrgId).getDgCommAmt()
									+ rs.getDouble("retSaleComm"));
					agtMap.get(agtOrgId).setDgPwt(
							agtMap.get(agtOrgId).getDgPwt()
									+ rs.getDouble("pwt"));
					agtMap.get(agtOrgId).setDgSDAmt(
							agtMap.get(agtOrgId).getDgSDAmt()
									+ rs.getDouble("saleSdAmt"));
					agtMap.get(agtOrgId).setDgLeviAmt(
							agtMap.get(agtOrgId).getDgLeviAmt()
									+ rs.getDouble("saleVatAmt"));
					agtMap.get(agtOrgId).setOrgName(rs.getString("agtCode_Name"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,
					LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,
					LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closePstmt(gamePstmt);
			DBConnect.closeCstmt(cstmt);
		}
		return agtMap;
	}

	public Map<String, NetSaleWinningReportDataBean> fetchNetSaleWinDataAllRetOfAllAgent(
			Timestamp stDate, Timestamp endDate, Connection con)
			throws LMSException {
		NetSaleWinningReportDataBean dataBean = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		PreparedStatement gamePstmt = null;
		Map<String, NetSaleWinningReportDataBean> agtMap = new LinkedHashMap<String, NetSaleWinningReportDataBean>();
		try {
			String gameQry = ReportUtility.getDrawGameMapQuery(stDate);
			gamePstmt = con.prepareStatement(gameQry);
			ResultSet rsGame = gamePstmt.executeQuery();
			while (rsGame.next()) {
				int gameNo = rsGame.getInt("game_id");
				cstmt = con
						.prepareCall("call getNetSaleWinDataAllRetAgentWise(?,?,?,?)");
				cstmt.setTimestamp(1, stDate);
				cstmt.setTimestamp(2, endDate);
				cstmt.setInt(3, gameNo);
				cstmt.setInt(4, 0);
				rs = cstmt.executeQuery();
				String agtOrgId = null;
				while (rs.next()) {
					agtOrgId = rs.getString("retCode_Name");
					if (!agtMap.containsKey(agtOrgId)) {
						dataBean = new NetSaleWinningReportDataBean();
						agtMap.put(agtOrgId, dataBean);
					}
					agtMap.get(agtOrgId).setOrgName(
							rs.getString("agtCode_Name"));
					agtMap.get(agtOrgId).setDgMrpSale(
							agtMap.get(agtOrgId).getDgMrpSale()
									+ rs.getDouble("mrpSale"));
					agtMap.get(agtOrgId).setDgNetSale(
							agtMap.get(agtOrgId).getDgNetSale()
									+ rs.getDouble("netSale") - rs.getDouble("pwt"));
					agtMap.get(agtOrgId).setDgCommAmt(
							agtMap.get(agtOrgId).getDgCommAmt()
									+ rs.getDouble("retSaleComm"));
					agtMap.get(agtOrgId).setDgPwt(
							agtMap.get(agtOrgId).getDgPwt()
									+ rs.getDouble("pwt"));
					agtMap.get(agtOrgId).setDgSDAmt(
							agtMap.get(agtOrgId).getDgSDAmt()
									+ rs.getDouble("saleSdAmt"));
					agtMap.get(agtOrgId).setDgLeviAmt(
							agtMap.get(agtOrgId).getDgLeviAmt()
									+ rs.getDouble("saleVatAmt"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,
					LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,
					LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closePstmt(gamePstmt);
			DBConnect.closeCstmt(cstmt);
		}
		return agtMap;
	}

}
