package com.skilrock.lms.rest.services.common.daoImpl;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.rest.services.bean.TpEBetDataBean;
import com.skilrock.lms.web.drawGames.common.Util;

public class TpEBetMgmtDaoImpl {
    
    private static Logger logger = LoggerFactory.getLogger(TpEBetMgmtDaoImpl.class);
	
	private static TpEBetMgmtDaoImpl classInstance = null;

	public static TpEBetMgmtDaoImpl getInstance() {
		if (classInstance == null)
			classInstance = new TpEBetMgmtDaoImpl();
		return classInstance;
	}
	
	public synchronized void fetchEBetTokenId(TpEBetDataBean betDataBean)throws LMSException{
	    	  logger.info("inside fetchEBetTokenId method with betDataBean {} ",betDataBean);
		  Connection con=null;
		  int tokenCount = 0;
		  try{
			con=DBConnect.getConnection();
			tokenCount = TpEBetMgmtDaoImpl.getInstance().checkAndGenerateTokenCount(betDataBean,con);
			if(tokenCount <Integer.parseInt(Utility.getPropertyValue("MAX_DEVICE_PER_RETAILER"))){
			    int getTokenCount = Integer.parseInt(betDataBean.getTokenId().substring(1,betDataBean.getTokenId().length()));
			    if(getTokenCount < 99){
				   if(getTokenCount< 9)
				     betDataBean.setTokenId(betDataBean.getDeviceCode()+"0"+(getTokenCount+1));
				   else{
				       betDataBean.setTokenId(betDataBean.getDeviceCode()+(getTokenCount+1));
				   }
			    }else if(getTokenCount == 99) {
				betDataBean.setTokenId(betDataBean.getDeviceCode()+"0"+1);
			    }else {
				  throw new LMSException(LMSErrors.BET_SLIP_NOT_GENERATED_ERROR_CODE,LMSErrors.BET_SLIP_NOT_GENERATED_ERROR_MESSAGE);
			    }
			  
			    TpEBetMgmtDaoImpl.getInstance().saveRequestDataAndGetTokenId(betDataBean,con);
			}else{
				throw new LMSException(LMSErrors.MAX_DEVICE_PER_RETAILER_ERROR_CODE,LMSErrors.MAX_DEVICE_PER_RETAILER_ERROR_MESSAGE);
			}
		  }catch(LMSException e){
			throw e;
		  }catch(Exception e){
			  e.printStackTrace();
			  throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		  }finally{
		      DBConnect.closeResource(con);
		  }
	}


	private int checkAndGenerateTokenCount(TpEBetDataBean betDataBean,Connection con)throws LMSException {
	    logger.info("inside fetchAndCheckRequestDataData method with connection and betDataBean {} ",betDataBean);

	    	PreparedStatement pstmt = null;
		ResultSet rs = null;
		int ticketCount = 0;
		try {
			pstmt = con.prepareStatement(" SELECT  b.id,b.user_id,b.organization_id,b.device_code ,IFNULL(token_id, '00') as token_id  FROM st_lms_ebet_sale_request a right join st_lms_ebet_retailer_mappping b on a.device_map_id = b.Id and a.status = 'initiated' and a.ebet_expiry_datetime > ?  WHERE b.device_id  = ?  order by a.id desc");
			pstmt.setString(1,Util.getCurrentTimeString());
			pstmt.setString(2,betDataBean.getDeviceId());
			logger.info("fetchAndCheckRequestDataData**************** pstmt= {}",pstmt);
			rs = pstmt.executeQuery();
			while (rs.next()) {
			    if(rs.getRow() == 1){
				betDataBean.setUserId(rs.getInt("user_id"));
				betDataBean.setDeviceMapId(rs.getInt("id"));
				betDataBean.setOrganizationId(rs.getInt("organization_id"));
				betDataBean.setDeviceCode(rs.getString("device_code"));
				betDataBean.setTokenId(rs.getString("token_id"));
			    }
			    ticketCount = rs.getRow();
			}
		} catch (SQLException e) {
			logger.error("SQLException e {}",e);
			throw new LMSException();
		}finally{
		      DBConnect.closeResource(pstmt,rs);
		  }
		return ticketCount;
	}
	

