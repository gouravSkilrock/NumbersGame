package com.skilrock.lms.api.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.api.lmsPayment.beans.LmsCashPaymentBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.web.drawGames.common.Util;

public class TpUtilityHelper {
	static Log logger=LogFactory.getLog(TpUtilityHelper.class);
	public static Map<String, TpSystemAuthenticationBean> tpSystemAuthenticationMap=null;
	
	static{
		setTpSystemAuthenticationMap();
	}
	
	private static void setTpSystemAuthenticationMap(){
		tpSystemAuthenticationMap=new java.util.HashMap<String, TpSystemAuthenticationBean>();
		PreparedStatement pstmt  = null;
		ResultSet rs=null;
		Connection con=null;
	try{
		con=DBConnect.getConnection();
		pstmt=con.prepareStatement("select id,system_ip,system_username,system_password from st_lms_wrapper_authentication_master");
		logger.debug("Get System Authentication Detail ="+pstmt);
		rs=pstmt.executeQuery();
		TpSystemAuthenticationBean tpAuthBean=null;
		while(rs.next()){
			
			
			String[] systemIpArr=rs.getString("system_ip").split(",");
			for(int i=0;i<systemIpArr.length;i++){
				tpAuthBean=new TpSystemAuthenticationBean();
			tpAuthBean.setSystemIp(systemIpArr[i]);
			tpAuthBean.setSystemPassword(rs.getString("system_password"));
			tpAuthBean.setSystemUserName(rs.getString("system_username"));
			tpAuthBean.setTpSystemId(rs.getInt("id"));
			tpSystemAuthenticationMap.put(systemIpArr[i], tpAuthBean);
			}
		}
		}catch (SQLException se) {
	          se.printStackTrace();
		}finally{
			try{
				con.close();
			}catch (SQLException se) {
				se.printStackTrace();
				
			}
		}
	}
	
	public static boolean checkDuplicateSystemRefTransId(String tpTxnId,int systemTpId) throws LMSException{
		boolean isDuplicate=true;
		PreparedStatement pstmt=null;
		Connection con=null;
		try{
			 con = DBConnect.getConnection();
			
			pstmt = con.prepareStatement("select count(*) count from st_lms_tp_system_txn_mapping where tp_system_id=? and tp_ref_txn_id=?");
			pstmt.setInt(1, systemTpId);
			pstmt.setString(2, tpTxnId);
			logger.debug("Check Duplicate RefTransId ="+pstmt);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()){
				return rs.getInt("count")>0;
			}
			
			DBConnect.closeCon(con);
		}catch (Exception e) {
			e.printStackTrace();
			throw new LMSException();
		}
		
		return isDuplicate;
	}
	
