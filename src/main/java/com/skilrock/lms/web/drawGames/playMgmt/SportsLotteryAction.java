package com.skilrock.lms.web.drawGames.playMgmt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.common.Utility;

public class SportsLotteryAction extends BaseAction {
	private String json;

	public String getJson() {
		return json;
	}
	public void setJson(String json) {
		this.json = json;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 811948114538661989L;
	public SportsLotteryAction() {
		super(SportsLotteryAction.class.getName());
		
	}
	public void slePurchaseTicketProcess(){
	    String respData = null;
		PrintWriter out = null;
		try {
			respData = getResponseData(json);
			out = response.getWriter();
			
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			out.print(respData);
			out.close();
			out.flush();
		}
		
		
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
