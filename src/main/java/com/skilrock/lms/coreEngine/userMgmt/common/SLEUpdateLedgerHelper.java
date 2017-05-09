package com.skilrock.lms.coreEngine.userMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.OrgCreditUpdation;
import com.skilrock.lms.web.drawGames.common.Util;

public class SLEUpdateLedgerHelper {
	
	private static Logger logger = LoggerFactory.getLogger(SLEUpdateLedgerHelper.class);
	private static final int batchSize = 500;
	/*public static void main(String[] args) throws ClassNotFoundException,SQLException,LMSException {/*
		Statement stmt=null;
		Class.forName("com.mysql.jdbc.Driver");
		Connection connection = DriverManager.getConnection("jdbc:mysql://192.168.124.125/lms_zim3", "root", "root");
		runLedgerForSLE(connection);
		
	}*/
	protected static void runLedgerForSLE(Connection con) throws LMSException{
		logger.debug(new Date()+"---Update Ledger Of SLE Start At--"+new Date().getTime());
		Statement stmt=null;
		try {
			stmt=con.createStatement();
			stmt.execute("CREATE TEMPORARY TABLE orgTransSummary (agent_ref_transaction_id BIGINT NOT NULL, ret_comm DECIMAL(20,2) NOT NULL DEFAULT 0.00, agt_comm DECIMAL(20,2) NOT NULL DEFAULT 0.00, govt_comm DECIMAL(20,2) NOT NULL DEFAULT 0.00, retailer_org_id INT UNSIGNED NOT NULL DEFAULT 0, game_id INT UNSIGNED NOT NULL DEFAULT 0,game_type_id INT UNSIGNED NOT NULL DEFAULT 0,trans_date DATETIME)");
			fillTempSLETransCommTlbForRet(con);
			// ADD Transaction Date in Agent Sale table While inserting the transactions while update ledger
			updateSLESaleLedgerForRet(con);
			
			
			
			// INCOMPLETE
			updateSLEPwtLedgerForRet(con);
			logger.debug(new Date()+"---Draw Game Sale Pwt Complete for Retailer Start At--"+new Date().getTime());
			stmt.execute("drop table orgTransSummary");
			stmt.execute("CREATE TEMPORARY TABLE agentTransSummary (bo_ref_transaction_id BIGINT NOT NULL, agt_comm DECIMAL(20,2) NOT NULL DEFAULT 0.00, govt_comm DECIMAL(20,2) NOT NULL DEFAULT 0.00, agent_org_id INT UNSIGNED NOT NULL DEFAULT 0, game_id INT UNSIGNED NOT NULL DEFAULT 0,game_type_id INT UNSIGNED NOT NULL DEFAULT 0,trans_date DATETIME)");
			fillTempSLETransCommTlbForAgt(con);
			updateSLESaleLedgerForAgt(con);
			
			
			updateSLEPwtLedgerForAgt(con);
			stmt.execute("drop table agentTransSummary");
			//logger.debug(new Date()+"---Draw Game Sale Pwt Complete for Agent Start At--"+new Date().getTime());
			//logger.debug(new Date()+"---Update Ledger Of Draw Game End At--"+new Date().getTime());
			
			/*cancelSleUpdateLedger(con);*/

		} catch (Exception e) {
			throw new LMSException(); // TEMP
		} finally {
			DBConnect.closeStmt(stmt);
		}	
	}
	
	protected static void cancelSleUpdateLedger(Connection con) throws LMSException{
	
		Statement stmt=null;
		try {
			logger.debug("SLE REFUND UPDATE LEDGET STARTS.......");
			stmt=con.createStatement();
			stmt.execute("drop table IF EXISTS orgTransSummary");
			//stmt.execute("CREATE TEMPORARY TABLE orgTransSummary (agent_ref_transaction_id BIGINT NOT NULL, ret_comm DECIMAL(20,2) NOT NULL DEFAULT 0.00, agt_comm DECIMAL(20,2) NOT NULL DEFAULT 0.00, govt_comm DECIMAL(20,2) NOT NULL DEFAULT 0.00, retailer_org_id INT UNSIGNED NOT NULL DEFAULT 0, game_id INT UNSIGNED NOT NULL DEFAULT 0,game_type_id INT UNSIGNED NOT NULL DEFAULT 0,trans_date DATETIME)");
			stmt.execute("CREATE TABLE orgTransSummary (agent_ref_transaction_id BIGINT NOT NULL, ret_comm DECIMAL(20,2) NOT NULL DEFAULT 0.00, agt_comm DECIMAL(20,2) NOT NULL DEFAULT 0.00, govt_comm DECIMAL(20,2) NOT NULL DEFAULT 0.00, retailer_org_id INT UNSIGNED NOT NULL DEFAULT 0, game_id INT UNSIGNED NOT NULL DEFAULT 0,game_type_id INT UNSIGNED NOT NULL DEFAULT 0,trans_date DATETIME)");
			fillTempSLETransCommTlbForRet(con);
			// ADD Transaction Date in Agent Sale table While inserting the transactions while update ledger
			updateSLESaleRefundLedgerForRet(con);
			stmt.execute("drop table IF EXISTS orgTransSummary");
			stmt.execute("drop table  IF EXISTS agentTransSummary");
			stmt.execute("CREATE TEMPORARY TABLE agentTransSummary (bo_ref_transaction_id BIGINT NOT NULL, agt_comm DECIMAL(20,2) NOT NULL DEFAULT 0.00, govt_comm DECIMAL(20,2) NOT NULL DEFAULT 0.00, agent_org_id INT UNSIGNED NOT NULL DEFAULT 0, game_id INT UNSIGNED NOT NULL DEFAULT 0,game_type_id INT UNSIGNED NOT NULL DEFAULT 0,trans_date DATETIME)");
			fillTempSLETransCommTlbForAgt(con);
			updateSLESaleRefundLedgerForAgt(con);
			stmt.execute("drop table IF EXISTS agentTransSummary");
			logger.debug("SLE REFUND UPDATE LEDGET ENDS.......");
			
			
			
		
			// INCOMPLETE
			//updateSLEPwtLedgerForRet(con);
			//logger.debug(new Date()+"---Draw Game Sale Pwt Complete for Retailer Start At--"+new Date().getTime());
			//stmt.execute("drop table orgTransSummary");
			//stmt.execute("CREATE TEMPORARY TABLE agentTransSummary (bo_ref_transaction_id BIGINT NOT NULL, agt_comm DECIMAL(20,2) NOT NULL DEFAULT 0.00, govt_comm DECIMAL(20,2) NOT NULL DEFAULT 0.00, agent_org_id INT UNSIGNED NOT NULL DEFAULT 0, game_id INT UNSIGNED NOT NULL DEFAULT 0,game_type_id INT UNSIGNED NOT NULL DEFAULT 0,trans_date DATETIME)");
			//fillTempSLETransCommTlbForAgt(con);
			//updateSLESaleLedgerForAgt(con);
			//updateSLEPwtLedgerForAgt(con);
			//tempStmt.execute("drop table agentTransSummary");
			//logger.debug(new Date()+"---Draw Game Sale Pwt Complete for Agent Start At--"+new Date().getTime());
			//logger.debug(new Date()+"---Update Ledger Of Draw Game End At--"+new Date().getTime());

		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(); // TEMP
		} 
	}

	
	
	private static void fillTempSLETransCommTlbForRet(Connection con) throws LMSException{

		//logger.info("fillTempSLETransCommTlbForRet() starts.......AT "+new Date() +" "+ new Date().getTime());
		//ResultSet rs = null;
		//PreparedStatement pstmt = null;
		try {
			con.prepareStatement("truncate table st_temp_update_ledger").executeUpdate();
			Statement stmt = con.createStatement();
			StringBuilder sb = new StringBuilder("insert into st_temp_update_ledger (trans_id, ret_comm, agt_comm, govt_comm) values");
			//String gameQry = "select * from (select game_id,game_type_id,sum(retailer_sale_comm_rate) ret_comm,sum(agent_sale_comm_rate) agt_comm,sum(govt_comm) govt_comm,now() date from (select  game_id,game_type_id ,retailer_sale_comm_rate,agent_sale_comm_rate,gov_comm_rate govt_comm from st_sle_game_type_master union all select game_id,game_type_id,sale_comm_variance,0,0 from st_sle_agent_retailer_sale_pwt_comm_variance where retailer_org_id=?  union all select game_id,game_type_id,0,sale_comm_variance,0 from st_sle_bo_agent_sale_pwt_comm_variance where agent_org_id=? ) aa group by game_type_id union all select gm.game_id,gm.game_type_id,retailer_sale_comm_rate+sale_comm_variance retComm,0 agtComm,0 govtComm,date_changed from st_sle_agent_retailer_sale_comm_variance_history his inner join st_sle_game_type_master gm on his.game_type_id=gm.game_type_id where retialer_org_id=? union all select gm.game_id,gm.game_type_id,0,agent_sale_comm_rate+sale_comm_variance agtComm,0,date_changed from st_dg_bo_agent_sale_comm_variance_history his inner join st_sle_game_type_master gm on his.game_id=gm.game_type_id where agent_org_id=? union all select game_id , game_type_id,0,0,govt_comm_rate,date_changed from st_sle_game_govt_comm_history ) aa where game_type_id = ? order by date desc";
			String gameQry = "select * from (select game_id,game_type_id,sum(retailer_sale_comm_rate) ret_comm,sum(agent_sale_comm_rate) agt_comm,sum(govt_comm) govt_comm,now() date from (select  game_id,game_type_id ,retailer_sale_comm_rate,agent_sale_comm_rate,gov_comm_rate govt_comm from st_sle_game_type_master union all select game_id,game_type_id,sale_comm_variance,0,0 from st_sle_agent_retailer_sale_pwt_comm_variance where retailer_org_id=?  union all select game_id,game_type_id,0,sale_comm_variance,0 from st_sle_agent_retailer_sale_pwt_comm_variance where agent_org_id=? ) aa group by game_type_id union all select gm.game_id,gm.game_type_id,retailer_sale_comm_rate+sale_comm_variance retComm,0 agtComm,0 govtComm,date_changed from st_sle_agent_retailer_sale_comm_variance_history his inner join st_sle_game_type_master gm on his.game_type_id=gm.game_type_id where retialer_org_id=? union all select gm.game_id,gm.game_type_id,0,agent_sale_comm_rate+sale_comm_variance agtComm,0,date_changed from st_sle_agent_retailer_sale_comm_variance_history his inner join st_sle_game_type_master gm on his.game_type_id=gm.game_type_id where agent_org_id=? union all select game_id , game_type_id,0,0,govt_comm_rate,date_changed from st_sle_game_govt_comm_history ) aa where game_type_id = ? order by date desc";
			PreparedStatement gameStmt = con.prepareStatement(gameQry);
			String transQry = "select sale.transaction_id,sale.game_id,sale.game_type_id ,tm.transaction_date,sale.retailer_org_id,parent_id from st_lms_retailer_transaction_master tm inner join st_lms_organization_master om inner join st_sle_ret_sale sale on tm.retailer_org_id=om.organization_id and tm.transaction_id=sale.transaction_id  where claim_status in('CLAIM_BAL') union all select sale.transaction_id,sale.game_id ,sale.game_type_id ,tm.transaction_date,sale.retailer_org_id,parent_id from st_lms_retailer_transaction_master tm inner join st_lms_organization_master om inner join st_sle_ret_sale_refund sale on tm.retailer_org_id=om.organization_id and tm.transaction_id=sale.transaction_id  where claim_status in('CLAIM_BAL')";
			PreparedStatement traStmt = con.prepareStatement(transQry);
			ResultSet tranRs = traStmt.executeQuery();
			int count =0; 
			while (tranRs.next()) {
					long transId = tranRs.getLong("transaction_id");
					int orgId = tranRs.getInt("retailer_org_id");
					int parentId = tranRs.getInt("parent_id");
					long transDate = tranRs.getTimestamp("transaction_date").getTime();
					int gameTypeId = tranRs.getInt("game_type_id");
					gameStmt.setInt(1, orgId);
					gameStmt.setInt(2, parentId);
					gameStmt.setInt(3, orgId);
					gameStmt.setInt(4, parentId);
					gameStmt.setInt(5, gameTypeId);
					ResultSet gameRs = gameStmt.executeQuery();
					while (gameRs.next()) {
						double retComm = 0.0;
						double 	agtComm = 0.0;
						double 	govtComm = 0.0;
						double retCommTemp = gameRs.getDouble("ret_comm");
						double agtCommTemp = gameRs.getDouble("agt_comm");
						double govtCommTemp = gameRs.getDouble("govt_comm");
						if (gameRs.getTimestamp("date").getTime() > transDate) {
							 retComm = ((retCommTemp != 0.0)?retCommTemp:0.0);
							 agtComm = ((agtCommTemp != 0.0)?agtCommTemp:0.0);
							 govtComm = ((govtCommTemp != 0.0)?govtCommTemp:0.0);
						}
						sb.append("(" + transId + ", " + retComm + ", " + agtComm + "," + govtComm + "),");
						count++;

						if (count > 500) {
							sb.deleteCharAt(sb.length() - 1);
							stmt.executeUpdate(sb.toString());
							count = 0;
							sb = null;
							sb = new StringBuilder("insert into st_temp_update_ledger (trans_id, ret_comm,agt_comm,govt_comm) values");
					}

				}
				if (count > 0) {
					sb.deleteCharAt(sb.length() - 1);
					logger.debug(sb.toString());
					stmt.executeUpdate(sb.toString());
					sb = new StringBuilder("insert into st_temp_update_ledger (trans_id, ret_comm,agt_comm,govt_comm) values");
				}
			}
			//logger.info("fillTempSLETransCommTlbForRet()  ends.......AT "+new Date() +" "+ new Date().getTime());
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("enable to fill comm table for retailer");
		}
	
		/*
		//logger.debug(new Date() + "---start---" + new Date().getTime());

		ResultSet rs = null;
		PreparedStatement pstmt = null;
		try {
			con.prepareStatement("truncate table st_temp_update_ledger").executeUpdate();

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
				
				gameQry = "select * from (select game_id,game_type_id,sum(retailer_sale_comm_rate) ret_comm,sum(agent_sale_comm_rate) agt_comm,sum(govt_comm) govt_comm,now() date from (select  game_id,game_type_id ,retailer_sale_comm_rate,agent_sale_comm_rate,gov_comm_rate govt_comm from st_sle_game_type_master union all select game_id,game_type_id,sale_comm_variance,0,0 from st_sle_agent_retailer_sale_pwt_comm_variance where retailer_org_id=3  union all select game_id,game_type_id,0,sale_comm_variance,0 from st_sle_bo_agent_sale_pwt_comm_variance where agent_org_id=2 ) aa group by game_id union all select gm.game_id,gm.game_type_id,retailer_sale_comm_rate+sale_comm_variance retComm,0 agtComm,0 govtComm,date_changed from st_sle_agent_retailer_sale_comm_variance_history his inner join st_sle_game_type_master gm on his.game_type_id=gm.game_type_id where retialer_org_id=3 union all select gm.game_id,gm.game_type_id,0,agent_sale_comm_rate+sale_comm_variance agtComm,0,date_changed from st_dg_bo_agent_sale_comm_variance_history his inner join st_sle_game_type_master gm on his.game_id=gm.game_type_id where agent_org_id=2 union all select game_id , game_type_id,0,0,govt_comm_rate,date_changed from st_sle_game_govt_comm_history ) aa order by date desc";
				
				PreparedStatement gameStmt = con.prepareStatement(gameQry);
				
				
				
				String transQry = "select sale.transaction_id,sale.game_id ,transaction_date,sale.retailer_org_id,parent_id from st_lms_retailer_transaction_master tm inner join st_lms_organization_master om inner join st_dg_ret_sale_"
						+ gameId
						+ " sale on tm.retailer_org_id=om.organization_id and tm.transaction_id=sale.transaction_id  where claim_status in('CLAIM_BAL') union all select sale.transaction_id,sale.game_id ,transaction_date,sale.retailer_org_id,parent_id from st_lms_retailer_transaction_master tm inner join st_lms_organization_master om inner join st_dg_ret_sale_refund_"
						+ gameId
						+ " sale on tm.retailer_org_id=om.organization_id and tm.transaction_id=sale.transaction_id  where claim_status in('CLAIM_BAL')";
				
				
				
				String transQry = "select sale.transaction_id,sale.game_id,sale.game_type_id ,tm.transaction_date,sale.retailer_org_id,parent_id from st_lms_retailer_transaction_master tm inner join st_lms_organization_master om inner join st_sle_ret_sale sale on tm.retailer_org_id=om.organization_id and tm.transaction_id=sale.transaction_id  where claim_status in('CLAIM_BAL') union all select sale.transaction_id,sale.game_id ,sale.game_type_id ,tm.transaction_date,sale.retailer_org_id,parent_id from st_lms_retailer_transaction_master tm inner join st_lms_organization_master om inner join st_sle_ret_sale_refund sale on tm.retailer_org_id=om.organization_id and tm.transaction_id=sale.transaction_id  where claim_status in('CLAIM_BAL')";
				
				
				PreparedStatement traStmt = con.prepareStatement(transQry);
				ResultSet tranRs = traStmt.executeQuery();
				
				while (tranRs.next()) {
					transId = tranRs.getLong("transaction_id");
					orgId = tranRs.getInt("retailer_org_id");
					parentId = tranRs.getInt("parent_id");
					transDate = tranRs.getTimestamp("transaction_date").getTime();
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
			//logger.debug(new Date() + "---end---" + new Date().getTime());
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("enable to fill comm table for retailer");
		}
	*/}
	
