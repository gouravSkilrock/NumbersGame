package com.skilrock.lms.coreEngine.reportsMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.CompleteCollectionBean;
import com.skilrock.lms.beans.ReportStatusBean;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.DBConnectReplica;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.web.reportsMgmt.common.ReportUtility;

public class CompleteCollectionReportHelper implements
		ICompleteCollectionReportHelper {
	static Log logger = LogFactory.getLog(CompleteCollectionReportHelper.class);
	StringBuilder dates = null;

	public Map<String, Double> agentDirectPlayerPwt(Timestamp startDate,
			Timestamp endDate, int agtOrgId, ReportStatusBean reportStatusBean) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String dirPwtQry = null;
		Map<String, Double> dirPlrPwtMap = new LinkedHashMap<String, Double>();
		// Draw Direct Player Qry
		try {
			if("NO".equals(Utility.getPropertyValue("IS_DATA_FROM_REPLICA")) || "MAIN_DB".equals(reportStatusBean.getReportingFrom()))
				con = DBConnect.getConnection();
			else
				con = DBConnectReplica.getConnection();

			dirPwtQry = "select agent_org_id,sum(pwt_amt+agt_claim_comm) pwtDir from st_dg_agt_direct_plr_pwt where transaction_date>=? and transaction_date<=? and agent_org_id=? group by agent_org_id";
			pstmt = con.prepareStatement(dirPwtQry);
			pstmt.setTimestamp(1, startDate);
			pstmt.setTimestamp(2, endDate);
			pstmt.setInt(3, agtOrgId);
			logger.info("-------Draw Direct Player Qry------\n" + pstmt);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				dirPlrPwtMap.put("DG", rs.getDouble("pwtDir"));
			} else {
				dirPlrPwtMap.put("DG", 0.0);
			}
			// Scratch Direct Player Qry

			dirPwtQry = "select agent_org_id,sum(pwt_amt+(pwt_amt*claim_comm*0.01)) pwtDir from st_se_agt_direct_player_pwt where transaction_date>=? and transaction_date<=? and agent_org_id=? group by agent_org_id";
			pstmt = con.prepareStatement(dirPwtQry);
			pstmt.setTimestamp(1, startDate);
			pstmt.setTimestamp(2, endDate);
			pstmt.setInt(3, agtOrgId);
			logger.info("-------Scratch Direct Player Qry------\n"
					+ pstmt);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				dirPlrPwtMap.put("SE", rs.getDouble("pwtDir"));
			} else {
				dirPlrPwtMap.put("SE", 0.0);
			}
			DBConnect.closeCon(con);
		} catch (Exception e) {

		}
		return dirPlrPwtMap;
	}



	public Map<String, CompleteCollectionBean> collectionAgentWise(
			Timestamp startDate, Timestamp endDate, Connection con) {
		PreparedStatement pstmt = null;
		ResultSet rsGame = null;
		ResultSet rs = null;
		ResultSet rsRetOrg = null;
		Map<String, CompleteCollectionBean> agtMap = new LinkedHashMap<String, CompleteCollectionBean>();
		CompleteCollectionBean collBean = null;
		// for Draw Game
		String saleTranDG = null;
		String cancelTranDG = null;
		String pwtTranDG = null;
		
		//for CS Products
		String saleTranCS = null;
		String cancelTranCS = null;
		
		//for OLA
		
		String depositTranOLA = null;
		String cancelTranOLA = null;
		String withdrawalOLA = null;

		if (startDate.after(endDate)) {
			return agtMap;
		}

		// for scratch game
		try {

			// Get All Agent
			
			
			String agtOrgQry = QueryManager.getOrgQry("AGENT");
			
			pstmt = con.prepareStatement(agtOrgQry);
			rsRetOrg = pstmt.executeQuery();
			while (rsRetOrg.next()) {
				collBean = new CompleteCollectionBean();
				collBean.setOrgName(rsRetOrg.getString("orgCode"));
				if (ReportUtility.isDG) {
					collBean.setDrawSale(0.0);
					collBean.setDrawPwt(0.0);
					collBean.setDrawCancel(0.0);
					collBean.setDrawDirPlyPwt(0.0);
				}
				if (ReportUtility.isSE) {
					collBean.setScratchSale(0.0);
					collBean.setScratchPwt(0.0);
					collBean.setScratchDirPlyPwt(0.0);
				}
				if(ReportUtility.isCS) {
					collBean.setCSSale(0.0);
					collBean.setCSCancel(0.0);
				}
				if(ReportUtility.isOLA)
				{
					collBean.setOlaDepositAmt(0.0);
					collBean.setOlaWithdrawalAmt(0.0);
					collBean.setOlaDepositCancelAmt(0.0);
					collBean.setOlaNetGaming(0.0);
				}
				agtMap.put(rsRetOrg.getString("organization_id"), collBean);
			}
			if (ReportUtility.isDG) {
				saleTranDG = "select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_SALE','DG_SALE_OFFLINE') and transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "' and retailer_org_id in (select organization_id from st_lms_organization_master where organization_type='RETAILER')";
				cancelTranDG = "select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') and transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "' and retailer_org_id in (select organization_id from st_lms_organization_master where organization_type='RETAILER')";
				pwtTranDG = "select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') and transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "' and retailer_org_id in (select organization_id from st_lms_organization_master where organization_type='RETAILER')";

				// Game Master Query
				String gameQry = ReportUtility.getDrawGameMapQuery(startDate);
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				rsGame = gamePstmt.executeQuery();
				StringBuilder saleQry = new StringBuilder(
						"select parent_id,sum(sale) as sale from (");
				StringBuilder cancelQry = new StringBuilder(
						"select parent_id,sum(cancel) as cancel from (");
				StringBuilder pwtQry = new StringBuilder(
						"select parent_id,sum(pwt) as pwt from (");
				while (rsGame.next()) {
					saleQry
							.append("(select bb.parent_id,sum(sale) as sale from (select drs.retailer_org_id,sum(agent_net_amt) as sale from st_dg_ret_sale_"
									+ rsGame.getInt("game_id")
									+ " drs where transaction_id in ("
									+ saleTranDG
									+ ") group by drs.retailer_org_id) aa,st_lms_organization_master bb where retailer_org_id=organization_id group by parent_id) union all");

					cancelQry
							.append("(select bb.parent_id,sum(cancel) as cancel from (select drs.retailer_org_id,sum(agent_net_amt) as cancel from st_dg_ret_sale_refund_"
									+ rsGame.getInt("game_id")
									+ " drs where transaction_id in ("
									+ cancelTranDG
									+ ") group by drs.retailer_org_id) aa,st_lms_organization_master bb where retailer_org_id=organization_id group by parent_id) union all");

					pwtQry
							.append("(select bb.parent_id,sum(pwt) as pwt from (select drs.retailer_org_id,sum(pwt_amt+agt_claim_comm) as pwt from st_dg_ret_pwt_"
									+ rsGame.getInt("game_id")
									+ " drs where transaction_id in ("
									+ pwtTranDG
									+ ") group by drs.retailer_org_id) aa,st_lms_organization_master bb where retailer_org_id=organization_id group by parent_id) union all");

				}

				saleQry.delete(saleQry.lastIndexOf("union all"), saleQry
						.length());
				cancelQry.delete(cancelQry.lastIndexOf("union all"), cancelQry
						.length());
				pwtQry.delete(pwtQry.lastIndexOf("union all"), pwtQry.length());

				saleQry.append(") saleTlb group by parent_id");
				cancelQry.append(") cancelTlb group by parent_id");
				pwtQry.append(") pwtTlb group by parent_id");
				logger.info("-------Draw Sale Qurey------\n" + saleQry);
				logger.info("-------Draw Cancel Qurey------\n"
						+ cancelQry);
				logger.info("-------Draw Pwt Qurey------\n" + pwtQry);

				// Draw Sale Query
				pstmt = con.prepareStatement(saleQry.toString());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					agtMap.get(rs.getString("parent_id")).setDrawSale(
							rs.getDouble("sale"));
				}
				// Draw Cancel Query
				pstmt = con.prepareStatement(cancelQry.toString());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					agtMap.get(rs.getString("parent_id")).setDrawCancel(
							rs.getDouble("cancel"));
				}
				// Draw Pwt Query
				pstmt = con.prepareStatement(pwtQry.toString());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					agtMap.get(rs.getString("parent_id")).setDrawPwt(
							rs.getDouble("pwt"));
				}

				// Draw Direct Player Qry

				String dirPwtQry = "select agent_org_id,sum(pwt_amt+agt_claim_comm) pwtDir from st_dg_agt_direct_plr_pwt where transaction_date>=? and transaction_date<=? group by agent_org_id";
				pstmt = con.prepareStatement(dirPwtQry);
				pstmt.setTimestamp(1, startDate);
				pstmt.setTimestamp(2, endDate);
				System.out.println("-------Draw Direct Player Qry------\n"
						+ pstmt);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					agtMap.get(rs.getString("agent_org_id")).setDrawDirPlyPwt(
							rs.getDouble("pwtDir"));
				}
			}
			if (ReportUtility.isSE) {
				// Calculate Scratch Sale
				String saleQry = "";
				
				if ("BOOK_WISE".equals(LMSFilterDispatcher.seSaleReportType)) {
					saleQry = "select organization_id,name,ifnull(mrpAmt,0.0) mrpAmt,ifnull(netAmt,0.0) netAmt from st_lms_organization_master left outer join (select sale.agent_org_id,(ifnull(mrpAmt,0.0)-ifnull(mrpAmtRet,0.0)) mrpAmt,(ifnull(netAmt,0.0)-ifnull(netAmtRet,0.0)) netAmt from (select agent_org_id,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt from (select agent_org_id,sum(mrp_amt) mrpAmt,sum(net_amt) netAmt from st_se_bo_agent_transaction where transaction_id in (select transaction_id from st_lms_bo_transaction_master where transaction_type='SALE' and transaction_date>='"
							+ startDate
							+ "'  and transaction_date<='"
							+ endDate
							+ "' ) group by agent_org_id union all select agent_org_id,sum(mrp_amt) mrpAmt,sum(net_amt) netAmt from  st_se_bo_agent_loose_book_transaction where transaction_id in (select transaction_id from st_lms_bo_transaction_master where transaction_type='LOOSE_SALE' and transaction_date>='"
							+ startDate
							+ "'  and transaction_date<='"
							+ endDate
							+ "' ) group by agent_org_id) sale group by agent_org_id) sale left outer join (select agent_org_id,sum(mrpAmtRet) mrpAmtRet,sum(netAmtRet) netAmtRet from (select agent_org_id,sum(mrp_amt) mrpAmtRet,sum(net_amt) netAmtRet from st_se_bo_agent_transaction where transaction_id in (select transaction_id from st_lms_bo_transaction_master where transaction_type='SALE_RET' and transaction_date>='"
							+ startDate
							+ "'  and transaction_date<='"
							+ endDate
							+ "' ) group by agent_org_id union all select agent_org_id,sum(mrp_amt) mrpAmtRet,sum(net_amt) netAmtRet from st_se_bo_agent_loose_book_transaction where transaction_id in (select transaction_id from st_lms_bo_transaction_master where transaction_type='LOOSE_SALE_RET' and transaction_date>='"
							+ startDate
							+ "'  and transaction_date<='"
							+ endDate
							+ "' ) group by agent_org_id)saleRet group by agent_org_id ) saleRet on sale.agent_org_id=saleRet.agent_org_id) saleTlb on organization_id=agent_org_id where organization_type='AGENT'";
				} else if ("TICKET_WISE"
						.equals(LMSFilterDispatcher.seSaleReportType)) {
					saleQry = "select organization_id,name,ifnull(mrpAmt,0.0) mrpAmt,ifnull(netAmt,0.0) netAmt from st_lms_organization_master left outer join (select current_owner_id,sum(soldTkt*ticket_price) mrpAmt,sum((soldTkt*ticket_price)-(soldTkt*ticket_price*transacrion_sale_comm_rate*0.01)) netAmt from st_se_game_master gm,st_se_game_inv_detail gid,(select game_id,book_nbr,sum(sold_tickets) soldTkt from st_se_game_ticket_inv_history where date>='"
							+ startDate
							+ "' and date<='"
							+ endDate
							+ "' and current_owner='RETAILER' group by book_nbr) TktTlb where gm.game_id=TktTlb.game_id and TktTlb.book_nbr=gid.book_nbr and gid.current_owner='AGENT' group by current_owner_id) saleTlb on organization_id=current_owner_id where organization_type='AGENT'";
				}

				pstmt = con.prepareStatement(saleQry);
				logger.info("***Scratch Sale Query*** \n" + pstmt);
				rs = pstmt.executeQuery();

				while (rs.next()) {
					agtMap.get(rs.getString("organization_id")).setScratchSale(
							rs.getDouble("netAmt"));
				}

				// Calculate Scratch Pwt
				String pwtQry = "select bb.parent_id,sum(pwt) as pwt from (select retailer_org_id,sum(pwt_amt+(pwt_amt*agt_claim_comm*0.01)) pwt from st_se_retailer_pwt where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_date>=? and transaction_date<=? and transaction_type='PWT' and retailer_org_id in (select organization_id from st_lms_organization_master where organization_type='RETAILER')) group by retailer_org_id union all select retailer_org_id,sum(pwt_amt+(pwt_amt*claim_comm*0.01)) pwt from st_se_agent_pwt where transaction_id in (select transaction_id from st_lms_agent_transaction_master where transaction_date>=? and transaction_date<=? and transaction_type='PWT' and retailer_org_id in (select organization_id from st_lms_organization_master where organization_type='RETAILER')) group by retailer_org_id) aa,st_lms_organization_master bb where retailer_org_id=organization_id group by parent_id";
				pstmt = con.prepareStatement(pwtQry);
				pstmt.setTimestamp(1, startDate);
				pstmt.setTimestamp(2, endDate);
				pstmt.setTimestamp(3, startDate);
				pstmt.setTimestamp(4, endDate);
				logger.info("***Scratch Pwt Query*** \n" + pstmt);
				rs = pstmt.executeQuery();

				while (rs.next()) {
					agtMap.get(rs.getString("parent_id")).setScratchPwt(
							rs.getDouble("pwt"));
				}

				// Scratch Direct Player Qry

				String dirPwtQry = "select agent_org_id,sum(pwtDir) pwtDir from (select agent_org_id,sum(pwt_amt+(pwt_amt*claim_comm*0.01)) pwtDir from st_se_agt_direct_player_pwt where transaction_date>=? and transaction_date<=? group by agent_org_id union all select agent_org_id,sum(pwt_amt+comm_amt) pwt from st_se_bo_pwt where transaction_id in (select transaction_id from st_lms_bo_transaction_master where transaction_date>=? and transaction_date<=? and transaction_type='PWT' and agent_org_id in (select organization_id from st_lms_organization_master where organization_type='AGENT')) group by agent_org_id) pwt group by agent_org_id";
				pstmt = con.prepareStatement(dirPwtQry);
				pstmt.setTimestamp(1, startDate);
				pstmt.setTimestamp(2, endDate);
				pstmt.setTimestamp(3, startDate);
				pstmt.setTimestamp(4, endDate);
				logger.info("-------Scratch Direct Player Qry------\n"
						+ pstmt);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					agtMap.get(rs.getString("agent_org_id"))
							.setScratchDirPlyPwt(rs.getDouble("pwtDir"));
				}
			}
			if(ReportUtility.isCS){

				saleTranCS = "select transaction_id from st_lms_retailer_transaction_master where transaction_type ='CS_SALE' and transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "' and retailer_org_id in (select organization_id from st_lms_organization_master where organization_type='RETAILER')";
				cancelTranCS = "select transaction_id from st_lms_retailer_transaction_master where transaction_type in('CS_CANCEL_SERVER','CS_CANCEL_RET') and transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "' and retailer_org_id in (select organization_id from st_lms_organization_master where organization_type='RETAILER')";

				// Category Master Query
				String catQry = "select category_id from st_cs_product_category_master where status = 'ACTIVE'";
				PreparedStatement prodPstmt = con.prepareStatement(catQry);
				rsGame = prodPstmt.executeQuery();
				StringBuilder saleQry = new StringBuilder(
						"select parent_id,sum(sale) as sale from (");
				StringBuilder cancelQry = new StringBuilder(
						"select parent_id,sum(cancel) as cancel from (");
				while (rsGame.next()) {
					saleQry
							.append("(select bb.parent_id,sum(sale) as sale from (select cs.retailer_org_id,sum(agent_net_amt) as sale from st_cs_sale_"
									+ rsGame.getInt("category_id")
									+ " cs where transaction_id in ("
									+ saleTranCS
									+ ") group by cs.retailer_org_id) aa,st_lms_organization_master bb where retailer_org_id=organization_id group by parent_id) union all");

					cancelQry
							.append("(select bb.parent_id,sum(cancel) as cancel from (select cs.retailer_org_id,sum(agent_net_amt) as cancel from st_cs_refund_"
									+ rsGame.getInt("category_id")
									+ " cs where transaction_id in ("
									+ cancelTranCS
									+ ") group by cs.retailer_org_id) aa,st_lms_organization_master bb where retailer_org_id=organization_id group by parent_id) union all");

				}

				saleQry.delete(saleQry.lastIndexOf("union all"), saleQry
						.length());
				cancelQry.delete(cancelQry.lastIndexOf("union all"), cancelQry
						.length());

				saleQry.append(") saleTlb group by parent_id");
				cancelQry.append(") cancelTlb group by parent_id");

				logger.info("-------CS Sale Query------\n" + saleQry);
				logger.info("-------CS Cancel Query------\n"
						+ cancelQry);

				// CS Sale Query
				pstmt = con.prepareStatement(saleQry.toString());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					agtMap.get(rs.getString("parent_id")).setCSSale(
							rs.getDouble("sale"));
				}
				// CS Cancel Query
				pstmt = con.prepareStatement(cancelQry.toString());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					agtMap.get(rs.getString("parent_id")).setCSCancel(
							rs.getDouble("cancel"));
				}
			}
			if (ReportUtility.isOLA) {
				depositTranOLA = "select transaction_id from st_lms_retailer_transaction_master where transaction_type in('OLA_DEPOSIT') and transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "' and retailer_org_id in (select organization_id from st_lms_organization_master where organization_type='RETAILER')";
				cancelTranOLA = "select transaction_id from st_lms_retailer_transaction_master where transaction_type in('OLA_DEPOSIT_REFUND') and transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "' and retailer_org_id in (select organization_id from st_lms_organization_master where organization_type='RETAILER')";
				withdrawalOLA = "select transaction_id from st_lms_retailer_transaction_master where transaction_type in('OLA_WITHDRAWL') and transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "' and retailer_org_id in (select organization_id from st_lms_organization_master where organization_type='RETAILER')";

				StringBuilder depositQry = new StringBuilder(
						"select parent_id,sum(deposit_amt) as deposit_amt from (");
				StringBuilder depositcancelQry = new StringBuilder(
						"select parent_id,sum(deposit_amt) as deposit_amt_refund from (");
				StringBuilder withdrawalQry = new StringBuilder(
						"select parent_id,sum(withdrawl_amt) as withdrawl_amt from (");
				
				depositQry.append("(select bb.parent_id,sum(deposit_amt) as deposit_amt from (select drs.retailer_org_id,sum(agent_net_amt) as deposit_amt from st_ola_ret_deposit drs where transaction_id in ("
									+ depositTranOLA
									+ ") group by drs.retailer_org_id) aa,st_lms_organization_master bb where retailer_org_id=organization_id group by parent_id)");

				depositcancelQry
							.append("(select bb.parent_id,sum(deposit_amt) as deposit_amt from (select drs.retailer_org_id,sum(agent_net_amt) as deposit_amt from st_ola_ret_deposit_refund drs where transaction_id in ("
									+ cancelTranOLA
									+ ") group by drs.retailer_org_id) aa,st_lms_organization_master bb where retailer_org_id=organization_id group by parent_id)");

				withdrawalQry
							.append("(select bb.parent_id,sum(withdrawl_amt) as withdrawl_amt from (select drs.retailer_org_id,sum(agent_net_amt) as withdrawl_amt from st_ola_ret_withdrawl drs where transaction_id in ("
									+ withdrawalOLA
									+ ") group by drs.retailer_org_id) aa,st_lms_organization_master bb where retailer_org_id=organization_id group by parent_id)");

				String netGamingQry = "select om.organization_id agtOrgId, ifnull(sum(commission_calculated),0.0) netAmt from (select agt_org_id, net_gaming, commission_calculated from st_ola_bo_agt_commission where transaction_id in (select transaction_id from st_lms_bo_transaction_master where transaction_type = 'OLA_COMMISSION' and transaction_date >= '"
								+ startDate
								+ "' and transaction_date <= '"
								+ endDate
								+ "')) bac right outer join (select organization_id from st_lms_organization_master where organization_type = 'AGENT')om on bac.agt_org_id = om.organization_id group by om.organization_id";
				
				
				depositQry.append(") depositTlb group by parent_id");
				depositcancelQry.append(") cancelTlb group by parent_id");
				withdrawalQry.append(") withdrawalTlb group by parent_id");
				System.out.println("-------OLA Deposit Qurey------\n" + depositQry);
				System.out.println("-------OLA Deposit Cancel Qurey------\n"+ depositcancelQry);
				System.out.println("-------OLA Withdrawal Qurey------\n" + withdrawalQry);

				// OLA Deposit Query
				pstmt = con.prepareStatement(depositQry.toString());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					agtMap.get(rs.getString("parent_id")).setOlaDepositAmt(
							rs.getDouble("deposit_amt"));
				}
				// OLA Deposit Cancel Query
				pstmt = con.prepareStatement(depositcancelQry.toString());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					agtMap.get(rs.getString("parent_id")).setOlaDepositCancelAmt(
							rs.getDouble("deposit_amt_refund"));
				}
				// OLA Withdrawal Query
				pstmt = con.prepareStatement(withdrawalQry.toString());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					agtMap.get(rs.getString("parent_id")).setOlaWithdrawalAmt(
							rs.getDouble("withdrawl_amt"));
				}
				// OLA Net Gaming Query
				pstmt = con.prepareStatement(netGamingQry);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					agtMap.get(rs.getString("agtOrgId")).setOlaNetGaming(
							rs.getDouble("netAmt"));
				}
			
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return agtMap;
	}

	public Map<String, Map<String, Map<String, CompleteCollectionBean>>> collectionAgentWiseExpand(
			Timestamp startDate, Timestamp endDate, Connection con) {
		PreparedStatement pstmt = null;
		ResultSet rsGame = null;
		ResultSet rs = null;
		ResultSet rsRetOrg = null;
		Map<String, Map<String, Map<String, CompleteCollectionBean>>> serGameAgtMap = new LinkedHashMap<String, Map<String, Map<String, CompleteCollectionBean>>>();
		Map<String, Map<String, CompleteCollectionBean>> gameAgtMap = null;
		// for Draw Game
		String saleTranDG = null;
		String cancelTranDG = null;
		String pwtTranDG = null;
		String dirPlrPwt = null;
		StringBuilder drawQry = null;
		// for scratch game
		StringBuilder scratchQry = null;
		//for CS
		StringBuilder CSQry = null;
		String saleTranCS = null;
		String cancelTranCS = null;
		//for OLA
		StringBuilder OLAQry = null;
		String depositTranOLA = null;
		String cancelTranOLA = null;
		String withdrawalTranOLA = null;
		
		if (startDate.after(endDate)) {
			return serGameAgtMap;
		}
		try {

			// Get All Agent
		
			String agtOrgQry = QueryManager.getOrgQry("AGENT");
					
			
			pstmt = con.prepareStatement(agtOrgQry);
			rsRetOrg = pstmt.executeQuery();

			if (ReportUtility.isDG) {
				gameAgtMap = new LinkedHashMap<String, Map<String, CompleteCollectionBean>>();
				drawQry = new StringBuilder(
						"select organization_id,ifnull(sale,0.0) sale,ifnull(cancel,0.0) cancel,ifnull(pwt,0.0)  pwt from (select sale.parent_id,sale,cancel,pwt from ");
				saleTranDG = "(select bb.parent_id,sum(sale) as sale from (select drs.retailer_org_id,sum(agent_net_amt) as sale from st_dg_ret_sale_? drs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_SALE','DG_SALE_OFFLINE') and transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "') group by drs.retailer_org_id) aa right outer join (select organization_id,parent_id from st_lms_organization_master where organization_type='RETAILER')bb on retailer_org_id=organization_id group by parent_id) sale,";
				cancelTranDG = "(select bb.parent_id,sum(cancel) as cancel from (select drs.retailer_org_id,sum(agent_net_amt) as cancel from st_dg_ret_sale_refund_? drs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') and transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "') group by drs.retailer_org_id) aa right outer join (select organization_id,parent_id from st_lms_organization_master where organization_type='RETAILER') bb on retailer_org_id=organization_id group by parent_id) cancel,";
				pwtTranDG = "(select bb.parent_id,sum(pwt) as pwt from (select drs.retailer_org_id,sum(pwt_amt+agt_claim_comm) as pwt from st_dg_ret_pwt_? drs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') and transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "') group by drs.retailer_org_id) aa right outer join (select organization_id,parent_id from st_lms_organization_master where organization_type='RETAILER') bb on retailer_org_id=organization_id group by parent_id) pwt";
				drawQry.append(saleTranDG);
				drawQry.append(cancelTranDG);
				drawQry.append(pwtTranDG);
				drawQry
						.append(" where sale.parent_id=cancel.parent_id and sale.parent_id=pwt.parent_id and cancel.parent_id=pwt.parent_id) gameTlb right outer join (select organization_id from st_lms_organization_master where organization_type='AGENT') agtTlb on gameTlb.parent_id=agtTlb.organization_id");
				System.out.println("For Expand draw game Qry:: " + drawQry);

				dirPlrPwt = "select organization_id,ifnull(pwtDir,0.0) pwtDir from (select agent_org_id,sum(pwt_amt+agt_claim_comm) pwtDir from st_dg_agt_direct_plr_pwt where transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "' and game_id=? group by agent_org_id) aa right outer join (select organization_id from st_lms_organization_master where organization_type='AGENT') bb on agent_org_id=organization_id";

				System.out.println("For Expand draw game Qry Direct Ply:: "
						+ dirPlrPwt);
				// Game Master Query
				String gameQry = "select game_id,game_name from st_dg_game_master";
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				rsGame = gamePstmt.executeQuery();
				while (rsGame.next()) {
					int gameId = rsGame.getInt("game_id");
					Map<String, CompleteCollectionBean> gameMap = new LinkedHashMap<String, CompleteCollectionBean>();
					gameMap.putAll(getAgentMap(rsRetOrg));
					pstmt = con.prepareStatement(drawQry.toString());
					pstmt.setInt(1, gameId);
					pstmt.setInt(2, gameId);
					pstmt.setInt(3, gameId);
					System.out.println("-------Indivisual Game Qry-------\n"
							+ pstmt);
					rs = pstmt.executeQuery();
					while (rs.next()) {
						String agtId = rs.getString("organization_id");
						gameMap.get(agtId).setDrawSale(rs.getDouble("sale"));
						gameMap.get(agtId)
								.setDrawCancel(rs.getDouble("cancel"));
						gameMap.get(agtId).setDrawPwt(rs.getDouble("pwt"));

					}

					// Direct Player Pwt
					pstmt = con.prepareStatement(dirPlrPwt);
					pstmt.setInt(1, gameId);
					rs = pstmt.executeQuery();
					while (rs.next()) {
						String agtId = rs.getString("organization_id");
						gameMap.get(agtId).setDrawDirPlyPwt(
								rs.getDouble("pwtDir"));
					}
					gameAgtMap.put(rsGame.getString("game_name"), gameMap);
				}

				serGameAgtMap.put("DG", gameAgtMap);
			}
			if (ReportUtility.isSE) {
				gameAgtMap = new LinkedHashMap<String, Map<String, CompleteCollectionBean>>();
				scratchQry = new StringBuilder(
						"select organization_id,ifnull(sale,0.0) sale,ifnull(pwt,0.0) pwt from (select sale.parent_id,sale,pwt from");
				logger.info("----Type Select ---"
						+ LMSFilterDispatcher.seSaleReportType);
				if ("BOOK_WISE".equals(LMSFilterDispatcher.seSaleReportType)) {
					saleTranDG = "(select organization_id parent_id,name,ifnull(mrpAmt,0.0) mrpAmt,ifnull(netAmt,0.0) sale from st_lms_organization_master left outer join (select sale.agent_org_id,(ifnull(mrpAmt,0.0)-ifnull(mrpAmtRet,0.0)) mrpAmt,(ifnull(netAmt,0.0)-ifnull(netAmtRet,0.0)) netAmt from (select agent_org_id,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt from(select agent_org_id,sum(mrp_amt) mrpAmt,sum(net_amt) netAmt from st_se_bo_agent_transaction where game_id=?  and transaction_id in (select transaction_id from st_lms_bo_transaction_master where transaction_type='SALE' and transaction_date>='"
							+ startDate
							+ "'  and transaction_date<='"
							+ endDate
							+ "' ) group by agent_org_id union all select agent_org_id,sum(mrp_amt) mrpAmt,sum(net_amt) netAmt from st_se_bo_agent_loose_book_transaction where game_id=?  and transaction_id in (select transaction_id from st_lms_bo_transaction_master where transaction_type='LOOSE_SALE' and transaction_date>='"
							+ startDate
							+ "'  and transaction_date<='"
							+ endDate
							+ "' ) group by agent_org_id) sale group by agent_org_id ) sale left outer join (select agent_org_id,sum(mrpAmtRet) mrpAmtRet,sum(netAmtRet) netAmtRet from (select agent_org_id,sum(mrp_amt) mrpAmtRet,sum(net_amt) netAmtRet from st_se_bo_agent_transaction where game_id=?  and transaction_id in (select transaction_id from st_lms_bo_transaction_master where transaction_type='SALE_RET' and transaction_date>='"
							+ startDate
							+ "'  and transaction_date<='"
							+ endDate
							+ "' ) group by agent_org_id union all select agent_org_id,sum(mrp_amt) mrpAmtRet,sum(net_amt) netAmtRet from st_se_bo_agent_loose_book_transaction where game_id=?  and transaction_id in (select transaction_id from st_lms_bo_transaction_master where transaction_type='LOOSE_SALE_RET' and transaction_date>='"
							+ startDate
							+ "'  and transaction_date<='"
							+ endDate
							+ "' ) group by agent_org_id) saleRet  group by agent_org_id) saleRet on sale.agent_org_id=saleRet.agent_org_id) saleTlb on organization_id=agent_org_id where organization_type='AGENT') sale,";
				} else if ("TICKET_WISE"
						.equals(LMSFilterDispatcher.seSaleReportType)) {
					saleTranDG = "(select organization_id parent_id,name,ifnull(mrpAmt,0.0) mrpAmt,ifnull(netAmt,0.0) sale from st_lms_organization_master left outer join (select current_owner_id,sum(soldTkt*ticket_price) mrpAmt,sum((soldTkt*ticket_price)-(soldTkt*ticket_price*transacrion_sale_comm_rate*0.01)) netAmt from st_se_game_master gm,st_se_game_inv_detail gid,(select game_id,book_nbr,sum(sold_tickets) soldTkt from st_se_game_ticket_inv_history where date>='"
							+ startDate
							+ "' and date<='"
							+ endDate
							+ "' and current_owner='RETAILER' and game_id=? group by book_nbr) TktTlb where gm.game_id=? and gm.game_id=TktTlb.game_id and TktTlb.book_nbr=gid.book_nbr and gid.current_owner='AGENT' group by current_owner_id) saleTlb on organization_id=current_owner_id where organization_type='AGENT') sale,";
				}

				pwtTranDG = "(select bb.parent_id,sum(pwt) as pwt from (select retailer_org_id,sum(pwt_amt+(pwt_amt*agt_claim_comm*0.01)) pwt from st_se_retailer_pwt where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "' and transaction_type='PWT') and game_id=? group by retailer_org_id) aa right outer join (select organization_id,parent_id from st_lms_organization_master where organization_type='RETAILER') bb on retailer_org_id=organization_id group by parent_id) pwt";
				scratchQry.append(saleTranDG);
				scratchQry.append(pwtTranDG);
				scratchQry
						.append(" where sale.parent_id=pwt.parent_id) gameTlb right outer join (select organization_id from st_lms_organization_master where organization_type='AGENT') agtTlb on gameTlb.parent_id=agtTlb.organization_id");
				System.out.println("For Expand scratch game Qry:: "
						+ scratchQry);

				dirPlrPwt = "select organization_id,ifnull(pwtDir,0.0) pwtDir from (select agent_org_id,sum(pwt_amt+(pwt_amt*claim_comm*0.01)) pwtDir from st_se_agt_direct_player_pwt where transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "' and game_id=? group by agent_org_id) aa right outer join (select organization_id from st_lms_organization_master where organization_type='AGENT') bb on agent_org_id=organization_id";

				System.out.println("For Expand scratch game Qry Direct Ply:: "
						+ dirPlrPwt);
				// Game Master Query
				String gameQry = "select game_id,game_name from st_se_game_master";
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				rsGame = gamePstmt.executeQuery();
				while (rsGame.next()) {
					int gameId = rsGame.getInt("game_id");
					Map<String, CompleteCollectionBean> gameMap = new LinkedHashMap<String, CompleteCollectionBean>();
					gameMap.putAll(getAgentMap(rsRetOrg));
					pstmt = con.prepareStatement(scratchQry.toString());
					pstmt.setInt(1, gameId);
					pstmt.setInt(2, gameId);
					pstmt.setInt(3, gameId);
					pstmt.setInt(4, gameId);
					pstmt.setInt(5, gameId);

					System.out.println("-------Indivisual Game Qry-------\n"
							+ pstmt);
					rs = pstmt.executeQuery();
					while (rs.next()) {
						String agtId = rs.getString("organization_id");
						gameMap.get(agtId).setDrawSale(rs.getDouble("sale"));
						gameMap.get(agtId).setDrawPwt(rs.getDouble("pwt"));
					}

					// Direct Player Pwt
					pstmt = con.prepareStatement(dirPlrPwt);
					pstmt.setInt(1, gameId);
					rs = pstmt.executeQuery();
					while (rs.next()) {
						String agtId = rs.getString("organization_id");
						gameMap.get(agtId).setScratchDirPlyPwt(
								rs.getDouble("pwtDir"));
					}

					gameAgtMap.put(rsGame.getString("game_name"), gameMap);
				}
				serGameAgtMap.put("SE", gameAgtMap);
			}
			if(ReportUtility.isCS){

				gameAgtMap = new LinkedHashMap<String, Map<String, CompleteCollectionBean>>();
				CSQry = new StringBuilder(
						"select organization_id,ifnull(sale,0.0) sale,ifnull(cancel,0.0) cancel from (select sale.parent_id,sale,cancel from ");
				saleTranCS = "(select bb.parent_id,sum(sale) as sale from (select cs.retailer_org_id,sum(agent_net_amt) as sale from st_cs_sale_? cs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type = 'CS_SALE' and transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "') group by cs.retailer_org_id) aa right outer join (select organization_id,parent_id from st_lms_organization_master where organization_type='RETAILER')bb on retailer_org_id=organization_id group by parent_id) sale,";
				cancelTranCS = "(select bb.parent_id,sum(cancel) as cancel from (select cs.retailer_org_id,sum(agent_net_amt) as cancel from st_cs_refund_? cs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type in('CS_CANCEL_SERVER','CS_CANCEL_RET') and transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "') group by cs.retailer_org_id) aa right outer join (select organization_id,parent_id from st_lms_organization_master where organization_type='RETAILER') bb on retailer_org_id=organization_id group by parent_id) cancel ";
				CSQry.append(saleTranCS);
				CSQry.append(cancelTranCS);
				CSQry
						.append(" where sale.parent_id=cancel.parent_id) gameTlb right outer join (select organization_id from st_lms_organization_master where organization_type='AGENT') agtTlb on gameTlb.parent_id=agtTlb.organization_id");
				logger.debug("For Expand draw game Qry:: " + CSQry);

				// Category Master Query
				String catQry = "select category_id,category_code from st_cs_product_category_master";
				PreparedStatement prodPstmt = con.prepareStatement(catQry);
				rsGame = prodPstmt.executeQuery();
				while (rsGame.next()) {
					int catId = rsGame.getInt("category_id");
					Map<String, CompleteCollectionBean> prodMap = new LinkedHashMap<String, CompleteCollectionBean>();
					prodMap.putAll(getAgentMap(rsRetOrg));
					pstmt = con.prepareStatement(CSQry.toString());
					pstmt.setInt(1, catId);
					pstmt.setInt(2, catId);
					logger.debug("-------Indivisual Game Qry-------\n"
							+ pstmt);
					rs = pstmt.executeQuery();
					while (rs.next()) {
						String agtId = rs.getString("organization_id");
						prodMap.get(agtId).setCSSale(rs.getDouble("sale"));
						prodMap.get(agtId).setCSCancel(rs.getDouble("cancel"));
					}
					gameAgtMap.put(rsGame.getString("category_code"), prodMap);
				}

				serGameAgtMap.put("CS", gameAgtMap);
			
			}
			if (ReportUtility.isOLA) {
				gameAgtMap = new LinkedHashMap<String, Map<String, CompleteCollectionBean>>();
				OLAQry = new StringBuilder(
						"select organization_id,deposit_amt,deposit_refund_amt,withdrawl_amt,total_amt from(select organization_id,ifnull(deposit_amt,0.0) deposit_amt,ifnull(deposit_refund_amt,0.0) deposit_refund_amt,ifnull(withdrawl_amt,0.0)  withdrawl_amt from (select deposit.parent_id,deposit_amt,deposit_refund_amt,withdrawl_amt from ");
				depositTranOLA = "(select bb.parent_id,sum(deposit_amt) as deposit_amt from (select drs.retailer_org_id,sum(agent_net_amt) as deposit_amt from st_ola_ret_deposit drs where wallet_id=? and transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type in('OLA_DEPOSIT') and transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "') group by drs.retailer_org_id) aa right outer join (select organization_id,parent_id from st_lms_organization_master where organization_type='RETAILER')bb on retailer_org_id=organization_id group by parent_id) deposit,";
				cancelTranOLA = "(select bb.parent_id,sum(deposit_refund_amt) as deposit_refund_amt from (select drs.retailer_org_id,sum(agent_net_amt) as deposit_refund_amt from st_ola_ret_deposit_refund drs where wallet_id=? and transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type in('OLA_DEPOSIT_REFUND') and transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "') group by drs.retailer_org_id) aa right outer join (select organization_id,parent_id from st_lms_organization_master where organization_type='RETAILER') bb on retailer_org_id=organization_id group by parent_id) cancel,";
				withdrawalTranOLA = "(select bb.parent_id,sum(withdrawl_amt) as withdrawl_amt from (select drs.retailer_org_id,sum(agent_net_amt) as withdrawl_amt from st_ola_ret_withdrawl drs where wallet_id=? and transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type in('OLA_WITHDRAWL') and transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "') group by drs.retailer_org_id) aa right outer join (select organization_id,parent_id from st_lms_organization_master where organization_type='RETAILER') bb on retailer_org_id=organization_id group by parent_id) withdrawal";
				OLAQry.append(depositTranOLA);
				OLAQry.append(cancelTranOLA);
				OLAQry.append(withdrawalTranOLA);
				OLAQry
						.append(" where deposit.parent_id=cancel.parent_id and deposit.parent_id=withdrawal.parent_id and cancel.parent_id=withdrawal.parent_id) depositTlb right outer join (select organization_id from st_lms_organization_master where organization_type='AGENT') agtTlb on depositTlb.parent_id=agtTlb.organization_id");
				OLAQry.append(")totalTab,(select bb.organization_id agtOrgId,sum(total_amt) as total_amt from (select agt_org_id,sum(commission_calculated) as total_amt from st_ola_bo_agt_commission drs where wallet_id=? and transaction_id in (select transaction_id from st_lms_bo_transaction_master where transaction_type in('OLA_COMMISSION') and transaction_date>='"+startDate+"' and transaction_date<='"+endDate+"') group by agt_org_id) aa right outer join (select organization_id from st_lms_organization_master where organization_type='AGENT')bb on aa.agt_org_id=bb.organization_id group by organization_id)netGaming where totalTab.organization_id=netGaming.agtOrgId");
				System.out.println("OLA Deposit Qry:: " + OLAQry);

				// Wallet Master Query
				String catQry = "select wallet_id,wallet_display_name from st_ola_wallet_master";
				PreparedStatement prodPstmt = con.prepareStatement(catQry);
				rsGame = prodPstmt.executeQuery();
				while (rsGame.next()) {
					int walletIdId = rsGame.getInt("wallet_id");
					Map<String, CompleteCollectionBean> walletMap = new LinkedHashMap<String, CompleteCollectionBean>();
					walletMap.putAll(getAgentMap(rsRetOrg));
					pstmt = con.prepareStatement(OLAQry.toString());
					pstmt.setInt(1, walletIdId);
					pstmt.setInt(2, walletIdId);
					pstmt.setInt(3, walletIdId);
					pstmt.setInt(4, walletIdId);
					rs = pstmt.executeQuery();
					while (rs.next()) {
						String agtId = rs.getString("organization_id");
						walletMap.get(agtId).setOlaDepositAmt(rs.getDouble("deposit_amt"));
						walletMap.get(agtId)
								.setOlaDepositCancelAmt(rs.getDouble("deposit_refund_amt"));
						walletMap.get(agtId).setOlaWithdrawalAmt(rs.getDouble("withdrawl_amt"));
						walletMap.get(agtId).setOlaNetGaming(rs.getDouble("total_amt"));
					}

					gameAgtMap.put(rsGame.getString("wallet_display_name"), walletMap);
				}
					serGameAgtMap.put("OLA", gameAgtMap);
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return serGameAgtMap;
	}

	public Map<String, CompleteCollectionBean> collectionDayWise(
			Timestamp startDate, Timestamp endDate, Connection con, String viewBy, int orgId) {
		PreparedStatement pstmt = null;
		ResultSet rsGame = null;
		ResultSet rs = null;
		Map<String, CompleteCollectionBean> dateMap = new LinkedHashMap<String, CompleteCollectionBean>();
		CompleteCollectionBean collBean = null;
		String date = null;
		// for Draw Game
		String saleTranDG = null;
		String cancelTranDG = null;
		String pwtTranDG = null;
		//for CS
		String saleTranCS = null;
		String cancelTranCS = null;
		
		//for OLA
		String depositTranOLA = null;
		String depositCancelTranOLA = null;
		String withdrawalTranOLA = null;
		
		if (startDate.after(endDate)) {
			return dateMap;
		}

		String addDrawQry = "";
		String addOlaQry = "";
		String addDirPlrQry = "";
		String addScratchPwtQry = "";
		String addCSQry = "";

		// For Column which is used

		String dgSaleCol = "agent_net_amt";
		String csSaleCol = "agent_net_amt";
		String olaDepositCol = "agent_net_amt";
		String olaWithdrawalCol = "agent_net_amt";
		String dgPwtCol = "pwt_amt+agt_claim_comm";
	
		String sePwtCol = "pwt_amt+(pwt_amt*agt_claim_comm*0.01)";
		boolean viewByAgt = false;
		if ("AGENT".equals(viewBy) && orgId != 0) {
			viewByAgt = true;
			dgSaleCol = "net_amt";
			csSaleCol = "net_amt";
			olaDepositCol = "net_amt";
			olaWithdrawalCol = "net_amt";
			dgPwtCol = "pwt_amt+retailer_claim_comm";
			sePwtCol = "pwt_amt+(pwt_amt*claim_comm*0.01)";
			addDrawQry = " and drs.retailer_org_id in (select organization_id from st_lms_organization_master where parent_id="
					+ orgId + ")";
			addOlaQry = " and drs.retailer_org_id in (select organization_id from st_lms_organization_master where parent_id="
				+ orgId + ")";
			addDirPlrQry = " and agent_org_id=" + orgId;
			addScratchPwtQry = " and rp.retailer_org_id in (select organization_id from st_lms_organization_master where parent_id="
					+ orgId + ")";
			addCSQry = "and cs.retailer_org_id in (select organization_id from st_lms_organization_master where parent_id=";
		}

		// for scratch game
		try {

			// Get All Agent
			Calendar startCal = Calendar.getInstance();
			startCal.setTime(startDate);

			Calendar endCal = Calendar.getInstance();
			endCal.setTime(endDate);
			while (startCal.getTime().before(endCal.getTime())) {
				date = new java.sql.Date(startCal.getTimeInMillis()).toString();
				collBean = new CompleteCollectionBean();
				collBean.setOrgName(date);
				dateMap.put(date, collBean);
				startCal.setTimeInMillis(startCal.getTimeInMillis() + 24 * 60
						* 60 * 1000);
				if (ReportUtility.isDG) {
					collBean.setDrawSale(0.0);
					collBean.setDrawPwt(0.0);
					collBean.setDrawCancel(0.0);
					collBean.setDrawDirPlyPwt(0.0);
				}
				if (ReportUtility.isSE) {
					collBean.setScratchSale(0.0);
					collBean.setScratchPwt(0.0);
					collBean.setScratchDirPlyPwt(0.0);
				}
				if (ReportUtility.isCS) {
					collBean.setCSSale(0.0);
					collBean.setCSCancel(0.0);
				}
				if(ReportUtility.isOLA)
				{
					collBean.setOlaDepositAmt(0.0);
					collBean.setOlaWithdrawalAmt(0.0);
					collBean.setOlaDepositCancelAmt(0.0);
					collBean.setOlaNetGaming(0.0);
				}
			}
			

			if (ReportUtility.isDG) {
				saleTranDG = "drs,st_lms_retailer_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='"
						+ startDate
						+ "' and rtm.transaction_date<='"
						+ endDate
						+ "' and transaction_type in('DG_SALE','DG_SALE_OFFLINE') "
						+ addDrawQry + " group by tx_date";
				cancelTranDG = "drs,st_lms_retailer_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='"
						+ startDate
						+ "' and rtm.transaction_date<='"
						+ endDate
						+ "' and transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') "
						+ addDrawQry + " group by tx_date";
				pwtTranDG = "drs,st_lms_retailer_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='"
						+ startDate
						+ "' and rtm.transaction_date<='"
						+ endDate
						+ "' and transaction_type in('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') "
						+ addDrawQry + " group by tx_date";

				// Game Master Query
				String gameQry = ReportUtility.getDrawGameMapQuery(startDate);
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				rsGame = gamePstmt.executeQuery();
				StringBuilder saleQry = new StringBuilder(
						"select tx_date,sum(sale) sale from (");
				StringBuilder cancelQry = new StringBuilder(
						"select tx_date,sum(cancel) cancel from (");
				StringBuilder pwtQry = new StringBuilder(
						"select tx_date,sum(pwt) pwt from (");
				while (rsGame.next()) {
					saleQry
							.append("(select SUBSTRING_INDEX(transaction_date,' ',1) tx_date,sum("
									+ dgSaleCol
									+ ") sale from st_dg_ret_sale_"
									+ rsGame.getInt("game_id")
									+ " "
									+ saleTranDG + ") union all");

					cancelQry
							.append("(select SUBSTRING_INDEX(transaction_date,' ',1) tx_date,sum("
									+ dgSaleCol
									+ ") cancel from st_dg_ret_sale_refund_"
									+ rsGame.getInt("game_id")
									+ " "
									+ cancelTranDG + ") union all");

					pwtQry
							.append("(select SUBSTRING_INDEX(transaction_date,' ',1) tx_date,sum("
									+ dgPwtCol
									+ ") pwt from st_dg_ret_pwt_"
									+ rsGame.getInt("game_id")
									+ " "
									+ pwtTranDG + ") union all");

				}

				saleQry.delete(saleQry.lastIndexOf("union all"), saleQry
						.length());
				cancelQry.delete(cancelQry.lastIndexOf("union all"), cancelQry
						.length());
				pwtQry.delete(pwtQry.lastIndexOf("union all"), pwtQry.length());

				saleQry.append(") saleTlb group by tx_date");
				cancelQry.append(") cancelTlb group by tx_date");
				pwtQry.append(") pwtTlb group by tx_date");
				System.out.println("-------Draw Sale Qurey------\n" + saleQry);
				System.out.println("-------Draw Cancel Qurey------\n"
						+ cancelQry);
				System.out.println("-------Draw Pwt Qurey------\n" + pwtQry);

				// Draw Sale Query
				pstmt = con.prepareStatement(saleQry.toString());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					dateMap.get(rs.getString("tx_date")).setDrawSale(
							rs.getDouble("sale"));
				}
				// Draw Cancel Query
				pstmt = con.prepareStatement(cancelQry.toString());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					dateMap.get(rs.getString("tx_date")).setDrawCancel(
							rs.getDouble("cancel"));
				}
				// Draw Pwt Query
				pstmt = con.prepareStatement(pwtQry.toString());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					dateMap.get(rs.getString("tx_date")).setDrawPwt(
							rs.getDouble("pwt"));
				}

				// Draw Direct Player Qry
				String dirPwtQry = "select tx_date,sum(net_amt) pwtDir from (select SUBSTRING_INDEX(transaction_date,' ',1) tx_date ,(pwt_amt+agt_claim_comm) net_amt from st_dg_agt_direct_plr_pwt where transaction_date>=? and transaction_date<=? "
						+ addDirPlrQry + ") aa group by tx_date";
				pstmt = con.prepareStatement(dirPwtQry);
				pstmt.setTimestamp(1, startDate);
				pstmt.setTimestamp(2, endDate);
				System.out.println("-------Draw Direct Player Qry------\n"
						+ pstmt);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					dateMap.get(rs.getString("tx_date")).setDrawDirPlyPwt(
							rs.getDouble("pwtDir"));
				}
			}
			if (ReportUtility.isSE) {
				String saleQry = "";
				logger.info("----Type Select ---"
						+ LMSFilterDispatcher.seSaleReportType);
				if ("BOOK_WISE".equals(LMSFilterDispatcher.seSaleReportType)) {
					if (viewByAgt) {
						saleQry = "select transaction_date date,mrpAmt, netAmt sale from( select sale.transaction_date, mrpAmt-mrpAmtRet mrpAmt,netAmt-netAmtRet netAmt from (select transaction_date,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt from (select transaction_date,sum(mrp_amt) mrpAmt,sum(net_amt) netAmt from st_se_bo_agent_transaction sale,(select transaction_id,SUBSTRING_INDEX(transaction_date,' ',1) transaction_date from st_lms_bo_transaction_master where transaction_type='SALE' and  transaction_date>='"
								+ startDate
								+ "'  and  transaction_date<='"
								+ endDate
								+ "' ) tranTlb where sale.transaction_id=tranTlb.transaction_id  and agent_org_id="
								+ orgId
								+ "   group by transaction_date union all select transaction_date,sum(mrp_amt) mrpAmt,sum(net_amt) netAmt from st_se_bo_agent_loose_book_transaction sale,(select transaction_id,SUBSTRING_INDEX(transaction_date,' ',1) transaction_date from st_lms_bo_transaction_master where transaction_type='LOOSE_SALE' and  transaction_date>='"
								+ startDate
								+ "'  and  transaction_date<='"
								+ endDate
								+ "' ) tranTlb where sale.transaction_id=tranTlb.transaction_id  and agent_org_id="
								+ orgId
								+ "   group by transaction_date) sale group by transaction_date) sale left join (select transaction_date,sum(mrpAmtRet) mrpAmtRet,sum(netAmtRet) netAmtRet from (select transaction_date,sum(mrp_amt) mrpAmtRet,sum(net_amt) netAmtRet from st_se_bo_agent_transaction sale,(select transaction_id,SUBSTRING_INDEX(transaction_date,' ',1) transaction_date from st_lms_bo_transaction_master where transaction_type='SALE_RET' and  transaction_date>='"
								+ startDate
								+ "'  and  transaction_date<='"
								+ endDate
								+ "' ) tranTlb where sale.transaction_id=tranTlb.transaction_id  and agent_org_id="
								+ orgId
								+ "   group by transaction_date union all select transaction_date,sum(mrp_amt) mrpAmtRet,sum(net_amt) netAmtRet from st_se_bo_agent_loose_book_transaction sale,(select transaction_id,SUBSTRING_INDEX(transaction_date,' ',1) transaction_date from st_lms_bo_transaction_master where transaction_type='LOOSE_SALE_RET' and   transaction_date>='"
								+ startDate
								+ "'  and  transaction_date<='"
								+ endDate
								+ "' ) tranTlb where sale.transaction_id=tranTlb.transaction_id  and agent_org_id="
								+ orgId
								+ "   group by transaction_date) saleRet group by transaction_date ) saleRet on   sale.transaction_date=saleRet.transaction_date 	union select saleRet.transaction_date, ifnull(mrpAmt,0)-ifnull(mrpAmtRet,0) mrpAmt,ifnull(netAmt,0)-ifnull(netAmtRet,0) netAmt from (select transaction_date,ifnull(sum(mrpAmt),0) mrpAmt,ifnull(sum(netAmt),0) netAmt from (select transaction_date,sum(mrp_amt) mrpAmt,sum(net_amt) netAmt from st_se_bo_agent_transaction sale,(select transaction_id,SUBSTRING_INDEX(transaction_date,' ',1) transaction_date from st_lms_bo_transaction_master where transaction_type='SALE' and  transaction_date>='"
								+ startDate
								+ "'  and  transaction_date<='"
								+ endDate
								+ "' ) tranTlb where sale.transaction_id=tranTlb.transaction_id  and agent_org_id="
								+ orgId
								+ "   group by transaction_date union all select transaction_date,ifnull(sum(mrp_amt),0) mrpAmt,ifnull(sum(net_amt),0) netAmt from st_se_bo_agent_loose_book_transaction sale,(select transaction_id,SUBSTRING_INDEX(transaction_date,' ',1) transaction_date from st_lms_bo_transaction_master where transaction_type='LOOSE_SALE' and  transaction_date>='"
								+ startDate
								+ "'  and  transaction_date<='"
								+ endDate
								+ "' ) tranTlb where sale.transaction_id=tranTlb.transaction_id  and agent_org_id="
								+ orgId
								+ "   group by transaction_date) sale group by transaction_date) sale right join (select transaction_date,ifnull(sum(mrpAmtRet),0) mrpAmtRet,ifnull(sum(netAmtRet),0) netAmtRet from (select transaction_date,sum(mrp_amt) mrpAmtRet,sum(net_amt) netAmtRet from st_se_bo_agent_transaction sale,(select transaction_id,SUBSTRING_INDEX(transaction_date,' ',1) transaction_date from st_lms_bo_transaction_master where transaction_type='SALE_RET' and  transaction_date>='"
								+ startDate
								+ "'  and  transaction_date<='"
								+ endDate
								+ "' ) tranTlb where sale.transaction_id=tranTlb.transaction_id  and agent_org_id="
								+ orgId
								+ "   group by transaction_date union all select transaction_date,ifnull(sum(mrp_amt),0) mrpAmtRet,ifnull(sum(net_amt),0) netAmtRet from  st_se_bo_agent_loose_book_transaction sale,(select transaction_id,SUBSTRING_INDEX(transaction_date,' ',1) transaction_date from st_lms_bo_transaction_master where transaction_type='LOOSE_SALE_RET' and   transaction_date>='"
								+ startDate
								+ "'  and  transaction_date<='"
								+ endDate
								+ "' ) tranTlb where sale.transaction_id=tranTlb.transaction_id  and agent_org_id="
								+ orgId
								+ "   group by transaction_date) saleRet group by transaction_date ) saleRet on   sale.transaction_date=saleRet.transaction_date) SALE";
					} else {
						saleQry = "select transaction_date date,mrpAmt, netAmt sale from( select sale.transaction_date, mrpAmt-mrpAmtRet mrpAmt,netAmt-netAmtRet netAmt from (select transaction_date,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt from (select transaction_date,sum(mrp_amt) mrpAmt,sum(net_amt) netAmt from st_se_bo_agent_transaction sale,(select transaction_id,SUBSTRING_INDEX(transaction_date,' ',1) transaction_date from st_lms_bo_transaction_master where transaction_type='SALE' and  transaction_date>='"
								+ startDate
								+ "'  and  transaction_date<='"
								+ endDate
								+ "' ) tranTlb where sale.transaction_id=tranTlb.transaction_id group by transaction_date union all select transaction_date,sum(mrp_amt) mrpAmt,sum(net_amt) netAmt from st_se_bo_agent_loose_book_transaction sale,(select transaction_id,SUBSTRING_INDEX(transaction_date,' ',1) transaction_date from st_lms_bo_transaction_master where transaction_type='LOOSE_SALE' and  transaction_date>='"
								+ startDate
								+ "'  and  transaction_date<='"
								+ endDate
								+ "' ) tranTlb where sale.transaction_id=tranTlb.transaction_id group by transaction_date) sale group by transaction_date) sale left join (select transaction_date,sum(mrpAmtRet) mrpAmtRet,sum(netAmtRet) netAmtRet from (select transaction_date,sum(mrp_amt) mrpAmtRet,sum(net_amt) netAmtRet from st_se_bo_agent_transaction sale,(select transaction_id,SUBSTRING_INDEX(transaction_date,' ',1) transaction_date from st_lms_bo_transaction_master where transaction_type='SALE_RET' and  transaction_date>='"
								+ startDate
								+ "'  and  transaction_date<='"
								+ endDate
								+ "' ) tranTlb where sale.transaction_id=tranTlb.transaction_id group by transaction_date union all select transaction_date,sum(mrp_amt) mrpAmtRet,sum(net_amt) netAmtRet from st_se_bo_agent_loose_book_transaction sale,(select transaction_id,SUBSTRING_INDEX(transaction_date,' ',1) transaction_date from st_lms_bo_transaction_master where transaction_type='LOOSE_SALE_RET' and   transaction_date>='"
								+ startDate
								+ "'  and  transaction_date<='"
								+ endDate
								+ "' ) tranTlb where sale.transaction_id=tranTlb.transaction_id group by transaction_date) saleRet group by transaction_date) saleRet on   sale.transaction_date=saleRet.transaction_date 	union  select saleRet.transaction_date, ifnull(mrpAmt,0)-ifnull(mrpAmtRet,0) mrpAmt,ifnull(netAmt,0)-ifnull(netAmtRet,0) netAmt from (select transaction_date,ifnull(sum(mrpAmt),0) mrpAmt,ifnull(sum(netAmt),0) netAmt from (select transaction_date,sum(mrp_amt) mrpAmt,sum(net_amt) netAmt from st_se_bo_agent_transaction sale,(select transaction_id,SUBSTRING_INDEX(transaction_date,' ',1) transaction_date from st_lms_bo_transaction_master where transaction_type='SALE' and  transaction_date>='"
								+ startDate
								+ "'  and  transaction_date<='"
								+ endDate
								+ "' ) tranTlb where sale.transaction_id=tranTlb.transaction_id group by transaction_date union all select transaction_date,ifnull(sum(mrp_amt),0) mrpAmt,ifnull(sum(net_amt),0) netAmt from st_se_bo_agent_loose_book_transaction sale,(select transaction_id,SUBSTRING_INDEX(transaction_date,' ',1) transaction_date from st_lms_bo_transaction_master where transaction_type='LOOSE_SALE' and  transaction_date>='"
								+ startDate
								+ "'  and  transaction_date<='"
								+ endDate
								+ "' ) tranTlb where sale.transaction_id=tranTlb.transaction_id group by transaction_date) sale group by transaction_date) sale right join (select transaction_date,ifnull(sum(mrpAmtRet),0) mrpAmtRet,ifnull(sum(netAmtRet),0) netAmtRet from (select transaction_date,sum(mrp_amt) mrpAmtRet,sum(net_amt) netAmtRet from st_se_bo_agent_transaction sale,(select transaction_id,SUBSTRING_INDEX(transaction_date,' ',1) transaction_date from st_lms_bo_transaction_master where transaction_type='SALE_RET' and  transaction_date>='"
								+ startDate
								+ "'  and  transaction_date<='"
								+ endDate
								+ "' ) tranTlb where sale.transaction_id=tranTlb.transaction_id group by transaction_date union all select transaction_date,ifnull(sum(mrp_amt),0) mrpAmtRet,ifnull(sum(net_amt),0) netAmtRet from  st_se_bo_agent_loose_book_transaction sale,(select transaction_id,SUBSTRING_INDEX(transaction_date,' ',1) transaction_date from st_lms_bo_transaction_master where transaction_type='LOOSE_SALE_RET' and   transaction_date>='"
								+ startDate
								+ "'  and  transaction_date<='"
								+ endDate
								+ "' ) tranTlb where sale.transaction_id=tranTlb.transaction_id group by transaction_date) saleRet group by transaction_date) saleRet on   sale.transaction_date=saleRet.transaction_date) SALE";
					}
				} else if ("TICKET_WISE"
						.equals(LMSFilterDispatcher.seSaleReportType)) {
					saleQry = "select date,sum(soldTkt*ticket_price) mrpAmt,sum((soldTkt*ticket_price)-(soldTkt*ticket_price*transacrion_sale_comm_rate*0.01)) sale from st_se_game_master gm,st_se_game_inv_detail gid,(select SUBSTRING_INDEX(date,' ',1) date,game_id,book_nbr,sum(sold_tickets) soldTkt from st_se_game_ticket_inv_history where date>='"
							+ startDate
							+ "' and date<='"
							+ endDate
							+ "' and current_owner='RETAILER' group by book_nbr,SUBSTRING_INDEX(date,' ',1)) TktTlb where gm.game_id=TktTlb.game_id and TktTlb.book_nbr=gid.book_nbr and gid.current_owner=";
					if (viewByAgt) {
						saleQry += "'RETAILER' and gid.current_owner_id in(select organization_id from st_lms_organization_master where parent_id="
								+ orgId + ") group by date";
					} else {
						saleQry += "'AGENT' group by date";
					}
				}

				pstmt = con.prepareStatement(saleQry);

				System.out.println("***Scratch Sale Query*** \n" + pstmt);
				rs = pstmt.executeQuery();

				while (rs.next()) {
					dateMap.get(rs.getString("date")).setScratchSale(
							rs.getDouble("sale"));
				}

				// Calculate Scratch Pwt
				String pwtQry = "select SUBSTRING_INDEX(rtm.transaction_date,' ',1) tx_date,sum("
						+ sePwtCol
						+ ") pwt from st_se_retailer_pwt rp,st_lms_retailer_transaction_master rtm where rp.transaction_id=rtm.transaction_id and rtm.transaction_date>=? and rtm.transaction_date<=? and rtm.transaction_type='PWT' "
						+ addScratchPwtQry + " group by tx_date";
				pstmt = con.prepareStatement(pwtQry);
				pstmt.setTimestamp(1, startDate);
				pstmt.setTimestamp(2, endDate);

				logger.info("***Scratch Pwt Query*** \n" + pstmt);
				rs = pstmt.executeQuery();

				while (rs.next()) {
					dateMap.get(rs.getString("tx_date")).setScratchPwt(
							rs.getDouble("pwt"));
				}

				// Scratch Direct Player Qry

				String dirPwtQry = "select tx_date,sum(net_amt) pwtDir from (select SUBSTRING_INDEX(transaction_date,' ',1) tx_date ,(pwt_amt+(pwt_amt*claim_comm*0.01)) net_amt from st_se_agt_direct_player_pwt where transaction_date>=? and transaction_date<=? "
						+ addDirPlrQry + ") aa group by tx_date";
				pstmt = con.prepareStatement(dirPwtQry);
				pstmt.setTimestamp(1, startDate);
				pstmt.setTimestamp(2, endDate);
				System.out.println("-------Scratch Direct Player Qry------\n"
						+ pstmt);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					dateMap.get(rs.getString("tx_date")).setScratchDirPlyPwt(
							rs.getDouble("pwtDir"));
				}
			}
			if(ReportUtility.isCS){

				saleTranCS = "cs,st_lms_retailer_transaction_master rtm where cs.transaction_id=rtm.transaction_id and rtm.transaction_date>='"
						+ startDate
						+ "' and rtm.transaction_date<='"
						+ endDate
						+ "' and transaction_type = 'CS_SALE' "
						+ addCSQry + " group by tx_date";
				cancelTranCS = "cs,st_lms_retailer_transaction_master rtm where cs.transaction_id=rtm.transaction_id and rtm.transaction_date>='"
						+ startDate
						+ "' and rtm.transaction_date<='"
						+ endDate
						+ "' and transaction_type in('CS_CANCEL_SERVER','CS_CANCEL_RET') "
						+ addCSQry + " group by tx_date";

				// Game Master Query
				String CSQry = "select category_id from st_cs_product_category_master";
				PreparedStatement prodPstmt = con.prepareStatement(CSQry);
				rsGame = prodPstmt.executeQuery();
				StringBuilder saleQry = new StringBuilder(
						"select tx_date,sum(sale) sale from (");
				StringBuilder cancelQry = new StringBuilder(
						"select tx_date,sum(cancel) cancel from (");
				while (rsGame.next()) {
					saleQry
							.append("(select SUBSTRING_INDEX(transaction_date,' ',1) tx_date,sum("
									+ csSaleCol
									+ ") sale from st_cs_sale_"
									+ rsGame.getInt("category_id")
									+ " "
									+ saleTranCS + ") union all");

					cancelQry
							.append("(select SUBSTRING_INDEX(transaction_date,' ',1) tx_date,sum("
									+ csSaleCol
									+ ") cancel from st_cs_refund_"
									+ rsGame.getInt("category_id")
									+ " "
									+ cancelTranCS + ") union all");

				}

				saleQry.delete(saleQry.lastIndexOf("union all"), saleQry
						.length());
				cancelQry.delete(cancelQry.lastIndexOf("union all"), cancelQry
						.length());
				
				saleQry.append(") saleTlb group by tx_date");
				cancelQry.append(") cancelTlb group by tx_date");

				logger.debug("-------CS Sale Query------\n" + saleQry);
				logger.debug("-------CS Cancel Query------\n"
						+ cancelQry);

				// CS Sale Query
				pstmt = con.prepareStatement(saleQry.toString());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					dateMap.get(rs.getString("tx_date")).setCSSale(
							rs.getDouble("sale"));
				}
				// CS Cancel Query
				pstmt = con.prepareStatement(cancelQry.toString());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					dateMap.get(rs.getString("tx_date")).setCSCancel(
							rs.getDouble("cancel"));
				}
			}
			

			if (ReportUtility.isOLA) {
				depositTranOLA = " drs,st_lms_retailer_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='"
						+ startDate
						+ "' and rtm.transaction_date<='"
						+ endDate
						+ "' and transaction_type in('OLA_DEPOSIT') "
						+ addOlaQry + " group by tx_date";
				depositCancelTranOLA = " drs,st_lms_retailer_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='"
						+ startDate
						+ "' and rtm.transaction_date<='"
						+ endDate
						+ "' and transaction_type in('OLA_DEPOSIT_REFUND') "
						+ addOlaQry + " group by tx_date";
				withdrawalTranOLA = " drs,st_lms_retailer_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='"
						+ startDate
						+ "' and rtm.transaction_date<='"
						+ endDate
						+ "' and transaction_type in('OLA_WITHDRAWL') "
						+ addOlaQry + " group by tx_date";

			
				StringBuilder depositQry = new StringBuilder(
						"select tx_date,sum(deposit_amount) deposit_amount from (");
				StringBuilder depositCancelQry = new StringBuilder(
						"select tx_date,sum(deposit_refund_amt) deposit_refund_amt from (");
				StringBuilder withdrawalQry = new StringBuilder(
						"select tx_date,sum(withdrawal_amt) withdrawal_amt from (");
				
				depositQry
							.append("(select SUBSTRING_INDEX(transaction_date,' ',1) tx_date,sum("
									+ olaDepositCol
									+ ") deposit_amount from st_ola_ret_deposit"
									+ depositTranOLA + ")");

				depositCancelQry
							.append("(select SUBSTRING_INDEX(transaction_date,' ',1) tx_date,sum("
									+ olaDepositCol
									+ ") deposit_refund_amt from st_ola_ret_deposit_refund"
									+ depositCancelTranOLA + ")");

				withdrawalQry
							.append("(select SUBSTRING_INDEX(transaction_date,' ',1) tx_date,sum("
									+ olaWithdrawalCol
									+ ") withdrawal_amt from st_ola_ret_withdrawl"
									+ withdrawalTranOLA + ")");

				String netGamingQuery = "select tx_date,sum(netGaming_amt) total_amt from ((select SUBSTRING_INDEX(transaction_date,' ',1) tx_date,sum(commission_calculated) netGaming_amt from st_ola_agt_ret_commission drs,st_lms_agent_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='"+startDate+"' and rtm.transaction_date<='"+endDate+"' and transaction_type in('OLA_COMMISSION')  group by tx_date)) netGamingTlb group by tx_date";
				
				depositQry.append(") depositTlb group by tx_date");
				depositCancelQry.append(") cancelTlb group by tx_date");
				withdrawalQry.append(") withdrawalTlb group by tx_date");
				System.out.println("-------depositQry------\n" + depositQry);
				System.out.println("-------depositCancelQry------\n"
						+ depositCancelQry);
				System.out.println("-------withdrawalQry------\n" + withdrawalQry);
				System.out.println("-------netGaming Qry------\n" + netGamingQuery);

				// OLA deposit Query
				pstmt = con.prepareStatement(depositQry.toString());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					dateMap.get(rs.getString("tx_date")).setOlaDepositAmt(
							rs.getDouble("deposit_amount"));
				}
				// OLA Deposit Cancel Query
				pstmt = con.prepareStatement(depositCancelQry.toString());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					dateMap.get(rs.getString("tx_date")).setOlaDepositCancelAmt(
							rs.getDouble("deposit_refund_amt"));
				}
				// OLA Withdrawal Query
				pstmt = con.prepareStatement(withdrawalQry.toString());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					dateMap.get(rs.getString("tx_date")).setOlaWithdrawalAmt(
							rs.getDouble("withdrawal_amt"));
				}
				// OLA Net Gaming Query
				pstmt = con.prepareStatement(netGamingQuery.toString());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					dateMap.get(rs.getString("tx_date")).setOlaNetGaming(
							rs.getDouble("total_amt"));
				}
				}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateMap;
	}

	public Map<String, Map<String, Map<String, CompleteCollectionBean>>> collectionDayWiseExpand(
			Timestamp startDate, Timestamp endDate, Connection con, String viewBy, int orgId) {
		PreparedStatement pstmt = null;
		ResultSet rsGame = null;
		ResultSet rs = null;
		Map<String, Map<String, Map<String, CompleteCollectionBean>>> serGameAgtMap = new LinkedHashMap<String, Map<String, Map<String, CompleteCollectionBean>>>();
		Map<String, Map<String, CompleteCollectionBean>> gameAgtMap = null;
		// for Draw Game
		String saleTranDG = null;
		String cancelTranDG = null;
		String pwtTranDG = null;
		String dirPlrPwt = null;
		StringBuilder drawQry = null;
		//for CS
		String saleTranCS = null;
		String cancelTranCS = null;
		StringBuilder CSQry = null;
		// for scratch game
		StringBuilder scratchQry = null;
		
		//for OLA
		String depositTranOLA = null;
		String depositCancelTranOLA = null;
		String withdrawalTranOLA = null;
		String netGamingTranOLA = null;
		StringBuilder olaQuery = null;

		if (startDate.after(endDate)) {
			return serGameAgtMap;
		}

		String addDrawQry = "";
		String addOlaQry = "";
		String addCSQry = "";
		String addDirPlrQry = "";
		String addScratchPwtQry = "";

		// For Column which is used

		String dgSaleCol = "agent_net_amt";
		String csSaleCol = "agent_net_amt";
		String olaDepositCol = "agent_net_amt";
		String olaWithdrawalCol = "agent_net_amt";
		String dgPwtCol = "pwt_amt+agt_claim_comm";
		String sePwtCol = "pwt_amt+(pwt_amt*agt_claim_comm*0.01)";
		boolean viewByAgt = false;
		if ("AGENT".equals(viewBy) && orgId != 0) {
			viewByAgt = true;
			dgSaleCol = "net_amt";
			csSaleCol = "net_amt";
			olaDepositCol = "net_amt";
			olaWithdrawalCol = "net_amt";
			dgPwtCol = "pwt_amt+retailer_claim_comm";
			sePwtCol = "pwt_amt+(pwt_amt*claim_comm*0.01)";
			addDrawQry = " and drs.retailer_org_id in (select organization_id from st_lms_organization_master where parent_id="
					+ orgId + ")";
			addOlaQry = " and drs.retailer_org_id in (select organization_id from st_lms_organization_master where parent_id="
				+ orgId + ")";
			addDirPlrQry = " and agent_org_id=" + orgId;
			addScratchPwtQry = " and rp.retailer_org_id in (select organization_id from st_lms_organization_master where parent_id="
					+ orgId + ")";
		}
		try {
			PreparedStatement datePstmt = con
					.prepareStatement("CREATE TABLE if not exists `tempdate` ( `alldate` timestamp)");
			datePstmt.execute();
			datePstmt = con.prepareStatement("truncate table tempdate");
			datePstmt.execute();

			Calendar startCal = Calendar.getInstance();
			startCal.setTime(startDate);

			Calendar endCal = Calendar.getInstance();
			endCal.setTime(endDate);
			StringBuilder date = new StringBuilder("");
			while (startCal.getTime().before(endCal.getTime())) {
				date.append("('"
						+ new java.sql.Date(startCal.getTimeInMillis())
								.toString() + "'),");
				startCal.setTimeInMillis(startCal.getTimeInMillis() + 24 * 60
						* 60 * 1000);
			}
			date.deleteCharAt(date.length() - 1);
			datePstmt = con.prepareStatement("insert tempdate values" + date);
			logger.info(datePstmt);
			datePstmt.execute();
			if (ReportUtility.isDG) {
				gameAgtMap = new LinkedHashMap<String, Map<String, CompleteCollectionBean>>();
				drawQry = new StringBuilder(
						"select sale.alldate,ifnull(sale,0.0) sale,ifnull(cancel,0.0) cancel,ifnull(pwt,0.0)  pwt from ");
				saleTranDG = "(select alldate,sum(sale) as sale from (select SUBSTRING_INDEX(rtm.transaction_date,' ',1) trx_date,sum("
						+ dgSaleCol
						+ ") as sale from st_dg_ret_sale_? drs,st_lms_retailer_transaction_master rtm where transaction_type in('DG_SALE','DG_SALE_OFFLINE') and drs.transaction_id=rtm.transaction_id and transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "' "
						+ addDrawQry
						+ " group by trx_date) aa right outer join (select SUBSTRING_INDEX(alldate,' ',1) alldate from tempdate ) bb on trx_date=alldate group by alldate) sale,";
				cancelTranDG = "(select alldate,sum(cancel) as cancel from (select SUBSTRING_INDEX(rtm.transaction_date,' ',1) trx_date,sum("
						+ dgSaleCol
						+ ") as cancel from st_dg_ret_sale_refund_? drs,st_lms_retailer_transaction_master rtm where transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') and drs.transaction_id=rtm.transaction_id and transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "' "
						+ addDrawQry
						+ " group by trx_date) aa right outer join (select SUBSTRING_INDEX(alldate,' ',1) alldate from tempdate ) bb on trx_date=alldate group by alldate) cancel,";
				pwtTranDG = "(select alldate,sum(pwt) as pwt from (select SUBSTRING_INDEX(rtm.transaction_date,' ',1) trx_date,sum("
						+ dgPwtCol
						+ ") as pwt from st_dg_ret_pwt_? drs,st_lms_retailer_transaction_master rtm  where transaction_type in('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') and drs.transaction_id=rtm.transaction_id and transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "' "
						+ addDrawQry
						+ " group by trx_date) aa right outer join (select SUBSTRING_INDEX(alldate,' ',1) alldate from tempdate ) bb on trx_date=alldate group by alldate) pwt";
				drawQry.append(saleTranDG);
				drawQry.append(cancelTranDG);
				drawQry.append(pwtTranDG);
				drawQry
						.append(" where sale.alldate=cancel.alldate and sale.alldate=pwt.alldate and cancel.alldate=pwt.alldate");
				System.out.println("For Expand draw game Qry:: " + drawQry);

				dirPlrPwt = "select alldate,ifnull(pwtDir,0.0) pwtDir from (select SUBSTRING_INDEX(transaction_date,' ',1) trx_date,sum(pwt_amt+agt_claim_comm) pwtDir from st_dg_agt_direct_plr_pwt where transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "' and game_id=? "
						+ addDirPlrQry
						+ " group by trx_date) aa right outer join (select SUBSTRING_INDEX(alldate,' ',1) alldate from tempdate) bb on alldate=trx_date";

				System.out.println("For Expand draw game Qry Direct Ply:: "
						+ dirPlrPwt);

				// Game Master Query
				String gameQry =ReportUtility.getDrawGameMapQuery(startDate);
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				rsGame = gamePstmt.executeQuery();
				while (rsGame.next()) {
					int gameId = rsGame.getInt("game_id");
					Map<String, CompleteCollectionBean> gameMap = new LinkedHashMap<String, CompleteCollectionBean>();
					gameMap.putAll(getDayMap(startDate, endDate));
					pstmt = con.prepareStatement(drawQry.toString());
					pstmt.setInt(1, gameId);
					pstmt.setInt(2, gameId);
					pstmt.setInt(3, gameId);
					
					rs = pstmt.executeQuery();
					while (rs.next()) {
						String agtId = rs.getString("alldate");
						gameMap.get(agtId).setDrawSale(rs.getDouble("sale"));
						gameMap.get(agtId)
								.setDrawCancel(rs.getDouble("cancel"));
						gameMap.get(agtId).setDrawPwt(rs.getDouble("pwt"));

					}

					// Direct Player Pwt
					pstmt = con.prepareStatement(dirPlrPwt);
					pstmt.setInt(1, gameId);
					rs = pstmt.executeQuery();
					while (rs.next()) {
						String agtId = rs.getString("alldate");
						gameMap.get(agtId).setDrawDirPlyPwt(
								rs.getDouble("pwtDir"));
					}
					gameAgtMap.put(rsGame.getString("game_name"), gameMap);
				}

				serGameAgtMap.put("DG", gameAgtMap);
			}
			if (ReportUtility.isSE) {
				gameAgtMap = new LinkedHashMap<String, Map<String, CompleteCollectionBean>>();
				scratchQry = new StringBuilder(
						"select sale.alldate,ifnull(sale,0.0) sale,ifnull(pwt,0.0) pwt from ");
				logger.info("----Type Select ---"
						+ LMSFilterDispatcher.seSaleReportType);
				if ("BOOK_WISE".equals(LMSFilterDispatcher.seSaleReportType)) {
					if (viewByAgt) {
						saleTranDG = "(select bb.alldate,sum(sale) as sale from (select sale.transaction_date trx_date,(ifnull(mrpAmt,0.0)-ifnull(mrpAmtRet,0.0)) mrpAmt,(ifnull(netAmt,0.0)-ifnull(netAmtRet,0.0)) sale from (select transaction_date,sum(mrp_amt) mrpAmt,sum(net_amt) netAmt from st_se_agent_retailer_transaction sale,(select transaction_id,SUBSTRING_INDEX(transaction_date,' ',1) transaction_date from st_lms_agent_transaction_master where transaction_type='SALE' and transaction_date>='"
								+ startDate
								+ "' and transaction_date<='"
								+ endDate
								+ "') tranTlb where sale.transaction_id=tranTlb.transaction_id and game_id=? and agent_org_id="
								+ orgId
								+ " group by transaction_date) sale left outer join (select transaction_date,sum(mrp_amt) mrpAmtRet,sum(net_amt) netAmtRet from st_se_agent_retailer_transaction sale,(select transaction_id,SUBSTRING_INDEX(transaction_date,' ',1) transaction_date from st_lms_agent_transaction_master where transaction_type='SALE_RET' and transaction_date>='"
								+ startDate
								+ "' and transaction_date<='"
								+ endDate
								+ "') tranTlb where sale.transaction_id=tranTlb.transaction_id and game_id=? and agent_org_id="
								+ orgId
								+ " group by transaction_date) saleRet on sale.transaction_date=saleRet.transaction_date) aa right outer join (select SUBSTRING_INDEX(alldate,' ',1) alldate from tempdate ) bb on trx_date=alldate group by alldate) sale,";
					} else {
						saleTranDG = "(select bb.alldate,sum(sale) as sale from (select sale.transaction_date trx_date,(ifnull(mrpAmt,0.0)-ifnull(mrpAmtRet,0.0)) mrpAmt,(ifnull(netAmt,0.0)-ifnull(netAmtRet,0.0)) sale from (select transaction_date,sum(mrp_amt) mrpAmt,sum(net_amt) netAmt from st_se_bo_agent_transaction sale,(select transaction_id,SUBSTRING_INDEX(transaction_date,' ',1) transaction_date from st_lms_bo_transaction_master where transaction_type='SALE' and transaction_date>='"
								+ startDate
								+ "' and transaction_date<='"
								+ endDate
								+ "') tranTlb where sale.transaction_id=tranTlb.transaction_id and game_id=? group by transaction_date) sale left outer join (select transaction_date,sum(mrp_amt) mrpAmtRet,sum(net_amt) netAmtRet from st_se_bo_agent_transaction sale,(select transaction_id,SUBSTRING_INDEX(transaction_date,' ',1) transaction_date from st_lms_bo_transaction_master where transaction_type='SALE_RET' and transaction_date>='"
								+ startDate
								+ "' and transaction_date<='"
								+ endDate
								+ "') tranTlb where sale.transaction_id=tranTlb.transaction_id and game_id=? group by transaction_date) saleRet on sale.transaction_date=saleRet.transaction_date) aa right outer join (select SUBSTRING_INDEX(alldate,' ',1) alldate from tempdate ) bb on trx_date=alldate group by alldate) sale,";
					}
				} else if ("TICKET_WISE"
						.equals(LMSFilterDispatcher.seSaleReportType)) {
					String addQry = null;
					if (viewByAgt) {
						addQry = "'RETAILER' and gid.current_owner_id in(select organization_id from st_lms_organization_master where parent_id="
								+ orgId + ")";
					} else {
						addQry = "'AGENT'";
					}

					saleTranDG = "(select bb.alldate,sum(sale) as sale from (select date,sum(soldTkt*ticket_price) mrpAmt,sum((soldTkt*ticket_price)-(soldTkt*ticket_price*transacrion_sale_comm_rate*0.01)) sale from st_se_game_master gm,st_se_game_inv_detail gid,(select SUBSTRING_INDEX(date,' ',1) date,game_id,book_nbr,sum(sold_tickets) soldTkt from st_se_game_ticket_inv_history where date>='"
							+ startDate
							+ "' and date<='"
							+ endDate
							+ "' and current_owner='RETAILER' and game_id=? group by book_nbr,SUBSTRING_INDEX(date,' ',1)) TktTlb where gm.game_id=TktTlb.game_id and gm.game_id=? and TktTlb.book_nbr=gid.book_nbr and gid.current_owner="
							+ addQry
							+ " group by date) aa right outer join (select SUBSTRING_INDEX(alldate,' ',1) alldate from tempdate ) bb on date=alldate group by alldate) sale, ";

				}

				pwtTranDG = "(select bb.alldate,sum(pwt) as pwt from (select trx_date,sum("
						+ sePwtCol
						+ ") as pwt from (select SUBSTRING_INDEX(transaction_date,' ',1) trx_date,pwt_amt,claim_comm,agt_claim_comm,rp.game_id from st_se_retailer_pwt rp,st_lms_retailer_transaction_master rtm where rp.transaction_id=rtm.transaction_id and transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "' and transaction_type='PWT' "
						+ addScratchPwtQry
						+ ") aa where game_id=? group by trx_date) aa right outer join (select SUBSTRING_INDEX(alldate,' ',1) alldate from tempdate ) bb on trx_date=alldate group by alldate) pwt";
				scratchQry.append(saleTranDG);
				scratchQry.append(pwtTranDG);
				scratchQry.append(" where sale.alldate=pwt.alldate");
				System.out.println("For Expand scratch game Qry:: "
						+ scratchQry);

				dirPlrPwt = "select alldate,ifnull(pwtDir,0.0) pwtDir from (select SUBSTRING_INDEX(transaction_date,' ',1) trx_date,sum(pwt_amt+(pwt_amt*claim_comm*0.01)) pwtDir from st_se_agt_direct_player_pwt where transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "' and game_id=? "
						+ addDirPlrQry
						+ " group by trx_date) aa right outer join (select SUBSTRING_INDEX(alldate,' ',1) alldate from tempdate) bb on alldate=trx_date";

				System.out.println("For Expand draw game Qry Direct Ply:: "
						+ dirPlrPwt);
				// Game Master Query
				String gameQry = "select game_id,game_name from st_se_game_master";
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				rsGame = gamePstmt.executeQuery();
				while (rsGame.next()) {
					int gameId = rsGame.getInt("game_id");
					Map<String, CompleteCollectionBean> gameMap = new LinkedHashMap<String, CompleteCollectionBean>();
					gameMap.putAll(getDayMap(startDate, endDate));
					pstmt = con.prepareStatement(scratchQry.toString());
					pstmt.setInt(1, gameId);
					pstmt.setInt(2, gameId);
					pstmt.setInt(3, gameId);
					System.out.println("-------Indivisual Game Qry-------\n"
							+ pstmt);
					rs = pstmt.executeQuery();
					while (rs.next()) {
						String agtId = rs.getString("alldate");
						gameMap.get(agtId).setDrawSale(rs.getDouble("sale"));
						gameMap.get(agtId).setDrawPwt(rs.getDouble("pwt"));
					}

					// Direct Player Pwt
					pstmt = con.prepareStatement(dirPlrPwt);
					pstmt.setInt(1, gameId);
					rs = pstmt.executeQuery();
					while (rs.next()) {
						String agtId = rs.getString("alldate");
						gameMap.get(agtId).setScratchDirPlyPwt(
								rs.getDouble("pwtDir"));
					}
					gameAgtMap.put(rsGame.getString("game_name"), gameMap);
				}

				serGameAgtMap.put("SE", gameAgtMap);
			}
			if(ReportUtility.isCS){

				gameAgtMap = new LinkedHashMap<String, Map<String, CompleteCollectionBean>>();
				CSQry = new StringBuilder(
						"select sale.alldate,ifnull(sale,0.0) sale,ifnull(cancel,0.0) cancel from ");
				saleTranCS = "(select alldate,sum(sale) as sale from (select SUBSTRING_INDEX(rtm.transaction_date,' ',1) trx_date,sum("
						+ csSaleCol
						+ ") as sale from st_cs_sale_? cs,st_lms_retailer_transaction_master rtm where transaction_type = 'CS_SALE' and cs.transaction_id=rtm.transaction_id and transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "' "
						+ addCSQry
						+ " group by trx_date) aa right outer join (select SUBSTRING_INDEX(alldate,' ',1) alldate from tempdate ) bb on trx_date=alldate group by alldate) sale,";
				cancelTranCS = "(select alldate,sum(cancel) as cancel from (select SUBSTRING_INDEX(rtm.transaction_date,' ',1) trx_date,sum("
						+ csSaleCol
						+ ") as cancel from st_cs_refund_? cs,st_lms_retailer_transaction_master rtm where transaction_type in('CS_CANCEL_SERVER','CS_CANCEL_RET') and cs.transaction_id=rtm.transaction_id and transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "' "
						+ addCSQry
						+ " group by trx_date) aa right outer join (select SUBSTRING_INDEX(alldate,' ',1) alldate from tempdate ) bb on trx_date=alldate group by alldate) cancel ";
				CSQry.append(saleTranCS);
				CSQry.append(cancelTranCS);
				CSQry
						.append(" where sale.alldate=cancel.alldate");
				logger.debug("For Expand CS Qry:: " + CSQry);

				// Game Master Query
				String catQry = "select category_id,category_code from st_cs_product_category_master where status = 'ACTIVE'";
				PreparedStatement gamePstmt = con.prepareStatement(catQry);
				rsGame = gamePstmt.executeQuery();
				while (rsGame.next()) {
					int catId = rsGame.getInt("category_id");
					Map<String, CompleteCollectionBean> prodMap = new LinkedHashMap<String, CompleteCollectionBean>();
					prodMap.putAll(getDayMap(startDate, endDate));
					pstmt = con.prepareStatement(CSQry.toString());
					pstmt.setInt(1, catId);
					pstmt.setInt(2, catId);
					logger.debug("-------Indivisual category Qry-------\n"
							+ pstmt);
					rs = pstmt.executeQuery();
					while (rs.next()) {
						String agtId = rs.getString("alldate");
						prodMap.get(agtId).setDrawSale(rs.getDouble("sale"));
						prodMap.get(agtId).setDrawCancel(rs.getDouble("cancel"));
					}
					gameAgtMap.put(rsGame.getString("category_code"), prodMap);
				}

				serGameAgtMap.put("CS", gameAgtMap);
			
			}
			
			if (ReportUtility.isOLA) {

				gameAgtMap = new LinkedHashMap<String, Map<String, CompleteCollectionBean>>();
				olaQuery = new StringBuilder(
						"select deposit.alldate, ifnull(deposit_amount,0.0) deposit_amt, ifnull(deposit_refund_amt,0.0) deposit_refund_amt, ifnull(withdrawal_amt,0.0) withdrawal_amt, ifnull(total_amt,0.0) netGaming_amt from ");
				depositTranOLA = "(select alldate,sum(deposit_amount) deposit_amount from (select SUBSTRING_INDEX(transaction_date,' ',1) tx_date,sum("
						+ olaDepositCol
						+ ") deposit_amount from st_ola_ret_deposit drs,st_lms_retailer_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='"
						+ startDate
						+ "' and rtm.transaction_date<='"
						+ endDate
						+ "' "
						+ addOlaQry
						+ " and transaction_type in('OLA_DEPOSIT') and wallet_id=?  group by tx_date) depositTlb right outer join (select SUBSTRING_INDEX(alldate,' ',1) alldate from tempdate ) temp on tx_date=alldate group by alldate) deposit,";
				depositCancelTranOLA = "(select alldate,sum(deposit_refund_amt) deposit_refund_amt from (select SUBSTRING_INDEX(transaction_date,' ',1) tx_date,sum("
						+ olaDepositCol
						+ ") deposit_refund_amt from st_ola_ret_deposit_refund drs,st_lms_retailer_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='"
						+ startDate
						+ "' and rtm.transaction_date<='"
						+ endDate
						+ "' "
						+ addOlaQry
						+ " and transaction_type in('OLA_DEPOSIT_REFUND') and wallet_id=? group by tx_date) cancelTlb right outer join (select SUBSTRING_INDEX(alldate,' ',1) alldate from tempdate ) temp on tx_date=alldate group by alldate) cancel,";
				withdrawalTranOLA = "(select alldate,sum(withdrawal_amt) withdrawal_amt from (select SUBSTRING_INDEX(transaction_date,' ',1) tx_date,sum("
						+ olaWithdrawalCol
						+ ") withdrawal_amt from st_ola_ret_withdrawl drs,st_lms_retailer_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='"
						+ startDate
						+ "' and rtm.transaction_date<='"
						+ endDate
						+ "' "
						+ addOlaQry
						+ " and transaction_type in('OLA_WITHDRAWL') and wallet_id=? group by tx_date) withdrawalTlb right outer join (select SUBSTRING_INDEX(alldate,' ',1) alldate from tempdate ) temp on tx_date=alldate group by alldate) withdraw,";
				netGamingTranOLA = "(select alldate,sum(netGaming_amt) total_amt from (select SUBSTRING_INDEX(transaction_date,' ',1) tx_date,sum(commission_calculated) netGaming_amt from st_ola_agt_ret_commission drs,st_lms_agent_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='"
					+ startDate
					+ "' and rtm.transaction_date<='"
					+ endDate
					+ "' "
					+ addOlaQry
					+ " and transaction_type in('OLA_COMMISSION') and wallet_id=? group by tx_date) netGamingTlb right outer join (select SUBSTRING_INDEX(alldate,' ',1) alldate from tempdate ) temp on tx_date=alldate group by alldate) netgaming";
				olaQuery.append(depositTranOLA);
				olaQuery.append(depositCancelTranOLA);
				olaQuery.append(withdrawalTranOLA);
				olaQuery.append(netGamingTranOLA);
				olaQuery
						.append(" where deposit.alldate = cancel.alldate and cancel.alldate = withdraw.alldate and withdraw.alldate = netgaming.alldate");
				System.out.println("For Expand OLA Qry:: " + olaQuery);

				// Wallet Master Query
				String catQry = "select wallet_id,wallet_display_name from st_ola_wallet_master";
				PreparedStatement prodPstmt = con.prepareStatement(catQry);
				rsGame = prodPstmt.executeQuery();
				while (rsGame.next()) {
					int walletIdId = rsGame.getInt("wallet_id");
					Map<String, CompleteCollectionBean> walletMap = new LinkedHashMap<String, CompleteCollectionBean>();
					walletMap.putAll(getDayMap(startDate, endDate));
					pstmt = con.prepareStatement(olaQuery.toString());
					pstmt.setInt(1, walletIdId);
					pstmt.setInt(2, walletIdId);
					pstmt.setInt(3, walletIdId);
					pstmt.setInt(4, walletIdId);
					logger.debug("-------Indivisual Wallet Qry-------\n"
							+ pstmt);
					rs = pstmt.executeQuery();
					while (rs.next()) {
						String agtId = rs.getString("alldate");
						walletMap.get(agtId).setOlaDepositAmt(rs.getDouble("deposit_amt"));
						walletMap.get(agtId)
								.setOlaDepositCancelAmt(rs.getDouble("deposit_refund_amt"));
						walletMap.get(agtId).setOlaWithdrawalAmt(rs.getDouble("withdrawal_amt"));
						walletMap.get(agtId).setOlaNetGaming(rs.getDouble("netGaming_amt"));
					}

					gameAgtMap.put(rsGame.getString("wallet_display_name"), walletMap);
				}
					serGameAgtMap.put("OLA", gameAgtMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return serGameAgtMap;
	}

	public Map<String, CompleteCollectionBean> collectionReport(
			Timestamp startDate, Timestamp endDate, String reportType) {
		
		Connection con = null;
		Map<String, CompleteCollectionBean> agtMap = null;
		
		try {
			con = DBConnect.getConnection();
			
			if ("Agent Wise".equals(reportType)) {
				agtMap = collectionAgentWise(startDate, endDate, con);
			} else if ("Day Wise".equals(reportType)) {
				agtMap = collectionDayWise(startDate, endDate, con,
						"BO", 0);
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
		return agtMap;
	}

	public Map<String, CompleteCollectionBean> collectionReport(
			Timestamp startDate, Timestamp endDate, String reportType, ReportStatusBean reportStatusBean) {
		
		Connection con = null;
		Map<String, CompleteCollectionBean> agtMap = null;
		
		try {
			if("NO".equals(Utility.getPropertyValue("IS_DATA_FROM_REPLICA")) || "MAIN_DB".equals(reportStatusBean.getReportingFrom()))
				con = DBConnect.getConnection();
			else
				con = DBConnectReplica.getConnection();

			if ("Agent Wise".equals(reportType)) {
				agtMap = collectionAgentWise(startDate, endDate, con);
			} else if ("Day Wise".equals(reportType)) {
				agtMap = collectionDayWise(startDate, endDate, con,
						"BO", 0);
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
		return agtMap;
	}

	public Map<String, Map<String, Map<String, CompleteCollectionBean>>> collectionReportExpand(
			Timestamp startDate, Timestamp endDate, String reportType, ReportStatusBean reportStatusBean) {
		
		Connection con = null;
		Map<String, Map<String, Map<String, CompleteCollectionBean>>> agtMap = null;
		
		try {
			if("NO".equals(Utility.getPropertyValue("IS_DATA_FROM_REPLICA")) || "MAIN_DB".equals(reportStatusBean.getReportingFrom()))
				con = DBConnect.getConnection();
			else
				con = DBConnectReplica.getConnection();
			
			if ("Agent Wise Expand".equals(reportType)) {
				agtMap = collectionAgentWiseExpand(startDate, endDate, con);
			} else if ("Day Wise Expand".equals(reportType)) {
				agtMap = collectionDayWiseExpand(startDate, endDate, con, "BO", 0);
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
		return agtMap;
	}

	public Map<String, CompleteCollectionBean> collectionReportForAgent(
			Timestamp startDate, Timestamp endDate, int agtOrgId,
			String reportType) {
		
		Connection con = null;
		Map<String, CompleteCollectionBean> agtMap = null;
		
		try {
			con = DBConnect.getConnection();
			
			if ("Retailer Wise".equals(reportType)) {
				agtMap = collectionRetailerWise(startDate, endDate, con,agtOrgId);
			} else if ("Agent Wise".equals(reportType)) {
				agtMap = collectionAgentWise(startDate, endDate, con);
			} else if ("Day Wise".equals(reportType)) {
				agtMap = collectionDayWise(startDate, endDate, con,
						"AGENT", agtOrgId);
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
		return agtMap;
	}

	public Map<String, Map<String, Map<String, CompleteCollectionBean>>> collectionReportForAgentExpand(
			Timestamp startDate, Timestamp endDate, String reportType,
			int agtOrgId) {
		System.out
				.println("----------collectionReport----------reportType-----"
						+ reportType);
		Connection con = null;
		Map<String, Map<String, Map<String, CompleteCollectionBean>>> agtMap = null;
		
		try {
			con = DBConnect.getConnection();
			
			if ("Retailer Wise Expand".equals(reportType)) {
				agtMap = collectionRetailerWiseExpand(startDate, endDate, con, agtOrgId);
			} else if ("Day Wise Expand".equals(reportType)) {
				agtMap = collectionDayWiseExpand(startDate, endDate, con, "AGENT", agtOrgId);
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
		return agtMap;
	}

	public Map<String, CompleteCollectionBean> collectionRetailerWise(
			Timestamp startDate, Timestamp endDate, Connection con, int agtOrgId) {
		PreparedStatement pstmt = null;
		ResultSet rsGame = null;
		ResultSet rs = null;
		ResultSet rsRetOrg = null;
		Map<String, CompleteCollectionBean> agtMap = new LinkedHashMap<String, CompleteCollectionBean>();
		CompleteCollectionBean collBean = null;
		// for Draw Game
		String saleTranDG = null;
		String cancelTranDG = null;
		String pwtTranDG = null;
		//for CS
		String saleTranCS = null;
		String cancelTranCS = null;

		
		//for OLA
		String depositTranOLA = null;
		String cancelTranOLA = null;
		String withdrawalTranOLA = null;

		if (startDate.after(endDate)) {
			return agtMap;
		}

		// for scratch game
		try {
			
		
			String agtOrgQry =" ";
				
			if (agtOrgId == 0) {
					agtOrgQry = QueryManager.getOrgQry("RETAILER");
			} else {

				String appendOrder = QueryManager.getAppendOrgOrder();
				agtOrgQry = "select name orgCode,organization_id from st_lms_organization_master where  parent_id="
						+ agtOrgId + "  order by " + appendOrder;
				if ((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE")) {

					agtOrgQry = "select org_code orgCode,organization_id from st_lms_organization_master where  parent_id="
							+ agtOrgId + "  order by " + appendOrder;

				} else if ((LMSFilterDispatcher.orgFieldType)
						.equalsIgnoreCase("CODE_NAME")) {

					agtOrgQry = "select concat(org_code,'_',name) orgCode,organization_id from st_lms_organization_master where  parent_id="
							+ agtOrgId + "  order by " + appendOrder;

				} else if ((LMSFilterDispatcher.orgFieldType)
						.equalsIgnoreCase("NAME_CODE")) {
					agtOrgQry = "select concat(name,'_',org_code) orgCode,organization_id from st_lms_organization_master where  parent_id="
							+ agtOrgId + "  order by " + appendOrder;

				}

			}
				
			
		
			pstmt = con.prepareStatement(agtOrgQry);
			rsRetOrg = pstmt.executeQuery();
			while (rsRetOrg.next()) {
				collBean = new CompleteCollectionBean();
				collBean.setOrgName(rsRetOrg.getString("orgCode"));
				if (ReportUtility.isDG) {
					collBean.setDrawSale(0.0);
					collBean.setDrawPwt(0.0);
					collBean.setDrawCancel(0.0);
					collBean.setDrawDirPlyPwt(0.0);
				}
				if (ReportUtility.isSE) {
					collBean.setScratchSale(0.0);
					collBean.setScratchPwt(0.0);
					collBean.setScratchDirPlyPwt(0.0);
				}
				if (ReportUtility.isCS) {
					collBean.setCSSale(0.0);
					collBean.setCSCancel(0.0);
				}
				if(ReportUtility.isOLA){
					collBean.setOlaDepositAmt(0.0);
					collBean.setOlaDepositCancelAmt(0.0);
					collBean.setOlaWithdrawalAmt(0.0);
					collBean.setOlaNetGaming(0.0);
				}
				agtMap.put(rsRetOrg.getString("organization_id"), collBean);
			}
			agtOrgQry = agtOrgQry.replace("name,", "");
			if (ReportUtility.isDG) {
				saleTranDG = "select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_SALE','DG_SALE_OFFLINE') and transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "' and retailer_org_id in (" + agtOrgQry + ")";
				cancelTranDG = "select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') and transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "' and retailer_org_id in (" + agtOrgQry + ")";
				pwtTranDG = "select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') and transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "' and retailer_org_id in (" + agtOrgQry + ")";

				// Game Master Query
				String gameQry = ReportUtility.getDrawGameMapQuery(startDate);
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				rsGame = gamePstmt.executeQuery();
				StringBuilder saleQry = new StringBuilder(
						"select retailer_org_id,sum(sale) as sale from (");
				StringBuilder cancelQry = new StringBuilder(
						"select retailer_org_id,sum(cancel) as cancel from (");
				StringBuilder pwtQry = new StringBuilder(
						"select retailer_org_id,sum(pwt) as pwt from (");
				while (rsGame.next()) {
					saleQry
							.append("(select drs.retailer_org_id,sum(net_amt) as sale from st_dg_ret_sale_"
									+ rsGame.getInt("game_id")
									+ " drs where transaction_id in ("
									+ saleTranDG
									+ ") group by drs.retailer_org_id) union all");

					cancelQry
							.append("(select drs.retailer_org_id,sum(net_amt) as cancel from st_dg_ret_sale_refund_"
									+ rsGame.getInt("game_id")
									+ " drs where transaction_id in ("
									+ cancelTranDG
									+ ") group by drs.retailer_org_id) union all");

					pwtQry
							.append("(select drs.retailer_org_id,sum(pwt_amt+retailer_claim_comm) as pwt from st_dg_ret_pwt_"
									+ rsGame.getInt("game_id")
									+ " drs where transaction_id in ("
									+ pwtTranDG
									+ ") group by drs.retailer_org_id) union all");

				}

				saleQry.delete(saleQry.lastIndexOf("union all"), saleQry
						.length());
				cancelQry.delete(cancelQry.lastIndexOf("union all"), cancelQry
						.length());
				pwtQry.delete(pwtQry.lastIndexOf("union all"), pwtQry.length());

				saleQry.append(") saleTlb group by retailer_org_id");
				cancelQry.append(") cancelTlb group by retailer_org_id");
				pwtQry.append(") pwtTlb group by retailer_org_id");
				System.out.println("-------Draw Sale Qurey------\n" + saleQry);
				System.out.println("-------Draw Cancel Qurey------\n"
						+ cancelQry);
				System.out.println("-------Draw Pwt Qurey------\n" + pwtQry);

				// Draw Sale Query
				pstmt = con.prepareStatement(saleQry.toString());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					agtMap.get(rs.getString("retailer_org_id")).setDrawSale(
							rs.getDouble("sale"));
				}
				// Draw Cancel Query
				pstmt = con.prepareStatement(cancelQry.toString());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					agtMap.get(rs.getString("retailer_org_id")).setDrawCancel(
							rs.getDouble("cancel"));
				}
				// Draw Pwt Query
				pstmt = con.prepareStatement(pwtQry.toString());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					agtMap.get(rs.getString("retailer_org_id")).setDrawPwt(
							rs.getDouble("pwt"));
				}
			}
			if (ReportUtility.isSE) {
				String saleQry = "";
				logger.info("----Type Select ---"
						+ LMSFilterDispatcher.seSaleReportType);
				if ("BOOK_WISE".equals(LMSFilterDispatcher.seSaleReportType)) {
					saleQry = "select organization_id,name,ifnull(mrpAmt,0.0) mrpAmt,ifnull(netAmt,0.0) netAmt from st_lms_organization_master left outer join (select sale.retailer_org_id,(ifnull(mrpAmt,0.0)-ifnull(mrpAmtRet,0.0)) mrpAmt,(ifnull(netAmt,0.0)-ifnull(netAmtRet,0.0)) netAmt from (select retailer_org_id,sum(mrp_amt) mrpAmt,sum(net_amt) netAmt from st_se_agent_retailer_transaction where transaction_id in (select transaction_id from st_lms_agent_transaction_master where transaction_type='SALE' and transaction_date>='"
							+ startDate
							+ "' and transaction_date<='"
							+ endDate
							+ "') and agent_org_id="
							+ agtOrgId
							+ " group by retailer_org_id) sale left outer join (select retailer_org_id,sum(mrp_amt) mrpAmtRet,sum(net_amt) netAmtRet from st_se_agent_retailer_transaction where transaction_id in (select transaction_id from st_lms_agent_transaction_master where transaction_type='SALE_RET' and transaction_date>='"
							+ startDate
							+ "' and transaction_date<='"
							+ endDate
							+ "') and agent_org_id="
							+ agtOrgId
							+ " group by retailer_org_id) saleRet on sale.retailer_org_id=saleRet.retailer_org_id) saleTlb on organization_id=retailer_org_id where parent_id="
							+ agtOrgId;
				} else if ("TICKET_WISE"
						.equals(LMSFilterDispatcher.seSaleReportType)) {
					saleQry = "select organization_id,name,ifnull(mrpAmt,0.0) mrpAmt,ifnull(netAmt,0.0) netAmt from st_lms_organization_master left outer join (select current_owner_id,sum(soldTkt*ticket_price) mrpAmt,sum((soldTkt*ticket_price)-(soldTkt*ticket_price*transacrion_sale_comm_rate*0.01)) netAmt from st_se_game_master gm,st_se_game_inv_detail gid,(select game_id,book_nbr,sum(sold_tickets) soldTkt from st_se_game_ticket_inv_history where date>='"
							+ startDate
							+ "' and date<='"
							+ endDate
							+ "' and current_owner='RETAILER' group by book_nbr) TktTlb where gm.game_id=TktTlb.game_id and TktTlb.book_nbr=gid.book_nbr and gid.current_owner='RETAILER' group by current_owner_id) saleTlb on organization_id=current_owner_id where organization_type='RETAILER' and parent_id="
							+ agtOrgId;
				}
				pstmt = con.prepareStatement(saleQry);

				System.out.println("***Scratch Sale Query*** \n" + pstmt);
				rs = pstmt.executeQuery();

				while (rs.next()) {
					agtMap.get(rs.getString("organization_id")).setScratchSale(
							rs.getDouble("netAmt"));
				}

				// Calculate Scratch Pwt
				String pwtQry = "select retailer_org_id,sum(pwt_amt+(pwt_amt*claim_comm*0.01)) pwt from st_se_retailer_pwt where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_date>=? and transaction_date<=? and transaction_type='PWT' and retailer_org_id in ("
						+ agtOrgQry + ")) group by retailer_org_id";
				pstmt = con.prepareStatement(pwtQry);
				pstmt.setTimestamp(1, startDate);
				pstmt.setTimestamp(2, endDate);
				logger.info("***Scratch Pwt Query*** \n" + pstmt);
				rs = pstmt.executeQuery();

				while (rs.next()) {
					agtMap.get(rs.getString("retailer_org_id")).setScratchPwt(
							rs.getDouble("pwt"));
				}
			}
			if (ReportUtility.isCS) {

				saleTranCS = "select transaction_id from st_lms_retailer_transaction_master where transaction_type = 'CS_SALE' and transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "' and retailer_org_id in (" + agtOrgQry + ")";
				cancelTranCS = "select transaction_id from st_lms_retailer_transaction_master where transaction_type in('CS_CANCEL_SERVER','CS_CANCEL_RET') and transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "' and retailer_org_id in (" + agtOrgQry + ")";
				
				// Category Master Query
				String catQry = "select category_id from st_cs_product_category_master where status = 'ACTIVE'";
				PreparedStatement prodPstmt = con.prepareStatement(catQry);
				rsGame = prodPstmt.executeQuery();
				StringBuilder saleQry = new StringBuilder(
						"select retailer_org_id,sum(sale) as sale from (");
				StringBuilder cancelQry = new StringBuilder(
						"select retailer_org_id,sum(cancel) as cancel from (");
				while (rsGame.next()) {
					saleQry
							.append("(select cs.retailer_org_id,sum(net_amt) as sale from st_cs_sale_"
									+ rsGame.getInt("category_id")
									+ " cs where transaction_id in ("
									+ saleTranCS
									+ ") group by cs.retailer_org_id) union all");

					cancelQry
							.append("(select cs.retailer_org_id,sum(net_amt) as cancel from st_cs_refund_"
									+ rsGame.getInt("category_id")
									+ " cs where transaction_id in ("
									+ cancelTranCS
									+ ") group by cs.retailer_org_id) union all");
				}

				saleQry.delete(saleQry.lastIndexOf("union all"), saleQry
						.length());
				cancelQry.delete(cancelQry.lastIndexOf("union all"), cancelQry
						.length());
				
				saleQry.append(") saleTlb group by retailer_org_id");
				cancelQry.append(") cancelTlb group by retailer_org_id");
				logger.debug("-------CS Sale Query------\n" + saleQry);
				logger.debug("-------CS Cancel Query------\n"
						+ cancelQry);
				// CS Sale Query
				pstmt = con.prepareStatement(saleQry.toString());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					agtMap.get(rs.getString("retailer_org_id")).setCSSale(
							rs.getDouble("sale"));
				}
				// CS Cancel Query
				pstmt = con.prepareStatement(cancelQry.toString());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					agtMap.get(rs.getString("retailer_org_id")).setCSCancel(
							rs.getDouble("cancel"));
				}
			}
			if (ReportUtility.isOLA) {
				depositTranOLA = "select transaction_id from st_lms_retailer_transaction_master where transaction_type in('OLA_DEPOSIT') and transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "' and retailer_org_id in (" + agtOrgQry + ")";
				cancelTranOLA = "select transaction_id from st_lms_retailer_transaction_master where transaction_type in('OLA_DEPOSIT_REFUND') and transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "' and retailer_org_id in (" + agtOrgQry + ")";
				withdrawalTranOLA = "select transaction_id from st_lms_retailer_transaction_master where transaction_type in('OLA_WITHDRAWL') and transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "' and retailer_org_id in (" + agtOrgQry + ")";

				
				StringBuilder depositQry = new StringBuilder(
						"select retailer_org_id,sum(deposit_amt) as deposit_amt from (");
				StringBuilder depositCancelQry = new StringBuilder(
						"select retailer_org_id,sum(depositRefund) as depositRefund from (");
				StringBuilder withdrawalQry = new StringBuilder(
						"select retailer_org_id,sum(withdrawl_amt) as withdrawl_amt from (");
				
				depositQry
							.append("(select drs.retailer_org_id,sum(net_amt) as deposit_amt from st_ola_ret_deposit drs where transaction_id in ("
									+ depositTranOLA
									+ ") group by drs.retailer_org_id)");

				depositCancelQry
							.append("(select drs.retailer_org_id,sum(net_amt) as depositRefund from st_ola_ret_deposit_refund drs where transaction_id in ("
									+ cancelTranOLA
									+ ") group by drs.retailer_org_id)");

				withdrawalQry
							.append("(select drs.retailer_org_id,sum(net_amt) as withdrawl_amt from st_ola_ret_withdrawl drs where transaction_id in ("
									+ withdrawalTranOLA
									+ ") group by drs.retailer_org_id)");

				String netGamingQry = "select netGaming.retailer_org_id,sum(netGaming.netAmt) as total_amt from (select drs.ret_org_id retailer_org_id,sum(commission_calculated) as netAmt from st_ola_agt_ret_commission drs where transaction_id in (select transaction_id from st_lms_agent_transaction_master where transaction_type in('OLA_COMMISSION') and transaction_date>='"+startDate+"' and transaction_date<='"+endDate+"' and party_id in (select organization_id from st_lms_organization_master where parent_id="+agtOrgId+" order by name)) group by drs.ret_org_id) netGaming group by netGaming.retailer_org_id";

				depositQry.append(") depositTlb group by retailer_org_id");
				depositCancelQry.append(") cancelTlb group by retailer_org_id");
				withdrawalQry.append(") withdrawalTlb group by retailer_org_id");
				System.out.println("------OLA Deposit Qurey------\n" + depositQry);
				System.out.println("-------OLA Deposit Refund Qurey------\n"
						+ depositCancelQry);
				System.out.println("-------OLA Withdrawal Qurey------\n" + withdrawalQry);

				// OLA Deposit Query
				pstmt = con.prepareStatement(depositQry.toString());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					agtMap.get(rs.getString("retailer_org_id")).setOlaDepositAmt(
							rs.getDouble("deposit_amt"));
				}
				// OLA Deposit Refund Query
				pstmt = con.prepareStatement(depositCancelQry.toString());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					agtMap.get(rs.getString("retailer_org_id")).setOlaDepositCancelAmt(
							rs.getDouble("depositRefund"));
				}
				// OLA Withdrawal Query
				pstmt = con.prepareStatement(withdrawalQry.toString());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					agtMap.get(rs.getString("retailer_org_id")).setOlaWithdrawalAmt(
							rs.getDouble("withdrawl_amt"));
				}
				// OLA Net Gaming Query
				pstmt = con.prepareStatement(netGamingQry);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					agtMap.get(rs.getString("retailer_org_id")).setOlaNetGaming(
							rs.getDouble("total_amt"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return agtMap;
	}

	public Map<String, Map<String, Map<String, CompleteCollectionBean>>> collectionRetailerWiseExpand(
			Timestamp startDate, Timestamp endDate, Connection con, int agtOrgId) {
		PreparedStatement pstmt = null;
		ResultSet rsGame = null;
		ResultSet rs = null;
		ResultSet rsRetOrg = null;
		Map<String, Map<String, Map<String, CompleteCollectionBean>>> serGameAgtMap = new LinkedHashMap<String, Map<String, Map<String, CompleteCollectionBean>>>();
		Map<String, Map<String, CompleteCollectionBean>> gameAgtMap = null;
		// for Draw Game
		String saleTranDG = null;
		String cancelTranDG = null;
		String pwtTranDG = null;
		StringBuilder drawQry = null;
		// for scratch game
		StringBuilder scratchQry = null;
		if (startDate.after(endDate)) {
			return serGameAgtMap;
		}
		try {
			
			// Get All Agent
			String orgCodeQry = " name orgCode ";

			
			if ((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE")) {
				orgCodeQry = "org_code orgCode ";
	

			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("CODE_NAME")) {
				orgCodeQry = " concat(org_code,'_',name)  orgCode  ";
			

			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("NAME_CODE")) {
				orgCodeQry = " concat(name,'_',org_code)  orgCode ";
			

			}
			String agtOrgQry = "select "+orgCodeQry+",organization_id from st_lms_organization_master where parent_id="
					+ agtOrgId + " order by "+QueryManager.getAppendOrgOrder();
			pstmt = con.prepareStatement(agtOrgQry);
			rsRetOrg = pstmt.executeQuery();

			if (ReportUtility.isDG) {
				gameAgtMap = new LinkedHashMap<String, Map<String, CompleteCollectionBean>>();
				drawQry = new StringBuilder(
						"select agtTlb.organization_id,ifnull(sale,0.0) sale,ifnull(cancel,0.0) cancel,ifnull(pwt,0.0)  pwt from (select sale.organization_id,sale,cancel,pwt from ");
				saleTranDG = "(select bb.organization_id,sum(sale) as sale from (select drs.retailer_org_id,sum(net_amt) as sale from st_dg_ret_sale_? drs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_SALE','DG_SALE_OFFLINE') and transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "') group by drs.retailer_org_id) aa right outer join (select organization_id from st_lms_organization_master where parent_id="
						+ agtOrgId
						+ ")bb on retailer_org_id=organization_id group by organization_id) sale,";
				cancelTranDG = "(select bb.organization_id,sum(cancel) as cancel from (select drs.retailer_org_id,sum(net_amt) as cancel from st_dg_ret_sale_refund_? drs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') and transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "') group by drs.retailer_org_id) aa right outer join (select organization_id from st_lms_organization_master where parent_id="
						+ agtOrgId
						+ ") bb on retailer_org_id=organization_id group by organization_id) cancel,";
				pwtTranDG = "(select bb.organization_id,sum(pwt) as pwt from (select drs.retailer_org_id,sum(pwt_amt+retailer_claim_comm) as pwt from st_dg_ret_pwt_? drs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') and transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "') group by drs.retailer_org_id) aa right outer join (select organization_id from st_lms_organization_master where parent_id="
						+ agtOrgId
						+ ") bb on retailer_org_id=organization_id group by organization_id) pwt ";
				drawQry.append(saleTranDG);
				drawQry.append(cancelTranDG);
				drawQry.append(pwtTranDG);
				drawQry
						.append(" where sale.organization_id=cancel.organization_id and sale.organization_id=pwt.organization_id and cancel.organization_id=pwt.organization_id) gameTlb right outer join (select organization_id from st_lms_organization_master where parent_id="
								+ agtOrgId
								+ ") agtTlb on gameTlb.organization_id=agtTlb.organization_id");
				System.out.println("For Expand draw game Qry:: " + drawQry);

				// Game Master Query
				String gameQry =ReportUtility.getDrawGameMapQuery(startDate);
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				rsGame = gamePstmt.executeQuery();
				while (rsGame.next()) {
					int gameId = rsGame.getInt("game_id");
					Map<String, CompleteCollectionBean> gameMap = new LinkedHashMap<String, CompleteCollectionBean>();
					gameMap.putAll(getAgentMap(rsRetOrg));
					pstmt = con.prepareStatement(drawQry.toString());
					pstmt.setInt(1, gameId);
					pstmt.setInt(2, gameId);
					pstmt.setInt(3, gameId);
					System.out.println("-------Indivisual Game Qry-------\n"
							+ pstmt);
					rs = pstmt.executeQuery();
					while (rs.next()) {
						String agtId = rs.getString("organization_id");
						gameMap.get(agtId).setDrawSale(rs.getDouble("sale"));
						gameMap.get(agtId)
								.setDrawCancel(rs.getDouble("cancel"));
						gameMap.get(agtId).setDrawPwt(rs.getDouble("pwt"));

					}
					gameAgtMap.put(rsGame.getString("game_name"), gameMap);
				}

				serGameAgtMap.put("DG", gameAgtMap);
			}
			if (ReportUtility.isSE) {
				gameAgtMap = new LinkedHashMap<String, Map<String, CompleteCollectionBean>>();
				scratchQry = new StringBuilder(
						"select agtTlb.organization_id,ifnull(sale,0.0) sale,ifnull(pwt,0.0) pwt from (select sale.organization_id,sale,pwt from");

				logger.info("----Type Select ---"
						+ LMSFilterDispatcher.seSaleReportType);
				if ("BOOK_WISE".equals(LMSFilterDispatcher.seSaleReportType)) {
					saleTranDG = "(select organization_id,name,ifnull(mrpAmt,0.0) mrpAmt,ifnull(netAmt,0.0) sale from st_lms_organization_master left outer join (select sale.retailer_org_id,(ifnull(mrpAmt,0.0)-ifnull(mrpAmtRet,0.0)) mrpAmt,(ifnull(netAmt,0.0)-ifnull(netAmtRet,0.0)) netAmt from (select retailer_org_id,sum(mrp_amt) mrpAmt,sum(net_amt) netAmt from st_se_agent_retailer_transaction where transaction_id in (select transaction_id from st_lms_agent_transaction_master where transaction_type='SALE' and transaction_date>='"
							+ startDate
							+ "' and transaction_date<='"
							+ endDate
							+ "') and game_id=? and agent_org_id="
							+ agtOrgId
							+ " group by retailer_org_id) sale left outer join (select retailer_org_id,sum(mrp_amt) mrpAmtRet,sum(net_amt) netAmtRet from st_se_agent_retailer_transaction where transaction_id in (select transaction_id from st_lms_agent_transaction_master where transaction_type='SALE_RET' and transaction_date>='"
							+ startDate
							+ "' and transaction_date<='"
							+ endDate
							+ "') and game_id=? and agent_org_id="
							+ agtOrgId
							+ " group by retailer_org_id) saleRet on sale.retailer_org_id=saleRet.retailer_org_id) saleTlb on organization_id=retailer_org_id where parent_id="
							+ agtOrgId + ") sale,";
				} else if ("TICKET_WISE"
						.equals(LMSFilterDispatcher.seSaleReportType)) {
					saleTranDG = "(select organization_id,name,ifnull(mrpAmt,0.0) mrpAmt,ifnull(netAmt,0.0) sale from st_lms_organization_master left outer join (select current_owner_id,sum(soldTkt*ticket_price) mrpAmt,sum((soldTkt*ticket_price)-(soldTkt*ticket_price*transacrion_sale_comm_rate*0.01)) netAmt from st_se_game_master gm,st_se_game_inv_detail gid,(select game_id,book_nbr,sum(sold_tickets) soldTkt from st_se_game_ticket_inv_history where date>='"
							+ startDate
							+ "' and date<='"
							+ endDate
							+ "' and current_owner='RETAILER' and game_id=? group by book_nbr) TktTlb where gm.game_id=TktTlb.game_id and gm.game_id=? and TktTlb.book_nbr=gid.book_nbr and gid.current_owner='RETAILER' group by current_owner_id) saleTlb on organization_id=current_owner_id where organization_type='RETAILER' and parent_id="
							+ agtOrgId + ") sale,";
				}

				pwtTranDG = "(select bb.organization_id,sum(pwt) as pwt from (select retailer_org_id,sum(pwt_amt+(pwt_amt*claim_comm*0.01)) pwt from st_se_retailer_pwt where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "' and transaction_type='PWT') and game_id=? group by retailer_org_id) aa right outer join (select organization_id from st_lms_organization_master where parent_id="
						+ agtOrgId
						+ ") bb on retailer_org_id=organization_id group by organization_id) pwt ";
				scratchQry.append(saleTranDG);
				scratchQry.append(pwtTranDG);
				scratchQry
						.append(" where sale.organization_id=pwt.organization_id) gameTlb right outer join (select organization_id from st_lms_organization_master where parent_id="
								+ agtOrgId
								+ ") agtTlb on gameTlb.organization_id=agtTlb.organization_id");
				System.out.println("For Expand scratch game Qry:: "
						+ scratchQry);

				// Game Master Query
				String gameQry = "select game_id,game_name from st_se_game_master";
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				rsGame = gamePstmt.executeQuery();
				while (rsGame.next()) {
					int gameId = rsGame.getInt("game_id");
					Map<String, CompleteCollectionBean> gameMap = new LinkedHashMap<String, CompleteCollectionBean>();
					gameMap.putAll(getAgentMap(rsRetOrg));
					pstmt = con.prepareStatement(scratchQry.toString());
					pstmt.setInt(1, gameId);
					pstmt.setInt(2, gameId);
					pstmt.setInt(3, gameId);
					System.out.println("-------Indivisual Game Qry-------\n"
							+ pstmt);
					rs = pstmt.executeQuery();
					while (rs.next()) {
						String agtId = rs.getString("organization_id");
						gameMap.get(agtId).setDrawSale(rs.getDouble("sale"));
						gameMap.get(agtId).setDrawPwt(rs.getDouble("pwt"));
					}

					gameAgtMap.put(rsGame.getString("game_name"), gameMap);
				}
				serGameAgtMap.put("SE", gameAgtMap);
			}
			System.out.println(serGameAgtMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return serGameAgtMap;
	}

	public Map<String, Map<String, Map<String, CompleteCollectionBean>>> collectionRetailerWiseExpandMrpWise(
			Timestamp startDate, Timestamp endDate, Connection con, int agtOrgId) {
		PreparedStatement pstmt = null;
		ResultSet rsGame = null;
		ResultSet rs = null;
		ResultSet rsRetOrg = null;
		Map<String, Map<String, Map<String, CompleteCollectionBean>>> serGameAgtMap = new LinkedHashMap<String, Map<String, Map<String, CompleteCollectionBean>>>();
		Map<String, Map<String, CompleteCollectionBean>> gameAgtMap = null;
		// for Draw Game
		String saleTranDG = null;
		String cancelTranDG = null;
		String pwtTranDG = null;
		StringBuilder drawQry = null;
		// for scratch game
		StringBuilder scratchQry = null;
		if (startDate.after(endDate)) {
			return serGameAgtMap;
		}
		try {

			// Get All Agent
			String agtOrgQry = "select name,organization_id from st_lms_organization_master where parent_id="
					+ agtOrgId + " order by name";
			pstmt = con.prepareStatement(agtOrgQry);
			rsRetOrg = pstmt.executeQuery();

			if (ReportUtility.isDG) {
				gameAgtMap = new LinkedHashMap<String, Map<String, CompleteCollectionBean>>();
				drawQry = new StringBuilder(
						"select agtTlb.organization_id,ifnull(sale,0.0) sale,ifnull(cancel,0.0) cancel,ifnull(pwt,0.0)  pwt from (select sale.organization_id,sale,cancel,pwt from ");
				saleTranDG = "(select bb.organization_id,sum(sale) as sale from (select drs.retailer_org_id,sum(mrp_amt) as sale from st_dg_ret_sale_? drs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_SALE','DG_SALE_OFFLINE') and transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "') group by drs.retailer_org_id) aa right outer join (select organization_id from st_lms_organization_master where parent_id="
						+ agtOrgId
						+ ")bb on retailer_org_id=organization_id group by organization_id) sale,";
				cancelTranDG = "(select bb.organization_id,sum(cancel) as cancel from (select drs.retailer_org_id,sum(mrp_amt) as cancel from st_dg_ret_sale_refund_? drs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') and transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "') group by drs.retailer_org_id) aa right outer join (select organization_id from st_lms_organization_master where parent_id="
						+ agtOrgId
						+ ") bb on retailer_org_id=organization_id group by organization_id) cancel,";
				pwtTranDG = "(select bb.organization_id,sum(pwt) as pwt from (select drs.retailer_org_id,sum(pwt_amt) as pwt from st_dg_ret_pwt_? drs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') and transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "') group by drs.retailer_org_id) aa right outer join (select organization_id from st_lms_organization_master where parent_id="
						+ agtOrgId
						+ ") bb on retailer_org_id=organization_id group by organization_id) pwt ";
				drawQry.append(saleTranDG);
				drawQry.append(cancelTranDG);
				drawQry.append(pwtTranDG);
				drawQry
						.append(" where sale.organization_id=cancel.organization_id and sale.organization_id=pwt.organization_id and cancel.organization_id=pwt.organization_id) gameTlb right outer join (select organization_id from st_lms_organization_master where parent_id="
								+ agtOrgId
								+ ") agtTlb on gameTlb.organization_id=agtTlb.organization_id");
				System.out.println("For Expand draw game Qry:: " + drawQry);

				// Game Master Query
				String gameQry = "select game_id,game_name from st_dg_game_master";
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				rsGame = gamePstmt.executeQuery();
				while (rsGame.next()) {
					int gameId = rsGame.getInt("game_id");
					Map<String, CompleteCollectionBean> gameMap = new LinkedHashMap<String, CompleteCollectionBean>();
					gameMap.putAll(getAgentMap(rsRetOrg));
					pstmt = con.prepareStatement(drawQry.toString());
					pstmt.setInt(1, gameId);
					pstmt.setInt(2, gameId);
					pstmt.setInt(3, gameId);
					System.out.println("-------Indivisual Game Qry-------\n"
							+ pstmt);
					rs = pstmt.executeQuery();
					while (rs.next()) {
						String agtId = rs.getString("organization_id");
						gameMap.get(agtId).setDrawSale(rs.getDouble("sale"));
						gameMap.get(agtId)
								.setDrawCancel(rs.getDouble("cancel"));
						gameMap.get(agtId).setDrawPwt(rs.getDouble("pwt"));

					}
					gameAgtMap.put(rsGame.getString("game_name"), gameMap);
				}

				serGameAgtMap.put("DG", gameAgtMap);
			}
			if (ReportUtility.isSE) {
				gameAgtMap = new LinkedHashMap<String, Map<String, CompleteCollectionBean>>();
				scratchQry = new StringBuilder(
						"select agtTlb.organization_id,ifnull(sale,0.0) sale,ifnull(pwt,0.0) pwt from (select sale.organization_id,sale,pwt from");
				saleTranDG = "(select bb.organization_id,sum(sale) as sale from (select current_owner_id,sum(sale) sale from (select gmti.game_id,gmti.current_owner_id, (gmti.sold_tickets*ticket_price) sale from st_se_game_ticket_inv_history gmti ,st_se_game_master gm where gmti.current_owner='RETAILER'and gmti.date>='"
						+ startDate
						+ "' and gmti.date<='"
						+ endDate
						+ "' and gmti.game_id=gm.game_id and gm.game_id=?) aa group by current_owner_id) aa right outer join (select organization_id from st_lms_organization_master where parent_id="
						+ agtOrgId
						+ ") bb on current_owner_id=organization_id group by organization_id) sale, ";
				pwtTranDG = "(select bb.organization_id,sum(pwt) as pwt from (select retailer_org_id,sum(pwt_amt) pwt from st_se_retailer_pwt where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "' and transaction_type='PWT') and game_id=? group by retailer_org_id) aa right outer join (select organization_id from st_lms_organization_master where parent_id="
						+ agtOrgId
						+ ") bb on retailer_org_id=organization_id group by organization_id) pwt ";
				scratchQry.append(saleTranDG);
				scratchQry.append(pwtTranDG);
				scratchQry
						.append(" where sale.organization_id=pwt.organization_id) gameTlb right outer join (select organization_id from st_lms_organization_master where parent_id=4) agtTlb on gameTlb.organization_id=agtTlb.organization_id");
				System.out.println("For Expand scratch game Qry:: "
						+ scratchQry);

				// Game Master Query
				String gameQry = "select game_id,game_name from st_se_game_master";
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				rsGame = gamePstmt.executeQuery();
				while (rsGame.next()) {
					int gameId = rsGame.getInt("game_id");
					Map<String, CompleteCollectionBean> gameMap = new LinkedHashMap<String, CompleteCollectionBean>();
					gameMap.putAll(getAgentMap(rsRetOrg));
					pstmt = con.prepareStatement(scratchQry.toString());
					pstmt.setInt(1, gameId);
					pstmt.setInt(2, gameId);
					System.out.println("-------Indivisual Game Qry-------\n"
							+ pstmt);
					rs = pstmt.executeQuery();
					while (rs.next()) {
						String agtId = rs.getString("organization_id");
						gameMap.get(agtId).setDrawSale(rs.getDouble("sale"));
						gameMap.get(agtId).setDrawPwt(rs.getDouble("pwt"));
					}

					gameAgtMap.put(rsGame.getString("game_name"), gameMap);
				}
				serGameAgtMap.put("SE", gameAgtMap);
			}
			System.out.println(serGameAgtMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return serGameAgtMap;
	}

	private Map<String, CompleteCollectionBean> getAgentMap(ResultSet rsRetOrg) throws SQLException {
		Map<String, CompleteCollectionBean> agtMap = new LinkedHashMap<String, CompleteCollectionBean>();
		CompleteCollectionBean collBean = null;

		while (rsRetOrg.next()) {
			collBean = new CompleteCollectionBean();
			collBean.setOrgName(rsRetOrg.getString("orgCode"));
			if (ReportUtility.isDG) {
				collBean.setDrawSale(0.0);
				collBean.setDrawPwt(0.0);
				collBean.setDrawCancel(0.0);
			}
			if (ReportUtility.isSE) {
				collBean.setScratchSale(0.0);
				collBean.setScratchPwt(0.0);
			}
			if(ReportUtility.isCS){
				collBean.setCSSale(0.0);
				collBean.setCSCancel(0.0);
			}
			if(ReportUtility.isOLA)
			{
				collBean.setOlaDepositAmt(0.0);
				collBean.setOlaDepositCancelAmt(0.0);
				collBean.setOlaWithdrawalAmt(0.0);
				collBean.setOlaNetGaming(0.0);
			}
			agtMap.put(rsRetOrg.getString("organization_id"), collBean);
		}
		rsRetOrg.beforeFirst();
		return agtMap;
	}

	private Map<String, CompleteCollectionBean> getDayMap(Timestamp startDate,
			Timestamp endDate) throws SQLException {
		Map<String, CompleteCollectionBean> dateMap = new LinkedHashMap<String, CompleteCollectionBean>();
		CompleteCollectionBean collBean = null;
		String date = null;
		// Get All Day
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(startDate);

		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endDate);
		while (startCal.getTime().before(endCal.getTime())) {
			date = new java.sql.Date(startCal.getTimeInMillis()).toString();
			collBean = new CompleteCollectionBean();
			collBean.setOrgName(date);
			dateMap.put(date, collBean);
			startCal.setTimeInMillis(startCal.getTimeInMillis() + 24 * 60 * 60
					* 1000);
			if (ReportUtility.isDG) {
				collBean.setDrawSale(0.0);
				collBean.setDrawPwt(0.0);
				collBean.setDrawCancel(0.0);
				collBean.setDrawDirPlyPwt(0.0);
			}
			if (ReportUtility.isSE) {
				collBean.setScratchSale(0.0);
				collBean.setScratchPwt(0.0);
				collBean.setScratchDirPlyPwt(0.0);
			}
			if (ReportUtility.isCS) {
				collBean.setCSSale(0.0);
				collBean.setCSCancel(0.0);
			}
			if(ReportUtility.isOLA)
			{
				collBean.setOlaDepositAmt(0.0);
				collBean.setOlaDepositCancelAmt(0.0);
				collBean.setOlaWithdrawalAmt(0.0);
				collBean.setOlaNetGaming(0.0);
			}
		}
		return dateMap;
	}

	

	public Map<Integer, String> getOrgAddMap(String orgType, int parentId)
			throws LMSException {
		Map<Integer, String> orgAddMap = new TreeMap<Integer, String>();
		Connection con = null;
		con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			if (orgType.equalsIgnoreCase("AGENT")) {
				pstmt = con
						.prepareStatement("select addr_line1, addr_line2, city, organization_id from st_lms_organization_master where organization_type='AGENT'");

			} else if (orgType.equalsIgnoreCase("RETAILER")) {
				pstmt = con
						.prepareStatement("select addr_line1, addr_line2, city, organization_id from st_lms_organization_master where organization_type='RETAILER' and parent_id = ?");
				pstmt.setInt(1, parentId);
			}
			rs = pstmt.executeQuery();
			while (rs.next()) {
				orgAddMap.put(rs.getInt("organization_id"), rs
						.getString("addr_line1")
						+ ","
						+ rs.getString("addr_line2")
						+ ","
						+ rs.getString("city"));
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
		return orgAddMap;
	}

	

	public Map<String, Map<String, Map<String, Map<String, CompleteCollectionBean>>>> transactionReportForAgent(
			Timestamp startDate, Timestamp endDate, String reportType,
			int agtOrgId) {
		
		Connection con = null;
		Map<String, Map<String, Map<String, Map<String, CompleteCollectionBean>>>> resultMap = new HashMap<String, Map<String, Map<String, Map<String, CompleteCollectionBean>>>>();
		Map<String, Map<String, Map<String, CompleteCollectionBean>>> agtMap = null;
		
		try {
			con = DBConnect.getConnection();
			
			if ("Retailer Wise Expand".equals(reportType)) {
				agtMap = collectionRetailerWiseExpand(startDate, endDate, con, agtOrgId);
				resultMap.put("NetAmt", agtMap);
				agtMap = collectionRetailerWiseExpandMrpWise(startDate,
						endDate, con, agtOrgId);
				resultMap.put("MrpAmt", agtMap);
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
		return resultMap;
	}
}
