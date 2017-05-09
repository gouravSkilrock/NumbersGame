package com.skilrock.lms.coreEngine.ola.reportMgmt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.OlaReportBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.coreEngine.ola.common.CommonFunctionsHelper;

public class OlaRetailerReportHelper {
Log logger  = LogFactory.getLog(OlaRetailerReportHelper.class);
	public List<OlaReportBean> fetchOlaRetailerReportData(
			OlaReportBean olaReportBean, int walletId, int retOrgId,String playerType) {
		List<OlaReportBean> olaReportList = new ArrayList<OlaReportBean>();
		Connection con=null;
		try {
			double depositAmt = 0.0;
			double withdrawlAmt = 0.0;
			
			double totalDepositAmount = 0.0;
			double totalWithdrawlAmount = 0.0;
			/*double retailerDepositComm = 0.0;
			double retailerWithdrawlComm = 0.0;
			double totalDepositCommission = 0.0;
			double totalWithdrawlCommission = 0.0;
			double totalPlayerNetGaming = 0.0;
			double totalComm = 0.0;
			double totalCommissionCalculated = 0.0;*/
			//DecimalFormat df = new DecimalFormat("##.## ");
			 con= DBConnect.getConnection();
			String startDate = olaReportBean.getFromDate();
			String endDate = olaReportBean.getToDate();
			con.setAutoCommit(false);
			// double commPer = CommonFunctionsHelper.fetchOLACommOfOrganization(walletId, retOrgId, "NETGAMING", "RETAILER", con);
			String subQuery=" ";
			if(playerType.equalsIgnoreCase("Bind")){
				subQuery =" where player_id is not null ";
			}else if(playerType.equalsIgnoreCase("Notbind")) {
				subQuery=" where player_id is null  ";
			}
			
		
			StringBuilder sbQuery = new StringBuilder();
									sbQuery.append(" select sum(depositAmt) depositAmt,sum(withdrawlAmt) withdrawlAmt,party_id,transaction_type,Date(transaction_date) transaction_date,player_id from (");
									sbQuery.append(" select 0.0 depositAmt,withdrawlAmt,party_id,transaction_type,transaction_date from (	select  sum(withdrawl_amt) withdrawlAmt,0.0 refundAmt,party_id,transaction_type,transaction_date");
									sbQuery.append(" from st_lms_retailer_transaction_master rtm  inner join st_ola_ret_withdrawl wl on rtm.transaction_id=wl.transaction_id where rtm.transaction_type='OLA_WITHDRAWL'");
									sbQuery.append(" and wallet_id=? and wl.retailer_org_id=? and transaction_date>=? and transaction_date<=? group by party_id ");
									sbQuery.append(" union all select 0.0 	withdrawlAmt,sum(withdrawl_amt) refundAmt,party_id,transaction_type,transaction_date from 	 st_lms_retailer_transaction_master rtm");
									sbQuery.append("  inner join st_ola_ret_withdrawl_refund wlr on rtm.transaction_id=wlr.transaction_id	where rtm.transaction_type='OLA_WITHDRAWL_REFUND' and wallet_id=? and wlr.retailer_org_id=? and");
									sbQuery.append("  transaction_date>=?  and transaction_date<=? 	group by party_id )withdrawl group by party_id ");
									sbQuery.append("  union all	select sum(depositAmt- refundAmt) depositAmt,0.0 withdrawlAmt,party_id,transaction_type,transaction_date from (select sum(deposit_amt) depositAmt,0.0 refundAmt,party_id,transaction_type,transaction_date ");
									sbQuery.append("   from st_lms_retailer_transaction_master rtm	inner join st_ola_ret_deposit dp on rtm.transaction_id=dp.transaction_id  where rtm.transaction_type='OLA_DEPOSIT' and wallet_id=? and dp.retailer_org_id=? ");
									sbQuery.append("  and transaction_date>=?  and transaction_date<=?  group by party_id union all select 0.0  depositAmt,sum(deposit_amt) refundAmt,party_id,transaction_type,transaction_date from");
									sbQuery.append("  st_lms_retailer_transaction_master rtm inner join st_ola_ret_deposit_refund dpr on rtm.transaction_id=dpr.transaction_id 	  where rtm.transaction_type='OLA_DEPOSIT_REFUND' and wallet_id=? and dpr.retailer_org_id=? and");
									sbQuery.append(" transaction_date>=?  and transaction_date<=?	group by party_id )olaDeposit group by party_id	)ola left join (select player_id from st_ola_affiliate_plr_mapping  where ref_user_org_id=? and wallet_id=?)apm  on ola.party_id =apm.player_id ");
									sbQuery.append(subQuery);
									sbQuery.append(" group by party_id");
									
								

	
			PreparedStatement pstmt = con.prepareStatement(sbQuery.toString());
			pstmt.setInt(1,walletId);
			pstmt.setInt(2,retOrgId);
			pstmt.setString(3,startDate);
			pstmt.setString(4,endDate);
			pstmt.setInt(5,walletId);
			pstmt.setInt(6,retOrgId);
			pstmt.setString(7,startDate);
			pstmt.setString(8,endDate);
			pstmt.setInt(9,walletId);
			pstmt.setInt(10,retOrgId);
			pstmt.setString(11,startDate);
			pstmt.setString(12,endDate);
			pstmt.setInt(13,walletId);
			pstmt.setInt(14,retOrgId);
			pstmt.setString(15,startDate);
			pstmt.setString(16,endDate);
			pstmt.setInt(17,retOrgId);
			pstmt.setInt(18,walletId);
			System.out.println(pstmt);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				olaReportBean = new OlaReportBean();
				String playerName = rs.getString("party_id");
				olaReportBean.setPlayerName(playerName);
				depositAmt = rs.getDouble("depositAmt");
				olaReportBean.setDepositAmt(depositAmt);
				totalDepositAmount = totalDepositAmount + depositAmt;
				withdrawlAmt = rs.getDouble("withdrawlAmt");
				olaReportBean.setWithdrawlAmt(withdrawlAmt);
				if(rs.getString("player_id")==null){
					olaReportBean.setPlayerType("Not Bind");
				}else{
					olaReportBean.setPlayerType("Bind");
				}
				olaReportBean.setTrnDate(rs.getString("transaction_date"));
				totalWithdrawlAmount = totalWithdrawlAmount + withdrawlAmt;
			/*	retailerDepositComm = Double.parseDouble(df.format(depositAmt
						* (rs.getDouble("deposit_retailer_comm")) * .01));
				olaReportBean.setRetailerDepositCommission(retailerDepositComm);*/
				/*totalDepositCommission = Double.parseDouble(df
						.format(totalDepositCommission + retailerDepositComm));
				retailerWithdrawlComm = Double.parseDouble(df
						.format(withdrawlAmt
								* (rs.getDouble("withdrawl_retailer_comm"))
								* .01));
				olaReportBean
						.setRetailerWithdrawlCommission(retailerWithdrawlComm);
				totalWithdrawlCommission = Double.parseDouble(df
						.format(totalWithdrawlCommission
								+ retailerWithdrawlComm));
				totalComm = commPer*(rs.getDouble("totalPlrNetGaming"))*.01;
				totalPlayerNetGaming = totalPlayerNetGaming + rs.getDouble("totalPlrNetGaming");
				totalCommissionCalculated = totalCommissionCalculated + totalComm;
				olaReportBean.setRetailerNetGaming(rs.getDouble("totalPlrNetGaming"));
				olaReportBean.setCommissionCalculated(totalComm);*/
				olaReportList.add(olaReportBean);
			}

			OlaReportBean totalBean = new OlaReportBean();
			totalBean.setPlayerName("Total");
			totalBean.setDepositAmt(totalDepositAmount);
			//totalBean.setRetailerDepositCommission(totalDepositCommission);
			totalBean.setWithdrawlAmt(totalWithdrawlAmount);
		//	totalBean.setRetailerWithdrawlCommission(totalWithdrawlCommission);
		//	totalBean.setRetailerNetGaming(totalPlayerNetGaming);
			//totalBean.setCommissionCalculated(totalCommissionCalculated);
			olaReportList.add(totalBean);

			con.commit();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			DBConnect.closeCon(con);
		}
		return olaReportList;
	}
	
	public String fetchOlaRetailerReportDataConsolidate(
			String  fromDate, int walletId, int retOrgId) {
		
		Connection con=null;
		StringBuilder sb= new StringBuilder();
		try {
		

			String startDate =null;
			String endDate =null;
			con= DBConnect.getConnection();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		 if(fromDate.equalsIgnoreCase("lastday")){
				 Calendar cal = Calendar.getInstance();
				 cal.add(Calendar.DATE, -1);
				 startDate = (sdf.format(cal.getTime()))+" 00:00:00";
				 endDate = (sdf.format(cal.getTime()))+" 23:59:59";
				 
			 }else if(fromDate.equalsIgnoreCase("currentday")){
				 Calendar cal = Calendar.getInstance();
				 startDate = (sdf.format(cal.getTime()))+" 00:00:00";
				 endDate = (sdf.format(cal.getTime()))+" 23:59:59";
			 }else {
				 Calendar cal = Calendar.getInstance();
				 startDate = (sdf.format(sdf.parse(fromDate)))+" 00:00:00";
				 endDate = (sdf.format(cal.getTime()))+" 23:59:59";
			 }
			StringBuilder sbQuery = new StringBuilder();
									sbQuery.append(" select sum(depositAmt) depositAmt,sum(withdrawlAmt) withdrawlAmt,transactionDate from ( ");
									sbQuery.append("select 0.0 depositAmt,withdrawlAmt,transactionDate  from ( select  sum(withdrawl_amt) withdrawlAmt,0.0 refundAmt,transaction_type,Date(transaction_date) transactionDate  ");
									sbQuery.append(" from st_lms_retailer_transaction_master rtm  inner join st_ola_ret_withdrawl wl on rtm.transaction_id=wl.transaction_id where rtm.transaction_type='OLA_WITHDRAWL' and wallet_id=? and wl.retailer_org_id=? and transaction_date>=?  and transaction_date<=?  group by transactionDate");
									sbQuery.append(" union all select 0.0 	withdrawlAmt,sum(withdrawl_amt) refundAmt,transaction_type,Date(transaction_date) transactionDate ");
									sbQuery.append(" from st_lms_retailer_transaction_master rtm  inner join st_ola_ret_withdrawl_refund wlr on rtm.transaction_id=wlr.transaction_id	where rtm.transaction_type='OLA_WITHDRAWL_REFUND' and wallet_id=? and wlr.retailer_org_id=? and  transaction_date>=?  and transaction_date<=? group by transactionDate  )withdrawl ");
									sbQuery.append(" union all	select sum(depositAmt- refundAmt) depositAmt,0.0 withdrawlAmt,transactionDate from (select sum(deposit_amt) depositAmt,0.0 refundAmt,Date(transaction_date) transactionDate  ");
									sbQuery.append("  from st_lms_retailer_transaction_master rtm	inner join st_ola_ret_deposit dp on rtm.transaction_id=dp.transaction_id  where rtm.transaction_type='OLA_DEPOSIT' and wallet_id=? and dp.retailer_org_id=?   and transaction_date>=?  and transaction_date<=? group by transactionDate");
									sbQuery.append("  union all	select 0.0  depositAmt,sum(deposit_amt) refundAmt,Date(transaction_date) transactionDate  from  st_lms_retailer_transaction_master rtm inner join st_ola_ret_deposit_refund dpr on rtm.transaction_id=dpr.transaction_id 	 ");
									sbQuery.append(" where rtm.transaction_type='OLA_DEPOSIT_REFUND' and wallet_id=? and dpr.retailer_org_id=? and transaction_date>=?  and transaction_date<=? group by transactionDate ");
									sbQuery.append(")olaDeposit  group by transactionDate	)ola group by transactionDate");
									
								

	
			PreparedStatement pstmt = con.prepareStatement(sbQuery.toString());
			pstmt.setInt(1,walletId);
			pstmt.setInt(2,retOrgId);
			pstmt.setString(3,startDate);
			pstmt.setString(4,endDate);
			pstmt.setInt(5,walletId);
			pstmt.setInt(6,retOrgId);
			pstmt.setString(7,startDate);
			pstmt.setString(8,endDate);
			pstmt.setInt(9,walletId);
			pstmt.setInt(10,retOrgId);
			pstmt.setString(11,startDate);
			pstmt.setString(12,endDate);
			pstmt.setInt(13,walletId);
			pstmt.setInt(14,retOrgId);
			pstmt.setString(15,startDate);
			pstmt.setString(16,endDate);
			logger.info(pstmt);
			ResultSet rs = pstmt.executeQuery();
			
			
			while (rs.next()) {
			
					sb.append(rs.getString("transactionDate")+"*");
					sb.append(rs.getString("depositAmt")+",");
					sb.append(rs.getString("withdrawlAmt"));
					sb.append("|");
					
				
				
				
			}

			logger.info("response data:"+ sb.toString());
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			DBConnect.closeCon(con);
		}
		return sb.toString() ;
	}

	
}
