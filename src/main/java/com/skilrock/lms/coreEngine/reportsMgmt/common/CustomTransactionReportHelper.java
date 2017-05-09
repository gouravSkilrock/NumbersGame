package com.skilrock.lms.coreEngine.reportsMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.CollectionReportBean;
import com.skilrock.lms.beans.CollectionReportOverAllBean;
import com.skilrock.lms.beans.CustomTransactionReportBean;
import com.skilrock.lms.beans.ReportStatusBean;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.DBConnectReplica;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.FormatNumber;
import com.skilrock.lms.web.accMgmt.common.RetailerOpeningBalanceHelper;
import com.skilrock.lms.web.reportsMgmt.common.ReportUtility;

public class CustomTransactionReportHelper {
	Log logger = LogFactory.getLog(CustomTransactionReportHelper.class);
	private PreparedStatement pstmt = null;
	private double openningBalance = 0.0;

	public Map<String, String> allGameMap() throws LMSException {
		Map<String, String> gameMap = new LinkedHashMap<String, String>();
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String gameQry = "select game_name,'DG' as game_type from st_dg_game_master union all select game_name,'SE' as game_type from st_se_game_master union all select category_code,'CS' from st_cs_product_category_master where status = 'ACTIVE' order by game_type";
			pstmt = con.prepareStatement(gameQry);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				gameMap.put(rs.getString("game_name"), rs
						.getString("game_type"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("Error in fetch Game List");
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return gameMap;
	}

	public Map<String, String> allCatMap() throws LMSException {
		Map<String, String> gameMap = new LinkedHashMap<String, String>();
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String gameQry = "select category_code,'CS' as cat_type from st_cs_product_category_master where status = 'ACTIVE'";
			pstmt = con.prepareStatement(gameQry);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				gameMap.put(rs.getString("category_code"), rs
						.getString("cat_type"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("Error in fetch Cat List");
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return gameMap;
	}

	public Map<String, Boolean> checkAvailableService() {
		Connection con = null;
		PreparedStatement pstmt = null;
		Map<String, Boolean> serMap = new HashMap<String, Boolean>();
		try {
			con = DBConnect.getConnection();
			String chkService = "select service_code,status from st_lms_service_master where service_code!='MGMT'";
			pstmt = con.prepareStatement(chkService);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				serMap.put(rs.getString("service_code"),
						"ACTIVE".equals(rs.getString("status")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return serMap;
	}

	public void collectionTransactionWise(Timestamp startDate, Timestamp endDate, Connection con, boolean isDG, boolean isSE, boolean isCS, boolean isSLE, boolean isIW, boolean isVS, Map<String, CustomTransactionReportBean> retailerMap, int retOrgId) throws LMSException {
		PreparedStatement pstmt = null;
		PreparedStatement pstmt1 = null;
		ResultSet rs = null;
		Statement stmt = null;
		if (startDate.after(endDate)) {
			return;
		}
		String csString = "";
		String seString = "";
		String olaString = "";
		String sleString = "";
		String iwString = "";
		String vsString = "";
		if (isCS) {
			csString = " union all select transaction_id,transaction_date,transaction_type,description as game_name,'GAME' as amount from st_lms_retailer_transaction_master rtm inner join st_cs_product_master scm on scm.product_id=rtm.game_id and date(transaction_date)>='"
					+ startDate
					+ "' and date(transaction_date) <= '"
					+ endDate
					+ "' and rtm.transaction_type in('CS_CANCEL_SERVER','CS_CANCEL_RET','CS_SALE') and retailer_org_id="
					+ retOrgId;
		}
		if (isSE) {
			seString = "union all select atm.transaction_id,transaction_date,atm.transaction_type,(select game_name from st_se_game_master gm where gm.game_id=art.game_id) as game_name,net_amt as amount from st_se_agent_retailer_transaction art,st_lms_agent_transaction_master atm where art.transaction_id=atm.transaction_id and  date(transaction_date)>='"
					+ startDate
					+ "' and date(transaction_date) <= '"
					+ endDate
					+ "' and atm.transaction_type in('SALE','SALE_RET') and retailer_org_id="
					+ retOrgId
					+ " union all "
					+ " select rtm.transaction_id,transaction_date,rtm.transaction_type,(select game_name from st_se_game_master gm where gm.game_id=srp.game_id) as game_name,(pwt_amt+(pwt_amt*claim_comm*0.01))  as amount from st_se_retailer_pwt srp,st_lms_retailer_transaction_master rtm where srp.transaction_id=rtm.transaction_id and  date(transaction_date)>='"
					+ startDate
					+ "' and date(transaction_date) <= '"
					+ endDate
					+ "' and rtm.transaction_type in('PWT') and srp.retailer_org_id= "
					+ retOrgId;
		}
		if (ReportUtility.isOLA) {
			olaString = "union all select mTbl.transaction_id,transaction_date,transaction_type,game_name,net_amt amount from(select transaction_id,retailer_org_id,'OLA Deposit' game_name,net_amt from st_ola_ret_deposit where retailer_org_id=? union all select transaction_id,retailer_org_id,'OLA Deposit Refund' game_name,net_amt from st_ola_ret_deposit_refund where retailer_org_id=? union all select transaction_id,retailer_org_id,'OLA Withdrawl' game_name,net_amt from st_ola_ret_withdrawl where retailer_org_id=? union all select transaction_id,retailer_org_id,'OLA Commission' game_name,retailer_net_claim_comm net_amt from st_ola_ret_comm where retailer_org_id=?)mTbl inner join  st_lms_retailer_transaction_master rtm on mTbl.transaction_id=rtm.transaction_id where date(transaction_date)>='"
					+ startDate
					+ "' and date(transaction_date) <= '"
					+ endDate
					+ "'";
		}
		if (isSLE) {
			sleString = "union all select transaction_id,transaction_date,transaction_type,type_disp_name game_name,'GAME' as amount from "
					+ "st_lms_retailer_transaction_master rtm inner join st_sle_game_type_master sdg on sdg.game_type_id=rtm.game_id and date(transaction_date)>='"
					+ startDate
					+ "' and date(transaction_date) <= '"
					+ endDate
					+ "' and rtm.transaction_type in('SLE_REFUND_CANCEL','SLE_REFUND_FAILED','SLE_SALE','SLE_PWT_AUTO','SLE_PWT_PLR','SLE_PWT') and retailer_org_id="
					+ retOrgId + "";
		}
		if (isIW) {
			iwString = "union all select transaction_id,transaction_date,transaction_type,game_disp_name game_name,'GAME' as amount from "
					+ "st_lms_retailer_transaction_master rtm inner join st_iw_game_master sdg on sdg.game_id=rtm.game_id and date(transaction_date)>='"
					+ startDate
					+ "' and date(transaction_date) <= '"
					+ endDate
					+ "' and rtm.transaction_type in('IW_SALE','IW_REFUND_CANCEL','IW_PWT') and retailer_org_id="
					+ retOrgId + "";
		}
		if (isVS) {
			vsString = "union all select transaction_id,transaction_date,transaction_type,game_disp_name game_name,'GAME' as amount from "
					+ "st_lms_retailer_transaction_master rtm inner join st_vs_game_master sdg on sdg.game_id=rtm.game_id and date(transaction_date)>='"
					+ startDate
					+ "' and date(transaction_date) <= '"
					+ endDate
					+ "' and rtm.transaction_type in('VS_SALE','VS_REFUND_CANCEL','VS_PWT') and retailer_org_id="
					+ retOrgId + "";
		}
		try {
			stmt = con.createStatement();
			String transactionQuery="select transaction_id,transaction_date,transaction_type,game_name,amount from(select transaction_id,transaction_date,transaction_type,game_name,'GAME' as amount from "
				+	"st_lms_retailer_transaction_master rtm inner join st_dg_game_master sdg on sdg.game_id=rtm.game_id and date(transaction_date)>='"+startDate+"' and date(transaction_date) <= '"+endDate+"' and rtm.transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED','DG_SALE','DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') and retailer_org_id="+retOrgId +" union all select concat(auto_id,type),date_time,type,concat(type,'  UPDATE'),amount from st_lms_cl_xcl_update_history where date(date_time)>='"+startDate+"' and date(date_time) <= '"+endDate+"' and organization_id="+retOrgId +" union all " 
				+ " select atm.transaction_id,transaction_date,atm.transaction_type,'Debit Note' as game_name, amount from st_lms_agent_transaction_master atm,st_lms_agent_debit_note sad where atm.transaction_id=sad.transaction_id and date(transaction_date)>='"+startDate+"' and date(transaction_date) <= '"+endDate+"' and party_id="+retOrgId+"	union"
				+"	select atm.transaction_id,transaction_date,atm.transaction_type,'Credit Note' as game_name, amount from st_lms_agent_transaction_master atm,st_lms_agent_credit_note sac where atm.transaction_id=sac.transaction_id and date(transaction_date)>='"+startDate+"' and date(transaction_date) <= '"+endDate+"' and party_id="+retOrgId+"	union" 
				+"	select atm.transaction_id,transaction_date,atm.transaction_type,'Cash Deposit' as game_name, amount from st_lms_agent_transaction_master atm,st_lms_agent_cash_transaction sct where atm.transaction_id=sct.transaction_id and date(transaction_date)>='"+startDate+"' and date(transaction_date) <= '"+endDate+"' and party_id="+retOrgId+"	union"
				+"	select atm.transaction_id,transaction_date,atm.transaction_type,'Cheque Update' as game_name,cheque_amt amount from st_lms_agent_transaction_master atm,st_lms_agent_sale_chq sas where atm.transaction_id=sas.transaction_id and date(transaction_date)>='"+startDate+"' and date(transaction_date) <= '"+endDate+"' and party_id="+retOrgId+" union select atm.transaction_id,transaction_date,atm.transaction_type,'Bank Deposit' as game_name, amount from st_lms_agent_transaction_master atm,st_lms_agent_bank_deposit_transaction bdt where atm.transaction_id=bdt.transaction_id and date(transaction_date)>='"+startDate+"' and date(transaction_date) <= '"+endDate+"' and party_id="+retOrgId+" "
				 +csString
				+"  " +seString+ "  "+olaString+"  "+sleString+"  "+iwString+"  "+vsString+") xyz order by transaction_date asc";
			pstmt = con.prepareStatement(transactionQuery);
			if (ReportUtility.isOLA) {
				pstmt.setInt(1, retOrgId);
				pstmt.setInt(2, retOrgId);
				pstmt.setInt(3, retOrgId);
				pstmt.setInt(4, retOrgId);
			}
			System.out.println("transaction Query::" + transactionQuery);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String transactionType = rs.getString("transaction_type");
				CustomTransactionReportBean transactionBean = new CustomTransactionReportBean();
				transactionBean.setDate(rs.getString("transaction_date")
						.substring(0,
								rs.getString("transaction_date").length() - 2));
				transactionBean.setTransactionType(transactionType);
				transactionBean.setGameName(rs.getString("game_name"));
				if ("CL".equalsIgnoreCase(transactionType)
						|| "XCL".equalsIgnoreCase(transactionType)) {
					transactionBean.setAmount(rs.getDouble("amount"));
					transactionBean.setService("Home");
				} else if ("CHEQUE".equalsIgnoreCase(transactionType)
						|| "CR_NOTE_CASH".equalsIgnoreCase(transactionType)
						|| "CASH".equalsIgnoreCase(transactionType)
						|| "CR_NOTE".equalsIgnoreCase(transactionType)) {
					transactionBean.setAmount(rs.getDouble("amount"));
					transactionBean.setService("Home");
				} else if ("CHQ_BOUNCE".equalsIgnoreCase(transactionType)
						|| "DR_NOTE".equalsIgnoreCase(transactionType)
						|| "DR_NOTE_CASH".equalsIgnoreCase(transactionType)) {
					transactionBean.setAmount(-(rs.getDouble("amount")));
					transactionBean.setService("Home");
				} else if ("SALE".equalsIgnoreCase(transactionType)) {
					transactionBean.setAmount(-(rs.getDouble("amount")));
					transactionBean.setService("Scratch Service");
					transactionBean.setGameName("Sale : "
							+ rs.getString("game_name"));
				} else if ("SALE_RET".equalsIgnoreCase(transactionType)) {
					transactionBean.setAmount(rs.getDouble("amount"));
					transactionBean.setService("Scratch Service");
					transactionBean.setGameName("Cancel : "
							+ rs.getString("game_name"));
				} else if ("PWT".equalsIgnoreCase(transactionType)) {
					transactionBean.setAmount(rs.getDouble("amount"));
					transactionBean.setService("Scratch Service");
					transactionBean.setGameName("PWT : "
							+ rs.getString("game_name"));
				} else if ("OLA_DEPOSIT".equalsIgnoreCase(transactionType)) {
					transactionBean.setAmount(-(rs.getDouble("amount")));
					transactionBean.setService("Home");
					transactionBean.setGameName(rs.getString("game_name"));
				} else if ("OLA_DEPOSIT_REFUND"
						.equalsIgnoreCase(transactionType)
						|| "OLA_WITHDRAWL".equalsIgnoreCase(transactionType)
						|| "OLA_COMMISSION".equalsIgnoreCase(transactionType)) {
					transactionBean.setAmount(rs.getDouble("amount"));
					transactionBean.setService("Home");
					transactionBean.setGameName(rs.getString("game_name"));
				} else if ("BANK_DEPOSIT".equalsIgnoreCase(transactionType)) {
					transactionBean.setAmount(rs.getDouble("amount"));
					transactionBean.setService("Home");
				} else {
					transactionBean.setAmount(0.0);
				}
				if ("CHEQUE".equalsIgnoreCase(transactionType)) {
					transactionBean.setGameName("CHEQUE");
				} else if ("CHQ_BOUNCE".equalsIgnoreCase(transactionType)) {
					transactionBean.setGameName("CHEQUE BOUNCE");
				}

				retailerMap
						.put(rs.getString("transaction_id"), transactionBean);
			}
			System.out.println("retailerMap:::" + retailerMap);
			if (isSLE) {
				// String gameQry = "select game_id from st_sle_game_master";
				// PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				// ResultSet rsGame = gamePstmt.executeQuery();
				StringBuilder saleQry = new StringBuilder(
						"select transaction_id,retailer_net_amt from (");
				StringBuilder cancelQry = new StringBuilder(
						"select transaction_id,retailer_net_amt from (");
				StringBuilder pwtQry = new StringBuilder(
						"select transaction_id,pwt_amt from (");
				
				// while (rsGame.next()) {
				saleQry.append("(select rtm.transaction_id,retailer_net_amt from st_lms_retailer_transaction_master rtm inner join st_sle_ret_sale drs on rtm.transaction_id=drs.transaction_id and  date(rtm.transaction_date)>='"
						+ startDate
						+ "' and date(rtm.transaction_date) <= '"
						+ endDate
						+ "' and rtm.transaction_type ='SLE_SALE' and rtm.retailer_org_id="
						+ retOrgId + " ) union all ");

				cancelQry
						.append("(select rtm.transaction_id,retailer_net_amt from st_lms_retailer_transaction_master rtm inner join st_sle_ret_sale_refund drs on rtm.transaction_id=drs.transaction_id and  date(rtm.transaction_date)>='"
								+ startDate
								+ "' and date(rtm.transaction_date) <= '"
								+ endDate
								+ "' and rtm.transaction_type in ('SLE_REFUND_CANCEL','SLE_REFUND_FAILED') and rtm.retailer_org_id="
								+ retOrgId + " ) union all  ");

				pwtQry.append("(select rtm.transaction_id,pwt_amt from st_lms_retailer_transaction_master rtm inner join st_sle_ret_pwt drs on rtm.transaction_id=drs.transaction_id and  date(rtm.transaction_date)>='"
						+ startDate
						+ "' and date(rtm.transaction_date) <= '"
						+ endDate
						+ "' and rtm.transaction_type in('SLE_PWT_AUTO','SLE_PWT_PLR','SLE_PWT') and rtm.retailer_org_id="
						+ retOrgId + " ) union all");
				// }

				saleQry.delete(saleQry.lastIndexOf("union all"),
						saleQry.length());
				cancelQry.delete(cancelQry.lastIndexOf("union all"),
						cancelQry.length());
				pwtQry.delete(pwtQry.lastIndexOf("union all"), pwtQry.length());

				saleQry.append(") saletable order by transaction_id asc");
				cancelQry.append(") cancelTlb order by transaction_id asc");
				pwtQry.append(") pwtTlb order by transaction_id asc");
				logger.debug("-------SLE Sale Qurey------\n" + saleQry);
				logger.debug("-------SLE Cancel Qurey------\n" + cancelQry);
				logger.debug("-------SLE Pwt Qurey------\n" + pwtQry);

				rs = stmt.executeQuery(saleQry.toString());
				while (rs.next()) {

					retailerMap.get(rs.getString("transaction_id")).setAmount(
							-(rs.getDouble("retailer_net_amt")));
					retailerMap.get(rs.getString("transaction_id")).setService(
							"Sports Lottery");
					String gameName = retailerMap.get(
							rs.getString("transaction_id")).getGameName();
					retailerMap.get(rs.getString("transaction_id"))
							.setGameName("Sale : " + gameName);
				}
				// Draw Cancel Query
				/*
				 * pstmt = con.prepareStatement(cancelQry.toString()); rs =
				 * pstmt.executeQuery();
				 */

				rs = stmt.executeQuery(cancelQry.toString());

				while (rs.next()) {
					retailerMap.get(rs.getString("transaction_id")).setAmount(
							rs.getDouble("retailer_net_amt"));
					retailerMap.get(rs.getString("transaction_id")).setService(
							"Sports Lottery");
					String gameName = retailerMap.get(
							rs.getString("transaction_id")).getGameName();
					retailerMap.get(rs.getString("transaction_id"))
							.setGameName("Cancel : " + gameName);

				}
				// Draw Pwt Query
				/*
				 * pstmt = con.prepareStatement(pwtQry.toString()); rs =
				 * pstmt.executeQuery();
				 */

				rs = stmt.executeQuery(pwtQry.toString());

				while (rs.next()) {
					retailerMap.get(rs.getString("transaction_id")).setAmount(
							rs.getDouble("pwt_amt"));
					retailerMap.get(rs.getString("transaction_id")).setService(
							"Sports Lottery");
					String gameName = retailerMap.get(
							rs.getString("transaction_id")).getGameName();
					retailerMap.get(rs.getString("transaction_id"))
							.setGameName("Pwt : " + gameName);
				}

				System.out.println("retailerMap::" + retailerMap);

			}
			if (isIW) {
				// String gameQry = "select game_id from st_sle_game_master";
				// PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				// ResultSet rsGame = gamePstmt.executeQuery();
				StringBuilder saleQry = new StringBuilder(
						"select transaction_id,retailer_net_amt from (");
				StringBuilder cancelQry = new StringBuilder(
						"select transaction_id,retailer_net_amt from (");
				StringBuilder pwtQry = new StringBuilder(
						"select transaction_id,pwt_amt from (");

				// while (rsGame.next()) {
				saleQry.append("(select rtm.transaction_id,retailer_net_amt from st_lms_retailer_transaction_master rtm inner join st_iw_ret_sale drs on rtm.transaction_id=drs.transaction_id and  date(rtm.transaction_date)>='"
						+ startDate
						+ "' and date(rtm.transaction_date) <= '"
						+ endDate
						+ "' and rtm.transaction_type ='IW_SALE' and rtm.retailer_org_id="
						+ retOrgId + " ) union all ");

				cancelQry
						.append("(select rtm.transaction_id,retailer_net_amt from st_lms_retailer_transaction_master rtm inner join st_iw_ret_sale_refund drs on rtm.transaction_id=drs.transaction_id and  date(rtm.transaction_date)>='"
								+ startDate
								+ "' and date(rtm.transaction_date) <= '"
								+ endDate
								+ "' and rtm.transaction_type in ('IW_REFUND_CANCEL') and rtm.retailer_org_id="
								+ retOrgId + " ) union all  ");

				pwtQry.append("(select rtm.transaction_id,(pwt_amt+retailer_claim_comm) pwt_amt from st_lms_retailer_transaction_master rtm inner join st_iw_ret_pwt drs on rtm.transaction_id=drs.transaction_id and  date(rtm.transaction_date)>='"
						+ startDate
						+ "' and date(rtm.transaction_date) <= '"
						+ endDate
						+ "' and rtm.transaction_type in('IW_PWT') and rtm.retailer_org_id="
						+ retOrgId + " ) union all");
				// }

				saleQry.delete(saleQry.lastIndexOf("union all"),
						saleQry.length());
				cancelQry.delete(cancelQry.lastIndexOf("union all"),
						cancelQry.length());
				pwtQry.delete(pwtQry.lastIndexOf("union all"), pwtQry.length());

				saleQry.append(") saletable order by transaction_id asc");
				cancelQry.append(") cancelTlb order by transaction_id asc");
				pwtQry.append(") pwtTlb order by transaction_id asc");
				logger.debug("-------IW Sale Qurey------\n" + saleQry);
				logger.debug("-------IW Cancel Qurey------\n" + cancelQry);
				logger.debug("-------IW Pwt Qurey------\n" + pwtQry);

				rs = stmt.executeQuery(saleQry.toString());
				while (rs.next()) {

					retailerMap.get(rs.getString("transaction_id")).setAmount(
							-(rs.getDouble("retailer_net_amt")));
					retailerMap.get(rs.getString("transaction_id")).setService(
							"Instant Win");
					String gameName = retailerMap.get(
							rs.getString("transaction_id")).getGameName();
					retailerMap.get(rs.getString("transaction_id"))
							.setGameName("Sale : " + gameName);
				}
				// Draw Cancel Query
				/*
				 * pstmt = con.prepareStatement(cancelQry.toString()); rs =
				 * pstmt.executeQuery();
				 */

				rs = stmt.executeQuery(cancelQry.toString());

				while (rs.next()) {
					retailerMap.get(rs.getString("transaction_id")).setAmount(
							rs.getDouble("retailer_net_amt"));
					retailerMap.get(rs.getString("transaction_id")).setService(
							"Instant Win");
					String gameName = retailerMap.get(
							rs.getString("transaction_id")).getGameName();
					retailerMap.get(rs.getString("transaction_id"))
							.setGameName("Cancel : " + gameName);

				}
				// Draw Pwt Query
				/*
				 * pstmt = con.prepareStatement(pwtQry.toString()); rs =
				 * pstmt.executeQuery();
				 */

				rs = stmt.executeQuery(pwtQry.toString());

				while (rs.next()) {
					retailerMap.get(rs.getString("transaction_id")).setAmount(
							rs.getDouble("pwt_amt"));
					retailerMap.get(rs.getString("transaction_id")).setService(
							"Instant Win");
					String gameName = retailerMap.get(
							rs.getString("transaction_id")).getGameName();
					retailerMap.get(rs.getString("transaction_id"))
							.setGameName("Pwt : " + gameName);
				}

				System.out.println("retailerMap::" + retailerMap);

			}
			if (isDG) {

				// Game Master Query
				String gameQry = "select game_id from st_dg_game_master";
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				ResultSet rsGame = gamePstmt.executeQuery();
				StringBuilder saleQry = new StringBuilder(
						"select transaction_id,net_amt from (");
				StringBuilder cancelQry = new StringBuilder(
						"select transaction_id,net_amt,ref_transaction_id from (");
				StringBuilder pwtQry = new StringBuilder(
						"select transaction_id,pwt_amt from (");
				while (rsGame.next()) {
					saleQry.append("(select rtm.transaction_id,net_amt from st_lms_retailer_transaction_master rtm inner join st_dg_ret_sale_"+ rsGame.getInt("game_id")
							+ " drs on rtm.transaction_id=drs.transaction_id and  date(transaction_date)>='"
							+ startDate
							+ "' and date(transaction_date) <= '"
							+ endDate
							+ "' and rtm.transaction_type ='DG_SALE' and rtm.retailer_org_id="
							+ retOrgId + " ) union all ");

					cancelQry
							.append("(select rtm.transaction_id,net_amt,ref_transaction_id from st_lms_retailer_transaction_master rtm inner join st_dg_ret_sale_refund_"
									+ rsGame.getInt("game_id")
									+ " drs on rtm.transaction_id=drs.transaction_id and  date(transaction_date)>='"
									+ startDate
									+ "' and date(transaction_date) <= '"
									+ endDate
									+ "' and rtm.transaction_type in ('DG_REFUND_CANCEL','DG_REFUND_FAILED') and rtm.retailer_org_id="
									+ retOrgId + " ) union all  ");

					pwtQry.append("(select rtm.transaction_id,(pwt_amt+retailer_claim_comm-govt_claim_comm) pwt_amt from st_lms_retailer_transaction_master rtm inner join st_dg_ret_pwt_"
							+ rsGame.getInt("game_id")
							+ " drs on rtm.transaction_id=drs.transaction_id and  date(transaction_date)>='"
							+ startDate
							+ "' and date(transaction_date) <= '"
							+ endDate
							+ "' and rtm.transaction_type in('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') and rtm.retailer_org_id="
							+ retOrgId + " ) union all");

				}

				saleQry.delete(saleQry.lastIndexOf("union all"),
						saleQry.length());
				cancelQry.delete(cancelQry.lastIndexOf("union all"),
						cancelQry.length());
				pwtQry.delete(pwtQry.lastIndexOf("union all"), pwtQry.length());

				saleQry.append(") saletable order by transaction_id asc");
				cancelQry.append(") cancelTlb order by transaction_id asc");
				pwtQry.append(") pwtTlb order by transaction_id asc");
				logger.debug("-------Draw Sale Qurey------\n" + saleQry);
				logger.debug("-------Draw Cancel Qurey------\n" + cancelQry);
				logger.debug("-------Draw Pwt Qurey------\n" + pwtQry);

				// Draw Sale Query
				/*
				 * pstmt = con.prepareStatement(saleQry.toString()); rs =
				 * pstmt.executeQuery();
				 */

				rs = stmt.executeQuery(saleQry.toString());
				while (rs.next()) {

					retailerMap.get(rs.getString("transaction_id")).setAmount(
							-(rs.getDouble("net_amt")));
					retailerMap.get(rs.getString("transaction_id")).setService(
							"Draw Games");
					String gameName = retailerMap.get(
							rs.getString("transaction_id")).getGameName();
					retailerMap.get(rs.getString("transaction_id"))
							.setGameName("Sale : " + gameName);
				}
				// Draw Cancel Query
				/*
				 * pstmt = con.prepareStatement(cancelQry.toString()); rs =
				 * pstmt.executeQuery();
				 */

				rs = stmt.executeQuery(cancelQry.toString());

				while (rs.next()) {
					retailerMap.get(rs.getString("transaction_id")).setAmount(
							rs.getDouble("net_amt"));
					retailerMap.get(rs.getString("transaction_id")).setService(
							"Draw Games");
					String gameName = retailerMap.get(
							rs.getString("transaction_id")).getGameName();
					retailerMap.get(rs.getString("transaction_id"))
							.setGameName("Cancel : " + gameName);

				}
				// Draw Pwt Query
				/*
				 * pstmt = con.prepareStatement(pwtQry.toString()); rs =
				 * pstmt.executeQuery();
				 */

				rs = stmt.executeQuery(pwtQry.toString());

				while (rs.next()) {
					retailerMap.get(rs.getString("transaction_id")).setAmount(
							rs.getDouble("pwt_amt"));
					retailerMap.get(rs.getString("transaction_id")).setService(
							"Draw Games");
					String gameName = retailerMap.get(
							rs.getString("transaction_id")).getGameName();
					retailerMap.get(rs.getString("transaction_id"))
							.setGameName("Pwt : " + gameName);
				}

				System.out.println("retailerMap::" + retailerMap);
			}
			/*		if (isSE) {
				// Calculate Scratch Sale
				String saleQry = "";
				String cancelQry="";
				logger.info("----Type Select ---"
						+ LMSFilterDispatcher.seSaleReportType);
				if ("BOOK_WISE".equals(LMSFilterDispatcher.seSaleReportType)) {
					saleQry="select sbt.agent_org_id,ifnull(sum(mrp_amt),0.0) as mrp_amt,ifnull(sum(net_amt),0.0) netAmt,date(btm.transaction_date) date from st_se_bo_agent_transaction sbt,st_lms_bo_transaction_master btm where sbt.transaction_id=btm.transaction_id and btm.transaction_type ='SALE' and transaction_date>='" 
						+ startDate
					    +"' and transaction_date<='" 
					    + endDate
					    +"' and sbt.agent_org_id="+agtOrgId +" group by date(transaction_date)";
					
					
				 cancelQry="select sbt.agent_org_id,ifnull(sum(mrp_amt),0.0) as mrp_amt,ifnull(sum(net_amt),0.0) netAmt,date(btm.transaction_date) date from st_se_bo_agent_transaction sbt,st_lms_bo_transaction_master btm where sbt.transaction_id=btm.transaction_id and btm.transaction_type ='SALE_RET' and transaction_date>='" 
						+ startDate
					    +"' and transaction_date<='" 
					    + endDate
					    +"' and sbt.agent_org_id="+agtOrgId +" group by date(transaction_date)";
				
				} else if ("TICKET_WISE"
						.equals(LMSFilterDispatcher.seSaleReportType)) {
					saleQry = "select gid.current_owner_id,sum(soldTkt*ticket_price) mrpAmt,sum((soldTkt*ticket_price)-(soldTkt*ticket_price*transacrion_sale_comm_rate*0.01)) netAmt,date(transaction_date) date from "
                              +"st_se_game_master gm,st_se_game_inv_detail gid,(select game_id,book_nbr,sum(sold_tickets) soldTkt from st_se_game_ticket_inv_history where date>='" 
                              + startDate
                              +"' and date<='" 
                              + endDate
                              +"' and current_owner='RETAILER' group by book_nbr) TktTlb where gm.game_id=TktTlb.game_id and TktTlb.book_nbr=gid.book_nbr and gid.current_owner='AGENT' and gid.current_owner_id="+ agtOrgId +" group by date(transaction_date) ";
				}
				pstmt = con.prepareStatement(saleQry);
				logger.debug("***Scratch Sale Query*** \n" + pstmt);
				rs = pstmt.executeQuery();

				while (rs.next()) {
					SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
					String dateFrDtParse =dateformat.format(rs.getDate("date"));
									
				agtMap.get(dateFrDtParse).setSeSale(rs.getDouble("netAmt"));
					
				}
				if ("BOOK_WISE".equals(LMSFilterDispatcher.seSaleReportType)) {
				pstmt = con.prepareStatement(cancelQry);
				logger.debug("***Scratch Cancel Query*** \n" + pstmt);
				rs = pstmt.executeQuery();

				while (rs.next()) {
					SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
					String dateFrDtParse =dateformat.format(rs.getDate("date"));
									
				agtMap.get(dateFrDtParse).setSeCancel(rs.getDouble("netAmt"));
					
				}
				}
				
				// Calculate Scratch Pwt
				String pwtQry = "select date(transaction_date) date,parent_id,sum(pwt) as pwt from (select bb.parent_id,sum(pwt) as pwt,transaction_date from(select srp.retailer_org_id,sum(pwt_amt+(pwt_amt*agt_claim_comm*0.01)) pwt,transaction_date from " 
					+" st_se_retailer_pwt srp inner join st_lms_retailer_transaction_master rtm on srp.transaction_id=rtm.transaction_id and transaction_date>= ? and transaction_date<= ? and transaction_type='PWT' and srp.retailer_org_id in "
					+"(select organization_id from st_lms_organization_master where organization_type='RETAILER')group by date(transaction_date),retailer_org_id)aa,st_lms_organization_master bb where retailer_org_id=organization_id and parent_id="+ agtOrgId+" group by date(transaction_date) union all select bb.parent_id,sum(pwt) as pwt,transaction_date from(select srp.retailer_org_id,sum(pwt_amt+(pwt_amt*claim_comm*0.01)) pwt,transaction_date from st_se_agent_pwt srp "
					+"inner join st_lms_retailer_transaction_master rtm on srp.transaction_id=rtm.transaction_id and transaction_date>= ? and transaction_date<= ?  and transaction_type='PWT' and srp.retailer_org_id in (select organization_id from st_lms_organization_master where organization_type='RETAILER')group by date(transaction_date),retailer_org_id)aa,st_lms_organization_master bb where retailer_org_id=organization_id and parent_id="+agtOrgId+" group by date(transaction_date)"
					+"union all select agent_org_id,sum(pwt_amt+comm_amt) Pwt,transaction_date  from st_se_bo_pwt sbp inner join st_lms_bo_transaction_master btm on sbp.transaction_id=btm.transaction_id and transaction_date>= ? and transaction_date<= ? and transaction_type='PWT' and agent_org_id in (select organization_id from st_lms_organization_master where organization_type='AGENT' and organization_id= "+ agtOrgId +") group by date(transaction_date))pwtTlb group by date(transaction_date) ";
				pstmt = con.prepareStatement(pwtQry);
				pstmt.setTimestamp(1, startDate);
				pstmt.setTimestamp(2, endDate);
				pstmt.setTimestamp(3, startDate);
				pstmt.setTimestamp(4, endDate);
				pstmt.setTimestamp(5, startDate);
				pstmt.setTimestamp(6, endDate);
				logger.debug("***Scratch Pwt Query*** \n" + pstmt);
				rs = pstmt.executeQuery();

				while (rs.next()) {
					SimpleDateFormat dateformat = new SimpleDateFormat(
					"yyyy-MM-dd");
					String dateFrDtParse =dateformat.format(rs.getDate("date"));
					agtMap.get(dateFrDtParse).setSePwt(rs.getDouble("pwt"));
					}

				// Scratch Direct Player Qry
				String dirPwtQry = "select date(transaction_date) date,agent_org_id,sum(pwt_amt+(pwt_amt*claim_comm*0.01)) pwtDir from st_se_agt_direct_player_pwt where transaction_date>=? and transaction_date<=? and agent_org_id="+agtOrgId+" group by date(transaction_date) ";
				
					
			
				
				//String dirPwtQry = "select organization_id,ifnull(pwtDir,0.0) pwtDir from (select agent_org_id,sum(pwt_amt+(pwt_amt*claim_comm*0.01)) pwtDir from st_se_agt_direct_player_pwt where transaction_date>=? and transaction_date<=? group by agent_org_id) pwtDirPly right outer join (select organization_id from st_lms_organization_master where organization_type='AGENT' and organization_id="+agtOrgId+") om on agent_org_id=organization_id";
				pstmt = con.prepareStatement(dirPwtQry);
				pstmt.setTimestamp(1, startDate);
				pstmt.setTimestamp(2, endDate);
				logger
						.debug("-------Scratch Direct Player Qry------\n"
								+ pstmt);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					SimpleDateFormat dateformat = new SimpleDateFormat(
					"yyyy-MM-dd");
					String dateFrDtParse =dateformat.format(rs.getDate("date"));
					agtMap.get(dateFrDtParse).setSeDirPlyPwt(rs.getDouble("pwtDir"));
				}
			}*/
			if (isCS) {
				System.out.println("retailerMap::" + retailerMap);

				// Category Master Query
				String catQry = "select category_id from st_cs_product_category_master";
				PreparedStatement gamePstmt = con.prepareStatement(catQry);
				ResultSet rsProduct = gamePstmt.executeQuery();
				StringBuilder saleQry = new StringBuilder(
						"select transaction_id,net_amt from (");
				StringBuilder cancelQry = new StringBuilder(
						"select transaction_id,net_amt,cs_ref_transaction_id from (");

				while (rsProduct.next()) {
					saleQry.append("(select rtm.transaction_id,net_amt from st_lms_retailer_transaction_master rtm inner join st_cs_sale_"
							+ rsProduct.getInt("category_id")
							+ " drs on rtm.transaction_id=drs.transaction_id and  date(transaction_date)>='"
							+ startDate
							+ "' and date(transaction_date) <= '"
							+ endDate
							+ "' and rtm.transaction_type ='CS_SALE' and rtm.retailer_org_id="
							+ retOrgId + " ) union all ");

					cancelQry
							.append("(select rtm.transaction_id,net_amt,cs_ref_transaction_id from st_lms_retailer_transaction_master rtm inner join st_cs_refund_"
									+ rsProduct.getInt("category_id")
									+ " drs on rtm.transaction_id=drs.transaction_id and  date(transaction_date)>='"
									+ startDate
									+ "' and date(transaction_date) <='"
									+ endDate
									+ "' and rtm.transaction_type in ('CS_CANCEL_SERVER','CS_CANCEL_RET') and rtm.retailer_org_id="
									+ retOrgId + " ) union all  ");

				}

				saleQry.delete(saleQry.lastIndexOf("union all"),
						saleQry.length());
				cancelQry.delete(cancelQry.lastIndexOf("union all"),
						cancelQry.length());

				saleQry.append(") saletable order by transaction_id asc");
				cancelQry.append(") cancelTlb order by transaction_id asc");

				logger.debug("-------CS Sale Query------\n" + saleQry);
				logger.debug("-------CS Cancel Query------\n" + cancelQry);

				// CS Sale Query
				pstmt = con.prepareStatement(saleQry.toString());
				rs = pstmt.executeQuery();
				while (rs.next()) {

					retailerMap.get(rs.getString("transaction_id")).setAmount(-(rs.getDouble("net_amt")));
					retailerMap.get(rs.getString("transaction_id")).setService("Commercial Service");
					String gameName=retailerMap.get(rs.getString("transaction_id")).getGameName();
					retailerMap.get(rs.getString("transaction_id")).setGameName("Sale : "+gameName);

				}
				// CS Cancel Query
				pstmt = con.prepareStatement(cancelQry.toString());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					retailerMap.get(rs.getString("transaction_id")).setAmount(rs.getDouble("net_amt"));
					retailerMap.get(rs.getString("transaction_id")).setService("Commercial Service");
					String gameName=retailerMap.get(rs.getString("transaction_id")).getGameName();
					retailerMap.get(rs.getString("transaction_id")).setGameName("Cancel : "+gameName);

				}
			}
			if (isVS) {
				StringBuilder saleQry = new StringBuilder("select transaction_id,retailer_net_amt from (");
				StringBuilder cancelQry = new StringBuilder("select transaction_id,retailer_net_amt from (");
				StringBuilder pwtQry = new StringBuilder("select transaction_id,pwt_amt from (");

				saleQry.append("(select rtm.transaction_id,retailer_net_amt from st_lms_retailer_transaction_master rtm inner join st_vs_ret_sale drs on rtm.transaction_id=drs.transaction_id and date(rtm.transaction_date)>='"
						+ startDate
						+ "' and date(rtm.transaction_date) <= '"
						+ endDate
						+ "' and rtm.transaction_type ='VS_SALE' and rtm.retailer_org_id="
						+ retOrgId + " ) union all ");
				cancelQry
						.append("(select rtm.transaction_id,retailer_net_amt from st_lms_retailer_transaction_master rtm inner join st_vs_ret_sale_refund drs on rtm.transaction_id=drs.transaction_id and date(rtm.transaction_date)>='"
								+ startDate
								+ "' and date(rtm.transaction_date) <= '"
								+ endDate
								+ "' and rtm.transaction_type in ('VS_REFUND_CANCEL') and rtm.retailer_org_id="
								+ retOrgId + " ) union all  ");

				pwtQry.append("(select rtm.transaction_id,(pwt_amt+retailer_claim_comm) pwt_amt from st_lms_retailer_transaction_master rtm inner join st_vs_ret_pwt drs on rtm.transaction_id=drs.transaction_id and date(rtm.transaction_date)>='"
						+ startDate
						+ "' and date(rtm.transaction_date) <= '"
						+ endDate
						+ "' and rtm.transaction_type in('VS_PWT') and rtm.retailer_org_id="
						+ retOrgId + " ) union all");

				saleQry.delete(saleQry.lastIndexOf("union all"), saleQry.length());
				cancelQry.delete(cancelQry.lastIndexOf("union all"), cancelQry.length());
				pwtQry.delete(pwtQry.lastIndexOf("union all"), pwtQry.length());

				saleQry.append(") saletable order by transaction_id asc");
				cancelQry.append(") cancelTlb order by transaction_id asc");
				pwtQry.append(") pwtTlb order by transaction_id asc");
				logger.debug("-------VS Sale Qurey------\n" + saleQry);
				logger.debug("-------VS Cancel Qurey------\n" + cancelQry);
				logger.debug("-------VS Pwt Qurey------\n" + pwtQry);

				rs = stmt.executeQuery(saleQry.toString());
				while (rs.next()) {
					retailerMap.get(rs.getString("transaction_id")).setAmount(-(rs.getDouble("retailer_net_amt")));
					retailerMap.get(rs.getString("transaction_id")).setService("Virtual Sport");
					String gameName = retailerMap.get(rs.getString("transaction_id")).getGameName();
					retailerMap.get(rs.getString("transaction_id")).setGameName("Sale : " + gameName);
				}
				rs = stmt.executeQuery(cancelQry.toString());

				while (rs.next()) {
					retailerMap.get(rs.getString("transaction_id")).setAmount(rs.getDouble("retailer_net_amt"));
					retailerMap.get(rs.getString("transaction_id")).setService("Virtual Sport");
					String gameName = retailerMap.get(rs.getString("transaction_id")).getGameName();
					retailerMap.get(rs.getString("transaction_id")).setGameName("Cancel : " + gameName);
				}
				rs = stmt.executeQuery(pwtQry.toString());

				while (rs.next()) {
					retailerMap.get(rs.getString("transaction_id")).setAmount(rs.getDouble("pwt_amt"));
					retailerMap.get(rs.getString("transaction_id")).setService("Virtual Sport");
					String gameName = retailerMap.get(rs.getString("transaction_id")).getGameName();
					retailerMap.get(rs.getString("transaction_id")).setGameName("Pwt : " + gameName);
				}
				System.out.println("retailerMap::" + retailerMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("Error in report collectionAgentWise");
		}
	}

	public List<CollectionReportBean> getRetailerCollectionDetail(
			Timestamp startDate, Timestamp endDate, int retOrgId,
			int agentOrgId, boolean isDraw, boolean isScratch, boolean isCS) {
		List<CollectionReportBean> list = new ArrayList<CollectionReportBean>();
		CollectionReportBean collectionBean = null;
		Connection con = DBConnect.getConnection();
		ResultSet rs = null, rs1 = null, rs2 = null, rs3 = null;
		System.out.println(con + "size of report list " + retOrgId);
		java.sql.Date start = new java.sql.Date(startDate.getTime());
		java.sql.Date end = new java.sql.Date(endDate.getTime());

		try {
			// double totalSale=0.0;
			double totalSaleRet = 0.0;
			// double TotalSaleNRetTotal=0.0;
			double totalCash = 0.0;
			double totalChq = 0.0;
			double totalChqRet = 0.0;
			double totalCredit = 0.0;
			double totalDebit = 0.0;
			double totalDrawSale = 0.0;
			double totalDrawSaleRefund = 0.0;
			double totalDrawPwt = 0.0;
			double totalScratchSale = 0.0;
			double totalScratchPwt = 0.0;
			double totalRecTotal = 0.0;
			double totalScratchTotal = 0.0;
			double totalDrawTotal = 0.0;
			double totalGrandTotal = 0.0;
			int count = 1;

			int partyid = retOrgId;
			pstmt = con.prepareStatement(QueryManager
					.getST_COLLECTION_DETAILS_FOR_AGENT());
			pstmt.setInt(1, agentOrgId);
			pstmt.setInt(2, partyid);
			pstmt.setDate(3, start);
			pstmt.setDate(4, end);

			pstmt.setInt(5, agentOrgId);
			pstmt.setInt(6, partyid);
			pstmt.setDate(7, start);
			pstmt.setDate(8, end);

			pstmt.setInt(9, agentOrgId);
			pstmt.setInt(10, partyid);
			pstmt.setDate(11, start);
			pstmt.setDate(12, end);

			pstmt.setInt(13, agentOrgId);
			pstmt.setInt(14, partyid);
			pstmt.setDate(15, start);
			pstmt.setDate(16, end);

			pstmt.setInt(17, agentOrgId);
			pstmt.setInt(18, partyid);
			pstmt.setDate(19, start);
			pstmt.setDate(20, end);

			rs = pstmt.executeQuery();
			logger.debug("get Agent accounts collections details query- ==== -"
					+ pstmt);

			String clXclamt = "select ifnull(sum(amount),0.0) as amount from st_lms_cl_xcl_update_history where organization_id=? and date_time>=? and date_time< ? ";
			pstmt = con.prepareStatement(clXclamt);
			pstmt.setInt(1, partyid);
			pstmt.setDate(2, start);
			pstmt.setDate(3, end);
			rs3 = pstmt.executeQuery();

			if (isScratch) {
				pstmt = con.prepareStatement(QueryManager
						.getST_COLLECTION_DETAILS_FOR_AGENT_SE());
				pstmt.setInt(1, agentOrgId);
				pstmt.setInt(2, partyid);
				pstmt.setDate(3, start);
				pstmt.setDate(4, end);

				pstmt.setInt(5, agentOrgId);
				pstmt.setInt(6, partyid);
				pstmt.setDate(7, start);
				pstmt.setDate(8, end);

				pstmt.setInt(9, agentOrgId);
				pstmt.setInt(10, partyid);
				pstmt.setDate(11, start);
				pstmt.setDate(12, end);
				logger.debug("get Agent scratch collections details query- ==== -"
						+ pstmt);
				rs1 = pstmt.executeQuery();
			}
			if (isDraw) {
				pstmt = con.prepareStatement(QueryManager
						.getST_COLLECTION_DETAILS_FOR_AGENT_DG());
				pstmt.setInt(1, agentOrgId);
				pstmt.setInt(2, partyid);
				pstmt.setDate(3, start);
				pstmt.setDate(4, end);

				pstmt.setInt(5, agentOrgId);
				pstmt.setInt(6, partyid);
				pstmt.setDate(7, start);
				pstmt.setDate(8, end);

				pstmt.setInt(9, agentOrgId);
				pstmt.setInt(10, partyid);
				pstmt.setDate(11, start);
				pstmt.setDate(12, end);
				logger.debug(" get Agent draw collections details query- ==== -"
						+ pstmt);
				rs2 = pstmt.executeQuery();
			}
			double recTotal = 0.0;
			if (rs.next()) {
				collectionBean = new CollectionReportBean();
				collectionBean.setSrNo(count);

				collectionBean.setOrgId(partyid);

				double cash = rs.getDouble("cash");
				collectionBean.setCash(FormatNumber.formatNumber(cash));
				double cheque = rs.getDouble("cheque");
				collectionBean.setChq(FormatNumber.formatNumber(cheque));
				double credit = rs.getDouble("credit");
				collectionBean.setCredit(FormatNumber.formatNumber(credit));
				double debit = rs.getDouble("debit");
				collectionBean.setDebit(FormatNumber.formatNumber(debit));
				double chqRet = rs.getDouble("cheque_ret");
				collectionBean.setChqRet(FormatNumber.formatNumber(chqRet));

				recTotal = cash + cheque + credit - debit - chqRet;
				collectionBean.setRecTotal(FormatNumber.formatNumber(recTotal));
				totalCash += cash;
				totalChq += cheque;
				totalCredit += credit;
				totalDebit += debit;
				totalChqRet += chqRet;
				totalRecTotal += recTotal;
			}
			double scratchTotal = 0.0;
			double drawTotal = 0.0;
			double clXclTotal = 0.0;
			if (rs3 != null) {
				if (rs3.next()) {
					clXclTotal = rs3.getDouble("amount");
				}
			}
			if (rs2 != null) {
				if (rs2.next()) {
					collectionBean.setIsDraw(true);
					double dgSale = rs2.getDouble("dg_sale");
					collectionBean.setDrawSale(FormatNumber
							.formatNumber(dgSale));
					double dgSaleRefund = rs2.getDouble("dg_sale_refund");
					collectionBean.setDrawSaleRefund(FormatNumber
							.formatNumber(dgSaleRefund));
					double dgPwt = rs2.getDouble("dg_pwt");
					collectionBean.setDrawPwt(FormatNumber.formatNumber(dgPwt));
					totalDrawSale += dgSale;
					totalDrawSaleRefund += dgSaleRefund;
					totalDrawPwt += dgPwt;
					drawTotal = dgSale - dgSaleRefund - dgPwt;
					totalDrawTotal += drawTotal;
					collectionBean.setDrawTotal(FormatNumber
							.formatNumber(drawTotal));
				}
			}
			if (rs1 != null) {
				if (rs1.next()) {
					collectionBean.setIsScratch(true);
					double seSale = rs1.getDouble("se_sale");
					collectionBean.setScratchSale(FormatNumber
							.formatNumber(seSale));
					double sePwt = rs1.getDouble("se_pwt");
					collectionBean.setScratchPwt(FormatNumber
							.formatNumber(sePwt));
					double seSaleRet = rs1.getDouble("se_sale_ret");
					collectionBean.setSaleRet(FormatNumber
							.formatNumber(seSaleRet));
					totalScratchSale += seSale;
					totalSaleRet += seSaleRet;
					totalScratchPwt += sePwt;
					scratchTotal = seSale - seSaleRet - sePwt;
					totalScratchTotal += scratchTotal;
					collectionBean.setScratchTotal(FormatNumber
							.formatNumber(scratchTotal));
				}
			}

			double grandTotal = drawTotal + scratchTotal - recTotal
					+ clXclTotal;

			collectionBean.setOpenBal(FormatNumber.formatNumber(grandTotal));

			collectionBean.setGrandTotal(FormatNumber.formatNumber(grandTotal));

			totalGrandTotal += grandTotal;
			logger.debug("this is a shit: " + totalGrandTotal);
			logger.debug("this is a shit2: " + grandTotal);
			list.add(collectionBean);
			count += 1;

			/*
			 * if (totalOpenBal != null) { totalGrandTotal +=
			 * Double.parseDouble(totalOpenBal); }
			 */

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return list;
	}

	public Map<Integer, Double> getRetailerOpeningBalance(Timestamp startDate,
			Timestamp endDate, Connection con, boolean isDG, boolean isSE,
			boolean isCS, int retOrgId, int agentOrgId) throws LMSException {
		Map<Integer, Double> myMap = new TreeMap<Integer, Double>();

		List<CollectionReportBean> tempBeanList = getRetailerCollectionDetail(
				startDate, endDate, retOrgId, agentOrgId, isDG, isSE, isCS);
		double tempOpenBalTot = 0.0;
		Iterator<CollectionReportBean> it = tempBeanList.iterator();
		while (it.hasNext()) {
			CollectionReportBean tempBean = it.next();
			myMap.put(new Integer(tempBean.getOrgId()),
					Double.parseDouble(tempBean.getOpenBal()));
			tempOpenBalTot += Double.parseDouble(tempBean.getOpenBal());
		}

		return myMap;
	}

	public Map<String, CustomTransactionReportBean> collectionTransactionWiseWithOpeningBal(
			Timestamp deployDate, Timestamp startDate, Timestamp endDate,
			int retOrgId, int agentOrgId, boolean isDG, boolean isSE,
			boolean isCS, boolean isSLE, boolean isIW, boolean isVS,
			ReportStatusBean reportStatusBean) throws LMSException {
		Connection con = null;
		if (startDate.after(endDate)) {
			return null;
		}
		Map<String, CollectionReportOverAllBean> retMapOpenningBalance = new LinkedHashMap<String, CollectionReportOverAllBean>();
		Map<String, CustomTransactionReportBean> retailerMap = new LinkedHashMap<String, CustomTransactionReportBean>();
		Map<Integer, Double> mapForOpenBal = null;
		// Map<String, CollectionReportOverAllBean> resultMap = new
		// LinkedHashMap<String, CollectionReportOverAllBean>();
		CollectionReportOverAllBean collBean = null;
		Double openingBalance = 0.0;
		try {
			double openBal = 0.0;

			if ("NO".equals(Utility.getPropertyValue("IS_DATA_FROM_REPLICA"))
					|| "MAIN_DB".equals(reportStatusBean.getReportingFrom()))
				con = DBConnect.getConnection();
			else
				con = DBConnectReplica.getConnection();

			//String agtOrgQry = "select name,organization_id from st_lms_organization_master where organization_type='AGENT' and organization_id="+agtOrgId;
			//pstmt = con.prepareStatement(agtOrgQry);
			//rsRetOrg = pstmt.executeQuery();

			Calendar startCal = Calendar.getInstance();
			Calendar endCal = Calendar.getInstance();
			Calendar nextCal = Calendar.getInstance();
			startCal.setTimeInMillis(startDate.getTime());
			endCal.setTimeInMillis(endDate.getTime());
			nextCal.setTimeInMillis(startDate.getTime());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			//nextCal.add(Calendar.DAY_OF_MONTH, 1);
		//	startDate = new Timestamp(sdf.parse(sdf.format(startCal.getTime())).getTime());
		//	endDate = new Timestamp(sdf.parse(sdf.format(endCal.getTime())).getTime());
			
			
			
			/*mapForOpenBal = getRetailerOpeningBalance(deployDate, startDate, con, isDG, isSE, isCS,
					retOrgId,agentOrgId);*/
			
			/*System.out.println("mapForOpenBal**"+mapForOpenBal);*/
			System.out.println("***agentMap***" + retailerMap);
			RetailerOpeningBalanceHelper opehelper = new RetailerOpeningBalanceHelper();
			openBal = opehelper.getRetailerOpeningBalIncludingCLXCL(deployDate,startDate, retOrgId,
					agentOrgId, con);

			/*
			 * System.out.println("opening Balance::");
			 * openBal=mapForOpenBal.get(retOrgId);
			 */

			CustomTransactionReportBean bean1 = new CustomTransactionReportBean();
			bean1.setCurrentBalance(openBal);
			bean1.setGameName("Opening Balance(including CL/XCL) :");

			retailerMap.put("openBalance", bean1);
			collectionTransactionWise(startDate, endDate, con, isDG, isSE, isCS, isSLE, isIW, isVS, retailerMap, retOrgId);

			Iterator<Map.Entry<String, CustomTransactionReportBean>> itr1 = retailerMap
					.entrySet().iterator();
			while (itr1.hasNext()) {
				Map.Entry<String, CustomTransactionReportBean> pair = itr1
						.next();
				CustomTransactionReportBean bean = pair.getValue();
				openBal = openBal + bean.getAmount();

				bean.setCurrentBalance(openBal);

				/*
				 * if("CL".equalsIgnoreCase(bean.getTransactionType()) ||
				 * "XCL".equalsIgnoreCase(bean.getTransactionType())){
				 * openBal=openBal+bean.getAmount();
				 * 
				 * bean.setCurrentBalance(openBal); }
				 * 
				 * 
				 * if("CS_SALE".equalsIgnoreCase(bean.getTransactionType())){
				 * openBal=openBal+bean.getAmount();
				 * 
				 * bean.setCurrentBalance(openBal); }
				 * if("CS_CANCEL_SERVER".equalsIgnoreCase
				 * (bean.getTransactionType()) ||
				 * "CS_CANCEL_RET".equalsIgnoreCase(bean.getTransactionType())){
				 * openBal=openBal + bean.getAmount();
				 * bean.setCurrentBalance(openBal); }
				 * 
				 * if("SALE".equalsIgnoreCase(bean.getTransactionType())){
				 * openBal=openBal+bean.getAmount();
				 * 
				 * bean.setCurrentBalance(openBal); }else
				 * if("SALE_RET".equalsIgnoreCase(bean.getTransactionType())){
				 * openBal=openBal + bean.getAmount();
				 * bean.setCurrentBalance(openBal); }else
				 * if("PWT".equalsIgnoreCase(bean.getTransactionType())){
				 * openBal=openBal + bean.getAmount();
				 * bean.setCurrentBalance(openBal); }
				 * if("CHEQUE".equalsIgnoreCase(bean.getTransactionType()) ||
				 * "CR_NOTE_CASH".equalsIgnoreCase(bean.getTransactionType()) ||
				 * "CASH".equalsIgnoreCase(bean.getTransactionType())
				 * ||"CR_NOTE".equalsIgnoreCase(bean.getTransactionType())){
				 * openBal=openBal+bean.getAmount();
				 * bean.setCurrentBalance(openBal); } if(
				 * "CHQ_BOUNCE".equalsIgnoreCase(bean.getTransactionType()) ||
				 * "DR_NOTE".equalsIgnoreCase(bean.getTransactionType())
				 * ||"DR_NOTE_CASH".equalsIgnoreCase(bean.getTransactionType())
				 * ){ openBal=openBal + bean.getAmount();
				 * 
				 * bean.setCurrentBalance(openBal); }
				 * if("DG_SALE".equalsIgnoreCase(bean.getTransactionType())){
				 * openBal=openBal+bean.getAmount();
				 * 
				 * bean.setCurrentBalance(openBal); }
				 * if("DG_REFUND_CANCEL".equalsIgnoreCase
				 * (bean.getTransactionType()) ||
				 * "DG_REFUND_FAILED".equalsIgnoreCase
				 * (bean.getTransactionType())){
				 * openBal=openBal+bean.getAmount();
				 * bean.setCurrentBalance(openBal); }
				 * if("DG_PWT_AUTO".equalsIgnoreCase(bean.getTransactionType())
				 * || "DG_PWT".equalsIgnoreCase(bean.getTransactionType()) ||
				 * "DG_PWT_PLR".equalsIgnoreCase(bean.getTransactionType())){
				 * openBal=openBal+bean.getAmount();
				 * bean.setCurrentBalance(openBal); }
				 */

			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(
					"Error in report collectionAgentWiseWithOpeningBal");
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return retailerMap;
	}

	public Map<String, String> getOrgMap(String orgType) {
		Connection con = null;
		PreparedStatement pstmt = null;
		Map<String, String> orgMap = new LinkedHashMap<String, String>();
		try {
			con = DBConnect.getConnection();
			String chkService = "select name,organization_id from st_lms_organization_master where organization_type=? order by name";
			pstmt = con.prepareStatement(chkService);
			pstmt.setString(1, orgType);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				orgMap.put(rs.getString("organization_id"),
						rs.getString("name"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return orgMap;
	}

	public String getOrgAdd(int orgId, ReportStatusBean reportStatusBean) throws LMSException {
		String orgAdd = "";
		Connection con = null;

		if("NO".equals(Utility.getPropertyValue("IS_DATA_FROM_REPLICA"))
				|| "MAIN_DB".equals(reportStatusBean.getReportingFrom()))
			con = DBConnect.getConnection();
		else
			con = DBConnectReplica.getConnection();

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con
					.prepareStatement("select addr_line1, addr_line2, city from st_lms_organization_master where organization_id = ?");
			pstmt.setInt(1, orgId);
			rs = pstmt.executeQuery();
			System.out.println(pstmt);
			while (rs.next()) {
				orgAdd = rs.getString("addr_line1") + ", "
						+ rs.getString("addr_line2") + ", "
						+ rs.getString("city");
			}
		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				logger.error("Exception: " + e);
				e.printStackTrace();
				throw new LMSException(e);
			}
		}
		return orgAdd;
	}

	public double getOpenBal() {
		return openningBalance;
	}

	public int getOrgId(int userId) throws LMSException {
		int orgId = 0;
		Connection con = null;
		con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con
					.prepareStatement("select organization_id from st_lms_user_master where user_id=?");
			pstmt.setInt(1, userId);
			rs = pstmt.executeQuery();
			System.out.println(pstmt);
			while (rs.next()) {
				orgId = rs.getInt("organization_id");

			}
		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				logger.error("Exception: " + e);
				e.printStackTrace();
				throw new LMSException(e);
			}
		}
		return orgId;
	}
}
