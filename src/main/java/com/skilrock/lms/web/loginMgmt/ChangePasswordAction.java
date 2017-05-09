package com.skilrock.lms.web.loginMgmt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.coreEngine.loginMgmt.common.ChangePasswordHelper;

/**
 * This class provides method for change password
 * 
 * @author Skilrock Technologies
 * 
 */
public class ChangePasswordAction extends ActionSupport implements
		ServletRequestAware {
	static Log logger = LogFactory.getLog(ChangePasswordAction.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String newPassword = null;
	private String password = null;
	private HttpServletRequest request;
	private String verifynewPassword = null;

	/**
	 * This method is used to change password
	 * 
	 * @return String
	 * @throws Exception
	 */
	public String authentication() throws Exception {
		logger.debug("inside change pass action");
		HttpSession session = request.getSession();
		UserInfoBean bean = (UserInfoBean) session.getAttribute("USER_INFO");
		boolean isRetailer = false;
		String returntype = "";
		if (bean.getUserType().equalsIgnoreCase("RETAILER")
				&& "integer".equalsIgnoreCase(ServletActionContext
						.getServletContext().getAttribute("RETAILER_PASS")
						.toString().trim())) {
			isRetailer = true;
			try {
				Integer.parseInt(newPassword);
			} catch (NumberFormatException e) {
				// e.printStackTrace();
				addActionError(getText("msg.enter.new.pwd.numeral")+"!!");
				return "PasswordTypeNotMatched";
			}

		}
		if (!newPassword.equals(verifynewPassword)
				|| returntype.equals("INCORRECT")) {
			logger
					.debug("Correct Password not Entered*****************************************");
			addActionError(getText("error.enter.correct.old.pwd")+" !!");
			return "wrongpass";
		}

		if (bean == null) {
			addActionError(getText("error.you.have.to.login.to.change.pwd"));
			return "NOTLOGIN";
		}

		String uname = bean.getUserName();
		logger.debug(session.getAttribute("FIRST") + "****************");
		boolean first = false;
		if (session.getAttribute("FIRST") != null) {
			first = (Boolean) session.getAttribute("FIRST");
		}

		ChangePasswordHelper changepass = new ChangePasswordHelper();
		if (uname != null && password != null && newPassword != null
				&& verifynewPassword != null) {
			if(changepass.verifyPasswordChars(newPassword, isRetailer)){
				returntype = changepass.changePassword(uname, password,
						newPassword, verifynewPassword, isRetailer);
			}else{
				returntype = "PASSWORD INAPPROPRIATE";
			}
		}

		logger
				.debug("***************************************************************************"
						+ returntype);
		if (returntype.equals("ERROR")) {
			addActionError(getText("error.new.pwd.not.verified")+" !!");
			return ERROR;
		} else if (returntype.equals("INPUT") || "NEW_OLD_SAME".equals(returntype)) {
			addActionError(getText("error.you.have.used.this.pwd.recent"));
			return INPUT;
		} else if (returntype.equals("SUCCESS")) {
			if (first) {
				session.setAttribute("FIRST", false);				
				return isRetailer ? "RetFirstSuccess" : "UserFirstSuccess";				
			}
			return "UserSuccess";
		} else if (returntype.equals("wrongpass")
				|| returntype.equals("INCORRECT")) {
			addActionError(getText("error.enter.correct.old.pwd")+" !!");
			return "wrongpass";
		}else if(returntype.equals("PASSWORD INAPPROPRIATE")){
			addActionError(getText("error.pwd.inapp.pwd.should.contain.one.digit.one.lcuc.alpha")+"!!");
		}
		//addActionError("Enter Correct  Old Password");
		return ERROR;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public String getPassword() {
		return password;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public String getVerifynewPassword() {
		return verifynewPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public void setPassword(String value) {
		password = value;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setVerifynewPassword(String verifynewPassword) {
		this.verifynewPassword = verifynewPassword;
	}

}