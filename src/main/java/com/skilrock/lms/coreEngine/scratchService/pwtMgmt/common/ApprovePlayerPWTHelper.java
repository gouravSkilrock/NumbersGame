package com.skilrock.lms.coreEngine.scratchService.pwtMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.skilrock.lms.beans.GamePlayerPWTBean;
import com.skilrock.lms.beans.PrizeStatusBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.GameUtilityHelper;

public class ApprovePlayerPWTHelper {
	public boolean approvePWT(int playerReceiptId) {
		System.out.println("inside approve pwt helper");
		 
		Connection con = null;
		PreparedStatement pstmt;
		int isUpdateDone = 0;
		try {
			con = DBConnect.getConnection();
			con.setAutoCommit(false);
			String updateStatusQuery = "update st_se_direct_player_pwt_temp_receipt set status=? where pwt_receipt_id=?";
			pstmt = con.prepareStatement(updateStatusQuery);
			pstmt.setString(1, "PND_PWT");
			pstmt.setInt(2, playerReceiptId);
			isUpdateDone = pstmt.executeUpdate();
			con.commit();
			if (isUpdateDone == 1) {
				return true;
			} else {
				return false;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return false;
	}

	public boolean denyPWT(int playerReceiptId, int gameId, String virnCode,
			String ticketNbr, String denyPwtStatus, int gameNbr)
			throws LMSException {

		System.out
				.println("ApprovePlayerPwtHelper denyPWT for boMster  & bo admin is called");
		boolean statusChange = false;
		Connection connection = null;
		 
		connection = DBConnect.getConnection();
		try {
			connection.setAutoCommit(false);

			// update direct playe temp table
			// 'CANCELLED_PERMANENT':'Permanent Cancellation',
			// 'UNCLM_CANCELLED':'Temporary Cancellation'
			String tempPwtStatus = "";
			String pwtStatus = "";
			int isPwtTicketDelete = 0;

			if ("Permanent Cancellation".equalsIgnoreCase(denyPwtStatus.trim())) {
				tempPwtStatus = "CANCEL";
				pwtStatus = "CANCELLED_PERMANENT";
			} else if ("Temporary Cancellation".equalsIgnoreCase(denyPwtStatus
					.trim())) {
				tempPwtStatus = "CANCEL";
				pwtStatus = "UNCLM_CANCELLED";
				// deleted entry in case of Temporary Cancellation
				PreparedStatement pstmtPwtTicketUpdate = connection
						.prepareStatement("delete from st_se_pwt_tickets_inv_? where game_id=? and ticket_nbr=?");
				pstmtPwtTicketUpdate.setInt(1, gameNbr);
				pstmtPwtTicketUpdate.setInt(2, gameId);
				pstmtPwtTicketUpdate.setString(3, ticketNbr);
				System.out.println("update ticket  table:: "
						+ pstmtPwtTicketUpdate);
				isPwtTicketDelete = pstmtPwtTicketUpdate.executeUpdate();
				System.out.println("query is  " + pstmtPwtTicketUpdate);
			}

			PreparedStatement pstmt = connection
					.prepareStatement("update st_se_direct_player_pwt_temp_receipt set status=? where pwt_receipt_id=?");
			pstmt.setString(1, tempPwtStatus);
			pstmt.setInt(2, playerReceiptId);
			int isUpdate = pstmt.executeUpdate();
			System.out.println("update st_se_direct_player_pwt_temp_receipt =="
					+ pstmt);

			// update pwt inv table status
			PreparedStatement pstmtPwtInvUpdate = connection
					.prepareStatement("update st_se_pwt_inv_? set status=? where virn_code=? and game_id=?");
			pstmtPwtInvUpdate.setInt(1, gameNbr);
			pstmtPwtInvUpdate.setString(2, pwtStatus);
			pstmtPwtInvUpdate.setString(3, virnCode);
			pstmtPwtInvUpdate.setInt(4, gameId);
			int isPwtupdate = pstmtPwtInvUpdate.executeUpdate();
			System.out.println("update st_pwt_inv ==" + pstmtPwtInvUpdate);

			if ("Temporary Cancellation".equalsIgnoreCase(denyPwtStatus.trim())
					&& isUpdate == 1 && isPwtupdate == 1
					&& isPwtTicketDelete == 1) {
				statusChange = true;
				connection.commit();
				System.out.println("commited successfully");
			} else if ("Permanent Cancellation".equalsIgnoreCase(denyPwtStatus
					.trim())
					&& isUpdate == 1 && isPwtupdate == 1) {
				statusChange = true;
				connection.commit();
				System.out.println("commited successfully");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
		} finally {

			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
				throw new LMSException(se);
			}

		}

		return statusChange;
	}

	/**
	 * This method inside helper class is used to provide list of remaining
	 * prizes for a game
	 * 
	 * @param gameId
	 * @return List of remaining prizes
	 * @throws LMSException
	 */

	public List<PrizeStatusBean> fetchRemainingPrizeList(int gameId)
			throws LMSException {
		return GameUtilityHelper.fetchRemainingPrizeList(gameId);

		/*
		 * Connection connection = null;
		 * 
		 * PreparedStatement pstmt = null; PreparedStatement prizePstmt = null;
		 * 
		 * ResultSet resultSet = null; ResultSet prizeRSet = null;
		 * 
		 * 
		 * List<PrizeStatusBean> prizeStatusList = new ArrayList<PrizeStatusBean>();
		 * 
		 * 
		 * double pwtAmt = 0.0; int nbrOfPrizeLeft = 0;
		 * 
		 * 
		 * try {
		 * 
		 * PrizeStatusBean bean = null;  
		 * connection = DBConnect.getConnection(); String prizeQuery =
		 * QueryManager.getST1DistinctPrizeQuery(); prizePstmt =
		 * connection.prepareStatement(prizeQuery); prizePstmt.setInt(1,gameId);
		 * prizeRSet = prizePstmt.executeQuery();
		 * 
		 * while(prizeRSet.next()){ bean = new PrizeStatusBean();
		 * bean.setPrizeAmt(prizeRSet.getDouble(TableConstants.SPI_PWT_AMT));
		 * //bean.setNbrOfPrizeLeft(0); prizeStatusList.add(bean); } String
		 * query = QueryManager.getST1PrizeListQuery(); pstmt =
		 * connection.prepareStatement(query); pstmt.setInt(1, gameId);
		 * resultSet = pstmt.executeQuery(); while(resultSet.next()){
		 * //isPrizeRemaining = true;
		 * 
		 * pwtAmt = resultSet.getDouble(TableConstants.SPI_PWT_AMT);
		 * nbrOfPrizeLeft = resultSet.getInt(TableConstants.PRIZES_REMAINING);
		 * 
		 * for(int i=0; i< prizeStatusList.size(); i++){ bean =
		 * prizeStatusList.get(i); if (pwtAmt == bean.getPrizeAmt()){
		 * bean.setNbrOfPrizeLeft(nbrOfPrizeLeft); break; } } }
		 * 
		 * return prizeStatusList; } catch (SQLException e) {
		 * 
		 * e.printStackTrace(); throw new LMSException(e);
		 * 
		 * }finally {
		 * 
		 * try {
		 * 
		 * if (pstmt != null) { pstmt.close(); } if (connection != null) {
		 * connection.close(); } } catch (SQLException se) {
		 * se.printStackTrace(); } }
		 * 
		 * 
		 * //return null;
		 */

	}

	public List<GamePlayerPWTBean> getUnapprovedPwt() throws LMSException {
		 
		Connection con = null;
		PreparedStatement pstmt;
		GamePlayerPWTBean gamePlayerBean;
		List<GamePlayerPWTBean> gamePlayerBeanList = new ArrayList<GamePlayerPWTBean>();
		try {
			con = DBConnect.getConnection();
			String getUnapprovePwtQuery = "select b.game_name,b.game_nbr,a.pwt_receipt_id,a.player_id,a.game_id,a.virn_code,a.pwt_amt,a.status,a.ticket_nbr from st_se_direct_player_pwt_temp_receipt a,st_se_game_master b where a.status=? and a.game_id=b.game_id";
			pstmt = con.prepareStatement(getUnapprovePwtQuery);
			pstmt.setString(1, "PND_ADM");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				gamePlayerBean = new GamePlayerPWTBean();
				gamePlayerBean.setPlayerReceiptId(rs.getInt("pwt_receipt_id"));
				gamePlayerBean.setPlrId(rs.getInt("player_id"));
				gamePlayerBean.setPwtAmt(rs.getDouble("pwt_amt"));
				gamePlayerBean.setTicketNbr(rs.getString("ticket_nbr"));
				gamePlayerBean.setGameId(rs.getInt("game_id"));
				gamePlayerBean.setGameName(rs.getString("game_name"));
				gamePlayerBean.setGameNbr(rs.getInt("game_nbr"));
				gamePlayerBean.setVirnCode(rs.getString("virn_code"));
				gamePlayerBeanList.add(gamePlayerBean);
			}

			return gamePlayerBeanList;

		} catch (SQLException e) {

			e.printStackTrace();

		} finally {
			try {

				if (con != null) {
					con.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return null;

	}

	public GamePlayerPWTBean getUnapprovedPwtDetails(int plrId, int gameId)
			throws LMSException {
		 
		Connection con = null;
		PreparedStatement pstmt;
		GamePlayerPWTBean gamePlayerBean;
		try {
			gamePlayerBean = new GamePlayerPWTBean();
			con = DBConnect.getConnection();
			String getDetailsQuery = "select b.game_name,b.game_nbr,a.first_name,a.last_name,a.addr_line1,a.addr_line2,a.city,a.email_id,a.phone_nbr from st_lms_player_master a,st_se_game_master b where a.player_id=? and b.game_id=?";
			pstmt = con.prepareStatement(getDetailsQuery);
			pstmt.setInt(1, plrId);
			pstmt.setInt(2, gameId);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				gamePlayerBean.setFirstName(rs.getString("first_name"));
				gamePlayerBean.setLastName(rs.getString("last_name"));
				gamePlayerBean.setPlrAddr1(rs.getString("addr_line1"));
				gamePlayerBean.setPlrAddr2(rs.getString("addr_line2"));
				gamePlayerBean.setPlrCity(rs.getString("city"));
				gamePlayerBean.setEmailId(rs.getString("email_id"));
				gamePlayerBean.setPhone(rs.getString("phone_nbr"));

				gamePlayerBean.setGameName(rs.getString("game_name"));
				gamePlayerBean.setGameNbr(rs.getInt("game_nbr"));

			}
			return gamePlayerBean;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

		return null;
	}

}