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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.GameBean;
import com.skilrock.lms.beans.OrgBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.db.TableConstants;
import com.skilrock.lms.common.exception.LMSException;

/**
 * This is a helper class providing methods for creating agent initiated orders -
 * self and for retailer
 * 
 * @author Skilrock Technologies
 * 
 */
public class AgentOrderProcessHelper {
	Log logger = LogFactory.getLog(AgentOrderProcessHelper.class);

	/**
	 * This method is used for generating order for a retailer. Returns true if
	 * the order is successfully generated
	 * 
	 * @param userId
	 * @param cartList
	 * @param retOrgList
	 * @param retOrgName
	 * @return boolean
	 * @throws LMSException
	 */
	public int generateOrder(int userId, List<GameBean> cartList,
			List<OrgBean> retOrgList, String retOrgName, int userOrgId)
			throws LMSException {

		int retOrgId = -1;
		int retailerId = -1;
		if (retOrgList != null) {
			OrgBean bean = null;
			for (int i = 0; i < retOrgList.size(); i++) {
				bean = retOrgList.get(i);
				logger.debug("---OrG Name::" + bean.getOrgName());
				if (retOrgName.equals(bean.getOrgName())) {
					retOrgId = bean.getOrgId();
					retailerId = bean.getUserId();

					logger.debug("RetOrgId::" + retOrgId);
					logger.debug("retailerId::" + retailerId);
					break;
				}
			}
		}

		logger.debug("RetOrgId::" + retOrgId);
		logger.debug("retailerId::" + retailerId);

		Connection connection = null;
		PreparedStatement orderPstmt = null;
		PreparedStatement gamePstmt = null;

		ResultSet resultSet = null;
		int orderId = -1;

		if (cartList != null) {
			int size = cartList.size();
			// QueryManager queryManager = null;
			GameBean gameBean = null;

			String orderQuery = null;
			String gameQuery = null;

			if (size > 0) {
				try {

					// create database connection

					 
					connection = DBConnect.getConnection();
					connection.setAutoCommit(false);

					// get order query

					orderQuery = QueryManager.getST1InsertAgtOrderQuery();
					orderPstmt = connection.prepareStatement(orderQuery);

					// get ordered game query
					gameQuery = QueryManager.getST1InsertAgtOrderedGamesQuery();
					gamePstmt = connection.prepareStatement(gameQuery);

					// set parameters for insert into order table

					orderPstmt.setInt(1, userId);
					orderPstmt.setInt(2, retailerId);
					orderPstmt.setInt(3, retOrgId);
					orderPstmt.setDate(4, new java.sql.Date(new Date()
							.getTime()));

					orderPstmt.setString(5, "APPROVED");
					orderPstmt.setString(6, "Y");
					orderPstmt.setInt(7, userOrgId);

					orderPstmt.execute();
					resultSet = orderPstmt.getGeneratedKeys();

					while (resultSet.next()) {
						orderId = resultSet.getInt(1);
					}

					logger.debug("OrderId::" + orderId);

					// set parameters for insert into ordered games table

					for (int i = 0; i < size; i++) {
						gameBean = cartList.get(i);

						logger.debug("1:" + gameBean.getGameId());
						logger.debug("2:" + gameBean.getOrderedQty());

						gamePstmt.setInt(1, orderId);
						gamePstmt.setInt(2, gameBean.getGameId());
						gamePstmt.setInt(3, gameBean.getOrderedQty());
						gamePstmt.setInt(4, gameBean.getOrderedQty());
						gamePstmt.execute();

					}

					// commit the connection
					connection.commit();
					return orderId;

				} catch (SQLException e) {

					try {
						connection.rollback();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					e.printStackTrace();
					throw new LMSException(e);

				} finally {

					try {
						if (orderPstmt != null) {
							orderPstmt.close();
						}
						if (gamePstmt != null) {
							gamePstmt.close();
						}
						if (connection != null) {
							connection.close();
						}
					} catch (SQLException se) {
						se.printStackTrace();
					}
				}

			}
		}
		return orderId;
	}

	/**
	 * This method returns a list of retailers for the passed agent
	 * 
	 * @param agtOrgId
	 * @return List
	 * @throws LMSException
	 */
	public List<OrgBean> getRetailers(int agtOrgId) throws LMSException {

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		String delimiter = new String("-");

		try {

			OrgBean retailerOrgBean = null;
			List<OrgBean> searchResults = new ArrayList<OrgBean>();

			// create database connection

			 
			connection = DBConnect.getConnection();

			// get fetch retailer query

			String query = QueryManager.getST1RetOrgQuery();// + " order by
			// name";
			statement = connection.prepareStatement(query);
			statement.setInt(1, agtOrgId);
			resultSet = statement.executeQuery();

			while (resultSet.next()) {

				retailerOrgBean = new OrgBean();
				retailerOrgBean.setOrgId(resultSet
						.getInt(TableConstants.SOM_ORG_ID));
				retailerOrgBean.setOrgName(resultSet
						.getString(TableConstants.SOM_ORG_NAME));
				retailerOrgBean.setUserId(resultSet
						.getInt(TableConstants.SUM_USER_ID));

				searchResults.add(retailerOrgBean);

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

	/*
	 * public int getRetailerOrgId(List<OrgBean> retOrgList, String retOrgName) {
	 * 
	 * 
	 * if(retOrgList != null){ OrgBean bean = null; for(int i=0; i<retOrgList.size();
	 * i++ ){ bean = retOrgList.get(i);
	 * if("retOrgName".equals(bean.getOrgName())){ return bean.getOrgId(); } } }
	 * return -1; }
	 */

}
