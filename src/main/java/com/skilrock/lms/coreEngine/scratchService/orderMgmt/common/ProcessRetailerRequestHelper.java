package com.skilrock.lms.coreEngine.scratchService.orderMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.skilrock.lms.beans.OrderRequestBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSException;

/**
 * 
 * This class used to process UnApproved Requests by the BO Admin
 * 
 * @author Skilrock Technologies
 * 
 */
public class ProcessRetailerRequestHelper {
	public List<OrderRequestBean> getRequestedOrders(int agtOrgId, String gameName, String gameNumber, String agtOrgName, String orderNumber)
			throws Exception {

		List<OrderRequestBean> list;
		list = new ArrayList<OrderRequestBean>();
		OrderRequestBean orderBean;

		 
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		try {
			conn = DBConnect.getConnection();
			StringBuilder queryBuilder = new StringBuilder("SELECT distinct(a.order_id), a.order_date, b.name FROM st_se_agent_order a INNER JOIN st_lms_organization_master b ON a.retailer_org_id=b.organization_id INNER JOIN st_se_agent_ordered_games c ON a.order_id=c.order_id INNER JOIN st_se_game_master d ON c.game_id=d.game_id WHERE a.order_status='REQUESTED' AND a.agent_org_id=?");
			if(gameName!=null && gameName.length()>0)
				queryBuilder.append(" AND game_name LIKE '%").append(gameName).append("%'");
			if(gameNumber!=null && gameNumber.length()>0)
				queryBuilder.append(" AND game_nbr LIKE '%").append(gameNumber).append("%'");
			if(agtOrgName!=null && agtOrgName.length()>0)
				queryBuilder.append(" AND name LIKE '%").append(agtOrgName).append("%'");
			if(orderNumber!=null && orderNumber.length()>0)
				queryBuilder.append(" AND order_status LIKE '%").append(orderNumber).append("%'");
			pstmt = conn.prepareStatement(queryBuilder.toString());
			//pstmt = conn.prepareStatement(QueryManager.getST5AgtRequestListQuery());
			pstmt.setInt(1, agtOrgId);
			resultSet = pstmt.executeQuery();
			while (resultSet.next()) {
				orderBean = new OrderRequestBean();
				orderBean.setOrderId(resultSet.getInt("order_id"));
				orderBean.setDate(resultSet.getDate("order_date"));
				orderBean.setName(resultSet.getString("name"));
				list.add(orderBean);
			}
			return list;

		} catch (SQLException se) {
			throw new LMSException(se);

		} finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException se) {
				throw new LMSException(se);
			}

		}

	}
	
	public List<OrderRequestBean> getRetailerRequestedOrders(int retOrgId, String gameName, String gameNumber, String orderNumber, String orderStatus, String startDate, String endDate) throws Exception {
		List<OrderRequestBean> list;
		list = new ArrayList<OrderRequestBean>();
		OrderRequestBean orderBean;

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;
		try {
			conn = DBConnect.getConnection();
			StringBuilder queryBuilder = new StringBuilder("SELECT distinct(a.order_id), a.order_status, a.order_date, d.game_name, c.nbr_of_books_req, c.nbr_of_books_appr,c.nbr_of_books_dlvrd FROM st_se_agent_order a INNER JOIN st_lms_organization_master b ON a.retailer_org_id=b.organization_id INNER JOIN st_se_agent_ordered_games c ON a.order_id=c.order_id INNER JOIN st_se_game_master d ON c.game_id=d.game_id WHERE a.retailer_org_id=?");
			if (gameName != null && gameName.length() > 0)
				queryBuilder.append(" AND game_name LIKE '%").append(gameName).append("%'");
			if (gameNumber != null && gameNumber.length() > 0)
				queryBuilder.append(" AND game_nbr LIKE '%").append(gameNumber).append("%'");
			if (orderNumber != null && orderNumber.length() > 0)
				queryBuilder.append(" AND a.order_id =").append(orderNumber);
			if(orderStatus != null && orderStatus.length() > 0 && !"ALL".equals(orderStatus))
				queryBuilder.append(" AND a.order_status ='").append(orderStatus).append("'");

			queryBuilder.append(" AND order_date >='").append(startDate).append("'");
			queryBuilder.append(" AND order_date <= '").append(endDate).append("'");

			pstmt = conn.prepareStatement(queryBuilder.toString());
			pstmt.setInt(1, retOrgId);
			resultSet = pstmt.executeQuery();
			while (resultSet.next()) {
				orderBean = new OrderRequestBean();
				orderBean.setOrderId(resultSet.getInt("order_id"));
				orderBean.setDate(resultSet.getDate("order_date"));
				orderBean.setName(resultSet.getString("order_status"));
				orderBean.setGameName(resultSet.getString("game_name"));
				orderBean.setNbrOfBooksReq(resultSet.getInt("nbr_of_books_req"));
				orderBean.setNbrOfBooksDlvrd(resultSet.getInt("nbr_of_books_dlvrd"));
				orderBean.setNbrAppBooks(resultSet.getInt("nbr_of_books_appr"));
				list.add(orderBean);
			}
			return list;
		} catch (SQLException se) {
			throw new LMSException(se);
		} finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException se) {
				throw new LMSException(se);
			}
		}
	}

}
