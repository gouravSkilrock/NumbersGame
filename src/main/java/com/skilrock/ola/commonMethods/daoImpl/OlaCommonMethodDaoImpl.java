package com.skilrock.ola.commonMethods.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.ola.common.CommonFunctionsHelper;
import com.skilrock.ola.accMgmt.javaBeans.OLADepositRefundBean;
import com.skilrock.ola.javaBeans.OlaWalletBean;

public class OlaCommonMethodDaoImpl {
	static Log logger = LogFactory.getLog(OlaCommonMethodDaoImpl.class);
	
	public Map<Integer, OlaWalletBean> olaWalletDetails() {
		String queryStr = null;		
		
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;	
		
		Map<Integer, OlaWalletBean> walletDataMap = new LinkedHashMap<Integer, OlaWalletBean>();		
		try{	
				con = DBConnect.getConnection();				
				stmt = con.createStatement();
				queryStr = "SELECT SQL_CACHE wallet_id,wallet_display_name,wallet_name,verification_type,min_deposit,registration_type FROM st_ola_wallet_master WHERE wallet_status = 'ACTIVE'";
				logger.info("Wallet Data Query : "+ queryStr);
				rs = stmt.executeQuery(queryStr);				
				while (rs.next()) {
					OlaWalletBean olaWalletBean = new OlaWalletBean();
					olaWalletBean.setWalletId(rs.getInt("wallet_id"));
					olaWalletBean.setWalletDisplayName(rs.getString("wallet_display_name"));
					olaWalletBean.setWalletDevName(rs.getString("wallet_name"));
					olaWalletBean.setVerificationType(rs.getString("verification_type"));
					olaWalletBean.setMinDeposit(rs.getDouble("min_deposit"));
					olaWalletBean.setRegistrationType(rs.getString("registration_type"));
					walletDataMap.put(rs.getInt("wallet_id"),olaWalletBean);
				}				
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			DBConnect.closeConnection(con, stmt, rs);
		}
		return walletDataMap;
	}
	
	
	
	public static int checkPlrAffiliateMapping(Connection con, int plrId,int walletId)throws SQLException, LMSException {		
		String getMappingQry = "select ref_user_id from st_ola_affiliate_plr_mapping where player_id =? and wallet_id=?";
		PreparedStatement getMappingPstmt = con.prepareStatement(getMappingQry);
		getMappingPstmt.setInt(1, plrId);
		getMappingPstmt.setInt(2, walletId);
		logger.info(getMappingPstmt);
		ResultSet rs = getMappingPstmt.executeQuery();
		if (rs.next()) {
			return rs.getInt("ref_user_id");
		} else {
			return 0;
		}			
	}
	
	public static void bindPlrNAffiliate(Connection con, int plrId,UserInfoBean userBean,int walletId) throws SQLException {
			String mappingQry = "insert into st_ola_affiliate_plr_mapping values(?,?,?,?,?)";
			PreparedStatement mappingPstmt = con.prepareStatement(mappingQry);
				mappingPstmt.setInt(1, plrId);
				mappingPstmt.setInt(2, userBean.getUserId());
				mappingPstmt.setString(3, userBean.getUserType());
				mappingPstmt.setInt(4, userBean.getUserOrgId());
				mappingPstmt.setInt(5, walletId);
			int isUpdate = mappingPstmt.executeUpdate();
			logger.info("st_ola_affiliate_plr_mapping is update"+isUpdate);
	}
	
	
	public static int fetchPlayerIdFromRefCode(String refCode,int walletId,Connection con, String verificationType,StringBuilder userName) throws SQLException, LMSException{
		int playerId = 0;		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String verificationQuery = null;
				if("MOBILE_NUM".equalsIgnoreCase(verificationType)){
					verificationQuery = "select lms_plr_id,username from st_ola_player_master where phone = ? and wallet_id=? ";
				}else{
					verificationQuery = "select lms_plr_id,username from st_ola_player_master where username = ? and wallet_id=?";
				}
				pstmt = con.prepareStatement(verificationQuery);
				pstmt.setString(1, refCode);
				pstmt.setInt(2, walletId);
				rs = pstmt.executeQuery();
				if(rs.next()){
					playerId = rs.getInt("lms_plr_id");
					userName.append(rs.getString("username"));
				}
			
		return playerId;
	}
	
	public static boolean checkOrgBalance(int userOrgId, double userAmt, String userType, Connection con) throws SQLException, LMSException {
		boolean isSaleBalanceAval = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		pstmt = con.prepareStatement("select (available_credit-claimable_bal) as availbale_sale_bal from st_lms_organization_master where organization_id=?");
		pstmt.setInt(1, userOrgId);
		rs = pstmt.executeQuery();
		if(rs.next()){
			if(!((rs.getDouble("availbale_sale_bal")) > userAmt)){
				if("AGENT".equalsIgnoreCase(userType)){
					throw new LMSException(LMSErrors.INSUFFICIENT_AGENT_BALANCE_ERROR_CODE);
				}
				if("RETAILER".equalsIgnoreCase(userType)){
					throw new LMSException(LMSErrors.INSUFFICIENT_RETAILER_BALANCE_ERROR_CODE);
				}
			}else{
				isSaleBalanceAval = true;
			}
		}else{
			throw new LMSException(LMSErrors.INVALID_RETAILER_ERROR_CODE);
		}
		return isSaleBalanceAval;
	}
	
	
	public static boolean checkRefCodeinOLA(String refCode,String verificationType,int walletId) throws SQLException, LMSException{
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String verificationQuery = null;
		
				if("MOBILE_NUM".equalsIgnoreCase(verificationType)){
					verificationQuery = "select lms_plr_id from st_ola_player_master where phone = ? and wallet_id=?";
				}else{
					verificationQuery = "select lms_plr_id from st_ola_player_master where username = ? and wallet_id=?";
				}
				pstmt = con.prepareStatement(verificationQuery);
				pstmt.setString(1, refCode);
				pstmt.setInt(2, walletId);
				rs = pstmt.executeQuery();
				if(rs.next()){
					return true;
				}			
		return false;
	}
	
	
	public static String getPlayerNameFromPlayerId(int playerId) throws SQLException{
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String qry = null;
		qry = "select IF(concat(IFNULL(fname,''),' ',IFNULL(lname,''))='',username,concat(IFNULL(fname,''),' ',IFNULL(lname,'')))  name from st_ola_player_master where lms_plr_id = ?";
		pstmt = con.prepareStatement(qry);
		pstmt.setInt(1, playerId);
		rs = pstmt.executeQuery();
		if(rs.next()){
			return rs.getString("name");
		}			
		return "Anonymous";
	}
	
	public static String getUserNameFromMobileNo(String phone,int walletId,Connection con){
		String userName=null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String qry = null;
		try {			
			qry = "SELECT username from st_ola_player_master WHERE phone = ? and wallet_id=? ";
			pstmt = con.prepareStatement(qry);
			pstmt.setString(1, phone);
			pstmt.setInt(2, walletId);
			rs = pstmt.executeQuery();
			if(rs.next()){
				userName=rs.getString("username");
			}			
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			DBConnect.closeConnection(pstmt, rs);
		}
		return userName;
		
	}
	
}
