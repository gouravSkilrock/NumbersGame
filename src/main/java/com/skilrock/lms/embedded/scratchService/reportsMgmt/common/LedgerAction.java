package com.skilrock.lms.embedded.scratchService.reportsMgmt.common;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.coreEngine.userMgmt.common.CommonFunctionsHelper;
import com.skilrock.lms.embedded.drawGames.common.EmbeddedErrors;

/**
 * This class is for generation of Agent and Back Office ledger.
 * 
 * @author Skilrock Technologies
 * 
 */
public class LedgerAction extends ActionSupport implements ServletRequestAware,
		ServletResponseAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static ServletContext sc = ServletActionContext.getServletContext();
	static boolean isReceiptWise = (Boolean) sc.getAttribute("IS_RECEIPT_WISE");
	SimpleDateFormat dateformat = null;
	Date dateFrDtParse = null;
	String formatString = null;
	private Date frDate;

	private String fromDate;

	String query = null;

	// LedgerHelper ledgerHelper=new LedgerHelper();

	/*
	 * These were the duplicate variable which have been moved to global
	 * variables 07-03-
	 */
	private HttpServletRequest request;
	private HttpServletResponse response;

	HttpSession session = null;
	private Date tDate;

	private String toDate;
	UserInfoBean userBean = null;
	private String userName;

	private void generateReport(String query, Timestamp fromTimeStamp,
			Timestamp dt, String typeName) {

		DBConnect dbConnect = null;
		Connection connection = null;

		double balance = 0.0;
		double amount = 0.0;

		System.out.println("query " + query);
		System.out.println("fromTimeStamp  " + fromTimeStamp);
		System.out.println("dt  " + dt);
		System.out.println("typeName  " + typeName);
		ServletContext sc = ServletActionContext.getServletContext();
		String isDraw = (String) sc.getAttribute("IS_SCRATCH");
		if (isDraw.equalsIgnoreCase("NO")) {
			try {
				response.getOutputStream().write(
						"Scratch Game Not Avaialbe".getBytes());
			} catch (IOException e) {
				System.out.println(" exception in ledger action");
				e.printStackTrace();
			}
			return;
		}
		try {

			 
			connection = DBConnect.getConnection();
			PreparedStatement statement = connection.prepareStatement(query);

			statement.setString(1, typeName);
			statement.setTimestamp(2, fromTimeStamp);
			statement.setTimestamp(3, dt);

			System.out.println("  " + statement);
			ResultSet resultSet = statement.executeQuery();

			String transactionType = null;
			// String transactionId = null;
			// String accountType = null;
			// Timestamp trDate = null;
			// String transactionWith = null;

			StringBuilder xmlString = new StringBuilder(
					"<?xml version=\"1.0\" encoding=\"UTF-8\"?><ledger><date>"
							+ fromTimeStamp.toString().substring(0, 10)
							+ "</date>");

			System.out.println(" before result set");
			int i = 0;
			String amtType = null;
			while (resultSet.next()) {
				System.out.println(" <<<----inside---->>> result set");
				if (i == 0) {
					balance = resultSet.getDouble("balance");

				}
				i++;
				amount = resultSet.getDouble("amount");
				if (amount > 0) {
					amtType = "C";
				} else {
					amtType = "D";
					amount = amount * -1;
				}
				transactionType = resultSet.getString("transaction_type");
				xmlString.append("<details id=\"" + i + "\">" + transactionType
						+ " " + amount + " " + amtType + "</details>");
				// transactionId = resultSet.getString("transaction_id");
				// accountType = resultSet.getString("account_type");
				// trDate = resultSet.getTimestamp("transaction_date");
				// transactionWith = resultSet.getString("transaction_with");
				// System.out.println(balance + " " + amount + " "
				// + transactionType + " " + transactionId + " "
				// + accountType + " " + trDate + " " + transactionWith);

			}
			xmlString.append("<balance>" + balance + "</balance></ledger>");
			System.out.println("openingBalance " + balance);

			System.out.println("PPPPPPPPPP" + statement.toString());

			response.getOutputStream().write(xmlString.toString().getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {

				System.out.println(" closing connection  ");
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	public String getFromDate() {
		return fromDate;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public String getToDate() {
		return toDate;
	}

	public String getUserName() {
		return userName;
	}

	/**
	 * This method is for entering the data into agent ledger.
	 * 
	 * @return String
	 * @throws Exception
	 */
	public void retLedger() throws Exception {
		System.out.println(" from data " + fromDate);
		System.out.println(" to date " + toDate);
		ServletContext sc = ServletActionContext.getServletContext();
		String isDraw = (String) sc.getAttribute("IS_SCRATCH");
		if (isDraw.equalsIgnoreCase("NO")) {
			response.getOutputStream().write(
					"Scratch Game Not Avaialbe".getBytes());
			return;
		}

		// ServletContext sc = ServletActionContext.getServletContext();

		Map currentUserSessionMap = (Map) sc.getAttribute("LOGGED_IN_USERS");
		if (currentUserSessionMap == null) {
			response
					.getOutputStream()
					.write(
							("ErrorMsg:" + EmbeddedErrors.SESSION_EXPIRED + "ErrorCode:01|")
									.getBytes());
			return;
		}
		//System.out.println(" LOGGED_IN_USERS maps is " + currentUserSessionMap);

		System.out.println(" user name is " + userName);
		session = (HttpSession) currentUserSessionMap.get(userName);
		if (!CommonFunctionsHelper.isSessionValid(session)) {
			response
					.getOutputStream()
					.write(
							("ErrorMsg:" + EmbeddedErrors.SESSION_EXPIRED + "ErrorCode:01|")
									.getBytes());
			return;
		}
		formatString = (String) session.getAttribute("date_format");
		dateformat = new SimpleDateFormat(formatString);
		dateFrDtParse = dateformat.parse(getFromDate());
		userBean = (UserInfoBean) session.getAttribute("USER_INFO");
		frDate = dateformat.parse(fromDate);
		tDate = dateformat.parse(toDate);

		String orgName = userBean.getOrgName();
		dateFrDtParse = dateformat.parse(getFromDate());
		Timestamp fromTimeStamp = new Timestamp(dateFrDtParse.getTime());

		Date dateToDtParse = dateformat.parse(getToDate());
		Timestamp dt = new Timestamp(dateToDtParse.getTime() + 1000 * 60 * 60
				* 24l);

		query = QueryManager.getST6RetWiseLedgerAgt(isReceiptWise);
		generateReport(query, fromTimeStamp, dt, orgName);

	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;

	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}