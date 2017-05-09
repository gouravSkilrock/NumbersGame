package com.skilrock.lms.coreEngine.messageMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.InboxMessageBean;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.web.drawGames.common.Util;

public class InboxMessageMgmtHelper {
	private static final Log logger = LogFactory.getLog(InboxMessageMgmtHelper.class);

	public InboxMessageBean getNewMessagesStatus(int msgId, int userId, String userType, String interfaceType, Connection connection) throws LMSException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		InboxMessageBean messageBean = null;
		try {
			pstmt = connection.prepareStatement("SELECT COUNT(*) new_messages, IF(SUM(a)>0,'Y','N') is_popup, IF(SUM(b)>0,'Y','N') is_mandatory FROM (" +
					"SELECT IF(is_popup='YES',1,0) a, IF(is_mandatory='YES',1,0) b FROM " +
					"st_lms_user_message_detail umd INNER JOIN st_lms_user_inbox_message_mapping imm ON umd.message_id=imm.message_id " +
					"AND (imm.user_id=? OR imm.user_id=-1) AND umd.message_id>? AND imm.status='ACTIVE' AND umd.status='ACTIVE' " +
					"AND user_type=? AND interface_type=? AND message_type='INBOX' AND expiry_period>?)aa;");
			pstmt.setInt(1, userId);
			pstmt.setInt(2, msgId);
			pstmt.setString(3, userType);
			pstmt.setString(4,interfaceType );
			pstmt.setTimestamp(5, Util.getCurrentTimeStamp());
			logger.info("New Messages Detail - "+pstmt);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				messageBean = new InboxMessageBean();
				messageBean.setMessageId(rs.getInt("new_messages"));
				messageBean.setIsPopup(rs.getString("is_popup"));
				messageBean.setIsMandatory(rs.getString("is_mandatory"));
			}
		} catch (SQLException se) {
			logger.error("Exception",se);
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			logger.error("Exception",e);
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}

		return messageBean;
	}

	public List<InboxMessageBean> fetchUserWiseMessage(int msgId, int userId, String userType, String interfaceType, String messageType) throws LMSException {
		List<InboxMessageBean> messageBeanList = null;
		Connection connection = null;
		try {
			connection = DBConnect.getConnection();
			messageBeanList = fetchUserWiseMessage(msgId, userId, userType, interfaceType, messageType, connection);
		} catch (Exception e) {
			logger.error("Exception",e);
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeCon(connection);
		}

		return messageBeanList;
	}

	public List<InboxMessageBean> fetchUserWiseMessage(int msgId, int userId, String userType, String interfaceType, String messageType, Connection connection) throws LMSException {
		int messageLimit = 0;
		String limitAppender = "";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		SimpleDateFormat simpleDateFormat = null;
		List<InboxMessageBean> messageBeanList = new ArrayList<InboxMessageBean>();
		InboxMessageBean messageBean = null;
		try {
			if("FLASH".equals(messageType)) {
				msgId = 0;
			}
	
			if("TERMINAL".equals(interfaceType)) {
				messageLimit = Integer.parseInt(Utility.getPropertyValue("TERMINAL_INBOX_MESSAGE_LIMIT"));
				limitAppender = " ORDER BY message_date DESC LIMIT "+messageLimit;
			}
			simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			pstmt = connection.prepareStatement("SELECT message_id, message_subject, message_body, message_date, expiry_period expiry_date, TIMESTAMPDIFF(SECOND, message_date, expiry_period) expiry_period, is_popup, is_mandatory, creator_user_id, user_name, user_type, STATUS FROM (" +
					"SELECT umd.message_id, message_subject, message_body, message_date, expiry_period, IF(is_popup='YES','Y','N') is_popup, IF(is_mandatory='YES','Y','N') is_mandatory, creator_user_id, user_type, umd.status " +
					"FROM st_lms_user_message_detail umd INNER JOIN st_lms_user_inbox_message_mapping imm ON umd.message_id=imm.message_id " +
					"AND (imm.user_id=? OR imm.user_id=-1) AND umd.message_id>? AND imm.status='ACTIVE' AND umd.status='ACTIVE' AND user_type=? " +
					"AND interface_type=? AND expiry_period>? AND message_type=?"+limitAppender+")aa INNER JOIN (" +
					"SELECT user_id, user_name FROM st_lms_user_master um)bb ON aa.creator_user_id=bb.user_id ORDER BY message_id;");
			pstmt.setInt(1, userId);
			pstmt.setInt(2, msgId);
			pstmt.setString(3, userType);
			pstmt.setString(4,interfaceType );
			pstmt.setTimestamp(5, Util.getCurrentTimeStamp());
			pstmt.setString(6, messageType);
			logger.info("fetch Messages Detail - "+pstmt);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				messageBean = new InboxMessageBean();
				messageBean.setMessageId(rs.getInt("message_id"));
				messageBean.setMessageSubject(rs.getString("message_subject"));
				messageBean.setMessageBody(rs.getString("message_body"));
				messageBean.setMessageDate(simpleDateFormat.format(rs.getTimestamp("message_date")));
				messageBean.setExpiryDate(simpleDateFormat.format(rs.getTimestamp("expiry_date")));
				messageBean.setExpiryPeriod(rs.getInt("expiry_period"));
				messageBean.setIsPopup(rs.getString("is_popup"));
				messageBean.setIsMandatory(rs.getString("is_mandatory"));
				messageBean.setCreatorUserId(rs.getInt("creator_user_id"));
				messageBean.setCreatorUserName(rs.getString("user_name"));
				messageBean.setUserType(rs.getString("user_type"));
				messageBean.setStatus(rs.getString("STATUS"));
				messageBeanList.add(messageBean);
			}
		} catch (SQLException se) {
			logger.error("Exception",se);
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			logger.error("Exception",e);
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}

		return messageBeanList;
	}

	public void addFlashInboxMessages(String subject, String content, Timestamp expiryPeriod, String messageType,
			String isPopup, String isMandatory, int userId, String userType, String interfaceType, String[] retName) throws LMSException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int messageId = 0;
		int messageTypeId = 0;
		try {
			connection = DBConnect.getConnection();
			connection.setAutoCommit(false);

			pstmt = connection.prepareStatement("SELECT message_type_id FROM st_lms_user_message_type_master WHERE type_dev_name=? AND user_type=? AND interface_type=? AND status='ACTIVE';");
			pstmt.setString(1, messageType);
			pstmt.setString(2, userType);
			pstmt.setString(3, interfaceType);
			logger.info("Select messageTypeId From st_lms_user_message_type_master - "+pstmt);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				messageTypeId = rs.getInt("message_type_id");
			} else {
				throw new LMSException("Messaging Not Allowed.");
			}

			pstmt = connection.prepareStatement("INSERT INTO st_lms_user_message_detail (message_type_id, message_subject, message_body, message_date, expiry_period, is_popup, is_mandatory, creator_user_id, status) VALUES (?,?,?,?,?,?,?,?,?);", PreparedStatement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, messageTypeId);
			pstmt.setString(2, subject);
			pstmt.setString(3, content);
			pstmt.setTimestamp(4, Util.getCurrentTimeStamp());
			pstmt.setTimestamp(5, expiryPeriod);
			pstmt.setString(6, isPopup);
			pstmt.setString(7, isMandatory);
			pstmt.setInt(8, userId);
			pstmt.setString(9, "ACTIVE");
			logger.info("INSERT IN st_lms_user_message_detail - "+pstmt);
			pstmt.executeUpdate();

			if(!"REGISTRATION".equals(messageType)) {
				rs = pstmt.getGeneratedKeys();
				if(rs.next()) {
					messageId = rs.getInt(1);
				}

				addInboxMessageMapping(messageId, retName, connection);
				/*pstmt = connection.prepareStatement("INSERT INTO st_lms_user_inbox_message_mapping (message_id, user_id, send_date, status) VALUES (?,?,?,?,?,?,?);");
				for(String retailer : retName) {
					int retUserId = Integer.parseInt(retailer.split("~")[0]);
					pstmt.setInt(1, messageId);
					pstmt.setInt(2, retUserId);
					pstmt.setTimestamp(3, Util.getCurrentTimeStamp());
					pstmt.setString(4, "UNREAD");
					pstmt.addBatch();
				}
				pstmt.executeBatch();*/
			}

			connection.commit();
		} catch (SQLException se) {
			logger.error("Exception",se);
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			logger.error("Exception",e);
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeCon(connection);
		}
	}

	public void addInboxMessageMapping (int messageId, String[] retName, Connection connection) throws LMSException {
		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareStatement("INSERT INTO st_lms_user_inbox_message_mapping (message_id, user_id, send_date, status) VALUES (?,?,?,?);");
			for(String retailer : retName) {
				int retUserId = Integer.parseInt(retailer.split("~")[0]);
				pstmt.setInt(1, messageId);
				pstmt.setInt(2, retUserId);
				pstmt.setTimestamp(3, Util.getCurrentTimeStamp());
				pstmt.setString(4, "UNREAD");
				pstmt.addBatch();
			}
			pstmt.executeBatch();
		} catch (SQLException se) {
			logger.error("Exception",se);
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			logger.error("Exception",e);
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}

	public void insertRegistrationMessages(int retailerId) throws LMSException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		try {
			connection = DBConnect.getConnection();
			pstmt = connection.prepareStatement("INSERT INTO st_lms_user_inbox_message_mapping (message_id, user_id, message_date, status) VALUES (?,?,?,?);");
			pstmt.setInt(2, retailerId);
			pstmt.setTimestamp(3, Util.getCurrentTimeStamp());
			pstmt.setString(4, "UNREAD");
			pstmt.executeUpdate();
		} catch (SQLException se) {
			logger.error("Exception",se);
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			logger.error("Exception",e);
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeCon(connection);
		}
	}

	public List<InboxMessageBean> getAllMessagesByStatus(String status) throws Exception {
		Connection connection = null;
		Statement stmt = null;
		ResultSet rs = null;
		SimpleDateFormat dateFormat = null;
		List<InboxMessageBean> messageList = new ArrayList<InboxMessageBean>();
		InboxMessageBean messageBean = null;
		try {
			dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			connection = DBConnect.getConnection();
			stmt = connection.createStatement();
			String appender = null;
			if(status == null) {
				appender = "";
			} else if("ACTIVE".equals(status)) {
				appender = " WHERE status='ACTIVE'";
			} else if("INACTIVE".equals(status)) {
				appender = " WHERE status='INACTIVE'";
			} else {
				throw new LMSException("Enter Valid Status.");
			}
			String query = "SELECT message_id, message_subject, message_body, message_date, expiry_period, type_dev_name, is_popup, is_mandatory, creator_user_id, user_type, interface_type, md.status FROM st_lms_user_message_detail md INNER JOIN st_lms_user_message_type_master imm ON md.message_type_id=imm.message_type_id"+appender+";";
			logger.info("getAllMessagesByStatus - "+query);
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				messageBean = new InboxMessageBean();
				messageBean.setMessageId(rs.getInt("message_id"));
				messageBean.setMessageSubject(rs.getString("message_subject"));
				messageBean.setMessageBody(rs.getString("message_body"));
				messageBean.setMessageDate(dateFormat.format(rs.getTimestamp("message_date")));
				Timestamp expiryDate = rs.getTimestamp("expiry_period");
				if(expiryDate != null) {
					messageBean.setExpiryDate(dateFormat.format(expiryDate));
				} else {
					messageBean.setExpiryDate("");
				}
				messageBean.setMessageType(rs.getString("type_dev_name"));
				messageBean.setIsPopup(rs.getString("is_popup"));
				messageBean.setIsMandatory(rs.getString("is_mandatory"));
				messageBean.setCreatorUserId(rs.getInt("creator_user_id"));
				messageBean.setUserType(rs.getString("user_type"));
				messageBean.setInterfaceType(rs.getString("interface_type"));
				messageBean.setStatus(rs.getString("status"));
				messageList.add(messageBean);
			}
		} catch (SQLException se) {
			logger.error("Exception",se);
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			logger.error("Exception",e);
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeCon(connection);
		}

		return messageList;
	}

	public void editInboxMessage(InboxMessageBean messageBean) throws LMSException {
		Connection connection = null;
		Statement stmt = null;
		try {
			connection = DBConnect.getConnection();
			connection.setAutoCommit(false);
			stmt = connection.createStatement();
			String query = "INSERT INTO st_lms_user_message_detail_history " +
					"(message_id, message_subject, message_body, message_date, expiry_period, message_type, is_popup, is_mandatory, creator_user_id, user_type, interface_type, STATUS, update_on) " +
					"SELECT message_id, message_subject, message_body, message_date, expiry_period, message_type, is_popup, is_mandatory, creator_user_id, user_type, interface_type, STATUS, '"+Util.getCurrentTimeStamp()+"' " +
					"FROM st_lms_user_message_detail WHERE message_id="+messageBean.getMessageId()+";";
			logger.info("Insert in History Table - "+query);
			stmt.executeUpdate(query);

			query = "UPDATE st_lms_user_message_detail SET expiry_period='"+messageBean.getExpiryDate()+"', " +
					"is_popup='"+messageBean.getIsPopup()+"', is_mandatory='"+messageBean.getIsMandatory()+"', " +
					"STATUS='"+messageBean.getStatus()+"' WHERE message_id="+messageBean.getMessageId()+";";
			logger.info("editInboxMessage - "+query);
			stmt.executeUpdate(query);
			connection.commit();
		} catch (SQLException se) {
			logger.error("Exception",se);
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			logger.error("Exception",e);
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeCon(connection);
		}
	}
}