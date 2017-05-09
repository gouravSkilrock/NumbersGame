package com.skilrock.lms.keba.msgMgmt.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.skilrock.lms.beans.InboxMessageBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.messageMgmt.common.InboxMessageMgmtHelper;

public class RetailerInboxMessageAction extends BaseAction {
	private static Log logger = LogFactory
			.getLog(RetailerInboxMessageAction.class);
	private static final long serialVersionUID = 1L;

	private String requestData;

	public RetailerInboxMessageAction() {
		super(RetailerInboxMessageAction.class);
	}

	public String getRequestData() {
		return requestData;
	}

	public void setRequestData(String requestData) {
		this.requestData = requestData;
	}

	JsonObject responseObject = null;

	public void fetchRetailerWiseMessage() throws LMSException, IOException {
		List<InboxMessageBean> inboxMessageList = null;
		InboxMessageMgmtHelper messageHelper = new InboxMessageMgmtHelper();
		boolean isSuccess = false;
		String errorMsg = "";
		PrintWriter out = null;
		try {
			out = response.getWriter();
			response.setContentType("application/json");
			responseObject = new JsonObject();
			JsonObject jsonObject = (JsonObject) new JsonParser()
					.parse(requestData);
			UserInfoBean userBean = getUserBean();
			inboxMessageList = messageHelper.fetchUserWiseMessage(jsonObject
					.get("msgId").getAsInt(), userBean.getUserId(), userBean
					.getUserType(), "TERMINAL", "INBOX");

			if (inboxMessageList == null || inboxMessageList.size() == 0) {
				isSuccess = false;
				errorMsg = "ErrorMsg:No New Message";
			} else {
				responseObject.add("responseData", new JsonParser()
						.parse(new Gson().toJson(inboxMessageList)));
			}
		} catch (LMSException e) {
			isSuccess = false;
			errorMsg = "ErrorMsg:Error!Try Again";
		} catch (Exception e) {
			e.printStackTrace();
			isSuccess = false;
			errorMsg = "ErrorMsg:Error!Try Again";
		}
		responseObject.addProperty("isSuccess", isSuccess);
		responseObject.addProperty("errorMsg", errorMsg);
		out.println(responseObject);
		return;
	}

}