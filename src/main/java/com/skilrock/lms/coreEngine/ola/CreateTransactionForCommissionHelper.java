package com.skilrock.lms.coreEngine.ola;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.OrgCreditUpdation;
import com.skilrock.lms.coreEngine.drawGames.common.CommonFunctionsHelper;

public class CreateTransactionForCommissionHelper {
	static Log logger = LogFactory.getLog(CreateTransactionForCommissionHelper.class);
	
	public static void main(String[] args) throws LMSException, java.text.ParseException{
		Calendar calStart=Calendar.getInstance();
		calStart.add(Calendar.DAY_OF_MONTH, -7);
		Date startDate=new Date(calStart.getTime().getTime());
		System.out.println("start_date:"+startDate);
		
		calStart.add(Calendar.DAY_OF_MONTH,6);
		Date endDate=new Date(calStart.getTime().getTime());
		
		System.out.println("end_date:"+endDate);
		retOlaCommissionTransaction(startDate,endDate, "WEEKLY", "AUTO");
	}
	
	
	
	public static String retOlaCommissionAgentWiseTransaction(int walletId,int orgId[],Date startDate,Date endDate,String updateType,String updateMode) throws LMSException{
		Connection con = null;
		PreparedStatement walletPstmt =null;
		ResultSet rsWallet =null;
		StringBuilder orgIds = new StringBuilder();
		StringBuilder netGamingExpResp = new StringBuilder();
		for (int i = 0; i < orgId.length; i++) {
			orgIds.append(orgId[i] + ",");
		}
		orgIds.deleteCharAt(orgIds.length() - 1);
		
		try{
			con =DBConnect.getConnection();
			con.setAutoCommit(false);
			String walletQry = "select wallet_id, wallet_name,tds_comm_rate from st_ola_wallet_master where wallet_status='ACTIVE' and wallet_id="+walletId;
			walletPstmt = con.prepareStatement(walletQry);
			rsWallet = walletPstmt.executeQuery();
			if(rsWallet.next()){
				// int walletId = rsWallet.getInt("wallet_id");
				double tds_comm_rate=rsWallet.getDouble("tds_comm_rate");
			if ("WEEKLY".equalsIgnoreCase(updateType)) {
				String selectCommWeekely="select id,wallet_id,user_id,retailer_org_id,ret_comm_amt,agt_comm_amt,parent_id from st_ola_retailer_weekly_commission_exp rwc , st_lms_user_master um,st_lms_organization_master om where rwc.retailer_org_id=um.organization_id and um.organization_type='RETAILER' and rwc.date>=? and rwc.date <=? and wallet_id=? and  parent_id in("+orgIds+") and  um.organization_id=om.organization_id and rwc.status='PENDING' ";
				PreparedStatement pstmt=con.prepareStatement(selectCommWeekely);
				pstmt.setDate(1,startDate);
				pstmt.setDate(2,endDate);
				pstmt.setInt(3,walletId);
				ResultSet rsComm=pstmt.executeQuery();
			while(rsComm.next()){
				int id=rsComm.getInt("id");
				int userId=rsComm.getInt("user_id");
				int userOrgId=rsComm.getInt("retailer_org_id");
				int parentOrgId =rsComm.getInt("parent_id");
				double retailerComm=rsComm.getDouble("ret_comm_amt");
				double agentComm=rsComm.getDouble("agt_comm_amt");
				retOlaNetGamingPayment(userId,userOrgId,parentOrgId,walletId,retailerComm,tds_comm_rate,agentComm,updateType,updateMode,id,con);
						
			}
				}
			else if("MONTHLY".equalsIgnoreCase(updateType)){
				String selectCommWeekely="select id,wallet_id,user_id,retailer_org_id,ret_comm_amt,agt_comm_amt,parent_id from st_ola_retailer_monthly_commission_exp rwc , st_lms_user_master um,st_lms_organization_master om where rwc.retailer_org_id=um.organization_id and um.organization_type='RETAILER' and rwc.date>=? and rwc.date <=? and wallet_id=? and   parent_id in("+orgIds+") and um.organization_id=om.organization_id and rwc.status='PENDING' ";
				PreparedStatement pstmt=con.prepareStatement(selectCommWeekely);
				pstmt.setDate(1,startDate);
				pstmt.setDate(2,endDate);
				pstmt.setInt(3,walletId);
				ResultSet rsComm=pstmt.executeQuery();
			while(rsComm.next()){
				int id=rsComm.getInt("id");
				int userId=rsComm.getInt("user_id");
				int userOrgId=rsComm.getInt("retailer_org_id");
				int parentOrgId =rsComm.getInt("parent_id");
				double retailerComm=rsComm.getDouble("ret_comm_amt");
				double agentComm=rsComm.getDouble("agt_comm_amt");
				retOlaNetGamingPayment(userId,userOrgId,parentOrgId,walletId,retailerComm,tds_comm_rate,agentComm,updateType,updateMode,id,con);
				}
			}
			Statement statement=con.createStatement();
			String selectQry = "select parent_id,status,updated_date from st_ola_retailer_"+updateType.toLowerCase()+"_commission_exp we,st_lms_organization_master om where we.retailer_org_id=om.organization_id and om.parent_id in("+orgIds+") and we.date>='"+startDate+"' and we.date <='"+endDate+"' and we.status='DONE' group by parent_id ";
		 ResultSet rs = statement.executeQuery(selectQry);
		while (rs.next()) {
			netGamingExpResp.append(rs.getInt("parent_id")
					+ ","
					+ rs.getString("status")
					+ ","
					
					+ rs.getString("updated_date").substring(0,
							rs.getString("updated_date").lastIndexOf('.'))
					+ "Nxt");
		}
		logger.info(netGamingExpResp);
		
			con.commit();
			
		}else{
			logger.info("wallet Does not exists "+walletId);
		}
			
		}catch (SQLException e) {
			logger.error("SQL Exception ",e);
		}catch (Exception e) {
			logger.error("Exception ",e);
		}finally{
			DBConnect.closeCon(con);
			DBConnect.closePstmt(walletPstmt);
			DBConnect.closeRs(rsWallet);
			
		}
		return netGamingExpResp.toString();
	}
	
	
	
	
	public static void retOlaCommissionTransaction(Date startDate,Date endDate,String updateType,String updateMode) throws LMSException{
		Connection con = null;
		PreparedStatement walletPstmt =null;
		ResultSet rsWallet =null;
		try{
			con =DBConnect.getConnection();
		
			String walletQry = "select wallet_id, wallet_name,tds_comm_rate from st_ola_wallet_master where wallet_status='ACTIVE'";
			walletPstmt	 = con.prepareStatement(walletQry);
			rsWallet = walletPstmt.executeQuery();
			con.setAutoCommit(false);
			while(rsWallet.next()){
				int walletId = rsWallet.getInt("wallet_id");
				double tds_comm_rate=rsWallet.getDouble("tds_comm_rate");
				retOlaCommission( walletId, updateType, updateMode, tds_comm_rate, startDate, endDate, con);
				
				
				}
			con.commit();
		}catch (SQLException e) {
			logger.error("SQL Exception ",e);
		}catch (Exception e) {
			logger.error("Exception ",e);
		}finally{
			DBConnect.closeCon(con);
			DBConnect.closePstmt(walletPstmt);
			DBConnect.closeRs(rsWallet);
			
		}
	}
	public static void retOlaCommissionTransaction(int walletId,Date startDate,Date endDate,String updateType,String updateMode) throws LMSException{
		Connection con = null;
		PreparedStatement walletPstmt =null;
		ResultSet rsWallet=null;
		try{
			con =DBConnect.getConnection();
			String walletQry = "select wallet_id, wallet_name,tds_comm_rate from st_ola_wallet_master where wallet_status='ACTIVE' and wallet_id="+walletId;
			walletPstmt = con.prepareStatement(walletQry);
			rsWallet = walletPstmt.executeQuery();
			con.setAutoCommit(false);
			if(rsWallet.next()){
				double tds_comm_rate=rsWallet.getDouble("tds_comm_rate");
				retOlaCommission( walletId, updateType, updateMode, tds_comm_rate, startDate, endDate, con);
				con.commit();
				}else {
					logger.info("wallet Does not exists "+walletId);
					
				}
			
		}catch (SQLException e) {
			logger.error("SQL Exception ",e);
		}catch (Exception e) {
			logger.error("Exception ",e);
		}finally{
			DBConnect.closeCon(con);
			DBConnect.closePstmt(walletPstmt);
			DBConnect.closeRs(rsWallet);
			
		}
	}
	public static void retOlaCommission(int walletId,String updateType,String updateMode,double tds_comm_rate,Date startDate,Date endDate,Connection con){
		PreparedStatement pstmt=null;
		ResultSet rsComm=null;
		try{
			if ("WEEKLY".equalsIgnoreCase(updateType)) {
				String selectCommWeekely="select id,wallet_id,user_id,retailer_org_id,ret_comm_amt,agt_comm_amt,parent_id from st_ola_retailer_weekly_commission_exp rwc , st_lms_user_master um,st_lms_organization_master om where rwc.retailer_org_id=um.organization_id and um.organization_type='RETAILER' and rwc.date>=? and rwc.date <=? and wallet_id=? and um.organization_id=om.organization_id and rwc.status='PENDING'";
				pstmt=con.prepareStatement(selectCommWeekely);
				pstmt.setDate(1,startDate);
				pstmt.setDate(2,endDate);
				pstmt.setInt(3,walletId);
				rsComm=pstmt.executeQuery();
			while(rsComm.next()){
				int id=rsComm.getInt("id");
				int userId=rsComm.getInt("user_id");
				int userOrgId=rsComm.getInt("retailer_org_id");
				int parentOrgId =rsComm.getInt("parent_id");
				double retailerComm=rsComm.getDouble("ret_comm_amt");
				double agentComm=rsComm.getDouble("agt_comm_amt");
				retOlaNetGamingPayment(userId,userOrgId,parentOrgId,walletId,retailerComm,tds_comm_rate,agentComm,updateType,updateMode,id,con);
						
			}
				}
			else if("MONTHLY".equalsIgnoreCase(updateType)){
				String selectCommWeekely="select id,wallet_id,user_id,retailer_org_id,ret_comm_amt,agt_comm_amt,parent_id from st_ola_retailer_monthly_commission_exp rwc , st_lms_user_master um,st_lms_organization_master om where rwc.retailer_org_id=um.organization_id and um.organization_type='RETAILER' and rwc.date>=? and rwc.date <=? and wallet_id=? and um.organization_id=om.organization_id and rwc.status='PENDING'";
				pstmt=con.prepareStatement(selectCommWeekely);
				pstmt.setDate(1,startDate);
				pstmt.setDate(2,endDate);
				pstmt.setInt(3,walletId);
				rsComm=pstmt.executeQuery();
			while(rsComm.next()){
				int id=rsComm.getInt("id");
				int userId=rsComm.getInt("user_id");
				int userOrgId=rsComm.getInt("retailer_org_id");
				int parentOrgId =rsComm.getInt("parent_id");
				double retailerComm=rsComm.getDouble("ret_comm_amt");
				double agentComm=rsComm.getDouble("agt_comm_amt");
				retOlaNetGamingPayment(userId,userOrgId,parentOrgId,walletId,retailerComm,tds_comm_rate,agentComm,updateType,updateMode,id,con);
				}
			}
		}catch(SQLException e){
			logger.error("SQL Exception",e);
		}catch(Exception e){
			logger.error(" Exception",e);
		}finally{
			DBConnect.closeRs(rsComm);
			DBConnect.closePstmt(pstmt);
		}
		
		
		
	}
	
