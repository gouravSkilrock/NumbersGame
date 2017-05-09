package com.skilrock.lms.coreEngine.instantWin.common;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.db.DBConnect;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2Session;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2SessionException;

public class InstantWinUtility {
	private static Log logger = LogFactory.getLog(InstantWinUtility.class);

	private static URL serverSportsLotteryURL;
	private static Map<String,String> lmsPropertyMap =null;

	static {
		/*
		try {
			serverSportsLotteryURL = new URL(
					"http://localhost:8080/SportsLottery/sportsLotteryManagement");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		*/
		getWebLinkInfo();
	}

	/*
	public static void main(String[] args) throws MalformedURLException {
		JSONObject requestObject = new JSONObject();
		requestObject.put("drawId", "1");
		JSONObject responseObject = SportsUtility.sendCallApi("getEventMasterList", requestObject, "1");
		System.out.println(responseObject.get("eventMasterList"));
	}
	*/

	public static JSONObject sendCallApi(String method, JSONObject params,
			String id) {
		JSONRPC2Session mySession = new JSONRPC2Session(serverSportsLotteryURL);
		mySession.getOptions().setRequestContentType("application/json");

		JSONRPC2Request req = new JSONRPC2Request(method, params, id);
		JSONRPC2Response response = null;
        logger.info("Method - "+method+" Params - "+params);
		try {
			response = mySession.send(req);
			if (response.indicatesSuccess()) {
				logger.info(response.getResult().toString());
				return (JSONObject) JSONSerializer.toJSON(response.getResult().toString());
			}
			else {
				logger.info(response.getError().getMessage());
			}
			return null;
		} catch (JSONRPC2SessionException e) {
			e.printStackTrace();
			return null;
		}
	}

	static void getWebLinkInfo() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		String query = null;
		try {
			con = DBConnect.getConnection();
			query = "SELECT server_id, server_code, host_address, protocol, project_name, port FROM st_lms_server_info_master WHERE server_code='SLE' AND status='ACTIVE';";
			pstm = con.prepareStatement(query);
			rs = pstm.executeQuery();
			if (rs.next()) {
				String url = rs.getString("protocol") + "://"
				+ rs.getString("host_address") + ":"
				+ rs.getString("port") + "/"
				+ rs.getString("project_name") + "/sportsLotteryManagement";
				serverSportsLotteryURL = new URL(url);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}
	}

	public static void setLmsPropertyMap(Map<String, String> lmsPropertyMap) {
		InstantWinUtility.lmsPropertyMap = lmsPropertyMap;
	}

	public static String  getPropertyValue(String propName) {
		return lmsPropertyMap.get(propName);
	}
}