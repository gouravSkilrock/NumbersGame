package com.skilrock.lms.coreEngine.userMgmt.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.GenericException;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.coreEngine.userMgmt.javaBeans.MessageInboxBean;
import com.skilrock.lms.web.drawGames.common.Util;

public class MessageInboxDaoImpl {
	private static final Log logger = LogFactory.getLog(MessageInboxDaoImpl.class);

	private static MessageInboxDaoImpl singleInstance;

	private MessageInboxDaoImpl(){}

	public static MessageInboxDaoImpl getSingleInstance() {
		if (singleInstance == null) {
			synchronized (MessageInboxDaoImpl.class) {
				if (singleInstance == null) {
					singleInstance = new MessageInboxDaoImpl();
				}
			}
		}

		return singleInstance;
	}

	 public List<String> getActiveUserType(Connection connection) throws SQLException {
		List<String> userTypeList = new ArrayList<String>();
		Statement stmt = connection.createStatement();
		String query = "SELECT SQL_CACHE user_type FROM st_lms_user_message_type_master WHERE status='ACTIVE' GROUP BY user_type;";
		logger.info("getActiveUserType - "+query);
		ResultSet rs = stmt.executeQuery(query);
		while(rs.next()) {
			userTypeList.add(rs.getString("user_type"));
		}

		return userTypeList;
	}

	public List<String> getActiveInterfaceType(String userType, Connection connection) throws SQLException {
		List<String> interfaceTypeList = new ArrayList<String>();
		Statement stmt = connection.createStatement();
		String query = "SELECT DISTINCT interface_type FROM st_lms_user_message_type_master WHERE user_type='"+userType+"' AND status='ACTIVE';";
		logger.info("getActiveInterfaceType - "+query);
		ResultSet rs = stmt.executeQuery(query);
		while(rs.next()) {
			interfaceTypeList.add(rs.getString("interface_type"));
		}

		return interfaceTypeList;
	}

	public Map<Integer, String> getActiveModes(String userType, String interfaceType, Connection connection) throws SQLException {
		Map<Integer, String> modeMap = new HashMap<Integer, String>();
		Statement stmt = connection.createStatement();
		String query = "SELECT DISTINCT type_dev_name, message_type_id FROM st_lms_user_message_type_master WHERE user_type='"+userType+"' AND interface_type='"+interfaceType+"' AND STATUS='ACTIVE';";
		logger.info("getActiveModes - "+query);
		ResultSet rs = stmt.executeQuery(query);
		while(rs.next()) {
			modeMap.put(rs.getInt("message_type_id"), rs.getString("type_dev_name"));
		}

		return modeMap;
	}

