package com.skilrock.lms.coreEngine.accMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.GenerateRecieptNo;
import com.skilrock.lms.common.utility.OrgCreditUpdation;
import com.skilrock.lms.web.drawGames.common.Util;

public class CreditNoteAgentHelper {

    public String doCreditNoteAgt(int retOrgId,int agtOrgId,int agtId,double amount,String remarks,String userType,Connection con) throws LMSException{
		Statement stmt = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		PreparedStatement pstmt3 = null;
		PreparedStatement pstmt4 = null;
		PreparedStatement pstmt5 = null;
		PreparedStatement LMSTransMaspstmt = null;
		ResultSet rs1=null,rs2=null;
		int id = 0;
		String autoGeneRecieptNo=null;
		try {
			stmt = con.createStatement();
			
		/*	String getAgentOrgId = "select organization_id from st_lms_organization_master where name='"
					+ orgName + "'";*/
			String updateLMSTransmaster = QueryManager
					.insertInLMSTransactionMaster();
			String updateAgtMaster = QueryManager
					.insertInAgentTransactionMaster();
			
			String updateCreditNote = "insert into st_lms_agent_credit_note(transaction_id,retailer_org_id,amount,transaction_type,remarks,agent_user_id,agent_org_id) values(?,?,?,?,?,?,?)";
			
			String updateAgtRecieptGenMapping = QueryManager
					.updateST5AGENTReceiptGenMappimg();
	
		/*	ResultSet rs = stmt.executeQuery(getAgentOrgId);
			while (rs.next()) {
				retOrgId = orgName ;//rs.getInt(TableConstants.SOM_ORG_ID);
			}*/
			// insert into LMS transaction master
			LMSTransMaspstmt = con.prepareStatement(updateLMSTransmaster);
			LMSTransMaspstmt.setString(1, "AGENT");
			LMSTransMaspstmt.executeUpdate();
	
		     rs1 = LMSTransMaspstmt.getGeneratedKeys();
			long  transaction_id = 0;
			if (rs1.next())
				transaction_id = rs1.getLong(1);
	
			// insert into agent transaction master
	
			pstmt1 = con.prepareStatement(updateAgtMaster);
			pstmt1.setLong(1, transaction_id);
			pstmt1.setInt(2, agtId);
			pstmt1.setInt(3, agtOrgId);
			pstmt1.setString(4, "RETAILER");
			pstmt1.setInt(5, retOrgId);
			pstmt1.setString(6, "CR_NOTE_CASH");
			pstmt1.setTimestamp(7, new java.sql.Timestamp(new java.util.Date()
					.getTime()));
	
			/*
			 * pstmt1.setInt(1,agtId); pstmt1.setInt(2,retOrgId);
			 * pstmt1.setString(3,"DR_NOTE_CASH"); pstmt1.setTimestamp(4, new
			 * java.sql.Timestamp(new java.util.Date().getTime()));
			 */
	
			System.out.println(pstmt1);
			pstmt1.executeUpdate();
			System.out.println(pstmt1);
	
			pstmt = con.prepareStatement(updateCreditNote);
			pstmt.setLong(1, transaction_id);
			pstmt.setInt(2, retOrgId);
			pstmt.setDouble(3, amount);
			pstmt.setString(4, "CR_NOTE_CASH");
			pstmt.setString(5, remarks);
			pstmt.setInt(6, agtId);
			pstmt.setInt(7, agtOrgId);
			pstmt.executeUpdate();
	
			// get auto generated reciept number
		
			pstmt4 = con
					.prepareStatement(QueryManager.getAgentLatestCRNoteNb());
			pstmt4.setString(1, "CR_NOTE_CASH");
			pstmt4.setString(2, "CR_NOTE");
			pstmt4.setInt(3, agtOrgId);
			ResultSet recieptRs = pstmt4.executeQuery();
			String lastRecieptNoGenerated = null;
	
			while (recieptRs.next()) {
				lastRecieptNoGenerated = recieptRs.getString("generated_id");
			}
	
		    autoGeneRecieptNo = GenerateRecieptNo.getRecieptNoAgt(
					"CR_NOTE_CASH", lastRecieptNoGenerated, userType, agtOrgId);
	
			// insert in receipt master
	
			pstmt2 = con.prepareStatement(QueryManager.insertInReceiptMaster());
			pstmt2.setString(1, "AGENT");
			pstmt2.executeUpdate();
	
			rs2 = pstmt2.getGeneratedKeys();
			
			if (rs2.next())
				id = rs2.getInt(1);
	
			// insert into reciept table
	
			pstmt2 = con.prepareStatement(QueryManager.insertInAgentReceipts());
	
			pstmt2.setInt(1, id);
			pstmt2.setString(2, "CR_NOTE_CASH");
			pstmt2.setInt(3, agtOrgId);
			pstmt2.setInt(4, retOrgId);
			pstmt2.setString(5, "RETAILER");
			pstmt2.setString(6, autoGeneRecieptNo);
			pstmt2.setTimestamp(7, Util.getCurrentTimeStamp());
	
			pstmt2.executeUpdate();
	
			// insert into reciept transaction mapping
			pstmt3 = con.prepareStatement(QueryManager
					.insertAgentReceiptTrnMapping());
			pstmt3.setInt(1, id);
			pstmt3.setLong(2, transaction_id);
			pstmt3.executeUpdate();
	
	
			// update organization details of retailer
			
			boolean isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(amount, "TRANSACTION", "CR_NOTE_CASH", retOrgId,
					agtOrgId, "RETAILER", 0, con);
			
			if(!isValid)
				throw new LMSException();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBConnect.closeConnection(pstmt, pstmt1, rs1);
			DBConnect.closeConnection(pstmt2, pstmt3, rs2);
		}
		return autoGeneRecieptNo+"#"+id;
	}
}
