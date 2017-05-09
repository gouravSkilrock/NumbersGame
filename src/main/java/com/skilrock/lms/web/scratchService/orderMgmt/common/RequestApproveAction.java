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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.OrderRequestBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.db.TableConstants;
import com.skilrock.lms.common.exception.LMSException;

/**
 * 
 * This class used to approve or Deny the request.
 * 
 * @author SkilRock Technology
 * 
 */
public class RequestApproveAction extends ActionSupport implements
		ServletRequestAware {
	static Log logger = LogFactory.getLog(RequestApproveAction.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int[] allowedBooks;
	private int[] differenceBtAgentandApprBooks; // for retailer request
	private int[] differenceBtBOndApprBooks;
	private int[] gameId;
	private String[] gameName;
	private int[] gameNumber;
	private int[] nbrOfBooksAtAgent; // for retailer request
	private int[] nbrOfBooksAtBO;

	private int[] nbrOfBooksReq;
	OrderRequestBean orderBean;

	private int orderId;
	private HttpServletRequest request = null;
	private String requestApproval = null;
	private int[] totalApproved;

	/**
	 * 
	 * This method is used to Deny the request from the Agent
	 * 
	 * @author SkilRock Technologies
	 * @Param
	 * @Return String(SUCCESS or ERROR) throws LMSException
	 */

	public String Deny() throws LMSException {
		HttpSession session = getRequest().getSession();
		orderId = ((Integer) session.getAttribute("OrgId")).intValue();

		 
		Connection conn = null;
		PreparedStatement pstmt1 = null;

		try {
			conn = DBConnect.getConnection();
			conn.setAutoCommit(false);

			String query = QueryManager.getST5OrderRequest5Query();
			// String query = "update st_se_bo_order set order_status='DENIED'
			// WHERE order_id=?";
			pstmt1 = conn.prepareStatement(query);
			System.out.println("Query1 from Request Deny Action  " + query);
			System.out.println("OrderId>>>>" + orderId);
			pstmt1.setInt(1, orderId);
			pstmt1.executeUpdate();
			conn.commit();
			return SUCCESS;
		} catch (SQLException se) {
			System.out
					.println("We got an exception while preparing a statement:"
							+ "Probably bad SQL.");
			se.printStackTrace();
			setRequestApproval("No");
			throw new LMSException(se);
		} finally {

			try {

				if (pstmt1 != null) {
					pstmt1.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException se) {
				throw new LMSException(se);
			}

		}

	}

	public String denyRetailer() throws LMSException {
		System.out.println("denie order for retailer");
		HttpSession session = getRequest().getSession();
		orderId = ((Integer) session.getAttribute("OrgId")).intValue();

		 
		Connection conn = null;
		PreparedStatement pstmt1 = null;

		try {
			conn = DBConnect.getConnection();
			conn.setAutoCommit(false);

			String query = QueryManager.getST5RetOrderRequest5Query();
			// String query = "update st_se_bo_order set order_status='DENIED'
			// WHERE order_id=?";
			pstmt1 = conn.prepareStatement(query);
			System.out.println("Query1 from Request Deny Action  " + query);
			System.out.println("OrderId>>>>" + orderId);
			pstmt1.setInt(1, orderId);
			pstmt1.executeUpdate();
			System.out.println(pstmt1);
			conn.commit();
			return SUCCESS;
		} catch (SQLException se) {
			System.out
					.println("We got an exception while preparing a statement:"
							+ "Probably bad SQL.");
			se.printStackTrace();
			setRequestApproval("No");
			throw new LMSException(se);
		} finally {

			try {

				if (pstmt1 != null) {
					pstmt1.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException se) {
				throw new LMSException(se);
			}

		}

	}

	/**
	 * 
	 * This method is used to approve the request by the BO Admin
	 * 
	 * @author SkilRock Technologies
	 * @Param takes
	 *            the parameters coming from jsp Page
	 * @Return String throws LMSException
	 */
	@Override
	public String execute() throws LMSException {
		 
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt1 = null;
		String orgQuery = null;
		PreparedStatement orgPstmt = null;
		ResultSet resultSet = null;
		double currCreditAmt = 0.0;
		double creditLimit = 0.0;
		String stt = null;

		HttpSession session = getRequest().getSession();
		orderId = ((Integer) session.getAttribute("OrgId")).intValue();
		System.out.println("gameNumber" + gameNumber.length);
		if (gameNumber.length > 0) {
			totalApproved = new int[gameNumber.length];
		} else {
			totalApproved = new int[1];
		}
		// From the front end allowed books for each game will come in the
		// allowedBooks[] array. If total approved books are greater than the
		// min boks b/w the total no of books at the BO and the remaining books.
		for (int i = 0; i < gameNumber.length; i++) {
			System.out.println("Game Number" + gameNumber[i]);

			if (allowedBooks[i] > findMin(nbrOfBooksAtBO[i],
					differenceBtBOndApprBooks[i])) {
				addActionError("Enter valid Alloted book value for game"
						+ gameName[i]);
				setRequestApproval("No");
				System.out.println("There is ERROR");
				return ERROR;
			}
			System.out.println("allowedBooks[i]  " + allowedBooks[i]);

			totalApproved[i] = allowedBooks[i];
			System.out.println("Approved for game" + totalApproved[i]);
		}

		try {

			conn = DBConnect.getConnection();
			conn.setAutoCommit(false);
			/*
			 * check if available credit is <0
			 */

			orgQuery = QueryManager.getST1OrgCreditQuery();
			orgPstmt = conn.prepareStatement(orgQuery);

			orgPstmt.setInt(1, orderId);
			resultSet = orgPstmt.executeQuery();

			while (resultSet.next()) {

				currCreditAmt = resultSet
						.getDouble(TableConstants.SOM_CURR_CREDIT_AMT);
				creditLimit = resultSet
						.getDouble(TableConstants.SOM_CREDIT_LIMIT);

			}

			/*
			 * end
			 */

			String query1 = QueryManager.getST5OrderRequest4Query();
			// pstmt1=conn.prepareStatement("update st_se_bo_order set
			// order_status='APPROVED' WHERE order_id=?");
			// / if agent is having the less credit amt than he wont be allowed
			// for the order approved
			if (currCreditAmt > creditLimit) {
				stt = Deny();
				addActionError("You Do not have enough  Credit Available ");
			} else {
				pstmt1 = conn.prepareStatement(query1);
				// String query2 ="update st_se_bo_ordered_games set
				// nbr_of_books_appr="+totalApproved+" WHERE
				// order_id="+orderId+"and game_id=";

				System.out.println("Query1 from Request Aprove Action  "
						+ query1);
				System.out.println("OrderId>>>>" + orderId);
				pstmt1.setInt(1, orderId);
				pstmt1.executeUpdate();

				String query2 = QueryManager.getST5OrderRequest3Query();
				// pstmt = conn.prepareStatement("UPDATE st_se_bo_ordered_games
				// SET nbr_of_books_appr = ? WHERE order_id ="+orderId+" and
				// game_id=?");
				pstmt = conn.prepareStatement(query2);
				for (int i = 0; i < gameNumber.length; i++) {
					System.out.println("gameId" + gameId[i]);
					pstmt.setInt(1, totalApproved[i]);
					pstmt.setInt(2, orderId);
					pstmt.setInt(3, gameId[i]);
					pstmt.executeUpdate();
					System.out.println("gameId" + gameId[i]);
				}
				conn.commit();
				setRequestApproval("Yes");
				stt = "SUCCESS";

			}
			return stt;

		} catch (SQLException se) {
			System.out
					.println("We got an exception while preparing a statement:"
							+ "Probably bad SQL.");
			se.printStackTrace();
			setRequestApproval("No");
			throw new LMSException(se);
		} finally {

			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (pstmt1 != null) {
					pstmt1.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException se) {
				throw new LMSException(se);
			}

		}

	}

	public String executeAgent() throws LMSException {
		 
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt1 = null;
		String orgQuery = null;
		PreparedStatement orgPstmt = null;
		ResultSet resultSet = null;
		double currCreditAmt = 0.0;
		double creditLimit = 0.0;
		String stt = null;

		HttpSession session = getRequest().getSession();
		orderId = ((Integer) session.getAttribute("OrgId")).intValue();
		System.out.println("gameNumber" + gameNumber.length);
		if (gameNumber.length > 0) {
			totalApproved = new int[gameNumber.length];
		} else {
			totalApproved = new int[1];
		}
		// From the front end allowed books for each game will come in the
		// allowedBooks[] array. If total approved books are greater than the
		// min boks b/w the total no of books at the BO and the remaining books.
		for (int i = 0; i < gameNumber.length; i++) {
			System.out.println("Game Number" + gameNumber[i]);
			System.out.println("ssssss " + getAllowedBooks());
			System.out.println("11 "
					+ allowedBooks[i]
					+ "22 "
					+ findMin(nbrOfBooksAtAgent[i],
							differenceBtAgentandApprBooks[i]));

			if (allowedBooks[i] > findMin(nbrOfBooksAtAgent[i],
					differenceBtAgentandApprBooks[i])) {
				addActionError("Enter valid Alloted book value for game"
						+ gameName[i]);
				setRequestApproval("No");
				System.out.println("There is ERROR");
				return ERROR;
			}
			System.out.println("allowedBooks[i]  " + allowedBooks[i]);

			totalApproved[i] = allowedBooks[i];
			System.out.println("Approved for game" + totalApproved[i]);
		}

		try {

			conn = DBConnect.getConnection();
			conn.setAutoCommit(false);
			/*
			 * check if available credit is <0
			 */

			orgQuery = QueryManager.getST1OrgCreditQuery();
			orgPstmt = conn.prepareStatement(orgQuery);

			orgPstmt.setInt(1, orderId);
			resultSet = orgPstmt.executeQuery();

			while (resultSet.next()) {

				currCreditAmt = resultSet
						.getDouble(TableConstants.SOM_CURR_CREDIT_AMT);
				creditLimit = resultSet
						.getDouble(TableConstants.SOM_CREDIT_LIMIT);

			}

			/*
			 * end
			 */

			String query1 = QueryManager.getST5RetailerOrderRequest4Query();
			// pstmt1=conn.prepareStatement("update st_se_bo_order set
			// order_status='APPROVED' WHERE order_id=?");
			// / if agent is having the less credit amt than he wont be allowed
			// for the order approved
			if (currCreditAmt > creditLimit) {
				stt = Deny();
				addActionError("You Do not have enough  Credit Available ");
			} else {
				pstmt1 = conn.prepareStatement(query1);
				// String query2 ="update st_se_bo_ordered_games set
				// nbr_of_books_appr="+totalApproved+" WHERE
				// order_id="+orderId+"and game_id=";

				System.out.println("Query1 from Request Aprove Action  "
						+ query1);
				System.out.println("OrderId>>>>" + orderId);
				pstmt1.setInt(1, orderId);
				pstmt1.executeUpdate();

				String query2 = QueryManager.getST5RetailerOrderRequest3Query();
				// pstmt = conn.prepareStatement("UPDATE st_se_bo_ordered_games
				// SET nbr_of_books_appr = ? WHERE order_id ="+orderId+" and
				// game_id=?");
				pstmt = conn.prepareStatement(query2);
				for (int i = 0; i < gameNumber.length; i++) {
					System.out.println("gameId" + gameId[i]);
					pstmt.setInt(1, totalApproved[i]);
					pstmt.setInt(2, orderId);
					pstmt.setInt(3, gameId[i]);
					pstmt.executeUpdate();
					System.out.println("gameId" + gameId[i]);
				}
				conn.commit();
				setRequestApproval("Yes");
				stt = "SUCCESS";

			}
			return stt;

		} catch (SQLException se) {
			System.out
					.println("We got an exception while preparing a statement:"
							+ "Probably bad SQL.");
			se.printStackTrace();
			setRequestApproval("No");
			throw new LMSException(se);
		} finally {

			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (pstmt1 != null) {
					pstmt1.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException se) {
				throw new LMSException(se);
			}

		}

	}

	/**
	 * 
	 * This method is used to find minimum.
	 * 
	 * @author SkilRock Technologies
	 * @Param a
	 *            is no of books at BO,b total remaining books
	 * @Return int (minimum b/w books at BO and total remaining books )
	 * 
	 */
	public int findMin(int a, int b) {
		int min = 0;
		if (a < b) {
			min = a;
		} else if (b < a) {
			min = b;
		} else {
			min = a;
		}
		return min;
	}

	public int[] getAllowedBooks() {
		return allowedBooks;
	}

	public int[] getDifferenceBtAgentandApprBooks() {
		return differenceBtAgentandApprBooks;
	}

	public int[] getDifferenceBtBOndApprBooks() {
		return differenceBtBOndApprBooks;
	}

	public int[] getGameId() {
		return gameId;
	}

	public String[] getGameName() {
		return gameName;
	}

	public int[] getGameNumber() {
		return gameNumber;
	}

	public int[] getNbrOfBooksAtAgent() {
		return nbrOfBooksAtAgent;
	}

	public int[] getNbrOfBooksAtBO() {
		return nbrOfBooksAtBO;
	}

	public int[] getNbrOfBooksReq() {
		return nbrOfBooksReq;
	}

	public int getOrderId() {
		return orderId;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public String getRequestApproval() {
		return requestApproval;
	}

	public void setAllowedBooks(int[] allowedBooks) {
		this.allowedBooks = allowedBooks;
	}

	public void setDifferenceBtAgentandApprBooks(
			int[] differenceBtAgentandApprBooks) {
		this.differenceBtAgentandApprBooks = differenceBtAgentandApprBooks;
	}

	public void setDifferenceBtBOndApprBooks(int[] differenceBtBOndApprBooks) {
		this.differenceBtBOndApprBooks = differenceBtBOndApprBooks;
	}

	public void setGameId(int[] gameId) {
		this.gameId = gameId;
	}

	/**
	 * 
	 * This method is used to get Sql date from Date in String format. *
	 * 
	 * @author SkilRock Technologies
	 * @Param Date(string
	 *            format)
	 * @Return java.sql.Date throws LMSException
	 */
	/*
	 * private java.sql.Date getDate(String date) throws LMSException{
	 * 
	 * DateFormat dateFormat = new SimpleDateFormat(); try {
	 * 
	 * Date parsedDate = dateFormat.parse(date); return new
	 * java.sql.Date(parsedDate.getTime()); } catch (ParseException e) {
	 * 
	 * e.printStackTrace(); throw new LMSException(e); } }
	 */
	public void setGameName(String[] gameName) {
		this.gameName = gameName;
	}

	public void setGameNumber(int[] gameNumber) {
		this.gameNumber = gameNumber;
	}

	public void setNbrOfBooksAtAgent(int[] nbrOfBooksAtAgent) {
		this.nbrOfBooksAtAgent = nbrOfBooksAtAgent;
	}

	public void setNbrOfBooksAtBO(int[] nbrOfBooksAtBO) {
		this.nbrOfBooksAtBO = nbrOfBooksAtBO;
	}

	public void setNbrOfBooksReq(int[] nbrOfBooksReq) {
		this.nbrOfBooksReq = nbrOfBooksReq;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setRequestApproval(String requestApproval) {
		this.requestApproval = requestApproval;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}
}
