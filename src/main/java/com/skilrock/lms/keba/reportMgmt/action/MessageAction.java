package com.skilrock.lms.keba.reportMgmt.action;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.web.reportsMgmt.common.ReportUtility;

public class MessageAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	public MessageAction() {
		super(MessageAction.class);
	}

	public JSONObject getActiveMessages() {
		JSONObject responseObject = new JSONObject();
		JSONArray messageArray = new JSONArray();
		JSONObject messageBean = null;
		PrintWriter out = null;
		Connection connection = null;
		Statement statement = null;
		String query = null;
		ResultSet rs = null;

		SimpleDateFormat dateFormat = null;
		try {
			response.setContentType("application/json");
			out = response.getWriter();
			dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			connection = DBConnect.getConnection();
			statement = connection.createStatement();
			query = "SELECT inbox_id, inbox_title, inbox_content, status, display_order, sender_name, messageDate FROM st_lms_message_inbox WHERE status='ACTIVE';";
			rs = statement.executeQuery(query);
			while(rs.next()) {
				messageBean = new JSONObject();
				//messageBean.put("inboxId", rs.getInt("inbox_id"));
				//messageBean.put("inboxTitle", rs.getString("inbox_title"));
				messageBean.put("content", rs.getString("inbox_content"));
				//messageBean.put("status", rs.getString("status"));
				//messageBean.put("displayOrder", rs.getInt("display_order"));
				messageBean.put("senderName", rs.getString("sender_name"));
				//messageBean.put("messageDate", dateFormat.format(rs.getTimestamp("messageDate")));
				//messageBean.put("messageTime", timeFormat.format(rs.getTimestamp("messageDate")));
				messageBean.put("dateTime", dateFormat.format(rs.getTimestamp("messageDate")));
				messageArray.add(messageBean);
			}
			responseObject.put("isSuccess", true);
			responseObject.put("errorMsg", "");
			responseObject.put("errorCode", 0);
			responseObject.put("msgData", messageArray);
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
			responseObject.put("errorMsg", "Exception Occured.");
			responseObject.put("isSuccess", false);
			return responseObject;
		} finally {
			if (responseObject.isEmpty()) {
				responseObject.put("errorMsg", "Compile Time Error.");
				responseObject.put("isSuccess", false);
			}
			logger.info("Message Inbox Response Data : " + responseObject);
			out.print(responseObject);
			out.flush();
			out.close();
		}

		return responseObject;
	}

	public static void main(String[] args) {
		System.out.println(new MessageAction().getActiveMessages());
	}
}