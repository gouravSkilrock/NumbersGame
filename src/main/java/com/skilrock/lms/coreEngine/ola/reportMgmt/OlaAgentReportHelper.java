package com.skilrock.lms.coreEngine.ola.reportMgmt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.skilrock.lms.beans.OlaAgentReportBean;
import com.skilrock.lms.beans.OlaReportBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;

public class OlaAgentReportHelper {

	// mandy wala
	public List<OlaReportBean> fetchOlaAgentDepWithReportData(
			OlaReportBean olaReportBean, int walletId, int agtOrgId) {
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
			System.out
					.println("heeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee.................................");

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
		} catch (Exception e) {
			e.printStackTrace();
		}
		return olaReportList;
	}

	public List<OlaAgentReportBean> fetchOlaAgentReportData(String startDate,
			String endDate, int walletId, int retOrgId) {
		double totalPlayerNetGaming = 0.0;
		double totalCommission = 0.0;
		double totalDoneCommission = 0.0;
		double totalPendingCommission = 0.0;
		List<OlaAgentReportBean> olaReportList = new ArrayList<OlaAgentReportBean>();
		Connection con = DBConnect.getConnection();
		try {
			con.setAutoCommit(false);
			String agtReportQuery = "select sum(pendingComm) as pendingComm,sum(doneComm) as doneComm,ret_org_id,sum(plr_net_gaming)as plrNetGaming from ( (select 0.0 doneComm,sum(commission_calculated) pendingComm,ret_org_id,sum(plr_net_gaming)  plr_net_gaming from st_ola_daily_retailer_commission_"
					+ walletId
					+ " where date>='"
					+ startDate
					+ "' and date<='"
					+ endDate
					+ "' and status='PENDING' and ret_org_id="
					+ retOrgId
					+ " group by ret_org_id )union all (select sum(commission_calculated) doneComm,0.0 pendingComm,ret_org_id,sum(plr_net_gaming) plr_net_gaming from st_ola_daily_retailer_commission_"
					+ walletId
					+ " where date>='"
					+ startDate
					+ "' and date<='"
					+ endDate
					+ "' and status='DONE' and ret_org_id="
					+ retOrgId
					+ " group by ret_org_id) ) a group by ret_org_id";
			PreparedStatement pstmt = con.prepareStatement(agtReportQuery);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				String retOrganizationName = null;
				int retOrganizationId = rs.getInt("ret_org_id");
				PreparedStatement pStatement = con
						.prepareStatement("select name from st_lms_organization_master where organization_id="
								+ retOrganizationId + "");
				ResultSet resultSet = pStatement.executeQuery();
				if (resultSet.next()) {
					retOrganizationName = resultSet.getString("name");
				}
				OlaAgentReportBean olaAgentReportBean = new OlaAgentReportBean();
				olaAgentReportBean.setPlayerName(retOrganizationName);
				olaAgentReportBean.setDoneCommission(rs.getDouble("doneComm"));
				olaAgentReportBean.setPendingCommission(rs
						.getDouble("pendingComm"));
				olaAgentReportBean.setPlayerNetGaming(rs
						.getDouble("plrNetGaming"));
				olaAgentReportBean.setCommissionCalculated((rs
						.getDouble("pendingComm"))
						+ (rs.getDouble("doneComm")));
				totalPlayerNetGaming = totalPlayerNetGaming
						+ rs.getDouble("plrNetGaming");
				totalCommission = totalCommission
						+ (rs.getDouble("pendingComm"))
						+ (rs.getDouble("doneComm"));
				totalDoneCommission = totalDoneCommission
						+ rs.getDouble("doneComm");
				totalPendingCommission = totalPendingCommission
						+ rs.getDouble("pendingComm");
				olaReportList.add(olaAgentReportBean);
			}
			OlaAgentReportBean totalBean = new OlaAgentReportBean();
			totalBean.setPlayerName("Total");
			totalBean.setCommissionCalculated(totalCommission);
			totalBean.setDoneCommission(totalDoneCommission);
			totalBean.setPendingCommission(totalPendingCommission);
			totalBean.setPlayerNetGaming(totalPlayerNetGaming);
			olaReportList.add(totalBean);
			System.out.println(olaReportList.size());
			con.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return olaReportList;
	}

}
