package com.skilrock.lms.api.pmsMgmt.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.api.pmsMgmt.serviceHandler.PmsDataHandler;
import com.thetransactioncompany.jsonrpc2.JSONRPC2ParseException;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;

public class LmsManagementServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	static Log logger = LogFactory.getLog(LmsManagementServlet.class);

	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("In Do Post");
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		logger.info("In LMS Mgmt Servlet called from PMS");

		BufferedReader reader = request.getReader();
		StringBuilder sb = new StringBuilder();
		String line = reader.readLine();
		while (line != null) {
			sb.append(line + "\n");
			line = reader.readLine();
		}
		reader.close();
		String data = sb.toString();
		logger.info("Data : " + data);

		JSONRPC2Request reqIn = null;
		JSONRPC2Response resOut = null;
		try {
			reqIn = JSONRPC2Request.parse(data);
		} catch (JSONRPC2ParseException e) {
			logger.info(e.getMessage());
		}

		resOut = PmsDataHandler.getDispatcher(reqIn);
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(resOut);
	}
}
