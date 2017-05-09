package com.skilrock.lms.coreEngine.ola;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.OlaGetPendingWithdrawalDetailsBean;
import com.skilrock.lms.beans.OlaPTResponseBean;
import com.skilrock.lms.beans.OrgPwtLimitBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.OrgCreditUpdation;
import com.skilrock.lms.coreEngine.ola.common.CommonFunctionsHelper;
import com.skilrock.lms.coreEngine.ola.common.OLAClient;
import com.skilrock.lms.coreEngine.ola.common.OLAConstants;
import com.skilrock.lms.coreEngine.ola.common.OLAUtility;
import com.skilrock.ola.userMgmt.javaBeans.OlaPlayerRegistrationRequestBean;

public class OlaHelper {
	static Log logger = LogFactory.getLog(OlaHelper.class);
	public OlaGetPendingWithdrawalDetailsBean depositMoney(String userName,
			double depositAmt, String walletName, UserInfoBean userBean,
			int walletId, String depositAnyWhere,
			OlaGetPendingWithdrawalDetailsBean pendingResponseBean,
			boolean isPendingData, String rootPath) throws LMSException {
		pendingResponseBean = new OlaGetPendingWithdrawalDetailsBean();
		Connection con = DBConnect.getConnection();
		double retailerComm = 0.0;
		double agentComm = 0.0;
		double retNetAmt = 0.0;
		double agentNetAmt = 0.0;
		long imsTransactionId = 0;
		long agentRefTransactionId = 0;
		int isUpdate;
		try {
			con.setAutoCommit(false);
			int agentOrgId = userBean.getParentOrgId();
			int retOrgId = userBean.getUserOrgId();

			
			retailerComm = CommonFunctionsHelper.fetchOLACommOfOrganization(
					walletId, retOrgId, "DEPOSIT", "RETAILER", con);
			agentComm = CommonFunctionsHelper.fetchOLACommOfOrganization(
					walletId, agentOrgId, "DEPOSIT", "AGENT", con);
			// check with organizarizations limit
			CommonFunctionsHelper commonFunction = new CommonFunctionsHelper();
			OrgPwtLimitBean orgPwtLimit = commonFunction
					.fetchPwtLimitsOfOrgnization(retOrgId, con);

			if (orgPwtLimit == null) { // send mail to backoffice
				System.out.println("OLA Limits Are Not defined Properly!!");
				throw new LMSException("OLA Limits Are Not defined Properly!!");
			}
			double olaDepositLimit = orgPwtLimit.getOlaDepositLimit();
			System.out.println("olaDepositLimit" + olaDepositLimit);
			System.out.println("ola deposite money" + depositAmt);
			if (depositAmt > olaDepositLimit) {
				System.out
						.println("Deposit amount is greater then deposit limit");
				pendingResponseBean
						.setReturnType("Deposit amount is greater then deposit limit");
				return pendingResponseBean;
				// return "Deposit amount is greater then deposit limit";
			}
			// check with retailer and agent balance to deposit
			int isCheck = checkOrgBalance(depositAmt, retOrgId, agentOrgId,
					con, retailerComm, agentComm);
			System.out.println("ischeck" + isCheck);

			if (isCheck == -1) {
				// Agent has insufficient
				pendingResponseBean.setReturnType("Agent has insufficient Balance");
				return pendingResponseBean;
				// return "Agent has insufficient";

			} else if (isCheck == -2) {
				// Error LMS
				pendingResponseBean.setReturnType("Error in LMS");
				return pendingResponseBean;
				// return "Error LMS";
			} else if (isCheck == 0) {
				// Retailer has insufficient
				pendingResponseBean.setReturnType("Retailer has insufficient Balance");
				return pendingResponseBean;
				// return "Retailer has insufficient";
			}
			// inser in LMS transaction master
			if (isCheck == 2) {

				// here check the plr mapping
				String affiliateId = null;
				PreparedStatement affPstmt = con
						.prepareStatement("select ref_user_id from st_ola_org_affiliate_mapping where organization_id="
								+ userBean.getUserOrgId() + "");
				ResultSet resultSet = affPstmt.executeQuery();
				if (resultSet.next()) {
					affiliateId = resultSet.getString("ref_user_id");

				}
				String mappingData = OLAUtility.affiliatePlrBinding(
						depositAnyWhere, userName, affiliateId,retOrgId,con, rootPath,walletId);

				// boolean isMappingOk=true;
				if (!mappingData.equalsIgnoreCase("OK")) {
					System.out.println("Player is not Mapped");
					pendingResponseBean.setReturnType(mappingData);
					return pendingResponseBean;
					// return "Player is not Mapped";
				}
				// Fetch Pending Withdrawal Data of Player
				if (isPendingData) {
					pendingResponseBean = OLAUtility.parsePendingWithdrawalXML(
							userName, pendingResponseBean, rootPath);
					if (!pendingResponseBean.getErrorCode().equalsIgnoreCase(
							"null")) {
						if (pendingResponseBean.getErrorCode()
								.equalsIgnoreCase("0")) {

							if (pendingResponseBean.getPendingWithdrawalList() != null) {
								pendingResponseBean
										.setReturnType("PENDING_WITHDRAWAL_REQUEST");
								return pendingResponseBean;
								// return "PENDING_WITHDRAWAL_REQUEST";
							} else {
								System.out.println("No Pending withdrawls");

							}
						} else {
							System.out.println("some error"
									+ pendingResponseBean.getErrorText());
							pendingResponseBean
									.setReturnType(pendingResponseBean
											.getErrorText());
							return pendingResponseBean;
							// return "some Internal error occur";
						}
					} else {
						System.out
								.println("error occured during fetching the pending Withdrawal Response data of"
										+ userName);
						pendingResponseBean
								.setReturnType("some Internal error occur");
						return pendingResponseBean;
						// return
						// "error occur during fetching the pending Withdrawal Response date of"+userName;
					}
				}

				String insertInLMS = "insert into st_lms_transaction_master(user_type,service_code,interface) values('RETAILER','OLA','WEB')";
				PreparedStatement pstmt1 = con.prepareStatement(insertInLMS);

				long transactionId = 0;
				pstmt1.executeUpdate();
				ResultSet rs1 = pstmt1.getGeneratedKeys();
				if (rs1.next()) {
					transactionId = rs1.getLong(1);
					// insert into retailer transaction master
					pstmt1 = con
							.prepareStatement("INSERT INTO st_lms_retailer_transaction_master (transaction_id,retailer_user_id,retailer_org_id,game_id,transaction_date,transaction_type) VALUES (?,?,?,?,?,?)");
					pstmt1.setLong(1, transactionId);
					pstmt1.setInt(2, userBean.getUserId());
					pstmt1.setInt(3, userBean.getUserOrgId());
					pstmt1.setInt(4, walletId);
					pstmt1.setTimestamp(5, new java.sql.Timestamp(new Date()
							.getTime()));
					pstmt1.setString(6, "OLA_DEPOSIT");
					isUpdate = pstmt1.executeUpdate();

					// insert in deposit master
					retNetAmt = (depositAmt - ((depositAmt * retailerComm) / 100));
					agentNetAmt = (depositAmt - ((depositAmt * agentComm) / 100));
					String insertQry = "insert into st_ola_ret_deposit(transaction_id, wallet_id, party_id, retailer_org_id, deposit_amt, retailer_comm, net_amt, agent_comm, agent_net_amt, agent_ref_transaction_id, claim_status, deposit_channel, ims_ref_transaction_id) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
					PreparedStatement pstmtUpdate = con
							.prepareStatement(insertQry);
					pstmtUpdate.setLong(1, transactionId);
					pstmtUpdate.setInt(2, walletId);
					pstmtUpdate.setString(3, userName);
					pstmtUpdate.setInt(4, userBean.getUserOrgId());
					pstmtUpdate.setDouble(5, depositAmt);
					pstmtUpdate.setDouble(6, retailerComm);
					pstmtUpdate.setDouble(7, retNetAmt);
					pstmtUpdate.setDouble(8, agentComm);
					pstmtUpdate.setDouble(9, agentNetAmt);
					pstmtUpdate.setLong(10, agentRefTransactionId);
					pstmtUpdate.setString(11, "CLAIM_BAL");
					pstmtUpdate.setString(12, "WEB");
					pstmtUpdate.setLong(13, imsTransactionId);
					pstmtUpdate.executeUpdate();

					// update st_lms_organization_master for claimable balance
					// for
					// retailer
					CommonFunctionsHelper commHelper = new CommonFunctionsHelper();
					commHelper.updateOrgBalance("CLAIM_BAL", retNetAmt,
							userBean.getUserOrgId(), "CREDIT", con);

					// update st_lms_organization_master for claimable balance
					// for
					// agent
					commHelper.updateOrgBalance("CLAIM_BAL", agentNetAmt,
							userBean.getParentOrgId(), "CREDIT", con);
					con.commit();

					// here request would be sent to IMS
					boolean isIMSSuccess = false;
					OlaPTResponseBean respBean = new OlaPTResponseBean();
					respBean = OLAUtility.callDepositApi(transactionId,
							depositAmt, userName, respBean);
					if ((respBean.getDepositStatus()).equals("approved")) {
						isIMSSuccess = true;
					} else if ((respBean.getDepositStatus()).equals("declined")) {
						isIMSSuccess = false;
					} else {
						// this is the connection time out case or response not
						// received
						// here call the getstaus API
						respBean = OLAUtility.callCheckTransactionIdForDeposit(
								respBean, transactionId);
						if ((respBean.getStatus()).equalsIgnoreCase("approved")) {
							isIMSSuccess = true;
						} else {
							respBean.setDepositError("4141");
							isIMSSuccess = false;
						}
					}

					if (isIMSSuccess) {
						System.out
								.println("in ola helper amount is deposit Successfully");
						imsTransactionId = respBean
								.getImsDepositTransactionId();
						pstmt1 = con
								.prepareStatement("update st_ola_ret_deposit set ims_ref_transaction_id=? where transaction_id=?");
						pstmt1.setLong(1, imsTransactionId);
						pstmt1.setLong(2, transactionId);
						pstmt1.executeUpdate();
						con.commit();

					} else {
						// call refund

						boolean isRefund = depositeRefund(depositAmt,
								retNetAmt, agentNetAmt, retailerComm,
								agentComm, userName, con, walletId, userBean,
								transactionId, respBean
										.getImsDepositTransactionId());
						if (isRefund == true) {
							con.commit();
							String error = convertErrorCodeToErrorMessageForDeposit(respBean
									.getDepositError());
							pendingResponseBean.setReturnType(error);
							return pendingResponseBean;
							// return error;

						} else {
							System.out
									.println("Error During Refund to the retailer after getting failed from PlayTech");
							pendingResponseBean
									.setReturnType("error during Refund the money");
							return pendingResponseBean;
							// return "error during Refund the money";
						}
					}
				} else {
					System.out
							.println("Trabsaction Id is not Generated in LMS transaction master");
					pendingResponseBean
							.setReturnType("error in Deposit the money");
					return pendingResponseBean;
					// return "error in Deposit the money";
				}
			} else {
				System.out.println("Error During balance verification");
				pendingResponseBean
						.setReturnType("Error During balance verification");
				return pendingResponseBean;
				// return "Error During balance verification";
			}
			// con.commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("Error during deposit");
		} finally {

			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
				throw new LMSException(se);
			}

		}
		pendingResponseBean.setReturnType("true");
		return pendingResponseBean;
		// return "true";
	}

	/*public boolean depositeRefund(double depositAmt, double retNetAmt,
			double agentNetAmt, double retailerComm, double agentComm,
			String userName, Connection con, int walletId,
			UserInfoBean userBean, long depositTransactionId,
			long imsTransactionID) throws SQLException {

		// update in transaction master
		String insertInLMS = "insert into st_lms_transaction_master(user_type,service_code,interface) values('RETAILER','OLA','WEB')";
		PreparedStatement pstmt1 = con.prepareStatement(insertInLMS);
		long transactionId = 0;
		pstmt1.executeUpdate();
		ResultSet rs1 = pstmt1.getGeneratedKeys();
		if (rs1.next()) {
			transactionId = rs1.getLong(1);
			// insert into retailer transaction master
			pstmt1 = con
					.prepareStatement("INSERT INTO st_lms_retailer_transaction_master (transaction_id,retailer_user_id,retailer_org_id,game_id,transaction_date,transaction_type) VALUES (?,?,?,?,?,?)");
			pstmt1.setLong(1, transactionId);
			pstmt1.setInt(2, userBean.getUserId());
			pstmt1.setInt(3, userBean.getUserOrgId());
			pstmt1.setInt(4, walletId);
			pstmt1
					.setTimestamp(5, new java.sql.Timestamp(new Date()
							.getTime()));
			pstmt1.setString(6, "OLA_DEPOSIT_REFUND");
			pstmt1.executeUpdate();

			pstmt1 = con
					.prepareStatement("insert into st_ola_ret_deposit_refund(transaction_id, wallet_id, retailer_org_id, ims_ref_transaction_id, ola_ref_transaction_id, claim_status, cancel_reason, deposit_amt, net_amt, agent_net_amt, retailer_comm, agent_comm, agent_ref_transaction_id, party_id) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			pstmt1.setLong(1, transactionId);
			pstmt1.setInt(2, walletId);
			pstmt1.setInt(3, userBean.getUserOrgId());
			pstmt1.setLong(4, imsTransactionID);
			pstmt1.setLong(5, depositTransactionId);
			pstmt1.setString(6, "CLAIM_BAL");
			pstmt1.setString(7, "CANCEL_SERVER");
			pstmt1.setDouble(8, depositAmt);
			pstmt1.setDouble(9, retNetAmt);
			pstmt1.setDouble(10, agentNetAmt);
			pstmt1.setDouble(11, retailerComm);
			pstmt1.setDouble(12, agentComm);
			pstmt1.setInt(13, 0);
			pstmt1.setString(14, userName);
			pstmt1.executeUpdate();
			// update ret_comm in st_ola_ret_withdrawl
			// pstmt1 =
			// con.prepareStatement("update st_ola_ret_deposit set retailer_comm=(retailer_comm-"+retailerComm+"),agent_comm=(agent_comm-"+agentComm+") where transaction_id="+depositTransactionId+"");
			// pstmt1.executeUpdate();

			// update st_lms_organization_master for claimable balance
			// for
			// retailer
			CommonFunctionsHelper commHelper = new CommonFunctionsHelper();
			commHelper.updateOrgBalance("CLAIM_BAL", retNetAmt, userBean
					.getUserOrgId(), "DEBIT", con);

			// update st_lms_organization_master for claimable balance
			// for
			// agent
			commHelper.updateOrgBalance("CLAIM_BAL", agentNetAmt, userBean
					.getParentOrgId(), "DEBIT", con);

		} else {
			return false;
		}
		return true;
	}*/

	public int checkOrgBalance(double depositeAmt, int retOrgId,
			int agentOrgId, Connection con, double agtCommRate,
			double retCommRate) throws SQLException {
		// check with ACA
		// if online sale amt > ACA then return ERROR

		PreparedStatement pstmt4 = con
				.prepareStatement("select (available_credit-claimable_bal) as availbale_sale_bal from st_lms_organization_master where organization_id=?");
		pstmt4.setInt(1, retOrgId);
		ResultSet rsTrns = pstmt4.executeQuery();
		if (rsTrns.next()) {
			System.out
					.println(((rsTrns.getDouble("availbale_sale_bal")) > (depositeAmt - (depositeAmt
							* retCommRate * .01))));
			if (!((rsTrns.getDouble("availbale_sale_bal")) > (depositeAmt - (depositeAmt
					* retCommRate * .01)))) {
				return 0;
			}

		} else {
			return -2;
		}

		// check for agents ACA and claimable balance
		pstmt4 = con
				.prepareStatement("select (available_credit-claimable_bal) as availbale_sale_bal from st_lms_organization_master where organization_id=?");
		pstmt4.setInt(1, agentOrgId);
		rsTrns = pstmt4.executeQuery();
		if (rsTrns.next()) {
			if (!(rsTrns.getDouble("availbale_sale_bal") > (depositeAmt - (depositeAmt
					* agtCommRate * .01)))) {
				return -1;
			}
		} else {
			return -2;
		}
		return 2;
	}

	// for withdrawl the money

	public String withdrawlMoney(String userName, double WithdrawlAmt,
			String walletName, UserInfoBean userBean, int walletId,
			String withdrawlAnyWhere, String aunthticationCode)
			throws LMSException {
		Connection con = DBConnect.getConnection();

		double retailerComm = 0;
		double agentComm = 0;
		double retNetAmt = 0;
		double agentNetAmt = 0;
		long tempTransactionId = 0;
		long imsTransactionId = 0;
		long agentRefTransactionId = 0;
		try {
			con.setAutoCommit(false);

			int retOrgId = userBean.getUserOrgId();
			
			retailerComm = CommonFunctionsHelper.fetchOLACommOfOrganization(
					walletId, retOrgId, "WITHDRAWAL", "RETAILER", con);
			agentComm = CommonFunctionsHelper.fetchOLACommOfOrganization(
					walletId, userBean.getParentOrgId(), "WITHDRAWAL", "AGENT", con);
			// check with organizarizations limit
			CommonFunctionsHelper commonFunction = new CommonFunctionsHelper();
			OrgPwtLimitBean orgPwtLimit = commonFunction
					.fetchPwtLimitsOfOrgnization(retOrgId, con);
			if (orgPwtLimit == null) { // send mail to backoffice
				throw new LMSException("PWT Limits Are Not defined Properly!!");
			}
			double olaWithdrawlLimit = orgPwtLimit.getOlaWithdrawlLimit();
			System.out.println("olaWithdrawlLimit" + olaWithdrawlLimit);
			if (WithdrawlAmt > olaWithdrawlLimit) {
				System.out
						.println("withdrawl amount is greater then withdrawl limit");
				return "WITHDRAWL_LIMIT";
			}

			String affiliateId = null;
			PreparedStatement affPstmt = con
					.prepareStatement("select ref_user_id from st_ola_org_affiliate_mapping where organization_id="
							+ userBean.getUserOrgId() + "");
			ResultSet resultSet = affPstmt.executeQuery();
			if (resultSet.next()) {
				affiliateId = resultSet.getString("ref_user_id");
			}
			boolean isMappingOk = OLAUtility.affiliatePlrBindingForWithdrawl(
					withdrawlAnyWhere, userName, affiliateId, con,walletId);
			// isMappingOk=true;
			if (!isMappingOk) {
				System.out.println("Player is not Mapped");
				return "Player is not mapped";
			}
			retNetAmt = (WithdrawlAmt + ((WithdrawlAmt * retailerComm) / 100));
			agentNetAmt = (WithdrawlAmt + ((WithdrawlAmt * agentComm) / 100));
			// insert withdrawal details in st_ola_ret_withdrawal_temp
			PreparedStatement insertTemp = con
					.prepareStatement("insert into st_ola_ret_withdrawl_temp(wallet_id, retailer_org_id, ims_ref_transaction_id, withdrawl_amt, net_amt, agent_net_amt, retailer_comm, agent_comm, deposit_channel, status, retailer_ref_transaction_id, party_id)values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			insertTemp.setInt(1, walletId);
			insertTemp.setInt(2, userBean.getUserOrgId());
			insertTemp.setInt(3, 0);
			insertTemp.setDouble(4, WithdrawlAmt);
			insertTemp.setDouble(5, retNetAmt);
			insertTemp.setDouble(6, agentNetAmt);
			insertTemp.setDouble(7, retailerComm);
			insertTemp.setDouble(8, agentComm);
			insertTemp.setString(9, "WEB");
			insertTemp.setString(10, "PENDING");
			insertTemp.setInt(11, 0);
			insertTemp.setString(12, userName);
			insertTemp.executeUpdate();
			ResultSet resultSet2 = insertTemp.getGeneratedKeys();
			if (resultSet2.next()) {
				tempTransactionId = resultSet2.getLong(1);
				con.commit(); // here commit the data before sending the request
				// to PT as INITIATED
				boolean isIMSSuccess = false;
				OlaPTResponseBean respBean = new OlaPTResponseBean();
				respBean = OLAUtility.callWithdrawlApi(tempTransactionId,
						WithdrawlAmt, respBean, aunthticationCode);
				if (respBean.getWithdrawalStatus() == null) {
					respBean.setWithdrawalStatus("null");
					isIMSSuccess = false;
				} else if ((respBean.getWithdrawalStatus()).equals("declined")) {
					isIMSSuccess = false;
				} else if ((respBean.getWithdrawalStatus()).equals("approved")
						&& respBean.getWithdrawalError() == null) {
					isIMSSuccess = true;
				} else if ((respBean.getWithdrawalStatus())
						.equalsIgnoreCase("null")) {
					// this is the connection time out case or response not
					// received
					// here call the getstaus API
					respBean = OLAUtility.callCheckTransactionIdForWithdrawal(
							respBean, tempTransactionId);
					if ((respBean.getStatus()).equalsIgnoreCase("approved")) {
						isIMSSuccess = true;
					} else {
						respBean.setWithdrawalStatus("null");
						respBean.setWithdrawalError("4141");
						isIMSSuccess = false;
					}
				}

				if (isIMSSuccess) {

					String insertInLMS = "insert into st_lms_transaction_master(user_type,service_code,interface) values('RETAILER','OLA','WEB')";
					PreparedStatement pstmt1 = con
							.prepareStatement(insertInLMS);
					int transactionId = 0;
					pstmt1.executeUpdate();
					ResultSet rs1 = pstmt1.getGeneratedKeys();
					if (rs1.next()) {
						transactionId = rs1.getInt(1);
						// insert into retailer transaction master
						pstmt1 = con
								.prepareStatement("INSERT INTO st_lms_retailer_transaction_master (transaction_id,retailer_user_id,retailer_org_id,game_id,transaction_date,transaction_type) VALUES (?,?,?,?,?,?)");
						pstmt1.setInt(1, transactionId);
						pstmt1.setInt(2, userBean.getUserId());
						pstmt1.setInt(3, userBean.getUserOrgId());
						pstmt1.setInt(4, walletId);
						pstmt1.setTimestamp(5, new java.sql.Timestamp(
								new Date().getTime()));
						pstmt1.setString(6, "OLA_WITHDRAWL");
						pstmt1.executeUpdate();
						// insert in withdrawl master

						String insertQry = "insert into st_ola_ret_withdrawl(transaction_id, wallet_id, retailer_org_id, ims_ref_transaction_id, withdrawl_amt, net_amt, agent_net_amt, retailer_comm, agent_comm, deposit_channel, claim_status, agent_ref_transaction_id, party_id) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
						PreparedStatement pstmtUpdate = con
								.prepareStatement(insertQry);
						pstmtUpdate.setInt(1, transactionId);
						pstmtUpdate.setInt(2, walletId);
						pstmtUpdate.setInt(3, userBean.getUserOrgId());
						pstmtUpdate.setLong(4, imsTransactionId);
						pstmtUpdate.setDouble(5, WithdrawlAmt);
						pstmtUpdate.setDouble(6, retNetAmt);
						pstmtUpdate.setDouble(7, agentNetAmt);
						pstmtUpdate.setDouble(8, retailerComm);
						pstmtUpdate.setDouble(9, agentComm);
						pstmtUpdate.setString(10, "WEB");
						pstmtUpdate.setString(11, "CLAIM_BAL");
						pstmtUpdate.setLong(12, agentRefTransactionId);
						pstmtUpdate.setString(13, userName);
						pstmtUpdate.executeUpdate();
						// update st_lms_organization_master for claimable
						// balance
						// for
						// retailer
						CommonFunctionsHelper commHelper = new CommonFunctionsHelper();
						commHelper.updateOrgBalance("CLAIM_BAL", retNetAmt,
								userBean.getUserOrgId(), "DEBIT", con);

						// update st_lms_organization_master for claimable
						// balance
						// for
						// agent
						commHelper.updateOrgBalance("CLAIM_BAL", agentNetAmt,
								userBean.getParentOrgId(), "DEBIT", con);

						System.out.println("withdrawl amount successfully");
						PreparedStatement updateImsTransId = con
								.prepareStatement("update st_ola_ret_withdrawl set ims_ref_transaction_id=? where transaction_id=?");
						updateImsTransId.setLong(1, respBean
								.getImsWithdrawalTransactionId());
						updateImsTransId.setInt(2, transactionId);
						updateImsTransId.executeUpdate();

						PreparedStatement updateTemp = con
								.prepareStatement("update st_ola_ret_withdrawl_temp set ims_ref_transaction_id=?,retailer_ref_transaction_id=? , status=? where transaction_id=?");
						updateTemp.setLong(1, respBean
								.getImsWithdrawalTransactionId());
						updateTemp.setInt(2, transactionId);
						updateTemp.setString(3, "DONE");
						updateTemp.setLong(4, tempTransactionId);
						updateTemp.executeUpdate();

						con.commit();
					} else {
						System.out
								.println("Trabsaction Id is not Generated in LMS transaction master");
						return "error in withdrawl the money";
					}
				} else {
					if ((respBean.getWithdrawalStatus()).equals("declined")) {
						PreparedStatement updateTempStatus = con
								.prepareStatement("update st_ola_ret_withdrawl_temp set ims_ref_transaction_id=? ,retailer_ref_transaction_id=? ,status=? where transaction_id=?");
						updateTempStatus.setLong(1, respBean
								.getImsWithdrawalTransactionId());
						updateTempStatus.setInt(2, 0);
						updateTempStatus.setString(3, "DENIED");
						updateTempStatus.setLong(4, tempTransactionId);
						updateTempStatus.executeUpdate();
						con.commit();
						System.out
								.println("withdrawal request is declined from Playtech end");
						String error = convertErrorCodeToErrorMessageForWithdrawal(respBean
								.getWithdrawalError());
						return error;
					} else {
						String error = convertErrorCodeToErrorMessageForWithdrawal(respBean
								.getWithdrawalError());
						return error;
					}
				}
			} else {
				System.out
						.println("Trabsaction Id is not Generated in st_ola_ret_withdrawal_temp");
				return "error in withdrawl the money";
			}
			// con.commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("Error during withdrawl");

		} finally {
			try {
				// con.commit();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return "true";
	}

	public boolean withdrawlRefund(double depositAmt, double retNetAmt,
			double agentNetAmt, double retailerComm, double agentComm,
			String userName, Connection con, int walletId,
			UserInfoBean userBean, long withdrawlTransactionId,
			long imsTransactionId) throws SQLException {

		// update in transaction master
		String insertInLMS = "insert into st_lms_transaction_master(user_type,service_code,interface) values('RETAILER','OLA','WEB')";
		PreparedStatement pstmt1 = con.prepareStatement(insertInLMS);
		long transactionId = 0;
		pstmt1.executeUpdate();
		ResultSet rs1 = pstmt1.getGeneratedKeys();
		if (rs1.next()) {
			transactionId = rs1.getLong(1);
			// insert into retailer transaction master
			pstmt1 = con
					.prepareStatement("INSERT INTO st_lms_retailer_transaction_master (transaction_id,retailer_user_id,retailer_org_id,game_id,transaction_date,transaction_type) VALUES (?,?,?,?,?,?)");
			pstmt1.setLong(1, transactionId);
			pstmt1.setInt(2, userBean.getUserId());
			pstmt1.setInt(3, userBean.getUserOrgId());
			pstmt1.setInt(4, walletId);
			pstmt1
					.setTimestamp(5, new java.sql.Timestamp(new Date()
							.getTime()));
			pstmt1.setString(6, "OLA_WITHDRAWL_REFUND");
			pstmt1.executeUpdate();

			pstmt1 = con
					.prepareStatement("insert into st_ola_ret_withdrawl_refund(transaction_id, wallet_id, retailer_org_id, ims_ref_transaction_id, ola_ref_transaction_id, claim_status, cancel_reason, withdrawl_amt, net_amt, agent_net_amt, retailer_comm, agent_comm, agent_ref_transaction_id, party_id) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			pstmt1.setLong(1, transactionId);
			pstmt1.setInt(2, walletId);
			pstmt1.setInt(3, userBean.getUserOrgId());
			pstmt1.setLong(4, imsTransactionId);
			pstmt1.setLong(5, withdrawlTransactionId);
			pstmt1.setString(6, "CLAIM_BAL");
			pstmt1.setString(7, "CANCEL_SERVER");
			pstmt1.setDouble(8, depositAmt);
			pstmt1.setDouble(9, retNetAmt);
			pstmt1.setDouble(10, agentNetAmt);
			pstmt1.setDouble(11, retailerComm);
			pstmt1.setDouble(12, agentComm);
			pstmt1.setInt(13, 0);
			pstmt1.setString(14, userName);
			pstmt1.executeUpdate();

			CommonFunctionsHelper commHelper = new CommonFunctionsHelper();
			commHelper.updateOrgBalance("CLAIM_BAL", retNetAmt, userBean
					.getUserOrgId(), "CREDIT", con);

			// update st_lms_organization_master for claimable balance
			// for
			// agent
			commHelper.updateOrgBalance("CLAIM_BAL", agentNetAmt, userBean
					.getParentOrgId(), "CREDIT", con);

		} else {
			return false;
		}
		return true;
	}

	public String olaWalletDetails() {
		String authenticationType = null;
		Connection con = DBConnect.getConnection();
	try{
		String walletDisplayName;
		con.setAutoCommit(false);
		String query = "select wallet_id,wallet_display_name  from st_ola_wallet_master where wallet_status='ACTIVE'";
		PreparedStatement pstmt = con.prepareStatement(query);
		ResultSet rs = pstmt.executeQuery();
		StringBuilder walletDetailValue = new StringBuilder("");
		boolean flag = false;
		while (rs.next()) {
			int walletId = rs.getInt("wallet_id");
			walletDisplayName = rs.getString("wallet_display_name");
			// select the value of aunthtication type
			PreparedStatement pstmt1 = con
					.prepareStatement("select authentication_type from st_ola_wallet_authentication_type where wallet_id="
							+ walletId + "");
			ResultSet rs1 = pstmt1.executeQuery();
			while (rs1.next()) {
				authenticationType = rs1.getString("authentication_type");
			}
			walletDetailValue.append(walletId + ":" + walletDisplayName + ":"
					+ authenticationType + "Nxt");
			flag = true;

		}
		if (flag) {
			walletDetailValue.delete(walletDetailValue.length() - 3,
					walletDetailValue.length());
		}

		String walletDetail = walletDetailValue.toString();
		con.commit();
		return walletDetail;
	}catch(Exception e){
		e.printStackTrace();
	}finally{
		try {
			con.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	
	}
	return "";

	}

	
	public String convertErrorCodeToErrorMessageForDeposit(String error) {

		switch (Integer.parseInt(error)) {
		case 1:
			error = "Incorrect password or username.";
			break;
		case 2:
			error = "Incorrect casino.";
			break;
		case 3:
			error = "Secret key validation failed.";
			break;
		case 4:
			error = "Insufficient funds.";
			break;
		case 5:
			error = "Internal system error.";
			break;
		case 6:
			error = "The external withdrawal request was cancelled, because the player had an active pre-wager bonus that was marked to be declined upon a withdrawal request, and an open(confirmed!) bet in a game";
			break;
		case 7:
			error = "Currency mismatch – remote and local currencies are different.";
			break;
		case 8:
			error = "Amount exceeds maximum deposit limit";
			break;
		case 9:
			error = "Amount exceeds minimum deposit limit.";
			break;
		case 10:
			error = "The external transaction ID is not unique";
			break;
		case 15:
			error = "Fund transfers are denied for internal accounts. (This option is configurable on the Casino Configuration page in the Casino Admin.)";
			break;
		case 16:
			error = "Account has been frozen. (This option is configurable from the Casino Admin. Allowing or prohibiting deposits/withdrawals when player‟s account is frozen can be configured separately.)";
			break;
		case 17:
			error = "Invalid promotional code";
			break;
		case 1000:
			error = "Database connection error.";
			break;
		case 1116:
			error = "Internal system error";
			break;
		case 4141:
			error = "Network error occur during communication with playtech";
			break;
		default:
			break;
		}
		return error;
	}

	public String convertErrorCodeToErrorMessageForWithdrawal(String error) {
		switch (Integer.parseInt(error)) {
		case 1001:
			error = "Internal System Error";
			break;
		case 1002:
			error = "Aunthtication failed.Wrong secret key or IP not in the sync IP's whitelist";
			break;
		case 1003:
			error = "Missing Request Parameter. Reference code is required";
			break;
		case 1004:
			error = "Missing or Invalid request Parameter. Amount is required";
			break;
		case 1005:
			error = "Missing Request Parameter. External transaction ID required";
			break;
		case 1006:
			error = "Missing or Invalid request Parameter. Currency is required";
			break;
		case 2001:
			error = "Tranaction not found in the system";
			break;
		case 2002:
			error = "Transaction already Processed";
			break;
		case 2003:
			error = "Account has been frozen";
			break;
		case 4141:
			error = "Network error occur during communication with playtech";
			break;
		default:
			break;
		}
		return error;
	}

	// Added for comm Update Type

	public String commUpdateTypes() throws SQLException {
		Connection con = DBConnect.getConnection();
		String commUpTypes = null;
		con.setAutoCommit(false);
		String query = "select value from st_lms_property_master where property_code='ola_comm_update_type'";
		PreparedStatement pstmt = con.prepareStatement(query);
		ResultSet rs = pstmt.executeQuery();

		while (rs.next()) {

			commUpTypes = rs.getString("value");

		}

		return commUpTypes;

	}

	

	// end 
	public int checkOrgBalance(double depositeAmt,
			int orgId, Connection con, double agtCommRate) throws SQLException {
		// check with ACA
		// if online sale amt > ACA then return ERROR

		PreparedStatement pstmt4 = con
				.prepareStatement("select (available_credit-claimable_bal) as availbale_sale_bal from st_lms_organization_master where organization_id=?");
		pstmt4.setInt(1, orgId);
		ResultSet rsTrns = pstmt4.executeQuery();
		if (rsTrns.next()) {
			System.out
					.println(((rsTrns.getDouble("availbale_sale_bal")) > (depositeAmt - (depositeAmt
							* agtCommRate * .01))));
			if (!((rsTrns.getDouble("availbale_sale_bal")) > (depositeAmt - (depositeAmt
					* agtCommRate * .01)))) {
				return -1;
			}

		} else {
			return -2;
		}

		return 2;
	}

	public boolean agtDepositeRefund(double depositAmt,
			double agentNetAmt, double agentComm,int plrId,
			String plrName, Connection con, int walletId,
			UserInfoBean userBean, long reftransactionId) throws SQLException, LMSException {

		// update in transaction master
		String insertInLMS = QueryManager.insertInLMSTransactionMaster();
		PreparedStatement pstmt1 = con.prepareStatement(insertInLMS);
		pstmt1.setString(1, "AGENT");
		long transactionId = 0;
		pstmt1.executeUpdate();
		int isUpdate=0;
		ResultSet rs1 = pstmt1.getGeneratedKeys();
		if (rs1.next()) {
			transactionId = rs1.getLong(1);
			// insert into retailer transaction master
			pstmt1 = con.prepareStatement("INSERT INTO st_lms_agent_transaction_master (transaction_id,user_id,user_org_id,party_type,party_id,transaction_type,transaction_date) VALUES (?,?,?,?,?,?,?)");
			pstmt1.setLong(1, transactionId);
			pstmt1.setInt(2, userBean.getUserId());
			pstmt1.setInt(3, userBean.getUserOrgId());
			pstmt1.setString(4, "PLAYER");	
			pstmt1.setInt(5, plrId);
			pstmt1.setString(6, "OLA_DEPOSIT_REFUND_PLR");
			java.util.Date date = new java.util.Date();
			pstmt1.setTimestamp(7, new java.sql.Timestamp(date.getTime()));
		
			isUpdate = pstmt1.executeUpdate();
		
			logger.info("inserted into agent transaction master"+isUpdate);


			pstmt1 = con
					.prepareStatement("insert into st_ola_agt_direct_plr_deposit_refund(agent_user_id,agent_org_id,transaction_id,wallet_id,plr_id,plr_alias, deposit_amt,  net_amt, deposit_claim_status,cancel_reason,agt_claim_comm,ref_transaction_id) values(?,?,?,?,?,?,?,?,?,?,?,?)");
			pstmt1.setInt(1, userBean.getUserId());
			pstmt1.setInt(2, userBean.getUserOrgId());
			pstmt1.setLong(3, transactionId);
			pstmt1.setInt(4, walletId);
			pstmt1.setInt(5,plrId);
			pstmt1.setString(6,plrName);
			pstmt1.setDouble(7, depositAmt);
			pstmt1.setDouble(8, agentNetAmt);
			pstmt1.setString(9, "CLAIM_BAL");
			pstmt1.setString(10, "CANCEL_SERVER");
			pstmt1.setDouble(11, agentComm);
			pstmt1.setDouble(12, reftransactionId);
			isUpdate=pstmt1.executeUpdate();
				
			logger.info("inserted into st_ola_agt_direct_plr_deposit_refund "+isUpdate);
			// update ret_comm in st_ola_ret_withdrawl
			// pstmt1 =
			// con.prepareStatement("update st_ola_ret_deposit set retailer_comm=(retailer_comm-"+retailerComm+"),agent_comm=(agent_comm-"+agentComm+") where transaction_id="+depositTransactionId+"");
			// pstmt1.executeUpdate();

			//CommonFunctionsHelper commHelper = new CommonFunctionsHelper();
			

			// update st_lms_organization_master for claimable balance
			// for
			// agent
			/*commHelper.updateOrgBalance("CLAIM_BAL", agentNetAmt, userBean
					.getUserOrgId(), "DEBIT", con);*/
			OrgCreditUpdation.updateOrganizationBalWithValidate(agentNetAmt, "CLAIM_BAL", "DEBIT", userBean.getUserOrgId(), userBean.getParentOrgId(), userBean.getUserType(), 0, con);

		} else {
			return false;
		}
		return true;
	}	
	public boolean boDepositeRefund(double depositAmt,int plrId,
			String plrName, Connection con, int walletId,
			UserInfoBean userBean, long reftransactionId) throws SQLException {

		// update in transaction master
		String insertInLMS = QueryManager.insertInLMSTransactionMaster();
		PreparedStatement pstmt1 = con.prepareStatement(insertInLMS);
		pstmt1.setString(1, "BO");
		long transactionId = 0;
		pstmt1.executeUpdate();
		int isUpdate=0;
		ResultSet rs1 = pstmt1.getGeneratedKeys();
		if (rs1.next()) {
			transactionId = rs1.getLong(1);
			// insert into retailer transaction master
			pstmt1 = con.prepareStatement("INSERT INTO st_lms_bo_transaction_master (transaction_id,user_id,user_org_id,party_type,party_id,transaction_type,transaction_date) VALUES (?,?,?,?,?,?,?)");
			pstmt1.setLong(1, transactionId);
			pstmt1.setInt(2, userBean.getUserId());
			pstmt1.setInt(3, userBean.getUserOrgId());
			pstmt1.setString(4, "PLAYER");	
			pstmt1.setInt(5, plrId);
			pstmt1.setString(6, "OLA_DEPOSIT_REFUND_PLR");
			java.util.Date date = new java.util.Date();
			pstmt1.setTimestamp(7, new java.sql.Timestamp(date.getTime()));
		
			isUpdate = pstmt1.executeUpdate();
		
			logger.info("inserted into bo transaction master"+isUpdate);


			pstmt1 = con
					.prepareStatement("insert into st_ola_bo_direct_plr_deposit_refund(bo_user_id,bo_org_id,transaction_id,wallet_id,plr_id,plr_alias, deposit_amt,cancel_reason,ref_transaction_id) values(?,?,?,?,?,?,?,?,?)");
			pstmt1.setInt(1, userBean.getUserId());
			pstmt1.setInt(2, userBean.getUserOrgId());
			pstmt1.setLong(3, transactionId);
			pstmt1.setInt(4, walletId);
			pstmt1.setInt(5,plrId);
			pstmt1.setString(6,plrName);
			pstmt1.setDouble(7, depositAmt);
			pstmt1.setString(8, "CANCEL_SERVER");
			pstmt1.setDouble(9, reftransactionId);
			isUpdate=pstmt1.executeUpdate();
				
			logger.info("inserted into st_ola_bo_direct_plr_deposit_refund "+isUpdate);
	
		} else {
			return false;
		}
		return true;
	}	
	public static boolean checkPlrBinding(Connection con,String plr_id,String walletName){
		PreparedStatement ps =null;
		ResultSet rs=null;
		try{
			String query = " select player_id from	(select player_id,wallet_id from st_ola_affiliate_plr_mapping where player_id=?)pm "
							+ "inner join	(select wallet_id from st_ola_wallet_master where wallet_name=?)wm on pm.wallet_id=wm.wallet_id";
			ps	= con.prepareStatement(query);
			ps.setString(1, plr_id);
			ps.setString(2,walletName);
			logger.info("checkPlrBinding Query"+ps);
			rs = ps.executeQuery();
			if(rs.next()){
				return true;
			}
			else{
				return false;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(ps!=null){
					ps.close();
				}
				if(rs!=null){
					ps.close();
				}
				
			}catch(Exception e){
				e.printStackTrace();
				
			}
			
		}
		
		return false;
	}	
	public static synchronized String depositMoneyForKpRummy(
			String depositAnyWhere, String plrName, double depositAmt,
			UserInfoBean userBean, String walletName, int walletId,
			String userPhone) throws LMSException {

		Connection con = null;

		double retailerComm = 0.0;
		double agentComm = 0.0;
		double retNetAmt = 0.0;
		double agentNetAmt = 0.0;
		long imsTransactionId = 0;
		long agentRefTransactionId = 0;
		int isUpdate;

		try {
			con = DBConnect.getConnection();
			int agentOrgId = userBean.getParentOrgId();
			int retOrgId = userBean.getUserOrgId();

			retailerComm = CommonFunctionsHelper.fetchOLACommOfOrganization(
					walletId, retOrgId, "DEPOSIT", "RETAILER", con);
			agentComm = CommonFunctionsHelper.fetchOLACommOfOrganization(
					walletId, agentOrgId, "DEPOSIT", "AGENT", con);

			// check with organizations limit
			CommonFunctionsHelper commonFunction = new CommonFunctionsHelper();
			OrgPwtLimitBean orgPwtLimit = commonFunction.fetchPwtLimitsOfOrgnization(retOrgId, con);
			if (orgPwtLimit == null) { // send mail to back office
				logger.info("OLA Limits Are Not defined Properly!!");
				throw new LMSException("OLA Limits Are Not defined Properly!!");
			}
			double olaDepositLimit = orgPwtLimit.getOlaDepositLimit();
			
			logger.info("olaDepositLimit" + olaDepositLimit);
			logger.info("ola deposite money" + depositAmt);

			if (depositAmt > olaDepositLimit) {
				logger.info("Deposit amount is greater then deposit limit");

				return "Deposit amount is greater then deposit limit";
				// return "Deposit amount is greater then deposit limit";
			}
			// check with retailer and agent balance to deposit
			OlaHelper olahelper = new OlaHelper();
			int isCheck = olahelper.checkOrgBalance(depositAmt, retOrgId,
					agentOrgId, con, retailerComm, agentComm);
			logger.info("ischeck" + isCheck);

			if (isCheck == -1) {
				// Agent has insufficient

				return "Agent has insufficient Balance";
				// return "Agent has insufficient";

			} else if (isCheck == -2) {
				// Error LMS

				return "Error LMS";
				// return "Error LMS";
			} else if (isCheck == 0) {
				// Retailer has insufficient

				return "Retailer has insufficient Balance ";
				// return "Retailer has insufficient";
			}
			// insert in LMS transaction master
			if (isCheck == 2) {
				con.setAutoCommit(false);

				/*String isBinding = OLAUtility.affiliatePlrBindingKpRummy(depositAnyWhere, plrName, userBean.getUserName(),
																		userBean.getUserOrgId(), walletId, con, walletName);*/
				String isBinding =	OLAUtility.affiliatePlrBinding(depositAnyWhere,plrName, userBean,walletId,con); 
				logger.info("isBinding :" + isBinding);

				if (isBinding.equalsIgnoreCase("OK")) {
					String insertInLMS = "insert into st_lms_transaction_master(user_type,service_code,interface) values('RETAILER','OLA','WEB')";
					PreparedStatement pstmt1 = con
							.prepareStatement(insertInLMS);
					long transactionId = 0;
					pstmt1.executeUpdate();
					ResultSet rs1 = pstmt1.getGeneratedKeys();
					if (rs1.next()) {
						transactionId = rs1.getLong(1);
						// insert into retailer transaction master
						pstmt1 = con
								.prepareStatement("INSERT INTO st_lms_retailer_transaction_master (transaction_id,retailer_user_id,retailer_org_id,game_id,transaction_date,transaction_type) VALUES (?,?,?,?,?,?)");
						pstmt1.setLong(1, transactionId);
						pstmt1.setInt(2, userBean.getUserId());
						pstmt1.setInt(3, userBean.getUserOrgId());
						pstmt1.setInt(4, walletId);
						java.util.Date date = new java.util.Date();
						pstmt1.setTimestamp(5, new java.sql.Timestamp(date.getTime()));
						pstmt1.setString(6, "OLA_DEPOSIT");
						isUpdate = pstmt1.executeUpdate();
						logger.info("insert into retailer transaction master"+ isUpdate);
						// insert in deposit master
						retNetAmt = (depositAmt - ((depositAmt * retailerComm) / 100));
						agentNetAmt = (depositAmt - ((depositAmt * agentComm) / 100));
						String insertQry = "insert into st_ola_ret_deposit(transaction_id, wallet_id, party_id, retailer_org_id, deposit_amt, retailer_comm, net_amt, agent_comm, agent_net_amt, agent_ref_transaction_id, claim_status, deposit_channel, ims_ref_transaction_id) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
						PreparedStatement pstmtUpdate = con.prepareStatement(insertQry);
						pstmtUpdate.setLong(1, transactionId);
						pstmtUpdate.setInt(2, walletId);
						pstmtUpdate.setString(3, plrName);
						pstmtUpdate.setInt(4, userBean.getUserOrgId());
						pstmtUpdate.setDouble(5, depositAmt);
						pstmtUpdate.setDouble(6, retailerComm);
						pstmtUpdate.setDouble(7, retNetAmt);
						pstmtUpdate.setDouble(8, agentComm);
						pstmtUpdate.setDouble(9, agentNetAmt);
						pstmtUpdate.setLong(10, agentRefTransactionId);
						pstmtUpdate.setString(11, "CLAIM_BAL");
						pstmtUpdate.setString(12, "WEB");
						pstmtUpdate.setLong(13, imsTransactionId);
						pstmtUpdate.executeUpdate();

						// update st_lms_organization_master for claimable
						// balance for retailer
						boolean isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(retNetAmt, "CLAIM_BAL", "CREDIT", userBean.getUserOrgId(),0, userBean.getUserType(), 0, con);
						if (!isValid)
							throw new LMSException();
						isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(agentNetAmt, "CLAIM_BAL", "CREDIT",userBean.getParentOrgId(),0, userBean.getUserType(), 0, con);
						if (!isValid)
							throw new LMSException();
					/*	CommonFunctionsHelper commHelper = new CommonFunctionsHelper();
						commHelper.updateOrgBalance("CLAIM_BAL", retNetAmt,
								userBean.getUserOrgId(), "CREDIT", con);

						// update st_lms_organization_master for claimable
						// balance for agent
						commHelper.updateOrgBalance("CLAIM_BAL", agentNetAmt,
								userBean.getParentOrgId(), "CREDIT", con);*/
						con.commit();
						logger.info("in ola helper amount is deposit Successfully");
						// Call kpRummy Api

						Map<String, String> depositRespMap = null;
						depositRespMap = sendDepositInfoToKpRummy(walletId,
								plrName, depositAmt, transactionId);
						logger.info("Deposit API Response" + depositRespMap);
						if (depositRespMap == null) {
							logger.info("player binding msg:" + depositRespMap);
							boolean isRefund = depositeRefund(depositAmt,retNetAmt, agentNetAmt, retailerComm,agentComm,
														plrName, con, walletId,userBean, transactionId, imsTransactionId);
							if (isRefund) {
								con.commit();
								logger
										.info("Error In Player Mgmt  Deposit. Amount Refunded Successfully");
								return "Error In Player Lottery Deposit. Amount Refunded Successfully";
							} else {
								logger.info("Error In LMS Deposit Refund");
								return "Error In LMS Deposit Refund";
							}
						} else if (depositRespMap.get("respMsg") != null) {
							logger.info("Deposit Successful At Kp Rummy:"
									+ depositRespMap.get("respMsg"));
							imsTransactionId = Long.parseLong(depositRespMap.get("requestId"));
							pstmt1 = con.prepareStatement("update st_ola_ret_deposit set ims_ref_transaction_id=? where transaction_id=?");
							pstmt1.setLong(1, imsTransactionId);
							pstmt1.setLong(2, transactionId);
							pstmt1.executeUpdate();
							con.commit();
						
							String msg ="Dear Customer, Your Deposit Request of Amt:"+depositAmt+" has been initiated with PlrName:"+plrName+",please visit the cashier page at khelplayrummy.com ";
							SendSMS smsSend = new SendSMS(msg,userPhone);
							smsSend.setDaemon(true);
							smsSend.start();
							System.out.println(" SMS Sent");
							return "true";

						} else {
							logger.info("player binding msg:"+ depositRespMap.get("errorMsg") + "Code:"+ depositRespMap.get("errorCode"));
							boolean isRefund = depositeRefund(depositAmt,retNetAmt, agentNetAmt, retailerComm,agentComm, plrName, con, walletId,
													userBean, transactionId, imsTransactionId);
							if (isRefund) {
								con.commit();
								logger
										.info("Error In Player Mgmt  Deposit. Amount Refunded Successfully");
								return "Error In Player Lottery Deposit. Amount Refunded Successfully";
							} else {
								logger.info("Error In LMS Deposit Refund");
								return "Error In LMS Deposit Refund";
							}

						}

						// transactionId
					} else {
						logger
								.info("Trabsaction Id is not Generated in LMS transaction master");

						return "error in Deposit the money";

					}
				} else {

					return isBinding;
				}

			} else {
				logger.info("Error During balance verification");

				return "Error During balance verification";

			}

		}

		catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("Error during deposit");
		} finally {
			DBConnect.closeCon(con);

		}

	}
	public static boolean depositeRefund(double depositAmt, double retNetAmt,
			double agentNetAmt, double retailerComm, double agentComm,
			String userName, Connection con, int walletId,
			UserInfoBean userBean, long depositTransactionId,
			long imsTransactionID) throws SQLException {

		// update in transaction master
		String insertInLMS = "insert into st_lms_transaction_master(user_type,service_code,interface) values('RETAILER','OLA','WEB')";
		PreparedStatement pstmt1 = con.prepareStatement(insertInLMS);
		long transactionId = 0;
		pstmt1.executeUpdate();
		ResultSet rs1 = pstmt1.getGeneratedKeys();
		if (rs1.next()) {
			transactionId = rs1.getLong(1);
			// insert into retailer transaction master
			pstmt1 = con
					.prepareStatement("INSERT INTO st_lms_retailer_transaction_master (transaction_id,retailer_user_id,retailer_org_id,game_id,transaction_date,transaction_type) VALUES (?,?,?,?,?,?)");
			pstmt1.setLong(1, transactionId);
			pstmt1.setInt(2, userBean.getUserId());
			pstmt1.setInt(3, userBean.getUserOrgId());
			pstmt1.setInt(4, walletId);
			pstmt1
					.setTimestamp(5, new java.sql.Timestamp(new Date()
							.getTime()));
			pstmt1.setString(6, "OLA_DEPOSIT_REFUND");
			pstmt1.executeUpdate();

			pstmt1 = con
					.prepareStatement("insert into st_ola_ret_deposit_refund(transaction_id, wallet_id, retailer_org_id, ims_ref_transaction_id, ola_ref_transaction_id, claim_status, cancel_reason, deposit_amt, net_amt, agent_net_amt, retailer_comm, agent_comm, agent_ref_transaction_id, party_id) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			pstmt1.setLong(1, transactionId);
			pstmt1.setInt(2, walletId);
			pstmt1.setInt(3, userBean.getUserOrgId());
			pstmt1.setLong(4, imsTransactionID);
			pstmt1.setLong(5, depositTransactionId);
			pstmt1.setString(6, "CLAIM_BAL");
			pstmt1.setString(7, "CANCEL_SERVER");
			pstmt1.setDouble(8, depositAmt);
			pstmt1.setDouble(9, retNetAmt);
			pstmt1.setDouble(10, agentNetAmt);
			pstmt1.setDouble(11, retailerComm);
			pstmt1.setDouble(12, agentComm);
			pstmt1.setInt(13, 0);
			pstmt1.setString(14, userName);
			pstmt1.executeUpdate();
			// update ret_comm in st_ola_ret_withdrawl
			// pstmt1 =
			// con.prepareStatement("update st_ola_ret_deposit set retailer_comm=(retailer_comm-"+retailerComm+"),agent_comm=(agent_comm-"+agentComm+") where transaction_id="+depositTransactionId+"");
			// pstmt1.executeUpdate();

			// update st_lms_organization_master for claimable balance
			// for
			// retailer
			CommonFunctionsHelper commHelper = new CommonFunctionsHelper();
			commHelper.updateOrgBalance("CLAIM_BAL", retNetAmt, userBean
					.getUserOrgId(), "DEBIT", con);

			// update st_lms_organization_master for claimable balance
			// for
			// agent
			commHelper.updateOrgBalance("CLAIM_BAL", agentNetAmt, userBean
					.getParentOrgId(), "DEBIT", con);

		} else {
			return false;
		}
		return true;
	}
	public static Map<String, String> sendDepositInfoToKpRummy(int walletId, String plrName, double depositAmt,long transactionId) {
		Map<String, String> depositReqMap = new HashMap<String, String>();
		try{
			depositReqMap.put("requestType", "DEPOSIT");
			depositReqMap.put("domainName",OLAUtility.getWalletIntBean(walletId+"").getTpWalletCode());
			depositReqMap.put("userName", plrName);
			depositReqMap.put("amount", depositAmt + "");
			depositReqMap.put("depositMode", "OLA");
			depositReqMap.put("paymentType", "CASH_PAYMENT");
			depositReqMap.put("providerName", "OLA");
			depositReqMap.put("refTxnNo", transactionId + "");
			InputStream depositResponse = OLAClient.callKhelPlayRummy(OLAUtility.prepareXMLFromData(depositReqMap),walletId,OLAConstants.depReq);
			return OLAUtility.prepareDataFromXml(depositResponse);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	public static Map<String, String> verifyWithdrawalAtKpRummy(int walletId, String plrName, double withAmt,long transactionId,String verificationCode) {
		Map<String, String> depositReqMap = new HashMap<String, String>();
		try{
			depositReqMap.put("requestType", "WITHDRAWAL_SUCCESS");
			depositReqMap.put("domainName",OLAUtility.getWalletIntBean(walletId+"").getTpWalletCode());
			depositReqMap.put("userName", plrName);
			depositReqMap.put("amount", withAmt + "");
			depositReqMap.put("verficationCode", verificationCode);
			depositReqMap.put("refTxnNo", transactionId + "");
			InputStream depositResponse = OLAClient.callKhelPlayRummy(OLAUtility.prepareXMLFromData(depositReqMap),walletId,OLAConstants.depReq);
			return OLAUtility.prepareDataFromXml(depositResponse);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	public Map<String, String> verifyPlrName(String plrName, int walletId,
			String verificationType) throws LMSException {
		Map<String, String> errorMap = new TreeMap<String, String>();
		try {
			Map<String, String> verifyPlrReqMap = new HashMap<String, String>();
			verifyPlrReqMap.put("requestType", verificationType);
			verifyPlrReqMap.put("domainName",OLAUtility.getWalletIntBean(walletId+"").getTpWalletCode());
			verifyPlrReqMap.put("availabilityValue", plrName);
			InputStream verifyPlrResponse = OLAClient.callKhelPlayRummy(OLAUtility.prepareXMLFromData(verifyPlrReqMap),walletId,OLAConstants.depReq);
			Map<String, String> verifyPlrRespMap = null;
			verifyPlrRespMap = OLAUtility.prepareDataFromXml(verifyPlrResponse);
			logger.info(verifyPlrRespMap);
			if (verifyPlrRespMap == null) {
				logger.info("player availibility msg:" + verifyPlrRespMap);
				errorMap.put("userError", "Some Error");

			} else if (verifyPlrRespMap.get("errorCode") != null||verifyPlrRespMap.get("errorMsg")!=null) {
				logger.info("player availibility msg:"
						+ verifyPlrRespMap.get("errorMsg") + "Code:"
						+ verifyPlrRespMap.get("errorCode"));
				errorMap.put("userError", "User Name Not Exists !!");

			} else if (verifyPlrRespMap.get("respMsg") != null) {
				logger.info("player availibility msg:"
						+ verifyPlrRespMap.get("respMsg"));
				errorMap.put("userError", "Avail");

			}
		} catch(LMSException e){
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return errorMap;

	}
	
	public static OlaPlayerRegistrationRequestBean getPlayerInfoFromKP(int walletId, String refCode,String walletName) throws LMSException {
		Map<String, String> depositReqMap = new HashMap<String, String>();
		try{
			depositReqMap.put("requestType", "GET_PLAYER_INFO");
			depositReqMap.put("domainName",OLAUtility.getWalletIntBean(walletId+"").getTpWalletCode());
			if("ALA_WALLET".equalsIgnoreCase(walletName)){
				depositReqMap.put("userName", refCode);
			}else{
				depositReqMap.put("mobileNo", refCode);
			}
			InputStream depositResponse = OLAClient.callKhelPlayRummy(OLAUtility.prepareXMLFromData(depositReqMap),walletId,OLAConstants.depReq);
			return OLAUtility.preparePlayerInfoBeanDataFromXml(depositResponse);
		}catch(LMSException e){
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}catch(Exception e){
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}
	

}
