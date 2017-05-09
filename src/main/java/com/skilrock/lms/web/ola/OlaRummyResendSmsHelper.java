package com.skilrock.lms.web.ola;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.skilrock.lms.beans.OlaRummyResendSMSBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.coreEngine.ola.SendSMS;

public class OlaRummyResendSmsHelper {
Connection con = DBConnect.getConnection();
String query = null;
public ArrayList<OlaRummyResendSMSBean> searchDeposit(ArrayList<OlaRummyResendSMSBean> smsBeanList,double amount,
									String pinType,String plrname,String depositDate,String retailer){
	String plrquery=" ";
	String retquery=" ";
	int deno = (int)amount;
			if(retailer.equalsIgnoreCase("-1")){
				if(!plrname.equalsIgnoreCase("")){
					plrquery = "player_id='"+plrname+"' and ";
				}
				else {
					plrname="NA";
				}
			}
			else {
				retquery="where name='"+retailer+"'";
					if(!plrname.equalsIgnoreCase("")){
						plrquery = " player_id='"+plrname+"' and ";
				}
				else {
					plrname="NA";
				}
			}
	
	try {
			if (pinType.equalsIgnoreCase("oladeposit")) {
				query = "select serial_number,pin_number,amount,player_phone_nbr,player_id,name,transaction_date,verification_status from( "
						+ "(select transaction_id,transaction_date,retailer_org_id from st_lms_retailer_transaction_master where transaction_date like '"
						+ depositDate
						+ "%')aa inner join "
						+ "(select lms_transaction_id,serial_number,pin_number,amount,player_id,player_phone_nbr,verification_status  from st_ola_pin_rep_rm_2"
						+ " where "
						+ plrquery
						+ " amount='"
						+ amount
						+ "')bb 	on aa.transaction_id = bb.lms_transaction_id"
						+ " inner join (select name,organization_id   from st_lms_organization_master "
						+ retquery + ")d2 on retailer_org_id =organization_id)";

				PreparedStatement ps = con.prepareStatement(query);
				ResultSet rs = ps.executeQuery();
				int i = 0;
				while (rs.next()) {
					OlaRummyResendSMSBean smsBean = new OlaRummyResendSMSBean();
					smsBean.setSrNbr(rs.getLong("serial_number"));
					smsBean.setPinNbr(rs.getString("pin_number"));
					smsBean.setAmount(amount);
					smsBean.setDate(rs.getTimestamp("transaction_date") + "");
					smsBean.setPinType(pinType);
					smsBean.setPlrName(rs.getString("player_id"));
					smsBean.setStatus(rs.getString("verification_status"));
					smsBean.setUserPhone(rs.getString("player_phone_nbr"));
					smsBean.setId(i);
					smsBean.setRetOrgName(rs.getString("name"));
					smsBeanList.add(smsBean);
					i++;
				}

			} else {
				String tableName = "st_ola_cashcard_rm_2_" + deno;
				boolean tableExits = checkTable(tableName, con);
				if (!tableExits) {
					System.out.println("Table Does Not Exits");
				} else {
					query = " select serial_number,pin_number,amount,name,transaction_date,verification_status from("
							+ " (select transaction_id,transaction_date,retailer_org_id from st_lms_retailer_transaction_master where transaction_date like '"
							+ depositDate
							+ "%')aa inner join "
							+ "(select lms_transaction_id,serial_number,pin_number,amount,verification_status  from "+tableName+")bb " 
							+ "	on aa.transaction_id = bb.lms_transaction_id "
							+ " inner join (select name,organization_id   from st_lms_organization_master "
							+ retquery + ")d2 on retailer_org_id =organization_id)";
					PreparedStatement ps = con.prepareStatement(query);
					ResultSet rs = ps.executeQuery();
					int i = 0;
					while (rs.next()) {
						OlaRummyResendSMSBean smsBean = new OlaRummyResendSMSBean();
						smsBean.setSrNbr(rs.getLong("serial_number"));
						smsBean.setPinNbr(rs.getString("pin_number"));
						smsBean.setAmount(amount);
						smsBean.setDate(rs.getTimestamp("transaction_date")+ "");
						smsBean.setPinType(pinType);
						smsBean.setPlrName(plrname);
						smsBean.setStatus(rs.getString("verification_status"));
						smsBean.setUserPhone("NA");
						smsBean.setRetOrgName(rs.getString("name"));
						smsBean.setId(i);
						smsBeanList.add(smsBean);
						i++;
					}
				}

			
			}

		}
	catch(Exception e){
		e.printStackTrace();
	}
	finally {
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();

		}
	}
return smsBeanList;
	
}	

public String sendSMS(OlaRummyResendSMSBean smsBean,String desKey,String propKey){
	
	CashCardPinGeneratorHelper helper = new CashCardPinGeneratorHelper();
	StringBuilder sb = new StringBuilder(smsBean.getSrNbr()+"");
	String srNbr =sb.substring(0,4)+" "+sb.substring(4,8)+" "+sb.substring(8,12)+" "+sb.substring(12,16);
	sb = new StringBuilder(helper.decryptPin(smsBean.getPinNbr(),desKey, propKey));
	String pinNbr =sb.substring(0,4)+" "+sb.substring(4,8)+" "+sb.substring(8,12)+" "+sb.substring(12,16);		
	String msg ="Dear Customer, Your Deposit details are: Usr Name: "+smsBean.getPlrName()+" Amt: "+smsBean.getAmount()+" Sr. Nbr :"+srNbr+" Pin Nbr: "+pinNbr+" To redeem pin please visit www.khelplayrummy.com";
	SendSMS smsSend = new SendSMS(msg,smsBean.getUserPhone());
	smsSend.setDaemon(true);
	smsSend.start();
	System.out.println(" SMS Sent");
	
	return "Message Sent";
	
}
public boolean checkTable(String tableName, Connection con) {

	try {
		ResultSet rs = con.getMetaData().getTables(null, null, tableName,null);
		if (rs.next()) {
			return true;
		}
	}
	catch (SQLException e) {
		e.printStackTrace();
		return false;
	}

	return false;
}	


}
