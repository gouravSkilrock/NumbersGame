package com.skilrock.lms.coreEngine.accMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;

import com.skilrock.lms.beans.MultiBankDepositBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.GenerateRecieptNo;
import com.skilrock.lms.common.utility.OrgCreditUpdation;
import com.skilrock.lms.web.drawGames.common.Util;

public class AgentBankDepositHelper {
/**
 * @modified  transaction_id type changed to long from int
 *  By Neeraj
 * 
 */
	public String submitBankDepositAmt(int orgId, String orgType,
			double totalAmount, String receiptNumber, String bankName,
			String branchName, String depositDate,UserInfoBean userBean) throws LMSException{

		Connection conn = null;
		PreparedStatement preState = null;
		Statement st = null;
		PreparedStatement preState2 = null;
		PreparedStatement preState3 = null;
		PreparedStatement preState4 = null;
		PreparedStatement preState5 = null;
		//int transaction_id=0;
		long transaction_id=0;
		try {

			conn = DBConnect.getConnection();
			conn.setAutoCommit(false);

			String queryLMSTrans = QueryManager.insertInLMSTransactionMaster();
			String queryBOTrans = QueryManager.insertInBOTransactionMaster();
			String query1 = QueryManager.getST5BOBDTransactionQuery();
			// String query2 = QueryManager.getST5BOReceiptMappingQuery();
			// String query3 = QueryManager.getST5BOReceiptIdQuery();
			// String queryChq = QueryManager.getST5CashTransactionQuery();
			//String queryChq2 = QueryManager.getST5BOReceiptMappingQuery();

			if (totalAmount > 0) {
				// insert entry into LMS transaction master
				preState = conn.prepareStatement(queryLMSTrans);
				preState.setString(1, "BO");
				preState.executeUpdate();
				ResultSet rs = preState.getGeneratedKeys();
				if(rs.next()){
					transaction_id = rs.getLong(1);
				}else{
					throw new LMSException("transaction_id not generated... ");
				}
				

				// insert entry into bo transaction master

				preState = conn.prepareStatement(queryBOTrans);
				preState.setLong(1, transaction_id);
				preState.setInt(2, userBean.getUserId());
				preState.setInt(3, userBean.getUserOrgId());
				preState.setString(4, orgType);
				preState.setInt(5, orgId);
				preState.setTimestamp(6, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preState.setString(7, "BANK_DEPOSIT");

			
				System.out.println("query for trans  ::: " + preState);
				preState.executeUpdate();

				preState2 = conn.prepareStatement(query1);
				preState2.setLong(1, transaction_id);
				preState2.setInt(2, orgId);
				preState2.setDouble(3, totalAmount);
				preState2.setString(4, bankName);
				preState2.setString(5, branchName);
				preState2.setString(6, receiptNumber);
				preState2.setString(7, depositDate);
				System.out.println("bank deposit query..."+preState2);
				if(preState2.executeUpdate()<1)
					throw new LMSException();

			}
			// insert into receipt master
			preState4 = conn.prepareStatement(QueryManager
					.insertInReceiptMaster());
			preState4.setString(1, "BO");
			preState4.executeUpdate();

			ResultSet rs1 = preState4.getGeneratedKeys();
			int receiptId=0;
			if(rs1.next()){
				receiptId = rs1.getInt(1);
			}else{
				throw new LMSException("receipt id not genrated....");
			}
			// get auto generated receipt number

			preState5 = conn.prepareStatement(QueryManager
					.getBOLatestReceiptNb());
			preState5.setString(1, "RECEIPT");
			ResultSet recieptRs = preState5.executeQuery();
			String lastRecieptNoGenerated = null;

			while (recieptRs.next()) {
				lastRecieptNoGenerated = recieptRs.getString("generated_id");
			}

			String autoGeneRecieptNo = GenerateRecieptNo.getRecieptNo(
					"RECEIPT", lastRecieptNoGenerated, userBean.getUserType());

			// insert in st bo receipts
			preState4 = conn
					.prepareStatement(QueryManager.insertInBOReceipts());

			preState4.setInt(1, receiptId);
			preState4.setString(2, "RECEIPT");
			preState4.setInt(3, orgId);
			preState4.setString(4, "AGENT");
			preState4.setString(5, autoGeneRecieptNo);
			preState4.setTimestamp(6, Util.getCurrentTimeStamp());
			System.out.println("inserting data in bo receipt master.."+preState4);
			preState4.executeUpdate();

			preState3 = conn.prepareStatement(QueryManager
					.insertBOReceiptTrnMapping());
			preState3.setInt(1, receiptId);
			preState3.setLong(2, transaction_id);
			preState3.executeUpdate();
			
			//Only One Method use for Org Balanace Update
			boolean isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(totalAmount, "TRANSACTION", "BANK_DEPOSIT", orgId,
					0, "AGENT", 0, conn);
			
			/*OrgCreditUpdation.updateCreditLimitForAgent(orgId, "BANK_DEPOSIT",
					totalAmount, conn);*/
			if(isValid)
			conn.commit();
			else
				throw new LMSException();
			return receiptId+"Nxt"+autoGeneRecieptNo;
		} catch (SQLException se) {
			System.out
					.println(" ================= Exception in Agent Payment Submit Class.===================");
			/*addActionError("We got an exception while preparing a statement:"
					+ "Probably bad SQL.");*/
			System.out
					.println("We got an exception while preparing a statement:"
							+ "Probably bad SQL.");
			se.printStackTrace();
			throw new LMSException(se);
		} finally {
			try {
				if (st != null) {
					st.close();
				}
				if (preState3 != null) {
					preState3.close();
				}
				if (preState3 != null) {
					preState3.close();
				}
				if (preState4 != null) {
					preState4.close();
				}
				if (preState2 != null) {
					preState2.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception ee) {
				System.out.println("Error in closing the Connection");
				ee.printStackTrace();
				throw new LMSException(ee);

			}

		}
	
	}
	
	
	
	public String submitBankDepositAmt(int orgId, String orgType,
			double totalAmount, String receiptNumber, String bankName,
			String branchName, String depositDate,UserInfoBean userBean,Connection conn) throws LMSException{
		
		PreparedStatement preState = null;
		Statement st = null;
		PreparedStatement preState2 = null;
		PreparedStatement preState3 = null;
		PreparedStatement preState4 = null;
		PreparedStatement preState5 = null;
		//int transaction_id=0;
		long transaction_id=0;
		try {
			String queryLMSTrans = QueryManager.insertInLMSTransactionMaster();
			String queryBOTrans = QueryManager.insertInBOTransactionMaster();
			String query1 = QueryManager.getST5BOBDTransactionQuery();
			// String query2 = QueryManager.getST5BOReceiptMappingQuery();
			// String query3 = QueryManager.getST5BOReceiptIdQuery();
			// String queryChq = QueryManager.getST5CashTransactionQuery();
			//String queryChq2 = QueryManager.getST5BOReceiptMappingQuery();

			if (totalAmount > 0) {
				// insert entry into LMS transaction master
				preState = conn.prepareStatement(queryLMSTrans);
				preState.setString(1, "BO");
				preState.executeUpdate();
				ResultSet rs = preState.getGeneratedKeys();
				if(rs.next()){
					transaction_id = rs.getLong(1);
				}else{
					throw new LMSException("transaction_id not generated... ");
				}
				

				// insert entry into bo transaction master

				preState = conn.prepareStatement(queryBOTrans);
				preState.setLong(1, transaction_id);
				preState.setInt(2, userBean.getUserId());
				preState.setInt(3, userBean.getUserOrgId());
				preState.setString(4, orgType);
				preState.setInt(5, orgId);
				preState.setTimestamp(6, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preState.setString(7, "BANK_DEPOSIT");

			
				System.out.println("query for trans  ::: " + preState);
				preState.executeUpdate();

				preState2 = conn.prepareStatement(query1);
				preState2.setLong(1, transaction_id);
				preState2.setInt(2, orgId);
				preState2.setDouble(3, totalAmount);
				preState2.setString(4, bankName);
				preState2.setString(5, branchName);
				preState2.setString(6, receiptNumber);
				preState2.setString(7, depositDate);
				System.out.println("bank deposit query..."+preState2);
				if(preState2.executeUpdate()<1)
					throw new LMSException();

			}
			// insert into receipt master
			preState4 = conn.prepareStatement(QueryManager
					.insertInReceiptMaster());
			preState4.setString(1, "BO");
			preState4.executeUpdate();

			ResultSet rs1 = preState4.getGeneratedKeys();
			int receiptId=0;
			if(rs1.next()){
				receiptId = rs1.getInt(1);
			}else{
				throw new LMSException("receipt id not genrated....");
			}
			// get auto generated receipt number

			preState5 = conn.prepareStatement(QueryManager
					.getBOLatestReceiptNb());
			preState5.setString(1, "RECEIPT");
			ResultSet recieptRs = preState5.executeQuery();
			String lastRecieptNoGenerated = null;

			while (recieptRs.next()) {
				lastRecieptNoGenerated = recieptRs.getString("generated_id");
			}

			String autoGeneRecieptNo = GenerateRecieptNo.getRecieptNo(
					"RECEIPT", lastRecieptNoGenerated, userBean.getUserType());

			// insert in st bo receipts
			preState4 = conn
					.prepareStatement(QueryManager.insertInBOReceipts());

			preState4.setInt(1, receiptId);
			preState4.setString(2, "RECEIPT");
			preState4.setInt(3, orgId);
			preState4.setString(4, "AGENT");
			preState4.setString(5, autoGeneRecieptNo);
			preState4.setTimestamp(6, Util.getCurrentTimeStamp());
			System.out.println("inserting data in bo receipt master.."+preState4);
			preState4.executeUpdate();

			preState3 = conn.prepareStatement(QueryManager
					.insertBOReceiptTrnMapping());
			preState3.setInt(1, receiptId);
			preState3.setLong(2, transaction_id);
			preState3.executeUpdate();
			
			
			boolean isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(totalAmount, "TRANSACTION", "BANK_DEPOSIT", orgId,
					0, "AGENT", 0, conn);
			if(!isValid){
				throw new LMSException();
			}
			/*OrgCreditUpdation.updateCreditLimitForAgent(orgId, "BANK_DEPOSIT",
					totalAmount, conn);*/
			
			return receiptId+"Nxt"+autoGeneRecieptNo+"Nxt"+transaction_id;
		} catch (SQLException se) {
			System.out
					.println(" ================= Exception in Agent Payment Submit Class.===================");
			/*addActionError("We got an exception while preparing a statement:"
					+ "Probably bad SQL.");*/
			System.out
					.println("We got an exception while preparing a statement:"
							+ "Probably bad SQL.");
			se.printStackTrace();
			throw new LMSException(se);
		} finally {
			try {
				if (st != null) {
					st.close();
				}
				if (preState3 != null) {
					preState3.close();
				}
				if (preState3 != null) {
					preState3.close();
				}
				if (preState4 != null) {
					preState4.close();
				}
				if (preState2 != null) {
					preState2.close();
				}
				
			} catch (Exception ee) {
				System.out.println("Error in closing the Connection");
				ee.printStackTrace();
				throw new LMSException(ee);

			}

		}
	
	}
	
	public void submitBankDepositAmt(List<MultiBankDepositBean> beanList, UserInfoBean userBean) throws LMSException{
		
		MultiBankDepositBean bean = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;		
		long transactionId=0;
		int i =0;
		
		try {
				String queryLMSTrans = QueryManager.insertInLMSTransactionMaster();
				String queryBOTrans = QueryManager.insertInBOTransactionMaster();
				String query1 = QueryManager.getST5BOBDTransactionQuery();
				
				Iterator<MultiBankDepositBean> itr = beanList.iterator();
				
				con = DBConnect.getConnection();
				con.setAutoCommit(false);
				while(itr.hasNext()){
					i++;
					bean = itr.next();
					
					if(bean.getTotalAmt() > 0){
						
						/* Insert into LMS Transaction Master */
						pstmt = con.prepareStatement(queryLMSTrans);
						pstmt.setString(1, "BO");
						pstmt.executeUpdate();
						rs = pstmt.getGeneratedKeys();
						if(rs.next())
							transactionId = rs.getLong(1);
						
						pstmt.clearParameters();
						
						/* Insert entry into bo_txn_master */
						pstmt = con.prepareStatement(queryBOTrans);
						pstmt.setLong(1, transactionId);
						pstmt.setInt(2, userBean.getUserId());
						pstmt.setInt(3, userBean.getUserOrgId());
						pstmt.setString(4, bean.getOrgType());
						pstmt.setInt(5, bean.getAgentOrgId());
						pstmt.setTimestamp(6, new java.sql.Timestamp(new java.util.Date().getTime()));
						pstmt.setString(7, "BANK_DEPOSIT");
						System.out.println("Query For Bo Txn : " + pstmt);
						pstmt.executeUpdate();
						
						pstmt.clearParameters();
						
						/* Bank Deposit Transaction */
						pstmt = con.prepareStatement(query1);
						pstmt.setLong(1, transactionId);
						pstmt.setInt(2, bean.getAgentOrgId());
						pstmt.setDouble(3, bean.getTotalAmt());
						pstmt.setString(4, bean.getBankName());
						pstmt.setString(5, bean.getBranchName());
						pstmt.setString(6, bean.getReceiptNo());
						pstmt.setString(7, bean.getDepositDate());
						System.out.println("Bank Deposit Query : "+pstmt);
						pstmt.executeUpdate();
						
						pstmt.clearParameters();
					}
					
					/* Insert into Receipt Master */					
					pstmt = con.prepareStatement(QueryManager.insertInReceiptMaster());
					pstmt.setString(1, "BO");
					pstmt.executeUpdate();
					rs = pstmt.getGeneratedKeys();					
					int receiptId=0;
					if(rs.next())
						receiptId = rs.getInt(1);
					
					pstmt.clearParameters();
					rs.clearWarnings();
					
					/* Get Auto Generated Receipt Number */
					pstmt = con.prepareStatement(QueryManager.getBOLatestReceiptNb());
					pstmt.setString(1, "RECEIPT");
					rs = pstmt.executeQuery();
					String lastRecieptNoGenerated = null;
					while (rs.next()) {
						lastRecieptNoGenerated = rs.getString("generated_id");
					}
					
					pstmt.clearParameters();
					rs.clearWarnings();

					/* Generate Auto Receipt Number */
					String autoGeneRecieptNo = GenerateRecieptNo.getRecieptNo("RECEIPT", lastRecieptNoGenerated, userBean.getUserType());
					
					/* Insert BO Receipts */
					pstmt = con.prepareStatement(QueryManager.insertInBOReceipts());
					pstmt.setInt(1, receiptId);
					pstmt.setString(2, "RECEIPT");
					pstmt.setInt(3, bean.getAgentOrgId());
					pstmt.setString(4, "AGENT");
					pstmt.setString(5, autoGeneRecieptNo);
					pstmt.setTimestamp(6, Util.getCurrentTimeStamp());
					System.out.println("Inserting data in bo receipt master : "+pstmt);
					pstmt.executeUpdate();
					
					pstmt.clearParameters();
					
					pstmt = con.prepareStatement(QueryManager.insertBOReceiptTrnMapping());
					pstmt.setInt(1, receiptId);
					pstmt.setLong(2, transactionId);
					pstmt.executeUpdate();
					
					/* Update Organization Balance */
					boolean isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(bean.getTotalAmt(), "TRANSACTION", "BANK_DEPOSIT", bean.getAgentOrgId(), 0, "AGENT", 0, con);
					
					if(!isValid)
						throw new LMSException("Update Organization Balance Failed !!");
					
					bean.setAutoGeneratedReceiptId(receiptId+"Nxt"+autoGeneRecieptNo+"Nxt"+transactionId);
					
					if(i%5 == 0){
						con.commit();
						//con.setAutoCommit(false);
					}
				}
				con.commit();
			} catch (SQLException se) {
				System.out.println(" ================= Exception in Agent Payment Submit Class.===================");
				System.out.println("We got an exception while preparing a statement:Probably bad SQL.");
				se.printStackTrace();
				throw new LMSException(se);
			} finally {
				DBConnect.closeConnection(con, pstmt);
				DBConnect.closeRs(rs);
			}
		}

	public String submitBankDepositAmtForRetailer(int retOrgId,int agentOrgId ,int agentUserId,String orgType,
			double totalAmount, String receiptNumber, String bankName,
			String branchName, String depositDate,Connection conn) throws LMSException{
		
		PreparedStatement preState = null;
		PreparedStatement preState2 = null;
		PreparedStatement preState3 = null;
		PreparedStatement preState4 = null;
		PreparedStatement preState5 = null;
		long transaction_id=0;
		try {
				String queryLMSTrans = QueryManager.insertInLMSTransactionMaster();
				String queryAgentTrans = QueryManager.insertInAgentTransactionMaster();
				String query1 = QueryManager.getST5AgentBDTransactionQuery();
				
					if (totalAmount > 0) {
						// insert entry into LMS transaction master
							preState = conn.prepareStatement(queryLMSTrans);
							preState.setString(1, "AGENT");
							preState.executeUpdate();
							ResultSet rs = preState.getGeneratedKeys();
							if(rs.next()){
								transaction_id = rs.getLong(1);
							}else{
								throw new LMSException("transaction_id not generated... ");
							}
							// insert entry into agent transaction master
							preState = conn.prepareStatement(queryAgentTrans);
							preState.setLong(1, transaction_id);
							preState.setInt(2, agentUserId);
							preState.setInt(3, agentOrgId);
							preState.setString(4, orgType);
							preState.setInt(5, retOrgId);
							preState.setString(6, "BANK_DEPOSIT");
							preState.setTimestamp(7, new java.sql.Timestamp(
									new java.util.Date().getTime()));
			
							System.out.println("query for trans  ::: " + preState);
							preState.executeUpdate();
							
							preState2 = conn.prepareStatement(query1);
							preState2.setLong(1, transaction_id);
							preState2.setInt(2, retOrgId);
							preState2.setDouble(3, totalAmount);
							preState2.setString(4, bankName);
							preState2.setString(5, branchName);
							preState2.setString(6, receiptNumber);
							preState2.setString(7, depositDate);
							System.out.println("bank deposit query..."+preState2);
							if(preState2.executeUpdate()<1)
								throw new LMSException();
			
					}
				// insert into receipt master
				preState4 = conn.prepareStatement(QueryManager
						.insertInReceiptMaster());
				preState4.setString(1, "AGENT");
				preState4.executeUpdate();
	
				ResultSet rs1 = preState4.getGeneratedKeys();
				int receiptId=0;
				if(rs1.next()){
					receiptId = rs1.getInt(1);
				}else{
					throw new LMSException("receipt id not genrated....");
				}
				// get auto generated receipt number
				preState5 = conn.prepareStatement(QueryManager
						.getAGENTLatestReceiptNb());
				preState5.setString(1, "RECEIPT");
				preState5.setInt(2, agentOrgId);
				ResultSet recieptRs = preState5.executeQuery();
				String lastRecieptNoGenerated = null;
	
				while (recieptRs.next()) {
					lastRecieptNoGenerated = recieptRs.getString("generated_id");
				}
				String autoGeneRecieptNo = GenerateRecieptNo.getRecieptNoAgt(
						"RECEIPT", lastRecieptNoGenerated, "AGENT",agentOrgId);
	
				// insert in st agent receipts
				preState4 = conn
						.prepareStatement(QueryManager.insertInAgentReceipts());
				preState4.setInt(1, receiptId);
				preState4.setString(2, "RECEIPT");
				preState4.setInt(3, agentOrgId);
				preState4.setInt(4, retOrgId);
				preState4.setString(5, "RETAILER");
				preState4.setString(6, autoGeneRecieptNo);
				preState4.setTimestamp(7, Util.getCurrentTimeStamp());
				System.out.println("inserting data in bo receipt master.."+preState4);
				preState4.executeUpdate();
	
				preState3 = conn.prepareStatement(QueryManager
						.insertAgentReceiptTrnMapping());
				preState3.setInt(1, receiptId);
				preState3.setLong(2, transaction_id);
				preState3.executeUpdate();
				
				boolean isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(totalAmount, "TRANSACTION", "BANK_DEPOSIT", retOrgId,
						agentOrgId, "RETAILER", 0, conn);
				if(!isValid){
					throw new LMSException();
				}
				return receiptId+"Nxt"+autoGeneRecieptNo+"Nxt"+transaction_id;
			} catch (SQLException se) {
					System.out.println(" ================= Exception in Agent Payment Submit Class.===================");
					System.out.println("We got an exception while preparing a statement:"
									+ "Probably bad SQL.");
					se.printStackTrace();
			        throw new LMSException(se);
			 }finally {
				try {
						if (preState3 != null){
							preState3.close();
						}
						if (preState3 != null){
							preState3.close();
						}
						if (preState4 != null){
							preState4.close();
						}
						if (preState2 != null){
							preState2.close();
						}
			   }catch (Exception ee) {
					System.out.println("Error in closing the Connection");
					ee.printStackTrace();
					throw new LMSException(ee);
			    }

		    }
	
	}
}
