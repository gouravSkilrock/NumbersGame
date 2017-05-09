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

package com.skilrock.lms.embedded.scratchService.orderMgmt.common;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.GameBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.scratchService.orderMgmt.common.GenerateRetailerOrderHelper;
import com.skilrock.lms.coreEngine.userMgmt.common.CommonFunctionsHelper;
import com.skilrock.lms.embedded.drawGames.common.EmbeddedErrors;

/**
 * This class provides methods for generating order for agent
 * 
 * @author Skilrock Technologies
 * 
 */
public class GenerateRetailerOrderAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String gameNo[]; // game id array
	private String noOfBooks[];
	private HttpServletRequest request;

	private HttpServletResponse response;
	private String userName;

	public void generateQuickOrder() throws LMSException {
		try {
			ServletContext sc = ServletActionContext.getServletContext();
			String isDraw = (String) sc.getAttribute("IS_SCRATCH");
			if (isDraw.equalsIgnoreCase("NO")) {
				response.getOutputStream().write(
						"Scratch Game Not Avaialbe".getBytes());
				return;
			}

			if (gameNo == null || gameNo.length == 0 || noOfBooks == null
					|| noOfBooks.length == 0
					|| gameNo.length != noOfBooks.length) {

				System.out.println(" game name is = " + gameNo
						+ "   no of books is = " + noOfBooks);
				response.getOutputStream().write(
						"Please Enter Valid No. Of Books.".getBytes());
			}
			System.out.println(" game name is = " + gameNo
					+ " game name length = " + gameNo.length
					+ "   no of books is = " + noOfBooks
					+ " no of books length = " + noOfBooks.length);

			Map currentUserSessionMap = (Map) sc
					.getAttribute("LOGGED_IN_USERS");
			if (currentUserSessionMap == null) {
				response
						.getOutputStream()
						.write(
								("ErrorMsg:" + EmbeddedErrors.SESSION_EXPIRED + "ErrorCode:01|")
										.getBytes());
				return;
			}
			/*
			 * System.out.println(" LOGGED_IN_USERS maps is " +
			 * currentUserSessionMap);
			 */

			System.out.println(" user name is " + userName);

			HttpSession session = (HttpSession) currentUserSessionMap
					.get(userName);
			if (!CommonFunctionsHelper.isSessionValid(session)) {
				response
						.getOutputStream()
						.write(
								("ErrorMsg:" + EmbeddedErrors.SESSION_EXPIRED + "ErrorCode:01|")
										.getBytes());
				return;
			}
			boolean isGameNoValid = false;
			String gameStatus = null;

			String query = "select game_id,game_status from st_game_master where game_nbr="
					+ gameNo[0];

			Connection con = DBConnect.getConnection();

			try {
				PreparedStatement pStatement = con.prepareStatement(query);
				ResultSet rs = pStatement.executeQuery();
				while (rs.next()) {
					gameNo[0] = "" + rs.getInt("game_id");
					gameStatus = rs.getString("game_status");
					isGameNoValid = true;
				}
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(" game status is " + gameStatus);

			System.out.println(" isgameNoValid " + isGameNoValid);
			//
			if (isGameNoValid) {
				if (!gameStatus.equals("OPEN")) {
					response.getOutputStream().write(
							"This game is not OPEN for sale.".getBytes());
					return;
				}
				GenerateRetailerOrderHelper orderHelper = new GenerateRetailerOrderHelper();
				List<GameBean> cartList = orderHelper.createCartOfOrder(gameNo,
						noOfBooks);
				if (cartList == null) {

					System.out.println(" game name is = " + gameNo
							+ "   no of books is = " + noOfBooks);
					response.getOutputStream().write(
							"Please Enter Valid No. Of Books.".getBytes());
				}
				UserInfoBean userInfoBean = (UserInfoBean) session
						.getAttribute("USER_INFO");
				// int agtId = userInfoBean.getUserId();
				int agtOrgId = userInfoBean.getParentOrgId();

				int retId = userInfoBean.getUserId();
				int retOrgId = userInfoBean.getUserOrgId();
				orderHelper.generateOrder(agtOrgId,retId, retOrgId, cartList);
				response.getOutputStream().write(
						"Quick Order has been requested Successfully"
								.getBytes());
			} else {
				response.getOutputStream().write(
						"Game number not valid".getBytes());
			}
		} catch (IOException e) {
			throw new LMSException();
		}
	}

	public String[] getGameNo() {
		return gameNo;
	}

	public String[] getNoOfBooks() {
		return noOfBooks;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public String getUserName() {
		return userName;
	}

	public void setGameNo(String[] gameNo) {
		this.gameNo = gameNo;
	}

	public void setNoOfBooks(String[] noOfBooks) {
		this.noOfBooks = noOfBooks;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse res) {
		this.response = res;

	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
