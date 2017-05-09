package com.skilrock.lms.web.bankReports;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.web.bankMgmt.Helpers.BankHelper;
import com.skilrock.lms.web.bankMgmt.beans.BankRepDataBean;
import com.skilrock.lms.web.bankMgmt.beans.BranchDetailsBean;

public class BankRepHelper {
	static Log logger = LogFactory.getLog(BankRepHelper.class);
	public List<BankRepDataBean> fetchCreditUpdateData(BranchDetailsBean branchDetailsBean,String startDate,String endDate,	UserInfoBean userBean ){
		Connection con =null;
		PreparedStatement ps =null;
		ResultSet rs =null;
		List<BankRepDataBean> bankRepDataList =new ArrayList<BankRepDataBean>();
		try{
			con =DBConnect.getConnection();
			int bankId =0;
			if(userBean.getRoleId()==1){
				 bankId =branchDetailsBean.getBankId();
			}else{
				bankId=Integer.parseInt(new BankHelper().fetchBankList(userBean.getRoleId()).split(":")[0]);
			}
			int branchId=branchDetailsBean.getBranchId();
			String appendQry = getBankDataQuery(bankId,branchId);
			String query = " select bank_display_name,bank_full_name,branch_display_name,branch_full_name,netAmount, noOfMach from (select bank_id,branch_id,sum(netAmount) netAmount, count(mach) noOfMach from ( " +
			" select branch_id,bank_id,sum(totalAmount) as netAmount,retOrgId,1 as mach from ( select btm.branch_id,btm.bank_id,sum(ifnull((agtcn.amount),-agtdn.amount)) totalAmount,ifnull((agtdn.retailer_org_id),agtcn.retailer_org_id) retOrgId"+
			"	from (select lms_transaction_id,branch_id,bank_id from st_lms_branch_transaction_mapping where trn_type in(?,?))btm inner join (select transaction_date,transaction_id  from st_lms_agent_transaction_master   where date(transaction_date)>=? and date(transaction_date)<=? ) agtm"+ 
			"	on  agtm.transaction_id = btm.lms_transaction_id left join st_lms_agent_debit_note agtdn on  agtdn.transaction_id = btm.lms_transaction_id left join st_lms_agent_cash_transaction agtcn on  agtcn.transaction_id = btm.lms_transaction_id "+ 
			" group by branch_id,bank_id,retOrgId)a group by branch_id,bank_id,retOrgId )b "+appendQry+" group by branch_id,bank_id )main left join st_lms_bank_master bm on bm.bank_id=main.bank_id left join st_lms_branch_master brm on brm.branch_id=main.branch_id  order by  bank_display_name,branch_display_name"; 
			ps =con.prepareStatement(query);
			ps.setString(1,"CASH");
			ps.setString(2,"DR_NOTE_CASH");
			ps.setString(3, startDate);
			ps.setString(4, endDate);
			rs =ps.executeQuery();
			logger.info("bank data query"+ps);
			while(rs.next()){
				BankRepDataBean repDataBean = new BankRepDataBean();
				repDataBean.setBankName(rs.getString("bank_display_name"));
				repDataBean.setBankFullName(rs.getString("bank_full_name"));
				repDataBean.setBranchName(rs.getString("branch_display_name"));
				repDataBean.setBranchFullName(rs.getString("branch_full_name"));
				repDataBean.setTotalAmount(rs.getDouble("netAmount"));
				repDataBean.setTotalMachines(rs.getInt("noOfMach"));
				bankRepDataList.add(repDataBean);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			try{
				if(con!=null){
					con.close();
				}
				if(ps!=null){
					ps.close();
				}
				if(rs!=null){
					rs.close();
				}
					
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		return bankRepDataList;
	}
	
	public String getBankDataQuery(int bankId,int branchId){
		StringBuilder appendQuery =new StringBuilder();
		if(bankId>0){
			appendQuery.append(" where bank_id="+bankId);
			
			if(branchId>0){
				appendQuery.append(" and  branch_id="+branchId);
			}
		}
		
		return appendQuery.toString();
	}
	
	public List<BankRepDataBean> fetchCashierWiseTrnData (BranchDetailsBean branchDetailsBean,String startDate,String endDate,	UserInfoBean userBean ,String terminalId,String repType) {
		

		Connection con =null;
		PreparedStatement ps =null;
		ResultSet rs =null;
		List<BankRepDataBean> bankRepDataList =new ArrayList<BankRepDataBean>();
		try{
			con =DBConnect.getConnection();
			int bankId =0;
			int userId =0;
			if(userBean.getRoleId()==1){
				 bankId =branchDetailsBean.getBankId();
				 
			}else{
				bankId=Integer.parseInt(new BankHelper().fetchBankList(userBean.getRoleId()).split(":")[0]);
				if(!userBean.getIsRoleHeadUser().equalsIgnoreCase("Y")){
					userId=userBean.getUserId();
				}
			
				
			}
			int branchId=branchDetailsBean.getBranchId();
			if(!repType.equalsIgnoreCase("Payment")){
				String query =getCashierDataQuery(bankId,branchId,terminalId,userId);
				ps =con.prepareStatement(query);
				ps.setString(1,"CASH");
				ps.setString(2,"DR_NOTE_CASH");
				ps.setString(3, startDate);
				ps.setString(4, endDate);
				logger.info("cashier data query"+ps);
				rs =ps.executeQuery();
			
				while(rs.next()){
					BankRepDataBean repDataBean = new BankRepDataBean();
					repDataBean.setBankName(rs.getString("bank_display_name"));
					repDataBean.setBankFullName(rs.getString("bank_full_name"));
					repDataBean.setBranchName(rs.getString("branch_display_name"));
					repDataBean.setBranchFullName(rs.getString("branch_full_name"));
					repDataBean.setTotalAmount(rs.getDouble("Amount"));
					repDataBean.setTerminalId(rs.getString("terminalId"));
					repDataBean.setAgentCode(rs.getString("agentCode"));
					repDataBean.setAgentName(rs.getString("agentName"));
					repDataBean.setRefId(rs.getLong("lms_transaction_id")+"");
					repDataBean.setTrnDate(rs.getString("transaction_date").replace(".0",""));
					repDataBean.setCashierName(rs.getString("user_name"));
					repDataBean.setTrnType("Credit");
					bankRepDataList.add(repDataBean);
				}
			}
			
			if(!repType.equalsIgnoreCase("Credit")&&(terminalId.length()<=0)){
				String query =getWinningDataQuery(bankId,branchId,userId);
				ps =con.prepareStatement(query);
				ps.setString(1,"DG_PWT_PLR");
				ps.setString(2, startDate);
				ps.setString(3, endDate);
				logger.info("winning data query"+ps);
				rs =ps.executeQuery();
			while(rs.next()){
					BankRepDataBean repDataBean = new BankRepDataBean();
					repDataBean.setBankName(rs.getString("bank_display_name"));
					repDataBean.setBankFullName(rs.getString("bank_full_name"));
					repDataBean.setBranchName(rs.getString("branch_display_name"));
					repDataBean.setBranchFullName(rs.getString("branch_full_name"));
					repDataBean.setTotalAmount(rs.getDouble("Amount"));
					repDataBean.setRefId(rs.getLong("lms_transaction_id")+"");
					repDataBean.setTerminalId("NA");
					repDataBean.setAgentCode("NA");
					repDataBean.setAgentName("NA");
					repDataBean.setTrnType("Payment");
					repDataBean.setTrnDate(rs.getString("transaction_date").replace(".0",""));
					repDataBean.setCashierName(rs.getString("user_name"));
					bankRepDataList.add(repDataBean);
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
			bankRepDataList.clear();
		}finally {
			try{
				if(con!=null){
					con.close();
				}
				if(ps!=null){
					ps.close();
				}
				if(rs!=null){
					rs.close();
				}
					
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		return bankRepDataList;
	
	
	}
	private String getWinningDataQuery(int bankId, int branchId, int userId) {

		StringBuilder appendQuery =new StringBuilder();
		appendQuery.append(" select lms_transaction_id,transaction_date,um.user_name,bank_display_name,bank_full_name,branch_display_name,branch_full_name,Amount from (select lms_transaction_id,botm.transaction_date,btm.branch_id,btm.bank_id,user_id,net_amt Amount  from (select lms_transaction_id,branch_id,bank_id,user_id from st_lms_branch_transaction_mapping where trn_type in(?))btm 	inner join (select transaction_date,transaction_id  from st_lms_bo_transaction_master   where date(transaction_date)>=? and date(transaction_date)<=? ) botm on  botm.transaction_id = btm.lms_transaction_id 	left join st_dg_bo_direct_plr_pwt boPlr on  boPlr.transaction_id = btm.lms_transaction_id   ");
		if(bankId>0){
			appendQuery.append(" where bank_id="+bankId);
			
			if(branchId>0){
				appendQuery.append(" and  branch_id="+branchId);
			}
			if(userId>0){
				appendQuery.append(" and  user_id="+userId);	
			}
			
		}else{
			if(userId>0){
				appendQuery.append(" where  btm.user_id="+userId);
				
				
				
			}
			
		}
		appendQuery.append("  )main  left join st_lms_bank_master bm on bm.bank_id=main.bank_id  left join st_lms_branch_master brm on brm.branch_id=main.branch_id left join st_lms_user_master um on um.user_id=main.user_id    order  by main.user_id " );
		
		return appendQuery.toString();
	
		
		
	}

	public String getCashierDataQuery(int bankId,int branchId,String terminalId,int userId){
		StringBuilder appendQuery =new StringBuilder();
		appendQuery.append(" select lms_transaction_id,transaction_date,branch_display_name,branch_full_name,bank_display_name,bank_full_name,ifnull((agtcn.amount),-agtdn.amount) Amount,user_name,om.org_code terminalId,parentOm.org_code agentCode,parentOm.name agentName	from   (select lms_transaction_id,branch_id,bank_id,user_id from st_lms_branch_transaction_mapping where trn_type in(?,?))btm inner join (select transaction_date,transaction_id  from st_lms_agent_transaction_master   where date(transaction_date)>=? and date(transaction_date)<=? ) agtm  on  agtm.transaction_id = btm.lms_transaction_id left join st_lms_agent_debit_note agtdn on  agtdn.transaction_id = btm.lms_transaction_id left join st_lms_agent_cash_transaction agtcn on  agtcn.transaction_id = btm.lms_transaction_id left join  st_lms_branch_master brm on brm.branch_id =btm.branch_id left join  st_lms_bank_master bm on bm.bank_id =btm.bank_id left join st_lms_user_master um on um.user_id=btm .user_id  left join st_lms_organization_master om on (om.organization_id=ifnull((agtdn.retailer_org_id),agtcn.retailer_org_id) )  left join st_lms_organization_master parentOm  on om.parent_id = parentOm.organization_id      ");
		if(bankId>0){
			appendQuery.append(" where btm.bank_id="+bankId);
			
			if(branchId>0){
				appendQuery.append(" and  btm.branch_id="+branchId);
			}
			if(userId>0){
				appendQuery.append(" and  btm.user_id="+userId);	
			}
			if(terminalId.length()>0){
				appendQuery.append(" and  om.org_code="+terminalId);
			}
		}else{
			if(userId>0){
				appendQuery.append(" where  btm.user_id="+userId);
				if(terminalId.length()>0){
					appendQuery.append(" and  om.org_code="+terminalId);
				}
				
				
			}else{
				if(terminalId.length()>0){
					appendQuery.append(" where  om.org_code="+terminalId);
				}
				
			}
			
		}
		appendQuery.append(" order  by om.org_code " );
		
		return appendQuery.toString();
	}

	public List<BankRepDataBean> fetchWinningUpdateData(
			BranchDetailsBean branchDetailsBean, String startDate,
			String endDate, UserInfoBean userBean) {

		Connection con =null;
		PreparedStatement ps =null;
		ResultSet rs =null;
		List<BankRepDataBean> bankRepDataList =new ArrayList<BankRepDataBean>();
		try{
			con =DBConnect.getConnection();
			int bankId =0;
			if(userBean.getRoleId()==1){
				 bankId =branchDetailsBean.getBankId();
			}else{
				bankId=Integer.parseInt(new BankHelper().fetchBankList(userBean.getRoleId()).split(":")[0]);
			}
			int branchId=branchDetailsBean.getBranchId();
			String appendQry = getBankDataQuery(bankId,branchId);
			
			String query =" select bank_display_name,bank_full_name,branch_full_name,branch_display_name,totalAmount from (select btm.branch_id,btm.bank_id,sum(net_amt) totalAmount  from (select * from st_lms_branch_transaction_mapping where trn_type in(?))btm "+ 
							"	inner join (select transaction_date,transaction_id  from st_lms_bo_transaction_master   where date(transaction_date)>=? and date(transaction_date)<=? ) botm on  botm.transaction_id = btm.lms_transaction_id "+ 
							"	left join st_dg_bo_direct_plr_pwt boPlr on  boPlr.transaction_id = btm.lms_transaction_id "+appendQry+" group by branch_id,bank_id  )main  left join st_lms_bank_master bm on bm.bank_id=main.bank_id  left join st_lms_branch_master brm on brm.branch_id=main.branch_id   ";
			
			ps =con.prepareStatement(query);
			ps.setString(1,"DG_PWT_PLR");
			ps.setString(2, startDate);
			ps.setString(3, endDate);
			rs =ps.executeQuery();
			logger.info("bank data query"+ps);
			while(rs.next()){
				BankRepDataBean repDataBean = new BankRepDataBean();
				repDataBean.setBankName(rs.getString("bank_display_name"));
				repDataBean.setBankFullName(rs.getString("bank_full_name"));
				repDataBean.setBranchName(rs.getString("branch_display_name"));
				repDataBean.setBranchFullName(rs.getString("branch_full_name"));
				repDataBean.setTotalAmount(rs.getDouble("totalAmount"));
				bankRepDataList.add(repDataBean);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			try{
				if(con!=null){
					con.close();
				}
				if(ps!=null){
					ps.close();
				}
				if(rs!=null){
					rs.close();
				}
					
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		return bankRepDataList;
	
	}
	
}
