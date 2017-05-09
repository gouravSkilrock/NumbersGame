package com.skilrock.lms.coreEngine.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.skilrock.lms.beans.ServiceRequest;
import com.skilrock.lms.beans.ServiceResponse;
import com.skilrock.lms.common.Utility;

public class ServiceDelegateWrapper implements IServiceDelegate {
	private static Logger logger = LoggerFactory.getLogger(ServiceDelegateWrapper.class);
	private static IServiceDelegate serviceDelegate;

	private ServiceDelegateWrapper() {
	}

	public static IServiceDelegate getInstance() {
		if (serviceDelegate == null) {
			synchronized (ServiceDelegateWrapper.class) {
				if (serviceDelegate == null) {
					serviceDelegate = new ServiceDelegateWrapper();
				}
			}
		}

		return serviceDelegate;
	}

	public ServiceResponse getResponse(ServiceRequest sReq) {
		return null;
	}

	public String getResponseString(ServiceRequest sReq) {
		// String urlString = "http://192.168.124.77:8080/SportsLottery/rest/home/" + sReq.getServiceName() + "/" + sReq.getServiceMethod();
		String urlString = Utility.serverWrapperURL + "rest/" + sReq.getServiceName() + "/" + sReq.getServiceMethod();
		StringBuilder reqJson = null;
		HttpURLConnection connection = null;
		BufferedReader in = null;
		try {
			URL url = new URL(urlString);
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("userName", com.skilrock.lms.common.Utility.getPropertyValue("SYSTEM_AUTHENTICATION_USERNAME").trim());
			connection.setRequestProperty("password", com.skilrock.lms.common.Utility.getPropertyValue("SYSTEM_AUTHENTICATION_PASSWORD").trim());
			OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());

			Gson gson = new Gson();
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

		logger.info(reqJson.toString());
		return reqJson.toString();
	}
	
	@Override
	public String getResponseStringForTrainingExpense(ServiceRequest sReq) {
		// TODO Auto-generated method stub
		return null;
	}
}