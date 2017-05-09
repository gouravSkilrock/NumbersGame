package com.skilrock.lms.jnlp;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class App extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Log logger = LogFactory.getLog(App.class);

	/**
	 * Constructor of the object.
	 */
	public App() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	@Override
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occurs
	 */
	@Override
	public void init() throws ServletException {
		// Put your code here
	}

	public void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/x-java-jnlp-file;charset=UTF-8");
		// response.setContentType("text/html;charset=UTF-8");
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

			logger.debug("Inside AppletJnlp.java");
			logger.debug("AppletPrinter.java -> codebase=: "
					+ codebaseBuffer.toString());
			out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			out.println("<jnlp spec=\"1.0+\" href=\"\">");
			out.println("   <information>");
			out.println("       <title>File Tranfer Testing Page</title>");
			out.println("       <vendor>WINLOT Kenya Ltd.</vendor>");
			out.println("       <description>Program Receiving file from Retailer POS Device</description>");
			out.println("       <description kind=\"short\">Receiving file from Retailer POS Device</description>");
			out.println("       <offline-allowed/>");
			out.println("   </information>");
			out.println("   <security>");
			out.println("       <all-permissions/>");
			out.println("   </security>");
			out.println("   <resources>");
			out.println("       <j2se version=\"1.5+\"  href=\"http://java.sun.com/products/autodl/j2se\"/>");
			out.println("<jar href=\"" + codebaseBuffer.toString()+ "java1.5/PCPOSAppletTicketEngine.jar\" main=\"true\" />");
			out.println("       <jar href=\"" + codebaseBuffer.toString()+ "applets/gson-2.2.2.jar\" main=\"true\" />");
			out.println("   </resources>");
			out.println("   <applet-desc name=\"File Tranfer Testing\" main-class=\"PCPOSAppletTicketEngine\" width=\"200\" height=\"500\">");
			out.println("       <param name=\"data\" value=\"108172000002746000\"/>");
			out.println("   </applet-desc>");
			out.println("   <update check=\"background\"/>");
			out.println("</jnlp>");
		} finally {
			out.close();
		}
	}

}
