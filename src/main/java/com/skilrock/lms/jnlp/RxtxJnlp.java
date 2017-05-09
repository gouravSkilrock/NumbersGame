/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.skilrock.lms.jnlp;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author SKILUSER1
 */
public class RxtxJnlp extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Log logger = LogFactory.getLog(RxtxJnlp.class);

	// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on
	// the + sign on the left to edit the code.">
	/**
	 * Handles the HTTP <code>GET</code> method.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Handles the HTTP <code>POST</code> method.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Returns a short description of the servlet.
	 * 
	 * @return a String containing servlet description
	 */
	@Override
	public String getServletInfo() {
		return "Short description";
	}// </editor-fold>

	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
	 * methods.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/x-java-jnlp-file;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache"); // HTTP 1.1
		PrintWriter out = response.getWriter();
		try {
			StringBuffer codebaseBuffer = new StringBuffer();
			codebaseBuffer.append(!request.isSecure() ? "http://" : "https://");
			codebaseBuffer.append(request.getServerName());
			if (request.getServerPort() != (!request.isSecure() ? 80 : 443)) {
				codebaseBuffer.append(':');
				codebaseBuffer.append(request.getServerPort());
			}
			codebaseBuffer.append(request.getContextPath());
			codebaseBuffer.append('/');

			logger.debug("Inside RxtxJnlp.java");
			logger.debug("RxtxJnlp.java -> codebase=: "
					+ codebaseBuffer.toString());

			out.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
			out.println("<jnlp codebase=\"" + codebaseBuffer.toString()
					+ "\"  href=\"applets/rxtxS.jnlp\">");
			out.println("   <information>");
			out.println("       <title>rxtx</title>");
			out.println("       <vendor>ajmas</vendor>");
			out.println("       <homepage href=\"http://www.rxtx.org\"/>");
			out
					.println("       <description>Java API for serial port communication</description>");
			out
					.println("       <description kind=\"short\">Java API for serial port communication.</description>");
			out.println("       <offline-allowed/>");
			out.println("       </information>");
			out.println("   <security>");
			out.println("       <all-permissions/>");
			out.println("   </security>");
			out.println("   <resources>");
			out.println("       <jar href=\"applets/RXTXcomm.jar\" />");
			out.println("   </resources>");

			out.println("   <resources>");
			out
					.println("       <jar href=\"applets/apache-mime4j-0.6.jar\" />");
			out.println("   </resources>");
			out.println("   <resources>");
			out
					.println("       <jar href=\"applets/commons-logging-1.1.1.jar\" />");
			out.println("   </resources>");
			out.println("   <resources>");
			out.println("       <jar href=\"applets/httpclient-4.0.1.jar\" />");
			out.println("   </resources>");
			out.println("   <resources>");
			out.println("       <jar href=\"applets/httpcore-4.0.1.jar\" />");
			out.println("   </resources>");
			out.println("   <resources>");
			out.println("       <jar href=\"applets/httpmime-4.0.1.jar\" />");
			out.println("   </resources>");

			out.println("   <resources os=\"Windows\" arch=\"x86\">");
			out
					.println("       <nativelib href=\"applets/rxtxcomm_win.jar\" />");
			out.println("   </resources>");
			out.println("   <resources os=\"Windows\" arch=\"i386\">");
			out
					.println("       <nativelib href=\"applets/rxtxcomm_win.jar\" />");
			out.println("   </resources>");
			out.println("   <resources os=\"Linux\" arch=\"x86\">");
			out
					.println("       <nativelib href=\"applets/rxtxcomm_linux_x86_64.jar\" />");
			out.println("   </resources>");
			out.println("   <resources os=\"Mac OS X\" >");
			out
					.println("       <nativelib href=\"applets/rxtxcomm_mac_X.jar\" />");
			out.println("   </resources>");
			out.println("   <component-desc />");
			out.println("</jnlp>");
		} finally {
			out.close();
		}
	}

}
