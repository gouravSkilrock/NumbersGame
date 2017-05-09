<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%
	response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<script type="text/javascript"
		src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/playMgmt/js/common.js"></script>
	<head>
		<s:head theme="ajax" debug="false" />
		<meta http-equiv="CACHE-CONTROL" content="NO-CACHE" />
		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>


		<style type="text/css">
.outerBorderLeft {
	border-left: 3px solid #000000;
}
</style>

	</head>

	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

		<h1>
			You have Successfully cancel the Withdrawal Request
		</h1>


	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: ola_pending_withdrawal_request_Success.jsp,v $ $Revision:
	1.1.6.1.2.5 $
</code>