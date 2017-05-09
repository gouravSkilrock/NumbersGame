package com.skilrock.lms.web.loginMgmt;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AfterLogin extends HttpServlet {
	static Log logger = LogFactory.getLog(AfterLogin.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String password = null;
	private HttpServletRequest request;
	ServletContext sc = null;
	private String username = null;
	private String browser;

	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	public String afterLogin() {
		return "success";
	}

	public String adminLogin() {
		return "success";
	}

	public String beforeLogin() {
		logger.info("-insidemethod afterLogin");
		// System.out.println("-insidemethod-");
		System.out.println("-After  insidemethod-" + sc);
		Map httpsSessionMap = (Map) sc.getAttribute("HTTPS_SESSION_MAP");
		HttpSession session = getRequest().getSession();
		if (httpsSessionMap == null) {
			httpsSessionMap = new HashMap();
		}
		session.setAttribute("HTTPS_USERNAME", request.getParameter("username")
				.toLowerCase());
		session
				.setAttribute("HTTPS_PASSWORD", request
						.getParameter("password"));
		session.setAttribute("HTTPS_MACHINE", request.getParameter("macId"));		
		session.setAttribute("BROWSER", request.getParameter("browser"));
		logger.debug(session.getAttribute("BROWSER").toString());
		logger.debug("-Before-" + httpsSessionMap);
		httpsSessionMap.put(session.getId(), session);
		sc.setAttribute("HTTPS_SESSION_MAP", httpsSessionMap);
		logger.debug("-After-" + httpsSessionMap);
		return "success";
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		// doPost(request, response);
		System.out.println("In Get Cannot use GET");
		PrintWriter out = response.getWriter();
		response.setContentType("text/html");
		out.println("Please Login Properly ");

	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		setServletRequest(request);
		beforeLogin();
		if ("admin".equalsIgnoreCase(request.getParameter("username"))) {
			response.sendRedirect(request.getContextPath()
					+ "/com/skilrock/lms/web/loginMgmt/adminLogin.action");
		} else {
			response.sendRedirect(request.getContextPath()
					+ "/com/skilrock/lms/web/loginMgmt/afterLogin.action");
		}
	}

	public String getPassword() {
		return password;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public String getUsername() {
		return username;
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		sc = getServletContext();
	}

	public void setPassword(String value) {
		password = value;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setUsername(String value) {
		username = value;
	}
}
