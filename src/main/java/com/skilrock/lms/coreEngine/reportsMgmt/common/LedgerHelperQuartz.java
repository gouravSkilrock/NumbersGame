package com.skilrock.lms.coreEngine.reportsMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.FormatNumber;
import com.skilrock.lms.common.utility.PropertyLoader;

/**
 * This Helper class is for generation of Agent and Back Office ledger.
 * 
 * @author Skilrock Technologies
 * 
 */
public class LedgerHelperQuartz {
	private static Log logger = LogFactory.getLog(LedgerHelperQuartz.class);
	String address = null;
	double AGENT_ACCOUNT_CURRENT;
	double agent_bank_acc_current;
	private int agent_id;
	double agent_pwt_charges_current;
	double agent_pwt_charges_rcv_current;
	double agent_pwt_pay_current;
	double agent_vat_pay_current;
	double agnt_prchse_current;
	double agnt_purchs_ret_current;
	double agnt_pwt_rcv_current;
	double agnt_sale_acc_current;
	double agnt_sale_ret_current;

	double BANK_ACC_CURRENT;

	double bo_bank_acc_current;
	double bo_pwt_charges_current;
	double bo_pwt_pay_current;
	double bo_sale_acc_current;
	double bo_sale_ret_current;
	Connection connection = null;
	Date date = null;
	DateFormat dateFormat1 = new java.text.SimpleDateFormat("dd-MM-yyyy");

	String dep_Date = getDeployMentDate();
	String genNum = null;
	double GOVT_COMM_CURRENT;
	String orgName = null;
	double PLAYER_CASH_CURRENT;
	double PLAYER_PWT_CURRENT;
	double PLAYER_TDS_CURRENT;
	// Date present_date = null;
	Timestamp present_date = null;
	double PWT_CHARGES_CURRENT;
	double PWT_PAY_CURRENT;
	String query1 = null;
	String query2 = null;
	String query3 = null;
	PreparedStatement rcptPS = null;
	String rcptQry = null;
	ResultSet rcptRS = null;
	double retailer_org_id_current;
	ResultSet rs = null;

	ResultSet rs1 = null;
	ResultSet rs2 = null;
	double SALE_ACC_CURRENT;
	double SALE_RET_CURRENT;

	Statement statement = null;
	PreparedStatement stmt = null;
	PreparedStatement stmt1 = null;
	PreparedStatement stmt2 = null;

	double TDS_PAY_CURRENT;
	Timestamp to_Date = null;
	double UNCLM_GOVT_CURRENT;
	PreparedStatement updatePstmt = null;
	PreparedStatement updPstmt = null;
	PreparedStatement upPstmt = null;
	double VAT_PAY_CURRENT;

	/**
	 * This method is for entering the data into back office ledger.
	 * 
	 * @param toDate_Bean
	 *            is Date and used for comparing the date from Database.
	 * @throws LMSException
	 */
	public String getDeployMentDate() {
		String dep_Date = null;

		if (ServletActionContext.getServletContext() != null) {
			dep_Date = (String) ServletActionContext.getServletContext()
					.getAttribute("DEPLOYMENT_DATE");
		} else {
			PropertyLoader.loadProperties("RMS/LMS.properties");
			dep_Date = PropertyLoader.getProperty("deployment_date");
		}

		return dep_Date;
	}

	private String chkArcLedgerTable() {
		String archDate = null;
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con.setAutoCommit(false);
			String selQry = "select max(last_date) archDate from tempdate_history";
			pstmt = con.prepareStatement(selQry);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				archDate = rs.getString("archDate");
			} else {
				archDate = null;
			}