	public void addNewMessage(MessageInboxBean messageBean, String[] organizationList, Connection connection) throws SQLException {
		int messageId = 0;
		PreparedStatement pstmt = connection.prepareStatement("INSERT INTO st_lms_user_message_detail (message_type_id, message_subject, message_body, message_date, expiry_period, is_popup, is_mandatory, creator_user_id, status) VALUES (?,?,?,?,?,?,?,?,?);", PreparedStatement.RETURN_GENERATED_KEYS);
		pstmt.setInt(1, messageBean.getMessageTypeId());
		pstmt.setString(2, messageBean.getMessageSubject());
		pstmt.setString(3, messageBean.getMessageContent());
		pstmt.setTimestamp(4, Util.getCurrentTimeStamp());
		pstmt.setTimestamp(5, messageBean.getExpiryPeriod());
		pstmt.setString(6, (messageBean.getIsPopup())?"YES":"NO");
		pstmt.setString(7, (messageBean.getIsMandatory())?"YES":"NO");
		pstmt.setInt(8, messageBean.getUserId());
		pstmt.setString(9, "ACTIVE");
		logger.info("INSERT IN st_lms_user_message_detail - "+pstmt);
		pstmt.executeUpdate();

		if(!"REGISTRATION".equals(messageBean.getMessageType())) {
			ResultSet rs = pstmt.getGeneratedKeys();
			if(rs.next()) {
				messageId = rs.getInt(1);
			}

			if("ALL".equals(messageBean.getUserSelection())) {
				String appender = "";
				if("BO".equals(messageBean.getMessageFor())) {
					appender = "SELECT ?, user_id, ?, ? FROM st_lms_user_master WHERE organization_type='BO' AND STATUS='ACTIVE' AND parent_user_id<>0;";
				} else if("AGENT".equals(messageBean.getMessageFor())) {
					appender = "SELECT ?, user_id, ?, ? FROM st_lms_user_master WHERE organization_type='AGENT' AND STATUS='ACTIVE' AND isrolehead='y';";
				} else if("RETAILER".equals(messageBean.getMessageFor())) {
					appender = "SELECT ?, user_id, ?, ? FROM st_lms_user_master WHERE organization_type='RETAILER' AND STATUS='ACTIVE';";
				}
				pstmt = connection.prepareStatement("INSERT INTO st_lms_user_inbox_message_mapping (message_id, user_id, send_date, status) "+appender);
				pstmt.setInt(1, messageId);
				pstmt.setTimestamp(2, Util.getCurrentTimeStamp());
				pstmt.setString(3, "UNREAD");
				logger.info("Insert in st_lms_user_inbox_message_mapping - "+pstmt);
				pstmt.executeUpdate();
			} else {
				pstmt = connection.prepareStatement("INSERT INTO st_lms_user_inbox_message_mapping (message_id, user_id, send_date, status) VALUES (?,?,?,?);");
				for(String organization : organizationList) {
					int userId = Integer.parseInt(organization.split("~")[0]);
					pstmt.setInt(1, messageId);
					pstmt.setInt(2, userId);
					pstmt.setTimestamp(3, Util.getCurrentTimeStamp());
					pstmt.setString(4, "UNREAD");
					pstmt.addBatch();
				}
				pstmt.executeBatch();
			}
		}
	}

	public void addRegistrationMessage(int userId, String userType, String interfaceType, Connection connection) throws SQLException {
		String appender = "";
		if("TERMINAL".equals(interfaceType)) {
			appender = " AND expiry_period>='"+Util.getCurrentTimeString()+"'";
		}

		PreparedStatement pstmt = connection.prepareStatement("INSERT INTO st_lms_user_inbox_message_mapping (message_id, user_id, send_date, STATUS) SELECT message_id, ?, ?, 'UNREAD' FROM st_lms_user_message_detail WHERE message_type_id=(SELECT message_type_id FROM st_lms_user_message_type_master WHERE type_dev_name='REGISTRATION' AND user_type=? AND interface_type=? AND STATUS='ACTIVE')"+appender+" AND STATUS='ACTIVE' ORDER BY message_date DESC;");
		pstmt.setInt(1, userId);
		pstmt.setTimestamp(2, Util.getCurrentTimeStamp());
		pstmt.setString(3, userType);
		pstmt.setString(4, interfaceType);
		logger.info("Insert in st_lms_user_inbox_message_mapping - "+pstmt);
		pstmt.executeUpdate();
	}

	public int getMessageCount(String messageType, String userType, String interfaceType, Connection connection) throws SQLException {
		int noOfMessages = 0;
		Statement stmt = connection.createStatement();
		String query = "SELECT no_of_messages FROM st_lms_user_message_type_master WHERE type_dev_name='"+messageType+"' AND user_type='"+userType+"' AND interface_type='"+interfaceType+"';";
		logger.info("getNoOfMessages - "+query);
		ResultSet rs = stmt.executeQuery(query);
		if(rs.next()) {
			noOfMessages = rs.getInt("no_of_messages");
		}
		logger.info("No of Message Limit - "+noOfMessages);

		return noOfMessages;
	}

	public int getTotalNoOfMessages(int userId, String userType, List<String> statusList, Connection connection) throws SQLException {
		int noOfMessages = 0;
		String appender = null;
		if(statusList.contains("'IMPORTANT'")) {
			appender = "AND imm.status IN ('READ', 'UNREAD') AND md.is_mandatory='YES'"; 
		} else {
			appender = "AND imm.status IN "+statusList.toString().replace("[", "(").replace("]", ")");
		}

		Statement stmt = connection.createStatement();
		String query = "SELECT COUNT(*) noOfMessages FROM st_lms_user_message_detail md INNER JOIN st_lms_user_inbox_message_mapping imm ON md.message_id=imm.message_id INNER JOIN st_lms_user_message_type_master mtm ON md.message_type_id=mtm.message_type_id INNER JOIN st_lms_user_master um ON md.creator_user_id=um.user_id INNER JOIN st_lms_organization_master om ON om.organization_id=um.organization_id WHERE md.status='ACTIVE' "+appender+" AND mtm.interface_type='WEB' AND is_inbox_message='YES' AND imm.user_id="+userId+";";
		logger.info("fetchWebMessages - "+query);
		ResultSet rs = stmt.executeQuery(query);
		if(rs.next()) {
			noOfMessages = rs.getInt("noOfMessages");
		}

		return noOfMessages;
	}

