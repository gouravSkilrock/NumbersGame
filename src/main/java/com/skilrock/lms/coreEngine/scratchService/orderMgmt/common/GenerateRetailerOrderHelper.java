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

import com.skilrock.lms.beans.GameBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;

/**
 * This is a helper class providing methods for generating order for agent
 * 
 * @author Skilrock Technologies
 * 
 */
public class GenerateRetailerOrderHelper {

	public List<GameBean> createCartOfOrder(String[] gameName,
			String[] noOfBooks) {

		// creating cart for order request
		List<GameBean> gameCart = new ArrayList<GameBean>();
		GameBean cartBean = null;

		for (int j = 0; j < noOfBooks.length; j++) {
			if (!"-1".equals(gameName[j].trim())
					&& !("".equals(noOfBooks[j].trim()) || Integer
							.parseInt(noOfBooks[j].trim()) <= 0)) {
				cartBean = new GameBean();
				cartBean.setGameId(Integer.parseInt(gameName[j]));
				cartBean.setOrderedQty(Integer.parseInt(noOfBooks[j]));
				System.out.println("gameName[" + j + "] = " + gameName[j]
						+ "  ,  noOfBooks[" + j + "] = " + noOfBooks[j]);
				gameCart.add(cartBean);
			}
		}
		System.out.println("size of gameCart = " + gameCart.size());
		if (gameCart.isEmpty()) {
			return null;
		} else {
			return gameCart;
		}
	}

	/**
	 * This method generates order for the agent. Returns true if successfully
	 * generated
	 * 
	 * @param agtId
	 * @param agtOrgId
	 * @param cartList
	 * @return boolean
	 * @throws LMSException
	 */
	public int generateOrder(int agtOrgId, int retId, int retOrgId,
			List cartList) throws LMSException {

		Connection connection = null;
		PreparedStatement orderPstmt = null;
		PreparedStatement gamePstmt = null;
		PreparedStatement agtUserIdPstmt = null;

		ResultSet resultSet = null;
		int orderId = -1;

		if (cartList != null) {
			int size = cartList.size();
			GameBean gameBean = null;
			String orderQuery = null;
			String gameQuery = null;
			String agtUserIdQuery = null;

			if (size > 0) {
				try {
					 
					connection = DBConnect.getConnection();
					connection.setAutoCommit(false);

					orderQuery = QueryManager.getST1InsertAgtOrderQuery();
					orderPstmt = connection.prepareStatement(orderQuery);

					gameQuery = QueryManager
							.getST1AutoInsertAgtOrderedGamesQuery();
					gamePstmt = connection.prepareStatement(gameQuery);

					agtUserIdQuery = "select user_id from st_lms_user_master where organization_id=? and isrolehead=?";
					agtUserIdPstmt = connection
							.prepareStatement(agtUserIdQuery);

					agtUserIdPstmt.setInt(1, agtOrgId);
					agtUserIdPstmt.setString(2, "Y");
					ResultSet agtIdRs = agtUserIdPstmt.executeQuery();
					int agtId = 0;
					if (agtIdRs.next()) {
						agtId = agtIdRs.getInt("user_id");
					} else {
						throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
					}

					orderPstmt.setInt(1, agtId); // agent's user Id
					orderPstmt.setInt(2, retId);
					orderPstmt.setInt(3, retOrgId);
					orderPstmt.setDate(4, new java.sql.Date(new Date()
							.getTime()));
					orderPstmt.setString(5, "REQUESTED");
					orderPstmt.setString(6, "N");
					orderPstmt.setInt(7, agtOrgId);

					orderPstmt.execute();
					resultSet = orderPstmt.getGeneratedKeys();

					while (resultSet.next()) {
						orderId = resultSet.getInt(1);
					}

					System.out.println("OrderId::" + orderId);

					for (int i = 0; i < size; i++) {
						gameBean = (GameBean) cartList.get(i);

						gamePstmt.setInt(1, orderId);
						gamePstmt.setInt(2, gameBean.getGameId());
						gamePstmt.setInt(3, gameBean.getOrderedQty());
						gamePstmt.execute();

					}

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
					throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
				} catch (Exception e) {
					try {
						connection.rollback();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					e.printStackTrace();
					throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
				}finally {
					DBConnect.closePstmt(orderPstmt);
					DBConnect.closeConnection(connection, gamePstmt);
				}

			}
		}
		return orderId;
	}

}