			if (archDate != null) {
				String insQry = "insert into st_lms_bo_current_balance (account_type, current_balance, agent_org_id) select account_type,current_balance,agent_org_id from st_lms_bo_current_balance_history_arch where date(transaction_date)=?";
				pstmt = con.prepareStatement(insQry);
				pstmt.setString(1, archDate);
				pstmt.executeUpdate();

				insQry = "insert into st_lms_agent_current_balance (account_type,current_balance,agent_org_id) select account_type,current_balance,agent_org_id from st_lms_agent_current_balance_history_arch where date(transaction_date)=?";
				pstmt = con.prepareStatement(insQry);
				pstmt.setString(1, archDate);
				pstmt.executeUpdate();
			} else {
				archDate = new java.sql.Date(dateFormat1.parse(
						getDeployMentDate()).getTime())
						+ "";
			}
			con.commit();

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return archDate;
	}

	// END OF METHOD

	// Fetching data for ajax list on Boledger

	public double getDouble(double number) {
		double d = 0.0;
		d = number * 100;
		d = Math.round(d);
		d = d / 100;

		return d;
	}

	/**
	 * This method is for entering the data into agent ledger.
	 * 
	 * @param toDate_Bean
	 *            is Date and used for comparing the date from Database.
	 * @param userId
	 *            is integer and is current user's Organization Id
	 * @throws NumberFormatException
	 */

	public void ledgerAgentEntry(Timestamp toDate_Bean, int userId)
			throws NumberFormatException {

		int retailerPresent = 0;
		agent_id = userId;
		try {
			//  
			connection = DBConnect.getConnection();
			connection.setAutoCommit(false);
			present_date = toDate_Bean;
			toDate_Bean = new Timestamp(present_date.getTime() + 24 * 60 * 60
					* 1000 - 1000);
			to_Date = toDate_Bean;
			String rcptQry = "select sbr.receipt_id,sbr.generated_id from st_lms_bo_receipts_trn_mapping sbrtm , st_lms_bo_receipts sbr where transaction_id=? and sbrtm.receipt_id=sbr.receipt_id";

			if (present_date.before(to_Date)) {
				logger.debug("LH-AGT**PresentDate-" + present_date
						+ "**ToDate-" + toDate_Bean);

				int i = 0;
				String genNum = null;
				stmt = connection.prepareStatement(QueryManager
						.getST2AgentCurrentBal());
				stmt.setInt(1, agent_id);
				rs = stmt.executeQuery();
				while (rs.next()) {
					if (rs.getString("account_type").equals("AGNT_BANK_ACC")) {
						agent_bank_acc_current = rs
								.getDouble("current_balance");
					} else if (rs.getString("account_type").equals(
							"AGNT_SALE_ACC")) {
						agnt_sale_acc_current = rs.getDouble("current_balance");
					} else if (rs.getString("account_type").equals(
							"AGNT_SALE_RET")) {
						agnt_sale_ret_current = rs.getDouble("current_balance");
					} else if (rs.getString("account_type").equals(
							"AGNT_PWT_PAY")) {
						agent_pwt_pay_current = rs.getDouble("current_balance");
					} else if (rs.getString("account_type").equals(
							"AGNT_PWT_RCV")) {
						agnt_pwt_rcv_current = rs.getDouble("current_balance");
					} else if (rs.getString("account_type").equals(
							"AGNT_PRCHSE_ACC")) {
						agnt_prchse_current = rs.getDouble("current_balance");
					} else if (rs.getString("account_type").equals(
							"AGNT_PURCHS_RET_ACC")) {
						agnt_purchs_ret_current = rs
								.getDouble("current_balance");
					} else if (rs.getString("account_type").equals(
							"BO_BANK_ACC")) {
						bo_bank_acc_current = rs.getDouble("current_balance");
					} else if (rs.getString("account_type").equals(
							"BO_SALE_ACC")) {
						bo_sale_acc_current = rs.getDouble("current_balance");
					} else if (rs.getString("account_type").equals(
							"BO_SALE_RET")) {
						bo_sale_ret_current = rs.getDouble("current_balance");
					} else if (rs.getString("account_type")
							.equals("BO_PWT_PAY")) {
						bo_pwt_pay_current = rs.getDouble("current_balance");
					} else if (rs.getString("account_type").equals(
							"AGNT_PWT_CHARGES_RCV")) {
						agent_pwt_charges_rcv_current = rs
								.getDouble("current_balance");
					} else if (rs.getString("account_type").equals(
							"BO_PWT_CHARGES")) {
						bo_pwt_charges_current = rs
								.getDouble("current_balance");
					} else if (rs.getString("account_type").equals(
							"AGENT_PWT_CHARGES")) {
						agent_pwt_charges_current = rs
								.getDouble("current_balance");
					} else if (rs.getString("account_type").equals(
							"AGENT_VAT_PAY")) {
						agent_vat_pay_current = rs.getDouble("current_balance");
					} else if (rs.getString("account_type").equals(
							"AGNT_PLAYER_TDS")) {
						PLAYER_TDS_CURRENT = rs.getDouble("current_balance");
					} else if (rs.getString("account_type").equals(
							"AGNT_PLAYER_PWT")) {
						PLAYER_PWT_CURRENT = rs.getDouble("current_balance");
					} else if (rs.getString("account_type").equals(
							"AGNT_PLAYER_CAS")) {
						PLAYER_CASH_CURRENT = rs.getDouble("current_balance");
					} else if (rs.getString("account_type").equals(
							"AGNT_TDS_PAY")) {
						PLAYER_CASH_CURRENT = rs.getDouble("current_balance");
					}
					i++;
				}

				if (rs != null) {
					rs.close();
				}
				if (i == 0) {
					stmt
							.executeUpdate("insert into st_lms_agent_current_balance (account_type,current_balance,agent_org_id) values ('AGNT_BANK_ACC',0.0,"
									+ agent_id + ")");
					stmt
							.executeUpdate("insert into st_lms_agent_current_balance (account_type,current_balance,agent_org_id) values ('AGNT_SALE_ACC',0.0,"
									+ agent_id + ")");
					stmt
							.executeUpdate("insert into st_lms_agent_current_balance (account_type,current_balance,agent_org_id) values ('AGNT_SALE_RET',0.0,"
									+ agent_id + ")");
					stmt
							.executeUpdate("insert into st_lms_agent_current_balance (account_type,current_balance,agent_org_id) values ('AGNT_PWT_PAY',0.0,"
									+ agent_id + ")");
					stmt
							.executeUpdate("insert into st_lms_agent_current_balance (account_type,current_balance,agent_org_id) values ('AGNT_PWT_RCV',0.0,"
									+ agent_id + ")");
					stmt
							.executeUpdate("insert into st_lms_agent_current_balance (account_type,current_balance,agent_org_id) values ('AGNT_PRCHSE_ACC',0.0,"
									+ agent_id + ")");
					stmt
							.executeUpdate("insert into st_lms_agent_current_balance (account_type,current_balance,agent_org_id) values ('AGNT_PURCHS_RET_ACC',0.0,"
									+ agent_id + ")");
					stmt
							.executeUpdate("insert into st_lms_agent_current_balance (account_type,current_balance,agent_org_id) values ('BO_BANK_ACC',0.0,"
									+ agent_id + ")");
					stmt
							.executeUpdate("insert into st_lms_agent_current_balance (account_type,current_balance,agent_org_id) values ('BO_SALE_ACC',0.0,"
									+ agent_id + ")");
					stmt
							.executeUpdate("insert into st_lms_agent_current_balance (account_type,current_balance,agent_org_id) values ('BO_SALE_RET',0.0,"
									+ agent_id + ")");
					stmt
							.executeUpdate("insert into st_lms_agent_current_balance (account_type,current_balance,agent_org_id) values ('BO_PWT_PAY',0.0,"
									+ agent_id + ")");
					stmt
							.executeUpdate("insert into st_lms_agent_current_balance (account_type,current_balance,agent_org_id) values ('AGNT_PLAYER_TDS',0.0,"
									+ agent_id + ")");
					stmt
							.executeUpdate("insert into st_lms_agent_current_balance (account_type,current_balance,agent_org_id) values ('AGNT_PLAYER_PWT',0.0,"
									+ agent_id + ")");
					stmt
							.executeUpdate("insert into st_lms_agent_current_balance (account_type,current_balance,agent_org_id) values ('AGNT_PLAYER_CAS',0.0,"
									+ agent_id + ")");
					stmt
							.executeUpdate("insert into st_lms_agent_current_balance (account_type,current_balance,agent_org_id) values ('AGNT_TDS_PAY',0.0,"
									+ agent_id + ")");
					stmt
							.executeUpdate("insert into st_lms_agent_current_balance (account_type,current_balance,agent_org_id) values ('AGNT_PWT_CHARGES_RCV',0.0,"
									+ agent_id + ")");
					stmt
							.executeUpdate("insert into st_lms_agent_current_balance (account_type,current_balance,agent_org_id) values ('BO_PWT_CHARGES',0.0,"
									+ agent_id + ")");
					stmt
							.executeUpdate("insert into st_lms_agent_current_balance (account_type,current_balance,agent_org_id) values ('AGENT_PWT_CHARGES',0.0,"
									+ agent_id + ")");
					stmt
							.executeUpdate("insert into st_lms_agent_current_balance (account_type,current_balance,agent_org_id) values ('AGENT_VAT_PAY',0.0,"
									+ agent_id + ")");

					if (stmt != null) {
						stmt.close();
					}

				}

				// ///////////////////////Start of Back Office and Agent Ledger
				// Entry Part/////////////

				stmt1 = connection.prepareStatement(QueryManager
						.getST2BoAgtTransactions());
				stmt1.setTimestamp(1, present_date);
				stmt1.setTimestamp(2, to_Date);
				stmt1.setInt(3, agent_id);
				logger.debug("LH-AGT**Select Trxn Master**" + stmt1);
				rs1 = stmt1.executeQuery();
				while (rs1.next()) {

					long transaction_id = rs1.getLong("transaction_id");
					String transaction_type = rs1.getString("transaction_type");
					String RECEIPT_ID = "N.A.";

					rcptPS = connection.prepareStatement(rcptQry);
					rcptPS.setLong(1, transaction_id);
					rcptRS = rcptPS.executeQuery();

					while (rcptRS.next()) {
						genNum = rcptRS.getString("generated_id");
						if (genNum != null
								&& !genNum.trim().equals("")
								&& !(genNum.contains("DLB") || genNum
										.contains("DSB"))) {
							RECEIPT_ID = rcptRS.getString("generated_id");
						}

					}

					if (rcptPS != null) {
						rcptPS.close();
					}
					if (rcptRS != null) {
						rcptRS.close();
					}

					double amount = 0;
					double pwt_charges_amount = 0;
					Timestamp transaction_date = null;
					// java.sql.Date transaction_date = null;
					String acc_type = "";
					String acc_type_charges = "";
					String saleQuery = "";
					String saleRetQuery = "";
					if (transaction_type.equalsIgnoreCase("Sale")
							|| transaction_type.equalsIgnoreCase("DG_SALE")
							|| transaction_type.equalsIgnoreCase("CS_SALE") || transaction_type.equalsIgnoreCase("OLA_DEPOSIT")) {
						if (transaction_type.equalsIgnoreCase("Sale")) {
							saleQuery = QueryManager.getST2BoSale();
						} else if (transaction_type.equalsIgnoreCase("DG_SALE")) {
							saleQuery = QueryManager.getST2BoDrwGmSale();
						} else if (transaction_type.equalsIgnoreCase("CS_SALE")) {
							saleQuery = QueryManager.getST2BoCsSale();
						}
						else if (transaction_type.equalsIgnoreCase("OLA_DEPOSIT")) {
							saleQuery = QueryManager.getST2BoOlaDeposit();
						}
						stmt2 = connection.prepareStatement(saleQuery);
						stmt2.setLong(1, transaction_id);
						rs2 = stmt2.executeQuery();
						logger.debug("LH-AGT**" + transaction_type
								+ "**TRX_ID-" + transaction_id + "**R_ID-"
								+ RECEIPT_ID);
						while (rs2.next()) {
							amount = rs2.getDouble("net_amt");
							transaction_date = rs2
									.getTimestamp("transaction_date");
							acc_type = "AGNT_PRCHSE_ACC";
						}

						if (stmt2 != null) {
							stmt2.close();
						}
						if (rs2 != null) {
							rs2.close();
						}

						stmt = connection.prepareStatement(QueryManager
								.getST6AgtCurrBal());
						stmt.setString(1, acc_type);
						stmt.setInt(2, agent_id);
						rs = stmt.executeQuery();
						while (rs.next()) {
							agnt_prchse_current = rs
									.getDouble("current_balance");

						}

						if (stmt != null) {
							stmt.close();
						}
						if (rs != null) {
							rs.close();
						}

						agnt_prchse_current = agnt_prchse_current - amount;
						bo_sale_acc_current = bo_sale_acc_current + amount;

						logger.debug("LH-AGT**" + transaction_date + "**AMT-"
								+ amount + "**ACT_TYPE-" + acc_type
								+ "**AGT_PURCH_ACC=" + agnt_prchse_current
								+ "**BO_SALE_ACC" + bo_sale_acc_current);
						query1 = QueryManager.getST2InsertAgentLedger();
						updatePstmt = connection.prepareStatement(query1);

						updatePstmt.setInt(1, agent_id);
						updatePstmt.setString(2, "PURCH");
						updatePstmt.setLong(3, transaction_id);
						updatePstmt.setTimestamp(4, transaction_date);
						updatePstmt.setString(5, -amount + "");
						updatePstmt.setString(6, acc_type);
						updatePstmt.setString(7, agnt_prchse_current + "");
						updatePstmt.setString(8, "BO");
						updatePstmt.setString(9, RECEIPT_ID);
						updatePstmt.execute();

						if (updatePstmt != null) {
							updatePstmt.close();
						}

						query2 = QueryManager.getST2InsertAgentLedger();
						upPstmt = connection.prepareStatement(query2);
						upPstmt.setInt(1, agent_id);
						upPstmt.setString(2, "PURCH");
						upPstmt.setLong(3, transaction_id);
						upPstmt.setTimestamp(4, transaction_date);
						upPstmt.setString(5, amount + "");
						upPstmt.setString(6, "BO_SALE_ACC");
						upPstmt.setString(7, bo_sale_acc_current + "");
						upPstmt.setString(8, "BO");
						upPstmt.setString(9, RECEIPT_ID);
						upPstmt.execute();

						if (upPstmt != null) {
							upPstmt.close();
						}

						query3 = QueryManager.getST2UpadateAgentCurrentBal();
						updPstmt = connection.prepareStatement(query3);
						updPstmt.setString(1, agnt_prchse_current + "");
						updPstmt.setString(2, acc_type);
						updPstmt.setInt(3, agent_id);
						updPstmt.execute();

						if (updPstmt != null) {
							updPstmt.close();
						}

					} else if (transaction_type.equalsIgnoreCase("sale_ret")
							|| transaction_type
									.equalsIgnoreCase("DG_REFUND_FAILED")
							|| transaction_type
									.equalsIgnoreCase("DG_REFUND_CANCEL")
							|| transaction_type
									.equalsIgnoreCase("CS_CANCEL_SERVER")
							|| transaction_type
									.equalsIgnoreCase("CS_CANCEL_RET")
							|| transaction_type.equalsIgnoreCase("OLA_DEPOSIT_REFUND")) {

						if (transaction_type.equalsIgnoreCase("sale_ret")) {
							saleRetQuery = QueryManager.getST2BoSaleRet();
						} else if (transaction_type
								.equalsIgnoreCase("DG_REFUND_FAILED")
								|| transaction_type
										.equalsIgnoreCase("DG_REFUND_CANCEL")) {
							saleRetQuery = QueryManager.getST2BoDrwGmRefnd();
						} else if (transaction_type
								.equalsIgnoreCase("CS_CANCEL_SERVER")
								|| transaction_type
										.equalsIgnoreCase("CS_CANCEL_RET")) {
							saleRetQuery = QueryManager.getST2BoCsRefnd();
						}else if(transaction_type.equalsIgnoreCase("OLA_DEPOSIT_REFUND"))
						{
							saleRetQuery = QueryManager.getST2BoOlaRefnd();
						}
						stmt2 = connection.prepareStatement(saleRetQuery);
						stmt2.setLong(1, transaction_id);
						rs2 = stmt2.executeQuery();
						logger.debug("LH-AGT**" + transaction_type
								+ "**TRX_ID-" + transaction_id + "**R_ID-"
								+ RECEIPT_ID);
						while (rs2.next()) {
							amount = rs2.getDouble("net_amt");
							transaction_date = rs2
									.getTimestamp("transaction_date");
							acc_type = "AGNT_PURCHS_RET_ACC";
						}

						if (stmt2 != null) {
							stmt2.close();
						}
						if (rs2 != null) {
							rs2.close();
						}

						stmt = connection.prepareStatement(QueryManager
								.getST6AgtCurrBal());
						stmt.setString(1, acc_type);
						stmt.setInt(2, agent_id);
						rs = stmt.executeQuery();
						while (rs.next()) {
							agnt_purchs_ret_current = rs
									.getDouble("current_balance");
						}

						if (stmt != null) {
							stmt.close();
						}
						if (rs != null) {
							rs.close();
						}

						agnt_purchs_ret_current = agnt_purchs_ret_current
								+ amount;
						bo_sale_ret_current = bo_sale_ret_current - amount;

						logger.debug("LH-AGT**" + transaction_date + "**AMT-"
								+ amount + "**ACT_TYPE-" + acc_type
								+ "**AGT_PURCH_RET_ACC="
								+ agnt_purchs_ret_current + "**BO_SALE_RET_ACC"
								+ bo_sale_ret_current);

						query2 = QueryManager.getST2InsertAgentLedger();
						upPstmt = connection.prepareStatement(query2);
						upPstmt.setInt(1, agent_id);
						upPstmt.setString(2, "PURCH_RET");
						upPstmt.setLong(3, transaction_id);
						upPstmt.setTimestamp(4, transaction_date);
						upPstmt.setString(5, -amount + "");
						upPstmt.setString(6, "BO_SALE_RET_ACC");
						upPstmt.setString(7, bo_sale_ret_current + "");
						upPstmt.setString(8, "BO");
						upPstmt.setString(9, RECEIPT_ID);
						upPstmt.execute();

						if (upPstmt != null) {
							upPstmt.close();
						}

						query1 = QueryManager.getST2InsertAgentLedger();
						updatePstmt = connection.prepareStatement(query1);

						updatePstmt.setInt(1, agent_id);
						updatePstmt.setString(2, "PURCH_RET");
						updatePstmt.setLong(3, transaction_id);
						updatePstmt.setTimestamp(4, transaction_date);
						updatePstmt.setString(5, amount + "");
						updatePstmt.setString(6, acc_type);
						updatePstmt.setString(7, agnt_purchs_ret_current + "");
						updatePstmt.setString(8, "BO");
						updatePstmt.setString(9, RECEIPT_ID);
						updatePstmt.execute();

						if (updatePstmt != null) {
							updatePstmt.close();
						}

						query3 = QueryManager.getST2UpadateAgentCurrentBal();
						updPstmt = connection.prepareStatement(query3);
						updPstmt.setString(1, agnt_purchs_ret_current + "");
						updPstmt.setString(2, acc_type);
						updPstmt.setInt(3, agent_id);
						updPstmt.execute();

						if (updatePstmt != null) {
							updatePstmt.close();
						}

					}

					else if (transaction_type.equalsIgnoreCase("cheque")) {
						// This has been changed to prepared statement 10-03-08
						stmt2 = connection.prepareStatement(QueryManager
								.getST2BoChq());
						stmt2.setLong(1, transaction_id);
						rs2 = stmt2.executeQuery();
						// rs2 =
						// stmt2.executeQuery(QueryManager.getST2BoChq()+""+
						// transaction_id + "");;

						while (rs2.next()) {
							amount = rs2.getDouble("cheque_amt");
							transaction_date = rs2
									.getTimestamp("transaction_date");
							acc_type = "AGNT_BANK_ACC";
						}

						if (stmt2 != null) {
							stmt2.close();
						}
						if (rs2 != null) {
							rs2.close();
						}

						stmt = connection.prepareStatement(QueryManager
								.getST6AgtCurrBal());
						stmt.setString(1, acc_type);
						stmt.setInt(2, agent_id);
						rs = stmt.executeQuery();
						logger.debug("LH-AGT**" + transaction_type
								+ "**TRX_ID-" + transaction_id + "**R_ID-"
								+ RECEIPT_ID);

						while (rs.next()) {
							agent_bank_acc_current = rs
									.getDouble("current_balance");
						}

						if (stmt != null) {
							stmt.close();
						}
						if (rs != null) {
							rs.close();
						}

						agent_bank_acc_current = agent_bank_acc_current
								+ amount;
						bo_bank_acc_current = bo_bank_acc_current - amount;
						logger.debug("LH-AGT**" + transaction_date + "**AMT-"
								+ amount + "**ACT_TYPE-" + acc_type
								+ "**AGT_BANK_ACC=" + agent_bank_acc_current
								+ "**BO_ACC" + bo_sale_acc_current);
						query2 = QueryManager.getST2InsertAgentLedger();
						upPstmt = connection.prepareStatement(query2);
						upPstmt.setInt(1, agent_id);
						upPstmt.setString(2, "BO_" + transaction_type);
						upPstmt.setLong(3, transaction_id);
						upPstmt.setTimestamp(4, transaction_date);
						upPstmt.setString(5, -amount + "");
						upPstmt.setString(6, "BO_BANK_ACC");
						upPstmt.setString(7, bo_bank_acc_current + "");
						upPstmt.setString(8, "BO");
						upPstmt.setString(9, RECEIPT_ID);
						upPstmt.execute();

						if (upPstmt != null) {
							upPstmt.close();
						}

						query1 = QueryManager.getST2InsertAgentLedger();
						updatePstmt = connection.prepareStatement(query1);

						updatePstmt.setInt(1, agent_id);
						updatePstmt.setString(2, "BO_" + transaction_type);
						updatePstmt.setLong(3, transaction_id);
						updatePstmt.setTimestamp(4, transaction_date);
						updatePstmt.setString(5, amount + "");
						updatePstmt.setString(6, acc_type);
						updatePstmt.setString(7, agent_bank_acc_current + "");
						updatePstmt.setString(8, "BO");
						updatePstmt.setString(9, RECEIPT_ID);
						updatePstmt.execute();

						if (updatePstmt != null) {
							updatePstmt.close();
						}

						query3 = QueryManager.getST2UpadateAgentCurrentBal();
						updPstmt = connection.prepareStatement(query3);
						updPstmt.setString(1, agent_bank_acc_current + "");
						updPstmt.setString(2, acc_type);
						updPstmt.setInt(3, agent_id);
						updPstmt.execute();

						if (updPstmt != null) {
							updPstmt.close();
						}

					}

					else if (transaction_type.equalsIgnoreCase("chq_bounce")) {
						// This has been changed to prepared statement 10-03-08
						stmt2 = connection.prepareStatement(QueryManager
								.getST2BoChqBounce());
						stmt2.setLong(1, transaction_id);
						rs2 = stmt2.executeQuery();
						// rs2 =
						// stmt2.executeQuery(QueryManager.getST2BoChqBounce()+""+
						// transaction_id + "");
						while (rs2.next()) {
							amount = rs2.getDouble("cheque_amt");
							transaction_date = rs2
									.getTimestamp("transaction_date");
							acc_type = "AGNT_BANK_ACC";
						}

						if (stmt2 != null) {
							stmt2.close();
						}
						if (rs2 != null) {
							rs2.close();
						}

						stmt = connection.prepareStatement(QueryManager
								.getST6AgtCurrBal());
						stmt.setString(1, acc_type);
						stmt.setInt(2, agent_id);
						rs = stmt.executeQuery();
						logger.debug("LH-AGT**" + transaction_type
								+ "**TRX_ID-" + transaction_id + "**R_ID-"
								+ RECEIPT_ID);
						while (rs.next()) {
							agent_bank_acc_current = rs
									.getDouble("current_balance");
						}

						if (stmt != null) {
							stmt.close();
						}
						if (rs != null) {
							rs.close();
						}

						agent_bank_acc_current = agent_bank_acc_current
								- amount;
						bo_bank_acc_current = bo_bank_acc_current + amount;
						logger.debug("LH-AGT**" + transaction_date + "**AMT-"
								+ amount + "**ACT_TYPE-" + acc_type
								+ "**AGT_BANK_ACC=" + agent_bank_acc_current
								+ "**BO_ACC" + bo_sale_acc_current);
						query1 = QueryManager.getST2InsertAgentLedger();
						updatePstmt = connection.prepareStatement(query1);

						updatePstmt.setInt(1, agent_id);
						updatePstmt.setString(2, "BO_CH_BOUN");
						updatePstmt.setLong(3, transaction_id);
						updatePstmt.setTimestamp(4, transaction_date);
						updatePstmt.setString(5, -amount + "");
						updatePstmt.setString(6, acc_type);
						updatePstmt.setString(7, agent_bank_acc_current + "");
						updatePstmt.setString(8, "BO");
						updatePstmt.setString(9, RECEIPT_ID);
						updatePstmt.execute();

						if (updatePstmt != null) {
							updatePstmt.close();
						}

						query2 = QueryManager.getST2InsertAgentLedger();
						upPstmt = connection.prepareStatement(query2);
						upPstmt.setInt(1, agent_id);
						upPstmt.setString(2, "BO_CH_BOUN");
						upPstmt.setLong(3, transaction_id);
						upPstmt.setTimestamp(4, transaction_date);
						upPstmt.setString(5, amount + "");
						upPstmt.setString(6, "BO_BANK_ACC");
						upPstmt.setString(7, bo_bank_acc_current + "");
						upPstmt.setString(8, "BO");
						upPstmt.setString(9, RECEIPT_ID);
						upPstmt.execute();

						if (upPstmt != null) {
							upPstmt.close();
						}

						query3 = QueryManager.getST2UpadateAgentCurrentBal();
						updPstmt = connection.prepareStatement(query3);
						updPstmt.setString(1, agent_bank_acc_current + "");
						updPstmt.setString(2, acc_type);
						updPstmt.setInt(3, agent_id);
						updPstmt.execute();

						if (updPstmt != null) {
							updPstmt.close();
						}

					}

					else if (transaction_type.equalsIgnoreCase("DR_NOTE_CASH")
							|| transaction_type.equalsIgnoreCase("DR_NOTE")) {
						// This has been changed to prepared statement 10-03-08
						stmt2 = connection.prepareStatement(QueryManager
								.getST6BoDebitNote());
						stmt2.setLong(1, transaction_id);
						rs2 = stmt2.executeQuery();
						// rs2 =
						// stmt2.executeQuery(QueryManager.getST2BoChqBounce()+""+
						// transaction_id + "");
						while (rs2.next()) {
							amount = rs2.getDouble("amount");
							transaction_date = rs2
									.getTimestamp("transaction_date");
							acc_type = "AGNT_BANK_ACC";
						}

						if (stmt2 != null) {
							stmt2.close();
						}
						if (rs2 != null) {
							rs2.close();
						}
						logger.debug("LH-AGT**" + transaction_type
								+ "**TRX_ID-" + transaction_id + "**R_ID-"
								+ RECEIPT_ID);
						stmt = connection.prepareStatement(QueryManager
								.getST6AgtCurrBal());
						stmt.setString(1, acc_type);
						stmt.setInt(2, agent_id);
						rs = stmt.executeQuery();
						// This has been changed to prepared statement and moved
						// to query manager 10-03-08
						// rs = stmt.executeQuery("select current_balance from
						// st_lms_agent_current_balance where account_type = '"+
						// acc_type + "' and agent_id = "+agent_id);
						while (rs.next()) {
							agent_bank_acc_current = rs
									.getDouble("current_balance");
						}

						if (stmt != null) {
							stmt.close();
						}
						if (rs != null) {
							rs.close();
						}

						agent_bank_acc_current = agent_bank_acc_current
								- amount;
						bo_bank_acc_current = bo_bank_acc_current + amount;
						logger.debug("LH-AGT**" + transaction_date + "**AMT-"
								+ amount + "**ACT_TYPE-" + acc_type
								+ "**AGT_BANK_ACC=" + agent_bank_acc_current
								+ "**BO_ACC" + bo_bank_acc_current);
						query1 = QueryManager.getST2InsertAgentLedger();
						updatePstmt = connection.prepareStatement(query1);

						updatePstmt.setInt(1, agent_id);
						updatePstmt.setString(2, "BO_" + transaction_type);
						updatePstmt.setLong(3, transaction_id);
						updatePstmt.setTimestamp(4, transaction_date);
						updatePstmt.setString(5, -amount + "");
						updatePstmt.setString(6, acc_type);
						updatePstmt.setString(7, agent_bank_acc_current + "");
						updatePstmt.setString(8, "BO");
						updatePstmt.setString(9, RECEIPT_ID);
						updatePstmt.execute();

						if (updatePstmt != null) {
							updatePstmt.close();
						}

						query2 = QueryManager.getST2InsertAgentLedger();
						upPstmt = connection.prepareStatement(query2);
						upPstmt.setInt(1, agent_id);
						upPstmt.setString(2, "BO_" + transaction_type);
						upPstmt.setLong(3, transaction_id);
						upPstmt.setTimestamp(4, transaction_date);
						upPstmt.setString(5, amount + "");
						upPstmt.setString(6, "BO_BANK_ACC");
						upPstmt.setString(7, bo_bank_acc_current + "");
						upPstmt.setString(8, "BO");
						upPstmt.setString(9, RECEIPT_ID);
						upPstmt.execute();

						if (upPstmt != null) {
							upPstmt.close();
						}

						query3 = QueryManager.getST2UpadateAgentCurrentBal();
						updPstmt = connection.prepareStatement(query3);
						updPstmt.setString(1, agent_bank_acc_current + "");
						updPstmt.setString(2, acc_type);
						updPstmt.setInt(3, agent_id);
						updPstmt.execute();

						if (updPstmt != null) {
							updPstmt.close();
						}

					} else if (transaction_type
							.equalsIgnoreCase("CR_NOTE_CASH")
							|| transaction_type.equalsIgnoreCase("CR_NOTE")) {
						// This has been changed to prepared statement 10-03-08
						stmt2 = connection.prepareStatement(QueryManager
								.getST6BoCreditNote());
						stmt2.setLong(1, transaction_id);
						rs2 = stmt2.executeQuery();
						logger.debug("LH-AGT**" + transaction_type
								+ "**TRX_ID-" + transaction_id + "**R_ID-"
								+ RECEIPT_ID);
						while (rs2.next()) {
							amount = rs2.getDouble("amount");
							transaction_date = rs2
									.getTimestamp("transaction_date");
							acc_type = "AGNT_BANK_ACC";
						}

						if (stmt2 != null) {
							stmt2.close();
						}
						if (rs2 != null) {
							rs2.close();
						}

						stmt = connection.prepareStatement(QueryManager
								.getST6AgtCurrBal());
						stmt.setString(1, acc_type);
						stmt.setInt(2, agent_id);
						rs = stmt.executeQuery();

						while (rs.next()) {
							agent_bank_acc_current = rs
									.getDouble("current_balance");
						}

						if (stmt != null) {
							stmt.close();
						}
						if (rs != null) {
							rs.close();
						}

						agent_bank_acc_current = agent_bank_acc_current
								+ amount;
						bo_bank_acc_current = bo_bank_acc_current - amount;
						logger.debug("LH-AGT**" + transaction_date + "**AMT-"
								+ amount + "**ACT_TYPE-" + acc_type
								+ "**AGT_ACC" + agent_bank_acc_current
								+ "**BO_ACC" + bo_bank_acc_current);
						query2 = QueryManager.getST2InsertAgentLedger();
						upPstmt = connection.prepareStatement(query2);
						upPstmt.setInt(1, agent_id);
						upPstmt.setString(2, "BO_" + transaction_type);
						upPstmt.setLong(3, transaction_id);
						upPstmt.setTimestamp(4, transaction_date);
						upPstmt.setString(5, -amount + "");
						upPstmt.setString(6, "BO_BANK_ACC");
						upPstmt.setString(7, bo_bank_acc_current + "");
						upPstmt.setString(8, "BO");
						upPstmt.setString(9, RECEIPT_ID);
						upPstmt.execute();

						if (upPstmt != null) {
							upPstmt.close();
						}

						query1 = QueryManager.getST2InsertAgentLedger();
						updatePstmt = connection.prepareStatement(query1);

						updatePstmt.setInt(1, agent_id);
						updatePstmt.setString(2, "BO_" + transaction_type);
						updatePstmt.setLong(3, transaction_id);
						updatePstmt.setTimestamp(4, transaction_date);
						updatePstmt.setString(5, amount + "");
						updatePstmt.setString(6, acc_type);
						updatePstmt.setString(7, agent_bank_acc_current + "");
						updatePstmt.setString(8, "BO");
						updatePstmt.setString(9, RECEIPT_ID);
						updatePstmt.execute();

						if (updatePstmt != null) {
							updatePstmt.close();
						}

						query3 = QueryManager.getST2UpadateAgentCurrentBal();
						updPstmt = connection.prepareStatement(query3);
						updPstmt.setString(1, agent_bank_acc_current + "");
						updPstmt.setString(2, acc_type);
						updPstmt.setInt(3, agent_id);
						updPstmt.execute();

						if (updPstmt != null) {
							updPstmt.close();
						}

					}

					else if (transaction_type.equalsIgnoreCase("cash")) {
						stmt2 = connection.prepareStatement(QueryManager
								.getST2BoCash());
						stmt2.setLong(1, transaction_id);
						rs2 = stmt2.executeQuery();
						// rs2 =
						// stmt2.executeQuery(QueryManager.getST2BoCash()+""+
						// transaction_id + "");
						while (rs2.next()) {
							amount = rs2.getDouble("amount");
							transaction_date = rs2
									.getTimestamp("transaction_date");
							acc_type = "AGNT_BANK_ACC";
						}

						if (stmt2 != null) {
							stmt2.close();
						}
						if (rs2 != null) {
							rs2.close();
						}
						logger.debug("LH-AGT**" + transaction_type
								+ "**TRX_ID-" + transaction_id + "**R_ID-"
								+ RECEIPT_ID);
						stmt = connection.prepareStatement(QueryManager
								.getST6AgtCurrBal());
						stmt.setString(1, acc_type);
						stmt.setInt(2, agent_id);
						rs = stmt.executeQuery();
						// This has been changed to prepared statement and moved
						// to query manager 10-03-08
						// rs = stmt.executeQuery("select current_balance from
						// st_lms_agent_current_balance where account_type = '"+
						// acc_type + "' and agent_id = "+agent_id);
						while (rs.next()) {
							agent_bank_acc_current = rs
									.getDouble("current_balance");
						}

						if (stmt != null) {
							stmt.close();
						}
						if (rs != null) {
							rs.close();
						}

						agent_bank_acc_current = agent_bank_acc_current
								+ amount;
						bo_bank_acc_current = bo_bank_acc_current - amount;
						logger.debug("LH-AGT**" + transaction_date + "**AMT-"
								+ amount + "**ACT_TYPE-" + acc_type
								+ "**AGT_ACC-" + agent_bank_acc_current
								+ "**BO_ACC-" + bo_bank_acc_current);
						query2 = QueryManager.getST2InsertAgentLedger();
						upPstmt = connection.prepareStatement(query2);
						upPstmt.setInt(1, agent_id);
						upPstmt.setString(2, "BO_" + transaction_type);
						upPstmt.setLong(3, transaction_id);
						upPstmt.setTimestamp(4, transaction_date);
						upPstmt.setString(5, -amount + "");
						upPstmt.setString(6, "BO_BANK_ACC");
						upPstmt.setString(7, bo_bank_acc_current + "");
						upPstmt.setString(8, "BO");
						upPstmt.setString(9, RECEIPT_ID);
						upPstmt.execute();

						if (upPstmt != null) {
							upPstmt.close();
						}

						query1 = QueryManager.getST2InsertAgentLedger();
						updatePstmt = connection.prepareStatement(query1);

						updatePstmt.setInt(1, agent_id);
						updatePstmt.setString(2, "BO_" + transaction_type);
						updatePstmt.setLong(3, transaction_id);
						updatePstmt.setTimestamp(4, transaction_date);
						updatePstmt.setString(5, amount + "");
						updatePstmt.setString(6, acc_type);
						updatePstmt.setString(7, agent_bank_acc_current + "");
						updatePstmt.setString(8, "BO");
						updatePstmt.setString(9, RECEIPT_ID);
						updatePstmt.execute();

						if (updatePstmt != null) {
							updatePstmt.close();
						}

						query3 = QueryManager.getST2UpadateAgentCurrentBal();
						updPstmt = connection.prepareStatement(query3);
						updPstmt.setString(1, agent_bank_acc_current + "");
						updPstmt.setString(2, acc_type);
						updPstmt.setInt(3, agent_id);
						updPstmt.execute();

						if (updPstmt != null) {
							updPstmt.close();
						}

					}
					else if (transaction_type.equalsIgnoreCase("OLA_COMMISSION")) {
						stmt2 = connection.prepareStatement(QueryManager
								.getST2BoOlaCommission());
						stmt2.setLong(1, transaction_id);
						rs2 = stmt2.executeQuery();
						// rs2 =
						// stmt2.executeQuery(QueryManager.getST2BoCash()+""+
						// transaction_id + "");
						while (rs2.next()) {
							amount = rs2.getDouble("amount");
							transaction_date = rs2
									.getTimestamp("transaction_date");
							acc_type = "AGNT_BANK_ACC";
						}

						if (stmt2 != null) {
							stmt2.close();
						}
						if (rs2 != null) {
							rs2.close();
						}
						logger.debug("LH-AGT**" + transaction_type
								+ "**TRX_ID-" + transaction_id + "**R_ID-"
								+ RECEIPT_ID);
						stmt = connection.prepareStatement(QueryManager
								.getST6AgtCurrBal());
						stmt.setString(1, acc_type);
						stmt.setInt(2, agent_id);
						rs = stmt.executeQuery();
						// This has been changed to prepared statement and moved
						// to query manager 10-03-08
						// rs = stmt.executeQuery("select current_balance from
						// st_lms_agent_current_balance where account_type = '"+
						// acc_type + "' and agent_id = "+agent_id);
						while (rs.next()) {
							agent_bank_acc_current = rs
									.getDouble("current_balance");
						}

						if (stmt != null) {
							stmt.close();
						}
						if (rs != null) {
							rs.close();
						}

						agent_bank_acc_current = agent_bank_acc_current
								+ amount;
						bo_bank_acc_current = bo_bank_acc_current - amount;
						logger.debug("LH-AGT**" + transaction_date + "**AMT-"
								+ amount + "**ACT_TYPE-" + acc_type
								+ "**AGT_ACC-" + agent_bank_acc_current
								+ "**BO_ACC-" + bo_bank_acc_current);
						query2 = QueryManager.getST2InsertAgentLedger();
						upPstmt = connection.prepareStatement(query2);
						upPstmt.setInt(1, agent_id);
						upPstmt.setString(2, "BO_" + transaction_type);
						upPstmt.setLong(3, transaction_id);
						upPstmt.setTimestamp(4, transaction_date);
						upPstmt.setString(5, -amount + "");
						upPstmt.setString(6, "BO_BANK_ACC");
						upPstmt.setString(7, bo_bank_acc_current + "");
						upPstmt.setString(8, "BO");
						upPstmt.setString(9, RECEIPT_ID);
						upPstmt.execute();

						if (upPstmt != null) {
							upPstmt.close();
						}

						query1 = QueryManager.getST2InsertAgentLedger();
						updatePstmt = connection.prepareStatement(query1);

						updatePstmt.setInt(1, agent_id);
						updatePstmt.setString(2, "BO_" + transaction_type);
						updatePstmt.setLong(3, transaction_id);
						updatePstmt.setTimestamp(4, transaction_date);
						updatePstmt.setString(5, amount + "");
						updatePstmt.setString(6, acc_type);
						updatePstmt.setString(7, agent_bank_acc_current + "");
						updatePstmt.setString(8, "BO");
						updatePstmt.setString(9, RECEIPT_ID);
						updatePstmt.execute();

						if (updatePstmt != null) {
							updatePstmt.close();
						}

						query3 = QueryManager.getST2UpadateAgentCurrentBal();
						updPstmt = connection.prepareStatement(query3);
						updPstmt.setString(1, agent_bank_acc_current + "");
						updPstmt.setString(2, acc_type);
						updPstmt.setInt(3, agent_id);
						updPstmt.execute();

						if (updPstmt != null) {
							updPstmt.close();
						}

					}
					else if (transaction_type.equalsIgnoreCase("pwt")
							|| transaction_type.equalsIgnoreCase("pwt_auto")
							|| transaction_type.equalsIgnoreCase("DG_PWT")
							|| transaction_type.equalsIgnoreCase("DG_PWT_AUTO") || transaction_type.equalsIgnoreCase("OLA_WITHDRAWL")) {
						int result = 0;
						String pwtQuery = "";
						// This has been changed to prepared statement 10-03-08
						if (transaction_type.equalsIgnoreCase("DG_PWT")
								|| transaction_type
										.equalsIgnoreCase("DG_PWT_AUTO")) {
							pwtQuery = QueryManager.getST2BoDrGmPwtPay();
						}else if(transaction_type.equalsIgnoreCase("OLA_WITHDRAWL"))
						{
							pwtQuery = QueryManager.getST2BoOlaWithdrawl();
						}
						else {
							pwtQuery = QueryManager.getST2BoPwtPay();
						}
						stmt2 = connection.prepareStatement(pwtQuery);
						stmt2.setLong(1, transaction_id);
						rs2 = stmt2.executeQuery();
						logger.debug("LH-AGT**" + transaction_type
								+ "**TRX_ID-" + transaction_id + "**R_ID-"
								+ RECEIPT_ID);
						while (rs2.next()) {
							amount = rs2.getDouble("pwt_amount");
							pwt_charges_amount = rs2.getDouble("comm_amount");
							transaction_date = rs2
									.getTimestamp("transaction_date");
							acc_type = "AGNT_PWT_RCV";
							acc_type_charges = "AGNT_PWT_CHARGES_RCV";
							result++;
						}

						if (stmt2 != null) {
							stmt2.close();
						}
						if (rs2 != null) {
							rs2.close();
						}

						if (result != 0) {
							if (acc_type.equals("AGNT_PWT_RCV")) {

								stmt = connection.prepareStatement(QueryManager
										.getST6AgtCurrBal());
								stmt.setString(1, acc_type);
								stmt.setInt(2, agent_id);
								rs = stmt.executeQuery();

								while (rs.next()) {
									agnt_pwt_rcv_current = rs
											.getDouble("current_balance");
								}

								if (stmt != null) {
									stmt.close();
								}
								if (rs != null) {
									rs.close();
								}

								agnt_pwt_rcv_current = agnt_pwt_rcv_current
										+ amount;
								bo_pwt_pay_current = bo_pwt_pay_current
										- amount;
								logger.debug("LH-AGT**" + transaction_date
										+ "**AMT-" + amount + "**ACT_TYPE-"
										+ acc_type + "**AGT_PWT_RCV_ACC-"
										+ agnt_pwt_rcv_current
										+ "**BO_PWT_PAY_ACC-"
										+ bo_pwt_pay_current);

								query2 = QueryManager.getST2InsertAgentLedger();
								upPstmt = connection.prepareStatement(query2);
								upPstmt.setInt(1, agent_id);
								upPstmt.setString(2, "BO_" + transaction_type);
								upPstmt.setLong(3, transaction_id);
								upPstmt.setTimestamp(4, transaction_date);
								upPstmt.setString(5, -amount + "");
								upPstmt.setString(6, "BO_PWT_PAY");
								upPstmt.setString(7, bo_pwt_pay_current + "");
								upPstmt.setString(8, "BO");
								upPstmt.setString(9, RECEIPT_ID);
								upPstmt.execute();

								if (upPstmt != null) {
									upPstmt.close();
								}

								query1 = QueryManager.getST2InsertAgentLedger();
								updatePstmt = connection
										.prepareStatement(query1);

								updatePstmt.setInt(1, agent_id);
								updatePstmt.setString(2, "BO_"
										+ transaction_type);
								updatePstmt.setLong(3, transaction_id);
								updatePstmt.setTimestamp(4, transaction_date);
								updatePstmt.setString(5, amount + "");
								updatePstmt.setString(6, acc_type);
								updatePstmt.setString(7, agnt_pwt_rcv_current
										+ "");
								updatePstmt.setString(8, "BO");
								updatePstmt.setString(9, RECEIPT_ID);
								updatePstmt.execute();

								if (updatePstmt != null) {
									updatePstmt.close();
								}

								query3 = QueryManager
										.getST2UpadateAgentCurrentBal();
								updPstmt = connection.prepareStatement(query3);
								updPstmt
										.setString(1, agnt_pwt_rcv_current + "");
								updPstmt.setString(2, acc_type);
								updPstmt.setInt(3, agent_id);
								updPstmt.execute();

								if (updPstmt != null) {
									updPstmt.close();
								}

							}/*
								 * for charges collection
								 */
							if (acc_type_charges.equals("AGNT_PWT_CHARGES_RCV")) {

								stmt = connection.prepareStatement(QueryManager
										.getST6AgtCurrBal());
								stmt.setString(1, acc_type_charges);
								stmt.setInt(2, agent_id);
								rs = stmt.executeQuery();
								// This has been changed to prepared statement
								// and moved to query manager 10-03-08
								// rs = stmt.executeQuery("select
								// current_balance from
								// st_lms_agent_current_balance where
								// account_type = '"+ acc_type + "' and agent_id
								// = "+agent_id);
								while (rs.next()) {
									agent_pwt_charges_rcv_current = rs
											.getDouble("current_balance");
								}

								if (stmt != null) {
									stmt.close();
								}
								if (rs != null) {
									rs.close();
								}

								agent_pwt_charges_rcv_current = agent_pwt_charges_rcv_current
										+ pwt_charges_amount;
								bo_pwt_charges_current = bo_pwt_charges_current
										- pwt_charges_amount;

								if (pwt_charges_amount != 0.0) {
									query2 = QueryManager
											.getST2InsertAgentLedger();
									upPstmt = connection
											.prepareStatement(query2);
									upPstmt.setInt(1, agent_id);
									upPstmt.setString(2, "BO_"
											+ transaction_type);
									upPstmt.setLong(3, transaction_id);
									upPstmt.setTimestamp(4, transaction_date);
									upPstmt.setString(5, -pwt_charges_amount
											+ "");
									upPstmt.setString(6, "BO_PWT_CHARGES");
									upPstmt.setString(7, bo_pwt_charges_current
											+ "");
									upPstmt.setString(8, "BO");
									upPstmt.setString(9, RECEIPT_ID);
									upPstmt.execute();

									if (upPstmt != null) {
										upPstmt.close();
									}

									query1 = QueryManager
											.getST2InsertAgentLedger();
									updatePstmt = connection
											.prepareStatement(query1);

									updatePstmt.setInt(1, agent_id);
									updatePstmt.setString(2, "BO_"
											+ transaction_type);
									updatePstmt.setLong(3, transaction_id);
									updatePstmt.setTimestamp(4,
											transaction_date);
									updatePstmt.setString(5, pwt_charges_amount
											+ "");
									updatePstmt.setString(6, acc_type_charges);
									updatePstmt.setString(7,
											agent_pwt_charges_rcv_current + "");
									updatePstmt.setString(8, "BO");
									updatePstmt.setString(9, RECEIPT_ID);
									updatePstmt.execute();

									if (updatePstmt != null) {
										updatePstmt.close();
									}

								}

								query3 = QueryManager
										.getST2UpadateAgentCurrentBal();
								updPstmt = connection.prepareStatement(query3);
								updPstmt.setString(1,
										agent_pwt_charges_rcv_current + "");
								updPstmt.setString(2, acc_type_charges);
								updPstmt.setInt(3, agent_id);
								updPstmt.execute();

								if (updPstmt != null) {
									updPstmt.close();
								}

							}
						} else {
							System.out.println("Error in Transaction"
									+ transaction_id + " and Transaction_type "
									+ transaction_type);
						}
					}

				}

				if (stmt1 != null) {
					stmt1.close();
				}
				if (rs1 != null) {
					rs1.close();
				}

				// ///////////////////////////////////////////Agent Retailer
				// Transaction
				// Start////////////////////////////////////////////////////////////////////
				String rcptQryAgt = "select sar.receipt_id,sar.generated_id from st_lms_agent_receipts_trn_mapping sartm , st_lms_agent_receipts sar where transaction_id=? and sartm.receipt_id=sar.receipt_id";

				stmt1 = connection.prepareStatement(QueryManager
						.getST6AgtTransaction());
				stmt1.setTimestamp(1, present_date);
				stmt1.setTimestamp(2, to_Date);
				stmt1.setInt(3, agent_id);
				rs1 = stmt1.executeQuery();
				logger.debug("LH-AGT-RET**PresentDate-" + present_date
						+ "**ToDate-" + toDate_Bean);
				while (rs1.next()) {

					long transaction_id = rs1.getLong("transaction_id");
					String transaction_type = rs1.getString("transaction_type");
					String RECEIPT_ID = "N.A.";

					rcptPS = connection.prepareStatement(rcptQryAgt);
					rcptPS.setLong(1, transaction_id);
					rcptRS = rcptPS.executeQuery();

					while (rcptRS.next()) {
						genNum = rcptRS.getString("generated_id");
						if (genNum != null
								&& !genNum.trim().equals("")
								&& !(genNum.contains("DLA") || genNum
										.contains("DSA"))) {
							RECEIPT_ID = rcptRS.getString("generated_id");
						}

					}

					if (rcptPS != null) {
						rcptPS.close();
					}
					if (rcptRS != null) {
						rcptRS.close();
					}

					double amount = 0;
					double pwt_charges_amount = 0;
					// java.sql.Date transaction_date = null;
					Timestamp transaction_date = null;
					// java.sql.Date TRANS_DATE = null;
					String acc_type = "";
					if (transaction_type.equalsIgnoreCase("Sale")
							|| transaction_type.equalsIgnoreCase("DG_SALE")
							|| transaction_type.equalsIgnoreCase("CS_SALE") || transaction_type.equalsIgnoreCase("OLA_DEPOSIT")) {
						String saleQuery = "";
						if (transaction_type.equalsIgnoreCase("Sale")) {
							saleQuery = QueryManager.getST2AgentSale();
						} else if (transaction_type.equalsIgnoreCase("DG_SALE")) {
							saleQuery = QueryManager.getST2AgentDrGmSale();
						} else if (transaction_type.equalsIgnoreCase("CS_SALE")) {
							saleQuery = QueryManager.getST2AgentCSSale();
						} else if (transaction_type.equalsIgnoreCase("OLA_DEPOSIT"))
						{
							saleQuery = QueryManager.getST2AgentOLADeposit();
						}
						stmt2 = connection.prepareStatement(saleQuery);
						stmt2.setLong(1, transaction_id);
						rs2 = stmt2.executeQuery();
						// This has been moved to query manager 10-03-08 and
						// statement has been changed in to Prepared statement
						// 10-03-08
						// rs2 =
						// stmt2.executeQuery(QueryManager.getST2AgentSale()+""+
						// transaction_id + "");
						while (rs2.next()) {
							amount = rs2.getDouble("net_amt");
							transaction_date = rs2
									.getTimestamp("transaction_date");
							acc_type = "" + rs2.getInt("retailer_org_id");
							orgName = rs2.getString("name");
						}

						if (stmt2 != null) {
							stmt2.close();
						}
						if (rs2 != null) {
							rs2.close();
						}

						stmt = connection.prepareStatement(QueryManager
								.getST6AgtCurrBal());
						stmt.setString(1, acc_type);
						stmt.setInt(2, agent_id);
						rs = stmt.executeQuery();
						logger.debug("LH-AGT-RET**" + transaction_type
								+ "**TRX_ID-" + transaction_id + "**R_ID-"
								+ RECEIPT_ID);
						while (rs.next()) {
							retailer_org_id_current = rs
									.getDouble("current_balance");

							retailerPresent++;
						}

						if (stmt != null) {
							stmt.close();
						}
						if (rs != null) {
							rs.close();
						}

						if (retailerPresent == 0) {

							stmt = connection.prepareStatement(QueryManager
									.getST6InsertAgentCurrBal());
							stmt.setString(1, acc_type);
							stmt.setDouble(2, 0.0);
							stmt.setInt(3, agent_id);
							stmt.execute();
							retailer_org_id_current = 0.0;

							if (stmt != null) {
								stmt.close();
							}

						}
						retailerPresent = 0;

						retailer_org_id_current = retailer_org_id_current
								- amount;

						agnt_sale_acc_current = agnt_sale_acc_current + amount;
						logger.debug("LH-AGT-RET**" + transaction_date
								+ "**AMT-" + amount + "**ACT_TYPE-" + acc_type
								+ "**RET_ACC-" + retailer_org_id_current
								+ "**AGT_ACC-" + agnt_sale_acc_current);
						query1 = QueryManager.getST2InsertAgentLedger();
						updatePstmt = connection.prepareStatement(query1);

						updatePstmt.setInt(1, agent_id);
						updatePstmt.setString(2, transaction_type);
						updatePstmt.setLong(3, transaction_id);
						updatePstmt.setTimestamp(4, transaction_date);
						updatePstmt.setString(5, -amount + "");
						updatePstmt.setString(6, acc_type);
						updatePstmt.setString(7, retailer_org_id_current + "");
						updatePstmt.setString(8, orgName);
						updatePstmt.setString(9, RECEIPT_ID);
						updatePstmt.execute();

						if (updatePstmt != null) {
							updatePstmt.close();
						}

						query2 = QueryManager.getST2InsertAgentLedger();
						upPstmt = connection.prepareStatement(query2);
						upPstmt.setInt(1, agent_id);
						upPstmt.setString(2, transaction_type);
						upPstmt.setLong(3, transaction_id);
						upPstmt.setTimestamp(4, transaction_date);
						upPstmt.setString(5, amount + "");
						upPstmt.setString(6, "AGNT_SALE_ACC");
						upPstmt.setString(7, agnt_sale_acc_current + "");
						upPstmt.setString(8, orgName);
						upPstmt.setString(9, RECEIPT_ID);
						upPstmt.execute();

						if (upPstmt != null) {
							upPstmt.close();
						}

						query3 = QueryManager.getST2UpadateAgentCurrentBal();
						updPstmt = connection.prepareStatement(query3);
						updPstmt.setString(1, retailer_org_id_current + "");
						updPstmt.setString(2, acc_type);
						updPstmt.setInt(3, agent_id);
						updPstmt.execute();

						if (updPstmt != null) {
							updPstmt.close();
						}

					}

					else if (transaction_type.equalsIgnoreCase("cheque")) {
						stmt2 = connection.prepareStatement(QueryManager
								.getST2AgentChq());
						stmt2.setLong(1, transaction_id);
						rs2 = stmt2.executeQuery();

						// This has been changed into prepared Statement
						// 10-03-08

						// rs2 =
						// stmt2.executeQuery(QueryManager.getST2AgentChq()+""+
						// transaction_id + "");

						while (rs2.next()) {
							amount = rs2.getDouble("cheque_amt");
							transaction_date = rs2
									.getTimestamp("transaction_date");
							// TRANS_DATE= new
							// java.sql.Date(transaction_date.getTime());
							acc_type = "" + rs2.getInt("retailer_org_id");
							orgName = rs2.getString("name");
						}

						if (stmt2 != null) {
							stmt2.close();
						}
						if (rs2 != null) {
							rs2.close();
						}

						stmt = connection.prepareStatement(QueryManager
								.getST6AgtCurrBal());
						stmt.setString(1, acc_type);
						stmt.setInt(2, agent_id);
						rs = stmt.executeQuery();

						while (rs.next()) {
							retailer_org_id_current = rs
									.getDouble("current_balance");
							retailerPresent++;

						}

						if (stmt != null) {
							stmt.close();
						}
						if (rs != null) {
							rs.close();
						}

						if (retailerPresent == 0) {

							stmt = connection.prepareStatement(QueryManager
									.getST6InsertAgentCurrBal());
							stmt.setString(1, acc_type);
							stmt.setDouble(2, 0.0);
							stmt.setInt(3, agent_id);
							stmt.execute();
							retailer_org_id_current = 0.0;

							if (stmt != null) {
								stmt.close();
							}

						}
						logger.debug("LH-AGT-RET**" + transaction_type
								+ "**TRX_ID-" + transaction_id + "**R_ID-"
								+ RECEIPT_ID);
						retailerPresent = 0;
						retailer_org_id_current = retailer_org_id_current
								+ amount;

						agent_bank_acc_current = agent_bank_acc_current
								- amount;
						logger.debug("LH-AGT-RET**" + transaction_date
								+ "**AMT-" + amount + "**ACT_TYPE-" + acc_type
								+ "**RET_ACC-" + retailer_org_id_current
								+ "**AGT_ACC-" + agent_bank_acc_current);
						query2 = QueryManager.getST2InsertAgentLedger();
						upPstmt = connection.prepareStatement(query2);
						upPstmt.setInt(1, agent_id);
						upPstmt.setString(2, transaction_type);
						upPstmt.setLong(3, transaction_id);
						upPstmt.setTimestamp(4, transaction_date);
						upPstmt.setString(5, -amount + "");
						upPstmt.setString(6, "AGNT_BANK_ACC");
						upPstmt.setString(7, agent_bank_acc_current + "");
						upPstmt.setString(8, orgName);
						upPstmt.setString(9, RECEIPT_ID);
						upPstmt.execute();

						if (upPstmt != null) {
							upPstmt.close();
						}

						query1 = QueryManager.getST2InsertAgentLedger();
						updatePstmt = connection.prepareStatement(query1);

						updatePstmt.setInt(1, agent_id);
						updatePstmt.setString(2, transaction_type);
						updatePstmt.setLong(3, transaction_id);
						updatePstmt.setTimestamp(4, transaction_date);
						updatePstmt.setString(5, amount + "");
						updatePstmt.setString(6, acc_type);
						updatePstmt.setString(7, retailer_org_id_current + "");
						updatePstmt.setString(8, orgName);
						updatePstmt.setString(9, RECEIPT_ID);
						updatePstmt.execute();

						if (updatePstmt != null) {
							updatePstmt.close();
						}

						query3 = QueryManager.getST2UpadateAgentCurrentBal();
						updPstmt = connection.prepareStatement(query3);
						updPstmt.setString(1, retailer_org_id_current + "");
						updPstmt.setString(2, acc_type);
						updPstmt.setInt(3, agent_id);
						updPstmt.execute();

						if (updPstmt != null) {
							updPstmt.close();
						}

					}

					else if (transaction_type.equalsIgnoreCase("chq_bounce")) {

						stmt2 = connection.prepareStatement(QueryManager
								.getST2AgentChqBounce());
						stmt2.setLong(1, transaction_id);
						rs2 = stmt2.executeQuery();
						// This has been changed to prepared statement 10-03-08
						// rs2 =
						// stmt2.executeQuery(QueryManager.getST2AgentChqBounce()+""+
						// transaction_id + "");
						while (rs2.next()) {
							amount = rs2.getDouble("cheque_amt");
							transaction_date = rs2
									.getTimestamp("transaction_date");
							// TRANS_DATE= new
							// java.sql.Date(transaction_date.getTime());
							acc_type = "" + rs2.getInt("retailer_org_id");
							orgName = rs2.getString("name");
						}

						if (stmt2 != null) {
							stmt2.close();
						}
						if (rs2 != null) {
							rs2.close();
						}

						stmt = connection.prepareStatement(QueryManager
								.getST6AgtCurrBal());
						stmt.setString(1, acc_type);
						stmt.setInt(2, agent_id);
						rs = stmt.executeQuery();
						// This has been changed into Prepared statement and
						// moved to Query manager 10-03-08
						// rs = stmt.executeQuery("select current_balance from
						// st_lms_agent_current_balance where account_type = '"+
						// acc_type + "' and agent_id = "+agent_id);

						while (rs.next()) {
							retailer_org_id_current = rs
									.getDouble("current_balance");
							retailerPresent++;
						}

						if (stmt != null) {
							stmt.close();
						}
						if (rs != null) {
							rs.close();
						}

						if (retailerPresent == 0) {
							stmt = connection.prepareStatement(QueryManager
									.getST6InsertAgentCurrBal());
							stmt.setString(1, acc_type);
							stmt.setDouble(2, 0.0);
							stmt.setInt(3, agent_id);
							stmt.execute();
							retailer_org_id_current = 0.0;

							if (stmt != null) {
								stmt.close();
							}

							// This has been moved into query manager and
							// changed to prepared statement 10-03-08
							// stmt.executeUpdate("insert into
							// st_lms_agent_current_balance
							// (account_type,current_balance,agent_id) values
							// ('"+acc_type+"',0.0,"+agent_id+")");
						}
						retailerPresent = 0;

						logger.debug("LH-AGT-RET**" + transaction_type
								+ "**TRX_ID-" + transaction_id + "**R_ID-"
								+ RECEIPT_ID);
						retailer_org_id_current = retailer_org_id_current
								- amount;
						agent_bank_acc_current = agent_bank_acc_current
								+ amount;
						logger.debug("LH-AGT-RET**" + transaction_date
								+ "**AMT-" + amount + "**ACT_TYPE-" + acc_type
								+ "**RET_ACC-" + retailer_org_id_current
								+ "**AGT_ACC-" + agent_bank_acc_current);
						query1 = QueryManager.getST2InsertAgentLedger();
						updatePstmt = connection.prepareStatement(query1);

						updatePstmt.setInt(1, agent_id);
						updatePstmt.setString(2, transaction_type);
						updatePstmt.setLong(3, transaction_id);
						updatePstmt.setTimestamp(4, transaction_date);
						updatePstmt.setString(5, -amount + "");
						updatePstmt.setString(6, acc_type);
						updatePstmt.setString(7, retailer_org_id_current + "");
						updatePstmt.setString(8, orgName);
						updatePstmt.setString(9, RECEIPT_ID);
						updatePstmt.execute();

						if (updatePstmt != null) {
							updatePstmt.close();
						}

						query2 = QueryManager.getST2InsertAgentLedger();
						upPstmt = connection.prepareStatement(query2);
						upPstmt.setInt(1, agent_id);
						upPstmt.setString(2, transaction_type);
						upPstmt.setLong(3, transaction_id);
						upPstmt.setTimestamp(4, transaction_date);
						upPstmt.setString(5, amount + "");
						upPstmt.setString(6, "AGNT_BANK_ACC");
						upPstmt.setString(7, agent_bank_acc_current + "");
						upPstmt.setString(8, orgName);
						upPstmt.setString(9, RECEIPT_ID);
						upPstmt.execute();

						if (upPstmt != null) {
							upPstmt.close();
						}

						query3 = QueryManager.getST2UpadateAgentCurrentBal();
						updPstmt = connection.prepareStatement(query3);
						updPstmt.setString(1, retailer_org_id_current + "");
						updPstmt.setString(2, acc_type);
						updPstmt.setInt(3, agent_id);
						updPstmt.execute();

						if (updPstmt != null) {
							updPstmt.close();
						}

					}
					/*
					 * on abshishekhs recomendation
					 */

					else if (transaction_type.equalsIgnoreCase("vat")) {

						stmt2 = connection.prepareStatement(QueryManager
								.getST6AgentVATPayable());
						stmt2.setLong(1, transaction_id);
						rs2 = stmt2.executeQuery();
						// This has been replaced by the obove prepared
						// statements statements 07-03-08
						// rs2 =
						// stmt2.executeQuery(QueryManager.getST2BoGovtComm()+""+
						// transaction_id + "");

						while (rs2.next()) {
							amount = rs2.getDouble("amount");
							// TRANSACTION_DATE =
							// rs2.getDate("transaction_date");
							transaction_date = rs2
									.getTimestamp("transaction_date");
							acc_type = "AGENT_VAT_PAY";
						}

						if (stmt2 != null) {
							stmt2.close();
						}
						if (rs2 != null) {
							rs2.close();
						}

						agent_vat_pay_current = agent_vat_pay_current - amount;
						agent_bank_acc_current = agent_bank_acc_current
								+ amount;

						query1 = QueryManager.getST2InsertAgentLedger();
						updatePstmt = connection.prepareStatement(query1);
						updatePstmt.setInt(1, agent_id);
						updatePstmt.setString(2, transaction_type);
						updatePstmt.setLong(3, transaction_id);
						// updatePstmt.setDate(3, TRANSACTION_DATE);
						updatePstmt.setTimestamp(4, transaction_date);
						updatePstmt.setString(5, -amount + "");
						updatePstmt.setString(6, acc_type);
						updatePstmt.setString(7, agent_vat_pay_current + "");
						updatePstmt.setString(8, "GOVERNMENT");
						updatePstmt.setString(9, RECEIPT_ID);
						updatePstmt.execute();

						if (updatePstmt != null) {
							updatePstmt.close();
						}

						query2 = QueryManager.getST2InsertAgentLedger();
						upPstmt = connection.prepareStatement(query2);
						upPstmt.setInt(1, agent_id);
						upPstmt.setString(2, transaction_type);
						upPstmt.setLong(3, transaction_id);
						// upPstmt.setDate(3, TRANSACTION_DATE);
						upPstmt.setTimestamp(4, transaction_date);
						upPstmt.setString(5, amount + "");
						upPstmt.setString(6, "AGNT_BANK_ACC");
						upPstmt.setString(7, agent_bank_acc_current + "");
						upPstmt.setString(8, "GOVERNMENT");
						upPstmt.setString(9, RECEIPT_ID);
						upPstmt.execute();

						if (upPstmt != null) {
							upPstmt.close();
						}

					}

					/*
					 */
					else if (transaction_type.equalsIgnoreCase("DR_NOTE_CASH")
							|| transaction_type.equalsIgnoreCase("DR_NOTE")) {

						stmt2 = connection.prepareStatement(QueryManager
								.getST6AgentDebitNote());
						stmt2.setLong(1, transaction_id);
						rs2 = stmt2.executeQuery();
						// This has been changed to prepared statement 10-03-08
						// rs2 =
						// stmt2.executeQuery(QueryManager.getST2AgentChqBounce()+""+
						// transaction_id + "");
						while (rs2.next()) {
							amount = rs2.getDouble("amount");
							transaction_date = rs2
									.getTimestamp("transaction_date");
							// TRANS_DATE= new
							// java.sql.Date(transaction_date.getTime());
							acc_type = "" + rs2.getInt("retailer_org_id");
							orgName = rs2.getString("name");
						}

						if (stmt2 != null) {
							stmt2.close();
						}
						if (rs2 != null) {
							rs2.close();
						}

						stmt = connection.prepareStatement(QueryManager
								.getST6AgtCurrBal());
						stmt.setString(1, acc_type);
						stmt.setInt(2, agent_id);
						rs = stmt.executeQuery();

						while (rs.next()) {
							retailer_org_id_current = rs
									.getDouble("current_balance");
							retailerPresent++;
						}

						if (stmt2 != null) {
							stmt2.close();
						}
						if (rs2 != null) {
							rs2.close();
						}

						if (retailerPresent == 0) {
							stmt = connection.prepareStatement(QueryManager
									.getST6InsertAgentCurrBal());
							stmt.setString(1, acc_type);
							stmt.setDouble(2, 0.0);
							stmt.setInt(3, agent_id);
							stmt.execute();
							retailer_org_id_current = 0.0;

							if (stmt != null) {
								stmt.close();
							}

							// This has been moved into query manager and
							// changed to prepared statement 10-03-08
							// stmt.executeUpdate("insert into
							// st_lms_agent_current_balance
							// (account_type,current_balance,agent_id) values
							// ('"+acc_type+"',0.0,"+agent_id+")");
						}
						retailerPresent = 0;
						retailer_org_id_current = retailer_org_id_current
								- amount;
						agent_bank_acc_current = agent_bank_acc_current
								+ amount;

						logger.debug("LH-AGT-RET**" + transaction_type
								+ "**TRX_ID-" + transaction_id + "**R_ID-"
								+ RECEIPT_ID);
						logger.debug("LH-AGT-RET**" + transaction_date
								+ "**AMT-" + amount + "**ACT_TYPE-" + acc_type
								+ "**RET_ACC-" + retailer_org_id_current
								+ "**AGT_ACC-" + agent_bank_acc_current);
						query1 = QueryManager.getST2InsertAgentLedger();
						updatePstmt = connection.prepareStatement(query1);

						updatePstmt.setInt(1, agent_id);
						updatePstmt.setString(2, transaction_type);
						updatePstmt.setLong(3, transaction_id);
						updatePstmt.setTimestamp(4, transaction_date);
						updatePstmt.setString(5, -amount + "");
						updatePstmt.setString(6, acc_type);
						updatePstmt.setString(7, retailer_org_id_current + "");
						updatePstmt.setString(8, orgName);
						updatePstmt.setString(9, RECEIPT_ID);
						updatePstmt.execute();

						if (updatePstmt != null) {
							updatePstmt.close();
						}

						query2 = QueryManager.getST2InsertAgentLedger();
						upPstmt = connection.prepareStatement(query2);
						upPstmt.setInt(1, agent_id);
						upPstmt.setString(2, transaction_type);
						upPstmt.setLong(3, transaction_id);
						upPstmt.setTimestamp(4, transaction_date);
						upPstmt.setString(5, amount + "");
						upPstmt.setString(6, "AGNT_BANK_ACC");
						upPstmt.setString(7, agent_bank_acc_current + "");
						upPstmt.setString(8, orgName);
						upPstmt.setString(9, RECEIPT_ID);
						upPstmt.execute();

						if (upPstmt != null) {
							upPstmt.close();
						}

						query3 = QueryManager.getST2UpadateAgentCurrentBal();
						updPstmt = connection.prepareStatement(query3);
						updPstmt.setString(1, retailer_org_id_current + "");
						updPstmt.setString(2, acc_type);
						updPstmt.setInt(3, agent_id);
						updPstmt.execute();

						if (updPstmt != null) {
							updPstmt.close();
						}

					}
					// code for credit note entry in ledger @amit starts...

					else if (transaction_type.equalsIgnoreCase("CR_NOTE_CASH")
							|| transaction_type.equalsIgnoreCase("CR_NOTE")) {
						stmt2 = connection.prepareStatement(QueryManager
								.getST6AgentCreditNote());
						stmt2.setLong(1, transaction_id);
						rs2 = stmt2.executeQuery();
						// This has been changed to prepared statement 10-03-08
						// rs2 =
						// stmt2.executeQuery(QueryManager.getST2AgentChqBounce()+""+
						// transaction_id + "");
						while (rs2.next()) {
							amount = rs2.getDouble("amount");
							transaction_date = rs2
									.getTimestamp("transaction_date");
							// TRANS_DATE= new
							// java.sql.Date(transaction_date.getTime());
							acc_type = "" + rs2.getInt("retailer_org_id");
							orgName = rs2.getString("name");
						}

						if (stmt2 != null) {
							stmt2.close();
						}
						if (rs2 != null) {
							rs2.close();
						}

						stmt = connection.prepareStatement(QueryManager
								.getST6AgtCurrBal());
						stmt.setString(1, acc_type);
						stmt.setInt(2, agent_id);
						rs = stmt.executeQuery();

						while (rs.next()) {
							retailer_org_id_current = rs
									.getDouble("current_balance");
							retailerPresent++;
						}

						if (stmt2 != null) {
							stmt2.close();
						}
						if (rs2 != null) {
							rs2.close();
						}

						if (retailerPresent == 0) {
							stmt = connection.prepareStatement(QueryManager
									.getST6InsertAgentCurrBal());
							stmt.setString(1, acc_type);
							stmt.setDouble(2, 0.0);
							stmt.setInt(3, agent_id);
							stmt.execute();
							retailer_org_id_current = 0.0;

							if (stmt != null) {
								stmt.close();
							}

							// This has been moved into query manager and
							// changed to prepared statement 10-03-08
							// stmt.executeUpdate("insert into
							// st_lms_agent_current_balance
							// (account_type,current_balance,agent_id) values
							// ('"+acc_type+"',0.0,"+agent_id+")");
						}
						retailerPresent = 0;
						retailer_org_id_current = retailer_org_id_current
								+ amount;
						agent_bank_acc_current = agent_bank_acc_current
								- amount;
						logger.debug("LH-AGT-RET**" + transaction_type
								+ "**TRX_ID-" + transaction_id + "**R_ID-"
								+ RECEIPT_ID);
						logger.debug("LH-AGT-RET**" + transaction_date
								+ "**AMT-" + amount + "**ACT_TYPE-" + acc_type
								+ "**RET_ACC-" + retailer_org_id_current
								+ "**AGT_ACC-" + agent_bank_acc_current);
						query1 = QueryManager.getST2InsertAgentLedger();
						updatePstmt = connection.prepareStatement(query1);

						updatePstmt.setInt(1, agent_id);
						updatePstmt.setString(2, transaction_type);
						updatePstmt.setLong(3, transaction_id);
						updatePstmt.setTimestamp(4, transaction_date);
						updatePstmt.setString(5, amount + "");
						updatePstmt.setString(6, acc_type);
						updatePstmt.setString(7, retailer_org_id_current + "");
						updatePstmt.setString(8, orgName);
						updatePstmt.setString(9, RECEIPT_ID);
						updatePstmt.execute();

						if (updatePstmt != null) {
							updatePstmt.close();
						}

						query2 = QueryManager.getST2InsertAgentLedger();
						upPstmt = connection.prepareStatement(query2);
						upPstmt.setInt(1, agent_id);
						upPstmt.setString(2, transaction_type);
						upPstmt.setLong(3, transaction_id);
						upPstmt.setTimestamp(4, transaction_date);
						upPstmt.setString(5, -amount + "");
						upPstmt.setString(6, "AGNT_BANK_ACC");
						upPstmt.setString(7, agent_bank_acc_current + "");
						upPstmt.setString(8, orgName);
						upPstmt.setString(9, RECEIPT_ID);
						upPstmt.execute();

						if (upPstmt != null) {
							upPstmt.close();
						}

						query3 = QueryManager.getST2UpadateAgentCurrentBal();
						updPstmt = connection.prepareStatement(query3);
						updPstmt.setString(1, retailer_org_id_current + "");
						updPstmt.setString(2, acc_type);
						updPstmt.setInt(3, agent_id);
						updPstmt.execute();

						if (updPstmt != null) {
							updPstmt.close();
						}
					}

					// code for credit note entry in ledger @amit ends...

					else if (transaction_type.equalsIgnoreCase("sale_ret")
							|| transaction_type
									.equalsIgnoreCase("DG_REFUND_FAILED")
							|| transaction_type
									.equalsIgnoreCase("DG_REFUND_CANCEL")
							|| transaction_type
									.equalsIgnoreCase("CS_CANCEL_SERVER")
							|| transaction_type
									.equalsIgnoreCase("CS_CANCEL_RET")
							|| transaction_type.equalsIgnoreCase("OLA_DEPOSIT_REFUND")) {
						String saleRetQuery = "";
						if (transaction_type.equalsIgnoreCase("sale_ret")) {
							saleRetQuery = QueryManager.getST2AgentSaleRet();
						} else if (transaction_type
								.equalsIgnoreCase("DG_REFUND_FAILED")
								|| transaction_type
										.equalsIgnoreCase("DG_REFUND_CANCEL")) {
							saleRetQuery = QueryManager.getST2AgtDrwGmRefnd();

						} else if (transaction_type
								.equalsIgnoreCase("CS_CANCEL_SERVER")
								|| transaction_type
										.equalsIgnoreCase("CS_CANCEL_RET")) {
							saleRetQuery = QueryManager.getST2AgtCSRefnd();

						} else if (transaction_type.equalsIgnoreCase("OLA_DEPOSIT_REFUND"))
						{
							saleRetQuery = QueryManager.getST2AgtOLARefnd();
						}
						stmt2 = connection.prepareStatement(saleRetQuery);
						stmt2.setLong(1, transaction_id);
						rs2 = stmt2.executeQuery();
						// This has been changed to prepared statement 10-03-08
						// Wrong query was being used here instead of sale
						// return query for sale was being used here
						// rs2 =
						// stmt2.executeQuery(QueryManager.getST2AgentSale()+""+
						// transaction_id + "");
						while (rs2.next()) {
							amount = rs2.getDouble("net_amt");
							transaction_date = rs2
									.getTimestamp("transaction_date");
							acc_type = "" + rs2.getInt("retailer_org_id");
							orgName = rs2.getString("name");
						}

						if (stmt2 != null) {
							stmt2.close();
						}
						if (rs2 != null) {
							rs2.close();
						}

						stmt = connection.prepareStatement(QueryManager
								.getST6AgtCurrBal());
						stmt.setString(1, acc_type);
						stmt.setInt(2, agent_id);
						rs = stmt.executeQuery();

						while (rs.next()) {
							retailer_org_id_current = rs
									.getDouble("current_balance");

							retailerPresent++;
						}

						if (stmt != null) {
							stmt.close();
						}
						if (rs != null) {
							rs.close();
						}

						if (retailerPresent == 0) {
							stmt = connection.prepareStatement(QueryManager
									.getST6InsertAgentCurrBal());
							stmt.setString(1, acc_type);
							stmt.setDouble(2, 0.0);
							stmt.setInt(3, agent_id);
							stmt.execute();
							retailer_org_id_current = 0.0;

							if (stmt != null) {
								stmt.close();
							}
						}
						retailerPresent = 0;
						retailer_org_id_current = retailer_org_id_current
								+ amount;
						agnt_sale_ret_current = agnt_sale_ret_current - amount;
						logger.debug("LH-AGT-RET**" + transaction_type
								+ "**TRX_ID-" + transaction_id + "**R_ID-"
								+ RECEIPT_ID);
						logger.debug("LH-AGT-RET**" + transaction_date
								+ "**AMT-" + amount + "**ACT_TYPE-" + acc_type
								+ "**RET_ACC-" + retailer_org_id_current
								+ "**AGT_ACC-" + agnt_sale_ret_current);
						query2 = QueryManager.getST2InsertAgentLedger();
						upPstmt = connection.prepareStatement(query2);
						upPstmt.setInt(1, agent_id);
						upPstmt.setString(2, transaction_type);
						upPstmt.setLong(3, transaction_id);
						upPstmt.setTimestamp(4, transaction_date);
						upPstmt.setString(5, -amount + "");
						upPstmt.setString(6, "AGNT_SALE_RET_ACC");
						upPstmt.setString(7, agnt_sale_ret_current + "");
						upPstmt.setString(8, orgName);
						upPstmt.setString(9, RECEIPT_ID);
						upPstmt.execute();

						if (upPstmt != null) {
							upPstmt.close();
						}

						query1 = QueryManager.getST2InsertAgentLedger();
						updatePstmt = connection.prepareStatement(query1);

						updatePstmt.setInt(1, agent_id);
						updatePstmt.setString(2, transaction_type);
						updatePstmt.setLong(3, transaction_id);
						updatePstmt.setTimestamp(4, transaction_date);
						updatePstmt.setString(5, amount + "");
						updatePstmt.setString(6, acc_type);
						updatePstmt.setString(7, retailer_org_id_current + "");
						updatePstmt.setString(8, orgName);
						updatePstmt.setString(9, RECEIPT_ID);
						updatePstmt.execute();

						if (updatePstmt != null) {
							updatePstmt.close();
						}

						query3 = QueryManager.getST2UpadateAgentCurrentBal();
						updPstmt = connection.prepareStatement(query3);
						updPstmt.setString(1, retailer_org_id_current + "");
						updPstmt.setString(2, acc_type);
						updPstmt.setInt(3, agent_id);
						updPstmt.execute();

						if (updPstmt != null) {
							updPstmt.close();
						}

					}

					else if (transaction_type.equalsIgnoreCase("pwt")
							|| transaction_type.equalsIgnoreCase("pwt_auto")
							|| transaction_type.equalsIgnoreCase("DG_PWT")
							|| transaction_type.equalsIgnoreCase("DG_PWT_AUTO")
							|| transaction_type.equalsIgnoreCase("OLA_WITHDRAWL")) {
						int result = 0;
						String pwtQuery = "";
						if (transaction_type.equalsIgnoreCase("DG_PWT")
								|| transaction_type
										.equalsIgnoreCase("DG_PWT_AUTO")) {
							pwtQuery = QueryManager.getST2AgentDrGmPwtPay();
						} else if(transaction_type.equalsIgnoreCase("OLA_WITHDRAWL"))
						{
							pwtQuery = QueryManager.getST2AgentOLAWithdrawal();
						}
						else {
							pwtQuery = QueryManager.getST2AgentPwtPay();
						}
						stmt2 = connection.prepareStatement(pwtQuery);
						stmt2.setLong(1, transaction_id);

						rs2 = stmt2.executeQuery();

						while (rs2.next()) {
							amount = rs2.getDouble("pwt_amount");
							pwt_charges_amount = rs2.getDouble("comm_amount");
							transaction_date = rs2
									.getTimestamp("transaction_date");
							acc_type = "" + rs2.getInt("retailer_org_id");
							orgName = rs2.getString("name");

							result++;
						}

						if (stmt2 != null) {
							stmt2.close();
						}
						if (rs2 != null) {
							rs2.close();
						}

						if (result != 0) {
							stmt = connection.prepareStatement(QueryManager
									.getST6AgtCurrBal());
							stmt.setString(1, acc_type);
							stmt.setInt(2, agent_id);
							rs = stmt.executeQuery();
							// This has been changed into Prepared statement and
							// moved to Query manager 10-03-08
							// rs = stmt.executeQuery("select current_balance
							// from st_lms_agent_current_balance where
							// account_type = '"+ acc_type + "' and agent_id =
							// "+agent_id);
							while (rs.next()) {
								retailer_org_id_current = rs
										.getDouble("current_balance");
								retailerPresent++;
							}

							if (stmt != null) {
								stmt.close();
							}
							if (rs != null) {
								rs.close();
							}

							if (retailerPresent == 0) {
								stmt = connection.prepareStatement(QueryManager
										.getST6InsertAgentCurrBal());
								stmt.setString(1, acc_type);
								stmt.setDouble(2, 0.0);
								stmt.setInt(3, agent_id);
								stmt.execute();
								retailer_org_id_current = 0.0;

								if (stmt != null) {
									stmt.close();
								}

							}
							retailerPresent = 0;
							try {
								retailer_org_id_current = getDouble(retailer_org_id_current)
										+ amount;
								agent_pwt_pay_current = getDouble(agent_pwt_pay_current)
										- amount;

							} catch (NumberFormatException e) {
							}
							logger.debug("LH-AGT-RET**" + transaction_type
									+ "**TRX_ID-" + transaction_id + "**R_ID-"
									+ RECEIPT_ID);
							logger.debug("LH-AGT-RET**" + transaction_date
									+ "**AMT-" + amount + "**ACT_TYPE-"
									+ acc_type + "**RET_ACC-"
									+ retailer_org_id_current + "**AGT_ACC-"
									+ agent_pwt_pay_current);
							query2 = QueryManager.getST2InsertAgentLedger();
							upPstmt = connection.prepareStatement(query2);
							upPstmt.setInt(1, agent_id);
							upPstmt.setString(2, transaction_type);
							upPstmt.setLong(3, transaction_id);
							upPstmt.setTimestamp(4, transaction_date);
							upPstmt.setString(5, -amount + "");
							upPstmt.setString(6, "AGNT_PWT_PAY");
							upPstmt.setString(7, agent_pwt_pay_current + "");
							upPstmt.setString(8, orgName);
							upPstmt.setString(9, RECEIPT_ID);
							upPstmt.execute();

							if (upPstmt != null) {
								upPstmt.close();
							}

							query1 = QueryManager.getST2InsertAgentLedger();
							updatePstmt = connection.prepareStatement(query1);

							updatePstmt.setInt(1, agent_id);
							updatePstmt.setString(2, transaction_type);
							updatePstmt.setLong(3, transaction_id);
							updatePstmt.setTimestamp(4, transaction_date);
							updatePstmt.setString(5, amount + "");
							updatePstmt.setString(6, acc_type);
							updatePstmt.setString(7, retailer_org_id_current
									+ "");
							updatePstmt.setString(8, orgName);
							updatePstmt.setString(9, RECEIPT_ID);
							updatePstmt.execute();

							if (updatePstmt != null) {
								updatePstmt.close();
							}

							query3 = QueryManager
									.getST2UpadateAgentCurrentBal();
							updPstmt = connection.prepareStatement(query3);
							updPstmt.setString(1, retailer_org_id_current + "");
							updPstmt.setString(2, acc_type);
							updPstmt.setInt(3, agent_id);
							updPstmt.execute();

							if (updPstmt != null) {
								updPstmt.close();
							}

							/*
							 * moved below for collection charges query3 =
							 * QueryManager.getST2UpadateAgentCurrentBal();
							 * updPstmt=connection.prepareStatement(query3);
							 * updPstmt.setString(1,
							 * retailer_org_id_current+"");
							 * updPstmt.setString(2, acc_type);
							 * updPstmt.setInt(3, agent_id); updPstmt.execute();
							 */
							/*
							 * 
							 * for collection charges
							 */
							try {
								retailer_org_id_current = getDouble(retailer_org_id_current)
										+ pwt_charges_amount;
								agent_pwt_charges_current = getDouble(agent_pwt_charges_current)
										- pwt_charges_amount;

							} catch (NumberFormatException e) {
							}

							if (pwt_charges_amount != 0.0) {
								query2 = QueryManager.getST2InsertAgentLedger();
								upPstmt = connection.prepareStatement(query2);
								upPstmt.setInt(1, agent_id);
								upPstmt.setString(2, transaction_type);
								upPstmt.setLong(3, transaction_id);
								upPstmt.setTimestamp(4, transaction_date);
								upPstmt.setString(5, -pwt_charges_amount + "");
								upPstmt.setString(6, "AGENT_PWT_CHARGES");
								upPstmt.setString(7, agent_pwt_charges_current
										+ "");
								upPstmt.setString(8, orgName);
								upPstmt.setString(9, RECEIPT_ID);
								upPstmt.execute();

								if (upPstmt != null) {
									upPstmt.close();
								}

								query1 = QueryManager.getST2InsertAgentLedger();
								updatePstmt = connection
										.prepareStatement(query1);

								updatePstmt.setInt(1, agent_id);
								updatePstmt.setString(2, transaction_type);
								updatePstmt.setLong(3, transaction_id);
								updatePstmt.setTimestamp(4, transaction_date);
								updatePstmt.setString(5, pwt_charges_amount
										+ "");
								updatePstmt.setString(6, acc_type + "#");
								updatePstmt.setString(7,
										retailer_org_id_current + "");
								updatePstmt.setString(8, orgName);
								updatePstmt.setString(9, RECEIPT_ID);
								updatePstmt.execute();

								if (updatePstmt != null) {
									updatePstmt.close();
								}

							}
							query3 = QueryManager
									.getST2UpadateAgentCurrentBal();
							updPstmt = connection.prepareStatement(query3);
							updPstmt.setString(1, retailer_org_id_current + "");
							updPstmt.setString(2, acc_type);
							updPstmt.setInt(3, agent_id);
							updPstmt.execute();

							if (updPstmt != null) {
								updPstmt.close();
							}

						} else {
							System.out.println("Error in Transaction at "
									+ transaction_id + " and Transaction_type"
									+ transaction_type);
						}
					} else if (transaction_type.equalsIgnoreCase("cash")) {
						stmt2 = connection.prepareStatement(QueryManager
								.getST2AgentCash());
						stmt2.setLong(1, transaction_id);
						rs2 = stmt2.executeQuery();
						// This has been changed to prepared statement 10-03-08
						// rs2 =
						// stmt2.executeQuery(QueryManager.getST2AgentCash()+""+
						// transaction_id + "");
						while (rs2.next()) {
							amount = rs2.getDouble("amount");
							transaction_date = rs2
									.getTimestamp("transaction_date");
							acc_type = "" + rs2.getInt("retailer_org_id");
							orgName = rs2.getString("name");
						}

						if (stmt2 != null) {
							stmt2.close();
						}
						if (rs2 != null) {
							rs2.close();
						}

						stmt = connection.prepareStatement(QueryManager
								.getST6AgtCurrBal());
						stmt.setString(1, acc_type);
						stmt.setInt(2, agent_id);
						rs = stmt.executeQuery();

						while (rs.next()) {

							retailer_org_id_current = rs
									.getDouble("current_balance");

							retailerPresent++;
						}

						if (stmt != null) {
							stmt.close();
						}
						if (rs != null) {
							rs.close();
						}

						if (retailerPresent == 0) {
							stmt = connection.prepareStatement(QueryManager
									.getST6InsertAgentCurrBal());
							stmt.setString(1, acc_type);
							stmt.setDouble(2, 0.0);
							stmt.setInt(3, agent_id);
							stmt.execute();
							retailer_org_id_current = 0.0;

							if (stmt != null) {
								stmt.close();
							}

							// This has been moved into query manager and
							// changed to prepared statement 10-03-08
							// stmt.executeUpdate("insert into
							// st_lms_agent_current_balance
							// (account_type,current_balance,agent_id) values
							// ('"+acc_type+"',0.0,"+agent_id+")");
						}
						retailerPresent = 0;
						agent_bank_acc_current = agent_bank_acc_current
								- amount;
						retailer_org_id_current = retailer_org_id_current
								+ amount;
						logger.debug("LH-AGT-RET**" + transaction_type
								+ "**TRX_ID-" + transaction_id + "**R_ID-"
								+ RECEIPT_ID);
						logger.debug("LH-AGT-RET**" + transaction_date
								+ "**AMT-" + amount + "**ACT_TYPE-" + acc_type
								+ "**RET_ACC-" + retailer_org_id_current
								+ "**AGT_ACC-" + agent_bank_acc_current);
						query2 = QueryManager.getST2InsertAgentLedger();
						upPstmt = connection.prepareStatement(query2);
						upPstmt.setInt(1, agent_id);
						upPstmt.setString(2, transaction_type);
						upPstmt.setLong(3, transaction_id);
						upPstmt.setTimestamp(4, transaction_date);
						upPstmt.setString(5, -amount + "");
						upPstmt.setString(6, "AGNT_BANK_ACC");
						upPstmt.setString(7, agent_bank_acc_current + "");
						upPstmt.setString(8, orgName);
						upPstmt.setString(9, RECEIPT_ID);
						upPstmt.execute();

						if (upPstmt != null) {
							upPstmt.close();
						}

						query1 = QueryManager.getST2InsertAgentLedger();
						updatePstmt = connection.prepareStatement(query1);

						updatePstmt.setInt(1, agent_id);
						updatePstmt.setString(2, transaction_type);
						updatePstmt.setLong(3, transaction_id);
						updatePstmt.setTimestamp(4, transaction_date);
						updatePstmt.setString(5, amount + "");
						updatePstmt.setString(6, acc_type);
						updatePstmt.setString(7, retailer_org_id_current + "");
						updatePstmt.setString(8, orgName);
						updatePstmt.setString(9, RECEIPT_ID);
						updatePstmt.execute();

						if (updatePstmt != null) {
							updatePstmt.close();
						}

						query3 = QueryManager.getST2UpadateAgentCurrentBal();
						updPstmt = connection.prepareStatement(query3);
						updPstmt.setString(1, retailer_org_id_current + "");
						updPstmt.setString(2, acc_type);
						updPstmt.setInt(3, agent_id);
						updPstmt.execute();

						if (updPstmt != null) {
							updPstmt.close();
						}

					}else if (transaction_type.equalsIgnoreCase("OLA_COMMISSION")) {
						stmt2 = connection.prepareStatement(QueryManager
								.getST2AgentOlaCommission());
						stmt2.setLong(1, transaction_id);
						rs2 = stmt2.executeQuery();
						// This has been changed to prepared statement 10-03-08
						// rs2 =
						// stmt2.executeQuery(QueryManager.getST2AgentCash()+""+
						// transaction_id + "");
						while (rs2.next()) {
							amount = rs2.getDouble("amount");
							transaction_date = rs2
									.getTimestamp("transaction_date");
							acc_type = "" + rs2.getInt("retailer_org_id");
							orgName = rs2.getString("name");
						}

						if (stmt2 != null) {
							stmt2.close();
						}
						if (rs2 != null) {
							rs2.close();
						}

						stmt = connection.prepareStatement(QueryManager
								.getST6AgtCurrBal());
						stmt.setString(1, acc_type);
						stmt.setInt(2, agent_id);
						rs = stmt.executeQuery();

						while (rs.next()) {

							retailer_org_id_current = rs
									.getDouble("current_balance");

							retailerPresent++;
						}

						if (stmt != null) {
							stmt.close();
						}
						if (rs != null) {
							rs.close();
						}

						if (retailerPresent == 0) {
							stmt = connection.prepareStatement(QueryManager
									.getST6InsertAgentCurrBal());
							stmt.setString(1, acc_type);
							stmt.setDouble(2, 0.0);
							stmt.setInt(3, agent_id);
							stmt.execute();
							retailer_org_id_current = 0.0;

							if (stmt != null) {
								stmt.close();
							}

							// This has been moved into query manager and
							// changed to prepared statement 10-03-08
							// stmt.executeUpdate("insert into
							// st_lms_agent_current_balance
							// (account_type,current_balance,agent_id) values
							// ('"+acc_type+"',0.0,"+agent_id+")");
						}
						retailerPresent = 0;
						agent_bank_acc_current = agent_bank_acc_current
								- amount;
						retailer_org_id_current = retailer_org_id_current
								+ amount;
						logger.debug("LH-AGT-RET**" + transaction_type
								+ "**TRX_ID-" + transaction_id + "**R_ID-"
								+ RECEIPT_ID);
						logger.debug("LH-AGT-RET**" + transaction_date
								+ "**AMT-" + amount + "**ACT_TYPE-" + acc_type
								+ "**RET_ACC-" + retailer_org_id_current
								+ "**AGT_ACC-" + agent_bank_acc_current);
						query2 = QueryManager.getST2InsertAgentLedger();
						upPstmt = connection.prepareStatement(query2);
						upPstmt.setInt(1, agent_id);
						upPstmt.setString(2, transaction_type);
						upPstmt.setLong(3, transaction_id);
						upPstmt.setTimestamp(4, transaction_date);
						upPstmt.setString(5, -amount + "");
						upPstmt.setString(6, "AGNT_BANK_ACC");
						upPstmt.setString(7, agent_bank_acc_current + "");
						upPstmt.setString(8, orgName);
						upPstmt.setString(9, RECEIPT_ID);
						upPstmt.execute();

						if (upPstmt != null) {
							upPstmt.close();
						}

						query1 = QueryManager.getST2InsertAgentLedger();
						updatePstmt = connection.prepareStatement(query1);

						updatePstmt.setInt(1, agent_id);
						updatePstmt.setString(2, transaction_type);
						updatePstmt.setLong(3, transaction_id);
						updatePstmt.setTimestamp(4, transaction_date);
						updatePstmt.setString(5, amount + "");
						updatePstmt.setString(6, acc_type);
						updatePstmt.setString(7, retailer_org_id_current + "");
						updatePstmt.setString(8, orgName);
						updatePstmt.setString(9, RECEIPT_ID);
						updatePstmt.execute();

						if (updatePstmt != null) {
							updatePstmt.close();
						}

						query3 = QueryManager.getST2UpadateAgentCurrentBal();
						updPstmt = connection.prepareStatement(query3);
						updPstmt.setString(1, retailer_org_id_current + "");
						updPstmt.setString(2, acc_type);
						updPstmt.setInt(3, agent_id);
						updPstmt.execute();

						if (updPstmt != null) {
							updPstmt.close();
						}

					} 
					else if (transaction_type.equalsIgnoreCase("pwt_plr")
							|| transaction_type.equalsIgnoreCase("dg_pwt_plr")) {

						double PWT_AMT = 0;
						double TAX_AMT = 0;
						double NET_AMT = 0;
						String directPwtPlrQry = "";
						if (transaction_type.equalsIgnoreCase("dg_pwt_plr")) {
							directPwtPlrQry = QueryManager
									.getST2AgentDGPwtPlr();
						} else {
							directPwtPlrQry = QueryManager.getST2AgentPwtPlr();
						}
						stmt = connection.prepareStatement(directPwtPlrQry);
						stmt.setLong(1, transaction_id);
						rs = stmt.executeQuery();
						// This has been replaced by the obove prepared
						// statements statements 07-03-08
						// rs =
						// stmt.executeQuery(QueryManager.getST2BoPwtPlr()+""+
						// transaction_id + "");
						while (rs.next()) {
							PWT_AMT = rs.getDouble("pwt_amt");
							TAX_AMT = rs.getDouble("tax_amt");
							NET_AMT = rs.getDouble("net_amt");
							transaction_type = rs.getString("transaction_type");
							// TRANSACTION_DATE =
							// rs.getDate("transaction_date");
							transaction_date = rs
									.getTimestamp("transaction_date");
							orgName = rs.getString("first_name");
						}

						if (rs != null) {
							rs.close();
						}

						PLAYER_PWT_CURRENT = PLAYER_PWT_CURRENT + PWT_AMT;
						agent_pwt_pay_current = agent_pwt_pay_current - PWT_AMT;
						PLAYER_TDS_CURRENT = PLAYER_TDS_CURRENT - TAX_AMT;
						TDS_PAY_CURRENT = TDS_PAY_CURRENT + TAX_AMT;
						agent_bank_acc_current = agent_bank_acc_current
								+ NET_AMT;
						PLAYER_CASH_CURRENT = PLAYER_CASH_CURRENT - NET_AMT;

						stmt
								.executeUpdate("insert into st_lms_agent_ledger (agent_org_id,transaction_type, transaction_id,transaction_date,amount,account_type,balance,transaction_with,receipt_id) values ('"
										+ agent_id
										+ "','"
										+ transaction_type
										+ "',"
										+ transaction_id
										+ ",'"
										+ transaction_date
										+ "',"
										+ (-PWT_AMT + "")
										+ ",'AGNT_PWT_PAY',"
										+ FormatNumber
												.formatNumber(agent_pwt_pay_current)
										+ ", '"
										+ orgName
										+ "','"
										+ RECEIPT_ID
										+ "')");

						stmt
								.executeUpdate("insert into st_lms_agent_ledger (agent_org_id,transaction_type, transaction_id,transaction_date,amount,account_type,balance,transaction_with,receipt_id) values ('"
										+ agent_id
										+ "','"
										+ transaction_type
										+ "',"
										+ transaction_id
										+ ",'"
										+ transaction_date
										+ "',"
										+ FormatNumber.formatNumber(PWT_AMT)
										+ ",'AGNT_PLAYER_PWT',"
										+ FormatNumber
												.formatNumber(PLAYER_PWT_CURRENT)
										+ ", '"
										+ orgName
										+ "','"
										+ RECEIPT_ID
										+ "')");

						stmt
								.executeUpdate("insert into st_lms_agent_ledger (agent_org_id,transaction_type, transaction_id,transaction_date,amount,account_type,balance,transaction_with,receipt_id) values ('"
										+ agent_id
										+ "','"
										+ transaction_type
										+ "',"
										+ transaction_id
										+ ",'"
										+ transaction_date
										+ "',"
										+ (-TAX_AMT + "")
										+ ",'AGNT_PLAYER_TDS',"
										+ FormatNumber
												.formatNumber(PLAYER_TDS_CURRENT)
										+ ", '"
										+ orgName
										+ "','"
										+ RECEIPT_ID
										+ "')");
						stmt
								.executeUpdate("insert into st_lms_agent_ledger (agent_org_id,transaction_type, transaction_id,transaction_date,amount,account_type,balance,transaction_with,receipt_id) values ('"
										+ agent_id
										+ "','"
										+ transaction_type
										+ "',"
										+ transaction_id
										+ ",'"
										+ transaction_date
										+ "',"
										+ TAX_AMT
										+ ""
										+ ",'AGNT_TDS_PAY',"
										+ FormatNumber
												.formatNumber(TDS_PAY_CURRENT)
										+ ", '"
										+ orgName
										+ "','"
										+ RECEIPT_ID
										+ "' )");

						stmt
								.executeUpdate("insert into st_lms_agent_ledger (agent_org_id,transaction_type, transaction_id,transaction_date,amount,account_type,balance,transaction_with,receipt_id) values ('"
										+ agent_id
										+ "','"
										+ transaction_type
										+ "',"
										+ transaction_id
										+ ",'"
										+ transaction_date
										+ "',"
										+ (-NET_AMT + "")
										+ ",'AGNT_PLAYER_CAS',"
										+ FormatNumber
												.formatNumber(PLAYER_CASH_CURRENT)
										+ ", '"
										+ orgName
										+ "','"
										+ RECEIPT_ID
										+ "' )");

						stmt
								.executeUpdate("insert into st_lms_agent_ledger (agent_org_id,transaction_type, transaction_id,transaction_date,amount,account_type,balance,transaction_with,receipt_id) values ('"
										+ agent_id
										+ "','"
										+ transaction_type
										+ "',"
										+ transaction_id
										+ ",'"
										+ transaction_date
										+ "',"
										+ FormatNumber.formatNumber(NET_AMT)
										+ ",'AGNT_BANK_ACC',"
										+ FormatNumber
												.formatNumber(agent_bank_acc_current)
										+ ", '"
										+ orgName
										+ "','"
										+ RECEIPT_ID
										+ "' )");

						if (stmt != null) {
							stmt.close();
						}

					}

					query3 = QueryManager.getST2UpadateAgentCurrentBal();
					updPstmt = connection.prepareStatement(query3);
					updPstmt.setString(1, agent_bank_acc_current + "");
					updPstmt.setString(2, "AGNT_BANK_ACC");
					updPstmt.setInt(3, agent_id);
					updPstmt.execute();

					if (updPstmt != null) {
						updPstmt.close();
					}

					query3 = QueryManager.getST2UpadateAgentCurrentBal();
					updPstmt = connection.prepareStatement(query3);
					updPstmt.setString(1, agnt_sale_acc_current + "");
					updPstmt.setString(2, "AGNT_SALE_ACC");
					updPstmt.setInt(3, agent_id);
					updPstmt.execute();

					if (updPstmt != null) {
						updPstmt.close();
					}

					query3 = QueryManager.getST2UpadateAgentCurrentBal();
					updPstmt = connection.prepareStatement(query3);
					updPstmt.setString(1, agnt_sale_ret_current + "");
					updPstmt.setString(2, "AGNT_SALE_RET");
					updPstmt.setInt(3, agent_id);
					updPstmt.execute();

					if (updPstmt != null) {
						updPstmt.close();
					}

					query3 = QueryManager.getST2UpadateAgentCurrentBal();
					updPstmt = connection.prepareStatement(query3);
					updPstmt.setString(1, agent_pwt_pay_current + "");
					updPstmt.setString(2, "AGNT_PWT_PAY");
					updPstmt.setInt(3, agent_id);
					updPstmt.execute();

					if (updPstmt != null) {
						updPstmt.close();
					}

					/*
					 * for collection charges
					 */
					query3 = QueryManager.getST2UpadateAgentCurrentBal();
					updPstmt = connection.prepareStatement(query3);
					updPstmt.setString(1, agent_pwt_charges_current + "");
					updPstmt.setString(2, "AGENT_PWT_CHARGES");
					updPstmt.setInt(3, agent_id);
					updPstmt.execute();

					if (updPstmt != null) {
						updPstmt.close();
					}

					/*
					 * for vat payable
					 */

					query3 = QueryManager.getST2UpadateAgentCurrentBal();
					updPstmt = connection.prepareStatement(query3);
					updPstmt.setString(1, agent_vat_pay_current + "");
					updPstmt.setString(2, "AGENT_VAT_PAY");
					updPstmt.setInt(3, agent_id);
					updPstmt.execute();

					if (updPstmt != null) {
						updPstmt.close();
					}

					query3 = QueryManager.getST2UpadateAgentCurrentBal();
					updPstmt = connection.prepareStatement(query3);
					updPstmt.setString(1, PLAYER_PWT_CURRENT + "");
					updPstmt.setString(2, "AGNT_PLAYER_PWT");
					updPstmt.setInt(3, agent_id);
					updPstmt.execute();

					if (updPstmt != null) {
						updPstmt.close();
					}

					query3 = QueryManager.getST2UpadateAgentCurrentBal();
					updPstmt = connection.prepareStatement(query3);
					updPstmt.setString(1, PLAYER_TDS_CURRENT + "");
					updPstmt.setString(2, "AGNT_PLAYER_TDS");
					updPstmt.setInt(3, agent_id);
					updPstmt.execute();

					if (updPstmt != null) {
						updPstmt.close();
					}

					query3 = QueryManager.getST2UpadateAgentCurrentBal();
					updPstmt = connection.prepareStatement(query3);
					updPstmt.setString(1, TDS_PAY_CURRENT + "");
					updPstmt.setString(2, "AGNT_TDS_PAY");
					updPstmt.setInt(3, agent_id);
					updPstmt.execute();

					if (updPstmt != null) {
						updPstmt.close();
					}

					query3 = QueryManager.getST2UpadateAgentCurrentBal();
					updPstmt = connection.prepareStatement(query3);
					updPstmt.setString(1, PLAYER_CASH_CURRENT + "");
					updPstmt.setString(2, "AGNT_PLAYER_CAS");
					updPstmt.setInt(3, agent_id);
					updPstmt.execute();

					if (updPstmt != null) {
						updPstmt.close();
					}

				}

				if (stmt1 != null) {
					stmt1.close();
				}
				if (rs1 != null) {
					rs1.close();
				}

				// ///////////////////////////////////////////Agent Retailer
				// Transaction
				// End////////////////////////////////////////////////////////////////////
				// This is the end of Retailer and Agent Ledger entry Part

				query3 = QueryManager.getST2UpadateAgentCurrentBal();
				updPstmt = connection.prepareStatement(query3);
				updPstmt.setString(1, bo_bank_acc_current + "");
				updPstmt.setString(2, "BO_BANK_ACC");
				updPstmt.setInt(3, agent_id);
				updPstmt.execute();

				if (updPstmt != null) {
					updPstmt.close();
				}

				query3 = QueryManager.getST2UpadateAgentCurrentBal();
				updPstmt = connection.prepareStatement(query3);
				updPstmt.setString(1, bo_sale_acc_current + "");
				updPstmt.setString(2, "BO_SALE_ACC");
				updPstmt.setInt(3, agent_id);
				updPstmt.execute();

				if (updPstmt != null) {
					updPstmt.close();
				}

				query3 = QueryManager.getST2UpadateAgentCurrentBal();
				updPstmt = connection.prepareStatement(query3);
				updPstmt.setString(1, bo_sale_ret_current + "");
				updPstmt.setString(2, "BO_SALE_RET");
				updPstmt.setInt(3, agent_id);
				updPstmt.execute();

				if (updPstmt != null) {
					updPstmt.close();
				}

				query3 = QueryManager.getST2UpadateAgentCurrentBal();
				updPstmt = connection.prepareStatement(query3);
				updPstmt.setString(1, bo_pwt_pay_current + "");
				updPstmt.setString(2, "BO_PWT_PAY");
				updPstmt.setInt(3, agent_id);
				updPstmt.execute();

				if (updPstmt != null) {
					updPstmt.close();
				}

				query3 = QueryManager.getST2UpadateAgentCurrentBal();
				updPstmt = connection.prepareStatement(query3);
				updPstmt.setString(1, bo_pwt_charges_current + "");
				updPstmt.setString(2, "BO_PWT_CHARGES");
				updPstmt.setInt(3, agent_id);
				updPstmt.execute();

				if (updPstmt != null) {
					updPstmt.close();
				}

				// END OF BO AND AGENT LEDGER TRANSACTION
				insBalHistory("agent", connection, toDate_Bean);
			} // END OF IF STATEMENT

			connection.commit();
		} catch (SQLException e) {

			try {
				System.out.println("In agent Exception " + e);
				e.printStackTrace();
				connection.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		finally {
			if (connection != null) {
				try {

					connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public void ledgerBoEntry(Timestamp toDate_Bean) throws LMSException {

		try {

			connection = DBConnect.getConnection();
			connection.setAutoCommit(false);
			present_date = toDate_Bean;
			toDate_Bean = new Timestamp(present_date.getTime() + 24 * 60 * 60
					* 1000 - 1000);
			to_Date = toDate_Bean;
			String rcptQry = "select sbr.receipt_id,sbr.generated_id from st_lms_bo_receipts_trn_mapping sbrtm , st_lms_bo_receipts sbr where transaction_id=? and sbrtm.receipt_id=sbr.receipt_id";

			logger.debug("LH-BO**PresentDate-" + present_date + "**ToDate-"
					+ toDate_Bean);
			if (present_date.before(to_Date)) {

				int resultQuery = 0;
				statement = connection.createStatement();
				rs = statement.executeQuery(QueryManager.getST2BoCurrentBal());

				while (rs.next()) {
					if (rs.getString("account_type").equals("TDS_PAY")) {
						TDS_PAY_CURRENT = rs.getDouble("current_balance");
					} else if (rs.getString("account_type").equals("SALE_ACC")) {
						SALE_ACC_CURRENT = rs.getDouble("current_balance");
					} else if (rs.getString("account_type").equals("SALE_RET")) {
						SALE_RET_CURRENT = rs.getDouble("current_balance");
					} else if (rs.getString("account_type").equals("PWT_PAY")) {
						PWT_PAY_CURRENT = rs.getDouble("current_balance");
					} else if (rs.getString("account_type")
							.equals("UNCLM_GOVT")) {
						UNCLM_GOVT_CURRENT = rs.getDouble("current_balance");
					} else if (rs.getString("account_type").equals("GOVT_COMM")) {
						GOVT_COMM_CURRENT = rs.getDouble("current_balance");
					} else if (rs.getString("account_type").equals("BANK_ACC")) {
						BANK_ACC_CURRENT = rs.getDouble("current_balance");
					} else if (rs.getString("account_type")
							.equals("PLAYER_TDS")) {
						PLAYER_TDS_CURRENT = rs.getDouble("current_balance");
					} else if (rs.getString("account_type")
							.equals("PLAYER_PWT")) {
						PLAYER_PWT_CURRENT = rs.getDouble("current_balance");
					} else if (rs.getString("account_type")
							.equals("PLAYER_CAS")) {
						PLAYER_CASH_CURRENT = rs.getDouble("current_balance");
					} else if (rs.getString("account_type").equals("VAT_PAY")) {
						VAT_PAY_CURRENT = rs.getDouble("current_balance");
					} else if (rs.getString("account_type").equals(
							"PWT_CHARGES")) {
						PWT_CHARGES_CURRENT = rs.getDouble("current_balance");
					}
					resultQuery++;
				}
				if (statement != null) {
					statement.close();
				}
				if (rs != null) {
					rs.close();
				}

				Statement stmtInitial = connection.createStatement();
				if (resultQuery == 0) {

					stmtInitial
							.executeUpdate("insert into st_lms_bo_current_balance (account_type,current_balance) values ('BANK_ACC',0.0)");
					stmtInitial
							.executeUpdate("insert into st_lms_bo_current_balance (account_type,current_balance) values ('GOVT_COMM',0.0)");
					stmtInitial
							.executeUpdate("insert into st_lms_bo_current_balance (account_type,current_balance) values ('PLAYER_CAS',0.0)");
					stmtInitial
							.executeUpdate("insert into st_lms_bo_current_balance (account_type,current_balance) values ('PLAYER_PWT',0.0)");
					stmtInitial
							.executeUpdate("insert into st_lms_bo_current_balance (account_type,current_balance) values ('PLAYER_TDS',0.0)");
					stmtInitial
							.executeUpdate("insert into st_lms_bo_current_balance (account_type,current_balance) values ('PWT_PAY',0.0)");
					stmtInitial
							.executeUpdate("insert into st_lms_bo_current_balance (account_type,current_balance) values ('SALE_ACC',0.0)");
					stmtInitial
							.executeUpdate("insert into st_lms_bo_current_balance (account_type,current_balance) values ('SALE_RET',0.0)");
					stmtInitial
							.executeUpdate("insert into st_lms_bo_current_balance (account_type,current_balance) values ('TDS_PAY',0.0)");
					stmtInitial
							.executeUpdate("insert into st_lms_bo_current_balance (account_type,current_balance) values ('UNCLM_GOVT',0.0)");
					stmtInitial
							.executeUpdate("insert into st_lms_bo_current_balance (account_type,current_balance) values ('VAT_PAY',0.0)");
					stmtInitial
							.executeUpdate("insert into st_lms_bo_current_balance (account_type,current_balance) values ('PWT_CHARGES',0.0)");

				}
				if (stmtInitial != null) {
					stmtInitial.close();
				}

				stmt1 = connection.prepareStatement(QueryManager
						.getST2BoTransaction());
				stmt1.setTimestamp(1, present_date);
				stmt1.setTimestamp(2, to_Date);
				logger.debug("LH-BO**Select Trxn Master**" + stmt1);
				rs1 = stmt1.executeQuery();
				// This has been replaced by the obove prepared statements
				// statements 07-03-08
				// rs1 =
				// stmt1.executeQuery(QueryManager.getST2BoTransaction()+"'"+
				// present_date + "'");

				while (rs1.next()) {

					long transaction_id = rs1.getLong("transaction_id");
					String TRANSACTION_TYPE = rs1.getString("transaction_type");
					String RECEIPT_ID = "N.A.";

					rcptPS = connection.prepareStatement(rcptQry);
					rcptPS.setLong(1, transaction_id);
					rcptRS = rcptPS.executeQuery();
					while (rcptRS.next()) {
						genNum = rcptRS.getString("generated_id");
						// System.out.println("****"+genNum+"***"+genNum.trim()+"***"+genNum.trim().equals(""));
						if (genNum != null
								&& !genNum.trim().equals("")
								&& !(genNum.contains("DLB") || genNum
										.contains("DSB"))) {
							RECEIPT_ID = rcptRS.getString("generated_id");
						}

					}
					if (rcptPS != null) {
						rcptPS.close();
					}
					if (rcptRS != null) {
						rcptRS.close();
					}

					double AMOUNT = 0;
					Timestamp TRANSACTION_DATE = null;
					String ACC_TYPE = "";
					String saleQuery = "";
					String saleRetQuery = "";
					logger.debug("LH-BO**" + TRANSACTION_TYPE + "**TRX_ID-"
							+ transaction_id + "**R_ID-" + RECEIPT_ID);
					if (TRANSACTION_TYPE.equalsIgnoreCase("Sale")
							| TRANSACTION_TYPE.equalsIgnoreCase("DG_SALE")
							| TRANSACTION_TYPE.equalsIgnoreCase("CS_SALE") | TRANSACTION_TYPE.equalsIgnoreCase("OLA_DEPOSIT")) {
						if (TRANSACTION_TYPE.equalsIgnoreCase("Sale")) {
							saleQuery = QueryManager.getST2BoSale();
						} else if (TRANSACTION_TYPE.equalsIgnoreCase("DG_SALE")) {
							saleQuery = QueryManager.getST2BoDrwGmSale();
						} else if (TRANSACTION_TYPE.equalsIgnoreCase("CS_SALE")) {
							saleQuery = QueryManager.getST2BoCsSale();
						}else if (TRANSACTION_TYPE.equalsIgnoreCase("OLA_DEPOSIT")) {
							saleQuery = QueryManager.getST2BoOlaDeposit();
						}
						stmt2 = connection.prepareStatement(saleQuery);
						stmt2.setLong(1, transaction_id);
						rs2 = stmt2.executeQuery();
						while (rs2.next()) {
							AMOUNT = rs2.getDouble("net_amt");
							TRANSACTION_DATE = rs2
									.getTimestamp("transaction_date");
							ACC_TYPE = "" + rs2.getInt("agent_org_id");
							orgName = rs2.getString("name");
						}
						if (stmt2 != null) {
							stmt2.close();
						}
						if (rs2 != null) {
							rs2.close();
						}

						stmt = connection.prepareStatement(QueryManager
								.getST2BoCurrentBalAgent());
						stmt.setString(1, ACC_TYPE);
						rs = stmt.executeQuery();
						int i = 0;
						while (rs.next()) {
							AGENT_ACCOUNT_CURRENT = rs
									.getDouble("current_balance");
							i++;
						}
						if (stmt != null) {
							stmt.close();
						}
						if (rs != null) {
							rs.close();
						}

						if (i == 0) {
							query1 = QueryManager.getST2InsertAgentCurrentBal();
							updatePstmt = connection.prepareStatement(query1);
							updatePstmt.setString(1, "AGENT_ACC");
							updatePstmt.setInt(2, 0);
							updatePstmt.setString(3, ACC_TYPE);
							updatePstmt.execute();
							AGENT_ACCOUNT_CURRENT = 0.0;

							if (updatePstmt != null) {
								updatePstmt.close();
							}

						}
						AGENT_ACCOUNT_CURRENT = AGENT_ACCOUNT_CURRENT - AMOUNT;
						SALE_ACC_CURRENT = SALE_ACC_CURRENT + AMOUNT;
						logger.debug("LH-BO**" + TRANSACTION_DATE + "**AMT-"
								+ AMOUNT + "**ACT_TYPE-" + ACC_TYPE
								+ "**AGT_ACC=" + AGENT_ACCOUNT_CURRENT
								+ "**SALE_ACC" + SALE_ACC_CURRENT);
						query1 = QueryManager.getST2InsertBoLedger();
						updatePstmt = connection.prepareStatement(query1);

						updatePstmt.setString(1, TRANSACTION_TYPE);
						updatePstmt.setLong(2, transaction_id);
						updatePstmt.setTimestamp(3, TRANSACTION_DATE);
						updatePstmt.setString(4, -AMOUNT + "");
						updatePstmt.setString(5, ACC_TYPE);
						updatePstmt.setString(6, AGENT_ACCOUNT_CURRENT + "");
						updatePstmt.setString(7, orgName);
						updatePstmt.setString(8, RECEIPT_ID);
						updatePstmt.execute();

						if (updatePstmt != null) {
							updatePstmt.close();
						}

						query2 = QueryManager.getST2InsertBoLedger();
						upPstmt = connection.prepareStatement(query2);
						upPstmt.setString(1, TRANSACTION_TYPE);
						upPstmt.setLong(2, transaction_id);
						upPstmt.setTimestamp(3, TRANSACTION_DATE);
						upPstmt.setString(4, AMOUNT + "");
						upPstmt.setString(5, "SALE_ACC");
						upPstmt.setString(6, SALE_ACC_CURRENT + "");
						upPstmt.setString(7, orgName);
						upPstmt.setString(8, RECEIPT_ID);
						upPstmt.execute();

						if (upPstmt != null) {
							upPstmt.close();
						}

						query3 = QueryManager.getST2UpadateCurrentBalAgent();
						updPstmt = connection.prepareStatement(query3);
						updPstmt.setString(1, AGENT_ACCOUNT_CURRENT + "");
						updPstmt.setString(2, "AGENT_ACC");
						updPstmt.setString(3, ACC_TYPE);
						updPstmt.execute();

						if (updPstmt != null) {
							updPstmt.close();
						}
					}

				else if (TRANSACTION_TYPE.equalsIgnoreCase("cash")) {
						stmt2 = connection.prepareStatement(QueryManager
								.getST2BoCash());
						stmt2.setLong(1, transaction_id);
						rs2 = stmt2.executeQuery();

						while (rs2.next()) {
							TRANSACTION_DATE = rs2
									.getTimestamp("transaction_date");
							AMOUNT = rs2.getDouble("amount");
							ACC_TYPE = "" + rs2.getInt("agent_org_id");
							orgName = rs2.getString("name");
						}
						if (stmt2 != null) {
							stmt2.close();
						}
						if (rs2 != null) {
							rs2.close();
						}
						stmt = connection.prepareStatement(QueryManager
								.getST2BoCurrentBalAgent());
						stmt.setString(1, ACC_TYPE);
						rs = stmt.executeQuery();
						int i = 0;
						while (rs.next()) {
							AGENT_ACCOUNT_CURRENT = rs
									.getDouble("current_balance");
							i++;
						}

						if (stmt != null) {
							stmt.close();
						}
						if (rs != null) {
							rs.close();
						}

						if (i == 0) {
							query1 = QueryManager.getST2InsertAgentCurrentBal();
							updatePstmt = connection.prepareStatement(query1);
							updatePstmt.setString(1, "AGENT_ACC");
							updatePstmt.setInt(2, 0);
							updatePstmt.setString(3, ACC_TYPE);
							updatePstmt.execute();
							AGENT_ACCOUNT_CURRENT = 0.0;

							if (updatePstmt != null) {
								updatePstmt.close();
							}

						}

						AGENT_ACCOUNT_CURRENT = AGENT_ACCOUNT_CURRENT + AMOUNT;
						BANK_ACC_CURRENT = BANK_ACC_CURRENT - AMOUNT;
						logger.debug("LH-BO**" + TRANSACTION_DATE + "**AMT-"
								+ AMOUNT + "**ACT_TYPE-" + ACC_TYPE
								+ "**AGT_ACC=" + AGENT_ACCOUNT_CURRENT
								+ "**BANK_ACC" + BANK_ACC_CURRENT);

						query2 = QueryManager.getST2InsertBoLedger();
						upPstmt = connection.prepareStatement(query2);
						upPstmt.setString(1, TRANSACTION_TYPE);
						upPstmt.setLong(2, transaction_id);
						// upPstmt.setDate(3, TRANSACTION_DATE);
						upPstmt.setTimestamp(3, TRANSACTION_DATE);
						upPstmt.setString(4, -AMOUNT + "");
						upPstmt.setString(5, "BANK_ACC");
						upPstmt.setString(6, BANK_ACC_CURRENT + "");
						upPstmt.setString(7, orgName);
						upPstmt.setString(8, RECEIPT_ID);
						upPstmt.execute();

						if (upPstmt != null) {
							upPstmt.close();
						}

						query1 = QueryManager.getST2InsertBoLedger();
						updatePstmt = connection.prepareStatement(query1);

						updatePstmt.setString(1, TRANSACTION_TYPE);
						updatePstmt.setLong(2, transaction_id);
						// updatePstmt.setDate(3, TRANSACTION_DATE);
						updatePstmt.setTimestamp(3, TRANSACTION_DATE);
						updatePstmt.setString(4, AMOUNT + "");
						updatePstmt.setString(5, ACC_TYPE);
						updatePstmt.setString(6, AGENT_ACCOUNT_CURRENT + "");
						updatePstmt.setString(7, orgName);
						updatePstmt.setString(8, RECEIPT_ID);
						updatePstmt.execute();

						if (updatePstmt != null) {
							updatePstmt.close();
						}

						query3 = QueryManager.getST2UpadateCurrentBalAgent();
						updPstmt = connection.prepareStatement(query3);
						updPstmt.setString(1, AGENT_ACCOUNT_CURRENT + "");
						updPstmt.setString(2, "AGENT_ACC");
						updPstmt.setString(3, ACC_TYPE);
						updPstmt.execute();

						if (updPstmt != null) {
							updPstmt.close();
						}

					} 
				else if (TRANSACTION_TYPE.equalsIgnoreCase("OLA_COMMISSION")) {
					stmt2 = connection.prepareStatement(QueryManager
							.getST2BoOlaCommission());
					stmt2.setLong(1, transaction_id);
					rs2 = stmt2.executeQuery();

					while (rs2.next()) {
						TRANSACTION_DATE = rs2
								.getTimestamp("transaction_date");
						AMOUNT = rs2.getDouble("amount");
						ACC_TYPE = "" + rs2.getInt("agent_org_id");
						orgName = rs2.getString("name");
					}
					if (stmt2 != null) {
						stmt2.close();
					}
					if (rs2 != null) {
						rs2.close();
					}
					stmt = connection.prepareStatement(QueryManager
							.getST2BoCurrentBalAgent());
					stmt.setString(1, ACC_TYPE);
					rs = stmt.executeQuery();
					int i = 0;
					while (rs.next()) {
						AGENT_ACCOUNT_CURRENT = rs
								.getDouble("current_balance");
						i++;
					}

					if (stmt != null) {
						stmt.close();
					}
					if (rs != null) {
						rs.close();
					}

					if (i == 0) {
						query1 = QueryManager.getST2InsertAgentCurrentBal();
						updatePstmt = connection.prepareStatement(query1);
						updatePstmt.setString(1, "AGENT_ACC");
						updatePstmt.setInt(2, 0);
						updatePstmt.setString(3, ACC_TYPE);
						updatePstmt.execute();
						AGENT_ACCOUNT_CURRENT = 0.0;

						if (updatePstmt != null) {
							updatePstmt.close();
						}

					}

					AGENT_ACCOUNT_CURRENT = AGENT_ACCOUNT_CURRENT + AMOUNT;
					BANK_ACC_CURRENT = BANK_ACC_CURRENT - AMOUNT;
					logger.debug("LH-BO**" + TRANSACTION_DATE + "**AMT-"
							+ AMOUNT + "**ACT_TYPE-" + ACC_TYPE
							+ "**AGT_ACC=" + AGENT_ACCOUNT_CURRENT
							+ "**BANK_ACC" + BANK_ACC_CURRENT);

					query2 = QueryManager.getST2InsertBoLedger();
					upPstmt = connection.prepareStatement(query2);
					upPstmt.setString(1, TRANSACTION_TYPE);
					upPstmt.setLong(2, transaction_id);
					// upPstmt.setDate(3, TRANSACTION_DATE);
					upPstmt.setTimestamp(3, TRANSACTION_DATE);
					upPstmt.setString(4, -AMOUNT + "");
					upPstmt.setString(5, "BANK_ACC");
					upPstmt.setString(6, BANK_ACC_CURRENT + "");
					upPstmt.setString(7, orgName);
					upPstmt.setString(8, RECEIPT_ID);
					upPstmt.execute();

					if (upPstmt != null) {
						upPstmt.close();
					}

					query1 = QueryManager.getST2InsertBoLedger();
					updatePstmt = connection.prepareStatement(query1);

					updatePstmt.setString(1, TRANSACTION_TYPE);
					updatePstmt.setLong(2, transaction_id);
					// updatePstmt.setDate(3, TRANSACTION_DATE);
					updatePstmt.setTimestamp(3, TRANSACTION_DATE);
					updatePstmt.setString(4, AMOUNT + "");
					updatePstmt.setString(5, ACC_TYPE);
					updatePstmt.setString(6, AGENT_ACCOUNT_CURRENT + "");
					updatePstmt.setString(7, orgName);
					updatePstmt.setString(8, RECEIPT_ID);
					updatePstmt.execute();

					if (updatePstmt != null) {
						updatePstmt.close();
					}

					query3 = QueryManager.getST2UpadateCurrentBalAgent();
					updPstmt = connection.prepareStatement(query3);
					updPstmt.setString(1, AGENT_ACCOUNT_CURRENT + "");
					updPstmt.setString(2, "AGENT_ACC");
					updPstmt.setString(3, ACC_TYPE);
					updPstmt.execute();

					if (updPstmt != null) {
						updPstmt.close();
					}

				}
				else if (TRANSACTION_TYPE
							.equalsIgnoreCase("BANK_DEPOSIT")) {// code updated
						// for Bank Dep
						// starts ...
						stmt2 = connection.prepareStatement(QueryManager
								.getST2BoBankDep());
						stmt2.setLong(1, transaction_id);
						rs2 = stmt2.executeQuery();

						while (rs2.next()) {
							TRANSACTION_DATE = rs2
									.getTimestamp("transaction_date");
							AMOUNT = rs2.getDouble("amount");
							ACC_TYPE = "" + rs2.getInt("agent_org_id");
							orgName = rs2.getString("name");
						}
						if (stmt2 != null) {
							stmt2.close();
						}
						if (rs2 != null) {
							rs2.close();
						}
						stmt = connection.prepareStatement(QueryManager
								.getST2BoCurrentBalAgent());
						stmt.setString(1, ACC_TYPE);
						rs = stmt.executeQuery();
						int i = 0;
						while (rs.next()) {
							AGENT_ACCOUNT_CURRENT = rs
									.getDouble("current_balance");
							i++;
						}

						if (stmt != null) {
							stmt.close();
						}
						if (rs != null) {
							rs.close();
						}

						if (i == 0) {
							query1 = QueryManager.getST2InsertAgentCurrentBal();
							updatePstmt = connection.prepareStatement(query1);
							updatePstmt.setString(1, "AGENT_ACC");
							updatePstmt.setInt(2, 0);
							updatePstmt.setString(3, ACC_TYPE);
							updatePstmt.execute();
							AGENT_ACCOUNT_CURRENT = 0.0;

							if (updatePstmt != null) {
								updatePstmt.close();
							}

						}

						AGENT_ACCOUNT_CURRENT = AGENT_ACCOUNT_CURRENT + AMOUNT;
						BANK_ACC_CURRENT = BANK_ACC_CURRENT - AMOUNT;
						logger.debug("LH-BO**" + TRANSACTION_DATE + "**AMT-"
								+ AMOUNT + "**ACT_TYPE-" + ACC_TYPE
								+ "**AGT_ACC=" + AGENT_ACCOUNT_CURRENT
								+ "**BANK_ACC" + BANK_ACC_CURRENT);

						query2 = QueryManager.getST2InsertBoLedger();
						upPstmt = connection.prepareStatement(query2);
						upPstmt.setString(1, TRANSACTION_TYPE);
						upPstmt.setLong(2, transaction_id);
						// upPstmt.setDate(3, TRANSACTION_DATE);
						upPstmt.setTimestamp(3, TRANSACTION_DATE);
						upPstmt.setString(4, -AMOUNT + "");
						upPstmt.setString(5, "BANK_ACC");
						upPstmt.setString(6, BANK_ACC_CURRENT + "");
						upPstmt.setString(7, orgName);
						upPstmt.setString(8, RECEIPT_ID);
						upPstmt.execute();

						if (upPstmt != null) {
							upPstmt.close();
						}

						query1 = QueryManager.getST2InsertBoLedger();
						updatePstmt = connection.prepareStatement(query1);

						updatePstmt.setString(1, TRANSACTION_TYPE);
						updatePstmt.setLong(2, transaction_id);
						// updatePstmt.setDate(3, TRANSACTION_DATE);
						updatePstmt.setTimestamp(3, TRANSACTION_DATE);
						updatePstmt.setString(4, AMOUNT + "");
						updatePstmt.setString(5, ACC_TYPE);
						updatePstmt.setString(6, AGENT_ACCOUNT_CURRENT + "");
						updatePstmt.setString(7, orgName);
						updatePstmt.setString(8, RECEIPT_ID);
						updatePstmt.execute();

						if (updatePstmt != null) {
							updatePstmt.close();
						}

						query3 = QueryManager.getST2UpadateCurrentBalAgent();
						updPstmt = connection.prepareStatement(query3);
						updPstmt.setString(1, AGENT_ACCOUNT_CURRENT + "");
						updPstmt.setString(2, "AGENT_ACC");
						updPstmt.setString(3, ACC_TYPE);
						updPstmt.execute();

						if (updPstmt != null) {
							updPstmt.close();
						}

					}// code updated for Bank Dep ends...
					else if (TRANSACTION_TYPE.equalsIgnoreCase("cheque")) {
						// System.out.println("in Cheque");
						stmt2 = connection.prepareStatement(QueryManager
								.getST2BoChq());
						stmt2.setLong(1, transaction_id);
						rs2 = stmt2.executeQuery();
						// This has been replaced by the obove prepared
						// statements statements 07-03-08
						// rs2 =
						// stmt2.executeQuery(QueryManager.getST2BoChq()+""+
						// transaction_id + "");

						while (rs2.next()) {
							AMOUNT = rs2.getDouble("cheque_amt");
							// TRANSACTION_DATE =
							// rs2.getDate("transaction_date");
							TRANSACTION_DATE = rs2
									.getTimestamp("transaction_date");
							// TRANS_DATE= new
							// java.sql.Date(TRANSACTION_DATE.getTime());

							ACC_TYPE = "" + rs2.getInt("agent_org_id");
							orgName = rs2.getString("name");
						}

						if (stmt2 != null) {
							stmt2.close();
						}
						if (rs2 != null) {
							rs2.close();
						}

						// System.out.println("Amount-- "+AMOUNT+" TRANS DATE--
						// "+TRANSACTION_DATE+" ACC_TYPE-- "+ACC_TYPE+" Org NAme
						// "+orgName);
						stmt = connection.prepareStatement(QueryManager
								.getST2BoCurrentBalAgent());
						stmt.setString(1, ACC_TYPE);
						rs = stmt.executeQuery();
						// This has been replaced by the obove prepared
						// statements statements 07-03-08
						// rs =
						// stmt.executeQuery(QueryManager.getST2BoCurrentBalAgent()+"'"+
						// ACC_TYPE + "'");
						int i = 0;
						while (rs.next()) {
							AGENT_ACCOUNT_CURRENT = rs
									.getDouble("current_balance");
							i++;
						}

						if (stmt != null) {
							stmt.close();
						}
						if (rs != null) {
							rs.close();
						}

						if (i == 0) {
							query1 = QueryManager.getST2InsertAgentCurrentBal();
							updatePstmt = connection.prepareStatement(query1);
							updatePstmt.setString(1, "AGENT_ACC");
							updatePstmt.setInt(2, 0);
							updatePstmt.setString(3, ACC_TYPE);
							updatePstmt.execute();
							AGENT_ACCOUNT_CURRENT = 0.0;

							if (updatePstmt != null) {
								updatePstmt.close();
							}

						}

						AGENT_ACCOUNT_CURRENT = AGENT_ACCOUNT_CURRENT + AMOUNT;
						BANK_ACC_CURRENT = BANK_ACC_CURRENT - AMOUNT;
						// System.out.println("AGT ACC CUR--
						// "+AGENT_ACCOUNT_CURRENT+"BANK ACC
						// CUR"+BANK_ACC_CURRENT+"");
						logger.debug("LH-BO**" + TRANSACTION_DATE + "**AMT-"
								+ AMOUNT + "**ACT_TYPE-" + ACC_TYPE
								+ "**AGT_ACC=" + AGENT_ACCOUNT_CURRENT
								+ "**BANK_ACC" + BANK_ACC_CURRENT);

						query2 = QueryManager.getST2InsertBoLedger();
						upPstmt = connection.prepareStatement(query2);
						upPstmt.setString(1, TRANSACTION_TYPE);
						upPstmt.setLong(2, transaction_id);
						// upPstmt.setDate(3, TRANSACTION_DATE);
						upPstmt.setTimestamp(3, TRANSACTION_DATE);
						upPstmt.setString(4, -AMOUNT + "");
						upPstmt.setString(5, "BANK_ACC");
						upPstmt.setString(6, BANK_ACC_CURRENT + "");
						upPstmt.setString(7, orgName);
						upPstmt.setString(8, RECEIPT_ID);
						upPstmt.execute();

						if (upPstmt != null) {
							upPstmt.close();
						}

						query1 = QueryManager.getST2InsertBoLedger();
						updatePstmt = connection.prepareStatement(query1);

						updatePstmt.setString(1, TRANSACTION_TYPE);
						updatePstmt.setLong(2, transaction_id);
						// updatePstmt.setDate(3, TRANSACTION_DATE);
						updatePstmt.setTimestamp(3, TRANSACTION_DATE);
						updatePstmt.setString(4, AMOUNT + "");
						updatePstmt.setString(5, ACC_TYPE);
						updatePstmt.setString(6, AGENT_ACCOUNT_CURRENT + "");
						updatePstmt.setString(7, orgName);
						updatePstmt.setString(8, RECEIPT_ID);
						updatePstmt.execute();

						if (updatePstmt != null) {
							updatePstmt.close();
						}

						query3 = QueryManager.getST2UpadateCurrentBalAgent();
						updPstmt = connection.prepareStatement(query3);
						updPstmt.setString(1, AGENT_ACCOUNT_CURRENT + "");
						updPstmt.setString(2, "AGENT_ACC");
						updPstmt.setString(3, ACC_TYPE);
						updPstmt.execute();

						if (updPstmt != null) {
							updPstmt.close();
						}

					} else if (TRANSACTION_TYPE.equalsIgnoreCase("sale_ret")
							|| TRANSACTION_TYPE
									.equalsIgnoreCase("DG_REFUND_FAILED")
							|| TRANSACTION_TYPE
									.equalsIgnoreCase("DG_REFUND_CANCEL")
							|| TRANSACTION_TYPE
									.equalsIgnoreCase("CS_CANCEL_SERVER")
							|| TRANSACTION_TYPE
									.equalsIgnoreCase("CS_CANCEL_RET") || TRANSACTION_TYPE.equalsIgnoreCase("OLA_DEPOSIT_REFUND")) {
						// System.out.println("in Sale Ret");
						if (TRANSACTION_TYPE.equalsIgnoreCase("sale_ret")) {
							saleRetQuery = QueryManager.getST2BoSaleRet();
						} else if (TRANSACTION_TYPE
								.equalsIgnoreCase("DG_REFUND_FAILED")
								|| TRANSACTION_TYPE
										.equalsIgnoreCase("DG_REFUND_CANCEL")) {
							saleRetQuery = QueryManager.getST2BoDrwGmRefnd();
						} else if (TRANSACTION_TYPE
								.equalsIgnoreCase("CS_CANCEL_SERVER")
								|| TRANSACTION_TYPE
										.equalsIgnoreCase("CS_CANCEL_RET")) {
							saleRetQuery = QueryManager.getST2BoCsRefnd();
						}
						else if(TRANSACTION_TYPE.equalsIgnoreCase("OLA_DEPOSIT_REFUND"))
						{
							saleRetQuery = QueryManager.getST2BoOlaRefnd();
						}
						stmt2 = connection.prepareStatement(saleRetQuery);
						stmt2.setLong(1, transaction_id);
						rs2 = stmt2.executeQuery();
						// This has been replaced by the obove prepared
						// statements statements 07-03-08
						// rs2 =
						// stmt2.executeQuery(QueryManager.getST2BoSaleRet()+""+
						// transaction_id + "");

						while (rs2.next()) {
							AMOUNT = rs2.getDouble("net_amt");
							// TRANSACTION_DATE =
							// rs2.getDate("transaction_date");
							TRANSACTION_DATE = rs2
									.getTimestamp("transaction_date");
							ACC_TYPE = "" + rs2.getInt("agent_org_id");
							orgName = rs2.getString("name");
						}

						if (stmt2 != null) {
							stmt2.close();
						}
						if (rs2 != null) {
							rs2.close();
						}

						// System.out.println("Amount-- "+AMOUNT+" TRANS DATE--
						// "+TRANSACTION_DATE+" ACC_TYPE-- "+ACC_TYPE+" Org NAme
						// "+orgName);
						stmt = connection.prepareStatement(QueryManager
								.getST2BoCurrentBalAgent());
						stmt.setString(1, ACC_TYPE);
						rs = stmt.executeQuery();
						// This has been replaced by the obove prepared
						// statements statements 07-03-08
						// rs =
						// stmt.executeQuery(QueryManager.getST2BoCurrentBalAgent()+"'"+
						// ACC_TYPE + "'");
						int i = 0;
						while (rs.next()) {
							AGENT_ACCOUNT_CURRENT = rs
									.getDouble("current_balance");
							i++;
						}

						if (stmt != null) {
							stmt.close();
						}
						if (rs != null) {
							rs.close();
						}

						if (i == 0) {
							query1 = QueryManager.getST2InsertAgentCurrentBal();
							updatePstmt = connection.prepareStatement(query1);
							updatePstmt.setString(1, "AGENT_ACC");
							updatePstmt.setInt(2, 0);
							updatePstmt.setString(3, ACC_TYPE);
							updatePstmt.execute();
							AGENT_ACCOUNT_CURRENT = 0.0;

							if (updatePstmt != null) {
								updatePstmt.close();
							}

						}

						AGENT_ACCOUNT_CURRENT = AGENT_ACCOUNT_CURRENT + AMOUNT;
						SALE_RET_CURRENT = SALE_RET_CURRENT - AMOUNT;
						// System.out.println("AGT ACC CUR--
						// "+AGENT_ACCOUNT_CURRENT+"SALE RET ACC
						// CUR"+SALE_RET_CURRENT);
						// System.out.println(TRANSACTION_DATE);
						logger.debug("LH-BO**" + TRANSACTION_DATE + "**AMT-"
								+ AMOUNT + "**ACT_TYPE-" + ACC_TYPE
								+ "**AGT_ACC=" + AGENT_ACCOUNT_CURRENT
								+ "**SALE_RET_ACC" + SALE_RET_CURRENT);

						query2 = QueryManager.getST2InsertBoLedger();
						upPstmt = connection.prepareStatement(query2);
						upPstmt.setString(1, TRANSACTION_TYPE);
						upPstmt.setLong(2, transaction_id);
						// upPstmt.setDate(3, TRANSACTION_DATE);
						upPstmt.setTimestamp(3, TRANSACTION_DATE);
						upPstmt.setString(4, -AMOUNT + "");
						upPstmt.setString(5, "SALE_RET");
						upPstmt.setString(6, SALE_RET_CURRENT + "");
						upPstmt.setString(7, orgName);
						upPstmt.setString(8, RECEIPT_ID);
						upPstmt.execute();

						if (upPstmt != null) {
							upPstmt.close();
						}

						query1 = QueryManager.getST2InsertBoLedger();
						updatePstmt = connection.prepareStatement(query1);

						updatePstmt.setString(1, TRANSACTION_TYPE);
						updatePstmt.setLong(2, transaction_id);
						// updatePstmt.setDate(3, TRANSACTION_DATE);
						updatePstmt.setTimestamp(3, TRANSACTION_DATE);
						updatePstmt.setString(4, AMOUNT + "");
						updatePstmt.setString(5, ACC_TYPE);
						updatePstmt.setString(6, AGENT_ACCOUNT_CURRENT + "");
						updatePstmt.setString(7, orgName);
						updatePstmt.setString(8, RECEIPT_ID);
						updatePstmt.execute();

						if (updatePstmt != null) {
							updatePstmt.close();
						}

						query3 = QueryManager.getST2UpadateCurrentBalAgent();
						updPstmt = connection.prepareStatement(query3);
						updPstmt.setString(1, AGENT_ACCOUNT_CURRENT + "");
						updPstmt.setString(2, "AGENT_ACC");
						updPstmt.setString(3, ACC_TYPE);
						updPstmt.execute();

						if (updPstmt != null) {
							updPstmt.close();
						}

					} else if (TRANSACTION_TYPE.equalsIgnoreCase("chq_bounce")) {
						// System.out.println("in chq bounce");
						stmt2 = connection.prepareStatement(QueryManager
								.getST2BoChqBounce());
						stmt2.setLong(1, transaction_id);
						rs2 = stmt2.executeQuery();
						// This has been replaced by the obove prepared
						// statements statements 07-03-08
						// rs2 =
						// stmt2.executeQuery(QueryManager.getST2BoChqBounce()+""+
						// transaction_id + "");

						while (rs2.next()) {
							AMOUNT = rs2.getDouble("cheque_amt");
							// TRANSACTION_DATE =
							// rs2.getDate("transaction_date");
							TRANSACTION_DATE = rs2
									.getTimestamp("transaction_date");
							// TRANS_DATE= new
							// java.sql.Date(TRANSACTION_DATE.getTime());
							ACC_TYPE = "" + rs2.getInt("agent_org_id");
							orgName = rs2.getString("name");
						}

						if (stmt2 != null) {
							stmt2.close();
						}
						if (rs2 != null) {
							rs2.close();
						}

						// System.out.println("Amount-- "+AMOUNT+" TRANS DATE--
						// "+TRANSACTION_DATE+" ACC_TYPE-- "+ACC_TYPE+" Org NAme
						// "+orgName);
						stmt = connection.prepareStatement(QueryManager
								.getST2BoCurrentBalAgent());
						stmt.setString(1, ACC_TYPE);
						rs = stmt.executeQuery();
						// This has been replaced by the obove prepared
						// statements statements 07-03-08
						// rs =
						// stmt.executeQuery(QueryManager.getST2BoCurrentBalAgent()+"'"+
						// ACC_TYPE + "'");
						int i = 0;
						while (rs.next()) {
							AGENT_ACCOUNT_CURRENT = rs
									.getDouble("current_balance");
							i++;
						}

						if (stmt != null) {
							stmt.close();
						}
						if (rs != null) {
							rs.close();
						}

						if (i == 0) {
							query1 = QueryManager.getST2InsertAgentCurrentBal();
							updatePstmt = connection.prepareStatement(query1);
							updatePstmt.setString(1, "AGENT_ACC");
							updatePstmt.setInt(2, 0);
							updatePstmt.setString(3, ACC_TYPE);
							updatePstmt.execute();
							AGENT_ACCOUNT_CURRENT = 0.0;

							if (updatePstmt != null) {
								updatePstmt.close();
							}

						}

						AGENT_ACCOUNT_CURRENT = AGENT_ACCOUNT_CURRENT - AMOUNT;
						BANK_ACC_CURRENT = BANK_ACC_CURRENT + AMOUNT;

						// System.out.println("AGT ACC CUR--
						// "+AGENT_ACCOUNT_CURRENT+"Bank ACC
						// CUR"+BANK_ACC_CURRENT+"");
						logger.debug("LH-BO**" + TRANSACTION_DATE + "**AMT-"
								+ AMOUNT + "**ACT_TYPE-" + ACC_TYPE
								+ "**AGT_ACC=" + AGENT_ACCOUNT_CURRENT
								+ "**BANK_ACC" + BANK_ACC_CURRENT);

						query1 = QueryManager.getST2InsertBoLedger();
						updatePstmt = connection.prepareStatement(query1);

						updatePstmt.setString(1, TRANSACTION_TYPE);
						updatePstmt.setLong(2, transaction_id);
						// updatePstmt.setDate(3, TRANSACTION_DATE);
						updatePstmt.setTimestamp(3, TRANSACTION_DATE);
						updatePstmt.setString(4, -AMOUNT + "");
						updatePstmt.setString(5, ACC_TYPE);
						updatePstmt.setString(6, AGENT_ACCOUNT_CURRENT + "");
						updatePstmt.setString(7, orgName);
						updatePstmt.setString(8, RECEIPT_ID);
						updatePstmt.execute();

						if (updatePstmt != null) {
							updatePstmt.close();
						}

						query2 = QueryManager.getST2InsertBoLedger();
						upPstmt = connection.prepareStatement(query2);
						upPstmt.setString(1, TRANSACTION_TYPE);
						upPstmt.setLong(2, transaction_id);
						// upPstmt.setDate(3, TRANSACTION_DATE);
						upPstmt.setTimestamp(3, TRANSACTION_DATE);
						upPstmt.setString(4, AMOUNT + "");
						upPstmt.setString(5, "BANK_ACC");
						upPstmt.setString(6, BANK_ACC_CURRENT + "");
						upPstmt.setString(7, orgName);
						upPstmt.setString(8, RECEIPT_ID);
						upPstmt.execute();

						if (upPstmt != null) {
							upPstmt.close();
						}

						query3 = QueryManager.getST2UpadateCurrentBalAgent();
						updPstmt = connection.prepareStatement(query3);
						updPstmt.setString(1, AGENT_ACCOUNT_CURRENT + "");
						updPstmt.setString(2, "AGENT_ACC");
						updPstmt.setString(3, ACC_TYPE);
						updPstmt.execute();

						if (updPstmt != null) {
							updPstmt.close();
						}

					} else if (TRANSACTION_TYPE
							.equalsIgnoreCase("DR_NOTE_CASH")
							|| TRANSACTION_TYPE.equalsIgnoreCase("DR_NOTE")) {
						// System.out.println("in DR_NOTE_CASH ");
						stmt2 = connection.prepareStatement(QueryManager
								.getST6BoDebitNote());
						stmt2.setLong(1, transaction_id);
						rs2 = stmt2.executeQuery();
						// System.out.println("query >>>>>>>>>>>>>>" + stmt2);//
						// for
						// testing
						// This has been replaced by the obove prepared
						// statements statements 07-03-08
						// rs2 =
						// stmt2.executeQuery(QueryManager.getST2BoChqBounce()+""+
						// transaction_id + "");

						while (rs2.next()) {
							AMOUNT = rs2.getDouble("amount");
							// TRANSACTION_DATE =
							// rs2.getDate("transaction_date");
							TRANSACTION_DATE = rs2
									.getTimestamp("transaction_date");
							// TRANS_DATE= new
							// java.sql.Date(TRANSACTION_DATE.getTime());
							ACC_TYPE = "" + rs2.getInt("agent_org_id");
							orgName = rs2.getString("name");
						}
						if (stmt2 != null) {
							stmt2.close();
						}
						if (rs2 != null) {
							rs2.close();
						}
						// System.out.println("Amount-- "+AMOUNT+" TRANS DATE--
						// "+TRANSACTION_DATE+" ACC_TYPE-- "+ACC_TYPE+" Org NAme
						// "+orgName);
						stmt = connection.prepareStatement(QueryManager
								.getST2BoCurrentBalAgent());
						stmt.setString(1, ACC_TYPE);
						rs = stmt.executeQuery();
						// This has been replaced by the obove prepared
						// statements statements 07-03-08
						// rs =
						// stmt.executeQuery(QueryManager.getST2BoCurrentBalAgent()+"'"+
						// ACC_TYPE + "'");
						int i = 0;
						while (rs.next()) {
							AGENT_ACCOUNT_CURRENT = rs
									.getDouble("current_balance");
							i++;
						}

						if (stmt != null) {
							stmt.close();
						}
						if (rs != null) {
							rs.close();
						}

						if (i == 0) {
							query1 = QueryManager.getST2InsertAgentCurrentBal();
							updatePstmt = connection.prepareStatement(query1);
							updatePstmt.setString(1, "AGENT_ACC");
							updatePstmt.setInt(2, 0);
							updatePstmt.setString(3, ACC_TYPE);
							updatePstmt.execute();
							AGENT_ACCOUNT_CURRENT = 0.0;

							if (updatePstmt != null) {
								updatePstmt.close();
							}

						}

						AGENT_ACCOUNT_CURRENT = AGENT_ACCOUNT_CURRENT - AMOUNT;
						BANK_ACC_CURRENT = BANK_ACC_CURRENT + AMOUNT;

						// System.out.println("AGT ACC CUR--
						// "+AGENT_ACCOUNT_CURRENT+"Bank ACC
						// CUR"+BANK_ACC_CURRENT+"");
						logger.debug("LH-BO**" + TRANSACTION_DATE + "**AMT-"
								+ AMOUNT + "**ACT_TYPE-" + ACC_TYPE
								+ "**AGT_ACC=" + AGENT_ACCOUNT_CURRENT
								+ "**BANK_ACC" + BANK_ACC_CURRENT);

						query1 = QueryManager.getST2InsertBoLedger();
						updatePstmt = connection.prepareStatement(query1);

						updatePstmt.setString(1, TRANSACTION_TYPE);
						updatePstmt.setLong(2, transaction_id);
						// updatePstmt.setDate(3, TRANSACTION_DATE);
						updatePstmt.setTimestamp(3, TRANSACTION_DATE);
						updatePstmt.setString(4, -AMOUNT + "");
						updatePstmt.setString(5, ACC_TYPE);
						updatePstmt.setString(6, AGENT_ACCOUNT_CURRENT + "");
						updatePstmt.setString(7, orgName);
						updatePstmt.setString(8, RECEIPT_ID);
						updatePstmt.execute();

						if (updatePstmt != null) {
							updatePstmt.close();
						}

						query2 = QueryManager.getST2InsertBoLedger();
						upPstmt = connection.prepareStatement(query2);
						upPstmt.setString(1, TRANSACTION_TYPE);
						upPstmt.setLong(2, transaction_id);
						// upPstmt.setDate(3, TRANSACTION_DATE);
						upPstmt.setTimestamp(3, TRANSACTION_DATE);
						upPstmt.setString(4, AMOUNT + "");
						upPstmt.setString(5, "BANK_ACC");
						upPstmt.setString(6, BANK_ACC_CURRENT + "");
						upPstmt.setString(7, orgName);
						upPstmt.setString(8, RECEIPT_ID);
						upPstmt.execute();

						if (upPstmt != null) {
							upPstmt.close();
						}

						query3 = QueryManager.getST2UpadateCurrentBalAgent();
						updPstmt = connection.prepareStatement(query3);
						updPstmt.setString(1, AGENT_ACCOUNT_CURRENT + "");
						updPstmt.setString(2, "AGENT_ACC");
						updPstmt.setString(3, ACC_TYPE);
						updPstmt.execute();

						if (updPstmt != null) {
							updPstmt.close();
						}

					} else if (TRANSACTION_TYPE
							.equalsIgnoreCase("CR_NOTE_CASH")) {
						// System.out.println("in CR_NOTE_CASH ");
						stmt2 = connection.prepareStatement(QueryManager
								.getST6BoCreditNote());
						stmt2.setLong(1, transaction_id);
						rs2 = stmt2.executeQuery();
						// This has been replaced by the obove prepared
						// statements statements 07-03-08
						// rs2 =
						// stmt2.executeQuery(QueryManager.getST2BoChqBounce()+""+
						// transaction_id + "");

						while (rs2.next()) {
							AMOUNT = rs2.getDouble("amount");
							// TRANSACTION_DATE =
							// rs2.getDate("transaction_date");
							TRANSACTION_DATE = rs2
									.getTimestamp("transaction_date");
							// TRANS_DATE= new
							// java.sql.Date(TRANSACTION_DATE.getTime());
							ACC_TYPE = "" + rs2.getInt("agent_org_id");
							orgName = rs2.getString("name");
						}

						if (stmt2 != null) {
							stmt2.close();
						}
						if (rs2 != null) {
							rs2.close();
						}

						// System.out.println("Amount-- "+AMOUNT+" TRANS DATE--
						// "+TRANSACTION_DATE+" ACC_TYPE-- "+ACC_TYPE+" Org NAme
						// "+orgName);
						stmt = connection.prepareStatement(QueryManager
								.getST2BoCurrentBalAgent());
						stmt.setString(1, ACC_TYPE);
						rs = stmt.executeQuery();
						// This has been replaced by the obove prepared
						// statements statements 07-03-08
						// rs =
						// stmt.executeQuery(QueryManager.getST2BoCurrentBalAgent()+"'"+
						// ACC_TYPE + "'");
						int i = 0;
						while (rs.next()) {
							AGENT_ACCOUNT_CURRENT = rs
									.getDouble("current_balance");
							i++;
						}

						if (stmt != null) {
							stmt.close();
						}
						if (rs != null) {
							rs.close();
						}

						if (i == 0) {
							query1 = QueryManager.getST2InsertAgentCurrentBal();
							updatePstmt = connection.prepareStatement(query1);
							updatePstmt.setString(1, "AGENT_ACC");
							updatePstmt.setInt(2, 0);
							updatePstmt.setString(3, ACC_TYPE);
							updatePstmt.execute();
							AGENT_ACCOUNT_CURRENT = 0.0;

							if (updatePstmt != null) {
								updatePstmt.close();
							}

						}

						AGENT_ACCOUNT_CURRENT = AGENT_ACCOUNT_CURRENT + AMOUNT;
						BANK_ACC_CURRENT = BANK_ACC_CURRENT - AMOUNT;

						// System.out.println("AGT ACC CUR--
						// "+AGENT_ACCOUNT_CURRENT+"Bank ACC
						// CUR"+BANK_ACC_CURRENT+"");
						logger.debug("LH-BO**" + TRANSACTION_DATE + "**AMT-"
								+ AMOUNT + "**ACT_TYPE-" + ACC_TYPE
								+ "**AGT_ACC=" + AGENT_ACCOUNT_CURRENT
								+ "**BANK_ACC" + BANK_ACC_CURRENT);

						query2 = QueryManager.getST2InsertBoLedger();
						upPstmt = connection.prepareStatement(query2);
						upPstmt.setString(1, TRANSACTION_TYPE);
						upPstmt.setLong(2, transaction_id);
						// upPstmt.setDate(3, TRANSACTION_DATE);
						upPstmt.setTimestamp(3, TRANSACTION_DATE);
						upPstmt.setString(4, -AMOUNT + "");
						upPstmt.setString(5, "BANK_ACC");
						upPstmt.setString(6, BANK_ACC_CURRENT + "");
						upPstmt.setString(7, orgName);
						upPstmt.setString(8, RECEIPT_ID);
						upPstmt.execute();

						if (upPstmt != null) {
							upPstmt.close();
						}

						query1 = QueryManager.getST2InsertBoLedger();
						updatePstmt = connection.prepareStatement(query1);

						updatePstmt.setString(1, TRANSACTION_TYPE);
						updatePstmt.setLong(2, transaction_id);
						// updatePstmt.setDate(3, TRANSACTION_DATE);
						updatePstmt.setTimestamp(3, TRANSACTION_DATE);
						updatePstmt.setString(4, AMOUNT + "");
						updatePstmt.setString(5, ACC_TYPE);
						updatePstmt.setString(6, AGENT_ACCOUNT_CURRENT + "");
						updatePstmt.setString(7, orgName);
						updatePstmt.setString(8, RECEIPT_ID);
						updatePstmt.execute();

						if (updatePstmt != null) {
							updatePstmt.close();
						}

						query3 = QueryManager.getST2UpadateCurrentBalAgent();
						updPstmt = connection.prepareStatement(query3);
						updPstmt.setString(1, AGENT_ACCOUNT_CURRENT + "");
						updPstmt.setString(2, "AGENT_ACC");
						updPstmt.setString(3, ACC_TYPE);
						updPstmt.execute();

						if (updPstmt != null) {
							updPstmt.close();
						}

					} else if (TRANSACTION_TYPE.equalsIgnoreCase("pwt_plr")
							|| TRANSACTION_TYPE.equalsIgnoreCase("dg_pwt_plr")) {

						double PWT_AMT = 0;
						double TAX_AMT = 0;
						double NET_AMT = 0;
						String directPwtPlrQry = "";
						if (TRANSACTION_TYPE.equalsIgnoreCase("dg_pwt_plr")) {
							directPwtPlrQry = QueryManager.getST2BoDGPwtPlr();
						} else {
							directPwtPlrQry = QueryManager.getST2BoPwtPlr();
						}
						stmt = connection.prepareStatement(directPwtPlrQry);
						stmt.setLong(1, transaction_id);
						rs = stmt.executeQuery();
						// This has been replaced by the obove prepared
						// statements statements 07-03-08
						// rs =
						// stmt.executeQuery(QueryManager.getST2BoPwtPlr()+""+
						// transaction_id + "");
						while (rs.next()) {
							PWT_AMT = rs.getDouble("pwt_amt");
							TAX_AMT = rs.getDouble("tax_amt");
							NET_AMT = rs.getDouble("net_amt");
							TRANSACTION_TYPE = rs.getString("transaction_type");
							// TRANSACTION_DATE =
							// rs.getDate("transaction_date");
							TRANSACTION_DATE = rs
									.getTimestamp("transaction_date");
							orgName = rs.getString("first_name");
						}

						logger.debug("LH-BO**" + TRANSACTION_DATE + "**AMT-"
								+ AMOUNT + "**ACT_TYPE-" + ACC_TYPE + "**"
								+ PLAYER_CASH_CURRENT + " PLAYER_TDS_CURRENT ="
								+ PLAYER_TDS_CURRENT + "PLAYER_PWT_CURRENT ="
								+ PLAYER_PWT_CURRENT);
						logger.debug("In PWT_PAY_CURRENT is ="
								+ FormatNumber.formatNumber(PWT_PAY_CURRENT)
								+ " TDS_PAY_CURRENT = " + TDS_PAY_CURRENT
								+ "BANK_ACC_CURRENT ="
								+ FormatNumber.formatNumber(BANK_ACC_CURRENT));

						stmt
								.executeUpdate("insert into st_lms_bo_ledger (transaction_type, transaction_id,transaction_date,amount,account_type,balance,transaction_with,receipt_id) values ('"
										+ TRANSACTION_TYPE
										+ "',"
										+ transaction_id
										+ ",'"
										+ TRANSACTION_DATE
										+ "',"
										+ (-PWT_AMT + "")
										+ ",'PWT_PAY',"
										+ FormatNumber
												.formatNumber(PWT_PAY_CURRENT)
										+ ", '"
										+ orgName
										+ "','"
										+ RECEIPT_ID
										+ "')");

						stmt
								.executeUpdate("insert into st_lms_bo_ledger (transaction_type, transaction_id,transaction_date,amount,account_type,balance,transaction_with,receipt_id) values ('"
										+ TRANSACTION_TYPE
										+ "',"
										+ transaction_id
										+ ",'"
										+ TRANSACTION_DATE
										+ "',"
										+ FormatNumber.formatNumber(PWT_AMT)
										+ ",'PLAYER_PWT',"
										+ FormatNumber
												.formatNumber(PLAYER_PWT_CURRENT)
										+ ", '"
										+ orgName
										+ "','"
										+ RECEIPT_ID
										+ "')");

						stmt
								.executeUpdate("insert into st_lms_bo_ledger (transaction_type, transaction_id,transaction_date,amount,account_type,balance,transaction_with,receipt_id) values ('"
										+ TRANSACTION_TYPE
										+ "',"
										+ transaction_id
										+ ",'"
										+ TRANSACTION_DATE
										+ "',"
										+ (-TAX_AMT + "")
										+ ",'PLAYER_TDS',"
										+ FormatNumber
												.formatNumber(PLAYER_TDS_CURRENT)
										+ ", '"
										+ orgName
										+ "','"
										+ RECEIPT_ID
										+ "')");
						stmt
								.executeUpdate("insert into st_lms_bo_ledger (transaction_type, transaction_id,transaction_date,amount,account_type,balance,transaction_with,receipt_id) values ('"
										+ TRANSACTION_TYPE
										+ "',"
										+ transaction_id
										+ ",'"
										+ TRANSACTION_DATE
										+ "',"
										+ TAX_AMT
										+ ""
										+ ",'TDS_PAY',"
										+ FormatNumber
												.formatNumber(TDS_PAY_CURRENT)
										+ ", '"
										+ orgName
										+ "','"
										+ RECEIPT_ID
										+ "' )");

						stmt
								.executeUpdate("insert into st_lms_bo_ledger (transaction_type, transaction_id,transaction_date,amount,account_type,balance,transaction_with,receipt_id) values ('"
										+ TRANSACTION_TYPE
										+ "',"
										+ transaction_id
										+ ",'"
										+ TRANSACTION_DATE
										+ "',"
										+ (-NET_AMT + "")
										+ ",'PLAYER_CAS',"
										+ FormatNumber
												.formatNumber(PLAYER_CASH_CURRENT)
										+ ", '"
										+ orgName
										+ "','"
										+ RECEIPT_ID
										+ "' )");

						stmt
								.executeUpdate("insert into st_lms_bo_ledger (transaction_type, transaction_id,transaction_date,amount,account_type,balance,transaction_with,receipt_id) values ('"
										+ TRANSACTION_TYPE
										+ "',"
										+ transaction_id
										+ ",'"
										+ TRANSACTION_DATE
										+ "',"
										+ FormatNumber.formatNumber(NET_AMT)
										+ ",'BANK_ACC',"
										+ FormatNumber
												.formatNumber(BANK_ACC_CURRENT)
										+ ", '"
										+ orgName
										+ "','"
										+ RECEIPT_ID
										+ "' )");

						if (stmt != null) {
							stmt.close();
						}
						if (rs != null) {
							rs.close();
						}

					}

					else if (TRANSACTION_TYPE.equalsIgnoreCase("tds")) {
						// System.out.println("in TDS");
						stmt2 = connection.prepareStatement(QueryManager
								.getST2BoTds());
						stmt2.setLong(1, transaction_id);
						rs2 = stmt2.executeQuery();
						// This has been replaced by the obove prepared
						// statements statements 07-03-08
						// rs2 =
						// stmt2.executeQuery(QueryManager.getST2BoTds()+""+
						// transaction_id + "");

						while (rs2.next()) {
							AMOUNT = rs2.getDouble("amount");
							// TRANSACTION_DATE =
							// rs2.getDate("transaction_date");
							TRANSACTION_DATE = rs2
									.getTimestamp("transaction_date");
							ACC_TYPE = "TDS_PAY";
						}

						if (stmt2 != null) {
							stmt2.close();
						}
						if (rs2 != null) {
							rs2.close();
						}

						// System.out.println("Amount-- "+AMOUNT+" TRANS DATE--
						// "+TRANSACTION_DATE+" ACC_TYPE-- "+ACC_TYPE);

						TDS_PAY_CURRENT = TDS_PAY_CURRENT - AMOUNT;
						BANK_ACC_CURRENT = BANK_ACC_CURRENT + AMOUNT;
						// System.out.println("TDS ACC CUR--
						// "+TDS_PAY_CURRENT+"Bank ACC
						// CUR"+BANK_ACC_CURRENT+"");
						logger.debug("LH-BO**" + TRANSACTION_DATE + "**AMT-"
								+ AMOUNT + "**ACT_TYPE-" + ACC_TYPE
								+ "**TDS_ACC=" + TDS_PAY_CURRENT + "**BANK_ACC"
								+ BANK_ACC_CURRENT);

						query1 = QueryManager.getST2InsertBoLedger();
						updatePstmt = connection.prepareStatement(query1);

						updatePstmt.setString(1, TRANSACTION_TYPE);
						updatePstmt.setLong(2, transaction_id);
						// updatePstmt.setDate(3, TRANSACTION_DATE);
						updatePstmt.setTimestamp(3, TRANSACTION_DATE);
						updatePstmt.setString(4, -AMOUNT + "");
						updatePstmt.setString(5, ACC_TYPE);
						updatePstmt.setString(6, TDS_PAY_CURRENT + "");
						updatePstmt.setString(7, "GOVERNMENT");
						updatePstmt.setString(8, RECEIPT_ID);

						updatePstmt.execute();

						if (updatePstmt != null) {
							updatePstmt.close();
						}

						query2 = QueryManager.getST2InsertBoLedger();
						upPstmt = connection.prepareStatement(query2);
						upPstmt.setString(1, TRANSACTION_TYPE);
						upPstmt.setLong(2, transaction_id);
						// upPstmt.setDate(3, TRANSACTION_DATE);
						upPstmt.setTimestamp(3, TRANSACTION_DATE);
						upPstmt.setString(4, AMOUNT + "");
						upPstmt.setString(5, "BANK_ACC");
						upPstmt.setString(6, BANK_ACC_CURRENT + "");
						upPstmt.setString(7, "GOVERNMENT");
						upPstmt.setString(8, RECEIPT_ID);
						upPstmt.execute();

						if (upPstmt != null) {
							upPstmt.close();
						}

					} else if (TRANSACTION_TYPE.equalsIgnoreCase("govt_comm")) {
						// System.out.println("in Govt COmm");
						stmt2 = connection.prepareStatement(QueryManager
								.getST2BoGovtComm());
						stmt2.setLong(1, transaction_id);
						rs2 = stmt2.executeQuery();
						// This has been replaced by the obove prepared
						// statements statements 07-03-08
						// rs2 =
						// stmt2.executeQuery(QueryManager.getST2BoGovtComm()+""+
						// transaction_id + "");

						while (rs2.next()) {
							AMOUNT = rs2.getDouble("amount");
							// TRANSACTION_DATE =
							// rs2.getDate("transaction_date");
							TRANSACTION_DATE = rs2
									.getTimestamp("transaction_date");
							ACC_TYPE = "GOVT_COMM";
						}

						if (stmt2 != null) {
							stmt2.close();
						}
						if (rs2 != null) {
							rs2.close();
						}

						// System.out.println("Amount-- "+AMOUNT+" TRANS DATE--
						// "+TRANSACTION_DATE+" ACC_TYPE-- "+ACC_TYPE);

						GOVT_COMM_CURRENT = GOVT_COMM_CURRENT - AMOUNT;
						BANK_ACC_CURRENT = BANK_ACC_CURRENT + AMOUNT;
						// System.out.println("GOVT ACC CUR--
						// "+GOVT_COMM_CURRENT+"Bank ACC
						// CUR"+BANK_ACC_CURRENT+"");
						logger.debug("LH-BO**" + TRANSACTION_DATE + "**AMT-"
								+ AMOUNT + "**ACT_TYPE-" + ACC_TYPE
								+ "**GOVT_ACC=" + GOVT_COMM_CURRENT
								+ "**BANK_ACC" + BANK_ACC_CURRENT);

						query1 = QueryManager.getST2InsertBoLedger();
						updatePstmt = connection.prepareStatement(query1);

						updatePstmt.setString(1, TRANSACTION_TYPE);
						updatePstmt.setLong(2, transaction_id);
						// updatePstmt.setDate(3, TRANSACTION_DATE);
						updatePstmt.setTimestamp(3, TRANSACTION_DATE);
						updatePstmt.setString(4, -AMOUNT + "");
						updatePstmt.setString(5, ACC_TYPE);
						updatePstmt.setString(6, GOVT_COMM_CURRENT + "");
						updatePstmt.setString(7, "GOVERNMENT");
						updatePstmt.setString(8, RECEIPT_ID);
						updatePstmt.execute();

						if (updatePstmt != null) {
							updatePstmt.close();
						}

						query2 = QueryManager.getST2InsertBoLedger();
						upPstmt = connection.prepareStatement(query2);
						upPstmt.setString(1, TRANSACTION_TYPE);
						upPstmt.setLong(2, transaction_id);
						// upPstmt.setDate(3, TRANSACTION_DATE);
						upPstmt.setTimestamp(3, TRANSACTION_DATE);
						upPstmt.setString(4, AMOUNT + "");
						upPstmt.setString(5, "BANK_ACC");
						upPstmt.setString(6, BANK_ACC_CURRENT + "");
						upPstmt.setString(7, "GOVERNMENT");
						upPstmt.setString(8, RECEIPT_ID);
						upPstmt.execute();

						if (upPstmt != null) {
							upPstmt.close();
						}

					}
					/*
					 * added on 7-12-2008 on Abhisheks recommendations
					 */
					else if (TRANSACTION_TYPE.equalsIgnoreCase("vat")) {

						// System.out.println("in VAT");
						stmt2 = connection.prepareStatement(QueryManager
								.getST6BoVATPayable());
						stmt2.setLong(1, transaction_id);
						rs2 = stmt2.executeQuery();
						// This has been replaced by the obove prepared
						// statements statements 07-03-08
						// rs2 =
						// stmt2.executeQuery(QueryManager.getST2BoGovtComm()+""+
						// transaction_id + "");

						while (rs2.next()) {
							AMOUNT = rs2.getDouble("amount");
							// TRANSACTION_DATE =
							// rs2.getDate("transaction_date");
							TRANSACTION_DATE = rs2
									.getTimestamp("transaction_date");
							ACC_TYPE = "VAT_PAY";
						}

						if (stmt2 != null) {
							stmt2.close();
						}
						if (rs2 != null) {
							rs2.close();
						}

						// System.out.println("Amount-- "+AMOUNT+" TRANS DATE--
						// "+TRANSACTION_DATE+" ACC_TYPE-- "+ACC_TYPE);

						VAT_PAY_CURRENT = VAT_PAY_CURRENT - AMOUNT;
						BANK_ACC_CURRENT = BANK_ACC_CURRENT + AMOUNT;
						// System.out.println("VAT_PAY_CURRENT--
						// "+VAT_PAY_CURRENT+"Bank ACC
						// CUR"+BANK_ACC_CURRENT+"");
						logger.debug("LH-BO**" + TRANSACTION_DATE + "**AMT-"
								+ AMOUNT + "**ACT_TYPE-" + ACC_TYPE
								+ "**VAT_ACC=" + VAT_PAY_CURRENT + "**BANK_ACC"
								+ BANK_ACC_CURRENT);

						query1 = QueryManager.getST2InsertBoLedger();
						updatePstmt = connection.prepareStatement(query1);

						updatePstmt.setString(1, TRANSACTION_TYPE);
						updatePstmt.setLong(2, transaction_id);
						// updatePstmt.setDate(3, TRANSACTION_DATE);
						updatePstmt.setTimestamp(3, TRANSACTION_DATE);
						updatePstmt.setString(4, -AMOUNT + "");
						updatePstmt.setString(5, ACC_TYPE);
						updatePstmt.setString(6, VAT_PAY_CURRENT + "");
						updatePstmt.setString(7, "GOVERNMENT");
						updatePstmt.setString(8, RECEIPT_ID);
						updatePstmt.execute();

						if (updatePstmt != null) {
							updatePstmt.close();
						}

						query2 = QueryManager.getST2InsertBoLedger();
						upPstmt = connection.prepareStatement(query2);
						upPstmt.setString(1, TRANSACTION_TYPE);
						upPstmt.setLong(2, transaction_id);
						// upPstmt.setDate(3, TRANSACTION_DATE);
						upPstmt.setTimestamp(3, TRANSACTION_DATE);
						upPstmt.setString(4, AMOUNT + "");
						upPstmt.setString(5, "BANK_ACC");
						upPstmt.setString(6, BANK_ACC_CURRENT + "");
						upPstmt.setString(7, "GOVERNMENT");
						upPstmt.setString(8, RECEIPT_ID);
						upPstmt.execute();

						if (upPstmt != null) {
							upPstmt.close();
						}

					}

					else if (TRANSACTION_TYPE.equalsIgnoreCase("unclm_pwt")) {
						// System.out.println("in UNCLM PWT");
						stmt2 = connection.prepareStatement(QueryManager
								.getST2BoUnclmPwt());
						stmt2.setLong(1, transaction_id);
						rs2 = stmt2.executeQuery();
						// This has been replaced by the obove prepared
						// statements statements 07-03-08
						// rs2 =
						// stmt2.executeQuery(QueryManager.getST2BoUnclmPwt()+""+
						// transaction_id + "");

						while (rs2.next()) {
							AMOUNT = rs2.getDouble("amount");
							// TRANSACTION_DATE =
							// rs2.getDate("transaction_date");
							TRANSACTION_DATE = rs2
									.getTimestamp("transaction_date");
							ACC_TYPE = "UNCLM_GOVT";
						}

						if (stmt2 != null) {
							stmt2.close();
						}
						if (rs2 != null) {
							rs2.close();
						}
						// System.out.println("Amount-- "+AMOUNT+" TRANS DATE--
						// "+TRANSACTION_DATE+" ACC_TYPE-- "+ACC_TYPE);

						UNCLM_GOVT_CURRENT = UNCLM_GOVT_CURRENT - AMOUNT;
						BANK_ACC_CURRENT = BANK_ACC_CURRENT + AMOUNT;
						// System.out.println("UNCLM ACC CUR--
						// "+UNCLM_GOVT_CURRENT+"Bank ACC
						// CUR"+BANK_ACC_CURRENT+"");
						logger.debug("LH-BO**" + TRANSACTION_DATE + "**AMT-"
								+ AMOUNT + "**ACT_TYPE-" + ACC_TYPE
								+ "**UNCLM_GOVT_ACC=" + UNCLM_GOVT_CURRENT
								+ "**BANK_ACC" + BANK_ACC_CURRENT);

						query1 = QueryManager.getST2InsertBoLedger();
						updatePstmt = connection.prepareStatement(query1);

						updatePstmt.setString(1, TRANSACTION_TYPE);
						updatePstmt.setLong(2, transaction_id);
						// updatePstmt.setDate(3, TRANSACTION_DATE);
						updatePstmt.setTimestamp(3, TRANSACTION_DATE);
						updatePstmt.setString(4, -AMOUNT + "");
						updatePstmt.setString(5, ACC_TYPE);
						updatePstmt.setString(6, UNCLM_GOVT_CURRENT + "");
						updatePstmt.setString(7, "GOVERNMENT");
						updatePstmt.setString(8, RECEIPT_ID);
						updatePstmt.execute();

						if (updatePstmt != null) {
							updatePstmt.close();
						}

						query2 = QueryManager.getST2InsertBoLedger();
						upPstmt = connection.prepareStatement(query2);
						upPstmt.setString(1, TRANSACTION_TYPE);
						upPstmt.setLong(2, transaction_id);
						// upPstmt.setDate(3, TRANSACTION_DATE);
						upPstmt.setTimestamp(3, TRANSACTION_DATE);
						upPstmt.setString(4, AMOUNT + "");
						upPstmt.setString(5, "BANK_ACC");
						upPstmt.setString(6, BANK_ACC_CURRENT + "");
						upPstmt.setString(7, "GOVERNMENT");
						upPstmt.setString(8, RECEIPT_ID);
						upPstmt.execute();

						if (upPstmt != null) {
							upPstmt.close();
						}

					} else if (TRANSACTION_TYPE.equalsIgnoreCase("pwt")
							|| TRANSACTION_TYPE.equalsIgnoreCase("pwt_auto")
							|| TRANSACTION_TYPE.equalsIgnoreCase("DG_PWT")
							|| TRANSACTION_TYPE.equalsIgnoreCase("DG_PWT_AUTO") || TRANSACTION_TYPE.equalsIgnoreCase("OLA_WITHDRAWL")) {
						// System.out.println("in PWT");
						// System.out.println("in pwtttttt");
						String pwtQuery = "";
						double PWT_CHARGES_AMOUNT = 0.0;
						if (TRANSACTION_TYPE.equalsIgnoreCase("DG_PWT")
								|| TRANSACTION_TYPE
										.equalsIgnoreCase("DG_PWT_AUTO")) {
							pwtQuery = QueryManager.getST2BoDrGmPwtPay();
							// System.out.println("getST2BoDrGmPwtPay " +
							// pwtQuery);
						} 
						else if(TRANSACTION_TYPE.equalsIgnoreCase("OLA_WITHDRAWL"))
							{
							pwtQuery =  QueryManager.getST2BoOlaWithdrawl();
							}else {
							pwtQuery = QueryManager.getST2BoPwtPay();
							// System.out.println("getST2BoPwtPay " + pwtQuery);
						}
						stmt2 = connection.prepareStatement(pwtQuery);
						stmt2.setLong(1, transaction_id);
						rs2 = stmt2.executeQuery();
						// This has been replaced by the obove prepared
						// statements statements 07-03-08
						// rs2 =
						// stmt2.executeQuery(QueryManager.getST2BoPwtPay()+""+
						// transaction_id + "");
						while (rs2.next()) {
							AMOUNT = rs2.getDouble("pwt_amount");
							PWT_CHARGES_AMOUNT = rs2.getDouble("comm_amount");
							// TRANSACTION_DATE =
							// rs2.getDate("transaction_date");
							TRANSACTION_DATE = rs2
									.getTimestamp("transaction_date");
							ACC_TYPE = "" + rs2.getInt("agent_org_id");
							orgName = rs2.getString("name");
						}

						if (stmt2 != null) {
							stmt2.close();
						}
						if (rs2 != null) {
							rs2.close();
						}

						// System.out.println("Amount-- "+AMOUNT+" TRANS DATE--
						// "+TRANSACTION_DATE+" ACC_TYPE-- "+ACC_TYPE +"
						// PWT_CHARGES_AMOUNT "+PWT_CHARGES_AMOUNT+"");

						stmt = connection.prepareStatement(QueryManager
								.getST2BoCurrentBalAgent());
						stmt.setString(1, ACC_TYPE);
						// System.out.println("gggggggggggggggggggg");
						rs = stmt.executeQuery();
						// System.out.println("ffffffffffffffffffffffff");
						// This has been replaced by the obove prepared
						// statements statements 07-03-08
						// rs =
						// stmt.executeQuery(QueryManager.getST2BoCurrentBalAgent()+"
						// '"+ ACC_TYPE + "'");
						int i = 0;
						while (rs.next()) {
							AGENT_ACCOUNT_CURRENT = rs
									.getDouble("current_balance");
							i++;
						}

						if (stmt != null) {
							stmt.close();
						}
						if (rs != null) {
							rs.close();
						}

						if (i == 0) {
							query1 = QueryManager.getST2InsertAgentCurrentBal();
							updatePstmt = connection.prepareStatement(query1);
							updatePstmt.setString(1, "AGENT_ACC");
							updatePstmt.setInt(2, 0);
							updatePstmt.setString(3, ACC_TYPE);
							// System.out.println("sdsdsdsd");
							// System.out.println("query is : " + updatePstmt +
							// "agent id is " + ACC_TYPE);
							updatePstmt.execute();
							// System.out.println("hhhhhhhhh");
							AGENT_ACCOUNT_CURRENT = 0.0;

							if (updatePstmt != null) {
								updatePstmt.close();
							}

						}

						AGENT_ACCOUNT_CURRENT = AGENT_ACCOUNT_CURRENT + AMOUNT;
						PWT_PAY_CURRENT = PWT_PAY_CURRENT - AMOUNT;
						// System.out.println("In BoLedger trans_id is =
						// "+transaction_id+" tratype = "+TRANSACTION_TYPE+
						// "TRANSACTION_DATE = "+TRANSACTION_DATE+" AGENT
						// curr="+AGENT_ACCOUNT_CURRENT+"");
						// System.out.println("PWT ACC CUR--
						// "+PWT_PAY_CURRENT+"AGENT ACC
						// CUR"+AGENT_ACCOUNT_CURRENT+"");
						logger.debug("LH-BO**" + TRANSACTION_DATE + "**AMT-"
								+ AMOUNT + "**ACT_TYPE-" + ACC_TYPE
								+ "**AGT_ACC=" + AGENT_ACCOUNT_CURRENT
								+ "**PWT_ACC" + PWT_PAY_CURRENT);

						query2 = QueryManager.getST2InsertBoLedger();
						upPstmt = connection.prepareStatement(query2);
						upPstmt.setString(1, TRANSACTION_TYPE);
						upPstmt.setLong(2, transaction_id);
						// upPstmt.setDate(3, TRANSACTION_DATE);
						upPstmt.setTimestamp(3, TRANSACTION_DATE);
						upPstmt.setString(4, -AMOUNT + "");
						upPstmt.setString(5, "PWT_PAY");
						upPstmt.setString(6, PWT_PAY_CURRENT + "");
						upPstmt.setString(7, orgName);
						upPstmt.setString(8, RECEIPT_ID);
						upPstmt.execute();

						if (upPstmt != null) {
							upPstmt.close();
						}

						query1 = QueryManager.getST2InsertBoLedger();
						updatePstmt = connection.prepareStatement(query1);

						updatePstmt.setString(1, TRANSACTION_TYPE);
						updatePstmt.setLong(2, transaction_id);
						// updatePstmt.setDate(3, TRANSACTION_DATE);
						updatePstmt.setTimestamp(3, TRANSACTION_DATE);
						updatePstmt.setString(4, AMOUNT + "");
						updatePstmt.setString(5, ACC_TYPE);
						updatePstmt.setString(6, AGENT_ACCOUNT_CURRENT + "");
						updatePstmt.setString(7, orgName);
						updatePstmt.setString(8, RECEIPT_ID);
						updatePstmt.execute();

						if (updatePstmt != null) {
							updatePstmt.close();
						}

						query3 = QueryManager.getST2UpadateCurrentBalAgent();
						updPstmt = connection.prepareStatement(query3);
						updPstmt.setString(1, AGENT_ACCOUNT_CURRENT + "");
						updPstmt.setString(2, "AGENT_ACC");
						updPstmt.setString(3, ACC_TYPE);
						updPstmt.execute();

						if (updPstmt != null) {
							updPstmt.close();
						}

						/*
						 * moved below after changes for collection charges
						 * 
						 * query3 = QueryManager.getST2UpadateCurrentBalAgent();
						 * updPstmt=connection.prepareStatement(query3);
						 * updPstmt.setString(1, AGENT_ACCOUNT_CURRENT+"");
						 * updPstmt.setString(2, "AGENT_ACC");
						 * updPstmt.setString(3, ACC_TYPE); updPstmt.execute();
						 */

						/*
						 * added for pwt collection charges 08-12-2008
						 */
						AGENT_ACCOUNT_CURRENT = AGENT_ACCOUNT_CURRENT
								+ PWT_CHARGES_AMOUNT;
						PWT_CHARGES_CURRENT = PWT_CHARGES_CURRENT
								- PWT_CHARGES_AMOUNT;
						// System.out.println("In BoLedger trans_id is =
						// "+transaction_id+" tratype = "+TRANSACTION_TYPE+
						// "TRANSACTION_DATE = "+TRANSACTION_DATE+" AGENT
						// curr="+AGENT_ACCOUNT_CURRENT+"");
						// System.out.println("PWT_CHARGES_CURRENT--
						// "+PWT_CHARGES_CURRENT+"AGENT ACC
						// CUR"+AGENT_ACCOUNT_CURRENT+"");

						if (PWT_CHARGES_AMOUNT != 0.0) {
							query2 = QueryManager.getST2InsertBoLedger();
							upPstmt = connection.prepareStatement(query2);
							upPstmt.setString(1, TRANSACTION_TYPE);
							upPstmt.setLong(2, transaction_id);
							// upPstmt.setDate(3, TRANSACTION_DATE);
							upPstmt.setTimestamp(3, TRANSACTION_DATE);
							upPstmt.setString(4, -PWT_CHARGES_AMOUNT + "");
							upPstmt.setString(5, "PWT_CHARGES");
							upPstmt.setString(6, PWT_CHARGES_CURRENT + "");
							upPstmt.setString(7, orgName);
							upPstmt.setString(8, RECEIPT_ID);
							upPstmt.execute();

							if (upPstmt != null) {
								upPstmt.close();
							}

							query1 = QueryManager.getST2InsertBoLedger();
							updatePstmt = connection.prepareStatement(query1);

							updatePstmt.setString(1, TRANSACTION_TYPE);
							updatePstmt.setLong(2, transaction_id);
							// updatePstmt.setDate(3, TRANSACTION_DATE);
							updatePstmt.setTimestamp(3, TRANSACTION_DATE);
							updatePstmt.setString(4, PWT_CHARGES_AMOUNT + "");
							updatePstmt.setString(5, ACC_TYPE + "#");
							updatePstmt
									.setString(6, AGENT_ACCOUNT_CURRENT + "");
							updatePstmt.setString(7, orgName);
							updatePstmt.setString(8, RECEIPT_ID);
							updatePstmt.execute();

							if (updatePstmt != null) {
								updatePstmt.close();
							}

						}

						query3 = QueryManager.getST2UpadateCurrentBalAgent();
						updPstmt = connection.prepareStatement(query3);
						updPstmt.setString(1, AGENT_ACCOUNT_CURRENT + "");
						updPstmt.setString(2, "AGENT_ACC");
						updPstmt.setString(3, ACC_TYPE);
						updPstmt.execute();

						if (updPstmt != null) {
							updPstmt.close();
						}

						/*
						 * end pwt collection charges
						 */
					}
				}

				// System.out.println("BANK ACC CUR-- "+BANK_ACC_CURRENT+"");
				// System.out.println("TDS ACC CUR-- "+TDS_PAY_CURRENT+"");
				// System.out.println("SALE ACC CUR-- "+SALE_ACC_CURRENT+"");
				// System.out.println("SAle ret ACC CUR-- "+SALE_RET_CURRENT);
				// System.out.println("PWT ACC CUR-- "+PWT_PAY_CURRENT);
				// System.out.println("UNCLM ACC CUR-- "+UNCLM_GOVT_CURRENT);
				// System.out.println("GOVT ACC CUR-- "+GOVT_COMM_CURRENT);
				// System.out.println("VAT_PAY_CURRENT-- "+VAT_PAY_CURRENT);
				// System.out.println("AGENT ACC CUR--
				// "+AGENT_ACCOUNT_CURRENT+"");
				// System.out.println("PWT_CHARGES_CURRENT--
				// "+PWT_CHARGES_CURRENT);

				query3 = QueryManager.getST2UpdateCurrentBal();
				updPstmt = connection.prepareStatement(query3);

				updPstmt.setString(1, BANK_ACC_CURRENT + "");
				updPstmt.setString(2, "BANK_ACC");
				updPstmt.execute();
				updPstmt.setString(1, TDS_PAY_CURRENT + "");
				updPstmt.setString(2, "TDS_PAY");
				updPstmt.execute();
				updPstmt.setString(1, SALE_ACC_CURRENT + "");
				updPstmt.setString(2, "SALE_ACC");
				updPstmt.execute();

				updPstmt.setString(1, SALE_RET_CURRENT + "");
				updPstmt.setString(2, "SALE_RET");
				updPstmt.execute();
				updPstmt.setString(1, PWT_PAY_CURRENT + "");
				updPstmt.setString(2, "PWT_PAY");
				updPstmt.execute();

				updPstmt.setString(1, UNCLM_GOVT_CURRENT + "");
				updPstmt.setString(2, "UNCLM_GOVT");
				updPstmt.execute();
				updPstmt.setString(1, GOVT_COMM_CURRENT + "");
				updPstmt.setString(2, "GOVT_COMM");
				updPstmt.execute();

				/*
				 * added on 07-12-2008 on abhishekhs recommendation
				 */

				updPstmt.setString(1, VAT_PAY_CURRENT + "");
				updPstmt.setString(2, "VAT_PAY");
				updPstmt.execute();

				/*
				 * for collection charges
				 */
				updPstmt.setString(1, PWT_CHARGES_CURRENT + "");
				updPstmt.setString(2, "PWT_CHARGES");
				updPstmt.execute();

				/*
				 */
				updPstmt.setString(1, PLAYER_TDS_CURRENT + "");
				updPstmt.setString(2, "PLAYER_TDS");
				updPstmt.execute();

				updPstmt.setString(1, PLAYER_PWT_CURRENT + "");
				updPstmt.setString(2, "PLAYER_PWT");
				updPstmt.execute();

				updPstmt.setString(1, PLAYER_CASH_CURRENT + "");
				updPstmt.setString(2, "PLAYER_CAS");
				updPstmt.execute();

				if (updPstmt != null) {
					updPstmt.close();
				}
				insBalHistory("bo", connection, toDate_Bean);
			}

			connection.commit();
		} catch (SQLException e) {

			try {
				System.out.println("In BO Exception " + e);
				connection.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
			throw new LMSException(e);
		}

		finally {

			if (connection != null) {
				try {
					if (rs1 != null) {
						rs1.close();
					}
					connection.close();

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	private Timestamp onAdhocUpdateLedger(String tier) {
		Connection connection = DBConnect.getConnection();
		Timestamp trxDate = null;
		Timestamp ledgerDate = null;
		Timestamp deleteTS = null;
		String trxWith = null;
		String tableName = tier;
		long trxId = 0;
		try {
			connection.setAutoCommit(false);
			stmt = connection
					.prepareStatement("select transaction_date,transaction_id,transaction_with from st_lms_"
							+ tier
							+ "_ledger order by transaction_date desc limit 1");
			rs = stmt.executeQuery();
			if (rs.next()) {
				ledgerDate = rs.getTimestamp("transaction_date");
				trxId = rs.getLong("transaction_id");
				trxWith = rs.getString("transaction_with");

				logger.debug("In Ledger Helper**tier " + tier
						+ "**Selecting ledger Date**" + trxId + "---TrxID"
						+ stmt);
				if (trxWith.equals("BO")) {
					tableName = "bo";
				}
				stmt = connection
						.prepareStatement("select transaction_date from st_lms_"
								+ tableName
								+ "_transaction_master where transaction_id>"
								+ trxId
								+ " order by transaction_date asc limit 1");
				logger.debug("In Ledger Helper**tier Agent****" + stmt);
				rs = stmt.executeQuery();
				if (rs.next()) {
					trxDate = rs.getTimestamp("transaction_date");
				}

				logger.debug("Tier*****" + tier
						+ "Date in Transaction Master***" + trxDate
						+ "***Date in Ledger*****" + ledgerDate);
				if (trxDate != null && ledgerDate != null
						&& trxDate.before(ledgerDate)) {
					Date deleteFromDate = new java.sql.Date(trxDate.getTime());
					deleteTS = trxDate;

					stmt = connection
							.prepareStatement("select distinct(transaction_date) from st_lms_"
									+ tier
									+ "_current_balance_history where transaction_date<'"
									+ deleteFromDate
									+ " 00:00:00' order by transaction_date desc");
					rs = stmt.executeQuery();
					while (rs.next()) {
						trxDate = rs.getTimestamp("transaction_date");
						if (deleteFromDate.after(rs
								.getTimestamp("transaction_date"))) {
							deleteTS = rs.getTimestamp("transaction_date");
							deleteFromDate = new java.sql.Date(deleteTS
									.getTime());
							break;
						}
					}
					logger.debug("In Ledger Helper Deleting data from*******"
							+ deleteTS);
				}
			}
			connection.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(connection);
		}
		return deleteTS;
	}

	private static void insBalHistory(String tier, Connection connection,
			Timestamp toDate_Bean) throws SQLException {
		PreparedStatement stmt = connection
				.prepareStatement("delete from st_lms_"
						+ tier
						+ "_current_balance_history where date(transaction_date) ='"
						+ new java.sql.Date(toDate_Bean.getTime()) + "'");
		logger.debug("Deleting from bal history ledger****" + stmt);
		stmt.execute();

		stmt = connection
				.prepareStatement("insert into st_lms_"
						+ tier
						+ "_current_balance_history select account_type,current_balance,agent_org_id,'"
						+ toDate_Bean + "' from st_lms_" + tier
						+ "_current_balance");
		logger.debug("Inserting into bal history ledger****" + stmt);
		stmt.execute();

	}

	public static void main(String[] args) throws SQLException {
		Connection connection = DBConnect.getConnection();
		// new LedgerHelper().onAdhocUpdateLedger("bo");
		// new LedgerHelper().insBalHistory("bo", connection);
		new LedgerHelperQuartz().fillLedger();

	}

	public void fillLedger() {
		Connection connection = DBConnect.getConnection();

		try {
			Timestamp toDate = null;
			ResultSet rs = null;
			ResultSet rsDate = null;
			Timestamp boDelDate = onAdhocUpdateLedger("bo");
			Timestamp agentDelDate = onAdhocUpdateLedger("agent");
			Timestamp delDate = null;
			String delDateStr = null;
			String fromDateStr = null;
			if (boDelDate != null) {
				delDate = boDelDate;
			}
			if (agentDelDate != null && delDate != null
					&& agentDelDate.before(delDate)) {
				delDate = agentDelDate;
			}

			if (delDate != null) {
				delDateStr = new java.sql.Date(delDate.getTime()) + " 23:59:59";
				logger.debug("Deleting data after " + delDateStr + "****date");
				stmt = connection
						.prepareStatement("delete from st_lms_bo_current_balance");
				stmt.addBatch("delete from st_lms_bo_current_balance");
				stmt
						.addBatch("delete from st_lms_bo_ledger where transaction_date>'"
								+ delDateStr + "'");
				stmt
						.addBatch("insert into st_lms_bo_current_balance select account_type,current_balance,agent_org_id from st_lms_bo_current_balance_history where date(transaction_date)='"
								+ new java.sql.Date(delDate.getTime()) + "'");
				stmt
						.addBatch("delete from st_lms_bo_current_balance_history where transaction_date>'"
								+ delDateStr + "'");
				stmt.addBatch("delete from st_lms_agent_current_balance");
				stmt
						.addBatch("delete from st_lms_agent_ledger where transaction_date>'"
								+ delDateStr + "'");
				stmt
						.addBatch("insert into st_lms_agent_current_balance select account_type,current_balance,agent_org_id from st_lms_agent_current_balance_history where date(transaction_date)='"
								+ new java.sql.Date(delDate.getTime()) + "'");
				stmt
						.addBatch("delete from st_lms_agent_current_balance_history where transaction_date>'"
								+ delDateStr + "'");
				stmt.executeBatch();
			}
			stmt = connection
					.prepareStatement("select distinct(date(transaction_date)) transaction_date from st_lms_bo_current_balance_history order by transaction_date desc limit 1");
			rs = stmt.executeQuery();
			if (rs.next()) {
				fromDateStr = new java.sql.Date(rs.getDate("transaction_date")
						.getTime())
						+ "";
			}

			if (fromDateStr == null) {
				fromDateStr = chkArcLedgerTable();
			}

			PreparedStatement stmtDate = connection
					.prepareStatement("select distinct(date(trDate)) transaction_date from (select distinct(date(transaction_date)) trDate from st_lms_agent_transaction_master union select distinct(date(transaction_date)) trDate from  st_lms_bo_transaction_master union select distinct(date(transaction_date)) trDate from st_lms_retailer_transaction_master) c where c.trDate>'"+ fromDateStr + "' order by transaction_date");
			logger.debug("Filling Ledger********" + stmtDate);
			rsDate = stmtDate.executeQuery();
			System.out.println(new Date() + "---Fill Ledger Start---"
					+ new Date().getTime());
			while (rsDate.next()) {

				toDate = rsDate.getTimestamp("transaction_date");
				PreparedStatement stmt = connection
						.prepareStatement("select organization_id from st_lms_organization_master where organization_type='AGENT'");
				rs = stmt.executeQuery();
				while (rs.next()) {
					ledgerAgentEntry(toDate, rs.getInt("organization_id"));
				}
				ledgerBoEntry(toDate);

			}
			System.out.println(new Date() + "---Fill Ledger Start---"
					+ new Date().getTime());
			DBConnect.closeCon(connection);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
