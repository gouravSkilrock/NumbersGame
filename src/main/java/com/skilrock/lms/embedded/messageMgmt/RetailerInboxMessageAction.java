package com.skilrock.lms.embedded.messageMgmt;

import java.io.IOException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.userMgmt.javaBeans.MessageInboxBean;
import com.skilrock.lms.coreEngine.userMgmt.serviceImpl.MessageInboxServiceImpl;

public class RetailerInboxMessageAction extends BaseAction {
	private static Log logger = LogFactory.getLog(RetailerInboxMessageAction.class);
	private static final long serialVersionUID = 1L;

	public RetailerInboxMessageAction() {
		super(RetailerInboxMessageAction.class);
	}

	private String userName;
	private int msgId;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getMsgId() {
		return msgId;
	}

	public void setMsgId(int msgId) {
		this.msgId = msgId;
	}

	public void fetchRetailerWiseMessage() throws LMSException, IOException{
		UserInfoBean userBean = null;
		List<MessageInboxBean> messageList = null;
		StringBuilder responseData = null;
		try {
			userBean = getUserBean(userName);
			responseData = new StringBuilder();

			messageList = MessageInboxServiceImpl.getSingleInstance().fetchTerminalMessages(msgId, userBean.getUserId(), "REGISTRATION");
			messageList.addAll(MessageInboxServiceImpl.getSingleInstance().fetchTerminalMessages(msgId, userBean.getUserId(), "INBOX"));
			if(messageList == null || messageList.size()==0) {
				responseData.append("ErrorMsg:No New Message");
			} else {
				for(MessageInboxBean messageBean : messageList) {
					responseData.append(messageBean.getMessageId()).append("|");
					responseData.append(messageBean.getMessageDate()).append("|");
					responseData.append(messageBean.getMessageSubject()).append("|");
					responseData.append(messageBean.getMessageContent()).append("|");
					responseData.append(messageBean.getExpiryTimeInSec()).append("|");
					responseData.append(messageBean.getCreatorUserName()).append("|");
					responseData.append(messageBean.getMandatoryStatus()).append("#");
				}
				responseData.deleteCharAt(responseData.length()-1);
			}
			logger.info("Fetch Message Response - "+responseData.toString());
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html");
			response.getOutputStream().write(responseData.toString().getBytes());
			return;
		} catch (IOException e) {
			e.printStackTrace();
			try {
				response.getOutputStream().write("ErrorMsg:Error!Try Again".getBytes());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return;
		} catch (Exception e) {
			e.printStackTrace();
			try {
				response.getOutputStream().write("ErrorMsg:Error!Try Again".getBytes());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return;
		}
	}
}