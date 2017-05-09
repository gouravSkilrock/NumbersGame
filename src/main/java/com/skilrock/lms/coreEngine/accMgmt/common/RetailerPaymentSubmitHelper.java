package com.skilrock.lms.coreEngine.accMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.GenerateRecieptNo;
import com.skilrock.lms.common.utility.OrgCreditUpdation;
import com.skilrock.lms.coreEngine.reportsMgmt.common.GraphReportHelper;
import com.skilrock.lms.web.accMgmt.common.PaymentValidation;
import com.skilrock.lms.web.drawGames.common.Util;
import com.skilrock.lms.web.reportsMgmt.common.ReportUtility;

public class RetailerPaymentSubmitHelper {
	static Log logger = LogFactory.getLog(RetailerPaymentSubmitHelper.class);

	public synchronized Map createRecieptForPayment(Connection conn,
			long transId[], UserInfoBean userInfo, int partyId)
			throws SQLException {
		Map map = new TreeMap();
		int id = -1;
		// get auto generated last receipt number
		PreparedStatement pstmt = conn.prepareStatement(QueryManager
				.getAGENTLatestReceiptNb());
		pstmt.setString(1, "RECEIPT");
		pstmt.setInt(2, userInfo.getUserOrgId());
		ResultSet recieptRs = pstmt.executeQuery();
		String lastRecieptNoGenerated = null;
		if (recieptRs.next()) {
			lastRecieptNoGenerated = recieptRs.getString("generated_id");
		}

		// get the receipt Number after
		String autoGeneRecieptNo = GenerateRecieptNo.getRecieptNoAgt("RECEIPT",
				lastRecieptNoGenerated, userInfo.getUserType(), userInfo
						.getUserOrgId());

		// insert into receipt master table
		pstmt = conn.prepareStatement(QueryManager.insertInReceiptMaster());
		pstmt.setString(1, "AGENT");
		pstmt.executeUpdate();
		ResultSet rs = pstmt.getGeneratedKeys();
		if (rs.next()) {
			id = rs.getInt(1);

			// insert into agent receipt table
			String insInAgtRecQuery = QueryManager.insertInAgentReceipts();
			pstmt = conn.prepareStatement(insInAgtRecQuery);
			pstmt.setInt(1, id);
			pstmt.setString(2, "RECEIPT");
			pstmt.setInt(3, userInfo.getUserOrgId());
			pstmt.setInt(4, partyId);
			pstmt.setString(5, "RETAILER");
			pstmt.setString(6, autoGeneRecieptNo);
			pstmt.setTimestamp(7, Util.getCurrentTimeStamp());
			
			pstmt.executeUpdate();

			// insert all transaction entry with receipt id
			if (transId != null && transId.length > 0) {
				for (long element : transId) {
					String query = QueryManager.insertAgentReceiptTrnMapping();
					pstmt = conn.prepareStatement(query);
					pstmt.setInt(1, id);
					pstmt.setLong(2, element);
					pstmt.executeUpdate();
				}
			}

		}
		map.put("id", id);
		map.put("genId", autoGeneRecieptNo);
		return map;
	}

