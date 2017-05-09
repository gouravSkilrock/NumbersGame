package com.skilrock.lms.coreEngine.drawGames.pwtMgmt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.PlayerBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSException;

public class PlayerVerifyHelperForApp {
	static Log logger = LogFactory.getLog(PlayerVerifyHelperForApp.class);

	private Connection connection;
	private PreparedStatement prepareState;
	private ResultSet resultSet;

	public int registerPlayer(PlayerBean plrBean, Connection connection)
			throws LMSException {

		int playerId = 0;
		try {

			String query = "insert into st_lms_player_master (first_name, last_name,email_id, phone_nbr, addr_line1,"
					+ "addr_line2, city,state_code, country_code, pin_code, photo_id_type, photo_id_nbr, bank_name, "
					+ " bank_branch, location_city, bank_acc_nbr) "
					+ "select ?,?,?,?,?,?,?,state_code,e.country_code,?,?,?,?,?,?,? from st_lms_country_master d, st_lms_state_master e "
					+ "where  e.country_code=d.country_code and e.name=? and d.name = ?";

			prepareState = connection.prepareStatement(query);
			prepareState.setString(1, plrBean.getFirstName());
			prepareState.setString(2, plrBean.getLastName());
			prepareState.setString(3, plrBean.getEmailId());
			prepareState.setString(4, plrBean.getPhone());
			prepareState.setString(5, plrBean.getPlrAddr1());
			prepareState.setString(6, plrBean.getPlrAddr2());
			prepareState.setString(7, plrBean.getPlrCity());
			prepareState.setLong(8, plrBean.getPlrPin());
			prepareState.setString(9, plrBean.getIdType());
			prepareState.setString(10, plrBean.getIdNumber());

			// by Arun new Added fields in st_plr_detail table
			prepareState.setString(11, plrBean.getBankName());
			prepareState.setString(12, plrBean.getBankBranch());
			prepareState.setString(13, plrBean.getLocationCity());
			prepareState.setString(14, plrBean.getBankAccNbr());

			prepareState.setString(15, plrBean.getPlrState());
			prepareState.setString(16, plrBean.getPlrCountry());
			prepareState.executeUpdate();
			logger.debug("player master insert == " + prepareState);

			ResultSet rs = prepareState.getGeneratedKeys();
			if (rs.next()) {
				playerId = rs.getInt(1);
				logger.debug("Player == " + plrBean.getFirstName()
						+ " registered successfully And id is == " + playerId);
			} else {
				throw new LMSException("player data not inserted ");
			}

			logger
					.debug("SucessFully Inserted into st_lms_player_master Table");
			return playerId;

		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException(e);
		}

	}

	public Map<String, Object> searchPlayer(String firstName, String lastName,
			String idNumber, String idType) throws LMSException {
		connection = DBConnect.getConnection();
		Map<String, Object> map = new TreeMap<String, Object>();
		try {
			String query = QueryManager.getST5PlayerDetailQuery();
			prepareState = connection.prepareStatement(query);
			prepareState.setString(1, firstName);
			prepareState.setString(2, lastName);
			prepareState.setString(3, idType);
			prepareState.setString(4, idNumber);
			resultSet = prepareState.executeQuery();
			logger.debug("fetch player details::" + prepareState);
			PlayerBean plrBean = null;
			if (resultSet.next()) {
				logger.debug("State   " + resultSet.getString("state"));
				plrBean = new PlayerBean();
				plrBean.setPlrId(resultSet.getInt("player_id"));
				plrBean.setFirstName(firstName);
				plrBean.setLastName(lastName);
				plrBean.setIdType(idType);
				plrBean.setIdNumber(idNumber);
				plrBean.setEmailId(resultSet.getString("email_id"));
				plrBean.setPhone(resultSet.getString("phone_nbr"));
				plrBean.setPlrAddr1(resultSet.getString("addr_line1"));
				plrBean.setPlrAddr2(resultSet.getString("addr_line2"));
				plrBean.setPlrCity(resultSet.getString("city"));
				plrBean.setPlrState(resultSet.getString("state"));
				plrBean.setPlrCountry(resultSet.getString("country"));
				plrBean.setPlrPin(resultSet.getLong("pin_code"));

				// bank details
				plrBean.setBankAccNbr(resultSet.getString("bank_acc_nbr"));
				plrBean.setBankName(resultSet.getString("bank_name"));
				plrBean.setBankBranch(resultSet.getString("bank_branch"));
				plrBean.setLocationCity(resultSet.getString("location_city"));

				map.put("plrBean", plrBean);
			} else {
				query = QueryManager.getST5Country();
				logger.debug("query === " + query);
				prepareState = connection.prepareStatement(query);
				ResultSet rs = prepareState.executeQuery();
				ArrayList<String> list = new ArrayList<String>();
				while (rs.next()) {
					list.add(rs.getString("name"));
				}
				map.put("countryList", list);
			}
			return map;
		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					logger.error("Exception: " + e);
					e.printStackTrace();
					throw new LMSException(e);
				}
			}
		}

	}

	public int verifyPlayer(String firstName, String lastName, String idNumber,
			String idType) throws LMSException {
		connection = DBConnect.getConnection();
		Map<String, Object> map = new TreeMap<String, Object>();
		try {
			String query = QueryManager.getST5PlayerDetailQuery();
			prepareState = connection.prepareStatement(query);
			prepareState.setString(1, firstName);
			prepareState.setString(2, lastName);
			prepareState.setString(3, idType);
			prepareState.setString(4, idNumber);
			resultSet = prepareState.executeQuery();
			logger.debug("fetch player details::" + prepareState);
			if (resultSet.next()) {
				return resultSet.getInt("player_id");
			} else {
				return 0;
			}
		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					logger.error("Exception: " + e);
					e.printStackTrace();
					throw new LMSException(e);
				}
			}
		}

	}

}