	public List<MessageInboxBean> fetchWebMessages(int userId, String userType, List<String> statusList, int messageNumber, Connection connection) throws SQLException {
		String appender = null;
		if(statusList.contains("'IMPORTANT'")) {
			appender = "AND imm.status IN ('READ', 'UNREAD') AND md.is_mandatory='YES'"; 
		} else {
			appender = "AND imm.status IN "+statusList.toString().replace("[", "(").replace("]", ")");
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<MessageInboxBean> messageList = new ArrayList<MessageInboxBean>();
		MessageInboxBean messageBean = null;
		Statement stmt = connection.createStatement();
		int noOfMessages = getMessageCount("INBOX", userType, "WEB", connection);
		String query = "SELECT inbox_message_id, name, message_subject, message_body, is_mandatory, send_date, imm.status FROM st_lms_user_message_detail md INNER JOIN st_lms_user_inbox_message_mapping imm ON md.message_id=imm.message_id INNER JOIN st_lms_user_message_type_master mtm ON md.message_type_id=mtm.message_type_id INNER JOIN st_lms_user_master um ON md.creator_user_id=um.user_id INNER JOIN st_lms_organization_master om ON om.organization_id=um.organization_id WHERE md.status='ACTIVE' "+appender+" AND mtm.interface_type='WEB' AND is_inbox_message='YES' AND imm.user_id="+userId+" ORDER BY send_date DESC LIMIT "+messageNumber+", "+noOfMessages+";";
		logger.info("fetchWebMessages - "+query);
		ResultSet rs = stmt.executeQuery(query);
		while(rs.next()) {
			messageBean = new MessageInboxBean();
			messageBean.setMessageId(rs.getInt("inbox_message_id"));
			messageBean.setCreatorUserName(rs.getString("name"));
			messageBean.setMessageSubject(rs.getString("message_subject"));
			messageBean.setMessageContent(rs.getString("message_body"));
			messageBean.setMandatoryStatus(rs.getString("is_mandatory"));
			messageBean.setMessageDate(dateFormat.format(rs.getTimestamp("send_date")));
			messageBean.setStatus(rs.getString("status"));
			messageList.add(messageBean);
		}

		return messageList;
	}

	public MessageInboxBean fetchWebMessageDetail(int messageId, int userId, Connection connection) throws SQLException {
		MessageInboxBean messageBean = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Statement stmt = connection.createStatement();
		String query = "SELECT inbox_message_id, message_subject, message_body, is_mandatory, send_date, name FROM st_lms_user_message_detail md INNER JOIN st_lms_user_inbox_message_mapping imm ON md.message_id=imm.message_id INNER JOIN st_lms_user_master um ON md.creator_user_id=um.user_id INNER JOIN st_lms_organization_master om ON om.organization_id=um.organization_id WHERE inbox_message_id="+messageId+" AND imm.status IN ('READ', 'UNREAD', 'DELETE');";
		logger.info("fetchWebMessageDetail - "+query);
		ResultSet rs = stmt.executeQuery(query);
		if(rs.next()) {
			messageBean = new MessageInboxBean();
			messageBean.setMessageId(rs.getInt("inbox_message_id"));
			messageBean.setMessageSubject(rs.getString("message_subject"));
			messageBean.setMessageContent(rs.getString("message_body"));
			messageBean.setMessageDate(dateFormat.format(rs.getTimestamp("send_date")));
			messageBean.setMandatoryStatus(rs.getString("is_mandatory"));
			messageBean.setCreatorUserName(rs.getString("name"));
		}

		return messageBean;
	}

	public List<MessageInboxBean> searchWebMessages(int userId, String userType, String text, Connection connection) throws SQLException {
		MessageInboxBean messageBean = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<MessageInboxBean> messageList = new ArrayList<MessageInboxBean>();
		Statement stmt = connection.createStatement();
		//int noOfMessages = getMessageCount("INBOX", userType, "WEB", connection);
		String query = "SELECT inbox_message_id, name, message_subject, message_body, is_mandatory, send_date, imm.status FROM st_lms_user_message_detail md INNER JOIN st_lms_user_inbox_message_mapping imm ON md.message_id=imm.message_id INNER JOIN st_lms_user_message_type_master mtm ON md.message_type_id=mtm.message_type_id INNER JOIN st_lms_user_master um ON md.creator_user_id=um.user_id INNER JOIN st_lms_organization_master om ON om.organization_id=um.organization_id WHERE md.status='ACTIVE' AND imm.status IN ('READ','UNREAD','DELETE') AND mtm.interface_type='WEB' AND is_inbox_message='YES' AND imm.user_id="+userId+" AND (NAME LIKE '%"+text+"%' OR message_subject LIKE '%"+text+"%' OR message_body LIKE '%"+text+"%') ORDER BY send_date DESC;";
		logger.info("fetchWebMessages - "+query);
		ResultSet rs = stmt.executeQuery(query);
		while(rs.next()) {
			messageBean = new MessageInboxBean();
			messageBean.setMessageId(rs.getInt("inbox_message_id"));
			messageBean.setCreatorUserName(rs.getString("name"));
			messageBean.setMessageSubject(rs.getString("message_subject"));
			messageBean.setMessageContent(rs.getString("message_body"));
			messageBean.setMandatoryStatus(rs.getString("is_mandatory"));
			messageBean.setMessageDate(dateFormat.format(rs.getTimestamp("send_date")));
			messageBean.setStatus(rs.getString("status"));
			messageList.add(messageBean);
		}

		return messageList;
	}

	public void updateMessageStatus(int messageId, String status, Connection connection) throws SQLException {
		String query = "UPDATE st_lms_user_message_detail SET STATUS='"+status+"' WHERE message_id="+messageId+";";
		Statement stmt = connection.createStatement();
		logger.info("updateMessageStatus - "+query);
		stmt.executeUpdate(query);
	}

	public void updateUserMessageStatus(List<Integer> messageList, String status, Connection connection) throws SQLException {
		String column = null;
		if("READ".equals(status)) {
			column = "read_date";
		} else if("DELETE".equals(status)) {
			column = "delete_date";
		} else if("REMOVE".equals(status)) {
			column = "remove_date";
		}
		String query = "UPDATE st_lms_user_inbox_message_mapping SET STATUS='"+status+"', "+column+"='"+Util.getCurrentTimeStamp()+"' WHERE inbox_message_id IN "+messageList.toString().replace("[", "(").replace("]", ")")+";";

		Statement stmt = connection.createStatement();
		logger.info("updateMessageStatus - "+query);
		stmt.executeUpdate(query);
	}

	public List<MessageInboxBean> getMessageListForEdit(int messageTypeId, Connection connection) throws SQLException {
		List<MessageInboxBean> messageList = new ArrayList<MessageInboxBean>();
		MessageInboxBean messageBean = null;
		Statement stmt = connection.createStatement();
		String query = "SELECT message_id, message_subject, message_body, message_date, status FROM st_lms_user_message_detail WHERE message_type_id="+messageTypeId+";";
		logger.info("getMessageListForEdit - "+query);
		ResultSet rs = stmt.executeQuery(query);
		while(rs.next()) {
			messageBean = new MessageInboxBean();
			messageBean.setMessageId(rs.getInt("message_id"));
			messageBean.setMessageSubject(rs.getString("message_subject"));
			messageBean.setMessageContent(rs.getString("message_body"));
			messageBean.setMessageDate(Util.getDateTimeFormat(rs.getTimestamp("message_date")));
			messageBean.setStatus(rs.getString("status"));
			messageList.add(messageBean);
		}

		return messageList;
	}

	public MessageInboxBean getNewMessagesStatus(int msgId, int userId, String userType, String interfaceType, Connection connection) throws SQLException {
		MessageInboxBean messageBean = null;
		PreparedStatement pstmt = connection.prepareStatement("SELECT COUNT(*) new_messages, IF(SUM(a)>0,'Y','N') is_popup, IF(SUM(b)>0,'Y','N') is_mandatory FROM (" +
				"SELECT IF(is_popup='YES',1,0) a, IF(is_mandatory='YES',1,0) b FROM st_lms_user_message_detail md INNER JOIN st_lms_user_inbox_message_mapping imm ON md.message_id=imm.message_id INNER JOIN st_lms_user_message_type_master mtm ON mtm.message_type_id=md.message_type_id WHERE " +
				"imm.user_id=? AND md.message_id>? AND user_type=? AND interface_type=? AND expiry_period>? AND type_dev_name IN ('INBOX','REGISTRATION') AND mtm.status='ACTIVE' AND md.status='ACTIVE')aa;");
		pstmt.setInt(1, userId);
		pstmt.setInt(2, msgId);
		pstmt.setString(3, userType);
		pstmt.setString(4,interfaceType );
		pstmt.setTimestamp(5, Util.getCurrentTimeStamp());
		logger.info("getNewMessagesStatus - "+pstmt);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()) {
			messageBean = new MessageInboxBean();
			messageBean.setNewMessageCount(rs.getInt("new_messages"));
			messageBean.setPopupStatus(rs.getString("is_popup"));
			messageBean.setMandatoryStatus(rs.getString("is_mandatory"));
		}

		return messageBean;
	}

