package com.skilrock.lms.coreEngine.ola.reportMgmt;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import com.skilrock.lms.beans.OlaCommissionBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;

public class OlaPlrTransactionCommissionHelper {

	public List<OlaCommissionBean> getPlayerWiseCommissionData(Date startDate,Date endDate,int walletId,int retOrgId,String olaNetGamingDistributionModel) throws LMSException{
		Connection con=null;
		ResultSet rs=null;
		PreparedStatement pstmt=null;
		String tableName=null;
		if("CUMMULATIVE".equalsIgnoreCase(olaNetGamingDistributionModel)){
			tableName="st_ola_rummy_plr_cummulative_deposit_datewise";
		}else{
			tableName="st_ola_rummy_plr_deposit_datewise";
		}
		List<OlaCommissionBean> olaCommissionBeanList=null;
		OlaCommissionBean olaCommissionBean=null;
		try{
			olaCommissionBeanList=new ArrayList<OlaCommissionBean>();
			con=DBConnect.getConnection();
			
			String getPlayerWiseData="select dateWiseTlb.plr_id,net_gaming,total_commission from (select plr_id,sum(total_play) net_gaming from "+tableName+" where date>=? and date <= ? and ref_ret_org_id=? and plr_id not in(select plr_id from "+tableName+" where date>=? and date <= ? and ref_ret_org_id=? and status='UNMATCHED') group by plr_id)dateWiseTlb inner join (select plr_id,sum(ret_debit_card_comm)+sum(ret_credit_card_comm)+sum(ret_net_banking_comm)+sum(ret_cash_card_comm)+sum(ret_ola_bind_comm)+sum(ret_ola_non_bind_comm)+sum(ret_tech_process_comm)+sum(ret_bonus_comm)+sum(ret_inhouse_comm)+sum(ret_wire_transfer_comm)total_commission from st_ola_rummy_plr_retailer_commission where start_date>=? and end_date <=? group by plr_id) commTlb on dateWiseTlb.plr_id=commTlb.plr_id";
			pstmt=con.prepareStatement(getPlayerWiseData);
			pstmt.setDate(1,startDate);
			pstmt.setDate(2,endDate);
			pstmt.setInt(3,retOrgId);
			pstmt.setDate(4,startDate);
			pstmt.setDate(5,endDate);
			pstmt.setInt(6,retOrgId);
			pstmt.setDate(7,startDate);
			pstmt.setDate(8,endDate);
			System.out.println("Get Player Wise Data:"+pstmt);		
			rs=pstmt.executeQuery();
			
			while(rs.next()){
				olaCommissionBean =new OlaCommissionBean();
				olaCommissionBean.setPlayerId(rs.getString("plr_id"));
				olaCommissionBean.setTotalPlayerNetGaming(rs.getDouble("net_gaming"));
				olaCommissionBean.setTotalCommissionCalculated(rs.getDouble("total_commission"));
				olaCommissionBeanList.add(olaCommissionBean);
			}
			
		}catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException("Some Internal Error");
		}finally{
			try{
				con.close();
			}catch (SQLException e) {
				e.printStackTrace();
				throw new LMSException("Some Internal Error");
			}
		}
		return olaCommissionBeanList;
	}
	
	
	public List<OlaCommissionBean> getPlayerDepositCommissionData(Date startDate,Date endDate,int walletId,String playerId) throws LMSException{
		Connection con=null;
		ResultSet rs=null;
		PreparedStatement pstmt=null;
		List<OlaCommissionBean> olaCommissionBeanList=null;
		OlaCommissionBean olaCommissionBean=null;
		try{
			olaCommissionBeanList=new ArrayList<OlaCommissionBean>();
			con=DBConnect.getConnection();
			
			String getPlayerWiseData="select plr_id,till_date date,debit_card_deposit,credit_card_deposit,net_banking_deposit,cash_card_deposit,ola_bind_deposit,tech_process_deposit,ola_non_bind_deposit,bonus_deposit,inhouse_deposit,wire_transfer_deposit,total_deposit,0 total_play,0 total_winning from st_ola_rummy_plr_deposit_history where plr_id=? and till_date=date_sub(?,interval 1 day) union all select plr_id,date,sum(debit_card_deposit)debit_card_deposit,sum(credit_card_deposit)credit_card_deposit,sum(net_banking_deposit)net_banking_deposit,sum(cash_card_deposit)cash_card_deposit," 
				+"sum(ola_bind_deposit)ola_bind_deposit,sum(tech_process_deposit)tech_process_deposit,sum(ola_non_bind_deposit)ola_non_bind_deposit,sum(bonus_deposit)bonus_deposit,sum(inhouse_deposit)inhouse_deposit,sum(wire_transfer_deposit)wire_transfer_deposit,sum(total_deposit)total_deposit,sum(total_play)total_play,sum(total_winning)total_winning from st_ola_rummy_plr_deposit_datewise where plr_id=? and date>=? and date <= ? group by date";
			pstmt=con.prepareStatement(getPlayerWiseData);
			pstmt.setString(1,playerId);
			pstmt.setDate(2,startDate);
			pstmt.setString(3,playerId);
			pstmt.setDate(4,startDate);
			pstmt.setDate(5,endDate);
			
					System.out.println("Get Player Wise data Qry:"+pstmt);
			rs=pstmt.executeQuery();
			
			while(rs.next()){
				olaCommissionBean =new OlaCommissionBean();
				olaCommissionBean.setPlayerId(rs.getString("plr_id"));
				olaCommissionBean.setDepositDate(rs.getDate("date"));
				olaCommissionBean.setDebitCardDeposit(rs.getDouble("debit_card_deposit"));
				olaCommissionBean.setCreditCardDeposit(rs.getDouble("credit_card_deposit"));
				olaCommissionBean.setNetBankingDeposit(rs.getDouble("net_banking_deposit"));
				olaCommissionBean.setCashCardDeposit(rs.getDouble("cash_card_deposit"));
				olaCommissionBean.setOlaBindDeposit(rs.getDouble("ola_bind_deposit"));
				olaCommissionBean.setTechProcessDeposit(rs.getDouble("tech_process_deposit"));
				olaCommissionBean.setOlaNonBindDeposit(rs.getDouble("ola_non_bind_deposit"));
				olaCommissionBean.setBonusDeposit(rs.getDouble("bonus_deposit"));
				olaCommissionBean.setInhouseDeposit(rs.getDouble("inhouse_deposit"));
				olaCommissionBean.setWireTransferDeposit(rs.getDouble("wire_transfer_deposit"));
				olaCommissionBean.setTotalDeposit(rs.getDouble("total_deposit"));
				olaCommissionBean.setTotalPlay(rs.getDouble("total_play"));
				olaCommissionBean.setTotalWin(rs.getDouble("total_winning"));
				olaCommissionBeanList.add(olaCommissionBean);
			}
			
		}catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException("Some Internal Error");
		}finally{
			try{
				con.close();
			}catch (SQLException e) {
				e.printStackTrace();
				throw new LMSException("Some Internal Error");
			}
		}
		return olaCommissionBeanList;
	}
	
	
	
	public List<OlaCommissionBean> getPlayerDepositCummulativeTypeCommissionData(Date startDate,Date endDate,int walletId,String playerId) throws LMSException{
		Connection con=null;
		ResultSet rs=null;
		PreparedStatement pstmt=null;
		List<OlaCommissionBean> olaCommissionBeanList=null;
		OlaCommissionBean olaCommissionBean=null;
		try{
			olaCommissionBeanList=new ArrayList<OlaCommissionBean>();
			con=DBConnect.getConnection();
			
			String getPlayerWiseData="select plr_id,till_date date,debit_card_deposit,credit_card_deposit,net_banking_deposit,cash_card_deposit,ola_bind_deposit,tech_process_deposit,ola_non_bind_deposit,wire_transfer_deposit,total_deposit,0 total_play from st_ola_rummy_plr_deposit_comm_history where plr_id=? and  status='CONTINUE' and till_date=date_sub(?,interval 1 day) union all select plr_id,date,sum(debit_card_deposit)debit_card_deposit,sum(credit_card_deposit)credit_card_deposit,sum(net_banking_deposit)net_banking_deposit,sum(cash_card_deposit)cash_card_deposit," 
				+"sum(ola_bind_deposit)ola_bind_deposit,sum(tech_process_deposit)tech_process_deposit,sum(ola_non_bind_deposit)ola_non_bind_deposit,sum(wire_transfer_deposit)wire_transfer_deposit,sum(total_deposit)total_deposit,sum(total_play)total_play from st_ola_rummy_plr_cummulative_deposit_datewise where plr_id=? and date>=? and date <= ? group by date";
			pstmt=con.prepareStatement(getPlayerWiseData);
			pstmt.setString(1,playerId);
			pstmt.setDate(2,startDate);
			pstmt.setString(3,playerId);
			pstmt.setDate(4,startDate);
			pstmt.setDate(5,endDate);
			
					System.out.println("Get Player Wise data Qry:"+pstmt);
			rs=pstmt.executeQuery();
			
			while(rs.next()){
				olaCommissionBean =new OlaCommissionBean();
				olaCommissionBean.setPlayerId(rs.getString("plr_id"));
				olaCommissionBean.setDepositDate(rs.getDate("date"));
				olaCommissionBean.setDebitCardDeposit(rs.getDouble("debit_card_deposit"));
				olaCommissionBean.setCreditCardDeposit(rs.getDouble("credit_card_deposit"));
				olaCommissionBean.setNetBankingDeposit(rs.getDouble("net_banking_deposit"));
				olaCommissionBean.setCashCardDeposit(rs.getDouble("cash_card_deposit"));
				olaCommissionBean.setOlaBindDeposit(rs.getDouble("ola_bind_deposit"));
				olaCommissionBean.setTechProcessDeposit(rs.getDouble("tech_process_deposit"));
				olaCommissionBean.setOlaNonBindDeposit(rs.getDouble("ola_non_bind_deposit"));
				olaCommissionBean.setWireTransferDeposit(rs.getDouble("wire_transfer_deposit"));
				olaCommissionBean.setTotalDeposit(rs.getDouble("total_deposit"));
				olaCommissionBean.setTotalPlay(rs.getDouble("total_play"));
				
				olaCommissionBeanList.add(olaCommissionBean);
			}
			
		}catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException("Some Internal Error");
		}finally{
			try{
				con.close();
			}catch (SQLException e) {
				e.printStackTrace();
				throw new LMSException("Some Internal Error");
			}
		}
		return olaCommissionBeanList;
	}
	
	
	
	public String checkRetailerTransactionStatus(Date startDate,Date endDate,int retOrgId,int walletId) throws LMSException{
		String status="";
		Connection con=null;
		ResultSet rs=null;
		PreparedStatement pstmt=null;
		
		try{
			con=DBConnect.getConnection();
			String chkStatusQry="select status from st_ola_retailer_weekly_commission_exp where date>=? and date<= ? and retailer_org_id=? and wallet_id=? ";
			pstmt=con.prepareStatement(chkStatusQry);
			pstmt.setDate(1,startDate);
			pstmt.setDate(2,endDate);
			pstmt.setInt(3,retOrgId);
			pstmt.setInt(4,walletId);
			rs=pstmt.executeQuery();
			
			if(rs.next()){
				status=rs.getString("status");
			}
			
			
		}catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException("Some Internal Error");
		}finally{
			try{
				con.close();
			}catch (SQLException e) {
				e.printStackTrace();
				throw new LMSException("Some Internal Error");
			}
		}
		System.out.println("Return Data:"+status);
		return status;
	}
	
	
	
	public List<OlaCommissionBean> getRetailerWiseCommissionData(Date startDate,Date endDate,int walletId,int agtOrgId) throws LMSException{
		Connection con=null;
		ResultSet rs=null;
		PreparedStatement pstmt=null;
		List<OlaCommissionBean> olaCommissionBeanList=null;
		OlaCommissionBean olaCommissionBean=null;
		try{
			olaCommissionBeanList=new ArrayList<OlaCommissionBean>();
			con=DBConnect.getConnection();
			
			String getRetailerWiseData="select retailer_org_id,name,net_gaming,retailer_claim_comm,ROUND((retailer_claim_comm*tds_comm_rate*.01) ,2)ret_tds_comm,retailer_net_claim_comm,agt_claim_comm,ROUND((agt_claim_comm*tds_comm_rate*.01),2) agt_tds_comm,agt_net_claim_comm from st_ola_ret_comm retCommTlb inner join " 
				                       +"(select sum(net_gaming) net_gaming,refTransactionId,name,parent_id from st_ola_retailer_weekly_commission_exp we inner join st_lms_organization_master om on we.retailer_org_id=om.organization_id where date>=? and date <=? and status='DONE' and wallet_id=? and parent_id=? group by retailer_org_id) netTlb on retCommTlb.transaction_id=netTlb.refTransactionId";
			pstmt=con.prepareStatement(getRetailerWiseData);
			pstmt.setDate(1,startDate);
			pstmt.setDate(2,endDate);
			pstmt.setInt(3,walletId);
			pstmt.setInt(4,agtOrgId);
			
			System.out.println("Get Retailer Wise Data:"+pstmt);		
			rs=pstmt.executeQuery();
			
			while(rs.next()){
				olaCommissionBean =new OlaCommissionBean();
				olaCommissionBean.setRetOrdId(rs.getInt("retailer_org_id"));
				olaCommissionBean.setRetOrgName(rs.getString("name"));
				olaCommissionBean.setRetNetGaming(rs.getDouble("net_gaming"));
				olaCommissionBean.setRetComm(rs.getDouble("retailer_claim_comm"));
				olaCommissionBean.setTdsRetComm(rs.getDouble("ret_tds_comm"));
				olaCommissionBean.setNetRetComm(rs.getDouble("retailer_net_claim_comm"));
				olaCommissionBean.setAgtComm(rs.getDouble("agt_claim_comm"));
				olaCommissionBean.setTdsagtComm(rs.getDouble("agt_tds_comm"));
				olaCommissionBean.setNetAgtComm(rs.getDouble("agt_net_claim_comm"));
				
				
				olaCommissionBeanList.add(olaCommissionBean);
			}
			
		}catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException("Some Internal Error");
		}finally{
			try{
				con.close();
			}catch (SQLException e) {
				e.printStackTrace();
				throw new LMSException("Some Internal Error");
			}
		}
		return olaCommissionBeanList;
	}
	
	
	public List<OlaCommissionBean> getAgentWiseCommissionData(Date startDate,Date endDate,int walletId) throws LMSException{
		Connection con=null;
		ResultSet rs=null;
		PreparedStatement pstmt=null;
		List<OlaCommissionBean> olaCommissionBeanList=null;
		OlaCommissionBean olaCommissionBean=null;
		try{
			olaCommissionBeanList=new ArrayList<OlaCommissionBean>();
			con=DBConnect.getConnection();
			
			String getRetailerWiseData="select ttt.parent_id,om.name,sum(net_gaming)net_gaming,sum(agt_claim_comm)agt_claim_comm,sum(agt_tds_comm)agt_tds_comm,sum(agt_net_claim_comm)agt_net_claim_comm from(select parent_id,name,net_gaming,agt_claim_comm,ROUND((agt_claim_comm*tds_comm_rate*.01),2) agt_tds_comm,agt_net_claim_comm from st_ola_ret_comm retCommTlb inner join (select sum(net_gaming) net_gaming,refTransactionId,name,parent_id from st_ola_retailer_weekly_commission_exp we inner join st_lms_organization_master om on we.retailer_org_id=om.organization_id where date>=? and date <=? and status='DONE' and wallet_id=?  group by retailer_org_id) netTlb on retCommTlb.transaction_id=netTlb.refTransactionId) ttt inner join st_lms_organization_master om on ttt.parent_id=om.organization_id group by parent_id";
			StringBuilder unionQuery=null;
			StringBuilder mainQuery=null;
			if(LMSFilterDispatcher.isRepFrmSP){
				mainQuery=new StringBuilder("select parent_id,name,sum(net_gaming)net_gaming,sum(agt_claim_comm)agt_claim_comm,sum(agt_tds_comm)agt_tds_comm,sum(agt_net_claim_comm)agt_net_claim_comm from (");
				unionQuery=new StringBuilder(" union all select organization_id parent_id , name, net_gaming,agt_claim_comm,agt_tds_comm,agt_net_claim_comm from st_lms_organization_master om inner join (select parent_id ,sum(net_gaming) net_gaming,sum(net_gaming_comm) agt_claim_comm, sum(net_gaming_tds_comm) agt_tds_comm,sum(net_gaming_net_comm) agt_net_claim_comm from st_rep_ola_retailer where finaldate>=? and finaldate <=?  and wallet_id=? group by  parent_id ) rep on om.organization_id=rep.parent_id) repTbl group by parent_id");
				mainQuery.append(getRetailerWiseData).append(unionQuery.toString());
				pstmt=con.prepareStatement(mainQuery.toString());
				pstmt.setDate(1,startDate);
				pstmt.setDate(2,endDate);
				pstmt.setInt(3,walletId);
				pstmt.setDate(4,startDate);
				pstmt.setDate(5,endDate);
				pstmt.setInt(6,walletId);
			}
			else
			{
				pstmt=con.prepareStatement(getRetailerWiseData);
				pstmt.setDate(1,startDate);
				pstmt.setDate(2,endDate);
				pstmt.setInt(3,walletId);
			}
			
			System.out.println("Get Agent Wise Data:"+pstmt);		
			rs=pstmt.executeQuery();
			
			while(rs.next()){
				olaCommissionBean =new OlaCommissionBean();
				olaCommissionBean.setAgtOrdId(rs.getInt("parent_id"));
				olaCommissionBean.setAgtOrgName(rs.getString("name"));
				olaCommissionBean.setAgtNetGaming(rs.getDouble("net_gaming"));
				olaCommissionBean.setAgtComm(rs.getDouble("agt_claim_comm"));
				olaCommissionBean.setTdsagtComm(rs.getDouble("agt_tds_comm"));
				olaCommissionBean.setNetAgtComm(rs.getDouble("agt_net_claim_comm"));
				
				
				olaCommissionBeanList.add(olaCommissionBean);
			}
			
		}catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException("Some Internal Error");
		}finally{
			try{
				con.close();
			}catch (SQLException e) {
				e.printStackTrace();
				throw new LMSException("Some Internal Error");
			}
		}
		return olaCommissionBeanList;
	}
}
