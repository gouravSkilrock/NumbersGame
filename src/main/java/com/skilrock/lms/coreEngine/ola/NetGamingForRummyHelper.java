package com.skilrock.lms.coreEngine.ola;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.api.ola.beans.OlaRummyNGDepositRepBean;
import com.skilrock.lms.api.ola.beans.OlaRummyNGPlrTxnRepBean;
import com.skilrock.lms.api.ola.beans.OlaRummyNGTxnRepBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.ola.common.OLAClient;
import com.skilrock.lms.coreEngine.ola.common.OLAConstants;
import com.skilrock.lms.coreEngine.ola.common.OLAUtility;
import com.skilrock.lms.csv.CSVReader;
public class NetGamingForRummyHelper {
	
	private static Log logger =LogFactory.getLog(NetGamingForRummyHelper.class);
	public static void main(String[] args) throws LMSException {
		/*Connection con=DBConnect.getConnection();
		insertPlrDepositDateWise(con);
		*/
	   /* DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	    Calendar cal = Calendar.getInstance();
	    cal.add(Calendar.DAY_OF_MONTH,-1);
	    endDate=dateFormat.format(new Date(cal.getTimeInMillis()))+"23:59:59";*/
		 Calendar calStart=Calendar.getInstance();
		 calStart.add(Calendar.DAY_OF_MONTH, -1);
		
		System.out.println(new Date(calStart.getTime().getTime())+" 23:59:59");
		//insertPlrDepositDateWise(con);
		//insertDepositPlrFromCsv(con);
		//insertWageredPlrFromCsv(con);
		/*Calendar calStart=Calendar.getInstance();
		calStart.add(Calendar.DAY_OF_MONTH, -7);
		Date startDate=new Date(calStart.getTime().getTime());
		System.out.println("start_date:"+startDate);
		
		calStart.add(Calendar.DAY_OF_MONTH,6);
		Date endDate=new Date(calStart.getTime().getTime());
		System.out.println("end_date:"+endDate);
		insertRetailerCommWeekely(con,2,startDate,endDate);*/
		//insertPlrConsolidateDeposit(con,startDate,endDate,2,1,2);
		//insertPlrConsolidateCommission(new Date(21),new Date(2024, 1, 1),2,2);
		//insertPlrCommissionDateWise();
		//insertPlrDepositDateWise();
		//Connection con =null;
		try{
			insertNetGamingData();
			String stDate ="2013-07-15";
			String enDate ="2013-07-21";
			//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			//new NetGamingForRummyHelper().updateNetGamingDataWeeklyWise(1,2,"CUMMULATIVE",1,new Date(sdf.parse(stDate).getTime()),new Date(sdf.parse(enDate).getTime()));
		}catch(Exception e){
			logger.error("Exception ",e);
		}finally{
		//	DBConnect.closeCon(con);
		}
		
	}
	
