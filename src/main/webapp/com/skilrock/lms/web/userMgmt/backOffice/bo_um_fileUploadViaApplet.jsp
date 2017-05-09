
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@page import="com.skilrock.lms.beans.UserInfoBean"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<%
		response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
		response.setHeader("Pragma", "no-cache"); //HTTP 1.0
		response.setDateHeader("Expires", 0); //prevents caching at the proxy server
	%>
	<head>

		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<s:head theme="ajax" debug="false" />
	</head>
	<%
		StringBuffer codebaseBuffer = new StringBuffer();
		codebaseBuffer.append(!request.isSecure() ? "http://" : "https://");
		codebaseBuffer.append(request.getServerName());
		if (request.getServerPort() != (!request.isSecure() ? 80 : 443)) {
			codebaseBuffer.append(':');
			codebaseBuffer.append(request.getServerPort());
		}
		codebaseBuffer.append(request.getContextPath());
		codebaseBuffer.append('/');

		if (request.getHeader("User-Agent").indexOf("Safari/") != -1) {
			response.sendRedirect("/applets/Applet.jnlp");
		}
	%>

	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

		<div id="wrap">

			<div id="top_form">
				<h3>Upload Files</h3>
				<br>
				<applet code="FileTransfer.FTApplet" codebase="<%=codebaseBuffer.toString()%>"
						jnlp_href="applets/Applet.jnlp" width="460" height="200">
					<param name="url" value="<%=codebaseBuffer.toString()%>" />
					<param name="userName" value="<%=((UserInfoBean)session.getAttribute("USER_INFO")).getUserName()%>" />
					<%if(session.getAttribute("RETAILER_LIST_FOR_OFFLINE_FILE_UPLOAD") != null) %>
					<param name="retailerList" value="<%=session.getAttribute("RETAILER_LIST_FOR_OFFLINE_FILE_UPLOAD")%>" />
					
					<%if(session.getAttribute("AGENT_LIST_FOR_OFFLINE_FILE_UPLOAD") != null) %>
					<param name="agentList" value="<%=session.getAttribute("AGENT_LIST_FOR_OFFLINE_FILE_UPLOAD")%>" />
					<param name="userType" value="BO" />
					
				</applet>

			</div>
			
		</div>
	</body>
</html>

<code id="headId" style="visibility: hidden">
$RCSfile: bo_um_fileUploadViaApplet.jsp,v $
$Revision: 1.2 $
</code>