//	public static boolean checkOrgStatus(String orgCode) throws LMSException{
//		boolean isDuplicate=true;
//		PreparedStatement pstmt=null;
//		Connection con=null;
//		try{
//			 con = DBConnect.getConnection();
//			
//			pstmt = con.prepareStatement("select count(*) count from st_lms_tp_system_txn_mapping where tp_system_id=? and tp_ref_txn_id=?");
//			pstmt.setInt(1, systemTpId);
//			pstmt.setString(2, tpTxnId);
//			logger.debug("Check Duplicate RefTransId ="+pstmt);
//			ResultSet rs = pstmt.executeQuery();
//			if(rs.next()){
//				return rs.getInt("count")>0;
//			}
//			
//			DBConnect.closeCon(con);
//		}catch (Exception e) {
//			e.printStackTrace();
//			throw new LMSException();
//		}
//		
//		return isDuplicate;
//	}
	
	public static void storeTpSystemTxnId(int tpSystemId,String lmsTxnId,String tpTxnId,Connection con) throws LMSException{
		PreparedStatement pstmt  = null;
		try{
			
			pstmt  = con.prepareStatement("insert into st_lms_tp_system_txn_mapping(tp_system_id,lms_txn_id,tp_ref_txn_id) values(?,?,?)");
			pstmt.setInt(1, tpSystemId);
			pstmt.setString(2, lmsTxnId);
			pstmt.setString(3, tpTxnId);
			logger.debug("st_lms_tp_system_txn_mapping Insert Data ="+pstmt);
			
			pstmt.executeUpdate();
		}catch (Exception e) {
			e.printStackTrace();
			throw new LMSException();
		}
		
	}
	
	public static String getLmsTransIdFromTpTransId(String tpTransId,int systemTpId){
		PreparedStatement pstmt  = null;
		ResultSet rs=null;
		Connection con=null;
		String lmsTransId=null;
		try{
			con=DBConnect.getConnection();
			pstmt=con.prepareStatement("select user_type,lms_txn_id from st_lms_tp_system_txn_mapping map inner join st_lms_transaction_master tm on map.lms_txn_id=tm.transaction_id where tp_system_id=? and tp_ref_txn_id=?");
			pstmt.setInt(1, systemTpId);
			pstmt.setString(2, tpTransId);
			logger.debug("Get Lms Transaction Id From Ref Transaction Id ="+pstmt);
			rs=pstmt.executeQuery();
			if(rs.next()){
				lmsTransId=rs.getString("lms_txn_id")+"#"+rs.getString("user_type");
			}
		}catch (SQLException se) {
	          se.printStackTrace();
		}finally{
			try{
				con.close();
			}catch (SQLException se) {
				se.printStackTrace();
				
			}
		}
		return lmsTransId;
	}
	
	public static boolean validateTpSystemUser(String ip,String systemUsername,String systemPassword){
		logger.debug("Validate System Auth ip="+ip+" systemUsername="+systemUsername+" systemPassword="+systemPassword);
		if(TpUtilityHelper.tpSystemAuthenticationMap != null){
			TpSystemAuthenticationBean tpAuthBean=TpUtilityHelper.tpSystemAuthenticationMap.get(ip);
			if(tpAuthBean !=null){
				
				if(systemUsername.equals(tpAuthBean.getSystemUserName()) && systemPassword.equals(tpAuthBean.getSystemPassword())){
					return true;
				}
			}else{
				logger.debug("IP Address not Authorized");
			}
			
		}
		logger.debug("INVALID System username or password");
		return false;
	}
	
	public static UserInfoBean getUserDataFromUserId(int userId){
		Connection con = DBConnect.getConnection();
		UserInfoBean userInfo= null;
		try{
		userInfo = getUserDataFromUserId(userId, con) ;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			DBConnect.closeCon(con);
		}
		
		
		return userInfo;
	}
	
	public static UserInfoBean getUserDataFromUserId(int userId, Connection con){
		UserInfoBean userInfo= null;
		String getUserDataQry="select user_id,om.organization_id,role_id,parent_user_id,om.organization_type,registration_date,user_name,name,available_credit,credit_limit,claimable_bal,unclaimable_bal,current_credit_amt,organization_status,status,parent_id,pwt_scrap,tp_organization,isrolehead from st_lms_user_master um,st_lms_organization_master om where um.organization_id=om.organization_id and um.user_id="+userId;
		try{
		
		Statement stmt=con.createStatement();
		ResultSet rs=stmt.executeQuery(getUserDataQry);
		if(rs.next()){
			userInfo = new UserInfoBean();
			
			userInfo.setRoleId(rs.getInt("role_id"));
			userInfo.setUserId(rs.getInt("user_id"));
			userInfo.setUserName(rs.getString("user_name"));
			userInfo.setUserOrgId(rs.getInt("organization_id"));
			userInfo.setUserType(rs.getString("om.organization_type"));
			userInfo.setOrgName(rs.getString("name"));
			userInfo.setAvailableCreditLimit(rs.getDouble("available_credit"));
			userInfo.setClaimableBal(rs.getDouble("claimable_bal"));
			userInfo.setUnclaimableBal(rs.getDouble("unclaimable_bal"));
			userInfo.setCurrentCreditAmt(rs.getDouble("current_credit_amt"));
			userInfo.setStatus(rs.getString("status"));
			userInfo.setOrgStatus(rs.getString("organization_status"));
			userInfo.setPwtSacrap(rs.getString("pwt_scrap"));
			userInfo.setParentOrgId(rs.getInt("parent_id"));
			userInfo.setIsRoleHeadUser(rs.getString("isrolehead"));
		}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return userInfo;
	}
	
	public static UserInfoBean getUserData(){
		//to be change later on
		Connection con=DBConnect.getConnection();
		int userId=0;
		String query="select user_id from st_lms_user_master where organization_type='BO' and isrolehead='Y' and user_name='bomaster'";
		try{
			PreparedStatement ps =con.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				userId = rs.getInt("user_id");
				return getUserDataFromUserId(userId);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				con.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		return null;
		
	}
	
	public static void storeTpSystemTxnIdDetail(String agentTxnId,
			String retailerTxnId, LmsCashPaymentBean cashPaymentBean,
			Connection con) throws LMSException {
		PreparedStatement pstmt = null;
		try {
			pstmt = con
					.prepareStatement("INSERT INTO st_lms_tp_txn_details (agent_trans_id, retailer_trans_id, tp_trans_id, transaction_date, bank_name, branch_name, cashier_name, region_name) VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setLong(1, Long.parseLong(agentTxnId));
			pstmt.setLong(2, Long.parseLong(retailerTxnId));
			pstmt.setString(3, cashPaymentBean.getRefTransId());
			pstmt.setTimestamp(4, Util.getCurrentTimeStamp());
			pstmt.setString(5, cashPaymentBean.getBankName());
			pstmt.setString(6, cashPaymentBean.getBranchName());
			pstmt.setString(7, cashPaymentBean.getCashierName());
			pstmt.setString(8, cashPaymentBean.getRegionName());

			logger.debug("st_lms_tp_txn_details Insert Data =" + pstmt);

			pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException();
		} finally {
			DBConnect.closePstmt(pstmt);
		}
	}
}
