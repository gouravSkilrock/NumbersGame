package com.skilrock.lms.embedded.drawGames.playMgmt;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.utility.ResponsibleGaming;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameOfflineHelper;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.OfflineRetSaleHelper;
import com.skilrock.lms.coreEngine.userMgmt.common.CommonFunctionsHelper;
import com.skilrock.lms.dge.beans.FileUploadBean;
import com.skilrock.lms.embedded.drawGames.common.EmbeddedErrors;
import com.skilrock.lms.web.drawGames.common.Util;

public class DrawGameOfflineAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	static Log logger = LogFactory.getLog(DrawGameOfflineAction.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private File[] file;
	private String[] fileName;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private int userId;
	private String userName;

	public File[] getFile() {
		return file;
	}

	public void getFileContents() {
		System.out.println(new Date() + "before Upload------"
				+ new Date().getTime());
		ServletContext sc = ServletActionContext.getServletContext();
		String isDraw = (String) sc.getAttribute("IS_DRAW");
		try {
			if (isDraw.equalsIgnoreCase("NO")) {
				response.getOutputStream().write(
						("ErrorMsg:" + EmbeddedErrors.DRAW_GAME_NOT_AVAILABLE)
								.getBytes());
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Map currentUserSessionMap = (Map) sc.getAttribute("LOGGED_IN_USERS");
		if (currentUserSessionMap == null) {
			try {
				response
						.getOutputStream()
						.write(
								("ErrorMsg:" + EmbeddedErrors.SESSION_EXPIRED + "ErrorCode:01|")
										.getBytes());
				return;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		HttpSession session = (HttpSession) currentUserSessionMap.get(userName);
		if (!CommonFunctionsHelper.isSessionValid(session)) {
			try {
				response
						.getOutputStream()
						.write(
								("ErrorMsg:" + EmbeddedErrors.SESSION_EXPIRED + "ErrorCode:01|")
										.getBytes());
				return;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		int uploadedBy = userInfoBean.getUserId();

		if (userInfoBean.getUserType().equalsIgnoreCase("AGENT")
				|| userInfoBean.getUserType().equalsIgnoreCase("BO")) {
			// int userId = Integer.parseInt(fileName[0].split("_")[0]);
			int userId = fileName[0].contains("_") ? Integer
					.parseInt(fileName[0].split("_")[0]) : Integer
					.parseInt(fileName[0].substring(0, 4));
			if (!DrawGameOfflineHelper.checkOfflineUser(userId)) {
				try {
					response
							.getOutputStream()
							.write(
									("ErrorMsg:" + EmbeddedErrors.FILE_UPLOAD_INVALID_USERID)
											.getBytes());
					return;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			userInfoBean = new UserInfoBean();
			userInfoBean.setUserId(userId);
			userInfoBean = DrawGameOfflineHelper.fillUserBeanData(userInfoBean);
		}
		List<FileUploadBean> beanList = new ArrayList<FileUploadBean>();
		FileUploadBean bean = null;
		String result = null;

		if (file.length == fileName.length) { // Validate equal fileName and
			// files in post data
			for (int i = 0; i < file.length; i++) {
				bean = new FileUploadBean();
				int gameNo = Util.getOfflineFileGameNo(fileName[i]);
				bean.setGameNo(gameNo);
				bean.setGameId(Util.getGameIdFromGameNumber(gameNo));
				bean.setRetailerOrgId(userInfoBean.getUserOrgId());
				bean.setRetailerUserId(userInfoBean.getUserId());
				bean.setFileName(fileName[i]);
				bean.setFile(file[i]);
				beanList.add(bean);
			}
			Map<Integer, Map<Integer, String>> drawIdTableMap = (Map<Integer, Map<Integer, String>>) sc
					.getAttribute("drawIdTableMap");
			// DrawGameOfflineHelper helper = new DrawGameOfflineHelper();
			// result = helper.insertFileInDB(beanList,
			// userInfoBean,drawIdTableMap, uploadedBy);
			OfflineRetSaleHelper helper = new OfflineRetSaleHelper();
			result = helper.processOfflineFile(beanList, userInfoBean,
					drawIdTableMap, uploadedBy, refMerchantId, "LMS_Terminal");

			if (result.contains("UPLOADED") || result.contains("ERROR")
					|| result.contains("LATE_UPLOAD")) {
				/*
				 * MailSend mailSend = new MailSend("lms.error@skilrock.com",
				 * result);
				 */
				int errCount = 0;
				int lateUploadCount = 0;
				int idx = 0;
				while ((idx = result.indexOf("LATE_UPLOAD", idx)) != -1) {
					idx++;
					lateUploadCount++;
				}
				idx = 0;
				while ((idx = result.indexOf("ERROR", idx)) != -1) {
					idx++;
					errCount++;
				}
				boolean isFraudLateUpload = ResponsibleGaming.respGaming(
						userInfoBean, "DG_OFFLINE_LATEUPLOAD", lateUploadCount
								+ "");
				boolean isFraudErrorFile = ResponsibleGaming.respGaming(
						userInfoBean, "DG_OFFLINE_ERRORFILE", errCount + "");
				logger.debug("isFraud is "
						+ (isFraudLateUpload && isFraudErrorFile));
				DrawGameOfflineHelper.setInactiveRetailer(userInfoBean
						.getUserId());
			} else if ("".equalsIgnoreCase(result)) {
				for (int i = 0; i < file.length; i++) {
					result = fileName[i] + ":FILE:ERROR|";
				}
			}
			System.out.println(new Date() + "after Upload------"
					+ new Date().getTime() + " Result :: " + result);
			logger.debug(new Date() + "after Upload------"
					+ new Date().getTime() + " Result :: " + result);
			try {
				response.getOutputStream().write(result.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				response.getOutputStream().write(
						("ErrorMsg:" + EmbeddedErrors.FILE_UPLOAD_INVALID_DATA)
								.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public String[] getFileName() {
		return fileName;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public int getUserId() {
		return userId;
	}

	/*
	 * public void getFileContents() { logger.debug(new Date() + "before
	 * Upload------" + new Date().getTime()); System.out.println(new Date() +
	 * "before Upload------" + new Date().getTime()); ServletContext sc =
	 * ServletActionContext.getServletContext(); String isDraw = (String)
	 * sc.getAttribute("IS_DRAW");
	 * 
	 * Map currentUserSessionMap = (Map) sc.getAttribute("LOGGED_IN_USERS");
	 * System.out.println(userName + " LOGGED_IN_USERS maps is " +
	 * currentUserSessionMap);
	 * 
	 * HttpSession session = (HttpSession) currentUserSessionMap.get(userName);
	 * 
	 * UserInfoBean userInfoBean = (UserInfoBean) session
	 * .getAttribute("USER_INFO"); try { if (isDraw.equalsIgnoreCase("NO")) {
	 * response.getOutputStream().write( "Draw Game Not Avaialbe".getBytes());
	 * return; } } catch (Exception e) { e.printStackTrace(); }
	 * 
	 * List<FileUploadBean> beanList = new ArrayList<FileUploadBean>();
	 * FileUploadBean bean = null; String result = null; if (file.length ==
	 * fileName.length) { //Validate equal fileName and files in post data for
	 * (int i = 0; i < file.length; i++) { bean = new FileUploadBean(); int
	 * gameNo = Integer.parseInt(fileName[i].split("_")[1]);
	 * bean.setGameNo(gameNo); bean.setGameId(Util.getGameId(gameNo));
	 * bean.setRetailerOrgId(userInfoBean.getUserOrgId());
	 * bean.setRetailerUserId(userInfoBean.getUserId());
	 * bean.setFileName(fileName[i]); bean.setFile(file[i]); beanList.add(bean); }
	 * Map<Integer, Map<Integer, String>> drawIdTableMap = (Map<Integer, Map<Integer,
	 * String>>) sc .getAttribute("drawIdTableMap"); DrawGameOfflineHelper
	 * helper = new DrawGameOfflineHelper(); result =
	 * helper.insertFileInDB(beanList, userInfoBean, drawIdTableMap);
	 * if(result.contains("UPLOADED")||result.contains("ERROR")||result.contains("LATE_UPLOAD")){
	 * MailSend mailSend=new MailSend("lms.error@skilrock.com",result);
	 * DrawGameOfflineHelper.setInactiveRetailer(userInfoBean.getUserId()); }
	 * System.out.println(new Date() + "after Upload------" + new
	 * Date().getTime() + " Result :: " + result); logger.debug(new Date() +
	 * "after Upload------" + new Date().getTime() + " Result :: " + result);
	 * try { response.getOutputStream().write(result.getBytes()); } catch
	 * (IOException e) { e.printStackTrace(); } } else { try {
	 * response.getOutputStream().write("Please send proper data".getBytes()); }
	 * catch (IOException e) { e.printStackTrace(); } } }
	 */

	public String getUserName() {
		return userName;
	}

	public void setAFUTime() {
		ServletContext sc = ServletActionContext.getServletContext();
		String isDraw = (String) sc.getAttribute("IS_DRAW");

		Map currentUserSessionMap = (Map) sc.getAttribute("LOGGED_IN_USERS");
		if (currentUserSessionMap == null) {
			try {
				response
						.getOutputStream()
						.write(
								("ErrorMsg:" + EmbeddedErrors.SESSION_EXPIRED + "ErrorCode:01|")
										.getBytes());
				return;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		/*
		 * System.out.println(userName + " LOGGED_IN_USERS maps is " +
		 * currentUserSessionMap);
		 */

		HttpSession session = (HttpSession) currentUserSessionMap.get(userName);
		if (!CommonFunctionsHelper.isSessionValid(session)) {
			try {
				response
						.getOutputStream()
						.write(
								("ErrorMsg:" + EmbeddedErrors.SESSION_EXPIRED + "ErrorCode:01|")
										.getBytes());
				return;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		if (userInfoBean.getUserType().equalsIgnoreCase("AGENT")
				|| userInfoBean.getUserType().equalsIgnoreCase("BO")) {
			if (userId == 0) {
				try {
					response
							.getOutputStream()
							.write(
									("ErrorMsg:" + EmbeddedErrors.FILE_UPLOAD_INVALID_USERID)
											.getBytes());
					return;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			userInfoBean = new UserInfoBean();
			userInfoBean.setUserId(userId);
			userInfoBean = DrawGameOfflineHelper.fillUserBeanData(userInfoBean);
		}
		try {
			if (isDraw.equalsIgnoreCase("NO")) {
				response.getOutputStream().write(
						("ErrorMsg:" + EmbeddedErrors.DRAW_GAME_NOT_AVAILABLE)
								.getBytes());
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		String result = DrawGameOfflineHelper.setAFUTime(userInfoBean, sc);

		try {
			response.getOutputStream().write(result.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setFile(File[] file) {
		this.file = file;
	}

	public void setFileName(String[] fileName) {
		this.fileName = fileName;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;

	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
