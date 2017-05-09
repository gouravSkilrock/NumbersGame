package com.skilrock.lms.coreEngine.accMgmt.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.accMgmt.javaBeans.BankDepositBean;
import com.skilrock.lms.web.bankMgmt.beans.BankDetailsBean;
import com.skilrock.lms.web.drawGames.common.Util;

public class BankDepositDaoImpl {
	private static Log logger = LogFactory.getLog(BankDepositDaoImpl.class);
	private static BankDepositDaoImpl instance = null;

	private BankDepositDaoImpl() {
	}

	public static BankDepositDaoImpl getInstance() {
		if (instance == null) {
			instance = new BankDepositDaoImpl();
		}
		return instance;
	}

	public List<BankDetailsBean> getBankDetails(Connection connection) throws LMSException {
		Statement stmt = null;
		ResultSet rs = null;
		List<BankDetailsBean> bankDetailList = new ArrayList<BankDetailsBean>();
		BankDetailsBean detailBean = null;
		try {
			stmt = connection.createStatement();
			String query = "SELECT SQL_CACHE bank_id, bank_disp_name, account_number, description FROM st_lms_bank_deposit_bank_details WHERE status='ACTIVE';";
			logger.info("getBankDetails Query - "+query);
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				detailBean = new BankDetailsBean();
				detailBean.setBankId(rs.getInt("bank_id"));
				detailBean.setBankFullName(rs.getString("bank_disp_name"));
				detailBean.setAccountNo(rs.getString("account_number"));
				detailBean.setDescription(rs.getString("description"));
				bankDetailList.add(detailBean);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeStmt(stmt);
			DBConnect.closeRs(rs);
		}

		return bankDetailList;
	}

	public boolean notifyBankDeposit(BankDepositBean depositBean, Connection connection) throws LMSException {
		boolean status = false;
		SimpleDateFormat dateFormat = null;
		PreparedStatement pstmt = null;
		try {
			dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Timestamp bankDepositTime = new Timestamp(dateFormat.parse(depositBean.getDate()+" 00:00:00").getTime());

			pstmt = connection.prepareStatement("INSERT INTO st_lms_bank_deposit_details (user_id, organization_id, bank_id, branch_name, receipt_no, amount, bank_deposit_date, request_date, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);");
			pstmt.setInt(1, depositBean.getUserId());
			pstmt.setInt(2, depositBean.getOrganizationId());
			pstmt.setInt(3, depositBean.getBankId());
			pstmt.setString(4, depositBean.getBranchName());
			pstmt.setString(5, depositBean.getReceiptNo());
			pstmt.setDouble(6, depositBean.getAmount());
			pstmt.setTimestamp(7, bankDepositTime);
			pstmt.setTimestamp(8, Util.getCurrentTimeStamp());
			pstmt.setString(9, "PENDING");
			logger.info("notifyBankDeposit Query - "+pstmt);
			pstmt.executeUpdate();
			status = true;
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closePstmt(pstmt);
		}

		return status;
	}

	public List<BankDepositBean> getLastRecords(int userId, int numberOfRecords, Connection connection) throws LMSException {
		SimpleDateFormat dateFormat = null;
		Statement stmt = null;
		ResultSet rs = null;
		List<BankDepositBean> depositList = new ArrayList<BankDepositBean>();
		BankDepositBean depositBean = null;
		try {
			dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			stmt = connection.createStatement();
			String query = "SELECT receipt_no, bank_disp_name, amount, request_date, a.status FROM st_lms_bank_deposit_details a INNER JOIN st_lms_bank_deposit_bank_details b ON a.bank_id=b.bank_id WHERE user_id="+userId+" ORDER BY id DESC LIMIT "+numberOfRecords+";";
			logger.info("getLastRecords Query - "+query);
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				depositBean = new BankDepositBean();
				depositBean.setReceiptNo(rs.getString("receipt_no"));
				depositBean.setBankName(rs.getString("bank_disp_name"));
				depositBean.setAmount(rs.getDouble("amount"));
				depositBean.setRequestDate(dateFormat.format(rs.getTimestamp("request_date")));
				depositBean.setStatus(rs.getString("status"));
				depositList.add(depositBean);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeStmt(stmt);
			DBConnect.closeRs(rs);
		}

		return depositList;
	}

