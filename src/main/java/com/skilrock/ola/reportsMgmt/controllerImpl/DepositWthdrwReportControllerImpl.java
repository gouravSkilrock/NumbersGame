package com.skilrock.ola.reportsMgmt.controllerImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.OlaAgentReportBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.GenericException;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.coreEngine.reportsMgmt.common.OrganizationTerminateReportHelper;
import com.skilrock.lms.rolemgmt.beans.userBean;
import com.skilrock.ola.commonMethods.controllerImpl.OlaCommonMethodControllerImpl;
import com.skilrock.ola.reportsMgmt.javaBeans.OlaOrgReportRequestBean;
import com.skilrock.ola.reportsMgmt.javaBeans.OlaOrgReportResponseBean;
import com.skilrock.ola.reportsMgmt.javaBeans.OlaReportBean;


public class DepositWthdrwReportControllerImpl  {
	
	static Log logger = LogFactory.getLog(DepositWthdrwReportControllerImpl.class);
	
	public List<OlaReportBean> fetchOlaRetailerReportData(
			OlaReportBean olaReportBean, int walletId, int retOrgId,String playerType) throws GenericException {
		List<OlaReportBean> olaReportList = new ArrayList<OlaReportBean>();
		Connection con=null;
		try {
			double depositAmt = 0.0;
			double withdrawlAmt = 0.0;			
			double totalDepositAmount = 0.0;
			double totalWithdrawlAmount = 0.0;
			 con= DBConnect.getConnection();
			String startDate = olaReportBean.getFromDate();
			String endDate = olaReportBean.getToDate();
			con.setAutoCommit(false);
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
				String playerName = OlaCommonMethodControllerImpl.getPlayerNameFromPlayerId(rs.getInt("party_id"));
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
			
				olaReportList.add(olaReportBean);
			}
			OlaReportBean totalBean = new OlaReportBean();
			totalBean.setPlayerName("Total");
			totalBean.setDepositAmt(totalDepositAmount);			
			totalBean.setWithdrawlAmt(totalWithdrawlAmount);
			olaReportList.add(totalBean);
			con.commit();
			
		}catch(SQLException se){
			throw new GenericException(LMSErrors.SQL_EXCEPTION_CODE,se);
		}catch(Exception e){
			e.printStackTrace();
			throw new GenericException(LMSErrors.GENERAL_EXCEPTION_CODE,e);
		}
		finally{
			DBConnect.closeCon(con);
		}
		return olaReportList;
	}
	
	public OlaReportBean fetchTxnReportData(
			String fromDate, String toDate, int retOrgId) throws GenericException {
		Connection con=null;
		OlaReportBean totalBean = null;
		try {
			double mrpDepositAmt = 0.0;
			double mrpWithdrawlAmt = 0.0;			
			double totalMrpDepositAmount = 0.0;
			double totalMrpWithdrawlAmount = 0.0;
			double netAmt = 0.0;
			double totalNetAmt = 0.0;
			totalBean = new OlaReportBean();
			 con= DBConnect.getConnection();
			String startDate = fromDate;
			String endDate = toDate;
			
			
			StringBuilder depositSbQuery = new StringBuilder();
				depositSbQuery.append("select sum(depositAmt) depositAmt, sum(netAmt) netAmt, party_id,transaction_type,Date(transaction_date) transaction_date,player_id from (");
				depositSbQuery.append(" select sum(depositAmt- refundAmt) depositAmt, sum(netDAmt-netRAmt) netAmt, party_id,transaction_type,transaction_date from (");
				depositSbQuery.append(" select sum(deposit_amt) depositAmt,0.0 refundAmt, sum(net_amt) netDAmt, 0.0 netRAmt, party_id,transaction_type,transaction_date from st_lms_retailer_transaction_master rtm inner join st_ola_ret_deposit dp on rtm.transaction_id=dp.transaction_id  where rtm.transaction_type='OLA_DEPOSIT'  and dp.retailer_org_id=? and transaction_date>=?  and transaction_date<=? group by party_id");
				depositSbQuery.append(" union all");
				depositSbQuery.append(" select 0.0  depositAmt,sum(deposit_amt) refundAmt, 0.0 netDAmt, sum(net_amt) netRAmt, party_id,transaction_type,transaction_date from  st_lms_retailer_transaction_master rtm inner join st_ola_ret_deposit_refund dpr on rtm.transaction_id=dpr.transaction_id where rtm.transaction_type='OLA_DEPOSIT_REFUND'  and dpr.retailer_org_id=? and transaction_date>=?  and transaction_date<=? group by party_id ) olaDeposit");
				depositSbQuery.append(" group by party_id )ola");
				depositSbQuery.append(" left join");
				depositSbQuery.append(" (select player_id from st_ola_affiliate_plr_mapping  where ref_user_org_id=? )apm");
				depositSbQuery.append(" on ola.party_id =apm.player_id  group by party_id");
				
			StringBuilder wdrwlSbQuery = new StringBuilder();
				wdrwlSbQuery.append("select sum(withdrawlAmt) withdrawlAmt,sum(netAmt) netAmt,party_id,transaction_type,Date(transaction_date) transaction_date,player_id from ( ");
				wdrwlSbQuery.append(" select withdrawlAmt, netAmt,party_id,transaction_type,transaction_date from (	");
				wdrwlSbQuery.append(" select  sum(withdrawl_amt) withdrawlAmt,0.0 refundAmt, sum(net_amt) netAmt, party_id,transaction_type,transaction_date from st_lms_retailer_transaction_master rtm  inner join st_ola_ret_withdrawl wl on rtm.transaction_id=wl.transaction_id where rtm.transaction_type='OLA_WITHDRAWL'  and wl.retailer_org_id=? and transaction_date>=? and transaction_date<=? group by party_id ) withdrawl group by party_id)ola ");
				wdrwlSbQuery.append(" left join ");
				wdrwlSbQuery.append(" (select player_id from st_ola_affiliate_plr_mapping  where ref_user_org_id=? )apm  ");
				wdrwlSbQuery.append(" on ola.party_id =apm.player_id  group by party_id");
				
			PreparedStatement pstmt = con.prepareStatement(depositSbQuery.toString());
				pstmt.setInt(1, retOrgId);
				pstmt.setString(2, startDate);
				pstmt.setString(3, endDate);
				pstmt.setInt(4, retOrgId);
				pstmt.setString(5, startDate);
				pstmt.setString(6, endDate);
				pstmt.setInt(7, retOrgId);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				mrpDepositAmt = rs.getDouble("depositAmt");				
				totalMrpDepositAmount = totalMrpDepositAmount + mrpDepositAmt;
				netAmt = rs.getDouble("netAmt");
				totalNetAmt = totalNetAmt + netAmt;				
			}
			totalBean.setTotalDepositAmount(totalNetAmt);
			totalBean.setDepositAmt(totalMrpDepositAmount);
			totalNetAmt = 0.0;
			netAmt = 0.0;
			pstmt.clearParameters();
			pstmt = con.prepareStatement(wdrwlSbQuery.toString());
				pstmt.setInt(1, retOrgId);
				pstmt.setString(2, startDate);
				pstmt.setString(3, endDate);
				pstmt.setInt(4, retOrgId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				mrpWithdrawlAmt = rs.getDouble("withdrawlAmt");			
				totalMrpWithdrawlAmount = totalMrpWithdrawlAmount + mrpWithdrawlAmt;
				netAmt = rs.getDouble("netAmt");
				totalNetAmt = totalNetAmt + netAmt;
			}
			totalBean.setTotalWithdrawlAmount(netAmt);
			totalBean.setWithdrawlAmt(totalMrpWithdrawlAmount);	
			
			
		}catch(SQLException se){
			se.printStackTrace();
			throw new GenericException(LMSErrors.SQL_EXCEPTION_CODE, se);
		}catch(Exception e){
			e.printStackTrace();
			throw new GenericException(LMSErrors.GENERAL_EXCEPTION_CODE, e);
		}
		finally{
			DBConnect.closeCon(con);
		}
		return totalBean;
	}
	
	
	public String fetchOlaRetailerReportDataConsolidate(String  fromDate, int walletId, int retOrgId) throws GenericException {
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
		}catch(SQLException se){
			throw new GenericException(LMSErrors.SQL_EXCEPTION_CODE,se);
		}catch(Exception e){
			e.printStackTrace();
			throw new GenericException(LMSErrors.GENERAL_EXCEPTION_CODE,e);
		}finally{
			DBConnect.closeCon(con);
		}
		return sb.toString() ;
	}
	
	public List<OlaReportBean> fetchOlaAgentDepWithReportData(
			OlaReportBean olaReportBean, int walletId, int agtOrgId) throws GenericException {
		List<OlaReportBean> olaReportList = new ArrayList<OlaReportBean>();

		double depositAmt = 0.0;
		double withdrawlAmt = 0.0;
		double retailerDepositComm = 0.0;
		double retailerWithdrawlComm = 0.0;

		double retailerNetGaming = 0.0;
		double retailerCalculatedcomm = 0.0;

		double totalDepositAmount = 0.0;
		double totalWithdrawlAmount = 0.0;
		double totalDepositCommission = 0.0;
		double totalWithdrawlCommission = 0.0;
		double totalPlayerNetGaming = 0.0;
		double totalCommissionCalculated = 0.0;
		PreparedStatement pstmt=null;
		try {

			DecimalFormat df = new DecimalFormat("##.## ");
			Connection con = DBConnect.getConnection();
			String startDate = olaReportBean.getFromDate();
			String endDate = olaReportBean.getToDate();
			con.setAutoCommit(false);

		//	String query1 = "select user_name,organization_id from st_lms_user_master where organization_id in(select organization_id from st_lms_organization_master where parent_id="
			//		+ agtOrgId + ")";
			String orgCodeQry = "  name orgCode  ";

			
			if ((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE")) {
				orgCodeQry = "org_code orgCode  ";
	

			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("CODE_NAME")) {
				orgCodeQry = " concat(org_code,'_',name)  orgCode  ";
			

			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("NAME_CODE")) {
				orgCodeQry = " concat(name,'_',org_code)  orgCode  ";
			

			}
			
			String query1 = "select "+orgCodeQry+",organization_id from st_lms_organization_master where parent_id="+agtOrgId+" order by "+QueryManager.getAppendOrgOrder();
			
			PreparedStatement pstmt1 = con.prepareStatement(query1);
			ResultSet rs1 = pstmt1.executeQuery();


			while (rs1.next()) {
				String retailerName = rs1.getString(1);
				int retOrgId = rs1.getInt(2);
				olaReportBean = new OlaReportBean();

				String query = "select Final.deposit deposit ,Final.deposit*Final.ret_dep_comm/100 retailer_deposit_cal_comm ,Final.withdraw withdraw,Final.withdraw*Final.ret_with_comm/100  retailer_withdraw_cal_comm,Final.commission_calculated commission_calculated,Final.plr_net_gaming plr_net_gaming from	(select  DEPOSIT.deposit-DEPREF.refund deposit ,  DEPOSIT.ret_dep_comm ret_dep_comm ,WITHDRAW.withdraw-WITHREF.refund withdraw ,WITHDRAW.ret_with_comm ret_with_comm,NETPLR.plr_net_gaming plr_net_gaming,NETPLR.commission_calculated commission_calculated from (select rettx.retailer_org_id retalier_org,ifnull(ola .retailer_comm,0.0) ,ifnull(sum(withdrawl_amt),0.0) refund from st_ola_ret_withdrawl_refund ola  inner join (select transaction_id,retailer_org_id from st_lms_retailer_transaction_master where transaction_date>='"
						+ startDate
						+ "' and transaction_date<='"
						+ endDate
						+ "' and transaction_type='OLA_WITHDRAWL_REFUND' and retailer_org_id="
						+ retOrgId
						+ " ) rettx on ola.transaction_id=rettx.transaction_id and wallet_id="
						+ walletId
						+ ") WITHREF,(select rettx.retailer_org_id retalier_org,ifnull(sum(withdrawl_amt),0.0) withdraw,ifnull(ola .retailer_comm,0.0) ret_with_comm from st_ola_ret_withdrawl ola  inner join (select transaction_id,retailer_org_id from st_lms_retailer_transaction_master where transaction_date>='"
						+ startDate
						+ "'    and transaction_date<='"
						+ endDate
						+ "'    and transaction_type='OLA_WITHDRAWL' and retailer_org_id="
						+ retOrgId
						+ "   ) rettx on ola.transaction_id=rettx.transaction_id and wallet_id="
						+ walletId
						+ ")WITHDRAW,(select retailer_org_id retalier_org,ifnull(sum(deposit_amt),0.0) refund ,ifnull(ola .retailer_comm,0.0) from st_ola_ret_deposit_refund ola  inner join (select transaction_id from st_lms_retailer_transaction_master where transaction_date>='"
						+ startDate
						+ "'    and transaction_date<='"
						+ endDate
						+ "'    and transaction_type='OLA_DEPOSIT_REFUND' and retailer_org_id="
						+ retOrgId
						+ "   ) rettx on ola.transaction_id=rettx.transaction_id and wallet_id="
						+ walletId
						+ ") DEPREF,(select retailer_org_id retalier_org,ifnull(sum(deposit_amt),0.0) deposit,ifnull(ola .retailer_comm,0.0) ret_dep_comm from st_ola_ret_deposit ola  inner join (select transaction_id from st_lms_retailer_transaction_master where transaction_date>='"
						+ startDate
						+ "'    and transaction_date<='"
						+ endDate
						+ "'      and transaction_type='OLA_DEPOSIT' and retailer_org_id="
						+ retOrgId
						+ "   ) rettx on ola.transaction_id=rettx.transaction_id and wallet_id="
						+ walletId
						+ ") DEPOSIT,(select arc.plr_net_gaming plr_net_gaming,arc.commission_calculated commission_calculated from st_ola_agt_ret_commission arc inner join (select transaction_id ,user_org_id from st_lms_agent_transaction_master where transaction_type ='OLA_COMMISSION' and transaction_date>='"
						+ startDate
						+ "'     and transaction_date<='"
						+ endDate
						+ "'   and user_org_id="
						+ agtOrgId
						+ "   ) atm on atm.transaction_id=arc.transaction_id and arc.ret_org_id="
						+ retOrgId
						+ " and wallet_id="
						+ walletId
						+ " group by arc.ret_org_id union select distinct(0.0) commission_calculated,0.0 plr_net_gaming from st_lms_organization_master limit 1) NETPLR) Final";
				
				StringBuilder mainQuery=null;
				StringBuilder unionQuery=null;
				if(LMSFilterDispatcher.isRepFrmSP){
					mainQuery=new StringBuilder("select sum(deposit) deposit,sum(retailer_deposit_cal_comm) retailer_deposit_cal_comm, sum(withdraw) withdraw,sum(retailer_withdraw_cal_comm) retailer_withdraw_cal_comm,sum(commission_calculated) commission_calculated,sum(plr_net_gaming) plr_net_gaming from (");
					unionQuery=new StringBuilder(" union all select sum(deposit_net)-sum(ref_deposit_net_amt) deposit,0.0 retailer_deposit_cal_comm,sum(withdrawal_net_amt)-sum(ref_withdrawal_net_amt) withdraw , 0.00 retailer_withdraw_cal_comm,sum(net_gaming_net_comm) commission_calculated , 0.00 plr_net_gaming from st_rep_ola_retailer where finaldate>='"+startDate+"'  and finaldate<='"+endDate+"' and organization_id="+retOrgId+" and wallet_id="+walletId+") reptbl");
					mainQuery.append(query).append(unionQuery.toString());
					pstmt = con.prepareStatement(mainQuery.toString());
				} else{
					pstmt = con.prepareStatement(query.toString());
				}
				
				System.out.println(pstmt);
				ResultSet rs = pstmt.executeQuery();

				while (rs.next()) {

					olaReportBean.setRetailerName(retailerName);
					depositAmt = rs.getDouble("deposit");
					olaReportBean.setDepositAmt(depositAmt);
					totalDepositAmount = Double.parseDouble(df
							.format(totalDepositAmount + depositAmt));

					withdrawlAmt = rs.getDouble("withdraw");
					olaReportBean.setWithdrawlAmt(withdrawlAmt);
					totalWithdrawlAmount = Double.parseDouble(df
							.format(totalWithdrawlAmount + withdrawlAmt));

					retailerNetGaming = rs.getDouble("plr_net_gaming");
					olaReportBean.setRetailerNetGaming(retailerNetGaming);
					totalPlayerNetGaming = Double.parseDouble(df
							.format(totalPlayerNetGaming + retailerNetGaming));

					retailerCalculatedcomm = rs
							.getDouble("commission_calculated");
					olaReportBean
							.setRetailerCommissionCalculated(retailerCalculatedcomm);
					totalCommissionCalculated = Double.parseDouble(df
							.format(totalCommissionCalculated
									+ retailerCalculatedcomm));

					retailerDepositComm = Double.parseDouble(df.format(rs
							.getDouble("retailer_deposit_cal_comm")));
					olaReportBean
							.setRetailerDepositCommission(retailerDepositComm);

					totalDepositCommission = Double.parseDouble(df
							.format(totalDepositCommission
									+ retailerDepositComm));

					retailerWithdrawlComm = Double.parseDouble(df.format(rs
							.getDouble("retailer_withdraw_cal_comm")));
					olaReportBean
							.setRetailerWithdrawlCommission(retailerWithdrawlComm);

					totalWithdrawlCommission = Double.parseDouble(df
							.format(totalWithdrawlCommission
									+ retailerWithdrawlComm));

					olaReportList.add(olaReportBean);
				}
			}
			OlaReportBean totalBean = new OlaReportBean();
			totalBean.setPlayerName("Total");
			totalBean.setTotalDepositAmount(totalDepositAmount);
			totalBean.setTotalDepositCommission(totalDepositCommission);
			totalBean.setTotalWithdrawlAmount(totalWithdrawlAmount);
			totalBean.setTotalWithdrawlCommission(totalWithdrawlCommission);
			totalBean.setTotalPlayerNetGaming(totalPlayerNetGaming);
			totalBean.setCommissionCalculated(totalCommissionCalculated);
			System.out.println(totalCommissionCalculated
					+ "totalCommissionCalculated ");

			olaReportList.add(totalBean);

			con.commit();
		}catch(SQLException se){
			throw new GenericException(LMSErrors.SQL_EXCEPTION_CODE,se);
		}catch(Exception e){
			e.printStackTrace();
			throw new GenericException(LMSErrors.GENERAL_EXCEPTION_CODE,e);
		}
		return olaReportList;
	}
	
	@Deprecated
	public List<OlaAgentReportBean> fetchOlaBoDepWithReportData(
			OlaAgentReportBean olaReportBean, int walletId, int boOrgId) throws GenericException {
		List<OlaAgentReportBean> olaReportList = new ArrayList<OlaAgentReportBean>();

		double depositAmt = 0.0;
		double withdrawlAmt = 0.0;
		double agentDepositComm = 0.0;
		double agentWithdrawlComm = 0.0;

		double agentNetGaming = 0.0;
		double agentCalculatedcomm = 0.0;

		double totalDepositAmount = 0.0;
		double totalWithdrawlAmount = 0.0;
		double totalDepositCommission = 0.0;
		double totalWithdrawlCommission = 0.0;
		double totalPlayerNetGaming = 0.0;
		double totalCommissionCalculated = 0.0;

		try {

			DecimalFormat df = new DecimalFormat("##.## ");
			Connection con = DBConnect.getConnection();
			String startDate = olaReportBean.getFromDate();
			String endDate = olaReportBean.getToDate();
			con.setAutoCommit(false);
			
			String query1="select user_name,organization_id from st_lms_user_master where organization_id in(select organization_id from st_lms_organization_master where parent_id="+boOrgId+")";
			PreparedStatement pstmt1 = con.prepareStatement(query1);
			ResultSet rs1 = pstmt1.executeQuery();
			System.out
			.println("heeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee2222222222222.................................");
				
			while (rs1.next()) {
				String agentName=rs1.getString(1);
				int agtOrgId= rs1.getInt(2);
				olaReportBean = new OlaAgentReportBean();
								
			
			String query = "select Final.deposit deposit ,Final.deposit*Final.agt_dep_comm/100 agent_deposit_cal_comm ,Final.withdraw withdraw,	Final.withdraw*Final.agt_with_comm/100  agent_withdraw_cal_comm,Final.commission_calculated commission_calculated,Final.net_gaming net_gaming from (select  DEPOSIT.deposit-DEPREF.refund deposit ,DEPOSIT.agt_dep_comm agt_dep_comm ,WITHDRAW.withdraw-WITHREF.refund withdraw ,WITHDRAW.agt_with_comm agt_with_comm,NETPLR.net_gaming net_gaming,NETPLR.commission_calculated commission_calculated from (select rettx.user_org_id agent_org,ifnull(sum(withdrawl_amt),0.0) refund,ifnull(ola .agent_comm,0.0) agt_with_comm from st_ola_agt_withdrawl_refund ola  inner join (select transaction_id,user_org_id from st_lms_agent_transaction_master where transaction_date>='" +startDate+"'  and transaction_date<='"+endDate+"'    and transaction_type='OLA_WITHDRAWL_REFUND' and user_org_id="+agtOrgId +"  ) rettx on ola.transaction_id=rettx.transaction_id ) WITHREF,	(				select rettx.user_org_id agent_org,ifnull(sum(withdrawl_amt),0.0) withdraw,ifnull(ola .agent_comm,0.0) agt_with_comm from st_ola_agt_withdrawl ola  inner join (select transaction_id,user_org_id from st_lms_agent_transaction_master where transaction_date>='" +startDate+"'  and transaction_date<='"+endDate+"'    and transaction_type='OLA_WITHDRAWL' and user_org_id="+agtOrgId +"  ) rettx on ola.transaction_id=rettx.transaction_id)WITHDRAW,(select rettx.user_org_id agent_org,ifnull(sum(deposit_amt),0.0) refund,ifnull(ola .agent_comm,0.0) agt_dep_comm from st_ola_agt_deposit_refund ola  inner join (select transaction_id,user_org_id from st_lms_agent_transaction_master where transaction_date>='" +startDate+"'  and transaction_date<='"+endDate+"'    and transaction_type='OLA_DEPOSIT_REFUND' and user_org_id="+agtOrgId +"  ) rettx on ola.transaction_id=rettx.transaction_id) DEPREF,(select rettx.user_org_id agent_org,ifnull(sum(deposit_amt),0.0) deposit,ifnull(ola .agent_comm,0.0) agt_dep_comm from st_ola_agt_deposit ola  inner join (select transaction_id,user_org_id from st_lms_agent_transaction_master where transaction_date>='" +startDate+"'  and transaction_date<='"+endDate+"'    and transaction_type='OLA_DEPOSIT' and user_org_id="+agtOrgId +"  ) rettx on ola.transaction_id=rettx.transaction_id) DEPOSIT, (select arc.net_gaming net_gaming,arc.commission_calculated commission_calculated from st_ola_bo_agt_commission arc inner join (select transaction_id ,user_org_id from st_lms_bo_transaction_master where transaction_type ='OLA_COMMISSION' and transaction_date>='" +startDate+"'  and transaction_date<='"+endDate+"' and user_org_id="+boOrgId +"  ) atm on atm.transaction_id=arc.transaction_id and arc.agt_org_id="+agtOrgId+"  group by arc.agt_org_id	union select distinct(0.0) commission_calculated,0.0 plr_net_gaming from st_lms_organization_master limit 1) NETPLR) Final";
			System.out.println(query);
			PreparedStatement pstmt = con.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				
				
				olaReportBean.setAgentName(agentName);
				depositAmt = rs.getDouble("deposit");
				olaReportBean.setDepositAmt(depositAmt);
				totalDepositAmount = Double.parseDouble(df
						.format(totalDepositAmount + depositAmt));

				withdrawlAmt = rs.getDouble("withdraw");
				olaReportBean.setWithdrawlAmt(withdrawlAmt);
				totalWithdrawlAmount = Double.parseDouble(df
						.format(totalWithdrawlAmount + withdrawlAmt));

				agentNetGaming = rs.getDouble("net_gaming");
				olaReportBean.setAgentNetGaming(agentNetGaming);
				totalPlayerNetGaming = Double.parseDouble(df
						.format(totalPlayerNetGaming + agentNetGaming));

				agentCalculatedcomm = rs.getDouble("commission_calculated");
				olaReportBean
						.setAgentCommissionCalculated(agentCalculatedcomm);
				totalCommissionCalculated = Double.parseDouble(df
						.format(totalCommissionCalculated
								+ agentCalculatedcomm));

				agentDepositComm = Double.parseDouble(df.format(rs.getDouble("agent_deposit_cal_comm")));
				olaReportBean.setAgentDepositCommission(agentDepositComm);

				totalDepositCommission = Double.parseDouble(df
						.format(totalDepositCommission + agentDepositComm));

				agentWithdrawlComm =  Double.parseDouble(df.format(rs.getDouble("agent_withdraw_cal_comm")));
				olaReportBean.setAgentWithdrawlCommission(agentWithdrawlComm);

				totalWithdrawlCommission = Double.parseDouble(df
						.format(totalWithdrawlCommission+ agentWithdrawlComm));

				olaReportList.add(olaReportBean);
			}
			}
			OlaAgentReportBean totalBean = new OlaAgentReportBean();
			totalBean.setPlayerName("Total");
			totalBean.setTotalDepositAmount(totalDepositAmount);
			totalBean.setTotalDepositCommission(totalDepositCommission);
			totalBean.setTotalWithdrawlAmount(totalWithdrawlAmount);
			totalBean.setTotalWithdrawlCommission(totalWithdrawlCommission);
			totalBean.setTotalPlayerNetGaming(totalPlayerNetGaming);
			totalBean.setCommissionCalculated(totalCommissionCalculated);
			System.out.println(totalCommissionCalculated
					+ "totalCommissionCalculated ");

			olaReportList.add(totalBean);

			con.commit();
		}catch(SQLException se){
			throw new GenericException(LMSErrors.SQL_EXCEPTION_CODE,se);
		}catch(Exception e){
			e.printStackTrace();
			throw new GenericException(LMSErrors.GENERAL_EXCEPTION_CODE,e);
		}
		return olaReportList;
	}
	
	public List<OlaAgentReportBean> fetchOlaBoDepWithReportData(OlaOrgReportRequestBean requestBean) throws SQLException, GenericException, ParseException{
		
		double depositAmt = 0.0;
		double withdrawlAmt = 0.0;
		double agentDepositComm = 0.0;
		double agentWithdrawlComm = 0.0;

		double totalDepositAmount = 0.0;
		double totalWithdrawlAmount = 0.0;
		double totalDepositCommission = 0.0;
		double totalWithdrawlCommission = 0.0;
		double totalPlayerNetGaming = 0.0;
		double totalCommissionCalculated = 0.0;
		
		 Connection con = DBConnect.getConnection();
		 Map<Integer, OlaOrgReportResponseBean> detMap = OlaAgentReportControllerImpl.fetchDepositWithdrawlMultipleAgent(requestBean, con);
		 String agtOrgQry = QueryManager.getOrgQry("AGENT");
		 PreparedStatement pstmt = con.prepareStatement(agtOrgQry);
		 ResultSet rs = pstmt.executeQuery();
		 int orgId = 0;
		 List<OlaAgentReportBean> respList = new ArrayList<OlaAgentReportBean>();
		 SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		 Timestamp stDate = new Timestamp(sdf.parse(requestBean.getFromDate().toString()).getTime());
		 Timestamp toDate = new Timestamp(sdf.parse(requestBean.getToDate().toString()).getTime() + (1000 * 60 * 60 * 24) - 1);
		 	
		 //remove terminate Agent
		OrganizationTerminateReportHelper.getTerminateAgentListForRep(stDate, toDate);
		List<Integer> terminateAgentList=OrganizationTerminateReportHelper.AgentOrgIdIntTypeList;
	    logger.info("Terminate agent List::"+terminateAgentList);
	         
		 while (rs.next()) {
			 orgId = rs.getInt("organization_id");
			 if(terminateAgentList.contains(orgId)){
				 continue;
			 }
			
			 OlaAgentReportBean resBean = new OlaAgentReportBean();
			 resBean.setAgentName(rs.getString("orgCode"));
			 if(detMap.containsKey(orgId)){
				 
				 depositAmt = detMap.get(orgId).getMrpDepositAmt();
				 resBean.setDepositAmt(depositAmt);
				 totalDepositAmount = totalDepositAmount + depositAmt;
				 
				 agentDepositComm = detMap.get(orgId).getMrpDepositAmt() - detMap.get(orgId).getNetDepositAmt();
				 resBean.setAgentDepositCommission(agentDepositComm);
				 totalDepositCommission = totalDepositCommission + agentDepositComm;
				 
				 withdrawlAmt = detMap.get(orgId).getMrpWithdrawalAmt();
				 resBean.setWithdrawlAmt(withdrawlAmt);
				 totalWithdrawlAmount = totalWithdrawlAmount + withdrawlAmt;
				 
				 agentWithdrawlComm = detMap.get(orgId).getMrpWithdrawalAmt() - detMap.get(orgId).getNetWithdrawalAmt();
				 resBean.setAgentWithdrawlCommission(agentWithdrawlComm);
				 totalWithdrawlCommission = totalWithdrawlCommission + agentWithdrawlComm;
			 }	
			 respList.add(resBean);
		 }
		 
		OlaAgentReportBean totalBean = new OlaAgentReportBean();
		totalBean.setPlayerName("Total");
		totalBean.setTotalDepositAmount(totalDepositAmount);
		totalBean.setTotalDepositCommission(totalDepositCommission);
		totalBean.setTotalWithdrawlAmount(totalWithdrawlAmount);
		totalBean.setTotalWithdrawlCommission(totalWithdrawlCommission);
		totalBean.setTotalPlayerNetGaming(totalPlayerNetGaming);
		totalBean.setCommissionCalculated(totalCommissionCalculated);
		respList.add(totalBean);
			
		return respList;		
	}
	
	
	public OlaAgentReportBean fetchOlaBoDepWithReportDataforBOUser(OlaOrgReportRequestBean requestBean) throws SQLException, GenericException, ParseException{		
		Connection con = null;
		OlaAgentReportBean resBean = null;
		try{
			 con = DBConnect.getConnection();
			 resBean = new OlaAgentReportBean();
			 OlaOrgReportResponseBean respBean = OlaBOReportController.fetchDepositWithdrawlBO(requestBean, con);
			 
			 resBean.setDepositAmt(respBean.getMrpDepositAmt());
			 resBean.setWithdrawlAmt(respBean.getMrpWithdrawalAmt());
		}catch(GenericException ge){
			throw ge;
		}catch(Exception e){
			e.printStackTrace();
			throw new GenericException(LMSErrors.GENERAL_EXCEPTION_CODE,e);
		}finally{
			DBConnect.closeCon(con);
		}
		return resBean;		
	}

}
