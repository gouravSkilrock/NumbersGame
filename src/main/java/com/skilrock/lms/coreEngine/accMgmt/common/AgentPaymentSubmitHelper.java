package com.skilrock.lms.coreEngine.accMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.GenerateRecieptNo;
import com.skilrock.lms.common.utility.OrgCreditUpdation;
import com.skilrock.lms.web.accMgmt.common.PaymentValidation;
import com.skilrock.lms.web.drawGames.common.Util;


public class AgentPaymentSubmitHelper {
	 static Log logger = LogFactory.getLog(AgentPaymentSubmitHelper.class);

	public String submitCashAgentAmt(int orgId, String orgType,
			double totalAmount,int userId,int userOrgId,String userType,String[] denoType,String[] multiples,String[] retDenoType,String[] retMuliples,Connection conn) throws LMSException{
		
		
	    String autoGeneRecieptNo=null;
	    String id=null;
		PreparedStatement preState = null;
		PreparedStatement preState6 = null;
		
		try {
			if(!PaymentValidation.isValidateCashAmount(totalAmount,orgId))
				throw new LMSException(LMSErrors.CASH_PAYMENT_INVALIDATE_DATA_ERROR_CODE,LMSErrors.CASH_PAYMENT_INVALIDATE_DATA_ERROR_MESSAGE);
			
 			long transaction_id=0;
		
			
			
			if(!PaymentValidation.isValidateAgent(orgId,userOrgId, conn))
				throw new LMSException(LMSErrors.INVALIDATE_AGENT_ERROR_CODE,LMSErrors.INVALIDATE_AGENT_ERROR_MESSAGE);
			logger.info("orgId" +orgId);
			String queryLMSTrans = QueryManager.insertInLMSTransactionMaster();
			String queryBOTrans = QueryManager.insertInBOTransactionMaster();
			String query1 = QueryManager.getST5BOTransactionQuery();

			
			if (totalAmount > 0) {
				
				preState = conn.prepareStatement(queryLMSTrans);
				preState.setString(1, "BO");
				preState.executeUpdate();
				ResultSet rs = preState.getGeneratedKeys();
				rs.next();
				transaction_id = rs.getLong(1);

				

				preState = conn.prepareStatement(queryBOTrans);
				preState.setLong(1, transaction_id);
				preState.setInt(2, userId);
				preState.setInt(3, userOrgId);
				preState.setString(4, orgType);
				preState.setInt(5, orgId);
				preState.setTimestamp(6, Util.getCurrentTimeStamp());
				preState.setString(7, "CASH");

				
				logger.info("query for trans  ::: " + preState);
				preState.executeUpdate();

				preState = conn.prepareStatement(query1);
				preState.setLong(1, transaction_id);
				preState.setInt(2, orgId);
				preState.setDouble(3, totalAmount);
				
				preState.executeUpdate();
				
				
				if(multiples != null || retMuliples != null){
					preState6 = conn
					.prepareStatement("insert into st_lms_bo_cash_denomination_details (transaction_id, cashier_id,receive_denomination, return_denomination, nbrOfNotes)values(?,?,?,?,?)");

				}
					
				if (multiples != null) {
					
					for (int i = 0; i < multiples.length; i++) {

						if (!multiples[i].equalsIgnoreCase("")) {
							String denoType1 = denoType[i].trim();
							int multiples1 = Integer.parseInt(multiples[i].trim());
							preState6.setLong(1, transaction_id);
							preState6.setInt(2, userId);
							preState6.setString(3, denoType1);
							preState6.setString(4, "0");
							preState6.setInt(5, multiples1);
							preState6.executeUpdate();

							preState = conn
									.prepareStatement("select drawer_id,nbrOfNotes from st_lms_bo_cash_drawer_status where cashier_id="
											+ userId
											+ " and denomination='"
											+ denoType1 + "'");
							ResultSet rs1 = preState.executeQuery();
							if (rs1.next()) {
								int nbrOfNotes = rs1.getInt("nbrOfNotes")
										+ multiples1;
								preState = conn
										.prepareStatement("update st_lms_bo_cash_drawer_status set nbrOfNotes=? where cashier_id=? and denomination=?");
								preState.setInt(1, nbrOfNotes);
								preState.setInt(2, userId);
								preState.setString(3, denoType1);
								preState.executeUpdate();
							}
						}
					}
				}
				if (retMuliples != null) {
					for (int i = 0; i < retMuliples.length; i++) {

						if (!retMuliples[i].equalsIgnoreCase("") && !retMuliples[i].equalsIgnoreCase("0")) {
							String retDenoType1 = retDenoType[i].trim();
							int retMuliples1 = Integer.parseInt(retMuliples[i].trim());
							preState6.setLong(1, transaction_id);
							preState6.setInt(2, userId);
							preState6.setString(3, "0");
							preState6.setString(4, retDenoType1);
							preState6.setInt(5, retMuliples1);
							preState6.executeUpdate();

							preState = conn
									.prepareStatement("select drawer_id,nbrOfNotes from st_lms_bo_cash_drawer_status where cashier_id="
											+ userId
											+ " and denomination='"
											+ retDenoType1 + "'");
							ResultSet rs1 = preState.executeQuery();
							if (rs1.next()) {
								int nbrOfNotes = rs1.getInt("nbrOfNotes")
										- retMuliples1;
								preState = conn
										.prepareStatement("update st_lms_bo_cash_drawer_status set nbrOfNotes=? where cashier_id=? and denomination=?");
								preState.setInt(1, nbrOfNotes);
								preState.setInt(2, userId);
								preState.setString(3, retDenoType1);
								preState.executeUpdate();
							}
						}
					}
				}
			}
			

			
			preState = conn.prepareStatement(QueryManager
					.insertInReceiptMaster());
			preState.setString(1, "BO");
			preState.executeUpdate();

			ResultSet rs1 = preState.getGeneratedKeys();
			rs1.next();
			id = rs1.getString(1);

			

			preState = conn.prepareStatement(QueryManager
					.getBOLatestReceiptNb());
			preState.setString(1, "RECEIPT");
			ResultSet recieptRs = preState.executeQuery();
			String lastRecieptNoGenerated = null;

			while (recieptRs.next()) {
				lastRecieptNoGenerated = recieptRs.getString("generated_id");
			}

			 autoGeneRecieptNo = GenerateRecieptNo.getRecieptNo(
					"RECEIPT", lastRecieptNoGenerated, userType);

			
			 preState = conn
					.prepareStatement(QueryManager.insertInBOReceipts());

			 preState.setString(1, id);
			 preState.setString(2, "RECEIPT");
			 preState.setInt(3, orgId);
			 preState.setString(4, "AGENT");
			 preState.setString(5, autoGeneRecieptNo);
			 preState.setTimestamp(6, Util.getCurrentTimeStamp());
			 preState.executeUpdate();

			 preState = conn.prepareStatement(QueryManager
					.insertBOReceiptTrnMapping());
			 preState.setString(1, id);
			 preState.setLong(2, transaction_id);
			
			 preState.executeUpdate();

			boolean isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(totalAmount, "TRANSACTION", "CASH_CHEQUE", orgId,
					0, "AGENT", 0, conn);
			if(!isValid)
				throw new LMSException();
			
			
	} catch (SQLException se) {
		   logger.error("Exception",se);
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		}catch (LMSException e) {
			logger.error("Exception",e);
			throw e;
		}catch (Exception e) {
			logger.error("Exception",e);
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} 
		return autoGeneRecieptNo+"#"+id;
		
}
}