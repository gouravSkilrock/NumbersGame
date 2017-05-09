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

package com.skilrock.lms.coreEngine.scratchService.orderMgmt.common;

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

import com.skilrock.lms.GameContants;
import com.skilrock.lms.beans.GameBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.db.TableConstants;
import com.skilrock.lms.common.exception.LMSException;

/**
 * This is a helper class providing methods for handling Searcg Game
 * functionality
 * 
 * @author Skilrock Technologies
 * 
 */
public class SearchGameHelper {

	private java.sql.Date getDate(String date) {

		System.out.println("Passed date::" + date);
		String format = "yyyy-MM-dd";
		DateFormat dateFormat = new SimpleDateFormat(format);
		try {

			Date parsedDate = dateFormat.parse(date);
			System.out.println("Parsed date::" + parsedDate);
			System.out.println(new java.sql.Date(parsedDate.getTime()));
			return new java.sql.Date(parsedDate.getTime());

		} catch (ParseException e) {

			e.printStackTrace();
		}
		return null;
	}

	public int getOrgIdByOrgName(String agtOrgName) {
		int orgId = -1;
		 
		Connection con = DBConnect.getConnection();
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt
					.executeQuery("select organization_id from st_lms_organization_master where name='"
							+ agtOrgName + "'");
			while (rs.next()) {
				orgId = rs.getInt("organization_id");
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

		return orgId;
	}

	private String getWhereClause(Map searchMap) {
		Set keySet = null;
		StringBuffer whereClause = new StringBuffer();

		if (searchMap != null) {
			keySet = searchMap.keySet();

			Iterator itr = keySet.iterator();
			String key = null;
			String strValue;

			int fieldAdded = 0;

			whereClause.append(" where ");

			while (itr.hasNext()) {
				key = (String) itr.next();

				if (key.equals(GameContants.GAME_NAME)) {
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
						whereClause.append(" = ");
						whereClause.append(strValue.trim());

						fieldAdded++;

					}
				} else if (key.equals(GameContants.FROM_PRICE)) {

					strValue = (String) searchMap.get(key);
					if (strValue != null && !strValue.equals("")) {

						if (fieldAdded > 0) {
							whereClause.append(" And ");
						}

						whereClause.append(TableConstants.SGM_TICKET_PRICE);
						whereClause.append(" >= ");
						whereClause.append(strValue.trim());

						fieldAdded++;

					}
				}

				else if (key.equals(GameContants.TO_PRICE)) {

					strValue = (String) searchMap.get(key);
					if (strValue != null && !strValue.equals("")) {

						if (fieldAdded > 0) {
							whereClause.append(" And ");
						}

						whereClause.append(TableConstants.SGM_TICKET_PRICE);
						whereClause.append(" <= ");
						whereClause.append(strValue.trim());

						fieldAdded++;

					}
				}

				else if (key.equals(GameContants.FROM_DATE)) {

					strValue = (String) searchMap.get(key);
					if (strValue != null && !strValue.equals("")) {

						if (fieldAdded > 0) {
							whereClause.append(" And ");
						}

						whereClause.append(TableConstants.SGM_START_DATE);
						whereClause.append(" >='");
						whereClause.append(getDate(strValue.trim()));
						whereClause.append("' ");

						fieldAdded++;
					}
				} else if (key.equals(GameContants.TO_DATE)) {

					strValue = (String) searchMap.get(key);
					if (strValue != null && !strValue.equals("")) {

						if (fieldAdded > 0) {
							whereClause.append(" And ");
						}

						whereClause.append(TableConstants.SGM_START_DATE);
						whereClause.append(" <='");
						whereClause.append(getDate(strValue.trim()));
						whereClause.append("' ");

						fieldAdded++;
					}
				}

				else if (key.equals(GameContants.ST1_END_FROM_DATE)) {

					strValue = (String) searchMap.get(key);
					if (strValue != null && !strValue.equals("")) {

						if (fieldAdded > 0) {
							whereClause.append(" And ");
						}

						whereClause.append(TableConstants.SGM_SALE_END_DATE);
						whereClause.append(" >='");
						whereClause.append(getDate(strValue.trim()));
						whereClause.append("' ");

						fieldAdded++;
					}
				}

				else if (key.equals(GameContants.ST1_END_TO_DATE)) {

					strValue = (String) searchMap.get(key);
					if (strValue != null && !strValue.equals("")) {

						if (fieldAdded > 0) {
							whereClause.append(" And ");
						}

						whereClause.append(TableConstants.SGM_SALE_END_DATE);
						whereClause.append(" <='");
						whereClause.append(getDate(strValue.trim()));
						whereClause.append("' ");

						fieldAdded++;
					}
				}

				else if (key.equals(GameContants.PRICE_CONDITION)) {

					strValue = (String) searchMap.get(key);
					if (strValue != null && !strValue.equals("")) {

						if (fieldAdded > 0) {
							whereClause.append(" And ");
						}

						whereClause.append(TableConstants.SGM_TICKET_PRICE);
						whereClause.append(strValue.trim());
						fieldAdded++;
					}
				}

			}
			if (fieldAdded == 0) {
				whereClause.append(" 1=1");
			}

		}

		return whereClause.toString();
	}

