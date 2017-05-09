package com.skilrock.lms.web.bankMgmt.Helpers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.web.bankMgmt.beans.BranchDetailsBean;

public class BankUtil {
	static Log logger = LogFactory.getLog(BankUtil.class);
	public static BranchDetailsBean getBankBranchDetails(int userId,Connection con) throws LMSException{
		BranchDetailsBean branchDetailsBean=null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			String query = " select bmp.bank_id,bmp.branch_id,bank_display_name,branch_display_name from st_lms_user_branch_mapping bmp inner join st_lms_bank_master bm on  bm.bank_id = bmp.bank_id inner join st_lms_branch_master brm on brm.branch_id =bmp.branch_id where user_id=? and  bm.status=? and brm.status=? ";			
			pstmt =con.prepareStatement(query);
			pstmt.setInt(1,userId);
			pstmt.setString(2,"ACTIVE");
			pstmt.setString(3,"ACTIVE"); 
			logger.info("branch Details Query "+pstmt);
			rs =pstmt.executeQuery();
			if(rs.next()){
				branchDetailsBean=new BranchDetailsBean();
				branchDetailsBean.setBankId(rs.getInt("bank_id"));
				branchDetailsBean.setBankName(rs.getString("bank_display_name"));
				branchDetailsBean.setBranchId(rs.getInt("branch_id"));
				branchDetailsBean.setBranchName(rs.getString("branch_display_name"));
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new LMSException("PROBLEM PROCESSING RETAILER PAYMENTS  For Bank or branch CONTACT BACK OFFICE...!!!");
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
		
		return branchDetailsBean;
	}

	public static boolean branchTrnMapping(int userId, long lmsTransId, int bankId, int branchId, String trnType,String reason,Connection con) throws LMSException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			String query = "insert into  st_lms_branch_transaction_mapping  values(?,?,?,?,?,?)";			
			pstmt =con.prepareStatement(query);
			pstmt.setLong(1,lmsTransId);
			pstmt.setInt(2,bankId);
			pstmt.setInt(3,branchId);
			pstmt.setString(4,trnType);
			pstmt.setString(5,reason);
			pstmt.setInt(6,userId);
			logger.info("branch transaction mapping  Query "+pstmt);
			int inserted =pstmt.executeUpdate();
			if(inserted>0){
				return true;
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new LMSException("PROBLEM PROCESSING RETAILER PAYMENTS  For Bank or branch CONTACT BACK OFFICE...!!!");
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
		
		return false;
	
	}
	
	public static boolean isBypassPWTDates(int userId , Connection con){
		
		boolean isbypassDates=false;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		
		try {
			pstmt = con.prepareStatement("select branch_id from st_lms_user_branch_mapping where user_id=? and branch_id in (select branch_id from st_lms_branch_master where status='ACTIVE' and bypass_dates_for_pwt=true)");
			pstmt.setInt(1, userId);
			rs=pstmt.executeQuery();
			while (rs.next()) {
				isbypassDates=true;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			try {
				pstmt.close();
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if(con!=null){
				//DBConnect.closeCon(con);
			}
		}
	return isbypassDates;
	}
	
	
	public static boolean validateChkNoForSameDay(String chequeNumber){
		
		boolean allowCheckEntry=false;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		Connection con=null;
		try {
			con=DBConnect.getConnection();
			pstmt = con.prepareStatement("select cheque_nbr,transaction_date,if(date(transaction_date)<date(now()),0,1 ) flag  from st_dg_bo_direct_plr_pwt  where cheque_nbr=?");
			pstmt.setString(1, chequeNumber);
			rs=pstmt.executeQuery();
			while (rs.next() && !rs.getBoolean("flag")) {
				allowCheckEntry=true;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			try {
				pstmt.close();
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if(con!=null){
				DBConnect.closeCon(con);
			}
		}
	return allowCheckEntry;
	}
	
	
	
	
	
}
