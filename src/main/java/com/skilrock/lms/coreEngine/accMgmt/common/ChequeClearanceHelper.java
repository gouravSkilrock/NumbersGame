package com.skilrock.lms.coreEngine.accMgmt.common;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.AgentReceiptBean;
import com.skilrock.lms.beans.chequeForClearanceBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.db.TableConstants;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.GenerateRecieptNo;
import com.skilrock.lms.common.utility.OrgCreditUpdation;
import com.skilrock.lms.web.drawGames.common.Util;

public class ChequeClearanceHelper {
	static Log logger = LogFactory.getLog(ChequeClearanceHelper.class);
	public List<AgentReceiptBean> doChequeClear(String[] chequeNbr,
			Double[] chequeAmt, String[] chequeStatus, Double[] commAmt,
			String[] agtOrgName, String[] isCleared, String[] draweeBank,
			Date[] chequeDate, String[] issuingPartyName,
			Date[] chequeClearanceDate, String userType, String rootPath,
			String boOrgName, int userOrgID, Date[] clearanceDate, int userId,
			String[] taskId) throws LMSException {

		 
		PreparedStatement pstmtBOtrans;
		PreparedStatement pstmtSaleChq;
		PreparedStatement pstmtTempChq;
		PreparedStatement preStateChq3 = null;
		PreparedStatement preState4 = null;
		PreparedStatement preState6 = null;
		PreparedStatement preState7 = null;
		Connection con = DBConnect.getConnection();

		String query5 = "insert into st_lms_bo_debit_note(transaction_id ,agent_org_id,amount,transaction_type,remarks) values(?,?,?,?,?)";
		// String query3=QueryManager.getST5BOReceiptIdQuery();
		// String queryChq2 = QueryManager.getST5BOReceiptMappingQuery();
		String queryBOTrans = QueryManager.insertInBOTransactionMaster();
		String queryLMSTrans = QueryManager.insertInLMSTransactionMaster();

		try {

			AgentReceiptBean agtRcptBean;
			Set<String> orgNameSet = new TreeSet<String>();

			for (String agentOrgName : agtOrgName) {
				orgNameSet.add(agentOrgName);
			}

			List<Long> tranIdListClear = null;
			List<Long> tranIdListCancel = null;
			List<AgentReceiptBean> agtReciptbeanlist = new ArrayList<AgentReceiptBean>();

			String newChequeStatus;

			int orgId = 0;
			//int transId = 0;
			long transId=0;
			con.setAutoCommit(false);

			for (String orgAgtName : orgNameSet) {

				double totalChequeAmt = 0.0;
				double totalDebitedAmt = 0.0;

				int clearanceId = 0;
				int cancellationId = 0;

				boolean isClear = false;
				boolean isCancel = false;

				tranIdListClear = new ArrayList<Long>();
				tranIdListCancel = new ArrayList<Long>();

				agtRcptBean = new AgentReceiptBean();
				orgId = Integer.parseInt(orgAgtName);//getOrgIdFromOrgName(orgAgtName, con);
				agtRcptBean.setAgtOrgId(orgId);
				agtRcptBean.setAgtOrgName(orgAgtName);
				for (int i = 0; i < chequeNbr.length; i++) {

					if (orgAgtName.equals(agtOrgName[i])) {

						logger.debug("org id is " + orgId);

						if (isCleared[i].equals("Cleared"))

						{
							isClear = true;
							logger.debug("inside cleared");
							newChequeStatus = "CLEARED";

							// insert into LMS transaction master

							pstmtBOtrans = con.prepareStatement(queryLMSTrans);
							pstmtBOtrans.setString(1, "BO");
							pstmtBOtrans.executeUpdate();

							ResultSet rsTransId = pstmtBOtrans
									.getGeneratedKeys();
							if (rsTransId.next()) {
								transId = rsTransId.getLong(1);
							}
							tranIdListClear.add(transId); // added to insert
							// entry into
							// receipt
							// tansaction
							// mapping table

							pstmtBOtrans = con.prepareStatement(queryBOTrans);

							pstmtBOtrans.setLong(1, transId);
							pstmtBOtrans.setInt(2, userId);
							pstmtBOtrans.setInt(3, userOrgID);
							pstmtBOtrans.setString(4, "AGENT");
							pstmtBOtrans.setInt(5, orgId);
							pstmtBOtrans.setTimestamp(6,
									new java.sql.Timestamp(new java.util.Date()
											.getTime()));
							pstmtBOtrans.setString(7, "CHEQUE");

							pstmtBOtrans.executeUpdate();

							// insert into st bo sale cheque table

							String insertSaleCheque = "insert into st_lms_bo_sale_chq(transaction_id,agent_org_id,cheque_nbr,cheque_date,issuing_party_name,drawee_bank,cheque_amt,transaction_type) values(?,?,?,?,?,?,?,?)";
							pstmtSaleChq = con
									.prepareStatement(insertSaleCheque);
							pstmtSaleChq.setLong(1, transId);
							pstmtSaleChq.setInt(2, orgId);
							pstmtSaleChq.setString(3, chequeNbr[i]);
							pstmtSaleChq.setDate(4, chequeDate[i]);
							pstmtSaleChq.setString(5, issuingPartyName[i]);
							pstmtSaleChq.setString(6, draweeBank[i]);
							pstmtSaleChq.setDouble(7, chequeAmt[i]);
							pstmtSaleChq.setString(8, "CHEQUE");
							pstmtSaleChq.executeUpdate();
							logger
									.debug("ccccccccccccccccccccccccccccccccccccc");

							// update cheque temporary table

							String updateChqTemp = "update st_lms_bo_cheque_temp_receipt set transaction_id=? , cheque_clearance_date=?,cheque_status=? where cheque_nbr=? and agent_org_id=? and task_id=?";
							pstmtTempChq = con.prepareStatement(updateChqTemp);
							pstmtTempChq.setLong(1, transId);
							pstmtTempChq.setDate(2, clearanceDate[i]);
							pstmtTempChq.setString(3, newChequeStatus);
							pstmtTempChq.setString(4, chequeNbr[i]);
							pstmtTempChq.setInt(5, orgId);
							pstmtTempChq.setInt(6, Integer.parseInt(taskId[i]));
							logger
									.debug("query for changing the status is in Cleared cheque:"
											+ pstmtTempChq);
							pstmtTempChq.executeUpdate();
							logger.debug("rtttttttttttttttttttttttttttttt");

							totalChequeAmt = totalChequeAmt + chequeAmt[i];

						}

						else {
							isCancel = true;
							logger.debug("inside cancel ");
							newChequeStatus = "CANCEL";
							//int transIdforCheque = 0;
							//int transIdforBounce = 0;
								long transIdforCheque=0;
								long transIdforBounce=0;
							// insert into LMS transaction master for cheque

							// String queryLMSTrans =
							// QueryManager.insertInLMSTransactionMaster();
							pstmtBOtrans = con.prepareStatement(queryLMSTrans);
							pstmtBOtrans.setString(1, "BO");
							pstmtBOtrans.executeUpdate();

							ResultSet rsTransId = pstmtBOtrans
									.getGeneratedKeys();
							if (rsTransId.next()) {
								transIdforCheque = rsTransId.getLong(1);
							}
							tranIdListClear.add(transIdforCheque);

							// insert entry into bo transaction master for
							// cheque

							pstmtBOtrans = con.prepareStatement(queryBOTrans);

							pstmtBOtrans.setLong(1, transIdforCheque);
							pstmtBOtrans.setInt(2, userId);
							pstmtBOtrans.setInt(3, userOrgID);
							pstmtBOtrans.setString(4, "AGENT");
							pstmtBOtrans.setInt(5, orgId);
							pstmtBOtrans.setTimestamp(6,
									new java.sql.Timestamp(new java.util.Date()
											.getTime()));
							pstmtBOtrans.setString(7, "CHEQUE");

							pstmtBOtrans.executeUpdate();
							logger
									.debug("transaction id for cheque is for cancellation is "
											+ transIdforCheque);

							// insert into LMS transaction master for cheque
							// bounce

							pstmtBOtrans = con.prepareStatement(queryLMSTrans);
							pstmtBOtrans.setString(1, "BO");
							pstmtBOtrans.executeUpdate();

							rsTransId = pstmtBOtrans.getGeneratedKeys();
							if (rsTransId.next()) {
								transIdforBounce = rsTransId.getLong(1);
							}
							tranIdListCancel.add(transIdforBounce); // added to
							// insert
							// entry
							// into
							// receipt
							// tansaction
							// mapping
							// table

							// insert entry into bo transaction master for
							// cheque bounce

							pstmtBOtrans = con.prepareStatement(queryBOTrans);

							pstmtBOtrans.setLong(1, transIdforBounce);
							pstmtBOtrans.setInt(2, userId);
							pstmtBOtrans.setInt(3, userOrgID);
							pstmtBOtrans.setString(4, "AGENT");
							pstmtBOtrans.setInt(5, orgId);
							pstmtBOtrans.setTimestamp(6,
									new java.sql.Timestamp(new java.util.Date()
											.getTime()));
							pstmtBOtrans.setString(7, "CHQ_BOUNCE");

							pstmtBOtrans.executeUpdate();

							// insert into LMS transaction master for debit note

							pstmtBOtrans = con.prepareStatement(queryLMSTrans);
							pstmtBOtrans.setString(1, "BO");
							pstmtBOtrans.executeUpdate();

							// insert entry into bo transaction master for debit
							// note

							rsTransId = pstmtBOtrans.getGeneratedKeys();
							if (rsTransId.next()) {
								transId = rsTransId.getLong(1);
							}
							tranIdListCancel.add(transId); // added to insert
							// entry into
							// receipt
							// tansaction
							// mapping table

							// String insertTransMaster="insert into
							// st_lms_bo_transaction_master(party_type,party_id,transaction_date,transaction_type)
							// values(?,?,?,?)";
							pstmtBOtrans = con.prepareStatement(queryBOTrans);

							pstmtBOtrans.setLong(1, transId);
							pstmtBOtrans.setInt(2, userId);
							pstmtBOtrans.setInt(3, userOrgID);
							pstmtBOtrans.setString(4, "AGENT");
							pstmtBOtrans.setInt(5, orgId);
							pstmtBOtrans.setTimestamp(6,
									new java.sql.Timestamp(new java.util.Date()
											.getTime()));
							pstmtBOtrans.setString(7, "DR_NOTE");

							pstmtBOtrans.executeUpdate();

							// insert entry into debit note table
							preState6 = con.prepareStatement(query5);
							preState6.setLong(1, transId);
							preState6.setInt(2, orgId);
							preState6.setDouble(3, commAmt[i]);
							preState6.setString(4, "DR_NOTE");
							preState6.setString(5, "cheque bounce charges("
									+ chequeNbr[i] + ")");
							preState6.executeUpdate();

							// insert into st bo sale cheque table for CLOSED

							String insertSaleCheque = "insert into st_lms_bo_sale_chq(transaction_id,agent_org_id,cheque_nbr,cheque_date,issuing_party_name,drawee_bank,cheque_amt,transaction_type) values(?,?,?,?,?,?,?,?)";
							pstmtSaleChq = con
									.prepareStatement(insertSaleCheque);
							pstmtSaleChq.setLong(1, transIdforCheque);
							pstmtSaleChq.setInt(2, orgId);
							pstmtSaleChq.setString(3, chequeNbr[i]);
							pstmtSaleChq.setDate(4, chequeDate[i]);
							pstmtSaleChq.setString(5, issuingPartyName[i]);
							pstmtSaleChq.setString(6, draweeBank[i]);
							pstmtSaleChq.setDouble(7, chequeAmt[i]);
							pstmtSaleChq.setString(8, "CLOSED");
							pstmtSaleChq.executeUpdate();
							logger
									.debug("ccccccccccccccccccccccccccccccccccccc");

							// insert into st bo sale cheque table for
							// chque_bounce

							// String insertSaleCheque="insert into
							// st_lms_bo_sale_chq(transaction_id,agent_org_id,cheque_nbr,cheque_date,issuing_party_name,drawee_bank,cheque_amt,transaction_type)
							// values(?,?,?,?,?,?,?,?)";
							pstmtSaleChq = con
									.prepareStatement(insertSaleCheque);
							pstmtSaleChq.setLong(1, transIdforBounce);
							pstmtSaleChq.setInt(2, orgId);
							pstmtSaleChq.setString(3, chequeNbr[i]);
							pstmtSaleChq.setDate(4, chequeDate[i]);
							pstmtSaleChq.setString(5, issuingPartyName[i]);
							pstmtSaleChq.setString(6, draweeBank[i]);
							pstmtSaleChq.setDouble(7, chequeAmt[i]);
							pstmtSaleChq.setString(8, "CHQ_BOUNCE");
							pstmtSaleChq.executeUpdate();
							logger
									.debug("ccccccccccccccccccccccccccccccccccccc");

							// update cheque temporary table

							String updateChqTemp = "update st_lms_bo_cheque_temp_receipt set transaction_id=? , cheque_clearance_date=?,cheque_status=? where cheque_nbr=? and agent_org_id=? and task_id=?";
							pstmtTempChq = con.prepareStatement(updateChqTemp);
							pstmtTempChq.setLong(1, transIdforCheque);
							pstmtTempChq.setDate(2, clearanceDate[i]);
							pstmtTempChq.setString(3, newChequeStatus);
							pstmtTempChq.setString(4, chequeNbr[i]);
							pstmtTempChq.setInt(5, orgId);
							pstmtTempChq.setInt(6, Integer.parseInt(taskId[i]));
							logger.debug("query is :: " + pstmtTempChq);
							pstmtTempChq.executeUpdate();
							logger.debug("rtttttttttttttttttttttttttttttt");

							totalChequeAmt = totalChequeAmt + chequeAmt[i];
							// totalCommAmt=totalCommAmt+commAmt[i];
							// total debited amount
							totalDebitedAmt = totalDebitedAmt + commAmt[i]
									+ chequeAmt[i];

						}
					}
				}

				String autoGeneRecieptNoCancel = null;
				String autoGeneRecieptNoChequeCancel = null;

				if (isClear || isCancel) {
					ResultSet recieptRsCancel = null;
					// get auto generated receipt numner for cheque while cheque
					// cancellation

					preState7 = con.prepareStatement(QueryManager
							.getBOLatestReceiptNb());
					preState7.setString(1, "RECEIPT");
					// preState7.setString(2,"DR_NOTE_CASH");
					recieptRsCancel = preState7.executeQuery();
					String lastRecieptNoGeneratedChequeCancel = null;

					while (recieptRsCancel.next()) {
						lastRecieptNoGeneratedChequeCancel = recieptRsCancel
								.getString("generated_id");
					}

					autoGeneRecieptNoChequeCancel = GenerateRecieptNo
							.getRecieptNo("RECEIPT",
									lastRecieptNoGeneratedChequeCancel,
									userType);
					// get auto generated number for debit note while cheque
					// cancellation
					// String getLatestRecieptNumberCancel="SELECT * from
					// st_bo_receipt_gen_mapping where receipt_type=? or
					// receipt_type=? ORDER BY generated_id DESC LIMIT 1 ";
					if (isCancel) {
						preState7 = con.prepareStatement(QueryManager
								.getBOLatestDRNoteNb());
						preState7.setString(1, "DR_NOTE");
						preState7.setString(2, "DR_NOTE_CASH");
						recieptRsCancel = preState7.executeQuery();
						String lastRecieptNoGeneratedCancel = null;

						while (recieptRsCancel.next()) {
							lastRecieptNoGeneratedCancel = recieptRsCancel
									.getString("generated_id");
						}

						autoGeneRecieptNoCancel = GenerateRecieptNo
								.getRecieptNo("DR_NOTE",
										lastRecieptNoGeneratedCancel, userType);

					}
				}

				ResultSet rs1;
				// insert into reciept table for clearance

				// insert into reciept table for cancellation

				if (isCancel || isClear) {
					// insert in receipt master for cheque while cancellation
					preState4 = con.prepareStatement(QueryManager
							.insertInReceiptMaster());
					preState4.setString(1, "BO");
					preState4.executeUpdate();

					rs1 = preState4.getGeneratedKeys();
					rs1.next();
					clearanceId = rs1.getInt(1);
					preState4 = con.prepareStatement(QueryManager
							.insertInBOReceipts());

					preState4.setInt(1, clearanceId);
					preState4.setString(2, "RECEIPT");
					preState4.setInt(3, orgId);
					preState4.setString(4, "AGENT");
					preState4.setString(5, autoGeneRecieptNoChequeCancel);
					preState4.setTimestamp(6, Util.getCurrentTimeStamp());
					/*
					 * preState4.setString(1, "DR_NOTE"); preState4.setInt(2,
					 * orgId);
					 */

					preState4.executeUpdate();

					if (isCancel) {

						// insert in receipt master for debit note while
						// cancellation
						preState4 = con.prepareStatement(QueryManager
								.insertInReceiptMaster());
						preState4.setString(1, "BO");
						preState4.executeUpdate();

						rs1 = preState4.getGeneratedKeys();
						rs1.next();
						cancellationId = rs1.getInt(1);

						preState4 = con.prepareStatement(QueryManager
								.insertInBOReceipts());

						preState4.setInt(1, cancellationId);
						preState4.setString(2, "DR_NOTE");
						preState4.setInt(3, orgId);
						preState4.setString(4, "AGENT");
						preState4.setString(5, autoGeneRecieptNoCancel);
						preState4.setTimestamp(6, new java.sql.Timestamp(new java.util.Date().getTime()));
						/*
						 * preState4.setString(1, "DR_NOTE");
						 * preState4.setInt(2, orgId);
						 */

						preState4.executeUpdate();
					}
				}
				agtRcptBean.setReceiptIdForCancel(cancellationId);
				agtRcptBean.setReceiptIdForClear(clearanceId);

				// insert into transaction mapping for clearance
				if (tranIdListClear != null && tranIdListClear.size() > 0) {
					for (int j = 0; j < tranIdListClear.size(); j++) {
						preStateChq3 = con.prepareStatement(QueryManager
								.insertBOReceiptTrnMapping());
						preStateChq3.setInt(1, clearanceId);
						preStateChq3.setLong(2, tranIdListClear.get(j));
						preStateChq3.executeUpdate();
					}

				}
				// insert into transaction mapping for cancellation

				if (tranIdListCancel != null && tranIdListCancel.size() > 0) {
					for (int j = 0; j < tranIdListCancel.size(); j++) {
						preStateChq3 = con.prepareStatement(QueryManager
								.insertBOReceiptTrnMapping());
						preStateChq3.setInt(1, cancellationId);
						preStateChq3.setLong(2, tranIdListCancel.get(j));
						preStateChq3.executeUpdate();
					}

				}

				// agtRcptBean.setTransIdClearList(tranIdListClear);
				// agtRcptBean.setTransIdCancelList(tranIdListCancel);
				logger.debug("debited by " + totalDebitedAmt);
				agtRcptBean.setAmountCreditedBy(totalChequeAmt);
				agtRcptBean.setAmountDebitedBy(totalDebitedAmt);
				agtReciptbeanlist.add(agtRcptBean);

				//Only One Method use for Org Balanace Update
				boolean isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(totalChequeAmt, "TRANSACTION", "CHEQUE", orgId,
						0, "AGENT", 0, con);
				if(isValid){
					isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(totalDebitedAmt, "TRANSACTION", "DR_NOTE_CASH", orgId,
							0, "AGENT", 0, con);
				}
				
				if(isValid)
					con.commit();
				else
					throw new LMSException();
				/*OrgCreditUpdation.updateCreditLimitForAgent(orgId, "CHEQUE",
						totalChequeAmt, con);

				OrgCreditUpdation.updateCreditLimitForAgent(orgId,
						"DR_NOTE_CASH", totalDebitedAmt, con);*/

			}

			

			return agtReciptbeanlist;

		} catch (SQLException e) {

			e.printStackTrace();

		} finally {

			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}

		}