	private static void updateSLESaleLedgerForRet(Connection con) throws LMSException{

		//logger.info("Transactions from SALE in updateSLESaleLedgerForRet() begins........");
		Statement stmt = null;
		ResultSet rs = null;
		Map<String, List<Long>> transIdInvMap = null;
		Map<Integer, Double> orgIdAmountMap = new HashMap<Integer, Double>();
		Map<Integer, Integer> orgIdParentIdMap = new HashMap<Integer, Integer>();
		Map<Integer, Map<String, List<Long>>> orgIdTransIdInvRetMap = new HashMap<Integer, Map<String, List<Long>>>();
		Map<Integer, Map<String, List<Long>>> orgIdTransIdInvMap = new HashMap<Integer, Map<String, List<Long>>>();

		Map<String, List<Long>> transIdInvRetMap = null;
		Statement tempStmt=null;
		try {
			//String saleQry = "select a.game_type_id,b.game_id , a.retailer_org_id,agtUserId,agtOrgId,sum(mrp_amt) totalMrp ,sum(retailer_comm_amt) totalRetComm,sum(agent_comm_amt) totalagtComm, sum(good_cause_amt) totalgovtComm, sum(vat_amt) totalVat,sum(taxable_sale) totalTaxSale,DATE(a.transaction_date) 'transaction_date',ret_comm,agt_comm,govt_comm from st_sle_ret_sale a inner join st_lms_retailer_transaction_master b inner join st_temp_update_ledger c inner join (select um.user_id retUserId,um.organization_id retOrgId,um.parent_user_id agtUserId,om.parent_id agtOrgId from st_lms_organization_master om inner join st_lms_user_master um on om.organization_id=um.organization_id where om.organization_type='RETAILER' ) d on a.transaction_id = b.transaction_id and a.transaction_id=trans_id and a.retailer_org_id=retOrgId  where claim_status='CLAIM_BAL' group by a.retailer_org_id,DATE(transaction_date),a.game_type_id,ret_comm,agt_comm,govt_comm  order by retailer_org_id,game_type_id,transaction_date";
			String saleQry = "select a.game_type_id,a.game_id , retailer_org_id,agtUserId,agtOrgId,totalMrp , totalRetComm, totalagtComm,  totalgovtComm,  totalVat, totalTaxSale,transaction_date,ret_comm,agt_comm,govt_comm ,prize_payout_ratio,vat_amt from (select a.game_type_id,a.game_id , a.retailer_org_id,agtUserId,agtOrgId,sum(mrp_amt) totalMrp ,sum(retailer_comm_amt) totalRetComm,sum(agent_comm_amt) totalagtComm, sum(good_cause_amt) totalgovtComm, sum(vat_amt) totalVat,sum(taxable_sale) totalTaxSale,DATE(a.transaction_date) 'transaction_date',ret_comm,agt_comm,govt_comm from st_sle_ret_sale a inner join st_lms_retailer_transaction_master b inner join st_temp_update_ledger c inner join (select um.user_id retUserId,um.organization_id retOrgId,um.parent_user_id agtUserId,om.parent_id agtOrgId from st_lms_organization_master om inner join st_lms_user_master um on om.organization_id=um.organization_id where om.organization_type='RETAILER' ) d on a.transaction_id = b.transaction_id and a.transaction_id=trans_id and a.retailer_org_id=retOrgId  where claim_status='CLAIM_BAL' group by a.retailer_org_id,DATE(transaction_date),a.game_type_id,ret_comm,agt_comm,govt_comm  order by retailer_org_id,game_type_id,transaction_date) a inner join st_sle_game_type_master sle on sle.game_type_id = a.game_type_id ";
			PreparedStatement pstmtAgt = con.prepareStatement(QueryManager.insertInLMSTransactionMaster());
			PreparedStatement pstmtAgtTrans = con.prepareStatement(QueryManager.insertInAgentTransactionMaster());
			PreparedStatement insAgtSalePstmt = con.prepareStatement("insert into st_sle_agt_sale(transaction_id,agent_org_id,retailer_org_id,game_id,game_type_id,mrp_amt,retailer_comm_amt,net_amt,agent_comm_amt,bo_net_amt,claim_status,vat_amt,taxable_sale,govt_comm,transaction_date) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			PreparedStatement insAgtSaleRetPstmt = con.prepareStatement("insert into st_sle_agt_sale_refund(transaction_id,agent_org_id,retailer_org_id,game_id,game_type_id,mrp_amt,retailer_comm_amt,net_amt,agent_comm_amt,bo_net_amt,claim_status,vat_amt,taxable_sale,govt_comm,cancellation_charges,transaction_date) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			PreparedStatement insTempTablePstmt=con.prepareStatement("insert into orgTransSummary(agent_ref_transaction_id,ret_comm,agt_comm,govt_comm,retailer_org_id,game_id,game_type_id,trans_date) values(?,?,?,?,?,?,?,?)");			tempStmt=con.createStatement();
			tempStmt.execute("delete from orgTransSummary ");
			
			
			int count = 0;
			double retDebitAmount = 0.0;
			stmt = con.createStatement();
			rs = stmt.executeQuery(saleQry);

			while (rs.next()) {
				double agtCommRate = rs.getDouble("agt_comm");
				double totalAgtComm = rs.getDouble("totalagtComm");
				double totalMrpAmt = rs.getDouble("totalMrp");
				double totalRetComm = rs.getDouble("totalRetComm");
				double totalretNetAmt = totalMrpAmt - totalRetComm;
				double totalAgtNetAmt = totalMrpAmt - totalAgtComm;
				double retCommRate = rs.getDouble("ret_comm");
				int ppr = rs.getInt("prize_payout_ratio");
				double govtCommRate = rs.getDouble("govt_comm");
				double vatAmt = rs.getDouble("vat_amt");

				// CHECK IF necessary
				double tatalAgtTaxableSale = CommonMethods.calTaxableSale(totalMrpAmt, retCommRate, ppr, govtCommRate, vatAmt);

				int retOrgId = rs.getInt("retailer_org_id");
				if (orgIdAmountMap.containsKey(retOrgId)) {
					retDebitAmount = orgIdAmountMap.get(retOrgId) + totalretNetAmt;
				} else {
					retDebitAmount = totalretNetAmt;
				}

				orgIdAmountMap.put(retOrgId, retDebitAmount);
				int agtOrgId = rs.getInt("agtOrgId");
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
				int gameId = rs.getInt("game_id");
				int gameTypeId = rs.getInt("game_type_id");
				double totalAgtVat = rs.getDouble("totalVat");
				double totalGoodCauseAmt = rs.getDouble("totalgovtComm");
				String transDate = rs.getString("transaction_date");
				int agtUserId = rs.getInt("agtUserId");

				if (rsTrns.next()) {
					long transId = rsTrns.getLong(1);
					//logger.debug("--Retailer org id"+retOrgId+"--Agent org Id-- "+agtOrgId+" -- Start Sale for Game type "+gameTypeId +" with transaction "+ transId);
					if (!transIdInvMap.containsKey(transDate)) {
						transIdInvMap.put(transDate, new ArrayList<Long>());
					}

					
					transIdInvMap.get(transDate).add(transId);
					pstmtAgtTrans.setLong(1, transId);
					pstmtAgtTrans.setInt(2, agtUserId);
					pstmtAgtTrans.setInt(3, agtOrgId);
					pstmtAgtTrans.setString(4, "RETAILER");
					pstmtAgtTrans.setInt(5, retOrgId);
					pstmtAgtTrans.setString(6, "SLE_SALE");
					pstmtAgtTrans.setTimestamp(7, updateLedgerHelper.fetchTransTimeStamp(transDate));
					pstmtAgtTrans.executeUpdate();

					insAgtSalePstmt.setLong(1, transId);
					insAgtSalePstmt.setInt(2, agtOrgId);
					insAgtSalePstmt.setInt(3, retOrgId);
					insAgtSalePstmt.setInt(4, gameId);
					insAgtSalePstmt.setInt(5, gameTypeId);
					insAgtSalePstmt.setDouble(6, totalMrpAmt);
					insAgtSalePstmt.setDouble(7, totalRetComm);
					insAgtSalePstmt.setDouble(8, totalretNetAmt);
					insAgtSalePstmt.setDouble(9, totalAgtComm);
					insAgtSalePstmt.setDouble(10, totalAgtNetAmt);
					insAgtSalePstmt.setString(11, "CLAIM_BAL");
					insAgtSalePstmt.setDouble(12, totalAgtVat);
					insAgtSalePstmt.setDouble(13, tatalAgtTaxableSale);
					insAgtSalePstmt.setDouble(14, totalGoodCauseAmt);
					insAgtSalePstmt.setTimestamp(15, updateLedgerHelper.fetchTransTimeStamp(transDate));
					insAgtSalePstmt.executeUpdate();

					// insert temp table
					insTempTablePstmt.setLong(1, transId);
					insTempTablePstmt.setDouble(2, retCommRate);
					insTempTablePstmt.setDouble(3, agtCommRate);
					insTempTablePstmt.setDouble(4, govtCommRate);
					insTempTablePstmt.setInt(5, retOrgId);
					insTempTablePstmt.setInt(6, gameId);
					insTempTablePstmt.setInt(7, gameTypeId);
					insTempTablePstmt.setString(8, transDate);
					insTempTablePstmt.addBatch();
				
					if (++count % batchSize == 0) {
						insTempTablePstmt.executeBatch();
					}
				}
			}
				
				insTempTablePstmt.executeBatch();
				//logger.info("Transactions from SALE in updateSLESaleLedgerForRet() are updated successfully");
				
				// BE CARE FULL FOR THE GAME_TYPE_ID   
				//tempStmt.executeUpdate("update st_sle_ret_sale sale inner join (select yy.transaction_id,xx.agent_ref_transaction_id from orgTransSummary xx inner join (select transaction_id,transaction_date,retailer_org_id,ret_comm,agt_comm,govt_comm,game_id from st_lms_retailer_transaction_master a inner join st_temp_update_ledger b on a.transaction_id = b.trans_id)yy on DATE(trans_date)=DATE(transaction_date) and xx.retailer_org_id=yy.retailer_org_id and xx.ret_comm=yy.ret_comm and xx.agt_comm=yy.agt_comm and xx.govt_comm=yy.govt_comm and xx.game_id=yy.game_id) tlb on sale.transaction_id=tlb.transaction_id set sale.claim_status='DONE_CLAIM',sale.agent_ref_transaction_id=tlb.agent_ref_transaction_id");
				//logger.debug(tempStmt.executeUpdate("update st_sle_ret_sale sale inner join (select yy.transaction_id,xx.agent_ref_transaction_id from orgTransSummary xx inner join (select transaction_id,transaction_date,retailer_org_id,ret_comm,agt_comm,govt_comm,game_id from st_lms_retailer_transaction_master a inner join st_temp_update_ledger b on a.transaction_id = b.trans_id)yy on DATE(trans_date)=DATE(transaction_date) and xx.retailer_org_id=yy.retailer_org_id and xx.ret_comm=yy.ret_comm and xx.agt_comm=yy.agt_comm and xx.govt_comm=yy.govt_comm and xx.game_type_id=yy.game_id ) tlb on sale.transaction_id=tlb.transaction_id set sale.claim_status='DONE_CLAIM',sale.agent_ref_transaction_id=tlb.agent_ref_transaction_id"));
				tempStmt.executeUpdate("update st_sle_ret_sale sale inner join (select yy.transaction_id,xx.agent_ref_transaction_id from orgTransSummary xx inner join (select a.transaction_id,a.transaction_date,a.retailer_org_id,ret_comm,agt_comm,govt_comm,a.game_id,game_type_id from st_lms_retailer_transaction_master a inner join st_temp_update_ledger b inner join st_sle_ret_sale c on a.transaction_id = b.trans_id and b.trans_id = c.transaction_id) yy on DATE(trans_date)=DATE(transaction_date) and xx.retailer_org_id=yy.retailer_org_id and xx.ret_comm=yy.ret_comm and xx.agt_comm=yy.agt_comm and xx.govt_comm=yy.govt_comm and xx.game_type_id=yy.game_type_id ) tlb on sale.transaction_id=tlb.transaction_id set sale.claim_status='DONE_CLAIM',sale.agent_ref_transaction_id=tlb.agent_ref_transaction_id");
				
				tempStmt.execute("delete from  orgTransSummary ");

				String saleReturnQry = "select a.game_type_id,a.game_id,retailer_org_id,agtUserId,agtOrgId, totalMrp ,totalRetComm, totalagtComm,  totalgovtComm,  totalVat, totalTaxSale, totalCancelCharge,transaction_date,ret_comm,agt_comm,govt_comm,transaction_type,prize_payout_ratio,vat_amt from (select a.game_type_id,a.game_id,a.retailer_org_id,agtUserId,agtOrgId,sum(mrp_amt) totalMrp ,sum(retailer_comm_amt) totalRetComm,sum(agent_comm_amt) totalagtComm, sum(good_cause_amt) totalgovtComm, sum(vat_amt) totalVat,sum(taxable_sale) totalTaxSale,sum(cancellation_charges) totalCancelCharge,DATE(a.transaction_date) 'transaction_date',ret_comm,agt_comm,govt_comm,transaction_type from st_sle_ret_sale_refund a inner join  st_lms_retailer_transaction_master b inner join st_temp_update_ledger c inner join (select um.user_id retUserId,um.organization_id retOrgId,um.parent_user_id agtUserId,om.parent_id agtOrgId from st_lms_organization_master om inner join st_lms_user_master um on om.organization_id=um.organization_id where om.organization_type='RETAILER') d on a.transaction_id = b.transaction_id and a.transaction_id=trans_id and a.retailer_org_id=retOrgId  where claim_status='CLAIM_BAL' group by a.retailer_org_id,DATE(transaction_date),a.game_type_id,transaction_type,ret_comm,agt_comm,govt_comm  order by retailer_org_id,game_type_id,transaction_date) a inner join st_sle_game_type_master sle on sle.game_type_id  =  a.game_type_id"; 
				stmt = con.createStatement();
				rs = stmt.executeQuery(saleReturnQry);
				while (rs.next()) {
					int retOrgId = rs.getInt("retailer_org_id");
					int agtUserId = rs.getInt("agtUserId");
					int agtOrgId = rs.getInt("agtOrgId");
					int gameId  = 	rs.getInt("game_id");
					int gameTypeId  = 	rs.getInt("game_type_id");
					double totalMrpAmt = rs.getDouble("totalMrp");
					double totalRetComm = rs.getDouble("totalRetComm");
					double totalAgtComm = rs.getDouble("totalagtComm");
					double totalGoodCauseAmt = rs.getDouble("totalgovtComm");
					double totalCancelCharge = rs.getDouble("totalCancelCharge");
					double retCommRate = rs.getDouble("ret_comm");
					double agtCommRate = rs.getDouble("agt_comm");
					double govtCommRate = rs.getDouble("govt_comm");
					String transDate = rs.getString("transaction_date");
					double totalretNetAmt = totalMrpAmt - totalRetComm	- totalCancelCharge;
					double totalAgtNetAmt = totalMrpAmt - totalAgtComm	- totalCancelCharge;
					String tranType = rs.getString("transaction_type");
					double totalAgtVat =rs.getDouble("totalVat");
					int ppr = rs.getInt("prize_payout_ratio");
					double vatAmt = rs.getDouble("vat_amt");
					double tatalAgtTaxableSale = CommonMethods.calTaxableSale(totalMrpAmt, retCommRate, ppr, govtCommRate, vatAmt);

					if (orgIdAmountMap.containsKey(retOrgId)) {
						retDebitAmount = orgIdAmountMap.get(retOrgId) - totalretNetAmt;
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
						//logger.debug("--Retailer org id"+retOrgId+"--Agent org Id--"+agtOrgId+"--Start Sale Refund tran for Game type "+gameTypeId +" with transaction "+ transId);
						if (!transIdInvRetMap.containsKey(transDate)) {
							transIdInvRetMap.put(transDate ,	new ArrayList<Long>());
						}

						transIdInvRetMap.get(transDate).add(transId);

						pstmtAgtTrans.setLong(1, transId);
						pstmtAgtTrans.setInt(2, agtUserId);
						pstmtAgtTrans.setInt(3, agtOrgId);
						pstmtAgtTrans.setString(4, "RETAILER");
						pstmtAgtTrans.setInt(5, retOrgId);
						pstmtAgtTrans.setString(6, tranType);
						pstmtAgtTrans.setTimestamp(7,updateLedgerHelper.fetchTransTimeStamp(transDate));
						pstmtAgtTrans.executeUpdate();

						insAgtSaleRetPstmt.setLong(1, transId);
						insAgtSaleRetPstmt.setInt(2, agtOrgId);
						insAgtSaleRetPstmt.setInt(3, retOrgId);
						insAgtSaleRetPstmt.setInt(4, gameId);
						insAgtSaleRetPstmt.setInt(5, gameTypeId);
						// ADD GAME TYPE ID FOR AGENT
						insAgtSaleRetPstmt.setDouble(6, totalMrpAmt);
						insAgtSaleRetPstmt.setDouble(7, totalRetComm);
						insAgtSaleRetPstmt.setDouble(8, totalretNetAmt);
						insAgtSaleRetPstmt.setDouble(9, totalAgtComm);
						insAgtSaleRetPstmt.setDouble(10, totalAgtNetAmt);
						insAgtSaleRetPstmt.setString(11, "CLAIM_BAL");
						insAgtSaleRetPstmt.setDouble(12, totalAgtVat);
						insAgtSaleRetPstmt.setDouble(13, tatalAgtTaxableSale);
						insAgtSaleRetPstmt.setDouble(14, totalGoodCauseAmt);
						insAgtSaleRetPstmt.setDouble(15, totalCancelCharge);
						insAgtSaleRetPstmt.setTimestamp(16, updateLedgerHelper.fetchTransTimeStamp(transDate));
						insAgtSaleRetPstmt.executeUpdate();

						
						//insert temp table
						insTempTablePstmt.setLong(1, transId);
						insTempTablePstmt.setDouble(2, retCommRate);
						insTempTablePstmt.setDouble(3, agtCommRate);
						insTempTablePstmt.setDouble(4, govtCommRate);
						insTempTablePstmt.setInt(5, retOrgId);
						insTempTablePstmt.setInt(6, gameId);
						insTempTablePstmt.setInt(7, gameTypeId);
						insTempTablePstmt.setString(8, transDate);
						insTempTablePstmt.addBatch();
						if(++count % batchSize == 0) {
							insTempTablePstmt.executeBatch();
					    }
					}
				}
				
				insTempTablePstmt.executeBatch();
				// BE CARE FULL FOR THE GAME_TYPE_ID   
				//tempStmt.executeUpdate("update st_dg_ret_sale_refund sale inner join (select yy.transaction_id,xx.agent_ref_transaction_id from orgTransSummary xx inner join (select transaction_id,transaction_date,retailer_org_id,ret_comm,agt_comm,govt_comm,game_id from st_lms_retailer_transaction_master a inner join st_temp_update_ledger b on a.transaction_id = b.trans_id)yy on DATE(trans_date)=DATE(transaction_date) and xx.retailer_org_id=yy.retailer_org_id and xx.ret_comm=yy.ret_comm and xx.agt_comm=yy.agt_comm and xx.govt_comm=yy.govt_comm and xx.game_id=yy.game_id) tlb on sale.transaction_id=tlb.transaction_id set sale.claim_status='DONE_CLAIM',sale.agent_ref_transaction_id=tlb.agent_ref_transaction_id");
				//tempStmt.executeUpdate("update st_sle_ret_sale_refund sale inner join (select yy.transaction_id,xx.agent_ref_transaction_id from orgTransSummary xx inner join (select transaction_id,transaction_date,retailer_org_id,ret_comm,agt_comm,govt_comm,game_id from st_lms_retailer_transaction_master a inner join st_temp_update_ledger b on a.transaction_id = b.trans_id)yy on DATE(trans_date)=DATE(transaction_date) and xx.retailer_org_id=yy.retailer_org_id and xx.ret_comm=yy.ret_comm and xx.agt_comm=yy.agt_comm and xx.govt_comm=yy.govt_comm and xx.game_type_id=yy.game_id) tlb on sale.transaction_id=tlb.transaction_id set sale.claim_status='DONE_CLAIM',sale.agent_ref_transaction_id=tlb.agent_ref_transaction_id");
				tempStmt.executeUpdate("update st_sle_ret_sale_refund sale inner join (select yy.transaction_id,xx.agent_ref_transaction_id from orgTransSummary xx inner join (select a.transaction_id,a.transaction_date,a.retailer_org_id,ret_comm,agt_comm,govt_comm,a.game_id,game_type_id from st_lms_retailer_transaction_master a inner join st_temp_update_ledger b inner join st_sle_ret_sale_refund c on a.transaction_id = b.trans_id and b.trans_id = c.transaction_id )yy on DATE(trans_date)=DATE(transaction_date) and xx.retailer_org_id=yy.retailer_org_id and xx.ret_comm=yy.ret_comm and xx.agt_comm=yy.agt_comm and xx.govt_comm=yy.govt_comm and xx.game_type_id=yy.game_type_id ) tlb on sale.transaction_id=tlb.transaction_id set sale.claim_status='DONE_CLAIM',sale.agent_ref_transaction_id=tlb.agent_ref_transaction_id");
				tempStmt.execute("delete from orgTransSummary");
				
				updateLedgerHelper helper = new updateLedgerHelper();
				helper.generateDGReceiptNew(orgIdTransIdInvMap, orgIdParentIdMap,"SLE_INVOICE", "SLE_RECEIPT", con);
				helper.generateDGReceiptNew(orgIdTransIdInvRetMap, orgIdParentIdMap,"CR_NOTE", "CR_NOTE_CASH", con);

				//logger.debug("retailer orgIdAmountMap\n" + orgIdAmountMap);

				// update retailer balance
				Set<Integer> retOrgIdSet = orgIdAmountMap.keySet();
				for (Integer orgId : retOrgIdSet) {
					boolean isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(CommonMethods.fmtToTwoDecimal(orgIdAmountMap.get(orgId)), "TRANSACTION", "SLE_SALE", orgId,
							orgIdParentIdMap.get(orgId), "RETAILER", 0, con);
					if(!isValid)
						throw new LMSException();
						
					OrgCreditUpdation.updateOrganizationBalWithValidate(CommonMethods.fmtToTwoDecimal(orgIdAmountMap.get(orgId)), "CLAIM_BAL", "DEBIT", orgId,
							orgIdParentIdMap.get(orgId), "RETAILER", 0, con);
					
				}
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("error in sale retailer");
		}
		//logger.debug(new Date() + "---end-dg sale update for retailer--"+ new Date().getTime());
	
		/*
		//logger.debug(new Date() + "---start-dg sale update for retailer--"+ new Date().getTime());
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
			String getGameNbrFromGameMaster = "select game_id,game_type_id,prize_payout_ratio,vat_amt from st_sle_game_type_master";
			String saleQry = "select a.game_type_id,b.game_id , a.retailer_org_id,agtUserId,agtOrgId,sum(mrp_amt) totalMrp ,sum(retailer_comm_amt) totalRetComm,sum(agent_comm_amt) totalagtComm, sum(good_cause_amt) totalgovtComm, sum(vat_amt) totalVat,sum(taxable_sale) totalTaxSale,DATE(a.transaction_date) 'transaction_date',ret_comm,agt_comm,govt_comm from st_sle_ret_sale a inner join st_lms_retailer_transaction_master b inner join st_temp_update_ledger c inner join (select um.user_id retUserId,um.organization_id retOrgId,um.parent_user_id agtUserId,om.parent_id agtOrgId from st_lms_organization_master om inner join st_lms_user_master um on om.organization_id=um.organization_id where om.organization_type='RETAILER' ) d on a.transaction_id = b.transaction_id and a.transaction_id=trans_id and a.retailer_org_id=retOrgId  where claim_status='CLAIM_BAL' group by a.retailer_org_id,DATE(transaction_date),a.game_type_id,ret_comm,agt_comm,govt_comm  order by retailer_org_id,game_type_id,transaction_date";
			String saleReturnQry = "select a.game_type_id,a.game_id,a.retailer_org_id,agtUserId,agtOrgId,sum(mrp_amt) totalMrp ,sum(retailer_comm_amt) totalRetComm,sum(agent_comm_amt) totalagtComm, sum(good_cause_amt) totalgovtComm, sum(vat_amt) totalVat,sum(taxable_sale) totalTaxSale,sum(cancellation_charges) totalCancelCharge,DATE(a.transaction_date) 'transaction_date',ret_comm,agt_comm,govt_comm,transaction_type from st_sle_ret_sale_refund a inner join  st_lms_retailer_transaction_master b inner join st_temp_update_ledger c inner join (select um.user_id retUserId,um.organization_id retOrgId,um.parent_user_id agtUserId,om.parent_id agtOrgId from st_lms_organization_master om inner join st_lms_user_master um on om.organization_id=um.organization_id where om.organization_type='RETAILER') d on a.transaction_id = b.transaction_id and a.transaction_id=trans_id and a.retailer_org_id=retOrgId  where claim_status='CLAIM_BAL' group by a.retailer_org_id,DATE(transaction_date),a.game_type_id,transaction_type,ret_comm,agt_comm,govt_comm  order by retailer_org_id,game_type_id,transaction_date";

			
			PreparedStatement pstmtGameNbr = con.prepareStatement(getGameNbrFromGameMaster);
			ResultSet resultGameNbr = pstmtGameNbr.executeQuery();

			PreparedStatement pstmtAgt = con.prepareStatement(QueryManager.insertInLMSTransactionMaster());
			PreparedStatement pstmtAgtTrans = con.prepareStatement(QueryManager.insertInAgentTransactionMaster());
			PreparedStatement insAgtSalePstmt = con.prepareStatement("insert into st_sle_agt_sale(transaction_id,agent_org_id,retailer_org_id,game_id,game_type_id,mrp_amt,retailer_comm,net_amt,agent_comm,bo_net_amt,claim_status,vat_amt,taxable_sale,govt_comm) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			PreparedStatement insTempTablePstmt=con.prepareStatement("insert into orgTransSummary(agent_ref_transaction_id,ret_comm,agt_comm,govt_comm,retailer_org_id,game_id,trans_date) values(?,?,?,?,?,?,?)");
			PreparedStatement insAgtSaleRetPstmt = con.prepareStatement("insert into st_dg_agt_sale_refund(transaction_id,agent_org_id,retailer_org_id,game_id,mrp_amt,retailer_comm,net_amt,agent_comm,bo_net_amt,claim_status,vat_amt,taxable_sale,govt_comm,cancellation_charges) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
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
				//logger.debug("--Start Sale tran for game No" + gameId);
				
				while (rs.next()) {
					retOrgId = rs.getInt("retailer_org_id");
					agtUserId = rs.getInt("agtUserId");
					agtOrgId = rs.getInt("agtOrgId");

					totalMrpAmt = rs.getDouble("totalMrp");
					totalRetComm = rs.getDouble("totalRetComm");
					totalAgtComm = rs.getDouble("totalagtComm");
					totalGoodCauseAmt = rs.getDouble("totalgovtComm");
					retCommRate = rs.getDouble("ret_comm");
					agtCommRate = rs.getDouble("agt_comm");
					govtCommRate = rs.getDouble("govt_comm");
					transDate = rs.getString("transaction_date");
					totalretNetAmt = totalMrpAmt - totalRetComm;
					totalAgtNetAmt = totalMrpAmt - totalAgtComm;
					double totalAgtVat= rs.getDouble("totalVat");
					double tatalAgtTaxableSale = CommonMethods.calTaxableSale(totalMrpAmt, retCommRate, ppr,govtCommRate, vatAmt);

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
						//logger.debug("--Retailer org id"+retOrgId+"--Agent org Id--"+agtOrgId+"--Start Sale tran for game No" + gameId);
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
						pstmtAgtTrans.setString(6, "SLE_SALE");
						pstmtAgtTrans.setTimestamp(7,updateLedgerHelper.fetchTransTimeStamp(transDate));
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
					}

				}
				
				insTempTablePstmt.executeBatch();
				
				
				tempStmt.executeUpdate("update st_sle_ret_sale_"+gameId+" sale inner join (select yy.transaction_id,xx.agent_ref_transaction_id from orgTransSummary xx inner join (select transaction_id,transaction_date,retailer_org_id,ret_comm,agt_comm,govt_comm,game_id from st_lms_retailer_transaction_master a inner join st_temp_update_ledger b on a.transaction_id = b.trans_id)yy on DATE(trans_date)=DATE(transaction_date) and xx.retailer_org_id=yy.retailer_org_id and xx.ret_comm=yy.ret_comm and xx.agt_comm=yy.agt_comm and xx.govt_comm=yy.govt_comm and xx.game_id=yy.game_id) tlb on sale.transaction_id=tlb.transaction_id set sale.claim_status='DONE_CLAIM',sale.agent_ref_transaction_id=tlb.agent_ref_transaction_id");
				tempStmt.execute("delete from  orgTransSummary ");
				//tempStmt.execute("truncate table orgTransSummary");
				
				pstmt = con.prepareStatement(saleReturnQry);
				pstmt.setInt(1, gameId);
				rs = pstmt.executeQuery();
				//logger.debug("--Start Cancel tran for game No" + gameId);
				while (rs.next()) {
					retOrgId = rs.getInt("retailer_org_id");
					agtUserId = rs.getInt("agtUserId");
					agtOrgId = rs.getInt("agtOrgId");

					totalMrpAmt = rs.getDouble("totalMrp");
					totalRetComm = rs.getDouble("totalRetComm");
					totalAgtComm = rs.getDouble("totalagtComm");
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
					double totalAgtVat =rs.getDouble("totalVat");

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
						//logger.debug("--Retailer org id"+retOrgId+"--Agent org Id--"+agtOrgId+"--Start Sale Cancel tran for game No" + gameId);
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
								updateLedgerHelper.fetchTransTimeStamp(transDate));
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
						
					}

				}
				
				insTempTablePstmt.executeBatch();
				
				tempStmt.executeUpdate("update st_dg_ret_sale_refund_"+gameId+" sale inner join (select yy.transaction_id,xx.agent_ref_transaction_id from orgTransSummary xx inner join (select transaction_id,transaction_date,retailer_org_id,ret_comm,agt_comm,govt_comm,game_id from st_lms_retailer_transaction_master a inner join st_temp_update_ledger b on a.transaction_id = b.trans_id)yy on DATE(trans_date)=DATE(transaction_date) and xx.retailer_org_id=yy.retailer_org_id and xx.ret_comm=yy.ret_comm and xx.agt_comm=yy.agt_comm and xx.govt_comm=yy.govt_comm and xx.game_id=yy.game_id) tlb on sale.transaction_id=tlb.transaction_id set sale.claim_status='DONE_CLAIM',sale.agent_ref_transaction_id=tlb.agent_ref_transaction_id");
				tempStmt.execute("delete from orgTransSummary");
				
			}
			updateLedgerHelper helper = new updateLedgerHelper();
			helper.generateDGReceiptNew(orgIdTransIdInvMap, orgIdParentIdMap,"SLE_INVOICE", "SLE_RECEIPT", con);
			helper.generateDGReceiptNew(orgIdTransIdInvRetMap, orgIdParentIdMap,"SLE_CR_NOTE", "SLE_CR_NOTE_CASH", con);

			//logger.debug("retailer orgIdAmountMap\n" + orgIdAmountMap);

			// update retailer balance
			Set<Integer> retOrgIdSet = orgIdAmountMap.keySet();
			for (Integer orgId : retOrgIdSet) {
				boolean isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(CommonMethods.fmtToTwoDecimal(orgIdAmountMap.get(orgId)), "TRANSACTION", "DRAW_GAME_SALE", orgId,
						orgIdParentIdMap.get(orgId), "RETAILER", 0, con);
				if(!isValid)
					throw new LMSException();
					
				OrgCreditUpdation.updateOrganizationBalWithValidate(CommonMethods.fmtToTwoDecimal(orgIdAmountMap.get(orgId)), "CLAIM_BAL", "DEBIT", orgId,
						orgIdParentIdMap.get(orgId), "RETAILER", 0, con);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("error in sale retailer");
		}
		//logger.debug(new Date() + "---end-dg sale update for retailer--"
				+ new Date().getTime());
	*/}
	
	private static void fillTempSLETransCommTlbForAgt(Connection con) throws LMSException{
		//logger.debug(new Date() + "---start---" + new Date().getTime());

		try {
			con.prepareStatement("truncate table st_temp_update_ledger").executeUpdate();  
			long transId = 0;
			int gameId = 0;
			int gameTypeId = 0;
			int orgId = 0;
			Timestamp transDate = null;
			double retComm, agtComm, govtComm;
			//String gameQry = "select * from (select game_id,sum(retailer_sale_comm_rate) ret_comm,sum(agent_sale_comm_rate) agt_comm,sum(govt_comm) govt_comm,now() date from (select game_id,retailer_sale_comm_rate,agent_sale_comm_rate,govt_comm from st_dg_game_master where game_id=? union all select game_id,0,sale_comm_variance,0 from st_dg_bo_agent_sale_pwt_comm_variance where agent_org_id=? and game_id=?) aa group by game_id union all select gm.game_id,0,agent_sale_comm_rate+sale_comm_variance agtComm,0,date_changed from st_dg_bo_agent_sale_comm_variance_history his inner join st_dg_game_master gm on his.game_id=gm.game_id where agent_org_id=? and gm.game_id=? union all select game_id,0,0,govt_comm_rate,date_changed from st_dg_game_govt_comm_history where game_id=?) aa order by date desc";
			String gameQry = "select * from (select game_id,game_type_id,sum(retailer_sale_comm_rate) ret_comm,sum(agent_sale_comm_rate) agt_comm,sum(gov_comm_rate) govt_comm,now() date from (select game_id,game_type_id,retailer_sale_comm_rate,agent_sale_comm_rate,gov_comm_rate from st_sle_game_type_master where game_type_id=? and game_id = ? union all select game_id,game_type_id,0,sale_comm_variance,0 from st_sle_bo_agent_sale_pwt_comm_variance where agent_org_id=? and game_type_id=? and game_id = ?) aa group by game_type_id , game_id union all select gm.game_id,gm.game_type_id,0,agent_sale_comm_rate+sale_comm_variance agtComm,0,date_changed from st_sle_bo_agent_sale_comm_variance_history his inner join st_sle_game_type_master gm on his.game_type_id=gm.game_type_id and his.game_id=gm.game_id where agent_org_id=? and gm.game_type_id=? and gm.game_id=? union all select game_id,game_type_id,0,0,govt_comm_rate,date_changed from st_sle_game_govt_comm_history where game_type_id = ? and game_id=?) aa order by date desc";
			Statement stmt = con.createStatement();

			int count = 0;
			StringBuilder sb = new StringBuilder("insert into st_temp_update_ledger (trans_id, ret_comm, agt_comm, govt_comm) values");
			//String transQry = "select sale.transaction_id,sale.game_id ,transaction_date,sale.agent_org_id,parent_id from st_lms_agent_transaction_master tm inner join st_lms_organization_master om inner join st_dg_agt_sale sale on sale.agent_org_id=om.organization_id and tm.transaction_id=sale.transaction_id  where claim_status in('CLAIM_BAL') union all select sale.transaction_id,sale.game_id ,transaction_date,sale.agent_org_id,parent_id from st_lms_agent_transaction_master tm inner join st_lms_organization_master om inner join st_dg_agt_sale_refund sale on sale.agent_org_id=om.organization_id and tm.transaction_id=sale.transaction_id  where claim_status in('CLAIM_BAL')";
			String transQry = "select sale.transaction_id,sale.game_id ,sale.game_type_id,tm.transaction_date,sale.agent_org_id,parent_id from st_lms_agent_transaction_master tm inner join st_lms_organization_master om inner join st_sle_agt_sale sale on sale.agent_org_id=om.organization_id and tm.transaction_id=sale.transaction_id  where claim_status in('CLAIM_BAL') union all select sale.transaction_id,sale.game_id ,sale.game_type_id,tm.transaction_date,sale.agent_org_id,parent_id from st_lms_agent_transaction_master tm inner join st_lms_organization_master om inner join st_sle_agt_sale_refund sale on sale.agent_org_id=om.organization_id and tm.transaction_id=sale.transaction_id  where claim_status in('CLAIM_BAL')"; 
			PreparedStatement traStmt = con.prepareStatement(transQry);
			ResultSet tranRs = traStmt.executeQuery();
			PreparedStatement gameStmt = con.prepareStatement(gameQry);

			while (tranRs.next()) {
				gameId = tranRs.getInt("game_id");
				orgId = tranRs.getInt("agent_org_id");
				gameTypeId = tranRs.getInt("game_type_id");
				transId = tranRs.getLong("transaction_id");
				transDate = tranRs.getTimestamp("transaction_date");

				gameStmt.setInt(1, gameTypeId);
				gameStmt.setInt(2, gameId);
				gameStmt.setInt(3, orgId);
				gameStmt.setInt(4, gameTypeId);
				gameStmt.setInt(5, gameId);
				gameStmt.setInt(6, orgId);
				gameStmt.setInt(7, gameTypeId);
				gameStmt.setInt(8, gameId);
				gameStmt.setInt(9, gameTypeId);
				gameStmt.setInt(10, gameId);
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

				sb.append("(" + transId + ", " + retComm + ", " + agtComm + ","	+ govtComm + "),");
				count++;
			}
			if (count > 0) {
				sb.deleteCharAt(sb.length() - 1);
				stmt.executeUpdate(sb.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("error in comm table agent");
		}
	}

	private static void updateSLESaleLedgerForAgt(Connection con) throws LMSException{
		logger.debug(new Date() + "---start-dg sale update for agent--"+ new Date().getTime());
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
			//String saleQry = "select agent_org_id,boUserId,boOrgId,totalMrp,totalRetComm,totalagtComm,totalgovtComm,totalVat,totalTaxSale,tot.game_id,transaction_date,ret_comm,agt_comm,tot.govt_comm,prize_payout_ratio,vat_amt from (select agent_org_id,boUserId,boOrgId,totalMrp,totalRetComm,totalagtComm,totalgovtComm,totalVat,totalTaxSale,game_id,transaction_date,ret_comm,agt_comm,govt_comm from(select a.agent_org_id,sum(mrp_amt) totalMrp ,sum(retailer_comm) totalRetComm,sum(agent_comm) totalagtComm, sum(a.govt_comm) totalgovtComm, sum(vat_amt) totalVat,sum(taxable_sale) totalTaxSale,a.game_id,DATE(transaction_date) 'transaction_date',ret_comm,agt_comm,c.govt_comm from st_dg_agt_sale a inner join  st_lms_agent_transaction_master b inner join st_temp_update_ledger c on a.transaction_id = b.transaction_id and a.transaction_id=trans_id where claim_status='CLAIM_BAL' group by a.agent_org_id,DATE(transaction_date),a.game_id,agt_comm,c.govt_comm  order by agent_org_id,game_id,transaction_date) sale inner join (select um.user_id agtUserId,um.organization_id agtOrgId,um.parent_user_id boUserId,om.parent_id boOrgId,isrolehead from st_lms_organization_master om inner join st_lms_user_master um on om.organization_id=um.organization_id where om.organization_type='AGENT') om on agent_org_id=agtOrgId and isrolehead = 'Y') tot inner join st_dg_game_master gm on tot.game_id=gm.game_id";
			String saleQry = "select agent_org_id,boUserId,boOrgId,totalMrp,totalRetComm,totalagtComm,totalgovtComm,totalVat,totalTaxSale,tot.game_id,tot.game_type_id,transaction_date,ret_comm,agt_comm,tot.govt_comm,prize_payout_ratio,vat_amt from (select agent_org_id,boUserId,boOrgId,totalMrp,totalRetComm,totalagtComm,totalgovtComm,totalVat,totalTaxSale,game_id,game_type_id,transaction_date,ret_comm,agt_comm,govt_comm from (select a.agent_org_id,sum(mrp_amt) totalMrp ,sum(retailer_comm_amt) totalRetComm,sum(agent_comm_amt) totalagtComm, sum(a.govt_comm) totalgovtComm, sum(vat_amt) totalVat,sum(taxable_sale) totalTaxSale,a.game_id,game_type_id,DATE(b.transaction_date) 'transaction_date',ret_comm,agt_comm,c.govt_comm from st_sle_agt_sale a inner join  st_lms_agent_transaction_master b inner join st_temp_update_ledger c on a.transaction_id = b.transaction_id and a.transaction_id=trans_id where claim_status='CLAIM_BAL' group by a.agent_org_id,DATE(b.transaction_date),a.game_type_id,a.game_id,agt_comm,c.govt_comm  order by agent_org_id,game_id,transaction_date) sale inner join (select um.user_id agtUserId,um.organization_id agtOrgId,um.parent_user_id boUserId,om.parent_id boOrgId,isrolehead from st_lms_organization_master om inner join st_lms_user_master um on om.organization_id=um.organization_id where om.organization_type='AGENT') om on agent_org_id=agtOrgId and isrolehead = 'Y')  tot inner join st_sle_game_type_master gm on tot.game_id=gm.game_id and tot.game_type_id=gm.game_type_id";
			
			//String saleReturnQry = "select agent_org_id,boUserId,boOrgId,totalMrp,totalRetComm,totalagtComm,totalgovtComm,totalVat,totalTaxSale,totalCancelCharge,tot.game_id,transaction_date,ret_comm,agt_comm,tot.govt_comm,transaction_type,prize_payout_ratio,vat_amt from(select agent_org_id,boUserId,boOrgId,totalMrp,totalRetComm,totalagtComm,totalgovtComm,totalVat,totalTaxSale,totalCancelCharge,game_id,transaction_date,ret_comm,agt_comm,govt_comm,transaction_type from(select a.agent_org_id,sum(mrp_amt) totalMrp ,sum(retailer_comm) totalRetComm,sum(agent_comm) totalagtComm, sum(a.govt_comm) totalgovtComm, sum(vat_amt) totalVat,sum(taxable_sale) totalTaxSale,sum(cancellation_charges) totalCancelCharge,a.game_id,DATE(transaction_date) 'transaction_date',ret_comm,agt_comm,c.govt_comm,transaction_type from st_dg_agt_sale_refund a inner join  st_lms_agent_transaction_master b inner join st_temp_update_ledger c on a.transaction_id = b.transaction_id and a.transaction_id=trans_id where claim_status='CLAIM_BAL' group by a.agent_org_id,DATE(transaction_date),a.game_id,transaction_type,agt_comm,c.govt_comm  order by agent_org_id,game_id,transaction_date) saleRefund inner join (select um.user_id agtUserId,um.organization_id agtOrgId,um.parent_user_id boUserId,om.parent_id boOrgId,isrolehead from st_lms_organization_master om inner join st_lms_user_master um on om.organization_id=um.organization_id where om.organization_type='AGENT') d on agent_org_id=agtOrgId and isrolehead = 'Y') tot inner join st_dg_game_master gm on tot.game_id=gm.game_id";
			String saleReturnQry = "select agent_org_id,boUserId,boOrgId,totalMrp,totalRetComm,totalagtComm,totalgovtComm,totalVat,totalTaxSale,totalCancelCharge,tot.game_id,tot.game_type_id,transaction_date,ret_comm,agt_comm,tot.govt_comm,transaction_type,prize_payout_ratio,vat_amt from(select agent_org_id,boUserId,boOrgId,totalMrp,totalRetComm,totalagtComm,totalgovtComm,totalVat,totalTaxSale,totalCancelCharge,game_id,game_type_id,transaction_date,ret_comm,agt_comm,govt_comm,transaction_type from(select a.agent_org_id,sum(mrp_amt) totalMrp ,sum(retailer_comm_amt) totalRetComm,sum(agent_comm_amt) totalagtComm, sum(a.govt_comm) totalgovtComm, sum(vat_amt) totalVat,sum(taxable_sale) totalTaxSale,sum(cancellation_charges) totalCancelCharge,a.game_id,a.game_type_id,DATE(b.transaction_date) 'transaction_date',ret_comm,agt_comm,c.govt_comm,transaction_type from st_sle_agt_sale_refund a inner join  st_lms_agent_transaction_master b inner join st_temp_update_ledger c on a.transaction_id = b.transaction_id and a.transaction_id=trans_id where claim_status='CLAIM_BAL' group by a.agent_org_id,DATE(b.transaction_date),a.game_type_id,a.game_id,transaction_type,agt_comm,c.govt_comm  order by agent_org_id,game_id,game_type_id,transaction_date) saleRefund inner join (select um.user_id agtUserId,um.organization_id agtOrgId,um.parent_user_id boUserId,om.parent_id boOrgId,isrolehead from st_lms_organization_master om inner join st_lms_user_master um on om.organization_id=um.organization_id where om.organization_type='AGENT') d on agent_org_id=agtOrgId and isrolehead = 'Y') tot inner join st_sle_game_type_master gm on tot.game_type_id=gm.game_type_id and tot.game_id=gm.game_id";

			PreparedStatement pstmtBO = con.prepareStatement(QueryManager.insertInLMSTransactionMaster());
			PreparedStatement insBoTranPstmt = con.prepareStatement(QueryManager.insertInBOTransactionMaster());
			PreparedStatement insBoSalePstmt = con.prepareStatement("insert into st_sle_bo_sale(transaction_id,agent_org_id,game_id,game_type_id,mrp_amt,agent_comm,net_amt,vat_amt,taxable_sale,govt_comm,transaction_date) values(?,?,?,?,?,?,?,?,?,?,?)");
			
			//PreparedStatement updAgtSalepstmt = con.prepareStatement("update st_dg_agt_sale a inner join (select transaction_date,transaction_id,agt_comm,govt_comm from st_lms_agent_transaction_master a inner join st_temp_update_ledger b on a.transaction_id = b.trans_id) b on a.transaction_id = b.transaction_id set claim_status='DONE_CLAIM',bo_ref_transaction_id=? where a.agent_org_id=? and b.agt_comm=? and b.govt_comm=? and DATE(transaction_date)=? and game_id=?");
			PreparedStatement insTempTablePstmt=con.prepareStatement("insert into agentTransSummary(bo_ref_transaction_id,agt_comm,govt_comm,agent_org_id,game_id,game_type_id,trans_date) values(?,?,?,?,?,?,?)");

			PreparedStatement insBoSaleRetpstmt = con.prepareStatement("insert into st_sle_bo_sale_refund(transaction_id,agent_org_id,game_id,game_type_id,mrp_amt,agent_comm,net_amt,vat_amt,taxable_sale,govt_comm,cancellation_charges,transaction_date) values(?,?,?,?,?,?,?,?,?,?,?,?)");
			//PreparedStatement updAgtSaleRetPstmt = con.prepareStatement("update st_dg_agt_sale_refund a inner join (select transaction_date,transaction_type,transaction_id,agt_comm,govt_comm from st_lms_agent_transaction_master a inner join st_temp_update_ledger b on a.transaction_id = b.trans_id) b on a.transaction_id = b.transaction_id set claim_status='DONE_CLAIM',bo_ref_transaction_id=? where a.agent_org_id=? and b.agt_comm=? and b.govt_comm=? and DATE(transaction_date)=? and transaction_type=? and game_id=?");

			int gameId = 0;
			int gameTypeId = 0;
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
				gameTypeId = rs.getInt("game_type_id");
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
				double tatalAgtTaxableSale = CommonMethods.calTaxableSale(totalMrpAmt, agtCommRate, ppr, govtCommRate, vatAmt);

				if (orgIdAmountMap.containsKey(agtOrgId)) {
					agtDebitAmount = orgIdAmountMap.get(agtOrgId) + totalAgtNetAmt;
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
					insBoTranPstmt.setTimestamp(6,updateLedgerHelper.fetchTransTimeStamp(transDate));
					insBoTranPstmt.setString(7, "SLE_SALE");
					insBoTranPstmt.executeUpdate();

					insBoSalePstmt.setLong(1, transId);
					insBoSalePstmt.setInt(2, agtOrgId);
					insBoSalePstmt.setInt(3, gameId);
					insBoSalePstmt.setInt(4, gameTypeId);
					insBoSalePstmt.setDouble(5, totalMrpAmt);
					insBoSalePstmt.setDouble(6, totalAgtComm);
					insBoSalePstmt.setDouble(7, totalAgtNetAmt);
					insBoSalePstmt.setDouble(8, totalAgtVat);
					insBoSalePstmt.setDouble(9, tatalAgtTaxableSale);
					insBoSalePstmt.setDouble(10, totalGoodCauseAmt);
					insBoSalePstmt.setTimestamp(11,updateLedgerHelper.fetchTransTimeStamp(transDate));

					insBoSalePstmt.executeUpdate();

					
					//insert temp table
					insTempTablePstmt.setLong(1, transId);
					insTempTablePstmt.setDouble(2, agtCommRate);
					insTempTablePstmt.setDouble(3, govtCommRate);
					insTempTablePstmt.setInt(4, agtOrgId);
					insTempTablePstmt.setInt(5, gameId);
					insTempTablePstmt.setInt(6, gameTypeId);
					insTempTablePstmt.setString(7, transDate);
					insTempTablePstmt.addBatch();
					if(++count % batchSize == 0) {
						insTempTablePstmt.executeBatch();
				    }
				}
			}

			insTempTablePstmt.executeBatch();

			// CHECK FOR THE GAME TYPE ID 
			//tempStmt.executeUpdate("update st_sle_agt_sale  sale inner join (select yy.transaction_id,xx.bo_ref_transaction_id,game_id from agentTransSummary xx inner join (select transaction_id,transaction_date,user_org_id agent_org_id,agt_comm,govt_comm from st_lms_agent_transaction_master a inner join st_temp_update_ledger b on a.transaction_id = b.trans_id)yy on DATE(trans_date)=DATE(transaction_date) and xx.agent_org_id=yy.agent_org_id and  xx.agt_comm=yy.agt_comm and xx.govt_comm=yy.govt_comm ) tlb on sale.transaction_id=tlb.transaction_id and  sale.game_id=tlb.game_id set sale.claim_status='DONE_CLAIM',sale.bo_ref_transaction_id=tlb.bo_ref_transaction_id");
			tempStmt.executeUpdate("update st_sle_agt_sale  sale inner join (select yy.transaction_id,xx.bo_ref_transaction_id,game_id,game_type_id from agentTransSummary xx inner join (select transaction_id,transaction_date,user_org_id agent_org_id,agt_comm,govt_comm from st_lms_agent_transaction_master a inner join st_temp_update_ledger b on a.transaction_id = b.trans_id)yy on DATE(trans_date)=DATE(transaction_date) and xx.agent_org_id=yy.agent_org_id and  xx.agt_comm=yy.agt_comm and xx.govt_comm=yy.govt_comm ) tlb on sale.transaction_id=tlb.transaction_id and sale.game_type_id = tlb.game_type_id and sale.game_id=tlb.game_id set sale.claim_status='DONE_CLAIM',sale.bo_ref_transaction_id=tlb.bo_ref_transaction_id");
			tempStmt.execute("delete from  agentTransSummary ");
		
			
			pstmt = con.prepareStatement(saleReturnQry);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				agtOrgId = rs.getInt("agent_org_id");
				boUserId = rs.getInt("boUserId");
				boOrgId = rs.getInt("boOrgId");
				gameId = rs.getInt("game_id");
				gameTypeId = rs.getInt("game_type_id");
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
					insBoTranPstmt.setTimestamp(6,updateLedgerHelper.fetchTransTimeStamp(transDate));
					insBoTranPstmt.setString(7, tranType);
					insBoTranPstmt.executeUpdate();

					insBoSaleRetpstmt.setLong(1, transId);
					insBoSaleRetpstmt.setInt(2, agtOrgId);
					insBoSaleRetpstmt.setInt(3, gameId);
					insBoSaleRetpstmt.setInt(4, gameTypeId);
					insBoSaleRetpstmt.setDouble(5, totalMrpAmt);
					insBoSaleRetpstmt.setDouble(6, totalAgtComm);
					insBoSaleRetpstmt.setDouble(7, totalAgtNetAmt);
					insBoSaleRetpstmt.setDouble(8, totalAgtVat);
					insBoSaleRetpstmt.setDouble(9, tatalAgtTaxableSale);
					insBoSaleRetpstmt.setDouble(10, totalGoodCauseAmt);
					insBoSaleRetpstmt.setDouble(11, totalCancelCharge);
					insBoSaleRetpstmt.setTimestamp(12,updateLedgerHelper.fetchTransTimeStamp(transDate));
					insBoSaleRetpstmt.executeUpdate();

					//insert temp table
					insTempTablePstmt.setLong(1, transId);
					insTempTablePstmt.setDouble(2, agtCommRate);
					insTempTablePstmt.setDouble(3, govtCommRate);
					insTempTablePstmt.setInt(4, agtOrgId);
					insTempTablePstmt.setInt(5, gameId);
					insTempTablePstmt.setInt(6, gameTypeId);
					insTempTablePstmt.setString(7, transDate);
					insTempTablePstmt.addBatch();
					if(++count % batchSize == 0) {
						insTempTablePstmt.executeBatch();
				    }
				}

			}
			insTempTablePstmt.executeBatch();
			
			// CHECK FOR THE GAME TYPE ID 
			//tempStmt.executeUpdate("update st_dg_sle_sale_refund  sale inner join (select yy.transaction_id,xx.bo_ref_transaction_id,game_id from agentTransSummary xx inner join (select transaction_id,transaction_date,user_org_id agent_org_id,agt_comm,govt_comm from st_lms_agent_transaction_master a inner join st_temp_update_ledger b on a.transaction_id = b.trans_id)yy on DATE(trans_date)=DATE(transaction_date) and xx.agent_org_id=yy.agent_org_id and  xx.agt_comm=yy.agt_comm and xx.govt_comm=yy.govt_comm ) tlb on sale.transaction_id=tlb.transaction_id and  sale.game_id=tlb.game_id set sale.claim_status='DONE_CLAIM',sale.bo_ref_transaction_id=tlb.bo_ref_transaction_id");
			tempStmt.executeUpdate("update st_sle_agt_sale_refund  sale inner join (select yy.transaction_id,xx.bo_ref_transaction_id,game_id,game_type_id from agentTransSummary xx inner join (select transaction_id,transaction_date,user_org_id agent_org_id,agt_comm,govt_comm from st_lms_agent_transaction_master a inner join st_temp_update_ledger b on a.transaction_id = b.trans_id)yy on DATE(trans_date)=DATE(transaction_date) and xx.agent_org_id=yy.agent_org_id and  xx.agt_comm=yy.agt_comm and xx.govt_comm=yy.govt_comm ) tlb on sale.transaction_id=tlb.transaction_id and  sale.game_type_id = tlb.game_type_id and  sale.game_id=tlb.game_id set sale.claim_status='DONE_CLAIM',sale.bo_ref_transaction_id=tlb.bo_ref_transaction_id");
			tempStmt.execute("delete from  agentTransSummary ");
		
			updateLedgerHelper helper = new updateLedgerHelper();
			helper.generateDGReceiptForBONew(orgIdTransIdInvMap, orgIdParentIdMap,"SLE_INVOICE", "SLE_RECEIPT", con);
			helper.generateDGReceiptForBONew(orgIdTransIdInvRetMap, orgIdParentIdMap, "CR_NOTE", "CR_NOTE_CASH", con);

			logger.debug("agent orgIdAmountMap\n" + orgIdAmountMap);
			// update agent balance
			Set<Integer> agtOrgIdSet = orgIdAmountMap.keySet();
			for (Integer orgId : agtOrgIdSet) {
				boolean isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(CommonMethods.fmtToTwoDecimal(orgIdAmountMap.get(orgId)), "TRANSACTION", "SLE_SALE", orgId,
						0, "AGENT", 0, con);
				if(!isValid)
					throw new LMSException();
				OrgCreditUpdation.updateOrganizationBalWithValidate(CommonMethods.fmtToTwoDecimal(orgIdAmountMap.get(orgId)), "CLAIM_BAL", "DEBIT", orgId,
						0, "AGENT", 0, con);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("error in sale agent");
		}
		logger.debug(new Date() + "---end-dg sale update for agent--"+ new Date().getTime());
	}

