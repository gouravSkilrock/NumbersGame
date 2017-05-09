package com.skilrock.lms.middleware.sle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.rest.services.common.daoImpl.TpEBetMgmtDaoImpl;


@Path("/tpSlMgmt")
public class TpSlMgmt {
	private static Logger logger = LoggerFactory.getLogger(TpSlMgmt.class);
	
	@Path("/slePurchaseTicket")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	
	public String slePurchaseTicket(String requestData){
		  String respData = null;
		try {
			logger.info("Request Data{}",requestData);
			if(requestData == null || requestData.trim().isEmpty()) {
				throw new LMSException(LMSErrors.NO_REQUEST_DATA_PROVIDED_ERROR_CODE,LMSErrors.NO_REQUEST_DATA_PROVIDED_ERROR_MESSAGE);
			}
			respData = getResponseData(requestData);
			
		} catch (Exception e) {
			logger.error("Exception {}",e);
			e.printStackTrace();
		} 
		return respData;
	}
	
	public String getResponseData(String json) throws IOException {
		//reqData = URLEncoder.encode(reqData, "UTF-8");
		String urlString = Utility.serverSLEURL+"com/skilrock/sle/web/merchantUser/playMgmt/action/sportsLotteryPurchaseTicket.action";
		StringBuilder reqJson = null;
		HttpURLConnection connection=null;
		BufferedReader in = null;
		String respData = null;
		//String[] arr = null;
		try {
			URL url = new URL(urlString);
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
          
			
			//reqData = reqData.substring(reqData.split("%")[1].split("\\$")[0].length()+1);
			/*reqData = reqData.split("|")[1];
			arr = reqData.split("\\$");
			reqData = reqData.substring(reqData.split("\\$")[0].length()+1);*/
			//DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			//wr.writeBytes(reqData);
			//wr.flush();
			//wr.close();
		//	json = URLEncoder.encode(json, "UTF-8");
			out.write("requestData="+json);
			out.close();
			int responseCode = connection.getResponseCode();
			reqJson = new StringBuilder("");
			if (responseCode == 200) {
				in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String responseString;
				while ((responseString = in.readLine()) != null) {
					reqJson.append(responseString);
				}

			} else {
				in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
				String responseString;
				while ((responseString = in.readLine()) != null) {
					reqJson.append(responseString);
				}
				System.out.println(reqJson);
				return null;
			}
			respData = reqJson.toString();
			in.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try{
			if(in != null){
				in.close();
			}
			if(connection != null){
				connection.disconnect();
			}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return respData;
		
	}
	
	
	
	
	
	

	

}
