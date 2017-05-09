package com.skilrock.lms.coreEngine.reportsMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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


public class AgentPostSaleCommissionHelper {
	static Log logger = LogFactory.getLog(AgentPostSaleCommissionHelper.class);
	public static void main(String[] args){
		//new AgentPostSaleCommissionHelper().insertPostDepositAgentWiseWithCommission("2011-12-09", "APPROVED");
		//new AgentPostSaleCommissionHelper().insertDailyPostDepositAgentWiseScheduler("10-01-2013");
		//Connection con=DBConnect.getConnection();
		//logger.info(getNoOfMachineAtAgent(con,9));
		//new AgentPostSaleCommissionHelper().insertMonthlyPostDepositAgentWiseScheduler("10-12-2012");
	}
	
	public void insertDailyPostDepositAgentWiseScheduler(String deploymentDate) throws LMSException{
		PreparedStatement pstmt=null;
		ResultSet resultSet=null;
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -1);//done for rescheduling of training expanses at 12:15 AM
		Date lastDateDone =new java.sql.Date(cal.getTimeInMillis());
		Date startDate=null;
		Connection con=DBConnect.getConnection();
		try {
			pstmt = con.prepareStatement("select DATE_ADD(date_time,INTERVAL 1 DAY) date_time from st_lms_agent_post_deposit_daily_commission order by date_time desc  limit 1");
		
		resultSet = pstmt.executeQuery();
		if(resultSet.next()){					
			startDate = resultSet.getDate("date_time");
			
			logger.info("last daily Deposit exapnses given for "+lastDateDone);
		}else{
			logger.info("daily Deposit expanses generating first time");
			
			
			SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy");
			
			startDate=new java.sql.Date(sdf.parse(deploymentDate).getTime());
			
			
		}
		
		if(getZeroTimeDate(startDate).compareTo(getZeroTimeDate(lastDateDone)) > 0){
			return;
		}else if(getZeroTimeDate(startDate).compareTo(getZeroTimeDate(lastDateDone)) == 0){
			logger.info("Date for Deposit Commission"+lastDateDone.toString());
			new AgentPostSaleCommissionHelper().insertPostDepositAgentWiseWithCommission(startDate.toString(), "APPROVED");
		}else{
			
          while(getZeroTimeDate(startDate).compareTo(getZeroTimeDate(lastDateDone)) <=0 ){
        		logger.info("Date for Deposit Commission"+startDate.toString());
        		new AgentPostSaleCommissionHelper().insertPostDepositAgentWiseWithCommission(startDate.toString(), "APPROVED");
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
	
	
	
	public void insertPostDepositAgentWiseWithCommission(String date,String status){
        PreparedStatement pstmt=null;
		Connection con=null;
		try{
			con=DBConnect.getConnection();
			pstmt=con.prepareStatement("insert into st_lms_agent_post_deposit_daily_commission(agt_org_id,date_time,deposit_amount,deposit_comm_rate,deposit_comm_amount,tax_comm_rate,tax_amount,charges_1,charges_2,net_amount_to_pay,status) " 
					                 +" select agt_org_id,? date,deposit,deposit_comm_rate,deposit_comm_amt,tax_comm_rate,tax_amt,charges_1,charges_2,(deposit_comm_amt-tax_amt-charges_1-charges_2) net_amt,? from("
					                 +" select agt_org_id,ifnull(deposit,0.0)deposit,deposit_comm_rate,ifnull(deposit,0.0)*.01*deposit_comm_rate deposit_comm_amt,tax_comm_rate,(ifnull(deposit,0.0)*.01*deposit_comm_rate)*tax_comm_rate*.01 tax_amt,charges_1,charges_2 from(" 
					                 +" select agent_org_id,sum(cash)+sum(chq)-sum(chq_ret)+sum(credit)-sum(debit)+sum(bank) deposit from(select agent_org_id,sum(amount) cash ,0 chq,0 chq_ret,0 credit,0 debit,0 bank from st_lms_bo_cash_transaction cash inner join st_lms_bo_transaction_master btm on cash.transaction_id=btm.transaction_id  where transaction_type='CASH' and cash.agent_org_id=btm.party_id and date(transaction_date)=? group by agent_org_id" 
					                 +" union all select agent_org_id,0 cash,sum(cheque_amt) chq,0 chq_ret,0 credit,0 debit,0 bank from st_lms_bo_sale_chq chq inner join st_lms_bo_transaction_master btm on chq.transaction_id=btm.transaction_id  where chq.transaction_type IN ('CHEQUE','CLOSED') and chq.agent_org_id=btm.party_id and date(transaction_date)=? group by agent_org_id" 
					                 +" union all select agent_org_id,0 cash,0 chq,sum(cheque_amt) chq_ret,0 credit,0 debit,0 bank from st_lms_bo_sale_chq chq inner join st_lms_bo_transaction_master btm on chq.transaction_id=btm.transaction_id  where chq.transaction_type IN ('CHQ_BOUNCE') and chq.agent_org_id=btm.party_id and date(transaction_date)=? group by agent_org_id" 
					                 +" union all select agent_org_id,0 cash,0 chq,0 chq_ret,0 credit,sum(amount) debit,0 bank from st_lms_bo_debit_note debit,st_lms_bo_transaction_master btm where debit.transaction_id=btm.transaction_id and debit.transaction_type IN ('DR_NOTE_CASH','DR_NOTE') and date(transaction_date)=? group by agent_org_id" 
					                 +" union all select agent_org_id,0 cash,0 chq,0 chq_ret,sum(amount) credit,0 debit,0 bank from st_lms_bo_credit_note credit,st_lms_bo_transaction_master btm where credit.transaction_id=btm.transaction_id and credit.transaction_type IN ('CR_NOTE_CASH','CR_NOTE') and date(transaction_date)=? group by agent_org_id" 
					                 +" union all select agent_org_id,0 cash,0 chq,0 chq_ret,0 credit,0 debit,sum(amount) bank from st_lms_bo_bank_deposit_transaction bank,st_lms_bo_transaction_master btm where bank.transaction_id=btm.transaction_id and date(transaction_date)=? group by agent_org_id)aaa group by agent_org_id" 
					                 +" )bbb right join (select organization_id agt_org_id,deposit_default_comm_rate+ifnull(deposit_comm_var,0.0) deposit_comm_rate,tax_default_comm_rate+ifnull(tax_var,0.0) tax_comm_rate,charges_1+ifnull(charges_1_var,0.0) charges_1,charges_2+ifnull(charges_2_var,0.0) charges_2 from(select organization_id,deposit_default_comm_rate,tax_default_comm_rate,charges_1,charges_2 from st_lms_organization_master om,st_lms_agent_post_deposit_commission_details com where com.status='ACTIVE' and om.organization_type='AGENT')aa left join st_lms_agent_post_deposit_commission_variance var on aa.organization_id=var.agt_org_id)commTlb  on bbb.agent_org_id=commTlb.agt_org_id)ccc");
			pstmt.setString(1, date);
			pstmt.setString(2, status);
			pstmt.setString(3, date);
			pstmt.setString(4, date);
			pstmt.setString(5, date);
			pstmt.setString(6, date);
			pstmt.setString(7, date);
			pstmt.setString(8, date);
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
				
				pstmt=con.prepareStatement("insert into st_lms_agent_post_sales_daily_commission(agt_org_id,game_id,date_time,sale_amount,sale_comm_rate,sale_comm_amount,pwt_amount,pwt_comm_rate,pwt_comm_amount,tax_comm_rate,tax_amount,net_amount_to_pay,status) select agt_org_id,game_id,? date_time,sale_amt,sale_comm_rate,sale_comm_amt,pwt_amt,pwt_comm_rate,pwt_comm_amt,tax_comm_rate,(sale_comm_amt+pwt_comm_amt)*.01*tax_comm_rate tax_amount,(sale_comm_amt+pwt_comm_amt)-((sale_comm_amt+pwt_comm_amt)*.01*tax_comm_rate) net_pay_amt,? status from( " 
						                  +	"select ccc.agt_org_id,ccc.game_id,sale_amt,sale_comm_rate,(sale_amt*.01)*sale_comm_rate sale_comm_amt,pwt_amt,pwt_comm_rate,(pwt_amt*.01)*pwt_comm_rate pwt_comm_amt,tax_comm_rate from " 
						                  +	"( select parent_id agt_org_id,game_id,sum(net_amt) sale_amt,sum(pwt_amt) pwt_amt from (select game_id,sum(sale_amt)-sum(cancel_amt) net_amt,sum(pwt_amt) pwt_amt,retailer_org_id from ( " 
						                  +	"select rtm.game_id,sum(mrp_amt) sale_amt,0 cancel_amt,0 pwt_amt,sdr.retailer_org_id,date(transaction_date) sale_date from st_dg_ret_sale_? sdr inner join st_lms_retailer_transaction_master rtm on sdr.transaction_id=rtm.transaction_id where transaction_type='DG_SALE' and sdr.game_id=rtm.game_id and date(transaction_date)=? group by sdr.retailer_org_id " 
						                  +	" union all select rtm.game_id,0 sale_amt,sum(mrp_amt) refund_amt,0 pwt_amt,drs.retailer_org_id,date(transaction_date) cancel_date from st_dg_ret_sale_refund_? drs inner join st_lms_retailer_transaction_master rtm on drs.transaction_id=rtm.transaction_id where transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') and drs.game_id=rtm.game_id and date(transaction_date)=? group by drs.retailer_org_id " 
						                  +	" union all select rtm.game_id,0 sale_amt,0 refund_amt,sum(pwt_amt) pwt_amt,drp.retailer_org_id,date(transaction_date) from st_dg_ret_pwt_? drp inner join st_lms_retailer_transaction_master rtm on drp.transaction_id=rtm.transaction_id where transaction_type in ('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') and drp.game_id=rtm.game_id and date(transaction_date)=? group by drp.retailer_org_id ) aaa group by retailer_org_id)bbb inner join st_lms_organization_master om on bbb.retailer_org_id=om.organization_id group by parent_id " 
						                  +	")ccc left join (select agt_org_id,var.game_id,sale_comm_var+sale_default_comm_rate sale_comm_rate,pwt_comm_var+pwt_default_comm_rate pwt_comm_rate,tax_default_comm_rate+tax_var tax_comm_rate from st_lms_agent_post_sales_commission_variance var inner join st_lms_agent_post_sales_commission_details com on var.game_id=com.game_id )commTlb on ccc.agt_org_id=commTlb.agt_org_id and ccc.game_id=commTlb.game_id)ddd " 
						                  +	" ");
				
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
			// TODO: handle exception
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
	
	public Map<String, PostSalePwtCommissionBean> getAgentCommissionDetail(String startDate,String endDate,int agentOrgId){
		Map<String, PostSalePwtCommissionBean> postSaleDepositAgentMap=new LinkedHashMap<String, PostSalePwtCommissionBean>();
       PreparedStatement pstmt=null;
       PostSalePwtCommissionBean depositCommBean=null;
		ResultSet rs=null;
		
		Connection con=null;
		try{
		con=DBConnect.getConnection();
			
		int noOfMac=getNoOfMachineAtAgent(con,agentOrgId,startDate,endDate);
		//pstmt=con.prepareStatement("select date_time,deposit_amount,deposit_comm_rate,deposit_comm_amount,tax_amount,charges_1,net_amount_to_pay,status from st_lms_agent_post_deposit_daily_commission where agt_org_id=? and date_time>=? and date_time<?  union  select 'Total',sum(deposit_amount),deposit_comm_rate,sum(deposit_comm_amount),sum(tax_amount),sum(charges_1),sum(net_amount_to_pay),status from st_lms_agent_post_deposit_daily_commission where agt_org_id=? and date_time>=? and date_time<? group by agt_org_id order by date_time");
		pstmt=con.prepareStatement("select date_time,sum(deposit_amount)deposit_amount,deposit_comm_rate,sum(deposit_comm_amount)deposit_comm_amount,sum(tax_amount)tax_amount,d.charges_1*"+noOfMac+" charges_1,(sum(deposit_comm_amount)-sum(tax_amount)-(d.charges_1*"+noOfMac+")) net_amount_to_pay,com.status from st_lms_agent_post_deposit_daily_commission com,st_lms_agent_post_deposit_commission_details d where agt_org_id=? and date_time>=? and date_time<? and d.status='ACTIVE' group by agt_org_id");
		pstmt.setInt(1, agentOrgId);
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
		
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			try{
				if(con!=null){
					con.close();
				}
				if(pstmt!=null){
					pstmt.close();
				}
				if(rs!=null){
					rs.close();
				}
				
			}catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return postSaleDepositAgentMap;
	}


	public void updateAgentCommissionDetailStatus(String startDate,
			String endDate, int agentOrgId,String paidMode,int paidBy) {
		 PreparedStatement pstmt=null;
		PreparedStatement insertPstmt=null;		
			Connection con=null;
			try{
				con=DBConnect.getConnection();
			//	int noOfMac=getNoOfMachineAtAgent(con,agentOrgId);
				int noOfMac=getNoOfMachineAtAgent(con, agentOrgId, startDate, endDate);
			insertPstmt=con.prepareStatement("insert into st_lms_agent_post_deposit_datewise_commission(agt_org_id,start_date,end_date,deposit_amount,deposit_comm_rate,deposit_comm_amount,tax_comm_rate,tax_amount,charges_1,charges_2,net_amount_to_pay,paid_date,paid_mode,paid_by_user_id,status) select ?,?,DATE_ADD(?,INTERVAL -1 DAY) end_date,sum(deposit_amount)deposit_amount,deposit_comm_rate,sum(deposit_comm_amount)deposit_comm_amount,tax_comm_rate,sum(tax_amount)tax_amount,d.charges_1*"+noOfMac+",d.charges_2*"+noOfMac+",(sum(deposit_comm_amount)-sum(tax_amount)-(d.charges_1*"+noOfMac+")-(d.charges_2*"+noOfMac+"))net_amount_to_pay,now(),?,?,'PAID' from st_lms_agent_post_deposit_daily_commission com,st_lms_agent_post_deposit_commission_details d where agt_org_id=? and date_time>=? and date_time<? and d.status='ACTIVE' group by agt_org_id");	
			insertPstmt.setInt(1, agentOrgId);
			insertPstmt.setString(2, startDate);
			insertPstmt.setString(3, endDate);
			insertPstmt.setString(4, paidMode);
			insertPstmt.setInt(5, paidBy);
			insertPstmt.setInt(6, agentOrgId);
			insertPstmt.setString(7, startDate);
			insertPstmt.setString(8, endDate);
			insertPstmt.executeUpdate();
			
			pstmt=con.prepareStatement("update st_lms_agent_post_deposit_daily_commission set status='PAID' where agt_org_id=? and date_time>=? and date_time<?");
			pstmt.setInt(1, agentOrgId);
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
	
	public static int getNoOfMachineAtAgent(Connection con,int agentOrgId){
		int mac=0;
		 PreparedStatement pstmt=null;
		 ResultSet rs=null;
		 try {
			pstmt=con.prepareStatement("select sum(mac) mach from (select count(serial_no)mac from (select serial_no,current_owner_type,current_owner_id from st_lms_inv_status inv inner join st_lms_inv_model_master mm on inv.inv_model_id=mm.model_id where inv_id=1) status inner join st_lms_organization_master om on status.current_owner_id=om.organization_id where status.current_owner_type='RETAILER' and parent_id=? union all" +
					                  " select count(serial_no)mac from(select serial_no,current_owner_type,current_owner_id from st_lms_inv_status inv inner join st_lms_inv_model_master mm on inv.inv_model_id=mm.model_id where inv_id=1 )aa where current_owner_id=? and current_owner_type='AGENT')bb");
		
			pstmt.setInt(1, agentOrgId);
			pstmt.setInt(2, agentOrgId);
		   rs=pstmt.executeQuery();
		   if(rs.next()){
			   mac=rs.getInt("mach"); 
		   }
		 } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		
		
		
		return mac;
	}

	private int getNoOfMachineAtAgent(Connection con, int agentOrgId,
			String startDate, String endDate) {
		
		int mac=0;
		 PreparedStatement pstmt=null;
		 ResultSet rs=null;
		 try {
			pstmt=con.prepareStatement("select sum(assigned_total+notAssigned_total) totalTerminal from st_lms_ret_activityHistory_agentwise where status=? and date>=? and date<=? and agent_id=? group by date order by totalTerminal desc limit 1 ");
			pstmt.setString(1,"ACTIVE");
			pstmt.setString(2, startDate);
			pstmt.setString(3, endDate);
			pstmt.setInt(4, agentOrgId);
			rs=pstmt.executeQuery();
		   if(rs.next()){
			   mac=rs.getInt("totalTerminal"); 
		   }
		 } catch (SQLException e) {
			
			e.printStackTrace();
		}finally{
			try{
				if(pstmt!=null){
					pstmt.close();
				}
				if(rs!=null){
					rs.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
		return mac;
	}

	public void insertMonthlyPostDepositAgentWiseScheduler(String deploymentDate) throws LMSException{
		PreparedStatement pstmt=null;
		ResultSet resultSet=null;
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		Date lastDateDone =new java.sql.Date(calendar.getTimeInMillis());
		Date startDate=null;
		Date endDate=null;
		Connection con=null;
		try {
			
			con = DBConnect.getConnection();
			con.setAutoCommit(false);
			pstmt = con
					.prepareStatement("select DATE_ADD(end_date,INTERVAL 1 DAY) date_time from st_lms_agent_post_deposit_datewise_commission order by end_date desc  limit 1");

			resultSet = pstmt.executeQuery();
			if (resultSet.next()) {
				startDate = resultSet.getDate("date_time");

				logger.info("last daily Deposit exapnses given for "
						+ lastDateDone);
			} else {
				System.out
						.println("daily Deposit expanses generating first time");

				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

				startDate = new java.sql.Date(sdf.parse(deploymentDate)
						.getTime());

			}
			
			if (getZeroTimeDate(startDate).compareTo(
					getZeroTimeDate(lastDateDone)) >= 0) {
				return;
			}else {

				while (getZeroTimeDate(startDate).compareTo(getZeroTimeDate(lastDateDone)) < 0) {
					logger.info("Date for Deposit Commission"+ startDate.toString());
					calendar.setTime(startDate);
					calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
					endDate=new java.sql.Date(calendar.getTimeInMillis());
					insertPostDepositAgentWiseDateWiseWithCommission(startDate
									.toString(), endDate.toString(),"APPROVED",con);
					con.commit();
					calendar.add(Calendar.DAY_OF_MONTH, 1);
					startDate = new java.sql.Date(calendar.getTimeInMillis());
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
			DBConnect.closePstmt(pstmt);
		} 
		
	}	
	public void insertPostDepositAgentWiseDateWiseWithCommission(String startDate,String endDate,String status,Connection con) throws LMSException{
        PreparedStatement pstmt=null;
		
		try{
			
			pstmt=con.prepareStatement("insert into st_lms_agent_post_deposit_datewise_commission(agt_org_id,start_date,end_date,deposit_amount,deposit_comm_amount,tax_amount,charges_1,net_amount_to_pay,status) " 
					                 	+" select agt_org_id,? startDate,? endDate, depositAmt,depCommAmt,taxAmt,(charges_1*totalTerminal) charge_1,(depCommAmt-taxAmt-(charges_1*totalTerminal)) net_amt,? status from " 
					                  +" (select agt_org_id, sum(deposit_amount) depositAmt,sum(deposit_comm_amount) depCommAmt,sum(tax_amount) taxAmt,charges_1,date_time from st_lms_agent_post_deposit_daily_commission where date_time>=? and date_time<=? and status=? group by agt_org_id) dailComm "
					                  + " inner join (select max(assigned_total+notAssigned_total) totalTerminal,agent_id,date  from st_lms_ret_activityHistory_agentwise where status=? and date>=? and date<=? group by agent_id order by totalTerminal  desc ) terminalCount  on agent_id=agt_org_id order by agt_org_id " ) ;  
			pstmt.setString(1, startDate);
			pstmt.setString(2, endDate);
			pstmt.setString(3, status);
			pstmt.setString(4, startDate);
			pstmt.setString(5, endDate);
			pstmt.setString(6, status);
			pstmt.setString(7, "ACTIVE");
			pstmt.setString(8, startDate);
			pstmt.setString(9, endDate);
			logger.info("insertPostDepositAgentWiseDateWiseWithCommission query "+pstmt);
			pstmt.executeUpdate();
			
			pstmt=con.prepareStatement("update  st_lms_agent_post_deposit_daily_commission   set status=?  where date_time>=?  and date_time<=?  and status=?  ");
			pstmt.setString(1, "PROCESSED");
			pstmt.setString(2, startDate);
			pstmt.setString(3, endDate);
			pstmt.setString(4, status);
		
			logger.info("update status  query "+pstmt);
			pstmt.executeUpdate();
			
			
		}catch (Exception se) {
			se.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
			throw new LMSException("Error In insertPostDepositAgentWiseDateWiseWithCommission");
		}finally{
			try{
				if(pstmt!=null){
					pstmt.close();
				}
				
			}catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}
	
}