		return null;

	}
/**
 * @deprecated  because of duplicate as getOrganizations in  AgentPaymentChequeHelper
 * 
 * @return
 */
	public List<String> getAgtOrgList() {

		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;

		List<String> orgNameResults = new ArrayList<String>();
		try {
			 
			connection = DBConnect.getConnection();
			statement = connection.createStatement();

			String query = "select name from st_lms_organization_master where organization_type='AGENT' and organization_status!='TERMINATE' order by name";
			logger.debug("-----Query----::" + query);
			resultSet = statement.executeQuery(query);
			while (resultSet.next()) {

				orgNameResults.add(resultSet.getString(TableConstants.NAME));

			}
			return orgNameResults;
		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException se) {
				logger.error("Exception: " + se);
				se.printStackTrace();
			}
		}
		return null;
	}

	public int getOrgIdFromOrgName(String orgName, Connection conn) {

		int orgId = 0;
		PreparedStatement pstmtOrg;
		String queryGetOrgId = "select organization_id from st_lms_organization_master where name=?";
		try {
			pstmtOrg = conn.prepareStatement(queryGetOrgId);
			pstmtOrg.setString(1, orgName);
			ResultSet idSet = pstmtOrg.executeQuery();
			if (idSet.next()) {
				orgId = idSet.getInt("organization_id");
			}

			return orgId;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return orgId;
	}

	public List<chequeForClearanceBean> searchPendingCheques(String chkNbr,
			String draweeBnk, String chqStatus, String agentOrgName) {
		List<chequeForClearanceBean> pendingChqList = new ArrayList<chequeForClearanceBean>();

		 
		PreparedStatement pstmtSearchChq;
		Connection con = DBConnect.getConnection();
		chequeForClearanceBean chqClearanceBean;
		try {
			// get org iD from org name
			String orgCodeQry = " b.name orgCode ";

			
			if ((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE")) {
				orgCodeQry = " b.org_code orgCode";
	

			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("CODE_NAME")) {
				orgCodeQry = " concat(b.org_code,'_',b.name)  orgCode ";
			

			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("NAME_CODE")) {
				orgCodeQry = " concat(b.name,'_',b.org_code)  orgCode";
			

			}	
			int agtOrgId = Integer.parseInt(agentOrgName);//sgetOrgIdFromOrgName(agentOrgName, con);

			StringBuilder searchCheque = new StringBuilder(
					"select "+orgCodeQry+",b.organization_id agtOrgId,a.cheque_nbr,a.agent_org_id,a.cheque_amt,a.cheque_date,a.cheque_receiving_date,a.cheque_clearance_date,a.cheque_status,a.drawee_bank,a.issuing_party_name,a.task_id from st_lms_bo_cheque_temp_receipt a,st_lms_organization_master b where b.organization_id=a.agent_org_id ");

			if (chkNbr.trim() != null && !"".equals(chkNbr.trim())) {
				searchCheque.append("and a.cheque_nbr='" + chkNbr + "' ");
			}

			if (draweeBnk.trim() != null && !"".equals(draweeBnk.trim())) {
				searchCheque.append("and a.drawee_bank like '" + draweeBnk
						+ "%' ");
			}

			if (chqStatus.trim() != null && !"".equals(chqStatus.trim())) {
				searchCheque.append("and a.cheque_status='" + chqStatus + "' ");
			}

			logger.debug("%%%%%%%%%%%%%%%% agt org name is  "
					+ agentOrgName.trim());

			if (agentOrgName.trim() != null && !"".equals(agentOrgName.trim())
					&& !agentOrgName.trim().equals("-1")) {
				searchCheque.append("and a.agent_org_id=" + agtOrgId);
			}

			logger.debug("Query is " + searchCheque);
			pstmtSearchChq = con.prepareStatement(searchCheque.toString());
			ResultSet rsCheque = pstmtSearchChq.executeQuery();
			while (rsCheque.next()) {

				chqClearanceBean = new chequeForClearanceBean();
				chqClearanceBean.setChequeNbr(rsCheque.getString("cheque_nbr"));
				chqClearanceBean.setAgtOrgId(rsCheque.getInt("agent_org_id"));
				chqClearanceBean.setChequeAmt(rsCheque.getDouble("cheque_amt"));
				chqClearanceBean.setChequeDate(rsCheque.getDate("cheque_date"));
				chqClearanceBean.setChequeReceivingDate(rsCheque
						.getDate("cheque_receiving_date"));
				chqClearanceBean.setChequeStatus(rsCheque
						.getString("cheque_status"));
				chqClearanceBean.setClearanceDate(rsCheque
						.getDate("cheque_clearance_date"));
				chqClearanceBean.setDraweeBank(rsCheque
						.getString("drawee_bank"));
				chqClearanceBean.setIssuingPartyName(rsCheque
						.getString("issuing_party_name"));
				chqClearanceBean.setAgtOrgName(rsCheque.getString("orgCode"));
				chqClearanceBean.setAgtOrgId(rsCheque.getInt("agtOrgId"));
				chqClearanceBean.setTaskId("" + rsCheque.getInt("task_id"));
				logger.debug("task id for the check"
						+ rsCheque.getString("cheque_nbr") + "is :" + ""
						+ rsCheque.getInt("task_id"));

				pendingChqList.add(chqClearanceBean);

			}

			return pendingChqList;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return null;

	}

}