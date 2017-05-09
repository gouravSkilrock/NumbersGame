
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>

		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />

		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>


	</head>


	<body>

		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

		<div id="wrap">

			<div id="top_form">
				<h3>
					<s:text name="msg.login.attempt.reset.success" />
					<s:property value="userName" />
					.
				</h3>
				<br />
				<br />
				<s:a href="bo_um_searchAgtRetUser_Menu.action?request_locale=%{#session.WW_TRANS_I18N_LOCALE}">
					<s:text name="label.srch.edit.more.usr" />
				</s:a>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_um_searchAgtRetUser_loginAttemptsResetSuccess.jsp,v $
	$Revision: 1.3 $
</code>