	/**
	 * This method returns the list of games based on the passed search
	 * parameters and passed agent
	 * 
	 * @param searchMap
	 * @param agtOrgId
	 * @return List
	 * @throws LMSException
	 */
	public List searchAgentGame(Map searchMap, int agtOrgId)
			throws LMSException {

		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;

		String agentGameQuery = null;

		try {

			GameBean gameBean = null;
			List<GameBean> searchResults = new ArrayList<GameBean>();

			 
			connection = DBConnect.getConnection();
			statement = connection.createStatement();

			agentGameQuery = QueryManager.getST1GameSearchAgentQuery();

			String dynamicWhereClause = getWhereClause(searchMap);
			String query = QueryManager.getST1GameSearchQuery()
					+ dynamicWhereClause + " and game_id in(" + agentGameQuery
					+ agtOrgId + ")"
					+ " and game_status='OPEN' order by game_nbr";

			System.out.println("-----Query----::" + query);

			resultSet = statement.executeQuery(query);

			while (resultSet.next()) {

				gameBean = new GameBean();
				gameBean
						.setGameId(resultSet.getInt(TableConstants.SGM_GAME_ID));
				gameBean.setGameName(resultSet
						.getString(TableConstants.SGM_GAME_NAME));
				gameBean.setGameNbr(resultSet
						.getInt(TableConstants.SGM_GAME_NBR));
				gameBean.setTicketPrice(resultSet
						.getDouble(TableConstants.SGM_TICKET_PRICE));
				gameBean.setStartDate(resultSet
						.getDate(TableConstants.SGM_START_DATE));
				gameBean.setSaleEndDate(resultSet
						.getDate(TableConstants.SGM_SALE_END_DATE));
				gameBean.setTicketsPerBook(resultSet
						.getInt(TableConstants.SGM_NBR_OF_TICKETS_PER_BOOK));
				gameBean.setRetSaleRate(resultSet
						.getInt(TableConstants.SGM_RET_SALE_RATE));
				searchResults.add(gameBean);

			}

			return searchResults;

		} catch (SQLException e) {

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
				se.printStackTrace();
			}
		}

		// return null;

	}

	/**
	 * This method returns the list of games based on the search parameters
	 * passed
	 * 
	 * @param searchMap
	 * @return List
	 * @throws LMSException
	 */
	public List searchGame(Map searchMap, int agtOrgId) throws LMSException {

		Connection connection = null;
		Statement statement = null;
		Statement statement1 = null;
		ResultSet resultSet = null;
		ResultSet resultSet1 = null;
		try {

			GameBean gameBean = null;
			List<GameBean> searchResults = new ArrayList<GameBean>();

			 
			connection = DBConnect.getConnection();
			statement = connection.createStatement();
			statement1 = connection.createStatement();

			String dynamicWhereClause = getWhereClause(searchMap);
			String query = QueryManager.getST1GameSearchQuery()
					+ dynamicWhereClause
					+ " and game_status='OPEN' order by game_nbr";

			System.out.println("-----Query----::" + query);

			resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				int game_id = resultSet.getInt(TableConstants.SGM_GAME_ID);
				double agtCommvariance = 0.0;
				gameBean = new GameBean();
				gameBean
						.setGameId(resultSet.getInt(TableConstants.SGM_GAME_ID));

				// get agt comm variance for each game
				resultSet1 = statement1
						.executeQuery("select sale_comm_variance from st_se_bo_agent_sale_pwt_comm_variance where agent_org_id="
								+ agtOrgId + " and game_id=" + game_id);
				while (resultSet1.next()) {
					agtCommvariance = resultSet1
							.getDouble("sale_comm_variance");
				}
				gameBean.setGameName(resultSet
						.getString(TableConstants.SGM_GAME_NAME));
				gameBean.setGameNbr(resultSet
						.getInt(TableConstants.SGM_GAME_NBR));
				gameBean.setTicketPrice(resultSet
						.getDouble(TableConstants.SGM_TICKET_PRICE));
				gameBean.setStartDate(resultSet
						.getDate(TableConstants.SGM_START_DATE));
				gameBean.setSaleEndDate(resultSet
						.getDate(TableConstants.SGM_SALE_END_DATE));
				gameBean.setTicketsPerBook(resultSet
						.getInt(TableConstants.SGM_NBR_OF_TICKETS_PER_BOOK));
				gameBean.setAgentSaleRate(resultSet
						.getDouble(TableConstants.SGM_AGT_SALE_RATE)
						+ agtCommvariance);
				searchResults.add(gameBean);

			}

			return searchResults;

		} catch (SQLException e) {

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
				se.printStackTrace();
			}
		}

		// return null;

	}

}
