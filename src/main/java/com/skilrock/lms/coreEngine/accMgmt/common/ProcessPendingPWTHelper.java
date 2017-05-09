/*
 * � copyright 2007, SkilRock Technologies, A division of Sugal & Damani Lottery Agency Pvt. Ltd.
 * All Rights Reserved
 * The contents of this file are the property of Sugal & Damani Lottery Agency Pvt. Ltd.
 * and are subject to a License agreement with Sugal & Damani Lottery Agency Pvt. Ltd.; you may
 * not use this file except in compliance with that License.  You may obtain a
 * copy of that license from:
 * Legal Department
 * Sugal & Damani Lottery Agency Pvt. Ltd.
 * 6/35,WEA, Karol Bagh,
 * New Delhi
 * India - 110005
 * This software is distributed under the License and is provided on an �AS IS�
 * basis, without warranty of any kind, either express or implied, unless
 * otherwise provided in the License.  See the License for governing rights and
 * limitations under the License.
 */

package com.skilrock.lms.coreEngine.accMgmt.common;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.GameContants;
import com.skilrock.lms.beans.PlayerPWTBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.db.TableConstants;
import com.skilrock.lms.common.exception.LMSException;

/**
 * This is a helper class having methods Pending Direct Player PWTs
 * 
 * @author Skilrock Technologies
 * 
 */
public class ProcessPendingPWTHelper {
	static Log logger = LogFactory.getLog(ProcessPendingPWTHelper.class);

	private java.sql.Date getDate(String date) {

		logger.debug("Passed date::" + date);
		String format = "yyyy-MM-dd";
		DateFormat dateFormat = new SimpleDateFormat(format);
		try {

			Date parsedDate = dateFormat.parse(date);
			logger.debug("Parsed date::" + parsedDate);
			logger.debug(new java.sql.Date(parsedDate.getTime()));
			return new java.sql.Date(parsedDate.getTime());

		} catch (ParseException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
		}
		return null;
	}

	private String getWhereClause(Map searchMap, String statusForSearch) {
		Set keySet = null;
		StringBuffer whereClause = new StringBuffer();

		if (searchMap != null) {
			keySet = searchMap.keySet();

			Iterator itr = keySet.iterator();
			String key = null;
			String strValue;

			int fieldAdded = 1;

			while (itr.hasNext()) {
				key = (String) itr.next();
				if (key.equals(GameContants.PLAYER_FIRST_NAME)) {
					strValue = (String) searchMap.get(key);

					if (strValue != null && !strValue.equals("")) {
						if (fieldAdded > 0) {
							whereClause.append(" And ");
						}

						whereClause.append(TableConstants.PLR_FIRSTNAME);
						whereClause.append(" like '");
						whereClause.append(strValue.trim());
						whereClause.append("%' ");

						fieldAdded++;
					}
				}

				else if (key.equals(GameContants.GAME_NAME)) {
					strValue = (String) searchMap.get(key);

					if (strValue != null && !strValue.equals("")) {
						if (fieldAdded > 0) {
							whereClause.append(" And ");
						}

						whereClause.append(TableConstants.SGM_GAME_NAME);
						whereClause.append(" like '");
						whereClause.append(strValue.trim());
						whereClause.append("%' ");

						fieldAdded++;
					}
				}

				else if (key.equals(GameContants.GAME_NBR)) {

					strValue = (String) searchMap.get(key);
					if (strValue != null && !strValue.equals("")) {

						if (fieldAdded > 0) {
							whereClause.append(" And ");
						}

						whereClause.append(TableConstants.SGM_GAME_NBR);
						whereClause.append(" = '");
						whereClause.append(strValue.trim());
						whereClause.append("'");
						fieldAdded++;

					}
				} else if (key.equals(TableConstants.PWT_STATUS)) {

					strValue = (String) searchMap.get(key);
					logger.debug(strValue + "...............Status");
					if (strValue != null && !strValue.equals("")) {

						if (fieldAdded > 0) {
							whereClause.append(" And ");
						}

						whereClause.append(TableConstants.PWT_STATUS);
						whereClause.append(" = '");
						whereClause.append(strValue.trim());
						whereClause.append("'");

						fieldAdded++;

					}
				}

				else if (key.equals(TableConstants.TRANC_DATE)) {

					strValue = (String) searchMap.get(key);
					if (strValue != null && !strValue.equals("")) {

						if (fieldAdded > 0) {
							whereClause.append(" And ");
						}
						if (statusForSearch.equals("CLAIM_PLR")) {
							whereClause
									.append("d." + TableConstants.TRANC_DATE);
							whereClause.append(" between '");
							whereClause.append(getDate(strValue.trim()));
							whereClause.append("' and '");
							whereClause.append(getDate(strValue.trim())
									+ " 23:59:59");
							whereClause.append("' ");
						} else {
							whereClause.append(TableConstants.TRANC_DATE);
							whereClause.append(" between '");
							whereClause.append(getDate(strValue.trim()));
							whereClause.append("' and '");
							whereClause.append(getDate(strValue.trim())
									+ " 23:59:59");
							whereClause.append("' ");
						}
						fieldAdded++;
					}
				}

			}
			if (fieldAdded == 1) {
				whereClause.append("and 1=1");
			}

		}

		return whereClause.toString();
	}

