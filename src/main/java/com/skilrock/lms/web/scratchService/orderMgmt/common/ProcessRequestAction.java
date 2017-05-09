/***
 *  * � copyright 2007, SkilRock Technologies, A division of Sugal & Damani Lottery Agency Pvt. Ltd.
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
 * 
 */
package com.skilrock.lms.web.scratchService.orderMgmt.common;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.OrderRequestBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.CommonMethods;

/**
 * 
 * This class used to process UnApproved Requests by the BO Admin
 * 
 * @author Skilrock Technologies
 * 
 */
public class ProcessRequestAction extends ActionSupport implements
		ServletRequestAware {
	static Log logger = LogFactory.getLog(ProcessRequestAction.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private java.sql.Date date = null;
	private String end = null;
	private List<OrderRequestBean> list;
	private String name = null;
	private OrderRequestBean orderBean;
	private int orderId;
	private HttpServletRequest request = null;

	int start = 0;
	private String varFromProcessRequest = null;

	/**
	 * 
	 * This method is used to process Unapproved requested by the agent.
	 * 
	 * @author SkilRock Technologies
	 * @return String throws LMSException
	 */

	private String gameName;
	private String gameNumber;
	private String agtOrgName;
	private String orderNumber;

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getGameNumber() {
		return gameNumber;
	}

	public void setGameNumber(String gameNumber) {
		this.gameNumber = gameNumber;
	}

	public String getAgtOrgName() {
		return agtOrgName;
	}

	public void setAgtOrgName(String agtOrgName) {
		this.agtOrgName = agtOrgName;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	@Override
	public String execute() throws Exception {
		HttpSession session = getRequest().getSession();
		session.setAttribute("RequestList", null);
		session.setAttribute("orderId", null);
		session.setAttribute("TOTAL", null);
		session.setAttribute("BALANCE", null);
		session.setAttribute("CREDIT_LIMIT", null);
		session.setAttribute("CREDIT_AMT", null);

		list = new ArrayList<OrderRequestBean>();

		 
		Connection conn = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			conn = DBConnect.getConnection();
			StringBuilder queryBuilder = new StringBuilder();
			String query = "SELECT a.order_id, a.order_date, b.name FROM st_se_bo_order a INNER JOIN st_lms_organization_master b ON a.agent_org_id=b.organization_id INNER JOIN st_se_bo_ordered_games c ON a.order_id=c.order_id INNER JOIN st_se_game_master d ON c.game_id=d.game_id WHERE a.order_status='REQUESTED'";
			query = CommonMethods.appendRoleAgentMappingQuery(query,"a.agent_org_id",((UserInfoBean) session.getAttribute("USER_INFO")).getRoleId());// +" group by
			queryBuilder.append(query);
			if(gameName!=null && gameName.length()>0)
				queryBuilder.append(" AND game_name LIKE '%").append(gameName).append("%'");
			if(gameNumber!=null && gameNumber.length()>0)
				queryBuilder.append(" AND game_nbr LIKE '%").append(gameNumber).append("%'");
			if(agtOrgName!=null && agtOrgName.length()>0)
				queryBuilder.append(" AND name LIKE '%").append(agtOrgName).append("%'");
			if(orderNumber!=null && orderNumber.length()>0)
				queryBuilder.append(" AND order_status LIKE '%").append(orderNumber).append("%'");
			logger.info("Search - " + queryBuilder.toString());
			statement = conn.createStatement();
			resultSet = statement.executeQuery(queryBuilder.toString());
			while (resultSet.next()) {
				orderBean = new OrderRequestBean();
				orderId = resultSet.getInt("order_id");
				date = resultSet.getDate("order_date");
				name = resultSet.getString("name");
				orderBean.setOrderId(orderId);
				orderBean.setDate(date);
				orderBean.setName(name);
				list.add(orderBean);

			}
			session.setAttribute("RequestList", list);
			session.setAttribute("RequestList1", list);
			session.setAttribute("orderId", orderId);

			if (list != null && list.size() > 0) {
				session.setAttribute("startValueRequestSearch", new Integer(0));
				searchAjaxRequest();
			}
			setVarFromProcessRequest("Yes");
			return SUCCESS;
		} catch (SQLException se) {
			setVarFromProcessRequest("No");
			conn.rollback();
			System.out
					.println("We got an exception while preparing a statement:"
							+ "Probably bad SQL.");
			throw new LMSException(se);

		} finally {

			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (statement != null) {
					statement.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException se) {
				throw new LMSException(se);
			}

		}

	}

	public java.sql.Date getDate() {
		return date;
	}

	public String getEnd() {
		return end;
	}

	public String getName() {
		return name;
	}

	public int getOrderId() {
		return orderId;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public int getStart() {
		return start;
	}

	public String getVarFromProcessRequest() {
		return varFromProcessRequest;
	}

	public String searchAjaxRequest() {
		int endValue = 0;
		int startValue = 0;
		// PrintWriter out = getResponse().getWriter();
		HttpSession session = getRequest().getSession();
		List ajaxList = (List) session.getAttribute("RequestList1");
		List ajaxSearchList = new ArrayList();
		// logger.debug("end "+getEnd());
		if (ajaxList != null) {
			if (getEnd() != null) {
				end = getEnd();
			} else {
				end = "first";
			}
			// logger.debug("end "+end);
			startValue = (Integer) session
					.getAttribute("startValueRequestSearch");
			if (end.equals("first")) {
				logger.debug("i m in first");
				startValue = 0;
				endValue = startValue + 5;

				if (endValue > ajaxList.size()) {
					endValue = ajaxList.size();
				}
			} else if (end.equals("Previous")) {
				logger.debug("i m in Previous");
				startValue = startValue - 5;
				if (startValue < 5) {
					startValue = 0;
				}
				endValue = startValue + 5;
				if (endValue > ajaxList.size()) {
					endValue = ajaxList.size();
				}
			} else if (end.equals("Next")) {
				logger.debug("i m in Next");
				startValue = startValue + 5;
				endValue = startValue + 5;
				if (endValue > ajaxList.size()) {
					endValue = ajaxList.size();
				}
				if (startValue > ajaxList.size()) {
					startValue = ajaxList.size() - ajaxList.size() % 5;
				}
			} else if (end.equals("last")) {
				endValue = ajaxList.size();
				startValue = endValue - endValue % 5;

			}
			if (startValue == endValue) {
				startValue = endValue - 5;
			}
			// logger.debug("End value"+endValue);
			// logger.debug("Start Value"+startValue);
			for (int i = startValue; i < endValue; i++) {
				ajaxSearchList.add(ajaxList.get(i));
			}
			session.setAttribute("RequestList", ajaxSearchList);
			session.setAttribute("startValueRequestSearch", startValue);
		}
		return SUCCESS;
	}

	public void setDate(java.sql.Date date) {
		this.date = date;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public void setVarFromProcessRequest(String varFromProcessRequest) {
		this.varFromProcessRequest = varFromProcessRequest;
	}

}
