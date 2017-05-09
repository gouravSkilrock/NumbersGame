package com.skilrock.lms.coreEngine.accMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.GenerateRecieptNo;
import com.skilrock.lms.common.utility.OrgCreditUpdation;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameOfflineHelper;
import com.skilrock.lms.web.drawGames.common.Util;

public class DebitNoteAgentHelper {

	public String doDebitNoteAgtHelper(int orgId,double amount,String remarks,int agtOrgId,int agtId,String userType,Connection con) throws LMSException, SQLException{
		//Connection con = DBConnect.getConnection();
		Statement stmt = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		PreparedStatement pstmt3 = null;
		PreparedStatement pstmt4 = null;
		PreparedStatement LMSTransMaspstmt = null;
	//	int retOrgId = 0;
		String autoGenAndId="";
		try {
			stmt = con.createStatement();
			//con.setAutoCommit(false);

			/*String getAgentOrgId = "select organization_id from st_lms_organization_master where name='"
					+ retName + "'";*/
			String updateLMSTransmaster = QueryManager
					.insertInLMSTransactionMaster();
			String updateAgtMaster = QueryManager
					.insertInAgentTransactionMaster();
			// String updateAgtMaster="insert into
			// st_lms_agent_transaction_master(agent_id,retailer_org_id,transaction_type,transaction_date)
			// values(?,?,?,?)";
			String updateDebitNote = "insert into st_lms_agent_debit_note(transaction_id,retailer_org_id,amount,transaction_type,remarks,agent_user_id,agent_org_id) values(?,?,?,?,?,?,?)";
			// String updateAgtReciepts="insert into
			// st_lms_agent_receipts(receipt_type,agent_id,retailer_org_id)
			// values(?,?,?)";
			// String updateAgtRecieptmapping="insert into
			// st_lms_agent_receipts_trn_mapping(id,transaction_id)
			// values(?,?)";
			String updateAgtRecieptGenMapping = QueryManager
					.updateST5AGENTReceiptGenMappimg();
/*
			ResultSet rs = stmt.executeQuery(getAgentOrgId);
			while (rs.next()) {
				retOrgId = rs.getInt(TableConstants.SOM_ORG_ID);
			}*/
		
			if (!DrawGameOfflineHelper.fetchLoginStatus(orgId)) {
				// insert into LMS transaction master
				LMSTransMaspstmt = con.prepareStatement(updateLMSTransmaster);
				LMSTransMaspstmt.setString(1, "AGENT");
				LMSTransMaspstmt.executeUpdate();

				ResultSet rs1 = LMSTransMaspstmt.getGeneratedKeys();
				long transaction_id = 0;
				if (rs1.next()) {
					transaction_id = rs1.getLong(1);
				}

				// insert into agent transaction master

				pstmt1 = con.prepareStatement(updateAgtMaster);
				pstmt1.setLong(1, transaction_id);
				pstmt1.setInt(2, agtId);
				pstmt1.setInt(3, agtOrgId);
				pstmt1.setString(4, "RETAILER");
				pstmt1.setInt(5, orgId);
				pstmt1.setString(6, "DR_NOTE_CASH");
				pstmt1.setTimestamp(7, new java.sql.Timestamp(
						new java.util.Date().getTime()));

				/*
				 * pstmt1.setInt(1,agtId); pstmt1.setInt(2,retOrgId);
				 * pstmt1.setString(3,"DR_NOTE_CASH"); pstmt1.setTimestamp(4,
				 * new java.sql.Timestamp(new java.util.Date().getTime()));
				 */

				System.out.println(pstmt1);
				pstmt1.executeUpdate();
				System.out.println(pstmt1);

				pstmt = con.prepareStatement(updateDebitNote);
				pstmt.setLong(1, transaction_id);
				pstmt.setInt(2, orgId);
				pstmt.setDouble(3, amount);
				pstmt.setString(4, "DR_NOTE_CASH");
				pstmt.setString(5, remarks);
				pstmt.setInt(6, agtId);
				pstmt.setInt(7, agtOrgId);
				pstmt.executeUpdate();

				// get auto generated treciept number
				// String getLatestRecieptNumber="SELECT * from
				// st_lms_agent_receipts where receipt_type=? or receipt_type=?
				// ORDER BY id DESC LIMIT 1 ";
				// String getLatestRecieptNumber="SELECT * from
				// st_lms_agent_receipts_gen_mapping where (receipt_type=? or
				// receipt_type=?) and agt_org_id=? ORDER BY generated_id DESC
				// LIMIT
				// 1";
				pstmt4 = con.prepareStatement(QueryManager
						.getAgentLatestDRNoteNb());
				pstmt4.setString(1, "DR_NOTE_CASH");
				pstmt4.setString(2, "DR_NOTE");
				pstmt4.setInt(3, agtOrgId);
				ResultSet recieptRs = pstmt4.executeQuery();
				String lastRecieptNoGenerated = null;

				while (recieptRs.next()) {
					lastRecieptNoGenerated = recieptRs
							.getString("generated_id");
				}

				String autoGeneRecieptNo = GenerateRecieptNo.getRecieptNoAgt(
						"DR_NOTE_CASH", lastRecieptNoGenerated, userType, agtOrgId);

				// insert in receipt master

				pstmt2 = con.prepareStatement(QueryManager
						.insertInReceiptMaster());
				pstmt2.setString(1, "AGENT");
				pstmt2.executeUpdate();

				ResultSet rs2 = pstmt2.getGeneratedKeys();
				int id = 0;
				if (rs2.next()) {
					id = rs2.getInt(1);
				}

				// insert into reciept table

				pstmt2 = con.prepareStatement(QueryManager
						.insertInAgentReceipts());

				pstmt2.setInt(1, id);
				pstmt2.setString(2, "DR_NOTE_CASH");
				pstmt2.setInt(3, agtOrgId);
				pstmt2.setInt(4, orgId);
				pstmt2.setString(5, "RETAILER");
				pstmt2.setString(6, autoGeneRecieptNo);
				pstmt2.setTimestamp(7, Util.getCurrentTimeStamp());

				/*
				 * pstmt2.setString(1,"DR_NOTE_CASH"); pstmt2.setInt(2, agtId);
				 * pstmt2.setInt(3, retOrgId);
				 */

				pstmt2.executeUpdate();

				// insert into reciept transaction mapping
				pstmt3 = con.prepareStatement(QueryManager
						.insertAgentReceiptTrnMapping());
				pstmt3.setInt(1, id);
				pstmt3.setLong(2, transaction_id);
				pstmt3.executeUpdate();

				/*
				 * // insert into receipt gen mapping
				 * pstmt5=con.prepareStatement(updateAgtRecieptGenMapping);
				 * pstmt5.setInt(1,id); pstmt5.setString(2,autoGeneRecieptNo);
				 * pstmt5.setString(3,"DR_NOTE_CASH");
				 * pstmt5.setInt(4,agtOrgId); pstmt5.executeUpdate();
				 */

				// update organization details of retailer
				
				boolean isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(amount, "TRANSACTION", "DR_NOTE_CASH", orgId,
						agtOrgId, "RETAILER", 0, con);
				if(!isValid)
					throw new LMSException();
				
				/*OrgCreditUpdation.updateCreditLimitForRetailer(retOrgId,
						"DR_NOTE_CASH", amount, con);*/
				//con.commit();
				autoGenAndId=autoGeneRecieptNo+"#"+id+"#"+transaction_id;
			}else {
				return "LOGIN";
					//session.setAttribute("amount", "LOGIN");
				}
				
			
			} catch (SQLException e) {
				e.printStackTrace();
				throw new SQLException();
			} 
			return autoGenAndId;
	}
}