	public List<MessageInboxBean> fetchTerminalMessages(int msgId, int userId, String messageType, Connection connection) throws SQLException {
		if("FLASH".equals(messageType)) {
			msgId = 0;
		}

		int messageLimit = Integer.parseInt(Utility.getPropertyValue("TERMINAL_INBOX_MESSAGE_LIMIT"));
		String limitAppender = " ORDER BY message_date DESC LIMIT "+messageLimit;

		List<MessageInboxBean> messageBeanList = new ArrayList<MessageInboxBean>();
		MessageInboxBean messageBean = null;
		PreparedStatement pstmt = connection.prepareStatement("SELECT message_id, message_subject, message_body, message_date, expiry_period expiry_date, TIMESTAMPDIFF(SECOND, message_date, expiry_period) expiry_period, is_popup, is_mandatory, creator_user_id, NAME FROM (" +
				"SELECT md.message_id, message_subject, message_body, message_date, expiry_period, is_popup, is_mandatory, creator_user_id FROM st_lms_user_message_detail md INNER JOIN st_lms_user_inbox_message_mapping imm ON md.message_id=imm.message_id INNER JOIN st_lms_user_message_type_master mtm ON md.message_type_id=mtm.message_type_id WHERE " +
				"imm.user_id=? AND type_dev_name=? AND interface_type='TERMINAL' AND expiry_period>? " +
				"AND mtm.user_type='RETAILER' AND md.message_id>? AND md.status='ACTIVE' AND mtm.status='ACTIVE'"+limitAppender+")aa INNER JOIN (SELECT user_id, NAME FROM st_lms_user_master um INNER JOIN st_lms_organization_master om ON um.organization_id=om.organization_id)bb ON aa.creator_user_id=bb.user_id ORDER BY message_id;");
		pstmt.setInt(1, userId);
		pstmt.setString(2, messageType);
		pstmt.setTimestamp(3, Util.getCurrentTimeStamp());
		pstmt.setInt(4, msgId);
		logger.info("fetchTerminalMessages - "+pstmt);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			messageBean = new MessageInboxBean();
			messageBean.setMessageId(rs.getInt("message_id"));
			messageBean.setMessageSubject(rs.getString("message_subject"));
			messageBean.setMessageContent(rs.getString("message_body"));
			messageBean.setMessageDate(Util.getDateTimeFormat(rs.getTimestamp("message_date")));
			messageBean.setExpiryDate(Util.getDateTimeFormat(rs.getTimestamp("expiry_date")));
			messageBean.setExpiryTimeInSec(rs.getLong("expiry_period"));
			messageBean.setPopupStatus(rs.getString("is_popup"));
			messageBean.setMandatoryStatus(rs.getString("is_mandatory"));
			messageBean.setUserId(rs.getInt("creator_user_id"));
			messageBean.setCreatorUserName(rs.getString("name"));
			messageBeanList.add(messageBean);
		}

		return messageBeanList;
	}
}