	public List<BankDepositBean> processBankDepositRequestSearch(String retName, String receiptNo, String startDate, String endDate, String status, Connection connection) throws LMSException {
		SimpleDateFormat dateFormat = null;
		Statement stmt = null;
		ResultSet rs = null;
		List<BankDepositBean> depositList = new ArrayList<BankDepositBean>();
		BankDepositBean depositBean = null;
		try {
			dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			stmt = connection.createStatement();
			String query = "SELECT id, name, receipt_no, bank_disp_name, branch_name, amount, request_date, process_date, a.status FROM st_lms_bank_deposit_details a INNER JOIN st_lms_bank_deposit_bank_details b ON a.bank_id=b.bank_id INNER JOIN st_lms_organization_master c ON a.organization_id=c.organization_id WHERE name LIKE '%"+retName+"%' AND receipt_no LIKE '%"+receiptNo+"%' AND request_date>='"+startDate+" 00:00:00' AND request_date<='"+endDate+" 23:59:59' AND a.status='"+status+"';";
			logger.info("processBankDepositRequestSearch Query - "+query);
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				String processDate = (rs.getTimestamp("process_date") == null) ? "" : dateFormat.format(rs.getTimestamp("process_date"));

				depositBean = new BankDepositBean();
				depositBean.setId(rs.getInt("id"));
				depositBean.setUserName(rs.getString("name"));
				depositBean.setReceiptNo(rs.getString("receipt_no"));
				depositBean.setBankName(rs.getString("bank_disp_name"));
				depositBean.setBranchName(rs.getString("branch_name"));
				depositBean.setAmount(rs.getDouble("amount"));
				depositBean.setRequestDate(dateFormat.format(rs.getTimestamp("request_date")));
				depositBean.setProcessDate(processDate);
				depositBean.setStatus(rs.getString("status"));
				depositList.add(depositBean);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeStmt(stmt);
			DBConnect.closeRs(rs);
		}

		return depositList;
	}

	public BankDepositBean getBankDepositRequestById(int id, Connection connection) throws LMSException {
		SimpleDateFormat dateFormat = null;
		Statement stmt = null;
		ResultSet rs = null;
		BankDepositBean depositBean = null;
		try {
			dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			stmt = connection.createStatement();
			String query = "SELECT id, user_id, a.organization_id, name, receipt_no, bank_disp_name, branch_name, amount, bank_deposit_date, request_date, a.status FROM st_lms_bank_deposit_details a INNER JOIN st_lms_bank_deposit_bank_details b ON a.bank_id=b.bank_id INNER JOIN st_lms_organization_master c ON a.organization_id=c.organization_id WHERE id="+id+" AND a.status='PENDING';";
			logger.info("getBankDepositDetails Query - "+query);
			rs = stmt.executeQuery(query);
			if(rs.next()) {
				depositBean = new BankDepositBean();
				depositBean.setId(rs.getInt("id"));
				depositBean.setUserId(rs.getInt("user_id"));
				depositBean.setOrganizationId(rs.getInt("organization_id"));
				depositBean.setUserName(rs.getString("name"));
				depositBean.setReceiptNo(rs.getString("receipt_no"));
				depositBean.setBankName(rs.getString("bank_disp_name"));
				depositBean.setBranchName(rs.getString("branch_name"));
				depositBean.setAmount(rs.getDouble("amount"));
				depositBean.setDate(dateFormat.format(rs.getTimestamp("bank_deposit_date")));
				depositBean.setRequestDate(dateFormat.format(rs.getTimestamp("request_date")));
				depositBean.setStatus(rs.getString("status"));
			} else
				return null;

			query = "SELECT a.user_id, a.organization_id FROM st_lms_user_master a INNER JOIN st_lms_user_master b ON a.user_id=b.parent_user_id WHERE b.user_id="+depositBean.getUserId()+";";
			logger.info("getParentDetails Query - "+query);
			rs = stmt.executeQuery(query);
			if(rs.next()) {
				depositBean.setParentUserId(rs.getInt("user_id"));
				depositBean.setParentOrgId(rs.getInt("organization_id"));
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeStmt(stmt);
			DBConnect.closeRs(rs);
		}

		return depositBean;
	}

	public boolean updateBankDepositDetails(int userId, String status, List<Integer> idList, Connection connection) throws LMSException {
		boolean updateStatus = false;
		PreparedStatement pstmt = null;
		try {
			String processDate = Util.getCurrentTimeString();

			pstmt = connection.prepareStatement("UPDATE st_lms_bank_deposit_details SET process_date='"+processDate+"', process_by_user_id="+userId+", status='"+status+"' WHERE id=? AND status='PENDING';");
			for(Integer id : idList) {
				pstmt.setInt(1, id);
				logger.info("updateBankDepositDetails Query - "+pstmt);
				pstmt.addBatch();
			}
			pstmt.executeBatch();
			updateStatus = true;
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closePstmt(pstmt);
		}

		return updateStatus;
	}
}