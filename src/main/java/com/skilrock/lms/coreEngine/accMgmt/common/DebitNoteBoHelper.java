package com.skilrock.lms.coreEngine.accMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.db.TableConstants;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.GenerateRecieptNo;
import com.skilrock.lms.common.utility.OrgCreditUpdation;
import com.skilrock.lms.web.drawGames.common.Util;

public class DebitNoteBoHelper {
/**
 * 
 * @param agentName agent organiztion id
 * @param amount
 * @param remarks
 * @param reason
 * @param userOrgId
 * @param userId
 * @param userType
 * @param con
 * @return
 * @throws LMSException
 * @throws SQLException
 */
	public String doDebitNoteBoHelper(int  agentOrgId,double amount,String remarks,String reason,int userOrgId,int userId,String userType,Connection con) throws LMSException, SQLException{
		
		//int agentOrgId = 0;
		Statement stmt = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		PreparedStatement pstmt3 = null;
		PreparedStatement pstmt4 = null;
		String autoGenAndId="";
		try {
			//con.setAutoCommit(false);
			String gameName=null;
			stmt = con.createStatement();
			/*String getAgentOrgId = "select organization_id from st_lms_organization_master where name='"
					+ agentName + "'";*/
			// String updateBoMaster="insert into
			// st_lms_bo_transaction_master(party_type,party_id,transaction_date,transaction_type)
			// values(?,?,?,?)";

			String queryLMSTrans = QueryManager.insertInLMSTransactionMaster();
			String updateBoMaster = QueryManager.insertInBOTransactionMaster();

			String updateDebitNote = "insert into st_lms_bo_debit_note(transaction_id,agent_org_id,amount,transaction_type,remarks,reason,ref_id) values(?,?,?,?,?,?,?)";
			// String updateBoReciepts="insert into
			// st_lms_bo_receipts(receipt_type,agent_org_id) values(?,?)";
			// String updateBoRecieptmapping="insert into
			// st_lms_bo_receipts_trn_mapping(id,transaction_id) values(?,?)";
			String updateBoRecieptGenMapping = QueryManager
					.updateST5BOReceiptGenMapping();

		/*	ResultSet rs = stmt.executeQuery(getAgentOrgId);
			while (rs.next()) {
				agentOrgId = rs.getInt(TableConstants.SOM_ORG_ID);
			}*/
			//agentOrgId = Integer.parseInt(agentName);

			// inset into LMS transaction master
			pstmt1 = con.prepareStatement(queryLMSTrans);
			pstmt1.setString(1, "BO");
			pstmt1.executeUpdate();

			ResultSet rs1 = pstmt1.getGeneratedKeys();
			//int transaction_id = 0;
			long transaction_id=0;
			if (rs1.next()) {
				transaction_id = rs1.getLong(1);
			}

			pstmt1 = con.prepareStatement(updateBoMaster);

			pstmt1.setLong(1, transaction_id);
			pstmt1.setInt(2, userId);
			pstmt1.setInt(3, userOrgId);
			pstmt1.setString(4, "AGENT");
			pstmt1.setInt(5, agentOrgId);
			pstmt1.setTimestamp(6, new java.sql.Timestamp(new java.util.Date()
					.getTime()));
			pstmt1.setString(7, "DR_NOTE_CASH");

			/*
			 * pstmt1.setString(1,"AGENT"); pstmt1.setInt(2,agentOrgId);
			 * pstmt1.setTimestamp(3, new java.sql.Timestamp(new
			 * java.util.Date().getTime())); pstmt1.setString(4,"DR_NOTE_CASH");
			 */

			System.out.println(pstmt1);
			pstmt1.executeUpdate();

			System.out.println(pstmt1);

			int gameNo = 0;
			if(gameName != null && !"-1".equalsIgnoreCase(gameName)){
				gameNo = Integer.parseInt(gameName.split("-")[0]);
			}
			
			pstmt = con.prepareStatement(updateDebitNote);
			pstmt.setLong(1, transaction_id);
			pstmt.setInt(2, agentOrgId);
			pstmt.setDouble(3, amount);
			pstmt.setString(4, "DR_NOTE_CASH");
			pstmt.setString(5, remarks);
			pstmt.setString(6, reason);
			pstmt.setInt(7, gameNo);
			pstmt.executeUpdate();

			// get auto generated treciept number
			// String getLatestRecieptNumber="SELECT * from st_lms_bo_receipts
			// where receipt_type=? or receipt_type=? ORDER BY id DESC LIMIT 1
			// ";
			// String getLatestRecieptNumber= "SELECT * from
			// st_bo_receipt_gen_mapping where receipt_type=? or receipt_type=?
			// ORDER BY generated_id DESC LIMIT 1";
			pstmt4 = con.prepareStatement(QueryManager.getBOLatestDRNoteNb());
			pstmt4.setString(1, "DR_NOTE_CASH");
			pstmt4.setString(2, "DR_NOTE");
			ResultSet recieptRs = pstmt4.executeQuery();
			String lastRecieptNoGenerated = null;

			while (recieptRs.next()) {
				lastRecieptNoGenerated = recieptRs.getString("generated_id");
			}

			String autoGeneRecieptNo = GenerateRecieptNo.getRecieptNo(
					"DR_NOTE_CASH", lastRecieptNoGenerated, userType);

			// insert in receipt master table

			pstmt2 = con.prepareStatement(QueryManager.insertInReceiptMaster());
			pstmt2.setString(1, "BO");
			pstmt2.executeUpdate();

			ResultSet rs2 = pstmt2.getGeneratedKeys();
			int id = 0;
			if (rs2.next()) {
				id = rs2.getInt(1);
			}

			// insert into BO reciept table

			pstmt2 = con.prepareStatement(QueryManager.insertInBOReceipts());

			pstmt2.setInt(1, id);
			pstmt2.setString(2, "DR_NOTE_CASH");
			pstmt2.setInt(3, agentOrgId);
			pstmt2.setString(4, "AGENT");
			pstmt2.setString(5, autoGeneRecieptNo);
			pstmt2.setTimestamp(6, Util.getCurrentTimeStamp());
			/*
			 * //pstmt2.setString(1,autoGeneRecieptNo);
			 * pstmt2.setString(1,"DR_NOTE_CASH"); pstmt2.setInt(2, agentOrgId);
			 */

			pstmt2.executeUpdate();

			// insert into reciept transaction mapping
			pstmt3 = con.prepareStatement(QueryManager
					.insertBOReceiptTrnMapping());
			pstmt3.setInt(1, id);
			pstmt3.setLong(2, transaction_id);
			pstmt3.executeUpdate();

			/*
			 * //insert into recipt gen reciept mapping
			 * 
			 * pstmt5=con.prepareStatement(updateBoRecieptGenMapping);
			 * pstmt5.setInt(1,id); pstmt5.setString(2,autoGeneRecieptNo);
			 * pstmt5.setString(3,"DR_NOTE_CASH"); pstmt5.executeUpdate();
			 */

			// update organization account details
			//Only One Method use for Org Balanace Update
			boolean isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(amount, "TRANSACTION", "DR_NOTE_CASH", agentOrgId,
					0, "AGENT", 0, con);
			if(!isValid){
				throw new LMSException();
			}
			
		/*	OrgCreditUpdation.updateCreditLimitForAgent(agentOrgId,
					"DR_NOTE_CASH", amount, con);*/
			autoGenAndId=autoGeneRecieptNo+"#"+id+"#"+transaction_id;
			
		
	}catch(SQLException se){
		se.printStackTrace();
		throw new SQLException();
	}
	return autoGenAndId;
	}
}
