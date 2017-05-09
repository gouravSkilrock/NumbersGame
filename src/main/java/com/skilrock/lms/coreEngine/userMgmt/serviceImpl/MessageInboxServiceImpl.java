package com.skilrock.lms.coreEngine.userMgmt.serviceImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.GenericException;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.coreEngine.userMgmt.daoImpl.MessageInboxDaoImpl;
import com.skilrock.lms.coreEngine.userMgmt.javaBeans.MessageInboxBean;

public class MessageInboxServiceImpl {

	private static MessageInboxServiceImpl singleInstance;

	private MessageInboxServiceImpl(){}

	public static MessageInboxServiceImpl getSingleInstance() {
		if (singleInstance == null) {
			synchronized (MessageInboxServiceImpl.class) {
				if (singleInstance == null) {
					singleInstance = new MessageInboxServiceImpl();
				}
			}
		}

		return singleInstance;
	}

	public List<String> getActiveUserType() throws GenericException {
		List<String> userTypeList = null;
		Connection connection = null;
		try {
			connection = DBConnect.getConnection();
			userTypeList = MessageInboxDaoImpl.getSingleInstance().getActiveUserType(connection);
		} catch (SQLException e) {
			throw new GenericException(LMSErrors.SQL_EXCEPTION_CODE, e);
		} catch (Exception e) {
			throw new GenericException(LMSErrors.GENERAL_EXCEPTION_CODE, e);
		} finally {
			DBConnect.closeCon(connection);
		}

		return userTypeList;
	}

	public List<String> getActiveInterfaceType(String userType) throws GenericException {
		List<String> interfaceTypeList = null;
		Connection connection = null;
		try {
			connection = DBConnect.getConnection();
			interfaceTypeList = MessageInboxDaoImpl.getSingleInstance().getActiveInterfaceType(userType, connection);
		} catch (SQLException e) {
			throw new GenericException(LMSErrors.SQL_EXCEPTION_CODE, e);
		} catch (Exception e) {
			throw new GenericException(LMSErrors.GENERAL_EXCEPTION_CODE, e);
		} finally {
			DBConnect.closeCon(connection);
		}

		return interfaceTypeList;
	}

	public Map<Integer, String> getActiveModes(String userType, String interfaceType) throws GenericException {
		Map<Integer, String> modeMap = null;
		Connection connection = null;
		try {
			connection = DBConnect.getConnection();
			modeMap = MessageInboxDaoImpl.getSingleInstance().getActiveModes(userType, interfaceType, connection);
		} catch (SQLException e) {
			throw new GenericException(LMSErrors.SQL_EXCEPTION_CODE, e);
		} catch (Exception e) {
			throw new GenericException(LMSErrors.GENERAL_EXCEPTION_CODE, e);
		} finally {
			DBConnect.closeCon(connection);
		}

		return modeMap;
	}

	public void addNewMessage(MessageInboxBean messageBean, String[] organizationList) throws GenericException {
		Connection connection = null;
		try {
			connection = DBConnect.getConnection();
			connection.setAutoCommit(false);

			MessageInboxDaoImpl.getSingleInstance().addNewMessage(messageBean, organizationList, connection);
			if(!(messageBean.getIsForNewUser()==null || "-1".equals(messageBean.getIsForNewUser()))) {
				int messageTypeId = messageBean.getMessageTypeId();
				String messageType = messageBean.getMessageType();
				messageBean.setMessageTypeId(Integer.parseInt(messageBean.getIsForNewUser()));
				messageBean.setMessageType("REGISTRATION");
				MessageInboxDaoImpl.getSingleInstance().addNewMessage(messageBean, organizationList, connection);
				messageBean.setMessageTypeId(messageTypeId);
				messageBean.setMessageType(messageType);
			}
			connection.commit();
		} catch (SQLException e) {
			throw new GenericException(LMSErrors.SQL_EXCEPTION_CODE, e);
		} catch (Exception e) {
			throw new GenericException(LMSErrors.GENERAL_EXCEPTION_CODE, e);
		} finally {
			DBConnect.closeCon(connection);
		}
	}

	public void addRegistrationMessage(int userId, String userType, String interfaceType) throws GenericException {
		Connection connection = null;
		try {
			connection = DBConnect.getConnection();
			connection.setAutoCommit(false);

			MessageInboxDaoImpl.getSingleInstance().addRegistrationMessage(userId, userType, interfaceType, connection);
			connection.commit();
		} catch (SQLException e) {
			throw new GenericException(LMSErrors.SQL_EXCEPTION_CODE, e);
		} catch (Exception e) {
			throw new GenericException(LMSErrors.GENERAL_EXCEPTION_CODE, e);
		} finally {
			DBConnect.closeCon(connection);
		}
	}

	public int getMessageCount(String messageType, String userType, String interfaceType) throws GenericException {
		int messageCount = 0;
		Connection connection = null;
		try {
			connection = DBConnect.getConnection();

			messageCount = MessageInboxDaoImpl.getSingleInstance().getMessageCount(messageType, userType, interfaceType, connection);
		} catch (SQLException e) {
			throw new GenericException(LMSErrors.SQL_EXCEPTION_CODE, e);
		} catch (Exception e) {
			throw new GenericException(LMSErrors.GENERAL_EXCEPTION_CODE, e);
		} finally {
			DBConnect.closeCon(connection);
		}

		return messageCount;
	}

