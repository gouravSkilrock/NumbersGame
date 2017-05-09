package com.skilrock.lms.coreEngine.messageMgmt;

import java.sql.Connection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.InboxMessageBean;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.messageMgmt.common.InboxMessageMgmtHelper;
import com.skilrock.lms.coreEngine.userMgmt.daoImpl.MessageInboxDaoImpl;
import com.skilrock.lms.coreEngine.userMgmt.javaBeans.MessageInboxBean;

public class MessageUtility {
	private static final Log logger = LogFactory.getLog(MessageUtility.class);

	public static String fetchUserWiseFlashMessagesEmbedded(int msgId, int userId, String userType, Connection connection) throws LMSException {
		List<InboxMessageBean> inboxMessageList = new InboxMessageMgmtHelper().fetchUserWiseMessage(msgId, userId, userType, "TERMINAL", "FLASH", connection);
		StringBuilder flashMessages = new StringBuilder("");
		for(InboxMessageBean bean : inboxMessageList) {
			flashMessages.append(bean.getMessageSubject()).append("~").append(bean.getMessageBody()).append("#");
		}

		logger.info("Flash Messages String - "+flashMessages.toString());
		return flashMessages.toString();
	}

	/*
	public static final String getNewMessageStatusEmbedded(int msgId, int userId, String userType, Connection connection) throws LMSException {
		InboxMessageBean messageBean = new InboxMessageMgmtHelper().getNewMessagesStatus(msgId, userId, userType, "TERMINAL", connection);
		StringBuilder responseData = new StringBuilder();

		int maxMessages = Integer.parseInt(Utility.getPropertyValue("TERMINAL_INBOX_MESSAGE_LIMIT"));
		if(messageBean.getMessageId()>maxMessages)
			messageBean.setMessageId(maxMessages);

		responseData.append(messageBean.getMessageId()).append(",")
			.append(messageBean.getIsPopup()).append(",")
			.append(messageBean.getIsMandatory());

		logger.info("New Message Status Embedded Response Data - "+responseData.toString());
		return responseData.toString();
	}
	*/

	public static void main(String[] args) throws Exception {
		Connection connection = DBConnect.getConnection();
		//System.out.println(fetchUserWiseFlashMessagesEmbedded(0, 11004, connection));
		System.out.println(getNewMessageStatusEmbedded(0, 11004, "RETAILER", connection));
	}

	public static String fetchUserWiseFlashMessagesEmbedded(int msgId, int userId, Connection connection) throws LMSException {
		List<MessageInboxBean> messageList = null;
		StringBuilder flashMessages = null;
		try {
			messageList = MessageInboxDaoImpl.getSingleInstance().fetchTerminalMessages(msgId, userId, "FLASH", connection);
			flashMessages = new StringBuilder("");
			for(MessageInboxBean bean : messageList) {
				flashMessages.append(bean.getMessageSubject()).append("~").append(bean.getMessageContent()).append("#");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		logger.info("Flash Messages String - "+flashMessages.toString());
		return flashMessages.toString();
	}

	public static final String getNewMessageStatusEmbedded(int msgId, int userId, String userType, Connection connection) throws LMSException {
		MessageInboxBean messageBean = null;
		StringBuilder responseData = null;
		try {
			messageBean = MessageInboxDaoImpl.getSingleInstance().getNewMessagesStatus(msgId, userId, userType, "TERMINAL", connection);
			int maxMessages = Integer.parseInt(Utility.getPropertyValue("TERMINAL_INBOX_MESSAGE_LIMIT"));
			if(messageBean.getNewMessageCount()>maxMessages) {
				messageBean.setNewMessageCount(maxMessages);
			}

			responseData = new StringBuilder();
			responseData.append(messageBean.getNewMessageCount()).append(",")
				.append(messageBean.getPopupStatus()).append(",")
				.append(messageBean.getMandatoryStatus());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		logger.info("New Message Status Embedded Response Data - "+responseData.toString());
		return responseData.toString();
	}
}