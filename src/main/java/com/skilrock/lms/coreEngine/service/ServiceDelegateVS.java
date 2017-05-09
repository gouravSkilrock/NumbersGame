package com.skilrock.lms.coreEngine.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServiceDelegateVS {
	private static volatile ServiceDelegateVS serviceDelegate;

	private ServiceDelegateVS(){}

	public static ServiceDelegateVS getInstance() {
		if (serviceDelegate == null) {
			synchronized (ServiceDelegateVS.class) {
				if (serviceDelegate == null) {
					serviceDelegate = new ServiceDelegateVS();
				}
			}
		}
		return serviceDelegate;
	}

	public String getResponseInputStream(String req) {
		String urlString = req;
		HttpURLConnection connection = null;
		BufferedReader in = null;
		StringBuilder reqJson = null;
		try {
			URL url = new URL(urlString);
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");

			int responseCode = connection.getResponseCode();
			reqJson = new StringBuilder("");
			if (responseCode == 200) {
				in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String responseString;
				while ((responseString = in.readLine()) != null) {
					reqJson.append(responseString);
				}
			} else {
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
		return reqJson.toString();
	}
}