package com.skilrock.lms.coreEngine.reportsMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.PostSalePwtCommissionBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.accMgmt.common.TrainingExpAgentHelper;


public class RetailerPostSaleCommissionHelper {
	private static Log logger = LogFactory.getLog(TrainingExpAgentHelper.class);
	public static void main(String[] args){
		//new RetailerPostSaleCommissionHelper().insertPostDepositRetailerWiseWithCommission("2013-01-21", "APPROVED");
		//new RetailerPostSaleCommissionHelper().insertDailyPostDepositRetailerWiseScheduler("2013-01-21");
	}
	
	public void insertDailyPostDepositRetailerWiseScheduler(String deploymentDate) throws LMSException{
		PreparedStatement pstmt=null;
		ResultSet resultSet=null;
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -1);//done for rescheduling of training expanses at 12:15 AM
		Date lastDateDone =new java.sql.Date(cal.getTimeInMillis());
		Date startDate=null;
		Connection con=DBConnect.getConnection();
		try {
			pstmt = con.prepareStatement("select DATE_ADD(date_time,INTERVAL 1 DAY) date_time from st_lms_retailer_post_deposit_daily_commission order by date_time desc  limit 1");
			RetailerPostSaleCommissionHelper retailerPostHelper=new RetailerPostSaleCommissionHelper();
		resultSet = pstmt.executeQuery();
		if(resultSet.next()){					
			startDate = resultSet.getDate("date_time");
			
			System.out.println("last daily deposit exapnses given for "+lastDateDone);
		}else{
			System.out.println("daily deposit expanses generating first time");
             SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy");
			
			startDate=new java.sql.Date(sdf.parse(deploymentDate).getTime());
			
		}
		
		if(getZeroTimeDate(startDate).compareTo(getZeroTimeDate(lastDateDone)) > 0){
			return;
		}else if(getZeroTimeDate(startDate).compareTo(getZeroTimeDate(lastDateDone)) == 0){
			System.out.println("Date for Deposit Commission"+lastDateDone.toString());
			retailerPostHelper.insertPostDepositRetailerWiseWithCommission(startDate.toString(), "APPROVED");
		}else{
			
          while(getZeroTimeDate(startDate).compareTo(getZeroTimeDate(lastDateDone)) <=0 ){
        		System.out.println("Date for Deposit Commission"+startDate.toString());
        		retailerPostHelper.insertPostDepositRetailerWiseWithCommission(startDate.toString(), "APPROVED");
        		Calendar calendar = Calendar.getInstance();

    		    calendar.setTime(startDate);
    		    calendar.add(Calendar.DAY_OF_MONTH, 1);
    		    startDate=new java.sql.Date(calendar.getTimeInMillis());
			}
			
			
			
		}
		
		
		
		} catch (SQLException e) {
			logger.info("SQL Exception ",e);
			throw new LMSException("SQL Exception "+e.getMessage());
		}catch (Exception e) {
			logger.info("Exception ",e);
			throw new LMSException("Exception"+e.getMessage());
		} finally {
			DBConnect.closeCon(con);
		}
		
	}
	
	

	public void insertPostDepositRetailerWiseWithCommission(String date,String status){
        PreparedStatement pstmt=null;
		Connection con=null;
		try{
			con=DBConnect.getConnection();
			pstmt=con.prepareStatement("insert into st_lms_retailer_post_deposit_daily_commission(ret_org_id,date_time,deposit_amount,deposit_comm_rate,deposit_comm_amount,tax_comm_rate,tax_amount,charges_1,charges_2,net_amount_to_pay,status)" 
					                +" select ret_org_id,? date,deposit,deposit_comm_rate,deposit_comm_amt,tax_comm_rate,tax_amt,charges_1,charges_2,(deposit_comm_amt-tax_amt-charges_1-charges_2) net_amt,? from(" 
					                +" select ret_org_id,ifnull(deposit,0.0)deposit,deposit_comm_rate,ifnull(deposit,0.0)*.01*deposit_comm_rate deposit_comm_amt,tax_comm_rate,(ifnull(deposit,0.0)*.01*deposit_comm_rate)*tax_comm_rate*.01 tax_amt,charges_1,charges_2 from(" 
					                +" select retailer_org_id,sum(cash)+sum(chq)-sum(chq_ret)+sum(credit)-sum(debit) deposit from(select retailer_org_id,sum(amount) cash ,0 chq,0 chq_ret,0 credit,0 debit from st_lms_agent_cash_transaction cash inner join st_lms_agent_transaction_master btm on cash.transaction_id=btm.transaction_id  where transaction_type='CASH' and cash.retailer_org_id=btm.party_id and date(transaction_date)=? group by retailer_org_id" 
					                +" union all select retailer_org_id,0 cash,sum(cheque_amt) chq,0 chq_ret,0 credit,0 debit from st_lms_agent_sale_chq chq inner join st_lms_agent_transaction_master btm on chq.transaction_id=btm.transaction_id  where chq.transaction_type IN ('CHEQUE','CLOSED') and chq.retailer_org_id=btm.party_id and date(transaction_date)=? group by retailer_org_id" 
					                +" union all select retailer_org_id,0 cash,0 chq,sum(cheque_amt) chq_ret,0 credit,0 debit from st_lms_agent_sale_chq chq inner join st_lms_agent_transaction_master btm on chq.transaction_id=btm.transaction_id  where chq.transaction_type IN ('CHQ_BOUNCE') and chq.retailer_org_id=btm.party_id and date(transaction_date)=? group by retailer_org_id" 
					                +" union all select retailer_org_id,0 cash,0 chq,0 chq_ret,0 credit,sum(amount) debit from st_lms_agent_debit_note debit,st_lms_agent_transaction_master btm where debit.transaction_id=btm.transaction_id and debit.transaction_type IN ('DR_NOTE_CASH','DR_NOTE') and date(transaction_date)=? group by retailer_org_id" 
					                +" union all select retailer_org_id,0 cash,0 chq,0 chq_ret,sum(amount) credit,0 debit from st_lms_agent_credit_note credit,st_lms_agent_transaction_master btm where credit.transaction_id=btm.transaction_id and credit.transaction_type IN ('CR_NOTE_CASH','CR_NOTE') and date(transaction_date)=? group by retailer_org_id)aaa group by retailer_org_id)bbb" 
					                +" right join ( select organization_id ret_org_id,deposit_default_comm_rate+ifnull(deposit_comm_var,0.0) deposit_comm_rate,tax_default_comm_rate+ifnull(tax_var,0.0) tax_comm_rate,charges_1+ifnull(charges_1_var,0.0) charges_1,charges_2+ifnull(charges_2_var,0.0) charges_2 from(select organization_id,deposit_default_comm_rate,tax_default_comm_rate,charges_1,charges_2 from st_lms_organization_master om,st_lms_retailer_post_deposit_commission_details com where com.status='ACTIVE' and om.organization_type='RETAILER')aa left join st_lms_retailer_post_deposit_commission_variance var on aa.organization_id=var.ret_org_id)commTlb  on bbb.retailer_org_id=commTlb.ret_org_id)ccc");
			
			pstmt.setString(1, date);
			pstmt.setString(2, status);
			pstmt.setString(3, date);
			pstmt.setString(4, date);
			pstmt.setString(5, date);
			pstmt.setString(6, date);
			pstmt.setString(7, date);
			
			
			System.out.println(pstmt);
			pstmt.executeUpdate();
			System.out.println("dd"+pstmt);
		}catch (Exception se) {
			se.printStackTrace();
		}finally{
			try{
				con.close();
			}catch (SQLException se) {
				// TODO: handle exception
			}
		}
	}
	
	
	public void insertPostSalePwtDailyDataGameWiseWithCommission(String date,String status){
		
		PreparedStatement pstmt=null;
		
		Statement stmt=null;
		ResultSet gameRs=null;
		
		Connection con=null;
		try{
			con=DBConnect.getConnection();
			stmt=con.createStatement();
			gameRs=stmt.executeQuery("select game_id from st_dg_game_master");
			
			while(gameRs.next()){
				int gameId=gameRs.getInt("game_id");
				
				pstmt=con.prepareStatement("insert into st_lms_retailer_post_sales_daily_commission(ret_org_id,game_id,date_time,sale_amount,sale_comm_rate,sale_comm_amount,pwt_amount,pwt_comm_rate,pwt_comm_amount,tax_comm_rate,tax_amount,net_amount_to_pay,status) " 
						                 + " select retailer_org_id,game_id,? date_time,sale_amt,sale_comm_rate,sale_comm_amt,pwt_amt,pwt_comm_rate,pwt_comm_amt,tax_comm_rate,(sale_comm_amt+pwt_comm_amt)*.01*tax_comm_rate tax_amount, (sale_comm_amt+pwt_comm_amt)-((sale_comm_amt+pwt_comm_amt)*.01*tax_comm_rate) net_pay_amt,? status from( " 
						                 + "select bbb.retailer_org_id,bbb.game_id,sale_amt,sale_comm_rate,(sale_amt*.01)*sale_comm_rate sale_comm_amt,pwt_amt,pwt_comm_rate,(pwt_amt*.01)*pwt_comm_rate pwt_comm_amt,tax_comm_rate from ( " 
						                 +"select game_id,sum(sale_amt)-sum(cancel_amt) sale_amt,sum(pwt_amt) pwt_amt,retailer_org_id from (select rtm.game_id,sum(mrp_amt) sale_amt,0 cancel_amt,0 pwt_amt,sdr.retailer_org_id,date(transaction_date) sale_date from st_dg_ret_sale_? sdr inner join st_lms_retailer_transaction_master rtm on sdr.transaction_id=rtm.transaction_id where transaction_type='DG_SALE' and sdr.game_id=rtm.game_id and date(transaction_date)=? group by sdr.retailer_org_id " 
						                 +" union all  select rtm.game_id,0 sale_amt,sum(mrp_amt) refund_amt,0 pwt_amt,drs.retailer_org_id,date(transaction_date) cancel_date from st_dg_ret_sale_refund_? drs inner join st_lms_retailer_transaction_master rtm on drs.transaction_id=rtm.transaction_id where transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') and drs.game_id=rtm.game_id and date(transaction_date)=? group by drs.retailer_org_id" 
						                 +" union all select rtm.game_id,0 sale_amt,0 refund_amt,sum(pwt_amt) pwt_amt,drp.retailer_org_id,date(transaction_date) from st_dg_ret_pwt_? drp inner join st_lms_retailer_transaction_master rtm on drp.transaction_id=rtm.transaction_id where transaction_type in ('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') and drp.game_id=rtm.game_id and date(transaction_date)=? group by drp.retailer_org_id ) aaa group by retailer_org_id" 
						                 +")bbb left join (select ret_org_id,var.game_id,sale_comm_var+sale_default_comm_rate sale_comm_rate,pwt_comm_var+pwt_default_comm_rate pwt_comm_rate,tax_default_comm_rate+tax_var tax_comm_rate from st_lms_retailer_post_sales_commission_variance var inner join st_lms_retailer_post_sales_commission_details com on var.game_id=com.game_id where com.status='ACTIVE')commTlb on bbb.retailer_org_id=commTlb.ret_org_id and bbb.game_id=commTlb.game_id)ccc");
				
				pstmt.setString(1, date);
				pstmt.setString(2, status);
				pstmt.setInt(3, gameId);
				pstmt.setString(4, date);
				pstmt.setInt(5, gameId);
				pstmt.setString(6, date);
				pstmt.setInt(7, gameId);
				pstmt.setString(8, date);
				pstmt.executeUpdate();
				
			}
		}catch (SQLException se) {
			se.printStackTrace();
		}finally{
			try{
				con.close();
			}catch (SQLException se) {
				// TODO: handle exception
			}
		}
		
	}
	
	
	public static Date getZeroTimeDate(Date fecha) {
	    Date res = fecha;
	    Calendar calendar = Calendar.getInstance();

	    calendar.setTime( fecha );
	    calendar.set(Calendar.HOUR_OF_DAY, 0);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
	    calendar.set(Calendar.MILLISECOND, 0);

	    res = new Date(calendar.getTime().getTime());

	    return res;
	}
	
	public Map<Integer, String> getRetailerNameList(int retOrgId){
		Map<Integer, String> retailerNameList=new LinkedHashMap<Integer, String>();
		Connection con=null;
		Statement stmt=null;
		ResultSet rs=null;
		try{
			con=DBConnect.getConnection();
			stmt=con.createStatement();
			rs=stmt.executeQuery("select organization_id,name from st_lms_organization_master where organization_type='RETAILER' and parent_id="+retOrgId);
			while(rs.next()){
				retailerNameList.put(rs.getInt("organization_id"), rs.getString("name"));
			}
			
		}catch (SQLException se) {
			// TODO: handle exception
		}finally{
			try{
				con.close();
			}catch (SQLException se) {
				// TODO: handle exception
			}
		}
		return retailerNameList;
		
	}
	public Map<String, PostSalePwtCommissionBean> getRetailerCommissionDetail(String startDate,String endDate,int retOrgId){
		Map<String, PostSalePwtCommissionBean> postSaleDepositAgentMap=new LinkedHashMap<String, PostSalePwtCommissionBean>();
       PreparedStatement pstmt=null;
       PostSalePwtCommissionBean depositCommBean=null;
		ResultSet rs=null;
		
		Connection con=null;
		try{
			con=DBConnect.getConnection();
		//pstmt=con.prepareStatement("select date_time,deposit_amount,deposit_comm_rate,deposit_comm_amount,tax_amount,charges_1,net_amount_to_pay,status from st_lms_agent_post_deposit_daily_commission where agt_org_id=? and date_time>=? and date_time<?  union  select 'Total',sum(deposit_amount),deposit_comm_rate,sum(deposit_comm_amount),sum(tax_amount),sum(charges_1),sum(net_amount_to_pay),status from st_lms_agent_post_deposit_daily_commission where agt_org_id=? and date_time>=? and date_time<? group by agt_org_id order by date_time");
		pstmt=con.prepareStatement("select date_time,sum(deposit_amount)deposit_amount,deposit_comm_rate,sum(deposit_comm_amount)deposit_comm_amount,sum(tax_amount)tax_amount,d.charges_1,(sum(deposit_comm_amount)-sum(tax_amount)-d.charges_1)net_amount_to_pay,com.status from st_lms_retailer_post_deposit_daily_commission com,st_lms_retailer_post_deposit_commission_details d where ret_org_id=? and date_time>=? and date_time<? and d.status='ACTIVE' group by ret_org_id");
		pstmt.setInt(1, retOrgId);
		pstmt.setString(2, startDate);
		pstmt.setString(3, endDate);
		/*pstmt.setInt(4, agentOrgId);
		pstmt.setString(5, startDate);
		pstmt.setString(6, endDate);*/
		rs=pstmt.executeQuery();
		while(rs.next()){
			 depositCommBean=new PostSalePwtCommissionBean();
			 depositCommBean.setDate(rs.getString("date_time"));
			 depositCommBean.setDepAmount(rs.getDouble("deposit_amount"));
			 depositCommBean.setDepCommRate(rs.getDouble("deposit_comm_rate"));
			 depositCommBean.setCommAmount(rs.getDouble("deposit_comm_amount"));
			 depositCommBean.setTaxCharges(rs.getDouble("tax_amount"));
			 depositCommBean.setCharges_1(rs.getDouble("charges_1"));
			 depositCommBean.setNetAmount(rs.getDouble("net_amount_to_pay"));
			 depositCommBean.setStatus(rs.getString("status"));
			 postSaleDepositAgentMap.put(rs.getString("date_time"), depositCommBean);
			 
		}
		
		}catch (SQLException se) {
			// TODO: handle exception
		}finally{
			try{
				con.close();
			}catch (SQLException se) {
				// TODO: handle exception
			}
		}
		return postSaleDepositAgentMap;
	}

	public void updateRetailerCommissionDetailStatus(String startDate,
			String endDate, int retOrgId,String paidMode,int paidBy) {
		 PreparedStatement pstmt=null;
		PreparedStatement insertPstmt=null;		
			Connection con=null;
			try{
				con=DBConnect.getConnection();
				
			insertPstmt=con.prepareStatement("insert into st_lms_retailer_post_deposit_datewise_commission(ret_org_id,start_date,end_date,deposit_amount,deposit_comm_rate,deposit_comm_amount,tax_comm_rate,tax_amount,charges_1,charges_2,net_amount_to_pay,paid_date,paid_mode,paid_by_user_id,status) select ?,?,DATE_ADD(?,INTERVAL -1 DAY) end_date,sum(deposit_amount)deposit_amount,deposit_comm_rate,sum(deposit_comm_amount)deposit_comm_amount,tax_comm_rate,sum(tax_amount)tax_amount,d.charges_1,d.charges_2,(sum(deposit_comm_amount)-sum(tax_amount)-d.charges_1-d.charges_2)net_amount_to_pay,now(),?,?,'PAID' from st_lms_retailer_post_deposit_daily_commission com,st_lms_retailer_post_deposit_commission_details d where ret_org_id=? and date_time>=? and date_time<? and d.status='ACTIVE' group by ret_org_id");	
			insertPstmt.setInt(1, retOrgId);
			insertPstmt.setString(2, startDate);
			insertPstmt.setString(3, endDate);
			insertPstmt.setString(4, paidMode);
			insertPstmt.setInt(5, paidBy);
			insertPstmt.setInt(6, retOrgId);
			insertPstmt.setString(7, startDate);
			insertPstmt.setString(8, endDate);
			insertPstmt.executeUpdate();
			
			pstmt=con.prepareStatement("update st_lms_retailer_post_deposit_daily_commission set status='PAID' where ret_org_id=? and date_time>=? and date_time<?");
			pstmt.setInt(1, retOrgId);
			pstmt.setString(2, startDate);
			pstmt.setString(3, endDate);
			
			pstmt.executeUpdate();
			
			}catch (SQLException se) {
				// TODO: handle exception
			}finally{
				try{
					con.close();
				}catch (SQLException se) {
					// TODO: handle exception
				}
			}
	}
	
	
}
