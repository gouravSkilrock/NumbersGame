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
package com.skilrock.lms.coreEngine.scratchService.orderMgmt.common;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.skilrock.lms.beans.OrderRequestBean;
import com.skilrock.lms.beans.ScratchBookOrderDataBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.scratchService.common.ScratchErrors;
import com.skilrock.lms.coreEngine.scratchService.common.ScratchException;

/**
 * 
 * This is a helper class used to get the details of the request.It consists the
 * query reading and business logic.
 * 
 * @author Skilrock Technologies
 * 
 */
public class RequestDetailHelper {
	int game_id;
	List pwtRemaiList = null;

	/**
	 * This method is used to find minimum between books at BO and the total
	 * remaining books.
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public int findMin(int a, int b) {
		int min = 0;
		if (b < 0) {
			return min;
		}
		if (a < b) {
			min = a;
		} else if (b < a) {
			min = b;
		} else {
			min = a;
		}
		return min;
	}

	public List getPwtRemaiList() {
		return pwtRemaiList;
	}

	/**
	 * This method is used to get the details of the agent
	 * 
	 * @param name
	 * @return List
	 * @throws LMSException
	 */
	public List searchAgent(int orderId, String name) throws LMSException {

		Connection connection = null;
		Statement statement = null;
		Statement statement1 = null;
		ResultSet resultSet = null;
		try {
			OrderRequestBean orderBean = null;
			List<OrderRequestBean> searchResults = new ArrayList<OrderRequestBean>();
			 
			connection = DBConnect.getConnection();
			statement = connection.createStatement();

			String query = QueryManager.getST5AgentDetailsQuery();

			System.out.println("-----Query----::" + query + name + "'");
			resultSet = statement.executeQuery(query + name + "'");

			while (resultSet.next()) {
				orderBean = new OrderRequestBean();
				orderBean.setOrderId(orderId);
				orderBean.setName(name);
				orderBean.setAddress(resultSet.getString("addr_line1"));
				orderBean.setCity(resultSet.getString("city"));
				orderBean.setCountry(resultSet.getString("country_name"));
				orderBean.setState(resultSet.getString("state_name"));
				orderBean.setPinCode(resultSet.getInt("pin_code"));
				orderBean.setCriditLimit(resultSet.getDouble("credit_limit")
						+ resultSet.getDouble("extended_credit_limit"));
				orderBean.setAvailableCredit(resultSet
						.getDouble("available_credit"));
				System.out.println("available cerdit is  "
						+ resultSet.getDouble("available_credit"));

				System.out.println("credit limit + xcl for agent is "
						+ (resultSet.getDouble("credit_limit") + resultSet
								.getDouble("extended_credit_limit")));
				orderBean.setCurrentBalance(resultSet
						.getDouble("current_credit_amt"));
				searchResults.add(orderBean);

			}

			return searchResults;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new LMSException(e);
		} finally {

			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (statement1 != null) {
					statement1.close();
				}
				if (statement != null) {
					statement.close();
				}

				if (connection != null) {
					connection.close();
				}
			} catch (SQLException se) {
				throw new LMSException(se);
			}

		}

	}

	/**
	 * This method is used to get the details of the request by the BO Admin
	 * 
	 * @param orderId
	 * @return List
	 * @throws LMSException
	 */

	public List searchResult(int orderId) throws LMSException {
		System.out.println(">>>>>>>>>>>>>>>>>" + orderId);
		Connection connection = null;
		Statement statement = null;
		Statement statement1 = null;
		ResultSet resultSet = null;
		ResultSet resultSet1 = null;
		try {
			OrderRequestBean orderBean = null;
			List<OrderRequestBean> searchResults = new ArrayList<OrderRequestBean>();
			 
			connection = DBConnect.getConnection();
			statement = connection.createStatement();
			statement1 = connection.createStatement();
			// String dynamicWhereClause = getWhereClause(searchMap);

			// String query3="select b.game_id 'game_id',b.game_nbr
			// 'game_number',b.game_name 'game_name', a.nbr_of_books_req
			// 'nbr_of_books_req',COUNT(d.book_nbr) 'total' from
			// st_se_bo_ordered_games a,st_se_game_master b,st_se_bo_order
			// c,st_se_game_inv_status d where a.game_id=b.game_id and
			// a.game_id=d.game_id and c.order_status='REQUESTED' and
			// d.current_owner='BACK_OFF' and a.order_id=c.order_id and
			// a.order_id="+orderId+" group by a.game_id";
			String query3 = QueryManager.getST5OrderRequest1Query() + orderId
					+ " group by a.game_id";
			System.out.println(">>>>>>>>Query3" + query3);
			// String query2="select SUM(a.nbr_of_books_appr) 'no_of_books_appr'
			// from st_se_bo_ordered_games a,st_se_bo_order c where
			// c.order_status='APPROVED' and a.order_id=c.order_id and
			// a.game_id=";
			String query2 = QueryManager.getST5OrderRequest2Query();
			System.out.println("-----Query----::" + query3);
			resultSet = statement.executeQuery(query3);

			while (resultSet.next()) {
				orderBean = new OrderRequestBean();

				orderBean.setGameId(resultSet.getInt("game_id"));
				// orderBean.setTicketPrice(resultSet.getDouble("ticket_price"));
				// orderBean.setTicketsPerBook(resultSet.getInt("tickets_per_book"));
				orderBean.setBookPrice(resultSet.getDouble("ticket_price")
						* resultSet.getInt("tickets_per_book"));
				int x = resultSet.getInt("game_id");
				System.out.println("Query2>>>>>>>" + x);
				String q2 = query2 + x;
				resultSet1 = statement1.executeQuery(q2);
				System.out.println(q2);
				resultSet1.next();
				int no_of_books_appr = resultSet1.getInt("no_of_books_appr");
				System.out.println(no_of_books_appr);

				orderBean.setNbrAppBooks(no_of_books_appr);

				orderBean.setGameName(resultSet.getString("game_name"));
				orderBean.setGameNumber(resultSet.getInt("game_number"));
				orderBean
						.setNbrOfBooksReq(resultSet.getInt("nbr_of_books_req"));
				int requested_book = resultSet.getInt("nbr_of_books_req");
				orderBean.setNbrOfBooksAtBO(resultSet.getInt("total"));
				int atBO = resultSet.getInt("total");
				System.out.println("at back office" + atBO);
				System.out.println("approved" + no_of_books_appr);
				int differenceBtBOndApprBooks = atBO - no_of_books_appr;
				System.out.println("diffrerence" + differenceBtBOndApprBooks);
				orderBean
						.setDifferenceBtBOndApprBooks(differenceBtBOndApprBooks);
				orderBean.setAllowedBooks(findMin(requested_book,
						differenceBtBOndApprBooks));
				searchResults.add(orderBean);

			}

			return searchResults;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new LMSException(e);
		} finally {

			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (statement1 != null) {
					statement1.close();
				}
				if (statement != null) {
					statement.close();
				}

				if (connection != null) {
					connection.close();
				}
			} catch (SQLException se) {
				throw new LMSException(se);
			}

		}

	}

	public List searchResultRetailer(int orderId, int agtOrgId)
			throws LMSException {
		System.out.println("hhhhhaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
		System.out.println(">>>>>>>>>>>>>>>>>" + orderId);
		Connection connection = null;
		Statement statement = null;
		Statement statement1 = null;
		ResultSet resultSet = null;
		ResultSet resultSet1 = null;
		try {
			OrderRequestBean orderBean = null;
			List<OrderRequestBean> searchResults = new ArrayList<OrderRequestBean>();
			 
			connection = DBConnect.getConnection();
			statement = connection.createStatement();
			statement1 = connection.createStatement();
			// String dynamicWhereClause = getWhereClause(searchMap);

			// String query3="select b.game_id 'game_id',b.game_nbr
			// 'game_number',b.game_name 'game_name', a.nbr_of_books_req
			// 'nbr_of_books_req',COUNT(d.book_nbr) 'total' from
			// st_se_bo_ordered_games a,st_se_game_master b,st_se_bo_order
			// c,st_se_game_inv_status d where a.game_id=b.game_id and
			// a.game_id=d.game_id and c.order_status='REQUESTED' and
			// d.current_owner='BACK_OFF' and a.order_id=c.order_id and
			// a.order_id="+orderId+" group by a.game_id";
			// String
			// query3=QueryManager.getST5RetailerOrderRequest1Query()+orderId+"
			// and d.current_owner_id="+agtOrgId+" group by a.game_id";
			String query3 = "select a.order_id,b.game_id 'game_id',b.game_nbr 'game_number',b.game_name 'game_name',b.ticket_price 'ticket_price',b.nbr_of_tickets_per_book 'tickets_per_book',a.nbr_of_books_req 'nbr_of_books_req',(select count(book_nbr) from st_se_game_inv_status where current_owner='AGENT' and current_owner_id="
					+ agtOrgId
					+ " and game_id=b.game_id) 'total' from st_se_agent_ordered_games a,st_se_game_master b  where a.order_id="
					+ orderId + " and b.game_id=a.game_id group by a.game_id";
			System.out.println(">>>>>>>>Query3" + query3);
			// String query2="select SUM(a.nbr_of_books_appr) 'no_of_books_appr'
			// from st_se_bo_ordered_games a,st_se_bo_order c where
			// c.order_status='APPROVED' and a.order_id=c.order_id and
			// a.game_id=";
			String query2 = QueryManager.getST5RetailerOrderRequest2Query();
			System.out.println("-----Query----::" + query3);
			resultSet = statement.executeQuery(query3);

			while (resultSet.next()) {
				orderBean = new OrderRequestBean();

				orderBean.setGameId(resultSet.getInt("game_id"));
				orderBean.setBookPrice(resultSet.getDouble("ticket_price")
						* resultSet.getInt("tickets_per_book"));
				int x = resultSet.getInt("game_id");
				System.out.println("Query2>>>>>>>" + x);
				String q2 = query2 + x + " and c.agent_org_id=" + agtOrgId;
				resultSet1 = statement1.executeQuery(q2);
				System.out.println(q2);
				resultSet1.next();
				int no_of_books_appr = resultSet1.getInt("no_of_books_appr");
				System.out.println(no_of_books_appr);

				orderBean.setNbrAppBooks(no_of_books_appr);

				orderBean.setGameName(resultSet.getString("game_name"));
				orderBean.setGameNumber(resultSet.getInt("game_number"));
				orderBean
						.setNbrOfBooksReq(resultSet.getInt("nbr_of_books_req"));
				int requested_book = resultSet.getInt("nbr_of_books_req");
				orderBean.setNbrOfBooksAtAgent(resultSet.getInt("total"));
				int atAgent = resultSet.getInt("total");
				System.out.println("at agent" + atAgent);
				System.out.println("approved" + no_of_books_appr);
				int differenceBtAgentandApprBooks = atAgent - no_of_books_appr;
				System.out.println("diffrerence"
						+ differenceBtAgentandApprBooks);
				if (differenceBtAgentandApprBooks < 0) {
					differenceBtAgentandApprBooks = 0;
				}
				orderBean
						.setDifferenceBtAgentandApprBooks(differenceBtAgentandApprBooks);
				orderBean.setAllowedBooks(findMin(requested_book,
						differenceBtAgentandApprBooks));

				System.out
						.println("alloted books are "
								+ findMin(requested_book,
										differenceBtAgentandApprBooks));

				searchResults.add(orderBean);

			}

			return searchResults;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new LMSException(e);
		} finally {

			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (statement1 != null) {
					statement1.close();
				}
				if (statement != null) {
					statement.close();
				}

				if (connection != null) {
					connection.close();
				}
			} catch (SQLException se) {
				throw new LMSException(se);
			}

		}

	}

	public List searchRetailer(int orderId, String name) throws LMSException {

		Connection connection = null;
		Statement statement = null;
		Statement statement1 = null;
		ResultSet resultSet = null;
		try {
			OrderRequestBean orderBean = null;
			List<OrderRequestBean> searchResults = new ArrayList<OrderRequestBean>();
			 
			connection = DBConnect.getConnection();
			statement = connection.createStatement();

			String query = QueryManager.getST5RetailerDetailsQuery();

			System.out.println("-----Query----::" + query + name + "'");
			resultSet = statement.executeQuery(query + name + "'");

			while (resultSet.next()) {
				orderBean = new OrderRequestBean();
				orderBean.setOrderId(orderId);
				orderBean.setName(name);
				orderBean.setAddress(resultSet.getString("addr_line1"));
				orderBean.setCity(resultSet.getString("city"));
				orderBean.setCountry(resultSet.getString("country_name"));
				orderBean.setState(resultSet.getString("state_name"));
				orderBean.setPinCode(resultSet.getInt("pin_code"));
				orderBean.setCriditLimit(resultSet.getDouble("credit_limit")
						+ resultSet.getDouble("extended_credit_limit"));
				System.out.println("cl + xcl is "
						+ resultSet.getDouble("credit_limit")
						+ resultSet.getDouble("extended_credit_limit"));
				orderBean.setAvailableCredit(resultSet
						.getDouble("available_credit"));
				System.out.println("available credit is + "
						+ resultSet.getDouble("available_credit"));
				orderBean.setCurrentBalance(resultSet
						.getDouble("current_credit_amt"));
				searchResults.add(orderBean);

			}

			return searchResults;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new LMSException(e);
		} finally {

			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (statement1 != null) {
					statement1.close();
				}
				if (statement != null) {
					statement.close();
				}

				if (connection != null) {
					connection.close();
				}
			} catch (SQLException se) {
				throw new LMSException(se);
			}

		}

	}

	public void setPwtRemaiList(List pwtRemaiList) {
		this.pwtRemaiList = pwtRemaiList;
	}
	
	public ScratchBookOrderDataBean fetchOrderDetail(int orderId) throws ScratchException {
		ScratchBookOrderDataBean scratchBookOrderDataBean = null;
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;

		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		Map<String, Map<String, Integer>> gameDataMap = null;
		Map<String, Integer> gameDetailMap = null;

		try {
			con = DBConnect.getConnection();
			stmt = con.createStatement();
			String qry = "select ao.order_id, ao.order_date, ao.order_status, gm.game_name, aog.nbr_of_books_req, ifnull(aog.nbr_of_books_appr, 0) nbr_of_books_appr, ifnull(aog.nbr_of_books_dlvrd, 0) nbr_of_books_dlvrd, dc_id from st_se_agent_order ao  inner join st_se_agent_ordered_games aog on ao.order_id = aog.order_id inner join st_se_game_master gm on aog.game_id = gm.game_id inner join st_se_agent_order_invoices aoi on aoi.order_id = ao.order_id and aoi.game_id = aog.game_id where ao.order_id = " + orderId;
			rs = stmt.executeQuery(qry);
			if(rs.next()) {
				scratchBookOrderDataBean = new ScratchBookOrderDataBean();
				gameDataMap = new HashMap<String, Map<String, Integer>>();

				scratchBookOrderDataBean.setOrderId(rs.getInt("order_id"));
				scratchBookOrderDataBean.setOrderDate(df.format(rs.getTimestamp("order_date")));
				scratchBookOrderDataBean.setOrderStatus(rs.getString("order_status"));
				scratchBookOrderDataBean.setDlNbr(rs.getString("dc_id"));

				rs.beforeFirst();
				while(rs.next()) {
					gameDetailMap = new LinkedHashMap<String, Integer>();
					gameDetailMap.put("noOfBookOrder", rs.getInt("nbr_of_books_req"));
					gameDetailMap.put("approvedBook", rs.getInt("nbr_of_books_appr"));
					gameDetailMap.put("deliveredBooks", rs.getInt("nbr_of_books_dlvrd"));
					
					gameDataMap.put(rs.getString("game_name"), gameDetailMap);
				}
				scratchBookOrderDataBean.setGameDataMap(gameDataMap);
			} else {
				System.out.println("adasd");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ScratchException(ScratchErrors.SQL_EXCEPTION_ERROR_CODE, ScratchErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ScratchException(ScratchErrors.GENERAL_EXCEPTION_ERROR_CODE, ScratchErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeCon(con);
		}
		return scratchBookOrderDataBean;
	}
}
