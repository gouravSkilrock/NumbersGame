package com.skilrock.lms.coreEngine.userMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.GenerateRecieptNo;
import com.skilrock.lms.common.utility.OrgCreditUpdation;
import com.skilrock.lms.web.drawGames.common.Util;

public class updateLedgerHelper {
	private static Log logger = LogFactory.getLog(updateLedgerHelper.class);

	public static void main(String[] args) throws ParseException {
		/*String transDate = "2011-04-12 23:30:00";
		String curDate = (new SimpleDateFormat("yyyy-MM-dd"))
				.format((new Timestamp((new Date()).getTime())));
		Timestamp startDate = new Timestamp((new SimpleDateFormat(
				"yyyy-MM-dd hh:mm:ss")).parse(transDate).getTime());
		logger.debug(curDate);
		logger.debug(startDate);*/
		// logger.debug(fetchTransTimeStamp(transDate));
		updateLedgerHelper helper = new updateLedgerHelper();
		Connection con = DBConnect.getConnection();
		Statement tempStmt=null;
		try {
			helper.fillTempDGTransCommTlbForRet(con);
			tempStmt=con.createStatement();
			tempStmt.execute("CREATE TEMPORARY TABLE orgTransSummary (agent_ref_transaction_id BIGINT NOT NULL, ret_comm DECIMAL(20,2) NOT NULL DEFAULT 0.00, agt_comm DECIMAL(20,2) NOT NULL DEFAULT 0.00, govt_comm DECIMAL(20,2) NOT NULL DEFAULT 0.00, retailer_org_id INT UNSIGNED NOT NULL DEFAULT 0, game_id INT UNSIGNED NOT NULL DEFAULT 0,trans_date DATETIME)");
			helper.updateDGSaleLedgerForRet(con);
		} catch (LMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public static void updateLedger() throws LMSException {
		Connection con = DBConnect.getConnection();

		try {
			logger.debug(new Date()+"---Update Ledger Start At--"+new Date().getTime());
			con.setAutoCommit(false);
			Statement tempStmt=null;
			boolean isDraw = "YES".equalsIgnoreCase(LMSFilterDispatcher.getIsDraw());
			if (isDraw) {
				updateLedgerHelper helper = new updateLedgerHelper();
				logger.debug(new Date()+"---Update Ledger Of Draw Game Start At--"+new Date().getTime());
				tempStmt=con.createStatement();
				tempStmt.execute("CREATE TEMPORARY TABLE orgTransSummary (agent_ref_transaction_id BIGINT NOT NULL, ret_comm DECIMAL(20,2) NOT NULL DEFAULT 0.00, agt_comm DECIMAL(20,2) NOT NULL DEFAULT 0.00, govt_comm DECIMAL(20,2) NOT NULL DEFAULT 0.00, retailer_org_id INT UNSIGNED NOT NULL DEFAULT 0, game_id INT UNSIGNED NOT NULL DEFAULT 0,trans_date DATETIME)");

				helper.fillTempDGTransCommTlbForRet(con);
				helper.updateDGSaleLedgerForRet(con);
				//helper.updateDGSaleLedgerForRetwithoutComm(con);
				helper.updateDGPwtLedgerForRet(con);
				logger.debug(new Date()+"---Draw Game Sale Pwt Complete for Retailer Start At--"+new Date().getTime());
				tempStmt.execute("drop table orgTransSummary");
				
				tempStmt.execute("CREATE TEMPORARY TABLE agentTransSummary (bo_ref_transaction_id BIGINT NOT NULL, agt_comm DECIMAL(20,2) NOT NULL DEFAULT 0.00, govt_comm DECIMAL(20,2) NOT NULL DEFAULT 0.00, agent_org_id INT UNSIGNED NOT NULL DEFAULT 0, game_id INT UNSIGNED NOT NULL DEFAULT 0,trans_date DATETIME)");

				helper.fillTempDGTransCommTlbForAgt(con);
				helper.updateDGSaleLedgerForAgt(con);
				//helper.updateDGSaleLedgerForAgtwithoutComm(con);
				helper.updateDGPwtLedgerForAgt(con);
				
				tempStmt.execute("drop table agentTransSummary");
				logger.debug(new Date()+"---Draw Game Sale Pwt Complete for Agent Start At--"+new Date().getTime());
				logger.debug(new Date()+"---Update Ledger Of Draw Game End At--"+new Date().getTime());
			} else {
				logger.debug("Draw Game Module not implemented!!");
			}

			boolean isCS = "YES"
					.equalsIgnoreCase(LMSFilterDispatcher.getIsCS());
			if (isCS) {
				updateLedgerHelper helper = new updateLedgerHelper();
				logger.debug(new Date()+"---Update Ledger Of CS Start At--"+new Date().getTime());
				helper.updateCSSaleLedgerForRet(con);
				helper.updateCSSaleLedgerForAgt(con);
				logger.debug(new Date()+"---Update Ledger Of CS End At--"+new Date().getTime());
			}else {
				logger.debug("CS Module not implemented!!");
			}
			boolean isOLA = "YES".equalsIgnoreCase(LMSFilterDispatcher.getIsOLA());
			if(isOLA)
			{
				updateLedgerHelper helper = new updateLedgerHelper();
				//for retailer
				helper.updateOlaDepositLedgerForRet(con);
				helper.updateOlaWithdrawlLedgerForRet(con);
				//for Agent
				helper.updateOlaDepositLedgerForAgt(con);
				helper.updateOlaWithdrawlLedgerForAgt(con);
				
				//For commission agent and retailer in OLA
				helper.updateOLACommissionLedgerForRet(con);
				helper.updateOLACommissionLedgerForAgt(con);
				//for Agent for Player Commission
			//	helper.updateOlaPlayerCommissionLedgerForAgt(con);
				
			}
			else
			{
				logger.debug("OLA module not implemented");
			}
			// con.setAutoCommit(false);
			boolean isScratch = "YES".equalsIgnoreCase(LMSFilterDispatcher
					.getIsScratch());
			if (isScratch) {
				logger.debug(new Date()+"---Update Ledger Of Scratch Start At--"+new Date().getTime());
				CommonFunctionsHelper commHelper = new CommonFunctionsHelper();
				List<UserInfoBean> orgidList = commHelper
						.getOrgIdNClmAmtList(con);
				for (UserInfoBean userBean : orgidList) {
					String receiptId = null;
					logger.debug("Instant game pwt started");
					Map virnPwtMap = commHelper.fetchPwtDetailsOfOrg(userBean
							.getUserOrgId(), userBean.getUserType(),
							"CLAIM_BAL", con);
					if (virnPwtMap != null && !virnPwtMap.isEmpty()) {						
						if ("RETAILER".equalsIgnoreCase(userBean.getUserType())) {
							
							logger.debug("CLAIMABLE for RETAILER");
							receiptId = commHelper.updateClmableBalOfRetOrg(
									userBean.getUserId(), userBean
											.getUserOrgId(), userBean.getParentUserId(), userBean
											.getParentOrgId(), virnPwtMap, con);
						} else if ("AGENT".equalsIgnoreCase(userBean
								.getUserType())) { // claimable
							// for
							// AGENT
							logger.debug("CLAIMABLE for AGENT");
							receiptId = commHelper.updateClmableBalOfAgtOrg(
									userBean.getUserId(), userBean
											.getUserOrgId(), userBean.getParentUserId(), userBean
											.getParentOrgId(), virnPwtMap, con);
						}
						logger.debug("receipt id is " + receiptId);
					} else {
						receiptId = "AlreadyUpdated";
					}

				}
				logger.debug(new Date()+"---Update Ledger Of Scratch End At--"+new Date().getTime());
			} else {
				logger.debug("Scratch Game Module not implemented !!");
			}
			boolean isSLE = "YES".equalsIgnoreCase(LMSFilterDispatcher.getIsSLE());
			if (isSLE) {
				SLEUpdateLedgerHelper.runLedgerForSLE(con);
				//SLEUpdateLedgerHelper.runTemp(con);
			} else {
				logger.debug("Draw Game Module not implemented!!");
			}
			
			
			
			con.commit();
			//new LedgerHelperQuartz().fillLedger();
		} catch (SQLException e) {
			logger.info("SQL Exception ",e);
			throw new LMSException("SQL Exception "+e.getMessage());
		}catch (Exception e) {
			logger.info("Exception ",e);
			throw new LMSException("Exception"+e.getMessage());
		} finally {
			DBConnect.closeCon(con);
		}
	}

	public void updateCSSaleLedgerForRet(Connection con) throws LMSException {
		logger.debug(new Date() + "---start-cs sale update for retailer--"
				+ new Date().getTime());
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		double totalMrpAmt, totalRetComm, totalAgtComm, totalJVAmt, totalretNetAmt, totalAgtNetAmt, vatAmt, govtCommAmt, totalCancelCharge;
		double retCommRate, agtCommRate, JVCommRate, vat, govtComm, retDebitAmount;
		String transDate;
		int retOrgId, agtUserId, agtOrgId=0;
		Map<Integer, Double> orgIdAmountMap = new HashMap<Integer, Double>();
		Map<Integer, Integer> orgIdParentIdMap = new HashMap<Integer, Integer>();
		Map<Integer, Map<String, List<Long>>> orgIdTransIdInvMap = new HashMap<Integer, Map<String, List<Long>>>();
		Map<String, List<Long>> transIdInvMap = null;
		Map<Integer, Map<String, List<Long>>> orgIdTransIdInvRetMap= new HashMap<Integer, Map<String, List<Long>>>();
		Map<String, List<Long>> transIdInvRetMap= null;
		try {
			String getGameNbrFromGameMaster = "select category_id from st_cs_product_category_master where status='ACTIVE'";
			String saleQry = "select a.retailer_org_id,agtUserId,agtOrgId,sum(mrp_amt) totalMrp ,(retailer_comm) totalRetComm,(agent_comm) totalagtComm, (jv_comm) totalJVComm,a.product_id,vat,vat_amt,govt_comm,govt_comm_amt,DATE(transaction_date) 'transaction_date',transaction_type from st_cs_sale_? a inner join  st_lms_retailer_transaction_master b inner join (select um.user_id retUserId,um.organization_id retOrgId,um.parent_user_id agtUserId,om.parent_id agtOrgId from st_lms_organization_master om inner join st_lms_user_master um on om.organization_id=um.organization_id where om.organization_type='RETAILER') c on a.transaction_id = b.transaction_id and a.retailer_org_id=retOrgId  where claim_status='CLAIM_BAL' group by a.retailer_org_id,DATE(transaction_date),a.product_id,retailer_comm,agent_comm,jv_comm  order by retailer_org_id,game_id,transaction_date";
			String saleReturnQry = "select a.retailer_org_id,agtUserId,agtOrgId,sum(mrp_amt) totalMrp ,(retailer_comm) totalRetComm,(agent_comm) totalagtComm, (jv_comm) totalJVComm,sum(cancellation_charges) totalCancelCharge,a.product_id,vat,vat_amt,govt_comm,govt_comm_amt,DATE(transaction_date) 'transaction_date',transaction_type from st_cs_refund_? a inner join  st_lms_retailer_transaction_master b inner join (select um.user_id retUserId,um.organization_id retOrgId,um.parent_user_id agtUserId,om.parent_id agtOrgId from st_lms_organization_master om inner join st_lms_user_master um on om.organization_id=um.organization_id where om.organization_type='RETAILER') c on a.transaction_id = b.transaction_id and a.retailer_org_id=retOrgId  where claim_status='CLAIM_BAL' group by a.retailer_org_id,DATE(transaction_date),a.product_id,transaction_type,retailer_comm,agent_comm,jv_comm  order by retailer_org_id,game_id,transaction_date";
			PreparedStatement pstmtGameNbr = con
					.prepareStatement(getGameNbrFromGameMaster);
			ResultSet resultGameNbr = pstmtGameNbr.executeQuery();

			PreparedStatement pstmtAgt = con.prepareStatement(QueryManager
					.insertInLMSTransactionMaster());
			PreparedStatement pstmtAgtTrans = con.prepareStatement(QueryManager
					.insertInAgentTransactionMaster());
			PreparedStatement insAgtSalePstmt = con
					.prepareStatement("insert into st_cs_agt_sale(transaction_id,agent_org_id,retailer_org_id,product_id,mrp_amt,retailer_comm,net_amt,agent_comm,bo_net_amt,claim_status,jv_comm,jv_comm_amt, vat, vat_amt, govt_comm, govt_comm_amt) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			PreparedStatement updateRetSalePstmt = con
					.prepareStatement("update st_cs_sale_? a inner join  st_lms_retailer_transaction_master b on a.transaction_id = b.transaction_id set claim_status='DONE_CLAIM',agent_ref_transaction_id=? where a.retailer_org_id=? and a.product_id =? and retailer_comm=? and agent_comm=? and jv_comm=? and DATE(transaction_date)=? and transaction_type=?");
			PreparedStatement insAgtSaleRetPstmt = con
					.prepareStatement("insert into st_cs_agt_refund(transaction_id,agent_org_id,retailer_org_id,product_id,mrp_amt,retailer_comm,net_amt,agent_comm,bo_net_amt,claim_status,jv_comm,jv_comm_amt,vat, vat_amt, govt_comm, govt_comm_amt,cancellation_charges) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			PreparedStatement updateRetSaleRetPstmt = con
					.prepareStatement("update st_cs_refund_? a inner join  st_lms_retailer_transaction_master b on a.transaction_id = b.transaction_id set claim_status='DONE_CLAIM',agent_ref_transaction_id=? where a.retailer_org_id=? and a.product_id =? and retailer_comm=? and agent_comm=? and jv_comm=? and DATE(transaction_date)=? and transaction_type=?");

			int productId = 0;
			int categoryId = 0;
			while (resultGameNbr.next()) {
				categoryId = resultGameNbr.getInt("category_id");
				pstmt = con.prepareStatement(saleQry);
				pstmt.setInt(1, categoryId);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					productId = rs.getInt("product_id");
					retOrgId = rs.getInt("retailer_org_id");
					agtUserId = rs.getInt("agtUserId");
					agtOrgId = rs.getInt("agtOrgId");

					totalMrpAmt = rs.getDouble("totalMrp");
					totalRetComm = rs.getDouble("totalRetComm") * totalMrpAmt
							* 0.01;
					totalAgtComm = rs.getDouble("totalagtComm") * totalMrpAmt
							* 0.01;
					totalJVAmt = rs.getDouble("totalJVComm") * totalMrpAmt
							* 0.01;
					retCommRate = rs.getDouble("totalRetComm");
					agtCommRate = rs.getDouble("totalagtComm");
					JVCommRate = rs.getDouble("totalJVComm");
					vat = rs.getDouble("vat");
					vatAmt = rs.getDouble("vat_amt");
					govtComm = rs.getDouble("govt_comm");
					govtCommAmt = rs.getDouble("govt_comm_amt");
					transDate = rs.getString("transaction_date");
					String tranType = rs.getString("transaction_type");
					totalretNetAmt = totalMrpAmt - totalRetComm;
					totalAgtNetAmt = totalMrpAmt - totalAgtComm;

					if (orgIdAmountMap.containsKey(retOrgId)) {
						retDebitAmount = orgIdAmountMap.get(retOrgId)
								+ totalretNetAmt;
					} else {
						retDebitAmount = totalretNetAmt;
					}

					orgIdAmountMap.put(retOrgId, retDebitAmount);
					orgIdParentIdMap.put(retOrgId, agtOrgId);

					if (orgIdTransIdInvMap.containsKey(retOrgId)) {
						transIdInvMap = orgIdTransIdInvMap.get(retOrgId);
					} else {
						transIdInvMap = new HashMap<String, List<Long>>();
						orgIdTransIdInvMap.put(retOrgId, transIdInvMap);
					}

					// insert in main transaction table

					pstmtAgt.setString(1, "AGENT");
					pstmtAgt.executeUpdate();
					ResultSet rsTrns = pstmtAgt.getGeneratedKeys();
					if (rsTrns.next()) {
						long transId = rsTrns.getLong(1);
						logger.debug(productId+"--"+retOrgId+"-Retailer Sale ---"+transId+"---"+transDate);
						if (!transIdInvMap.containsKey(transDate)) {
							transIdInvMap.put(transDate,
									new ArrayList<Long>());
						}

						transIdInvMap.get(transDate).add(transId);

						pstmtAgtTrans.setLong(1, transId);
						pstmtAgtTrans.setInt(2, agtUserId);
						pstmtAgtTrans.setInt(3, agtOrgId);
						pstmtAgtTrans.setString(4, "RETAILER");
						pstmtAgtTrans.setInt(5, retOrgId);
						pstmtAgtTrans.setString(6, "CS_SALE");
						pstmtAgtTrans.setTimestamp(7,fetchTransTimeStamp(transDate));
						pstmtAgtTrans.executeUpdate();

						insAgtSalePstmt.setLong(1, transId);
						insAgtSalePstmt.setInt(2, agtOrgId);
						insAgtSalePstmt.setInt(3, retOrgId);
						insAgtSalePstmt.setInt(4, productId);
						insAgtSalePstmt.setDouble(5, totalMrpAmt);
						insAgtSalePstmt.setDouble(6, retCommRate);
						insAgtSalePstmt.setDouble(7, totalretNetAmt);
						insAgtSalePstmt.setDouble(8, agtCommRate);
						insAgtSalePstmt.setDouble(9, totalAgtNetAmt);
						insAgtSalePstmt.setString(10, "CLAIM_BAL");
						insAgtSalePstmt.setDouble(11, JVCommRate);
						insAgtSalePstmt.setDouble(12, totalJVAmt);
						insAgtSalePstmt.setDouble(13, vat);
						insAgtSalePstmt.setDouble(14, vatAmt);
						insAgtSalePstmt.setDouble(15, govtComm);
						insAgtSalePstmt.setDouble(16, govtCommAmt);
						insAgtSalePstmt.executeUpdate();

						// update retailer table

						updateRetSalePstmt.setInt(1, categoryId);
						updateRetSalePstmt.setLong(2, transId); // agent trans
						// Id
						updateRetSalePstmt.setInt(3, retOrgId);
						updateRetSalePstmt.setInt(4, productId);
						updateRetSalePstmt.setDouble(5, retCommRate);
						updateRetSalePstmt.setDouble(6, agtCommRate);
						updateRetSalePstmt.setDouble(7, JVCommRate);
						updateRetSalePstmt.setString(8, transDate);
						updateRetSalePstmt.setString(9, tranType);
						logger.debug("sale status update qry for retailer: "
								+ updateRetSalePstmt);
						updateRetSalePstmt.executeUpdate();
					}

				}

				pstmt = con.prepareStatement(saleReturnQry);
				pstmt.setInt(1, categoryId);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					productId = rs.getInt("product_id");
					retOrgId = rs.getInt("retailer_org_id");
					agtUserId = rs.getInt("agtUserId");
					agtOrgId = rs.getInt("agtOrgId");

					totalMrpAmt = rs.getDouble("totalMrp");
					totalRetComm = rs.getDouble("totalRetComm") * totalMrpAmt
							* 0.01;
					totalAgtComm = rs.getDouble("totalagtComm") * totalMrpAmt
							* 0.01;
					totalJVAmt = rs.getDouble("totalJVComm") * totalMrpAmt
							* 0.01;
					totalCancelCharge = rs.getDouble("totalCancelCharge");
					retCommRate = rs.getDouble("totalRetComm");
					agtCommRate = rs.getDouble("totalagtComm");
					JVCommRate = rs.getDouble("totalJVComm");
					vat = rs.getDouble("vat");
					vatAmt = rs.getDouble("vat_amt");
					govtComm = rs.getDouble("govt_comm");
					govtCommAmt = rs.getDouble("govt_comm_amt");
					transDate = rs.getString("transaction_date");
					totalretNetAmt = totalMrpAmt - totalRetComm
							- totalCancelCharge;
					totalAgtNetAmt = totalMrpAmt - totalAgtComm
							- totalCancelCharge;
					String tranType = rs.getString("transaction_type");

					if (orgIdAmountMap.containsKey(retOrgId)) {
						retDebitAmount = orgIdAmountMap.get(retOrgId)
								- totalretNetAmt;
					} else {
						retDebitAmount = totalretNetAmt;
					}

					orgIdAmountMap.put(retOrgId, retDebitAmount);
					orgIdParentIdMap.put(retOrgId, agtOrgId);

					if (orgIdTransIdInvRetMap.containsKey(retOrgId)) {
						transIdInvRetMap = orgIdTransIdInvRetMap.get(retOrgId);
					} else {
						transIdInvRetMap = new HashMap<String, List<Long>>();
						orgIdTransIdInvRetMap.put(retOrgId, transIdInvRetMap);
					}

					// insert in main transaction table

					pstmtAgt.setString(1, "AGENT");
					pstmtAgt.executeUpdate();
					ResultSet rsTrns = pstmtAgt.getGeneratedKeys();
					if (rsTrns.next()) {
						long transId = rsTrns.getLong(1);
						logger.debug(productId+"--"+retOrgId+"-Retailer cancel ---"+transId+"---"+transDate);
						if (!transIdInvRetMap.containsKey(transDate)) {
							transIdInvRetMap.put(transDate,
									new ArrayList<Long>());
						}

						transIdInvRetMap.get(transDate).add(transId);

						pstmtAgtTrans.setLong(1, transId);
						pstmtAgtTrans.setInt(2, agtUserId);
						pstmtAgtTrans.setInt(3, agtOrgId);
						pstmtAgtTrans.setString(4, "RETAILER");
						pstmtAgtTrans.setInt(5, retOrgId);
						pstmtAgtTrans.setString(6, tranType);
						pstmtAgtTrans.setTimestamp(7,
								fetchTransTimeStamp(transDate));
						pstmtAgtTrans.executeUpdate();

						insAgtSaleRetPstmt.setLong(1, transId);
						insAgtSaleRetPstmt.setInt(2, agtOrgId);
						insAgtSaleRetPstmt.setInt(3, retOrgId);
						insAgtSaleRetPstmt.setInt(4, productId);
						insAgtSaleRetPstmt.setDouble(5, totalMrpAmt);
						insAgtSaleRetPstmt.setDouble(6, retCommRate);
						insAgtSaleRetPstmt.setDouble(7, totalretNetAmt); // net
						// amt
						insAgtSaleRetPstmt.setDouble(8, agtCommRate);
						insAgtSaleRetPstmt.setDouble(9, totalAgtNetAmt); // bo
						// net
						// amt
						insAgtSaleRetPstmt.setString(10, "CLAIM_BAL");
						insAgtSaleRetPstmt.setDouble(11, JVCommRate);
						insAgtSaleRetPstmt.setDouble(12, totalJVAmt);
						insAgtSaleRetPstmt.setDouble(13, vat);
						insAgtSaleRetPstmt.setDouble(14, vatAmt);
						insAgtSaleRetPstmt.setDouble(15, govtComm);
						insAgtSaleRetPstmt.setDouble(16, govtCommAmt);
						insAgtSaleRetPstmt.setDouble(17, totalCancelCharge);
						insAgtSaleRetPstmt.executeUpdate();

						// update retailer table
						updateRetSaleRetPstmt.setInt(1, categoryId);
						updateRetSaleRetPstmt.setLong(2, transId); // agent
						// trans Id
						updateRetSaleRetPstmt.setInt(3, retOrgId);
						updateRetSaleRetPstmt.setInt(4, productId);
						updateRetSaleRetPstmt.setDouble(5, retCommRate);
						updateRetSaleRetPstmt.setDouble(6, agtCommRate);
						updateRetSaleRetPstmt.setDouble(7, JVCommRate);
						updateRetSaleRetPstmt.setString(8, transDate);
						updateRetSaleRetPstmt.setString(9, tranType);
						logger
								.debug("sale Ref status update qry for retailer: "
										+ updateRetSaleRetPstmt);
						updateRetSaleRetPstmt.executeUpdate();
					}

				}
			}

			generateDGReceiptNew(orgIdTransIdInvMap, orgIdParentIdMap,
					"CS_INVOICE", "CS_RECEIPT", con);
			generateDGReceiptNew(orgIdTransIdInvRetMap, orgIdParentIdMap,
					"CR_NOTE", "CR_NOTE_CASH", con);

			logger.debug("retailer orgIdAmountMap\n" + orgIdAmountMap);
			// update retailer balance
			Set<Integer> retOrgIdSet = orgIdAmountMap.keySet();
			for (Integer orgId : retOrgIdSet) {
				
				boolean isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(orgIdAmountMap.get(orgId), "TRANSACTION", "CS_SALE", orgId,
						orgIdParentIdMap.get(orgId), "RETAILER", 0, con);
				if(!isValid)
					throw new LMSException();
					
				OrgCreditUpdation.updateOrganizationBalWithValidate(orgIdAmountMap.get(orgId), "CLAIM_BAL", "DEBIT", orgId,
						orgIdParentIdMap.get(orgId), "RETAILER", 0, con);
				
				
				/*OrgCreditUpdation.updateCreditLimitForRetailer(orgId,
						"CS_SALE", orgIdAmountMap.get(orgId), con);
				updateOrgBalance("CLAIM_BAL", orgIdAmountMap.get(orgId), orgId,
						"DEBIT", con);*/
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("error in sale retailer");
		}
		logger.debug(new Date() + "---end-cs sale update for retailer--"
				+ new Date().getTime());
	}

	public void updateCSSaleLedgerForAgt(Connection con) throws Exception {

		logger.debug(new Date() + "---start-cs sale update for agent--"
				+ new Date().getTime());
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		double totalMrpAmt, totalAgtComm, totalJVComm, totalAgtNetAmt, vatAmt, govtCommAmt, totalCancelCharge;
		double agtCommRate, JVCommRate, agtDebitAmount, vat, govtComm;
		String transDate;
		int agtOrgId, boUserId, boOrgId;
		Map<Integer, Double> orgIdAmountMap = new HashMap<Integer, Double>();
		Map<Integer, Integer> orgIdParentIdMap = new HashMap<Integer, Integer>();
		Map<Integer, Map<String, List<Long>>> orgIdTransIdInvMap = new HashMap<Integer, Map<String, List<Long>>>();
		Map<String, List<Long>> transIdInvMap = null;
		Map<Integer, Map<String, List<Long>>> orgIdTransIdInvRetMap = new HashMap<Integer, Map<String, List<Long>>>();
		Map<String, List<Long>> transIdInvRetMap = null;
		try {
			String saleQry = "select agent_org_id,boUserId,boOrgId,totalMrp,totalRetComm,totalagtComm,totalgovtComm,totalVat,totalJVComm,vat, govt_comm,product_id,transaction_date,transaction_type from(select a.agent_org_id,sum(mrp_amt) totalMrp ,retailer_comm totalRetComm,agent_comm totalagtComm, govt_comm_amt totalgovtComm, vat_amt totalVat, jv_comm totalJVComm, vat, govt_comm, a.product_id,transaction_type,DATE(transaction_date) 'transaction_date' from st_cs_agt_sale a inner join  st_lms_agent_transaction_master b on a.transaction_id = b.transaction_id where claim_status='CLAIM_BAL' group by a.agent_org_id,DATE(transaction_date),a.product_id,agent_comm,jv_comm  order by agent_org_id,product_id,transaction_date) sale inner join (select um.user_id agtUserId,um.organization_id agtOrgId,um.parent_user_id boUserId,om.parent_id boOrgId,isrolehead from st_lms_organization_master om inner join st_lms_user_master um on om.organization_id=um.organization_id where om.organization_type='AGENT') om on agent_org_id=agtOrgId and isrolehead = 'Y'";
			String saleReturnQry = "select agent_org_id,boUserId,boOrgId,totalMrp,totalRetComm,totalagtComm,totalgovtComm,totalVat,totalJVComm,vat, govt_comm,product_id,transaction_date,transaction_type,totalCancelCharge from(select a.agent_org_id,sum(mrp_amt) totalMrp ,retailer_comm totalRetComm,agent_comm totalagtComm, govt_comm_amt totalgovtComm, vat_amt totalVat, jv_comm totalJVComm, vat, govt_comm, a.product_id,transaction_type,DATE(transaction_date) 'transaction_date',cancellation_charges totalCancelCharge from st_cs_agt_refund a inner join  st_lms_agent_transaction_master b on a.transaction_id = b.transaction_id where claim_status='CLAIM_BAL' group by a.agent_org_id,DATE(transaction_date),a.product_id,agent_comm  order by agent_org_id,product_id,transaction_date) sale inner join (select um.user_id agtUserId,um.organization_id agtOrgId,um.parent_user_id boUserId,om.parent_id boOrgId,isrolehead from st_lms_organization_master om inner join st_lms_user_master um on om.organization_id=um.organization_id where om.organization_type='AGENT') om on agent_org_id=agtOrgId and isrolehead = 'Y'";

			PreparedStatement pstmtBO = con.prepareStatement(QueryManager
					.insertInLMSTransactionMaster());
			PreparedStatement insBoTranPstmt = con
					.prepareStatement(QueryManager
							.insertInBOTransactionMaster());
			PreparedStatement insBoSalePstmt = con
					.prepareStatement("insert into st_cs_bo_sale(transaction_id,agent_org_id,product_id,mrp_amt,agent_comm,net_amt,jv_comm,jv_comm_amt, vat, vat_amt, govt_comm, govt_comm_amt) values(?,?,?,?,?,?,?,?,?,?,?,?)");
			PreparedStatement updAgtSalepstmt = con
					.prepareStatement("update st_cs_agt_sale a inner join  st_lms_agent_transaction_master b on a.transaction_id = b.transaction_id set claim_status='DONE_CLAIM',bo_ref_transaction_id=? where a.agent_org_id=? and a.product_id =? and agent_comm=? and jv_comm=? and DATE(transaction_date)=? and transaction_type=?");
			PreparedStatement insBoSaleRetpstmt = con
					.prepareStatement("insert into st_cs_bo_refund(transaction_id,agent_org_id,product_id,mrp_amt,agent_comm,net_amt,jv_comm,jv_comm_amt, vat, vat_amt,govt_comm,govt_comm_amt,cancellation_charges) values(?,?,?,?,?,?,?,?,?,?,?,?,?)");
			PreparedStatement updAgtSaleRetPstmt = con
					.prepareStatement("update st_cs_agt_refund a inner join  st_lms_agent_transaction_master b on a.transaction_id = b.transaction_id set claim_status='DONE_CLAIM',bo_ref_transaction_id=? where a.agent_org_id=? and a.product_id =? and agent_comm=? and jv_comm=? and DATE(transaction_date)=? and transaction_type=?");

			int productId = 0;

			pstmt = con.prepareStatement(saleQry);
			rs = pstmt.executeQuery();
			while (rs.next()) {

				agtOrgId = rs.getInt("agent_org_id");
				boUserId = rs.getInt("boUserId");
				boOrgId = rs.getInt("boOrgId");
				productId = rs.getInt("product_id");
				totalMrpAmt = rs.getDouble("totalMrp");
				agtCommRate = rs.getDouble("totalagtComm");
				totalAgtComm = rs.getDouble("totalagtComm") * totalMrpAmt
						* 0.01;
				totalJVComm = rs.getDouble("totalJVComm") * totalMrpAmt * 0.01;
				JVCommRate = rs.getDouble("totalJVComm");
				govtComm = rs.getDouble("govt_comm");
				govtCommAmt = rs.getDouble("totalgovtComm");
				vat = rs.getDouble("vat");
				vatAmt = rs.getDouble("totalVat");
				String tranType = rs.getString("transaction_type");
				transDate = rs.getString("transaction_date");

				totalAgtNetAmt = totalMrpAmt - totalAgtComm;

				if (orgIdAmountMap.containsKey(agtOrgId)) {
					agtDebitAmount = orgIdAmountMap.get(agtOrgId)
							+ totalAgtNetAmt;
				} else {
					agtDebitAmount = totalAgtNetAmt;
				}

				orgIdAmountMap.put(agtOrgId, agtDebitAmount);
				orgIdParentIdMap.put(agtOrgId, boOrgId);

				if (orgIdTransIdInvMap.containsKey(agtOrgId)) {
					transIdInvMap = orgIdTransIdInvMap.get(agtOrgId);
				} else {
					transIdInvMap = new HashMap<String, List<Long>>();
					orgIdTransIdInvMap.put(agtOrgId, transIdInvMap);
				}

				// insert in main transaction table

				pstmtBO.setString(1, "BO");
				pstmtBO.executeUpdate();
				ResultSet rsTrns = pstmtBO.getGeneratedKeys();
				if (rsTrns.next()) {
					long transId = rsTrns.getLong(1);
					logger.debug(productId+"--"+agtOrgId+"-Agent Sale ---"+transId+"---"+transDate);
					if (!transIdInvMap.containsKey(transDate)) {
						transIdInvMap.put(transDate, new ArrayList<Long>());
					}

					transIdInvMap.get(transDate).add(transId);

					insBoTranPstmt.setLong(1, transId);
					insBoTranPstmt.setInt(2, boUserId);
					insBoTranPstmt.setInt(3, boOrgId);
					insBoTranPstmt.setString(4, "AGENT");
					insBoTranPstmt.setInt(5, agtOrgId);
					insBoTranPstmt.setTimestamp(6,
							fetchTransTimeStamp(transDate));
					insBoTranPstmt.setString(7, "CS_SALE");
					insBoTranPstmt.executeUpdate();

					insBoSalePstmt.setLong(1, transId);
					insBoSalePstmt.setInt(2, agtOrgId);
					insBoSalePstmt.setInt(3, productId);
					insBoSalePstmt.setDouble(4, totalMrpAmt);
					insBoSalePstmt.setDouble(5, agtCommRate);
					insBoSalePstmt.setDouble(6, totalAgtNetAmt);
					insBoSalePstmt.setDouble(7, JVCommRate);
					insBoSalePstmt.setDouble(8, totalJVComm);
					insBoSalePstmt.setDouble(9, vat);
					insBoSalePstmt.setDouble(10, vatAmt);
					insBoSalePstmt.setDouble(11, govtComm);
					insBoSalePstmt.setDouble(12, govtCommAmt);
					insBoSalePstmt.executeUpdate();

					// update agent table
					updAgtSalepstmt.setLong(1, transId); // agent trans Id
					updAgtSalepstmt.setInt(2, agtOrgId);
					updAgtSalepstmt.setInt(3, productId);
					updAgtSalepstmt.setDouble(4, agtCommRate);
					updAgtSalepstmt.setDouble(5, JVCommRate);
					updAgtSalepstmt.setString(6, transDate);
					updAgtSalepstmt.setString(7, tranType);
					logger.debug("sale status update qry for agent: "
							+ updAgtSalepstmt);
					updAgtSalepstmt.executeUpdate();
				}

			}

			pstmt = con.prepareStatement(saleReturnQry);
			rs = pstmt.executeQuery();
			while (rs.next()) {

				agtOrgId = rs.getInt("agent_org_id");
				boUserId = rs.getInt("boUserId");
				boOrgId = rs.getInt("boOrgId");
				productId = rs.getInt("product_id");
				totalMrpAmt = rs.getDouble("totalMrp");
				agtCommRate = rs.getDouble("totalagtComm");
				totalAgtComm = rs.getDouble("totalagtComm") * totalMrpAmt
						* 0.01;
				totalJVComm = rs.getDouble("totalJVComm") * totalMrpAmt * 0.01;
				JVCommRate = rs.getDouble("totalJVComm");
				govtComm = rs.getDouble("govt_comm");
				govtCommAmt = rs.getDouble("totalgovtComm");
				vat = rs.getDouble("vat");
				vatAmt = rs.getDouble("totalVat");
				String tranType = rs.getString("transaction_type");
				totalCancelCharge = rs.getDouble("totalCancelCharge");
				transDate = rs.getString("transaction_date");

				totalAgtNetAmt = totalMrpAmt - totalAgtComm - totalCancelCharge;

				if (orgIdAmountMap.containsKey(agtOrgId)) {
					agtDebitAmount = orgIdAmountMap.get(agtOrgId)
							- totalAgtNetAmt;
				} else {
					agtDebitAmount = totalAgtNetAmt;
				}

				orgIdAmountMap.put(agtOrgId, agtDebitAmount);
				orgIdParentIdMap.put(agtOrgId, boOrgId);

				if (orgIdTransIdInvRetMap.containsKey(agtOrgId)) {
					transIdInvRetMap = orgIdTransIdInvRetMap.get(agtOrgId);
				} else {
					transIdInvRetMap = new HashMap<String, List<Long>>();
					orgIdTransIdInvRetMap.put(agtOrgId, transIdInvRetMap);
				}

				// insert in main transaction table

				pstmtBO.setString(1, "BO");
				pstmtBO.executeUpdate();
				ResultSet rsTrns = pstmtBO.getGeneratedKeys();
				if (rsTrns.next()) {
					long transId = rsTrns.getLong(1);
					logger.debug(productId+"--"+agtOrgId+"-Agent Cancel ---"+transId+"---"+transDate);
					if (!transIdInvRetMap.containsKey(transDate)) {
						transIdInvRetMap.put(transDate,
								new ArrayList<Long>());
					}

					transIdInvRetMap.get(transDate).add(transId);

					insBoTranPstmt.setLong(1, transId);
					insBoTranPstmt.setInt(2, boUserId);
					insBoTranPstmt.setInt(3, boOrgId);
					insBoTranPstmt.setString(4, "AGENT");
					insBoTranPstmt.setInt(5, agtOrgId);
					insBoTranPstmt.setTimestamp(6,
							fetchTransTimeStamp(transDate));
					insBoTranPstmt.setString(7, tranType);
					insBoTranPstmt.executeUpdate();

					insBoSaleRetpstmt.setLong(1, transId);
					insBoSaleRetpstmt.setInt(2, agtOrgId);
					insBoSaleRetpstmt.setInt(3, productId);
					insBoSaleRetpstmt.setDouble(4, totalMrpAmt);
					insBoSaleRetpstmt.setDouble(5, agtCommRate);
					insBoSaleRetpstmt.setDouble(6, totalAgtNetAmt);
					insBoSaleRetpstmt.setDouble(7, JVCommRate);
					insBoSaleRetpstmt.setDouble(8, totalJVComm);
					insBoSaleRetpstmt.setDouble(9, vat);
					insBoSaleRetpstmt.setDouble(10, vatAmt);
					insBoSaleRetpstmt.setDouble(11, govtComm);
					insBoSaleRetpstmt.setDouble(12, govtCommAmt);
					insBoSaleRetpstmt.setDouble(13, totalCancelCharge);
					insBoSaleRetpstmt.executeUpdate();

					// update agent table
					updAgtSaleRetPstmt.setLong(1, transId); // agent trans Id
					updAgtSaleRetPstmt.setInt(2, agtOrgId);
					updAgtSaleRetPstmt.setInt(3, productId);
					updAgtSaleRetPstmt.setDouble(4, agtCommRate);
					updAgtSaleRetPstmt.setDouble(5, JVCommRate);
					updAgtSaleRetPstmt.setString(6, transDate);
					updAgtSaleRetPstmt.setString(7, tranType);
					logger.debug("sale return status update qry for agent: "
							+ updAgtSaleRetPstmt);
					updAgtSaleRetPstmt.executeUpdate();
				}

			}

			generateDGReceiptForBONew(orgIdTransIdInvMap, orgIdParentIdMap,
					"CS_INVOICE", "CS_RECEIPT", con);
			generateDGReceiptForBONew(orgIdTransIdInvRetMap, orgIdParentIdMap,
					"CR_NOTE", "CR_NOTE_CASH", con);

			logger.debug("agent orgIdAmountMap\n" + orgIdAmountMap);
			// update agent balance
			Set<Integer> agtOrgIdSet = orgIdAmountMap.keySet();
			for (Integer orgId : agtOrgIdSet) {
				
					boolean isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(orgIdAmountMap.get(orgId), "TRANSACTION", "CS_SALE", orgId,
						0, "AGENT", 0, con);
				if(!isValid)
					throw new LMSException();
				OrgCreditUpdation.updateOrganizationBalWithValidate(orgIdAmountMap.get(orgId), "CLAIM_BAL", "DEBIT", orgId,
						0, "AGENT", 0, con);
					/*OrgCreditUpdation.updateCreditLimitForAgent(orgId, "CS_SALE",
						orgIdAmountMap.get(orgId), con);*/
				/*updateOrgBalance("CLAIM_BAL", orgIdAmountMap.get(orgId), orgId,
						"DEBIT", con);*/
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("error in sale agent");
		}
		logger.debug(new Date() + "---end-cs sale update for agent--"
				+ new Date().getTime());

	}

	public static Timestamp fetchTransTimeStamp(String transDate)
			throws ParseException {

		String curDate = (new SimpleDateFormat("yyyy-MM-dd"))
				.format((new Timestamp((new Date()).getTime())));

		if (transDate.equals(curDate)) {
			return new java.sql.Timestamp(new java.util.Date().getTime());
		} else {
			transDate = transDate + " 23:30:00";
			return new Timestamp((new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"))
					.parse(transDate).getTime());
		}

	}

	public void fillTempDGTransCommTlbForRet(Connection con)
			throws LMSException {
		logger.debug(new Date() + "---start---" + new Date().getTime());

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con.prepareStatement("truncate table st_temp_update_ledger")
					.executeUpdate();

			String gameQry = "select game_id from st_dg_game_master";
			pstmt = con.prepareStatement(gameQry);
			rs = pstmt.executeQuery();
			//int transId = 0;
			long transId=0;
			int gameId = 0;
			int orgId = 0;
			int parentId = 0;
			long transDate = 0;
			double retComm, agtComm, govtComm;
			Statement stmt = con.createStatement();
			while (rs.next()) {
				int count = 0;
				gameId = rs.getInt("game_id");
				StringBuilder sb = new StringBuilder(
						"insert into st_temp_update_ledger (trans_id, ret_comm, agt_comm, govt_comm) values");

				String transQry = "select sale.transaction_id,sale.game_id ,transaction_date,sale.retailer_org_id,parent_id from st_lms_retailer_transaction_master tm inner join st_lms_organization_master om inner join st_dg_ret_sale_"
						+ gameId
						+ " sale on tm.retailer_org_id=om.organization_id and tm.transaction_id=sale.transaction_id  where claim_status in('CLAIM_BAL') union all select sale.transaction_id,sale.game_id ,transaction_date,sale.retailer_org_id,parent_id from st_lms_retailer_transaction_master tm inner join st_lms_organization_master om inner join st_dg_ret_sale_refund_"
						+ gameId
						+ " sale on tm.retailer_org_id=om.organization_id and tm.transaction_id=sale.transaction_id  where claim_status in('CLAIM_BAL')";
				PreparedStatement traStmt = con.prepareStatement(transQry);
				ResultSet tranRs = traStmt.executeQuery();
				gameQry = "select * from (select game_id,sum(retailer_sale_comm_rate) ret_comm,sum(agent_sale_comm_rate) agt_comm,sum(govt_comm) govt_comm,now() date from (select game_id,retailer_sale_comm_rate,agent_sale_comm_rate,govt_comm from st_dg_game_master where game_id="
						+ gameId
						+ " union all select game_id,sale_comm_variance,0,0 from st_dg_agent_retailer_sale_pwt_comm_variance where retailer_org_id=? and game_id="
						+ gameId
						+ " union all select game_id,0,sale_comm_variance,0 from st_dg_bo_agent_sale_pwt_comm_variance where agent_org_id=? and game_id="
						+ gameId
						+ ") aa group by game_id union all select gm.game_id,retailer_sale_comm_rate+sale_comm_variance retComm,0 agtComm,0 govtComm,date_changed from st_dg_agent_retailer_sale_comm_variance_history his inner join st_dg_game_master gm on his.game_id=gm.game_id where retialer_org_id=? and gm.game_id="
						+ gameId
						+ " union all select gm.game_id,0,agent_sale_comm_rate+sale_comm_variance agtComm,0,date_changed from st_dg_bo_agent_sale_comm_variance_history his inner join st_dg_game_master gm on his.game_id=gm.game_id where agent_org_id=? and gm.game_id="
						+ gameId
						+ " union all select game_id,0,0,govt_comm_rate,date_changed from st_dg_game_govt_comm_history where game_id="
						+ gameId + ") aa order by date desc";
				PreparedStatement gameStmt = con.prepareStatement(gameQry);

				while (tranRs.next()) {
					transId = tranRs.getLong("transaction_id");
					orgId = tranRs.getInt("retailer_org_id");
					parentId = tranRs.getInt("parent_id");
					transDate = tranRs.getTimestamp("transaction_date")
							.getTime();
					gameStmt.setInt(1, orgId);
					gameStmt.setInt(2, parentId);
					gameStmt.setInt(3, orgId);
					gameStmt.setInt(4, parentId);
					ResultSet gameRs = gameStmt.executeQuery();
					retComm = 0;
					agtComm = 0;
					govtComm = 0;

					while (gameRs.next()) {
						double retCommTemp = gameRs.getDouble("ret_comm");
						double agtCommTemp = gameRs.getDouble("agt_comm");
						double govtCommTemp = gameRs.getDouble("govt_comm");
						if (gameRs.getTimestamp("date").getTime() > transDate) {
							if (retCommTemp != 0.0) {
								retComm = retCommTemp;
							}
							if (agtCommTemp != 0.0) {
								agtComm = agtCommTemp;
							}
							if (govtCommTemp != 0.0) {
								govtComm = govtCommTemp;
							}
						}
					}

					sb.append("(" + transId + ", " + retComm + ", " + agtComm
							+ "," + govtComm + "),");
					count++;

					if (count > 500) {
						sb.deleteCharAt(sb.length() - 1);
						stmt.executeUpdate(sb.toString());
						count = 0;
						sb = null;
						sb = new StringBuilder(
								"insert into st_temp_update_ledger (trans_id, ret_comm,agt_comm,govt_comm) values");
					}

				}

				if (count > 0) {
					sb.deleteCharAt(sb.length() - 1);
					stmt.executeUpdate(sb.toString());
				}
			}
			logger.debug(new Date() + "---end---" + new Date().getTime());
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("enable to fill comm table for retailer");
		}
	}

	public void fillTempDGTransCommTlbForAgt(Connection con)
			throws LMSException {
		logger.debug(new Date() + "---start---" + new Date().getTime());

		try {
			con.prepareStatement("truncate table st_temp_update_ledger")
					.executeUpdate(); // it commits all previous transaction in database of this connection;
			long transId = 0;
			int gameId = 0;
			int orgId = 0;
			Timestamp transDate = null;
			double retComm, agtComm, govtComm;
			String gameQry = "select * from (select game_id,sum(retailer_sale_comm_rate) ret_comm,sum(agent_sale_comm_rate) agt_comm,sum(govt_comm) govt_comm,now() date from (select game_id,retailer_sale_comm_rate,agent_sale_comm_rate,govt_comm from st_dg_game_master where game_id=? union all select game_id,0,sale_comm_variance,0 from st_dg_bo_agent_sale_pwt_comm_variance where agent_org_id=? and game_id=?) aa group by game_id union all select gm.game_id,0,agent_sale_comm_rate+sale_comm_variance agtComm,0,date_changed from st_dg_bo_agent_sale_comm_variance_history his inner join st_dg_game_master gm on his.game_id=gm.game_id where agent_org_id=? and gm.game_id=? union all select game_id,0,0,govt_comm_rate,date_changed from st_dg_game_govt_comm_history where game_id=?) aa order by date desc";
			Statement stmt = con.createStatement();

			int count = 0;
			StringBuilder sb = new StringBuilder(
					"insert into st_temp_update_ledger (trans_id, ret_comm, agt_comm, govt_comm) values");

			String transQry = "select sale.transaction_id,sale.game_id ,transaction_date,sale.agent_org_id,parent_id from st_lms_agent_transaction_master tm inner join st_lms_organization_master om inner join st_dg_agt_sale sale on sale.agent_org_id=om.organization_id and tm.transaction_id=sale.transaction_id  where claim_status in('CLAIM_BAL') union all select sale.transaction_id,sale.game_id ,transaction_date,sale.agent_org_id,parent_id from st_lms_agent_transaction_master tm inner join st_lms_organization_master om inner join st_dg_agt_sale_refund sale on sale.agent_org_id=om.organization_id and tm.transaction_id=sale.transaction_id  where claim_status in('CLAIM_BAL')";
			PreparedStatement traStmt = con.prepareStatement(transQry);
			ResultSet tranRs = traStmt.executeQuery();
			PreparedStatement gameStmt = con.prepareStatement(gameQry);

			while (tranRs.next()) {
				transId = tranRs.getLong("transaction_id");
				gameId = tranRs.getInt("game_id");
				orgId = tranRs.getInt("agent_org_id");
				transDate = tranRs.getTimestamp("transaction_date");
				gameStmt.setInt(1, gameId);
				gameStmt.setInt(2, orgId);
				gameStmt.setInt(3, gameId);
				gameStmt.setInt(4, orgId);
				gameStmt.setInt(5, gameId);
				gameStmt.setInt(6, gameId);
				ResultSet gameRs = gameStmt.executeQuery();
				retComm = 0;
				agtComm = 0;
				govtComm = 0;
				while (gameRs.next()) {
					if (gameRs.getTimestamp("date").getTime() > transDate
							.getTime()) {
						if (gameRs.getDouble("ret_comm") != 0.0) {
							retComm = gameRs.getDouble("ret_comm");
						}
						if (gameRs.getDouble("agt_comm") != 0.0) {
							agtComm = gameRs.getDouble("agt_comm");
						}
						if (gameRs.getDouble("govt_comm") != 0.0) {
							govtComm = gameRs.getDouble("govt_comm");
						}
					}
				}

				sb.append("(" + transId + ", " + retComm + ", " + agtComm + ","
						+ govtComm + "),");
				count++;
				// if (count > 500) {
				// sb.deleteCharAt(sb.length() - 1);
				// stmt.executeUpdate(sb.toString());
				// count = 0;
				//
				// sb = new StringBuilder(
				// "insert into st_temp_update_ledger (trans_id, ret_comm,
				// agt_comm, govt_comm) values");
				//
				// }
			}
			if (count > 0) {
				sb.deleteCharAt(sb.length() - 1);
				stmt.executeUpdate(sb.toString());
			}
			logger.debug(new Date() + "---end---" + new Date().getTime());
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("error in comm table agent");
		}
	}

	public void updateDGSaleLedgerForRet(Connection con) throws LMSException {
		logger.debug(new Date() + "---start-dg sale update for retailer--"
				+ new Date().getTime());
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		double totalMrpAmt, totalRetComm, totalAgtComm, totalGoodCauseAmt, totalretNetAmt, totalAgtNetAmt, totalCancelCharge;
		double retCommRate, agtCommRate, govtCommRate, retDebitAmount;
		String transDate;
		int retOrgId, agtUserId, agtOrgId=0;
		Map<Integer, Double> orgIdAmountMap = new HashMap<Integer, Double>();
		Map<Integer, Integer> orgIdParentIdMap = new HashMap<Integer, Integer>();
		Map<Integer, Map<String, List<Long>>> orgIdTransIdInvMap = new HashMap<Integer, Map<String, List<Long>>>();
		Map<String, List<Long>> transIdInvMap = null;
		Map<Integer, Map<String, List<Long>>> orgIdTransIdInvRetMap = new HashMap<Integer, Map<String, List<Long>>>();
		Map<String, List<Long>> transIdInvRetMap = null;
		Statement tempStmt=null;
		
		try {
			String getGameNbrFromGameMaster = "select game_id,prize_payout_ratio,vat_amt from st_dg_game_master";
			String saleQry = "select a.retailer_org_id,agtUserId,agtOrgId,sum(mrp_amt) totalMrp ,sum(retailer_comm) totalRetComm,sum(agent_comm) totalagtComm, sum(good_cause_amt) totalgovtComm, sum(vat_amt) totalVat,sum(taxable_sale) totalTaxSale,sum(ret_sd_amt) totalSd,sum(agt_vat_amt)totatAgtVat,a.game_id,DATE(transaction_date) 'transaction_date',ret_comm,agt_comm,govt_comm from st_dg_ret_sale_? a inner join  st_lms_retailer_transaction_master b inner join st_temp_update_ledger c inner join (select um.user_id retUserId,um.organization_id retOrgId,um.parent_user_id agtUserId,om.parent_id agtOrgId from st_lms_organization_master om inner join st_lms_user_master um on om.organization_id=um.organization_id where om.organization_type='RETAILER') d on a.transaction_id = b.transaction_id and a.transaction_id=trans_id and a.retailer_org_id=retOrgId  where claim_status='CLAIM_BAL' group by a.retailer_org_id,DATE(transaction_date),a.game_id,ret_comm,agt_comm,govt_comm  order by retailer_org_id,game_id,transaction_date";
			String saleReturnQry = "select a.retailer_org_id,agtUserId,agtOrgId,sum(mrp_amt) totalMrp ,sum(retailer_comm) totalRetComm,sum(agent_comm) totalagtComm, sum(good_cause_amt) totalgovtComm, sum(vat_amt) totalVat,sum(taxable_sale) totalTaxSale,sum(cancellation_charges) totalCancelCharge,sum(ret_sd_amt) totalSd,sum(agt_vat_amt)totatAgtVat, a.game_id,DATE(transaction_date) 'transaction_date',ret_comm,agt_comm,govt_comm,transaction_type from st_dg_ret_sale_refund_? a inner join  st_lms_retailer_transaction_master b inner join st_temp_update_ledger c inner join (select um.user_id retUserId,um.organization_id retOrgId,um.parent_user_id agtUserId,om.parent_id agtOrgId from st_lms_organization_master om inner join st_lms_user_master um on om.organization_id=um.organization_id where om.organization_type='RETAILER') d on a.transaction_id = b.transaction_id and a.transaction_id=trans_id and a.retailer_org_id=retOrgId  where claim_status='CLAIM_BAL' group by a.retailer_org_id,DATE(transaction_date),a.game_id,transaction_type,ret_comm,agt_comm,govt_comm  order by retailer_org_id,game_id,transaction_date";
			PreparedStatement pstmtGameNbr = con
					.prepareStatement(getGameNbrFromGameMaster);
			ResultSet resultGameNbr = pstmtGameNbr.executeQuery();

			PreparedStatement pstmtAgt = con.prepareStatement(QueryManager
					.insertInLMSTransactionMaster());
			PreparedStatement pstmtAgtTrans = con.prepareStatement(QueryManager
					.insertInAgentTransactionMaster());
			PreparedStatement insAgtSalePstmt = con
					.prepareStatement("insert into st_dg_agt_sale(transaction_id,agent_org_id,retailer_org_id,game_id,mrp_amt,retailer_comm,net_amt,agent_comm,bo_net_amt,claim_status,vat_amt,taxable_sale,govt_comm) values(?,?,?,?,?,?,?,?,?,?,?,?,?)");
			//PreparedStatement updateRetSalePstmt = con
				//	.prepareStatement("update st_dg_ret_sale_? a inner join  st_lms_retailer_transaction_master b inner join st_temp_update_ledger c on a.transaction_id = b.transaction_id and a.transaction_id=trans_id set claim_status='DONE_CLAIM',agent_ref_transaction_id=? where a.retailer_org_id=? and ret_comm=? and agt_comm=? and govt_comm=? and DATE(transaction_date)=?");
			PreparedStatement insTempTablePstmt=con.prepareStatement("insert into orgTransSummary(agent_ref_transaction_id,ret_comm,agt_comm,govt_comm,retailer_org_id,game_id,trans_date) values(?,?,?,?,?,?,?)");
			PreparedStatement insAgtSaleRetPstmt = con
					.prepareStatement("insert into st_dg_agt_sale_refund(transaction_id,agent_org_id,retailer_org_id,game_id,mrp_amt,retailer_comm,net_amt,agent_comm,bo_net_amt,claim_status,vat_amt,taxable_sale,govt_comm,cancellation_charges) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			//PreparedStatement updateRetSaleRetPstmt = con
				//	.prepareStatement("update st_dg_ret_sale_refund_? a inner join  st_lms_retailer_transaction_master b inner join st_temp_update_ledger c on a.transaction_id = b.transaction_id and a.transaction_id=trans_id set claim_status='DONE_CLAIM',agent_ref_transaction_id=? where a.retailer_org_id=? and ret_comm=? and agt_comm=? and govt_comm=? and DATE(transaction_date)=? and transaction_type=?");
			tempStmt=con.createStatement();
			tempStmt.execute("delete from  orgTransSummary ");
			int gameId = 0;
			double ppr = 0.0;
			double vatAmt = 0.0;
			final int batchSize = 500;
			int count = 0;
			while (resultGameNbr.next()) {
				gameId = resultGameNbr.getInt("game_id");
				ppr = resultGameNbr.getInt("prize_payout_ratio");
				vatAmt = resultGameNbr.getDouble("vat_amt");
				pstmt = con.prepareStatement(saleQry);
				pstmt.setInt(1, gameId);
				rs = pstmt.executeQuery();
				logger.debug("--Start Sale tran for game No" + gameId);
				
				while (rs.next()) {
					retOrgId = rs.getInt("retailer_org_id");
					agtUserId = rs.getInt("agtUserId");
					agtOrgId = rs.getInt("agtOrgId");

					totalMrpAmt = rs.getDouble("totalMrp");
					if("BENIN".equalsIgnoreCase(Utility.getPropertyValue("COUNTRY_DEPLOYED"))){
						totalRetComm = rs.getDouble("totalRetComm")-rs.getDouble("totalVat")-rs.getDouble("totalSd");
						totalAgtComm = totalRetComm;
					}else{
						totalRetComm = rs.getDouble("totalRetComm");
						totalAgtComm = rs.getDouble("totalagtComm");
					}
					totalGoodCauseAmt = rs.getDouble("totalgovtComm");
					retCommRate = rs.getDouble("ret_comm");
					agtCommRate = rs.getDouble("agt_comm");
					govtCommRate = rs.getDouble("govt_comm");
					transDate = rs.getString("transaction_date");
					totalretNetAmt = totalMrpAmt - totalRetComm;
					totalAgtNetAmt = totalMrpAmt - totalAgtComm;
					double totalAgtVat= rs.getDouble("totatAgtVat");
					/*double totalAgtVat = CommonMethods
							.calculateDrawGameVatRet(totalMrpAmt, retCommRate,
									ppr, govtCommRate, vatAmt);*/
					double tatalAgtTaxableSale = CommonMethods
							.calTaxableSale(totalMrpAmt, retCommRate, ppr,
									govtCommRate, vatAmt);

					if (orgIdAmountMap.containsKey(retOrgId)) {
						retDebitAmount = orgIdAmountMap.get(retOrgId)
								+ totalretNetAmt;
					} else {
						retDebitAmount = totalretNetAmt;
					}

					orgIdAmountMap.put(retOrgId, retDebitAmount);
					orgIdParentIdMap.put(retOrgId, agtOrgId);

					if (orgIdTransIdInvMap.containsKey(retOrgId)) {
						transIdInvMap = orgIdTransIdInvMap.get(retOrgId);
					} else {
						transIdInvMap = new HashMap<String, List<Long>>();
						orgIdTransIdInvMap.put(retOrgId, transIdInvMap);
					}

					// insert in main transaction table

					pstmtAgt.setString(1, "AGENT");
					pstmtAgt.executeUpdate();
					ResultSet rsTrns = pstmtAgt.getGeneratedKeys();
					if (rsTrns.next()) {
						long transId = rsTrns.getLong(1);
						logger.debug("--Retailer org id"+retOrgId+"--Agent org Id--"+agtOrgId+"--Start Sale tran for game No" + gameId);
						if (!transIdInvMap.containsKey(transDate)) {
							transIdInvMap.put(transDate,
									new ArrayList<Long>());
						}

						transIdInvMap.get(transDate).add(transId);

						pstmtAgtTrans.setLong(1, transId);
						pstmtAgtTrans.setInt(2, agtUserId);
						pstmtAgtTrans.setInt(3, agtOrgId);
						pstmtAgtTrans.setString(4, "RETAILER");
						pstmtAgtTrans.setInt(5, retOrgId);
						pstmtAgtTrans.setString(6, "DG_SALE");
						pstmtAgtTrans.setTimestamp(7,
								fetchTransTimeStamp(transDate));
						pstmtAgtTrans.executeUpdate();

						insAgtSalePstmt.setLong(1, transId);
						insAgtSalePstmt.setInt(2, agtOrgId);
						insAgtSalePstmt.setInt(3, retOrgId);
						insAgtSalePstmt.setInt(4, gameId);
						insAgtSalePstmt.setDouble(5, totalMrpAmt);
						insAgtSalePstmt.setDouble(6, totalRetComm);
						insAgtSalePstmt.setDouble(7, totalretNetAmt);
						insAgtSalePstmt.setDouble(8, totalAgtComm);
						insAgtSalePstmt.setDouble(9, totalAgtNetAmt);
						insAgtSalePstmt.setString(10, "CLAIM_BAL");
						insAgtSalePstmt.setDouble(11, totalAgtVat);
						insAgtSalePstmt.setDouble(12, tatalAgtTaxableSale);
						insAgtSalePstmt.setDouble(13, totalGoodCauseAmt);
						insAgtSalePstmt.executeUpdate();

						
						
						//insert temp table
						insTempTablePstmt.setLong(1, transId);
						insTempTablePstmt.setDouble(2, retCommRate);
						insTempTablePstmt.setDouble(3, agtCommRate);
						insTempTablePstmt.setDouble(4, govtCommRate);
						insTempTablePstmt.setInt(5, retOrgId);
						insTempTablePstmt.setInt(6, gameId);
						insTempTablePstmt.setString(7, transDate);
						insTempTablePstmt.addBatch();
						if(++count % batchSize == 0) {
							insTempTablePstmt.executeBatch();
					    }
						/*// update retailer table

						updateRetSalePstmt.setInt(1, gameId);
						updateRetSalePstmt.setLong(2, transId); // agent trans
						// Id
						updateRetSalePstmt.setInt(3, retOrgId);
						updateRetSalePstmt.setDouble(4, retCommRate);
						updateRetSalePstmt.setDouble(5, agtCommRate);
						updateRetSalePstmt.setDouble(6, govtCommRate);
						updateRetSalePstmt.setString(7, transDate);
						updateRetSalePstmt.addBatch();*/
						
					}

				}
				
				insTempTablePstmt.executeBatch();
				
				
				tempStmt.executeUpdate("update st_dg_ret_sale_"+gameId+" sale inner join (select yy.transaction_id,xx.agent_ref_transaction_id from orgTransSummary xx inner join (select transaction_id,transaction_date,retailer_org_id,ret_comm,agt_comm,govt_comm,game_id from st_lms_retailer_transaction_master a inner join st_temp_update_ledger b on a.transaction_id = b.trans_id)yy on DATE(trans_date)=DATE(transaction_date) and xx.retailer_org_id=yy.retailer_org_id and xx.ret_comm=yy.ret_comm and xx.agt_comm=yy.agt_comm and xx.govt_comm=yy.govt_comm and xx.game_id=yy.game_id) tlb on sale.transaction_id=tlb.transaction_id set sale.claim_status='DONE_CLAIM',sale.agent_ref_transaction_id=tlb.agent_ref_transaction_id");
				tempStmt.execute("delete from  orgTransSummary ");
				//tempStmt.execute("truncate table orgTransSummary");
				
				pstmt = con.prepareStatement(saleReturnQry);
				pstmt.setInt(1, gameId);
				rs = pstmt.executeQuery();
				logger.debug("--Start Cancel tran for game No" + gameId);
				while (rs.next()) {
					retOrgId = rs.getInt("retailer_org_id");
					agtUserId = rs.getInt("agtUserId");
					agtOrgId = rs.getInt("agtOrgId");

					totalMrpAmt = rs.getDouble("totalMrp");
					if("BENIN".equalsIgnoreCase(Utility.getPropertyValue("COUNTRY_DEPLOYED"))){
						totalRetComm = rs.getDouble("totalRetComm")-rs.getDouble("totalVat")-rs.getDouble("totalSd");
						totalAgtComm = totalRetComm;
					}else{
						totalRetComm = rs.getDouble("totalRetComm");
						totalAgtComm = rs.getDouble("totalagtComm");
					}
					totalGoodCauseAmt = rs.getDouble("totalgovtComm");
					totalCancelCharge = rs.getDouble("totalCancelCharge");
					retCommRate = rs.getDouble("ret_comm");
					agtCommRate = rs.getDouble("agt_comm");
					govtCommRate = rs.getDouble("govt_comm");
					transDate = rs.getString("transaction_date");
					totalretNetAmt = totalMrpAmt - totalRetComm
							- totalCancelCharge;
					totalAgtNetAmt = totalMrpAmt - totalAgtComm
							- totalCancelCharge;
					String tranType = rs.getString("transaction_type");
					double totalAgtVat =rs.getDouble("totatAgtVat");
					/*double totalAgtVat = CommonMethods
							.calculateDrawGameVatRet(totalMrpAmt, retCommRate,
									ppr, govtCommRate, vatAmt);*/
					double tatalAgtTaxableSale = CommonMethods
							.calTaxableSale(totalMrpAmt, retCommRate, ppr,
									govtCommRate, vatAmt);

					if (orgIdAmountMap.containsKey(retOrgId)) {
						retDebitAmount = orgIdAmountMap.get(retOrgId)
								- totalretNetAmt;
					} else {
						retDebitAmount = -totalretNetAmt;
					}

					orgIdAmountMap.put(retOrgId, retDebitAmount);
					orgIdParentIdMap.put(retOrgId, agtOrgId);

					if (orgIdTransIdInvRetMap.containsKey(retOrgId)) {
						transIdInvRetMap = orgIdTransIdInvRetMap.get(retOrgId);
					} else {
						transIdInvRetMap = new HashMap<String, List<Long>>();
						orgIdTransIdInvRetMap.put(retOrgId, transIdInvRetMap);
					}

					// insert in main transaction table

					pstmtAgt.setString(1, "AGENT");
					pstmtAgt.executeUpdate();
					ResultSet rsTrns = pstmtAgt.getGeneratedKeys();
					if (rsTrns.next()) {
						long transId = rsTrns.getLong(1);
						logger.debug("--Retailer org id"+retOrgId+"--Agent org Id--"+agtOrgId+"--Start Sale Cancel tran for game No" + gameId);
						if (!transIdInvRetMap.containsKey(transDate)) {
							transIdInvRetMap.put(transDate,
									new ArrayList<Long>());
						}

						transIdInvRetMap.get(transDate).add(transId);

						pstmtAgtTrans.setLong(1, transId);
						pstmtAgtTrans.setInt(2, agtUserId);
						pstmtAgtTrans.setInt(3, agtOrgId);
						pstmtAgtTrans.setString(4, "RETAILER");
						pstmtAgtTrans.setInt(5, retOrgId);
						pstmtAgtTrans.setString(6, tranType);
						pstmtAgtTrans.setTimestamp(7,
								fetchTransTimeStamp(transDate));
						pstmtAgtTrans.executeUpdate();

						insAgtSaleRetPstmt.setLong(1, transId);
						insAgtSaleRetPstmt.setInt(2, agtOrgId);
						insAgtSaleRetPstmt.setInt(3, retOrgId);
						insAgtSaleRetPstmt.setInt(4, gameId);
						insAgtSaleRetPstmt.setDouble(5, totalMrpAmt);
						insAgtSaleRetPstmt.setDouble(6, totalRetComm);
						insAgtSaleRetPstmt.setDouble(7, totalretNetAmt);
						insAgtSaleRetPstmt.setDouble(8, totalAgtComm);
						insAgtSaleRetPstmt.setDouble(9, totalAgtNetAmt);
						insAgtSaleRetPstmt.setString(10, "CLAIM_BAL");
						insAgtSaleRetPstmt.setDouble(11, totalAgtVat);
						insAgtSaleRetPstmt.setDouble(12, tatalAgtTaxableSale);
						insAgtSaleRetPstmt.setDouble(13, totalGoodCauseAmt);
						insAgtSaleRetPstmt.setDouble(14, totalCancelCharge);
						insAgtSaleRetPstmt.executeUpdate();

						
						//insert temp table
						insTempTablePstmt.setLong(1, transId);
						insTempTablePstmt.setDouble(2, retCommRate);
						insTempTablePstmt.setDouble(3, agtCommRate);
						insTempTablePstmt.setDouble(4, govtCommRate);
						insTempTablePstmt.setInt(5, retOrgId);
						insTempTablePstmt.setInt(6, gameId);
						insTempTablePstmt.setString(7, transDate);
						insTempTablePstmt.addBatch();
						if(++count % batchSize == 0) {
							insTempTablePstmt.executeBatch();
					    }
						
						// update retailer table
						/*updateRetSaleRetPstmt.setInt(1, gameId);
						updateRetSaleRetPstmt.setLong(2, transId); // agent
						// trans Id
						updateRetSaleRetPstmt.setInt(3, retOrgId);
						updateRetSaleRetPstmt.setDouble(4, retCommRate);
						updateRetSaleRetPstmt.setDouble(5, agtCommRate);
						updateRetSaleRetPstmt.setDouble(6, govtCommRate);
						updateRetSaleRetPstmt.setString(7, transDate);
						updateRetSaleRetPstmt.setString(8, tranType);
						updateRetSaleRetPstmt.addBatch();*/
						//updateRetSaleRetPstmt.executeUpdate();
					}

				}
				
				insTempTablePstmt.executeBatch();
				
				tempStmt.executeUpdate("update st_dg_ret_sale_refund_"+gameId+" sale inner join (select yy.transaction_id,xx.agent_ref_transaction_id from orgTransSummary xx inner join (select transaction_id,transaction_date,retailer_org_id,ret_comm,agt_comm,govt_comm,game_id from st_lms_retailer_transaction_master a inner join st_temp_update_ledger b on a.transaction_id = b.trans_id)yy on DATE(trans_date)=DATE(transaction_date) and xx.retailer_org_id=yy.retailer_org_id and xx.ret_comm=yy.ret_comm and xx.agt_comm=yy.agt_comm and xx.govt_comm=yy.govt_comm and xx.game_id=yy.game_id) tlb on sale.transaction_id=tlb.transaction_id set sale.claim_status='DONE_CLAIM',sale.agent_ref_transaction_id=tlb.agent_ref_transaction_id");
				tempStmt.execute("delete from orgTransSummary");
				
			}

			generateDGReceiptNew(orgIdTransIdInvMap, orgIdParentIdMap,
					"DG_INVOICE", "DG_RECEIPT", con);
			generateDGReceiptNew(orgIdTransIdInvRetMap, orgIdParentIdMap,
					"CR_NOTE", "CR_NOTE_CASH", con);

			logger.debug("retailer orgIdAmountMap\n" + orgIdAmountMap);
			// update retailer balance
			Set<Integer> retOrgIdSet = orgIdAmountMap.keySet();
			for (Integer orgId : retOrgIdSet) {
				boolean isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(CommonMethods.fmtToTwoDecimal(orgIdAmountMap.get(orgId)), "TRANSACTION", "DRAW_GAME_SALE", orgId,
						orgIdParentIdMap.get(orgId), "RETAILER", 0, con);
				if(!isValid)
					throw new LMSException();
					
				OrgCreditUpdation.updateOrganizationBalWithValidate(CommonMethods.fmtToTwoDecimal(orgIdAmountMap.get(orgId)), "CLAIM_BAL", "DEBIT", orgId,
						orgIdParentIdMap.get(orgId), "RETAILER", 0, con);
				
				/*OrgCreditUpdation.updateCreditLimitForRetailer(orgId,
						"DRAW_GAME_SALE", orgIdAmountMap.get(orgId), con);
				updateOrgBalance("CLAIM_BAL", orgIdAmountMap.get(orgId), orgId,
						"DEBIT", con);*/
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("error in sale retailer");
		}
		logger.debug(new Date() + "---end-dg sale update for retailer--"
				+ new Date().getTime());
	}

	public void updateDGSaleLedgerForRetwithoutComm(Connection con) throws LMSException {
		logger.debug(new Date() + "---start-dg sale update for retailer--"
				+ new Date().getTime());
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		double totalMrpAmt, totalRetComm, totalAgtComm, totalGoodCauseAmt, totalretNetAmt, totalAgtNetAmt, totalCancelCharge;
		double retCommRate,  govtCommRate,retDebitAmount;
		String transDate;
		int retOrgId, agtUserId, agtOrgId=0;
		Map<Integer, Double> orgIdAmountMap = new HashMap<Integer, Double>();
		Map<Integer, Integer> orgIdParentIdMap = new HashMap<Integer, Integer>();
		Map<Integer, Map<String, List<Long>>> orgIdTransIdInvMap = new HashMap<Integer, Map<String, List<Long>>>();
		Map<String, List<Long>> transIdInvMap = null;
		Map<Integer, Map<String, List<Long>>> orgIdTransIdInvRetMap = new HashMap<Integer, Map<String, List<Long>>>();
		Map<String, List<Long>> transIdInvRetMap = null;
		try {
			String getGameNbrFromGameMaster = "select game_id,prize_payout_ratio,vat_amt from st_dg_game_master";
			String saleQry = "select a.retailer_org_id,agtUserId,agtOrgId,sum(mrp_amt) totalMrp ,sum(retailer_comm) totalRetComm,sum(agent_comm) totalagtComm, sum(good_cause_amt) totalgovtComm, sum(vat_amt) totalVat,sum(taxable_sale) totalTaxSale,a.game_id,DATE(transaction_date) 'transaction_date' from st_dg_ret_sale_? a inner join  st_lms_retailer_transaction_master b inner join (select um.user_id retUserId,um.organization_id retOrgId,um.parent_user_id agtUserId,om.parent_id agtOrgId from st_lms_organization_master om inner join st_lms_user_master um on om.organization_id=um.organization_id where om.organization_type='RETAILER') d on a.transaction_id = b.transaction_id and a.retailer_org_id=retOrgId  where claim_status='CLAIM_BAL' group by a.retailer_org_id,DATE(transaction_date),a.game_id order by retailer_org_id,game_id,transaction_date";
			String saleReturnQry = "select a.retailer_org_id,agtUserId,agtOrgId,sum(mrp_amt) totalMrp ,sum(retailer_comm) totalRetComm,sum(agent_comm) totalagtComm, sum(good_cause_amt) totalgovtComm, sum(vat_amt) totalVat,sum(taxable_sale) totalTaxSale,sum(cancellation_charges) totalCancelCharge,a.game_id,DATE(transaction_date) 'transaction_date',transaction_type from st_dg_ret_sale_refund_? a inner join  st_lms_retailer_transaction_master b inner join (select um.user_id retUserId,um.organization_id retOrgId,um.parent_user_id agtUserId,om.parent_id agtOrgId from st_lms_organization_master om inner join st_lms_user_master um on om.organization_id=um.organization_id where om.organization_type='RETAILER') d on a.transaction_id = b.transaction_id and a.retailer_org_id=retOrgId  where claim_status='CLAIM_BAL' group by a.retailer_org_id,DATE(transaction_date),a.game_id,transaction_type order by retailer_org_id,game_id,transaction_date";
			PreparedStatement pstmtGameNbr = con
					.prepareStatement(getGameNbrFromGameMaster);
			ResultSet resultGameNbr = pstmtGameNbr.executeQuery();

			PreparedStatement pstmtAgt = con.prepareStatement(QueryManager
					.insertInLMSTransactionMaster());
			PreparedStatement pstmtAgtTrans = con.prepareStatement(QueryManager
					.insertInAgentTransactionMaster());
			PreparedStatement insAgtSalePstmt = con
					.prepareStatement("insert into st_dg_agt_sale(transaction_id,agent_org_id,retailer_org_id,game_id,mrp_amt,retailer_comm,net_amt,agent_comm,bo_net_amt,claim_status,vat_amt,taxable_sale,govt_comm) values(?,?,?,?,?,?,?,?,?,?,?,?,?)");
			PreparedStatement updateRetSalePstmt = con
					.prepareStatement("update st_dg_ret_sale_? a inner join  st_lms_retailer_transaction_master b on a.transaction_id = b.transaction_id set claim_status='DONE_CLAIM',agent_ref_transaction_id=? where a.retailer_org_id=? and DATE(transaction_date)=?");
			PreparedStatement insAgtSaleRetPstmt = con
					.prepareStatement("insert into st_dg_agt_sale_refund(transaction_id,agent_org_id,retailer_org_id,game_id,mrp_amt,retailer_comm,net_amt,agent_comm,bo_net_amt,claim_status,vat_amt,taxable_sale,govt_comm,cancellation_charges) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			PreparedStatement updateRetSaleRetPstmt = con
					.prepareStatement("update st_dg_ret_sale_refund_? a inner join  st_lms_retailer_transaction_master b on a.transaction_id = b.transaction_id set claim_status='DONE_CLAIM',agent_ref_transaction_id=? where a.retailer_org_id=? and DATE(transaction_date)=? and transaction_type=?");

			int gameId = 0;
			double ppr = 0.0;
			double vatAmt = 0.0;
			while (resultGameNbr.next()) {
				gameId = resultGameNbr.getInt("game_id");
				ppr = resultGameNbr.getInt("prize_payout_ratio");
				vatAmt = resultGameNbr.getDouble("vat_amt");
				pstmt = con.prepareStatement(saleQry);
				pstmt.setInt(1, gameId);
				rs = pstmt.executeQuery();
				logger.debug("--Start Sale tran for game No" + gameId);
				while (rs.next()) {
					retOrgId = rs.getInt("retailer_org_id");
					agtUserId = rs.getInt("agtUserId");
					agtOrgId = rs.getInt("agtOrgId");

					totalMrpAmt = rs.getDouble("totalMrp");
					totalRetComm = rs.getDouble("totalRetComm");
					totalAgtComm = rs.getDouble("totalagtComm");
					totalGoodCauseAmt = rs.getDouble("totalgovtComm");
					retCommRate = totalMrpAmt!=0?(totalRetComm/totalMrpAmt)*100:0;
//					agtCommRate = rs.getDouble("agt_comm");
					govtCommRate = totalMrpAmt!=0?(totalGoodCauseAmt/totalMrpAmt)*100:0;
					transDate = rs.getString("transaction_date");
					totalretNetAmt = totalMrpAmt - totalRetComm;
					totalAgtNetAmt = totalMrpAmt - totalAgtComm;

					double totalAgtVat = CommonMethods
							.calculateDrawGameVatRet(totalMrpAmt, retCommRate,
									ppr, govtCommRate, vatAmt);
					double tatalAgtTaxableSale = CommonMethods
							.calTaxableSale(totalMrpAmt, retCommRate, ppr,
									govtCommRate, vatAmt);

					if (orgIdAmountMap.containsKey(retOrgId)) {
						retDebitAmount = orgIdAmountMap.get(retOrgId)
								+ totalretNetAmt;
					} else {
						retDebitAmount = totalretNetAmt;
					}

					orgIdAmountMap.put(retOrgId, retDebitAmount);
					orgIdParentIdMap.put(retOrgId, agtOrgId);

					if (orgIdTransIdInvMap.containsKey(retOrgId)) {
						transIdInvMap = orgIdTransIdInvMap.get(retOrgId);
					} else {
						transIdInvMap = new HashMap<String, List<Long>>();
						orgIdTransIdInvMap.put(retOrgId, transIdInvMap);
					}

					// insert in main transaction table

					pstmtAgt.setString(1, "AGENT");
					pstmtAgt.executeUpdate();
					ResultSet rsTrns = pstmtAgt.getGeneratedKeys();
					if (rsTrns.next()) {
						long transId = rsTrns.getLong(1);
						logger.debug("--Retailer org id"+retOrgId+"--Agent org Id--"+agtOrgId+"--Start Sale tran for game No" + gameId);
						if (!transIdInvMap.containsKey(transDate)) {
							transIdInvMap.put(transDate,
									new ArrayList<Long>());
						}

						transIdInvMap.get(transDate).add(transId);

						pstmtAgtTrans.setLong(1, transId);
						pstmtAgtTrans.setInt(2, agtUserId);
						pstmtAgtTrans.setInt(3, agtOrgId);
						pstmtAgtTrans.setString(4, "RETAILER");
						pstmtAgtTrans.setInt(5, retOrgId);
						pstmtAgtTrans.setString(6, "DG_SALE");
						pstmtAgtTrans.setTimestamp(7,
								fetchTransTimeStamp(transDate));
						pstmtAgtTrans.executeUpdate();

						insAgtSalePstmt.setLong(1, transId);
						insAgtSalePstmt.setInt(2, agtOrgId);
						insAgtSalePstmt.setInt(3, retOrgId);
						insAgtSalePstmt.setInt(4, gameId);
						insAgtSalePstmt.setDouble(5, totalMrpAmt);
						insAgtSalePstmt.setDouble(6, totalRetComm);
						insAgtSalePstmt.setDouble(7, totalretNetAmt);
						insAgtSalePstmt.setDouble(8, totalAgtComm);
						insAgtSalePstmt.setDouble(9, totalAgtNetAmt);
						insAgtSalePstmt.setString(10, "CLAIM_BAL");
						insAgtSalePstmt.setDouble(11, totalAgtVat);
						insAgtSalePstmt.setDouble(12, tatalAgtTaxableSale);
						insAgtSalePstmt.setDouble(13, totalGoodCauseAmt);
						insAgtSalePstmt.executeUpdate();

						// update retailer table

						updateRetSalePstmt.setInt(1, gameId);
						updateRetSalePstmt.setLong(2, transId); // agent trans
						// Id
						updateRetSalePstmt.setInt(3, retOrgId);
//						updateRetSalePstmt.setDouble(4, retCommRate);
//						updateRetSalePstmt.setDouble(5, agtCommRate);
//						updateRetSalePstmt.setDouble(6, govtCommRate);
						updateRetSalePstmt.setString(4, transDate);
						updateRetSalePstmt.executeUpdate();
					}

				}

				pstmt = con.prepareStatement(saleReturnQry);
				pstmt.setInt(1, gameId);
				rs = pstmt.executeQuery();
				logger.debug("--Start Cancel tran for game No" + gameId);
				while (rs.next()) {
					retOrgId = rs.getInt("retailer_org_id");
					agtUserId = rs.getInt("agtUserId");
					agtOrgId = rs.getInt("agtOrgId");

					totalMrpAmt = rs.getDouble("totalMrp");
					totalRetComm = rs.getDouble("totalRetComm");
					totalAgtComm = rs.getDouble("totalagtComm");
					totalGoodCauseAmt = rs.getDouble("totalgovtComm");
					totalCancelCharge = rs.getDouble("totalCancelCharge");
					retCommRate = totalMrpAmt!=0?(totalRetComm/totalMrpAmt)*100:0;
//					agtCommRate = rs.getDouble("agt_comm");
					govtCommRate = totalMrpAmt!=0?(totalGoodCauseAmt/totalMrpAmt)*100:0;
					transDate = rs.getString("transaction_date");
					totalretNetAmt = totalMrpAmt - totalRetComm
							- totalCancelCharge;
					totalAgtNetAmt = totalMrpAmt - totalAgtComm
							- totalCancelCharge;
					String tranType = rs.getString("transaction_type");
					double totalAgtVat = CommonMethods
							.calculateDrawGameVatRet(totalMrpAmt, retCommRate,
									ppr, govtCommRate, vatAmt);
					double tatalAgtTaxableSale = CommonMethods
							.calTaxableSale(totalMrpAmt, retCommRate, ppr,
									govtCommRate, vatAmt);

					if (orgIdAmountMap.containsKey(retOrgId)) {
						retDebitAmount = orgIdAmountMap.get(retOrgId)
								- totalretNetAmt;
					} else {
						retDebitAmount = -totalretNetAmt;
					}

					orgIdAmountMap.put(retOrgId, retDebitAmount);
					orgIdParentIdMap.put(retOrgId, agtOrgId);

					if (orgIdTransIdInvRetMap.containsKey(retOrgId)) {
						transIdInvRetMap = orgIdTransIdInvRetMap.get(retOrgId);
					} else {
						transIdInvRetMap = new HashMap<String, List<Long>>();
						orgIdTransIdInvRetMap.put(retOrgId, transIdInvRetMap);
					}

					// insert in main transaction table

					pstmtAgt.setString(1, "AGENT");
					pstmtAgt.executeUpdate();
					ResultSet rsTrns = pstmtAgt.getGeneratedKeys();
					if (rsTrns.next()) {
						long transId = rsTrns.getLong(1);
						logger.debug("--Retailer org id"+retOrgId+"--Agent org Id--"+agtOrgId+"--Start Sale Cancel tran for game No" + gameId);
						if (!transIdInvRetMap.containsKey(transDate)) {
							transIdInvRetMap.put(transDate,
									new ArrayList<Long>());
						}

						transIdInvRetMap.get(transDate).add(transId);

						pstmtAgtTrans.setLong(1, transId);
						pstmtAgtTrans.setInt(2, agtUserId);
						pstmtAgtTrans.setInt(3, agtOrgId);
						pstmtAgtTrans.setString(4, "RETAILER");
						pstmtAgtTrans.setInt(5, retOrgId);
						pstmtAgtTrans.setString(6, tranType);
						pstmtAgtTrans.setTimestamp(7,
								fetchTransTimeStamp(transDate));
						pstmtAgtTrans.executeUpdate();

						insAgtSaleRetPstmt.setLong(1, transId);
						insAgtSaleRetPstmt.setInt(2, agtOrgId);
						insAgtSaleRetPstmt.setInt(3, retOrgId);
						insAgtSaleRetPstmt.setInt(4, gameId);
						insAgtSaleRetPstmt.setDouble(5, totalMrpAmt);
						insAgtSaleRetPstmt.setDouble(6, totalRetComm);
						insAgtSaleRetPstmt.setDouble(7, totalretNetAmt);
						insAgtSaleRetPstmt.setDouble(8, totalAgtComm);
						insAgtSaleRetPstmt.setDouble(9, totalAgtNetAmt);
						insAgtSaleRetPstmt.setString(10, "CLAIM_BAL");
						insAgtSaleRetPstmt.setDouble(11, totalAgtVat);
						insAgtSaleRetPstmt.setDouble(12, tatalAgtTaxableSale);
						insAgtSaleRetPstmt.setDouble(13, totalGoodCauseAmt);
						insAgtSaleRetPstmt.setDouble(14, totalCancelCharge);
						insAgtSaleRetPstmt.executeUpdate();

						// update retailer table
						updateRetSaleRetPstmt.setInt(1, gameId);
						updateRetSaleRetPstmt.setLong(2, transId); // agent
						// trans Id
						updateRetSaleRetPstmt.setInt(3, retOrgId);
//						updateRetSaleRetPstmt.setDouble(4, retCommRate);
//						updateRetSaleRetPstmt.setDouble(5, agtCommRate);
//						updateRetSaleRetPstmt.setDouble(6, govtCommRate);
						updateRetSaleRetPstmt.setString(4, transDate);
						updateRetSaleRetPstmt.setString(5, tranType);
						updateRetSaleRetPstmt.executeUpdate();
					}

				}
			}

			generateDGReceiptNew(orgIdTransIdInvMap, orgIdParentIdMap,
					"DG_INVOICE", "DG_RECEIPT", con);
			generateDGReceiptNew(orgIdTransIdInvRetMap, orgIdParentIdMap,
					"CR_NOTE", "CR_NOTE_CASH", con);

			logger.debug("retailer orgIdAmountMap\n" + orgIdAmountMap);
			// update retailer balance
			Set<Integer> retOrgIdSet = orgIdAmountMap.keySet();
			for (Integer orgId : retOrgIdSet) {
				boolean isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(orgIdAmountMap.get(orgId), "TRANSACTION", "DRAW_GAME_SALE", orgId,
						orgIdParentIdMap.get(orgId), "RETAILER", 0, con);
				if(!isValid)
					throw new LMSException();
					
				OrgCreditUpdation.updateOrganizationBalWithValidate(orgIdAmountMap.get(orgId), "CLAIM_BAL", "DEBIT", orgId,
						orgIdParentIdMap.get(orgId), "RETAILER", 0, con);
				
				/*OrgCreditUpdation.updateCreditLimitForRetailer(orgId,
						"DRAW_GAME_SALE", orgIdAmountMap.get(orgId), con);
				updateOrgBalance("CLAIM_BAL", orgIdAmountMap.get(orgId), orgId,
						"DEBIT", con);*/
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("error in sale retailer");
		}
		logger.debug(new Date() + "---end-dg sale update for retailer--"
				+ new Date().getTime());
	}
	
	public void updateDGSaleLedgerForAgt(Connection con) throws LMSException {
		logger.debug(new Date() + "---start-dg sale update for agent--"
				+ new Date().getTime());
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		double totalMrpAmt, totalAgtComm, totalGoodCauseAmt, totalAgtNetAmt, totalCancelCharge;
		double agtCommRate, govtCommRate, agtDebitAmount;
		String transDate;
		int agtOrgId, boUserId, boOrgId;
		Map<Integer, Double> orgIdAmountMap = new HashMap<Integer, Double>();
		Map<Integer, Integer> orgIdParentIdMap = new HashMap<Integer, Integer>();
		Map<Integer, Map<String, List<Long>>> orgIdTransIdInvMap = new HashMap<Integer, Map<String, List<Long>>>();
		Map<String, List<Long>> transIdInvMap = null;
		Map<Integer, Map<String, List<Long>>> orgIdTransIdInvRetMap = new HashMap<Integer, Map<String, List<Long>>>();
		Map<String, List<Long>> transIdInvRetMap = null;
		Statement tempStmt=null;
		try {
			String saleQry = "select agent_org_id,boUserId,boOrgId,totalMrp,totalRetComm,totalagtComm,totalgovtComm,totalVat,totalTaxSale,tot.game_id,transaction_date,ret_comm,agt_comm,tot.govt_comm,prize_payout_ratio,vat_amt from (select agent_org_id,boUserId,boOrgId,totalMrp,totalRetComm,totalagtComm,totalgovtComm,totalVat,totalTaxSale,game_id,transaction_date,ret_comm,agt_comm,govt_comm from(select a.agent_org_id,sum(mrp_amt) totalMrp ,sum(retailer_comm) totalRetComm,sum(agent_comm) totalagtComm, sum(a.govt_comm) totalgovtComm, sum(vat_amt) totalVat,sum(taxable_sale) totalTaxSale,a.game_id,DATE(transaction_date) 'transaction_date',ret_comm,agt_comm,c.govt_comm from st_dg_agt_sale a inner join  st_lms_agent_transaction_master b inner join st_temp_update_ledger c on a.transaction_id = b.transaction_id and a.transaction_id=trans_id where claim_status='CLAIM_BAL' group by a.agent_org_id,DATE(transaction_date),a.game_id,agt_comm,c.govt_comm  order by agent_org_id,game_id,transaction_date) sale inner join (select um.user_id agtUserId,um.organization_id agtOrgId,um.parent_user_id boUserId,om.parent_id boOrgId,isrolehead from st_lms_organization_master om inner join st_lms_user_master um on om.organization_id=um.organization_id where om.organization_type='AGENT') om on agent_org_id=agtOrgId and isrolehead = 'Y') tot inner join st_dg_game_master gm on tot.game_id=gm.game_id";
			String saleReturnQry = "select agent_org_id,boUserId,boOrgId,totalMrp,totalRetComm,totalagtComm,totalgovtComm,totalVat,totalTaxSale,totalCancelCharge,tot.game_id,transaction_date,ret_comm,agt_comm,tot.govt_comm,transaction_type,prize_payout_ratio,vat_amt from(select agent_org_id,boUserId,boOrgId,totalMrp,totalRetComm,totalagtComm,totalgovtComm,totalVat,totalTaxSale,totalCancelCharge,game_id,transaction_date,ret_comm,agt_comm,govt_comm,transaction_type from(select a.agent_org_id,sum(mrp_amt) totalMrp ,sum(retailer_comm) totalRetComm,sum(agent_comm) totalagtComm, sum(a.govt_comm) totalgovtComm, sum(vat_amt) totalVat,sum(taxable_sale) totalTaxSale,sum(cancellation_charges) totalCancelCharge,a.game_id,DATE(transaction_date) 'transaction_date',ret_comm,agt_comm,c.govt_comm,transaction_type from st_dg_agt_sale_refund a inner join  st_lms_agent_transaction_master b inner join st_temp_update_ledger c on a.transaction_id = b.transaction_id and a.transaction_id=trans_id where claim_status='CLAIM_BAL' group by a.agent_org_id,DATE(transaction_date),a.game_id,transaction_type,agt_comm,c.govt_comm  order by agent_org_id,game_id,transaction_date) saleRefund inner join (select um.user_id agtUserId,um.organization_id agtOrgId,um.parent_user_id boUserId,om.parent_id boOrgId,isrolehead from st_lms_organization_master om inner join st_lms_user_master um on om.organization_id=um.organization_id where om.organization_type='AGENT') d on agent_org_id=agtOrgId and isrolehead = 'Y') tot inner join st_dg_game_master gm on tot.game_id=gm.game_id";

			PreparedStatement pstmtBO = con.prepareStatement(QueryManager.insertInLMSTransactionMaster());
			PreparedStatement insBoTranPstmt = con.prepareStatement(QueryManager.insertInBOTransactionMaster());
			PreparedStatement insBoSalePstmt = con.prepareStatement("insert into st_dg_bo_sale(transaction_id,agent_org_id,game_id,mrp_amt,agent_comm,net_amt,vat_amt,taxable_sale,govt_comm) values(?,?,?,?,?,?,?,?,?)");
			
			//PreparedStatement updAgtSalepstmt = con.prepareStatement("update st_dg_agt_sale a inner join (select transaction_date,transaction_id,agt_comm,govt_comm from st_lms_agent_transaction_master a inner join st_temp_update_ledger b on a.transaction_id = b.trans_id) b on a.transaction_id = b.transaction_id set claim_status='DONE_CLAIM',bo_ref_transaction_id=? where a.agent_org_id=? and b.agt_comm=? and b.govt_comm=? and DATE(transaction_date)=? and game_id=?");
			
			PreparedStatement insTempTablePstmt=con.prepareStatement("insert into agentTransSummary(bo_ref_transaction_id,agt_comm,govt_comm,agent_org_id,game_id,trans_date) values(?,?,?,?,?,?)");

			PreparedStatement insBoSaleRetpstmt = con.prepareStatement("insert into st_dg_bo_sale_refund(transaction_id,agent_org_id,game_id,mrp_amt,agent_comm,net_amt,vat_amt,taxable_sale,govt_comm,cancellation_charges) values(?,?,?,?,?,?,?,?,?,?)");
			//PreparedStatement updAgtSaleRetPstmt = con.prepareStatement("update st_dg_agt_sale_refund a inner join (select transaction_date,transaction_type,transaction_id,agt_comm,govt_comm from st_lms_agent_transaction_master a inner join st_temp_update_ledger b on a.transaction_id = b.trans_id) b on a.transaction_id = b.transaction_id set claim_status='DONE_CLAIM',bo_ref_transaction_id=? where a.agent_org_id=? and b.agt_comm=? and b.govt_comm=? and DATE(transaction_date)=? and transaction_type=? and game_id=?");

			int gameId = 0;
			double ppr = 0.0;
			double vatAmt = 0.0;
			final int batchSize = 500;
			int count = 0;
			tempStmt=con.createStatement();
			tempStmt.execute("delete from  agentTransSummary ");
			pstmt = con.prepareStatement(saleQry);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				agtOrgId = rs.getInt("agent_org_id");
				boUserId = rs.getInt("boUserId");
				boOrgId = rs.getInt("boOrgId");
				gameId = rs.getInt("game_id");
				logger.debug("--Agent Org Id--"+agtOrgId+"--Agent Sale tran for game No" + gameId);
				totalMrpAmt = rs.getDouble("totalMrp");
				totalAgtComm = rs.getDouble("totalagtComm");
				totalGoodCauseAmt = rs.getDouble("totalgovtComm");
				agtCommRate = rs.getDouble("agt_comm");
				govtCommRate = rs.getDouble("govt_comm");
				transDate = rs.getString("transaction_date");
				vatAmt = rs.getDouble("vat_amt");
				ppr = rs.getDouble("prize_payout_ratio");
				totalAgtNetAmt = totalMrpAmt - totalAgtComm;
				double totalAgtVat =rs.getDouble("totalVat");
				/*double totalAgtVat = CommonMethods.calculateDrawGameVatAgt(
						totalMrpAmt, agtCommRate, ppr, govtCommRate, vatAmt);*/
				double tatalAgtTaxableSale = CommonMethods.calTaxableSale(
						totalMrpAmt, agtCommRate, ppr, govtCommRate, vatAmt);

				if (orgIdAmountMap.containsKey(agtOrgId)) {
					agtDebitAmount = orgIdAmountMap.get(agtOrgId)
							+ totalAgtNetAmt;
				} else {
					agtDebitAmount = totalAgtNetAmt;
				}

				orgIdAmountMap.put(agtOrgId, agtDebitAmount);
				orgIdParentIdMap.put(agtOrgId, boOrgId);

				if (orgIdTransIdInvMap.containsKey(agtOrgId)) {
					transIdInvMap = orgIdTransIdInvMap.get(agtOrgId);
				} else {
					transIdInvMap = new HashMap<String, List<Long>>();
					orgIdTransIdInvMap.put(agtOrgId, transIdInvMap);
				}

				// insert in main transaction table

				pstmtBO.setString(1, "BO");
				pstmtBO.executeUpdate();
				ResultSet rsTrns = pstmtBO.getGeneratedKeys();
				if (rsTrns.next()) {
					long transId = rsTrns.getLong(1);

					if (!transIdInvMap.containsKey(transDate)) {
						transIdInvMap.put(transDate, new ArrayList<Long>());
					}

					transIdInvMap.get(transDate).add(transId);

					insBoTranPstmt.setLong(1, transId);
					insBoTranPstmt.setInt(2, boUserId);
					insBoTranPstmt.setInt(3, boOrgId);
					insBoTranPstmt.setString(4, "AGENT");
					insBoTranPstmt.setInt(5, agtOrgId);
					insBoTranPstmt.setTimestamp(6,
							fetchTransTimeStamp(transDate));
					insBoTranPstmt.setString(7, "DG_SALE");
					insBoTranPstmt.executeUpdate();

					insBoSalePstmt.setLong(1, transId);
					insBoSalePstmt.setInt(2, agtOrgId);
					insBoSalePstmt.setInt(3, gameId);
					insBoSalePstmt.setDouble(4, totalMrpAmt);
					insBoSalePstmt.setDouble(5, totalAgtComm);
					insBoSalePstmt.setDouble(6, totalAgtNetAmt);
					insBoSalePstmt.setDouble(7, totalAgtVat);
					insBoSalePstmt.setDouble(8, tatalAgtTaxableSale);
					insBoSalePstmt.setDouble(9, totalGoodCauseAmt);
					insBoSalePstmt.executeUpdate();

					
					//insert temp table
					insTempTablePstmt.setLong(1, transId);
					insTempTablePstmt.setDouble(2, agtCommRate);
					insTempTablePstmt.setDouble(3, govtCommRate);
					insTempTablePstmt.setInt(4, agtOrgId);
					insTempTablePstmt.setInt(5, gameId);
					insTempTablePstmt.setString(6, transDate);
					insTempTablePstmt.addBatch();
					if(++count % batchSize == 0) {
						insTempTablePstmt.executeBatch();
				    }
					
					
					/*// update retailer table

					updAgtSalepstmt.setLong(1, transId); // agent trans Id
					updAgtSalepstmt.setInt(2, agtOrgId);
					updAgtSalepstmt.setDouble(3, agtCommRate);
					updAgtSalepstmt.setDouble(4, govtCommRate);
					updAgtSalepstmt.setString(5, transDate);
					updAgtSalepstmt.setInt(6, gameId);
					updAgtSalepstmt.executeUpdate();*/
				}

			}

			insTempTablePstmt.executeBatch();
			
			
			tempStmt.executeUpdate("update st_dg_agt_sale  sale inner join (select yy.transaction_id,xx.bo_ref_transaction_id,game_id from agentTransSummary xx inner join (select transaction_id,transaction_date,user_org_id agent_org_id,agt_comm,govt_comm from st_lms_agent_transaction_master a inner join st_temp_update_ledger b on a.transaction_id = b.trans_id)yy on DATE(trans_date)=DATE(transaction_date) and xx.agent_org_id=yy.agent_org_id and  xx.agt_comm=yy.agt_comm and xx.govt_comm=yy.govt_comm ) tlb on sale.transaction_id=tlb.transaction_id and  sale.game_id=tlb.game_id set sale.claim_status='DONE_CLAIM',sale.bo_ref_transaction_id=tlb.bo_ref_transaction_id");
			tempStmt.execute("delete from  agentTransSummary ");
		
			
			pstmt = con.prepareStatement(saleReturnQry);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				agtOrgId = rs.getInt("agent_org_id");
				boUserId = rs.getInt("boUserId");
				boOrgId = rs.getInt("boOrgId");
				gameId = rs.getInt("game_id");
				logger.debug("--Agent Org Id--"+agtOrgId+"--Agent Sale Cancel tran for game No" + gameId);
				totalMrpAmt = rs.getDouble("totalMrp");
				totalAgtComm = rs.getDouble("totalagtComm");
				totalGoodCauseAmt = rs.getDouble("totalgovtComm");
				totalCancelCharge = rs.getDouble("totalCancelCharge");
				agtCommRate = rs.getDouble("agt_comm");
				govtCommRate = rs.getDouble("govt_comm");
				transDate = rs.getString("transaction_date");
				vatAmt = rs.getDouble("vat_amt");
				ppr = rs.getDouble("prize_payout_ratio");
				totalAgtNetAmt = totalMrpAmt - totalAgtComm - totalCancelCharge;
				String tranType = rs.getString("transaction_type");
				double totalAgtVat =rs.getDouble("totalVat");
				/*double totalAgtVat = CommonMethods.calculateDrawGameVatAgt(
						totalMrpAmt, agtCommRate, ppr, govtCommRate, vatAmt);*/
				double tatalAgtTaxableSale = CommonMethods.calTaxableSale(
						totalMrpAmt, agtCommRate, ppr, govtCommRate, vatAmt);

				if (orgIdAmountMap.containsKey(agtOrgId)) {
					agtDebitAmount = orgIdAmountMap.get(agtOrgId)
							- totalAgtNetAmt;
				} else {
					agtDebitAmount = -totalAgtNetAmt;
				}

				orgIdAmountMap.put(agtOrgId, agtDebitAmount);
				orgIdParentIdMap.put(agtOrgId, boOrgId);

				if (orgIdTransIdInvRetMap.containsKey(agtOrgId)) {
					transIdInvRetMap = orgIdTransIdInvRetMap.get(agtOrgId);
				} else {
					transIdInvRetMap = new HashMap<String, List<Long>>();
					orgIdTransIdInvRetMap.put(agtOrgId, transIdInvRetMap);
				}

				// insert in main transaction table

				pstmtBO.setString(1, "BO");
				pstmtBO.executeUpdate();
				ResultSet rsTrns = pstmtBO.getGeneratedKeys();
				if (rsTrns.next()) {
					long transId = rsTrns.getLong(1);

					if (!transIdInvRetMap.containsKey(transDate)) {
						transIdInvRetMap.put(transDate,
								new ArrayList<Long>());
					}

					transIdInvRetMap.get(transDate).add(transId);

					insBoTranPstmt.setLong(1, transId);
					insBoTranPstmt.setInt(2, boUserId);
					insBoTranPstmt.setInt(3, boOrgId);
					insBoTranPstmt.setString(4, "AGENT");
					insBoTranPstmt.setInt(5, agtOrgId);
					insBoTranPstmt.setTimestamp(6,
							fetchTransTimeStamp(transDate));
					insBoTranPstmt.setString(7, tranType);
					insBoTranPstmt.executeUpdate();

					insBoSaleRetpstmt.setLong(1, transId);
					insBoSaleRetpstmt.setInt(2, agtOrgId);
					insBoSaleRetpstmt.setInt(3, gameId);
					insBoSaleRetpstmt.setDouble(4, totalMrpAmt);
					insBoSaleRetpstmt.setDouble(5, totalAgtComm);
					insBoSaleRetpstmt.setDouble(6, totalAgtNetAmt);
					insBoSaleRetpstmt.setDouble(7, totalAgtVat);
					insBoSaleRetpstmt.setDouble(8, tatalAgtTaxableSale);
					insBoSaleRetpstmt.setDouble(9, totalGoodCauseAmt);
					insBoSaleRetpstmt.setDouble(10, totalCancelCharge);
					insBoSaleRetpstmt.executeUpdate();

					//insert temp table
					insTempTablePstmt.setLong(1, transId);
					insTempTablePstmt.setDouble(2, agtCommRate);
					insTempTablePstmt.setDouble(3, govtCommRate);
					insTempTablePstmt.setInt(4, agtOrgId);
					insTempTablePstmt.setInt(5, gameId);
					insTempTablePstmt.setString(6, transDate);
					insTempTablePstmt.addBatch();
					if(++count % batchSize == 0) {
						insTempTablePstmt.executeBatch();
				    }
					
					
					/*// update retailer table

					updAgtSaleRetPstmt.setLong(1, transId); // agent trans Id
					updAgtSaleRetPstmt.setInt(2, agtOrgId);
					updAgtSaleRetPstmt.setDouble(3, agtCommRate);
					updAgtSaleRetPstmt.setDouble(4, govtCommRate);
					updAgtSaleRetPstmt.setString(5, transDate);
					updAgtSaleRetPstmt.setString(6, tranType);
					updAgtSaleRetPstmt.setInt(7, gameId);
					updAgtSaleRetPstmt.executeUpdate();*/
				}

			}
			insTempTablePstmt.executeBatch();
			
			
			tempStmt.executeUpdate("update st_dg_agt_sale_refund  sale inner join (select yy.transaction_id,xx.bo_ref_transaction_id,game_id from agentTransSummary xx inner join (select transaction_id,transaction_date,user_org_id agent_org_id,agt_comm,govt_comm from st_lms_agent_transaction_master a inner join st_temp_update_ledger b on a.transaction_id = b.trans_id)yy on DATE(trans_date)=DATE(transaction_date) and xx.agent_org_id=yy.agent_org_id and  xx.agt_comm=yy.agt_comm and xx.govt_comm=yy.govt_comm ) tlb on sale.transaction_id=tlb.transaction_id and  sale.game_id=tlb.game_id set sale.claim_status='DONE_CLAIM',sale.bo_ref_transaction_id=tlb.bo_ref_transaction_id");
			tempStmt.execute("delete from  agentTransSummary ");
		
			generateDGReceiptForBONew(orgIdTransIdInvMap, orgIdParentIdMap,
					"DG_INVOICE", "DG_RECEIPT", con);
			generateDGReceiptForBONew(orgIdTransIdInvRetMap, orgIdParentIdMap,
					"CR_NOTE", "CR_NOTE_CASH", con);

			logger.debug("agent orgIdAmountMap\n" + orgIdAmountMap);
			// update agent balance
			Set<Integer> agtOrgIdSet = orgIdAmountMap.keySet();
			for (Integer orgId : agtOrgIdSet) {
				boolean isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(CommonMethods.fmtToTwoDecimal(orgIdAmountMap.get(orgId)), "TRANSACTION", "DRAW_GAME_SALE", orgId,
						0, "AGENT", 0, con);
				if(!isValid)
					throw new LMSException();
				OrgCreditUpdation.updateOrganizationBalWithValidate(CommonMethods.fmtToTwoDecimal(orgIdAmountMap.get(orgId)), "CLAIM_BAL", "DEBIT", orgId,
						0, "AGENT", 0, con);
				
				/*OrgCreditUpdation.updateCreditLimitForAgent(orgId,
						"DRAW_GAME_SALE", orgIdAmountMap.get(orgId), con);
				updateOrgBalance("CLAIM_BAL", orgIdAmountMap.get(orgId), orgId,
						"DEBIT", con);*/
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("error in sale agent");
		}
		logger.debug(new Date() + "---end-dg sale update for agent--"
				+ new Date().getTime());
	}
	
	
	public void updateDGSaleLedgerForAgtwithoutComm(Connection con) throws LMSException {
		logger.debug(new Date() + "---start-dg sale update for agent--"
				+ new Date().getTime());
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		double totalMrpAmt, totalAgtComm, totalGoodCauseAmt, totalAgtNetAmt, totalCancelCharge;
		double agtCommRate, govtCommRate, agtDebitAmount;
		String transDate;
		int agtOrgId, boUserId, boOrgId;
		Map<Integer, Double> orgIdAmountMap = new HashMap<Integer, Double>();
		Map<Integer, Integer> orgIdParentIdMap = new HashMap<Integer, Integer>();
		Map<Integer, Map<String, List<Long>>> orgIdTransIdInvMap = new HashMap<Integer, Map<String, List<Long>>>();
		Map<String, List<Long>> transIdInvMap = null;
		Map<Integer, Map<String, List<Long>>> orgIdTransIdInvRetMap = new HashMap<Integer, Map<String, List<Long>>>();
		Map<String, List<Long>> transIdInvRetMap = null;
		try {
			String saleQry = "select agent_org_id,boUserId,boOrgId,totalMrp,totalRetComm,totalagtComm,totalgovtComm,totalVat,totalTaxSale,game_id,transaction_date from(select a.agent_org_id,sum(mrp_amt) totalMrp ,sum(retailer_comm) totalRetComm,sum(agent_comm) totalagtComm, sum(a.govt_comm) totalgovtComm, sum(vat_amt) totalVat,sum(taxable_sale) totalTaxSale,a.game_id,DATE(transaction_date) 'transaction_date' from st_dg_agt_sale a inner join  st_lms_agent_transaction_master b on a.transaction_id = b.transaction_id where claim_status='CLAIM_BAL' group by a.agent_org_id,DATE(transaction_date),a.game_id order by agent_org_id,game_id,transaction_date) sale inner join (select um.user_id agtUserId,um.organization_id agtOrgId,um.parent_user_id boUserId,om.parent_id boOrgId,isrolehead from st_lms_organization_master om inner join st_lms_user_master um on om.organization_id=um.organization_id where om.organization_type='AGENT') om on agent_org_id=agtOrgId and isrolehead = 'Y'";
			String saleReturnQry = "select agent_org_id,boUserId,boOrgId,totalMrp,totalRetComm,totalagtComm,totalgovtComm,totalVat,totalTaxSale,totalCancelCharge,game_id,transaction_date,transaction_type from(select a.agent_org_id,sum(mrp_amt) totalMrp ,sum(retailer_comm) totalRetComm,sum(agent_comm) totalagtComm, sum(a.govt_comm) totalgovtComm, sum(vat_amt) totalVat,sum(taxable_sale) totalTaxSale,sum(cancellation_charges) totalCancelCharge,a.game_id,DATE(transaction_date) 'transaction_date',transaction_type from st_dg_agt_sale_refund a inner join  st_lms_agent_transaction_master b on a.transaction_id = b.transaction_id where claim_status='CLAIM_BAL' group by a.agent_org_id,DATE(transaction_date),a.game_id,transaction_type  order by agent_org_id,game_id,transaction_date) saleRefund inner join (select um.user_id agtUserId,um.organization_id agtOrgId,um.parent_user_id boUserId,om.parent_id boOrgId,isrolehead from st_lms_organization_master om inner join st_lms_user_master um on om.organization_id=um.organization_id where om.organization_type='AGENT') d on agent_org_id=agtOrgId and isrolehead = 'Y'";

			PreparedStatement pstmtBO = con.prepareStatement(QueryManager
					.insertInLMSTransactionMaster());
			PreparedStatement insBoTranPstmt = con
					.prepareStatement(QueryManager
							.insertInBOTransactionMaster());
			PreparedStatement insBoSalePstmt = con
					.prepareStatement("insert into st_dg_bo_sale(transaction_id,agent_org_id,game_id,mrp_amt,agent_comm,net_amt,vat_amt,taxable_sale,govt_comm) values(?,?,?,?,?,?,?,?,?)");
			PreparedStatement updAgtSalepstmt = con
					.prepareStatement("update st_dg_agt_sale a inner join st_lms_agent_transaction_master b on a.transaction_id = b.transaction_id set claim_status='DONE_CLAIM',bo_ref_transaction_id=? where a.agent_org_id=? and DATE(transaction_date)=? and game_id=?");
			PreparedStatement insBoSaleRetpstmt = con
					.prepareStatement("insert into st_dg_bo_sale_refund(transaction_id,agent_org_id,game_id,mrp_amt,agent_comm,net_amt,vat_amt,taxable_sale,govt_comm,cancellation_charges) values(?,?,?,?,?,?,?,?,?,?)");
			PreparedStatement updAgtSaleRetPstmt = con
					.prepareStatement("update st_dg_agt_sale_refund a inner join st_lms_agent_transaction_master b on a.transaction_id = b.transaction_id set claim_status='DONE_CLAIM',bo_ref_transaction_id=? where a.agent_org_id=? and DATE(transaction_date)=? and transaction_type=? and game_id=?");

			int gameId = 0;
			double ppr = 0.0;
			double vatAmt = 0.0;

			pstmt = con.prepareStatement(saleQry);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				agtOrgId = rs.getInt("agent_org_id");
				boUserId = rs.getInt("boUserId");
				boOrgId = rs.getInt("boOrgId");
				gameId = rs.getInt("game_id");
				logger.debug("--Agent Org Id--"+agtOrgId+"--Agent Sale tran for game No" + gameId);
				totalMrpAmt = rs.getDouble("totalMrp");
				totalAgtComm = rs.getDouble("totalagtComm");
				totalGoodCauseAmt = rs.getDouble("totalgovtComm");
				agtCommRate = totalMrpAmt!=0?(totalAgtComm/totalMrpAmt)*100:0;
				govtCommRate = totalMrpAmt!=0?(totalGoodCauseAmt/totalMrpAmt)*100:0;
				transDate = rs.getString("transaction_date");

				totalAgtNetAmt = totalMrpAmt - totalAgtComm;

				double totalAgtVat = CommonMethods.calculateDrawGameVatAgt(
						totalMrpAmt, agtCommRate, ppr, govtCommRate, vatAmt);
				double tatalAgtTaxableSale = CommonMethods.calTaxableSale(
						totalMrpAmt, agtCommRate, ppr, govtCommRate, vatAmt);

				if (orgIdAmountMap.containsKey(agtOrgId)) {
					agtDebitAmount = orgIdAmountMap.get(agtOrgId)
							+ totalAgtNetAmt;
				} else {
					agtDebitAmount = totalAgtNetAmt;
				}

				orgIdAmountMap.put(agtOrgId, agtDebitAmount);
				orgIdParentIdMap.put(agtOrgId, boOrgId);

				if (orgIdTransIdInvMap.containsKey(agtOrgId)) {
					transIdInvMap = orgIdTransIdInvMap.get(agtOrgId);
				} else {
					transIdInvMap = new HashMap<String, List<Long>>();
					orgIdTransIdInvMap.put(agtOrgId, transIdInvMap);
				}

				// insert in main transaction table

				pstmtBO.setString(1, "BO");
				pstmtBO.executeUpdate();
				ResultSet rsTrns = pstmtBO.getGeneratedKeys();
				if (rsTrns.next()) {
					long transId = rsTrns.getLong(1);

					if (!transIdInvMap.containsKey(transDate)) {
						transIdInvMap.put(transDate, new ArrayList<Long>());
					}

					transIdInvMap.get(transDate).add(transId);

					insBoTranPstmt.setLong(1, transId);
					insBoTranPstmt.setInt(2, boUserId);
					insBoTranPstmt.setInt(3, boOrgId);
					insBoTranPstmt.setString(4, "AGENT");
					insBoTranPstmt.setInt(5, agtOrgId);
					insBoTranPstmt.setTimestamp(6,
							fetchTransTimeStamp(transDate));
					insBoTranPstmt.setString(7, "DG_SALE");
					insBoTranPstmt.executeUpdate();

					insBoSalePstmt.setLong(1, transId);
					insBoSalePstmt.setInt(2, agtOrgId);
					insBoSalePstmt.setInt(3, gameId);
					insBoSalePstmt.setDouble(4, totalMrpAmt);
					insBoSalePstmt.setDouble(5, totalAgtComm);
					insBoSalePstmt.setDouble(6, totalAgtNetAmt);
					insBoSalePstmt.setDouble(7, totalAgtVat);
					insBoSalePstmt.setDouble(8, tatalAgtTaxableSale);
					insBoSalePstmt.setDouble(9, totalGoodCauseAmt);
					insBoSalePstmt.executeUpdate();

					// update retailer table

					updAgtSalepstmt.setLong(1, transId); // agent trans Id
					updAgtSalepstmt.setInt(2, agtOrgId);
//					updAgtSalepstmt.setDouble(3, agtCommRate);
//					updAgtSalepstmt.setDouble(4, govtCommRate);
					updAgtSalepstmt.setString(3, transDate);
					updAgtSalepstmt.setInt(4, gameId);
					updAgtSalepstmt.executeUpdate();
				}

			}

			pstmt = con.prepareStatement(saleReturnQry);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				agtOrgId = rs.getInt("agent_org_id");
				boUserId = rs.getInt("boUserId");
				boOrgId = rs.getInt("boOrgId");
				gameId = rs.getInt("game_id");
				logger.debug("--Agent Org Id--"+agtOrgId+"--Agent Sale Cancel tran for game No" + gameId);
				totalMrpAmt = rs.getDouble("totalMrp");
				totalAgtComm = rs.getDouble("totalagtComm");
				totalGoodCauseAmt = rs.getDouble("totalgovtComm");
				totalCancelCharge = rs.getDouble("totalCancelCharge");
				agtCommRate = totalMrpAmt!=0?(totalAgtComm/totalMrpAmt)*100:0;
				govtCommRate = totalMrpAmt!=0?(totalGoodCauseAmt/totalMrpAmt)*100:0;
				transDate = rs.getString("transaction_date");

				totalAgtNetAmt = totalMrpAmt - totalAgtComm - totalCancelCharge;
				String tranType = rs.getString("transaction_type");

				double totalAgtVat = CommonMethods.calculateDrawGameVatAgt(
						totalMrpAmt, agtCommRate, ppr, govtCommRate, vatAmt);
				double tatalAgtTaxableSale = CommonMethods.calTaxableSale(
						totalMrpAmt, agtCommRate, ppr, govtCommRate, vatAmt);

				if (orgIdAmountMap.containsKey(agtOrgId)) {
					agtDebitAmount = orgIdAmountMap.get(agtOrgId)
							- totalAgtNetAmt;
				} else {
					agtDebitAmount = -totalAgtNetAmt;
				}

				orgIdAmountMap.put(agtOrgId, agtDebitAmount);
				orgIdParentIdMap.put(agtOrgId, boOrgId);

				if (orgIdTransIdInvRetMap.containsKey(agtOrgId)) {
					transIdInvRetMap = orgIdTransIdInvRetMap.get(agtOrgId);
				} else {
					transIdInvRetMap = new HashMap<String, List<Long>>();
					orgIdTransIdInvRetMap.put(agtOrgId, transIdInvRetMap);
				}

				// insert in main transaction table

				pstmtBO.setString(1, "BO");
				pstmtBO.executeUpdate();
				ResultSet rsTrns = pstmtBO.getGeneratedKeys();
				if (rsTrns.next()) {
					long transId = rsTrns.getLong(1);

					if (!transIdInvRetMap.containsKey(transDate)) {
						transIdInvRetMap.put(transDate,
								new ArrayList<Long>());
					}

					transIdInvRetMap.get(transDate).add(transId);

					insBoTranPstmt.setLong(1, transId);
					insBoTranPstmt.setInt(2, boUserId);
					insBoTranPstmt.setInt(3, boOrgId);
					insBoTranPstmt.setString(4, "AGENT");
					insBoTranPstmt.setInt(5, agtOrgId);
					insBoTranPstmt.setTimestamp(6,
							fetchTransTimeStamp(transDate));
					insBoTranPstmt.setString(7, tranType);
					insBoTranPstmt.executeUpdate();

					insBoSaleRetpstmt.setLong(1, transId);
					insBoSaleRetpstmt.setInt(2, agtOrgId);
					insBoSaleRetpstmt.setInt(3, gameId);
					insBoSaleRetpstmt.setDouble(4, totalMrpAmt);
					insBoSaleRetpstmt.setDouble(5, totalAgtComm);
					insBoSaleRetpstmt.setDouble(6, totalAgtNetAmt);
					insBoSaleRetpstmt.setDouble(7, totalAgtVat);
					insBoSaleRetpstmt.setDouble(8, tatalAgtTaxableSale);
					insBoSaleRetpstmt.setDouble(9, totalGoodCauseAmt);
					insBoSaleRetpstmt.setDouble(10, totalCancelCharge);
					insBoSaleRetpstmt.executeUpdate();

					// update retailer table

					updAgtSaleRetPstmt.setLong(1, transId); // agent trans Id
					updAgtSaleRetPstmt.setInt(2, agtOrgId);
//					updAgtSaleRetPstmt.setDouble(3, agtCommRate);
//					updAgtSaleRetPstmt.setDouble(4, govtCommRate);
					updAgtSaleRetPstmt.setString(3, transDate);
					updAgtSaleRetPstmt.setString(4, tranType);
					updAgtSaleRetPstmt.setInt(5, gameId);
					updAgtSaleRetPstmt.executeUpdate();
				}

			}

			generateDGReceiptForBONew(orgIdTransIdInvMap, orgIdParentIdMap,
					"DG_INVOICE", "DG_RECEIPT", con);
			generateDGReceiptForBONew(orgIdTransIdInvRetMap, orgIdParentIdMap,
					"CR_NOTE", "CR_NOTE_CASH", con);

			logger.debug("agent orgIdAmountMap\n" + orgIdAmountMap);
			// update agent balance
			Set<Integer> agtOrgIdSet = orgIdAmountMap.keySet();
			for (Integer orgId : agtOrgIdSet) {
				boolean isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(orgIdAmountMap.get(orgId), "TRANSACTION", "DRAW_GAME_SALE", orgId,
						0, "AGENT", 0, con);
				if(!isValid)
					throw new LMSException();
				OrgCreditUpdation.updateOrganizationBalWithValidate(orgIdAmountMap.get(orgId), "CLAIM_BAL", "DEBIT", orgId,
						0, "AGENT", 0, con);
				/*OrgCreditUpdation.updateCreditLimitForAgent(orgId,
						"DRAW_GAME_SALE", orgIdAmountMap.get(orgId), con);
				updateOrgBalance("CLAIM_BAL", orgIdAmountMap.get(orgId), orgId,
						"DEBIT", con);*/
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("error in sale agent");
		}
		logger.debug(new Date() + "---end-dg sale update for agent--"
				+ new Date().getTime());
	}

	public void updateDGPwtLedgerForRet(Connection con) throws LMSException {
		logger.debug(new Date() + "---start-dg pwt update for retailer--"
				+ new Date().getTime());
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			String gameQry = "select game_id, game_nbr, game_name from st_dg_game_master";
			String pwtQry = "select aaa.game_id, bbb.game_nbr, bbb.game_name,retailer_org_id,agtUserId,agtOrgId, aaa.draw_id, sum(aaa.agt_claim_comm) totalAgtComm, sum(aaa.pwt_amt) totalPwtAmt, aaa.pwt_type, sum(aaa.retailer_claim_comm) totalRetComm,DATE(aaa.transaction_date) 'transaction_date', sum(govt_claim_comm) govtClaimComm from((select  b.ticket_nbr,a.retailer_org_id, a.pwt_amt, 'Anonymous' as  'pwt_type' , a.game_id, b.panel_id,   b.draw_id, a.retailer_claim_comm , a.agt_claim_comm,a.govt_claim_comm , 'Anonymous' as name, transaction_date from  st_dg_ret_pwt_?  a, st_dg_pwt_inv_? b, st_lms_retailer_transaction_master c where a.status = 'CLAIM_BAL' and a.transaction_id = b.retailer_transaction_id and  a.transaction_id = c.transaction_id) union all (select  cc.ticket_nbr,aa.retailer_org_id, aa.pwt_amt , 'direct_plr' as  'pwt_type',  aa.game_id, aa.panel_id, cc.draw_id, aa.retailer_claim_comm,  aa.agt_claim_comm, 0,  concat(bb.first_name, ' ', bb.last_name) 'name', transaction_date from  st_dg_ret_direct_plr_pwt aa,  st_lms_player_master bb, st_dg_pwt_inv_? cc where   aa.pwt_claim_status = 'CLAIM_BAL' and aa.player_id = bb.player_id and  aa.transaction_id =   cc.retailer_transaction_id))aaa, st_dg_game_master bbb,(select um.user_id retUserId,um.organization_id retOrgId,um.parent_user_id agtUserId,om.parent_id agtOrgId from st_lms_organization_master om inner join st_lms_user_master um on om.organization_id=um.organization_id where om.organization_type='RETAILER') ccc  where  aaa.game_id = bbb.game_id and retailer_org_id=retOrgId  group by pwt_type,retailer_org_id,aaa.game_id, aaa.draw_id,date(aaa.transaction_date)";

			String retPwtUpdateQuery = "update st_dg_ret_pwt_? aa, st_dg_pwt_inv_? bb,st_lms_retailer_transaction_master cc set aa.status = ? ,"
					+ " bb.status = ? , bb.agent_transaction_id =? where aa.transaction_id = bb.retailer_transaction_id and aa.transaction_id=cc.transaction_id "
					+ " and aa.game_id = ? and  bb.draw_id = ?  and aa.retailer_org_id =? and date(cc.transaction_date)=?";
			PreparedStatement retPwtUpdatePstmt = con
					.prepareStatement(retPwtUpdateQuery);

			String retPwtUpdateDirectPlrQry = "update st_dg_ret_direct_plr_pwt aa, st_dg_pwt_inv_? bb,st_lms_retailer_transaction_master cc "
					+ " set aa.pwt_claim_status = ? , bb.status = ? , bb.agent_transaction_id =? where aa.transaction_id = bb.retailer_transaction_id and aa.transaction_id=cc.transaction_id and aa.game_id = ? and bb.draw_id = ? "
					+ " and aa.retailer_org_id =? and date(cc.transaction_date)=?";
			PreparedStatement retDirPlrPstmt = con
					.prepareStatement(retPwtUpdateDirectPlrQry);

			PreparedStatement gameStmt = con.prepareStatement(gameQry);

			PreparedStatement transMasQueryPstmt = con
					.prepareStatement(QueryManager
							.insertInLMSTransactionMaster());
			PreparedStatement agtTransMasterPstmt = con
					.prepareStatement(QueryManager
							.insertInAgentTransactionMaster());
			String agtDGPwtEntry = "insert into st_dg_agt_pwt (agent_user_id, agent_org_id, retailer_org_id, "
					+ " draw_id, game_id, transaction_id, pwt_amt, comm_amt, net_amt, agt_claim_comm, "
					+ " status, govt_claim_comm) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
			PreparedStatement agtPstmt = con.prepareStatement(agtDGPwtEntry);

			ResultSet gameRs = gameStmt.executeQuery();
			pstmt = con.prepareStatement(pwtQry);

			int retOrgId = 0, agtOrgId=0, agtUserId, gameId, drawId;
			double totalPwtAmt, totalAgtComm, govtClaimComm, totalRetComm, totalNetAmt = 0;
			String pwtType, transDate;
			Map<Integer, Double> orgAmtMap = new HashMap<Integer, Double>();
			Map<Integer, Integer> orgIdParentIdMap = new HashMap<Integer, Integer>();
			Map<Integer, Map<String, List<Long>>> orgIdTransIdInvMap = new HashMap<Integer, Map<String, List<Long>>>();
			Map<String, List<Long>> transIdInvMap = null;
			while (gameRs.next()) {
				gameId = gameRs.getInt("game_id");
				totalNetAmt = 0;
				pstmt.setInt(1, gameId);
				pstmt.setInt(2, gameId);
				pstmt.setInt(3, gameId);
				rs = pstmt.executeQuery();
				logger.debug("--Start PWT tran for game No" + gameId);
				while (rs.next()) {
					retOrgId = rs.getInt("retailer_org_id");
					agtOrgId = rs.getInt("agtOrgId");
					agtUserId = rs.getInt("agtUserId");
					gameId = rs.getInt("game_id");
					drawId = rs.getInt("draw_id");
					totalPwtAmt = rs.getDouble("totalPwtAmt");
					totalAgtComm = rs.getDouble("totalAgtComm");
					govtClaimComm= rs.getDouble("govtClaimComm");
					totalRetComm = rs.getDouble("totalRetComm");
					pwtType = rs.getString("pwt_type");
					transDate = rs.getString("transaction_date");
					totalNetAmt = totalPwtAmt + totalRetComm - govtClaimComm;

					orgIdParentIdMap.put(retOrgId, agtOrgId);
					if (orgIdTransIdInvMap.containsKey(retOrgId)) {
						transIdInvMap = orgIdTransIdInvMap.get(retOrgId);
					} else {
						transIdInvMap = new HashMap<String, List<Long>>();
						orgIdTransIdInvMap.put(retOrgId, transIdInvMap);
					}
					// insert data into main transaction master

					transMasQueryPstmt.setString(1, "AGENT");
					transMasQueryPstmt.executeUpdate();
					ResultSet tranRs = transMasQueryPstmt.getGeneratedKeys();

					if (tranRs.next()) {
						long transId = tranRs.getLong(1);

						if (!transIdInvMap.containsKey(transDate)) {
							transIdInvMap.put(transDate,
									new ArrayList<Long>());
						}
						transIdInvMap.get(transDate).add(transId);

						agtTransMasterPstmt.setLong(1, transId);
						agtTransMasterPstmt.setInt(2, agtUserId);
						agtTransMasterPstmt.setInt(3, agtOrgId);
						agtTransMasterPstmt.setString(4, "RETAILER");
						agtTransMasterPstmt.setInt(5, retOrgId);
						agtTransMasterPstmt.setString(6, "DG_PWT_AUTO");
						agtTransMasterPstmt.setTimestamp(7,
								fetchTransTimeStamp(transDate));
						agtTransMasterPstmt.executeUpdate();

						if (!"direct_plr".equals(pwtType)) {
							retPwtUpdatePstmt.setInt(1, gameId);
							retPwtUpdatePstmt.setInt(2, gameId);
							retPwtUpdatePstmt.setString(3, "DONE_CLM");
							retPwtUpdatePstmt
									.setString(4, "CLAIM_RET_CLM_AUTO");
							retPwtUpdatePstmt.setLong(5, transId);
							retPwtUpdatePstmt.setInt(6, gameId);
							retPwtUpdatePstmt.setInt(7, drawId);
							retPwtUpdatePstmt.setInt(8, retOrgId);
							retPwtUpdatePstmt.setString(9, transDate);
							retPwtUpdatePstmt.executeUpdate();
						} else {
							retDirPlrPstmt.setInt(1, gameId);
							retDirPlrPstmt.setString(2, "DONE_CLM");
							retDirPlrPstmt.setString(3, "CLAIM_RET_CLM_AUTO");
							retDirPlrPstmt.setLong(4, transId);
							retDirPlrPstmt.setInt(5, gameId);
							retDirPlrPstmt.setInt(6, drawId);
							retDirPlrPstmt.setInt(7, retOrgId);
							retPwtUpdatePstmt.setString(8, transDate);
							retDirPlrPstmt.executeUpdate();
						}

						agtPstmt.setInt(1, agtUserId);
						agtPstmt.setInt(2, agtOrgId);
						agtPstmt.setInt(3, retOrgId);
						agtPstmt.setInt(4, drawId);
						agtPstmt.setInt(5, gameId);
						agtPstmt.setLong(6, transId);
						agtPstmt.setDouble(7, totalPwtAmt);
						agtPstmt.setDouble(8, totalRetComm);
						agtPstmt.setDouble(9, totalNetAmt);
						agtPstmt.setDouble(10, totalAgtComm);
						agtPstmt.setString(11, "CLAIM_BAL");
						agtPstmt.setDouble(12, govtClaimComm) ;
						agtPstmt.executeUpdate();

					}
					double retCrAmt = totalNetAmt;
					if (orgAmtMap.containsKey(retOrgId)) {
						retCrAmt = retCrAmt + orgAmtMap.get(retOrgId);
					}
					orgAmtMap.put(retOrgId, retCrAmt);
				}

			}

			Set<Integer> allRetId = orgAmtMap.keySet();
			logger.debug("retailer pwt update org amt map" + orgAmtMap);
			for (Integer orgId : allRetId) {
				if (orgId != 0) {
					boolean isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(CommonMethods.fmtToTwoDecimal(orgAmtMap.get(orgId)), "TRANSACTION", "PWT_AUTO", orgId,
							orgIdParentIdMap.get(orgId), "RETAILER", 0, con);
					if(!isValid)
						throw new LMSException();
						
					OrgCreditUpdation.updateOrganizationBalWithValidate(CommonMethods.fmtToTwoDecimal(orgAmtMap.get(orgId)), "CLAIM_BAL", "CREDIT_UPDATE_LEDGER", orgId,
							orgIdParentIdMap.get(orgId), "RETAILER", 0, con);
					
					/*OrgCreditUpdation.updateCreditLimitForRetailer(orgId,
							"PWT_AUTO", orgAmtMap.get(orgId), con);
					updateOrgBalance("CLAIM_BAL", orgAmtMap.get(orgId), orgId,
							"CREDIT", con);*/
					generateReciptForPwtNew(orgIdTransIdInvMap.get(orgId), con,
							orgIdParentIdMap.get(orgId), retOrgId, "RETAILER",
							"DRAWGAME");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("error in pwt retailer");
		}
		logger.debug(new Date() + "---start-dg pwt update for retailer--"
				+ new Date().getTime());
	}

	public void updateDGPwtLedgerForAgt(Connection con) throws LMSException {
		logger.debug(new Date() + "---start-dg pwt update for agent--"
				+ new Date().getTime());
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			String pwtQry = "select aaa.agent_org_id,boUserId,boOrgId, aaa.game_id, bbb.game_nbr, bbb.game_name, aaa.draw_id, sum(aaa.agt_claim_comm) totalAgtComm,sum(aaa.govt_claim_comm) govtClaimComm, sum(aaa.pwt_amt) totalPwtAmt, aaa.pwt_type, DATE(aaa.transaction_date) 'transaction_date' from ((select  a.transaction_id,a.agent_org_id,  a.draw_id, a.pwt_amt, 'Anonymous' as 'pwt_type', a.game_id,  a.agt_claim_comm,a.govt_claim_comm, transaction_date from st_dg_agt_pwt a, st_lms_agent_transaction_master b where   a.status = 'CLAIM_BAL' and a.transaction_id = b.transaction_id) union all (select    aa.transaction_id,aa.agent_org_id, aa.draw_id, aa.pwt_amt, 'direct_plr' as 'pwt_type', aa.game_id,  agt_claim_comm,0, transaction_date from st_dg_agt_direct_plr_pwt aa where aa.pwt_claim_status = 'CLAIM_BAL' ))aaa, st_dg_game_master bbb,(select um.user_id agtUserId,um.organization_id agtOrgId,um.parent_user_id boUserId,om.parent_id boOrgId from st_lms_organization_master om inner join st_lms_user_master um on om.organization_id=um.organization_id where om.organization_type='AGENT' and isrolehead='Y') ccc where  aaa.game_id = bbb.game_id and agent_org_id=agtOrgId  group by pwt_type,agent_org_id,aaa.game_id, aaa.draw_id, date(aaa.transaction_date)";

			String agtPwtUpdateQuery = "update st_dg_agt_pwt aa, st_dg_pwt_inv_? bb,st_lms_agent_transaction_master cc set aa.status = ? , bb.status = ? , bb.bo_transaction_id =? where aa.transaction_id = bb.agent_transaction_id and aa.transaction_id=cc.transaction_id and aa.game_id = ? and bb.draw_id = ? and aa.agent_org_id=? and date(cc.transaction_date)=?";
			PreparedStatement agtPwtUpdatePstmt = con
					.prepareStatement(agtPwtUpdateQuery);

			// insert in st_agt_direct_player table in case of
			// player

			String agtPwtUpdateDirectPlrQry = "update st_dg_agt_direct_plr_pwt aa, st_dg_pwt_inv_? bb,st_lms_agent_transaction_master cc set aa.pwt_claim_status = ? , bb.status = ? , bb.bo_transaction_id =? where aa.transaction_id = bb.agent_transaction_id and aa.transaction_id=cc.transaction_id and aa.game_id = ? and bb.draw_id = ? and aa.agent_org_id=? and date(cc.transaction_date)=?";
			PreparedStatement agtDirPlrPstmt = con
					.prepareStatement(agtPwtUpdateDirectPlrQry);
			PreparedStatement transMasQueryPstmt = con
					.prepareStatement(QueryManager
							.insertInLMSTransactionMaster());
			PreparedStatement boTransMasterPstmt = con
					.prepareStatement(QueryManager
							.insertInBOTransactionMaster());
			String agtDGPwtEntry = "insert into st_dg_bo_pwt (bo_user_id, bo_org_id, agent_org_id, "
					+ " draw_id, game_id, transaction_id, pwt_amt, comm_amt, net_amt, govt_claim_comm"
					+ ") values (?, ?, ?, ?, ?, ?, ?, ?,?,?)";
			PreparedStatement agtPstmt = con.prepareStatement(agtDGPwtEntry);
			int agtOrgId, boOrgId, boUserId, gameId, drawId;
			double totalPwtAmt, totalAgtComm, totalNetAmt, govtClaimComm;
			String pwtType, transDate;
			Map<Integer, Double> orgAmtMap = new HashMap<Integer, Double>();
			Map<Integer, Integer> orgIdParentIdMap = new HashMap<Integer, Integer>();
			Map<Integer, Map<String, List<Long>>> orgIdTransIdInvMap = new HashMap<Integer, Map<String, List<Long>>>();
			Map<String, List<Long>> transIdInvMap = null;

			pstmt = con.prepareStatement(pwtQry);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				agtOrgId = rs.getInt("agent_org_id");
				boOrgId = rs.getInt("boOrgId");
				boUserId = rs.getInt("boUserId");
				gameId = rs.getInt("game_id");
				logger.debug("--Agent PWT tran for game No" + gameId);
				drawId = rs.getInt("draw_id");
				totalPwtAmt = rs.getDouble("totalPwtAmt");
				totalAgtComm = rs.getDouble("totalAgtComm");
				govtClaimComm = rs.getDouble("govtClaimComm");
				pwtType = rs.getString("pwt_type");
				transDate = rs.getString("transaction_date");
				totalNetAmt = totalPwtAmt + totalAgtComm - govtClaimComm;

				orgIdParentIdMap.put(agtOrgId, boOrgId);
				if (orgIdTransIdInvMap.containsKey(agtOrgId)) {
					transIdInvMap = orgIdTransIdInvMap.get(agtOrgId);
				} else {
					transIdInvMap = new HashMap<String, List<Long>>();
					orgIdTransIdInvMap.put(agtOrgId, transIdInvMap);
				}
				// insert data into main transaction master

				transMasQueryPstmt.setString(1, "BO");
				transMasQueryPstmt.executeUpdate();
				ResultSet tranRs = transMasQueryPstmt.getGeneratedKeys();

				if (tranRs.next()) {
					long transId = tranRs.getLong(1);

					if (!transIdInvMap.containsKey(transDate)) {
						transIdInvMap.put(transDate, new ArrayList<Long>());
					}
					transIdInvMap.get(transDate).add(transId);

					boTransMasterPstmt.setLong(1, transId);
					boTransMasterPstmt.setInt(2, boUserId);
					boTransMasterPstmt.setInt(3, boOrgId);
					boTransMasterPstmt.setString(4, "AGENT");
					boTransMasterPstmt.setInt(5, agtOrgId);
					boTransMasterPstmt.setTimestamp(6,
							fetchTransTimeStamp(transDate));
					boTransMasterPstmt.setString(7, "DG_PWT_AUTO");
					boTransMasterPstmt.executeUpdate();

					if (!"direct_plr".equals(pwtType)) {
						agtPwtUpdatePstmt.setInt(1, gameId);
						agtPwtUpdatePstmt.setString(2, "DONE_CLM");
						agtPwtUpdatePstmt.setString(3, "CLAIM_AGT_CLM_AUTO");
						agtPwtUpdatePstmt.setLong(4, transId);
						agtPwtUpdatePstmt.setInt(5, gameId);
						agtPwtUpdatePstmt.setInt(6, drawId);
						agtPwtUpdatePstmt.setInt(7, agtOrgId);
						agtPwtUpdatePstmt.setString(8, transDate);
						logger.debug("--agent pwt update--"+agtDirPlrPstmt);
						agtPwtUpdatePstmt.executeUpdate();
					} else {
						agtDirPlrPstmt.setInt(1, gameId);
						agtDirPlrPstmt.setString(2, "DONE_CLM");
						agtDirPlrPstmt.setString(3, "CLAIM_AGT_CLM_AUTO");
						agtDirPlrPstmt.setLong(4, transId);
						agtDirPlrPstmt.setInt(5, gameId);
						agtDirPlrPstmt.setInt(6, drawId);
						agtDirPlrPstmt.setInt(7, agtOrgId);
						agtDirPlrPstmt.setString(8, transDate);
						logger.debug("--agent pwt update- direct plr-"+agtDirPlrPstmt);
						agtDirPlrPstmt.executeUpdate();
					}

					agtPstmt.setInt(1, boUserId);
					agtPstmt.setInt(2, boOrgId);
					agtPstmt.setInt(3, agtOrgId);
					agtPstmt.setInt(4, drawId);
					agtPstmt.setInt(5, gameId);
					agtPstmt.setLong(6, transId);
					agtPstmt.setDouble(7, totalPwtAmt);
					agtPstmt.setDouble(8, totalAgtComm);
					agtPstmt.setDouble(9, totalNetAmt);
					agtPstmt.setDouble(10, govtClaimComm);
					agtPstmt.executeUpdate();

				}
				double agtCrAmt = totalNetAmt;
				if (orgAmtMap.containsKey(agtOrgId)) {
					agtCrAmt = agtCrAmt + orgAmtMap.get(agtOrgId);
				}
				orgAmtMap.put(agtOrgId, agtCrAmt);
			}

			Set<Integer> allAgtId = orgAmtMap.keySet();
			logger.debug("agent pwt update org amt map" + orgAmtMap);
			for (Integer orgId : allAgtId) {
				if (orgId != 0) {
					
					
					boolean isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(CommonMethods.fmtToTwoDecimal(orgAmtMap.get(orgId)), "TRANSACTION", "PWT_AUTO", orgId,
							0, "AGENT", 0, con);
					if(!isValid)
						throw new LMSException();
					OrgCreditUpdation.updateOrganizationBalWithValidate(CommonMethods.fmtToTwoDecimal(orgAmtMap.get(orgId)), "CLAIM_BAL", "CREDIT_UPDATE_LEDGER", orgId,
							0, "AGENT", 0, con);
					/*OrgCreditUpdation.updateCreditLimitForAgent(orgId,
							"PWT_AUTO", orgAmtMap.get(orgId), con);
					updateOrgBalance("CLAIM_BAL", orgAmtMap.get(orgId), orgId,
							"CREDIT", con);*/
					generateReceiptDGBoNew(con, orgId, "AGENT", orgIdTransIdInvMap
							.get(orgId));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("error in pwt agent");
		}
		logger.debug(new Date() + "---start-dg pwt update for agent--"
				+ new Date().getTime());
	}
	
	protected void generateDGReceiptNew(
			Map<Integer, Map<String, List<Long>>> orgTranMap,
			Map<Integer, Integer> orgParentMap, String mainRecType,
			String secRecType, Connection con) throws SQLException {

		Map<String, List<Long>> trnIdMapInvoice = null;
		//List<Integer> trnIdListInvoice = null;
		//List<Integer> trnIdListInvoice = new ArrayList<Integer>();
		Set<Integer> orgIdSet = null;
		String query = "SELECT generated_id from st_lms_agent_receipts where (receipt_type=? or receipt_type=?) and agent_org_id=? ORDER BY receipt_id DESC LIMIT 1";
		PreparedStatement selStmt = con.prepareStatement(query);
		PreparedStatement recMasStmt = con.prepareStatement(QueryManager
				.insertInReceiptMaster());
		PreparedStatement agtRecStmt = con.prepareStatement(QueryManager
				.insertInAgentReceipts());
		PreparedStatement recMapStmt = con.prepareStatement(QueryManager
				.insertAgentReceiptTrnMapping());

		if (orgTranMap != null && orgParentMap != null) {
			orgIdSet = orgTranMap.keySet();
		}
		for (Integer orgId : orgIdSet) {
			trnIdMapInvoice = orgTranMap.get(orgId);
			List<Long> trnIdListInvoice = new ArrayList<Long>();
			if (trnIdMapInvoice != null && trnIdMapInvoice.size() > 0) {
				Set<String> dateKeys = trnIdMapInvoice.keySet();
				//for (String date : dateKeys) {
					//trnIdListInvoice = trnIdMapInvoice.get(date);
				for (String date : dateKeys) {
                //trnIdListInvoice = trnIdMapInvoice.get(date);
					trnIdListInvoice.addAll(trnIdMapInvoice.get(date)) ;
				}
					selStmt.setString(1, mainRecType);
					selStmt.setString(2, secRecType);
					selStmt.setInt(3, orgParentMap.get(orgId));
					ResultSet recieptRs = selStmt.executeQuery();
					String lastRecieptNoGenerated = null;
					while (recieptRs.next()) {
						lastRecieptNoGenerated = recieptRs
								.getString("generated_id");
					}

					// String autoGeneRecieptNo=null;
					String autoGeneRecieptNo = GenerateRecieptNo
							.getRecieptNoAgt(mainRecType,
									lastRecieptNoGenerated, "AGENT",
									orgParentMap.get(orgId));
					logger.debug("--ReceieptNo--" + autoGeneRecieptNo);
					// insert in receipt master for invoice

					recMasStmt.setString(1, "AGENT");
					recMasStmt.executeUpdate();

					ResultSet rs = recMasStmt.getGeneratedKeys();
					int invoiceId = -1;
					while (rs.next()) {
						invoiceId = rs.getInt(1);
					}

					// insert into agent receipt table

					agtRecStmt.setInt(1, invoiceId);
					agtRecStmt.setString(2, mainRecType);
					agtRecStmt.setInt(3, orgParentMap.get(orgId));
					agtRecStmt.setInt(4, orgId);
					agtRecStmt.setString(5, "RETAILER");
					agtRecStmt.setString(6, autoGeneRecieptNo);
					agtRecStmt.setTimestamp(7, Util.getCurrentTimeStamp());
					agtRecStmt.execute();

					for (int i = 0; i < trnIdListInvoice.size(); i++) {
						recMapStmt.setInt(1, invoiceId);
						recMapStmt.setLong(2, trnIdListInvoice.get(i));
						recMapStmt.execute();

					}

				//}
			}
		}
	}


	protected void generateDGReceiptForBONew(
			Map<Integer, Map<String, List<Long>>> orgTranMap,
			Map<Integer, Integer> orgParentMap, String mainRecType,
			String secRecType, Connection con) throws SQLException {

		Map<String, List<Long>> trnIdMapInvoice = null;
		//List<Integer> trnIdListInvoice = null;
		//List<Integer> trnIdListInvoice = new ArrayList<Integer>();
		Set<Integer> orgIdSet = null;
		String query = "SELECT generated_id from st_lms_bo_receipts where receipt_type=? or receipt_type=? ORDER BY receipt_id DESC LIMIT 1";
		PreparedStatement selStmt = con.prepareStatement(query);
		PreparedStatement recMasStmt = con.prepareStatement(QueryManager
				.insertInReceiptMaster());
		PreparedStatement boRecStmt = con.prepareStatement(QueryManager
				.insertInBOReceipts());
		PreparedStatement recMapStmt = con.prepareStatement(QueryManager
				.insertBOReceiptTrnMapping());

		if (orgTranMap != null && orgParentMap != null) {
			orgIdSet = orgTranMap.keySet();
		}
		for (Integer orgId : orgIdSet) {
			trnIdMapInvoice = orgTranMap.get(orgId);
			List<Long> trnIdListInvoice = new ArrayList<Long>();
			if (trnIdMapInvoice != null && trnIdMapInvoice.size() > 0) {
				Set<String> dateKeys = trnIdMapInvoice.keySet();
				//for (String date : dateKeys) {
					//trnIdListInvoice = trnIdMapInvoice.get(date);
                    
				for (String date : dateKeys) {
					//trnIdListInvoice = trnIdMapInvoice.get(date);
					trnIdListInvoice.addAll(trnIdMapInvoice.get(date)) ;
				}
					selStmt.setString(1, mainRecType);
					selStmt.setString(2, secRecType);
					ResultSet recieptRs = selStmt.executeQuery();
					String lastRecieptNoGenerated = null;
					while (recieptRs.next()) {
						lastRecieptNoGenerated = recieptRs
								.getString("generated_id");
					}

					String autoGeneRecieptNo = GenerateRecieptNo.getRecieptNo(
							mainRecType, lastRecieptNoGenerated, "BO");
					logger.debug("--Reciept No--" + autoGeneRecieptNo);
					// insert in receipt master for invoice

					recMasStmt.setString(1, "BO");
					recMasStmt.executeUpdate();

					ResultSet rs = recMasStmt.getGeneratedKeys();
					int invoiceId = -1;
					while (rs.next()) {
						invoiceId = rs.getInt(1);
					}

					// insert into agent receipt table

					boRecStmt.setInt(1, invoiceId);
					boRecStmt.setString(2, mainRecType);
					boRecStmt.setInt(3, orgId);
					boRecStmt.setString(4, "AGENT");
					boRecStmt.setString(5, autoGeneRecieptNo);
					boRecStmt.setTimestamp(6, Util.getCurrentTimeStamp());
					boRecStmt.execute();

					for (int i = 0; i < trnIdListInvoice.size(); i++) {
						recMapStmt.setInt(1, invoiceId);
						recMapStmt.setLong(2, trnIdListInvoice.get(i));
						recMapStmt.execute();

					}

				//}
			}
		}
	}

	private boolean updateOrgBalance(String claimType, double amount,
			int orgId, String amtUpdateType, Connection connection)
			throws SQLException {

		// check whether amount type is debit or credit
		amount = "DEBIT".equals(amtUpdateType) ? -1 * amount : amount;

		String updateRetBalQuery = null;
		if ("UNCLAIM_BAL".equalsIgnoreCase(claimType)) {
			updateRetBalQuery = "update st_lms_organization_master set unclaimable_bal = (unclaimable_bal+?) where organization_id = ?";
		} else if ("CLAIM_BAL".equalsIgnoreCase(claimType)) {
			updateRetBalQuery = "update st_lms_organization_master set claimable_bal = (claimable_bal+?) "
					+ " , organization_status =  if(organization_status='TERMINATE','TERMINATE',if((available_credit-claimable_bal)>0, 'ACTIVE', 'INACTIVE')) "
					+ " where organization_id = ?";
			// updateRetBalQuery = "update st_lms_organization_master set
			// claimable_bal = (claimable_bal+?) where organization_id = ?";
		} else if ("ACA_N_CLAIM_BAL".equalsIgnoreCase(claimType)) {
			updateRetBalQuery = "update st_lms_organization_master set claimable_bal = (claimable_bal-?),"
					+ " available_credit=(available_credit+?) where organization_id = ?";
		} else if ("ACA_N_UNCLAIM_BAL".equalsIgnoreCase(claimType)) {
			updateRetBalQuery = "update st_lms_organization_master set unclaimable_bal = (unclaimable_bal-?),"
					+ " available_credit=(available_credit+?) where organization_id = ?";
		}
		PreparedStatement updateRetBal = connection
				.prepareStatement(updateRetBalQuery);
		updateRetBal.setDouble(1, amount);
		if ("ACA_N_CLAIM_BAL".equalsIgnoreCase(claimType)) {
			updateRetBal.setDouble(2, amount);
			updateRetBal.setInt(3, orgId);
		} else {
			updateRetBal.setInt(2, orgId);
		}
		logger.debug("organisation master update::" + updateRetBal);
		int retBalRow = updateRetBal.executeUpdate();

		return retBalRow > 0;
	}
	
	protected String generateReciptForPwtNew(Map<String, List<Long>> transIdMap,
			Connection connection, int userOrgID, int partyId,
			String partyType, String recType) {
		int agtReceiptId = -1;
		StringBuilder receipts = new StringBuilder("");
		// int boReceiptId=-1;
		PreparedStatement agtReceiptPstmt = null;
		PreparedStatement agtReceiptMappingPstmt = null;
		String receiptType = null;
		try {
			Set<String> transDate = transIdMap.keySet();
			//for (String date : transDate) {
			//	List<Integer> transIdList = transIdMap.get(date);
            //List<Integer> transIdList=null;
			List<Long> transIdList= new ArrayList<Long>();
			for (String date : transDate) {
					 //transIdList = transIdMap.get(date);
				     transIdList.addAll(transIdMap.get(date)) ;
			}
				// for generating receipt********************
				// insert in receipt master
				agtReceiptPstmt = connection.prepareStatement(QueryManager
						.insertInReceiptMaster());
				agtReceiptPstmt.setString(1, "AGENT");
				agtReceiptPstmt.executeUpdate();
				if ("DRAWGAME".equalsIgnoreCase(recType)) {
					receiptType = "DG_RECEIPT";
				}
				else if("OLA".equalsIgnoreCase(recType)){
					receiptType = "OLA_RECEIPT";
				}else {
					receiptType = "RECEIPT";
				}
				ResultSet agtRSet = agtReceiptPstmt.getGeneratedKeys();
				while (agtRSet.next()) {
					agtReceiptId = agtRSet.getInt(1);
				}

				PreparedStatement autoGenRecptPstmt = null;
				if (receiptType.equalsIgnoreCase("RECEIPT")) {
					autoGenRecptPstmt = connection
							.prepareStatement(QueryManager
									.getAGENTLatestReceiptNb());
					autoGenRecptPstmt.setString(1, receiptType);
					autoGenRecptPstmt.setInt(2, userOrgID);
				} else if (receiptType.equalsIgnoreCase("DG_RECEIPT")) {
						String query = "SELECT generated_id from st_lms_agent_receipts where (receipt_type='DG_INVOICE' or receipt_type='DG_RECEIPT') and agent_org_id="
								+ userOrgID
								+ " ORDER BY generated_id DESC LIMIT 1";
						autoGenRecptPstmt = connection.prepareStatement(query);
					}
					else if (receiptType.equalsIgnoreCase("OLA_RECEIPT")) {
							String query = "SELECT generated_id from st_lms_agent_receipts where (receipt_type='OLA_INVOICE' or receipt_type='OLA_RECEIPT') and agent_org_id="
									+ userOrgID
									+ " ORDER BY generated_id DESC LIMIT 1";
							autoGenRecptPstmt = connection.prepareStatement(query);
						}
			
		
				ResultSet recieptRs = autoGenRecptPstmt.executeQuery();
				String lastRecieptNoGenerated = null;

				while (recieptRs.next()) {
					lastRecieptNoGenerated = recieptRs
							.getString("generated_id");
				}

				String autoGeneRecieptNoAgt = GenerateRecieptNo
						.getRecieptNoAgt(receiptType, lastRecieptNoGenerated,
								"AGENT", userOrgID);
				logger.debug("--Reciept No--" + autoGeneRecieptNoAgt);
				// insert in agent receipts
				// agtReceiptQuery = QueryManager.getST1AgtReceiptsQuery();
				agtReceiptPstmt = connection.prepareStatement(QueryManager
						.insertInAgentReceipts());
				agtReceiptPstmt.setInt(1, agtReceiptId);
				agtReceiptPstmt.setString(2, receiptType);
				agtReceiptPstmt.setInt(3, userOrgID);
				agtReceiptPstmt.setInt(4, partyId);
				agtReceiptPstmt.setString(5, partyType);
				agtReceiptPstmt.setString(6, autoGeneRecieptNoAgt);
				agtReceiptPstmt.setTimestamp(7, Util.getCurrentTimeStamp());
				agtReceiptPstmt.execute();

				// insert agetn receipt trn mapping

				// agtReceiptMappingQuery =
				// QueryManager.getST1AgtReceiptsMappingQuery();
				agtReceiptMappingPstmt = connection
						.prepareStatement(QueryManager
								.insertAgentReceiptTrnMapping());

				for (int i = 0; i < transIdList.size(); i++) {
					agtReceiptMappingPstmt.setInt(1, agtReceiptId);
					agtReceiptMappingPstmt.setLong(2, transIdList.get(i));
					agtReceiptMappingPstmt.execute();
				}
				receipts.append(agtReceiptId + "-" + autoGeneRecieptNoAgt);
			//}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return receipts.toString();
	}
	private String generateReceiptOlaBo(Connection connection, int partyId,
			String partyType, Map<String, List<Long>> transIdMap)
			throws LMSException {
		try {
			Set<String> dateKeys = transIdMap.keySet();
			StringBuilder result = new StringBuilder("");
			for (String date : dateKeys) {
				List<Long> transIdList = transIdMap.get(date);

				// get latest generated receipt number
				// PreparedStatement autoGenRecptPstmtBO =
				// connection.prepareStatement(QueryManager.getBOLatestReceiptNb());
				String query = "SELECT generated_id from st_lms_bo_receipts where receipt_type='OLA_INVOICE' or receipt_type='OLA_RECEIPT' ORDER BY generated_id DESC LIMIT 1";
				PreparedStatement autoGenRecptPstmtBO = connection
						.prepareStatement(query);
				// autoGenRecptPstmtBO.setString(1, "DG_RECEIPT");
				ResultSet recieptRsBO = autoGenRecptPstmtBO.executeQuery();

				String autoGeneRecieptNoBO = null;
				if (recieptRsBO.next()) {
					String lastRecieptNoGeneratedBO = recieptRsBO
							.getString("generated_id");
					autoGeneRecieptNoBO = GenerateRecieptNo.getRecieptNo(
							"OLA_RECEIPT", lastRecieptNoGeneratedBO, "BO");
				} else {
					autoGeneRecieptNoBO = GenerateRecieptNo.getRecieptNo(
							"OLA_RECEIPT", null, "BO");
				}
				logger.debug("--Reciept No--" + autoGeneRecieptNoBO);
				// for generating receipt********************

				PreparedStatement boReceiptPstmt = connection
						.prepareStatement(QueryManager.insertInReceiptMaster());
				boReceiptPstmt.setString(1, "BO");
				boReceiptPstmt.executeUpdate();
				int boReceiptId = -1;
				ResultSet boRSet = boReceiptPstmt.getGeneratedKeys();
				if (boRSet.next()) {
					boReceiptId = boRSet.getInt(1);

					boReceiptPstmt = connection.prepareStatement(QueryManager
							.insertInBOReceipts());
					boReceiptPstmt.setInt(1, boReceiptId);
					boReceiptPstmt.setString(2, "OLA_RECEIPT");
					boReceiptPstmt.setInt(3, partyId);
					boReceiptPstmt.setString(4, partyType);
					boReceiptPstmt.setString(5, autoGeneRecieptNoBO);
					boReceiptPstmt.setTimestamp(6, Util.getCurrentTimeStamp());
					boReceiptPstmt.execute();

					PreparedStatement boReceiptMappingPstmt = connection
							.prepareStatement(QueryManager
									.insertBOReceiptTrnMapping());
					for (Long transId : transIdList) {
						boReceiptMappingPstmt.setInt(1, boReceiptId);
						boReceiptMappingPstmt.setLong(2, transId);
						boReceiptMappingPstmt.execute();
					}

				}

				result.append(boReceiptId + "-" + autoGeneRecieptNoBO + ";");
			}

			return result.toString();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException();
		}
	}
	protected String generateReceiptDGBoNew(Connection connection, int partyId,
			String partyType, Map<String, List<Long>> transIdMap)
			throws LMSException {
		try {
			Set<String> dateKeys = transIdMap.keySet();
			StringBuilder result = new StringBuilder("");
			//for (String date : dateKeys) {
			//	List<Integer> transIdList = transIdMap.get(date);
           //List<Integer> transIdList = null;
			List<Long> transIdList = new ArrayList<Long>();
			for (String date : dateKeys) {
					//transIdList = transIdMap.get(date);
				    transIdList.addAll(transIdMap.get(date)) ;
			}

				// get latest generated receipt number
				// PreparedStatement autoGenRecptPstmtBO =
				// connection.prepareStatement(QueryManager.getBOLatestReceiptNb());
				String query = "SELECT generated_id from st_lms_bo_receipts where receipt_type='DG_INVOICE' or receipt_type='DG_RECEIPT' ORDER BY generated_id DESC LIMIT 1";
				PreparedStatement autoGenRecptPstmtBO = connection
						.prepareStatement(query);
				// autoGenRecptPstmtBO.setString(1, "DG_RECEIPT");
				ResultSet recieptRsBO = autoGenRecptPstmtBO.executeQuery();

				String autoGeneRecieptNoBO = null;
				if (recieptRsBO.next()) {
					String lastRecieptNoGeneratedBO = recieptRsBO
							.getString("generated_id");
					autoGeneRecieptNoBO = GenerateRecieptNo.getRecieptNo(
							"DG_RECEIPT", lastRecieptNoGeneratedBO, "BO");
				} else {
					autoGeneRecieptNoBO = GenerateRecieptNo.getRecieptNo(
							"DG_RECEIPT", null, "BO");
				}
				logger.debug("--Reciept No--" + autoGeneRecieptNoBO);
				// for generating receipt********************

				PreparedStatement boReceiptPstmt = connection
						.prepareStatement(QueryManager.insertInReceiptMaster());
				boReceiptPstmt.setString(1, "BO");
				boReceiptPstmt.executeUpdate();
				int boReceiptId = -1;
				ResultSet boRSet = boReceiptPstmt.getGeneratedKeys();
				if (boRSet.next()) {
					boReceiptId = boRSet.getInt(1);

					boReceiptPstmt = connection.prepareStatement(QueryManager
							.insertInBOReceipts());
					boReceiptPstmt.setInt(1, boReceiptId);
					boReceiptPstmt.setString(2, "DG_RECEIPT");
					boReceiptPstmt.setInt(3, partyId);
					boReceiptPstmt.setString(4, partyType);
					boReceiptPstmt.setString(5, autoGeneRecieptNoBO);
					boReceiptPstmt.setTimestamp(6, Util.getCurrentTimeStamp());
					boReceiptPstmt.execute();

					PreparedStatement boReceiptMappingPstmt = connection
							.prepareStatement(QueryManager
									.insertBOReceiptTrnMapping());
					for (Long transId : transIdList) {
						boReceiptMappingPstmt.setInt(1, boReceiptId);
						boReceiptMappingPstmt.setLong(2, transId);
						boReceiptMappingPstmt.execute();
					}

				}

				result.append(boReceiptId + "-" + autoGeneRecieptNoBO + ";");
			//}

			return result.toString();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException();
		}
	}
	/**
	 * @since 
	 * @param con
	 */
	public void updateOlaDepositLedgerForRet(Connection con)
	{
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int retOrgId, agtUserId, agtOrgId,walletId;
		String transDate;
		double depositAmt,retComm,agtComm,retDebitAmount,totalRetNetAmt,totalAgtNetAmt;
		try{
			String depositQuery = "select retailer_comm,agent_comm,DATE(transaction_date) 'transaction_date',wallet_id,a.retailer_org_id,agtUserId,agtOrgId,sum(deposit_Amt) depositAmt,sum(net_amt) totalretNetAmt,sum(agent_net_amt) totalAgentNetAmt from st_ola_ret_deposit a inner join  st_lms_retailer_transaction_master b inner join (select um.user_id retUserId,um.organization_id retOrgId,um.parent_user_id agtUserId,om.parent_id agtOrgId from st_lms_organization_master om inner join st_lms_user_master um on om.organization_id=um.organization_id where om.organization_type='RETAILER') d on a.transaction_id = b.transaction_id and a.retailer_org_id=retOrgId  where claim_status='CLAIM_BAL' group by a.retailer_org_id,a.wallet_id,DATE(b.transaction_date),retailer_comm,agent_comm order by retailer_org_id,wallet_id,transaction_date";
			String depositRefundQuery = "select retailer_comm,agent_comm,wallet_id,sum(net_amt) totalretNetAmt,sum(agent_net_amt) totalAgentNetAmt,a.retailer_org_id,agtUserId,agtOrgId,sum(deposit_amt) depositAmt,DATE(transaction_date) 'transaction_date',transaction_type from st_ola_ret_deposit_refund a inner join  st_lms_retailer_transaction_master b inner join (select um.user_id retUserId,um.organization_id retOrgId,um.parent_user_id agtUserId,om.parent_id agtOrgId from st_lms_organization_master om inner join st_lms_user_master um on om.organization_id=um.organization_id where om.organization_type='RETAILER') d on a.transaction_id = b.transaction_id and a.retailer_org_id=retOrgId  where claim_status='CLAIM_BAL' group by a.retailer_org_id,DATE(transaction_date),transaction_type,wallet_id,retailer_comm,agent_comm order by retailer_org_id,transaction_date";
			Map<Integer, Double> orgIdAmountMap = new HashMap<Integer, Double>();
			Map<Integer, Integer> orgIdParentIdMap = new HashMap<Integer, Integer>();
			Map<Integer, Map<String, List<Long>>> orgIdTransIdInvMap = new HashMap<Integer, Map<String, List<Long>>>();
			Map<String, List<Long>> transIdInvMap = null;
			Map<Integer, Map<String, List<Long>>> orgIdTransIdInvRetMap = new HashMap<Integer, Map<String, List<Long>>>();
			Map<String, List<Long>> transIdInvRetMap = null;
			PreparedStatement pstmtAgt = con.prepareStatement(QueryManager.insertInLMSTransactionMaster());
			PreparedStatement pstmtAgtTrans = con.prepareStatement(QueryManager.insertInAgentTransactionMaster());
			pstmt = con.prepareStatement(depositQuery);
			PreparedStatement insAgtDepositPstmt = con.prepareStatement("insert into st_ola_agt_deposit(transaction_id, bo_ref_transaction_id, agent_org_id, deposit_amt, net_amt,bo_net_amt,retailer_comm, agent_comm, claim_status, wallet_id, retailer_org_id) values(?,?,?,?,?,?,?,?,?,?,?)");
	PreparedStatement updateRetDepositPstmt = con
			.prepareStatement("update st_ola_ret_deposit a inner join  st_lms_retailer_transaction_master b  on a.transaction_id = b.transaction_id  set claim_status='DONE_CLAIM',agent_ref_transaction_id=? where a.retailer_org_id=? and wallet_id=? and retailer_comm=? and agent_comm=?");
	PreparedStatement insAgtDepositRetPstmt = con
			.prepareStatement("insert into st_ola_agt_deposit_refund (transaction_id, bo_ref_transaction_id, deposit_amt, net_amt,bo_net_amt, retailer_comm,agent_comm,wallet_id, status, retailer_org_id, agent_org_id) values(?,?,?,?,?,?,?,?,?,?,?)");
	PreparedStatement updateRetDepositRetPstmt = con
			.prepareStatement("update st_ola_ret_deposit_refund a inner join  st_lms_retailer_transaction_master b on a.transaction_id = b.transaction_id  set claim_status='DONE_CLAIM',agent_ref_transaction_id=? where a.retailer_org_id=? and DATE(transaction_date)=? and transaction_type=? and wallet_id=? and retailer_comm=? and agent_comm=?");
			rs = pstmt.executeQuery();
			while(rs.next())
			{
				transDate = rs.getString("transaction_date");
				walletId = rs.getInt("wallet_id");
				retOrgId = rs.getInt("retailer_org_id");
				agtUserId = rs.getInt("agtUserId");
				agtOrgId = rs.getInt("agtOrgId");
				depositAmt = rs.getDouble("depositAmt");
				totalRetNetAmt = rs.getDouble("totalretNetAmt");
				totalAgtNetAmt = rs.getDouble("totalAgentNetAmt");
				//totalRetComm = rs.getDouble("retailerComm");
				//totalAgtComm = rs.getDouble("agentComm");
				retComm = rs.getDouble("retailer_comm");
				agtComm = rs.getDouble("agent_comm");
			

		if (orgIdAmountMap.containsKey(retOrgId)) {
			retDebitAmount = orgIdAmountMap.get(retOrgId)
					+ totalRetNetAmt;
		} else {
			retDebitAmount = totalRetNetAmt;
		}

		orgIdAmountMap.put(retOrgId, retDebitAmount);
		orgIdParentIdMap.put(retOrgId, agtOrgId);

		if (orgIdTransIdInvMap.containsKey(retOrgId)) {
			transIdInvMap = orgIdTransIdInvMap.get(retOrgId);
		} else {
			transIdInvMap = new HashMap<String, List<Long>>();
			orgIdTransIdInvMap.put(retOrgId, transIdInvMap);
		}

		// insert in main transaction table

		pstmtAgt.setString(1, "AGENT");
		pstmtAgt.executeUpdate();
		ResultSet rsTrns = pstmtAgt.getGeneratedKeys();
		if(rsTrns.next()) {
			long  transId = rsTrns.getLong(1);

			if (!transIdInvMap.containsKey(transDate)) {
				transIdInvMap.put(transDate,
						new ArrayList<Long>());
			}

			transIdInvMap.get(transDate).add(transId);
			pstmtAgtTrans.setLong(1, transId);
			pstmtAgtTrans.setInt(2, agtUserId);
			pstmtAgtTrans.setInt(3, agtOrgId);
			pstmtAgtTrans.setString(4, "RETAILER");
			pstmtAgtTrans.setInt(5, retOrgId);
			pstmtAgtTrans.setString(6, "OLA_DEPOSIT");
			pstmtAgtTrans.setTimestamp(7,
					fetchTransTimeStamp(transDate));
			pstmtAgtTrans.executeUpdate();

			insAgtDepositPstmt.setLong(1, transId);
			insAgtDepositPstmt.setInt(2, 0);
			insAgtDepositPstmt.setInt(3, agtOrgId);
			insAgtDepositPstmt.setDouble(4, depositAmt);
			insAgtDepositPstmt.setDouble(5, totalRetNetAmt);
			insAgtDepositPstmt.setDouble(6, totalAgtNetAmt);
			insAgtDepositPstmt.setDouble(7, retComm);
			insAgtDepositPstmt.setDouble(8, agtComm);
			insAgtDepositPstmt.setString(9, "CLAIM_BAL");
			insAgtDepositPstmt.setInt(10, walletId);
			insAgtDepositPstmt.setInt(11, retOrgId);
			insAgtDepositPstmt.executeUpdate();

			// update retailer table
			updateRetDepositPstmt.setLong(1, transId); // agent trans
			// Id
			updateRetDepositPstmt.setInt(2, retOrgId);
			updateRetDepositPstmt.setInt(3, walletId);
			updateRetDepositPstmt.setDouble(4, retComm);
			updateRetDepositPstmt.setDouble(5, agtComm);
			int isUpdate = updateRetDepositPstmt.executeUpdate();
			System.out.println("isUpdate"+isUpdate);
			
		}

	}

	pstmt = con.prepareStatement(depositRefundQuery);
	rs = pstmt.executeQuery();
	while (rs.next()) {
		retComm = rs.getDouble("retailer_comm");
		agtComm = rs.getDouble("agent_comm");
		walletId = rs.getInt("wallet_id");
		totalRetNetAmt = rs.getDouble("totalretNetAmt");
		retOrgId = rs.getInt("retailer_org_id");
		agtUserId = rs.getInt("agtUserId");
		agtOrgId = rs.getInt("agtOrgId");
		totalAgtNetAmt = rs.getDouble("totalAgentNetAmt");
		depositAmt = rs.getDouble("depositAmt");
		//totalRetComm = rs.getDouble("totalRetComm");
		//totalAgtComm = rs.getDouble("totalagtComm");
		transDate = rs.getString("transaction_date");
		String tranType = rs.getString("transaction_type");
		if (orgIdAmountMap.containsKey(retOrgId)) {
			retDebitAmount = orgIdAmountMap.get(retOrgId)
					- totalRetNetAmt;
		} else {
			retDebitAmount = totalRetNetAmt;
		}

		orgIdAmountMap.put(retOrgId, retDebitAmount);
		orgIdParentIdMap.put(retOrgId, agtOrgId);

		if (orgIdTransIdInvRetMap.containsKey(retOrgId)) {
			transIdInvRetMap = orgIdTransIdInvRetMap.get(retOrgId);
		} else {
			transIdInvRetMap = new HashMap<String, List<Long>>();
			orgIdTransIdInvRetMap.put(retOrgId, transIdInvRetMap);
		}

		// insert in main transaction table

		pstmtAgt.setString(1, "AGENT");
		pstmtAgt.executeUpdate();
		ResultSet rsTrns = pstmtAgt.getGeneratedKeys();
		if(rsTrns.next()) {
			long transId = rsTrns.getLong(1);

			if (!transIdInvRetMap.containsKey(transDate)) {
				transIdInvRetMap.put(transDate,
						new ArrayList<Long>());
			}

			transIdInvRetMap.get(transDate).add(transId);

			pstmtAgtTrans.setLong(1, transId);
			pstmtAgtTrans.setInt(2, agtUserId);
			pstmtAgtTrans.setDouble(3, agtOrgId);
			pstmtAgtTrans.setString(4, "RETAILER");
			pstmtAgtTrans.setInt(5, retOrgId);
			pstmtAgtTrans.setString(6, tranType);
			pstmtAgtTrans.setTimestamp(7,
					fetchTransTimeStamp(transDate));
			pstmtAgtTrans.executeUpdate();

			insAgtDepositRetPstmt.setLong(1, transId);
			insAgtDepositRetPstmt.setInt(2, 0);
			insAgtDepositRetPstmt.setDouble(3, depositAmt);
			insAgtDepositRetPstmt.setDouble(4, totalRetNetAmt);
			insAgtDepositRetPstmt.setDouble(5, totalAgtNetAmt);
			insAgtDepositRetPstmt.setDouble(6, retComm);
			insAgtDepositRetPstmt.setDouble(7, agtComm);
			insAgtDepositRetPstmt.setInt(8, walletId);
			insAgtDepositRetPstmt.setString(9, "CLAIM_BAL");
			insAgtDepositRetPstmt.setInt(10, retOrgId);
			insAgtDepositRetPstmt.setInt(11, agtOrgId);
			insAgtDepositRetPstmt.executeUpdate();

			// update retailer table
			updateRetDepositRetPstmt.setLong(1, transId); // agent
			// trans Id
			updateRetDepositRetPstmt.setInt(2, retOrgId);
			updateRetDepositRetPstmt.setString(3, transDate);
			updateRetDepositRetPstmt.setString(4, tranType);
			updateRetDepositRetPstmt.setInt(5, walletId);
			updateRetDepositRetPstmt.setDouble(6, retComm);
			updateRetDepositRetPstmt.setDouble(7, agtComm);
			updateRetDepositRetPstmt.executeUpdate();
		}

	}
	
	generateDGReceiptNew(orgIdTransIdInvMap, orgIdParentIdMap,
			"OLA_INVOICE", "OLA_RECEIPT", con);
	generateDGReceiptNew(orgIdTransIdInvRetMap, orgIdParentIdMap,
			"CR_NOTE", "CR_NOTE_CASH", con);
// update retailer balance
Set<Integer> retOrgIdSet = orgIdAmountMap.keySet();

			for (Integer orgId : retOrgIdSet) {

				boolean isValid = OrgCreditUpdation.updateOrganizationBalWithValidate(
						orgIdAmountMap.get(orgId), "TRANSACTION", "OLA_DEPOSIT",orgId,
						orgIdParentIdMap.get(orgId), "RETAILER",
								0, con);
				if (!isValid)
					throw new LMSException();

				OrgCreditUpdation.updateOrganizationBalWithValidate(
						orgIdAmountMap.get(orgId), "CLAIM_BAL", "DEBIT", orgId,
						orgIdParentIdMap.get(orgId), "RETAILER", 0, con);

				/*
				 * OrgCreditUpdation.updateCreditLimitForRetailer(orgId,
				 * "OLA_DEPOSIT", orgIdAmountMap.get(orgId), con);
				 * updateOrgBalance("CLAIM_BAL", orgIdAmountMap.get(orgId),
				 * orgId, "DEBIT", con);
				 */

			}
} 
		catch(Exception e)
		{
			e.printStackTrace();
		}
}		
	public void updateOlaWithdrawlLedgerForRet(Connection con)
	{
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int retOrgId = 0, agtUserId, agtOrgId,walletId;
		String transDate;
		double withdrawlAmt,retComm,agtComm,retDebitAmount,totalRetNetAmt,totalAgtNetAmt;
		try{
			String withdrawlQuery = "select retailer_comm,agent_comm,DATE(transaction_date) 'transaction_date',wallet_id,a.retailer_org_id,agtUserId,agtOrgId,sum(withdrawl_Amt) withdrawlAmt,sum(net_amt) totalretNetAmt,sum(agent_net_amt) totalAgentNetAmt from st_ola_ret_withdrawl a inner join  st_lms_retailer_transaction_master b inner join (select um.user_id retUserId,um.organization_id retOrgId,um.parent_user_id agtUserId,om.parent_id agtOrgId from st_lms_organization_master om inner join st_lms_user_master um on om.organization_id=um.organization_id where om.organization_type='RETAILER') d on a.transaction_id = b.transaction_id and a.retailer_org_id=retOrgId  where claim_status='CLAIM_BAL' group by a.retailer_org_id,a.wallet_id,DATE(b.transaction_id),retailer_comm,agent_comm order by retailer_org_id,wallet_id,transaction_date";
			String withdrawlRefundQuery = "select wallet_id,sum(net_amt) totalretNetAmt,sum(agent_net_amt) totalAgentNetAmt,a.retailer_org_id,agtUserId,agtOrgId,sum(withdrawl_amt) withdrawlAmt,retailer_comm,agent_comm,DATE(transaction_date) 'transaction_date',transaction_type from st_ola_ret_withdrawl_refund a inner join  st_lms_retailer_transaction_master b inner join (select um.user_id retUserId,um.organization_id retOrgId,um.parent_user_id agtUserId,om.parent_id agtOrgId from st_lms_organization_master om inner join st_lms_user_master um on om.organization_id=um.organization_id where om.organization_type='RETAILER') d on a.transaction_id = b.transaction_id and a.retailer_org_id=retOrgId  where claim_status='CLAIM_BAL' group by a.retailer_org_id,DATE(transaction_date),transaction_type,wallet_id,retailer_comm,agent_comm order by retailer_org_id,transaction_date";
			Map<Integer, Double> orgIdAmountMap = new HashMap<Integer, Double>();
			Map<Integer, Integer> orgIdParentIdMap = new HashMap<Integer, Integer>();
			Map<Integer, Map<String, List<Long>>> orgIdTransIdInvMap = new HashMap<Integer, Map<String, List<Long>>>();
			Map<String, List<Long>> transIdInvMap = null;
			Map<Integer, Map<String, List<Long>>> orgIdTransIdInvRetMap = new HashMap<Integer, Map<String, List<Long>>>();
			Map<String, List<Long>> transIdInvRetMap = null;
			PreparedStatement pstmtAgt = con.prepareStatement(QueryManager
					.insertInLMSTransactionMaster());
			PreparedStatement pstmtAgtTrans = con.prepareStatement(QueryManager
					.insertInAgentTransactionMaster());
			pstmt = con.prepareStatement(withdrawlQuery);
			PreparedStatement insAgtWithdrawlPstmt = con
			.prepareStatement("insert into st_ola_agt_withdrawl(transaction_id, bo_ref_transaction_id, agent_org_id, withdrawl_amt, net_amt,bo_net_amt,retailer_comm, agent_comm, claim_status, wallet_id, retailer_org_id) values(?,?,?,?,?,?,?,?,?,?,?)");
	PreparedStatement updateRetWithdrawlPstmt = con
			.prepareStatement("update st_ola_ret_withdrawl a inner join  st_lms_retailer_transaction_master b  on a.transaction_id = b.transaction_id  set claim_status='DONE_CLAIM',agent_ref_transaction_id=? where a.retailer_org_id=? and wallet_id=? and retailer_comm=? and agent_comm=?");
	PreparedStatement insAgtWithdrawlRetPstmt = con
			.prepareStatement("insert into st_ola_agt_withdrawl_refund (transaction_id, bo_ref_transaction_id, withdrawl_amt, net_amt,bo_net_amt,retailer_comm,agent_comm ,wallet_id, status, retailer_org_id, agent_org_id) values(?,?,?,?,?,?,?,?,?,?,?)");
	PreparedStatement updateRetWithdrawlRetPstmt = con
			.prepareStatement("update st_ola_ret_withdrawl_refund a inner join  st_lms_retailer_transaction_master b on a.transaction_id = b.transaction_id  set claim_status='DONE_CLAIM',agent_ref_transaction_id=? where a.retailer_org_id=? and DATE(transaction_date)=? and transaction_type=? and wallet_id=? and retailer_comm=? and agent_comm=?");
			rs = pstmt.executeQuery();
			while(rs.next())
			{
				transDate = rs.getString("transaction_date");
				walletId = rs.getInt("wallet_id");
				retOrgId = rs.getInt("retailer_org_id");
				agtUserId = rs.getInt("agtUserId");
				agtOrgId = rs.getInt("agtOrgId");
				withdrawlAmt = rs.getDouble("withdrawlAmt");
				totalRetNetAmt = rs.getDouble("totalretNetAmt");
				
				totalAgtNetAmt = rs.getDouble("totalAgentNetAmt");
				//totalRetComm = rs.getDouble("retailerComm");
				//totalAgtComm = rs.getDouble("agentComm");
				retComm = rs.getDouble("retailer_comm");
				agtComm = rs.getDouble("agent_comm");
			

		if (orgIdAmountMap.containsKey(retOrgId)) {
			retDebitAmount = orgIdAmountMap.get(retOrgId)
					+ totalRetNetAmt;
		} else {
			retDebitAmount = totalRetNetAmt;
		}

		orgIdAmountMap.put(retOrgId, retDebitAmount);
		orgIdParentIdMap.put(retOrgId, agtOrgId);

		if (orgIdTransIdInvMap.containsKey(retOrgId)) {
			transIdInvMap = orgIdTransIdInvMap.get(retOrgId);
		} else {
			transIdInvMap = new HashMap<String, List<Long>>();
			orgIdTransIdInvMap.put(retOrgId, transIdInvMap);
		}

		// insert in main transaction table

		pstmtAgt.setString(1, "AGENT");
		pstmtAgt.executeUpdate();
		ResultSet rsTrns = pstmtAgt.getGeneratedKeys();
		if(rsTrns.next()) {
			long transId = rsTrns.getLong(1);

			if (!transIdInvMap.containsKey(transDate)) {
				transIdInvMap.put(transDate,
						new ArrayList<Long>());
			}

			transIdInvMap.get(transDate).add(transId);
			pstmtAgtTrans.setLong(1, transId);
			pstmtAgtTrans.setInt(2, agtUserId);
			pstmtAgtTrans.setInt(3, agtOrgId);
			pstmtAgtTrans.setString(4, "RETAILER");
			pstmtAgtTrans.setInt(5, retOrgId);
			pstmtAgtTrans.setString(6, "OLA_WITHDRAWL");
			pstmtAgtTrans.setTimestamp(7,
					fetchTransTimeStamp(transDate));
			pstmtAgtTrans.executeUpdate();

			insAgtWithdrawlPstmt.setLong(1, transId);
			insAgtWithdrawlPstmt.setInt(2, 0);
			insAgtWithdrawlPstmt.setInt(3, agtOrgId);
			insAgtWithdrawlPstmt.setDouble(4, withdrawlAmt);
			insAgtWithdrawlPstmt.setDouble(5, totalRetNetAmt);
			insAgtWithdrawlPstmt.setDouble(6, totalAgtNetAmt);
			insAgtWithdrawlPstmt.setDouble(7, retComm);
			insAgtWithdrawlPstmt.setDouble(8, agtComm);
			insAgtWithdrawlPstmt.setString(9, "CLAIM_BAL");
			insAgtWithdrawlPstmt.setInt(10, walletId);
			insAgtWithdrawlPstmt.setInt(11, retOrgId);
			insAgtWithdrawlPstmt.executeUpdate();

			// update retailer table
			updateRetWithdrawlPstmt.setLong(1, transId); // agent trans
			// Id
			updateRetWithdrawlPstmt.setInt(2, retOrgId);
			updateRetWithdrawlPstmt.setInt(3, walletId);
			updateRetWithdrawlPstmt.setDouble(4, retComm);
			updateRetWithdrawlPstmt.setDouble(5, agtComm);
			updateRetWithdrawlPstmt.executeUpdate();
			
		}

	}

	pstmt = con.prepareStatement(withdrawlRefundQuery);
	rs = pstmt.executeQuery();
	while (rs.next()) {
		retComm = rs.getDouble("retailer_comm");
		agtComm = rs.getDouble("agent_comm");
		walletId = rs.getInt("wallet_id");
		totalRetNetAmt = rs.getDouble("totalretNetAmt");
		retOrgId = rs.getInt("retailer_org_id");
		agtUserId = rs.getInt("agtUserId");
		agtOrgId = rs.getInt("agtOrgId");
		withdrawlAmt = rs.getDouble("withdrawlAmt");
		totalAgtNetAmt = rs.getDouble("totalAgentNetAmt");
		//	totalRetComm = rs.getDouble("totalRetComm");
		//totalAgtComm = rs.getDouble("totalagtComm");
		transDate = rs.getString("transaction_date");
		String tranType = rs.getString("transaction_type");
		if (orgIdAmountMap.containsKey(retOrgId)) {
			retDebitAmount = orgIdAmountMap.get(retOrgId)
					- totalRetNetAmt;
		} else {
			retDebitAmount = totalRetNetAmt;
		}

		orgIdAmountMap.put(retOrgId, retDebitAmount);
		orgIdParentIdMap.put(retOrgId, agtOrgId);

		if (orgIdTransIdInvRetMap.containsKey(retOrgId)) {
			transIdInvRetMap = orgIdTransIdInvRetMap.get(retOrgId);
		} else {
			transIdInvRetMap = new HashMap<String, List<Long>>();
			orgIdTransIdInvRetMap.put(retOrgId, transIdInvRetMap);
		}

		// insert in main transaction table

		pstmtAgt.setString(1, "AGENT");
		pstmtAgt.executeUpdate();
		ResultSet rsTrns = pstmtAgt.getGeneratedKeys();
		if(rsTrns.next()) {
			long transId = rsTrns.getLong(1);

			if (!transIdInvRetMap.containsKey(transDate)) {
				transIdInvRetMap.put(transDate,
						new ArrayList<Long>());
			}

			transIdInvRetMap.get(transDate).add(transId);

			pstmtAgtTrans.setLong(1, transId);
			pstmtAgtTrans.setInt(2, agtUserId);
			pstmtAgtTrans.setDouble(3, agtOrgId);
			pstmtAgtTrans.setString(4, "RETAILER");
			pstmtAgtTrans.setInt(5, retOrgId);
			pstmtAgtTrans.setString(6, tranType);
			pstmtAgtTrans.setTimestamp(7,
					fetchTransTimeStamp(transDate));
			pstmtAgtTrans.executeUpdate();

			insAgtWithdrawlRetPstmt.setLong(1, transId);
			insAgtWithdrawlRetPstmt.setInt(2, 0);
			insAgtWithdrawlRetPstmt.setDouble(3, withdrawlAmt);
			insAgtWithdrawlRetPstmt.setDouble(4, totalRetNetAmt);
			insAgtWithdrawlRetPstmt.setDouble(5, totalAgtNetAmt);
			insAgtWithdrawlRetPstmt.setDouble(6, retComm);
			insAgtWithdrawlRetPstmt.setDouble(7, agtComm);
			insAgtWithdrawlRetPstmt.setInt(8, walletId);
			insAgtWithdrawlRetPstmt.setString(9, "CLAIM_BAL");
			insAgtWithdrawlRetPstmt.setInt(10, retOrgId);
			insAgtWithdrawlRetPstmt.setInt(11, agtOrgId);
			insAgtWithdrawlRetPstmt.executeUpdate();

			// update retailer table
			updateRetWithdrawlRetPstmt.setLong(1, transId); // agent
			// trans Id
			updateRetWithdrawlRetPstmt.setInt(2, retOrgId);
			updateRetWithdrawlRetPstmt.setString(3, transDate);
			updateRetWithdrawlRetPstmt.setString(4, tranType);
			updateRetWithdrawlRetPstmt.setInt(5, walletId);
			updateRetWithdrawlRetPstmt.setDouble(6, retComm);
			updateRetWithdrawlRetPstmt.setDouble(7, agtComm);
			updateRetWithdrawlRetPstmt.executeUpdate();
		}

	}
// update retailer balance
Set<Integer> retOrgIdSet = orgIdAmountMap.keySet();
			for (Integer orgId : retOrgIdSet) {

				boolean isValid = OrgCreditUpdation
						.updateOrganizationBalWithValidate(orgIdAmountMap
								.get(orgId), "TRANSACTION", "OLA_WITHDRAWL",
								orgId, orgIdParentIdMap.get(orgId), "RETAILER",
								0, con);
				if (!isValid)
					throw new LMSException();

				OrgCreditUpdation.updateOrganizationBalWithValidate(
						orgIdAmountMap.get(orgId), "CLAIM_BAL", "CREDIT",
						orgId, orgIdParentIdMap.get(orgId), "RETAILER", 0, con);
				/*
				 * OrgCreditUpdation.updateCreditLimitForRetailer(orgId,
				 * "OLA_WITHDRAWL", orgIdAmountMap.get(orgId), con);
				 * 
				 * updateOrgBalance("CLAIM_BAL", orgIdAmountMap.get(orgId),
				 * orgId, "CREDIT", con);
				 */
				generateReciptForPwtNew(orgIdTransIdInvMap.get(orgId), con,
						orgIdParentIdMap.get(orgId), retOrgId, "RETAILER",
						"OLA");

			}
} 
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public void updateOlaDepositLedgerForAgt(Connection con)
	{
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		double depositAmt, totalAgtNetAmt;
		double agentComm,agtDebitAmount;
		String transDate;
		int agtOrgId, boUserId, boOrgId;
		Map<Integer, Double> orgIdAmountMap = new HashMap<Integer, Double>();
		Map<Integer, Integer> orgIdParentIdMap = new HashMap<Integer, Integer>();
		Map<Integer, Map<String, List<Long>>> orgIdTransIdInvMap = new HashMap<Integer, Map<String, List<Long>>>();
		Map<String, List<Long>> transIdInvMap = null;
		Map<Integer, Map<String, List<Long>>> orgIdTransIdInvRetMap = new HashMap<Integer, Map<String, List<Long>>>();
		Map<String, List<Long>> transIdInvRetMap = null;
		try {
			String depositQry = "select agent_org_id,boUserId,boOrgId,depositAmt,bo_net_amt,wallet_id,transaction_date,agent_comm from(select a.agent_org_id,sum(deposit_amt) depositAmt,sum(bo_net_amt) bo_net_amt ,a.wallet_id,DATE(transaction_date) 'transaction_date',agent_comm from ((select agent_org_id,deposit_amt,bo_net_amt ,wallet_id,transaction_id,agent_comm from st_ola_agt_deposit where claim_status='CLAIM_BAL') union all (select agent_org_id,deposit_amt,net_amt bo_net_amt,wallet_id,transaction_id,agt_claim_comm from st_ola_agt_direct_plr_deposit where deposit_claim_status='CLAIM_BAL')) a inner join  st_lms_agent_transaction_master b  on a.transaction_id = b.transaction_id   group by a.agent_org_id,DATE(transaction_date),a.wallet_id,agent_comm) deposit inner join (select um.user_id agtUserId,um.organization_id agtOrgId,um.parent_user_id boUserId,om.parent_id boOrgId,isrolehead from st_lms_organization_master om inner join st_lms_user_master um on om.organization_id=um.organization_id where om.organization_type='AGENT') om on agent_org_id=agtOrgId and isrolehead = 'Y'";
			String depositRefundQry = "select agent_org_id,boUserId,boOrgId,depositAmt,bo_net_amt,wallet_id,transaction_date,agent_comm,transaction_type from(select a.agent_org_id,sum(deposit_amt) depositAmt ,sum(bo_net_amt) bo_net_amt,a.wallet_id,DATE(transaction_date) 'transaction_date',agent_comm,transaction_type from ((select agent_org_id,deposit_amt,bo_net_amt ,wallet_id,transaction_id,agent_comm from st_ola_agt_deposit_refund where status='CLAIM_BAL') union all (select agent_org_id,deposit_amt,net_amt bo_net_amt,wallet_id,transaction_id,agt_claim_comm from st_ola_agt_direct_plr_deposit_refund where deposit_claim_status='CLAIM_BAL')) a inner join  st_lms_agent_transaction_master b on a.transaction_id = b.transaction_id   group by a.agent_org_id,DATE(transaction_date),a.wallet_id,transaction_type,agent_comm order by agent_org_id,wallet_id,transaction_date) depositRefund inner join (select um.user_id agtUserId,um.organization_id agtOrgId,um.parent_user_id boUserId,om.parent_id boOrgId,isrolehead from st_lms_organization_master om inner join st_lms_user_master um on om.organization_id=um.organization_id where om.organization_type='AGENT') d on agent_org_id=agtOrgId and isrolehead = 'Y'";
			PreparedStatement pstmtBO = con.prepareStatement(QueryManager.insertInLMSTransactionMaster());
			PreparedStatement insBoTranPstmt = con.prepareStatement(QueryManager.insertInBOTransactionMaster());
			PreparedStatement insBoDepositPstmt = con
					.prepareStatement("insert into st_ola_bo_deposit (transaction_id, deposit_amt, net_amt, wallet_id, agent_org_id, agent_comm) values(?,?,?,?,?,?)");
			PreparedStatement updAgtDepositpstmt = con
					.prepareStatement("update st_ola_agt_deposit a inner join st_lms_agent_transaction_master b on a.transaction_id = b.transaction_id set claim_status='DONE_CLAIM',bo_ref_transaction_id=? where a.agent_org_id=?  and agent_comm=? and DATE(transaction_date)=? and wallet_id=?");
			PreparedStatement updAgtDirPlrDepositpstmt = con
			.prepareStatement("update st_ola_agt_direct_plr_deposit a inner join st_lms_agent_transaction_master b on a.transaction_id = b.transaction_id set deposit_claim_status='DONE_CLAIM',bo_ref_transaction_id=? where a.agent_org_id=?  and agt_claim_comm=? and DATE(transaction_date)=? and wallet_id=?");
	
			
			
			PreparedStatement insBoDepositRetpstmt = con
					.prepareStatement("insert into st_ola_bo_deposit_refund(transaction_id, agent_org_id, deposit_amt, net_amt, agent_comm, wallet_id)	values(?,?,?,?,?,?)");
			PreparedStatement updAgtDepositRetPstmt = con
					.prepareStatement("update st_ola_agt_deposit_refund a inner join st_lms_agent_transaction_master b on a.transaction_id = b.transaction_id set status='DONE_CLAIM',bo_ref_transaction_id=? where a.agent_org_id=? and agent_comm=? and DATE(transaction_date)=? and transaction_type=? and wallet_id=?");
			PreparedStatement updAgtDirPlrDepositRefpstmt = con
			.prepareStatement("update st_ola_agt_direct_plr_deposit_refund a inner join st_lms_agent_transaction_master b on a.transaction_id = b.transaction_id set deposit_claim_status='DONE_CLAIM',bo_ref_transaction_id=? where a.agent_org_id=? and agt_claim_comm=? and DATE(transaction_date)=? and transaction_type=? and wallet_id=?");

		
			int walletId = 0;
			pstmt = con.prepareStatement(depositQry);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				agtOrgId = rs.getInt("agent_org_id");
				boUserId = rs.getInt("boUserId");
				boOrgId = rs.getInt("boOrgId");
				walletId = rs.getInt("wallet_id");
				depositAmt = rs.getDouble("depositAmt");
				//retailerComm = rs.getDouble("retailer_comm");
				agentComm = rs.getDouble("agent_comm");
				transDate = rs.getString("transaction_date");
				totalAgtNetAmt=rs.getDouble("bo_net_amt");
				//totalAgtNetAmt = (depositAmt - (depositAmt*agentComm*.01));
				
				if (orgIdAmountMap.containsKey(agtOrgId)) {
					agtDebitAmount = orgIdAmountMap.get(agtOrgId)
							+ totalAgtNetAmt;
				} else {
					agtDebitAmount = totalAgtNetAmt;
				}

				orgIdAmountMap.put(agtOrgId, agtDebitAmount);
				orgIdParentIdMap.put(agtOrgId, boOrgId);

				if (orgIdTransIdInvMap.containsKey(agtOrgId)) {
					transIdInvMap = orgIdTransIdInvMap.get(agtOrgId);
				} else {
					transIdInvMap = new HashMap<String, List<Long>>();
					orgIdTransIdInvMap.put(agtOrgId, transIdInvMap);
				}

				// insert in main transaction table

				pstmtBO.setString(1, "BO");
				pstmtBO.executeUpdate();
				ResultSet rsTrns = pstmtBO.getGeneratedKeys();
				if (rsTrns.next()) {
					long transId = rsTrns.getLong(1);

					if (!transIdInvMap.containsKey(transDate)) {
						transIdInvMap.put(transDate, new ArrayList<Long>());
					}

					transIdInvMap.get(transDate).add(transId);

					insBoTranPstmt.setLong(1, transId);
					insBoTranPstmt.setInt(2, boUserId);
					insBoTranPstmt.setInt(3, boOrgId);
					insBoTranPstmt.setString(4, "AGENT");
					insBoTranPstmt.setInt(5, agtOrgId);
					insBoTranPstmt.setTimestamp(6,
							fetchTransTimeStamp(transDate));
					insBoTranPstmt.setString(7, "OLA_DEPOSIT");
					insBoTranPstmt.executeUpdate();

					insBoDepositPstmt.setLong(1, transId);
					insBoDepositPstmt.setDouble(2, depositAmt);
					insBoDepositPstmt.setDouble(3, totalAgtNetAmt);
					insBoDepositPstmt.setInt(4, walletId);
					insBoDepositPstmt.setInt(5, agtOrgId);
					insBoDepositPstmt.setDouble(6, agentComm);
					insBoDepositPstmt.executeUpdate();

					// update retupdateOlaDepositLedgerForAgtailer table

					updAgtDepositpstmt.setLong(1, transId); // agent trans Id
					updAgtDepositpstmt.setInt(2, agtOrgId);
					updAgtDepositpstmt.setDouble(3, agentComm);
					updAgtDepositpstmt.setString(4, transDate);
					updAgtDepositpstmt.setInt(5, walletId);
					updAgtDepositpstmt.executeUpdate();
					// update agent direct player deposit 
					updAgtDirPlrDepositpstmt.setLong(1, transId); // agent trans Id
					updAgtDirPlrDepositpstmt.setInt(2, agtOrgId);
					updAgtDirPlrDepositpstmt.setDouble(3, agentComm);
					updAgtDirPlrDepositpstmt.setString(4, transDate);
					updAgtDirPlrDepositpstmt.setInt(5, walletId);
					updAgtDirPlrDepositpstmt.executeUpdate();
					
					
				}
			}

			pstmt = con.prepareStatement(depositRefundQry);
			rs = pstmt.executeQuery();
			while (rs.next()) {

				agtOrgId = rs.getInt("agent_org_id");
				boUserId = rs.getInt("boUserId");
				boOrgId = rs.getInt("boOrgId");
				walletId = rs.getInt("wallet_id");
				depositAmt = rs.getDouble("depositAmt");
				agentComm = rs.getDouble("agent_comm");
				///	retailerComm = rs.getDouble("retailer_comm");
				transDate = rs.getString("transaction_date");
				totalAgtNetAmt =rs.getDouble("bo_net_amt");
				//totalAgtNetAmt = depositAmt - (depositAmt*agentComm*.01);
				String tranType = rs.getString("transaction_type");

				if (orgIdAmountMap.containsKey(agtOrgId)) {
					agtDebitAmount = orgIdAmountMap.get(agtOrgId)
							- totalAgtNetAmt;
				} else {
					agtDebitAmount = totalAgtNetAmt;
				}

				orgIdAmountMap.put(agtOrgId, agtDebitAmount);
				orgIdParentIdMap.put(agtOrgId, boOrgId);

				if (orgIdTransIdInvRetMap.containsKey(agtOrgId)) {
					transIdInvRetMap = orgIdTransIdInvRetMap.get(agtOrgId);
				} else {
					transIdInvRetMap = new HashMap<String, List<Long>>();
					orgIdTransIdInvRetMap.put(agtOrgId, transIdInvRetMap);
				}

				// insert in main transaction table

				pstmtBO.setString(1, "BO");
				pstmtBO.executeUpdate();
				ResultSet rsTrns = pstmtBO.getGeneratedKeys();
				if (rsTrns.next()) {
					long transId = rsTrns.getLong(1);

					if (!transIdInvRetMap.containsKey(transDate)) {
						transIdInvRetMap.put(transDate,
								new ArrayList<Long>());
					}

					transIdInvRetMap.get(transDate).add(transId);

					insBoTranPstmt.setLong(1, transId);
					insBoTranPstmt.setInt(2, boUserId);
					insBoTranPstmt.setInt(3, boOrgId);
					insBoTranPstmt.setString(4, "AGENT");
					insBoTranPstmt.setInt(5, agtOrgId);
					insBoTranPstmt.setTimestamp(6,
							fetchTransTimeStamp(transDate));
					insBoTranPstmt.setString(7, tranType);
					insBoTranPstmt.executeUpdate();

					insBoDepositRetpstmt.setLong(1, transId);
					insBoDepositRetpstmt.setInt(2, agtOrgId);
					insBoDepositRetpstmt.setDouble(3, depositAmt);
					insBoDepositRetpstmt.setDouble(4, totalAgtNetAmt);
					insBoDepositRetpstmt.setDouble(5, agentComm);
					insBoDepositRetpstmt.setInt(6, walletId);
					insBoDepositRetpstmt.executeUpdate();

					// update retailer table

					updAgtDepositRetPstmt.setLong(1, transId); // agent trans Id
					updAgtDepositRetPstmt.setInt(2, agtOrgId);
					updAgtDepositRetPstmt.setDouble(3, agentComm);
					updAgtDepositRetPstmt.setString(4, transDate);
					updAgtDepositRetPstmt.setString(5, tranType);
					updAgtDepositRetPstmt.setInt(6, walletId);
					 updAgtDepositRetPstmt.executeUpdate();
					 
					// update agent direct player deposit refund table

					 updAgtDirPlrDepositRefpstmt.setLong(1, transId); // agent trans Id
					 updAgtDirPlrDepositRefpstmt.setInt(2, agtOrgId);
					 updAgtDirPlrDepositRefpstmt.setDouble(3, agentComm);
					 updAgtDirPlrDepositRefpstmt.setString(4, transDate);
					 updAgtDirPlrDepositRefpstmt.setString(5, tranType);
					 updAgtDirPlrDepositRefpstmt.setInt(6, walletId);
					 updAgtDirPlrDepositRefpstmt.executeUpdate();
					 
					 
				}
			}
			generateDGReceiptForBONew(orgIdTransIdInvMap, orgIdParentIdMap,
					"OLA_INVOICE", "OLA_RECEIPT", con);
			generateDGReceiptForBONew(orgIdTransIdInvRetMap, orgIdParentIdMap,
					"CR_NOTE", "CR_NOTE_CASH", con);
			// update agent balance
			Set<Integer> agtOrgIdSet = orgIdAmountMap.keySet();
			for (Integer orgId : agtOrgIdSet) {
			
				boolean isValid = OrgCreditUpdation.updateOrganizationBalWithValidate(
						orgIdAmountMap.get(orgId), "TRANSACTION", "OLA_DEPOSIT",orgId,
						orgIdParentIdMap.get(orgId), "AGENT",
								0, con);
				if (!isValid)
					throw new LMSException();

				OrgCreditUpdation.updateOrganizationBalWithValidate(
						orgIdAmountMap.get(orgId), "CLAIM_BAL", "DEBIT", orgId,
						orgIdParentIdMap.get(orgId), "AGENT", 0, con);

				/*
				 	OrgCreditUpdation.updateCreditLimitForAgent(orgId,
						"OLA_DEPOSIT", orgIdAmountMap.get(orgId), con);
				updateOrgBalance("CLAIM_BAL", orgIdAmountMap.get(orgId), orgId,
						"DEBIT", con);
				 */
				
				
				
				
				
				
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	public void updateOlaWithdrawlLedgerForAgt(Connection con)
	{

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		double withdrawlAmt, totalAgtNetAmt;
		double agentComm,retailerComm, agtDebitAmount;
		String transDate;
		int agtOrgId, boUserId, boOrgId;
		Map<Integer, Double> orgIdAmountMap = new HashMap<Integer, Double>();
		Map<Integer, Integer> orgIdParentIdMap = new HashMap<Integer, Integer>();
		Map<Integer, Map<String, List<Long>>> orgIdTransIdInvMap = new HashMap<Integer, Map<String, List<Long>>>();
		Map<String, List<Long>> transIdInvMap = null;
		Map<Integer, Map<String, List<Long>>> orgIdTransIdInvRetMap = new HashMap<Integer, Map<String, List<Long>>>();
		Map<String, List<Long>> transIdInvRetMap = null;
		try {
			String withdrawlQry = "select agent_org_id,boUserId,boOrgId,withdrawlAmt,wallet_id,transaction_date,agent_comm from(select a.agent_org_id,sum(withdrawl_amt) withdrawlAmt,sum(bo_net_amt) bo_net_amt ,a.wallet_id,DATE(transaction_date) 'transaction_date',agent_comm from ((select agent_org_id ,withdrawl_amt,bo_net_amt ,wallet_id,transaction_id,agent_comm from st_ola_agt_withdrawl where claim_status='CLAIM_BAL') union all (select agent_org_id agent_org_id,withdrawl_amt,net_amt bo_net_amt,wallet_id,transaction_id,agt_claim_comm from st_ola_agt_direct_plr_withdrawl where withdrawl_claim_status='CLAIM_BAL')) a inner join  st_lms_agent_transaction_master b  on a.transaction_id = b.transaction_id   group by a.agent_org_id,DATE(transaction_date),a.wallet_id,agent_comm) withdrawl inner join (select um.user_id agtUserId,um.organization_id agtOrgId,um.parent_user_id boUserId,om.parent_id boOrgId,isrolehead from st_lms_organization_master om inner join st_lms_user_master um on om.organization_id=um.organization_id where om.organization_type='AGENT') om on agent_org_id=agtOrgId and isrolehead = 'Y'";
			String withdrawlRefundQry = "select agent_org_id,boUserId,boOrgId,withdrawlRefAmt,wallet_id,transaction_date,agent_comm from(select a.agent_org_id,sum(withdrawl_amt) withdrawlRefAmt,sum(bo_net_amt) bo_net_amt ,a.wallet_id,DATE(transaction_date) 'transaction_date',agent_comm from ((select agent_org_id ,withdrawl_amt,bo_net_amt ,wallet_id,transaction_id,agent_comm from st_ola_agt_withdrawl_refund where status='CLAIM_BAL') union all (select agt_org_id agent_org_id,withdrawl_amt,agent_net_amt bo_net_amt,wallet_id,transaction_id,agent_comm from st_ola_agt_direct_plr_withdrawl_refund where claim_status='CLAIM_BAL')) a inner join  st_lms_agent_transaction_master b  on a.transaction_id = b.transaction_id   group by a.agent_org_id,DATE(transaction_date),a.wallet_id,agent_comm) withdrawl inner join (select um.user_id agtUserId,um.organization_id agtOrgId,um.parent_user_id boUserId,om.parent_id boOrgId,isrolehead from st_lms_organization_master om inner join st_lms_user_master um on om.organization_id=um.organization_id where om.organization_type='AGENT') om on agent_org_id=agtOrgId and isrolehead = 'Y'";
			PreparedStatement pstmtBO = con.prepareStatement(QueryManager.insertInLMSTransactionMaster());
			PreparedStatement insBoTranPstmt = con.prepareStatement(QueryManager.insertInBOTransactionMaster());
			PreparedStatement insBoWithdrawlPstmt = con.prepareStatement("insert into st_ola_bo_withdrawl (transaction_id, withdrawl_amt, net_amt, wallet_id, agent_org_id, agent_comm) values(?,?,?,?,?,?)");
			PreparedStatement updAgtWithdrawlpstmt = con.prepareStatement("update st_ola_agt_withdrawl a inner join st_lms_agent_transaction_master b on a.transaction_id = b.transaction_id set claim_status='DONE_CLAIM',bo_ref_transaction_id=? where a.agent_org_id=? and agent_comm=? and DATE(transaction_date)=? and wallet_id=?");
			PreparedStatement updAgtDirectPlrWithdrawlpstmt = con.prepareStatement("update st_ola_agt_direct_plr_withdrawl a inner join st_lms_agent_transaction_master b on a.transaction_id = b.transaction_id set withdrawl_claim_status='DONE_CLAIM',bo_ref_transaction_id=? where a.agent_org_id=? and agt_claim_comm=? and DATE(transaction_date)=? and wallet_id=?");
			PreparedStatement insBoWithdrawlRetpstmt = con.prepareStatement("insert into st_ola_bo_withdrawl_refund(transaction_id, agent_org_id, withdrawl_amt, net_amt, agent_comm, wallet_id)	values(?,?,?,?,?,?)");
			PreparedStatement updAgtwithdrawlRetPstmt = con.prepareStatement("update st_ola_agt_withdrawl_refund a inner join st_lms_agent_transaction_master b on a.transaction_id = b.transaction_id set status='DONE_CLAIM',bo_ref_transaction_id=? where a.agent_org_id=? and agent_comm=? and DATE(transaction_date)=? and transaction_type=? and wallet_id=?");
			PreparedStatement updAgtDirectPlrWithdrawlRefpstmt = con.prepareStatement("update st_ola_agt_direct_plr_withdrawl_refund a inner join st_lms_agent_transaction_master b on a.transaction_id = b.transaction_id set status='DONE_CLAIM',bo_ref_transaction_id=? where a.agt_org_id=? and agent_comm=? and DATE(transaction_date)=? and transaction_type=? and wallet_id=?");

			int walletId = 0;
			pstmt = con.prepareStatement(withdrawlQry);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				agtOrgId = rs.getInt("agent_org_id");
				boUserId = rs.getInt("boUserId");
				boOrgId = rs.getInt("boOrgId");
				walletId = rs.getInt("wallet_id");
				withdrawlAmt = rs.getDouble("withdrawlAmt");
				//retailerComm = rs.getDouble("retailer_comm");
				agentComm = rs.getDouble("agent_comm");
				transDate = rs.getString("transaction_date");

				//totalAgtNetAmt = (withdrawlAmt + (withdrawlAmt*agentComm*.01));
				
				totalAgtNetAmt =rs.getDouble("withdrawlAmt");
				if (orgIdAmountMap.containsKey(agtOrgId)) {
					agtDebitAmount = orgIdAmountMap.get(agtOrgId)
							+ totalAgtNetAmt;
				} else {
					agtDebitAmount = totalAgtNetAmt;
				}

				orgIdAmountMap.put(agtOrgId, agtDebitAmount);
				orgIdParentIdMap.put(agtOrgId, boOrgId);

				if (orgIdTransIdInvMap.containsKey(agtOrgId)) {
					transIdInvMap = orgIdTransIdInvMap.get(agtOrgId);
				} else {
					transIdInvMap = new HashMap<String, List<Long>>();
					orgIdTransIdInvMap.put(agtOrgId, transIdInvMap);
				}

				// insert in main transaction table

				pstmtBO.setString(1, "BO");
				pstmtBO.executeUpdate();
				ResultSet rsTrns = pstmtBO.getGeneratedKeys();
				if (rsTrns.next()) {
					long transId = rsTrns.getLong(1);

					if (!transIdInvMap.containsKey(transDate)) {
						transIdInvMap.put(transDate, new ArrayList<Long>());
					}

					transIdInvMap.get(transDate).add(transId);

					insBoTranPstmt.setLong(1, transId);
					insBoTranPstmt.setInt(2, boUserId);
					insBoTranPstmt.setInt(3, boOrgId);
					insBoTranPstmt.setString(4, "AGENT");
					insBoTranPstmt.setInt(5, agtOrgId);
					insBoTranPstmt.setTimestamp(6,
							fetchTransTimeStamp(transDate));
					insBoTranPstmt.setString(7, "OLA_WITHDRAWL");
					insBoTranPstmt.executeUpdate();

					insBoWithdrawlPstmt.setLong(1, transId);
					insBoWithdrawlPstmt.setDouble(2, withdrawlAmt);
					insBoWithdrawlPstmt.setDouble(3, totalAgtNetAmt);
					insBoWithdrawlPstmt.setInt(4, walletId);
					insBoWithdrawlPstmt.setInt(5, agtOrgId);
					insBoWithdrawlPstmt.setDouble(6, agentComm);
					insBoWithdrawlPstmt.executeUpdate();

					// update Agent table

					updAgtWithdrawlpstmt.setLong(1, transId); // agent trans Id
					updAgtWithdrawlpstmt.setInt(2, agtOrgId);
					updAgtWithdrawlpstmt.setDouble(3, agentComm);
					updAgtWithdrawlpstmt.setString(4, transDate);
					updAgtWithdrawlpstmt.setInt(5, walletId);
					updAgtWithdrawlpstmt.executeUpdate();
					
					// update Agent Direct Player Withdrawl Table
					updAgtDirectPlrWithdrawlpstmt.setLong(1, transId); // agent trans Id
					updAgtDirectPlrWithdrawlpstmt.setInt(2, agtOrgId);
					updAgtDirectPlrWithdrawlpstmt.setDouble(3, agentComm);
					updAgtDirectPlrWithdrawlpstmt.setString(4, transDate);
					updAgtDirectPlrWithdrawlpstmt.setInt(5, walletId);
					updAgtDirectPlrWithdrawlpstmt.executeUpdate();
					
					
					
					
				}
			}

			pstmt = con.prepareStatement(withdrawlRefundQry);
			rs = pstmt.executeQuery();
			while (rs.next()) {

				agtOrgId = rs.getInt("agent_org_id");
				boUserId = rs.getInt("boUserId");
				boOrgId = rs.getInt("boOrgId");
				walletId = rs.getInt("wallet_id");
				withdrawlAmt = rs.getDouble("withdrawlAmt");
				agentComm = rs.getDouble("agent_comm");
				//	retailerComm = rs.getDouble("retailer_comm");
				transDate = rs.getString("transaction_date");
				totalAgtNetAmt =  rs.getDouble("withdrawlRefAmt");// withdrawlAmt - (withdrawlAmt*agentComm*.01);
				String tranType = rs.getString("transaction_type");

				if (orgIdAmountMap.containsKey(agtOrgId)) {
					agtDebitAmount = orgIdAmountMap.get(agtOrgId)
							- totalAgtNetAmt;
				} else {
					agtDebitAmount = totalAgtNetAmt;
				}

				orgIdAmountMap.put(agtOrgId, agtDebitAmount);
				orgIdParentIdMap.put(agtOrgId, boOrgId);

				if (orgIdTransIdInvRetMap.containsKey(agtOrgId)) {
					transIdInvRetMap = orgIdTransIdInvRetMap.get(agtOrgId);
				} else {
					transIdInvRetMap = new HashMap<String, List<Long>>();
					orgIdTransIdInvRetMap.put(agtOrgId, transIdInvRetMap);
				}

				// insert in main transaction table

				pstmtBO.setString(1, "BO");
				pstmtBO.executeUpdate();
				ResultSet rsTrns = pstmtBO.getGeneratedKeys();
				if (rsTrns.next()) {
					long transId = rsTrns.getLong(1);

					if (!transIdInvRetMap.containsKey(transDate)) {
						transIdInvRetMap.put(transDate,
								new ArrayList<Long>());
					}

					transIdInvRetMap.get(transDate).add(transId);

					insBoTranPstmt.setLong(1, transId);
					insBoTranPstmt.setInt(2, boUserId);
					insBoTranPstmt.setInt(3, boOrgId);
					insBoTranPstmt.setString(4, "AGENT");
					insBoTranPstmt.setInt(5, agtOrgId);
					insBoTranPstmt.setTimestamp(6,
							fetchTransTimeStamp(transDate));
					insBoTranPstmt.setString(7, tranType);
					insBoTranPstmt.executeUpdate();

					insBoWithdrawlRetpstmt.setLong(1, transId);
					insBoWithdrawlRetpstmt.setInt(2, agtOrgId);
					insBoWithdrawlRetpstmt.setDouble(3, withdrawlAmt);
					insBoWithdrawlRetpstmt.setDouble(4, totalAgtNetAmt);
					insBoWithdrawlRetpstmt.setDouble(5, agentComm);
					insBoWithdrawlRetpstmt.setInt(6, walletId);
					insBoWithdrawlRetpstmt.executeUpdate();

					// update agent table

					updAgtwithdrawlRetPstmt.setLong(1, transId); // agent trans Id
					updAgtwithdrawlRetPstmt.setInt(2, agtOrgId);
					updAgtwithdrawlRetPstmt.setDouble(3, agentComm);
					//updAgtwithdrawlRetPstmt.setDouble(4, retailerComm);
					updAgtwithdrawlRetPstmt.setString(4, transDate);
					updAgtwithdrawlRetPstmt.setString(5, tranType);
					updAgtwithdrawlRetPstmt.setInt(6, walletId);
					updAgtwithdrawlRetPstmt.executeUpdate();
					// update agent direct player withdrawl refund table
					updAgtDirectPlrWithdrawlRefpstmt.setLong(1, transId); // agent trans Id
					updAgtDirectPlrWithdrawlRefpstmt.setInt(2, agtOrgId);
					updAgtDirectPlrWithdrawlRefpstmt.setDouble(3, agentComm);
					//updAgtwithdrawlRetPstmt.setDouble(4, retailerComm);
					updAgtDirectPlrWithdrawlRefpstmt.setString(4, transDate);
					updAgtDirectPlrWithdrawlRefpstmt.setString(5, tranType);
					updAgtDirectPlrWithdrawlRefpstmt.setInt(6, walletId);
					updAgtDirectPlrWithdrawlRefpstmt.executeUpdate();
					
					
					
				}
			}
			// update agent balance
			Set<Integer> agtOrgIdSet = orgIdAmountMap.keySet();
			for (Integer orgId : agtOrgIdSet) {
			

				
				boolean isValid = OrgCreditUpdation.updateOrganizationBalWithValidate(
						orgIdAmountMap.get(orgId), "TRANSACTION", "OLA_WITHDRAWL",orgId,
						orgIdParentIdMap.get(orgId), "AGENT",
								0, con);
				if (!isValid)
					throw new LMSException();

				OrgCreditUpdation.updateOrganizationBalWithValidate(
						orgIdAmountMap.get(orgId), "CLAIM_BAL", "CREDIT", orgId,
						orgIdParentIdMap.get(orgId), "AGENT", 0, con);

				/*
				 		OrgCreditUpdation.updateCreditLimitForAgent(orgId,
						"OLA_WITHDRAWL", orgIdAmountMap.get(orgId), con);
				updateOrgBalance("CLAIM_BAL", orgIdAmountMap.get(orgId), orgId,
						"CREDIT", con);
				
				 */
						
				
				generateReceiptOlaBo(con, orgId, "AGENT", orgIdTransIdInvMap
						.get(orgId));
				
				
				
				
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	
	}
	
	
	
	
	public void updateOLACommissionLedgerForRet(Connection con) throws LMSException {
		logger.debug(new Date() + "---start-dg pwt update for retailer--"
				+ new Date().getTime());
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			String walletQry = "select wallet_id,tds_comm_rate from st_ola_wallet_master where wallet_status='ACTIVE'";
			String CommQry = "select wallet_id,retailer_user_id,sum(retailer_net_claim_comm)retailer_net_claim_comm,sum(retailer_claim_comm) retailer_claim_comm,tds_comm_rate,sum(agt_claim_comm)agt_claim_comm,transaction_date,transaction_id,agtUserId,agtOrgId,retOrgId from( select wallet_id,rc.retailer_user_id,retailer_net_claim_comm,retailer_claim_comm,tds_comm_rate,agt_claim_comm,transaction_date,rc.transaction_id from st_ola_ret_comm rc,st_lms_retailer_transaction_master rtm " 
				            +"where rc.transaction_id=rtm.transaction_id and rc.status='CLAIM_BAL') commTlb inner join (select um.user_id retUserId,um.organization_id retOrgId,um.parent_user_id agtUserId,om.parent_id agtOrgId from st_lms_organization_master om inner join st_lms_user_master um on om.organization_id=um.organization_id where om.organization_type='RETAILER') oum on commTlb.retailer_user_id=oum.retUserId group by retOrgId,wallet_id,date(transaction_date)";

			String retOlaCommUpdateQuery = "update st_ola_ret_comm rc,st_lms_retailer_transaction_master rtm set rc.status='DONE_CLM' where rc.transaction_id=rtm.transaction_id and rc.wallet_id=? and rc.retailer_org_id=?";
			//String retOlaCommUpdateQuery = "update st_ola_ret_comm set status='DONE_CLM' where transaction_id=? and retailer_org_id=?";
			PreparedStatement retOlaCommUpdatePstmt = con
					.prepareStatement(retOlaCommUpdateQuery);

			
			PreparedStatement walletStmt = con.prepareStatement(walletQry);

			PreparedStatement transMasQueryPstmt = con
					.prepareStatement("INSERT INTO st_lms_transaction_master (user_type, service_code, interface) VALUES (?, ?,?)");
			PreparedStatement agtTransMasterPstmt = con
					.prepareStatement("INSERT INTO st_lms_agent_transaction_master (transaction_id,user_id,user_org_id,party_type,party_id,transaction_type,transaction_date) VALUES (?,?,?,?,?,?,?)");
			
			String agtOlaCommEntry = "insert into st_ola_agt_comm(transaction_id,wallet_id,agent_user_id,agent_org_id,retailer_org_id,agt_claim_comm,tds_comm_rate,agt_net_claim_comm,ret_mrp_comm,ret_claim_comm,status)" +
					" values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
			PreparedStatement agtPstmt = con.prepareStatement(agtOlaCommEntry);

			ResultSet walletRs = walletStmt.executeQuery();
			pstmt = con.prepareStatement(CommQry);

			int retOrgId = 0, agtOrgId, agtUserId, walletId, drawId;
			double  netAgtComm,agtComm, totalRetComm,totalRetMrpComm,tdsCommRate, totalNetAmt = 0;
			String  transDate;
			Map<Integer, Double> orgAmtMap = new HashMap<Integer, Double>();
			Map<Integer, Integer> orgIdParentIdMap = new HashMap<Integer, Integer>();
			Map<Integer, Map<String, List<Long>>> orgIdTransIdInvMap = new HashMap<Integer, Map<String, List<Long>>>();
			Map<String, List<Long>> transIdInvMap = null;
			while (walletRs.next()) {
				walletId = walletRs.getInt("wallet_id");
				totalNetAmt = 0;
				
				rs = pstmt.executeQuery();
				logger.debug("--Start PWT tran for Wallet Id" + walletId);
				while (rs.next()) {
					int id=rs.getInt("transaction_id");
					retOrgId = rs.getInt("retOrgId");
					agtOrgId = rs.getInt("agtOrgId");
					agtUserId = rs.getInt("agtUserId");
					walletId = rs.getInt("wallet_id");
	
					agtComm = rs.getDouble("agt_claim_comm");
					tdsCommRate=rs.getDouble("tds_comm_rate");
					netAgtComm=agtComm-(agtComm*tdsCommRate*.01);
					totalRetComm = rs.getDouble("retailer_net_claim_comm");
					totalRetMrpComm=rs.getDouble("retailer_claim_comm");
					transDate = rs.getString("transaction_date");
					
					
					orgIdParentIdMap.put(retOrgId, agtOrgId);
					if (orgIdTransIdInvMap.containsKey(retOrgId)) {
						transIdInvMap = orgIdTransIdInvMap.get(retOrgId);
					} else {
						transIdInvMap = new HashMap<String, List<Long>>();
						orgIdTransIdInvMap.put(retOrgId, transIdInvMap);
					}
					// insert data into main transaction master

					transMasQueryPstmt.setString(1, "AGENT");
					transMasQueryPstmt.setString(2, "OLA");
					transMasQueryPstmt.setString(3, "WEB");
					transMasQueryPstmt.executeUpdate();
					ResultSet tranRs = transMasQueryPstmt.getGeneratedKeys();

					if (tranRs.next()) {
						long transId = tranRs.getLong(1);

						if (!transIdInvMap.containsKey(transDate)) {
							transIdInvMap.put(transDate,
									new ArrayList<Long>());
						}
						transIdInvMap.get(transDate).add(transId);

						agtTransMasterPstmt.setLong(1, transId);
						agtTransMasterPstmt.setInt(2, agtUserId);
						agtTransMasterPstmt.setInt(3, agtOrgId);
						agtTransMasterPstmt.setString(4, "RETAILER");
						agtTransMasterPstmt.setInt(5, retOrgId);
						agtTransMasterPstmt.setString(6, "OLA_COMMISSION");
						agtTransMasterPstmt.setTimestamp(7,
								fetchTransTimeStamp(transDate));
						agtTransMasterPstmt.executeUpdate();

						
						retOlaCommUpdatePstmt.setInt(1, walletId);
						retOlaCommUpdatePstmt.setInt(2, retOrgId);
						System.out.println("update st_ola_ret_comm:"+retOlaCommUpdatePstmt);
						retOlaCommUpdatePstmt.executeUpdate();
						
							
						agtPstmt.setLong(1, transId);
						agtPstmt.setInt(2, walletId);
						agtPstmt.setInt(3, agtUserId);
						agtPstmt.setInt(4, agtOrgId);
						agtPstmt.setInt(5, retOrgId);
						agtPstmt.setDouble(6, agtComm);
						agtPstmt.setDouble(7, tdsCommRate);
						agtPstmt.setDouble(8, netAgtComm);
						agtPstmt.setDouble(9, totalRetMrpComm);
						agtPstmt.setDouble(10, totalRetComm);
						agtPstmt.setString(11, "CLAIM_BAL");
						agtPstmt.executeUpdate();

					}
					double retCrAmt = totalRetComm;
					if (orgAmtMap.containsKey(retOrgId)) {
						retCrAmt = retCrAmt + orgAmtMap.get(retOrgId);
					}
					orgAmtMap.put(retOrgId, retCrAmt);
				}

			}

			Set<Integer> allRetId = orgAmtMap.keySet();
			logger.debug("retailer pwt update org amt map" + orgAmtMap);
			for (Integer orgId : allRetId) {
				if (orgId != 0) {
					OrgCreditUpdation.updateCreditLimitForRetailer(orgId,
							"OLA_COMMISSION", orgAmtMap.get(orgId), con);
					updateOrgBalance("CLAIM_BAL", orgAmtMap.get(orgId), orgId,
							"CREDIT", con);
					generateReciptForPwtNew(orgIdTransIdInvMap.get(orgId), con,
							orgIdParentIdMap.get(orgId), orgId, "RETAILER",
							"OLA");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("error in pwt retailer");
		}
		logger.debug(new Date() + "---start-dg pwt update for retailer--"
				+ new Date().getTime());
	}
	
	public void updateOLACommissionLedgerForAgt(Connection con) throws LMSException {
		logger.debug(new Date() + "---start-dg pwt update for agent--"
				+ new Date().getTime());
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			String CommQry = "select wallet_id,agent_org_id,sum(agt_claim_comm) total_agt_comm,tds_comm_rate,sum(ret_claim_comm)ret_claim_comm,transaction_date,transaction_id,agtUserId,boUserId,boOrgId from( select wallet_id,ac.agent_org_id,agt_claim_comm,agt_net_claim_comm,tds_comm_rate,ret_claim_comm,transaction_date,ac.transaction_id from st_ola_agt_comm ac,st_lms_agent_transaction_master rtm where ac.transaction_id=rtm.transaction_id and ac.status='CLAIM_BAL') commTlb inner join " 
				            +"(select um.user_id agtUserId,um.organization_id agtOrgId,um.parent_user_id boUserId,om.parent_id boOrgId from st_lms_organization_master om inner join st_lms_user_master um on om.organization_id=um.organization_id where om.organization_type='AGENT' and isrolehead='Y') oum on commTlb.agent_org_id=oum.agtOrgId group by agtOrgId,wallet_id,date(transaction_date)";

			
			String agtOlaCommUpdateQuery = "update st_ola_agt_comm rc,st_lms_agent_transaction_master rtm set rc.status='DONE_CLM' where rc.transaction_id=rtm.transaction_id and rc.wallet_id=? and rc.agent_org_id=?";

			//String agtOlaCommUpdateQuery = "update st_ola_agt_comm set status='DONE_CLM' where transaction_id=? and agent_org_id=?";
			PreparedStatement agtOlaCommUpdatePstmt = con
					.prepareStatement(agtOlaCommUpdateQuery);

			
			// insert in st_agt_direct_player table in case of
			// player


			PreparedStatement transMasQueryPstmt = con
			.prepareStatement("INSERT INTO st_lms_transaction_master (user_type, service_code, interface) VALUES (?, ?,?)");
	           PreparedStatement boTransMasterPstmt = con
			.prepareStatement("INSERT INTO st_lms_bo_transaction_master (transaction_id,user_id,user_org_id,party_type,party_id,transaction_type,transaction_date) VALUES (?,?,?,?,?,?,?)");
	
	           String boOlaCommEntry = "insert into st_ola_bo_comm(transaction_id,wallet_id,bo_user_id,bo_org_id,agent_org_id,agt_claim_comm,tds_comm_rate,agt_net_claim_comm)" +
				" values (?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement boPstmt = con.prepareStatement(boOlaCommEntry);
		
		
			int agtOrgId, boOrgId, boUserId, walletId, drawId;
			double netAgtComm, totalAgtComm, totalNetAmt,tdsCommRate;
			String pwtType, transDate;
			Map<Integer, Double> orgAmtMap = new HashMap<Integer, Double>();
			Map<Integer, Integer> orgIdParentIdMap = new HashMap<Integer, Integer>();
			Map<Integer, Map<String, List<Long>>> orgIdTransIdInvMap = new HashMap<Integer, Map<String, List<Long>>>();
			Map<String, List<Long>> transIdInvMap= null;

			pstmt = con.prepareStatement(CommQry);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				int id=rs.getInt("transaction_id");
				agtOrgId = rs.getInt("agent_org_id");
				boOrgId = rs.getInt("boOrgId");
				boUserId = rs.getInt("boUserId");
				walletId = rs.getInt("wallet_id");
				logger.debug("--Agent PWT tran for game No" + walletId);
				
				totalAgtComm = rs.getDouble("total_agt_comm");
				tdsCommRate=rs.getDouble("tds_comm_rate");
				netAgtComm=totalAgtComm-(totalAgtComm*tdsCommRate*.01);
				transDate = rs.getString("transaction_date");
				

				orgIdParentIdMap.put(agtOrgId, boOrgId);
				if (orgIdTransIdInvMap.containsKey(agtOrgId)) {
					transIdInvMap = orgIdTransIdInvMap.get(agtOrgId);
				} else {
					transIdInvMap = new HashMap<String, List<Long>>();
					orgIdTransIdInvMap.put(agtOrgId, transIdInvMap);
				}
				// insert data into main transaction master

				transMasQueryPstmt.setString(1, "BO");
				transMasQueryPstmt.setString(2, "OLA");
				transMasQueryPstmt.setString(3, "WEB");
				
				transMasQueryPstmt.executeUpdate();
				ResultSet tranRs = transMasQueryPstmt.getGeneratedKeys();

				if (tranRs.next()) {
					long transId = tranRs.getLong(1);

					if (!transIdInvMap.containsKey(transDate)) {
						transIdInvMap.put(transDate, new ArrayList<Long>());
					}
					transIdInvMap.get(transDate).add(transId);

					boTransMasterPstmt.setLong(1, transId);
					boTransMasterPstmt.setInt(2, boUserId);
					boTransMasterPstmt.setInt(3, boOrgId);
					boTransMasterPstmt.setString(4, "AGENT");
					boTransMasterPstmt.setInt(5, agtOrgId);
					boTransMasterPstmt.setString(6, "OLA_COMMISSION");
					boTransMasterPstmt.setTimestamp(7,
							fetchTransTimeStamp(transDate));
					
					boTransMasterPstmt.executeUpdate();

					
					
					

					boPstmt.setLong(1, transId);
					boPstmt.setInt(2, walletId);
					boPstmt.setInt(3, boUserId);
					boPstmt.setInt(4, boOrgId);
					boPstmt.setInt(5, agtOrgId);
					boPstmt.setDouble(6, totalAgtComm);
					boPstmt.setDouble(7, tdsCommRate);
					boPstmt.setDouble(8, netAgtComm);
										
					boPstmt.executeUpdate();

					agtOlaCommUpdatePstmt.setInt(1, walletId);
					agtOlaCommUpdatePstmt.setInt(2, agtOrgId);
						
						
					agtOlaCommUpdatePstmt.executeUpdate();
				}
				double agtCrAmt = netAgtComm;
				if (orgAmtMap.containsKey(agtOrgId)) {
					agtCrAmt = agtCrAmt + orgAmtMap.get(agtOrgId);
				}
				orgAmtMap.put(agtOrgId, agtCrAmt);
			}

			Set<Integer> allAgtId = orgAmtMap.keySet();
			logger.debug("agent pwt update org amt map" + orgAmtMap);
			for (Integer orgId : allAgtId) {
				if (orgId != 0) {
					OrgCreditUpdation.updateCreditLimitForAgent(orgId,
							"OLA_COMMISSION", orgAmtMap.get(orgId), con);
					updateOrgBalance("CLAIM_BAL", orgAmtMap.get(orgId), orgId,
							"CREDIT", con);
					generateReceiptOlaBo(con, orgId, "AGENT", orgIdTransIdInvMap
							.get(orgId));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("error in pwt agent");
		}
		logger.debug(new Date() + "---start-dg pwt update for agent--"
				+ new Date().getTime());
	}
	
	
	
	/*public void updateOlaPlayerCommissionLedgerForAgt(Connection con)
	{
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		double netGamingAmt, commissionCalculated,totalAgentComm;
		double agentComm, agtDebitAmount;
		String startDate,endDate;
		int agtOrgId, boUserId, boOrgId;
		Map<Integer, Double> orgIdAmountMap = new HashMap<Integer, Double>();
		try {
			String playerCommQry = "select agt_org_id,boUserId,boOrgId,netGamingAmt,wallet_id,start_date,end_date,commissionCalculated,agentCommission,agt_comm_rate from(select a.agt_org_id,sum(plr_net_gaming) netGamingAmt ,a.wallet_id,start_date,end_date, sum(commission_calculated) commissionCalculated, sum(agent_commission) agentCommission,agt_comm_rate from  st_ola_agt_ret_commisiion  a inner join  st_lms_agent_transaction_master b  on a.transaction_id = b.transaction_id  where claim_status='CLAIM_BAL' group by a.agt_org_id,a.wallet_id,agt_comm_rate  order by agt_org_id,wallet_id) netGaming inner join (select um.user_id agtUserId,um.organization_id agtOrgId,um.parent_user_id boUserId,om.parent_id boOrgId,isrolehead from st_lms_organization_master om inner join st_lms_user_master um on om.organization_id=um.organization_id where om.organization_type='AGENT') om on agt_org_id=agtOrgId and isrolehead = 'Y'";
			PreparedStatement pstmtBO = con.prepareStatement(QueryManager
					.insertInLMSTransactionMaster());
			PreparedStatement insBoTranPstmt = con
					.prepareStatement(QueryManager
							.insertInBOTransactionMaster());
			PreparedStatement insBoAgentCommissionPstmt = con
					.prepareStatement("insert into st_ola_bo_agt_commisiion (agt_org_id, net_gaming, commission_calculated, comm_rate, transaction_id, start_date, end_date)values(?,?,?,?,?,?,?)");
			PreparedStatement updAgtRetCommissionpstmt = con
					.prepareStatement("update st_ola_agt_ret_commisiion a inner join st_lms_agent_transaction_master b on a.transaction_id = b.transaction_id set claim_status='DONE_CLAIM',bo_ref_transaction_id=? where a.agt_org_id=? and agt_comm_rate  =?  and wallet_id=?");
			int walletId = 0;
			pstmt = con.prepareStatement(playerCommQry);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				agtOrgId = rs.getInt("agt_org_id");
					boUserId = rs.getInt("boUserId");
				boOrgId = rs.getInt("boOrgId");
				walletId = rs.getInt("wallet_id");
				netGamingAmt = rs.getDouble("netGamingAmt");
				commissionCalculated = rs.getDouble("commissionCalculated");
				totalAgentComm = rs.getDouble("agentCommission");
				agentComm = rs.getDouble("agt_comm_rate");
				System.out.println("agent commisiion"+agentComm);
				startDate = rs.getString("start_date");
				endDate = rs.getString("end_date");
				if (orgIdAmountMap.containsKey(agtOrgId)) {
					agtDebitAmount = orgIdAmountMap.get(agtOrgId)
							+ totalAgentComm;
				} else {
					agtDebitAmount = totalAgentComm;
				}

				orgIdAmountMap.put(agtOrgId, agtDebitAmount);
				// insert in main transaction table

				pstmtBO.setString(1, "BO");
				pstmtBO.executeUpdate();
				ResultSet rsTrns = pstmtBO.getGeneratedKeys();
				if (rsTrns.next()) {
					int transId = rsTrns.getInt(1);

					//if (!transIdInvMap.containsKey(transDate)) {
						//transIdInvMap.put(transDate, new ArrayList<Integer>());
					//}

				//	transIdInvMap.get(transDate).add(transId);

					insBoTranPstmt.setInt(1, transId);
					insBoTranPstmt.setInt(2, boUserId);
					insBoTranPstmt.setInt(3, boOrgId);
					insBoTranPstmt.setString(4, "AGENT");
					insBoTranPstmt.setInt(5, agtOrgId);
					insBoTranPstmt.setTimestamp(6,
							fetchTransTimeStamp(startDate));
					insBoTranPstmt.setString(7, "OLA_COMMISSION");
					insBoTranPstmt.executeUpdate();

					insBoAgentCommissionPstmt.setInt(1, agtOrgId);
					insBoAgentCommissionPstmt.setDouble(2, netGamingAmt);
					insBoAgentCommissionPstmt.setDouble(3, commissionCalculated);
					System.out.println("agent+sdjksd"+agentComm);
					insBoAgentCommissionPstmt.setDouble(4, agentComm);
					insBoAgentCommissionPstmt.setInt(5, transId);
					insBoAgentCommissionPstmt.setString(6, startDate);
					insBoAgentCommissionPstmt.setString(7, endDate);
					insBoAgentCommissionPstmt.executeUpdate();

					// update retailer table

					updAgtRetCommissionpstmt.setInt(1, transId); // agent trans Id
					updAgtRetCommissionpstmt.setInt(2, agtOrgId);
					updAgtRetCommissionpstmt.setDouble(3, agentComm);
					updAgtRetCommissionpstmt.setInt(4, walletId);
					updAgtRetCommissionpstmt.executeUpdate();
				}
			}
			
				// update agent balance
			Set<Integer> agtOrgIdSet = orgIdAmountMap.keySet();
			for (Integer orgId : agtOrgIdSet) {
				OrgCreditUpdation.updateCreditLimitForAgent(orgId,
						"OLA_COMMISSION", orgIdAmountMap.get(orgId), con);
				updateOrgBalance("CLAIM_BAL", orgIdAmountMap.get(orgId), orgId,
						"DEBIT", con);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	
	}*/
}