	private void saveRequestDataAndGetTokenId(TpEBetDataBean betDataBean,	Connection con)throws LMSException{
	    logger.info("inside saveRequestDataAndGetTokenId method with connection and betDataBean {} ",betDataBean);
		PreparedStatement pstmt  = null;
		try{
		    pstmt  = con.prepareStatement("insert into st_lms_ebet_sale_request(token_id,user_id,organization_id,device_map_id,request_data,sale_type,ebet_request_datetime,ebet_expiry_datetime,user_mobile,status)values(?,?,?,?,?,?,?,date_add(?,interval ? MINUTE),?,?)",Statement.RETURN_GENERATED_KEYS);
		    pstmt.setString(1,betDataBean.getTokenId());
		    pstmt.setInt(2, betDataBean.getUserId());
		    pstmt.setInt(3,betDataBean.getOrganizationId());
		    pstmt.setInt(4,betDataBean.getDeviceMapId());
		    pstmt.setString(5,betDataBean.getRequestData());
		    pstmt.setString(6,betDataBean.getSaleType());
		    pstmt.setString(7,Util.getCurrentTimeString());
		    pstmt.setString(8,Util.getCurrentTimeString());
		    pstmt.setString(9,Utility.getPropertyValue("EBET_EXPIRY_PERIOD"));
		    pstmt.setString(10,betDataBean.getMobileNumber());
		    pstmt.setString(11,"Initiated");
		    logger.info("st_lms_ebet_sale_request Insert Data pstmt= {}",pstmt);
		    pstmt.executeUpdate();
		}catch (SQLException e) {
			logger.error("SQLException e {}",e);
			throw new LMSException();
		}finally{
		    DBConnect.closeResource(pstmt);
		}
	}
	
