package com.skilrock.lms.web.loginMgmt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.UserQuesBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.coreEngine.loginMgmt.common.CheckAnsHelper;

/**
 * This class provides methods for check secret answer while forget password to
 * get new password
 * 
 * @author Skilrock Technologies
 * 
 */
public class CheckAnsAction extends ActionSupport implements
		ServletRequestAware {
	static Log logger = LogFactory.getLog(CheckAnsAction.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	 
	// private sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();

	private HttpServletRequest request;

	private String secAnsUser;

	// private List<ForgotPassword>dataList=null;
	// private List<UserQuesBean>Data1=null;
	// private List<UserQuesBean>Data=new ArrayList<UserQuesBean>();

	/**
	 * This method is used to check secret answer and to send password to the
	 * user's email address
	 * 
	 * @return String
	 * @exception Exception
	 */
	@Override
	public String execute() throws Exception {
		String secAns;
		String userName;
		logger.debug("heeeeeeeeeeeeellllllllllpooooooooooooooooo");

		HttpSession session = request.getSession();
		// Data =(List<UserQuesBean>) session.getAttribute("list");

		UserQuesBean userQuesBean = new UserQuesBean();
		userQuesBean = (UserQuesBean) session.getAttribute("UserQuesBean");
		logger.debug("user email is " + userQuesBean.getEmail());
		logger.debug("user name  is " + userQuesBean.getUserName());
		secAns = userQuesBean.getSecAns();
		userName = userQuesBean.getUserName();
		CheckAnsHelper ansHelper = new CheckAnsHelper();

		// null pointer conditions are applied at the time of self review
		// getSecAnsUser()!=""

		if (secAns.equals(getSecAnsUser()) && getSecAnsUser() != "") {

			ansHelper.checkAns(userQuesBean.getUserId(), userQuesBean
					.getUserName(), userQuesBean.getEmail(), userQuesBean
					.getFirstName(), userQuesBean.getLastName());
			addActionError("Your Password is Successfully Sent to Your Email Adress !");
			return SUCCESS;

		}

		addActionError(" Enter Correct Answer ");
		return INPUT;

	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public String getSecAnsUser() {
		return secAnsUser;
	}

	public void setSecAnsUser(String secAnsUser) {
		this.secAnsUser = secAnsUser;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

}