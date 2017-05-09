package com.skilrock.lms.coreEngine.ola.reportMgmt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.skilrock.lms.beans.OlaAgentReportBean;
import com.skilrock.lms.common.db.DBConnect;
public class OlaBoReportHelper {
	
	public List<OlaAgentReportBean> fetchOlaBoDepWithReportData(
			OlaAgentReportBean olaReportBean, int walletId, int boOrgId) {
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		return olaReportList;
	}

}
