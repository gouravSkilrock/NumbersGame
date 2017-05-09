package com.skilrock.lms.web.roleMgmt.common;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.utility.AutoGenerate;
import com.skilrock.lms.common.utility.MD5Encoder;

public class AddNewSubUserAction extends ActionSupport implements
		ServletRequestAware {
	static Log logger = LogFactory.getLog(AddNewSubUserAction.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String email;
	private String firstName;
	private String lastName;
	private String phone;
	HttpServletRequest request;
	private String secAns;
	private String secQues;
	private String status;
	private String userName;

	@Override
	public String execute() {
		HttpSession session = getRequest().getSession();
		 
		Connection con = DBConnect.getConnection();
		try {
			con.setAutoCommit(false);
			Statement stmt = con.createStatement();

			UserInfoBean userBean = new UserInfoBean();
			userBean = (UserInfoBean) session.getAttribute("USER_INFO");
			int orgId = userBean.getUserOrgId();
			int roleId = userBean.getRoleId();
			String type = "BO"; // it would be the type of logged in person org
			// type
			String password = MD5Encoder.encode(AutoGenerate.autoPassword());

			stmt
					.executeUpdate("insert into st_lms_user_master(organization_id,role_id,organization_type,registration_date,auto_password,user_name,password,status,secret_ques,secret_ans,isrolehead) values("
							+ orgId
							+ ","
							+ roleId
							+ ",'"
							+ type
							+ "',CURRENT_DATE,1,'"
							+ getUserName().toLowerCase()
							+ "','"
							+ password
							+ "','"
							+ getStatus()
							+ "','"
							+ getSecQues()
							+ "','" + getSecAns() + "','N')");

			con.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return SUCCESS;

	}

	public String getEmail() {
		return email;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getPhone() {
		return phone;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public String getSecAns() {
		return secAns;
	}

	public String getSecQues() {
		return secQues;
	}

	public String getStatus() {
		return status;
	}

	public String getUserName() {
		return userName;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setSecAns(String secAns) {
		this.secAns = secAns;
	}

	public void setSecQues(String secQues) {
		this.secQues = secQues;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}