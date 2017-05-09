package com.skilrock.lms.coreEngine.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skilrock.lms.beans.ServiceRequest;
import com.skilrock.lms.beans.ServiceResponse;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.instantWin.javaBeans.VerifyTicketRequestBean;

public class ServiceDelegateIW implements IServiceDelegate {
	private static Logger logger = LoggerFactory.getLogger(ServiceDelegateIW.class);
	private static IServiceDelegate serviceDelegate;

	private ServiceDelegateIW() {
	}

	public static IServiceDelegate getInstance() {
		if (serviceDelegate == null) {
			synchronized (ServiceDelegateIW.class) {
				if (serviceDelegate == null) {
					serviceDelegate = new ServiceDelegateIW();
				}
			}
		}
		return serviceDelegate;
	}

	public String getResponseStringForTrainingExpense(ServiceRequest sReq) {
		StringBuilder reqJson = null;
		HttpURLConnection connection = null;
		BufferedReader in = null;
		StringBuilder strBuilder = new StringBuilder(Utility.serverIWURL + "service" + sReq.getServiceName() + sReq.getServiceMethod());
		try {
			strBuilder.append(sReq.getServiceData().toString());
			logger.info("Non Winning IW Amount - URL " + strBuilder.toString());
			URL url = new URL(strBuilder.toString());
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("GET");
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
		logger.info("Non Winning IW Amount - Response " +reqJson.toString());
		return reqJson.toString();
	}

	public String getResponseString(ServiceRequest sReq) {
		StringBuilder reqJson = null;
		HttpURLConnection connection = null;
		BufferedReader in = null;
		VerifyTicketRequestBean verifyTicketRequestBean = (VerifyTicketRequestBean)sReq.getServiceData();
		StringBuilder strBuilder = new StringBuilder(Utility.serverIWURL + sReq.getServiceMethod());
		try {
			strBuilder.append("domainName=").append(verifyTicketRequestBean.getDomainName());
			strBuilder.append("&").append("currencyCode=").append(Utility.getPropertyValue("CURRENCY_SYMBOL"));
			strBuilder.append("&").append("merchantKey=").append(verifyTicketRequestBean.getMerchantKey());
			strBuilder.append("&").append("lmsPlayerId=").append(verifyTicketRequestBean.getLmsPlayerId());
			strBuilder.append("&").append("clientType=").append(verifyTicketRequestBean.getClientType());
			strBuilder.append("&").append("merchantSessionId=").append(verifyTicketRequestBean.getMerchantSessionId());
			strBuilder.append("&").append("ticketNbr=").append(verifyTicketRequestBean.getTicketNbr());
			strBuilder.append("&").append("lang=").append(verifyTicketRequestBean.getLang());
			strBuilder.append("&").append("screenSize=").append(verifyTicketRequestBean.getScreenSize());
			strBuilder.append("&").append("userName=").append(verifyTicketRequestBean.getUserName());
			strBuilder.append("&").append("deviceType=").append(verifyTicketRequestBean.getDeviceType());
			strBuilder.append("&").append("appType=").append(verifyTicketRequestBean.getAppType());
			strBuilder.append("&").append("remarks=").append(verifyTicketRequestBean.getRemarks());
			strBuilder.append("&").append("userType=").append(verifyTicketRequestBean.getUserType());
			
			logger.info("URL TO Call IW is " + strBuilder.toString());

			URL url = new URL(strBuilder.toString());
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("userName", "E49B4EF3-1511-4B8B-86D2-CE78DA0F74D6");
			connection.setRequestProperty("password", "p@55w0rd");
//			OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
//
//			Gson gson = new Gson();
//			String json = gson.toJson(sReq.getServiceData());
//			out.write(json);
//			out.close();

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
	public ServiceResponse getResponse(ServiceRequest sReq) {
		// TODO Auto-generated method stub
		return null;
	}
}