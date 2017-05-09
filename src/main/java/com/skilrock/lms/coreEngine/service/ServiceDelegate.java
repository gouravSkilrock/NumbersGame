package com.skilrock.lms.coreEngine.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.skilrock.lms.beans.ServiceRequest;
import com.skilrock.lms.beans.ServiceResponse;
import com.skilrock.lms.common.Utility;



public class ServiceDelegate implements IServiceDelegate {
	private static ServiceDelegate serviceDelegate;
	// on lines of Singleton pattern
	static {
		serviceDelegate = new ServiceDelegate();
	}

	static public ServiceDelegate getInstance() {
		return serviceDelegate;
	}


	private ServiceDelegate() {
		// Blank implementation
	}

	/**
	 * This method invokes the SessionBean method.
	 * 
	 * @param sReq -
	 *            ServiceRequest coarse grain object containing bean data
	 * @return - ServiceResponse object containing bean data
	 */
	public ServiceResponse getResponse(ServiceRequest sReq) {

		String urlString = Utility.serverDrawGameURL+"services/"
				+ sReq.getServiceName() + "/" + sReq.getServiceMethod();
		ServiceResponse obj = new ServiceResponse();
		StringBuilder reqJson = null;
		HttpURLConnection connection=null;
		BufferedReader in = null;
		try {
			URL url = new URL(urlString);
			 connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("userName",
					"E49B4EF3-1511-4B8B-86D2-CE78DA0F74D6");
			connection.setRequestProperty("password", "p@55w0rd");
			/*
			 * connection.setRequestProperty( "Cookie","JSESSIONID=" +
			 * "12ECA807E0C31320DE59FD6E355369A");
			 */
			OutputStreamWriter out = new OutputStreamWriter(
					connection.getOutputStream());

			Gson gson = new Gson();
			String json = gson.toJson(sReq.getServiceData());

			out.write(json);
			out.close();

			int responseCode = connection.getResponseCode();
			reqJson = new StringBuilder("");
			if (responseCode == 200) {
				in = new BufferedReader(new InputStreamReader(
						connection.getInputStream()));
				String responseString;
				while ((responseString = in.readLine()) != null) {
					reqJson.append(responseString);
				}

			} else {
				in = new BufferedReader(new InputStreamReader(
						connection.getErrorStream()));
				String responseString;
				while ((responseString = in.readLine()) != null) {
					reqJson.append(responseString);
				}
				System.out.println(reqJson);
				return null;
			}
			// Gson gson = new Gson();

			JsonParser parser = new JsonParser();
			JsonObject o = (JsonObject) parser.parse(reqJson.toString());
			obj.setIsSuccess(o.get("isSuccess").getAsBoolean());
			obj.setResponseData(o.get("responseData"));
			if(!(o.get("errorMessage") == null)){
				obj.setErrorCode(o.get("errorCode").getAsInt());
				obj.setErrorMessage(o.get("errorMessage").getAsString());
			}
			// obj = gson.fromJson(decodedString, ServiceResponse.class);
			//System.out.println(obj);

			in.close();
		} catch (MalformedURLException e) {
			// TODO: handle exception
		} catch (ProtocolException e) {
			// TODO: handle exception
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
		return obj;

	}


	public String getResponseString(ServiceRequest sReq) {

		String urlString = Utility.serverDrawGameURL+"services/"
				+ sReq.getServiceName() + "/" + sReq.getServiceMethod();

		StringBuilder reqJson = null;
		HttpURLConnection connection=null;
		BufferedReader in = null;
		try {
			URL url = new URL(urlString);
			connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("userName",
					"E49B4EF3-1511-4B8B-86D2-CE78DA0F74D6");
			connection.setRequestProperty("password", "p@55w0rd");
			/*
			 * connection.setRequestProperty( "Cookie","JSESSIONID=" +
			 * "12ECA807E0C31320DE59FD6E355369A");
			 */
			//long t1 = System.currentTimeMillis();
			OutputStreamWriter out = new OutputStreamWriter(
					connection.getOutputStream());

			Gson gson = new Gson();
			String json = gson.toJson(sReq.getServiceData());

			out.write(json);
			out.close();
			//System.out.println("Time for Parse Keno Request Bean (LMS) - "+(System.currentTimeMillis()-t1));

			int responseCode = connection.getResponseCode();
			reqJson = new StringBuilder("");
			if (responseCode == 200) {
				in = new BufferedReader(new InputStreamReader(
						connection.getInputStream()));
				String responseString;
				while ((responseString = in.readLine()) != null) {
					reqJson.append(responseString);
				}

			} else {
				in = new BufferedReader(new InputStreamReader(
						connection.getErrorStream()));
				String responseString;
				while ((responseString = in.readLine()) != null) {
					reqJson.append(responseString);
				}
				//System.out.println(reqJson);
				return null;
			}
			// Gson gson = new Gson();

			/*JsonParser parser = new JsonParser();
			JsonObject o = (JsonObject) parser.parse(reqJson.toString());
			obj.setIsSuccess(o.get("isSuccess").getAsBoolean());
			obj.setResponseData(o.get("responseData"));*/

			// obj = gson.fromJson(decodedString, ServiceResponse.class);
			//System.out.println(obj);

			in.close();
		} catch (MalformedURLException e) {
			// TODO: handle exception
		} catch (ProtocolException e) {
			// TODO: handle exception
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
		return reqJson.toString();

	}


	@Override
	public String getResponseStringForTrainingExpense(ServiceRequest sReq) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * This method initializes the remote EJB.
	 * 
	 * @return - the remote class object.
	 */
	

}
