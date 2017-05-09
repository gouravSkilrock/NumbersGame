package com.skilrock.lms.embedded.scratchService.inventoryMgmt.common;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.scratchService.inventoryMgmt.common.BookWiseInvDetailForRetHelper;

public class ActivateBookAction extends ActionSupport implements ServletResponseAware {
	static Log logger = LogFactory.getLog(ActivateBookAction.class);
	private static final long serialVersionUID = 1L;

	private HttpServletResponse response;
	private String userName;
	private String bookNumber;

	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getBookNumber() {
		return bookNumber;
	}

	public void setBookNumber(String bookNumber) {
		this.bookNumber = bookNumber;
	}

	@SuppressWarnings("unchecked")
	public void activateBook() throws LMSException {
		logger.info("-- Inside activateBook --");
		ServletContext sc = ServletActionContext.getServletContext();
		HttpSession session = null;
		UserInfoBean userBean = null;
		try {
			bookNumber = bookNumber.trim();

			Map currentUserSessionMap = (Map) sc.getAttribute("LOGGED_IN_USERS");
			session = (HttpSession) currentUserSessionMap.get(userName);
			userBean = (UserInfoBean) session.getAttribute("USER_INFO");

			BookWiseInvDetailForRetHelper helper = new BookWiseInvDetailForRetHelper();

			String status = helper.validateBookAndStatus(userBean.getUserOrgId(), bookNumber.replaceAll("-", ""));
			if("ACTIVE".equals(status)) { 
				logger.info("Book Already Active - "+bookNumber);
				response.getOutputStream().write("ErrorMsg:Book Already Active.".getBytes());
				return;
			} else if("CLAIMED".equals(status)) { 
				logger.info("Book Already Active - "+bookNumber);
				response.getOutputStream().write("ErrorMsg:Book Already Active.".getBytes());
				return;
			} else if("MISSING".equals(status)) { 
				logger.info("Book is Missing - "+bookNumber);
				response.getOutputStream().write("ErrorMsg:Book is Missing.".getBytes());
				return;
			} else if("NO_BOOK_FOUND".equals(status)) {
				logger.info("Book Not Found - "+bookNumber);
				response.getOutputStream().write("ErrorMsg:Book Not Found.".getBytes());
				return;
			}

			if(bookNumber.contains("-") && !status.equals(bookNumber)) {
				logger.info("Invalid Book Number - "+bookNumber);
				response.getOutputStream().write("ErrorMsg:Invalid Book Number.".getBytes());
				return;
			}

			List<String> bookList = new ArrayList<String>();
			bookList.add(status);
			//boolean valid = helper.updateBooks(userBean.getUserOrgId(), bookList);
			helper.updateBooks(userBean.getUserOrgId(),userBean.getUserId(), bookList);
			//if (valid) {
				logger.info("Book Activated Successfully - "+bookNumber);
				response.getOutputStream().write("success".getBytes());
			/*} else {
				logger.info("Error in Book Activation - "+bookNumber);
				response.getOutputStream().write("ErrorMsg:Error in Activation.".getBytes());
			}*/
		} catch (IOException e) {
			e.printStackTrace();
			throw new LMSException(e);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
		}
	}
}