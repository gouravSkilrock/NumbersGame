package com.skilrock.lms.coreEngine.accMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.web.drawGames.common.Util;

public class UpdateGovCommRateHelper {
	static Log logger = LogFactory.getLog(UpdateGovCommRateHelper.class);

	public Map<String, String> getGameMap() {
		Map<String, String> gameMap = new TreeMap<String, String>();
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = con
					.prepareStatement("select game_id, concat(game_nbr, concat('-',game_name)) 'game_name', govt_comm_rate  from st_se_game_master where game_status !='TERMINATE'");
			ResultSet rs = pstmt.executeQuery();
			logger.debug("getgameList");
			while (rs.next()) {
				String gameIdAmt = rs.getInt("game_id") + ":"
						+ rs.getDouble("govt_comm_rate");
				String gameName = rs.getString("game_name");
				gameMap.put(gameIdAmt, gameName);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return gameMap;
	}

	public boolean updateGovCommRateDG(String type, Integer id, Double govtCommRate) {
		boolean flag = true;
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		try {
			con.setAutoCommit(false);

			String field = "SALE".equals(type) ? "govt_comm" : "govt_comm_pwt";
			pstmt = con
					.prepareStatement("update st_dg_game_master set "+field+"=?  where game_id =?");
			pstmt.setDouble(1, govtCommRate);
			pstmt.setInt(2, id);
			int updateRow = pstmt.executeUpdate();
			logger.debug("update row " + updateRow
					+ "\nst_dg_game_master query == " + pstmt);
			pstmt.close();
			if (updateRow < 1) {
				throw new LMSException("rows not Updated");
			}

			// putting the entry into gov_comm_rate_history table
			pstmt = con
					.prepareStatement("insert into st_dg_game_govt_comm_history (game_id, govt_comm_rate, govt_comm_type, date_changed) values (?, ?, ?, ?);");
			pstmt.setInt(1, id);
			pstmt.setDouble(2, govtCommRate);
			pstmt.setString(3, type);
			pstmt.setTimestamp(4, Util.getCurrentTimeStamp());
			updateRow = pstmt.executeUpdate();
			logger.debug("update row " + updateRow
					+ "\nst_dg_game_master query == " + pstmt);
			pstmt.close();
			if (updateRow < 1) {
				throw new LMSException("rows not Updated");
			}

			con.commit();

		} catch (SQLException e) {
			e.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			flag = false;
		} catch (LMSException e) {
			e.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			flag = false;
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return flag;
	}

	public boolean updateGovCommRateSLE(String type, Integer id, Double govtCommRate) {
		boolean flag = true;
		Connection connection = null;
		PreparedStatement pstmt = null;
		try {
			connection = DBConnect.getConnection();
			connection.setAutoCommit(false);

			String field = "SALE".equals(type) ? "gov_comm_rate" : "govt_pwt_comm_rate";
			pstmt = connection.prepareStatement("UPDATE st_sle_game_type_master SET "+field+"=? WHERE game_type_id=?;");
			pstmt.setDouble(1, govtCommRate);
			pstmt.setInt(2, id);
			int updateRow = pstmt.executeUpdate();
			pstmt.close();
			if (updateRow < 1)
				throw new LMSException("rows not Updated");

			/*pstmt = connection.prepareStatement("INSERT INTO st_dg_game_govt_comm_history (game_id, govt_comm_rate, govt_comm_type, date_changed) VALUES (?,?,?,?);");
			pstmt.setInt(1, id);
			pstmt.setDouble(2, govtCommRate);
			pstmt.setString(3, type);
			pstmt.setTimestamp(4, Util.getCurrentTimeStamp());
			updateRow = pstmt.executeUpdate();
			pstmt.close();
			if (updateRow < 1)
				throw new LMSException("rows not Updated");*/

			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			flag = false;
		} catch (LMSException e) {
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			flag = false;
		} finally {
			DBConnect.closeConnection(connection, pstmt);
		}

		return flag;
	}

	public boolean updateGovCommRateIW(Integer id, Double govtCommRate) {
		boolean flag = true;
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		try {
			con.setAutoCommit(false);
			pstmt = con.prepareStatement("update st_iw_game_master set gov_comm_rate=?  where game_id =?");
			pstmt.setDouble(1, govtCommRate);
			pstmt.setInt(2, id);
			int updateRow = pstmt.executeUpdate();
			logger.debug("update row " + updateRow + "\n st_iw_game_master query == " + pstmt);
			pstmt.close();
			if (updateRow < 1) {
				throw new LMSException("rows not Updated");
			}
			// putting the entry into gov_comm_rate_history table
			pstmt = con.prepareStatement("insert into st_iw_game_govt_comm_history values (?, ?, ?)");
			pstmt.setInt(1, id);
			pstmt.setDouble(2, govtCommRate);
			pstmt.setTimestamp(3, new Timestamp(new java.util.Date().getTime()));
			updateRow = pstmt.executeUpdate();
			logger.debug("update row " + updateRow + "\n st_iw_game_govt_comm_history query == " + pstmt);
			pstmt.close();
			if (updateRow < 1) {
				throw new LMSException("rows not Updated");
			}
			con.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			flag = false;
		} catch (LMSException e) {
			e.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			flag = false;
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return flag;
	}

	public boolean updateGovCommRateSE(Integer id, Double govtCommRate) {
		boolean flag = true;
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		try {
			con.setAutoCommit(false);

			pstmt = con
					.prepareStatement("update st_se_game_master set govt_comm_rate=?  where game_id =?");
			pstmt.setDouble(1, govtCommRate);
			pstmt.setInt(2, id);
			int updateRow = pstmt.executeUpdate();
			logger.debug("update row " + updateRow
					+ "\nst_se_game_master query == " + pstmt);
			pstmt.close();
			if (updateRow < 1) {
				throw new LMSException("rows not Updated");
			}

			// putting the entry into gov_comm_rate_history table
			pstmt = con
					.prepareStatement("insert into st_se_game_govt_comm_history values (?, ?, ?)");
			pstmt.setInt(1, id);
			pstmt.setDouble(2, govtCommRate);
			pstmt
					.setTimestamp(3, new Timestamp(new java.util.Date()
							.getTime()));
			updateRow = pstmt.executeUpdate();
			logger.debug("update row " + updateRow
					+ "\nst_se_game_master query == " + pstmt);
			pstmt.close();
			if (updateRow < 1) {
				throw new LMSException("rows not Updated");
			}

			con.commit();

		} catch (SQLException e) {
			e.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			flag = false;
		} catch (LMSException e) {
			e.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			flag = false;
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return flag;
	}
	
	
	public boolean updateGovCommRateVS(Integer id, Double govtCommRate) {
		boolean flag = true;
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		try {
			con.setAutoCommit(false);
			pstmt = con.prepareStatement("update st_vs_game_master set gov_comm_rate=?  where game_id =?");
			pstmt.setDouble(1, govtCommRate);
			pstmt.setInt(2, id);
			int updateRow = pstmt.executeUpdate();
			logger.debug("update row " + updateRow + "\n st_vs_game_master query == " + pstmt);
			pstmt.close();
			if (updateRow < 1) {
				throw new LMSException("rows not Updated");
			}
			// putting the entry into gov_comm_rate_history table
			pstmt = con.prepareStatement("insert into st_vs_game_govt_comm_history values (?, ?, ?)");
			pstmt.setInt(1, id);
			pstmt.setDouble(2, govtCommRate);
			pstmt.setTimestamp(3, new Timestamp(new java.util.Date().getTime()));
			updateRow = pstmt.executeUpdate();
			logger.debug("update row " + updateRow + "\n st_vs_game_govt_comm_history query == " + pstmt);
			pstmt.close();
			if (updateRow < 1) {
				throw new LMSException("rows not Updated");
			}
			con.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			flag = false;
		} catch (LMSException e) {
			e.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			flag = false;
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return flag;
	}

}