	public void retOlaCommissionPayment(){
			
	}
	
	public static double fmtToTwoDecimal(double number) {
		return Math.round((number * 100)) / 100.0;

	}
	public static int   retOlaNetGamingPayment(int userId, int userOrgId, int parentOrgId
			,int walletId, double retCommAmt,double tds_comm_rate,double agtCommAmt,String updateType,String updateMode,int generatedId,
			Connection connection) throws LMSException {
		
		try {
			double netRetailerCommAmt=retCommAmt-(retCommAmt*tds_comm_rate*.01);
			Double fmtRetCommAmt=fmtToTwoDecimal(retCommAmt);
			Double fmtNetRetCommAmt=fmtToTwoDecimal(netRetailerCommAmt);
			
			
			double netAgentCommAmt=agtCommAmt-(agtCommAmt*tds_comm_rate*.01);
			Double fmtAgtCommAmt=fmtToTwoDecimal(agtCommAmt);
			Double fmtNetAgtCommAmt=fmtToTwoDecimal(netAgentCommAmt);
			// insert data into main transaction master
			logger.debug("insert data into transaction master ");
			String transMasQuery ="INSERT INTO st_lms_transaction_master (user_type, service_code, interface) VALUES (?,?,?)";
			PreparedStatement pstmt = connection
					.prepareStatement(transMasQuery);
			pstmt.setString(1, "RETAILER");
			pstmt.setString(2, "OLA");
			pstmt.setString(3, "WEB");
			pstmt.executeUpdate();
			ResultSet rs = pstmt.getGeneratedKeys();
            PreparedStatement pstmt1=null;
			if (rs.next()) {
				int transId = rs.getInt(1);
				int isUpdate = 0;
				//CommonFunctionsHelper commHelper = new CommonFunctionsHelper();
				// insert into retailer transaction master
				String retTransMasterQuery = "insert into  st_lms_retailer_transaction_master ( transaction_id , retailer_user_id , retailer_org_id ,game_id, transaction_date , transaction_type ) values (?,?,?,?,?,?)";
				logger.debug("retTransMasterQuery = " + retTransMasterQuery);
				pstmt = connection.prepareStatement(retTransMasterQuery);
				pstmt.setInt(1, transId);
				pstmt.setInt(2, userId);
				pstmt.setInt(3, userOrgId);
				pstmt.setInt(4, walletId);
				pstmt.setTimestamp(5, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				pstmt.setString(6, "OLA_COMMISSION");
				pstmt.executeUpdate();
				logger.debug("insert into retailer transaction master = "
						+ pstmt);

				/*// fetch Agent And Retailer PWT commission
				double agtComm = CommonFunctionsHelper
						.fetchDGCommOfOrganization(gameId, parentOrgId, "PWT",
								"AGENT", connection);
				double retComm = CommonFunctionsHelper
						.fetchDGCommOfOrganization(gameId, userOrgId, "PWT",
								"RETAILER", connection);*/

				// insert into st_retailer_pwt when comes pwtAmt<Aproval
				// required
				String retOlaCommEntry = "insert into st_ola_ret_comm(transaction_id,wallet_id,retailer_user_id,retailer_org_id,retailer_claim_comm,tds_comm_rate,retailer_net_claim_comm,agt_claim_comm,agt_net_claim_comm,status) " 
					              +	" values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				pstmt = connection.prepareStatement(retOlaCommEntry);
				
				pstmt.setInt(1, transId);
				pstmt.setInt(2,walletId);
				pstmt.setInt(3, userId);
				pstmt.setInt(4, userOrgId);
				pstmt.setDouble(5,fmtRetCommAmt);
				pstmt.setDouble(6,tds_comm_rate);
				pstmt.setDouble(7,fmtNetRetCommAmt);
				pstmt.setDouble(8,fmtAgtCommAmt);
				pstmt.setDouble(9,fmtNetAgtCommAmt);
				pstmt.setString(10, "CLAIM_BAL");
				
				isUpdate=pstmt.executeUpdate();
				logger.debug("insert into st_dg_ret_pwt = " + pstmt);

				/*// insert ticket details into st_dg_pwt_inv_? table
				String insIntoDGPwtInvQuery = "insert into st_dg_pwt_inv_?(ticket_nbr, draw_id, pwt_amt, "
						+ " status, retailer_transaction_id, is_direct_plr, panel_id) values (?, ?, ?, ?, ?, ?, ?)";
				PreparedStatement insIntoDGPwtInvPstmt = connection
						.prepareStatement(insIntoDGPwtInvQuery);
				insIntoDGPwtInvPstmt.setInt(1, gameNbr);
				insIntoDGPwtInvPstmt.setString(2, ticketNbr);
				insIntoDGPwtInvPstmt.setInt(3, drawId);
				insIntoDGPwtInvPstmt.setDouble(4, fmtPwtAmt);
				if (isAgent) {
					insIntoDGPwtInvPstmt.setString(5, "CLAIM_AGT");
				} else {
					insIntoDGPwtInvPstmt.setString(5,
							isAutoScrap ? "CLAIM_PLR_RET_CLM"
									: "CLAIM_PLR_RET_UNCLM");
				}
				insIntoDGPwtInvPstmt.setInt(6, transId);
				insIntoDGPwtInvPstmt.setString(7, "N");
				insIntoDGPwtInvPstmt.setObject(8, panelId);
				insIntoDGPwtInvPstmt.executeUpdate();
				logger.debug("insIntoDGPwtInvPstmt = " + insIntoDGPwtInvPstmt);*/

				// update retailer UNCLAIM_BAL payment
				
				
				//Now make payment updte method only one
				OrgCreditUpdation.updateOrganizationBalWithValidate(fmtNetRetCommAmt, "CLAIM_BAL", "DEBIT", userOrgId, parentOrgId, "RETAILER", 0, connection);
				
				OrgCreditUpdation.updateOrganizationBalWithValidate(fmtNetAgtCommAmt, "CLAIM_BAL", "DEBIT", parentOrgId,0, "AGENT", 0, connection);
				
					/*// now retailer claimable balance DEBITED
					commHelper.updateOrgBalance("CLAIM_BAL", fmtNetRetCommAmt, userOrgId, "DEBIT", connection);
					// agent claimable balance DEBITED
					commHelper.updateOrgBalance("CLAIM_BAL",fmtNetAgtCommAmt, parentOrgId, "DEBIT", connection);*/
					System.out.println("isUpdate" + isUpdate);
					if (isUpdate == 1 && updateType.equalsIgnoreCase("WEEKLY")) {

						//String updateRetWeekltComm="update st_ola_retailer_weekly_commission_exp set status='DONE',refTransactionId = "+ transId+ ", updated_date='"+ new Timestamp(new java.util.Date().getTime())+"' , updated_mode='AUTO' where id="+generatedId;
						String updateRetWeekltComm="update st_ola_retailer_weekly_commission_exp set status='DONE',refTransactionId = "+ transId+ ", updated_date='"+new java.sql.Timestamp(new java.util.Date().getTime())+"' , updated_mode='"+updateMode+"' where id="+generatedId;
						pstmt1 = connection.prepareStatement(updateRetWeekltComm);
						
						pstmt1.executeUpdate();
					}
					else if(isUpdate == 1 && updateType.equalsIgnoreCase("MONTHLY"))
					{
						pstmt1 = connection.prepareStatement("update st_ola_retailer_monthly_commission_exp set status='DONE',refTransactionId = "+ transId+ ", updated_date='"+ new java.sql.Timestamp(new java.util.Date().getTime())+"' , updated_mode='"+updateMode+"' where id="+generatedId+"");
						pstmt1.executeUpdate();
					}
				// receipt entries are required to be inserted into receipt
				// table
				return transId;

			} else {
				throw new LMSException(
						"no data insert into main transaction master");
			}

		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException(e);
		}

	}
	
	
	
}