	public int getTotalNoOfMessages(int userId, String userType, List<String> statusList) throws GenericException {
		int noOfMessages = 0;
		Connection connection = null;
		try {
			connection = DBConnect.getConnection();

			noOfMessages = MessageInboxDaoImpl.getSingleInstance().getTotalNoOfMessages(userId, userType, statusList, connection);
		} catch (SQLException e) {
			throw new GenericException(LMSErrors.SQL_EXCEPTION_CODE, e);
		} catch (Exception e) {
			throw new GenericException(LMSErrors.GENERAL_EXCEPTION_CODE, e);
		} finally {
			DBConnect.closeCon(connection);
		}

		return noOfMessages;
	}

	public List<MessageInboxBean> fetchWebMessages(int userId, String userType, List<String> statusList, int messageNumber) throws GenericException {
		List<MessageInboxBean> messageList = null;
		Connection connection = null;
		try {
			connection = DBConnect.getConnection();

			messageList = MessageInboxDaoImpl.getSingleInstance().fetchWebMessages(userId, userType, statusList, messageNumber, connection);
		} catch (SQLException e) {
			throw new GenericException(LMSErrors.SQL_EXCEPTION_CODE, e);
		} catch (Exception e) {
			throw new GenericException(LMSErrors.GENERAL_EXCEPTION_CODE, e);
		} finally {
			DBConnect.closeCon(connection);
		}

		return messageList;
	}

	public MessageInboxBean fetchWebMessageDetail(int messageId, int userId) throws GenericException {
		MessageInboxBean messageBean = null;
		Connection connection = null;
		try {
			connection = DBConnect.getConnection();

			messageBean = MessageInboxDaoImpl.getSingleInstance().fetchWebMessageDetail(messageId, userId, connection);
		} catch (SQLException e) {
			throw new GenericException(LMSErrors.SQL_EXCEPTION_CODE, e);
		} catch (Exception e) {
			throw new GenericException(LMSErrors.GENERAL_EXCEPTION_CODE, e);
		} finally {
			DBConnect.closeCon(connection);
		}

		return messageBean;
	}

	public List<MessageInboxBean> searchWebMessages(int userId, String userType, String text) throws GenericException {
		List<MessageInboxBean> messageList = null;
		Connection connection = null;
		try {
			connection = DBConnect.getConnection();

			messageList = MessageInboxDaoImpl.getSingleInstance().searchWebMessages(userId, userType, text, connection);
		} catch (SQLException e) {
			throw new GenericException(LMSErrors.SQL_EXCEPTION_CODE, e);
		} catch (Exception e) {
			throw new GenericException(LMSErrors.GENERAL_EXCEPTION_CODE, e);
		} finally {
			DBConnect.closeCon(connection);
		}

		return messageList;
	}

	public void updateMessageStatus(int messageId, String status) throws GenericException {
		Connection connection = null;
		try {
			connection = DBConnect.getConnection();
			connection.setAutoCommit(false);

			MessageInboxDaoImpl.getSingleInstance().updateMessageStatus(messageId, status, connection);
			connection.commit();
		} catch (SQLException e) {
			throw new GenericException(LMSErrors.SQL_EXCEPTION_CODE, e);
		} catch (Exception e) {
			throw new GenericException(LMSErrors.GENERAL_EXCEPTION_CODE, e);
		} finally {
			DBConnect.closeCon(connection);
		}
	}

	public void updateUserMessageStatus(List<Integer> messageList, String status) throws GenericException {
		Connection connection = null;
		try {
			connection = DBConnect.getConnection();
			connection.setAutoCommit(false);

			MessageInboxDaoImpl.getSingleInstance().updateUserMessageStatus(messageList, status, connection);
			connection.commit();
		} catch (SQLException e) {
			throw new GenericException(LMSErrors.SQL_EXCEPTION_CODE, e);
		} catch (Exception e) {
			throw new GenericException(LMSErrors.GENERAL_EXCEPTION_CODE, e);
		} finally {
			DBConnect.closeCon(connection);
		}
	}

	public List<MessageInboxBean> getMessageListForEdit(int messageTypeId) throws GenericException {
		List<MessageInboxBean> messageList = null;
		Connection connection = null;
		try {
			connection = DBConnect.getConnection();

			messageList = MessageInboxDaoImpl.getSingleInstance().getMessageListForEdit(messageTypeId, connection);
		} catch (SQLException e) {
			throw new GenericException(LMSErrors.SQL_EXCEPTION_CODE, e);
		} catch (Exception e) {
			throw new GenericException(LMSErrors.GENERAL_EXCEPTION_CODE, e);
		} finally {
			DBConnect.closeCon(connection);
		}

		return messageList;
	}

	public List<MessageInboxBean> fetchTerminalMessages(int msgId, int userId, String messageType) throws GenericException {
		List<MessageInboxBean> messageList = null;
		Connection connection = null;
		try {
			connection = DBConnect.getConnection();

			messageList = MessageInboxDaoImpl.getSingleInstance().fetchTerminalMessages(msgId, userId, messageType, connection);
		} catch (SQLException e) {
			throw new GenericException(LMSErrors.SQL_EXCEPTION_CODE, e);
		} catch (Exception e) {
			throw new GenericException(LMSErrors.GENERAL_EXCEPTION_CODE, e);
		} finally {
			DBConnect.closeCon(connection);
		}

		return messageList;
	}
}