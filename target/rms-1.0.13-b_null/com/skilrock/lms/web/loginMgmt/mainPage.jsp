<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	response.setDateHeader("Expires",0);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>



		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />

		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />

		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	</head>
	<body topmargin="0" leftmargin="0" bottommargin="0">



		<%@include file="menu.jsp"%>
		<br />

<span>
<%-- <div align="right"><img src="<%=request.getContextPath() %>/LMSImages/images/suaglDamaniWaterMark.gif"/></div> --%>
</span>
</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: mainPage.jsp,v $
$Revision: 1.2 $
</code>