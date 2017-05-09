package com.skilrock.lms.coreEngine.ola.reportMgmt;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.skilrock.lms.beans.OlaPinStatusBean;
import com.skilrock.lms.common.db.DBConnect;
/**
 * Helper class to fetch pin status records
 * @author Neeraj jain
 *
 */
public class OlaBoPinStatusReportHelper {
/**
 * This method fetch Pin status record
 * @param walletId 
 * @param distributorType For whom pins has been generated
 * @param pinStatusBeanList record list
 * @param amount denomination
 * @return ArrayList of OlaPinStatusBean
 */	
	public ArrayList<OlaPinStatusBean> pinStatusData(int walletId,String distributorType,ArrayList<OlaPinStatusBean> pinStatusBeanList,int amount){
		Connection con =DBConnect.getConnection();
		String query =null;
		String subQuery=" ";
		String amtQuery =" ";
		PreparedStatement ps =null;
	// create subQuery and amtQuery according to amount,walletId,distributorType input value
		if(amount!=-1){
			amtQuery ="where amount="+amount+" ";
			if(walletId==-1){
				if(!distributorType.endsWith("-1")){
					subQuery=" and generated_for='"+distributorType+"'";
				}
			}
			else{
				if(!distributorType.endsWith("-1")){
					subQuery=" and generated_for='"+distributorType+"' and wallet_id="+walletId;
				}
				else{
					subQuery=" and wallet_id="+walletId;
				}
			}
		}
		else{
			if(walletId==-1){
				if(!distributorType.endsWith("-1")){
					subQuery=" where generated_for='"+distributorType+"'";
				}
			}
			else{
				if(!distributorType.endsWith("-1")){
					subQuery=" where generated_for='"+distributorType+"' and wallet_id="+walletId;
				}
				else{
					subQuery=" where wallet_id="+walletId;
				}
			}
			
		}
		
		
		query ="select date,generated_for,amount,no_of_pin_generated,no_of_pin_redeemed,wallet_display_name,sale_comm_rate,expiry_date from (select Date(generation_time) date,Date(expiry_date) expiry_date,sale_comm_rate,generated_for,amount,sum(no_of_pin_generated)no_of_pin_generated,sum(no_of_pin_redeemed) no_of_pin_redeemed,wallet_id from st_ola_pin_status "+amtQuery+" "+subQuery+" group by Date(generation_time),amount,wallet_id,generated_for,expiry_date)ps " +
				"inner join (select wallet_id,wallet_display_name from st_ola_wallet_master where wallet_status='ACTIVE')wm on wm.wallet_id=ps.wallet_id and amount>10";
		try{		
			ps =con.prepareStatement(query);
			ResultSet rs =ps.executeQuery();
			while(rs.next()){
				// set data in 	OlaPinStatusBean
				OlaPinStatusBean olaPinStatusBean = new OlaPinStatusBean();
				olaPinStatusBean.setAmount(rs.getInt("amount"));
				olaPinStatusBean.setDistributor(rs.getString("generated_for"));
				olaPinStatusBean.setGenerationDate(rs.getDate("date"));
				olaPinStatusBean.setPinGenerated(rs.getInt("no_of_pin_generated"));
				olaPinStatusBean.setPinRedeemed(rs.getInt("no_of_pin_redeemed"));
				olaPinStatusBean.setRedeemedAt(rs.getString("wallet_display_name"));
				olaPinStatusBean.setSaleCommRate(rs.getDouble("sale_comm_rate"));
				olaPinStatusBean.setExpiryDate(rs.getDate("expiry_date"));
				pinStatusBeanList.add(olaPinStatusBean);// add bean to ArrayList
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		return pinStatusBeanList;
	}

}
