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
public class AppletJnlp extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Log logger = LogFactory.getLog(AppletJnlp.class);

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

			StringBuffer rxtxBuffer = new StringBuffer();
			rxtxBuffer.append(!request.isSecure() ? "http://" : "https://");
			rxtxBuffer.append(request.getServerName());
			if (request.getServerPort() != (!request.isSecure() ? 80 : 443)) {
				rxtxBuffer.append(':');
				rxtxBuffer.append(request.getServerPort());
			}
			rxtxBuffer.append(request.getContextPath());
			rxtxBuffer.append('/');
			rxtxBuffer.append("applets/rxtxS.jnlp");

			logger.debug("Inside AppletJnlp.java");
			logger.debug("AppletJnlp.java -> codebase=: "
					+ codebaseBuffer.toString());

			out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			out.println("<jnlp spec=\"1.0+\" codebase=\""
					+ codebaseBuffer.toString()
					+ "\"  href=\"applets/Applet.jnlp\">");
			out.println("   <information>");
			out.println("       <title>File Tranfer Testing Page</title>");
			out.println("       <vendor>WINLOT Kenya Ltd.</vendor>");
			out
					.println("       <description>Program Receiving file from Retailer POS Device</description>");
			out
					.println("       <description kind=\"short\">Receiving file from Retailer POS Device</description>");
			out.println("       <offline-allowed/>");
			out.println("   </information>");
			out.println("   <security>");
			out.println("       <all-permissions/>");
			out.println("   </security>");
			out.println("   <resources>");
			out
					.println("       <j2se version=\"1.5+\"  href=\"http://java.sun.com/products/autodl/j2se\"/>");
			out.println("       <jar href=\"" + codebaseBuffer.toString()
					+ "applets/FTApplet.jar\" main=\"true\" />");
			out.println("       <extension name=\"rxtx\" href=\""
					+ rxtxBuffer.toString() + "\" />");
			out.println("   </resources>");
			out
					.println("   <applet-desc name=\"File Tranfer Testing\" main-class=\"FileTranfer.FTApplet\" width=\"800\" height=\"400\">");
			out.println("       <param name=\"url\" value=\""
					+ codebaseBuffer.toString() + "\"/>");
			out.println("   </applet-desc>");
			out.println("   <update check=\"background\"/>");
			out.println("</jnlp>");
		} finally {
			out.close();
		}
	}

}
