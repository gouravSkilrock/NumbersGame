package com.skilrock.lms.web.loginMgmt;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.ServicesBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.ConfigurationVariables;
import com.skilrock.lms.controller.userMgmtController.UserMgmtController;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameOfflineHelper;
import com.skilrock.lms.rest.common.TransactionManager;
import com.skilrock.lms.sportsLottery.common.NotifySLE;
import com.skilrock.lms.sportsLottery.common.SLE;
import com.skilrock.lms.sportsLottery.userMgmt.javaBeans.UserDataBean;

/**
 * <p>
 * This class checks the userId and password and solves the authentication
 * purpose.
 * </p>
 */
public class Logout extends ActionSupport implements ServletRequestAware {

	static Log logger = LogFactory.getLog(Logout.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HttpServletRequest request;
	private String varRolePage;
	private String wrapperURL ;

	public HttpServletRequest getRequest() {
		return request;
	}

	public String getVarRolePage() {
		return varRolePage;
	}

	private void loggedOutUser(String user) {

		if (ConfigurationVariables.USER_RELOGIN_SESSION_TERMINATE) {
			ServletContext sc = ServletActionContext.getServletContext();
			Map currentUserSessionMap = (Map) sc
					.getAttribute("LOGGED_IN_USERS");
			currentUserSessionMap.remove(user);
			sc.setAttribute("LOGGED_IN_USERS", currentUserSessionMap);
			System.out.println(" LOGGED_IN_USERS maps is " + currentUserSessionMap);
		} else {
			ServletContext sc = ServletActionContext.getServletContext();
			List<String> currentUserList = (List<String>) sc
					.getAttribute("LOGGED_IN_USERS");
			currentUserList.remove(user);
			sc.setAttribute("LOGGED_IN_USERS", currentUserList);
			System.out.println(" LOGGED_IN_USERS maps is " + currentUserList);
		}
	}

	public String logOut() throws Exception {
		logger.debug("I am in Logout");

		HttpSession session = null;
		session = getRequest().getSession();
		ServletContext sc = ServletActionContext.getServletContext();
		wrapperURL = (String)sc.getAttribute("WRAPPER_LOGOUT") ;
		logger.info("Wrapper Logout : "+ wrapperURL);
		session.setAttribute("id", null);
		session.setAttribute("varRole", null);
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");

		if (userBean != null) {
			loggedOutUser(userBean.getUserName());
			if("RETAILER".equalsIgnoreCase(userBean.getUserType()))
					DrawGameOfflineHelper.updateLogoutSuccess(userBean.getUserName());
		}
		//wrapperURL = "http://localhost:8080/LMSWrapper/" ;
		UserMgmtController.getInstance().updateUserLogout(session.getId());
		TransactionManager.setResponseData("true");
		session.removeAttribute("USER_INFO");
		session.removeAttribute("ACTION_LIST");
		session.removeAttribute("PRIV_LIST");
		session.invalidate();
		session = null;
		logger.debug("Log out Successfully and Session is " + session);

		if(userBean != null) {
			if(ServicesBean.isSLE()) {
				if(ServicesBean.isSLE()) {
					UserDataBean dataBean = UserMgmtController.getInstance().getUserInfo(userBean.getUserName().trim());
					//UserDataBean dataBean = new UserDataBean();
					dataBean.setSessionId(null);
	
					NotifySLE notifySLE = new NotifySLE(SLE.Activity.NOTIFY_ON_LOGOUT, dataBean);
					notifySLE.start();
				}
			}
		}

		return wrapperURL == null ? SUCCESS : "WRAPPER_REDIRECT";
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setVarRolePage(String varRolePage) {
		this.varRolePage = varRolePage;
	}

	public String getWrapperURL() {
		return wrapperURL;
	}

	public void setWrapperURL(String wrapperURL) {
		this.wrapperURL = wrapperURL;
	}
	
	

}