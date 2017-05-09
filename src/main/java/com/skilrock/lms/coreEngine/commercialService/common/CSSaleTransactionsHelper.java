package com.skilrock.lms.coreEngine.commercialService.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.CSSaleBean;
import com.skilrock.lms.beans.CSUserBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.OrgCreditUpdation;
import com.skilrock.lms.coreEngine.drawGames.common.CommonFunctionsHelper;

public class CSSaleTransactionsHelper {
	static Log logger = LogFactory.getLog(CSSaleTransactionsHelper.class);

	public CSSaleBean CommServSaleBalDeduction(CSSaleBean saleBean) {
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rsTrns = null;
		saleBean.setStatus("Failure");
		CSUserBean userBean = CSUtil.fetchUserInfo(saleBean.getUserName());
		
		// validations
		if (userBean == null) {
			saleBean.setErrorCode(103); // 103 : wrong userName
			return saleBean;
		} else if (userBean.getUserOrgStatus().equalsIgnoreCase("INACTIVE")) {
			saleBean.setErrorCode(105); // 105 : retailer inactive
			return saleBean;
		} else if (userBean.getParentOrgStatus().equalsIgnoreCase("INACTIVE")) {
			saleBean.setErrorCode(106); // 106 : retailer's parent
			// inactive
			return saleBean;
		} else if (Double.compare(saleBean.getMrpAmt(), saleBean.getUnitPrice()
				* saleBean.getMult()) != 0 || Double.compare(saleBean.getMrpAmt(), 0.0) == 0) {
			saleBean.setErrorCode(113); // 113 : wrong MRP Calculation
			return saleBean;
		}

		saleBean.setBalance(userBean.getUserOrgBalance());
		logger.debug("setting balance before calculation in case of sale:"+saleBean.getBalance());
		saleBean.setRetOrgId(userBean.getUserOrgId());
		try {
			con.setAutoCommit(false);
			boolean isFraud = false;/*
									 * ResponsibleGaming.respGaming(userBean,
									 * "CS_SALE", saleBean.getMrpAmt() + "");
									 */

			if (!isFraud) {
				// fetch commission rates from DB
				String merchant = LMSFilterDispatcher.csProvider;
				Map<String, Double> commMap = CSUtil.fetchCommisions(saleBean
						.getProdCode(),saleBean.getOperatorCode(), saleBean.getCircleCode(), saleBean.getDenomination(), merchant,con);
				double retailerComm = 0.0;
				double agentComm = 0.0;
				double jvComm = 0.0;
				double isFlexi = 0.0;
				int categoryId = 0;
				if (commMap.size() == 0) {
					saleBean.setErrorCode(107); // 107 : wrong product code
					return saleBean;
				} else {
					saleBean.setProdId(commMap.get("prodId").intValue());
					categoryId = commMap.get("category_id").intValue();
					saleBean.setCategoryId(categoryId);
					CSUtil
							.fetchRetCommVar(userBean, saleBean.getProdId(),
									con);
					retailerComm = commMap.get("retailerComm")
							+ userBean.getUserSaleCommVar();
					agentComm = commMap.get("agentComm")
							+ userBean.getParentSaleCommVar();
					jvComm = commMap.get("jvComm");
					isFlexi = commMap.get("is_flexi");
					// Net sale is being calculated without jv and vat
					saleBean.setNetAmt(CSUtil.fmtToFourDecimal(saleBean.getMrpAmt()
							- saleBean.getMrpAmt() * retailerComm / 100)); // set
					// retailer
					// net
					// amount
					// in
					// saleBeann12
					
					if ((Double.compare(isFlexi, 1.0) != 0) && (Double.compare(commMap.get("unit_price"), saleBean
							.getUnitPrice()) != 0)) {
						saleBean.setErrorCode(115); // 115 : unitPrice not
						// matching with RMS
						// database
						return saleBean;
					}
				}

				// balance availability check
				if (Double.compare(userBean.getUserOrgBalance(), saleBean
						.getNetAmt()) < 0) {
					saleBean.setErrorCode(101); // 3 : balance insufficient
					return saleBean;
				}

				if (Double.compare(userBean.getParentBalance(), saleBean
						.getNetAmt()) < 0) {
					saleBean.setErrorCode(102); // 3 : parent balance
					// insufficient
					return saleBean;
				}
				double totComm = (saleBean.getMrpAmt()*retailerComm/100)+ (saleBean.getMrpAmt()*agentComm/100) + (saleBean.getMrpAmt()*jvComm/100);
				if(Double.compare(saleBean.getMrpAmt(),totComm) < 0){
					logger.debug("improper total commission:---- retComm:"+ (saleBean.getMrpAmt()*retailerComm/100)+", agentComm:"+ (saleBean.getMrpAmt()*agentComm/100)+", jvComm:"+ (saleBean.getMrpAmt()*jvComm/100));
					saleBean.setErrorCode(110); // improper JV Cost Calculation
					return saleBean;
				}

				// insert in main transaction table
				pstmt = con
						.prepareStatement("insert into st_lms_transaction_master (user_type, service_code, interface) values('RETAILER','CS','TERMINAL')");
				pstmt.executeUpdate();
				rsTrns = pstmt.getGeneratedKeys();
				if (rsTrns.next()) {
					int transId = rsTrns.getInt(1);
					logger.debug("trans id " + transId);

					// insert into retailer transaction master
					pstmt = con
							.prepareStatement("INSERT INTO st_lms_retailer_transaction_master (transaction_id,retailer_user_id,retailer_org_id,game_id,transaction_date,transaction_type) VALUES (?,?,?,?,?,?)");
					pstmt.setInt(1, transId);
					pstmt.setInt(2, CSUtil.fetchUserOrgId("",
							saleBean.getRetOrgId()).get("UserId")); //retailer User Id
					pstmt.setInt(3, saleBean.getRetOrgId());
					pstmt.setInt(4, saleBean.getProdId());
					pstmt.setTimestamp(5, new java.sql.Timestamp(new Date()
							.getTime()));
					//log transaction time in bean
					saleBean.setTransTime(new Date());
					pstmt.setString(6, "CS_SALE");
					pstmt.executeUpdate();

					// insert in cs sale table
					pstmt = con
							.prepareStatement("insert into st_cs_sale_? (transaction_id, product_id, retailer_org_id, cs_ref_transaction_id, mrp_amt, net_amt, agent_net_amt, retailer_comm, agent_comm, jv_comm, jv_comm_amt, vat, vat_amt,govt_comm, govt_comm_amt, multiple, purchase_channel, claim_status) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
					pstmt.setInt(1, categoryId);
					pstmt.setInt(2, transId);
					pstmt.setInt(3, saleBean.getProdId());
					pstmt.setInt(4, saleBean.getRetOrgId());
					pstmt.setInt(5, saleBean.getCSRefTxId());
					pstmt.setDouble(6, saleBean.getMrpAmt()); // mrp_amt
					pstmt.setDouble(7, CSUtil.fmtToFourDecimal(saleBean.getNetAmt()));
					pstmt.setDouble(8, CSUtil.fmtToFourDecimal(saleBean.getMrpAmt()
							- saleBean.getMrpAmt() * agentComm / 100)); // agent_net_amt
					pstmt.setDouble(9, retailerComm); // retailer_comm
					pstmt.setDouble(10, agentComm); // agent_comm
					pstmt.setDouble(11, jvComm); // jv_comm
					pstmt.setDouble(12, CSUtil.fmtToFourDecimal(saleBean.getMrpAmt()
							- saleBean.getMrpAmt() * jvComm / 100)); // jv_comm_amt
					pstmt.setDouble(13, 0.0); //vat
					pstmt.setDouble(14, 0.0); //vat_amt
					pstmt.setDouble(15, 0.0); //govt_comm
					pstmt.setDouble(16, 0.0); //govt_comm_amt
					pstmt.setInt(17, saleBean.getMult());
					pstmt.setString(18, "TERMINAL");
					pstmt.setString(19, "CLAIM_BAL");
					pstmt.executeUpdate();

					
					//Now make payment updte method only one
					OrgCreditUpdation.updateOrganizationBalWithValidate(saleBean
							.getNetAmt(), "CLAIM_BAL", 
							"CREDIT", saleBean.getRetOrgId(),userBean
							.getParentOrgId(), "RETAILER", 0, con);
					
					OrgCreditUpdation.updateOrganizationBalWithValidate(saleBean
							.getMrpAmt()
							- saleBean.getMrpAmt() * agentComm / 100, "CLAIM_BAL",
							"CREDIT",userBean
							.getParentOrgId(),0, "AGENT", 0, con);
				
					
					/*// update st_lms_organization_master for claimable balance
					// for
					// retailer
					CommonFunctionsHelper commHelper = new CommonFunctionsHelper();
					commHelper
							.updateOrgBalance("CLAIM_BAL",
									saleBean.getNetAmt(), saleBean
											.getRetOrgId(), "CREDIT", con);

					// update st_lms_organization_master for claimable balance
					// for
					// agent
					commHelper.updateOrgBalance("CLAIM_BAL", saleBean
							.getMrpAmt()
							- saleBean.getMrpAmt() * agentComm / 100, userBean
							.getParentOrgId(), "CREDIT", con);*/

					// update balance in saleBean
					saleBean.setRMSRefId(transId);
					saleBean.setBalance(CommonMethods.fmtToTwoDecimal(userBean.getUserOrgBalance()
							- saleBean.getNetAmt()));
					logger.debug("setting balance in case of sale:"+saleBean.getBalance());
				}
				con.commit();
				saleBean.setErrorCode(100);
				saleBean.setStatus("Success");
			} else {
				logger.debug("Responsing Gaming not allowed to sale");
				saleBean.setErrorCode(112);
				return saleBean;
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().indexOf("cs_ref_transaction_id") != -1) {
				saleBean.setErrorCode(107); // 107: invalid CS Ref. Transaction
				// id
			} else if (e.getLocalizedMessage()
					.indexOf("rms_ref_transaction_id") != -1) {
				saleBean.setErrorCode(108); // 108: invalid RMS Ref. transaction
				// id
			} else {
				saleBean.setErrorCode(114); // 114: Internal Server Error
			}
			//e.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				saleBean.setErrorCode(114); // 114 : Internal Server Error
				e1.printStackTrace();
				return saleBean;
			}

			e.printStackTrace();
			return saleBean;

		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return saleBean;
	}

	public CSSaleBean CommServSaleRefund(CSSaleBean saleBean) {
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rsTrns = null;
		ResultSet rsTrns2 = null;
		saleBean.setStatus("Failure");
		int categoryId = 0;
		try {
			con.setAutoCommit(false);
			boolean isFraud = false;/*
									 * ResponsibleGaming.respGaming(userBean,
									 * "CS_SALE_CANCEL", saleBean.getMrpAmt() +
									 * "");
									 */

			if (!isFraud) {

				// get Product Code from st_cs_product_master
				pstmt = con
						.prepareStatement("select cpm.product_code, cpm.category_id, cpm.product_id from st_cs_product_master cpm,(select game_id from st_lms_retailer_transaction_master where transaction_id = ? and transaction_type = 'CS_SALE')rtm where cpm.product_id=rtm.game_id");
				pstmt.setInt(1, saleBean.getRMSRefIdForRefund());
				rsTrns2 = pstmt.executeQuery();
				if (rsTrns2.next()) {
					categoryId = rsTrns2.getInt("category_id");
					saleBean.setCategoryId(categoryId);
					saleBean.setProdId(rsTrns2.getInt("product_id"));
					saleBean.setProdCode(rsTrns2.getString("product_code"));
				} else {
					saleBean.setErrorCode(109); // 109 : RMS Ref Id for
					// Cancellation is not in DB
					return saleBean;
				}
				
//  To Stop Duplicate  Refund  
				 PreparedStatement pstmt1 = con.prepareStatement("select rms_ref_transaction_id from st_cs_refund_? where rms_ref_transaction_id=?");
			        pstmt1.setInt(1, categoryId);
			        pstmt1.setInt(2, saleBean.getRMSRefIdForRefund());
			        ResultSet rsTrns1 = pstmt1.executeQuery();
			        if(rsTrns1.next())
			        {			
			        	
			        	saleBean.setErrorCode(0);
			        	   
			        
			           return saleBean ;
			        }
// by neeraj			  
				
				
				
				
				pstmt = con
						.prepareStatement("select retailer_org_id,product_id, mrp_amt, net_amt, agent_net_amt, retailer_comm, agent_comm, jv_comm, jv_comm_amt,vat, vat_amt, govt_comm, govt_comm_amt, agent_ref_transaction_id from st_cs_sale_? where cs_ref_transaction_id = ? and transaction_id = ?");
				pstmt.setInt(1, categoryId);
				pstmt.setInt(2, saleBean.getCSRefTxIdForRefund());
				pstmt.setInt(3, saleBean.getRMSRefIdForRefund());
				logger.debug("sale table data " + pstmt);
				rsTrns = pstmt.executeQuery();
				if (rsTrns.next()) {
					saleBean.setRetOrgId(rsTrns.getInt("retailer_org_id"));
					saleBean.setProdId(rsTrns.getInt("product_id"));
					saleBean.setMrpAmt(rsTrns.getDouble("mrp_amt"));
					saleBean.setNetAmt(rsTrns.getDouble("net_amt"));
				} else {
					saleBean.setErrorCode(108); // 108 : CS Ref transaction Id
					// for Cancellation is not in DB
					return saleBean;
				}

				// insert in main transaction master
				pstmt = con
						.prepareStatement("insert into st_lms_transaction_master (user_type, service_code, interface) values('RETAILER','CS','TERMINAL')");
				pstmt.executeUpdate();
				rsTrns2 = pstmt.getGeneratedKeys();
				if (rsTrns2.next()) {
					int transId = rsTrns2.getInt(1);
					logger.debug("trans id " + transId);
					// insert into retailer transaction master
					pstmt = con
							.prepareStatement("INSERT INTO st_lms_retailer_transaction_master (transaction_id,retailer_user_id,retailer_org_id,game_id,transaction_date,transaction_type) VALUES (?,?,?,?,?,?)");
					pstmt.setInt(1, transId);
					pstmt.setInt(2, CSUtil.fetchUserOrgId("",
							saleBean.getRetOrgId()).get("UserId")); // retailer
					// user Id
					pstmt.setInt(3, saleBean.getRetOrgId());
					pstmt.setInt(4, saleBean.getProdId());
					pstmt.setTimestamp(5, new java.sql.Timestamp(new Date()
							.getTime()));
					//log transaction time in bean
					saleBean.setTransTime(new Date());
					pstmt.setString(6, "CS_"
							+ saleBean.getReasonForCancel().toUpperCase()); // transactionType
					pstmt.executeUpdate();

					// insert in cs sale refund table
					double cancellationCharge = 0.0;
					pstmt = con
							.prepareStatement("insert into st_cs_refund_? (transaction_id, product_id, retailer_org_id, cs_ref_transaction_id, rms_ref_transaction_id, claim_status, cancel_reason, mrp_amt, net_amt, agent_net_amt, retailer_comm ,agent_comm, jv_comm, jv_comm_cost, vat, vat_amt, govt_comm, govt_comm_amt, agent_ref_transaction_id, cancellation_charges ) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
					pstmt.setInt(1, categoryId);
					pstmt.setInt(2, transId);
					pstmt.setInt(3, saleBean.getProdId());
					pstmt.setInt(4, saleBean.getRetOrgId());
					pstmt.setInt(5, saleBean.getCSRefTxId());
					pstmt.setInt(6, saleBean.getRMSRefIdForRefund());
					pstmt.setString(7, "CLAIM_BAL");
					pstmt.setString(8, saleBean.getReasonForCancel());
					pstmt.setDouble(9, saleBean.getMrpAmt());
					pstmt.setDouble(10, saleBean.getNetAmt());
					pstmt.setDouble(11, rsTrns.getDouble("agent_net_amt"));
					pstmt.setDouble(12, rsTrns.getDouble("retailer_comm"));
					pstmt.setDouble(13, rsTrns.getDouble("agent_comm"));
					pstmt.setDouble(14, rsTrns.getDouble("jv_comm"));
					pstmt.setDouble(15, rsTrns.getDouble("jv_comm_amt"));
					pstmt.setDouble(16, rsTrns.getDouble("vat"));
					pstmt.setDouble(17, rsTrns.getDouble("vat_amt"));
					pstmt.setDouble(18, rsTrns.getDouble("govt_comm"));
					pstmt.setDouble(19, rsTrns.getDouble("govt_comm_amt"));
					pstmt.setInt(20, rsTrns.getInt("agent_ref_transaction_id"));
					pstmt.setString(21, cancellationCharge + "");
					logger
							.debug("Query for sale refund insert in st_cs_refund: "
									+ pstmt);
					int rows = pstmt.executeUpdate();
					if (rows == 0) {
						saleBean.setErrorCode(108); // 108 : Invalid CS
						// Reference Transaction Id
						return saleBean;
					}
					saleBean.setRMSRefId(transId);

					
					
					//Now make payment updte method only one
					OrgCreditUpdation.updateOrganizationBalWithValidate(saleBean
							.getNetAmt(), "CLAIM_BAL", 
							"DEBIT", saleBean.getRetOrgId(),CSUtil
							.fetchRetParentId(saleBean.getRetOrgId()), "RETAILER", 0, con);
					
					OrgCreditUpdation.updateOrganizationBalWithValidate(rsTrns
							.getDouble("agent_net_amt"), "CLAIM_BAL",
							"DEBIT",CSUtil
							.fetchRetParentId(saleBean.getRetOrgId()),0, "AGENT", 0, con);
				
					
					/*
					// update st_lms_organization_master for claimable balance
					// for
					// retailer
					CommonFunctionsHelper commHelper = new CommonFunctionsHelper();
					commHelper.updateOrgBalance("CLAIM_BAL", saleBean
							.getNetAmt(), saleBean.getRetOrgId(), "DEBIT", con);

					// update st_lms_organization_master for claimable balance
					// for
					// agent
					commHelper.updateOrgBalance("CLAIM_BAL", rsTrns
							.getDouble("agent_net_amt"), CSUtil
							.fetchRetParentId(saleBean.getRetOrgId()), "DEBIT",
							con);*/

					pstmt = con
							.prepareStatement("select (available_credit - claimable_bal) as bal from st_lms_organization_master where organization_id = ? and organization_type = 'RETAILER'");
					pstmt.setInt(1, saleBean.getRetOrgId());
					rsTrns = pstmt.executeQuery();
					if (rsTrns.next()) {
						saleBean.setBalance(rsTrns.getDouble("bal"));
					}

					// update balance in saleBean
					saleBean.setBalance(CommonMethods.fmtToTwoDecimal(saleBean.getBalance()
							+ saleBean.getNetAmt()));
					con.commit();
					saleBean.setErrorCode(100);
					saleBean.setStatus("Success");
				}
			} else {
				logger.debug("Responsing Gaming not allowed to cancel sale");
				saleBean.setErrorCode(112); // 112: Cancellation Limit Reached
				return saleBean;
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getLocalizedMessage().indexOf("cs_ref_transaction_id") != -1) {
				saleBean.setErrorCode(108); // 108: invalid CS ref.
				// transaction_id
			} else if (e.getLocalizedMessage()
					.indexOf("rms_ref_transaction_id") != -1) {
				saleBean.setErrorCode(109); // 109: invalid RMS Ref id
			} else {
				saleBean.setErrorCode(114); // 114: Internal Server Error
			}
			try {
				con.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return saleBean;
			}

			e.printStackTrace();
			return saleBean;
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return saleBean;
	}
}
