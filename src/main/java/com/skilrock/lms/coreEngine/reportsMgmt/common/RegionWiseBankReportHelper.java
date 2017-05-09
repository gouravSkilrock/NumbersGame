package com.skilrock.lms.coreEngine.reportsMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.RegionWiseBankDetailBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;

public class RegionWiseBankReportHelper {
	private static final Log logger = LogFactory
			.getLog(RegionWiseBankReportHelper.class);

	private String getFormattedTimeStamp(String inDate) throws ParseException {
		try {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
							.parse(inDate));
		} catch (Exception e) {
			e.printStackTrace();
			return inDate;
		}
	}

	public String fetchOrgAddress(int orgId) throws LMSException {
		String orgAddress = null;

		Connection con = null;
		PreparedStatement pStmt = null;
		ResultSet rs = null;

		logger.info("***** Inside fetchOrgAddress Function");
		try {
			con = DBConnect.getConnection();
			pStmt = con
					.prepareStatement("select addr_line1, addr_line2, city from st_lms_organization_master where organization_id = ?");
			pStmt.setInt(1, orgId);
			rs = pStmt.executeQuery();
			logger.info("Fetching Org Address Query " + pStmt);
			while (rs.next()) {
				orgAddress = rs.getString("addr_line1") + ", "
						+ rs.getString("addr_line2") + ", "
						+ rs.getString("city");
			}
			logger.info("Ord Address is " + orgAddress);
		} catch (SQLException e) {
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,
					LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,
					LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeCon(con);
			DBConnect.closePstmt(pStmt);
			DBConnect.closeRs(rs);
		}
		return orgAddress;
	}

	public List<RegionWiseBankDetailBean> fetchRegionWiseBankReport(
			Timestamp start_date, Timestamp end_date) throws LMSException {
		List<RegionWiseBankDetailBean> regionWiseBankDetailBeanList;
		RegionWiseBankDetailBean regionWiseBankDetailBean = null;

		Connection con = null;
		PreparedStatement pStmt = null;
		ResultSet rs = null;

		logger.info("***** Inside fetchRegionWiseBankReport Function");

		String query = "SELECT tp_trans_id, transaction_date, bank_name, branch_name, cashier_name, region_name, amount, trans_type, retailer_org_id, ret_org_code, ret_org_name, agent_org_code FROM(SELECT map.tp_trans_id, map.transaction_date, map.bank_name, map.branch_name, map.cashier_name, map.region_name, debit.amount, 'DEBIT' trans_type, debit.retailer_org_id, rom.org_code ret_org_code, rom.name ret_org_name, aom.org_code agent_org_code FROM st_lms_tp_txn_details map INNER JOIN st_lms_agent_debit_note debit INNER JOIN st_lms_organization_master rom INNER JOIN st_lms_organization_master aom ON map.retailer_trans_id = debit.transaction_id AND rom.organization_id = debit.retailer_org_id AND aom.organization_id = rom.parent_id WHERE map.transaction_date >= ? AND map.transaction_date <= ? "
				+ "UNION SELECT map.tp_trans_id, map.transaction_date, map.bank_name, map.branch_name, map.cashier_name, map.region_name, cash.amount,  'CREDIT' trans_type, cash.retailer_org_id, rom.org_code ret_org_code, rom.name ret_org_name, aom.org_code agent_org_code FROM st_lms_tp_txn_details map INNER JOIN st_lms_agent_cash_transaction cash INNER JOIN st_lms_organization_master rom INNER JOIN st_lms_organization_master aom ON map.retailer_trans_id = cash.transaction_id AND rom.organization_id = cash.retailer_org_id AND aom.organization_id = rom.parent_id WHERE map.transaction_date >= ? AND map.transaction_date <= ? ) main ORDER BY transaction_date DESC";
		regionWiseBankDetailBeanList = new ArrayList<RegionWiseBankDetailBean>();
		try {
			con = DBConnect.getConnection();

			pStmt = con.prepareStatement(query);
			pStmt.setTimestamp(1, start_date);
			pStmt.setTimestamp(2, end_date);
			pStmt.setTimestamp(3, start_date);
			pStmt.setTimestamp(4, end_date);
			logger.info("Query Fetching Data is " + pStmt);

			rs = pStmt.executeQuery();
			while (rs.next()) {
				regionWiseBankDetailBean = new RegionWiseBankDetailBean();
				regionWiseBankDetailBean.setRegion(rs.getString("region_name"));
				regionWiseBankDetailBean.setBankName(rs.getString("bank_name"));
				regionWiseBankDetailBean.setBranchName(rs
						.getString("branch_name"));
				regionWiseBankDetailBean.setTxnRef(rs.getString("tp_trans_id"));

				regionWiseBankDetailBean.setTxnDate(getFormattedTimeStamp(rs
						.getString("transaction_date")));

				regionWiseBankDetailBean.setCashierName(rs
						.getString("cashier_name"));
				regionWiseBankDetailBean.setCustomerName(rs
						.getString("ret_org_name"));
				regionWiseBankDetailBean.setTerminalId(rs
						.getString("ret_org_code"));
				regionWiseBankDetailBean.setLmcId(rs
						.getString("agent_org_code"));
				regionWiseBankDetailBean.setTransactionType(rs
						.getString("trans_type"));
				regionWiseBankDetailBean.setAmount(rs.getString("amount"));

				regionWiseBankDetailBeanList.add(regionWiseBankDetailBean);
			}
		} catch (SQLException e) {
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,
					LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,
					LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeCon(con);
			DBConnect.closePstmt(pStmt);
			DBConnect.closeRs(rs);
		}

		return regionWiseBankDetailBeanList;
	}
}
