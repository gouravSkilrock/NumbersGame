package com.skilrock.lms.coreEngine.scratchService.inventoryMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.GenerateRecieptNo;
import com.skilrock.lms.common.utility.OrgCreditUpdation;
import com.skilrock.lms.coreEngine.reportsMgmt.common.GraphReportHelper;
import com.skilrock.lms.web.drawGames.common.Util;

public class LooseSaleReturnAgentHelper {

	public void looseSaleReturnForAgent(String[] gameName,
			String[] numberOfTickets, String[] ticketAmt, String[] ticketComm,
			int retOrgId, UserInfoBean userBean, String rootPath) {
		Connection connection = null;
		ResultSet rs = null;
		double creditAmt = 0.0;
		List<Integer> trnIdList = new ArrayList<Integer>();
		try {
			connection = DBConnect.getConnection();
			connection.setAutoCommit(false);
			java.sql.Timestamp currentDate = new java.sql.Timestamp(new Date()
					.getTime());
			String agtMasterQuery = QueryManager
					.insertInAgentTransactionMaster();
			PreparedStatement agtMasterStmt = connection
					.prepareStatement(agtMasterQuery);

			String LMSMasterQuery = QueryManager.insertInLMSTransactionMaster();
			PreparedStatement LMSMasterStmt = connection
					.prepareStatement(LMSMasterQuery);

			String agtAgentQuery = QueryManager
					.getST1AgentRetQueryForLooseSale();
			PreparedStatement agtAgentStmt = connection
					.prepareStatement(agtAgentQuery);
			for (int i = 0; i < gameName.length; i++) {

				if (!gameName[i].equalsIgnoreCase("-1")) {
					int nbrOfTickets = Integer.parseInt(numberOfTickets[i]);
					double ticketAmount = Double.parseDouble(ticketAmt[i]);
					double ticketCommission = Double.parseDouble(ticketComm[i]);
					int gameId = Integer.parseInt(gameName[i].split(":")[0]);
					int transactionId = 0;
					LMSMasterStmt.setString(1, "AGENT");
					LMSMasterStmt.executeUpdate();
					rs = LMSMasterStmt.getGeneratedKeys();
					if (rs.next()) {
						transactionId = rs.getInt(1);
						trnIdList.add(transactionId);
						// insert in Agent transaction master
						agtMasterStmt.setInt(1, transactionId);
						agtMasterStmt.setInt(2, userBean.getUserId());
						agtMasterStmt.setInt(3, userBean.getUserOrgId());
						agtMasterStmt.setString(4, "RETAILER");
						agtMasterStmt.setInt(5, retOrgId);
						agtMasterStmt.setString(6, "LOOSE_SALE_RET");
						agtMasterStmt.setTimestamp(7, currentDate);
						agtMasterStmt.executeUpdate();

						Statement stmt = connection.createStatement();

						double netAmt = 0.0;
						double vatAmt = 0.0;
						double govt_comm_rate = 0.0;
						double prizepayoutRatio = 0.0;
						double vat = 0.0;

						String govtCommRule = null;
						double fixed_amt = 0.0;
						long tickets_in_scheme = 0;
						double taxableSale = 0.0;
						ResultSet rsGame2 = stmt
								.executeQuery("select * from st_se_game_master where game_id="
										+ gameId);
						if (rsGame2.next()) {
							prizepayoutRatio = rsGame2
									.getDouble("prize_payout_ratio");
							vat = rsGame2.getDouble("vat_amt");
							fixed_amt = rsGame2.getDouble("fixed_amt");
							tickets_in_scheme = rsGame2
									.getLong("tickets_in_scheme");
							govtCommRule = rsGame2.getString("govt_comm_type");
							govt_comm_rate = rsGame2
									.getDouble("govt_comm_rate");
						}

						// Insert into st_agt_retailer_transaction_master
						agtAgentStmt.setInt(1, transactionId);
						agtAgentStmt.setInt(2, gameId);
						agtAgentStmt.setInt(3, userBean.getUserId());
						agtAgentStmt.setInt(4, retOrgId);
						double mrpAmt = nbrOfTickets * ticketAmount;
						agtAgentStmt.setDouble(5, mrpAmt);

						double retSaleCommRate = ticketCommission;
						double retcommAmt = mrpAmt * retSaleCommRate * 0.01;
						netAmt = mrpAmt - retcommAmt;
						creditAmt += netAmt;
						vatAmt = CommonMethods.calculateVat(mrpAmt,
								retSaleCommRate, prizepayoutRatio,
								govt_comm_rate, vat, govtCommRule, fixed_amt,
								tickets_in_scheme);

						taxableSale = CommonMethods.calTaxableSale(mrpAmt,
								retSaleCommRate, prizepayoutRatio,
								govt_comm_rate, vat);
						agtAgentStmt.setDouble(6, retSaleCommRate);
						agtAgentStmt.setDouble(7, retcommAmt);
						agtAgentStmt.setDouble(8, netAmt);
						agtAgentStmt.setString(9, "LOOSE_SALE_RET");
						agtAgentStmt.setInt(10, nbrOfTickets);
						agtAgentStmt.setDouble(11, vatAmt);
						agtAgentStmt.setDouble(12, taxableSale);
						agtAgentStmt.setInt(13, userBean.getUserOrgId());
						agtAgentStmt.executeUpdate();
					}
				}
			}
			// insert into receipt master
			PreparedStatement preState = connection
					.prepareStatement(QueryManager.insertInReceiptMaster());
			preState.setString(1, "AGENT");
			preState.executeUpdate();

			ResultSet rs12 = preState.getGeneratedKeys();
			int invoiceId = -1;
			while (rs12.next()) {
				invoiceId = rs.getInt(1);
			}

			// get auto generated receipt number
			String queryRcpt = "SELECT * from st_lms_agent_receipts where receipt_type like ('CR_NOTE%') and agent_org_id=?  ORDER BY generated_id DESC LIMIT 1";
			PreparedStatement autoGenRecptPstmt = connection
					.prepareStatement(queryRcpt);
			autoGenRecptPstmt.setInt(1, userBean.getUserOrgId());
			ResultSet recieptRs = autoGenRecptPstmt.executeQuery();
			String lastRecieptNoGenerated = null;
			while (recieptRs.next()) {
				lastRecieptNoGenerated = recieptRs.getString("generated_id");
			}

			String autoGeneRecieptNoAgt = GenerateRecieptNo.getRecieptNoAgt(
					"CR_NOTE", lastRecieptNoGenerated, "AGENT", userBean
							.getUserOrgId());

			// insert in st Agent receipts
			PreparedStatement agtReceiptStmt = connection
					.prepareStatement(QueryManager.insertInAgentReceipts());

			agtReceiptStmt.setInt(1, invoiceId);
			agtReceiptStmt.setString(2, "CR_NOTE");
			agtReceiptStmt.setInt(3, userBean.getUserOrgId());
			agtReceiptStmt.setInt(4, retOrgId);
			agtReceiptStmt.setString(5, "RETAILER");
			agtReceiptStmt.setString(6, autoGeneRecieptNoAgt);
			agtReceiptStmt.setTimestamp(7, Util.getCurrentTimeStamp());
			agtReceiptStmt.executeUpdate();

			PreparedStatement preState4 = connection
					.prepareStatement(QueryManager
							.insertAgentReceiptTrnMapping());
			// insert for receipt and transaction mapping table for Invoice
			for (int i = 0; i < trnIdList.size(); i++) {
				preState4.setInt(1, invoiceId);
				preState4.setInt(2, trnIdList.get(i));
				preState4.executeUpdate();
			}
			
			boolean isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(creditAmt, "TRANSACTION", "LOOSE_SALE_RET", retOrgId,
					userBean.getUserOrgId(), "RETAILER", 0, connection);
			
			if(!isValid)
				throw new LMSException();
			
			/*OrgCreditUpdation.updateCreditLimitForRetailer(retOrgId,
					"LOOSE_SALE_RET", creditAmt, connection);*/
			connection.commit();
			if (invoiceId > -1) {
				GraphReportHelper graphReportHelper = new GraphReportHelper();
				graphReportHelper.createTextReportAgent(invoiceId, rootPath,
						userBean.getUserOrgId(), userBean.getOrgName());
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}

}