	/**
	 * This method returns the list of games based on the search parameters
	 * passed
	 * 
	 * @param searchMap
	 * @return List
	 * @throws LMSException
	 */
	public List SearchPendingPWT(Map searchMap) throws LMSException {

		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {

			PlayerPWTBean playerPWTBean = null;
			List<PlayerPWTBean> searchResults = new ArrayList<PlayerPWTBean>();

			 
			connection = DBConnect.getConnection();
			statement = connection.createStatement();

			Set keySet = null;
			keySet = searchMap.keySet();
			Iterator itr = keySet.iterator();
			String statusForSearch = null;

			while (itr.hasNext()) {
				String key = (String) itr.next();
				if (key.equals(TableConstants.PWT_STATUS)) {
					statusForSearch = (String) searchMap.get(key);
				}
			}
			logger.debug("status for search  " + statusForSearch);

			String dynamicWhereClause = getWhereClause(searchMap,
					statusForSearch);
			String query = null;
			if (statusForSearch.equals("CLAIM_PLR")) {

				// query="select a.pwt_receipt_id,a.player_id
				// ,a.game_id,a.transaction_date,a.virn_code,a.pwt_amt,a.tax_amt,a.net_amt,a.status,a.ticket_nbr,b.first_name,b.last_name,c.game_name,c.game_nbr,d.cheque_nbr,d.cheque_date,d.drawee_bank,d.issuing_party_name
				// from st_se_direct_player_pwt_temp_receipt a
				// ,st_lms_player_master b,st_se_game_master
				// c,st_se_direct_player_pwt d where a.game_id=c.game_id and
				// a.player_id=b.player_id and a.virn_code=d.virn_code and
				// a.game_id=d.game_id";
				query = "select a.pwt_receipt_id,a.player_id ,a.game_id,a.transaction_date,a.virn_code,a.pwt_amt,a.tax_amt,a.net_amt,a.status,a.ticket_nbr,b.first_name,b.last_name,c.game_name,c.game_nbr,d.cheque_nbr,d.cheque_date,d.drawee_bank,d.issuing_party_name from st_se_direct_player_pwt_temp_receipt a ,st_lms_player_master b,st_se_game_master c,st_se_direct_player_pwt d where a.game_id=c.game_id and a.player_id=b.player_id and a.virn_code=d.virn_code and a.game_id=d.game_id "
						+ dynamicWhereClause;
				;
				logger.debug("in case of claim ed to player :: " + query);
			} else {
				query = QueryManager
						.getST5SelectDirectPlrTempTransactionQuery()
						+ dynamicWhereClause;
			}

			logger.debug("-----Query----::" + query);

			resultSet = statement.executeQuery(query);

			while (resultSet.next()) {

				playerPWTBean = new PlayerPWTBean();
				playerPWTBean.setGameName(resultSet
						.getString(TableConstants.SGM_GAME_NAME));

				playerPWTBean.setGameNbr(resultSet
						.getInt(TableConstants.GAME_NBR));

				playerPWTBean.setGameId(resultSet
						.getInt(TableConstants.SGM_GAME_ID));
				playerPWTBean.setPlayerFirstName(resultSet
						.getString(TableConstants.PLR_FIRSTNAME));
				playerPWTBean.setPlayerLastName(resultSet
						.getString(TableConstants.PLR_LASTNAME));

				playerPWTBean.setPlayerId(resultSet
						.getInt(TableConstants.PLR_ID));
				playerPWTBean.setTrancDate(getDate(
						resultSet.getString(TableConstants.TRANC_DATE))
						.toString());
				playerPWTBean.setPwtAmt(resultSet
						.getDouble(TableConstants.SPI_PWT_AMT));
				playerPWTBean.setTax(resultSet
						.getDouble(TableConstants.TAX_AMOUNT));
				playerPWTBean.setNetAmt(resultSet
						.getDouble(TableConstants.SPI_NET_AMT));
				if (resultSet.getString(TableConstants.STATUS)
						.equalsIgnoreCase("PND_PWT")) {
					playerPWTBean.setStatus("Pending PWT");

				}
				if (resultSet.getString(TableConstants.STATUS)
						.equalsIgnoreCase("CLAIM_PLR")) {
					playerPWTBean.setStatus("Claimed PWT");

					playerPWTBean.setChequeDate(resultSet
							.getString("cheque_date"));
					playerPWTBean.setChequeNbr(resultSet
							.getString("cheque_nbr"));
					playerPWTBean.setDraweeBank(resultSet
							.getString("drawee_bank"));
					playerPWTBean.setTicketNbr(resultSet
							.getString("ticket_nbr"));

				}
				if (resultSet.getString(TableConstants.STATUS)
						.equalsIgnoreCase("CANCEL")) {
					playerPWTBean.setStatus("Cancelled PWT");
					playerPWTBean.setChequeDate("NA");
					playerPWTBean.setChequeNbr("NA");
					playerPWTBean.setDraweeBank("NA");
				}

				playerPWTBean.setTransactionId(resultSet
						.getInt(TableConstants.PWT_RECEIPT_ID));
				playerPWTBean.setVirnCode(resultSet
						.getString(TableConstants.SPI_VIRN_CODE));
				playerPWTBean.setTicketNbr(resultSet.getString("ticket_nbr"));

				searchResults.add(playerPWTBean);

			}

			return searchResults;

		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException(e);

		} finally {

			try {

				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException se) {
				logger.error("Exception: " + se);
				se.printStackTrace();
			}
		}

		// return null;

	}

}