	private static void updateSLEPwtLedgerForRet(Connection connection) throws LMSException {
		logger.debug("--- Start SLE PWT Update for Retailer --- "+Util.getCurrentTimeString());

		Statement gameStmt = null;
		PreparedStatement pwtPstmt = null;
		PreparedStatement retPwtUpdatePstmt = null;
		PreparedStatement transMasQueryPstmt = null;
		PreparedStatement agtTransMasterPstmt = null;
		PreparedStatement agtPstmt = null;
		ResultSet rs = null;
		ResultSet gameRs = null;
		ResultSet tranRs = null;

		try {
			//String gameQry = "SELECT game_id, game_type_id FROM st_sle_game_type_master;";
			//gameStmt = connection.createStatement();

			transMasQueryPstmt = connection.prepareStatement(QueryManager.insertInLMSTransactionMaster());
			agtTransMasterPstmt = connection.prepareStatement(QueryManager.insertInAgentTransactionMaster());

//			pwtPstmt = connection.prepareStatement("SELECT aaa.game_id, aaa.game_type_id, retailer_org_id, agtUserId, agtOrgId, aaa.draw_id, SUM(aaa.agt_claim_comm) totalAgtComm, SUM(aaa.pwt_amt) totalPwtAmt, aaa.pwt_type, SUM(aaa.retailer_claim_comm) totalRetComm, DATE(aaa.transaction_date) transaction_date FROM (SELECT b.ticket_nbr, a.retailer_org_id, a.pwt_amt, 'Anonymous' AS pwt_type, a.game_id, a.game_type_id, b.draw_id, a.retailer_claim_comm, a.agt_claim_comm, 'Anonymous' AS NAME, a.transaction_date FROM st_sle_ret_pwt a, st_sle_pwt_inv b, st_lms_retailer_transaction_master c WHERE a.pwt_claim_status = 'CLAIM_BAL' AND a.transaction_id = b.retailer_transaction_id AND a.transaction_id=c.transaction_id)aaa, st_sle_game_master bbb, (SELECT um.user_id retUserId, um.organization_id retOrgId, um.parent_user_id agtUserId, om.parent_id agtOrgId FROM st_lms_organization_master om INNER JOIN st_lms_user_master um ON om.organization_id=um.organization_id WHERE om.organization_type='RETAILER') ccc WHERE aaa.game_id=bbb.game_id AND retailer_org_id=retOrgId GROUP BY pwt_type, retailer_org_id, aaa.game_id, aaa.game_type_id, aaa.draw_id, DATE(aaa.transaction_date);");
			pwtPstmt = connection.prepareStatement("SELECT aaa.game_id, aaa.game_type_id, retailer_org_id, agtUserId, agtOrgId, aaa.draw_id, SUM(aaa.agt_claim_comm) totalAgtComm, SUM(aaa.pwt_amt) totalPwtAmt, sum(aaa.govt_claim_comm) totalGovAmt, aaa.pwt_type, SUM(aaa.retailer_claim_comm) totalRetComm, DATE(aaa.transaction_date) transaction_date FROM (SELECT b.ticket_nbr, a.retailer_org_id, a.pwt_amt, a.govt_claim_comm, 'Anonymous' AS pwt_type, a.game_id, a.game_type_id, b.draw_id, a.retailer_claim_comm, a.agt_claim_comm, 'Anonymous' AS NAME, a.transaction_date FROM st_sle_ret_pwt a, st_sle_pwt_inv b, st_lms_retailer_transaction_master c WHERE a.pwt_claim_status = 'CLAIM_BAL' AND a.transaction_id = b.retailer_transaction_id AND a.transaction_id=c.transaction_id)aaa, st_sle_game_master bbb, (SELECT um.user_id retUserId, um.organization_id retOrgId, um.parent_user_id agtUserId, om.parent_id agtOrgId FROM st_lms_organization_master om INNER JOIN st_lms_user_master um ON om.organization_id=um.organization_id WHERE om.organization_type='RETAILER') ccc WHERE aaa.game_id=bbb.game_id AND retailer_org_id=retOrgId GROUP BY pwt_type, retailer_org_id, aaa.game_id, aaa.game_type_id, aaa.draw_id, DATE(aaa.transaction_date);");
			retPwtUpdatePstmt = connection.prepareStatement("UPDATE st_sle_ret_pwt aa, st_sle_pwt_inv bb, st_lms_retailer_transaction_master cc SET aa.pwt_claim_status=?, aa.agent_ref_reansaction_id=?, bb.status=?, bb.agent_transaction_id=? WHERE aa.transaction_id=bb.retailer_transaction_id AND aa.transaction_id=cc.transaction_id AND aa.game_id=? AND aa.game_type_id=? AND aa.draw_id=? AND aa.retailer_org_id=? AND DATE(cc.transaction_date)=DATE(?);");
			agtPstmt = connection.prepareStatement("INSERT INTO st_sle_agt_pwt (agent_user_id, agent_org_id, retailer_org_id, game_id, game_type_id, draw_id, transaction_id, pwt_amt, comm_amt, net_amt, agt_claim_comm, govt_claim_comm, STATUS) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?);");

			int retOrgId = 0, agtOrgId=0, agtUserId, gameId, gameTypeId, drawId;
			double totalPwtAmt, totalAgtComm, totalRetComm, totalNetAmt = 0, totalGovComm = 0;
			String pwtType, transDate;
			Map<Integer, Double> orgAmtMap = new HashMap<Integer, Double>();
			Map<Integer, Integer> orgIdParentIdMap = new HashMap<Integer, Integer>();
			Map<Integer, Map<String, List<Long>>> orgIdTransIdInvMap = new HashMap<Integer, Map<String, List<Long>>>();
			Map<String, List<Long>> transIdInvMap = null;
			totalNetAmt = 0;
			/*logger.debug("Game Statement - "+gameQry);
			gameRs = gameStmt.executeQuery(gameQry);
			while (gameRs.next()) {
				

				gameId = gameRs.getInt("game_id");
				gameTypeId = gameRs.getInt("game_type_id");
				logger.debug("Start PWT transaction gameId - gameTypeId : "+gameId+" - "+gameTypeId+" | "+pwtPstmt);*/
				rs = pwtPstmt.executeQuery();
				while (rs.next()) {
					retOrgId = rs.getInt("retailer_org_id");
					agtOrgId = rs.getInt("agtOrgId");
					agtUserId = rs.getInt("agtUserId");
					gameId = rs.getInt("game_id");
					gameTypeId = rs.getInt("game_type_id");
					drawId = rs.getInt("draw_id");
					totalPwtAmt = rs.getDouble("totalPwtAmt");
					totalRetComm = rs.getDouble("totalRetComm");
					totalAgtComm = rs.getDouble("totalAgtComm");
					totalGovComm = rs.getDouble("totalGovAmt");
					pwtType = rs.getString("pwt_type");
					transDate = rs.getString("transaction_date");
					totalNetAmt = totalPwtAmt + totalRetComm - totalGovComm;

					orgIdParentIdMap.put(retOrgId, agtOrgId);
					if (orgIdTransIdInvMap.containsKey(retOrgId)) {
						transIdInvMap = orgIdTransIdInvMap.get(retOrgId);
					} else {
						transIdInvMap = new HashMap<String, List<Long>>();
						orgIdTransIdInvMap.put(retOrgId, transIdInvMap);
					}

					transMasQueryPstmt.setString(1, "AGENT");
					logger.debug("Insert main Transaction - "+transMasQueryPstmt);
					transMasQueryPstmt.executeUpdate();
					tranRs = transMasQueryPstmt.getGeneratedKeys();
					if (tranRs.next()) {
						long transId = tranRs.getLong(1);

						if (!transIdInvMap.containsKey(transDate)) {
							transIdInvMap.put(transDate, new ArrayList<Long>());
						}
						transIdInvMap.get(transDate).add(transId);

						agtTransMasterPstmt.setLong(1, transId);
						agtTransMasterPstmt.setInt(2, agtUserId);
						agtTransMasterPstmt.setInt(3, agtOrgId);
						agtTransMasterPstmt.setString(4, "RETAILER");
						agtTransMasterPstmt.setInt(5, retOrgId);
						agtTransMasterPstmt.setString(6, "SLE_PWT_AUTO");
						agtTransMasterPstmt.setTimestamp(7,updateLedgerHelper.fetchTransTimeStamp(transDate));
						logger.debug("Insert Agent Transaction - "+agtTransMasterPstmt);
						agtTransMasterPstmt.executeUpdate();

						if ("Anonymous".equals(pwtType)) {
							retPwtUpdatePstmt.setString(1, "DONE_CLM");
							retPwtUpdatePstmt.setLong(2, transId);
							retPwtUpdatePstmt.setString(3, "CLAIM_RET_CLM_AUTO");
							retPwtUpdatePstmt.setLong(4, transId);
							retPwtUpdatePstmt.setInt(5, gameId);
							retPwtUpdatePstmt.setInt(6, gameTypeId);
							retPwtUpdatePstmt.setInt(7, drawId);
							retPwtUpdatePstmt.setInt(8, retOrgId);
							retPwtUpdatePstmt.setString(9, transDate);
							//retPwtUpdatePstmt.setString(9, "2014-12-10 11:54:34");
							logger.debug("Update Retailer PWT Tables - "+retPwtUpdatePstmt);
							retPwtUpdatePstmt.executeUpdate();
						}

						agtPstmt.setInt(1, agtUserId);
						agtPstmt.setInt(2, agtOrgId);
						agtPstmt.setInt(3, retOrgId);
						agtPstmt.setInt(4, gameId);
						agtPstmt.setInt(5, gameTypeId);
						agtPstmt.setInt(6, drawId);
						agtPstmt.setLong(7, transId);
						agtPstmt.setDouble(8, totalPwtAmt);
						agtPstmt.setDouble(9, totalRetComm);
						agtPstmt.setDouble(10, totalNetAmt);
						agtPstmt.setDouble(11, totalAgtComm);
						agtPstmt.setDouble(12, totalGovComm);
						agtPstmt.setString(13, "CLAIM_BAL");
						logger.debug("Insert In st_sle_agt_pwt - "+agtPstmt);
						agtPstmt.executeUpdate();
					}

					double retCrAmt = totalNetAmt;
					if (orgAmtMap.containsKey(retOrgId)) {
						retCrAmt = retCrAmt + orgAmtMap.get(retOrgId);
					}
					orgAmtMap.put(retOrgId, retCrAmt);
				}
		//	}

			logger.debug("Retailer PWT Update orgMap - "+orgAmtMap);

			for (Integer orgId : orgAmtMap.keySet()) {
				if (orgId != 0) {
					boolean isValid = OrgCreditUpdation.updateOrganizationBalWithValidate(CommonMethods.fmtToTwoDecimal(orgAmtMap.get(orgId)), "TRANSACTION", "PWT_AUTO", orgId, orgIdParentIdMap.get(orgId), "RETAILER", 0, connection);
					if(!isValid)
						throw new LMSException();

					OrgCreditUpdation.updateOrganizationBalWithValidate(CommonMethods.fmtToTwoDecimal(orgAmtMap.get(orgId)), "CLAIM_BAL", "CREDIT_UPDATE_LEDGER", orgId, orgIdParentIdMap.get(orgId), "RETAILER", 0, connection);

					//helper.generateReciptForPwtNew(orgIdTransIdInvMap.get(orgId), connection, orgIdParentIdMap.get(orgId), retOrgId, "RETAILER", "DRAWGAME");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("Error in PWT Retailer");
		}

		logger.debug("--- End SLE PWT Update for Retailer --- "+Util.getCurrentTimeString());
	}

	private static void updateSLEPwtLedgerForAgt(Connection con) throws LMSException{
		logger.debug("--- Start SLE PWT Update for Agent ---"+Util.getCurrentTimeString());

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			PreparedStatement agtPwtUpdatePstmt = con.prepareStatement("UPDATE st_sle_agt_pwt aa, st_sle_pwt_inv bb, st_lms_agent_transaction_master cc SET aa.status=?, bb.status=?, bb.bo_transaction_id=? WHERE aa.transaction_id=bb.agent_transaction_id AND aa.transaction_id=cc.transaction_id AND aa.game_id=? AND aa.game_type_id=? AND aa.draw_id=? AND bb.game_id=? AND bb.game_type_id=? AND bb.draw_id=? AND aa.agent_org_id=? AND DATE(cc.transaction_date)=?;");
			PreparedStatement agtDirPlrPstmt = con.prepareStatement("UPDATE st_sle_agent_direct_plr_pwt aa, st_sle_pwt_inv bb, st_lms_agent_transaction_master cc SET aa.pwt_claim_status=?, bb.status=?, bb.bo_transaction_id=? WHERE aa.transaction_id=bb.agent_transaction_id AND aa.transaction_id=cc.transaction_id AND aa.game_id=? AND aa.game_type_id=? AND aa.draw_id=? AND bb.game_id=? AND bb.game_type_id=? AND bb.draw_id=? AND aa.agent_org_id=? AND DATE(cc.transaction_date)=?;");
			PreparedStatement transMasQueryPstmt = con.prepareStatement(QueryManager.insertInLMSTransactionMaster());
			PreparedStatement boTransMasterPstmt = con.prepareStatement(QueryManager.insertInBOTransactionMaster());
			PreparedStatement agtPstmt = con.prepareStatement("INSERT INTO st_sle_bo_pwt (bo_user_id, bo_org_id, agent_org_id, game_id, game_type_id, draw_id, transaction_id, pwt_amt, comm_amt, net_amt, govt_claim_comm) VALUES (?,?,?,?,?,?,?,?,?,?,?);");

			int agtOrgId, boOrgId, boUserId, gameId, gameTypeId, drawId;
			double totalPwtAmt, totalAgtComm, totalNetAmt, totalGovComm = 0;
			String pwtType, transDate;
			Map<Integer, Double> orgAmtMap = new HashMap<Integer, Double>();
			Map<Integer, Integer> orgIdParentIdMap = new HashMap<Integer, Integer>();
			Map<Integer, Map<String, List<Long>>> orgIdTransIdInvMap = new HashMap<Integer, Map<String, List<Long>>>();
			Map<String, List<Long>> transIdInvMap = null;

//			pstmt = con.prepareStatement("SELECT aaa.agent_org_id, boUserId, boOrgId, aaa.game_id, bbb.game_type_id, aaa.draw_id, SUM(aaa.agt_claim_comm) totalAgtComm, SUM(aaa.pwt_amt) totalPwtAmt, aaa.pwt_type, DATE(aaa.transaction_date) 'transaction_date' FROM ((SELECT a.transaction_id, a.agent_org_id, a.draw_id, a.pwt_amt, 'Anonymous' AS 'pwt_type', a.game_id, a.game_type_id, a.agt_claim_comm, transaction_date FROM st_sle_agt_pwt a INNER JOIN st_lms_agent_transaction_master b ON a.transaction_id=b.transaction_id WHERE a.status='CLAIM_BAL') UNION ALL (SELECT aa.transaction_id, aa.agent_org_id, aa.draw_id, aa.pwt_amt, 'DIRECT_PLAYER' AS 'pwt_type', aa.game_id, aa.game_type_id, agt_claim_comm, transaction_date FROM st_sle_agent_direct_plr_pwt aa WHERE aa.pwt_claim_status='CLAIM_BAL'))aaa, st_sle_game_type_master bbb, (SELECT um.user_id agtUserId, um.organization_id agtOrgId, um.parent_user_id boUserId, om.parent_id boOrgId FROM st_lms_organization_master om INNER JOIN st_lms_user_master um ON om.organization_id=um.organization_id WHERE om.organization_type='AGENT' AND isrolehead='Y') ccc WHERE aaa.game_id=bbb.game_id AND aaa.game_type_id=bbb.game_type_id AND agent_org_id=agtOrgId GROUP BY pwt_type, agent_org_id, aaa.game_id, aaa.draw_id, DATE(aaa.transaction_date);");
			pstmt = con.prepareStatement("SELECT aaa.agent_org_id, boUserId, boOrgId, aaa.game_id, bbb.game_type_id, aaa.draw_id, SUM(aaa.agt_claim_comm) totalAgtComm, SUM(aaa.pwt_amt) totalPwtAmt, sum(aaa.govt_comm) totalGovComm, aaa.pwt_type, DATE(aaa.transaction_date) 'transaction_date' FROM ((SELECT a.transaction_id, a.agent_org_id, a.draw_id, a.pwt_amt, a.govt_claim_comm govt_comm, 'Anonymous' AS 'pwt_type', a.game_id, a.game_type_id, a.agt_claim_comm, transaction_date FROM st_sle_agt_pwt a INNER JOIN st_lms_agent_transaction_master b ON a.transaction_id=b.transaction_id WHERE a.status='CLAIM_BAL') UNION ALL (SELECT aa.transaction_id, aa.agent_org_id, aa.draw_id, aa.pwt_amt, aa.tax_amt govt_comm, 'DIRECT_PLAYER' AS 'pwt_type', aa.game_id, aa.game_type_id, agt_claim_comm, transaction_date FROM st_sle_agent_direct_plr_pwt aa WHERE aa.pwt_claim_status='CLAIM_BAL'))aaa, st_sle_game_type_master bbb, (SELECT um.user_id agtUserId, um.organization_id agtOrgId, um.parent_user_id boUserId, om.parent_id boOrgId FROM st_lms_organization_master om INNER JOIN st_lms_user_master um ON om.organization_id=um.organization_id WHERE om.organization_type='AGENT' AND isrolehead='Y') ccc WHERE aaa.game_id=bbb.game_id AND aaa.game_type_id=bbb.game_type_id AND agent_org_id=agtOrgId GROUP BY pwt_type, agent_org_id, aaa.game_id, aaa.draw_id, DATE(aaa.transaction_date);");
			rs = pstmt.executeQuery();
			while (rs.next()) {
				agtOrgId = rs.getInt("agent_org_id");
				boOrgId = rs.getInt("boOrgId");
				boUserId = rs.getInt("boUserId");
				gameId = rs.getInt("game_id");
				gameTypeId = rs.getInt("game_type_id");
				drawId = rs.getInt("draw_id");
				totalPwtAmt = rs.getDouble("totalPwtAmt");
				totalAgtComm = rs.getDouble("totalAgtComm");
				totalGovComm = rs.getDouble("totalGovComm");
				pwtType = rs.getString("pwt_type");
				transDate = rs.getString("transaction_date");

				totalNetAmt = totalPwtAmt + totalAgtComm - totalGovComm;

				orgIdParentIdMap.put(agtOrgId, boOrgId);
				if (orgIdTransIdInvMap.containsKey(agtOrgId)) {
					transIdInvMap = orgIdTransIdInvMap.get(agtOrgId);
				} else {
					transIdInvMap = new HashMap<String, List<Long>>();
					orgIdTransIdInvMap.put(agtOrgId, transIdInvMap);
				}

				transMasQueryPstmt.setString(1, "BO");
				transMasQueryPstmt.executeUpdate();
				ResultSet tranRs = transMasQueryPstmt.getGeneratedKeys();
				if (tranRs.next()) {
					if (!transIdInvMap.containsKey(transDate)) {
						transIdInvMap.put(transDate, new ArrayList<Long>());
					}

					long transId = tranRs.getLong(1);
					transIdInvMap.get(transDate).add(transId);

					boTransMasterPstmt.setLong(1, transId);
					boTransMasterPstmt.setInt(2, boUserId);
					boTransMasterPstmt.setInt(3, boOrgId);
					boTransMasterPstmt.setString(4, "AGENT");
					boTransMasterPstmt.setInt(5, agtOrgId);
					boTransMasterPstmt.setTimestamp(6,updateLedgerHelper.fetchTransTimeStamp(transDate));
					boTransMasterPstmt.setString(7, "SLE_PWT_AUTO");
					boTransMasterPstmt.executeUpdate();

					if (!"DIRECT_PLAYER".equals(pwtType)) {
						agtPwtUpdatePstmt.setString(1, "DONE_CLM");
						agtPwtUpdatePstmt.setString(2, "CLAIM_AGT_CLM_AUTO");
						agtPwtUpdatePstmt.setLong(3, transId);
						agtPwtUpdatePstmt.setInt(4, gameId);
						agtPwtUpdatePstmt.setInt(5, gameTypeId);
						agtPwtUpdatePstmt.setInt(6, drawId);
						agtPwtUpdatePstmt.setInt(7, gameId);
						agtPwtUpdatePstmt.setInt(8, gameTypeId);
						agtPwtUpdatePstmt.setInt(9, drawId);
						agtPwtUpdatePstmt.setInt(10, agtOrgId);
						agtPwtUpdatePstmt.setString(11, transDate);
						logger.debug("Agent PWT - "+agtPwtUpdatePstmt);
						agtPwtUpdatePstmt.executeUpdate();
					} else {
						agtDirPlrPstmt.setString(1, "DONE_CLM");
						agtDirPlrPstmt.setString(2, "CLAIM_AGT_CLM_AUTO");
						agtDirPlrPstmt.setLong(3, transId);
						agtDirPlrPstmt.setInt(4, gameId);
						agtDirPlrPstmt.setInt(5, gameTypeId);
						agtDirPlrPstmt.setInt(6, drawId);
						agtDirPlrPstmt.setInt(7, gameId);
						agtDirPlrPstmt.setInt(8, gameTypeId);
						agtDirPlrPstmt.setInt(9, drawId);
						agtDirPlrPstmt.setInt(10, agtOrgId);
						agtDirPlrPstmt.setString(11, transDate);
						logger.debug("Agent PWT (Direct Player) - "+agtDirPlrPstmt);
						agtDirPlrPstmt.executeUpdate();
					}

					agtPstmt.setInt(1, boUserId);
					agtPstmt.setInt(2, boOrgId);
					agtPstmt.setInt(3, agtOrgId);
					agtPstmt.setInt(4, gameId);
					agtPstmt.setInt(5, gameTypeId);
					agtPstmt.setInt(6, drawId);
					agtPstmt.setLong(7, transId);
					agtPstmt.setDouble(8, totalPwtAmt);
					agtPstmt.setDouble(9, totalAgtComm);
					agtPstmt.setDouble(10, totalNetAmt);
					agtPstmt.setDouble(11, totalGovComm);
					agtPstmt.executeUpdate();
				}

				double agtCrAmt = totalNetAmt;
				if (orgAmtMap.containsKey(agtOrgId)) {
					agtCrAmt = agtCrAmt + orgAmtMap.get(agtOrgId);
				}
				orgAmtMap.put(agtOrgId, agtCrAmt);
			}
			logger.debug("Agent PWT Update ORG Amt Map - "+orgAmtMap);

			updateLedgerHelper helper = new updateLedgerHelper();

			Set<Integer> allAgtId = orgAmtMap.keySet();
			for (Integer orgId : allAgtId) {
				if (orgId != 0) {
					boolean isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(CommonMethods.fmtToTwoDecimal(orgAmtMap.get(orgId)), "TRANSACTION", "PWT_AUTO", orgId, 0, "AGENT", 0, con);
					if(!isValid)
						throw new LMSException();
					OrgCreditUpdation.updateOrganizationBalWithValidate(CommonMethods.fmtToTwoDecimal(orgAmtMap.get(orgId)), "CLAIM_BAL", "CREDIT_UPDATE_LEDGER", orgId, 0, "AGENT", 0, con);
					helper.generateReceiptDGBoNew(con, orgId, "AGENT", orgIdTransIdInvMap.get(orgId));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("Error in PWT Agent");
		}

		logger.debug("--- End SLE PWT Update for Agent --- "+Util.getCurrentTimeString());
	}
	


	private static void updateSLESaleRefundLedgerForRet(Connection con) throws LMSException{

		//logger.debug("Transactions from SALE in updateSLESaleLedgerForRet() begins........");
		Statement stmt = null;
		ResultSet rs = null;
		Map<String, List<Long>> transIdInvMap = null;
		Map<Integer, Double> orgIdAmountMap = new HashMap<Integer, Double>();
		Map<Integer, Integer> orgIdParentIdMap = new HashMap<Integer, Integer>();
		Map<Integer, Map<String, List<Long>>> orgIdTransIdInvRetMap = new HashMap<Integer, Map<String, List<Long>>>();
		Map<Integer, Map<String, List<Long>>> orgIdTransIdInvMap = new HashMap<Integer, Map<String, List<Long>>>();

		Map<String, List<Long>> transIdInvRetMap = null;
		Statement tempStmt=null;
		try {
			con.setAutoCommit(false);
			//String saleQry = "select a.game_type_id,b.game_id , a.retailer_org_id,agtUserId,agtOrgId,sum(mrp_amt) totalMrp ,sum(retailer_comm_amt) totalRetComm,sum(agent_comm_amt) totalagtComm, sum(good_cause_amt) totalgovtComm, sum(vat_amt) totalVat,sum(taxable_sale) totalTaxSale,DATE(a.transaction_date) 'transaction_date',ret_comm,agt_comm,govt_comm from st_sle_ret_sale a inner join st_lms_retailer_transaction_master b inner join st_temp_update_ledger c inner join (select um.user_id retUserId,um.organization_id retOrgId,um.parent_user_id agtUserId,om.parent_id agtOrgId from st_lms_organization_master om inner join st_lms_user_master um on om.organization_id=um.organization_id where om.organization_type='RETAILER' ) d on a.transaction_id = b.transaction_id and a.transaction_id=trans_id and a.retailer_org_id=retOrgId  where claim_status='CLAIM_BAL' group by a.retailer_org_id,DATE(transaction_date),a.game_type_id,ret_comm,agt_comm,govt_comm  order by retailer_org_id,game_type_id,transaction_date";
			String saleQry = "select a.game_type_id,a.game_id , retailer_org_id,agtUserId,agtOrgId,totalMrp , totalRetComm, totalagtComm,  totalgovtComm,  totalVat, totalTaxSale,transaction_date,ret_comm,agt_comm,govt_comm ,prize_payout_ratio,vat_amt from (select a.game_type_id,b.game_id , a.retailer_org_id,agtUserId,agtOrgId,sum(mrp_amt) totalMrp ,sum(retailer_comm_amt) totalRetComm,sum(agent_comm_amt) totalagtComm, sum(good_cause_amt) totalgovtComm, sum(vat_amt) totalVat,sum(taxable_sale) totalTaxSale,DATE(a.transaction_date) 'transaction_date',ret_comm,agt_comm,govt_comm from st_sle_ret_sale a inner join st_lms_retailer_transaction_master b inner join st_temp_update_ledger c inner join (select um.user_id retUserId,um.organization_id retOrgId,um.parent_user_id agtUserId,om.parent_id agtOrgId from st_lms_organization_master om inner join st_lms_user_master um on om.organization_id=um.organization_id where om.organization_type='RETAILER' ) d on a.transaction_id = b.transaction_id and a.transaction_id=trans_id and a.retailer_org_id=retOrgId  where claim_status='CLAIM_BAL' group by a.retailer_org_id,DATE(transaction_date),a.game_type_id,ret_comm,agt_comm,govt_comm  order by retailer_org_id,game_type_id,transaction_date) a inner join st_sle_game_type_master sle on sle.game_type_id  =  a.game_type_id ";
			PreparedStatement pstmtAgt = con.prepareStatement(QueryManager.insertInLMSTransactionMaster());
			PreparedStatement pstmtAgtTrans = con.prepareStatement(QueryManager.insertInAgentTransactionMaster());
			PreparedStatement insAgtSalePstmt = con.prepareStatement("insert into st_sle_agt_sale(transaction_id,agent_org_id,retailer_org_id,game_id,game_type_id,mrp_amt,retailer_comm_amt,net_amt,agent_comm_amt,bo_net_amt,claim_status,vat_amt,taxable_sale,govt_comm,transaction_date) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			PreparedStatement insAgtSaleRetPstmt = con.prepareStatement("insert into st_sle_agt_sale_refund(transaction_id,agent_org_id,retailer_org_id,game_id,game_type_id,mrp_amt,retailer_comm_amt,net_amt,agent_comm_amt,bo_net_amt,claim_status,vat_amt,taxable_sale,govt_comm,cancellation_charges,transaction_date) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			PreparedStatement insTempTablePstmt=con.prepareStatement("insert into orgTransSummary(agent_ref_transaction_id,ret_comm,agt_comm,govt_comm,retailer_org_id,game_id,game_type_id,trans_date) values(?,?,?,?,?,?,?,?)");
			tempStmt=con.createStatement();
			tempStmt.execute("delete from orgTransSummary ");
			
			
			int count = 0;
			double retDebitAmount = 0.0;
		/*	stmt = con.createStatement();
			rs = stmt.executeQuery(saleQry);

			while (rs.next()) {
				double agtCommRate = rs.getDouble("agt_comm");
				double totalAgtComm = rs.getDouble("totalagtComm");
				double totalMrpAmt = rs.getDouble("totalMrp");
				double totalRetComm = rs.getDouble("totalRetComm");
				double totalretNetAmt = totalMrpAmt - totalRetComm;
				double totalAgtNetAmt = totalMrpAmt - totalAgtComm;
				double retCommRate = rs.getDouble("ret_comm");
				int ppr = rs.getInt("prize_payout_ratio");
				double govtCommRate = rs.getDouble("govt_comm");
				double vatAmt = rs.getDouble("vat_amt");

				// CHECK IF necessary
				double tatalAgtTaxableSale = CommonMethods.calTaxableSale(totalMrpAmt, retCommRate, ppr, govtCommRate, vatAmt);

				int retOrgId = rs.getInt("retailer_org_id");
				if (orgIdAmountMap.containsKey(retOrgId)) {
					retDebitAmount = orgIdAmountMap.get(retOrgId) + totalretNetAmt;
				} else {
					retDebitAmount = totalretNetAmt;
				}

				orgIdAmountMap.put(retOrgId, retDebitAmount);
				int agtOrgId = rs.getInt("agtOrgId");
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
				int gameId = rs.getInt("game_id");
				int gameTypeId = rs.getInt("game_type_id");
				double totalAgtVat = rs.getDouble("totalVat");
				double totalGoodCauseAmt = rs.getDouble("totalgovtComm");
				String transDate = rs.getString("transaction_date");
				int agtUserId = rs.getInt("agtUserId");

				if (rsTrns.next()) {
					long transId = rsTrns.getLong(1);
					//logger.debug("--Retailer org id"+retOrgId+"--Agent org Id-- "+agtOrgId+" -- Start Sale for Game type "+gameTypeId +" with transaction "+ transId);
					if (!transIdInvMap.containsKey(transDate)) {
						transIdInvMap.put(transDate, new ArrayList<Long>());
					}

					
					transIdInvMap.get(transDate).add(transId);
					pstmtAgtTrans.setLong(1, transId);
					pstmtAgtTrans.setInt(2, agtUserId);
					pstmtAgtTrans.setInt(3, agtOrgId);
					pstmtAgtTrans.setString(4, "RETAILER");
					pstmtAgtTrans.setInt(5, retOrgId);
					pstmtAgtTrans.setString(6, "SLE_SALE");
					pstmtAgtTrans.setTimestamp(7, updateLedgerHelper.fetchTransTimeStamp(transDate));
					pstmtAgtTrans.executeUpdate();

					insAgtSalePstmt.setLong(1, transId);
					insAgtSalePstmt.setInt(2, agtOrgId);
					insAgtSalePstmt.setInt(3, retOrgId);
					insAgtSalePstmt.setInt(4, gameId);
					insAgtSalePstmt.setInt(5, gameTypeId);
					insAgtSalePstmt.setDouble(6, totalMrpAmt);
					insAgtSalePstmt.setDouble(7, totalRetComm);
					insAgtSalePstmt.setDouble(8, totalretNetAmt);
					insAgtSalePstmt.setDouble(9, totalAgtComm);
					insAgtSalePstmt.setDouble(10, totalAgtNetAmt);
					insAgtSalePstmt.setString(11, "CLAIM_BAL");
					insAgtSalePstmt.setDouble(12, totalAgtVat);
					insAgtSalePstmt.setDouble(13, tatalAgtTaxableSale);
					insAgtSalePstmt.setDouble(14, totalGoodCauseAmt);
					insAgtSalePstmt.setTimestamp(15, updateLedgerHelper.fetchTransTimeStamp(transDate));
					insAgtSalePstmt.executeUpdate();

					// insert temp table
					insTempTablePstmt.setLong(1, transId);
					insTempTablePstmt.setDouble(2, retCommRate);
					insTempTablePstmt.setDouble(3, agtCommRate);
					insTempTablePstmt.setDouble(4, govtCommRate);
					insTempTablePstmt.setInt(5, retOrgId);
					insTempTablePstmt.setInt(6, gameId);
					insTempTablePstmt.setInt(7, gameTypeId);
					insTempTablePstmt.setString(8, transDate);
					insTempTablePstmt.addBatch();
				
					if (++count % batchSize == 0) {
						insTempTablePstmt.executeBatch();
					}
				}
			}
				
				logger.debug(insTempTablePstmt.executeBatch());
				//logger.debug("Transactions from SALE in updateSLESaleLedgerForRet() are updated successfully");
				
				con.commit();
				// BE CARE FULL FOR THE GAME_TYPE_ID   
				//tempStmt.executeUpdate("update st_sle_ret_sale sale inner join (select yy.transaction_id,xx.agent_ref_transaction_id from orgTransSummary xx inner join (select transaction_id,transaction_date,retailer_org_id,ret_comm,agt_comm,govt_comm,game_id from st_lms_retailer_transaction_master a inner join st_temp_update_ledger b on a.transaction_id = b.trans_id)yy on DATE(trans_date)=DATE(transaction_date) and xx.retailer_org_id=yy.retailer_org_id and xx.ret_comm=yy.ret_comm and xx.agt_comm=yy.agt_comm and xx.govt_comm=yy.govt_comm and xx.game_id=yy.game_id) tlb on sale.transaction_id=tlb.transaction_id set sale.claim_status='DONE_CLAIM',sale.agent_ref_transaction_id=tlb.agent_ref_transaction_id");
				//logger.debug(tempStmt.executeUpdate("update st_sle_ret_sale sale inner join (select yy.transaction_id,xx.agent_ref_transaction_id from orgTransSummary xx inner join (select transaction_id,transaction_date,retailer_org_id,ret_comm,agt_comm,govt_comm,game_id from st_lms_retailer_transaction_master a inner join st_temp_update_ledger b on a.transaction_id = b.trans_id)yy on DATE(trans_date)=DATE(transaction_date) and xx.retailer_org_id=yy.retailer_org_id and xx.ret_comm=yy.ret_comm and xx.agt_comm=yy.agt_comm and xx.govt_comm=yy.govt_comm and xx.game_type_id=yy.game_type_id ) tlb on sale.transaction_id=tlb.transaction_id set sale.claim_status='DONE_CLAIM',sale.agent_ref_transaction_id=tlb.agent_ref_transaction_id"));
				logger.debug(tempStmt.executeUpdate("update st_sle_ret_sale sale inner join (select yy.transaction_id,xx.agent_ref_transaction_id from orgTransSummary xx inner join (select a.transaction_id,a.transaction_date,a.retailer_org_id,ret_comm,agt_comm,govt_comm,a.game_id,game_type_id from st_lms_retailer_transaction_master a inner join st_temp_update_ledger b inner join st_sle_ret_sale c on a.transaction_id = b.trans_id and b.trans_id = c.transaction_id) yy on DATE(trans_date)=DATE(transaction_date) and xx.retailer_org_id=yy.retailer_org_id and xx.ret_comm=yy.ret_comm and xx.agt_comm=yy.agt_comm and xx.govt_comm=yy.govt_comm and xx.game_type_id=yy.game_type_id ) tlb on sale.transaction_id=tlb.transaction_id set sale.claim_status='DONE_CLAIM',sale.agent_ref_transaction_id=tlb.agent_ref_transaction_id"));*/
				
				tempStmt.execute("delete from  orgTransSummary ");

				String saleReturnQry = "select a.game_type_id,a.game_id,retailer_org_id,agtUserId,agtOrgId, totalMrp ,totalRetComm, totalagtComm,  totalgovtComm,  totalVat, totalTaxSale, totalCancelCharge,transaction_date,ret_comm,agt_comm,govt_comm,transaction_type,prize_payout_ratio,vat_amt from (select a.game_type_id,a.game_id,a.retailer_org_id,agtUserId,agtOrgId,sum(mrp_amt) totalMrp ,sum(retailer_comm_amt) totalRetComm,sum(agent_comm_amt) totalagtComm, sum(good_cause_amt) totalgovtComm, sum(vat_amt) totalVat,sum(taxable_sale) totalTaxSale,sum(cancellation_charges) totalCancelCharge,DATE(a.transaction_date) 'transaction_date',ret_comm,agt_comm,govt_comm,transaction_type from st_sle_ret_sale_refund a inner join  st_lms_retailer_transaction_master b inner join st_temp_update_ledger c inner join (select um.user_id retUserId,um.organization_id retOrgId,um.parent_user_id agtUserId,om.parent_id agtOrgId from st_lms_organization_master om inner join st_lms_user_master um on om.organization_id=um.organization_id where om.organization_type='RETAILER') d on a.transaction_id = b.transaction_id and a.transaction_id=trans_id and a.retailer_org_id=retOrgId  where claim_status='CLAIM_BAL' group by a.retailer_org_id,DATE(transaction_date),a.game_type_id,transaction_type,ret_comm,agt_comm,govt_comm  order by retailer_org_id,game_type_id,transaction_date) a inner join st_sle_game_type_master sle on sle.game_type_id  =  a.game_type_id"; 
				stmt = con.createStatement();
				rs = stmt.executeQuery(saleReturnQry);
				while (rs.next()) {
					int retOrgId = rs.getInt("retailer_org_id");
					int agtUserId = rs.getInt("agtUserId");
					int agtOrgId = rs.getInt("agtOrgId");
					int gameId  = 	rs.getInt("game_id");
					int gameTypeId  = 	rs.getInt("game_type_id");
					double totalMrpAmt = rs.getDouble("totalMrp");
					double totalRetComm = rs.getDouble("totalRetComm");
					double totalAgtComm = rs.getDouble("totalagtComm");
					double totalGoodCauseAmt = rs.getDouble("totalgovtComm");
					double totalCancelCharge = rs.getDouble("totalCancelCharge");
					double retCommRate = rs.getDouble("ret_comm");
					double agtCommRate = rs.getDouble("agt_comm");
					double govtCommRate = rs.getDouble("govt_comm");
					String transDate = rs.getString("transaction_date");
					double totalretNetAmt = totalMrpAmt - totalRetComm	- totalCancelCharge;
					double totalAgtNetAmt = totalMrpAmt - totalAgtComm	- totalCancelCharge;
					String tranType = rs.getString("transaction_type");
					double totalAgtVat =rs.getDouble("totalVat");
					int ppr = rs.getInt("prize_payout_ratio");
					double vatAmt = rs.getDouble("vat_amt");
					double tatalAgtTaxableSale = CommonMethods.calTaxableSale(totalMrpAmt, retCommRate, ppr, govtCommRate, vatAmt);

					if (orgIdAmountMap.containsKey(retOrgId)) {
						retDebitAmount = orgIdAmountMap.get(retOrgId) - totalretNetAmt;
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
						//logger.debug("--Retailer org id"+retOrgId+"--Agent org Id--"+agtOrgId+"--Start Sale Refund tran for Game type "+gameTypeId +" with transaction "+ transId);
						if (!transIdInvRetMap.containsKey(transDate)) {
							transIdInvRetMap.put(transDate ,	new ArrayList<Long>());
						}

						transIdInvRetMap.get(transDate).add(transId);

						pstmtAgtTrans.setLong(1, transId);
						pstmtAgtTrans.setInt(2, agtUserId);
						pstmtAgtTrans.setInt(3, agtOrgId);
						pstmtAgtTrans.setString(4, "RETAILER");
						pstmtAgtTrans.setInt(5, retOrgId);
						pstmtAgtTrans.setString(6, tranType);
						pstmtAgtTrans.setTimestamp(7,updateLedgerHelper.fetchTransTimeStamp(transDate));
						pstmtAgtTrans.executeUpdate();

						insAgtSaleRetPstmt.setLong(1, transId);
						insAgtSaleRetPstmt.setInt(2, agtOrgId);
						insAgtSaleRetPstmt.setInt(3, retOrgId);
						insAgtSaleRetPstmt.setInt(4, gameId);
						insAgtSaleRetPstmt.setInt(5, gameTypeId);
						// ADD GAME TYPE ID FOR AGENT
						insAgtSaleRetPstmt.setDouble(6, totalMrpAmt);
						insAgtSaleRetPstmt.setDouble(7, totalRetComm);
						insAgtSaleRetPstmt.setDouble(8, totalretNetAmt);
						insAgtSaleRetPstmt.setDouble(9, totalAgtComm);
						insAgtSaleRetPstmt.setDouble(10, totalAgtNetAmt);
						insAgtSaleRetPstmt.setString(11, "CLAIM_BAL");
						insAgtSaleRetPstmt.setDouble(12, totalAgtVat);
						insAgtSaleRetPstmt.setDouble(13, tatalAgtTaxableSale);
						insAgtSaleRetPstmt.setDouble(14, totalGoodCauseAmt);
						insAgtSaleRetPstmt.setDouble(15, totalCancelCharge);
						insAgtSaleRetPstmt.setTimestamp(16, updateLedgerHelper.fetchTransTimeStamp(transDate));
						insAgtSaleRetPstmt.executeUpdate();

						
						//insert temp table
						insTempTablePstmt.setLong(1, transId);
						insTempTablePstmt.setDouble(2, retCommRate);
						insTempTablePstmt.setDouble(3, agtCommRate);
						insTempTablePstmt.setDouble(4, govtCommRate);
						insTempTablePstmt.setInt(5, retOrgId);
						insTempTablePstmt.setInt(6, gameId);
						insTempTablePstmt.setInt(7, gameTypeId);
						insTempTablePstmt.setString(8, transDate);
						insTempTablePstmt.addBatch();
						if(++count % batchSize == 0) {
							insTempTablePstmt.executeBatch();
					    }
					}
				}
				
				insTempTablePstmt.executeBatch();
				// BE CARE FULL FOR THE GAME_TYPE_ID   
				//tempStmt.executeUpdate("update st_dg_ret_sale_refund sale inner join (select yy.transaction_id,xx.agent_ref_transaction_id from orgTransSummary xx inner join (select transaction_id,transaction_date,retailer_org_id,ret_comm,agt_comm,govt_comm,game_id from st_lms_retailer_transaction_master a inner join st_temp_update_ledger b on a.transaction_id = b.trans_id)yy on DATE(trans_date)=DATE(transaction_date) and xx.retailer_org_id=yy.retailer_org_id and xx.ret_comm=yy.ret_comm and xx.agt_comm=yy.agt_comm and xx.govt_comm=yy.govt_comm and xx.game_id=yy.game_id) tlb on sale.transaction_id=tlb.transaction_id set sale.claim_status='DONE_CLAIM',sale.agent_ref_transaction_id=tlb.agent_ref_transaction_id");
				//logger.debug(tempStmt.executeUpdate("update st_sle_ret_sale_refund sale inner join (select yy.transaction_id,xx.agent_ref_transaction_id from orgTransSummary xx inner join (select transaction_id,transaction_date,retailer_org_id,ret_comm,agt_comm,govt_comm,game_id from st_lms_retailer_transaction_master a inner join st_temp_update_ledger b on a.transaction_id = b.trans_id)yy on DATE(trans_date)=DATE(transaction_date) and xx.retailer_org_id=yy.retailer_org_id and xx.ret_comm=yy.ret_comm and xx.agt_comm=yy.agt_comm and xx.govt_comm=yy.govt_comm and xx.game_type_id=yy.game_id) tlb on sale.transaction_id=tlb.transaction_id set sale.claim_status='DONE_CLAIM',sale.agent_ref_transaction_id=tlb.agent_ref_transaction_id"));
				
				System.out.println(tempStmt.executeUpdate("update st_sle_ret_sale_refund sale inner join (select yy.transaction_id,xx.agent_ref_transaction_id from orgTransSummary xx inner join (select a.transaction_id,a.transaction_date,a.retailer_org_id,ret_comm,agt_comm,govt_comm,a.game_id,game_type_id from st_lms_retailer_transaction_master a inner join st_temp_update_ledger b inner join st_sle_ret_sale_refund c on a.transaction_id = b.trans_id and b.trans_id = c.transaction_id )yy on DATE(trans_date)=DATE(transaction_date) and xx.retailer_org_id=yy.retailer_org_id and xx.ret_comm=yy.ret_comm and xx.agt_comm=yy.agt_comm and xx.govt_comm=yy.govt_comm and xx.game_type_id=yy.game_type_id ) tlb on sale.transaction_id=tlb.transaction_id set sale.claim_status='DONE_CLAIM',sale.agent_ref_transaction_id=tlb.agent_ref_transaction_id"));
				con.commit();
/*				
				tempStmt.execute("delete from orgTransSummary");
				
				updateLedgerHelper helper = new updateLedgerHelper();
				helper.generateDGReceiptNew(orgIdTransIdInvMap, orgIdParentIdMap,"SLE_INVOICE", "SLE_RECEIPT", con);
				helper.generateDGReceiptNew(orgIdTransIdInvRetMap, orgIdParentIdMap,"CR_NOTE", "CR_NOTE_CASH", con);

				//logger.debug("retailer orgIdAmountMap\n" + orgIdAmountMap);

				// update retailer balance
				Set<Integer> retOrgIdSet = orgIdAmountMap.keySet();
				for (Integer orgId : retOrgIdSet) {
					boolean isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(CommonMethods.fmtToTwoDecimal(orgIdAmountMap.get(orgId)), "TRANSACTION", "SLE_SALE", orgId,
							orgIdParentIdMap.get(orgId), "RETAILER", 0, con);
					if(!isValid)
						throw new LMSException();
						
					OrgCreditUpdation.updateOrganizationBalWithValidate(CommonMethods.fmtToTwoDecimal(orgIdAmountMap.get(orgId)), "CLAIM_BAL", "DEBIT", orgId,
							orgIdParentIdMap.get(orgId), "RETAILER", 0, con);
					
				}*/
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("error in sale refund retailer");
		}
		//logger.debug(new Date() + "---end-dg sale update for retailer--"+ new Date().getTime());
	
		/*
		//logger.debug(new Date() + "---start-dg sale update for retailer--"+ new Date().getTime());
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
			String getGameNbrFromGameMaster = "select game_id,game_type_id,prize_payout_ratio,vat_amt from st_sle_game_type_master";
			String saleQry = "select a.game_type_id,b.game_id , a.retailer_org_id,agtUserId,agtOrgId,sum(mrp_amt) totalMrp ,sum(retailer_comm_amt) totalRetComm,sum(agent_comm_amt) totalagtComm, sum(good_cause_amt) totalgovtComm, sum(vat_amt) totalVat,sum(taxable_sale) totalTaxSale,DATE(a.transaction_date) 'transaction_date',ret_comm,agt_comm,govt_comm from st_sle_ret_sale a inner join st_lms_retailer_transaction_master b inner join st_temp_update_ledger c inner join (select um.user_id retUserId,um.organization_id retOrgId,um.parent_user_id agtUserId,om.parent_id agtOrgId from st_lms_organization_master om inner join st_lms_user_master um on om.organization_id=um.organization_id where om.organization_type='RETAILER' ) d on a.transaction_id = b.transaction_id and a.transaction_id=trans_id and a.retailer_org_id=retOrgId  where claim_status='CLAIM_BAL' group by a.retailer_org_id,DATE(transaction_date),a.game_type_id,ret_comm,agt_comm,govt_comm  order by retailer_org_id,game_type_id,transaction_date";
			String saleReturnQry = "select a.game_type_id,a.game_id,a.retailer_org_id,agtUserId,agtOrgId,sum(mrp_amt) totalMrp ,sum(retailer_comm_amt) totalRetComm,sum(agent_comm_amt) totalagtComm, sum(good_cause_amt) totalgovtComm, sum(vat_amt) totalVat,sum(taxable_sale) totalTaxSale,sum(cancellation_charges) totalCancelCharge,DATE(a.transaction_date) 'transaction_date',ret_comm,agt_comm,govt_comm,transaction_type from st_sle_ret_sale_refund a inner join  st_lms_retailer_transaction_master b inner join st_temp_update_ledger c inner join (select um.user_id retUserId,um.organization_id retOrgId,um.parent_user_id agtUserId,om.parent_id agtOrgId from st_lms_organization_master om inner join st_lms_user_master um on om.organization_id=um.organization_id where om.organization_type='RETAILER') d on a.transaction_id = b.transaction_id and a.transaction_id=trans_id and a.retailer_org_id=retOrgId  where claim_status='CLAIM_BAL' group by a.retailer_org_id,DATE(transaction_date),a.game_type_id,transaction_type,ret_comm,agt_comm,govt_comm  order by retailer_org_id,game_type_id,transaction_date";

			
			PreparedStatement pstmtGameNbr = con.prepareStatement(getGameNbrFromGameMaster);
			ResultSet resultGameNbr = pstmtGameNbr.executeQuery();

			PreparedStatement pstmtAgt = con.prepareStatement(QueryManager.insertInLMSTransactionMaster());
			PreparedStatement pstmtAgtTrans = con.prepareStatement(QueryManager.insertInAgentTransactionMaster());
			PreparedStatement insAgtSalePstmt = con.prepareStatement("insert into st_sle_agt_sale(transaction_id,agent_org_id,retailer_org_id,game_id,game_type_id,mrp_amt,retailer_comm,net_amt,agent_comm,bo_net_amt,claim_status,vat_amt,taxable_sale,govt_comm) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			PreparedStatement insTempTablePstmt=con.prepareStatement("insert into orgTransSummary(agent_ref_transaction_id,ret_comm,agt_comm,govt_comm,retailer_org_id,game_id,trans_date) values(?,?,?,?,?,?,?)");
			PreparedStatement insAgtSaleRetPstmt = con.prepareStatement("insert into st_dg_agt_sale_refund(transaction_id,agent_org_id,retailer_org_id,game_id,mrp_amt,retailer_comm,net_amt,agent_comm,bo_net_amt,claim_status,vat_amt,taxable_sale,govt_comm,cancellation_charges) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
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
				//logger.debug("--Start Sale tran for game No" + gameId);
				
				while (rs.next()) {
					retOrgId = rs.getInt("retailer_org_id");
					agtUserId = rs.getInt("agtUserId");
					agtOrgId = rs.getInt("agtOrgId");

					totalMrpAmt = rs.getDouble("totalMrp");
					totalRetComm = rs.getDouble("totalRetComm");
					totalAgtComm = rs.getDouble("totalagtComm");
					totalGoodCauseAmt = rs.getDouble("totalgovtComm");
					retCommRate = rs.getDouble("ret_comm");
					agtCommRate = rs.getDouble("agt_comm");
					govtCommRate = rs.getDouble("govt_comm");
					transDate = rs.getString("transaction_date");
					totalretNetAmt = totalMrpAmt - totalRetComm;
					totalAgtNetAmt = totalMrpAmt - totalAgtComm;
					double totalAgtVat= rs.getDouble("totalVat");
					double tatalAgtTaxableSale = CommonMethods.calTaxableSale(totalMrpAmt, retCommRate, ppr,govtCommRate, vatAmt);

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
						//logger.debug("--Retailer org id"+retOrgId+"--Agent org Id--"+agtOrgId+"--Start Sale tran for game No" + gameId);
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
						pstmtAgtTrans.setString(6, "SLE_SALE");
						pstmtAgtTrans.setTimestamp(7,updateLedgerHelper.fetchTransTimeStamp(transDate));
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
					}

				}
				
				insTempTablePstmt.executeBatch();
				
				
				tempStmt.executeUpdate("update st_sle_ret_sale_"+gameId+" sale inner join (select yy.transaction_id,xx.agent_ref_transaction_id from orgTransSummary xx inner join (select transaction_id,transaction_date,retailer_org_id,ret_comm,agt_comm,govt_comm,game_id from st_lms_retailer_transaction_master a inner join st_temp_update_ledger b on a.transaction_id = b.trans_id)yy on DATE(trans_date)=DATE(transaction_date) and xx.retailer_org_id=yy.retailer_org_id and xx.ret_comm=yy.ret_comm and xx.agt_comm=yy.agt_comm and xx.govt_comm=yy.govt_comm and xx.game_id=yy.game_id) tlb on sale.transaction_id=tlb.transaction_id set sale.claim_status='DONE_CLAIM',sale.agent_ref_transaction_id=tlb.agent_ref_transaction_id");
				tempStmt.execute("delete from  orgTransSummary ");
				//tempStmt.execute("truncate table orgTransSummary");
				
				pstmt = con.prepareStatement(saleReturnQry);
				pstmt.setInt(1, gameId);
				rs = pstmt.executeQuery();
				//logger.debug("--Start Cancel tran for game No" + gameId);
				while (rs.next()) {
					retOrgId = rs.getInt("retailer_org_id");
					agtUserId = rs.getInt("agtUserId");
					agtOrgId = rs.getInt("agtOrgId");

					totalMrpAmt = rs.getDouble("totalMrp");
					totalRetComm = rs.getDouble("totalRetComm");
					totalAgtComm = rs.getDouble("totalagtComm");
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
					double totalAgtVat =rs.getDouble("totalVat");

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
						//logger.debug("--Retailer org id"+retOrgId+"--Agent org Id--"+agtOrgId+"--Start Sale Cancel tran for game No" + gameId);
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
								updateLedgerHelper.fetchTransTimeStamp(transDate));
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
						
					}

				}
				
				insTempTablePstmt.executeBatch();
				
				tempStmt.executeUpdate("update st_dg_ret_sale_refund_"+gameId+" sale inner join (select yy.transaction_id,xx.agent_ref_transaction_id from orgTransSummary xx inner join (select transaction_id,transaction_date,retailer_org_id,ret_comm,agt_comm,govt_comm,game_id from st_lms_retailer_transaction_master a inner join st_temp_update_ledger b on a.transaction_id = b.trans_id)yy on DATE(trans_date)=DATE(transaction_date) and xx.retailer_org_id=yy.retailer_org_id and xx.ret_comm=yy.ret_comm and xx.agt_comm=yy.agt_comm and xx.govt_comm=yy.govt_comm and xx.game_id=yy.game_id) tlb on sale.transaction_id=tlb.transaction_id set sale.claim_status='DONE_CLAIM',sale.agent_ref_transaction_id=tlb.agent_ref_transaction_id");
				tempStmt.execute("delete from orgTransSummary");
				
			}
			updateLedgerHelper helper = new updateLedgerHelper();
			helper.generateDGReceiptNew(orgIdTransIdInvMap, orgIdParentIdMap,"SLE_INVOICE", "SLE_RECEIPT", con);
			helper.generateDGReceiptNew(orgIdTransIdInvRetMap, orgIdParentIdMap,"SLE_CR_NOTE", "SLE_CR_NOTE_CASH", con);

			//logger.debug("retailer orgIdAmountMap\n" + orgIdAmountMap);

			// update retailer balance
			Set<Integer> retOrgIdSet = orgIdAmountMap.keySet();
			for (Integer orgId : retOrgIdSet) {
				boolean isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(CommonMethods.fmtToTwoDecimal(orgIdAmountMap.get(orgId)), "TRANSACTION", "DRAW_GAME_SALE", orgId,
						orgIdParentIdMap.get(orgId), "RETAILER", 0, con);
				if(!isValid)
					throw new LMSException();
					
				OrgCreditUpdation.updateOrganizationBalWithValidate(CommonMethods.fmtToTwoDecimal(orgIdAmountMap.get(orgId)), "CLAIM_BAL", "DEBIT", orgId,
						orgIdParentIdMap.get(orgId), "RETAILER", 0, con);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("error in sale retailer");
		}
		//logger.debug(new Date() + "---end-dg sale update for retailer--"
				+ new Date().getTime());
	*/}
	
	private static void updateSLESaleRefundLedgerForAgt(Connection con) throws LMSException{
		Statement stmt = null;
		ResultSet rs = null;
		Map<String, List<Long>> transIdInvMap = null;
		Map<Integer, Double> orgIdAmountMap = new HashMap<Integer, Double>();
		Map<Integer, Integer> orgIdParentIdMap = new HashMap<Integer, Integer>();
		Map<Integer, Map<String, List<Long>>> orgIdTransIdInvRetMap = new HashMap<Integer, Map<String, List<Long>>>();
		Map<Integer, Map<String, List<Long>>> orgIdTransIdInvMap = new HashMap<Integer, Map<String, List<Long>>>();

		Map<String, List<Long>> transIdInvRetMap = null;
		Statement tempStmt=null;
		try {
			con.setAutoCommit(false);
			PreparedStatement pstmtAgt = con.prepareStatement(QueryManager.insertInLMSTransactionMaster());
			PreparedStatement pstmtAgtTrans = con.prepareStatement(QueryManager.insertInBOTransactionMaster());
			PreparedStatement insAgtSaleRetPstmt = con.prepareStatement("insert into st_sle_bo_sale_refund(transaction_id,agent_org_id,game_id,game_type_id,mrp_amt,agent_comm,net_amt,vat_amt,taxable_sale,govt_comm,cancellation_charges,transaction_date) values(?,?,?,?,?,?,?,?,?,?,?,?)");
			PreparedStatement insTempTablePstmt=con.prepareStatement("insert into agentTransSummary(bo_ref_transaction_id,agt_comm,govt_comm,agent_org_id,game_id,game_type_id,trans_date) values(?,?,?,?,?,?,?)");
			tempStmt=con.createStatement();
			tempStmt.execute("delete from agentTransSummary ");
			
			
			int count = 0;
			double retDebitAmount = 0.0;
			String saleReturnQry = "select a.game_type_id,a.game_id,agent_org_id,boUserId,boOrgId, totalMrp ,totalRetComm, totalagtComm, totalVat, totalTaxSale, totalCancelCharge,transaction_date,ret_comm,agt_comm,a.govt_comm,transaction_type,prize_payout_ratio,vat_amt from (select a.game_type_id,a.game_id,a.agent_org_id,boUserId,boOrgId,sum(mrp_amt) totalMrp ,sum(retailer_comm_amt) totalRetComm,sum(agent_comm_amt) totalagtComm, sum(vat_amt) totalVat,sum(taxable_sale) totalTaxSale,sum(cancellation_charges) totalCancelCharge,DATE(a.transaction_date) 'transaction_date',ret_comm,agt_comm,a.govt_comm,transaction_type from st_sle_agt_sale_refund a inner join  st_lms_agent_transaction_master b inner join st_temp_update_ledger c inner join (select um.user_id agtUserId,um.organization_id agtOrgId,um.parent_user_id boUserId,isrolehead,om.parent_id boOrgId from st_lms_organization_master om inner join st_lms_user_master um on om.organization_id=um.organization_id where om.organization_type='AGENT') d on a.transaction_id = b.transaction_id and a.transaction_id=trans_id and a.agent_org_id=agtOrgId and isrolehead = 'Y'  where claim_status='CLAIM_BAL' group by a.agent_org_id,DATE(transaction_date),a.game_type_id,transaction_type,ret_comm,agt_comm,a.govt_comm  order by agent_org_id,game_type_id,transaction_date) a inner join st_sle_game_type_master sle on sle.game_type_id  =  a.game_type_id"; 
			stmt = con.createStatement();
			rs = stmt.executeQuery(saleReturnQry);
			while (rs.next()) {
				int agtOrgId = rs.getInt("agent_org_id");
				int boUserId = rs.getInt("boUserId");
				int boOrgId = rs.getInt("boOrgId");
				int gameId  = 	rs.getInt("game_id");
				int gameTypeId  = 	rs.getInt("game_type_id");
				double totalMrpAmt = rs.getDouble("totalMrp");
				double totalRetComm = rs.getDouble("totalRetComm");
				double totalAgtComm = rs.getDouble("totalagtComm");
				double totalCancelCharge = rs.getDouble("totalCancelCharge");
				double retCommRate = rs.getDouble("ret_comm");
				double agtCommRate = rs.getDouble("agt_comm");
				double govtCommRate = rs.getDouble("govt_comm");
				String transDate = rs.getString("transaction_date");
				double totalretNetAmt = totalMrpAmt - totalRetComm	- totalCancelCharge;
				double totalAgtNetAmt = totalMrpAmt - totalAgtComm	- totalCancelCharge;
				String tranType = rs.getString("transaction_type");
				double totalAgtVat =rs.getDouble("totalVat");
				int ppr = rs.getInt("prize_payout_ratio");
				double vatAmt = rs.getDouble("vat_amt");
				double tatalAgtTaxableSale = CommonMethods.calTaxableSale(totalMrpAmt, retCommRate, ppr, govtCommRate, vatAmt);

				if (orgIdAmountMap.containsKey(agtOrgId)) {
					retDebitAmount = orgIdAmountMap.get(agtOrgId) - totalretNetAmt;
				} else {
					retDebitAmount = -totalretNetAmt;
				}

				orgIdAmountMap.put(agtOrgId, retDebitAmount);
				orgIdParentIdMap.put(agtOrgId, agtOrgId);

				if (orgIdTransIdInvRetMap.containsKey(agtOrgId)) {
					transIdInvRetMap = orgIdTransIdInvRetMap.get(agtOrgId);
				} else {
					transIdInvRetMap = new HashMap<String, List<Long>>();
					orgIdTransIdInvRetMap.put(agtOrgId, transIdInvRetMap);
				}

				// insert in main transaction table

				pstmtAgt.setString(1, "BO");
				pstmtAgt.executeUpdate();
				ResultSet rsTrns = pstmtAgt.getGeneratedKeys();
				if (rsTrns.next()) {
					long transId = rsTrns.getLong(1);
					logger.debug("--Agent org id"+agtOrgId+"--Bo org Id--"+boOrgId+"--Start Sale Refund tran for Game type "+gameTypeId +" with transaction "+ transId);
					if (!transIdInvRetMap.containsKey(transDate)) {
						transIdInvRetMap.put(transDate ,	new ArrayList<Long>());
					}

					transIdInvRetMap.get(transDate).add(transId);

					pstmtAgtTrans.setLong(1, transId);
					pstmtAgtTrans.setInt(2, boUserId);
					pstmtAgtTrans.setInt(3, boOrgId);
					pstmtAgtTrans.setString(4, "AGENT");
					pstmtAgtTrans.setInt(5, agtOrgId);
					pstmtAgtTrans.setTimestamp(6,updateLedgerHelper.fetchTransTimeStamp(transDate));
					pstmtAgtTrans.setString(7, tranType);
					pstmtAgtTrans.executeUpdate();

					insAgtSaleRetPstmt.setLong(1, transId);
					insAgtSaleRetPstmt.setInt(2, agtOrgId);
					insAgtSaleRetPstmt.setInt(3, gameId);
					insAgtSaleRetPstmt.setInt(4, gameTypeId);
					// ADD GAME TYPE ID FOR AGENT
					insAgtSaleRetPstmt.setDouble(5, totalMrpAmt);
					insAgtSaleRetPstmt.setDouble(6, totalAgtComm);
					insAgtSaleRetPstmt.setDouble(7, totalAgtNetAmt);
					insAgtSaleRetPstmt.setDouble(8, totalAgtVat);
					insAgtSaleRetPstmt.setDouble(9, tatalAgtTaxableSale);
					insAgtSaleRetPstmt.setDouble(10, govtCommRate);
					insAgtSaleRetPstmt.setDouble(11, totalCancelCharge);
					insAgtSaleRetPstmt.setTimestamp(12, updateLedgerHelper.fetchTransTimeStamp(transDate));
					insAgtSaleRetPstmt.executeUpdate();

					
					//insert temp table
					insTempTablePstmt.setLong(1, transId);
					insTempTablePstmt.setDouble(2, agtCommRate);
					insTempTablePstmt.setDouble(3, govtCommRate);
					insTempTablePstmt.setInt(4, agtOrgId);
					insTempTablePstmt.setInt(5, gameId);
					insTempTablePstmt.setInt(6, gameTypeId);
					insTempTablePstmt.setString(7, transDate);
					insTempTablePstmt.addBatch();
					if(++count % batchSize == 0) {
						insTempTablePstmt.executeBatch();
				    }
				}
			}
			
			insTempTablePstmt.executeBatch();
			// BE CARE FULL FOR THE GAME_TYPE_ID   
			//tempStmt.executeUpdate("update st_dg_ret_sale_refund sale inner join (select yy.transaction_id,xx.agent_ref_transaction_id from orgTransSummary xx inner join (select transaction_id,transaction_date,retailer_org_id,ret_comm,agt_comm,govt_comm,game_id from st_lms_retailer_transaction_master a inner join st_temp_update_ledger b on a.transaction_id = b.trans_id)yy on DATE(trans_date)=DATE(transaction_date) and xx.retailer_org_id=yy.retailer_org_id and xx.ret_comm=yy.ret_comm and xx.agt_comm=yy.agt_comm and xx.govt_comm=yy.govt_comm and xx.game_id=yy.game_id) tlb on sale.transaction_id=tlb.transaction_id set sale.claim_status='DONE_CLAIM',sale.agent_ref_transaction_id=tlb.agent_ref_transaction_id");
			//logger.debug(tempStmt.executeUpdate("update st_sle_ret_sale_refund sale inner join (select yy.transaction_id,xx.agent_ref_transaction_id from orgTransSummary xx inner join (select transaction_id,transaction_date,retailer_org_id,ret_comm,agt_comm,govt_comm,game_id from st_lms_retailer_transaction_master a inner join st_temp_update_ledger b on a.transaction_id = b.trans_id)yy on DATE(trans_date)=DATE(transaction_date) and xx.retailer_org_id=yy.retailer_org_id and xx.ret_comm=yy.ret_comm and xx.agt_comm=yy.agt_comm and xx.govt_comm=yy.govt_comm and xx.game_type_id=yy.game_id) tlb on sale.transaction_id=tlb.transaction_id set sale.claim_status='DONE_CLAIM',sale.agent_ref_transaction_id=tlb.agent_ref_transaction_id"));
			
			System.out.println(tempStmt.executeUpdate("update st_sle_agt_sale_refund sale inner join (select yy.transaction_id,xx.bo_ref_transaction_id from agentTransSummary xx inner join (select a.transaction_id,a.transaction_date,a.user_org_id agent_org_id,ret_comm,agt_comm,c.govt_comm,c.game_id,c.game_type_id from st_lms_agent_transaction_master a inner join st_temp_update_ledger b inner join st_sle_agt_sale_refund c on a.transaction_id = b.trans_id and b.trans_id = c.transaction_id )yy on DATE(trans_date)=DATE(transaction_date) and xx.agent_org_id=yy.agent_org_id and xx.agt_comm=yy.agt_comm and xx.govt_comm=yy.govt_comm and xx.game_type_id=yy.game_type_id ) tlb on sale.transaction_id=tlb.transaction_id set sale.claim_status='DONE_CLAIM',sale.bo_ref_transaction_id=tlb.bo_ref_transaction_id"));
			con.commit();
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("error in sale refund Agent");
		}
	}

}
