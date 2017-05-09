package com.skilrock.lms.coreEngine.ola;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;




import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
/**
 * This Class performs job of Serial Number Counter Reset
 * @author Neeraj Jain
 *
 */

public class OlaRummyCounterReset {
private static Log logger =LogFactory.getLog(OlaRummyCounterReset.class);

public static void resetCounter() throws LMSException{

	Connection con = DBConnect.getConnection();	
	PreparedStatement ps = null;
	PreparedStatement ps1 = null;
	PreparedStatement ps2 = null;
	ResultSet rs =null;
	String query =null;
	try {
		con.setAutoCommit(false);
		System.out.println("Counter Reset In Process");
		query ="SELECT  opg.wallet_id,last_generated_serial_nbr,pin_type,last_day_serial_nbr FROM st_ola_pin_generation opg INNER JOIN st_ola_wallet_master om  on opg.wallet_id=om.wallet_id and wallet_status='ACTIVE'";
		ps1=con.prepareStatement(query);
		rs=ps1.executeQuery();
		int n=0;// n number of records will be inserted in pin generation history
		while(rs.next()){
			Calendar cal = Calendar.getInstance();
			query="insert into st_ola_pin_generation_history values(?,?,?,?,?)";
			ps2 =con.prepareStatement(query);
			ps2.setInt(1,rs.getInt("wallet_id"));
			ps2.setString(2,rs.getString("last_generated_serial_nbr"));
			ps2.setString(3,rs.getString("pin_type"));
			ps2.setTimestamp(4,new Timestamp(cal.getTime().getTime()));
			ps2.setString(5,rs.getString("last_day_serial_nbr"));
			n=n+ps2.executeUpdate();
			
		}
		System.out.println(n+"Record Inserted in st_ola_pin_generation_history");
		String dayCount=setDayCount(con);
		if(dayCount!=null && !dayCount.equalsIgnoreCase("")){
			query ="update st_ola_pin_generation set last_generated_serial_nbr=?, last_day_serial_nbr=?";
			System.out.println("Query:"+query);
			ps = con.prepareStatement(query);
			ps.setString(1,"00000");
			ps.setString(2,dayCount);
			System.out.println("Update pin generation Query "+ps);
			ps.executeUpdate();
			con.commit();
		}
		
	}catch (SQLException e) {
		logger.info("SQL Exception ",e);
		throw new LMSException("SQL Exception "+e.getMessage());
	}catch (Exception e) {
		logger.info("Exception ",e);
		throw new LMSException("Exception"+e.getMessage());
	}finally{
		DBConnect.closeResource(ps,ps1,ps2,rs,con);
	}
	
}
private static String setDayCount(Connection con){
	String query="select last_day_serial_nbr from st_ola_pin_generation";
	try{
		PreparedStatement ps =con.prepareStatement(query);
		ResultSet rs=ps.executeQuery();
		String dayCount =null;
		if(rs.next()){
			dayCount=rs.getString("last_day_serial_nbr");
		}
		if(dayCount!=null){
			int dCount =Integer.parseInt(dayCount);
			if(dCount==999){
				dayCount="000";
			}
			else{
				dCount++;
			if (dCount / 100 == 0) {
					if (dCount / 10 == 0) {
						dayCount = "00" + dCount;
					} else {
						dayCount = "0" + dCount;
					}
				}

			else {
					dayCount = dCount+ "";
				}
			}
			
		return dayCount;	
		}
		else{
			System.out.println("Problem in getting Day Count");
		}
	}catch(Exception e){
		e.printStackTrace();
	}
	
	return "";
}
public static void main(String[] args) {
	try {
		resetCounter();
	} catch (LMSException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
}
