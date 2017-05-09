package com.skilrock.lms.coreEngine.loginMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.AutoGenerate;
import com.skilrock.lms.common.utility.MD5Encoder;
import com.skilrock.lms.common.utility.MailSend;

/**
 * This class is used while forget password to check the secret answer and
 * generate new password
 * 
 * @author Skilrock Technologies
 * 
 */

public class CheckAnsHelper {
	Log logger = LogFactory.getLog(CheckAnsHelper.class);

	/**
	 * This method is used to check secret answer and send new password to users
	 * email address
	 * 
	 * @param Data
	 *            is List of type UserQuesBean
	 * @throws LMSException
	 */
	public ResultSet checkAns(int userId, String userName, String email,
			String firstName, String lastName) throws LMSException {
		String projectName = ServletActionContext.getServletContext()
				.getContextPath();
		String query = null;
		String querypass = null;
		Connection connection = null;
		PreparedStatement statementuser = null;
		PreparedStatement statementpass = null;
		String autoPass = AutoGenerate.autoPassword();

		try {

			// userId=Data.get(0).getUserId();
			// userName=Data.get(0).getUserName();
			// userId=Data.get(0).getUserId();
			// email=Data.get(0).getEmail();
			logger.debug("email  is " + email);

			 
			connection = DBConnect.getConnection();
			connection.setAutoCommit(false);
			query = QueryManager.getST3UpdateUserMaster();
			querypass = QueryManager.getST3UpdatePassHistory();

			statementuser = connection.prepareStatement(query);
			statementpass = connection.prepareStatement(querypass);

			statementuser.setString(1, MD5Encoder.encode(autoPass));
			statementuser.setString(2, "1");
			statementuser.setString(3, userName);

			statementpass.setInt(1, userId);
			statementpass.setString(2, MD5Encoder.encode(autoPass));
			statementpass.setString(3, "1");

			statementuser.execute();
			statementpass.execute();

			connection.commit();
			String msgFor = "Welcome to our gaming system Your login details are !";
			// MailSend.sendMailToUser(email, autoPass,userName);
			String emailMsgTxt = "<html><table><tr><td>Hi " + firstName + " "
					+ lastName + "</td></tr><tr><td>" + msgFor
					+ "</td></tr></table><table><tr><td>User Name :: </td><td>"
					+ userName + "</td></tr><tr><td>password :: </td><td>"
					+ autoPass.toString()
					+ "</td></tr><tr><td>log on </td><td>"
					+ LMSFilterDispatcher.webLink + "/"
					+ LMSFilterDispatcher.mailProjName
					+ "/</td></tr></table></html>";
			MailSend mailSend = new MailSend(email, emailMsgTxt);
			mailSend.setDaemon(true);
			mailSend.start();
			// MailSend.sendMailToUser(email,
			// autoPass,userName,firstName,firstName,msgFor);
			logger.debug("11111111111");
		} catch (SQLException e) {

			try {
				connection.rollback();
			} catch (SQLException se) {
				se.printStackTrace();
				throw new LMSException("Error During Rollback", se);
			}
			e.printStackTrace();
			throw new LMSException(e);

		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException see) {
				see.printStackTrace();
				throw new LMSException("Error During closing connection", see);
			}
		}

		return null;

	}

}
