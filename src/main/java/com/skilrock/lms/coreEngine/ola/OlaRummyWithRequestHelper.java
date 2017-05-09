package com.skilrock.lms.coreEngine.ola;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rng.RNGUtilities;

import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
/**
 * Fill ola bind players' data in RMS DB and sms reference code for withdrawal 
 * @author Neeraj Jain
 *
 */
public class OlaRummyWithRequestHelper {
static Log logger = LogFactory.getLog(OlaRummyWithRequestHelper.class);
/**
 * Process all withdrawal request data and sms reference code for withdrawal 
 * @throws LMSException
 */
public static synchronized void processWithRequest() throws LMSException{
	logger.info("Processing With Request");
	Connection con = null;	
	PreparedStatement ps = null;
	PreparedStatement ps1 =null;
	PreparedStatement ps2 =null;
	PreparedStatement ps3 =null;
	String query =null;
	ResultSet rs = null;
	Calendar cal = Calendar.getInstance();
	Date currentDt = null;
	currentDt = new Date(cal.getTime().getTime());
	try {
		con=DBConnect.getConnection();
		con.setAutoCommit(false);	
		query ="select wallet_status,wallet_name from st_ola_wallet_master where (wallet_name ='Rummy' or  wallet_name='KhelPlayRummy')";
		ps3=con.prepareStatement(query);
		rs=ps3.executeQuery();
		String wallet_status ="INACTIVE";
			while (rs.next()) {

				wallet_status = rs.getString("wallet_status");

				if (wallet_status.equals("ACTIVE")) {
					// Select Bind and Pending withdrawal request
					query = "select task_id,user_id,plr_phone,amount,request_date,approval_date  from st_ola_rummy_withdrawal_rep where isBind='YES' and rms_process_status='PENDING' and wallet_name=?";
					ps = con.prepareStatement(query);
					ps.setString(1, rs.getString("wallet_name"));
					rs = ps.executeQuery();
					query = "insert into st_ola_withdrawal_request(task_id,plr_id,phone_nbr,amount,request_date,approve_date,ref_code,status) values(?,?,?,?,?,?,?,?)";
					int count = 0;
					ps1 = con.prepareStatement(query);
					ArrayList<String> msgList = new ArrayList<String>();
					ArrayList<String> phNbrList = new ArrayList<String>();
					// insert ola bind data
					while (rs.next()) {

						String phoneNbr = rs.getString("plr_phone");
						String userId = rs.getString("user_id");
						String msg = null;
						double amount = rs.getDouble("amount");
						count++;
						ps1.setInt(1, rs.getInt("task_id"));
						ps1.setString(2, userId);
						ps1.setString(3, phoneNbr);
						ps1.setDouble(4, amount);
						ps1.setDate(5, rs.getDate("request_date"));
						ps1.setDate(6, rs.getDate("approval_date"));
						Long rnumber = RNGUtilities.generateRandomNumber(
								100000l, 999999l);// Generate six digit random
													// number
						ps1.setString(7, rnumber.toString());
						ps1.setString(8, "PENDING");
						ps1.addBatch();
						if (count % 100 == 0) {
							ps1.executeBatch();
						}
						msg = "Dear User "
								+ userId
								+ " Your withdrawal request at khelplayrummy.com of amount "
								+ amount
								+ " has been approved, your withdrawal Authentication code is "
								+ rnumber
								+ " Please contact your affiliate to withdraw money";
						msgList.add(msg);
						phNbrList.add(phoneNbr);

					}

					ps1.executeBatch();
					// update rms_process_status in st_ola_rummy_withdrawal_rep
					ps2 = con.prepareStatement("update st_ola_rummy_withdrawal_rep set  rms_process_status='DONE',rms_process_date=? where isBind='YES' and rms_process_status='PENDING'");
					ps2.setDate(1, currentDt);
					int update = ps2.executeUpdate();
					logger.info("Selected Requests: "+count+" Updated Requests :" + update);

					if (count == update) {
						con.commit();
					} else {
						throw new LMSException("Cannot Update Incorrect Data");
					}
					// Send SMS to players
					SendSMS smsSend = new SendSMS(msgList, phNbrList);
					smsSend.setDaemon(true);
					smsSend.start();
					logger.info("SMS Sent");
				}
			}
}catch(Exception e){
		e.printStackTrace();
		throw new LMSException("Some Error");
	}finally{
		try {
			if(con!=null){
				con.close();
				
			}
			if(rs!=null){
				rs.close();
				
			}
			if(ps!=null){
				ps.close();
				
			}
			if(ps1!=null){
				ps1.close();
				
			}
			if(ps2!=null){
				ps2.close();
				
			}
			if(ps3!=null){
				ps3.close();
				
			}
		}			
		catch(Exception e){
			e.printStackTrace();
			throw new LMSException(e);
		}
	}

}

}