	public void insertDepositWageredData(File[] filesUpload,String fromDate,String olaNetGamingDistributionModel) throws ParseException, LMSException{
		Connection con=DBConnect.getConnection();
		try {
			con.setAutoCommit(false);
			insertWageredPlrFromCsv(con,filesUpload[0]);
			insertDepositPlrFromCsv(con,filesUpload[1]);
			insertWinningPlrFromCsv(con, filesUpload[2]);
			insertBonusDepositPlrFromCsvModule1(con,filesUpload[3]);
			
			insertPlrDepositDateWise(con);
			
			con.commit();
		} catch (LMSException e) {
			e.printStackTrace();
			throw new LMSException("Some problem occured while uploding files please check the sequence of files or contact Support Team");
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new LMSException("Some problem occured while uploding files please check the sequence of files or contact Support Team");
		}finally{
			try{
				con.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
	public void insertDepositWageredWinningData(File[] filesUpload,String fromDate,String olaNetGamingDistributionModel) throws ParseException{
		Connection con=DBConnect.getConnection();
		try {
			con.setAutoCommit(false);
			insertWageredPlrFromCsv(con,filesUpload[0]);
			insertDepositPlrFromCsv(con,filesUpload[1]);
			insertWinningPlrFromCsv(con, filesUpload[2]);
			insertBonusDepositPlrFromCsv(con,filesUpload[3]);
			insertPlrDepositWageredWinningDateWise(con);
			con.commit();
		} catch (LMSException e) {
			e.printStackTrace();
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try{
				con.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
	public static void insertDepositPlrFromCsv(Connection con,File depositCsvFile) throws LMSException{
        //Connection con=DBConnect.getConnection();
        int count=0;
		try{
			Statement stmt=con.createStatement();
		CSVReader reader = new CSVReader(new FileReader(depositCsvFile));
	    String [] nextLine;
	    String insertDeposit="insert into st_ola_rummy_plr_deposit_transaction(plr_id,plr_alias,transaction_id,deposit_amount,deposit_mode,date_time) values ";
            StringBuilder sb=new StringBuilder();
	    while ((nextLine = reader.readNext()) != null) {
	    	if(nextLine.length <=1){
	    		sb.delete(0, sb.length());
	    		break;
	    	}
	    	count++;
	    	String depositMode;
	    	if("PIN".equalsIgnoreCase(nextLine[4].trim())){
	    		 depositMode="Cash Card";
	    	}else{
	    		depositMode=nextLine[4].trim();
	    	}
	    	
	    	  // nextLine[] is an array of values from the line
	       sb.append("('"+nextLine[0]+"','"+nextLine[1].trim()+"','"+nextLine[2]+"',"+nextLine[3]+",'"+depositMode+"','"+nextLine[5]+"'),");
    	 //System.out.println("query:"+insertDeposit+sb.toString());
	   if(count==2000){
		   sb.deleteCharAt(sb.length() - 1);
		   System.out.println("query:"+insertDeposit+sb.toString());
		   stmt.addBatch(insertDeposit+sb.toString());
		   sb.delete(0, sb.length());
		   count=0;
	  }
	    }
	    
	    if(sb.length() > 2){
	    	sb.deleteCharAt(sb.length() - 1);
			   System.out.println("query:"+insertDeposit+sb.toString());
			   stmt.addBatch(insertDeposit+sb.toString());
			   sb.delete(0, sb.length());
			   count=0;
	    }
	    
	    stmt.executeBatch();
	}catch (FileNotFoundException fe) {
		fe.printStackTrace();
		throw new LMSException();
		// TODO: handle exception
	}
	catch (IOException e) {
		e.printStackTrace();
		throw new LMSException();
		// TODO: handle exception
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		throw new LMSException();
	}
		}
	
	
	public static void insertBonusDepositPlrFromCsvModule1(Connection con,File depositCsvFile) throws LMSException, ParseException{
        //Connection con=DBConnect.getConnection();
        int count=0;
		try{
			Statement stmt=con.createStatement();
		CSVReader reader = new CSVReader(new FileReader(depositCsvFile));
	    String [] nextLine;
	    String insertDeposit="insert into st_ola_rummy_plr_bonus_deposit_transaction(plr_id,plr_alias,promo_code_used,bonus_amount_credit,date_of_issue,expiry_date,total_no_of_chunks,no_of_chunks_released,released_amount) values ";
            StringBuilder sb=new StringBuilder();
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
            Timestamp dateOfIssue = null;
            Timestamp expiryDate = null;
	    while ((nextLine = reader.readNext()) != null) {
	    	if(nextLine.length <=1){
	    		sb.delete(0, sb.length());
	    		break;
	    	}
	    	count++;
	    	cal.setTimeInMillis(sdf.parse(nextLine[4]).getTime());
	    	dateOfIssue=new Timestamp(cal.getTimeInMillis());
	    	cal.setTimeInMillis(sdf.parse(nextLine[5]).getTime());
	    	expiryDate=new Timestamp(cal.getTimeInMillis());
	    	  // nextLine[] is an array of values from the line
	       sb.append("('"+nextLine[0]+"','"+nextLine[1].trim()+"','"+nextLine[2]+"',"+nextLine[3]+",'"+dateOfIssue+"','"+expiryDate+"','"+nextLine[6]+"','"+nextLine[7]+"',"+nextLine[8]+"),");
    	 //System.out.println("query:"+insertDeposit+sb.toString());
	   if(count==2000){
		   sb.deleteCharAt(sb.length() - 1);
		   System.out.println("query:"+insertDeposit+sb.toString());
		   stmt.addBatch(insertDeposit+sb.toString());
		   sb.delete(0, sb.length());
		   count=0;
	  }
	    }
	    
	    if(sb.length() > 2){
	    	sb.deleteCharAt(sb.length() - 1);
			   System.out.println("query:"+insertDeposit+sb.toString());
			   stmt.addBatch(insertDeposit+sb.toString());
			   sb.delete(0, sb.length());
			   count=0;
	    }
	    
	    stmt.executeBatch();
	    
		}catch (FileNotFoundException fe) {
		fe.printStackTrace();
		throw new LMSException();
		// TODO: handle exception
	}
	catch (IOException e) {
		e.printStackTrace();
		throw new LMSException();
		// TODO: handle exception
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		throw new LMSException();
	}
		}
	
	
	public static void insertBonusDepositPlrFromCsv(Connection con,File depositCsvFile) throws LMSException, ParseException{
        //Connection con=DBConnect.getConnection();
        int count=0;
		try{
			Statement stmt=con.createStatement();
		CSVReader reader = new CSVReader(new FileReader(depositCsvFile));
	    String [] nextLine;
	    String insertDeposit="insert into st_ola_rummy_plr_bonus_deposit_transaction(plr_id,plr_alias,promo_code_used,bonus_amount_credit,date_of_issue,expiry_date,total_no_of_chunks,no_of_chunks_released,released_amount) values ";
            StringBuilder sb=new StringBuilder();
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
            Timestamp dateOfIssue = null;
            Timestamp expiryDate = null;
	    while ((nextLine = reader.readNext()) != null) {
	    	if(nextLine.length <=1){
	    		sb.delete(0, sb.length());
	    		break;
	    	}
	    	count++;
	    	cal.setTimeInMillis(sdf.parse(nextLine[4]).getTime());
	    	dateOfIssue=new Timestamp(cal.getTimeInMillis());
	    	cal.setTimeInMillis(sdf.parse(nextLine[5]).getTime());
	    	expiryDate=new Timestamp(cal.getTimeInMillis());
	    	  // nextLine[] is an array of values from the line
	       sb.append("('"+nextLine[0]+"','"+nextLine[1].trim()+"','"+nextLine[2]+"',"+nextLine[3]+",'"+dateOfIssue+"','"+expiryDate+"','"+nextLine[6]+"','"+nextLine[7]+"',"+nextLine[8]+"),");
    	 //System.out.println("query:"+insertDeposit+sb.toString());
	   if(count==2000){
		   sb.deleteCharAt(sb.length() - 1);
		   System.out.println("query:"+insertDeposit+sb.toString());
		   stmt.addBatch(insertDeposit+sb.toString());
		   sb.delete(0, sb.length());
		   count=0;
	  }
	    }
	    
	    if(sb.length() > 2){
	    	sb.deleteCharAt(sb.length() - 1);
			   System.out.println("query:"+insertDeposit+sb.toString());
			   stmt.addBatch(insertDeposit+sb.toString());
			   sb.delete(0, sb.length());
			   count=0;
	    }
	    
	    stmt.executeBatch();
	    
	    String insertDepositTransactionQry="insert into st_ola_rummy_plr_deposit_transaction(plr_id,transaction_id,deposit_amount,deposit_mode,date_time,plr_alias) select plr_id,promo_code_used,bonus_amount_credit,'BONUS_DEPOSIT',date_of_issue,plr_alias from st_ola_rummy_plr_bonus_deposit_transaction where date_of_issue> ifnull((select max(date_time) from st_ola_rummy_plr_deposit_transaction where deposit_mode='BONUS_DEPOSIT'),'2012-06-16')";
	     Statement insertStmt=con.createStatement();
	     insertStmt.executeUpdate(insertDepositTransactionQry);
		
		}catch (FileNotFoundException fe) {
		fe.printStackTrace();
		throw new LMSException();
		// TODO: handle exception
	}
	catch (IOException e) {
		e.printStackTrace();
		throw new LMSException();
		// TODO: handle exception
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		throw new LMSException();
	}
		}
	
	
	public static void insertWageredPlrFromCsv(Connection con,File wageredCsvFile) throws LMSException{
      //  Connection con=DBConnect.getConnection();
        int count=0;
		try{
			Statement stmt=con.createStatement();
		CSVReader reader = new CSVReader(new FileReader(wageredCsvFile));
	    String [] nextLine;
	    String insertWagere="insert into st_ola_rummy_plr_wagered_transaction " +
		"(plr_id,plr_alias,ref_transaction_id,wagered_amount,table_id,game_id,transaction_type,game_name,date_time,amount_type) values ";
          StringBuilder sb=new StringBuilder();
	    while ((nextLine = reader.readNext()) != null) {
	    	count++;
	    	if(nextLine.length <=1){
	    		sb.delete(0, sb.length());
	    		break;
	    	}
	      sb.append("('"+nextLine[0]+"','"+nextLine[1].trim()+"','"+nextLine[2].trim()+"',"+nextLine[3]+","+nextLine[4]+","+nextLine[5]+",'"+nextLine[6]+"','"+nextLine[7]+"','"+nextLine[8]+"','"+nextLine[9]+"'),");

	   if(count==2000){
		   sb.deleteCharAt(sb.length() - 1);
		   System.out.println("query:"+insertWagere+sb.toString());
		   stmt.addBatch(insertWagere+sb.toString());
		   sb.delete(0, sb.length());
		   count=0;
		  
	   }
	    }
	    if(sb.length() > 2){
	    	sb.deleteCharAt(sb.length() - 1);
			   System.out.println("query:"+insertWagere+sb.toString());
			   stmt.addBatch(insertWagere+sb.toString());
			   sb.delete(0, sb.length());
			   count=0;
	    }
	    stmt.executeBatch();
	}catch (FileNotFoundException fe) {
		// TODO: handle exception
		throw new LMSException();
	}
	catch (IOException e) {
		// TODO: handle exception
		throw new LMSException();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		throw new LMSException();
	}
   }
	
	
	public static void insertWinningPlrFromCsv(Connection con,File winningCsvFile) throws LMSException{
	      //  Connection con=DBConnect.getConnection();
	        int count=0;
			try{
				Statement stmt=con.createStatement();
			CSVReader reader = new CSVReader(new FileReader(winningCsvFile));
		    String [] nextLine;
		    String insertWinning="insert into st_ola_rummy_plr_winning_transaction(plr_id,plr_alias,table_id,cash_win_amount,bb_win_amount,chip_win_amount,game_name,date_time) values ";
	          StringBuilder sb=new StringBuilder();
		    while ((nextLine = reader.readNext()) != null) {
		    	count++;
		    	if(nextLine.length <=1){
		    		sb.delete(0, sb.length());
		    		break;
		    	}
		      sb.append("('"+nextLine[0]+"','"+nextLine[1].trim()+"','"+nextLine[2].trim()+"',"+nextLine[3]+","+nextLine[4]+","+nextLine[5]+",'"+nextLine[6]+"','"+nextLine[7]+"'),");

		   if(count==2000){
			   sb.deleteCharAt(sb.length() - 1);
			   System.out.println("query:"+insertWinning+sb.toString());
			   stmt.addBatch(insertWinning+sb.toString());
			   sb.delete(0, sb.length());
			   count=0;
			  
		   }
		    }
		    if(sb.length() > 2){
		    	sb.deleteCharAt(sb.length() - 1);
				   System.out.println("query:"+insertWinning+sb.toString());
				   stmt.addBatch(insertWinning+sb.toString());
				   sb.delete(0, sb.length());
				   count=0;
		    }
		    stmt.executeBatch();
		}catch (FileNotFoundException fe) {
			// TODO: handle exception
			throw new LMSException();
		}
		catch (IOException e) {
			// TODO: handle exception
			throw new LMSException();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new LMSException();
		}
	   }
	
	public static void insertPlrDepositWageredWinningDateWise(Connection con){
		//Connection con=DBConnect.getConnection();
		String getLastDate="select max(date) as date from st_ola_rummy_plr_deposit_datewise";
		try{
		Statement stmt=con.createStatement();
		ResultSet rs=stmt.executeQuery(getLastDate);
		String last_date="2000-11-01";
		if(rs.next()){
			if(rs.getString("date") != null){
			last_date=rs.getString("date");
			}
		}
		System.out.println("LAST DATE:"+last_date);
		
		String insertQuery="insert into st_ola_rummy_plr_deposit_datewise(plr_id,ref_ret_org_id,date,debit_card_deposit,credit_card_deposit,net_banking_deposit,cash_card_deposit,ola_bind_deposit,tech_process_deposit,ola_non_bind_deposit,bonus_deposit,inhouse_deposit,wire_transfer_deposit,status,total_deposit,total_play,total_winning) select plr_alias,ref_user_org_id,date,sum(debit_card_deposit),sum(credit_card_deposit),sum(net_banking_deposit),sum(cash_card_deposit),sum(ola_bind_deposit),sum(tech_process_deposit),sum(ola_non_bind_deposit),sum(bonus_deposit),sum(inhouse_deposit),sum(wire_transfer_deposit),status,sum(total_deposit) total_deposit,sum(total_play) total_play,sum(total_win) total_win from " 
			               +"(select plr_alias,ref_user_org_id,date(date_time) as date,debit_card_deposit,credit_card_deposit,net_banking_deposit,cash_card_deposit,ifnull(ola_bind_deposit,0.00)ola_bind_deposit,tech_process_deposit,ifnull(ola_non_bind_deposit,0.00)ola_non_bind_deposit,bonus_deposit,inhouse_deposit,wire_transfer_deposit,if(ola_deposit=ifnull(amount,0.00),'MATCHED','UNMATCHED') as status,total_deposit,0 as total_play,0 as total_win from(select plr_alias,sum(total_deposit) as total_deposit,sum(debit_card_deposit)as debit_card_deposit,sum(credit_card_deposit) as credit_card_deposit,sum(net_banking_deposit) as net_banking_deposit," 
			               +"sum(cash_card_deposit) as cash_card_deposit,sum(ola_deposit) as ola_deposit,sum(tech_process_deposit) tech_process_deposit,sum(bonus_deposit) bonus_deposit,sum(inhouse_deposit) inhouse_deposit,sum(wire_transfer_deposit) wire_transfer_deposit,date_time,ref_user_org_id from( select plr_alias,sum(deposit_amount) as total_deposit,if(deposit_mode='Debit Card',sum(deposit_amount),0.0) as debit_card_deposit,if(deposit_mode='Credit Card',sum(deposit_amount),0.0) as credit_card_deposit,if(deposit_mode='Net Banking',sum(deposit_amount),0.0) as net_banking_deposit,if(deposit_mode='Cash Card',sum(deposit_amount),0.0) as cash_card_deposit,if(deposit_mode='OLA',sum(deposit_amount),0.0) as ola_deposit," 
			               +"if(deposit_mode='TechProcess',sum(deposit_amount),0.0) as tech_process_deposit,if(deposit_mode='BONUS_DEPOSIT',sum(deposit_amount),0.0) as bonus_deposit,if(deposit_mode='Inhouse',sum(deposit_amount),0.0) as inhouse_deposit,if(deposit_mode='Wire Transfer',sum(deposit_amount),0.0) as wire_transfer_deposit,deposit_mode,date_time,ref_user_org_id from st_ola_rummy_plr_deposit_transaction tt,st_ola_affiliate_plr_mapping mm  where mm.player_id=tt.plr_alias and date(date_time)> '"+last_date+"'  group by plr_alias,deposit_mode,date(date_time) order by date(date_time)) ddd group by plr_alias,date(date_time))deposit_tlb left join (select player_id,sum(ola_non_bind_deposit) as ola_non_bind_deposit,sum(ola_bind_deposit) as ola_bind_deposit,sum(amount) amount,verification_date from(select ifnull(mapTlb.player_id,odTlb.player_id) player_id," 
			               +"if(ref_user_org_id is NULL,'OLA_Non_Bind','OLA_Bind') as ola_deposit_mode,if(ref_user_org_id is NULL,sum(amount),0.0) as ola_non_bind_deposit,if(ref_user_org_id is NOT NULL,sum(amount),0.0) as ola_bind_deposit,ifnull(ref_user_org_id,retailer_org_id) as ret_org_id,sum(amount) as amount,verification_date,verification_status from st_ola_affiliate_plr_mapping mapTlb right join (select player_id,sum(amount) amount,verification_date,verification_status,retailer_org_id from st_ola_pin_rep_rm_2 rr,st_ola_ret_deposit dd where rr.lms_transaction_id=dd.transaction_id and rr.player_id=dd.party_id and date(rr.verification_date)> '"+last_date+"' and " 
			               +" verification_status='DONE' group by player_id,retailer_org_id,date(verification_date),verification_status ) odTlb on mapTlb.player_id=odTlb.player_id and ref_user_org_id=retailer_org_id group by player_id,date(verification_date),ola_deposit_mode)aaa group by player_id,date(verification_date)) ola_tlb on deposit_tlb.plr_alias=ola_tlb.player_id and date(deposit_tlb.date_time)=date(verification_date) union all select plr_alias,ref_user_org_id,date(date_time) date,0 debit_card_deposit,0 credit_card_deposit,0 net_banking_deposit,0 cash_card_deposit,0 ola_bind_deposit,0 tech_process_deposit,0 ola_non_bind_deposit,0 bonus_deposit,0 inhouse_deposit,0 wire_transfer_deposit,'MATCHED'status,0 total_deposit," 
			               +"sum(wagered_amount)total_play,0 total_win from st_ola_rummy_plr_wagered_transaction wt inner join st_ola_affiliate_plr_mapping pm on wt.plr_alias=pm.player_id where date(date_time)> '"+last_date+"' group by plr_alias,date(date_time) union all select plr_alias,ref_user_org_id,date(date_time) date,0 debit_card_deposit,0 credit_card_deposit,0 net_banking_deposit,0 cash_card_deposit,0 ola_bind_deposit,0 tech_process_deposit,0 ola_non_bind_deposit,0 bonus_deposit,0 inhouse_deposit,0 wire_transfer_deposit,'MATCHED'status,0 total_deposit,0 total_play,sum(cash_win_amount)+sum(bb_win_amount) total_win from st_ola_rummy_plr_winning_transaction wt inner join st_ola_affiliate_plr_mapping pm on wt.plr_alias=pm.player_id " 
			               +" where date(date_time)> '"+last_date+"' group by plr_alias,date(date_time) )dateWiseTlb where (total_win>0.0 or total_play>0 or total_deposit>0) group by plr_alias,date";
	
		Statement insertStmt=con.createStatement();
		System.out.println(insertQuery);
		insertStmt.executeUpdate(insertQuery);
		
	}catch (SQLException e) {
		e.printStackTrace();
	}
	}
	
	
	
	public static void insertPlrDepositDateWise(Connection con) throws LMSException{
		//Connection con=DBConnect.getConnection();
		Statement stmt=null;
		ResultSet rs=null;
		String getLastDate="select max(date) as date from st_ola_rummy_plr_cummulative_deposit_datewise";
		try{
		stmt=con.createStatement();
		rs=stmt.executeQuery(getLastDate);
		String last_date="2000-11-01";
		if(rs.next()){
			if(rs.getString("date") != null){
			last_date=rs.getString("date");
			}
		}
		logger.info("LAST DATE DateWise:"+last_date);
		
		String insertQuery=" insert into st_ola_rummy_plr_cummulative_deposit_datewise(plr_id,ref_ret_org_id,date,dep_wallet_id,debit_card_deposit,credit_card_deposit,net_banking_deposit,cash_card_deposit,ola_bind_deposit,tech_process_deposit,ola_non_bind_deposit,wire_transfer_deposit,cheque_transfer_deposit,cash_payment_deposit,status,total_deposit,total_play) " 
							+" select plr_alias,ref_user_org_id,date,wallet_id,sum(debit_card_deposit),sum(credit_card_deposit),sum(net_banking_deposit),sum(cash_card_deposit),sum(ola_bind_deposit),sum(tech_process_deposit),sum(ola_non_bind_deposit),sum(wire_transfer_deposit),sum(cheque_transfer_deposit),sum(cash_payment_deposit),status,sum(total_deposit) total_deposit,sum(total_play) total_play from( select plr_alias,ref_user_org_id,date(date_time) as date,deposit_tlb.wallet_id,debit_card_deposit,credit_card_deposit,net_banking_deposit,cash_card_deposit,ifnull(ola_bind_deposit,0.00)ola_bind_deposit,tech_process_deposit,ifnull(ola_non_bind_deposit,0.00)ola_non_bind_deposit,wire_transfer_deposit,cheque_transfer_deposit,cash_payment_deposit,if(ola_deposit=ifnull(amount,0.00),'MATCHED','UNMATCHED') as status,total_deposit,0 as total_play from("
							+" select plr_alias,dep_wallet_id wallet_id, sum(total_deposit) as total_deposit,sum(debit_card_deposit)as debit_card_deposit,sum(credit_card_deposit) as credit_card_deposit,sum(net_banking_deposit) as net_banking_deposit, sum(cash_card_deposit) as cash_card_deposit,sum(ola_deposit) as ola_deposit,sum(tech_process_deposit) tech_process_deposit,sum(wire_transfer_deposit) wire_transfer_deposit,sum(cheque_transfer_deposit) cheque_transfer_deposit,sum(cash_payment_deposit) cash_payment_deposit,date_time,ref_user_org_id from( select plr_alias,dep_wallet_id,sum(deposit_amount) as total_deposit,if(deposit_mode='DEBIT CARD',sum(deposit_amount),0.0) as debit_card_deposit,if(deposit_mode='CREDIT CARD',sum(deposit_amount),0.0) as credit_card_deposit,if(deposit_mode='Net Banking',sum(deposit_amount),0.0) as net_banking_deposit,if(deposit_mode='CASH CARD',sum(deposit_amount),0.0) as cash_card_deposit,if(deposit_mode='OLA',sum(deposit_amount),0.0) as ola_deposit, "
							+" if(deposit_mode in('TechProcess','CITRUS','Essecom'),sum(deposit_amount),0.0) as tech_process_deposit,if(deposit_mode='Wire Transfer',sum(deposit_amount),0.0) as wire_transfer_deposit,if(deposit_mode='CHEQUE_TRANS',sum(deposit_amount),0.0) as cheque_transfer_deposit,if(deposit_mode='CASH_PAYMENT',sum(deposit_amount),0.0) as cash_payment_deposit,deposit_mode,date_time,ref_user_org_id from st_ola_rummy_plr_deposit_transaction tt,st_ola_affiliate_plr_mapping mm  where mm.player_id=tt.plr_alias and mm.wallet_id =tt.dep_wallet_id and date(date_time)> '"+last_date+"'   group by plr_alias,deposit_mode,date(date_time),dep_wallet_id order by date(date_time)) ddd group by plr_alias,date(date_time),dep_wallet_id )deposit_tlb left join ( select player_id,sum(ola_non_bind_deposit) as ola_non_bind_deposit,sum(ola_bind_deposit) as ola_bind_deposit,sum(amount) amount,trnDate,wallet_id from( select ifnull(mapTlb.player_id,odTlb.party_id) player_id,if(ref_user_org_id is NULL,'OLA_Non_Bind','OLA_Bind') as ola_deposit_mode,if(ref_user_org_id is NULL,sum(amount),0.0) as ola_non_bind_deposit,if(ref_user_org_id is NOT NULL,sum(amount),0.0) as ola_bind_deposit,ifnull(ref_user_org_id,retailer_org_id) as ret_org_id,sum(amount) as amount,trnDate,odTlb.wallet_id  from st_ola_affiliate_plr_mapping mapTlb right join ( "
							+" select party_id,sum(amount) amount,olaDeposit.retailer_org_id,date(transaction_date) trnDate,olaDeposit.wallet_id from (select  retDep.transaction_id ,retDep.wallet_id,retDep.party_id,sum(retDep.deposit_amt) amount,retDep.retailer_org_id from st_ola_ret_deposit  retDep left join st_ola_ret_deposit_refund retDepRef on ola_ref_transaction_id=retDep.transaction_id where ola_ref_transaction_id is null group by  retDep.party_id,retDep.retailer_org_id,retDep.transaction_id ) olaDeposit inner join st_lms_retailer_transaction_master  tm on  olaDeposit.transaction_id= tm.transaction_id where  date(transaction_date)> '"+last_date+"'  group by party_id,olaDeposit.retailer_org_id,date(transaction_date),olaDeposit.wallet_id ) odTlb  on mapTlb.player_id=odTlb.party_id and ref_user_org_id=retailer_org_id and mapTlb.wallet_id=odTlb.wallet_id group by player_id,trnDate,ola_deposit_mode,odTlb.wallet_id  ) aaa  group by player_id,trnDate,wallet_id ) ola_tlb on deposit_tlb.plr_alias=ola_tlb.player_id and date(deposit_tlb.date_time)=date(trnDate) and deposit_tlb.wallet_id =ola_tlb.wallet_id union all select plr_alias,ref_user_org_id,date(date_time) date,dep_wallet_id wallet_id,0 debit_card_deposit,0 credit_card_deposit,0 net_banking_deposit,0 cash_card_deposit,0 ola_bind_deposit,0 tech_process_deposit,0 ola_non_bind_deposit,0 wire_transfer_deposit,0 cheque_transfer_deposit,0 cash_payment_deposit,'MATCHED'status,0 total_deposit,sum(wagered_amount)total_play from st_ola_rummy_plr_wagered_transaction wt inner join st_ola_affiliate_plr_mapping pm on wt.plr_alias=pm.player_id and wt.dep_wallet_id=pm.wallet_id  where date(date_time)> '"+last_date+"' and (ref_transaction_id >0 ) group by plr_alias,date(date_time),wallet_id "
							+" )dateWiseTlb where ( total_play>0 or total_deposit>0)   group by plr_alias,date,wallet_id ";
		/*String insertQuery="insert into st_ola_rummy_plr_cummulative_deposit_datewise(plr_id,ref_ret_org_id,date,debit_card_deposit,credit_card_deposit,net_banking_deposit,cash_card_deposit,ola_bind_deposit,tech_process_deposit,ola_non_bind_deposit,wire_transfer_deposit,status,total_deposit,total_play) select plr_alias,ref_user_org_id,date,sum(debit_card_deposit),sum(credit_card_deposit),sum(net_banking_deposit),sum(cash_card_deposit),sum(ola_bind_deposit),sum(tech_process_deposit),sum(ola_non_bind_deposit),sum(wire_transfer_deposit),status,sum(total_deposit) total_deposit,sum(total_play) total_play from " 
			               +"(select plr_alias,ref_user_org_id,date(date_time) as date,debit_card_deposit,credit_card_deposit,net_banking_deposit,cash_card_deposit,ifnull(ola_bind_deposit,0.00)ola_bind_deposit,tech_process_deposit,ifnull(ola_non_bind_deposit,0.00)ola_non_bind_deposit,wire_transfer_deposit,if(ola_deposit=ifnull(amount,0.00),'MATCHED','UNMATCHED') as status,total_deposit,0 as total_play from(select plr_alias,sum(total_deposit) as total_deposit,sum(debit_card_deposit)as debit_card_deposit,sum(credit_card_deposit) as credit_card_deposit,sum(net_banking_deposit) as net_banking_deposit," 
			               +"sum(cash_card_deposit) as cash_card_deposit,sum(ola_deposit) as ola_deposit,sum(tech_process_deposit) tech_process_deposit,sum(wire_transfer_deposit) wire_transfer_deposit,date_time,ref_user_org_id from( select plr_alias,sum(deposit_amount) as total_deposit,if(deposit_mode='Debit Card',sum(deposit_amount),0.0) as debit_card_deposit,if(deposit_mode='Credit Card',sum(deposit_amount),0.0) as credit_card_deposit,if(deposit_mode='Net Banking',sum(deposit_amount),0.0) as net_banking_deposit,if(deposit_mode='Cash Card',sum(deposit_amount),0.0) as cash_card_deposit,if(deposit_mode='OLA',sum(deposit_amount),0.0) as ola_deposit," 
			               +"if(deposit_mode in('TechProcess','CITRUS','Essecom'),sum(deposit_amount),0.0) as tech_process_deposit,if(deposit_mode='Wire Transfer',sum(deposit_amount),0.0) as wire_transfer_deposit,deposit_mode,date_time,ref_user_org_id from st_ola_rummy_plr_deposit_transaction tt,st_ola_affiliate_plr_mapping mm  where mm.player_id=tt.plr_alias and date(date_time)> '"+last_date+"'  group by plr_alias,deposit_mode,date(date_time) order by date(date_time)) ddd group by plr_alias,date(date_time))deposit_tlb left join (select player_id,sum(ola_non_bind_deposit) as ola_non_bind_deposit,sum(ola_bind_deposit) as ola_bind_deposit,sum(amount) amount,verification_date from(select ifnull(mapTlb.player_id,odTlb.player_id) player_id," 
			               +"if(ref_user_org_id is NULL,'OLA_Non_Bind','OLA_Bind') as ola_deposit_mode,if(ref_user_org_id is NULL,sum(amount),0.0) as ola_non_bind_deposit,if(ref_user_org_id is NOT NULL,sum(amount),0.0) as ola_bind_deposit,ifnull(ref_user_org_id,retailer_org_id) as ret_org_id,sum(amount) as amount,verification_date,verification_status from st_ola_affiliate_plr_mapping mapTlb right join (select player_id,sum(amount) amount,verification_date,verification_status,retailer_org_id from st_ola_pin_rep_rm_2 rr,st_ola_ret_deposit dd where rr.lms_transaction_id=dd.transaction_id and rr.player_id=dd.party_id and date(rr.verification_date)> '"+last_date+"' and " 
			               +" verification_status='DONE' group by player_id,retailer_org_id,date(verification_date),verification_status ) odTlb on mapTlb.player_id=odTlb.player_id and ref_user_org_id=retailer_org_id group by player_id,date(verification_date),ola_deposit_mode)aaa group by player_id,date(verification_date)) ola_tlb on deposit_tlb.plr_alias=ola_tlb.player_id and date(deposit_tlb.date_time)=date(verification_date) union all select plr_alias,ref_user_org_id,date(date_time) date,0 debit_card_deposit,0 credit_card_deposit,0 net_banking_deposit,0 cash_card_deposit,0 ola_bind_deposit,0 tech_process_deposit,0 ola_non_bind_deposit,0 wire_transfer_deposit,'MATCHED'status,0 total_deposit," 
			               +"sum(wagered_amount)total_play from st_ola_rummy_plr_wagered_transaction wt inner join st_ola_affiliate_plr_mapping pm on wt.plr_alias=pm.player_id where date(date_time)> '"+last_date+"' and (ref_transaction_id >0 or amount_type='Real Chips') group by plr_alias,date(date_time) )dateWiseTlb where ( total_play>0 or total_deposit>0) group by plr_alias,date";
		*/
	PreparedStatement insertPstmt=con.prepareStatement(insertQuery);
	logger.info("insert st_ola_rummy_plr_cummulative_deposit_datewise Qry:"+insertQuery);
	int updatedRow=insertPstmt.executeUpdate();
	logger.info("row Effected"+updatedRow);
	con.commit();
	}catch(SQLException e){
		logger.error("SQL Exception",e);
		try {
			con.rollback();
		} catch (SQLException e1) {
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		}
		throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
	}catch(Exception e){
		logger.error(" Exception"+e);
		throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
	}finally{
		DBConnect.closeRs(rs);
		DBConnect.closeStmt(stmt);
	}
	}
	
	
	public void updateNetGamingDataWeeklyWise(int agtTierId,int retTierId,String olaNetGamingDistributionModel,int settlementAfter,Date startDate,Date endDate) throws LMSException{
		
		//System.out.println("startDate:"+startDate);
		//System.out.println("endDate:"+endDate);
		logger.info("updateNetGamingData statDate:"+startDate+" EndDate :"+endDate);
		 
		Connection con=null;
		//Statement stmt=null;
		//ResultSet rsWallet=null;
		try{
		
		con=DBConnect.getConnection();
			
		Date settlementDate=getSettlementDateForPlayerCommission(settlementAfter,con);
		
		//String selectWalletQry="select wallet_id from st_ola_wallet_master where wallet_status='ACTIVE'";
		//stmt=con.createStatement();
		con.setAutoCommit(false);
		//rsWallet=stmt.executeQuery(selectWalletQry);
		  //settlementPlayerCommission(con,settlementDate);
		for(Entry<Integer, String> entry :OLAUtility.getOlaWalletDataMap().entrySet()){
			  int walletId=entry.getKey();
			  if("DEPOSITRATIO".equalsIgnoreCase(olaNetGamingDistributionModel)){
				  insertPlrConsolidateDeposit(con, startDate, endDate, walletId, agtTierId, retTierId);
				  insertRetailerCommWeekely(con,walletId,startDate,endDate);
			  }else if("CUMMULATIVE".equalsIgnoreCase(olaNetGamingDistributionModel)){      
				
				  if(getZeroTimeDate(settlementDate).compareTo(getZeroTimeDate(startDate))==0){
				  settlementPlayerCommission(con,settlementDate);
				  }
				  
				  insertPlrConsolidateCommission(con,startDate,endDate,walletId,agtTierId,retTierId);
				  insertCummulativeRetailerCommWeekely(con,walletId,startDate,endDate);
			  }
			  
			  
			  
		  }
		  
		  con.commit();
		}
		catch (SQLException e) {
		logger.info("SQL EXCEPTION",e);
		throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			DBConnect.closeCon(con);
						
		}
		
	}
	
     public void updateNetGamingDataMonthlyWise(int agtTierId,int retTierId,String olaNetGamingDistributionModel,int settlementAfter,Date startDate,Date endDate ) throws LMSException{
    	 System.out.println("startDate:"+startDate);
 		System.out.println("endDate:"+endDate);
 		
    	/* Calendar calendar = Calendar.getInstance();
		  int year = calendar.get(Calendar.YEAR);
		  System.out.println(year);
		  int month = calendar.get(Calendar.MONTH);
		  System.out.println(month);
		  int date = 1;
		  
		 
		  calendar.set(year, month-1, date);
		  
		  Date startDate=new Date(calendar.getTime().getTime());
	 		System.out.println("start_date:"+startDate);
	 		int noOfdays=calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
			  System.out.println(noOfdays);
			  calendar.set(year, month-1, noOfdays);
			  Date endDate=new Date(calendar.getTime().getTime());
		 		System.out.println("end_date:"+endDate);*/
 		
 		Connection con=null;
 		//Statement stmt=null;
 		//ResultSet rsWallet=null;
 		try{
 			con=DBConnect.getConnection();
 		/*String selectWalletQry="select wallet_id from st_ola_wallet_master where wallet_status='ACTIVE'";
 		 stmt=con.createStatement();*/
 		 con.setAutoCommit(false);
 		  //rsWallet=stmt.executeQuery(selectWalletQry);
 		  
 		 for(Entry<Integer, String> entry :OLAUtility.getOlaWalletDataMap().entrySet()){
 			  int walletId=entry.getKey();
 			  
 			 if("DEPOSITRATIO".equalsIgnoreCase(olaNetGamingDistributionModel)){
				  insertPlrConsolidateDeposit(con, startDate, endDate, walletId, agtTierId, retTierId);
			  }else if("CUMMULATIVE".equalsIgnoreCase(olaNetGamingDistributionModel)){
				  insertPlrConsolidateCommission(con,startDate,endDate,walletId,agtTierId,retTierId);
			  }
 			 insertRetailerCommMonthly(con,walletId,startDate,endDate);
 			  
 		  }
 		  
 		  con.commit();
 		}
 		catch (SQLException e) {
 			e.printStackTrace();
 		throw new LMSException("Error in Net Gaming ");
 		}
 		finally{
 			try{
 				if(con != null){
 				con.close();
 				}
 			}
 			catch (Exception e) {
 				e.printStackTrace();
 				throw new LMSException();
 			}
 		}
	}
	
     
     public static void settlementPlayerCommission(Connection con,Date settlementDate) throws LMSException{
    	 String insertSettlementHistoryQry= " insert into st_ola_rummy_plr_deposit_comm_history(plr_id,debit_card_deposit,credit_card_deposit,net_banking_deposit,cash_card_deposit,ola_bind_deposit,ola_non_bind_deposit,tech_process_deposit,wire_transfer_deposit,cheque_transfer_deposit,cash_payment_deposit,total_deposit,total_play,ret_debit_card_comm,ret_credit_card_comm,ret_net_banking_comm,ret_cash_card_comm,ret_ola_bind_comm,ret_ola_non_bind_comm,ret_tech_process_comm,ret_wire_transfer_comm,ret_cheque_transfer_comm,ret_cash_payment_comm,agt_debit_card_comm,agt_credit_card_comm,agt_net_banking_comm,agt_cash_card_comm,agt_ola_bind_comm,agt_ola_non_bind_comm,agt_tech_process_comm,agt_wire_transfer_comm,agt_cheque_transfer_comm,agt_cash_payment_comm,till_date,settlement_upto_date,status) select plr_id,debit_card_deposit,credit_card_deposit,net_banking_deposit,cash_card_deposit,ola_bind_deposit,ola_non_bind_deposit,tech_process_deposit,wire_transfer_deposit,cheque_transfer_deposit,cash_payment_deposit,total_deposit,total_play,ret_debit_card_comm,ret_credit_card_comm,ret_net_banking_comm,ret_cash_card_comm,ret_ola_bind_comm,ret_ola_non_bind_comm,ret_tech_process_comm,ret_wire_transfer_comm,ret_cheque_transfer_comm,ret_cash_payment_comm,agt_debit_card_comm,agt_credit_card_comm,agt_net_banking_comm,agt_cash_card_comm,agt_ola_bind_comm,agt_ola_non_bind_comm,agt_tech_process_comm,agt_wire_transfer_comm,agt_cheque_transfer_comm,agt_cash_payment_comm,till_date,settlement_upto_date,status from st_ola_rummy_plr_deposit_comm_status  ";

    		 
    		 
    	//	 "insert into st_ola_rummy_plr_deposit_comm_history(plr_id,debit_card_deposit,credit_card_deposit,net_banking_deposit,cash_card_deposit,ola_bind_deposit,ola_non_bind_deposit,tech_process_deposit,wire_transfer_deposit,total_deposit,total_play,ret_debit_card_comm,ret_credit_card_comm,ret_net_banking_comm,ret_cash_card_comm,ret_ola_bind_comm,ret_ola_non_bind_comm,ret_tech_process_comm,ret_wire_transfer_comm,agt_debit_card_comm,agt_credit_card_comm,agt_net_banking_comm,agt_cash_card_comm,agt_ola_bind_comm,agt_ola_non_bind_comm,agt_tech_process_comm,agt_wire_transfer_comm,till_date,settlement_upto_date,status) select plr_id,debit_card_deposit,credit_card_deposit,net_banking_deposit,cash_card_deposit,ola_bind_deposit,ola_non_bind_deposit,tech_process_deposit,wire_transfer_deposit,total_deposit,total_play,ret_debit_card_comm,ret_credit_card_comm,ret_net_banking_comm,ret_cash_card_comm,ret_ola_bind_comm,ret_ola_non_bind_comm,ret_tech_process_comm,ret_wire_transfer_comm,agt_debit_card_comm,agt_credit_card_comm,agt_net_banking_comm,agt_cash_card_comm,agt_ola_bind_comm,agt_ola_non_bind_comm,agt_tech_process_comm,agt_wire_transfer_comm,till_date,settlement_upto_date,status from st_ola_rummy_plr_deposit_comm_status";
    	 
    	 String settlementPlrCommStatusQry= "update st_ola_rummy_plr_deposit_comm_status set total_play=0.0,ret_debit_card_comm=0.0,ret_credit_card_comm=0.0,ret_net_banking_comm=0.0,ret_cash_card_comm=0.0,ret_ola_bind_comm=0.0,ret_ola_non_bind_comm=0.0,ret_tech_process_comm=0.0,ret_wire_transfer_comm=0.0,ret_cheque_transfer_comm=0.0,ret_cash_payment_comm=0.0,agt_debit_card_comm=0.0,agt_credit_card_comm=0.0,agt_net_banking_comm=0.0,agt_cash_card_comm=0.0,agt_ola_bind_comm=0.0,agt_ola_non_bind_comm=0.0,agt_tech_process_comm=0.0,agt_wire_transfer_comm=0.0,agt_cheque_transfer_comm=0.0,agt_cash_payment_comm=0.0,till_date='"+settlementDate+"' - INTERVAL 1 DAY,settlement_upto_date='"+settlementDate+"' - INTERVAL 1 DAY where status='DONE'";
    		 
    //		 "update st_ola_rummy_plr_deposit_comm_status set total_play=0.0,ret_debit_card_comm=0.0,ret_credit_card_comm=0.0,ret_net_banking_comm=0.0,ret_cash_card_comm=0.0,ret_ola_bind_comm=0.0,ret_ola_non_bind_comm=0.0,ret_tech_process_comm=0.0,ret_wire_transfer_comm=0.0,agt_debit_card_comm=0.0,agt_credit_card_comm=0.0,agt_net_banking_comm=0.0,agt_cash_card_comm=0.0,agt_ola_bind_comm=0.0,agt_ola_non_bind_comm=0.0,agt_tech_process_comm=0.0,agt_wire_transfer_comm=0.0,till_date='"+settlementDate+"' - INTERVAL 1 DAY,settlement_upto_date='"+settlementDate+"' - INTERVAL 1 DAY where status='DONE'";
    	 Statement stmt =null;
    		try{
    			
    			stmt=con.createStatement();
    			logger.info("insertSettlementHistoryQry"+insertSettlementHistoryQry);
    			logger.info("settlementPlrCommStatusQry"+settlementPlrCommStatusQry);
    			stmt.executeUpdate(insertSettlementHistoryQry);
    			stmt.executeUpdate(settlementPlrCommStatusQry);
    			
    		}catch (SQLException e) {
    			logger.info("SQL Exception",e);
    			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
    		}finally{
    			DBConnect.closeStmt(stmt);
    		}
     
     
     }
     //for module1 of net Gaming
	public static void insertPlrConsolidateCommission(Connection con,Date startDate,Date endDate,int wallet_id,int agtTierId,int retTierId) throws LMSException{
				
		String updatePlrStatus=" update st_ola_rummy_plr_deposit_comm_status ss inner join (select * from(select plr_id,dep_wallet_id,date,if(status='MATCHED','CONTINUE','HOLD')status from st_ola_rummy_plr_cummulative_deposit_datewise where date>='"+startDate+"' and date <= '"+endDate+"'  order by status desc) dateWise  group by plr_id,dep_wallet_id ) dd on ss.plr_id=dd.plr_id and ss.dep_wallet_id=dd.dep_wallet_id set ss.status=dd.status ";   
			
			//"update st_ola_rummy_plr_deposit_comm_status ss inner join (select * from(select plr_id,date,if(status='MATCHED','CONTINUE','HOLD')status from st_ola_rummy_plr_cummulative_deposit_datewise where date>='"+startDate+"' and date <= '"+endDate+"'  order by status desc) dateWise  group by plr_id ) dd on ss.plr_id=dd.plr_id  set ss.status=dd.status";

		String insertRetailerCommQuery=" insert into st_ola_rummy_plr_retailer_commission(plr_id,start_date,end_date,ret_debit_card_comm,ret_credit_card_comm,ret_net_banking_comm,ret_cash_card_comm,ret_ola_bind_comm,ret_ola_non_bind_comm,ret_tech_process_comm,ret_wire_transfer_comm,ret_cheque_transfer_comm,ret_cash_payment_comm)"
									   +" select plr_id,'"+startDate+"','"+endDate+"',((ifnull((debit_card_deposit/total_deposit),0.0)*total_play)*(debit_card_comm_rate*.01))-ret_debit_card_comm as ret_debit_card_comm,((ifnull((credit_card_deposit/total_deposit),0.0)*total_play)*(credit_card_comm_rate*.01)) -ret_credit_card_comm as credit_card_comm,((ifnull((net_banking_deposit/total_deposit),0.0)*total_play)*(net_banking_comm_rate*.01))-ret_net_banking_comm as ret_net_banking_comm,((ifnull((cash_card_deposit/total_deposit),0.0)*total_play)*(cash_card_comm_rate*.01))-ret_cash_card_comm as ret_cash_card_comm,((ifnull((ola_bind_deposit/total_deposit),0.0)*total_play)*(ola_bind_comm_rate*.01))-ret_ola_bind_comm as ret_ola_bind_comm,((ifnull((ola_non_bind_deposit/total_deposit),0.0)*total_play)*(ola_non_bind_comm_rate*.01))-ret_ola_non_bind_comm as ret_ola_non_bind_comm,((ifnull((tech_process_deposit/total_deposit),0.0)*total_play)*(tech_process_comm_rate*.01))-ret_tech_process_comm as ret_tech_process_comm,((ifnull((wire_transfer_deposit/total_deposit),0.0)*total_play)*(wire_transfer_comm_rate*.01))-ret_wire_transfer_comm as ret_wire_transfer_comm,((ifnull((cheque_transfer_deposit/total_deposit),0.0)*total_play)*(cheque_transfer_comm_rate*.01))-ret_cheque_transfer_comm as ret_cheque_transfer_comm,((ifnull((cash_payment_deposit/total_deposit),0.0)*total_play)*(cash_payment_comm_rate*.01))-ret_cash_payment_comm as ret_cash_payment_comm " 
									   +" from(select tlb1.plr_id,tlb1.date,tlb1.dep_wallet_id,ifnull((sum(tlb1.debit_card_deposit)+tlb2.debit_card_deposit),0.0) as debit_card_deposit,ifnull((sum(tlb1.credit_card_deposit)+tlb2.credit_card_deposit),0.0) as credit_card_deposit,ifnull((sum(tlb1.net_banking_deposit)+tlb2.net_banking_deposit),0.0) as net_banking_deposit,ifnull((sum(tlb1.cash_card_deposit)+tlb2.cash_card_deposit),0.0) as cash_card_deposit,ifnull((sum(tlb1.ola_bind_deposit)+tlb2.ola_bind_deposit),0.0) as ola_bind_deposit,ifnull((sum(tlb1.ola_non_bind_deposit)+tlb2.ola_non_bind_deposit),0.0) as ola_non_bind_deposit,ifnull((sum(tlb1.tech_process_deposit)+tlb2.tech_process_deposit),0.0) as tech_process_deposit,ifnull((sum(tlb1.wire_transfer_deposit)+tlb2.wire_transfer_deposit),0.0) as wire_transfer_deposit,ifnull((sum(tlb1.cheque_transfer_deposit)+tlb2.cheque_transfer_deposit),0.0) as cheque_transfer_deposit,ifnull((sum(tlb1.cash_payment_deposit)+tlb2.cash_payment_deposit),0.0) as cash_payment_deposit,ifnull((sum(tlb1.total_deposit)+tlb2.total_deposit),0.0)total_deposit,ifnull((sum(tlb1.total_play)+tlb2.total_play),0.0)total_play,ret_debit_card_comm,ret_credit_card_comm,ret_net_banking_comm,ret_cash_card_comm,ret_ola_bind_comm,ret_ola_non_bind_comm,ret_tech_process_comm,ret_wire_transfer_comm,ret_cheque_transfer_comm,ret_cash_payment_comm,ref_ret_org_id from st_ola_rummy_plr_cummulative_deposit_datewise tlb1 , st_ola_rummy_plr_deposit_comm_status tlb2 " +
									   		"where tlb1.date>='"+startDate+"' and tlb1.date<='"+endDate+"' and tlb1.plr_id=tlb2.plr_id  and tlb1.dep_wallet_id="+wallet_id+" and tlb2.dep_wallet_id="+wallet_id+" and tlb2.status='CONTINUE' group by plr_id ) ddd ,st_ola_commission_master cc where cc.wallet_id="+wallet_id+" and cc.tier_id="+retTierId;

			
			
			/*"insert into st_ola_rummy_plr_retailer_commission(plr_id,start_date,end_date,ret_debit_card_comm,ret_credit_card_comm,ret_net_banking_comm,ret_cash_card_comm,ret_ola_bind_comm,ret_ola_non_bind_comm,ret_tech_process_comm,ret_wire_transfer_comm)
			 *  select plr_id,'"+startDate+"','"+endDate+"',((ifnull((debit_card_deposit/total_deposit),0.0)*total_play)*(debit_card_comm_rate*.01))-ret_debit_card_comm as ret_debit_card_comm,((ifnull((credit_card_deposit/total_deposit),0.0)*total_play)*(credit_card_comm_rate*.01)) -ret_credit_card_comm as credit_card_comm,((ifnull((net_banking_deposit/total_deposit),0.0)*total_play)*(net_banking_comm_rate*.01))-ret_net_banking_comm as ret_net_banking_comm,((ifnull((cash_card_deposit/total_deposit),0.0)*total_play)*(cash_card_comm_rate*.01))-ret_cash_card_comm as ret_cash_card_comm,((ifnull((ola_bind_deposit/total_deposit),0.0)*total_play)*(ola_bind_comm_rate*.01))-ret_ola_bind_comm as ret_ola_bind_comm,((ifnull((ola_non_bind_deposit/total_deposit),0.0)*total_play)*(ola_non_bind_comm_rate*.01))-ret_ola_non_bind_comm as ret_ola_non_bind_comm,((ifnull((tech_process_deposit/total_deposit),0.0)*total_play)*(tech_process_comm_rate*.01))-ret_tech_process_comm as ret_tech_process_comm,((ifnull((wire_transfer_deposit/total_deposit),0.0)*total_play)*(wire_transfer_comm_rate*.01))-ret_wire_transfer_comm as ret_wire_transfer_comm " 
			                 + "from(select tlb1.plr_id,tlb1.date,ifnull((sum(tlb1.debit_card_deposit)+tlb2.debit_card_deposit),0.0) as debit_card_deposit,ifnull((sum(tlb1.credit_card_deposit)+tlb2.credit_card_deposit),0.0) as credit_card_deposit,ifnull((sum(tlb1.net_banking_deposit)+tlb2.net_banking_deposit),0.0) as net_banking_deposit,ifnull((sum(tlb1.cash_card_deposit)+tlb2.cash_card_deposit),0.0) as cash_card_deposit,ifnull((sum(tlb1.ola_bind_deposit)+tlb2.ola_bind_deposit),0.0) as ola_bind_deposit,ifnull((sum(tlb1.ola_non_bind_deposit)+tlb2.ola_non_bind_deposit),0.0) as ola_non_bind_deposit,ifnull((sum(tlb1.tech_process_deposit)+tlb2.tech_process_deposit),0.0) as tech_process_deposit,ifnull((sum(tlb1.wire_transfer_deposit)+tlb2.wire_transfer_deposit),0.0) as wire_transfer_deposit,ifnull((sum(tlb1.total_deposit)+tlb2.total_deposit),0.0)total_deposit,ifnull((sum(tlb1.total_play)+tlb2.total_play),0.0)total_play,ret_debit_card_comm,ret_credit_card_comm,ret_net_banking_comm,ret_cash_card_comm,ret_ola_bind_comm,ret_ola_non_bind_comm,ret_tech_process_comm,ret_wire_transfer_comm,ref_ret_org_id from st_ola_rummy_plr_cummulative_deposit_datewise tlb1 , st_ola_rummy_plr_deposit_comm_status tlb2 where tlb1.date>='"+startDate+"' and " 
			                 + "tlb1.date<='"+endDate+"' and tlb1.plr_id=tlb2.plr_id and tlb2.status='CONTINUE' group by plr_id) ddd ,st_ola_commission_master cc where cc.wallet_id="+wallet_id+" and cc.tier_id="+retTierId;
		*/
		
		String insertAgentCommQuery=" insert into st_ola_rummy_plr_agent_commission(plr_id,start_date,end_date,agt_debit_card_comm,agt_credit_card_comm,agt_net_banking_comm,agt_cash_card_comm,agt_ola_bind_comm,agt_ola_non_bind_comm,agt_tech_process_comm,agt_wire_transfer_comm,agt_cheque_transfer_comm,agt_cash_payment_comm)" 
									+" select plr_id,'"+startDate+"','"+endDate+"',((ifnull((debit_card_deposit/total_deposit),0.0)*total_play)*(debit_card_comm_rate*.01))-agt_debit_card_comm as agt_debit_card_comm,((ifnull((credit_card_deposit/total_deposit),0.0)*total_play)*(credit_card_comm_rate*.01)) -agt_credit_card_comm as credit_card_comm,((ifnull((net_banking_deposit/total_deposit),0.0)*total_play)*(net_banking_comm_rate*.01))-agt_net_banking_comm as agt_net_banking_comm,((ifnull((cash_card_deposit/total_deposit),0.0)*total_play)*(cash_card_comm_rate*.01))-agt_cash_card_comm as agt_cash_card_comm,((ifnull((ola_bind_deposit/total_deposit),0.0)*total_play)*(ola_bind_comm_rate*.01))-agt_ola_bind_comm as agt_ola_bind_comm,((ifnull((ola_non_bind_deposit/total_deposit),0.0)*total_play)*(ola_non_bind_comm_rate*.01))-agt_ola_non_bind_comm as agt_ola_non_bind_comm,((ifnull((tech_process_deposit/total_deposit),0.0)*total_play)*(tech_process_comm_rate*.01))-agt_tech_process_comm as agt_tech_process_comm,((ifnull((wire_transfer_deposit/total_deposit),0.0)*total_play)*(wire_transfer_comm_rate*.01))-agt_wire_transfer_comm as agt_wire_transfer_comm,((ifnull((cheque_transfer_deposit/total_deposit),0.0)*total_play)*(cheque_transfer_comm_rate*.01))-agt_cheque_transfer_comm as agt_cheque_transfer_comm,((ifnull((cash_payment_deposit/total_deposit),0.0)*total_play)*(cash_payment_comm_rate*.01))-agt_cash_payment_comm as agt_cash_payment_comm"
									+"	from(	select tlb1.plr_id,tlb1.date,ifnull((sum(tlb1.debit_card_deposit)+tlb2.debit_card_deposit),0.0) as debit_card_deposit,ifnull((sum(tlb1.credit_card_deposit)+tlb2.credit_card_deposit),0.0) as credit_card_deposit,ifnull((sum(tlb1.net_banking_deposit)+tlb2.net_banking_deposit),0.0) as net_banking_deposit,ifnull((sum(tlb1.cash_card_deposit)+tlb2.cash_card_deposit),0.0) as cash_card_deposit,ifnull((sum(tlb1.ola_bind_deposit)+tlb2.ola_bind_deposit),0.0) as ola_bind_deposit,ifnull((sum(tlb1.ola_non_bind_deposit)+tlb2.ola_non_bind_deposit),0.0) as ola_non_bind_deposit,ifnull((sum(tlb1.tech_process_deposit)+tlb2.tech_process_deposit),0.0) as tech_process_deposit,ifnull((sum(tlb1.wire_transfer_deposit)+tlb2.wire_transfer_deposit),0.0) as wire_transfer_deposit,ifnull((sum(tlb1.cheque_transfer_deposit)+tlb2.cheque_transfer_deposit),0.0) as cheque_transfer_deposit,ifnull((sum(tlb1.cash_payment_deposit)+tlb2.cash_payment_deposit),0.0) as cash_payment_deposit,ifnull((sum(tlb1.total_deposit)+tlb2.total_deposit),0.0)total_deposit,ifnull((sum(tlb1.total_play)+tlb2.total_play),0.0)total_play,agt_debit_card_comm,agt_credit_card_comm,agt_net_banking_comm,agt_cash_card_comm,agt_ola_bind_comm,agt_ola_non_bind_comm,agt_tech_process_comm,agt_wire_transfer_comm,agt_cheque_transfer_comm,agt_cash_payment_comm,ref_ret_org_id from st_ola_rummy_plr_cummulative_deposit_datewise tlb1 , st_ola_rummy_plr_deposit_comm_status tlb2" 
									+ " where tlb1.date>='"+startDate+"' and tlb1.date<='"+endDate+"' and tlb1.plr_id=tlb2.plr_id and tlb1.dep_wallet_id="+wallet_id+" and tlb2.dep_wallet_id="+wallet_id+" and tlb2.status='CONTINUE' group by plr_id) ddd ,st_ola_commission_master cc where cc.wallet_id="+wallet_id+" and cc.tier_id="+agtTierId; 
			
			
		/*	"insert into st_ola_rummy_plr_agent_commission(plr_id,start_date,end_date,agt_debit_card_comm,agt_credit_card_comm,agt_net_banking_comm,agt_cash_card_comm,agt_ola_bind_comm,agt_ola_non_bind_comm,agt_tech_process_comm,agt_wire_transfer_comm)  select plr_id,'"+startDate+"','"+endDate+"',((ifnull((debit_card_deposit/total_deposit),0.0)*total_play)*(debit_card_comm_rate*.01))-agt_debit_card_comm as agt_debit_card_comm,((ifnull((credit_card_deposit/total_deposit),0.0)*total_play)*(credit_card_comm_rate*.01)) -agt_credit_card_comm as credit_card_comm,((ifnull((net_banking_deposit/total_deposit),0.0)*total_play)*(net_banking_comm_rate*.01))-agt_net_banking_comm as agt_net_banking_comm,((ifnull((cash_card_deposit/total_deposit),0.0)*total_play)*(cash_card_comm_rate*.01))-agt_cash_card_comm as agt_cash_card_comm,((ifnull((ola_bind_deposit/total_deposit),0.0)*total_play)*(ola_bind_comm_rate*.01))-agt_ola_bind_comm as agt_ola_bind_comm,((ifnull((ola_non_bind_deposit/total_deposit),0.0)*total_play)*(ola_non_bind_comm_rate*.01))-agt_ola_non_bind_comm as agt_ola_non_bind_comm,((ifnull((tech_process_deposit/total_deposit),0.0)*total_play)*(tech_process_comm_rate*.01))-agt_tech_process_comm as agt_tech_process_comm,((ifnull((wire_transfer_deposit/total_deposit),0.0)*total_play)*(wire_transfer_comm_rate*.01))-agt_wire_transfer_comm as agt_wire_transfer_comm " 
        + "from(select tlb1.plr_id,tlb1.date,ifnull((sum(tlb1.debit_card_deposit)+tlb2.debit_card_deposit),0.0) as debit_card_deposit,ifnull((sum(tlb1.credit_card_deposit)+tlb2.credit_card_deposit),0.0) as credit_card_deposit,ifnull((sum(tlb1.net_banking_deposit)+tlb2.net_banking_deposit),0.0) as net_banking_deposit,ifnull((sum(tlb1.cash_card_deposit)+tlb2.cash_card_deposit),0.0) as cash_card_deposit,ifnull((sum(tlb1.ola_bind_deposit)+tlb2.ola_bind_deposit),0.0) as ola_bind_deposit,ifnull((sum(tlb1.ola_non_bind_deposit)+tlb2.ola_non_bind_deposit),0.0) as ola_non_bind_deposit,ifnull((sum(tlb1.tech_process_deposit)+tlb2.tech_process_deposit),0.0) as tech_process_deposit,ifnull((sum(tlb1.wire_transfer_deposit)+tlb2.wire_transfer_deposit),0.0) as wire_transfer_deposit,ifnull((sum(tlb1.total_deposit)+tlb2.total_deposit),0.0)total_deposit,ifnull((sum(tlb1.total_play)+tlb2.total_play),0.0)total_play,agt_debit_card_comm,agt_credit_card_comm,agt_net_banking_comm,agt_cash_card_comm,agt_ola_bind_comm,agt_ola_non_bind_comm,agt_tech_process_comm,agt_wire_transfer_comm,ref_ret_org_id from st_ola_rummy_plr_cummulative_deposit_datewise tlb1 , st_ola_rummy_plr_deposit_comm_status tlb2 where tlb1.date>='"+startDate+"' and " 
        + "tlb1.date<='"+endDate+"' and tlb1.plr_id=tlb2.plr_id and tlb2.status='CONTINUE' group by plr_id) ddd ,st_ola_commission_master cc where cc.wallet_id="+wallet_id+" and cc.tier_id="+agtTierId;
*/
		
		String insertDepositCommHistoryQry=" insert into st_ola_rummy_plr_deposit_comm_history(plr_id,dep_wallet_id,debit_card_deposit,credit_card_deposit,net_banking_deposit,cash_card_deposit,ola_bind_deposit,ola_non_bind_deposit,tech_process_deposit,wire_transfer_deposit,cheque_transfer_deposit,cash_payment_deposit,total_deposit,total_play,ret_debit_card_comm,ret_credit_card_comm,ret_net_banking_comm,ret_cash_card_comm,ret_ola_bind_comm,ret_ola_non_bind_comm,ret_tech_process_comm,ret_wire_transfer_comm,ret_cheque_transfer_comm,ret_cash_payment_comm,agt_debit_card_comm,agt_credit_card_comm,agt_net_banking_comm,agt_cash_card_comm,agt_ola_bind_comm,agt_ola_non_bind_comm,agt_tech_process_comm,agt_wire_transfer_comm,agt_cheque_transfer_comm,agt_cash_payment_comm,till_date,settlement_upto_date,status) " +
											" select ss.plr_id,"+wallet_id+",debit_card_deposit,credit_card_deposit,net_banking_deposit,cash_card_deposit,ola_bind_deposit,ola_non_bind_deposit,tech_process_deposit,wire_transfer_deposit,cheque_transfer_deposit,cash_payment_deposit,total_deposit,total_play,ss.ret_debit_card_comm,ss.ret_credit_card_comm,ss.ret_net_banking_comm,ss.ret_cash_card_comm,ss.ret_ola_bind_comm,ss.ret_ola_non_bind_comm,ss.ret_tech_process_comm,ss.ret_wire_transfer_comm,ss.ret_cheque_transfer_comm,ss.ret_cash_payment_comm,agt_debit_card_comm,agt_credit_card_comm,agt_net_banking_comm,agt_cash_card_comm,agt_ola_bind_comm,agt_ola_non_bind_comm,agt_tech_process_comm,agt_wire_transfer_comm,agt_cheque_transfer_comm,agt_cash_payment_comm,till_date,settlement_upto_date,status from st_ola_rummy_plr_deposit_comm_status ss " +
											" inner join st_ola_rummy_plr_retailer_commission rc on ss.plr_id=rc.plr_id and rc.start_date>='"+startDate+"' and rc.end_date <='" +endDate+ "'";
			
			
			
			
			/*"insert into st_ola_rummy_plr_deposit_comm_history(plr_id,debit_card_deposit,credit_card_deposit,net_banking_deposit,cash_card_deposit,ola_bind_deposit,ola_non_bind_deposit,tech_process_deposit,wire_transfer_deposit,total_deposit,total_play,ret_debit_card_comm,ret_credit_card_comm,ret_net_banking_comm,ret_cash_card_comm,ret_ola_bind_comm,ret_ola_non_bind_comm,ret_tech_process_comm,ret_wire_transfer_comm,agt_debit_card_comm,agt_credit_card_comm,agt_net_banking_comm,agt_cash_card_comm,agt_ola_bind_comm,agt_ola_non_bind_comm,agt_tech_process_comm,agt_wire_transfer_comm,till_date,settlement_upto_date,status) " 
            +"select ss.plr_id,debit_card_deposit,credit_card_deposit,net_banking_deposit,cash_card_deposit,ola_bind_deposit,ola_non_bind_deposit,tech_process_deposit,wire_transfer_deposit,total_deposit,total_play,ss.ret_debit_card_comm,ss.ret_credit_card_comm,ss.ret_net_banking_comm,ss.ret_cash_card_comm,ss.ret_ola_bind_comm,ss.ret_ola_non_bind_comm,ss.ret_tech_process_comm,ss.ret_wire_transfer_comm,agt_debit_card_comm,agt_credit_card_comm,agt_net_banking_comm,agt_cash_card_comm,agt_ola_bind_comm,agt_ola_non_bind_comm,agt_tech_process_comm,agt_wire_transfer_comm,till_date,settlement_upto_date,status " 
            +"from st_ola_rummy_plr_deposit_comm_status ss inner join st_ola_rummy_plr_retailer_commission rc on ss.plr_id=rc.plr_id and rc.start_date>='"+startDate+"' and rc.end_date <='" +endDate+ "'";
		*/
		
		String updateDepositCommPlrStatusQry=" update st_ola_rummy_plr_deposit_comm_status stlb inner join ( select cc.plr_id,debit_card_deposit,credit_card_deposit,net_banking_deposit,cash_card_deposit,ola_bind_deposit,ola_non_bind_deposit,tech_process_deposit,wire_transfer_deposit,total_deposit,total_play,(cc.ret_debit_card_comm+ss.ret_debit_card_comm) ret_debit_card_comm,(cc.ret_credit_card_comm+ss.ret_credit_card_comm) ret_credit_card_comm,(cc.ret_net_banking_comm+ss.ret_net_banking_comm) ret_net_banking_comm,(cc.ret_cash_card_comm+ss.ret_cash_card_comm) ret_cash_card_comm,(cc.ret_ola_bind_comm+ss.ret_ola_bind_comm) ret_ola_bind_comm,(cc.ret_ola_non_bind_comm+ss.ret_ola_non_bind_comm) ret_ola_non_bind_comm,(cc.ret_tech_process_comm+ss.ret_tech_process_comm) ret_tech_process_comm,(cc.ret_wire_transfer_comm+ss.ret_wire_transfer_comm) ret_wire_transfer_comm,(cc.agt_debit_card_comm+ss.agt_debit_card_comm) agt_debit_card_comm,(cc.agt_credit_card_comm+ss.agt_credit_card_comm) agt_credit_card_comm,(cc.agt_net_banking_comm+ss.agt_net_banking_comm) agt_net_banking_comm,(cc.agt_cash_card_comm+ss.agt_cash_card_comm) agt_cash_card_comm,(cc.agt_ola_bind_comm+ss.agt_ola_bind_comm) agt_ola_bind_comm,(cc.agt_ola_non_bind_comm+ss.agt_ola_non_bind_comm) agt_ola_non_bind_comm,(cc.agt_tech_process_comm+ss.agt_tech_process_comm) agt_tech_process_comm,(cc.agt_wire_transfer_comm+ss.agt_wire_transfer_comm) agt_wire_transfer_comm from (select rc.plr_id ,ret_debit_card_comm,ret_credit_card_comm,ret_net_banking_comm,ret_cash_card_comm,ret_ola_bind_comm,ret_ola_non_bind_comm,ret_tech_process_comm,ret_wire_transfer_comm,ret_cheque_transfer_comm,ret_cash_payment_comm,agt_debit_card_comm,agt_credit_card_comm,agt_net_banking_comm,agt_cash_card_comm,agt_ola_bind_comm,agt_ola_non_bind_comm,agt_tech_process_comm,agt_wire_transfer_comm,agt_cheque_transfer_comm,agt_cash_payment_comm from st_ola_rummy_plr_retailer_commission rc " +
												" inner join st_ola_rummy_plr_agent_commission ac on rc.plr_id=ac.plr_id and rc.start_date>='"+startDate+"' and rc.end_date <='"+endDate+"' and ac.start_date>='"+startDate+"' and ac.end_date <= '"+endDate+"' and rc.dep_wallet_id="+wallet_id+" and ac.dep_wallet_id="+wallet_id+" ) cc inner join ( select tlb1.plr_id as plr_id,tlb1.date,ifnull((sum(tlb1.debit_card_deposit)+tlb2.debit_card_deposit),0.0) as debit_card_deposit,ifnull((sum(tlb1.credit_card_deposit)+tlb2.credit_card_deposit),0.0) as credit_card_deposit,ifnull((sum(tlb1.net_banking_deposit)+tlb2.net_banking_deposit),0.0) as net_banking_deposit,ifnull((sum(tlb1.cash_card_deposit)+tlb2.cash_card_deposit),0.0) as cash_card_deposit,ifnull((sum(tlb1.ola_bind_deposit)+tlb2.ola_bind_deposit),0.0) as ola_bind_deposit,ifnull((sum(tlb1.ola_non_bind_deposit)+tlb2.ola_non_bind_deposit),0.0) as ola_non_bind_deposit,ifnull((sum(tlb1.tech_process_deposit)+tlb2.tech_process_deposit),0.0) as tech_process_deposit,ifnull((sum(tlb1.wire_transfer_deposit)+tlb2.wire_transfer_deposit),0.0) as wire_transfer_deposit,ifnull((sum(tlb1.cheque_transfer_deposit)+tlb2.cheque_transfer_deposit),0.0) as cheque_transfer_deposit,ifnull((sum(tlb1.cash_payment_deposit)+tlb2.cash_payment_deposit),0.0) as cash_payment_deposit,ifnull((sum(tlb1.total_deposit)+tlb2.total_deposit),0.0)total_deposit,ifnull((sum(tlb1.total_play)+tlb2.total_play),0.0)total_play,ret_debit_card_comm,ret_credit_card_comm,ret_net_banking_comm,ret_cash_card_comm,ret_ola_bind_comm,ret_ola_non_bind_comm,ret_tech_process_comm,ret_wire_transfer_comm,ret_cheque_transfer_comm,ret_cash_payment_comm,agt_debit_card_comm,agt_credit_card_comm,agt_net_banking_comm,agt_cash_card_comm,agt_ola_bind_comm,agt_ola_non_bind_comm,agt_tech_process_comm,agt_wire_transfer_comm,agt_cheque_transfer_comm,agt_cash_payment_comm,ref_ret_org_id from st_ola_rummy_plr_cummulative_deposit_datewise tlb1 , st_ola_rummy_plr_deposit_comm_status tlb2 " +
												" where  tlb1.date>='"+startDate+"' and tlb1.date<='"+endDate+"' and tlb1.plr_id=tlb2.plr_id and tlb2.status='CONTINUE' and tlb1.dep_wallet_id="+wallet_id+" and tlb2.dep_wallet_id="+wallet_id+"  group by plr_id ) ss on cc.plr_id= ss.plr_id) dtlb on  stlb.plr_id=dtlb.plr_id  set stlb.debit_card_deposit=dtlb.debit_card_deposit, stlb.credit_card_deposit =dtlb.credit_card_deposit,stlb.net_banking_deposit=dtlb.net_banking_deposit, stlb.cash_card_deposit  =dtlb.cash_card_deposit,stlb.ola_bind_deposit=dtlb.ola_bind_deposit,stlb.ola_non_bind_deposit  =dtlb.ola_non_bind_deposit,stlb.tech_process_deposit=dtlb.tech_process_deposit,stlb.wire_transfer_deposit=dtlb.wire_transfer_deposit,stlb.total_deposit =dtlb.total_deposit, stlb.total_play=dtlb.total_play, stlb.ret_debit_card_comm=dtlb.ret_debit_card_comm,  stlb.ret_credit_card_comm=dtlb.ret_credit_card_comm,stlb.ret_net_banking_comm=dtlb.ret_net_banking_comm,stlb.ret_cash_card_comm=dtlb.ret_cash_card_comm, stlb.ret_ola_bind_comm=dtlb.ret_ola_bind_comm,  stlb.ret_ola_non_bind_comm=dtlb.ret_ola_non_bind_comm, stlb.ret_tech_process_comm=dtlb.ret_tech_process_comm, stlb.ret_wire_transfer_comm=dtlb.ret_wire_transfer_comm,  stlb.agt_debit_card_comm=dtlb.agt_debit_card_comm,stlb.agt_credit_card_comm=dtlb.agt_credit_card_comm,  stlb.agt_net_banking_comm =dtlb.agt_net_banking_comm, stlb.agt_cash_card_comm=dtlb.agt_cash_card_comm,  stlb.agt_ola_bind_comm=dtlb.agt_ola_bind_comm,stlb.agt_ola_non_bind_comm=dtlb.agt_ola_non_bind_comm,  stlb.agt_tech_process_comm=dtlb.agt_tech_process_comm,  stlb.agt_wire_transfer_comm=dtlb.agt_wire_transfer_comm,stlb.till_date='"+endDate+"',stlb.status='DONE' where stlb.dep_wallet_id="+wallet_id; 

			
			
			
			/*"update st_ola_rummy_plr_deposit_comm_status stlb inner join ( select cc.plr_id,debit_card_deposit,credit_card_deposit,net_banking_deposit,cash_card_deposit,ola_bind_deposit,ola_non_bind_deposit,tech_process_deposit,wire_transfer_deposit,total_deposit,total_play,(cc.ret_debit_card_comm+ss.ret_debit_card_comm) ret_debit_card_comm,(cc.ret_credit_card_comm+ss.ret_credit_card_comm) ret_credit_card_comm,(cc.ret_net_banking_comm+ss.ret_net_banking_comm) ret_net_banking_comm,(cc.ret_cash_card_comm+ss.ret_cash_card_comm) ret_cash_card_comm,(cc.ret_ola_bind_comm+ss.ret_ola_bind_comm) ret_ola_bind_comm,(cc.ret_ola_non_bind_comm+ss.ret_ola_non_bind_comm) ret_ola_non_bind_comm,(cc.ret_tech_process_comm+ss.ret_tech_process_comm) ret_tech_process_comm,(cc.ret_wire_transfer_comm+ss.ret_wire_transfer_comm) ret_wire_transfer_comm,(cc.agt_debit_card_comm+ss.agt_debit_card_comm) agt_debit_card_comm,(cc.agt_credit_card_comm+ss.agt_credit_card_comm) agt_credit_card_comm,(cc.agt_net_banking_comm+ss.agt_net_banking_comm) agt_net_banking_comm,(cc.agt_cash_card_comm+ss.agt_cash_card_comm) agt_cash_card_comm,(cc.agt_ola_bind_comm+ss.agt_ola_bind_comm) agt_ola_bind_comm,(cc.agt_ola_non_bind_comm+ss.agt_ola_non_bind_comm) agt_ola_non_bind_comm,(cc.agt_tech_process_comm+ss.agt_tech_process_comm) agt_tech_process_comm,(cc.agt_wire_transfer_comm+ss.agt_wire_transfer_comm) agt_wire_transfer_comm from " 
            +"(select rc.plr_id ,ret_debit_card_comm,ret_credit_card_comm,ret_net_banking_comm,ret_cash_card_comm,ret_ola_bind_comm,ret_ola_non_bind_comm,ret_tech_process_comm,ret_wire_transfer_comm,agt_debit_card_comm,agt_credit_card_comm,agt_net_banking_comm,agt_cash_card_comm,agt_ola_bind_comm,agt_ola_non_bind_comm,agt_tech_process_comm,agt_wire_transfer_comm from st_ola_rummy_plr_retailer_commission rc inner join st_ola_rummy_plr_agent_commission ac on rc.plr_id=ac.plr_id and rc.start_date>='"+startDate+"' and rc.end_date <= '"+endDate+"' and ac.start_date>='"+startDate+"' and ac.end_date <= '"+endDate+"') cc inner join (select tlb1.plr_id as plr_id,tlb1.date,ifnull((sum(tlb1.debit_card_deposit)+tlb2.debit_card_deposit),0.0) as debit_card_deposit,ifnull((sum(tlb1.credit_card_deposit)+tlb2.credit_card_deposit),0.0) as credit_card_deposit,ifnull((sum(tlb1.net_banking_deposit)+tlb2.net_banking_deposit),0.0) as net_banking_deposit,ifnull((sum(tlb1.cash_card_deposit)+tlb2.cash_card_deposit),0.0) as cash_card_deposit,ifnull((sum(tlb1.ola_bind_deposit)+tlb2.ola_bind_deposit),0.0) as ola_bind_deposit,ifnull((sum(tlb1.ola_non_bind_deposit)+tlb2.ola_non_bind_deposit),0.0) as ola_non_bind_deposit,ifnull((sum(tlb1.tech_process_deposit)+tlb2.tech_process_deposit),0.0) as tech_process_deposit,ifnull((sum(tlb1.wire_transfer_deposit)+tlb2.wire_transfer_deposit),0.0) as wire_transfer_deposit,ifnull((sum(tlb1.total_deposit)+tlb2.total_deposit),0.0)total_deposit," 
            + "ifnull((sum(tlb1.total_play)+tlb2.total_play),0.0)total_play,ret_debit_card_comm,ret_credit_card_comm,ret_net_banking_comm,ret_cash_card_comm,ret_ola_bind_comm,ret_ola_non_bind_comm,ret_tech_process_comm,ret_wire_transfer_comm,agt_debit_card_comm,agt_credit_card_comm,agt_net_banking_comm,agt_cash_card_comm,agt_ola_bind_comm,agt_ola_non_bind_comm,agt_tech_process_comm,agt_wire_transfer_comm,ref_ret_org_id from st_ola_rummy_plr_cummulative_deposit_datewise tlb1 , st_ola_rummy_plr_deposit_comm_status tlb2 where tlb1.date>='"+startDate+"' and tlb1.date<='"+endDate+"' and tlb1.plr_id=tlb2.plr_id and tlb2.status='CONTINUE' group by plr_id) ss on cc.plr_id= ss.plr_id) dtlb on  stlb.plr_id=dtlb.plr_id set stlb.debit_card_deposit=dtlb.debit_card_deposit, stlb.credit_card_deposit =dtlb.credit_card_deposit,stlb.net_banking_deposit=dtlb.net_banking_deposit, stlb.cash_card_deposit  =dtlb.cash_card_deposit,stlb.ola_bind_deposit=dtlb.ola_bind_deposit,stlb.ola_non_bind_deposit  =dtlb.ola_non_bind_deposit,stlb.tech_process_deposit=dtlb.tech_process_deposit,stlb.wire_transfer_deposit=dtlb.wire_transfer_deposit,stlb.total_deposit =dtlb.total_deposit, stlb.total_play=dtlb.total_play, stlb.ret_debit_card_comm=dtlb.ret_debit_card_comm,  stlb.ret_credit_card_comm=dtlb.ret_credit_card_comm,stlb.ret_net_banking_comm=dtlb.ret_net_banking_comm,  " 
            +"stlb.ret_cash_card_comm=dtlb.ret_cash_card_comm, stlb.ret_ola_bind_comm=dtlb.ret_ola_bind_comm,  stlb.ret_ola_non_bind_comm=dtlb.ret_ola_non_bind_comm, stlb.ret_tech_process_comm=dtlb.ret_tech_process_comm, stlb.ret_wire_transfer_comm=dtlb.ret_wire_transfer_comm,  stlb.agt_debit_card_comm=dtlb.agt_debit_card_comm,stlb.agt_credit_card_comm=dtlb.agt_credit_card_comm,  stlb.agt_net_banking_comm =dtlb.agt_net_banking_comm, stlb.agt_cash_card_comm=dtlb.agt_cash_card_comm,  stlb.agt_ola_bind_comm=dtlb.agt_ola_bind_comm,stlb.agt_ola_non_bind_comm=dtlb.agt_ola_non_bind_comm,  stlb.agt_tech_process_comm=dtlb.agt_tech_process_comm,  stlb.agt_wire_transfer_comm=dtlb.agt_wire_transfer_comm,stlb.till_date='"+endDate+"',stlb.status='DONE'";
*/
		System.out.println("UPDATE PLAYER STATUS:"+updatePlrStatus);
		System.out.println("INSERT COMMISSION QUERY:"+insertRetailerCommQuery);
		System.out.println("INSERT COMMISSION QUERY:"+insertAgentCommQuery);
		System.out.println("insert Deposit commission history:"+insertDepositCommHistoryQry);
		System.out.println("Update plr_deposit status table:"+updateDepositCommPlrStatusQry);

	try{
		Statement stmt=con.createStatement();
	int rowsEffected=stmt.executeUpdate(updatePlrStatus);
	System.out.println("Update plr_deposit status table:"+rowsEffected);
	rowsEffected=	stmt.executeUpdate(insertRetailerCommQuery);
	System.out.println("insertRetailerCommQuery:"+rowsEffected);
	rowsEffected=stmt.executeUpdate(insertAgentCommQuery);
	System.out.println("insertAgentCommQuery:"+rowsEffected);
	rowsEffected=stmt.executeUpdate(insertDepositCommHistoryQry);
	System.out.println("insertDepositCommHistoryQry:"+rowsEffected);
	rowsEffected=stmt.executeUpdate(updateDepositCommPlrStatusQry);
	System.out.println("updateDepositCommPlrStatusQry:"+rowsEffected);
	}catch (SQLException e) {
		e.printStackTrace();
		throw new LMSException();
	}
	
	}
	
	public static void insertRetailerCommWeekely(Connection con,int walletId,Date startDate,Date endDate) throws LMSException{
		
		String insertRetailerCommWeekelyExp="insert into st_ola_retailer_weekly_commission_exp(wallet_id,date,retailer_org_id,net_gaming,agt_comm_amt,ret_comm_amt,refTransactionId,status) select "+walletId+",'"+endDate+"',ref_ret_org_id,sum(total_play),sum(agt_total_comm),sum(ret_total_comm),0,'PENDING' from( select plr_id,ref_ret_org_id,sum(total_play)total_play,0 agt_total_comm ,0 ret_total_comm  from st_ola_rummy_plr_deposit_datewise " 
		                                   +"where date>='"+startDate+"' and date <= '"+endDate+"' group by plr_id union all select plr_id,0 ref_ret_id,0 total_play,0 agt_total_comm,(ret_debit_card_comm+ret_credit_card_comm+ret_net_banking_comm+ret_cash_card_comm+ret_ola_bind_comm+ret_ola_non_bind_comm+ret_tech_process_comm+ret_bonus_comm+ret_inhouse_comm+ret_wire_transfer_comm) as ret_total_comm  from st_ola_rummy_plr_retailer_commission where " 
		                                   +" start_date >= '"+startDate+"' and end_date <='"+endDate+"' union all select plr_id,0 ref_ret_id,0 total_play,(agt_debit_card_comm+agt_credit_card_comm+agt_net_banking_comm+agt_cash_card_comm+agt_ola_bind_comm+agt_ola_non_bind_comm+agt_tech_process_comm+agt_bonus_comm+agt_inhouse_comm+agt_wire_transfer_comm) as agt_total_comm,0 as ret_total_comm  from st_ola_rummy_plr_agent_commission where start_date >= '"+startDate+"' and end_date <='"+endDate+"')mainTlb where (total_play>0 or agt_total_comm>0 or ret_total_comm >0) group by plr_id";
		
		
		try{
	    PreparedStatement pstmtRet=con.prepareStatement(insertRetailerCommWeekelyExp);
	    //PreparedStatement pstmtAgt=con.prepareStatement(insertAgentCommExp);
	    System.out.println("insert retailer Comm Exp:"+pstmtRet);
	    //System.out.println("insert agent Comm Exp:"+pstmtAgt);
	    pstmtRet.executeUpdate();
	   // pstmtAgt.executeUpdate();
	}
	catch (Exception e) {
		e.printStackTrace();
		throw new LMSException();
	}
	}
	
	
	public static void insertCummulativeRetailerCommWeekely(Connection con,int walletId,Date startDate,Date endDate) throws LMSException{
		
		String insertRetailerCommWeekelyExp="insert into st_ola_retailer_weekly_commission_exp(wallet_id,date,retailer_org_id,net_gaming,agt_comm_amt,ret_comm_amt,refTransactionId,status)" +
											" select "+walletId+",'"+endDate+"',ref_ret_org_id,sum(total_play),sum(agt_total_comm),sum(ret_total_comm),0,'PENDING' from(select ref_ret_org_id,sum(total_play)total_play,sum(agt_total_comm)agt_total_comm,sum(ret_total_comm)ret_total_comm from( select plr_id,ref_ret_org_id,sum(total_play)total_play,0 agt_total_comm ,0 ret_total_comm  from st_ola_rummy_plr_cummulative_deposit_datewise  where date>='"+startDate+"' and date <= '"+endDate+"' and dep_wallet_id="+walletId+" group by plr_id union all select plr_id,0 ref_ret_id,0 total_play,0 agt_total_comm,(ret_debit_card_comm+ret_credit_card_comm+ret_net_banking_comm+ret_cash_card_comm+ret_ola_bind_comm+ret_ola_non_bind_comm+ret_tech_process_comm+ret_bonus_comm+ret_inhouse_comm+ret_wire_transfer_comm+ret_cheque_transfer_comm+ret_cash_payment_comm) as ret_total_comm  from st_ola_rummy_plr_retailer_commission where start_date >= '"+startDate+"' and end_date <='"+endDate+"' and dep_wallet_id="+walletId+"  " +
											" union all select plr_id,0 ref_ret_id,0 total_play,(agt_debit_card_comm+agt_credit_card_comm+agt_net_banking_comm+agt_cash_card_comm+agt_ola_bind_comm+agt_ola_non_bind_comm+agt_tech_process_comm+agt_bonus_comm+agt_inhouse_comm+agt_wire_transfer_comm+agt_cheque_transfer_comm+agt_cash_payment_comm) as agt_total_comm,0 as ret_total_comm  from st_ola_rummy_plr_agent_commission where start_date >= '"+startDate+"' and end_date <='"+endDate+"' and dep_wallet_id="+walletId+"  )mainTlb" +
											" where (total_play>0 or agt_total_comm!=0 or ret_total_comm !=0) group by plr_id )bbb group by ref_ret_org_id " ;
		
			
			
			
			/*"insert into st_ola_retailer_weekly_commission_exp(wallet_id,date,retailer_org_id,net_gaming,agt_comm_amt,ret_comm_amt,refTransactionId,status) select "+walletId+",'"+endDate+"',ref_ret_org_id,sum(total_play),sum(agt_total_comm),sum(ret_total_comm),0,'PENDING' from(select ref_ret_org_id,sum(total_play)total_play,sum(agt_total_comm)agt_total_comm,sum(ret_total_comm)ret_total_comm from( select plr_id,ref_ret_org_id,sum(total_play)total_play,0 agt_total_comm ,0 ret_total_comm  from st_ola_rummy_plr_cummulative_deposit_datewise " 
		                                   +"where date>='"+startDate+"' and date <= '"+endDate+"' group by plr_id union all select plr_id,0 ref_ret_id,0 total_play,0 agt_total_comm,(ret_debit_card_comm+ret_credit_card_comm+ret_net_banking_comm+ret_cash_card_comm+ret_ola_bind_comm+ret_ola_non_bind_comm+ret_tech_process_comm+ret_bonus_comm+ret_inhouse_comm+ret_wire_transfer_comm) as ret_total_comm  from st_ola_rummy_plr_retailer_commission where " 
		                                   +" start_date >= '"+startDate+"' and end_date <='"+endDate+"' union all select plr_id,0 ref_ret_id,0 total_play,(agt_debit_card_comm+agt_credit_card_comm+agt_net_banking_comm+agt_cash_card_comm+agt_ola_bind_comm+agt_ola_non_bind_comm+agt_tech_process_comm+agt_bonus_comm+agt_inhouse_comm+agt_wire_transfer_comm) as agt_total_comm,0 as ret_total_comm  from st_ola_rummy_plr_agent_commission where start_date >= '"+startDate+"' and end_date <='"+endDate+"')mainTlb where (total_play>0 or agt_total_comm!=0 or ret_total_comm !=0) group by plr_id )bbb group by ref_ret_org_id";
		*/
		try{
	    PreparedStatement pstmtRet=con.prepareStatement(insertRetailerCommWeekelyExp);
	    //PreparedStatement pstmtAgt=con.prepareStatement(insertAgentCommExp);
	    System.out.println("insert retailer Comm Exp:"+pstmtRet);
	    //System.out.println("insert agent Comm Exp:"+pstmtAgt);
	    int insertedRows = pstmtRet.executeUpdate();
	    System.out.println("insert retailer Comm Exp Records:"+insertedRows);
	   // pstmtAgt.executeUpdate();
	}
	catch (Exception e) {
		e.printStackTrace();
		throw new LMSException();
	}
	}
	
public static void insertRetailerCommMonthly(Connection con,int walletId,Date startDate,Date endDate) throws LMSException{
		
	String insertRetailerCommMonthlyExp=" insert into st_ola_retailer_monthly_commission_exp(wallet_id,date,retailer_org_id,net_gaming,agt_comm_amt,ret_comm_amt,refTransactionId,status) select "+walletId+",'"+endDate+"',ref_ret_org_id,sum(total_play),sum(agt_total_comm),sum(ret_total_comm),0,'PENDING' from( " +
										"  select plr_id,ref_ret_org_id,sum(total_play)total_play,0 agt_total_comm ,0 ret_total_comm  from st_ola_rummy_plr_deposit_datewise where date>='"+startDate+"' and date <= '"+endDate+"' and dep_wallet_id="+walletId+"  group by plr_id" +
										" union all select plr_id,0 ref_ret_id,0 total_play,0 agt_total_comm,(ret_debit_card_comm+ret_credit_card_comm+ret_net_banking_comm+ret_cash_card_comm+ret_ola_bind_comm+ret_ola_non_bind_comm+ret_tech_process_comm+ret_cheque_transfer_comm+ret_cash_payment_comm) as ret_total_comm  from st_ola_rummy_plr_retailer_commission where  start_date >= '"+startDate+"' and end_date <='"+endDate+"' and dep_wallet_id="+walletId+"" +
												" union all select plr_id,0 ref_ret_id,0 total_play,(agt_debit_card_comm+agt_credit_card_comm+agt_net_banking_comm+agt_cash_card_comm+agt_ola_bind_comm+agt_ola_non_bind_comm+agt_tech_process_comm+agt_cheque_transfer_comm+agt_cash_payment_comm) as agt_total_comm,0 as ret_total_comm  from st_ola_rummy_plr_agent_commission where start_date >= '"+startDate+"' and end_date <='"+endDate+"' and dep_wallet_id="+walletId+" )mainTlb where (total_play>0 or agt_total_comm>0 or ret_total_comm >0) group by plr_id";
		
		
		
		/*"insert into st_ola_retailer_monthly_commission_exp(wallet_id,date,retailer_org_id,net_gaming,agt_comm_amt,ret_comm_amt,refTransactionId,status) select "+walletId+",'"+endDate+"',ref_ret_org_id,sum(total_play),sum(agt_total_comm),sum(ret_total_comm),0,'PENDING' from( select plr_id,ref_ret_org_id,sum(total_play)total_play,0 agt_total_comm ,0 ret_total_comm  from st_ola_rummy_plr_deposit_datewise " 
    +"where date>='"+startDate+"' and date <= '"+endDate+"' group by plr_id union all select plr_id,0 ref_ret_id,0 total_play,0 agt_total_comm,(ret_debit_card_comm+ret_credit_card_comm+ret_net_banking_comm+ret_cash_card_comm+ret_ola_bind_comm+ret_ola_non_bind_comm+ret_tech_process_comm) as ret_total_comm  from st_ola_rummy_plr_retailer_commission where " 
    +" start_date >= '"+startDate+"' and end_date <='"+endDate+"' union all select plr_id,0 ref_ret_id,0 total_play,(agt_debit_card_comm+agt_credit_card_comm+agt_net_banking_comm+agt_cash_card_comm+agt_ola_bind_comm+agt_ola_non_bind_comm+agt_tech_process_comm) as agt_total_comm,0 as ret_total_comm  from st_ola_rummy_plr_agent_commission where start_date >= '"+startDate+"' and end_date <='"+endDate+"')mainTlb where (total_play>0 or agt_total_comm>0 or ret_total_comm >0) group by plr_id";
*/
	   	try{
	    PreparedStatement pstmtRet=con.prepareStatement(insertRetailerCommMonthlyExp);
	   // PreparedStatement pstmtAgt=con.prepareStatement(insertAgentCommExp);
	    System.out.println("insert retailer Comm Exp:"+pstmtRet);
	    //System.out.println("insert agent Comm Exp:"+pstmtAgt);
	    pstmtRet.executeUpdate(); 
	   // pstmtAgt.executeUpdate();
	}
	catch (Exception e) {
		e.printStackTrace();
		throw new LMSException();
	}
	}





// for module2 of Net Gaming

public static void insertPlrConsolidateDeposit(Connection con,Date startDate,Date endDate,int wallet_id,int agtTierId,int retTierId) throws LMSException{
	
	String updatePlrStatus="update st_ola_rummy_plr_deposit_status ss inner join (select * from(select plr_id,date,if(status='MATCHED','CONTINUE','HOLD')status from st_ola_rummy_plr_deposit_datewise where date>='"+startDate+"' and date <= '"+endDate+"'  order by status desc) dateWise  group by plr_id ) dd on ss.plr_id=dd.plr_id  set ss.status=dd.status";
	
	String insertRetailerCommQuery="insert into st_ola_rummy_plr_retailer_commission(plr_id,start_date,end_date,ret_debit_card_comm,ret_credit_card_comm,ret_net_banking_comm,ret_cash_card_comm,ret_ola_bind_comm,ret_ola_non_bind_comm,ret_tech_process_comm,ret_bonus_comm,ret_inhouse_comm,ret_wire_transfer_comm) select plr_id,'"+startDate+"','"+endDate+"'," 
                                  +	"((debit_card_deposit/total_deposit)*total_play*debit_card_comm_rate*.01) debit_card_comm,((credit_card_deposit/total_deposit)*total_play*credit_card_comm_rate*.01) credit_card_comm,((net_banking_deposit/total_deposit)*total_play*net_banking_comm_rate*.01) net_banking_comm," 
                                  +	"((cash_card_deposit/total_deposit)*total_play*debit_card_comm_rate*.01) cash_card_comm,((ola_bind_deposit/total_deposit)*total_play*ola_bind_comm_rate*.01) ola_bind_comm,((ola_non_bind_deposit/total_deposit)*total_play*ola_non_bind_comm_rate*.01) ola_non_bind_comm," 
                                  +	"((tech_process_deposit/total_deposit)*total_play*tech_process_comm_rate*.01) tech_process_comm,((bonus_deposit/total_deposit)*total_play*bonus_comm_rate*.01) bonus_comm,((inhouse_deposit/total_deposit)*total_play*inhouse_comm_rate*.01) inhouse_comm,((wire_transfer_deposit/total_deposit)*total_play*wire_transfer_comm_rate*.01) wire_transfer_comm from(select dd.plr_id,(sum(dd.debit_card_deposit) + ss.debit_card_deposit) as debit_card_deposit,(sum(dd.credit_card_deposit) + ss.credit_card_deposit) as credit_card_deposit," 
                                  +	"(sum(dd.net_banking_deposit) + ss.net_banking_deposit) as net_banking_deposit,(sum(dd.cash_card_deposit) + ss.cash_card_deposit) as cash_card_deposit,(sum(dd.ola_bind_deposit) + ss.ola_bind_deposit) as ola_bind_deposit ,(sum(dd.ola_non_bind_deposit) + ss.ola_non_bind_deposit) as ola_non_bind_deposit,(sum(dd.tech_process_deposit) + ss.tech_process_deposit) as tech_process_deposit,(sum(dd.bonus_deposit) + ss.bonus_deposit) as bonus_deposit,(sum(dd.inhouse_deposit) + ss.inhouse_deposit) as inhouse_deposit,(sum(dd.wire_transfer_deposit) + ss.wire_transfer_deposit) as wire_transfer_deposit,(sum(dd.total_deposit) + ss.total_deposit) as total_deposit,sum(total_play) total_play,sum(dd.total_deposit)+ ss.total_deposit+sum(total_winning)-sum(total_play) balance from st_ola_rummy_plr_deposit_datewise dd ,st_ola_rummy_plr_deposit_status ss where dd.plr_id=ss.plr_id and date>='"+startDate+"' and date <= '"+endDate+"' and dd.status='MATCHED' group by plr_id)depositTlb inner join st_ola_commission_master cc where wallet_id="+wallet_id+" and tier_id="+retTierId;

	String insertAgentCommQuery="insert into st_ola_rummy_plr_agent_commission(plr_id,start_date,end_date,agt_debit_card_comm,agt_credit_card_comm,agt_net_banking_comm,agt_cash_card_comm,agt_ola_bind_comm,agt_ola_non_bind_comm,agt_tech_process_comm,agt_bonus_comm,agt_inhouse_comm,agt_wire_transfer_comm) select plr_id,'"+startDate+"','"+endDate+"'," 
                                 +	"((debit_card_deposit/total_deposit)*total_play*debit_card_comm_rate*.01) debit_card_comm,((credit_card_deposit/total_deposit)*total_play*credit_card_comm_rate*.01) credit_card_comm,((net_banking_deposit/total_deposit)*total_play*net_banking_comm_rate*.01) net_banking_comm," 
                                 +	"((cash_card_deposit/total_deposit)*total_play*debit_card_comm_rate*.01) cash_card_comm,((ola_bind_deposit/total_deposit)*total_play*ola_bind_comm_rate*.01) ola_bind_comm,((ola_non_bind_deposit/total_deposit)*total_play*ola_non_bind_comm_rate*.01) ola_non_bind_comm," 
                                 +	"((tech_process_deposit/total_deposit)*total_play*tech_process_comm_rate*.01) tech_process_comm,((bonus_deposit/total_deposit)*total_play*bonus_comm_rate*.01) bonus_comm,((inhouse_deposit/total_deposit)*total_play*inhouse_comm_rate*.01) inhouse_comm,((wire_transfer_deposit/total_deposit)*total_play*wire_transfer_comm_rate*.01) wire_transfer_comm from(select dd.plr_id,(sum(dd.debit_card_deposit) + ss.debit_card_deposit) as debit_card_deposit,(sum(dd.credit_card_deposit) + ss.credit_card_deposit) as credit_card_deposit," 
                                  +	"(sum(dd.net_banking_deposit) + ss.net_banking_deposit) as net_banking_deposit,(sum(dd.cash_card_deposit) + ss.cash_card_deposit) as cash_card_deposit,(sum(dd.ola_bind_deposit) + ss.ola_bind_deposit) as ola_bind_deposit ,(sum(dd.ola_non_bind_deposit) + ss.ola_non_bind_deposit) as ola_non_bind_deposit,(sum(dd.tech_process_deposit) + ss.tech_process_deposit) as tech_process_deposit,(sum(dd.bonus_deposit) + ss.bonus_deposit) as bonus_deposit,(sum(dd.inhouse_deposit) + ss.inhouse_deposit) as inhouse_deposit,(sum(dd.wire_transfer_deposit) + ss.wire_transfer_deposit) as wire_transfer_deposit,(sum(dd.total_deposit) + ss.total_deposit) as total_deposit,sum(total_play) total_play,sum(dd.total_deposit)+ ss.total_deposit+sum(total_winning)-sum(total_play) balance from st_ola_rummy_plr_deposit_datewise dd ,st_ola_rummy_plr_deposit_status ss where dd.plr_id=ss.plr_id and date>='"+startDate+"' and date <= '"+endDate+"' and dd.status='MATCHED' group by plr_id)depositTlb inner join st_ola_commission_master cc where wallet_id="+wallet_id+" and tier_id="+agtTierId;

	
	String insertDepositHistoryQry="insert into st_ola_rummy_plr_deposit_history(plr_id,debit_card_deposit,credit_card_deposit,net_banking_deposit,cash_card_deposit,ola_bind_deposit,ola_non_bind_deposit,tech_process_deposit,bonus_deposit,inhouse_deposit,wire_transfer_deposit,total_deposit,till_date,status) " 
                                  +	" select ss.plr_id,debit_card_deposit,credit_card_deposit,net_banking_deposit,cash_card_deposit,ola_bind_deposit,ola_non_bind_deposit,tech_process_deposit,bonus_deposit,inhouse_deposit,wire_transfer_deposit,total_deposit,till_date,status from st_ola_rummy_plr_deposit_status ss inner join st_ola_rummy_plr_retailer_commission rc on ss.plr_id=rc.plr_id and rc.start_date>='"+startDate+"' and rc.end_date <='"+endDate+"'";
	
	String updateDepositCommPlrStatusQry="update st_ola_rummy_plr_deposit_status ss inner join (select plr_id,ifnull(((debit_card_deposit/total_deposit)*balance),0.0) debit_card_deposit ,ifnull(((credit_card_deposit/total_deposit)*balance),0.0) credit_card_deposit ,ifnull(((net_banking_deposit/total_deposit)*balance),0.0) net_banking_deposit ,ifnull(((cash_card_deposit/total_deposit)*balance),0.0) cash_card_deposit ,ifnull(((ola_bind_deposit/total_deposit)*balance),0.0) ola_bind_deposit ,ifnull(((ola_non_bind_deposit/total_deposit)*balance),0.0) ola_non_bind_deposit ," 
                                          +"ifnull(((tech_process_deposit/total_deposit)*balance),0.0) tech_process_deposit,ifnull(((bonus_deposit/total_deposit)*balance),0.0) bonus_deposit ,ifnull(((inhouse_deposit/total_deposit)*balance),0.0) inhouse_deposit,ifnull(((wire_transfer_deposit/total_deposit)*balance),0.0) wire_transfer_deposit,balance as total_deposit from (select dd.plr_id,(sum(dd.debit_card_deposit) + ss.debit_card_deposit) as debit_card_deposit,(sum(dd.credit_card_deposit) + ss.credit_card_deposit) as credit_card_deposit,(sum(dd.net_banking_deposit) + ss.net_banking_deposit) as net_banking_deposit,(sum(dd.cash_card_deposit) + ss.cash_card_deposit) as cash_card_deposit,(sum(dd.ola_bind_deposit) + ss.ola_bind_deposit) as ola_bind_deposit ," 
                                          +"(sum(dd.ola_non_bind_deposit) + ss.ola_non_bind_deposit) as ola_non_bind_deposit,(sum(dd.tech_process_deposit) + ss.tech_process_deposit) as tech_process_deposit,(sum(dd.bonus_deposit) + ss.bonus_deposit) as bonus_deposit,(sum(dd.inhouse_deposit) + ss.inhouse_deposit) as inhouse_deposit,(sum(dd.wire_transfer_deposit) + ss.wire_transfer_deposit) as wire_transfer_deposit,(sum(dd.total_deposit) + ss.total_deposit) as total_deposit,sum(total_play) total_play,sum(dd.total_deposit)+ ss.total_deposit+sum(total_winning)-sum(total_play) balance from st_ola_rummy_plr_deposit_datewise dd ,st_ola_rummy_plr_deposit_status ss where dd.plr_id=ss.plr_id and date>='"+startDate+"' and date <= '"+endDate+"' and dd.status='MATCHED' group by plr_id)depositTlb) dd " 
                                          +	"on ss.plr_id=dd.plr_id set ss.debit_card_deposit=dd.debit_card_deposit,ss.debit_card_deposit=dd.debit_card_deposit,ss.credit_card_deposit=dd.credit_card_deposit,ss.net_banking_deposit=dd.net_banking_deposit,ss.cash_card_deposit=dd.cash_card_deposit,ss.ola_bind_deposit=dd.ola_bind_deposit,ss.ola_non_bind_deposit=dd.ola_non_bind_deposit,ss.tech_process_deposit=dd.tech_process_deposit,ss.bonus_deposit=dd.bonus_deposit,ss.inhouse_deposit=dd.inhouse_deposit,ss.wire_transfer_deposit=dd.wire_transfer_deposit,ss.total_deposit=dd.total_deposit,ss.till_date='"+endDate+"',ss.status='DONE'";
	System.out.println("UPDATE PLAYER STATUS:"+updatePlrStatus);
	System.out.println("INSERT COMMISSION QUERY:"+insertRetailerCommQuery);
	System.out.println("INSERT COMMISSION QUERY:"+insertAgentCommQuery);
	System.out.println("insert Deposit commission history:"+insertDepositHistoryQry);
	System.out.println("Update plr_deposit status table:"+updateDepositCommPlrStatusQry);

try{
	Statement stmt=con.createStatement();
	stmt.executeUpdate(updatePlrStatus);
	stmt.executeUpdate(insertRetailerCommQuery);
	stmt.executeUpdate(insertAgentCommQuery);
	stmt.executeUpdate(insertDepositHistoryQry);
	stmt.executeUpdate(updateDepositCommPlrStatusQry);
}catch (SQLException e) {
	e.printStackTrace();
	throw new LMSException();
}

}


public Date getLastDateForPlayerCommission() throws LMSException{

	
	
	Date lastDate=null;
	Connection con=null;
	Statement stmt=null;
	ResultSet rs=null;
	try{
		con=DBConnect.getConnection();
	String selectLastDateQry="select max(date) date from st_ola_retailer_weekly_commission_exp";
	 stmt=con.createStatement();
	
	 rs=stmt.executeQuery(selectLastDateQry);
	  
	  while(rs.next()){
		  lastDate=rs.getDate("date");
		  if(lastDate==null){
			  Calendar cal=Calendar.getInstance();
			  cal.set(2012, 5, 17);
			  lastDate=new Date(cal.getTime().getTime());
			  
		  }
	  }
	  
	 
	}
	catch (SQLException e) {
		e.printStackTrace();
	throw new LMSException("Error in Net Gaming ");
	}
	finally{
		try{
			if(con != null){
			con.close();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new LMSException();
		}
	}
	return lastDate;
	

}

public Date getSettlementDateForPlayerCommission(int week,Connection con) throws LMSException{

	
	
	Date lastDate=null;
	//Connection con=null;
	Statement stmt=null;
	ResultSet rs=null;
	try{
		//con=DBConnect.getConnection();
	String selectLastDateQry="select DATE_ADD(date,INTERVAL 1 DAY) settlement_date from (select DATE_ADD(max(settlement_upto_date),INTERVAL "+week+" WEEK) date from st_ola_rummy_plr_deposit_comm_status)aa";
	 stmt=con.createStatement();
	 logger.info("SettlementDateQuery"+selectLastDateQry);
	 rs=stmt.executeQuery(selectLastDateQry);
	  if(rs.next()){
		  lastDate=rs.getDate("settlement_date");
		  
	  }
	 
	}catch (SQLException e) {
		logger.error("SQL Exception ",e);
		throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
	}finally{
		DBConnect.closeRs(rs);
		DBConnect.closeStmt(stmt);
	}
	return lastDate;
	

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

public static void insertNetGamingData() throws LMSException{
	Connection con =null;
	try{
		con =DBConnect.getConnection();
		con.setAutoCommit(false);
		for(Entry<Integer, String> entry :OLAUtility.getOlaWalletDataMap().entrySet()){
				//insertDepositTransactions( con,entry.getKey());
				//insertWageringTransactions( con,entry.getKey());
			
		}
		insertPlrDepositDateWise(con);
	}catch (SQLException e) {
		logger.info("SQL Exception ",e);
		throw new LMSException("SQL Exception "+e.getMessage());
	}catch (Exception e) {
		logger.info("Exception ",e);
		throw new LMSException("Exception"+e.getMessage());
	} finally {
		DBConnect.closeCon(con);
	}


}
private static void insertWageringTransactions(Connection con,int walletId) throws LMSException {

	Statement stmt =null;
	ResultSet rs =null;
	PreparedStatement pstmt =null;
	try{
		
		String query =" select DATE_ADD(date(date_time) ,INTERVAL 1 DAY) date  from st_ola_rummy_plr_wagered_transaction where dep_wallet_id ="+walletId+" order by date_time  desc limit 1" ;
		stmt =con.createStatement();
		rs= stmt.executeQuery(query);
		String startDate ="2000-01-01 00:00:00";
		if(rs.next()){
			startDate=rs.getString("date")+" 00:00:00";
			
		}
		logger.info("Start DATE Wagering:"+startDate);
		Calendar calStart=Calendar.getInstance();
		calStart.add(Calendar.DAY_OF_MONTH, -1);
		String endDate=new Date(calStart.getTime().getTime())+" 23:59:59";
		Map<String,String> bindReqMap = new HashMap<String, String>();
		bindReqMap.put("requestType","NG_WAGER_REPORT");
		//bindReqMap.put("domainName",OLAUtility.getWalletIntBean(walletId+"").getTpIp());
		 bindReqMap.put("fromDate",startDate);		 //bindReqMap.put("fromDate","2013-09-01 00:00:00");
		 bindReqMap.put("toDate",endDate);//	bindReqMap.put("toDate","2013-09-03 23:59:59");	
		// int walletId =2;
		//String reqType ="/Weaver/service/rest/weaverTxnReport";
		InputStream bindResponse =OLAClient.callKhelPlayRummy(OLAUtility.prepareXMLFromData(bindReqMap),walletId,OLAConstants.repReq);
		OlaRummyNGDepositRepBean olaNgDepRepBean=OLAUtility.prepareBeanDataFromXml(bindResponse);
		logger.info(olaNgDepRepBean);
		if(olaNgDepRepBean==null){
			logger.info("player binding msg:"+olaNgDepRepBean);
			
		}else {
		
			String query1= "insert into st_ola_rummy_plr_wagered_transaction(dep_wallet_id,plr_id,plr_alias,ref_transaction_id,wagered_amount,transaction_type,game_name,date_time) values(?,?,?,?,?,?,?,?) ";
			pstmt = con.prepareStatement(query1);
			if(olaNgDepRepBean.getRummyngTxnList()==null){
				logger.info(olaNgDepRepBean.getErrorCode()+olaNgDepRepBean.getErrorMsg());
			}else{
				List<OlaRummyNGTxnRepBean> rummyngTxnList  =olaNgDepRepBean.getRummyngTxnList();
				for(int i=0;i<rummyngTxnList.size();i++){
					String wallet = rummyngTxnList.get(i).getDomainName();
					List<OlaRummyNGPlrTxnRepBean> rummyngplrTxnList  =rummyngTxnList.get(i).getRummyngplrTxnList();
					
					for(int j=0;j<rummyngplrTxnList.size();j++){
						pstmt.setInt(1, OLAUtility.getWalletIntBean(wallet).getWalletId());
						OlaRummyNGPlrTxnRepBean bean = rummyngplrTxnList.get(j);
						pstmt.setString(2,bean.getAccountId());
						pstmt.setString(3,bean.getUserName());
						pstmt.setString(4,bean.getTxnId());
						pstmt.setDouble(5,bean.getAmount());
						pstmt.setString(6,"Wagering");
						pstmt.setString(7,bean.getGameType());
						pstmt.setString(8,bean.getTxnTime());
						pstmt.addBatch();
					}
					
					
				}
			int[] recordsInserted=pstmt.executeBatch();
			con.commit();
			logger.info("rowsUpdated"+recordsInserted.length);
			}
			
		}
	}catch(SQLException e){
		logger.error("SQL Exception",e);
		try {
			con.rollback();
		} catch (SQLException e1) {
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		}
		throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
	}catch(Exception e){
		logger.error("Exception",e);
		throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
	}finally{
		DBConnect.closePstmt(pstmt);
		DBConnect.closeStmt(stmt);
		DBConnect.closeRs(rs);
	}	
	
	


	
}

public static void insertDepositTransactions(Connection con,int walletId) throws LMSException{
	Statement stmt =null;
	ResultSet rs =null;
	PreparedStatement pstmt=null;
	try{
		
		String query =" select DATE_ADD(date(date_time) ,INTERVAL 1 DAY) date from st_ola_rummy_plr_deposit_transaction where dep_wallet_id="+walletId+" order by date_time  desc limit 1" ;
		stmt =con.createStatement();
		rs= stmt.executeQuery(query);
		String startDate ="2000-01-01 00:00:00";
		if(rs.next()){
			startDate=rs.getString("date")+" 00:00:00";
			
		}
		logger.info("Start DATE Deposit:"+startDate);
		Calendar calStart=Calendar.getInstance();
		calStart.add(Calendar.DAY_OF_MONTH, -1);
		String endDate=new Date(calStart.getTime().getTime())+" 23:59:59";
		Map<String,String> bindReqMap = new HashMap<String, String>();
		bindReqMap.put("requestType","NG_DEPOSIT_REPORT");
		bindReqMap.put("domainName",OLAUtility.getWalletIntBean(walletId+"").getTpIp());
		bindReqMap.put("fromDate",startDate);			//  	bindReqMap.put("fromDate","2013-09-01 00:00:00");
		bindReqMap.put("toDate",endDate); // 	 bindReqMap.put("toDate","2013-09-03 23:59:59");
		// int walletId =2;
		//String reqType ="/Weaver/service/rest/weaverTxnReport";
		InputStream bindResponse =OLAClient.callKhelPlayRummy(OLAUtility.prepareXMLFromData(bindReqMap),walletId,OLAConstants.repReq);
		OlaRummyNGDepositRepBean olaNgDepRepBean=OLAUtility.prepareBeanDataFromXml(bindResponse);
		logger.info(olaNgDepRepBean);
		if(olaNgDepRepBean==null){
			logger.info("player binding msg:"+olaNgDepRepBean);
			
		}else {
		
			String query1= "insert into st_ola_rummy_plr_deposit_transaction(dep_wallet_id,plr_id,transaction_id,deposit_amount,deposit_mode,payment_provider,date_time,plr_alias) values(?,?,?,?,?,?,?,?) ";
			pstmt = con.prepareStatement(query1);
			if(olaNgDepRepBean.getRummyngTxnList()==null){
				logger.info(olaNgDepRepBean.getErrorCode()+olaNgDepRepBean.getErrorMsg());
			}else{
				List<OlaRummyNGTxnRepBean> rummyngTxnList  =olaNgDepRepBean.getRummyngTxnList();
				for(int i=0;i<rummyngTxnList.size();i++){
					String wallet = rummyngTxnList.get(i).getDomainName();
					List<OlaRummyNGPlrTxnRepBean> rummyngplrTxnList  =rummyngTxnList.get(i).getRummyngplrTxnList();
					
					for(int j=0;j<rummyngplrTxnList.size();j++){
						pstmt.setInt(1, OLAUtility.getWalletIntBean(wallet).getWalletId());
						OlaRummyNGPlrTxnRepBean bean = rummyngplrTxnList.get(j);
						pstmt.setString(2, rummyngplrTxnList.get(j).getAccountId());
						pstmt.setString(3, rummyngplrTxnList.get(j).getTxnId());
						pstmt.setDouble(4, rummyngplrTxnList.get(j).getAmount());
						System.out.println(bean.getPaymentType()+"asdasd"+bean.getProviderName());
						String paymentType ="other";
						if(bean.getPaymentType().equalsIgnoreCase("Cash_Payment")&&bean.getProviderName().equalsIgnoreCase("OLA") ){
							paymentType="OLA";
						}else if(bean.getProviderName().equalsIgnoreCase("OLA")&& bean.getPaymentType().equalsIgnoreCase("CASH_CARD")){
							paymentType="Cash Card";
						}else if(bean.getProviderName().equalsIgnoreCase("HDFC Bank")&& bean.getPaymentType().equalsIgnoreCase("CHEQUE_TRANS")){
							paymentType="CHEQUE_TRANS";
						}else if(bean.getProviderName().equalsIgnoreCase("HDFC Bank")&& bean.getPaymentType().equalsIgnoreCase("WIRE_TRANS")){
							paymentType="Wire Transfer";
						}else if(bean.getPaymentType().equalsIgnoreCase("DEBIT_CARD")){
							paymentType="Debit Card";
						}else if(bean.getPaymentType().equalsIgnoreCase("CREDIT_CARD")){
							paymentType="Credit Card";
						}else if(bean.getPaymentType().equalsIgnoreCase("NET_BANKING")){
							paymentType="Net Banking";
						}else if(bean.getPaymentType().equalsIgnoreCase("CASH_PAYMENT")&&bean.getProviderName().equalsIgnoreCase("GharPay")){
							paymentType="CASH_PAYMENT";
						}else {
							paymentType=bean.getPaymentType();
							
						}
						
						
						pstmt.setString(5,paymentType);
						pstmt.setString(6,bean.getProviderName());
						pstmt.setString(7,rummyngplrTxnList.get(j).getTxnTime());
						pstmt.setString(8,rummyngplrTxnList.get(j).getUserName());
						logger.info("deposit query"+pstmt);
						pstmt.addBatch();
						//pstmt.execute();
					}
					
					
				}
				logger.info("deposit query"+pstmt);
				int[] recordsInserted=pstmt.executeBatch();
				con.commit();
				logger.info("rowsUpdated"+recordsInserted.length);
			}
			
		}
	}catch(SQLException e){
		logger.error("SQL Exception",e);
		try {
			con.rollback();
		} catch (SQLException e1) {
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		}
		throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
	}catch(Exception e){
		logger.error("Exception",e);
		throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
	}finally{
		DBConnect.closePstmt(pstmt);
		DBConnect.closeStmt(stmt);
		DBConnect.closeRs(rs);
	}	
	
	

}
}