	public List<TpEBetDataBean> fetchSaleData(TpEBetDataBean betDataBean)throws LMSException {
	    logger.info("inside fetchAndCheckRequestDataData method with connection and betDataBean {} ",betDataBean);

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		TpEBetDataBean bean = null;
		Connection con = null;
		Blob blob = null; 
		int orgId = 0;
		List<TpEBetDataBean> betDataList = new ArrayList<TpEBetDataBean>();
		try {
		    	con = DBConnect.getConnection();
		    	orgId = new TpEBetMgmtDaoImpl().getOrgIdFromUserName(betDataBean.getUserName(), con);
		    	if (betDataBean.getDeviceId().trim().isEmpty()) {
        		    pstmt = con.prepareStatement("SELECT a.id,token_id,a.user_id,a.organization_id,model_id,device_id,device_code,request_data,sale_type,ebet_request_datetime,ebet_expiry_datetime,user_mobile,processed_datetime FROM st_lms_ebet_sale_request a inner join st_lms_ebet_retailer_mappping b on a.device_map_id = b.Id WHERE a.organization_id = ? and status = 'initiated' and ebet_expiry_datetime > ? order by device_code,ebet_request_datetime desc");
        		    pstmt.setInt(1,orgId);
        		    pstmt.setString(2,Util.getCurrentTimeString());
    			    logger.info("fetchAndCheckRequestDataData**************** pstmt= {}",pstmt);
    			    rs = pstmt.executeQuery();
    			    while(rs.next()) {
    				bean = new TpEBetDataBean();
    				bean.setRequestId(rs.getInt("id"));
    				bean.setTokenId(rs.getString("token_id"));
    				bean.setUserId(rs.getInt("user_id"));
    				bean.setOrganizationId(rs.getInt("organization_id"));
    				bean.setDeviceCode(rs.getString("device_code"));
    				blob =rs.getBlob("request_data");
    				bean.setRequestData(new String(blob.getBytes(1,(int)blob.length())));
    				bean.setSaleType(rs.getString("sale_type"));
    				bean.setEbetRequestDatetime(rs.getString("ebet_request_datetime"));
    				bean.setEbetExpiryDatetime(rs.getString("ebet_expiry_datetime"));
    				bean.setMobileNumber(rs.getString("user_mobile"));
    				bean.setProcessedDatetime(rs.getString("processed_datetime"));
    				bean.setDeviceId(rs.getString("device_id"));
    				bean.setModelId(rs.getInt("model_id"));
    				betDataList.add(bean);
    			    }
			}else{
			    pstmt = con.prepareStatement("SELECT a.id,token_id,a.user_id,a.organization_id,model_id,device_id,device_code,request_data,sale_type,ebet_request_datetime,ebet_expiry_datetime,user_mobile,processed_datetime FROM st_lms_ebet_sale_request a inner join st_lms_ebet_retailer_mappping b on a.device_map_id = b.Id WHERE b.device_id = ? and  a.organization_id = ? and status = 'initiated' and ebet_expiry_datetime > ? order by device_code,ebet_request_datetime desc");
        		    pstmt.setString(1,betDataBean.getDeviceId());
        		    pstmt.setInt(2, orgId);
        		    pstmt.setString(3, Util.getCurrentTimeString());
    			    logger.info("fetchAndCheckRequestDataData**************** pstmt= {}",pstmt);
    			    rs = pstmt.executeQuery();
			    while(rs.next()) {
        			bean = new TpEBetDataBean();
        			bean.setRequestId(rs.getInt("id"));
        			bean.setTokenId(rs.getString("token_id"));
        			bean.setUserId(rs.getInt("user_id"));
        			bean.setOrganizationId(rs.getInt("organization_id"));
        			bean.setDeviceCode(rs.getString("device_code"));
        			blob =rs.getBlob("request_data");
    				bean.setRequestData(new String(blob.getBytes(1,(int)blob.length())));
        			bean.setSaleType(rs.getString("sale_type"));
        			bean.setEbetRequestDatetime(rs.getString("ebet_request_datetime"));
        			bean.setEbetExpiryDatetime(rs.getString("ebet_expiry_datetime"));
        			bean.setMobileNumber(rs.getString("user_mobile"));
        			bean.setProcessedDatetime(rs.getString("processed_datetime"));
        			bean.setModelId(rs.getInt("model_id"));
        			betDataList.add(bean);
        		    }
			}
			
		} catch (SQLException e) {
			logger.error("SQLException e {}",e);
			throw new LMSException();
		}finally{
		      DBConnect.closeResource(con,pstmt,rs);
		  }
		return betDataList;
	}
	public void cancelEBetSaleData(int requestId)
		throws LMSException {
	    	logger.info("inside cancelEBetSaleData method with connection",requestId);
	    	PreparedStatement ps = null;
	    	Connection con = null;
	    	try {
        		con = DBConnect.getConnection();
        		String query = "update st_lms_ebet_sale_request set processed_datetime=?,status=? where Id=? and status=?";
        		ps = con.prepareStatement(query);
        		ps.setString(1, Util.getCurrentTimeString());
        		ps.setString(2, "Cancelled");
        		ps.setInt(3, requestId);
        		ps.setString(4,"Initiated");
        		ps.executeUpdate();
        	} catch (SQLException e) {
        	    logger.info("SQLException e {}", e);
        	    throw new LMSException();
        	} finally {
        	    DBConnect.closeResource(con, ps);
        	}
	}
	
	public int getOrgIdFromUserName(String userName, Connection con)
		throws LMSException {
	    	logger.info("inside getOrgIdFromUserName method with connection and userName {} {}",userName);
	    	PreparedStatement psmt = null;
	    	int orgId = 0;
	    	ResultSet rs = null;
	    	try {
        		String query = "select organization_id from st_lms_user_master where user_name = ?";
        		psmt = con.prepareStatement(query);
        		psmt.setString(1, userName);
        		rs = psmt.executeQuery();
        		while(rs.next()) {
        		    orgId = rs.getInt("organization_id");
        		}
        		logger.info("query {}", psmt);
        	} catch (SQLException e) {
        	    logger.info("SQLException e {}", e);
        	    throw new LMSException();
        	} finally {
        	    DBConnect.closeResource(rs,psmt);
        	}
		return orgId;
	}
	
	public boolean isBetSlipActive(String tokenId) throws LMSException{
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = null;
		try {
			query = "SELECT Id FROM st_lms_ebet_sale_request WHERE token_Id = ? AND ebet_expiry_datetime > ? AND status = 'initiated'";
			con =  DBConnect.getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, tokenId);
			ps.setString(2, Util.getCurrentTimeString());
			rs = ps.executeQuery();
			if(rs.next()) {
				return true;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeResource(con,ps,rs);
		}
		return false;
		
	}
	
}
