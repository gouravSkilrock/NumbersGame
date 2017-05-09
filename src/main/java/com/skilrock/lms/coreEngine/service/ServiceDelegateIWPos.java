package com.skilrock.lms.coreEngine.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.skilrock.lms.beans.ServiceRequest;
import com.skilrock.lms.beans.ServiceResponse;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.web.instantWin.common.InstantWinPCPOS;

public class ServiceDelegateIWPos implements IServiceDelegate {
	Log logger = LogFactory.getLog(InstantWinPCPOS.class);
	private static IServiceDelegate serviceDelegate;

	private ServiceDelegateIWPos(){}

	public static IServiceDelegate getInstance() {
		if (serviceDelegate == null) {
			synchronized (ServiceDelegateIWPos.class) {
				if (serviceDelegate == null) {
					serviceDelegate = new ServiceDelegateIWPos();
				}
			}
		}

		return serviceDelegate;
	}

	public ServiceResponse getResponse(ServiceRequest sReq) {
		return null;
	}

	public String getResponseString(ServiceRequest sReq) {

		//String urlString = "http://192.168.124.118:8380/InstantGameEngine/service/retailer/" + sReq.getServiceName() + "/" + sReq.getServiceMethod();
		String urlString = Utility.serverIWURL+"service/retailer/" + sReq.getServiceName() + "/" + sReq.getServiceMethod();
		StringBuilder reqJson = null;
		HttpURLConnection connection = null;
		BufferedReader in = null;
		try {
			URL url = new URL(urlString);
			logger.info("Request Url "+urlString);
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("userName", "E49B4EF3-1511-4B8B-86D2-CE78DA0F74D6");
			connection.setRequestProperty("password", "p@55w0rd");
			OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());

			Gson gson = new Gson();
			logger.info("Request data "+sReq.getServiceData());
			String json = gson.toJson(sReq.getServiceData());
			out.write(json);
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

				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (connection != null) {
					connection.disconnect();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		logger.info("Response "+reqJson.toString());
		return reqJson.toString();
	}

	@Override
	public String getResponseStringForTrainingExpense(ServiceRequest sReq) {
		// TODO Auto-generated method stub
		return null;
	}
}