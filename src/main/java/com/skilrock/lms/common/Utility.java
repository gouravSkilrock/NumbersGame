package com.skilrock.lms.common;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.coreEngine.ola.common.OLAConstants;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2Session;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2SessionException;

public class Utility {
	private static Log logger = LogFactory.getLog(Utility.class);

	private static URL serverURL;
	private static URL serverWEBURL;

	public static String serverDrawGameURL=null;
	public static String serverDrawGameWEBURL=null;

	public static String serverSLEURL = null;
	public static String serverSLEWEBURL = null;
	public static String serverIWURL = null;
	public static String serverIWWEBURL = null;
	public static String serverWrapperURL = null;
	public static String serverDGWURL = null;
	private static Map<String,String> lmsPropertyMap =null;
	

	static {
		getWebLinkInfo();

	}
	public static JSONObject sendCallApi(String method, JSONObject params,
			String id) {

		// Create new JSON-RPC 2.0 client session
		JSONRPC2Session mySession = new JSONRPC2Session(serverURL);
		mySession.getOptions().setRequestContentType("application/json");

		JSONRPC2Request req = new JSONRPC2Request(method, params, id);
		JSONRPC2Response response = null;
        logger.info("Method="+method+" params"+params+"");
		try {

			response = mySession.send(req);
			
			// Print response result / error
			if (response.indicatesSuccess()){
				logger.info(response.getResult()
						.toString());
				return (JSONObject) JSONSerializer.toJSON(response.getResult()
						.toString());
			}
			else
				logger.info(response.getError().getMessage());
			return null;

		} catch (JSONRPC2SessionException e) {

			System.err.println(e.getMessage());

			return null;// added To Handle Error By Neeraj

		}

	}


public static JSONRPC2Response sendCallRummyRegApi(String method,JSONObject params,String id){
		
		
		URL url=null;
		try {
			url = new URL(OLAConstants.rummyRegApi);
			
		} catch (MalformedURLException e1) {
		
			e1.printStackTrace();
		}
		JSONRPC2Session mySession = new JSONRPC2Session(url);
		mySession.getOptions().setRequestContentType("application/json");

		JSONRPC2Request req = new JSONRPC2Request(method, params, id);
		JSONRPC2Response response = null;
		
	
		try {
			logger.info("Sending Request for Method:"+method+" With Params:"+params+" having req id :"+id);
			response = mySession.send(req);
			logger.info("Got Reponse for Id :"+response.getID());
			return response;
			
			
		} catch (JSONRPC2SessionException e) {
			logger.error(e.getMessage());
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
			query = "select server_id,server_code,host_address,local_address,protocol,project_name,port from st_lms_server_info_master where status='ACTIVE'";
			pstm = con.prepareStatement(query);
			rs = pstm.executeQuery();

			while (rs.next()) {
				if (rs.getString("server_code").equals("DGE")) {
					String url = rs.getString("protocol") + "://" + rs.getString("host_address") + ":" + rs.getString("port") + "/" + rs.getString("project_name") + "/";
					serverDrawGameURL = url;

					url = rs.getString("local_address") + rs.getString("project_name") + "/";
					serverDrawGameWEBURL = url;
				} else if (rs.getString("server_code").equals("PMS")) {
					String url = rs.getString("protocol") + "://" + rs.getString("host_address") + ":" + rs.getString("port") + "/" + rs.getString("project_name") + "/PlayerManagement";
					serverURL = new URL(url);

					url = rs.getString("local_address") + rs.getString("project_name") + "/";
					serverWEBURL = new URL(url);
				} else if (rs.getString("server_code").equals("SLE")) {
					String url = rs.getString("protocol") + "://" + rs.getString("host_address") + ":" + rs.getString("port") + "/" + rs.getString("project_name") + "/";
					serverSLEURL = url;

					url = rs.getString("local_address") + rs.getString("project_name") + "/";
					serverSLEWEBURL = url;
				} else if ("IW".equals(rs.getString("server_code"))) {
					String url = rs.getString("protocol") + "://" + rs.getString("host_address") + ":" + rs.getString("port") + "/" + rs.getString("project_name") + "/";
					serverIWURL = url;

					url = rs.getString("local_address") + rs.getString("project_name") + "/";
					serverIWWEBURL = url;
				} else if ("WPR".equals(rs.getString("server_code"))) {
					String url = rs.getString("protocol") + "://" + rs.getString("host_address") + ":" + rs.getString("port") + "/" + rs.getString("project_name") + "/";
					serverWrapperURL = url;
				}else if ("DGW".equalsIgnoreCase(rs.getString("server_code"))) {
					String url = rs.getString("protocol") + "://" + rs.getString("host_address") + ":" + rs.getString("port") + "/" + rs.getString("project_name") + "/";
					serverDGWURL = url;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (!con.isClosed() || con != null) {
					DBConnect.closeCon(con);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

public static void setLmsPropertyMap(Map<String, String> lmsPropertyMap) {
	Utility.lmsPropertyMap = lmsPropertyMap;
}
public static String  getPropertyValue(String propName){
		return lmsPropertyMap.get(propName);
}

public String convertJSON(Object object) {
    Gson gson = new Gson();
    return gson.toJson(object);
}

	public static String getPropertyFromDB(String propertyName) {
		Connection connection = null;
		String propertyValue = null;
		try {
			connection = DBConnect.getConnection();
			propertyValue = getPropertyFromDB(propertyName, connection);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(connection);
		}
	
		return propertyValue;
	}
	
	public static String getPropertyFromDB(String propertyName, Connection connection) {
		Statement stmt = null;
		ResultSet rs = null;
		String propertyValue = null;
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery("SELECT value FROM st_lms_property_master WHERE property_dev_name='"+propertyName+"';");
			if(rs.next())
				propertyValue = rs.getString("value");
	
			logger.info("Property : "+propertyName+" - "+propertyValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return propertyValue;
	}
}