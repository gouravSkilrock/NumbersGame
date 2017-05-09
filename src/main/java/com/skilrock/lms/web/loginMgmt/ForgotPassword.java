package com.skilrock.lms.web.loginMgmt;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.UserQuesBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.db.TableConstants;
import com.skilrock.lms.common.exception.LMSException;

/**
 * This class provides method for checking Login name while forgot password and
 * fetch secret question for that particular user
 * 
 * @author Skilrock Technologies
 * 
 */
public class ForgotPassword extends ActionSupport implements
		ServletRequestAware {
	static Log logger = LogFactory.getLog(ForgotPassword.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	 
	private String email;
	private String firstName;
	private String lastName;
	private sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
	// List<ForgotPassword> list =new ArrayList<ForgotPassword>();
	// List<UserQuesBean>listBean=new ArrayList<UserQuesBean>();
	private HttpServletRequest request;
	private String secAns = null;
	private String secQues = null;

	private int userId;
	private String userName = null;

	private String userNameDb = null;

	/**
	 * This method is used for checking Login name and fetch secret question for
	 * that particular user
	 * 
	 * @return String
	 * @exception Exception
	 * 
	 */
	@Override
	public String execute() throws LMSException {
		HttpSession s = null;
		logger.debug("heeeeeeeeeeeeellllllllllpo");
		s = getRequest().getSession();
		Connection con = null;
		Statement stmt = null;
		Statement stmt1 = null;
		Statement fetchStmt = null;
		con = DBConnect.getConnection();
		try {
			stmt = con.createStatement();
			stmt1 = con.createStatement();
			fetchStmt = con.createStatement();
			ResultSet rs = null;

			if (getUserName() != "") {
				String query = QueryManager.getST3ForgotPass()
						+ " where user_name = '" + getUserName() + "'";
				rs = stmt.executeQuery(query);
				logger.debug("ha jeeeee");
				while (rs.next()) {

					secQues = rs.getString(TableConstants.SEC_QUES1);
					userNameDb = rs.getString(TableConstants.USER_NAME);
					secAns = rs.getString(TableConstants.SEC_ANS);
					userId = rs.getInt(TableConstants.USER_ID);

					// ResultSet rs1= stmt1.executeQuery("select email_id from
					// st_lms_user_contact_details where user_id="+userId+" ");
					ResultSet rs1 = stmt1.executeQuery(QueryManager
							.getST3ContactDetails()
							+ " where user_id=" + userId + " ");
					while (rs1.next()) {
						email = rs1.getString(TableConstants.EMAIL);
						firstName = rs1.getString(TableConstants.FIRST_NAME);
						lastName = rs1.getString(TableConstants.LAST_NAME);

					}
					logger.debug("hhhhhhhhhhh" + secQues);
					logger.debug("ppppppppp" + userNameDb);

				}

				if (userNameDb != null && userNameDb.equals(getUserName()))

				{
					logger.debug("from db   " + userNameDb + "from user  "
							+ getUserName());
					addActionError(secQues);
					UserQuesBean userQuesBean = new UserQuesBean();
					userQuesBean.setSecAns(secAns);
					userQuesBean.setSecQues(secQues);
					userQuesBean.setUserId(userId);
					userQuesBean.setUserNameDb(userNameDb);
					userQuesBean.setUserName(userName);
					userQuesBean.setFirstName(firstName);
					userQuesBean.setLastName(lastName);
					userQuesBean.setEmail(email);
					// listBean.add(userQuesBean);
					s.setAttribute("UserQuesBean", userQuesBean);
					return SUCCESS;

				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException("error during connection ", e);
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

		addActionError("Enter Correct USER NAME  ");
		return INPUT;

	}

	public String getEmail() {
		return email;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public String getSecAns() {
		return secAns;
	}

	public int getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setSecAns(String secAns) {
		this.secAns = secAns;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}