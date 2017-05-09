package com.skilrock.lms.rest.scratchService.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.scratchService.orderMgmt.beans.ScratchGameDataBean;
import com.skilrock.lms.rest.scratchService.dao.ScratchDao;
import com.skilrock.lms.rest.services.bean.DaoBean;
import com.skilrock.lms.web.drawGames.common.Util;

public class ScratchDaoImpl implements ScratchDao{

	public DaoBean getUserOrgIdAndUserIdFromTpUserId(String tpUserId) throws SQLException{
		Connection con = null;
		DaoBean bean = null;
		con = DBConnect.getConnection();
		String query = "select user_id , organization_id  from st_lms_user_master  where tp_user_id = ?";
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1,tpUserId);
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			int userId = rs.getInt("user_id");
			int userOrgId = rs.getInt("organization_id");
			bean = new DaoBean();
			bean.setUserId(userId);
			bean.setUserOrgId(userOrgId);
		}
		return bean;
	}
	
	public int getAgentOrgIdFromTPUserId(String tpUserId) {
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String selectQuery = null;
		int agentOrgId = 0;
		try {
			selectQuery = "select organization_id from st_lms_user_master where user_id = ( select parent_user_id from st_lms_user_master where tp_user_id = ? )";
			pstmt = con.prepareStatement(selectQuery);
			pstmt.setString(1, tpUserId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				agentOrgId = rs.getInt("organization_id");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeConnection(con, pstmt, rs);
		}
		
		return agentOrgId;
	}

	@Override
	public UserInfoBean getUserBeanFromTpUserId(String tpUserId, Connection connection) throws LMSException {
		UserInfoBean userInfoBean=null;
		String query=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		try {
			query = "SELECT user_id , organization_id, parent_user_id  FROM st_lms_user_master  WHERE tp_user_id = ?";
			pstmt = connection.prepareStatement(query);
			pstmt.setString(1, tpUserId);
			rs=pstmt.executeQuery();
			if (rs.next()) {
				userInfoBean=new UserInfoBean();
				userInfoBean.setUserId(rs.getInt("user_id"));
				userInfoBean.setUserOrgId(rs.getInt("organization_id"));
				userInfoBean.setParentUserId(rs.getInt("parent_user_id"));
			}
			
		} catch (SQLException e) {
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		finally {
			DBConnect.closeResource(pstmt,rs);
		}
		return userInfoBean;
	}

	@Override
	public ScratchGameDataBean getGameDataWithPwtEndDateVerifyFromTicketNbr(String ticketNbr,Connection connection) throws LMSException {
		int gameNbr =0;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String gameDataQuery=null;
		ScratchGameDataBean scracthGameDataBean=new ScratchGameDataBean();
  
	try {
		gameNbr = Integer.parseInt(ticketNbr.substring(0, Math.min(ticketNbr.length(), 3)));
		gameDataQuery = "SELECT game_name,game_id,pwt_end_date FROM st_se_game_master WHERE game_nbr=?";
		pstmt = connection.prepareStatement(gameDataQuery);
		pstmt.setInt(1,gameNbr);
		rs=pstmt.executeQuery();
		if(rs.next()) {
			if(Util.getCurrentTimeStamp().after(rs.getDate("pwt_end_date"))){
				throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.TICKET_EXPIRE_ERROR_MESSAGE);
			}
			scracthGameDataBean.setGameId(rs.getInt("game_id"));
			scracthGameDataBean.setGameNbr(gameNbr);
			scracthGameDataBean.setGameName(rs.getString("game_name"));
		}
		else{
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GAME_NOT_AVAILABLE_ERROR_MESSAGE);
		}
		
	    }catch (SQLException e) {
	    	e.printStackTrace();
	    	throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
	   }catch(LMSException e) {
			throw e;
		}catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
	} finally {
		DBConnect.closeResource(pstmt,rs);
	}
	return scracthGameDataBean;
	}

	@Override
	public int getParentOrgId(int userId, Connection connection) throws LMSException {
		int parentOrgId = 0;
		String query=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		try {
			query = "SELECT organization_id FROM st_lms_user_master  WHERE user_id = ?";
			pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, userId);
			rs=pstmt.executeQuery();
			if (rs.next()) {
				parentOrgId = rs.getInt("organization_id");
			}
			
		} catch (SQLException e) {
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		finally {
			DBConnect.closeResource(pstmt,rs);
		}
		return parentOrgId;
	}
	
	public DaoBean getGameIdFromGameMasterByUsingGameNbrInTicket(int gameNbr) throws SQLException{
		Connection con = null;
		try{
			DaoBean bean = null;
			con = DBConnect.getConnection();
			String query = "select game_id from st_se_game_master where game_nbr= ?";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setInt(1,gameNbr);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				int gameId = rs.getInt("game_id");
				bean = new DaoBean();
				bean.setGameId(gameId);
			}
			return bean;
		}finally{
		 con.close(); 
	  }
	}
}
