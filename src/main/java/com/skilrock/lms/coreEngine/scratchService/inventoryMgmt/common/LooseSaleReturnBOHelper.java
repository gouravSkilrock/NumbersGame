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

public class LooseSaleReturnBOHelper {
	public void looseSaleReturnForBo(String[] gameName,
			String[] numberOfTickets, String[] ticketAmt, String[] ticketComm,
			int agtOrgId, UserInfoBean userBean, String rootPath) {
		Connection connection = null;
		ResultSet rs = null;
		double netAmtOrg = 0.0;
		List<Integer> trnIdList = new ArrayList<Integer>();

		try {
			connection = DBConnect.getConnection();
			connection.setAutoCommit(false);
			java.sql.Timestamp currentDate = new java.sql.Timestamp(new Date()
					.getTime());
			String boMasterQuery = QueryManager.insertInBOTransactionMaster();
			PreparedStatement boMasterStmt = connection
					.prepareStatement(boMasterQuery);

			String LMSMasterQuery = QueryManager.insertInLMSTransactionMaster();
			PreparedStatement LMSMasterStmt = connection
					.prepareStatement(LMSMasterQuery);

			String boAgentQuery = QueryManager.getST1BOAgentLooseSaleQuery();
			PreparedStatement boAgentStmt = connection
					.prepareStatement(boAgentQuery);
			for (int i = 0; i < gameName.length; i++) {

				if (!gameName[i].equalsIgnoreCase("-1")) {
					int nbrOfTickets = Integer.parseInt(numberOfTickets[i]);
					double ticketAmount = Double.parseDouble(ticketAmt[i]);
					double ticketCommission = Double.parseDouble(ticketComm[i]);
					int gameId = Integer.parseInt(gameName[i].split(":")[0]);
					int transactionId = 0;
					LMSMasterStmt.setString(1, "BO");
					LMSMasterStmt.executeUpdate();
					rs = LMSMasterStmt.getGeneratedKeys();
					if (rs.next()) {
						transactionId = rs.getInt(1);
						trnIdList.add(transactionId);
						// insert in BO transaction master
						boMasterStmt.setInt(1, transactionId);
						boMasterStmt.setInt(2, userBean.getUserId());
						boMasterStmt.setInt(3, userBean.getUserOrgId());
						boMasterStmt.setString(4, "AGENT");
						boMasterStmt.setInt(5, agtOrgId);
						boMasterStmt.setTimestamp(6, currentDate);
						boMasterStmt.setString(7, "LOOSE_SALE_RET");
						boMasterStmt.executeUpdate();

						Statement stmt = connection.createStatement();
						double netAmt = 0.0;
						double vatAmt = 0.0;
						double prizepayoutRatio = 0.0;
						double vat = 0.0;
						String govtCommRule = null;
						double govt_comm_rate = 0.0;
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

						// Insert into st_bo_agt_transaction_master
						boAgentStmt.setInt(1, transactionId);
						boAgentStmt.setInt(2, gameId);
						boAgentStmt.setInt(3, nbrOfTickets);
						boAgentStmt.setInt(4, agtOrgId);
						double mrpAmt = nbrOfTickets * ticketAmount;
						boAgentStmt.setDouble(5, mrpAmt);
						double govtCommAmt = mrpAmt * govt_comm_rate * .01;
						double agtSaleCommRate = ticketCommission;
						double agtcommAmt = mrpAmt * agtSaleCommRate * 0.01;
						netAmt = mrpAmt - agtcommAmt;
						netAmtOrg += netAmt;
						vatAmt = CommonMethods.calculateVat(mrpAmt,
								agtSaleCommRate, prizepayoutRatio,
								govt_comm_rate, vat, govtCommRule, fixed_amt,
								tickets_in_scheme);

						taxableSale = CommonMethods.calTaxableSale(mrpAmt,
								agtSaleCommRate, prizepayoutRatio,
								govt_comm_rate, vat);
						boAgentStmt.setDouble(6, agtSaleCommRate);
						boAgentStmt.setDouble(7, agtcommAmt);
						boAgentStmt.setDouble(8, netAmt);
						boAgentStmt.setString(9, "LOOSE_SALE_RET");

						boAgentStmt.setDouble(10, vatAmt);
						boAgentStmt.setDouble(11, taxableSale);
						boAgentStmt.setDouble(12, govtCommAmt);
						boAgentStmt.executeUpdate();
					}
				}
			}
			// insert into receipt master
			PreparedStatement preState = connection
					.prepareStatement(QueryManager.insertInReceiptMaster());
			preState.setString(1, "BO");
			preState.executeUpdate();

			ResultSet rs12 = preState.getGeneratedKeys();
			int invoiceId = -1;
			while (rs12.next()) {
				invoiceId = rs.getInt(1);
			}

			// get auto generated receipt number

			PreparedStatement preState2 = connection
					.prepareStatement("SELECT * from st_lms_bo_receipts where receipt_type like ('CR_NOTE%')  ORDER BY generated_id DESC LIMIT 1");
			// preState2.setString(1, "INVOICE");
			ResultSet recieptRs = preState2.executeQuery();
			String lastRecieptNoGenerated = null;

			while (recieptRs.next()) {
				lastRecieptNoGenerated = recieptRs.getString("generated_id");
			}

			String autoGeneRecieptNoBO = GenerateRecieptNo.getRecieptNo(
					"CR_NOTE", lastRecieptNoGenerated, "BO");

			// insert in st bo receipts
			PreparedStatement preState3 = connection
					.prepareStatement(QueryManager.insertInBOReceipts());

			preState3.setInt(1, invoiceId);
			preState3.setString(2, "CR_NOTE");
			preState3.setInt(3, agtOrgId);
			preState3.setString(4, "AGENT");
			preState3.setString(5, autoGeneRecieptNoBO);
			preState3.setTimestamp(6, Util.getCurrentTimeStamp());

			preState3.executeUpdate();

			PreparedStatement preState4 = connection
					.prepareStatement(QueryManager.insertBOReceiptTrnMapping());
			
			// insert for receipt and transaction mapping table for Invoice
			for (int i = 0; i < trnIdList.size(); i++) {
				preState4.setInt(1, invoiceId);
				preState4.setInt(2, trnIdList.get(i));
				preState4.executeUpdate();
			}
			
			boolean isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(netAmtOrg, "TRANSACTION", "LOOSE_SALE_RET", agtOrgId,
					0, "AGENT", 0, connection);
			
			if(!isValid)
				throw new LMSException();
			
			/*// for org credit updation

			OrgCreditUpdation.updateCreditLimitForAgent(agtOrgId,
					"LOOSE_SALE_RET", netAmtOrg, connection);*/

			connection.commit();

			if (invoiceId > -1) {
				GraphReportHelper graphReportHelper = new GraphReportHelper();
				graphReportHelper.createTextReportBO(invoiceId, userBean
						.getOrgName(), userBean.getUserOrgId(), rootPath);
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