	public Map retailerChqPaySubmit(int noOfChq, UserInfoBean userInfo,
			String orgType, String orgName, String[] chequeNumber,
			String[] issuePartyName, String[] bankName, String[] chequeAmount,
			String[] chequeDate, String rootPath) throws LMSException {

		Connection conn = DBConnect.getConnection();
		double totalAmount = 0;
		try {
			int orgId = Integer.parseInt(orgName);//ReportUtility.getOrgIdFromOrgName(orgName, orgType);
			logger.debug("Retailer Org Id:" + orgId);

			conn.setAutoCommit(false);
			long transactionId[] = new long[noOfChq];
			for (int i = 0; i < noOfChq; i++) {

				// insert entry into transaction master
				String queryLMSTransMaster = QueryManager
						.insertInLMSTransactionMaster();
				PreparedStatement pstmt = conn
						.prepareStatement(queryLMSTransMaster);
				pstmt.setString(1, "AGENT");
				pstmt.executeUpdate();
				ResultSet rs = pstmt.getGeneratedKeys();
				if (rs.next()) {
					transactionId[i] = rs.getLong(1);

					// insert in agent transaction master for cheque
					String queryAgentTransMas = QueryManager
							.insertInAgentTransactionMaster();
					pstmt = conn.prepareStatement(queryAgentTransMas);
					pstmt.setLong(1, transactionId[i]);
					pstmt.setInt(2, userInfo.getUserId());
					pstmt.setInt(3, userInfo.getUserOrgId());
					pstmt.setString(4, orgType);
					pstmt.setInt(5, orgId);
					pstmt.setString(6, "CHEQUE");
					pstmt.setTimestamp(7, new java.sql.Timestamp(
							new java.util.Date().getTime()));
					pstmt.executeUpdate();

					// parse the date into SQL Date format
					DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
					// dateFormat.setCalendar(Calendar.getInstance());
					java.sql.Date sqlChequeDate = new java.sql.Date(dateFormat
							.parse(chequeDate[i]).getTime());
					logger.debug("cheque Date : " + chequeDate[i]
							+ "\t parsed cheque date : " + sqlChequeDate);

					// insert Cheque details into table
					String queryRet = QueryManager
							.getST5AGENTSaleChequeQuery1();
					pstmt = conn.prepareStatement(queryRet);
					pstmt.setLong(1, transactionId[i]);
					pstmt.setInt(2, userInfo.getUserId());
					pstmt.setInt(3, orgId);
					pstmt.setString(4, chequeNumber[i]);
					pstmt.setDate(5, sqlChequeDate);
					pstmt.setString(6, issuePartyName[i]);
					pstmt.setString(7, bankName[i]);
					pstmt.setDouble(8, Double.parseDouble(chequeAmount[i]));
					pstmt.setString(9, "CHEQUE");
					pstmt.setInt(10, userInfo.getUserOrgId());
					pstmt.executeUpdate();

					// Cheque amount added
					totalAmount = totalAmount
							+ Double.parseDouble(chequeAmount[i]);
				} else {
					throw new LMSException("Problem in DataBase");
				}
			}
			// generate the receipt for cheque entries
			Map map = createRecieptForPayment(conn, transactionId, userInfo,
					orgId);
			map.put("totalAmt", totalAmount);
			if ((Integer) map.get("id") > 0) {
				boolean isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(totalAmount, "TRANSACTION", "CASH_CHEQUE", orgId,
						userInfo.getUserOrgId(), "RETAILER", 0, conn);
				if(isValid)
					conn.commit();
				else
					throw new LMSException();
				
				/*OrgCreditUpdation.updateCreditLimitForRetailer(orgId,
						"CASH_CHEQUE", totalAmount, conn);
				conn.commit();*/
				// session.setAttribute("Receipt_Id", autoGeneRecieptNo);
				GraphReportHelper graphReportHelper = new GraphReportHelper();
				graphReportHelper.createTextReportAgent(
						(Integer) map.get("id"), rootPath, userInfo
								.getUserOrgId(), userInfo.getOrgName());

			} else {
				map.clear();
				throw new LMSException("Problem with Cheque entries.");
			}
			// session.setAttribute("totalPay", totalAmount);

			return map;
		} catch (Exception e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				logger.error("Exception: " + e);
				e.printStackTrace();
				throw new LMSException(e);
			}
		}
	}

	public String retailerCashPaySubmit(int orgId,String orgType,double totalAmount,int agentId,int userOrgID,String userType) throws LMSException{
		String autoGeneRecieptNo="";
		 String id="";
		    //double totalCashAmount=0.0;
			double totalPay;
		
		Connection conn = null;
		PreparedStatement preState = null;
		long transaction_id=0;
		try {
			if(!PaymentValidation.isValidateCashAmount(totalAmount,orgId))
				throw new LMSException(LMSErrors.RETAILER_PAYMENT_INVALIDATE_DATA_ERROR_CODE,LMSErrors.RETAILER_CASH_PAYMENT_INVALIDATE_DATA_ERROR_MESSAGE);
		
			
			
			conn = DBConnect.getConnection();
			conn.setAutoCommit(false);
			
			//check retailer of same agent or not
			if(!PaymentValidation.isValidateRetailer(userOrgID,orgId, conn))
				throw new LMSException(LMSErrors.INVALIDATE_RETAILER_ERROR_CODE,LMSErrors.INVALIDATE_RETAILER_ERROR_MESSAGE);
			
			
			String queryLMSTransMaster = QueryManager.insertInLMSTransactionMaster();
			String queryAgentTransMas = QueryManager.insertInAgentTransactionMaster();
			String query1 = QueryManager.getST5AGENTTransactionQuery();
			String query2 = QueryManager.insertAgentReceiptTrnMapping();
			String query3 = QueryManager.insertInAgentReceipts();
			

			
			
			if (totalAmount > 0) {
				String errMsg = CommonMethods.chkCreditLimitAgt(userOrgID, totalAmount,conn);
				if (!"TRUE".equals(errMsg)) {
					
					return errMsg;
				}
				// insert into LMS transaction master
				preState = conn.prepareStatement(queryLMSTransMaster);
				preState.setString(1, "AGENT");
				logger.info("LMS Transction Master:"+preState);
				preState.executeUpdate();
				
				ResultSet rs = preState.getGeneratedKeys();
				rs.next();
				transaction_id = rs.getLong(1);

				preState = conn.prepareStatement(queryAgentTransMas);
                preState.setLong(1, transaction_id);
				preState.setInt(2, agentId);
				preState.setInt(3, userOrgID);
				preState.setString(4, "RETAILER");
				preState.setInt(5, orgId);
				preState.setString(6, "CASH");
				preState.setTimestamp(7, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				logger.info("AGENT Transction Master:"+preState);
                preState.executeUpdate();

                preState = conn.prepareStatement(query1);
                preState.setLong(1, transaction_id);
                preState.setInt(2, agentId);
                preState.setInt(3, orgId);
                preState.setDouble(4, totalAmount);
                preState.setInt(5, userOrgID);
                logger.info("AGENT CASH TRANSCTION:"+preState);
                preState.executeUpdate();
               

				
			}
			

			preState = conn.prepareStatement(QueryManager
					.getAGENTLatestReceiptNb());
			preState.setString(1, "RECEIPT");
			preState.setInt(2, userOrgID);
			logger.info("AGENT LATEST RECEIPT NUMBER:"+preState);
			ResultSet recieptRs = preState.executeQuery();
			String lastRecieptNoGenerated = null;

			while (recieptRs.next()) {
				lastRecieptNoGenerated = recieptRs.getString("generated_id");
			}

			 autoGeneRecieptNo = GenerateRecieptNo.getRecieptNoAgt(
					"RECEIPT", lastRecieptNoGenerated, userType,
					userOrgID);

			// insert into receipt master table
			 preState = conn.prepareStatement(QueryManager
					.insertInReceiptMaster());
			 preState.setString(1, "AGENT");
			 logger.info("RECEIPT Master:"+preState);
			 preState.executeUpdate();

			ResultSet rs1 = preState.getGeneratedKeys();
			rs1.next();
			id = rs1.getString(1);

			// insert into agent reciept table
			preState = conn.prepareStatement(query3);

			preState.setString(1, id);
			preState.setString(2, "RECEIPT");
			preState.setInt(3, userOrgID);
			preState.setInt(4, orgId);
			preState.setString(5, "RETAILER");
			preState.setString(6, autoGeneRecieptNo);
			preState.setTimestamp(7, Util.getCurrentTimeStamp());
			logger.info("Agent Receipt Master:"+preState);
			preState.executeUpdate();

			// commented by arun
			// here transaction_id = 0 entry inserted into database
			if (totalAmount > 0) {

				preState = conn.prepareStatement(query2);
				preState.setString(1, id);
				preState.setLong(2, transaction_id);
				preState.executeUpdate();
				System.out.println("cash query ==" + preState);

			}
			
			totalPay = totalAmount;
			boolean isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(totalPay, "TRANSACTION", "CASH_CHEQUE", orgId,
					userOrgID, "RETAILER", 0, conn);
			if(isValid)
				conn.commit();
			else
				throw new LMSException();
			
		

		}catch (SQLException se) {
			logger.error("Exception",se);
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		}catch (LMSException e) {
			logger.error("Exception",e);
			throw e;
		}catch (Exception e) {
			logger.error("Exception",e);
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			try {
				DBConnect.closeCon(conn);
				}
			 catch (Exception ee) {
				 logger.error("Exception",ee);
				throw new LMSException(LMSErrors.CONNECTION_CLOSE_ERROR_CODE,LMSErrors.CONNECTION_CLOSE_ERROR_MESSAGE);
}
		}

	
		return autoGeneRecieptNo+"#"+id+"#"+transaction_id;
	}
	
	public String retailerCashPaySubmit(int orgId,String orgType,int retOrgId,double totalAmount,int agentId,int userOrgID,String userType,Connection conn) throws LMSException{
		
		
		String autoGeneRecieptNo="";
		String id="";
		// double totalCashAmount=0.0;
		double totalPay;
		long transaction_id=0;
		PreparedStatement preState = null;
		PreparedStatement LMSTransPstmt = null;
		Statement st = null;
		PreparedStatement preState2 = null;
		PreparedStatement preState3 = null;
		PreparedStatement preState4 = null;
		PreparedStatement preState5 = null;
		PreparedStatement preStateRet = null;

		PreparedStatement preStateRet2 = null;
		PreparedStatement preStateRet3 = null;
		try {
			
			// CashProcess();

			if (orgType == null ) {
			
				throw new LMSException(" Invalid Inputs");

			}
			logger.info("orgType " + orgType + "  orgName "
					+ retOrgId);
			orgId = retOrgId;
			if(orgId<=0){
				throw new LMSException(" Invalid Inputs");
				
			}
			
			logger.info("User org Id:" + orgId);

			String queryAgentTransMas = QueryManager
					.insertInAgentTransactionMaster();
			// preState = conn.prepareStatement(queryAgentTransMas);

			String queryLMSTransMaster = QueryManager
					.insertInLMSTransactionMaster();

			String query1 = QueryManager.getST5AGENTTransactionQuery();
			String query2 = QueryManager.insertAgentReceiptTrnMapping();
			String query3 = QueryManager.insertInAgentReceipts();
			// String queryRet =
			// QueryManager.getST5CashRetailerTransactionQuery();

			String queryRet1 = QueryManager.getST5AGENTSaleChequeQuery1();
			// String queryRet2 = QueryManager.getST5AGENTReceiptMappingQuery();
			if (totalAmount > 0) {
				
				if(!"GHANA".equalsIgnoreCase(Utility.getPropertyValue("COUNTRY_DEPLOYED"))){
					String errMsg = CommonMethods.chkCreditLimitAgt(userOrgID, totalAmount,conn);
					if (!"TRUE".equals(errMsg)) {
						
						return "ERROR";
					}
				}
				
				// insert into LMS transaction master
				LMSTransPstmt = conn.prepareStatement(queryLMSTransMaster);
				LMSTransPstmt.setString(1, "AGENT");
				LMSTransPstmt.executeUpdate();
				ResultSet rs = LMSTransPstmt.getGeneratedKeys();
				rs.next();
				transaction_id = rs.getInt(1);

				preState = conn.prepareStatement(queryAgentTransMas);

				java.util.Date current_date = new java.util.Date();

				preState.setLong(1, transaction_id);
				preState.setInt(2, agentId);
				preState.setInt(3, userOrgID);
				preState.setString(4, "RETAILER");
				preState.setInt(5, orgId);
				preState.setString(6, "CASH");
				preState.setTimestamp(7, new java.sql.Timestamp(
						new java.util.Date().getTime()));

				/*
				 * preState.setInt(1, agentId); preState.setInt(2, orgId);
				 * preState.setString(3, "CASH"); preState.setTimestamp(4, new
				 * java.sql.Timestamp( new java.util.Date().getTime()));
				 */

				preState.executeUpdate();

				preState2 = conn.prepareStatement(query1);
				preState2.setLong(1, transaction_id);
				preState2.setInt(2, agentId);
				preState2.setInt(3, orgId);
				preState2.setDouble(4, totalAmount);
				preState2.setInt(5, userOrgID);
				preState2.executeUpdate();

				// cheque
			}
			

			// here code being synchronized
			// get auto generated treciept number
			// String getLatestRecieptNumber = "SELECT * from
			// st_lms_agent_receipts_gen_mapping where receipt_type=? and
			// agt_org_id=? ORDER BY generated_id DESC LIMIT 1";
			preState5 = conn.prepareStatement(QueryManager
					.getAGENTLatestReceiptNb());
			preState5.setString(1, "RECEIPT");
			preState5.setInt(2, userOrgID);
			ResultSet recieptRs = preState5.executeQuery();
			String lastRecieptNoGenerated = null;

			while (recieptRs.next()) {
				lastRecieptNoGenerated = recieptRs.getString("generated_id");
			}

			 autoGeneRecieptNo = GenerateRecieptNo.getRecieptNoAgt(
					"RECEIPT", lastRecieptNoGenerated, userType,
					userOrgID);

			// insert into receipt master table
			preState4 = conn.prepareStatement(QueryManager
					.insertInReceiptMaster());
			preState4.setString(1, "AGENT");
			preState4.executeUpdate();

			ResultSet rs1 = preState4.getGeneratedKeys();
			rs1.next();
			id = rs1.getString(1);

			// insert into agent reciept table
			preState4 = conn.prepareStatement(query3);

			preState4.setString(1, id);
			preState4.setString(2, "RECEIPT");
			preState4.setInt(3, userOrgID);
			preState4.setInt(4, orgId);
			preState4.setString(5, "RETAILER");
			preState4.setString(6, autoGeneRecieptNo);
			preState4.setTimestamp(7, Util.getCurrentTimeStamp());

			/*
			 * preState4.setString(1, "RECEIPT"); preState4.setInt(2, agentId);
			 * preState4.setInt(3, orgId);
			 */
			preState4.executeUpdate();

			// commented by arun
			// here transaction_id = 0 entry inserted into database
			if (totalAmount > 0) {

				preState3 = conn.prepareStatement(query2);
				preState3.setString(1, id);
				preState3.setLong(2, transaction_id);
				preState3.executeUpdate();
				System.out.println("cash query ==" + preState3);

			}
			
			totalPay = totalAmount;
			
			boolean isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(totalPay, "TRANSACTION", "CASH_CHEQUE", orgId,
					userOrgID, "RETAILER", 0, conn);
			if(!isValid)
				throw new LMSException();
			/*OrgCreditUpdation.updateCreditLimitForRetailer(orgId,
					"CASH_CHEQUE", totalPay, conn);*/
	
		} catch (SQLException se) {

			
			System.out
					.println("We got an exception while preparing a statement:"
							+ "Probably bad SQL.");
			se.printStackTrace();
			throw new LMSException(se);
		} catch (LMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new LMSException(e);
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

	
		return autoGeneRecieptNo+"#"+id+"#"+transaction_id;
	}
	
	
}
