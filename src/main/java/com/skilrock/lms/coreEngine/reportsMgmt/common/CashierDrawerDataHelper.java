package com.skilrock.lms.coreEngine.reportsMgmt.common;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.skilrock.lms.api.lmsWrapper.beans.LmsWrapperCashierDrawerDataReportBean;
import com.skilrock.lms.beans.CashierDrawerDataReportBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;

public class CashierDrawerDataHelper {

	public List<CashierDrawerDataReportBean> fetchCashierWiseDrawerData(Timestamp startDate,Timestamp endDate,int userId,String reportType) {
		List<CashierDrawerDataReportBean> drawerDataReportBeanList=new ArrayList<CashierDrawerDataReportBean>();
		CashierDrawerDataReportBean drawerDataReportBean = null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		Connection con =null;
		String selfCashierQry="";
		if("Self".equals(reportType)){
			selfCashierQry ="and mainTlb.user_id = "+userId;
		}
		String cashierDrawerDataQry=" select sum(credit_amt) credit_amt,sum(debit_amt)debit_amt,sum(cheque_amt)cheque_amt,sum(cheque_bounce_amt)cheque_bounce_amt,sum(cash_amt)cash_amt,sum(bank_amt)bank_amt,sum(pwt_amt)pwt_amt,((sum(credit_amt)+sum(cheque_amt)+sum(cash_amt)+sum(bank_amt))-(sum(debit_amt)+sum(cheque_bounce_amt)+sum(pwt_amt)))net_amt,um.user_id,um.user_name from(" 
			                         +	"select ifnull(sum(amount),0) 'credit_amt',0 debit_amt,0 cheque_amt,0 cheque_bounce_amt,0 cash_amt,0 bank_amt,0 pwt_amt, btm.user_id from st_lms_bo_credit_note bo,st_lms_bo_transaction_master btm where  btm.transaction_id=bo.transaction_id and (bo.transaction_type ='CR_NOTE_CASH') and ( btm.transaction_date >= ? and btm.transaction_date <= ?) group by btm.user_id " 
			                         +" union all select 0 credit_amt,ifnull(sum(amount),0) 'debit_amt',0 cheque_amt,0 cheque_bounce_amt,0 cash_amt,0 bank_amt,0 pwt_amt,btm.user_id from st_lms_bo_debit_note bo,st_lms_bo_transaction_master btm where  btm.transaction_id=bo.transaction_id and (bo.transaction_type IN ('DR_NOTE_CASH','DR_NOTE')) and ( btm.transaction_date >= ? and btm.transaction_date <= ?) group by btm.user_id" 
			                         +" union all select 0 credit_amt,0 debit_amt,ifnull(sum(bo.cheque_amt),0) 'cheque_amt',0 cheque_bounce_amt,0 cash_amt,0 bank_amt,0 pwt_amt,btm.user_id from st_lms_bo_sale_chq bo,st_lms_bo_transaction_master btm where  btm.transaction_id=bo.transaction_id and (bo.transaction_type in('CHEQUE','CLOSED')) and ( btm.transaction_date >= ? and btm.transaction_date <= ?) group by btm.user_id" 
			                         +" union all select 0 credit_amt,0 debit_amt,0 cheque_amt, ifnull(sum(bo.cheque_amt),0) 'cheque_bounce_amt',0 cash_amt,0 bank_amt,0 pwt_amt,btm.user_id from st_lms_bo_sale_chq bo,st_lms_bo_transaction_master btm where  btm.transaction_id=bo.transaction_id and (bo.transaction_type ='CHQ_BOUNCE' ) and ( btm.transaction_date >= ? and btm.transaction_date <= ?) group by btm.user_id" 
			                         +" union all select 0 credit_amt,0 debit_amt,0 cheque_amt,0 cheque_bounce_amt,ifnull(sum(amount),0) 'cash_amt',0 bank_amt,0 pwt_amt,btm.user_id from st_lms_bo_cash_transaction bo,st_lms_bo_transaction_master btm where  btm.transaction_id=bo.transaction_id  and ( btm.transaction_date >= ? and btm.transaction_date <= ?) group by btm.user_id" 
			                         +" union all select 0 credit_amt,0 debit_amt,0 cheque_amt,0 cheque_bounce_amt,0 cash_amt,ifnull(sum(amount),0) 'bank_amt',0 pwt_amt,btm.user_id from st_lms_bo_bank_deposit_transaction bo,st_lms_bo_transaction_master btm where  btm.transaction_id=bo.transaction_id  and ( btm.transaction_date >= ? and btm.transaction_date <= ?) group by btm.user_id union all select 0 credit_amt,0 debit_amt,0 cheque_amt,0 cheque_bounce_amt,0 cash_amt,0 bank_amt,ifnull(sum(net_amt),0) 'pwt_amt', btm.user_id from st_dg_bo_direct_plr_pwt bo,st_lms_bo_transaction_master btm where  btm.transaction_id=bo.transaction_id  and ( btm.transaction_date >= ? and btm.transaction_date <= ?) group by btm.user_id" 
			                         +" union all select 0 credit_amt,0 debit_amt,0 cheque_amt,0 cheque_bounce_amt,0 cash_amt,0 bank_amt,ifnull(sum(net_amt),0) 'pwt_amt', btm.user_id from st_iw_bo_direct_plr_pwt bo,st_lms_bo_transaction_master btm where  btm.transaction_id=bo.transaction_id  and ( btm.transaction_date >= ? and btm.transaction_date <= ?) group by btm.user_id)mainTlb, st_lms_user_master um where mainTlb.user_id=um.user_id "+selfCashierQry+" group by mainTlb.user_id  ";
		
		try{
			con=DBConnect.getConnection();
			pstmt=con.prepareStatement(cashierDrawerDataQry);
			pstmt.setTimestamp(1,startDate);
			pstmt.setTimestamp(2, endDate);
			pstmt.setTimestamp(3,startDate);
			pstmt.setTimestamp(4, endDate);
			pstmt.setTimestamp(5,startDate);
			pstmt.setTimestamp(6, endDate);
			pstmt.setTimestamp(7,startDate);
			pstmt.setTimestamp(8, endDate);
			pstmt.setTimestamp(9,startDate);
			pstmt.setTimestamp(10, endDate);
			pstmt.setTimestamp(11,startDate);
			pstmt.setTimestamp(12, endDate);
			pstmt.setTimestamp(13,startDate);
			pstmt.setTimestamp(14, endDate);
			pstmt.setTimestamp(15,startDate);
			pstmt.setTimestamp(16, endDate);
		System.out.println("Cashier Wise Drawer data Qry:"+pstmt);
		rs=pstmt.executeQuery();
		while(rs.next()){
			/*if(rs.getDouble("net_amt") ==0){
				continue;
			}*/
			drawerDataReportBean=new CashierDrawerDataReportBean();
			drawerDataReportBean.setCredit(rs.getString("credit_amt"));
			drawerDataReportBean.setDebit(rs.getString("debit_amt"));
			drawerDataReportBean.setTotalCash(rs.getString("cash_amt"));
			drawerDataReportBean.setTotalChq(rs.getString("cheque_amt"));
			drawerDataReportBean.setBankDeposit(rs.getString("bank_amt"));
			drawerDataReportBean.setCheqBounce(rs.getString("cheque_bounce_amt"));
			drawerDataReportBean.setWinnnigAmt(rs.getString("pwt_amt"));
			drawerDataReportBean.setNetAmt(rs.getString("net_amt"));
			drawerDataReportBean.setName(rs.getString("user_name"));
			drawerDataReportBean.setUserId(rs.getInt("user_id"));	
			drawerDataReportBeanList.add(drawerDataReportBean);
		}
			
		}catch (SQLException e) {
			e.printStackTrace();
		}
		
		return drawerDataReportBeanList;

	}
	}
